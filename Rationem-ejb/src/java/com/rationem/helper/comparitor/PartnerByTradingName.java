/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.partner.PartnerBaseRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PartnerByTradingName implements Comparator<PartnerBaseRec>{
 
 @Override
 public int compare(PartnerBaseRec p1,PartnerBaseRec p2){
  return p1.getTradingName().compareTo(p2.getTradingName());
 }
 
}
