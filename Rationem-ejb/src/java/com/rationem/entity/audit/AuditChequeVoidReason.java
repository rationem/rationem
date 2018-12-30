/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.ChequeVoidReason;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditChequeVoidReason")
@Table(name="audit67")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditChequeVoidReason extends AuditBase {
  
 @ManyToOne
 @JoinColumn(name="void_reason_id", referencedColumnName="void_reason_id")
 private ChequeVoidReason voidReason;

 public ChequeVoidReason getVoidReason() {
  return voidReason;
 }

 public void setVoidReason(ChequeVoidReason voidReason) {
  this.voidReason = voidReason;
 }
 
 
}
