/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.tr.BankAccountCompanyRec;

/**
 *
 * @author user
 */
public class NumberRangeChequeRec extends NumberRangeRec {
 
 private BankAccountCompanyRec bankAccountComp;

 public BankAccountCompanyRec getBankAccountComp() {
  return bankAccountComp;
 }

 public void setBankAccountComp(BankAccountCompanyRec bankAccountComp) {
  this.bankAccountComp = bankAccountComp;
 }
 
 
 
}
