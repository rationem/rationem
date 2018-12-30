/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.cm.Contact;
import com.rationem.entity.config.common.DocReversalReason;
import com.rationem.entity.config.common.TransactionType;
import com.rationem.entity.fi.company.CompanyBasic;
import java.util.List;
import com.rationem.entity.user.User;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Collection;
import java.util.Date;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.CascadeType.ALL;


/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("document.DocBase")
@Table(name="doc01")
@NamedQueries({
@NamedQuery(name="docCompAll", query="select d from DocBase d where d.company.id = :compId "),
@NamedQuery(name="docCompDocNumPartAll", 
        query="select d from DocBase d where d.company.id = :compId and d.docNumber >= :docNum order by d.docNumber")
})
@SequenceGenerator(name = "doc_s1", sequenceName = "doc01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class DocBase implements Serializable {

 

 
 
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
 @OneToMany(mappedBy = "doc")
 private List<Contact> contacts;
 
 @ManyToOne
 @JoinColumn(name="doc_type_id", referencedColumnName="doc_type_id")
 private DocType docType;
 
 @Column(name="doc_text")
 private String docHdrText;
    
 @Column(name="doc_notes")
 private String notes;

 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id" )
 private CompanyBasic company;
    
 @ManyToOne
 @JoinColumn(name="doc_rev_id", referencedColumnName="doc_rev_id")
 private DocReversalReason docReversalReason;
 
 @JoinColumn(name="reversed_by_doc",  referencedColumnName="doc_id")
 @OneToOne(mappedBy = "reversalOfDoc")
 private DocBase reversalDoc;
    
 @JoinColumn(name = "reversal_of_doc", referencedColumnName = "doc_id")
 @OneToOne
 private DocBase reversalOfDoc;
 
 
    

 @ManyToMany
 @JoinTable(name="doc_map01", joinColumns =
  @JoinColumn(name="original_doc_id", referencedColumnName="doc_id"),
       inverseJoinColumns=
        @JoinColumn(name="linked_doc_id", referencedColumnName="doc_id")
  )
 private Collection<DocBase> linkedToDocs;

 @ManyToMany(mappedBy = "linkedToDocs")
 private Collection<DocBase> Docslinked;


 @OneToMany(cascade=ALL,mappedBy = "docHeaderBase", orphanRemoval=true )
    private List<DocLineBase> docLines;
  
    
 

 @ManyToOne
 @JoinColumn(name="recur_doc_id", referencedColumnName="doc_tmpl_id")
 private DocFiTemplateBase templateRecurDoc;
 @OneToOne(mappedBy = "revJnl")
 private DocFiTemplAccrPrePay templateRev;
 @OneToOne(mappedBy = "originalJnl")
 private DocFiTemplAccrPrePay templateDoc;
 

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

 public DocFiTemplateBase getTemplateRecurDoc() {
  return templateRecurDoc;
 }

 public void setTemplateRecurDoc(DocFiTemplateBase templateRecurDoc) {
  this.templateRecurDoc = templateRecurDoc;
 }
 
 public void setTemplateRecurDoc(DocFiTemplAccrPrePay templateRecurDoc) {
  this.templateRecurDoc = templateRecurDoc;
 }

 public DocFiTemplAccrPrePay getTemplateRev() {
  return templateRev;
 }

 public void setTemplateRev(DocFiTemplAccrPrePay templateRev) {
  this.templateRev = templateRev;
 }

 public DocFiTemplAccrPrePay getTemplateDoc() {
  return templateDoc;
 }

 public void setTemplateDoc(DocFiTemplAccrPrePay templateDoc) {
  this.templateDoc = templateDoc;
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

 public List<Contact> getContacts() {
  return contacts;
 }

 public void setContacts(List<Contact> contacts) {
  this.contacts = contacts;
 }

 public DocReversalReason getDocReversalReason() {
  return docReversalReason;
 }

 public void setDocReversalReason(DocReversalReason docReversalReason) {
  this.docReversalReason = docReversalReason;
 }

 public DocType getDocType() {
  return docType;
 }

 public void setDocType(DocType docType) {
  this.docType = docType;
 }

 public DocBase getReversalDoc() {
  return reversalDoc;
 }

 public void setReversalDoc(DocBase reversalDoc) {
  this.reversalDoc = reversalDoc;
 }

 public DocBase getReversalOfDoc() {
  return reversalOfDoc;
 }

 public void setReversalOfDoc(DocBase reversalOfDoc) {
  this.reversalOfDoc = reversalOfDoc;
 }

 public Collection<DocBase> getLinkedToDocs() {
  return linkedToDocs;
 }

 public void setLinkedToDocs(Collection<DocBase> linkedToDocs) {
  this.linkedToDocs = linkedToDocs;
 }

 public Collection<DocBase> getDocslinked() {
  return Docslinked;
 }

 public List<DocLineBase> getDocLines() {
  return docLines;
 }

 public void setDocLines(List<DocLineBase> docLines) {
  this.docLines = docLines;
 }

 
 public void setDocslinked(Collection<DocBase> Docslinked) {
  this.Docslinked = Docslinked;
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
  if (!(object instanceof DocBase)) {
   return false;
  }
  DocBase other = (DocBase) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocBase[ id=" + id + " ]";
 }
 
}
