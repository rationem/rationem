/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.doc;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.arap.FiArPeriodBalanceRec;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;


/**
 *
 * @author Chris
 */
public class DocLineArRec extends DocLineBaseRec {
 private static final Logger logger =  Logger.getLogger(DocLineArRec.class.getName());
  private FiArPeriodBalanceRec arCreditPeriodBalance;
  private FiArPeriodBalanceRec arDebitPeriodBalance;
  private int currID;
  private PaymentTermsRec payTerms;
  private PaymentTypeRec payType;
  private ArBankAccountRec bankAc;
  private Date dueDate;
  private ArAccountRec arAccount;
  private DocInvoiceArRec invoice;
  private List<DocLineGlRec> reconiliationLines;
  private boolean giftAid;
  private String bacsPayRunRef;
  private int bacsPayRunLnNum;
  private DocBankLineBaseRec bankLine;
  /**
   * temp field
   */
  private int bacsPaySumRecLine;

  public DocLineArRec() {
  }

 @Override
  public String getAccountRef(){
  String acNum = arAccount.getArAccountCode();
  return acNum;
 }
  

  public ArAccountRec getArAccount() {
   if(arAccount == null){
    arAccount = new ArAccountRec();
   }
    return arAccount;
  }

  public void setArAccount(ArAccountRec arAccount) {
    this.arAccount = arAccount;
  }

  public FiArPeriodBalanceRec getArCreditPeriodBalance() {
    return arCreditPeriodBalance;
  }

  public void setArCreditPeriodBalance(FiArPeriodBalanceRec arCreditPeriodBalance) {
    this.arCreditPeriodBalance = arCreditPeriodBalance;
  }

  public FiArPeriodBalanceRec getArDebitPeriodBalance() {
    return arDebitPeriodBalance;
  }

  public void setArDebitPeriodBalance(FiArPeriodBalanceRec arDebitPeriodBalance) {
    this.arDebitPeriodBalance = arDebitPeriodBalance;
  }

  public int getCurrID() {
    return currID;
  }

  public void setCurrID(int currID) {
    this.currID = currID;
  }

  

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

 public boolean isGiftAid() {
  return giftAid;
 }

 public void setGiftAid(boolean giftAid) {
  this.giftAid = giftAid;
 }

 public DocInvoiceArRec getInvoice() {
  return invoice;
 }

 public void setInvoice(DocInvoiceArRec invoice) {
  this.invoice = invoice;
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

 public List<DocLineGlRec> getReconiliationLines() {
  logger.log(INFO, "DocLineArRec.getReconiliationLines is {0}", reconiliationLines);
  return reconiliationLines;
 }

 public void setReconiliationLines(List<DocLineGlRec> reconiliationLines) {
  this.reconiliationLines = reconiliationLines;
 }
 
 public String getBacsPayRunRef() {
  return bacsPayRunRef;
 }

 public void setBacsPayRunRef(String bacsPayRunRef) {
  this.bacsPayRunRef = bacsPayRunRef;
 }

 public int getBacsPayRunLnNum() {
  return bacsPayRunLnNum;
 }

 public void setBacsPayRunLnNum(int bacsPayRunLnNum) {
  this.bacsPayRunLnNum = bacsPayRunLnNum;
 }

 public ArBankAccountRec getBankAc() {
  return bankAc;
 }

 public void setBankAc(ArBankAccountRec bankAc) {
  this.bankAc = bankAc;
 }

 public DocBankLineBaseRec getBankLine() {
  return bankLine;
 }

 public void setBankLine(DocBankLineBaseRec bankLine) {
  this.bankLine = bankLine;
 }

 public int getBacsPaySumRecLine() {
  return bacsPaySumRecLine;
 }

 public void setBacsPaySumRecLine(int bacsPaySumRecLine) {
  this.bacsPaySumRecLine = bacsPaySumRecLine;
 }

 

}
