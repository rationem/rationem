/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.sales;

import com.rationem.entity.audit.AuditSalesInfo;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocInvoiceAr;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

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
@Table(name="sl_info01")
@SequenceGenerator(name = "salesInfo_s1", sequenceName = "sl_info01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class SalesInfo implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "salesInfo_s1")
 @Column(name="sl_info_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic comp;
 
 
 @Column(name="invoice")
 private boolean invoiceDoc;
 
 @Column(name="fisc_yr")
 private int fiscalYear;
 @Column(name="fisc_per")
 private int fiscalPeriod;
 @Temporal(DATE)
 @Column(name="doc_date")
 private Date docDate;
 @Temporal(DATE)
 @Column(name="post_date")
 private Date postingDate;
 @Temporal(DATE)
 @Column(name="due_date")
 private Date dueDate;
 @ManyToOne
 @JoinColumn(name="sold_to_id", referencedColumnName="partner_id")
 private PartnerBase partner;
 @ManyToOne
 @JoinColumn(name="ordered_by_id", referencedColumnName="partner_id")
 private PartnerPerson orderedBy;
 
 @ManyToOne
 @JoinColumn(name="ar_account_id", referencedColumnName="ar_account_id")
 private ArAccount arAccount;
 
 @ManyToOne
 @JoinColumn(name="fi_doc_id", referencedColumnName="doc_id")
 private DocFi fiDoc;
 
 @ManyToOne
 @JoinColumn(name="invoice_id", referencedColumnName="ar_inv_id")
 private DocInvoiceAr invoice;
 
 @ManyToOne
 @JoinColumn(name="revenue_gl_accnt_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp salesGlAc;
 @ManyToOne
 @JoinColumn(name="sales_cat_id", referencedColumnName="sl_cat_id")
 private SalesCat salesCategory;
 @ManyToOne
 @JoinColumn(name="sales_part_id", referencedColumnName="sale_part_comp_id")
 private SalesPartCompany salesPart;
 
 @Column(name="amount")
 private double amount;
 
 @Column(name="cos_amount")
 private double costOfSales;
 
 @ManyToOne
 @JoinColumn(name="comp_vat_code_id", referencedColumnName="vat_code_comp_id")
 private VatCodeCompany vatCodeComp;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private PartnerBase createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private PartnerBase changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int changes;
 
 @OneToMany(mappedBy = "salesInfo")
 private List<AuditSalesInfo> SalesInfoAuditRecs;
 
 public SalesInfo() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 
 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public boolean isInvoiceDoc() {
  return invoiceDoc;
 }

 public void setInvoiceDoc(boolean invoiceDoc) {
  this.invoiceDoc = invoiceDoc;
 }

 public int getFiscalYear() {
  return fiscalYear;
 }

 public void setFiscalYear(int fiscalYear) {
  this.fiscalYear = fiscalYear;
 }

 public int getFiscalPeriod() {
  return fiscalPeriod;
 }

 public void setFiscalPeriod(int fiscalPeriod) {
  this.fiscalPeriod = fiscalPeriod;
 }

 public Date getDocDate() {
  return docDate;
 }

 public void setDocDate(Date docDate) {
  this.docDate = docDate;
 }

 public Date getPostingDate() {
  return postingDate;
 }

 public void setPostingDate(Date postingDate) {
  this.postingDate = postingDate;
 }

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }

 public PartnerBase getPartner() {
  return partner;
 }

 public void setPartner(PartnerBase partner) {
  this.partner = partner;
 }

 public PartnerPerson getOrderedBy() {
  return orderedBy;
 }

 public void setOrderedBy(PartnerPerson orderedBy) {
  this.orderedBy = orderedBy;
 }

 public DocFi getFiDoc() {
  return fiDoc;
 }

 public void setFiDoc(DocFi fiDoc) {
  this.fiDoc = fiDoc;
 }

 public DocInvoiceAr getInvoice() {
  return invoice;
 }

 public void setInvoice(DocInvoiceAr invoice) {
  this.invoice = invoice;
 }

 public FiGlAccountComp getSalesGlAc() {
  return salesGlAc;
 }

 public void setSalesGlAc(FiGlAccountComp salesGlAc) {
  this.salesGlAc = salesGlAc;
 }

 public SalesCat getSalesCategory() {
  return salesCategory;
 }

 public void setSalesCategory(SalesCat salesCategory) {
  this.salesCategory = salesCategory;
 }

 public List<AuditSalesInfo> getSalesInfoAuditRecs() {
  return SalesInfoAuditRecs;
 }

 public void setSalesInfoAuditRecs(List<AuditSalesInfo> SalesInfoAuditRecs) {
  this.SalesInfoAuditRecs = SalesInfoAuditRecs;
 }

 
 public SalesPartCompany getSalesPart() {
  return salesPart;
 }

 public void setSalesPart(SalesPartCompany salesPart) {
  this.salesPart = salesPart;
 }
 
 public SalesPartCompany getSalesPartCompany() {
  return salesPart;
 }

 public void setSalesPartCompany(SalesPartCompany salesPart) {
  this.salesPart = salesPart;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public double getCostOfSales() {
  return costOfSales;
 }

 public void setCostOfSales(double costOfSales) {
  this.costOfSales = costOfSales;
 }

 public PartnerBase getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(PartnerBase createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public PartnerBase getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(PartnerBase changedBy) {
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

 public VatCodeCompany getVatCodeComp() {
  return vatCodeComp;
 }

 public void setVatCodeComp(VatCodeCompany vatCodeComp) {
  this.vatCodeComp = vatCodeComp;
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
  if (!(object instanceof SalesInfo)) {
   return false;
  }
  SalesInfo other = (SalesInfo) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.sales.SalesInfo[ id=" + id + " ]";
 }
 
}
