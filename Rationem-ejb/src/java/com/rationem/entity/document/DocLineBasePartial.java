/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.entity.audit.AuditDocLinePartial;
import com.rationem.entity.config.common.LineTypeRule;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import com.rationem.entity.tr.bank.BnkPaymentRun;
import com.rationem.entity.user.User;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import java.util.ArrayList;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;

/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("document.DocLineBasePartial")
@Table(name="doc_line08" , uniqueConstraints = @UniqueConstraint(columnNames =  {
    "doc_id", "line_num"         }
))
@SequenceGenerator(name = "docLineBasePartial_s1", sequenceName = "docLIne08_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class DocLineBasePartial implements Serializable {
 @OneToMany(mappedBy = "line")
 private List<AuditDocLinePartial> auditRecords;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "docLine_s1")
 @Column(name="doc_line_id")
 private Long id;
 @Column(name="line_num")
 private Long lineNum;
 
 @ManyToOne
 @JoinColumn(name="bnk_pay_run_id", referencedColumnName="bank_pay_run_id")
 private BnkPaymentRun bnkPaymentRun;
 @ManyToOne
 @JoinColumn(name="bank_trans_id", referencedColumnName="bank_trans_id")
 private DocBankLineBase bankLine;
 @Column(name="home_amount")
 private double homeAmount;
 @Column(name="original_amount")
 private double orginalAmount;
 @Column(name="paid_amount")
 private double paidAmount;
 @Column(name="line_text")
 private String lineText;
 @Column(name="sort_order")
 private String sortOrder;
 @Column(name="ref1")
 private String reference1;
 @Column(name="ref2")
 private String reference2;
 @Column(name="doc_notes")
 private String notes;
 @ManyToOne
 @JoinColumn(name="doc_id", referencedColumnName="doc_id")
 private DocBasePartial docHeaderBase;
    
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic comp;

 @ManyToOne
 @JoinColumn(name="line_type_id", referencedColumnName="line_type_id")
 private LineTypeRule lineType;
    
 @ManyToOne
 @JoinColumn(name="post_type_id",referencedColumnName="post_type_id")
 private PostType postType;
 
 @ManyToOne
 @JoinColumn(name="vat_code_id", referencedColumnName="vat_code_comp_id")
 private VatCodeCompany vatCode;
 
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createDate;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changeDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changeBy;
 @Column(name="changes")
 @Version
 private int revision;

 public DocLineBasePartial() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditDocLinePartial> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditDocLinePartial> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public BnkPaymentRun getBnkPaymentRun() {
  return bnkPaymentRun;
 }

 public void setBnkPaymentRun(BnkPaymentRun bnkPaymentRun) {
  this.bnkPaymentRun = bnkPaymentRun;
 }

 public DocBankLineBase getBankLine() {
  return bankLine;
 }

 public void setBankLine(DocBankLineBase bankLine) {
  this.bankLine = bankLine;
 }

 public double getHomeAmount() {
  return homeAmount;
 }

 public void setHomeAmount(double homeAmount) {
  this.homeAmount = homeAmount;
 }

 public String getLineText() {
  return lineText;
 }

 public void setLineText(String lineText) {
  this.lineText = lineText;
 }

 public DocBasePartial getDocHeaderBase() {
  return docHeaderBase;
 }

 public void setDocHeaderBase(DocBasePartial docHeaderBase) {
  this.docHeaderBase = docHeaderBase;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public LineTypeRule getLineType() {
  return lineType;
 }

 public void setLineType(LineTypeRule lineType) {
  this.lineType = lineType;
 }

 public Long getLineNum() {
  return lineNum;
 }

 public void setLineNum(Long lineNum) {
  this.lineNum = lineNum;
 }

 public double getOrginalAmount() {
  return orginalAmount;
 }

 public void setOrginalAmount(double orginalAmount) {
  this.orginalAmount = orginalAmount;
 }

 public double getPaidAmount() {
  return paidAmount;
 }

 public void setPaidAmount(double paidAmount) {
  this.paidAmount = paidAmount;
 }

 public PostType getPostType() {
  return postType;
 }

 public void setPostType(PostType postType) {
  this.postType = postType;
 }

 
 public String getSortOrder() {
  return sortOrder;
 }

 public void setSortOrder(String sortOrder) {
  this.sortOrder = sortOrder;
 }

 public String getReference1() {
  return reference1;
 }

 public void setReference1(String reference1) {
  this.reference1 = reference1;
 }

 public String getReference2() {
  return reference2;
 }

 public void setReference2(String reference2) {
  this.reference2 = reference2;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 public VatCodeCompany getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompany vatCode) {
  this.vatCode = vatCode;
 }

 
 
 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public User getCreateBy() {
  return createBy;
 }

 
 public void setCreateBy(User createBy) {
  this.createBy = createBy;
 }

 public Date getChangeDate() {
  return changeDate;
 }

 public void setChangeDate(Date changeDate) {
  this.changeDate = changeDate;
 }

 public User getChangeBy() {
  return changeBy;
 }

 public void setChangeBy(User changeBy) {
  this.changeBy = changeBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }
 
 

 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof DocLineBasePartial)) {
   return false;
  }
  DocLineBasePartial other = (DocLineBasePartial) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocLineBasePartial[ id=" + id + " ]";
 }
 
}
