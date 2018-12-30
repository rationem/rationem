/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.common.NumberRangeTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class NumRangeTypeByCode implements Comparator<NumberRangeTypeRec> {
 
 @Override
 public int compare(NumberRangeTypeRec ty1,NumberRangeTypeRec ty2){
  return ty1.getNumRangeTypeCode().compareTo(ty2.getNumRangeTypeCode());
 }
}
