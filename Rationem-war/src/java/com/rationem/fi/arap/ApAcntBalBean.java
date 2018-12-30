/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.helper.ApArAccountBal;
import com.rationem.util.ApLineSel;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
public class ApAcntBalBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ApAcntBalBean.class.getName());
 
 private List<ApArAccountBal> accountBalList;
 private List<DocLineApRec> acntLines;
 private List<DocLineApRec> acntLinesFiltered;
 private CompanyBasicRec compSel;
 private double totalBal; 
 private ApArAccountBal accountBalSel; 
 private ApLineSel selOpt;
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 

 /**
  * Creates a new instance of ApAcntBalBean
  */
 public ApAcntBalBean() {
 }

 public ApAcntBalBean(List<ApArAccountBal> accountBalList, CompanyBasicRec compSel, double totalBal) {
  this.accountBalList = accountBalList;
  this.compSel = compSel;
  this.totalBal = totalBal;
 }

 
 @PostConstruct
 private void init(){
  accountBalList = new ArrayList<>();
  if(this.getCompList() == null || this.getCompList().isEmpty()){
   MessageUtil.addClientWarnMessage("errMsg", "compsNone", "errorText");
   return;
  }
  compSel = getCompList().get(0);
 }
 
 public List<ApArAccountBal> getAccountBalList() {
  return accountBalList;
 }

 public void setAccountBalList(List<ApArAccountBal> accountBalList) {
  this.accountBalList = accountBalList;
 }

 public ApArAccountBal getAccountBalSel() {
  return accountBalSel;
 }

 public void setAccountBalSel(ApArAccountBal accountBalSel) {
  this.accountBalSel = accountBalSel;
 }

 public List<DocLineApRec> getAcntLines() {
  return acntLines;
 }

 public void setAcntLines(List<DocLineApRec> acntLines) {
  this.acntLines = acntLines;
 }

 public List<DocLineApRec> getAcntLinesFiltered() {
  return acntLinesFiltered;
 }

 public void setAcntLinesFiltered(List<DocLineApRec> acntLinesFiltered) {
  this.acntLinesFiltered = acntLinesFiltered;
 }
 
 
 

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public double getTotalBal() {
  return totalBal;
 }

 public void setTotalBal(double totalBal) {
  this.totalBal = totalBal;
 }
 
 public void onExecBtn(){
  LOGGER.log(INFO, "execute buoon presss with Comp id {0}", this.compSel.getId());
  accountBalList = apMgr.getApBalances(compSel);
  if(accountBalList != null && !accountBalList.isEmpty() ){
   totalBal = 0;
   for(ApArAccountBal bal:accountBalList){
    if(bal.getAmount() >0){
     totalBal += bal.getAmount();
    }else{
     totalBal += bal.getAmount();
    }
   }
   RequestContext.getCurrentInstance().update("acntBalFrm:acntBalTbl");
  }
 }
 
 public void onAcntTransDlg(){
  selOpt = new ApLineSel();
  selOpt.setComp(compSel);
  ApAccountRec acnt = new ApAccountRec();
  acnt.setId(accountBalSel.getId());
  acnt.setAccountCode(accountBalSel.getCode());
  acnt.setAccountName(accountBalSel.getName());
  selOpt.setApAcnt(acnt);
  selOpt.setPayStatus(ApLineSel.STATUS_OS);
  
  acntLines = docMgr.getApLines(selOpt);
  LOGGER.log(INFO, "acntLines found {0}", acntLines);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(acntLines == null || acntLines.isEmpty()){
   MessageUtil.addClientWarnMessage("acntBalFrm:errMsg", "apAcntTrNo", "validationText");
   rCtx.update("acntBalFrm:errMsg");
   return;
  }
  
  rCtx.update("lineItmsFrm");
  rCtx.execute("PF('lineItmsWv').show();");
 }
 
 
 
}
