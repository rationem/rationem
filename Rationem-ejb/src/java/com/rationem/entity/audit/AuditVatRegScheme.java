/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatRegScheme;
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
@DiscriminatorValue("audit.AuditVatRegScheme")
@Table(name="audit13")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatRegScheme extends AuditBase {
 @ManyToOne
 @JoinColumn(name="vat_reg_Scheme_id",referencedColumnName="vat_reg_Scheme_id")
 private VatRegScheme vatRegScheme;

 public VatRegScheme getVatRegScheme() {
  return vatRegScheme;
 }

 public void setVatRegScheme(VatRegScheme vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }
 
 

 
}
