/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class NameLocalisationMap implements Serializable {
 
 private String name;
 private String nameLocal;

 public NameLocalisationMap() {
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getNameLocal() {
  return nameLocal;
 }

 public void setNameLocal(String nameLocal) {
  this.nameLocal = nameLocal;
 }
 
 
 
}
