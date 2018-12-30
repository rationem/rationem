/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.audit.AuditCurrency;
import com.rationem.entity.config.company.ChartOfAccounts;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import javax.persistence.NamedQuery;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @author user
 */
@Entity
@Table(name="mdm03" )
@NamedQuery(name="currencyByCode", query="Select c from Currency c where c.currAlphaCode = :code")
@SequenceGenerator(name = "currency_s1", sequenceName = "mdm03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class Currency implements Serializable {
 @OneToMany(mappedBy = "chartCurrency")
 private List<ChartOfAccounts> accountCharts;
 @OneToMany(mappedBy = "curr")
 private List<AuditCurrency> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "currency_s1")
 @Column(name="currency_id")
 private Long id;
 @Column(name="curr_alpha")
 private String currAlphaCode;
 @Column(name="name")
 private String name;
 @Column(name="curr_symbol")
 private String currSymbol;
 @Column(name="minor_unit")
 private int minorUnit;
 @Column(name="curr_numeric_code")
 private int currNumCode;
 @Column(name="major_unit_descr", length=20)
 private String majorUnitDescr;
 @Column(name="major_unit_descr_plural", length=20)
 private String majorUnitDescrPl;
 @Column(name="minor_unit_descr", length=20)
 private String minorUnitDescr;
 @Column(name="minor_unit_descr_plural", length=20)
 private String minorUnitDescrPl;
 
 @Temporal(TIMESTAMP)
 @Column(name="create_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="create_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="change_on")
 private Date changedOn;
 @ManyToOne
 @JoinColumn(name="change_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<ChartOfAccounts> getAccountCharts() {
  return accountCharts;
 }

 public void setAccountCharts(List<ChartOfAccounts> accountCharts) {
  this.accountCharts = accountCharts;
 }

 public List<AuditCurrency> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditCurrency> auditRecords) {
  this.auditRecords = auditRecords;
 }

 

 public int getCurrNumCode() {
  return currNumCode;
 }

 public void setCurrNumCode(int currNumCode) {
  this.currNumCode = currNumCode;
 }

 public String getCurrSymbol() {
  return currSymbol;
 }

 public void setCurrSymbol(String currSymbol) {
  this.currSymbol = currSymbol;
 }

 public String getCurrAlphaCode() {
  return currAlphaCode;
 }

 public void setCurrAlphaCode(String currAlphaCode) {
  this.currAlphaCode = currAlphaCode;
 }

 public String getMajorUnitDescr() {
  return majorUnitDescr;
 }

 public void setMajorUnitDescr(String majorUnitDescr) {
  this.majorUnitDescr = majorUnitDescr;
 }

 public String getMajorUnitDescrPl() {
  return majorUnitDescrPl;
 }

 public void setMajorUnitDescrPl(String majorUnitDescrPl) {
  this.majorUnitDescrPl = majorUnitDescrPl;
 }

 
 public int getMinorUnit() {
  return minorUnit;
 }

 public void setMinorUnit(int minorUnit) {
  this.minorUnit = minorUnit;
 }

 public String getMinorUnitDescr() {
  return minorUnitDescr;
 }

 public void setMinorUnitDescr(String minorUnitDescr) {
  this.minorUnitDescr = minorUnitDescr;
 }

 public String getMinorUnitDescrPl() {
  return minorUnitDescrPl;
 }

 public void setMinorUnitDescrPl(String minorUnitDescrPl) {
  this.minorUnitDescrPl = minorUnitDescrPl;
 }
 
 
 public String getName() {
  return name;
 }

 

 public void setName(String name) {
  this.name = name;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof Currency)) {
   return false;
  }
  Currency other = (Currency) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.mdm.Currency[ id=" + id + " ]";
 }
 
}
