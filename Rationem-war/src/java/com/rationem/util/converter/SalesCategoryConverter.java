/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.sales.SalesCatRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.SalesCategory")
public class SalesCategoryConverter implements Converter {
 private static final Map<Long, SalesCatRec> CACHE = new HashMap<>();
 private static final Logger LOGGER = Logger.getLogger(SalesCategoryConverter.class.getName());
 
 @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
   LOGGER.log(INFO, "SalesCategoryConverter getAsObject val {0}", value);
   
   
    
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {
     long inputVal = Long.parseLong(value);
     SalesCatRec cat =  CACHE.get(inputVal);
     return cat;
        } catch (NumberFormatException e) {
            throw new ConverterException("cat Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
    LOGGER.log(INFO, "SalesCategoryConverter getAsString val {0}", object);
     if(object == null){
      return "0";
     }
     SalesCatRec cat = (SalesCatRec) object;
        Long id = cat.getId();
        if (id != null) {
            CACHE.put(id, cat);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
 
}
