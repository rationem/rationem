/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditDocRevReason;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocLineBase;
import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;


import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;

/**
 *
 * @author user
 */
@Entity
@Table(name="bac_config23")
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "docRevReason_s1", sequenceName = "bac_config23_s1", allocationSize = 1,initialValue=1)
public class DocReversalReason implements Serializable {
 private static final long serialVersionUID = 1L;
 
 @Id
 @GeneratedValue(generator = "docRevReason_s1")
 @Column(name="doc_rev_id")
 private Long id;
 
 @Column(name="code")
 private String code;
 @Column(name="descr")
 private String description;
 
 @OneToMany(mappedBy = "docReversalReason")
 List<DocBase> reversedDocs;
 @Column(name="sys_use")
 private boolean sysUse;
 
 
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;
 
 @OneToMany(mappedBy = "docRevReason")
 private List<AuditDocRevReason> auditRecs;

 public DocReversalReason() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditDocRevReason> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditDocRevReason> auditRecs) {
  this.auditRecs = auditRecs;
 }

 
 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public List<DocBase> getReversedDocs() {
  return reversedDocs;
 }

 public void setReversedDocs(List<DocBase> reversedDocs) {
  this.reversedDocs = reversedDocs;
 }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
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
 
 
}
