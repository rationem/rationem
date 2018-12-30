/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatReturn;
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
@DiscriminatorValue("audit.AuditVatReturn")
@Table(name="audit18")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatReturn extends AuditBase{
 @ManyToOne
 @JoinColumn(name="vat_return_id",referencedColumnName="vat_return_id")
 private VatReturn vatReturn;

 public VatReturn getVatReturn() {
  return vatReturn;
 }

 public void setVatReturn(VatReturn vatReturn) {
  this.vatReturn = vatReturn;
 }

 
 
}
