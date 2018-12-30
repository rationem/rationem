/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditSortOrder;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import java.util.Date;
import javax.persistence.Column;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config09")
@NamedQueries({ 
@NamedQuery(name = "findAllSortOrders",query = "SELECT s FROM SortOrder s"),
@NamedQuery(name = "sortOrdBySortCd",query = "SELECT s FROM SortOrder s where s.sortCode = :sc")
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "SortOrder_s1", sequenceName = "bac_config09_s1", allocationSize = 1,initialValue=1)

public class SortOrder implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "SortOrder_s1")
 @Column(name="sort_order_id")
 private Long id;
 @Column(name="sort_code")
  private String sortCode;
 @Column(name="sort_name")
  private String name;
 @Column(name="long_descr")
  private String description;

  @ManyToOne
  @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
  private User createdBy;
  @Temporal(TIMESTAMP)
  @Column(name="created_on")
  private Date createdOn;
  @ManyToOne
  @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
  private User updatedBy;
  @Temporal(TIMESTAMP)
  @Column(name="changed_on")
  private Date updatedOn;
  @Version
  @Column(name="changes")
  private int revision;
  
 @OneToMany(mappedBy = "sortOrder")
 private List<ArAccount> arAccounts;
 @OneToMany(mappedBy = "sortOrder")
 private List<ApAccount> apAccounts;
 @OneToMany(mappedBy = "sortOrder")
 private List<AuditSortOrder> auditRecords;

  public SortOrder() {
  }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<ArAccount> getArAccounts() {
  return arAccounts;
 }

 public void setArAccounts(List<ArAccount> arAccounts) {
  this.arAccounts = arAccounts;
 }

 public List<ApAccount> getApAccounts() {
  return apAccounts;
 }

 public void setApAccounts(List<ApAccount> apAccounts) {
  this.apAccounts = apAccounts;
 }

 public List<AuditSortOrder> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditSortOrder> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getSortCode() {
  return sortCode;
 }

 public void setSortCode(String sortCode) {
  this.sortCode = sortCode;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
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

 public User getUpdatedBy() {
  return updatedBy;
 }

 public void setUpdatedBy(User updatedBy) {
  this.updatedBy = updatedBy;
 }

 public Date getUpdatedOn() {
  return updatedOn;
 }

 public void setUpdatedOn(Date updatedOn) {
  this.updatedOn = updatedOn;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
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
  if (!(object instanceof SortOrder)) {
   return false;
  }
  SortOrder other = (SortOrder) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.SortOrder[ id=" + id + " ]";
 }
 
}
