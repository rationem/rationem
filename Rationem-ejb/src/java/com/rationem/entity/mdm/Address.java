/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.mdm;

import com.rationem.entity.user.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.DiscriminatorType.STRING;
import org.apache.commons.lang3.StringUtils;



/**
 *
 * @author Chris
 */
@Table(name="mdm01" )
@Entity
@NamedQueries({
@NamedQuery(name="addessesByPostCode", query="select a from Address a where upper(a.postCode) like  :pstCd"),
@NamedQuery(name="addressByStreetPostCode", 
        query="select a from Address a where a.postCode = :pstCd and a.street = :str "),
@NamedQuery(name="addressUnique", query="select a from Address a where a.houseNum = :hseNum "
  + "and a.houseName = :hseName and a.street = :street and a.postCode = :postCd"),
@NamedQuery(name="allAddresses", query="select distinct a from Address a")        
})
@SequenceGenerator(name = "address_s1", sequenceName = "mdm_addr01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50,contextProperty="tenantId")
public class Address implements Serializable {

 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(generator = "address_s1")
 @Column(name="address_id")
 private Long id;
 @Column(name="addr_ref")
 private String addrRef;
 @Column(name="house_num", length=10)
 private String houseNum; 
 @Column(name="house_name")
 private String houseName;
 @ManyToOne
 @JoinColumn(name="care_of_id", referencedColumnName="partner_id")
 private PartnerPerson careOf;
 @OneToMany(mappedBy = "headOfficeAddress")
 private List<PartnerPerson> hoPartnerPersons;
 
 @Column(name="building")
 private String building;
 @Column(name="street")
 private String street;
 @Column(name="street2")
 private String street2;
 @Column(name="town")
 private String town;
 @Column(name="county_name")
 private String countyName;
 @Column(name="county_id")
 private Long countyId;
 @Column(name="country_name")
 private String countryName;
 @Column(name="country_iso")
 private Long countryIso;
 @ManyToOne
 @JoinColumn(name="country_id", referencedColumnName="country_id")
 private Country country;
 @Column(name="floor")
 private String floor;
 @Column(name="room")
 private String room;
 @Column(name="dept")
 private String department;
 @Column(name="po_box")
 private String poBox;
 @Column(name="post_code")
 private String postCode;
 @Temporal(TIMESTAMP)
 @Column(name="created_on")
 private Date createdDate;
 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
 @Column(name="changed_on")
 @Temporal(TIMESTAMP)
 private Date updateDate;
 @ManyToOne
 @JoinColumn(name="changed_by_id", referencedColumnName="partner_id")
 private User updateBy;
 @Version
 @Column(name="revision")
 private int revision;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
  
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }

 public User getUpdateBy() {
  return updateBy;
 }

 public void setUpdateBy(User updateBy) {
  this.updateBy = updateBy;
 }

 public String getAddrRef() {
  return addrRef;
 }

 public void setAddrRef(String addrRef) {
  this.addrRef = addrRef;
 }

 public PartnerPerson getCareOf() {
  return careOf;
 }

 public void setCareOf(PartnerPerson careOf) {
  this.careOf = careOf;
 }

 public String getHouseNum() {
  return houseNum;
 }

 public void setHouseNum(String houseNum) {
  this.houseNum = houseNum;
 }

 public List<PartnerPerson> getHoPartnerPersons() {
  return hoPartnerPersons;
 }

 public void setHoPartnerPersons(List<PartnerPerson> hoPartnerPersons) {
  this.hoPartnerPersons = hoPartnerPersons;
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

 public String getStreet2() {
  return street2;
 }

 public void setStreet2(String street2) {
  this.street2 = street2;
 }

 public String getStreetFormatted(){
  StringBuilder streetFrmt = new StringBuilder();
  if(!StringUtils.isBlank(this.houseNum)){
   streetFrmt.append(this.houseNum);
  }else{
   streetFrmt.append(this.houseName);
  }
  streetFrmt.append(" ");
  streetFrmt.append(street);
     
  return streetFrmt.toString();
 }
 public String getTown() {
  return town;
 }

 public void setTown(String town) {
  this.town = town;
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

 public Country getCountry() {
  return country;
 }

 public void setCountry(Country country) {
  this.country = country;
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

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public int getRevision() {
  return revision;
 }

 public void setRevision(int revision) {
  this.revision = revision;
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
  if (!(object instanceof Address)) {
   return false;
  }
  Address other = (Address) object;
  if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "com.rationem.entity.mdm.Address[ id=" + id + " ]";
 }
 
}
