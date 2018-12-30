/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class ChequeTemplateRec {
 private Long id;
 private String reference;
 private String description;
 private List<PaymentTypeRec> paymentTypes;
 private String pdfFileExt;
 private String pdfFileName;
 private byte[] pdfData;
 private String originalFileName;
 private String originalFileExt;
 private byte[] originalData;
 private boolean printChqNumLine;
 
 private List<BankAccountCompanyRec> bankAcnts;
 
 UserRec createdBy;
 Date createdOn;
 UserRec changedBy;
 Date changedOn;

 public ChequeTemplateRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getOriginalFileName() {
  return originalFileName;
 }

 public void setOriginalFileName(String originalFileName) {
  this.originalFileName = originalFileName;
 }

 public String getPdfFileName() {
  return pdfFileName;
 }

 public void setPdfFileName(String pdfFileName) {
  this.pdfFileName = pdfFileName;
 }

 
 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isPrintChqNumLine() {
  return printChqNumLine;
 }

 public void setPrintChqNumLine(boolean printChqNumLine) {
  this.printChqNumLine = printChqNumLine;
 }

 public List<PaymentTypeRec> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 
 public String getPdfFileExt() {
  return pdfFileExt;
 }

 public void setPdfFileExt(String pdfFileExt) {
  this.pdfFileExt = pdfFileExt;
 }

 public byte[] getPdfData() {
  return pdfData;
 }

 public void setPdfData(byte[] pdfData) {
  this.pdfData = pdfData;
 }

 public String getOriginalFileExt() {
  return originalFileExt;
 }

 public void setOriginalFileExt(String originalFileExt) {
  this.originalFileExt = originalFileExt;
 }

 public byte[] getOriginalData() {
  return originalData;
 }

 public void setOriginalData(byte[] originalData) {
  this.originalData = originalData;
 }

 public List<BankAccountCompanyRec> getBankAcnts() {
  return bankAcnts;
 }

 public void setBankAcnts(List<BankAccountCompanyRec> bankAcnts) {
  this.bankAcnts = bankAcnts;
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
 
 
}
