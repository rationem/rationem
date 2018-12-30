/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.config.common.SortOrderRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class SortOrderByName implements Comparator<SortOrderRec>{
 @Override
 public int compare(SortOrderRec p1, SortOrderRec p2) {
  return p1.getName().compareTo(p2.getName());
 }
}
