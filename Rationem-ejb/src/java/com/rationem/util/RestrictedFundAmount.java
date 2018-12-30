/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;

/**
 *
 * @author Chris
 */
public class RestrictedFundAmount {
 
 private Long fund;
 private FiGlAccountComp glAccountComp;
 private double amount;

 public RestrictedFundAmount() {
 }

 public RestrictedFundAmount(Long fund, FiGlAccountComp glAccountComp, double amount) {
  this.fund = fund;
  this.glAccountComp = glAccountComp;
  this.amount = amount;
 }

 public Long getFund() {
  return fund;
 }

 public void setFund(Long fund) {
  this.fund = fund;
 }

 public FiGlAccountComp getGlAccountComp() {
  return glAccountComp;
 }

 public void setGlAccountComp(FiGlAccountComp glAccountComp) {
  this.glAccountComp = glAccountComp;
 }

 

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 
 
 }
