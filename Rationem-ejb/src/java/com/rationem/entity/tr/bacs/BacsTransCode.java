/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bacs;

//import com.rationem.entity.audit.AuditBacsTransCode;
import com.rationem.entity.audit.AuditBacsTransCode;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.document.DocBankLineBacs;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="bnk06")
@NamedQueries({
 @NamedQuery(name="BacsTransAll", query="select b from BacsTransCode b"),
 @NamedQuery(name="BacsTransDD", query="select b from BacsTransCode b where b.collection = true"),
 @NamedQuery(name="BacsTransDC", query="select b from BacsTransCode b where b.collection = false")
})
@SequenceGenerator(name = "bacsTransCode_s1", sequenceName = "bnk06_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class BacsTransCode implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bacsTransCode_s1")
 @Column(name="dd_trans_cd_id")
 private Long id;
 /**
  * 2 digit BACS code for partner bank transaction
  */
 @Column(name="dd_trans_code")
 private String ptnrBnkTransCode;
 @Column(name="contra_trans_cd")
 private String contrTransCode;
 /**
  * short description
  */
 @Column(name="name")
 private String name;
 /**
  * Long description
  */
 @Column(name="description")
 private String description;
 /**
  * If true this is a direct collection - claim money from 3rd party's bank
  * If false this is a direct payment - payment to a 3rd party
  */
 @Column(name="collection")
 private boolean collection;
 @Column(name="debit")
 private boolean debit;
 @Column(name="process_code")
 private String prCode;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="CHANGED_BY_ID", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int changes;
 
 @OneToMany(mappedBy = "transCode", cascade= CascadeType.REMOVE)
 private List<AuditBacsTransCode> auditRecords;
 
 @OneToMany(mappedBy = "bacsTransCode")
 private List<PaymentType> paymentTypes;
 @OneToMany(mappedBy = "bacsTransCode")
 private List<DocBankLineBacs> bacsDocBankLines;



 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditBacsTransCode> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditBacsTransCode> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getPtnrBnkTransCode() {
  return ptnrBnkTransCode;
 }

 public void setPtnrBnkTransCode(String ptnrBnkTransCode) {
  this.ptnrBnkTransCode = ptnrBnkTransCode;
 }

 public String getContrTransCode() {
  return contrTransCode;
 }

 public void setContrTransCode(String contrTransCode) {
  this.contrTransCode = contrTransCode;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

 
 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isCollection() {
  return collection;
 }

 public void setCollection(boolean collection) {
  this.collection = collection;
 }

 public String getPrCode() {
  return prCode;
 }

 public void setPrCode(String prCode) {
  this.prCode = prCode;
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

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public List<DocBankLineBacs> getBacsDocBankLines() {
  return bacsDocBankLines;
 }

 public void setBacsDocBankLines(List<DocBankLineBacs> bacsDocBankLines) {
  this.bacsDocBankLines = bacsDocBankLines;
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
  if (!(object instanceof BacsTransCode)) {
   return false;
  }
  BacsTransCode other = (BacsTransCode) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bacs.BacsTransCode[ id=" + id + " ]";
 }
 
}
