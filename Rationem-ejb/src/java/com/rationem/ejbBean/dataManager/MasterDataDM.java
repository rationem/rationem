/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.helper.comparitor.PartnerByRef;
import java.util.Date;
import com.rationem.entity.user.User;
import java.util.ArrayList;
import java.util.ListIterator;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.mdm.PartnerCorporate;
import java.util.List;
import com.rationem.busRec.partner.PartnerCorporateRec;
import javax.annotation.PostConstruct;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import javax.persistence.TransactionRequiredException;
import javax.persistence.EntityManager;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.entity.audit.AuditAddress;
import com.rationem.entity.audit.AuditCountry;
import com.rationem.entity.audit.AuditCurrency;
import com.rationem.entity.audit.AuditPartner;
import com.rationem.entity.audit.AuditPartnerRole;
import com.rationem.entity.config.common.NumberRange;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.Country;
import com.rationem.entity.mdm.Currency;
import com.rationem.entity.mdm.PartnerRole;
import com.rationem.exception.BacException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.ejb.LocalBean;
import javax.persistence.Persistence;
import java.util.logging.Logger;



import static java.util.logging.Level.INFO;

import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Performs the actual database access for Master data. Partners and addresses
 * @author Chris
 */
@Stateless
@LocalBean
public class MasterDataDM {
    private static final Logger LOGGER =
            Logger.getLogger("com.rationem.ejbBean.dataManager.MasterDataDM");

   // @PersistenceContext
    @EJB
    private SysBuffer buffer;

    @EJB
    private UserDM usrDM;

    private EntityManager em;
    private EntityTransaction trans;
    
    private static EntityManager EM;
    private static EntityTransaction TRANS;



  @PostConstruct
  void init(){
   LOGGER.log(INFO,  "Loaded Master data DM");
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
    }
  
public static synchronized NumberRangeRec getNumberRangeNextVal(NumberRangeRec nrRec){
  LOGGER.log(INFO, "MasterDataDM.getNumberRangeNextVal called with id {0} ", nrRec.getNumberControlId());
  
  FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   EM = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   TRANS = EM.getTransaction();
   TRANS.begin();
   NumberRange nr = EM.find(NumberRange.class, nrRec.getNumberControlId());
   nrRec.setNextNum(nr.getNextNum());
   int nextNum = nr.getNextNum();
   nextNum++;
   nr.setNextNum(nextNum);
   EM.flush();
   TRANS.commit();
  return nrRec;
 }

   private AuditAddress buildAuditAddress(Address addr, User usr, String pg){
    AuditAddress aud = new AuditAddress();
    aud.setAddress(addr);
    aud.setAuditDate(new Date());
    aud.setCreatedBy(usr);
    aud.setSource(pg);
    em.persist(aud);
    return aud;
   }
   
   private AuditCountry buildAuditCountry(Country cntry, User usr, String pg){
    AuditCountry aud = new AuditCountry();
    aud.setAuditDate(new Date());
    aud.setCountry(cntry);
    aud.setCreatedBy(usr);
    aud.setSource(pg);
    em.persist(aud);
    return aud;
   }
   
   private AuditCurrency buildAuditCurrency(Currency curr, User usr, char action, String pg){
    AuditCurrency aud = new AuditCurrency();
    aud.setAuditDate(new Date());
    aud.setCurr(curr);
    aud.setSource(pg);
    aud.setUsrAction(action);
    em.persist(aud);
    return aud;
   }
    public Address buildAddressDM(AddressRec rec,  String pg){
     if(!trans.isActive()){
      trans.begin();
     }
     
     Address addr = buildAddress(rec,  pg);
     trans.commit();
     return addr;
     
    }

    private Address buildAddress(AddressRec rec,  String pg){
        LOGGER.log(INFO, "buildAddress with: {0}",rec);
        LOGGER.log(INFO, "Address id: {0}",rec.getId());
        LOGGER.log(INFO, "rec.getCreatedBy(): {0}",rec.getCreatedBy());
        boolean newAddr = false;
        boolean changed = false;
        Address f;
        if(rec.getAddrRef() == null || rec.getAddrRef().isEmpty()){
         StringBuilder refBuff = new StringBuilder();
         if(!StringUtils.isBlank(rec.getHouseNumber())){
          refBuff.append(rec.getHouseNumber());
         }
         if(!StringUtils.isBlank(rec.getHouseName())){
          refBuff.append(rec.getHouseName());
         }
         if(!StringUtils.isBlank(rec.getStreet())){
          refBuff.append(rec.getStreet());
         }
         if(!StringUtils.isBlank(rec.getPostCode())){
          refBuff.append(rec.getStreet());
         }
         
         rec.setAddrRef(refBuff.toString());
        }
        
        if(rec.getId() == null || rec.getId() == 0){
         f = new Address();
         User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
         f.setCreatedBy(crUsr);
         f.setCreatedDate(rec.getCreatedOn());
         em.persist(f);
         AuditAddress aud = this.buildAuditAddress(f, crUsr, pg);
         aud.setNewValue(rec.getAddrRef());
         aud.setUsrAction('I');
         newAddr = true;
        }else{
         f = em.find(Address.class, rec.getId(), OPTIMISTIC);
        }
        
        if(newAddr){
         f.setAddrRef(rec.getAddrRef());
         f.setBuilding(rec.getBuilding());
         f.setDepartment(rec.getDepartment());
         f.setPoBox(rec.getPoBox());
         
         if(rec.getCountry() != null){
          Country c = em.find(Country.class, rec.getCountry().getId());
          f.setCountry(c);
         }
         f.setCountyName(rec.getCountyName());
         
         f.setFloor(rec.getFloor());
         f.setHouseName(rec.getHouseName());
         f.setHouseNum(rec.getHouseNumber());
         f.setPostCode(rec.getPostCode());
         f.setRevision(rec.getRevision());
         f.setRoom(rec.getRoom());
         f.setStreet(rec.getStreet());
         f.setStreet2(rec.getStreet2());
         f.setTown(rec.getTown());
         if(rec.getCountry() != null){
          Country cntry = em.find(Country.class, rec.getCountry().getId(), OPTIMISTIC);
          f.setCountry(cntry);
         }
        }else{
         // change?
         User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
         
         if((rec.getAddrRef() == null && f.getAddrRef() != null) ||
            (rec.getAddrRef() != null && f.getAddrRef() == null) ||     
            (rec.getAddrRef() != null && !rec.getAddrRef().equals(f.getAddrRef()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_REF");
          aud.setNewValue(rec.getAddrRef());
          if(f.getAddrRef() != null){
           aud.setOldValue(f.getAddrRef());
          }
          aud.setUsrAction('U');
          f.setAddrRef(rec.getAddrRef());
          changed = true;
         }
         
         if((rec.getBuilding() == null && f.getBuilding() != null) ||
            (rec.getBuilding() != null && f.getBuilding() == null) ||    
            (rec.getBuilding() != null && !rec.getBuilding().equals(f.getBuilding()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_BUILD");
          aud.setNewValue(rec.getBuilding());
          aud.setOldValue(f.getBuilding());
          aud.setUsrAction('U');
          f.setBuilding(rec.getBuilding());
          changed = true;
         }
         
         if((rec.getCountry() == null && f.getCountry() != null) ||
            (rec.getCountry() != null && f.getCountry() == null) ||
            (rec.getCountryName() != null &&  !
           Objects.equals(rec.getCountry().getId(), f.getCountry().getId()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_CNTRY");
          aud.setNewValue(rec.getCountry().getCountryName());
          aud.setOldValue(f.getCountry().getCountryName());
          aud.setUsrAction('U');
          f.setCountryName(rec.getCountryName());
          changed = true;
         }
         
         
         if((rec.getCountyName() == null && f.getCountyName() != null) ||
            (rec.getCountyName() != null && f.getCountyName() == null) ||
            (rec.getCountyName() != null && !rec.getCountyName().equals(f.getCountyName()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_CNTY");
          aud.setNewValue(rec.getCountyName());
          aud.setOldValue(f.getCountyName());
          aud.setUsrAction('U');
          f.setCountyName(rec.getCountyName());
          changed = true;
         }
         
         if((rec.getHouseName() == null && f.getHouseName() != null)||
            (rec.getHouseName() != null && f.getHouseName() == null)||
            (rec.getHouseName() != null && !rec.getHouseName().equals(f.getHouseName()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_HSE_NAME");
          aud.setNewValue(rec.getHouseName());
          aud.setOldValue(f.getHouseName());
          aud.setUsrAction('U');
          f.setHouseName(rec.getHouseName());
          changed = true;
         }
         
         if(!rec.getHouseNumber().equals(f.getHouseNum())){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_HSE_NUM");
          aud.setNewValue(rec.getHouseNumber());
          aud.setOldValue(f.getHouseNum());
          aud.setUsrAction('U');
          try{
           f.setHouseNum(rec.getHouseNumber());
          }catch( NumberFormatException x){
            f.setHouseNum(rec.getHouseNumber());
          }
            
          changed = true;
         }
         
         LOGGER.log(INFO, "rec post code {0} f postCode {1}", new Object[]{rec.getPostCode(), f.getPostCode()});
         if((rec.getPostCode() != null && f.getPostCode() == null)||
            (rec.getPostCode() == null && f.getPostCode() != null)||
            (rec.getPostCode() != null && !rec.getPostCode().equals(f.getPostCode()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          
          aud.setFieldName("ADDR_PC");
          aud.setNewValue(rec.getPostCode());
          aud.setOldValue(f.getPostCode());
          aud.setUsrAction('U');
          f.setPostCode(rec.getPostCode());
          changed = true;
         }
         
         if((rec.getStreet() == null && f.getStreet() != null)||
            (rec.getStreet() != null && f.getStreet() == null)||
            (rec.getStreet() != null && !rec.getStreet().equals(f.getStreet()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_STR");
          aud.setNewValue(rec.getStreet());
          aud.setOldValue(f.getStreet());
          aud.setUsrAction('U');
          f.setStreet(rec.getStreet());
          changed = true;
         }
         
         if((rec.getStreet2() == null && f.getStreet2() != null)||
            (rec.getStreet2() != null && f.getStreet2() == null)||
            (rec.getStreet2() != null && !rec.getStreet2().equals(f.getStreet()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_STR2");
          aud.setNewValue(rec.getStreet2());
          aud.setOldValue(f.getStreet2());
          aud.setUsrAction('U');
          f.setStreet2(rec.getStreet2());
          changed = true;
         }
         
         if((rec.getTown() == null && f.getTown() != null) ||
            (rec.getTown() != null && f.getTown() == null) ||
            (rec.getTown() != null && !rec.getTown().equals(f.getTown()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_TOWN");
          aud.setNewValue(rec.getTown());
          aud.setOldValue(f.getTown());
          aud.setUsrAction('U');
          f.setTown(rec.getTown());
          changed = true;
         }
         if((rec.getCountry() == null && f.getCountry() != null) ||
            (rec.getCountry() != null && f.getCountry() == null) ||
            (rec.getCountry() != null && !Objects.equals(rec.getCountry().getId(), f.getCountry().getId()))){
          AuditAddress aud = this.buildAuditAddress(f, chUsr, pg);
          aud.setFieldName("ADDR_CNTRY");
          aud.setNewValue(rec.getCountry().getCountryCode2());
          if(f.getCountry() != null){
           aud.setOldValue(f.getCountry().getCountryCode2());
          }
          aud.setUsrAction('U');
          Country cntry = em.find(Country.class, rec.getCountry().getId(), OPTIMISTIC);
          f.setCountry(cntry);
          changed = true;
         }
         
         if(changed){
          f.setUpdateBy(chUsr);
          f.setUpdateDate(rec.getChangedOn());
         }
        }
        
      
      return f;
    }



    private AddressRec buildAddressRec(Address rec){

      AddressRec f = new AddressRec();
      f.setAddrRef(rec.getAddrRef());
      f.setBuilding(rec.getBuilding());
      f.setDepartment(rec.getDepartment());
      f.setCountryIso(f.getCountryIso());
      f.setCountryName(rec.getCountryName());
      f.setCountyId(rec.getCountyId());
      f.setCreatedOn(rec.getCreatedDate());
      f.setFloor(rec.getFloor());
      f.setHouseName(rec.getHouseName());
      if(!StringUtils.isBlank(rec.getHouseNum())){
       f.setHouseNumber(rec.getHouseNum());
      }
      f.setId(rec.getId());
      f.setPoBox(rec.getPoBox());
      f.setPostCode(rec.getPostCode());
      f.setRevision(rec.getRevision());
      f.setRoom(rec.getRoom());
      f.setStreet(rec.getStreet());
      f.setStreet2(rec.getStreet2());
      f.setTown(rec.getTown());
      f.setChangedOn(rec.getUpdateDate());
      if(rec.getCountry() != null){
       CountryRec cntry = this.buildCountryRec(rec.getCountry());
       f.setCountry(cntry);
      }
     return f;
    }
    

  private AuditPartner buildAuditPartner(PartnerBase ptnr, User usr, char usrAction, String pg){
   AuditPartner aud = new AuditPartner();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setPartner(ptnr);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
  
  private AuditPartnerRole buildAuditPartnerRole(PartnerRole role, User usr, char usrAction, String pg){
   AuditPartnerRole aud = new AuditPartnerRole();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setPtnrRole(role);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
  
  private Country buildCountry(CountryRec rec, UserRec usr, String pg){
   Country cntry;
   boolean newCountry = false;
   boolean countryUpdated = false;
   if(rec.getId() == null){
    cntry = new Country();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    cntry.setCreatedBy(crUsr);
    cntry.setCreatedOn(rec.getCreatedOn());
    em.persist(cntry);
    AuditCountry aud = this.buildAuditCountry(cntry, crUsr, pg);
    aud.setNewValue(rec.getCountryCode2());
    aud.setUsrAction('I');
    newCountry = true;
   }else{
    cntry = em.find(Country.class, rec.getId(), OPTIMISTIC);
   }
   
   if(newCountry){
    cntry.setCountryCode2(rec.getCountryCode2());
    cntry.setCountryCode3(rec.getCountryCode3());
    cntry.setCountryName(rec.getCountryName());
    cntry.setCountryNameFr(rec.getCountryNameFr());
    cntry.setCountryNumeric(rec.getCountryNumeric());
    cntry.setCurrCode(rec.getCurrCode());
    cntry.setCurrName(rec.getCurrName());
    cntry.setCurrNumCode(rec.getCurrNumCode());
    cntry.setDependency(rec.getDependency());
    cntry.setDialCd(rec.getDialCd());
    cntry.setFractionalLength(rec.getFractionalLength());
    cntry.setIndependent(rec.isIndependent());
    cntry.setCurrMajorTitle(rec.getCurrMajorTitle());
    cntry.setCurrMajorTitlePlural(rec.getCurrMajorTitlePlural());
   }else{
    // updated?
    User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
    if((rec.getCountryCode2() == null && cntry.getCountryCode2() != null) ||
       (rec.getCountryCode2() != null && cntry.getCountryCode2() == null) ||
       (rec.getCountryCode2() != null && !rec.getCountryCode2().equals(cntry.getCountryCode2()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CODE2");
     aud.setNewValue(rec.getCountryCode2());
     aud.setOldValue(cntry.getCountryCode2());
     aud.setUsrAction('U');
     cntry.setCountryCode2(rec.getCountryCode2());
     countryUpdated = true;
    }
    
    if((rec.getCountryCode3() == null && cntry.getCountryCode3() != null) ||
       (rec.getCountryCode3() != null && cntry.getCountryCode3() == null) ||
       (rec.getCountryCode3() != null && !rec.getCountryCode3().equals(cntry.getCountryCode3()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CODE3");
     aud.setNewValue(rec.getCountryCode3());
     aud.setOldValue(cntry.getCountryCode3());
     aud.setUsrAction('U');
     cntry.setCountryCode3(rec.getCountryCode3());
     countryUpdated = true;
    }
    
    if((rec.getCountryName() == null && cntry.getCountryName() != null) ||
       (rec.getCountryName() != null && cntry.getCountryName() == null) ||
       (rec.getCountryName() != null && !rec.getCountryName().equals(cntry.getCountryName()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_NAME");
     aud.setNewValue(rec.getCountryName());
     aud.setOldValue(cntry.getCountryName());
     aud.setUsrAction('U');
     cntry.setCountryName(rec.getCountryName());
     countryUpdated = true;
    }
   
    if((rec.getCountryNameFr() == null && cntry.getCountryNameFr() != null) ||
       (rec.getCountryNameFr() != null && cntry.getCountryNameFr() == null) ||
       (rec.getCountryNameFr() != null && !rec.getCountryNameFr().equals(cntry.getCountryNameFr()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_NAME_FR");
     aud.setNewValue(rec.getCountryNameFr());
     aud.setOldValue(cntry.getCountryNameFr());
     aud.setUsrAction('U');
     cntry.setCountryNameFr(rec.getCountryNameFr());
     countryUpdated = true;
    }
    
    if(rec.getCountryNumeric() != cntry.getCountryNumeric()){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CODE_NUM");
     aud.setNewValue(String.valueOf(cntry.getCountryNumeric()));
     aud.setOldValue(String.valueOf(rec.getCountryNumeric()));
     aud.setUsrAction('U');
     cntry.setCountryNumeric(rec.getCountryNumeric());
     countryUpdated = true;
    }
    
    if((rec.getDependency() == null && cntry.getDependency() != null) ||
       (rec.getDependency() != null && cntry.getDependency() == null) ||
       (rec.getDependency() != null && !rec.getDependency().equals(cntry.getDependency()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_DEP");
     aud.setNewValue(rec.getDependency());
     aud.setOldValue(cntry.getDependency());
     aud.setUsrAction('U');
     cntry.setDependency(rec.getDependency());
     countryUpdated = true;
    }
    
    if((rec.getDialCd() == null && cntry.getDialCd() != null) ||
       (rec.getDialCd() != null && cntry.getDialCd() == null) ||
       (rec.getDialCd() != null && !rec.getDialCd().equals(cntry.getDialCd()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_DIAL");
     aud.setNewValue(rec.getDialCd());
     aud.setOldValue(cntry.getDialCd());
     aud.setUsrAction('U');
     cntry.setDialCd(rec.getDialCd());
     countryUpdated = true;
    }
    
    
    
    if((rec.getCurrCode() == null && cntry.getCurrCode() != null) ||
       (rec.getCurrCode() != null && cntry.getCurrCode() == null) ||
       (rec.getCurrCode() != null && !rec.getCurrCode().equals(cntry.getCurrCode()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CURR_CODE");
     aud.setNewValue(rec.getCurrCode());
     aud.setOldValue(cntry.getCurrCode());
     aud.setUsrAction('U');
     cntry.setCurrCode(rec.getCurrCode());
    }
    
    if(!StringUtils.equals(rec.getCurrMajorTitle(), cntry.getCurrMajorTitle())){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CURR_MAJOR");
     aud.setNewValue(rec.getCurrMajorTitle());
     aud.setOldValue(cntry.getCurrMajorTitle());
     aud.setUsrAction('U');
     cntry.setCurrMajorTitle(rec.getCurrMajorTitle());
    }
    
    if(!StringUtils.equals(rec.getCurrMajorTitlePlural(), cntry.getCurrMajorTitlePlural())){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CURR_MAJOR_PL");
     aud.setNewValue(rec.getCurrMajorTitlePlural());
     aud.setOldValue(cntry.getCurrMajorTitlePlural());
     aud.setUsrAction('U');
     cntry.setCurrMajorTitlePlural(rec.getCurrMajorTitlePlural());
    }
    
    if((rec.getCurrName() == null && cntry.getCurrName() != null) ||
       (rec.getCurrName() != null && cntry.getCurrName() == null) ||
       (rec.getCurrName() != null && !rec.getCurrName().equals(cntry.getCurrName()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CURR_NAME");
     aud.setNewValue(rec.getCurrName());
     aud.setOldValue(cntry.getCurrName());
     aud.setUsrAction('U');
     cntry.setCurrName(rec.getCurrName());
     countryUpdated = true;
    }
    
    if(rec.getCurrNumCode() != cntry.getCurrNumCode()){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_CODE_NUM");
     aud.setNewValue(String.valueOf(cntry.getCurrNumCode()));
     aud.setOldValue(String.valueOf(rec.getCurrNumCode()));
     aud.setUsrAction('U');
     cntry.setCurrNumCode(rec.getCurrNumCode());
     countryUpdated = true;
    }
    
    if((rec.getDependency() == null && cntry.getDependency() != null) ||
       (rec.getDependency() != null && cntry.getDependency() == null) ||
       (rec.getDependency() != null && !rec.getDependency().equals(cntry.getDependency()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_DEP");
     aud.setNewValue(rec.getDependency());
     aud.setOldValue(cntry.getDependency());
     aud.setUsrAction('U');
     cntry.setDependency(rec.getDependency());
     countryUpdated = true;
    }
    
    if((rec.getDialCd() == null && cntry.getDialCd() != null) ||
       (rec.getDialCd() != null && cntry.getDialCd() == null) ||
       (rec.getDialCd() != null && !rec.getDialCd().equals(cntry.getDialCd()))){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_DIAL");
     aud.setNewValue(rec.getDialCd());
     aud.setOldValue(cntry.getDialCd());
     aud.setUsrAction('U');
     cntry.setDialCd(rec.getDialCd());
     countryUpdated = true;
    }
    
     if(rec.getFractionalLength() != cntry.getFractionalLength()){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_FR_LEN");
     aud.setNewValue(String.valueOf(cntry.getFractionalLength()));
     aud.setOldValue(String.valueOf(rec.getFractionalLength()));
     aud.setUsrAction('U');
     cntry.setFractionalLength(rec.getFractionalLength());
     countryUpdated = true;
    }
     
    if(rec.isIndependent() != cntry.isIndependent()){
     AuditCountry aud = this.buildAuditCountry(cntry, chUsr, pg);
     aud.setFieldName("CNTRY_FR_LEN");
     aud.setNewValue(String.valueOf(cntry.isIndependent()));
     aud.setOldValue(String.valueOf(rec.isIndependent()));
     aud.setUsrAction('U');
     cntry.setIndependent(rec.isIndependent());
     countryUpdated = true;
    }
   
    if(countryUpdated){
     cntry.setChangedBy(chUsr);
     cntry.setChangedOn(rec.getChangedOn());
    }
   }
   
   return cntry;
  }
  
  public CountryRec buildCountryRecPvt(Country cntry){
   return buildCountryRec(cntry);
  }
  
  
  private CountryRec buildCountryRec(Country cntry){
   CountryRec rec = new CountryRec();
    
   rec.setId(cntry.getId());
   if(cntry.getChangedBy() != null){
    UserRec chUser = this.usrDM.getUserRecPvt(cntry.getChangedBy());
    rec.setChangedBy(chUser);
    rec.setChangedOn(cntry.getChangedOn());
   }
   
   rec.setCountryCode2(cntry.getCountryCode2());
   rec.setCountryCode3(cntry.getCountryCode3());
   rec.setCountryName(cntry.getCountryName());
   rec.setCountryNameFr(cntry.getCountryNameFr());
   rec.setCountryNumeric(cntry.getCountryNumeric());
   rec.setCurrCode(cntry.getCurrCode());
   rec.setCurrName(cntry.getCurrName());
   rec.setCurrNumCode(cntry.getCurrNumCode());
   rec.setDependency(cntry.getDependency());
   rec.setDialCd(cntry.getDialCd());
   rec.setFractionalLength(cntry.getFractionalLength());
   rec.setIndependent(cntry.isIndependent());
   rec.setCurrMajorTitle(cntry.getCurrMajorTitle());
   rec.setCurrMajorTitlePlural(cntry.getCurrMajorTitlePlural());
   
   UserRec crUsr = usrDM.getUserRecPvt(cntry.getCreatedBy());
   rec.setCreatedBy(crUsr);
   rec.setCreatedOn(cntry.getCreatedOn());
   
   List<Locale> locs = LocaleUtils.languagesByCountry(rec.getCountryCode2());
   if(locs != null && !locs.isEmpty()){
    rec.setLocale(locs.get(0));
   }
   return rec;
  }
  
  private Currency buildCurrency(CurrencyRec rec, String src){
   Currency curr;
   boolean newCurr = false;
   boolean changedCurr = false;
   
   if(rec.getId() == null){
    curr = new Currency();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    curr.setCreatedBy(crUsr);
    curr.setCreatedOn(rec.getCreatedOn());
    em.persist(curr);
    AuditCurrency aud = this.buildAuditCurrency(curr, crUsr, 'I', src);
    aud.setNewValue(rec.getCurrAlphaCode());
    newCurr = true;
   }else{
    curr = em.find(Currency.class, rec.getId(), OPTIMISTIC);
    
   }
   if(newCurr){
    curr.setCurrAlphaCode(rec.getCurrAlphaCode());
    curr.setCurrNumCode(rec.getCurrNumCode());
    curr.setCurrSymbol(rec.getCurrSymbol());
    curr.setMinorUnit(rec.getMinorUnit());
    curr.setName(rec.getName());
    curr.setMajorUnitDescr(rec.getMajorUnitDescr());
    curr.setMajorUnitDescrPl(rec.getMajorUnitDescrPl());
    curr.setMinorUnitDescr(rec.getMinorUnitDescr());
    curr.setMinorUnitDescrPl(rec.getMinorUnitDescrPl());
   }else{
    // changed ?
    User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
    
    if((curr.getCurrAlphaCode() == null && rec.getCurrAlphaCode() != null) ||
       (curr.getCurrAlphaCode() != null && rec.getCurrAlphaCode() == null) ||
       (curr.getCurrAlphaCode() != null && !curr.getCurrAlphaCode().equals(rec.getCurrAlphaCode()))){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_ALPHA_CD");
     aud.setNewValue(rec.getCurrAlphaCode());
     aud.setOldValue(curr.getCurrAlphaCode());
     curr.setCurrAlphaCode(rec.getCurrAlphaCode());
     changedCurr = true;
    }
    
    if((curr.getCurrSymbol() == null && rec.getCurrSymbol() != null) ||
       (curr.getCurrSymbol() != null && rec.getCurrSymbol() == null) ||
       (curr.getCurrSymbol() != null && !curr.getCurrSymbol().equals(rec.getCurrSymbol()))){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_SYMB");
     aud.setNewValue(rec.getCurrSymbol());
     aud.setOldValue(curr.getCurrSymbol());
     curr.setCurrSymbol(rec.getCurrSymbol());
     changedCurr = true;
    }
    
    if((curr.getName() == null && rec.getName() != null) ||
       (curr.getName() != null && rec.getName() == null) ||
       (curr.getName() != null && !curr.getName().equals(rec.getName()))){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_NM");
     aud.setNewValue(rec.getName());
     aud.setOldValue(curr.getName());
     curr.setName(rec.getName());
     changedCurr = true;
    }
    
    if(curr.getCurrNumCode() != rec.getCurrNumCode()){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_NUM_CD");
     aud.setNewValue(String.valueOf(rec.getCurrNumCode()));
     aud.setOldValue(String.valueOf(curr.getCurrNumCode()));
     curr.setCurrNumCode(rec.getCurrNumCode());
     changedCurr = true;
    }
    
    if(!StringUtils.equals(curr.getMajorUnitDescr(), rec.getMajorUnitDescr())){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_MAJOR_DESCR");
     aud.setNewValue(rec.getMajorUnitDescr());
     aud.setOldValue(curr.getMajorUnitDescr());
     curr.setMajorUnitDescr(rec.getMajorUnitDescr());
     changedCurr = true;
    }
    
    if(curr.getMinorUnit() != rec.getMinorUnit()){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_MINOR_UNIT");
     aud.setNewValue(String.valueOf(rec.getMinorUnit()));
     aud.setOldValue(String.valueOf(curr.getMinorUnit()));
     curr.setMinorUnit(rec.getMinorUnit());
     changedCurr = true;
    }
    
    if(!StringUtils.equals(curr.getMinorUnitDescr(), rec.getMinorUnitDescr())){
     AuditCurrency aud = buildAuditCurrency(curr, chUsr, 'U', src);
     aud.setFieldName("CURR_MINOR_DESCR");
     aud.setNewValue(rec.getMinorUnitDescr());
     aud.setOldValue(curr.getMinorUnitDescr());
     curr.setMinorUnitDescr(rec.getMinorUnitDescr());
     changedCurr = true;
    }
    if(changedCurr){
     curr.setChangedBy(chUsr);
     curr.setChangedOn(rec.getChangedOn());
    }
    
   }
   
   return curr;
   
  }
  
  public CurrencyRec buildCurrencyRecPvt(Currency curr){
   return buildCurrencyRec(curr);
  }
  
  private CurrencyRec buildCurrencyRec(Currency curr){
   CurrencyRec rec = new CurrencyRec();
   rec.setId(curr.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(curr.getCreatedBy());
   rec.setCreatedBy(crUsr);
   rec.setCreatedOn(curr.getCreatedOn());
   if(curr.getChangedBy() != null){
    UserRec chUsr = usrDM.getUserRecPvt(curr.getChangedBy());
    rec.setChangedBy(chUsr);
    rec.setChangedOn(curr.getChangedOn());
   }
   rec.setCurrAlphaCode(curr.getCurrAlphaCode());
   rec.setCurrNumCode(curr.getCurrNumCode());
   rec.setCurrSymbol(curr.getCurrSymbol());
   rec.setMinorUnit(curr.getMinorUnit());
   rec.setName(curr.getName());
   rec.setMajorUnitDescr(curr.getMajorUnitDescr());
   rec.setMajorUnitDescrPl(curr.getMajorUnitDescrPl());
   rec.setMinorUnitDescr(curr.getMinorUnitDescr());
   rec.setMinorUnitDescrPl(curr.getMinorUnitDescrPl());
   return rec;
  }
  
  private PartnerBase  buildPartner(PartnerBaseRec rec, String pg){
   LOGGER.log(INFO, "buildPartner called with {0}", rec);
   boolean newPtnr = false;
   boolean changedPtnr = false;
   String actClass = rec.getClass().getSimpleName();
   PartnerBase ptnr;
   
   if(rec.getId() == null){
    if(StringUtils.equals(actClass, "PartnerCorporateRec")){
     ptnr = new PartnerCorporate();
    }else{
     ptnr = new PartnerPerson();
    }
    User crUsr = em.find(User.class, rec.getCreatedBy().getId());
    ptnr.setCreatedBy(crUsr);
    ptnr.setCreatedDate(rec.getCreatedDate());
    em.persist(ptnr);
    AuditPartner aud = this.buildAuditPartner(ptnr, crUsr, 'I', pg);
    aud.setNewValue(rec.getName());
    newPtnr = true;
   }else{
    ptnr = em.find(PartnerBase.class, rec.getId());
   }
   
   if(newPtnr){
    // build basic details
    if(rec.getDefaultAddress() != null){
     Address addr = buildAddress(rec.getDefaultAddress(), pg);
     ptnr.setDefaultAddress(addr);
    }
    
    if(rec.getCountry() != null){
     Country c = em.find(Country.class, rec.getCountry().getId());
     ptnr.setCountry(c);
    }
    ptnr.setCategory(rec.getCategory());
    ptnr.setEmail(rec.getEmail());
    ptnr.setMobileTelephone(rec.getMobileTelephone());
    
    LOGGER.log(INFO, "rec.getPartnerRoles() {0}", rec.getPartnerRoles());
    if(rec.getPartnerRoles() != null){
     List<PartnerRole> roles = new ArrayList<>();
     for(PartnerRoleRec curr:rec.getPartnerRoles()){
      PartnerRole role = em.find(PartnerRole.class, curr.getId());
      roles.add(role);
     }
     LOGGER.log(INFO, "roles to add to ptnr {0}", roles);
     ptnr.setPartnerRoles(roles);
    }
    ptnr.setRef(rec.getRef());
    ptnr.setTelephone(rec.getTelephone());
    ptnr.setTradingName(rec.getTradingName());
    ptnr.setUrl(rec.getUrl());
    ptnr.setVatNumber(rec.getVatNumber());
    ptnr.setVatRegisteredDate(rec.getVatRegisteredDate());
    ptnr.setWebAddress(rec.getWebAddress());
    if(StringUtils.equals(actClass, "PartnerCorporateRec")){
     // add corp details
     
     ((PartnerCorporate)ptnr).setAccountsFilingDate(((PartnerCorporateRec)rec).getAccountsFilingDate());
     ((PartnerCorporate)ptnr).setCharityNumber(((PartnerCorporateRec)rec).getCharityNumber());
     ((PartnerCorporate)ptnr).setCharityRegistrationDate(((PartnerCorporateRec)rec).getCharityRegistrationDate());
     ((PartnerCorporate)ptnr).setCompanyNumber(((PartnerCorporateRec)rec).getCompanyNumber());
     ((PartnerCorporate)ptnr).setTradingName(((PartnerCorporateRec)rec).getTradingName());
     ((PartnerCorporate)ptnr).setLegalName(((PartnerCorporateRec)rec).getLegalName());
     ((PartnerCorporate)ptnr).setCharity(((PartnerCorporateRec)rec).isCharity());
     if(((PartnerCorporate)ptnr).getHeadOfficeAddress() != null){
      Address ho = this.buildAddress(((PartnerCorporateRec)rec).getHeadOfficeAddress(),  pg);
      ((PartnerCorporate)ptnr).setHeadOfficeAddress(ho);
     }
     if(((PartnerCorporateRec)rec).getRegisteredOfficeAddress() != null){
      Address ho = this.buildAddress(((PartnerCorporateRec)rec).getRegisteredOfficeAddress(),  pg);
      ((PartnerCorporate)ptnr).setRegisteredOfficeAddress(ho);
     }
    }else{
     //Person partner
     ((PartnerPerson)ptnr).setTitle(((PartnerPersonRec)rec).getTitle());
     ((PartnerPerson)ptnr).setDateOfBirth(((PartnerPersonRec)rec).getDateOfBirth());
     ((PartnerPerson)ptnr).setFamilyName(((PartnerPersonRec)rec).getFamilyName());
     ((PartnerPerson)ptnr).setFirstName(((PartnerPersonRec)rec).getFirstName());
     ((PartnerPerson)ptnr).setMiddleName(((PartnerPersonRec)rec).getMiddleName());
     if(((PartnerPersonRec)rec).getHeadOfficeAddress() != null){
      Address hoAddress = this.buildAddress(((PartnerPersonRec)rec).getHeadOfficeAddress(), pg);
      ((PartnerPerson)ptnr).setHeadOfficeAddress(hoAddress);
     }
    }
    
   }else{
    //has there been a change?
    User chUsr = em.find(User.class, rec.getChangedBy().getId());
    SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MMM/yyyy");
    
    if((rec.getDefaultAddress() == null && ptnr.getDefaultAddress() != null) ||
      (rec.getDefaultAddress() != null && ptnr.getDefaultAddress() == null) ||
      (rec.getDefaultAddress() != null 
       && Objects.equals(rec.getDefaultAddress().getId(), ptnr.getDefaultAddress().getId()))){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_DEF_ADDR");
     aud.setOldValue(ptnr.getDefaultAddress().getStreetFormatted() + " "+ptnr.getDefaultAddress().getPostCode());
     aud.setNewValue(rec.getDefaultAddress().getStreetLine() + " "+rec.getDefaultAddress().getPostCode());
     Address addr = em.find(Address.class, rec.getDefaultAddress().getId());
     ptnr.setDefaultAddress(addr);
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getCategory(), ptnr.getCategory())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_CAT");
     aud.setNewValue(rec.getCategory());
     aud.setOldValue(ptnr.getCategory());
     ptnr.setCategory(rec.getCategory());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getEmail(), ptnr.getEmail())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_EMAIL");
     aud.setNewValue(rec.getEmail());
     aud.setOldValue(ptnr.getEmail());
     ptnr.setEmail(rec.getEmail());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getMobileTelephone(), ptnr.getMobileTelephone())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_MOB");
     aud.setNewValue(rec.getMobileTelephone());
     aud.setOldValue(ptnr.getMobileTelephone());
     ptnr.setMobileTelephone(rec.getMobileTelephone());
     changedPtnr = true;
    }
    
    if((rec.getPartnerRoles() == null && ptnr.getPartnerRoles() != null) ||
      (rec.getPartnerRoles() != null && ptnr.getPartnerRoles() == null) ||
      (rec.getPartnerRoles() != null && ptnr.getPartnerRoles() != null)){
     LOGGER.log(INFO, "rec roles {0}", rec.getPartnerRoles());
     LOGGER.log(INFO, "ptnr roles {0}", ptnr.getPartnerRoles());
     boolean found;
     for(PartnerRoleRec currRec:rec.getPartnerRoles()){
      // is there a new role
      found = false;
      for(PartnerRole curr:ptnr.getPartnerRoles()){
       if(Objects.equals(curr.getPtnrRoleId(), currRec.getId())){
        found = true;
        break;
       }
      }
      if(!found){
       AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
       aud.setFieldName("PTNR_ROLE_ADD");
       aud.setNewValue(currRec.getRoleCode());
       PartnerRole r = this.buildPartnerRole(currRec, pg);
       ptnr.getPartnerRoles().add(r);
       changedPtnr = true;
      }
     }
     ListIterator<PartnerRole> li = ptnr.getPartnerRoles().listIterator();
     // is there a role that has been removed?
     
     while(li.hasNext()){
      PartnerRole curr = li.next();
      found = false;
      for(PartnerRoleRec currRec:rec.getPartnerRoles()){
       if(Objects.equals(curr.getPtnrRoleId(), currRec.getId())){
        found = true;
        break;
       }
      }
      if(!found){
       AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
       aud.setFieldName("PTNR_ROLE_DEL");
       aud.setOldValue(curr.getRoleCode());
       li.remove();
       changedPtnr = true;
      }
     }
    }
     
    
    
    if(!StringUtils.equals(rec.getRef(), ptnr.getRef())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_REF");
     aud.setNewValue(rec.getRef());
     aud.setOldValue(ptnr.getRef());
     ptnr.setRef(rec.getRef());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getTelephone(), ptnr.getTelephone())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_PHONE");
     aud.setNewValue(rec.getTelephone());
     aud.setOldValue(ptnr.getTelephone());
     ptnr.setTelephone(rec.getTelephone());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getTradingName(), ptnr.getTradingName())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_TR_NAME");
     aud.setNewValue(rec.getTradingName());
     aud.setOldValue(ptnr.getTradingName());
     ptnr.setTradingName(rec.getTradingName());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getVatNumber(), ptnr.getVatNumber())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_VAT_NUM");
     aud.setNewValue(rec.getVatNumber());
     aud.setOldValue(ptnr.getVatNumber());
     ptnr.setVatNumber(rec.getVatNumber());
     changedPtnr = true;
    }
    
    if((rec.getVatRegisteredDate() == null && ptnr.getVatRegisteredDate() != null) ||
      (rec.getVatRegisteredDate() != null && ptnr.getVatRegisteredDate() == null) ||
      (rec.getVatRegisteredDate() != null 
       && !rec.getVatRegisteredDate().equals(ptnr.getVatRegisteredDate()) )){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_VAT_REG_DT");
     
     aud.setNewValue(dateFmt.format(rec.getVatNumber()));
     aud.setOldValue(dateFmt.format(rec.getVatNumber()));
     ptnr.setVatRegisteredDate(rec.getVatRegisteredDate());
     changedPtnr = true;
    }
    
    if(!StringUtils.equals(rec.getUrl(), ptnr.getUrl())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_URL");
     aud.setNewValue(rec.getUrl());
     aud.setOldValue(ptnr.getUrl());
     ptnr.setUrl(rec.getUrl());
     changedPtnr = true;
    }
    
    
    if(!StringUtils.equals(rec.getWebAddress(), ptnr.getWebAddress())){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_WEB");
     aud.setNewValue(rec.getWebAddress());
     aud.setOldValue(ptnr.getWebAddress());
     ptnr.setWebAddress(rec.getWebAddress());
     changedPtnr = true;
    }
    if(StringUtils.equals(actClass, "PartnerCorporateRec")){
     // change to corp partner
     if((((PartnerCorporateRec)rec).getAccountsFilingDate() == null && ((PartnerCorporate)ptnr).getAccountsFilingDate() != null ) ||
        (((PartnerCorporateRec)rec).getAccountsFilingDate() != null && ((PartnerCorporate)ptnr).getAccountsFilingDate() == null ) ||
        (((PartnerCorporateRec)rec).getAccountsFilingDate() != null && 
         !((PartnerCorporateRec)rec).getAccountsFilingDate().equals(((PartnerCorporate)ptnr).getAccountsFilingDate()))){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_ACS_FILE_DT");
      aud.setNewValue(dateFmt.format(((PartnerCorporateRec)rec).getAccountsFilingDate()));
      aud.setOldValue(dateFmt.format(((PartnerCorporate)ptnr).getAccountsFilingDate()));
      ((PartnerCorporate)ptnr).setAccountsFilingDate(((PartnerCorporateRec)rec).getAccountsFilingDate());
      changedPtnr = true;
     }
     
     
     if(!StringUtils.equals(((PartnerCorporateRec)rec).getCharityNumber(), ((PartnerCorporate)ptnr).getCharityNumber())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_CHARITY_NUM");
      aud.setNewValue(((PartnerCorporateRec)rec).getCharityNumber());
      aud.setOldValue(((PartnerCorporate)ptnr).getCharityNumber());
      ((PartnerCorporate)ptnr).setCharityNumber(((PartnerCorporateRec)rec).getCharityNumber());
      changedPtnr = true;
     }
     
     if((((PartnerCorporateRec)rec).getCharityRegistrationDate() == null && ((PartnerCorporate)ptnr).getCharityRegistrationDate() != null) || 
        (((PartnerCorporateRec)rec).getCharityRegistrationDate() != null && ((PartnerCorporate)ptnr).getCharityRegistrationDate() == null) ||
        (((PartnerCorporateRec)rec).getCharityRegistrationDate() != null && 
       !((PartnerCorporateRec)rec).getCharityRegistrationDate().equals(((PartnerCorporate)ptnr).getCharityRegistrationDate()))){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_CH_REG_DT");
      aud.setNewValue(dateFmt.format(((PartnerCorporateRec)rec).getCharityRegistrationDate()));
      aud.setOldValue(dateFmt.format(((PartnerCorporate)ptnr).getCharityRegistrationDate()));
      ((PartnerCorporate)ptnr).setCharityRegistrationDate(((PartnerCorporateRec)rec).getCharityRegistrationDate());
      changedPtnr = true;
     }
     
     if(!StringUtils.equals(((PartnerCorporateRec)rec).getCompanyNumber(), ((PartnerCorporate)ptnr).getCompanyNumber())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_COMP_NUM");
      aud.setNewValue(((PartnerCorporateRec)rec).getCompanyNumber());
      aud.setOldValue(((PartnerCorporate)ptnr).getCompanyNumber());
      ((PartnerCorporate)ptnr).setCompanyNumber(((PartnerCorporateRec)rec).getCompanyNumber());
      changedPtnr = true;
     }
     
     if(!StringUtils.equals(((PartnerCorporateRec)rec).getLegalName(), ((PartnerCorporate)ptnr).getLegalName())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_LEG_NAME");
      aud.setNewValue(((PartnerCorporateRec)rec).getLegalName());
      aud.setOldValue(((PartnerCorporate)ptnr).getLegalName());
      ((PartnerCorporate)ptnr).setLegalName(((PartnerCorporateRec)rec).getLegalName());
      changedPtnr = true;
     }
     
     if(((PartnerCorporateRec)rec).isCharity() != ((PartnerCorporate)ptnr).isCharity()){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_CHARITY");
      aud.setNewValue(String.valueOf(((PartnerCorporateRec)rec).isCharity()));
      aud.setOldValue(String.valueOf(((PartnerCorporate)ptnr).isCharity()));
      ((PartnerCorporate)ptnr).setCharity(((PartnerCorporateRec)rec).isCharity());
      changedPtnr = true;
     }
     
     if((((PartnerCorporateRec)rec).getHeadOfficeAddress() == null && ((PartnerCorporate)ptnr).getHeadOfficeAddress() != null) ||
      (((PartnerCorporateRec)rec).getHeadOfficeAddress() != null && ((PartnerCorporate)ptnr).getHeadOfficeAddress() == null) ||
      (((PartnerCorporateRec)rec).getHeadOfficeAddress() != null 
       && !Objects.equals(((PartnerCorporateRec)rec).getHeadOfficeAddress().getId(), ((PartnerCorporate)ptnr).getHeadOfficeAddress().getId()))){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_HO_ADDR");
     
     aud.setNewValue(((PartnerCorporateRec)rec).getHeadOfficeAddress().getStreetLine() + " "
       +((PartnerCorporateRec)rec).getHeadOfficeAddress().getPostCode());
     aud.setOldValue(((PartnerCorporate)ptnr).getHeadOfficeAddress().getStreetFormatted()+" "+
       ((PartnerCorporate)ptnr).getHeadOfficeAddress().getPostCode());
     Address addr = em.find(Address.class, ((PartnerCorporateRec)rec).getHeadOfficeAddress().getId());
     ((PartnerCorporate)ptnr).setHeadOfficeAddress(addr);
     changedPtnr = true;
    }
     
    if((((PartnerCorporateRec)rec).getRegisteredOfficeAddress() == null && ((PartnerCorporate)ptnr).getRegisteredOfficeAddress() != null) ||
      (((PartnerCorporateRec)rec).getRegisteredOfficeAddress() != null && ((PartnerCorporate)ptnr).getRegisteredOfficeAddress() == null) ||
      (((PartnerCorporateRec)rec).getRegisteredOfficeAddress() != null 
       && !Objects.equals(((PartnerCorporateRec)rec).getRegisteredOfficeAddress().getId(), ((PartnerCorporate)ptnr).getRegisteredOfficeAddress().getId()))){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_REG_ADDR");
     
     aud.setNewValue(((PartnerCorporateRec)rec).getRegisteredOfficeAddress().getStreetLine() + " "
       +((PartnerCorporateRec)rec).getRegisteredOfficeAddress().getPostCode());
     aud.setOldValue(((PartnerCorporate)ptnr).getRegisteredOfficeAddress().getStreetFormatted()+" "+
       ((PartnerCorporate)ptnr).getRegisteredOfficeAddress().getPostCode());
      Address addr = em.find(Address.class, ((PartnerCorporateRec)rec).getRegisteredOfficeAddress().getId());
      ((PartnerCorporate)ptnr).setRegisteredOfficeAddress(addr);
      changedPtnr = true;
     }
    }else{
     //Changes to person partner
     if(!StringUtils.equals(((PartnerPersonRec)rec).getTitle(), ((PartnerPerson)ptnr).getTitle())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_TITLE");
      aud.setNewValue(((PartnerPersonRec)rec).getTitle());
      aud.setOldValue(((PartnerPerson)ptnr).getTitle());
      ((PartnerPerson)ptnr).setTitle(((PartnerPersonRec)rec).getTitle());
      changedPtnr = true;
     }
     
     if(!StringUtils.equals(((PartnerPersonRec)rec).getFamilyName(), ((PartnerPerson)ptnr).getFamilyName())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_FAM_NM");
      aud.setNewValue(((PartnerPersonRec)rec).getFamilyName());
      aud.setOldValue(((PartnerPerson)ptnr).getFamilyName());
      ((PartnerPerson)ptnr).setFamilyName(((PartnerPersonRec)rec).getFamilyName());
      changedPtnr = true;
     }
     
     if(!StringUtils.equals(((PartnerPersonRec)rec).getFirstName(), ((PartnerPerson)ptnr).getFirstName())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_FIRST_NM");
      aud.setNewValue(((PartnerPersonRec)rec).getFirstName());
      aud.setOldValue(((PartnerPerson)ptnr).getFirstName());
      ((PartnerPerson)ptnr).setFirstName(((PartnerPersonRec)rec).getFirstName());
      changedPtnr = true;
     }
     
     if(!StringUtils.equals(((PartnerPersonRec)rec).getMiddleName(), ((PartnerPerson)ptnr).getMiddleName())){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_MID_NM");
      aud.setNewValue(((PartnerPersonRec)rec).getMiddleName());
      aud.setOldValue(((PartnerPerson)ptnr).getMiddleName());
      ((PartnerPerson)ptnr).setMiddleName(((PartnerPersonRec)rec).getMiddleName());
      changedPtnr = true;
     }
     
     if((((PartnerPersonRec)rec).getDateOfBirth() == null && ((PartnerPerson)ptnr).getDateOfBirth() != null) || 
        (((PartnerPersonRec)rec).getDateOfBirth() != null && ((PartnerPerson)ptnr).getDateOfBirth() == null) ||
        (((PartnerPersonRec)rec).getDateOfBirth() != null && 
       !((PartnerPersonRec)rec).getDateOfBirth().equals(((PartnerPerson)ptnr).getDateOfBirth()))){
      AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
      aud.setFieldName("PTNR_DOB");
      aud.setNewValue(dateFmt.format(((PartnerPersonRec)rec).getDateOfBirth()));
      aud.setOldValue(dateFmt.format(((PartnerPerson)ptnr).getDateOfBirth()));
      ((PartnerPerson)ptnr).setDateOfBirth(((PartnerPersonRec)rec).getDateOfBirth());
      changedPtnr = true;
     }
     
     if((((PartnerPersonRec)rec).getHeadOfficeAddress() == null && ((PartnerPerson)ptnr).getHeadOfficeAddress() != null) ||
      (((PartnerPersonRec)rec).getHeadOfficeAddress() != null && ((PartnerPerson)ptnr).getHeadOfficeAddress() == null) ||
      (((PartnerPersonRec)rec).getHeadOfficeAddress() != null 
       && !Objects.equals(((PartnerPersonRec)rec).getHeadOfficeAddress().getId(), ((PartnerPerson)ptnr).getHeadOfficeAddress().getId()))){
     AuditPartner aud = this.buildAuditPartner(ptnr, chUsr, 'U', pg);
     aud.setFieldName("PTNR_HO_ADDR");
     
     aud.setNewValue(((PartnerPersonRec)rec).getHeadOfficeAddress().getStreetLine() + " "
       +((PartnerPersonRec)rec).getHeadOfficeAddress().getPostCode());
     aud.setOldValue(((PartnerPerson)ptnr).getHeadOfficeAddress().getStreetFormatted()+" "+
       ((PartnerPerson)ptnr).getHeadOfficeAddress().getPostCode());
     Address addr = em.find(Address.class, ((PartnerPersonRec)rec).getHeadOfficeAddress().getId());
     ((PartnerPerson)ptnr).setHeadOfficeAddress(addr);
     changedPtnr = true;
    }
     
    }
    if(changedPtnr){
     ptnr.setChangedBy(chUsr);
     ptnr.setChangedOn(rec.getChangedOn());
    } 
   }
   return ptnr;
  } 
  private PartnerBase buildPartnerBase(PartnerBaseRec p,PartnerBase rec, String pg, boolean newPtnr){
      LOGGER.log(INFO,"buildPartnerBaseRec called with partner rec {0} partner db {1} new partner {2}",
              new Object[]{p,rec,newPtnr});
      LOGGER.log(INFO, "Create by {0} update by {1}", new Object[]{p.getCreatedBy(),p.getChangedBy()});
      LOGGER.log(INFO, "Roles {0}", p.getPartnerRoles());
      boolean changedPtnr = false;
      if(p.getId() == null){
       LOGGER.log(INFO, "Create base partner newPtnr {0}",newPtnr);
       if(p.getDefaultAddress() != null){
        LOGGER.log(INFO, "Add partner Address");
        Address addr = buildAddress(p.getDefaultAddress(), pg);
        rec.setDefaultAddress(addr);
       }
       rec.setCategory(p.getCategory());
       rec.setEmail(p.getEmail());
       rec.setMobileTelephone(p.getMobileTelephone());
       rec.setRef(p.getRef());
       rec.setTelephone(p.getTelephone());
       rec.setUrl(p.getUrl());
       rec.setVatNumber(p.getVatNumber());
       rec.setVatRegisteredDate(p.getVatRegisteredDate());
       rec.setWebAddress(p.getWebAddress());
       
       rec.setTradingName(p.getTradingName());
       
      }else{
       LOGGER.log(INFO, "Change base partner newPtnr {0}",newPtnr);
       // has base field been changed
       User chUsr;
       if(p.getChangedBy() != null){
        chUsr = em.find(User.class, p.getChangedBy().getId(), OPTIMISTIC);
       }else{
        chUsr = em.find(User.class, p.getCreatedBy().getId(), OPTIMISTIC);
       }
/*
       if((p.getDefaultAddress() == null && rec.getDefaultAddress() != null ) ||
          (p.getDefaultAddress() != null && rec.getDefaultAddress() == null ) ||
           (p.getDefaultAddress() != null && !Objects.equals(p.getDefaultAddress().getId(), rec.getDefaultAddress().getId()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_DEF_ADDR");
        aud.setNewValue(p.getDefaultAddress().getAddrRef());
        if(rec.getDefaultAddress() != null){
         aud.setOldValue(rec.getDefaultAddress().getAddrRef());
        }
        Address adr;
        
        adr = this.buildAddress(p.getDefaultAddress(), pg);
         
        rec.setDefaultAddress(adr);
        changedPtnr = true;
       }else if((p.getDefaultAddress() != null && rec.getDefaultAddress() == null ) &&
        (Objects.equals(p.getDefaultAddress().getId(), rec.getDefaultAddress().getId())) ){
        LOGGER.log(INFO, "buildAddress - default");
        Address adr = buildAddress(p.getDefaultAddress(),  pg);
        rec.setDefaultAddress(adr);
       }
       
       */
       if((p.getCategory() == null && rec.getCategory() != null ) ||
          (p.getCategory() != null && rec.getCategory() == null ) ||
           (p.getCategory() != null && !p.getCategory().equals(rec.getCategory()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_CAT");
        aud.setNewValue(p.getCategory());
        aud.setOldValue(rec.getCategory());
        rec.setCategory(p.getCategory());
        changedPtnr = true;
       }
       
       if((p.getEmail() == null && rec.getEmail() != null ) ||
          (p.getEmail() != null && rec.getEmail() == null ) ||
           (p.getEmail() != null && !p.getEmail().equals(rec.getEmail()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_EMAIL");
        aud.setNewValue(p.getEmail());
        aud.setOldValue(rec.getEmail());
        rec.setEmail(p.getEmail());
        changedPtnr = true;
       }
       
       if((p.getMobileTelephone() == null && rec.getMobileTelephone() != null ) ||
          (p.getMobileTelephone() != null && rec.getMobileTelephone() == null ) ||
           (p.getMobileTelephone() != null && !p.getMobileTelephone().equals(rec.getMobileTelephone()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_EMAIL");
        aud.setNewValue(p.getMobileTelephone());
        aud.setOldValue(rec.getMobileTelephone());
        rec.setMobileTelephone(p.getMobileTelephone());
        changedPtnr = true;
       }
       
       if((p.getRef() == null && rec.getRef() != null ) ||
          (p.getRef() != null && rec.getRef() == null ) ||
           (p.getRef() != null && !p.getRef().equals(rec.getRef()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_REF");
        aud.setNewValue(p.getRef());
        aud.setOldValue(rec.getRef());
        rec.setRef(p.getRef());
        changedPtnr = true;
       }
       
       if((p.getTelephone() == null && rec.getTelephone() != null ) ||
          (p.getTelephone() != null && rec.getTelephone() == null ) ||
           (p.getTelephone() != null && !p.getTelephone().equals(rec.getTelephone()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_REF");
        aud.setNewValue(p.getTelephone());
        aud.setOldValue(rec.getTelephone());
        rec.setTelephone(p.getTelephone());
        changedPtnr = true;
       }
       
       if((p.getTradingName() == null && rec.getTradingName() != null ) ||
          (p.getTradingName() != null && rec.getTradingName() == null ) ||
           (p.getTradingName() != null && !p.getTradingName().equals(rec.getTradingName()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_REF");
        aud.setNewValue(p.getTradingName());
        aud.setOldValue(rec.getTradingName());
        rec.setTradingName(p.getTradingName());
        changedPtnr = true;
       }
       
       if((p.getUrl() == null && rec.getUrl() != null ) ||
          (p.getUrl() != null && rec.getUrl() == null ) ||
           (p.getUrl() != null && !p.getUrl().equals(rec.getUrl()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_REF");
        aud.setNewValue(p.getUrl());
        aud.setOldValue(rec.getUrl());
        rec.setTradingName(p.getUrl());
        changedPtnr = true;
       }
       
        if((p.getVatNumber() == null && rec.getVatNumber() != null ) ||
          (p.getVatNumber() != null && rec.getVatNumber() == null ) ||
           (p.getVatNumber() != null && !p.getVatNumber().equals(rec.getVatNumber()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_VAT_NUM");
        aud.setNewValue(p.getVatNumber());
        aud.setOldValue(rec.getVatNumber());
        rec.setVatNumber(p.getVatNumber());
        changedPtnr = true;
       }
        
       if((p.getVatRegisteredDate() == null && rec.getVatRegisteredDate() != null ) ||
          (p.getVatRegisteredDate() != null && rec.getVatRegisteredDate() == null ) ||
           (p.getVatRegisteredDate() != null && !p.getVatRegisteredDate().equals(rec.getVatRegisteredDate()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_VAT_REG_DT");
        aud.setNewValue(p.getVatRegisteredDate().toString());
        aud.setOldValue(rec.getVatRegisteredDate().toString());
        rec.setVatRegisteredDate(p.getVatRegisteredDate());
        changedPtnr = true;
       }
       
       if((p.getWebAddress() == null && rec.getWebAddress() != null ) ||
          (p.getWebAddress() != null && rec.getWebAddress() == null ) ||
           (p.getWebAddress() != null && !p.getWebAddress().equals(rec.getWebAddress()))){
        AuditPartner aud = this.buildAuditPartner(rec, chUsr, 'U', pg);
        aud.setFieldName("PTNR_VAT_NUM");
        aud.setNewValue(p.getWebAddress());
        aud.setOldValue(rec.getWebAddress());
        rec.setWebAddress(p.getWebAddress());
        changedPtnr = true;
       }
        
       
      }
      
      return rec;
      }
      
     
    
    public PartnerBaseRec buildPartnerBaseRecPvt(PartnerBase p){
      return buildPartnerBaseRec(p);
    }
    
    private PartnerBaseRec buildPartnerBaseRec(PartnerBase p){
      LOGGER.log(INFO,"buildPartnerBaseRec called with {0}",p);
      PartnerBaseRec rec = new PartnerBaseRec();
      if(p.getCreatedBy() != null){
        UserRec usr = this.usrDM.getUserRecPvt(p.getCreatedBy());
        rec.setCreatedBy(usr);
        rec.setCreatedDate(p.getCreatedDate());
      }
      if(p.getChangedBy()!= null){
        UserRec usr = this.usrDM.getUserRecPvt(p.getChangedBy());
        rec.setChangedBy(usr);
        rec.setChangedOn(p.getChangedOn());
      }
      if(p.getDefaultAddress() != null){
        AddressRec addr = this.buildAddressRec(p.getDefaultAddress());
        rec.setDefaultAddress(addr);
      }
      
      if(p.getCountry() != null){
       CountryRec c = this.buildCountryRec(p.getCountry());
       rec.setCountry(c);
      }
      rec.setId(p.getId());
      rec.setMobileTelephone(p.getMobileTelephone());
      rec.setRef(p.getRef());
      rec.setRevision(p.getRevision());
      rec.setTelephone(p.getTelephone());
      rec.setUrl(p.getUrl());
      rec.setVatNumber(p.getVatNumber());
      rec.setVatRegisteredDate(p.getVatRegisteredDate());
      rec.setWebAddress(p.getWebAddress());
      rec.setCategory(p.getCategory());
      rec.setEmail(p.getEmail());

      return rec;

    }

    public PartnerPersonRec buildPartnerPersonRecPvt(PartnerPerson p){
     LOGGER.log(INFO, "buildPartnerPersonRecPvt called with {0}", p);
     return buildPartnerPersonRec(p);
    }

    
    private PartnerPersonRec buildPartnerPersonRec(PartnerPerson p){
      LOGGER.log(INFO,"buildPartnerCorporateRec called with {0}",p);
      PartnerPersonRec rec = new PartnerPersonRec();
      if(p == null){
       return rec;
      }
      rec.setFamilyName(p.getFamilyName());
      rec.setDateOfBirth(p.getDateOfBirth());
      rec.setFirstName(p.getFirstName());
      rec.setMiddleName(p.getMiddleName());
      
      if(StringUtils.isNotBlank(p.getFamilyName())){
       if(StringUtils.isNotBlank(p.getFirstName())){
        StringBuilder sb = new StringBuilder();
        sb.append(p.getFamilyName());
        sb.append(", ");
        sb.append(p.getFirstName());
        rec.setDisplayName(sb.toString());
       }else{
        rec.setDisplayName(p.getFamilyName());
       }
      }

      if(p.getCreatedBy() != null){
        UserRec usr = this.usrDM.getUserRecPvt(p.getCreatedBy());
        rec.setCreatedBy(usr);
        rec.setCreatedDate(p.getCreatedDate());
      }
      if(p.getChangedBy() != null){
        UserRec usr = this.usrDM.getUserRecPvt(p.getChangedBy());
        rec.setChangedBy(usr);
        rec.setChangedOn(p.getChangedOn());
      }
      if(p.getDefaultAddress() != null){
        AddressRec addr = this.buildAddressRec(p.getDefaultAddress());
        rec.setDefaultAddress(addr);
      }
      rec.setId(p.getId());
      rec.setMobileTelephone(p.getMobileTelephone());
      rec.setRef(p.getRef());
      rec.setRevision(p.getRevision());
      rec.setTelephone(p.getTelephone());
      rec.setUrl(p.getUrl());
      rec.setVatNumber(p.getVatNumber());
      rec.setVatRegisteredDate(p.getVatRegisteredDate());
      rec.setWebAddress(p.getWebAddress());
      rec.setTitle(p.getTitle());
      rec.setTradingName(p.getTradingName());


      return rec;
    }
 private PartnerRole buildPartnerRole(PartnerRoleRec rec, String pg){
  PartnerRole role;
  boolean newRole = false;
  boolean changedRole = false;
  if(rec.getId() == null){
   role = new PartnerRole();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId());
   role.setCreatedBy(crUsr);
   role.setCreatedOn(rec.getCreatedOn());
   em.persist(role);
   AuditPartnerRole aud = this.buildAuditPartnerRole(role, crUsr, AuditPartnerRole.ACTION_INSERT, pg);
   aud.setNewValue(rec.getRoleCode());
   newRole = true;
  }else{
   role = em.find(PartnerRole.class, rec.getId());
   
  }
  if(newRole){
   role.setRoleCode(rec.getRoleCode());
   role.setRoleName(rec.getRoleName());
   role.setInUse(rec.isInUse());
   role.setTaxable(rec.isTaxable());
   role.setUserRole(rec.isUserRole());
   
  }else{
   User chUsr = em.find(User.class, rec.getChangedBy().getId());
   
   if(!StringUtils.equals(rec.getRoleCode(), role.getRoleCode())){
    AuditPartnerRole aud = this.buildAuditPartnerRole(role, chUsr, AuditPartnerRole.ACTION_UPDATE, pg);
    aud.setFieldName("PTNR_ROLE_CODE");
    aud.setNewValue(rec.getRoleCode());
    aud.setOldValue(role.getRoleCode());
    role.setRoleCode(rec.getRoleCode());
    changedRole = true;
   }
   if(!StringUtils.equals(rec.getRoleName(), role.getRoleName())){
    AuditPartnerRole aud = this.buildAuditPartnerRole(role, chUsr, AuditPartnerRole.ACTION_UPDATE, pg);
    aud.setFieldName("PTNR_ROLE_NAME");
    aud.setNewValue(rec.getRoleName());
    aud.setOldValue(role.getRoleName());
    role.setRoleName(rec.getRoleName());
    changedRole = true;
   }
   if(rec.isInUse() != role.isInUse()){
    AuditPartnerRole aud = this.buildAuditPartnerRole(role, chUsr, AuditPartnerRole.ACTION_UPDATE, pg);
    aud.setFieldName("PTNR_ROLE_IN_USE");
    aud.setNewValue(String.valueOf(rec.isInUse()));
    aud.setOldValue(String.valueOf(role.isInUse()));
    role.setInUse(rec.isInUse());
    changedRole = true;
   }
   if(rec.isTaxable()!= role.isTaxable()){
    AuditPartnerRole aud = this.buildAuditPartnerRole(role, chUsr, AuditPartnerRole.ACTION_UPDATE, pg);
    aud.setFieldName("PTNR_ROLE_TAX");
    aud.setNewValue(String.valueOf(rec.isTaxable()));
    aud.setOldValue(String.valueOf(role.isTaxable()));
    role.setTaxable(rec.isTaxable());
    changedRole = true;
   }
   if(rec.isUserRole()!= role.isUserRole()){
    AuditPartnerRole aud = this.buildAuditPartnerRole(role, chUsr, AuditPartnerRole.ACTION_UPDATE, pg);
    aud.setFieldName("PTNR_ROLE_USR");
    aud.setNewValue(String.valueOf(rec.isUserRole()));
    aud.setOldValue(String.valueOf(role.isUserRole()));
    role.setUserRole(rec.isUserRole());
    changedRole = true;
   }
   if(changedRole){
    role.setChangedBy(chUsr);
    role.setChangedOn(rec.getChangedOn());
   }
  }
  
  return role;
 }
 
 private PartnerRoleRec buildPartnerRoleRec(PartnerRole role){
  PartnerRoleRec rec = new PartnerRoleRec();
  
  rec.setId(role.getPtnrRoleId());
  rec.setRoleCode(role.getRoleCode());
  rec.setRoleName(role.getRoleName());
  rec.setInUse(role.isInUse());
  rec.setTaxable(role.isTaxable());
  rec.setUserRole(role.isUserRole());
  
  UserRec crUsr = this.usrDM.getUserRecPvt(role.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(role.getCreatedOn());
  
  if(role.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(role.getCreatedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(role.getChangedOn());
  }
  return rec;
 }
 
 
    public PartnerBaseRec getPartnerAddresses(PartnerBaseRec rec){
     if(rec.getClass().getSimpleName().equals("PartnerPersonRec")){
      PartnerPerson pers = em.find(PartnerPerson.class, rec.getId(), OPTIMISTIC);
      if(pers.getDefaultAddress() != null){
       AddressRec defAddr = this.buildAddressRec(pers.getDefaultAddress());
       rec.setDefaultAddress(defAddr);
       return rec;
      }
     }else{
      PartnerCorporateRec corpRec = (PartnerCorporateRec)rec;
      PartnerCorporate corp = em.find(PartnerCorporate.class, rec.getId(), OPTIMISTIC);
      if(corp.getDefaultAddress() != null){
       AddressRec defAddr = this.buildAddressRec(corp.getDefaultAddress());
       corpRec.setDefaultAddress(defAddr);
      }
      if(corp.getHeadOfficeAddress() != null){
       AddressRec hoAddr = this.buildAddressRec(corp.getHeadOfficeAddress());
       corpRec.setHeadOfficeAddress(hoAddr);
      }
      if(corp.getRegisteredOfficeAddress() != null){
       AddressRec regAddr = this.buildAddressRec(corp.getRegisteredOfficeAddress());
       corpRec.setRegisteredOfficeAddress(regAddr);
      }
      return corpRec;
     }
     
     return rec;
    }
    public PartnerCorporateRec getPartnerCorporateRec(PartnerCorporate ptnr){
      return this.buildPartnerCorporateRec(ptnr);
    }
    
    public PartnerBaseRec getPartnerById(Long id){
     LOGGER.log(INFO, "getPartnerById called with {0}", id);
     
     PartnerBase ptnrBase = em.find(PartnerBase.class, id);
     String ptnrType = ptnrBase.getClass().getSimpleName();
     if(ptnrType.equals("PartnerPerson")){
      PartnerPersonRec persRec = this.buildPartnerPersonRec((PartnerPerson)ptnrBase);
      return persRec;
     }else if(ptnrType.equals("PartnerCorporate")){
      PartnerCorporateRec corpRec = this.buildPartnerCorporateRec((PartnerCorporate)ptnrBase);
      return corpRec;
     }
     LOGGER.log(INFO, "invalid partner type {0}", ptnrType);
     return null;
    }
    private PartnerCorporateRec buildPartnerCorporateRec(PartnerCorporate ptnr){
      LOGGER.log(INFO,"buildPartnerCorporateRec called with {0}",ptnr);
      PartnerCorporateRec rec = new PartnerCorporateRec();
      rec.setAccountsFilingDate(ptnr.getAccountsFilingDate());
      rec.setCharityNumber(ptnr.getCharityNumber());
      rec.setCharityRegistrationDate(ptnr.getCharityRegistrationDate());
      rec.setCompanyNumber(ptnr.getCompanyNumber());
      UserRec usr = usrDM.getUserRecPvt(ptnr.getCreatedBy());
      rec.setCreatedBy(usr);
      rec.setCreatedDate(ptnr.getCreatedDate());
      if(ptnr.getDefaultAddress() != null){
        AddressRec defAddr = this.buildAddressRec(ptnr.getDefaultAddress());
        rec.setDefaultAddress(defAddr);
      }
      if(ptnr.getHeadOfficeAddress() != null){
        AddressRec headAddr = this.buildAddressRec(ptnr.getHeadOfficeAddress());
        rec.setHeadOfficeAddress(headAddr);
      }
      rec.setId(ptnr.getId());
      rec.setCharity(ptnr.isCharity());
      if(ptnr.getChangedBy() != null){
        usr = usrDM.getUserRecPvt(ptnr.getChangedBy());
        rec.setChangedBy(usr);
        rec.setChangedOn(ptnr.getChangedOn());
      }
      rec.setLegalName(ptnr.getLegalName());
      rec.setRef(ptnr.getRef());
      if(ptnr.getRegisteredOfficeAddress() != null){
        AddressRec regAddr = this.buildAddressRec(ptnr.getRegisteredOfficeAddress());
        rec.setRegisteredOfficeAddress(regAddr);
      }
      rec.setRevision(ptnr.getRevision());
      rec.setTradingName(ptnr.getTradingName());
      rec.setVatNumber(ptnr.getVatNumber());
      rec.setVatRegisteredDate(ptnr.getVatRegisteredDate());
      if(StringUtils.isNotBlank(ptnr.getTradingName())){
       rec.setDisplayName(ptnr.getTradingName());
      }else{
       rec.setDisplayName(ptnr.getLegalName());
      }
      

      return rec;
    }

    public PartnerCorporate buildPartnerCorporatePvt(PartnerCorporateRec ptnr, String pg  ){
      return buildPartnerCorporate(ptnr, pg);
    }
    
    private PartnerCorporate buildPartnerCorporate(PartnerCorporateRec ptnr, String pg  ){
      LOGGER.log(INFO,"buildPartnerCorporate called with {0}",ptnr);
      LOGGER.log(INFO,"buildPartnerCorporate called with id{0}",ptnr.getId());
      LOGGER.log(INFO,"create by  id {0}",ptnr.getCreatedBy().getId());
      boolean newPtnr = false;
      boolean changedPtnr = false;
      PartnerCorporate dbPtnr;
      if(ptnr.getId() == null){
      
       newPtnr = true;
      }
      
      if(newPtnr ){
        LOGGER.log(INFO,"build new Partner Corp");
        dbPtnr = new PartnerCorporate();
        User crUsr = em.find(User.class, ptnr.getCreatedBy().getId(), OPTIMISTIC);
        dbPtnr.setCreatedBy(crUsr);
        dbPtnr.setCreatedDate(ptnr.getCreatedDate());
        em.persist(dbPtnr);
        AuditPartner aud = this.buildAuditPartner(dbPtnr, crUsr, 'I', pg);
        aud.setNewValue(ptnr.getRef());
        LOGGER.log(INFO,"build new Partner Corp id is {0}",dbPtnr.getId());
        
        LOGGER.log(INFO,"newPtnr is {0}",newPtnr);
      }else{
       LOGGER.log(INFO, "Find partner with id {0}", ptnr.getId());
        dbPtnr = em.find(PartnerCorporate.class, ptnr.getId());
      }
      // build base partner
      ptnr.setId(dbPtnr.getId());
      LOGGER.log(INFO, "Partner id before build base {0} newPtnr {1}", new Object[]{dbPtnr.getId(),newPtnr});
        dbPtnr = (PartnerCorporate)buildPartnerBase(ptnr, dbPtnr,pg,newPtnr);
        
      LOGGER.log(INFO, "Partner id after build base {0}", dbPtnr.getId());
      if(newPtnr){
       // new corp
       ptnr.setId(dbPtnr.getId());
       dbPtnr.setAccountsFilingDate(ptnr.getAccountsFilingDate());
       dbPtnr.setCharityNumber(ptnr.getCharityNumber());
       dbPtnr.setCharityRegistrationDate(ptnr.getCharityRegistrationDate());
       dbPtnr.setCompanyNumber(ptnr.getCompanyNumber());
       dbPtnr.setTradingName(ptnr.getTradingName());
       dbPtnr.setLegalName(ptnr.getTradingName());
       dbPtnr.setCharity(ptnr.isCharity());
       
       if(ptnr.getHeadOfficeAddress() != null){
        
         Address ho = this.buildAddress(ptnr.getHeadOfficeAddress(),  pg);
         dbPtnr.setHeadOfficeAddress(ho);
        
       }
       dbPtnr.setCharity(ptnr.isCharity());
       dbPtnr.setLegalName(ptnr.getLegalName());
       if(ptnr.getRegisteredOfficeAddress() != null){
        
         Address ho = this.buildAddress(ptnr.getRegisteredOfficeAddress(),  pg);
         dbPtnr.setRegisteredOfficeAddress(ho);
        
       }
        
      }else{
       // changed Corp fiedlds
       User chUsr = em.find(User.class, ptnr.getChangedBy().getId(), OPTIMISTIC);
       
       if(ptnr.isCharity() != dbPtnr.isCharity()){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_CHARITY");
        aud.setNewValue(String.valueOf(ptnr.isCharity()));
        aud.setOldValue(String.valueOf(ptnr.isCharity()));
        dbPtnr.setCharity(ptnr.isCharity());
        changedPtnr = true;
       }
       
       if((ptnr.getAccountsFilingDate() == null && dbPtnr.getAccountsFilingDate() != null) ||
          (ptnr.getAccountsFilingDate() != null && dbPtnr.getAccountsFilingDate() == null) ||
          (ptnr.getAccountsFilingDate() != null 
               && ptnr.getAccountsFilingDate().equals(dbPtnr.getAccountsFilingDate()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_ACS_FILE_DT");
        aud.setNewValue(ptnr.getAccountsFilingDate().toString());
        if(dbPtnr.getAccountsFilingDate() != null){
        aud.setOldValue(dbPtnr.getAccountsFilingDate().toString());
        }
        dbPtnr.setAccountsFilingDate(ptnr.getAccountsFilingDate());
        changedPtnr = true;
       }
       
       if((ptnr.getCharityNumber() == null && dbPtnr.getCharityNumber() != null) ||
          (ptnr.getCharityNumber() != null && dbPtnr.getCharityNumber() == null) ||
          (ptnr.getCharityNumber() != null 
               && ptnr.getCharityNumber().equals(dbPtnr.getCharityNumber()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_CHARITY_NUM");
        aud.setNewValue(ptnr.getCharityNumber());
        aud.setOldValue(dbPtnr.getCharityNumber());
        dbPtnr.setCharityNumber(ptnr.getCharityNumber());
        changedPtnr = true;
       }
       
       
       if((ptnr.getCharityRegistrationDate() == null && dbPtnr.getCharityRegistrationDate() != null) ||
          (ptnr.getCharityRegistrationDate() != null && dbPtnr.getCharityRegistrationDate() == null) ||
          (ptnr.getCharityRegistrationDate() != null 
               && ptnr.getCharityRegistrationDate().equals(dbPtnr.getCharityRegistrationDate()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_CH_REG_DT");
        aud.setNewValue(ptnr.getCharityRegistrationDate().toString());
        if(dbPtnr.getCharityRegistrationDate() != null){
        aud.setOldValue(dbPtnr.getCharityRegistrationDate().toString());
        }
        dbPtnr.setCharityRegistrationDate(ptnr.getCharityRegistrationDate());
        changedPtnr = true;
       }
       
       if((ptnr.getCompRegistrationDate() == null && dbPtnr.getCompRegistrationDate() != null) ||
          (ptnr.getCompRegistrationDate() != null && dbPtnr.getCompRegistrationDate() == null) ||
          (ptnr.getCompRegistrationDate() != null 
               && ptnr.getCompRegistrationDate().equals(dbPtnr.getCompRegistrationDate()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_COMP_REG_DT");
        aud.setNewValue(ptnr.getCompRegistrationDate().toString());
        if(dbPtnr.getCompRegistrationDate() != null){
        aud.setOldValue(dbPtnr.getCompRegistrationDate().toString());
        }
        dbPtnr.setCompRegistrationDate(ptnr.getCompRegistrationDate());
        changedPtnr = true;
       }
       
       if((ptnr.getCompanyNumber() == null && dbPtnr.getCompanyNumber() != null) ||
          (ptnr.getCompanyNumber() != null && dbPtnr.getCompanyNumber() == null) ||
          (ptnr.getCompanyNumber() != null 
               && ptnr.getCompanyNumber().equals(dbPtnr.getCompanyNumber()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_COMP_NUM");
        aud.setNewValue(ptnr.getCompanyNumber());
        aud.setOldValue(dbPtnr.getCompanyNumber());
        dbPtnr.setCompanyNumber(ptnr.getCompanyNumber());
        changedPtnr = true;
       }
       
       if((ptnr.getHeadOfficeAddress() == null && dbPtnr.getHeadOfficeAddress() != null) ||
          (ptnr.getHeadOfficeAddress() != null && dbPtnr.getHeadOfficeAddress() == null) ||
          (ptnr.getHeadOfficeAddress() != null 
               && !Objects.equals(ptnr.getHeadOfficeAddress().getId(), dbPtnr.getHeadOfficeAddress().getId()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_HO_ADDR");
        aud.setNewValue(ptnr.getHeadOfficeAddress().getAddrRef());
        if(dbPtnr.getHeadOfficeAddress() != null){
        aud.setOldValue(dbPtnr.getHeadOfficeAddress().getAddrRef());
        }
        Address addr = buildAddress(ptnr.getHeadOfficeAddress(),  pg);
        dbPtnr.setHeadOfficeAddress(addr);
        changedPtnr = true;
       }else if((ptnr.getHeadOfficeAddress() != null && dbPtnr.getHeadOfficeAddress() != null) &&
               (Objects.equals(ptnr.getHeadOfficeAddress().getId(), dbPtnr.getHeadOfficeAddress().getId()))){
        Address addr = buildAddress(ptnr.getHeadOfficeAddress(),  pg);
        dbPtnr.setHeadOfficeAddress(addr);
       }
       
       if((ptnr.getLegalName() == null && dbPtnr.getLegalName() != null) ||
          (ptnr.getLegalName() != null && dbPtnr.getLegalName() == null) ||
          (ptnr.getLegalName() != null 
               && ptnr.getLegalName().equals(dbPtnr.getLegalName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_LEG_NAME");
        aud.setNewValue(ptnr.getLegalName());
        aud.setOldValue(dbPtnr.getLegalName());
        dbPtnr.setLegalName(ptnr.getLegalName());
        changedPtnr = true;
       }
       
       if((ptnr.getRegisteredOfficeAddress() == null && dbPtnr.getRegisteredOfficeAddress() != null) ||
          (ptnr.getRegisteredOfficeAddress() != null && dbPtnr.getRegisteredOfficeAddress() == null) ||
          (ptnr.getRegisteredOfficeAddress() != null 
               && !Objects.equals(ptnr.getRegisteredOfficeAddress().getId(), dbPtnr.getRegisteredOfficeAddress().getId()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_REG_ADDR");
        aud.setNewValue(ptnr.getRegisteredOfficeAddress().getAddrRef());
        if(dbPtnr.getRegisteredOfficeAddress() != null){
        aud.setOldValue(dbPtnr.getRegisteredOfficeAddress().getAddrRef());
        
        }
        
        Address addr =  buildAddress(ptnr.getRegisteredOfficeAddress(),  pg);//em.find(Address.class, ptnr.getRegisteredOfficeAddress().getId(), OPTIMISTIC);
        dbPtnr.setRegisteredOfficeAddress(addr);
        changedPtnr = true;
       }else if((ptnr.getRegisteredOfficeAddress() == null && dbPtnr.getRegisteredOfficeAddress() != null) &&
              (Objects.equals(ptnr.getRegisteredOfficeAddress().getId(), dbPtnr.getRegisteredOfficeAddress().getId())) ){
        Address addr =  buildAddress(ptnr.getRegisteredOfficeAddress(),  pg);//em.find(Address.class, ptnr.getRegisteredOfficeAddress().getId(), OPTIMISTIC);
        dbPtnr.setRegisteredOfficeAddress(addr);
       }
       
       if((ptnr.getTradingName() == null && dbPtnr.getTradingName() != null) ||
          (ptnr.getTradingName() != null && dbPtnr.getTradingName() == null) ||
          (ptnr.getTradingName() != null 
               && ptnr.getTradingName().equals(dbPtnr.getTradingName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_TR_NAME");
        aud.setNewValue(ptnr.getTradingName());
        aud.setOldValue(dbPtnr.getTradingName());
        dbPtnr.setTradingName(ptnr.getTradingName());
        changedPtnr = true;
       }
       
       if(changedPtnr){
        dbPtnr.setChangedBy(chUsr);
        dbPtnr.setChangedOn(ptnr.getChangedOn());
       }
       
       
      }
        
      LOGGER.log(INFO, "dbPtnr id is {0}", ptnr.getId());
      return dbPtnr;
    }

    public PartnerPerson buildPartnerPersonDM(PartnerPersonRec ptnr, String pg  ){
     if(!trans.isActive()){
      trans.begin();
     }
     PartnerPerson cont  = this.buildPartnerPerson(ptnr, pg);
     trans.commit();
     return cont;
    }
  private PartnerPerson buildPartnerPerson(PartnerPersonRec ptnr, String pg  ){
      LOGGER.log(INFO,"buildPartnerPerson called with ptnr id {0}",ptnr.getId());
      boolean newPtnr = false;
      boolean changedPtnr = false;
      PartnerPerson dbPtnr;
      if(ptnr.getId() == null ){
        dbPtnr = new PartnerPerson();
        User crUsr = em.find(User.class, ptnr.getCreatedBy().getId(), OPTIMISTIC);
        dbPtnr.setCreatedBy(crUsr);
        dbPtnr.setCreatedDate(ptnr.getCreatedDate());
        em.persist(dbPtnr);
        AuditPartner aud = this.buildAuditPartner(dbPtnr, crUsr, 'I', pg);
        aud.setNewValue(ptnr.getRef());
        LOGGER.log(INFO,"build new Partner person id is {0}",dbPtnr.getId());
        newPtnr = true;
        
      }else{
        dbPtnr = em.find(PartnerPerson.class, ptnr.getId());
      }
      LOGGER.log(INFO, "newPtnr {0}", newPtnr);
      // build base partner
      dbPtnr = (PartnerPerson)this.buildPartnerBase(ptnr, dbPtnr,pg,newPtnr);
      if(newPtnr){
      dbPtnr.setTitle(ptnr.getTitle());
      dbPtnr.setTradingName(ptnr.getTradingName());
      dbPtnr.setDateOfBirth(ptnr.getDateOfBirth());
      dbPtnr.setFamilyName(ptnr.getFamilyName());
      dbPtnr.setFirstName(ptnr.getFirstName());
      dbPtnr.setMiddleName(ptnr.getMiddleName());
      }else{
       User chUsr = em.find(User.class, ptnr.getChangedBy().getId(), OPTIMISTIC);
       
       if((ptnr.getTitle() == null && dbPtnr.getTitle() != null) ||
          (ptnr.getTitle() != null && dbPtnr.getTitle() == null) ||
          (ptnr.getTitle() != null && !ptnr.getTitle().equals(dbPtnr.getTitle()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_TITLE");
        aud.setNewValue(ptnr.getTitle());
        aud.setOldValue(dbPtnr.getTitle());
        dbPtnr.setTitle(ptnr.getTitle());
        changedPtnr = true;
       }
       
       if((ptnr.getTradingName() == null && dbPtnr.getTradingName() != null) ||
          (ptnr.getTradingName() != null && dbPtnr.getTradingName() == null) ||
          (ptnr.getTradingName() != null && !ptnr.getTradingName().equals(dbPtnr.getTradingName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_TR_NAME");
        aud.setNewValue(ptnr.getTradingName());
        aud.setOldValue(dbPtnr.getTradingName());
        dbPtnr.setTitle(ptnr.getTradingName());
        changedPtnr = true;
       }
       
       if((ptnr.getDateOfBirth() == null && dbPtnr.getDateOfBirth() != null) ||
          (ptnr.getDateOfBirth() != null && dbPtnr.getDateOfBirth() == null) ||
          (ptnr.getDateOfBirth() != null && !ptnr.getDateOfBirth().equals(dbPtnr.getDateOfBirth()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_DOB");
        if(ptnr.getDateOfBirth() != null){
        aud.setNewValue(ptnr.getDateOfBirth().toString());
        }
        if(dbPtnr.getDateOfBirth() != null){
        aud.setOldValue(dbPtnr.getDateOfBirth().toString());
        }
        dbPtnr.setDateOfBirth(ptnr.getDateOfBirth());
        changedPtnr = true;
       }
       
       if((ptnr.getFamilyName() == null && dbPtnr.getFamilyName() != null) ||
          (ptnr.getFamilyName() != null && dbPtnr.getFamilyName() == null) ||
          (ptnr.getFamilyName() != null && !ptnr.getFamilyName().equals(dbPtnr.getFamilyName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_FAM_NM");
        aud.setNewValue(ptnr.getFamilyName());
        aud.setOldValue(dbPtnr.getFamilyName());
        dbPtnr.setFamilyName(ptnr.getFamilyName());
        changedPtnr = true;
       }
       
       if((ptnr.getFamilyName() == null && dbPtnr.getFamilyName() != null) ||
          (ptnr.getFirstName() != null && dbPtnr.getFirstName() == null) ||
          (ptnr.getFirstName() != null && !ptnr.getFirstName().equals(dbPtnr.getFirstName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_FIRST_NM");
        aud.setNewValue(ptnr.getFirstName());
        aud.setOldValue(dbPtnr.getFirstName());
        dbPtnr.setFirstName(ptnr.getFirstName());
        changedPtnr = true;
       }
       
       if((ptnr.getMiddleName() == null && dbPtnr.getMiddleName() != null) ||
          (ptnr.getMiddleName() != null && dbPtnr.getMiddleName() == null) ||
          (ptnr.getMiddleName() != null && !ptnr.getMiddleName().equals(dbPtnr.getMiddleName()))){
        AuditPartner aud = this.buildAuditPartner(dbPtnr, chUsr, 'U', pg);
        aud.setFieldName("PTNR_MID_NM");
        aud.setNewValue(ptnr.getMiddleName());
        aud.setOldValue(dbPtnr.getMiddleName());
        dbPtnr.setFirstName(ptnr.getMiddleName());
        changedPtnr = true;
       }
       
       if(changedPtnr){
        dbPtnr.setChangedBy(chUsr);
        dbPtnr.setChangedOn(ptnr.getChangedOn());
       }
       
      }
      return dbPtnr;
  }

  public AddressRec addressUpdate(AddressRec addr, UserRec usr, String source){
   LOGGER.log(INFO, "MasterDataDM.addressUpdate called with {0}", addr);
   if(!trans.isActive()){
    trans.begin();
   }
   LOGGER.log(INFO, "addressrec country {0}", addr.getCountry());
   Address address = this.buildAddress(addr, source);
  
   if(address == null){
    trans.rollback();
   }else{
    trans.commit();
   }
   return addr;
  }
  
  public CountryRec countryUpdate(CountryRec rec, UserRec usr, String pg){
   if(!trans.isActive()){
    trans.begin();
   }
   Country cntry = buildCountry(rec, usr, pg);
   rec.setId(cntry.getId());
   trans.commit();
   return rec;
  }
    public AddressRec createAddress(AddressRec rec, UserRec usr, String source) throws BacException {
        LOGGER.log(INFO, "DB createAddress called with {0}", rec.getPostCode());
        if(rec.getPostCode().isEmpty()){
         return null;
        }
        if(!trans.isActive()){
         trans.begin();
        }
        Address addr = this.buildAddress(rec,source);
        rec.setId(addr.getId());
        trans.commit();
        return rec;

        

    }



    public Address getAddressDB(AddressRec rec) throws BacException {
        Address addr = new Address();
        return addr;
    }

    public Address createAddressPvt(AddressRec rec,  String source) throws BacException {
        // TODO: first check address does not already exist
        LOGGER.log(INFO, "DB createAddress Pvt called with {0}", rec);
        if(!trans.isActive()){
         trans.begin();
        }
        Address addr = this.buildAddress(rec,  source);
        LOGGER.log(INFO, "Address id {0}", addr.getId());
        trans.commit();
        return addr;
    }

  public AddressRec getAddressRec(Address address) throws BacException {
   
    return  this.buildAddressRec(address);
  }
/**
 * Get list of Corporate partners by Trading name
 * @param tradingName
 * @return
 * @throws BacException
 */
  public List<PartnerCorporateRec> getCorpPartnersByTradingName(String tradingName) throws BacException {
    LOGGER.log(INFO, "DB getCorpPartnersByTradingName called with name: {0}", tradingName);
    List<PartnerCorporateRec> corpList = new ArrayList<>();

    if(tradingName == null){
      tradingName = new String();
    }
    String searchName = tradingName.replace(" ", "");
    searchName = searchName.trim();
    
    searchName = StringUtils.upperCase(searchName);
    searchName = searchName + '%';
    searchName = searchName.trim();
    searchName = searchName.toUpperCase();
    LOGGER.log(INFO, "Search name is:{0} length {1}",new Object[]{searchName,searchName.length()});
    try{
      Query q;
      if(searchName.equals("%")){
        LOGGER.log(INFO, "Use allcorpPtnrs");
        q = em.createNamedQuery("allcorpPtnrs");
        
      }else {
        LOGGER.log(INFO, "Use corpPtnrListByTradingName with name {0}",searchName);
         q = em.createNamedQuery("corpPtnrsByTradeName");
         q.setParameter("trName", searchName);
      }
    
    try{
    List l = q.getResultList();
    ListIterator li = l.listIterator();
    while(li.hasNext()){
      PartnerCorporate dbCorp = (PartnerCorporate)li.next();
      PartnerCorporateRec corpRec = this.buildPartnerCorporateRec(dbCorp);
      corpList.add(corpRec);
      
    }

    LOGGER.log(INFO, "Corp list from named is  {0}", corpList);
    return corpList;
      }catch(IllegalStateException ex){
        throw new BacException("Corp partner by Trading name ","CORPPtnr:01");
      }catch(QueryTimeoutException ex){
        throw new BacException("Corp partner by Trading name ","CORPPtnr:02");
      }catch(TransactionRequiredException ex){
        throw new BacException("Corp partner by Trading name trans req ","CORPPtnr:03");
      }catch(PessimisticLockException ex){
        throw new BacException("Corp partner by Trading name locking failed ","CORPPtnr:04");
      }catch(LockTimeoutException ex){
        throw new BacException("Corp partner by Trading name locking timeout ","CORPPtnr:05");
      }catch(PersistenceException ex){
        throw new BacException("Corp partner by Trading name locking timeout ","CORPPtnr:05");
      }
    }catch(IllegalArgumentException ex){

    }


    return null;
  }

  public List<PartnerCorporateRec> getCorpPartnersByTradingName(String tradingName, boolean withRoles){
   
   if(tradingName == null){
      tradingName = new String();
    }
   String searchName = tradingName.replace(" ", "");
   searchName = searchName.trim();
   searchName = StringUtils.upperCase(searchName);
   searchName = searchName + '%';
   searchName = searchName.trim();
   searchName = searchName.toUpperCase();
   
   LOGGER.log(INFO, "Search name is:{0} length {1}",new Object[]{searchName,searchName.length()});
   TypedQuery q;
   if(searchName.equals("%")){
    LOGGER.log(INFO, "Use allcorpPtnrs");
    q = em.createNamedQuery("allcorpPtnrs",PartnerCorporate.class);
   }else {
    LOGGER.log(INFO, "Use corpPtnrListByTradingName with name {0}",searchName);
    q = em.createNamedQuery("corpPtnrsByTradeName",PartnerCorporate.class);
    q.setParameter("trName", searchName);
   }
   List<PartnerCorporate> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<PartnerCorporateRec> retList = new ArrayList<>();
   for(PartnerCorporate ptnr:rs){
    PartnerCorporateRec rec = this.buildPartnerCorporateRec(ptnr);
    if(withRoles){
     if(!ptnr.getPartnerRoles().isEmpty()){
     List<PartnerRole> rlList = ptnr.getPartnerRoles();
     LOGGER.log(INFO, "roles for partner {0}", rlList);
     
      List<PartnerRoleRec> roleRecLst = new ArrayList<>();
      for(PartnerRole rl:rlList){
       LOGGER.log(INFO, "rl.getRoleCode() {0}", rl.getRoleCode());
       PartnerRoleRec rlRec = buffer.getPartnerRoleByCode(rl.getRoleCode());
       LOGGER.log(INFO, "role from Buffer {0}", rlRec);
       roleRecLst.add(rlRec);
       LOGGER.log(INFO, "roleRecLst {0}", roleRecLst);
      }
      rec.setPartnerRoles(roleRecLst);
      LOGGER.log(INFO, "rec roles {0}", rec.getPartnerRoles());
     
     }
    }
    
    retList.add(rec);
    
   }
   return retList;  
  }
  
  
  public List<CountryRec> getCountriesAll(){
   List<CountryRec> retList = new ArrayList<>();
   Query q = em.createNamedQuery("countriesAll");
   List rs = q.getResultList();
   ListIterator li = rs.listIterator();
   while(li.hasNext()){
    Country cntry = (Country)li.next();
    CountryRec rec = this.buildCountryRec(cntry);
    retList.add(rec);
   }
   return retList;
  }
  
  public List<CountryRec> getCountriesByName(String name){
   List<CountryRec> retList = new ArrayList<>();
   if(name == null || name.isEmpty()){
    return getCountriesAll();
   }
   name = name + "%";
   Query q = em.createNamedQuery("countriesByname");
   q.setParameter("name", name);
    List rs = q.getResultList();
   ListIterator li = rs.listIterator();
   while(li.hasNext()){
    Country cntry = (Country)li.next();
    CountryRec rec = this.buildCountryRec(cntry);
    retList.add(rec);
   }
   return retList;
  }
  
  public CountryRec getCountryByRefAlpha2(String ref)throws BacException{
   LOGGER.log(INFO, "MasterDAtaDM.getCountryByRefAlpha2 called with ref {0}", ref);
   CountryRec rec ;
   
   Query q = em.createNamedQuery("countryByAlpha2");
   q.setParameter("code2", ref);
   try{
    Country cntry = (Country)q.getSingleResult();
    rec = this.buildCountryRec(cntry);
    return rec;
   }catch(NonUniqueResultException ex){
    LOGGER.log(INFO, "Multiple countries with ref {0}found ", ref);
    throw new BacException(ex.getLocalizedMessage(),"cntryDupl");
   }catch(NoResultException ex){
    LOGGER.log(INFO, "Country with 2 digit code {0} not found", ref);
    return null;
   }
   
  }
  
  public CountryRec getCountryByUser(UserRec usrRec){
   LOGGER.log(INFO, "getCountryByUser called with user id {0}", usrRec.getId());
   User usr = em.find(User.class, usrRec.getId());
   LOGGER.log(INFO, "Country for user {0}", usr.getCountry());
   if(usr.getCountry() == null){
    return null;
   }
   CountryRec cntryRec = buildCountryRec(usr.getCountry());
   return cntryRec;
  }
  
  public CurrencyRec getCurrencyByCode(String currCode) throws BacException{
   
   CurrencyRec rec;
   
   Query q = em.createNamedQuery("currencyByCode");
   q.setParameter("code", currCode);
   try{
    Currency curr = (Currency)q.getSingleResult();
    rec = this.buildCurrencyRec(curr);
    return rec;
   }catch(NonUniqueResultException ex){
    LOGGER.log(INFO, "multiple currency records for currency code {0}", currCode);
    throw new BacException(ex.getLocalizedMessage(),"currCodeDupl");
   }catch(NoResultException ex){
    LOGGER.log(INFO, "Currency code {0} not found",currCode);
    return null;
   }
   
   
  }
  public List<PartnerBaseRec> getPartnersAll() {
   LOGGER.log(INFO, "DB getPartnersAll called");
   List<PartnerBaseRec> ptnrLst = new ArrayList<>();
   Query q = em.createNamedQuery("ptnrsAll");
   List ptnrLstDb = q.getResultList();
   ListIterator li = ptnrLstDb.listIterator();
   while(li.hasNext()){
    PartnerBase ptnr = (PartnerBase)li.next();
    if(ptnr.getClass().getSimpleName().equals("User")){
     continue; 
    }
    LOGGER.log(INFO, "Actual partner class {0}", ptnr.getClass().getCanonicalName());
    if(ptnr.getClass().getCanonicalName().endsWith("PartnerCorporate")){
     PartnerCorporateRec rec = this.buildPartnerCorporateRec((PartnerCorporate)ptnr);
     ptnrLst.add(rec);
    }else{
     PartnerPersonRec rec = this.buildPartnerPersonRec((PartnerPerson)ptnr);
     ptnrLst.add(rec);
    }
   }
   return ptnrLst;
  }
  
  
  public List<PartnerBaseRec> getPartnersByRef(String searchRef){
   ArrayList<PartnerBaseRec> partnersList = new ArrayList<>();
   TypedQuery q = em.createNamedQuery("ptnrsByRef",PartnerBase.class);
   searchRef = StringUtils.replace(searchRef, "*", " ");
   q.setParameter("ptnrRef", searchRef);
   List<PartnerBase> rs = q.getResultList();
   ListIterator<PartnerBase> li = rs.listIterator();
   while(li.hasNext()){
    PartnerBase ptn = li.next();
    if(ptn.getClass().getSimpleName().equals("PartnerPerson")){
     PartnerPersonRec ind = this.buildPartnerPersonRec((PartnerPerson)ptn);
     partnersList.add(ind);
    }else if(ptn.getClass().getSimpleName().equals("PartnerCorporate")){
     PartnerCorporateRec corp = this.buildPartnerCorporateRec((PartnerCorporate)ptn);
     partnersList.add(corp);
    }
   }
   return partnersList;
  }
  
  public List<PartnerBaseRec> getPartnersByRole(PartnerRoleRec rl){
   LOGGER.log(INFO, "getPartnersByRole called with role {0}", rl.getId());
   if(!trans.isActive()){
    trans.begin();
   }
   PartnerRole r = em.find(PartnerRole.class, rl.getId());
   List<PartnerBase> ptnrs = r.getPartners();
   List<PartnerBaseRec> retList = new ArrayList<>();
   if(ptnrs != null){
    
    for(PartnerBase p: ptnrs){
     LOGGER.log(INFO, "Acual Partner class {0}", p.getClass().getSimpleName());
     if(StringUtils.equals(p.getClass().getSimpleName(), "PartnerCorporate")){
      PartnerCorporateRec corp = this.buildPartnerCorporateRec((PartnerCorporate)p);
      retList.add(corp);
     }else{
      PartnerPersonRec pers = this.buildPartnerPersonRec((PartnerPerson)p);
      retList.add(pers);
     }
    }
   }
   trans.commit();
   
   return retList;
   
  }
  
  public List<PartnerBaseRec> getPartnersByNameForRole(PartnerRoleRec rl, String acntName){
   if(!trans.isActive()){
    trans.begin();
   }
   PartnerRole r = em.find(PartnerRole.class, rl.getId());
   List<PartnerBase> ptnrs = r.getPartners();
   List<PartnerBaseRec> retList = new ArrayList<>();
   if(ptnrs != null){
    
    for(PartnerBase p: ptnrs){
     LOGGER.log(INFO, "Acual Partner class {0}", p.getClass().getSimpleName());
     if(StringUtils.equals(p.getClass().getSimpleName(), "PartnerCorporate")){
      PartnerCorporateRec corp = this.buildPartnerCorporateRec((PartnerCorporate)p);
      if(StringUtils.startsWith(corp.getTradingName(), acntName)){
       retList.add(corp);
      }
      
     }else{
      PartnerPersonRec pers = this.buildPartnerPersonRec((PartnerPerson)p);
      if(StringUtils.startsWith(pers.getName(), acntName)){
       retList.add(pers);
      }
      
     }
    }
   }
   trans.commit();
   
   return retList;
   
  }
  public List<PartnerBaseRec> getPartnersByCategory(String category) throws BacException {
    LOGGER.log(INFO, "getPartnersByCategory called with cat {0}", category);
   String actualClass;
   ArrayList<PartnerBaseRec> partnersList = new ArrayList<>();
    try{
    Query q = em.createNamedQuery("partnerByCategory");
    q.setParameter("cat", category);
    try{
      List l = q.getResultList();
      LOGGER.log(INFO, "named query partnerByCategory returned {0}", l);
      ListIterator li = l.listIterator();
      while(li.hasNext()){
        PartnerBase ptnr = (PartnerBase)li.next();
        actualClass = ptnr.getClass().getName();
        if(actualClass.contains("PartnerCorporate")){
          PartnerCorporateRec ptnrCorp = this.buildPartnerCorporateRec((PartnerCorporate)ptnr);
          partnersList.add(ptnrCorp);
        }else{
          PartnerPersonRec ptnrPers = this.buildPartnerPersonRec((PartnerPerson)ptnr);
          partnersList.add(ptnrPers);
        }

      }
      return partnersList;
      }catch(IllegalStateException ex){
        throw new BacException("Corp partner by Trading name ","CORPPtnr:01");
      }catch(QueryTimeoutException ex){
        throw new BacException("Corp partner by Trading name ","CORPPtnr:02");
      }catch(TransactionRequiredException ex){
        throw new BacException("Corp partner by Trading name trans req ","CORPPtnr:03");
      }catch(PessimisticLockException ex){
        throw new BacException("Corp partner by Trading name locking failed ","CORPPtnr:04");
      }catch(LockTimeoutException ex){
        throw new BacException("Corp partner by Trading name locking timeout ","CORPPtnr:05");
      }catch(PersistenceException ex){
        throw new BacException("Corp partner by Trading name locking timeout ","CORPPtnr:05");
      }catch(IllegalArgumentException ex){
      }
      }catch(IllegalArgumentException ex){

      }
   return null;
  }

 public List<PartnerPersonRec> getPartnerIndivByRole(PartnerRoleRec role){
  if(role == null){
   return null;
  }
  List<PartnerPersonRec> retList = null;
  Query q = em.createNamedQuery("persPtnrsByRole");
  PartnerRole r = em.find(PartnerRole.class, role.getId());
  List<PartnerRole> roles = new ArrayList<>();
  roles.add(r);
  q.setParameter("roles", roles);
  List rs = q.getResultList();
  LOGGER.log(INFO, "Query returns {0}", rs);
  for(Object o :rs){
   PartnerPerson p = (PartnerPerson)o;
   PartnerPersonRec rec = this.buildPartnerPersonRec(p);
   if(retList == null){
    retList = new ArrayList<>();
   }
   retList.add(rec);
  }
  LOGGER.log(INFO, "Ptnrs to return {0}", retList);
  return retList;
 }
 public PartnerRoleRec getPartnerRoleByCode(String ptnrCd){
  if(!trans.isActive()){
   trans.begin();
  }
  Query q = em.createNamedQuery("ptnrRoleByCode");
  q.setParameter("code", ptnrCd);
  try{
  PartnerRole role = (PartnerRole)q.getSingleResult();
  PartnerRoleRec rec = this.buildPartnerRoleRec(role);
  return rec;
 }catch (NoResultException ex){
   return null; 
 }
 }
 
 
 
 public List<PartnerRoleRec> getPartnerRoles(){
  if(!trans.isActive()){
   trans.begin();
  }
  TypedQuery q = em.createNamedQuery("ptnrActRoles",PartnerRole.class);
  List<PartnerRole> rs = q.getResultList();
  if(rs == null){
   return null;
  }
  List<PartnerRoleRec> roles = new ArrayList<>();
  for(PartnerRole rl : rs){
   PartnerRoleRec rec = this.buildPartnerRoleRec(rl);
   roles.add(rec);
  }
  return roles;
 }

  public Long createCorporatePartnerAR(PartnerCorporateRec partner, UserRec usr, String pg) {
    LOGGER.log(INFO, "master data DM createCorporatePartnerAR called with partner {0} user {1}",
            new Object[]{partner,usr});
    if(!trans.isActive()){
     trans.begin();
    }
    PartnerCorporate ptnrCorp = buildPartnerCorporate(partner,pg);
    LOGGER.log(INFO, "master data DM createCorporatePartnerAR returned corp id {0}",ptnrCorp.getId());
    trans.commit();
    return ptnrCorp.getId();
  }
  
  /**
   * Save new partner with minimum details and return Partner id
   * @param partner
   * @param usr
   * @param view page user was on when creating 
   * @return
   */
  public PartnerCorporateRec createCorporatePtnrSubLed(PartnerCorporateRec partner, UserRec usr, 
          String view) throws BacException {
    LOGGER.log(INFO, "masterdataDM.createCorporatePtnrSubLed called with partner {0} ptnr id {1} user {2}",
            new Object[]{partner,partner.getId(),usr});
    if(!trans.isActive()){
     trans.begin();
    }
    PartnerCorporate ptnrCorp = buildPartnerCorporate(partner,view);
    
    if(partner.getId() == null){
     partner.setId(ptnrCorp.getId());
    }
   trans.rollback();
   //trans.commit();
    /*
    
    Date crDate = new Date();
    User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
    try{
     ptnrCorp.setCreatedBy(crUsr);
     ptnrCorp.setCreatedDate(crDate);
     ptnrCorp.setRef(partner.getRef());
     ptnrCorp.setTradingName(partner.getTradingName());
     ptnrCorp.setLegalName(partner.getLegalName());
     ptnrCorp.setVatNumber(partner.getVatNumber());
     em.persist(ptnrCorp);
     
     partner.setId(ptnrCorp.getId());
     AuditPartner aud = new AuditPartner();
     aud.setAuditDate(crDate);
     aud.setCreatedBy(crUsr);
     aud.setFieldName(ptnrCorp.getRef());
     aud.setPartner(ptnrCorp);
     aud.setSource(view);
     aud.setUsrAction('I');
     em.persist(aud);
    }catch(EntityExistsException ex){
     LOGGER.log(INFO, "partner create entity exist error {0}", ex.getLocalizedMessage());
     throw new BacException("Partner Already exists","PTNR_EXISTS");
    } 
    */
    LOGGER.log(INFO, "master data DM createCorporatePartnerAR returned corp id {0}",partner.getId());
    return partner;
  }
  
  public PartnerCorporate createCorporatePartnerDM(PartnerCorporateRec partner, UserRec usr, String pg) {
    LOGGER.log(INFO, "master data DM createCorporatePartnerBank called with partner {0} user {1}",
            new Object[]{partner,usr});
    if(!trans.isActive()){
     trans.begin();
    }
    PartnerCorporate ptnrCorp = buildPartnerCorporate(partner,pg);
    User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
    ptnrCorp.setCreatedBy(crUsr);
    ptnrCorp.setCreatedDate(new Date());
    LOGGER.log(INFO, "master data DM createCorporatePartnerDM returned corp with id {0}",ptnrCorp.getId());
    return ptnrCorp;
  }
  
  public Long createIndividualPartnerAR(PartnerPersonRec partner, UserRec usr, String source) {
    LOGGER.log(INFO, "master data DM createIndividualPartnerAR called with partner {0} user {1}",
            new Object[]{partner,usr});
    if(!trans.isActive()){
     trans.begin();
    }
    PartnerPerson ptnrPers = this.buildPartnerPerson(partner,source);

    trans.commit();
    LOGGER.log(INFO, "master data DM createIndividualPartnerAR returned corp id {0}",ptnrPers.getId());
    return ptnrPers.getId();
  }

  
  public PartnerBaseRec updatePartner(PartnerBaseRec rec, String pg){
   LOGGER.log(INFO, "Called MasterDataDM.updatePartner with ptnr {0}", rec);
   LOGGER.log(INFO, "Rec roles {0}", rec.getPartnerRoles());
   //String ptnrType = ptnr.getClass().getSimpleName();
   if(!trans.isActive()){
    trans.begin();
   }
   
   PartnerBase ptnr = buildPartner(rec, pg);
   LOGGER.log(INFO,"ptnr id after build {0}",ptnr.getId());
   LOGGER.log(INFO, "ptnr roles {0}", ptnr.getPartnerRoles());
   
   if(rec.getId() == null){
    rec.setId(ptnr.getId());
   }
   
   trans.commit();
   //trans.rollback();
   return rec;
  }
  public PartnerBase updatePartnerDM(PartnerBaseRec ptnr, String pg){
   LOGGER.log(INFO, "Called MasterDataDM.updatePartnerDM with ptnr {0}", ptnr);
   LOGGER.log(INFO, "Rec roles {0}", ptnr.getPartnerRoles());
   String ptnrType = ptnr.getClass().getSimpleName();
   if(!trans.isActive()){
    trans.begin();
   }
   switch (ptnrType){
   case "PartnerIndividualRec":
    PartnerPerson ptnrPers = buildPartnerPerson((PartnerPersonRec)ptnr, pg);
    trans.commit();
    return ptnrPers;
   case "PartnerCorporateRec":
    PartnerCorporate ptnrCorp = buildPartnerCorporate((PartnerCorporateRec)ptnr, pg);
    trans.commit();
    return ptnrCorp;
    
   default:
    return null;
  }
   
   
  }
  public List<PartnerPersonRec> getPersonPtnrByFamName(String name){
   LOGGER.log(INFO, "getPersonPtnrByFamName called with {0}", name);
   TypedQuery q = em.createNamedQuery("persPtnrsBySurnameName", PartnerPerson.class);
   name = StringUtils.remove(name, "%");
   name = StringUtils.appendIfMissing(name,"%");
   name = StringUtils.upperCase(name);
   LOGGER.log(INFO, "name parameter is {0}", name);
   q.setParameter("surName", name);
   
   List<PartnerPerson> rs = q.getResultList();
   
   LOGGER.log(INFO, "result returned {0}",rs);
   if(rs == null){
    return null;
   }
   List<PartnerPersonRec> retList = new ArrayList<>();
   for(PartnerPerson p:rs){
    PartnerPersonRec curr = this.buildPartnerPersonRec(p);
    retList.add(curr);
   }
   
   return retList;
  }
  
  public List<PartnerCorporateRec> getPtnrByLegalName(String legNme){
   LOGGER.log(INFO,"getPtnrByLegalName called with {0}",legNme);
   legNme = StringUtils.replace(legNme, " ", "%");
   TypedQuery q = em.createNamedQuery("corpPtnrByLegalName", PartnerCorporate.class);
   q.setParameter("legName", legNme);
   List<PartnerCorporate> rs = q.getResultList();
   LOGGER.log(INFO, "rs from query {0}", rs);
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<PartnerCorporateRec> recList = new ArrayList<>();
   for(PartnerCorporate c:rs){
    PartnerCorporateRec  rec = this.buildPartnerCorporateRec(c);
    recList.add(rec);
   }
   return recList;
   
  }
  
  
 /**
  * Get list of partner given a partial trading name
  * @param srchName
  * @return
  */
 public List<PartnerBaseRec> getPtnrsByTrName(String srchName){
   LOGGER.log(INFO, "DM getPtnrsByTrName called");
   
   if(srchName == null || srchName.isEmpty()){
    srchName = "%";
   }else{
    srchName = srchName.replaceAll("%", "");
    srchName = srchName + "%";
    srchName = srchName.toUpperCase();
   }
   Query q = em.createNamedQuery("ptnrsByTrName");
   q.setParameter("trName", srchName);
   List rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<PartnerBaseRec> retList = new ArrayList<>();
   for(Object o:rs){
    String className= o.getClass().getSimpleName();
    LOGGER.log(INFO, "Ptnr {0} is type {1}", 
      new Object[]{((PartnerBase)o).getTradingName(),className});
    if(className.equals("PartnerCorporate")){
     PartnerCorporateRec corp = this.buildPartnerCorporateRec((PartnerCorporate)o);
     retList.add(corp);
    }else{
     PartnerPersonRec pers = this.buildPartnerPersonRec((PartnerPerson)o);
     retList.add(pers);
    }
   }
   return retList;
   
  }
 
 public List<PartnerBaseRec> getPtnrsByType(String typeName ){
  
  
  if(StringUtils.equals(typeName, "corp")){
   List<PartnerBaseRec> corpList = this.getAllCorpPartners();
   return corpList;
  }else if(StringUtils.equals(typeName, "pers")){
   List<PartnerPersonRec> persList = this.getAllPersonPartners();
   LOGGER.log(INFO, "getAllPersonPartners() returns {0}", persList);
   if(persList == null || persList.isEmpty()){
    return null;
   }else{
    List<PartnerBaseRec> retList = new ArrayList<>();
    for(PartnerPersonRec p:persList){
     retList.add(p);
    }
    LOGGER.log(INFO, "retList {0}", retList);
    return retList;
   }
   
  }
  return null;
 }
 
  public List<PartnerBaseRec> getPtnrsByName(String name){
   
   List<PartnerBaseRec> retList = this.getPtnrsByTrName(name);
   List<PartnerPersonRec> indivList = getIndivPtnrBySurname(name);
   if(indivList != null){
    if(retList == null){
     retList = new ArrayList<>();
    }
    for(PartnerPersonRec p:indivList){
     retList.add(p);
    }
   }
   
   if(retList != null){
    Collections.sort(retList, new PartnerByRef());
   }
   
   return retList;
  }
  
  public PartnerBaseRec getRolesForPtnr(PartnerBaseRec rec){
   LOGGER.log(INFO, "getPartnerRoles called with ptnr id {0}", rec.getId());
   
   PartnerBase ptnr = em.find(PartnerBase.class, rec.getId());
   List<PartnerRole> roles = ptnr.getPartnerRoles();
   if(roles == null){
    LOGGER.log(INFO, "roles {0}", usrDM);
    return rec;
   }
   int numRoles = roles.size();
   LOGGER.log(INFO, "Num roles {0}", numRoles);
   List<PartnerRoleRec> recRoles = new ArrayList<>();
   for(PartnerRole p:roles){
    PartnerRoleRec r = this.buildPartnerRoleRec(p);
    recRoles.add(r);
   }
   LOGGER.log(INFO, "Roles to add to Ptnr {0}", recRoles);
   rec.setPartnerRoles(recRoles);
   return rec;
   
  }
 
  public List<PartnerPersonRec> getIndivPtnrBySurname(String surname) throws BacException {
    LOGGER.log(INFO, "MasterData DM getIndivPtnrBySurname called with {0}", surname);
    List<PartnerPersonRec> indivList = new ArrayList<>();
    if(surname == null ){
      surname = new String();
    }
    String searchName = surname.replace(" ", "");
    searchName = searchName.trim();
    searchName = searchName.toUpperCase();
    searchName = searchName + '%';
    LOGGER.log(INFO, "Search name is: {0} param len {1}",new Object[]{searchName,searchName.length()});
    try{
      TypedQuery q;
      if(searchName.equals("%")){
        q = em.createNamedQuery("allPersPtnrs",PartnerPerson.class);

      }else {
        q = em.createNamedQuery("persPtnrsBySurnameName",PartnerPerson.class);
        q.setParameter("surName", searchName);
      }
    try{
      List<PartnerPerson> l = q.getResultList();
      LOGGER.log(INFO, " partner num records found {0}", l.size());
      LOGGER.log(INFO, " partner records found {0}", l);
      if(l == null || l.isEmpty()){
       return null;
      }
      ListIterator<PartnerPerson> li = l.listIterator();
      while(li.hasNext()){
       PartnerPerson pers = li.next();
       List<PartnerRole> roles = pers.getPartnerRoles();
       if(roles == null || roles.isEmpty()){
        LOGGER.log(INFO, "No roles for partner");
        continue;
       } 
       ListIterator<PartnerRole> roleLi = roles.listIterator();
       boolean userRole = false;
       while(roleLi.hasNext() && !userRole){
        PartnerRole rl = roleLi.next();
        userRole = rl.isUserRole();
       }
       if(userRole){
        LOGGER.log(INFO, "User {0} is a user", pers.getFamilyName());
        continue;
       }
       LOGGER.log(INFO, "Pers from DB {0}", pers.getFamilyName());
       PartnerPersonRec persRec = this.buildPartnerPersonRec(pers);
       LOGGER.log(INFO, "Pers rec{0}", persRec.getFamilyName());
       indivList.add(persRec);
      }
      LOGGER.log(INFO, "DB to retuen person partners {0}", indivList);
      
      return indivList;
      }catch(IllegalStateException ex){
        throw new BacException("indiv partner by Surname illegal state  ","IndivPtnr:01");
      }catch(QueryTimeoutException ex){
        throw new BacException("indiv partner by Surname time out ","IndivPtnr:02");
      }catch(TransactionRequiredException ex){
        throw new BacException("indiv partner by Surname trans req ","IndivPtnr:03");
      }catch(PessimisticLockException ex){
        throw new BacException("indiv partner by Surname locking failed ","IndivPtnr:04");
      }catch(LockTimeoutException ex){
        throw new BacException("indiv partner by Surname locking timeout ","IndivPtnr:05");
      }catch(PersistenceException ex){
        throw new BacException("indiv partner by Surname locking timeout ","IndivPtnr:05");
      }
    }catch(IllegalArgumentException ex){
    }


    return null;
  }

  public Long getCorpPartnerIdByTradingName(String tradingName){
   Query q = em.createNamedQuery("corpPtnrByTradeName");
   q.setParameter("trName", tradingName);
   try{
   PartnerCorporate ptnr = (PartnerCorporate)q.getSingleResult();
   return ptnr.getId();
   }catch(NoResultException ex){
    return null;
    
   }
  }
  public List<PartnerCorporateRec> getCorpPartnerByTradingName(String tradingName) {
    LOGGER.log(INFO,"masterDataDM getCorpPartnerByTradingName called with {0}",tradingName);
    ArrayList<PartnerCorporateRec> returnList = new ArrayList<>();
    if(tradingName == null || tradingName.isEmpty()){
      tradingName = "%";
    }else{
      tradingName = tradingName + "%";
    }
    try{
      Query q = em.createNamedQuery("corpPtnrListByTradingName");
      q.setParameter("trName", tradingName);
      try{
        List rl = q.getResultList();
        LOGGER.log(INFO, "DB returned {0}", rl);
        ListIterator li = rl.listIterator();
        while(li.hasNext()){
          PartnerCorporate ptnr = (PartnerCorporate)li.next();
          PartnerCorporateRec ptnrRec = this.buildPartnerCorporateRec(ptnr);
          returnList.add(ptnrRec);

        }
        LOGGER.log(INFO, "masterDataDM getCorpPartnerByTradingName to return {0}", returnList);
        return returnList;
      }catch(IllegalStateException ex){
        throw new BacException("Corp partner by trading name illegal state  ","CorpPtnr:01");
      }catch(QueryTimeoutException ex){
        throw new BacException("Corp partner by trading name time out ","CorpPtnr:02");
      }catch(TransactionRequiredException ex){
        throw new BacException("Corp partner by trading name trans req ","CorpPtnr:03");
      }catch(PessimisticLockException ex){
        throw new BacException("Corp partner by trading name locking failed ","CorpPtnr:04");
      }catch(LockTimeoutException ex){
        throw new BacException("Corp partner by trading name locking timeout ","CorpPtnr:05");
      }catch(PersistenceException ex){
        throw new BacException("Corp partner by trading name locking timeout ","CorpPtnr:05");
      }

    }catch(IllegalArgumentException ex){
      LOGGER.log(INFO, "Invalid query", ex.getLocalizedMessage());
    }

    return null;
  }





public AddressRec getAddressByStreetPostCode(String street, String postCode){
 
 
 Query q = em.createNamedQuery("addressByStreetPostCode");
 q.setParameter("pstCd", postCode);
 q.setParameter("str",street);
 try{
  Address addr = (Address)q.getSingleResult();
  AddressRec addrRec = this.buildAddressRec(addr);
  return addrRec;
 }catch(NoResultException ex){
  return null;
 }
 
 
}


    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<AddressRec> getAddressesByPostCode(String postCode) {
  LOGGER.log(INFO, "DB getAddressesByPostCode called with {0}", postCode);
  TypedQuery addrQ = em.createNamedQuery("addessesByPostCode", Address.class);
  //Query addrQ = em.createNamedQuery("addessesByPostCode");
  List<AddressRec> retList = new ArrayList<>();
  addrQ.setParameter("pstCd", postCode);
  List<Address> addrList = addrQ.getResultList();
  LOGGER.log(INFO, "Addresses found {0}", addrList);
  if(addrList == null || addrList.isEmpty()){
   return null;
  }
  for(Address addr:addrList){
   AddressRec addrRec = buildAddressRec(addr);
    retList.add(addrRec);
  }
  
  LOGGER.log(INFO, "Addresses found {0}", addrList);
  return retList;
 }

 
 public List<AddressRec> getAllAddresses() {
  LOGGER.log(INFO, "DB getAllAddresses");
  Query addrQ = em.createNamedQuery("allAddresses");
  List<AddressRec> retList = new ArrayList<>();
  List addrList = addrQ.getResultList();
  LOGGER.log(INFO, "Addresses returned by query {0}", addrList);
  ListIterator li = addrList.listIterator();
  while(li.hasNext()){
   Address addr = (Address)li.next();
   AddressRec addrRec = this.buildAddressRec(addr);
   retList.add(addrRec);
   
  }
  LOGGER.log(INFO, "Addresses found", retList);
  return retList;
  
 }
 
 public List<PartnerBaseRec> getAllCorpPartners(){
  
  TypedQuery q = em.createNamedQuery("allcorpPtnrs", PartnerCorporate.class);
  List<PartnerCorporate> rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }else{
   List<PartnerBaseRec> retList = new ArrayList<>();
   for(PartnerCorporate p:rs){
    PartnerCorporateRec rec = this.buildPartnerCorporateRec(p);
    retList.add(rec);
   }
   return retList;
  }
 }
/**
 * Returns all Person partners
 * @return
 * @throws BacException 
 */
 public List<PartnerPersonRec> getAllPersonPartners() throws BacException {
  LOGGER.log(INFO, "MasterDataDM.getAllPersonPartners() called");
  List<PartnerPersonRec> persList = new ArrayList<>();
  Query q = em.createNamedQuery("allPersPtnrs");
  List l = q.getResultList();
  ListIterator li = l.listIterator();
  while(li.hasNext()){
   PartnerPerson pers = (PartnerPerson)li.next(); 
   if(pers.getClass().getSimpleName().equals("User")){
     continue; 
    }
   PartnerPersonRec persRec = this.buildPartnerPersonRec(pers);
   persList.add(persRec);
  }
  return persList;
 }

 public PartnerCorporateRec createCorporatePartnerBank(PartnerCorporateRec partner, UserRec usr, String view) {
  LOGGER.log(INFO, "master data DM createCorporatePartnerBank called with partner {0} user {1}",
            new Object[]{partner,usr});
  Date crDate = new Date();
  if(!trans.isActive()){
   trans.begin();
  }
  PartnerCorporate ptnrCorp = this.buildPartnerCorporate(partner, view);
  PartnerRoleRec roleRec = this.buffer.getPartnerRoleByCode("BNK");
  PartnerRole role = em.find(PartnerRole.class, roleRec.getId());
  List<PartnerRole> roles = ptnrCorp.getPartnerRoles();
  if(roles == null || roles.isEmpty()){
   roles = new ArrayList<>();
  }
  roles.add(role);
  ptnrCorp.setPartnerRoles(roles);
  partner.setId(ptnrCorp.getId());
  List<PartnerRoleRec> ptntRolesRec = partner.getPartnerRoles();
  if(ptntRolesRec ==  null || ptntRolesRec.isEmpty()){
   ptntRolesRec = new ArrayList<>();
  }
  ptntRolesRec.add(roleRec);
  partner.setPartnerRoles(ptntRolesRec);
  trans.commit();
  return partner;
 }
 
 public boolean isAddressUnique(AddressRec rec){
  
  TypedQuery q = em.createNamedQuery("addressUnique", Address.class);
  q.setParameter("hseNum", rec.getHouseNumber());
  q.setParameter("hseName", rec.getHouseName());
  q.setParameter("street", rec.getStreet());
  q.setParameter("postCd", rec.getPostCode());
  
  List<Address> rs = q.getResultList();
  LOGGER.log(INFO, "isAddressUnique Query returned address {0}", rs);
  
     return rs == null || rs.isEmpty();
     
 } 
 public boolean isPartnerRefUnique(String ref){
 
 Query q = em.createNamedQuery("ptnrByRef");
 q.setParameter("ptnrRef", ref);
 List rs = q.getResultList();
 LOGGER.log(INFO, "partner by ref Query returns {0}", rs);
 return rs == null || rs.isEmpty();
 
}

public CurrencyRec updateCurrencyRec(CurrencyRec rec, String pg){
 LOGGER.log(INFO, "masterDataDM.updateCurrencyRec called with curr {0} ", rec);
 if(!trans.isActive()){
  trans.begin();
 }
 
 Currency curr = this.buildCurrency(rec, pg);
 if(rec.getId() == null){
  rec.setId(curr.getId());
 }
 
 trans.commit();
 return rec;
}

public PartnerRoleRec updatePartnerRole(PartnerRoleRec rec, String pg ){
 LOGGER.log(INFO, "MasteDataDM.updatePartnerRole called eith role code {0}", rec.getRoleCode());
 if(!trans.isActive()){
  trans.begin();
 }
 PartnerRole role = this.buildPartnerRole(rec, pg);
 if(rec.getId() == null){
  rec.setId(role.getPtnrRoleId());
 }
 trans.commit();
 return rec;
}

public boolean userDefaultCountryUpdate(String countryCode, 
  UserRec procUsrRec, String pg){
 LOGGER.log(INFO, "MastDataDM.userDefaultCountryUpdate called with {0}", countryCode);
 if(!trans.isActive()){
  trans.begin();
 }
 TypedQuery qCntry = em.createNamedQuery("countryByAlpha2", Country.class);
 qCntry.setParameter("code2", countryCode);
 qCntry.setMaxResults(1);
 List<Country> cntryLst = qCntry.getResultList();
  Country cntry = null;
 if(cntryLst != null && !cntryLst.isEmpty()){
  cntry = cntryLst.get(0);
 }
 if(cntry == null){
 return false;
 }
 TypedQuery qUser = em.createNamedQuery("userCntryBlnk", User.class);
 List<User> users = qUser.getResultList();
 
 if(!users.isEmpty()){
  User procUsr = em.find(User.class, procUsrRec.getId());
  for(User u:users){
   u.setCountry(cntry);
   AuditPartner aud = this.buildAuditPartner(u, procUsr, 'U', pg);
   aud.setFieldName("PTNR_CNTRY");
   aud.setNewValue(cntry.getCountryCode2());
   u.setChangedBy(procUsr);
   u.setChangedOn(new Date());
   LOGGER.log(INFO, "Updated User id {0} with country {1}", 
     new Object[]{u.getId(),u.getCountry().getCountryCode2()});
  }
  return true;
 }
 return false;
}
 
}
