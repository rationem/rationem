/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.ma;

import com.rationem.entity.audit.AuditCostAccountDirect;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
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
@NamedQueries({
@NamedQuery(name="costAcsDirByCostCent", query="Select act from CostAccountDirect act "
        + "where act.costCentre.id = :ccId"),
@NamedQuery(name="costAcDirByCostCentGlAc", query="Select act from CostAccountDirect act "
       + "where act.costCentre.id = :costCentId and act.glAccount.id = :glAcId")
})
@Table(name="ma_mast02")
@SequenceGenerator(name = "costAccountDirect_s1", sequenceName = "ma_mast02_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class CostAccountDirect implements Serializable {
 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "costAccountDirect_s1")
 @Column(name="cost_accnt_id")
 private Long id;
 @OneToMany(mappedBy = "costAccount")
 private List<AuditCostAccountDirect> auditRecords;
 @Column(name="reference")
 private String ref;
 @Column(name="short_name")
 private String name;
 @Column(name="description")
 private String description;
 @ManyToOne
 @JoinColumn(name="resp_pers_id", referencedColumnName="partner_id")
 private PartnerPerson responsibilityOf;
 
 @ManyToOne
 @JoinColumn(name="gl_accnt_gl_id", referencedColumnName="fi_comp_gl_account_id")
 private FiGlAccountComp glAccount;

 @ManyToOne
 @JoinColumn(name="COST_CENT_ID", referencedColumnName="COST_CENT_ID")
 private CostCentre costCentre; 
 
 @OneToMany(mappedBy = "costAccountActual")
 private List<FiPeriodBalance> costBalances;
 @OneToMany(mappedBy = "costAccountBudget")
 private List<FiPeriodBalance> budgetBalances;
 @OneToMany(mappedBy = "costAccount")
 private List<DocLineGl> costActDocLines;
 @Temporal(DATE)
 @Column(name="valid_from")
 private Date validFrom;
 @Temporal(DATE)
 @Column(name="valid_to")
 private Date validTo;
 
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

 public List<AuditCostAccountDirect> getAuditRecords() {
  return auditRecords;
 }

 public void setAuditRecords(List<AuditCostAccountDirect> auditRecords) {
  this.auditRecords = auditRecords;
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

 public List<FiPeriodBalance> getCostBalances() {
  return costBalances;
 }

 public void setCostBalances(List<FiPeriodBalance> costBalances) {
  this.costBalances = costBalances;
 }

 public CostCentre getCostCentre() {
  return costCentre;
 }

 public void setCostCentre(CostCentre costCentre) {
  this.costCentre = costCentre;
 }

 public List<FiPeriodBalance> getBudgetBalances() {
  return budgetBalances;
 }

 public void setBudgetBalances(List<FiPeriodBalance> budgetBalances) {
  this.budgetBalances = budgetBalances;
 }

 public List<DocLineGl> getCostActDocLines() {
  return costActDocLines;
 }

 public void setCostActDocLines(List<DocLineGl> costActDocLines) {
  this.costActDocLines = costActDocLines;
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
 
public FiPeriodBalance getActualBalance(int year, int period) throws BacException{
  ListIterator<FiPeriodBalance> balListLi = this.getCostBalances().listIterator();
  while(balListLi.hasNext()){
   FiPeriodBalance bal = balListLi.next();
   if(bal.getBalPeriod() == period && bal.getBalYear() == year){
    return bal;
   }
  }
  throw new BacException("No cost ac balance");
  
 } 
 
 public void addActualBalance(FiPeriodBalance newBal){
  List<FiPeriodBalance> balList = getCostBalances();
  balList.add(newBal);
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
  if (!(object instanceof CostAccountDirect)) {
   return false;
  }
  CostAccountDirect other = (CostAccountDirect) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.ma.CostAccountDirect[ id=" + id + " ]";
 }
 
}
