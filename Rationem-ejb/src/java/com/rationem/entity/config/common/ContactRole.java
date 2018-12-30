/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditContactRole;
import com.rationem.entity.cm.Contact;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;


import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author user
 */
@Entity
@Table(name="bac_config20")
@NamedQueries({
 @NamedQuery(name="contRoleByName", query="Select c from ContactRole c where c.name = :contRoleName "),
 @NamedQuery(name="contRoleAll", query="Select c from ContactRole c ")
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "ContactRole_s1", sequenceName = "bac_config20_s1", allocationSize = 1,initialValue=1)
public class ContactRole implements Serializable {
 @OneToMany(mappedBy = "contactRole")
 private List<AuditContactRole> auditRecords;
 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "ContactRole_s1")
 @Column(name="contact_role_id")
 private Long id;
 @Column(name="name")
 private String name;
 @Column(name="descr")
 private String description;
 @Column(name="in_bound")
 private boolean inbound;
 @OneToMany(mappedBy = "role")
 List<Contact> contacts;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private int revision;
 
 
 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditContactRole> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditContactRole> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public List<Contact> getContacts() {
  return contacts;
 }

 public void setContacts(List<Contact> contacts) {
  this.contacts = contacts;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isInbound() {
  return inbound;
 }

 public void setInbound(boolean inbound) {
  this.inbound = inbound;
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
  if (!(object instanceof ContactRole)) {
   return false;
  }
  ContactRole other = (ContactRole) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.ContactRole[ id=" + id + " ]";
 }
 
}
