/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;


import com.rationem.entity.document.DocInvoiceAr;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditDocInvoiceAr")
@Table(name="audit65")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditDocInvoiceAr extends AuditBase {

 @ManyToOne
 @JoinColumn(name="inv_id", referencedColumnName="ar_inv_id")
 DocInvoiceAr inv; 

 public DocInvoiceAr getInv() {
  return inv;
 }

 public void setInv(DocInvoiceAr inv) {
  this.inv = inv;
 }
 
 
 
}
