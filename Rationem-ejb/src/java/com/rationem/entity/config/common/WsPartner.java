/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import javax.persistence.NamedQuery;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config12")
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "WsPartner_s1", sequenceName = "bac_config12_s1", allocationSize = 1,initialValue=1)
@NamedQuery(name="wsPtnrByWsName", query="select o from WsPartner o where o.wsName = :name ")
public class WsPartner implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @Column(name="ws_ptnr_id")
 @GeneratedValue(generator = "WsPartner_s1")
 private Long id;
 @Column(name="ws_name")
 private String wsName;
 @Column(name="ws_description", length=200)
 private String descr;
 @Column(name="active")
 private boolean active;
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Column(name="ws_login_name")
 private String accountName;
 @Column(name="ws_password")
 private String password;
 @Column(name="ws_calls")
 private Long calls;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createDate;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changeDate;
 @ManyToOne
 @JoinColumn(name="created_by", referencedColumnName="partner_id")
 private User createdBy;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int revision;

 public WsPartner() {
 }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

 public String getWsName() {
  return wsName;
 }

 public void setWsName(String wsName) {
  this.wsName = wsName;
 }

 public boolean isActive() {
  return active;
 }

 public void setActive(boolean active) {
  this.active = active;
 }

 public Date getValidFrom() {
  return validFrom;
 }

 public void setValidFrom(Date validFrom) {
  this.validFrom = validFrom;
 }

 public String getAccountName() {
  return accountName;
 }

 public void setAccountName(String accountName) {
  this.accountName = accountName;
 }

 public String getPassword() {
  return password;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public Long getCalls() {
  return calls;
 }

 public void setCalls(Long calls) {
  this.calls = calls;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public Date getChangeDate() {
  return changeDate;
 }

 public void setChangeDate(Date changeDate) {
  this.changeDate = changeDate;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
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
  if (!(object instanceof WsPartner)) {
   return false;
  }
  WsPartner other = (WsPartner) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.WsPartner[ id=" + id + " ]";
 }
 
}
