/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.mdm;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class AddressRec implements Serializable {
 private Long id;
 private String addrRef;
 private String houseNumber;
 private String houseName;
 
 private String building;
 private String street;
 private String streetLine;
 private String street2;
 private String town;
 private String countyName;
 private Long countyId;
 private String countryName;
 private Long countryIso;
 private CountryRec country;
 private String department;
 private String floor;
 private String room;
 private String poBox;
 private String postCode;
 private Date createdOn;
 private UserRec createdBy;
 private Date changedOn;
 private UserRec changedBy;
 private int revision;

 public AddressRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public String getAddrRef() {
  return addrRef;
 }

 public void setAddrRef(String addrRef) {
  this.addrRef = addrRef;
 }

 public String getHouseNumber() {
  return houseNumber;
 }

 public void setHouseNumber(String houseNumber) {
  this.houseNumber = houseNumber;
 }

 public String getHouseName() {
  return houseName;
 }

 public void setHouseName(String houseName) {
  this.houseName = houseName;
 }

 public String getBuilding() {
  return building;
 }

 public void setBuilding(String building) {
  this.building = building;
 }

 public String getStreet() {
  return street;
 }

 public void setStreet(String street) {
  this.street = street;
 }

 /**
     * returns the street address line with house number or name appended 
     * @return 
     */
 public String getStreetLine(){
     if(streetLine == null){
     streetLine = new String();
     StringBuilder streetBuff = new StringBuilder();
     String sep = " ";
     
     if(houseNumber != null && !houseNumber.isEmpty()){
      streetBuff.append(houseNumber);
      streetBuff.append(sep);
      
     }
     if(houseName != null ){
      streetBuff.append(houseName);
      streetBuff.append(sep);
     }
     if(street != null && !street.isEmpty()){
      streetBuff.append(street);
     }
     if(streetBuff.length() > 0){
      streetLine = streetBuff.toString();
     }
     }
     
     return streetLine;
    }
    
 public String getStreet2() {
  return street2;
 }

 public void setStreet2(String street2) {
  this.street2 = street2;
 }

 public String getTown() {
  return town;
 }

 public void setTown(String town) {
  this.town = town;
 }

 public CountryRec getCountry() {
  return country;
 }

 public void setCountry(CountryRec country) {
  this.country = country;
 }

 public String getCountyName() {
  return countyName;
 }

 public void setCountyName(String countyName) {
  this.countyName = countyName;
 }

 public Long getCountyId() {
  return countyId;
 }

 public void setCountyId(Long countyId) {
  this.countyId = countyId;
 }

 public String getCountryName() {
  return countryName;
 }

 public void setCountryName(String countryName) {
  this.countryName = countryName;
 }

 public Long getCountryIso() {
  return countryIso;
 }

 public void setCountryIso(Long countryIso) {
  this.countryIso = countryIso;
 }

 public String getDepartment() {
  return department;
 }

 public void setDepartment(String department) {
  this.department = department;
 }

 
 public String getFloor() {
  return floor;
 }

 public void setFloor(String floor) {
  this.floor = floor;
 }

 public String getRoom() {
  return room;
 }

 public void setRoom(String room) {
  this.room = room;
 }

 public String getPoBox() {
  return poBox;
 }

 public void setPoBox(String poBox) {
  this.poBox = poBox;
 }

 public String getPostCode() {
  return postCode;
 }

 public void setPostCode(String postCode) {
  this.postCode = postCode;
 }

 
 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
 }
 
 
}
