/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class ApArPayRunDocLine implements Serializable {
 
 private Long id;
 private Long docNumber;
 private Long lineNumber;
 private ApAccountRec apAcnt;
 private ArAccountRec arAcnt;
 private Long accountId;
 private String accountRef;
 private PostTypeRec postType;
 private DocTypeRec docType;
 private PaymentTypeRec payType;
 private Date dueDate;
 private String lineText;
 private double docAmount;
 private double partPayAmount;

 public ApArPayRunDocLine() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
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

 public ApAccountRec getApAcnt() {
  return apAcnt;
 }

 public void setApAcnt(ApAccountRec apAcnt) {
  this.apAcnt = apAcnt;
 }

 public ArAccountRec getArAcnt() {
  return arAcnt;
 }

 public void setArAcnt(ArAccountRec arAcnt) {
  this.arAcnt = arAcnt;
 }

 
 public Long getDocNumber() {
  return docNumber;
 }

 public void setDocNumber(Long docNumber) {
  this.docNumber = docNumber;
 }

 
 public PostTypeRec getPostType() {
  return postType;
 }

 public void setPostType(PostTypeRec postType) {
  this.postType = postType;
 }

 public DocTypeRec getDocType() {
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
 }

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }

 
 public Long getLineNumber() {
  return lineNumber;
 }

 public void setLineNumber(Long lineNumber) {
  this.lineNumber = lineNumber;
 }

 
 public String getLineText() {
  return lineText;
 }

 public void setLineText(String lineText) {
  this.lineText = lineText;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public double getPartPayAmount() {
  return partPayAmount;
 }

 public void setPartPayAmount(double partPayAmount) {
  this.partPayAmount = partPayAmount;
 }

 public PaymentTypeRec getPayType() {
  return payType;
 }

 public void setPayType(PaymentTypeRec payType) {
  this.payType = payType;
 }
 
 
 
 
}
