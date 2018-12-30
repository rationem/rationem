/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.mdm.Currency;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;


/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditCurrency")
@Table(name="audit52")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCurrency extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="currency_id", referencedColumnName="currency_id")
 private Currency curr;

 public Currency getCurr() {
  return curr;
 }

 public void setCurr(Currency curr) {
  this.curr = curr;
 }
 
 
 
}
