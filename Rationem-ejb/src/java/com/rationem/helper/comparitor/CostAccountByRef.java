/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class CostAccountByRef implements Comparator<CostAccountDirectRec> {
 
 @Override
 public int compare(CostAccountDirectRec ac1, CostAccountDirectRec ac2) {
  String ref1 = ac1.getRef();
  String ref2 = ac2.getRef();
  return ref1.compareTo(ref2);
  
 }
 
}
