/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class UserTenant implements Serializable {
 
 private String principleName;
 private String tenantRef;

 public UserTenant() {
 }

 public UserTenant(String principleName, String tenantRef) {
  this.principleName = principleName;
  this.tenantRef = tenantRef;
 }

 public String getPrincipleName() {
  return principleName;
 }

 public void setPrincipleName(String principleName) {
  this.principleName = principleName;
 }

 public String getTenantRef() {
  return tenantRef;
 }

 public void setTenantRef(String tenantRef) {
  this.tenantRef = tenantRef;
 }

 @Override
 public String toString() {
  return "UserTenant{" + "principleName=" + principleName + ", tenantRef=" + tenantRef + '}';
 }
 
 
 
}
