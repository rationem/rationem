/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.arap;

import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class FiArPeriodBalanceRec implements Serializable {
  private Long id;
    private int balYear;
    private int balPeriod;
    private ArAccountRec arAccount;
    private ArrayList<DocLineArRec> debitDocLines;
    private ArrayList<DocLineArRec> creditDocLines;
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

  public FiArPeriodBalanceRec() {
  }

  public ArAccountRec getArAccount() {
    return arAccount;
  }

  public void setArAccount(ArAccountRec arAccount) {
    this.arAccount = arAccount;
  }

  public int getBalPeriod() {
    return balPeriod;
  }

  public void setBalPeriod(int balPeriod) {
    this.balPeriod = balPeriod;
  }

  public int getBalYear() {
    return balYear;
  }

  public void setBalYear(int balYear) {
    this.balYear = balYear;
  }

  public double getBfwdDocAmount() {
    return bfwdDocAmount;
  }

  public void setBfwdDocAmount(double bfwdDocAmount) {
    this.bfwdDocAmount = bfwdDocAmount;
  }

  public double getBfwdLocalAmount() {
    return bfwdLocalAmount;
  }

  public void setBfwdLocalAmount(double bfwdLocalAmount) {
    this.bfwdLocalAmount = bfwdLocalAmount;
  }

  public double getCfwdDocAmount() {
    return cfwdDocAmount;
  }

  public void setCfwdDocAmount(double cfwdDocAmount) {
    this.cfwdDocAmount = cfwdDocAmount;
  }

  public double getCfwdLocalAmount() {
    return cfwdLocalAmount;
  }

  public void setCfwdLocalAmount(double cfwdLocalAmount) {
    this.cfwdLocalAmount = cfwdLocalAmount;
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

  public ArrayList<DocLineArRec> getCreditDocLines() {
    return creditDocLines;
  }

  public void setCreditDocLines(ArrayList<DocLineArRec> creditDocLines) {
    this.creditDocLines = creditDocLines;
  }

  public ArrayList<DocLineArRec> getDebitDocLines() {
    return debitDocLines;
  }

  public void setDebitDocLines(ArrayList<DocLineArRec> debitDocLines) {
    this.debitDocLines = debitDocLines;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public double getPeriodDocAmount() {
    return periodDocAmount;
  }

  public void setPeriodDocAmount(double periodDocAmount) {
    this.periodDocAmount = periodDocAmount;
  }

  public double getPeriodLocalAmount() {
    return periodLocalAmount;
  }

  public void setPeriodLocalAmount(double periodLocalAmount) {
    this.periodLocalAmount = periodLocalAmount;
  }

  public double getPeriodTurnover() {
    return periodTurnover;
  }

  public void setPeriodTurnover(double periodTurnover) {
    this.periodTurnover = periodTurnover;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public UserRec getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(UserRec updateBy) {
    this.updateBy = updateBy;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }




}
