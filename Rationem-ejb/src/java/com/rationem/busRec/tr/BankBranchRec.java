/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.tr;

import java.util.List;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 * A brach at a bank corresponds to a sort code
 * @author Chris
 */
public class BankBranchRec {
  private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.tr.BankBranchRec");

  private Long id;
  private BankRec bank;
  private String sortCode;
  private String swiftCode;
  private boolean subBranch;
  private String branchName;
  private AddressRec branchAddress;
  private String phoneArea;
  private String phoneNumber;
  private List<BankAccountRec> accounts;
  private UserRec createdBy;
  private Date createdOn;
  private UserRec updatedBy;
  private Date updatedOn;
  private int revision;

  public BankBranchRec() {
    logger.log(INFO, "BankBranchRec constructor");
  }

  public BankRec getBank() {
    if(bank == null){

      bank = new BankRec();
    }
    
    return bank;
  }

  public void setBank(BankRec bank) {
    this.bank = bank;
  }
  
  public void setBankOrg(BankRec bank) {
    this.bank = bank;
  }

  public AddressRec getBranchAddress() {
    
    return branchAddress;
  }

  public void setBranchAddress(AddressRec branchAddress) {
    this.branchAddress = branchAddress;
  }

 public String getPhoneArea() {
  return phoneArea;
 }

 public void setPhoneArea(String phoneArea) {
  this.phoneArea = phoneArea;
 }

 public String getPhoneNumber() {
  return phoneNumber;
 }

 public void setPhoneNumber(String phoneNumber) {
  this.phoneNumber = phoneNumber;
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

  public String getBranchName() {
    logger.log(INFO, "getBranchName called {0}", branchName);
    if(branchName == null){
      branchName = new String();
    }
    logger.log(INFO, "getBranchName returned {0}", branchName);
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getSortCode() {
    
    if(sortCode == null){
      sortCode = new String();
    }
    
    return sortCode;
  }

  public void setSortCode(String sortCode) {
    logger.log(INFO,"set sort code called with {0}",sortCode);
    this.sortCode = sortCode;
  }

 public String getSwiftCode() {
  return swiftCode;
 }

 public void setSwiftCode(String swiftCode) {
  this.swiftCode = swiftCode;
 }

 public boolean isSubBranch() {
  return subBranch;
 }

 public void setSubBranch(boolean subBranch) {
  this.subBranch = subBranch;
 }

  public List<BankAccountRec> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BankAccountRec> accounts) {
    this.accounts = accounts;
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

 @Override
 public int hashCode() {
  int hash = 3;
  hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
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
  final BankBranchRec other = (BankBranchRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "BankBranchRec{" + "id=" + id + ", sortCode=" + sortCode + ", branchName=" + branchName + '}';
 }

  

}
