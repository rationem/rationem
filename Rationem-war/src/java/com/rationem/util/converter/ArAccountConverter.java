/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.ArAccount")
public class ArAccountConverter implements Converter {
 private static Map<Long, ArAccountRec> cache = new HashMap<Long, ArAccountRec>();
 
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
    
     ArAccountRec acnt = (ArAccountRec) object;
        Long id = acnt.getId();

        if (id != null) {
            cache.put(id, acnt);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


 
}