/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.glAccount;

import com.rationem.entity.user.User;
import com.rationem.entity.audit.AuditGlAccount;
import com.rationem.entity.config.company.AccountType;
//import com.rationem.entity.config.fi.FiGlActType;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.rationem.entity.config.company.ChartOfAccounts;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import java.io.Serializable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;

import static javax.persistence.InheritanceType.JOINED;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;


/**
 *
 * @author Chris
 */
@Entity
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("fi.glAccount.FiGLAccountBase")
@Table(name="fi_account01" )
@NamedQueries({
    @NamedQuery(name = "findAllGlAccounts",query = "SELECT gl "
    + "FROM FiGLAccountBase gl ORDER BY gl.ref")
    , @NamedQuery(name = "findGlAccountByRef", query = "SELECT gl FROM FiGLAccountBase gl "
    + "WHERE gl.ref = :GlReference " + "ORDER BY gl.ref"),
    @NamedQuery(name = "accountByChart", query = "SELECT gl FROM FiGLAccountBase gl "
    + "WHERE  gl.chartOfAccounts.id = :chartId  and gl.ref =  :acntRef"),
    @NamedQuery(name = "accountsByChart",
    query = "SELECT gl from FiGLAccountBase gl where gl.chartOfAccounts.id = :chartId ORDER BY gl.ref"),
    @NamedQuery(name = "accountsByType",
    query = "SELECT gl from FiGLAccountBase gl where gl.accountType.id = :acntTyId ORDER BY gl.ref "),
    @NamedQuery(name = "glAccountsByRef", query = "SELECT gl FROM FiGLAccountBase gl "
    + "WHERE gl.ref like :GlReference " + "ORDER BY gl.ref"),
    @NamedQuery(name = "glAcntsNotInComp", query = "SELECT gl FROM FiGLAccountBase gl "
      + "WHERE gl.chartOfAccounts.id = :chartId and NOT EXISTS "
      + "(select c from FiGlAccountComp c where c.coaAccount.id = gl.id and c.company.id = :compId ) "
      + "ORDER BY gl.ref "),
    @NamedQuery(name = "glAcntsRefNotInComp", query = "SELECT gl FROM FiGLAccountBase gl "
      + "WHERE gl.ref like :glRef and gl.chartOfAccounts.id = :chartId "
      + "and NOT EXISTS "
      + "(select c from FiGlAccountComp c where c.coaAccount.id = gl.id and c.company.id = :compId ) "
      + "ORDER BY gl.ref ")
   
    })
@SequenceGenerator(name = "fiGLAccount_s1", sequenceName = "fi_account01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class FiGLAccountBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "fiGLAccount_s1")
 @Column(name="fi_gl_account_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="accounts_chart_id", referencedColumnName="accounts_chart_id")
 private ChartOfAccounts chartOfAccounts;
    
 @Column(name="fi_mast_ref")
 private String ref;
 @Column(name="short_name")
 private String name;
 @Column(name="description")
 private String description;
 @Column(name="pl")
 private boolean pl;
        

 @ManyToOne
 @JoinColumn(name="account_cat_id", referencedColumnName="account_type_id")
 private AccountType accountType;
 
 @Column(name="report_cat")
 private String reportCat;

    
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date created;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date updateDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User updateBy;
 @Version
 @Column(name="changes")
 private int revision;
    
 @OneToMany(mappedBy = "glAccount")
 private List<AuditGlAccount> auditGlAccounts;

    /* list of company level accounts */
 
 @OneToMany(mappedBy = "coaAccount" )
 private List<FiGlAccountComp> glAccountsComp;
 
    



 public FiGLAccountBase() {
 }
 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public AccountType getAccountType() {
  return accountType;
 }

 public void setAccountType(AccountType accountType) {
  this.accountType = accountType;
 }

 public List<AuditGlAccount> getAuditGlAccounts() {
  return auditGlAccounts;
 }

 public void setAuditGlAccounts(List<AuditGlAccount> auditGlAccounts) {
  this.auditGlAccounts = auditGlAccounts;
 }

 
 public ChartOfAccounts getChartOfAccounts() {
  return chartOfAccounts;
 }

 public void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
  this.chartOfAccounts = chartOfAccounts;
 }

 public List<FiGlAccountComp> getGlAccountsComp() {
  return glAccountsComp;
 }

 public void setGlAccountsComp(List<FiGlAccountComp> glAccountsComp) {
  this.glAccountsComp = glAccountsComp;
 }

 
 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public boolean isPl() {
  return pl;
 }

 public void setPl(boolean pl) {
  this.pl = pl;
 }

 public String getReportCat() {
  return reportCat;
 }

 public void setReportCat(String reportCat) {
  this.reportCat = reportCat;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreated() {
  return created;
 }

 public void setCreated(Date created) {
  this.created = created;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public User getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(User updateBy) {
  this.updateBy = updateBy;
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
  if (!(object instanceof FiGLAccountBase)) {
   return false;
  }
  FiGLAccountBase other = (FiGLAccountBase) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.glAccount.FiGLAccountBase[ id=" + id + " ]";
 }
 
}
