/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.audit.AuditPartner;
import com.rationem.entity.cm.Contact;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.salesTax.vat.VatReturnLine;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;



import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.ManyToMany;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;



/**
 *
 * @author Chris
 */
@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("com.rationem.entity.mdm.PartnerBase")
@Table(name="ptnr01")
@NamedQueries({
@NamedQuery(name = "partnersById",query ="select p from PartnerBase p where p.id in :idSet "),
@NamedQuery(name = "partnerByCategory",query ="select p from PartnerBase p where p.category = :cat "),
@NamedQuery(name = "ptnrsByTrName",query ="select p from PartnerBase p where upper(p.tradingName) like :trName "),
@NamedQuery(name = "ptnrsAll",query ="select p from PartnerBase p"),
@NamedQuery(name="ptnrByRef", query="select p from PartnerBase p where p.ref = :ptnrRef"),
@NamedQuery(name="ptnrsByRef", query="select p from PartnerBase p where p.ref like :ptnrRef")
//@NamedQuery(name="ptnrsInRole", 
//  query="select p from PartnerBase p where p. in :roles ")]
})
@SequenceGenerator(name = "ptnrBase_s1", sequenceName = "ptnr01_s1", allocationSize = 1,initialValue=1 )
@Entity
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class PartnerBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "ptnrBase_s1")
 @Column(name="partner_id")
 private Long id;
 @Column(name="partner_ref")
 private String ref;
 @Column(name="trading_name")
 private String tradingName;
 
 @ManyToOne
 @JoinColumn(name="country_id",referencedColumnName="country_id")
 private Country country;
 
   

 @ManyToOne
 @JoinColumn(name="created_by_id",referencedColumnName="partner_id")
 private User createdBy;

 @Column(name="created_on")
 @Temporal(TIMESTAMP)
 private Date createdDate;

  
 @ManyToOne
 @JoinColumn(name="changed_by_id",referencedColumnName="partner_id")
 private User changedBy;

 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date changedOn;

// move to sub class
 @Column(name="vat_number")
 private String vatNumber;

 @Column(name="vat_registered_date")
 @Temporal(DATE)
 private Date vatRegisteredDate;
 @Column(name="web_address")
 private String webAddress;
 @Column(name="email_address")
 private String email;
 @Column(name="url")
 private String url;
 @Column(name="telephone")
 private String telephone;
 @Column(name="mobile_telephone")
 private String mobileTelephone;
 @Column(name="catergory")
 private String category;

 @ManyToOne
 @JoinColumn(name="default_address_id", referencedColumnName="address_id")
 private Address defaultAddress;
 

  

 @Version
 @Column(name="changes")
 private Long revision;

 @OneToMany(mappedBy = "arAccountFor")
 private List<ArAccount> arAccounts;
 
 @ManyToMany
 @JoinTable(name="ptnr05",
   joinColumns={@JoinColumn(name="partner_id", referencedColumnName="partner_id")},
   inverseJoinColumns={@JoinColumn(name="ptnr_role_id",referencedColumnName="ptnr_role_id")})
 private List<PartnerRole> partnerRoles;
 
 
 
 @OneToMany(mappedBy = "partner")
 private List<AuditPartner> auditRecords;
 @OneToMany(mappedBy = "partner")
 private List<VatReturnLine> vatReturnLines;
 @OneToMany(mappedBy = "apAccountFor")
 private List<ApAccount> apAccounts;
 
 @OneToMany(mappedBy = "contactFor")
 private List<Contact> contacts;
 
 
 

    public PartnerBase() {
        this.createdDate = new Date();
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    

    public Date getCreatedDate() {
        if(createdDate == null){
          createdDate = new Date();
        }
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

 public List<ArAccount> getArAccounts() {
  return arAccounts;
 }

 public void setArAccounts(List<ArAccount> arAccounts) {
  this.arAccounts = arAccounts;
 }

 public List<AuditPartner> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditPartner> auditRecords) {
  this.auditRecords = auditRecords;
 }

 public List<ApAccount> getApAccounts() {
  return apAccounts;
 }

 public void setApAccounts(List<ApAccount> apAccounts) {
  this.apAccounts = apAccounts;
 }

 public List<Contact> getResponsibilityOf() {
  return contacts;
 }

 public void setResponsibilityOf(List<Contact> contacts) {
  this.contacts = contacts;
 }

 public Country getCountry() {
  return country;
 }

 public void setCountry(Country country) {
  this.country = country;
 }

 
    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Address defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }
    
    
/*
    public User getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(User lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
*/
    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

 public List<PartnerRole> getPartnerRoles() {
  return partnerRoles;
 }

 public void setPartnerRoles(List<PartnerRole> partnerRoles) {
  this.partnerRoles = partnerRoles;
 }

 

 

 public String getTradingName() {
  return tradingName;
 }

 public void setTradingName(String tradingName) {
  this.tradingName = tradingName;
 }

    
    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Date getVatRegisteredDate() {
        return vatRegisteredDate;
    }

    public void setVatRegisteredDate(Date vatRegisteredDate) {
        this.vatRegisteredDate = vatRegisteredDate;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }


    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
/*
  public List<ArAccount> getArAccounts() {
    return arAccounts;
  }

  public void setArAccounts(List<ArAccount> arAccounts) {
    this.arAccounts = arAccounts;
  }
*/
  public String getMobileTelephone() {
    return mobileTelephone;
  }

  public void setMobileTelephone(String mobileTelephone) {
    this.mobileTelephone = mobileTelephone;
  }

 
 
 
 
  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getWebAddress() {
    return webAddress;
  }

  public void setWebAddress(String webAddress) {
    this.webAddress = webAddress;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

 

 public List<VatReturnLine> getVatReturnLines() {
  return vatReturnLines;
 }

 public void setVatReturnLines(List<VatReturnLine> vatReturnLines) {
  this.vatReturnLines = vatReturnLines;
 }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartnerBase)) {
            return false;
        }
        PartnerBase other = (PartnerBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.PartnerBase[id=" + id + "]";
    }
 
}
