/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.cm.ContactRec;
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
@FacesConverter("com.rationem.util.converter.Contact")
public class ContactConverter implements Converter {
 private static final Map<Long, ContactRec> cache = new HashMap<>();
 
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
   ContactRec cnt = (ContactRec) object;
   Long id = cnt.getId();
   if (id != null) {
    cache.put(id, cnt);
    return String.valueOf(id.longValue());
   } else {
     return "0";
   }
  }
}
