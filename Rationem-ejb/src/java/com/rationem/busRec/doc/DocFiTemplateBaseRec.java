/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class DocFiTemplateBaseRec implements Serializable {
 private Long id;
 
 private long docNumber;
 private TransactionTypeRec transactionType;
 private Date createOn;
 private UserRec createdBy;
 private Date changedOn;
 private UserRec changedBy;
 private int revisionNumber;

 private Date documentDate;

 private Date postingDate;

 private String docHdrText;
    
 private String notes;

 private CompanyBasicRec company;
 
   
    
 private DocTypeRec docType;
 
 private List<DocLineFiTemplateRec> docLines;
 
 // FI settings
 private Date taxDate;
 private String partnerRef;
 private int fisPeriod;
 private int fisYear;
 private int tmplType;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public long getDocNumber() {
  return docNumber;
 }

 public void setDocNumber(long docNumber) {
  this.docNumber = docNumber;
 }

 public TransactionTypeRec getTransactionType() {
  return transactionType;
 }

 public void setTransactionType(TransactionTypeRec transactionType) {
  this.transactionType = transactionType;
 }

 public Date getCreateOn() {
  return createOn;
 }

 public void setCreateOn(Date createOn) {
  this.createOn = createOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public int getRevisionNumber() {
  return revisionNumber;
 }

 public void setRevisionNumber(int revisionNumber) {
  this.revisionNumber = revisionNumber;
 }

 public Date getDocumentDate() {
  return documentDate;
 }

 public void setDocumentDate(Date documentDate) {
  this.documentDate = documentDate;
 }

 public Date getPostingDate() {
  return postingDate;
 }

 public void setPostingDate(Date postingDate) {
  this.postingDate = postingDate;
 }

 public String getDocHdrText() {
  return docHdrText;
 }

 public void setDocHdrText(String docHdrText) {
  this.docHdrText = docHdrText;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 public DocTypeRec getDocType() {
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
 }

 public List<DocLineFiTemplateRec> getDocLines() {
  return docLines;
 }

 public void setDocLines(List<DocLineFiTemplateRec> docLines) {
  this.docLines = docLines;
 }

 public Date getTaxDate() {
  return taxDate;
 }

 public void setTaxDate(Date taxDate) {
  this.taxDate = taxDate;
 }

 public String getPartnerRef() {
  return partnerRef;
 }

 public void setPartnerRef(String partnerRef) {
  this.partnerRef = partnerRef;
 }

 public int getFisPeriod() {
  return fisPeriod;
 }

 public void setFisPeriod(int fisPeriod) {
  this.fisPeriod = fisPeriod;
 }

 public int getFisYear() {
  return fisYear;
 }

 public void setFisYear(int fisYear) {
  this.fisYear = fisYear;
 }

 public int getTmplType() {
  return tmplType;
 }

 public void setTmplType(int tmplType) {
  this.tmplType = tmplType;
 }
 
 
    
}
