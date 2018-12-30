/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.fi.arap.ArAccount;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;



import static javax.persistence.TemporalType.DATE;
import javax.persistence.OneToMany;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("mdm.PartnerPerson")
@Table(name="ptnr03")
@NamedQueries({
  @NamedQuery(name = "persPtnrsBySurnameName",
    query="select o from PartnerPerson o where upper(o.familyName) like :surName" ),
//  @NamedQuery(name = "persPtnrsByRole",
//    query="select o from PartnerPerson o where o.partnerRoles in :roles" ),
  @NamedQuery(name = "allPersPtnrs",
  query="select o from PartnerPerson o"),
  
        })
@PrimaryKeyJoinColumn(name="partner_id",referencedColumnName = "partner_id")

public class PartnerPerson extends PartnerBase {
 @Column(name="salutation")
 private String title;
 @Column(name="first_name")
 private String firstName;
 @Column(name="middle_name")
 private String middleName;
 @Column(name="family_name")
 private String familyName;
 @Temporal(DATE)
 @Column(name="date_of_birth")
 private Date dateOfBirth;
 
 @ManyToOne
 @JoinColumn(name="ho_addr_id", referencedColumnName="address_id")
 private Address headOfficeAddress;
 
 @OneToMany(mappedBy = "orderBy")
 private List<DocLineAp> docLinApOrders;
 @OneToMany(mappedBy = "arAccountForPerson")
 private List<ArAccount> arAccounts;

 public PartnerPerson() {
 }
 
 

 public String getFirstName() {
  return firstName;
 }

 public void setFirstName(String firstName) {
  this.firstName = firstName;
 }

 public Address getHeadOfficeAddress() {
  return headOfficeAddress;
 }

 public void setHeadOfficeAddress(Address headOfficeAddress) {
  this.headOfficeAddress = headOfficeAddress;
 }
 
 

 public String getMiddleName() {
  return middleName;
 }

 public void setMiddleName(String middleName) {
  this.middleName = middleName;
 }

 public String getFamilyName() {
  return familyName;
 }

 public void setFamilyName(String familyName) {
  this.familyName = familyName;
 }
public String getNameStructured(){
  StringBuilder sb = new StringBuilder();
  if(title != null){
   sb.append(title);
   sb.append(" ");
  }
  
  if(familyName != null){
   sb.append(familyName);
   sb.append(" ");
  } 
  
  if(firstName != null){
   sb.append(firstName);
  }
  
  return sb.toString();
 }

 
 public List<DocLineAp> getDocLinApOrders() {
  return docLinApOrders;
 }

 public void setDocLinApOrders(List<DocLineAp> docLinApOrders) {
  this.docLinApOrders = docLinApOrders;
 }

 public Date getDateOfBirth() {
  return dateOfBirth;
 }

 public void setDateOfBirth(Date dateOfBirth) {
  this.dateOfBirth = dateOfBirth;
 }

 public String getTitle() {
  return title;
 }

 public void setTitle(String title) {
  this.title = title;
 }
 
 
 
 
}
