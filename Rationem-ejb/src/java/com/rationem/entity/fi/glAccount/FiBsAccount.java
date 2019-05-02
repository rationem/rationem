/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.glAccount;

//import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.config.company.Ledger;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.NamedQuery;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="fi_account03")
@DiscriminatorValue("fi.glAccount.FiBsAccount")
@PrimaryKeyJoinColumn(name="bs_account_id",referencedColumnName = "fi_gl_account_id")
public class FiBsAccount extends FiGLAccountBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="bal_fwd")
 private boolean balFwd;
 
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id")
 private Ledger reconForLed;
   
    
 public FiBsAccount() {
 }

 public boolean isBalFwd() {
  return balFwd;
 }

 public void setBalFwd(boolean balFwd) {
  this.balFwd = balFwd;
 }

 public Ledger getReconForLed() {
  return reconForLed;
 }

 public void setReconForLed(Ledger reconForLed) {
  this.reconForLed = reconForLed;
 }

 
 
}
