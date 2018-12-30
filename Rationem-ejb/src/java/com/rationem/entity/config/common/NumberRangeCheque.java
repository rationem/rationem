/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;

import com.rationem.entity.tr.bank.BankAccountCompany;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.JoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.JOINED;
import javax.persistence.NamedQueries;



/**
 *
 * @author user
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("common.NumberRangeCheque")
@PrimaryKeyJoinColumn(name="num_cntrl_id",referencedColumnName = "num_cntrl_id")
@Table(name="bac_config24")
@NamedQueries({
 @NamedQuery(name="chqBksAll", query="Select cb from NumberRangeCheque cb"),
 @NamedQuery(name="chqBk4BnkAcnt", 
   query="Select cb from NumberRangeCheque cb where cb.bankAccountComp.id = :bnkActId")
})
public class NumberRangeCheque extends NumberRange {
 
 @ManyToOne
 @JoinColumn(name="BANK_ACCOUNT_ID", referencedColumnName="BANK_ACCOUNT_ID")
 private BankAccountCompany bankAccountComp;

 public BankAccountCompany getBankAccountComp() {
  return bankAccountComp;
 }

 public void setBankAccountComp(BankAccountCompany bankAccountCompbankAccountComp) {
  this.bankAccountComp = bankAccountCompbankAccountComp;
 }
 
 
}
