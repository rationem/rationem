/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

import com.rationem.entity.audit.AuditCompPostPer;
import com.rationem.entity.config.company.Ledger;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.UniqueConstraint;

import com.rationem.entity.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.ManyToOne;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.OneToMany;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author user
 */
@Entity
@Table(name="comp09", uniqueConstraints=
        @UniqueConstraint(columnNames={"comp_id", "ledger_id"}))
@NamedQueries({
 @NamedQuery(name="postPerByComp", query="Select p from CompPostPer p where p.comp.id = :compId"),
 @NamedQuery(name="postPerByCompLed", 
        query="Select p from CompPostPer p where p.comp.id = :compId and p.ledger.id = :ledId")
 })
@SequenceGenerator(name = "compPostPer_s1", sequenceName = "comp09_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class CompPostPer implements Serializable {
 @OneToMany(mappedBy = "compPostingPeriod")
 private List<AuditCompPostPer> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @Column(name="comp_post_per_id")
 @GeneratedValue(generator = "compPostPer_s1")
 private Long id;
 
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id", nullable=false)
 private CompanyBasic comp;
 
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id", nullable=false)
 private Ledger ledger;
 @Column(name="start_compressed")
 private int startLong;
 @Column(name="end_compressed")
 private int endLong;
 @Column(name="start_year")
 private int startYear;
 @Column(name="start_period")
 private int startPer;
 @Column(name="end_year")
 private int endYear;
 @Column(name="end_period")
 private int endPer;
 
 @ManyToOne
 @JoinColumn(name="created_by_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;
 
 

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCompPostPer> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditCompPostPer> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
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

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
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
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof CompPostPer)) {
   return false;
  }
  CompPostPer other = (CompPostPer) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.CompPostPer[ id=" + id + " ]";
 }
 
}
