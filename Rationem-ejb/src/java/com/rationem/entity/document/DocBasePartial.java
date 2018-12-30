/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.config.common.TransactionType;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;



import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.CascadeType.ALL;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("document.DocBasePartial")
@Table(name="doc05")
@SequenceGenerator(name = "doc_partial_s1", sequenceName = "doc05_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class DocBasePartial implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "doc_s1")
 @Column(name="doc_id")
 private Long id;
 @Column(name="doc_num")
 private long docNumber;
 @ManyToOne
 @JoinColumn(name="tran_type_id", referencedColumnName="tran_type_id")
 private TransactionType transactionType;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createOn;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int revisionNumber;

 @Column(name="doc_date")
 @Temporal(DATE)
 private Date documentDate;

 @Column(name="post_date")
 @Temporal(DATE)
 private Date postingDate;

 @Column(name="doc_text")
 private String docHdrText;
    
 @Column(name="doc_notes")
 private String notes;

 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id" )
 private CompanyBasic company;
 
 @ManyToOne
 @JoinColumn(name="doc_type_id", referencedColumnName="doc_type_id")
 private DocType docType;
 @OneToMany(cascade=ALL,mappedBy = "docHeaderBase", orphanRemoval=true )
 private List<DocLineBasePartial> docLines;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<DocLineBasePartial> getDocLines() {
  return docLines;
 }

 public void setDocLines(List<DocLineBasePartial> docLines) {
  this.docLines = docLines;
 }

 public long getDocNumber() {
  return docNumber;
 }

 public void setDocNumber(long docNumber) {
  this.docNumber = docNumber;
 }

 public TransactionType getTransactionType() {
  return transactionType;
 }

 public void setTransactionType(TransactionType transactionType) {
  this.transactionType = transactionType;
 }

 public Date getCreateOn() {
  return createOn;
 }

 public void setCreateOn(Date createOn) {
  this.createOn = createOn;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
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

 public DocType getDocType() {
  return docType;
 }

 public void setDocType(DocType docType) {
  this.docType = docType;
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

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
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
  if (!(object instanceof DocBasePartial)) {
   return false;
  }
  DocBasePartial other = (DocBasePartial) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocBasePartial[ id=" + id + " ]";
 }
 
}
