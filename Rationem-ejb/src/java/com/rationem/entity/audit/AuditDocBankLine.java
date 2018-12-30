/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocBankLineBase;
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
@Table(name="audit68")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditDocBankLine extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="bank_trans_id", referencedColumnName="bank_trans_id")
 private DocBankLineBase bankLine;

 public DocBankLineBase getBankLine() {
  return bankLine;
 }

 public void setBankLine(DocBankLineBase bankLine) {
  this.bankLine = bankLine;
 }
 
 
 
}
