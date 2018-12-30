/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.company.AccountType;
//import com.rationem.entity.config.fi.FiGlActType;
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
@DiscriminatorValue("audit.AuditAccountType")
@Table(name="audit04")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditAccountType extends AuditBase {
 @ManyToOne
 @JoinColumn(name="acnt_type_id", referencedColumnName="account_type_id")
  private AccountType accountType;

 public AccountType getAccountType() {
  return accountType;
 }

 public void setAccountType(AccountType accountType) {
  this.accountType = accountType;
 }
 
 

 
}
