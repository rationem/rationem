/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.mdm;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
public class CurrencyRec implements Serializable {
 
 private Long id;
 
 private String currAlphaCode;
 private String name;
 private String currSymbol;
 private int minorUnit;
 private int currNumCode;
 private String majorUnitDescr;
 private String minorUnitDescr;
 private String majorUnitDescrPl;
 private String minorUnitDescrPl;
 
 private Date createdOn;
 private UserRec createdBy;
 private Date changedOn;
 private UserRec changedBy;
 private int changes;

 public CurrencyRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getMajorUnitDescr() {
  return majorUnitDescr;
 }

 public void setMajorUnitDescr(String majorUnitDescr) {
  this.majorUnitDescr = majorUnitDescr;
 }

 public String getMajorUnitDescrPl() {
  return majorUnitDescrPl;
 }

 public void setMajorUnitDescrPl(String majorUnitDescrPl) {
  this.majorUnitDescrPl = majorUnitDescrPl;
 }
 
 

 
 public int getMinorUnit() {
  return minorUnit;
 }

 public void setMinorUnit(int minorUnit) {
  this.minorUnit = minorUnit;
 }

 public String getMinorUnitDescr() {
  return minorUnitDescr;
 }

 public void setMinorUnitDescr(String minorUnitDescr) {
  this.minorUnitDescr = minorUnitDescr;
 }

 public String getMinorUnitDescrPl() {
  return minorUnitDescrPl;
 }

 public void setMinorUnitDescrPl(String minorUnitDescrPl) {
  this.minorUnitDescrPl = minorUnitDescrPl;
 }
 
 

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public int getCurrNumCode() {
  return currNumCode;
 }

 public void setCurrNumCode(int currNumCode) {
  this.currNumCode = currNumCode;
 }

 public String getCurrSymbol() {
  return currSymbol;
 }

 public void setCurrSymbol(String currSymbol) {
  this.currSymbol = currSymbol;
 }

 public String getCurrAlphaCode() {
  return currAlphaCode;
 }

 public void setCurrAlphaCode(String currAlphaCode) {
  this.currAlphaCode = currAlphaCode;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 
}
