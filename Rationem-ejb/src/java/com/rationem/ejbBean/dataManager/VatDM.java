/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.doc.DocGlVatCodeSummary;
import com.rationem.busRec.doc.DocVatSummary;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.*;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.entity.audit.*;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.fi.arap.ArAPAccountIF;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.salesTax.vat.*;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.LocalBean;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;
import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.Persistence;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean

public class VatDM {
 private static final Logger LOGGER = Logger.getLogger(VatDM.class.getName());

 private EntityManager em;
 private EntityTransaction trans;
 
 @EJB
 private UserDM usrDm;
 
 @EJB
 private MasterDataDM masterDataDM;
 
 @EJB
 private SysBuffer sysBuff;

 @PostConstruct
 private void init(){
  LOGGER.log(INFO,  "Loaded config data manager");
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
   trans = em.getTransaction();
 }

 private VatIndustryFlatRate buildVatFlatRateIndRate(VatFlatRateIndRateRec rec, UserRec usr,String pg){
  VatIndustryFlatRate r;
  boolean newFlRate = false;
  boolean changed = false;
  if(rec.getId() == null || rec.getId() == 0){
   r = new VatIndustryFlatRate();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   r.setCreatedBy(crUsr);
   r.setCreatedOn(rec.getCreatedOn());
   em.persist(r);
   AuditVatIndustryFlatRate aud = this.buildAuditFlatRate(r, crUsr, r.getCreatedOn(), pg);
   aud.setNewValue(r.getRef());
   aud.setUsrAction('I');
   newFlRate = true;
  }else{
   r = em.find(VatIndustryFlatRate.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
  }
  
  if(newFlRate){
  
   r.setDescr(rec.getDescr());
   r.setIndustry(rec.getIndustry());
   r.setRate(rec.getRate());
   r.setRef(rec.getRef());
   if(rec.getValidFrom() != null){
   r.setValidFrom(rec.getValidFrom());
   }
   if(rec.getValidTo() != null){
    r.setValidTo(rec.getValidTo());
   } 
  }else{
   // changed
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   if(!r.getDescr().equalsIgnoreCase(rec.getDescr())){
    AuditVatIndustryFlatRate aud = this.buildAuditFlatRate(r, chUsr, r.getChangedOn(), pg);
    aud.setFieldName("VAT_FL_DESCR");
    aud.setNewValue(rec.getDescr());
    aud.setOldValue(r.getDescr());
    aud.setUsrAction('U');
    r.setDescr(rec.getDescr());
    changed = true;
   }
   
   if(!r.getIndustry().equalsIgnoreCase(rec.getIndustry())){
    AuditVatIndustryFlatRate aud = this.buildAuditFlatRate(r, chUsr, r.getChangedOn(), pg);
    aud.setFieldName("VAT_FL_IND");
    aud.setNewValue(rec.getIndustry());
    aud.setOldValue(r.getIndustry());
    aud.setUsrAction('U');
    r.setIndustry(rec.getIndustry());
    changed = true;
   }
   if(r.getRate() != rec.getRate()){
    AuditVatIndustryFlatRate aud = this.buildAuditFlatRate(r, chUsr, r.getChangedOn(), pg);
    aud.setFieldName("VAT_FL_RATE");
    aud.setNewValue(String.valueOf(rec.getRate()));
    aud.setOldValue(String.valueOf(r.getRate()));
    aud.setUsrAction('U');
    r.setRate(rec.getRate());
    changed = true;
   }
   
   if(!r.getRef().equalsIgnoreCase(rec.getRef())){
    AuditVatIndustryFlatRate aud = this.buildAuditFlatRate(r, chUsr, r.getChangedOn(), pg);
    aud.setFieldName("VAT_FL_REF");
    aud.setNewValue(rec.getRef());
    aud.setOldValue(r.getRef());
    aud.setUsrAction('U');
    r.setRef(rec.getRef());
    changed = true;
   }
   
   if(!r.getValidFrom().equals(rec.getValidFrom())){
    
   }
   if(rec.getValidFrom() != null){
   r.setValidFrom(rec.getValidFrom());
   }
   if(rec.getValidTo() != null){
    r.setValidTo(rec.getValidTo());
   }
   
   if(changed){
    r.setChangedBy(chUsr);
    r.setChangedOn(rec.getChangedOn());
   }
  }
  
  
  
  
  
  
  return r;
 }
 
 private VatFlatRateIndRateRec buildVatFlatRateIndRateRec(VatIndustryFlatRate r){
  VatFlatRateIndRateRec rec = new VatFlatRateIndRateRec();
  rec.setId(r.getId());
  UserRec crUsr = usrDm.getUserRecPvt(r.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(r.getCreatedOn());
  if(r.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(r.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(rec.getChangedOn());
  }
  rec.setDescr(r.getDescr());
  rec.setIndustry(r.getIndustry());
  rec.setRate(r.getRate());
  rec.setRef(r.getRef());
  if(r.getValidFrom() != null){
   rec.setValidFrom(r.getValidFrom());
  }
  if(r.getValidTo() != null){
   rec.setValidTo(r.getValidTo());
  }
  
  
  
  return rec;
 }
 private VatCodeRec buildVatCodeRec(VatCode cd){
  LOGGER.log(INFO, "buildVatCodeRec called with {0}", cd);
  VatCodeRec rec = new VatCodeRec();
  rec.setId(cd.getId());
  if(cd.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(cd.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(cd.getChangedOn());
  }
  rec.setAddnlCat(cd.getAddnlCat());
  rec.setCode(cd.getCode());
  LOGGER.log(INFO, "created user  {0}", cd.getCreatedBy());
  User usr = cd.getCreatedBy();
  LOGGER.log(INFO, "usr  {0}", usr);
  UserRec usrCr = this.usrDm.getUserRecPvt(usr);
  rec.setCreatedBy(usrCr);
  rec.setCreatedOn(cd.getCreatedOn());
  rec.setDescription(cd.getDescription());
  rec.getIrrrecoverableRate();
  rec.setRate(cd.getRate());
  rec.setValidFrom(cd.getValidFrom());
  rec.setValidTo(cd.getValidTo());
  rec.setImportExport(cd.getImportExport());
  rec.setTaxType(cd.getTaxType());
  rec.setInterStatSupply(cd.isInterStatSupply());
  rec.setInputTax(cd.isInputTax());
  rec.setVatRule(cd.isVatRule());
  
  return rec;
 }
 
 public VatCodeRec buildVatCodeRecPvt(VatCode cd){
  LOGGER.log(INFO, "buildVatCodeRecPvt called with vat code {0}", cd);
  VatCodeRec vatCode = buildVatCodeRec(cd);
  return vatCode;
  
 }
 private VatCodeCompanyRec buildVatCodeCompRec(VatCodeCompany comp){
  VatCodeCompanyRec rec = new VatCodeCompanyRec();
  rec.setId(comp.getId());
  UserRec crUsr = usrDm.getUserRecPvt(comp.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(comp.getCreatedOn());
  if(comp.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(comp.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(comp.getChangedOn());
  }
  
  rec.setChanges(comp.getChanges());
  if(comp.getCompany() == null){
   LOGGER.log(INFO, "no company");
   return rec;
  }
  
  if(comp.getVatCode() == null){
   LOGGER.log(INFO, "No VAT code");
   throw new BacException("NO VAT code for company VAT");
  }
  CompanyBasicRec compRec = sysBuff.getCompanyById(comp.getCompany().getId());
  rec.setCompany(compRec);
  
  VatCodeRec vatcd = this.buildVatCodeRec(comp.getVatCode());
  rec.setVatCode(vatcd);
  
  
  List<FiGlAccountCompRec> compGlAcs = compRec.getGlAccounts();
  if(comp.getChargeGlAccount() != null){
   Long chargeGlId = comp.getChargeGlAccount().getId();
   ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    FiGlAccountCompRec glAc = li.next();
    if(Objects.equals(glAc.getId(), chargeGlId) ){
     rec.setChargeGlAccount(glAc);
     found = true;
    }
   }
   
  }
  if(comp.getVatGlAccount() != null){
   ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    FiGlAccountCompRec glAc = li.next();
    if(Objects.equals(glAc.getId(), comp.getVatGlAccount().getId()) ){
     rec.setRateGlAccount(glAc);
     found = true;
    }
   }
  }
  if(comp.getProvnGlAccount() != null){
   ListIterator<FiGlAccountCompRec> li = compGlAcs.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    FiGlAccountCompRec glAc = li.next();
    if(Objects.equals(glAc.getId(), comp.getProvnGlAccount().getId()) ){
     rec.setAccrualGlAccount(glAc);
     found = true;
    }
   }
  }
  
  return rec;
   
 }
 
 private AuditCompany buildAuditCompany(CompanyBasic comp, char usrAction ,User usr,  String pg ){
  AuditCompany aud = new AuditCompany();
  aud.setAuditDate(new Date());
  aud.setComp(comp);
  aud.setUsrAction(usrAction);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  em.persist(aud);
  return aud;
 }
 private AuditVatIndustryFlatRate buildAuditFlatRate(VatIndustryFlatRate flRate, User usr, Date dt, String pg){
  AuditVatIndustryFlatRate aud = new AuditVatIndustryFlatRate();
  aud.setAuditDate(dt);
  aud.setCreatedBy(usr);
  aud.setIndRate(flRate);
  aud.setSource(pg);
  em.persist(aud);
  return aud;
 }
 
 private AuditVatRegistration buildAuditVatRegistration(VatRegistration vatReg, char usrAction,User usr, String pg){
  AuditVatRegistration aud = new AuditVatRegistration();
  aud.setAuditDate(new Date());
  aud.setSource(pg);
  aud.setCreatedBy(usr);
  aud.setUsrAction(' ');
  aud.setVatRegistration(vatReg);
  em.persist(aud);
  return aud;
   }
 
 private AuditVatCode buildAuditVatCode(VatCode vatCode, char usrAction, User usr, String pg){
  AuditVatCode aud = new AuditVatCode();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  aud.setVatCode(vatCode);
  em.persist(aud);
  return aud;
 }
 
 private AuditVatCodeComp buildAuditVatCodeComp(VatCodeCompany vatComp, char usrAction, User usr, String pg){
  AuditVatCodeComp aud = new AuditVatCodeComp();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  aud.setVatCodeComp(vatComp);
  em.persist(aud);
  return aud;
 }
 private AuditVatRegScheme buildAuditVatRegScheme(VatRegScheme scheme,char usrAction, User usr, String pg){
  AuditVatRegScheme aud = new AuditVatRegScheme();
  aud.setAuditDate(new Date());
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  aud.setCreatedBy(usr);
  aud.setVatRegScheme(scheme);
  em.persist(aud);
  return aud;
  
 }
 private AuditVatScheme buildAuditVatScheme(VatScheme sch, User usr, Date dt, String pg){
  AuditVatScheme aud = new AuditVatScheme();
  aud.setAuditDate(dt);
  aud.setSource(pg);
  aud.setCreatedBy(usr);
  aud.setVatScheme(sch);
  em.persist(aud);
  return aud;
 }
  private VatCodeCompany buildVatCodeComp(VatCodeCompanyRec v, String pg){
  VatCodeCompany vatComp;
  boolean newVatComp = false;
  boolean changedVatComp = false;
  if(v.getId() == null  ){
   vatComp = new VatCodeCompany();
   User crUsr = em.find(User.class, v.getCreatedBy().getId(), OPTIMISTIC);
   vatComp.setCreatedBy(crUsr);
   vatComp.setCreatedOn(v.getCreatedOn());
   em.persist(vatComp);
   AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'I', crUsr, pg);
   LOGGER.log(INFO, "vatCode {0} company {1} aud {2}", new Object[]{v.getVatCode(),v.getCompany(),aud});
   aud.setNewValue(v.getVatCode().getCode() + v.getCompany().getReference());
   newVatComp = true;
  }else{
   vatComp = em.find(VatCodeCompany.class, v.getId(), OPTIMISTIC_FORCE_INCREMENT);
  }
  
  if(newVatComp){
   CompanyBasic comp = em.find(CompanyBasic.class, v.getCompany().getId(), OPTIMISTIC);
   vatComp.setCompany(comp);
   
   if(v.getChargeGlAccount() != null){
    FiGlAccountComp glAc = em.find(FiGlAccountComp.class, v.getChargeGlAccount().getId(), OPTIMISTIC);
    vatComp.setChargeGlAccount(glAc);
   }
   if(v.getAccrualGlAccount() != null){
    FiGlAccountComp glAc = em.find(FiGlAccountComp.class, v.getAccrualGlAccount().getId(), OPTIMISTIC);
    vatComp.setProvnGlAccount(glAc);
   }
   
   if(v.getRateGlAccount() != null){
   FiGlAccountComp glAc = em.find(FiGlAccountComp.class, v.getRateGlAccount().getId(), OPTIMISTIC);
    vatComp.setVatGlAccount(glAc);
   }
   VatCode vatCode = em.find(VatCode.class, v.getVatCode().getId(), OPTIMISTIC);
   vatComp.setVatCode(vatCode);
   vatComp.setIrrecoverRate(v.getIrrecoverRate());
   vatComp.setNoIrrecoverableLine(v.isNoIrrecoverableLine());
   vatComp.setChargeSingleGl(v.isWoffIrrecoverable());
  }else{
   // changed
   User chUsr = em.find(User.class, v.getChangedBy().getId(), OPTIMISTIC);
   if(v.isNoIrrecoverableLine() != vatComp.isNoIrrecoverableLine()){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_IRRECOVER_LN");
    aud.setNewValue(String.valueOf(v.isNoIrrecoverableLine()));
    aud.setOldValue(String.valueOf(vatComp.isNoIrrecoverableLine()));
    vatComp.setNoIrrecoverableLine(v.isNoIrrecoverableLine());
    changedVatComp = true;
   }
   
   if(v.isWoffIrrecoverable() != vatComp.isChargeSingleGl()){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_SINGL_WO_ACNT");
    aud.setNewValue(String.valueOf(v.isWoffIrrecoverable()));
    aud.setOldValue(String.valueOf(vatComp.isChargeSingleGl()));
    vatComp.setChargeSingleGl(v.isWoffIrrecoverable());
    changedVatComp = true;
   }
   
   if((v.getChargeGlAccount() == null && vatComp.getChargeGlAccount() != null) ||
      (v.getChargeGlAccount() != null && vatComp.getChargeGlAccount() == null) ||
      (v.getChargeGlAccount() != null && !Objects.equals(v.getChargeGlAccount().getId(), vatComp.getChargeGlAccount().getId()))){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_CHARGE_GL");
    aud.setNewValue(v.getChargeGlAccount().getCoaAccount().getRef());
    aud.setOldValue(vatComp.getChargeGlAccount().getCoaAccount().getRef());
    FiGlAccountComp chargeGl = em.find(FiGlAccountComp.class, v.getChargeGlAccount().getId(), OPTIMISTIC);
    vatComp.setChargeGlAccount(chargeGl);
    changedVatComp = true;
   }
   
   if((v.getCompany() == null && vatComp.getCompany() != null) ||
      (v.getCompany() != null && vatComp.getCompany() == null) ||
      ((v.getCompany() != null && !Objects.equals(v.getCompany().getId(), vatComp.getCompany().getId())))){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_COMP");
    aud.setNewValue(v.getCompany().getReference());
    aud.setOldValue(vatComp.getCompany().getNumber());
    CompanyBasic comp = em.find(CompanyBasic.class, v.getCompany().getId(), OPTIMISTIC);
    vatComp.setCompany(comp);
    changedVatComp = true;
   }
   
   if(v.getIrrecoverRate() != vatComp.getIrrecoverRate()){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_IRRECOVER_RATE");
    aud.setNewValue(String.valueOf(v.getIrrecoverRate()));
    aud.setOldValue(String.valueOf(vatComp.getIrrecoverRate()));
    vatComp.setIrrecoverRate(v.getIrrecoverRate());
    changedVatComp = true;
   }
   
   if((v.getRateGlAccount() == null && vatComp.getVatGlAccount() != null) ||
      (v.getRateGlAccount() != null && vatComp.getVatGlAccount() == null) ||       
      (v.getRateGlAccount() != null && !Objects.equals(v.getRateGlAccount().getId(), vatComp.getVatGlAccount().getId()))){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_RATE_GL");
    aud.setNewValue(v.getRateGlAccount().getCoaAccount().getRef());
    aud.setOldValue(vatComp.getVatGlAccount().getCoaAccount().getRef());
    FiGlAccountComp rateGl = em.find(FiGlAccountComp.class, v.getRateGlAccount().getId(), OPTIMISTIC);
    vatComp.setVatGlAccount(rateGl);
    changedVatComp = true;
   }
   
   if((v.getVatCode() == null && vatComp.getVatCode() != null) ||
      (v.getVatCode() != null && vatComp.getVatCode() == null) ||
      (v.getVatCode() != null && !Objects.equals(v.getVatCode().getId(), vatComp.getVatCode().getId()))){
    AuditVatCodeComp aud = buildAuditVatCodeComp(vatComp, 'U', chUsr, pg);
    aud.setFieldName("VAT_COMP_VAT_CD");
    aud.setNewValue(v.getVatCode().getCode());
    aud.setOldValue(vatComp.getVatCode().getCode());
    VatCode vatCode = em.find(VatCode.class, v.getVatCode().getId(), OPTIMISTIC);
    vatComp.setVatCode(vatCode);
    changedVatComp = true;
   }
   if(changedVatComp){
    vatComp.setChangedBy(chUsr);
    vatComp.setChangedOn(v.getChangedOn());
   }
  }
  return vatComp;
   
 }
 
 private VatCode buildVatCode(VatCodeRec rec,  String pg){
  VatCode cd;
  boolean newVatCode = false;
  boolean vatCodeChanged = false;
  if(rec.getId() == null || rec.getId() == 0){
   cd = new VatCode();
   User  crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   cd.setCreatedBy(crUsr);
   cd.setCreatedOn(rec.getCreatedOn());
   em.persist(cd);
   AuditVatCode aud = this.buildAuditVatCode(cd, 'I', crUsr, pg);
   aud.setNewValue(rec.getCode());
   newVatCode = true;
  }else{
   cd = em.find(VatCode.class, rec.getId());
  }
  
  if(newVatCode){
   cd.setAddnlCat(rec.getAddnlCat());
   cd.setCode(rec.getCode());
   cd.setDescription(rec.getDescription());
   cd.setIrrrecoverableRate(rec.getIrrrecoverableRate());
   cd.setRate(rec.getRate());
   cd.setValidFrom(rec.getValidFrom());
   cd.setValidTo(rec.getValidTo());
   cd.setImportExport(rec.getImportExport());
   cd.setTaxType(rec.getTaxType());
   cd.setInterStatSupply(rec.isInterStatSupply());
   cd.setInputTax(rec.isInputTax());
   cd.setVatRule(rec.isVatRule());
  }else{
   //changed ?
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   if(!rec.getCode().equalsIgnoreCase(cd.getCode())){
    AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
    aud.setFieldName("VAT_CD_CODE");
    aud.setNewValue(rec.getCode());
    aud.setOldValue(cd.getCode());
    cd.setCode(rec.getCode());
    vatCodeChanged = true;
   }
   
   if(rec.getAddnlCat() != cd.getAddnlCat()){
    AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
    aud.setFieldName("VAT_CD_ADDNL_CAT");
    aud.setNewValue(String.valueOf(rec.getAddnlCat()));
    aud.setOldValue(String.valueOf(cd.getAddnlCat()));
    cd.setAddnlCat(rec.getAddnlCat());
    vatCodeChanged = true;
   }
   if((rec.getDescription() == null && cd.getDescription() != null) ||
      (rec.getDescription() != null && cd.getDescription() == null) ||  
      (rec.getDescription() != null && !rec.getDescription().equalsIgnoreCase(cd.getDescription()))){
    AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
    aud.setFieldName("VAT_CD_DESCR");
    aud.setNewValue(rec.getDescription());
    aud.setOldValue(cd.getDescription());
    cd.setDescription(rec.getDescription());
    vatCodeChanged = true;
   }
   
   if(rec.getIrrrecoverableRate() != cd.getIrrrecoverableRate()){
    AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
    aud.setFieldName("VAT_CD_IRREC");
    aud.setNewValue(String.valueOf(rec.getIrrrecoverableRate()));
    aud.setOldValue(String.valueOf(cd.getIrrrecoverableRate()));
    cd.setIrrrecoverableRate(rec.getIrrrecoverableRate());
    vatCodeChanged = true;
   }
   
   if(rec.getRate() != cd.getRate()){
    AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
    aud.setFieldName("VAT_CD_RATE");
    aud.setNewValue(String.valueOf(rec.getRate()));
    aud.setOldValue(String.valueOf(cd.getRate()));
    cd.setIrrrecoverableRate(rec.getRate());
    vatCodeChanged = true;
   }
    if((rec.getValidFrom() == null && cd.getValidFrom() != null ) ||
       (rec.getValidFrom() != null && cd.getValidFrom() == null ) ||
       (rec.getValidFrom() != null && ! rec.getValidFrom().equals(cd.getValidFrom()))    ){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_VALID_FR");
     aud.setNewValue(rec.getValidFrom().toString());
     aud.setOldValue(cd.getValidFrom().toString());
     cd.setValidFrom(rec.getValidFrom());
     vatCodeChanged = true;
    }
    
    if((rec.getValidTo() == null && cd.getValidTo() != null ) ||
       (rec.getValidTo() != null && cd.getValidTo() == null ) ||
       (rec.getValidTo() != null && ! rec.getValidTo().equals(cd.getValidTo()))    ){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_VALID_TO");
     aud.setNewValue(rec.getValidTo().toString());
     aud.setOldValue(cd.getValidTo().toString());
     cd.setValidFrom(rec.getValidTo());
     vatCodeChanged = true;
    }
    
    if(rec.getImportExport() != cd.getImportExport()){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_IMP_EXP");
     aud.setNewValue(String.valueOf(rec.getImportExport()));
     aud.setOldValue(String.valueOf(cd.getImportExport()));
     cd.setImportExport(rec.getImportExport());
     vatCodeChanged = true;
    }
    
    if(rec.getTaxType() != cd.getImportExport()){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_TAX_TY");
     aud.setNewValue(String.valueOf(rec.getTaxType()));
     aud.setOldValue(String.valueOf(cd.getTaxType()));
     cd.setTaxType(rec.getTaxType());
     vatCodeChanged = true;
    }
    
    if(rec.isInterStatSupply() != cd.isInterStatSupply()){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_IMPORT");
     aud.setNewValue(String.valueOf(rec.isInterStatSupply()));
     aud.setOldValue(String.valueOf(cd.isInterStatSupply()));
     cd.setInterStatSupply(rec.isInterStatSupply());
     vatCodeChanged = true;
    }
    
    if(rec.isInputTax() != cd.isInputTax()){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_INPUT");
     aud.setNewValue(String.valueOf(rec.isInputTax()));
     aud.setOldValue(String.valueOf(cd.isInputTax()));
     cd.setInputTax(rec.isInputTax());
     vatCodeChanged = true;
    }
    
    if(rec.isVatRule() != cd.isVatRule()){
     AuditVatCode aud = this.buildAuditVatCode(cd, 'U', chUsr, pg);
     aud.setFieldName("VAT_CD_RULE");
     aud.setNewValue(String.valueOf(rec.isVatRule()));
     aud.setOldValue(String.valueOf(cd.isVatRule()));
     cd.setVatRule(rec.isVatRule());
     vatCodeChanged = true;
    }
    
    if(vatCodeChanged){
     cd.setChangedBy(chUsr);
     cd.setChangedOn(rec.getChangedOn());
    }
  } 
  
  
  return cd;
 }
 
 public VatRegistrationRec buildVatRegistrationRecPvt(VatRegistration reg){
  return buildVatRegistrationRec(reg);
 }
 private VatRegistrationRec buildVatRegistrationRec(VatRegistration reg){
  LOGGER.log(INFO, "Build vatReg id {0} end date {1}", new Object[]{reg.getId(),reg.getRegistrationEnd()});
  VatRegistrationRec rec = new VatRegistrationRec();
  rec.setId(reg.getId());
  if(reg.getChangedBy() != null ){
   UserRec chUsr = this.usrDm.getUserRecPvt(reg.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(reg.getChangedOn());
  }
  UserRec crUsr = this.usrDm.getUserRecPvt(reg.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(reg.getCreatedOn());
  
  LOGGER.log(INFO, "VAT inspector {0}", reg.getInspector());
  if(reg.getInspector() != null){
   PartnerPersonRec insp = masterDataDM.buildPartnerPersonRecPvt(reg.getInspector());
   rec.setInspector(insp);
  }
  rec.setRegistrationDate(reg.getRegistrationDate());
  
  rec.setRegistrationEnd(reg.getRegistrationEnd());
  LOGGER.log(INFO, "VAT reg end date on build vatRegRec {0}", rec.getRegistrationEnd());
  rec.setVatNumber(reg.getVatNumber());
  rec.setVatOffice(reg.getVatOffice());
  LOGGER.log(INFO,"VatOffice {0}",rec.getVatOffice());
  if(reg.getVatOfficeAddress() != null){
   AddressRec off = masterDataDM.getAddressRec(reg.getVatOfficeAddress());
   rec.setVatOfficeAddress(off);
  }
  rec.setActiveReg(reg.isActiveReg());
  /*
  
  */
  
  
  
  return rec;
  
 }
 private VatRegistration buildVatRegistration(VatRegistrationRec rec, UserRec usr, String pg){
  LOGGER.log(INFO, "buildVatRegistration called with id {0}", rec.getId());
  VatRegistration reg;
  boolean newVatReg = false;
  boolean changedVatReg = false;
   boolean compChanged = false;
  
  if(rec.getId() == null || rec.getId() == 0){
   reg = new VatRegistration();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   reg.setCreatedBy(crUsr);
   reg.setCreatedOn(rec.getCreatedOn());
   em.persist(reg);
   AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', crUsr, pg);
   aud.setNewValue(reg.getVatNumber());
   aud.setUsrAction('I');
   
   LOGGER.log(INFO, "Vat Reg id is now {0}", reg.getId());
   newVatReg = true;
  }else{
   reg = em.find(VatRegistration.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
  }
  
  if(newVatReg){
   CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
   reg.setComp(comp);
   comp.setVatRegCurrent(reg);
   if(rec.getInspector() != null){
    User insp = em.find(User.class, rec.getInspector().getId(), OPTIMISTIC);
    reg.setInspector(insp);
   }
   reg.setRegistrationDate(rec.getRegistrationDate());
   reg.setRegistrationEnd(rec.getRegistrationEnd());
   reg.setVatNumber(rec.getVatNumber());
   reg.setVatOffice(rec.getVatOffice());
   if(rec.getVatOfficeAddress() != null && rec.getVatOfficeAddress().getId() != null){
    Address off = em.find(Address.class, rec.getVatOfficeAddress().getId(),  OPTIMISTIC);
    reg.setVatOfficeAddress(off);
   }
   reg.setActiveReg(true);
   
  }else{
   //changed?
   User chUsr = em.find(User.class, rec.getChangedBy().getId(),OPTIMISTIC);
   if((rec.getComp() == null && reg.getComp() != null)||
       (rec.getComp() != null && reg.getComp() == null) ||
      (rec.getComp() != null && !Objects.equals(rec.getComp().getId(), rec.getComp().getId()))){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg, 'U',chUsr, pg);
    aud.setFieldName("VAT_REG_COMP");
    aud.setNewValue(rec.getComp().getReference());
    aud.setOldValue(reg.getComp().getNumber());
    
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
    reg.setComp(comp);
    changedVatReg = true;
   }
   
   if((rec.getInspector() == null && reg.getInspector() != null) ||
      (rec.getInspector() != null && reg.getInspector() == null) ||
       (rec.getInspector() != null && !Objects.equals(rec.getInspector().getId(), reg.getInspector().getId()))
     ){
     AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
     aud.setFieldName("VAT_REG_INSP");
     aud.setNewValue(rec.getInspector().getFamilyName());
     if(reg.getInspector() != null){
      aud.setOldValue(reg.getInspector().getFamilyName() );
     }
     aud.setUsrAction('U');
     PartnerPerson insp = em.find(PartnerPerson.class, rec.getInspector().getId(), OPTIMISTIC);
     reg.setInspector(insp);
     changedVatReg = true;
   }
   
   if(!rec.getRegistrationDate().equals(reg.getRegistrationDate())){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_REG_DT");
    aud.setNewValue(rec.getRegistrationDate().toString());
    aud.setOldValue((reg.getRegistrationDate().toString()));
    aud.setUsrAction('U');
    reg.setRegistrationDate(rec.getRegistrationDate());
    changedVatReg = true;
   }
   
   if(!rec.getRegistrationEnd().equals(reg.getRegistrationEnd())){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_DEREG_DT");
    aud.setNewValue(rec.getRegistrationEnd().toString());
    aud.setOldValue(reg.getRegistrationEnd().toString());
    reg.setRegistrationEnd(rec.getRegistrationEnd());
    changedVatReg = true;
   }
   
   if((rec.getVatNumber() == null && reg.getVatNumber() != null) ||
      (rec.getVatNumber() != null && reg.getVatNumber() == null) ||
      (rec.getVatNumber() != null && rec.getVatNumber().equalsIgnoreCase(reg.getVatNumber()))
     ){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_NUM");
    aud.setNewValue(rec.getVatNumber());
    aud.setOldValue(reg.getVatNumber());
    reg.setVatNumber(rec.getVatNumber());
    changedVatReg = true;
   }
  
   if((rec.getVatOffice() == null && reg.getVatOffice() != null) ||
      (rec.getVatOffice() != null && reg.getVatOffice() == null) ||
      (rec.getVatOffice() != null && !rec.getVatOffice().equalsIgnoreCase(reg.getVatOffice()))
      ){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_OFF");
    aud.setNewValue(rec.getVatOffice());
    aud.setOldValue(reg.getVatOffice());
    reg.setVatOffice(rec.getVatOffice());
    changedVatReg = true;
   }
   
   if((rec.getVatOfficeAddress() == null && reg.getVatOfficeAddress() != null ) ||
      (rec.getVatOfficeAddress() != null && reg.getVatOfficeAddress() == null ) ||
      (rec.getVatOfficeAddress() != null && !Objects.equals(rec.getVatOfficeAddress().getId(), reg.getVatOfficeAddress().getId())) 
     ){
    AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_OFF_ADDR");
    aud.setNewValue(rec.getVatOfficeAddress().getAddrRef());
    aud.setOldValue(reg.getVatOfficeAddress().getAddrRef());
    Address off = em.find(Address.class, rec.getVatOfficeAddress().getId(),  OPTIMISTIC);
    reg.setVatOfficeAddress(off);
    changedVatReg = true;
   }
   
   if(rec.isActiveReg() != reg.isActiveReg()){
    boolean vatRegChanged = false;

 if(!reg.isActiveReg()){
  AuditVatRegistration aud = this.buildAuditVatRegistration(reg,'U', chUsr, pg);
  aud.setFieldName("VAT_REG_ACTIVE");
  aud.setNewValue(String.valueOf(true));
  aud.setOldValue(String.valueOf(false));
  aud.setUsrAction('U');
  
  reg.setActiveReg(true);
  vatRegChanged = true;
 }
 
 LOGGER.log(INFO, "vatRegRec.getComp() rec comp {0} vatReg comp {1}", 
         new Object[]{rec.getComp(),reg.getComp()});
 if((rec.getComp() == null && reg.getComp() != null )||
    (rec.getComp() != null && reg.getComp() == null ) ||
    (rec.getComp() != null && !Objects.equals(rec.getComp().getId(), reg.getComp().getId()) )  ){
  CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
  AuditVatRegistration aud = this.buildAuditVatRegistration(reg, 'U',chUsr, pg);
  aud.setFieldName("VAT_REG_COMP");
  aud.setNewValue(rec.getComp().getReference());
  aud.setOldValue(reg.getComp().getNumber());
  aud.setUsrAction('U');
  reg.setComp(comp);
  vatRegChanged = true;
 }
 
 if((rec.getComp().getVatRegDetails() == null && reg.getComp().getVatRegCurrent()  != null) ||
    (rec.getComp().getVatRegDetails() != null && reg.getComp().getVatRegCurrent()  == null) ||
    (!Objects.equals(rec.getComp().getVatRegDetails().getId(), reg.getComp().getVatRegCurrent().getId()))    ){
  CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
  AuditCompany aud = this.buildAuditCompany(comp,'U', chUsr, pg);
  aud.setFieldName("COMP_VAT_CURR_REG");
  aud.setNewValue(rec.getComp().getVatRegDetails().getVatNumber());
  aud.setOldValue(reg.getComp().getVatRegCurrent().getVatNumber());
  //aud.setUsrAction('U');
  comp.setVatRegCurrent(reg);
  compChanged = true;
 }
   }
   
   if(changedVatReg){
    reg.setChangedBy(chUsr);
    reg.setChangedOn(rec.getChangedOn());
   }
   
   if(compChanged){
    
   }
     }
  
  return reg;
 }
 
 private VatRegScheme buildVatRegScheme(VatRegSchemeRec rec, UserRec usr, String pg){
  VatRegScheme sch;
  boolean newVatSch = false;
  boolean vatSchChanged = false;
  if(rec.getId() == null || rec.getId() == 0){
   sch = new VatRegScheme();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   sch.setCreatedBy(crUsr);
   sch.setCreatedOn(rec.getCreatedOn());
   em.persist(sch);
   AuditVatRegScheme aud = buildAuditVatRegScheme(sch,'I', crUsr, pg);
   
   newVatSch = true;
  }else{
   sch = em.find(VatRegScheme.class, rec.getId(), OPTIMISTIC);
  }
  
  if(newVatSch){
   sch.setActive(rec.isActive());
   sch.setDescr(rec.getDescription());
   sch.setReference(rec.getRef());
   sch.setValidFrom(rec.getValidFrom());
   sch.setValidTo(rec.getValidTo());
   VatScheme vatScheme = em.find(VatScheme.class, rec.getVatScheme().getId(), OPTIMISTIC);
   sch.setVatScheme(vatScheme);
  }else{
   //changed ?
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   if(sch.isActive() != rec.isActive()){
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_ACTIVE");
    aud.setNewValue(String.valueOf(rec.isActive()));
    aud.setNewValue(String.valueOf(sch.isActive()));
    aud.setUsrAction('U');
    sch.setActive(rec.isActive());
    vatSchChanged = true;
   }
   
   if(rec.isFlatRate() != sch.isFlatRate()){
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_FLAT");
    aud.setNewValue(String.valueOf(rec.isFlatRate()));
    aud.setNewValue(String.valueOf(sch.isFlatRate()));
    aud.setUsrAction('U');
    sch.setActive(rec.isFlatRate());
    vatSchChanged = true;
   }
   
   if((rec.getDescription() == null && sch.getDescr() != null) ||
      (rec.getDescription() != null && sch.getDescr() == null) ||
      (rec.getDescription() != null && !rec.getDescription().equalsIgnoreCase(sch.getDescr()))){
    LOGGER.log(INFO, "update Description");

     AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
     aud.setFieldName("VAT_REG_SCH_DESCR");
     aud.setNewValue(rec.getDescription());
     aud.setNewValue(sch.getDescr());
     aud.setUsrAction('U');
     sch.setDescr(rec.getDescription());
     vatSchChanged = true;
   }
   
   if((rec.getRef() == null && sch.getReference() != null) ||
      (rec.getRef() != null && sch.getReference() != null) ||
      (rec.getRef() != null && !rec.getRef().equalsIgnoreCase(sch.getReference()))){
    LOGGER.log(INFO, "update reference");
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_REF");
    aud.setNewValue(rec.getRef());
    aud.setNewValue(sch.getReference());
    aud.setUsrAction('U');
    sch.setReference(rec.getRef());
    vatSchChanged = true;
   }
   
   if(!rec.getValidFrom().equals(sch.getValidFrom())){
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_VALID_FR");
    aud.setNewValue(rec.getValidFrom().toString());
    aud.setNewValue(sch.getValidFrom().toString());
    aud.setUsrAction('U');
    sch.setValidFrom(rec.getValidFrom());
    vatSchChanged = true;
   }
   if(!rec.getValidTo().equals(sch.getValidTo())){
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_VALID_TO");
    aud.setNewValue(rec.getValidTo().toString());
    aud.setNewValue(sch.getValidTo().toString());
    aud.setUsrAction('U');
    sch.setValidFrom(rec.getValidTo());
    vatSchChanged = true;
   }
   
   if(!Objects.equals(rec.getVatScheme().getId(), sch.getVatScheme().getId())){
    AuditVatRegScheme aud = this.buildAuditVatRegScheme(sch,'U', chUsr, pg);
    aud.setFieldName("VAT_REG_SCH_SCHEME");
    aud.setNewValue(rec.getVatScheme().getRef());
    aud.setNewValue(sch.getVatScheme().getRef());
    aud.setUsrAction('U');
    VatScheme scheme = em.find(VatScheme.class, rec.getVatScheme().getId(), OPTIMISTIC);
    sch.setVatScheme(scheme);
    vatSchChanged = true;
   }
   if(vatSchChanged){
    sch.setChangedBy(chUsr);
    sch.setChangedOn(rec.getChangedOn());
   }
  }
  
  return sch;
 }
 
 private VatRegSchemeRec buildVatRegSchemeRec(VatRegScheme rec, VatRegistrationRec vatReg){
  VatRegSchemeRec sch = new VatRegSchemeRec();
  sch.setId(rec.getId());
  
  UserRec crUsr = usrDm.getUserRecPvt(rec.getCreatedBy());
  sch.setCreatedBy(crUsr);
  sch.setCreatedOn(rec.getCreatedOn());
  sch.setActive(rec.isActive());
  if(rec.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(rec.getChangedBy());
   sch.setChangedBy(chUsr);
   sch.setChangedOn(rec.getChangedOn());
  }
  sch.setDescription(rec.getDescr());
  sch.setRef(rec.getReference());
  if(rec.getValidFrom() != null){
   sch.setValidFrom(rec.getValidFrom());
  }
  if(vatReg == null){
   vatReg = this.buildVatRegistrationRecPvt(rec.getVatReg());
  }
  sch.setVatReg(vatReg);
  
  if(rec.getValidTo() != null){
   sch.setValidTo(rec.getValidTo());
  }
  
  if(rec.getVatScheme() != null){
   VatSchemeRec vatScheme = this.buildVatSchemeRec(rec.getVatScheme());
   sch.setVatScheme(vatScheme);
  }
  LOGGER.log(INFO, "return VatRegScheme for vat reg {0}", sch.getVatReg());
  return sch;
 }
 
 private VatRegSchemeRec buildVatRegSchemeRec(VatRegScheme rec){
  VatRegSchemeRec sch = new VatRegSchemeRec();
  sch.setId(rec.getId());
  
  UserRec crUsr = usrDm.getUserRecPvt(rec.getCreatedBy());
  sch.setCreatedBy(crUsr);
  sch.setCreatedOn(rec.getCreatedOn());
  sch.setActive(rec.isActive());
  if(rec.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(rec.getChangedBy());
   sch.setChangedBy(chUsr);
   sch.setChangedOn(rec.getChangedOn());
  }
  sch.setDescription(rec.getDescr());
  sch.setRef(rec.getReference());
  if(rec.getValidFrom() != null){
   sch.setValidFrom(rec.getValidFrom());
  }
  if(rec.getVatReg() != null){
   VatRegistrationRec vatReg = this.buildVatRegistrationRecPvt(rec.getVatReg());
   sch.setVatReg(vatReg);
  }
  if(rec.getValidTo() != null){
   sch.setValidTo(rec.getValidTo());
  }
  
  if(rec.getVatScheme() != null){
   VatSchemeRec vatScheme = this.buildVatSchemeRec(rec.getVatScheme());
   sch.setVatScheme(vatScheme);
  }
  LOGGER.log(INFO, "return VatRegScheme for vat reg {0}", sch.getVatReg());
  return sch;
 }

 private VatReturn buildVatReturn(VatReturnRec rec){
  VatReturn v = null;
  if(rec.getId() == null){
   v = new VatReturn();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   v.setCreatedBy(crUsr);
   v.setCreatedOn(rec.getCreatedOn());
   em.persist(v);
  } 
  v.setBox1Value(rec.getBox1Value());
  v.setBox2Value(rec.getBox2Value());
  v.setBox3Value(rec.getBox3Value());
  v.setBox4Value(rec.getBox4Value());
  v.setBox5Value(rec.getBox5Value());
  v.setBox6Value(rec.getBox6Value());
  v.setBox7Value(rec.getBox7Value());
  v.setBox8Value(rec.getBox8Value());
  if(rec.getChangedBy() != null){
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   v.setChangedBy(chUsr);
   v.setChangedOn(rec.getChangedOn());
  }
  if(rec.getPaymentDueDate() != null){
   v.setPaymentDueDate(rec.getPaymentDueDate());
  }
  v.setProvisionReturn(rec.isProvisionReturn());
  v.setReturnDate(rec.getReturnDate());
  v.setReturnRef(rec.getReturnRef());
  if(rec.getVatRegScheme() != null){
   VatRegScheme regSch = em.find(VatRegScheme.class, rec.getVatRegScheme().getId(), OPTIMISTIC);
   v.setVatRegScheme(regSch);
  }
  
  return v;
 }
 
 private VatReturnRec buildVatReturnRec(VatReturn rec){
  LOGGER.log(INFO, "buildVatReturnRec called with {0}", rec);
  VatReturnRec v = new VatReturnRec();
  v.setId(rec.getId()) ;   
  UserRec crUsr = this.usrDm.getUserRecPvt(rec.getCreatedBy());
  v.setCreatedBy(crUsr);
  v.setCreatedOn(rec.getCreatedOn());
  v.setBox1Value(rec.getBox1Value());
  v.setBox2Value(rec.getBox2Value());
  v.setBox3Value(rec.getBox3Value());
  v.setBox4Value(rec.getBox4Value());
  v.setBox5Value(rec.getBox5Value());
  v.setBox6Value(rec.getBox6Value());
  v.setBox7Value(rec.getBox7Value());
  v.setBox8Value(rec.getBox8Value());
  if(rec.getChangedBy() != null){
   UserRec chUsr = this.usrDm.getUserRecPvt(rec.getChangedBy());
   v.setChangedBy(chUsr);
   v.setChangedOn(rec.getChangedOn());
  }
  v.setProvisionReturn(rec.isProvisionReturn());
  v.setReturnDate(rec.getReturnDate());
  v.setPaymentDueDate(rec.getPaymentDueDate());
  v.setReturnRef(rec.getReturnRef());
  if(rec.getVatRegScheme() != null){
   VatRegSchemeRec regSch = this.buildVatRegSchemeRec(rec.getVatRegScheme());
   v.setVatRegScheme(regSch);
  }
  
  return v;
 }
 
 private VatScheme buildVatScheme(VatSchemeRec rec, String pg){
  VatScheme scheme;
  boolean changed = false;
  if(rec.getId() == null || rec.getId() == 0){
   scheme = new VatScheme();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   scheme.setCreatedBy(crUsr);
   scheme.setCreatedOn(rec.getCreatedOn());
   scheme.setAnnualAccounting(rec.isAnnualAccounting());
   scheme.setCashAccounting(rec.isCashAccounting());
   scheme.setDescription(rec.getDescription());
   scheme.setFlatRate(rec.isFlatRate());
   scheme.setName(rec.getName());
   scheme.setRef(rec.getRef());
   scheme.setPaymentFrequency(rec.getPaymentFrequency());
   em.persist(scheme);
   AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getCreatedBy(), scheme.getCreatedOn(), pg);
   aud.setNewValue(scheme.getName());
   aud.setUsrAction('I');
  }else{
   scheme = em.find(VatScheme.class, rec.getId(), OPTIMISTIC_FORCE_INCREMENT);
   User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
   if(scheme.isAnnualAccounting() != rec.isAnnualAccounting()){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_ANN_RET");
    aud.setNewValue(String.valueOf(rec.isAnnualAccounting()));
    aud.setOldValue(String.valueOf(scheme.isAnnualAccounting()));
    aud.setUsrAction('U');
    scheme.setAnnualAccounting(rec.isAnnualAccounting());
    changed = true;
   }
   
   if(!rec.getDescription().equalsIgnoreCase(scheme.getDescription())){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_DESCR");
    aud.setNewValue(rec.getDescription());
    aud.setOldValue(scheme.getDescription());
    aud.setUsrAction('U');
    scheme.setDescription(rec.getDescription());
    changed = true;
   }
   
   if(rec.isCashAccounting() != scheme.isCashAccounting()){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_CSH_ACNT");
    aud.setNewValue(String.valueOf(rec.isCashAccounting()));
    aud.setOldValue(String.valueOf(scheme.isCashAccounting()));
    aud.setUsrAction('U');
    scheme.setCashAccounting(rec.isCashAccounting());
    changed = true;
   }
   
   if(rec.isFlatRate() != scheme.isFlatRate()){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_FL_RATE");
    aud.setNewValue(String.valueOf(rec.isFlatRate()));
    aud.setOldValue(String.valueOf(scheme.isFlatRate()));
    aud.setUsrAction('U');
    scheme.setFlatRate(rec.isFlatRate());
    changed = true;
   }
   
   if(!rec.getName().equalsIgnoreCase(scheme.getName())){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_NAME");
    aud.setNewValue(rec.getName());
    aud.setOldValue(scheme.getName());
    aud.setUsrAction('U');
    scheme.setName(rec.getName());
    changed = true;
   }
   if(rec.getPaymentFrequency() != scheme.getPaymentFrequency()){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_RET_FR");
    aud.setNewValue(String.valueOf(rec.getPaymentFrequency()));
    aud.setOldValue(String.valueOf(scheme.getPaymentFrequency()));
    aud.setUsrAction('U');
    scheme.setPaymentFrequency(rec.getPaymentFrequency());
    changed = true;
   }
   
   if(!rec.getRef().equalsIgnoreCase(scheme.getRef())){
    AuditVatScheme aud = this.buildAuditVatScheme(scheme, scheme.getChangedBy(), scheme.getChangedOn(), pg);
    aud.setFieldName("VAT_SCH_REF");
    aud.setNewValue(rec.getRef());
    aud.setOldValue(scheme.getRef());
    aud.setUsrAction('U');
    scheme.setRef(rec.getRef());
    changed = true;
   }
  
   if(changed){
    scheme.setChangedBy(chUsr);
    scheme.setChangedOn(rec.getChangedOn());
   }
  }
  
  
  
  
  
  
  return scheme;
 }
 
 private VatSchemeRec buildVatSchemeRec(VatScheme scheme){
  VatSchemeRec rec = new VatSchemeRec();
  rec.setId(scheme.getId());
  rec.setAnnualAccounting(scheme.isAnnualAccounting());
  UserRec crUsr = usrDm.getUserRecPvt(scheme.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(scheme.getCreatedOn());
  LOGGER.log(INFO, "buildVatSchemeRec code {0} create on DB {1} create on rec {2}", 
          new Object[]{scheme.getRef(),scheme.getCreatedOn(),rec.getCreatedOn()});
  if(scheme.getChangedBy() != null){
   UserRec chUsr = usrDm.getUserRecPvt(scheme.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(scheme.getChangedOn());
  }
  
  rec.setDescription(scheme.getDescription());
  rec.setFlatRate(scheme.isFlatRate());
  rec.setCashAccounting(scheme.isCashAccounting());
  rec.setName(scheme.getName());
  rec.setPrCode(scheme.getPrCode());
  rec.setPaymentFrequency(scheme.getPaymentFrequency());
  rec.setRef(scheme.getRef());
  
  return rec;
 }
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public VatCodeRec addVatCode(VatCodeRec vatCd, String source) throws BacException {
  LOGGER.log(INFO, "VAT DM saveVatCode called with code {0} source {1}", new Object[]{vatCd.getCode(),source});
  
  trans.begin();
  VatCode code = this.buildVatCode(vatCd,source);
  
  vatCd.setId(code.getId());
  
  trans.commit();
  
  
  return vatCd;
 }
 
 public int copyVatCodeComp(CompanyBasicRec c1, CompanyBasicRec c2, 
        UserRec usrRec, String pg){
 LOGGER.log(INFO, "VatMgr.copyVatCodeComp called with Comp {0} and {1}", new Object[]{
  c1.getReference(),c2.getReference() });
 if(!trans.isActive()){
  trans.begin();
 }
 TypedQuery q2 = em.createNamedQuery("vatCompCodesForComp", VatCodeCompany.class);
 q2.setMaxResults(1);
 q2.setParameter("compId", c2.getId());
 List<VatCodeCompany> rs = q2.getResultList();
 if(!rs.isEmpty()){
  return CompanyManager.ATTR_IN_DEST_COMP;
 }
 TypedQuery q1 = em.createNamedQuery("vatCompCodesForComp", VatCodeCompany.class);
 
 q1.setParameter("compId", c1.getId());
 rs = q1.getResultList();
 if(rs.isEmpty()){
  return CompanyManager.ATTR_NOT_IN_SRC_COMP;
 }
 User usr = em.find(User.class, usrRec.getId());
 CompanyBasic comp2 = em.find(CompanyBasic.class, c2.getId());
 int numVatCodes = 0;
 for(VatCodeCompany v:rs){
  VatCodeCompany curr = new VatCodeCompany();
  
  if(v.getChargeGlAccount() != null){
   Query glQ = em.createNamedQuery("GlCompAcnt");
   glQ.setParameter("compId", c2.getId());
   glQ.setParameter("acntRef", v.getChargeGlAccount().getCoaAccount().getRef());
   try{
    FiGlAccountComp chGl = (FiGlAccountComp)glQ.getSingleResult();
    curr.setChargeGlAccount(chGl);
   }catch(NoResultException e){
    LOGGER.log(INFO, "GL Account {0} not found in comp {1}", new Object[]{
     v.getChargeGlAccount().getCoaAccount().getRef(), c2.getReference() });
   }
  }
  curr.setCompany(comp2);
  curr.setCreatedBy(usr);
  curr.setCreatedOn(new Date());
  curr.setIrrecoverRate(v.getIrrecoverRate());
  curr.setNoIrrecoverableLine(v.isNoIrrecoverableLine());
  curr.setChargeSingleGl(v.isChargeSingleGl());
  if(v.getProvnGlAccount() != null){
   Query glQ = em.createNamedQuery("GlCompAcnt");
   glQ.setParameter("compId", c2.getId());
   glQ.setParameter("acntRef", v.getProvnGlAccount().getCoaAccount().getRef());
   try{
    FiGlAccountComp chGl = (FiGlAccountComp)glQ.getSingleResult();
    curr.setProvnGlAccount(chGl);
   }catch(NoResultException e){
    LOGGER.log(INFO, "GL Account {0} not found in comp {1}", new Object[]{
     v.getProvnGlAccount().getCoaAccount().getRef(), c2.getReference() });
   }
  }
  curr.setVatCode(v.getVatCode());
  
  if(v.getVatGlAccount() != null){
   Query glQ = em.createNamedQuery("GlCompAcnt");
   glQ.setParameter("compId", c2.getId());
   glQ.setParameter("acntRef", v.getVatGlAccount().getCoaAccount().getRef());
   try{
    FiGlAccountComp chGl = (FiGlAccountComp)glQ.getSingleResult();
    curr.setVatGlAccount(chGl);
   }catch(NoResultException e){
    LOGGER.log(INFO, "GL Account {0} not found in comp {1}", new Object[]{
     v.getVatGlAccount().getCoaAccount().getRef(), c2.getReference() });
   }
  }
  em.persist(curr);
  AuditVatCodeComp aud = this.buildAuditVatCodeComp(curr, 'I', usr, pg);
  aud.setNewValue(curr.getVatCode().getCode());
  numVatCodes++;
 }
 if(numVatCodes == 0){
  trans.rollback();
  return CompanyManager.ATTR_NOT_CREATED;
 }else{
  trans.commit();
  return CompanyManager.ATTR_CREATED;
 }
}
 
public CompanyBasicRec getCompVatRegRec(CompanyBasicRec compRec){
 LOGGER.log(INFO, "vatDM getCompVatRegRec called with comp id {0}", compRec.getId());
 CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId(), OPTIMISTIC);
 VatRegistration vatReg = comp.getVatRegCurrent();
 LOGGER.log(INFO, "Comp current vat reg {0}", vatReg);
 
 VatRegistrationRec vatRegRec = this.buildVatRegistrationRec(vatReg);
 compRec.setVatRegDetails(vatRegRec);
 return compRec;
}
 public List<VatCodeRec> getAllVatCodes() throws BacException {
  LOGGER.log(INFO, "VatDM.allVatCodes called");
  List<VatCodeRec> codeList = new ArrayList<>();
  TypedQuery q = em.createNamedQuery("allVatCodes",VatCode.class);
  List<VatCode> resultLst = q.getResultList();
  ListIterator<VatCode> li = resultLst.listIterator();
  while(li.hasNext()){
   VatCode vatCd = li.next();
   VatCodeRec vatRec = this.buildVatCodeRec(vatCd);
   codeList.add(vatRec);
   
  }
  LOGGER.log(INFO,"VAT codes returned {0}",codeList);
  
  return codeList;
 }
 
 
/**
 *  Saves new VAT posting settings for company
 * @param vatCode - VAT code to be updated
 * @param vatCodeComps
 * @param createdUser
 * @param source - page on which the change was made
 * @return
 * @throws BacException 
 */
public VatCodeRec addVatPostingForCompanies(VatCodeRec vatCode, List<VatCodeCompanyRec> vatCodeComps,
        UserRec createdUser, String source) throws BacException {
 LOGGER.log(INFO, "VAT DM addVatPostingForCompanies called for VAT code {0}", vatCode.getCode());
 if(vatCode == null){
  throw new BacException("DB:No VAT Code ","VatCode:01");
 }
 VatCode vatCodeDb = em.find(VatCode.class, vatCode.getId(), OPTIMISTIC);
 
 ListIterator<VatCodeCompanyRec> li = vatCodeComps.listIterator();
 while(li.hasNext()){
  VatCodeCompanyRec compRec = li.next();
  LOGGER.log(INFO, "About to build db rec for company {0} created by {1} created on {2}", 
    new Object[]{compRec.getCompany().getReference(),compRec.getCreatedBy().getId(),compRec.getCreatedOn()});
  VatCodeCompany vatComp = this.buildVatCodeComp(compRec,source);
  vatComp.setVatCode(vatCodeDb);
  List<VatCodeCompany> vatCompsDb = vatCodeDb.getVatCodeComps();
  if(vatCompsDb == null){
   vatCompsDb = new ArrayList<>(); 
  }
  vatCompsDb.add(vatComp);
  vatCodeDb.setVatCodeComps(vatCompsDb);
  compRec.setId(vatComp.getId());
  LOGGER.log(INFO,"vatCodeDb.getId(): {0}",vatCodeDb.getId());
  if(vatComp.getId() != null ){
   AuditVatCodeComp aud = new AuditVatCodeComp();
   aud.setAuditDate(vatComp.getCreatedOn());
   aud.setNewValue(vatCode.getCode());
   aud.setSource(source);
   aud.setUsrAction('I');
   aud.setVatCodeComp(vatComp);
  }
  compRec.setId(vatComp.getId());
  li.set(compRec);
 
 }
 
 
 
 return vatCode;
}

public VatCodeCompanyRec vatCodeCompanyUpdate(VatCodeCompanyRec vatCompRec, String pg){
 LOGGER.log(INFO, "VATdm.vatCodeCompanyUpdate caled with {0}", vatCompRec);
 boolean newVatComp = vatCompRec.getId() == null;
 if(!trans.isActive()){
  trans.begin();
 }
 VatCodeCompany vatComp = this.buildVatCodeComp(vatCompRec,pg);
 if(newVatComp){
  vatCompRec.setId(vatComp.getId());
 }
 trans.commit();
 return vatCompRec;
 
}
/**
 * Save VAT scheme to the database and return the generated id in the scheme record
 * @param scheme
 * @param user
 * @param source
 * @return
 * @throws BacException 
 */
 public VatSchemeRec addVatScheme(VatSchemeRec scheme, UserRec user, String source) throws BacException {
  LOGGER.log(INFO, "VAT DM addVatScheme called with scheme {0} user {1} source{2}", 
          new Object[]{scheme,user,source});
  if(!trans.isActive()){
   trans.begin();
  }
  VatScheme vatSc = buildVatScheme(scheme,source);
  
  trans.commit();
  scheme.setId(vatSc.getId());
  LOGGER.log(INFO, "End VAT DM add vatScheme scheme id {0}", scheme.getId());
  return scheme;
 }

 /**
  * Saves the new industry record for Flat Rate VAT processing and logs creation
  * @param rate
  * @param user
  * @param source
  * @return
  * @throws BacException 
  */
 public VatFlatRateIndRateRec vatIndustryFlatRateSave(VatFlatRateIndRateRec rate, UserRec user, 
         String source) throws BacException {
  LOGGER.log(INFO, "VarDM.addVatIndustryFlatRate called with rate {0} user {1} page source {2}", 
          new Object[]{rate,user,source});
  if(!trans.isActive()){
   trans.begin();
  }
  VatIndustryFlatRate rateDb = this.buildVatFlatRateIndRate(rate,user,source);
  trans.commit();
  rate.setId(rateDb.getId());
  LOGGER.log(INFO, "End create Ind rate returns {0}", rate);
  return rate;
 }
 
 public boolean vatRegistrationDelete(VatRegistrationRec vatRegRec, CompanyBasicRec compRec,
   UserRec usrRec,   String pg){
  LOGGER.log(INFO, "vatDM.vatRegistrationDelete called with reg {0} ", vatRegRec.getVatNumber());
  if(!trans.isActive()){
   trans.begin();
  }
  boolean rc;
  User usr ;
  VatRegistration vatReg = em.find(VatRegistration.class, vatRegRec.getId());
  if(vatReg == null){
   LOGGER.log(INFO, "Could not find vatReg");
   return false;
  }
  
  usr = em.find(User.class, usrRec.getId());
  CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId());
  LOGGER.log(INFO, "Current comp VAT reg {0}", comp.getVatRegCurrent());
  
  if(comp.getVatRegCurrent() != null && 
    Objects.equals(comp.getVatRegCurrent().getId(), vatReg.getId())){
   // this Vat reg is the one allocated to the company so remove company VAT registration
   LOGGER.log(INFO, "Remove VAT reg from comp");
   
   AuditCompany aud = this.buildAuditCompany(comp, 'U', usr, pg);
   aud.setFieldName("COMP_VAT_NUM");
   aud.setOldValue(compRec.getVatNumber());
   aud.setNewValue(" ");
   comp.setVatNumber(null);
   
   aud = this.buildAuditCompany(comp, 'U', usr, pg);
   aud.setFieldName("COMP_VAT_REG");
   aud.setOldValue(String.valueOf(compRec.isVatReg()));
   aud.setNewValue(String.valueOf(false));
   comp.setVatReg(false);
   
   aud = this.buildAuditCompany(comp, 'U', usr, pg);
   aud.setFieldName("COMP_VAT_CURR_REG");
   aud.setOldValue(compRec.getVatRegDetails().getVatNumber());
   aud.setNewValue(" ");
   comp.setVatRegCurrent(null);
   LOGGER.log(INFO, "Current VAT reg for comp is now {0}", comp.getVatRegCurrent());
       
  }
   Query q = em.createNamedQuery("AuditVatRegDel");
   q.setParameter("vatRegId", vatReg.getId());
   int numDel = q.executeUpdate();
   LOGGER.log(INFO, "Number of audit records deleted {0}", numDel);
   AuditDelete audDel = new AuditDelete();
   audDel.setAuditDate(new Date());
   audDel.setCreatedBy(usr);
   audDel.setFieldName("DEL_NAME");
   audDel.setName("VatRegistration");
   audDel.setNamePath("com.rationem.entity.salesTax.vat.VatRegistration");
   em.remove(vatReg);
   
   rc = true;
   trans.commit();
  return rc; 
 }
/**
 * SAves a new VAT registration to database
 * @param compRec Company the VAT registration is for
 * @param vatReg
 * @param usr
 * @param source
 * @return 
 */
 public VatRegistrationRec vatRegistrationUpdate(CompanyBasicRec compRec, VatRegistrationRec vatReg, 
         UserRec usr, String source) {
  LOGGER.log(INFO, "VATDM.addVatRegistration called with company {0} vatReg {1}", 
          new Object[]{compRec.getId(),vatReg});
  if(!trans.isActive()){
   trans.begin();
  }
  LOGGER.log(INFO, "vatReg createdBy {0} comp vatReg {1}", new Object[]{vatReg.getCreatedBy(),compRec.getVatRegDetails().getCreatedBy()} );
  VatRegistration reg = buildVatRegistration(vatReg, usr,source);
  List<VatRegScheme> regSchemes = reg.getVatRegSchemes();
  if(regSchemes == null){
   regSchemes = new ArrayList<>();
  }
  List<VatRegSchemeRec> regRecSchemes = vatReg.getRegSchemes();
  if(regRecSchemes != null){
   ListIterator<VatRegSchemeRec> schemeLi = regRecSchemes.listIterator();
   while(schemeLi.hasNext()){
    VatRegSchemeRec schRec = schemeLi.next();
    VatRegScheme sch = buildVatRegScheme(schRec, usr, source);
   
    if(schRec.getId() == null){
     regSchemes.add(sch);
     sch.setVatReg(reg);
     schRec.setId(sch.getId());
    }
    schemeLi.set(schRec);
   }
  }
  reg.setVatRegSchemes(regSchemes);
  vatReg.setRegSchemes(regRecSchemes);
  vatReg.setId(reg.getId());
  compRec.setVatRegDetails(vatReg);
  CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId());
  User audUsr = em.find(User.class, usr.getId());
  AuditCompany aud = this.buildAuditCompany(comp,'U', audUsr, source);
  aud.setFieldName("COMP_VAT_NUM");
  LOGGER.log(INFO, "comp.getVatNumber() {0} vatReg.getVatNumber() {1} ", new Object[]{
   comp.getVatNumber(),vatReg.getVatNumber()  });
  if(comp.getVatNumber() != null){
   aud.setOldValue(comp.getVatNumber());
  }
  
  aud.setNewValue(vatReg.getVatNumber());
  comp.setVatNumber(vatReg.getVatNumber());
  
  AuditCompany aud1 = this.buildAuditCompany(comp, 'U', audUsr, source);
  LOGGER.log(INFO, "comp.isVatReg() {0} new isVatReg {1} ", new Object[]{
   String.valueOf(comp.isVatReg()),String.valueOf(true)
  });
  aud1.setFieldName("COMP_VAT_REG");
  aud1.setOldValue(String.valueOf(comp.isVatReg()));
  aud1.setNewValue(String.valueOf(true));
  comp.setVatReg(true);
  LOGGER.log(INFO, "addVatRegistration returns VAT registration {0}", vatReg.getId());
  
  
  
  
  trans.commit();
  
  return vatReg;
 }
public VatRegistrationRec vatRegistrationSetActive(CompanyBasicRec compRec,VatRegistrationRec vatRegRec, 
         UserRec usr, String source){
 if(!trans.isActive()){
  trans.begin();
 }
 User chUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
 CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId(), OPTIMISTIC);
 VatRegistration vatReg = em.find(VatRegistration.class, vatRegRec.getId(), OPTIMISTIC);
 boolean vatRegChanged = false;
 boolean compChanged = false;
 if(!vatReg.isActiveReg()){
  AuditVatRegistration aud = this.buildAuditVatRegistration(vatReg,'U', chUsr, source);
  aud.setFieldName("VAT_REG_ACTIVE");
  aud.setNewValue(String.valueOf(true));
  aud.setOldValue(String.valueOf(false));
  aud.setUsrAction('U');
  
  vatReg.setActiveReg(true);
  vatRegChanged = true;
 }
 
 LOGGER.log(INFO, "vatRegRec.getComp() rec comp {0} vatReg comp {1}", 
         new Object[]{vatRegRec.getComp(),vatReg.getComp()});
 if((vatRegRec.getComp() == null && vatReg.getComp() != null )||
    (vatRegRec.getComp() != null && vatReg.getComp() == null ) ||
    (vatRegRec.getComp() != null && !Objects.equals(vatRegRec.getComp().getId(), vatReg.getComp().getId()) )  ){
  AuditVatRegistration aud = this.buildAuditVatRegistration(vatReg, 'U',chUsr, source);
  aud.setFieldName("VAT_REG_COMP");
  aud.setNewValue(vatRegRec.getComp().getReference());
  aud.setOldValue(vatReg.getComp().getNumber());
  aud.setUsrAction('U');
  vatReg.setComp(comp);
  vatRegChanged = true;
 }
 
 if((compRec.getVatRegDetails() == null && comp.getVatRegCurrent()  != null) ||
    (compRec.getVatRegDetails() != null && comp.getVatRegCurrent()  == null) ||
    (!Objects.equals(compRec.getVatRegDetails().getId(), comp.getVatRegCurrent().getId()))    ){
  AuditCompany aud = this.buildAuditCompany(comp, 'U', chUsr, source);
  aud.setFieldName("COMP_VAT_CURR_REG");
  aud.setNewValue(compRec.getVatRegDetails().getVatNumber());
  aud.setNewValue(comp.getVatRegCurrent().getVatNumber());
  aud.setUsrAction('U');
  comp.setVatRegCurrent(vatReg);
  compChanged = true;
 }
 
 if(vatRegChanged){
  vatReg.setChangedBy(chUsr);
  vatReg.setChangedOn(new Date());
 }
 
 if(compChanged){
  comp.setChangedBy(chUsr);
  comp.setChangedDate(new Date());
 }
 
 trans.commit();
 return vatRegRec;
} 
public VatRegistration vatRegistrationUpdatePvt(CompanyBasic comp, VatRegistrationRec vatReg, 
         UserRec usr, String source) {
  LOGGER.log(INFO, "VATDM.vatRegistrationUpdatePvt called with company {0} vatReg {1} vat reg created by {2}", 
          new Object[]{comp.getId(),vatReg, vatReg.getCreatedBy().getId()});
 
  if(!trans.isActive()){
   trans.begin();
  }
  //em.joinTransaction();
  VatRegistration reg = buildVatRegistration(vatReg, usr,source);
  
  
  trans.commit();
  
  return reg;
 }

 
 /**
  * read database for all VAT schemes
  * @return
  * @throws BacException 
  */
 public List<VatSchemeRec> getVatSchemesAll() throws BacException {
  LOGGER.log(INFO, "VatDM.getVatSchemesAll ");
  ArrayList<VatSchemeRec> vatSchemeList = new ArrayList<>();
  Query q = em.createNamedQuery("allVatSchemes");
  List l = q.getResultList();
  ListIterator li = l.listIterator();
  while(li.hasNext()){
   VatScheme sch = (VatScheme)li.next();
   LOGGER.log(INFO, "DB vat scheme {0}", sch);
   VatSchemeRec rec = this.buildVatSchemeRec(sch);
   LOGGER.log(INFO, "rec vat scheme {0}", rec);
   vatSchemeList.add(rec);
  }
          
  return vatSchemeList;
 }

 public List<VatFlatRateIndRateRec> getVatIndustries() throws BacException {
  List<VatFlatRateIndRateRec> inds = new ArrayList<>();
  Query q = em.createNamedQuery("allVatIndustries");
  List l = q.getResultList();
  ListIterator li = l.listIterator();
  while(li.hasNext()){
   VatIndustryFlatRate ind = (VatIndustryFlatRate)li.next();
   VatFlatRateIndRateRec rec = this.buildVatFlatRateIndRateRec(ind);
   inds.add(rec);
  }
  LOGGER.log(INFO,"DB layer returns inds {0}",inds);
  return inds;
 }

 /*public VatRegSchemeRec VatRegistrationAddScheme(VatRegSchemeRec vatRegSch, VatSchemeRec scheme, UserRec usr, String source) 
         throws BacException {
  LOGGER.log(INFO, "VatDM.VatRegistrationAddSchemes called with reg {0} user {1} source {2}", 
          new Object[]{vatRegSch,usr,source});
  
  VatRegScheme regScheme = this.buildVatRegScheme(vatRegSch);
  List<VatScheme> vatSchemeList = regScheme.getVatSchemes();
  if(vatSchemeList == null){
   vatSchemeList = new ArrayList<VatScheme>();
  }
  List<VatSchemeRec> vatSchemeRecsList = vatRegSch.getVatSchemes();
  ListIterator<VatSchemeRec> schIt = vatSchemeRecsList.listIterator();
  while(schIt.hasNext()){
   VatSchemeRec schemeRec = schIt.next();
   VatScheme sch = this.buildVatScheme(schemeRec);
   List<VatRegScheme> vatRegs = sch.getVatRegSchemes();
   if(vatRegs == null){
    vatRegs = new ArrayList<VatRegScheme>();
   }
   vatRegs.add(regScheme);
   sch.setVatRegSchemes(vatRegs);
   vatSchemeList.add(sch);
  
   if(scheme.isFlatRate()){
    // if flat rate get current flat rate
    LOGGER.log(INFO, "Need to get flat rate");
    
    
   }
  }
  regScheme.
  vatRegSch.setId(regScheme.getId());
  vatRegSch.setChanges(regScheme.getChanges());
  
  AuditVatRegScheme aud = new AuditVatRegScheme();
  aud.setAuditDate(new Date());
  User audUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  aud.setCreatedBy(audUsr);
  aud.setNewValue(regScheme.getRef());
  aud.setSource(source);
  aud.setUsrAction('I');
  aud.setVatRegScheme(regScheme);
  em.persist(aud);
  
  return vatRegSch;
 }
*/
 public List<VatCodeCompanyRec> getVatCompanyCodesAll() throws BacException {
  LOGGER.log(INFO, "VatDM.getVatCompanyCodesAll");
  List<VatCodeCompanyRec> list = new ArrayList<>();
  Query q = em.createNamedQuery("vatCompCodesAll");
  List l = q.getResultList();
  ListIterator li = l.listIterator();
  LOGGER.log(INFO, "Company VAT returned {0} records", l.size());
  while(li.hasNext()){
   LOGGER.log(INFO, "About to build vat rec"); 
   VatCodeCompany vatCode = (VatCodeCompany)li.next();
   VatCodeCompanyRec vatCodeRec = this.buildVatCodeCompRec(vatCode);
   LOGGER.log(INFO, "VAT rec {0}",vatCodeRec);
   list.add(vatCodeRec);
  }
  LOGGER.log(INFO,"getVatCompanyCodesAll returns {0} records",list.size());
  return list;
 }

 public List<VatRegistrationRec> getVatRegistrationsForCompany(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "VatDM getVatRegistrationsForCompany called with comp {0}", comp);
  if(!trans.isActive())
  {
   trans.begin();
  }
  
  List<VatRegistrationRec> vatRegList = new ArrayList<>();
  CompanyBasic company = em.find(CompanyBasic.class, comp.getId(), OPTIMISTIC);
  Query q = em.createNamedQuery("vatRegForComp");
  q.setParameter("company", company);
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   VatRegistration vatReg = (VatRegistration)li.next();
   VatRegistrationRec vatRegRec = buildVatRegistrationRec(vatReg);
   vatRegRec.setComp(comp);
   LOGGER.log(INFO, "Comp for vat reg {0}", vatRegRec.getComp());
   List<VatRegSchemeRec> vatRegSchemes = vatRegRec.getRegSchemes();
  LOGGER.log(INFO, "VatDM vatReg.schemes {0}",vatReg.getVatRegSchemes());
  if(vatRegSchemes == null || vatRegSchemes.isEmpty()){
   vatRegSchemes = new ArrayList<>();
   List<VatRegScheme> regSchemes = vatReg.getVatRegSchemes();
   if(regSchemes != null){
    ListIterator<VatRegScheme> regSchemesLi = regSchemes.listIterator();
    while(regSchemesLi.hasNext()){
     VatRegScheme regScheme = regSchemesLi.next();
     VatRegSchemeRec regSchemeRec = buildVatRegSchemeRec(regScheme);
     vatRegSchemes.add(regSchemeRec);
    }
    LOGGER.log(INFO, "VatDM vatReg.schemeRecs {0}",vatRegSchemes);
    vatRegRec.setRegSchemes(vatRegSchemes);
   }
  }
  LOGGER.log(INFO, "VatDM vatReg.schemeRecs {0}",vatRegSchemes);
  vatRegRec.setRegSchemes(vatRegSchemes);
  vatRegList.add(vatRegRec);
  }
  LOGGER.log(INFO, "VAT reg list to return {0}", vatRegList);
  return vatRegList;
 }
 public VatRegistrationRec getVatRegistrationForCompany(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "VatDM getVatRegistrationForCompany called with comp {0}", comp);
  CompanyBasic company = em.find(CompanyBasic.class, comp.getId(), OPTIMISTIC);
  Query q = em.createNamedQuery("vatRegForComp");
  q.setParameter("company", company);
  VatRegistration vatReg = (VatRegistration)q.getSingleResult();
  VatRegistrationRec vatRegRec = buildVatRegistrationRec(vatReg);
  vatRegRec.setComp(comp);
  List<VatRegSchemeRec> vatRegSchemes = vatRegRec.getRegSchemes();
  LOGGER.log(INFO, "VatDM vatReg.schemes {0}",vatReg.getVatRegSchemes());
  if(vatRegSchemes == null){
   vatRegSchemes = new ArrayList<>();
   List<VatRegScheme> regSchemes = vatReg.getVatRegSchemes();
   if(regSchemes != null){
    ListIterator<VatRegScheme> regSchemesLi = regSchemes.listIterator();
    while(regSchemesLi.hasNext()){
     VatRegScheme regScheme = regSchemesLi.next();
     VatRegSchemeRec regSchemeRec = buildVatRegSchemeRec(regScheme);
     vatRegSchemes.add(regSchemeRec);
    }
    vatRegRec.setRegSchemes(vatRegSchemes);
   }
  }
 return vatRegRec;
   
 }

  
 /**
  * Returns schemes associated with a particular VAT registration
  * @param vatRegRec
  * @return
  * @throws BacException 
  */ 
 /*public List<VatRegSchemeRec> getVatRegSchemesForVatRegistration(VatRegistrationRec vatRegRec) 
         throws BacException {
  LOGGER.log(INFO, "VatDM getVatRegSchemesForVatRegistration called for vatReg {0}", vatRegRec);
  if(vatRegRec == null){
   throw new BacException("vatReg is required");
  }
  
  if(vatRegRec.getId() == null || vatRegRec.getId() == 0){
   throw new BacException("vatReg must have a valid id");
  }
  List<VatRegSchemeRec> vatRegSchemeList = new ArrayList<VatRegSchemeRec>();
  VatRegistration vatReg = em.find(VatRegistration.class, vatRegRec.getId(), OPTIMISTIC);
  List<VatRegScheme> schemes = vatReg.getVatRegSchemes();
  if(schemes == null){
   return null;
  }
  ListIterator<VatRegScheme> li = schemes.listIterator();
  while(li.hasNext()){
   VatRegScheme regScheme = li.next();
   List<VatScheme> vatSchemes = regScheme.getVatSchemes();
   if(vatSchemes == null){
    throw new BacException("No VAT schemes found");
   }
   List<VatSchemeRec> vatSchemesRec = new ArrayList<VatSchemeRec>();
   ListIterator<VatScheme> vatSchemesLi = vatSchemes.listIterator();
   while(vatSchemesLi.hasNext()){
    VatScheme vatScheme = vatSchemesLi.next();
    VatSchemeRec vatSchemeRec = buildVatSchemeRec(vatScheme);
    vatSchemesRec.add(vatSchemeRec);
   }
   VatRegSchemeRec regSchemeRec = buildVatRegSchemeRec(regScheme);
   regSchemeRec.setVatSchemes(vatSchemesRec);
   LOGGER.log(INFO, "vatSchemesRec.size() {0}", vatSchemesRec.size());
   if(vatSchemesRec.size() == 1){
   // regSchemeRec.setVatScheme(vatSchemesRec.get(0));
   }
   
   vatRegSchemeList.add(regSchemeRec);
  }
  
  return vatRegSchemeList;
 }
*/
 public List<VatCodeCompanyRec> getVatCompCodesForVatCode(VatCodeRec vatCode){
  List<VatCodeCompanyRec> retList = new ArrayList<>();
  VatCode vatCd = em.find(VatCode.class, vatCode.getId(), OPTIMISTIC);
  List<VatCodeCompany> compCodes = vatCd.getVatCodeComps();
  if(compCodes == null){
   throw new BacException("no company settings for vat code: "+vatCode.getCode());
  }
  ListIterator<VatCodeCompany> li = compCodes.listIterator();
  while(li.hasNext()){
   VatCodeCompanyRec compVatCode = this.buildVatCodeCompRec(li.next());
   retList.add(compVatCode);
  }
  LOGGER.log(INFO, "getVatCompCodesForVatCode returns {0}", retList);
  
  return retList;
 }
 public VatCodeCompanyRec getVatCodeCompRec(VatCodeCompany v){
  VatCodeCompanyRec vc = this.buildVatCodeCompRec(v);
  return vc;
 }
 
 public VatCodeRec getVatCodeForCompVatCode(VatCodeCompanyRec compVatRec){
  LOGGER.log(INFO, "getVatCodeForCompVatCode called");
  VatCodeCompany vatCodeComp = em.find(VatCodeCompany.class, compVatRec.getId());
  VatCode vc = vatCodeComp.getVatCode();
  if(vc == null){
   LOGGER.log(INFO, "Comp Vat code id {0} missing vat cade ", compVatRec.getId());
   return null;
  }else{
   VatCodeRec vatCodeRec = buildVatCodeRec(vc);
   LOGGER.log(INFO, "vatCodeRec returned {0} ", vatCodeRec.getCode());
   return vatCodeRec;
  }
 }
 
 public VatCodeRec getVatCompRecsForVatCode(VatCodeRec vatCode) throws BacException {
  LOGGER.log(INFO,"VatDM getVatCompRecsForVatCode called with code {0} ",vatCode.getCode());
  VatCode cd = em.find(VatCode.class, vatCode.getId(), OPTIMISTIC);
  List<VatCodeCompany> vatCompLst = cd.getVatCodeComps();
  List<VatCodeCompanyRec> vatCompRecLst = new ArrayList<>();
  ListIterator<VatCodeCompany> vatCompLstLi = vatCompLst.listIterator();
  while(vatCompLstLi.hasNext()){
   VatCodeCompany compVat = vatCompLstLi.next();
   VatCodeCompanyRec compVatRec = this.buildVatCodeCompRec(compVat);
   vatCompRecLst.add(compVatRec);
  }
  LOGGER.log(INFO, "Number of comp vat Recs {0}", vatCompRecLst.size());
  VatCodeRec vatCodeRec = this.buildVatCodeRec(cd);
  vatCodeRec.setVatCodeCompanies(vatCompRecLst);
  return vatCodeRec;
 }

 public VatReturnRec addVatReturn(VatReturnRec vatReturn, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO,"VATDm.addVatReturn called with  vat return {0}", vatReturn);
  
  VatReturn ret = this.buildVatReturn(vatReturn);
  vatReturn.setId(ret.getId());
  AuditVatReturn aud = new AuditVatReturn();
  aud.setAuditDate(new Date());
  User audUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  aud.setCreatedBy(audUsr);
  aud.setNewValue(vatReturn.getReturnRef());
  aud.setSource(page);
  aud.setUsrAction('I');
  aud.setVatReturn(ret);
  em.persist(aud);
  return vatReturn;
 }

 
 public List<VatReturnRec> getVatReturnsForRegScheme(VatRegSchemeRec regScheme, Date returnDate) {
  LOGGER.log(INFO, "VatDM.getVatReturnsForRegScheme called with scheme id: {0}", regScheme.getId());
  VatRegScheme schDB = em.find(VatRegScheme.class, regScheme.getId(), OPTIMISTIC);
  Query q = em.createNamedQuery("vatReturnsAfterDate");
  q.setParameter("regSchem", schDB);
  q.setParameter("retDate", returnDate);
  List rs = q.getResultList();
  LOGGER.log(INFO, "DB Query found vat Returns {0}", rs);
  if(rs == null){
   return null;
  }
  List<VatReturnRec> vatReturnList = new ArrayList<>();
  ListIterator vLi = rs.listIterator();
  while(vLi.hasNext()){
   VatReturn vatReturn = (VatReturn)vLi.next();
   LOGGER.log(INFO, "vatreturn {0}", vatReturn.getId());
   VatReturnRec vatReturnRec = this.buildVatReturnRec(vatReturn);
   vatReturnList.add(vatReturnRec);
  }
  LOGGER.log(INFO, "VatDM.getVatReturnsForRegScheme returns regs {0}", vatReturnList);
  return vatReturnList;
 }

 public VatReturn addVatReturnLineArPvt(VatReturn vatReturn,VatRegScheme vatRegScheme, 
         ArAPAccountIF partnerAc, DocFi doc, 
         DocGlVatCodeSummary vatSumRec, 
         VatCodeCompany vatCodeComp,
         DocLineBase docLine, String vatTransTy
         ) 
         throws BacException {
  LOGGER.log(INFO, "addVatReturnLineArPvt called with ret {0} acnt {1} doc {2} line {3}", new Object[]{
   vatReturn,partnerAc,doc,vatSumRec });
  VatReturnLine retLine = new VatReturnLine();
  String partnerType = partnerAc.getClass().getCanonicalName();
  VatCode vatCode = null;
  boolean cashActing = vatRegScheme.getVatScheme().isCashAccounting();
  if(vatSumRec.getVatCode() != null){
   vatCode = vatCodeComp.getVatCode();
   
  }
  if(vatTransTy.equalsIgnoreCase("suppDomInv")){
   //Domestic sale invoices   
   retLine.setVatTransType(vatTransTy);
   
   
    retLine.setVatGlAccount(vatCodeComp.getVatGlAccount());
   
   
    retLine.setProvnGlAccount(vatCodeComp.getProvnGlAccount());
   
   double vatDue = vatReturn.getBox1Value();
   vatDue = vatDue + vatSumRec.getVatAmount();
   vatReturn.setBox1Value(vatDue);
   double netVat = vatReturn.getBox1Value() + vatReturn.getBox2Value();
   vatReturn.setBox3Value(netVat);
   double sales = vatReturn.getBox6Value();
   sales = sales + vatSumRec.getGoods();
   vatReturn.setBox6Value(sales);
   
  }else if(vatTransTy.equalsIgnoreCase("suppDomCrn")){
   //Domestic sale credit notes   
   retLine.setVatTransType(vatTransTy);
   
    retLine.setVatGlAccount(vatCodeComp.getVatGlAccount());
   
   
    retLine.setProvnGlAccount(vatCodeComp.getProvnGlAccount());
   
   double vatDue = vatReturn.getBox1Value();
   vatDue = vatDue - vatSumRec.getVatAmount();
   vatReturn.setBox1Value(vatDue);
   double netVat = vatReturn.getBox1Value() + vatReturn.getBox2Value();
   vatReturn.setBox3Value(netVat);
   double sales = vatReturn.getBox6Value();
   sales = sales - vatSumRec.getGoods();
   vatReturn.setBox6Value(sales);
  }
  
  retLine.setCreatedBy(doc.getCreatedBy());
  retLine.setCreatedOn(new Date());
  if(partnerType.endsWith("ArAccount")){
   ArAccount arAccount = (ArAccount)partnerAc;
   retLine.setArAccount(arAccount);
   PartnerBase partner = arAccount.getArAccountFor();
   retLine.setPartner(partner);
   
  }
  retLine.setArInvoice(doc.getDocInvoiceAr());
  if(vatSumRec.getGlAccount() != null){
   FiGlAccountComp costAc = em.find(FiGlAccountComp.class, vatSumRec.getGlAccount().getId(), OPTIMISTIC);
   retLine.setExpenseGlAccount(costAc);
  }
  
  retLine.setGoodsValue(vatSumRec.getGoods());
  retLine.setVatValue(vatSumRec.getVatAmount());
  //retLine.setIrrecoverableValue(vatSumRec.);
  if(vatCode != null){
   retLine.setVatCode(vatCode);
  }
  
  
   retLine.setVatIrrecoverableGlAccount(vatCodeComp.getChargeGlAccount());
  
  
  retLine.setFiDoc(doc);
  retLine.setFiDocLine((DocLineBase)docLine);

  retLine.setVatReturn(vatReturn);
  em.persist(retLine);
  
  //vatReturn
  
  
  return vatReturn;
}
 public VatReturn addVatReturnLinePvt(VatReturn vatReturn,VatRegScheme vatRegScheme, 
         ArAPAccountIF partnerAc, DocFi doc, 
         DocVatSummary vatSumRec, 
         DocLineBase docLine, String vatTransTy
         ) 
         throws BacException {
  LOGGER.log(INFO, "addVatReturnLinePvt called with ret {0} acnt {1} doc {2} line {3}", new Object[]{
   vatReturn,partnerAc,doc,vatSumRec });
  VatReturnLine retLine = new VatReturnLine();
  String partnerType = partnerAc.getClass().getCanonicalName();
  VatCode vatCode = null;
  boolean cashActing = vatRegScheme.getVatScheme().isCashAccounting();
  if(vatSumRec.getVatCode() != null){
   vatCode = em.find(VatCode.class, vatSumRec.getVatCode().getId(), OPTIMISTIC);
   
  }
  if(vatTransTy.equalsIgnoreCase("suppDomInv")){
   //Domestic sale invoices   
   retLine.setVatTransType(vatTransTy);
   if(vatSumRec.getRateAccount() != null && !cashActing){
    FiGlAccountComp vatAc = em.find(FiGlAccountComp.class, vatSumRec.getRateAccount().getId(), OPTIMISTIC);
    retLine.setVatGlAccount(vatAc);
   }
   if(vatSumRec.getProvisionAccount() != null && cashActing){
    FiGlAccountComp provnAc = em.find(FiGlAccountComp.class, vatSumRec.getProvisionAccount().getId(), OPTIMISTIC);
    retLine.setProvnGlAccount(provnAc);
   }
   double vatDue = vatReturn.getBox1Value();
   vatDue = vatDue + vatSumRec.getVat();
   vatReturn.setBox1Value(vatDue);
   double netVat = vatReturn.getBox1Value() + vatReturn.getBox2Value();
   vatReturn.setBox3Value(netVat);
   double sales = vatReturn.getBox6Value();
   sales = sales + vatSumRec.getGoods();
   vatReturn.setBox6Value(sales);
   
  }else if(vatTransTy.equalsIgnoreCase("suppDomCrn")){
   //Domestic sale credit notes   
   retLine.setVatTransType(vatTransTy);
   if(vatSumRec.getRateAccount() != null && !cashActing){
    FiGlAccountComp vatAc = em.find(FiGlAccountComp.class, vatSumRec.getRateAccount().getId(), OPTIMISTIC);
    retLine.setVatGlAccount(vatAc);
   }
   if(vatSumRec.getProvisionAccount() != null && cashActing){
    FiGlAccountComp provnAc = em.find(FiGlAccountComp.class, vatSumRec.getProvisionAccount().getId(), OPTIMISTIC);
    retLine.setProvnGlAccount(provnAc);
   }
   double vatDue = vatReturn.getBox1Value();
   vatDue = vatDue - vatSumRec.getVat();
   vatReturn.setBox1Value(vatDue);
   double netVat = vatReturn.getBox1Value() + vatReturn.getBox2Value();
   vatReturn.setBox3Value(netVat);
   double sales = vatReturn.getBox6Value();
   sales = sales - vatSumRec.getGoods();
   vatReturn.setBox6Value(sales);
  }
  
  retLine.setCreatedBy(doc.getCreatedBy());
  retLine.setCreatedOn(new Date());
  if(partnerType.endsWith("ArAccount")){
   ArAccount arAccount = (ArAccount)partnerAc;
   retLine.setArAccount(arAccount);
   PartnerBase partner = arAccount.getArAccountFor();
   retLine.setPartner(partner);
   
  }
  retLine.setArInvoice(doc.getDocInvoiceAr());
  if(vatSumRec.getGlAccount() != null){
   FiGlAccountComp costAc = em.find(FiGlAccountComp.class, vatSumRec.getGlAccount().getId(), OPTIMISTIC);
   retLine.setExpenseGlAccount(costAc);
  }
  
  retLine.setGoodsValue(vatSumRec.getGoods());
  retLine.setVatValue(vatSumRec.getVat());
  retLine.setIrrecoverableValue(vatSumRec.getIrrecoverableVat());
  if(vatCode != null){
   retLine.setVatCode(vatCode);
  }
  
  if(vatSumRec.getIrrecoverableAccount() != null){
   FiGlAccountComp irRecovAc = em.find(FiGlAccountComp.class, vatSumRec.getIrrecoverableAccount().getId(), OPTIMISTIC);
   retLine.setVatIrrecoverableGlAccount(irRecovAc);
  }
  
  retLine.setFiDoc(doc);
  retLine.setFiDocLine((DocLineBase)docLine);

  retLine.setVatReturn(vatReturn);
  em.persist(retLine);
  
  //vatReturn
  
  
  return vatReturn;
 }

 public List<VatCodeRec> getVatCodesInput(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "VATDM.getVatCodesInput called");
  Query q = em.createNamedQuery("vatCodesInput");
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  List<VatCodeRec> vatCodeRecList = new ArrayList<>();
  while(li.hasNext()){
   VatCode v = (VatCode)li.next();
   List<VatCodeCompany> vatComps = v.getVatCodeComps();
   ListIterator<VatCodeCompany> vatCompsLi = vatComps.listIterator();
   while(vatCompsLi.hasNext()){
    VatCodeCompany vatComp = vatCompsLi.next();
    if(Objects.equals(vatComp.getId(), comp.getId())){
     VatCodeRec vatCodeRec = this.buildVatCodeRec(v);
     vatCodeRecList.add(vatCodeRec);
    }
   }
  }
  return vatCodeRecList;
 }

 public List<VatCodeRec> getVatCodesOutput(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "VATDM.getVatCodesOutput called");
  Query q = em.createNamedQuery("vatCodesOutput");
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  List<VatCodeRec> vatCodeRecList = new ArrayList<>();
  while(li.hasNext()){
   VatCode v = (VatCode)li.next();
   List<VatCodeCompany> vatComps = v.getVatCodeComps();
   ListIterator<VatCodeCompany> vatCompsLi = vatComps.listIterator();
   while(vatCompsLi.hasNext()){
    VatCodeCompany vatComp = vatCompsLi.next();
    if(Objects.equals(vatComp.getId(), comp.getId())){
     VatCodeRec vatCodeRec = this.buildVatCodeRec(v);
     vatCodeRecList.add(vatCodeRec);
    }
   }
  }
  return vatCodeRecList;
 }
 
 public VatCodeRec vatCodeUpdate(VatCodeRec code, String pg){
  LOGGER.log(INFO, "VatDM.vatCodeUpdate called code {0}", code);
  LOGGER.log(INFO, "VAT code {0} id {1} created by {2} changed by {3}", new Object[]{
   code.getCode(), code.getId(), code.getCreatedBy(), code.getChangedBy()});
  if(!trans.isActive()){
   trans.begin();
  }
  VatCode cd = this.buildVatCode(code, pg);
  if(code.getId() == null){
   code.setId(cd.getId());
  }
  trans.commit();
  return code;
 }
 public List<VatRegSchemeRec> vatRegSchemeUpdatePvt(VatRegistration vatReg,List<VatRegSchemeRec> schemes, UserRec usr, String pg){
  LOGGER.log(INFO, "VatRegSchemeUpdatePvt");
  if(!trans.isActive()){
   trans.begin();
  }
  List<VatRegScheme> regSchemes =  vatReg.getVatRegSchemes();
  if(regSchemes == null){
  regSchemes = new ArrayList<>();
  }
  ListIterator<VatRegSchemeRec> schemeLi = schemes.listIterator();
  while(schemeLi.hasNext()){
   VatRegSchemeRec sch = schemeLi.next();
   VatRegScheme scheme = this.buildVatRegScheme(sch, usr, pg);
   if(sch.getId() == null){
    sch.setId(scheme.getId());
    scheme.setVatReg(vatReg);
    regSchemes.add(scheme);
   }
   schemeLi.set(sch);
  }
  
  trans.commit();
  return schemes;
 }
public VatRegistrationRec vatRegSchemeUpdate(VatRegistrationRec vatRegRec, UserRec usr, String pg){
 LOGGER.log(INFO, "VatRegSchemeUpdate called with varReg {0} usr id {1}", new Object[]{vatRegRec.getId(),usr.getId()});
 if(!trans.isActive()){
  trans.begin();
 }
 VatRegistration vatReg = em.find(VatRegistration.class, vatRegRec.getId(), OPTIMISTIC);
 List<VatRegScheme> regSchemes =  vatReg.getVatRegSchemes();
 if(regSchemes == null){
  regSchemes = new ArrayList<>();
 }
 List<VatRegSchemeRec> regSchemeRec = vatRegRec.getRegSchemes();
 ListIterator<VatRegSchemeRec> regSchLi = regSchemeRec.listIterator();
 while(regSchLi.hasNext()){
  VatRegSchemeRec regSch = regSchLi.next();
  VatRegScheme regSchDB = this.buildVatRegScheme(regSch, usr, pg);
  if(regSch.getId() == null){
   regSch.setId(regSchDB.getId());
   regSchDB.setVatReg(vatReg);
   regSchemes.add(regSchDB);
  }
  regSchLi.set(regSch);
 }
 vatRegRec.setRegSchemes(regSchemeRec);
 trans.commit();
 
 return vatRegRec;
}
public VatSchemeRec vatSchemeUpdate(VatSchemeRec scheme, UserRec user, String source) throws BacException {
  LOGGER.log(INFO, "vatSchemeUpdate called with scheme {0} user {1} source{2}", 
          new Object[]{scheme,user,source});
  if(!trans.isActive()){
   trans.begin();
  }
  VatScheme vatSc = buildVatScheme(scheme,source);
  
  trans.commit();
  
  return scheme;
 }

 
 
 
 
}
