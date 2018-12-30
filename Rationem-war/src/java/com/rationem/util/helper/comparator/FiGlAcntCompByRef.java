/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class FiGlAcntCompByRef implements Comparator<FiGlAccountCompRec> {
 @Override
 public int compare(FiGlAccountCompRec acnt1, FiGlAccountCompRec acnt2){
  return acnt1.getCoaAccount().getRef().compareTo(acnt2.getCoaAccount().getRef());
  
 }
 
}
