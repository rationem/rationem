/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.user;

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class UserRoleRec implements Serializable{
 
 private Long id;
 private UserLoginRec usr;
 private String roleCode;
 

 public UserRoleRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public UserLoginRec getUsr() {
  return usr;
 }

 public void setUsr(UserLoginRec usr) {
  this.usr = usr;
 }

 public String getRoleCode() {
  return roleCode;
 }

 public void setRoleCode(String roleCode) {
  this.roleCode = roleCode;
 }
 
 
 
}
