/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.company;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.JoinColumn;
import javax.persistence.Version;

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
@Entity@Table(name="comp08")
@NamedQuery(name="lastDocNum", 
        query="select num from CompanyDocNumbers num where num.comp.id = :compId and "
        + "num.docType = :docType")
@SequenceGenerator(name = "companyDocNum_s1", sequenceName = "comp08_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")

public class CompanyDocNumbers implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "companyDocNum_s1")
 @Column(name="comp_doc_num_id")
 private Long id;
 @ManyToOne
@JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
private CompanyBasic comp;

@Column(name="doc_type")
private String docType;

@Column(name="doc_num")
private long docNum;

@Version
@Column(name="changes")
private long changes;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CompanyBasic getComp() {
  return comp;
 }

 public void setComp(CompanyBasic comp) {
  this.comp = comp;
 }

 public String getDocType() {
  return docType;
 }

 public void setDocType(String docType) {
  this.docType = docType;
 }

 public long getDocNum() {
  return docNum;
 }

 public void setDocNum(long docNum) {
  this.docNum = docNum;
 }

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
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
  if (!(object instanceof CompanyDocNumbers)) {
   return false;
  }
  CompanyDocNumbers other = (CompanyDocNumbers) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.fi.company.CompanyDocNumbers[ id=" + id + " ]";
 }
 
}
