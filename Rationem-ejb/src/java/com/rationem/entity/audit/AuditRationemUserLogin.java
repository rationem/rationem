package com.rationem.entity.audit;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.rationem.entity.user.UserLogin;

@Entity
@DiscriminatorValue("audit.AuditUserLogin")
@Table(name="audit74")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditRationemUserLogin extends AuditBase implements Serializable {
 
 

 public AuditRationemUserLogin() {
  super();
  // TODO Auto-generated constructor stub
 }
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="user_login_id",referencedColumnName="user_login_id")
 private UserLogin login;
 /**
  * @return the login
  */
 public UserLogin getLogin() {
  return login;
 }
 /**
  * @param login the login to set
  */
 public void setLogin(UserLogin login) {
  this.login = login;
 }
 
 
 
}
