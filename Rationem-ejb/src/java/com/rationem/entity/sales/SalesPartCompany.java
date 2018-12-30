/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.sales;

//import com.rationem.entity.audit.AuditSalesPartCompany;
import com.rationem.entity.config.common.Uom;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import java.util.List;
import com.rationem.entity.user.User;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Date;
import java.io.Serializable;
import javax.persistence.*;

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
@Table(name="sl_mast03")
@NamedQueries({
 @NamedQuery(name="allCompSalesParts",query="select p from SalesPartCompany p where p.active = true  order by p.part.partCode "),
 @NamedQuery(name="compSalesPartsByComp", query="Select p from SalesPartCompany p where p.active = true and p.company.id = :compId" )
})
@SequenceGenerator(name = "salesPartCompany_s1", sequenceName = "sl_mast03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class SalesPartCompany implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "salesPartCompany_s1")
 @Column(name="sale_part_comp_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="sales_part_id", referencedColumnName="sales_part_id")
 private SalesPart part; 
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic company;
 /*
 @OneToMany(mappedBy = "compPart")
 private List<AuditSalesPartCompany> auditRecords;
 */
 @ManyToOne
 @JoinColumn(name="sl_cat_id", referencedColumnName="sl_cat_id")
 private SalesCat category;
 @ManyToOne
 @JoinColumn(name="revenue_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp salesAccount;
 @ManyToOne
 @JoinColumn(name="fnd_id", referencedColumnName="restricted_fund_id")
 private Fund fund;
 @ManyToOne
 @JoinColumn(name="sale_unit", referencedColumnName="uom_id")
 private Uom uom;
 @Column(name="sale_amount")
 private double saleValue;
 @Column(name="stock_amount")
 private double stockValue;
 @Column(name="cost_amount")
 private double costValue;
 @ManyToOne
 @JoinColumn(name="cos_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp cosAccount;
 @ManyToOne
 @JoinColumn(name="stock_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp stockAccount;
 @Column(name="cos_accounting")
 private boolean costOfSalesAccounting;
 @OneToMany(mappedBy = "salesPart")
 private List<DocLineGl> GlDocLines;
 @Temporal(DATE)
 @Column(name="valid_to")
 private Date validTo;
 @Column(name="active")
 private boolean active;
 
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

 public SalesPart getPart() {
  return part;
 }

 public void setPart(SalesPart part) {
  this.part = part;
 }

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public SalesCat getCategory() {
  return category;
 }

 public void setCategory(SalesCat category) {
  this.category = category;
 }

 public FiGlAccountComp getSalesAccount() {
  return salesAccount;
 }

 public void setSalesAccount(FiGlAccountComp salesAccount) {
  this.salesAccount = salesAccount;
 }

 public Fund getFund() {
  return fund;
 }

 public void setFund(Fund fund) {
  this.fund = fund;
 }

 public Uom getUom() {
  return uom;
 }

 public void setUom(Uom uom) {
  this.uom = uom;
 }

 public double getSaleValue() {
  return saleValue;
 }

 public void setSaleValue(double saleValue) {
  this.saleValue = saleValue;
 }

 public double getStockValue() {
  return stockValue;
 }

 public void setStockValue(double stockValue) {
  this.stockValue = stockValue;
 }

 public FiGlAccountComp getCosAccount() {
  return cosAccount;
 }

 public void setCosAccount(FiGlAccountComp cosAccount) {
  this.cosAccount = cosAccount;
 }

 public double getCostValue() {
  return costValue;
 }

 public void setCostValue(double costValue) {
  this.costValue = costValue;
 }

 
 public FiGlAccountComp getStockAccount() {
  return stockAccount;
 }

 public void setStockAccount(FiGlAccountComp stockAccount) {
  this.stockAccount = stockAccount;
 }

 public boolean isCostOfSalesAccounting() {
  return costOfSalesAccounting;
 }

 public void setCostOfSalesAccounting(boolean costOfSalesAccounting) {
  this.costOfSalesAccounting = costOfSalesAccounting;
 }

 public List<DocLineGl> getGlDocLines() {
  return GlDocLines;
 }

 public void setGlDocLines(List<DocLineGl> GlDocLines) {
  this.GlDocLines = GlDocLines;
 }

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
 }

 public boolean isActive() {
  return active;
 }

 public void setActive(boolean active) {
  this.active = active;
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
  if (!(object instanceof SalesPartCompany)) {
   return false;
  }
  SalesPartCompany other = (SalesPartCompany) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.sales.SalesPartCompany[ id=" + id + " ]";
 }
 
}
