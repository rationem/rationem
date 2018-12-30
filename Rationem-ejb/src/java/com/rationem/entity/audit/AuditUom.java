/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.Uom;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditUom")
@Table(name="audit37")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditUom extends AuditBase {

 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="uom_id",referencedColumnName="uom_id")
 private Uom uom;

 public Uom getUom() {
  return uom;
 }

 public void setUom(Uom uom) {
  this.uom = uom;
 }
 
 
 
 
}
