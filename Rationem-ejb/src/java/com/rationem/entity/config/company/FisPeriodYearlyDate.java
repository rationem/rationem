/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.user.User;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Date;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config04" )
@SequenceGenerator(name = "fisPeriodYearlyDate_s1", sequenceName = "bac_config04_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class FisPeriodYearlyDate implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "fisPeriodYearlyDate_s1")
 @Column(name="period_yr_date_id")
 private Long id;
 @Column(name="fis_year")
 private int fiscalYear;
 @Temporal(DATE)
 @Column(name="start_date")
 private Date startDate;
 @Temporal(DATE)
 @Column(name="end_date")
 private Date endDate;
 @Column(name="period_num")
 private int periodNum;
 @Column(name="special_period_num")
 private int specialPeriodNum;
 @Version
 @Column(name="changes")
 private Long revision;
 @Temporal(TIMESTAMP)
 @Column(name="create_date")
 private Date createDate;
 @Temporal(TIMESTAMP)
 @Column(name="change_date")
 private Date ChangeDate;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @OneToMany(mappedBy = "annualSchedule")
 private List<FisPeriodRule> fisPeriodRules;

 public FisPeriodYearlyDate() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public int getFiscalYear() {
  return fiscalYear;
 }

 public void setFiscalYear(int fiscalYear) {
  this.fiscalYear = fiscalYear;
 }

 public Date getStartDate() {
  return startDate;
 }

 public void setStartDate(Date startDate) {
  this.startDate = startDate;
 }

 public Date getEndDate() {
  return endDate;
 }

 public void setEndDate(Date endDate) {
  this.endDate = endDate;
 }

 public int getPeriodNum() {
  return periodNum;
 }

 public void setPeriodNum(int periodNum) {
  this.periodNum = periodNum;
 }

 public int getSpecialPeriodNum() {
  return specialPeriodNum;
 }

 public void setSpecialPeriodNum(int specialPeriodNum) {
  this.specialPeriodNum = specialPeriodNum;
 }

 public Long getRevision() {
  return revision;
 }

 public void setRevision(Long revision) {
  this.revision = revision;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public Date getChangeDate() {
  return ChangeDate;
 }

 public void setChangeDate(Date ChangeDate) {
  this.ChangeDate = ChangeDate;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public List<FisPeriodRule> getFisPeriodRules() {
  return fisPeriodRules;
 }

 public void setFisPeriodRules(List<FisPeriodRule> fisPeriodRules) {
  this.fisPeriodRules = fisPeriodRules;
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
  if (!(object instanceof FisPeriodYearlyDate)) {
   return false;
  }
  FisPeriodYearlyDate other = (FisPeriodYearlyDate) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.FisPeriodYearlyDate[ id=" + id + " ]";
 }
 
}
