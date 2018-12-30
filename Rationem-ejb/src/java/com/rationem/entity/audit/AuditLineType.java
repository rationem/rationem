/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.LineTypeRule;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditLineType")
@Table(name="audit43")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditLineType extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="line_type_id", referencedColumnName="line_type_id")
 private LineTypeRule lineType;

 public LineTypeRule getLineType() {
  return lineType;
 }

 public void setLineType(LineTypeRule lineType) {
  this.lineType = lineType;
 }
 
 
 
}
