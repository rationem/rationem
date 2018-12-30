/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;


import com.rationem.busRec.config.common.ContactRoleRec;
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
@FacesConverter("com.rationem.util.converter.ContactRole")
public class ContactRoleConverter implements Converter {
  private static final Map<Long, ContactRoleRec> cache = new HashMap<>();
 
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


     ContactRoleRec contRole = (ContactRoleRec) object;
        Long id = contRole.getId();

        if (id != null) {
            cache.put(id, contRole);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
 
}
