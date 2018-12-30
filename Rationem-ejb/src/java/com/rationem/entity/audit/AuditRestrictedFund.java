/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.company.Fund;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditRestrictedFund")
@Table(name="audit45")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditRestrictedFund extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="restr_fnd_id", referencedColumnName="restricted_fund_id")
 private Fund restrFnd;

 public Fund getRestrFnd() {
  return restrFnd;
 }

 public void setRestrFnd(Fund restrFnd) {
  this.restrFnd = restrFnd;
 }
 
 
}
