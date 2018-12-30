/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.entity.audit.AuditProgramme;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.ma.ProgramAccount;
import com.rationem.entity.ma.Programme;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.LocalBean;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;



import static java.util.logging.Level.INFO;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
@ConcurrencyManagement(CONTAINER)
public class ProgrammeDM {
 private static final Logger LOGGER = Logger.getLogger(ProgrammeDM.class.getName());
 
 private EntityManager em;
 private EntityTransaction trans;
 
 @EJB
 private SysBuffer buff;
 
 @EJB
 private UserDM usrDM;
 
 @EJB
 private CompanyDM compDM;
 
 @EJB
 private MasterDataDM masterDataDM;
 
 @EJB
 private FiMasterRecordDM fiMastRecDM;
 
 @PostConstruct
 public void init(){
  LOGGER.log(INFO,  "Loaded Cost Centre DM");
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
 }
 
 private AuditProgramme buildAuditProgramme(Programme prg, User usr, String pg, char usrAction){
  AuditProgramme aud = new AuditProgramme();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setProg(prg);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
 }
 private Programme buildProgramme(ProgrammeRec rec, String pg){
  Programme prog;
  boolean newProg = false;
  boolean changedProg = false;
  
  if(rec.getId() == null ){
   prog = new Programme();
   User usr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   prog.setCreatedBy(usr);
   prog.setCreatedOn(rec.getCreatedOn());
   em.persist(prog);
   AuditProgramme aud = this.buildAuditProgramme(prog, usr, pg, 'I');
   aud.setNewValue(rec.getReference());
   newProg = true;
  }else{
   prog = em.find(Programme.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
  }
  
  if(newProg){
   prog.setBudget(rec.getBudget()); 
   prog.setCost(rec.getCost());
   prog.setName(rec.getName());
   prog.setRefrence(rec.getReference());
   prog.setValidFrom(rec.getValidFrom());
   prog.setValidTo(rec.getValidTo());
   if(rec.getProgrammeForCompany() != null){
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getProgrammeForCompany().getId(), OPTIMISTIC);
    prog.setProgrammeForCompany(comp);
   }
   if(rec.getResponsibilityOf() != null){
    PartnerPerson resp = em.find(PartnerPerson.class, rec.getResponsibilityOf().getId(), OPTIMISTIC);
    prog.setResponsibilityOf(resp);
   }
  }else{
   //Changed ?
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   
   if(rec.getBudget() != prog.getBudget()){
    AuditProgramme aud = this.buildAuditProgramme(prog, chUsr, pg, 'U');
    aud.setFieldName("MA_PROG_BUD");
    aud.setNewValue(String.valueOf(rec.getBudget()));
    aud.setOldValue(String.valueOf(prog.getBudget()));
    prog.setBudget(rec.getBudget());
    changedProg = true;
   }
   
   if(rec.getCost() != prog.getCost()){
    AuditProgramme aud = this.buildAuditProgramme(prog, chUsr, pg, 'U');
    aud.setFieldName("MA_PROG_CST");
    aud.setNewValue(String.valueOf(rec.getCost()));
    aud.setOldValue(String.valueOf(prog.getCost()));
    prog.setCost(rec.getCost());
    changedProg = true;
   }
  
  
  if((rec.getName() == null && prog.getName() != null) ||
     (rec.getName() != null && prog.getName() == null) ||
     (rec.getName() != null && !rec.getName().equals(prog.getName()))){
    AuditProgramme aud = this.buildAuditProgramme(prog, chUsr, pg, 'U');
    aud.setFieldName("MA_PROG_NAME");
    aud.setNewValue(rec.getName());
    aud.setOldValue(prog.getName());
    prog.setName(rec.getName());
    changedProg = true;
  } 
  
  if((rec.getReference() == null && prog.getRefrence() != null) ||
     (rec.getReference() != null && prog.getRefrence() == null) ||
     (rec.getReference() != null && !rec.getReference().equals(prog.getRefrence()))){
    AuditProgramme aud = this.buildAuditProgramme(prog, chUsr, pg, 'U');
    aud.setFieldName("MA_PROG_REF");
    aud.setNewValue(rec.getReference());
    aud.setOldValue(prog.getRefrence());
    prog.setRefrence(rec.getReference());
    changedProg = true;
  }
  
   if(changedProg){
    prog.setChangedBy(chUsr);
    prog.setChangedOn(rec.getChangedOn());
   }
  
  }
  
   
  
  
  
  return prog;
 } 
 
 public ProgrammeRec getProgrammeRec(Programme prog){
  return buildProgrammeRec(prog);
 }
 
 private ProgrammeRec buildProgrammeRec(Programme prog){
  ProgrammeRec rec = new ProgrammeRec();
  rec.setId(prog.getId());
  UserRec crUsr = usrDM.getUserRecPvt(prog.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(prog.getCreatedOn());
  
  rec.setBudget(prog.getBudget());
  if(prog.getChangedBy() != null){
   UserRec chUsr = usrDM.getUserRecPvt(prog.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(prog.getChangedOn());
  }
  rec.setCost(prog.getCost());
  rec.setName(prog.getName());
  if(prog.getProgrammeForCompany() != null){
   CompanyBasicRec comp = this.compDM.buildCompanyBasicRecPvt(prog.getProgrammeForCompany());
   rec.setProgrammeForCompany(comp);
  }
  rec.setReference(prog.getRefrence());
  if(prog.getResponsibilityOf() != null){
   PartnerPersonRec resp = masterDataDM.buildPartnerPersonRecPvt(prog.getResponsibilityOf());
   rec.setResponsibilityOf(resp);
  }
  rec.setValidFrom(prog.getValidFrom());
  rec.setValidTo(prog.getValidTo());
  LOGGER.log(INFO, "buildProgrammeRec returns program with id{0}", rec.getId());
  return rec;
 }
 
 private ProgramAccountRec buildProgramAccountRec(ProgramAccount act){
  ProgramAccountRec rec = new ProgramAccountRec();
  rec.setId(act.getId());
  
  UserRec crUsr = this.usrDM.getUserRecPvt(act.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(act.getCreatedOn());
  if(act.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(act.getChangedBy());
   rec.setCreatedBy(chUsr);
   rec.setCreatedOn(act.getCreatedOn());
  }
  rec.setDescription(act.getDescription());
  if(act.getGlAccount() != null){
   FiGlAccountCompRec glAct = this.fiMastRecDM.buildFiCompGlAccountRecPvt(act.getGlAccount());
   rec.setGlAccount(glAct);
  }
  rec.setName(act.getName());
  rec.setRef(act.getRef());
  if(act.getResponsibilityOf() != null){
   PartnerPersonRec respPers = this.masterDataDM.buildPartnerPersonRecPvt(act.getResponsibilityOf());
   rec.setResponsibilityOf(respPers);
  }
  rec.setValidFrom(act.getValidFrom());
  rec.setValidTo(act.getValidTo());
  return rec;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
public int copyProgrammesToComp(CompanyBasicRec c1, CompanyBasicRec c2, 
        UserRec usrRec, String pg ){
 LOGGER.log(INFO, "ProgDM.copyProgrammesToComp called with comp {0} and {1}", 
         new Object[]{c1.getReference(),c2.getReference()});
 
 if(trans.isActive()){
  trans.begin();
 }
 
 TypedQuery qC2 = em.createNamedQuery("progsByCompId", Programme.class);
 qC2.setMaxResults(1);
 qC2.setParameter("compId", c2.getId());
 List<Programme> rs = qC2.getResultList();
 if(!rs.isEmpty()){
  
  return CompanyManager.ATTR_IN_DEST_COMP;
 }
 TypedQuery qC1 = em.createNamedQuery("progsByCompId", Programme.class);
 qC1.setParameter("compId", c1.getId());
 rs = qC1.getResultList();
 if(rs.isEmpty()){
  return CompanyManager.ATTR_NOT_IN_SRC_COMP;
 }
 CompanyBasic compC2 = em.find(CompanyBasic.class, c2.getId());
 User usr = em.find(User.class, usrRec.getId());
 int numCreated = 0;
 for(Programme pr: rs){
  Programme curr = new Programme();
  curr.setBudget(pr.getBudget());
  curr.setCost(pr.getCost());
  curr.setCreatedBy(usr);
  curr.setCreatedOn(new Date());
  curr.setName(pr.getName());
  curr.setProgrammeForCompany(compC2);
  curr.setRefrence(pr.getRefrence());
  curr.setResponsibilityOf(pr.getResponsibilityOf());
  curr.setValidFrom(pr.getValidFrom());
  curr.setValidTo(pr.getValidTo());
  em.persist(curr);
  AuditProgramme aud = this.buildAuditProgramme(pr, usr, pg, 'I');
  aud.setNewValue(curr.getRefrence());
  numCreated++;
 }
 if(numCreated == 0){
  trans.rollback();
  return CompanyManager.ATTR_NOT_CREATED;
 }else{
  trans.commit();
  return CompanyManager.ATTR_CREATED;
 }
}
 
 public ProgrammeRec updateProgramme(ProgrammeRec programme, UserRec user, String source) 
         throws BacException {
  if(programme.getCreatedBy() == null){
   throw new BacException("Must specify the user that created programme");
  }
  if(!trans.isActive()){
   trans.begin();
  }
  Programme prog = this.buildProgramme(programme, source);
  if(programme.getId() == null){
   programme.setId(prog.getId());
  }
  
  trans.commit();
  return programme;
 }

 public List<ProgrammeRec> getAllProgrammes(CompanyBasicRec comp) {
  LOGGER.log(INFO, "ProgrammDM getAllProgrammes called with company {0}", comp);
  Query q = em.createNamedQuery("allProg");
  q.setParameter("compId", comp.getId());
  List progList = q.getResultList();
  LOGGER.log(INFO, "Result List {0}", progList);
  if(progList == null){
   LOGGER.log(INFO, "No programs found");
   return null;
  }
  List<ProgrammeRec> retList = new ArrayList<ProgrammeRec>();
  ListIterator li = progList.listIterator();
  while(li.hasNext()){
   Programme prog = (Programme)li.next();
   ProgrammeRec rec = this.buildProgrammeRec(prog);
   LOGGER.log(INFO, "Add program id {0} to list",rec.getId());
   retList.add(rec);
  }
  return retList;
 }

 
 public List<ProgrammeRec> getProgrammesByName(CompanyBasicRec comp, String name) {
  LOGGER.log(INFO, "ProgrammDM getAllProgrammes called with company {0} name {1}", new Object[]{comp, name});
  
  LOGGER.log(INFO, "name for query {0}", name);
  Query q = em.createNamedQuery("progByName");
  q.setParameter("compId", comp.getId());
  q.setParameter("name", name);
  List progList = q.getResultList();
  if(progList == null){
   return null;
  }
  List<ProgrammeRec> retList = new ArrayList<ProgrammeRec>();
  ListIterator li = progList.listIterator();
  while(li.hasNext()){
   Programme prog = (Programme)li.next();
   ProgrammeRec rec = this.buildProgrammeRec(prog);
   retList.add(rec);
  }
  return retList;
 }
 
 public ProgrammeRec getProgrammeByRef(CompanyBasicRec comp, String ref){
  
  Query q = em.createNamedQuery("progsByRef");
  q.setParameter("compId", comp.getId());
  q.setParameter("ref", ref);
  
  try{
   Programme p = (Programme)q.getSingleResult();
   ProgrammeRec rec = this.buildProgrammeRec(p);
   return rec;
  }catch(NoResultException ex){
   LOGGER.log(INFO, "Could not find programme with ref {0} ex {1}", new Object[]{ref,ex.getLocalizedMessage()});
   return null;
  }catch(NonUniqueResultException ex){
   LOGGER.log(INFO, "Multiple programms found {0}", ex.getLocalizedMessage());
   return null;
  }
  
  
 }
 
 public ProgrammeRec getProgrammeById(long progId){
  Programme prog = em.find(Programme.class, progId, OPTIMISTIC);
  ProgrammeRec progRec = this.buildProgrammeRec(prog);
  return progRec;
 }
 public List<ProgrammeRec> getProgrammesByRef(CompanyBasicRec comp, String ref) {
  LOGGER.log(INFO, "ProgrammDM getProgrammes for ref called with company {0} ref {1}", new Object[]{comp, ref});
  
  LOGGER.log(INFO, "name for query {0}", ref);
  Query q = em.createNamedQuery("progsByRef");
  q.setParameter("compId", comp.getId());
  q.setParameter("ref", ref);
  List progList = q.getResultList();
  if(progList == null){
   return null;
  }
  
  List<ProgrammeRec> retList = new ArrayList<>();
  ListIterator li = progList.listIterator();
  while(li.hasNext()){
   Programme prog = (Programme)li.next();
   ProgrammeRec rec = this.buildProgrammeRec(prog);
   retList.add(rec);
  }
  return retList;
 }

 public List<ProgrammeRec> getProgrammesForComp(Long compId){
  Query q = em.createNamedQuery("progsByCompId");
  q.setParameter("compId", compId);
  List rs = q.getResultList();
  LOGGER.log(INFO, "getProgrammesForComp", rs);
  if(rs == null){
   return null;
  }
  List<ProgrammeRec> progList = new ArrayList<>();
  for(Object o: rs){
   Programme curr = (Programme)o;
   ProgrammeRec rec = this.buildProgrammeRec(curr);
   progList.add(rec);
  }
  return progList;
 }
 public ProgramAccountRec getProgramAccountRec(ProgramAccount progAc){
  ProgramAccountRec ac = new ProgramAccountRec();
  ac.setId(progAc.getId());
  UserRec crUsr = this.usrDM.getUserRecPvt(progAc.getCreatedBy());
  ac.setCreatedBy(crUsr);
  ac.setCreatedOn(progAc.getCreatedOn());
  if(progAc.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(progAc.getChangedBy());
   ac.setChangedBy(chUsr);
   ac.setChangedOn(progAc.getChangedOn());
  }
  ac.setChanges(progAc.getChanges());
  ac.setDescription(progAc.getDescription());
  if(progAc.getGlAccount() != null){
   //FiGlAccountComp glAc = progAc.getGlAccount();
   FiGlAccountCompRec glAc = this.fiMastRecDM.getFiCompGlAccountRec(progAc.getGlAccount());
   ac.setGlAccount(glAc);
  }
  ac.setName(progAc.getName());
  ac.setRef(progAc.getRef());
  if(progAc.getResponsibilityOf() != null){
   PartnerPersonRec resp = this.masterDataDM.buildPartnerPersonRecPvt(progAc.getResponsibilityOf());
   ac.setResponsibilityOf(resp);
  }
  ac.setValidFrom(progAc.getValidFrom());
  ac.setValidTo(progAc.getValidTo());
  return ac;
 }
 public ProgramAccount getProgActByGlAc(FiGlAccountCompRec glAc, ProgrammeRec prog, 
         boolean auto, UserRec usr, Date crdate, String page) throws BacException {
  LOGGER.log(INFO,"getProgActByGlAc called with user {0}",usr.getId());
  if(glAc == null || glAc.getId() == null || glAc.getId() == 0){
   throw new BacException("Invalid glAccount "+glAc);
  }
  if(prog == null || prog.getId() == null || prog.getId() == 0){
   throw new BacException("Invalid program "+prog);
  }
  ProgramAccount act = null;
  LOGGER.log(INFO, "ProgamDM.getProgActByGlAc called");
  Query q = em.createNamedQuery("progActByProgGlAct");
  q.setParameter("glActId", glAc.getId());
  q.setParameter("progId", prog.getId());
  try{
   act = (ProgramAccount)q.getSingleResult();
   return act; 
  }catch(NoResultException ex){
   if(auto){
    LOGGER.log(INFO, "Auto create {0}", auto);
    act = new ProgramAccount();
    em.persist(act);
    User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
    act.setCreatedBy(crUsr);
    act.setCreatedOn(crdate);
    act.setRef(glAc.getCoaAccount().getRef());
    act.setName(glAc.getCoaAccount().getName());
    FiGlAccountComp glAcDb = em.find(FiGlAccountComp.class, glAc.getId(), OPTIMISTIC);
    act.setGlAccount(glAcDb);
    if(prog.getResponsibilityOf() != null){
     PartnerPerson respPers = em.find(PartnerPerson.class, prog.getResponsibilityOf().getId(), OPTIMISTIC);
     act.setResponsibilityOf(respPers);
    }
    act.setValidFrom(prog.getValidFrom());
    act.setValidTo(prog.getValidTo());
    return act;
   }
   
  }
  
          
  
  return act;
 }
 
 public ProgramAccountRec getProgActRecByGlAc(FiGlAccountCompRec glAc, ProgrammeRec prog, 
         boolean auto, UserRec usr, Date crdate, String page) throws BacException {
  LOGGER.log(INFO,"getProgActByGlAc called with user {0}",usr.getId());
  if(glAc == null || glAc.getId() == null || glAc.getId() == 0){
   throw new BacException("Invalid glAccount "+glAc);
  }
  if(prog == null || prog.getId() == null || prog.getId() == 0){
   throw new BacException("Invalid program "+prog);
  }
  ProgramAccountRec actRec = null;
  LOGGER.log(INFO, "ProgamDM.getProgActByGlAc called");
  Query q = em.createNamedQuery("progActByProgGlAct");
  q.setParameter("glActId", glAc.getId());
  q.setParameter("progId", prog.getId());
  try{
   ProgramAccount act = (ProgramAccount)q.getSingleResult();
   actRec = this.buildProgramAccountRec(act); 
    
  }catch(NoResultException ex){
   if(auto){
    LOGGER.log(INFO, "Auto create {0}", auto);
    ProgramAccount act = new ProgramAccount();
    em.persist(act);
    User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
    act.setCreatedBy(crUsr);
    act.setCreatedOn(crdate);
    act.setRef(glAc.getCoaAccount().getRef());
    act.setName(glAc.getCoaAccount().getName());
    FiGlAccountComp glAcDb = em.find(FiGlAccountComp.class, glAc.getId(), OPTIMISTIC);
    act.setGlAccount(glAcDb);
    if(prog.getResponsibilityOf() != null){
     PartnerPerson respPers = em.find(PartnerPerson.class, prog.getResponsibilityOf().getId(), OPTIMISTIC);
     act.setResponsibilityOf(respPers);
    }
    act.setValidFrom(prog.getValidFrom());
    act.setValidTo(prog.getValidTo());
    actRec = this.buildProgramAccountRec(act);
   }
   
  }
  
          
  
  return actRec;
 }

 
 
 
 
 

}
