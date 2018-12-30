/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.partner;

import com.rationem.busRec.mdm.AddressRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class PartnerCorporateRec extends PartnerBaseRec {
  private String legalName;
  private String companyNumber;
  private String charityNumber;
  private boolean charity;
  private AddressRec registeredOfficeAddress;
  private AddressRec headOfficeAddress;
  private Date compRegistrationDate;
  private Date charityRegistrationDate;
  private Date accountsFilingDate;


    public PartnerCorporateRec() {
        super();
    }

  public Date getAccountsFilingDate() {
    return accountsFilingDate;
  }

  public void setAccountsFilingDate(Date accountsFilingDate) {
    this.accountsFilingDate = accountsFilingDate;
  }

  public String getCharityNumber() {
    return charityNumber;
  }

  public void setCharityNumber(String charityNumber) {
    this.charityNumber = charityNumber;
  }

  public Date getCharityRegistrationDate() {
    return charityRegistrationDate;
  }

  public void setCharityRegistrationDate(Date charityRegistrationDate) {
    this.charityRegistrationDate = charityRegistrationDate;
  }

  public Date getCompRegistrationDate() {
    return compRegistrationDate;
  }

  public void setCompRegistrationDate(Date compRegistrationDate) {
    this.compRegistrationDate = compRegistrationDate;
  }

  public String getCompanyNumber() {
    return companyNumber;
  }

  public void setCompanyNumber(String companyNumber) {
    this.companyNumber = companyNumber;
  }

  public AddressRec getHeadOfficeAddress() {
    return headOfficeAddress;
  }

  public void setHeadOfficeAddress(AddressRec headOfficeAddress) {
    this.headOfficeAddress = headOfficeAddress;
  }

  public boolean isCharity() {
    return charity;
  }

  public void setCharity(boolean charity) {
    this.charity = charity;
  }

  public String getLegalName() {
    return legalName;
  }

  public void setLegalName(String legalName) {
    this.legalName = legalName;
  }

  public AddressRec getRegisteredOfficeAddress() {
    return registeredOfficeAddress;
  }

  public void setRegisteredOfficeAddress(AddressRec registeredOfficeAddress) {
    this.registeredOfficeAddress = registeredOfficeAddress;
  }

  
@Override
  public String getName(){
   return this.getTradingName();
  }
    




}
