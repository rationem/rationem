/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.NumberRange;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import javax.persistence.DiscriminatorValue;



/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditNumberControl")
@Table(name="audit05")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditNumberControl extends AuditBase  {
 @ManyToOne
 @JoinColumn(name="num_rng_id", referencedColumnName="num_cntrl_id")
 private NumberRange numControl;

 public NumberRange getNumControl() {
  return numControl;
 }

 public void setNumControl(NumberRange numControl) {
  this.numControl = numControl;
 }
 
}
