/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditDelete")
@Table(name="Audit76")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditDelete extends AuditBase {
 
 @Column(name="obj_name", length=50)
 private String name;
 @Column(name="obj_name_path", length=255)
 private String namePath;

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getNamePath() {
  return namePath;
 }

 public void setNamePath(String namePath) {
  this.namePath = namePath;
 }
 
 
 
}
