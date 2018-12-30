/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class BankStatementRec {
 private Long id;
 private CompanyBasicRec comp;
 private BankAccountCompanyRec bankAc;
 int statNum;
 String reference;
 Date startDate;
 Date endDate;
 double openingBalance;
 double closingBalance;
 boolean complete;
 List<DocBankLineBaseRec> bankItem;
 UserRec createdBy;
 Date createdOn;
 UserRec changedBy;
 Date changedOn;
 long changes;

 public BankStatementRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public BankAccountCompanyRec getBankAc() {
  return bankAc;
 }

 public void setBankAc(BankAccountCompanyRec bankAc) {
  this.bankAc = bankAc;
 }

 public int getStatNum() {
  return statNum;
 }

 public void setStatNum(int statNum) {
  this.statNum = statNum;
 }

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public Date getStartDate() {
  return startDate;
 }

 public void setStartDate(Date startDate) {
  this.startDate = startDate;
 }

 public Date getEndDate() {
  return endDate;
 }

 public void setEndDate(Date endDate) {
  this.endDate = endDate;
 }

 public double getOpeningBalance() {
  return openingBalance;
 }

 public void setOpeningBalance(double openingBalance) {
  this.openingBalance = openingBalance;
 }

 public double getClosingBalance() {
  return closingBalance;
 }

 public void setClosingBalance(double closingBalance) {
  this.closingBalance = closingBalance;
 }

 public boolean isComplete() {
  return complete;
 }

 public void setComplete(boolean complete) {
  this.complete = complete;
 }

 public List<DocBankLineBaseRec> getBankItem() {
  return bankItem;
 }

 public void setBankItem(List<DocBankLineBaseRec> bankItem) {
  this.bankItem = bankItem;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }
 
 
}
