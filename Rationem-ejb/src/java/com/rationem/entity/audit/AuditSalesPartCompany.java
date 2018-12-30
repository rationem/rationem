/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.sales.SalesPartCompany;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditSalesPartComp")
@Table(name="audit19")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditSalesPartCompany extends AuditBase {
 @ManyToOne
 @JoinColumn(name="sales_part_comp_id",referencedColumnName="sale_part_comp_id")
 private SalesPartCompany compPart;

 public SalesPartCompany getCompPart() {
  return compPart;
 }

 public void setCompPart(SalesPartCompany compPart) {
  this.compPart = compPart;
 }
 
 

}
