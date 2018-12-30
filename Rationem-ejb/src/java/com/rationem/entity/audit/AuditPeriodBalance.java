/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditPeriodBalance")
@Table(name="audit58")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditPeriodBalance extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="per_bal_id", referencedColumnName="per_bal_id")
 private FiPeriodBalance perBal;

 public FiPeriodBalance getPerBal() {
  return perBal;
 }

 public void setPerBal(FiPeriodBalance perBal) {
  this.perBal = perBal;
 }
 
 
 }
