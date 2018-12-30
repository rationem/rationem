/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditFisPeriodRule;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.SequenceGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config02")
@NamedQuery(name = "findAllPeriodRules",query="Select periodRule.id from FisPeriodRule periodRule")
@SequenceGenerator(name = "fisPeriodRule_s1", sequenceName = "mdm_addr01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class FisPeriodRule implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "fisPeriodRule_s1")
 
 @Column(name="period_rule_id")
 private Long id;
 @Column(name="rule")
 private String periodRule;
 @Column(name="rule_descr")
 private String periodDescription;
 @Column(name="num_periods_std")
 private int numPeriods;
 @Column(name="num_periods_audit")
 private int numAuditPeriods;
 @Column(name="calendar_basis")
 private boolean calendarMonthBasis;
 @Column(name="calendar_natural")
 private boolean calendarNatural;
 @Column(name="fixed_day_basis")
 private boolean fixedDayOfMonthBasis;
 @Column(name="fixed_len")
 private boolean periodLenBasis;
 @Column(name="annual_date_basis")
 private boolean annualDateScheduleBasis;
 @OneToMany(mappedBy = "periodRule")
 private List<CompanyBasic> companys;
 @ManyToOne
 @JoinColumn(name="cal_rul_id", referencedColumnName="fis_per_cal_base_id")
 private CalendarRuleBase calRule;
 @ManyToOne
 @JoinColumn(name="ANNUAL_SCHED_ID", referencedColumnName="period_yr_date_id")
 private FisPeriodYearlyDate annualSchedule;
 @OneToMany(mappedBy = "perRule")
 private List<AuditFisPeriodRule> auditRecs;
 
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createDate;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changeDate;
 @Version
 @Column(name="revision")
 private int revision;
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createBy;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changeBy;
 

 public List<AuditFisPeriodRule> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditFisPeriodRule> auditRecs) {
  this.auditRecs = auditRecs;
 }


    
 

    
 

    
 public FisPeriodRule() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public FisPeriodYearlyDate getAnnualSchedule() {
  return annualSchedule;
 }

 public void setAnnualSchedule(FisPeriodYearlyDate annualSchedule) {
  this.annualSchedule = annualSchedule;
 }

 public String getPeriodRule() {
  return periodRule;
 }

 public void setPeriodRule(String periodRule) {
  this.periodRule = periodRule;
 }

 public String getPeriodDescription() {
  return periodDescription;
 }

 public void setPeriodDescription(String periodDescription) {
  this.periodDescription = periodDescription;
 }

 public int getNumPeriods() {
  return numPeriods;
 }

 public void setNumPeriods(int numPeriods) {
  this.numPeriods = numPeriods;
 }

 public int getNumAuditPeriods() {
  return numAuditPeriods;
 }

 public void setNumAuditPeriods(int numAuditPeriods) {
  this.numAuditPeriods = numAuditPeriods;
 }

 public boolean isCalendarMonthBasis() {
  return calendarMonthBasis;
 }

 public void setCalendarMonthBasis(boolean calendarMonthBasis) {
  this.calendarMonthBasis = calendarMonthBasis;
 }

 public boolean isCalendarNatural() {
  return calendarNatural;
 }

 public void setCalendarNatural(boolean calendarNatural) {
  this.calendarNatural = calendarNatural;
 }

 public boolean isFixedDayOfMonthBasis() {
  return fixedDayOfMonthBasis;
 }

 public void setFixedDayOfMonthBasis(boolean fixedDayOfMonthBasis) {
  this.fixedDayOfMonthBasis = fixedDayOfMonthBasis;
 }

 public boolean isPeriodLenBasis() {
  return periodLenBasis;
 }

 public void setPeriodLenBasis(boolean periodLenBasis) {
  this.periodLenBasis = periodLenBasis;
 }

 public boolean isAnnualDateScheduleBasis() {
  return annualDateScheduleBasis;
 }

 public void setAnnualDateScheduleBasis(boolean annualDateScheduleBasis) {
  this.annualDateScheduleBasis = annualDateScheduleBasis;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public Date getChangeDate() {
  return changeDate;
 }

 public void setChangeDate(Date changeDate) {
  this.changeDate = changeDate;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public User getCreateBy() {
  return createBy;
 }

 public void setCreateBy(User createBy) {
  this.createBy = createBy;
 }

 public User getChangeBy() {
  return changeBy;
 }

 public void setChangeBy(User changeBy) {
  this.changeBy = changeBy;
 }

 public CalendarRuleBase getCalRule() {
  return calRule;
 }

 public void setCalRule(CalendarRuleBase calRule) {
  this.calRule = calRule;
 }

 public List<CompanyBasic> getCompanys() {
  return companys;
 }

 public void setCompanys(List<CompanyBasic> companys) {
  this.companys = companys;
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
  if (!(object instanceof FisPeriodRule)) {
   return false;
  }
  FisPeriodRule other = (FisPeriodRule) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.company.FisPeriodRule[ id=" + id + " ]";
 }
 
}
