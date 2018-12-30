/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.tr.BankAccountCompanyRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class BankAccountCompByAcntNumber implements Comparator<BankAccountCompanyRec> {
 @Override
 public int compare(BankAccountCompanyRec acnt1, BankAccountCompanyRec acnt2){
  return acnt1.getAccountNumber().compareTo(acnt2.getAccountNumber());
 }
 
}
