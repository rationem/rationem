/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class BacsPaySumRec implements Serializable {
 private ArAccountRec arAccount;
 private DocLineArRec doc;
 private BacsTransCodeRec bacsCode;
 private BankAccountRec targetBank;
 private double amount;

 public BacsPaySumRec() {
 }

 public ArAccountRec getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccountRec arAccount) {
  this.arAccount = arAccount;
 }

 public DocLineArRec getDoc() {
  return doc;
 }

 public void setDoc(DocLineArRec doc) {
  this.doc = doc;
 }

 public BacsTransCodeRec getBacsCode() {
  return bacsCode;
 }

 public void setBacsCode(BacsTransCodeRec bacsCode) {
  this.bacsCode = bacsCode;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public BankAccountRec getTargetBank() {
  return targetBank;
 }

 public void setTargetBank(BankAccountRec targetBank) {
  this.targetBank = targetBank;
 }
 
 
 
 
}
