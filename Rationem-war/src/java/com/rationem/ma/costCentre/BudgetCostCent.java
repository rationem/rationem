/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ma.costCentre;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.AnnualBudget;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 *
 * @author user
 */
public class BudgetCostCent extends BaseBean {
 private static final Logger logger = Logger.getLogger(BudgetCostCent.class.getName());
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private CostCentreMgr ccMgr;
 private CompanyBasicRec compSel;
 
 private List<AnnualBudget> annBudgets; 
 private double annualTotal;
 
 private List<CostCentreRec> costCentList;
 private CostCentreRec costCentSel;
 private List<CostAccountDirectRec> costAcntList;
 private CostAccountDirectRec costAcnt;
 private FiPeriodBalanceRec budget;
 private List<FiPeriodBalanceRec> budgetList;
 private List<Integer> budgetYears;
 private String budYearNew;
 private int budYearSel;
 
 private int numPeriods;
private double preEditAmnt;
 /**
  * Creates a new instance of BudgetCostCent
  */
 public BudgetCostCent() {
 }

 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  numPeriods = 12;
  compSel = this.getCompList().get(0);
  
  costCentList = this.sysBuff.getCostCentForComp(compSel.getId());
  logger.log(INFO, "costCentList {0}", costCentList);
  if(costCentList != null && !costCentList.isEmpty()){
   costCentSel = costCentList.get(0);
   costAcntList = ccMgr.getCostAccountsByCostCent(costCentSel);
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

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
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

 public List<CostAccountDirectRec> getCostAcntList() {
  return costAcntList;
 }

 public void setCostAcntList(List<CostAccountDirectRec> costAcntList) {
  this.costAcntList = costAcntList;
 }

 public CostAccountDirectRec getCostAcnt() {
  return costAcnt;
 }

 public void setCostAcnt(CostAccountDirectRec costAcnt) {
  this.costAcnt = costAcnt;
 }

 public List<CostCentreRec> getCostCentList() {
  return costCentList;
 }

 public void setCostCentList(List<CostCentreRec> costCentList) {
  this.costCentList = costCentList;
 }

 public CostCentreRec getCostCentSel() {
  return costCentSel;
 }

 public void setCostCentSel(CostCentreRec costCentSel) {
  this.costCentSel = costCentSel;
 }

 
 public int getNumPeriods() {
  return numPeriods;
 }

 public void setNumPeriods(int numPeriods) {
  this.numPeriods = numPeriods;
 }

 public double getPreEditAmnt() {
  return preEditAmnt;
 }

 public void setPreEditAmnt(double preEditAmnt) {
  this.preEditAmnt = preEditAmnt;
 }
 
 public void onBudSpread(){
  
 }
 public void onCostCentBudSave(){
  
 }
 
 
 
}
