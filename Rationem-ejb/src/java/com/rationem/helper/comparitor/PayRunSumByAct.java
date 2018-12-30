/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.helper.PayRunSumAct;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PayRunSumByAct implements Comparator<PayRunSumAct>{
 
 @Override
 public int compare(PayRunSumAct sum1,PayRunSumAct sum2){
  return sum1.getAccountRef().compareTo(sum2.getAccountRef());
 }
}
