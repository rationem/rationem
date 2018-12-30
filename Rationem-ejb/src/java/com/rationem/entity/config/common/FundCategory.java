/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditFundCategory;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config15")
@NamedQueries({
 @NamedQuery(name="fndCatAll", query="Select fnd from FundCategory fnd order by fnd.catRef"),
 @NamedQuery(name="fndCatByRef", 
        query="Select fnd from FundCategory fnd where fnd.catRef = :ref ")
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "FundCategory_s1", sequenceName = "fi_config15_s1", allocationSize = 1,initialValue=1)

public class FundCategory implements Serializable {
 @OneToMany(mappedBy = "fndCategory")
 private List<AuditFundCategory> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "FundCategory_s1")
 @Column(name="fnd_cat_id")
 private Long id;
 @Column(name="fund_cat_ref")
 private String catRef;
 @Column(name="descr")
 private String description;
 @Column(name="restricted")
 private boolean restricted;
 @Column(name="desig")
 private boolean designated;
 @Column(name="capital")
 private boolean endowment;
 @Column(name="perm")
 private boolean permanent;
 @Column(name="process_code")
 private String processCode;
 @OneToMany(mappedBy = "fundCategory")
 private List<Fund> fund;
 @ManyToOne
 @JoinColumn(name="created_by_id" , referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id" , referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="revision")
 private int changes;
 

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditFundCategory> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditFundCategory> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getCatRef() {
  return catRef;
 }

 public void setCatRef(String catRef) {
  this.catRef = catRef;
 }

 
 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isDesignated() {
  return designated;
 }

 public void setDesignated(boolean designated) {
  this.designated = designated;
 }

 public boolean isEndowment() {
  return endowment;
 }

 public void setEndowment(boolean endowment) {
  this.endowment = endowment;
 }

 public boolean isPermanent() {
  return permanent;
 }

 public void setPermanent(boolean permanent) {
  this.permanent = permanent;
 }

 
 public boolean isRestricted() {
  return restricted;
 }

 public void setRestricted(boolean restricted) {
  this.restricted = restricted;
 }

 public String getProcessCode() {
  return processCode;
 }

 public void setProcessCode(String processCode) {
  this.processCode = processCode;
 }

 public List<Fund> getFund() {
  return fund;
 }

 public void setFund(List<Fund> fund) {
  this.fund = fund;
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

 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof FundCategory)) {
   return false;
  }
  FundCategory other = (FundCategory) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.FundCategory[ id=" + id + " ]";
 }
 
}
