/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.audit.AuditCountry;
import com.rationem.entity.config.company.ChartOfAccounts;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.OneToMany;


/**
 *
 * @author Chris
 */
@Entity
@Table(name="mdm02" )
@NamedQueries({
 @NamedQuery(name="countriesAll", query="Select c from Country c order by c.countryName"),
 @NamedQuery(name="countriesByname", 
    query="Select c from Country c where c.countryName like :name order by c.countryName"),
 @NamedQuery(name="countryByAlpha2", 
        query="Select c from Country c where c.countryCode2 = :code2")
})
@SequenceGenerator(name = "country_s1", sequenceName = "mdm02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class Country implements Serializable {

 @OneToMany(mappedBy = "country")
 private List<PartnerBase> partners;

 
 
 @OneToMany(mappedBy = "chartCountry")
 private List<ChartOfAccounts> accountCharts;
 @OneToMany(mappedBy = "country")
 private List<Address> addresses;
 private static final long serialVersionUID = 1L;
 @Id
 @Column(name="country_id")
 @GeneratedValue(generator = "country_s1")
 private Long id;
 @Column(name="country_name")
 private String countryName;
 @Column(name="country_name_fr")
 private String countryNameFr;
 @Column(name="country_code_2ALFA")
 private String countryCode2;
 @Column(name="country_code_3ALFA")
 private String countryCode3;
 @Column(name="country_numeric")
 private int countryNumeric;
 @Column(name="dial_code")
 private String dialCd;
 @Column(name="currency_alpha")
 private String currCode;
 @Column(name="curr_minor_unit")
 private int fractionalLength;
 @Column(name="currency_name")
 private String currName;
 @Column(name="curr_numeric_code")
 private int currNumCode;
 @Column(name="currency_major_name", length=20)
 private String currMajorTitle;
 @Column(name="currency_major_name_plural", length=20)
 private String currMajorTitlePlural;
 @Column(name="independent")
 private boolean independent;
 @Column(name="dependency")
 private String dependency;
 @Temporal(TIMESTAMP)
 @Column(name="create_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="create_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="change_on")
 private Date changedOn;
 @ManyToOne
 @JoinColumn(name="change_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int changes;
 
 @OneToMany(mappedBy = "country", cascade=REMOVE)
 private List<AuditCountry> auditRecords;
 
 

 public Long getId() {
  return id;
 }
 

 public void setId(Long id) {
  this.id = id;
 }

 public List<AuditCountry> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditCountry> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public List<ChartOfAccounts> getAccountCharts() {
  return accountCharts;
 }

 public void setAccountCharts(List<ChartOfAccounts> accountCharts) {
  this.accountCharts = accountCharts;
 }

 
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNameFr() {
        return countryNameFr;
    }

    public void setCountryNameFr(String countryNameFr) {
        this.countryNameFr = countryNameFr;
    }

    public String getCountryCode2() {
        return countryCode2;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getCountryCode3() {
        return countryCode3;
    }

    public void setCountryCode3(String countryCode3) {
        this.countryCode3 = countryCode3;
    }

    public int getCountryNumeric() {
        return countryNumeric;
    }

    public void setCountryNumeric(int countryNumeric) {
        this.countryNumeric = countryNumeric;
    }

    public String getDialCd() {
        return dialCd;
    }

    public void setDialCd(String dialCd) {
        this.dialCd = dialCd;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public int getFractionalLength() {
        return fractionalLength;
    }

    public void setFractionalLength(int fractionalLength) {
        this.fractionalLength = fractionalLength;
    }

 public String getCurrMajorTitle() {
  return currMajorTitle;
 }

 public void setCurrMajorTitle(String currMajorTitle) {
  this.currMajorTitle = currMajorTitle;
 }

 public String getCurrMajorTitlePlural() {
  return currMajorTitlePlural;
 }

 public void setCurrMajorTitlePlural(String currMajorTitlePlural) {
  this.currMajorTitlePlural = currMajorTitlePlural;
 }

    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public int getCurrNumCode() {
        return currNumCode;
    }

    public void setCurrNumCode(int currNumCode) {
        this.currNumCode = currNumCode;
    }

    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

 public List<PartnerBase> getPartners() {
  return partners;
 }

 public void setPartners(List<PartnerBase> partners) {
  this.partners = partners;
 }
    
    

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
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
  if (!(object instanceof Country)) {
   return false;
  }
  Country other = (Country) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.mdm.CountryISO3166[ id=" + id + " ]";
 }
 
}
