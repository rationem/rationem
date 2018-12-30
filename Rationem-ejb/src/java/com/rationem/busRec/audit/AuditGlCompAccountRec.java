/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.audit;

import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class AuditGlCompAccountRec extends AuditBaseRec {
  private char usrAction;
  private String source;
  private FiGlAccountCompRec compAccount;
  private String fieldName;
  private String newValue;
  private String oldValue;

  public AuditGlCompAccountRec() {
  }

  public AuditGlCompAccountRec(Long id, Date auditDate, UserRec createdBy, char usrAction, String source, FiGlAccountCompRec compAccount, String fieldName, String newValue, String oldValue) {
    this.usrAction = usrAction;
    this.source = source;
    this.compAccount = compAccount;
    this.fieldName = fieldName;
    this.newValue = newValue;
    this.oldValue = oldValue;
  }


}
