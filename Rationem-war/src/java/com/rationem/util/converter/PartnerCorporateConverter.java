/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;
import com.rationem.busRec.partner.PartnerCorporateRec;
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
@FacesConverter("com.rationem.util.converter.PartnerCorporate")
public class PartnerCorporateConverter implements Converter {
  private static Map<Long, PartnerCorporateRec> cache = new HashMap<Long, PartnerCorporateRec>();
  private static final Logger logger =
            Logger.getLogger("com.rationem.util.converter.PartnerCorportateConverter");


  @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
    if(cache.isEmpty()){
      return null;
    }
    if ((value == null) || value.equals("0")) {
            return null;
        }
    try {

            return cache.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
          logger.log(INFO, "PartnerCorportateConverter get as object NumberFormatException for value {0}", value);
            throw new ConverterException("PartnerCorportateConverter Invalid value: " + value, e);
        }
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
     

     PartnerCorporateRec ptnr = (PartnerCorporateRec) object;
        Long id = ptnr.getId();

        if (id != null) {
            cache.put(id, ptnr);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


}
