/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.tr;

import com.rationem.busRec.audit.AuditBankAccountRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class BankAccountRec {
  private static final Logger LOGGER = Logger.getLogger(BankAccountRec.class.getName());
  private Long id;

  private String accountRef;
  private String accountNumber;
  private String accountName;
  private boolean directCreditAllowed;
  private boolean directDebitsAllowed;
  private boolean fasterPayments;
  private boolean validated;
//  private double balance;
//  private double clearedBalance;
 // private List<Long> chequeRange;
  
  private BankBranchRec accountForBranch;
  private UserRec createdBy;
  private Date createdOn;
  private UserRec updatedBy;
  private Date updatedOn;
  private int revision;
  private List<AuditBankAccountRec> auditRecs;

  public BankAccountRec() {
    LOGGER.log(INFO, "TR - BankAccountRec contsructor");
  }

 public List<AuditBankAccountRec> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditBankAccountRec> auditRecs) {
  this.auditRecs = auditRecs;
 }

  

  public BankBranchRec getAccountForBranch() {
    
    return accountForBranch;
  }

  public void setAccountForBranch(BankBranchRec accountFor) {
   LOGGER.log(INFO, "setAccountForBranch called with {0}", accountFor);
   /*if(accountFor == null){
    return;
   }*/
    this.accountForBranch = accountFor;
  }

  

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

 public boolean isDirectCreditAllowed() {
  return directCreditAllowed;
 }

 public void setDirectCreditAllowed(boolean directCreditAllowed) {
  this.directCreditAllowed = directCreditAllowed;
 }

 public boolean isDirectDebitsAllowed() {
  return directDebitsAllowed;
 }

 public void setDirectDebitsAllowed(boolean directDebitsAllowed) {
  this.directDebitsAllowed = directDebitsAllowed;
 }

 public boolean isFasterPayments() {
  return fasterPayments;
 }

 public void setFasterPayments(boolean fasterPayments) {
  this.fasterPayments = fasterPayments;
 }

 public String getAccountRef() {
    return accountRef;
 }

 public void setAccountRef(String accountRef) {
    this.accountRef = accountRef;
 }

 public boolean isValidated() {
  return validated;
 }

 public void setValidated(boolean validated) {
  this.validated = validated;
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

  

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  

  public UserRec getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(UserRec updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }



}
