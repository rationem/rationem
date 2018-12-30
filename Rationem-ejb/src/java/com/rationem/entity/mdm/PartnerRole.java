/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.audit.AuditPartnerRole;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.OneToMany;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author user
 */
@Entity
@Table(name="ptnr04")
@NamedQueries({
 @NamedQuery(name="ptnrRoleByCode", query="Select r from PartnerRole r where r.roleCode = :code and r.inUse = true and r.userRole = false"),
 @NamedQuery(name="ptnrActRoles", 
   query="Select r from PartnerRole r where r.inUse = true and r.userRole = false"),
 @NamedQuery(name="ptnrRolesDeact",query="Select r from PartnerRole r where r.userRole = false"),
 @NamedQuery(name="ptnrUserRolesIncDeact",query="Select r from PartnerRole r where  r.userRole = true")
})
@SequenceGenerator(name = "ptnrRole_s1", sequenceName = "ptnr04_s1", allocationSize = 1,initialValue=1 )
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class PartnerRole implements Serializable {

 private static final long serialVersionUID = 1L;
 @Id
 @Column(name="ptnr_role_id")
 @GeneratedValue(generator = "ptnrRole_s1")
 private Long ptnrRoleId;
 
 @Column(name="code")
 private String roleCode;
 @Column(name="name")
 private String roleName;
 @Column(name="in_use")
 private boolean inUse;
 @Column(name="taxable")
 private boolean taxable;
 @Column(name="user_role")
 private boolean userRole;
 
 
 @ManyToMany(mappedBy = "partnerRoles")
 private List<PartnerBase> partners;
 

 @OneToMany(mappedBy = "ptnrRole")
 private List<AuditPartnerRole> auditRecords;
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;

 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdOn;

  
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;

 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedOn;
 
 @Version
 @Column(name="changes")
 private Long revision;

 
 public Long getPtnrRoleId() {
  return ptnrRoleId;
 }

 public void setPtnrRoleId(Long id) {
  this.ptnrRoleId = id;
 }

 public List<AuditPartnerRole> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditPartnerRole> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public boolean isInUse() {
  return inUse;
 }

 public void setInUse(boolean inUse) {
  this.inUse = inUse;
 }

 
 public String getRoleCode() {
  return roleCode;
 }

 public void setRoleCode(String roleCode) {
  this.roleCode = roleCode;
 }

 public String getRoleName() {
  return roleName;
 }

 public void setRoleName(String roleName) {
  this.roleName = roleName;
 }

 public List<PartnerBase> getPartners() {
  return partners;
 }

 public void setPartners(List<PartnerBase> partners) {
  this.partners = partners;
 }

 public boolean isTaxable() {
  return taxable;
 }

 public void setTaxable(boolean taxable) {
  this.taxable = taxable;
 }

 public boolean isUserRole() {
  return userRole;
 }

 public void setUserRole(boolean userRole) {
  this.userRole = userRole;
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

 
 public Long getRevision() {
  return revision;
 }

 public void setRevision(Long revision) {
  this.revision = revision;
 }
 
 

 @Override
 public int hashCode() {
  int hash = 0;
  hash += (ptnrRoleId != null ? ptnrRoleId.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof PartnerRole)) {
   return false;
  }
  PartnerRole other = (PartnerRole) object;
  return !((this.ptnrRoleId == null && other.ptnrRoleId != null) || (this.ptnrRoleId != null 
    && !this.ptnrRoleId.equals(other.ptnrRoleId)));
 }

 @Override
 public String toString() {
  StringBuilder sb = new StringBuilder();
  sb.append("com.rationem.entity.mdm.PartnerRole[ id= ");
  sb.append(String.valueOf(ptnrRoleId));
  sb.append(" ]");
  return sb.toString();
 }
 
}
