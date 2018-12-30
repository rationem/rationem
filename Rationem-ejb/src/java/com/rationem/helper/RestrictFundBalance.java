/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.entity.fi.company.Fund;
import java.io.Serializable;

/**
 *  helper to hold the balance associated with a document
 * @author Chris
 */
public class RestrictFundBalance implements Serializable {
 
 private FundRec fund;
 private double amount;
 private double proportion;

 public RestrictFundBalance() {
 }

 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public double getProportion() {
  return proportion;
 }

 public void setProportion(double proportion) {
  this.proportion = proportion;
 }

 @Override
 public int hashCode() {
  int hash = 3;
  hash = 79 * hash + (this.getFund().getId() != null ? this.getFund().getId().hashCode() : 0);
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
  final RestrictFundBalance other = (RestrictFundBalance) obj;
  if (this.getFund().getId() != other.getFund().getId() && (this.getFund().getId() == null || 
          !this.getFund().getId().equals(other.getFund().getId()))) {
   return false;
  }
  return true;
 }

 
 
}
