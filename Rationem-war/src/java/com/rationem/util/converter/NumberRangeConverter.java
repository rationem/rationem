/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.config.common.NumberRangeRec;
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
@FacesConverter("com.rationem.util.converter.NumberControl")
public class NumberRangeConverter implements Converter {
 private static Map<Long, NumberRangeRec> cache = new HashMap<Long, NumberRangeRec>();
 
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
     NumberRangeRec numCntrl = (NumberRangeRec) object;
        Long id = numCntrl.getNumberControlId();

        if (id != null) {
            cache.put(id, numCntrl);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
}
