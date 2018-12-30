/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import java.util.Date;

/**
 *
 * @author user
 */
public class ContactSelection {

 
 private Date from;
 private Date to;
 private int contactType;
 
 public ContactSelection() {
  
 }

 public Date getFrom() {
  return from;
 }

 public void setFrom(Date from) {
  this.from = from;
 }

 public Date getTo() {
  return to;
 }

 public void setTo(Date to) {
  this.to = to;
 }

 public int getContactType() {
  return contactType;
 }

 public void setContactType(int contactType) {
  this.contactType = contactType;
 }
 
 
 
 
}
