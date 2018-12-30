/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.doc.DocLineApRec;
import java.util.Comparator;

/**
 * Sort AP lines by account code and posting date
 * @author user
 */
public class ApLineByAcntPstDt implements Comparator<DocLineApRec> {
 @Override
 public int compare(DocLineApRec ln1, DocLineApRec ln2) {
  int status = 0;
  status = ln1.getApAccount().getAccountCode().compareTo(ln2.getApAccount().getAccountCode());
  if(status == 0){
   
   status = ln1.getDocFi().getFisYearPeriod().compareTo(ln2.getDocFi().getFisYearPeriod());
  }
  return status;
  
 }
 
}
