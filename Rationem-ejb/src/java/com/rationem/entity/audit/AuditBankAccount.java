/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.tr.bank.BankAccount;
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
@Table(name="audit22")
@DiscriminatorValue("audit.AuditBankAccount")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditBankAccount extends AuditBase {
 @ManyToOne
 @JoinColumn(name="bank_account_id", referencedColumnName="bank_account_id")
 private BankAccount bankAc;

 public BankAccount getBankAc() {
  return bankAc;
 }

 public void setBankAc(BankAccount bankAc) {
  this.bankAc = bankAc;
 }

 
}
