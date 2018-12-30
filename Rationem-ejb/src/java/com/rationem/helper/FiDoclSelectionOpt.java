/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class FiDoclSelectionOpt implements Serializable {
 
 private CompanyBasicRec comp;
 private Date docDateFrom;
 private Date docDateTo;
 private Date postDateFrom;
 private Date postDateTo;
 private Date entryDateFrom;
 private Date entryDateTo;
 private UserRec enteredBy;
 private DocTypeRec docType;
 private String docText;
 private String ptnrRef;
 private String ledgerCode;
 private BankAccountCompanyRec bnkAc;
 private NumberRangeChequeRec chkBk;
 

 public FiDoclSelectionOpt() {
 }

 public BankAccountCompanyRec getBnkAc() {
  return bnkAc;
 }

 public void setBnkAc(BankAccountCompanyRec bnkAc) {
  this.bnkAc = bnkAc;
 }

 public NumberRangeChequeRec getChkBk() {
  return chkBk;
 }

 public void setChkBk(NumberRangeChequeRec chkBk) {
  this.chkBk = chkBk;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
  
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public Date getDocDateFrom() {
  return docDateFrom;
 }

 public void setDocDateFrom(Date docDateFrom) {
  this.docDateFrom = docDateFrom;
 }

 public Date getDocDateTo() {
  return docDateTo;
 }

 public void setDocDateTo(Date docDateTo) {
  this.docDateTo = docDateTo;
 }
 
 public String getDocText() {
  return docText;
 }

 public void setDocText(String docText) {
  this.docText = docText;
 }

 public DocTypeRec getDocType() {
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
 }
 
 public UserRec getEnteredBy() {
  return enteredBy;
 }

 public void setEnteredBy(UserRec enteredBy) {
  this.enteredBy = enteredBy;
 }
 
 public Date getEntryDateFrom() {
  return entryDateFrom;
 }

 public void setEntryDateFrom(Date entryDateFrom) {
  this.entryDateFrom = entryDateFrom;
 }

 public Date getEntryDateTo() {
  return entryDateTo;
 }

 /**
  *
  * @param entryDateTo
  */
 public void setEntryDateTo(Date entryDateTo) {
  this.entryDateTo = entryDateTo;
 }

 public String getLedgerCode() {
  return ledgerCode;
 }

 public void setLedgerCode(String ledgerCode) {
  this.ledgerCode = ledgerCode;
 }
 
 

 

 public Date getPostDateFrom() {
  return postDateFrom;
 }

 public void setPostDateFrom(Date postDateFrom) {
  this.postDateFrom = postDateFrom;
 }

 public Date getPostDateTo() {
  return postDateTo;
 }

 public void setPostDateTo(Date postDateTo) {
  this.postDateTo = postDateTo;
 }

 
 

 

 public String getPtnrRef() {
  return ptnrRef;
 }

 public void setPtnrRef(String ptnrRef) {
  this.ptnrRef = ptnrRef;
 }
 
 
 
}
