/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.audit;

import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class AuditBaseRec {

 private Long id;
    
 private Date auditDate;
/**
  * User action is either I=Insert, U=Update, D=Delete
  */
 private char usrAction;
    /**
     * page user was on when the event occurred
     */
 private String source;


 private String fieldName;
 private String fieldNameTranslated;
 private String fieldCode;
 private String newValue;
 private String oldValue;
 private UserRec createdBy;

  public AuditBaseRec() {
  }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Date getAuditDate() {
  return auditDate;
 }

 public void setAuditDate(Date auditDate) {
  this.auditDate = auditDate;
 }

 public char getUsrAction() {
  return usrAction;
 }

 public void setUsrAction(char usrAction) {
  this.usrAction = usrAction;
 }

 public String getSource() {
  return source;
 }

 public void setSource(String source) {
  this.source = source;
 }

 public String getFieldCode() {
  return fieldCode;
 }

 public void setFieldCode(String fieldCode) {
  this.fieldCode = fieldCode;
 }

 public String getFieldName() {
  return fieldName;
 }

 public void setFieldName(String fieldName) {
  this.fieldName = fieldName;
 }

 /**
  * Translation to be done in web layer where user language is known 
  * @return 
  */
 public String getFieldNameTranslated() {
  return fieldNameTranslated;
 }

 public void setFieldNameTranslated(String fieldNameTranslated) {
  this.fieldNameTranslated = fieldNameTranslated;
 }
 
 

 public String getNewValue() {
  return newValue;
 }

 public void setNewValue(String newValue) {
  this.newValue = newValue;
 }

 public String getOldValue() {
  return oldValue;
 }

 public void setOldValue(String oldValue) {
  this.oldValue = oldValue;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

  




}
