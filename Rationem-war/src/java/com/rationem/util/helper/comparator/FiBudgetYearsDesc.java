/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class FiBudgetYearsDesc implements Comparator<Integer> {
 @Override
 public int compare(Integer yr1, Integer yr2){
  return yr2.compareTo(yr1);
 }
}
