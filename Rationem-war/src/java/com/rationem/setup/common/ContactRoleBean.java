/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import javax.ejb.EJB;

/**
 *
 * @author user
 */
public class ContactRoleBean extends BaseBean{
 
 @EJB
 private BasicSetup setup;
 
 private ContactRoleRec contRl;
 

 /**
  * Creates a new instance of ContactRoleBean
  */
 public ContactRoleBean() {
 }
 
}
