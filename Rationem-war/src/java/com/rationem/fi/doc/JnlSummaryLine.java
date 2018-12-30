/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.doc;

/**
 * Summary line for display in document summary
 * The line number must agree to actual document line number.
 * Only for GUI display
 * @author Chris
 */
public class JnlSummaryLine {
  private  Long lineNum;
  private String glAccountNumber;
  private String accountDescr;
  private String debit;
  private String credit;

  public JnlSummaryLine() {
  }

  public String getAccountDescr() {
    return accountDescr;
  }

  public void setAccountDescr(String accountDescr) {
    this.accountDescr = accountDescr;
  }

  public String getCredit() {
    return credit;
  }

  public void setCredit(String credit) {
    this.credit = credit;
  }

  public String getDebit() {
    return debit;
  }

  public void setDebit(String debit) {
    this.debit = debit;
  }

  public String getGlAccountNumber() {
    return glAccountNumber;
  }

  public void setGlAccountNumber(String glAccountNumber) {
    this.glAccountNumber = glAccountNumber;
  }

  public Long getLineNum() {
    return lineNum;
  }

  public void setLineNum(Long lineNum) {
    this.lineNum = lineNum;
  }

  


}
