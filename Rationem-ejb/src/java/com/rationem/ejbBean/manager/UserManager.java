/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.manager;

import com.rationem.busRec.user.UserRec;
import com.rationem.exception.BacException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class UserManager {

 public UserRec saveUser(UserRec usr) throws BacException {
  return null;
 }
 
 

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public UserRec getUser(String name) {
  return null;
 }
 
 

}
