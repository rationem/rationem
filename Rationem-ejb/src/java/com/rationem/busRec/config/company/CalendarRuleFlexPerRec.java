/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class CalendarRuleFlexPerRec {
 private Long id;
 
 private CalendarRuleFlexYearRec calRuleFlexYr;
 private int calPeriod;
 private Date startPeriod;
 private Date endPeriod;
 private boolean auditPer;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public CalendarRuleFlexPerRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public boolean isAuditPer() {
  return auditPer;
 }

 public void setAuditPer(boolean auditPer) {
  this.auditPer = auditPer;
 }

 public CalendarRuleFlexYearRec getCalRuleFlexYr() {
  return calRuleFlexYr;
 }

 public void setCalRuleFlexYr(CalendarRuleFlexYearRec calRuleFlexYr) {
  this.calRuleFlexYr = calRuleFlexYr;
 }

 public int getCalPeriod() {
  return calPeriod;
 }

 public void setCalPeriod(int calPeriod) {
  this.calPeriod = calPeriod;
 }

 public Date getStartPeriod() {
  return startPeriod;
 }

 public void setStartPeriod(Date startPeriod) {
  this.startPeriod = startPeriod;
 }

 public Date getEndPeriod() {
  return endPeriod;
 }

 public void setEndPeriod(Date endPeriod) {
  this.endPeriod = endPeriod;
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
