/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.doc.DocTypeRec;
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
@FacesConverter("com.rationem.util.converter.DocType")
public class DocTypeConverter implements Converter {
 private static final Map<Long, DocTypeRec> CACHE = new HashMap<Long, DocTypeRec>();
  
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


     DocTypeRec docType = (DocTypeRec) object;
        Long id = docType.getId();

        if (id != null) {
            CACHE.put(id, docType);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

 
}
