/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.arap;

import com.rationem.entity.audit.AuditPaymentType;
import javax.persistence.NamedQuery;
import com.rationem.entity.config.company.Ledger;
import javax.persistence.Version;
import com.rationem.entity.config.common.Uom;
import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.tr.bank.BankAccountCompany;

import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.tr.bacs.BacsTransCode;
import com.rationem.entity.tr.bank.ChequeTemplate;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.NamedQueries;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config11")
@SequenceGenerator(name = "paymentType_s1", sequenceName = "bac_config11_s1", allocationSize = 1,initialValue=1)
@NamedQueries({
 @NamedQuery(name = "allPaymentTypes",query = "SELECT p FROM PaymentType p order by p.payTypeCode"),
 @NamedQuery(name = "payTypesForComp",query = "SELECT p FROM PaymentType p  where p.company.id = :compId order by p.payTypeCode"),
 @NamedQuery(name = "payTypesForCompLed",query = "SELECT p FROM PaymentType p  where p.company.id = :compId and p.ledger.id = :ledId order by p.payTypeCode"),
 @NamedQuery(name = "payTypesByChqTempl",
   query = "SELECT p.id FROM PaymentType p where p.chqTemplate.id = :chqTemplId")
})
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class PaymentType implements Serializable {
 private static final long serialVersionUID = 1L;
 public static final  String PAY_MED_CHQ = "CHQ";
 public static final String PAY_MED_DD = "DD";
 public static final String PAY_MED_DC = "DC";
 public static final String PAY_MED_CASH = "CSH";
 public static final int PAY_SUMM_DOC = 1;
 public static final int PAY_SUMM_ACNT = 2;
 
 @Id
 @GeneratedValue(generator = "paymentType_s1")
 @Column(name="pay_type_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic company;
 @Column(name="pay_type_code")
 private String payTypeCode;
 @Column(name="description")
 private String description;
  /**
   * CHQ = cheque DD=Direct debit DC=direct collection CSH = cash
   */
 @Column(name="pay_medium")
 private String payMedium;
 @Column(name="summ_level")
 private int summLevel;
 @OneToMany(mappedBy = "payType")
 private List<DocLineAr> arDocLines;
 @ManyToOne
 @JoinColumn(name="medium_uom_id", referencedColumnName="uom_id")
 private Uom mediumUom;
 @ManyToOne
 @JoinColumn(name="ledger_id", referencedColumnName="ledger_id")
 private Ledger ledger;
 @Column(name="inbound")
 private boolean inbound;
 
 @Column(name="has_cheque_templete")
 private boolean hasChqTemplate;
 
 @ManyToOne
 @JoinColumn(name="bank_account_id", referencedColumnName="bank_account_id")
 private BankAccountCompany payTypeForBankAccount;
 @ManyToOne
 @JoinColumn(name="bacs_trans_id", referencedColumnName="dd_trans_cd_id")
 private BacsTransCode bacsTransCode;
 @ManyToOne
 @JoinColumn(name="gl_bank_ac_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp glAccount;
 @OneToMany(mappedBy = "payType")
 private List<AuditPaymentType> paymentTypeAuditRecords;
 @OneToMany(mappedBy = "payType")
 private List<DocLineAp> apDocLines;
 
 @ManyToOne
 @JoinColumn(name="cheque_template_id", referencedColumnName="cheque_template_id")
 private ChequeTemplate chqTemplate;
 
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
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
 private int revision;
 
 
 
 


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public List<DocLineAr> getArDocLines() {
  return arDocLines;
 }

 public void setArDocLines(List<DocLineAr> arDocLines) {
  this.arDocLines = arDocLines;
 }

 public List<DocLineAp> getApDocLines() {
  return apDocLines;
 }

 public void setApDocLines(List<DocLineAp> apDocLines) {
  this.apDocLines = apDocLines;
 }

 
 public BacsTransCode getBacsTransCode() {
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCode bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
 }

 public ChequeTemplate getChqTemplate() {
  return chqTemplate;
 }

 public void setChqTemplate(ChequeTemplate chqTemplate) {
  this.chqTemplate = chqTemplate;
 }

 
 public CompanyBasic getCompany() {
  return company;
 }

 public void setCompany(CompanyBasic company) {
  this.company = company;
 }

 public String getPayTypeCode() {
  return payTypeCode;
 }

 public void setPayTypeCode(String payTypeCode) {
  this.payTypeCode = payTypeCode;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public FiGlAccountComp getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountComp glAccount) {
  this.glAccount = glAccount;
 }

 public boolean isHasChqTemplate() {
  return hasChqTemplate;
 }

 public void setHasChqTemplate(boolean hasChqTemplate) {
  this.hasChqTemplate = hasChqTemplate;
 }

 
 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }

 public BankAccountCompany getPayTypeForBankAccount() {
  return payTypeForBankAccount;
 }

 public void setPayTypeForBankAccount(BankAccountCompany payTypeForBankAccount) {
  this.payTypeForBankAccount = payTypeForBankAccount;
 }

 public List<AuditPaymentType> getPaymentTypeAuditRecords() {
  return paymentTypeAuditRecords;
 }

 public void setPaymentTypeAuditRecords(List<AuditPaymentType> paymentTypeAuditRecords) {
  this.paymentTypeAuditRecords = paymentTypeAuditRecords;
 }

 
 public String getPayMedium() {
  return payMedium;
 }

 public void setPayMedium(String payMedium) {
  this.payMedium = payMedium;
 }

 public int getSummLevel() {
  return summLevel;
 }

 public void setSummLevel(int summLevel) {
  this.summLevel = summLevel;
 }
/*
 public List<DocLineAR> getArDocLines() {
  return arDocLines;
 }

 public void setArDocLines(List<DocLineAR> arDocLines) {
  this.arDocLines = arDocLines;
 }

 public Uom getMediumUom() {
  return mediumUom;
 }

 public void setMediumUom(Uom mediumUom) {
  this.mediumUom = mediumUom;
 }

 public Ledger getLedger() {
  return ledger;
 }

 public void setLedger(Ledger ledger) {
  this.ledger = ledger;
 }
*/
 public boolean isInbound() {
  return inbound;
 }

 public void setInbound(boolean inbound) {
  this.inbound = inbound;
 }

 public Uom getMediumUom() {
  return mediumUom;
 }

 public void setMediumUom(Uom mediumUom) {
  this.mediumUom = mediumUom;
 }
 
 
/*
 public BankAccountCompany getPayTypeForBankAccount() {
  return payTypeForBankAccount;
 }

 public void setPayTypeForBankAccount(BankAccountCompany payTypeForBankAccount) {
  this.payTypeForBankAccount = payTypeForBankAccount;
 }

 public BacsTransCode getBacsTransCode() {
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCode bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
 }
*/
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

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }
/*
 public FiGlAccountComp getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountComp glAccount) {
  this.glAccount = glAccount;
 }
*/
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof PaymentType)) {
   return false;
  }
  PaymentType other = (PaymentType) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.arap.PaymentType[ id=" + id + " ]";
 }
 
}
