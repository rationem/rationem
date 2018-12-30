/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

import com.rationem.entity.audit.AuditCompany;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.config.company.ChartOfAccounts;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import com.rationem.entity.config.company.FisPeriodRule;


import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.mdm.Country;
import com.rationem.entity.mdm.Currency;
import com.rationem.entity.salesTax.vat.VatRegistration;
import com.rationem.entity.tr.bank.BankAccountCompany;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.QueryHint;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="comp01")
@NamedQueries({
    @NamedQuery(name = "allCompanies",query = "SELECT comp FROM CompanyBasic comp"),
    @NamedQuery(name = "compById",query = "SELECT c FROM CompanyBasic c where c.id = :id",
      hints={
       @QueryHint(name=QueryHints.FETCH, value="c.bankAccounts"),
       @QueryHint(name=QueryHints.BATCH, value="c.glAccountComps")}  ),
    @NamedQuery(name = "compsByRef", query = "SELECT comp FROM CompanyBasic comp "
    + "WHERE comp.number = :compRef " + "ORDER BY comp.number")
    , @NamedQuery(name = "findCompanyByTitle", query = "SELECT comp FROM CompanyBasic comp "
    + "WHERE comp.name = :CompTitle " + "ORDER BY comp.number")

})
@SequenceGenerator(name = "companyBasic_s1", sequenceName = "comp01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class CompanyBasic implements Serializable {

 
 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "companyBasic_s1")
 @Column(name="home_comp_id")
 private Long id;
 @Column(name="reference")
 private String number;
 @Column(name="short_name")
 private String name;
 @Column(name="full_name")
 private String legalName;
 @Column(name="comp_type")
 private String companyType;
 @Column(name="corporate")
 private boolean corp;
 @Column(name="reg_num")
 private String companyNumber;
 @Column(name="charity")
 private boolean charity;
 @Column(name="charity_number")
 private String charityNumber;
 @Temporal(DATE)
 @Column(name="charity_reg_date")
 private Date charityRegDate;
 @Column(name="vat_reg")
 private boolean vatReg;
 @Column(name="vat_num")
 private String vatNumber;
 @Column(name="restricted_funds")
 private boolean restrictedFunds;
 @Column(name="fund_accounting")
 private boolean fundAccounting;
 @OneToMany(mappedBy = "fundForComp")
    private List<Fund> compRestrictedFunds;
 @Column(name="default_comp") 
 private boolean defaultCompany;
 @Temporal(DATE)
 @Column(name="incorp_date")
 private Date incorporatedDate;
 @Column(name="bus_area")
 private boolean businessArea;
 @ManyToOne
 @JoinColumn(name="currency_id", referencedColumnName="currency_id")
 private Currency currency;
 @ManyToOne
 @JoinColumn(name="country_id", referencedColumnName="country_id")
 private Country country;
 @Column(name="locale_code")
 private String localeCode;
 @OneToOne(mappedBy = "comp")
 private CompanyApAr compApAr;
 @ManyToOne
 @JoinColumn(name="fisc_per_rule_id", referencedColumnName="period_rule_id")
 private FisPeriodRule periodRule;
 @OneToMany(mappedBy = "comp")
 private List<CompPostPer> compPostPeriods;
 @OneToMany(mappedBy = "comp", cascade=REMOVE)
 private List<AuditCompany> auditRecs;
 @ManyToOne
 @JoinColumn(name="created_by_id",  referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changedDate;
 @Version
 @Column(name="changes")
 private long changes;
 @ManyToOne
 @JoinColumn(name="chart_of_accounts_id", referencedColumnName="accounts_chart_id")
 private ChartOfAccounts chartOfAccounts;
 @OneToMany(mappedBy = "comp")
 private List<VatRegistration> vatRegistrations;
 @OneToOne
 @JoinColumn(name="active_vat_reg_id", referencedColumnName="vat_reg_id")
 private VatRegistration vatRegCurrent;
 
 
 @OneToMany(mappedBy = "company")
 private List<PeriodControl> periodControl;
 @ManyToOne
 @JoinColumn(name="address_id", referencedColumnName="address_id")
 private Address address;
 @OneToMany(mappedBy = "comp")
 private List<BankAccountCompany> bankAccounts;
 @OneToMany(mappedBy = "company")
 private List<ApAccount> apAccounts;
 
 
 @OneToMany(mappedBy = "company")
 private List<FiGlAccountComp> glAccountComps;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCompany> getAuditRecs() {
  return auditRecs;
 }

 public void setAuditRecs(List<AuditCompany> auditRecs) {
  this.auditRecs = auditRecs;
 }

 public List<CompPostPer> getCompPostPeriods() {
  return compPostPeriods;
 }

 public void setCompPostPeriods(List<CompPostPer> compPostPeriods) {
  this.compPostPeriods = compPostPeriods;
 }

 
 public List<Fund> getCompRestrictedFunds() {
  return compRestrictedFunds;
 }

 public void setCompRestrictedFunds(List<Fund> compRestrictedFunds) {
  this.compRestrictedFunds = compRestrictedFunds;
 }

 public Address getAddress() {
  return address;
 }

 public void setAddress(Address address) {
  this.address = address;
 }

 public List<BankAccountCompany> getBankAccounts() {
  return bankAccounts;
 }

 public void setBankAccounts(List<BankAccountCompany> bankAccounts) {
  this.bankAccounts = bankAccounts;
 }

 public List<ApAccount> getApAccounts() {
  return apAccounts;
 }

 public void setApAccounts(List<ApAccount> apAccounts) {
  this.apAccounts = apAccounts;
 }

 
 public String getNumber() {
  return number;
 }

 public void setNumber(String number) {
  this.number = number;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getLegalName() {
  return legalName;
 }

 public void setLegalName(String legalName) {
  this.legalName = legalName;
 }

 public String getLocaleCode() {
  return localeCode;
 }

 public void setLocaleCode(String localeCode) {
  this.localeCode = localeCode;
 }

 public String getCompanyType() {
  return companyType;
 }

 public void setCompanyType(String companyType) {
  this.companyType = companyType;
 }

 public String getCompanyNumber() {
  return companyNumber;
 }

 public void setCompanyNumber(String companyNumber) {
  this.companyNumber = companyNumber;
 }

 public CompanyApAr getCompApAr() {
  return compApAr;
 }

 public void setCompApAr(CompanyApAr compApAr) {
  this.compApAr = compApAr;
 }
 
 
 public boolean isCorp() {
  return corp;
 }

 public void setCorp(boolean corp) {
  this.corp = corp;
 }

 public boolean isCharity() {
  return charity;
 }

 public void setCharity(boolean charity) {
  this.charity = charity;
 }

 public String getCharityNumber() {
  return charityNumber;
 }

 public void setCharityNumber(String charityNumber) {
  this.charityNumber = charityNumber;
 }

 public Date getCharityRegDate() {
  return charityRegDate;
 }

 public void setCharityRegDate(Date charityRegDate) {
  this.charityRegDate = charityRegDate;
 }

 public boolean isVatReg() {
  return vatReg;
 }

 public void setVatReg(boolean vatReg) {
  this.vatReg = vatReg;
 }

 public String getVatNumber() {
  return vatNumber;
 }

 public void setVatNumber(String vatNumber) {
  this.vatNumber = vatNumber;
 }

 public boolean isRestrictedFunds() {
  return restrictedFunds;
 }

 public void setRestrictedFunds(boolean restrictedFunds) {
  this.restrictedFunds = restrictedFunds;
 }

 public boolean isDefaultCompany() {
  return defaultCompany;
 }

 public void setDefaultCompany(boolean defaultCompany) {
  this.defaultCompany = defaultCompany;
 }

 public boolean isFundAccounting() {
  return fundAccounting;
 }

 public void setFundAccounting(boolean fundAccounting) {
  this.fundAccounting = fundAccounting;
 }

 public List<FiGlAccountComp> getGlAccountComps() {
  return glAccountComps;
 }

 public void setGlAccountComps(List<FiGlAccountComp> glAccountComps) {
  this.glAccountComps = glAccountComps;
 }

 public Date getIncorporatedDate() {
  return incorporatedDate;
 }

 public void setIncorporatedDate(Date incorporatedDate) {
  this.incorporatedDate = incorporatedDate;
 }

 public boolean isBusinessArea() {
  return businessArea;
 }

 public void setBusinessArea(boolean businessArea) {
  this.businessArea = businessArea;
 }

 public Country getCountry() {
  return country;
 }

 public void setCountry(Country country) {
  this.country = country;
 }

 
 public Currency getCurrency() {
  return currency;
 }

 public void setCurrency(Currency currency) {
  this.currency = currency;
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

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }

 public ChartOfAccounts getChartOfAccounts() {
  return chartOfAccounts;
 }

 public void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
  this.chartOfAccounts = chartOfAccounts;
 }

 

 public List<PeriodControl> getPeriodControl() {
  return periodControl;
 }

 public void setPeriodControl(List<PeriodControl> periodControl) {
  this.periodControl = periodControl;
 }

 public FisPeriodRule getPeriodRule() {
  return periodRule;
 }

 public void setPeriodRule(FisPeriodRule periodRule) {
  this.periodRule = periodRule;
 }

 
 public VatRegistration getVatRegCurrent() {
  return vatRegCurrent;
 }

 public void setVatRegCurrent(VatRegistration vatRegCurrent) {
  this.vatRegCurrent = vatRegCurrent;
 }

 

 public List<VatRegistration> getVatRegistrations() {
  return vatRegistrations;
 }

 public void setVatRegistrations(List<VatRegistration> vatRegistrations) {
  this.vatRegistrations = vatRegistrations;
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
  if (!(object instanceof CompanyBasic)) {
   return false;
  }
  CompanyBasic other = (CompanyBasic) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.CompanyBasic[ id=" + id + " ]";
 }
 
}
