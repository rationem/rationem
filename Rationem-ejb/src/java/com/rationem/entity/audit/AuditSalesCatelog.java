/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.sales.SalesCat;
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
@DiscriminatorValue("audit.AuditSalesCatelog")
@Table(name="audit64")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditSalesCatelog extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="cat_id",referencedColumnName="sl_cat_id")
 private SalesCat cat;

 public SalesCat getCat() {
  return cat;
 }

 public void setCat(SalesCat cat) {
  this.cat = cat;
 }
 
 
 
}
