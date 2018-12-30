/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.tr.BankAccountCompanyRec;
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
@FacesConverter("com.rationem.util.converter.BankAccountCompany")
public class BankAccountCompanyConverter implements Converter{
 private static final Map<Long, BankAccountCompanyRec> CACHE = new HashMap<>();
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

     BankAccountCompanyRec account = (BankAccountCompanyRec) object;
        Long id = account.getId();

        if (id != null) {
            CACHE.put(id, account);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
}
