/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.company.FisPeriodRule;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditFisPeriodRule")
@Table(name="audit36")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditFisPeriodRule extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="period_rule_id",referencedColumnName="period_rule_id")
 private FisPeriodRule perRule;

 public FisPeriodRule getPerRule() {
  return perRule;
 }

 public void setPerRule(FisPeriodRule perRule) {
  this.perRule = perRule;
 }
 
 
}
