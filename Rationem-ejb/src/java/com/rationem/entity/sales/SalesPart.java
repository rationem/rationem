/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.sales;

//import com.rationem.entity.audit.AuditSalesPart;
import java.util.List;
import com.rationem.entity.user.User;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Date;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@NamedQueries({
 @NamedQuery(name="allSalesParts", query="Select p from SalesPart p "),
 @NamedQuery(name="partsByCodePartial",query="Select p from SalesPart p where p.partCode like :pcode")
})
@Table(name="sl_mast02")
@SequenceGenerator(name = "salesPart_s1", sequenceName = "sl_mast02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class SalesPart implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "salesPart_s1")
 @Column(name="sales_part_id")
 private Long id;
 @Column(name="part_code")
 private String partCode;
 @Column(name="short_descr")
 private String shortDescription;
 @Column(name="extern_descr")
 private String externalDescription;
 @Column(name="physical")
 private boolean physicalPart;
 @OneToMany(mappedBy = "part")
 private List<SalesPartCompany> salesPartCompany;
 /*
 @OneToMany(mappedBy = "part")
 private List<AuditSalesPart> auditRecords;
 */
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private long changes;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getPartCode() {
  return partCode;
 }

 public void setPartCode(String partCode) {
  this.partCode = partCode;
 }

 public String getShortDescription() {
  return shortDescription;
 }

 public void setShortDescription(String shortDescription) {
  this.shortDescription = shortDescription;
 }

 public String getExternalDescription() {
  return externalDescription;
 }

 public void setExternalDescription(String externalDescription) {
  this.externalDescription = externalDescription;
 }

 public boolean isPhysicalPart() {
  return physicalPart;
 }

 public void setPhysicalPart(boolean physicalPart) {
  this.physicalPart = physicalPart;
 }

 public List<SalesPartCompany> getSalesPartCompany() {
  return salesPartCompany;
 }

 public void setSalesPartCompany(List<SalesPartCompany> salesPartCompany) {
  this.salesPartCompany = salesPartCompany;
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
  if (!(object instanceof SalesPart)) {
   return false;
  }
  SalesPart other = (SalesPart) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.sales.SalesPart[ id=" + id + " ]";
 }
 
}
