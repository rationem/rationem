/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.config.common.ChequeVoidReason;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Temporal;


import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import static javax.persistence.TemporalType.DATE;


/**
 *
 * @author Chris
 */
@Entity
@NamedQueries({
 @NamedQuery(name="chqListBnkAll",query="Select c from DocBankLineChq c "
   + "where c.comp.id = :compId and ( c.unClearedBankAc.id = :unclBnkAcntId or c.clearedBankAc.id = :clearedBnkAcntId ) "),
 @NamedQuery(name="chqListBnkClrStat",query="Select c from DocBankLineChq c  "
   + "where c.comp.id = :compId and c.arBank.id = :bnkAcntId and c.cleared = :clrStat")
})
@Table(name="doc_line05" )
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocBankLineChq")
@PrimaryKeyJoinColumn(name="bank_trans_id",referencedColumnName = "bank_trans_id")

public class DocBankLineChq extends DocBankLineBase implements Serializable {
 @ManyToOne
 @JoinColumn(name="payee_id",  referencedColumnName="partner_id")
 private PartnerBase payee;
 @Column(name="printed")
 private boolean printed;
 @Column(name="file_type", length=10)
 private String fileType;
 @Column(name="file_content")
 private byte[] fileContent;
 @Column(name="issue_date")
 @Temporal(DATE)
 private Date issueDate;
 
 @ManyToOne
 @JoinColumn(name="void_reason_id",  referencedColumnName="void_reason_id")
 private ChequeVoidReason voidReason;
 @Temporal(TIMESTAMP)
 @Column(name="void_on")
 private Date voidDate;
 @ManyToOne
 @JoinColumn(name="void_by_id",  referencedColumnName="partner_id")
 private User voidBy;

 public PartnerBase getPayee() {
  return payee;
 }

 public void setPayee(PartnerBase payee) {
  this.payee = payee;
 }

 public String getFileType() {
  return fileType;
 }

 public void setFileType(String fileType) {
  this.fileType = fileType;
 }

 public byte[] getFileContent() {
  return fileContent;
 }

 public void setFileContent(byte[] fileContent) {
  this.fileContent = fileContent;
 }

 public boolean isPrinted() {
  return printed;
 }

 public void setPrinted(boolean printed) {
  this.printed = printed;
 }

 public Date getIssueDate() {
  return issueDate;
 }

 public void setIssueDate(Date issueDate) {
  this.issueDate = issueDate;
 }

 
 public ChequeVoidReason getVoidReason() {
  return voidReason;
 }

 public void setVoidReason(ChequeVoidReason voidReason) {
  this.voidReason = voidReason;
 }

 public Date getVoidDate() {
  return voidDate;
 }

 public void setVoidDate(Date voidDate) {
  this.voidDate = voidDate;
 }

 public User getVoidBy() {
  return voidBy;
 }

 public void setVoidBy(User voidBy) {
  this.voidBy = voidBy;
 }

 
 
}
