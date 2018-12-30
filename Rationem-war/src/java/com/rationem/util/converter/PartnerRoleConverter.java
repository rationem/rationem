/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.partner.PartnerRoleRec;
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
@FacesConverter("com.rationem.util.converter.PartnerRole")
public class PartnerRoleConverter implements Converter{
 private static Map<Long, PartnerRoleRec> CACHE = new HashMap<>();
 @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
    if(CACHE.isEmpty()){
      return null;
    }
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {

            return CACHE.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("PartnerCorportateConverter Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
    
     PartnerRoleRec r = (PartnerRoleRec) object;
     Long id = r.getId();
     if (id != null) {
      CACHE.put(id, r);
      return String.valueOf(id.longValue());
     } else {
      return "0";
     }
    } 
 
}
