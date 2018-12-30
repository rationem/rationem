/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocLineTemplate;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.DiscriminatorValue;


/**
 *
 * @author user
 */
@Entity
@Table(name="audit57")
@DiscriminatorValue("audit.AuditDocLineTemplate")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocLineTemplate extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="doc_line_templ_id", referencedColumnName="doc_line_templ_id")
 private DocLineTemplate docLine;

 public DocLineTemplate getDocLine() {
  return docLine;
 }

 public void setDocLine(DocLineTemplate docLine) {
  this.docLine = docLine;
 }
 
 
 }
