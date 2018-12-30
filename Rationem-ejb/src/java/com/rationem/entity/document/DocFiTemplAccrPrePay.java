/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.config.common.Uom;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.Column;
import java.util.Date;

import java.util.List;

import static javax.persistence.TemporalType.DATE;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;


/**
 *
 * @author user
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocFiTemplAccrPrePay")
@PrimaryKeyJoinColumn(name="doc_templ_id",referencedColumnName = "doc_tmpl_id")
@NamedQuery(name="allFiTemplAct", 
   query="Select a from DocFiTemplAccrPrePay a where a.company.id = :compId "
      + "and a.complete = false ")
@Table(name="doc08" )

public class DocFiTemplAccrPrePay extends DocFiTemplateBase {
 private static final long serialVersionUID = 1L;
 
 
 public static final int REVERSING = 1;
 public static final int RECURRING = 2;
 public static final String DOC_TYPE = "FI_DOC_FIN_TEMPL_ACCR_PP";
 
 
 
 @Column(name="complete")
 private boolean complete;
 @Temporal(DATE)
 @Column(name="next_post_date")
 private Date nextPostDate;
 @Column(name="total_recurr")
 private int numRecur;
 @Column(name="number_posted")
 private int numPosted;
 @Column(name="reversal_year")
 private int revYer;
 @Column(name="reversal_period")
 private int revPer;
 @Column(name="recurr_start_year")
 private int recurStartYear;
 @Column(name="recurr_start_period")
 private int recurStartPer;
 
 @ManyToOne
 @JoinColumn(name="recur_unit_id", referencedColumnName="uom_id")
 private Uom recurUom;
 @OneToOne
 @JoinColumn(name="setup_doc_id", referencedColumnName="doc_id")
 private DocBase originalJnl;
 @OneToOne
 @JoinColumn(name="reversal_doc_id", referencedColumnName="doc_id")
 private DocBase revJnl;
 @OneToMany(mappedBy = "templateRecurDoc")
 private List<DocBase> recurringDocs;

 
 public boolean isComplete() {
  return complete;
 }

 public void setComplete(boolean complete) {
  this.complete = complete;
 }

 public Date getNextPostDate() {
  return nextPostDate;
 }

 public void setNextPostDate(Date nextPostDate) {
  this.nextPostDate = nextPostDate;
 }

 public int getNumRecur() {
  return numRecur;
 }

 public void setNumRecur(int numRecur) {
  this.numRecur = numRecur;
 }

 public int getNumPosted() {
  return numPosted;
 }

 public void setNumPosted(int numPosted) {
  this.numPosted = numPosted;
 }

 public int getRevYer() {
  return revYer;
 }

 public void setRevYer(int revYer) {
  this.revYer = revYer;
 }

 public int getRevPer() {
  return revPer;
 }

 public void setRevPer(int revPer) {
  this.revPer = revPer;
 }

 public int getRecurStartYear() {
  return recurStartYear;
 }

 public void setRecurStartYear(int recurStartYear) {
  this.recurStartYear = recurStartYear;
 }

 public int getRecurStartPer() {
  return recurStartPer;
 }

 public void setRecurStartPer(int recurStartPer) {
  this.recurStartPer = recurStartPer;
 }

 

 public Uom getRecurUom() {
  return recurUom;
 }

 public void setRecurUom(Uom recurUom) {
  this.recurUom = recurUom;
 }

 public DocBase getOriginalJnl() {
  return originalJnl;
 }

 public void setOriginalJnl(DocBase originalJnl) {
  this.originalJnl = originalJnl;
 }

 public DocBase getRevJnl() {
  return revJnl;
 }

 public void setRevJnl(DocBase revJnl) {
  this.revJnl = revJnl;
 }

 public List<DocBase> getRecurringDocs() {
  return recurringDocs;
 }

 public void setRecurringDocs(List<DocBase> recurringDocs) {
  this.recurringDocs = recurringDocs;
 }
 

 
 
}
