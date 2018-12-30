/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.ma.programme;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class ProgrammeRec implements Serializable {
 private Long id;
 private String reference;
 private String name;
 private CompanyBasicRec programmeForCompany;
 private double cost;
 private double budget;
 private PartnerPersonRec responsibilityOf;
 private Date validFrom;
 private Date validTo;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public ProgrammeRec() {
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

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public CompanyBasicRec getProgrammeForCompany() {
  return programmeForCompany;
 }

 public void setProgrammeForCompany(CompanyBasicRec programmeForCompany) {
  this.programmeForCompany = programmeForCompany;
 }

 public double getCost() {
  return cost;
 }

 public void setCost(double cost) {
  this.cost = cost;
 }

 public double getBudget() {
  return budget;
 }

 public void setBudget(double budget) {
  this.budget = budget;
 }

 public PartnerPersonRec getResponsibilityOf() {
  return responsibilityOf;
 }

 public void setResponsibilityOf(PartnerPersonRec responsibilityOf) {
  this.responsibilityOf = responsibilityOf;
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

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final ProgrammeRec other = (ProgrammeRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }
 
 
}
