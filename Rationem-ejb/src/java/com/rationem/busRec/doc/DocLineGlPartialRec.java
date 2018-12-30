/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;

/**
 *
 * @author Chris
 */
public class DocLineGlPartialRec extends DocLineBasePartialRec {
 private int currID;
 private double docAmount;
 private FiGlAccountCompRec GlAccount;
 private CostCentreRec costCentre;
 private ProgrammeRec programme;
 private CostAccountDirectRec costAccount;
 private ProgramAccountRec programCostAccount;
 private VatCodeCompanyRec vatCodeComp;
 private FundRec restrictedFund;

 public DocLineGlPartialRec() {
 }

 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public FiGlAccountCompRec getGlAccount() {
  return GlAccount;
 }

 public void setGlAccount(FiGlAccountCompRec GlAccount) {
  this.GlAccount = GlAccount;
 }

 public CostAccountDirectRec getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirectRec costAccount) {
  this.costAccount = costAccount;
 }

 public CostCentreRec getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentreRec costCentre) {
  this.costCentre = costCentre;
 }

 public ProgrammeRec getProgramme() {
  return programme;
 }

 public void setProgramme(ProgrammeRec programme) {
  this.programme = programme;
 }

 public ProgramAccountRec getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccountRec programCostAccount) {
  this.programCostAccount = programCostAccount;
 }

 public FundRec getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(FundRec restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 
 public VatCodeCompanyRec getVatCodeComp() {
  return vatCodeComp;
 }

 public void setVatCodeComp(VatCodeCompanyRec vatCodeComp) {
  this.vatCodeComp = vatCodeComp;
 }
 
 
 
}
