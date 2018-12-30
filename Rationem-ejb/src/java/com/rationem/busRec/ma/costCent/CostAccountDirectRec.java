/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.ma.costCent;

import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class CostAccountDirectRec {
 
 private Long id;
 private String ref;
 private String name;
 private String description;
 private PartnerPersonRec responsibilityOf;
 private FiGlAccountCompRec glAccount;
 private CostCentreRec costCentre;
 private List<FiPeriodBalanceRec> costBalances;
 private List<FiPeriodBalanceRec> budgetBalances;
 private Date validFrom;
 private Date validTo;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public CostAccountDirectRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
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

 public PartnerPersonRec getResponsibilityOf() {
  return responsibilityOf;
 }

 public void setResponsibilityOf(PartnerPersonRec responsibilityOf) {
  this.responsibilityOf = responsibilityOf;
 }

 public FiGlAccountCompRec getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountCompRec glAccount) {
  this.glAccount = glAccount;
 }

 public List<FiPeriodBalanceRec> getCostBalances() {
  return costBalances;
 }

 public CostCentreRec getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentreRec costCentre) {
  this.costCentre = costCentre;
 }

 public void setCostBalances(List<FiPeriodBalanceRec> costBalances) {
  this.costBalances = costBalances;
 }

 public List<FiPeriodBalanceRec> getBudgetBalances() {
  return budgetBalances;
 }

 public void setBudgetBalances(List<FiPeriodBalanceRec> budgetBalances) {
  this.budgetBalances = budgetBalances;
 }

 public Date getValidFrom() {
  return validFrom;
 }

 public void setValidFrom(Date validFrom) {
  this.validFrom = validFrom;
 }

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
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

 
}
