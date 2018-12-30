/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import com.rationem.entity.user.User;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

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
@Table(name="doc03" )
@NamedQueries({
@NamedQuery(name = "allDocTypes",query = "SELECT docTy FROM DocType docTy"),
@NamedQuery(name = "allDocTyByCode",
query = "SELECT docTy FROM DocType docTy where docTy.code = :docCode ")
})
@SequenceGenerator(name = "docType_s1", sequenceName = "doc03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class DocType implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "docType_s1")
 @Column(name="doc_type_id")
 private Long id;
 @Column(name="codd")
 private String code;
 @Column(name="name")
 private String name;
 @Column(name="gl_lines_allowed")
 private boolean glAllowed;
 @Column(name="ar_lines_allowed")
 private boolean arAllowed;
 @Column(name="ap_lines_allowed")
 private boolean apAllowed;
 @Column(name="tr_lines_allowed")
 private boolean trAllowed;
 @OneToMany(mappedBy = "docType")
 private List<DocFi> documents;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date created;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int revision;
 @OneToMany(mappedBy = "docType")
 private List<DocFi> fiDocs;
 @OneToMany(mappedBy = "docType")
 private List<AuditDocType> auditRecs;

  public DocType() {
  }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditDocType> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditDocType> auditRecs) {
  this.auditRecs = auditRecs;
 }

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public boolean isGlAllowed() {
  return glAllowed;
 }

 public void setGlAllowed(boolean glAllowed) {
  this.glAllowed = glAllowed;
 }

 public boolean isArAllowed() {
  return arAllowed;
 }

 public void setArAllowed(boolean arAllowed) {
  this.arAllowed = arAllowed;
 }

 public boolean isApAllowed() {
  return apAllowed;
 }

 public void setApAllowed(boolean apAllowed) {
  this.apAllowed = apAllowed;
 }

 public boolean isTrAllowed() {
  return trAllowed;
 }

 public void setTrAllowed(boolean trAllowed) {
  this.trAllowed = trAllowed;
 }

 public List<DocFi> getDocuments() {
  return documents;
 }

 public void setDocuments(List<DocFi> documents) {
  this.documents = documents;
 }

 public Date getCreated() {
  return created;
 }

 public void setCreated(Date created) {
  this.created = created;
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

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public List<DocFi> getFiDocs() {
  return fiDocs;
 }

 public void setFiDocs(List<DocFi> fiDocs) {
  this.fiDocs = fiDocs;
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
  if (!(object instanceof DocType)) {
   return false;
  }
  DocType other = (DocType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocType[ id=" + id + " ]";
 }
 
}
