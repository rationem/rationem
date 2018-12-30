/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatScheme;
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
@DiscriminatorValue("audit.AuditVatScheme")
@Table(name="audit08")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatScheme extends AuditBase {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="vat_scheme_id",referencedColumnName="vat_scheme_id")
 private VatScheme vatScheme;

 public VatScheme getVatScheme() {
  return vatScheme;
 }

 public void setVatScheme(VatScheme vatScheme) {
  this.vatScheme = vatScheme;
 }

 
}
