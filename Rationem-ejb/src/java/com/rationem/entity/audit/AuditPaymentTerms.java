/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.arap.PaymentTerms;
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
@DiscriminatorValue("audit.AuditPaymentTerms")
@Table(name="audit66")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditPaymentTerms extends AuditBase {

 @ManyToOne
 @JoinColumn(name="pay_terms_id", referencedColumnName="pay_terms_id")
 private PaymentTerms terms;

 public PaymentTerms getTerms() {
  return terms;
 }

 public void setTerms(PaymentTerms terms) {
  this.terms = terms;
 }
 
 
}
