/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.document.DocLineBase;
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
@NamedQuery(name="auditForDocLine", 
        query="Select aud from AuditDocLine aud where aud.docLine.id = :docLineId "
        + "order by aud.fieldName, aud.auditDate")
@DiscriminatorValue("audit.AuditDocLine")
@Table(name="audit29")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditDocLine extends AuditBase {
 @ManyToOne
 @JoinColumn(name="doc_line_id",referencedColumnName="doc_line_id")
 private DocLineBase docLine;

 public DocLineBase getDocLine() {
  return docLine;
 }

 public void setDocLine(DocLineBase docLine) {
  this.docLine = docLine;
 }
 
 
}
