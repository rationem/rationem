/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.mdm.Country;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("audit.AuditCountryISO3166")
@Table(name="audit42")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditCountry extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="country_id", referencedColumnName="country_id")
 private Country country;

 public Country getCountry() {
  return country;
 }

 public void setCountry(Country country) {
  this.country = country;
 }
 
 
}
