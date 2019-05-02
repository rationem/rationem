/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.common.SortOrderRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class SortOrderByCode implements Comparator<SortOrderRec>{
 
 @Override
 public int compare(SortOrderRec s1, SortOrderRec s2){
  return s1.getSortCode().compareTo(s2.getSortCode());
 }
 
}
