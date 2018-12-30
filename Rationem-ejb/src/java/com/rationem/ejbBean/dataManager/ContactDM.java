/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.busRec.doc.DocBaseRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.audit.AuditContact;
import com.rationem.entity.audit.AuditContactRole;
import com.rationem.entity.cm.Contact;
import com.rationem.entity.config.common.ContactRole;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.util.ContactSelection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class ContactDM {
 private static final Logger LOGGER =  Logger.getLogger(ContactDM.class.getName());
 private EntityManager em;
 private EntityTransaction trans;
 
 @EJB
 private UserDM usrDM;
 
 @EJB
 private ArAccountDM arAcntDm;
 
 @EJB
 MasterDataDM masterDataDm;

 @EJB
 DocumentDM docDm;
 
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
  
 private AuditContact buildAuditContact(Contact cont, User usr, char action, String pg){
  AuditContact aud = new AuditContact();
  aud.setAuditDate(new Date());
  aud.setContact(cont);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(action);
  return aud;
  
 }
 
 
 private AuditContactRole buildAuditContactRole(ContactRole contRole, User usr, char action, String pg){
  AuditContactRole aud = new AuditContactRole();
  aud.setAuditDate(new Date());
  aud.setContactRole(contRole);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(action);
  em.persist(aud);
  return aud;
 }
 
 private Contact buildContact(ContactRec contRec, String pg){
  boolean newContact = false;
  boolean changedContact = false;
  Contact cont;
  if(contRec.getId() == null){
   cont = new Contact();
   User crUsr = em.find(User.class, contRec.getCreatedBy().getId());
   cont.setCreatedBy(crUsr);
   cont.setCreatedOn(contRec.getCreatedOn());
   em.persist(cont);
   AuditContact aud = this.buildAuditContact(cont, crUsr, 'I', pg);
   aud.setNewValue(contRec.getSummary());
   newContact = true;
  }else{
   cont = em.find(Contact.class, contRec.getId());
  }
  
  if(newContact){
   cont.setActionCompleted(contRec.isActionCompleted());
   cont.setActionContact(contRec.isActionContact());
   if(contRec.getApAccount() != null){
    ApAccount ap = em.find(ApAccount.class, contRec.getApAccount().getId());
    cont.setApAccount(ap);
   }
   if(contRec.getArAccount() != null){
    ArAccount ar = em.find(ArAccount.class, contRec.getArAccount().getId());
    cont.setArAccount(ar);
   }
   cont.setAttFile(contRec.getAttFile());
   cont.setAttFileType(contRec.getAttFileType());
   cont.setAttFileName(contRec.getAttFileName());
   cont.setCompletedDate(contRec.getCompletedDate());
   if(contRec.getContactFor() != null){
    PartnerBase ptnr = em.find(PartnerBase.class, contRec.getContactFor().getId());
    cont.setContactFor(ptnr);
   }
   cont.setDetail(contRec.getDetail());
   if(contRec.getDoc() != null){
    DocBase doc = em.find(DocBase.class, contRec.getDoc().getId());
    cont.setDoc(doc);
   }
   if(contRec.getDocLine() != null){
    DocLineBase ln = em.find(DocLineBase.class, contRec.getDocLine().getId());
    cont.setDocLine(ln);
   }
   if(contRec.getRespibilityOf() != null){
    PartnerPerson ass = em.find(PartnerPerson.class, contRec.getRespibilityOf().getId());
    cont.setRespibilityOf(ass);
   }
   if(contRec.getRole() != null){
    ContactRole rl = em.find(ContactRole.class, contRec.getRole().getId());
    cont.setRole(rl);
   }
   cont.setSummary(contRec.getSummary());
  }else{
   // has contact been changed
   User chUsr = em.find(User.class, contRec.getChangedBy().getId());
   
   if(contRec.isActionCompleted() != cont.isActionCompleted()){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_ACT");
    aud.setNewValue(String.valueOf(contRec.isActionCompleted()));
    aud.setOldValue(String.valueOf(cont.isActionCompleted()));
    cont.setActionCompleted(contRec.isActionCompleted());
    changedContact = true;
   }
   if(contRec.isActionContact() != cont.isActionContact()){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_COMPL");
    aud.setNewValue(String.valueOf(contRec.isActionContact()));
    aud.setOldValue(String.valueOf(cont.isActionContact()));
    cont.setActionContact(contRec.isActionContact());
    changedContact = true;
   }
   if((contRec.getApAccount() == null && cont.getApAccount() != null) ||
     (contRec.getApAccount() != null && cont.getApAccount() == null) ||
     (contRec.getApAccount() != null && 
     !Objects.equals(contRec.getApAccount().getId(), cont.getApAccount().getId())) ){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_AP");
    aud.setNewValue(contRec.getApAccount().getAccountCode());
    aud.setOldValue("err");
    ApAccount ap = em.find(ApAccount.class, contRec.getApAccount().getId());
    cont.setApAccount(ap);
    changedContact = true;
   }
   if((contRec.getArAccount() == null && cont.getArAccount() != null) ||
     (contRec.getArAccount() != null && cont.getArAccount() == null) ||
     (contRec.getArAccount() != null && 
     !Objects.equals(contRec.getArAccount().getId(), cont.getArAccount().getId()) )){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_AR");
    aud.setNewValue(contRec.getArAccount().getArAccountCode());
    aud.setOldValue(cont.getArAccount().getAccountCode());
    ArAccount ar = em.find(ArAccount.class, contRec.getArAccount().getId());
    cont.setArAccount(ar);
    changedContact = true;
   }
   if((contRec.getAttFile() == null && cont.getAttFile() != null) ||
     (contRec.getAttFile() != null && cont.getAttFile() == null) ||
     (contRec.getAttFile() != null && 
     contRec.getAttFile().length == cont.getAttFile().length)){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_FILE");
    aud.setNewValue("CHANGED");
    aud.setOldValue("CHANGED");
    ArAccount ar = em.find(ArAccount.class, contRec.getArAccount().getId());
    cont.setArAccount(ar);
    changedContact = true;
   }
   if((contRec.getAttFileType() == null && cont.getAttFileType() != null) ||
     (contRec.getAttFileType() != null && cont.getAttFileType() == null) ||
     (contRec.getAttFileType() != null && 
     !contRec.getAttFileType().equals(cont.getAttFileType()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_F_TY");
    aud.setNewValue(contRec.getAttFileType());
    aud.setOldValue(cont.getAttFileType());
    cont.setAttFileType(contRec.getAttFileType());
    changedContact = true;
   }
   if((contRec.getAttFileName() == null && cont.getAttFileName() != null) ||
     (contRec.getAttFileName() != null && cont.getAttFileName() == null) ||
     (contRec.getAttFileName() != null && 
     !contRec.getAttFileName().equals(cont.getAttFileName()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_F_TY");
    aud.setNewValue(contRec.getAttFileName());
    aud.setOldValue(cont.getAttFileName());
    cont.setAttFileName(contRec.getAttFileName());
    changedContact = true;
   }
   
   if((contRec.getCompletedDate() == null && cont.getCompletedDate() != null) ||
     (contRec.getCompletedDate() != null && !contRec.getCompletedDate().equals(cont.getCompletedDate()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_CMPL_DT");
    aud.setNewValue(String.valueOf(contRec.getCompletedDate()));
    aud.setOldValue(String.valueOf(cont.getCompletedDate()));
    cont.setCompletedDate(contRec.getCompletedDate());
    changedContact = true;
   }
   if((contRec.getContactFor() == null && cont.getContactFor() != null) ||
     (contRec.getContactFor() != null && cont.getContactFor() == null) ||
     (contRec.getContactFor() != null && 
      !Objects.equals(contRec.getContactFor().getId(), cont.getContactFor().getId()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_FOR");
    aud.setNewValue(contRec.getContactFor().getName());
    aud.setOldValue(cont.getContactFor().getTradingName());
    PartnerBase ptnr = em.find(PartnerBase.class, contRec.getContactFor().getId());
    cont.setContactFor(ptnr);
    changedContact = true;
   }
   if((contRec.getDetail() == null && cont.getDetail() != null)||
     (contRec.getDetail() != null && !contRec.getDetail().equals(cont.getDetail()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_DT");
    aud.setNewValue(contRec.getDetail());
    aud.setOldValue(cont.getDetail());
    cont.setDetail(contRec.getDetail());
    changedContact = true;
   }
   if((contRec.getDoc() == null && cont.getDoc() != null)||
     (contRec.getDoc() != null && !Objects.equals(contRec.getDoc().getId(), cont.getDoc().getId()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_DT");
    aud.setNewValue(String.valueOf(contRec.getDoc().getDocNumber()));
    aud.setOldValue(String.valueOf(contRec.getDoc().getDocNumber()));
    DocBase d = em.find(DocBase.class, contRec.getDoc().getId());
    cont.setDoc(d);
    changedContact = true;
   }
   if((contRec.getDocLine() == null && cont.getDocLine() != null)||
     (contRec.getDocLine() != null && 
      !Objects.equals(contRec.getDocLine().getId(), cont.getDocLine().getId()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_DT");
    aud.setNewValue(contRec.getDocLine().getDocHeaderBase().getDocNumber() + " - "+contRec.getDocLine().getLineNum());
    aud.setOldValue(cont.getDocLine().getDocHeaderBase().getDocNumber() + " - "+cont.getDocLine().getLineNum());
    DocLineBase l = em.find(DocLineBase.class, contRec.getDocLine().getId());
    cont.setDocLine(l);
    changedContact = true;
   }
   if((contRec.getRespibilityOf() == null && cont.getRespibilityOf() != null)||
     (contRec.getRespibilityOf() != null && 
      !Objects.equals(contRec.getRespibilityOf().getId(), cont.getRespibilityOf().getId()))){
    AuditContact aud = buildAuditContact(cont, chUsr, 'U', pg);
    aud.setFieldName("CONT_DT");
    aud.setNewValue(contRec.getRespibilityOf().getName());
    aud.setOldValue(cont.getRespibilityOf().getFamilyName());
    PartnerPerson p = em.find(PartnerPerson.class, contRec.getRespibilityOf().getId());
    cont.setRespibilityOf(p);
    changedContact = true;
   }
   if(changedContact){
    cont.setChangedBy(chUsr);
    cont.setChangedOn(contRec.getCompletedDate());
   }
  }
  return cont;
 }
 
 private ContactRec buildContactRec(Contact cont){
  ContactRec contRec = new ContactRec();
  contRec.setId(cont.getId());
  UserRec crUsr = this.usrDM.getUserRecPvt(cont.getCreatedBy());
  contRec.setCreatedBy(crUsr);
  contRec.setCreatedOn(cont.getCreatedOn());
  if(cont.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(cont.getChangedBy());
   contRec.setChangedBy(chUsr);
   contRec.setChangedOn(cont.getChangedOn());
  }
  contRec.setActionCompleted(cont.isActionCompleted());
  contRec.setActionContact(cont.isActionContact());
  contRec.setApAccount(null);
  if(cont.getArAccount() != null){
   ArAccountRec ar = arAcntDm.getArAccountRecPvt(cont.getArAccount());
   contRec.setArAccount(ar);
  }
  contRec.setAttFile(cont.getAttFile());
  contRec.setAttFileType(cont.getAttFileType());
  contRec.setAttFileName(cont.getAttFileName());
  contRec.setCompletedDate(cont.getCompletedDate());
  if(cont.getContactFor() != null){
   String ptnrClass = cont.getContactFor().getClass().getSimpleName();
   PartnerBaseRec contFor;
   if(ptnrClass.equals("PartnerPerson")){
    contFor = masterDataDm.buildPartnerPersonRecPvt((PartnerPerson)cont.getContactFor());
   }else{
    contFor = masterDataDm.getPartnerCorporateRec((PartnerCorporate)cont.getContactFor());
   }
   contRec.setContactFor(contFor);
  }
  contRec.setDetail(cont.getDetail());
  if(cont.getDoc() != null){
  DocBaseRec doc = this.docDm.buildDocFiRecPvt((DocFi)cont.getDoc());
  contRec.setDoc(doc);
  }
  if(cont.getDocLine() != null){
   DocLineBaseRec ln = this.docDm.getDocLineBaseRecPvt(cont.getDocLine());
   contRec.setDocLine(ln);
  }
  if(cont.getRespibilityOf() != null){
   PartnerPersonRec resp = this.masterDataDm.buildPartnerPersonRecPvt(cont.getRespibilityOf());
   contRec.setRespibilityOf(resp);
  }
  if(cont.getRole() != null){
   ContactRoleRec rl = this.buildContactRoleRec(cont.getRole());
   contRec.setRole(rl);
  }
  contRec.setSummary(cont.getSummary());
  return contRec;
 }
 private ContactRole buildContactRole(ContactRoleRec roleRec, String pg){
  LOGGER.log(INFO, "buildContactRole called with role {0}", roleRec);
  boolean newContactRole = false;
  boolean changedContactRole = false;
  ContactRole rl;
  if(roleRec.getId() == null){
   rl = new ContactRole();
   User usr = em.find(User.class, roleRec.getCreatedBy().getId());
   rl.setCreatedBy(usr);
   rl.setCreatedOn(roleRec.getCreatedOn());
   em.persist(rl);
   AuditContactRole aud = this.buildAuditContactRole(rl, usr, 'I', pg);
   aud.setNewValue(roleRec.getName());
   newContactRole = true;
  } else{
   rl = em.find(ContactRole.class, roleRec.getId());
  }
  
  if(newContactRole){
   rl.setDescription(roleRec.getDescription());
   rl.setName(roleRec.getName());
   rl.setInbound(roleRec.isInbound());
   
  }else{
   // has contract role changed
   User chUsr = em.find(User.class, roleRec.getChangedBy().getId());
   
   if(roleRec.getName().equals(rl.getName())){
    AuditContactRole aud = this.buildAuditContactRole(rl, chUsr, 'U', pg);
    aud.setFieldName("CONTRL_NAME");
    aud.setNewValue(roleRec.getName());
    aud.setOldValue(rl.getName());
    rl.setName(roleRec.getName());
    changedContactRole = true;
   }
   if(roleRec.getDescription().equals(rl.getDescription())){
    AuditContactRole aud = this.buildAuditContactRole(rl, chUsr, 'U', pg);
    aud.setFieldName("CONTRL_DESCR");
    aud.setNewValue(roleRec.getDescription());
    aud.setOldValue(rl.getDescription());
    rl.setDescription(roleRec.getDescription());
    changedContactRole = true;
   }
   if(roleRec.isInbound() != rl.isInbound()){
    AuditContactRole aud = this.buildAuditContactRole(rl, chUsr, 'U', pg);
    aud.setFieldName("CONTRL_IN");
    aud.setNewValue(String.valueOf(roleRec.isInbound()));
    aud.setOldValue(String.valueOf(rl.isInbound()));
    rl.setInbound(roleRec.isInbound());
    changedContactRole = true;
   }
   if(changedContactRole){
    rl.setChangedBy(chUsr);
    rl.setChangedOn(roleRec.getChangedOn());
   }
  }
  return rl;
 }

 private ContactRoleRec buildContactRoleRec(ContactRole role){
  ContactRoleRec rec = new ContactRoleRec();
  rec.setDescription(role.getDescription());
  rec.setId(role.getId());
  rec.setInbound(role.isInbound());
  rec.setName(role.getName());
  UserRec crUsr = usrDM.getUserRecPvt(role.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(role.getCreatedOn());
  if(role.getChangedBy() != null){
   UserRec chUsr = usrDM.getUserRecPvt(role.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(role.getChangedOn());
  }
  return rec;
 }

    // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
 
 
 

 public List<ContactRec> conactsForArAccount(ArAccountRec ar){
  LOGGER.log(INFO, "contDM.conactsForArAccount called with Ar acnt {0}",ar.getArAccountCode());
  Query q = em.createNamedQuery("contactsByArAcnt");
  q.setParameter("arAcntId", ar.getId());
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  
  List<ContactRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRec rec = this.buildContactRec((Contact)o);
   retList.add(rec);
  }
  return retList;
 }
 
 public List<ContactRec> conactsForApAcnt(ContactSelection sel, ApAccountRec acnt){
  LOGGER.log(INFO, "ContDM.conactsForApAcnt called with sel {0} doc {1}", new Object[]{sel,acnt});
  Query q;
  if(sel == null){
   q = em.createNamedQuery("contactsForApAcnt");
   q.setParameter("acntId", acnt.getId());
  }else{
   q = em.createNamedQuery("contactsForApAcntDate");
   q.setParameter("acntId", acnt.getId());
   q.setParameter("startDt", sel.getFrom());
   q.setParameter("endDt", sel.getTo());
  }
  List rs = q.getResultList();
  LOGGER.log(INFO, "rs {0}", rs);
  if(rs == null){
   return null;
  }
  List<ContactRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRec curr = this.buildContactRec((Contact)o);
   retList.add(curr);
  }
  return retList;
 }
 
 public List<ContactRec> conactsForDoc(ContactSelection sel, DocFiRec doc){
  LOGGER.log(INFO, "ContDM.conactsForDoc called with sel {0} doc {1}", new Object[]{sel,doc.getId()});
  Query q;
  if(sel == null || sel.getFrom() == null){
   q = em.createNamedQuery("contactsForDoc");
   q.setParameter("docId", doc.getId());
  }else{
   q = em.createNamedQuery("contactsForDocDate");
   q.setParameter("docId", doc.getId());
   q.setParameter("startDt",sel.getFrom());
   q.setParameter("endDt", sel.getTo());
  }
  List rs = q.getResultList();
  LOGGER.log(INFO, "Query returns {0}", rs);
  if(rs == null){
   return null;
  }
  List<ContactRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRec curr = this.buildContactRec((Contact)o);
   retList.add(curr);
  }
  LOGGER.log(INFO, "ContDM.conactsForDoc returns {0}",retList);
  return retList;
 }
 
 public List<ContactRec> contactsForApAccount(ContactSelection sel, ApAccountRec acnt ){
  Query q;
  
  if(sel != null ){
   q = em.createNamedQuery("contactsForApAcntDate");
   q.setParameter("acntId", acnt.getId());
   q.setParameter("startDt", sel.getFrom());
   q.setParameter("endDt", sel.getTo());
  }else{
   q = em.createNamedQuery("contactsForApAcnt");
   q.setParameter("acntId", acnt.getId());
  }
  List rs = q.getResultList();
  if(rs == null){
   return null;
  }
  List<ContactRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRec curr = this.buildContactRec((Contact)o);
   retList.add(curr);
  }
  return retList;
 }
 
 public List<ContactRec> contactsForPtnr(ContactSelection sel, PartnerBaseRec ptnr ){
  Query q;
  
  if(sel != null ){
   q = em.createNamedQuery("contactsForPtnrDate");
   q.setParameter("ptnrId", ptnr.getId());
   q.setParameter("startDt", sel.getFrom());
   q.setParameter("endDt", sel.getTo());
  }else{
   q = em.createNamedQuery("contactsForPtnr");
   q.setParameter("ptnrId", ptnr.getId());
  }
  List rs = q.getResultList();
  if(rs == null){
   return null;
  }
  List<ContactRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRec curr = this.buildContactRec((Contact)o);
   retList.add(curr);
  }
  return retList;
 }
 
 public ContactRec updateContact(ContactRec cont, String page){
  LOGGER.log(INFO, "ContactDM.updateContact called with {0} page {1}", 
    new Object[]{cont,page});
  if(!trans.isActive()){
   trans.begin();
  }
  Contact c = this.buildContact(cont, page);
  if(cont.getId() == null ){
   cont.setId(c.getId());
  }
  trans.commit();
  return cont;
 }
 public ContactRoleRec updateContactRole(ContactRoleRec contactRole, String page) {
  LOGGER.log(INFO, "ContactDM.updateContactRole called with {0} page {1}", 
    new Object[]{contactRole,page});
  trans.begin();
  ContactRole rl = buildContactRole(contactRole, page);
  if(contactRole.getId() == null){
   contactRole.setId(rl.getId());
  }
  trans.commit();
  return contactRole;
 }
 
 public List<ContactRoleRec> getContactRoles(){
  Query q = em.createNamedQuery("contRoleAll");
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   LOGGER.log(INFO, "No ContactRoles found");
   return null;
  }
  List<ContactRoleRec> retList = new ArrayList<>();
  for(Object o:rs){
   ContactRoleRec rec = this.buildContactRoleRec((ContactRole)o);
   retList.add(rec);
  }
  return retList;
 }
 public ContactRoleRec getContactRoleByName(String name){

  Query q = em.createNamedQuery("contRoleByName");
  q.setParameter("contRoleName", name);
  try{
   ContactRole r = (ContactRole)q.getSingleResult();
   ContactRoleRec rec = this.buildContactRoleRec(r);
   return rec;
  }catch(NoResultException ex){
   return null;
  }catch(NonUniqueResultException ex){
   LOGGER.log(INFO, "Duplicate role {0}", ex.getLocalizedMessage());
   return null;
  }
   
  
 } 
}
