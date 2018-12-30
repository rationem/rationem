/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.doc.DocLineGlRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class DocLineGlRecByFund implements Comparator<DocLineGlRec>{
 @Override
 public int compare(DocLineGlRec ln1,DocLineGlRec ln2){
 
  // 0 if equal
  // -1 ln1 less than ln2
  // 1 if ln1 gt ln2
  if(ln1.getRestrictedFund() == null && ln2.getRestrictedFund() == null){
   return 0;
  }else if(ln1.getRestrictedFund() == null && ln2.getRestrictedFund() != null){
   return -1;
  }else if(ln1.getRestrictedFund() != null && ln2.getRestrictedFund() == null){
   return 1;
  }else{
   return ln1.getRestrictedFund().getId().compareTo(ln1.getRestrictedFund().getId());
  }
 }
}
