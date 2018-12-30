/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.company.CalendarRuleFlexYear;
import java.io.Serializable;
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
@DiscriminatorValue("audit.AuditCalRuleFlexYr")
@Table(name="audit40")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCalRuleFlexYr extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="cal_flex_yr_id", referencedColumnName="cal_flex_yr_id")
 private CalendarRuleFlexYear flexYr;

 public CalendarRuleFlexYear getFlexYr() {
  return flexYr;
 }

 public void setFlexYr(CalendarRuleFlexYear flexYr) {
  this.flexYr = flexYr;
 }
 
 
  
}
