/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.fi.glAccount.FiGLAccountBase;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.NamedQuery;

import javax.persistence.DiscriminatorValue;


/**
 *
 * @author Chris
 */
@Entity
@NamedQuery(name = "coaGlAccountAllChanges",
    query = "SELECT ch FROM AuditGlAccount ch")
@DiscriminatorValue("audit.auditGlAccount")
@Table(name="audit02")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditGlAccount extends AuditBase  {
 @ManyToOne
 @JoinColumn(name="gl_account_id", referencedColumnName="fi_gl_account_id")
    private FiGLAccountBase glAccount;

 public FiGLAccountBase getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGLAccountBase glAccount) {
  this.glAccount = glAccount;
 }
 
 
}
