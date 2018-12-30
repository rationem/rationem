/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.tr.bank.ChequeTemplate;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author user
 */
@Entity
@Table(name="audit70")
@DiscriminatorValue("audit.AuditChequeTemplate")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditChequeTemplate extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="cheque_template_id",referencedColumnName="cheque_template_id")
 private ChequeTemplate chequeTemplate;

 public ChequeTemplate getChequeTemplate() {
  return chequeTemplate;
 }

 public void setChequeTemplate(ChequeTemplate chequeTemplate) {
  this.chequeTemplate = chequeTemplate;
 }
 
 
 
}
