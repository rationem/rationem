/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;
import com.rationem.busRec.sales.SalesPartCompanyRec;
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
@FacesConverter("com.rationem.util.converter.SalesPartCompany")
public class SalesPartCompanyConverter implements Converter {
 private static Map<Long, SalesPartCompanyRec> cache = new HashMap<Long, SalesPartCompanyRec>();
 
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
    if(object.getClass().getCanonicalName().equalsIgnoreCase("java.lang.String") ){
     return "0";
    }
     SalesPartCompanyRec cat = (SalesPartCompanyRec) object;
        Long id = cat.getId();
        if (id != null) {
            cache.put(id, cat);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
 
}
