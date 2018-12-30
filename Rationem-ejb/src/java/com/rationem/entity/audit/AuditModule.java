/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.config.common.Module;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditModule")
@Table(name="audit33")
@PrimaryKeyJoinColumn(name="AUDIT_ID",referencedColumnName = "AUDIT_ID")

public class AuditModule extends AuditBase implements Serializable {
 private static final long serialVersionUID = 1L;
 
 @ManyToOne
 @JoinColumn(name="module_id", referencedColumnName="module_id")
 private Module module;

 public Module getModule() {
  return module;
 }

 public void setModule(Module module) {
  this.module = module;
 }
 
 
 
}
