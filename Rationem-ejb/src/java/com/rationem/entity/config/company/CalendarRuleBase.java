/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditCalRuleBase;
import com.rationem.entity.user.User;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import javax.persistence.Version;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Temporal;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.SequenceGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;


/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=100)
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRuleBase")
@Table(name="bac_config05" )
@NamedQueries({
@NamedQuery(name = "getAllCalRules",
query="Select calRule from CalendarRuleBase calRule"),
@NamedQuery(name = "calRuleByRef",
query="Select c from CalendarRuleBase c where c.reference= :ref")
})
@SequenceGenerator(name = "calendarRuleBase_s1", sequenceName = "bac_config05_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class CalendarRuleBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "calendarRuleBase_s1")
 @Column(name="fis_per_cal_base_id")
 private Long id;
 @Column(name="cal_ref")
 private String reference;
 @Column(name="cal_descr")
 private String description;
 @Column(name="nat_cal")
 private boolean naturalCal;
 @Column(name="month_cal")
 private boolean monthCal;
 @Column(name="flex_cal")
 private boolean flexCal;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int version;
 
 
 @OneToMany(mappedBy = "calRul", cascade=CascadeType.REMOVE)
 private List<AuditCalRuleBase> auditRecords;

 public CalendarRuleBase() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCalRuleBase> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditCalRuleBase> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public boolean isNaturalCal() {
  return naturalCal;
 }

 public void setNaturalCal(boolean naturalCal) {
  this.naturalCal = naturalCal;
 }

 public boolean isMonthCal() {
  return monthCal;
 }

 public void setMonthCal(boolean monthCal) {
  this.monthCal = monthCal;
 }

 public boolean isFlexCal() {
  return flexCal;
 }

 public void setFlexCal(boolean flexCal) {
  this.flexCal = flexCal;
 }

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 
 
 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
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

 public int getVersion() {
  return version;
 }

 public void setVersion(int version) {
  this.version = version;
 }

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof CalendarRuleBase)) {
   return false;
  }
  CalendarRuleBase other = (CalendarRuleBase) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.CalendarRuleBase[ id=" + id + " ]";
 }
 
}
