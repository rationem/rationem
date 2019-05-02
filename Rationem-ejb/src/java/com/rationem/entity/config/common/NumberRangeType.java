/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditNumberRangeType;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 *
 * @author user
 */

@Entity
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "NumberRangeForType_s1", sequenceName = "bac_config25_s1", allocationSize = 1,initialValue=1)
@Table(name="bac_config25" )
@NamedQuery(name="allNumRangeTypes", query="Select nr from NumberRangeType nr")
public class NumberRangeType implements Serializable {

 @OneToMany(mappedBy = "nrTy")
 private List<AuditNumberRangeType> auditRecords;

 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "NumberRangeForType_s1")
 @Column(name="id")
 private Long id;
 
 @Column(name="num_range_type_code", length=20)
 private String Code;
 @Column(name="num_range_type_name", length=70)
 private String Name;
 
 
 @OneToMany(mappedBy = "numberRangeType")
 private List<NumberRange> numberRanges;
 
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

 public String getCode() {
  return Code;
 }

 public void setCode(String Code) {
  this.Code = Code;
 }

 public String getName() {
  return Name;
 }

 public void setName(String Name) {
  this.Name = Name;
 }

 public List<NumberRange> getNumberRanges() {
  return numberRanges;
 }

 public void setNumberRanges(List<NumberRange> numberRanges) {
  this.numberRanges = numberRanges;
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
  if (!(object instanceof NumberRangeType)) {
   return false;
  }
  NumberRangeType other = (NumberRangeType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.NumberRangeObject[ id=" + id + " ]";
 }
 
}
