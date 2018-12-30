/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.tr;

import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.user.UserRec;
import java.util.List;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 *
 * @author Chris
 */
public class BankRec implements Serializable {
 private static final Logger LOGGER =   Logger.getLogger(BankRec.class.getName());
 private Long id;
 private String bankCode;
 private String chapsBankCode;
 private PartnerCorporateRec bankOrganisation;
 private AddressRec bankAddress;
 private PartnerPersonRec bankContact;
 private List<BankBranchRec> bankBranches;
 private List<ArBankAccountRec> arBanks;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec updatedBy;
 private Date updatedOn;
 private int revision;

  public BankRec() {
  }

  

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

 public List<ArBankAccountRec> getArBanks() {
  return arBanks;
 }

 public void setArBanks(List<ArBankAccountRec> arBanks) {
  this.arBanks = arBanks;
 }

 public List<BankBranchRec> getBankBranches() {
  return bankBranches;
 }

 public void setBankBranches(List<BankBranchRec> bankBranches) {
  this.bankBranches = bankBranches;
 }

  

  public AddressRec getBankAddress() {
   
    return bankAddress;
  }

  public void setBankAddress(AddressRec bankAddress) {
    this.bankAddress = bankAddress;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

 public String getChapsBankCode() {
  return chapsBankCode;
 }

 public void setChapsBankCode(String chapsBankCode) {
  this.chapsBankCode = chapsBankCode;
 }

  public PartnerPersonRec getBankContact() {
   
    return bankContact;
  }

  public void setBankContact(PartnerPersonRec bankContact) {
    this.bankContact = bankContact;
  }

  

  public void setBankOrganisation(PartnerCorporateRec bankOrganisation) {
    LOGGER.log(INFO, "setBankOrganisation called with {0}", bankOrganisation);
    this.bankOrganisation = bankOrganisation;
  }

  public PartnerCorporateRec getBankOrganisation() {
    
    return bankOrganisation;
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
