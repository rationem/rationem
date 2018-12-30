/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.glAccount;

import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class FiPeriodBalanceRec implements Serializable {
 public static final int GL_ACT = 0;
 public static final int RESTRICTED_ACT = 1;
 public static final int GL_BUDGET = 2;
 public static final int RESTRICTED_BUDGET = 3;
 public static final int CC_ACT = 4;
 public static final int CC_BUDGET = 5;
 public static final int PROG_ACT = 6;
 public static final int PROG_BUDGET = 7;
 
 private Long id;
 private int balYear;
 private int balPeriod;
 private int balanceType;
 private FiGlAccountCompRec fiGlAccountComp;
 private double bfwdLocalAmount;
 private double bfwdDocAmount;
 private double periodLocalAmount;
    private double periodDocAmount;
    private double cfwdLocalAmount;
    private double cfwdDocAmount;
    private double periodBudgetAmount;
    private double periodCreditAmount;
    private double periodDebitAmount;
    private ProgramAccountRec programBudgetAccount;
    private ProgramAccountRec programCostAccount;
    private Date created;
    private UserRec createdBy;
    private Date updateDate;
    private UserRec updateBy;
    private int revision;
    private ArrayList<DocLineGlRec> debitDocLines;
    private ArrayList<DocLineGlRec> creditDocLines;
    private ArrayList<DocLineGlRec> restrictedDebitDocLines;
    private ArrayList<DocLineGlRec> restrictedCreditDocLines;

    private FiPeriodBalanceRec glPerBal;
    private ArrayList<FiPeriodBalanceRec> restrPerBals;
    private FundRec restrictedFund;

  public FiPeriodBalanceRec() {
  }

  public FiPeriodBalanceRec(Long id, int balYear, int balPeriod, int balanceType, FiGlAccountCompRec fiGlAccountComp, double bfwdLocalAmount, double bfwdDocAmount, double periodLocalAmount, double periodDocAmount, double cfwdLocalAmount, double cfwdDocAmount, double periodBudgetAmount, Date created, UserRec createdBy, Date updateDate, UserRec updateBy, int revision) {
    this.id = id;
    this.balYear = balYear;
    this.balPeriod = balPeriod;
    this.balanceType = balanceType;
    this.fiGlAccountComp = fiGlAccountComp;
    this.bfwdLocalAmount = bfwdLocalAmount;
    this.bfwdDocAmount = bfwdDocAmount;
    this.periodLocalAmount = periodLocalAmount;
    this.periodDocAmount = periodDocAmount;
    this.cfwdLocalAmount = cfwdLocalAmount;
    this.cfwdDocAmount = cfwdDocAmount;
    this.periodBudgetAmount = periodBudgetAmount;
    this.created = created;
    this.createdBy = createdBy;
    this.updateDate = updateDate;
    this.updateBy = updateBy;
    this.revision = revision;
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

  public int getBalanceType() {
    return balanceType;
  }

  public void setBalanceType(int balanceType) {
    this.balanceType = balanceType;
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

  public FiGlAccountCompRec getFiGlAccountComp() {
    return fiGlAccountComp;
  }

  public void setFiGlAccountComp(FiGlAccountCompRec fiGlAccountComp) {
    this.fiGlAccountComp = fiGlAccountComp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getPeriodBudgetAmount() {
    return periodBudgetAmount;
  }

  public void setPeriodBudgetAmount(double periodBudgetAmount) {
    this.periodBudgetAmount = periodBudgetAmount;
  }

 public double getPeriodCreditAmount() {
  return periodCreditAmount;
 }

 public void setPeriodCreditAmount(double periodCreditAmount) {
  this.periodCreditAmount = periodCreditAmount;
 }

  public double getPeriodDocAmount() {
    return periodDocAmount;
  }

  public void setPeriodDocAmount(double periodDocAmount) {
    this.periodDocAmount = periodDocAmount;
  }

 public double getPeriodDebitAmount() {
  return periodDebitAmount;
 }

 public void setPeriodDebitAmount(double periodDebitAmount) {
  this.periodDebitAmount = periodDebitAmount;
 }

  public double getPeriodLocalAmount() {
    return periodLocalAmount;
  }

  public void setPeriodLocalAmount(double periodLocalAmount) {
    this.periodLocalAmount = periodLocalAmount;
  }

 public ProgramAccountRec getProgramBudgetAccount() {
  return programBudgetAccount;
 }

 public void setProgramBudgetAccount(ProgramAccountRec programBudgetAccount) {
  this.programBudgetAccount = programBudgetAccount;
 }

 public ProgramAccountRec getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccountRec programCostAccount) {
  this.programCostAccount = programCostAccount;
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

  public ArrayList<DocLineGlRec> getCreditDocLines() {
    return creditDocLines;
  }

  public void setCreditDocLines(ArrayList<DocLineGlRec> creditDocLines) {
    this.creditDocLines = creditDocLines;
  }

  public ArrayList<DocLineGlRec> getDebitDocLines() {
    return debitDocLines;
  }

  public void setDebitDocLines(ArrayList<DocLineGlRec> debitDocLines) {
    this.debitDocLines = debitDocLines;
  }

  public ArrayList<DocLineGlRec> getRestrictedCreditDocLines() {
    return restrictedCreditDocLines;
  }

  public void setRestrictedCreditDocLines(ArrayList<DocLineGlRec> restrictedCreditDocLines) {
    this.restrictedCreditDocLines = restrictedCreditDocLines;
  }

  public ArrayList<DocLineGlRec> getRestrictedDebitDocLines() {
    return restrictedDebitDocLines;
  }

  public void setRestrictedDebitDocLines(ArrayList<DocLineGlRec> restrictedDebitDocLines) {
    this.restrictedDebitDocLines = restrictedDebitDocLines;
  }

  public FiPeriodBalanceRec getGlPerBal() {
    return glPerBal;
  }

  public void setGlPerBal(FiPeriodBalanceRec glPerBal) {
    this.glPerBal = glPerBal;
  }

  public ArrayList<FiPeriodBalanceRec> getRestrPerBals() {
    return restrPerBals;
  }

  public void setRestrPerBals(ArrayList<FiPeriodBalanceRec> restrPerBals) {
    this.restrPerBals = restrPerBals;
  }

  public FundRec getRestrictedFund() {
    return restrictedFund;
  }

  public void setRestrictedFund(FundRec restrictedFund) {
    this.restrictedFund = restrictedFund;
  }

  


}
