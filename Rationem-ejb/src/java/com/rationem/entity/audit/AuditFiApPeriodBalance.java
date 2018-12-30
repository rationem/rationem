/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.arap.FiApPeriodBalance;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditFiApPeriodBalance")
@Table(name="audit71")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditFiApPeriodBalance extends AuditBase{
 
 @ManyToOne
 @JoinColumn(name="ap_per_bal_id",referencedColumnName="ap_per_bal_id")
 private FiApPeriodBalance periodBalance;

 public FiApPeriodBalance getPeriodBalance() {
  return periodBalance;
 }

 public void setPeriodBalance(FiApPeriodBalance periodBalance) {
  this.periodBalance = periodBalance;
 }
 
 
 
}
