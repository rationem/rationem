/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.glAccount;

import java.io.Serializable;
import com.rationem.entity.fi.glAccount.FiGLAccountBase;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account02")
@DiscriminatorValue("fi.glAccount.FiPlAccount")
@PrimaryKeyJoinColumn(name="fi_account_id",referencedColumnName = "fi_gl_account_id")

public class FiPlAccount extends FiGLAccountBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="account_category")
 private String accountCat;
 

 public String getAccountCat() {
  return accountCat;
 }

 public void setAccountCat(String accountCat) {
  this.accountCat = accountCat;
 }

 
}
