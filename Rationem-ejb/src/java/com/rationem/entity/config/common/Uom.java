/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.common;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.persistence.JoinColumn;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;



/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config01")
@NamedQueries({
@NamedQuery(name="UomAll", query="Select u from Uom u"),
@NamedQuery(name="UomByCode", query="Select u from Uom u "+
        "where u.uomCode like :UomCode")
        })
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
@SequenceGenerator(name = "Uom_s1", sequenceName = "bac_config01_s1", allocationSize = 1,initialValue=1)
public class Uom implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "Uom_s1")
 @Column(name="uom_id")
 private Long id;
 @Column(name="uom_code")
 private String uomCode;
 @Column(name="uom_name")
 private String name;
 @Column(name="description")
 private String description;
 @Column(name="process_code")
 private String processCode;
 @Temporal(TIMESTAMP)
 @Column(name="create_on")
 private Date createDate;
 @Temporal(TIMESTAMP)
 @Column(name="changed_on")
 private Date changeDate;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User changedBy;
 @Version
 @Column(name="changes")
 private int revision;
    
    
 @OneToMany(mappedBy = "mediumUom")
 private List<PaymentType> paymentTypes;
 @OneToMany(mappedBy = "uom")
 private List<PaymentTerms> paymentTermsList;

 public Uom() {
 }



 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getUomCode() {
  return uomCode;
 }

 public void setUomCode(String uomCode) {
  this.uomCode = uomCode;
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

 public String getProcessCode() {
  return processCode;
 }

 public void setProcessCode(String processCode) {
  this.processCode = processCode;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public Date getChangeDate() {
  return changeDate;
 }

 public void setChangeDate(Date changeDate) {
  this.changeDate = changeDate;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public User getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(User changedBy) {
  this.changedBy = changedBy;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }

 public List<PaymentType> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentType> paymentTypes) {
  this.paymentTypes = paymentTypes;
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
  if (!(object instanceof Uom)) {
   return false;
  }
  Uom other = (Uom) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.config.common.Uom[ id=" + id + " ]";
 }
 
}
