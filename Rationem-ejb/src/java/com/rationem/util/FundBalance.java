/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.fi.company.FundRec;

/**
 *
 * @author user
 */
public class FundBalance {
 
 
 private FundRec fund;
 private double amount;
 private double percentage;

 public FundBalance() {
 }

 

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }
 
 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 public double getPercentage() {
  return percentage;
 }

 public void setPercentage(double percentage) {
  this.percentage = percentage;
 }
 
 
}
