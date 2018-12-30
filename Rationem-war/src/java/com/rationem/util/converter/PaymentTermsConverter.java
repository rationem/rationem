/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * Converter for payment terms used by GUI select items
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.PaymentTerms")
public class PaymentTermsConverter implements Converter {
  private static  Map<Long, PaymentTermsRec> cache = new HashMap<>();
  private static final Logger logger =  Logger.getLogger("com.rationem.util.converter.PaymentTerms");

  @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
   logger.log(INFO, "payTerms converter getAsObject value passed {0} class {1}", 
     new Object[]{value, value.getClass().getSimpleName()});
   long id = NumberUtils.toLong(value);
   logger.log(INFO, "value from utils.toLong {0}", id);
   try{
   Object o = cache.get(id);
   return o;
   }catch(Exception ex){
    logger.log(INFO, "exemption type {0}", ex.getClass().getSimpleName());
   }
   return cache.get(id);
   
    /*if ((value == null) || value.equals("0")) {
            return null;
        }
    if(!isNumeric(value)){
     return "0";
    }
    try {
         value = value.trim();
         long id = NumberUtils.toLong(value);
         if(id == 0){
          return "0";
         }else{
          return cache.get(id);
         }
          
        } catch (NumberFormatException e) {
         try{
         long id = Integer.parseInt(value);
         long l_id = id;
          return cache.get(l_id);
         }catch(NumberFormatException ex){
          throw new ConverterException("Invalid value : " + ex.getLocalizedMessage());
         }
            
        }*/
    }

  @Override
   public String getAsString(FacesContext context,  UIComponent component, Object object) {
     
     PaymentTermsRec payTerrms = (PaymentTermsRec) object;
        Long id = payTerrms.getId();

        if (id != null) {
            cache.put(id, payTerrms);
            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }

}
