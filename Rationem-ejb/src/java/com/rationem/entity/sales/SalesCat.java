/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.sales;

import com.rationem.entity.audit.AuditSalesCatelog;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.Version;
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
@Table(name="sl_mast01")
@NamedQueries({
 @NamedQuery(name="allSalesCategories", query="Select sc from SalesCat sc"),
 @NamedQuery(name="salesCatsByComp", query="Select sc from SalesCat sc where sc.company.id = :compId"),
 @NamedQuery(name="salesCategoriesByCode", query="Select sc from SalesCat sc where sc.code like :cd")
        
})
@SequenceGenerator(name = "salesCat_s1", sequenceName = "sl_mast01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class SalesCat implements Serializable {

 @OneToMany(mappedBy = "cat")
 private List<AuditSalesCatelog> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "costCentre_s1")
 @Column(name="sl_cat_id")
 private Long id;
 @Column(name="code")
 private String code;
 @Column(name="short_descr")
 private String sortDescr;
 @Column(name="long_descr")
 private String longDescr;
 @OneToMany(mappedBy = "category")
 private List<SalesPartCompany> salesParts;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic company;
 @OneToMany(mappedBy = "salesCatParent")
 private List<SalesCat> subCategories;
 @ManyToOne
 @JoinColumn(name="parent_cat_id", referencedColumnName="sl_cat_id")
 private SalesCat salesCatParent;
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
 private int changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditSalesCatelog> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditSalesCatelog> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getSortDescr() {
  return sortDescr;
 }

 public void setSortDescr(String sortDescr) {
  this.sortDescr = sortDescr;
 }

 public String getLongDescr() {
  return longDescr;
 }

 public void setLongDescr(String longDescr) {
  this.longDescr = longDescr;
 }

 public List<SalesPartCompany> getSalesParts() {
  return salesParts;
 }

 public void setSalesParts(List<SalesPartCompany> salesParts) {
  this.salesParts = salesParts;
 }

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public List<SalesCat> getSubCategories() {
  return subCategories;
 }

 public void setSubCategories(List<SalesCat> subCategories) {
  this.subCategories = subCategories;
 }

 public SalesCat getSalesCatParent() {
  return salesCatParent;
 }

 public void setSalesCatParent(SalesCat salesCatParent) {
  this.salesCatParent = salesCatParent;
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
  if (!(object instanceof SalesCat)) {
   return false;
  }
  SalesCat other = (SalesCat) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.sales.SalesCat[ id=" + id + " ]";
 }
 
}
