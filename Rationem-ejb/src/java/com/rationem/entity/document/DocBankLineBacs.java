/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

//import com.rationem.entity.tr.bacs.BacsTransCode;
//import com.rationem.entity.tr.bank.BankAccount;
import com.rationem.entity.tr.bacs.BacsTransCode;
import com.rationem.entity.tr.bank.BankAccount;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocBankLineBacs")
@PrimaryKeyJoinColumn(name="bank_trans_id",referencedColumnName = "bank_trans_id")
@Table(name="doc_line06" )

public class DocBankLineBacs extends DocBankLineBase implements Serializable {
 private static final long serialVersionUID = 1L;
 
 @ManyToOne
 @JoinColumn(name="bacs_trans_id",  referencedColumnName="dd_trans_cd_id")
 private BacsTransCode bacsTransCode;

 
 @ManyToOne
 @JoinColumn(name="BNK_ACNT_ID",  referencedColumnName="BANK_ACCOUNT_ID")
 private BankAccount bankAccountPtnr;
 
 @Column(name="ptnr_name")
 private String bacsPtnrName;
  
 public BacsTransCode getBacsTransCode() {
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCode bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
 }

 public String getBacsPtnrName() {
  return bacsPtnrName;
 }

 public void setBacsPtnrName(String bacsPtnrName) {
  this.bacsPtnrName = bacsPtnrName;
 }

 
 public BankAccount getBankAccountPtnr() {
  return bankAccountPtnr;
 }

 public void setBankAccountPtnr(BankAccount bankAccountPtnr) {
  this.bankAccountPtnr = bankAccountPtnr;
 }
 
 
 
}
