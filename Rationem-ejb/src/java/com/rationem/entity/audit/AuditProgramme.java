/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.ma.Programme;
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
@DiscriminatorValue("audit.AuditProgramme")
@Table(name="audit16")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditProgramme extends AuditBase {
 @ManyToOne
 @JoinColumn(name="programme_id",referencedColumnName="programme_id")
 private Programme prog;

 public Programme getProg() {
  return prog;
 }

 public void setProg(Programme prog) {
  this.prog = prog;
 }
 
 
 
}
