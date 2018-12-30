/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class ArInvReceiptLine implements Serializable {
 
 private long id;
 private String docNumber;
 private String extDocNumber;
 private Date docDate;
 private Date dueDate;
 private String businessKey;
 private String postType;
 private String transType;
 private String descr;
 private String amount;
 private String payment;
 private String unpaid;
 private boolean allocated;

 public ArInvReceiptLine() {
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public String getBusinessKey() {
  return businessKey;
 }

 public void setBusinessKey(String businessKey) {
  this.businessKey = businessKey;
 }
 
 

 public String getDocNumber() {
  return docNumber;
 }

 public void setDocNumber(String docNumber) {
  this.docNumber = docNumber;
 }

 public String getExtDocNumber() {
  return extDocNumber;
 }

 public void setExtDocNumber(String extDocNumber) {
  this.extDocNumber = extDocNumber;
 }

 public Date getDocDate() {
  return docDate;
 }

 public void setDocDate(Date docDate) {
  this.docDate = docDate;
 }

 

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }



 public String getPostType() {
  return postType;
 }

 public void setPostType(String type) {
  this.postType = type;
 }

 
 public String getDescr() {
  return descr;
 }

 public void setDescr(String descr) {
  this.descr = descr;
 }

 public String getTransType() {
  return transType;
 }

 public void setTransType(String transType) {
  this.transType = transType;
 }

 public String getAmount() {
  return amount;
 }

 public void setAmount(String amount) {
  this.amount = amount;
 }

 public String getPayment() {
  return payment;
 }

 public void setPayment(String payment) {
  this.payment = payment;
 }

 public String getUnpaid() {
  return unpaid;
 }

 public void setUnpaid(String unpaid) {
  this.unpaid = unpaid;
 }

 public boolean isAllocated() {
  return allocated;
 }

 public void setAllocated(boolean allocated) {
  this.allocated = allocated;
 }
 
 
 
}
