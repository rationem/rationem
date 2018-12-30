/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.sales.SalesPart;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditSalesPart")
@Table(name="audit14")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditSalesPart extends AuditBase {
 @ManyToOne
 @JoinColumn(name="sales_part_id",referencedColumnName="sales_part_id")
 private SalesPart part;

 public SalesPart getPart() {
  return part;
 }

 public void setPart(SalesPart part) {
  this.part = part;
 }
 
 

 
}
