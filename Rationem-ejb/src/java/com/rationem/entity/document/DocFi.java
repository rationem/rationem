/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocFi;
import com.rationem.entity.salesTax.vat.VatReturnLine;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorValue;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.NamedNativeQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )

@DiscriminatorValue("document.DocFi")
@PrimaryKeyJoinColumn(name="doc_id",referencedColumnName = "doc_id")
@Table(name="doc02" )
@NamedQueries({
 @NamedQuery(name="docsForPeriodRange", 
        query="select d from DocFi d where d.fisYear = :fyr and (d.fisPeriod >= :frPer "
        + "and d.fisPeriod <= :toPer ) and d.company.id = :compId"),
 @NamedQuery(name="fiDocsForCompAll",
    query="Select d from DocFi d where d.company.id = :compId"),
 @NamedQuery(name="docsFiApComp",
   query="Select d from DocFi d join d.docLines l where l.lineType.lineCode = 'AP' and d.company.id = :compId"),
 @NamedQuery(name="docsFiApCompDocNum",
   query="Select d from DocFi d join d.docLines l where "
     + "l.lineType.lineCode = 'AP' and d.company.id = :compId and d.docNumber >= :docNum"),
 @NamedQuery(name="fiDocsComp1",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd" ),
 @NamedQuery(name="fiDocsComp2",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.createOn between :entrySt and :entryEnd" ),
 @NamedQuery(name="fiDocsComp3",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.postingDate between :postSt and :postEnd" ),
 @NamedQuery(name="fiDocsComp4",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.docType.id = :docTyId" ),  
 @NamedQuery(name="fiDocsComp5",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.createdBy.id = :createdById" ),
 @NamedQuery(name="fiDocsComp6",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.docHdrText like :hdrTxt" ),
 @NamedQuery(name="fiDocsComp7",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.partnerRef like :ref" ),
 @NamedQuery(name="fiDocsComp8",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd"
       + " and d.postingDate between :pstSt and :pstEnd" ),
 @NamedQuery(name="fiDocsComp9",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd"
       + " and d.createOn between :entrySt and :entryEnd" ),
 @NamedQuery(name="fiDocsComp10",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd"
       + " and d.createdBy.id = :createdById" ),
 @NamedQuery(name="fiDocsComp11",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd"
       + " and d.docType.id = :docTyId" ),
 @NamedQuery(name="fiDocsComp12",
    query="Select d from DocFi d where d.company.id = :compId "
       + "and d.documentDate between :docSt and :docEnd"
       + " and d.docHdrText like :hdrTxt" )
})
public class DocFi extends DocBase implements Serializable {

 @OneToMany(mappedBy = "docFi")
 private List<DocLineGl> glLines;

 

 @OneToMany(mappedBy = "docFi")
 private List<DocLineAp> apLines;

 @OneToMany(mappedBy = "docFi")
 private List<DocLineAr> arLines;

 @ManyToOne
 @JoinColumn(name="doc_type_id", referencedColumnName="doc_type_id")
 private DocType docType;
 
 @Column(name="doc_date")
 @Temporal(DATE)
 private Date documentDate;
 @Column(name="post_date")
 @Temporal(DATE)
 private Date postingDate;
 @Column(name="tax_date")
 @Temporal(DATE)
 private Date taxDate;
 @Column(name="extern_ref")
 private String partnerRef;
 @Column(name="fiscal_period")
 private int fisPeriod;
 @Column(name="fiscal_year")
 private int fisYear;

 @OneToOne(mappedBy = "fiDocument")
 private DocInvoiceAr docInvoiceAr;
 @OneToMany(mappedBy = "doc")
 private List<AuditDocFi> docFiAuditRecords;
 @OneToMany(mappedBy = "fiDoc")
 private List<VatReturnLine> vatReturnLines;

 public List<DocLineAp> getApLines() {
  return apLines;
 }

 public void setApLines(List<DocLineAp> apLines) {
  this.apLines = apLines;
 }

 public List<DocLineAr> getArLines() {
  return arLines;
 }

 public void setArLines(List<DocLineAr> arLines) {
  this.arLines = arLines;
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

 public Date getPostingDate() {
  return postingDate;
 }

 public void setPostingDate(Date postingDate) {
  this.postingDate = postingDate;
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

 public Date getDocumentDate() {
  return documentDate;
 }

 public void setDocumentDate(Date documentDate) {
  this.documentDate = documentDate;
 }

 
 public DocInvoiceAr getDocInvoiceAr() {
  return docInvoiceAr;
 }

 public void setDocInvoiceAr(DocInvoiceAr docInvoiceAr) {
  this.docInvoiceAr = docInvoiceAr;
 }

 

 
 public List<AuditDocFi> getDocFiAuditRecords() {
  return docFiAuditRecords;
 }

 public void setDocFiAuditRecords(List<AuditDocFi> docFiAuditRecords) {
  this.docFiAuditRecords = docFiAuditRecords;
 }

 public List<VatReturnLine> getVatReturnLines() {
  return vatReturnLines;
 }

 public void setVatReturnLines(List<VatReturnLine> vatReturnLines) {
  this.vatReturnLines = vatReturnLines;
 }

 
}
