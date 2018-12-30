/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.partner.PartnerPersonRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PartnerPersonByNameStructured implements Comparator<PartnerPersonRec> {
 @Override
 public int compare(PartnerPersonRec p1, PartnerPersonRec p2) {
  
  return p1.getNameStructured().compareTo(p2.getNameStructured());
  
 }
}
