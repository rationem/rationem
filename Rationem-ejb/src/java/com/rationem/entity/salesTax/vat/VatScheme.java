/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocInvoiceAr;
import com.rationem.entity.document.DocLineGl;
//import com.rationem.entity.fi.arap.ArAccount;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.Temporal;
import java.util.Collection;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config08")
@NamedQuery(name="allVatSchemes", query="select s from VatScheme s")
@SequenceGenerator(name = "vatScheme_s1", sequenceName = "fi_config08_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class VatScheme implements Serializable {
 private static final long serialVersionUID = 1L;
 

 @Id
 @GeneratedValue(generator = "vatScheme_s1")
 @Column(name="vat_scheme_id")
 private Long id;
 @Column(name="ref")
 private String ref;
 @Column(name="name")
 private String name; // short description
 @Column(name="description")
 private String description;
 @Column(name="cash_accounting")
 private boolean cashAccounting;  // is VAT charged on invoice
 @Column(name="flat_rate")
 private boolean flatRate;   // use flat rate
 @Column(name="annual_accounting")
 private boolean annualAccounting;
 /**
  * Payment frequency M = monthly, Q= quarterly
  */
 @Column(name="pay_freq")
 private char paymentFrequency;
 
 @Column(name="process_code")
 private String prCode;
 
 
 /*@ManyToMany
 @JoinTable(
   name="FI_CONFIG11",
   joinColumns=
     @JoinColumn(name="VAT_SCHEME_ID", referencedColumnName="ID"),
   inverseJoinColumns=
     @JoinColumn(name="VAT_REG_SCHEME_ID", referencedColumnName="ID")
 )*/
 @OneToMany(mappedBy = "vatScheme")
 private List<VatRegScheme> vatRegSchemes;
 @OneToMany(mappedBy = "scheme")
 private List<VatRegistration> vatRegistrations;
 
 
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
/*
 @OneToMany(mappedBy = "vatScheme")
 private List<AuditVatScheme> auditVatSchemes;
*/
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isCashAccounting() {
  return cashAccounting;
 }

 public void setCashAccounting(boolean cashAccounting) {
  this.cashAccounting = cashAccounting;
 }

 public boolean isFlatRate() {
  return flatRate;
 }

 public void setFlatRate(boolean flatRate) {
  this.flatRate = flatRate;
 }

 public boolean isAnnualAccounting() {
  return annualAccounting;
 }

 public void setAnnualAccounting(boolean annualAccounting) {
  this.annualAccounting = annualAccounting;
 }

 public char getPaymentFrequency() {
  return paymentFrequency;
 }

 public void setPaymentFrequency(char paymentFrequency) {
  this.paymentFrequency = paymentFrequency;
 }

 public String getPrCode() {
  return prCode;
 }

 public void setPrCode(String prCode) {
  this.prCode = prCode;
 }

 public List<VatRegScheme> getVatRegSchemes() {
  return vatRegSchemes;
 }

 public void setVatRegSchemes(List<VatRegScheme> vatRegSchemes) {
  this.vatRegSchemes = vatRegSchemes;
 }

 public List<VatRegistration> getVatRegistrations() {
  return vatRegistrations;
 }

 public void setVatRegistrations(List<VatRegistration> vatRegistrations) {
  this.vatRegistrations = vatRegistrations;
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
  if (!(object instanceof VatScheme)) {
   return false;
  }
  VatScheme other = (VatScheme) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatScheme[ id=" + id + " ]";
 }
 
}
