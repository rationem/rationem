/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.salesTax.vat.VatIndustryFlatRate;
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
@DiscriminatorValue("audit.AuditVatIndustryFlatRate")
@Table(name="audit09")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditVatIndustryFlatRate extends AuditBase{
 
 @ManyToOne
 @JoinColumn(name="vat_industry_rate_id",referencedColumnName="vat_industry_rate_id")
 private VatIndustryFlatRate indRate;

 public VatIndustryFlatRate getIndRate() {
  return indRate;
 }

 public void setIndRate(VatIndustryFlatRate indRate) {
  this.indRate = indRate;
 }

 
 
}
