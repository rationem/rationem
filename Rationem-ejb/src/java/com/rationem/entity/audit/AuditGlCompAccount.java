/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;




/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditGlCompAccount")
@Table(name="audit03")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditGlCompAccount extends AuditBase {
 @ManyToOne
 @JoinColumn(name="gl_comp_acnt_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp compAccount;

 public FiGlAccountComp getCompAccount() {
  return compAccount;
 }

 public void setCompAccount(FiGlAccountComp compAccount) {
  this.compAccount = compAccount;
 }
 
 
 

}
