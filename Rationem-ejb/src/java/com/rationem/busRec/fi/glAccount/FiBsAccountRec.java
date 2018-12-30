/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.glAccount;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.company.AccountTypeRec;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class FiBsAccountRec extends FiGlAccountBaseRec {

  private boolean balFwd;
  
  public FiBsAccountRec() {
  }

  public FiBsAccountRec(Long id, ChartOfAccountsRec chartOfAccounts, String ref, String name, 
          String description, boolean pl, AccountTypeRec accountType, Date created, Date updateDate,
          Long updateBy, int revision,  boolean balFwd,  boolean userClear) {
    this.balFwd = balFwd;
  }

  
  public boolean isBalFwd() {
    return balFwd;
  }

  public void setBalFwd(boolean balFwd) {
    this.balFwd = balFwd;
  }

  

  

  

}
