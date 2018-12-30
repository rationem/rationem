/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.util.BacsContraAmount;
import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.util.RestFundPostBalance;
import com.rationem.util.helper.ArInvReceiptLine;
import com.rationem.util.helper.BacsPyRunErrDoc;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocBankLineBacsRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BacsPaySumRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.ArManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.ArPayRunSelection;
import com.rationem.exception.BacException;
import javax.inject.Named;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.util.MessageFactory;


/**
 *
 * @author Chris
 */

public class ArReceiptBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ArReceiptBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private ArManager arMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private BankManager bnkMgr;
 
 private List<CompanyBasicRec> complist;
 private List<ArAccountRec> arAccountList;
 private List<DocTypeRec> docTypeList;
 private List<ArInvReceiptLine> outstandingDocs;
 private List<DocLineArRec> selectedDocs;
 private List<BacsPaySumRec> summaryDocs;
 private List<BacsPyRunErrDoc> pyRunErrDocs;
 private List<DocLineArRec> filteredArLineDocs;
 private List<ArInvReceiptLine> filteredDocs;
 private List<BacsPyRunErrDoc> filteredErrDocs;
 private List<PaymentTypeRec> payTypeList;
 private List<BankAccountCompanyRec> bankAcnts;
 private List<PostTypeRec> postingTypes;
 private List<DocBankLineBacsRec> bacsPayments;
 private List<DocBankLineBacsRec> bacsList;
 private BnkPaymentRunRec payRun;
 
 
 private CompanyBasicRec selectedCompany;
 private ArAccountRec selectedArAccount;
 private BankAccountCompanyRec selectedBankAccount;
 private boolean companySelected = false;
 private boolean arAccountSelected = false;
 private boolean receiptAmntEntered = false;
 private boolean receiptPostDisabled = true;
 private boolean bacsExtractDisabled = true;
 private int queryParts = 0;
 
 private DocFiRec fiDocument;
 private PaymentTypeRec payType;
 private String unallocated;
 private String receiptAmnt;
 private String clearingText;
 private double paidTotal;
 private double selectedDocsTotal;
 private String selectedDocsTotalStr;
 private double pyRunErrorTotal;
 private String pyRunErrorTotalStr;
 private DocLineArRec docLineAr;
 private ArPayRunSelection payRunSelOpts;
 
 
 private String fiscPeriodStr;

 /**
  * Creates a new instance of ArReceiptBean
  */
 public ArReceiptBean() {
 }
 
 /**
  * Set bean properties on creation
  */
 @PostConstruct
 public void init(){
  complist = sysBuff.getCompanies();
  
 }

 


 public boolean isCompanySelected() {
  return companySelected;
 }

 public void setCompanySelected(boolean companySelected) {
  this.companySelected = companySelected;
 }

 public List<ArAccountRec> getArAccountList() {
  return arAccountList;
 }

 public void setArAccountList(List<ArAccountRec> arAccountList) {
  this.arAccountList = arAccountList;
 }

 public boolean isArAccountSelected() {
  return arAccountSelected;
 }

 public void setArAccountSelected(boolean arAccountSelected) {
  this.arAccountSelected = arAccountSelected;
 }

 public ArPayRunSelection getPayRunSelOpts() {
  if(payRunSelOpts == null){
   payRunSelOpts = new ArPayRunSelection();
  }
  return payRunSelOpts;
 }

 public void setPayRunSelOpts(ArPayRunSelection payRunSelOpts) {
  this.payRunSelOpts = payRunSelOpts;
 }
 
 
 public List<ArAccountRec> arAccountComplete(String input){
  List<ArAccountRec> l = new ArrayList<ArAccountRec>();
  if(input == null || input.isEmpty() || input.equalsIgnoreCase("*")){
   l = arAccountList;
  }else{
   ListIterator<ArAccountRec> li = arAccountList.listIterator();
   while(li.hasNext()){
    ArAccountRec arAct = li.next();
    if(arAct.getArAccountCode().startsWith(input)){
     l.add(arAct);
    }
   }
   
  }
  return l;
 }
 /**
  * builds the BACS payment list
  */
 public void buildBacsTrans(){
  LOGGER.log(INFO, "buildBacsTrans called");
  bacsPayments = new ArrayList<DocBankLineBacsRec>();
  List<BacsContraAmount> bacsContraList = new ArrayList<BacsContraAmount>();
  LOGGER.log(INFO, "selected Ar Docs {0} bacsLines {1}", new Object[]{
   selectedDocs.size(),bacsPayments.size()});
  for(DocLineArRec selArln: selectedDocs){
   PaymentTypeRec payTy = selArln.getPayType();
   BacsTransCodeRec bacsCodeRec = payTy.getBacsTransCode();
   String contraBacsCd = bacsCodeRec.getContrTransCode();
   if(payTy.getSummLevel() == PaymentTypeRec.SUMM_DOC){
    // doc summarisation
    LOGGER.log(INFO, "Doc summarisation");
    DocBankLineBacsRec bacsLn = new DocBankLineBacsRec();
    bacsLn.setDocDate(fiDocument.getDocumentDate());
    bacsLn.setPostDate(fiDocument.getPostingDate());
    bacsLn.setAmount(selArln.getHomeAmount());
    LOGGER.log(INFO, "new bacs lin amount {0}", bacsLn.getAmount());
    bacsLn.setComp(fiDocument.getCompany());
    bacsLn.setArBank(selArln.getBankAc());
    bacsLn.setBacsTransCode(payTy.getBacsTransCode());
    bacsLn.setBankAccountPtnr(selArln.getBankAc().getBankAccount());
    bacsLn.setGlAccountComp(selArln.getPayType().getGlBankAccount());
    bacsLn.setBnkRef(fiDocument.getCompany().getName());
    List<DocBankLineBaseRec> bacsLines = this.payRun.getBankLines();
    if(bacsLines == null){
     bacsLines = new ArrayList<DocBankLineBaseRec>();
    }
    bacsLines.add(bacsLn);
    bacsLn.setArAccount(selArln.getArAccount());
    List<DocLineArRec> arLines = bacsLn.getArPymntLines();
    if(arLines == null){
     arLines = new ArrayList<DocLineArRec>();
    }
    arLines.add(selArln);
    bacsLn.setArPymntLines(arLines);
    bacsPayments.add(bacsLn);
    
   }else if(payTy.getSummLevel() == PaymentTypeRec.SUMM_AR_ACNT){
    // LOGGER summarise at AR level
    boolean foundBacsLn = false;
    for(DocBankLineBacsRec bacsLn:bacsPayments){
     LOGGER.log(INFO, "Process backs line {0}", bacsLn);
     if(bacsLn.getArAccount().getId() == selArln.getArAccount().getId() && 
             bacsLn.getBacsTransCode().getId() == payTy.getBacsTransCode().getId()){
      double amount = bacsLn.getAmount() + selArln.getDocAmount();
      bacsLn.setDocDate(fiDocument.getDocumentDate());
      bacsLn.setPostDate(fiDocument.getPostingDate());
      bacsLn.setAmount(amount);
      bacsLn.setAmountStr(GenUtil.formatNumberLocDp(bacsLn.getAmount(), this.getUsrBuff().getLoc()));
      selArln.setBankLine(bacsLn);
      List<DocLineArRec> bacsLines = bacsLn.getArPymntLines();
      bacsLines.add(selArln);
      bacsLn.setArPymntLines(bacsLines);
      LOGGER.log(INFO, "bacsln amount str {0}", bacsLn.getAmountStr());
     }
    }
    if(!foundBacsLn){
     DocBankLineBacsRec bacsLn = new DocBankLineBacsRec();
     bacsLn.setDocDate(fiDocument.getDocumentDate());
     bacsLn.setPostDate(fiDocument.getPostingDate());
     bacsLn.setComp(this.fiDocument.getCompany());
     bacsLn.setAmount(selArln.getHomeAmount());
     bacsLn.setArBank(selArln.getBankAc());
     bacsLn.setGlAccountComp(selArln.getPayType().getGlBankAccount());
     bacsLn.setBacsTransCode(payTy.getBacsTransCode());
     bacsLn.setBankAccountPtnr(selArln.getBankAc().getBankAccount());
     bacsLn.setBnkRef(fiDocument.getCompany().getName());
     List<DocBankLineBaseRec> bacsLines = this.payRun.getBankLines();
     bacsLines.add(bacsLn);
     bacsLn.setArAccount(selArln.getArAccount());
     List<DocLineArRec> arLines = bacsLn.getArPymntLines();
     arLines.add(selArln);
     bacsLn.setArPymntLines(arLines);
     bacsPayments.add(bacsLn);
    }
   }
   
  }
  // processed selected docs. Need to list of contra bacs lines
  LOGGER.log(INFO, "Nm bacs lines before add contra {0}", bacsPayments.size());
  int lineNum = 0;
  for(DocBankLineBacsRec bacsLn :bacsPayments){
   lineNum++;
   
   String contraCode = bacsLn.getBacsTransCode().getContrTransCode();
   ListIterator<BacsContraAmount> li = bacsContraList.listIterator();
   bacsLn.setDocDate(fiDocument.getDocumentDate());
   bacsLn.setPostDate(fiDocument.getPostingDate());
   bacsLn.setComp(fiDocument.getCompany());
   bacsLn.setAmountStr(GenUtil.formatNumberLocDp(bacsLn.getAmount(), this.getUsrBuff().getLoc()));
   bacsLn.setBacsPtnrName(bacsLn.getArAccount().getArAccountName());
   bacsLn.setBnkRef(String.valueOf(lineNum) + "|"+payRun.getRef()+"|"+bacsLn.getBnkRef());
   
   boolean contraFound = false;
   LOGGER.log(INFO, "bacsLn amount {0} partner {1}", new Object[]{bacsLn.getAmount(),bacsLn.getArAccount().getArAccountName()});
   for(BacsContraAmount contra : bacsContraList){
    LOGGER.log(INFO, "Contr list item contra code {0} bacslin contra {1}", new Object[]{contra.getContraCode(),
     contraCode   });
    if(contra.getContraCode().equalsIgnoreCase(contraCode) && 
            contra.getGlBnkAcComp().getId() == bacsLn.getGlAccountComp().getId() &&
            contra.getRestrictedFnd() == bacsLn.getRestrictedFund()){
     contra.setAmount(contra.getAmount() +bacsLn.getAmount() );
     contraFound = true;
    }
   }
   if(!contraFound){
    LOGGER.log(INFO, "need to add contra");
    // look for restricted funds in the AR documents
    List<DocLineArRec> arLinesPd = bacsLn.getArPymntLines();
    double paidAmnt = bacsLn.getAmount();
    boolean restrictedFndFound = false;
    List<RestFundPostBalance> restFundBals = new ArrayList<RestFundPostBalance>();
    for(DocLineArRec arLn : arLinesPd){
     List<DocLineGlRec> reconLines = arLn.getReconiliationLines();
     if(reconLines != null){
      for(DocLineGlRec glReconLn : reconLines){
       if(glReconLn.getRestrictedFund() != null && glReconLn.getRestrictedFund().getId() > 0){
       // we have found a restriccted fund
        boolean fndBalFound = false;
        if(restFundBals.isEmpty()){
        // bals so need to add
         RestFundPostBalance fundBal = new RestFundPostBalance();
         fundBal.setId(-1l);
         fundBal.setBalance(glReconLn.getDocAmount());
        }
       }
      }
     }
    }
    BacsContraAmount contra = new BacsContraAmount();
    contra.setAmount(bacsLn.getAmount());
    LOGGER.log(INFO, "contra amount {0}", contra.getAmount());
    contra.setContraCode(contraCode);
    contra.setGlBnkAcComp(bacsLn.getGlAccountComp());
    LOGGER.log(INFO, "contra bank gl a/c {0}", contra.getGlBnkAcComp());
    bacsContraList.add(contra);
    
    
   }
  List<DocBankLineBaseRec> bnkLines = this.payRun.getBankLines();
  if(bnkLines == null){
   bnkLines = new ArrayList<DocBankLineBaseRec>();
  }
  bacsLn.setPayRun(payRun);
  bnkLines.add(bacsLn);
  payRun.setBankLines(bnkLines);
  }
  for(BacsContraAmount contra:bacsContraList){
   // build the bacs contra entries
   DocBankLineBacsRec bacsLn = new DocBankLineBacsRec();
   BacsTransCodeRec contrBacsTransCode = this.sysBuff.getBacsTransactionCodeByCode(contra.getContraCode());
   bacsLn.setComp(this.fiDocument.getCompany());
   bacsLn.setDocDate(fiDocument.getDocumentDate());
   bacsLn.setPostDate(fiDocument.getPostingDate());
   bacsLn.setBacsTransCode(contrBacsTransCode);
   bacsLn.setAmount(contra.getAmount());
   bacsLn.setAmountStr(GenUtil.formatNumberLocDp(bacsLn.getAmount(), getUsrBuff().getLoc()));
   bacsLn.setBankAccountPtnr(this.selectedBankAccount);
   bacsLn.setGlAccountComp(contra.getGlBnkAcComp());
   bacsLn.setBacsPtnrName(this.fiDocument.getCompany().getName());
   bacsLn.setBnkRef(bacsPayments.size()+1 + "|"+payRun.getRef());
   bacsPayments.add(bacsLn);
   List<DocBankLineBaseRec> bnkLines = this.payRun.getBankLines();
   bacsLn.setPayRun(payRun);
   LOGGER.log(INFO, "Contra amount {0} gl account {1}", new Object[]{bacsLn.getAmount(), bacsLn.getGlAccountComp()});
   bnkLines.add(bacsLn);
   payRun.setBankLines(bnkLines);
  }
  LOGGER.log(INFO, "number of contra lines {0}", bacsContraList.size());
  
  for(DocBankLineBaseRec bacsLn : payRun.getBankLines()){
   LOGGER.log(INFO, "bacsLn amount {0} bacs code {1} bank ac {2}", new Object[]{bacsLn.getAmount(),
    ((DocBankLineBacsRec)bacsLn).getBacsTransCode(),((DocBankLineBacsRec)bacsLn).getBankAccountPtnr()
   });
  }
  
 }
 
 public List<CompanyBasicRec> companyComplete(String input){
  List<CompanyBasicRec> l = new ArrayList<CompanyBasicRec>();
  if(input == null || input.isEmpty()){
   l= complist;
  }else{
   ListIterator<CompanyBasicRec> li = complist.listIterator();
   while(li.hasNext()){
    CompanyBasicRec comp = li.next();
    if(comp.getReference().startsWith(input)){
     l.add(comp);
    }
   }
  }
  return l;
 }
 
 
 public List<DocTypeRec> docTypeComplete(String input){
  
  if(input == null || input.isEmpty() || input.equals("*")){
   return this.getDocTypeList();
  }else{
   List<DocTypeRec> retList = new ArrayList<DocTypeRec>();
   getDocTypeList();
   for(DocTypeRec docTy : docTypeList ){
    if(docTy.getCode().startsWith(input)){
     retList.add(docTy);
    }
   }
   return retList;
  }
  
 }
 public void onArAccountSelect(SelectEvent evt){
  LOGGER.log(INFO, "onArAccountSelect called with {0}", evt.getObject());
  List<DocLineArRec> arLines = arMgr.getOpenDocsForArAccount(this.selectedArAccount);
 
  outstandingDocs = new ArrayList<ArInvReceiptLine>();
  for(DocLineArRec docLine : arLines){
   ArInvReceiptLine osLine = new ArInvReceiptLine();
   osLine.setId(docLine.getId());
   osLine.setAllocated(false);
   PostTypeRec postType = docLine.getPostType();
   osLine.setTransType(postType.getPostTypeCode());
   if(docLine.getPostType().getSign() == '-' ){
    double amount =  docLine.getDocAmount() * -1;
    osLine.setAmount(GenUtil.formatNumberLocDp(amount, getUsrBuff().getLoc()));
    
   } else{
    osLine.setAmount(GenUtil.formatNumberLocDp(docLine.getDocAmount(), getUsrBuff().getLoc()));
   }
   osLine.setDescr(docLine.getLineText());
   osLine.setTransType(docLine.getPostType().getPostTypeCode());
   if(docLine.getDocHeaderBase() != null){
    DocFiRec docHeader = (DocFiRec)docLine.getDocHeaderBase();
    osLine.setPostType(docHeader.getDocType().getCode());
    osLine.setDocNumber(String.valueOf(docHeader.getDocNumber()));
    if(docHeader.getDocInvoiceAr() != null){
     osLine.setExtDocNumber(docHeader.getDocInvoiceAr().getInvoiceNumber());
    }
    osLine.setDocDate(docLine.getDocHeaderBase().getDocumentDate());
   }
   osLine.setDueDate(docLine.getDueDate());
   if(docLine.getInvoice() != null){
    osLine.setExtDocNumber(docLine.getInvoice().getInvoiceNumber());
   }
   osLine.setBusinessKey(docLine.getSortOrder());
   
      outstandingDocs.add(osLine);
  }
  
  
 }
 
 public void onChange(TabChangeEvent evt){
  LOGGER.log(INFO, "on tab change {0}", evt.getTab().getId());
 }
 public void onClose(TabChangeEvent evt){
  LOGGER.log(INFO, "on tab close {0}", evt.getTab().getId());
 }
 
 public void onPayRunDocSelect(SelectEvent evt){
  LOGGER.log(INFO, "onPayRunDocSelect called {0}", evt.getObject());
  DocLineArRec delDoc =(DocLineArRec)evt.getObject();
  selectedDocs.remove(delDoc);
  if(delDoc.getPostType().isDebit()){
   selectedDocsTotal = selectedDocsTotal - delDoc.getDocAmount();
  }else{
   selectedDocsTotal = selectedDocsTotal + delDoc.getDocAmount();
  }
  selectedDocsTotalStr = GenUtil.formatCurrency(selectedDocsTotal, this.getUsrBuff().getLoc());
  LOGGER.log(INFO, "number of selects docs {0}", selectedDocs);
 }
 public String onPayRunFlowProcess(FlowEvent evt){
  LOGGER.log(INFO, "Current step {0} next step {1}", new Object[]{evt.getOldStep(), evt.getNewStep()});
  Date defStart = new GregorianCalendar(1900,GregorianCalendar.JANUARY,01).getTime();
  Date defEnd = new GregorianCalendar(9999, GregorianCalendar.DECEMBER,31).getTime();
  if(evt.getOldStep().equalsIgnoreCase("docSelection") && evt.getNewStep().equalsIgnoreCase("reviewDoc")){
   
   /*
    * 1 = date only
    * 2 = doc types
    * 3 = ar accounts
    * 4 = pst ty
    * 5 = date + type
    * 6 = date + ar ac
    * 7 = date + restr
    * 8 = date + type + ar ac
    * 9 = date + type + restr
    * 10 = date + ar + restr
    * 11 = date + type + ar + fnd
    */
   queryParts = 0;
   LOGGER.log(INFO, "queryParts {0}",queryParts);
   if(payRunSelOpts.getDueDateTo() != null ||payRunSelOpts.getDueDateFrom() != null || 
           payRunSelOpts.getDocDateFrom() != null || payRunSelOpts.getDocDateTo() != null ||
           payRunSelOpts.getPostDateFrom() != null || payRunSelOpts.getPostDateTo() != null ||
           payRunSelOpts.getEntryDateFrom() != null || payRunSelOpts.getEntryDateTo() != null){
    queryParts = 1;
    LOGGER.log(INFO, "set dates in payRunSelOpts");
    // set defaults if no entry
    if(payRunSelOpts.getDueDateFrom() == null){
     payRunSelOpts.setDueDateFrom(defStart);
    }
    if(payRunSelOpts.getDueDateTo() == null){
     getPayRunSelOpts().setDueDateTo(defEnd);
    }
    if(payRunSelOpts.getDocDateFrom() == null){
     payRunSelOpts.setDocDateFrom(defStart);
    }
    if(payRunSelOpts.getDocDateTo() == null){
     payRunSelOpts.setDocDateTo(defEnd);
    }
    if(payRunSelOpts.getPostDateFrom()==null){
     payRunSelOpts.setPostDateFrom(defStart);
    }
    if(payRunSelOpts.getPostDateTo() == null){
     payRunSelOpts.setPostDateTo(defEnd);
    }
    if(payRunSelOpts.getEntryDateFrom() == null){
     payRunSelOpts.setEntryDateFrom(defStart);
    }
    if(payRunSelOpts.getEntryDateTo() == null){
     payRunSelOpts.setEntryDateTo(defEnd);
    }
   }
   
   LOGGER.log(INFO, "Doc type {0}", payRunSelOpts.getDocTypes());
   
   if(payRunSelOpts.getDocTypes() != null){
    if(queryParts == 0){
     queryParts = 2;
    }else if(queryParts == 1){
     queryParts  = 5; // date + doc types
    }
   }
   
   if(payRunSelOpts.getArAccounts() != null){
     if(queryParts == 0){
      queryParts = 3;
     }else if(queryParts == 1) {
      // Dates + AR account
      queryParts = 6;
     }else if(queryParts == 5) {
      // Dates + AR account + doc type
      queryParts = 7;
     }if(queryParts == 2) {
      // AR account + doc type
      queryParts = 8;
     }
   }
    
   if(payRunSelOpts.getPostTypes() != null){
    if(queryParts == 0){
     queryParts = 4;
    }else if(queryParts == 1) {
      // Dates + Post Type
      queryParts = 9;
     }else if(queryParts == 6) {
      // Dates +  Ar cs + pst type
      queryParts = 10;
     }else if(queryParts == 5) {
      // Dates +  Ar cs +  doc tyoe + pst type 
      queryParts = 11;
     }else if(queryParts == 8) {
      //  Ar cs +  doc type + pst type 
      queryParts = 12;
     }else if(queryParts == 2) {
      //  doc type + pst type 
      queryParts = 13;
     }else if(queryParts == 3) {
      //  Arcs + pst type 
      queryParts = 14;
    }
   }
   LOGGER.log(INFO, "Query type {0}", queryParts);
   selectedDocs = arMgr.getOpenDocsBySelectOpt(payRunSelOpts,queryParts,getLoggedInUser(),getView());
   // move error docs to error list
   validatePayRunDocs();
  }
  if(evt.getOldStep().equalsIgnoreCase("reviewDoc") && 
          evt.getNewStep().equalsIgnoreCase("docSelection")){
   //going back
   
   if(payRunSelOpts.getDueDateTo() != null && payRunSelOpts.getDueDateTo().equals(defEnd)){
    payRunSelOpts.setDueDateTo(null);
    LOGGER.log(INFO, "duedate to {0}", payRunSelOpts.getDueDateTo());
   }
   
   if(payRunSelOpts.getDueDateFrom() == null || payRunSelOpts.getDueDateFrom().equals(defStart)){
    payRunSelOpts.setDueDateFrom(null);
   }
   if(payRunSelOpts.getDocDateFrom() == null || payRunSelOpts.getDocDateFrom().equals(defStart)){
    payRunSelOpts.setDocDateFrom(null);
   }
   if(payRunSelOpts.getDocDateTo() == null ||  payRunSelOpts.getDocDateTo().equals(defEnd)){
    payRunSelOpts.setDocDateTo(null);
   }
   if(payRunSelOpts.getPostDateFrom() == null || payRunSelOpts.getPostDateFrom().equals(defStart)){
    payRunSelOpts.setPostDateFrom(null);
   }
   if(payRunSelOpts.getPostDateTo() == null || payRunSelOpts.getPostDateTo().equals(defEnd)){
    payRunSelOpts.setPostDateTo(null);
   }
   if(payRunSelOpts.getEntryDateFrom() == null || payRunSelOpts.getEntryDateFrom().equals(defStart)){
    payRunSelOpts.setEntryDateFrom(null);
   }
   if(payRunSelOpts.getEntryDateTo() == null || payRunSelOpts.getEntryDateTo().equals(defEnd)){
    payRunSelOpts.setEntryDateTo(null);
   }
   LOGGER.log(INFO, "payRunSelOpts.docdate from {0} to {1}",new Object[]{payRunSelOpts.getDocDateFrom(),payRunSelOpts.getDocDateTo()});
  }
  if(evt.getOldStep().equalsIgnoreCase("reviewDoc") && 
          evt.getNewStep().equalsIgnoreCase("postPymnt")){
   // go to post
   
   if(!selectedDocs.isEmpty() ){
   receiptPostDisabled = false;
   buildBacsTrans();
   }
  }
  return evt.getNewStep();
 }
 public void onPayEntry(CellEditEvent ce){
  LOGGER.log(INFO,"onCellEdit called with {0}",ce.getNewValue());
  
  double oldAmount = 0;
   if(ce.getOldValue() != null){
    oldAmount = Double.valueOf(String.valueOf(ce.getOldValue()));
   }
   double newAmount = 0;
   if(ce.getNewValue() != null){
    newAmount = Double.valueOf(String.valueOf(ce.getNewValue()));
   }
  
  double paymnt = newAmount - oldAmount;
  double onAc = GenUtil.formatedNum2Double(unallocated, this.getUsrBuff().getLoc());
  if(receiptAmnt == null){
   receiptAmnt = "0.00";
  }
  double receiptAmntDbl = Double.valueOf(String.valueOf(receiptAmnt));
  LOGGER.log(INFO, "receiptAmnt {0}", receiptAmnt);
  LOGGER.log(INFO, "onAc {0}", onAc);
  
  int rowIdx = ce.getRowIndex();
  ArInvReceiptLine row = outstandingDocs.get(rowIdx);
  
  double docAmount = 0;
  if(row.getAmount() != null || !row.getAmount().isEmpty()){
   docAmount = Double.valueOf(row.getAmount());
  }
  
  if(docAmount > 0 && docAmount < newAmount){
   String msg = this.validationForKey("arRecOverPdDoc");
   double adj = docAmount - newAmount;
   newAmount = docAmount;
   paymnt = paymnt + adj;
   GenUtil.addInfoMessage(msg);
  }else if(docAmount < 0 && docAmount > newAmount){
   String msg = this.validationForKey("arRecOverPdDoc");
   newAmount = docAmount;
   GenUtil.addInfoMessage(msg);
   this.receiptAmnt = String.valueOf(newAmount);
  }
    
  paidTotal = paidTotal + paymnt;
  
  double remaining =  docAmount - newAmount;
  
  String unPaid = GenUtil.formatNumberLocDp(remaining, getUsrBuff().getLoc());
  String payStr = GenUtil.formatNumberLocDp(newAmount, getUsrBuff().getLoc());
  row.setUnpaid(unPaid);
  row.setPayment(payStr);
  LOGGER.log(INFO, "payStr is {0}", payStr);
  outstandingDocs.set(rowIdx, row);
  if(receiptAmnt == null || receiptAmnt.isEmpty()){
   unallocated = GenUtil.formatNumberLocDp(0 - paidTotal, 
          this.getUsrBuff().getLoc());
  }else{
   unallocated = GenUtil.formatNumberLocDp(Double.valueOf(this.receiptAmnt) - paidTotal, 
          this.getUsrBuff().getLoc());
  }
 }
 
 public void onNewSingleReceipt(){
  this.resetFields();
 }
 
 public void onPostPayRun(){
  LOGGER.log(INFO, "onPostPayRun called");
  this.bacsExtractDisabled = false;
  
   payRun.setRunDate(fiDocument.getDocumentDate());
   payRun.setComp(fiDocument.getCompany());
   LOGGER.log(INFO, "payRun company {0}", payRun.getComp());
   try{
    DocFiRec posted = this.docMgr.postArBacsReceiptRun(fiDocument, payRun, selectedBankAccount, clearingText,
            getLoggedInUser(), getView());
   String msg = this.responseForKey("arPayRunDocNum")+String.valueOf(posted.getDocNumber());
   GenUtil.addInfoMessage(msg);
   LOGGER.log(INFO, "Posted {0}", posted);
  }catch(BacException ex){
   String msg = this.errorForKey("arDocPyRunPost")+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
  }
  
  
  
  
 }
 public void onCompanySelect(SelectEvent event){
  LOGGER.log(INFO, "onCompanySelect called with company id{0}", 
          ((CompanyBasicRec)event.getObject()).getId());
  selectedCompany = (CompanyBasicRec)event.getObject();
  companySelected = true;
  arAccountList = this.arMgr.getAccountsForCompany(selectedCompany);
  if(fiDocument.getCompany() != null){
   bankAcnts = bnkMgr.getBankAccountsForCompany(fiDocument.getCompany());
  }
 }

 public void onPostDateSelect(SelectEvent evt){
  LOGGER.log(INFO, "onPostDateSelect called {0}", evt.getObject());
  LOGGER.log(INFO, "fiDocument.getPostingDate() {0}",fiDocument.getPostingDate());
  FiscalPeriodYearRec perYr = sysBuff.getCompFiscalPeriodYearForDate(selectedCompany, 
           fiDocument.getPostingDate());
  fiDocument.setFisPeriod(perYr.getPeriod());
  fiDocument.setFisYear(perYr.getYear());
  fiscPeriodStr = fiDocument.getFisYear() + " / "+ fiDocument.getFisPeriod();
 }
 
 public void onReceiptAmountKeyup(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "onReceiptAmountKeyup called {0}", evt.getSource());
  LOGGER.log(INFO, "receipt amount {0}",receiptAmnt);
  receiptAmntEntered = true;
  double openAmnt = Double.valueOf(receiptAmnt) - this.paidTotal;
  unallocated = GenUtil.formatNumberLocDp(openAmnt, this.getUsrBuff().getLoc());
 }
 
 

 public void onReceiptSave(){
  LOGGER.log(INFO, "onReceiptSave clicked with comp {0} ar Account {1} amount {2}", 
          new Object[]{this.selectedCompany.getId(), this.selectedArAccount.getId(), this.receiptAmnt});
  List<DocLineArRec> paidDocLines = new ArrayList<DocLineArRec>();
  for(ArInvReceiptLine arRecLine : this.outstandingDocs){
   LOGGER.log(INFO, "arRecLine paid {0}", arRecLine.getPayment());
   if(arRecLine.getPayment() != null && !arRecLine.getPayment().isEmpty()){
    DocLineArRec paidLine = new DocLineArRec();
    paidLine.setId(arRecLine.getId());
    paidLine.setDocAmount(Double.valueOf(arRecLine.getAmount()));
    paidLine.setPaidAmount(Double.valueOf(arRecLine.getPayment()));
    LOGGER.log(INFO, "paidLine paid amount {0}", paidLine.getPaidAmount());
    paidDocLines.add(paidLine);
   }
  }
  
  // build payment document
  
  fiDocument.setCompany(selectedCompany);
  fiDocument.setCreatedBy(this.getLoggedInUser());
  fiDocument.setCreateOn(new Date());
  
  // Ar line
  DocLineArRec arLine = new DocLineArRec();
  arLine.setArAccount(selectedArAccount);
  arLine.setDocAmount(Double.valueOf(receiptAmnt));
  arLine.setPayType(payType);
  arLine.setPaidAmount(paidTotal);
  PostTypeRec payPostTy = sysBuff.getPostTypeForCode("arPymnt");
  arLine.setPostType(payPostTy);
  List<DocLineBaseRec> receiptDocLines = new ArrayList<DocLineBaseRec>();
  receiptDocLines.add(arLine);
  fiDocument.setDocLines(receiptDocLines);
 try{ 
  fiDocument =  docMgr.postArReceiptSingle(fiDocument, paidDocLines, getUsrBuff().getLoc(),getLoggedInUser(), getView());
  GenUtil.addInfoMessage("Receipt Posting doc number: "+fiDocument.getDocNumber());
  resetFields();
  
  LOGGER.log(INFO, "selectedCompany {0}", selectedCompany);
 }catch(BacException ex){
  GenUtil.addErrorMessage("Receipt posting failed B Reason: "+ex.getLocalizedMessage());
 }catch(Exception ex){
  GenUtil.addErrorMessage("Receipt posting failed other Reason: "+ex.getLocalizedMessage());
 }
 }
 
 public List<PostTypeRec> postingTypeComplete(String input){
  if(input == null || input.isEmpty() || input.startsWith("*")){
   return getPostingTypes();
  }else{
   List<PostTypeRec> list = new ArrayList<PostTypeRec>();
   for(PostTypeRec pstTy : getPostingTypes()){
    if(pstTy.getPostTypeCode().startsWith(input))
     list.add(pstTy);
   }
   return list;
  }
  
 }
 public void resetFields(){
  fiDocument = null;
  receiptAmnt = null;
  unallocated = null;
  outstandingDocs = null;
  selectedCompany = null;
  selectedArAccount = null;
  payType = null;
  fiscPeriodStr = null;
  payTypeList = null;
  outstandingDocs = null;
  companySelected = false;
 }

 public boolean isBacsExtractDisabled() {
  return bacsExtractDisabled;
 }

 public void setBacsExtractDisabled(boolean bacsExtractDisabled) {
  this.bacsExtractDisabled = bacsExtractDisabled;
 }

 public List<DocBankLineBacsRec> getBacsPayments() {
  return bacsPayments;
 }

 public void setBacsPayments(List<DocBankLineBacsRec> bacsPayments) {
  this.bacsPayments = bacsPayments;
 }

 public List<BankAccountCompanyRec> getBankAcnts() {
  if(bankAcnts == null && fiDocument.getCompany() != null){
   bankAcnts = bnkMgr.getBankAccountsForCompany(this.fiDocument.getCompany());
  }
  return bankAcnts;
 }

 public void setBankAcnts(List<BankAccountCompanyRec> bankAcnts) {
  this.bankAcnts = bankAcnts;
 }

  
 
 public List<CompanyBasicRec> getComplist() {
  return complist;
 }

 public void setComplist(List<CompanyBasicRec> complist) {
  this.complist = complist;
 }

 public String getClearingText() {
  return clearingText;
 }

 public void setClearingText(String clearingText) {
  this.clearingText = clearingText;
 }

 public DocLineArRec getDocLineAr() {
  return docLineAr;
 }

 public void setDocLineAr(DocLineArRec docLineAr) {
  this.docLineAr = docLineAr;
 }

 public List<DocTypeRec> getDocTypeList() {
  if(docTypeList == null || docTypeList.isEmpty()){
   docTypeList = sysBuff.getLedgerDocumentTypes("AR", getLoggedInUser());
  }
  return docTypeList;
 }

 public void setDocTypeList(List<DocTypeRec> docTypeList) {
  this.docTypeList = docTypeList;
 }
 
 public DocFiRec getFiDocument() {
  if(fiDocument == null){
   fiDocument = new DocFiRec();
  }
  return fiDocument;
 }

 public void setFiDocument(DocFiRec fiDocument) {
  this.fiDocument = fiDocument;
 }

 public List<ArInvReceiptLine> getFilteredDocs() {
  
  return filteredDocs;
 }

 public void setFilteredDocs(List<ArInvReceiptLine> filteredDocs) {
  this.filteredDocs = filteredDocs;
 }

 public List<DocLineArRec> getFilteredArLineDocs() {
  return filteredArLineDocs;
 }

 public void setFilteredArLineDocs(List<DocLineArRec> filteredArLineDocs) {
  this.filteredArLineDocs = filteredArLineDocs;
 }

 public List<BacsPyRunErrDoc> getFilteredErrDocs() {
  return filteredErrDocs;
 }

 public void setFilteredErrDocs(List<BacsPyRunErrDoc> filteredErrDocs) {
  this.filteredErrDocs = filteredErrDocs;
 }

 
 public String getFiscPeriodStr() {
  if(fiscPeriodStr == null){
   fiscPeriodStr = new String();
  }
  return fiscPeriodStr;
 }

 public void setFiscPeriodStr(String fiscPeriodStr) {
  this.fiscPeriodStr = fiscPeriodStr;
 }

 public List<PaymentTypeRec> getPayTypeList() {
  if(payTypeList == null && this.companySelected){
   payTypeList = sysBuff.getPaymentTypes(selectedCompany);
   ListIterator<PaymentTypeRec> payTypeLi = payTypeList.listIterator();
   boolean found = false;
   while(payTypeLi.hasNext() && !found){
    PaymentTypeRec pt = payTypeLi.next();
    LOGGER.log(INFO, "pt name {0} inbound {1}", new Object[]{pt.getPayTypeCode(),pt.isInbound()});
    if(!pt.isInbound()){
     payTypeLi.remove();
     LOGGER.log(INFO, "payment out remove from list"); 
    }
   }
   LOGGER.log(INFO, "paytypes {0}",payTypeList);
  }
  return payTypeList;
 }

 public void setPayTypeList(List<PaymentTypeRec> payTypeList) {
  this.payTypeList = payTypeList;
 }

 public List<ArInvReceiptLine> getOutstandingDocs() {
  return outstandingDocs;
 }

 public void setOutstandingDocs(List<ArInvReceiptLine> outstandingDocs) {
  this.outstandingDocs = outstandingDocs;
 }

 public double getPaidTotal() {
  return paidTotal;
 }

 public void setPaidTotal(double paidTotal) {
  this.paidTotal = paidTotal;
 }

 public BnkPaymentRunRec getPayRun() {
  if(payRun == null){
   payRun = new BnkPaymentRunRec();
  }
  return payRun;
 }

 public void setPayRun(BnkPaymentRunRec payRun) {
  this.payRun = payRun;
 }

 
 public PaymentTypeRec getPayType() {
  if(payType == null){
   payType = new PaymentTypeRec();
  }
  return payType;
 }

 public void setPayType(PaymentTypeRec payType) {
  this.payType = payType;
 }

 public List<PostTypeRec> getPostingTypes() {
  if(postingTypes == null){
   // need to get AR posting types
   postingTypes = sysBuff.getPostCodesForLedger("AR");
  }
  return postingTypes;
 }

 public void setPostingTypes(List<PostTypeRec> postingTypes) {
  this.postingTypes = postingTypes;
 }

 public List<BacsPyRunErrDoc> getPyRunErrDocs() {
  return pyRunErrDocs;
 }

 public void setPyRunErrDocs(List<BacsPyRunErrDoc> pyRunErrDocs) {
  this.pyRunErrDocs = pyRunErrDocs;
 }

 public double getPyRunErrorTotal() {
  return pyRunErrorTotal;
 }

 public void setPyRunErrorTotal(double pyRunErrorTotal) {
  this.pyRunErrorTotal = pyRunErrorTotal;
 }

 public String getPyRunErrorTotalStr() {
  pyRunErrorTotalStr = GenUtil.formatCurrency(pyRunErrorTotal, this.getUsrBuff().getLoc());
  return pyRunErrorTotalStr;
 }

 public void setPyRunErrorTotalStr(String pyRunErrorTotalStr) {
  this.pyRunErrorTotalStr = pyRunErrorTotalStr;
 }

 
 public String getReceiptAmnt() {
  return receiptAmnt;
 }

 public void setReceiptAmnt(String receiptAmnt) {
  this.receiptAmnt = receiptAmnt;
 }

 public boolean isReceiptAmntEntered() {
  return receiptAmntEntered;
 }

 public void setReceiptAmntEntered(boolean receiptAmntEntered) {
  this.receiptAmntEntered = receiptAmntEntered;
 }

 public boolean isReceiptPostDisabled() {
  return receiptPostDisabled;
 }

 public void setReceiptPostDisabled(boolean receiptPostDisabled) {
  this.receiptPostDisabled = receiptPostDisabled;
 }

  
 public ArAccountRec getSelectedArAccount() {
  return selectedArAccount;
 }

 public void setSelectedArAccount(ArAccountRec selectedArAccount) {
  this.selectedArAccount = selectedArAccount;
 }

 public BankAccountCompanyRec getSelectedBankAccount() {
  return selectedBankAccount;
 }

 public void setSelectedBankAccount(BankAccountCompanyRec selectedBankAccount) {
  this.selectedBankAccount = selectedBankAccount;
 }
 
 public CompanyBasicRec getSelectedCompany() {
  return selectedCompany;
 }

 public void setSelectedCompany(CompanyBasicRec selectedCompany) {
  this.selectedCompany = selectedCompany;
 }

 public List<DocLineArRec> getSelectedDocs() {
  if(selectedDocs == null){
   selectedDocs = new ArrayList<DocLineArRec>();
  }
  return selectedDocs;
 }

 public void setSelectedDocs(List<DocLineArRec> selectedDocs) {
  this.selectedDocs = selectedDocs;
 }

 public double getSelectedDocsTotal() {
  return selectedDocsTotal;
 }

 public void setSelectedDocsTotal(double selectedDocsTotal) {
  this.selectedDocsTotal = selectedDocsTotal;
 }

 public String getSelectedDocsTotalStr() {
  return selectedDocsTotalStr;
 }

 public void setSelectedDocsTotalStr(String selectedDocsTotalStr) {
  this.selectedDocsTotalStr = selectedDocsTotalStr;
 }

 
 public String getUnallocated() {
  if(unallocated == null || unallocated.isEmpty()){
   unallocated = "0.00";
  }
  return unallocated;
 }

 public void setUnallocated(String unallocated) {
  LOGGER.log(INFO, "setUnallocated called with {0}", unallocated);
  this.unallocated = GenUtil.formatNumberLocDp(Double.valueOf(unallocated),getUsrBuff().getLoc());
  LOGGER.log(INFO, "setUnallocated final {0}", unallocated);
 }

 
 public void validatePaymentRun(FacesContext fc, UIComponent comp, Object val){
  LOGGER.log(INFO, "validatePaymentRun called with ctx {0} comp {1} obj {2}", new Object[]{fc,comp,val});
  boolean payRunRefAvailable = this.bnkMgr.paymentRunRefAvailable(selectedCompany, (String)val);
  if(!payRunRefAvailable){
   ((UIInput)comp).setValid(false);
   String msg = this.errorForKey("arDocPyRunRef");
   GenUtil.addErrorMessage(msg);
  }else{
   ((UIInput)comp).setValid(true);
  }
 }
 public void validatePayRunDocs(){
  pyRunErrDocs = new ArrayList<BacsPyRunErrDoc>();
   ListIterator<DocLineArRec> li = selectedDocs.listIterator();
   while(li.hasNext()){
    DocLineArRec ln = li.next();
    boolean errorDoc = false;
    if(ln.getPayType() == null){
     LOGGER.log(INFO, "Not payment type doc: ",ln.getDocHeaderBase().getDocNumber());
     BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
     errLn.setDoc(ln);
     errLn.setErrorCD(this.errorForKey("arDocPyBacsNoPayTypeCd"));
     errLn.setErrorText(this.errorForKey("arDocPyBacsNoPayType"));
     errorDoc = true;
     if(ln.getPostType().isDebit()){
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }else{
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount() * -1, this.getUsrBuff().getLoc()));
     }
     pyRunErrDocs.add(errLn);
    } else if(!(ln.getPayType().getPayMedium().equalsIgnoreCase("DC") || ln.getPayType().getPayMedium().equalsIgnoreCase("DD")) ){
     LOGGER.log(INFO, "Not BACS payment type doc: ",ln.getDocHeaderBase().getDocNumber());
     LOGGER.log(INFO, "Not BACS payment medium {0}",ln.getPayType().getPayMedium());
     BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
     errLn.setDoc(ln);
     errLn.setErrorCD(this.errorForKey("arDocPyBacsPayMediumCd"));
     errLn.setErrorText(this.errorForKey("arDocPyBacsPayMedium"));
     errorDoc = true;
     if(ln.getPostType().isDebit()){
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }
     pyRunErrDocs.add(errLn);
    }else if(ln.getPayType().getPayTypeForBankAccount().getId() != this.selectedBankAccount.getId()){
     LOGGER.log(INFO, "Payment method bank {0}not valid run bank {1} ", new Object[]{
      ln.getPayType().getPayTypeForBankAccount().getId(),selectedBankAccount.getId()  });
     BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
     errLn.setDoc(ln);
     errLn.setErrorCD(this.errorForKey("arDocPyBacsBnkCd"));
     errLn.setErrorText(this.errorForKey("arDocPyBacsBnk" +"bank id"));
     errorDoc = true;
     if(ln.getPostType().isDebit()){
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }else{
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }
     pyRunErrDocs.add(errLn);
    }
    else if(ln.getBankAc() == null){
     LOGGER.log(INFO, "No AR bank for doc {0}", ln.getDocHeaderBase().getDocNumber());
     BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
     errLn.setDoc(ln);
     errLn.setErrorCD(this.errorForKey("arDocPyBacsBnkCd"));
     errLn.setErrorText(this.errorForKey("arDocPyBacsBnk"));
     errorDoc = true;
     
    
     if(ln.getPostType().isDebit()){
      selectedDocsTotal = selectedDocsTotal + ln.getDocAmount();
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }else{
      selectedDocsTotal = selectedDocsTotal - ln.getDocAmount();
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
     }
     pyRunErrDocs.add(errLn);
     
    }if(ln.getBankAc() != null ){
     LOGGER.log(INFO, "Coll auth: {0} valid from {1}", new Object[]{ln.getBankAc().getCollAuthYesNo(),
      ln.getBankAc().getCollValidFrom()});
     
     // check we have collection authorisation
     if(ln.getBankAc().getCollAuthYesNo().equalsIgnoreCase("No")){
      errorDoc = true;
      BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
      errLn.setDoc(ln);
      errLn.setErrorCD(this.errorForKey("arDocPyBacsCollAuthCd"));
      errLn.setErrorText(this.errorForKey("arDocPyBacsCollAuth"));
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
      pyRunErrDocs.add(errLn);
     } else if(ln.getBankAc().getCollValidFrom() == null || 
             ln.getBankAc().getCollValidFrom().after(new Date())){
      
      errorDoc = true;
      BacsPyRunErrDoc errLn = new BacsPyRunErrDoc();
      errLn.setDoc(ln);
      errLn.setErrorCD(this.errorForKey("arDocPyBacsCollAuthDtCd"));
      errLn.setErrorText(this.errorForKey("arDocPyBacsCollAuthDt"));
      ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
      pyRunErrDocs.add(errLn);
     }
    }
    if(errorDoc){
     if(ln.getPostType().isDebit()){
      pyRunErrorTotal = pyRunErrorTotal + ln.getDocAmount();
     }else{
      pyRunErrorTotal = pyRunErrorTotal - ln.getDocAmount();
     }
     li.remove();
    }
   }
   LOGGER.log(INFO, "Number of pyRunErrDocs errors {0}", pyRunErrDocs.size());
   selectedDocsTotal = 0.0;
   for(DocLineArRec ln : selectedDocs){
    if(ln.getPostType().isDebit()){
     selectedDocsTotal = selectedDocsTotal + ln.getDocAmount();
     ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
    }else{
     selectedDocsTotal = selectedDocsTotal - ln.getDocAmount();
     ln.setDocAmountStr(GenUtil.formatNumberLocDp(ln.getDocAmount(), this.getUsrBuff().getLoc()));
    }
   }
   selectedDocsTotalStr = GenUtil.formatCurrency(selectedDocsTotal, this.getUsrBuff().getLoc());
   LOGGER.log(INFO, "AR Mgr returns {0}", selectedDocs);
  }  
 
 
}
