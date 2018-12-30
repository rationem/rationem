/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;
import com.rationem.busRec.ma.costCent.CostCentreRec;
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
@FacesConverter("com.rationem.util.converter.CostCentre")
public class CostCentreConverter implements Converter {
private static final Map<Long, CostCentreRec> CACHE = new HashMap<>();
 
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
     
     CostCentreRec costCent = (CostCentreRec) object;
     if(costCent == null){
      return "0";
     }
        Long id = costCent.getId();

        if (id != null) {
            CACHE.put(id, costCent);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    } 
}
