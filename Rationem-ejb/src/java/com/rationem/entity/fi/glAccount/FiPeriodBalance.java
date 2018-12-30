/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.glAccount;

import com.rationem.entity.audit.AuditPeriodBalance;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.user.User;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.ProgramAccount;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Date;
import javax.persistence.Temporal;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.UniqueConstraint;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @author Chris
 */
@Entity

@Table(name="fi_account05", uniqueConstraints = @UniqueConstraint(columnNames =  {
    "balance_type", "account_id", "restricted_fund_id", "balance_year","balance_period"         } ) )
@NamedQueries({
@NamedQuery(name ="getFundBalForGlAcc",
 query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp.id = :glid and bal.restrictedFund.id = :fundId"),
@NamedQuery(name ="getGlAccPerBal",
 query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp.id = :glAcId and bal.balYear = :fisYear "
        + "and bal.balPeriod = :fisPer and bal.balanceType = :balType" ),
@NamedQuery(name ="getCostActPerBal",
 query="select bal from FiPeriodBalance bal where bal.costAccountActual = :costAc and bal.balYear = :fisYear "
        + "and bal.balPeriod = :fisPer and bal.balanceType = :balType" ),
@NamedQuery(name ="getProgActPerBal",
 query="select bal from FiPeriodBalance bal where bal.programCostAccount = :progAc and bal.balYear = :fisYear "
        + "and bal.balPeriod = :fisPer and bal.balanceType = :balType" ),
@NamedQuery(name ="getGlAccRestrPerBal",
 query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp = :glAc and bal.balYear = :fisYear "
        + "and bal.balPeriod = :fisPer and bal.balanceType = :balType and "
        + "bal.restrictedFund = :fnd" ),
@NamedQuery(name="fiGlActBalsForYr", 
  query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp.company.id = :compId"
        + " and bal.balYear = :yr and bal.balanceType = 0"),
@NamedQuery(name="fiGlBalYrs", 
  query="select distinct bal.balYear from FiPeriodBalance bal where bal.fiGlAccountComp.id = :acntId"
        + "  and bal.balanceType = :balType "),
@NamedQuery(name="fiGlFndBalYrs", 
  query="select distinct bal.balYear from FiPeriodBalance bal where bal.fiGlAccountComp.id = :acntId "
        + " and bal.restrictedFund.id = :fndId  and bal.balanceType = 1 "),
@NamedQuery(name="fiGlAcntFndBalsForYr", 
  query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp.id = :acntId   "
        + " and bal.restrictedFund.id = :fndId and bal.balYear = :yr and bal.balanceType = 1 "),
@NamedQuery(name="fiGlAcntBalsForYr", 
  query="select bal from FiPeriodBalance bal where bal.fiGlAccountComp.id = :acntId  "
        + " and bal.balYear = :yr and bal.balanceType = :balTy ")
})        
@SequenceGenerator(name = "fiPeriodBalance_s1", sequenceName = "fi_account05_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class FiPeriodBalance implements Serializable {
 private static final long serialVersionUID = 1L;
 public static final int GL_ACT = 0;
 public static final int RESTRICTED_ACT = 1;
 public static final int GL_BUDGET = 2;
 public static final int RESTRICTED_BUDGET = 3;
 public static final int CC_ACT = 4;
 public static final int CC_BUDGET = 5;
 public static final int PROG_ACT = 6;
 public static final int PROG_BUDGET = 7;
 
 @Id
 @GeneratedValue(generator = "restrictedFund_s1")
 @Column(name="per_bal_id")
    private Long id;
    @Column(name="balance_year")
    private int balYear;
    @Column(name="balance_period")
    private int balPeriod;
    /**
     * Balance type: 0 = actual general ledger, 1, restrictedActual , 
     * 2 = budget general ledger, 3= budget restricted
     * 4 = cost centre account actual, 5 = cost centre account budget
     * 6 = program account actual, 7 = program budget
     */
    @Column(name="balance_type")
    private int balanceType;
    @ManyToOne()
    @JoinColumn(name="account_id", referencedColumnName="fi_comp_gl_account_id" )
    private FiGlAccountComp fiGlAccountComp;
    @ManyToOne
    @JoinColumn(name="restricted_fund_id", referencedColumnName="restricted_fund_id")
    private Fund restrictedFund;
    @Column(name="amount_fwd_local")
    private double bfwdLocalAmount;
    @Column(name="amount_fwd_doc")
    private double bfwdDocAmount;
    @Column(name="period_amount_local")
    private double periodLocalAmount;
    @Column(name="period_amount_doc")
    private double periodDocAmount;
    @Column(name="priod_budget")
    private double periodBudgetAmount;
    @Column(name="period_credit_amount_doc")
    private double periodCreditAmount;
    @Column(name="period_debit_amount_doc")
    private double periodDebitAmount;
    @Column(name="amount_cfwd_local")
    private double cfwdLocalAmount;
    @Column(name="amunt_cfwd_doc")
    private double cfwdDocAmount;
    
    @Temporal(TIMESTAMP)
    @Column(name="created_on")
    private Date created;
    @ManyToOne
    @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
    private User createdBy;
    @Column(name="changed_on")
    @Temporal(TIMESTAMP)
    private Date updateDate;
    @ManyToOne
    @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
    private User updateBy;
    @Version
    @Column(name="changes")
    private int revision;
    
  @OneToMany(mappedBy = "debitBalance")
  private List<DocLineGl> debitDocLines;
  @OneToMany(mappedBy = "creditBalance")
  private List<DocLineGl> creditDocLines;
  @OneToMany(mappedBy = "restrictedDebitBalance")
  private List<DocLineGl> restrictedDebitDocLines;
  @OneToMany(mappedBy = "restrictedCreditBalance")
  private List<DocLineGl> restrictedCreditDocLines;
  
  /*@ManyToOne
  @JoinColumn(name=)
  private FiGlAccountComp restrictedBalForGlAcccount;
   *
*/
  /**
   * Restricted fund balance is attached to GL balance
   */
  @ManyToOne
  @JoinColumn(name="gl_per_bal_id",referencedColumnName="per_bal_id")
  private FiPeriodBalance glPerBal;
/**
 * Restricted funds attached to this GL account period balance
 */
 @OneToMany(mappedBy = "glPerBal")
 private List<FiPeriodBalance> restrPerBals;
 /**
  * Actual cost balance
  */ 
 
 @ManyToOne
 @JoinColumn(name="COST_ACNT_ACT_ID", referencedColumnName="COST_ACCNT_ID")
 private CostAccountDirect costAccountActual;
 
 @ManyToOne
 @JoinColumn(name="COST_ACNT_BUDGET_ID", referencedColumnName="COST_ACCNT_ID")
 private CostAccountDirect costAccountBudget;
 
 @ManyToOne
 @JoinColumn(name="PROG_ACCNT_ID", referencedColumnName="PROG_ACCNT_ID")
 private ProgramAccount programCostAccount;
 
 @ManyToOne
 @JoinColumn(name="PROG_ACCNT_BUDGET_ID", referencedColumnName="PROG_ACCNT_ID")
 private ProgramAccount programBudgetAccount;
 
 @OneToMany(mappedBy = "costAcBalance")
 private List<DocLineGl> costDocLines;
 @OneToMany(mappedBy = "costAcDebtBalance")
 private List<DocLineGl> costDebitDocLines;
 @OneToMany(mappedBy = "costCreditBalance")
 private List<DocLineGl> costCreditLines;
 @OneToMany(mappedBy = "projectBalance")
 private List<DocLineGl> projectDocLines;
 @OneToMany(mappedBy = "projectDebtBalance")
 private List<DocLineGl> projectDebitDocLines;
 @OneToMany(mappedBy = "projectCreditBalance")
 private List<DocLineGl> projectCreditDocLines;
 @OneToMany(mappedBy = "perBal")
 private List<AuditPeriodBalance> auditRecords;
 
 



    public FiPeriodBalance() {
    }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public int getYear() {
  return balYear;
 }
 
 public void setYear(int year) {
   this.balYear = year;
 }
 public int getBalYear() {
  return balYear;
 }

 public void setBalYear(int balYear) {
  this.balYear = balYear;
 }

 public int getBalPeriod() {
  return balPeriod;
 }

 public void setBalPeriod(int balPeriod) {
  this.balPeriod = balPeriod;
 }

 public int getPeriod() {
  return balPeriod;
 }

 public void setPeriod(int period) {
  this.balPeriod = period;
 }
 
 public int getBalanceType() {
  return balanceType;
 }

 public void setBalanceType(int balanceType) {
  this.balanceType = balanceType;
 }

 public FiGlAccountComp getFiGlAccountComp() {
  return fiGlAccountComp;
 }

 public void setFiGlAccountComp(FiGlAccountComp fiGlAccountComp) {
  this.fiGlAccountComp = fiGlAccountComp;
 }

 public Fund getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(Fund restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public double getBfwdLocalAmount() {
  return bfwdLocalAmount;
 }

 public void setBfwdLocalAmount(double bfwdLocalAmount) {
  this.bfwdLocalAmount = bfwdLocalAmount;
 }

 public double getBfwdDocAmount() {
  return bfwdDocAmount;
 }

 public void setBfwdDocAmount(double bfwdDocAmount) {
  this.bfwdDocAmount = bfwdDocAmount;
 }

 public double getPeriodLocalAmount() {
  return periodLocalAmount;
 }

 public void setPeriodLocalAmount(double periodLocalAmount) {
  this.periodLocalAmount = periodLocalAmount;
 }

 public double getPeriodDocAmount() {
  return periodDocAmount;
 }

 public void setPeriodDocAmount(double periodDocAmount) {
  this.periodDocAmount = periodDocAmount;
 }

 public double getPeriodBudgetAmount() {
  return periodBudgetAmount;
 }

 public void setPeriodBudgetAmount(double periodBudgetAmount) {
  this.periodBudgetAmount = periodBudgetAmount;
 }

 public double getPeriodCreditAmount() {
  return periodCreditAmount;
 }

 public void setPeriodCreditAmount(double periodCreditAmount) {
  this.periodCreditAmount = periodCreditAmount;
 }

 public double getPeriodDebitAmount() {
  return periodDebitAmount;
 }

 public void setPeriodDebitAmount(double periodDebitAmount) {
  this.periodDebitAmount = periodDebitAmount;
 }

 public CostAccountDirect getCostAccountActual() {
  return costAccountActual;
 }

 public void setCostAccountActual(CostAccountDirect costAccountActual) {
  this.costAccountActual = costAccountActual;
 }

 public ProgramAccount getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccount programCostAccount) {
  this.programCostAccount = programCostAccount;
 }

 public ProgramAccount getProgramBudgetAccount() {
  return programBudgetAccount;
 }

 public void setProgramBudgetAccount(ProgramAccount programBudgetAccount) {
  this.programBudgetAccount = programBudgetAccount;
 }

 public double getCfwdLocalAmount() {
  return cfwdLocalAmount;
 }

 public void setCfwdLocalAmount(double cfwdLocalAmount) {
  this.cfwdLocalAmount = cfwdLocalAmount;
 }

 public double getCfwdDocAmount() {
  return cfwdDocAmount;
 }

 public void setCfwdDocAmount(double cfwdDocAmount) {
  this.cfwdDocAmount = cfwdDocAmount;
 }

 public CostAccountDirect getCostAccountBudget() {
  return costAccountBudget;
 }

 public void setCostAccountBudget(CostAccountDirect costAccountBudget) {
  this.costAccountBudget = costAccountBudget;
 }

 public Date getCreated() {
  return created;
 }

 public void setCreated(Date created) {
  this.created = created;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public User getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(User updateBy) {
  this.updateBy = updateBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public FiPeriodBalance getGlPerBal() {
  return glPerBal;
 }

 public void setGlPerBal(FiPeriodBalance glPerBal) {
  this.glPerBal = glPerBal;
 }

 public List<FiPeriodBalance> getRestrPerBals() {
  return restrPerBals;
 }

 public void setRestrPerBals(List<FiPeriodBalance> restrPerBals) {
  this.restrPerBals = restrPerBals;
 }

 // updates the balance amounts for glLine
 public void updateActualBalance(DocLineGl glLine){
  double docAmount = glLine.getDocAmount();
  double localAmount = glLine.getHomeAmount();
  if(localAmount == 0){
   localAmount = docAmount;
  }
  if(glLine.getPostType().isDebit()){
   // debit line
   periodDocAmount = periodDocAmount + docAmount;
   periodLocalAmount = periodLocalAmount + localAmount;
   periodDebitAmount = periodDebitAmount + docAmount;
   
  }else{
   //this is a credit entry
   periodDocAmount = periodDocAmount - docAmount;
   periodLocalAmount = periodLocalAmount - localAmount;
   periodCreditAmount = periodCreditAmount + docAmount;
   
  }
  cfwdDocAmount = this.bfwdDocAmount + periodDocAmount;
  cfwdLocalAmount = cfwdLocalAmount + periodLocalAmount;
  
 } 

 public List<DocLineGl> getDebitDocLines() {
  return debitDocLines;
 }

 public void setDebitDocLines(List<DocLineGl> debitDocLines) {
  this.debitDocLines = debitDocLines;
 }

 public List<DocLineGl> getCreditDocLines() {
  return creditDocLines;
 }

 public void setCreditDocLines(List<DocLineGl> creditDocLines) {
  this.creditDocLines = creditDocLines;
 }

 public List<DocLineGl> getRestrictedDebitDocLines() {
  return restrictedDebitDocLines;
 }

 public void setRestrictedDebitDocLines(List<DocLineGl> restrictedDebitDocLines) {
  this.restrictedDebitDocLines = restrictedDebitDocLines;
 }

 public List<DocLineGl> getRestrictedCreditDocLines() {
  return restrictedCreditDocLines;
 }

 public void setRestrictedCreditDocLines(List<DocLineGl> restrictedCreditDocLines) {
  this.restrictedCreditDocLines = restrictedCreditDocLines;
 }

 public List<DocLineGl> getCostDocLines() {
  return costDocLines;
 }

 public void setCostDocLines(List<DocLineGl> costDocLines) {
  this.costDocLines = costDocLines;
 }

 public List<DocLineGl> getCostDebitDocLines() {
  return costDebitDocLines;
 }

 public void setCostDebitDocLines(List<DocLineGl> costDebitDocLines) {
  this.costDebitDocLines = costDebitDocLines;
 }

 public List<DocLineGl> getCostCreditLines() {
  return costCreditLines;
 }

 public void setCostCreditLines(List<DocLineGl> costCreditLines) {
  this.costCreditLines = costCreditLines;
 }

 public List<DocLineGl> getProjectDocLines() {
  return projectDocLines;
 }

 public void setProjectDocLines(List<DocLineGl> projectDocLines) {
  this.projectDocLines = projectDocLines;
 }

 public List<DocLineGl> getProjectDebitDocLines() {
  return projectDebitDocLines;
 }

 public void setProjectDebitDocLines(List<DocLineGl> projectDebitDocLines) {
  this.projectDebitDocLines = projectDebitDocLines;
 }

 public List<DocLineGl> getProjectCreditDocLines() {
  return projectCreditDocLines;
 }

 public void setProjectCreditDocLines(List<DocLineGl> projectCreditDocLines) {
  this.projectCreditDocLines = projectCreditDocLines;
 }

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof FiPeriodBalance)) {
   return false;
  }
  FiPeriodBalance other = (FiPeriodBalance) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.glAccount.FiPeriodBalance[ id=" + id + " ]";
 }
 
}
