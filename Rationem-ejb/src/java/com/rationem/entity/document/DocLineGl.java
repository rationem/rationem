/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.ma.ProgramAccount;
import com.rationem.entity.ma.Programme;
import com.rationem.entity.sales.SalesPartCompany;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import com.rationem.entity.salesTax.vat.VatReturnLine;

import static javax.persistence.InheritanceType.JOINED;

/**
 *
 * @author Chris
 */
@Entity
@NamedQueries({
 @NamedQuery(name="glReconLinesForArLine", 
        query="select l from DocLineGl l where l.reconcilForArLine.id = :arLineid"),
 @NamedQuery(name="glReconLinesForApLine", 
        query="select l from DocLineGl l where l.reconGlLnForApLine.id = :apLineid")
})
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocLineGL")
@PrimaryKeyJoinColumn(name="doc_line_id",referencedColumnName = "doc_line_id")
@Table(name="doc_line02" )
public class DocLineGl extends DocLineBase implements Serializable {
 @ManyToOne
 @JoinColumn(name="ar_doc_line_id", referencedColumnName="doc_line_id")
 private DocLineAr reconcilForArLine;
 @ManyToOne
 @JoinColumn(name="ap_doc_line_id", referencedColumnName="doc_line_id")
 private DocLineAp reconGlLnForApLine;
 @Column(name="curr_id")
 private int currID;
 @Column(name="doc_amount")
 private double docAmount;
 @ManyToOne
 @JoinColumn(name="doc_fi_id", referencedColumnName="doc_id")
 private DocFi docFi;
 @ManyToOne
 @JoinColumn(name="gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp GlAccount;
 @ManyToOne
 @JoinColumn(name="debit_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance debitBalance;
 @ManyToOne
 @JoinColumn(name="credit_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance creditBalance;
 @ManyToOne
 @JoinColumn(name="rest_debit_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance restrictedDebitBalance;
 @ManyToOne
 @JoinColumn(name="rest_credit_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance restrictedCreditBalance;
 @ManyToOne
 @JoinColumn(name="rest_fnd_id", referencedColumnName="restricted_fund_id")
 private Fund restrictedFund;
 
 @ManyToOne
 @JoinColumn(name="COST_CENTRE_ID", referencedColumnName="COST_CENT_ID")
 private CostCentre costCentre;
 
 
 @ManyToOne
 @JoinColumn(name="COST_ACNT_ACT_ID", referencedColumnName="COST_ACCNT_ID")
 private CostAccountDirect costAccount;
 
 @ManyToOne
 @JoinColumn(name="cost_ac_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance costAcBalance;
 
 @ManyToOne
 @JoinColumn(name="cost_ac_dr_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance costAcDebtBalance;
 @ManyToOne
 @JoinColumn(name="cost_ac_cr_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance costCreditBalance;
 @ManyToOne
 @JoinColumn(name="proj_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance projectBalance;
 @ManyToOne
 @JoinColumn(name="proj_dr_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance projectDebtBalance;
 @ManyToOne
 @JoinColumn(name="proj_cr_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance projectCreditBalance;

 
 @ManyToOne
 @JoinColumn(name="PROGRAMME_ID", referencedColumnName="PROGRAMME_ID")
 private Programme programme;
  
 
 @ManyToOne
 @JoinColumn(name="PROG_ACCNT_ID", referencedColumnName="PROG_ACCNT_ID")
 private ProgramAccount programCostAccount;
 
 
 @ManyToOne
 @JoinColumn(name="sales_part_id", referencedColumnName="sale_part_comp_id")
 private SalesPartCompany salesPart;
 
 
 @ManyToOne
 @JoinColumn(name="comp_vat_code_id" ,referencedColumnName="vat_code_comp_id" )
 private VatCodeCompany vatCode;
 
 
 @OneToOne(mappedBy = "expenseDocLine")
 private VatReturnLine vatReturnLine;
 
 public DocLineAr getReconcilForArLine() {
  return reconcilForArLine;
 }

 public void setReconcilForArLine(DocLineAr reconcilForArLine) {
  this.reconcilForArLine = reconcilForArLine;
 }

 public DocLineAp getReconGlLnForApLine() {
  return reconGlLnForApLine;
 }

 public void setReconGlLnForApLine(DocLineAp reconGlLnForApLine) {
  this.reconGlLnForApLine = reconGlLnForApLine;
 }

 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public DocFi getDocFi() {
  return docFi;
 }

 public void setDocFi(DocFi docFi) {
  this.docFi = docFi;
 }

 
 public FiGlAccountComp getGlAccount() {
  return GlAccount;
 }

 public void setGlAccount(FiGlAccountComp GlAccount) {
  this.GlAccount = GlAccount;
 }

 public FiPeriodBalance getDebitBalance() {
  return debitBalance;
 }

 public void setDebitBalance(FiPeriodBalance debitBalance) {
  this.debitBalance = debitBalance;
 }

 public FiPeriodBalance getCreditBalance() {
  return creditBalance;
 }

 public void setCreditBalance(FiPeriodBalance creditBalance) {
  this.creditBalance = creditBalance;
 }

 public FiPeriodBalance getRestrictedDebitBalance() {
  return restrictedDebitBalance;
 }

 public void setRestrictedDebitBalance(FiPeriodBalance restrictedDebitBalance) {
  this.restrictedDebitBalance = restrictedDebitBalance;
 }

 public FiPeriodBalance getRestrictedCreditBalance() {
  return restrictedCreditBalance;
 }

 public void setRestrictedCreditBalance(FiPeriodBalance restrictedCreditBalance) {
  this.restrictedCreditBalance = restrictedCreditBalance;
 }

 public Fund getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(Fund restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public CostAccountDirect getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirect costAccount) {
  this.costAccount = costAccount;
 }

 public FiPeriodBalance getCostAcBalance() {
  return costAcBalance;
 }

 public void setCostAcBalance(FiPeriodBalance costAcBalance) {
  this.costAcBalance = costAcBalance;
 }

 public FiPeriodBalance getCostAcDebtBalance() {
  return costAcDebtBalance;
 }

 public void setCostAcDebtBalance(FiPeriodBalance costAcDebtBalance) {
  this.costAcDebtBalance = costAcDebtBalance;
 }

 public CostCentre getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentre costCentre) {
  this.costCentre = costCentre;
 }

 public FiPeriodBalance getCostCreditBalance() {
  return costCreditBalance;
 }

 public void setCostCreditBalance(FiPeriodBalance costCreditBalance) {
  this.costCreditBalance = costCreditBalance;
 }

 public Programme getProgramme() {
  return programme;
 }

 public void setProgramme(Programme programme) {
  this.programme = programme;
 }

 public ProgramAccount getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccount programCostAccount) {
  this.programCostAccount = programCostAccount;
 }

 
 public FiPeriodBalance getProjectBalance() {
  return projectBalance;
 }

 public void setProjectBalance(FiPeriodBalance projectBalance) {
  this.projectBalance = projectBalance;
 }

 public FiPeriodBalance getProjectDebtBalance() {
  return projectDebtBalance;
 }

 public void setProjectDebtBalance(FiPeriodBalance projectDebtBalance) {
  this.projectDebtBalance = projectDebtBalance;
 }

 public FiPeriodBalance getProjectCreditBalance() {
  return projectCreditBalance;
 }

 public void setProjectCreditBalance(FiPeriodBalance projectCreditBalance) {
  this.projectCreditBalance = projectCreditBalance;
 }

 public SalesPartCompany getSalesPart() {
  return salesPart;
 }

 public void setSalesPart(SalesPartCompany salesPart) {
  this.salesPart = salesPart;
 }

 public void setSalesPartCompany(SalesPartCompany salesPart) {
  this.salesPart = salesPart;
 }
 public VatReturnLine getVatReturnLine() {
  return vatReturnLine;
 }

 public void setVatReturnLine(VatReturnLine vatReturnLine) {
  this.vatReturnLine = vatReturnLine;
 }

 public VatCodeCompany getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompany vatCode) {
  this.vatCode = vatCode;
 }

 
}
