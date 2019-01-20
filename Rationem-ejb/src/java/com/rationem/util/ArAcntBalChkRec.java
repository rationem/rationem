/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class ArAcntBalChkRec implements Serializable {
 
 private Long accountId;
 private String accountRef;
 private String name;
 private double acntBal;
 private double lineBal;

 public ArAcntBalChkRec() {
 }

 public Long getAccountId() {
  return accountId;
 }

 public void setAccountId(Long accountId) {
  this.accountId = accountId;
 }

 public String getAccountRef() {
  return accountRef;
 }

 public void setAccountRef(String accountRef) {
  this.accountRef = accountRef;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public double getAcntBal() {
  return acntBal;
 }

 public void setAcntBal(double acntBal) {
  this.acntBal = acntBal;
 }

 public double getLineBal() {
  return lineBal;
 }

 public void setLineBal(double lineBal) {
  this.lineBal = lineBal;
 }
 
 
 
}
