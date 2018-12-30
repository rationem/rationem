/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.arap;

import com.rationem.entity.cm.Contact;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.document.DocInvoiceAr;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import static javax.persistence.CascadeType.PERSIST;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.OneToMany;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account08")
@NamedQueries({
 @NamedQuery(name = "getAccountNumber",
query="Select o from ArAccount o where o.accountCode = :actCd"),
@NamedQuery(name = "getActsByActNumPart",
query="Select o from ArAccount o where o.accountCode like :actCd"),
@NamedQuery(name = "getActsByActName",
query="Select o from ArAccount o where o.accountName like :actName"),
@NamedQuery(name = "getActsByCompActNumPart",
query="Select o from ArAccount o where o.company.id = :compId and o.accountCode like :actCd"),
@NamedQuery(name = "getActsByFamilyNamePart",
query="Select o from ArAccount o where o.arAccountForPerson.familyName like :fname"),
@NamedQuery(name = "getActsByTradingNamePart",
query="Select o from ArAccount o where o.arAccountFor.tradingName like :tname"),
@NamedQuery(name = "getActsForCompany",
query="Select o from ArAccount o where o.company.id = :compId"),
@NamedQuery(name = "getArActsForPtnr",
query="Select o from ArAccount o where o.arAccountFor.id = :ptnrId")
})
@SequenceGenerator(name = "arAccount_s1", sequenceName = "fi_account08_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class ArAccount implements ArAPAccountIF  {

 @OneToMany(mappedBy = "arAccount")
 private List<DocBankLineBase> bankLines;

 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "arAccount_s1")
 @Column(name="ar_account_id")
 private Long id;
 @Column(name="ar_account_code")
 private String accountCode;
 @Column(name="ar_account_name")
 private String accountName;
 @ManyToOne
 @JoinColumn(name="comp_id",referencedColumnName="home_comp_id")
 private CompanyBasic company;
 @ManyToOne
 @JoinColumn(name="sort_order_id",referencedColumnName="sort_order_id")
 private SortOrder sortOrder;
 @ManyToOne
 @JoinColumn(name="account_address_id",referencedColumnName="address_id")
 private Address accountAddress;
 @ManyToOne
 @JoinColumn(name="inv_address_id",referencedColumnName="address_id")
 private Address invoiceAddress;
 @ManyToOne
  @JoinColumn(name="account_clerk_ptnr_id",referencedColumnName="partner_id")
  private PartnerPerson accountClerk;
  @ManyToOne
  @JoinColumn(name="account_ptnr_id",referencedColumnName="partner_id")
  private PartnerBase arAccountFor;
  @Column(name="account_ptnr_ty")
  private String arAccPtnrType;
  @ManyToOne
  @JoinColumn(name="partner_pers_id",referencedColumnName="partner_id" )
  private PartnerPerson arAccountForPerson;
  
  @ManyToOne
  @JoinColumn(name="partner_corp_id",referencedColumnName="partner_id")
  private PartnerCorporate arAccountForCorporate;
  
  @ManyToOne
  @JoinColumn(name="gl_reconciliation_acnt_id",referencedColumnName="fi_comp_gl_account_id")
  private FiGlAccountComp reconciliationAc;
  @ManyToOne
  @JoinColumn(name="pay_terms_id", referencedColumnName="pay_terms_id")
  private PaymentTerms paymentTerms;
  @ManyToOne
  @JoinColumn(name="pay_type_id",referencedColumnName="pay_type_id")
  private PaymentType paymentType;
 @OneToMany(mappedBy = "account")
 private List<DocInvoiceAr> invoices;
 @OneToMany(mappedBy = "arAccount")
 private List<Contact> contacts; 
  @Column(name="acnt_bal_doc")
  private double accountBalance;
  @Column(name="bal_bucket1")
  private double balBucket1;
  @Column(name="bal_bucket2")
  private double balBucket2;
  @Column(name="bal_bucket3")
  private double balBucket3;
  @Column(name="bal_bucket4")
  private double balBucket4;
  @Column(name="note", length=4000)
  private String acntNote;

  @Temporal(TIMESTAMP)
  @Column(name="created_on")
  private Date createdOn;
  @ManyToOne
  @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
  private User createdBy;
  @Temporal(TIMESTAMP)
  @Column(name="changed_on")
  private Date changedOn;
  @ManyToOne
  @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
  private User changedBy;
  @Version
  @Column(name="revision")
  private int changes;
  @OneToMany(mappedBy = "bankForArAccount")
  private List<ArBankAccount> arAccountBanks;
  
  
  @OneToMany(mappedBy = "arAccount")
  private List<DocLineAr> arLines;
  
  
  @OneToMany(mappedBy = "arAccount", cascade=PERSIST)
  private List<FiArPeriodBalance> arPeriodBalances;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getAccountCode() {
  return accountCode;
 }

 public void setAccountCode(String accountCode) {
  this.accountCode = accountCode;
 }

 public String getAccountName() {
  return accountName;
 }

 public void setAccountName(String accountName) {
  this.accountName = accountName;
 }

 
 public List<ArBankAccount> getArAccountBanks() {
  return arAccountBanks;
 }

 public void setArAccountBanks(List<ArBankAccount> arAccountBanks) {
  this.arAccountBanks = arAccountBanks;
 }

 public List<DocLineAr> getArLines() {
  return arLines;
 }

 public void setArLines(List<DocLineAr> arLines) {
  this.arLines = arLines;
 }

 public List<FiArPeriodBalance> getArPeriodBalances() {
  return arPeriodBalances;
 }

 public void setArPeriodBalances(List<FiArPeriodBalance> arPeriodBalances) {
  this.arPeriodBalances = arPeriodBalances;
 }

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public List<Contact> getContacts() {
  return contacts;
 }

 public void setContacts(List<Contact> contacts) {
  this.contacts = contacts;
 }

 public List<DocInvoiceAr> getInvoices() {
  return invoices;
 }

 public void setInvoices(List<DocInvoiceAr> invoices) {
  this.invoices = invoices;
 }

 
 @Override
 public SortOrder getSortOrder() {
  return sortOrder;
 }

 @Override
 public void setSortOrder(SortOrder sortOrder) {
  this.sortOrder = sortOrder;
 }

 public Address getAccountAddress() {
  return accountAddress;
 }

 public void setAccountAddress(Address accountAddress) {
  this.accountAddress = accountAddress;
 }

 public Address getInvoiceAddress() {
  return invoiceAddress;
 }

 public void setInvoiceAddress(Address invoiceAddress) {
  this.invoiceAddress = invoiceAddress;
 }

 public PartnerPerson getAccountClerk() {
  return accountClerk;
 }

 public void setAccountClerk(PartnerPerson accountClerk) {
  this.accountClerk = accountClerk;
 }

 public PartnerBase getArAccountFor() {
  return arAccountFor;
 }

 public void setArAccountFor(PartnerBase arAccountFor) {
  this.arAccountFor = arAccountFor;
 }

 public String getArAccPtnrType() {
  return arAccPtnrType;
 }

 public void setArAccPtnrType(String arAccPtnrType) {
  this.arAccPtnrType = arAccPtnrType;
 }


 public PartnerPerson getArAccountForPerson() {
  return arAccountForPerson;
 }

 public void setArAccountForPerson(PartnerPerson arAccountForPerson) {
  this.arAccountForPerson = arAccountForPerson;
 }

 public PartnerCorporate getArAccountForCorporate() {
  return arAccountForCorporate;
 }

 public void setArAccountForCorporate(PartnerCorporate arAccountForCorporate) {
  this.arAccountForCorporate = arAccountForCorporate;
 }

 public FiGlAccountComp getReconciliationAc() {
  return reconciliationAc;
 }

 public void setReconciliationAc(FiGlAccountComp reconciliationAc) {
  this.reconciliationAc = reconciliationAc;
 }

 public String getAcntNote() {
  return acntNote;
 }

 public void setAcntNote(String acntNote) {
  this.acntNote = acntNote;
 }

 
 public PaymentTerms getPaymentTerms() {
  return paymentTerms;
 }

 public void setPaymentTerms(PaymentTerms paymentTerms) {
  this.paymentTerms = paymentTerms;
 }

 public PaymentType getPaymentType() {
  return paymentType;
 }

 public void setPaymentType(PaymentType paymentType) {
  this.paymentType = paymentType;
 }

 public double getAccountBalance() {
  return accountBalance;
 }

 public void setAccountBalance(double accountBalance) {
  this.accountBalance = accountBalance;
 }

 public double getBalBucket1() {
  return balBucket1;
 }

 public void setBalBucket1(double balBucket1) {
  this.balBucket1 = balBucket1;
 }

 public double getBalBucket2() {
  return balBucket2;
 }

 public void setBalBucket2(double balBucket2) {
  this.balBucket2 = balBucket2;
 }

 public double getBalBucket3() {
  return balBucket3;
 }

 public void setBalBucket3(double balBucket3) {
  this.balBucket3 = balBucket3;
 }

 public double getBalBucket4() {
  return balBucket4;
 }

 public void setBalBucket4(double balBucket4) {
  this.balBucket4 = balBucket4;
 }

 public List<DocBankLineBase> getBankLines() {
  return bankLines;
 }

 public void setBankLines(List<DocBankLineBase> bankLines) {
  this.bankLines = bankLines;
 }
 
 

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

public double updateAccountBalance(DocLineAr arLine){
   
   if(arLine.getPostType().isDebit()){
    accountBalance = accountBalance + arLine.getDocAmount();
   }else{
    accountBalance = accountBalance - arLine.getDocAmount();
   }
   return this.accountBalance;
  }
  public ArAccount updatePeriodBalance(DocLineAr arLine, int year, int period){
   ListIterator<FiArPeriodBalance> periodsli = arPeriodBalances.listIterator();
   FiArPeriodBalance updateBal = null;
   boolean periodFound = false;
   while(periodsli.hasNext() && !periodFound){
    FiArPeriodBalance periodBalance = periodsli.next();
    if(periodBalance.getBalPeriod() == period && periodBalance.getBalYear() == year ){
     periodFound = true;
          updateBal = periodBalance;
    }
   }
   if(!periodFound){
    throw new BacException("Balance not found");
   }
   if(updateBal != null){
   double docDebitAmount = updateBal.getPeriodDebitAmount();
   double docCreditAmount = updateBal.getPeriodCreditAmount();
   double perTurnover = updateBal.getPeriodTurnover();
   
   if(arLine.getPostType().isDebit()){
    docDebitAmount = docDebitAmount + arLine.getDocAmount();
    arLine.setArDebitPeriodBalance(updateBal);
    if(arLine.getPostType().getPostTypeCode().equalsIgnoreCase("arInv")){
     perTurnover = perTurnover + docDebitAmount;
     updateBal.setPeriodTurnover(perTurnover);
    }
    
   }else{
    docCreditAmount = docCreditAmount +arLine.getDocAmount();
    arLine.setArCreditPeriodBalance(updateBal);
    if(arLine.getPostType().getPostTypeCode().equalsIgnoreCase("arCrn")){
     perTurnover = perTurnover - docDebitAmount;
     updateBal.setPeriodTurnover(perTurnover);
    }
   }
   
   double perAmount = docDebitAmount - docCreditAmount;
   updateBal.setPeriodDocAmount(perAmount);
   double cfwdDocAmount = updateBal.getBfwdDocAmount() + perAmount;
   updateBal.setCfwdDocAmount(cfwdDocAmount);
   }
   // update balance for Ar account
   periodsli = arPeriodBalances.listIterator();
   while(periodsli.hasNext()){
    FiArPeriodBalance periodBalance = periodsli.next();
    if(periodBalance.getBalPeriod() == period && periodBalance.getBalYear() == year ){
     periodsli.set(updateBal);
     return this;
    }
   }
   throw new BacException("Could not update AR period balance");
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
  if (!(object instanceof ArAccount)) {
   return false;
  }
  ArAccount other = (ArAccount) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 
 @Override
 public String toString() {
  return "com.rationem.entity.fi.arap.ArAccount[ id=" + id + " ]";
 }
 
}
