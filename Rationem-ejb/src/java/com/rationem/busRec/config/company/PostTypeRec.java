/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class PostTypeRec implements Serializable {
  private Long id;
  private String postTypeCode;
  private String langCode;
  private String description;
  private String moduleDescription;  // Description within module for module only transactions
  private ModuleRec module;
  private LedgerRec ledger;
  private boolean debit;
  private String procCode;
  /**
   * Is this + or -
  */
  private char sign;
  private PostTypeRec revPostType;
  private boolean sysUse;
  private UserRec createdBy;
  private UserRec changedBy;
  private Date createDate;
  private Date changedDate;
  private int revision;

  public PostTypeRec() {
  }

  public PostTypeRec(Long id, String postTypeCode, String langCode, String description, LedgerRec ledger, boolean debit, char sign, UserRec createdBy, UserRec changedBy, Date createDate, Date changedDate, int revision) {
    this.id = id;
    this.postTypeCode = postTypeCode;
    this.langCode = langCode;
    this.description = description;
    this.ledger = ledger;
    this.debit = debit;
    this.sign = sign;
    this.createdBy = createdBy;
    this.changedBy = changedBy;
    this.createDate = createDate;
    this.changedDate = changedDate;
    this.revision = revision;
  }

  public UserRec getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(UserRec changedBy) {
    this.changedBy = changedBy;
  }

  public Date getChangedDate() {
    return changedDate;
  }

  public void setChangedDate(Date changedDate) {
    this.changedDate = changedDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public UserRec getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserRec createdBy) {
    this.createdBy = createdBy;
  }

  public boolean isDebit() {
    return debit;
  }

  public void setDebit(boolean debit) {
    this.debit = debit;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

 public String getModuleDescription() {
  return moduleDescription;
 }

 public void setModuleDescription(String moduleDescription) {
  this.moduleDescription = moduleDescription;
 }

  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }

  public LedgerRec getLedger() {
    return ledger;
  }

  public void setLedger(LedgerRec ledger) {
    this.ledger = ledger;
  }

 public ModuleRec getModule() {
  return module;
 }

 public void setModule(ModuleRec module) {
  this.module = module;
 }

 public String getProcCode() {
  return procCode;
 }

 public void setProcCode(String procCode) {
  this.procCode = procCode;
 }

 
  public String getPostTypeCode() {
    return postTypeCode;
  }

  public void setPostTypeCode(String postTypeCode) {
    this.postTypeCode = postTypeCode;
  }

 public PostTypeRec getRevPostType() {
  return revPostType;
 }

 public void setRevPostType(PostTypeRec revPostType) {
  this.revPostType = revPostType;
 }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
 }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public char getSign() {
    return sign;
  }

  public void setSign(char sign) {
    this.sign = sign;
  }

 @Override
 public int hashCode() {
  int hash = 3;
  hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
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
  final PostTypeRec other = (PostTypeRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "PostTypeRec{" + "id=" + id + ", postTypeCode=" + postTypeCode + '}';
 }

  


}
