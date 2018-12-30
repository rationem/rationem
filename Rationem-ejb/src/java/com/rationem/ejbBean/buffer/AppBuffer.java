/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.buffer;

import com.rationem.busRec.user.UserLoginRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.user.UserRoleRec;
import com.rationem.ejbBean.dataManager.UserDM;
import com.rationem.ejbBean.manager.AuthenticationMgr;
import com.rationem.ejbBean.manager.PasswordUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
@Singleton
@LocalBean
public class AppBuffer {
 private static Logger LOGGER = Logger.getLogger(AppBuffer.class.getName());

 @EJB
 AuthenticationMgr authMgr;
 
 @EJB
 UserDM userDm;
 
 
 
 private boolean usersExist;

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
 
 
 

 public boolean defaultUsersExist() {
  LOGGER.log(INFO, "appBuffer defaultUsersExist {0}", usersExist);
  if(usersExist){
   return usersExist;
  }else{
   usersExist = authMgr.defaultUsersExist();
  }
  LOGGER.log(INFO, "after check with  authMgr defaultUsersExist {0}", usersExist);
  if(usersExist){
   return usersExist;
  }else{
  // create default users
   
   LOGGER.log(INFO, "Need to Create default users");
   UserLoginRec usr1 = new UserLoginRec();
   usr1.setFailedAttempts(0);
   usr1.setInitialPassWord(true);
   usr1.setLogonName("user1");
   
   String pass = "user1";
   String storePw = PasswordUtil.generateNewPasswordHash("user1");
   usr1.setTenant("t1");
   /*String storePw = PasswordUtil.generateStorngPasswordHash(pass);
   String[] pwParts = storePw.split(":");
   int wf = Integer.parseInt(pwParts[0]);
   String salt = pwParts[1];
   String pwHash = pwParts[2];*/
   LOGGER.log(INFO, "user1 init pass hash {0}", storePw);
   usr1.setPass(storePw);
   //usr1.setSalt(salt);
   //usr1.setWorkFactor(wf);
   UserRoleRec uRole = new UserRoleRec();
   uRole.setRoleCode("superUser");
   uRole.setUsr(usr1);
   List<UserRoleRec> uRoles = new ArrayList<UserRoleRec>();
   uRoles.add(uRole);
   usr1.setUserRoles(uRoles);
   // User Record
   UserRec usrRec1 = new UserRec();
   usrRec1.setFamilyName("User 1");
   usrRec1 =  userDm.createTenantDefaultUser(usrRec1, usr1.getTenant());
   LOGGER.log(INFO, "usrRec1 {0}", usrRec1);
   usr1.setUserId(usrRec1.getId());
   
   usr1 = authMgr.createUserLogin(usr1);
   LOGGER.log(INFO, "App buff after create user principle {0} ",new Object[]{usr1});
   
   
   
   UserLoginRec usr2 = new UserLoginRec();
   usr2.setFailedAttempts(0);
   usr2.setInitialPassWord(true);
   usr2.setLogonName("user2");
   pass = "user2";
   usr2.setTenant("t2");
   storePw = PasswordUtil.generateStorngPasswordHash(pass);
   /*
   pwParts = storePw.split(":");
   wf = Integer.parseInt(pwParts[0]);
   salt = pwParts[1];
   pwHash = pwParts[2];
   */
   LOGGER.log(INFO, "user2 init pass hash {0}", storePw);
   usr2.setPass(storePw);
   //usr2.setSalt(salt);
   //usr2.setWorkFactor(wf);
   UserRoleRec uRole2 = new UserRoleRec();
   uRole2.setRoleCode("superUser");
   uRole2.setUsr(usr1);
   List<UserRoleRec> uRoles2 = new ArrayList<UserRoleRec>();
   uRoles2.add(uRole2);
   usr2.setUserRoles(uRoles2);
   // User Record
   UserRec usrRec2 = new UserRec();
   usrRec2.setFamilyName("User 2");
   usrRec2 =  userDm.createTenantDefaultUser(usrRec2, usr1.getTenant());
   LOGGER.log(INFO, "usrRec2 id {0}", usrRec2.getId());
   usr2.setUserId(usrRec2.getId());
   usr2 = authMgr.createUserLogin(usr2);
   LOGGER.log(INFO, "userlogin 2 id {0}", usr2.getUserId());
   //User usr2Db = userDm.createTenantDefaultUser(userRec2, usr2.getTenant());
   //logger.log(INFO, "usr2Db {0}", usr2Db);
   return true;
   
  }
  
 }
 
 

}
