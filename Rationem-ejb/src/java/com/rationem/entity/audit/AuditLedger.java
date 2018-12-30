/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.company.Ledger;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditLedger")
@Table(name="audit34")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditLedger extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id")
 private Ledger ledger;

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }
 
 
}
