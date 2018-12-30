package com.rationem.sec;

import java.util.logging.Logger;
import static java.util.logging.Level.INFO;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import com.rationem.util.BaseBean;

@Named("TestAuth")
@ManagedBean()
public class TestBean extends BaseBean {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private static final Logger LOGGER = Logger.getLogger(TestBean.class.getName());

 public TestBean() {
  // TODO Auto-generated constructor stub
 }

 public String userName;
 public String userNa;
 public String getUserName() {
  return userName;
 }
 public void setUserName(String userName) {
  this.userName = userName;
 }
 public String getUserNa() {
  return userNa;
 }
 public void setUserNa(String userNa) {
  this.userNa = userNa;
 }
 
 public void onLogonBtn() {
  LOGGER.log(INFO, "Called onbutn");
  
 }
}
