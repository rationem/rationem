/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.sales.SalesCatRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.entity.audit.AuditSalesCatelog;
import com.rationem.entity.audit.AuditSalesInfo;
import com.rationem.entity.audit.AuditSalesPart;
import com.rationem.entity.audit.AuditSalesPartCompany;
import com.rationem.entity.config.common.Uom;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.document.DocLineGl;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.sales.SalesCat;
import com.rationem.entity.sales.SalesInfo;
import com.rationem.entity.sales.SalesPart;
import com.rationem.entity.sales.SalesPartCompany;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class SalesDM {
 private static final Logger LOGGER =
            Logger.getLogger("com.rationem.ejbBean.dataManager.AuditDM");

 @EJB
 private SysBuffer sysBuff;
  
 @EJB
 private MasterDataDM masterDataDM;
  
 @EJB
 private FiMasterRecordDM fiMastDM;
  
 @EJB
 private UserDM userDM;
  
 @EJB
 private CompanyDM compDM;
  
 private EntityManager em;
 private EntityTransaction trans;
 
 @PostConstruct  
 private void init(){
  FacesContext fc = FacesContext.getCurrentInstance();
  String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
  HashMap properties = new HashMap();
  properties.put("tenantId", tenantId);
  properties.put("eclipselink.session-name", "sessionName"+tenantId);
  em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
  trans = em.getTransaction();
 }
 
  private SalesPart buildSalesPart(SalesPartRec rec, String pg)throws BacException {
   LOGGER.log(INFO, "buildSalesPart called with partRec id {0}", rec.getId());
   SalesPart part;
   boolean partNew = false;
   boolean partChanged = false;
   if(rec.getId() == null || rec.getId() == 0){
    part = new SalesPart();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    part.setCreatedBy(crUsr);
    part.setCreatedOn(rec.getCreatedOn());
    em.persist(part);
    AuditSalesPart aud = this.buildAuditSalesPart(part, crUsr, 'I', pg);
    aud.setNewValue(rec.getPartCode());
    partNew = true;
   }else{
    part = em.find(SalesPart.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
   }
   if(partNew){
    part.setExternalDescription(rec.getExternalDescription());
    part.setPartCode(rec.getPartCode());
    part.setPhysicalPart(rec.isPhysicalPart());
    part.setShortDescription(rec.getShortDescription());
    if(rec.getSalesPartCompanies() != null && !rec.getSalesPartCompanies().isEmpty()){
     List<SalesPartCompany> partsComp = new ArrayList<>();
     for(SalesPartCompanyRec compPartRec: rec.getSalesPartCompanies()){
      SalesPartCompany compPart = buildSalesPartCompany(compPartRec,pg);
      partsComp.add(compPart);
     }
     part.setSalesPartCompany(partsComp);
    }
   }else{
    // user if changed
    User chUsr = em.find(User.class, rec.getChangedBy().getId());
    Date chDate =new Date();
    if(!StringUtils.equals(rec.getExternalDescription(), part.getExternalDescription()) &&
      StringUtils.isNotBlank(rec.getExternalDescription())){
     AuditSalesPart aud = this.buildAuditSalesPart(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_EXT_DESCR");
     aud.setNewValue(rec.getExternalDescription());
     aud.setOldValue(part.getExternalDescription());
     part.setExternalDescription(rec.getExternalDescription());
     partChanged = true;
    }
    if(!StringUtils.equals(rec.getPartCode(), part.getPartCode()) &&
      StringUtils.isNotBlank(rec.getPartCode())){
     AuditSalesPart aud = this.buildAuditSalesPart(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_CD");
     aud.setNewValue(rec.getPartCode());
     aud.setOldValue(part.getPartCode());
     part.setPartCode(rec.getPartCode());
     partChanged = true;
    }
    if(!StringUtils.equals(rec.getShortDescription(), part.getShortDescription()) &&
      StringUtils.isNotBlank(rec.getShortDescription())){
     AuditSalesPart aud = this.buildAuditSalesPart(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_SHRT_DESCR");
     aud.setNewValue(rec.getShortDescription());
     aud.setOldValue(part.getShortDescription());
     part.setPartCode(rec.getShortDescription());
     partChanged = true;
    }
    if(rec.isPhysicalPart() != part.isPhysicalPart()){
     AuditSalesPart aud = this.buildAuditSalesPart(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_PHYS");
     aud.setNewValue(String.valueOf(rec.isPhysicalPart()));
     aud.setOldValue(String.valueOf(part.isPhysicalPart()));
     part.setPhysicalPart(rec.isPhysicalPart());
     partChanged = true;
     
    }
     if(rec.getSalesPartCompanies() != null && !rec.getSalesPartCompanies().isEmpty()){
     List<SalesPartCompany> partsComp = new ArrayList<>();
     for(SalesPartCompanyRec compPartRec: rec.getSalesPartCompanies()){
      SalesPartCompany compPart = this.buildSalesPartCompany(compPartRec,pg);
      partsComp.add(compPart);
     }
     part.setSalesPartCompany(partsComp);
     }
    if(partChanged){
     part.setChangedBy(chUsr);
     part.setChangedOn(chDate);
    }
   }
   return part;
  }
  
  
  private SalesPartRec buildSalesPartRec(SalesPart rec)throws BacException {
   SalesPartRec part = new SalesPartRec();
   part.setId(rec.getId());
   UserRec crUsr = userDM.getUserRecPvt(rec.getCreatedBy());
   part.setCreatedBy(crUsr);
   part.setCreatedOn(rec.getCreatedOn());
   if(rec.getChangedBy() != null){
    UserRec chUsr = userDM.getUserRecPvt(rec.getChangedBy()); 
    part.setChangedBy(chUsr);
    part.setChangedOn(rec.getChangedOn());
   }
   
   part.setExternalDescription(rec.getExternalDescription());
   part.setPartCode(rec.getPartCode());
   part.setPhysicalPart(rec.isPhysicalPart());
   part.setShortDescription(rec.getShortDescription());
   
   return part;
  }
  
  public SalesPartCompanyRec getSalesPartCompany(SalesPartCompany rec){
   SalesPartCompanyRec part = new SalesPartCompanyRec();
   part.setId(rec.getId());
   UserRec crUsr = this.userDM.getUserRecPvt(rec.getCreatedBy());
   part.setCreatedBy(crUsr);
   part.setCreatedOn(rec.getCreatedOn());
   if(rec.getChangedBy() != null){
    UserRec chUsr = this.userDM.getUserRecPvt(rec.getChangedBy());
    part.setChangedBy(chUsr);
    part.setChangedOn(rec.getChangedOn());
   }
   part.setActive(rec.isActive());
   if(rec.getCategory() != null){
    SalesCatRec cat = this.buildSalesCatRec(rec.getCategory());
    part.setCategory(cat);
   }
   part.setChanges(rec.getChanges());
   if(rec.getCompany() != null){
    CompanyBasicRec comp = this.sysBuff.getCompanyById(rec.getCompany().getId());
    part.setCompany(comp);
    if(rec.getFund() != null){
     FundRec fnd = compDM.getRestrictedFundRec(comp, rec.getFund());
     part.setFund(fnd);
    }
   }
   if(rec.getCosAccount() != null){
    FiGlAccountCompRec cosAc = this.fiMastDM.buildFiCompGlAccountRecPvt(rec.getCosAccount());
   }
   if(rec.getPart() != null){
    SalesPartRec salesPart = this.buildSalesPartRec(rec.getPart());
    part.setPart(salesPart);
   }
   part.setSaleValue(rec.getSaleValue());
   if(rec.getSalesAccount() != null){
    FiGlAccountCompRec salesAc = this.fiMastDM.buildFiCompGlAccountRecPvt(rec.getSalesAccount());
    part.setSalesAccount(salesAc);
   }
   if(rec.getStockAccount() != null){
    FiGlAccountCompRec stockAc = this.fiMastDM.buildFiCompGlAccountRecPvt(rec.getStockAccount());
    part.setStockAccount(stockAc);
   }
   part.setStockValue(rec.getStockValue());
   if(rec.getUom() != null){
    UomRec uom = this.sysBuff.getUom(rec.getUom());
   }
   part.setCostOfSalesAccounting(rec.isCostOfSalesAccounting());
   part.setValidTo(rec.getValidTo());
   
   return part;
  } 
  
  public SalesCatRec getSubCats(SalesCatRec rec){
   LOGGER.log(INFO, "SalesDM.getSubCats called with cat id {0}", rec.getId());
   if(rec.getId() == null){
    return rec;
   }
   SalesCat cat = em.find(SalesCat.class, rec.getId());
   List<SalesCat> subCats = cat.getSubCategories();
   LOGGER.log(INFO, "subCats {0}", subCats);
   if(subCats == null || subCats.isEmpty()){
    return rec;
   }
   List<SalesCatRec> catList = new ArrayList<>();
   for(SalesCat currCat:subCats){
    SalesCatRec currRec = this.buildSalesCatRec(currCat);
    catList.add(currRec);
   }
   rec.setSubCategories(catList);
   return rec;
   
  }
  
  private SalesPartCompany buildSalesPartCompany(SalesPartCompanyRec rec, String pg) throws BacException{
   LOGGER.log(INFO, "buildSalesPartCompany called with rec id {0}", rec.getId());
   SalesPartCompany part;
   boolean partCompNew = false;
   boolean partCompChanged = false;
   if(rec.getId() == null){
    // new DB entity
    part = new SalesPartCompany();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    part.setCreatedBy(crUsr);
    part.setCreatedOn(rec.getCreatedOn());
    em.persist(part);
    AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, crUsr, 'I', pg);
    aud.setNewValue(rec.getPart().getPartCode()+"comp: "+rec.getCompany().getReference());
    partCompNew = true;
   }else{
    part = em.find(SalesPartCompany.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
   }
   LOGGER.log(INFO, "partCompNew {0}", partCompNew);
   if(partCompNew){
    if(rec.getCompany() != null){
    CompanyBasic comp = sysBuff.getComp(rec.getCompany()) ;
    part.setCompany(comp);
    }
    
    if(rec.getCategory() != null){
    SalesCat cat = em.find(SalesCat.class, rec.getCategory().getId(), OPTIMISTIC);
    part.setCategory(cat);
    }
    part.setCostOfSalesAccounting(rec.isCostOfSalesAccounting());
    part.setCostValue(rec.getCostValue());
    if(rec.getCosAccount() != null){
    FiGlAccountComp cosAc = em.find(FiGlAccountComp.class, rec.getCosAccount().getId(), OPTIMISTIC);
    part.setCosAccount(cosAc);
   }
   if(rec.getStockAccount() != null){
    FiGlAccountComp costAc = em.find(FiGlAccountComp.class, rec.getStockAccount().getId(), OPTIMISTIC);
    part.setStockAccount(costAc);
   }
   
    part.setStockValue(rec.getStockValue());
   
    if(rec.getFund() != null){
     Fund fnd = em.find(Fund.class, rec.getFund().getId(),OPTIMISTIC);
     part.setFund(fnd);
    }
    part.setSaleValue(rec.getSaleValue());
    if(rec.getSalesAccount() != null){
     FiGlAccountComp saleAc = em.find(FiGlAccountComp.class, rec.getSalesAccount().getId(), OPTIMISTIC);
     part.setSalesAccount(saleAc);
    }
    if(rec.getUom() != null){
     Uom uom = em.find(Uom.class, rec.getUom().getId(), OPTIMISTIC);
     part.setUom(uom);
    }
   
    if(rec.getValidTo() != null){
     part.setValidTo(rec.getValidTo());
    }
   
    part.setActive(rec.isActive());
   }else{
    // changed
    User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
    
    if((rec.getCompany() == null && part.getCompany() != null) ||
       (rec.getCompany() != null && part.getCompany() == null) ||
       (rec.getCompany() != null && Objects.equals(rec.getCompany().getId(), part.getCompany().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_COMP");
     aud.setOldValue(part.getCompany().getNumber());
     aud.setNewValue(rec.getCompany().getReference());
     CompanyBasic comp = this.sysBuff.getComp(rec.getCompany());
     part.setCompany(comp);
     partCompChanged = true;
    }
    if((rec.getCategory() == null && part.getCategory() != null) ||
       (rec.getCategory() != null && part.getCategory() == null) ||
       (rec.getCategory() != null && Objects.equals(rec.getCategory().getId(), part.getCategory().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_CAT");
     aud.setOldValue(part.getCategory().getSortDescr());
     aud.setNewValue(rec.getCategory().getSortDescr());
     SalesCat cat = em.find(SalesCat.class, rec.getCategory().getId());
     part.setCategory(cat);
     partCompChanged = true;
    }
    if(rec.isActive() != part.isActive()){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_ACT");
     aud.setOldValue(String.valueOf(part.isActive()));
     aud.setNewValue(String.valueOf(rec.isActive()));
     part.setActive(rec.isActive());
     partCompChanged = true;
    }
    if((rec.getCosAccount() == null && part.getCosAccount() != null) ||
       (rec.getCosAccount() != null && part.getCosAccount() == null) ||
       (rec.getCosAccount() != null && Objects.equals(rec.getCosAccount().getId(), part.getCosAccount().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_COS_AC");
     aud.setOldValue(part.getCosAccount().getCoaAccount().getRef());
     aud.setNewValue(rec.getCosAccount().getCoaAccount().getRef());
     FiGlAccountComp cosAcnt = em.find(FiGlAccountComp.class, rec.getCosAccount().getId());
     part.setCosAccount(cosAcnt);
     partCompChanged = true;
    }
    if(rec.isCostOfSalesAccounting() != part.isCostOfSalesAccounting()){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_STD_CST");
     aud.setOldValue(String.valueOf(part.isCostOfSalesAccounting()));
     aud.setNewValue(String.valueOf(rec.isCostOfSalesAccounting()));
     part.setCostOfSalesAccounting(rec.isCostOfSalesAccounting());
     partCompChanged = true;
    }
    if(rec.getCostValue() != part.getCostValue()){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_CST_VAL");
     aud.setOldValue(String.valueOf(part.getCostValue()));
     aud.setNewValue(String.valueOf(rec.getCostValue()));
     part.setCostValue(rec.getCostValue());
     partCompChanged = true;
    }
    if((rec.getFund() == null && part.getFund() != null) ||
       (rec.getFund() != null && part.getFund() == null) ||
       (rec.getFund() != null && Objects.equals(rec.getFund().getId(), part.getFund().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_FND");
     aud.setOldValue(part.getFund().getFndCode());
     aud.setNewValue(rec.getFund().getFndCode());
     Fund fnd = em.find(Fund.class, rec.getFund().getId());
     part.setFund(fnd);
     partCompChanged = true;
    }
    if((rec.getPart() == null && part.getPart() != null) ||
       (rec.getPart() != null && part.getPart() == null) ||
       (rec.getPart() != null && Objects.equals(rec.getPart().getId(), part.getPart().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_COMP_PART");
     aud.setOldValue(part.getPart().getPartCode());
     aud.setNewValue(rec.getPart().getPartCode());
     SalesPart prt = em.find(SalesPart.class, rec.getPart().getId());
     part.setPart(prt);
     partCompChanged = true;
    }
    if(rec.getSaleValue() != part.getSaleValue()){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_SL_VAL");
     aud.setOldValue(String.valueOf(part.getSaleValue()));
     aud.setNewValue(String.valueOf(rec.getSaleValue()));
     part.setSaleValue(rec.getSaleValue());
     partCompChanged = true;
    }
    if((rec.getSalesAccount() == null && part.getSalesAccount() != null) ||
       (rec.getSalesAccount() != null && part.getSalesAccount() == null) ||
       (rec.getSalesAccount() != null &&
      Objects.equals(rec.getSalesAccount().getId(), part.getSalesAccount().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_SL_ACNT");
     aud.setOldValue(part.getSalesAccount().getCoaAccount().getRef());
     aud.setNewValue(rec.getSalesAccount().getCoaAccount().getRef());
     FiGlAccountComp salesAcnt = em.find(FiGlAccountComp.class, rec.getSalesAccount().getId());
     part.setSalesAccount(salesAcnt);
     partCompChanged = true;
    }
    if((rec.getStockAccount() == null && part.getStockAccount() != null) ||
       (rec.getStockAccount() != null && part.getStockAccount() == null) ||
       (rec.getStockAccount() != null &&
      Objects.equals(rec.getStockAccount().getId(), part.getStockAccount().getId()))){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_SL_ACNT");
     aud.setOldValue(part.getStockAccount().getCoaAccount().getRef());
     aud.setNewValue(rec.getStockAccount().getCoaAccount().getRef());
     FiGlAccountComp stkAcnt = em.find(FiGlAccountComp.class, rec.getStockAccount().getId());
     part.setStockAccount(stkAcnt);
     partCompChanged = true;
    }
    if(rec.getStockValue() != part.getStockValue()){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_SL_VAL");
     aud.setOldValue(String.valueOf(part.getStockValue()));
     aud.setNewValue(String.valueOf(rec.getStockValue()));
     part.setStockValue(rec.getStockValue());
     partCompChanged = true;
    }
     if((rec.getValidTo() == null && part.getValidTo() != null) ||
       (rec.getValidTo() != null && part.getValidTo() == null) ||
       (rec.getValidTo() != null && !rec.getValidTo().equals(part.getValidTo())
      )){
     AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(part, chUsr, 'U', pg);
     aud.setFieldName("SL_PART_VAL");
     aud.setOldValue(part.getValidTo().toString());
     aud.setNewValue(rec.getStockAccount().getCoaAccount().getRef());
     FiGlAccountComp stkAcnt = em.find(FiGlAccountComp.class, rec.getStockAccount().getId());
     part.setStockAccount(stkAcnt);
     partCompChanged = true;
    }
    LOGGER.log(INFO, "partCompChanged {0}", partCompChanged);
    if(partCompChanged){
     part.setChangedBy(chUsr);
     part.setChangedOn(new Date());
    }
 }
  return part;
}
 
private AuditSalesCatelog buildAuditSalesCat(SalesCat cat, User usr, char usrAction, String page){
 AuditSalesCatelog aud = new AuditSalesCatelog();
 aud.setAuditDate(new Date());
 aud.setCat(cat);
 aud.setCreatedBy(usr);
 aud.setSource(page);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 return aud;
}

private AuditSalesPart buildAuditSalesPart(SalesPart part, User usr, char usrAction, String page){
 AuditSalesPart aud = new AuditSalesPart();
 aud.setAuditDate(new Date());
 aud.setPart(part);
 aud.setCreatedBy(usr);
 aud.setSource(page);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 return aud;
}


  /**
   * Add audit record for Sales Part Company change.
   * @param compPart
   * @param fld
   * @param newValue
   * @param oldValue
   * @param usr
   * @param audDate
   * @param page 
   */
  private AuditSalesPartCompany buildAuditSalesPartCompany(SalesPartCompany compPart, User usr, char usrAction, String page){
   
   AuditSalesPartCompany au = new AuditSalesPartCompany();
   au.setAuditDate(new Date());
   au.setCompPart(compPart);
   au.setSource(page);
   au.setUsrAction(usrAction);
   au.setCreatedBy(usr);
   em.persist(au);
   return au;
  }
  
  private SalesPartCompanyRec buildSalesPartCompanyRec(SalesPartCompany rec) throws BacException{
   LOGGER.log(INFO, "buildSalesPartCompanyRec called with {0} created by {1}", 
           new Object[]{rec,rec.getCreatedBy()});
   SalesPartCompanyRec compPart = new SalesPartCompanyRec();
   compPart.setId(rec.getId());
   UserRec crUsr = userDM.getUserRecPvt(rec.getCreatedBy());
   compPart.setCreatedBy(crUsr);
   compPart.setCreatedOn(rec.getCreatedOn());
   if(rec.getChangedBy() != null){
    UserRec chUsr = userDM.getUserRecPvt(rec.getChangedBy());
    compPart.setChangedBy(chUsr);
    compPart.setChangedOn(rec.getChangedOn());
   }
   if(rec.getCategory() != null){
    SalesCatRec cat = this.buildSalesCatRec(rec.getCategory());
    compPart.setCategory(cat);
   }
   
   LOGGER.log(INFO, "rec.getCompany()", rec.getCompany());
   if(rec.getCompany() != null){
    LOGGER.log(INFO, "buildSalesPartCompanyRec part {0} rec {1}", new Object[]{compPart,rec});
    CompanyBasicRec comp = sysBuff.getCompanyById(rec.getCompany().getId());
    LOGGER.log(INFO, "Comp from sys buff {0}",comp);
    compPart.setCompany(comp);
    List<FiGlAccountCompRec> compGlAcs = sysBuff.getGlAccountsByCompanyCode(comp);
   
   compPart.setCostOfSalesAccounting(rec.isCostOfSalesAccounting());
   if(rec.getCosAccount() != null){
    ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     FiGlAccountCompRec glAc = li.next();
     if(Objects.equals(glAc.getId(), rec.getCosAccount().getId())){
      compPart.setCosAccount(glAc);
     }
    }
    
   }
   if(rec.getStockAccount() != null){
    ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     FiGlAccountCompRec glAc = li.next();
     if(Objects.equals(glAc.getId(), rec.getStockAccount().getId())){
      compPart.setStockAccount(glAc);
     }
    }
   }
   LOGGER.log(INFO, "rec.getSalesAccount1() {0}", rec.getSalesAccount());
   if(rec.getSalesAccount() != null){
    LOGGER.log(INFO, "rec.getSalesAccount2() {0}", rec.getSalesAccount());
    
    ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     FiGlAccountCompRec glAc = li.next();
     if(Objects.equals(glAc.getId(), rec.getSalesAccount().getId())){
      compPart.setSalesAccount(glAc);
     }
    }
   }
   
   if(rec.getFund() != null){
    ListIterator<FundRec> li = comp.getFundList().listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     FundRec fund = li.next();
     if(Objects.equals(fund.getId(), rec.getFund().getId())){
      compPart.setFund(fund);
     }
    }
   }
   
   }
   
    compPart.setStockValue(rec.getStockValue());
   
   
   
   compPart.setSaleValue(rec.getSaleValue());
   
   LOGGER.log(INFO, "Sales Part Company UOM {0}", rec.getUom());
   if(rec.getUom() != null){
    UomRec uom = this.sysBuff.getUom(rec.getUom());
    compPart.setUom(uom);
   }
   LOGGER.log(INFO, "rec.getPart() {0}", rec.getPart().getId());
   if(rec.getPart() != null){
    SalesPartRec slpart = this.buildSalesPartRec(rec.getPart());
    LOGGER.log(INFO, "slpart id {0}", slpart.getId());
    compPart.setPart(slpart);
    
   }
   if(rec.getValidTo() != null){
    compPart.setValidTo(rec.getValidTo());
   }
   
   rec.setActive(rec.isActive());
   LOGGER.log(INFO, "compPart returned has part id {0}", compPart.getPart().getId());
   return compPart;
 
 
}
  
  private SalesCat buildSalesCat(SalesCatRec catRec, String pg){
   SalesCat cat;
   boolean newCat = false;
   boolean changedCat = false;
   if(catRec.getId() == null){
    cat = new SalesCat();
    User crUsr = em.find(User.class, cat.getCreatedBy().getId());
    cat.setCreatedBy(crUsr);
    cat.setCreatedOn(catRec.getCreatedOn());
    em.persist(cat);
    AuditSalesCatelog aud = this.buildAuditSalesCat(cat, crUsr, 'I', pg);
    aud.setNewValue(catRec.getCode());
    newCat = true;
   }else{
    cat = em.find(SalesCat.class, catRec.getId(), LockModeType.OPTIMISTIC);
   }
   
   if(newCat){
    if(catRec.getCompany() != null){
    CompanyBasic comp = this.sysBuff.getComp(catRec.getCompany());
    cat.setCompany(comp);
    }
    cat.setCode(pg);
    cat.setLongDescr(catRec.getLongDescr());
    if(catRec.getSalesCatParent() != null){
     SalesCat parent = em.find(SalesCat.class, catRec.getSalesCatParent().getId());
     cat.setSalesCatParent(parent);
    }
    cat.setSortDescr(catRec.getSortDescr());
    if(catRec.getSubCategories() != null){
     List<SalesCat> subCats = new ArrayList<>();
     for(SalesCatRec rec: catRec.getSubCategories() ){
      SalesCat curr = em.find(SalesCat.class, rec.getId());
      subCats.add(curr);
     }
     cat.setSubCategories(subCats);
    }
   
   }else{
    // category changed
    User chUsr = em.find(User.class, catRec.getChangedBy().getId());
    if(!Objects.equals(catRec.getCompany().getId(), cat.getCompany().getId())){
     AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
     aud.setFieldName("SL_CAT_COMP");
     aud.setOldValue(cat.getCompany().getNumber());
     aud.setNewValue(catRec.getCompany().getReference());
     CompanyBasic comp = this.sysBuff.getComp(catRec.getCompany());
     cat.setCompany(comp);
     changedCat = true;
    }
    if(!catRec.getCode().equals(cat.getCode())){
     AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
     aud.setFieldName("SL_CAT_CD");
     aud.setOldValue(cat.getCode());
     aud.setNewValue(catRec.getCode());
     cat.setCode(catRec.getCode());
     changedCat = true;
    }
    if(!catRec.getLongDescr().equals(cat.getLongDescr())){
     AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
     aud.setFieldName("SL_CAT_DESC");
     aud.setOldValue(cat.getLongDescr());
     aud.setNewValue(catRec.getLongDescr());
     cat.setLongDescr(catRec.getLongDescr());
     changedCat = true;
    }
    if((catRec.getSalesCatParent() == null && cat.getSalesCatParent() != null) ||
      (catRec.getSalesCatParent() != null && cat.getSalesCatParent() == null) ||
      (catRec.getSalesCatParent() != null && 
      !Objects.equals(catRec.getSalesCatParent().getId(), cat.getSalesCatParent().getId()))){
       AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
       aud.setFieldName("SL_CAT_PRNT");
       aud.setNewValue(catRec.getSalesCatParent().getSortDescr());
       aud.setOldValue(cat.getSalesCatParent().getSortDescr());
       SalesCat parent = em.find(SalesCat.class, catRec.getSalesCatParent().getId());
       cat.setSalesCatParent(parent);
       changedCat = true;
    }
    if(!StringUtils.equals(catRec.getSortDescr(), cat.getSortDescr())){
     AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
     aud.setFieldName("SL_CAT_NM");
     aud.setNewValue(catRec.getSortDescr());
     aud.setOldValue(cat.getSortDescr());
     cat.setSortDescr(catRec.getSortDescr());
     changedCat = true;
    }
    if((catRec.getSubCategories() == null && cat.getSubCategories() != null) ||
      (catRec.getSubCategories() != null && cat.getSubCategories() == null) ||
      (catRec.getSubCategories() != null && 
      !catRec.getSubCategories().equals(cat.getSubCategories()))){
     
    
       AuditSalesCatelog aud = this.buildAuditSalesCat(cat, chUsr, 'U', pg);
       aud.setFieldName("SL_CAT_SUB_CAT");
       List<SalesCat> subCatList;
       if(cat.getSubCategories() == null || cat.getSubCategories().isEmpty()){
        subCatList = new ArrayList();
        for(SalesCatRec currRec:catRec.getSubCategories() ){
         SalesCat currCat = em.find(SalesCat.class, currRec.getId());
         currCat.setSalesCatParent(cat);
         subCatList.add(currCat);
         changedCat = true;
        }
         cat.setSubCategories(subCatList);
       }else{
        // remove deleted
        if(!cat.getSubCategories().isEmpty()){
         ListIterator<SalesCat> remLi = cat.getSubCategories().listIterator();
         boolean found;
         while(remLi.hasNext()){
          SalesCat currSubCat = remLi.next();
          found = false;
          for(SalesCatRec currSubCatRec: catRec.getSubCategories() ){
           if(Objects.equals(currSubCatRec.getId(), currSubCat.getId())){
            found = true;
            break;
           }
          }
          if(!found){
           remLi.remove();
           changedCat = true;
          }
         }
        
        }
        // add new sub catgories
        if(!catRec.getSubCategories().isEmpty()){
         for(SalesCatRec currSubCatRec: catRec.getSubCategories() ){
          
          boolean found = false;
          while(!found){
           for(SalesCat currSubCat: cat.getSubCategories()){
            if(Objects.equals(currSubCat.getId(), currSubCatRec.getId())){
             found = true;
            }
           }
          }
          if(!found){
           SalesCat addCat = em.find(SalesCat.class, currSubCatRec.getId());
           cat.getSubCategories().add(addCat);
          }
         }
        }
       }
    }
    if(changedCat){
     cat.setChangedBy(chUsr);
     cat.setChangedOn(catRec.getChangedOn());
    }
   }
   return cat;
  }
  
  private SalesCatRec buildSalesCatRec(SalesCat cat){
   LOGGER.log(INFO, "buildSalesCatRec caled with {0}", cat);
   SalesCatRec rec = new SalesCatRec() ;
   
   rec.setId(cat.getId());
   if(cat.getChangedBy() != null){
    UserRec usr = userDM.getUserRecPvt(cat.getChangedBy());
    rec.setChangedBy(usr);
    rec.setChangedOn(cat.getChangedOn());
   }
   
   rec.setChanges(cat.getChanges());
   rec.setCode(cat.getCode());
   if(cat.getCompany() != null){
    CompanyBasicRec comp = sysBuff.getCompanyById(cat.getCompany().getId());
    rec.setCompany(comp);
   }
   UserRec crUsr = userDM.getUserRecPvt(cat.getCreatedBy());
   rec.setCreatedBy(crUsr);
   rec.setCreatedOn(cat.getCreatedOn());
   if(cat.getSalesCatParent() != null && cat.getSalesCatParent().getId() != 0 ){
    SalesCat parent = cat.getSalesCatParent();
    LOGGER.log(INFO,"Build cat {0} with parent with id {1} ",
            new Object[]{cat,parent});
    if(!Objects.equals(cat.getSalesCatParent().getId(), cat.getId()) ){
      LOGGER.log(INFO,"Build  2 cat {0} with parent with id {1} ",
            new Object[]{cat,parent});
     SalesCatRec parentRec = buildSalesCatRec(cat.getSalesCatParent());
     rec.setSalesCatParent(parentRec);
   }
   List<SalesCatRec> subCats = rec.getSubCategories() ;
   if(cat.getSubCategories() != null && !cat.getSubCategories().isEmpty()){
    if(subCats == null){
     subCats = new ArrayList<>();
    }
    List<SalesCat> subCatsDb = cat.getSubCategories();
    ListIterator<SalesCat> li = subCatsDb.listIterator();
    LOGGER.log(INFO, "cat parent {0}", cat.getSalesCatParent());
    while(li.hasNext()){
     SalesCatRec subCat = buildSalesCatRec(li.next());
     subCat.setSalesCatParent(rec);
     rec.setSubCategories(subCats);
    }
    LOGGER.log(INFO, "cat parent 2 {0}", cat.getSalesCatParent());
   }
   LOGGER.log(INFO, "cat parent 3{0}", cat.getSalesCatParent());
   
   
    
   }
   rec.setLongDescr(cat.getLongDescr());
   rec.setSortDescr(cat.getSortDescr());
   return rec;
  }

  
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public SalesCatRec addSalesCategory(SalesCatRec category, String pg) throws BacException {
  if(!trans.isActive()){
   trans.begin();
  }
  LOGGER.log(INFO, "Save sales category called with {0}",category);
  SalesCat catDb = this.buildSalesCat(category,pg);
  List<SalesCatRec> subSalesCategories = category.getSubCategories();
  if(subSalesCategories != null && !subSalesCategories.isEmpty() ){
   ListIterator<SalesCatRec> li = subSalesCategories.listIterator();
   while(li.hasNext()){
    SalesCatRec subCat = li.next();
    SalesCat subCatDb = this.buildSalesCat(subCat, pg);
    em.persist(subCatDb);
    List<SalesCat> subCatLstDb = catDb.getSubCategories();
    if(subCatLstDb == null){
     subCatLstDb = new ArrayList<>();
    }
    subCatLstDb.add(subCatDb);
    subCatDb.setSalesCatParent(catDb);
   }
  }
  em.persist(catDb);
  category.setId(catDb.getId());
  LOGGER.log(INFO, "Sales category id {0}", catDb.getId());
  trans.commit();
  return category;
 }

 public List<SalesCatRec> allSalesCategories() throws BacException {
  LOGGER.log(INFO, "SalesDM.allSalesCategories called");
  List<SalesCatRec> categories;
  try{
   Query q = em.createNamedQuery("allSalesCategories");
   List rl = q.getResultList();
   if(rl == null){
    return new ArrayList<>();
   }else{
    categories = new ArrayList<>();
    ListIterator li = rl.listIterator();
    while(li.hasNext()){
     SalesCat catDB;
     catDB = (SalesCat)li.next();
     LOGGER.log(INFO, "Build sales category with id {0} parent {1}",
             new Object[]{catDB.getId(),catDB.getSalesCatParent()});
     SalesCatRec cat = this.buildSalesCatRec(catDB);
     categories.add(cat);
     
    }
    LOGGER.log(INFO, "Number of sales categories returned {0}", categories);
    return categories;
   }
  }catch(IllegalArgumentException ex){
   LOGGER.log(INFO, "error creating named query {0}", ex.getLocalizedMessage());
   throw new BacException("named query error "+ex.getLocalizedMessage(),"SCAT:01");
  }
  
  
  
 }

 public SalesPartRec addSalesPart(SalesPartRec item, UserRec usr, String source) throws BacException {
  LOGGER.log(INFO, "SalesDM.addSales part called with part {0} user {1} page {2}", 
          new Object[]{item,usr,source});
  if(!trans.isActive()){
   trans.begin();
  }
  SalesPart part = buildSalesPart(item,source);
  if(item.getId() == null){
   item.setId(part.getId());
  }
  
  trans.commit();
  LOGGER.log(INFO, "SalesDM. addPart returns part {0} id {1}", new Object[]{item,item.getId()});
  return item;
 }

 
 public int copySalesCatByComp(CompanyBasicRec c1, CompanyBasicRec c2, 
         UserRec usrRec, String pg){
  LOGGER.log(INFO, "SalesDM.copySalesCatByComp call with comp {0} and {1}", new Object[]{
   c1.getReference(), c2.getReference()});
  
  if(!trans.isActive()){
   trans.begin();
  }
  
  TypedQuery qC2 = em.createNamedQuery("salesCatsByComp", SalesCat.class);
  qC2.setMaxResults(1);
  qC2.setParameter("compId", c2.getId());
  List<SalesCat> rs = qC2.getResultList();
  if(!rs.isEmpty()){
   return CompanyManager.ATTR_IN_DEST_COMP;
  }
  TypedQuery qC1 = em.createNamedQuery("salesCatsByComp", SalesCat.class);
  qC1.setMaxResults(1);
  qC1.setParameter("compId", c1.getId());
  rs = qC1.getResultList();
  if(rs.isEmpty()){
   return CompanyManager.ATTR_NOT_IN_SRC_COMP;
  }
  CompanyBasic comp1 = em.find(CompanyBasic.class, c1.getId());
  User usr = em.find(User.class, usrRec.getId());
  int numSalesCats = 0;
  for(SalesCat s: rs){
   SalesCat curr = new SalesCat();
   curr.setCode(s.getCode());
   curr.setCompany(comp1);
   curr.setCreatedBy(usr);
   curr.setCreatedOn(new Date());
   curr.setLongDescr(s.getLongDescr());
   curr.setSalesCatParent(s.getSalesCatParent());
   curr.setSalesParts(s.getSalesParts());
   curr.setSortDescr(s.getSortDescr());
   em.persist(curr);
   AuditSalesCatelog aud = this.buildAuditSalesCat(curr, usr, 'I', pg);
   aud.setNewValue(s.getSortDescr());
   numSalesCats++;
  }
  
  if(numSalesCats == 0){
   trans.rollback();
   return CompanyManager.ATTR_NOT_CREATED;
  }else{
   trans.commit();
   return CompanyManager.ATTR_CREATED;
  }
 }
 
 public int copySalesPartCompByComp(CompanyBasicRec c1, CompanyBasicRec c2, 
         UserRec usrRec, String pg){
  
  if(!trans.isActive()){
   trans.begin();
  }
  
  TypedQuery qC2 = em.createNamedQuery("salesCatsByComp", SalesPartCompany.class);
  qC2.setMaxResults(1);
  qC2.setParameter("compId", c2.getId());
  List<SalesPartCompany> rs = qC2.getResultList();
  if(!rs.isEmpty()){
   return CompanyManager.ATTR_IN_DEST_COMP;
  }
  
  TypedQuery qC1 = em.createNamedQuery("salesCatsByComp", SalesPartCompany.class);
  qC1.setParameter("compId", c1.getId());
  rs = qC1.getResultList();
  if(rs.isEmpty()){
   return CompanyManager.ATTR_NOT_IN_SRC_COMP;
  }
  CompanyBasic comp2 = em.find(CompanyBasic.class, c2.getId());
  User usr = em.find(User.class, usrRec.getId());
  int numParts = 0;
  for(SalesPartCompany p: rs){
   SalesPartCompany curr = new SalesPartCompany();
   curr.setActive(p.isActive());
   curr.setCompany(comp2);
   curr.setCosAccount(p.getCosAccount());
   curr.setCostValue(p.getCostValue());
   curr.setCreatedBy(usr);
   curr.setCreatedOn(new Date());
   if(p.getPart() == null){
    continue;
   }
   if(p.getFund() != null){
    Query q = em.createNamedQuery("fndByCodeComp");
    q.setParameter("code", p.getFund().getFndCode());
    q.setParameter("compId", c2.getId());
    try{
    Fund f  = (Fund)q.getSingleResult();
    curr.setFund(f);
    }catch(NoResultException e){
     LOGGER.log(INFO, "Could not find fund {0} for comp {1}", new Object[]{
      p.getFund().getFndCode(),c2.getId() });
    }
   }
   curr.setPart(p.getPart());
   curr.setSaleValue(p.getSaleValue());
   curr.setSalesAccount(p.getSalesAccount());
   curr.setStockAccount(p.getStockAccount());
   curr.setStockValue(p.getStockValue());
   curr.setUom(p.getUom());
   curr.setValidTo(p.getValidTo());
   em.persist(curr);
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(curr, usr, 'I', pg);
   aud.setNewValue(curr.getPart().getPartCode());
   numParts++;
  }
  if(numParts == 0){
   trans.rollback();
   return CompanyManager.ATTR_NOT_CREATED;
  }else{
   trans.commit();
   return CompanyManager.ATTR_CREATED;
  }
  
 }
 public SalesPartRec getCompPartsForPart(SalesPartRec partRec){
  LOGGER.log(INFO, "SaledDM.getCompPartsForPart called with {0}", partRec);
  if(!trans.isActive()){
   trans.begin();
  }
  SalesPart part = em.find(SalesPart.class, partRec.getId(), OPTIMISTIC);
  List<SalesPartCompanyRec> compPartsRec = new ArrayList<>();
  List<SalesPartCompany> compParts = part.getSalesPartCompany();
  LOGGER.log(INFO, "sales parts {0}", compParts);
  ListIterator<SalesPartCompany> compPartsLi = compParts.listIterator();
  while(compPartsLi.hasNext()){
   SalesPartCompany compPart = compPartsLi.next();
   SalesPartCompanyRec compPartRec = this.buildSalesPartCompanyRec(compPart);
   LOGGER.log(INFO, "getCompPartsForPart sales ac {0}", compPartRec.getSalesAccount());
   compPartsRec.add(compPartRec);
  }
  partRec.setSalesPartCompany(compPartsRec);
  return partRec;
 }
 
 public List<SalesPartRec> getAllSalesParts() throws BacException {
  LOGGER.log(INFO, "SalesDM.getAllSalesParts called");
  List<SalesPartRec> parts = new ArrayList<>();
  Query q = em.createNamedQuery("allSalesParts");
  List rl = q.getResultList();
  LOGGER.log(INFO, "Parts found by DB query {0}", rl);
  ListIterator li = rl.listIterator();
  while(li.hasNext()){
   SalesPart pt = (SalesPart)li.next();
   SalesPartRec partRec = this.buildSalesPartRec(pt);
   LOGGER.log(INFO, "Sales part comp details {0}", partRec.getSalesPartCompanies());
   parts.add(partRec);
  }
  return parts;
 }

 public List<SalesPartCompanyRec> getAllCompanySalesParts() throws BacException {
  LOGGER.log(INFO, "Sales DM getAllCompanySalesParts called");
  Query q = em.createNamedQuery("allCompSalesParts");
  List rl = q.getResultList();
  List<SalesPartCompanyRec> compParts = new ArrayList<>();
  LOGGER.log(INFO, "allCompSalesParts returns {0} parts",rl);
  if(rl == null || rl.isEmpty()){
   throw new BacException("No sales parts for company. Please check");
  }
  
  ListIterator li = rl.listIterator();
  while(li.hasNext()){
   SalesPartCompany compPart = (SalesPartCompany)li.next();
   LOGGER.log(INFO, "Call buildSalesPartCompanyRec with part {0} created by: {1} part id {2}", 
           new Object[]{compPart,compPart.getCreatedBy(),compPart.getPart().getId()});
   SalesPartCompanyRec compPartRec = buildSalesPartCompanyRec(compPart); 
   compParts.add(compPartRec);
  }
  return compParts;
 }

 public List<SalesPartRec> getPartsByPartCodePartial(String partCdInput) {
  LOGGER.log(INFO, "SalesDM.getPartsByPartCodePartial called with {0}", partCdInput);
  
  List<SalesPartRec> partsList = new ArrayList<>();
  Query q = em.createNamedQuery("partsByCodePartial");
  q.setParameter("pcode", partCdInput);
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   SalesPart part = (SalesPart)li.next();
   SalesPartRec partRec = this.buildSalesPartRec(part);
   partsList.add(partRec);
  }
  LOGGER.log(INFO, "getPartsByPartCodePartial returns {0}", partsList);
  return partsList;
 }

 public SalesPartCompanyRec addCompPartToSalesPart(SalesPartRec partRec, SalesPartCompanyRec compPartRec, 
         UserRec usr, Date createDate, String page) throws BacException {
  LOGGER.log(INFO, "SalesDM.addCompPartToSalesPart called with part {0} compPart {1} user {2} pages {3}", 
          new Object[]{partRec,compPartRec,usr,page});
  if(partRec == null || compPartRec == null){
   throw new BacException("addCompPartToSalesPart: Part and Company part are required","SM:01");
  }
  if(compPartRec.getValidTo() == null){
   throw new BacException("Company Valid to date must be specified");
  }
  
  SalesPart part = em.find(SalesPart.class, partRec.getId(), OPTIMISTIC);
  compPartRec.setCreatedBy(usr);
  compPartRec.setCreatedOn(createDate);
  compPartRec.setId(null);
  
  SalesPartCompany compPart = this.buildSalesPartCompany(compPartRec, page);
  compPart.setPart(part);
  LOGGER.log(INFO, "compPart {0}", compPart);
  AuditSalesPartCompany aud = new AuditSalesPartCompany();
  aud.setAuditDate(createDate);
  aud.setCompPart(compPart);
  User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  aud.setCreatedBy(crUsr);
  aud.setNewValue(compPart.getCompany().getCompanyNumber() +" | "+  compPart.getPart().getPartCode());
  aud.setSource(page);
  aud.setUsrAction('I');
  em.persist(aud);
  List<SalesPartCompany> partCompLst = part.getSalesPartCompany();
  partCompLst.add(compPart);
  part.setSalesPartCompany(partCompLst);
  compPartRec.setId(part.getId());
  return compPartRec;
 }

 
 public SalesPartCompanyRec updateSaleCompPart(SalesPartCompanyRec salesCompPartRec, UserRec usr, 
         Date chDate,  String page) throws BacException {
  LOGGER.log(INFO, "SalesDM.updateSaleCompPart called with compPart {0} usr {1} page {2} ", 
          new Object[]{salesCompPartRec,usr,page});
  User updateUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  if(salesCompPartRec == null){
   throw new BacException("updateSaleCompPart salesCompPart required","SM02");
   
  }
  boolean changed = false;
  SalesPartCompany compPart = em.find(SalesPartCompany.class, salesCompPartRec.getId(), OPTIMISTIC);
  if(!Objects.equals(compPart.getCategory().getId(), salesCompPartRec.getCategory().getId())){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_CAT");
   aud.setOldValue(compPart.getCategory().getCode());
   aud.setNewValue(salesCompPartRec.getCategory().getCode());
   SalesCat cat = em.find(SalesCat.class, salesCompPartRec.getCategory().getId(), OPTIMISTIC);
   compPart.setCategory(cat);
   changed = true;
  }
  
  if(compPart.getCosAccount() == null && salesCompPartRec.getCosAccount() != null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_COS_AC");
   aud.setNewValue(salesCompPartRec.getCosAccount().getCoaAccount().getRef());
   
    FiGlAccountComp cosAc = em.find(FiGlAccountComp.class, salesCompPartRec.getId());
    compPart.setCosAccount(cosAc);
    changed = true;
  } else if(compPart.getCosAccount() != null && salesCompPartRec.getCosAccount() == null){
    AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
    aud.setFieldName("SL_PART_COS_AC");
    aud.setOldValue(compPart.getCosAccount().getCoaAccount().getRef());
    
    compPart.setCosAccount(null);
    changed = true;
  } else if(compPart.getCosAccount() != null && salesCompPartRec.getCosAccount() != null){
   if(!Objects.equals(compPart.getCosAccount().getId(), salesCompPartRec.getCosAccount().getId())){
    AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
    aud.setFieldName("SL_PART_COS_AC");
    aud.setNewValue(salesCompPartRec.getCosAccount().getCoaAccount().getRef());
    aud.setOldValue(compPart.getCosAccount().getCoaAccount().getRef());
    FiGlAccountComp cosAc = em.find(FiGlAccountComp.class, salesCompPartRec.getId());
    compPart.setCosAccount(cosAc);
    changed = true;
   }
  }
  
  if(compPart.getFund()== null && salesCompPartRec.getFund() != null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_FND");
   aud.setNewValue(salesCompPartRec.getFund().getFndCode());
   FiGlAccountComp cosAc = em.find(FiGlAccountComp.class, salesCompPartRec.getFund().getId());
    compPart.setCosAccount(cosAc);
    changed = true;
  } else if(compPart.getFund() != null && salesCompPartRec.getFund() == null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_FND");
   aud.setOldValue(compPart.getFund().getFndCode());
   compPart.setCosAccount(null);
   changed = true;
  } else if((compPart.getFund() != null & salesCompPartRec.getFund() != null ) &&
    (!Objects.equals(compPart.getFund().getId(), salesCompPartRec.getFund().getId()))){
    AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
    aud.setFieldName("SL_PART_FND");
    aud.setNewValue(salesCompPartRec.getFund().getFndCode());
    aud.setOldValue(compPart.getFund().getFndCode());
    Fund fnd = em.find(Fund.class, salesCompPartRec.getFund().getId(), OPTIMISTIC);
    compPart.setFund(fnd);
    changed = true;
    
  }
  
  
  if(compPart.getSaleValue() != salesCompPartRec.getSaleValue()){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_SL_VAL");
   aud.setNewValue(String.valueOf(salesCompPartRec.getSaleValue()));
   aud.setOldValue(String.valueOf(compPart.getSaleValue()));
   compPart.setSaleValue(salesCompPartRec.getSaleValue());
   changed = true;
  }
  
  if(compPart.getSalesAccount()== null && salesCompPartRec.getSalesAccount() != null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_SL_ACNT");
   aud.setNewValue(salesCompPartRec.getSalesAccount().getCoaAccount().getRef());
   FiGlAccountComp salesAc = em.find(FiGlAccountComp.class, salesCompPartRec.getSalesAccount().getId(), OPTIMISTIC);
    compPart.setSalesAccount(salesAc);
    changed = true;
  } else if(compPart.getSalesAccount() != null && salesCompPartRec.getSalesAccount() == null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_SL_ACNT");
   aud.setOldValue(salesCompPartRec.getSalesAccount().getCoaAccount().getRef());
   compPart.setCosAccount(null);
   changed = true;
    
  } else if((compPart.getSalesAccount() != null & salesCompPartRec.getSalesAccount() != null) &&
    (!Objects.equals(compPart.getSalesAccount().getId(), salesCompPartRec.getSalesAccount().getId()) )){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_SL_ACNT");
   aud.setOldValue(salesCompPartRec.getSalesAccount().getCoaAccount().getRef());
   aud.setNewValue(compPart.getSalesAccount().getCoaAccount().getRef());
   FiGlAccountComp salesAc = em.find(FiGlAccountComp.class, salesCompPartRec.getSalesAccount().getId(), OPTIMISTIC);
   compPart.setCosAccount(salesAc);
   changed = true;
   
  }
  
  if(compPart.getStockAccount()== null && salesCompPartRec.getStockAccount() != null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_STK_ACNT");
   aud.setNewValue(salesCompPartRec.getStockAccount().getCoaAccount().getRef());
   FiGlAccountComp stockAc = em.find(FiGlAccountComp.class, salesCompPartRec.getStockAccount().getId(), OPTIMISTIC);
   compPart.setStockAccount(stockAc);
   changed = true;
  } else if(compPart.getStockAccount() != null && salesCompPartRec.getStockAccount() == null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_STK_ACNT");
   aud.setOldValue(compPart.getStockAccount().getCoaAccount().getRef());
   compPart.setStockAccount(null);
   changed = true;
    
  } else if((compPart.getStockAccount() != null & salesCompPartRec.getStockAccount() != null) &&
    (!Objects.equals(compPart.getStockAccount().getId(), salesCompPartRec.getStockAccount().getId()))){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_STK_ACNT");
   aud.setOldValue(compPart.getStockAccount().getCoaAccount().getRef());
   aud.setNewValue(salesCompPartRec.getStockAccount().getCoaAccount().getRef());
   FiGlAccountComp stockAc = em.find(FiGlAccountComp.class, salesCompPartRec.getStockAccount().getId(), OPTIMISTIC);
   compPart.setStockAccount(stockAc);
   changed = true;
  
  }
  
  if(compPart.getStockValue() != salesCompPartRec.getStockValue()){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_STK_VAL");
   aud.setNewValue(String.valueOf(salesCompPartRec.getStockValue()));
   aud.setNewValue(String.valueOf(compPart.getStockValue()));
   compPart.setStockValue(salesCompPartRec.getStockValue());
   changed = true;
  }
  
  if(compPart.getUom()== null && salesCompPartRec.getUom() != null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_UOM");
   aud.setNewValue(salesCompPartRec.getUom().getUomCode());
   Uom uom = em.find(Uom.class, salesCompPartRec.getUom().getId(), OPTIMISTIC);
   compPart.setUom(uom);
   changed = true;
  } else if(compPart.getUom() != null && salesCompPartRec.getUom() == null){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_UOM");
   aud.setOldValue(compPart.getUom().getUomCode());
   compPart.setUom(null);
   changed = true;
    
  } else if((compPart.getUom() != null & salesCompPartRec.getUom() != null) &&
    (!Objects.equals(compPart.getUom().getId(), salesCompPartRec.getUom().getId()))){
   AuditSalesPartCompany aud = this.buildAuditSalesPartCompany(compPart, updateUsr, 'U', page);
   aud.setFieldName("SL_PART_UOM");
   aud.setNewValue(salesCompPartRec.getUom().getUomCode());
   aud.setOldValue(compPart.getUom().getUomCode());
   Uom uom = em.find(Uom.class, salesCompPartRec.getUom().getId(), OPTIMISTIC);
   compPart.setUom(uom);
   changed = true;
   
  }
  
  if(changed){
   compPart.setChangedBy(updateUsr);
   compPart.setChangedOn(chDate);
   salesCompPartRec = this.buildSalesPartCompanyRec(compPart);
   return salesCompPartRec;
  }else{
   return null;
  }
   
  
 }


 public SalesPartRec updateSalespart(SalesPartRec partRec,  String page)  {
  LOGGER.log(INFO, "updateSalespart called with partRec id {0}", partRec.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  SalesPart part  = buildSalesPart(partRec, page);
  if(partRec.getSalesPartCompanies() != null){
   if(part.getSalesPartCompany() == null){
    part.setSalesPartCompany(new ArrayList<SalesPartCompany>());
   }
   ListIterator<SalesPartCompanyRec> ptCompRecLi = partRec.getSalesPartCompanies().listIterator();
   while(ptCompRecLi.hasNext()){
    SalesPartCompanyRec ptCompCurr = ptCompRecLi.next();
    if(ptCompCurr.getPart() == null){
     ptCompCurr.setPart(partRec);
    }
    if(ptCompCurr.getId() == null){
     SalesPartCompany compPt = buildSalesPartCompany(ptCompCurr, page);
     compPt.setPart(part);
     part.getSalesPartCompany().add(compPt);
     ptCompCurr.setId(compPt.getId());
     ptCompRecLi.set(ptCompCurr);
    }else{
     SalesPartCompany compPt = buildSalesPartCompany(ptCompCurr, page);
     boolean found = false;
     ListIterator<SalesPartCompany> compPtLi = part.getSalesPartCompany().listIterator();
     while(compPtLi.hasNext() && !found){
      SalesPartCompany compPtCurr = compPtLi.next();
      if(Objects.equals(compPtCurr.getId(), compPt.getId())){
       compPtLi.set(compPt);
       found = true;
      }
     }
     if(!found){
      part.getSalesPartCompany().add(compPt);
     }
    }
   }
  }
  
  if(partRec.getId() == null){
   partRec.setId(part.getId());
  }
  LOGGER.log(INFO, "Part id {0} comp part id {1}", new Object[]{partRec.getId(),
   partRec.getSalesPartCompanies().get(0).getId()
  });
   
  trans.commit();
  return partRec;
 }
 
 public SalesCatRec updateSalesCat(SalesCatRec catRec, String pg){
  LOGGER.log(INFO, "SalesDM.updateSalesCat called");
  if(!trans.isActive()){
   trans.begin();
  }
  SalesCat cat = buildSalesCat(catRec,pg);
  trans.commit();
  return catRec;
 }

 public void addSalesInfoRecordPvt(DocFi invoice, String invCrn, 
         String page) throws BacException {
  LOGGER.log(INFO, "addSalesInfoRecordPvt called with inv {0} user {1} date {2} page {3}", new Object[]{
   invoice,page });
  
  User usr = invoice.getCreatedBy();
  Date crDate = invoice.getCreateOn();
  
  // crate sales info record
   ListIterator<DocLineBase> linesLi =  invoice.getDocLines().listIterator();
   
   while(linesLi.hasNext()){
    DocLineBase line = linesLi.next();
    LOGGER.log(INFO, "Add sales info for line {0}", line);
    SalesInfo info = new SalesInfo();
    em.persist(info);
    info.setCreatedBy(usr);
    info.setCreatedOn(crDate);
    if(invCrn.equalsIgnoreCase("INV")){
     info.setInvoiceDoc(true);
    }else{
     info.setInvoiceDoc(false);
    }
   
 
 
    
    info.setAmount(invoice.getDocInvoiceAr().getGoodsAmount());
    
    info.setComp(invoice.getCompany());
    info.setDocDate(invoice.getDocumentDate());
    info.setFiscalPeriod(invoice.getFisPeriod());
    info.setFiscalYear(invoice.getFisYear());
    info.setPostingDate(invoice.getPostingDate());
    info.setInvoice(invoice.getDocInvoiceAr());
    info.setOrderedBy(invoice.getDocInvoiceAr().getOrderedBy());
    
    info.setFiDoc(invoice);
    
    
    
    String lineClass = line.getClass().getCanonicalName();
    if(lineClass.endsWith("DocLineAR")){
     // this is AR line
     DocLineAr arLine = (DocLineAr)line;
     info.setArAccount(arLine.getArAccount());
     info.setDueDate(arLine.getDueDate());
     info.setPartner(arLine.getArAccount().getArAccountFor());
     em.persist(info);
    }
    if(lineClass.endsWith("DocLineGL")){
     
     DocLineGl glLine = (DocLineGl)line;
     LOGGER.log(INFO, "GL account type {0} a/c num {1}", new Object[]{ 
             glLine.getGlAccount().getCoaAccount().getAccountType().getName(),
             glLine.getGlAccount().getCoaAccount().getRef()}) ;
     if(glLine.getGlAccount().getCoaAccount().getAccountType().getName().equalsIgnoreCase("SL")){
     info.setSalesGlAc(glLine.getGlAccount());
     LOGGER.log(INFO, "glLine.getSalesPart() {0}", glLine.getSalesPart());
     if(glLine.getSalesPart() != null){
      info.setSalesPartCompany(glLine.getSalesPart());
      if(glLine.getSalesPart().getCategory() != null){
       info.setSalesCategory(glLine.getSalesPart().getCategory());
      }
      info.setAmount(glLine.getDocAmount());
     
      info.setCostOfSales(glLine.getSalesPart().getStockValue());
     
     em.persist(info);
     }
    }
    }
    AuditSalesInfo aud = new AuditSalesInfo();
    aud.setAuditDate(crDate);
    aud.setCreatedBy(usr);
    aud.setNewValue(info.getFiDoc().getPartnerRef());
    aud.setSalesInfo(info);
    aud.setSource(page);
    aud.setUsrAction('I');
    em.persist(aud);
  
   }
   
  
 }
 
 
 
 
}
