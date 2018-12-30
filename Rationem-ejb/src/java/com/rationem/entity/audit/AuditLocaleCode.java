/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.common.LocaleCode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("audit.AuditLocaleCode")
@Table(name="audit53")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditLocaleCode  extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="locale_code_id", referencedColumnName="locale_id")
 private LocaleCode loc;

 public LocaleCode getLoc() {
  return loc;
 }

 public void setLoc(LocaleCode loc) {
  this.loc = loc;
 }
 
 
 
 
}
