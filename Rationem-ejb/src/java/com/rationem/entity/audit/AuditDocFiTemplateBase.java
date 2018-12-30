/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.document.DocFiTemplAccrPrePay;
import com.rationem.entity.document.DocFiTemplateBase;
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
@DiscriminatorValue("audit.AuditDocFiTemplate")
@Table(name="audit55")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDocFiTemplateBase extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="doc_fi_template_id", referencedColumnName="doc_tmpl_id")
 private DocFiTemplateBase tmpl;

 public DocFiTemplateBase getTmpl() {
  return tmpl;
 }

 public void setTmpl(DocFiTemplateBase tmpl) {
  this.tmpl = tmpl;
 }
 
 
 
}
