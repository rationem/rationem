/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.doc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Financial Accounting Document
 * @author Chris
 */
public class DocFiRec extends DocBaseRec {

  private Date taxDate;
  private String partnerRef;
  private int fisPeriod;
  private int fisYear;
  private DocTypeRec docType;
  private DocInvoiceArRec docInvoiceAr;
  private boolean reversed;
  private boolean vatable;
  private String fisYearPeriod;

  
 
  public DocFiRec() {
  }

  public DocFiRec(Date taxDate, String partnerRef) {
    this.taxDate = taxDate;
    this.partnerRef = partnerRef;
  }

  public String getPartnerRef() {
    return partnerRef;
  }

  public void setPartnerRef(String partnerRef) {
    this.partnerRef = partnerRef;
  }

 public boolean isReversed() {
  return reversed;
 }

 public void setReversed(boolean reversed) {
  this.reversed = reversed;
 }

 public String getTaxDateStr() {
  String ret = new String();
  if(taxDate != null){
   SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MMM/yyyy");
    ret =  dateFmt.format(taxDate.getTime());
  }
  return ret;
 }

  
  public Date getTaxDate() {
    return taxDate;
  }

  public void setTaxDate(Date taxDate) {
    this.taxDate = taxDate;
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

 public String getFisYearPeriod() {
  if(fisYearPeriod == null || fisYearPeriod.isEmpty()){
   fisYearPeriod = String.valueOf(fisYear)+" / "+ String.valueOf(fisPeriod);
  }
  return fisYearPeriod;
 }

 public void setFisYearPeriod(String fisYearPeriod) {
  this.fisYearPeriod = fisYearPeriod;
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

 public boolean isVatable() {
  return vatable;
 }

 public void setVatable(boolean vatable) {
  this.vatable = vatable;
 }

  

}
