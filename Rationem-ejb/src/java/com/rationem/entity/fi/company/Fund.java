/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

//import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.audit.AuditRestrictedFund;
import com.rationem.entity.config.common.FundCategory;
import com.rationem.entity.document.DocBankLineBase;

import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.sales.SalesPartCompany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import java.util.Date;
import javax.persistence.Column;
import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
/**
 *
 * @author Chris
 */
@Entity
@Table(name="comp07" ,uniqueConstraints=
 @UniqueConstraint(columnNames={"fnd_code", "comp_id"}))
@NamedQueries({
 @NamedQuery(name = "allRestrFnds",query = "SELECT fnds FROM Fund fnds"),
 @NamedQuery(name = "fundsByComp",query = "SELECT f FROM Fund f where f.fundForComp.id = :compId"),
 @NamedQuery(name="fndByCodeComp", 
        query="Select f from Fund f where f.fndCode = :code  and f.fundForComp.id = :compId")
})
@SequenceGenerator(name = "restrictedFund_s1", sequenceName = "comp07_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class Fund implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "restrictedFund_s1")
 @Column(name="restricted_fund_id")
 private Long id;
 @Column(name="fnd_code")
 private String fndCode;
  
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic fundForComp;
 @Column(name="name")
 private String name;
 @Column(name="fnd_purpose", length=4096)
 @Size(max=4096)
 private String purpose;
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="valid_to")
 private Date validTo;
 @Temporal(DATE)
 @Column(name="return_due_date")
 private Date returnDue;
 @Column(name="returned_required")
 private boolean returnRequired;
 
 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedOn;
 @Version
 @Column(name="changes")
 private long revision;
 
 @OneToMany(mappedBy = "restrictedFund")
 private List<FiPeriodBalance> PeriodBalances;
 @OneToMany(mappedBy = "restrictedFund")
 private List<DocLineGl> glLines;
 @OneToMany(mappedBy = "fund")
 private List<SalesPartCompany> salesPartCompany;

 @OneToMany(mappedBy = "restrFnd")
 private List<AuditRestrictedFund> auditRecords;
 @ManyToOne
 @JoinColumn(name="cat_id", referencedColumnName="fnd_cat_id")
 private FundCategory fundCategory;
 @OneToMany(mappedBy = "restrictedFund")
 private List<DocBankLineBase> bankLines;
 


  public Fund() {
  }


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditRestrictedFund> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditRestrictedFund> auditRecords) {
  this.auditRecords = auditRecords;
 }

 

 public String getFndCode() {
  return fndCode;
 }

 public void setFndCode(String fndCode) {
  this.fndCode = fndCode;
 }

 public CompanyBasic getFundForComp() {
  return fundForComp;
 }

 public void setFundForComp(CompanyBasic fundForComp) {
  this.fundForComp = fundForComp;
 }

 public FundCategory getFundCategory() {
  return fundCategory;
 }

 public void setFundCategory(FundCategory fundCategory) {
  this.fundCategory = fundCategory;
 }

 public List<DocLineGl> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineGl> glLines) {
  this.glLines = glLines;
 }

 
 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public List<FiPeriodBalance> getPeriodBalances() {
  return PeriodBalances;
 }

 public void setPeriodBalances(List<FiPeriodBalance> PeriodBalances) {
  this.PeriodBalances = PeriodBalances;
 }

 public String getPurpose() {
  return purpose;
 }

 public void setPurpose(String purpose) {
  this.purpose = purpose;
 }

 public List<SalesPartCompany> getSalesPartCompany() {
  return salesPartCompany;
 }

 public void setSalesPartCompany(List<SalesPartCompany> salesPartCompany) {
  this.salesPartCompany = salesPartCompany;
 }

 public Date getValidFrom() {
  return validFrom;
 }

 public void setValidFrom(Date validFrom) {
  this.validFrom = validFrom;
 }

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
 }

 public Date getReturnDue() {
  return returnDue;
 }

 public void setReturnDue(Date returnDue) {
  this.returnDue = returnDue;
 }

 public boolean isReturnRequired() {
  return returnRequired;
 }

 public void setReturnRequired(boolean returnRequired) {
  this.returnRequired = returnRequired;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public long getRevision() {
  return revision;
 }

 public void setRevision(long revision) {
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
  if (!(object instanceof Fund)) {
   return false;
  }
  Fund other = (Fund) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.RestrictedFund[ id=" + id + " ]";
 }
 
}
