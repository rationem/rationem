/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.arap;

//import com.rationem.entity.audit.AuditArBank;
import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.document.DocLineAp;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import com.rationem.entity.tr.bank.Bank;
import com.rationem.entity.tr.bank.BankAccount;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.ManyToMany;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;




/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account09")
@SequenceGenerator(name = "arBank_s1", sequenceName = "fi_account09_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class ArBankAccount implements Serializable {

 @OneToMany(mappedBy = "paymentBank")
 private List<DocLineAp> docApLines;

 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "arBank_s1")
 @Column(name="ar_bank_id")
 private Long id;
  
 @ManyToOne
 @JoinColumn(name="bank_account_id",referencedColumnName="bank_account_id")
 private BankAccount bankAccount;
 @Column(name="account_name")
 private String accountName;
 @Column(name="bank_ref")
 private String bankKey;
 @Column(name="dd_ref")
 private String ddRef;
 @Column(name="collection_auth")
 private boolean collectionAuthorisation;
 @Temporal(DATE)
 @Column(name="cll_valid_from")
 private Date collValidFrom;
 @Lob
 @Column(name="dd_coll_auth")
 private byte[] signedDD;
 @Column(name="dd_coll_file_type")
 private String ddFileType;
 
 @ManyToOne
 @JoinColumn(name="ar_account_id",referencedColumnName="ar_account_id")
 private ArAccount bankForArAccount;
 @ManyToMany(mappedBy = "apAccountBanks")
 private List<ApAccount> apAccounts;
 
 @ManyToOne
 @JoinColumn(name="ap_account_id",referencedColumnName="ap_account_id")
 private ApAccount bankForApAccount;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="revision")
 private int changes;
 /*
 @OneToMany(mappedBy = "arBank")
 private List<AuditArBank> auditRecords;
 */
 @OneToMany(mappedBy = "arBank")
 private List<DocBankLineBase> docBankLines;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public BankAccount getBankAccount() {
  return bankAccount;
 }

 public void setBankAccount(BankAccount bankAccount) {
  this.bankAccount = bankAccount;
 }

 public String getAccountName() {
  return accountName;
 }

 public void setAccountName(String accountName) {
  this.accountName = accountName;
 }

 public List<ApAccount> getApAccounts() {
  return apAccounts;
 }

 public void setApAccounts(List<ApAccount> apAccounts) {
  this.apAccounts = apAccounts;
 }

 public String getBankKey() {
  return bankKey;
 }

 public void setBankKey(String bankKey) {
  this.bankKey = bankKey;
 }

 public String getDdFileType() {
  return ddFileType;
 }

 public void setDdFileType(String ddFileType) {
  this.ddFileType = ddFileType;
 }

 public String getDdRef() {
  return ddRef;
 }

 public void setDdRef(String ddRef) {
  this.ddRef = ddRef;
 }

 public boolean isCollectionAuthorisation() {
  return collectionAuthorisation;
 }

 public void setCollectionAuthorisation(boolean collectionAuthorisation) {
  this.collectionAuthorisation = collectionAuthorisation;
 }

 public Date getCollValidFrom() {
  return collValidFrom;
 }

 public void setCollValidFrom(Date collValidFrom) {
  this.collValidFrom = collValidFrom;
 }

 public byte[] getSignedDD() {
  return signedDD;
 }

 public void setSignedDD(byte[] signedDD) {
  this.signedDD = signedDD;
 }

 public ArAccount getBankForArAccount() {
  return bankForArAccount;
 }

 public void setBankForArAccount(ArAccount bankForArAccount) {
  this.bankForArAccount = bankForArAccount;
 }

 public ApAccount getBankForApAccount() {
  return bankForApAccount;
 }

 public void setBankForApAccount(ApAccount bankForApAccount) {
  this.bankForApAccount = bankForApAccount;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
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

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 public List<DocBankLineBase> getDocBankLines() {
  return docBankLines;
 }

 public void setDocBankLines(List<DocBankLineBase> docBankLines) {
  this.docBankLines = docBankLines;
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
  if (!(object instanceof ArBankAccount)) {
   return false;
  }
  ArBankAccount other = (ArBankAccount) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.arap.ArBank[ id=" + id + " ]";
 }
 
}
