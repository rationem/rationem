/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.tr.bank.BnkPaymentRun;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;
/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditBnkPayRun")
@Table(name="audit30")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditBnkPayRun extends AuditBase {
 @ManyToOne
 @JoinColumn(name="bnk_pay_run_id",referencedColumnName="bank_pay_run_id")
 private BnkPaymentRun bnkPayRun;

 public BnkPaymentRun getBnkPayRun() {
  return bnkPayRun;
 }

 public void setBnkPayRun(BnkPaymentRun bnkPayRun) {
  this.bnkPayRun = bnkPayRun;
 }
 
 
 
}
