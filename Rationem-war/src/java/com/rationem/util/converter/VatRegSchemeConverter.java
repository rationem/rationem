/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;


import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
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
@FacesConverter("com.rationem.util.converter.VatRegScheme")
public class VatRegSchemeConverter implements Converter {
 
 private static Map<Long, VatRegSchemeRec> cache = new HashMap<Long, VatRegSchemeRec>(); 
 
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

     VatRegSchemeRec regSch = (VatRegSchemeRec) object;
        Long id = regSch.getId();

        if (id != null) {
            cache.put(id, regSch);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
}
