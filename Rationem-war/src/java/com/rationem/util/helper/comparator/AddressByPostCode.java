/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper.comparator;

import com.rationem.busRec.mdm.AddressRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class AddressByPostCode implements Comparator<AddressRec> {
 
 @Override
 public int compare(AddressRec a1, AddressRec a2){
  return a1.getPostCode().compareTo(a2.getPostCode());
 }
}
