/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.config.company.LedgerRec;
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
@FacesConverter("com.rationem.util.converter.Ledger")
public class LedgerConverter implements Converter {
 private static Map<Long, LedgerRec> cache = new HashMap<Long, LedgerRec>();
 
 @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
       throws ConverterException  
         {
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {
       Long id = Long.parseLong(value);
       
       return cache.get(id);
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
     LedgerRec ledger = (LedgerRec) object;
        Long id = ledger.getId();
        if (id != null) {
            cache.put(id, ledger);
            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
 
}
