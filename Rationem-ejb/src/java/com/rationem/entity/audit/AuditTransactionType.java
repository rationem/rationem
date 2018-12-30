/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.TransactionType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;


/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("AuditTransactionType")
@Table(name="audit51")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditTransactionType extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="trans_ty_id", referencedColumnName="tran_type_id")
 private TransactionType transTy;

 public TransactionType getTransTy() {
  return transTy;
 }

 public void setTransTy(TransactionType transTy) {
  this.transTy = transTy;
 }
 
 
 
}
