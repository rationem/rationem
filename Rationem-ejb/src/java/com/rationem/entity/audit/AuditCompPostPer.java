/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.fi.company.CompPostPer;
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
@Table(name="audit54")
@DiscriminatorValue("audit.AuditCompPostPer")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCompPostPer extends AuditBase {
 private static final long serialVersionUID = 1L;
 
 @ManyToOne
 @JoinColumn(name="comp_post_per_id",referencedColumnName="comp_post_per_id")
 private CompPostPer compPostingPeriod;

 public CompPostPer getCompPostingPeriod() {
  return compPostingPeriod;
 }

 public void setCompPostingPeriod(CompPostPer compPostingPeriod) {
  this.compPostingPeriod = compPostingPeriod;
 }
 
 
 
}
