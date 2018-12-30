/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditCalRuleFlexPer;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import static javax.persistence.TemporalType.DATE;
import javax.persistence.Temporal;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config18" )
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRuleFixedDate")
@PrimaryKeyJoinColumn(name="cal_flex_per_id",referencedColumnName = "fis_per_cal_base_id")
public class CalendarFlexPer extends CalendarRuleBase implements Serializable {
  
 @ManyToOne
 @JoinColumn(name="cal_rule_flex_yr", referencedColumnName="cal_flex_yr_id")
 private CalendarRuleFlexYear calRuleFlexYr;
 @Column(name="cal_period")
 private int calPeriod;
 @Column(name="start_period")
 @Temporal(DATE)
 private Date startPeriod;
 @Column(name="end_period")
 @Temporal(DATE)
 private Date endPeriod;
 
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name=" changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int changes;
 @OneToMany(mappedBy = "calPer")
 private List<AuditCalRuleFlexPer> auditRecords;

 public CalendarFlexPer() {
 }
 

 

 public CalendarRuleFlexYear getCalRuleFlexYr() {
  return calRuleFlexYr;
 }

 public void setCalRuleFlexYr(CalendarRuleFlexYear calRuleFlexYr) {
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

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
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
