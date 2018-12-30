/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.company;

import com.rationem.busRec.config.common.FundCategoryRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class FundRec implements Serializable {
  private Long id;
  private String fndCode;
  private CompanyBasicRec fundForComp;
  private String name;
  private String purpose;
  private Date validFrom;
  private Date validTo;
  private Date returnDue;
  private boolean returnRequired;
  private FundCategoryRec fundCategory;
  private UserRec createdBy;
  private Date createdOn;
  private UserRec changedBy;
  private Date changedOn;
  private long revision;

  public FundRec() {
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

  public String getFndCode() {
    return fndCode;
  }

  public void setFndCode(String fndCode) {
    this.fndCode = fndCode;
  }

 public FundCategoryRec getFundCategory() {
  return fundCategory;
 }

 public void setFundCategory(FundCategoryRec fundCategory) {
  this.fundCategory = fundCategory;
 }

  public CompanyBasicRec getFundForComp() {
    return fundForComp;
  }

  public void setFundForComp(CompanyBasicRec fundForComp) {
    this.fundForComp = fundForComp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

 

  public Date getReturnDue() {
    return returnDue;
  }

  public void setReturnDue(Date returnDue) {
    this.returnDue = returnDue;
  }

  public boolean isReturnRequired() {
    return returnRequired;
  }

  public void setReturnRequired(boolean returnRequired) {
    this.returnRequired = returnRequired;
  }

  public long getRevision() {
    return revision;
  }

  public void setRevision(long revision) {
    this.revision = revision;
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
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
  final FundRec other = (FundRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 
}
