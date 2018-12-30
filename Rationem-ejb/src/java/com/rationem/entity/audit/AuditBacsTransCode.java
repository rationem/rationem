/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.tr.bacs.BacsTransCode;
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

@Table(name="audit27")
@DiscriminatorValue("audit.AuditBacsTransCode")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditBacsTransCode extends AuditBase {
 @ManyToOne
 @JoinColumn(name="bacs_trans_cd_id", referencedColumnName="dd_trans_cd_id")
 private BacsTransCode transCode;

 public BacsTransCode getTransCode() {
  return transCode;
 }

 public void setTransCode(BacsTransCode transCode) {
  this.transCode = transCode;
 }
 
 
 
}
