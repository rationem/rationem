/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.arap.ApAccount;
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
@DiscriminatorValue("audit.AuditApAccount")
@Table(name="audit63")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditApAccount extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="ap_account_id",referencedColumnName="ap_account_id")
 private ApAccount acnt;

 public ApAccount getAcnt() {
  return acnt;
 }

 public void setAcnt(ApAccount acnt) {
  this.acnt = acnt;
 }
 
 
 
}
