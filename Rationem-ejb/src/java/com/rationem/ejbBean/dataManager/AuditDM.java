/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import javax.persistence.PersistenceContext;
import com.rationem.entity.audit.AuditGlAccount;
import com.rationem.busRec.audit.AuditGlAccountRec;
import java.util.ListIterator;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import com.rationem.entity.audit.AuditBase;
import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.audit.AuditDocFiRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.audit.*;
import java.util.ArrayList;
import com.rationem.exception.BacException;
import java.util.HashMap;
import static java.util.logging.Level.INFO;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;

/**
 * Singleton Manages database access for Audit manager
 * @author Chris
 */
@Stateless
@LocalBean
public class AuditDM {
  private static final Logger logger =
            Logger.getLogger("com.rationem.ejbBean.dataManager.AuditDM");

  @EJB
  SysBuffer sysBuff;

  @EJB
  FiMasterRecordDM fiMastDM;

  @EJB
  UserDM usrDm;

  private EntityManager em;
  private EntityTransaction trans;
  
  @PostConstruct
  void init(){
   logger.log(INFO,  "Loaded config data manager");
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();

    }

  private AuditBaseRec buildAuditRec(AuditBase aud){
   AuditBaseRec retRec = new AuditBaseRec();
   retRec.setAuditDate(aud.getAuditDate());
   UserRec crBy = this.usrDm.getUserRecPvt(aud.getCreatedBy());
   retRec.setCreatedBy(crBy);
   retRec.setFieldCode(aud.getFieldName());
   retRec.setId(aud.getId());
   retRec.setNewValue(aud.getNewValue());
   retRec.setOldValue(aud.getOldValue());
   retRec.setSource(aud.getSource());
   retRec.setUsrAction(aud.getUsrAction());
   return retRec;
  }
  
  private AuditBaseRec buildAditBaseRec(AuditBaseRec rec, AuditBase aud){
   rec.setFieldName(aud.getFieldName());
   rec.setNewValue(aud.getNewValue());
   rec.setOldValue(aud.getOldValue());
   rec.setSource(aud.getSource());
   rec.setUsrAction(aud.getUsrAction());
   rec.setId(aud.getId());
   rec.setCreatedBy(usrDm.getUserRecPvt(aud.getCreatedBy()));
   rec.setAuditDate(aud.getAuditDate());
   return rec;
  }
  
  
  private AuditBaseRec buildAuditChangeRec(String type,AuditBase change ){
   AuditBaseRec retRec ;
    String className = change.getClass().getSimpleName();
    logger.log(INFO, "Called buildGlAccountChangeRec with className {0} ", className);
   if(className.equalsIgnoreCase("AuditGlAccount")){
     AuditGlAccountRec changeRec = new AuditGlAccountRec();
     AuditGlAccount rec = (AuditGlAccount)change;
     changeRec.setGlAccount(fiMastDM.FiGlAccountCoaRecPvt(rec.getGlAccount()));
     changeRec = (AuditGlAccountRec)buildAditBaseRec(changeRec, change);
     retRec = (AuditBaseRec)changeRec;
   }else{
     AuditGlAccountRec changeRec = new AuditGlAccountRec();
     AuditGlAccount rec = (AuditGlAccount)change;
     changeRec.setGlAccount(fiMastDM.FiGlAccountCoaRecPvt(rec.getGlAccount()));
     changeRec = (AuditGlAccountRec)buildAditBaseRec(changeRec, change);
     
     retRec = (AuditBaseRec)changeRec;
   }


   return retRec;
  }

  public ArrayList<AuditBaseRec> allGlAccountChanges() throws BacException {
    logger.log(INFO, "AuditDM allGlAccountChanges called");
    ArrayList<AuditBaseRec> accountChanges = new ArrayList<AuditBaseRec>();
    try{
      Query q = em.createNamedQuery("coaGlAccountAllChanges");
      List changes = q.getResultList();
      ListIterator li = changes.listIterator();
      while(li.hasNext()){
       AuditBase dbRec = (AuditBase)li.next();
       AuditBaseRec rec = buildAuditChangeRec("coa",dbRec);
       accountChanges.add(rec);
      }
    }catch(IllegalArgumentException ex){
      throw new BacException("All Changes query not found ","AGL:01");
    }catch(IllegalStateException ex){
      throw new BacException("Invalid statement type ","AGL:02");
    }catch(QueryTimeoutException ex){
      throw new BacException("Too long ","AGL:03");
    }catch(PessimisticLockException ex){
      throw new BacException("DB locking error 1 ","AGL:04");
    }catch(PersistenceException ex){
      throw new BacException("DB  error  ","AGL:05");
    }
    return accountChanges;
  }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<AuditBaseRec> getDocFiAuditRecs(DocFiRec doc) {
  logger.log(INFO, "AuditDM.getDocFiAuditRecs called with doc {0} lines {1}", new Object[]{
   doc });
  List<AuditBaseRec> audList = new ArrayList<>();
  
  Query docQ = em.createNamedQuery("auditForDocId");
  docQ.setParameter("docId", doc.getId());
  List rs = docQ.getResultList();
  logger.log(INFO, "Audit query returns {0}", rs);
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   AuditBase aud = (AuditBase)li.next();
   logger.log(INFO, "Audit rec {0}", aud);
   AuditBaseRec audRec = this.buildAuditRec(aud);
   audList.add(audRec);
  }
  
   
   
 
  return audList;
 }

 public List<AuditBaseRec> getDocLineAudit(DocLineBaseRec ln) {
  logger.log(INFO, "getDocLineAudit called with {0}", ln);
  List<AuditBaseRec> audList = new ArrayList<AuditBaseRec>();
  Query linQ = em.createNamedQuery("auditForDocLine");
  
  linQ.setParameter("docLineId", ln.getId());
  List  rs = linQ.getResultList();
  ListIterator  li = rs.listIterator();
  while(li.hasNext()){
   AuditBase aud = (AuditBase)li.next();
   logger.log(INFO, "Audit rec {0}", aud);
   AuditBaseRec audRec = this.buildAuditRec(aud);
   audList.add(audRec);
  }
  return audList;
 }
 
}
