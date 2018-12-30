/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.arap.ArAccount;
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
@DiscriminatorValue("audit.AuditArAccount")
@Table(name="audit17")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditArAccount extends AuditBase  {
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="ar_account_id",referencedColumnName="ar_account_id")
 private ArAccount arAccount;

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }
 
 

}
