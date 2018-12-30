/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.TransactionTypeRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class DocFiPartialRec extends DocBasePartialRec {
 
 private Date taxDate;
  private String taxDateStr;
  private String partnerRef;
  private int fisPeriod;
  private int fisYear;
  private DocTypeRec docType;
  private DocInvoiceArRec docInvoiceAr;
  private TransactionTypeRec transactionType;
  private boolean reversed;
  private List<DocLineGlPartialRec> glLines;

 public DocFiPartialRec() {
 }

 public Date getTaxDate() {
  return taxDate;
 }

 public void setTaxDate(Date taxDate) {
  this.taxDate = taxDate;
 }

 public String getTaxDateStr() {
  return taxDateStr;
 }

 public void setTaxDateStr(String taxDateStr) {
  this.taxDateStr = taxDateStr;
 }

 public TransactionTypeRec getTransactionType() {
  return transactionType;
 }

 public void setTransactionType(TransactionTypeRec transactionType) {
  this.transactionType = transactionType;
 }

 public String getPartnerRef() {
  return partnerRef;
 }

 public void setPartnerRef(String partnerRef) {
  this.partnerRef = partnerRef;
 }

 public int getFisPeriod() {
  return fisPeriod;
 }

 public void setFisPeriod(int fisPeriod) {
  this.fisPeriod = fisPeriod;
 }

 public int getFisYear() {
  return fisYear;
 }

 public void setFisYear(int fisYear) {
  this.fisYear = fisYear;
 }

 
  public List<DocLineGlPartialRec> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineGlPartialRec> glLines) {
  this.glLines = glLines;
 }

 public DocTypeRec getDocType() {
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
 }

 public DocInvoiceArRec getDocInvoiceAr() {
  return docInvoiceAr;
 }

 public void setDocInvoiceAr(DocInvoiceArRec docInvoiceAr) {
  this.docInvoiceAr = docInvoiceAr;
 }

 public boolean isReversed() {
  return reversed;
 }

 public void setReversed(boolean reversed) {
  this.reversed = reversed;
 }
  
  

 
}
