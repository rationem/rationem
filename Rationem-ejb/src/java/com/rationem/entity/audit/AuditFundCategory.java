/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.FundCategory;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditRestrictedFndCategory")
@Table(name="audit46")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditFundCategory extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="fnd_cat_id", referencedColumnName="fnd_cat_id")
 private FundCategory fndCategory;

 public FundCategory getFndCategory() {
  return fndCategory;
 }

 public void setFndCategory(FundCategory fndCategory) {
  this.fndCategory = fndCategory;
 }
 
 
 
}
