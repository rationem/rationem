/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class NumberRangeTypeRec implements Serializable {
 
 private Long id;
 private String Code;
 private String Name;
 private List<NumberRangeRec> numberRanges;
 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getCode() {
  return Code;
 }

 public void setCode(String Code) {
  this.Code = Code;
 }

 public String getName() {
  return Name;
 }

 public void setName(String Name) {
  this.Name = Name;
 }

 public List<NumberRangeRec> getNumberRanges() {
  return numberRanges;
 }

 public void setNumberRanges(List<NumberRangeRec> numberRanges) {
  this.numberRanges = numberRanges;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
 }
 
 
 
}
