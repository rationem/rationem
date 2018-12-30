/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocInvoiceAr;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.mdm.PartnerPerson;
//import com.rationem.entity.salesTax.vat.VatReturnLine;
import com.rationem.entity.user.User;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.util.Date;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;




/**
 *
 * @author Chris
 */
@Entity
@Table(name="doc04")
@NamedQuery(name="salesInvsComp", query="Select i from DocInvoiceAr i where i.fiDocument.company.id = :compId ")
@SequenceGenerator(name = "docInvoiceAr_s1", sequenceName = "doc04_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class DocInvoiceAr implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "docInvoiceAr_s1")
 @Column(name="ar_inv_id")
 private Long id;
 @Column(name=("po_date"))
 @Temporal(DATE)
 private Date purchaseOrderDate;   
 @Column(name="po_num")
 private String purchaseOrderNumber;
 @Column(name="invoice_num")
 private String invoiceNumber;
 @Column(name="net_amount")
 private Double goodsAmount;
 @Column(name="vat_amount")
 private Double vatAmount;
 @Column(name="total_amount")
 private Double totalAmount;
 @ManyToOne
 @JoinColumn(name="ordered_by_id", referencedColumnName="partner_id")
 private PartnerPerson orderedBy;
 @Column(name="printed")
 private boolean printed;
 @Column(name="debit")
 private boolean debit;
 @OneToOne
 @JoinColumn(name="fi_doc_id", referencedColumnName="doc_id")
 private DocFi fiDocument;
 @ManyToOne
 @JoinColumn(name="account_id", referencedColumnName="ar_account_id")
 private ArAccount account;
 @Column(name="order_cont_type")
 private String uploadOrderContentType;
 @Column(name="order_file_name")
 private String uploadOrderFileName;
 @Column(name="order_data")
 private byte[] uploadOrderFileData;
 @Column(name="inv_crn_cont_data")
 private byte[] invCrnPdf;
 @Column(name="invoice_note")
 private String note;
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id" )
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id" )
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on_ON")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int changes;
 @OneToMany(mappedBy = "inv")
 private List<AuditDocInvoiceAr> auditRecords;
 /*
   @OneToMany(mappedBy = "arInvoice")
 private List<VatReturnLine> vatReturnLines;
*/
 public DocInvoiceAr() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public ArAccount getAccount() {
  return account;
 }

 public void setAccount(ArAccount account) {
  this.account = account;
 }

 public List<AuditDocInvoiceAr> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditDocInvoiceAr> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public Date getPurchaseOrderDate() {
  return purchaseOrderDate;
 }

 public void setPurchaseOrderDate(Date purchaseOrderDate) {
  this.purchaseOrderDate = purchaseOrderDate;
 }

 
 
 public String getPurchaseOrderNumber() {
  return purchaseOrderNumber;
 }

 public void setPurchaseOrderNumber(String purchaseOrderNumber) {
  this.purchaseOrderNumber = purchaseOrderNumber;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

 
 public String getInvoiceNumber() {
  return invoiceNumber;
 }

 public void setInvoiceNumber(String invoiceNumber) {
  this.invoiceNumber = invoiceNumber;
 }

 public Double getGoodsAmount() {
  return goodsAmount;
 }

 public void setGoodsAmount(Double goodsAmount) {
  this.goodsAmount = goodsAmount;
 }

 public Double getVatAmount() {
  return vatAmount;
 }

 public void setVatAmount(Double vatAmount) {
  this.vatAmount = vatAmount;
 }

 public Double getTotalAmount() {
  return totalAmount;
 }

 public void setTotalAmount(Double totalAmount) {
  this.totalAmount = totalAmount;
 }

 public PartnerPerson getOrderedBy() {
  return orderedBy;
 }

 public void setOrderedBy(PartnerPerson orderedBy) {
  this.orderedBy = orderedBy;
 }

 public boolean isPrinted() {
  return printed;
 }

 public void setPrinted(boolean printed) {
  this.printed = printed;
 }

 public DocFi getFiDocument() {
  return fiDocument;
 }

 public void setFiDocument(DocFi fiDocument) {
  this.fiDocument = fiDocument;
 }

 public String getUploadOrderContentType() {
  return uploadOrderContentType;
 }

 public void setUploadOrderContentType(String uploadOrderContentType) {
  this.uploadOrderContentType = uploadOrderContentType;
 }

 public String getUploadOrderFileName() {
  return uploadOrderFileName;
 }

 public void setUploadOrderFileName(String uploadOrderFileName) {
  this.uploadOrderFileName = uploadOrderFileName;
 }

 public byte[] getUploadOrderFileData() {
  return uploadOrderFileData;
 }

 public void setUploadOrderFileData(byte[] uploadOrderFileData) {
  this.uploadOrderFileData = uploadOrderFileData;
 }

 public byte[] getInvCrnPdf() {
  return invCrnPdf;
 }

 public void setInvCrnPdf(byte[] invCrnPdf) {
  this.invCrnPdf = invCrnPdf;
 }

 public String getNote() {
  return note;
 }

 public void setNote(String note) {
  this.note = note;
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
  if (!(object instanceof DocInvoiceAr)) {
   return false;
  }
  DocInvoiceAr other = (DocInvoiceAr) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.DocInvoiceAr[ id=" + id + " ]";
 }
 
}
