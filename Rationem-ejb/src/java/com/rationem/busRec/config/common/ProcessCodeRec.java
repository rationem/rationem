/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class ProcessCodeRec implements Serializable {
 private Long id;
 private String name;
 private String description;
 private ModuleRec module;
 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;
 private long changes;

 public ProcessCodeRec() {
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

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public ModuleRec getModule() {
  return module;
 }

 public void setModule(ModuleRec module) {
  this.module = module;
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

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }

 
 
}
