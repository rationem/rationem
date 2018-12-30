/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.ContactRole;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name="audit61")
@DiscriminatorValue("audit.AuditContactRole")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditContactRole extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="contact_role_id",referencedColumnName="contact_role_id")
 private ContactRole contactRole;

 public ContactRole getContactRole() {
  return contactRole;
 }

 public void setContactRole(ContactRole contactRole) {
  this.contactRole = contactRole;
 }
 
 
 
}
