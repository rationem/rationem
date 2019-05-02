/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.List;

/**
 *
 * @author Chris
 */
public class BankAccountCompanyRec extends BankAccountRec {
  private double balance;
  private double clearedBalance;
  private List<NumberRangeChequeRec> chequeBooks;
  private CompanyBasicRec comp;
  private List<FiGlAccountCompRec> glAccounts;
  private List<FiGlAccountCompRec> unclearedGlAccounts;
  private FiGlAccountCompRec clearedGlAccount; 
  private boolean hasChqTemplate;
  private ChequeTemplateRec chequeTemplate;
  private int chqueNumlen;
  

 public double getBalance() {
  return balance;
 }

 public void setBalance(double balance) {
  this.balance = balance;
 }

 public ChequeTemplateRec getChequeTemplate() {
  return chequeTemplate;
 }

 public void setChequeTemplate(ChequeTemplateRec chequeTemplate) {
  this.chequeTemplate = chequeTemplate;
 }

 

 
 public double getClearedBalance() {
  return clearedBalance;
 }

 public void setClearedBalance(double clearedBalance) {
  this.clearedBalance = clearedBalance;
 }

 public List<NumberRangeChequeRec> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeChequeRec> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 public int getChqueNumlen() {
  return chqueNumlen;
 }

 public void setChqueNumlen(int chqueNumlen) {
  this.chqueNumlen = chqueNumlen;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public List<FiGlAccountCompRec> getGlAccounts() {
  return glAccounts;
 }

 public void setGlAccounts(List<FiGlAccountCompRec> glAccounts) {
  this.glAccounts = glAccounts;
 }

 
 public boolean isHasChqTemplate() {
  return hasChqTemplate;
 }

 public void setHasChqTemplate(boolean hasChqTemplate) {
  this.hasChqTemplate = hasChqTemplate;
 }

 
 public List<FiGlAccountCompRec> getUnclearedGlAccounts() {
  return unclearedGlAccounts;
 }

 public void setUnclearedGlAccounts(List<FiGlAccountCompRec> unclearedGlAccountsunclearedGlAccounts) {
  this.unclearedGlAccounts = unclearedGlAccountsunclearedGlAccounts;
 }

 public FiGlAccountCompRec getClearedGlAccount() {
  return clearedGlAccount;
 }

 public void setClearedGlAccount(FiGlAccountCompRec clearedGlAccount) {
  this.clearedGlAccount = clearedGlAccount;
 }

 
 

 

 
}
