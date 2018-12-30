/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class VatRegByStartDateRev implements Comparator<VatRegistrationRec> {
 @Override
 public int compare(VatRegistrationRec v1, VatRegistrationRec v2){
  int rc = v1.getRegistrationDate().compareTo(v2.getRegistrationDate());
  if(rc > 0){
   rc = -1;
  }else if(rc < 0){
   rc = 1;
  }
  
  return rc;
 }
}
