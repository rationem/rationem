/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocVatSummary;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.helper.comparitor.PostTypeByDrDescr;
import com.rationem.util.BaseBean;
import com.rationem.util.FundBalance;
import com.rationem.util.MessageUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author user
 */
public class ApDocPost extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ApDocPost.class.getName());
 
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private MasterDataManager ptnrMgr;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 @EJB
 private ProgrammeMgr progMgr;
 
 @EJB
 private DocumentManager docMgr;
 

 private DocFiRec invoice;
 private List<DocLineApRec> invOpenList;
 private DocFiRec creditNote;
 private double docTotal;
 private DocLineApRec docLineAp;
 private DocLineGlRec docLineGl;
 private DocLineBaseRec docLineSel;
 
 private List<DocLineGlRec> docLinesGl;
 private List<DocTypeRec> documentTypes;
 private List<PaymentTermsRec> paymentTermsList;
 private List<PaymentTypeRec> paymentTypes;
 private PartnerPersonRec contactApNew;
 private List<PartnerPersonRec> currPersList;
 private boolean currPersMerge;
 private PartnerPersonRec currPersSelected;
 
 private boolean invWizSkip;
 private boolean vatCdSelected = false;
 private double totalDebit;
 private double totalCredit;
 private String currView;
 private int step;
 private int lines;
 private String stepText;
 private List<String> stepTexts;
 
 
 /**
  * Creates a new instance of ApDocPost
  */
 public ApDocPost() {
 }
 
 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "getViewSimple() {0}",getViewSimple());
  LOGGER.log(INFO, "init start");
  LOGGER.log(INFO, "compList {0}", getCompList());
  currView = getViewSimple();
  step = 0;
  stepTexts = new ArrayList<>();
  
  
  
  if(getCompList() != null){
   LOGGER.log(INFO, "getCompList() {0}", getCompList());
   // Get document types
   List<DocTypeRec> dTypes = sysBuff.getDocTypes();
   documentTypes = new ArrayList<>();
   if(dTypes != null){
    for(DocTypeRec dt:dTypes){
     if(dt.isApAllowed()){
      documentTypes.add(dt);
     }
    }
   }
   LOGGER.log(INFO,"documentTypes {0}",documentTypes);
   
   // get payment terms
   paymentTermsList = sysBuff.getPaymentTermsList();
   
   
  
   switch(currView){
    case "apInvoiceCr":
     stepTexts.add(formTextForKey("docHdr"));
     stepTexts.add(formTextApForKey("apDocPayPnl"));
     stepTexts.add(formTextForKey("docsPnl"));
     stepTexts.add(formTextForKey("notesPnlDoc"));
     stepTexts.add(formTextForKey("docLinesPnl"));
     invoice = new DocFiRec();
     invoice.setCompany(getCompList().get(0));
     invoice.setCreateOn(new Date());
     invoice.setCreatedBy(this.getLoggedInUser());
     LOGGER.log(INFO, "Invoice Company {0}", invoice.getCompany());
     LineTypeRuleRec lnTyAp = sysBuff.getLineTypeRuleByCode("AP");
     //PostTypeRec pt = sysBuff.getPostTypeForCode("apInv");
     docLineAp = new DocLineApRec();
     docLineAp.setLineType(lnTyAp);
     //docLineAp.setPostType(pt);
     docLineAp.setCreateDate(invoice.getCreateOn());
     docLineAp.setCreateBy(invoice.getCreatedBy());
   
     // get payment types
     paymentTypes = new ArrayList<>();
     List<PaymentTypeRec> ptypes = sysBuff.getPaymentTypes(invoice.getCompany());
     for(PaymentTypeRec pty:ptypes){
      if(pty.getLedger().getCode().equals("AP")){
       paymentTypes.add(pty);
      }
     }
     break;
    case "apCreditNoteCr":
     LOGGER.log(INFO, "apCreditNoteCr");
     creditNote = new DocFiRec();
     creditNote.setCompany(getCompList().get(0));
     creditNote.setCreateOn(new Date());
     creditNote.setCreatedBy(this.getLoggedInUser());
     LOGGER.log(INFO, "Invoice Company {0}", creditNote.getCompany());
     lnTyAp = this.sysBuff.getLineTypeRuleByCode("AP");
    // pt = sysBuff.getPostTypeForCode("apCrn");
     docLineAp = new DocLineApRec();
     docLineAp.setLineType(lnTyAp);
     //docLineAp.setPostType(pt);
     docLineAp.setCreateDate(creditNote.getCreateOn());
     docLineAp.setCreateBy(creditNote.getCreatedBy());
   
     // get payment types
     paymentTypes = new ArrayList<>();
     ptypes = sysBuff.getPaymentTypes(creditNote.getCompany());
     for(PaymentTypeRec pty:ptypes){
      if(pty.getLedger().getCode().equals("AP")){
       paymentTypes.add(pty);
      }
     }
     break;
    }
   stepText = stepTexts.get(step);
  }
 }
 
 public List<DocVatSummary> updateVatSummary(List<DocVatSummary> vatSumm, DocLineGlRec ln){
  LOGGER.log(INFO, "updateVatSummary called ");
  
  boolean addSummRec = false;
  boolean foundSummRec = false;
  if(vatSumm.isEmpty()){
   addSummRec = true;
  }else{
   ListIterator<DocVatSummary> vatSumLi = vatSumm.listIterator();
   while(vatSumLi.hasNext() && !foundSummRec){
    DocVatSummary curr =  vatSumLi.next();
    if(Objects.equals(curr.getVatCode().getId(), ln.getVatCodeCompany().getId())){
     // found vat Code
     if(Objects.equals(curr.getFund().getId(), ln.getRestrictedFund().getId())){
      // found fund for this vat entry
      double goods = curr.getGoods();
      double vat = curr.getVat();
      if(ln.getVatCodeCompany().getIrrecoverRate() == 0.0){
       // no irrecoverable tax so do not need GL account
       foundSummRec = true;
       goods += ln.getDocAmount();
       vat += ln.getTaxAmnt();
       curr.setGoods(goods);
       curr.setVat(vat);
      }else{
       // irrecoverable so need GL account and irrecoverable amount
       if(Objects.equals(curr.getGlAccount().getId(), ln.getGlAccount().getId())){
        // found gl account
        foundSummRec = true;
        double irrecoverable = curr.getIrrecoverableVat();
        goods += ln.getDocAmount();
        vat += ln.getTaxAmnt();
        double lnIrrecoverable = ln.getVatCodeCompany().getIrrecoverRate() * goods;
        irrecoverable += lnIrrecoverable;
        curr.setGoods(goods);
        curr.setVat(vat);
        curr.setIrrecoverableVat(irrecoverable);
       }
      }
     }
    }
    if(foundSummRec){
     vatSumLi.set(curr);
    }else{
     addSummRec = true;
    }
   }
   
   
  }
  if(addSummRec){
    // need to Add new VatSummary record
    DocVatSummary newSumm = new DocVatSummary();
    newSumm.setFund(ln.getRestrictedFund());
    newSumm.setGlAccount(ln.getGlAccount());
    newSumm.setGoods(ln.getDocAmount());
    newSumm.setVat(ln.getTaxAmnt());
    newSumm.setVatCode(ln.getVatCodeCompany());
    if(ln.getVatCodeCompany().getIrrecoverRate() != 0.0){
     newSumm.setIrrecoverable(true);
     double irrecoverable = ln.getVatCodeCompany().getIrrecoverRate() * ln.getDocAmount();
     newSumm.setIrrecoverableVat(irrecoverable);
    }
   
   }
  return vatSumm;
 }


 public DocFiRec getInvoice() {
  return invoice;
 }

 public List<FundBalance> getFundBalForDoc(List<DocLineBaseRec> lines){
  LOGGER.log(INFO, "getFundBalForDoc called with num lines {0}", lines);
  if(lines == null){
   return null;
   
  }
    
  List<FundBalance> fndBalList = new ArrayList<>();
  for(DocLineBaseRec curr: lines){
   LOGGER.log(INFO, "Line class {0}", curr.getClass().getSimpleName());
   if(curr.getClass().getSimpleName().equals("DocLineGLRec")){
    DocLineGlRec currLineGl = (DocLineGlRec)curr;
    double docAmount = currLineGl.getDocAmount();
    if(!currLineGl.getPostType().isDebit()){
      docAmount *= -1; 
    }
    LOGGER.log(INFO, "docAmnt {0}", docAmount);
    LOGGER.log(INFO, "fndBalList {0}", fndBalList.size());
    if(fndBalList.isEmpty()){
     LOGGER.log(INFO, "empty bal list so add");
     FundBalance fndBal = new FundBalance();
     fndBal.setFund( currLineGl.getRestrictedFund());
     fndBal.setAmount(currLineGl.getDocAmount());
     fndBalList.add(fndBal);
    }else{
     boolean found = false;
     LOGGER.log(INFO, "Fund bal has entry");
     // update FundBalance with amount for line
     ListIterator<FundBalance> li = fndBalList.listIterator();
     while(li.hasNext() && !found){
      FundBalance currBal = li.next();
      LOGGER.log(INFO, "currLineGl.getRestrictedFund() {0} currBal.getFund() {1}", new Object[]{
       currLineGl.getRestrictedFund(),currBal.getFund() });
      if(currLineGl.getRestrictedFund() == null && currBal.getFund() ==  null ){
       double balAmnt = currBal.getAmount();
       balAmnt += docAmount;
       currBal.setAmount(balAmnt);
       li.set(currBal);
      }else if (currLineGl.getRestrictedFund() != null && currBal.getFund() ==  null &&
        Objects.equals(currLineGl.getRestrictedFund().getId(), currBal.getFund().getId())){
       double balAmnt = currBal.getAmount();
       balAmnt += docAmount;
       currBal.setAmount(balAmnt);
       li.set(currBal);
      }
     }
     if(!found){
      // fund does not already exist so need to create
      LOGGER.log(INFO, "Fund {0} not in current list so adding ", currLineGl.getRestrictedFund().getName());
      FundBalance fndBal = new FundBalance();
      fndBal.setFund( currLineGl.getRestrictedFund());
      fndBal.setAmount(currLineGl.getDocAmount());
      fndBalList.add(fndBal);
     }
     
     
    }
    
   }
   LOGGER.log(INFO, "Fund bal list at end on line processing {0}", curr);
  }
  return fndBalList;
 }
 
 public void setInvoice(DocFiRec invoice) {
  this.invoice = invoice;
 }

 
 public List<DocLineApRec> getInvOpenList() {
  
  if(invOpenList == null){
   invOpenList = docMgr.getDocLinesApOpenForAcnt(docLineAp.getApAccount());
  }
  return invOpenList;
 }

 public void setInvOpenList(List<DocLineApRec> invOpenList) {
  this.invOpenList = invOpenList;
 }

 public int getLines() {
  return lines;
 }

 public void setLines(int lines) {
  this.lines = lines;
 }

 
 private DocLineGlRec buildReconGlLine(DocLineApRec apLine, DocFiRec apDoc,LineTypeRuleRec lnTyGl){
  
  FiGlAccountCompRec recAcnt = apLine.getApAccount().getReconciliationAc();
  DocLineGlRec apRec = new DocLineGlRec();
  apRec.setGlAccount(recAcnt);
  //apRec.setDocAmount(apLine.getDocAmount());
  apRec.setAccountRef(recAcnt.getCoaAccount().getRef());
  apRec.setSortOrder(apLine.getSortOrder());
  apRec.setComp(apLine.getComp());
  apRec.setCreateBy(apLine.getCreateBy());
  apRec.setCreateDate(apLine.getCreateDate());
  apRec.setDocFi(apDoc);
  apRec.setLineText(apLine.getLineText());
  apRec.setLineType(lnTyGl);
  // determine post type from apLIne
  PostTypeRec ptRec;
  if(apLine.getPostType().isDebit()){
   ptRec = sysBuff.getPostTypeForCode("glDrRecon");
  }else{
   ptRec = sysBuff.getPostTypeForCode("glCrRecon");   
  }
  
  apRec.setPostType(ptRec);
  apRec.setReference1(docLineAp.getReference1());
  apRec.setReference2(docLineAp.getReference2());
  apRec.setVatCodeCompany(docLineAp.getVatCodeCompany());
  return apRec;
 }
 
 private List<DocLineGlRec> buildInvReconLines(DocLineApRec apLine,List<FundBalance> fndbal,
   DocFiRec apDoc, LineTypeRuleRec lnTyGl ){
  LOGGER.log(INFO, "getInvReconLines apLine {0} fundBal {1}", new Object[]{apLine,fndbal} );
  List<DocLineGlRec> retList = new ArrayList<>();
  if(fndbal == null || fndbal.isEmpty()){
   DocLineGlRec recLine = buildReconGlLine(apLine, apDoc, lnTyGl);
   recLine.setDocAmount(apLine.getDocAmount());
   retList.add(recLine);
  }else{
   for(FundBalance curr:fndbal){
    DocLineGlRec recLine = buildReconGlLine(apLine, apDoc, lnTyGl);
    recLine.setDocAmount(curr.getAmount());
    recLine.setRestrictedFund(curr.getFund());
    retList.add(recLine);
   }
  }
  LOGGER.log(INFO, "Rec lines {0}", retList);
  return retList;
 } 
 
 public boolean isInvWizSkip() {
  return invWizSkip;
 }

 public void setInvWizSkip(boolean invWizSkip) {
  this.invWizSkip = invWizSkip;
 }

 
 public List<PaymentTermsRec> getPaymentTermsList() {
  return paymentTermsList;
 }

 public void setPaymentTermsList(List<PaymentTermsRec> paymentTermsList) {
  this.paymentTermsList = paymentTermsList;
 }

 public Date getPayTermsDueDate(PaymentTermsRec payTerm){
  LOGGER.log(INFO, "getDueDate called with payment term {0} ", 
          new Object[]{ payTerm});
  Date retDate = new Date();
  if(payTerm == null){
   payTerm = getPaymentTermsList().get(0);
  }
  if(payTerm.getBaseType().equalsIgnoreCase("docDT")){
   
   retDate = invoice.getDocumentDate();
  }
  
  Calendar cal = Calendar.getInstance();
  cal.setTime(retDate);
  if(payTerm.getDayOfMonth() == 0){
   // day of month is zero so must be days
   int days = payTerm.getDays();
   cal.add(Calendar.DATE, days);
   retDate = cal.getTime();
   return retDate;
  }
  return retDate;
 }
 public List<PaymentTypeRec> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public int getStep() {
  return step;
 }

 public void setStep(int step) {
  this.step = step;
 }

 public String getStepName() {
  return stepText;
 }

 public void setStepName(String stepText) {
  this.stepText = stepText;
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
 
 

 public boolean isVatCdSelected() {
  return vatCdSelected;
 }

 public void setVatCdSelected(boolean vatCdSelected) {
  this.vatCdSelected = vatCdSelected;
 }

 
 public PartnerPersonRec getContactApNew() {
  return contactApNew;
 }

 public void setContactApNew(PartnerPersonRec contactApNew) {
  this.contactApNew = contactApNew;
 }
 
 

 public DocFiRec getCreditNote() {
  return creditNote;
 }

 public void setCreditNote(DocFiRec creditNote) {
  this.creditNote = creditNote;
 }

 public List<PartnerPersonRec> getCurrPersList() {
  return currPersList;
 }

 public void setCurrPersList(List<PartnerPersonRec> currPersList) {
  this.currPersList = currPersList;
 }

 
 public DocLineApRec getDocLineAp() {
  return docLineAp;
 }

 public boolean isCurrPersMerge() {
  return currPersMerge;
 }

 public void setCurrPersMerge(boolean currPersMerge) {
  this.currPersMerge = currPersMerge;
 }

 
 public PartnerPersonRec getCurrPersSelected() {
  return currPersSelected;
 }

 public void setCurrPersSelected(PartnerPersonRec currPersSelected) {
  this.currPersSelected = currPersSelected;
 }

 public void setDocLineAp(DocLineApRec docLineAp) {
  this.docLineAp = docLineAp;
 }

 public DocLineBaseRec getDocLineSel() {
  return docLineSel;
 }

 public void setDocLineSel(DocLineBaseRec docLineSel) {
  this.docLineSel = docLineSel;
 }

 
 public DocLineGlRec getDocLineGl() {
  return docLineGl;
 }

 public void setDocLineGl(DocLineGlRec docLineGl) {
  this.docLineGl = docLineGl;
 }

 public List<DocLineGlRec> getDocLinesGl() {
  return docLinesGl;
 }

 public void setDocLinesGl(List<DocLineGlRec> docLinesGl) {
  this.docLinesGl = docLinesGl;
 }

 public double getDocTotal() {
  return docTotal;
 }

 public void setDocTotal(double docTotal) {
  this.docTotal = docTotal;
 }

 public List<DocTypeRec> getDocumentTypes() {
  return documentTypes;
 }

 public void setDocumentTypes(List<DocTypeRec> documentTypes) {
  this.documentTypes = documentTypes;
 }
 
 
 public void onAddLineGlAcntSel(SelectEvent evt){
  LOGGER.log(INFO, "onAddLineGlAcntSel called");
  FiGlAccountCompRec glAcnt = (FiGlAccountCompRec)evt.getObject();
  String sortText;
  if(StringUtils.contains(getViewSimple(), "apInvoiceCr")){
   sortText = this.getSortTextGl(glAcnt.getSortOrder(), invoice, docLineAp.getApAccount(), null);
  }else{
   sortText = this.getSortTextGl(glAcnt.getSortOrder(), creditNote, docLineAp.getApAccount(), null);
  }
   
  docLineGl.setSortOrder(sortText);
  if(StringUtils.contains(getViewSimple(), "apInvoiceCr")){
   RequestContext.getCurrentInstance().update("addDocLnFrm:addLnSort");
  }else if(StringUtils.contains(getViewSimple(), "apCreditNoteCr")){
   RequestContext.getCurrentInstance().update("addLineFrm:addLnSort");
  }
  
  
  
 }
 public void onAddLineTrf(){
  LOGGER.log(INFO, "onAddLineTrf called view {0} ",getViewSimple());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  //rCtx.execute("PF('docLinesWv').addRow()");
  List<String> updt = new ArrayList<>();
  if(StringUtils.equals(getViewSimple(), "apInvoiceCr")){
   
   if(docLineGl.getDocAmount() == 0){
    MessageUtil.addWarnMessage("apDocLnAmntNone", "validationText");
    RequestContext.getCurrentInstance().update("addDocLnMsg");
    return;
   }
   if(docLineGl.getDocAmount() < 0){
    MessageUtil.addWarnMessage("fiDocAmntNeg", "validationText");
    RequestContext.getCurrentInstance().update("addDocLnMsg");
    return;
   }
   LOGGER.log(INFO, "After check abount");
   UUID uuid = UUID.randomUUID();
   long id = uuid.getLeastSignificantBits();
   docLineGl.setId(id);
   boolean debit = docLineGl.getPostType().isDebit();
   docLineGl.setHomeAmount(docLineGl.getDocAmount());
   
   if(debit){
    docTotal += docLineGl.getHomeAmount();
    docTotal += docLineGl.getTaxAmnt();
    totalDebit += docLineGl.getHomeAmount();
   }else{
    docTotal -= docLineGl.getHomeAmount();
    docTotal -= docLineGl.getTaxAmnt();
    totalCredit += docLineGl.getHomeAmount();
   }
   if(invoice.getDocLines() == null){
    invoice.setDocLines(new ArrayList<DocLineBaseRec>());
   }
   invoice.getDocLines().add(docLineGl);
   LOGGER.log(INFO, "docLineGl.getDocAmount() {0}", docLineGl.getDocAmount());
   LOGGER.log(INFO, "docLineGl id {0}",docLineGl.getId());
   LOGGER.log(INFO, "docTotal is now {0}", docTotal);
   LOGGER.log(INFO, "Num doc lines  {0}", invoice.getDocLines().size());
  
   //updt.add("docAmount");
   lines = invoice.getDocLines().size();
   updt.add("apInvCrFrm:linesCxtMnu");
   updt.add("apInvCrFrm:saveBtn");
   updt.add("apInvCrFrm:lines");
   rCtx.execute("PF('addDocLnWv').hide()");
   rCtx.update(updt);
  }else{
   // credit note
   // validate entry
   if(docLineGl.getDocAmount() == 0){
    MessageUtil.addWarnMessage("apDocLnAmntNone", "validationText");
    RequestContext.getCurrentInstance().update("addLineFrm:addDocLnMsg");
    return;
   }
   if(docLineGl.getDocAmount() < 0){
    MessageUtil.addWarnMessage("fiDocAmntNeg", "validationText");
    RequestContext.getCurrentInstance().update("addLineFrm:addDocLnMsg");
    return;
   }
   LOGGER.log(INFO, "After check abount");
   UUID uuid = UUID.randomUUID();
   long id = uuid.getLeastSignificantBits();
   boolean debit = docLineGl.getPostType().isDebit();
   docLineGl.setHomeAmount(docLineGl.getDocAmount());
   docLineGl.setId(id);
   if(debit){
    docTotal -= docLineGl.getHomeAmount();
    docTotal -= docLineGl.getTaxAmnt();
    totalDebit += docLineGl.getHomeAmount();
   }else{
    docTotal += docLineGl.getHomeAmount();
    docTotal += docLineGl.getTaxAmnt();
    totalCredit += docLineGl.getHomeAmount();
   }
   if(creditNote.getDocLines() == null){
    creditNote.setDocLines(new ArrayList<DocLineBaseRec>());
   }
   creditNote.getDocLines().add(docLineGl);
   
   
   updt.add("apCrnFrm:docAmount");
   updt.add("apCrnFrm:lines");
   rCtx.execute("PF('addDocLnWv').hide()");
   rCtx.update(updt);
  }
  
 }
 
  
 public  String onInvoiceSave(){
  LOGGER.log(INFO, "onInvoiceSave called");
  DocFiRec postingDoc;
 
  if(currView.equals("apInvoiceCr")){
   postingDoc = invoice;
  }else{
   postingDoc = creditNote;
  }
  
  if(postingDoc.getDocLines() == null || postingDoc.getDocLines().isEmpty()){
   MessageUtil.addWarnMessage("docNoLines", "errorText");
   return null;
   
  }
  if(docTotal <= 0){
   MessageUtil.addWarnMessage("apInvTot", "errorText");
   return null;
  }
  
  long lineNum = 0l;
  
  // set GL line type
  List<FundBalance> fndbal = getFundBalForDoc(postingDoc.getDocLines());
  LOGGER.log(INFO, "fndbal {0}", fndbal);
  LOGGER.log(INFO, "fndbal lines {0}", fndbal.size());
  List<DocVatSummary> vatSummary = new ArrayList<>();
  LineTypeRuleRec lnTyGl = sysBuff.getLineTypeRuleByCode("GL");
  LineTypeRuleRec lnTyAp = sysBuff.getLineTypeRuleByCode("AP");
  ListIterator<DocLineBaseRec> lnGLIt = postingDoc.getDocLines().listIterator();
  double netDocBal = 0.0;
  while(lnGLIt.hasNext()){
   lineNum++;
   DocLineBaseRec curr = lnGLIt.next();
   LOGGER.log(INFO, "Process line {0}", lineNum);
   curr.setId(null);
   curr.setCreateBy(postingDoc.getCreatedBy());
   curr.setCreateDate(postingDoc.getCreateOn());
   curr.setComp(postingDoc.getCompany());
   
   curr.setLineNum(lineNum);
   if(curr.getPostType().isDebit()){
    netDocBal += curr.getDocAmount();
   }else{
    netDocBal -= curr.getDocAmount();
   }
   
   if(curr.getClass().getSimpleName().equals("DocLineGLRec")){
    DocLineGlRec currGl = (DocLineGlRec)curr;
    curr.setLineType(lnTyGl);
    if(currGl.getVatCodeCompany() != null){
     vatSummary = this.updateVatSummary(vatSummary, currGl);
    }
   }else if ( curr.getClass().getSimpleName().equals("DocLineApRec")){
    curr.setLineType(lnTyAp);
   }
   LOGGER.log(INFO, "Line type {0}", curr.getLineType().getLineCode());
   lnGLIt.set(curr);
  }
  
  lineNum++;
  docLineAp.setLineNum(lineNum);
  docLineAp.setDocAmount(docTotal);
  docLineAp.setHomeAmount(docTotal);
  docLineAp.setComp(postingDoc.getCompany());
  PostTypeRec apPostTy;
  if(netDocBal == 0){
   MessageUtil.addWarnMessage("docZero", "blacValidation");
   return null;
  }else if( netDocBal < 0){
   // need to balance doc
   apPostTy = sysBuff.getPostTypeForCode("apCrn");
  }else{
   apPostTy = sysBuff.getPostTypeForCode("apInv");
  }
  docLineAp.setPostType(apPostTy);
  if(docLineAp.getPayType() == null){
   PaymentTypeRec pt = getPaymentTypes().get(0);
   docLineAp.setPayType(pt);
  }
  // Build reconciliation account to add to apLine
  
  List<DocLineGlRec> recLines = buildInvReconLines(docLineAp, fndbal, postingDoc, lnTyGl);
  LOGGER.log(INFO, "recLines {0}", recLines);
   
  docLineAp.setReconiliationLines(recLines);
    
  
  postingDoc.getDocLines().add(docLineAp);
  for(DocLineBaseRec curr:postingDoc.getDocLines() ){
   LOGGER.log(INFO,"Line type  {0}",curr.getLineType());
   
  }
  
  if(vatSummary.isEmpty()){
   vatSummary = null;
  }
  // build VAT summary
  
  for(FundBalance b: fndbal){
   LOGGER.log(INFO, "fund {0} amount {1}", new Object[]{
    b.getFund(), b.getAmount()
   });
  }
  if(fndbal.isEmpty()){
   fndbal = null;
  }
  
  for(DocLineBaseRec curr: postingDoc.getDocLines()){
   LOGGER.log(INFO, "Line num {0} class {1} post key {2} line type {3}", 
     new Object[]{curr.getLineNum(),curr.getClass().getSimpleName(),curr.getPostType().getPostTypeCode(),
     curr.getLineType().getLineCode()});
  
   if(curr.getClass().getSimpleName().equals("DocLineApRec")){
    LOGGER.log(INFO, "ap rec acnt {0}", ((DocLineApRec)curr).getReconiliationLines());
   }
  }
  
  FiscalPeriodYearRec fisPerYr = this.sysBuff.getCompFiscalPeriodYearForDate(postingDoc.getCompany(), postingDoc.getPostingDate());
  postingDoc.setFisPeriod(fisPerYr.getPeriod());
  postingDoc.setFisYear(fisPerYr.getYear());
  
  LOGGER.log(INFO, "fisc per {0} year {1}", new Object[]{postingDoc.getFisPeriod(), postingDoc.getFisYear()});
 postingDoc = docMgr.postApInvoice(postingDoc, vatSummary,fndbal, getView());
  
  LOGGER.log(INFO, "Invoice id {0}", postingDoc.getId());
  if(postingDoc.getId() != null){
   if(currView.equals("apInvoiceCr")){
    invoice = postingDoc;
  }else{
   creditNote = postingDoc;
  }
   MessageUtil.addInfoMessageVar1("apDocPosted", "formTextAp", String.valueOf(postingDoc.getDocNumber()));
   return "home";
  }else{
   
    MessageUtil.addWarnMessage("docApInv", "errorText");
   
    lnGLIt = postingDoc.getDocLines().listIterator();
    long id = -1;
    while(lnGLIt.hasNext()){
     DocLineBaseRec currBse = lnGLIt.next();
     LOGGER.log(INFO, "", apMgr);
     if(currBse.getClass().getSimpleName().equals("DocLineApRec")){
      lnGLIt.remove();
     }else{
      if(currBse.getId() == null){
      currBse.setId(id);
      lnGLIt.set(currBse);
      }
      id--;
     }
      
    }
    
   
    if(currView.equals("apInvoiceCr")){
     invoice = postingDoc;
     RequestContext.getCurrentInstance().update("apInvCrFrm:lines");
    } else {
     creditNote = postingDoc;
     RequestContext.getCurrentInstance().update("apCrnFrm:lines");
    }
   return null;
  }
  
 }
 
 public void onInvCrnUpload(FileUploadEvent evt){
  UploadedFile file = evt.getFile();
  LOGGER.log(INFO, "File content type {0}", file.getContentType());
  docLineAp.setSupplierDocFileContents(file.getContents());
  docLineAp.setSupplierDocFileName(file.getFileName());
  docLineAp.setSupplierDocFileType(file.getContentType());
  LOGGER.log(INFO,"file name {0} file type {1}", 
    new Object[]{docLineAp.getSupplierDocFileName(), docLineAp.getSupplierDocFileType()});
  MessageUtil.addInfoMessage("apDocUploaded", "formTextAp");
 }
 
 public void onApInvNext(){
  LOGGER.log(INFO, "onApInvNext called");
  if(this.invWizSkip){
   step = 4;
  }else{
   step++;
  }
  this.stepText = stepTexts.get(step);
 }
 
 public void onApInvBack(){
  LOGGER.log(INFO, "onApInvNext called");
  step--;
  stepText = stepTexts.get(step);
 }
 
 public List<DocLineApRec> onApInvOs(String input){
  
  List<DocLineApRec> retList = this.getInvOpenList();
  for(DocLineApRec d:retList){
   LOGGER.log(INFO, "Doc num {0} line num {1}", new Object[]{d.getDocNumber(), d.getLineNum()});
  }
  return retList;
 }
 public List<ApAccountRec> onApAccountsForPtnrComplete(String input){
  
 List<ApAccountRec> retList; // = new ArrayList<>();
 CompanyBasicRec comp = null;
 LOGGER.log(INFO, "currView {0}", currView);
 switch(getViewSimple()){
  case "apInvoiceCr":
   comp = invoice.getCompany();
   break;
  case "apCreditNoteCr":
   comp = creditNote.getCompany();
 }
 
 LOGGER.log(INFO, "set comp {0} from doc", comp);
 /*if(getViewSimple().equals("apInvoiceCr")){
  comp = invoice.getCompany();
  LOGGER.log(INFO, "set comp {0} from invoice", comp);
 }else{
  
 }*/
 
 if(StringUtils.isBlank(input)){
  retList = apMgr.getApAccountsAll(comp);
 }else{
  retList = apMgr.getApAccountsStartinfWithCode(comp, input);
 }
 
 return retList;
 }
 
 public void onApAccountSelect(SelectEvent evt){
  LOGGER.log(INFO, "onApAccountSelect called with object {0}", evt.getObject());
  
  
 }
 
 public List<ArBankAccountRec> onApBankComplete(String input){
  
  
  return null;
 }
 
 public void onApContactSel(SelectEvent evt){
  LOGGER.log(INFO, "opApContactSel with name {0}", ((PartnerPersonRec)evt.getObject()).getFamilyName());
  this.currPersMerge = true;
  
 }
 
 
 public void onApContactNew(){
  LOGGER.log(INFO, "onApContactNew called");
  
  contactApNew.setCreatedBy(this.getLoggedInUser());
  contactApNew.setCreatedDate(new Date());
  currPersList = null;
  currPersList = ptnrMgr.getPersonPartnersByName(contactApNew.getFamilyName());
  LOGGER.log(INFO, "partner list for name {0}", currPersList);
  if(currPersList != null && !currPersList.isEmpty()){
   // check to see if on of these should be used
   LOGGER.log(INFO, "Found partners with name check if should be used");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("newPtnrPersFrm:similarNamesTbl");
   rCtx.execute("PF('ptnrFndWv').show()");
   return;
  }
  onApContactNewPostMerge("ptnrApContCr");
 }
 
 public void onApContactNewMerge(){
  contactApNew = currPersSelected;
  contactApNew.setChangedBy(this.getLoggedInUser());
  contactApNew.setChangedOn(new Date());
  LOGGER.log(INFO, "onApContactNewMerge - contactApNew id {0}", contactApNew.getId());
  //MessageUtil.addClientInfoMessage("apInvCrFrm:okMsg", "ptnrMerged", "blacResponse");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('ptnrFndWv').hide();");
  rCtx.execute("PF('newPtnrDlgWv').hide();");
  onApContactNewPostMerge("ptnrMerged");
  
 }
 
 public void onApContactNewDupl(){
  LOGGER.log(INFO, "onApContactNewDupl called");
  //MessageUtil.addClientInfoMessage("apInvCrFrm:okMsg", "ptnrAddDupl", "blacResponse");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('ptnrFndWv').hide();");
  rCtx.execute("PF('newPtnrDlgWv').hide();");
  //rCtx.update("apInvCrFrm:okMsg");
  onApContactNewPostMerge("ptnrAddDupl");
 }
 
 private void onApContactNewPostMerge(String msgId){
  PartnerRoleRec rl = sysBuff.getPartnerRoleByCode("AP_CONT");
  LOGGER.log(INFO, "rl from sysBuff {0}", rl);
  List<PartnerRoleRec> roles = new ArrayList<>();
  roles.add(rl);
  contactApNew.setPartnerRoles(roles);
  contactApNew = (PartnerPersonRec)ptnrMgr.updatePartner(contactApNew, getView());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(contactApNew.getId() == null){
   MessageUtil.addClientErrorMessage("newPtnrPersFrm:errMsg", "partnerApContCr", "errorText");
   rCtx.update("newPtnrPersFrm:errMsg");
  }else{
   docLineAp.setOrderedBy(contactApNew);
   MessageUtil.addClientInfoMessage("apInvCrFrm:okMsg", msgId, "blacResponse");
   List<String> updateList = new ArrayList<>();
   updateList.add("apInvCrFrm:okMsg");
   updateList.add("apInvCrFrm:orderedBy");
   rCtx.update(updateList);
   rCtx.execute("PF('newPtnrDlgWv').hide();");
  }
 }
 
 public List<CostCentreRec> onCostCentreComplete(String input){
  List<CostCentreRec> retList = null;
  switch(getViewSimple()){
   case "apInvoiceCr":
    retList = sysBuff.getCostCentForComp(invoice.getCompany().getId());
    break;
   case "apCreditNoteCr":
    retList = sysBuff.getCostCentForComp(creditNote.getCompany().getId());
    break;
  }
   
  
  return retList; 
 }
 
 public void onCrnAddLnMnu(){
  docLineGl = new DocLineGlRec();
  UUID uuid = UUID.randomUUID();
  docLineGl.setId(uuid.getLeastSignificantBits());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  
  rCtx.update("addLineFrm");
  rCtx.execute("PF('addDocLnWv').show();");
 }
 
 public void onCrnCtxMnu(SelectEvent evt){
  LOGGER.log(INFO, "onCrnCtxMnu called with {0}", evt.getObject());
  docLineSel = (DocLineBaseRec)evt.getObject();
 }
 public void onCrnDelLnMnu(){
  LOGGER.log(INFO, "onCrnDelLnMnu called");
  if(docLineSel == null){
   MessageUtil.addWarnMessage("fiDocLnSel", "validationText");
  } 
  ListIterator<DocLineBaseRec> linesLi = creditNote.getDocLines().listIterator();
  
  while(linesLi.hasNext() ){
   DocLineBaseRec curr = linesLi.next();
   if(Objects.equals(curr.getId(), docLineSel.getId())){
    linesLi.remove();
   }
  
  if(docLineSel.getPostType().isDebit()){
   docTotal += docLineSel.getDocAmount();
   totalDebit -= docLineSel.getDocAmount();
  }else{
   docTotal -= docLineSel.getDocAmount();
   totalCredit -= docLineSel.getDocAmount();
  }
  List<String> updates = new ArrayList<>();
  //updates.add("apCrnFrm:docAmount");
  updates.add("apCrnFrm:lines");
  RequestContext.getCurrentInstance().update(updates);
  return;
  }
 }
 
 
 public void onDateDocSel(SelectEvent evt){
  
  Date selectedDate = (Date)evt.getObject();
  List<String> updates = new ArrayList<>();
  PaymentTermsRec pt = docLineAp.getPayTerms();
   if(pt == null){
    pt = this.getPaymentTermsList().get(0);
   }
   Date dueDate;
  switch(getViewSimple()){
   case "apInvoiceCr":
    invoice.setDocumentDate(selectedDate);
    if(invoice.getPostingDate() == null){
     invoice.setPostingDate(selectedDate);
     updates.add("apInvCrFrm:postDate");
     FiscalPeriodYearRec fisYrPer = sysBuff.getCompFiscalPeriodYearForDate(invoice.getCompany(), selectedDate);
     invoice.setFisYear(fisYrPer.getYear());
     invoice.setFisPeriod(fisYrPer.getPeriod());
     
     String yrPer = String.valueOf(invoice.getFisYear())+"/"+String.valueOf(invoice.getFisPeriod());
     invoice.setFisYearPeriod(yrPer);
     updates.add("apInvCrFrm:fisYrPer");
    }
    if(invoice.getTaxDate() == null){
     invoice.setTaxDate(selectedDate);
     updates.add("apInvCrFrm:taxDate");
    }
    dueDate = getDueDateFromPayTerm(pt, invoice.getDocumentDate(), invoice.getPostingDate());
    docLineAp.setDueDate(dueDate);
    updates.add("apInvCrFrm:dueDate");
    
    break;
   case "apCreditNoteCr":
     creditNote.setDocumentDate(selectedDate);
     if(creditNote.getPostingDate() == null){
      creditNote.setPostingDate(selectedDate);
      updates.add("apCrnFrm:postDate");
     }
     if(creditNote.getTaxDate() == null){
     creditNote.setTaxDate(selectedDate);
     updates.add("apCrnFrm:taxDate");
    }
    dueDate = getDueDateFromPayTerm(pt, creditNote.getDocumentDate(), creditNote.getPostingDate());
    docLineAp.setDueDate(dueDate);
    updates.add("apInvCrFrm2:dueDate");
  }
  
  if(!updates.isEmpty()){
   RequestContext.getCurrentInstance().update(updates);
  }
 }
 
 public void onDatePostSel(SelectEvent evt){
  
  List<String> updates = new ArrayList<>();
  Date selectedDate = (Date)evt.getObject();
  
  
  
  PaymentTermsRec pt = docLineAp.getPayTerms();
  if(pt == null){
   pt = this.getPaymentTermsList().get(0);
  }
  Date dueDate = null;
  switch(getViewSimple()){
   case "apInvoiceCr":
    if(invoice.getDocumentDate() == null){
     invoice.setDocumentDate(selectedDate);
     updates.add("apInvCrFrm:invDate");
    }
    if(invoice.getTaxDate() == null){
     invoice.setTaxDate(selectedDate);
     updates.add("apInvCrFrm:taxDate");
    }
    dueDate = getDueDateFromPayTerm(pt, invoice.getDocumentDate(), invoice.getPostingDate());
    updates.add("apInvCrFrm:dueDate");
    invoice.setPostingDate(selectedDate);
    updates.add("apInvCrFrm:postDate");
    FiscalPeriodYearRec fisYrPer = sysBuff.getCompFiscalPeriodYearForDate(invoice.getCompany(), selectedDate);
    invoice.setFisYear(fisYrPer.getYear());
    invoice.setFisPeriod(fisYrPer.getPeriod());
    String yrPer = String.valueOf(invoice.getFisYear())+"/"+String.valueOf(invoice.getFisPeriod());
    invoice.setFisYearPeriod(yrPer);
    updates.add("apInvCrFrm:fisYrPer");
    break;
   case "apCreditNoteCr":
    dueDate = getDueDateFromPayTerm(pt, creditNote.getDocumentDate(), creditNote.getPostingDate());
    updates.add("apCrnFrm2:dueDate");
    creditNote.setPostingDate(selectedDate);
    updates.add("apCrnFrm:postDate");
    break;
  }
  
  docLineAp.setDueDate(dueDate);
  
  
  RequestContext.getCurrentInstance().update(updates);
  
 }
 
 public void onDocLineAdd(){
  LOGGER.log(INFO, "onDocLineAdd called");
  docLineGl = new DocLineGlRec();
  
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(getViewSimple().equals("apInvoiceCr")){
   docLineGl.setDocHeaderBase(invoice); 
   rCtx.update("addDocLnFrm");
   rCtx.execute("PF('addDocLnWv').show();");
  }
  
 }
 
 public void onDocLineDel(){
  LOGGER.log(INFO, "onDocLineDel called");
  if(getViewSimple().equals("apInvoiceCr")){
   DocLineBaseRec sel = this.docLineSel;
   
   ListIterator<DocLineBaseRec> docLineI = invoice.getDocLines().listIterator();
   while(docLineI.hasNext()){
    DocLineBaseRec currLn = docLineI.next();
    if(Objects.equals(currLn.getId(), sel.getId())){
     if(currLn.getPostType().isDebit()){
      totalDebit -= currLn.getDocAmount();
     }else{
      totalCredit -= currLn.getDocAmount();
     }
     docLineI.remove();
     List<String> updt = new ArrayList<>();
     lines = invoice.getDocLines().size();
     updt.add("apInvCrFrm:linesCxtMnu");
     updt.add("apInvCrFrm:saveBtn");
     updt.add("apInvCrFrm:lines");
     RequestContext.getCurrentInstance().update(updt);
     return;
    }
    LOGGER.log(INFO, "Check next line" );
   }
  }
 }
 
 public List<FiGlAccountCompRec> onGlaccountComplete(String input){
  
  List<FiGlAccountCompRec> glAcnts;
  if(StringUtils.contains(getViewSimple(), "apInvoiceCr")){
    glAcnts = glAcntMgr.getCompanyAccounts(invoice.getCompany());
   if(glAcnts == null){
    return null;
   }
  }else{
   glAcnts = glAcntMgr.getCompanyAccounts(creditNote.getCompany());
  if(glAcnts == null){
   return null;
  }
  }
  
  
  if(StringUtils.isBlank(input)){
   return glAcnts;
  }
  List<FiGlAccountCompRec> retList = new ArrayList<>();
  for(FiGlAccountCompRec curr:glAcnts){
   if(StringUtils.startsWith(curr.getCoaAccount().getRef(), input)){
    retList.add(curr);
   }
  }
  
  return retList;
 }
 
 public String onInvWizStepChange(FlowEvent evt){
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  
  if(currStep.equals("basicTabId") && docLineAp.getApAccount() == null){
   MessageUtil.addWarnMessage("apDocInvWiz", "validationText");
   nextStep = currStep;
  }else if(StringUtils.equals(currStep, "basicTabId") && invWizSkip){
   nextStep = "linesTabId";
  }
  
    return nextStep;
 }
 
 public void onPaymentTermsSel(SelectEvent evt){
  
  Date dueDate = null;
  switch(getViewSimple()){
   case "apInvoiceCr":
    if(invoice.getDocumentDate() != null && invoice.getPostingDate() != null){
     PaymentTermsRec pt = docLineAp.getPayTerms();
     if(pt == null){
      pt = this.getPaymentTermsList().get(0);
     }
     dueDate = getDueDateFromPayTerm(pt, invoice.getDocumentDate(), invoice.getPostingDate());
    }
    docLineAp.setDueDate(dueDate);
    RequestContext.getCurrentInstance().update("apInvCrFrm2:dueDate");
    break;
   case "apCreditNoteCr":
    if(creditNote.getDocumentDate() != null && creditNote.getPostingDate() != null){
     PaymentTermsRec pt = docLineAp.getPayTerms();
     if(pt == null){
      pt = this.getPaymentTermsList().get(0);
     }
     dueDate = getDueDateFromPayTerm(pt, creditNote.getDocumentDate(), creditNote.getPostingDate());
    }
    docLineAp.setDueDate(dueDate);
    RequestContext.getCurrentInstance().update("apInvCrFrm2:dueDate");
    break;
  }
 }
 
 public List<PostTypeRec> onPostTypeComplete(String input){
  
  List<PostTypeRec> postTypes = sysBuff.getPostCodesForLedger("GL");
  Collections.sort(postTypes, new PostTypeByDrDescr());
  
  if(postTypes == null){
   MessageUtil.addErrorMessage("docLnPostTyNon", "errorText");
   return null;
  }
  ListIterator<PostTypeRec> li = postTypes.listIterator();
  while(li.hasNext()){
   PostTypeRec pt= li.next();
   if(pt.isSysUse()){
    li.remove();
   }
  }
  
  if(StringUtils.isBlank(input)){
   return postTypes;
  }
  List<PostTypeRec> retList = new ArrayList<>();
  for(PostTypeRec curr:postTypes){
   if(StringUtils.contains(curr.getDescription(), input)){
    retList.add(curr);
   }
  }
  return retList;
 }
 
 public List<ProgrammeRec> onProgComplete(String input){
  
  List<ProgrammeRec> progs = null;
  if(StringUtils.equals(getViewSimple(), "apInvoiceCr")){
   progs = progMgr.getAllProgrammes(invoice.getCompany());
  } else if(StringUtils.equals(getViewSimple(), "apCreditNoteCr")) {
   progs = progMgr.getAllProgrammes(creditNote.getCompany());
  }
  
  if(progs == null){
   return null;
  }
  if(StringUtils.isBlank(input)){
   return progs;
  }else{
   List<ProgrammeRec> retList = new ArrayList<>();
   for(ProgrammeRec curr:progs){
    if(StringUtils.contains(curr.getReference(), input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }

public List<FundRec> onFundComplete(String input){
 List<FundRec> fundList = null;
 if(StringUtils.equals(getViewSimple(), "apInvoiceCr")){
  fundList = sysBuff.getRestrictedFunds(invoice.getCompany());
 }else if(StringUtils.equals(getViewSimple(), "apCreditNoteCr")){
  fundList = sysBuff.getRestrictedFunds(creditNote.getCompany());
 }
 
 
 if(fundList == null){
  return null;
 }
 
 if(StringUtils.isBlank(input)){
  return fundList;
 }else{
  List<FundRec> retList = new ArrayList<>();
  for(FundRec curr: fundList){
   if(StringUtils.startsWith(curr.getName(), input)){
    retList.add(curr);
   }
  }
  return retList;
 }
 
 
} 
 
 
 
 
 public void onOrderUpload(FileUploadEvent event) {
  UploadedFile file = event.getFile();
  LOGGER.log(INFO, "File content type {0}", file.getContentType());
  docLineAp.setOrderFileData(file.getContents());
  docLineAp.setOderFileName(file.getFileName());
  docLineAp.setOderFileType(file.getContentType());
  MessageUtil.addInfoMessage("docOrdUploadOk", "formTextSl");
 }
 
 public List<PartnerPersonRec> onPartnerIndivComplete(String input){
  
  PartnerRoleRec rl = sysBuff.getPartnerRoleByCode("AP_CONT");
  LOGGER.log(INFO, "onPartnerIndivComplete role from sysBuff", rl);
  
  List<PartnerPersonRec> ptnrList = ptnrMgr.getPartnersInvForRole(rl);
  return ptnrList;
 }
 
 public void onPartnerNewBtn(){
  contactApNew = new PartnerPersonRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("newPtnrPersFrm");
  rCtx.execute("PF('newPtnrDlgWv').show();");
 }
 
 public void onPartnerNewTrf(){
  
  if(StringUtils.equals(this.getViewSimple(), "apInvoiceCr")){
   docLineAp.setOrderedBy(contactApNew);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.execute("PF('newPtnrDlgWv').hide();");
  }
 }
 
 public List<VatCodeCompanyRec> onVatCodeComplete(String input){
  List<VatCodeCompanyRec> vatCodes = null;
  if(StringUtils.equals(getViewSimple(), "apInvoiceCr")){
  vatCodes = sysBuff.getCompVatCodes(invoice.getCompany());
 }else if(StringUtils.equals(getViewSimple(), "apCreditNoteCr")){
  vatCodes = sysBuff.getCompVatCodes(creditNote.getCompany());
 }
  
  return vatCodes;
 } 
 
 
 
 
}
