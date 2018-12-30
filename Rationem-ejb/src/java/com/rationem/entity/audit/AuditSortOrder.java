/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.SortOrder;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditSortOrder")
@Table(name="audit44")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditSortOrder extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="sort_order_id", referencedColumnName="sort_order_id")
 private SortOrder sortOrder;

 public SortOrder getSortOrder() {
  return sortOrder;
 }

 public void setSortOrder(SortOrder sortOrder) {
  this.sortOrder = sortOrder;
 }
 
 
 
}
