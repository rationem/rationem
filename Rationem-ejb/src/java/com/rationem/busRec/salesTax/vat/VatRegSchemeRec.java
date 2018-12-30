/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 * The scheme used by a registration along with the validity period
 * VAT returns attached to the Registration-Scheme
 * @author Chris
 */
public class VatRegSchemeRec {
 private Long id;
 //private List<AuditVatRegScheme> auditRecords;
 
 private String ref;
 private String description;
 private VatRegistrationRec vatReg;
 private boolean active;  // is this the current one
 private Date validFrom;
 private Date validTo;
 private boolean flatRate;
 private VatFlatRateIndRateRec flatRateIndustry;
 double activeFlatRate;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;
 private VatSchemeRec vatScheme;
 //private List<VatSchemeRec> vatSchemes;
 private List<VatReturnRec> vatReturns;
 
 public boolean isActive() {
  return active;
 }

 public void setActive(boolean active) {
  this.active = active;
 }

 public double getActiveFlatRate() {
  return activeFlatRate;
 }

 public void setActiveFlatRate(double activeFlatRate) {
  this.activeFlatRate = activeFlatRate;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
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

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public boolean isFlatRate() {
  return flatRate;
 }

 public void setFlatRate(boolean flatRate) {
  this.flatRate = flatRate;
 }

 public VatFlatRateIndRateRec getFlatRateIndustry() {
  return flatRateIndustry;
 }

 public void setFlatRateIndustry(VatFlatRateIndRateRec flatRateIndustry) {
  this.flatRateIndustry = flatRateIndustry;
 }

 
 
 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
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

 public VatRegistrationRec getVatReg() {
  return vatReg;
 }

 public void setVatReg(VatRegistrationRec vatReg) {
  this.vatReg = vatReg;
 }

 
 

 
 public List<VatReturnRec> getVatReturns() {
  return vatReturns;
 }

 public void setVatReturns(List<VatReturnRec> vatReturns) {
  this.vatReturns = vatReturns;
 }
 
/*
 public List<VatSchemeRec> getVatSchemes() {
  return vatSchemes;
 }

 public void setVatSchemes(List<VatSchemeRec> vatSchemes) {
  this.vatSchemes = vatSchemes;
 }
 */

 public VatSchemeRec getVatScheme() {
  return vatScheme;
 }

 public void setVatScheme(VatSchemeRec vatScheme) {
  this.vatScheme = vatScheme;
 }
 
 
}
