/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class CalendarRuleFlexYearRec {
 private Long id;
 
 private CalendarRuleBaseRec calRule;
 private List<CalendarRuleFlexPerRec> flexPeriods;
 private int calYear;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public CalendarRuleFlexYearRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CalendarRuleBaseRec getCalRule() {
  return calRule;
 }

 public void setCalRule(CalendarRuleBaseRec calRule) {
  this.calRule = calRule;
 }

 public List<CalendarRuleFlexPerRec> getFlexPeriods() {
  return flexPeriods;
 }

 public void setFlexPeriods(List<CalendarRuleFlexPerRec> flexPeriods) {
  this.flexPeriods = flexPeriods;
 }

 public int getCalYear() {
  return calYear;
 }

 
 public void setCalYear(int calYear) {
  this.calYear = calYear;
 }
public Long getCalYearL(){
  Integer intYr = calYear;
  return intYr.longValue();
 }
public void setCalYearL(Long yr){
 calYear = yr.intValue();
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
