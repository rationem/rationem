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
public class PartnerSelectOption implements Serializable {
 
 private String ptnrType;
 private String category;
 private String name;
 private String reference;
 private String postCode;

 public PartnerSelectOption() {
 }

 public String getPtnrType() {
  return ptnrType;
 }

 public void setPtnrType(String ptnrType) {
  this.ptnrType = ptnrType;
 }

 public String getCategory() {
  return category;
 }

 public void setCategory(String category) {
  this.category = category;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getPostCode() {
  return postCode;
 }

 public void setPostCode(String postCode) {
  this.postCode = postCode;
 }

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }
 
 
 
 
}
