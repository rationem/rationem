/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatRegistration;
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
@DiscriminatorValue("audit.AuditVatRegistration")
@Table(name="audit12")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatRegistration extends AuditBase {
 @ManyToOne
 @JoinColumn(name="vat_reg_id",referencedColumnName="vat_reg_id")
 private VatRegistration vatRegistration;

 public VatRegistration getVatRegistration() {
  return vatRegistration;
 }

 public void setVatRegistration(VatRegistration vatRegistration) {
  this.vatRegistration = vatRegistration;
 }

 
}
