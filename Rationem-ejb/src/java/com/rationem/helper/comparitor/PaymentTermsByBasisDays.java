/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PaymentTermsByBasisDays implements Comparator<PaymentTermsRec> {
 @Override
 public int compare(PaymentTermsRec pt1, PaymentTermsRec pt2) {
  
  // check base types
  int comp = pt1.getBaseType().compareTo(pt2.getBaseType());
  
  if(comp == 0){
   //base types are the same so days
   Integer days1 = pt1.getDays();
   Integer days2 = pt2.getDays();
   comp = days1.compareTo(days2);
  }
  
  
   return comp;
 }
 
}
