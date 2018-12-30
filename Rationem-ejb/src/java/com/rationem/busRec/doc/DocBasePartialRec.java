
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class DocBasePartialRec implements Serializable {
 private Long id;
 private long docNumber;
 private Date createDate;
 private UserRec createBy;
 private Date changeDate;
 private UserRec changeBy;
 private int revision;
 private Date documentDate;
 private Date postingDate;
 private String docHdrText;
 private String notes;
 private CompanyBasicRec company;
 private List<DocLineBasePartialRec> lines;
 private DocTypeRec docType;
 

 public DocBasePartialRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 

 public List<DocLineBasePartialRec> getLines() {
  return lines;
 }

 public void setLines(List<DocLineBasePartialRec> lines) {
  this.lines = lines;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public UserRec getCreateBy() {
  return createBy;
 }

 public void setCreateBy(UserRec createBy) {
  this.createBy = createBy;
 }

 public Date getChangeDate() {
  return changeDate;
 }

 public void setChangeDate(Date changeDate) {
  this.changeDate = changeDate;
 }

 public UserRec getChangeBy() {
  return changeBy;
 }

 public void setChangeBy(UserRec changeBy) {
  this.changeBy = changeBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
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

 public long getDocNumber() {
  return docNumber;
 }

 public void setDocNumber(long docNumber) {
  this.docNumber = docNumber;
 }

 public DocTypeRec getDocType() {
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
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

 
 
}
