/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Chris
 */
public class VatFlatRateIndRateRec {
 
 private Long id;
 private String ref;
 private String descr;
 private double rate;
 private Date validFrom;
 private Date validTo;
 private String industry;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public VatFlatRateIndRateRec() {
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

 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getIndustry() {
  return industry;
 }

 public void setIndustry(String industry) {
  this.industry = industry;
 }

 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
 }

 

 public double getRate() {
  return rate;
 }

 public void setRate(double rate) {
  this.rate = rate;
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
   validTo = new Date();
   GregorianCalendar cal = new GregorianCalendar();
   cal.set(9999, 11, 31);
   validTo = cal.getTime();
  }
  return validTo;
  
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
 }

 
 
}
