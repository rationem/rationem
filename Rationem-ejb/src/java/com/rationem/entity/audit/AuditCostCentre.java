/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.ma.CostCentre;
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
@DiscriminatorValue("audit.AuditCostCentre")
@Table(name="audit15")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditCostCentre extends AuditBase{
 @ManyToOne
 @JoinColumn(name="cost_centre_id",referencedColumnName="cost_cent_id")
 private CostCentre costCentre;

 public CostCentre getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentre costCentre) {
  this.costCentre = costCentre;
 }
 
 
 
}
