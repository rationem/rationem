/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

//import com.rationem.entity.audit.AuditBankAccount;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.rationem.entity.document.DocBankLineBacs;
//import com.rationem.entity.fi.arap.ArAccount;
//import com.rationem.entity.fi.arap.ArBank;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import java.util.Date;
import javax.persistence.Column;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("bank.BankAccount")
@Table(name="bnk02" )
@NamedQueries({
@NamedQuery(name = "allBankAccounts",
query ="select b from BankAccount b "),
@NamedQuery(name = "accountsByActNumStartingWith",
query ="select b from BankAccount b where b.accountNumber like :actNum "),
@NamedQuery(name = "accountByActNum",
query ="select b from BankAccount b where b.accountNumber = :actNum "),
@NamedQuery(name = "accountForBranch",
query ="select b from BankAccount b where b.accountNumber = :actNum "
        + "and b.accountForBranch.id = :brId "),
@NamedQuery(name = "accountBySortCdAcntNum",
 query ="select b from BankAccount b where b.accountNumber = :actNum  "
        + "and b.accountForBranch.sortCode = :sortCd "),
@NamedQuery(name = "accountsPartNumforBranch",
 query ="select b from BankAccount b where b.accountNumber like :actNum  "
        + "and b.accountForBranch.id = :brId ")
        
})
@SequenceGenerator(name = "bankAccount_s1", sequenceName = "bnk02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class BankAccount implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bankAccount_s1")
 @Column(name="bank_account_id")
 private Long id;
 @Column(name="account_ref", length=20)
 private String accountRef;
 @Column(name="account_number", length=10)
 private String accountNumber;
 @Column(name="account_name",length=50)
 private String accountName;
 @Column(name="dc_allowed")
 private boolean directCreditAllowed;
 @Column(name="dd_allowed")
 private boolean directDebitsAllowed;
 @Column(name="faster_payments")
 private boolean fasterPayments;
 @Column(name="iban")
 private String iban;
 @Column(name="swift")
 private String swift;
 @Column(name="validated")
 private boolean validated;

 @ManyToOne
 @JoinColumn(name="bank_branch_id",referencedColumnName="bank_branch_id")
 private BankBranch accountForBranch;
  
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User updatedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date updatedOn;
 @Version
 @Column(name="changes")
 private int revision;

 @OneToMany(mappedBy = "bankAccountPtnr")
 private List<DocBankLineBacs> bacsDocBankLines;
    
  
 
  public BankAccount() {
  }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getAccountNumber() {
  return accountNumber;
 }

 public void setAccountNumber(String accountNumber) {
  this.accountNumber = accountNumber;
 }

 public String getAccountName() {
  return accountName;
 }

 public void setAccountName(String accountName) {
  this.accountName = accountName;
 }

 public String getAccountRef() {
  return accountRef;
 }

 public void setAccountRef(String accountRef) {
  this.accountRef = accountRef;
 }

 
 public boolean isDirectCreditAllowed() {
  return directCreditAllowed;
 }

 public void setDirectCreditAllowed(boolean directCreditAllowed) {
  this.directCreditAllowed = directCreditAllowed;
 }

 public boolean isDirectDebitsAllowed() {
  return directDebitsAllowed;
 }

 public void setDirectDebitsAllowed(boolean directDebitsAllowed) {
  this.directDebitsAllowed = directDebitsAllowed;
 }

 public boolean isFasterPayments() {
  return fasterPayments;
 }

 public void setFasterPayments(boolean fasterPayments) {
  this.fasterPayments = fasterPayments;
 }

 public String getIban() {
  return iban;
 }

 public void setIban(String iban) {
  this.iban = iban;
 }

 public String getSwift() {
  return swift;
 }

 public void setSwift(String swift) {
  this.swift = swift;
 }

 public boolean isValidated() {
  return validated;
 }

 public void setValidated(boolean validated) {
  this.validated = validated;
 }

 public BankBranch getAccountForBranch() {
  return accountForBranch;
 }

 public void setAccountForBranch(BankBranch accountForBranch) {
  this.accountForBranch = accountForBranch;
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

 public User getUpdatedBy() {
  return updatedBy;
 }

 public void setUpdatedBy(User updatedBy) {
  this.updatedBy = updatedBy;
 }

 public Date getUpdatedOn() {
  return updatedOn;
 }

 public void setUpdatedOn(Date updatedOn) {
  this.updatedOn = updatedOn;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public List<DocBankLineBacs> getBacsDocBankLines() {
  return bacsDocBankLines;
 }

 public void setBacsDocBankLines(List<DocBankLineBacs> bacsDocBankLines) {
  this.bacsDocBankLines = bacsDocBankLines;
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
  if (!(object instanceof BankAccount)) {
   return false;
  }
  BankAccount other = (BankAccount) object;
  if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bank.BankAccount[ id=" + id + " ]";
 }
 
}
