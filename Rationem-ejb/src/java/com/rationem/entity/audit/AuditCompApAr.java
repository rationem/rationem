/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.fi.company.CompanyApAr;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;
@Entity
@Table(name="audit72")
@DiscriminatorValue("audit.AuditCompApAr")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

/**
 *
 * @author user
 */
public class AuditCompApAr extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="comp_ap_ar_id",referencedColumnName="id")
 private CompanyApAr apAr;

 public CompanyApAr getApAr() {
  return apAr;
 }

 public void setApAr(CompanyApAr apAr) {
  this.apAr = apAr;
 }
 
 
 
}
