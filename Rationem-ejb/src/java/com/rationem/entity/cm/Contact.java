/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.cm;

import com.rationem.entity.config.common.ContactRole;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Version;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author user
 */
@Entity
@Table(name="cm01")
@Multitenant(SINGLE_TABLE)

@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "contact_s1", sequenceName = "cm01_s1", allocationSize = 1,initialValue=1)
@NamedQueries({
 @NamedQuery(name="contactsAll", query="Select c from Contact c "),
 @NamedQuery(name="contByResp", query="Select c from Contact c where c.respibilityOf.id = :respId"),
 @NamedQuery(name="contFor", query="Select c from Contact c where c.contactFor = :forId"),
 @NamedQuery(name="contactsByArAcnt", query="Select c from Contact c where c.arAccount.id = :arAcntId "),
 @NamedQuery(name="contactsForDoc", query="Select c from Contact c where c.doc.id = :docId "),
 @NamedQuery(name="contactsForDocDate", query="Select c from Contact c where c.doc.id = :docId "
   + "and c.createdOn between :startDt and :endDt"),
 @NamedQuery(name="contactsForApAcnt", query="Select c from Contact c where c.apAccount.id = :acntId "),
 @NamedQuery(name="contactsForApAcntDate", query="Select c from Contact c where c.apAccount.id = :acntId "
   + "and c.createdOn between :startDt and :endDt"),
 @NamedQuery(name="contactsForPtnr", query="Select c from Contact c where c.contactFor.id = :ptnrId"),
 @NamedQuery(name="contactsForPtnrDate", query="Select c from Contact c where c.contactFor.id = :ptnrId "
   + "and c.createdOn between :startDt and :endDt")
})
public class Contact implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "contact_s1")
 @Column(name="contact_id")
 private Long id;
 @Column(name="contSumm")
 private String summary;
 @Column(name="details", length=4000)
 private String detail;
 @Column(name="action_needed")
 private boolean actionContact;
 @Column(name="action_completed")
 private boolean actionCompleted;
 @Column(name="completed_date")
 @Temporal(DATE)
 private Date completedDate;
 @ManyToOne
 @JoinColumn(name="resp_person_id", referencedColumnName="partner_id")
 private PartnerPerson respibilityOf;
 @ManyToOne
 @JoinColumn(name="role_id", referencedColumnName="contact_role_id")
 private ContactRole role;
 @ManyToOne
 @JoinColumn(name="ptnr_id", referencedColumnName="partner_id")
 private PartnerBase contactFor;
 @ManyToOne
 @JoinColumn(name="ar_account_id", referencedColumnName="ar_account_id")
 private ArAccount arAccount;
 @ManyToOne
 @JoinColumn(name="ap_account_id", referencedColumnName="ap_account_id")
 private ApAccount apAccount;
 @ManyToOne
 @JoinColumn(name="doc_id", referencedColumnName="doc_id")
 private DocBase doc;
 @ManyToOne
 @JoinColumn(name="doc_line_id", referencedColumnName="doc_line_id")
 private DocLineBase docLine;
 @Column(name="att_file_type", length=10)
 private String attFileType;
 @Column(name="att_file_name", length=50)
 private String attFileName;
 @Column(name="att_data")
 private byte[]  attFile;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int revision;
 

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getSummary() {
  return summary;
 }

 public void setSummary(String summary) {
  this.summary = summary;
 }

 public String getDetail() {
  return detail;
 }

 public void setDetail(String detail) {
  this.detail = detail;
 }

 public boolean isActionContact() {
  return actionContact;
 }

 public void setActionContact(boolean actionContact) {
  this.actionContact = actionContact;
 }

 public boolean isActionCompleted() {
  return actionCompleted;
 }

 public void setActionCompleted(boolean actionCompleted) {
  this.actionCompleted = actionCompleted;
 }

 public PartnerPerson getRespibilityOf() {
  return respibilityOf;
 }

 public void setRespibilityOf(PartnerPerson respibilityOf) {
  this.respibilityOf = respibilityOf;
 }

 public ContactRole getRole() {
  return role;
 }

 public void setRole(ContactRole role) {
  this.role = role;
 }

 public Date getCompletedDate() {
  return completedDate;
 }

 public void setCompletedDate(Date completedDate) {
  this.completedDate = completedDate;
 }

 public PartnerBase getContactFor() {
  return contactFor;
 }

 public void setContactFor(PartnerBase contactFor) {
  this.contactFor = contactFor;
 }

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 public ApAccount getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccount apAccount) {
  this.apAccount = apAccount;
 }

 public String getAttFileName() {
  return attFileName;
 }

 public void setAttFileName(String attFileName) {
  this.attFileName = attFileName;
 }

 public String getAttFileType() {
  return attFileType;
 }

 public void setAttFileType(String attFileType) {
  this.attFileType = attFileType;
 }

 public byte[] getAttFile() {
  return attFile;
 }

 public void setAttFile(byte[] attFile) {
  this.attFile = attFile;
 }

 
 public DocBase getDoc() {
  return doc;
 }

 public void setDoc(DocBase doc) {
  this.doc = doc;
 }

 public DocLineBase getDocLine() {
  return docLine;
 }

 public void setDocLine(DocLineBase docLine) {
  this.docLine = docLine;
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

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
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
  if (!(object instanceof Contact)) {
   return false;
  }
  Contact other = (Contact) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.cm.Contact[ id=" + id + " ]";
 }
 
}
