/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.salesTax.vat.VatRegistrationRec;

/**
 * Helper to hold Vat Registration by company
 * @author Chris
 */
public class CompVatRegistrationRec {
 
 private Long companyId;
 private VatRegistrationRec vatRegSumm;

 public CompVatRegistrationRec() {
 }

 public Long getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Long companyId) {
  this.companyId = companyId;
 }

 public VatRegistrationRec getVatRegSumm() {
  return vatRegSumm;
 }

 public void setVatRegSumm(VatRegistrationRec vatRegSumm) {
  this.vatRegSumm = vatRegSumm;
 }
 
 
 
 
 
}
