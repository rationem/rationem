/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.company.CalendarFlexPer;
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
@DiscriminatorValue("audit.AuditCalRuleFlexPer")
@Table(name="audit41")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCalRuleFlexPer extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="cal_flex_per_id", referencedColumnName="cal_flex_per_id")
 private CalendarFlexPer calPer;

 public CalendarFlexPer getCalPer() {
  return calPer;
 }

 public void setCalPer(CalendarFlexPer calPer) {
  this.calPer = calPer;
 }
 
 
 
}
