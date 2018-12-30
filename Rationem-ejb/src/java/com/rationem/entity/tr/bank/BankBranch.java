/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

//import com.rationem.entity.audit.AuditBankBranch;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.util.Date;
//import com.rationem.entity.fi.arap.ArBank;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

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
@Table(name="bnk03")
@NamedQueries({
@NamedQuery(name = "bankBranchBySortCode",
query ="select b from BankBranch b where b.sortCode = :sortCd "),
@NamedQuery(name = "bankBranchesBySortCode",
query ="select b from BankBranch b where b.sortCode like :sortCd "),
@NamedQuery(name = "bankBranchesAll",
query ="select b from BankBranch b  ")
  
})
@SequenceGenerator(name = "bankBranch_s1", sequenceName = "bnk03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class BankBranch implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bankBranch_s1")
 @Column(name="bank_branch_id")
  private Long id;
  @ManyToOne
  @JoinColumn(name="bank_org_id", referencedColumnName="bank_id")
  private Bank bank;
  @Column(name="sort_code")
  private String sortCode;
  @Column(name="branch_name")
  private String branchName;
  @Column(name="swift_code")
  private String swiftCode;
  @Column(name="sub_branch")
  private boolean subBranch;
  @Column(name="phone_area")
  private String phoneAreaCode;
  @Column(name="phone_number")
  private String phoneNumber;
  @ManyToOne
  @JoinColumn(name="bank_branch_address_id", referencedColumnName="address_id")
  private Address branchAddress;

  @OneToMany(mappedBy = "accountForBranch")
  private List<BankAccount> accounts;
  /*
  @OneToMany(mappedBy = "bankBranch")
  private List<AuditBankBranch> bankBranchAuditRecs;
  */
  @ManyToOne
  @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
  private User createdBy;
  @Temporal(TIMESTAMP)
  @Column(name="created_on")
  private Date createdOn;
  @ManyToOne
  @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
  private User updatedBy;
  @Temporal(TIMESTAMP)
  @Column(name="changed_on")
  private Date updatedOn;
  @Version
  @Column(name="changes")
  private int revision;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Bank getBank() {
  return bank;
 }

 public void setBank(Bank bank) {
  this.bank = bank;
 }

 public String getSortCode() {
  return sortCode;
 }

 public void setSortCode(String sortCode) {
  this.sortCode = sortCode;
 }

 public String getBranchName() {
  return branchName;
 }

 public void setBranchName(String branchName) {
  this.branchName = branchName;
 }

 public String getSwiftCode() {
  return swiftCode;
 }

 public void setSwiftCode(String swiftCode) {
  this.swiftCode = swiftCode;
 }

 public boolean isSubBranch() {
  return subBranch;
 }

 public void setSubBranch(boolean subBranch) {
  this.subBranch = subBranch;
 }

 public String getPhoneAreaCode() {
  return phoneAreaCode;
 }

 public void setPhoneAreaCode(String phoneAreaCode) {
  this.phoneAreaCode = phoneAreaCode;
 }

 public String getPhoneNumber() {
  return phoneNumber;
 }

 public void setPhoneNumber(String phoneNumber) {
  this.phoneNumber = phoneNumber;
 }

 public Address getBranchAddress() {
  return branchAddress;
 }

 public void setBranchAddress(Address branchAddress) {
  this.branchAddress = branchAddress;
 }

 public List<BankAccount> getAccounts() {
  return accounts;
 }

 public void setAccounts(List<BankAccount> accounts) {
  this.accounts = accounts;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof BankBranch)) {
   return false;
  }
  BankBranch other = (BankBranch) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bank.BankBranch[ id=" + id + " ]";
 }
 
}
