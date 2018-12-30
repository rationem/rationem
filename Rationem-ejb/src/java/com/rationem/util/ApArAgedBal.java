/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.io.Serializable;
import java.util.UUID;



/**
 *
 * @author user
 */
public class ApArAgedBal implements Serializable {
 
 private Long lineId;
 private Long acntId;
 private String acntName;
 private String acntRef;
 private double bal;
 private double amount1;
 private double amount2;
 private double amount3;
 private double amount4;
 private double amount5;
 private final UUID uuid;

 public ApArAgedBal() {
  uuid = UUID.randomUUID();
  lineId = uuid.getLeastSignificantBits();
  
 }

 public Long getLineId() {
  return lineId;
 }

 public void setLineId(Long lineId) {
  this.lineId = lineId;
 }

 public Long getAcntId() {
  return acntId;
 }

 public void setAcntId(Long acntId) {
  this.acntId = acntId;
 }

 public String getAcntName() {
  return acntName;
 }

 public void setAcntName(String acntName) {
  this.acntName = acntName;
 }

 public String getAcntRef() {
  return acntRef;
 }

 public void setAcntRef(String acntRef) {
  this.acntRef = acntRef;
 }

 
 public double getAmount1() {
  return amount1;
 }

 public void setAmount1(double amount1) {
  this.amount1 = amount1;
 }

 public double getAmount2() {
  return amount2;
 }

 public void setAmount2(double amount2) {
  this.amount2 = amount2;
 }

 public double getAmount3() {
  return amount3;
 }

 public void setAmount3(double amount3) {
  this.amount3 = amount3;
 }

 public double getAmount4() {
  return amount4;
 }

 public void setAmount4(double amount4) {
  this.amount4 = amount4;
 }

 public double getAmount5() {
  return amount5;
 }

 public void setAmount5(double amount5) {
  this.amount5 = amount5;
 }

 public double getBal() {
  return bal;
 }

 public void setBal(double bal) {
  this.bal = bal;
 }
 
 
 
 
 
}
