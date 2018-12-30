/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.cm;

import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.busRec.doc.DocBaseRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class ContactRec implements Serializable {
 private Long id;
 private String summary;
 private String detail;
 private boolean actionContact;
 private boolean actionCompleted;
 private Date completedDate;
 private Date dueDate;
 private PartnerPersonRec respibilityOf;
 private ContactRoleRec role;
 private PartnerBaseRec contactFor;
 private ArAccountRec arAccount;
 private ApAccountRec apAccount;
 private DocBaseRec doc;
 private DocLineBaseRec docLine;
 private byte[]  attFile;
 private String attFileType;
 private String attFileName;
 
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;

 public ContactRec() {
 }

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

 public PartnerPersonRec getRespibilityOf() {
  return respibilityOf;
 }

 public void setRespibilityOf(PartnerPersonRec respibilityOf) {
  this.respibilityOf = respibilityOf;
 }

 public ContactRoleRec getRole() {
  return role;
 }

 public void setRole(ContactRoleRec role) {
  this.role = role;
 }

 public Date getCompletedDate() {
  return completedDate;
 }

 public void setCompletedDate(Date completedDate) {
  this.completedDate = completedDate;
 }

 public PartnerBaseRec getContactFor() {
  return contactFor;
 }

 public void setContactFor(PartnerBaseRec contactFor) {
  this.contactFor = contactFor;
 }

 public ArAccountRec getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccountRec arAccount) {
  this.arAccount = arAccount;
 }

 public ApAccountRec getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 public byte[] getAttFile() {
  return attFile;
 }

 public void setAttFile(byte[] attFile) {
  this.attFile = attFile;
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

 
 public DocBaseRec getDoc() {
  return doc;
 }

 public void setDoc(DocBaseRec doc) {
  this.doc = doc;
 }

 public DocLineBaseRec getDocLine() {
  return docLine;
 }

 public void setDocLine(DocLineBaseRec docLine) {
  this.docLine = docLine;
 }

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
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
