/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.NamedQuery;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="bnk07")
@NamedQuery(name="bnkPayRunForComp", query="Select pr from BnkPaymentRun pr "
        + "where pr.comp.id = :compId and pr.reference = :ref")
@SequenceGenerator(name = "bankPaymentRun_s1", sequenceName = "bnk07_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class BnkPaymentRun implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "bankPaymentRun_s1")
 @Column(name="bank_pay_run_id")
 private Long id;
 @Column(name="reference")
 private String reference;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id" )
 private CompanyBasic comp;
 @Temporal(DATE)
 @Column(name="run_date")
 private Date runDate;
 @OneToMany(mappedBy = "bnkPaymentRun")
 private List<DocBankLineBase> bankLines;
 
 @OneToMany(mappedBy = "bnkPaymentRun")
 private List<DocLineBase> paymentFiDocs;
 @Temporal(TIMESTAMP)
 @Column(name="create_on")
 private Date createDate;
 @ManyToOne
 @JoinColumn(name="created_by_id")
 private User createBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date updateDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id")
 private User updateBy;
 @Version
 @Column(name="changes")
 private long changes;
 

 public BnkPaymentRun() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public Date getRunDate() {
  return runDate;
 }

 public void setRunDate(Date runDate) {
  this.runDate = runDate;
 }

 public List<DocBankLineBase> getBankLines() {
  return bankLines;
 }

 public void setBankLines(List<DocBankLineBase> bankLines) {
  this.bankLines = bankLines;
 }

 public List<DocLineBase> getPaymentFiDocs() {
  return paymentFiDocs;
 }

 public void setPaymentFiDocs(List<DocLineBase> paymentFiDocs) {
  this.paymentFiDocs = paymentFiDocs;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public User getCreateBy() {
  return createBy;
 }

 public void setCreateBy(User createBy) {
  this.createBy = createBy;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public User getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(User updateBy) {
  this.updateBy = updateBy;
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
  if (!(object instanceof BnkPaymentRun)) {
   return false;
  }
  BnkPaymentRun other = (BnkPaymentRun) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.tr.bank.BnkPaymentRun[ id=" + id + " ]";
 }
 
}
