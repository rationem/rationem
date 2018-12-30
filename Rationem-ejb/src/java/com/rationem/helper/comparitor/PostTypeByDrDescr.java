/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.company.PostTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PostTypeByDrDescr implements Comparator<PostTypeRec> {
 @Override
 public int compare(PostTypeRec pt1, PostTypeRec pt2) {
  // 0 = equals -1 = less that +1 = 
  int result = 0;
  if(pt1.isDebit() && pt2.isDebit() ||!pt1.isDebit() && !pt2.isDebit()){
   result = 0;
  }else if(!pt1.isDebit() && pt2.isDebit()){
   result = -1;
  }else if(pt1.isDebit() && !pt2.isDebit()){
   result = 1;
  }
  if(result == 0){
   result = pt1.getDescription().compareTo(pt2.getDescription());
  }
  return result;
 }
 
}
