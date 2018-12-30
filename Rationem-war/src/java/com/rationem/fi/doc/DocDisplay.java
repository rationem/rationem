/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.doc;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.ContactManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.AuditMgr;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.DocLinesByLineNum;
import com.rationem.util.MessageUtil;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author Chris
 */


public class DocDisplay extends BaseBean{
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private static final Logger LOGGER =  Logger.getLogger(DocDisplay.class.getSimpleName());
@EJB
private DocumentManager docMgr;

@EJB 
private SysBuffer sysBuff;

@EJB
private AuditMgr audMgr;

@EJB
private ContactManager contMgr;
 
 
 private CompanyBasicRec comp;
 private List<DocFiRec> docSelList;
 private List<DocLineBaseRec> docLines;
 private List<AuditBaseRec> auditRecs;
 private DocLineBaseRec docLineSel;
 private List<DocLineGlRec> docLineReconLines;
 private String docLineClass;
 private boolean docLineGl;
 private DocLineGlRec docLineGlSel;
 //private DocLineGlRec docLineGlRecSel;
 private DocLineArRec docLineArSel;
 private DocLineApRec docLineApSel;
 private DocLineArRec docClearedLineAr;
 private DocFiRec docSel;
 private List<ContactRec> docContacts;
 private String docTotalCredit;
 private String docTotalDebit;
 private boolean docSelected = false;
 private Locale locale;
 private StreamedContent invPdfile;
 
 /**
  * Creates a new instance of DocDisplay
  */
 public DocDisplay() {
  LOGGER.log(INFO, "DocDisplay constructor called");
 }
 
 @PostConstruct
 public void init(){
  locale = getUsrBuff().getLoc();
  LOGGER.log(INFO, "locale {0}", locale.getCountry());
  comp = this.getCompList().get(0);
  
 }

 public List<ArBankAccountRec> arBankComplete(String input){
  LOGGER.log(INFO, "docLineArSel {0}", docLineSel);
  LOGGER.log(INFO, "docLineArSel.getArAccount() {0}", getDocLineArSel().getArAccount());
  LOGGER.log(INFO, "getArAccountBanks() {0}", getDocLineArSel().getArAccount().getArAccountBanks());
  List<ArBankAccountRec> arBanks = getDocLineArSel().getArAccount().getArAccountBanks();
  if(input == null || input.isEmpty() ){
   LOGGER.log(INFO, "Bank Account {0}", arBanks.get(0).getBankAccount().getAccountNumber());
   LOGGER.log(INFO, "Branch {0}", arBanks.get(0).getBankAccount().getAccountForBranch());
   LOGGER.log(INFO, "Sort {0}", arBanks.get(0).getBankAccount().getAccountForBranch().getSortCode());
   return arBanks;
  }
  if(arBanks.isEmpty()){
   return arBanks;
  }
  List<ArBankAccountRec> retBanks = new ArrayList<>();
  for(ArBankAccountRec arBnk : arBanks){
   if(arBnk.getAccountName().startsWith(input)){
    retBanks.add(arBnk);
   }
  }
  return retBanks;
 }

 public List<AuditBaseRec> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditBaseRec> auditRecs) {
  this.auditRecs = auditRecs;
 }
 
 
 public List<DocFiRec> docEditComplete(String input){
  LOGGER.log(INFO, "docComplete called with input{0}",input);
  List<DocFiRec> retList = new ArrayList<>();
  if(comp == null){
   ListIterator<CompanyBasicRec> compLi = this.getCompList().listIterator();
   comp = compLi.next();
  }
  LOGGER.log(INFO, "comp {0}", comp);
 if(input == null || input.isEmpty()){
  docSelList = docMgr.getEditableFiDocsForComp(comp);
 }else{
  try{
   input = input.trim();
  long docNumInpt = Long.parseLong(input);
  docSelList = this.docMgr.getFiDocsByDocNumPartial(comp, docNumInpt, false,getLoggedInUser(),getView());
  LOGGER.log(INFO, "after call to get docs");
  }catch(NumberFormatException ex){
   GenUtil.addErrorMessage("Not a number");
  }
  
 }
/* LOGGER.log(INFO, "docComplete returns {0}", docSelList);
 for(DocFiRec d :docSelList){
  d = docMgr.getDocDetails(d);
  List<DocLineBaseRec> lines = d.getDocLines();
  if(lines == null){
   LOGGER.log(INFO, "No lines for doc id {0}", d.getId());
  }else{
   for(DocLineBaseRec ln : lines){
    LOGGER.log(INFO, "Doc line calss {0}", ln.getClass().getSimpleName());
   }
  }
 }
 */
 return docSelList;
  
 }
public List<DocFiRec> docComplete(String input){
 LOGGER.log(INFO, "docComplete called with input{0}", input.getClass().getCanonicalName());
 if(comp == null){
  ListIterator<CompanyBasicRec> compLi = this.getCompList().listIterator();
  comp = compLi.next();
 }
 LOGGER.log(INFO, "comp {0}", comp);
 if(input == null || input.isEmpty()){
  docSelList = docMgr.getAllFiDocsForComp(comp, false,getLoggedInUser(),getView());
 }else{
  try{
   input = input.trim();
  long docNumInpt = Long.parseLong(input);
  docSelList = this.docMgr.getFiDocsByDocNumPartial(comp, docNumInpt, false,getLoggedInUser(),getView());
  LOGGER.log(INFO, "after call to get docs");
  }catch(NumberFormatException ex){
   GenUtil.addErrorMessage("Not a number");
  }
  
 }
 LOGGER.log(INFO, "docComplete returns {0}", docSelList);
 return docSelList;
} 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public String getDocLineClass() {
  return docLineClass;
 }

 public void setDocLineClass(String docLineClass) {
  this.docLineClass = docLineClass;
 }

 
 public List<ContactRec> getDocContacts() {
  return docContacts;
 }

 public void setDocContacts(List<ContactRec> docContacts) {
  this.docContacts = docContacts;
 }

 
 public List<DocLineBaseRec> getDocLines() {
  if(docLines == null){
   docLines = new ArrayList<>();
  }
  return docLines;
 }

 public void setDocLines(List<DocLineBaseRec> docLines) {
  this.docLines = docLines;
 }

 public DocLineBaseRec getDocLineSel() {
  return docLineSel;
 }

 
 public void setDocLineSel(DocLineBaseRec docLineSel) {
  this.docLineSel = docLineSel;
 }

 public boolean isDocLineGl() {
  return docLineGl;
 }

 public List<DocLineGlRec> getDocLineReconLines() {
  return docLineReconLines;
 }

 public void setDocLineReconLines(List<DocLineGlRec> docLineReconLines) {
  this.docLineReconLines = docLineReconLines;
 }

 public void setDocLineGl(boolean docLineGl) {
  this.docLineGl = docLineGl;
 }

 

 public DocLineArRec getDocClearedLineAr() {
  return docClearedLineAr;
 }

 public void setDocClearedLineAr(DocLineArRec docClearedLineAr) {
  this.docClearedLineAr = docClearedLineAr;
 }

 public DocLineApRec getDocLineApSel() {
  return docLineApSel;
 }

 public void setDocLineApSel(DocLineApRec docLineApSel) {
  this.docLineApSel = docLineApSel;
 }

 
 public DocLineArRec getDocLineArSel() {
  return docLineArSel;
 }

 public void setDocLineArSel(DocLineArRec docLineArSel) {
  this.docLineArSel = docLineArSel;
 }

 
 public DocLineGlRec getDocLineGlSel() {
  return docLineGlSel;
 }

 public void setDocLineGlSel(DocLineGlRec docLineGlSel) {
  this.docLineGlSel = docLineGlSel;
 }

 

 
 public DocFiRec getDocSel() {
  return docSel;
 }

 public void setDocSel(DocFiRec docSel) {
  this.docSel = docSel;
  
 }

 
 public List<DocFiRec> getDocSelList() {
  return docSelList;
 }

 public void setDocSelList(List<DocFiRec> docSelList) {
  this.docSelList = docSelList;
  //GenUtil.round(unrounded, precision, roundingMode);
 }

 public boolean isDocSelected() {
  return docSelected;
 }

 public void setDocSelected(boolean docSelected) {
  this.docSelected = docSelected;
 }

 
 
 
 public void onDocSelectCompl(SelectEvent evt){
  docSel = (DocFiRec)evt.getObject();
  LOGGER.log(INFO, "Selected doc is {0}", docSel);
  LOGGER.log(INFO,"invoice id {0}",docSel.getDocInvoiceAr());
  docSel = docMgr.getDocLines(docSel);
  docContacts = contMgr.contactsForDoc(null, docSel);
  double totalDebit = 0;
  double totalCredit = 0;
  List<DocLineBaseRec> docLinesList = docSel.getDocLines();
  Collections.sort(docLinesList, new DocLinesByLineNum());
  LOGGER.log(INFO,"selected Doc lines {0}",docLinesList);
  
  ListIterator<DocLineBaseRec> docLinesLi = docLinesList.listIterator();
  while(docLinesLi.hasNext()){
   
   DocLineBaseRec docLine = docLinesLi.next();
   LOGGER.log(INFO, "Docline id {0}", docLine.getId());
   LOGGER.log(INFO, "selected doc line class {0}", docLine.getClass().getSimpleName());
   String className = docLine.getClass().getSimpleName();
   String value = GenUtil.formatNumberLocDp(docLine.getDocAmount(), this.getLocale());
   LOGGER.log(INFO, "post type {0} debit {1}", new Object[]{docLine.getPostType().getDescription(), docLine.getPostType().isDebit()});
   if(docLine.getPostType().isDebit()){
    totalDebit = totalDebit + docLine.getDocAmount();
    docLine.setDebitValue(value);
    docLine.setCreditValue(GenUtil.formatNumberLocDp(0, this.getLocale()));
   }else{
    totalCredit = totalCredit + docLine.getDocAmount();
    docLine.setCreditValue(value);
    docLine.setDebitValue(GenUtil.formatNumberLocDp(0, this.getLocale()));
   }
   if(docLine.isClearingLine()){
    // set cleared values
    LOGGER.log(INFO, "Need to set cleared debit/credit amount");
    List<DocLineBaseRec> clearedLines = docLine.getClearingLineForLines();
    if(clearedLines == null){
     if(className.equals("DocLineApRec")){
      docLine = this.docMgr.getApClearedLines((DocLineApRec)docLine);
      clearedLines = docLine.getClearingLineForLines();
     }
     
    } 
    ListIterator<DocLineBaseRec> clrdLnLi = clearedLines.listIterator();
    while(clrdLnLi.hasNext()){
     DocLineBaseRec clrdLn = clrdLnLi.next();
     if(clrdLn.getPostType().isDebit()){
      clrdLn.setDebitValue(GenUtil.formatNumberLocDp(clrdLn.getDocAmount(), locale));
     }else{
      clrdLn.setCreditValue(GenUtil.formatNumberLocDp(clrdLn.getDocAmount() * -1, locale));
     }
     clrdLnLi.set(clrdLn);
    }
    docLine.setClearingLineForLines(clearedLines);
   }
   if(docLine.getClass().getSimpleName().equalsIgnoreCase("DocLineArRec")){
    List<DocLineGlRec> reconLines = ((DocLineArRec)docLine).getReconiliationLines();
    if(reconLines != null){
     ListIterator<DocLineGlRec> reconLinesLi = reconLines.listIterator();
     while(reconLinesLi.hasNext()){
      DocLineGlRec reconLn = reconLinesLi.next();
      String val = GenUtil.formatNumberLocDp(reconLn.getDocAmount(), locale);
      if(reconLn.getPostType().isDebit()){
       LOGGER.log(INFO, "Debit amount {0}", val);
       reconLn.setDebitValue(GenUtil.formatNumberLocDp(reconLn.getDocAmount(), locale));
      }else{
       LOGGER.log(INFO, "Credit amount {0}", val);
       reconLn.setCreditValue(GenUtil.formatNumberLocDp(reconLn.getDocAmount() * -1, locale));
      }
      reconLinesLi.set(reconLn);
     }
     ((DocLineArRec)docLine).setReconiliationLines(reconLines);
    }
    
   }
   docLinesLi.set(docLine); 
   
  }
  LOGGER.log(INFO, "Doc totals debit {0} credit {1}", new Object[]{totalDebit,totalCredit});
  setDocTotalDebit(GenUtil.formatCurrency(totalDebit, locale));
  setDocTotalCredit(GenUtil.formatCurrency(totalCredit, locale));        
  LOGGER.log(INFO, "Doc lines from doc{0}", docSel.getDocLines());
  
  this.docSelected = true;
 }

 public void onDocUpdate(){
  LOGGER.log(INFO, "onDocUpdate called for doc {0}", this.docSel);
  
  try{
   docSel = this.docMgr.updateDocument(docSel, this.getLoggedInUser(), this.getView());
   String msg = this.validationForKey("fiDocUpdated")+docSel.getDocNumber();
   GenUtil.addInfoMessage(msg);
  }catch(BacException ex){
   String msg = this.errorForKey("docFiUpdated")+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
  }catch(Exception ex){
   String msg = this.errorForKey("docFiUpdated")+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
  }
  
 }
 
 public String getDocTotalCredit() {
  return docTotalCredit;
 }

 public void setDocTotalCredit(String docTotalCredit) {
  this.docTotalCredit = docTotalCredit;
 }

 public String getDocTotalDebit() {
  return docTotalDebit;
 }

 public void setDocTotalDebit(String docTotalDebit) {
  this.docTotalDebit = docTotalDebit;
 }

 public void onDocAuditAction(){
  LOGGER.log(INFO, "onAuditAction called");
  auditRecs = audMgr.getAuditRecForDocFi(docSel);
  ListIterator<AuditBaseRec> li = auditRecs.listIterator();
  LOGGER.log(INFO, "doc li {0}", li);
  while(li.hasNext()){
   AuditBaseRec aud = li.next();
   String fldCode = aud.getFieldCode();
   String fldName = fieldNameForKey(fldCode);
   LOGGER.log(INFO,"fldName {0}",fldName);
   LOGGER.log(INFO, "user surname {0}, First name {1} ref {2}", new Object[]{
    aud.getCreatedBy().getFamilyName(), aud.getCreatedBy().getFirstName(),aud.getCreatedBy().getRef()
   });
   aud.setFieldName(fldName);
   li.set(aud);
  }
  
  LOGGER.log(INFO, "auditMgr returns audit recs num: {0}", auditRecs);
 }
 
 public void onDocLineAuditAction(){
  LOGGER.log(INFO, "onDocLineAuditAction called");
  auditRecs = null;
  auditRecs = audMgr.getDocLineAudit(docLineSel);
  LOGGER.log(INFO, "auditRecs size {0}", auditRecs.size());
  ListIterator<AuditBaseRec> li = auditRecs.listIterator();
  while(li.hasNext()){
   AuditBaseRec aud = li.next();
   String fldCode = aud.getFieldCode();
   String fldName = fieldNameForKey(fldCode);
   LOGGER.log(INFO,"Line fldName {0}",fldName);
   aud.setFieldName(fldName);
   li.set(aud);
  }
  
  LOGGER.log(INFO, "Line auditMgr returns audit recs num: {0}", auditRecs);
 }
 
 public void onDocLineDisplayAction(){
  
  if(docLineSel == null){
   MessageUtil.addErrorMessage("docLineSelNone", "errorText");
   return;
  }
  LOGGER.log(INFO, "onDocLineDisplayAction selected line {0}", docLineSel.getClass().getSimpleName());
  docLineClass = docLineSel.getClass().getSimpleName();
  switch (docLineClass){
   case "DocLineArRec":
    setDocLineArSel((DocLineArRec)docLineSel);
    docLineReconLines = this.docLineArSel.getReconiliationLines();
    break;
   case "DocLineGLRec":
    this.setDocLineGlSel((DocLineGlRec)docLineSel);
    break;
   case "DocLineApRec":
    setDocLineApSel((DocLineApRec)docLineSel);
    docLineReconLines = docLineApSel.getReconiliationLines();
    LOGGER.log(INFO, "Docline ap Account {0}", docLineApSel.getApAccount());
    LOGGER.log(INFO, "Docline ap Account name {0}", docLineApSel.getApAccount().getApAccountFor().getName());
    LOGGER.log(INFO, "rec lines {0}", docLineApSel.getReconiliationLines());
    if(docLineApSel.getReconiliationLines() == null){
     docLineApSel = this.docMgr.getDocLineApReconLines(docLineApSel);
     LOGGER.log(INFO, "rec lines now {0}", docLineApSel.getReconiliationLines());
    }
    break;
    
  }
  
 }
 
 public void onDocLineRowSelect(SelectEvent evt){
  LOGGER.log(INFO, "onDocLineRowSelect called with {0}", evt.getObject());
 }
 
 public void onDocLineCtxMnu(SelectEvent evt){
  LOGGER.log(INFO, "onDocLineCtxMnu called with {0}", evt.getObject());
 }
 public void onDocResetAction(){
  LOGGER.log(INFO, "onDocLineResetAction called");
  for(DocLineBaseRec ln: this.docSel.getDocLines()){
   LOGGER.log(INFO, "doc line {0} clear {1} ", new Object[]{ln.getLineNum(),ln.isReset()});
  }
  try{
   docSel = this.docMgr.docResetLines(docSel, this.getLoggedInUser(), this.getView());
   String msg = this.responseForKey("docReset");
   GenUtil.addInfoMessage(msg);
  }catch(BacException ex){
   String msg = this.errorForKey("docFiReset")+" "+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
   
  }
 }
 
 public Locale getLocale() {
  if(locale == null){
   locale = this.getUsrBuff().getLoc();
  }
  return locale;
 }

 public void setLocale(Locale locale) {
  this.locale = locale;
 }

 public StreamedContent getInvPdfile() {
  String fname = docSel.getDocInvoiceAr().getInvoiceNumber() + ".pdf";
  ByteArrayInputStream bais = new ByteArrayInputStream(docSel.getDocInvoiceAr().getInvoicePdf());
  invPdfile = new DefaultStreamedContent(bais, "application/pdf",fname);
  return invPdfile;
 }

 public void setInvPdfile(StreamedContent invPdfile) {
  this.invPdfile = invPdfile;
 }
 
 public List<PaymentTypeRec> payTypeComplete(String input){
  List<PaymentTypeRec> payTypes = this.sysBuff.getPaymentTypes(comp);
  return payTypes;
 }
 
 public List<PaymentTermsRec> payTermsComplete(String input){
  List<PaymentTermsRec> payTerms = sysBuff.getPaymentTermsList();
  return payTerms;
 }
 
}
