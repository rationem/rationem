/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper.comparitor;

import com.rationem.busRec.config.company.PostTypeRec;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class PostTypeByDescr implements Comparator<PostTypeRec>{
 @Override
 public int compare(PostTypeRec pt1, PostTypeRec pt2) {
   return pt1.getDescription().compareToIgnoreCase(pt2.getDescription());
 }
 
}
