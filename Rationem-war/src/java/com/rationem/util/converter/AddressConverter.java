/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.mdm.AddressRec;
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
@FacesConverter("com.rationem.util.converter.Address")
public class AddressConverter implements Converter {
 private static final Map<Long, AddressRec> CACHE = new HashMap<>();
  

  @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
    
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {
            return CACHE.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
     AddressRec address = (AddressRec) object;
        Long id = address.getId();

        if (id != null) {
            CACHE.put(id, address);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


 
}
