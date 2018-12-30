/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import java.util.UUID;

/**
 *
 * @author Chris
 */
public class BacsContraAmount {
 
 private String contraCode;
 private double amount;
 private FiGlAccountCompRec glBnkAcComp;
 private FundRec restrictedFnd;

 public BacsContraAmount() {
 }

 

 public String getContraCode() {
  return contraCode;
 }

 public void setContraCode(String contraCode) {
  this.contraCode = contraCode;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public FiGlAccountCompRec getGlBnkAcComp() {
  return glBnkAcComp;
 }

 public void setGlBnkAcComp(FiGlAccountCompRec glBnkAcComp) {
  this.glBnkAcComp = glBnkAcComp;
 }

 public FundRec getRestrictedFnd() {
  return restrictedFnd;
 }

 public void setRestrictedFnd(FundRec restrictedFnd) {
  this.restrictedFnd = restrictedFnd;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 37 * hash + (this.contraCode != null ? this.contraCode.hashCode() : 0);
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
  final BacsContraAmount other = (BacsContraAmount) obj;
  if ((this.contraCode == null) ? (other.contraCode != null) : !this.contraCode.equals(other.contraCode)) {
   return false;
  }
  return true;
 }
 
 
 
}
