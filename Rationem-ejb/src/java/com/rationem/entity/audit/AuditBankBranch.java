/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.tr.bank.BankBranch;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;



/**
 *
 * @author Chris
 * 
 */
@Entity
@DiscriminatorValue("audit.AuditBankBranch")
@Table(name="audit21")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditBankBranch extends AuditBase  {
 @ManyToOne
 @JoinColumn(name="bank_branch_id", referencedColumnName="bank_branch_id")
 private BankBranch bankBranch;

 public BankBranch getBankBranch() {
  return bankBranch;
 }

 public void setBankBranch(BankBranch bankBranch) {
  this.bankBranch = bankBranch;
 }
 
 

 
}
