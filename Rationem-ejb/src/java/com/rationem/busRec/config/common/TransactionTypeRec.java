/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class TransactionTypeRec implements Serializable {
 private Long id;
 private String code;
 private String shortDescription;
 private String description;
 private LedgerRec ledger;
 private String processCode;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int revision;

 public TransactionTypeRec() {
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

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
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

 public LedgerRec getLedger() {
  return ledger;
 }

 public void setLedger(LedgerRec ledger) {
  this.ledger = ledger;
 }

 public String getProcessCode() {
  return processCode;
 }

 public void setProcessCode(String processCode) {
  this.processCode = processCode;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public String getShortDescription() {
  return shortDescription;
 }

 public void setShortDescription(String shortDescription) {
  this.shortDescription = shortDescription;
 }

 
 
}
