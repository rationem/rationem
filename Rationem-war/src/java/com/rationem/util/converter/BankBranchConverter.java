/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util.converter;
import com.rationem.busRec.tr.BankBranchRec;
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
@FacesConverter("com.rationem.util.converter.BankBranch")
public class BankBranchConverter implements Converter {
  private static Map<Long, BankBranchRec> cache = new HashMap<Long, BankBranchRec>();
  private static final Logger logger =
            Logger.getLogger("com.rationem.util.converter.BankBranchConverter");

  @Override
  public Object getAsObject(FacesContext context,UIComponent component, String value)
          throws ConverterException {
    logger.log(INFO, "Bank Branch converter getAsObject value is {0}", value);
    if ((value == null) ) {
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
     logger.log(INFO, "Bank Branch converter converter getAsString called with object {0}",
             object.getClass().getName());

     BankBranchRec branch = (BankBranchRec) object;
        Long id = branch.getId();

        if (id != null) {
            cache.put(id, branch);

            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }


}
