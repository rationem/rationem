/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.document.DocFi;
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
@NamedQuery(name="auditForDocId", 
        query="Select aud from AuditDocFi aud where aud.doc.id = :docId order by aud.fieldName, aud.auditDate")

@DiscriminatorValue("audit.AuditDocFi")
@Table(name="audit28")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditDocFi extends AuditBase {
 @ManyToOne
 @JoinColumn(name="doc_fi_id",referencedColumnName="doc_id")
 private DocFi doc;

 public DocFi getDoc() {
  return doc;
 }

 public void setDoc(DocFi doc) {
  this.doc = doc;
 }
 
 
}
