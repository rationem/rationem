/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="bnk05")
@SequenceGenerator(name = "bankStatement_s1", sequenceName = "bnk05_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class BankStatement implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bankStatement_s1")
 @Column(name="bank_statement_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="comp_basic_id", referencedColumnName="home_comp_id")
 private CompanyBasic comp;
 @ManyToOne
 @JoinColumn(name="bank_acnt_id", referencedColumnName="bank_account_id")
 private BankAccountCompany bankAc;
 @Column(name="number")
 int statNum;
 @Column(name="bnk_ref")
 String reference;
 @Column(name="start_date")
 @Temporal(DATE)
 Date startDate;
 @Column(name="end_date")
 @Temporal(DATE)
 Date endDate;
 @Column(name="opening_bal")
 double openingBalance;
 @Column(name="closing_bal")
 double closingBalance;
 @Column(name="complete")
 boolean complete;
 @OneToMany(mappedBy = "bnkStament")
 List<DocBankLineBase> bankItem;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")    
 User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 Date changedOn;
 @Version
 @Column(name="revision")
 long changes;

 public BankStatement() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public BankAccountCompany getBankAc() {
  return bankAc;
 }

 public void setBankAc(BankAccountCompany bankAc) {
  this.bankAc = bankAc;
 }

 public int getStatNum() {
  return statNum;
 }

 public void setStatNum(int statNum) {
  this.statNum = statNum;
 }

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public Date getStartDate() {
  return startDate;
 }

 public void setStartDate(Date startDate) {
  this.startDate = startDate;
 }

 public Date getEndDate() {
  return endDate;
 }

 public void setEndDate(Date endDate) {
  this.endDate = endDate;
 }

 public double getOpeningBalance() {
  return openingBalance;
 }

 public void setOpeningBalance(double openingBalance) {
  this.openingBalance = openingBalance;
 }

 public double getClosingBalance() {
  return closingBalance;
 }

 public void setClosingBalance(double closingBalance) {
  this.closingBalance = closingBalance;
 }

 public boolean isComplete() {
  return complete;
 }

 public void setComplete(boolean complete) {
  this.complete = complete;
 }

 public List<DocBankLineBase> getBankItem() {
  return bankItem;
 }

 public void setBankItem(List<DocBankLineBase> bankItem) {
  this.bankItem = bankItem;
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

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
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
  if (!(object instanceof BankStatement)) {
   return false;
  }
  BankStatement other = (BankStatement) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bank.BankStatement[ id=" + id + " ]";
 }
 
}
