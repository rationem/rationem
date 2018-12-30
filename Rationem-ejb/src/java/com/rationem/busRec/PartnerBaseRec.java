package com.rationem.busRec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PartnerBaseRec {
 private Long id;
 private String ref;
 //private UserRec createdBy;
 private Date createdDate;
 //private UserRec changedBy;
 private Date changedOn;
 private String vatNumber;
 private Date vatRegisteredDate;
 //private AddressRec defaultAddress;
 private Long revision;
 private String webAddress;
 private String tradingName;
 private String url;
 private String email;
 private String telephone;
 private String mobileTelephone;
 private String category;
 //private List<PartnerRoleRec> partnerRoles;
 
 public PartnerBaseRec() {
 }

 /*public PartnerBaseRec(UserRec createdBy, Date createdDate) {
  this.createdBy = createdBy;
  this.createdDate = createdDate;
}


public PartnerBaseRec(String ref, UserRec createdBy, Date createdDate, UserRec lastUpdateBy,
      Date lastUpdateDate, AddressRec defaultAddressID, long revision) {
  this.ref = ref;
  this.createdBy = createdBy;
  this.createdDate = createdDate;
  this.changedBy = lastUpdateBy;
  this.changedOn = lastUpdateDate;
  this.defaultAddress = defaultAddressID;
  this.revision = revision;
}

public PartnerBaseRec(Long id, String ref, UserRec createdBy, Date createdDate, UserRec lastUpdateBy,
      Date lastUpdateDate, String vatNumber, Date vatRegisteredDate, AddressRec defaultAddress,
      long revision) {
  this.id = id;
  this.ref = ref;
  this.createdBy = createdBy;
  this.createdDate = createdDate;
  this.changedBy = lastUpdateBy;
  this.changedOn = lastUpdateDate;
  this.vatNumber = vatNumber;
  this.vatRegisteredDate = vatRegisteredDate;
  this.defaultAddress = defaultAddress;
  this.revision = revision;
}

*/

public Long getId() {

  return id;
}

public void setId(Long id) {
  this.id = id;
}


/*
public UserRec getCreatedBy() {
  return createdBy;
}

public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
}
*/
public Date getCreatedDate() {
  return createdDate;
}

public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
}

/*
public AddressRec getDefaultAddress() {
  
  return defaultAddress; 
}

public void setDefaultAddress(AddressRec defaultAddressID) {
  this.defaultAddress = defaultAddressID;
}
*/
public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

/*
public UserRec getChangedBy() {
  return changedBy;
}

public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
}

*/
public Date getChangedOn() {
  return changedOn;
}

public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
}


public String getRef() {
  return ref;
}

public void setRef(String ref) {
  this.ref = ref;
}

public Long getRevision() {
  return revision;
}

public void setRevision(Long revision) {
  this.revision = revision;
}

public String getTradingName() {
return tradingName;
}

public void setTradingName(String tradingName) {
this.tradingName = tradingName;
}

public String getPtnrType(){

return this.getClass().getSimpleName();
}

/*
public List<PartnerRoleRec> getPartnerRoles() {
return partnerRoles;
}

public void setPartnerRoles(List<PartnerRoleRec> partnerRoles) {
this.partnerRoles = partnerRoles;
}

*/
public String getVatNumber() {
return vatNumber;
}

public void setVatNumber(String vatNumber) {
this.vatNumber = vatNumber;
}

public Date getVatRegisteredDate() {
return vatRegisteredDate;
}
/*
public ArrayList<ArAccountRec> getArAccounts() {
return arAccounts;
}

public void setArAccounts(ArrayList<ArAccountRec> arAccounts) {
this.arAccounts = arAccounts;
}
*/
public String getMobileTelephone() {
return mobileTelephone;
}

public String getName(){
return "Base rec";
}

public void setMobileTelephone(String mobileTelephone) {
this.mobileTelephone = mobileTelephone;
}

public String getTelephone() {
return telephone;
}

public void setTelephone(String telephone) {
this.telephone = telephone;
}

public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}

public String getWebAddress() {
return webAddress;
}

public void setWebAddress(String webAddress) {
this.webAddress = webAddress;
}



public void setVatRegisteredDate(Date vatRegisteredDate) {
  this.vatRegisteredDate = vatRegisteredDate;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}



}
