/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.audit;

import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class AuditGlAccountRec extends AuditBaseRec {
  private char usrAction;
    /**
     * page user was on when the event occurred
     */
    private String source;

    private FiGlAccountBaseRec glAccount;
    private String fieldName;
    private String oldValue;
    private String newValue;

    private ChartOfAccountsRec coa;

    private long comp;

  public AuditGlAccountRec() {
  }

  public AuditGlAccountRec(Long id, Date auditDate, UserRec createdBy, char usrAction, String source,
          FiGlAccountBaseRec glAccount, String fieldName, String oldValue, String newValue,
          ChartOfAccountsRec coa, long comp) {
    this.usrAction = usrAction;
    this.source = source;
    this.glAccount = glAccount;
    this.fieldName = fieldName;
    this.oldValue = oldValue;
    this.newValue = newValue;
    this.coa = coa;
    this.comp = comp;
  }



  public AuditGlAccountRec(char usrAction, String source, FiGlAccountBaseRec glAccount, String fieldName, String oldValue, String newValue, ChartOfAccountsRec coa, long comp) {
    this.usrAction = usrAction;
    this.source = source;
    this.glAccount = glAccount;
    this.fieldName = fieldName;
    this.oldValue = oldValue;
    this.newValue = newValue;
    this.coa = coa;
    this.comp = comp;
  }

  public ChartOfAccountsRec getCoa() {
    return coa;
  }

  public void setCoa(ChartOfAccountsRec coa) {
    this.coa = coa;
  }

  public long getComp() {
    return comp;
  }

  public void setComp(long comp) {
    this.comp = comp;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public FiGlAccountBaseRec getGlAccount() {
    return glAccount;
  }

  public void setGlAccount(FiGlAccountBaseRec glAccount) {
    this.glAccount = glAccount;
  }

  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public char getUsrAction() {
    return usrAction;
  }

  public void setUsrAction(char usrAction) {
    this.usrAction = usrAction;
  }

   


}
