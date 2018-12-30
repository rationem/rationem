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
//import com.rationem.entity.document.DocLineAP;
//import com.rationem.entity.document.DocLineAR;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.salesTax.vat.VatReturnLine;
import com.rationem.entity.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.OneToMany;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity

@Table(name="fi_account11")
@SequenceGenerator(name = "apAccount_s1", sequenceName = "fi_account11_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
@NamedQueries({
 
@NamedQuery(name="apActsByName", query="Select ap from ApAccount ap where ap.accountCode = :acntCode "
        + "and ap.company.id = :compId"),
@NamedQuery(name="apActsByNameXcomp", query="Select ap from ApAccount ap where ap.accountCode like :acntCode "
        + "and ap.company.id in :compIdList"),
//@NamedQuery(name="apActsBycode", 
//  query="Select ap from ApAccount ap where ap.accountCode = :code and ap.company.id = :compId"),
       
@NamedQuery(name="apActsByCompAll", query="Select ap from ApAccount ap where  ap.company.id = :compId"),
@NamedQuery(name="apActsByAllCrossComp", query="Select ap from ApAccount ap where   ap.company.id in :compIdList "),
@NamedQuery(name="apActsByPtnr", query="Select ap from ApAccount ap where ap.apAccountFor.id = :ptnrId "),
@NamedQuery(name="apBalsbyComp",
  query="select a.id, a.accountCode, a.accountName, a.accountBalance from ApAccount a "
    + "where a.company.id = :compId")
  
})
public class ApAccount implements ArAPAccountIF {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "apAccount_s1")
 @Column(name="ap_account_id")
 private Long id;
 @Column(name="ap_account_code")
 private String accountCode;
 @Column(name="ap_acnt_name")
 private String accountName;
 @Column(name="note", length=4000)
 private String acntNote;
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
 @JoinColumn(name="invoice_address_id",referencedColumnName="address_id")
 private Address invoiceAddress;
 @ManyToOne
 @JoinColumn(name="account_clerk_ptnr_id",referencedColumnName="partner_id")
 private PartnerPerson accountClerk;
 @ManyToOne
 @JoinColumn(name="account_for_partner_id",referencedColumnName="partner_id")
 private PartnerBase apAccountFor;
 @ManyToOne
 @JoinColumn(name="account_for_partner_id",referencedColumnName="partner_id",nullable=false,
     insertable=false, updatable=false)
 private PartnerPerson apAccountForPerson;
 @ManyToOne
 @JoinColumn(name="account_for_partner_id",referencedColumnName="partner_id",nullable=false,
     insertable=false, updatable=false)
 private PartnerCorporate apAccountForCorporate;
 @Column(name="account_email", length=50)
 private String apAccntEmail;
 
 @ManyToMany
 @JoinTable(name="fi_account13", 
   joinColumns={@JoinColumn(name="ap_account_id", referencedColumnName="ap_account_id")},
   inverseJoinColumns={@JoinColumn(name="ap_bank_id", referencedColumnName="ar_bank_id")}
 )
 private List<ArBankAccount> apAccountBanks;
 
 
 @OneToMany(mappedBy = "apAccount")
 private List<FiApPeriodBalance> apPeriodBalnces;
 
 @ManyToOne
 @JoinColumn(name="gl_reconcil_ac_id",referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp reconciliationAc;
 @ManyToOne
 @JoinColumn(name="pay_terms_id", referencedColumnName="pay_terms_id")
 private PaymentTerms paymentTerms;
 @ManyToOne
 @JoinColumn(name="pay_type_id",referencedColumnName="pay_type_id")
 private PaymentType paymentType;
  
 @Column(name="account_bal_doc")
 private double accountBalance;
 @Column(name="bal_bucket1")
 private double balBucket1;
 @Column(name="bal_bucket2")
 private double balBucket2;
 @Column(name="bal_bucket3")
 private double balBucket3;
 @Column(name="bal_bucket4")
 private double balBucket4;

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
 @Column(name="changes")
 private int changes;
 @OneToMany(mappedBy = "apAccount")
 private List<VatReturnLine> vatReturnLines;
 @OneToMany(mappedBy = "apAccount")
 private List<Contact> contacts;
 @OneToMany(mappedBy = "apAccount")
 private List<DocBankLineBase> bankLines;
 
 public ApAccount() {
 }

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

 public List<DocBankLineBase> getBankLines() {
  return bankLines;
 }

 public void setBankLines(List<DocBankLineBase> bankLines) {
  this.bankLines = bankLines;
 }

 
 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 @Override
 public SortOrder getSortOrder() {
  return sortOrder;
 }

 @Override
 public void setSortOrder(SortOrder sortOrder) {
  this.sortOrder = sortOrder;
 }

 public String getAcntNote() {
  return acntNote;
 }

 public void setAcntNote(String acntNote) {
  this.acntNote = acntNote;
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

 public String getApAccntEmail() {
  return apAccntEmail;
 }

 public void setApAccntEmail(String apAccntEmail) {
  this.apAccntEmail = apAccntEmail;
 }

 public PartnerBase getApAccountFor() {
  return apAccountFor;
 }

 public void setApAccountFor(PartnerBase apAccountFor) {
  this.apAccountFor = apAccountFor;
 }

 public PartnerPerson getApAccountForPerson() {
  return apAccountForPerson;
 }

 public void setApAccountForPerson(PartnerPerson apAccountForPerson) {
  this.apAccountForPerson = apAccountForPerson;
 }

 public PartnerCorporate getApAccountForCorporate() {
  return apAccountForCorporate;
 }

 public void setApAccountForCorporate(PartnerCorporate apAccountForCorporate) {
  this.apAccountForCorporate = apAccountForCorporate;
 }

 public FiGlAccountComp getReconciliationAc() {
  return reconciliationAc;
 }

 public void setReconciliationAc(FiGlAccountComp reconciliationAc) {
  this.reconciliationAc = reconciliationAc;
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

 public List<ArBankAccount> getApAccountBanks() {
  return apAccountBanks;
 }

 public void setApAccountBanks(List<ArBankAccount> apAccountBanks) {
  this.apAccountBanks = apAccountBanks;
 }

 public List<FiApPeriodBalance> getApPeriodBalnces() {
  return apPeriodBalnces;
 }

 public void setApPeriodBalnces(List<FiApPeriodBalance> apPeriodBalnces) {
  this.apPeriodBalnces = apPeriodBalnces;
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

 public List<VatReturnLine> getVatReturnLines() {
  return vatReturnLines;
 }

 public void setVatReturnLines(List<VatReturnLine> vatReturnLines) {
  this.vatReturnLines = vatReturnLines;
 }

 public List<Contact> getContacts() {
  return contacts;
 }

 public void setContacts(List<Contact> contacts) {
  this.contacts = contacts;
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
  if (!(object instanceof ApAccount)) {
   return false;
  }
  ApAccount other = (ApAccount) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.arap.ApAccount[ id=" + id + " ]";
 }
 
}
