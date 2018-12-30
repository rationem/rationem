/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author user
 */
public class ChequeVoidReasonRec implements Serializable {
 private static final Logger LOGGER = Logger.getLogger(ChequeVoidReasonRec.class.getName());
 
 private Long id;
 
 private String code;
 private String description;
 private boolean sysUse;
 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;
 private long changes;

 public ChequeVoidReasonRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
 }
 
 
 

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
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

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }
 
 
 
 
}
