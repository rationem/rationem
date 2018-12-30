/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.arap;

import com.rationem.entity.audit.AuditPaymentTerms;
import com.rationem.entity.config.common.Uom;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
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
@Table(name="bac_config10")
@NamedQuery(name = "allPaymentTerms",query = "SELECT p FROM PaymentTerms p order by p.payTermsCode")
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "PaymentTerms_s1", sequenceName = "bac_config10_s1", allocationSize = 1,initialValue=1)
public class PaymentTerms implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "PaymentTerms_s1")
 @Column(name="pay_terms_id")
 private Long id;
 @Column(name="terms_code")
 private String payTermsCode;
  /**
   * This is one of docDT document date, postDT posting date, Day of following month
   */
 @Column(name="base_type")
 private String baseType;
 @Column(name="description")
 private String description;
 @Column(name="days")
 private int days;
 @Column(name="DAY_OF_MONTH")
 private int dayOfMonth;
  
 @ManyToOne
 @JoinColumn(name="unit_of_measure_id", referencedColumnName="uom_id")
 private Uom uom;

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

 
 @OneToMany(mappedBy = "payTerms")
 private List<DocLineAr> aRDocLines;
 @OneToMany(mappedBy = "payTerms")
 private List<DocLineAp> apDocLines;
 @OneToMany(mappedBy = "terms")
 private List<AuditPaymentTerms> auditRecords;

 public PaymentTerms() {
 }

 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditPaymentTerms> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditPaymentTerms> auditRecords) {
  this.auditRecords = auditRecords;
 }

 
 public List<DocLineAr> getaRDocLines() {
  return aRDocLines;
 }

 public void setaRDocLines(List<DocLineAr> aRDocLines) {
  this.aRDocLines = aRDocLines;
 }

 public List<DocLineAp> getApDocLines() {
  return apDocLines;
 }

 public void setApDocLines(List<DocLineAp> apDocLines) {
  this.apDocLines = apDocLines;
 }

 
 public String getPayTermsCode() {
  return payTermsCode;
 }

 public void setPayTermsCode(String payTermsCode) {
  this.payTermsCode = payTermsCode;
 }

 public String getBaseType() {
  return baseType;
 }

 public void setBaseType(String baseType) {
  this.baseType = baseType;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public int getDays() {
  return days;
 }

 public void setDays(int days) {
  this.days = days;
 }

 public int getDayOfMonth() {
  return dayOfMonth;
 }

 public void setDayOfMonth(int dayOfMonth) {
  this.dayOfMonth = dayOfMonth;
 }

 public Uom getUom() {
  return uom;
 }

 public void setUom(Uom uom) {
  this.uom = uom;
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
  if (!(object instanceof PaymentTerms)) {
   return false;
  }
  PaymentTerms other = (PaymentTerms) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.arap.PaymentTerms[ id=" + id + " ]";
 }
 
}
