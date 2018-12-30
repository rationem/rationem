/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.entity.mdm.PartnerRole;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PartnerRoleByCode implements Comparator<PartnerRole> {
 
 @Override
 public int compare(PartnerRole r1, PartnerRole r2){
  return r1.getRoleCode().compareTo(r2.getRoleCode());
 }
  
 
}
