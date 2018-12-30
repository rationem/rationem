/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocBankLine;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.tr.bank.BankAccountCompany;
import com.rationem.entity.tr.bank.BankStatement;
import com.rationem.entity.tr.bank.BnkPaymentRun;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToOne;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="doc_line04")
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("document.DocBankLineBase")
@SequenceGenerator(name = "docBankLine_s1", sequenceName = "doc_line04_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class DocBankLineBase implements Serializable {

 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "docBankLine_s1")
 @Column(name="bank_trans_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="comp_id",  referencedColumnName="home_comp_id")
 private CompanyBasic comp;
 
 
 @ManyToOne
 @JoinColumn(name="ap_account_id",  referencedColumnName="ap_account_id")
 private ApAccount apAccount;
 
 
 @ManyToOne
 @JoinColumn(name="ar_account_id",  referencedColumnName="ar_account_id")
 private ArAccount arAccount;
 
 @ManyToOne
 @JoinColumn(name="uncleared_bnk_acnt_id",  referencedColumnName="bank_account_id")
 private BankAccountCompany unClearedBankAc;
 
 
 @ManyToOne
 @JoinColumn(name="cleared_bnk_acnt_id",  referencedColumnName="bank_account_id")
 private BankAccountCompany clearedBankAc;
 
 
 @ManyToOne
 @JoinColumn(name="bnk_statement",  referencedColumnName="bank_statement_id")
 private BankStatement bnkStament;
 
 @OneToMany(mappedBy = "bankLine")
 private List<AuditDocBankLine> auditRecs;
 @Temporal(DATE)
 @Column(name="doc_date")
 private Date docDate;
 @Temporal(DATE)
 @Column(name="post_date")
 private Date postDate;
 
 @Column(name="cleared")
 private boolean cleared;
 @Temporal(DATE)
 @Column(name="cleared_date")
 private Date clearedDate;
 @Temporal(DATE)
 @Column(name="value_date")
 private Date valueDate;
 @Column(name="bank_reference")
 private String bnkRef;
 @Column(name="bnk_trans_code")
 private String bankTransCode;
 /*
 @Column(name="bacs_trans_code")
 private String bacsTransCode;
 * */
 @OneToOne(mappedBy = "bankLine")
 private DocLineBase docFiLine;
 
 @Column(name="doc_amount")
 private double amount;
 @Column(name="receipt")
 private boolean receipt;
 
 @ManyToOne
 @JoinColumn(name="AR_BANK_ID",  referencedColumnName="AR_BANK_ID")
 private ArBankAccount arBank;
 
 
 @ManyToOne
 @JoinColumn(name="bnk_pay_run_id",  referencedColumnName="bank_pay_run_id")
 private BnkPaymentRun bnkPaymentRun;
 

 @ManyToOne
 @JoinColumn(name="restricted_fund_id", referencedColumnName="restricted_fund_id")
 private Fund restrictedFund;
 
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
 private int changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditDocBankLine> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditDocBankLine> auditRecs) {
  this.auditRecs = auditRecs;
 }

 
 public String getBankTransCode() {
  return bankTransCode;
 }

 public void setBankTransCode(String bankTransCode) {
  this.bankTransCode = bankTransCode;
 }


 public BnkPaymentRun getBnkPaymentRun() {
  return bnkPaymentRun;
 }

 public void setBnkPaymentRun(BnkPaymentRun bnkPaymentRun) {
  this.bnkPaymentRun = bnkPaymentRun;
 }

 
 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public Date getDocDate() {
  return docDate;
 }

 public void setDocDate(Date docDate) {
  this.docDate = docDate;
 }

 public Date getPostDate() {
  return postDate;
 }

 public void setPostDate(Date postDate) {
  this.postDate = postDate;
 }

 public Fund getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(Fund restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 
 public Date getClearedDate() {
  return clearedDate;
 }

 public void setClearedDate(Date clearedDate) {
  this.clearedDate = clearedDate;
 }

 public Date getValueDate() {
  return valueDate;
 }

 public void setValueDate(Date valueDate) {
  this.valueDate = valueDate;
 }

 public BankStatement getBnkStament() {
  return bnkStament;
 }

 public void setBnkStament(BankStatement bnkStament) {
  this.bnkStament = bnkStament;
 }

 
 public String getBnkRef() {
  return bnkRef;
 }

 public void setBnkRef(String bnkRef) {
  this.bnkRef = bnkRef;
 }

 public boolean isCleared() {
  return cleared;
 }

 public void setCleared(boolean cleared) {
  this.cleared = cleared;
 }

 
 public BankAccountCompany getClearedBankAc() {
  return clearedBankAc;
 }

 public void setClearedBankAc(BankAccountCompany clearedBankAc) {
  this.clearedBankAc = clearedBankAc;
 }

 
 
 public DocLineBase getDocFiLine() {
  return docFiLine;
 }

 public void setDocFiLine(DocLineBase docFiLines) {
  this.docFiLine = docFiLines;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public ApAccount getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccount apAccount) {
  this.apAccount = apAccount;
 }

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 
 public ArBankAccount getArBank() {
  return arBank;
 }

 public void setArBank(ArBankAccount arBank) {
  this.arBank = arBank;
 }

 
 public boolean isReceipt() {
  return receipt;
 }

 public void setReceipt(boolean receipt) {
  this.receipt = receipt;
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

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 public BankAccountCompany getUnClearedBankAc() {
  return unClearedBankAc;
 }

 public void setUnClearedBankAc(BankAccountCompany unClearedBankAc) {
  this.unClearedBankAc = unClearedBankAc;
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
  if (!(object instanceof DocBankLineBase)) {
   return false;
  }
  DocBankLineBase other = (DocBankLineBase) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocBankLineBase[ id=" + id + " ]";
 }
 
}
