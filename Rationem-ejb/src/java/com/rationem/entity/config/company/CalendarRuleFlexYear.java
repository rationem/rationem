/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditCalRuleFlexYr;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Version;


import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config17")
@NamedQueries({
@NamedQuery(name="flexYrByRuleRefYear", 
    query="Select f from CalendarRuleFlexYear f where f.reference = :ruleRef and f.calYear = :yr"),
@NamedQuery(name="flexYrCalRule", 
    query="Select c from CalendarRuleFlexYear c where c.reference = :ruleRef")
        })
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRuleFixedDate")
@PrimaryKeyJoinColumn(name="cal_flex_yr_id",referencedColumnName = "fis_per_cal_base_id")
public class CalendarRuleFlexYear extends CalendarRuleBase implements Serializable {
 private static final long serialVersionUID = 1L;
 
 /*
 @ManyToOne
 @JoinColumn(name="cal_rule_id", referencedColumnName="fis_per_cal_base_id")
 private CalendarRuleBase calRule; */
 @OneToMany(mappedBy = "calRuleFlexYr")
 private List<CalendarFlexPer> flexPeriods;
 @Column(name="cal_year")
 private int calYear;
 
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 private Date changedOn;
 @Version
 private int changes;
 @OneToMany(mappedBy = "flexYr", cascade=REMOVE)
 private List<AuditCalRuleFlexYr> auditRecords;

 public CalendarRuleFlexYear() {
 }

 

 



 public List<CalendarFlexPer> getFlexPeriods() {
  return flexPeriods;
 }

 public void setFlexPeriods(List<CalendarFlexPer> flexPeriods) {
  this.flexPeriods = flexPeriods;
 }

 public int getCalYear() {
  return calYear;
 }

 public void setCalYear(int calYear) {
  this.calYear = calYear;
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
