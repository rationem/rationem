/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.salesTax.vat.VatCodeRec;

/**
 * helper class for arraylist of VAT codes for a GL account id
 * @author Chris
 */
public class GlAcVatCode {
 
 int glAcId;
 VatCodeRec vatCode;

 public GlAcVatCode() {
 }

 public int getGlAcId() {
  return glAcId;
 }

 public void setGlAcId(int glAcId) {
  this.glAcId = glAcId;
 }

 public VatCodeRec getVatCode() {
  
  return vatCode;
 }

 public void setVatCode(VatCodeRec vatCode) {
  this.vatCode = vatCode;
 }

 @Override
 public int hashCode() {
  int hash = 5;
  hash = 71 * hash + this.glAcId;
  hash = 71 * hash + (this.vatCode != null ? this.vatCode.hashCode() : 0);
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
  final GlAcVatCode other = (GlAcVatCode) obj;
  if (this.glAcId != other.glAcId) {
   return false;
  }
  if (this.vatCode != other.vatCode && (this.vatCode == null || !this.vatCode.equals(other.vatCode))) {
   return false;
  }
  return true;
 }
 
 
 
}
