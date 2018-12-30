/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.ma;

import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
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
@Table(name="MA_MAST04")
@NamedQueries({
@NamedQuery(name="progActByProgGlAct", 
  query="Select p from ProgramAccount p where p.glAccount.id = :glActId and p.program.id = :progId")

})
@SequenceGenerator(name = "programAccount_s1", sequenceName = "ma_mast01_s1", 
        allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,
        contextProperty="tenantId")
public class ProgramAccount implements Serializable {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "programAccount_s1")
 @Column(name="PROG_ACCNT_ID")
 private Long id;
 @Column(name="REFERENCE")
 private String ref;
 @Column(name="NAME_SHORT")
 private String name;
 @Column(name="NAME_FULL")
 private String description;
 @ManyToOne
 @JoinColumn(name="RESP_PERS_ID", referencedColumnName="PARTNER_ID")
 private PartnerPerson responsibilityOf;
 @ManyToOne
 @JoinColumn(name="GL_ACCNT_COMP_ID", referencedColumnName="FI_COMP_GL_ACCOUNT_ID")
 private FiGlAccountComp glAccount;
 @ManyToOne
 @JoinColumn(name="PROGRAMME_ID", referencedColumnName="PROGRAMME_ID")
 private Programme program;
 @Temporal(DATE)
 @Column(name="VALID_FROM")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="VALID_TO")
 private Date validTo;
 @ManyToOne
 @JoinColumn(name="CREATED_BY_ID", referencedColumnName="PARTNER_ID")
 private User createdBy;
 @Temporal(TIMESTAMP)
 @Column(name="CREATED_ON")
 private Date createdOn;
 @ManyToOne
 @JoinColumn(name="CHANGED_BY_ID", referencedColumnName="PARTNER_ID")
 private User changedBy;
 @Temporal(TIMESTAMP)
 @Column(name="CHANGED_ON")
 private Date changedOn;
 @Version
 @Column(name="REVISION")
 private int changes;
 
 @OneToMany(mappedBy = "programCostAccount")
 private List<DocLineGl> docLinesProgCost;
 
 @OneToMany(mappedBy = "programCostAccount")
 private List<FiPeriodBalance> programmeCostBalances;
 
 @OneToMany(mappedBy = "programBudgetAccount")
 private List<FiPeriodBalance> progBudgetBalances;
 

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
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

 public PartnerPerson getResponsibilityOf() {
  return responsibilityOf;
 }

 public void setResponsibilityOf(PartnerPerson responsibilityOf) {
  this.responsibilityOf = responsibilityOf;
 }

 public FiGlAccountComp getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountComp glAccount) {
  this.glAccount = glAccount;
 }

 public Programme getProgram() {
  return program;
 }

 public void setProgram(Programme program) {
  this.program = program;
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

 public List<DocLineGl> getDocLinesProgCost() {
  return docLinesProgCost;
 }

 public void setDocLinesProgCost(List<DocLineGl> docLinesProgCost) {
  this.docLinesProgCost = docLinesProgCost;
 }

 public List<FiPeriodBalance> getProgrammeCostBalances() {
  return programmeCostBalances;
 }

 public void setProgrammeCostBalances(List<FiPeriodBalance> programmeCostBalances) {
  this.programmeCostBalances = programmeCostBalances;
 }

 public List<FiPeriodBalance> getProgBudgetBalances() {
  return progBudgetBalances;
 }

 public void setProgBudgetBalances(List<FiPeriodBalance> progBudgetBalances) {
  this.progBudgetBalances = progBudgetBalances;
 }

 public void addProgrammeCostBalance(FiPeriodBalance bal){
  List balList = getProgrammeCostBalances();
  balList.add(bal);
  
 }
 public FiPeriodBalance getProgrammeCostBalance(int year, int period) throws BacException{
  FiPeriodBalance bal;
  ListIterator<FiPeriodBalance> balLi = getProgrammeCostBalances().listIterator();
  while(balLi.hasNext()){
   bal = balLi.next();
   if(bal.getBalPeriod() == period && bal.getBalYear() == year){
    return bal;
   }
  }
  throw new BacException("Program Account not found");
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
  if (!(object instanceof ProgramAccount)) {
   return false;
  }
  ProgramAccount other = (ProgramAccount) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.ma.ProgramAccount[ id=" + id + " ]";
 }
 
}
