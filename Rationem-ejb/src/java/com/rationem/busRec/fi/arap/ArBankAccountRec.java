/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.arap;

import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class ArBankAccountRec {
  private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.fi.arap.ArBankRec");
  private Long id;
  private BankRec bank;
  private BankAccountRec bankAccount;
  private String accountName;
  private String bankKey;
  private boolean collectionAuthorisation;
  private String ddRef;
  private Date collValidFrom;
  private byte[] signedDD;
  private String ddFileType;
  private String ddFileName;
  private ArAccountRec bankForArAccount;
  private ApAccountRec bankForApAccount;
  private Date createdOn;
  private UserRec createdBy;
  private Date changedOn;
  private UserRec changedBy;
  private int changes;

  public ArBankAccountRec() {
    logger.log(INFO, "ArBankRec constructor");
  }

  public String getAccountName() {
   
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public BankAccountRec getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(BankAccountRec bankAccount) {
    this.bankAccount = bankAccount;
  }

 public ApAccountRec getBankForApAccount() {
  return bankForApAccount;
 }

 public void setBankForApAccount(ApAccountRec bankForApAccount) {
  this.bankForApAccount = bankForApAccount;
 }

  
  public ArAccountRec getBankForArAccount() {
    return bankForArAccount;
  }

  public void setBankForArAccount(ArAccountRec bankForArAccount) {
    this.bankForArAccount = bankForArAccount;
  }

  public BankRec getBank() {
    return bank;
  }

  public void setBank(BankRec bank) {
    this.bank = bank;
  }

  public String getBankKey() {
    return bankKey;
  }

  public void setBankKey(String bankKey) {
    this.bankKey = bankKey;
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

  public Date getCollValidFrom() {
    return collValidFrom;
  }

  public void setCollValidFrom(Date collValidFrom) {
    this.collValidFrom = collValidFrom;
  }

  public String getCollAuthYesNo(){
   if(collectionAuthorisation){
    return "Yes";
   }else{
    return "No";
   }
  }
  public boolean isCollectionAuthorisation() {
    return collectionAuthorisation;
  }

  public void setCollectionAuthorisation(boolean collectionAuthorisation) {
    this.collectionAuthorisation = collectionAuthorisation;
  }

 public String getDdFileName() {
  return ddFileName;
 }

 public void setDdFileName(String ddFileName) {
  this.ddFileName = ddFileName;
 }

  
 public String getDdFileType() {
  return ddFileType;
 }

 public void setDdFileType(String ddFileType) {
  this.ddFileType = ddFileType;
 }
  

  
 public String getDdRef() {
  return ddRef;
 }

 public void setDdRef(String ddRef) {
  this.ddRef = ddRef;
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

  public byte[] getSignedDD() {
    return signedDD;
  }

  public void setSignedDD(byte[] signedDD) {
    this.signedDD = signedDD;
  }




}
