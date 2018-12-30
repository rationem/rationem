/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.user;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class UserLoginRec implements Serializable {

 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private Date changedOn;
 private Date createdOn;
 private int failedAttempts;
 private Long id;
 private boolean initialPassWord;
 private Date lastLogin;
 private String logonName;
 private String pass;
 private Date passChanged;
 private String salt;
 private String tenant;
 private Long userId;
 private List<UserRoleRec> userRoles;
 private int workFactor;

 public UserLoginRec() {
 }

 public int getFailedAttempts() {
  return failedAttempts;
 }

 public Long getId() {
  return id;
 }

 public Date getLastLogin() {
  return lastLogin;
 }

 public String getLogonName() {
  return logonName;
 }

 public String getPass() {
  return pass;
 }

 public Date getPassChanged() {
  return passChanged;
 }

 public String getSalt() {
  return salt;
 }

 public String getTenant() {
  return tenant;
 }

 

 public Long getUserId() {
  return userId;
 }

 public List<UserRoleRec> getUserRoles() {
  return userRoles;
 }

 public int getWorkFactor() {
  return workFactor;
 }

 public boolean isInitialPassWord() {
  return initialPassWord;
 }

 public void setFailedAttempts(int failedAttempts) {
  this.failedAttempts = failedAttempts;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public void setInitialPassWord(boolean initialPassWord) {
  this.initialPassWord = initialPassWord;
 }

 public void setLastLogin(Date lastLogin) {
  this.lastLogin = lastLogin;
 }

 public void setLogonName(String logonName) {
  this.logonName = logonName;
 }

 public void setPass(String pass) {
  this.pass = pass;
 }

 public void setPassChanged(Date passChanged) {
  this.passChanged = passChanged;
 }

 public void setSalt(String salt) {
  this.salt = salt;
 }

 public void setTenant(String tenant) {
  this.tenant = tenant;
 }

 public void setUserId(Long userId) {
  this.userId = userId;
 }

 public void setUserRoles(List<UserRoleRec> userRoles) {
  this.userRoles = userRoles;
 }

 public void setWorkFactor(int workFactor) {
  this.workFactor = workFactor;
 }



 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }


 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }
 
 
 
}
