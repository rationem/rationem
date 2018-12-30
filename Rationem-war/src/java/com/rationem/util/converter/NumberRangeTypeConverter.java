/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;
import com.rationem.busRec.config.common.NumberRangeTypeRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author user
 */
@FacesConverter("com.rationem.util.converter.NumberRangeType")
public class NumberRangeTypeConverter implements Converter {
 private static Map<Long, NumberRangeTypeRec> CACHE = new HashMap<>();
 
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
  NumberRangeTypeRec nrTy = (NumberRangeTypeRec) object;
  Long id = nrTy.getId();
  if (id != null) {
   CACHE.put(id, nrTy);
   return String.valueOf(id.longValue());
  } else {
   return "0";
  }
 }
}
