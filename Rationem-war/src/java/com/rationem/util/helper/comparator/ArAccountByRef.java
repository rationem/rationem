/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.fi.arap.ArAccountRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ArAccountByRef implements Comparator<ArAccountRec> {
 @Override
 public int compare(ArAccountRec a1, ArAccountRec a2) {
  
  return a1.getArAccountCode().compareTo(a2.getArAccountCode());
  
 }
 
}
