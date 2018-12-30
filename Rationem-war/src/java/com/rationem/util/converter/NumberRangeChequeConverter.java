/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;


import com.rationem.busRec.config.common.NumberRangeChequeRec;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 *
 * @author user
 */
@FacesConverter("com.rationem.util.converter.NumberRangeCheque")
public class NumberRangeChequeConverter implements Converter {
 private static final Map<Long, NumberRangeChequeRec> CACHE = new HashMap<Long, NumberRangeChequeRec>();
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
    NumberRangeChequeRec chqBk = (NumberRangeChequeRec) object;
    Long id = chqBk.getNumberControlId();
    if (id != null) {
     CACHE.put(id, chqBk);
     return String.valueOf(id.longValue());
    } else {
     return "0";
    }
   }
}
