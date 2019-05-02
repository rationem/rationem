/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditNumberControl;
import com.rationem.entity.config.company.AccountType;
//import com.rationem.entity.config.fi.FiGlActType;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
//import com.rationem.entity.config.fi.FiGlActType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import com.rationem.entity.user.User;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;
import static javax.persistence.InheritanceType.JOINED;
import javax.persistence.OneToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Multitenant(SINGLE_TABLE)
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("common.NumberRange")
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "NumberControl_s1", sequenceName = "bac_config08_s1", allocationSize = 1,initialValue=1)
@Table(name="bac_config08" )
@NamedQueries({
@NamedQuery(name="getNumberControlAll",
  query="Select n from NumberRange n order by n.shortDescr"),
@NamedQuery(name="numberControlBySrtDescr",
  query="Select n from NumberRange n where n.shortDescr = :sDescr order by n.shortDescr")
})
public class NumberRange implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "NumberControl_s1")
 @Column(name="num_cntrl_id")
 private Long NumberControlId;
 @Column(name="short_descr")
 private String shortDescr;
 @Column(name="long_descr")
 private String longDescr;
 @OneToMany(mappedBy = "numberRange")
 @JoinColumn(name="account_type_id", referencedColumnName="account_type_id")
 private List<AccountType> accountTypes;
 
    
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id")
 private Module module;
 @Column(name="from_num")
 private int fromNum;
 @Column(name="to_num")
 private int toNum;
 @Column(name="next_num")
 private int nextNum;
 @Column(name="auto_num")
 private boolean autoNum;
 @ManyToOne
 @JoinColumn(name="created_by_id" , referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedDate;
 @Version
 @Column(name="changed")
 private long changes;
 
 
 @OneToMany(mappedBy = "numControl")
 private List<AuditNumberControl> auditNumberContolChanges;
 @ManyToOne
 @JoinColumn(name="num_rng_ty_id" , referencedColumnName="id")
 private NumberRangeType numberRangeType;
 
    public NumberRange() {
    }

 public List<AccountType> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountType> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public List<AuditNumberControl> getAuditNumberContolChanges() {
  return auditNumberContolChanges;
 }

 public void setAuditNumberContolChanges(List<AuditNumberControl> auditNumberContolChanges) {
  this.auditNumberContolChanges = auditNumberContolChanges;
 }


 public Long getId() {
  return NumberControlId;
 }

 public void setId(Long id) {
  this.NumberControlId = id;
 }

 
 public Long getNumberControlId() {
  return NumberControlId;
 }

 public void setNumberControlId(Long NumberControlId) {
  this.NumberControlId = NumberControlId;
 }

 public NumberRangeType getNumberRangeType() {
  return numberRangeType;
 }

 public void setNumberRangeType(NumberRangeType numberRangeType) {
  this.numberRangeType = numberRangeType;
 }

 
 public String getShortDescr() {
  return shortDescr;
 }

 public void setShortDescr(String shortDescr) {
  this.shortDescr = shortDescr;
 }

 public String getLongDescr() {
  return longDescr;
 }

 public void setLongDescr(String longDescr) {
  this.longDescr = longDescr;
 }

 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
 }



 
 public int getFromNum() {
  return fromNum;
 }

 public void setFromNum(int fromNum) {
  this.fromNum = fromNum;
 }

 public int getToNum() {
  return toNum;
 }

 public void setToNum(int toNum) {
  this.toNum = toNum;
 }

 public int getNextNum() {
  return nextNum;
 }

 public void setNextNum(int nextNum) {
  this.nextNum = nextNum;
 }

 

 public boolean isAutoNum() {
  return autoNum;
 }

 public void setAutoNum(boolean autoNum) {
  this.autoNum = autoNum;
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
  hash += (NumberControlId != null ? NumberControlId.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof NumberRange)) {
   return false;
  }
  NumberRange other = (NumberRange) object;
  if ((this.NumberControlId == null && other.NumberControlId != null) || 
          (this.NumberControlId != null && !this.NumberControlId.equals(other.NumberControlId))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.NumberControl[ id=" + NumberControlId + " ]";
 }
 
}
