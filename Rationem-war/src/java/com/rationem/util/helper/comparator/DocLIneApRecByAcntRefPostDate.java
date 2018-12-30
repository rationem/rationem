/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.doc.DocLineApRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class DocLIneApRecByAcntRefPostDate implements Comparator<DocLineApRec> {
 
 @Override
 public int compare(DocLineApRec ln1,DocLineApRec ln2){
  int rslt = ln1.getAccountRef().compareTo(ln2.getAccountRef());
  if(rslt == 0){
   return ln1.getDocFi().getPostingDate().compareTo(ln2.getDocFi().getPostingDate());
  }else{
   return rslt;
  }
 }
}
