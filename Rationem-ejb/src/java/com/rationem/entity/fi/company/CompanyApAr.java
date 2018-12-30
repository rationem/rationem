/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

import com.rationem.entity.audit.AuditCompApAr;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 *
 * @author user
 */
@Entity
@Table(name="comp10")
@SequenceGenerator(name = "companyBasicApAr_s1", sequenceName = "comp10_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class CompanyApAr implements Serializable {

 @OneToMany(mappedBy = "apAr")
 private List<AuditCompApAr> auditRecs;

 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "companyBasicApAr_s1")
 @Column(name="id")
 private Long id;
 
 @OneToOne
 @JoinColumn(name="COMP_ID", referencedColumnName="home_comp_id")
 private CompanyBasic comp;
 @Column(name="AR_BUCKET_1")
 private int arBucket1;
 @Column(name="AR_BUCKET_2")
 private int arBucket2;
 @Column(name="AR_BUCKET_3")
 private int arBucket3;
 @Column(name="AR_BUCKET_4")
 private int arBucket4;
 @Column(name="AP_BUCKET_1")
 private int apBucket1;
 @Column(name="AP_BUCKET_2")
 private int apBucket2;
 @Column(name="AP_BUCKET_3")
 private int apBucket3;
 @Column(name="AP_BUCKET_4")
 private int apBucket4;
 
 @ManyToOne
 @JoinColumn(name="created_by_id",  referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id",  referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCompApAr> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditCompApAr> auditRecs) {
  this.auditRecs = auditRecs;
 }

 
 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public int getArBucket1() {
  return arBucket1;
 }

 public void setArBucket1(int arBucket1) {
  this.arBucket1 = arBucket1;
 }

 public int getArBucket2() {
  return arBucket2;
 }

 public void setArBucket2(int arBucket2) {
  this.arBucket2 = arBucket2;
 }

 public int getArBucket3() {
  return arBucket3;
 }

 public void setArBucket3(int arBucket3) {
  this.arBucket3 = arBucket3;
 }

 public int getArBucket4() {
  return arBucket4;
 }

 public void setArBucket4(int arBucket4) {
  this.arBucket4 = arBucket4;
 }

 public int getApBucket1() {
  return apBucket1;
 }

 public void setApBucket1(int apBucket1) {
  this.apBucket1 = apBucket1;
 }

 public int getApBucket2() {
  return apBucket2;
 }

 public void setApBucket2(int apBucket2) {
  this.apBucket2 = apBucket2;
 }

 public int getApBucket3() {
  return apBucket3;
 }

 public void setApBucket3(int apBucket3) {
  this.apBucket3 = apBucket3;
 }

 public int getApBucket4() {
  return apBucket4;
 }

 public void setApBucket4(int apBucket4) {
  this.apBucket4 = apBucket4;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof CompanyApAr)) {
   return false;
  }
  CompanyApAr other = (CompanyApAr) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.CompanyApAr[ id=" + id + " ]";
 }
 
}
