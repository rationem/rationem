/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class PostProcessFieldFeature implements Serializable  {
 
 int col ;
 String attribute;
 String value;

 public PostProcessFieldFeature() {
 }

 public int getCol() {
  return col;
 }

 public void setCol(int col) {
  this.col = col;
 }

 public String getAttribute() {
  return attribute;
 }

 public void setAttribute(String attribute) {
  this.attribute = attribute;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }
 
 
 
 
 
}
