/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Chris
 */
public class DocInvoiceArRec implements Serializable {
 
 private Long id;
 private Date invoiceDate;
 private String purchaseOrderNumber;
 private Date purchaseOrderDate;
 private String invoiceNumber;
 private Double goodsAmount;
 private Double vatAmount;
 private Double totalAmount;
 private PartnerPersonRec orderedBy;
 private boolean printed;
 private boolean debit;
 private DocFiRec fiDocument;
 private ArAccountRec account;
 private DocLineArRec arDocLine;
 private String uploadOrderFileName;
 private String uploadOrderContentType;
 private byte[] uploadOrderFileData;
 private byte[] invoicePdf;
 private String notes;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int revision;
 
 public DocInvoiceArRec() {
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public DocFiRec getFiDocument() {
  return fiDocument;
 }

 public void setFiDocument(DocFiRec fiDocument) {
  System.out.println("Called setFiDocument ");
  this.fiDocument = fiDocument;
 }

 public ArAccountRec getAccount() {
  return account;
 }

 public void setAccount(ArAccountRec account) {
  this.account = account;
 }

 
 public DocLineArRec getArDocLine() {
  return arDocLine;
 }

 public void setArDocLine(DocLineArRec arDocLine) {
  this.arDocLine = arDocLine;
 }

 public Double getGoodsAmount() {
  return goodsAmount;
 }

 public void setGoodsAmount(Double goodsAmount) {
  this.goodsAmount = goodsAmount;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debitdebit) {
  this.debit = debitdebit;
 }

 
 public Date getInvoiceDate() {
  return invoiceDate;
 }

 public void setInvoiceDate(Date invoiceDate) {
  this.invoiceDate = invoiceDate;
 }

 public String getInvoiceNumber() {
  return invoiceNumber;
 }

 public void setInvoiceNumber(String invoiceNumber) {
  this.invoiceNumber = invoiceNumber;
 }

 public byte[] getInvoicePdf() {
  return invoicePdf;
 }

 
 public void setInvoicePdf(byte[] invoicePdf) {
  this.invoicePdf = invoicePdf;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 public PartnerPersonRec getOrderedBy() {
  if(orderedBy == null){
   orderedBy = new PartnerPersonRec();
  }
  return orderedBy;
 }

 public void setOrderedBy(PartnerPersonRec orderedBy) {
  this.orderedBy = orderedBy;
 }

 public boolean isPrinted() {
  return printed;
 }

 public void setPrinted(boolean printed) {
  this.printed = printed;
 }

 public String getPurchaseOrderNumber() {
  return purchaseOrderNumber;
 }

 public void setPurchaseOrderNumber(String purchaseOrderNumber) {
  this.purchaseOrderNumber = purchaseOrderNumber;
 }

 public String getPurchaseOrderDateStr(){
  String ret = new String();
  if(purchaseOrderDate != null){
   SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MMM/yyyy");
    ret =  dateFmt.format(purchaseOrderDate.getTime());
  }
  return ret;
 }
 public Date getPurchaseOrderDate() {
  return purchaseOrderDate;
 }

 public void setPurchaseOrderDate(Date purchaseOrderDate) {
  this.purchaseOrderDate = purchaseOrderDate;
 }

 

 
 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public Double getTotalAmount() {
  return totalAmount;
 }

 public void setTotalAmount(Double totalAmount) {
  this.totalAmount = totalAmount;
 }

 public String getUploadOrderContentType() {
  return uploadOrderContentType;
 }

 public void setUploadOrderContentType(String uploadOrderContentType) {
  this.uploadOrderContentType = uploadOrderContentType;
 }

 public byte[] getUploadOrderFileData() {
  return uploadOrderFileData;
 }

 public void setUploadOrderFileData(byte[] uploadOrderFileData) {
  this.uploadOrderFileData = uploadOrderFileData;
 }

 public String getUploadOrderFileName() {
  return uploadOrderFileName;
 }

 public void setUploadOrderFileName(String uploadOrderFileName) {
  this.uploadOrderFileName = uploadOrderFileName;
 }

 public Double getVatAmount() {
  return vatAmount;
 }

 public void setVatAmount(Double vatAmount) {
  this.vatAmount = vatAmount;
 }
 
 
}
