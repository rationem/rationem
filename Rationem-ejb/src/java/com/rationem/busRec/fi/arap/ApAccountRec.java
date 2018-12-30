/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.fi.arap;

import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class ApAccountRec {
 private Long id;
  private String accountCode;
  private String accountName;
  private CompanyBasicRec company;
  private SortOrderRec sortOrder;
  private AddressRec accountAddress;
  private AddressRec invoiceAddress;
  private PartnerPersonRec accountClerk;
  private PartnerBaseRec apAccountFor;
  private PartnerPersonRec apAccountForPerson;
  private PartnerCorporateRec apAccountForCorporate;
  private FiGlAccountCompRec reconciliationAc;
  private PaymentTermsRec paymentTerms;
  private PaymentTypeRec paymentType;
  private String acntEmail;
  private double accountBalance;
  private double balBucket1;
  private double balBucket2;
  private double balBucket3;
  private double balBucket4;
  private String acntNote;
  private List<ContactRec> contacts;

  private Date createdOn;
  private UserRec createdBy;
  private Date changedOn;
  private UserRec changedBy;
  private int changes;
  
  private List<FiApPeriodBalanceRec> apPeriodBalances;
 private List<ArBankAccountRec> apAccountBanks;
 private List<DocLineApRec> docLines;
 private List<FiApPeriodBalanceRec> periodBalances;

 public ApAccountRec() {
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

 public String getAcntEmail() {
  return acntEmail;
 }

 public void setAcntEmail(String acntEmail) {
  this.acntEmail = acntEmail;
 }

 public String getAcntNote() {
  return acntNote;
 }

 public void setAcntNote(String acntNote) {
  this.acntNote = acntNote;
 }

 
 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 public List<ContactRec> getContacts() {
  return contacts;
 }

 public void setContacts(List<ContactRec> contacts) {
  this.contacts = contacts;
 }

 public SortOrderRec getSortOrder() {
  return sortOrder;
 }

 public void setSortOrder(SortOrderRec sortOrder) {
  this.sortOrder = sortOrder;
 }

 public AddressRec getAccountAddress() {
  if(accountAddress == null){
   accountAddress = new AddressRec();
  }
  return accountAddress;
 }

 public void setAccountAddress(AddressRec accountAddress) {
  this.accountAddress = accountAddress;
 }

 public AddressRec getInvoiceAddress() {
  return invoiceAddress;
 }

 public void setInvoiceAddress(AddressRec invoiceAddress) {
  this.invoiceAddress = invoiceAddress;
 }

 public PartnerPersonRec getAccountClerk() {
  return accountClerk;
 }

 public void setAccountClerk(PartnerPersonRec accountClerk) {
  this.accountClerk = accountClerk;
 }

 public PartnerBaseRec getApAccountFor() {
  return apAccountFor;
 }

 public void setApAccountFor(PartnerBaseRec apAccountFor) {
  this.apAccountFor = apAccountFor;
 }

 public PartnerPersonRec getApAccountForPerson() {
  return apAccountForPerson;
 }

 public void setApAccountForPerson(PartnerPersonRec apAccountForPerson) {
  this.apAccountForPerson = apAccountForPerson;
 }

 public PartnerCorporateRec getApAccountForCorporate() {
  return apAccountForCorporate;
 }

 public void setApAccountForCorporate(PartnerCorporateRec apAccountForCorporate) {
  this.apAccountForCorporate = apAccountForCorporate;
 }

 public FiGlAccountCompRec getReconciliationAc() {
  return reconciliationAc;
 }

 public void setReconciliationAc(FiGlAccountCompRec reconciliationAc) {
  this.reconciliationAc = reconciliationAc;
 }

 public PaymentTermsRec getPaymentTerms() {
  return paymentTerms;
 }

 public void setPaymentTerms(PaymentTermsRec paymentTerms) {
  this.paymentTerms = paymentTerms;
 }

 public PaymentTypeRec getPaymentType() {
  return paymentType;
 }

 public void setPaymentType(PaymentTypeRec paymentType) {
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

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 public List<FiApPeriodBalanceRec> getApPeriodBalances() {
  return apPeriodBalances;
 }

 public void setApPeriodBalances(List<FiApPeriodBalanceRec> apPeriodBalances) {
  this.apPeriodBalances = apPeriodBalances;
 }

 public List<ArBankAccountRec> getApAccountBanks() {
  return apAccountBanks;
 }

 public void setApAccountBanks(List<ArBankAccountRec> apAccountBanks) {
  this.apAccountBanks = apAccountBanks;
 }

 public List<DocLineApRec> getDocLines() {
  return docLines;
 }

 public void setDocLines(List<DocLineApRec> docLines) {
  this.docLines = docLines;
 }

 public List<FiApPeriodBalanceRec> getPeriodBalances() {
  return periodBalances;
 }

 public void setPeriodBalances(List<FiApPeriodBalanceRec> periodBalances) {
  this.periodBalances = periodBalances;
 }

 
 
}
