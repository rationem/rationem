/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.arap;

import com.rationem.entity.document.DocLineAr;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import java.io.Serializable;
import javax.persistence.Column;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.SequenceGenerator;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account10")
@NamedQueries({
 @NamedQuery(name="arAnnBals", query="Select b from FiArPeriodBalance b "
  + "where b.arAccount.id = :acntId and b.balYear = :yr ") ,
 @NamedQuery(name="arPerBal", query="Select b from FiArPeriodBalance b "
  + "where b.arAccount.id = :acntId and b.balYear = :yr and b.balPeriod = :per ")
})
@SequenceGenerator(name = "fiArPeriodBalance_s1", sequenceName = "fi_account10_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class FiArPeriodBalance implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "fiArPeriodBalance_s1")
 @Column(name="ar_per_bal_id")
 private Long id;
 @Column(name="balance_year")
 private int balYear;
 @Column(name="balance_period")
 private int balPeriod;
 @ManyToOne
 @JoinColumn(name="ap_account_id", referencedColumnName="ap_account_id")
 private ApAccount apAccount;
 @ManyToOne
 @JoinColumn(name="ar_account_id", referencedColumnName="ar_account_id")
 private ArAccount arAccount;
 @OneToMany(mappedBy = "arDebitPeriodBalance")
 private List<DocLineAr> debitDocLines;
 @OneToMany(mappedBy = "arCreditPeriodBalance")
 private List<DocLineAr> creditDocLines;
 @Column(name="amount_fwd_local")
 private double bfwdLocalAmount;
 @Column(name="period_turnover")
 private double periodTurnover;
 @Column(name="amount_fwd_doc") 
 private double bfwdDocAmount;
 @Column(name="period_amount_local")
 private double periodLocalAmount;
 @Column(name="period_amount_doc")
 private double periodDocAmount;
 @Column(name="period_credit_amount_doc")
 private double periodCreditAmount;
 @Column(name="period_debit_amount_doc")
 private double periodDebitAmount;
 @Column(name="amount_cfwd_local")
 private double cfwdLocalAmount;
 @Column(name="amount_cfwd_doc")
 private double cfwdDocAmount;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date created;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date updateDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User updateBy;
 @Version
 @Column(name="changs")
 private int revision;

  public FiArPeriodBalance() {
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

 public int getBalPeriod() {
  return balPeriod;
 }

 public void setBalPeriod(int balPeriod) {
  this.balPeriod = balPeriod;
 }

 public ApAccount getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccount apAccount) {
  this.apAccount = apAccount;
 }

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 public List<DocLineAr> getDebitDocLines() {
  return debitDocLines;
 }

 public void setDebitDocLines(List<DocLineAr> debitDocLines) {
  this.debitDocLines = debitDocLines;
 }

 public List<DocLineAr> getCreditDocLines() {
  return creditDocLines;
 }

 public void setCreditDocLines(List<DocLineAr> creditDocLines) {
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

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public User getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(User updateBy) {
  this.updateBy = updateBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 


 
}
