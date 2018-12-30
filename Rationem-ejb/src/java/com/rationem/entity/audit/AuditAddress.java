/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.mdm.Address;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditAddress")
@Table(name="audit10")
@PrimaryKeyJoinColumn(name="AUDIT_ID",referencedColumnName = "AUDIT_ID")
public class AuditAddress extends AuditBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="address_id",referencedColumnName="address_id")
 private Address address;

 public Address getAddress() {
  return address;
 }

 public void setAddress(Address address) {
  this.address = address;
 }
 
 

}
