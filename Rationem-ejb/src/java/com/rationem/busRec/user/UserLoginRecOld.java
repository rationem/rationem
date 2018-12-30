/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.user;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class UserLoginRecOld {
    private Long id;
    private String loginName;
    private String password;
    private UserRec user;
    private UserRec loginFor;
    private Date lastLogin;
    private int faiedLogins;
    private UserRec createdBy;
    private Date createdOn;
    private short revision;
    
     private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.user.UserLoginRec");



    public UserLoginRecOld() {
    }

    public UserLoginRecOld(Long id, String loginName, String password, UserRec loginFor, Date lastLogin, 
            int faiedLogins, UserRec createdBy, Date createdOn, short revision) {
        this.id = id;
        this.loginName = loginName;
        this.password = password;
        this.loginFor = loginFor;
        this.lastLogin = lastLogin;
        this.faiedLogins = faiedLogins;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.revision = revision;
    }

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public int getFaiedLogins() {
        return faiedLogins;
    }

    public void setFaiedLogins(int faiedLogins) {
        this.faiedLogins = faiedLogins;
    }

    

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserRec getLoginFor() {
        return loginFor;
    }

    public void setLoginFor(UserRec loginFor) {
        this.loginFor = loginFor;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
     this.password = password;
    }

    public short getRevision() {
        return revision;
    }

    public void setRevision(short revision) {
        this.revision = revision;
    }

 public UserRec getUser() {
  return user;
 }

 public void setUser(UserRec user) {
  this.user = user;
 }

    
    private byte[] digest(String input) throws UnsupportedEncodingException{
      logger.log(INFO, "Called password {0}", password);
      byte[] output;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      output = md.digest(input.getBytes("UTF-5"));




    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger(UserLoginRecOld.class.getName()).log(Level.SEVERE, null, ex);
    }

      return null;
    }

    

    
   
}
