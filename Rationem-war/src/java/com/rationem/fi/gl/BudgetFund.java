/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.util.BaseBean;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.util.GenUtil;

import com.rationem.util.MessageUtil;
import com.rationem.util.helper.AnnualBudget;
import com.rationem.util.helper.comparator.FiBudgetYearsDesc;
import com.rationem.util.helper.comparator.FiFundByCodeAsc;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import com.rationem.util.helper.comparator.FiPerBalByGlAcntFiscPer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;

import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;


/**
 *
 * @author user
 */
public class BudgetFund extends BaseBean {
 private static final Logger logger = Logger.getLogger(BudgetFund.class.getName());
 
 private CompanyBasicRec compSel;
 
 private List<AnnualBudget> annBudgets; 
 private double annualTotal;
 private double preEditAmnt;
 private FiPeriodBalanceRec budget;
 private List<FiPeriodBalanceRec> budgetList;
 private List<Integer> budgetYears;
 private int budYearSel;
 private List<FiGlAccountCompRec> glAcntList;
 private FiGlAccountCompRec glAcntSel;
 private List<FundRec> fundList;
 private FundRec fundSel;
 private String budYearNew;
 private int numPeriods;
 
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glAcntMgr;
 

 /**
  * Creates a new instance of BudgetFund
  */
 public BudgetFund() {
 }

 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  numPeriods = 12;
  this.compSel = this.getCompList().get(0);
  glAcntList = glAcntMgr.getCompanyAccounts(compSel);
  if(glAcntList != null && !glAcntList.isEmpty()){
   Collections.sort(glAcntList, new FiGlAccountCompByRef());
   glAcntSel = glAcntList.get(0);
   fundList = compSel.getFundList();
   if(fundList != null & !fundList.isEmpty()){
    Collections.sort(fundList, new FiFundByCodeAsc());
    fundSel = fundList.get(0);
    budgetYears = this.glAcntMgr.getFiAcntFundBalsYrs(glAcntSel, fundSel);
    
    logger.log(INFO, "init budgetYears {0}", budgetYears);
    if(budgetYears != null){
     Collections.sort(budgetYears, new FiBudgetYearsDesc());
     budYearSel = budgetYears.get(0);
     for(Integer yr:budgetYears){
      List<FiPeriodBalanceRec> currYrBals = this.glAcntMgr.getFiBalForAcntFundYr(glAcntSel, fundSel, yr);
      if(annBudgets == null){
       annBudgets = new ArrayList<>();
      }
      AnnualBudget annBud = new AnnualBudget();
      annBud.setYear(yr);
      annBud.setPerBudgets(currYrBals);
      annBudgets.add(annBud);
      if(yr == this.budYearSel){
       this.budgetList = currYrBals;
      }
     }
    }
   }
  }
 }

 public List<AnnualBudget> getAnnBudgets() {
  return annBudgets;
 }

 public void setAnnBudgets(List<AnnualBudget> annBudgets) {
  this.annBudgets = annBudgets;
 }

 public double getAnnualTotal() {
  return annualTotal;
 }

 public void setAnnualTotal(double annualTotal) {
  this.annualTotal = annualTotal;
 }
 
 
 public FiPeriodBalanceRec getBudget() {
  return budget;
 }

 public void setBudget(FiPeriodBalanceRec budget) {
  this.budget = budget;
 }

 public List<FiPeriodBalanceRec> getBudgetList() {
  return budgetList;
 }

 public void setBudgetList(List<FiPeriodBalanceRec> budgetList) {
  this.budgetList = budgetList;
 }

 public String getBudYearNew() {
  return budYearNew;
 }

 public void setBudYearNew(String budYearNew) {
  this.budYearNew = budYearNew;
 }

 
 public List<Integer> getBudgetYears() {
  return budgetYears;
 }

 public void setBudgetYears(List<Integer> budgetYears) {
  this.budgetYears = budgetYears;
 }

 public int getBudYearSel() {
  return budYearSel;
 }

 public void setBudYearSel(int budYearSel) {
  this.budYearSel = budYearSel;
 }

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<FiGlAccountCompRec> getGlAcntList() {
  return glAcntList;
 }

 public void setGlAcntList(List<FiGlAccountCompRec> glAcntList) {
  this.glAcntList = glAcntList;
 }

 public FiGlAccountCompRec getGlAcntSel() {
  return glAcntSel;
 }

 public void setGlAcntSel(FiGlAccountCompRec glAcntSel) {
  this.glAcntSel = glAcntSel;
 }

 
 public List<FundRec> getFundList() {
  return fundList;
 }

 public void setFundList(List<FundRec> fundList) {
  this.fundList = fundList;
 }

 public FundRec getFundSel() {
  return fundSel;
 }

 public void setFundSel(FundRec fundSel) {
  this.fundSel = fundSel;
 }

 public double getPreEditAmnt() {
  return preEditAmnt;
 }

 public void setPreEditAmnt(double preEditAmnt) {
  this.preEditAmnt = preEditAmnt;
 }

 public void onAccountChange(ValueChangeEvent evt){
  logger.log(INFO, "onAccountChange called with {0}", evt.getNewValue());
  glAcntSel = (FiGlAccountCompRec)evt.getNewValue();
  logger.log(INFO, "glAcntSel ref {0}", glAcntSel.getCoaAccount().getRef());
  annBudgets = new ArrayList<>(); 
  budgetList = glAcntMgr.getFiBalForAcntFundYr(glAcntSel, fundSel, budYearSel);
  logger.log(INFO, "budgetList {0}", budgetList);
  if(budgetList != null && !budgetList.isEmpty()){
   Collections.sort(budgetList,new FiPerBalByGlAcntFiscPer());
   AnnualBudget annBud = new AnnualBudget();
   annBud.setYear(budYearSel);
   annBud.setPerBudgets(budgetList);
   
   annualTotal = 0.0;
   for(FiPeriodBalanceRec curr: budgetList ){
    logger.log(INFO, "Budget period {0} id {1}", new Object[]{curr.getBalPeriod(),curr.getId()});
    annualTotal += curr.getPeriodBudgetAmount();
   }
  }else{
   budYearSel = 0;
   annualTotal = 0.0;
   budgetYears = null;
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("fundBudFrm");
 }
 
 public void onBudPerValEdit(RowEditEvent evt){
  logger.log(INFO, "onBudPerValEdit called with {0}", evt.getObject());
  FiPeriodBalanceRec postBal = (FiPeriodBalanceRec)evt.getObject();
  double finalAmnt = postBal.getPeriodBudgetAmount();
  logger.log(INFO, "Total pre update {0} pre amnt {1} post amnt {2}", new Object[]
  {annualTotal,preEditAmnt,finalAmnt});
  annualTotal -= preEditAmnt;
  annualTotal += finalAmnt;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("annTotal");
 }
 
 public void onBudPerValEditInit(RowEditEvent evt){
  logger.log(INFO, "onBudPerValEditInit called with {0}", evt.getObject());
  FiPeriodBalanceRec preBal = (FiPeriodBalanceRec)evt.getObject();
  this.preEditAmnt = preBal.getPeriodBudgetAmount();
 }
 
 public void onBudSpread(){
  double tempTotal ;
  double periodic = annualTotal / numPeriods;
  double finalPer;
  periodic = GenUtil.round(periodic, this.compSel.getCurrency().getMinorUnit(), BigDecimal.ROUND_HALF_UP);
  tempTotal = periodic * (numPeriods -1 );
  finalPer = annualTotal - tempTotal;
  logger.log(INFO, "periodic {0} tempTotal {1} final per {2} ", new Object[]{periodic,tempTotal,finalPer} );
  ListIterator<FiPeriodBalanceRec> balLi = this.budgetList.listIterator();
  while(balLi.hasNext()){
   FiPeriodBalanceRec curr = balLi.next();
   if(curr.getBalPeriod() != numPeriods){
    curr.setPeriodBudgetAmount(periodic);
   }else{
    curr.setPeriodBudgetAmount(finalPer);
   }
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("budTblId");
  }
  
 }
 
 public void onFundBudSave(){
  logger.log(INFO, "onGlAcntBudSave");
  ListIterator<FiPeriodBalanceRec> balLi = budgetList.listIterator();
  UserRec updateUsr = getLoggedInUser();
  Date updateDate = new Date();
  while(balLi.hasNext()){
   FiPeriodBalanceRec curr = balLi.next();
   curr.setUpdateBy(updateUsr);
   curr.setUpdateDate(updateDate);
   balLi.set(curr);
  }
  glAcntSel.setRestrictedBalances(budgetList);
  try{
   glAcntMgr.updateGlAcntFndBudgets(glAcntSel, getView());
   MessageUtil.addInfoMessage("restrFndBudUpdt", "blacResponse");
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("fundBudFrm");
  }catch(Exception ex){
   logger.log(INFO, "could not save budget {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("budSaveNone", "errorText");
  }
 }
 
 public void onYearTrf(){
  logger.log(INFO, "onYearTrf call year {0}", this.budYearNew);
  budYearSel = Integer.parseInt(budYearNew);
  
  logger.log(INFO, "gl account {0} fund {1}", new Object[]{
  this.glAcntSel.getCoaAccount().getRef(),  this.fundSel.getFndCode()  });
  if(budgetYears != null && !budYearNew.isEmpty() ){
   for(Integer curr:budgetYears){
    if(curr == budYearSel){
     MessageUtil.addWarnMessage("budYrDupl", "errorText");
     logger.log(INFO, "Add dup year {0}", budYearSel);
     return;
    }
   }
  }
  UserRec newUsr = this.getLoggedInUser();
  Date currDate = new Date();
  int numPers = 12;//perRule.getNumPeriods();
  List<FiPeriodBalanceRec> bals = new ArrayList<>();
  for(int i = 0; i <numPers; i++){
   FiPeriodBalanceRec currBal = new FiPeriodBalanceRec();
   int currPer = i + 1;
   currBal.setCreatedBy(newUsr);
   currBal.setCreated(currDate);
   currBal.setBalPeriod(currPer);
   currBal.setBalYear(budYearSel);
   currBal.setFiGlAccountComp(glAcntSel);
   currBal.setRestrictedFund(fundSel);
   currBal.setBalanceType(FiPeriodBalanceRec.RESTRICTED_ACT);
   logger.log(INFO, "currBal created by id {0}", currBal.getCreatedBy().getId());
   logger.log(INFO, "currBal per {0} yr {1}", new Object[]{currBal.getBalPeriod(), currBal.getBalYear()});
   currBal = this.glAcntMgr.updatePeriodBalances(currBal, this.getView());
   bals.add(currBal);
  }
  budgetList = bals;
  if(annBudgets == null){
   annBudgets = new ArrayList<>();
  }
  AnnualBudget annBud = new AnnualBudget();
  annBud.setYear(budYearSel);
  annBud.setPerBudgets(budgetList);
  annBudgets.add(annBud);
  this.glAcntSel.setPeriodBalances(bals);
  if(budgetYears == null){
   budgetYears = new ArrayList<>();
  }
  budgetYears.add(budYearSel);
  annualTotal = 0.0;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("fundBudFrm");
  pf.executeScript("PF('addFndYrWv').hide();");
 }
 
}
