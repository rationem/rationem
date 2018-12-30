/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditChequeVoidReason;
import com.rationem.entity.document.DocBankLineChq;
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
import javax.persistence.NamedQuery;

/**
 *
 * @author user
 */
@Entity
@Table(name="bac_config22")
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "VoidReason_s1", sequenceName = "bac_config22_s1", allocationSize = 1,initialValue=1)
@NamedQuery(name="chqVoidRsnAll", query="select v from ChequeVoidReason v")
public class ChequeVoidReason implements Serializable {
 private static final long serialVersionUID = 1L;
 
 @Id
 @GeneratedValue(generator = "VoidReason_s1")
 @Column(name="void_reason_id")
 private Long id;
 
 @Column(name="code")
 private String code;
 @Column(name="descr")
 private String description;
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
 @OneToMany(mappedBy = "voidReason")
 private List<AuditChequeVoidReason> auditRecords;
 @OneToMany(mappedBy = "voidReason")
 private List<DocBankLineChq> voidedCheques;

 public ChequeVoidReason() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditChequeVoidReason> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditChequeVoidReason> auditRecords) {
  this.auditRecords = auditRecords;
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

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
 }

 public List<DocBankLineChq> getVoidedCheques() {
  return voidedCheques;
 }

 public void setVoidedCheques(List<DocBankLineChq> voidedCheques) {
  this.voidedCheques = voidedCheques;
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
