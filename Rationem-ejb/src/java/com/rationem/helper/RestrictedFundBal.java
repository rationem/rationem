/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.entity.fi.company.Fund;

/**
 *
 * @author Chris
 */
public class RestrictedFundBal {
 
 private long id;
 private Fund fund;
 private double amount;
 private double proportion;

 public RestrictedFundBal() {
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public Fund getFund() {
  return fund;
 }

 public void setFund(Fund fund) {
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
  int hash = 5;
  hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
  final RestrictedFundBal other = (RestrictedFundBal) obj;
  if (this.id != other.id) {
   return false;
  }
  return true;
 }
 
 
 
}
