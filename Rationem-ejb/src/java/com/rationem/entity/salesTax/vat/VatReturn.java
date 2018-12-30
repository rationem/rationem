/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.salesTax.vat;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
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
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
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
@NamedQueries({
@NamedQuery(name="vatReturnsAfterDate", 
        query="select ret from VatReturn ret where ret.vatRegScheme = :regSchem "
        + "and ret.returnDate >= :retDate order by ret.returnDate desc"),
@NamedQuery(name="vatReturnsForRegschem", 
        query="select ret from VatReturn ret where ret.vatRegScheme = :regSchem ")
})
@Table(name="vat_mast01")
@SequenceGenerator(name = "vatReturn_s1", sequenceName = "vat_mast01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class VatReturn implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "vatReturn_s1")
 @Column(name="vat_return_id")
 private Long id;
 @ManyToOne
 @JoinColumn(name="reg_scheme_id", referencedColumnName="vat_reg_Scheme_id")
 private VatRegScheme vatRegScheme;
 @Column(name="return_redf")
 private String returnRef;
 @Column(name="prov_return")
 private boolean provisionReturn;
 @Temporal(DATE)
 @Column(name="return_date")
 private Date returnDate;
 @Temporal(DATE)
 @Column(name="pay_date")
 private Date paymentDueDate;
 @Temporal(DATE)
 @Column(name="due_date_paper")
 private Date dueDatePaper;
 @Temporal(DATE)
 @Column(name="due_date_online")
 private Date dueDateOnline;
 /**
  * VAT due on sales
  */
 @Column(name="box1_value")
 private double box1Value;
 /**
  * 
  */
  @Column(name="box2_value")
 private double box2Value;
 @Column(name="box3_value")
 private double box3Value;
 @Column(name="box4_value")
 private double box4Value;
 @Column(name="box5_value")
 private double box5Value;
 @Column(name="box6_value")
 private double box6Value;
 @Column(name="box7_value")
 private double box7Value;
 @Column(name="box8_value")
 private double box8Value;
 /*
 @OneToMany(mappedBy = "vatReturn")
 private List<VatReturnLine> vatReturnLines;
 */
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="CHANGED_BY_ID", referencedColumnName="partner_id")
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

 public VatRegScheme getVatRegScheme() {
  return vatRegScheme;
 }

 public void setVatRegScheme(VatRegScheme vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }

 public String getReturnRef() {
  return returnRef;
 }

 public void setReturnRef(String returnRef) {
  this.returnRef = returnRef;
 }

 public boolean isProvisionReturn() {
  return provisionReturn;
 }

 public void setProvisionReturn(boolean provisionReturn) {
  this.provisionReturn = provisionReturn;
 }

 public Date getReturnDate() {
  return returnDate;
 }

 public void setReturnDate(Date returnDate) {
  this.returnDate = returnDate;
 }

 public Date getPaymentDueDate() {
  return paymentDueDate;
 }

 public void setPaymentDueDate(Date paymentDueDate) {
  this.paymentDueDate = paymentDueDate;
 }

 public Date getDueDatePaper() {
  return dueDatePaper;
 }

 public void setDueDatePaper(Date dueDatePaper) {
  this.dueDatePaper = dueDatePaper;
 }

 public Date getDueDateOnline() {
  return dueDateOnline;
 }

 public void setDueDateOnline(Date dueDateOnline) {
  this.dueDateOnline = dueDateOnline;
 }

 public double getBox1Value() {
  return box1Value;
 }

 public void setBox1Value(double box1Value) {
  this.box1Value = box1Value;
 }

 public double getBox2Value() {
  return box2Value;
 }

 public void setBox2Value(double box2Value) {
  this.box2Value = box2Value;
 }

 public double getBox3Value() {
  return box3Value;
 }

 public void setBox3Value(double box3Value) {
  this.box3Value = box3Value;
 }

 public double getBox4Value() {
  return box4Value;
 }

 public void setBox4Value(double box4Value) {
  this.box4Value = box4Value;
 }

 public double getBox5Value() {
  return box5Value;
 }

 public void setBox5Value(double box5Value) {
  this.box5Value = box5Value;
 }

 public double getBox6Value() {
  return box6Value;
 }

 public void setBox6Value(double box6Value) {
  this.box6Value = box6Value;
 }

 public double getBox7Value() {
  return box7Value;
 }

 public void setBox7Value(double box7Value) {
  this.box7Value = box7Value;
 }

 public double getBox8Value() {
  return box8Value;
 }

 public void setBox8Value(double box8Value) {
  this.box8Value = box8Value;
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
  if (!(object instanceof VatReturn)) {
   return false;
  }
  VatReturn other = (VatReturn) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.salesTax.vat.VatReturn[ id=" + id + " ]";
 }
 
}
