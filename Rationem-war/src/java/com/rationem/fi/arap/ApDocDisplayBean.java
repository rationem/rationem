/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.ejbBean.common.ContactManager;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.AuditMgr;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.BaseBean;
import com.rationem.util.ContactSelection;
import com.rationem.util.MessageUtil;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import java.util.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import static java.util.logging.Level.INFO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author user
 */
public class ApDocDisplayBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(ApDocDisplayBean.class.getName());

 private ApAccountRec apAccount;
 private CompanyBasicRec comp;
 private boolean compSelected;
 private ContactRec contact;
 private List<ContactRec> contacts;
 private ContactRoleRec contactRole;
 private List<ContactRoleRec> contactRoleList;
 private ContactSelection contSelOpt;
 private DocFiRec doc;
 private boolean docSel;
 private DocLineApRec docLineAp;
 private DocLineApRec docLineSel;
 private double totalDebit;
 private double totalCredit;
 private List<AuditBaseRec> auditRecs;
 
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private AuditMgr audMgr;
 
 @EJB
 private DocumentManager docMgr;

 @EJB
 private ContactManager contMgr; 
 
 @EJB
 private MasterDataManager masterDataMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 /**
  * Creates a new instance of ApDocDisplay
  */
 public ApDocDisplayBean() {
 }
 
 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("docCompNone", "errorText");
   compSelected = false;
  }else {
   comp = getCompList().get(0);
  }
  
  doc = new DocFiRec();
  doc.setCompany(comp);
  docSel = false;
   
 }

 public ApAccountRec getApAccount() {
  return apAccount;
  
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public boolean isCompSelected() {
  return compSelected;
 }

 public void setCompSelected(boolean compSelected) {
  this.compSelected = compSelected;
 }

 public ContactRec getContact() {
  return contact;
 }

 public void setContact(ContactRec contact) {
  this.contact = contact;
 }

 
 public ContactRoleRec getContactRole() {
  return contactRole;
 }

 public void setContactRole(ContactRoleRec contactRole) {
  this.contactRole = contactRole;
 }

 
 public List<ContactRoleRec> getContactRoleList() {
  return contactRoleList;
 }

 public void setContactRoleList(List<ContactRoleRec> contactRoleList) {
  this.contactRoleList = contactRoleList;
 }

 
 public List<ContactRec> getContacts() {
  return contacts;
 }

 public void setContacts(List<ContactRec> contacts) {
  this.contacts = contacts;
 }

 
 public ContactSelection getContSelOpt() {
  return contSelOpt;
 }

 public void setContSelOpt(ContactSelection contSelOpt) {
  this.contSelOpt = contSelOpt;
 }

 
 public DocFiRec getDoc() {
  return doc;
  
 }

 public void setDoc(DocFiRec doc) {
  this.doc = doc;
  
 }

 public DocLineApRec getDocLineAp() {
  return docLineAp;
 }

 public void setDocLineAp(DocLineApRec docLineAp) {
  this.docLineAp = docLineAp;
 }

 public DocLineApRec getDocLineSel() {
  return docLineSel;
 }

 public void setDocLineSel(DocLineApRec docLineSel) {
  this.docLineSel = docLineSel;
 }

 
 public boolean isDocSel() {
  return docSel;
 }

 public void setDocSel(boolean docSel) {
  this.docSel = docSel;
 }

 public double getTotalDebit() {
  return totalDebit;
 }

 public void setTotalDebit(double totalDebit) {
  this.totalDebit = totalDebit;
 }

 public double getTotalCredit() {
  return totalCredit;
 }

 public void setTotalCredit(double totalCredit) {
  this.totalCredit = totalCredit;
 }
 
 
 public StreamedContent getOrder(){
  ByteArrayInputStream bais = new ByteArrayInputStream(docLineAp.getOrderFileData());
  StreamedContent f = new DefaultStreamedContent(bais,docLineAp.getOderFileType(),docLineAp.getOderFileName());
  return f;
  
  }

 public List<ApAccountRec> onApAcntComplete(String input){
  LOGGER.log(INFO, "onApAcntComplete called with {0}", input);
  List<ApAccountRec> retList;
  if(StringUtils.isBlank(input)){
   retList = apMgr.getApAccountsAll(comp);
  }else{
   retList = apMgr.getApAccountsStartinfWithCode(comp, input);
  }
  
  return retList;
 }
 
 
 
 
 public void onContactAttachUpload(FileUploadEvent evt){
  UploadedFile att = evt.getFile();
  contact.setAttFileType(att.getContentType());
  contact.setAttFileName(att.getFileName());
  contact.setAttFile(att.getContents());
  
  
 }
 
 public void onContactLoadAcnt(){
  
  LOGGER.log(INFO, "onContactAcntLoad called ");
  contacts = contMgr.contactsForApAcnt(contSelOpt, docLineAp.getApAccount());
  LOGGER.log(INFO, "contacts after call to contMgr {0}", contacts);
  if(contacts == null || contacts.isEmpty()){
   MessageUtil.addWarnMessage("contNone", "blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contErr");
  }else{
   MessageUtil.addInfoMessage("contAcntLoad", "blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contactGr");
   
   
  }
  
  
  
  
 }
 
 public void onContactLoadDoc(){
  LOGGER.log(INFO, "onContactDocLoad called with selection {0}",contSelOpt);
  if(contSelOpt != null){
   LOGGER.log(INFO, "sel from {0} to {1}", new Object[]{contSelOpt.getFrom(),contSelOpt.getTo()});
  }
  contacts = contMgr.contactsForDoc(contSelOpt, doc);
  LOGGER.log(INFO, "contacts found {0}", contacts);
  
  if(contacts == null || contacts.isEmpty()){
   MessageUtil.addWarnMessage("contNone", "blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contErr");
  }else{
   MessageUtil.addInfoMessage("contDocLoad","blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contErr");
  }
  
  
 }
 
 public void onContactLoadPtnr(){
  
  LOGGER.log(INFO, "onContactPtnrLoad called ");
  contacts = contMgr.contactsForPtnr(contSelOpt, docLineAp.getApAccount().getApAccountFor());
  if(contacts == null || contacts.isEmpty()){
   MessageUtil.addWarnMessage("contNone", "blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contErr");
  }else{
   MessageUtil.addInfoMessage("contPtnrLoad", "blacResponse");
   PrimeFaces.current().ajax().update("docTabV:contsAccordId:contactGr");
  }
  
 }
 
 
 public void onContactNewMnu(){
  contact = new ContactRec();
  this.contactRoleList = this.sysBuff.getContactRoles();
  PrimeFaces rCtx = PrimeFaces.current();
  //rCtx.ajax().update("addCont");
  rCtx.executeScript("PF('addContWv').show();");
 }

 public void onContactTrf(){
  LOGGER.log(INFO, "onContactTrf called ");
  if(contact.getId() == null){
   contact.setCreatedBy(this.getLoggedInUser());
   contact.setCreatedOn(new Date());
   contact.setDoc(doc);
   contact.setApAccount(docLineAp.getApAccount());
   contact.setContactFor(docLineAp.getApAccount().getApAccountFor());
  
   contact = this.contMgr.contactUpdate(contact, this.getView());
   PrimeFaces rCtx = PrimeFaces.current();
   if(contact.getId() == null){
    // save contact failed
    MessageUtil.addWarnMessage("contactSave", "errorText");
    rCtx.ajax().update("uptErr");
    
   }else{
    //save cont asuccess
    contacts.add(contact);
    MessageUtil.addInfoMessage("contSaved", "blacResponse");
    
    List<String> updates = new ArrayList<>();
    updates.add("docTabV:contsAccordId:contTbl");
    updates.add("docTabV:contsAccordId:contactGr");
    rCtx.ajax().update(updates);
    rCtx.executeScript("PF('addContWv').hide()");
   }
  }
  
  
 }
 public List<DocFiRec> onDocComplete(String input){
  
  List<DocFiRec> retList;
  LOGGER.log(INFO,"onDocComplete apAccount {0}",apAccount);
  if(StringUtils.isBlank(input)){
   if(apAccount == null){
    retList = docMgr.getDocsApForComp(comp);
   }else{
    retList = docMgr.getDocsApForAcnt(comp,apAccount);
   }
  }else{
   if(StringUtils.isNumeric(input)){
    long docNum = Long.parseLong(input);
    retList = docMgr.getDocsApForComp(comp, docNum);
   }else{
    return null;
   }
  }
  return retList;
 }
 
 public void onDocSelect(SelectEvent evt){
  LOGGER.log(INFO, "onDocSelect called with {0}", evt.getObject());
  
  
  
  DocFiRec currDoc = (DocFiRec)evt.getObject();
  doc = currDoc;
  List<DocLineBaseRec> lines = doc.getDocLines();
  LOGGER.log(INFO, "lines {0}", lines);
  if(lines == null){
   lines = this.docMgr.getDocLinesForDoc(doc);
   
  }
  ListIterator<DocLineBaseRec> lineLi = lines.listIterator();
  boolean foundApLn = false;
  while(lineLi.hasNext() && !foundApLn){
   DocLineBaseRec curr = lineLi.next();
   if(StringUtils.equals(curr.getClass().getSimpleName(), "DocLineApRec")){
    docLineAp = (DocLineApRec)curr;
    lineLi.remove();
   }
  }
  docSel = true;
  PrimeFaces.current().ajax().update("docTabV");
  LOGGER.log(INFO, "doc lines  {0}", doc.getDocLines());
  LOGGER.log(INFO, "onDocSelect docLineAp {0}",docLineAp);
 }
 
 public List<PartnerPersonRec> onPersRespCompl(String input){
  
  List<PartnerPersonRec> retList = new ArrayList<>();
  if(StringUtils.isAnyBlank(input)){
   retList =    masterDataMgr.getAllPartnerIndividual();
  }else{
   retList =    masterDataMgr.getIndivPtnrsBySurname(input);
  }
  return retList;
 }
 public void onTabChange(TabChangeEvent evt){
  LOGGER.log(INFO, "onTabChangeCalled with {0}", evt.getTab().getClientId());
  String currTabId = evt.getTab().getClientId();
  PrimeFaces rCtx = PrimeFaces.current();
  switch(currTabId){
   case "docTabV:linesTabId":
    if(doc.getDocLines() == null || doc.getDocLines().isEmpty()){
     //get doc lines
     doc = docMgr.getDocLines(doc);
     
     // set debit and credit amount and remove AP line
     totalDebit = 0;
     totalCredit = 0;
     for(DocLineBaseRec ln:doc.getDocLines()){
      if(ln.getPostType().isDebit()){
       totalDebit += ln.getDocAmount();
      }else{
       totalCredit += ln.getDocAmount();
      }
     }
    }
    LOGGER.log(INFO, "doc lines {0}", doc.getDocLines());
    rCtx.ajax().update("docTabV:lines");
    break;
   case "docTabV:contactsId":
    contSelOpt = new ContactSelection();
    LOGGER.log(INFO, "Tab change to contacts contSelOpt {0}", contSelOpt);
    break;
   case "docTabV:auditId":
    List<AuditBaseRec> audRecs = this.audMgr.getAuditRecForDocFi(doc);
    LOGGER.log(INFO, "audit recs {0}", audRecs);
    setAuditList(audRecs);
    LOGGER.log(INFO, "getAuditList{0}", getAuditList());
    break;
    
  }
 }
 
}
