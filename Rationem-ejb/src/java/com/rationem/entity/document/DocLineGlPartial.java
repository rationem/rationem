/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.ma.ProgramAccount;
import com.rationem.entity.ma.Programme;
import com.rationem.entity.salesTax.vat.VatCodeCompany;

import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;




/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocLineGlPartial")
@PrimaryKeyJoinColumn(name="doc_line_id",referencedColumnName = "doc_line_id")
@Table(name="doc_line09" )
@NamedQuery(name="glPartialLinesForDoc", query="select ln.id from DocLineGlPartial ln where ln.docHeaderBase.id = :docId ")
public class DocLineGlPartial extends DocLineBasePartial {
 
 
private int currID;
 @Column(name="doc_amount")
 private double docAmount;
 
 
 @ManyToOne
 @JoinColumn(name="gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp GlAccount;
 @ManyToOne
 @JoinColumn(name="rest_fnd_id", referencedColumnName="restricted_fund_id")
 private Fund restrictedFund;
 @ManyToOne
 @JoinColumn(name="cost_centre_id", referencedColumnName="COST_CENT_ID")
 private CostCentre costCentre;
 @ManyToOne
 @JoinColumn(name="cost_acnt_act_id", referencedColumnName="COST_ACCNT_ID")
 private CostAccountDirect costAccount;
 @ManyToOne
 @JoinColumn(name="programme_id", referencedColumnName="PROGRAMME_ID")
 private Programme programme;
 @ManyToOne
 @JoinColumn(name="prog_accnt_id", referencedColumnName="PROG_ACCNT_ID")
 private ProgramAccount programCostAccount;
 @ManyToOne
 @JoinColumn(name="comp_vat_code_id" ,referencedColumnName="vat_code_comp_id" )
 private VatCodeCompany vatCodeComp;

 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public FiGlAccountComp getGlAccount() {
  return GlAccount;
 }

 public void setGlAccount(FiGlAccountComp GlAccount) {
  this.GlAccount = GlAccount;
 }

 public Fund getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(Fund restrictedFund) {
  this.restrictedFund = restrictedFund;
 }


 public CostCentre getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentre costCentre) {
  this.costCentre = costCentre;
 }

 public CostAccountDirect getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirect costAccount) {
  this.costAccount = costAccount;
 }

 public Programme getProgramme() {
  return programme;
 }

 public void setProgramme(Programme programme) {
  this.programme = programme;
 }

 public ProgramAccount getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccount programCostAccount) {
  this.programCostAccount = programCostAccount;
 }

 public VatCodeCompany getVatCodeComp() {
  return vatCodeComp;
 }

 public void setVatCodeComp(VatCodeCompany vatCodeComp) {
  this.vatCodeComp = vatCodeComp;
 }
 
 
}
