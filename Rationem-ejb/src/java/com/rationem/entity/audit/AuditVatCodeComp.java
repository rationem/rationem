/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
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
@DiscriminatorValue("audit.AuditVatCodeComp")
@Table(name="audit06")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatCodeComp extends AuditBase{
 @ManyToOne
 @JoinColumn(name="vat_code_comp_id", referencedColumnName="vat_code_comp_id")
 private VatCodeCompany vatCodeComp;

 public VatCodeCompany getVatCodeComp() {
  return vatCodeComp;
 }

 public void setVatCodeComp(VatCodeCompany vatCodeComp) {
  this.vatCodeComp = vatCodeComp;
 }
 
 
 

 
}
