/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatCode;
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
@DiscriminatorValue("audit.AuditVatCode")
@Table(name="audit06")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatCode extends AuditBase {
 @ManyToOne
  @JoinColumn(name="vat_code_id", referencedColumnName="vat_code_id")
  private VatCode vatCode;

 public VatCode getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCode vatCode) {
  this.vatCode = vatCode;
 }
 
 
 
}
