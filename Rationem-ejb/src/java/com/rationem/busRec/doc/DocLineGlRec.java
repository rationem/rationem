/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.doc;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatReturnLineRec;


import java.util.logging.Logger;


import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class DocLineGlRec extends DocLineBaseRec {
 private static final Logger LOGGER =   Logger.getLogger(DocLineGlRec.class.getName());
 private int currID;
 private double docAmount;
 private DocFiRec docFi;
 private FiGlAccountCompRec glAccount;
 private FiPeriodBalanceRec debitBalance;
 private FiPeriodBalanceRec creditBalance;
 private FiPeriodBalanceRec restrictedDebitBalance;
 private FiPeriodBalanceRec restrictedCreditBalance;
 private FundRec restrictedFund;
 private CostCentreRec costCentre;
 private CostAccountDirectRec costAccount;
 private FiPeriodBalanceRec costAcBalance;
 private FiPeriodBalanceRec costAcDebtBalance;
 private FiPeriodBalanceRec costCreditBalance;
 private ProgramAccountRec programeAccount;
 private ProgrammeRec programme;
 private FiPeriodBalanceRec projectBalance;
 private FiPeriodBalanceRec projectDebtBalance;
 private FiPeriodBalanceRec projectCreditBalance;
 private DocLineApRec reconcilForApLine;
 private DocLineArRec reconcilForArLine;
 private SalesPartCompanyRec salesPart;
 private VatReturnLineRec vatReturnLine;
 private VatCodeCompanyRec vatCode;
 
 public DocLineGlRec() {
 }

 @Override
 public String getAccountRef(){
  LOGGER.log(INFO,"DocLineGL get account ref called FiGlAccountCompRec - glAccount id {0}",glAccount);
  if(glAccount == null){
   return null;
  }
  LOGGER.log(INFO,"GlAccount.getCoaAccount() {0}",glAccount.getCoaAccount());
  String acNum = glAccount.getCoaAccount().getRef();
  
  return acNum;
 }
 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }
 
 @Override
 public double getDocAmount() {
  return docAmount;
 }

 @Override
 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public DocFiRec getDocFi() {
  return docFi;
 }

 public void setDocFi(DocFiRec docFi) {
  this.docFi = docFi;
 }

 
 public FiGlAccountCompRec getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountCompRec GlAccount) {
  this.glAccount = GlAccount;
 }

 public FiPeriodBalanceRec getDebitBalance() {
  return debitBalance;
 }

 public void setDebitBalance(FiPeriodBalanceRec debitBalance) {
  this.debitBalance = debitBalance;
 }

 public FiPeriodBalanceRec getCreditBalance() {
  return creditBalance;
 }

 public void setCreditBalance(FiPeriodBalanceRec creditBalance) {
  this.creditBalance = creditBalance;
 }

 public FiPeriodBalanceRec getRestrictedDebitBalance() {
  return restrictedDebitBalance;
 }

 public void setRestrictedDebitBalance(FiPeriodBalanceRec restrictedDebitBalance) {
  this.restrictedDebitBalance = restrictedDebitBalance;
 }

 public FiPeriodBalanceRec getRestrictedCreditBalance() {
  return restrictedCreditBalance;
 }

 public void setRestrictedCreditBalance(FiPeriodBalanceRec restrictedCreditBalance) {
  this.restrictedCreditBalance = restrictedCreditBalance;
 }

 public FundRec getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(FundRec restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public SalesPartCompanyRec getSalesPart() {
  return salesPart;
 }

 public void setSalesPart(SalesPartCompanyRec salesPart) {
  this.salesPart = salesPart;
 }

 public CostCentreRec getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentreRec costCentre) {
  this.costCentre = costCentre;
 }

 public FiPeriodBalanceRec getCostAcBalance() {
  return costAcBalance;
 }

 public void setCostAcBalance(FiPeriodBalanceRec costAcBalance) {
  this.costAcBalance = costAcBalance;
 }

 public FiPeriodBalanceRec getCostAcDebtBalance() {
  return costAcDebtBalance;
 }

 public void setCostAcDebtBalance(FiPeriodBalanceRec costAcDebtBalance) {
  this.costAcDebtBalance = costAcDebtBalance;
 }

 public CostAccountDirectRec getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirectRec costAccount) {
  this.costAccount = costAccount;
 }

 
 public FiPeriodBalanceRec getCostCreditBalance() {
  return costCreditBalance;
 }

 public void setCostCreditBalance(FiPeriodBalanceRec costCreditBalance) {
  this.costCreditBalance = costCreditBalance;
 }

 public ProgramAccountRec getProgrameAccount() {
  return programeAccount;
 }

 public void setProgrameAccount(ProgramAccountRec programeAccount) {
  this.programeAccount = programeAccount;
 }

 
 public ProgrammeRec getProgramme() {
  return programme;
 }

 public void setProgramme(ProgrammeRec programme) {
  this.programme = programme;
 }

 public FiPeriodBalanceRec getProjectBalance() {
  return projectBalance;
 }

 public void setProjectBalance(FiPeriodBalanceRec projectBalance) {
  this.projectBalance = projectBalance;
 }

 public FiPeriodBalanceRec getProjectDebtBalance() {
  return projectDebtBalance;
 }

 public void setProjectDebtBalance(FiPeriodBalanceRec projectDebtBalance) {
  this.projectDebtBalance = projectDebtBalance;
 }

 public FiPeriodBalanceRec getProjectCreditBalance() {
  return projectCreditBalance;
 }

 public void setProjectCreditBalance(FiPeriodBalanceRec projectCreditBalance) {
  this.projectCreditBalance = projectCreditBalance;
 }

 public DocLineApRec getReconcilForApLine() {
  return reconcilForApLine;
 }

 public void setReconcilForApLine(DocLineApRec reconcilForApLine) {
  this.reconcilForApLine = reconcilForApLine;
 }

 
 
 public DocLineArRec getReconcilForArLine() {
  return reconcilForArLine;
 }

 public void setReconcilForArLine(DocLineArRec reconcilForArLine) {
  this.reconcilForArLine = reconcilForArLine;
 }

 public VatCodeCompanyRec getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompanyRec vatCode) {
  this.vatCode = vatCode;
 }

 
 public VatReturnLineRec getVatReturnLine() {
  return vatReturnLine;
 }

 public void setVatReturnLine(VatReturnLineRec vatReturnLine) {
  this.vatReturnLine = vatReturnLine;
 }
 
 


}
