/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.doc.DocLineApRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ApLineByAcntRef implements Comparator<DocLineApRec>{
 
 @Override
 public int compare(DocLineApRec ln1, DocLineApRec ln2) {
  return ln1.getAccountRef().compareTo(ln2.getAccountRef());
 }
 
}
