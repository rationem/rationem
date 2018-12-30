/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl;

import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.AnnualBudget;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import com.rationem.util.helper.comparator.FiPerBalByGlAcntFiscPer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author user
 */
public class BudgetGlAccount extends BaseBean {
 private static final Logger logger =  Logger.getLogger(BudgetGlAccount.class.getName());
 private CompanyBasicRec compSel;
 private FiGlAccountCompRec glAcntSel;
 private List<FiGlAccountCompRec> glAcntList;
 private FiPeriodBalanceRec budget;
 private List<FiPeriodBalanceRec> budgetList;
 private List<Integer> budgetYears;
 private int budYearSel;
 private String budYearNew;
 private double annualTotal;
 private double preEditAmnt;
 private int numPeriods;
 private List<AnnualBudget> annBudgets;
 
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
 }
  compSel = this.getCompList().get(0);
  glAcntList = glAcntMgr.getCompanyAccounts(compSel);
  if(glAcntList == null){
   MessageUtil.addErrorMessage("glAcntCompNone", "errorText");
   return;
  }
  Collections.sort(glAcntList, new FiGlAccountCompByRef());
  glAcntSel = glAcntList.get(0);
  budgetYears = this.glAcntMgr.getGlAccountBalanceYears(glAcntSel, FiPeriodBalanceRec.GL_ACT);
  logger.log(INFO, "Balance Years {0}", budgetYears);
  if(budgetYears != null){
   this.budYearSel = budgetYears.get(0);
   this.budgetList = this.glAcntMgr.getFiBalForAccntYr(glAcntSel, budYearSel, FiPeriodBalanceRec.GL_ACT);
   Collections.sort(budgetList,new FiPerBalByGlAcntFiscPer());
   logger.log(INFO, "budgetList {0}", budgetList);
   annBudgets = new ArrayList<>();
   AnnualBudget annBud = new AnnualBudget();
   annBud.setYear(budYearSel);
   annBud.setPerBudgets(budgetList);
   annualTotal = 0.0;
   for(FiPeriodBalanceRec curr: budgetList ){
    logger.log(INFO, "Budget period {0} id {1}", new Object[]{curr.getBalPeriod(),curr.getId()});
    annualTotal += curr.getPeriodBudgetAmount();
   }
  }
  numPeriods = 12;
 }

 /**
  * Creates a new instance of BudgetGlAccount
  */
 public BudgetGlAccount() {
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

 public List<Integer> getBudgetYears() {
  return budgetYears;
 }

 public void setBudgetYears(List<Integer> budgetYears) {
  this.budgetYears = budgetYears;
 }

 public String getBudYearNew() {
  return budYearNew;
 }

 public void setBudYearNew(String budYearNew) {
  this.budYearNew = budYearNew;
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

 public FiGlAccountCompRec getGlAcntSel() {
  return glAcntSel;
 }

 public void setGlAcntSel(FiGlAccountCompRec glAcntSel) {
  this.glAcntSel = glAcntSel;
 }

 public List<FiGlAccountCompRec> getGlAcntList() {
  return glAcntList;
 }

 public void setGlAcntList(List<FiGlAccountCompRec> glAcntList) {
  this.glAcntList = glAcntList;
 }

 

 public List<FiPeriodBalanceRec> getBudgetList() {
  return budgetList;
 }

 public void setBudgetList(List<FiPeriodBalanceRec> budgetList) {
  this.budgetList = budgetList;
 }
 
 public void onAccountChange(ValueChangeEvent evt){
  logger.log(INFO, "onAccountChange called with {0}", evt.getNewValue());
  glAcntSel = (FiGlAccountCompRec)evt.getNewValue();
  logger.log(INFO, "glAcntSel ref {0}", glAcntSel.getCoaAccount().getRef());
  annBudgets = new ArrayList<>(); 
  budgetList = glAcntMgr.getFiBalForAccntYr(glAcntSel, budYearSel, FiPeriodBalanceRec.GL_ACT);
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
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("glActBud");
 }
 
 public void onBudgetYearChange(ValueChangeEvent evt){
  logger.log(INFO, "onBudgetYearChange called new value {0}", evt.getNewValue());
  budYearSel = (Integer)evt.getNewValue();
  int oldYr = (Integer)evt.getOldValue(); 
  boolean foundYr = false;
  ListIterator<AnnualBudget> annBudLi = this.annBudgets.listIterator();
  while(annBudLi.hasNext() && !foundYr){
   AnnualBudget curr = annBudLi.next();
   if(curr.getYear() == oldYr ){
    curr.setPerBudgets(budgetList);
    annBudLi.set(curr);
    foundYr = true;
   }
  }
  
  foundYr = false;
  annBudLi = this.annBudgets.listIterator();
  while(annBudLi.hasNext() && !foundYr){
   AnnualBudget curr = annBudLi.next();
   if(curr.getYear() == budYearSel ){
    budgetList = curr.getPerBudgets();
    foundYr = true;
   }
  }
  if(!foundYr){
   budgetList = glAcntMgr.getFiBalForAccntYr(glAcntSel, budYearSel, FiPeriodBalanceRec.GL_ACT);
   AnnualBudget curr = new AnnualBudget();
   curr.setYear(budYearSel);
   curr.setPerBudgets(budgetList);
   annBudgets.add(curr);
  }
  
  logger.log(INFO, "budgetList {0}", budgetList);
  annualTotal = 0.0;
  if(budgetList != null && !budgetList.isEmpty()){
   Collections.sort(budgetList,new FiPerBalByGlAcntFiscPer());
   
   for(FiPeriodBalanceRec curr:budgetList){
    annualTotal += curr.getPeriodBudgetAmount();
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("glActBud");
 }
 
 
 public void onBudgetYearNewTrf(){
  logger.log(INFO, "onBudgetYearNewTrf called with yr {0}", budYearNew);
  budYearSel = Integer.parseInt(budYearNew);
  if(budgetYears != null && !budYearNew.isEmpty() ){
   for(Integer curr:budgetYears){
    if(curr == budYearSel){
     MessageUtil.addWarnMessage("budYrDupl", "errorText");
     logger.log(INFO, "Add dup year {0}", budYearSel);
     return;
    }
   }
  }
  FisPeriodRuleRec perRule = this.compSel.getPeriodRule();
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
   currBal.setBalanceType(FiPeriodBalanceRec.GL_BUDGET);
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
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("glActBud");
  rCtx.execute("PF('addBudYrWv').hide();");
  logger.log(INFO, "budgetList {0}", budgetList);
  budYearNew = null;
 }
 
 public void onBudPerValEdit(RowEditEvent evt){
  logger.log(INFO, "onBudPerValEdit called with {0}", evt.getObject());
  FiPeriodBalanceRec postBal = (FiPeriodBalanceRec)evt.getObject();
  double finalAmnt = postBal.getPeriodBudgetAmount();
  logger.log(INFO, "Total pre update {0} pre amnt {1} post amnt {2}", new Object[]
  {annualTotal,preEditAmnt,finalAmnt});
  annualTotal -= preEditAmnt;
  annualTotal += finalAmnt;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("annTotal");
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
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("budTblId");
  }
  
 }
 public void onGlAcntBudSave(){
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
  this.glAcntSel.setPeriodBalances(budgetList);
  try{
   glAcntMgr.updateGlAcntBugets(glAcntSel, getView());
   MessageUtil.addInfoMessage("budSaved", "blacResponse");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("glActBud");
  }catch(Exception ex){
   logger.log(INFO, "could not save budget {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("budSaveNone", "errorText");
  }
 }
}