/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.converter;

import com.rationem.busRec.user.UserLoginRec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
@FacesConverter("com.rationem.util.converter.UserLoginConverter")
public class UserLoginConverter implements Converter {
 private static Logger logger = Logger.getLogger(UserLoginConverter.class.getName());
 private static Map<Long, UserLoginRec> cache = new HashMap<Long, UserLoginRec>();

 @Override
 public String getAsString(FacesContext context, UIComponent component, Object entity) {
  synchronized (cache) {
   logger.log(INFO, "getAsString called with {0}", entity);
   if (!cache.containsKey(entity)) {
    Long uuid = UUID.randomUUID().getLeastSignificantBits();
    cache.put(uuid, (UserLoginRec) entity);
    return uuid.toString();
   } else {
    return cache.get(String.valueOf(entity)).toString();
   }
  }
 }

 @Override
 public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
  logger.log(INFO, "getAsObject called with  id {0}", uuid);
  return cache.get(uuid);

  /*for (Entity<Object, String> entry : entities.entrySet()) {
   if (entry.getValue().equals(uuid)) {
   return entry.getKey();
   }
   }*/
//return null;
 }
 }



