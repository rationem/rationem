/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.company.CalendarRuleBase;
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
@DiscriminatorValue("audit.AuditCalRuleBase")
@Table(name="audit39")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCalRuleBase extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="fis_cal_rule_id", referencedColumnName="fis_per_cal_base_id")
 private CalendarRuleBase calRul;

 public CalendarRuleBase getCalRul() {
  return calRul;
 }

 public void setCalRul(CalendarRuleBase calRul) {
  this.calRul = calRul;
 }
 
 
 
 
}
