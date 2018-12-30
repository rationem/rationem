/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.fi.arap.ArAccountRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ArAccountByRef implements Comparator<ArAccountRec> {
 
 @Override
 public int compare(ArAccountRec ac1, ArAccountRec ac2) {
   return ac1.getArAccountCode().compareTo(ac2.getArAccountCode());
 }
 
}
