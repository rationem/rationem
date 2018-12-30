/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.tr.BankRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class BankByBankCode implements Comparator<BankRec>{

@Override
 public int compare(BankRec b1,BankRec b2 ){
  return b1.getBankCode().compareTo(b2.getBankCode());
 }
}
