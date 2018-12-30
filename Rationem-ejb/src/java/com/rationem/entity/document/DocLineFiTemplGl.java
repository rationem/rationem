/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.ma.Programme;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;



/**
 *
 * @author user
 */
@Entity
@Table(name="doc_line11")
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocLineFiTemplGl")
@PrimaryKeyJoinColumn(name="doc_line_templ_id",
        referencedColumnName = "doc_line_templ_id")
@NamedQueries({
 })
public class DocLineFiTemplGl extends DocLineTemplate {
 
 @ManyToOne
 @JoinColumn(name="gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp GlAccount;
 

 public FiGlAccountComp getGlAccount() {
  return GlAccount;
 }

 public void setGlAccount(FiGlAccountComp GlAccount) {
  this.GlAccount = GlAccount;
 }

 
 
}
