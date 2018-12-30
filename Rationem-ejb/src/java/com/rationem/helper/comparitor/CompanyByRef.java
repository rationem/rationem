/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class CompanyByRef implements Comparator<CompanyBasicRec> {
 
 @Override
 public int compare(CompanyBasicRec c1,CompanyBasicRec c2){
  return c1.getReference().compareTo(c2.getReference());
 }
}
