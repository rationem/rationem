/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Each company may be registered for VAT.
 * This 
 * @author Chris
 */
public class VatRegistrationRec {
 private Long id;
 /**
  * Company the registration is for 
  */
 private CompanyBasicRec comp; 
 private String vatNumber;
 private Date registrationDate;
 private VatSchemeRec scheme; // scheme the registration is for
 private Date registrationEnd; 
 private String vatOffice;
 private AddressRec vatOfficeAddress;
 private PartnerPersonRec inspector;
 private boolean activeReg;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;
 private List<VatRegSchemeRec> regSchemes;

 public VatRegistrationRec() {
 }

 public boolean isActiveReg() {
  return activeReg;
 }

 public void setActiveReg(boolean activeReg) {
  this.activeReg = activeReg;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
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

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public PartnerPersonRec getInspector() {
  if(inspector == null){
   inspector = new PartnerPersonRec(); 
  }
  return inspector;
 }

 public void setInspector(PartnerPersonRec inspector) {
  this.inspector = inspector;
 }

 

 public Date getRegistrationDate() {
  return registrationDate;
 }

 public void setRegistrationDate(Date registrationDate) {
  this.registrationDate = registrationDate;
 }

 public Date getRegistrationEnd() {
  if(registrationEnd == null){
   GregorianCalendar cal = new GregorianCalendar();
   cal.set(9999, 11, 31);
   registrationEnd = cal.getTime();
  }
  return registrationEnd;
 }

 public void setRegistrationEnd(Date registrationEnd) {
  this.registrationEnd = registrationEnd;
 }

 public List<VatRegSchemeRec> getRegSchemes() {
  return regSchemes;
 }

 public void setRegSchemes(List<VatRegSchemeRec> regSchemes) {
  this.regSchemes = regSchemes;
 }

 
 public VatSchemeRec getScheme() {
  return scheme;
 }

 public void setScheme(VatSchemeRec scheme) {
  this.scheme = scheme;
 }

 public String getVatNumber() {
  return vatNumber;
 }

 public void setVatNumber(String vatNumber) {
  this.vatNumber = vatNumber;
 }

 public String getVatOffice() {
  return vatOffice;
 }

 public void setVatOffice(String vatOffice) {
  this.vatOffice = vatOffice;
 }

 public AddressRec getVatOfficeAddress() {
  if(vatOfficeAddress == null){
   vatOfficeAddress = new AddressRec();
  }
  return vatOfficeAddress;
 }

 public void setVatOfficeAddress(AddressRec vatOfficeAddress) {
  this.vatOfficeAddress = vatOfficeAddress;
 }
 
 
 
}
