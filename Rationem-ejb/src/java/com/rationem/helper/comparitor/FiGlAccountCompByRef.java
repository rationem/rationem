/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class FiGlAccountCompByRef implements Comparator<FiGlAccountCompRec>{

 @Override
 public int compare(FiGlAccountCompRec ac1, FiGlAccountCompRec ac2) {
  String ref1 = ac1.getCoaAccount().getRef();
  String ref2 = ac2.getCoaAccount().getRef();
  return ref1.compareTo(ref2);
  
 }
 
}
