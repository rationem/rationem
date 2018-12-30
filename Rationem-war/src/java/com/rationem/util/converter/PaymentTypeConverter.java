/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
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
@FacesConverter("com.rationem.util.converter.PaymentType")
public class PaymentTypeConverter implements Converter {
  private static final Map<Long, PaymentTypeRec> CACHE = new HashMap<>();
  private static final Logger LOGGER = Logger.getLogger(PaymentTypeConverter.class.getName());

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
     
     PaymentTypeRec payTerrms = (PaymentTypeRec) object;
        Long id = payTerrms.getId();

        if (id != null) {
            CACHE.put(id, payTerrms);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


}
