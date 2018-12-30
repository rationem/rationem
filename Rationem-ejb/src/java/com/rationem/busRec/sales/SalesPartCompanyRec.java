/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.sales;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.util.GenUtilServer;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Chris
 */
public class SalesPartCompanyRec implements Serializable {
 private Long id;
 private SalesPartRec part; 
 private CompanyBasicRec company;
 private FiGlAccountCompRec salesAccount;
 private SalesCatRec category;
 private FundRec fund;
 private UomRec uom;
 private double saleValue;
 private double stockValue;
 private double costValue;
 private FiGlAccountCompRec cosAccount;
 private FiGlAccountCompRec stockAccount;
 private boolean costOfSalesAccounting;
 private Date validTo;
 private boolean active;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public SalesPartCompanyRec() {
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public boolean isActive() {
  return active;
 }

 public void setActive(boolean active) {
  this.active = active;
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

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 public SalesCatRec getCategory() {
  return category;
 }

 public void setCategory(SalesCatRec category) {
  this.category = category;
 }

 public FiGlAccountCompRec getCosAccount() {
  return cosAccount;
 }

 public void setCosAccount(FiGlAccountCompRec cosAccount) {
  this.cosAccount = cosAccount;
 }

 public FiGlAccountCompRec getStockAccount() {
  return stockAccount;
 }

 public void setStockAccount(FiGlAccountCompRec stockAccount) {
  this.stockAccount = stockAccount;
 }

 public boolean isCostOfSalesAccounting() {
  return costOfSalesAccounting;
 }

 public void setCostOfSalesAccounting(boolean costOfSalesAccounting) {
  this.costOfSalesAccounting = costOfSalesAccounting;
 }

 public double getCostValue() {
  return costValue;
 }
 
 public String getCostValueFormatted(){
  return GenUtilServer.numberDp(costValue, company.getCurrency().getMinorUnit());
 }

 public void setCostValue(double costValue) {
  this.costValue = costValue;
 }
 
 

 public double getStockValue() {
  return stockValue;
 }

 public String getStockValueFormatted(){
  return GenUtilServer.numberDp(stockValue, company.getCurrency().getMinorUnit());
 }
 
 public void setStockValue(double stockValue) {
  this.stockValue = stockValue;
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

 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public SalesPartRec getPart() {
  return part;
 }

 public void setPart(SalesPartRec part) {
  this.part = part;
 }

 public double getSaleValue() {
  return saleValue;
 }

 public void setSaleValue(double saleValue) {
  this.saleValue = saleValue;
 }
 
 public String getSaleValueFormatted(){
  return GenUtilServer.numberDp(saleValue, this.company.getCurrency().getMinorUnit());
 }

 public FiGlAccountCompRec getSalesAccount() {
  
  if(salesAccount == null){
   salesAccount = new FiGlAccountCompRec();
   
  }
  return salesAccount;
  
 }

 public void setSalesAccount(FiGlAccountCompRec salesAccount) {
  this.salesAccount = salesAccount;
 }

 public UomRec getUom() {
  return uom;
 }

 public void setUom(UomRec uom) {
  this.uom = uom;
 }

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
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
  final SalesPartCompanyRec other = (SalesPartCompanyRec) obj;
  if (!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 
 
}
