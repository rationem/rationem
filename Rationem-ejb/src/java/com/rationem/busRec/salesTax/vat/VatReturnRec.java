/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 * Summary information of a VAT return
 * @author Chris
 */
public class VatReturnRec {
 private Long id;
 private VatRegSchemeRec vatRegScheme;
 private String returnRef;
 private boolean provisionReturn;
 private Date returnDate;
 private Date paymentDueDate;
 /**
  * VAT due on sales on domestic sale
  */
 private double box1Value;
 /**
  * VAT due on sales other EU countries
  */
 private double box2Value;
 /**
  * Total VAT due box1 + box2
  */
 private double box3Value;
 /**
  * VAT reclaimable on purchases
  */
 private double box4Value;
 /**
  * VAT payable or reclaimable
  */
 private double box5Value;
 /**
  * Total sales amount excluding VAT 
  * Sales in box 1 + 
  * Zero rate / exempt
  * Box 8 sales to other EU countries
  * Goods supplied subject to reverse charge
  * Goods purchased subject to reverse charge
  */
 private double box6Value;
 /**
  * Total purchases excluding VAT
  * Purchases subject to reverse charge
  */
 private double box7Value;
 /**
  * Goods supplied to other EU countries
  */
 private double box8Value;
 private List<VatReturnLineRec> vatReturnLines;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public VatReturnRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public VatRegSchemeRec getVatRegScheme() {
  return vatRegScheme;
 }

 public void setVatRegScheme(VatRegSchemeRec vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }

 public String getReturnRef() {
  return returnRef;
 }

 public void setReturnRef(String returnRef) {
  this.returnRef = returnRef;
 }

 public Date getPaymentDueDate() {
  return paymentDueDate;
 }

 public void setPaymentDueDate(Date paymentDueDate) {
  this.paymentDueDate = paymentDueDate;
 }

 public boolean isProvisionReturn() {
  return provisionReturn;
 }

 public void setProvisionReturn(boolean provisionReturn) {
  this.provisionReturn = provisionReturn;
 }

 public Date getReturnDate() {
  return returnDate;
 }

 public void setReturnDate(Date returnDate) {
  this.returnDate = returnDate;
 }

 public double getBox1Value() {
  return box1Value;
 }

 public void setBox1Value(double box1Value) {
  this.box1Value = box1Value;
 }

 public double getBox2Value() {
  return box2Value;
 }

 public void setBox2Value(double box2Value) {
  this.box2Value = box2Value;
 }

 public double getBox3Value() {
  return box3Value;
 }

 public void setBox3Value(double box3Value) {
  this.box3Value = box3Value;
 }

 public double getBox4Value() {
  return box4Value;
 }

 public void setBox4Value(double box4Value) {
  this.box4Value = box4Value;
 }

 public double getBox5Value() {
  return box5Value;
 }

 public void setBox5Value(double box5Value) {
  this.box5Value = box5Value;
 }

 public double getBox6Value() {
  return box6Value;
 }

 public void setBox6Value(double box6Value) {
  this.box6Value = box6Value;
 }

 public double getBox7Value() {
  return box7Value;
 }

 public void setBox7Value(double box7Value) {
  this.box7Value = box7Value;
 }

 public double getBox8Value() {
  return box8Value;
 }

 public void setBox8Value(double box8Value) {
  this.box8Value = box8Value;
 }

 public List<VatReturnLineRec> getVatReturnLines() {
  return vatReturnLines;
 }

 public void setVatReturnLines(List<VatReturnLineRec> vatReturnLines) {
  this.vatReturnLines = vatReturnLines;
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
  hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
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
  final VatReturnRec other = (VatReturnRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }
  
 
 
}
