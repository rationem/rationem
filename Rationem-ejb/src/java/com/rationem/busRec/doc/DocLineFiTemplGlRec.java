/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;

/**
 *
 * @author user
 */
public class DocLineFiTemplGlRec extends DocLineFiTemplateRec {
 
 private FiGlAccountCompRec GlAccount;
 private FundRec restrictedFund;
 private CostCentreRec costCentre;
 private CostAccountDirectRec costAcnt;
 private ProgrammeRec programme;
 private ProgramAccountRec progAcnt;

 public CostAccountDirectRec getCostAcnt() {
  return costAcnt;
 }

 public void setCostAcnt(CostAccountDirectRec costAcnt) {
  this.costAcnt = costAcnt;
 }

 
 public FiGlAccountCompRec getGlAccount() {
  return GlAccount;
 }

 public void setGlAccount(FiGlAccountCompRec GlAccount) {
  this.GlAccount = GlAccount;
 }

 public FundRec getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(FundRec restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public CostCentreRec getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentreRec costCentre) {
  this.costCentre = costCentre;
 }

 public ProgramAccountRec getProgAcnt() {
  return progAcnt;
 }

 public void setProgAcnt(ProgramAccountRec progAcnt) {
  this.progAcnt = progAcnt;
 }

 public ProgrammeRec getProgramme() {
  return programme;
 }

 public void setProgramme(ProgrammeRec programme) {
  this.programme = programme;
 }
 
 
 
}
