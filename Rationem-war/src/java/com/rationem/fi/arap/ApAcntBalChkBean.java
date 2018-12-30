/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.FiApPeriodBalanceRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.helper.comparitor.ApLineByAcntPstDt;
import com.rationem.util.ApArAcntBalUpdate;
import com.rationem.util.ApLineSel;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

import static java.util.logging.Level.INFO;


/**
 *
 * @author user
 */
public class ApAcntBalChkBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(ApAcntBalChkBean.class.getName());
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 private CompanyBasicRec compSel;
 private ApAccountRec apAccount;
 
 private List<DocLineApRec> apLines;
 private ApLineSel selOpt;
 private List<ApArAcntBalUpdate> apAccountUpdates;
 

 /**
  * Creates a new instance of apAcntBalChk
  */
 public ApAcntBalChkBean() {
 }
 
 @PostConstruct
 private void init(){
  if(getCompList() == null || getCompList().isEmpty()){
   MessageUtil.addClientErrorMessage("acntBalChkFrm:errMsg", "compsNone", "errorText");
   RequestContext.getCurrentInstance().update("acntBalChkFrm:errMsg");
   
  }else{
   compSel = getCompList().get(0);
   selOpt = new ApLineSel();
  }
  
 }

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public ApAccountRec getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
  
 }

 public List<ApArAcntBalUpdate> getApAccountUpdates() {
  return apAccountUpdates;
 }

 public void setApAccountUpdates(List<ApArAcntBalUpdate> apAccountUpdates) {
  this.apAccountUpdates = apAccountUpdates;
 }
 
 
 public List<ApAccountRec> onApAccountComplete(String input){
  
  List<ApAccountRec> retList;
  if(StringUtils.isBlank(input)){
   retList = apMgr.getApAccountsAll(compSel);
  }else{
   retList = apMgr.getApAccountsStartinfWithCode(compSel, input);
 }
  
  
 return retList;
 }
 
 public boolean onUpdateBals(){
  LOGGER.log(INFO, "bean onUpdateBals called");
  boolean updated = false; 
  if(apAccount != null){
   // AP account selected so only need to process for a single account
   apLines = docMgr.getApLinesForAcnt(compSel, apAccount);
  }else{
   apLines = docMgr.getApLinesByComp(compSel);
  }
  if(apLines == null || apLines.isEmpty()){
   MessageUtil.addClientWarnMessage("acntBalChkFrm:errMsg", "apAcntLinesCompNone", "validationText");
   RequestContext.getCurrentInstance().update("acntBalChkFrm:errMsg");
   return false;
  }
  
  for(DocLineApRec l:apLines){
   LOGGER.log(INFO, "doc num {0} docFi {1}", new Object[]{l.getDocNumber(), l.getDocFi()});
  }
  Collections.sort(apLines, new ApLineByAcntPstDt());
  ApAccountRec currAcnt = null;
  List<DocLineApRec> apSingleLines = new ArrayList<>();
  apAccountUpdates = new ArrayList<>();
  for(DocLineApRec curr: apLines ){
   LOGGER.log(INFO, "currAcnt {0}", currAcnt);
   if(currAcnt == null){
    // first line
    currAcnt = curr.getApAccount();
    apSingleLines.add(curr);
    
   }else{
    // subsequent lines
    if(Objects.equals(curr.getApAccount().getId(), currAcnt.getId())){
     apSingleLines.add(curr);
    }else{
     LOGGER.log(INFO, " completed next ap account");
     updateBals(currAcnt, apSingleLines);
    }
     
   }
   
  }
  
  
  
  // process final AP account
  LOGGER.log(INFO, "Final AP account set {0} ap account {1}", new Object[]{apSingleLines,currAcnt});
  updated = updateBals(currAcnt, apSingleLines);
  
  RequestContext.getCurrentInstance().update("acntBalChkFrm:acntUpdtTbl");
  return updated;
  
 }
 
 private boolean updateBals(ApAccountRec acnt, List<DocLineApRec> lines){
  LOGGER.log(INFO, "updateBals called with acnt {0} lines {1}", new Object[]{acnt,lines});
  
  boolean upDateReq = false;
  double calcBal = 0;
  int lineNum = 0;
  
  
  if(acnt == null || lines == null){
   LOGGER.log(INFO, "Account or lines is null");
   return false;
  }
  
  LOGGER.log(INFO, "AP acnt rec period bals {0}", acnt.getApPeriodBalances());
  if(acnt.getApPeriodBalances() == null){
   // attempt to find period balances
   acnt = this.apMgr.getApAccountPeriodBalances(acnt);
  }
  LOGGER.log(INFO, "AP acnt rec period bals 2 {0}", acnt.getApPeriodBalances());
  List<FiApPeriodBalanceRec> calPerBals = new ArrayList<>();
  LOGGER.log(INFO, "LIne for account starts as {0}", calPerBals);
  
  LOGGER.log(INFO, "Lines before summarisation into Per balances");
  for(DocLineApRec curr:lines){
   LOGGER.log(INFO, "Year {0} period {1} amount {2} debit {3}" , new Object[]{
    curr.getDocFi().getFisYear(),curr.getDocFi().getFisPeriod(),curr.getDocAmount(),curr.getPostType().isDebit()
   });
  }
  List<DocLineApRec> crLines;
  List<DocLineApRec> drLines;
  for(DocLineApRec curr:lines){
   LOGGER.log(INFO, "calPerBals {0}", calPerBals);
   lineNum++;
   LOGGER.log(INFO, "line num {0}", lineNum);
   double amount = curr.getDocAmount();
   boolean debit = curr.getPostType().isDebit();
   int perBalsIndex = 0;
   int matchIndex = 0;
   boolean perBalFound = false;
   double periodDocAmnt;
   double periodDebit;
   double periodCredit;
   int fisYear = curr.getDocFi().getFisYear();
   int fisPer = curr.getDocFi().getFisPeriod();
   FiApPeriodBalanceRec currPerBal = null;
   LOGGER.log(INFO, "Curr line amount {0} debit {1} year {2} period {3}", new Object[]{
    amount,debit,fisYear,fisPer });
   LOGGER.log(INFO, "Balance list {0}", calPerBals);
   if(calPerBals.isEmpty()){
    LOGGER.log(INFO, "calPerBals is null / empty");
    currPerBal = new FiApPeriodBalanceRec();
    currPerBal.setApAccount(acnt);
    currPerBal.setBalPeriod(fisPer);
    currPerBal.setBalYear(fisYear);
   }else{
    LOGGER.log(INFO, "calPerBals populated");
    perBalsIndex = 0;
    
    for(FiApPeriodBalanceRec currPb:calPerBals){
     
     if(currPb.getBalYear() == fisYear && currPb.getBalPeriod() == fisPer){
      currPerBal = currPb;
      perBalFound = true;
      matchIndex = perBalsIndex;
     }
     perBalsIndex++;
    }
    
    if(!perBalFound){
     LOGGER.log(INFO, "Per Bal not in current list");
     
     currPerBal = new FiApPeriodBalanceRec();
     currPerBal.setApAccount(acnt);
     currPerBal.setBalPeriod(fisPer);
     currPerBal.setBalYear(fisYear);
     
    }
   }
   crLines = new ArrayList<>(); 
   drLines = new ArrayList<>();  
   LOGGER.log(INFO, "calPerBals now {0} perBalsIndex {1} ", new Object[]{calPerBals,matchIndex});
   if(currPerBal ==  null){
    LOGGER.log(INFO, "Line 231 curr bal file is blank");
    currPerBal = new FiApPeriodBalanceRec();
    currPerBal.setApAccount(acnt);
    currPerBal.setBalPeriod(fisPer);
    currPerBal.setBalYear(fisYear);
    crLines = new ArrayList<>();
    drLines = new ArrayList<>();
   }
   
   periodDocAmnt = currPerBal.getPeriodDocAmount();
   periodDebit = currPerBal.getPeriodDebitAmount();
   periodCredit = currPerBal.getPeriodCreditAmount();
   if(debit){
    calcBal += amount;
    periodDocAmnt += amount;
    periodDebit += amount;
    drLines.add(curr);
   }else{
    calcBal -= amount;
    periodDocAmnt -= amount;
    periodCredit += amount;
    crLines.add(curr);
   }
   
   currPerBal.setPeriodCreditAmount(periodCredit);
   currPerBal.setPeriodDebitAmount(periodDebit);
   currPerBal.setPeriodDocAmount(periodDocAmnt);
   currPerBal.setCreditDocLines(crLines);
   currPerBal.setDebitDocLines(drLines);
   //perBalsIndex = calPerBals.size() -1;
   LOGGER.log(INFO, "perBalsIndex {0} calPerBals.size() {1} matchIndex {2}",
     new Object[]{calPerBals, calPerBals.size(),matchIndex});
   LOGGER.log(INFO, "perBalFound {0}", perBalFound);
   if(calPerBals.isEmpty() || !perBalFound){
    calPerBals.add(currPerBal);
   }else{
    calPerBals.set(matchIndex, currPerBal);
   }
  }
  
  LOGGER.log(INFO, "calcBal {0} acnt.getAccountBalance() {1}", new Object[]{calcBal,acnt.getAccountBalance()});
  ApArAcntBalUpdate balChanged = new ApArAcntBalUpdate();
  balChanged.setApAcnt(acnt);
  balChanged.setAcntBalUpdate(false);
  balChanged.setPerBalUpdate(false);
  if(calcBal != acnt.getAccountBalance()){
   // balance changed
   balChanged.setAcntBalUpdate(true);
  }
  if(acnt.getApPeriodBalances() == null && !calPerBals.isEmpty()){
    balChanged.setPerBalUpdate(true);
  }else if(acnt.getApPeriodBalances() != null && calPerBals.isEmpty()){
    balChanged.setPerBalUpdate(true);
  }else if(acnt.getApPeriodBalances().size() != calPerBals.size()){
    balChanged.setPerBalUpdate(true);
  }else{
   LOGGER.log(INFO, "acnt.getApPeriodBalances() {0} calc num bals {1}", 
     new Object[]{acnt.getApPeriodBalances(),calPerBals});
   // loop over cal bal and see if current acnt has matching entry
   
   for(FiApPeriodBalanceRec currCal:calPerBals){
    boolean matched = false;
    LOGGER.log(INFO, "process cal Bal currCal year {0} period {1} bal {2} matched {3}", new Object[]{
     currCal.getBalYear(), currCal.getBalPeriod(),currCal.getPeriodDocAmount(),matched });
    for(FiApPeriodBalanceRec curr :acnt.getApPeriodBalances()){
     if((curr.getBalYear() == currCal.getBalYear()) && (curr.getBalYear() == currCal.getBalYear()) &&
       (curr.getPeriodDocAmount() == currCal.getPeriodDocAmount()) &&
       (curr.getPeriodCreditAmount() == currCal.getPeriodCreditAmount()) &&
       (curr.getPeriodDebitAmount() == currCal.getPeriodDebitAmount())){
        matched = true;
        break;
     }
     
    }
    LOGGER.log(INFO, "After check actual per bal found in act per bal list {0}", matched);
    if(!matched){
     
     balChanged.setPerBalUpdate(true);
    }
    LOGGER.log(INFO, "Update bal required {0} for calc bal year {1} period {2} ", new Object[]{
     balChanged.isPerBalUpdate(), currCal.getBalYear(), currCal.getBalPeriod()
    });
   }
   
  }
 
   
   if(apAccountUpdates == null){
    apAccountUpdates = new ArrayList<>();
   }
   apAccountUpdates.add(balChanged);
   upDateReq = true;
   acnt.setAccountBalance(calcBal);
   acnt.setApPeriodBalances(calPerBals);
   acnt.setChangedBy(this.getLoggedInUser());
   acnt.setChangedOn(new Date());
   
   for(FiApPeriodBalanceRec pb:acnt.getApPeriodBalances()){
    LOGGER.log(INFO, "Period Balance for account. Year {0} period {1}", new Object[]{pb.getBalYear(),pb.getBalPeriod()});
    LOGGER.log(INFO, "AP account id {0}", pb.getApAccount().getId());
    LOGGER.log(INFO,"Per perbalance id{0}",pb.getId());
    LOGGER.log(INFO,"Per perbalance debit docs {0}",pb.getDebitDocLines());
    LOGGER.log(INFO,"Per perbalance credit docs {0}",pb.getCreditDocLines());
    if(pb.getDebitDocLines() != null){
     LOGGER.log(INFO, "Debit doc lines");
     for(DocLineApRec apLn:pb.getDebitDocLines()){
      LOGGER.log(INFO, "Line id {0} amount {1}", new Object[]{apLn.getId(),apLn.getDocAmount()});
     }
    }
    if(pb.getCreditDocLines() != null){
     LOGGER.log(INFO, "Credit doc lines");
     for(DocLineApRec apLn:pb.getCreditDocLines()){
      LOGGER.log(INFO, "Line id {0} amount {1}", new Object[]{apLn.getId(),apLn.getDocAmount()});
     }
    }
   }
   
   apMgr.updateApAccountBal(acnt, this.getView());
  
  LOGGER.log(INFO, "Account updated {0}", acnt.getAccountCode());
  LOGGER.log(INFO, "Current Balance {0}", calcBal);
  for(FiApPeriodBalanceRec curr:calPerBals){
   LOGGER.log(INFO, "Period Year {0} period {1} debit {2} credit {3} bal {4}", new Object[]{
    curr.getBalYear(),curr.getBalPeriod(),curr.getPeriodDebitAmount(), curr.getPeriodCreditAmount(),
    curr.getPeriodDocAmount()});
  }
  
  LOGGER.log(INFO, "End of update apAccountUpdates {0}", apAccountUpdates);
  return upDateReq;
 }
}
