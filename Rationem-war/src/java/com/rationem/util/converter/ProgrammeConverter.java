/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.ma.programme.ProgrammeRec;
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
@FacesConverter("com.rationem.util.converter.Programme")
public class ProgrammeConverter implements Converter {
 private static Map<Long, ProgrammeRec> cache = new HashMap<Long, ProgrammeRec>();
 
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
     
     ProgrammeRec prog = (ProgrammeRec) object;
     if(prog == null){
      return "0";
     }
        Long id = prog.getId();

        if (id != null) {
            cache.put(id, prog);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    } 
 
}
