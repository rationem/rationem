/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.ma;

//import com.rationem.entity.audit.AuditCostCentre;
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
@Table(name="ma_mast01")
@NamedQueries({
 @NamedQuery(name="allCostCent", query="select cc from CostCentre cc where cc.costCentreForCompany.id = :compId"),
 @NamedQuery(name="costCentsByRef", 
        query="select cc from CostCentre cc where cc.refrence like :ref  and cc.costCentreForCompany.id = :compId"),
 @NamedQuery(name="costCentByRef", 
        query="select cc from CostCentre cc where cc.refrence = :ref  and cc.costCentreForCompany.id = :compId"),
 @NamedQuery(name="costCentByCompId",
    query="Select cc from CostCentre cc where cc.costCentreForCompany.id = :compId")
})
@SequenceGenerator(name = "costCentre_s1", sequenceName = "ma_mast01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class CostCentre implements Serializable {

 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "costCentre_s1")
 @Column(name="cost_cent_id")
 private Long id;

 @OneToMany(mappedBy = "costCentre")
 private List<CostAccountDirect> costAccountDirectAcs;
 @OneToMany(mappedBy = "costCentre")
 private List<DocLineGl> glLines;
 /*
 @OneToMany(mappedBy = "costCentre")
 private List<AuditCostCentre> auditCostCentres;
 */
 @Column(name="reference")
 private String refrence;
 @Column(name="cist_centre_name")
 private String costCentreName;
 @ManyToOne
 @JoinColumn(name="comp_id", referencedColumnName="home_comp_id")
 private CompanyBasic costCentreForCompany;
 @ManyToOne
 @JoinColumn(name="resp_pers_id", referencedColumnName="partner_id")
 private PartnerPerson responsibilityOf;
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

 public List<CostAccountDirect> getCostAccountDirectAcs() {
  return costAccountDirectAcs;
 }

 public void setCostAccountDirectAcs(List<CostAccountDirect> costAccountDirectAcs) {
  this.costAccountDirectAcs = costAccountDirectAcs;
 }

 public List<DocLineGl> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineGl> glLines) {
  this.glLines = glLines;
 }

 public String getRefrence() {
  return refrence;
 }

 public void setRefrence(String refrence) {
  this.refrence = refrence;
 }

 public String getCostCentreName() {
  return costCentreName;
 }

 public void setCostCentreName(String costCentreName) {
  this.costCentreName = costCentreName;
 }

 public CompanyBasic getCostCentreForCompany() {
  return costCentreForCompany;
 }

 public void setCostCentreForCompany(CompanyBasic costCentreForCompany) {
  this.costCentreForCompany = costCentreForCompany;
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

 
 @Override
 public int hashCode() {
  int hash = 0;
  hash += (id != null ? id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object object) {
  // TODO: Warning - this method won't work in the case the id fields are not set
  if (!(object instanceof CostCentre)) {
   return false;
  }
  CostCentre other = (CostCentre) object;
  return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
 }

 @Override
 public String toString() {
  return "com.rationem.entity.ma.CostCentre[ id=" + id + " ]";
 }
 
}
