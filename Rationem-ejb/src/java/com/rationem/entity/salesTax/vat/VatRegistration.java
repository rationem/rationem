/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import com.rationem.entity.audit.AuditVatRegistration;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.NamedQuery;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.CascadeType.REMOVE;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.PrivateOwned;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config09")
@NamedQuery(name="vatRegForComp", query="select v from VatRegistration v where v.comp = :company")
@SequenceGenerator(name = "vatRegistration_s1", sequenceName = "fi_config09_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class VatRegistration implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatRegistration_s1")
 @Column(name="vat_reg_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 @OneToOne(mappedBy = "vatRegCurrent")
 private CompanyBasic comp; 
 @Column(name="vat_num")
 private String vatNumber;
 @Temporal(DATE)
 @Column(name="registered_on")
 private Date registrationDate;
 
 @ManyToOne
 @JoinColumn(name="vat_scheme_id", referencedColumnName="vat_scheme_id")
 private VatScheme scheme; // scheme the registration is for
 
 @Temporal(DATE)
 @Column(name="dereg_on")
 private Date registrationEnd;
 @Column(name="vat_office_name")
 private String vatOffice;
 @ManyToOne
 @JoinColumn(name="vat_office_address_id", referencedColumnName="address_id")
 private Address vatOfficeAddress;
 @ManyToOne
 @JoinColumn(name="vat_inspector_ptnr_id", referencedColumnName="partner_id")
 private PartnerPerson inspector;
 
 @OneToMany(mappedBy = "vatRegistration", orphanRemoval=true,cascade=REMOVE)
 @CascadeOnDelete 
 @PrivateOwned
 private List<AuditVatRegistration> auditRecords;
 
 
 @OneToMany(mappedBy = "vatReg")
 private List<VatRegScheme> vatRegSchemes;
 @Column(name="active_flag")
 private boolean activeReg;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="CHANGED_BY_ID", referencedColumnName="partner_id")
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

 public boolean isActiveReg() {
  return activeReg;
 }

 public void setActiveReg(boolean activeReg) {
  this.activeReg = activeReg;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public String getVatNumber() {
  return vatNumber;
 }

 public void setVatNumber(String vatNumber) {
  this.vatNumber = vatNumber;
 }

 public Date getRegistrationDate() {
  return registrationDate;
 }

 public void setRegistrationDate(Date registrationDate) {
  this.registrationDate = registrationDate;
 }

 public Date getRegistrationEnd() {
  return registrationEnd;
 }

 public void setRegistrationEnd(Date registrationEnd) {
  this.registrationEnd = registrationEnd;
 }

 public VatScheme getScheme() {
  return scheme;
 }

 public void setScheme(VatScheme scheme) {
  this.scheme = scheme;
 }

 
 public String getVatOffice() {
  return vatOffice;
 }

 public void setVatOffice(String vatOffice) {
  this.vatOffice = vatOffice;
 }

 public Address getVatOfficeAddress() {
  return vatOfficeAddress;
 }

 public void setVatOfficeAddress(Address vatOfficeAddress) {
  this.vatOfficeAddress = vatOfficeAddress;
 }

 public PartnerPerson getInspector() {
  return inspector;
 }

 public void setInspector(PartnerPerson inspector) {
  this.inspector = inspector;
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

 public List<AuditVatRegistration> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditVatRegistration> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public List<VatRegScheme> getVatRegSchemes() {
  return vatRegSchemes;
 }

 public void setVatRegSchemes(List<VatRegScheme> vatRegSchemes) {
  this.vatRegSchemes = vatRegSchemes;
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
  if (!(object instanceof VatRegistration)) {
   return false;
  }
  VatRegistration other = (VatRegistration) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatRegistration[ id=" + id + " ]";
 }
 
}
