/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author user
 */
public class PayRunSumAct implements Serializable {
 
 private static final UUID UNQID = UUID.randomUUID();
 private long id;
 private String accountRef;
 private double open;
 private String partnerName;
 private double payment;
 private boolean selected;

 public PayRunSumAct() {
  id = UNQID.getLeastSignificantBits();
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public String getAccountRef() {
  return accountRef;
 }

 public void setAccountRef(String accountRef) {
  this.accountRef = accountRef;
 }

 public double getOpen() {
  return open;
 }

 public void setOpen(double open) {
  this.open = open;
 }

 
 public String getPartnerName() {
  return partnerName;
 }

 public void setPartnerName(String partnerName) {
  this.partnerName = partnerName;
 }

 
 public double getPayment() {
  return payment;
 }

 public void setPayment(double payment) {
  this.payment = payment;
 }

 public boolean isSelected() {
  return selected;
 }

 public void setSelected(boolean selected) {
  this.selected = selected;
 }
 
}
