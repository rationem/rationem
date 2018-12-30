/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.ProcessCode;
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
@Table(name="audit38")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditProcessCode extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="process_code_id",referencedColumnName="process_code_id")
 private ProcessCode processCode;

 public ProcessCode getProcessCode() {
  return processCode;
 }

 public void setProcessCode(ProcessCode processCode) {
  this.processCode = processCode;
 }
  
 
}
