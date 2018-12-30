/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PaymentTermsByCode implements Comparator<PaymentTermsRec> {
 @Override
 public int compare(PaymentTermsRec p1, PaymentTermsRec p2) {
  return p1.getPayTermsCode().compareTo(p2.getPayTermsCode());
 }
 
}
