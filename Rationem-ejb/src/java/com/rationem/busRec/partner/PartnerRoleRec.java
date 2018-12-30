/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.partner;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public class PartnerRoleRec implements Serializable {
 private Long id;
 
 private String roleCode;
 private String roleName;
 private boolean inUse;
 private boolean taxable;
 private boolean userRole;
 
 public List<PartnerBaseRec> rolesForPtnr;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private Long revision;

 public PartnerRoleRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public boolean isInUse() {
  return inUse;
 }

 public void setInUse(boolean inUse) {
  this.inUse = inUse;
 }

 
 public String getRoleCode() {
  return roleCode;
 }

 public void setRoleCode(String roleCode) {
  this.roleCode = roleCode;
 }

 public String getRoleName() {
  return roleName;
 }

 public void setRoleName(String roleName) {
  this.roleName = roleName;
 }

 public List<PartnerBaseRec> getRolesForPtnr() {
  return rolesForPtnr;
 }

 public void setRolesForPtnr(List<PartnerBaseRec> rolesForPtnr) {
  this.rolesForPtnr = rolesForPtnr;
 }

 public boolean isTaxable() {
  return taxable;
 }

 public void setTaxable(boolean taxable) {
  this.taxable = taxable;
 }

 public boolean isUserRole() {
  return userRole;
 }

 public void setUserRole(boolean userRole) {
  this.userRole = userRole;
 }
 
 

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public Long getRevision() {
  return revision;
 }

 public void setRevision(Long revision) {
  this.revision = revision;
 }

 
 
}
