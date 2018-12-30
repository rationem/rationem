/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

import com.rationem.entity.audit.AuditBank;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.rationem.entity.mdm.PartnerCorporate;
//import com.rationem.entity.fi.arap.ArBank;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name="bnk01")
@NamedQueries({
@NamedQuery(name = "allBanks",
query ="select bnk from Bank bnk "),
@NamedQuery(name = "banksByBnkCode",
query ="select bnk from Bank bnk where bnk.bankCode like :code"),  
@NamedQuery(name = "bankByBnkCode",
query ="select bnk from Bank bnk where bnk.bankCode = :code"),
@NamedQuery(name = "banksByName",
query ="select bnk from Bank bnk where bnk.bankOrganisation.tradingName like :name")
})
@SequenceGenerator(name = "bank_s1", sequenceName = "bnk01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class Bank implements Serializable {
 @OneToMany(mappedBy = "bank")
 private List<AuditBank> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bank_s1")
 @Column(name="bank_id")
  private Long id;
  @Column(name="bank_code")
  private String bankCode;
  @Column(name="chaps_bank_code")
  private String chapsBankCode;
  @OneToOne
  @JoinColumn(name="bank_partner_id", referencedColumnName="partner_id")
  private PartnerCorporate bankOrganisation;
  @OneToOne
  @JoinColumn(name="bank_address_id", referencedColumnName="address_id")
  private Address bankAddress;
  
  @OneToMany(mappedBy = "bank")
  private List<BankBranch> bankBranches;
  
  @OneToOne
  @JoinColumn(name="bank_contact_partner_id", referencedColumnName="partner_id")
  private PartnerBase bankContact;
  
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

 public String getBankCode() {
  return bankCode;
 }

 public void setBankCode(String bankCode) {
  this.bankCode = bankCode;
 }

 public String getChapsBankCode() {
  return chapsBankCode;
 }

 public void setChapsBankCode(String chapsBankCode) {
  this.chapsBankCode = chapsBankCode;
 }

 public PartnerCorporate getBankOrganisation() {
  return bankOrganisation;
 }

 public void setBankOrganisation(PartnerCorporate bankOrganisation) {
  this.bankOrganisation = bankOrganisation;
 }

 public Address getBankAddress() {
  return bankAddress;
 }

 public void setBankAddress(Address bankAddress) {
  this.bankAddress = bankAddress;
 }

 public List<BankBranch> getBankBranches() {
  return bankBranches;
 }

 public void setBankBranches(List<BankBranch> bankBranches) {
  this.bankBranches = bankBranches;
 }

 public PartnerBase getBankContact() {
  return bankContact;
 }

 public void setBankContact(PartnerBase bankContact) {
  this.bankContact = bankContact;
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
  if (!(object instanceof Bank)) {
   return false;
  }
  Bank other = (Bank) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bank.Bank[ id=" + id + " ]";
 }
 
}
