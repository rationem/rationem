/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.fi.arap;

import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class FiApPeriodBalanceRec {
 private Long id;
 private int balYear;
 private int balPeriod;
 private ApAccountRec apAccount;
 private List<DocLineApRec> debitDocLines;
 private List<DocLineApRec> creditDocLines;
 private double bfwdLocalAmount;
 private double periodTurnover;
 private double bfwdDocAmount;
 private double periodLocalAmount;
 private double periodDocAmount;
 private double periodCreditAmount;
 private double periodDebitAmount;
 private double cfwdLocalAmount;
 private double cfwdDocAmount;
 private Date created;
 private UserRec createdBy;
 private Date updateDate;
 private UserRec updateBy;
 private int revision;

 public FiApPeriodBalanceRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public int getBalYear() {
  return balYear;
 }

 public void setBalYear(int balYear) {
  this.balYear = balYear;
 }

 public String getBalYearPeriod(){
  String yrPer = String.valueOf(balYear) + String.valueOf(balPeriod);
  return yrPer;
 }
 
 public int getBalPeriod() {
  return balPeriod;
 }

 public void setBalPeriod(int balPeriod) {
  this.balPeriod = balPeriod;
 }

 public ApAccountRec getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 public List<DocLineApRec> getDebitDocLines() {
  return debitDocLines;
 }

 public void setDebitDocLines(List<DocLineApRec> debitDocLines) {
  this.debitDocLines = debitDocLines;
 }

 public List<DocLineApRec> getCreditDocLines() {
  return creditDocLines;
 }

 public void setCreditDocLines(List<DocLineApRec> creditDocLines) {
  this.creditDocLines = creditDocLines;
 }

 public double getBfwdLocalAmount() {
  return bfwdLocalAmount;
 }

 public void setBfwdLocalAmount(double bfwdLocalAmount) {
  this.bfwdLocalAmount = bfwdLocalAmount;
 }

 public double getPeriodTurnover() {
  return periodTurnover;
 }

 public void setPeriodTurnover(double periodTurnover) {
  this.periodTurnover = periodTurnover;
 }

 public double getBfwdDocAmount() {
  return bfwdDocAmount;
 }

 public void setBfwdDocAmount(double bfwdDocAmount) {
  this.bfwdDocAmount = bfwdDocAmount;
 }

 public double getPeriodLocalAmount() {
  return periodLocalAmount;
 }

 public void setPeriodLocalAmount(double periodLocalAmount) {
  this.periodLocalAmount = periodLocalAmount;
 }

 public double getPeriodDocAmount() {
  return periodDocAmount;
 }

 public void setPeriodDocAmount(double periodDocAmount) {
  this.periodDocAmount = periodDocAmount;
 }

 public double getPeriodCreditAmount() {
  return periodCreditAmount;
 }

 public void setPeriodCreditAmount(double periodCreditAmount) {
  this.periodCreditAmount = periodCreditAmount;
 }

 public double getPeriodDebitAmount() {
  return periodDebitAmount;
 }

 public void setPeriodDebitAmount(double periodDebitAmount) {
  this.periodDebitAmount = periodDebitAmount;
 }

 public double getCfwdLocalAmount() {
  return cfwdLocalAmount;
 }

 public void setCfwdLocalAmount(double cfwdLocalAmount) {
  this.cfwdLocalAmount = cfwdLocalAmount;
 }

 public double getCfwdDocAmount() {
  return cfwdDocAmount;
 }

 public void setCfwdDocAmount(double cfwdDocAmount) {
  this.cfwdDocAmount = cfwdDocAmount;
 }

 public Date getCreated() {
  return created;
 }

 public void setCreated(Date created) {
  this.created = created;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public UserRec getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(UserRec updateBy) {
  this.updateBy = updateBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }
 
 
}
