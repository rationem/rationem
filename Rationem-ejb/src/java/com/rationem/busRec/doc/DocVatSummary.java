/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import java.io.Serializable;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;

/**
 * VAT summary for document populated in Web layer and used in DB layer
 * @author Chris
 */
public class DocVatSummary implements Serializable {
 private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.doc.DocVatSummary");
 
  
 private VatCodeCompanyRec vatCode;
 private FundRec fund;
 private FiGlAccountCompRec rateAccount;
 private boolean provPost;
 private FiGlAccountCompRec provisionAccount;
 private boolean irrecoverable;
 private FiGlAccountCompRec irrecoverableAccount;
 private double goods;
 private double vat;
 private double irrecoverableVat;
 private FiGlAccountCompRec glAccount;
 private VatRegSchemeRec vatRegScheme;
 private PostTypeRec postingType;

 public DocVatSummary() {
 }

 public VatCodeCompanyRec getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompanyRec vatCode) {
  this.vatCode = vatCode;
 }

 public VatRegSchemeRec getVatRegScheme() {
  return vatRegScheme;
 }

 public void setVatRegScheme(VatRegSchemeRec vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }

 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 public FiGlAccountCompRec getGlAccount() {
  if(glAccount == null){
   glAccount = new FiGlAccountCompRec();
  }
  return glAccount;
 }

 public void setGlAccount(FiGlAccountCompRec glAccount) {
  
  this.glAccount = glAccount;
 }

 
 public FiGlAccountCompRec getRateAccount() {
  return rateAccount;
 }

 public void setRateAccount(FiGlAccountCompRec rateAccount) {
  this.rateAccount = rateAccount;
 }

 public PostTypeRec getPostingType() {
  return postingType;
 }

 public void setPostingType(PostTypeRec postingType) {
  this.postingType = postingType;
 }

 public boolean isProvPost() {
  return provPost;
 }

 public void setProvPost(boolean provPost) {
  this.provPost = provPost;
 }

 public FiGlAccountCompRec getProvisionAccount() {
  return provisionAccount;
 }

 public void setProvisionAccount(FiGlAccountCompRec provisionAccount) {
  this.provisionAccount = provisionAccount;
 }

 public boolean isIrrecoverable() {
  return irrecoverable;
 }

 public void setIrrecoverable(boolean irrecoverable) {
  this.irrecoverable = irrecoverable;
 }

 public FiGlAccountCompRec getIrrecoverableAccount() {
  return irrecoverableAccount;
 }

 public void setIrrecoverableAccount(FiGlAccountCompRec irrecoverableAccount) {
  this.irrecoverableAccount = irrecoverableAccount;
 }

 public double getIrrecoverableVat() {
  return irrecoverableVat;
 }

 public void setIrrecoverableVat(double irrecoverableVat) {
  this.irrecoverableVat = irrecoverableVat;
 }

 public double getGoods() {
  return goods;
 }

 public void setGoods(double goods) {
  this.goods = goods;
 }

 public double getVat() {
  return vat;
 }

 public void setVat(double vat) {
  this.vat = vat;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 13 * hash + (this.vatCode != null ? this.vatCode.hashCode() : 0);
  hash = 13 * hash + (this.fund != null ? this.fund.hashCode() : 0);
  hash = 13 * hash + (this.glAccount != null ? this.glAccount.hashCode() : 0);
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
  final DocVatSummary other = (DocVatSummary) obj;
  if (this.vatCode != other.vatCode && (this.vatCode == null || !this.vatCode.equals(other.vatCode))) {
   return false;
  }
  if (this.fund != other.fund && (this.fund == null || !this.fund.equals(other.fund))) {
   return false;
  }
  if (this.glAccount != other.glAccount && (this.glAccount == null || !this.glAccount.equals(other.glAccount))) {
   return false;
  }
  return true;
 }

 
}
