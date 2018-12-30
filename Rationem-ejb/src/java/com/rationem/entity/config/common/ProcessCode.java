/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditProcessCode;
import com.rationem.entity.config.company.AccountType;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.NamedQuery;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 *
 * @author Chris
 */
@Entity
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "ProcessCode_s1", sequenceName = "bac_config16_s1", allocationSize = 1,initialValue=1)
@Table(name="bac_config16" )
@NamedQueries({
 @NamedQuery(name="processCodesAll", query="Select p from ProcessCode p"),
 @NamedQuery(name="processCodeByName", query="Select p from ProcessCode p where p.name = :name")
})
public class ProcessCode implements Serializable {
 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "ProcessCode_s1")
 @Column(name="process_code_id")
 private Long id;
 @OneToMany(mappedBy = "processCode")
 private List<AccountType> accountTypes;
 @OneToMany(mappedBy = "processCode")
 private List<AuditProcessCode> auditRecords;
 @Column(name="name")
 private String name;
 @Column(name="description")
 private String description;
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id")
 private Module module;
 @ManyToOne
 @JoinColumn(name="created_by_id" , referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedDate;
 @Version
 @Column(name="changed")
 private long changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AccountType> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountType> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public List<AuditProcessCode> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditProcessCode> auditRecords) {
  this.auditRecords = auditRecords;
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

 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
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
  if (!(object instanceof ProcessCode)) {
   return false;
  }
  ProcessCode other = (ProcessCode) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.ProcessCode[ id=" + id + " ]";
 }
 
}
