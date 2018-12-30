/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;

import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;


/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.ChartOfAccounts")
public class ChartOfAccountsConverter implements Converter {
  private static Map<Long, ChartOfAccountsRec> CACHE = new HashMap<Long, ChartOfAccountsRec>();
  
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
  
     ChartOfAccountsRec chart = (ChartOfAccountsRec) object;
        Long id = chart.getId();

        if (id != null) {
            CACHE.put(id, chart);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

}
