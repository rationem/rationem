/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.ma;

import com.rationem.entity.audit.AuditProgramme;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

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
@Table(name="ma_mast03")
@NamedQueries({
 @NamedQuery(name="allProg", query="select p from Programme p where p.programmeForCompany.id = :compId"),
 @NamedQuery(name="progByName", 
        query="select p from Programme p where p.name like :name  and p.programmeForCompany.id = :compId"),
 @NamedQuery(name="progsByRef", 
        query="select p from Programme p where p.refrence like :ref  and p.programmeForCompany.id = :compId"),
 @NamedQuery(name="progByRef", 
        query="select p from Programme p where p.refrence = :ref  and p.programmeForCompany.id = :compId"),
 @NamedQuery(name="progsByCompId",
    query="Select p from Programme p where p.programmeForCompany.id = :compId")
})
@SequenceGenerator(name = "programme_s1", sequenceName = "ma_mast03_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class Programme implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "costCentre_s1")
 @Column(name="programme_id")
 private Long id;
 @Column(name="reference")
 private String refrence;
 @Column(name="programme_name")
 private String name;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic programmeForCompany;
 @Column(name="cost")
 private double cost;
 @Column(name="programme_budget")
 private double budget;
 @ManyToOne
 @JoinColumn(name="resp_pers_id", referencedColumnName="partner_id")
 private PartnerPerson responsibilityOf;
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="valid_to")
 private Date validTo;
 
 @OneToMany(mappedBy = "prog")
 private List<AuditProgramme> auditRecords;
 
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

 
 @OneToMany(mappedBy = "program")
 private List<ProgramAccount> programAccounts;
 @OneToMany(mappedBy = "programme")
 private List<DocLineGl> glLines;


 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getRefrence() {
  return refrence;
 }

 public void setRefrence(String refrence) {
  this.refrence = refrence;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public CompanyBasic getProgrammeForCompany() {
  return programmeForCompany;
 }

 public void setProgrammeForCompany(CompanyBasic programmeForCompany) {
  this.programmeForCompany = programmeForCompany;
 }

 public double getCost() {
  return cost;
 }

 public void setCost(double cost) {
  this.cost = cost;
 }

 public double getBudget() {
  return budget;
 }

 public void setBudget(double budget) {
  this.budget = budget;
 }

 public PartnerPerson getResponsibilityOf() {
  return responsibilityOf;
 }

 public void setResponsibilityOf(PartnerPerson responsibilityOf) {
  this.responsibilityOf = responsibilityOf;
 }

 public Date getValidFrom() {
  return validFrom;
 }

 public void setValidFrom(Date validFrom) {
  this.validFrom = validFrom;
 }

 public Date getValidTo() {
  return validTo;
 }

 public void setValidTo(Date validTo) {
  this.validTo = validTo;
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

 public List<ProgramAccount> getProgramAccounts() {
  return programAccounts;
 }

 public void setProgramAccounts(List<ProgramAccount> programAccounts) {
  this.programAccounts = programAccounts;
 }

 public List<DocLineGl> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineGl> glLines) {
  this.glLines = glLines;
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
  if (!(object instanceof Programme)) {
   return false;
  }
  Programme other = (Programme) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.ma.Programme[ id=" + id + " ]";
 }
 
}
