/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.glAccount;

import com.rationem.busRec.audit.AuditGlCompAccountRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class FiGlAccountCompRec  implements Serializable{
 
 public static final int VAT_NONE = 0;
 public static final int VAT_INPUT = 1;
 public static final int VAT_OUTPUT = 2;

  private Long id;
  //Common attributes
  private FiGlAccountBaseRec coaAccount;
  private CompanyBasicRec company;
  private SortOrderRec sortOrder;
  private String analysis1;
  private String analysis2;
  private VatCodeCompanyRec vatCode;
  private int vatStatus;
  private boolean noVatAllowed;
  private List<FiPeriodBalanceRec> periodBalances;
  private List<FiPeriodBalanceRec> restrictedBalances;
  private String repCategory;
  
  
  // balance sheet attributes
  private boolean accountClearing;
  private List<PaymentTypeRec> paymentTypes;
  
  //Bank accounting
  private BankAccountCompanyRec bankAccountCompanyCleared;
  private BankAccountCompanyRec bankAccountCompanyUncleared;
  
  //pl account attributes
  
  private String personResponsible;

  

  


  

  private UserRec createdBy;
  private Date createdOn;
  private UserRec changedBy;
  private Date changedOn;

  
  private int revision;
  private List<AuditGlCompAccountRec> auditGlCompAccounts;

  public FiGlAccountCompRec() {
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

 public boolean isAccountClearing() {
  return accountClearing;
 }

 public void setAccountClearing(boolean accountClearing) {
  this.accountClearing = accountClearing;
 }

 

 public BankAccountCompanyRec getBankAccountCompanyCleared() {
  return bankAccountCompanyCleared;
 }

 public void setBankAccountCompanyCleared(BankAccountCompanyRec bankAccountCompanyCleared) {
  this.bankAccountCompanyCleared = bankAccountCompanyCleared;
 }

 public BankAccountCompanyRec getBankAccountCompanyUncleared() {
  return bankAccountCompanyUncleared;
 }

 public void setBankAccountCompanyUncleared(BankAccountCompanyRec bankAccountCompanyUncleared) {
  this.bankAccountCompanyUncleared = bankAccountCompanyUncleared;
 }



  public FiGlAccountBaseRec getCoaAccount() {
    if(coaAccount == null){
     coaAccount = new  FiGlAccountBaseRec();
    }
    return coaAccount;
  }

  public void setCoaAccount(FiGlAccountBaseRec coaAccount) {
    this.coaAccount = coaAccount;
  }

  public CompanyBasicRec getCompany() {
    return company;
  }

  public void setCompany(CompanyBasicRec company) {
    this.company = company;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

 public List<PaymentTypeRec> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public String getPersonResponsible() {
  return personResponsible;
 }

 public void setPersonResponsible(String personResponsible) {
  this.personResponsible = personResponsible;
 }

  public SortOrderRec getSortOrder() {
    if(sortOrder == null){
      sortOrder = new SortOrderRec();
    }
    return sortOrder;
  }

  public void setSortOrder(SortOrderRec sortOrder) {
    this.sortOrder = sortOrder;
  }

 public int getVatStatus() {
  return vatStatus;
 }

 public void setVatStatus(int vatStatus) {
  this.vatStatus = vatStatus;
 }

  

  

  public VatCodeCompanyRec getVatCode() {
    return vatCode;
  }

  public void setVatCode(VatCodeCompanyRec vatCode) {
    this.vatCode = vatCode;
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

  public UserRec getCreatedBy() {
    return createdBy;
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

  public boolean isNoVatAllowed() {
    return noVatAllowed;
  }

  public void setNoVatAllowed(boolean noVatAllowed) {
    this.noVatAllowed = noVatAllowed;
  }

  public List<FiPeriodBalanceRec> getPeriodBalances() {
    return periodBalances;
  }

  public void setPeriodBalances(List<FiPeriodBalanceRec> periodBalances) {
    this.periodBalances = periodBalances;
  }

  public List<AuditGlCompAccountRec> getAuditGlCompAccounts() {
    return auditGlCompAccounts;
  }

  public void setAuditGlCompAccounts(List<AuditGlCompAccountRec> auditGlCompAccounts) {
    this.auditGlCompAccounts = auditGlCompAccounts;
  }

 public String getRepCategory() {
  return repCategory;
 }

 public void setRepCategory(String repCategory) {
  this.repCategory = repCategory;
 }

  public List<FiPeriodBalanceRec> getRestrictedBalances() {
    return restrictedBalances;
  }

  public void setRestrictedBalances(List<FiPeriodBalanceRec> restrictedBalances) {
    this.restrictedBalances = restrictedBalances;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final FiGlAccountCompRec other = (FiGlAccountCompRec) obj;
  return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

  

  


}
