/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.tr.bank;

//import com.rationem.entity.audit.AuditBankAccount;
//import com.rationem.entity.audit.AuditBankAccountCompany;
import com.rationem.entity.audit.AuditBankAccountCompany;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.NumberRangeCheque;
import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityManager;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;



/**
 *
 * @author Chris
 */
@Entity
@Table(name="bnk04")
@DiscriminatorValue("bank.BankAccountCompany")
@NamedQueries({
@NamedQuery(name="bankAcsForComp", 
        query="select b from BankAccountCompany b where b.comp.id = :compId"),
@NamedQuery(name="bankAccountForComp", 
 query="select b from BankAccountCompany b where b.comp.id = :compId and b.accountNumber = :acntNum"),
@NamedQuery(name="bankAccntCompAll", 
 query="select b from BankAccountCompany b"),
@NamedQuery(name="bankAccntCompByName", 
 query="select b from BankAccountCompany b where b.comp.id = :compId and b.accountName like :acntName"),
@NamedQuery(name="bankAccntCompByChqTempl", 
 query="select b.id from BankAccountCompany b where b.chequeTemplate.id = :chqTemplId"),

})
@PrimaryKeyJoinColumn(name="bank_account_id",referencedColumnName = "bank_account_id")
public class BankAccountCompany extends BankAccount  implements Serializable {

 @ManyToOne
 @JoinColumn(name="cheque_template_id", referencedColumnName="cheque_template_id")
 private ChequeTemplate chequeTemplate;
 private static final long serialVersionUID = 1L;
  
 @Column(name="balance")
 private double balance;
 @Column(name="chq_num_len")
 private int chequeNumLen;
 @Column(name="cleared_balance")
 private double clearedBalance;
 @Column(name="uncleared_balance")
 private double unclearedBalance;
 @OneToMany(mappedBy = "bankAccountComp")
 private List<NumberRangeCheque> chequeBooks;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic comp;
 @OneToMany(mappedBy = "payTypeForBankAccount")
 @JoinColumn(name="BANK_ACCOUNT_ID", referencedColumnName="BANK_ACCOUNT_ID")
 private List<PaymentType> paymentTypes; 
 
 @OneToMany(mappedBy = "bankAccount")
 private List<FiGlAccountComp> glAccounts;
 
 @OneToMany(mappedBy = "bankAccountCompanyUncleared")
 private List<FiGlAccountComp> unclearedGlAccounts;
 @OneToMany(mappedBy = "clearedBankAc")
 private List<DocBankLineBase> clearedBnkTrans;
 
 @OneToMany(mappedBy = "unClearedBankAc")
 private List<DocBankLineBase> unClearedBnkTrans;
 @OneToOne
 @JoinColumn(name="cleared_balance_gl_acnt_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp clearedGlAccount;
 
 @OneToMany(mappedBy = "bankAc")
 private List<BankStatement> bankStatements;
 

 

 

 @OneToMany(mappedBy = "bnkAcntComp")
 private List<AuditBankAccountCompany> auditRecords;

 public double getBalance() {
  return balance;
 }

 public void setBalance(double balance) {
  this.balance = balance;
 }

 public ChequeTemplate getChequeTemplate() {
  return chequeTemplate;
 }

 public void setChequeTemplate(ChequeTemplate chequeTemplate) {
  this.chequeTemplate = chequeTemplate;
 }

 
 public double getClearedBalance() {
  return clearedBalance;
 }

 public void setClearedBalance(double clearedBalance) {
  this.clearedBalance = clearedBalance;
 }

 public double getUnclearedBalance() {
  return unclearedBalance;
 }

 public void setUnclearedBalance(double unclearedBalance) {
  this.unclearedBalance = unclearedBalance;
 }

 public List<NumberRangeCheque> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeCheque> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 public int getChequeNumLen() {
  return chequeNumLen;
 }

 public void setChequeNumLen(int chequeNumLen) {
  this.chequeNumLen = chequeNumLen;
 }

 
 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public List<FiGlAccountComp> getGlAccounts() {
  return glAccounts;
 }

 public void setGlAccounts(List<FiGlAccountComp> glAccounts) {
  this.glAccounts = glAccounts;
 }

 

 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public List<FiGlAccountComp> getUnclearedGlAccounts() {
  return unclearedGlAccounts;
 }

 public void setUnclearedGlAccounts(List<FiGlAccountComp> unclearedGlAccounts) {
  this.unclearedGlAccounts = unclearedGlAccounts;
 }

 public List<DocBankLineBase> getClearedBnkTrans() {
  return clearedBnkTrans;
 }

 public void setClearedBnkTrans(List<DocBankLineBase> clearedBnkTrans) {
  this.clearedBnkTrans = clearedBnkTrans;
 }

 public List<DocBankLineBase> getUnClearedBnkTrans() {
  return unClearedBnkTrans;
 }

 public void setUnClearedBnkTrans(List<DocBankLineBase> unClearedBnkTrans) {
  this.unClearedBnkTrans = unClearedBnkTrans;
 }

 public FiGlAccountComp getClearedGlAccount() {
  return clearedGlAccount;
 }

 public void setClearedGlAccount(FiGlAccountComp clearedGlAccount) {
  this.clearedGlAccount = clearedGlAccount;
 }

 public List<BankStatement> getBankStatements() {
  return bankStatements;
 }

 public void setBankStatements(List<BankStatement> bankStatements) {
  this.bankStatements = bankStatements;
 }

 public List<AuditBankAccountCompany> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditBankAccountCompany> auditRecords) {
  this.auditRecords = auditRecords;
 }
 
 public void updateBalance(DocBankLineBase trans, String updateType, User usr, String source, EntityManager em){
  if(updateType.equalsIgnoreCase("N")){
   // new bank transaction.
   AuditBankAccountCompany audBal = new AuditBankAccountCompany();
   AuditBankAccountCompany audUnclBal = new AuditBankAccountCompany();
   audBal.setAuditDate(new Date());
   audBal.setBnkAcntComp(this);
   audBal.setCreatedBy(usr);
   audBal.setFieldName("Balance");
   audBal.setOldValue(String.valueOf(this.balance));
    
   audUnclBal.setAuditDate(new Date());
   audUnclBal.setBnkAcntComp(this);
   audUnclBal.setCreatedBy(usr);
   audUnclBal.setFieldName("UnclearedBalance");
   audUnclBal.setOldValue(String.valueOf(this.unclearedBalance));
   double amnt = trans.getAmount();
   if(trans.isReceipt()){
    balance = balance + amnt;
    unclearedBalance = balance + amnt;
   }else{
    balance = balance - amnt;
    unclearedBalance = balance - amnt;
   }
   audBal.setNewValue(String.valueOf(this.balance));
   audUnclBal.setOldValue(String.valueOf(this.unclearedBalance));
   audBal.setSource(source);
   audUnclBal.setSource(source);
   audBal.setUsrAction('U');
   audUnclBal.setUsrAction('U');
   em.persist(audBal);
   em.persist(audUnclBal);        
   trans.setUnClearedBankAc(this);
   //Balance change
   
  }
 }
 
}
