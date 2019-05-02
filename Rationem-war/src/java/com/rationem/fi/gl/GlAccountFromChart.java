/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl;

import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.primefaces.event.SelectEvent;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author user
 */
public class GlAccountFromChart extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(GlAccountFromChart.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glAcntMgr; 
 
 @EJB
 private BankManager bnkMgr;
 
 private ChartOfAccountsRec coa;
 private List<FiGlAccountCompRec> compAccounts;
 private List<FiGlAccountCompRec> compAccountsFiltered;
 private FiGlAccountCompRec compAccountSelected;
 private CompanyBasicRec compSelected;
 private FiBsAccountRec accountBs;
 private FiPlAccountRec accountPl;
 

 /**
  * Creates a new instance of GlAccountFromChart
  */
 public GlAccountFromChart() {
 }

 public FiBsAccountRec getAccountBs() {
  return accountBs;
 }

 public void setAccountBs(FiBsAccountRec accountBs) {
  this.accountBs = accountBs;
 }

 public FiPlAccountRec getAccountPl() {
  return accountPl;
 }

 public void setAccountPl(FiPlAccountRec accountPl) {
  this.accountPl = accountPl;
 }

 
 public ChartOfAccountsRec getCoa() {
  return coa;
 }

 public void setCoa(ChartOfAccountsRec coa) {
  this.coa = coa;
 }

 public List<FiGlAccountCompRec> getCompAccounts() {
  return compAccounts;
 }

 public void setCompAccounts(List<FiGlAccountCompRec> compAccounts) {
  this.compAccounts = compAccounts;
 }

 public List<FiGlAccountCompRec> getCompAccountsFiltered() {
  return compAccountsFiltered;
 }

 public void setCompAccountsFiltered(List<FiGlAccountCompRec> compAccountsFiltered) {
  this.compAccountsFiltered = compAccountsFiltered;
 }
 
 

 public CompanyBasicRec getCompSelected() {
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }

 public FiGlAccountCompRec getCompAccountSelected() {
  
  return compAccountSelected;
 }

 public void setCompAccountSelected(FiGlAccountCompRec compAccountSelected) {
  this.compAccountSelected = compAccountSelected;
 }
 
 public void onAccountEditBtn(){
  LOGGER.log(INFO, "onAccountEditBtn called account selected {0}",compAccountSelected.getCoaAccount().getName());
  PrimeFaces pf = PrimeFaces.current();
  if(StringUtils.equals(compAccountSelected.getCoaAccount().getClass().getSimpleName(), "FiBsAccountRec")){
   accountBs = (FiBsAccountRec)compAccountSelected.getCoaAccount();
  }else if (StringUtils.equals(compAccountSelected.getCoaAccount().getClass().getSimpleName(), "FiPlAccountRec")){
   accountPl =  (FiPlAccountRec)compAccountSelected.getCoaAccount();
  }else{
   MessageUtil.addClientWarnMessage("glAcntCrFrm:msgs", "glAcntTyErr", "errorText");
   pf.ajax().update("glAcntCrFrm:msgs");
   return;
  }
  
  pf.ajax().update("editFrm");
  //pf.ajax().update("editFrm:headerParam");
  pf.executeScript("PF('editDlgWv').show();");
  
 }
 
 public void onAccountListContextMnu(SelectEvent evt){
  LOGGER.log(INFO, "onAccountListContextMnu called selected {0}", evt.getObject());
  compAccountSelected = (FiGlAccountCompRec)evt.getObject();
 }
   
 public List<BankAccountCompanyRec> onBnkAccntCompRecComplete(String input){
  LOGGER.log(INFO, "onBnkAccntCompRecComplete called with {0}", input);
  List<BankAccountCompanyRec> bnkList = bnkMgr.getBankAccountsForCompany(this.compAccountSelected.getCompany());
  if(bnkList == null || bnkList.isEmpty()){
   return null;
  }
  if(StringUtils.isBlank(input)){
   return bnkList;
  }else{
   List<BankAccountCompanyRec> retList = new ArrayList<>();
   for(BankAccountCompanyRec curr:bnkList){
    if(StringUtils.startsWith(curr.getAccountRef(), input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
 public List<ChartOfAccountsRec> onCoaComplete(String input){
  LOGGER.log(INFO, "onCoaComplete called with {0}", input);
 
  if(StringUtils.isBlank(input)){
   return sysBuff.getChartsOfAccounts();
  }else{
   return sysBuff.getChartsOfAccountsByRef(input);
  }
 
 } 

 public void onCoaSelect(SelectEvent evt){
 
 }
 
 public void onCreateAccounts(){
  LOGGER.log(INFO, "onCreateAccounts called");
  compAccounts = glAcntMgr.glCompAcntFrCoaCreate(coa, compSelected, getLoggedInUser(), getView());
  PrimeFaces pf = PrimeFaces.current();
  if(compAccounts == null || compAccounts.isEmpty()){
   MessageUtil.addClientWarnMessage("glAcntCrFrm:msgs", "glAcntComFeCoaCr", "errorText");
   pf.ajax().update("glAcntCrFrm:msgs");
  }else{
   List<String> updates = new ArrayList<>();
   MessageUtil.addClientInfoMessage("glAcntCrFrm:msgs", "glAccountsCompCr", "blacResponse");
   updates.add("glAcntCrFrm:msgs");
   updates.add("glAcntCrFrm:glAcntsTbl");
   pf.ajax().update(updates);
  }
 }
 
 public void onCompAcntEditTrf(){
  LOGGER.log(INFO, "called onEditCompAcntTrf with accnt ",compAccountSelected.getClass().getSimpleName());
  /*
  compAccountSelected.setChangedBy(this.getLoggedInUser());
  compAccountSelected.setChangedOn(new Date());
  compAccountSelected = this.glAcntMgr.updateGlAccountComp(compAccountSelected, getView());
  */
  this.accountBs = null;
  this.accountPl = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.executeScript("PF('editDlgWv').hide()");
 }

public void onCompAcntEditClose(){
  LOGGER.log(INFO, "called onEditCompAcntTrf with accnt ",compAccountSelected.getClass().getSimpleName());
  this.accountBs = null;
  this.accountPl = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.executeScript("PF('editDlgWv').hide()");
}

 
 
 public List<SortOrderRec> onSortOrderComplete(String input){
  LOGGER.log(INFO, "onSortOrderComplete called with {0}", input);
  if(StringUtils.isBlank(input)){
   return sysBuff.getSortOrders();
  }else{
   return sysBuff.getSortOrdersByNamePrefix(input);
  }
 }
 
 public List<VatCodeCompanyRec> onVatCodeCompComplete(String input){
  LOGGER.log(INFO, "onVatCodeCompComplete called with {0}", input);
  
  if(StringUtils.isBlank(input)){
   return sysBuff.getCompVatCodes(compSelected);
  }else{
   List<VatCodeCompanyRec> vatRecs = sysBuff.getCompVatCodes(compSelected);
   if (vatRecs == null || vatRecs.isEmpty()){
    return null;
   }
   List<VatCodeCompanyRec> retList = new ArrayList<>();
   for(VatCodeCompanyRec curr: vatRecs){
    if(StringUtils.startsWith(curr.getVatCode().getCode(), input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
}
