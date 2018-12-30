/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author user
 */
public class PickListItem implements Serializable {
 
 private Long id;
 private String code;
 private String description;

 public PickListItem() {
 }

 public PickListItem(String code, String description) {
  UUID uuid = UUID.randomUUID();
  this.id = uuid.getLeastSignificantBits();
  this.code = code;
  this.description = description;
 }
 
 

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }
 
 
 
}
