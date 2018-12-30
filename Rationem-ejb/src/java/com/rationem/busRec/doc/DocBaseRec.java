/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Base class for all documents
 * @author Chris
 */
public class DocBaseRec implements Serializable {

  private Long id;
  private long docNumber;
  private Date createOn;
  private TransactionTypeRec transactionType;
  private UserRec createdBy;
  private Date changedOn;
  private UserRec changedBy;
  private int revisionNumber;
  private String documentDateStr;
  private Date documentDate;
  private Date postingDate;
  private CompanyBasicRec company;
  private String docHdrText;
  private String notes;
  private DocBaseRec reversalDoc;
  private DocBaseRec reversalOfDoc;
  private List<DocBaseRec> linkedToDocs;
  private List<DocBaseRec> Docslinked;
  private List<DocLineBaseRec> docLines;

  public DocBaseRec() {
  }

  public DocBaseRec(Long id, long docNumber, Date createOn, UserRec createdBy, Date changedOn,
          int revisionNumber, Date documentDate, Date postingDate, String docHdrText,
          DocBaseRec reversalDoc, DocBaseRec reversalOfDoc, ArrayList<DocBaseRec> linkedToDocs,
          ArrayList<DocBaseRec> Docslinked, ArrayList<DocLineBaseRec> docLines) {
    this.id = id;
    this.docNumber = docNumber;
    this.createOn = createOn;
    this.createdBy = createdBy;
    this.changedOn = changedOn;
    this.revisionNumber = revisionNumber;
    this.documentDate = documentDate;
    this.postingDate = postingDate;
    this.docHdrText = docHdrText;
    this.reversalDoc = reversalDoc;
    this.reversalOfDoc = reversalOfDoc;
    this.linkedToDocs = linkedToDocs;
    this.Docslinked = Docslinked;
    this.docLines = docLines;
  }

  public Collection<DocBaseRec> getDocslinked() {
    return Docslinked;
  }

  public void setDocslinked(ArrayList<DocBaseRec> Docslinked) {
    this.Docslinked = Docslinked;
  }

  public Date getChangedOn() {
    return changedOn;
  }

  public void setChangedOn(Date changedOn) {
    this.changedOn = changedOn;
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

  public UserRec getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(UserRec changedBy) {
    this.changedBy = changedBy;
  }

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 public List<DocBaseRec> getLinkedToDocs() {
  return linkedToDocs;
 }

 public void setLinkedToDocs(List<DocBaseRec> linkedToDocs) {
  this.linkedToDocs = linkedToDocs;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 

 public void setDocslinked(List<DocBaseRec> Docslinked) {
  this.Docslinked = Docslinked;
 }

 public TransactionTypeRec getTransactionType() {
  return transactionType;
 }

 public void setTransactionType(TransactionTypeRec transactionType) {
  this.transactionType = transactionType;
 }

  public String getDocHdrText() {
    return docHdrText;
  }

  public void setDocHdrText(String docHdrText) {
    this.docHdrText = docHdrText;
  }

  public List<DocLineBaseRec> getDocLines() {
    return docLines;
  }

  public void setDocLines(List<DocLineBaseRec> docLines) {
    this.docLines = docLines;
  }

  public long getDocNumber() {
    return docNumber;
  }

  public void setDocNumber(long docNumber) {
    this.docNumber = docNumber;
  }

 public String getDocumentDateStr() {
  String ret = new String();
  if(documentDateStr == null){
   if(documentDate != null){
    SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MMM/yyyy");
     documentDateStr =  dateFmt.format(documentDate.getTime());
   }
  }
  return documentDateStr;
 }

 public void setDocumentDateStr(String documentDateStr) {
  this.documentDateStr = documentDateStr;
 }

  
  public Date getDocumentDate() {
    return documentDate;
  }

  public void setDocumentDate(Date documentDate) {
    this.documentDate = documentDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  

  public Date getPostingDate() {
    return postingDate;
  }

  public void setPostingDate(Date postingDate) {
    this.postingDate = postingDate;
  }

  public DocBaseRec getReversalDoc() {
    return reversalDoc;
  }

  public void setReversalDoc(DocBaseRec reversalDoc) {
    this.reversalDoc = reversalDoc;
  }

  public DocBaseRec getReversalOfDoc() {
    return reversalOfDoc;
  }

  public void setReversalOfDoc(DocBaseRec reversalOfDoc) {
    this.reversalOfDoc = reversalOfDoc;
  }

  public int getRevisionNumber() {
    return revisionNumber;
  }

  public void setRevisionNumber(int revisionNumber) {
    this.revisionNumber = revisionNumber;
  }
  



}
