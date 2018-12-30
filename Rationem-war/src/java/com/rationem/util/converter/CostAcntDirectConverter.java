/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author user
 */
@FacesConverter("com.rationem.util.converter.CostAccountDirect")
public class CostAcntDirectConverter implements Converter {
 private static Map<Long, CostAccountDirectRec> cache = new HashMap<Long, CostAccountDirectRec>();
 
 @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
    
    
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {
            return cache.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
     
     CostAccountDirectRec costCent = (CostAccountDirectRec) object;
     if(costCent == null){
      return "0";
     }
        Long id = costCent.getId();

        if (id != null) {
            cache.put(id, costCent);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
 
}
