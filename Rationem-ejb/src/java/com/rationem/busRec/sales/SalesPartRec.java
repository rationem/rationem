/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.sales;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class SalesPartRec implements Serializable{
 private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.sales.SalesPartRec");
 
 
 private Long id;
 private String partCode;
 private String shortDescription;
 private String externalDescription;
 private boolean physicalPart;
 private List<SalesPartCompanyRec> salesPartCompanies;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private long changes;

 public SalesPartRec() {
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

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
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

 public String getExternalDescription() {
  return externalDescription;
 }

 public void setExternalDescription(String externalDescription) {
  this.externalDescription = externalDescription;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getPartCode() {
  return partCode;
 }

 public void setPartCode(String partCode) {
  this.partCode = partCode;
 }

 public boolean isPhysicalPart() {
  return physicalPart;
 }

 public void setPhysicalPart(boolean physicalPart) {
  this.physicalPart = physicalPart;
 }

 public List<SalesPartCompanyRec> getSalesPartCompanies() {
 if(salesPartCompanies == null){
   salesPartCompanies = new ArrayList<>();
  }
  return salesPartCompanies;
 }

 public void setSalesPartCompany(List<SalesPartCompanyRec> partPompanies) {
  this.salesPartCompanies = partPompanies;
 }

 public String getShortDescription() {
  return shortDescription;
 }

 public void setShortDescription(String shortDescription) {
  this.shortDescription = shortDescription;
 }

 
}
