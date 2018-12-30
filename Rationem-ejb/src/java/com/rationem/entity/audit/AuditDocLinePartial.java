/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocLineBasePartial;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditDocLinePartial")
@Table(name="audit50")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocLinePartial extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="doc_line_pt_id",referencedColumnName="doc_line_id")
 private DocLineBasePartial line;

 public DocLineBasePartial getLine() {
  return line;
 }

 public void setLine(DocLineBasePartial line) {
  this.line = line;
 }
 
 
 
}
