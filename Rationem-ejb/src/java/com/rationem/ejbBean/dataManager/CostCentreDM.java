/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.entity.audit.AuditCostAccountDirect;
import com.rationem.entity.audit.AuditCostCentre;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * Cost Centre data manager
 * @author Chris
 */
@Stateless
public class CostCentreDM {
 
 private static final Logger LOGGER =   Logger.getLogger(CostCentreDM.class.getName());
 
 private EntityManager em;
 private EntityTransaction trans;
 
 @EJB
 private SysBuffer buff;
 
 @EJB
 private UserDM userDM;
 
 @EJB
 private CompanyDM compDM;
 
 @EJB
 private MasterDataDM masterDataDM;
 
 @EJB
 private FiMasterRecordDM fiMastRecDM;

 public EntityManager getEm() {
  return em;
 }

 @PostConstruct
 void init(){
   LOGGER.log(INFO,  "Loaded config data manager");
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
 }
 
 private AuditCostAccountDirect buildAuditCostAccountDirect(CostAccountDirect acnt, User usr,
    String pg, char usrAction){
  AuditCostAccountDirect aud = new AuditCostAccountDirect();
  aud.setAuditDate(new Date());
  aud.setCostAccount(acnt);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
 }

   
 
 
 private AuditCostCentre buildAuditCostCentre(CostCentre cc, User usr, String pg, char usrAction){
  AuditCostCentre aud = new AuditCostCentre();
  aud.setAuditDate(new Date());
  aud.setCostCentre(cc);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
 }
 
 
 private CostAccountDirect buildCostAccountDirect(CostAccountDirectRec costAc, String page){
  CostAccountDirect rec ;
  boolean cstAcntChanged = false;
  boolean cstAcntNew = false;
  if(costAc.getId() == null || costAc.getId() == 0 ){
   LOGGER.log(INFO, "Build new DB CostAccountDirect");
   rec = new CostAccountDirect();
   User crUsr = em.find(User.class, costAc.getCreatedBy().getId(), OPTIMISTIC);
   rec.setCreatedBy(crUsr);
   rec.setCreatedOn(costAc.getCreatedOn());
   em.persist(rec);
   AuditCostAccountDirect aud = this.buildAuditCostAccountDirect(rec, crUsr, page, 'I');
   aud.setNewValue(costAc.getRef());
   cstAcntNew = true;
  }else{
   rec = em.find(CostAccountDirect.class, costAc.getId(), OPTIMISTIC);
  }
  if(cstAcntNew){
   if(costAc.getCostCentre() != null){
    CostCentre cc = em.find(CostCentre.class, costAc.getCostCentre().getId());
    rec.setCostCentre(cc);
   }
   
   if(costAc.getGlAccount() != null){
    FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, costAc.getGlAccount().getId());
    rec.setGlAccount(glAcnt);
   }
   if(costAc.getResponsibilityOf() != null){
    PartnerPerson ptnr = em.find(PartnerPerson.class, costAc.getResponsibilityOf().getId());
    rec.setResponsibilityOf(ptnr);
   }
   rec.setName(costAc.getName());
   rec.setDescription(costAc.getDescription());
   rec.setRef(costAc.getRef());
   rec.setValidFrom(costAc.getValidFrom());
   rec.setValidTo(costAc.getValidTo());
  } else{
   //changes made
   User chUsr = em.find(User.class, costAc.getChangedBy().getId());
   Date chDate = new Date();
  
   if(!costAc.getDescription().equals(rec.getDescription())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
    aud.setFieldName("MA_CA_DESCR");
    aud.setNewValue(costAc.getDescription());
    aud.setOldValue(rec.getDescription());
    rec.setDescription(costAc.getDescription());
    cstAcntChanged = true;
   }
  
   if(costAc.getGlAccount() == null && rec.getGlAccount() != null ||
      costAc.getGlAccount() != null && rec.getGlAccount() == null ||
      costAc.getGlAccount() != null && rec.getGlAccount().getId().compareTo(costAc.getGlAccount().getId()) != 0 ){
     AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
     aud.setFieldName("MA_CA_GL_AC");
     FiGlAccountComp glAc = em.find(FiGlAccountComp.class, costAc.getGlAccount().getId(), OPTIMISTIC);
     aud.setNewValue(glAc.getCoaAccount().getRef());
     aud.setOldValue(rec.getGlAccount().getCoaAccount().getRef());
     rec.setGlAccount(glAc);
     cstAcntChanged = true;
   }
   
   if(!costAc.getName().equals(rec.getName())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
     aud.setFieldName("MA_CA_MA_CA_NM");
     FiGlAccountComp glAc = em.find(FiGlAccountComp.class, costAc.getGlAccount().getId(), OPTIMISTIC);
     aud.setNewValue(glAc.getCoaAccount().getRef());
     aud.setOldValue(rec.getGlAccount().getCoaAccount().getRef());
     rec.setName(costAc.getName());
     cstAcntChanged = true;
   }
   
   if(!costAc.getRef().equals(rec.getRef())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
    aud.setFieldName("MA_CA_MA_CA_NM");
    aud.setNewValue(costAc.getRef());
    aud.setOldValue(rec.getRef());
    rec.setRef(costAc.getRef());
    cstAcntChanged = true;
   }
   
   if(costAc.getValidFrom() == null && rec.getValidFrom() != null ||
      costAc.getValidFrom() != null && rec.getValidFrom() == null ||
      !costAc.getValidFrom().equals(rec.getValidFrom())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
    aud.setFieldName("MA_CA_FROM");
    aud.setNewValue(costAc.getValidFrom().toString());
    aud.setOldValue(rec.getValidFrom().toString());
    rec.setValidFrom(costAc.getValidFrom());
    cstAcntChanged = true;
   }
   
   if(costAc.getValidTo() == null && rec.getValidTo() != null ||
      costAc.getValidTo() != null && rec.getValidTo() == null ||
      !costAc.getValidFrom().equals(rec.getValidFrom())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
    aud.setFieldName("MA_CA_TO");
    aud.setNewValue(costAc.getValidTo().toString());
    aud.setOldValue(rec.getValidTo().toString());
    rec.setValidFrom(costAc.getValidTo());
    cstAcntChanged = true;
   }
   
   if(costAc.getResponsibilityOf() == null && rec.getResponsibilityOf() != null ||
      costAc.getResponsibilityOf() != null && rec.getResponsibilityOf() != null ||
      costAc.getResponsibilityOf() != null && !costAc.getResponsibilityOf().getId().equals(rec.getResponsibilityOf().getId())){
    AuditCostAccountDirect aud = buildAuditCostAccountDirect(rec, chUsr, page, 'U');
    aud.setFieldName("MA_CA_RESP");
    aud.setNewValue(costAc.getResponsibilityOf().getNameStructured());
    aud.setOldValue(rec.getResponsibilityOf().getFamilyName() + " "+rec.getResponsibilityOf().getFirstName());
    PartnerPerson newResp = em.find(PartnerPerson.class, costAc.getResponsibilityOf().getId());
    rec.setResponsibilityOf(newResp);
    cstAcntChanged = true;
   }
   if(cstAcntChanged){
    rec.setChangedBy(chUsr);
    rec.setChangedOn(chDate);
   }
  }
  return rec;
  
 }
 private CostAccountDirectRec buildCostAccountDirectRec(CostAccountDirect costAc){
  CostAccountDirectRec rec = new CostAccountDirectRec();
  rec.setId(costAc.getId());
  UserRec crUsr = userDM.getUserRecPvt(costAc.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(costAc.getCreatedOn());
  if(costAc.getChangedBy() != null){
   UserRec chUsr = userDM.getUserRecPvt(costAc.getChangedBy());
  rec.setChangedBy(chUsr);
  rec.setChangedOn(costAc.getChangedOn());
  }
  rec.setDescription(costAc.getDescription());
  if(costAc.getGlAccount() != null){
   FiGlAccountCompRec glAccount = fiMastRecDM.buildFiCompGlAccountRecPvt(costAc.getGlAccount());
   rec.setGlAccount(glAccount);
  }
  rec.setRef(costAc.getRef());
  rec.setName(costAc.getName());
  if(costAc.getResponsibilityOf() != null){
   PartnerPersonRec resp = this.masterDataDM.buildPartnerPersonRecPvt(costAc.getResponsibilityOf());
   rec.setResponsibilityOf(resp);
  }
  if(costAc.getValidFrom() != null){
   rec.setValidFrom(costAc.getValidFrom());
  }
  if(costAc.getValidTo() != null){
   rec.setValidTo(costAc.getValidTo());
  }
  
  return rec;
  
 }
 
 private CostCentre buildCostCentre(CostCentreRec rec,  String pg){
  CostCentre cc;
  boolean newCc = false;
  boolean changedCc = false;
  if(rec.getId() == null){
   cc = new CostCentre();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   cc.setCreatedBy(crUsr);
   cc.setCreatedOn(rec.getCreatedOn());
   getEm().persist(cc);
   AuditCostCentre aud = this.buildAuditCostCentre(cc, crUsr, pg, 'I');
   aud.setNewValue(rec.getRefrence());
   newCc = true;
  }
  else{
   cc = getEm().find(CostCentre.class, rec.getId(), OPTIMISTIC);
  }
  
  if(newCc){
   if(rec.getCostCentreForCompany() != null){
   CompanyBasic comp = getEm().find(CompanyBasic.class, rec.getCostCentreForCompany().getId(), OPTIMISTIC);
   cc.setCostCentreForCompany(comp);
   }
   
   cc.setCostCentreName(rec.getCostCentreName());
   cc.setRefrence(rec.getRefrence());
   if(rec.getResponsibilityOf() != null){
    PartnerPerson resp = getEm().find(PartnerPerson.class, rec.getResponsibilityOf().getId(), OPTIMISTIC);
    cc.setResponsibilityOf(resp);
   }
   
   if(rec.getValidFrom() != null){
    cc.setValidFrom(rec.getValidFrom());
   }
  
   if(rec.getValidTo() != null){
    cc.setValidTo(rec.getValidTo());
   }
  
  }else{
   // changed ?
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   
   if((rec.getCostCentreForCompany() == null && cc.getCostCentreForCompany() != null) ||
      (rec.getCostCentreForCompany() != null && cc.getCostCentreForCompany() == null) ||
      (rec.getCostCentreForCompany() != null && rec.getCostCentreForCompany().getId() != cc.getCostCentreForCompany().getId())){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_COMP");
    
    aud.setNewValue(rec.getCostCentreForCompany().getReference());
    if(cc.getCostCentreForCompany() != null){
     aud.setOldValue(cc.getCostCentreForCompany().getNumber());
    }
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getCostCentreForCompany().getId(), OPTIMISTIC);
    cc.setCostCentreForCompany(comp);
    changedCc = true;
   }
   
   if((rec.getCostCentreName() == null && cc.getCostCentreName() != null ) ||
      (rec.getCostCentreName() != null && cc.getCostCentreName() == null ) ||
      (rec.getCostCentreName() != null && !rec.getCostCentreName().equals(cc.getCostCentreName()) )){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_NAME");
    aud.setNewValue(rec.getCostCentreName());
    aud.setOldValue(cc.getCostCentreName());
    cc.setCostCentreName(rec.getCostCentreName());
    changedCc = true;
   }
   
   if((rec.getRefrence() == null && cc.getRefrence() != null ) ||
      (rec.getRefrence() != null && cc.getRefrence() == null ) ||
      (rec.getRefrence() != null && !rec.getRefrence().equals(cc.getRefrence()) )){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_REF");
    aud.setNewValue(rec.getRefrence());
    aud.setOldValue(cc.getRefrence());
    cc.setRefrence(rec.getRefrence());
    changedCc = true;
   }
   
   if((rec.getResponsibilityOf() == null && cc.getResponsibilityOf() != null) ||
      (rec.getResponsibilityOf() != null && cc.getResponsibilityOf() == null) ||
      (rec.getResponsibilityOf() != null && rec.getResponsibilityOf().getId() != cc.getResponsibilityOf().getId())){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_RESP_PERS");
    
    aud.setNewValue(rec.getResponsibilityOf().getName());
    if(cc.getCostCentreForCompany() != null){
     aud.setOldValue(cc.getResponsibilityOf().getFamilyName());
    }
    PartnerPerson pers = em.find(PartnerPerson.class, rec.getResponsibilityOf().getId(), OPTIMISTIC);
    cc.setResponsibilityOf(pers);
    changedCc = true;
   }
   
   if((rec.getValidFrom() == null && cc.getValidFrom() != null ) ||
      (rec.getValidFrom() != null && cc.getValidFrom() == null ) ||
      (rec.getValidFrom() != null && !rec.getValidFrom().equals(cc.getValidFrom()) )){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_FROM");
    aud.setNewValue(rec.getValidFrom().toString());
    aud.setOldValue(cc.getValidFrom().toString());
    cc.setValidFrom(rec.getValidFrom());
    changedCc = true;
   }
   
   if((rec.getValidTo() == null && cc.getValidTo() != null ) ||
      (rec.getValidTo() != null && cc.getValidTo() == null ) ||
      (rec.getValidTo() != null && !rec.getValidTo().equals(cc.getValidTo()) )){
    AuditCostCentre aud = buildAuditCostCentre(cc, chUsr, pg, 'U');
    aud.setFieldName("MA_CC_To");
    aud.setNewValue(rec.getValidTo().toString());
    aud.setOldValue(cc.getValidTo().toString());
    cc.setValidTo(rec.getValidTo());
    changedCc = true;
   }
   
   if(changedCc){
    cc.setChangedBy(chUsr);
    cc.setChangedOn(new Date());
   }
   
  }
   
  
  return cc;
 }

 public CostCentreRec getCostCentreRec(CostCentre cc){
  return buildCostCentreRec(cc);
 }
 
 private CostCentreRec buildCostCentreRec(CostCentre cc){
  CostCentreRec rec = new CostCentreRec();
  rec.setId(cc.getId());
  UserRec crUser = userDM.getUserRecPvt(cc.getCreatedBy());
  rec.setCreatedBy(crUser);
  rec.setCreatedOn(cc.getCreatedOn());
  if(cc.getChangedBy() != null){
   UserRec chUser = userDM.getUserRecPvt(cc.getChangedBy());
   rec.setChangedBy(chUser);
   rec.setChangedOn(cc.getChangedOn());
  }
  if(cc.getCostCentreForCompany() != null){
   CompanyBasicRec comp = this.compDM.buildCompanyBasicRecPvt(cc.getCostCentreForCompany());
   rec.setCostCentreForCompany(comp);
  }
  rec.setCostCentreName(cc.getCostCentreName());
  rec.setRefrence(cc.getRefrence());
  if(cc.getResponsibilityOf() != null){
   PartnerPersonRec resp = masterDataDM.buildPartnerPersonRecPvt(cc.getResponsibilityOf());
   rec.setResponsibilityOf(resp);
  }
  if(cc.getValidFrom() != null){
   rec.setValidFrom(cc.getValidFrom());
  }
  
  if(cc.getValidTo() != null){
   rec.setValidTo(cc.getValidTo());
  }
  return rec;
 }
 
 
 public CostAccountDirectRec updateCostAccount(CostAccountDirectRec acnt, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  CostAccountDirect account = this.buildCostAccountDirect(acnt, pg);
  if(acnt.getId() == null){
   acnt.setId(account.getId());
  }
  trans.commit();
  return acnt;
 }
 
 public CostCentreRec updateCostCentre(CostCentreRec costCentre, String source) 
         throws BacException {
  if(costCentre.getCreatedBy() == null){
   throw new BacException("Cost Center Create User required");
  }
  if(!trans.isActive()){
   trans.begin();
  }
  CostCentre cc = this.buildCostCentre(costCentre, source);
  
  costCentre.setId(cc.getId());
  LOGGER.log(INFO,"costCentre.setId {0}",costCentre.getId());
  trans.commit();
  
  return costCentre;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public List<CostCentreRec> getAllCostCentres(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "getAllCostCentres called with company {0}", comp);
  
  Query q = em.createNamedQuery("allCostCent");
  q.setParameter("compId", comp.getId());
  List rl = q.getResultList();
  LOGGER.log(INFO, "allCostCent query returned {0}", rl);
  if(rl != null){
   List<CostCentreRec> costCentres = new ArrayList<CostCentreRec>();
   ListIterator li = rl.listIterator();
   while(li.hasNext()){
    CostCentre cc = (CostCentre)li.next();
    CostCentreRec rec = this.buildCostCentreRec(cc);
    costCentres.add(rec);
   }
   return costCentres;
  }
  return null;
 }

 public CostCentreRec getCostCentreByRef(CompanyBasicRec comp, String ref) {
  LOGGER.log(INFO, "CostCentDM.getCostCentreByRef called with comp {0} ref {1}", new Object[]{
   comp, ref});
  
  Query q =em.createNamedQuery("costCentByRef");
  q.setParameter("ref", ref);
  q.setParameter("compId", comp.getId());
  try{
  CostCentre cc = (CostCentre)q.getSingleResult();
  CostCentreRec rec = this.buildCostCentreRec(cc);
  return rec;
  
  }catch(NoResultException ex){
   LOGGER.log(INFO, "no cc found for ref {0}", ref);
   return null;
  }catch(NonUniqueResultException ex){
   LOGGER.log(INFO, "multiple cost centrs for ref {0}", ref);
   return null;
  }
 }
 
 public List<CostCentreRec> getCostCentresByRef(CompanyBasicRec comp, String ref) {
  LOGGER.log(INFO, "CC DM getCostCentresByRef called with comp {0} and ref {1}", 
          new Object[]{comp,ref});
  if(ref == null || ref.isEmpty()){
   ref = "%";
  }else{
   ref = ref + "%";
  }
  LOGGER.log(INFO, "Ref for query {0}", ref);
  Query q = em.createNamedQuery("costCentsByRef");
  q.setParameter("compId", comp.getId());
  q.setParameter("ref", ref);
  List rl = q.getResultList();
  LOGGER.log(INFO, "allCostCent query returned {0}", rl);
  if(rl != null){
   List<CostCentreRec> costCentres = new ArrayList<CostCentreRec>();
   ListIterator li = rl.listIterator();
   while(li.hasNext()){
    CostCentre cc = (CostCentre)li.next();
    CostCentreRec rec = this.buildCostCentreRec(cc);
    costCentres.add(rec);
   }
   return costCentres;
  }
  return null;
 }

 public CostAccountDirectRec getCostAccountRecByCostCentGlAc(FiGlAccountCompRec glAcComp, 
         CostCentreRec costCent) throws BacException {
  LOGGER.log(INFO, "CostCentreDM.getCostAccountRecByCostCentGlAc called with glAc {0} costCent {1}",
          new Object[]{glAcComp,costCent});
  Query q = em.createNamedQuery("costAcDirByCostCentGlAc");
  q.setParameter("costCentId", costCent.getId());
  q.setParameter("glAcId", glAcComp.getId());
  try{
  CostAccountDirect costAc = (CostAccountDirect)q.getSingleResult();
  CostAccountDirectRec rec = this.buildCostAccountDirectRec(costAc);
  return rec;
  }catch(NoResultException ex){
   return null;
  }
  
 }
 
public CostAccountDirectRec getCostAccountRecPvt(CostAccountDirect cstAct){
 CostAccountDirectRec rec = this.buildCostAccountDirectRec(cstAct);
 return rec;
 
}

public List<CostAccountDirectRec> getCostAccountsByCostCent(CostCentreRec cstCent){
 LOGGER.log(INFO, "getCostAccountsByCostCent called with cstCent id:  {0}", cstCent.getId());
 Query q = em.createNamedQuery("costAcsDirByCostCent");
 q.setParameter("ccId", cstCent.getId());
 List<Object> rs = q.getResultList();
 if(rs == null || rs.isEmpty()){
  LOGGER.log(INFO, "DB query return none {0}", rs);
  return null;
 }
 List<CostAccountDirectRec> acnts = new ArrayList<>();
 for(Object o:rs){
  CostAccountDirectRec curr = this.buildCostAccountDirectRec((CostAccountDirect)o);
  acnts.add(curr);
 }
 return acnts;
}
public List<CostCentreRec> getCostCentByCompId(Long compId){
 LOGGER.log(INFO, "getCostCentByCompId called with id {0}", compId);
 Query q = em.createNamedQuery("costCentByCompId");
 q.setParameter("compId", compId);
 List<CostCentreRec> ccList = new ArrayList<>();
 
 List rs = q.getResultList();
 for(Object o:rs){
  CostCentre cc = (CostCentre)o;
  CostCentreRec ccRec = this.buildCostCentreRec(cc);
  ccList.add(ccRec);
 }
 return ccList;
}

public CostAccountDirect getCostAccountByCostCentGlAc(FiGlAccountCompRec glAcComp, 
         CostCentreRec costCent) throws BacException {
  LOGGER.log(INFO, "CostCentreDM.getCostAccountRecByCostCentGlAc called with glAc {0} costCent {1}",
          new Object[]{glAcComp,costCent});
  Query q = em.createNamedQuery("costAcDirByCostCentGlAc");
  q.setParameter("costCentId", costCent.getId());
  q.setParameter("glAcId", glAcComp.getId());
  try{
  CostAccountDirect costAc = (CostAccountDirect)q.getSingleResult();
  return costAc;
  }catch(NoResultException ex){
   LOGGER.log(INFO, "No Cost Centre accnt found");
   return null;
  }
  
 }

 public void copyToComp(CompanyBasicRec compRec1,CompanyBasicRec compRec2, UserRec usr, String pg){
  LOGGER.log(INFO, "CostCentDM.copyToComp called with comp {0}, comp {1}", new Object[]{
   compRec1.getReference(),compRec2.getReference()});
  
  TypedQuery q = em.createNamedQuery("allCostCent",CostCentre.class);
  q.setParameter("compId", compRec1.getId());
  List<CostCentre> rl = q.getResultList();
  LOGGER.log(INFO, "result list {0}", rl);
  
 }
 public CostAccountDirectRec createCostAccountDirect(FiGlAccountCompRec glAc, 
         CostCentreRec costCent, UserRec usr, Date crDate, String pg) throws BacException {
  LOGGER.log(INFO, "CostCentDM.createCostAccountDirect called with glAc.id {0}, cost cent {1}", 
         new Object[]{glAc.getId(),costCent});
  Date endDate = new Date();
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(endDate);
  cal.set(9999, 11, 31);
  endDate = cal.getTime();
  if(!trans.isActive()){
   trans.begin();
  }
  CostAccountDirectRec costAc = new CostAccountDirectRec();
  costAc.setCreatedBy(usr);
  costAc.setCreatedOn(crDate);
  costAc.setGlAccount(glAc);
  costAc.setName(glAc.getCoaAccount().getName());
  costAc.setRef(glAc.getCoaAccount().getRef());
  costAc.setDescription(glAc.getCoaAccount().getDescription());
  costAc.setCostCentre(costCent);
  costAc.setValidFrom(crDate);
  costAc.setValidTo(endDate);
  CostAccountDirect rec = this.buildCostAccountDirect(costAc,pg);
  costAc.setId(rec.getId());
  LOGGER.log(INFO, "After call to create cost Account act is: {0}", costAc);
  LOGGER.log(INFO, "Cost Ac DB {0}", rec.getId());
  trans.commit();
  return costAc;
 }

 
 
 

}
