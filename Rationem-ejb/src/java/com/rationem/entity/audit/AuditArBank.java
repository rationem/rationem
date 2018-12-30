/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.arap.ArBankAccount;
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
@DiscriminatorValue("audit.AuditArBank")
@Table(name="audit32")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditArBank extends AuditBase  {
 @ManyToOne
 @JoinColumn(name="ar_bank_id", referencedColumnName="ar_bank_id")
 private ArBankAccount arBank;

 public ArBankAccount getArBank() {
  return arBank;
 }

 public void setArBank(ArBankAccount arBank) {
  this.arBank = arBank;
 }

 
 
}
