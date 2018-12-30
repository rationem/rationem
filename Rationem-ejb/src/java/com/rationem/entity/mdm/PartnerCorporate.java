/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.fi.arap.ArAccount;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author Chris
 */
@Entity
@DiscriminatorValue("mdm.PartnerCorporate")
@Table(name="ptnr02")
@NamedQueries({

 @NamedQuery(name = "allcorpPtnrs",
query="select pc from PartnerCorporate pc" ),
 @NamedQuery(name = "corpPtnrsByTradeName",
query="select pc from PartnerCorporate pc where upper (pc.tradingName) like :trName" ),
 @NamedQuery(name = "corpPtnrByTradeName",
query="select pc from PartnerCorporate pc where upper (pc.tradingName) = :trName"),
 @NamedQuery(name = "corpPtnrByLegalName",
query="select pc from PartnerCorporate pc where  upper (pc.legalName) like :legName")
})

@PrimaryKeyJoinColumn(name="partner_id",referencedColumnName = "partner_id")

public class PartnerCorporate extends PartnerBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="legal_name")
  private String legalName;
  @Column(name="company_reg_num")
  private String companyNumber;
  @Column(name="charity_reg_num")
  private String charityNumber;
  @Column(name="charity")
  private boolean charity;
  @ManyToOne
  @JoinColumn(name="reg_office_address_id",referencedColumnName="address_id")
  private Address registeredOfficeAddress;
  @ManyToOne
  @JoinColumn(name="head_office_Aaddress_id",referencedColumnName="address_id")
  private Address headOfficeAddress;
  @Temporal(DATE)
  @Column(name="comp_reg_date")
  private Date compRegistrationDate;
  @Temporal(DATE)
  @Column(name="charity_reg_date")
  private Date charityRegistrationDate;
  @Temporal(DATE)
  @Column(name="accounts_filed_date")
  private Date accountsFilingDate;
 @OneToMany(mappedBy = "arAccountForCorporate")
 private List<ArAccount> arAccounts;

 public PartnerCorporate() {
 }

 public String getLegalName() {
  return legalName;
 }

 public void setLegalName(String legalName) {
  this.legalName = legalName;
 }

 

 public String getCompanyNumber() {
  return companyNumber;
 }

 public void setCompanyNumber(String companyNumber) {
  this.companyNumber = companyNumber;
 }

 public String getCharityNumber() {
  return charityNumber;
 }

 public void setCharityNumber(String charityNumber) {
  this.charityNumber = charityNumber;
 }

 public boolean isCharity() {
  return charity;
 }

 public void setCharity(boolean charity) {
  this.charity = charity;
 }

 public Address getRegisteredOfficeAddress() {
  return registeredOfficeAddress;
 }

 public void setRegisteredOfficeAddress(Address registeredOfficeAddress) {
  this.registeredOfficeAddress = registeredOfficeAddress;
 }

 public Address getHeadOfficeAddress() {
  return headOfficeAddress;
 }

 public void setHeadOfficeAddress(Address headOfficeAddress) {
  this.headOfficeAddress = headOfficeAddress;
 }

 public Date getCompRegistrationDate() {
  return compRegistrationDate;
 }

 public void setCompRegistrationDate(Date compRegistrationDate) {
  this.compRegistrationDate = compRegistrationDate;
 }

 public Date getCharityRegistrationDate() {
  return charityRegistrationDate;
 }

 public void setCharityRegistrationDate(Date charityRegistrationDate) {
  this.charityRegistrationDate = charityRegistrationDate;
 }

 public Date getAccountsFilingDate() {
  return accountsFilingDate;
 }

 public void setAccountsFilingDate(Date accountsFilingDate) {
  this.accountsFilingDate = accountsFilingDate;
 }
  
  
 

}
