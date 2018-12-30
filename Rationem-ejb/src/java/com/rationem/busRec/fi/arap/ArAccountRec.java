/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.arap;

import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class ArAccountRec {
  private static final Logger LOGGER =  Logger.getLogger(ArAccountRec.class.getName());
  private Long id;
  private String arAccountCode;
  private String arAccountName;
  private AddressRec accountAddress;
  private AddressRec invoiceAddress;
  private CompanyBasicRec company;
  private SortOrderRec sortOrder;
  private PartnerPersonRec accountClerk;
  private PartnerBaseRec arAccountFor;
  private PaymentTermsRec paymentTerms;
  private PaymentTypeRec paymentType;
  private double accountBalance;
  private double balBucket1;
  private double balBucket2;
  private double balBucket3;
  private double balBucket4;
  private String acntNote;
  private List<DocInvoiceArRec> invoices; 
  private List<ArBankAccountRec> arAccountBanks;
  private List<DocLineArRec> arLines;
  private List<FiArPeriodBalanceRec> arPeriodBalances;
  private FiGlAccountCompRec reconciliationAc;
  private List<ContactRec> contacts;
  
  private Date createdOn;
  private UserRec createdBy;
  private Date changedOn;
  private UserRec changedBy;
  private int changes;
  

  public ArAccountRec() {
  }

  public String getArAccountCode() {
    return arAccountCode;
  }

  public void setArAccountCode(String arAccountCode) {
    this.arAccountCode = arAccountCode;
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

 public String getArAccountName() {
  return arAccountName;
 }

 public void setArAccountName(String arAccountName) {
  this.arAccountName = arAccountName;
 }

 public List<DocInvoiceArRec> getInvoices() {
  return invoices;
 }

 public void setInvoices(List<DocInvoiceArRec> invoices) {
  this.invoices = invoices;
 }


  public AddressRec getInvoiceAddress() {
    return invoiceAddress;
  }

  public void setInvoiceAddress(AddressRec invoiceAddress) {
    this.invoiceAddress = invoiceAddress;
  }

  public double getAccountBalance() {
    return accountBalance;
  }

  public void setAccountBalance(double accountBalance) {
    this.accountBalance = accountBalance;
  }

  public PartnerPersonRec getAccountClerk() {
    if(accountClerk == null){
     accountClerk = new PartnerPersonRec();
    }
    return accountClerk;
  }

  public void setAccountClerk(PartnerPersonRec accountClerk) {

    this.accountClerk = accountClerk;
  }

 public String getAcntNote() {
  return acntNote;
 }

 public void setAcntNote(String acntNote) {
  this.acntNote = acntNote;
 }

  public List<ArBankAccountRec> getArAccountBanks() {

    return arAccountBanks;
  }

  public void setArAccountBanks(List<ArBankAccountRec> arAccountBanks) {
    this.arAccountBanks = arAccountBanks;
  }

  public PartnerBaseRec getArAccountFor() {
    return arAccountFor;
  }

  public void setArAccountFor(PartnerBaseRec arAccountFor) {
    this.arAccountFor = arAccountFor;
  }

  public List<DocLineArRec> getArLines() {
    return arLines;
  }

  public void setArLines(List<DocLineArRec> arLines) {
    this.arLines = arLines;
  }

  public List<FiArPeriodBalanceRec> getArPeriodBalances() {
    return arPeriodBalances;
  }

  public void setArPeriodBalances(List<FiArPeriodBalanceRec> arPeriodBalances) {
    this.arPeriodBalances = arPeriodBalances;
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

  public UserRec getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(UserRec changedBy) {
    this.changedBy = changedBy;
  }

  public Date getChangedOn() {
    return changedOn;
  }

  public void setChangedOn(Date changedOn) {
    this.changedOn = changedOn;
  }

  public int getChanges() {
    return changes;
  }

  public void setChanges(int changes) {
    this.changes = changes;
  }

  public CompanyBasicRec getCompany() {
    return company;
  }

  public void setCompany(CompanyBasicRec company) {
    this.company = company;
  }

  public UserRec getCreatedBy() {
    return createdBy;
  }

 public List<ContactRec> getContacts() {
  return contacts;
 }

 public void setContacts(List<ContactRec> contacts) {
  this.contacts = contacts;
 }

  public void setCreatedBy(UserRec createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public SortOrderRec getSortOrder() {
    LOGGER.log(INFO,"getSortOrder {0}",sortOrder);
    return sortOrder;
  }

  public void setSortOrder(SortOrderRec sortOrder) {
    LOGGER.log(INFO,"setSortOrder called with {0}",sortOrder);
    this.sortOrder = sortOrder;
  }

  

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  
  public FiGlAccountCompRec getReconciliationAc() {
    if(reconciliationAc == null){
      reconciliationAc = new FiGlAccountCompRec();
    }
    return reconciliationAc;
  }

  public void setReconciliationAc(FiGlAccountCompRec reconciliationAc) {
    this.reconciliationAc = reconciliationAc;
  }
 
}
