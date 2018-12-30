/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PaymentTypeByCode implements Comparator<PaymentTypeRec> {
 @Override
 public int compare(PaymentTypeRec p1, PaymentTypeRec p2) {
  
  return p1.getPayTypeCode().compareTo(p2.getPayTypeCode());
  
 }
 
}
