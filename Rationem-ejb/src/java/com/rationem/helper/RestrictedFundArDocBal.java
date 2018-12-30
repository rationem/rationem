/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

/**
 * Helper structure to hold the restricted balance split at document level
 * @author Chris
 */
public class RestrictedFundArDocBal {
 
 private long arDocId;
 private long fundId;
 private double amount;

 public RestrictedFundArDocBal() {
 }

 public long getArDocId() {
  return arDocId;
 }

 public void setArDocId(long arDocId) {
  this.arDocId = arDocId;
 }

 public long getFundId() {
  return fundId;
 }

 public void setFundId(long fundId) {
  this.fundId = fundId;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 67 * hash + (int) (this.arDocId ^ (this.arDocId >>> 32));
  hash = 67 * hash + (int) (this.fundId ^ (this.fundId >>> 32));
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
  final RestrictedFundArDocBal other = (RestrictedFundArDocBal) obj;
  if (this.arDocId != other.arDocId) {
   return false;
  }
  if (this.fundId != other.fundId) {
   return false;
  }
  return true;
 }
 
 
 
}
