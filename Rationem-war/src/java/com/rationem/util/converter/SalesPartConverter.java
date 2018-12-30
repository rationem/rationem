/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.sales.SalesPartRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.SalesPart")
public class SalesPartConverter implements Converter {
 private static Map<Long, SalesPartRec> cache = new HashMap<Long, SalesPartRec>();
 
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
     if(object == null){
      return null;
     }
     SalesPartRec part = (SalesPartRec) object;
        Long id = part.getId();
        if (id != null) {
            cache.put(id, part);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
 
}
