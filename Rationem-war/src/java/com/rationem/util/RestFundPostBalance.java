/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;

/**
 * Used in posting rules to determine the net posted to each fund
 * @author Chris
 */
public class RestFundPostBalance {

  private Long id;
  private String name;
  private double balance;

  public RestFundPostBalance() {
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }



}
