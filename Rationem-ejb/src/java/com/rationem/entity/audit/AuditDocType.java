/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocType;
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
@DiscriminatorValue("audit.AuditDocType")
@Table(name="audit48")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocType extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="doc_type_id",referencedColumnName="doc_type_id")
 private DocType docType;

 public DocType getDocType() {
  return docType;
 }

 public void setDocType(DocType docType) {
  this.docType = docType;
 }
 
 
}
