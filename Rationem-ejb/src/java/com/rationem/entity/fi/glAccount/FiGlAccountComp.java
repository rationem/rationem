/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.glAccount;

import com.rationem.busRec.fi.company.FundRec;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.tr.bank.BankAccountCompany;
import java.util.ArrayList;
import java.util.List;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.io.Serializable;
import java.util.Date;
import java.util.ListIterator;
import java.util.Objects;
import javax.persistence.*;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.CascadeType.PERSIST;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account04", uniqueConstraints= @UniqueConstraint(columnNames={"comp_id", "fi_account_id"})) 
@NamedQueries({
 @NamedQuery(name = "GlCompAccountsAll",query = "SELECT gl.id "
    + "FROM FiGlAccountComp gl  "),
 
@NamedQuery(name = "GlAccountCompByCompId", 
  query ="SELECT gl FROM FiGlAccountComp gl where gl.company.id = :compId "),
@NamedQuery(name = "glCompAcsForCoaAcnt", 
  query = "Select gl  FROM FiGlAccountComp gl where gl.coaAccount.id = :comActId "),
@NamedQuery(name = "GlCompAcnt", query =
"Select gl  FROM FiGlAccountComp gl where gl.company.id = :compId and gl.coaAccount.ref = :acntRef "),
@NamedQuery(name = "GlCompAcntaAllForBnkClr", query =
"Select gl  FROM FiGlAccountComp gl where gl.company.id = :compId and gl.bankAccountCompanyUncleared.id = :bnkAcntId"),
@NamedQuery(name = "GlCompAcntsAcntTyp", query =
"Select gl  FROM FiGlAccountComp gl where gl.company.id = :compId and gl.coaAccount.accountType.id = :acntTyId")

})
@SequenceGenerator(name = "fiGlAccountComp_s1", sequenceName = "fi_account04_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class FiGlAccountComp implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "fiGlAccountComp_s1")
 @Column(name="fi_comp_gl_account_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="fi_account_id", referencedColumnName="fi_gl_account_id")
 private FiGLAccountBase coaAccount;
 @ManyToOne()
 @JoinColumn(name="comp_id",  referencedColumnName="home_comp_id")
 private CompanyBasic company;
 @OneToMany(mappedBy = "fiGlAccountComp", orphanRemoval=true, cascade=PERSIST)
 private List<FiPeriodBalance> periodBalances;
 @OneToMany(mappedBy = "fiGlAccountComp", orphanRemoval=true, cascade=PERSIST)
 private List<FiPeriodBalance> restrictedBalances;
 @ManyToOne
 @JoinColumn(name="created_by_id",  referencedColumnName="partner_id")
  private User createdBy;
  @Temporal(TIMESTAMP)
  @Column(name="created_on")
  private Date createdOn;
  @ManyToOne
  @JoinColumn(name="changed_by_id",  referencedColumnName="partner_id")
  private User changedBy;
  @Temporal(TIMESTAMP)
  @Column(name="changed_on")
  private Date changedOn;

  @ManyToOne
  @JoinColumn(name="sort_order_id",  referencedColumnName="sort_order_id")
  private SortOrder sortOrder;
  /**
   * A bank account can have many uncleared GL accounts 
   */
  
  
  @Column(name="analysis1")
  private String analysis1;
  @Column(name="analysis2")
  private String analysis2;
  @Column(name="report_cat")
  private String repCategory;
  
  @Column(name="vat_status")
  private int vatStatus;
  @OneToOne
  @JoinColumn(name="comp_vat_code_id",  referencedColumnName="vat_code_comp_id")
  private VatCodeCompany vatCode;
  
  @Column(name="no_vat_code_permitted")
  private boolean noVatAllowed;
  @Column(name="act_clearing")
  private boolean accountClearing;
  @Column(name="resp_person")
  private String respPerson;
  
   //@OneToMany(mappedBy = "glBankAccount")
  @OneToMany(mappedBy = "glAccount",orphanRemoval=true, cascade=PERSIST)
  private List<PaymentType> paymentTypes;
  @Version
  @Column(name="changes")
  private int revision;
  
  /*
   @OneToMany(mappedBy = "compAccount")
  private List<AuditGlCompAccount> auditGlCompAccounts;
  
 @OneToMany(mappedBy = "glAccount")
 private List<CostAccountDirect> costAccounts;
 @OneToMany(mappedBy = "reconciliationAc")
 private List<ArAccount> arAccounts;
 */
 @OneToOne(mappedBy = "clearedGlAccount",orphanRemoval=true, cascade=PERSIST)
 private BankAccountCompany bankAccountCompanyCleared;
 
 @ManyToOne
 @JoinColumn(name="BANK_AC_UNCL_ID", referencedColumnName="BANK_ACCOUNT_ID" )
 private BankAccountCompany bankAccountCompanyUncleared;
 /*
 @OneToMany(mappedBy = "reconciliationAc")
 private List<ApAccount> apAccounts;
 */
 @ManyToOne
 @JoinColumn(name="BANK_AC_ID", referencedColumnName="BANK_ACCOUNT_ID" )
 private BankAccountCompany bankAccount;
 
 

  public FiGlAccountComp() {
  }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public FiGLAccountBase getCoaAccount() {
  return coaAccount;
 }

 public void setCoaAccount(FiGLAccountBase coaAccount) {
  this.coaAccount = coaAccount;
 }

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public SortOrder getSortOrder() {
  return sortOrder;
 }

 public void setSortOrder(SortOrder sortOrder) {
  this.sortOrder = sortOrder;
 }

 public String getAnalysis1() {
  return analysis1;
 }

 public void setAnalysis1(String analysis1) {
  this.analysis1 = analysis1;
 }

 public String getAnalysis2() {
  return analysis2;
 }

 public void setAnalysis2(String analysis2) {
  this.analysis2 = analysis2;
 }

 public BankAccountCompany getBankAccountCompanyCleared() {
  return bankAccountCompanyCleared;
 }

 public void setBankAccountCompanyCleared(BankAccountCompany bankAccountCompanyCleared) {
  this.bankAccountCompanyCleared = bankAccountCompanyCleared;
 }

 public BankAccountCompany getBankAccountCompanyUncleared() {
  return bankAccountCompanyUncleared;
 }

 public void setBankAccountCompanyUncleared(BankAccountCompany bankAccountCompanyUncleared) {
  this.bankAccountCompanyUncleared = bankAccountCompanyUncleared;
 }

 
 public String getRepCategory() {
  return repCategory;
 }

 public void setRepCategory(String repCategory) {
  this.repCategory = repCategory;
 }

 public boolean isNoVatAllowed() {
  return noVatAllowed;
 }

 public void setNoVatAllowed(boolean noVatAllowed) {
  this.noVatAllowed = noVatAllowed;
 }

 public boolean isAccountClearing() {
  return accountClearing;
 }

 public void setAccountClearing(boolean accountClearing) {
  this.accountClearing = accountClearing;
 }

 public String getRespPerson() {
  return respPerson;
 }

 public void setRespPerson(String respPerson) {
  this.respPerson = respPerson;
 }

 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public List<FiPeriodBalance> getPeriodBalances() {
  return periodBalances;
 }

 public void setPeriodBalances(List<FiPeriodBalance> periodBalances) {
  this.periodBalances = periodBalances;
 }

 public List<FiPeriodBalance> getRestrictedBalances() {
  return restrictedBalances;
 }

 public FiPeriodBalance getRestrictedBalance(int year, int period, FundRec fnd) throws BacException{
   if(restrictedBalances == null){
    throw new BacException("No restricted balances");
   }
   
   ListIterator<FiPeriodBalance> restrBalLi = restrictedBalances.listIterator();
   while(restrBalLi.hasNext()){
    FiPeriodBalance restrBal = restrBalLi.next();
    if(restrBal.getBalPeriod() == period && restrBal.getBalYear() == year &&
       restrBal.getRestrictedFund() != null &&  
       Objects.equals(restrBal.getRestrictedFund().getId(), fnd.getId())){
     return restrBal;
    }
   }
   throw new BacException("Could not find restricted balance");
   
  }

 public void setRestrictedBalances(List<FiPeriodBalance> restrictedBalances) {
  this.restrictedBalances = restrictedBalances;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public VatCodeCompany getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompany vatCode) {
  this.vatCode = vatCode;
 }

 public int getVatStatus() {
  return vatStatus;
 }

 public void setVatStatus(int vatStatus) {
  this.vatStatus = vatStatus;
 }
 
 

public FiPeriodBalance getActualPeriodBalance(int year, int period) throws BacException{
   ListIterator<FiPeriodBalance> balLi = periodBalances.listIterator();
   while(balLi.hasNext()){
    FiPeriodBalance perBal = balLi.next();
    if(perBal.getBalYear() == year && perBal.getBalPeriod() == period){
     return perBal;
    }
   }
   throw new BacException("Period Balance not found");
  }

public void addActualBalance(FiPeriodBalance actBal){
   if(periodBalances == null){
    periodBalances = new ArrayList<>();
   }
   periodBalances.add(actBal);
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
  if (!(object instanceof FiGlAccountComp)) {
   return false;
  }
  FiGlAccountComp other = (FiGlAccountComp) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.glAccount.FiGlAccountComp[ id=" + id + " ]";
 }
 
}
