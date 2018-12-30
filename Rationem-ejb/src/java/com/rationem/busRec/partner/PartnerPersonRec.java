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
public class PartnerPersonRec extends PartnerBaseRec{
    private String title;
    private String firstName;
    private String middleName;
    private String familyName;
    private Date dateOfBirth;
    private AddressRec headOfficeAddress;

    public PartnerPersonRec(String firstName, String middleName, String familyName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.familyName = familyName;
    }

    public PartnerPersonRec(int createdBy, Date createdDate, String firstName, String middleName, String familyName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.familyName = familyName;
    }

    public PartnerPersonRec() {
    }

    

    public PartnerPersonRec(String ref, int createdBy, Date createdDate, int lastUpdateBy, Date lastUpdateDate, long defaultAddressID, long revision, String firstName, String middleName, String familyName, Date dateOfBirth) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

 public AddressRec getHeadOfficeAddress() {
  return headOfficeAddress;
 }

 public void setHeadOfficeAddress(AddressRec headOfficeAddress) {
  this.headOfficeAddress = headOfficeAddress;
 }

    
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

 public String getTitle() {
  return title;
 }

 public void setTitle(String title) {
  this.title = title;
 }
    
    
 @Override
 public String getName(){
  return familyName;
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

}
