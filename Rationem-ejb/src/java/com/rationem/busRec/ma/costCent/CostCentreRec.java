/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.ma.costCent;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Cost centre master record
 * @author Chris
 */
public class CostCentreRec implements Serializable {
 
 private Long id;
 private String refrence;
 private String costCentreName;
 private CompanyBasicRec costCentreForCompany;
 private List<CostAccountDirectRec> costAccountDirectAcs;
 private PartnerPersonRec responsibilityOf;
 private Date validFrom;
 private Date validTo;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;

 public CostCentreRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getRefrence() {
  return refrence;
 }

 public void setRefrence(String refrence) {
  this.refrence = refrence;
 }

 public String getCostCentreName() {
  return costCentreName;
 }

 public void setCostCentreName(String costCentreName) {
  this.costCentreName = costCentreName;
 }

 public CompanyBasicRec getCostCentreForCompany() {
  return costCentreForCompany;
 }

 public List<CostAccountDirectRec> getCostAccountDirectAcs() {
  return costAccountDirectAcs;
 }

 public void setCostAccountDirectAcs(List<CostAccountDirectRec> costAccountDirectAcs) {
  this.costAccountDirectAcs = costAccountDirectAcs;
 }

 
 public void setCostCentreForCompany(CompanyBasicRec costCentreForCompany) {
  this.costCentreForCompany = costCentreForCompany;
 }

 public PartnerPersonRec getResponsibilityOf() {
  return responsibilityOf;
 }

 public void setResponsibilityOf(PartnerPersonRec responsibilityOf) {
  this.responsibilityOf = responsibilityOf;
 }

 public Date getValidFrom() {
  if(validFrom == null){
   validFrom = new Date();
  }
  return validFrom;
 }

 public void setValidFrom(Date validFrom) {
  this.validFrom = validFrom;
 }

 public Date getValidTo() {
  if(validTo == null){
   GregorianCalendar cal = new GregorianCalendar();
   cal.set(9999, 11, 31);
   validTo = cal.getTime();
  }
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
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
 
 
}
