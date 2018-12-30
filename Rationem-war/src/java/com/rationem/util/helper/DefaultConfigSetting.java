/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author user
 */
public class DefaultConfigSetting implements Serializable {
 
 private Long id;
 private String name;
 private String code;
 private int order;

 public DefaultConfigSetting() {
  
 }

 public DefaultConfigSetting(String name, String code, int order) {
  UUID uuid = UUID.randomUUID();
  id = uuid.getLeastSignificantBits();
  this.name = name;
  this.code = code;
  this.order = order;
 }
 
 

 public Long getId() {
  
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public int getOrder() {
  return order;
 }

 public void setOrder(int order) {
  this.order = order;
 }
 
 
 
}
