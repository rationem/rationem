/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.common.NumberRangeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditNumberRangeType")
@Table(name="audit75")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditNumberRangeType extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="num_rng_id", referencedColumnName="id")
 private NumberRangeType nrTy;

 public NumberRangeType getNrTy() {
  return nrTy;
 }

 public void setNrTy(NumberRangeType nrTy) {
  this.nrTy = nrTy;
 }
 
 
}
