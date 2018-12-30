/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;

import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import com.rationem.busRec.config.common.SortOrderRec;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.SortOrder")
public class SortOrderConverter implements Converter {
  private static Map<Long, SortOrderRec> cache = new HashMap<Long, SortOrderRec>();
  
  public SortOrderConverter() {
  }



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
  
     SortOrderRec sortOrder = (SortOrderRec) object;
        Long id = sortOrder.getId();

        if (id != null) {
            cache.put(id, sortOrder);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

  }

