/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
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
@FacesConverter("com.rationem.util.converter.VatCode")
public class VatCodeConverter implements Converter {
 private static Map<Long, VatCodeRec> cache = new HashMap<Long, VatCodeRec>(); 
  

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

     VatCodeRec vatCode = (VatCodeRec) object;
        Long id = vatCode.getId();

        if (id != null) {
            cache.put(id, vatCode);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
}
