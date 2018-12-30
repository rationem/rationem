/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.arap.FiApPeriodBalanceRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class DocLineApRec extends DocLineBaseRec  {
 private int currID;
 
 private PaymentTermsRec payTerms;
 private PaymentTypeRec payType;
 private Date dueDate;
 private ApAccountRec apAccount;
 private DocFiRec docFi;
 private ArBankAccountRec paymntBank;
 private List<DocLineGlRec> reconiliationLines;
 private FiApPeriodBalanceRec apCreditPeriodBalance;
 private FiApPeriodBalanceRec apDebitPeriodBalance;
 private boolean giftAid;
 private DocBankLineBaseRec bankPayRunLine;
 private PartnerPersonRec orderedBy;
 private String orderReference;
 private byte[] orderFileData;
 private String oderFileName;
 private String oderFileType;
 private byte[] supplierDocFileContents;
 private String supplierDocFileName;
 private String supplierDocFileType;
 private double unpaidAmount;
 // used for display only
 private String ptnrRef;
 private String docTypeName;

 public DocLineApRec() {
 }


 @Override
  public String getAccountRef(){
  String acNum = apAccount.getAccountCode();
  return acNum;
 }
 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public PartnerPersonRec getOrderedBy() {
  return orderedBy;
 }

 public void setOrderedBy(PartnerPersonRec orderedBy) {
  this.orderedBy = orderedBy;
 }

 public byte[] getOrderFileData() {
  return orderFileData;
 }

 public void setOrderFileData(byte[] orderFileData) {
  this.orderFileData = orderFileData;
 }

 public String getOderFileName() {
  return oderFileName;
 }

 public void setOderFileName(String oderFileName) {
  this.oderFileName = oderFileName;
 }

 public String getOderFileType() {
  return oderFileType;
 }

 public void setOderFileType(String oderFileType) {
  this.oderFileType = oderFileType;
 }

 public String getOrderReference() {
  return orderReference;
 }

 public void setOrderReference(String orderReference) {
  this.orderReference = orderReference;
 }


 
 public PaymentTermsRec getPayTerms() {
  return payTerms;
 }

 public void setPayTerms(PaymentTermsRec payTerms) {
  this.payTerms = payTerms;
 }

 public PaymentTypeRec getPayType() {
  return payType;
 }

 public void setPayType(PaymentTypeRec payType) {
  this.payType = payType;
 }

  public String getPtnrRef() {
  return ptnrRef;
 }

 public void setPtnrRef(String ptnrRef) {
  this.ptnrRef = ptnrRef;
 }

 public DocFiRec getDocFi() {
  return docFi;
 }

 public void setDocFi(DocFiRec docFi) {
  this.docFi = docFi;
 }

 public String getDocTypeName() {
  return docTypeName;
 }

 public void setDocTypeName(String docTypeName) {
  this.docTypeName = docTypeName;
 }

 
 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }

 public ApAccountRec getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 public ArBankAccountRec getPaymntBank() {
  return paymntBank;
 }

 public void setPaymntBank(ArBankAccountRec paymntBank) {
  this.paymntBank = paymntBank;
 }

 public FiApPeriodBalanceRec getApCreditPeriodBalance() {
  return apCreditPeriodBalance;
 }

 public void setApCreditPeriodBalance(FiApPeriodBalanceRec apCreditPeriodBalance) {
  this.apCreditPeriodBalance = apCreditPeriodBalance;
 }

 public FiApPeriodBalanceRec getApDebitPeriodBalance() {
  return apDebitPeriodBalance;
 }

 public void setApDebitPeriodBalance(FiApPeriodBalanceRec apDebitPeriodBalance) {
  this.apDebitPeriodBalance = apDebitPeriodBalance;
 }

 public boolean isGiftAid() {
  return giftAid;
 }

 public void setGiftAid(boolean giftAid) {
  this.giftAid = giftAid;
 }

 public DocBankLineBaseRec getBankPayRunLine() {
  return bankPayRunLine;
 }

 public void setBankPayRunLine(DocBankLineBaseRec bankPayRunLine) {
  this.bankPayRunLine = bankPayRunLine;
 }

 public List<DocLineGlRec> getReconiliationLines() {
  return reconiliationLines;
 }

 public void setReconiliationLines(List<DocLineGlRec> reconiliationLines) {
  this.reconiliationLines = reconiliationLines;
 }

 
 public byte[] getSupplierDocFileContents() {
  return supplierDocFileContents;
 }

 public void setSupplierDocFileContents(byte[] supplierDocFileContents) {
  this.supplierDocFileContents = supplierDocFileContents;
 }

 public String getSupplierDocFileName() {
  return supplierDocFileName;
 }

 public void setSupplierDocFileName(String supplierDocFileName) {
  this.supplierDocFileName = supplierDocFileName;
 }

 public String getSupplierDocFileType() {
  return supplierDocFileType;
 }

 public void setSupplierDocFileType(String supplierDocFileType) {
  this.supplierDocFileType = supplierDocFileType;
 }

 public double getUnpaidAmount() {
  return unpaidAmount;
 }

 public void setUnpaidAmount(double unpaidAmount) {
  this.unpaidAmount = unpaidAmount;
 }

 
 
}
