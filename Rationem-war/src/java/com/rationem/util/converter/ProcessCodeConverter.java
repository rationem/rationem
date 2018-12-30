/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.config.common.ProcessCodeRec;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;


/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.ProcessCode")
public class ProcessCodeConverter implements Converter {
 private static Map<Long, ProcessCodeRec> cache = new HashMap<Long, ProcessCodeRec>();
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
     
     ProcessCodeRec procCode = (ProcessCodeRec) object;
        Long id = procCode.getId();

        if (id != null) {
            cache.put(id, procCode);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

}
