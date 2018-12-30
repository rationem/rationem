/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;


import com.rationem.entity.sales.SalesInfo;
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
@DiscriminatorValue("audit.AuditSalesInfo")
@Table(name="audit20")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditSalesInfo extends AuditBase {
 @ManyToOne
 @JoinColumn(name="sales_info_id", referencedColumnName="sl_info_id")
 private SalesInfo salesInfo;

 public SalesInfo getSalesInfo() {
  return salesInfo;
 }

 public void setSalesInfo(SalesInfo salesInfo) {
  this.salesInfo = salesInfo;
 }
 
 

 
}
