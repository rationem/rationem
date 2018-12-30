/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.manager;

import com.rationem.busRec.user.UserLoginRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.dataManager.UserDM;
import com.rationem.ejbBean.sec.AuthDM;
import com.rationem.entity.user.User;
import com.rationem.exception.BacEjbException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class AuthenticationMgr {
 private static final Logger LOGGER = Logger.getLogger(AuthenticationMgr.class.getName());
 
 @EJB
 AuthDM authDM;
 
 @EJB
 UserDM usrDm;

 
 
 @PostConstruct
 private void init(){
  boolean usersExist = authDM.usersExist();
  LOGGER.log(INFO, "AuthenticationMgr init userS created {0}", usersExist);
 }
 
 public UserLoginRec getUserLoginByName(String name) throws BacEjbException {
  LOGGER.log(INFO, "getUserLoginByName called with {0}", name);
  
  UserLoginRec usr = authDM.getUserByName(name);
  LOGGER.log(INFO, "getUserLoginByName returns User {0}", usr);
  return usr;
 
 }
 
 public UserRec getUserById(Long id){
  LOGGER.log(INFO, "AuthMgr.getUserById called with id {0}", id);
  String tenantId = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tenantId");
  LOGGER.log(INFO, "tenantId id is {0}", tenantId);
  UserRec usr = this.usrDm.getUserByIdNum(id,tenantId);
  
  return usr;
 }
 
 public List<UserLoginRec> getUserLogins(){
  List<UserLoginRec> logins = this.authDM.getLogins();
  return logins;
 }
 public void setLoginFailed(UserLoginRec usrLogin) {
  usrLogin = authDM.userLoginFailed(usrLogin);
  
 }
 public void setLoginOk(UserLoginRec usrLogin, String pg) {
  LOGGER.log(INFO, "SetLoginOk called for login id {0}", usrLogin.getId());
  usrLogin.setLastLogin(new Date());
  usrLogin.setFailedAttempts(0);
  authDM.userLoggedIn(usrLogin);
 }
 
 public UserLoginRec userPasswordUpdate(UserLoginRec login, String plainPw, String pg){
  String hashedPw = PasswordUtil.generateStorngPasswordHash(plainPw);
  login = authDM.userPasswordUpdate(login,  hashedPw, pg);
  return login;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public UserLoginRec createUserLogin(UserLoginRec login) {
  LOGGER.log(INFO, "AuthenticationMgr createUser called with usr {0}",login);
  UserRec userRec = new UserRec();
  userRec.setRef(login.getLogonName());
  userRec.setFamilyName(login.getLogonName());
  login = authDM.createUserLogin(login, false);
  LOGGER.log(INFO, "authMgr created usrLogin id {0}", login.getId());
  
  return login;
 }

 public boolean defaultUsersExist() {
  
  return authDM.defaultUsersExist();
 }

 public UserRec saveUser(UserRec usr) {
  return null;
 }

 public UserLoginRec setUserForLogin(UserLoginRec login, User usr) {
  LOGGER.log(INFO, "AuthMgr.setUserForLogin called with login {0} user {1}", new Object[]{login,usr});
  authDM.setUserForLogin(login, usr);
  return login;
 }
 
 public boolean checkPw(String inputPw, String dbPw){
  
  boolean rc = BCrypt.checkpw(inputPw, dbPw);
  
  return rc;
    
 }
 
 public void loginFailed(UserLoginRec login) {
  authDM.userLoginFailed(login);
 }
 

}
