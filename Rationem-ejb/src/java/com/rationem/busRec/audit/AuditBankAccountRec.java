/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.audit;

import com.rationem.busRec.tr.BankAccountRec;

/**
 *
 * @author user
 */
public class AuditBankAccountRec  extends AuditBaseRec{
 
 private BankAccountRec bnkAcnt;

 public BankAccountRec getBnkAcnt() {
  return bnkAcnt;
 }

 public void setBnkAcnt(BankAccountRec bnkAcnt) {
  this.bnkAcnt = bnkAcnt;
 }
 
 
 
}
