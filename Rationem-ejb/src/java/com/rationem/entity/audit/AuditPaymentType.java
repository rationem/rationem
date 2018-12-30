/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.arap.PaymentType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditPaymentType")
@Table(name="audit23")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditPaymentType extends AuditBase {
 @ManyToOne
 @JoinColumn(name="pay_type_id", referencedColumnName="pay_type_id")
 private PaymentType payType;

 public PaymentType getPayType() {
  return payType;
 }

 public void setPayType(PaymentType payType) {
  this.payType = payType;
 }
 
 
}
