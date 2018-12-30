/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.ValueDate;
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
@DiscriminatorValue("audit.AuditValueDate")
@Table(name="audit24")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditValueDate extends AuditBase {
 @ManyToOne
 @JoinColumn(name="value_date_id",referencedColumnName = "value_date_rule_id")
 private ValueDate valueDate;

 public ValueDate getValueDate() {
  return valueDate;
 }

 public void setValueDate(ValueDate valueDate) {
  this.valueDate = valueDate;
 }
 
 
}
