/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import com.rationem.entity.document.DocLineGl;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Column;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_config06")
@NamedQueries({
@NamedQuery(name="vatCompCodesForComp", query="select v from VatCodeCompany v where v.company.id = :compId"),
@NamedQuery(name="vatCompCodesAll", query="select v from VatCodeCompany v ")
})
@SequenceGenerator(name = "vatCodeComp_s1", sequenceName = "fi_config06_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class VatCodeCompany implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatCode_s1")
 @Column(name="vat_code_comp_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="vat_code_id", referencedColumnName="vat_code_id")
 private VatCode vatCode;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic company;
 @ManyToOne
 @JoinColumn(name="vat_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 @OneToOne(mappedBy = "vatCode")
 private FiGlAccountComp vatGlAccount;
 @Column(name="irrecoverable_rate")
 private double irrecoverRate;
 @Column(name="charge_central_gl_account")
 private boolean chargeSingleGl;
 @Column(name="no_irrecoverabe_line")
 private boolean noIrrecoverableLine;
 @ManyToOne
 @JoinColumn(name="charge_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp chargeGlAccount;
 @ManyToOne
 @JoinColumn(name="provn_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp provnGlAccount;
 
 @OneToMany(mappedBy = "vatCode")
 private List<DocLineGl> glDocLines;
 /*
 @OneToMany(mappedBy = "vatCodeComp")
 private List<AuditVatCodeComp> auditRecords;
 */
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

 public VatCode getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCode vatCode) {
  this.vatCode = vatCode;
 }

 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public FiGlAccountComp getVatGlAccount() {
  return vatGlAccount;
 }

 public void setVatGlAccount(FiGlAccountComp vatGlAccount) {
  this.vatGlAccount = vatGlAccount;
 }

 public double getIrrecoverRate() {
  return irrecoverRate;
 }

 public void setIrrecoverRate(double irrecoverRate) {
  this.irrecoverRate = irrecoverRate;
 }

 public boolean isChargeSingleGl() {
  return chargeSingleGl;
 }

 public void setChargeSingleGl(boolean chargeSingleGl) {
  this.chargeSingleGl = chargeSingleGl;
 }

 public boolean isNoIrrecoverableLine() {
  return noIrrecoverableLine;
 }

 public void setNoIrrecoverableLine(boolean noIrrecoverableLine) {
  this.noIrrecoverableLine = noIrrecoverableLine;
 }

 public FiGlAccountComp getChargeGlAccount() {
  return chargeGlAccount;
 }

 public void setChargeGlAccount(FiGlAccountComp chargeGlAccount) {
  this.chargeGlAccount = chargeGlAccount;
 }

 public FiGlAccountComp getProvnGlAccount() {
  return provnGlAccount;
 }

 public void setProvnGlAccount(FiGlAccountComp provnGlAccount) {
  this.provnGlAccount = provnGlAccount;
 }

 public List<DocLineGl> getGlDocLines() {
  return glDocLines;
 }

 public void setGlDocLines(List<DocLineGl> glDocLines) {
  this.glDocLines = glDocLines;
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
  if (!(object instanceof VatCodeCompany)) {
   return false;
  }
  VatCodeCompany other = (VatCodeCompany) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatCodeCompany[ id=" + id + " ]";
 }
 
}
