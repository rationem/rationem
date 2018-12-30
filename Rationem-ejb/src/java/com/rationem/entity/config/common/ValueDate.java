/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Version;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 * The number of dates after presentation to the bank that funds are available.
 * Cheque = 3 days, building society - 5 day BACS = 0 days (Immediate) 
 * @author Chris
 */
@Entity
@Table(name="bac_config15")
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "ValueDate_s1", sequenceName = "bac_config15_s1", allocationSize = 1,initialValue=1)

public class ValueDate implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "ValueDate_s1")
 @Column(name="value_date_rule_id")
 private Long id;
 @Column(name="num")
 private int number;
 @Column(name="code")
 private String code;
 @Column(name="descr")
 private String description;
 @Column(name="offset_from")
 private int offset;
 @ManyToOne
 @JoinColumn(name="uom_id", referencedColumnName="uom_id")
 private Uom uom;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")    
 User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 Date changedOn;
 @Version
 @Column(name="changes")
 long changes;

 public ValueDate() {
 }

 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public int getNumber() {
  return number;
 }

 public void setNumber(int number) {
  this.number = number;
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

 public int getOffset() {
  return offset;
 }

 public void setOffset(int offset) {
  this.offset = offset;
 }

 public Uom getUom() {
  return uom;
 }

 public void setUom(Uom uom) {
  this.uom = uom;
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
  if (!(object instanceof ValueDate)) {
   return false;
  }
  ValueDate other = (ValueDate) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.ValueDate[ id=" + id + " ]";
 }
 
}
