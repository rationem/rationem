/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

import com.rationem.entity.audit.AuditChequeTemplate;
import com.rationem.entity.config.arap.PaymentType;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import java.util.Date;
import javax.persistence.Column;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 * Cheque Template 
 * @author user
 */
@Entity
@NamedQueries({
 @NamedQuery(name="chqTemplAll", query="Select t from ChequeTemplate t"),
 @NamedQuery(name="chqTemplByRef", query="Select t from ChequeTemplate t where t.reference like :ref")
})
@Table(name="bnk08" )
@SequenceGenerator(name = "chqTemplate_s1", sequenceName = "bnk08_s1", allocationSize = 1,initialValue=0)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class ChequeTemplate implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "chqTemplate_s1")
 @Column(name="cheque_template_id")
 private Long id;
 @Column(name="code", length=20)
 private String reference;
 @Column(name="descr", length=50)
 private String description;
 @Column(name="output_bank_line")
 private boolean printChqNumLine;
 
 @Column(name="pdf_file_ext", length=50)
 private String pdfFileExt;
 @Column(name="pdf_file_name", length=50)
 private String pdfFileName;
 @Column(name="pdf_file_data")
 private byte[] pdfData;
 @Column(name="orig_file_ext", length=50)
 private String originalFileExt;
 @Column(name="orig_file_name", length=50)
 private String originalFileName;
 @Column(name="orig_file_data")
 private byte[] originalData;
 
 
 @OneToMany(mappedBy = "chequeTemplate")
 private List<BankAccountCompany> bankAcnt;
 
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
 @Column(name="revision")
 long changes;
 @OneToMany(mappedBy = "chequeTemplate")
 private List<AuditChequeTemplate> auditRecords;
 @OneToMany(mappedBy = "chqTemplate")
 private List<PaymentType> paymentTypes;

 public ChequeTemplate() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditChequeTemplate> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditChequeTemplate> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public List<BankAccountCompany> getBankAcnt() {
  return bankAcnt;
 }

 public void setBankAcnt(List<BankAccountCompany> bankAcnt) {
  this.bankAcnt = bankAcnt;
 }

 
 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 
 public String getPdfFileExt() {
  return pdfFileExt;
 }

 public void setPdfFileExt(String pdfFileExt) {
  this.pdfFileExt = pdfFileExt;
 }

 public String getPdfFileName() {
  return pdfFileName;
 }

 public void setPdfFileName(String pdfFileName) {
  this.pdfFileName = pdfFileName;
 }

 
 public byte[] getPdfData() {
  return pdfData;
 }

 public void setPdfData(byte[] pdfData) {
  this.pdfData = pdfData;
 }

 public boolean isPrintChqNumLine() {
  return printChqNumLine;
 }

 public void setPrintChqNumLine(boolean printChqNumLine) {
  this.printChqNumLine = printChqNumLine;
 }

 
 public String getOriginalFileExt() {
  return originalFileExt;
 }

 public void setOriginalFileExt(String originalFileExt) {
  this.originalFileExt = originalFileExt;
 }

 public String getOriginalFileName() {
  return originalFileName;
 }

 public void setOriginalFileName(String originalFileName) {
  this.originalFileName = originalFileName;
 }

 
 public byte[] getOriginalData() {
  return originalData;
 }

 public void setOriginalData(byte[] originalData) {
  this.originalData = originalData;
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
 
  
 
}
