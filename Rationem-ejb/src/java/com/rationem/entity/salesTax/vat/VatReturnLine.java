/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocInvoiceAr;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.persistence.TableGenerator;
import javax.persistence.Column;
import javax.persistence.Temporal;
import java.util.Collection;

import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.TemporalType.DATE;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="vat_doc01")
@SequenceGenerator(name = "vatReturnLine_s1", sequenceName = "vat_doc01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class VatReturnLine implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatReturnLine_s1")
 @Column(name="vat_return_line_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="vat_return_id", referencedColumnName="vat_return_id")
 private VatReturn vatReturn;
 
 @Column(name="vat_trans_type")
 private String vatTransType;
 
 @ManyToOne
 @JoinColumn(name="vat_code_id", referencedColumnName="vat_code_id")
 private VatCode vatCode;
 @Column(name="goods_value")
 private double goodsValue;
 @Column(name="vat_value")
 private double vatValue;
 @Column(name="vat_irrecoverable_value")
 private double irrecoverableValue;
 @ManyToOne
 @JoinColumn(name="fi_doc_line_id", referencedColumnName="doc_line_id")
 private DocLineBase fiDocLine;
 
 @ManyToOne
 @JoinColumn(name="ar_account_id", referencedColumnName="ar_account_id")
 private ArAccount arAccount;
 
 
 
 @ManyToOne
 @JoinColumn(name="ap_account_id", referencedColumnName="ap_account_id")
 private ApAccount apAccount;

 @ManyToOne
 @JoinColumn(name="invoice_id", referencedColumnName="ar_inv_id")
 private DocInvoiceAr arInvoice;
 @ManyToOne
 @JoinColumn(name="ar_ap_partner_id", referencedColumnName="partner_id")
 private PartnerBase partner; 
 @ManyToOne
 @JoinColumn(name="fi_doc_id", referencedColumnName="doc_id")
 private DocFi fiDoc;
 @Column(name="vat_paid")
 private boolean vatPaid;
 @ManyToOne
 @JoinColumn(name="vat_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp vatGlAccount;
 @ManyToOne
 @JoinColumn(name="vat_provn_gl_ac_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp provnGlAccount;
 @ManyToOne
 @JoinColumn(name="irrecov_gl_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp vatIrrecoverableGlAccount;
 @ManyToOne
 @JoinColumn(name="gl_expense_account_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp expenseGlAccount;
 
 @OneToOne
 @JoinColumn(name="exp_doc_line_id", referencedColumnName="doc_line_id")
 private DocLineGl expenseDocLine;
 
 @Column(name="doc_paid")
 private boolean docPaid;
 @Temporal(DATE)
 @Column(name="doc_paid_date")
 private Date paidDate;
 @Column(name="bad_debt")
 private boolean badDebt;
 
 @OneToOne
 @JoinColumn(name="cleared_by_line_id", referencedColumnName="vat_return_line_id")
 private VatReturnLine clearedByLine;
 @OneToOne
 @JoinColumn(name="clearing_for_line_id", referencedColumnName="vat_return_line_id")
 private VatReturnLine clearingForLine;
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
 private int changes;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public ArAccount getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccount arAccount) {
  this.arAccount = arAccount;
 }

 public ApAccount getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccount apAccount) {
  this.apAccount = apAccount;
 }

 
 public VatReturn getVatReturn() {
  return vatReturn;
 }

 public void setVatReturn(VatReturn vatReturn) {
  this.vatReturn = vatReturn;
 }

 public String getVatTransType() {
  return vatTransType;
 }

 public void setVatTransType(String vatTransType) {
  this.vatTransType = vatTransType;
 }

 public VatCode getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCode vatCode) {
  this.vatCode = vatCode;
 }

 public double getGoodsValue() {
  return goodsValue;
 }

 public void setGoodsValue(double goodsValue) {
  this.goodsValue = goodsValue;
 }

 public double getVatValue() {
  return vatValue;
 }

 public void setVatValue(double vatValue) {
  this.vatValue = vatValue;
 }

 public double getIrrecoverableValue() {
  return irrecoverableValue;
 }

 public void setIrrecoverableValue(double irrecoverableValue) {
  this.irrecoverableValue = irrecoverableValue;
 }

 public DocLineBase getFiDocLine() {
  return fiDocLine;
 }

 public void setFiDocLine(DocLineBase fiDocLine) {
  this.fiDocLine = fiDocLine;
 }

 public DocInvoiceAr getArInvoice() {
  return arInvoice;
 }

 public void setArInvoice(DocInvoiceAr arInvoice) {
  this.arInvoice = arInvoice;
 }

 public PartnerBase getPartner() {
  return partner;
 }

 public void setPartner(PartnerBase partner) {
  this.partner = partner;
 }

 public DocFi getFiDoc() {
  return fiDoc;
 }

 public void setFiDoc(DocFi fiDoc) {
  this.fiDoc = fiDoc;
 }

 public boolean isVatPaid() {
  return vatPaid;
 }

 public void setVatPaid(boolean vatPaid) {
  this.vatPaid = vatPaid;
 }

 public FiGlAccountComp getVatGlAccount() {
  return vatGlAccount;
 }

 public void setVatGlAccount(FiGlAccountComp vatGlAccount) {
  this.vatGlAccount = vatGlAccount;
 }

 public FiGlAccountComp getProvnGlAccount() {
  return provnGlAccount;
 }

 public void setProvnGlAccount(FiGlAccountComp provnGlAccount) {
  this.provnGlAccount = provnGlAccount;
 }

 public FiGlAccountComp getVatIrrecoverableGlAccount() {
  return vatIrrecoverableGlAccount;
 }

 public void setVatIrrecoverableGlAccount(FiGlAccountComp vatIrrecoverableGlAccount) {
  this.vatIrrecoverableGlAccount = vatIrrecoverableGlAccount;
 }

 public FiGlAccountComp getExpenseGlAccount() {
  return expenseGlAccount;
 }

 public void setExpenseGlAccount(FiGlAccountComp expenseGlAccount) {
  this.expenseGlAccount = expenseGlAccount;
 }

 public DocLineGl getExpenseDocLine() {
  return expenseDocLine;
 }

 public void setExpenseDocLine(DocLineGl expenseDocLine) {
  this.expenseDocLine = expenseDocLine;
 }

 public boolean isDocPaid() {
  return docPaid;
 }

 public void setDocPaid(boolean docPaid) {
  this.docPaid = docPaid;
 }

 public Date getPaidDate() {
  return paidDate;
 }

 public void setPaidDate(Date paidDate) {
  this.paidDate = paidDate;
 }

 public boolean isBadDebt() {
  return badDebt;
 }

 public void setBadDebt(boolean badDebt) {
  this.badDebt = badDebt;
 }

 public VatReturnLine getClearedByLine() {
  return clearedByLine;
 }

 public void setClearedByLine(VatReturnLine clearedByLine) {
  this.clearedByLine = clearedByLine;
 }

 public VatReturnLine getClearingForLine() {
  return clearingForLine;
 }

 public void setClearingForLine(VatReturnLine clearingForLine) {
  this.clearingForLine = clearingForLine;
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
  if (!(object instanceof VatReturnLine)) {
   return false;
  }
  VatReturnLine other = (VatReturnLine) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatReturnLine[ id=" + id + " ]";
 }
 
}
