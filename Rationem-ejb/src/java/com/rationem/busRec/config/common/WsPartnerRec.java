/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserLoginRecOld;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class WsPartnerRec {
  private Long id;
  private String wsName;
  private String descr;
  private boolean active;
  private Date validFrom;
  private String accountName;
  private String password;
  private Long calls;
  private Date createDate;
  private Date changeDate;
  private UserLoginRecOld createdBy;
  private UserLoginRecOld changedBy;
  private int revision;

  public WsPartnerRec() {
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getCalls() {
    return calls;
  }

  public void setCalls(Long calls) {
    this.calls = calls;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  public UserLoginRecOld getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(UserLoginRecOld changedBy) {
    this.changedBy = changedBy;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public UserLoginRecOld getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserLoginRecOld createdBy) {
    this.createdBy = createdBy;
  }

 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  public String getWsName() {
    return wsName;
  }

  public void setWsName(String wsName) {
    this.wsName = wsName;
  }



}
