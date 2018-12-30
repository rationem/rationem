/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditPostType;
import com.rationem.entity.config.common.Module;
import com.rationem.entity.user.User;
import javax.persistence.NamedQuery;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;

import static javax.persistence.TemporalType.TIMESTAMP;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
/**
 * 
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config02")
@NamedQueries({
 @NamedQuery(name="findAllPostTypes", query="Select pt from PostType pt order by pt.postTypeCode")

  
})
@SequenceGenerator(name = "postType_s1", sequenceName = "fi_config02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class PostType implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "postType_s1")
 @Column(name="post_type_id")
 private Long id;
 @Column(name="post_code")
 private String postTypeCode;
 @Column(name="description")
 private String description;
 @Column(name="mod_descr")
 private String moduleDescription;
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id")
 private Module module;
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id")
 private Ledger ledger;
 @Column(name="debit")
 private boolean debit;
    /**
     * Is this + or -
     */
 @Column(name="post_sign")
 private char sign;
 @Column(name="sys_use")
 private boolean sysUse;
 @OneToOne
 @JoinColumn(name="rev_post_type_id", referencedColumnName="post_type_id")
 private PostType revPostType;
 @Column(name="process_code", length=20)
 private String procCode;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createDate;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="changes")
 private int revision;
 @OneToMany(mappedBy = "postType")
 private List<AuditPostType> AuditRecs;

 public PostType() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditPostType> getAuditRecs() {
  return AuditRecs;
 }

 public void setAuditRecs(List<AuditPostType> AuditRecs) {
  this.AuditRecs = AuditRecs;
 }

 public String getProcCode() {
  return procCode;
 }

 public void setProcCode(String procCode) {
  this.procCode = procCode;
 }

 
 
 public String getPostTypeCode() {
  return postTypeCode;
 }

 public void setPostTypeCode(String postTypeCode) {
  this.postTypeCode = postTypeCode;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
 }

 public String getModuleDescription() {
  return moduleDescription;
 }

 public void setModuleDescription(String moduleDescription) {
  this.moduleDescription = moduleDescription;
 }

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

 public char getSign() {
  return sign;
 }

 public void setSign(char sign) {
  this.sign = sign;
 }

 public PostType getRevPostType() {
  return revPostType;
 }

 public void setRevPostType(PostType revPostType) {
  this.revPostType = revPostType;
 }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
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

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
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
  if (!(object instanceof PostType)) {
   return false;
  }
  PostType other = (PostType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.PostType[ id=" + id + " ]";
 }
 
}
