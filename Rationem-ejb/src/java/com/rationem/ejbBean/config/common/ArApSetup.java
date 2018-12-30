/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.common;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import com.rationem.exception.BacException;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.FINEST;
import javax.ejb.EJB;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class ArApSetup {
 private static final Logger logger =  Logger.getLogger(ArApSetup.class.getName());
 
 @EJB
 private ConfigurationDM configDM;
 
 @EJB
 private SysBuffer sysBuff;

 public PaymentTypeRec updatePaymentType(PaymentTypeRec payType, String view) 
         throws BacException {
  logger.log(INFO, "ArAPSetup.updatePaymentType called with {0}  view {1}", new Object[]{
   payType,view});
  logger.log(INFO, "Update payment type {0}  ",  payType.getChangedBy());
  payType = configDM.updatePaymentType(payType,  view);
  List<PaymentTypeRec> payTypes = sysBuff.getPaymentTypes();
  ListIterator<PaymentTypeRec> li = payTypes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   PaymentTypeRec pt = li.next();
   if(pt == payType ){
    pt = payType;
    li.set(pt);
    found = true;
   }
  }
  sysBuff.setPaymentTypes(payTypes);
  return payType;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public PaymentTypeRec paymentTypeCreate(PaymentTypeRec payType, UserRec usr, String view) 
         throws BacException {
  logger.log(INFO, "ArAPSetup.paymentTypeCreate called with {0} user id {1} view {2}", new Object[]{
   payType,usr.getId(),view});
  try{
   payType = configDM.paymentTypeCreate(payType, usr, view);
   logger.log(INFO, "After DB call payType id {0}",payType.getId());
   List<PaymentTypeRec> payTypes = sysBuff.getPaymentTypes();
   payTypes.add(payType);
   sysBuff.setPaymentTypes(payTypes);
  }catch(BacException ex){
   throw new BacException("Could not create paytype "+ex.getLocalizedMessage(),"PT:01");
  }catch(Exception ex){
   throw new BacException("Could not create paytype "+ex.getLocalizedMessage(),"PT:01");
  }
  return payType;
 }

}
