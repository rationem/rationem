/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class FundCategoryRec implements Serializable {
 private Long id;
 private String catRef;
 private String description;
 private boolean designated;
 private boolean endowment;
 private boolean permanent;
 private boolean restricted;
 private String processCode;
 private List<FundRec> fund;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public FundCategoryRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCatRef() {
  return catRef;
 }

 public void setCatRef(String catRef) {
  this.catRef = catRef;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isDesignated() {
  return designated;
 }

 public void setDesignated(boolean designated) {
  this.designated = designated;
 }

 public boolean isEndowment() {
  return endowment;
 }

 public void setEndowment(boolean endowment) {
  this.endowment = endowment;
 }

 public boolean isPermanent() {
  return permanent;
 }

 public void setPermanent(boolean permanent) {
  this.permanent = permanent;
 }

 public boolean isRestricted() {
  return restricted;
 }

 public void setRestricted(boolean restricted) {
  this.restricted = restricted;
 }

 public String getProcessCode() {
  return processCode;
 }

 public void setProcessCode(String processCode) {
  this.processCode = processCode;
 }

 public List<FundRec> getFund() {
  return fund;
 }

 public void setFund(List<FundRec> fund) {
  this.fund = fund;
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
 
 
 
 
}
