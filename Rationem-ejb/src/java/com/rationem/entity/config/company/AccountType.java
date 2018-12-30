/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditAccountType;
import com.rationem.entity.config.common.Module;
import com.rationem.entity.config.common.NumberRange;
import com.rationem.entity.config.common.ProcessCode;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import com.rationem.entity.config.company.Ledger;
import com.rationem.entity.user.User;
import javax.persistence.Version;
import java.util.Date;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;

import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;

import static javax.persistence.TemporalType.TIMESTAMP;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;

/**
 *
 * @author Chris
 */
@Entity
@SequenceGenerator(name = "accountType_s1", sequenceName = "comp03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@Table(name="comp03")
@NamedQueries({
    @NamedQuery(name = "allaccountTypes",query = "SELECT acType FROM AccountType acType order by acType.name "),
    @NamedQuery(name = "accountTypesByname", query = "Select acType from AccountType acType order by acType.name")
})
public class AccountType implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "accountType_s1")
 @Column(name="account_type_id")
 private Long id;
 @Column(name="account_type_name")
 private String name;
 @Column(name="descr")
 private String description;
 @Column(name="system_post")
 private boolean systemPost;
 @Column(name="debit")
 private boolean debit;
 @Column(name="control_ac")
 private boolean controlAccount;
 @ManyToOne
 @JoinColumn(name="subLedger_id", referencedColumnName="ledger_id")
 private Ledger subLedger;
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id")
 private Module module;
 @Column(name="pl")
 private boolean profitAndLossAccount;
 
 @ManyToOne
 @JoinColumn(name="process_code_id", referencedColumnName="process_code_id")
 private ProcessCode processCode;
 @Column(name="retained_earn")
 private boolean retainedEarn;

 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="created_ON")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;
 @ManyToOne
 @JoinColumn(name="num_range_id", referencedColumnName="num_cntrl_id")
 private NumberRange numberRange;
 @OneToMany(mappedBy = "accountType")
 private List<AuditAccountType> auditRecords;

 public AccountType() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditAccountType> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditAccountType> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isRetainedEarn() {
  return retainedEarn;
 }

 public void setRetainedEarn(boolean retainedEarn) {
  this.retainedEarn = retainedEarn;
 }

 public Ledger getSubLedger() {
  return subLedger;
 }

 public void setSubLedger(Ledger subLedger) {
  this.subLedger = subLedger;
 }

 
 public boolean isSystemPost() {
  return systemPost;
 }

 public void setSystemPost(boolean systemPost) {
  this.systemPost = systemPost;
 }

 public boolean isControlAccount() {
  return controlAccount;
 }

 public void setControlAccount(boolean controlAccount) {
  this.controlAccount = controlAccount;
 }

 public ProcessCode getProcessCode() {
  return processCode;
 }

 public void setProcessCode(ProcessCode processCode) {
  this.processCode = processCode;
 }

 public boolean isProfitAndLossAccount() {
  return profitAndLossAccount;
 }

 public void setProfitAndLossAccount(boolean profitAndLossAccount) {
  this.profitAndLossAccount = profitAndLossAccount;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
 }

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

 
 public NumberRange getNumberRange() {
  return numberRange;
 }

 public void setNumberRange(NumberRange numberRange) {
  this.numberRange = numberRange;
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
  if (!(object instanceof AccountType)) {
   return false;
  }
  AccountType other = (AccountType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.AccountType[ id=" + id + " ]";
 }
 
}
