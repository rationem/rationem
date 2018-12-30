/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.arap;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;




/**
 *
 * @author Chris
 */
public class PaymentTypeRec implements Serializable {
 private static final Logger LOGGER =  Logger.getLogger(PaymentTypeRec.class.getName());

 public static final  String PAY_MED_CHQ = "CHQ";
 public static final String PAY_MED_DD = "DD";
 public static final String PAY_MED_DC = "DC";
 public static final String PAY_MED_CASH = "CSH";
 public static final int PAY_SUMM_DOC = 1;
 public static final int PAY_SUMM_ACNT = 2;
 /**
  * Payment type summarises pay at document level
  */
 public static final int SUMM_DOC = 1;
 /**
  * Payment type summarises pay at AR Account level
  */
 public static final int SUMM_AR_ACNT = 2;
 
  private Long id;
  private CompanyBasicRec company;
  private String payTypeCode;
  private String description;
  /**
   * CHQ = cheque DD=Direct debit DC=direct collection
   */
  private String payMedium;
  private UomRec mediumUom;
  private LedgerRec ledger;
  private boolean inbound;
  private BankAccountCompanyRec payTypeForBankAccount;
  private FiGlAccountCompRec glBankAccount;
  private BacsTransCodeRec bacsTransCode;
  private ChequeTemplateRec chqTemplate;
  /**
   * 1 = doc
   * 2 = account
   */
  private int summLevel;
  private UserRec createdBy;
  private Date createdOn;
  private UserRec changedBy;
  private Date changedOn;
  private int revision;
  private boolean hasCheqTemplate;

  public PaymentTypeRec() {
  }

 public BacsTransCodeRec getBacsTransCode() {
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCodeRec bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
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

 public ChequeTemplateRec getChqTemplate() {
  return chqTemplate;
 }

 public void setChqTemplate(ChequeTemplateRec chqTemplate) {
  this.chqTemplate = chqTemplate;
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

  public void setCreatedBy(UserRec createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

 public boolean isHasCheqTemplate() {
  return hasCheqTemplate;
 }

 public void setHasCheqTemplate(boolean hasCheqTemplate) {
  this.hasCheqTemplate = hasCheqTemplate;
 }

  
 public boolean isInbound() {
  return inbound;
 }

 public void setInbound(boolean inbound) {
  this.inbound = inbound;
 }

  public LedgerRec getLedger() {
    return ledger;
  }

  public void setLedger(LedgerRec ledger) {
    this.ledger = ledger;
  }

  public UomRec getMediumUom() {
    return mediumUom;
  }

  public void setMediumUom(UomRec mediumUom) {
    this.mediumUom = mediumUom;
  }

  public String getPayMedium() {
    return payMedium;
  }

  public void setPayMedium(String payMedium) {
    this.payMedium = payMedium;
  }

  public String getPayTypeCode() {
    return payTypeCode;
  }

  public void setPayTypeCode(String payTypeCode) {
    this.payTypeCode = payTypeCode;
  }

  public BankAccountCompanyRec getPayTypeForBankAccount() {
    return payTypeForBankAccount;
  }

  public void setPayTypeForBankAccount(BankAccountCompanyRec payTypeForBankAccount) {
    this.payTypeForBankAccount = payTypeForBankAccount;
  }

  public FiGlAccountCompRec getGlBankAccount() {
    return glBankAccount;
  }

  public void setGlBankAccount(FiGlAccountCompRec glBankAccount) {
    this.glBankAccount = glBankAccount;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

 public int getSummLevel() {
  return summLevel;
 }

 public void setSummLevel(int summLevel) {
  this.summLevel = summLevel;
 }

 @Override
 public int hashCode() {
  int hash = 3;
  hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final PaymentTypeRec other = (PaymentTypeRec) obj;
  return !(!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "PaymentTypeRec{" + "id=" + id + ", payTypeCode=" + payTypeCode + '}';
 }

  


}
