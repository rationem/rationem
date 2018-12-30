/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Business VAT code record
 * The VAT record holds the rate that is applicable and value date
 * @author Chris
 */
public class VatCodeRec implements Serializable {

 private Long Id;
 private String code;
 private String description;
 private double rate;
 private double irrrecoverableRate;
 private boolean inputTax;
 private boolean interStatSupply;
 /**
  * Import or Export flag not currently used
  * I = Import
  * E= Export
  * Space = EU Supply
  */
 private char importExport; 
 private char taxType;
 private boolean vatRule;
 private Date validFrom;
 private Date validTo;
 private List<VatCodeCompanyRec> vatCodeCompanies;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int revision;

  public VatCodeRec() {
  }

  

  public Long getId() {
    return Id;
  }

  public void setId(Long Id) {
    this.Id = Id;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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

  public double getIrrrecoverableRate() {
    return irrrecoverableRate;
  }

  public void setIrrrecoverableRate(double irrrecoverableRate) {
    this.irrrecoverableRate = irrrecoverableRate;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

 public boolean isInterStatSupply() {
  return interStatSupply;
 }

 public void setInterStatSupply(boolean interstat) {
  this.interStatSupply = interstat;
 }

 public char getImportExport() {
  if(importExport != 'I' || importExport != 'E'){
   importExport = 'N';
  }
  return importExport;
 }

 public void setImportExport(char importExport) {
  this.importExport = importExport;
 }

 public boolean isInputTax() {
  
  return inputTax;
 }

 public void setInputTax(boolean inputTax) {
  this.inputTax = inputTax;
 }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
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
    GregorianCalendar cal = new GregorianCalendar();
    cal.set(9999,11,31);
    validTo = cal.getTime();
   }
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

 public List<VatCodeCompanyRec> getVatCodeCompanies() {
  return vatCodeCompanies;
 }

 public void setVatCodeCompanies(List<VatCodeCompanyRec> vatCodeCompanies) {
  this.vatCodeCompanies = vatCodeCompanies;
 }

 public char getTaxType() {
  return taxType;
 }

 public void setTaxType(char taxType) {
  this.taxType = taxType;
 }

 public boolean isVatRule() {
  return vatRule;
 }

 public void setVatRule(boolean vatRule) {
  this.vatRule = vatRule;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 37 * hash + (this.Id != null ? this.Id.hashCode() : 0);
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
  final VatCodeRec other = (VatCodeRec) obj;
  if (this.Id != other.Id && (this.Id == null || !this.Id.equals(other.Id))) {
   return false;
  }
  return true;
 }
  


}
