/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocFiPartial;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorValue;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import javax.persistence.Column;
import java.util.Date;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;


/**
 *
 * @author Chris
 */
@Entity
@NamedQuery(name="glPartDocForComp", query="Select d from DocFiPartial d where d.company.id = :compId")
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocFIPartial")
@PrimaryKeyJoinColumn(name="doc_id",referencedColumnName = "doc_id")
@Table(name="doc06" )
public class DocFiPartial extends DocBasePartial {
 @OneToMany(mappedBy = "doc", cascade=REMOVE)
 private List<AuditDocFiPartial> auditRecords;
 private static final long serialVersionUID = 1L;
 
 @Column(name="tax_date")
 @Temporal(DATE)
 private Date taxDate;
 @Column(name="extern_ref")
 private String partnerRef;
 @Column(name="fiscal_period")
 private int fisPeriod;
 @Column(name="fiscal_year")
 private int fisYear;
 

 public List<AuditDocFiPartial> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditDocFiPartial> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public Date getTaxDate() {
  return taxDate;
 }

 public void setTaxDate(Date taxDate) {
  this.taxDate = taxDate;
 }

 public String getPartnerRef() {
  return partnerRef;
 }

 public void setPartnerRef(String partnerRef) {
  this.partnerRef = partnerRef;
 }

 public int getFisPeriod() {
  return fisPeriod;
 }

 public void setFisPeriod(int fisPeriod) {
  this.fisPeriod = fisPeriod;
 }

 public int getFisYear() {
  return fisYear;
 }

 public void setFisYear(int fisYear) {
  this.fisYear = fisYear;
 }
 
 
 
}
