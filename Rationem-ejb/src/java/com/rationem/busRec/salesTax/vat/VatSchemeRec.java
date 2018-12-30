/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 * A VAT registration will be for a particular VAT scheme
 * The supported schemes are: Invoice or Cash Accounting
 * @author Chris
 */
public class VatSchemeRec {
 private Long id;
 private String ref;
 private String name; // short description
 private String description;
 private boolean cashAccounting;  // is VAT charged on invoice
 private boolean flatRate;   // use flat rate
 private boolean annualAccounting;
 private char paymentFrequency;
 private String prCode;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public VatSchemeRec() {
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

 public boolean isCashAccounting() {
  return cashAccounting;
 }

 public void setCashAccounting(boolean cashAccounting) {
  this.cashAccounting = cashAccounting;
 }

 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getPrCode() {
  return prCode;
 }

 public char getPaymentFrequency() {
  return paymentFrequency;
 }

 public void setPaymentFrequency(char paymentFrequency) {
  this.paymentFrequency = paymentFrequency;
 }

 public void setPrCode(String prCode) {
  this.prCode = prCode;
 }

 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
 }

 public boolean isFlatRate() {
  return flatRate;
 }

 public void setFlatRate(boolean flatRate) {
  this.flatRate = flatRate;
 }

 

 public boolean isAnnualAccounting() {
  return annualAccounting;
 }

 public void setAnnualAccounting(boolean annualAccounting) {
  this.annualAccounting = annualAccounting;
 }
 
 
}
