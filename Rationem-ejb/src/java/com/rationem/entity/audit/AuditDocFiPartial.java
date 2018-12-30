/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocFiPartial;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditDocFiPartial")
@Table(name="audit49")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocFiPartial extends AuditBase {

 @ManyToOne
 @JoinColumn(name="doc_fi_Pt_id",referencedColumnName="doc_id")
 private DocFiPartial doc;

 public DocFiPartial getDoc() {
  return doc;
 }

 public void setDoc(DocFiPartial doc) {
  this.doc = doc;
 }
 
 
}
