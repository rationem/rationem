/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.company.CompanyBasic;
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
@Table(name="audit35")
@DiscriminatorValue("audit.AuditCompany")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCompany extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="comp_id",referencedColumnName="home_comp_id")
 private CompanyBasic comp;

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }
 
 
}
