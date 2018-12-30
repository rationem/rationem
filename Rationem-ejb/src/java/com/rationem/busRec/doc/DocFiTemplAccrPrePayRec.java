/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.UomRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class DocFiTemplAccrPrePayRec extends DocFiTemplateBaseRec {
 public static final int REVERSING = 1;
 public static final int RECURRING = 2;
 public static final int REPEAT = 3;
 public static final int REVERSAL = 4;
 public static final int SETUP = 5;
 
 
 private String tmplTypeCode;
 private boolean complete;
 private Date nextPostDate;
 private int numRecur;
 private int numPosted;
 private UomRec recurUom;
 private int revYer;
 private int revPer;
 private int recurStartYear;
 private int recurStartPer;
 private DocBaseRec originalJnl;
 private DocBaseRec revJnl;
 private List<DocBaseRec> recurringDocs;
 private double totalCredit = 0.0;
 private double totalDebit = 0.0;
 
 
 
 

 

 public String getTmplTypeCode() {
  switch (this.getTmplType()){
   case DocFiTemplAccrPrePayRec.REVERSING :
    return "glJnlTemplRev";
   case DocFiTemplAccrPrePayRec.RECURRING :
    return "glJnlTemplRec";
   
 }
  
  return tmplTypeCode;
 }

 
 public double getTotalCredit() {
  return totalCredit;
 }

 public void setTotalCredit(double totalCredit) {
  this.totalCredit = totalCredit;
 }

 public double getTotalDebit() {
  return totalDebit;
 }

 public void setTotalDebit(double totalDebit) {
  this.totalDebit = totalDebit;
 }

 

 
 public boolean isComplete() {
  return complete;
 }

 public void setComplete(boolean complete) {
  this.complete = complete;
 }

 public Date getNextPostDate() {
  return nextPostDate;
 }

 public void setNextPostDate(Date nextPostDate) {
  this.nextPostDate = nextPostDate;
 }

 public int getNumRecur() {
  return numRecur;
 }

 public void setNumRecur(int numRecur) {
  this.numRecur = numRecur;
 }

 public int getNumPosted() {
  return numPosted;
 }

 public void setNumPosted(int numPosted) {
  this.numPosted = numPosted;
 }

 public int getRevYer() {
  return revYer;
 }

 public void setRevYer(int revYer) {
  this.revYer = revYer;
 }

 public int getRevPer() {
  return revPer;
 }

 public void setRevPer(int revPer) {
  this.revPer = revPer;
 }

 public int getRecurStartYear() {
  return recurStartYear;
 }

 public void setRecurStartYear(int recurStartYear) {
  this.recurStartYear = recurStartYear;
 }

 public int getRecurStartPer() {
  return recurStartPer;
 }

 public void setRecurStartPer(int recurStartPer) {
  this.recurStartPer = recurStartPer;
 }

 public UomRec getRecurUom() {
  return recurUom;
 }

 public void setRecurUom(UomRec recurUom) {
  this.recurUom = recurUom;
 }

 public DocBaseRec getOriginalJnl() {
  return originalJnl;
 }

 public void setOriginalJnl(DocBaseRec originalJnl) {
  this.originalJnl = originalJnl;
 }

 public DocBaseRec getRevJnl() {
  return revJnl;
 }

 public void setRevJnl(DocBaseRec revJnl) {
  this.revJnl = revJnl;
 }

 public List<DocBaseRec> getRecurringDocs() {
  return recurringDocs;
 }

 public void setRecurringDocs(List<DocBaseRec> recurringDocs) {
  this.recurringDocs = recurringDocs;
 }
 
 
}
