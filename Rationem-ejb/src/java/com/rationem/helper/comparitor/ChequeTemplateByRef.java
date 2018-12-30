/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;
import com.rationem.busRec.tr.ChequeTemplateRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ChequeTemplateByRef implements Comparator<ChequeTemplateRec> {
 
 @Override
 public int compare(ChequeTemplateRec templ1, ChequeTemplateRec templ2) {
 
  return templ1.getReference().compareTo(templ2.getReference());
    
 }
 
}
