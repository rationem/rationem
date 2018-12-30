/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.sec;
 
import com.rationem.busRec.user.UserLoginRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.buffer.AppBuffer;
import com.rationem.ejbBean.manager.AuthenticationMgr;
import com.rationem.ejbBean.manager.PasswordUtil;
import com.rationem.ejbBean.sec.AuthDM;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.UserSessionBean;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.jboss.weld.logging.ValidatorLogger;

import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Chris
 */

public class AuthManBean extends BaseBean {

 /**
  * 
  */
 private static final long serialVersionUID = 1L;

 /**
  * 
  */
 

 private static final Logger LOGGER = Logger.getLogger(AuthManBean.class.getName());

  
 @EJB
 private AuthenticationMgr authMgr;
 
 @EJB
 private AppBuffer appBuff;
 
  
 private List<UserLoginRec> userList;
 private UserLoginRec selectedLogonName;
 private String userName;
 private String userPass;
 private String userChangedPass;
 private String initPwd1;
 private String initPwd2;
 private String loginStatus;
 private UserLoginRec usrLogin;

 /**
  * Creates a new instance of AuthManBean
  */
 public AuthManBean() {
 }

 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "AuthManBean init");
  boolean defUsers = appBuff.defaultUsersExist();
  LOGGER.log(INFO, "AppBuff returns def users {0}", defUsers);
  if(StringUtils.equals(this.getViewSimple(), "initPassReset")){
   FacesContext fc = FacesContext.getCurrentInstance();
   usrLogin = (UserLoginRec)fc.getExternalContext().getSessionMap().get("userLogin");
  
  }
  
 }
 
 public String loadUserLogin(){
  LOGGER.log(INFO, "loadUserLogin called and userlogin name is {0}", usrLogin.getLogonName());
  usrLogin = this.authMgr.getUserLoginByName(usrLogin.getLogonName());
  return null;
 }
 public UserLoginRec getSelectedLogonName() {
  return selectedLogonName;
 }

 public void setSelectedLogonName(UserLoginRec selectedLogonName) {
  this.selectedLogonName = selectedLogonName;
 }

 public List<UserLoginRec> usrLogonComplete(String input) {
  List<UserLoginRec> completeList = getUserList();
  if (input == null || input.isEmpty()) {
   return completeList;
  } else {
   ListIterator<UserLoginRec> usrLi = completeList.listIterator();
   while (usrLi.hasNext()) {
    UserLoginRec usrRec = usrLi.next();
    if (!usrRec.getLogonName().startsWith(input)) {
     usrLi.remove();
    }
   }
  }
  return completeList;
 }

 public String getInitPwd1() {
  return initPwd1;
 }

 public void setInitPwd1(String initPwd1) {
  this.initPwd1 = initPwd1;
 }

 public String getInitPwd2() {
  return initPwd2;
 }

 public void setInitPwd2(String initPwd2) {
  this.initPwd2 = initPwd2;
 }

 
 public String getLoginStatus() {
  return loginStatus;
 }

 public void setLoginStatus(String loginStatus) {
  this.loginStatus = loginStatus;
 }

 public String getUserChangedPass() {
  return userChangedPass;
 }

 public void setUserChangedPass(String userChangedPass) {
  this.userChangedPass = userChangedPass;
 }

 public List<UserLoginRec> getUserList() {
  if (userList == null) {
   userList = this.authMgr.getUserLogins();
  }
  return userList;
 }

 public void setUserList(List<UserLoginRec> userList) {
  this.userList = userList;
 }

 public String getUserName() {
  return userName;
 }

 public void setUserName(String userName) {
  this.userName = userName;
 }

 public String getUserPass() {
  return userPass;
 }

 public void setUserPass(String userPass) {
  this.userPass = userPass;
 }

 public UserLoginRec getUsrLogin() {
  return usrLogin;
 }

 public void setUsrLogin(UserLoginRec usrLogin) {
  this.usrLogin = usrLogin;
 }

 public void onUsrDialogReturn(SelectEvent evt){
  usrLogin = (UserLoginRec)evt.getObject();
  LOGGER.log(INFO, "onUsrDialogReturn called with {0}", usrLogin.getId());
 }
 
 public String onLogout() {
  FacesContext fc = FacesContext.getCurrentInstance();
  HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
  try {
   request.logout();
   return "home";
  } catch (ServletException ex) {
   Logger.getLogger(AuthManBean.class.getName()).log(Level.SEVERE, null, ex);
   return "home";
  }
 }
 
 public String onLoginBtnAction() {
  
   LOGGER.log(INFO, "onLoginBtnAction called to return {0}", loginStatus);
   
   if(loginStatus != null && loginStatus.equalsIgnoreCase("loginOk")){
    LOGGER.log(INFO, "Login redirect to home");
    
    FacesContext ctx = FacesContext.getCurrentInstance();
    ExternalContext extContext = ctx.getExternalContext();
    String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/secure/home.xhtml"));
    LOGGER.log(INFO, "url {0}", url);
    try {
     extContext.redirect(url);
     
     return null;
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }
     
    //LOGGER.log(INFO, "return path {0}",loginStatus);
    //return loginStatus;
       
   }else if(loginStatus.equalsIgnoreCase("initPw")){
    LOGGER.log(INFO,"status is initPw");
    return loginStatus;
   }
   else{
    LOGGER.log(INFO, "new password required");
    return null;   
   }
   
  
  
 }

 
 public void onLoginBtnActionListener() {
  LOGGER.log(INFO, "onLoginBtnActionListener called");
  LOGGER.log(INFO, "User name {0} password {1}", new Object[]{userName, userPass});
  //String hashPw;
  boolean pwValid;
  
  usrLogin = authMgr.getUserLoginByName(userName);
  LOGGER.log(INFO, "Found user with id {0}",usrLogin.getId());
  if(usrLogin == null) {
   loginStatus = "loginError";
   return;
  }
  //logger.log(INFO, "Max Log on attempts {0}", getTenentBuff().getMaxFailedLogins() );
  if(usrLogin.getFailedAttempts() > 9) {
   MessageUtil.addErrorMessage("loginAttempt", "errorText");
   PrimeFaces.current().ajax().update("loginFrm:msgs");
   loginStatus = "null";
   return;
  }
  
  
  pwValid = authMgr.checkPw(userPass, usrLogin.getPass());
  LOGGER.log(INFO, "Check pw returns {0} entered pass {1} {2}" , 
    new Object[] {pwValid,userPass, usrLogin.getPass()});
  if (pwValid) {
   FacesContext fc = FacesContext.getCurrentInstance();
   HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
   Principal usrPrincipal = request.getUserPrincipal();
   LOGGER.log(INFO, "usrPrincipal {0}", usrPrincipal);
   if (usrPrincipal != null) {
    
    try {
     request.logout();
    } catch (ServletException e) {
    }
   }
   try {
    LOGGER.log(INFO, "request.login called with user {0} pw {1}", 
            new Object[]{userName,usrLogin.getPass()});
    request.login(userName, usrLogin.getPass());
    
    
    authMgr.setLoginOk(usrLogin, getViewSimple());
    LOGGER.log(INFO, "login status {0}", request.getUserPrincipal());
   } catch (ServletException e) {
     LOGGER.log(INFO, "Login failed");   
   }
   LOGGER.log(INFO, "login status {0}", request.getUserPrincipal());
   fc.getExternalContext().getSessionMap().put("tenantId", usrLogin.getTenant());
   fc.getExternalContext().getSessionMap().put("userId", usrLogin.getUserId());
   
   LOGGER.log(INFO, "Login tenantId {0}", fc.getExternalContext().getSessionMap().get("tenantId"));
   LOGGER.log(INFO, "usrLogin.isInitialPassWord() {0}", usrLogin.isInitialPassWord());
   LOGGER.log(INFO, "userLogin id : {0}", usrLogin.getId());
   UserRec loggedUser = authMgr.getUserById(usrLogin.getUserId());
   fc.getExternalContext().getSessionMap().put("usrRec", loggedUser);
   LOGGER.log(INFO, "AuthBean User from session {0}", fc.getExternalContext().getSessionMap().get("usrRec"));
   UserSessionBean usrSession = new UserSessionBean();
   usrSession.setUsr(loggedUser);
   fc.getExternalContext().getSessionMap().put("userBuff", usrSession);
   LOGGER.log(INFO, "Session map after login {0}", fc.getExternalContext().getSessionMap());
   LOGGER.log(INFO, "password init {0}", usrLogin.isInitialPassWord());
   if (usrLogin.isInitialPassWord()) {
    fc.getExternalContext().getSessionMap().put("userLogin", usrLogin);
    loginStatus = "initPw";
   } else {
   loginStatus = "loginOk";
   }
   LOGGER.log(INFO, "loginStatus {0}",loginStatus);
  } else {
   LOGGER.log(INFO, "PW invald");
   authMgr.loginFailed(usrLogin);
   
   loginStatus = "loginError";
   MessageUtil.addClientErrorMessage("loginFrm:msgs", "errRepeatPw", "errorText");
   PrimeFaces.current().ajax().update("loginFrm:msgs");
  }
  
 }

 public void onReturnFromPWuPdate(SelectEvent evt){
  LOGGER.log(INFO, "Called onReturnFromPWuPdate");
  FacesContext ctx = FacesContext.getCurrentInstance();
    ExternalContext extContext = ctx.getExternalContext();
    String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/secure/home.xhtml"));
    LOGGER.log(INFO, "url {0}", url);
    try {
     extContext.redirect(url);
    LOGGER.log(INFO, "redirect to home called");
     
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }
  
 }
 public String onUpdatePwAction(){
  String action;
  LOGGER.log(INFO, "onUpdatePwAction called with pw {0} repeat {1} login {2}", new Object[]{
   initPwd1,initPwd2,usrLogin});
 
  usrLogin.setPassChanged(new Date());
  authMgr.userPasswordUpdate(usrLogin, initPwd1, this.getView());
  MessageUtil.addInfoMessage("usrPwdUpdt", "blacResponse");
  action = "home";
  FacesContext ctx = FacesContext.getCurrentInstance();
    ExternalContext extContext = ctx.getExternalContext();
    String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/secure/home.xhtml"));
    LOGGER.log(INFO, "url {0}", url);
    try {
     extContext.redirect(url);
     return null;
     } catch (IOException ioe) {
      throw new FacesException(ioe);
    }
   
  //return action;
  
 }
 
 public void onSaveInitPw() throws NoSuchAlgorithmException {
  LOGGER.log(INFO, "onSaveInitPw called");
  LOGGER.log(INFO, "usrLogin {0} user name {1} ", new Object[]{usrLogin, userName});
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if (this.userChangedPass.equalsIgnoreCase(this.userPass)) {
   MessageUtil.addWarnMessage("userNewPwDiff", "validationText");
   LOGGER.log(INFO, "Password the same. Init pw {0} revised pw {1}", new Object[]{userPass, userChangedPass});
   userChangedPass = null;
   return;
  }
  
   String newPwHash = PasswordUtil.generateStorngPasswordHash(userChangedPass);
   LOGGER.log(INFO, "Hash pw {0}", newPwHash);
   usrLogin.setFailedAttempts(0);
   usrLogin.setPassChanged(new Date());
   usrLogin.setInitialPassWord(false);
   UserRec usrRec = this.getLoggedInUser();
   
   usrLogin.setChangedOn(new Date());
   
   usrLogin = authMgr.userPasswordUpdate(usrLogin, newPwHash, this.getView());
   MessageUtil.addInfoMessage("userPwUpdated", "validationText");
   PrimeFaces.current().executeScript("PF('pwResetWv').hide()");
   loginStatus = "loginOk";
   


 }
 public void validateInitPasswords(FacesContext c, UIComponent comp, Object val){
  LOGGER.log(INFO, "usrPwd1 {0} usrPwd2", new Object[]{initPwd1,initPwd2});
  if(StringUtils.isEmpty(initPwd1) || StringUtils.isEmpty(initPwd2) || 
          !StringUtils.equals(initPwd1, initPwd2)){
   MessageUtil.addClientErrorMessage("initPwFrm:errMsg", "userPw1Pw2", "validationText");
   ((EditableValueHolder) val).setValid(false);
   
  }else{
   ((EditableValueHolder) val).setValid(true);
  }

 }
}
