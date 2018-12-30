/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import com.rationem.busRec.config.common.TransactionTypeRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.TransactionType")
public class TransactionTypeConverter implements Converter {
 private static Map<Long, TransactionTypeRec> cache = new HashMap<Long, TransactionTypeRec>();
  
 



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

     TransactionTypeRec transType = (TransactionTypeRec) object;
        Long id = transType.getId();

        if (id != null) {
            cache.put(id, transType);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
}
