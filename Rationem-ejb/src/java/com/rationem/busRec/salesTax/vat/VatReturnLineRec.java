/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.doc.DocBaseRec;
import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 * Line in a VAT return
 * mapping of VAT totals to GL accounts and for VAT reporting
 * Transaction objects so not explicitly created by user
 * @author Chris
 */
public class VatReturnLineRec {
 private Long id;
 private VatReturnRec vatReturn;
 private String vatTransType;
 private VatCodeRec vatCode;
 private double goodsValue;
 private double vatValue;
 private double irrecoverableValue;
 private DocLineBaseRec fiDocLine;
 private ArAccountRec arAccount;
 private Long apAccountRec;
 private DocInvoiceArRec arInvoice;
 private DocBaseRec fiDoc;
 private boolean vatPaid;
 private FiGlAccountCompRec vatGlAccount;
 private FiGlAccountCompRec chargeGlAccount;
 private boolean docPaid;
 private Date paidDate;
 private boolean writeOff;
 private DocLineGlRec expenseDocLine;
 private VatReturnLineRec clearedByLine;
 private VatReturnLineRec clearingForLine;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public VatReturnLineRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public VatReturnRec getVatReturn() {
  return vatReturn;
 }

 public void setVatReturn(VatReturnRec vatReturn) {
  this.vatReturn = vatReturn;
 }

 public String getVatTransType() {
  return vatTransType;
 }

 public void setVatTransType(String vatTransType) {
  this.vatTransType = vatTransType;
 }

 public double getGoodsValue() {
  return goodsValue;
 }

 public void setGoodsValue(double goodsValue) {
  this.goodsValue = goodsValue;
 }

 public double getVatValue() {
  return vatValue;
 }

 public void setVatValue(double vatValue) {
  this.vatValue = vatValue;
 }

 public double getIrrecoverableValue() {
  return irrecoverableValue;
 }

 public void setIrrecoverableValue(double irrecoverableValue) {
  this.irrecoverableValue = irrecoverableValue;
 }

 public DocLineBaseRec getFiDocLine() {
  return fiDocLine;
 }

 public void setFiDocLine(DocLineBaseRec fiDocLine) {
  this.fiDocLine = fiDocLine;
 }

 public ArAccountRec getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccountRec arAccount) {
  this.arAccount = arAccount;
 }

 public Long getApAccountRec() {
  return apAccountRec;
 }

 public void setApAccountRec(Long apAccountRec) {
  this.apAccountRec = apAccountRec;
 }

 public DocInvoiceArRec getArInvoice() {
  return arInvoice;
 }

 public void setArInvoice(DocInvoiceArRec arInvoice) {
  this.arInvoice = arInvoice;
 }

 public DocBaseRec getFiDoc() {
  return fiDoc;
 }

 public void setFiDoc(DocBaseRec fiDoc) {
  this.fiDoc = fiDoc;
 }

 public VatCodeRec getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeRec vatCode) {
  this.vatCode = vatCode;
 }

 

 public boolean isVatPaid() {
  return vatPaid;
 }

 public void setVatPaid(boolean vatPaid) {
  this.vatPaid = vatPaid;
 }
 

 public FiGlAccountCompRec getVatGlAccount() {
  return vatGlAccount;
 }

 public void setVatGlAccount(FiGlAccountCompRec vatGlAccount) {
  this.vatGlAccount = vatGlAccount;
 }

 public FiGlAccountCompRec getChargeGlAccount() {
  return chargeGlAccount;
 }

 public void setChargeGlAccount(FiGlAccountCompRec chargeGlAccount) {
  this.chargeGlAccount = chargeGlAccount;
 }

 public boolean isDocPaid() {
  return docPaid;
 }

 public void setDocPaid(boolean docPaid) {
  this.docPaid = docPaid;
 }

 public DocLineGlRec getExpenseDocLine() {
  return expenseDocLine;
 }

 public void setExpenseDocLine(DocLineGlRec expenseDocLine) {
  this.expenseDocLine = expenseDocLine;
 }

 public Date getPaidDate() {
  return paidDate;
 }

 public void setPaidDate(Date paidDate) {
  this.paidDate = paidDate;
 }

 public boolean isWriteOff() {
  return writeOff;
 }

 public void setWriteOff(boolean writeOff) {
  this.writeOff = writeOff;
 }

 public VatReturnLineRec getClearedByLine() {
  return clearedByLine;
 }

 public void setClearedByLine(VatReturnLineRec clearedByLine) {
  this.clearedByLine = clearedByLine;
 }

 public VatReturnLineRec getClearingForLine() {
  return clearingForLine;
 }

 public void setClearingForLine(VatReturnLineRec clearingForLine) {
  this.clearingForLine = clearingForLine;
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
 
 
 
 
}
