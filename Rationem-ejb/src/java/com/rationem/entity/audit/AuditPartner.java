/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.mdm.PartnerBase;
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
@DiscriminatorValue("audit.AuditPartner")
@Table(name="audit11")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditPartner extends AuditBase {
 @ManyToOne
 @JoinColumn(name="partner_id",referencedColumnName="partner_id")
 private PartnerBase partner;

 public PartnerBase getPartner() {
  return partner;
 }

 public void setPartner(PartnerBase partner) {
  this.partner = partner;
 }
 
 
}
