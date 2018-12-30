/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.doc.DocVatSummary;
import com.rationem.busRec.doc.SalesPartFiLineRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.VatManager;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.ArManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.helper.CompVatRegistrationRec;
import com.rationem.helper.RestrictFundBalance;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.comparator.ArAccountByRef;
import com.rationem.util.helper.comparator.FiGlAcntCompByRef;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element; 
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.util.ArAcntSrchSelOpt;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;


import java.util.*;
import javax.ejb.EJB;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
//import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author Chris
 */
public class ArDocPostBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(ArDocPostBean.class.getName());
 
 private static final int ALIGN_RIGHT = Element.ALIGN_RIGHT;
 private static final int ALIGN_LEFT = Element.ALIGN_LEFT;
 private static final int ALIGN_MIDDLE = Element.ALIGN_MIDDLE;
 private StreamedContent invoiceMedia;

 @EJB
 private SysBuffer buffer;
 
 @EJB
 private ArManager arManager;
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private MasterDataManager masterDataMgr;
 
  
 
 @EJB
 private CostCentreMgr  costCentMgr;
 
 @EJB
 private ProgrammeMgr progMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private VatManager vatMgr;
 
 @EJB
 private GlAccountManager glAcMgr;

 private String docDateStr;
 private String taxDateStr;
 private String poDateStr;
 private String currView;
 private List<ApAccountRec> acntsAp;
 private List<TransactionTypeRec> transTypes;
 private List<TransactionTypeRec> arSalesDocTransTypes;
  private List<DocTypeRec> documentTypes;
 private List<PaymentTermsRec> paymentTermslist;
 private List<PaymentTypeRec> paymentTypes;
 private List<PostTypeRec> postTypes;
 private List<SalesPartCompanyRec> salesParts;
 private List<ArBankAccountRec> arBanks;
 private DocFiRec fiDocument;
 private DocInvoiceArRec invoice;
 private DocLineBaseRec docLineBase;
 private DocLineGlRec docLineGl;
 private DocLineApRec docLineAp;
 private DocLineArRec docLineAr;
 private DocLineArRec arDocLineSel;
 private DocLineBaseRec arInvTblLineSel;
 private CompanyBasicRec docCompany;
 private BigDecimal docAmount;
 private ArAccountRec custAccount;
 private double crnAmntUnalloc;
 private DualListModel <DocLineArRec> crnInvAllocList;
 private boolean custEntered = false;
 private boolean invPosted = false;
 private boolean canPostInv = false;
 private boolean vatablePeriod ;
 private boolean addLineMinEntry = false;
 private boolean docSelected = false;
 private boolean skip2Lines = false;
 private int slLinesInptMax = 8; 
 private List<ArAccountRec> custAccounts;
 
 private String partyType = "indiv";
 private SalesPartFiLineRec salesInvLine;
 private SalesPartFiLineRec salesInvLineNew;
 private List<SalesPartFiLineRec> salesInvLines;
 private SalesPartFiLineRec selectedSalesInvLine;
 private List<UomRec> uomList;
 private List<FiGlAccountCompRec> salesGlAccounts;
 private List<FundRec> restrictedFunds;
 private List<FiGlAccountCompRec> glAccountsForComp;
 private  Map<Long, List<VatCodeRec> > glVatAcMap;
 private List<VatCodeCompanyRec> vatCodeCompList;
 private List<VatRegistrationRec> vatRegList;
 private VatRegistrationRec vatReg;
 private double arDocAmnt;
 private double arDocAmntTax;
 
 private List<DocVatSummary> docVatSummary;
 private UploadedFile orderFile;
 private List<RestrictFundBalance> restrFundBal;
 private ArAcntSrchSelOpt acntSrchOpt;
 /**
  * Creates a new instance of ArDocPost
  */
 public ArDocPostBean()  {
 }
 
 @PostConstruct
 private void init(){
  currView = this.getViewSimple();
  LOGGER.log(INFO, "Init currview {0}", currView);
  fiDocument = new DocFiRec();
  if(getCompList() == null){
   MessageUtil.addErrorMessage("fiDocCompNon", "validationText");
   return;
  }else{
   Date currDate = new Date();
   docCompany = getCompList().get(0);
   fiDocument.setCompany(docCompany);
   fiDocument.setDocumentDate(currDate);
   fiDocument.setPostingDate(currDate);
   fiDocument.setTaxDate(currDate);
  }
  
  if(currView.equals("arInvPost")|| StringUtils.equals(currView, "arInvPostOld")){
   List<DocTypeRec> dtLst = getDocumentTypes();
   if(dtLst != null && !dtLst.isEmpty()){
    fiDocument.setDocType(dtLst.get(0));
   }
   docLineAr = new DocLineArRec();
   List<PaymentTermsRec> ptLst = this.getPaymentTermslist();
   if(ptLst != null && !ptLst.isEmpty()){
    docLineAr.setPayTerms(ptLst.get(0));
    Date dueDate = getPayTermsDueDate(docLineAr.getPayTerms());
    LOGGER.log(INFO, "init dueDate {0}", dueDate);
    docLineAr.setDueDate(dueDate);
   }
   
  }
   
 }
 
 private DocLineArRec buildDocLineArRec(DocFiRec fiDoc){
  LOGGER.log(INFO, "buildDocLineArRec called");
  
  
  
  docLineAr.setAccountRef(custAccount.getArAccountCode());
  docLineAr.setArAccount(custAccount);
  docLineAr.setClearedLine(false);
  docLineAr.setClearingLine(false);
  docLineAr.setComp(docCompany);
  docLineAr.setCreateBy(getLoggedInUser());
  docLineAr.setCreateDate(new Date());
  docLineAr.setDocAmount(arDocAmnt);
  docLineAr.setDocHeaderBase(fiDoc);
  docLineAr.setInvoice(invoice);
  //docLineAr.setLineNum(1l);
  LineTypeRuleRec arLineTy = this.buffer.getLineTypeRuleByCode("AR");
  docLineAr.setLineType(arLineTy);
  if(StringUtils.equals(getViewSimple(), "arInvPost")  ){
   if(arDocAmnt > 0){
    PostTypeRec arLinePt = buffer.getPostTypeForCode("arInv");
    docLineAr.setPostType(arLinePt);
    docLineAr.setDocAmount(arDocAmnt);
   }else if(StringUtils.equals(getViewSimple(), "arCrnPost")  ){
    PostTypeRec arLinePt = buffer.getPostTypeForCode("arCrn");
    docLineAr.setPostType(arLinePt);
    docLineAr.setDocAmount(arDocAmnt);
   }
  }
  
  return docLineAr;
 }
 
 private Date getPayTermsDueDate(PaymentTermsRec payTerm){
  LOGGER.log(INFO, "getDueDate called with payment term {0} ", 
          new Object[]{ payTerm});
  Date retDate = new Date();
  if(payTerm == null){
   payTerm = getPaymentTermslist().get(0);
  }
  if(payTerm.getBaseType().equalsIgnoreCase("docDT")){
   if(fiDocument == null){
    fiDocument = this.getFiDocument();
   }
   retDate = fiDocument.getDocumentDate();
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

 public DualListModel<DocLineArRec> getCrnInvAllocList() {
  LOGGER.log(INFO, "getCrnInvAllocList called list {0}",crnInvAllocList);
  if(crnInvAllocList == null){
   LOGGER.log(INFO, "arDocLine.getArAccount{0}", docLineAr.getArAccount());
   List<DocLineArRec> availableInvs = arManager.getOpenDocsForArAccount(docLineAr.getArAccount());
   LOGGER.log(INFO, "availableInvs from arManager {0}",availableInvs.size());
   // remove any lines without a doc header
   ListIterator<DocLineArRec> availableInvLi = availableInvs.listIterator();
   while(availableInvLi.hasNext()){
    DocLineArRec line = availableInvLi.next();
    LOGGER.log(INFO, "Doc header {0}", line.getDocHeaderBase());
    if(line.getDocHeaderBase() == null){
     availableInvLi.remove();
    }else{
     line.setPaidAmount(line.getDocAmount());
     availableInvLi.set(line);
    }
   } 
   List<DocLineArRec> allocatedInvs = new ArrayList<>();
   if(availableInvs == null){
    availableInvs = new ArrayList<>();
  }
  
   
   crnInvAllocList = new DualListModel<> (availableInvs,allocatedInvs);
   
  }
  LOGGER.log(INFO, "crnInvAllocList returned avilablbe {0} allocated {1}", new Object[]{
   crnInvAllocList.getSource().size(),crnInvAllocList.getTarget().size()  });
  return crnInvAllocList;
 }

 public void setCrnInvAllocList(DualListModel<DocLineArRec> crnInvAllocList) {
  this.crnInvAllocList = crnInvAllocList;
 }

 public double getCrnAmntUnalloc() {
  return crnAmntUnalloc;
 }

 public void setCrnAmntUnalloc(double crnAmntUnalloc) {
  this.crnAmntUnalloc = crnAmntUnalloc;
 }

 
 

 public CompanyBasicRec getDocCompany() {
  if(docCompany == null){
   LOGGER.log(INFO, "Company setup called ");
   getCompList();
   if(vatReg == null){
    // no VAT registration
    getVatReg();
    vatablePeriod = this.isVatableDoc();
    
   }
   LOGGER.log(INFO, "getDocCompany() VAT status {0}", docCompany.isVatReg());
   
  }
  return docCompany;
 }

 public void setDocCompany(CompanyBasicRec docCompany) {
  this.docCompany = docCompany;
 }

 public String getDocDateStr() {
  if(docDateStr == null && this.fiDocument.getDocumentDate() != null){
   docDateStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(fiDocument.getDocumentDate());
   } 
  return docDateStr;
 }

 public void setDocDateStr(String docDateStr) {
  this.docDateStr = docDateStr;
 }

  
 public BigDecimal getDocAmount() {
  if(docAmount == null){
   docAmount = new BigDecimal(0);
  }
  return docAmount;
 }

 public void setDocAmount(BigDecimal docAmount) {
  this.docAmount = docAmount;
 }

 public double getArDocAmntTax() {
  return arDocAmntTax;
 }

 public void setArDocAmntTax(double arDocAmntTax) {
  this.arDocAmntTax = arDocAmntTax;
 }

 
 public DocLineApRec getDocLineAp() {
  return docLineAp;
 }

 public void setDocLineAp(DocLineApRec docLineAp) {
  this.docLineAp = docLineAp;
 }

 
 public DocLineBaseRec getDocLineBase() {
  return docLineBase;
 }

 public void setDocLineBase(DocLineBaseRec docLineBase) {
  this.docLineBase = docLineBase;
 }

 public DocLineGlRec getDocLineGl() {
  return docLineGl;
 }

 public void setDocLineGl(DocLineGlRec docLineGl) {
  this.docLineGl = docLineGl;
 }
 
 

 public boolean isDocSelected() {
  return docSelected;
 }

 public void setDocSelected(boolean docSelected) {
  this.docSelected = docSelected;
 }

 
 public List<DocTypeRec> getDocumentTypes() {
  if(documentTypes == null){
   documentTypes = buffer.getLedgerDocumentTypes("SL", this.getLoggedInUser());
   LOGGER.log(INFO, "returned by SYS Buffer- SL document types {0}", documentTypes);
  }
  return documentTypes;
 }

 public void setDocumentTypes(List<DocTypeRec> documentTypes) {
  this.documentTypes = documentTypes;
 }

 public List<DocVatSummary> getDocVatSummary() {
  if(docVatSummary == null){
   docVatSummary = new ArrayList<>();
  }
  return docVatSummary;
 }

 public void setDocVatSummary(List<DocVatSummary> docVatSummary) {
  this.docVatSummary = docVatSummary;
 }

 
 public List<PaymentTermsRec> getPaymentTermslist() {
 if(paymentTermslist == null){
   paymentTermslist = buffer.getPaymentTermsList();
   LOGGER.log(INFO, "PaymentTermslist returns list {0}",paymentTermslist);
  }
  
  return paymentTermslist;
 }

 public void setPaymentTermslist(List<PaymentTermsRec> paymentTermslist) {
  this.paymentTermslist = paymentTermslist;
 }

 public List<PaymentTypeRec> getPaymentTypes() {
  if(paymentTypes == null){
   LOGGER.log(INFO, "about to call getPaymentTypes called with comp {0}", docCompany);
   paymentTypes = buffer.getPaymentTypes(docCompany);
  }
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public String getPoDateStr() {
  if(poDateStr == null && this.fiDocument.getDocInvoiceAr().getCreatedOn() != null){
   poDateStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(fiDocument.getDocInvoiceAr().getCreatedOn());
  }
  return poDateStr;
 }

 public void setPoDateStr(String poDateStr) {
  this.poDateStr = poDateStr;
 }

 public List<PostTypeRec> getPostTypes() {
  if(postTypes == null){
   postTypes = buffer.getPostTypes();
  }
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }

 
 public boolean isAddLineMinEntry() {
  return addLineMinEntry;
 }

 public ArAcntSrchSelOpt getAcntSrchOpt() {
  return acntSrchOpt;
 }

 public void setAcntSrchOpt(ArAcntSrchSelOpt acntSrchOpt) {
  this.acntSrchOpt = acntSrchOpt;
 }

 
 public List<ApAccountRec> getAcntsAp() {
  return acntsAp;
 }

 public void setAcntsAp(List<ApAccountRec> acntsAp) {
  this.acntsAp = acntsAp;
 }

 
 public void setAddLineMinEntry(boolean addLineMinEntry) {
  this.addLineMinEntry = addLineMinEntry;
 }

 
 
 public List<ArBankAccountRec> getArBanks() {
  if(arBanks == null){
   LOGGER.log(INFO, "ArBanks is null");
   arBanks = docLineAr.getArAccount().getArAccountBanks();
   LOGGER.log(INFO, "banks from customer a/c {0}", arBanks);
  }
  return arBanks;
 }

 public void setArBanks(List<ArBankAccountRec> arBanks) {
  this.arBanks = arBanks;
 }

 public boolean isCanPostInv() {
  if(invPosted){
   canPostInv = false;
  }
  if(salesInvLines != null && !salesInvLines.isEmpty() ){
   canPostInv = true;
 }
  return canPostInv;
 }

 public void setCanPostInv(boolean canPostInv) {
  this.canPostInv = canPostInv;
 }

 
 public List<TransactionTypeRec> getArSalesDocTransTypes() {
  if(arSalesDocTransTypes == null){
     arSalesDocTransTypes = new ArrayList<>();
   if(transTypes == null){
    transTypes = getTransTypes();
   }
   ListIterator<TransactionTypeRec> li = transTypes.listIterator();
   while(li.hasNext()){
    TransactionTypeRec transType = li.next();
    if(transType.getCode().equalsIgnoreCase("arInv") || transType.getCode().equalsIgnoreCase("arCrn") ){
     arSalesDocTransTypes.add(transType);
    }
   }
   
  }
  return arSalesDocTransTypes;
 }

 public void setArSalesDocTransTypes(List<TransactionTypeRec> arSalesDocTransTypes) {
  this.arSalesDocTransTypes = arSalesDocTransTypes;
 }

 public String getTaxDateStr() {
  if(taxDateStr == null && this.fiDocument.getTaxDate() != null){
   taxDateStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(fiDocument.getTaxDate());
  }
  return taxDateStr;
 }

 public void setTaxDateStr(String taxDateStr) {
  this.taxDateStr = taxDateStr;
 }

 
 public List<TransactionTypeRec> getTransTypes() {
  if(transTypes == null){
   transTypes = buffer.getArTransactionTypes(getLoggedInUser(),this.getView());
   LOGGER.log(INFO,"getTransTypes after get from sysBuffer {0}",transTypes);
  }
  return transTypes;
 }

 public void setTransTypes(List<TransactionTypeRec> transTypes) {
  this.transTypes = transTypes;
 }

 public DocFiRec getFiDocument() {
  if(fiDocument == null){
   LOGGER.log(INFO, "getFiDocument called");
   fiDocument = new DocFiRec();
   Date curr = new Date();
   fiDocument.setDocumentDate(curr);
   fiDocument.setPostingDate(curr);
   fiDocument.setTaxDate(curr);
   FiscalPeriodYearRec perYr = buffer.getCompFiscalPeriodYearForDate(docCompany, curr);
   fiDocument.setFisPeriod(perYr.getPeriod());
   fiDocument.setFisYear(perYr.getYear());
   if(paymentTermslist == null){
    this.getPaymentTermslist();
   }
   if(docLineAr == null){
    //no arDocLine need to create one
    docLineAr = new DocLineArRec();
    LOGGER.log(INFO, "build new arDocLine payterms {0}", paymentTermslist);
    PaymentTermsRec defPayTerms = paymentTermslist.get(0);
    LOGGER.log(INFO,"new arDocLine defPayTerms {0} ",defPayTerms);
    Date dDate = this.getPayTermsDueDate(defPayTerms);
    docLineAr.setDueDate(dDate);
   }
   //vatablePeriod = this.isVatableDoc();
       
  }
  return fiDocument;
 }

 public void setFiDocument(DocFiRec fiDocument) {
  this.fiDocument = fiDocument;
 }

 public List<FiGlAccountCompRec> getGlAccountsForComp() {
  return glAccountsForComp;
 }

 public void setGlAccountsForComp(List<FiGlAccountCompRec> glAccountsForComp) {
  this.glAccountsForComp = glAccountsForComp;
 }

 
 public Map<Long, List<VatCodeRec> > getGlVatAcMap() {
  return glVatAcMap;
 }

 public void setGlVatAcMap(Map<Long, List<VatCodeRec>> glVatAcMap) {
  this.glVatAcMap = glVatAcMap;
 }

 
 public DocInvoiceArRec getInvoice() {
  if(invoice == null){
   invoice = new DocInvoiceArRec(); 
   invoice.setTotalAmount(0.00);
   invoice.setGoodsAmount(0.00);
   invoice.setVatAmount(0.00);
   
  }
  return invoice;
 }

 public void setInvoice(DocInvoiceArRec invoice) {
  this.invoice = invoice;
 }

 public boolean isInvPosted() {
  return invPosted;
 }

 public void setInvPosted(boolean invPosted) {
  this.invPosted = invPosted;
 }

 public UploadedFile getOrderFile() {
    return orderFile;
  
 }

 public void setOrderFile(UploadedFile orderFile) {
  this.orderFile = orderFile;
 }

 public double getArDocAmnt() {
  return arDocAmnt;
 }

 public void setArDocAmnt(double arDocAmnt) {
  //this.arDocAmnt = arDocAmnt;
 }
 
 

 public DocLineArRec getDocLineAr() {
  if(docLineAr == null){
   docLineAr = new DocLineArRec();
   docLineAr.setComp(docCompany);
   LOGGER.log(INFO, "getArDocLine paymentTermslist {0} ", paymentTermslist);
   if(paymentTermslist == null || paymentTermslist.isEmpty() ){
    paymentTermslist = buffer.getPaymentTermsList();
    LOGGER.log(INFO, "paymentTermslist after call to buffer {0} ", paymentTermslist);
   }
   PaymentTermsRec defPayTerms = paymentTermslist.get(0);
   if(defPayTerms.getBaseType().equalsIgnoreCase("docDT")){
    docLineAr.setDueDate(getPayTermsDueDate(defPayTerms));
   }
   
   
  
  }
  return docLineAr;
 }

 public void setDocLineAr(DocLineArRec arDocLine) {
  this.docLineAr = arDocLine;
 }

 public DocLineArRec getArDocLineSel() {
  return arDocLineSel;
 }

 public void setArDocLineSel(DocLineArRec arDocLineSel) {
  this.arDocLineSel = arDocLineSel;
 }

 
 public ArAccountRec getCustAccount() {
  if(custAccount == null){
   custAccount = new ArAccountRec();
   
  }
  return custAccount;
 }

 public void setCustAccount(ArAccountRec custAccount) {
  this.custAccount = custAccount;
 }

 public boolean isCustEntered() {
  return custEntered;
 }

 public void setCustEntered(boolean custEntered) {
  this.custEntered = custEntered;
 }

 
 public List<ArAccountRec> getCustAccounts() {
  return custAccounts;
 }

 public void setCustAccounts(List<ArAccountRec> custAccounts) {
  this.custAccounts = custAccounts;
 }

 public String getPartyType() {
  return partyType;
 }

 public void setPartyType(String partyType) {
  this.partyType = partyType;
 }

 public List<SalesPartFiLineRec> getSalesInvLines() {
  if(salesInvLines == null){
   salesInvLines = new ArrayList<>();
  }
  return salesInvLines;
 }

 public void setSalesInvLines(List<SalesPartFiLineRec> salesInvLines) {
  this.salesInvLines = salesInvLines;
 }

 public List<FundRec> getRestrictedFunds() {
  if(restrictedFunds == null){
   CompanyBasicRec comp = this.getDocCompany();
   restrictedFunds = comp.getFundList();
   
   LOGGER.log(INFO, "ArDocPost - Restricted funds from company {0}", restrictedFunds);
  }
  return restrictedFunds;
 }

 public void setRestrictedFunds(List<FundRec> restrictedFunds) {
  this.restrictedFunds = restrictedFunds;
 }

 public List<RestrictFundBalance> getRestrFundBal() {
  if(restrFundBal == null){
   restrFundBal = new ArrayList<>();
  }
  return restrFundBal;
 }

 public void setRestrFundBal(List<RestrictFundBalance> restrFundBal) {
  this.restrFundBal = restrFundBal;
 }

 
 public List<SalesPartCompanyRec> getSalesParts() {
  LOGGER.log(INFO, "getSalesParts called parts {0}", salesParts);
  if(salesParts == null){
   LOGGER.log(INFO, "docCompany is {0}", docCompany);
   if(docCompany == null){
    docCompany = getCompList().get(0);
   }
   salesParts = buffer.getCompanySalesPartsForComp(docCompany);
   //salesInvLine.setDescription(salesParts.get(0).getPart().getExternalDescription());
   LOGGER.log(INFO, "After call to buffer parts {0}", salesParts);
   //salesInvLine.setDescription("ext descr");
  }
  if(salesParts == null){
   salesParts = new ArrayList<>();
  }
  LOGGER.log(INFO, "returns parts {0}", salesParts);
  return salesParts;
 }

 public void setSalesParts(List<SalesPartCompanyRec> salesParts) {
  this.salesParts = salesParts;
 }

 public List<FiGlAccountCompRec> getSalesGlAccounts() {
  if(salesGlAccounts == null || salesGlAccounts.isEmpty() ){
   salesGlAccounts = new ArrayList<>();
   List<FiGlAccountCompRec> compAcs = buffer.getGlAccountsByCompanyCode(docCompany);
   LOGGER.log(INFO, "compAcs {0}", compAcs);
   ListIterator<FiGlAccountCompRec> li = compAcs.listIterator();
   while(li.hasNext()){
    FiGlAccountCompRec compAc = li.next();
    LOGGER.log(INFO, "GL account number {0} account type {1}", 
            new Object[]{compAc.getCoaAccount().getRef(),compAc.getCoaAccount().getAccountType().getName()});
    if(compAc.getCoaAccount().getAccountType().getName().startsWith("Inc")){
     salesGlAccounts.add(compAc);
    }
   }
  }
  return salesGlAccounts;
 }

 public void setSalesGlAccounts(List<FiGlAccountCompRec> salesGlAccounts) {
  this.salesGlAccounts = salesGlAccounts;
 }

 public SalesPartFiLineRec getSelectedSalesInvLine() {
  
  /*
  if(selectedSalesInvLine == null){
   selectedSalesInvLine = new SalesPartFiLineRec();
   if(selectedSalesInvLine.getPartComp() == null){
    SalesPartCompanyRec salesPart = new SalesPartCompanyRec();
    salesPart.setId(10l);
    selectedSalesInvLine.setPartComp(salesPart);

    
   }
  }*/
  //LOGGER.log(INFO,"getSelectedSalesInvLine partComp id on return {0}",selectedSalesInvLine.getPartComp().getId());
  return selectedSalesInvLine;
 }

 public void setSelectedSalesInvLine(SalesPartFiLineRec selectedSalesInvLine) {
  LOGGER.log(INFO,"setSelectedSalesInvLine set to {0}",selectedSalesInvLine);
  if(selectedSalesInvLine == null){
   return;
  }
  this.selectedSalesInvLine = selectedSalesInvLine;
  selectedSalesInvLine.setPartCode(selectedSalesInvLine.getPartComp().getPart().getPartCode());
  LOGGER.log(INFO,"setSelectedSalesInvLine.partcode is {0}",selectedSalesInvLine.getPartCode());
 }

 public boolean isSkip2Lines() {
  return skip2Lines;
 }

 public void setSkip2Lines(boolean skip2Lines) {
  this.skip2Lines = skip2Lines;
 }

 
 public int getSlLinesInptMax() {
  return slLinesInptMax;
 }

 public void setSlLinesInptMax(int slLinesInptMax) {
  this.slLinesInptMax = slLinesInptMax;
 }

 
 public List<UomRec> getUomList() {
  if(uomList == null){
   // get from system buffer
   uomList = buffer.getUoms();
   LOGGER.log(INFO, "UOMs loaded from system buffer {0}", uomList);
  }
  
  return uomList;
 }

 public void setUomList(List<UomRec> uomList) {
  this.uomList = uomList;
 }

 public boolean isVatablePeriod() {
  
  return vatablePeriod;
 }

 public void setVatablePeriod(boolean vatablePeriod) {
  this.vatablePeriod = vatablePeriod;
 }

 

 public List<VatCodeCompanyRec> getVatCodeCompList() {
  if(vatCodeCompList == null){
   vatCodeCompList = buffer.getCompVatCodes(docCompany);
  }
  LOGGER.log(INFO, "vatCodeCompList is {0}", vatCodeCompList);
  return vatCodeCompList;
 }

 public void setVatCodeCompList(List<VatCodeCompanyRec> vatCodeCompList) {
  this.vatCodeCompList = vatCodeCompList;
 }

 public List<VatCodeRec> vatCodeComplete(String input){
  LOGGER.log(INFO, "vatCodeComplete called with {0}", input);
  List<VatCodeRec> retList = new ArrayList<>();
  FiGlAccountCompRec glAc = salesInvLine.getPartComp().getSalesAccount();
  if(glAc == null){
   // sales ac not set
   LOGGER.log(INFO,"No sales gl account");
   return retList;
  }
  
  return retList;
 }
 public VatRegistrationRec getVatReg() {
  if(vatReg == null){
   vatReg = buffer.getVatRegForCompany(docCompany);
   //set vat scheme settings for each vat scheme registration
   if(vatReg == null){
    return null;
   }
  List<VatRegSchemeRec> regSchemes = vatReg.getRegSchemes();
  ListIterator<VatRegSchemeRec>regSchemesLi = regSchemes.listIterator();
  while(regSchemesLi.hasNext()){
   VatRegSchemeRec regScheme = regSchemesLi.next();
    VatSchemeRec scheme = regScheme.getVatScheme();
    
    regScheme.setVatScheme(scheme);
    regSchemesLi.set(regScheme);
  }
  vatReg.setRegSchemes(regSchemes);
  
  }
  return vatReg;
 }

 public void setVatReg(VatRegistrationRec vatReg) {
  this.vatReg = vatReg;
 }

 public List<DocVatSummary> setVatSum(SalesPartFiLineRec line, Date taxPoint, CompanyBasicRec comp){
  LOGGER.log(INFO,"setVatSum called with: line {0} taxdate {1} comp {2}", new Object[]{line,taxPoint,comp});
  
  double goodsAmount = line.getLineTotal();
  
  DocVatSummary vatSum = null;
  
  VatCodeCompanyRec compVat = null ;
  ListIterator<VatCodeCompanyRec> compVatLi = this.getVatCodeCompList().listIterator();
  Boolean compVatFound = false;
   while(compVatLi.hasNext() && !compVatFound){
    VatCodeCompanyRec compVatRec = compVatLi.next();
    LOGGER.log(INFO, "compVatRec code {0}", compVatRec.getVatCode().getCode());
    if(compVatRec == line.getVatCode()){
     compVat = compVatRec;
     compVatFound = true;
    }
   }
  if(!compVatFound ){
   GenUtil.addErrorMessage("Could not find company VAT code");
   return null;
  }
  
  
  VatRegistrationRec vatReg = null;
  if(docVatSummary == null){
   LOGGER.log(INFO, "no docVatSummary");
   vatSum  = new DocVatSummary();
   docVatSummary = new ArrayList<>(); 
   LOGGER.log(INFO, "Before find compvat compVat {0}", compVat);
 
  }
  if(docVatSummary != null)  {
   //need to find the VAT summary record
   //do we have this gl account
   ListIterator<DocVatSummary> docVatSumLi = docVatSummary.listIterator();
   boolean vatSumFound = false;
   while(docVatSumLi.hasNext() && !vatSumFound){
    DocVatSummary vatSumRec = docVatSumLi.next();
    if(vatSumRec.getVatCode() == line.getVatCode()){
     LOGGER.log(INFO, "Found VAT code {0}", line.getVatCode().getVatCode().getCode());
     if(vatSumRec.getGlAccount() == line.getGlaccount()){
      // we have found Gl account
      LOGGER.log(INFO, "Found GL account", line.getGlaccount().getCoaAccount().getRef());
      if(line.getFund() != null && vatSumRec.getFund() != null){
       if(vatSumRec.getFund() == line.getFund()){
        vatSum = vatSumRec;
        vatSumFound = true;
      }
      }else if(line.getFund() == null && vatSumRec.getFund() == null){
       vatSum = vatSumRec;
       vatSumFound = true;
      }
     
     }
    }
    
   }
   if(!vatSumFound){
    vatSum = new DocVatSummary();
    VatCodeCompanyRec lnVatCode = line.getVatCode();
    vatSum.setVatCode(lnVatCode);
    if(vatReg == null){
     // no vat registration
     boolean vatRegFound = false;
     List<VatRegistrationRec> vatRegList = this.vatMgr.getVatRegistrationsForComp(comp);
     ListIterator<VatRegistrationRec> vatRegListIt = vatRegList.listIterator();
     while(vatRegListIt.hasNext() && !vatRegFound){
      VatRegistrationRec vatRegRec = vatRegListIt.next();
      if(vatRegRec.getRegistrationDate().before(taxPoint) &&vatRegRec.getRegistrationEnd().after(taxPoint)){
       vatRegFound = true;
       List<VatRegSchemeRec> vatRegSchemes = vatRegRec.getRegSchemes();
       boolean vatRegSchemeFound = false;
       // Find active a vatReg scheme.
       ListIterator<VatRegSchemeRec> vatRegSchemesLi = vatRegSchemes.listIterator();
       while(vatRegSchemesLi.hasNext() && !vatRegSchemeFound ){
        VatRegSchemeRec vatRegSchemeRec = vatRegSchemesLi.next();
        if(vatRegSchemeRec.getValidFrom().before(taxPoint) && vatRegSchemeRec.getValidTo().after(taxPoint)){
         vatRegSchemeFound = true;
         vatSum.setVatRegScheme(vatRegSchemeRec);
         }
       }
       
      }
     }
     
    }
    
    
    vatSum.setGlAccount(line.getGlaccount());
    if(compVat == null){
   // need to set comp Vat
  }
    LOGGER.log(INFO, "compVat is {0}", compVat);
    if(compVat.getChargeGlAccount() != null){
     vatSum.setIrrecoverableAccount(compVat.getChargeGlAccount());
    }
    if(compVat.getAccrualGlAccount() != null){
     vatSum.setProvisionAccount(compVat.getAccrualGlAccount());
    }
    if(compVat.getRateGlAccount() != null){
     vatSum.setRateAccount(compVat.getRateGlAccount());
    }
    
    if(line.getFund() != null){
     vatSum.setFund(line.getFund());
    }
    vatSum.setGoods(0.0);
    //Set accounts for this Vat summary
    
    docVatSummary.add(vatSum);
   }
   // end find vatSum rec
   vatSum.setGoods(vatSum.getGoods() + goodsAmount );
   double vatRate = line.getVatCode().getVatCode().getRate();
   double vatAmount = vatSum.getGoods() * vatRate;
   vatAmount = GenUtil.roundUp2Dp(vatAmount);
   vatSum.setVat(vatAmount);
   
   
  }
  
  LOGGER.log(INFO, "setVatSum returns {0}", docVatSummary); 
  return docVatSummary;
 }
 

 

 
 
 public SalesPartFiLineRec getSalesInvLine() {
  if(salesInvLine == null){
   salesInvLine = new SalesPartFiLineRec();
   
  }
  return salesInvLine;
 }

 public void setSalesInvLine(SalesPartFiLineRec salesInvLine) {
  this.salesInvLine = salesInvLine;
 }

 public SalesPartFiLineRec getSalesInvLineNew() {
  
  return salesInvLineNew;
 }

 public void setSalesInvLineNew(SalesPartFiLineRec salesInvLineNew) {
  this.salesInvLineNew = salesInvLineNew;
 }
 
 
 public void onPartChangeListener(ValueChangeEvent evt){
  LOGGER.log(INFO, "onPartChangeListener called with value {0}", evt.getNewValue());
 }
 
 public void onPartyTypeChangeListener(ValueChangeEvent evt){
  LOGGER.log(INFO, "onPartyTypeChangeListener called with value {0}", evt.getNewValue());
  partyType = (String)evt.getNewValue();
  LOGGER.log(INFO, "partyType is now {0}",partyType); 
  PrimeFaces.current().ajax().update("selAddrFrm:partyName");
 }
 
 public void onArAccountSelect(SelectEvent evt){
  LOGGER.log(INFO, "onArAccountSelect called with value {0} ", 
          evt.getObject());
  //String currView = this.getViewSimple();
  LOGGER.log(INFO, "currView {0}",currView);
  custAccount = (ArAccountRec)evt.getObject();
  if(docLineAr == null){
   docLineAr = new DocLineArRec();
  }
  docLineAr.setArAccount(custAccount);
  LOGGER.log(INFO, "ar account {0}",  docLineAr.getArAccount().getArAccountCode());
  custEntered = true;
  
  invoice.setNotes(null);
  
  fiDocument.setTaxDate(fiDocument.getDocumentDate());
  
  PaymentTermsRec payTerm = docLineAr.getArAccount().getPaymentTerms();
  if(payTerm == null){
   payTerm = this.getPaymentTermslist().get(0);
  }
  if(payTerm != null){
   docLineAr.setPayTerms(payTerm);
   if(docLineAr.getDueDate() == null){
   if(payTerm.getBaseType().equalsIgnoreCase("docDT")){
    docLineAr.setDueDate(getPayTermsDueDate(payTerm));
   }
   }
  }
  //RequestContext rCtx = RequestContext.getCurrentInstance();
  PrimeFaces pf = PrimeFaces.current();
  List<String> update = new ArrayList<>();
  update.add("custNum");
  update.add("custpanel");
  update.add("custAddrPanel");
  update.add("mainGrid");
  if(currView.equals("arInvPost")){
   update.add("arDocWiz");
  }else{
   update.add("arDocTabV");
  }
  pf.ajax().update(update);
  //rCtx.update(update);
  
 }

public void onArInvStepBack(){
 LOGGER.log(INFO, "onArInvStepBack called step is {0}", getStep());
 int currStep = getStep() - 1;
 if(currStep < 0){
  currStep = 0;
 }
 setStep(currStep);
 PrimeFaces.current().ajax().update("arDocCrDetFrm");
 
}
 public void onArInvStepNext(){
 LOGGER.log(INFO, "onArInvStepNext called step is {0}", getStep());
 int curr = getStep();
 if(curr == 0 && skip2Lines){
  setStep(4);
 }else{
  curr++;
  setStep(curr);
 }
 PrimeFaces.current().ajax().update("arDocCrDetFrm");
}
 
public void onArInvAcSrchBtn(){
 custAccounts = arManager.getAccountsBySelOpt(acntSrchOpt);
 LOGGER.log(INFO, "arManger return accounts {0}", custAccounts);
 
 PrimeFaces pf = PrimeFaces.current();
 pf.ajax().update("selAcntFrm:acntList");
 /*if(custAccounts != null && !custAccounts.isEmpty()){
  pf.executeScript("PF('custAcSrchDlgWv').hide()");
 }
 */
}
public void onArInvAcSrchDlgBtn(){
 LOGGER.log(INFO, "onArInvAcSrchDlgBtn called");
 
 acntSrchOpt = new ArAcntSrchSelOpt();
 
 custAccounts = arManager.getAccountsBySelOpt(acntSrchOpt);
 PrimeFaces pf = PrimeFaces.current();
 pf.ajax().update("selAcntFrm:acntList");
 pf.executeScript("PF('custAcSrchDlgWv').show()");
}

public void onArInvAcntRowSelect(SelectEvent evt){
 LOGGER.log(INFO, "onArInvAcntRowSelect called with {0}", evt.getObject());
 custAccount = (ArAccountRec)evt.getObject();
 docCompany = custAccount.getCompany();
 List<String> updates = new ArrayList<>();
 updates.add("arDocCrSelFrm");
 updates.add("arDocCrDetFrm");
 updates.add("displAcntAddr");
 PrimeFaces.current().ajax().update(updates);
 
 
 
}

public void onArInvLineAdd(){
 LOGGER.log(INFO, "onArInvLineAdd called");
  this.docLineAr = new DocLineArRec();
  this.docLineBase = new DocLineBaseRec();
  this.docLineGl = new DocLineGlRec();
  //RequestContext rCtx = RequestContext.getCurrentInstance();
  LOGGER.log(INFO, "Open new page");
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addDocLnFrm");
  pf.executeScript("PF('addDocLnWv').show();");
 }

public void onArInvPost(){
 LOGGER.log(INFO, "onArInvPost");
 fiDocument.setDocInvoiceAr(invoice);
 UserRec crUsr = getLoggedInUser();
 Date currDate = new Date();
 fiDocument.setCreateOn(currDate);
 fiDocument.setCreatedBy(crUsr);
 fiDocument.setCompany(docCompany);
 FiscalPeriodYearRec perYr = this.buffer.getCompFiscalPeriodYearForDate(fiDocument.getCompany(), fiDocument.getDocumentDate());
 fiDocument.setFisPeriod(perYr.getPeriod());
 fiDocument.setFisYear(perYr.getYear());
 
 boolean lnVat = false;
 long lineNum = 0;
 List<DocLineBaseRec> docLines = fiDocument.getDocLines();
 ListIterator<DocLineBaseRec> linesLi = docLines.listIterator();
 while(linesLi.hasNext()){
  DocLineBaseRec curr = linesLi.next();
  if(curr.getId() < 1){
   curr.setId(null);
  }
  lineNum++;
  curr.setLineNum(lineNum);
  curr.setCreateBy(crUsr);
  curr.setCreateDate(currDate);
  if(!lnVat){
   lnVat = ((DocLineGlRec)curr).getGlAccount().getVatCode() != null;
  }
  LOGGER.log(INFO, "GL account {0} vatable lines {1}", new Object[]{
   ((DocLineGlRec)curr).getGlAccount().getCoaAccount().getRef(), lnVat});
  curr.setDocHeaderBase(fiDocument);
  linesLi.set(curr);
 }
 DocLineArRec arLn = buildDocLineArRec(fiDocument);
 lineNum++;
 arLn.setLineNum(lineNum);
 arLn.setId(null);
 docLines.add(arLn);
 fiDocument.setDocLines(docLines);
 fiDocument.setPartnerRef(this.custAccount.getArAccountCode());
 TransactionTypeRec transTy = this.buffer.getTransactionTypeRecByCode("arInv");
 fiDocument.setTransactionType(transTy);
 boolean vatable = docCompany.isVatReg() && lnVat;
 fiDocument.setVatable(vatable);
 LOGGER.log(INFO,"Number of line items {0}",docLines.size());
 double balChk = 0;
 for (DocLineBaseRec curr: docLines){
  LOGGER.log(INFO, "Current Line {0} post Type {1} debit {2} amount {3}",
    new Object[]{curr.getLineNum(), curr.getPostType().getPostTypeCode(), curr.getPostType().isDebit(),
     curr.getDocAmount()});
  if(curr.getPostType().isDebit()){
   balChk += curr.getDocAmount();
  }else{
   balChk -= curr.getDocAmount();
  }
 }
 if(balChk != 0){
  MessageUtil.addClientWarnMessage("arDocCrSelFrm:msg", "fiDocBalanced", "validationText");
  PrimeFaces.current().ajax().update("arDocCrSelFrm:msg");
  return;
 }
 LOGGER.log(INFO, "About to call save ", perYr);
 fiDocument = this.docMgr.updateDocument(fiDocument, this.getLoggedInUser(), getView());
 if(fiDocument.getId() == null){
  MessageUtil.addClientWarnMessage("arDocCrSelFrm:msg", "docArInv", "errorText");
  PrimeFaces.current().ajax().update("arDocCrSelFrm:msg");
 }else{
  MessageUtil.addClientInfoMessage("arDocCrSelFrm:grl", "arInvPosted", "blacResponse", String.valueOf(fiDocument.getDocNumber()));
  fiDocument = null;
  docCompany = getCompList().get(0);
  arDocAmnt = 0.0;
  custAccount = null;
  arDocAmntTax = 0.0;
  setStep(0);
  docLineAr = null;
  invoice = null;
  PrimeFaces.current().ajax().update("arDocCrSelFrm");
  
  
 }

 
 
}
public List<PostTypeRec> onArInvPostKeyComplete(String input){
  LOGGER.log(INFO, "onPostKeyArInvComplete call with input {0}", input);
  List<PostTypeRec> retList = new ArrayList<>();
  if(input == null || input.isEmpty()){
   for(PostTypeRec pt:getPostTypes()){
    if(StringUtils.equals(pt.getLedger().getCode(), "GL") && !pt.isSysUse() ){
     retList.add(pt);
    }
   }
  }else{
   for(PostTypeRec pt:getPostTypes()){
    if((pt.getLedger().getCode().equals("GL") || pt.getLedger().getCode().equals("AR")) &&
      pt.getDescription().startsWith(input) && !pt.isSysUse()){
     retList.add(pt);
    }
   }
   if(retList.isEmpty()){
    MessageUtil.addWarnMessageParam("fiDocPostTypeNone", "validationText", input);
   }
  }
  return retList;
 }

public void onArInvTabChange(TabChangeEvent evt){
 Tab currTab = evt.getTab();
 LOGGER.log(INFO, "Curr tab id {0}", currTab.getId());
 if(currTab.getId().equals("basicTabId")){
  return;
 }
 if(custAccount.getArAccountCode() == null || custAccount.getArAccountCode().isEmpty()){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('docTabViewWv').select('0')");
  MessageUtil.addWarnMessage("arDocArAcnt", "validationText");
  rCtx.update("tabChngmsg");
  
 }
}

public List<VatCodeCompanyRec> onArInvVatCodeCompComplete(String input){
 LOGGER.log(INFO, "onArInvVatCodeCompComplete called with {0}", input);
 if(StringUtils.isBlank(input)){
  return buffer.getCompVatCodes(docCompany);
 } else{
  List<VatCodeCompanyRec> vatCodes = new ArrayList<>();
  for(VatCodeCompanyRec curr: buffer.getCompVatCodes(docCompany)  ){
   if(StringUtils.startsWith(curr.getVatCode().getCode(), input)){
    vatCodes.add(curr);
   }
  }
  return vatCodes;
 }
 
}

 public DocLineBaseRec getArInvTblLineSel() {
  return arInvTblLineSel;
 }

 public void setArInvTblLineSel(DocLineBaseRec arInvTblLineSel) {
  this.arInvTblLineSel = arInvTblLineSel;
 }


public void onCreateOrderedByPersonBtnListener(){
 LOGGER.log(INFO, "onCreateOrderedByPerson called with person", invoice.getOrderedBy());
}

public void onCloseAddLineBtn(){
  salesInvLine = new SalesPartFiLineRec();
  LOGGER.log(INFO,"onCloseAddLineBtn called salesInvLine is now {0}",salesInvLine);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addLineFrm");
  rCtx.execute("PF('addLineDlgWv').hide();");
  
 }

public void onCompanySel(SelectEvent evt){
 LOGGER.log(INFO, "onCompanySel called with {0}", evt.getObject());
 this.docCompany = (CompanyBasicRec)evt.getObject();
 
} 
 public List<CostCentreRec> onCostCentComplete(String input){
  List<CostCentreRec> costCents; 
  /*CostCentreRec selCostCent = new CostCentreRec();
  selCostCent.setId(Long.valueOf("0"));
  String entry = this.validationForKey("maCostCentSel");
  selCostCent.setRefrence(entry);
  costCents.add(selCostCent); */
  if(input == null || input.isEmpty()){
   CompanyBasicRec ccComp = this.getDocCompany();
   costCents = costCentMgr.getCostCentresForCompany(ccComp);
  }else{
   CompanyBasicRec ccComp = this.getDocCompany();
   costCents = costCentMgr.getCostCentresByRef(ccComp, input);
  }
  return costCents;
 }
 
 
 
 
 
 public void onDocNew(){
  LOGGER.log(INFO, "onDocNew");
  this.custAccount = null;
  docLineAr = null;
  this.docVatSummary = null;
  this.fiDocument = null;
  this.invPosted = false;
  this.salesInvLines = null;
  this.custEntered = false;
  
 }
 
 public void onDocInvLineCtxMnu(SelectEvent evt){
  LOGGER.log(INFO, "onDocInvLineCtxMnu Context menu select for line {0}", selectedSalesInvLine);
  LOGGER.log(INFO, "evt {0}", evt.getObject());
  selectedSalesInvLine = (SalesPartFiLineRec) evt.getObject();
 }
 
 public void onInvLineSel(){
  LOGGER.log(INFO, "onInvLineSel called select line {0}", selectedSalesInvLine);
  LOGGER.log(INFO, "selected line num {0}", selectedSalesInvLine.getLineNum());
  LOGGER.log(INFO, "Inv part {0}", selectedSalesInvLine.getPartComp().getPart().getPartCode());
  long id = 100;
  selectedSalesInvLine.getPartComp().setId(id);
  selectedSalesInvLine.setPartCode(selectedSalesInvLine.getPartComp().getPart().getPartCode());
  LOGGER.log(INFO, "Inv part code {0}", selectedSalesInvLine.getPartCode());
  selectedSalesInvLine.setPart(selectedSalesInvLine.getPartComp().getPart());
  LOGGER.log(INFO, "Inv part {0}",selectedSalesInvLine.getPart().getPartCode());
  
  
 }
 
 /**
  * use credit note as payment for invoices 
  * @param evt Invoices credit is to be used against
  */
 public void onCrnInvAllocTrf(TransferEvent evt) {
  LOGGER.log(INFO, "onCrnInvAllocTrf called items trf {0} add {1} remove {2}", new Object[]{evt.getItems(),
   evt.isAdd(),evt.isRemove() });
  if(evt.getItems() == null || evt.getItems().isEmpty()){
   LOGGER.log(INFO, "No items to trf");
   return;
  }
  List<DocLineArRec> trf = (List<DocLineArRec>) evt.getItems();
  if(evt.isAdd()){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(crnAmntUnalloc == 0.0){
   MessageUtil.addInfoMessageWithoutKey("Fully Allocated", "No credit availablr to allocate");
   for(DocLineArRec ln:trf){
    crnInvAllocList.getSource().add(ln);
    crnInvAllocList.getTarget().remove(ln);
    rCtx.update("invHeaderTab:pickInvs");
   }
   
  }else{
   ListIterator<DocLineArRec> trfLi = trf.listIterator();
   while(crnAmntUnalloc > 0 && trfLi.hasNext()){
    DocLineArRec currLn = trfLi.next();
    if(currLn.getDocAmount() <= crnAmntUnalloc){
     // Invoice can be fully allocated 
     LOGGER.log(INFO, "Assign whole invoice");
     double currDocAmnt = currLn.getDocAmount();
     if(!currLn.getPostType().isDebit()){
      currDocAmnt *= -1;
     }
     crnAmntUnalloc  -= currDocAmnt;
     LOGGER.log(INFO, "crnAmntUnalloc is now {0}", crnAmntUnalloc);
    }else{
     // credit note partly pays invoice
     currLn.setPaidAmount(crnAmntUnalloc);
     crnAmntUnalloc = 0.0;
     trfLi.set(currLn);
    }
    
   }
   List<String> opts = new ArrayList<>();
   opts.add("invHeaderTab:pickInvs");
   opts.add("invHeaderTab:unallocCr");
   
   RequestContext.getCurrentInstance().update(opts);
   
  }
  }
  
  
  
 }
 
 public List<ArAccountRec> onArAcntComplete(String input){
  LOGGER.log(INFO, "onArAcntComplete called with {0}", apMgr);
  List<ArAccountRec> retList;
  if(StringUtils.isEmpty(input)){
   retList = arManager.getAccountsForCompany(docCompany);
  }else{
   retList = arManager.getAccountsByActNumberPart(input);
  }
  
  if(!retList.isEmpty()){
   Collections.sort(retList, new ArAccountByRef());
  }
  
  LOGGER.log(INFO, "retList {0}", retList);
  //List<ArAccountRec> retList = new ArrayList<>();
  
  return retList;
 }
 
 public void onArAcntContPersDlgOpen(){
  
 }
 
 public void onArAcntSelect(SelectEvent evt){
  LOGGER.log(INFO, "onArAcntSelect called with {0}", evt.getObject()); 
  String view = this.getViewSimple();
  LOGGER.log(INFO, "view {0}", view);
  custEntered = true;
  List<String> update = new ArrayList<>();
  //update.add("custName");
  if(StringUtils.equals(view, "slCreditNote")){
   update.add("salesCrnFrm");
  }else{
   update.add("arDocCrDetFrm");
   update.add("displAcntAddr"); 
  }
  docLineAr.setArAccount((ArAccountRec)evt.getObject());
  LOGGER.log(INFO, "arDocLine account id {0}", docLineAr.getArAccount().getId());
  
  RequestContext.getCurrentInstance().update(update);
 }
 
 public List<ArBankAccountRec> onArBankComplete(String input){
  
   
  if(StringUtils.isEmpty(input)){
   return  docLineAr.getArAccount().getArAccountBanks(); 
  }else{
   List<ArBankAccountRec> retList = new ArrayList<>();
   for(ArBankAccountRec bnkAcnt: docLineAr.getArAccount().getArAccountBanks() ){
    if(bnkAcnt.getBankAccount().getAccountNumber().startsWith(input)){
     retList.add(bnkAcnt);
    }
   }
   return retList;
  }
  
 }
 
 
 public void onInvLineNewDelete(){
  LOGGER.log(INFO, "onInvLineDelete called select line {0}", selectedSalesInvLine);
  if(selectedSalesInvLine == null){
   MessageUtil.addErrorMessage("slDocLineDelNone", "errorText");
   return;
  }
  
  if(salesInvLines == null || salesInvLines.isEmpty()){
   MessageUtil.addErrorMessage("slDocLineDelNone", "errorText");
   return;
  }
  
  ListIterator<SalesPartFiLineRec> linesLi = salesInvLines.listIterator();
  
  double goods = invoice.getGoodsAmount();
  LOGGER.log(INFO, "start goods {0}", goods);
  double vat = invoice.getVatAmount();
  double total = invoice.getTotalAmount();
  LOGGER.log(INFO, "start total {0}", total);
  while(linesLi.hasNext()){
   SalesPartFiLineRec curr = linesLi.next();
   if(curr.getLineNum() == selectedSalesInvLine.getLineNum()){
    double lineAmnt = curr.getLineTotal();
    double lineVat = 0.0;
    LOGGER.log(INFO, "goods {0}", goods);
    goods -= lineAmnt;
    total = (lineVat +goods );
    LOGGER.log(INFO, "total {0}", total);
    invoice.setGoodsAmount(goods);
    invoice.setVatAmount(vat);
    invoice.setTotalAmount(total);
    linesLi.remove();
    List<String> updates = new ArrayList<>();
    updates.add("docAmount");
    updates.add("lines");
    RequestContext.getCurrentInstance().update(updates);
    LOGGER.log(INFO, "End of delete salesInvLines {0}", salesInvLines);
    return;
   }
  }
  
  
  
 }
 public void onInvLineDelete(){
  LOGGER.log(INFO, "onInvLineDelete called select line {0}", selectedSalesInvLine);
  // Determine Goods value
  double totalGoods = 0.00;
  double totalVat = this.invoice.getVatAmount();
  double invTotal = 0.0;
  
  LOGGER.log(INFO, "Inv total Goods {0}", totalGoods);
  // remove Line VAT
  if(docVatSummary != null){
   
  
  ListIterator<DocVatSummary> docVatSumLi = docVatSummary.listIterator();
  boolean vatLineFound = false;
  while(docVatSumLi.hasNext() && !vatLineFound){
   DocVatSummary docVatSumRec = docVatSumLi.next();
   LOGGER.log(INFO, "docvat Sum Gl ac id {0} line glAc id {1}", new Object[]{
    
    docVatSumRec.getGlAccount().getId(),selectedSalesInvLine.getPartComp().getSalesAccount().getId()
   });
   if(docVatSumRec.getGlAccount() == selectedSalesInvLine.getPartComp().getSalesAccount() ){
    LOGGER.log(INFO, "Found delete GL account");
    double lineGoods = selectedSalesInvLine.getLineTotal();
    docVatSumRec.setGoods(docVatSumRec.getGoods() - lineGoods);
    double lineVat = docVatSumRec.getGoods() * selectedSalesInvLine.getVatCode().getVatCode().getRate();
    lineVat = GenUtil.round(lineVat, 2, BigDecimal.ROUND_HALF_UP );
    LOGGER.log(INFO, "Line vat {0}", lineVat);
    if(selectedSalesInvLine.getFund() != null && selectedSalesInvLine.getFund() == docVatSumRec.getFund() ){
     totalVat = totalVat - lineVat;
     docVatSumRec.setVat(lineVat);
     docVatSumLi.set(docVatSumRec);
     vatLineFound = true;
    }else{
     totalVat = totalVat - lineVat;
     docVatSumRec.setVat(lineVat);
     docVatSumLi.set(docVatSumRec);
     vatLineFound = true;
    }
   }else{
    totalVat = totalVat +  docVatSumRec.getVat();
   }
  }
  }
  salesInvLines.remove(selectedSalesInvLine);
  ListIterator<SalesPartFiLineRec> invLinesLi = this.salesInvLines.listIterator();
  while(invLinesLi.hasNext()){
   totalGoods = totalGoods + invLinesLi.next().getLineTotal();
  }
  invTotal = totalGoods + totalVat;
  totalGoods = GenUtil.round(totalGoods, 2, BigDecimal.ROUND_HALF_UP );
  totalVat = GenUtil.round(totalVat, 2, BigDecimal.ROUND_HALF_UP );
  invTotal = GenUtil.round(invTotal, 2, BigDecimal.ROUND_HALF_UP );
  invoice.setGoodsAmount(totalGoods);
  invoice.setVatAmount(totalVat);
  invoice.setTotalAmount(invTotal);
  LOGGER.log(INFO, "after delete Total goods {0} vat {1} total {2}", new Object[]{
   totalGoods,totalVat,invTotal
  });
  List<String> updts = new ArrayList<>();
  updts.add("docTotalsPG");
  updts.add("lines");
    
  RequestContext.getCurrentInstance().update(updts);
 }
 
 public void onInvLineUpdateBtn(){
  LOGGER.log(INFO, "onInvLineUpdateBtn called");
  LOGGER.log(INFO, "selectedSalesInvLine total {0} unit  {1} qty {2}", new Object[]{
   selectedSalesInvLine.getLineTotal(),
   selectedSalesInvLine.getUnitPrice(),selectedSalesInvLine.getQty()
  });
  if(selectedSalesInvLine.getVatCode() != null &&  selectedSalesInvLine.getVatCode().getVatCode() != null ){
   LOGGER.log(INFO, "selectedSalesInvLine VAT code:{0}", selectedSalesInvLine.getVatCode().getVatCode().getCode());
  
   setVatSum(selectedSalesInvLine, this.fiDocument.getTaxDate(), docCompany);
  }
  // Determine Goods value
  double totalGoods = 0.00;
  ListIterator<SalesPartFiLineRec> invLinesLi = this.salesInvLines.listIterator();
  while(invLinesLi.hasNext()){
   totalGoods = totalGoods + invLinesLi.next().getLineTotal();
  }
  LOGGER.log(INFO, "Inv total Goods {0}", totalGoods);
  // determine total VAT
  if(docVatSummary != null && !docVatSummary.isEmpty()){
  double totalVat = 0.0;
  ListIterator<DocVatSummary> docVatSumLi = this.docVatSummary.listIterator();
  while(docVatSumLi.hasNext()){
   
   totalVat = totalVat + docVatSumLi.next().getVat();
  }
  
  double invTotal = totalGoods + totalVat;
  
  totalGoods = GenUtil.round(totalGoods, 2, BigDecimal.ROUND_HALF_UP );
  totalVat = GenUtil.round(totalVat, 2, BigDecimal.ROUND_HALF_UP );
  invTotal = GenUtil.round(invTotal, 2, BigDecimal.ROUND_HALF_UP );
  invoice.setGoodsAmount(totalGoods);
  invoice.setVatAmount(totalVat);
  invoice.setTotalAmount(invTotal);
  }else{
   // Goods and inv total without VAT
   totalGoods = GenUtil.round(totalGoods, 2, BigDecimal.ROUND_HALF_UP );
   invoice.setGoodsAmount(totalGoods);
   invoice.setTotalAmount(totalGoods);
  }
 // LOGGER.log(INFO, "End of onInvLineUpdateBtn goods {0} vat {1} total {2}", new Object[]{
 //  totalGoods,totalVat,invTotal
 // });
 
 RequestContext rCtx = RequestContext.getCurrentInstance();
 List<String> updates = new ArrayList<>();
 updates.add("docAmount");
 updates.add("lines");
 rCtx.update(updates);
 
 rCtx.execute("PF('editLineDlgWv').hide()");
 
 }
 public void onOrderUpload(FileUploadEvent event) {
  LOGGER.log(INFO, "onOrderUpload called with {0}", event);
  UploadedFile file = event.getFile();
  LOGGER.log(INFO, "File content type {0}", file.getContentType());
    
   invoice.setUploadOrderFileData(file.getContents());
   invoice.setUploadOrderFileName(file.getFileName());
   invoice.setUploadOrderContentType(file.getContentType());
   
   MessageUtil.addClientInfoMessage("arDocCrDetFrm:orderUploadGr", "docArInvOrdUpld", "blacResponse");
   //MessageUtil.addInfoMessage("docOrdUploadOk", "formTextSl");
   
   PrimeFaces pf = PrimeFaces.current();
   List<String> updates = new ArrayList<>();
   updates.add("arDocCrDetFrm:orderUploadGr");
   updates.add("arDocCrDetFrm:orderFn");
   
   pf.ajax().update(updates);
}
 public List<ApAccountRec> onApAcntComplete(String input){
  LOGGER.entering(docDateStr, "onApAcntComplete called input {0}", input);
  List<ApAccountRec> retList = new ArrayList<>();
  if(input == null || input.isEmpty()){
   if (acntsAp == null){
    acntsAp = apMgr.getApAccountsAll(docCompany);
   }
  }else{
   
  } 
  
  return retList;
 }
 public void onAcntNumBlur(){
  LOGGER.log(INFO, "onAcntNumBlur");
  
  if(custAccounts == null || custAccounts.isEmpty()){
    custAccounts = arManager.getAccountsByActNumberPart(docLineAr.getArAccount().getArAccountCode());
  }
  ListIterator<ArAccountRec> li = custAccounts.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   ArAccountRec arAc = li.next();
   LOGGER.log(INFO,"arAc id: {0} address id {1} invoice address id {2}", new Object[]{arAc,
    arAc.getAccountAddress().getId(), arAc.getInvoiceAddress().getId() });
   if(arAc.getArAccountCode().equalsIgnoreCase(docLineAr.getArAccount().getArAccountCode())){
    custAccount = arAc;
    found = true;
   }
   
  }
  if(!found){
   custAccounts = arManager.getAccountsByActNumberPart(docLineAr.getArAccount().getArAccountCode());
   li = custAccounts.listIterator();
   while(li.hasNext() && !found){
    ArAccountRec arAc = li.next();
    if(arAc.getArAccountCode().equalsIgnoreCase(docLineAr.getArAccount().getArAccountCode())){
     custAccount = arAc;
     found = true;
    }
   }
  }
  
  if(!found){
   custAccount = new ArAccountRec();
   String msg = this.errorForKey("arAccountNumNotFound");
   GenUtil.addErrorMessage(msg);
  }else{
   custEntered = true;
  }
  this.docLineAr.getArAccount().setArAccountFor(custAccount.getArAccountFor());
  LOGGER.log(INFO, "arDocLine.getArAccount() address{0}", docLineAr.getArAccount().getAccountAddress());
  LOGGER.log(INFO, "Account name is {0}", docLineAr.getArAccount().getArAccountFor().getName());
  
  LOGGER.log(INFO, "onAcntNumBlur End");
 }
 
 
 
 public void onPaymentTermsChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onPaymentTermsChange called with value {0}", evt.getNewValue());
  PaymentTermsRec payTerms = (PaymentTermsRec)evt.getNewValue();
  docLineAr.setPayTerms(payTerms);
  docLineAr.setDueDate(getPayTermsDueDate(payTerms));
 }
 
 public List<PaymentTypeRec> onPayTypeComplete(String input){
  
  
  if(StringUtils.isBlank(input)){
   return this.getPaymentTypes();
  }else{
   List<PaymentTypeRec> payTypes = buffer.getPaymentTypesForLed(docCompany, "AR");
   if(payTypes == null || payTypes.isEmpty()){
    return null;
   }else{
    List<PaymentTypeRec> retList = new ArrayList<>();
    for(PaymentTypeRec pt:payTypes){
     if(StringUtils.startsWith(pt.getPayMedium(), input)){
      retList.add(pt);
     }
    }
    return retList;
   }
  }
 }
 
 public List<PartnerPersonRec> partnerIndivComplete(String input){
  LOGGER.log(INFO,"partnerIndivComplete called with {0}",input);
  List<PartnerPersonRec> ptnrList = this.masterDataMgr.getIndivPtnrsBySurname(input);
  
  return ptnrList;
 }
 
 public List<VatCodeRec> glAccVatCodeComplete(String entry){
  LOGGER.log(INFO, "arDocPost.glAccVatCodeComplete called with {0}", entry);
  List<VatCodeRec> glAcVatCdList = new ArrayList<>();
  if(salesInvLine == null || salesInvLine.getPartComp() == null || 
          salesInvLine.getPartComp().getSalesAccount() == null){
   return glAcVatCdList;
  }
  FiGlAccountCompRec salesGlAc = salesInvLine.getPartComp().getSalesAccount();
  LOGGER.log(INFO, "salesGlAc id {0}", salesGlAc.getId());
  LOGGER.log(INFO,"glVatAcMap is {0}",glVatAcMap);
  if(glVatAcMap == null){
   LOGGER.log(INFO, "need new glVatAcMap");
   glVatAcMap = new HashMap<Long, List<VatCodeRec> >();
   glAcVatCdList = this.glAcMgr.getGlAccountVatCodes(salesGlAc);
   glVatAcMap.put(salesGlAc.getId(), glAcVatCdList);
   LOGGER.log(INFO, "glVatAcMap is {0}", glVatAcMap);
  }else{
   LOGGER.log(INFO,"need to get comp from map");
   glAcVatCdList = glVatAcMap.get(salesGlAc.getId());
   
  }
  LOGGER.log(INFO, "glAcVatCdList {0}", glAcVatCdList);
  if(entry == null || entry.isEmpty()){
   return glAcVatCdList;
  }else{
   ListIterator<VatCodeRec> glAcVatCdLstLi = glAcVatCdList.listIterator();
   List<VatCodeRec> retList = new ArrayList<>();
   while(glAcVatCdLstLi.hasNext()){
    VatCodeRec glVatRec = glAcVatCdLstLi.next();
    if(glVatRec.getCode().startsWith(entry)){
     retList.add(glVatRec);
    }
   }
   return retList; 
  }
  
 }
 
 public List<ArAccountRec> onArAccountsByAcNumComplete(String input){
  LOGGER.log(INFO,"arAccountsByAcNumComplete called with input {0}",input);
  List<ArAccountRec> actList = arManager.getAccountsByCompActNumPart(docCompany,input);
  LOGGER.log(INFO,"arAccountsByAcNumComplete returns to web layer accounts {0}",actList);
  return actList;
 }
 
 /**
  * retrieve AR account based on account number part
  * @param input
  * @return 
  */
 public List<ArAccountRec> arAccountsByIndivComplete(String input){
  LOGGER.log(INFO,"arAccountsByIndivComplete called with input {0}",input);
  List<ArAccountRec> actList = this.arManager.getAccountsBySurname(input);
  LOGGER.log(INFO,"arAccountsByIndivComplete returns {0} to web layer",actList);
  return actList;
 }
 
 public List<ArAccountRec> arAccountsByCorpComplete(String input){
  LOGGER.log(INFO,"arAccountsByCorpComplete called with input {0}",input);
  List<ArAccountRec> actList = this.arManager.getAccountsByTradingName(input);
  LOGGER.log(INFO,"arAccountsByCorpComplete returns {0} to web layer",actList);
  return actList;
 }
 
 
 public List<SalesPartCompanyRec> onPartComplete(String input){
  LOGGER.log(INFO, "slPartCompComplete called with {0}", input);
  if(docCompany == null){
   MessageUtil.addWarnMessage("fiDocCompReq", "validationText");
   return null;
  }
  
  if(StringUtils.isEmpty(input)){
   return buffer.getCompanySalesPartsForComp(docCompany);
  }else{
   if(buffer.getCompanySalesPartsForComp(docCompany) != null){
    List<SalesPartCompanyRec> salesPartCompList = new ArrayList<>();
    for(SalesPartCompanyRec currPt:buffer.getCompanySalesPartsForComp(docCompany)){
     if(currPt.getPart().getPartCode().startsWith(input)){
      salesPartCompList.add(currPt);
     }
    } 
   return salesPartCompList;
   }
   
  }
  
 return null; 
 } 
 
 public void onAddSaleLineCxtMnu(SelectEvent evt){
  LOGGER.log(INFO, "onAddSaleLineCxtMnu called with {0}", evt.getObject());
  
 }
 public void onAddSaleInvLine(){
  LOGGER.log(INFO, "onAddSaleInvLine called");
  LOGGER.log(INFO, "arDocLine arAccount id {0}", this.docLineAr.getArAccount().getId());
  salesInvLineNew =  new SalesPartFiLineRec();
  int numLines = 1;
  if(salesInvLines != null && !salesInvLines.isEmpty()){
   numLines = salesInvLines.size();
  }
  salesInvLineNew.setLineNum(numLines);
  
  vatablePeriod = isVatableDoc();
  LOGGER.log(INFO, "vatablePeriod {0}", vatablePeriod);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  //rCtx.update("addLineGrid");
  rCtx.execute("PF('addLineDlgWv').show()");
  LOGGER.log(INFO, "onAddSaleInvLine end");
 }
 
 public void onTaxDateChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "New tax date {0}", evt.getNewValue());
  fiDocument.setTaxDate((Date)evt.getNewValue());
  vatablePeriod = isVatableDoc();
  LOGGER.log(INFO, "vatablePeriod {0}",vatablePeriod);
 }
 
 public void onTaxDateSelect(SelectEvent evt){
  LOGGER.log(INFO, "Tax date select {0}",evt.getObject());
  vatablePeriod = isVatableDoc();
  LOGGER.log(INFO, "vatablePeriod {0}",vatablePeriod);
 }
 
 public List<UomRec> onUomComplete(String input){
  List<UomRec> uoms = getUomList();
  if(StringUtils.isBlank(input)){
   return uoms;
  }else{
   List<UomRec> retList = new ArrayList<>();
   for(UomRec curr:uoms){
    if(StringUtils.startsWith(curr.getName(), input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
 
 private boolean isVatableDoc(){
  LOGGER.log(INFO, "isVatableDoc called");
  boolean rc = false;
  if(!docCompany.isVatReg()){
   return false;
  }
  if(vatRegList == null){
    vatRegList = new ArrayList<>();
   }
  if(docCompany.getVatRegistrations() == null || docCompany.getVatRegistrations().isEmpty()){
   // get company VAT reg from sys buffer
    
   List<CompVatRegistrationRec> buffVatRegList = this.buffer.getCompVatRegList();
   LOGGER.log(INFO, "Comp VAT reg from buffer {0}", vatMgr);
   ListIterator<CompVatRegistrationRec> buffVatRegListLi = buffVatRegList.listIterator();
   while(buffVatRegListLi.hasNext()){
    CompVatRegistrationRec compVatRegRec = buffVatRegListLi.next();
    LOGGER.log(INFO, "compVatRegRec {0}", compVatRegRec);
    if(Objects.equals(compVatRegRec.getCompanyId(), docCompany.getId())){
     vatRegList.add(compVatRegRec.getVatRegSumm());
    }
   }
  }
 
  
  ListIterator<VatRegistrationRec> vatRegListLi = vatRegList.listIterator();
  while(vatRegListLi.hasNext()){
   VatRegistrationRec vatRegRec = vatRegListLi.next();
   LOGGER.log(INFO, "VAT reg from {0} to {1}", new Object[]{vatRegRec.getRegistrationDate(),vatRegRec.getRegistrationEnd()});
   Date taxPoint = null;
   if(fiDocument == null){
    taxPoint = new Date();
   }else{
    taxPoint = fiDocument.getTaxDate();
   }
   LOGGER.log(INFO, "tax point {0}",taxPoint);
   if(vatRegRec.getRegistrationDate().before(taxPoint) && 
            vatRegRec.getRegistrationEnd().after(taxPoint)){
    docCompany.setVatRegDetails(vatRegRec);
    LOGGER.log(INFO, "Valid Vat reg found");
     return true;
     
    }
   }
  
  return false;
 }
 
 public List<FiGlAccountCompRec> onGlAccountComplete(String input){
  
  if(glAccountsForComp == null || glAccountsForComp.isEmpty() ){
   glAccountsForComp = this.glAcMgr.getCompanyAccounts(docCompany);
   if(glAccountsForComp == null|| glAccountsForComp.isEmpty()){
    return null;
   }
   Collections.sort(glAccountsForComp, new FiGlAcntCompByRef());
  }
  if(input == null || input.isEmpty()){
   return glAccountsForComp;
  }
  List<FiGlAccountCompRec> retList = new ArrayList<>();
  for(FiGlAccountCompRec curr: glAccountsForComp){
   if(curr.getCoaAccount().getRef().startsWith(input)){
    retList.add(curr);
   }
  }
  
  return retList;
 }
 public void onInvDateSelect(SelectEvent event){
  Date invDt = (Date)event.getObject();
  fiDocument.setTaxDate(invDt);
  
 }
 
 public void onInvDateChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onInvDateChange {0}", evt.getNewValue());
  fiDocument.setTaxDate((Date)evt.getNewValue());
  
 }
 public void onPostDateSelect(SelectEvent event){
  Date postDt = (Date)event.getObject();
  FiscalPeriodYearRec perYr = buffer.getCompFiscalPeriodYearForDate(docCompany, postDt);
  fiDocument.setFisPeriod(perYr.getPeriod());
  fiDocument.setFisYear(perYr.getYear());
  
 }
 
 
 public List<PostTypeRec> onPostKeyComplete(String input){
  List<PostTypeRec> retList = new ArrayList<>();
  if(input == null || input.isEmpty()){
   for(PostTypeRec pt:getPostTypes()){
    LOGGER.log(INFO, "POst type {0} sys use {1}", new Object[]{pt.getPostTypeCode(),pt.isSysUse()});
    if(!pt.isSysUse()){
     retList.add(pt);
    }
   }
   
  }else{
   for(PostTypeRec pt:getPostTypes()){
    LOGGER.log(INFO, "POst type {0} sys use {1}", new Object[]{pt.getPostTypeCode(),pt.isSysUse()});
    if(pt.getDescription().startsWith(input) && !pt.isSysUse()){
     retList.add(pt);
    }
   }
   
  }
  if(retList.isEmpty()){
    MessageUtil.addWarnMessageParam("fiDocPostTypeNone", "valiationText", input);
   }
   return retList;
 }
 
  
  
 
 public void onPostSlInvoice(){
  LOGGER.log(INFO,"onPostSlInvoice");
  // build the invoice data structure
  LOGGER.log(INFO, "locale {0} ",this.getUsrBuff().getLoc().getDisplayName());
  LOGGER.log(INFO, "custAccount {0} custAccount id {1}",new Object[]{docLineAr.getArAccount(), docLineAr.getArAccount().getId()}); 
  
  LOGGER.log(INFO, "arDocLine.dueDate {0}", docLineAr.getDueDate());
  
  docLineAr.setInvoice(invoice);
  LineTypeRuleRec arLineTy = this.buffer.getLineTypeRuleByCode("AR");
  docLineAr.setLineType(arLineTy);
  PostTypeRec arInvpPt = buffer.getPostTypeForCode("arInv");
  docLineAr.setPostType(arInvpPt);
  
  LOGGER.log(INFO,"after called set invoice");
  List<DocLineBaseRec> docLines = this.fiDocument.getDocLines();
  if(docLines == null  ){
   docLines = new ArrayList<>();
  }
  
  ListIterator<SalesPartFiLineRec> invLines = salesInvLines.listIterator();
  
  LOGGER.log(INFO, "invLines is {0}", invLines);
  if(salesInvLines == null || salesInvLines.isEmpty()){
   MessageUtil.addErrorMessage("docNoLines", "validationText");
   LOGGER.log(INFO, "No invLines found");
   return;
  }
  
  ListIterator<DocLineBaseRec> docLnLi = docLines.listIterator();
  while(docLnLi.hasNext()){
   DocLineBaseRec ln = docLnLi.next();
   ln.setCreateBy(getLoggedInUser());
   ln.setCreateDate(new Date());
   docLnLi.set(ln);
  }
  
  
     
  
  LOGGER.log(INFO, "arDocLine post type {0}", docLineAr.getPostType());
  docLines.add(docLineAr);
  LOGGER.log(INFO, "Doc Post date  {0} company {1}", new Object[]{fiDocument.getPostingDate(),docCompany});
  fiDocument.setDocLines(docLines);
  fiDocument.setCompany(docCompany);
  LOGGER.log(INFO, " postslInvoice docType", fiDocument.getDocType().getName());
  LOGGER.log(INFO, "FisYear {0} FisPeriod{1}" , new Object[]{fiDocument.getFisYear(),fiDocument.getFisPeriod()});
  if(fiDocument.getFisYear() < 1 ||fiDocument.getFisPeriod() < 1){
   FiscalPeriodYearRec fiscPerYr = buffer.getCompFiscalPeriodYearForDate(docCompany, fiDocument.getPostingDate());
   fiDocument.setFisPeriod(fiscPerYr.getPeriod());
   fiDocument.setFisYear(fiscPerYr.getYear());
  }
  LOGGER.log(INFO, "Call docMgr.postSaleInvoice doc {0} inv lines {1} ",
          new Object[]{fiDocument,salesInvLines});
  
  
  
  try{
   
   UserRec crUser = getLoggedInUser();
   Date crDate = new Date();
   fiDocument.setCreateOn(crDate);
   fiDocument.setCreatedBy(crUser);
   
   ListIterator<DocLineBaseRec>  lnLi =   fiDocument.getDocLines().listIterator();
   while(lnLi.hasNext()){
    DocLineBaseRec curr = lnLi.next();
    curr.setCreateBy(crUser);
    curr.setCreateDate(crDate);
    curr.setComp(docCompany);
    lnLi.set(curr);
   }
   
   fiDocument.setCompany(docCompany);
   
   LOGGER.log(INFO, "about to call postSaleInvoice");
   LOGGER.log(INFO,"fidocument date {0}",fiDocument.getDocumentDate());
   LOGGER.log(INFO,"fidocument lines {0}",fiDocument.getDocLines().get(0).getLineType().getLineCode());
   LOGGER.log(INFO,"fidocument num lines {0}",fiDocument.getDocLines().size());
   LOGGER.log(INFO,"sale line ac id {0}",salesInvLines.get(0).getPartComp().getSalesAccount().getId());
   LOGGER.log(INFO," num sales lines {0}",salesInvLines.size());
   /*if(1 == 1){
    MessageUtil.addInfoMessageWithoutKey("Post Invoice test", "Invoice total "+fiDocument.getDocLines().get(0).getDocAmount());
    MessageUtil.addErrorMessageWithoutKey("Post Invoice test", "Invoice total "+fiDocument.getDocLines().get(0).getDocAmount());
    return;
   }
 */
   fiDocument = docMgr.postSaleInvoice(fiDocument,salesInvLines,docVatSummary,restrFundBal, 
          getLoggedInUser(), getView());
   
   LOGGER.log(INFO, "fiDocument.getPartnerRef() {0}", fiDocument.getPartnerRef());
   
   if(fiDocument.getDocInvoiceAr() == null){
    fiDocument.setDocInvoiceAr(invoice);
    
   }
   fiDocument.getDocInvoiceAr().setInvoiceNumber(fiDocument.getPartnerRef());
   invPosted = true;
   byte[] invPdf = pdfStreamInvoice().toByteArray();
   invoice.setInvoicePdf(invPdf);
   
   
   LOGGER.log(INFO, "fiDocument inv number {0}", fiDocument.getDocInvoiceAr().getInvoiceNumber());
   MessageUtil.addInfoMessageVar1("slInvPosted","blacResponse",  fiDocument.getDocInvoiceAr().getInvoiceNumber());
   
   RequestContext.getCurrentInstance().update("salesInvoiceFrm");
  }catch(BacException ex){
   MessageUtil.addErrorMessage("slInvPost", "errorText");
   LOGGER.log(INFO, "Inv sale error {0}", ex.getLocalizedMessage());
  }catch(IOException | DocumentException ex){
   MessageUtil.addErrorMessage("slInvPost", "errorText");
   LOGGER.log(INFO, "Inv sale error {0}", ex.getLocalizedMessage());
  }
 }
 
 public void onPostSlCreditNote(){
  LOGGER.log(INFO, "onPostSlCreditNote called");
  LOGGER.log(INFO, "arDocLine arAccount id {0}", this.docLineAr.getArAccount().getId());
  //arDocLine.setArAccount(custAccount);
  docLineAr.setInvoice(invoice);
  PostTypeRec crnPostType = buffer.getPostTypeForCode("arCrn");
  docLineAr.setPostType(crnPostType);
  LineTypeRuleRec lnTy = buffer.getLineTypeRuleByCode("AR");
  docLineAr.setLineType(lnTy);
  
  LOGGER.log(INFO,"after called set invoice");
  List<DocLineBaseRec> docLines = fiDocument.getDocLines();
  if(docLines == null  ){
   docLines = new ArrayList<>();
  }
  ListIterator<SalesPartFiLineRec> invLines = salesInvLines.listIterator();
  LOGGER.log(INFO, "crnLines vat {0}", salesInvLines.get(0).getVatCode());
  
  LOGGER.log(INFO, "crnLines is {0}", invLines);
  if(salesInvLines == null || salesInvLines.isEmpty()){
   String msg = this.errorForKey("slInvNoLines");
   GenUtil.addErrorMessage(msg);
   return;
  }
  
  
  
  LOGGER.log(INFO, "arDocLine arAcnt id {0}", docLineAr.getArAccount().getId());
  
  docLines.add(docLineAr);
  LOGGER.log(INFO, "Doc Post date  {0} company {1}", new Object[]{fiDocument.getPostingDate(),docCompany});
  fiDocument.setDocLines(docLines);
  fiDocument.setCompany(docCompany);
  LOGGER.log(INFO, "postslCreditNote docType {0}", fiDocument.getDocType().getName());
  if(fiDocument.getFisYear() < 1 ||fiDocument.getFisPeriod() < 1){
   FiscalPeriodYearRec fiscPerYr = buffer.getCompFiscalPeriodYearForDate(docCompany, fiDocument.getPostingDate());
   fiDocument.setFisPeriod(fiscPerYr.getPeriod());
   fiDocument.setFisYear(fiscPerYr.getYear());
  }
  LOGGER.log(INFO, "Call docMgr.postCreditNote doc {0} inv lines {1}, allocated {2} VAT sum {3} fndBal {4} ",
          new Object[]{fiDocument,salesInvLines,crnInvAllocList,docVatSummary,restrFundBal});
  try{
   List<DocLineArRec> allocated = null;
   if(crnInvAllocList != null){
    allocated = crnInvAllocList.getTarget();
   }
   LOGGER.log(INFO, "Actual doc mgr call");
   fiDocument.setCreatedBy(getLoggedInUser());
   fiDocument.setCreateOn(new Date());
   fiDocument = docMgr.postSaleCreditNote(fiDocument,salesInvLines,allocated,docVatSummary,
           restrFundBal,  getView());
   invPosted = true;
   LOGGER.log(INFO, "External/Partner Ref {0}", fiDocument.getDocNumber());
   invoice.setInvoiceNumber(fiDocument.getPartnerRef());
   DocInvoiceArRec docInvoiceAr = fiDocument.getDocInvoiceAr();
   LOGGER.log(INFO, "doInvoiceAr returned by manager {0}", docInvoiceAr);
   /*if(fiDocument.getDocInvoiceAr() == null){
    fiDocument.setDocInvoiceAr(invoice);
    
   }
   */
   byte[] crnPdf = pdfStreamCreditNote().toByteArray();
   invoice.setInvoicePdf(crnPdf);
   invoice.setId(docInvoiceAr.getId());
   docInvoiceAr.setInvoicePdf(crnPdf);
   fiDocument.setDocInvoiceAr(docInvoiceAr);
   LOGGER.log(INFO, "Add pdf to DB {0}",invoice.getInvoicePdf());
   docMgr.updateInvCrnFiledata(docInvoiceAr);
   GenUtil.addInfoMessage("Credit Note posted", "Document number:"+fiDocument.getDocNumber()+
           " credit note number: "+docInvoiceAr.getInvoiceNumber());
  }catch(BacException ex){
   GenUtil.addErrorMessage("Credit Note Posting failed BAC error "+ex.getLocalizedMessage());
  }catch(IOException | DocumentException ex){
   GenUtil.addErrorMessage("Credit Note Posting failed system error "+ex.getLocalizedMessage());
  }
  
 }
 public void onPrintInv(){
  FacesContext fc = FacesContext.getCurrentInstance();
  ExternalContext ec = fc.getExternalContext();
  ec.setResponseContentType("application/pdf");
 }
 public List<ProgrammeRec> onProgrameComplete(String input){
  LOGGER.log(INFO, "onProgrameComplete called with input {0}", input);
  List<ProgrammeRec> progList;
  CompanyBasicRec comp = this.getDocCompany();
  if(input == null || input.isEmpty()){
   progList = progMgr.getAllProgrammes(comp);
   LOGGER.log(INFO, "onProgrameComplete all returns {0}",progList);
   if(progList == null | progList.isEmpty()){
    return null;
   }
   LOGGER.log(INFO, "onProgrameComplete all returns {0} 1st item id is {1}", 
           new Object[]{progList, progList.get(0).getId()});
   return progList;
  }else{
   progList = progMgr.getProgrammesByName(comp, input);
   LOGGER.log(INFO, "onProgrameComplete input returns {0}", progList);
   return progList;
  }
  
 }
 
 public List<FundRec> onRestrictedFundComplete(String input){
  
  List<FundRec> retList = new ArrayList<>();
  
  if(StringUtils.isEmpty(input)){
   return buffer.getRestrictedFunds(docCompany);
  }else{
   for(FundRec curr:buffer.getRestrictedFunds(docCompany)){
    if(curr.getName().startsWith(input)){
     retList.add(curr);
    }
   }
   return retList;
  }
  
  
 }
 
 public void onSlPartCompSelect(SelectEvent evt) {
  LOGGER.log(INFO,"onSlPartSelect called with {0}",evt.getObject());
  SalesPartCompanyRec selected =(SalesPartCompanyRec)evt.getObject();
  
  salesInvLineNew.setDescription(selected.getPart().getExternalDescription());
  LOGGER.log(INFO, "DEscr {0}", salesInvLineNew.getDescription());
  
  salesInvLineNew.setUom(selected.getUom());
  LOGGER.log(INFO, "UOM {0}", salesInvLineNew.getUom().getName());
  salesInvLineNew.setGlaccount(selected.getSalesAccount());
  LOGGER.log(INFO, "Sales GL account", salesInvLineNew.getGlaccount());
  salesInvLineNew.setUnitPrice(selected.getSaleValue());
  salesInvLineNew.setVatCode(selected.getSalesAccount().getVatCode());
  salesInvLineNew.setFund(selected.getFund());
  salesInvLineNew.setQty(1);
  double tot = salesInvLineNew.getUnitPrice() * salesInvLineNew.getQty();
  salesInvLineNew.setLineTotal(tot);
  addLineMinEntry = true;
  List<String> updates = new ArrayList<>();
  if(this.getViewSimple().equals("slCreditNote")){
   updates.add("addLineFrm:invLineDescr");
   updates.add("addLineFrm:invLineUom");
   updates.add("addLineFrm:invLinePrice");
   updates.add("addLineFrm:invLineArAcnt");
   updates.add("addLineFrm:trfLineNew");
  }else{
   updates.add("invLineDescr"); 
   updates.add("invLineUom");
   updates.add("invLinePrice");
   updates.add("invLineArAcnt");
   updates.add("trfLineNew");
  }
  RequestContext.getCurrentInstance().update(updates);
  
 }
 
 public void validateQuantity(FacesContext context, UIComponent toValidate, Object value) {
  LOGGER.log(INFO, "validateQty value {0}", value);
  if(value == null){
   ((EditableValueHolder) toValidate).setValid(false);
  }else{
   ((EditableValueHolder) toValidate).setValid(true);
  }
  
  
 }
 
 public void onSlQuantityEdValueChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onSlQuantityValueChange called with value {0}", evt.getNewValue());
  LOGGER.log(INFO, "onSlQuantityValueChange called with value class {0}", evt.getNewValue().getClass().getCanonicalName());
  
  double oldTotalAmount = selectedSalesInvLine.getLineTotal();
  invoice.setGoodsAmount(invoice.getGoodsAmount() - oldTotalAmount);
  if(evt.getNewValue() == null){
   LOGGER.log(INFO,"Null value");
   selectedSalesInvLine.setQty(0);
   
   selectedSalesInvLine.setLineTotal(0);
   selectedSalesInvLine.setQtyString("0");
   LOGGER.log(INFO, "onSlQuantityEdValueChange qty return: {0}",selectedSalesInvLine.getQty());
   double tot = selectedSalesInvLine.getUnitPrice() * 0;
   selectedSalesInvLine.setLineTotal(tot);
   RequestContext.getCurrentInstance().update("invLineTotal");
   return;
  }
  
  String input = (String)evt.getNewValue();
  int qty;
  try{
   qty = Integer.parseInt(input);
   selectedSalesInvLine.setQty(qty);
  }catch(NumberFormatException ex){
   LOGGER.log(INFO,"Qty Input not numberic");
   String msg = errorForKey("slQtyNan");
   GenUtil.addErrorMessage(msg);
   selectedSalesInvLine.setQty(0);
   selectedSalesInvLine.setQtyString("0");
   double tot = selectedSalesInvLine.getUnitPrice() * 0;
   selectedSalesInvLine.setLineTotal(tot);
   LOGGER.log(INFO,"selectedSalesInvLine.getQty() {0}",selectedSalesInvLine.getQty());
   invoice.setGoodsAmount(invoice.getGoodsAmount() + tot);
   return ;
 }
  
  LOGGER.log(INFO, "onSlQuantityEdValueChange {0} qty {1} ", new Object[]{selectedSalesInvLine,qty});
  double tot = selectedSalesInvLine.getUnitPrice() * qty;
  selectedSalesInvLine.setLineTotal(tot);
  invoice.setGoodsAmount(invoice.getGoodsAmount() + tot);
  LOGGER.log(INFO, "salesInvLine Line total {0}", salesInvLine.getLineTotal());
 }
 
 public void onSaleLineNewQty(){
  LOGGER.log(INFO, "onSaleLineNewQty {0}", salesInvLineNew.getQty());
  double tot = salesInvLineNew.getUnitPrice() * salesInvLineNew.getQty();
  salesInvLineNew.setLineTotal(tot);
  LOGGER.log(INFO, "salesInvLine Line total {0}", salesInvLineNew.getLineTotal());
  if(getViewSimple().equals("slCreditNote")){
   RequestContext.getCurrentInstance().update("addLineFrm:invLineTotal");
  }else{
   RequestContext.getCurrentInstance().update("invLineTotal");
  }
  
 }
 
 public void onSlQuantityValueChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onSlQuantityValueChange called with value {0}", evt.getNewValue());
  if(evt.getNewValue() == null){
   salesInvLineNew.setQty(0);
   
   salesInvLineNew.setLineTotal(0);
   LOGGER.log(INFO, "onSlQuantityValueChange qty return: {0}",salesInvLineNew.getQty());
   return;
  }
  int qty = (Integer)evt.getNewValue();
  salesInvLineNew.setQty(qty);
  
  
  
  LOGGER.log(INFO, "onSlQuantityValueChange {0} qty {1} ", new Object[]{salesInvLineNew, qty});
  double tot = salesInvLineNew.getUnitPrice() * qty;
  salesInvLineNew.setLineTotal(tot);
  LOGGER.log(INFO, "salesInvLine Line total {0}", salesInvLineNew.getLineTotal());
  LOGGER.log(INFO, "Call to update invLineTotal");
  if(getViewSimple().equals("slCreditNote")){
   RequestContext.getCurrentInstance().update("addLineFrm:invLineTotal");
  }else{
   RequestContext.getCurrentInstance().update("invLineTotal");
  }
  
 }
 
 public void onSlUnitPriceValueChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onSlUnitPriceValueChange called with value {0}", evt.getNewValue());
  
  double unitPrice;
  if(evt.getNewValue() == null){
   unitPrice = 0;
  }else{
   unitPrice = (Double)evt.getNewValue();
  }
  
  double tot = unitPrice * salesInvLineNew.getQty();
  salesInvLineNew.setUnitPrice(unitPrice);
  salesInvLineNew.setLineTotal(tot);
  
  if(getViewSimple().equals("slCreditNote")){
   RequestContext.getCurrentInstance().update("addLineFrm:addLineGrid");
  }else{
   RequestContext.getCurrentInstance().update("addLineGrid");
  }
  
 }
 
 public void onAddLineBtn(){
  LOGGER.log(INFO, "onAddLineBtn");
  if(salesInvLines == null){
   salesInvLines = new ArrayList<>();
  }
  if (salesInvLineNew.getQty() < 1) {
   MessageUtil.addInfoMessage("arDocLineQqytReq", "validationText");
   return;
  }
  LOGGER.log(INFO, "arDocLine arAccount id {0}", this.docLineAr.getArAccount().getId());
  LOGGER.log(INFO, "Line goods value {0} qty {1} ", 
          new Object[]{salesInvLineNew.getUnitPrice(),salesInvLineNew.getQty()});
  LOGGER.log(INFO, "Line goods GL account {0}",salesInvLineNew.getGlaccount().getCoaAccount().getRef());
  double totalAmount = GenUtil.roundUp2Dp((salesInvLineNew.getUnitPrice() * salesInvLineNew.getQty()));
  
  salesInvLineNew.setLineTotal(totalAmount );
  
  LOGGER.log(INFO, "Line total {0}",salesInvLineNew.getLineTotal());
  
  int lines =  salesInvLines.size();
  lines ++;
  salesInvLineNew.setLineNum(lines);
  // add to total Goods value
  if(invoice.getGoodsAmount() == null){
    invoice.setGoodsAmount(0.00);
  }
  
  double goodsAmount = invoice.getGoodsAmount() + salesInvLineNew.getLineTotal();
  goodsAmount = GenUtil.roundUp2Dp(goodsAmount);
  invoice.setGoodsAmount(goodsAmount);
  LOGGER.log(INFO, "invoice goods {0}",invoice.getGoodsAmount());
  boolean lineHasVat = false;
  if(salesInvLineNew.getVatCode().getId() != null){
   lineHasVat = true;
  }
  LOGGER.log(INFO, "addLine lineHasVat {0}",lineHasVat);
  //get the VAT line and recalculate the VAT amount
  LOGGER.log(INFO, "addLine goods amount Grand Total {0}", invoice.getGoodsAmount());
  //salesInvLine.setVatCode(salesInvLine.getPartComp().getSalesAccount().getVatCode());

  LOGGER.log(INFO, "salesInvLine.fund id {0}", salesInvLine.getFund());
  DocVatSummary vatSum = null;
 if(vatablePeriod && lineHasVat){ 
 if(docVatSummary == null ){
  LOGGER.log(INFO, "no docVatSummary");
  docVatSummary = new ArrayList<>();
  double lineGoodsAmount = salesInvLineNew.getLineTotal();
  lineGoodsAmount = GenUtil.round(lineGoodsAmount, 2, BigDecimal.ROUND_HALF_UP);
  vatSum = new DocVatSummary();
  VatCodeCompanyRec compVat = null;
  
  ListIterator<VatCodeCompanyRec> compVatLi = getVatCodeCompList().listIterator();
  boolean compVatFound = false;
  while(compVatLi.hasNext() && !compVatFound){
   VatCodeCompanyRec compVatRec = compVatLi.next();
   if(Objects.equals(compVatRec.getVatCode().getId(), salesInvLineNew.getVatCode().getId())){
    compVat = compVatRec;
    compVatFound = true;
   }
  }
  LOGGER.log(INFO, "compVatFound {0}",compVatFound);
  vatSum.setVatCode(salesInvLineNew.getVatCode());
  vatSum.setGoods(lineGoodsAmount);
  double vatRate = vatSum.getVatCode().getVatCode().getRate();
  double vatAmount = lineGoodsAmount * vatRate;
  vatAmount = GenUtil.roundUp2Dp(vatAmount);
  vatSum.setVat(vatAmount);
  ListIterator<VatRegSchemeRec> vatRegSchemeLi = getVatReg().getRegSchemes().listIterator();
  boolean vatRegSchemeFound = false;
  while(vatRegSchemeLi.hasNext() && !vatRegSchemeFound){
    
   VatRegSchemeRec vatRegSchemeRec = vatRegSchemeLi.next();
   if(vatRegSchemeRec.getValidFrom().before(fiDocument.getTaxDate()) 
           && vatRegSchemeRec.getValidTo().after(fiDocument.getTaxDate())){
    LOGGER.log(INFO, "Found reg scheme");
    VatSchemeRec scheme = vatRegSchemeRec.getVatScheme();
    if(scheme.isAnnualAccounting() || scheme.isAnnualAccounting() ){
     vatSum.setProvPost(true) ;
   }else{
    vatSum.setProvPost(false) ;
   }
  }
  vatSum.setRateAccount(compVat.getRateGlAccount());
  vatSum.setProvisionAccount(compVat.getAccrualGlAccount());
  FiGlAccountCompRec glAc = salesInvLineNew.getPartComp().getSalesAccount();
  LOGGER.log(INFO, "Call vatSum.setGlAccount with {0}", glAc.getId());
  vatSum.setGlAccount(glAc);
  LOGGER.log(INFO, "Call vatSum.getGlAccount returns ac id {0}",vatSum.getGlAccount().getId());
  if(compVat.getIrrecoverRate() > 0){
   vatSum.setIrrecoverable(true);
   double irrRecovAmount = goodsAmount * compVat.getIrrecoverRate();
   irrRecovAmount = GenUtil.roundUp2Dp(irrRecovAmount);
   vatSum.setIrrecoverableVat(irrRecovAmount);
   if(compVat.isWoffIrrecoverable()){
    vatSum.setIrrecoverableAccount(compVat.getChargeGlAccount());
   }else{
    vatSum.setIrrecoverableAccount(glAc);
   }
  }
  }
  LOGGER.log(INFO, "New add vat line gl account {0}", vatSum.getGlAccount().getId());
  docVatSummary.add(vatSum);
  LOGGER.log(INFO, "New add vat line with fund {0}",salesInvLineNew);
  
  if(salesInvLineNew.getFund() != null){
   if(restrFundBal == null){
    restrFundBal = new ArrayList<>();
   }
   
   RestrictFundBalance fndBal = new RestrictFundBalance();
   fndBal.setFund(salesInvLineNew.getFund());
   fndBal.setAmount(vatSum.getGoods()+vatSum.getVat());
   restrFundBal.add(fndBal);
   
  }
  LOGGER.log(INFO, "new add Gl account in docVatSummary {0}",docVatSummary.get(0).getGlAccount()); 
 }else{
  // there exists VAT lines
  LOGGER.log(INFO, "VAT lines exist");
  //logger.log(INFO, "Gl account in docVatSummary {0}",docVatSummary.get(0).getGlAccount().getId());
  LOGGER.log(INFO, "invoice gl account {0} ",new Object[]{salesInvLineNew.getGlaccount().getId()});
  ListIterator<DocVatSummary> li =  docVatSummary.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   vatSum = li.next();
   if((vatSum.getVatCode().getVatCode().getCode().equalsIgnoreCase(salesInvLine.getVatCode().getVatCode().getCode())) 
     //&& vatSum.getFund() == salesInvLine.getFund() && 
        &&   vatSum.getGlAccount() == salesInvLineNew.getPartComp().getSalesAccount() ){
    LOGGER.log(INFO, "Found line by VAT code and GL account");
    LOGGER.log(INFO, "Fund on sales line {0} fund  ", 
            new Object[]{salesInvLineNew.getFund()});
    
    if(salesInvLineNew.getFund() == null){
     LOGGER.log(INFO,"No fund on invoice line vatSum.getFund() {0}",vatSum.getFund());
     if( vatSum.getFund() == null){
       found = true;
     }
    }else{
     LOGGER.log(INFO,"need to test for fund");
     LOGGER.log(INFO,"vatSum.getFund() {0} salesInvLine.getFund() {1} ",new Object[]{vatSum.getFund(),salesInvLine.getFund()});
     if(vatSum.getFund() == null){
      found = false;
     } else if(vatSum.getFund() == salesInvLine.getFund()){
      found = true;
     }
     
    }
    
   }
   LOGGER.log(INFO, " vatsum line found {0}", found);
   if(found){
    LOGGER.log(INFO, "VAT line found");
    double goodsValue = vatSum.getGoods() + salesInvLineNew.getLineTotal();
    vatSum.setGoods(goodsValue);
    double vatRate = vatSum.getVatCode().getVatCode().getRate();
    double vatAmount = GenUtil.roundUp2Dp(goodsValue * vatRate);
    vatSum.setVat(vatAmount);
    LOGGER.log(INFO, "Update VAT line to goods {0} VAT {1}", 
      new Object[]{vatSum.getGoods(),vatSum.getVat()});
    li.set(vatSum);
    LOGGER.log(INFO, "restrFundBal {0} ", restrFundBal );
    if(restrFundBal == null){
     restrFundBal = new ArrayList<>();
    }
    ListIterator<RestrictFundBalance> restrBalLi = restrFundBal.listIterator();
    boolean restFundFound = false;
    while(restrBalLi.hasNext() && !restFundFound){
     RestrictFundBalance bal = restrBalLi.next();
     if(Objects.equals(bal.getFund().getId(), vatSum.getFund().getId())){
      restFundFound = true;
      bal.setAmount(vatSum.getGoods() + vatSum.getVat());
      restrBalLi.set(bal);
     }
    }
   }//
  }
  
  if(!found){
    LOGGER.log(INFO, "Need to add to existing VAT list");
    vatSum = new DocVatSummary();
    VatCodeCompanyRec compVat = null;
  
    ListIterator<VatCodeCompanyRec> compVatLi = this.getVatCodeCompList().listIterator();
    boolean compVatFound = false;
    while(compVatLi.hasNext() && !compVatFound){
     VatCodeCompanyRec compVatRec = compVatLi.next();
     LOGGER.log(INFO, "compVatRec code {0}", compVatRec.getVatCode().getCode());
     if(compVatRec == salesInvLineNew.getVatCode()){
      compVat = compVatRec;
      compVatFound = true;
     }
    }
   LOGGER.log(INFO, "compVatFound {0}",compVatFound);
   vatSum.setVatCode(salesInvLineNew.getVatCode());
   double vatSumTotalGoods = vatSum.getGoods();
   double saleLineGoods = GenUtil.roundUp2Dp(salesInvLineNew.getLineTotal());
   LOGGER.log(INFO, "current vatSumTotalGoods {0} sales line goods {1}",
     new Object[]{vatSumTotalGoods,saleLineGoods});
   vatSumTotalGoods = vatSumTotalGoods + saleLineGoods;
   vatSum.setGoods(vatSumTotalGoods);
   double vatRate = vatSum.getVatCode().getVatCode().getRate();
   double vatAmount = vatSumTotalGoods * vatRate;
   vatAmount = GenUtil.roundUp2Dp(vatAmount);
   LOGGER.log(INFO,"VAtsum goods {0}",vatSum.getGoods());
   LOGGER.log(INFO, "goodsAmount {0} line goods {1}",new Object[]{goodsAmount, salesInvLineNew.getLineTotal()});
   LOGGER.log(INFO, "vatAmount {0}",vatAmount);
   vatSum.setVat(vatAmount);
   ListIterator<VatRegSchemeRec> vatRegSchemeLi = getVatReg().getRegSchemes().listIterator();
   boolean vatRegSchemeFound = false;
   while(vatRegSchemeLi.hasNext() && !vatRegSchemeFound){
    VatRegSchemeRec vatRegSchemeRec = vatRegSchemeLi.next();
    if(vatRegSchemeRec.getValidFrom().before(fiDocument.getTaxDate()) 
           && vatRegSchemeRec.getValidTo().after(fiDocument.getTaxDate())){
     vatRegSchemeFound = true;
     VatSchemeRec scheme = vatRegSchemeRec.getVatScheme();
     if(scheme.isAnnualAccounting() || scheme.isAnnualAccounting() ){
      vatSum.setProvPost(true) ;
     }else{
      vatSum.setProvPost(false) ;
     }
    }
  
   vatSum.setRateAccount(compVat.getRateGlAccount());
   vatSum.setProvisionAccount(compVat.getAccrualGlAccount());
   FiGlAccountCompRec glAc = salesInvLineNew.getPartComp().getSalesAccount();
   LOGGER.log(INFO, "Call vatSum.setGlAccount with {0}", glAc.getId());
   vatSum.setGlAccount(glAc);
   LOGGER.log(INFO, "Call vatSum.getGlAccount returns ac id {0}",vatSum.getGlAccount().getId());
   LOGGER.log(INFO, "Irrecoverable VAT rate {0}",compVat.getIrrecoverRate());
   if(compVat.getIrrecoverRate() > 0){
    vatSum.setIrrecoverable(true);
    double irrRecovAmount = goodsAmount * compVat.getIrrecoverRate();
    irrRecovAmount = GenUtil.roundUp2Dp(irrRecovAmount);
    vatSum.setIrrecoverableVat(irrRecovAmount);
    if(compVat.isWoffIrrecoverable()){
     vatSum.setIrrecoverableAccount(compVat.getChargeGlAccount());
    }else{
    vatSum.setIrrecoverableAccount(glAc);
    }
   }
  }
  LOGGER.log(INFO, "New add vat line gl account {0}", vatSum.getGlAccount().getId());
  li.add(vatSum);
  //docVatSummary.add(vatSum);
  if(salesInvLineNew.getFund() != null){
   if(restrFundBal == null){
    restrFundBal = new ArrayList<>();
   }
   ListIterator<RestrictFundBalance> restBalLi = restrFundBal.listIterator();
   boolean restBalFound = false;
   while(restBalLi.hasNext() && !restBalFound){
    RestrictFundBalance bal = restBalLi.next();
    
    if(bal.getFund() == vatSum.getFund()){
     bal.setAmount(vatSum.getGoods() + vatSum.getVat());
     restBalLi.set(bal);
    }
   }
   if(!found){
    RestrictFundBalance bal = new RestrictFundBalance();
    bal.setFund(vatSum.getFund());
    bal.setAmount(vatSum.getGoods() + vatSum.getVat());
    restrFundBal.add(bal);
   }
  }
  LOGGER.log(INFO, "new add Gl account in docVatSummary {0}",docVatSummary.get(0).getGlAccount());
  }
  LOGGER.log(INFO, " add line restricted funds ");
  // got to end so can update UI
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("lines");
  rCtx.execute("PF('addLineDlgWv').hide()");
 
 }
 
 // accumulate total VAT
 LOGGER.log(INFO, "Num VAT recs {0}", docVatSummary);
 ListIterator<DocVatSummary> li =  docVatSummary.listIterator();
 invoice.setVatAmount(0.00);
 while(li.hasNext()){
  DocVatSummary rec = li.next();
  LOGGER.log(INFO, "VAT summ rec vat code {0} Goods amount {1} ", 
          new Object[]{rec.getVatCode().getVatCode().getCode(),invoice.getGoodsAmount()});
  double vatTotal = invoice.getVatAmount() + rec.getVat();
  LOGGER.log(INFO, "VAT summ total VAT {0}",vatTotal);
  invoice.setVatAmount(vatTotal);
  li.set(rec);
 }
 
 
 }else{
  // set non-VAT balances
  LOGGER.log(INFO, "non-VAT restrFundBal {0} ", restrFundBal);
  LOGGER.log(INFO,"salesInvLine.getFund() {0}", salesInvLineNew.getFund());
  if(salesInvLineNew.getFund() != null){
   LOGGER.log(INFO, "Add to fund balance");
   boolean fndFound = false;
   RestrictFundBalance fundBal = null;
   if(restrFundBal == null){
    restrFundBal = new ArrayList<>();
   }
   
   ListIterator<RestrictFundBalance> fndLi = restrFundBal.listIterator();
   while(fndLi.hasNext() && !fndFound){
    RestrictFundBalance fnd = fndLi.next();
    if(Objects.equals(fnd.getFund().getId(), salesInvLineNew.getFund().getId()) ){
     fundBal = fnd;
     fndFound = true;
    }
   }
   if(!fndFound){
    fundBal = new RestrictFundBalance();
    fundBal.setFund(salesInvLineNew.getFund());
    restrFundBal.add(fundBal);
   } 
   double fndAmnt = fundBal.getAmount();
   fndAmnt = fndAmnt + salesInvLineNew.getLineTotal();
   fundBal.setAmount(fndAmnt);
   
   // update list with fnd amount
   fndFound = false;
   fndLi = restrFundBal.listIterator();
   while(fndLi.hasNext() && !fndFound){
    RestrictFundBalance fnd = fndLi.next();
    if(Objects.equals(fnd.getFund().getId(), salesInvLineNew.getFund().getId()) ){
     fnd = fundBal;
     fndLi.set(fnd);
     fndFound = true;
    }
   }
   LOGGER.log(INFO, "end non vat fund - restricted fund Totals");
   fndLi = restrFundBal.listIterator();
   while(fndLi.hasNext()){
    RestrictFundBalance fnd = fndLi.next();
    LOGGER.log(INFO, "Fund {1} Amount {0} prop {2}", 
            new Object[]{fnd.getAmount(),fnd.getFund().getFndCode(),fnd.getProportion()});
   }
  }
  
  
 }
 invoice.setTotalAmount(invoice.getGoodsAmount() + invoice.getVatAmount());
 LOGGER.log(INFO, "selectedSalesInvLine UOM {0}", salesInvLineNew.getUom().getName());
  
 salesInvLines.add(salesInvLineNew);
  // clear sales inv line 
 crnAmntUnalloc = crnAmntUnalloc + invoice.getTotalAmount();
 if(salesInvLines.size() > 0){
  this.canPostInv = true;
 }
 LOGGER.log(INFO, "canPostInv {0}",canPostInv);
 RequestContext rCtx = RequestContext.getCurrentInstance();
 List<String> updates = new ArrayList<>();
 invoice.setTotalAmount(totalAmount);
 rCtx.execute("PF('addLineDlgWv').hide()");
 updates.add("docAmount");
 
 if(getViewSimple().equals("slCreditNote")){
  
  updates.add("salesCrnFrm");  
 }else{
  updates.add("salesInvoiceFrm");
 }
 
 rCtx.update(updates);
 LOGGER.log(INFO, "arDocLine arAccount id {0}", this.docLineAr.getArAccount().getId());
 }
 
 public void onArInvAddLineTrf(){
  LOGGER.log(INFO, "onArInvAddLineTrf called");
  boolean debit = docLineBase.getPostType().isDebit();
  double currAmnt = 0.0;
  double currAmntTax = 0.0;
  LOGGER.log(INFO, "Doc amount {0}", arDocAmnt);
  LOGGER.log(INFO, "currAmnt {0}", currAmnt);
  UUID uuid = UUID.randomUUID();
  long id = uuid.getMostSignificantBits();
  if(id > 0){
   id *= -1;
  }
  
  
  if(fiDocument.getDocLines() == null){
   fiDocument.setDocLines(new ArrayList<DocLineBaseRec>());
  }
  long lineNum = fiDocument.getDocLines().size();
  lineNum++;
  if(docLineGl.getPostType().getLedger().getCode().equals("GL")){
   
   docLineGl.setId(id);
   docLineGl.setAccountRef(docLineGl.getGlAccount().getCoaAccount().getRef());
   docLineGl.setComp(docCompany);
   docLineGl.setLineNum(lineNum);
   LineTypeRuleRec lineType = buffer.getLineTypeRuleByCode("GL");
   docLineGl.setLineType(lineType);
   if(docLineGl.getVatCodeCompany() != null){
    double taxAmnt = docLineGl.getVatCodeCompany().getVatCode().getRate() * docLineGl.getDocAmount();
    if(!docLineBase.getPostType().isDebit()){
     taxAmnt *= -1;
    }
    docLineGl.setTaxAmnt(taxAmnt);
   }
   
   
   
   
   fiDocument.getDocLines().add(docLineGl);
  }
  currAmnt = 0.0;
  currAmntTax = 0.0;
  for(DocLineBaseRec curr:fiDocument.getDocLines()){
   LOGGER.log(INFO, "Line class {0}", curr.getClass().getSimpleName());
   if(StringUtils.equals(curr.getClass().getSimpleName(), "DocLineGlRec")){
    if(curr.getPostType().isDebit()){
     currAmnt -= curr.getDocAmount();
     currAmntTax -= curr.getTaxAmnt();
    }else{
     currAmnt += curr.getDocAmount();
     currAmntTax += curr.getTaxAmnt();
    }
    
   }else {
   if(curr.getPostType().isDebit()){
    currAmnt += curr.getDocAmount();
   }else{
    currAmnt -= curr.getDocAmount();
   }
   }
  }
  arDocAmnt = currAmnt;
  LOGGER.log(INFO, "arDocAmnt {0}", arDocAmnt);
  PrimeFaces pf= PrimeFaces.current();
  List<String> updates = new ArrayList<>();
  updates.add("arDocCrSelFrm:docAmount");
  updates.add("arDocCrSelFrm:docAmountTax");
 // updates.add("arDocTabV:docLines");
  pf.ajax().update(updates);
  if(fiDocument.getDocLines().size() == 1){
   pf.ajax().update("arDocCrDetFrm:docLines");
  }else{
   pf.executeScript("PF('docLinesWv').addRow();");
  }
  pf.executeScript("PF('addDocLnWv').hide();");
  if(currAmnt != 0){
   this.canPostInv = true;
   pf.ajax().update("arDocCrDetFrm:saveBtn");
  }
  
 }

 public ByteArrayOutputStream pdfStreamCreditNote() throws IOException, DocumentException{
  LOGGER.log(INFO,"pdfStreamCreditNote called");
  
  Document stampDoc = new Document(PageSize.A4, 50, 50, 50, 50);
  ByteArrayOutputStream stampBaos = new ByteArrayOutputStream();
  FacesContext fc = FacesContext.getCurrentInstance();
  
  InputStream is = fc.getExternalContext().getResourceAsStream("/resources/pdfTemplate/CreditNoteVatComp.pdf");
  if(is == null){
   String msg = this.errorForKey("slInvNoTempl");
   GenUtil.addErrorMessage(msg);
   throw new IOException("Could not find invoice template");
  }
  LOGGER.log(INFO, "Template resource is {0}", is);
  PdfReader pdfReader = new PdfReader(is);
  PdfStamper pdfStamper = new PdfStamper(pdfReader,stampBaos);
  PdfWriter stampWriter = pdfStamper.getWriter();
  stampWriter.setMargins(50, 50, 50, 50);
  
  Rectangle page = stampWriter.getPageSize();
  
  LOGGER.log(INFO, "pdfStamper is {0}", pdfStamper);
  
  pdfStamper.getAcroFields().setField("compName", docCompany.getName());
  pdfStamper.getAcroFields().setField("compStreet", docCompany.getAddress().getStreet());
  pdfStamper.getAcroFields().setField("compTown", docCompany.getAddress().getTown());
  pdfStamper.getAcroFields().setField("compCounty", docCompany.getAddress().getCountyName());
  pdfStamper.getAcroFields().setField("compPostType", docCompany.getAddress().getPostCode());
  pdfStamper.getAcroFields().setField("compVatNum", docCompany.getVatNumber());
  pdfStamper.getAcroFields().setField("soldToName", docLineAr.getArAccount().getArAccountFor().getName());
  pdfStamper.getAcroFields().setField("soldToStreet", docLineAr.getArAccount().getAccountAddress().getStreetLine());
  pdfStamper.getAcroFields().setField("soldToTown", docLineAr.getArAccount().getAccountAddress().getTown());
  pdfStamper.getAcroFields().setField("soldToCounty", docLineAr.getArAccount().getAccountAddress().getCountyName());
  pdfStamper.getAcroFields().setField("soldToPostCd", docLineAr.getArAccount().getAccountAddress().getPostCode());
  pdfStamper.getAcroFields().setField("orderNumber", invoice.getPurchaseOrderNumber());
  pdfStamper.getAcroFields().setField("orderDate", invoice.getPurchaseOrderDateStr());
  pdfStamper.getAcroFields().setField("taxPoint", fiDocument.getTaxDateStr());
  pdfStamper.getAcroFields().setField("invoiceDate", fiDocument.getDocumentDateStr());
  pdfStamper.getAcroFields().setField("invoiceNum", invoice.getInvoiceNumber());
  pdfStamper.getAcroFields().setField("compRegNum", docCompany.getCompanyNumber());
  pdfStamper.getAcroFields().setField("compRegOffice", docCompany.getRegisteredAddressFormatted());
  pdfStamper.getAcroFields().setField("compRegName", docCompany.getLegalName());
  pdfStamper.getAcroFields().setField("notes", invoice.getNotes());
  
  
  float[] yInvLinesAll = pdfStamper.getAcroFields().getFieldPositions("invLines");
  float yInvLines = yInvLinesAll[2];
  PdfPTable table = new PdfPTable(5);
  int[] widths;
  widths = new int[]{20,10,5,10,3};
  table.setWidths(widths);
  table.setWidthPercentage(100);
  table.setHeaderRows(1);
  table.setTotalWidth(page.getWidth() - 50);
  table = addInvHeaderRowPdf(table);
  table = addInvLinesPdf(table);
  table = addInvTotalsPdf(table);
  LOGGER.log(INFO, "table rows after addInvLinesPdf {0}", table.getRows());

  PdfPTable payTermsTable = new PdfPTable(2);
  payTermsTable.setTotalWidth(page.getWidth() - 50);
  payTermsTable = this.addPayTermsLinesPdf(payTermsTable);
  
  PdfPTable taxSumTable = new PdfPTable(4);
  widths = new int[]{4,5,10,10};
  taxSumTable.setWidths(widths);
  taxSumTable.setHeaderRows(2);
  taxSumTable.setTotalWidth(page.getWidth() - 350);
  taxSumTable.setWidthPercentage(50);
  taxSumTable = addTaxSumHeaderRowPdf(taxSumTable);
  taxSumTable = addInvTaxSumLinesPdf(taxSumTable);
  LOGGER.log(INFO, "After addInvTaxSumLinesPdf");
  
  
  
  float xPos = (page.getWidth() / 2) - (table.getTotalWidth() / 2);
  LOGGER.log(INFO, "xPos {0} page.getWidth() /2 {1} table.getTotalWidth() / 2 {2} page.getBorderWidthLeft() {3}", new Object[]{
   xPos, page.getWidth() / 2,table.getTotalWidth() / 2, page.getBorderWidthLeft()
  });
  PdfContentByte cont = pdfStamper.getUnderContent(1);
  float currYpos = table.writeSelectedRows(0, -1, xPos, yInvLines, cont);
  
  LOGGER.log(INFO, "currYpos {0}", currYpos);
  
  currYpos = payTermsTable.writeSelectedRows(0, -1, xPos, currYpos, cont);
  currYpos = currYpos - 10;
  currYpos = taxSumTable.writeSelectedRows(0, -1, xPos, currYpos, cont);
  
  
  
  LOGGER.log(INFO, "currYpos {0}", currYpos);
  pdfStamper.setFormFlattening(true);
  pdfStamper.setFreeTextFlattening(true);
  pdfStamper.close();
  stampDoc.close();
  

  return stampBaos;
 }
 public ByteArrayOutputStream pdfStreamInvoice() throws IOException, DocumentException{
  LOGGER.log(INFO,"pdfStreamInvoice called");
  
  Document stampDoc = new Document(PageSize.A4, 50, 50, 50, 50);
  ByteArrayOutputStream stampBaos = new ByteArrayOutputStream();
  //float cellHeight = stampDoc.topMargin();
  //PDF Doc size
  //Rectangle page = stampDoc.getPageSize();
  
  //PdfWriter pdfWriter = PdfWriter.getInstance(stampDoc, stampBaos);
  //stampDoc.open();
  
  FacesContext fc = FacesContext.getCurrentInstance();
  
  InputStream is = fc.getExternalContext().getResourceAsStream("/resources/pdfTemplate/InvoiceVatComp.pdf");
  if(is == null){
   String msg = this.errorForKey("slInvNoTempl");
   GenUtil.addErrorMessage(msg);
   throw new IOException("Could not find invoice template");
  }
  LOGGER.log(INFO, "Template resource is {0}", is);
  PdfReader pdfReader = new PdfReader(is);
  PdfStamper pdfStamper = new PdfStamper(pdfReader,stampBaos);
  PdfWriter stampWriter = pdfStamper.getWriter();
  stampWriter.setMargins(50, 50, 50, 50);
  
  Rectangle page = stampWriter.getPageSize();
  
  LOGGER.log(INFO, "pdfStamper is {0}", pdfStamper);
  
  pdfStamper.getAcroFields().setField("compName", docCompany.getName());
  pdfStamper.getAcroFields().setField("compStreet", docCompany.getAddress().getStreet());
  pdfStamper.getAcroFields().setField("compTown", docCompany.getAddress().getTown());
  pdfStamper.getAcroFields().setField("compCounty", docCompany.getAddress().getCountyName());
  pdfStamper.getAcroFields().setField("compPostCode", docCompany.getAddress().getPostCode());
  pdfStamper.getAcroFields().setField("compVatNum", docCompany.getVatNumber());
  pdfStamper.getAcroFields().setField("soldToName", docLineAr.getArAccount().getArAccountFor().getName());
  pdfStamper.getAcroFields().setField("soldToStreet", docLineAr.getArAccount().getAccountAddress().getStreetLine());
  pdfStamper.getAcroFields().setField("soldToTown", docLineAr.getArAccount().getAccountAddress().getTown());
  pdfStamper.getAcroFields().setField("soldToCounty", docLineAr.getArAccount().getAccountAddress().getCountyName());
  pdfStamper.getAcroFields().setField("soldToPostCd", docLineAr.getArAccount().getAccountAddress().getPostCode());
  pdfStamper.getAcroFields().setField("orderNumber", invoice.getPurchaseOrderNumber());
  pdfStamper.getAcroFields().setField("orderDate", invoice.getPurchaseOrderDateStr());
  pdfStamper.getAcroFields().setField("taxPoint", fiDocument.getTaxDateStr());
  pdfStamper.getAcroFields().setField("invoiceDate", fiDocument.getDocumentDateStr());
  pdfStamper.getAcroFields().setField("invoiceNum", invoice.getInvoiceNumber());
  pdfStamper.getAcroFields().setField("compRegNum", docCompany.getCompanyNumber());
  pdfStamper.getAcroFields().setField("compRegOffice", docCompany.getRegisteredAddressFormatted());
  pdfStamper.getAcroFields().setField("compRegName", docCompany.getLegalName());
  pdfStamper.getAcroFields().setField("notes", invoice.getNotes());
  
  
  float[] yInvLinesAll = pdfStamper.getAcroFields().getFieldPositions("invLines");
  float yInvLines = yInvLinesAll[2];
 // float yVatSum = null;
 
  // table in body of invoice
  PdfPTable table = new PdfPTable(5);
  int[] widths;
  widths = new int[]{20,10,5,10,3};
  table.setWidths(widths);
  table.setWidthPercentage(100);
  table.setHeaderRows(1);
  table.setTotalWidth(page.getWidth() - 50);
  table = addInvHeaderRowPdf(table);
  table = addInvLinesPdf(table);
  table = addInvTotalsPdf(table);
  LOGGER.log(INFO, "table rows after addInvLinesPdf {0}", table.getRows());

  PdfPTable payTermsTable = new PdfPTable(2);
  payTermsTable.setTotalWidth(page.getWidth() - 50);
  payTermsTable = this.addPayTermsLinesPdf(payTermsTable);
  
  PdfPTable taxSumTable = new PdfPTable(4);
  widths = new int[]{4,5,10,10};
  taxSumTable.setWidths(widths);
  taxSumTable.setHeaderRows(2);
  taxSumTable.setTotalWidth(page.getWidth() - 350);
  taxSumTable.setWidthPercentage(50);
  taxSumTable = addTaxSumHeaderRowPdf(taxSumTable);
  taxSumTable = addInvTaxSumLinesPdf(taxSumTable);
  LOGGER.log(INFO, "After addInvTaxSumLinesPdf");
  
  
  
  float xPos = (page.getWidth() / 2) - (table.getTotalWidth() / 2);
  LOGGER.log(INFO, "xPos {0} page.getWidth() /2 {1} table.getTotalWidth() / 2 {2} page.getBorderWidthLeft() {3}", new Object[]{
   xPos, page.getWidth() / 2,table.getTotalWidth() / 2, page.getBorderWidthLeft()
  });
  PdfContentByte cont = pdfStamper.getUnderContent(1);
  
  float currYpos = table.writeSelectedRows(0, -1, xPos, yInvLines, cont);
  
  LOGGER.log(INFO, "currYpos {0}", currYpos);
  //cont = pdfStamper.getUnderContent(1);
  
  
  currYpos = payTermsTable.writeSelectedRows(0, -1, xPos, currYpos, cont);
  //float yVatSum = currYpos + 5;
  
  currYpos = currYpos - 10;
  currYpos = taxSumTable.writeSelectedRows(0, -1, xPos, currYpos, cont);
  
  
  
  LOGGER.log(INFO, "currYpos {0}", currYpos);
  pdfStamper.setFormFlattening(true);
  pdfStamper.setFreeTextFlattening(true);
  pdfStamper.close();
  stampDoc.close();
  

  return stampBaos;
 }
 
 public PdfPTable addInvHeaderRowPdf(PdfPTable table){
  LOGGER.log(INFO, "addInvHeaderRowPdf called with {0}",table);
  table.addCell(headerCell("Description",false));
  table.addCell(headerCell("Price",false));
  table.addCell(headerCell("Uom",false));
  table.addCell(headerCell("Amount",false));
  table.addCell(headerCell("Vat Cd",true));
  
  table.completeRow();
  LOGGER.log(INFO, "table.size()", table.getRows());
  return table;
 }
 
 public PdfPTable addPayTermsLinesPdf(PdfPTable table){
  LOGGER.log(INFO, "addTermsLinesPdf called with {0}",table);
  String payTerms = "Terms: " +this.docLineAr.getPayTerms().getDescription();
  
  String dueOn = "Due on: " 
          + DateFormat.getDateInstance(DateFormat.MEDIUM).format(docLineAr.getDueDate());
  table.addCell(invPayDueCell(payTerms,false));
  table.addCell(invPayDueCell(dueOn,true));
  return table;
 }
 
 public PdfPCell invPayDueCell( String value,boolean rowEnd){
  Paragraph para = new Paragraph(value);
  para.getFont().setSize(10);
  PdfPCell cell = new PdfPCell();
  cell.addElement(para);
  cell.setBorder(Rectangle.NO_BORDER);
  //cell.setBorderWidthBottom(ALIGN_LEFT);
  return cell;
 }
 
 public PdfPCell invTaxHeaderCell( String hdrText){
  Font fnt = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 255));
  Color cellBkCol = new Color(230,230,230);
  Paragraph para = new Paragraph(hdrText);
  para.setAlignment(Paragraph.ALIGN_CENTER);
  para.setFont(fnt);
  PdfPCell cell = new PdfPCell();
  
  cell.setColspan(4);
  cell.addElement(para);
  cell.setBackgroundColor(cellBkCol);
  cell.setBorder(Rectangle.NO_BORDER);
  cell.setBorderWidth(1f);
  cell.setBorderColor(new Color(0,0,255));
  cell.setBorderWidthLeft(1f);
  cell.setBorderWidthTop(1f);
  
   cell.setBorderWidthRight(1f);
  
  cell.setUseBorderPadding(true);
  
  
  return cell;
 }
 
 public PdfPTable addInvLinesPdf(PdfPTable table){
  FacesContext fc = FacesContext.getCurrentInstance();
  Locale loc = fc.getExternalContext().getRequestLocale();
  LOGGER.log(INFO, "addInvLinesPdf called with table {0}", table);
  
  ListIterator<SalesPartFiLineRec> li = salesInvLines.listIterator();
//  LOGGER.log(INFO, "pdf vat code 1st line {0}", salesInvLines.get(0).getVatCode().getVatCode().getCode());
  while(li.hasNext()){
   SalesPartFiLineRec line = li.next();
   LOGGER.log(INFO, "Add row for inv line {0}", line);
   if(line.getVatCode().getVatCode() != null){
   LOGGER.log(INFO, "line vat {0}", line.getVatCode().getVatCode().getCode());
   }
   LOGGER.log(INFO, "line uom {0}", line.getUom());
   String value;
   
   value = line.getDescription();
   table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
   value = String.valueOf(line.getUnitPrice());
   
   table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,false));
   LOGGER.log(INFO, "line.getUom() {0}", line.getUom());
   if(line.getUom() != null){
    value = line.getPartComp().getUom().getName();
   }else{
    value = "no uom ";
   }
   table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
   
   value = GenUtil.formatNumber2Dp(line.getLineTotal(), loc);
   table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,false));
   if(line.getVatCode().getVatCode() != null){
    value = line.getVatCode().getVatCode().getCode();
    LOGGER.log(INFO, "Line vat code {0} id {1}", new Object[]{line.getVatCode().getVatCode().getCode(),line.getVatCode().getId()});
    table.addCell(invLineCell(value,ALIGN_LEFT,0,0,true));
   }
   table.completeRow();
   }
  return table;
 }
 
 public PdfPTable addInvTotalsPdf(PdfPTable table){
  // Goods total
  table.addCell(invLineSpacerCell(2, 0, false,true));
  String value = "Total";
  table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
  double goodsAmount = invoice.getGoodsAmount() ;
  Locale loc = this.getUsrBuff().getLoc();
  value = GenUtil.formatCurrency(goodsAmount, 2, loc);
  LOGGER.log(INFO, "Goods value {0}", value);
  table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,true));
  table.addCell(invLineSpacerCell(1, 0, false,true));
  table.completeRow();
 
  // VAT total
  table.addCell(invLineSpacerCell(2, 0, false,true));
  value = "VAT";
  table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
  double vatAmount = invoice.getVatAmount() ;
  value = GenUtil.formatCurrency(vatAmount, 2, loc);
  LOGGER.log(INFO, "Goods value {0}", value);
  table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,true));
  table.addCell(invLineSpacerCell(1, 0, false,true));
  table.completeRow();
  // VAT total
  table.addCell(invLineSpacerCell(2, 0, false,true));
  value = "Payable";
  table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
  double totalAmount = invoice.getTotalAmount() ;
  value = GenUtil.formatCurrency(totalAmount, 2, loc);
  LOGGER.log(INFO, "Goods value {0}", value);
  table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,true));
  table.addCell(invLineSpacerCell(1, 0, false,true));
  table.completeRow();
  
  return table;
 }
 
 public PdfPTable addInvTaxSumLinesPdf(PdfPTable table){
  FacesContext fc = FacesContext.getCurrentInstance();
  Locale loc = fc.getExternalContext().getRequestLocale();
  LOGGER.log(INFO, "addInvLinesPdf called with table {0}", table);
  LOGGER.log(INFO, "addInvLinesPdf docVatSummary {0}", docVatSummary);
  if(docVatSummary == null){
   table.addCell(invLineCell(" ",ALIGN_LEFT,0,0,false));
   return table;
  }
  ListIterator<DocVatSummary> li = docVatSummary.listIterator();
  while(li.hasNext()){
   DocVatSummary line = li.next();
   String value;
   
   value = line.getVatCode().getVatCode().getCode();
   table.addCell(invLineCell(value,ALIGN_LEFT,0,0,false));
   
   value = GenUtil.formatPercent(line.getVatCode().getVatCode().getRate(), 2, loc);
   table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,false));
   
   value = GenUtil.formatNumber2Dp(line.getGoods(),  loc);
   table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,false));
   
   value = GenUtil.formatNumber2Dp(line.getVat(), loc);
   table.addCell(invLineCell(value,ALIGN_RIGHT,0,0,true));
   
   table.completeRow();
   }
  LOGGER.log(INFO, "addInvTaxSumLinesPdf");
  return table;
 }
 
 public PdfPTable addTaxSumHeaderRowPdf(PdfPTable table){
  LOGGER.log(INFO, "addInvHeaderRowPdf called with {0}",table);
  table.addCell(this.invTaxHeaderCell("VAT Summary"));
  table.addCell(headerCell("Code   ",false));
  table.addCell(headerCell("Rate  ",false));
  table.addCell(headerCell("Amount",false));
  table.addCell(headerCell("Tax",true));
  table.completeRow();
  return table;
 }
 
 public PdfPCell invLineSpacerCell( int colSpan, int rowSpan,boolean rowEnd, boolean noBorder){
  Paragraph para = new Paragraph();
  PdfPCell cell = new PdfPCell();
  para.getFont().setSize(10);
  cell.addElement(para);
  cell.setBorder(Rectangle.NO_BORDER);
  if(!noBorder){
   cell.setBorderWidthBottom(1f);
   cell.setBorderColor(new Color(0,0,255));
   cell.setBorderWidthLeft(1f);
   cell.setBorderWidthTop(1f);
  if(rowEnd){
   cell.setBorderWidthRight(1f);
  }
  cell.setUseBorderPadding(true);
  }
  if(colSpan > 0){
   cell.setColspan(colSpan);
  }
  if(rowSpan > 0){
   cell.setRowspan(rowSpan);
  }
  return cell;
 }
 public PdfPCell invLineCell(String val, int align, int colSpan, int rowSpan, boolean rowEnd){
   
  Paragraph para = new Paragraph(val);
  para.getFont().setSize(10);
  para.setAlignment(align);
  PdfPCell cell = new PdfPCell();
  cell.addElement(para);
  cell.setBorder(Rectangle.NO_BORDER);
  cell.setBorderWidth(1f);
  cell.setBorderWidthBottom(1f);
  cell.setBorderColor(new Color(0,0,255));
  cell.setBorderWidthLeft(1f);
  //cell.setBorderWidthTop(1f);
  if(rowEnd){
   cell.setBorderWidthRight(1f);
  }
  cell.setUseBorderPadding(true);
  if(colSpan > 0){
   cell.setColspan(colSpan);
  }
  if(rowSpan > 0){
   cell.setRowspan(rowSpan);
  }
  return cell;
 }

 public PdfPCell headerCell(String hdrText, boolean last ){
  Font fnt = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 255));
  Color cellBkCol = new Color(230,230,230);
  Paragraph para = new Paragraph(hdrText);
  para.setFont(fnt);
  
  //PdfPCell cell = new PdfPCell(new Paragraph(hdrText));
  PdfPCell cell = new PdfPCell();
  
  cell.addElement(para);
  cell.setBackgroundColor(cellBkCol);
  cell.setBorder(Rectangle.NO_BORDER);
  cell.setBorderWidth(1f);
  cell.setBorderWidthBottom(3f);
  cell.setBorderColor(new Color(0,0,255));
  cell.setBorderWidthLeft(1f);
  cell.setBorderWidthTop(1f);
  if(last){
   cell.setBorderWidthRight(1f);
  }
  cell.setUseBorderPadding(true);
  
  
  return cell;
 }
 
 public StreamedContent getCreditNotePDF ()throws IOException{
  LOGGER.log(INFO, "getCreditNotePDF called");
  String fname = "Credit Note -"+invoice.getInvoiceNumber()+".pdf";
  try (ByteArrayInputStream bais = new ByteArrayInputStream(invoice.getInvoicePdf())) {
   invoiceMedia = new DefaultStreamedContent(bais, "application/pdf",fname);
   LOGGER.log(INFO, "invoiceMedia {0}", invoiceMedia);
  }
  LOGGER.log(INFO, "Call docMgr.printedSalesFiDoc for invoice id [0}", invoice.getId());
  docMgr.printedSalesFiDoc(invoice, this.getLoggedInUser(), this.getView());
  return invoiceMedia;
 }
 
 public StreamedContent getInvoicePDF ()throws IOException {
 LOGGER.log(INFO, "getInvoicePDF called");
 String fname = "Invoice-"+invoice.getInvoiceNumber()+".pdf";
 

 ByteArrayInputStream bais = new ByteArrayInputStream(invoice.getInvoicePdf());
 
 invoiceMedia = new DefaultStreamedContent(bais, "application/pdf",fname);
 LOGGER.log(INFO, "invoiceMedia {0}", invoiceMedia);
 bais.close();
 return invoiceMedia;
                
 
}

public void validateAddInvLine(FacesContext context,
UIComponent toValidate, Object value) {
 LOGGER.log(INFO, "validateAddInvLine called toValidate {0} value {1}", new Object[]{toValidate,value});
}



}
