/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.fi.company.FundRec;
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
@FacesConverter("com.rationem.util.converter.RestrictedFund")
public class RestrictedFundConverter implements Converter {
 private static Map<Long, FundRec> cache = new HashMap<Long, FundRec>();
 
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
     
     FundRec fund = (FundRec) object;
     if(fund == null){
      return "0";
     }
        Long id = fund.getId();

        if (id != null) {
            cache.put(id, fund);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
}
