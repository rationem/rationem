/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditLedger;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.fi.company.CompPostPer;
import com.rationem.entity.fi.company.PeriodControl;
import com.rationem.entity.user.User;
import javax.persistence.NamedQueries;
//import com.rationem.entity.config.company.PostType;
//import com.rationem.entity.fi.company.PeriodControl;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.util.Date;
import com.rationem.entity.user.UserLogin;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;


import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config14")
@NamedQueries({
    @NamedQuery(name="getAllLedgers",query="Select l from Ledger l"),
    @NamedQuery(name="getLedgersByCriteria",
     query="Select l from Ledger l where l.name like :name and l.descr like :descr and l.code like :cd")

})
@SequenceGenerator(name = "ledger_s1", sequenceName = "fi_config14_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class Ledger implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "ledger_s1")
 @Column(name="ledger_id")
 private Long id;
 @Column(name="ledger_name")
 private String name;
 @Column(name="description")
 private String descr;
 @Column(name="code")
 private String code;
 
 @OneToMany(mappedBy = "ledger")
 private List<PeriodControl> periodControls;
 @OneToMany(mappedBy = "ledger")
 private List<PostType> postTypes;
 @OneToMany(mappedBy = "ledger")
 private List<PaymentType> paymentTypes;
  
 @ManyToOne
 @JoinColumn(name="created_by_id")
 private User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id")
 private User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;
 @OneToMany(mappedBy = "subLedger")
 private List<AccountType> accountTypes;
 
 @OneToMany(mappedBy = "ledger")
 private List<AuditLedger> auditRecs;
 @OneToMany(mappedBy = "ledger")
 private List<CompPostPer> compPostPeriods;
    
  


 public Ledger() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AccountType> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountType> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public List<AuditLedger> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditLedger> auditRecs) {
  this.auditRecs = auditRecs;
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

 

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public List<PeriodControl> getPeriodControls() {
  return periodControls;
 }

 public void setPeriodControls(List<PeriodControl> periodControls) {
  this.periodControls = periodControls;
 }

 public List<PostType> getPostTypes() {
  return postTypes;
 }

 public void setPostTypes(List<PostType> postTypes) {
  this.postTypes = postTypes;
 }


 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof Ledger)) {
   return false;
  }
  Ledger other = (Ledger) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.Ledger[ id=" + id + " ]";
 }
 
}
