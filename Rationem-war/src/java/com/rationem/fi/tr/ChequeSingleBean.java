/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.tr.PaymentMediumManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.NameLocalisationMap;
import com.lowagie.text.DocumentException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
/**
 *
 * @author user
 */
public class ChequeSingleBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ChequeSingleBean.class.getName());
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private PaymentMediumManager payMediumMgr;
 
 @EJB
 private BankManager bankMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private MasterDataManager mastData;
 
 
 

 private CompanyBasicRec compSelected;
 private DocLineBaseRec  docLineSel;
 private String ledgerCode;
 private DocLineApRec docLineAp;
 private DocLineArRec docLineAr;
 private NumberRangeChequeRec chequeBook;
 private List<NumberRangeChequeRec> chequeBooks;
 private String chequeNumber;
 /**
  * Creates a new instance of ChequeSingleBean
  */
 public ChequeSingleBean() {
 }

 @PostConstruct
 private void init(){
  
  if(getCompList() == null || getCompList().isEmpty()){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  compSelected = getCompList().get(0);
  ledgerCode = "AP";
  
 }
 
 
 public NumberRangeChequeRec getChequeBook() {
  return chequeBook;
 }

 public void setChequeBook(NumberRangeChequeRec chequeBook) {
  this.chequeBook = chequeBook;
  
 }

 public List<NumberRangeChequeRec> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeChequeRec> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 public String getChequeNumber() {
  return chequeNumber;
 }

 public void setChequeNumber(String chequeNumber) {
  this.chequeNumber = chequeNumber;
 }
 
 
 
 public CompanyBasicRec getCompSelected() {
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }

 public DocLineApRec getDocLineAp() {
  return docLineAp;
 }

 public void setDocLineAp(DocLineApRec docLineAp) {
  this.docLineAp = docLineAp;
  
 }

 public DocLineArRec getDocLineAr() {
  return docLineAr;
 }

 public void setDocLineAr(DocLineArRec docLineAr) {
  this.docLineAr = docLineAr;
 }
 
 

 
 public DocLineBaseRec getDocLineSel() {
  return docLineSel;
 }

 public void setDocLineSel(DocLineBaseRec docLineSel) {
  this.docLineSel = docLineSel;
 }

 public String getLedgerCode() {
  return ledgerCode;
 }

 public void setLedgerCode(String ledgerCode) {
  this.ledgerCode = ledgerCode;
 }
 
 public StreamedContent getChequePdf(){
  LOGGER.log(INFO, "onChequeData called");
  String className = docLineSel.getClass().getSimpleName();
  Map<String, String> numTexts = null;
  if(docLineAp != null){
   LOGGER.log(INFO, "currency name {0}", docLineAp.getComp().getCurrency().getMajorUnitDescr());
   LOGGER.log(INFO, "currency plural {0}", docLineAp.getComp().getCurrency().getMajorUnitDescrPl());
   numTexts = getUsrBuff().getNumberTexts();
  }
  
  /*if(1 == 1){
   List<String> chequeAmount = 
     GenUtilServer.getChequeAmountWords(docLineAp.getComp().getCurrency(), 9782341.54,numTexts);
   LOGGER.log(INFO, "chequeAmount {0}", chequeAmount);
   LOGGER.log(INFO,"number map {0}",getUsrBuff().getNumberTexts());
  
   
   return null;
  }*/
  
  PaymentTypeRec pType = null;
  switch(className){
   case "DocLineApRec":
    pType = docLineAp.getPayType();
    break;
   case "DocLineArRec":
    pType = docLineAp.getPayType();
    break;
  }
  
  if(pType == null){
   LOGGER.log(INFO, "No payment type found for doc id {0}", docLineSel.getId());
   return null;
  }
  
  ChequeTemplateRec chqTemplate =  pType.getChqTemplate();
  LOGGER.log(INFO,"chqTemplate from pType {0}",chqTemplate);
  BankAccountCompanyRec bnkAcntCmp = pType.getPayTypeForBankAccount();
  if(this.chequeBook.isAutoNum()){
   // need to determine the next cheque number
   NumberRangeRec nr = mastData.getNumberRangeNextVal(chequeBook);
   chequeNumber = String.valueOf(nr.getNextNum());
  }
  LOGGER.log(INFO, className, pType);
  
  LOGGER.log(INFO, "Bank Payment line {0}", docLineAp.getBankLine());
  DocBankLineChqRec chqLine ;
  if(docLineAp.getBankLine() == null){
   //return null;
   //need to create bank line
   chqLine = new DocBankLineChqRec();
   chqLine.setAmount(docLineAp.getHomeAmount());
   chqLine.setUnClearedBankAc(bnkAcntCmp);
   chqLine.setApAccount(docLineAp.getApAccount());
   
   chqLine.setBnkRef(chequeNumber);
   
   //chqLine = (DocBankLineChqRec)this.bankMgr.updateDocBankLine(chqLine, docLineSel, pType, getView());
   
   
  }else{
   chqLine = (DocBankLineChqRec) docLineAp.getBankLine();
  }
  
  chqLine.setFileType("PDF");
  StreamedContent chqPdfStreamed;
  LOGGER.log(INFO, "chqTemplate from payType {0}", chqTemplate);
  boolean newChqTemplate = false;
  if(chqTemplate == null){
   chqTemplate = docLineAp.getPayType().getPayTypeForBankAccount().getChequeTemplate();
   //LOGGER.log(INFO, "chqTemplate id {0}", chqTemplate.getId());
   if(chqTemplate == null ||  chqTemplate.getPdfData() == null ){
    // not loaded into DB yet so get default
    FacesContext fc = FacesContext.getCurrentInstance();
    InputStream docIs;
    InputStream pdfIs ;
    if(chqTemplate == null){
     chqTemplate = new ChequeTemplateRec();
     chqTemplate.setCreatedBy(this.getLoggedInUser());
     chqTemplate.setCreatedOn(new Date());
     chqTemplate.setReference(formTextForKey("trChqTemplDefRef"));
     chqTemplate.setDescription(formTextForKey("trChqTemplDefDescr"));
     newChqTemplate = true;
    }
    try{
     docIs = fc.getExternalContext().getResourceAsStream("/resources/pdfTemplate/ChequeRemittance.doc");
     byte[] doc = IOUtils.toByteArray(docIs);
     chqTemplate.setOriginalData(doc);
     chqTemplate.setOriginalFileExt("doc");
    }catch(IOException ex){
     LOGGER.log(INFO, "Could not load file ChequeRemittance.doc error {0}",ex.getLocalizedMessage());
    }
    try{
     pdfIs = fc.getExternalContext().getResourceAsStream("/resources/pdfTemplate/ChequeRemittance.pdf");
     byte[] pdf = IOUtils.toByteArray(pdfIs);
     chqTemplate.setPdfData(pdf);
     chqTemplate.setPdfFileExt("pdf");
    }catch(IOException ex){
     LOGGER.log(INFO, "Could not load file ChequeRemittance.pdf error {0}",ex.getLocalizedMessage());
    }
    LOGGER.log(INFO, "chqTemplate pdf data {0}", chqTemplate.getPdfData());
    if(newChqTemplate){
     chqTemplate.setCreatedBy(getLoggedInUser());
     chqTemplate.setCreatedOn(new Date());
    }else{
     chqTemplate.setChangedBy(getLoggedInUser());
     chqTemplate.setChangedOn(new Date());
    }
    LOGGER.log(INFO, "About to call pay meduim Mgr update temple");
    chqTemplate = payMediumMgr.updateChequeTemplate(chqTemplate,docLineAp.getPayType().getPayTypeForBankAccount(),
      docLineAp.getPayType(), getView());
    LOGGER.log(INFO, "chqTemplate id {0}", chqTemplate.getId());
    docLineAp.getPayType().getPayTypeForBankAccount().setChequeTemplate(chqTemplate);
    
    
   }
   docLineAp.getPayType().setChqTemplate(chqTemplate);
   docLineAp.getPayType().setHasCheqTemplate(true);
    
   
  }
  try{
   LOGGER.log(INFO, "Make cheque");
   LOGGER.log(INFO, "Template to be used {0}", chqTemplate.getId());
   LOGGER.log(INFO, "print cheque line {0}", chqTemplate.isPrintChqNumLine());
   LOGGER.log(INFO, "Template  pdf data {0}", chqTemplate.getPdfData());
   
   List<NameLocalisationMap> tabTitles = new ArrayList<>();
   String headerDate = this.fieldNameForKey("CHQ_PD_DATE");
   NameLocalisationMap dateName = new NameLocalisationMap();
   dateName.setName("CHQ_PD_DATE");
   dateName.setNameLocal(headerDate);
   tabTitles.add(dateName);
   
   String headerRef = this.fieldNameForKey("CHQ_PD_INV_REF");
   NameLocalisationMap refName = new NameLocalisationMap();
   refName.setName("CHQ_PD_INV_REF");
   refName.setNameLocal(headerRef);
   tabTitles.add(refName);
   
   String headerDescr = this.fieldNameForKey("CHQ_PD_INV_DESC");
   LOGGER.log(INFO, "headerDescr {0}", headerDescr);
   NameLocalisationMap descName = new NameLocalisationMap();
   descName.setName("CHQ_PD_INV_DESC");
   descName.setNameLocal(headerDescr);
   tabTitles.add(descName);
   
   String headerAmnt = this.fieldNameForKey("CHQ_PD_INV_AMNT");
   NameLocalisationMap amntName = new NameLocalisationMap();
   amntName.setName("CHQ_PD_INV_AMNT");
   amntName.setNameLocal(headerAmnt);
   tabTitles.add(amntName);
   
   for(NameLocalisationMap map:tabTitles){
    LOGGER.log(INFO, "key {0} value {1}", new Object[]{map.getName(),map.getNameLocal()});
   }
   /*if(1 == 1){
    LOGGER.log(INFO, "chqTemplate data before call to format chq {0}", chqTemplate.getPdfData());
    
    return null;
   }
   
   */
   byte[] chqPdf =  payMediumMgr.getApChequePdf(docLineAp, chqLine,chqTemplate,
     tabTitles,getUsrBuff().getNumberTexts()).toByteArray();
   ByteArrayInputStream bis = new ByteArrayInputStream(chqPdf);
   chqPdfStreamed = new DefaultStreamedContent(bis,"application/pdf","cheque.pdf");
   chqLine.setFileType("PDF");
   chqLine.setFileContent(chqPdf);
   
   
   if(chqLine.getId() == null){
    chqLine.setCreatedBy(getLoggedInUser());
    chqLine.setCreatedOn(new Date());
   }else{
    chqLine.setChangedBy(getLoggedInUser());
    chqLine.setChangedOn(new Date());
   }
   chqLine.setComp(docLineSel.getComp());
   
   chqLine = (DocBankLineChqRec)bankMgr.updateDocBankLine(chqLine, docLineSel, pType, getView());
   LOGGER.log(INFO, "After Cheque line update DocBankLineChqRec id {0}", chqLine.getId());
   return chqPdfStreamed;
  }catch(IOException x){
   LOGGER.log(INFO, "Could not read template information", x);
  }catch(DocumentException x){
   LOGGER.log(INFO, "Could not create Cheque {0}", x.getLocalizedMessage());
  }
  return null;
  
  
  
  
   
  
 }
 
 public List<NumberRangeChequeRec> onChequeBookComplete(String input){
  
  List<NumberRangeChequeRec> chqBkList = this.getChequeBooks();
  if(StringUtils.isBlank(input)){
   return chqBkList;
  }else{
   List<NumberRangeChequeRec> retList = new ArrayList<>();
   for(NumberRangeChequeRec curr: chqBkList){
    if(curr.getShortDescr().startsWith(input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
 
 public void onChequeBookSelItem(SelectEvent evt){
  LOGGER.log(INFO, "onChequeBookSelItem called with {0}", evt.getObject());
  chequeBook = (NumberRangeChequeRec)evt.getObject();
  if(!chequeBook.isAutoNum()){
   PrimeFaces.current().ajax().update("chqSinglFrm:chqNum");
  }
  PrimeFaces.current().ajax().update("chqSinglFrm:chqDownLoad");
 }
 
 public void onChequeNumberChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onChequeNumberChange value {0}", evt.getNewValue().getClass().getSimpleName());
  String newVal = (String)evt.getNewValue();
  chequeNumber = StringUtils.leftPad(newVal, 8, '0');
  LOGGER.log(INFO, "chequeNumber {0}", chequeNumber);
  PrimeFaces.current().ajax().update("chqSinglFrm:chqNum");
  
 }
 
 
 public void onDocLineSelItem(SelectEvent evt){
  LOGGER.log(INFO, "onDocLineSelItem called with {0}", evt.getObject());
  
  String className = evt.getObject().getClass().getSimpleName();
  BankAccountCompanyRec bnkAcnt = null;
  switch(className){
   case "DocLineApRec":
    docLineAp = (DocLineApRec)evt.getObject();
    LOGGER.log(INFO, "payment type {0}", docLineAp.getPayType());
    LOGGER.log(INFO, "payment type bank {0}", docLineAp.getPayType().getPayTypeForBankAccount());
    
    bnkAcnt = docLineAp.getPayType().getPayTypeForBankAccount();
    break;
   case "DocLineArRec":
    docLineAr = (DocLineArRec)evt.getObject();
    bnkAcnt = docLineAr.getPayType().getPayTypeForBankAccount();
    break;
  }
  if(bnkAcnt == null){
   if(className.equals("DocLineApRec")){
    LOGGER.log(INFO, "Could not determine bank account for  APline {0}", docLineAp.getId());
   }
  }else{
   if(chequeBooks == null){
    bnkAcnt = bankMgr.getChequeBooksForBankAcnt(bnkAcnt);
    chequeBooks = bnkAcnt.getChequeBooks();
   }
  }
  LOGGER.log(INFO, "chequeBooks {0}", chequeBooks);
  if(chequeBooks != null){
   LOGGER.log(INFO, "activate cheque books");
   PrimeFaces.current().ajax().update("chqSinglFrm:cb");
  }
  /*
  if(docLineAp != null){
   LOGGER.log(INFO, "docLineAp {0}", docLineAp);
   PrimeFaces.current().ajax().update("chqSinglFrm:chqDownLoad");
  }
*/
 }
 
 public List<DocLineApRec> onDocLineSel(String input){
  
  List<DocLineApRec> retList = null; //new ArrayList<>();
  if(StringUtils.isBlank(input)){
   if(StringUtils.equals(ledgerCode, "AP")){
    retList = this.docMgr.getChequeApNotIssued(compSelected, 0l, "AP_PAY");
   }else{
    retList = this.docMgr.getChequeApNotIssued(compSelected, 0l, "AR_PAY");
   }
  }else{
   try{
    Long docNum = Long.valueOf(input);
    if(StringUtils.equals(ledgerCode, "AP")){
     retList = docMgr.getChequeApNotIssued(compSelected, docNum, "AP_PAY");
    }else{
     retList = docMgr.getChequeApNotIssued(compSelected, docNum, "AR_PAY");
    }
   }catch(NumberFormatException ex){
    
    MessageUtil.addErrorMessageParam1("fiDocNumFormat", "errorText", input);
   }

  }
  
  return retList;
 }
 
 public void onLedgerSelect(SelectEvent evt){
  LOGGER.log(INFO, "onLedgerSelect called with {0}", evt.getObject());
  
 }
 
 
}
