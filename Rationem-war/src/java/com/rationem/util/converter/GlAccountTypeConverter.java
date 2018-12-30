/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;

//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.config.company.AccountTypeRec;
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
@FacesConverter("com.rationem.util.converter.GlAccountType")
public class GlAccountTypeConverter implements Converter {

  private static Map<Long, AccountTypeRec> cache = new HashMap<Long, AccountTypeRec>();
  private static final Logger logger =
            Logger.getLogger("com.rationem.util.converter.ModuleConverter");

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
     
     AccountTypeRec actType = (AccountTypeRec) object;
        Long id = actType.getId();

        if (id != null) {
            cache.put(id, actType);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


}
