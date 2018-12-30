/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;
import com.rationem.busRec.fi.company.FundRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class FiFundByCodeAsc implements Comparator<FundRec> {
 
 @Override
 public int compare(FundRec f1, FundRec f2){
  String code1 = f1.getFndCode();
  String code2 = f2.getFndCode();
  
  return code1.compareTo(code2);
  
 }
 
}
