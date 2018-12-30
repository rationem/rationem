/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.sales;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class SalesCatRec implements Serializable {
 private Long id;
 private String code;
 private String sortDescr;
 private String longDescr;
 private CompanyBasicRec company;
 private List<SalesCatRec> subCategories;
 private SalesCatRec salesCatParent;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

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

 public String getCode() {
  return code;
 }

 public void setCode(String code) {
  this.code = code;
 }

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
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

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getLongDescr() {
  return longDescr;
 }

 public void setLongDescr(String longDescr) {
  this.longDescr = longDescr;
 }

 public String getSortDescr() {
  return sortDescr;
 }

 public void setSortDescr(String sortDescr) {
  this.sortDescr = sortDescr;
 }

 
 public SalesCatRec getSalesCatParent() {
  return salesCatParent;
 }

 public void setSalesCatParent(SalesCatRec salesCatParent) {
  this.salesCatParent = salesCatParent;
 }

 public List<SalesCatRec> getSubCategories() {
  if(subCategories == null){
   subCategories = new ArrayList<SalesCatRec>();
  }
  return subCategories;
 }

 public void setSubCategories(List<SalesCatRec> subCategories) {
  
  this.subCategories = subCategories;
 }

 
}
