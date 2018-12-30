/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.audit.AuditLocaleCode;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import javax.persistence.SequenceGenerator;
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
 * @author user
 */
@Entity
@Table(name="bac_config19" )
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "LocaleCode_s1", sequenceName = "bac_config19_s1", allocationSize = 1,initialValue=1)
@NamedQuery(name="localeByCode", query="Select l from LocaleCode l where l.localeCode = :code ")
public class LocaleCode implements Serializable {
 @OneToMany(mappedBy = "loc")
 private List<AuditLocaleCode> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "LocaleCode_s1")
 @Column(name="locale_id")
 private Long id;
 @Column(name="locale_code")
 private String localeCode;
 @Column(name="country_name")
 private String countryName;
 @Column(name="lang_name")
 private String langName;
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
 private int changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getLocaleCode() {
  return localeCode;
 }

 public void setLocaleCode(String localeCode) {
  this.localeCode = localeCode;
 }

 public String getCountryName() {
  return countryName;
 }

 public void setCountryName(String countryName) {
  this.countryName = countryName;
 }

 public String getLangName() {
  return langName;
 }

 public void setLangName(String langName) {
  this.langName = langName;
 }

 public List<AuditLocaleCode> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditLocaleCode> auditRecords) {
  this.auditRecords = auditRecords;
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
  if (!(object instanceof LocaleCode)) {
   return false;
  }
  LocaleCode other = (LocaleCode) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.LocaleCode[ id=" + id + " ]";
 }
 
}
