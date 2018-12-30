/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.DocReversalReason;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditDocRevReason")
@Table(name="audit69")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocRevReason extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="doc_rev_id",referencedColumnName="doc_rev_id")
 DocReversalReason docRevReason;

 public DocReversalReason getDocRevReason() {
  return docRevReason;
 }

 public void setDocRevReason(DocReversalReason docRevReason) {
  this.docRevReason = docRevReason;
 }
 
 
 
}
