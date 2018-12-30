/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.ChequeVoidReasonRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 * Details of a cheque
 * @author user
 */
public class DocBankLineChqRec extends DocBankLineBaseRec {
 
 private Date issueDate;
 private PartnerBaseRec payee;
 private String fileType;
 private byte[] fileContent;
 private ChequeVoidReasonRec voidReason;
 private Date voidDate;
 private UserRec voidBy;
 private boolean printed;

 public Date getIssueDate() {
  return issueDate;
 }

 public void setIssueDate(Date issueDate) {
  this.issueDate = issueDate;
 }

 public PartnerBaseRec getPayee() {
  return payee;
 }

 public void setPayee(PartnerBaseRec payee) {
  this.payee = payee;
 }

 public boolean isPrinted() {
  return printed;
 }

 public void setPrinted(boolean printed) {
  this.printed = printed;
 }

 
 public String getFileType() {
  return fileType;
 }

 public void setFileType(String fileType) {
  this.fileType = fileType;
 }

 public byte[] getFileContent() {
  return fileContent;
 }

 public void setFileContent(byte[] fileContent) {
  this.fileContent = fileContent;
 }

 public ChequeVoidReasonRec getVoidReason() {
  return voidReason;
 }

 public void setVoidReason(ChequeVoidReasonRec voidReason) {
  this.voidReason = voidReason;
 }

 public Date getVoidDate() {
  return voidDate;
 }

 public void setVoidDate(Date voidDate) {
  this.voidDate = voidDate;
 }

 public UserRec getVoidBy() {
  return voidBy;
 }

 public void setVoidBy(UserRec voidBy) {
  this.voidBy = voidBy;
 }
 
 
}
