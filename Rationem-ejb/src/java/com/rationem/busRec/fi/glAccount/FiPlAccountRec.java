/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.glAccount;

import java.util.Date;

/**
 *
 * @author Chris
 */
public class FiPlAccountRec extends FiGlAccountBaseRec {
    private String accountCat;
    
    
    public FiPlAccountRec() {
    }

    public FiPlAccountRec(Long id, String ref, String name, String description, Date created, Date updateDate, Long updateBy, int revision, String accountCat) {
        this.accountCat = accountCat;
    }

  public String getAccountCat() {
    return accountCat;
  }

  public void setAccountCat(String accountCat) {
    this.accountCat = accountCat;
  }

  



}
