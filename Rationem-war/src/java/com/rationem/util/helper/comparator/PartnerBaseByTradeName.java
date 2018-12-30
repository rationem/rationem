/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;
import com.rationem.busRec.partner.PartnerBaseRec;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author user
 */
public class PartnerBaseByTradeName implements Comparator<PartnerBaseRec>{
 
 @Override
 public int compare(PartnerBaseRec p1, PartnerBaseRec p2) {
  
  if(StringUtils.isBlank(p1.getTradingName()) || StringUtils.isBlank(p2.getTradingName())){
   return p1.getDisplayName().compareTo(p2.getDisplayName());
  }else{
   return p1.getTradingName().compareTo(p2.getTradingName());
  }
  
 }
 
}
