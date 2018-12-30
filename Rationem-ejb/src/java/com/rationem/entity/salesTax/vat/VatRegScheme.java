/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

//import com.rationem.entity.audit.AuditVatRegScheme;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
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
@Table(name="fi_config10")
@NamedQuery(name="vatRegSchemeForReg", query="select v from VatRegScheme v where v.vatReg = :reg")
@SequenceGenerator(name = "vatRegScheme_s1", sequenceName = "fi_config10_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class VatRegScheme implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatLineType_s1")
 @Column(name="vat_reg_Scheme_id")
 private Long id;
 /*
 @OneToMany(mappedBy = "vatRegScheme")
 private List<AuditVatRegScheme> auditRecords;
 */
 @Column(name="ref")
 private String reference;
 @Column(name="description")
 private String descr;
 @ManyToOne
 @JoinColumn(name="vat_reg_id", referencedColumnName="vat_reg_id")
 private VatRegistration vatReg;
 
 @ManyToOne
 @JoinColumn(name="vat_flat_rate_id", referencedColumnName="vat_industry_rate_id")
 private VatIndustryFlatRate vatIndustry;
 
 @Column(name="active")
 private boolean active;  // is this the current one
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="value_to")
 private Date validTo;
 @Column(name="flat_rate")
 private boolean flatRate;
 @Column(name="curr_flat_rate")
 double activeFlatRate;
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
 
 @ManyToOne
 @JoinColumn(name="VAT_SCHEME_ID", referencedColumnName="vat_scheme_id")
 private VatScheme vatScheme;
 /*
 @ManyToMany(mappedBy = "vatRegSchemes")
 private List<VatScheme> vatSchemes; 
 */
 @OneToMany(mappedBy = "vatRegScheme")
 private List<VatReturn> vatReturns;
 


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

 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

 public VatRegistration getVatReg() {
  return vatReg;
 }

 public void setVatReg(VatRegistration vatReg) {
  this.vatReg = vatReg;
 }

 public VatIndustryFlatRate getVatIndustry() {
  return vatIndustry;
 }

 public void setVatIndustry(VatIndustryFlatRate vatIndustry) {
  this.vatIndustry = vatIndustry;
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

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
 }

 public boolean isFlatRate() {
  return flatRate;
 }

 public void setFlatRate(boolean flatRate) {
  this.flatRate = flatRate;
 }

 public double getActiveFlatRate() {
  return activeFlatRate;
 }

 public void setActiveFlatRate(double activeFlatRate) {
  this.activeFlatRate = activeFlatRate;
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

 public VatScheme getVatScheme() {
  return vatScheme;
 }

 public void setVatScheme(VatScheme vatScheme) {
  this.vatScheme = vatScheme;
 }
/*
 public List<VatScheme> getVatSchemes() {
  return vatSchemes;
 }

 public void setVatSchemes(List<VatScheme> vatSchemes) {
  this.vatSchemes = vatSchemes;
 }
*/
 public List<VatReturn> getVatReturns() {
  return vatReturns;
 }

 public void setVatReturns(List<VatReturn> vatReturns) {
  this.vatReturns = vatReturns;
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
  if (!(object instanceof VatRegScheme)) {
   return false;
  }
  VatRegScheme other = (VatRegScheme) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatRegScheme[ id=" + id + " ]";
 }
 
}
