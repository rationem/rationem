/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.persistence.Version;

/**
 *
 * @author Chris
 */
@Entity
@NamedQueries({
 @NamedQuery(name = "allUsrLogin", query = "Select u from  UserLogin u"),
 @NamedQuery(name = "usrLoginByName", 
        query = "Select u from  UserLogin u where u.logonName = :uName")
})
@SequenceGenerator(name = "UserLogin_s1", sequenceName = "user_s1", allocationSize = 1)
@Table(name = "user_login")
public class UserLogin  implements Serializable {
 private static final long serialVersionUID = 1L;
 @OneToMany(mappedBy = "usr")
 private List<UserRole> userRoles;
 @Id
 @GeneratedValue(generator = "UserLogin_s1")
 @Column(name = "user_login_id")
 private Long id;
 @Column(name = "tenant")
 private String tenant;
 @Column(name = "logon_name")
 private String logonName;
 @Column(name = "pass")
 private String pass;
 
 @Column(name = "failed_logins")
 private int failedAttempts;
 @Temporal(TIMESTAMP)
 @Column(name = "pass_changed_date")
 private Date passChanged;
 @Column(name = "initial_pass")
 private boolean initialPassWord;
 
 @Column(name="user_id")
 private Long userId;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 
 
 
 @Temporal(TIMESTAMP)
 @Column(name="change_on")
 private Date changedOn;
  
 @Temporal(TIMESTAMP)
 @Column(name="last_login")
 private Date lastLogin;
 @Version
 @Column(name = "revision")
 private short revision;



 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof UserLogin)) {
   return false;
  }
  UserLogin other = (UserLogin) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 
 public Date getChangedOn() {
  return changedOn;
 }

 

 public Date getCreatedOn() {
  return createdOn;
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

 public short getRevision() {
  return revision;
 }

 
 public String getTenant() {
  return tenant;
 }

 public Long getUserId() {
  return userId;
 }

 public List<UserRole> getUserRoles() {
  return userRoles;
 }

 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 public boolean isInitialPassWord() {
  return initialPassWord;
 }


 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 
 
 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
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

 public void setRevision(short revision) {
  this.revision = revision;
 }

 public void setTenant(String tenant) {
  this.tenant = tenant;
 }


 public void setUserId(Long userId) {
  this.userId = userId;
 }

 public void setUserRoles(List<UserRole> userRoles) {
  this.userRoles = userRoles;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.UserLogin[ id=" + id + " ]";
 }
}
