/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.doc.DocTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class DocTypeByName implements Comparator<DocTypeRec> {
 
 @Override
 public int compare(DocTypeRec dt1, DocTypeRec dt2){
  return dt1.getName().compareTo(dt2.getName());
 }
}
