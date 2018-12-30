/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.doc.DocLineBaseRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class DocLinesByLineNum implements Comparator<DocLineBaseRec>{
 
 @Override
 public int compare(DocLineBaseRec l1, DocLineBaseRec l2){
  return l1.getId().compareTo(l2.getId());
 }
 
}
