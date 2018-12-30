/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditLineType;
import java.util.Collection;
import java.util.ArrayList;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Version;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import javax.persistence.Temporal;
import com.rationem.entity.user.User;
import java.util.List;
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
@Table(name="fi_config01")
@NamedQueries({
@NamedQuery(name="lineTypeByDescr", query=" select l from LineTypeRule l where l.lineCode = :lineCode"  ),
@NamedQuery(name="lineTypesAll",query="select l from LineTypeRule l")
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "LineTypeRule_s1", sequenceName = "fi_config01_s1", allocationSize = 1,initialValue=1)

public class LineTypeRule implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "LineTypeRule_s1")
 @Column(name="line_type_id")
 private Long id;
 @Column(name="line_type_code")
 private String lineCode;
 @Column(name="description")
 private String description;
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id" )
 private Module module;
 @Column(name="sys_use")
 private boolean sysUse;
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
 @Column(name="changes")
 private long changes;
 @OneToMany(mappedBy = "lineType")
 private List<AuditLineType> auditRecords;

 public LineTypeRule() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditLineType> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditLineType> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getLineCode() {
  return lineCode;
 }

 public void setLineCode(String lineCode) {
  this.lineCode = lineCode;
 }

 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
 }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
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
  if (!(object instanceof LineTypeRule)) {
   return false;
  }
  LineTypeRule other = (LineTypeRule) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.LineTypeRule[ id=" + id + " ]";
 }
 
}
