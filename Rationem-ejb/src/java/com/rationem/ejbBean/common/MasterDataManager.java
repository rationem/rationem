/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.common;

import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import javax.ejb.TransactionRolledbackLocalException;
import com.rationem.ejbBean.dataManager.TreasuryDM;
import com.rationem.busRec.tr.BankBranchRec;
import java.util.ListIterator;
import com.rationem.busRec.partner.PartnerBaseRec;
import java.util.ArrayList;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.ejbBean.dataManager.MasterDataDM;
import javax.ejb.EJB;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.entity.mdm.Address;
import com.rationem.exception.BacException;
import com.rationem.helper.PartnerSelectOption;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;
import java.util.List;



import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;

/**
 * Implements address management rules
 * @author Chris
 */
@Stateless
@LocalBean
public class MasterDataManager {
 private static final Logger LOGGER = Logger.getLogger(MasterDataManager.class.getName());

 @EJB
 private MasterDataDM masterDataDM;

 @EJB
 private TreasuryDM bankDM;


 public CountryRec countryUpdate(CountryRec rec, UserRec usr, String pg){
     rec = masterDataDM.countryUpdate(rec, usr, pg);
     LOGGER.log(INFO, "masterDataDM returned country {0}", rec);
     return rec;
    }
    public AddressRec addressUpdate(AddressRec addr,UserRec usr, String source){
     LOGGER.log(INFO, "MasterDataMgr.addressUpdate called with address {0} user {1} source {2}", 
             new Object[]{addr,usr,source});
     if(addr.getId() == null){
      addr.setCreatedBy(usr);
      addr.setCreatedOn(new Date());
     }else{
      addr.setChangedBy(usr);
      addr.setChangedOn(new Date());
     }
     
     addr = masterDataDM.addressUpdate(addr, usr, source);
     return addr;
    }
    public AddressRec createAddress(AddressRec rec,UserRec usr, String source) throws BacException {
     LOGGER.log(INFO, "MasterDataMgr.createAddress called with address {0} user {1} source {2}", 
             new Object[]{rec,usr,source});
     rec.setCreatedBy(usr);
     rec.setCreatedOn(new Date());
     rec = masterDataDM.createAddress(rec, usr, source);
     LOGGER.log(INFO, "Address ID in Mgr after call to DB layer {1}", rec.getId());
     return rec;
     
    }



    public Address getAddressDB(AddressRec rec) throws BacException {

        return null;
    }

    public Address createAddressPvt(AddressRec rec, UserRec usr, String pg) throws BacException {
        LOGGER.log(INFO, "master Data Manager createAddressPvt called with: {0}", rec);
        try{
            Address addr = this.masterDataDM.createAddressPvt(rec,pg);
            LOGGER.log(INFO, "After DB action address is: {0}", addr);
            return addr;
        }catch(BacException e){
            throw new BacException(e.getLocalizedMessage());
        }

        
    }

  
    
  public List<PartnerCorporateRec> getCorpPartnersByTradingName(String tradingName)
          throws BacException {
    LOGGER.log(INFO, "MasterDataManager getCorpPartnersByTradingName called with {0}", tradingName);
    List<PartnerCorporateRec> list = masterDataDM.getCorpPartnersByTradingName(tradingName);

    LOGGER.log(INFO, "returned by DB {0}", list);

    return list;
  }
  
  public List<PartnerCorporateRec> getCorpPartnersByTradingName(String tradingName, boolean withRoles){
   LOGGER.log(INFO, "MasterDataManager getCorpPartnersByTradingName with roles {0}",withRoles);
   List<PartnerCorporateRec> list = masterDataDM.getCorpPartnersByTradingName(tradingName,withRoles);
   LOGGER.log(INFO, "returned by DB {0}", list);
   return list;
  }
  
  public List<CountryRec> getCountriesAll(){
   return this.masterDataDM.getCountriesAll();
  }
  
  public List<CountryRec> getCountriesByName(String input){
   return masterDataDM.getCountriesByName(input);
  }
  
  public CountryRec getCountryByRef2(String ref)throws BacException{
   CountryRec rec = masterDataDM.getCountryByRefAlpha2(ref);
   return rec;
  }
  
  public CountryRec getCountryByUsr(UserRec usr){
   CountryRec rec = masterDataDM.getCountryByUser(usr);
   LOGGER.log(INFO, "getCountryByUsr returns {0}", rec);
   return rec;
  }
  
  
  public CurrencyRec getCurrencyByCode(String currAlpha){
   
   return masterDataDM.getCurrencyByCode(currAlpha);
  }
  
  public PartnerBaseRec getPartnerById(Long id){
   
   PartnerBaseRec ptnrBase = masterDataDM.getPartnerById(id);
   return ptnrBase;
  }
  public List<PartnerBaseRec> getPartnerByRole(PartnerRoleRec rl){
   
   List<PartnerBaseRec> ptnrs = masterDataDM.getPartnersByRole(rl);
   if(ptnrs == null){
    return null;
   }
   
   return ptnrs;
  }
  
/**
 * used find Partner by role restricted by name
 * @param name
 * @param rl role
 * @return
 * @throws BacException
 */
  public List<PartnerBaseRec> getPartnerByRoleAndTradingName(PartnerRoleRec rl, 
    String name) throws BacException {
    LOGGER.log(INFO, "getBankOrganisationByTradingName called with");
    List<PartnerBaseRec> retList = this.masterDataDM.getPartnersByNameForRole(rl, name);

    return retList;
  }

  public BankBranchRec getbankBranchBySortCode(String sortCode) throws BacException {
    LOGGER.log(INFO, "Master Data Mgr getbankBranchBySortCode called with {0}", sortCode);
    try{
      BankBranchRec rec = this.bankDM.getBranchBySortCode(sortCode);
      if(rec == null){
        return null;
      }
      return rec;
    }catch(BacException e){
      if(e.getErrorCode().equalsIgnoreCase("TRBR:09")){
        throw new BacException("Bank Branch not found ","TRBR:10");
      }else{
        throw new BacException("Bank Branch Database error ","TRBR:11");
      }

    }catch(TransactionRolledbackLocalException e){
      throw new BacException("DB exception "+e.getLocalizedMessage());

    }

  }

  public Long createCorporatePartnerAR(PartnerCorporateRec partner, UserRec usr, String pg) throws BacException {
    LOGGER.log(INFO, "MasterData Mgr createCorporatePartnerAR called with corp rec {0} user id{1}",
            new Object[]{partner,partner.getCreatedBy().getId()});
    
    Long ptnrId = this.masterDataDM.createCorporatePartnerAR(partner,usr,pg);
    //logger.log(INFO, "MasterData Mgr createCorporatePartnerAR returned corp id {0}",ptnrId);
    return 0l;
  }

  public PartnerCorporateRec createCorporatePtnrSubLed(PartnerCorporateRec partner, UserRec usr, String view)
          throws BacException {
    LOGGER.log(INFO, "MasterData Mgr createCorporatePartnerAR called with corp rec {0} user {1}",
            new Object[]{partner,usr});
    partner = masterDataDM.createCorporatePtnrSubLed(partner, usr,view);
    LOGGER.log(INFO, "MasterData Mgr createCorporatePartnerAR returned corp id {0}",partner.getId());
    return partner;
  }
  
  public Long createIndivPartnerAR(PartnerPersonRec partner, UserRec usr, String source) throws BacException {
    LOGGER.log(INFO, "MasterData Mgr createIndivPartnerAR called with indiv rec {0} user {1}",
            new Object[]{partner,usr});
    Long ptnrId = this.masterDataDM.createIndividualPartnerAR(partner, usr, source);
    LOGGER.log(INFO, "MasterData Mgr createCorporatePartnerAR returned corp id {0}",ptnrId);
    return ptnrId;
    
  }
  
  public PartnerBaseRec updatePartner(PartnerBaseRec ptnr, String pg){
   LOGGER.log(INFO, "updatePartner called with ptnr {0}", ptnr);
   LOGGER.log(INFO,"Partner class {0}", ptnr.getClass().getSimpleName());
   ptnr = masterDataDM.updatePartner(ptnr, pg);
   LOGGER.log(INFO, "masterDataDM return partner id {0}", ptnr.getId());
   return ptnr;
  }
  
  public PartnerRoleRec updatePartnerRole(PartnerRoleRec role,  String pg){
   LOGGER.log(INFO, "updatePartner called with ptnr role {0} ", role.getRoleCode());
   LOGGER.log(INFO, "update by {0} create by {1} ", new Object[]{role.getChangedBy(),role.getCreatedBy()});
   role = masterDataDM.updatePartnerRole(role,  pg);
   LOGGER.log(INFO, "masterDataDM return partner id {0}", role.getId());
   return role;
  }
  
  public boolean userDefaultCountryUpdate(String countryCode, UserRec procUser, String pg){
   return masterDataDM.userDefaultCountryUpdate(countryCode, procUser, pg);
   
  }
  
  public List<PartnerPersonRec> getIndivPtnrsBySurname(String surname)
          throws BacException {
    LOGGER.log(INFO, "masterdata Mgr getIndivPtnrsBySurnameForeName called with surname {0} ",
            surname);

    List<PartnerPersonRec> personList = masterDataDM.getIndivPtnrBySurname(surname);
    LOGGER.log(INFO, "Person list returned from DM {0}", personList);

    return personList;
  }


    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
/**
 * Gets address based on an input post code
 * @param postCode
 * @return
 * @throws BacException 
 */
 public List<AddressRec> getAddressesForPostCodePart(String postCode) throws BacException {
  LOGGER.log(INFO, "Master Data manager getAddressesForPostCode called with {0}", postCode);
  String searchPostCode;
  if(postCode == null){
   searchPostCode = new String();
  }else{
   searchPostCode = postCode.toUpperCase();
  }
  searchPostCode = searchPostCode.replace("%","");
  searchPostCode = searchPostCode.replace(" ", "%");
  
  searchPostCode = searchPostCode+"%";
  LOGGER.log(INFO, "searchPostCode {0}", searchPostCode);
  List<AddressRec> addressList = masterDataDM.getAddressesByPostCode(searchPostCode);
  if(addressList == null){
   return null;
  }
  ListIterator li = addressList.listIterator();
  while(li.hasNext()){
   AddressRec addr = (AddressRec)li.next();
   LOGGER.log(INFO, "Addr post code {0}", addr.getPostCode());
   if(addr.getPostCode() == null){
    li.remove();
   }else if(addr.getPostCode().isEmpty()){
    li.remove();
   }
  }
  LOGGER.log(INFO, "addressList returned by DB layer {0}", addressList);
  
  return addressList;
 }

 public boolean checkAddressUnique(AddressRec addr){
  LOGGER.log(INFO, "checkAddressExists called");
  boolean rc = masterDataDM.isAddressUnique(addr);
  LOGGER.log(INFO, "DB layer returns {0}", rc);
  return rc;
 }
 public AddressRec getAddressByStreetPostCode(String street, String postCode){
  
  AddressRec addrRec = masterDataDM.getAddressByStreetPostCode(street, postCode);
  LOGGER.log(INFO, "Address returned by masterDataDM {0}", addrRec);
  return addrRec;
 }
 /**
  * Obtain all address from DB layer
  * @return
  * @throws BacException 
  */
 public List<AddressRec> getAllAddresses() throws BacException {
  LOGGER.log(INFO, "MasterDataManager getAllAddresses");
  List<AddressRec> addressList = this.masterDataDM.getAllAddresses();
  ListIterator li = addressList.listIterator();
  while(li.hasNext()){
   AddressRec addr = (AddressRec)li.next();
   LOGGER.log(INFO, "Addr post code {0}", addr.getPostCode());
   if(addr.getPostCode() == null){
    li.remove();
   }else if(addr.getPostCode().isEmpty()){
    li.remove();
   }
  }
  LOGGER.log(INFO, "addressList returned by DB layer {0}", addressList);
  return addressList;
 }

 public NumberRangeRec getNumberRangeNextVal(NumberRangeRec nr){
  LOGGER.log(INFO, "getNumberRangeNextVal called with numRange name {0} next num {1}", 
    new Object[]{nr.getShortDescr(),nr.getNextNum()});
  nr = MasterDataDM.getNumberRangeNextVal(nr);
  LOGGER.log(INFO, "Num Range next num is now {{0}", nr.getNextNum());
  return nr;
 }
 public List<PartnerPersonRec> getAllPartnerIndividual() throws BacException {
  List<PartnerPersonRec> persList = this.masterDataDM.getAllPersonPartners();
  LOGGER.log(INFO, "MasterDataMgr getAllPartnerIndividual returns {0}", persList);
  return persList;
 }

 public PartnerRoleRec getPartnerRoleByCode(String ptnrCd){
  
  PartnerRoleRec rec = masterDataDM.getPartnerRoleByCode(ptnrCd);
  if(rec != null){
   LOGGER.log(INFO, "Role rec returned from masterDataDM role with code {0}", rec.getRoleCode());
  }
  return rec;
 }
 
 public List<PartnerRoleRec> getPartnerRoles(){
  
  List<PartnerRoleRec> retList = masterDataDM.getPartnerRoles();
  LOGGER.log(INFO, "Roles returned from DB layer {0}", retList);
  
  return retList;
 }
 
 public PartnerBaseRec getRolesForPtnr(PartnerBaseRec ptnr ){
   ptnr = this.masterDataDM.getRolesForPtnr(ptnr);
   return ptnr;
 }
 
 public PartnerBaseRec getPartnerAddress(PartnerBaseRec rec){
  rec = this.masterDataDM.getPartnerAddresses(rec);
  
  
  
  return rec;
 }
 
 
 public List<PartnerBaseRec> getPartnersByName(String name){
  List<PartnerBaseRec> ptnrlst = masterDataDM.getPtnrsByTrName(name);
  return ptnrlst;
 }
 
 public List<PartnerCorporateRec> getPartnersByNameLegal(String name){
  List<PartnerCorporateRec> ptnrList = masterDataDM.getPtnrByLegalName(name);
  LOGGER.log(INFO, "ptnrList from DB layer {0}", ptnrList);
  return ptnrList;
 }
 
 public List<PartnerPersonRec> getPersonPartnersByName(String name){
  List<PartnerPersonRec> ptnrlst = this.masterDataDM.getPersonPtnrByFamName(name);
  return ptnrlst;
 }
 
 public List<PartnerBaseRec> getPartnersByRef(String searchRef){
  
  if(StringUtils.isBlank(searchRef)){
   searchRef = searchRef + "%";
  }else{
   searchRef = StringUtils.remove(searchRef, "%");
   searchRef = StringUtils.appendIfMissing(searchRef, "%");
  }
  
  LOGGER.log(INFO, "getPartnersByRef called with {0}", searchRef);
  List<PartnerBaseRec> ptnrlst = this.masterDataDM.getPartnersByRef(searchRef);
  return ptnrlst;
 }
 
 public List<PartnerBaseRec> getPartnersBySelOpt(PartnerSelectOption opt){
  LOGGER.log(INFO, "getPartnersBySelOpt called");
  List<PartnerBaseRec> ptnrlst = new ArrayList<>();
  String options;
  LOGGER.log(INFO, "options cat {0} name {1} type {2} ref {3} ", 
          new Object[]{opt.getCategory(),opt.getName(),opt.getPtnrType(),opt.getReference()});
  if(StringUtils.isBlank(opt.getCategory() ) 
    && (StringUtils.isBlank(opt.getName()))
    && (StringUtils.isBlank(opt.getReference()))
    &&( StringUtils.isBlank(opt.getPtnrType()) || opt.getPtnrType().equalsIgnoreCase("noSelect"))
    && (StringUtils.isBlank(opt.getPostCode()) )){
   LOGGER.log(INFO, "Call getPartnersAll");
   ptnrlst = this.getPartnersAll();
   LOGGER.log(INFO, "All ptnrs {0}", ptnrlst);
   return ptnrlst;
   
  }else{
   // 1 = cat, 4 = type 
   if(StringUtils.isNotBlank(opt.getReference())){
    ptnrlst = masterDataDM.getPartnersByRef(opt.getReference());
   }else if(StringUtils.isNotBlank(opt.getName())){
    ptnrlst = masterDataDM.getPartnersByRef(opt.getReference());
   }else if(StringUtils.isNotBlank(opt.getCategory())){
    ptnrlst = masterDataDM.getPartnersByCategory(opt.getCategory());
   }else if(StringUtils.isNotBlank(opt.getPtnrType())){
    
    ptnrlst = masterDataDM.getPtnrsByType(opt.getPtnrType());
    LOGGER.log(INFO, "Partners by type returns {0}", ptnrlst);
   }
   
   ListIterator<PartnerBaseRec> ptnrLi = ptnrlst.listIterator();
    while(ptnrLi.hasNext()){
     PartnerBaseRec curr = ptnrLi.next();
     LOGGER.log(INFO, "name {0} cat {1} type {2}", new Object[]{curr.getName(),curr.getCategory(),curr.getPtnrType()});
     if(StringUtils.isNotBlank(opt.getName()) && 
      !StringUtils.startsWith(curr.getName(), opt.getName())){
      ptnrLi.remove();
     }else if(StringUtils.isNotBlank(opt.getCategory()) && 
       !StringUtils.startsWith(curr.getCategory(), opt.getCategory())){
      ptnrLi.remove();
     } else if(StringUtils.isNotBlank(opt.getPtnrType())){ 
       if (StringUtils.equalsIgnoreCase(opt.getPtnrType(), "pers") && 
               StringUtils.equals(curr.getClass().getSimpleName(), "PartnerCorporateRec")){
       //!StringUtils.startsWith(curr.getPtnrType(), opt.getPtnrType())){
         ptnrLi.remove();
       }else if (StringUtils.equalsIgnoreCase(opt.getPtnrType(), "corp") && 
               StringUtils.equals(curr.getClass().getSimpleName(), "PartnerPersonRec")){
       //!StringUtils.startsWith(curr.getPtnrType(), opt.getPtnrType())){
         ptnrLi.remove();
       }
     }
    }
   
  }
  
  
  return ptnrlst;
 }
 
 public List<PartnerPersonRec> getPartnersInvForRole(PartnerRoleRec role){
  
  List<PartnerPersonRec> ptnrList = this.masterDataDM.getPartnerIndivByRole(role);
  LOGGER.log(INFO, "DB layer returns {0}", ptnrList);
  return ptnrList;
 }
 
 public List<PartnerBaseRec> getPartnersAll() throws BacException {
  LOGGER.log(INFO,"masterDataMgr.getPartnersAll() called");
  try{
   List<PartnerBaseRec> ptnrlst = masterDataDM.getPartnersAll();
   return ptnrlst;
  }catch(Exception e){
   throw new BacException("Error getting partners - see system admin");
  }
  
 }
 
 public List<PartnerPersonRec> getPartnerIndivAll(){
  
  List<PartnerPersonRec> retList = masterDataDM.getAllPersonPartners();
  return retList;
 }

 /**
  * Creates bank partner 
  * @param ptnr
  * @param usr
  * @param view
  * @return
  * @throws BacException 
  */
 public PartnerCorporateRec createCorporatePartnerBank(PartnerCorporateRec ptnr, UserRec usr, String view) throws BacException {
  LOGGER.log(INFO, "MasterData Mgr createCorporatePartnerBank called");
  
  ptnr = this.masterDataDM.createCorporatePartnerBank(ptnr, usr, view);
  LOGGER.log(INFO, "createCorporatePartnerBank returned partner id {0}",ptnr.getId());
  return ptnr;
 }

 public BankRec createBankOrganisation(BankRec bnk,  String view) throws BacException {
  LOGGER.log(INFO, "createBankOrganisation called with bnk code {0}", bnk.getBankCode());
  LOGGER.log(INFO, "bnk created by {0}", bnk.getCreatedBy());
  
  PartnerCorporateRec bankCorpPtnr = createCorporatePartnerBank(bnk.getBankOrganisation(),bnk.getCreatedBy(),view);
  LOGGER.log(INFO, "bank Partner id after create {0}", bankCorpPtnr.getId());
  bnk.setBankOrganisation(bankCorpPtnr);
  /*if(bnk.getBankAddress() != null){
  if(bnk.getBankAddress().getId() == null){
   LOGGER.log(INFO, "new address required");
   if(bnk.getBankAddress().getPostCode() != null && !bnk.getBankAddress().getPostCode().isEmpty()){
    AddressRec addr = this.masterDataDM.createAddress(bnk.getBankAddress(),bnk.getCreatedBy(), view);
    bnk.setBankAddress(addr);
   }
   
  }else{
   List<AddressRec> addrLst = masterDataDM.getAddressesByPostCode(bnk.getBankAddress().getPostCode());
   ListIterator<AddressRec> addLi = addrLst.listIterator();
   boolean addrFound = false;
   while(addLi.hasNext() && !addrFound){
    AddressRec addr = addLi.next();
    if(addr.getId() == bnk.getBankAddress().getId()){
     LOGGER.log(INFO, "found address id");
     addrFound = true;
     boolean newAddrReq = false;
     if(!addr.getHouseNumber().equals(bnk.getBankAddress().getHouseNumber())){
      newAddrReq = true;
     }else if(!addr.getHouseName().equals(bnk.getBankAddress().getHouseName())){
      newAddrReq = true;
     }else if(!addr.getStreet().equals(bnk.getBankAddress().getStreet())){
      newAddrReq = true;
     }
     if(newAddrReq){
      AddressRec newAddr =this.masterDataDM.createAddress(bnk.getBankAddress(), bnk.getCreatedBy(), view);
      bnk.setBankAddress(newAddr);
     }
    }
   }
  }
  }*/
  
  /*LOGGER.log(INFO, "Bank contact {0}", bnk.getBankContact());
  if(bnk.getBankContact() != null){
  if(bnk.getBankContact().getId() == null || bnk.getBankContact().getId() == 0){
   LOGGER.log(INFO, "Need new contact person");
   if(bnk.getBankContact().getFamilyName() != null && !bnk.getBankContact().getFamilyName().isEmpty()){
    long id = this.createIndivPartnerAR(bnk.getBankContact(), bnk.getCreatedBy(), view);
    bnk.getBankContact().setId(id);
   }
  }
  }*/
  bnk = bankDM.createBank(bnk,view);
  LOGGER.log(INFO, "createBankOrganisation returns bank id {0}", bnk.getId());
  return bnk;
 }

 public boolean isPartnerRefUnique(String ref){
  boolean rc = this.masterDataDM.isPartnerRefUnique(ref);
  
  return rc;
 }
 
 public CurrencyRec updateCurrencyRec(CurrencyRec rec, String pg){
  
  rec = masterDataDM.updateCurrencyRec(rec, pg);
  LOGGER.log(INFO, "MasterDataMgr DB layer returns with id {0}", rec.getId());
  return rec;
 }
}
