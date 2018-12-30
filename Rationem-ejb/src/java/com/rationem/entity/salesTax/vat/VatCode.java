/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Collection;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.Temporal;

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
@Table(name="fi_config05")
@NamedQueries({
@NamedQuery(name="allVatCodes", query="select cd from VatCode cd order by cd.code"),
@NamedQuery(name="vatCodeByCode", query="select v from VatCode v where v.code = :vatCode"),
@NamedQuery(name="vatCodesInput", query="select v from VatCode v where v.inputTax = true")//,
//@NamedQuery(name="vatCodesOutput", query="select v from VatCode v where v.inputTax = false")
})
@SequenceGenerator(name = "vatCode_s1", sequenceName = "fi_config05_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class VatCode implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatCode_s1")
 @Column(name="vat_code_id")
 private Long id;
 @Column(name="code")
 private String code;
 @Column(name="descr")
 private String description;
 @Column(name="rate")
 private double rate;
 @Column(name="irrecoverable_rate")
 private double irrrecoverableRate;
 @Column(name="input_tax")
 private boolean inputTax;
 @Column(name="inter_state_supply")
 private boolean interStatSupply;
 @Column(name="import_export")
 private char importExport;
 @Column(name="tax_type")
 /**
  * Tax Type V= standard, E = Exempt, O = outside scope
  */
 private char taxType; 
 
 @OneToMany(mappedBy = "vatCode")
 private List<VatReturnLine> vatReturnLines;
 
 @Column(name="vat_rule")
 private boolean vatRule;
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="valid_to")
 private Date validTo;
 
 @OneToMany(mappedBy = "vatCode")
 private List<VatCodeCompany> vatCodeComps;
 
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


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public double getRate() {
  return rate;
 }

 public void setRate(double rate) {
  this.rate = rate;
 }

 public double getIrrrecoverableRate() {
  return irrrecoverableRate;
 }

 public void setIrrrecoverableRate(double irrrecoverableRate) {
  this.irrrecoverableRate = irrrecoverableRate;
 }

 public boolean isInputTax() {
  return inputTax;
 }

 public void setInputTax(boolean inputTax) {
  this.inputTax = inputTax;
 }

 public boolean isInterStatSupply() {
  return interStatSupply;
 }

 public void setInterStatSupply(boolean interStatSupply) {
  this.interStatSupply = interStatSupply;
 }

 public char getImportExport() {
  return importExport;
 }

 public void setImportExport(char importExport) {
  this.importExport = importExport;
 }

 public char getTaxType() {
  return taxType;
 }

 public void setTaxType(char taxType) {
  this.taxType = taxType;
 }

 public List<VatReturnLine> getVatReturnLines() {
  return vatReturnLines;
 }

 public void setVatReturnLines(List<VatReturnLine> vatReturnLines) {
  this.vatReturnLines = vatReturnLines;
 }

 public boolean isVatRule() {
  return vatRule;
 }

 public void setVatRule(boolean vatRule) {
  this.vatRule = vatRule;
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

 public List<VatCodeCompany> getVatCodeComps() {
  return vatCodeComps;
 }

 public void setVatCodeComps(List<VatCodeCompany> vatCodeComps) {
  this.vatCodeComps = vatCodeComps;
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
  if (!(object instanceof VatCode)) {
   return false;
  }
  VatCode other = (VatCode) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatCode[ id=" + id + " ]";
 }
 
}
