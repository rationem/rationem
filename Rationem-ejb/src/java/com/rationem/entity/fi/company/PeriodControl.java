/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

import com.rationem.entity.user.User;
import com.rationem.entity.config.company.Ledger;
import javax.persistence.Table;
import java.util.Collection;
import javax.persistence.Version;
import javax.persistence.Column;
import com.rationem.entity.user.UserLogin;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToOne;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="comp06" )
@SequenceGenerator(name = "periodControl_s1", sequenceName = "comp06_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class PeriodControl implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "periodControl_s1")
 @Column(name="period_cntrl_id")
 private Long id;
 
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id")
 private Ledger ledger;
 @Column(name="fiscal_period_from")
 private int periodFrom;
 @Column(name="fiscal_period_to")
 private int periodTo;
 @Column(name="fiscal_year_from")
 private int yearFrom;
 @Column(name="fiscal_year_to")
 private int yearTo;
 @Version
 @Column(name="changes")
 private int revision;

 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @ManyToOne
 @JoinColumn(name="created_by", referencedColumnName="partner_id")
 private User createdBy;
 @ManyToOne
 @JoinColumn(name="chnaged_by", referencedColumnName="partner_id")
 private User changedBy;
 @ManyToOne
 @JoinColumn(name="company_id", referencedColumnName="home_comp_id")
 private CompanyBasic company;

 public PeriodControl() {
    }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }

 public int getPeriodFrom() {
  return periodFrom;
 }

 public void setPeriodFrom(int periodFrom) {
  this.periodFrom = periodFrom;
 }

 public int getPeriodTo() {
  return periodTo;
 }

 public void setPeriodTo(int periodTo) {
  this.periodTo = periodTo;
 }

 public int getYearFrom() {
  return yearFrom;
 }

 public void setYearFrom(int yearFrom) {
  this.yearFrom = yearFrom;
 }

 public int getYearTo() {
  return yearTo;
 }

 public void setYearTo(int yearTo) {
  this.yearTo = yearTo;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
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

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
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
  if (!(object instanceof PeriodControl)) {
   return false;
  }
  PeriodControl other = (PeriodControl) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.PeriodControl[ id=" + id + " ]";
 }
 
}
