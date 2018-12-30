/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class ChequeListSelOpt implements Serializable {
 
 
 
 private BankAccountCompanyRec bnkAcntComp;
 private CompanyBasicRec compSel;
 private Date chqIssueFrom;
 private Date chqIssueTo;
 private String chqNumFr;
 private String chqNumTo;
 private int cashmntStat;
 
 public static final int ALL = 0;
 public static final int CLEARED = 1;
 public static final int UN_CLEARED = 2;

 public ChequeListSelOpt() {
 }

 public BankAccountCompanyRec getBnkAcntComp() {
  return bnkAcntComp;
 }

 public void setBnkAcntComp(BankAccountCompanyRec bnkAcntComp) {
  this.bnkAcntComp = bnkAcntComp;
 }

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public Date getChqIssueFrom() {
  return chqIssueFrom;
 }

 public void setChqIssueFrom(Date chqIssueFrom) {
  this.chqIssueFrom = chqIssueFrom;
 }

 public Date getChqIssueTo() {
  return chqIssueTo;
 }

 public void setChqIssueTo(Date chqIssueTo) {
  this.chqIssueTo = chqIssueTo;
 }

 public String getChqNumFr() {
  return chqNumFr;
 }

 public void setChqNumFr(String chqNumFr) {
  this.chqNumFr = chqNumFr;
 }

 public String getChqNumTo() {
  return chqNumTo;
 }

 public void setChqNumTo(String chqNumTo) {
  this.chqNumTo = chqNumTo;
 }

 public int getCashmntStat() {
  return cashmntStat;
 }

 public void setCashmntStat(int cashmntStat) {
  this.cashmntStat = cashmntStat;
 }
 
 
}


