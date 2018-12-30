/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.tax;

import com.rationem.busRec.tax.GiftAidScheduleLine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


/**
 *
 * @author Chris
 */
public class GiftAidLazyDataModel extends LazyDataModel<GiftAidScheduleLine> { 
 
 private List<GiftAidScheduleLine> datasource;

 public GiftAidLazyDataModel(List<GiftAidScheduleLine> datasource) {
  this.datasource = datasource;
 }
 
 @Override
 public GiftAidScheduleLine getRowData(String rowKey) {
  for(GiftAidScheduleLine giftAidLine : datasource) {
   long rowId = Long.parseLong(rowKey);
   if(giftAidLine.getId() == rowId){
    return giftAidLine;
   }
  }
   return null;
   
  
 }
 @Override
 public Object getRowKey(GiftAidScheduleLine giftAidLine) {
  return giftAidLine.getId();
  
 }
 
 
 /**
  *
  * @param first
  * @param pageSize
  * @param sortField
  * @param sortOrder
  * @param filters
  * @return
  */
 public List<GiftAidScheduleLine> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters, Object obj) {
  List<GiftAidScheduleLine> data = new ArrayList<GiftAidScheduleLine>();
  //filter  
        for(GiftAidScheduleLine giftAidLine : datasource) {  
            boolean match = true;  
  
            for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {  
                try {  
                    String filterProperty = it.next();  
                    String filterValue = filters.get(filterProperty);  
                    String fieldValue = String.valueOf(giftAidLine.getClass().getField(filterProperty).get(giftAidLine));  
  
                    if(filterValue == null || fieldValue.startsWith(filterValue)) {  
                        match = true;  
                    }  
                    else {  
                        match = false;  
                        break;  
                    }  
                } catch(Exception e) {  
                    match = false;  
                }   
            }  
  
            if(match) {  
                data.add(giftAidLine);  
            }  
        }  
  
        //sort  
        if(sortField != null) {  
            Collections.sort(data, new GiftAidLineSorter(sortField, sortOrder));  
        }  
  
        //rowCount  
        int dataSize = data.size();  
        this.setRowCount(dataSize);  
  
        //paginate  
        if(dataSize > pageSize) {  
            try {  
                return data.subList(first, first + pageSize);  
            }  
            catch(IndexOutOfBoundsException e) {  
                return data.subList(first, first + (dataSize % pageSize));  
            }  
        }  
        else {  
            return data;  
        }  
  
 }
}
