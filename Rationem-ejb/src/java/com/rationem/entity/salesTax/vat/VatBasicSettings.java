/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import com.rationem.entity.config.common.Uom;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Collection;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.Temporal;

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
@Table(name="fi_config03")
@SequenceGenerator(name = "vatBasicSettings_s1", sequenceName = "fi_config03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class VatBasicSettings implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatBasicSettings_s1")
 @Column(name="vat_basic_id")
 private Long id;
 @Column(name="country_code")
 private String countryCode;
 @Column(name="country_num")
 private String countryNumber;
 @ManyToOne
 @JoinColumn(name="paper_ret_due_uom_id",referencedColumnName="uom_id")
 private Uom returnPaperUom;
 @Column(name="paper_ret_due_value")
 private int returnPaperValue;
 @ManyToOne
 @JoinColumn(name="online_add_time_uom_id",referencedColumnName="uom_id")
 private Uom onLineOffsetUom;
 @Column(name="online_add_time_value")
 private int onLineOffsetValue;
 
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="revision")
 private int changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCountryCode() {
  return countryCode;
 }

 public void setCountryCode(String countryCode) {
  this.countryCode = countryCode;
 }

 public String getCountryNumber() {
  return countryNumber;
 }

 public void setCountryNumber(String countryNumber) {
  this.countryNumber = countryNumber;
 }

 public Uom getReturnPaperUom() {
  return returnPaperUom;
 }

 public void setReturnPaperUom(Uom returnPaperUom) {
  this.returnPaperUom = returnPaperUom;
 }

 public int getReturnPaperValue() {
  return returnPaperValue;
 }

 public void setReturnPaperValue(int returnPaperValue) {
  this.returnPaperValue = returnPaperValue;
 }

 public Uom getOnLineOffsetUom() {
  return onLineOffsetUom;
 }

 public void setOnLineOffsetUom(Uom onLineOffsetUom) {
  this.onLineOffsetUom = onLineOffsetUom;
 }

 public int getOnLineOffsetValue() {
  return onLineOffsetValue;
 }

 public void setOnLineOffsetValue(int onLineOffsetValue) {
  this.onLineOffsetValue = onLineOffsetValue;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof VatBasicSettings)) {
   return false;
  }
  VatBasicSettings other = (VatBasicSettings) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatBasicSettings[ id=" + id + " ]";
 }
 
}
