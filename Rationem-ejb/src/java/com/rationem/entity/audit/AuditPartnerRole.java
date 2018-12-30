/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.mdm.PartnerRole;
import java.io.Serializable;
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
@DiscriminatorValue("audit.AuditPartnerRole")
@Table(name="audit60")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditPartnerRole extends AuditBase implements Serializable {
 
 @ManyToOne
 @JoinColumn(name="ptnr_role_id",referencedColumnName="ptnr_role_id")
 private PartnerRole ptnrRole;

 public PartnerRole getPtnrRole() {
  return ptnrRole;
 }

 public void setPtnrRole(PartnerRole ptnrRole) {
  this.ptnrRole = ptnrRole;
 }
 
 
 
}
