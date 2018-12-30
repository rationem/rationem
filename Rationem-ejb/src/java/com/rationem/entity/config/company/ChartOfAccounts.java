/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.audit.AuditChartOfAccounts;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGLAccountBase;
import com.rationem.entity.mdm.Country;
import com.rationem.entity.mdm.Currency;
import javax.persistence.Version;
import java.util.Date;
import com.rationem.entity.user.User;
import java.io.Serializable;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;

import org.eclipse.persistence.annotations.Multitenant;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="comp02" )
@NamedQueries({
 @NamedQuery(name = "getAllChartsOfAccounts",
  query = "SELECT chartOfAc FROM ChartOfAccounts chartOfAc"),
 @NamedQuery(name = "coaByRef",
  query = "SELECT c FROM ChartOfAccounts c where c.reference = :ref")
})
@SequenceGenerator(name = "chartOfAccounts_s1", sequenceName = "comp02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class ChartOfAccounts implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "chartOfAccounts_s1")
 @Column(name="accounts_chart_id")
 private Long id;
 @Column(name="reference")
 private String reference;
 @Column(name="chart_name")
 private String name;
 @ManyToOne
 @JoinColumn(name="currency_id", referencedColumnName="currency_id")
 private Currency chartCurrency;
 @ManyToOne
 @JoinColumn(name="country_id", referencedColumnName="country_id")
 private Country chartCountry;
 
    @ManyToMany
    @JoinTable(
     name="comp05",
     joinColumns =
       @JoinColumn(name="accounts_chart_id", referencedColumnName="accounts_chart_id"),
 //      ,  ACCOUNTS_CHART_ID
     inverseJoinColumns =
       @JoinColumn(name="account_type_id", referencedColumnName="account_type_id")    )
    private List<AccountType> accountTypes;

    

 @Column(name="default_chart")
 private boolean defaultChart;

 @OneToMany(mappedBy = "chartOfAccounts")
 private List<FiGLAccountBase> glAccountsChart;

 
 @OneToMany(mappedBy="chartOfAccounts")
 private List<CompanyBasic> companies;

 @Column(name="bal_fwd")
 private boolean OibalFwd;

 @ManyToOne
 @JoinColumn(name="fis_period_rule", referencedColumnName="period_rule_id")
 private FisPeriodRule periodRule;
 
 @ManyToOne
 @JoinColumn(name="created_by_id")
 private User createdBy;
 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id")
 private User changedBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedDate;
 @Version
 @Column(name="changes")
 private int changes;
 
 
 
 @OneToMany(mappedBy = "coa")
 private List<AuditChartOfAccounts> auditGlAccounts;

 public ChartOfAccounts() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AccountType> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountType> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public List<AuditChartOfAccounts> getAuditGlAccounts() {
  return auditGlAccounts;
 }

 public void setAuditGlAccounts(List<AuditChartOfAccounts> auditGlAccounts) {
  this.auditGlAccounts = auditGlAccounts;
 }

 
 public List<FiGLAccountBase> getGlAccountsChart() {
  return glAccountsChart;
 }

 public void setGlAccountsChart(List<FiGLAccountBase> glAccountsChart) {
  this.glAccountsChart = glAccountsChart;
 }

 public Country getChartCountry() {
  return chartCountry;
 }

 public void setChartCountry(Country chartCountry) {
  this.chartCountry = chartCountry;
 }

 public Currency getChartCurrency() {
  return chartCurrency;
 }

 public void setChartCurrency(Currency chartCurrency) {
  this.chartCurrency = chartCurrency;
 }

 public List<CompanyBasic> getCompanies() {
  return companies;
 }

 public void setCompanies(List<CompanyBasic> companies) {
  this.companies = companies;
 }

 

 public String getReference() {
  return reference;
 }

 public void setReference(String reference) {
  this.reference = reference;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 

 public boolean isDefaultChart() {
  return defaultChart;
 }

 public void setDefaultChart(boolean defaultChart) {
  this.defaultChart = defaultChart;
 }

 public boolean isOibalFwd() {
  return OibalFwd;
 }

 public void setOibalFwd(boolean OibalFwd) {
  this.OibalFwd = OibalFwd;
 }

 public FisPeriodRule getPeriodRule() {
  return periodRule;
 }

 public void setPeriodRule(FisPeriodRule periodRule) {
  this.periodRule = periodRule;
 }

 
 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
 }

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
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
  if (!(object instanceof ChartOfAccounts)) {
   return false;
  }
  ChartOfAccounts other = (ChartOfAccounts) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.coompany.ChartOfAccounts[ id=" + id + " ]";
 }
 
}
