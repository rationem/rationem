/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.sec;

import com.rationem.busRec.user.UserLoginRec;
import com.rationem.busRec.user.UserRoleRec;
import com.rationem.ejbBean.dataManager.UserDM;
import com.rationem.entity.user.User;
import com.rationem.entity.user.UserLogin;
import com.rationem.entity.user.UserRole;
import com.rationem.entity.audit.AuditRationemUserLogin;
import com.rationem.entity.audit.AuditAddress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import static java.util.logging.Level.INFO;
import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.InheritanceType;
import javax.persistence.Persistence;




/**
 *
 * @author Chris
 * @param <AuditUserLogin>
 */
@Stateless
@LocalBean

public class AuthDM<AuditUserLogin> {
 private static final Logger logger = Logger.getLogger(AuthDM.class.getName());
 @PersistenceContext(unitName = "rationemPU")
 private EntityManager em;
 private EntityTransaction trans;
 private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
 
 private EntityManager emUser;

 @EJB
 UserDM usrDm;
 @PostConstruct
 private void init() {
  //em = Persistence.createEntityManagerFactory("rationemPU").createEntityManager();
  FacesContext fc = FacesContext.getCurrentInstance();
     String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
     HashMap properties = new HashMap();
     properties.put("tenantId", tenantId);
     properties.put("eclipselink.session-name", "sessionName"+tenantId);
     emUser = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
 }
 
 private void updateEmUser(){
  FacesContext fc = FacesContext.getCurrentInstance();
     String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
     logger.log(INFO, "TennantId from session {0}", tenantId);
     HashMap properties = new HashMap();
     properties.put("tenantId", tenantId);
     properties.put("eclipselink.session-name", "sessionName"+tenantId);
     emUser = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
 }

 private AuditRationemUserLogin buildAuditUserLogin(UserLogin ul,String pg, 
   char userAct) {
  AuditRationemUserLogin aud =  new AuditRationemUserLogin() ;
  aud.setAuditDate(new Date());
  aud.setUsrAction(userAct);
 
  aud.setLogin(ul);
  aud.setSource(pg);
  em.persist(aud);
  
  return aud;
 }
 
 private UserLogin buildUserLogin(UserLoginRec loginRec, String pg) {
  logger.log(INFO, "buildUserLogin called user rec id {0}",loginRec.getId());
  UserLogin ul;
  boolean newLogin = false;
  boolean changedLogin = false;
  
  if(loginRec.getId() == null) {
   newLogin = true;
   ul = new UserLogin();
   
   ul.setTenant(loginRec.getTenant());
   em.persist(ul);
  }else {
   ul = em.find(UserLogin.class, loginRec.getId());
  }
  if(newLogin) {
   ul.setFailedAttempts(loginRec.getFailedAttempts());
   ul.setInitialPassWord(loginRec.isInitialPassWord());
   ul.setLogonName(loginRec.getLogonName());
   ul.setPass(loginRec.getPass());
   ul.setPassChanged(loginRec.getPassChanged());
   ul.setLastLogin(loginRec.getLastLogin());
   List<UserRoleRec> roles = loginRec.getUserRoles();
   if(roles != null){
    List<UserRole> roleList = new ArrayList<UserRole>();
    for(UserRoleRec roleRec : roles){
     
     UserRole uRole;
     if(roleRec.getId() == null || roleRec.getId() < 1){
      uRole = new UserRole();
      uRole.setRoleCode(roleRec.getRoleCode());
      uRole.setUsr(ul);
      em.persist(uRole);
      roleList.add(uRole);
     }else{
      uRole = em.find(UserRole.class, roleRec.getId(), OPTIMISTIC);
      roleList.add(uRole);
     }
     ul.setUserRoles(roleList);
    }
   }
  }else {
   
   /*if(ul.getFailedAttempts() != loginRec.getFailedAttempts()) {
    
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg, chUsr, 'U');
    aud.setFieldName("USR_FAILED_LOGINS");
    aud.setOldValue(String.valueOf(ul.getFailedAttempts()));
    aud.setNewValue(String.valueOf(loginRec.getFailedAttempts()));
    ul.setFailedAttempts(loginRec.getFailedAttempts());
    changedLogin = true;
   }
   if(!Objects.equals(ul.getLastLogin(), loginRec.getLastLogin())) {
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg, chUsr, 'U');
    aud.setFieldName("USR_FAILED_LOGINS");
    aud.setOldValue(String.valueOf(ul.getFailedAttempts()));
    aud.setNewValue(String.valueOf(loginRec.getFailedAttempts()));
    ul.setFailedAttempts(loginRec.getFailedAttempts());
    changedLogin = true;
   }
   */
   if(!StringUtils.equals(ul.getLogonName(), loginRec.getLogonName())) {
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg,  'U');
    aud.setFieldName("USR_LOGON_NAME");
    aud.setOldValue(ul.getLogonName());
    aud.setNewValue(loginRec.getLogonName());
    ul.setLogonName(loginRec.getLogonName());
    changedLogin = true;
   }
   if(!StringUtils.equals(ul.getPass(), loginRec.getPass())) {
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg,  'U');
    aud.setFieldName("USR_LOGON_PASS");
    ul.setPass(loginRec.getPass());
    changedLogin = true;
   }
   if(!Objects.equals(ul.getPassChanged(), loginRec.getPassChanged())) {
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg,  'U');
    aud.setFieldName("USR_FAILED_LOGINS");
    String oldDate = fmt.format(ul.getPassChanged());
    String newDate = fmt.format(loginRec.getPassChanged());
    aud.setOldValue(oldDate);
    aud.setNewValue(newDate);
    ul.setPassChanged(loginRec.getPassChanged());
    changedLogin = true;
   }
   if(ul.isInitialPassWord() != loginRec.isInitialPassWord()) {
    AuditRationemUserLogin aud = buildAuditUserLogin(ul, pg,  'U');
    aud.setFieldName("USR_LOGON_INIT_PW");
    aud.setOldValue(String.valueOf(ul.isInitialPassWord()));
    aud.setNewValue(String.valueOf(loginRec.isInitialPassWord()));
    ul.setInitialPassWord(loginRec.isInitialPassWord());
    changedLogin = true;
   }
   
  }
  
  return ul;
 }
 private UserLoginRec buildUserLoginRec(UserLogin usr) {
  
  UserLoginRec rec = new UserLoginRec();
  rec.setFailedAttempts(usr.getFailedAttempts());
  rec.setId(usr.getId());
  rec.setInitialPassWord(usr.isInitialPassWord());
  rec.setLogonName(usr.getLogonName());
  rec.setPass(usr.getPass());
  rec.setPassChanged(usr.getPassChanged());
  
  rec.setTenant(usr.getTenant());
  
  rec.setUserId(usr.getUserId());
  rec.setLastLogin(usr.getLastLogin());
  return rec;
 }
 
 
 private void createDefaultLogins() {
  logger.log(INFO, "createDefaultLogins");
  
  trans.begin();
  UserLogin usr = new UserLogin();
  usr.setLogonName("usr100");
  usr.setPass("usr100");
  usr.setTenant("100");
  usr.setInitialPassWord(true);
  em.persist(usr);
  UserRole defRole = new UserRole();
  defRole.setUsr(usr);
  defRole.setRoleCode("ALL");
  em.persist(defRole);
  //em.flush();
  usr = new UserLogin();
  usr.setLogonName("usr200");
  usr.setPass("usr200");
  usr.setTenant("200");
  usr.setInitialPassWord(true);
  em.persist(usr);
  defRole = new UserRole();
  defRole.setUsr(usr);
  defRole.setRoleCode("ALL");
  em.persist(defRole);
  //em.flush();
  //trans.commit();
 }
 
 

 
 public List<UserLoginRec> getLogins() {
  //em = Persistence.createEntityManagerFactory("rationemPU").createEntityManager();


  Query q = em.createNamedQuery("allUsrLogin");
  List rs = q.getResultList();
  logger.log(INFO, "Get users returns {0}", rs);
  List<UserLoginRec> usrList = new ArrayList<UserLoginRec>();
  if (rs == null || rs.isEmpty()) {
   return null;
  }
  ListIterator rsLi = rs.listIterator();
  while (rsLi.hasNext()) {
   UserLogin usrDb = (UserLogin) rsLi.next();
   
   UserLoginRec usrRec = this.buildUserLoginRec(usrDb);
   
   usrList.add(usrRec);
  }

  logger.log(INFO, "User logins returned {0}", usrList);
  return usrList;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public UserLoginRec getUserByName(String name)  {
  logger.log(INFO, "getUserByName called with name {0}", name);
  
  TypedQuery<UserLogin> q = em.createNamedQuery("usrLoginByName",UserLogin.class);
  q.setParameter("uName", name);

  List<UserLogin> rs = q.getResultList();
  ListIterator<UserLogin> rsLi = rs.listIterator();
  while (rsLi.hasNext()) {
   UserLogin usrDb = rsLi.next();
   logger.log(INFO, "usrDb {0}", usrDb);
   UserLoginRec usr = buildUserLoginRec( usrDb);
   
   return usr;
  }
  logger.log(INFO, "Could not find user {0}",name);
  return null;
   
 }

 public UserLoginRec userLoginUpdate(UserLoginRec loginRec, String page){
  logger.log(INFO, "userLoginUpdate called with {0}", loginRec);
  
  UserLogin login = buildUserLogin(loginRec, page);// = em.find(UserLogin.class, loginRec.getId(), OPTIMISTIC);
  return loginRec;
 }
 
 public UserLoginRec userLoginFailed(UserLoginRec loginRec){
  logger.log(INFO, "userLoginFailed called {0}", loginRec.getId());
  UserLogin lg = em.find(UserLogin.class, loginRec.getId());
  int currNum = lg.getFailedAttempts();
  currNum++;
  lg.setFailedAttempts(currNum);
  loginRec.setFailedAttempts(currNum);
  logger.log(INFO, "Failed login count {0}", loginRec.getFailedAttempts());
  return loginRec;
 }
 
 public UserLoginRec userPasswordUpdate(UserLoginRec login,  String hashedPw,
   String pg) {
  logger.log(INFO, "userPasswordUpdate called with usr id{0} wf {1}  pw hashed {3}",
          new Object[]{login.getId(),  hashedPw});
  //trans.begin();
  
  login.setPass(hashedPw);
  TypedQuery<UserLogin> q = em.createNamedQuery("usrLoginByName",UserLogin.class);
  q.setParameter("uName", login.getLogonName());
  UserLogin usrLgin = q.getSingleResult();
  
  //UserLogin usrLgin = em.find(UserLogin.class, login.getId(), OPTIMISTIC);
  
  boolean changed = false;
  if(!usrLgin.isInitialPassWord()) {
   AuditRationemUserLogin aud = buildAuditUserLogin(usrLgin, pg,  'U');
   aud.setFieldName("USR_LOGON_INIT_PW");
   aud.setOldValue(String.valueOf(usrLgin.isInitialPassWord()));
   usrLgin.setInitialPassWord(false);
   aud.setNewValue(String.valueOf(usrLgin.isInitialPassWord()));
   changed = true;
  }
  
  logger.log(INFO, "Rec login new pass {0} , db login pass {1}", 
          new Object[]{login.getPass(),usrLgin.getPass()});
  if(!StringUtils.equals(login.getPass(), usrLgin.getPass())) {
   AuditRationemUserLogin aud = buildAuditUserLogin(usrLgin, pg, 'U');
   aud.setFieldName("USR_LOGON_PASS");
   usrLgin.setPass(hashedPw);
   changed = true;
   logger.log(INFO, "Password in db object changed to {0]}", usrLgin.getPass());
   usrLgin.setInitialPassWord(false);
  }
  
  if(!Objects.equals(login.getPassChanged(), usrLgin.getPassChanged()) ) {
   AuditRationemUserLogin aud = buildAuditUserLogin(usrLgin, pg, 'U');
   aud.setFieldName("USR_LOGON_PW_CH");
   if(usrLgin.getPassChanged() != null) {
    String oldDate = fmt.format(usrLgin.getPassChanged());
    aud.setOldValue(oldDate);
   }
   if(login.getPassChanged() != null) {
    String newDate = fmt.format(login.getPassChanged());
    aud.setOldValue(newDate);
   }
   usrLgin.setPassChanged(login.getPassChanged());
   //login.setPassChanged(usrLgin.getPassChanged());
   changed = true;
  }
  if(usrLgin.getFailedAttempts() != 0) {
   AuditRationemUserLogin aud = buildAuditUserLogin(usrLgin, pg,  'U');
   aud.setFieldName("USR_FAILED_LOGINS");
   aud.setOldValue(String.valueOf(usrLgin.getFailedAttempts()));
   aud.setNewValue("0");
   usrLgin.setFailedAttempts(0);
   changed = true;
  }
  logger.log(INFO, "changed {0}", changed);
  
  
  return login;
 }

 public UserLoginRec userLoggedIn(UserLoginRec rec) {
  UserLogin lgIn = em.find(UserLogin.class, rec.getId());
  lgIn.setLastLogin(rec.getLastLogin());
  lgIn.setFailedAttempts(0);
  return rec;
 }
 public UserLoginRec createUserLogin(UserLoginRec usr, boolean defaulted) {
  logger.log(INFO, "createUserLogin called with user name {0}", usr.getLogonName());
  logger.log(INFO, "UserId id {0} for login", usr.getUserId());
//  trans.begin();
  boolean defaultUsr = defaulted;
  if(usr.getLogonName().equalsIgnoreCase("user2") || usr.getLogonName().equalsIgnoreCase("user1")){
   defaultUsr = true;
  }
  UserLogin usrLgin = new UserLogin();
  usrLgin.setFailedAttempts(usr.getFailedAttempts());
  usrLgin.setInitialPassWord(usr.isInitialPassWord());
  usrLgin.setLogonName(usr.getLogonName());
  usrLgin.setPass(usr.getPass());
  usrLgin.setPassChanged(usr.getPassChanged());
  
  usrLgin.setTenant(usr.getTenant());
  
  usrLgin.setUserId(usr.getUserId());
  List<UserRoleRec> roles = usr.getUserRoles();
  if(roles != null){
   List<UserRole> roleList = new ArrayList<UserRole>();
   for(UserRoleRec roleRec : roles){
    
    UserRole uRole;
    if(roleRec.getId() == null || roleRec.getId() < 1){
     uRole = new UserRole();
     uRole.setRoleCode(roleRec.getRoleCode());
     uRole.setUsr(usrLgin);
     em.persist(uRole);
     roleList.add(uRole);
    }else{
     uRole = em.find(UserRole.class, roleRec.getId(), OPTIMISTIC);
     roleList.add(uRole);
    }
    usrLgin.setUserRoles(roleList);
   }
  }
  
  em.persist(usrLgin);
  //trans.commit();
  usr.setId(usrLgin.getId());
  logger.log(INFO, "createUserLogin user returns name {0} id {1}", new Object[]{usrLgin.getLogonName(),usr.getId()});
  return usr;
 }

 public boolean usersExist() {
  Query q = em.createNamedQuery("allUsrLogin");
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return false;
  }else{
   return true;
  }
  
 }

 public boolean defaultUsersExist() {
  Query q = em.createNamedQuery("allUsrLogin");
  List rs = q.getResultList();
  logger.log(INFO, "AuthDM.defaultUsersExist users {0}", rs);
  if(rs == null || rs.isEmpty()){
   return false;
  }else{
   return true;
  }
  
 }

 

 public UserLoginRec setUserForLogin(UserLoginRec loginRec, User usr) {
  logger.log(INFO, "authDM.setUserForLogin login rec {0} usr {1}", new Object[]{loginRec,usr});
  FacesContext ctx = FacesContext.getCurrentInstance();
  ctx.getExternalContext().getSessionMap().put("tenantId", loginRec.getTenant());
  UserLogin lg = em.find(UserLogin.class, loginRec.getId(), OPTIMISTIC);
  lg.setUserId(usr.getId());
  em.flush();
  logger.log(INFO, "User from login {0}", lg.getUserId());
  em.flush();
  return loginRec;
 }
 
 

 
}
