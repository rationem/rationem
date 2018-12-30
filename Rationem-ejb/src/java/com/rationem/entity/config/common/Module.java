/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditModule;
import com.rationem.entity.config.company.AccountType;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.user.User;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.ManyToOne;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="module01" )
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "Module_s1", sequenceName = "module01_s1", allocationSize = 1,initialValue=1)
@NamedQueries({
 @NamedQuery(name="getModulesByCriteria",
  query="Select m from Module m "+
        "where m.name like :name and m.description like :descr"+
        " and m.moduleCode like :modCode"),
 @NamedQuery(name="allModules", query="Select m from Module m ")
})
public class Module implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "Module_s1")
 @Column(name="module_id")
 private Long id;
 @Column(name="module_name")
 private String name;
 @Column(name="descr")
 private String description;
 @Column(name="module_code")
 private String moduleCode;
 @OneToMany(cascade=ALL ,mappedBy = "module")
 Collection<LineTypeRule> lineTypes;
 @OneToMany(mappedBy = "module" )
 private List<NumberRange> numberControl;

 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="revision")
 private int revision;
 @OneToMany(mappedBy = "module", cascade=CascadeType.REMOVE)
 private List<AuditModule> auditRecords;
 
 @OneToMany(mappedBy = "module")
 private List<AccountType> accountTypes;
 @OneToMany(mappedBy = "module")
 private List<ProcessCode> processCodes;
 @OneToMany(mappedBy = "module")
 private List<PostType> postTypes;

 public Module() {
 }
    


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditModule> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditModule> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public List<AccountType> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountType> accountTypes) {
  this.accountTypes = accountTypes;
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public List<NumberRange> getNumberControl() {
  return numberControl;
 }

 public void setNumberControl(List<NumberRange> numberControl) {
  this.numberControl = numberControl;
 }

 public List<ProcessCode> getProcessCodes() {
  return processCodes;
 }

 public void setProcessCodes(List<ProcessCode> processCodes) {
  this.processCodes = processCodes;
 }

 public List<PostType> getPostTypes() {
  return postTypes;
 }

 public void setPostTypes(List<PostType> postTypes) {
  this.postTypes = postTypes;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public String getModuleCode() {
  return moduleCode;
 }

 public void setModuleCode(String moduleCode) {
  this.moduleCode = moduleCode;
 }

 public Collection<LineTypeRule> getLineTypes() {
  return lineTypes;
 }

 public void setLineTypes(Collection<LineTypeRule> lineTypes) {
  this.lineTypes = lineTypes;
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
  if (!(object instanceof Module)) {
   return false;
  }
  Module other = (Module) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.Module[ id=" + id + " ]";
 }
 
}
