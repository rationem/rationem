/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.document;

import com.rationem.entity.audit.AuditDocLineTemplate;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;

import com.rationem.entity.config.common.LineTypeRule;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.ma.ProgramAccount;
import com.rationem.entity.ma.Programme;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import com.rationem.entity.user.User;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.SequenceGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.FetchType.LAZY;




/**
 *
 * @author user
 */
@Entity
@Table(name="doc_line10" , uniqueConstraints = @UniqueConstraint(columnNames =  {
    "doc_tmpl_id", "line_num"         }
))
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("document.DocLineTemplate")

@SequenceGenerator(name = "docLineTempl_s1", sequenceName = "docLIne10_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")

public class DocLineTemplate implements Serializable {
 @OneToMany(mappedBy = "docLine")
 private List<AuditDocLineTemplate> auditRecs;
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "docLineTempl_s1")
 @Column(name="doc_line_templ_id")
 private Long id;
 @Column(name="line_num")
 private Long lineNum;
 @ManyToOne(fetch=LAZY)
 @JoinColumn(name="doc_tmpl_id", referencedColumnName="doc_tmpl_id")
 private DocFiTemplateBase docHeader;
 @ManyToOne(fetch=LAZY)
 @JoinColumn(name="cpmp_id", referencedColumnName="home_comp_id")
 private CompanyBasic comp;
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
 @Column(name="curr_id")
 private int currID;
 @Column(name="doc_amount")
 private double docAmount;
 @Column(name="ln_notes",  length=4000)
 private String notes;
 @Column(name="line_text", length=255)
 private String lineText;
 @Column(name="reference1", length=50)
 private String ref1;
 @Column(name="reference2", length=50)
 private String ref2;
 @Column(name="sort_order", length=50)
 private String sortOrder;
 
 @ManyToOne
 @JoinColumn(name="line_type_id", referencedColumnName="line_type_id")
 private LineTypeRule lineType;
 @ManyToOne(fetch=LAZY)
 @JoinColumn(name="rest_fnd_id", referencedColumnName="restricted_fund_id")
 private Fund restrictedFund;
 
 @ManyToOne(fetch=LAZY)
 @JoinColumn(name="cost_centre_id", referencedColumnName="cost_cent_id")
 private CostCentre costCentre;
 
 
 @ManyToOne(fetch=LAZY)
 @JoinColumn(name="COST_ACNT_ACT_ID", referencedColumnName="COST_ACCNT_ID")
 private CostAccountDirect costAccount;
 
 @ManyToOne
 @JoinColumn(name="post_type_id",referencedColumnName="post_type_id")
 private PostType postType; 
 
 @ManyToOne(fetch = LAZY)
 @JoinColumn(name="PROGRAMME_ID", referencedColumnName="PROGRAMME_ID")
 private Programme programme;
  
 
 @ManyToOne
 @JoinColumn(name="PROG_ACCNT_ID", referencedColumnName="PROG_ACCNT_ID")
 private ProgramAccount programCostAccount;
 
 
 
 
 @ManyToOne
 @JoinColumn(name="comp_vat_code_id" ,referencedColumnName="vat_code_comp_id" )
 private VatCodeCompany vatCodeComp;
 
 
 

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditDocLineTemplate> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditDocLineTemplate> auditRecs) {
  this.auditRecs = auditRecs;
 }

 public Long getLineNum() {
  return lineNum;
 }

 public void setLineNum(Long lineNum) {
  this.lineNum = lineNum;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }

 public String getLineText() {
  return lineText;
 }

 public void setLineText(String lineText) {
  this.lineText = lineText;
 }

 public LineTypeRule getLineType() {
  return lineType;
 }

 public void setLineType(LineTypeRule lineType) {
  this.lineType = lineType;
 }

 
 public DocFiTemplateBase getDocHeader() {
  return docHeader;
 }

 public void setDocHeader(DocFiTemplateBase docHeader) {
  this.docHeader = docHeader;
 }
 public void setDocHeader(DocFiTemplAccrPrePay docHeader) {
  this.docHeader = docHeader;
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

 public int getCurrID() {
  return currID;
 }

 public void setCurrID(int currID) {
  this.currID = currID;
 }

 public double getDocAmount() {
  return docAmount;
 }

 public void setDocAmount(double docAmount) {
  this.docAmount = docAmount;
 }

 public String getRef1() {
  return ref1;
 }

 public void setRef1(String ref1) {
  this.ref1 = ref1;
 }

 public String getRef2() {
  return ref2;
 }

 public void setRef2(String ref2) {
  this.ref2 = ref2;
 }

 

 public Fund getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(Fund restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public String getSortOrder() {
  return sortOrder;
 }

 public void setSortOrder(String sortOrder) {
  this.sortOrder = sortOrder;
 }

 
 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public CostCentre getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentre costCentre) {
  this.costCentre = costCentre;
 }

 public CostAccountDirect getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirect costAccount) {
  this.costAccount = costAccount;
 }

 public PostType getPostType() {
  return postType;
 }

 public void setPostType(PostType postType) {
  this.postType = postType;
 }

 public Programme getProgramme() {
  return programme;
 }

 public void setProgramme(Programme programme) {
  this.programme = programme;
 }

 public ProgramAccount getProgramCostAccount() {
  return programCostAccount;
 }

 public void setProgramCostAccount(ProgramAccount programCostAccount) {
  this.programCostAccount = programCostAccount;
 }

 public VatCodeCompany getVatCodeComp() {
  return vatCodeComp;
 }

 public void setVatCodeComp(VatCodeCompany vatCodeComp) {
  this.vatCodeComp = vatCodeComp;
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
  if (!(object instanceof DocLineTemplate)) {
   return false;
  }
  DocLineTemplate other = (DocLineTemplate) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.document.DocLineTemplate[ id=" + id + " ]";
 }
 
}
