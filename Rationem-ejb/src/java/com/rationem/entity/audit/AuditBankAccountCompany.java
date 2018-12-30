/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.tr.bank.BankAccountCompany;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;



/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditBankAccountCompany")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
@Table(name="audit25")
public class AuditBankAccountCompany extends AuditBase {
 @ManyToOne
 @JoinColumn(name="bank_acnt_comp_id", referencedColumnName="bank_account_id")
 private BankAccountCompany bnkAcntComp;

 public BankAccountCompany getBnkAcntComp() {
  return bnkAcntComp;
 }

 public void setBnkAcntComp(BankAccountCompany bnkAcntComp) {
  this.bnkAcntComp = bnkAcntComp;
 }
 

 
}
