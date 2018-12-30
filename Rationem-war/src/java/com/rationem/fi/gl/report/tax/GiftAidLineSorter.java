/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.tax;
import com.rationem.busRec.tax.GiftAidScheduleLine;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Chris
 */
public class GiftAidLineSorter implements Comparator<GiftAidScheduleLine> {
 private String sortField;
 private SortOrder sortOrder;
    
 public GiftAidLineSorter(String sortField, SortOrder sortOrder) {
  this.sortField = sortField;
  this.sortOrder = sortOrder;
  }

 @Override
 public int compare(GiftAidScheduleLine giftAid1, GiftAidScheduleLine giftAid2) {
  try {
   Object value1 = GiftAidScheduleLine.class.getField(this.sortField).get(giftAid1);
   Object value2 = GiftAidScheduleLine.class.getField(this.sortField).get(giftAid2);

   int value = ((Comparable)value1).compareTo(value2);
        
   return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
  }catch(Exception e) {
   throw new RuntimeException();
  }
 }
    
 
}
