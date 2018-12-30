/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.partner.PartnerRoleRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PartnerRoleRecByCode implements Comparator<PartnerRoleRec> {

@Override
public int compare(PartnerRoleRec r1, PartnerRoleRec r2){
 return r1.getRoleCode().compareTo(r2.getRoleCode());
 
}
 
}
