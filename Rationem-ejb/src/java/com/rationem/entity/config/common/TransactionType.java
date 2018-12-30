/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditTransactionType;
import com.rationem.entity.config.company.Ledger;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.util.Date;
import javax.persistence.Temporal;
import com.rationem.entity.user.User;
import java.util.List;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config13")
@NamedQueries({
 @NamedQuery(name="allTransTypes", query="select t from TransactionType t" ),
 @NamedQuery(name="ledgerTransTypes", query="select t from TransactionType t where t.ledger.code = :ledCd" )
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "TransactionType_s1", sequenceName = "bac_config13_s1", allocationSize = 1,initialValue=1)

public class TransactionType implements Serializable {
 @OneToMany(mappedBy = "transTy")
 private List<AuditTransactionType> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "TransactionType_s1")
 @Column(name="tran_type_id")
 private Long id;
 @Column(name="code")
 private String code;
 @Column(name="short_desc")
 private String shortDescription;
 @Column(name="full_descr")
 private String description;
 @ManyToOne
 @JoinColumn(name="ledger_id" , referencedColumnName="ledger_id")
 private Ledger ledger;
 
 @Column(name="process_code")
 private String processCode;
 @ManyToOne
 @JoinColumn(name="created_by_id" , referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id" , referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int revision;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getShortDescription() {
  return shortDescription;
 }

 public void setShortDescription(String shortDescription) {
  this.shortDescription = shortDescription;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public String getProcessCode() {
  return processCode;
 }

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }

 public void setProcessCode(String processCode) {
  this.processCode = processCode;
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
  if (!(object instanceof TransactionType)) {
   return false;
  }
  TransactionType other = (TransactionType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.TransactionType[ id=" + id + " ]";
 }
 
}
//