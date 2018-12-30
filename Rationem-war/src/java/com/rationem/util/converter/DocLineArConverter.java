/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.doc.DocLineArRec;
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
@FacesConverter("com.rationem.util.converter.DocLineAr")
public class DocLineArConverter implements Converter {
 private static Map<Long, DocLineArRec> cache = new HashMap<Long, DocLineArRec>();
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
     
     DocLineArRec docLineAr = (DocLineArRec) object;
     if(docLineAr == null){
      return "0";
     }
        Long id = docLineAr.getId();

        if (id != null) {
            cache.put(id, docLineAr);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
}
