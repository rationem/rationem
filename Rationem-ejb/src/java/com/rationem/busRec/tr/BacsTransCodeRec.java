/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class BacsTransCodeRec implements Serializable {
 private Long id;
 /**
  * 2 digit BACS code
  */
 private String ptnrBnkTransCode;
 private String contrTransCode;
 /**
  * short description
  */
 private String name;
 /**
  * Long description
  */
 private String description;
 /**
  * If true this is a direct collection - claim money from 3rd party's bank
  * If false this is a direct payment - payment to a 3rd party
  */
 private boolean collection;
 private boolean debit;
 private String prCode;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public BacsTransCodeRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getPtnrBnkTransCode() {
  return ptnrBnkTransCode;
 }

 public void setPtnrBnkTransCode(String transCode) {
  this.ptnrBnkTransCode = transCode;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

 
 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isCollection() {
  return collection;
 }

 public void setCollection(boolean collection) {
  this.collection = collection;
 }

 public String getContrTransCode() {
  return contrTransCode;
 }

 public void setContrTransCode(String contrTransCode) {
  this.contrTransCode = contrTransCode;
 }
 

 public String getPrCode() {
  return prCode;
 }

 public void setPrCode(String prCode) {
  this.prCode = prCode;
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

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 @Override
 public int hashCode() {
  int hash = 3;
  hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final BacsTransCodeRec other = (BacsTransCodeRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "bacsTransCodeRec{" + "id=" + id + ", transCode=" + ptnrBnkTransCode + '}';
 }
 
 
}
