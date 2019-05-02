/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.common.NumberRangeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class NumberRangeByShrtDescr implements Comparator<NumberRangeRec> {
 
 @Override
 public int compare(NumberRangeRec r1,NumberRangeRec r2){
  return r1.getShortDescr().compareTo(r2.getShortDescr());
 }
 
}
