/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.company.AccountTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class AccountTypeByName implements Comparator<AccountTypeRec> {
 
 @Override
 public int compare(AccountTypeRec t1,AccountTypeRec t2){
  return t1.getName().compareTo(t2.getName());
 }
 
}
