/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ChequeBookByShrtDescr implements Comparator<NumberRangeChequeRec> {
 
 @Override
 public int compare(NumberRangeChequeRec chkBk1,NumberRangeChequeRec chkBk2){
  return chkBk1.getShortDescr().compareTo(chkBk2.getShortDescr());
 }
}
