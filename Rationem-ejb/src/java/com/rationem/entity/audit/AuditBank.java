/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.tr.bank.Bank;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="audit47")
@DiscriminatorValue("audit.AuditBank")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditBank extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="bank_id", referencedColumnName="bank_id")
 private Bank bank;

 public Bank getBank() {
  return bank;
 }

 public void setBank(Bank bank) {
  this.bank = bank;
 }
 
 
 
}
