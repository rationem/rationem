/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.util.ArAcntBalChkRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ArAccountBalById implements Comparator<ArAcntBalChkRec>{
 
 @Override
 public int compare(ArAcntBalChkRec b1, ArAcntBalChkRec b2){
  return b1.getAccountId().compareTo(b2.getAccountId());
 }
}
