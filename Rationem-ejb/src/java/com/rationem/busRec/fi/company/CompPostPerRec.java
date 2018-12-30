/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.fi.company;

import com.rationem.busRec.audit.AuditCompPostPerRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author user
 */
public class CompPostPerRec implements Serializable {
 private Long id;
 
 private CompanyBasicRec comp;
 private LedgerRec ledger;
 private int startLong;
 private int endLong;
 private int startYear;
 private int startPer;
 private int endYear;
 private int endPer;
 
 private List<AuditCompPostPerRec> auditRecs;
 
 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;
 private long changes;
 

 public CompPostPerRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCompPostPerRec> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditCompPostPerRec> auditRecs) {
  this.auditRecs = auditRecs;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public LedgerRec getLedger() {
  return ledger;
 }

 public void setLedger(LedgerRec ledger) {
  this.ledger = ledger;
 }

 public int getStartLong() {
  return startLong;
 }

 public void setStartLong(int startLong) {
  this.startLong = startLong;
 }

 public int getEndLong() {
  return endLong;
 }

 public void setEndLong(int endLong) {
  this.endLong = endLong;
 }

 public int getStartYear() {
  return startYear;
 }

 public void setStartYear(int startYear) {
  this.startYear = startYear;
 }

 public int getStartPer() {
  return startPer;
 }

 public void setStartPer(int startPer) {
  this.startPer = startPer;
 }

 public int getEndYear() {
  return endYear;
 }

 public void setEndYear(int endYear) {
  this.endYear = endYear;
 }

 public int getEndPer() {
  return endPer;
 }

 public void setEndPer(int endPer) {
  this.endPer = endPer;
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

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 17 * hash + Objects.hashCode(this.id);
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
  final CompPostPerRec other = (CompPostPerRec) obj;
  if (!Objects.equals(this.id, other.id)) {
   return false;
  }
  return true;
 }
 
 
 
}
