/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.cm.Contact;
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
@Table(name="audit62")
@DiscriminatorValue("audit.AuditContact")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditContact extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="contact_id",referencedColumnName="contact_id")
 private Contact contact;

 public Contact getContact() {
  return contact;
 }

 public void setContact(Contact contact) {
  this.contact = contact;
 }
 
 
 
}
