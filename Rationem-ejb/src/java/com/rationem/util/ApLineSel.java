/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author user
 */
public class ApLineSel implements Serializable {
 private static final Logger LOGGER =  Logger.getLogger(ApLineSel.class.getName());
 public static final int STATUS_OS = 0;
 public static final int STATUS_PAID = 1;
 public static final int STATUS_ALL = 2;
 
 private String fiscYearFr;
 private String fiscYearTo;
 private String fiscPerFr;
 private String fiscPerTo;
 private Date docDateFr;
 private Date docDateTo;
 private Date postDateFr;
 private Date postDateTo;
 private Date dueDateFr;
 private Date dueDateTo;
 private CompanyBasicRec comp;
 private List<CompanyBasicRec> compList; 
 private ApAccountRec apAcnt;
 private List<ApAccountRec> apAcntList;
 private DocTypeRec docTy;
 private List<DocTypeRec> docTypeList;
 private int payStatus;
 List<PaymentTypeRec> paymentTypeList;

 public ApLineSel() {
 }

 
 public ApAccountRec getApAcnt() {
  return apAcnt;
 }

 public void setApAcnt(ApAccountRec apAcnt) {
  this.apAcnt = apAcnt;
 }

 public List<ApAccountRec> getApAcntList() {
  return apAcntList;
 }

 public void setApAcntList(List<ApAccountRec> apAcntList) {
  this.apAcntList = apAcntList;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public List<CompanyBasicRec> getCompList() {
  return compList;
 }

 public void setCompList(List<CompanyBasicRec> compList) {
  this.compList = compList;
 }

 
 public Date getDocDateFr() {
  return docDateFr;
 }

 public void setDocDateFr(Date docDateFr) {
  this.docDateFr = docDateFr;
 }

 public Date getDocDateTo() {
  return docDateTo;
 }

 public void setDocDateTo(Date docDateTo) {
  this.docDateTo = docDateTo;
 }

 public DocTypeRec getDocTy() {
  return docTy;
 }

 public void setDocTy(DocTypeRec docTy) {
  this.docTy = docTy;
 }

 public List<DocTypeRec> getDocTypeList() {
  return docTypeList;
 }

 public void setDocTypeList(List<DocTypeRec> docTypeList) {
  this.docTypeList = docTypeList;
 }

 public Date getDueDateFr() {
  return dueDateFr;
 }

 public void setDueDateFr(Date dueDateFr) {
  this.dueDateFr = dueDateFr;
 }

 public Date getDueDateTo() {
  return dueDateTo;
 }

 public void setDueDateTo(Date dueDateTo) {
  this.dueDateTo = dueDateTo;
 }

 
 
 public String getFiscPerFr() {
  return fiscPerFr;
 }

 public void setFiscPerFr(String fiscPerFr) {
  this.fiscPerFr = fiscPerFr;
 }

 public String getFiscPerTo() {
  return fiscPerTo;
 }

 public void setFiscPerTo(String fiscPerTo) {
  this.fiscPerTo = fiscPerTo;
 }

 public String getFiscYearFr() {
  return fiscYearFr;
 }

 public void setFiscYearFr(String fiscYearFr) {
  this.fiscYearFr = fiscYearFr;
 }

 public String getFiscYearTo() {
  return fiscYearTo;
 }

 public void setFiscYearTo(String fiscYearTo) {
  this.fiscYearTo = fiscYearTo;
 }

 public List<PaymentTypeRec> getPaymentTypeList() {
  return paymentTypeList;
 }

 public void setPaymentTypeList(List<PaymentTypeRec> paymentTypeList) {
  this.paymentTypeList = paymentTypeList;
 }

 public int getPayStatus() {
  return payStatus;
 }

 public void setPayStatus(int payStatus) {
  this.payStatus = payStatus;
 }

 public Date getPostDateFr() {
  return postDateFr;
 }

 public void setPostDateFr(Date postDateFr) {
  this.postDateFr = postDateFr;
 }

 public Date getPostDateTo() {
  return postDateTo;
 }

 public void setPostDateTo(Date postDateTo) {
  this.postDateTo = postDateTo;
 }

 
}
