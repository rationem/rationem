/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.user;

import com.rationem.entity.user.UserLogin;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@SequenceGenerator(name = "UserRole_s1", sequenceName = "user_role_s1", allocationSize = 1)
@Table(name = "user_role")
public class UserRole implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "UserRole_s1")
 @Column(name = "user_role_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name = "user_login_id", referencedColumnName = "user_login_id")
 private UserLogin usr;
 @Column(name = "role_code")
 private String roleCode;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public UserLogin getUsr() {
  return usr;
 }

 public void setUsr(UserLogin usr) {
  this.usr = usr;
 }

 public String getRoleCode() {
  return roleCode;
 }

 public void setRoleCode(String roleCode) {
  this.roleCode = roleCode;
 }


 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof UserRole)) {
   return false;
  }
  UserRole other = (UserRole) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.UserRole[ id=" + id + " ]";
 }
}
