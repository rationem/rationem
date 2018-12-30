/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.salesTax.vat;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * Company settings for a VAT code
 * @author Chris
 */
public class VatCodeCompanyRec implements Serializable {
 private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.salesTax.vatCodeCompanyRec");
 private Long id;
 private VatCodeRec vatCode;
 private CompanyBasicRec company;
 private FiGlAccountCompRec rateGlAccount;
 private double irrecoverRate;
 private boolean woffIrrecoverable;
 private boolean noIrrecoverableLine;
 private FiGlAccountCompRec chargeGlAccount;
 private FiGlAccountCompRec accrualGlAccount;
 
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;
 
 

 public VatCodeCompanyRec() {
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

 public FiGlAccountCompRec getChargeGlAccount() {
  return chargeGlAccount;
 }

 public void setChargeGlAccount(FiGlAccountCompRec chargeGlAccount) {
  this.chargeGlAccount = chargeGlAccount;
 }

 public boolean isWoffIrrecoverable() {
  return woffIrrecoverable;
 }

 public void setWoffIrrecoverable(boolean woffIrrecoverable) {
  this.woffIrrecoverable = woffIrrecoverable;
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

 public double getIrrecoverRate() {
  return irrecoverRate;
 }

 public void setIrrecoverRate(double irrecoverRate) {
  this.irrecoverRate = irrecoverRate;
 }

 public boolean isNoIrrecoverableLine() {
  return noIrrecoverableLine;
 }

 public void setNoIrrecoverableLine(boolean noIrrecoverableLine) {
  this.noIrrecoverableLine = noIrrecoverableLine;
 }

 public FiGlAccountCompRec getAccrualGlAccount() {
  return accrualGlAccount;
 }

 public void setAccrualGlAccount(FiGlAccountCompRec accrualGlAccount) {
  this.accrualGlAccount = accrualGlAccount;
 }

 public FiGlAccountCompRec getRateGlAccount() {
  return rateGlAccount;
 }

 public void setRateGlAccount(FiGlAccountCompRec rateGlAccount) {
  this.rateGlAccount = rateGlAccount;
 }

 public VatCodeRec getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeRec vatCode) {
  this.vatCode = vatCode;
 }
 
 
 
}
