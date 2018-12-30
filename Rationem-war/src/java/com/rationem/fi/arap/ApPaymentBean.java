/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocBankLineBacsRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.tr.PaymentMediumManager;
import com.rationem.entity.document.DocBankLineBacs;
import com.rationem.entity.document.DocBankLineChq;
import com.rationem.helper.PayRunSumAct;
import com.rationem.helper.comparitor.ApLineByAcntRef;
import com.rationem.helper.comparitor.DocLineApByAcntPayLevelMedium;
import com.rationem.util.ApLineSel;
import com.rationem.util.BaseBean;
import com.rationem.util.FundBalance;
import com.rationem.util.MessageUtil;
import com.rationem.util.ApArPayRunDocLine;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;
import java.util.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import static java.util.logging.Level.INFO;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
/**
 *
 * @author user
 */
public class ApPaymentBean extends BaseBean {
 
 private static final Logger LOGGER = Logger.getLogger(ApPaymentBean.class.getName());
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private PaymentMediumManager payMediumMgr;
 
 private DocLineApRec docLineAp;
 private DocLineApRec docLineSelected;
 private List<DocLineApRec> docLinesSelected;
 
 
 
 private List<DocLineApRec> docLinesCheckBSel;
 private List<DocLineApRec> docLinesPaid;
 private List<DocLineApRec> docLinesOutStanding;
 private List<DocTypeRec> docTypeList;
 private double payRunDocLinePreEdit;
 private List<ApArPayRunDocLine> payRunDocLines;
 private List<ApArPayRunDocLine> payRunAcntDocLines;
 private List<ApArPayRunDocLine> payRunAcntDocLinesSel;
 private ApArPayRunDocLine payRunAcntDocLineSel;
 private List<PayRunSumAct> payRunSumList;
 private List<PayRunSumAct> payRunSumSelected;
 private List<DocBankLineChqRec> chqList;
 private List<DocBankLineBacsRec> bacsList;
 private PayRunSumAct payRunSumSelection;
 private DocFiRec fiDocument;
 private double amountAllocated;
 private double amountPaid;
 private double amountUnallocated;
 private double amountPreEdit;
 private double acntTotalAmountOs;
 private double acntTotalAmountPd;
 private String payRunRef;
 private double runTotalAmountOs;
 private double runTotalAmountPd;
 
 
 private ApLineSel selOpt; 
 private String fiscPeriodStr;
 private BnkPaymentRunRec paymentRun;
 private List<PaymentTypeRec> paymentTypes;
 private PaymentTypeRec paymentType;
 private List<PaymentTypeRec> paymentTypesSelected;
 
 private List<DocBankLineBaseRec> bankLines;
 private List<DocBankLineChqRec> bankLinescheque;
 private List<DocBankLineBacsRec> bankLinesBacs;
 

 /**
  * Creates a new instance of apPayment
  */
 public ApPaymentBean() {
 }
 
 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "apPaymentBeanView {0}", getViewSimple());
  if(StringUtils.equals(getViewSimple(), "apPayRun")){
   paymentRun = new BnkPaymentRunRec();
   selOpt = new ApLineSel();
  }
  fiDocument = new DocFiRec();
  if(getCompList() == null || getCompList().isEmpty()){
   MessageUtil.addWarnMessage("compsNone", "errorText");
   return;
  }
  fiDocument.setCompany(getCompList().get(0));
  
  fiDocument.setDocType(getDocTypeList().get(0));
  
  docLineAp = new DocLineApRec();
  paymentTypes = sysBuff.getPaymentTypes(fiDocument.getCompany(),"AP",false);
  if(paymentTypes == null || paymentTypes.isEmpty()){
   MessageUtil.addClientWarnMessage("errMsg", "payRunPayTy", "va;idationText");
   return;
  }
  LOGGER.log(INFO, "paymentTypes found for company {0}", paymentTypes);
  paymentType = paymentTypes.get(0);
  LineTypeRuleRec apLineTy = sysBuff.getLineTypeRuleByCode("AP");
  docLineAp.setLineType(apLineTy);
  PostTypeRec postTy = sysBuff.getPostTypeForCode("apPymnt");
  LOGGER.log(INFO, "apPymnt found {0}", postTy);
  
  docLineAp.setPostType(postTy);
 }
 
 public ApArPayRunDocLine buildPayRunDocLine(DocLineApRec docLine){
  LOGGER.log(INFO, "buildPayRunDocLine called with doclIne id {0}", docLine.getId());
  LOGGER.log(INFO, "doc line fiDoc {0}",docLine.getDocFi());
  
  
   ApArPayRunDocLine ret = new ApArPayRunDocLine();
   
   ret.setId(docLine.getId());
   ret.setDocNumber(docLine.getDocNumber());
   ret.setLineNumber(docLine.getLineNum());
   ret.setDocAmount(docLine.getDocAmount());
   if(docLine.getDocFi() != null){
    ret.setDocType(docLine.getDocFi().getDocType());
   }
   ret.setLineText(docLine.getLineText());
   ret.setPostType(docLine.getPostType());
   return ret;
  
 }
 
 private void buildPaymentLists(List<PayRunSumAct> payRunSumList){
  LOGGER.log(INFO, "buildPaymentLists called with {0}", payRunSumList);
  LOGGER.log(INFO, "docLinesOutStanding {0}", docLinesOutStanding);
  if(docLinesOutStanding == null){
   return;
  }
  chqList = new ArrayList<>();
  bacsList = new ArrayList<>();
  List<ApArPayRunDocLine> docsOs ;  
  for(PayRunSumAct acnt:payRunSumList){
   if(acnt.getPayment() > 0){
    LOGGER.log(INFO, "Process account {0} payment {1}", new Object[]{acnt.getAccountRef(),acnt.getPayment()});
    docsOs = new ArrayList<>();
    // need to get the outstanding lines for this vendor
    List<CompanyBasicRec> payComps = new ArrayList<>();
    payComps.add(fiDocument.getCompany());
    List<ApAccountRec> apAcnts = apMgr.getApAccountByCodeCrossComp(acnt.getAccountRef(), payComps);
    if(apAcnts == null){
     LOGGER.log(INFO, "Could not find account {0}", acnt.getAccountRef());
     return ;
    }
    ApAccountRec apAcnt = apAcnts.get(0);
    for(DocLineApRec l:docLinesOutStanding){
     LOGGER.log(INFO, "Doc number {0} id {1}", new Object[]{l.getDocNumber(),l.getId()});
     LOGGER.log(INFO, "Paymwent type {0}", l.getPayType());
    }
    ListIterator<DocLineApRec> lnLi = docLinesOutStanding.listIterator();
    while(lnLi.hasNext()){
     DocLineApRec currLn = lnLi.next();
     if(currLn.getPayType() == null){
      currLn.setPayType(apAcnt.getPaymentType());
      lnLi.set(currLn);
     }
     LOGGER.log(INFO, "Paymwent type {0}", currLn.getPayType());
    }
    Collections.sort(docLinesOutStanding, new DocLineApByAcntPayLevelMedium());
    ListIterator<DocLineApRec> li = docLinesOutStanding.listIterator();
    boolean balCompl = false;
    DocLineApRec apLn;
    while(li.hasNext() && !balCompl){
     apLn = li.next();
     
     if(StringUtils.equals(apLn.getAccountRef(), acnt.getAccountRef()) ){
      ApArPayRunDocLine payDocLn = new ApArPayRunDocLine();
      payDocLn.setApAcnt(apLn.getApAccount());
      payDocLn.setAccountId(apLn.getApAccount().getId());
      payDocLn.setAccountRef(apLn.getAccountRef());
      payDocLn.setDocAmount(apLn.getDocAmount());
      payDocLn.setDocNumber(apLn.getDocNumber());
      DocTypeRec dt = sysBuff.getDocTypeByCode(apLn.getDocTypeName());
      LOGGER.log(INFO, "Doc type {0} for name {1}", new Object[]{dt,apLn.getDocTypeName()});
      payDocLn.setDocType(dt);
      payDocLn.setDueDate(apLn.getDueDate());
      payDocLn.setId(apLn.getId());
      payDocLn.setLineNumber(apLn.getLineNum());
      payDocLn.setLineText(apLn.getLineText());
      payDocLn.setPayType(apLn.getPayType());
      payDocLn.setPostType(apLn.getPostType());
      docsOs.add(payDocLn);
     }
    }
    if(docsOs.isEmpty()){
     continue;
    }
    apAcnt = docsOs.get(0).getApAcnt();
    int summLvl = docsOs.get(0).getPayType().getSummLevel();
    if(summLvl == PaymentTypeRec.PAY_SUMM_DOC){
     // payment line per document
     ListIterator<ApArPayRunDocLine> osAcLi = docsOs.listIterator();
     while(osAcLi.hasNext()){
      ApArPayRunDocLine ln = osAcLi.next();
      if(ln.getPayType().getSummLevel() != PaymentTypeRec.PAY_SUMM_DOC){
       continue;
      }
      if(StringUtils.equals(ln.getPayType().getPayMedium(), PaymentTypeRec.PAY_MED_CHQ)){
       DocBankLineChqRec chqLn = new DocBankLineChqRec();
       chqLn.setAmount(ln.getDocAmount());
       chqLn.setApAccount(ln.getApAcnt());
       chqLn.setBnkRef("12345678");
       chqLn.setComp(fiDocument.getCompany());
       chqLn.setIssueDate(new Date());
       chqLn.setPayee(chqLn.getApAccount().getApAccountFor());
       chqLn.setReceipt(false);
      }
     }
    }
    
    
   }
  }
 
 }
 
 public double onAccountOS(String ref){
  LOGGER.log(INFO, "getAccountOS {0}", ref);
  return 1.0;
 }

 public double getAcntTotalAmountOs() {
  return acntTotalAmountOs;
 }

 public void setAcntTotalAmountOs(double acntTotalAmountOs) {
  this.acntTotalAmountOs = acntTotalAmountOs;
 }

 public double getAcntTotalAmountPd() {
  return acntTotalAmountPd;
 }

 public void setAcntTotalAmountPd(double acntTotalAmountPd) {
  this.acntTotalAmountPd = acntTotalAmountPd;
 }

 
 public double getRunTotalAmountOs() {
  return runTotalAmountOs;
 }

 public void setRunTotalAmountOs(double runTotalAmountOs) {
  this.runTotalAmountOs = runTotalAmountOs;
 }

 public double getRunTotalAmountPd() {
  return runTotalAmountPd;
 }

 public void setRunTotalAmountPd(double runTotalAmountPd) {
  this.runTotalAmountPd = runTotalAmountPd;
 }

 
 public double getAmountAllocated() {
  return amountAllocated;
 }

 public void setAmountAllocated(double amountAllocated) {
  this.amountAllocated = amountAllocated;
 }

 public double getAmountPaid() {
  return amountPaid;
 }

 public void setAmountPaid(double amountPaid) {
  this.amountPaid = amountPaid;
 }

 public double getAmountPreEdit() {
  return amountPreEdit;
 }

 public void setAmountPreEdit(double amountPreEdit) {
  this.amountPreEdit = amountPreEdit;
 }

 
 public double getAmountUnallocated() {
  return amountUnallocated;
 }

 public void setAmountUnallocated(double amountUnallocated) {
  this.amountUnallocated = amountUnallocated;
 }

 
 private DocLineGlRec getBankLine(double paidAmnt,FundBalance fndBal, UserRec crBy, Date crDate, 
   LineTypeRuleRec lnTy ){
  LOGGER.log(INFO, "getBankLine called with pay {0} fndProp  {2}", new Object[]{paidAmnt,fndBal.getPercentage()});
  paidAmnt *= fndBal.getPercentage();
  LOGGER.log(INFO, "Amount for this line {0}", paidAmnt);
  DocLineGlRec bnkLine = new DocLineGlRec();
  
  bnkLine.setCreateBy(crBy);
  bnkLine.setCreateDate(crDate);
  bnkLine.setComp(fiDocument.getCompany());
  bnkLine.setLineType(lnTy);
  PostTypeRec postTy = sysBuff.getPostTypeForCode("glCr");
  bnkLine.setPostType(postTy);
  bnkLine.setDocAmount(paidAmnt);
  bnkLine.setHomeAmount(paidAmnt);
  FiGlAccountCompRec bankAc = docLineAp.getPayType().getGlBankAccount();
  
  if(docLineAp.getPayType().getGlBankAccount() == null){
    // no bank account so get from DB
   PaymentTypeRec bnkPt = sysBuff.getPostTypeBnkGlAcnt(docLineAp.getPayType());
   bankAc = bnkPt.getGlBankAccount();
  }
  LOGGER.log(INFO, "bankAc {0}", bankAc);
  if(bankAc == null){
   MessageUtil.addErrorMessageParam1("payTyBnkAc","errorText",  docLineAp.getPayType().getDescription());
   return null;
  }
  
  LOGGER.log(INFO, "bank GL  {0}", bankAc.getCoaAccount().getRef());
  bnkLine.setGlAccount(bankAc);
  bnkLine.setLineText(fiDocument.getPartnerRef());
  bnkLine.setDocFi(fiDocument);
  return bnkLine;
 }

 public List<DocBankLineChqRec> getChqList() {
  return chqList;
 }

 public void setChqList(List<DocBankLineChqRec> chqList) {
  this.chqList = chqList;
 }
 
 
 public DocLineApRec getDocLineAp() {
  return docLineAp;
 }

 public List<DocBankLineBacsRec> getBacsList() {
  return bacsList;
 }

 public void setBacsList(List<DocBankLineBacsRec> bacsList) {
  this.bacsList = bacsList;
 }

 
 public List<DocBankLineBaseRec> getBankLines() {
  return bankLines;
 }

 public void setBankLines(List<DocBankLineBaseRec> bankLines) {
  this.bankLines = bankLines;
 }

 
 public List<DocBankLineBacsRec> getBankLinesBacs() {
  return bankLinesBacs;
 }

 public void setBankLinesBacs(List<DocBankLineBacsRec> bankLinesBacs) {
  this.bankLinesBacs = bankLinesBacs;
 } 
 
 public List<DocBankLineChqRec> getBankLinescheque() {
  return bankLinescheque;
 }

 public void setBankLinescheque(List<DocBankLineChqRec> bankLinescheque) {
  this.bankLinescheque = bankLinescheque;
 }

 

 public void setDocLineAp(DocLineApRec docLineAp) {
  this.docLineAp = docLineAp;
 }

 public List<DocLineApRec> getDocLinesOutStanding() {
  return docLinesOutStanding;
 }

 public void setDocLinesOutStanding(List<DocLineApRec> docLinesOutStanding) {
  this.docLinesOutStanding = docLinesOutStanding;
 }

 public List<DocLineApRec> getDocLinesPaid() {
  return docLinesPaid;
 }

 public void setDocLinesPaid(List<DocLineApRec> docLinesPaid) {
  this.docLinesPaid = docLinesPaid;
 }

 
 
 public List<DocTypeRec> getDocTypeList() {
  if(docTypeList == null){
   docTypeList = sysBuff.getDocTypesForLedgerCode("AP");
  }
  return docTypeList;
 }

 public void setDocTypeList(List<DocTypeRec> docTypeList) {
  this.docTypeList = docTypeList;
 }

 
 public DocFiRec getFiDocument() {
  return fiDocument;
 }

 public void setFiDocument(DocFiRec fiDocument) {
  this.fiDocument = fiDocument;
 }

 public String getFiscPeriodStr() {
  return fiscPeriodStr;
 }

 public void setFiscPeriodStr(String fiscPeriodStr) {
  this.fiscPeriodStr = fiscPeriodStr;
 }

 public List<FundBalance> getFundSplit(DocLineApRec paidLine){
  LOGGER.log(INFO,"getFundSplit called with line id {0}",paidLine.getId());
  List<FundBalance> fndSplit = new ArrayList<>();
  
  
   List<DocLineGlRec> lnReconLns = paidLine.getReconiliationLines();
   LOGGER.log(INFO, "Ln line id {0}  reconciliation lines {1}", new Object[]{paidLine.getId(),paidLine.getReconiliationLines()});
   if(lnReconLns == null){
    paidLine = this.docMgr.getDocLineApReconLines(paidLine);
    LOGGER.log(INFO, "after call doc Mgr Ln line id {0}  reconciliation lines {1}", 
      new Object[]{paidLine.getId(),paidLine.getReconiliationLines()});
    lnReconLns = paidLine.getReconiliationLines();
    for(DocLineGlRec currRecon:lnReconLns){
     if(fndSplit.isEmpty()){
      FundBalance currFndBal = new FundBalance();
      currFndBal.setFund(currRecon.getRestrictedFund());
      currFndBal.setAmount(currRecon.getDocAmount());
      fndSplit.add(currFndBal);
     }else{
      // see if there is a fund 
      boolean foundFnd = false;
      ListIterator<FundBalance> fndLi = fndSplit.listIterator();
      while(fndLi.hasNext() && !foundFnd){
       FundBalance currFnd = fndLi.next();
       if(currFnd.getFund() == null && currRecon.getRestrictedFund() == null){
        foundFnd = true;
       }else if(currFnd.getFund() != null && currRecon.getRestrictedFund() != null &&
         Objects.equals(currFnd.getFund().getId(), currRecon.getRestrictedFund().getId())){
        foundFnd = true;
       }
       if(foundFnd){
       double currBal = currFnd.getAmount();
       if(currRecon.getPostType().isDebit()){
        currBal += currRecon.getDocAmount();
       }else{
        currBal -= currRecon.getDocAmount();
       }
       currFnd.setAmount(currBal);
       fndLi.set(currFnd);
      }else{
       FundBalance currFndBal = new FundBalance();
       currFndBal.setFund(currRecon.getRestrictedFund());
       currFndBal.setAmount(currRecon.getDocAmount());
       fndSplit.add(currFndBal);
       }
      }
      
     }
    
    }
   double totalAmount = 0;
   for(FundBalance currFndBal:fndSplit){
    totalAmount += currFndBal.getAmount();
   }
   ListIterator<FundBalance> fndLi = fndSplit.listIterator();
   while(fndLi.hasNext()){
    FundBalance currFndBal = fndLi.next();
    double prop =  currFndBal.getAmount() / totalAmount ;
    currFndBal.setPercentage(prop);
    fndLi.set(currFndBal);
   }
   }
  
  return fndSplit;
 }

 public BnkPaymentRunRec getPaymentRun() {
  LOGGER.log(INFO, "getPaymentRun {0}", paymentRun);
  if(paymentRun == null){
   paymentRun = new BnkPaymentRunRec(); 
  }
  return paymentRun;
 }

 public void setPaymentRun(BnkPaymentRunRec paymentRun) {
  this.paymentRun = paymentRun;
 }

 public double getPayRunDocLinePreEdit() {
  return payRunDocLinePreEdit;
 }

 public void setPayRunDocLinePreEdit(double payRunDocLinePreEdit) {
  this.payRunDocLinePreEdit = payRunDocLinePreEdit;
 }

 public List<ApArPayRunDocLine> getPayRunDocLines() {
  return payRunDocLines;
 }

 public void setPayRunDocLines(List<ApArPayRunDocLine> payRunDocLines) {
  this.payRunDocLines = payRunDocLines;
 }

 
 public List<ApArPayRunDocLine> getPayRunAcntDocLines() {
  return payRunAcntDocLines;
 }

 public void setPayRunAcntDocLines(List<ApArPayRunDocLine> payRunAcntDocLines) {
  this.payRunAcntDocLines = payRunAcntDocLines;
 }

 public ApArPayRunDocLine getPayRunAcntDocLineSel() {
  return payRunAcntDocLineSel;
 }

 public void setPayRunAcntDocLineSel(ApArPayRunDocLine payRunAcntDocLineSel) {
  this.payRunAcntDocLineSel = payRunAcntDocLineSel;
 }

 
 public List<ApArPayRunDocLine> getPayRunAcntDocLinesSel() {
  return payRunAcntDocLinesSel;
 }

 public void setPayRunAcntDocLinesSel(List<ApArPayRunDocLine> payRunAcntDocLinesSel) {
  this.payRunAcntDocLinesSel = payRunAcntDocLinesSel;
 }

 
 public String getPayRunRef() {
  return payRunRef;
 }

 public void setPayRunRef(String payRunRef) {
  this.payRunRef = payRunRef;
 }

 public List<PayRunSumAct> getPayRunSumList() {
  return payRunSumList;
 }

 public void setPayRunSumList(List<PayRunSumAct> payRunSumList) {
  this.payRunSumList = payRunSumList;
 }

 public List<PayRunSumAct> getPayRunSumSelected() {
  return payRunSumSelected;
 }

 public void setPayRunSumSelected(List<PayRunSumAct> payRunSumSelected) {
  this.payRunSumSelected = payRunSumSelected;
 }

 public PayRunSumAct getPayRunSumSelection() {
  return payRunSumSelection;
 }

 public void setPayRunSumSelection(PayRunSumAct payRunSumSelection) {
  this.payRunSumSelection = payRunSumSelection;
 }

 public PaymentTypeRec getPaymentType() {
  return paymentType;
 }

 public void setPaymentType(PaymentTypeRec paymentType) {
  this.paymentType = paymentType;
 }
 
 
 
 
 public List<PaymentTypeRec> getPaymentTypes() {
  
  return paymentTypes;
 }

 
 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public List<PaymentTypeRec> getPaymentTypesSelected() {
  return paymentTypesSelected;
 }

 public void setPaymentTypesSelected(List<PaymentTypeRec> paymentTypesSelected) {
  this.paymentTypesSelected = paymentTypesSelected;
 }
 
 
 
 private DocLineGlRec getPaymentReconLine(DocLineApRec docLn, FundBalance fndBal ){
  LOGGER.log(INFO, "getPaymentReconLine called with line {0} fund {1} fund amount {2}", 
    new Object[]{docLn.getId(), fndBal.getFund(),fndBal.getAmount()});
  DocLineGlRec recLn = docMgr.getReconcilLine(docLn, fndBal.getAmount(), fndBal.getFund());
  
    
  
  return recLn;
 }

 public ApLineSel getSelOpt() {
  return selOpt;
 }

 public void setSelOpt(ApLineSel selOpt) {
  this.selOpt = selOpt;
 }

 
 
 
 public List<ApAccountRec> onApAccountComplete(String input){
  LOGGER.log(INFO, "onApAccountComplete  called with {0}", input);
  List<ApAccountRec> retList;
  if(StringUtils.isBlank(input)){
   retList = apMgr.getApAccountsAll(fiDocument.getCompany());
   LOGGER.log(INFO, "All AP accounts {0}", retList);
  }else{
   retList = apMgr.getApAccountsStartinfWithCode(fiDocument.getCompany(), input);
   LOGGER.log(INFO, "AP accounts {0}", retList);
  }
  
  return retList;
 }
 
 public void onApAccountSelect(SelectEvent evt){
  LOGGER.log(INFO, "onApAccountSelect called");
  ApAccountRec acnt = (ApAccountRec)evt.getObject();
  docLineAp.setApAccount(acnt);
  docLinesOutStanding = docMgr.getDocLinesApOpenForAcnt(acnt);
  ListIterator<DocLineApRec> li = docLinesOutStanding.listIterator();
  while(li.hasNext()){
   DocLineApRec curr = li.next();
   
   if(curr.getPostType().isDebit()){
    double pstVal = curr.getDocAmount();
    pstVal *= -1;
    curr.setDocAmount(pstVal);
   }
   curr.setUnpaidAmount(curr.getDocAmount());
   li.set(curr);
  }
  
 }
 
 public void onCompanySelect(SelectEvent evt){
  
  CompanyBasicRec compSel = (CompanyBasicRec)evt.getObject();
  if(compSel == null){
   MessageUtil.addWarnMessageWithoutKey("No Company", "No Company selected. Please a company");
   return;
  }
  fiDocument.setCompany(compSel);
  
 }
 
 
 
 public void onDocDateSelect(SelectEvent evt){
  LOGGER.log(INFO, "onPostDateSelect called {0}", evt.getObject());
  fiDocument.setDocumentDate((Date)evt.getObject());
  if(fiDocument.getPostingDate() == null){
   fiDocument.setPostingDate(fiDocument.getDocumentDate());
   List<String> upDates = new ArrayList<>();
   FiscalPeriodYearRec fisPerYear = sysBuff.getCompFiscalPeriodYearForDate(fiDocument.getCompany(), fiDocument.getDocumentDate());
   fiscPeriodStr = String.valueOf(fisPerYear.getYear())+ " / "+String.valueOf(fisPerYear.getPeriod());
   LOGGER.log(INFO, "post date fisPerYearStr {0}", fiscPeriodStr);
  
   fiDocument.setFisPeriod(fisPerYear.getPeriod());
   fiDocument.setFisYear(fisPerYear.getYear());
   upDates.add("apPaySinglFrm:postDate");
   upDates.add("apPaySinglFrm:period");
   RequestContext.getCurrentInstance().update(upDates);
   
  }
 }

 public DocLineApRec getDocLineSelected() {
  return docLineSelected;
 }

 public void setDocLineSelected(DocLineApRec docLineSelected) {
  this.docLineSelected = docLineSelected;
 }

 public List<DocLineApRec> getDocLinesCheckBSel() {
  return docLinesCheckBSel;
 }

 public void setDocLinesCheckBSel(List<DocLineApRec> docLinesCheckBSel) {
  this.docLinesCheckBSel = docLinesCheckBSel;
 }

 
 public List<DocLineApRec> getDocLinesSelected() {
  return docLinesSelected;
 }

 public void setDocLinesSelected(List<DocLineApRec> docLinesSelected) {
  this.docLinesSelected = docLinesSelected;
 }
 
 
 
 public void onDocPayAmnt(){
  LOGGER.log(INFO, "onDocPayAmnt called");
  amountUnallocated = amountPaid - amountAllocated;
  docLineAp.setDocAmount(amountPaid);
  RequestContext.getCurrentInstance().update("apPaySinglFrm:unallocated");
 }
 
 public List<DocTypeRec> onDocTypeComplete(String input){
  
  if(docTypeList == null || docTypeList.isEmpty()){
   return null;
  }
  if(StringUtils.isBlank(input)){
   return docTypeList;
  }else{
   List<DocTypeRec> retList = new ArrayList<>();
   for(DocTypeRec dt: docTypeList){
    if(StringUtils.startsWith(dt.getName(), input)){
     retList.add(dt);
    }
   }
   if(retList.isEmpty()){
    return null;
   }else{
    return retList;
   }
  }
 }
 
 public void onPayEditPre(RowEditEvent evt){
  DocLineApRec line = (DocLineApRec)evt.getObject();
  amountPreEdit = line.getPaidAmount();
  LOGGER.log(INFO, "Pre editr pay amnt {0}", amountPreEdit);
  
 }
 
 public void onPayCellEdit(CellEditEvent evt){
  LOGGER.log(INFO, "onPayCellEdit called index {0} key {1} value {2}", new Object[]{
   evt.getRowIndex(), evt.getRowKey(), evt.getNewValue()
  });
  DocLineApRec apLine = docLinesOutStanding.get(0);
  apLine.setPaidAmount(9999);
  docLinesOutStanding.set(0, apLine);
  RequestContext.getCurrentInstance().update("apPaySinglFrm:dataListOP");
 }
 
 
 public void onPayEdit(RowEditEvent evt) {
  DocLineApRec line = (DocLineApRec)evt.getObject();
  LOGGER.log(INFO, "onPayEdit called {0} {1}", new Object[]{line.getId()});
  amountAllocated += line.getPaidAmount();
  LOGGER.log(INFO,"amountAllocated is now {0}",amountAllocated);
  ListIterator<DocLineApRec> li = docLinesOutStanding.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   DocLineApRec curr = li.next();
   if(Objects.equals(curr.getId(), line.getId())){
    amountUnallocated += amountPreEdit;
    amountUnallocated -= line.getPaidAmount();
    
    double lnUnalloc = line.getDocAmount() - line.getPaidAmount();
    line.setUnpaidAmount(lnUnalloc);
    li.set(line);
    
   }
  }
  
  if(docLinesPaid == null){
   docLinesPaid = new ArrayList<>();
  }
  //update paid lines
  boolean foundLn = false;
  LOGGER.log(INFO, "line.getPaidAmount {0}", line.getPaidAmount());
  if(line.getPaidAmount() == 0){
   if(!docLinesPaid.isEmpty()){
    // remove from paid lines if found
    ListIterator<DocLineApRec> pdLi = docLinesPaid.listIterator();
    while(pdLi.hasNext() && !foundLn){
     DocLineApRec currLine = pdLi.next();
     LOGGER.log(INFO, "currLine id {0} line id {1}", new Object[]{currLine.getId(), line.getId()});
     if(Objects.equals(currLine.getId(), line.getId())){
      pdLi.remove();
      MessageUtil.addInfoMessageWithoutKey("Removed from paid list", "Removed line id "+line.getId());
      foundLn = true;
     }
    }
   }
  }else {
   if(docLinesPaid.isEmpty()){
    docLinesPaid.add(line);
    MessageUtil.addInfoMessageWithoutKey("Added to paid list", "Added line id "+line.getId());
   }else{
    ListIterator<DocLineApRec> pdLi = docLinesPaid.listIterator();
    while(pdLi.hasNext() && !foundLn){
     DocLineApRec currLine = pdLi.next();
     if(Objects.equals(currLine.getId(), line.getId())){
      pdLi.set(line);
      MessageUtil.addInfoMessageWithoutKey("updated paid list", "changed line id "+line.getId());
      foundLn = true;
     }
    }
    if(!foundLn){
      pdLi.add(line);
      MessageUtil.addInfoMessageWithoutKey("Added to paid list", "Added line id "+line.getId());
    }
   }
  }
 
  List<String> updt = new ArrayList<>();
  updt.add("apPaySinglFrm:unallocated");
  updt.add("apPaySinglFrm:osInvs");
  RequestContext.getCurrentInstance().update(updt);
  
  
 
 }
 
 public StreamedContent onPayPdfDownload(){
  StreamedContent pdfFile;
  //FacesContext fc = FacesContext.getCurrentInstance();
  //InputStream is = fc.getExternalContext().getResourceAsStream("/resources/pdfTemplate/CreditNoteVatComp.pdf");
  InputStream fileData = payMediumMgr.getChequePdfTemplate(fiDocument, docLineAp.getPayType());
  String chqFileName = this.formTextApForKey("chqPdf_FN");
  pdfFile = new DefaultStreamedContent(fileData, "application/pdf",chqFileName);
  LOGGER.log(INFO, "onPayPdfDownload file {0}", pdfFile);
  
  return pdfFile;
 }
 
 public void onPayRunAcntInvDlg(){
  LOGGER.log(INFO,"onPayRunAcntInvDlg payrunSum {0}",payRunSumSelection);
  LOGGER.log(INFO, "payRunSumSelection open {0} paid {1}", 
    new Object[]{payRunSumSelection.getOpen(),payRunSumSelection.getPayment()});
  if(payRunSumSelection == null){
   return;
  }
  boolean allPaid = false;
  if(payRunSumSelection.getOpen() == payRunSumSelection.getPayment()){
   allPaid = true;
  }
  
  payRunAcntDocLines = new ArrayList<>();
  acntTotalAmountOs = 0;
  acntTotalAmountPd = 0;
  for(DocLineApRec ln:docLinesOutStanding){
   if(StringUtils.equals(ln.getAccountRef(), payRunSumSelection.getAccountRef())){
    ln.setPaidAmount(ln.getDocAmount());
    ApArPayRunDocLine docLn = buildPayRunDocLine(ln);
    if(allPaid){
     docLn.setPartPayAmount(docLn.getDocAmount());
    }
    payRunAcntDocLines.add(docLn);
    if(ln.getPostType().isDebit()){
     acntTotalAmountOs += ln.getDocAmount();
     if(allPaid){
      acntTotalAmountPd += ln.getDocAmount();
     }else {
      acntTotalAmountPd = 0;
    }
     
    }else{
     acntTotalAmountOs -= ln.getDocAmount();
     if(allPaid){
      acntTotalAmountPd -= ln.getDocAmount();
     }else{
      acntTotalAmountPd = 0;
     }
     
    }
    
   }
  }
  if(!payRunAcntDocLines.isEmpty()){
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("acntLinesOsFrm");
   rCtx.execute("PF('acntLinesOsWv').show();");
  }
 }
 
 public void onPayRunBackBtn(){
  LOGGER.log(INFO, "onPayRunBackBtn called step{0} ", getStep());
  int oldStep = getStep();
  int currStep = oldStep;
  currStep--;
  setStep(currStep);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("apPayRunFrm");
  
  switch (currStep) {
   case 0:
    updates.add("apPayRunFrm:selOpt");
    updates.add("apPayRunFrm:sumTbl");
    break;
   case 1:
    updates.add("apPayRunFrm:sumTbl");
    updates.add("apPayRunFrm:sumTbl");
    
    break;
   case 2:
    break;
   default:
    break;
  }
  rCtx.update(updates);
 } 
 
 public void onPayRunNextBtn(){
  LOGGER.log(INFO, "onPayRunNextBtn called step {0} ", getStep());
  int oldStep = getStep();
  int currStep = oldStep;
  currStep++;
  setStep(currStep);
  LOGGER.log(INFO, "getStep is now {0} curr {1}", new Object[]{getStep(),currStep});
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("apPayRunFrm");
  updates.add("apPayRunFrm:errMsg");
  updates.add("apPayRunFrm:cntrlBtns");
  switch (currStep) {
   
   case 1:
    updates.add("apPayRunFrm:selOpt");
    updates.add("apPayRunFrm:sumTbl");
    if(oldStep == 0){
     onPayRunOsDocs();
    }
    break;
   case 2:
    // now analyse into payment list
    if(payRunSumList == null || payRunSumList.isEmpty()){
     MessageUtil.addClientWarnMessage("apPayRunFrm:errMsg", "payRunDocsNone", "validationText");
     updates.add("apPayRunFrm:errMsg");
     currStep--;
     
    }else{
     buildPaymentLists(payRunSumList);
     updates.add("apPayRunFrm:sumTbl");
    }
    
    break;
   default:
    break;
  }
  
  rCtx.update(updates);
 }
 
 public void onPayRunInvRowEdit(RowEditEvent evt){
  LOGGER.log(INFO, "onPayRunInvRowEdit called with {0}", evt.getObject().getClass().getSimpleName());
  ApArPayRunDocLine currLn = (ApArPayRunDocLine)evt.getObject();
 
  LOGGER.log(INFO, "currLn pay {0} payRunDocLinePreEdit paym {1}", 
    new Object[]{currLn.getPartPayAmount(),payRunDocLinePreEdit});
  if(currLn.getPartPayAmount() == 0 && payRunDocLinePreEdit == 0 ){
   return;
  }
  
  double changedAmnt = currLn.getPartPayAmount() - payRunDocLinePreEdit;
  acntTotalAmountPd += changedAmnt;
  LOGGER.log(INFO, "acntTotalAmountPd {0}", acntTotalAmountPd);
  
 }
 
 public void onPayRunInvRowEditInit(RowEditEvent evt){
  LOGGER.log(INFO, "onPayRunInvRowEditInit called with {0}", evt.getObject().getClass().getSimpleName());
  payRunDocLinePreEdit = ((ApArPayRunDocLine)evt.getObject()).getPartPayAmount();
  payRunAcntDocLineSel = (ApArPayRunDocLine)evt.getObject();
  LOGGER.log(INFO, "payRunDocLinePreEdit pay {0} ", payRunDocLinePreEdit);
  LOGGER.log(INFO, "payRunDocLinePreEdit pay {0} ",payRunAcntDocLineSel);
  
 } 
 
 
 public void onPayRunInvPayableEdit(CellEditEvent evt){
  LOGGER.log(INFO,"onPayRunInvPayableEdit called with object {0}",evt.getNewValue() );
  LOGGER.log(INFO,"row index {0} key {1}",new Object[]{evt.getRowIndex(),evt.getRowKey()} );
  
  double oldVal;
  double newVal;
  if(evt.getOldValue().getClass().getSimpleName().equals("Double")){
   oldVal =(Double)evt.getOldValue();
  }else{
   oldVal =(Integer)evt.getOldValue();
  }
  if(evt.getNewValue().getClass().getSimpleName().equals("Double")){
   newVal =(Double)evt.getNewValue();
  }else{
   newVal =(Integer)evt.getNewValue();
  }
  
  double change = newVal - oldVal;
  DocLineApRec docLine = this.docLinesSelected.get(evt.getRowIndex());
  ListIterator<PayRunSumAct> li = payRunSumList.listIterator();
  boolean found = false;
  while(li.hasNext() &&!found){
   PayRunSumAct curr = li.next();
   if(StringUtils.equals(curr.getAccountRef(), docLine.getAccountRef())){
    double pymnt = curr.getPayment();
    pymnt += change;
    curr.setPayment(pymnt);
    
   }
   
  }
  
  
 }
 
 public void onPayRunInvTransf(){
  LOGGER.log(INFO, "onPayRunInvTransf called num acnt lines {0}",payRunAcntDocLines.size());
  double bal = 0;
  boolean updateRequired = false;
  for(ApArPayRunDocLine acntLn:payRunAcntDocLines){
   ListIterator<ApArPayRunDocLine> li = payRunDocLines.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    ApArPayRunDocLine payRunLn = li.next();
    if(Objects.equals(payRunLn.getDocNumber(), acntLn.getDocNumber()) && payRunLn.getLineNumber() == acntLn.getLineNumber()){
     payRunLn.setPartPayAmount(acntLn.getPartPayAmount());
     bal += acntLn.getPartPayAmount();
     li.set(payRunLn);
     found = true;
     updateRequired = true;
    }
   }
  }
  if(updateRequired){
   payRunSumSelection.setPayment(bal);
   runTotalAmountPd += bal;
   LOGGER.log(INFO, "end of trf pd total {0}", runTotalAmountPd);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("apPayRunFrm:sumTbl");
   rCtx.execute("PF('acntLinesOsWv').hide();");
  }
 }
 
 
 
 public void onPayRunRowCbSel(SelectEvent evt){
  LOGGER.log(INFO,"onPayRunRowCbSel called with object ref {0}",((PayRunSumAct)evt.getObject()).getAccountRef());
  LOGGER.log(INFO, "payRunSumSelected {0}", payRunSumSelected);
  PayRunSumAct currSum = (PayRunSumAct)evt.getObject();
  
  
  currSum.setPayment(currSum.getOpen());
  
  ListIterator<PayRunSumAct> li = payRunSumList.listIterator();
  LOGGER.log(INFO, "payRunSumList size {0}", payRunSumList.size());
  boolean found = false;
  while(li.hasNext() && !found ){
   PayRunSumAct s = li.next();
   LOGGER.log(INFO, "s.id {0} currSum.id {1}", new Object[]{s.getAccountRef(),currSum.getAccountRef()});
   if(StringUtils.equals(s.getAccountRef(), currSum.getAccountRef())){
    li.set(currSum);
    found = true;
   }
  }
  if(found){
   RequestContext.getCurrentInstance().update("apPayRunFrm");
   LOGGER.log(INFO, "Update table");
  }
 }
 
 public void onPayRunRowCbUnSel(UnselectEvent evt){
  LOGGER.log(INFO,"onPayRunRowCb unSel called with object {0}",evt.getObject()
  );
 }
 
 public void onPayRunRowCbAll(ToggleSelectEvent evt){
  LOGGER.log(INFO,"onPayRunRowCbAll header Cb called called selected {0}",evt.isSelected() );
 }
 
 public void onPayRunMenuSelect(SelectEvent evt){
  LOGGER.log(INFO,"onPayRunMenuSelect called with object {0}",evt.getObject() );
  payRunSumSelection = (PayRunSumAct)evt.getObject();
  
 }
 
 public void onPayRunRowToggle(ToggleEvent evt){
  LOGGER.log(INFO,"expand/unexpand  selected {0} visible {1}",
    new Object[]{evt.getData(),evt.getVisibility()} );
  
  if(evt.getVisibility() == Visibility.VISIBLE){
   String apRef = ((PayRunSumAct)evt.getData()).getAccountRef();
   docLinesSelected = new ArrayList<>();
   for(DocLineApRec ln:this.docLinesOutStanding){
    if(StringUtils.equals(apRef, ln.getAccountRef())){
     docLinesSelected.add(ln);
    }
   }
   
  }
 }
 
 public void onPayRunOsDocs(){
  LOGGER.log(INFO,"onPayRunGetOsDocs called");
  selOpt.setComp(fiDocument.getCompany());
  selOpt.setPayStatus(ApLineSel.STATUS_OS);
  
  selOpt.setPaymentTypeList(paymentTypesSelected);
  docLinesOutStanding = docMgr.getApLinesForPayRun(selOpt);
  if(docLinesOutStanding == null || docLinesOutStanding.isEmpty()){
   MessageUtil.addClientWarnMessage("errMsg", "payRunDocsNone", "validationText");
   PrimeFaces.current().ajax().update("apPayRunFrm:errMsg");
   return;
  }
  payRunSumList = new ArrayList<>();
  payRunDocLines = new ArrayList<>();
  for(DocLineApRec l:docLinesOutStanding){
   LOGGER.log(INFO, "Doc Number {0} id {1}", new Object[]{l.getDocNumber(),l.getId()});
   LOGGER.log(INFO, "Payment type {0}", l.getPayType());
  }
  LOGGER.log(INFO, "About to sort docLinesOutStanding");
  Collections.sort(docLinesOutStanding, new ApLineByAcntRef());
  
  for(DocLineApRec ln:docLinesOutStanding){
   boolean found = false;
   LOGGER.log(INFO, "ln account number {0} found {1} amount {2}", 
     new Object[]{ln.getAccountRef(),found,ln.getDocAmount()});
   ApArPayRunDocLine docLn = new ApArPayRunDocLine();
   docLn.setAccountId(ln.getApAccount().getId());
   docLn.setAccountRef(ln.getAccountRef());
   docLn.setDocAmount(ln.getDocAmount());
   docLn.setDocNumber(ln.getDocNumber());
   if(ln.getDocFi() != null){
    docLn.setDocType(ln.getDocFi().getDocType());
   }
   docLn.setPayType(ln.getPayType());
   docLn.setDueDate(ln.getDueDate());
   docLn.setLineNumber(ln.getLineNum());
   docLn.setLineText(ln.getLineText());
   docLn.setPostType(ln.getPostType());
   payRunDocLines.add(docLn);
   
   ListIterator<PayRunSumAct> li = payRunSumList.listIterator();
   while(li.hasNext() && !found){
    PayRunSumAct currSum = li.next();
    
    if(StringUtils.equals(currSum.getAccountRef(), ln.getAccountRef())){
     found = true;
     double oldAmnt = currSum.getOpen();
     if(ln.getPostType().isDebit()){
      oldAmnt -= ln.getDocAmount();
     }else{
      oldAmnt += ln.getDocAmount();
     }
     currSum.setOpen(oldAmnt);
     li.set(currSum);
    }
   }
   LOGGER.log(INFO, "After pay run llop found {0} ", found);
   if(!found){
    PayRunSumAct sum = new PayRunSumAct();
    sum.setAccountRef(ln.getAccountRef());
    sum.setPartnerName(ln.getApAccount().getAccountName());
    sum.setOpen(ln.getDocAmount());
    payRunSumList.add(sum);
   }
   
   LOGGER.log(INFO, "payRunSumList size {0}", payRunSumList.size());
  }
  runTotalAmountOs = 0;
  runTotalAmountPd = 0;
  for(PayRunSumAct s:payRunSumList){
   runTotalAmountOs += s.getOpen();
  }
  
  
  this.setStep(1);
  RequestContext.getCurrentInstance().update("apPayRunFrm");
 }
 
 public void onPayRunPartPayEdit(CellEditEvent evt){
  LOGGER.log(INFO, "onPayRunPartPayedit part pay  called with val {0}", evt.getNewValue());
  LOGGER.log(INFO, "val concrete class {0}",evt.getNewValue().getClass().getSimpleName());
  LOGGER.log(INFO,"Row index {0}",evt.getRowIndex());
 }
 
 public void onPayRunPartPayEditInit(CellEditEvent evt){
  LOGGER.log(INFO, "onPayRunPartPayEditInt new val {0}",evt.getNewValue());
  LOGGER.log(INFO, "row index {0}",evt.getRowIndex());
  payRunAcntDocLineSel = payRunAcntDocLines.get(evt.getRowIndex());
 }
 
 public void onPayRunPartPayValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO, "onPayRunPartPayValidate part pay validate called with val {0}", val);
  LOGGER.log(INFO, "part pay  class {0}", val.getClass().getSimpleName());
  LOGGER.log(INFO, "doc amnt {0}",payRunAcntDocLineSel.getDocAmount());
 double valAmnt; 
  if(StringUtils.equals(val.getClass().getSimpleName(), "Double")){
   valAmnt = (Double)val;
    
  }else{
   Long valLong = (Long)val;
   valAmnt = valLong.doubleValue();
  }
  LOGGER.log(INFO, "valAmnt {0}", valAmnt);
  List<String> updates = new ArrayList<>();
  if(valAmnt > 0){
   if(valAmnt > payRunAcntDocLineSel.getDocAmount())  {
    MessageUtil.addClientWarnMessage("acntLinesOsFrm:acntLnOsErr", "apDocPaidAmnt", "validationText");
    updates.add("acntLinesOsFrm:acntLnOsErr");
    ((EditableValueHolder) toValidate).setValid(false);
    PrimeFaces.current().ajax().update("acntLinesOsFrm:acntLnOsErr");
    
   } else if(payRunAcntDocLineSel.getDocAmount() < 0)  {
    MessageUtil.addClientWarnMessage("acntLinesOsFrm:acntLnOsErr", "apDocCrnAmnt", "validationText");
    ((EditableValueHolder) toValidate).setValid(false);
    
   } else {
    ((EditableValueHolder) toValidate).setValid(true);
   }
  }
  if(valAmnt < 0){
   if(valAmnt > payRunAcntDocLineSel.getDocAmount())  {
    MessageUtil.addClientWarnMessage("acntLinesOsFrm:acntLnOsErr", "apDocPaidAmnt", "validationText");
   ((EditableValueHolder) toValidate).setValid(false);
   }else if(payRunAcntDocLineSel.getDocAmount() > 0)  {
    MessageUtil.addClientWarnMessage("acntLinesOsFrm:acntLnOsErr", "apDocCrnAmnt", "validationText");
    ((EditableValueHolder) toValidate).setValid(false);
   }else{
   ((EditableValueHolder) toValidate).setValid(true);
   }
  } 
  PrimeFaces.current().ajax().update("acntLinesOsFrm:acntLnOsErr");
  
 
}
 
 
 
 
 public List<PaymentTypeRec> onPayTypeComplete(String input){
  LOGGER.log(INFO, "onPayTypeComplete called with {0}", input);
  List<PaymentTypeRec> payTypes = sysBuff.getPaymentTypes();
  
  List<PaymentTypeRec> retList = new ArrayList<>();
  if(StringUtils.isBlank(input)){
   for(PaymentTypeRec p:payTypes){
    
    if(!p.isInbound() && !StringUtils.equals(p.getPayMedium(), "CSH")){
     retList.add(p);
    }
   }
   return retList;
  }else{
   
   if(payTypes == null || payTypes.isEmpty()){
    return null;
   }
   
   for(PaymentTypeRec p:payTypes){
    if(!p.isInbound() && StringUtils.startsWith(p.getDescription(), input)
      && !StringUtils.equals(p.getPayMedium(), "CSH")){
     retList.add(p);
     LOGGER.log(INFO, "Pay type id {0}", p.getId());
    }
   }
   LOGGER.log(INFO, "Payment type ret list {0}", retList);
   return retList;
  }
 }
 public void onPostDateSelect(SelectEvent evt){
  LOGGER.log(INFO,"onPostDateSelect called" );
  Date postDate = (Date)evt.getObject();
  List<String> updates = new ArrayList<>();
  this.fiDocument.setPostingDate(postDate);
  if(fiDocument.getDocumentDate() == null){
   fiDocument.setDocumentDate(postDate);
   updates.add("apPaySinglFrm:docDate");
  }
  updates.add("apPaySinglFrm:postDate");
  FiscalPeriodYearRec fisPerYear = sysBuff.getCompFiscalPeriodYearForDate(fiDocument.getCompany(), postDate);
  fiscPeriodStr = String.valueOf(fisPerYear.getYear())+ " / "+String.valueOf(fisPerYear.getPeriod());
  LOGGER.log(INFO, "post date fisPerYearStr {0}", fiscPeriodStr);
  fiDocument.setFisPeriod(fisPerYear.getPeriod());
  fiDocument.setFisYear(fisPerYear.getYear());
  updates.add("apPaySinglFrm:period");
  RequestContext.getCurrentInstance().update(updates);
  
 }
 public void onPrintOutput(){
  LOGGER.log(INFO, "Output print");
 }
 
 
 /**
  * Save payments and allocate to paid documents.
  * Fund allocation calculated on a document by document basis
  */
 public void onSaveSingle(){
  // 
  LOGGER.log(INFO, "onSaveSingle called");
  boolean errorFound = false;
  if(amountPaid == 0.0){
   MessageUtil.addWarnMessage("amountPaid", "validationText");
   errorFound = true;
  }
  
  
  if(docLineAp.getPayType().getGlBankAccount() == null){
   MessageUtil.addErrorMessageParam1( "payTyBnkAc","errorText", docLineAp.getPayType().getDescription());
   errorFound = true;
  }
  LOGGER.log(INFO, "errorFound {0}", errorFound);
  if(errorFound){
   RequestContext.getCurrentInstance().update("apPaySinglFrm:msg");
   return;
  }
  UserRec usr = this.getLoggedInUser();
  Date currDate = new Date();
  fiDocument.setCreatedBy(usr);
  fiDocument.setCreateOn(currDate);
  FiscalPeriodYearRec fisPerYr  = 
    sysBuff.getCompFiscalPeriodYearForDate(fiDocument.getCompany(), fiDocument.getPostingDate());
  fiDocument.setFisPeriod(fisPerYr.getPeriod());
  fiDocument.setFisYear(fisPerYr.getYear());
  docLineAp.setCreateBy(usr);
  docLineAp.setCreateDate(currDate);
  docLineAp.setComp(fiDocument.getCompany());
  docLineAp.setClearingLine(true);
  docLineAp.setClearingDate(currDate);
  LineTypeRuleRec apLineTy = sysBuff.getLineTypeRuleByCode("AP");
  LineTypeRuleRec glLineTy = sysBuff.getLineTypeRuleByCode("GL");
  List<DocLineBaseRec> docLines = new ArrayList<>();
  List<DocLineGlRec> reconPdLines = new ArrayList<>();
  List<DocLineGlRec> bankLinesGl = new ArrayList<>();
  FiGlAccountCompRec reconcilGlAcnt = docLineAp.getApAccount().getReconciliationAc();
  LOGGER.log(INFO, "reconcilGlAcnt {0}", reconcilGlAcnt);
  List<DocLineBaseRec> apPaidLines = new ArrayList<>();
  
  
  // unallocated need to create un-allocated line this will be to general fund if fund accounting
  // no fund split possible
  if(amountUnallocated > 0){
   DocLineApRec unallocApLn = new DocLineApRec();
   unallocApLn.setCreateBy(usr);
   unallocApLn.setCreateDate(currDate);
   unallocApLn.setComp(fiDocument.getCompany());
   
   unallocApLn.setLineType(apLineTy);
   unallocApLn.setDocAmount(amountUnallocated);
   unallocApLn.setHomeAmount(amountUnallocated);
   PostTypeRec postTy = sysBuff.getPostTypeForCode("apPyUnaloc");
   unallocApLn.setPostType(postTy);
   unallocApLn.setLineText(fiDocument.getPartnerRef());
   
   DocLineGlRec reconLine = new DocLineGlRec();
   reconLine.setCreateBy(usr);
   reconLine.setCreateDate(currDate);
   reconLine.setComp(fiDocument.getCompany());
   reconLine.setLineType(glLineTy);
   postTy = sysBuff.getPostTypeForCode("glDrRecon");
   reconLine.setPostType(postTy);
   reconLine.setDocAmount(amountUnallocated);
   reconLine.setHomeAmount(amountUnallocated);
   //FiGlAccountCompRec reconAc = docLineAp.getApAccount().getReconciliationAc();
   //LOGGER.log(INFO, "reconAc {0}", reconAc);
   reconLine.setGlAccount(reconcilGlAcnt);
   reconLine.setLineText(unallocApLn.getLineText());
   reconPdLines.add(reconLine);
   unallocApLn.setReconiliationLines(reconPdLines);
   LOGGER.log(INFO,"unallocApLn rec lines {0}",unallocApLn.getReconiliationLines());
   unallocApLn.setDocFi(fiDocument);
   docLines.add(unallocApLn);
  }
  
  
  // process payment lines
  
  for(DocLineApRec pdLine:docLinesPaid){
   LOGGER.log(INFO, "Paid doc line id {0}", pdLine.getId());
   double paidAmnt = pdLine.getPaidAmount();
   List<FundBalance> docfndSplit = getFundSplit(pdLine);
   for(FundBalance fndBal:docfndSplit){
    // bank lines on doc
    DocLineGlRec bnkLine = getBankLine(paidAmnt,fndBal,usr, currDate, glLineTy );
    if(bankLines.isEmpty()){
     LOGGER.log(INFO, "bankLines blank");
     bankLinesGl.add(bnkLine);
    }else{
     boolean foundBnkLine = false; 
     ListIterator<DocLineGlRec> bnkLnLi = bankLinesGl.listIterator();
     while(bnkLnLi.hasNext() && !foundBnkLine){
      DocLineGlRec currBnkLn = bnkLnLi.next();
      if(currBnkLn.getRestrictedFund() == null && bnkLine.getRestrictedFund() == null){
       foundBnkLine = true;
      }else if(currBnkLn.getRestrictedFund() != null && bnkLine.getRestrictedFund() != null &&
        Objects.equals(currBnkLn.getRestrictedFund().getId(), bnkLine.getRestrictedFund().getId())){
       foundBnkLine = true;
      } 
      if(foundBnkLine){
       double bnkLnAmnt = currBnkLn.getDocAmount();
       if((currBnkLn.getPostType().isDebit()&& bnkLine.getPostType().isDebit()) || 
         (!currBnkLn.getPostType().isDebit()&& !bnkLine.getPostType().isDebit())){
        bnkLnAmnt += bnkLine.getDocAmount();
       }else {
        bnkLnAmnt -= bnkLine.getDocAmount();
       }
       if(bnkLnAmnt < 0){
        PostTypeRec revPt =  currBnkLn.getPostType().getRevPostType();
        LOGGER.log(INFO, "curr post type {0} rev post type {1}", 
          new Object[]{currBnkLn.getPostType().getPostTypeCode(),revPt.getPostTypeCode()});
        currBnkLn.setPostType(revPt);
        bnkLnAmnt *= -1;
       }
       currBnkLn.setDocAmount(bnkLnAmnt);
      }
     }
     if(!foundBnkLine){
      bankLinesGl.add(bnkLine);
     }
    }
   
    // AP reconciliation line based of doc amount 
    DocLineGlRec apReconLn = getPaymentReconLine(docLineAp, fndBal);
    apReconLn.setGlAccount(reconcilGlAcnt);
    if(reconPdLines.isEmpty()){
     reconPdLines.add(apReconLn);
    }else{
     boolean foundLn = false;
     ListIterator<DocLineGlRec> reconLnsLi = reconPdLines.listIterator();
     while(reconLnsLi.hasNext() && !foundLn){
      DocLineGlRec currLn = reconLnsLi.next();
      if(currLn.getRestrictedFund() == null && apReconLn.getRestrictedFund() == null){
       foundLn = true;
      }else if(currLn.getRestrictedFund() != null && apReconLn.getRestrictedFund() != null &&
        Objects.equals(currLn.getRestrictedFund().getId(), apReconLn.getRestrictedFund().getId())){
       foundLn = true;
      }
      if(foundLn){
       // check signs the same before add
       if((currLn.getPostType().isDebit() && apReconLn.getPostType().isDebit()) ||
         (!currLn.getPostType().isDebit() && !apReconLn.getPostType().isDebit())){
        double amnt = currLn.getDocAmount();
        amnt += apReconLn.getDocAmount();
        currLn.setDocAmount(amnt);
       }else{
        double amnt = currLn.getDocAmount();
        amnt -= apReconLn.getDocAmount();
        if(amnt < 0){
         PostTypeRec revPt = apReconLn.getPostType().getRevPostType();
         amnt *= -1;
         apReconLn.setPostType(revPt);
         apReconLn.setDocAmount(amnt);
        }
       }
       reconLnsLi.set(currLn);
      }else{
       reconLnsLi.add(apReconLn);
      }
     }
    }
   }
   pdLine.setClearingDate(currDate);
   pdLine.setClearedLine(true);
   pdLine.setClearedByLine(docLineAp);
   apPaidLines.add(pdLine);
  }
  docLineAp.setReconiliationLines(reconPdLines);
  docLineAp.setClearingLineForLines(apPaidLines);
  docLines.add(docLineAp);
  docLines.addAll(bankLinesGl);
  fiDocument.setDocLines(docLines);
  
  LOGGER.log(INFO, "Clearing setting on docLineAp clearing {0} clear date {1} cleared docs {2}", 
    new Object[]{docLineAp.isClearingLine(),docLineAp.getClearingDate(),docLineAp.getClearingLineForLines()});
  
  for(DocLineBaseRec pdLn:docLineAp.getClearingLineForLines()){
   LOGGER.log(INFO, "Paid line clearing ln id{0} date {1} cleared {2} cleared by {3}", new Object[]{
    pdLn.getId(),pdLn.getClearingDate(), pdLn.isClearedLine(),pdLn.getClearedByLine()
   });
  }
  // check document balances
  double docBal = 0;
  double reconBal = 0;
  LOGGER.log(INFO, "Check Doc bal");
  for(DocLineBaseRec ln:fiDocument.getDocLines()){
   if(ln.getClass().getSimpleName().equals("DocLineApRec")){
    List<DocLineGlRec> recLns = ( (DocLineApRec)ln).getReconiliationLines();
    LOGGER.log(INFO, "AP rec lines {0} ", recLns);
    if(recLns != null){
     for(DocLineGlRec recLn:recLns){
      LOGGER.log(INFO, "Rec ln amount {0}", recLn.getDocAmount());
      LOGGER.log(INFO, "rec ln post key {0} debit {1}", 
       new Object[]{recLn.getPostType().getPostTypeCode(), recLn.getPostType().isDebit()});
      if(recLn.getPostType().isDebit()){
       reconBal += recLn.getDocAmount();
      }else{
       reconBal -= recLn.getDocAmount();
      }
     }
    }
    LOGGER.log(INFO, "reconBal {0}", reconBal);
   }
   if(ln.getPostType().isDebit()){
    docBal += ln.getDocAmount();
   }else{
    docBal -= ln.getDocAmount();
   }
   LOGGER.log(INFO, "doc line class {0} line amnt {1} debit {2} doc bal {3}", new Object[]{
    ln.getClass().getSimpleName(), ln.getDocAmount(), String.valueOf(ln.getPostType().isDebit()),docBal 
   });
  }
  
  if(docBal != 0){
   MessageUtil.addErrorMessageParam1("docBal", "errorText",String.valueOf(docBal));
   return;
  }
  fiDocument = docMgr.postApPaySingle(fiDocument,docLineAp,  getViewSimple());
  if(fiDocument.getId() == null){
   MessageUtil.addWarnMessage("docApPaySingle", "errorText");
   
  }else{
   MessageUtil.addInfoMessage("docApPaySingle", "blacResponse");
   MessageUtil.addInfoMessageVar1("docApPaySingle", "blacResponse", String.valueOf(fiDocument.getDocNumber()));
   init();
   
   amountPaid = 0.0;
   amountUnallocated = 0.00;
   docLinesOutStanding = null;
   RequestContext.getCurrentInstance().update("apPaySinglFrm");
  }
  
 }
 
 
}
