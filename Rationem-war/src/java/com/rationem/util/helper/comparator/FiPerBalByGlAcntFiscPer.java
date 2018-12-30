/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class FiPerBalByGlAcntFiscPer implements Comparator<FiPeriodBalanceRec> {

 @Override
 public int compare(FiPeriodBalanceRec bal1, FiPeriodBalanceRec bal2) {
  String ref1 = bal1.getFiGlAccountComp().getCoaAccount().getRef();
  String ref2 = bal2.getFiGlAccountComp().getCoaAccount().getRef();
  int glAcntComp = ref1.compareTo(ref2);
  if(glAcntComp == 0){
   Integer per1 = bal1.getBalPeriod();
   Integer per2 = bal2.getBalPeriod();
   return per1.compareTo(per2);
  }else{
   return glAcntComp;
  }
 }
 
}
