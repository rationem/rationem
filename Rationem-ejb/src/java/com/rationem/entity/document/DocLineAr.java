/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.fi.arap.FiArPeriodBalance;
import java.util.Date;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.fi.arap.ArBankAccount;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;


import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocLineAR")
@PrimaryKeyJoinColumn(name="doc_line_id",referencedColumnName = "doc_line_id")
@Table(name="doc_line03")

@NamedNativeQuery(name = "giftAid", query = "select d1.* from doc_Line03 l3, doc_line01 l1,  "
        + "doc02 d2, doc01 d1"
    +" where l3.doc_line_id = l1.doc_line_id and l1.doc_id = d2.doc_id and l3.GIFT_AID = 1 and d2.doc_id = d1.doc_id"
    +" and d2.fiscaL_YEAR = ? AND d2.FISCAL_PERIOD >= ? and d2.FISCAL_PERIOD <= ?", 
    resultClass = DocFi.class)
@NamedQueries({
 @NamedQuery(name="lineForDoc", query="select l from DocLineAr l where l.docHeaderBase.id = :docId "),
 @NamedQuery(name="openLinesforArAcnt", query="select l from DocLineAr l where l.arAccount.id = :acntId"),
 @NamedQuery(name="ArPayRun0", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "),
 @NamedQuery(name="ArPayRun1", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo"),
 @NamedQuery(name="ArPayRun2", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun3", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
       + "and l.arAccount.id in :arAcs"),
 @NamedQuery(name="ArPayRun4", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.postType.id in :pstTypes"),
 @NamedQuery(name="ArPayRun5", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo "
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun6", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo "
        + "and l.arAccount.id in :arAcs"),
 @NamedQuery(name="ArPayRun7", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docFi.createOn between :entryFr and :entryTo "
        + "and l.arAccount.id in :arAcs "
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun8", 
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.arAccount.id in :arAcs "
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun9",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo "
        + "and l.postType.id in :pstTypes "
        ),
 @NamedQuery(name="ArPayRun10",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo "
        + "and l.postType.id in :pstTypes "
        + "and l.arAccount.id in :arAcs " ),
 @NamedQuery(name="ArPayRun11",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.dueDate between :dueFr and :dueTo "
        + "and l.docFi.documentDate between :docFr and :docTo "
        + "and l.docFi.postingDate between :postFr and :postTo "
        + "and l.docHeaderBase.createOn between :entryFr and :entryTo "
        + "and l.postType.id in :pstTypes "
        + "and l.arAccount.id in :arAcs " 
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun12",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.postType.id in :pstTypes "
        + "and l.arAccount.id in :arAcs " 
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun13",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.postType.id in :pstTypes "
        + "and l.docFi.docType.id in :docTypeLst"),
 @NamedQuery(name="ArPayRun14",  
        query="select l from DocLineAr l where l.clearedLine = false "
        + "and l.clearingLine = false "
        + "and l.postType.id in :pstTypes "
        + "and l.arAccount.id in :arAcs ")
       
})
public class DocLineAr extends DocLineBase implements Serializable, DocLineSubLedgerIF {
 private static final long serialVersionUID = 1L;

 
 
 
 
 @Column(name="curr_id")
 private int currID;
 @Column(name="doc_amount")
  private double docAmount;
 
 @ManyToOne
 @JoinColumn(name="doc_fi_id", referencedColumnName="doc_id")
 private DocFi docFi;
 
  @ManyToOne
  @JoinColumn(name="pay_terms_id", referencedColumnName="pay_terms_id")
  private PaymentTerms payTerms;
  @ManyToOne
  @JoinColumn(name="pay_type_id", referencedColumnName="pay_type_id")
  private PaymentType payType;
  @Temporal(DATE)
  @Column(name="due_date")
  private Date dueDate;
  
  @ManyToOne
  @JoinColumn(name="ar_account_id", referencedColumnName="ar_account_id")
   private ArAccount arAccount;
  
  
  @ManyToOne
  @JoinColumn(name="ar_bank_id", referencedColumnName="ar_bank_id")
  private ArBankAccount paymntBank;

  
  @ManyToOne
  @JoinColumn(name="ar_credit_period_bal_id", referencedColumnName="ar_per_bal_id" )
  private FiArPeriodBalance arCreditPeriodBalance;
  
  
  @ManyToOne
  @JoinColumn(name="ar_debit_period_bal_id", referencedColumnName="ar_per_bal_id" )
  private FiArPeriodBalance arDebitPeriodBalance;
  
  @Column(name="gift_aid")
  private boolean giftAid;
  
  @OneToMany(mappedBy = "reconcilForArLine")
  List<DocLineGl> reconiliationLines;
  
  //@ManyToOne
  
  
  @ManyToOne
  @JoinColumn(name="bnk_pay_run_line_id", referencedColumnName="bank_trans_id" )
  private DocBankLineBase bankPayRunLine;

  
  public ArAccount getArAccount() {
   
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 public FiArPeriodBalance getArCreditPeriodBalance() {
  return arCreditPeriodBalance;
 }

 public void setArCreditPeriodBalance(FiArPeriodBalance arCreditPeriodBalance) {
  this.arCreditPeriodBalance = arCreditPeriodBalance;
 }

 public FiArPeriodBalance getArDebitPeriodBalance() {
  return arDebitPeriodBalance;
 }

 public void setArDebitPeriodBalance(FiArPeriodBalance arDebitPeriodBalance) {
  this.getComp();
  this.arDebitPeriodBalance = arDebitPeriodBalance;
 }
 
 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public DocFi getDocFi() {
  return docFi;
 }

 public void setDocFi(DocFi docFi) {
  this.docFi = docFi;
 }

 
 public PaymentTerms getPayTerms() {
  return payTerms;
 }

 public void setPayTerms(PaymentTerms payTerms) {
  this.payTerms = payTerms;
 }

 public PaymentType getPayType() {
  return payType;
 }

 public void setPayType(PaymentType payType) {
  this.payType = payType;
 }

 public ArBankAccount getPaymntBank() {
  return paymntBank;
 }

 public void setPaymntBank(ArBankAccount paymntBank) {
  this.paymntBank = paymntBank;
 }

 public Date getDueDate() {
  return dueDate;
 }

 public void setDueDate(Date dueDate) {
  this.dueDate = dueDate;
 }

 public boolean isGiftAid() {
  return giftAid;
 }

 public void setGiftAid(boolean giftAid) {
  this.giftAid = giftAid;
 }

 public List<DocLineGl> getReconiliationLines() {
  return reconiliationLines;
 }

 public void setReconiliationLines(List<DocLineGl> reconiliationLines) {
  this.reconiliationLines = reconiliationLines;
 }

 public DocBankLineBase getBankPayRunLine() {
  return bankPayRunLine;
 }

 public void setBankPayRunLine(DocBankLineBase bankPayRunLine) {
  this.bankPayRunLine = bankPayRunLine;
 }

  
 
}
