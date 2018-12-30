/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountRec;

/**
 *
 * @author Chris
 */
public class DocBankLineBacsRec extends DocBankLineBaseRec {
 
 
 private BacsTransCodeRec bacsTransCode;
 private BankAccountRec bankAccountPtnr;
 private String bacsPtnrName;
 

 public BacsTransCodeRec getBacsTransCode() {
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCodeRec bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
 }

 public BankAccountRec getBankAccountPtnr() {
  return bankAccountPtnr;
 }

 public void setBankAccountPtnr(BankAccountRec bankAccountPtnr) {
  this.bankAccountPtnr = bankAccountPtnr;
 }

 public String getBacsPtnrName() {
  return bacsPtnrName;
 }

 public void setBacsPtnrName(String bacsPtnrName) {
  this.bacsPtnrName = bacsPtnrName;
 }
 
 
 
}
