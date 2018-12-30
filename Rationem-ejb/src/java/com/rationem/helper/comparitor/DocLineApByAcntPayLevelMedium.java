/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.doc.DocLineApRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class DocLineApByAcntPayLevelMedium implements Comparator<DocLineApRec> {
 
 @Override
 public int compare(DocLineApRec l1, DocLineApRec l2){
  
  int apAcntRc = l1.getAccountRef().compareTo(l2.getAccountRef());
  if(apAcntRc != 0){
   return apAcntRc;
  }
  int payTyLv = 0;
  if(l1.getPayType().getSummLevel() < l2.getPayType().getSummLevel() ){
   payTyLv = -1;
  }else if(l1.getPayType().getSummLevel() > l2.getPayType().getSummLevel() ){
   payTyLv = 1;
  }else{
   payTyLv = 0;
  }
  if(payTyLv != 0){
   return payTyLv;
  }
  
  return l1.getPayType().getPayMedium().compareTo(l2.getPayType().getPayMedium());
  
 }
}
