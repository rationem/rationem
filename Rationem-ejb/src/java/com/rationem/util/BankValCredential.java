/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class BankValCredential implements Serializable {

  private Long id;
  private String type;
  private String userName;
  private String password;

  public BankValCredential() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


}
