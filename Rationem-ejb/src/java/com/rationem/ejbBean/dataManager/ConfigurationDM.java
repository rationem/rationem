/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.common.*;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.entity.config.company.PostType;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.user.User;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.config.company.Ledger;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.doc.DocTypeRec;
//import com.rationem.entity.config.fi.FiGlActType;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.entity.config.common.NumberRange;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.entity.audit.AuditAccountType;
import com.rationem.entity.audit.AuditChequeVoidReason;
import com.rationem.entity.audit.AuditDocRevReason;
import com.rationem.entity.audit.AuditDocType;
import com.rationem.entity.audit.AuditFundCategory;
import com.rationem.entity.audit.AuditLedger;
import com.rationem.entity.audit.AuditLineType;
import com.rationem.entity.audit.AuditLocaleCode;
import com.rationem.entity.audit.AuditModule;
import com.rationem.entity.audit.AuditNumberControl;
import com.rationem.entity.audit.AuditNumberRangeType;
import com.rationem.entity.audit.AuditPaymentTerms;
import com.rationem.entity.audit.AuditPaymentType;
import com.rationem.entity.audit.AuditPostType;
import com.rationem.entity.audit.AuditProcessCode;
import com.rationem.entity.audit.AuditSortOrder;
import com.rationem.entity.audit.AuditTransactionType;
import com.rationem.entity.audit.AuditUom;
import com.rationem.entity.audit.AuditValueDate;
import com.rationem.entity.config.common.*;
import com.rationem.entity.config.company.AccountType;
import com.rationem.entity.document.DocType;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.tr.bacs.BacsTransCode;
import com.rationem.entity.tr.bank.BankAccountCompany;
import com.rationem.entity.tr.bank.ChequeTemplate;
import javax.ejb.EJB;
import java.util.Date;
import java.util.ListIterator;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.Query;
import javax.persistence.Persistence;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import javax.persistence.TransactionRequiredException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import com.rationem.exception.BacException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.FINEST;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;




/**
 * Single point of DB access. All requests are queued
 * @author Chris
 */
@Stateless
public class ConfigurationDM { //implements ConfigurationDMRemote, Serializable {
 private static final Logger LOGGER = Logger.getLogger(ConfigurationDM.class.getName());



 private EntityManager em;
 private EntityTransaction trans;

 @EJB
 UserDM userDM;

 @EJB
 SysBuffer buffer;

 @EJB
 FiMasterRecordDM fiMastDM;

 @EJB
 TreasuryDM treasuryDM;
    
    


 @PostConstruct
 void init(){
  LOGGER.log(INFO,  "Loaded config data manager");
  FacesContext fc = FacesContext.getCurrentInstance();
  String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
  HashMap properties = new HashMap();
  properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();

    }

 private LocaleCode buildLocaleCode(LocaleCodeRec rec, String pg){
  LocaleCode loc;
  boolean newLoc = false;
  boolean changedLoc = false;
   
  if(rec.getId() == null){
   loc = new LocaleCode();
   User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
   loc.setCreatedBy(crUsr);
   loc.setCreatedOn(rec.getCreatedOn());
   em.persist(loc);
   AuditLocaleCode aud = this.buildAuditLocaleCode(loc, crUsr, 'I', pg);
   aud.setNewValue(loc.getLocaleCode());
   newLoc = true;
  }else{
   loc = em.find(LocaleCode.class, rec.getId(), OPTIMISTIC);
  }
   
  if(newLoc){
    loc.setCountryName(rec.getCountryName());
    loc.setLangName(rec.getLangName());
    loc.setLocaleCode(rec.getLocaleCode());
   }else{
    // changed?
    User chUsr = em.find(User.class, rec.getChangedBy(), OPTIMISTIC);
    
    if((rec.getCountryName() == null && loc.getCountryName() != null)||
       (rec.getCountryName() != null && loc.getCountryName() == null) ||
       (rec.getCountryName() != null && !rec.getCountryName().equals(loc.getCountryName()))){
     AuditLocaleCode aud = this.buildAuditLocaleCode(loc, chUsr, 'U', pg);
     aud.setFieldName("LOCALE_CNTRY");
     aud.setNewValue(rec.getCountryName());
     aud.setOldValue(loc.getCountryName());
     loc.setCountryName(rec.getCountryName());
     changedLoc = true;
    }
    
    if((rec.getLangName() == null && loc.getLangName() != null)||
       (rec.getLangName() != null && loc.getLangName() == null) ||
       (rec.getLangName() != null && !rec.getLangName().equals(loc.getLangName()))){
     AuditLocaleCode aud = this.buildAuditLocaleCode(loc, chUsr, 'U', pg);
     aud.setFieldName("LOCALE_LANG");
     aud.setNewValue(rec.getLangName());
     aud.setOldValue(loc.getLangName());
     loc.setLangName(rec.getLangName());
     changedLoc = true;
    }
    
    if((rec.getLocaleCode() == null && loc.getLocaleCode() != null)||
       (rec.getLocaleCode() != null && loc.getLocaleCode() == null) ||
       (rec.getLocaleCode() != null && !rec.getLocaleCode().equals(loc.getLocaleCode()))){
     AuditLocaleCode aud = this.buildAuditLocaleCode(loc, chUsr, 'U', pg);
     aud.setFieldName("LOCALE_CD");
     aud.setNewValue(rec.getLocaleCode());
     aud.setOldValue(loc.getLocaleCode());
     loc.setLangName(rec.getLocaleCode());
     changedLoc = true;
    }
   }
   return loc;
   
  }
     private Module buildModule(ModuleRec m){
        Module module; 
        if(m.getId() == null ||  m.getId() <1){
         module = new Module();
         module.setCreatedDate(m.getChangedDate());
         LOGGER.log(INFO, "m.getCreatedBy() {0}", m.getCreatedBy());
         User crUsr = em.find(User.class, m.getCreatedBy().getId(), OPTIMISTIC);
         module.setCreatedBy(crUsr);
         em.persist(module);
        }else{
         module = em.find(Module.class, m.getId(), OPTIMISTIC);
         module.setChangedDate(m.getChangedDate());
         User chUsr = em.find(User.class, m.getId(), OPTIMISTIC);
         module.setChangedBy(chUsr);
        }
        
        module.setName(m.getName());
        module.setDescription(m.getDescription());
        module.setModuleCode(m.getModuleCode());

        return module;

    }
    
    private ModuleRec buildModuleRec(Module m){
        ModuleRec moduleRec = new ModuleRec();
        moduleRec.setChangedDate(m.getChangedDate());
        moduleRec.setCreatedDate(m.getChangedDate());
        moduleRec.setId(m.getId());
        moduleRec.setName(m.getName());
        moduleRec.setDescription(m.getDescription());
        moduleRec.setModuleCode(m.getModuleCode());

        return moduleRec;

    }

    

    private LineTypeRule buildLineTypeRule(LineTypeRuleRec lineType, String pg) throws BacException{
     LOGGER.log(INFO," buildLineTypeRule called with: {0}",lineType);
     boolean newLineTy = false;
     boolean changedLineTy = false;
     LineTypeRule l; 
     if(lineType.getId() == null){
      l = new LineTypeRule();
      l.setCreatedDate(lineType.getCreatedDate());
      User crUsr = em.find(User.class, lineType.getCreatedBy().getId(), OPTIMISTIC);
      l.setCreatedBy(crUsr);
      em.persist(l);
      AuditLineType aud = this.buildAuditLineType(l, 'I', crUsr, pg);
      aud.setNewValue(lineType.getLineCode());
      newLineTy = true;
     }else{
      l = em.find(LineTypeRule.class, lineType.getId(), OPTIMISTIC);
     }
        
     if(newLineTy){
      l.setDescription(lineType.getDescription());
      l.setLineCode(lineType.getLineCode());
      Module m = em.find(Module.class, lineType.getModule().getId(), OPTIMISTIC);
      l.setModule(m);
      l.setSysUse(lineType.isSysUse());
     }else{
      //changed ?
      User chUsr = em.find(User.class, lineType.getChangedBy().getId(), OPTIMISTIC);
      
      if((lineType.getDescription() == null && l.getDescription() != null) ||
         (lineType.getDescription() != null && l.getDescription() == null) || 
         (lineType.getDescription() != null && !lineType.getDescription().equalsIgnoreCase(l.getDescription()))){
       AuditLineType aud = this.buildAuditLineType(l, 'U', chUsr, pg);
       aud.setFieldName("LN_TY_DESCR");
       aud.setNewValue(lineType.getDescription());
       aud.setOldValue(l.getDescription());
       l.setDescription(lineType.getDescription());
       changedLineTy = true;
      }
      if((lineType.getLineCode() == null && l.getLineCode() != null) ||
         (lineType.getLineCode() != null && l.getLineCode() == null) || 
         (lineType.getLineCode() != null && !lineType.getLineCode().equalsIgnoreCase(l.getLineCode()))){
       AuditLineType aud = this.buildAuditLineType(l, 'U', chUsr, pg);
       aud.setFieldName("LN_TY_CODE");
       aud.setNewValue(lineType.getLineCode());
       aud.setOldValue(l.getDescription());
       l.setDescription(lineType.getDescription());
       changedLineTy = true;
      }
      if((lineType.getModule() == null && l.getModule() != null) ||
         (lineType.getModule() != null && l.getModule() == null) || 
         (lineType.getModule() != null && lineType.getModule().getId() != l.getModule().getId())){
       AuditLineType aud = this.buildAuditLineType(l, 'U', chUsr, pg);
       aud.setFieldName("LN_TY_MODULE");
       aud.setNewValue(lineType.getModule().getModuleCode());
       aud.setOldValue(l.getModule().getModuleCode());
       Module m = em.find(Module.class, lineType.getModule().getId(), OPTIMISTIC);
       l.setModule(m);
       changedLineTy = true;
      }
      
      if(lineType.isSysUse() != l.isSysUse()){
       AuditLineType aud = this.buildAuditLineType(l, 'U', chUsr, pg);
       aud.setFieldName("LN_TY_SYS");
       aud.setNewValue(String.valueOf(lineType.isSysUse()));
       aud.setOldValue(String.valueOf(l.isSysUse()));
       l.setSysUse(lineType.isSysUse());
       changedLineTy = true;
      }
      if(changedLineTy){
       l.setChangedBy(chUsr);
       l.setChangedDate(lineType.getChangedDate());
      }
     }
     
        return l;
    }
    
    private LineTypeRuleRec buildLineTypeRuleRec(LineTypeRule lt) throws BacException{
        LOGGER.log(FINEST," buildLineTypeRuleRec called with: {0}",lt);
        LineTypeRuleRec l = new LineTypeRuleRec();
        l.setId(lt.getId());
        l.setCreatedDate(lt.getCreatedDate());
        UserRec crUsr = userDM.getUserRecPvt(lt.getCreatedBy());
        l.setCreatedBy(crUsr);
        if(lt.getModule() != null){
         ModuleRec mod = this.buildModuleRec(lt.getModule());
         l.setModule(mod);
        }
        l.setDescription(lt.getDescription());
        l.setLineCode(lt.getLineCode());
        if(lt.getChangedBy() != null){
         l.setChangedDate(lt.getChangedDate());
         UserRec chUser = userDM.getUserRecPvt(lt.getChangedBy());
         l.setChangedBy(chUser);
        }
        return l;
    }

   
   private AuditUom buildUomAudit(Uom u, String pg, User usr){
    AuditUom aud = new AuditUom();
    aud.setAuditDate(new Date());
    aud.setSource(pg);
    aud.setUom(u);
    aud.setCreatedBy(usr);
    
    return aud;
   }

    private UomRec buildUomRec(Uom u){
        LOGGER.log(FINEST, "buildUomRec called with code: {0}", u.getUomCode());
        UomRec uomRec = new UomRec();
        uomRec.setChangeDate(u.getChangeDate());
        uomRec.setCreateDate(u.getCreateDate());
        uomRec.setDescription(u.getDescription());
        uomRec.setId(u.getId());
        uomRec.setName(u.getName());
        uomRec.setProcessCode(u.getProcessCode());
        uomRec.setUomCode(u.getUomCode());
        uomRec.setRevision(u.getRevision());
        LOGGER.log(FINEST, "Return UomRec with uomCode {0}", uomRec.getUomCode());
        return uomRec;
    }

  private Uom buildUom(UomRec uom, String pg){
   LOGGER.log(FINEST, "buildUom called with code: {0}", uom.getUomCode());
   Uom u ;
   boolean newUom = false;
   boolean changedUom = false;
   if(uom.getId() == null){
    u = new Uom();
    User crUser = em.find(User.class, uom.getCreatedBy().getId());
    u.setCreatedBy(crUser);
    u.setCreateDate(uom.getCreateDate());
    em.persist(u);
    AuditUom aud = this.buildAuditUom(u, crUser, 'I', pg);
    aud.setNewValue(uom.getUomCode());
    newUom = true;
   }else{
    u = em.find(Uom.class, uom.getId(), OPTIMISTIC);
   }
   
   if(newUom){
    u.setDescription(uom.getDescription());
    u.setName(uom.getName());
    u.setProcessCode(uom.getProcessCode());
    u.setUomCode(uom.getUomCode());
   }else{
    // Uom changed ?
    User chUsr = em.find(User.class, uom.getChangedBy().getId(), OPTIMISTIC);
    
    if((uom.getDescription() == null && u.getDescription() != null) ||
        (uom.getDescription() != null && u.getDescription() == null) ||
       (uom.getDescription() != null && !uom.getDescription().equals(u.getDescription()) )){
     AuditUom aud = buildAuditUom(u, chUsr, 'U', pg);
     aud.setFieldName("UOM_DESCR");
     aud.setNewValue(uom.getDescription());
     aud.setOldValue(u.getDescription());
     u.setDescription(uom.getDescription());
     changedUom = true;
    }
    if((uom.getName() == null && u.getName() != null) ||
        (uom.getName() != null && u.getName() == null) ||
       (uom.getName() != null && !uom.getName().equals(u.getName()) )){
     AuditUom aud = buildAuditUom(u, chUsr, 'U', pg);
     aud.setFieldName("UOM_NAME");
     aud.setNewValue(uom.getName());
     aud.setOldValue(u.getName());
     u.setName(uom.getName());
     changedUom = true;
    }
    if((uom.getProcessCode() == null && u.getProcessCode() != null) ||
        (uom.getProcessCode() != null && u.getProcessCode() == null) ||
       (uom.getProcessCode() != null && !uom.getProcessCode().equals(u.getProcessCode()) )){
     AuditUom aud = buildAuditUom(u, chUsr, 'U', pg);
     aud.setFieldName("UOM_PROC_CD");
     aud.setNewValue(uom.getProcessCode());
     aud.setOldValue(u.getProcessCode());
     u.setProcessCode(uom.getProcessCode());
     changedUom = true;
    }
    if((uom.getUomCode() == null && u.getUomCode() != null) ||
        (uom.getUomCode() != null && u.getUomCode() == null) ||
       (uom.getUomCode() != null && !uom.getUomCode().equals(u.getUomCode()) )){
     AuditUom aud = buildAuditUom(u, chUsr, 'U', pg);
     aud.setFieldName("UOM_PROC_CD");
     aud.setNewValue(uom.getUomCode());
     aud.setOldValue(u.getUomCode());
     u.setUomCode(uom.getUomCode());
     changedUom = true;
    }
    if(changedUom){
     u.setChangedBy(chUsr);
     u.setChangeDate(uom.getChangeDate());
    }
   }
   
   return u;
  }
    
    private ValueDate buildValueDate(ValueDateRec rec){
     
     ValueDate vd;
     if(rec.getId() == null || rec.getId() == 0){
      vd = new ValueDate();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      vd.setCreatedBy(crUsr);
      vd.setCreatedOn(rec.getCreatedOn());
      em.persist(vd);
     }else{
      vd = em.find(ValueDate.class, rec.getId(), OPTIMISTIC);
     }
     if(rec.getChangedOn() != null){
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      vd.setChangedBy(chUsr);
      vd.setChangedOn(rec.getChangedOn());
     }
     vd.setCode(rec.getCode());
     vd.setDescription(rec.getDescription());
     vd.setNumber(rec.getNumber());
     vd.setOffset(rec.getOffset());
     if(rec.getUom() != null){
      Uom uom = em.find(Uom.class, rec.getUom().getId(), OPTIMISTIC);
      vd.setUom(uom);
     }
     vd.setNumber(vd.getId().intValue());
     return vd;
    }

    private NumberRange buildNumberRange(NumberRangeRec rec, String src){
     LOGGER.log(INFO, "buildNumberControl called with num cntrl {0} src {1}", new Object[]{rec,src});
     boolean newNc = false;
     boolean changedNc = false;
     
     
     NumberRange nc;
     if(rec.getNumberControlId() == null){
      if(StringUtils.equals(rec.getClass().getSimpleName(), "NumberRangeChequeRec")){
       nc = new NumberRangeCheque();
      }else{
       nc = new NumberRange();
      }
      
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      nc.setCreatedBy(crUsr);
      nc.setCreatedDate(rec.getCreatedDate());
      em.persist(nc);
      AuditNumberControl aud = this.buildAuditNumberControl(nc, rec.getCreatedDate(), 'I', crUsr, src);
      aud.setNewValue(rec.getShortDescr());
      newNc = true;
     }else{
      nc = em.find(NumberRange.class, rec.getNumberControlId(), OPTIMISTIC);
     }
     
     if(newNc){
      
     /* if(StringUtils.equals(rec.getClass().getSimpleName(), "NumberRangeCheque")){
       BankAccountCompany  bnk = em.find(BankAccountCompany.class, rec.getChequeNumberRangeForBankAccount().getId(), OPTIMISTIC);
       ((NumberRangeCheque)nc).setBankAccountComp(bnk);
      }
     */ 
      nc.setFromNum(rec.getFromNum());
      nc.setLongDescr(rec.getLongDescr());
      if(rec.getModule() != null){
       Module m = em.find(Module.class, rec.getModule().getId(), OPTIMISTIC);
       nc.setModule(m);
      }
      nc.setNextNum(rec.getNextNum());
      nc.setShortDescr(rec.getShortDescr());
      nc.setToNum(rec.getToNum());
      nc.setAutoNum(rec.isAutoNum());
      NumberRangeType nrTy = em.find(NumberRangeType.class, rec.getNumberRangeForType().getId());
      nc.setNumberRangeForType(nrTy);
      
     }else{
      // changed ?
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      LOGGER.log(INFO, "Number Control edit");
     
      
      if(rec.getFromNum() != nc.getFromNum()){
       AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
       aud.setNewValue(String.valueOf(rec.getFromNum()));
       aud.setOldValue(String.valueOf(nc.getFromNum()));
       nc.setFromNum(rec.getFromNum());
       changedNc =true;
      }
      
      if((rec.getLongDescr() == null && nc.getLongDescr() != null) ||
         (rec.getLongDescr() != null && nc.getLongDescr() == null) ||
         (rec.getLongDescr() != null && !rec.getLongDescr().equals(nc.getLongDescr()))){
        AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
        aud.setNewValue(rec.getLongDescr());
        aud.setOldValue(nc.getLongDescr());
        nc.setLongDescr(rec.getLongDescr());
        changedNc =true;
     }
      if((rec.getModule() == null && nc.getModule() != null) ||
         (rec.getModule() != null && nc.getModule() == null) ||
         (rec.getModule() != null && !Objects.equals(rec.getModule().getId(), nc.getModule().getId())) ){
        AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
        aud.setNewValue(rec.getModule().getModuleCode());
        aud.setOldValue(nc.getModule().getModuleCode());
        Module  m = em.find(Module.class , rec.getModule().getId(), OPTIMISTIC);
        nc.setModule(m);
        changedNc =true;
      }
      
      
      if(rec.getNextNum() != nc.getNextNum()){
       AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
       aud.setNewValue(String.valueOf(rec.getNextNum()));
       aud.setOldValue(String.valueOf(nc.getFromNum()));
       nc.setFromNum(rec.getNextNum());
       changedNc =true;
      }
      
      LOGGER.log(INFO, "rec short name {0} nc name {1}", 
        new Object[]{rec.getShortDescr(),nc.getShortDescr()});
      if((rec.getShortDescr() == null && nc.getShortDescr() != null) ||
         (rec.getShortDescr() != null && nc.getShortDescr() == null) ||
         (rec.getShortDescr() != null && !rec.getShortDescr().equals(nc.getShortDescr()) ) ){
        AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
        aud.setNewValue(rec.getShortDescr());
        aud.setOldValue(nc.getShortDescr());
        nc.setShortDescr(rec.getShortDescr());
        changedNc =true;
      }
      
      if(rec.getToNum() != nc.getToNum()){
       AuditNumberControl aud = buildAuditNumberControl(nc, rec.getCreatedDate(), 'U', chUsr, src);
        aud.setNewValue(String.valueOf(rec.getToNum()));
        aud.setOldValue(String.valueOf(rec.getToNum()));
        nc.setShortDescr(rec.getShortDescr());
        changedNc =true;
      }
     
      if(changedNc){
       nc.setChangedBy(chUsr);
       nc.setChangedDate(rec.getChangedDate());
      }
     }

      return nc;
    }
 
 private NumberRangeRec buildNumberRangeRec(NumberRange nr){
  
  NumberRangeRec nrRec = buildNumberControlRec(nr);
  return nrRec;
  
 }   
 private NumberRangeCheque buildNumberRangeCheque(NumberRangeChequeRec rec, String src){
   
  boolean newNumRng;
  
  newNumRng = rec.getNumberControlId() == null;
  
  NumberRangeCheque nc = (NumberRangeCheque)this.buildNumberRange(rec, src);
  
  if(newNumRng){
   BankAccountCompany ba = em.find(BankAccountCompany.class, rec.getBankAccountComp().getId());
   nc.setBankAccountComp(ba);
  }else{
   
  }
  
  
  
  
  return nc;
 }
 
 private NumberRangeChequeRec buildNumberRangeChequeRec(NumberRangeCheque nrChq){
  
  NumberRangeChequeRec rec = (NumberRangeChequeRec)buildNumberControlRec(nrChq);
  BankAccountCompanyRec bnkAcnt = treasuryDM.getBankAccountCompany(nrChq.getBankAccountComp());
  rec.setBankAccountComp(bnkAcnt);
  return rec;
 }
 
 private NumberRangeType buildNumberRangeType(NumberRangeTypeRec nrTyRec, String pg){
  LOGGER.log(INFO, "buildNumberRangeType called with create date{0}", nrTyRec.getCreatedDate());
  LOGGER.log(INFO, "num nr created on {0}", nrTyRec.getCreatedDate().toString());
  LOGGER.log(INFO, "num nr id {0}", nrTyRec.getId());
  boolean newNrTy = false;
  boolean changedNrTy = false;
  NumberRangeType nrTy;
  if(nrTyRec.getId() == null){
   newNrTy = true;
   nrTy = new NumberRangeType();
   User crUsr = em.find(User.class, nrTyRec.getCreatedBy().getId());
   nrTy.setCreatedBy(crUsr);
   nrTy.setCreatedDate(nrTyRec.getCreatedDate());
   em.persist(nrTy);
   AuditNumberRangeType aud = this.buildAuditNumberRangeType(nrTy, nrTy.getCreatedDate(), 'I', crUsr, pg);
  }else{
   nrTy = em.find(NumberRangeType.class, nrTyRec.getId());
  }
  if(newNrTy){
   nrTy.setNumRangeTypeCode(nrTyRec.getNumRangeTypeCode());
   nrTy.setNumRangeTypeName(nrTyRec.getNumRangeTypeName());
  }else{
   User chUsr = em.find(User.class, nrTyRec.getChangedBy().getId());
   
   if(!StringUtils.equals(nrTyRec.getNumRangeTypeCode(), nrTy.getNumRangeTypeCode())){
    changedNrTy = true;
    AuditNumberRangeType aud = this.buildAuditNumberRangeType(nrTy, nrTy.getCreatedDate(), 'U', chUsr, pg);
    aud.setFieldName("NUM_R_TYPE_CD");
    aud.setNewValue(nrTyRec.getNumRangeTypeCode());
    aud.setOldValue(nrTy.getNumRangeTypeCode());
    nrTy.setNumRangeTypeCode(nrTyRec.getNumRangeTypeCode());
   }
   
   if(!StringUtils.equals(nrTyRec.getNumRangeTypeName(), nrTy.getNumRangeTypeName())){
    changedNrTy = true;
    AuditNumberRangeType aud = this.buildAuditNumberRangeType(nrTy, nrTy.getCreatedDate(), 'U', chUsr, pg);
    aud.setFieldName("NUM_R_TYPE_NM");
    aud.setNewValue(nrTyRec.getNumRangeTypeName());
    aud.setOldValue(nrTy.getNumRangeTypeName());
    nrTy.setNumRangeTypeName(nrTyRec.getNumRangeTypeName());
   }
   if(changedNrTy){
    nrTy.setChangedBy(chUsr);
    nrTy.setChangedDate(nrTyRec.getChangedDate());
   }
  }
  
  return nrTy;
  
 }
 
 private NumberRangeTypeRec buildNumberRangeTypeRec(NumberRangeType nrTy){
  
  NumberRangeTypeRec nrTyRec = new NumberRangeTypeRec();
  nrTyRec.setId(nrTy.getId());
  UserRec crUsr = this.userDM.getUserRecPvt(nrTy.getCreatedBy());
  nrTyRec.setCreatedBy(crUsr);
  nrTyRec.setCreatedDate(nrTy.getCreatedDate());
  
  if(nrTy.getChangedBy() != null){
   UserRec chUsr = this.userDM.getUserRecPvt(nrTy.getChangedBy());
   nrTyRec.setChangedBy(chUsr);
   nrTyRec.setChangedDate(nrTy.getChangedDate());
  }
  
  nrTyRec.setNumRangeTypeCode(nrTy.getNumRangeTypeCode());
  nrTyRec.setNumRangeTypeName(nrTy.getNumRangeTypeName());
  return nrTyRec;
 }

 public NumberRangeRec getNumberControlRecPvt(NumberRange rec){
  return this.buildNumberControlRec(rec);
 }
  
 public List<NumberRangeChequeRec> getNumberRangeChequeAll(){
  Query q = em.createNamedQuery("chqBksAll");
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  List<NumberRangeChequeRec> retList = new ArrayList<>();
  for(Object o:rs){
   NumberRangeChequeRec rec = this.buildNumberRangeChequeRec((NumberRangeCheque)o);
   retList.add(rec);
  }
  return retList;
}
 
public List<NumberRangeTypeRec> getNumberRangeTypesAll(){
 
 TypedQuery q = em.createNamedQuery("allNumRangeTypes", NumberRangeType.class);
 List<NumberRangeType> rs = q.getResultList();
 if(rs == null){
  return null;
 }
 List<NumberRangeTypeRec> retList = new ArrayList<>();
 for(NumberRangeType nr:rs ){
  NumberRangeTypeRec nrTyRec = this.buildNumberRangeTypeRec(nr);
  retList.add(nrTyRec);
 }
 
 return retList;
}
    
private NumberRangeRec buildNumberControlRec(NumberRange rec){
 NumberRangeRec f = null;
 String nrClass = rec.getClass().getSimpleName();
 switch (nrClass){
  case "NumberRangeCheque":
   f = new NumberRangeChequeRec();
   break;
  case "NumberRange":
   f = new NumberRangeRec();
 }
 //NumberRangeCheque

 // NumberRangeRec f = new NumberRangeRec();
      
 if(rec.getModule() != null){
  // need to set the module
  Module m = rec.getModule();
  ModuleRec mRec = this.buildModuleRec(m);
  f.setModule(mRec);
 }
 f.setNumberControlId(rec.getNumberControlId());
 if(rec.getCreatedBy() != null){
  f.setCreatedDate(rec.getCreatedDate());
  UserRec usrCr = this.userDM.getUserRecPvt(rec.getCreatedBy());
  f.setCreatedBy(usrCr);
 }
 f.setAutoNum(rec.isAutoNum());
 if(rec.getChangedBy() != null){
  f.setChangedDate(rec.getChangedDate());
  UserRec usrCh = this.userDM.getUserRecPvt(rec.getChangedBy());
  f.setChangedBy(usrCh);
 }
      
 f.setCreatedDate(rec.getCreatedDate());
 f.setFromNum(rec.getFromNum());
 f.setLongDescr(rec.getLongDescr());
 f.setNextNum(rec.getNextNum());
 f.setNumberControlId(rec.getNumberControlId());
 f.setShortDescr(rec.getShortDescr());
 f.setToNum(rec.getToNum());
 f.setNumberControlId(rec.getNumberControlId());
 NumberRangeTypeRec nrTy = this.buffer.getNumRangeTypeById(rec.getNumberRangeForType().getId());
 f.setNumberRangeForType(nrTy);
      
 return f;
}

    private FundCategory buildFundCategory(FundCategoryRec catRec, String pg){
     FundCategory cat; 
     boolean newCat = false;
     boolean changedCat = false;
     
     if(catRec.getId() == null){
      cat =  new FundCategory();
      User crUsr = em.find(User.class, catRec.getCreatedBy().getId(), OPTIMISTIC);
      cat.setCreatedBy(crUsr);
      cat.setCreatedOn(catRec.getCreatedOn());
      em.persist(cat);
      AuditFundCategory aud = buildAuditFundCategory(cat, 'I', crUsr, pg);
      aud.setNewValue(catRec.getCatRef());
      newCat = true;
     }else{
      cat = em.find(FundCategory.class, catRec.getId(), OPTIMISTIC);
     }
     
     if(newCat){
      cat.setCatRef(catRec.getCatRef());
      cat.setDescription(catRec.getDescription());
      cat.setProcessCode(catRec.getProcessCode());
      cat.setRestricted(catRec.isRestricted());
      cat.setDesignated(catRec.isDesignated());
      cat.setEndowment(catRec.isEndowment());
      cat.setPermanent(catRec.isPermanent());
     }else{
      //Changed ?
      User chUsr = em.find(User.class, catRec.getChangedBy().getId(), OPTIMISTIC);
      
      if((catRec.getCatRef() == null && cat.getCatRef() != null) ||
         (catRec.getCatRef() != null && cat.getCatRef() == null) ||
         (catRec.getCatRef() != null && !catRec.getCatRef().equalsIgnoreCase(cat.getCatRef()))){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_REF");
       aud.setNewValue(catRec.getCatRef());
       aud.setOldValue(cat.getCatRef());
       cat.setCatRef(catRec.getCatRef());
       changedCat = true;
      }
      if((catRec.getDescription() == null && cat.getDescription() != null) ||
         (catRec.getDescription() != null && cat.getDescription() == null) ||
         (catRec.getDescription() != null && !catRec.getDescription().equalsIgnoreCase(cat.getDescription()))){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_DESCR");
       aud.setNewValue(catRec.getDescription());
       aud.setOldValue(cat.getDescription());
       cat.setDescription(catRec.getDescription());
       changedCat = true;
      }
      
      if((catRec.getProcessCode() == null && cat.getProcessCode() != null) ||
         (catRec.getProcessCode() != null && cat.getProcessCode() == null) ||
         (catRec.getProcessCode() != null && !catRec.getProcessCode().equalsIgnoreCase(cat.getProcessCode()))){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_PROCESS_CD");
       aud.setNewValue(catRec.getProcessCode());
       aud.setOldValue(cat.getProcessCode());
       cat.setProcessCode(catRec.getProcessCode());
       changedCat = true;
      }
      
      if(catRec.isRestricted() != cat.isRestricted()){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_RESTR");
       aud.setNewValue(String.valueOf(catRec.isRestricted()));
       aud.setOldValue(String.valueOf(cat.isRestricted()));
       cat.setRestricted(catRec.isRestricted());
       changedCat = true;
      }
      if(catRec.isDesignated() != cat.isDesignated()){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_DESIG");
       aud.setNewValue(String.valueOf(catRec.isDesignated()));
       aud.setOldValue(String.valueOf(cat.isDesignated()));
       cat.setDesignated(catRec.isDesignated());
       changedCat = true;
      }
      if(catRec.isEndowment() != cat.isEndowment()){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_CAP");
       aud.setNewValue(String.valueOf(catRec.isEndowment()));
       aud.setOldValue(String.valueOf(cat.isEndowment()));
       cat.setEndowment(catRec.isEndowment());
       changedCat = true;
      }
      if(catRec.isPermanent() != cat.isPermanent()){
       AuditFundCategory aud = buildAuditFundCategory(cat, 'U', chUsr, pg);
       aud.setFieldName("FND_CAT_CAP");
       aud.setNewValue(String.valueOf(catRec.isPermanent()));
       aud.setOldValue(String.valueOf(cat.isPermanent()));
       cat.setPermanent(catRec.isPermanent());
       changedCat = true;
      }
      if(changedCat){
       cat.setChangedBy(chUsr);
       cat.setChangedOn(catRec.getChangedOn());
      }
     }
     
     return cat;
    }
  
  private ChequeVoidReason buildChequeVoidReason(ChequeVoidReasonRec rsnRec, String pg){
   
   ChequeVoidReason rsn ;
   boolean newReason = false;
   boolean changedReason= false;
   if(rsnRec.getId() == null){
    rsn = new ChequeVoidReason();
    User crUsr = em.find(User.class, rsnRec.getCreatedBy().getId());
    rsn.setCreatedDate(rsnRec.getCreatedDate());
    rsn.setCreatedBy(crUsr);
    em.persist(rsn);
    AuditChequeVoidReason aud = buildAuditChequeVoidReason(rsn, crUsr, 'C', pg);
    newReason = true;
   }else{
    rsn = em.find(ChequeVoidReason.class, rsnRec.getId());
   }
   
   if(newReason){
    rsn.setCode(rsnRec.getCode());
    rsn.setDescription(rsnRec.getDescription());
   }else{
    // changed reason
    User chUsr = em.find(User.class, rsnRec.getChangedBy().getId(), OPTIMISTIC);
    
    if(!StringUtils.equals(rsnRec.getCode(), rsn.getCode())){
     AuditChequeVoidReason aud = buildAuditChequeVoidReason(rsn, chUsr, 'u', pg);
     aud.setFieldName("BNK_CHQ_VOID_CD");
     aud.setNewValue(rsnRec.getCode());
     aud.setOldValue(rsn.getCode());
     rsn.setCode(rsnRec.getCode());
     changedReason = true;
    }
    
    if(!StringUtils.equals(rsnRec.getDescription(), rsn.getDescription())){
     AuditChequeVoidReason aud = buildAuditChequeVoidReason(rsn, chUsr, 'u', pg);
     aud.setFieldName("BNK_CHQ_VOID_DESCR");
     aud.setNewValue(rsnRec.getDescription());
     aud.setOldValue(rsn.getDescription());
     rsn.setDescription(rsnRec.getDescription());
     changedReason = true;
    }
    
    if(rsnRec.isSysUse() != rsn.isSysUse()){
     AuditChequeVoidReason aud = buildAuditChequeVoidReason(rsn, chUsr, 'u', pg);
     aud.setFieldName("BNK_CHQ_VOID_SYS");
     aud.setNewValue(String.valueOf(rsnRec.isSysUse()));
     aud.setOldValue(String.valueOf(rsn.isSysUse()));
     rsn.setSysUse(rsnRec.isSysUse());
     changedReason = true;
    }
    if(changedReason){
     rsn.setChangedBy(chUsr);
     rsn.setChangedDate(rsnRec.getChangedDate());
    }
    
   }
    
   return rsn;
  }
  
  private ChequeVoidReasonRec buildChequeVoidReasonRec(ChequeVoidReason rsn){
   ChequeVoidReasonRec rsnRec = new ChequeVoidReasonRec();
   rsnRec.setId(rsn.getId());
   UserRec usrRec = this.userDM.getUserRecPvt(rsn.getCreatedBy());
   rsnRec.setCreatedBy(usrRec);
   rsnRec.setCreatedDate(rsn.getCreatedDate());
   if(rsn.getChangedBy() != null){
    usrRec = this.userDM.getUserRecPvt(rsn.getChangedBy());
    rsnRec.setChangedBy(usrRec);
    rsnRec.setChangedDate(rsn.getChangedDate());
   }
   rsnRec.setCode(rsn.getCode());
   rsnRec.setDescription(rsn.getDescription());
   rsnRec.setSysUse(rsn.isSysUse());
   
   return rsnRec;
  }
  
  private DocReversalReason buildDocReversalReason(DocReversalReasonRec rsnRec, String pg){
   DocReversalReason rsn;
   boolean newRev = false;
   boolean changedRev = false;
   if(rsnRec.getId() == null){
    rsn = new DocReversalReason();
    User crUsr = em.find(User.class, rsnRec.getCreatedBy().getId());
    rsn.setCreatedDate(rsnRec.getCreatedDate());
    rsn.setCreatedBy(crUsr);
    em.persist(rsn);
    AuditDocRevReason aud = buildAuditDocRevReason(rsn, crUsr, 'C', pg);
    newRev = true;
   }else{
    rsn = em.find(DocReversalReason.class, rsnRec.getId());
   }
   if(newRev){
    rsn.setCode(rsnRec.getCode());
    rsn.setDescription(rsnRec.getDescription());
    rsn.setSysUse(rsnRec.isSysUse());
   }else{
    // changes made
    User chUsr = em.find(User.class, rsnRec.getChangedBy().getId());
    if(!StringUtils.equals(rsnRec.getCode(), rsn.getCode())){
     AuditDocRevReason aud = buildAuditDocRevReason(rsn, chUsr, 'U', pg);
     aud.setFieldName("DOC_REV_CODE");
     aud.setNewValue(rsnRec.getCode());
     aud.setOldValue(rsn.getCode());
     rsn.setCode(rsnRec.getCode());
     changedRev = true;
    }
    
    if(!StringUtils.equals(rsnRec.getDescription(), rsn.getDescription())){
     AuditDocRevReason aud = buildAuditDocRevReason(rsn, chUsr, 'U', pg);
     aud.setFieldName("DOC_REV_DESCR");
     aud.setNewValue(rsnRec.getDescription());
     aud.setOldValue(rsn.getDescription());
     rsn.setDescription(rsnRec.getDescription());
     changedRev = true;
    }
    
    if(rsnRec.isSysUse() != rsn.isSysUse() ){
     AuditDocRevReason aud = buildAuditDocRevReason(rsn, chUsr, 'U', pg);
     aud.setFieldName("DOC_REV_SYS");
     aud.setNewValue(String.valueOf(rsnRec.isSysUse()));
     aud.setOldValue(String.valueOf(rsnRec.isSysUse()));
     rsn.setSysUse(rsnRec.isSysUse());
     changedRev = true;
    }
    if(changedRev){
     rsn.setChangedBy(chUsr);
     rsn.setChangedDate(rsnRec.getChangedDate());
    }
   }
   
   return rsn;
  }
  
  private DocType buildDocType(DocTypeRec rec, String pg){
     DocType dt;
     boolean newDocTy = false;
     boolean changedDocTy = false;
     if(rec.getId() == null){
      dt = new DocType();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      dt.setCreated(new Date());
      dt.setCreatedBy(crUsr);
      em.persist(dt);
      AuditDocType aud = this.buildAuditDocType(dt, crUsr, 'I', pg);
      aud.setNewValue(rec.getCode());
      newDocTy = true;
     }else{
      dt = em.find(DocType.class, rec.getId(), OPTIMISTIC);
     }
     
     if(newDocTy){
      dt.setApAllowed(rec.isApAllowed());
      dt.setArAllowed(rec.isArAllowed());
      dt.setCode(rec.getCode());
      dt.setGlAllowed(rec.isGlAllowed());
      dt.setName(rec.getName());
      dt.setTrAllowed(rec.isTrAllowed());
     }else{
      // Changed ?
      LOGGER.log(INFO,"update doc type");
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      
      if(rec.isApAllowed() != dt.isApAllowed()){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_AP");
       aud.setNewValue(String.valueOf(rec.isApAllowed()));
       aud.setOldValue(String.valueOf(dt.isApAllowed()));
       dt.setApAllowed(rec.isApAllowed());
       changedDocTy = true;
      }
      
      if(rec.isArAllowed() != dt.isArAllowed()){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_AR");
       aud.setNewValue(String.valueOf(rec.isArAllowed()));
       aud.setOldValue(String.valueOf(dt.isArAllowed()));
       dt.setArAllowed(rec.isArAllowed());
       changedDocTy = true;
      }
      
      if(rec.isGlAllowed() != dt.isGlAllowed()){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_GL");
       aud.setNewValue(String.valueOf(rec.isGlAllowed()));
       aud.setOldValue(String.valueOf(dt.isGlAllowed()));
       dt.setGlAllowed(rec.isGlAllowed());
       changedDocTy = true;
      }
      
      if(rec.isGlAllowed() != dt.isGlAllowed()){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_GL");
       aud.setNewValue(String.valueOf(rec.isGlAllowed()));
       aud.setOldValue(String.valueOf(dt.isGlAllowed()));
       dt.setGlAllowed(rec.isGlAllowed());
       changedDocTy = true;
      }
      
      if(rec.isTrAllowed() != dt.isTrAllowed()){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_TR");
       aud.setNewValue(String.valueOf(rec.isTrAllowed()));
       aud.setOldValue(String.valueOf(dt.isTrAllowed()));
       dt.setTrAllowed(rec.isTrAllowed());
       changedDocTy = true;
      }
      
      if((rec.getCode() == null && dt.getCode() != null) ||
         (rec.getCode() != null && dt.getCode() == null) ||
         (rec.getCode() != null && !rec.getCode().equals(dt.getCode())  )  ){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_CODE");
       aud.setNewValue(rec.getCode());
       aud.setOldValue(dt.getCode());
       dt.setCode(rec.getCode());
       changedDocTy = true;
      }
      
      if((rec.getName() == null && dt.getName() != null) ||
         (rec.getName() != null && dt.getName() == null) ||
         (rec.getName() != null && !rec.getName().equals(dt.getName())  )  ){
       AuditDocType aud = this.buildAuditDocType(dt, chUsr, 'U', pg);
       aud.setFieldName("DOC_TY_NAME");
       aud.setNewValue(rec.getName());
       aud.setOldValue(dt.getName());
       dt.setName(rec.getName());
       changedDocTy = true;
      }
      
      if(changedDocTy){
       dt.setChangedBy(chUsr);
       dt.setChangedOn(rec.getChangedOn());
      }
     }
     return dt;
    }
    private FundCategoryRec buildFundCategory(FundCategory cat){
     FundCategoryRec catRec = new FundCategoryRec(); 
     UserRec crUsr = this.userDM.getUserRecPvt(cat.getCreatedBy());
     catRec.setCreatedBy(crUsr);
     catRec.setCreatedOn(cat.getCreatedOn());
     if(cat.getChangedBy() != null){
      UserRec chUsr = userDM.getUserRecPvt(cat.getCreatedBy());
      catRec.setChangedBy(chUsr);
      catRec.setChangedOn(cat.getChangedOn());
     }
     catRec.setCatRef(cat.getCatRef());
     catRec.setDescription(cat.getDescription());
     catRec.setId(cat.getId());
     catRec.setProcessCode(cat.getProcessCode());
     catRec.setRestricted(cat.isRestricted());
     return catRec;
     
     }
    
    
    private Ledger buildLedger(LedgerRec rec, String pg){
     LOGGER.log(INFO, "buildLedger called with {0}", rec);
     trans.begin();
        Ledger l;
        if(rec.getId() == null || rec.getId() < 1){
         l= new Ledger();
         User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
         l.setCreatedBy(crUsr);
         l.setCreatedDate(rec.getCreatedDate());
         em.persist(l);
         AuditLedger aud = new AuditLedger();
         aud.setAuditDate(new Date());
         aud.setCreatedBy(crUsr);
         aud.setLedger(l);
         aud.setNewValue(rec.getCode());
         aud.setSource(pg);
         aud.setUsrAction('I');
         LOGGER.log(INFO, "Ledger id {0}", l.getId());
        }else{
         l = em.find(Ledger.class, rec.getId(), OPTIMISTIC);
        }
        l.setId(rec.getId());
        l.setChangedDate(rec.getChangedDate());
        l.setCreatedDate(rec.getCreatedDate());
        l.setName(rec.getName());
        l.setDescr(rec.getDescr());
        l.setCode(rec.getCode());
    trans.commit();
        return l;
    }

    public LedgerRec buildLedgerRecPvt(Ledger l){
      LOGGER.log(FINEST, "buildLedgerRec called with: {0}", l.getId());
      return this.buildLedgerRec(l);
    }

    private LedgerRec buildLedgerRec(Ledger l){
        LOGGER.log(FINEST, "buildLedgerRec called with: {0}", l.getId());
        LedgerRec lgr = new LedgerRec();
        lgr.setId(l.getId());

        lgr.setChangedDate(l.getChangedDate());
        lgr.setCreatedDate(l.getCreatedDate());
        lgr.setName(l.getName());
        lgr.setDescr(l.getDescr());
        lgr.setCode(l.getCode());
        return lgr;
    }

    private AuditAccountType buildAuditAccountType(AccountType ty, User usr, char usrAction, String pg){
     AuditAccountType aud = new AuditAccountType();
     aud.setAccountType(ty);
     aud.setAuditDate(new Date());
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AccountType buildAccountType(AccountTypeRec rec, String pg){
     LOGGER.log(INFO, "buildAccountType  ");
     LOGGER.log(INFO, "Account type code {0} process code {1}", new Object[]{
      rec.getName(),rec.getProcessCode().getName() });
     boolean newActTy = false;
     boolean changedActTy = false;
     
     AccountType f;
     if(rec.getId() == null){
      // account type
      f = new AccountType();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      f.setCreatedBy(crUsr);
      f.setCreatedDate(rec.getCreatedDate());
      em.persist(f);
      AuditAccountType aud = this.buildAuditAccountType(f, crUsr, 'I', pg);
      aud.setNewValue(rec.getName());
      newActTy = true;
     }else{
      f = em.find(AccountType.class, rec.getId(), OPTIMISTIC);
     }
         
     if(newActTy){   
      f.setName(rec.getName());
      f.setDebit(rec.isDebit());
      f.setDescription(rec.getDescription());
      f.setProfitAndLossAccount(rec.isProfitAndLossAccount());
      f.setRetainedEarn(rec.isRetainedEarn());
      f.setChanges(rec.getChanges());
      f.setSystemPost(rec.isSystemPost());
        
      if(rec.getSubLedger() != null){
       Ledger l = em.find(Ledger.class, rec.getSubLedger().getId());
       f.setSubLedger(l);
      }
      
      if(rec.getProcessCode() != null){
       ProcessCode pr = em.find(ProcessCode.class, rec.getProcessCode().getId(), OPTIMISTIC);
       f.setProcessCode(pr);
      }
      
      if(rec.getModule() != null){
       Module m = em.find(Module.class, rec.getModule().getId(), OPTIMISTIC);
       f.setModule(m);
      }
      if(rec.getNumberRange() != null){
      
      NumberRange numCntrl = em.find(NumberRange.class, rec.getNumberRange().getNumberControlId(), OPTIMISTIC);
      f.setNumberRange(numCntrl);
      }
     }else{
      //Changed ?
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      if(rec.isControlAccount() != f.isControlAccount()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_CNTRL");
       aud.setNewValue(String.valueOf(rec.isControlAccount()));
       aud.setOldValue(String.valueOf(f.isControlAccount()));
       f.setControlAccount(rec.isControlAccount());
       changedActTy = true;
      }
      
      if(rec.isDebit() != f.isDebit()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_DR");
       aud.setNewValue(String.valueOf(rec.isDebit()));
       aud.setOldValue(String.valueOf(f.isDebit()));
       f.setDebit(rec.isDebit());
       changedActTy = true;
      }
      
      if(rec.isProfitAndLossAccount() != f.isProfitAndLossAccount()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_PL");
       aud.setNewValue(String.valueOf(rec.isProfitAndLossAccount()));
       aud.setOldValue(String.valueOf(f.isProfitAndLossAccount()));
       f.setProfitAndLossAccount(rec.isProfitAndLossAccount());
       changedActTy = true;
      }
      
      if(rec.isRetainedEarn() != f.isRetainedEarn()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_RET_EARN");
      }
      
      if((rec.getName() == null && f.getName() != null) ||
         (rec.getName() != null && f.getName() == null) ||
         (rec.getName() != null && !rec.getName().equalsIgnoreCase(f.getName())) ){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_NAME");
       aud.setNewValue(rec.getName());
       aud.setOldValue(f.getName());
       f.setName(rec.getName());
       changedActTy = true;
      }
      
      if((rec.getNumberRange() == null && f.getNumberRange() != null) ||
         (rec.getNumberRange() != null && f.getNumberRange() == null) ||
         (rec.getNumberRange() != null && 
              rec.getNumberRange().getNumberControlId() != f.getNumberRange().getNumberControlId()) ){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_NUM_RNG");
       aud.setNewValue(rec.getNumberRange().getShortDescr());
       aud.setOldValue(f.getNumberRange().getShortDescr());
       NumberRange n = em.find(NumberRange.class, rec.getNumberRange().getNumberControlId(), OPTIMISTIC);
       f.setNumberRange(n);
       changedActTy = true;
     }
     
     if((rec.getProcessCode() == null && f.getProcessCode() != null) ||
        (rec.getProcessCode() != null && f.getProcessCode() == null) ||
        (rec.getProcessCode() != null && !Objects.equals(rec.getProcessCode().getId(), f.getProcessCode().getId()))){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_PR_CD");
       aud.setNewValue(rec.getProcessCode().getName());
       if(f.getProcessCode() != null){
       aud.setOldValue(f.getProcessCode().getName());
       }
       ProcessCode p = em.find(ProcessCode.class, rec.getProcessCode().getId(), OPTIMISTIC);
       f.setProcessCode(p);
       changedActTy = true;
     }
     
     if(rec.isRetainedEarn() != f.isRetainedEarn()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_RET_EARN");
       aud.setNewValue(String.valueOf(rec.isRetainedEarn()));
       aud.setOldValue(String.valueOf(f.isRetainedEarn()));
       f.setRetainedEarn(rec.isRetainedEarn());
       changedActTy = true;
      }
      
      if(rec.isSystemPost() != f.isSystemPost()){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_SYS_PST");
       aud.setNewValue(String.valueOf(rec.isRetainedEarn()));
       aud.setOldValue(String.valueOf(f.isRetainedEarn()));
       f.setRetainedEarn(rec.isRetainedEarn());
       changedActTy = true;
      }
      
      if((rec.getDescription() == null && f.getDescription() != null) ||
         (rec.getDescription() != null && f.getDescription() == null) || 
         (rec.getDescription() != null && !rec.getDescription().equalsIgnoreCase(f.getDescription()))     ){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_DESCR");
       aud.setNewValue(rec.getDescription());
       aud.setOldValue(f.getDescription());
       f.setDescription(rec.getDescription());
       changedActTy = true;
      }
      
      if((rec.getModule() == null && f.getModule() != null) ||
         (rec.getModule() != null && f.getModule() == null) ||
          (rec.getModule() != null && rec.getModule().getId() != f.getModule().getId())    ){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_MOD");
       aud.setNewValue(rec.getModule().getName());
       aud.setOldValue(f.getModule().getName());
       Module m = em.find(Module.class, rec.getModule().getId(), OPTIMISTIC);
       f.setModule(m);
       changedActTy = true;
      }
      
      if(  (rec.getSubLedger() != null && f.getSubLedger() == null) ||
        (rec.getSubLedger() != null && rec.getSubLedger().getId() !=  f.getSubLedger().getId()) ){
       AuditAccountType aud = this.buildAuditAccountType(f, chUsr, 'U', pg);
       aud.setFieldName("ACT_TY_LED");
       aud.setNewValue(rec.getSubLedger().getName());
       aud.setOldValue(f.getSubLedger().getName());
       Ledger l = em.find(Ledger.class, rec.getSubLedger().getId(), OPTIMISTIC);
       f.setSubLedger(l);
       changedActTy = true;
     }
     
     if(changedActTy){
      f.setChangedBy(chUsr);
      f.setChangedDate(rec.getChangedDate());
     }
      
      
     }

       return f;
    }

    private AuditChequeVoidReason buildAuditChequeVoidReason(ChequeVoidReason rsn, User usr, char usrAct, String pg){
     AuditChequeVoidReason aud = new AuditChequeVoidReason();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAct);
     aud.setVoidReason(rsn);
     em.persist(aud);
     return aud;
    }
    
    private AuditDocRevReason buildAuditDocRevReason(DocReversalReason rsn, User usr, char usrAct, String pg){
     AuditDocRevReason aud = new AuditDocRevReason();
     aud.setAuditDate(new Date());
     aud.setDocRevReason(rsn);
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAct);
     
     em.persist(aud);
     return aud;
    }
    
    private AuditDocType buildAuditDocType(DocType docTy, User usr, char usrAct, String pg){
     AuditDocType aud = new AuditDocType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setDocType(docTy);
     aud.setSource(pg);
     aud.setUsrAction(usrAct);
     em.persist(aud);
     return aud;
    }
    
    private AuditFundCategory buildAuditFundCategory(FundCategory fndCat, char usrAct,User usr, String pg){
     AuditFundCategory aud = new AuditFundCategory();
     aud.setAuditDate(new Date());
     aud.setFndCategory(fndCat);
     aud.setSource(pg);
     aud.setUsrAction(usrAct);
     em.persist(aud);
     return aud;
    }
    
    private AuditLedger buildAuditLedger(Ledger led, char usrAction, User usr, String source){
     AuditLedger aud = new AuditLedger();
     aud.setAuditDate(new Date());
     aud.setLedger(led);
     aud.setSource(source);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    private AuditLineType buildAuditLineType(LineTypeRule type,char usrAction, User usr, String source){
     AuditLineType aud = new AuditLineType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setLineType(type);
     aud.setSource(source);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditLocaleCode buildAuditLocaleCode(LocaleCode loc,User usr, char usrAction, 
           String pg){
    AuditLocaleCode aud = new AuditLocaleCode();
    aud.setCreatedBy(usr);
    aud.setAuditDate(new Date());
    aud.setSource(pg);
    aud.setUsrAction(usrAction);
    em.persist(aud);
    return aud;
   }
    
    private AuditNumberControl buildAuditNumberControl(NumberRange nc, Date audDate,char usrAction, 
            User usr,String pg){
     AuditNumberControl aud = new AuditNumberControl();
     aud.setAuditDate(audDate);
     aud.setCreatedBy(usr);
     aud.setNumControl(nc);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditNumberRangeType buildAuditNumberRangeType(NumberRangeType nr, Date audDate, 
            char userAction,User usr, String pg){
     AuditNumberRangeType aud = new AuditNumberRangeType();
     aud.setAuditDate(audDate);
     aud.setCreatedBy(usr);
     aud.setNrTy(nr);
     aud.setSource(pg);
     aud.setUsrAction(userAction);
     em.persist(aud);
     return aud;
    }
    private AuditPaymentTerms buildAuditPaymentTerms(PaymentTerms terms, char usrAction, 
      User usr,String pg){
     
     AuditPaymentTerms aud = new AuditPaymentTerms();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
     
    }
    
    private AuditPaymentType buildAuditPaymentType(PaymentType payTy, char usrAction, 
      User usr,String pg){
     AuditPaymentType aud = new AuditPaymentType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setPayType(payTy);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    private AuditPostType buildAuditPostType(PostType pstTy, char usrAction, User usr,String pg){
     AuditPostType aud = new AuditPostType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setPostType(pstTy);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditSortOrder buildAuditSortOrder(SortOrder sort, User usr,char usrAction,String pg){
     AuditSortOrder aud = new AuditSortOrder();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSortOrder(sort);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditTransactionType buildAuditTransactionType(TransactionType transTy, User usr,char usrAction,String pg){
     AuditTransactionType aud = new AuditTransactionType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setTransTy(transTy);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditUom buildAuditUom(Uom uom, User usr, char usrAction, String pg){
     AuditUom aud = new AuditUom();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUom(uom);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    public AccountTypeRec buildAccountTypeRecPvt(AccountType rec){
      return this.buildAccountTypeRec(rec);

    }
        private AccountTypeRec buildAccountTypeRec(AccountType rec){
         
        AccountTypeRec f = new AccountTypeRec();
        f.setId(rec.getId());
        f.setName(rec.getName());
        UserRec crUser = this.userDM.getUserRecPvt(rec.getCreatedBy());
        f.setCreatedBy(crUser);
        f.setCreatedDate(rec.getCreatedDate());
        if(rec.getChangedBy() != null){
        UserRec chUser = userDM.getUserRecPvt(rec.getChangedBy());
         f.setChangedBy(chUser);
         f.setChangedDate(rec.getChangedDate());
        }
        f.setDebit(rec.isDebit());
        f.setDescription(rec.getDescription());
        f.setProfitAndLossAccount(rec.isProfitAndLossAccount());
        f.setDescription(rec.getDescription());
        f.setRetainedEarn(rec.isRetainedEarn());
        f.setChanges(rec.getChanges());
        f.setSystemPost(rec.isSystemPost());
        if(rec.getSubLedger() != null){
          LedgerRec l = this.buildLedgerRec(rec.getSubLedger());
          f.setSubLedger(l);
          f.setControlAccount(rec.isControlAccount());
        }
        if(rec.getNumberRange() != null){
          NumberRangeRec numRec = this.buildNumberControlRec(rec.getNumberRange());
          f.setNumberRange(numRec);  
        }
        if(rec.getProcessCode() != null){
         ProcessCodeRec pr = this.buildProcessCodeRec(rec.getProcessCode());
         f.setProcessCode(pr);
        }

        return f;
    }

        private SortOrder buildSortOrder(SortOrderRec sortRec,String pg){
          boolean newSort = false;
          boolean changedSort =false;
          SortOrder sortOrder;
          if(sortRec.getId() == null){
            sortOrder = new SortOrder();
            User crUsr = em.find(User.class, sortRec.getCreatedBy().getId(), OPTIMISTIC);
            sortOrder.setCreatedBy(crUsr);
            sortOrder.setCreatedOn(sortRec.getCreatedOn());
            em.persist(sortOrder);
            AuditSortOrder aud = this.buildAuditSortOrder(sortOrder, crUsr, 'I', pg);
            aud.setNewValue(sortRec.getSortCode());
            newSort = true;
          }else{
            sortOrder = em.find(SortOrder.class, sortRec.getId(), OPTIMISTIC);
          }
          
          if(newSort){
           sortOrder.setDescription(sortRec.getDescription());
           sortOrder.setName(sortRec.getName());
           
           sortOrder.setSortCode(sortRec.getSortCode());
          }else{
           //changed ?
           User chUsr = em.find(User.class, sortRec.getId(), OPTIMISTIC);
           if((sortRec.getDescription() == null && sortOrder.getDescription() != null ) ||
              (sortRec.getDescription() != null && sortOrder.getDescription() == null ) ||
              (sortRec.getDescription() != null && !sortRec.getDescription().equalsIgnoreCase(sortOrder.getDescription()))){
            AuditSortOrder aud = this.buildAuditSortOrder(sortOrder, chUsr, 'U', pg);
            aud.setFieldName("SORT_ODR_DESCR");
            aud.setNewValue(sortRec.getDescription());
            aud.setOldValue(sortOrder.getDescription());
            sortOrder.setDescription(sortRec.getDescription());
            changedSort = true;
           }
           
           if((sortRec.getName() == null&& sortOrder.getName() != null) ||
              (sortRec.getName() != null && sortOrder.getName() == null) ||
              (sortRec.getName() != null && !sortRec.getName().equalsIgnoreCase(sortOrder.getName()))){
            AuditSortOrder aud = this.buildAuditSortOrder(sortOrder, chUsr, 'U', pg);
            aud.setFieldName("SORT_ODR_NAME");
            aud.setNewValue(sortRec.getName());
            aud.setOldValue(sortOrder.getName());
            sortOrder.setName(sortRec.getName());
            changedSort = true;
           }
           
           if((sortRec.getSortCode() == null && sortOrder.getSortCode() != null) ||
              (sortRec.getSortCode() != null && sortOrder.getSortCode() == null) ||
              (sortRec.getSortCode() != null && !sortRec.getSortCode().equalsIgnoreCase(sortOrder.getSortCode()))){
             AuditSortOrder aud = this.buildAuditSortOrder(sortOrder, chUsr, 'U', pg);
             aud.setFieldName("SORT_ODR_CODE");
             aud.setNewValue(sortRec.getSortCode());
             aud.setOldValue(sortOrder.getSortCode());
             sortOrder.setSortCode(sortRec.getSortCode());
             changedSort = true;
           }
           
           if(changedSort){
            sortOrder.setUpdatedBy(chUsr);
            sortOrder.setUpdatedOn(sortRec.getUpdatedOn());
           }
          }
          
         
          return sortOrder;
        }

        private TransactionTypeRec buildTransactionTypeRec(TransactionType tt){
         TransactionTypeRec ret = new TransactionTypeRec();
         ret.setId(tt.getId());
         if(tt.getChangedBy() != null){
          UserRec usr = this.userDM.getUserRecPvt(tt.getChangedBy());
          ret.setChangedBy(usr);
          ret.setChangedOn(tt.getChangedOn());
         }
         ret.setCode(tt.getCode());
         UserRec usrCr = userDM.getUserRecPvt(tt.getCreatedBy());
         ret.setCreatedBy(usrCr);
         ret.setCreatedOn(tt.getCreatedOn());
         ret.setDescription(tt.getDescription());
         if(tt.getLedger() != null){
          LedgerRec led = buildLedgerRec(tt.getLedger());
          ret.setLedger(led);
         }
         ret.setProcessCode(tt.getProcessCode());
         ret.setRevision(tt.getRevision());
         ret.setShortDescription(tt.getShortDescription());
         return ret;
        }
        
        /**
         * Updates existing Transaction type or creates a new transaction type DB record if no ID
         * @param tt
         * @return 
         */
        private TransactionType buildTransactionType(TransactionTypeRec tt, String pg){
         LOGGER.log(INFO, "buildTransactionType called with {0}", tt);
         boolean newTranTy = false;
         boolean changedTranTy = false;
         TransactionType ret; 
         if(tt.getId() == null ){
          ret = new TransactionType();
          User usrCr = em.find(User.class, tt.getCreatedBy().getId(), OPTIMISTIC);
          ret.setCreatedBy(usrCr);
          ret.setCreatedOn(tt.getCreatedOn());
          em.persist(ret);
          AuditTransactionType aud = buildAuditTransactionType(ret, usrCr, 'I', pg);
          aud.setNewValue(tt.getCode());
          newTranTy = true;
         }else{
          ret = em.find(TransactionType.class, tt.getId(), OPTIMISTIC_FORCE_INCREMENT);
         }
         
         if(newTranTy){
          ret.setCode(tt.getCode());
          ret.setDescription(tt.getDescription());
          if(tt.getLedger() != null){
           Ledger led = em.find(Ledger.class, tt.getLedger().getId(), OPTIMISTIC);
           ret.setLedger(led);
          }
          ret.setProcessCode(tt.getProcessCode());
          ret.setShortDescription(tt.getShortDescription());
         }else{
          // changed ?
          User chUsr = em.find(User.class, tt.getChangedBy().getId(), OPTIMISTIC);
          if((tt.getCode() == null && ret.getCode() != null) ||
             (tt.getCode() != null && ret.getCode() == null) ||
             (tt.getCode() != null && !tt.getCode().equals(ret.getCode()))){
           AuditTransactionType aud = buildAuditTransactionType(ret, chUsr, 'U', pg);
           aud.setFieldName("TRANS_TY_CD");
           aud.setNewValue(tt.getCode());
           aud.setOldValue(ret.getCode());
           ret.setCode(tt.getCode());
           changedTranTy = true;
          }
          
          if((tt.getDescription() == null && ret.getDescription() != null) ||
             (tt.getDescription() != null && ret.getDescription() == null) ||
             (tt.getDescription() != null && !tt.getDescription().equals(ret.getDescription()))){
           AuditTransactionType aud = buildAuditTransactionType(ret, chUsr, 'U', pg);
           aud.setFieldName("TRANS_TY_DESCR");
           aud.setNewValue(tt.getDescription());
           aud.setOldValue(ret.getDescription());
           ret.setDescription(tt.getDescription());
           changedTranTy = true;
          }
          
          if((tt.getProcessCode() == null && ret.getProcessCode() != null) ||
             (tt.getProcessCode() != null && ret.getProcessCode() == null) ||
             (tt.getProcessCode() != null && !tt.getProcessCode().equals(ret.getProcessCode()))){
           AuditTransactionType aud = buildAuditTransactionType(ret, chUsr, 'U', pg);
           aud.setFieldName("TRANS_TY_PR_CD");
           aud.setNewValue(tt.getProcessCode());
           aud.setOldValue(ret.getProcessCode());
           ret.setProcessCode(tt.getProcessCode());
           changedTranTy = true;
          }
          
          if((tt.getShortDescription() == null && ret.getShortDescription() != null) ||
             (tt.getShortDescription() != null && ret.getShortDescription() == null) ||
             (tt.getShortDescription() != null && !tt.getShortDescription().equals(ret.getShortDescription()))){
           AuditTransactionType aud = buildAuditTransactionType(ret, chUsr, 'U', pg);
           aud.setFieldName("TRANS_TY_NM");
           aud.setNewValue(tt.getShortDescription());
           aud.setOldValue(ret.getShortDescription());
           ret.setShortDescription(tt.getShortDescription());
           changedTranTy = true;
          }
          
          if((tt.getLedger() == null && ret.getLedger() != null) ||
             (tt.getLedger() != null && ret.getLedger() == null) ||
             (tt.getLedger() != null && tt.getLedger().getId() != ret.getLedger().getId())){
           AuditTransactionType aud = buildAuditTransactionType(ret, chUsr, 'U', pg);
           aud.setFieldName("TRANS_TY_NM");
           
           aud.setNewValue(tt.getLedger().getCode());
           if(ret.getLedger() !=  null){
           aud.setOldValue(ret.getLedger().getCode());
           }
           Ledger led = em.find(Ledger.class, tt.getLedger().getId(), OPTIMISTIC);
           ret.setLedger(led);
           changedTranTy = true;
          }
          if(changedTranTy){
          User usr = em.find(User.class, tt.getChangedBy().getId(), OPTIMISTIC);
          ret.setChangedBy(usr);
          ret.setChangedOn(tt.getChangedOn());
         }
          
         }
         return ret;
        }
        
        private SortOrderRec buildSortOrderRec(SortOrder sort){
          SortOrderRec sortOrder = new SortOrderRec();
          
          sortOrder.setId(sort.getId());
          UserRec usr = userDM.getUserRecPvt(sort.getCreatedBy());
          sortOrder.setCreatedBy(usr);
          sortOrder.setCreatedOn(sort.getCreatedOn());
          sortOrder.setDescription(sort.getDescription());
          sortOrder.setName(sort.getName());
          sortOrder.setRevision(sort.getRevision());
          sortOrder.setSortCode(sort.getSortCode());
          if(sort.getUpdatedBy() != null){
            usr = userDM.getUserRecPvt(sort.getCreatedBy());
            sortOrder.setUpdatedBy(usr);
            sortOrder.setUpdatedOn(sort.getUpdatedOn());
          }

          return sortOrder;
        }

    private PostTypeRec buildPostTypeRec(PostType pt){
      PostTypeRec ptRec = new PostTypeRec();
      if(pt.getChangedBy() != null){
        UserRec chUsr = userDM.getUserRecPvt(pt.getChangedBy());
        ptRec.setChangedBy(chUsr);
        ptRec.setChangedDate(pt.getChangedDate());
      }
      if(pt.getCreatedBy() !=null){
        UserRec crUsr = userDM.getUserRecPvt(pt.getCreatedBy());
        ptRec.setCreatedBy(crUsr);
        ptRec.setCreateDate(pt.getCreateDate());
      }
      ptRec.setDebit(pt.isDebit());
      ptRec.setDescription(pt.getDescription());
      ptRec.setModuleDescription(pt.getModuleDescription());
      if(pt.getModule() != null){
       ModuleRec m = this.buildModuleRec(pt.getModule());
       ptRec.setModule(m);
      }
      ptRec.setId(pt.getId());
      if(pt.getLedger() != null){
        LedgerRec l = this.buildLedgerRec(pt.getLedger());
        ptRec.setLedger(l);
      }
      ptRec.setPostTypeCode(pt.getPostTypeCode());
      ptRec.setRevision(pt.getRevision());
      ptRec.setSign(pt.getSign());
      ptRec.setSysUse(pt.isSysUse());
      ptRec.setProcCode(pt.getProcCode());
      return ptRec;
    }
    
    
    private ProcessCode buildProcessCode(ProcessCodeRec pr, String pg){
     ProcessCode code;
     boolean newRec = false;
     boolean updated = false;
     if(pr.getId() == null){
      code = new ProcessCode();
      User crUsr = em.find(User.class, pr.getCreatedBy().getId(), OPTIMISTIC);
      code.setCreatedBy(crUsr);
      code.setCreatedDate(pr.getCreatedDate());
      em.persist(code);
      AuditProcessCode aud = new AuditProcessCode();
      aud.setAuditDate(pr.getCreatedDate());
      aud.setNewValue(pr.getName());
      aud.setProcessCode(code);
      aud.setSource(pg);
      aud.setUsrAction('I');
      em.persist(aud);
      newRec = true;
     }else{
      code = em.find(ProcessCode.class, pr.getId(), OPTIMISTIC);
     }
     
     if(newRec){
      code.setName(pr.getName());
      code.setDescription(pr.getDescription());
      Module mod = em.find(Module.class, pr.getModule().getId(), OPTIMISTIC);
      code.setModule(mod);
      
     }else{
      User chUsr = em.find(User.class, pr.getChangedBy().getId(), OPTIMISTIC);
      if(!pr.getName().equalsIgnoreCase(code.getName())){
       AuditProcessCode aud = new AuditProcessCode();
       aud.setAuditDate(pr.getChangedDate());
       aud.setCreatedBy(chUsr);
       aud.setFieldName("PR_CD_NAME");
       aud.setNewValue(pr.getName());
       aud.setOldValue(code.getName());
       aud.setProcessCode(code);
       aud.setSource(pg);
       aud.setUsrAction('U');
       code.setName(pr.getName());
       updated = true;
      }
      if(!pr.getDescription().equalsIgnoreCase(code.getDescription())){
       AuditProcessCode aud = new AuditProcessCode();
       aud.setAuditDate(pr.getChangedDate());
       aud.setCreatedBy(chUsr);
       aud.setFieldName("PR_CD_DESCR");
       aud.setNewValue(pr.getDescription());
       aud.setOldValue(code.getDescription());
       aud.setProcessCode(code);
       aud.setSource(pg);
       aud.setUsrAction('U');
       code.setName(pr.getName());
       updated = true;
      }
      if(pr.getModule().getId() != code.getModule().getId()){
       AuditProcessCode aud = new AuditProcessCode();
       aud.setAuditDate(pr.getChangedDate());
       aud.setCreatedBy(chUsr);
       aud.setFieldName("PR_CD_MOD");
       aud.setNewValue(pr.getModule().getModuleCode());
       aud.setOldValue(code.getModule().getModuleCode());
       aud.setProcessCode(code);
       aud.setSource(pg);
       aud.setUsrAction('U');
       code.setName(pr.getName());
       updated = true;
      }
      if(updated){
       code.setChangedBy(chUsr);
       code.setChangedDate(pr.getChangedDate());
      }
     }
     return code;
    }
    
    private ProcessCodeRec buildProcessCodeRec(ProcessCode prCode){
     ProcessCodeRec codeRec = new ProcessCodeRec();
     UserRec crUsr = this.userDM.getUserRecPvt(prCode.getCreatedBy());
     codeRec.setId(prCode.getId());
     codeRec.setCreatedBy(crUsr);
     if(prCode.getChangedBy() != null){
      UserRec chUsr = this.userDM.getUserRecPvt(prCode.getChangedBy());
      codeRec.setChangedBy(chUsr);
      codeRec.setChangedDate(prCode.getChangedDate());
     }
     codeRec.setCreatedDate(prCode.getCreatedDate());
     codeRec.setName(prCode.getName());
     codeRec.setDescription(prCode.getDescription());
     ModuleRec mod =  buffer.getModuleById(prCode.getModule().getId());
     codeRec.setModule(mod);
      
    
    
      
     
     return codeRec;
    }
    
    private PostType buildPostType(PostTypeRec pt, String pg){
      LOGGER.log(INFO, "buildPostTypeRec called with {0}",pt);
      boolean newPstType = false;
      boolean changedPstType = false;
      PostType ptRec;
      if(pt.getId() == null){
       ptRec = new PostType();
       User crUsr = em.find(User.class, pt.getCreatedBy().getId());
        ptRec.setCreatedBy(crUsr);
        ptRec.setCreateDate(pt.getCreateDate());
        em.persist(ptRec);
        AuditPostType aud = this.buildAuditPostType(ptRec, 'I', crUsr, pg);
        aud.setNewValue(pt.getPostTypeCode());
        pt.setId(ptRec.getId());
        newPstType = true;
      }else{
       ptRec = em.find(PostType.class, pt.getId(), OPTIMISTIC);
      }
      
      if(newPstType){
       ptRec.setDebit(pt.isDebit());
       ptRec.setDescription(pt.getDescription());
       ptRec.setModuleDescription(pt.getModuleDescription());
       if(pt.getModule() != null){
        Module m = em.find(Module.class, pt.getModule().getId(), OPTIMISTIC);
        ptRec.setModule(m);
       }
       if(pt.getLedger() != null){
        Ledger l = em.find(Ledger.class, pt.getLedger().getId(), OPTIMISTIC);
        ptRec.setLedger(l);
       }
       ptRec.setPostTypeCode(pt.getPostTypeCode());
       if(pt.isDebit()){
        ptRec.setSign('+');
       }else{
        ptRec.setSign('-');
       }
       if(pt.getRevPostType() != null && pt.getRevPostType().getId() != null){
        PostType rev = em.find(PostType.class, pt.getRevPostType().getId(), OPTIMISTIC);
        ptRec.setRevPostType(rev);
       }
       ptRec.setSysUse(pt.isSysUse());
       
       
      }else{
       // Changed ?
       User chUsr = em.find(User.class, pt.getChangedBy().getId(), OPTIMISTIC);
       if((pt.getDescription() == null && ptRec.getDescription() != null) ||
          (pt.getDescription() != null && ptRec.getDescription() == null) ||
          (pt.getDescription() != null && !pt.getDescription().equalsIgnoreCase(ptRec.getDescription()))){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_DESCR");
        aud.setNewValue(pt.getDescription());
        aud.setOldValue(ptRec.getDescription());
        ptRec.setDescription(pt.getDescription());
        changedPstType = true;
       }
       if(pt.isDebit() != ptRec.isDebit()){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_DR");
        aud.setNewValue(String.valueOf(pt.isDebit()));
        aud.setOldValue(String.valueOf(ptRec.isDebit()));
        ptRec.setDebit(pt.isDebit());
        changedPstType = true;
       }
       if((pt.getLedger() == null && ptRec.getLedger() != null)||
          (pt.getLedger() != null && ptRec.getLedger() == null) ||
          (pt.getLedger() != null && ptRec.getLedger().getId() != pt.getLedger().getId())){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_LED");
        aud.setNewValue(pt.getLedger().getCode());
        aud.setOldValue(pt.getLedger().getCode());
        Ledger l = em.find(Ledger.class, pt.getLedger().getId(), OPTIMISTIC);
        ptRec.setLedger(l);
        changedPstType = true;
       }
       //if(pt.getModule() == null || pt.getModule().getId() == null){
       // pt.setModule(null);
       //}
       LOGGER.log(INFO, "pt.getModule {0} ptRec.getModule {1}", new Object[]{pt.getModule(),pt.getModule()});
       if((pt.getModule() == null && ptRec.getModule() != null) ||
          (pt.getModule() != null && ptRec.getModule() == null) || 
          (pt.getModule() != null && pt.getModule().getId() != ptRec.getLedger().getId()) ){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_MOD");
        if(pt.getModule() != null){
        aud.setNewValue(pt.getModule().getModuleCode());
        }
        if(ptRec.getModule() != null){
        aud.setOldValue(ptRec.getModule().getModuleCode());
        }
        if(pt.getModule() != null){
        Module m = em.find(Module.class, pt.getModule().getId(), OPTIMISTIC);
        ptRec.setModule(m);
        }
        changedPstType = true;
        
       }
       
       if((pt.getPostTypeCode() == null && ptRec.getPostTypeCode() != null) ||
          (pt.getPostTypeCode() != null && ptRec.getPostTypeCode() == null) ||
          (pt.getPostTypeCode() != null && !pt.getPostTypeCode().equalsIgnoreCase(ptRec.getPostTypeCode()))){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_CODE");
        aud.setNewValue(pt.getPostTypeCode());
        aud.setOldValue(ptRec.getPostTypeCode());
        ptRec.setPostTypeCode(pt.getPostTypeCode());
        changedPstType = true;
       }
       
       if((pt.getRevPostType() == null && ptRec.getRevPostType() != null) ||
          (pt.getRevPostType() != null && ptRec.getRevPostType() == null) ||
          (pt.getRevPostType() != null && pt.getRevPostType().getId() != ptRec.getRevPostType().getId())){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_REV");
        aud.setNewValue(pt.getRevPostType().getPostTypeCode());
        if(ptRec.getRevPostType() != null){
        aud.setOldValue(ptRec.getRevPostType().getPostTypeCode());
        }
        PostType rev = em.find(PostType.class, pt.getRevPostType().getId(), OPTIMISTIC);
        ptRec.setRevPostType(rev);
        changedPstType = true;
       }
       
       if(pt.getSign() != ptRec.getSign()){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_SIGN");
        aud.setNewValue(String.valueOf(pt.getSign()));
        aud.setOldValue(String.valueOf(pt.getSign()));
        ptRec.setSign(pt.getSign());
        changedPstType = true;
       }
       if(pt.isSysUse() != ptRec.isSysUse()){
        AuditPostType aud = this.buildAuditPostType(ptRec, 'U', chUsr, pg);
        aud.setFieldName("PST_TY_SYS");
        aud.setNewValue(String.valueOf(pt.isSysUse()));
        aud.setOldValue(String.valueOf(pt.isSysUse()));
        ptRec.setSysUse(pt.isSysUse());
        changedPstType = true;
       }
       
       if(changedPstType){
        ptRec.setChangedBy(chUsr);
        ptRec.setChangedDate(new Date());
       }
          
      }
      
      
      
      return ptRec;
    }

    private PaymentTermsRec buildPaymentTermsRec(PaymentTerms p){
      PaymentTermsRec rec = new PaymentTermsRec();
      rec.setBaseType(p.getBaseType());
      if(p.getChangedBy() != null){
        UserRec usr = userDM.getUserRecPvt(p.getChangedBy());
        rec.setChangedBy(usr);
        rec.setChangedOn(p.getChangedOn());

      }
      if(p.getCreatedBy() != null){
        UserRec usr = userDM.getUserRecPvt(p.getCreatedBy());
        rec.setCreatedBy(usr);
        rec.setCreatedOn(p.getCreatedOn());
      }
      rec.setDayOfMonth(p.getDayOfMonth());
      rec.setDays(p.getDays());
      rec.setDescription(p.getDescription());
      rec.setId(p.getId());
      rec.setPayTermsCode(p.getPayTermsCode());
      UomRec u = this.buildUomRec(p.getUom());
      rec.setUom(u);
      return rec;
    }
    
    private PaymentTerms buildPaymentTerms(PaymentTermsRec p, String pg){
     LOGGER.log(INFO, "buildPaymentTerms called with terms rec code {0}", p.getPayTermsCode());
     LOGGER.log(INFO, "p.id {0}", p.getId());
     boolean newTerms = false;
     boolean changedTerms = false;
     
     PaymentTerms terms;
     
     if(p.getId() == null){
      terms = new PaymentTerms();
      User crUsr = em.find(User.class, p.getCreatedBy().getId());
      terms.setCreatedBy(crUsr);
      terms.setCreatedOn(p.getCreatedOn());
      em.persist(terms);
      LOGGER.log(INFO, "terms id after persist {0}", terms.getId());
      AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'I', crUsr, pg);
      newTerms = true;
      
     }else{
      terms = em.find(PaymentTerms.class, p.getId());
     }
     
     LOGGER.log(INFO, "newTerms {0}", newTerms);
     if(newTerms){
      terms.setBaseType(p.getBaseType());
      terms.setDayOfMonth(p.getDayOfMonth());
      terms.setDays(p.getDays());
      terms.setDescription(p.getDescription());
      
      terms.setPayTermsCode(p.getPayTermsCode());
      Uom uom = em.find(Uom.class, p.getUom().getId());
      terms.setUom(uom);
     }else{
      // changed ??
      User chUsr = em.find(User.class, p.getChangedBy().getId());
      
      if(!StringUtils.equals(p.getBaseType(), terms.getBaseType())){
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERM_DATE_BASE");
       aud.setNewValue(p.getBaseType());
       aud.setOldValue(terms.getBaseType());
       terms.setBaseType(p.getBaseType());
       changedTerms = true;
      }
      
      if(p.getDayOfMonth() != terms.getDayOfMonth()){
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERM_DOM");
       aud.setNewValue(String.valueOf(p.getDayOfMonth()));
       aud.setOldValue(String.valueOf(p.getDayOfMonth()));
       terms.setDayOfMonth(p.getDayOfMonth());
       changedTerms = true;
      }
      
      if(p.getDays() != terms.getDayOfMonth()){
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERM_DAYS");
       aud.setNewValue(String.valueOf(p.getDays()));
       aud.setOldValue(String.valueOf(p.getDayOfMonth()));
       terms.setDays(p.getDays());
       changedTerms = true;
      }
      
      if(!StringUtils.equals(p.getDescription(), terms.getDescription())){
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERMS_DESCR");
       aud.setNewValue(p.getDescription());
       aud.setOldValue(terms.getDescription());
       terms.setDescription(p.getDescription());
       changedTerms = true;
      }
      
      if(!StringUtils.equals(p.getPayTermsCode(), terms.getPayTermsCode())){
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERMS_CODE");
       aud.setNewValue(p.getPayTermsCode());
       aud.setOldValue(terms.getPayTermsCode());
       terms.setPayTermsCode(p.getPayTermsCode());
       changedTerms = true;
      }
      
      if((p.getUom() == null && terms.getUom() != null ) || 
         (p.getUom() != null && terms.getUom() == null ) ||
         (p.getUom() != null && !Objects.equals(p.getUom().getId(), terms.getUom().getId()) )) {
       AuditPaymentTerms aud = this.buildAuditPaymentTerms(terms, 'U', chUsr, pg);
       aud.setFieldName("PAY_TERMS_UOM");
       aud.setNewValue(p.getUom().getUomCode());
       aud.setOldValue(terms.getUom().getUomCode());
       Uom u = em.find(Uom.class, p.getUom().getId());
       terms.setUom(u);
       changedTerms = true;
      }
      
      if(changedTerms){
       terms.setChangedBy(chUsr);
       terms.setChangedOn(p.getChangedOn());
      }
     }
     
     LOGGER.log(INFO, "buildPaymentTerms returns id {0}", terms.getId());
     return terms;
     
    }


  
  private PaymentType buildPaymentType(PaymentTypeRec payType, String view) {
  LOGGER.log(INFO,"buildPaymentType called with {0}",payType);
  LOGGER.log(INFO,"chganged by {0}",payType.getChangedBy());
  
  //User usrUpdt = em.find(User.class, usr.getId(), OPTIMISTIC);
  boolean updated = false;
  boolean newPayTy = false;
  PaymentType pt;
  if(payType.getId() == null){
   pt = new  PaymentType();
   User crUsr = em.find(User.class, payType.getCreatedBy().getId());
   pt.setCreatedBy(crUsr);
   pt.setCreatedOn(payType.getCreatedOn());
   em.persist(pt);
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'I', crUsr, view);
   aud.setNewValue(payType.getPayTypeCode());
   newPayTy = true;
  }else{
   pt = em.find(PaymentType.class, payType.getId(), OPTIMISTIC);
  }
  
 LOGGER.log(INFO, "pt {0}", pt);
 if(newPayTy){
  if(payType.getBacsTransCode() != null){
   BacsTransCode bacsCode = em.find(BacsTransCode.class, payType.getBacsTransCode().getId());
   pt.setBacsTransCode(bacsCode);
  }
  
  CompanyBasic comp = buffer.getComp(payType.getCompany());
  pt.setCompany(comp);
  pt.setDescription(payType.getDescription());
  pt.setInbound(payType.isInbound());
  Ledger led = em.find(Ledger.class, payType.getLedger().getId());
  pt.setLedger(led);
  LOGGER.log(INFO, "PayMedium test {0}", payType.getPayMedium());
  
  Uom uom = em.find(Uom.class, payType.getMediumUom().getId());
  
  pt.setMediumUom(uom);
  pt.setPayMedium(payType.getPayMedium());
  pt.setPayTypeCode(payType.getPayTypeCode());
  if(payType.isHasCheqTemplate()){
   ChequeTemplate tmpl = em.find(ChequeTemplate.class, payType.getChqTemplate().getId(),OPTIMISTIC);
   pt.setChqTemplate(tmpl);
  }
  pt.setSummLevel(payType.getSummLevel());
 }else{
  User chUsr = em.find(User.class, payType.getChangedBy().getId());
  LOGGER.log(INFO, "Pay rec comp {0} db comp {1}", new Object[]{payType.getCompany(),pt.getCompany()});
  if(!Objects.equals(payType.getCompany().getId(), pt.getCompany().getId())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_COMP");
   aud.setNewValue(payType.getCompany().getReference());
   aud.setOldValue(pt.getCompany().getNumber());
   CompanyBasic comp = buffer.getComp(payType.getCompany());
   pt.setCompany(comp);
   updated = true;
  }
  if(!payType.getDescription().equalsIgnoreCase(pt.getDescription())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_DESCR");
   aud.setNewValue(payType.getDescription());
   aud.setOldValue(pt.getDescription());
   pt.setDescription(payType.getDescription());
   updated = true;
  }
  
  if((payType.getGlBankAccount() == null && pt.getGlAccount() != null ) ||
     (payType.getGlBankAccount() != null && pt.getGlAccount() == null) ||
     (payType.getGlBankAccount() != null && 
       !Objects.equals(payType.getGlBankAccount().getId(), pt.getGlAccount().getId()))){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_GLAC");
   aud.setNewValue(payType.getGlBankAccount().getCoaAccount().getRef());
   LOGGER.log(INFO, "pt.getGlAccount() {0}", pt.getGlAccount());
   if(pt.getGlAccount() == null){
    aud.setOldValue("");
   }else{
    aud.setOldValue(pt.getGlAccount().getCoaAccount().getRef());
   }
   pt.setDescription(payType.getDescription());
   FiGlAccountComp  glAc = em.find(FiGlAccountComp.class, payType.getGlBankAccount().getId());
   pt.setGlAccount(glAc);
   updated = true;
  }
  if(payType.getLedger() != null 
    && Objects.equals(payType.getLedger().getId(), pt.getLedger().getId())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_LED");
   aud.setNewValue(payType.getLedger().getCode());
   aud.setOldValue(pt.getLedger().getCode());
   Ledger led = em.find(Ledger.class, payType.getLedger().getId());
   pt.setLedger(led);
   updated = true;
  }
  if(!payType.getPayMedium().equals(pt.getPayMedium())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_MED");
   aud.setNewValue(payType.getPayMedium());
   aud.setOldValue(pt.getPayMedium());
   pt.setPayMedium(payType.getPayMedium());
   updated = true;
  }
  if(!payType.getPayTypeCode().equals(pt.getPayTypeCode())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_CD");
   aud.setNewValue(payType.getPayTypeCode());
   aud.setOldValue(pt.getPayTypeCode());
   pt.setPayMedium(payType.getPayTypeCode());
   updated = true;
  }
  
  if((payType.getPayTypeForBankAccount() == null && pt.getPayTypeForBankAccount() != null) ||
    (payType.getPayTypeForBankAccount() != null && pt.getPayTypeForBankAccount() == null) ||
    payType.getPayTypeForBankAccount() != null &&
    !Objects.equals(payType.getPayTypeForBankAccount().getId(), pt.getPayTypeForBankAccount().getId())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_BNKAC");
   aud.setNewValue(payType.getPayTypeForBankAccount().getAccountNumber());
   if(pt.getPayTypeForBankAccount() == null){
    aud.setOldValue(" ");
   }else{
    aud.setOldValue(pt.getPayTypeForBankAccount().getAccountNumber());
   }
   
   BankAccountCompany bnk = em.find(BankAccountCompany.class, payType.getPayTypeForBankAccount().getId());
   pt.setPayTypeForBankAccount(bnk);
   updated = true;
  }
  if(payType.isInbound() != pt.isInbound()){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_IN");
   aud.setNewValue(String.valueOf(payType.isInbound()));
   aud.setOldValue(String.valueOf(pt.isInbound()));
   pt.setInbound(payType.isInbound());
   updated = true; 
  }

  if(payType.getBacsTransCode() != null 
    && !Objects.equals(payType.getBacsTransCode().getId(), pt.getBacsTransCode().getId())){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_BACSCD");
   aud.setNewValue(payType.getBacsTransCode().getPtnrBnkTransCode());
   aud.setOldValue(pt.getBacsTransCode().getPtnrBnkTransCode());
   BacsTransCode cd = em.find(BacsTransCode.class, payType.getBacsTransCode().getId());
   pt.setBacsTransCode(cd);
   updated = true; 
  }
  
  if(payType.isHasCheqTemplate() != pt.isHasChqTemplate() ){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view); 
   aud.setFieldName("PAY_TY_HAS_CHQ_TEMPL");
   aud.setNewValue(String.valueOf(payType.isHasCheqTemplate()));
   aud.setNewValue(String.valueOf(pt.isHasChqTemplate()));
   pt.setHasChqTemplate(payType.isHasCheqTemplate());
   updated = true;
  }
  
  if((payType.getChqTemplate() == null && pt.getChqTemplate() != null) ||
    (payType.getChqTemplate() != null && pt.getChqTemplate() == null) ||
    (payType.getChqTemplate() != null && 
    !Objects.equals(payType.getChqTemplate().getId(), pt.getChqTemplate().getId()))){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view);
   aud.setFieldName("PAY_TY_CHQ_TEMPL");
   aud.setNewValue(payType.getChqTemplate().getReference());
   if(pt.getChqTemplate() != null){
    aud.setOldValue(pt.getChqTemplate().getReference());
   }
   ChequeTemplate tmpl = em.find(ChequeTemplate.class, payType.getChqTemplate().getId());
   pt.setChqTemplate(tmpl);
   updated = true;
  }
  
  if(payType.getSummLevel() != pt.getSummLevel()){
   AuditPaymentType aud = this.buildAuditPaymentType(pt, 'U', chUsr, view);
   aud.setFieldName("PAY_TY_SUMM");
   aud.setNewValue(String.valueOf(payType.getSummLevel()));
   aud.setOldValue(String.valueOf(pt.getSummLevel()));
   pt.setSummLevel(payType.getSummLevel());
   updated = true;
  }
  
  if(updated){
   pt.setChangedBy(chUsr);
   pt.setChangedOn(payType.getChangedOn());
  }
 }
 
  return pt;
}


private PaymentTypeRec buildPaymentTypeRec(PaymentType p){
  PaymentTypeRec rec = new PaymentTypeRec();
  if(p.getChangedBy() != null){
    UserRec usr = this.userDM.getUserRecPvt(p.getChangedBy());
    rec.setChangedBy(usr);
    rec.setChangedOn(p.getChangedOn());
  }
  if(p.getCreatedBy() != null){
    UserRec usr = this.userDM.getUserRecPvt(p.getCreatedBy());
    rec.setCreatedBy(usr);
    rec.setCreatedOn(p.getCreatedOn());
  }
 
  if(p.getCompany() != null){
    CompanyBasicRec comp = this.buffer.getCompanyById(p.getCompany().getId());
    rec.setCompany(comp);
  }

  rec.setDescription(p.getDescription());
  
  rec.setId(p.getId());
  if(p.getLedger() != null){
    LedgerRec l = this.buildLedgerRec(p.getLedger());
    rec.setLedger(l);
  }
  if(p.getMediumUom() != null){
    UomRec u = this.buildUomRec(p.getMediumUom());
    rec.setMediumUom(u);
  }
  rec.setPayMedium(p.getPayMedium());
  rec.setPayTypeCode(p.getPayTypeCode());
  if(p.getPayTypeForBankAccount() != null){
    
    BankAccountCompanyRec bnkActRec = 
      buffer.getCompBankAcntById(p.getPayTypeForBankAccount().getId(), rec.getCompany());
    rec.setPayTypeForBankAccount(bnkActRec);
  }
  
  if(p.getGlAccount() != null){
   FiGlAccountCompRec glBankAc = this.fiMastDM.buildFiCompGlAccountRecPvt(p.getGlAccount());
   rec.setGlBankAccount(glBankAc);
  }
  
  if(p.getBacsTransCode() != null){
   BacsTransCodeRec bacsCode = this.treasuryDM.buildBacsTransCodeRecPvt(p.getBacsTransCode());
   rec.setBacsTransCode(bacsCode);
  }
  rec.setSummLevel(p.getSummLevel());
  rec.setInbound(p.isInbound());
  

  return rec;
}

public ProcessCodeRec addProcessCode(ProcessCodeRec pr, String pg){
 LOGGER.log(INFO, "addProcessCode called with pr {0}", pr);
 if(!trans.isActive()){
  trans.begin();
 }
 ProcessCode code = this.buildProcessCode(pr, pg);
 pr.setId(code.getId());
 trans.commit();
 return pr;
}

    public ModuleRec createModule(ModuleRec mod, UserRec usr, String page) throws BacException {
        LOGGER.log(INFO, "Data Manager createModule called with: {0}", mod);
        Date updateDate = new Date();
        try{
         if(!trans.isActive()){
          trans.begin();
         }
         mod.setCreatedDate(updateDate);
         mod.setCreatedBy(usr);
         LOGGER.log(INFO, "mod created by {0}", mod.getCreatedBy());
         Module m = buildModule(mod);
         LOGGER.log(INFO, "DB entity id: {0}", "id: "+m.getId());
         mod.setId(m.getId());
         AuditModule aud = new AuditModule();
         aud.setAuditDate(updateDate);
         aud.setCreatedBy(em.find(User.class, usr.getId(), OPTIMISTIC));
         aud.setModule(m);
         aud.setNewValue(mod.getModuleCode());
         aud.setSource(page);
         aud.setUsrAction('I');
         em.persist(aud);
         trans.commit();
        }catch (EntityExistsException e){
            LOGGER.log(INFO, "Module already exists :", e.getMessage());
            throw new BacException("Module already exists :"+ e.getMessage());

        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Module data type invalid :", e.getMessage());
            throw new BacException("Module data type invalid :"+ e.getMessage());
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Module data type invalid :", e.getMessage());
            throw new BacException("Module already exists :"+ e.getMessage());
        }catch(PersistenceException e){
            throw new BacException("Module already exists :"+ e.getMessage());
        }


        return mod;
    }

    @SuppressWarnings("finally")
    public ArrayList<ModuleRec> getAllModules() throws BacException{
        LOGGER.log(INFO, "getAllModules called");
        ArrayList<ModuleRec> modLst = new ArrayList<>();
        try{
         
            Query modLstQ = em.createNamedQuery("allModules");
            List l =   modLstQ.getResultList();
            ListIterator lIT = l.listIterator();
            while(lIT.hasNext()){
                Module mod = (Module)lIT.next();
                ModuleRec modRec = this.buildModuleRec(mod);
                modLst.add(modRec);
            }
            LOGGER.log(INFO, "Number of modules returned from DB", modLst.size());
        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Criteria not defined: {0}", e.getMessage());
        }catch(IllegalStateException e){
            LOGGER.log(INFO, "Query type error: {0}", e.getMessage());
        } catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Query timeout: {0}", e.getMessage());
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction missing: {0}", e.getMessage());
        }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Locking error transaction rolled back: {0}", e.getMessage());
        }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Locking error statement rolled back: {0}", e.getMessage());
        }catch(PersistenceException e){
            LOGGER.log(INFO, "Other database error: {0}", e.getMessage());
        } finally{
            return  modLst;
        }



    }



    public ArrayList<ModuleRec> getModulesByCriteria(ModuleRec criteria) {
        LOGGER.log(INFO, "getModulesByCriteria called with: ", criteria);
        ArrayList<ModuleRec> modLst = new ArrayList<>();
        try{
        Query modLstQ = em.createNamedQuery("getModulesByCriteria");
        modLstQ.setParameter("name", criteria.getName());
        modLstQ.setParameter("descr", criteria.getDescription());
        modLstQ.setParameter("modCode", criteria.getModuleCode());
        LOGGER.log(INFO, "lan code paramter: {0}", modLstQ.getParameterValue("langCode"));
        LOGGER.log(INFO, "name paramter: {0}", modLstQ.getParameterValue("name"));
        LOGGER.log(INFO, "descr paramter: {0}", modLstQ.getParameterValue("descr"));
        LOGGER.log(INFO, "modCode paramter: {0}", modLstQ.getParameterValue("modCode"));
        LOGGER.log(INFO, "lan code paramter: {0}", modLstQ.getParameterValue("langCode"));
        List l =   modLstQ.getResultList();
        LOGGER.log(INFO, "get result list returned {0} records", l.size());
        ListIterator lIT = l.listIterator();
        while(lIT.hasNext()){
            Module mod = (Module)lIT.next();
            ModuleRec modRec = this.buildModuleRec(mod);
            modLst.add(modRec);
        }
        LOGGER.log(INFO, "Number of modules returned from DB", modLst.size());
        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Criteria not defined: {0}", e.getMessage());
        }catch(IllegalStateException e){
            LOGGER.log(INFO, "Query type error: {0}", e.getMessage());
        } catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Query timeout: {0}", e.getMessage());
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction missing: {0}", e.getMessage());
        }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Locking error transaction rolled back: {0}", e.getMessage());
        }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Locking error statement rolled back: {0}", e.getMessage());
        }catch(PersistenceException e){
            LOGGER.log(INFO, "Other database error: {0}", e.getMessage());
        } finally{
            return  modLst;
        }

    }

    public LedgerRec updateLedger(LedgerRec ledger , String pg) throws BacException {
     LOGGER.log(FINEST, "updateLedger called with {0}", ledger.getCode());
     boolean newLed = false;
     boolean changedLed = false;
     Ledger l;
     trans.begin();
     if(ledger.getId() == null){
      l = new Ledger();
      User crUsr = em.find(User.class, ledger.getCreatedBy().getId(), OPTIMISTIC);
      l.setCreatedBy(crUsr);
      l.setCreatedDate(ledger.getCreatedDate());
      em.persist(l);
      AuditLedger aud = this.buildAuditLedger(l, 'I', crUsr, pg);
      aud.setNewValue(ledger.getCode());
      newLed = true;
      ledger.setId(l.getId());
      
     }else{
         l = em.find(Ledger.class,ledger.getId());
     }
     
     if(newLed){
      l.setCode(ledger.getCode());
      l.setDescr(ledger.getDescr());
      l.setName(ledger.getName());
     }else{
      // changed ??
      User chUsr = em.find(User.class, ledger.getChangedBy().getId(), OPTIMISTIC);
      
      if((ledger.getCode() == null && l.getCode() != null) ||
         (ledger.getCode() != null && l.getCode() == null) ||
         (ledger.getCode() != null && !ledger.getCode().equals(l.getCode()))){
       AuditLedger aud = this.buildAuditLedger(l, 'I', chUsr, pg);
       aud.setFieldName("LED_NAME");
       aud.setNewValue(ledger.getCode());
       aud.setOldValue(l.getCode());
       l.setCode(ledger.getCode());
       changedLed = true;
      }
      
      if((ledger.getDescr() == null && l.getDescr() != null) ||
         (ledger.getDescr() != null && l.getDescr() == null) ||
         (ledger.getDescr() != null && !ledger.getDescr().equals(l.getDescr()))){
       AuditLedger aud = this.buildAuditLedger(l, 'I', chUsr, pg);
       aud.setFieldName("LED_DESCR");
       aud.setNewValue(ledger.getDescr());
       aud.setOldValue(l.getDescr());
       l.setDescr(ledger.getDescr());
       changedLed = true;
      }
      
      if((ledger.getName() == null && l.getName() != null) ||
         (ledger.getName() != null && l.getName() == null) ||
         (ledger.getName() != null && !ledger.getName().equals(l.getName()))){
       AuditLedger aud = this.buildAuditLedger(l, 'I', chUsr, pg);
       aud.setFieldName("LED_NM");
       aud.setNewValue(ledger.getName());
       aud.setOldValue(l.getName());
       l.setName(ledger.getName());
       changedLed = true;
      }
      
      if(changedLed){
       l.setChangedBy(chUsr);
       l.setChangedDate(ledger.getChangedDate());
      }
      
     }
     this.buffer.updateLeder(ledger);
     trans.commit();
     return ledger;
      
     

    }
    
    public LineTypeRuleRec updateLineType(LineTypeRuleRec lineType, String pg) throws BacException {
     LOGGER.log(INFO, "Called DM.updateLineType with {0}",lineType);
     if(!trans.isActive()){
      trans.begin();
     }
     LineTypeRule l = this.buildLineTypeRule(lineType, pg);
     lineType.setId(l.getId());
     trans.commit();
     return lineType;
    }
    
    public LocaleCodeRec updateLocaleCode(LocaleCodeRec loc, String pg){
     LOGGER.log(INFO, "Called DM.updateLocaleCode with {0}", loc);
     if(!trans.isActive()){
      trans.begin();
     }
     LocaleCode cd = this.buildLocaleCode(loc, pg);
     if(loc.getId() == null){
      loc.setId(cd.getId());
     }
     
     trans.commit();
     return loc;
    }
    public void updateModule(ModuleRec mod, String source) throws BacException {
        LOGGER.log(INFO, "updateModule with {0}", mod.getId());
        boolean changed = false;
        trans.begin();
        Long id = mod.getId();
        User chUsr = em.find(User.class, mod.getChangedBy().getId(), OPTIMISTIC);
        Module rec = (Module)em.find(Module.class, id);
        //Date updated= new Date();
        if(!rec.getDescription().equalsIgnoreCase(mod.getDescription())){
         changed = true;
         AuditModule aud = new AuditModule();
         aud.setAuditDate(new Date());
         aud.setCreatedBy(chUsr);
         aud.setModule(rec);
         aud.setFieldName("MOD_DESCR");
         aud.setNewValue(mod.getDescription());
         aud.setOldValue(rec.getDescription());
         aud.setSource(source);
         aud.setUsrAction('U');
         em.persist(aud);
         rec.setDescription(mod.getDescription());
        }
        if(!rec.getName().equalsIgnoreCase(mod.getName())){
         changed = true;
         AuditModule aud = new AuditModule();
         aud.setAuditDate(new Date());
         aud.setCreatedBy(chUsr);
         aud.setModule(rec);
         aud.setFieldName("MOD_NAME");
         aud.setNewValue(mod.getName());
         aud.setOldValue(rec.getName());
         aud.setSource(source);
         aud.setUsrAction('U');
         em.persist(aud);
         rec.setName(mod.getName());
        }
        if(!rec.getModuleCode().equalsIgnoreCase(mod.getModuleCode())){
         changed = true;
         AuditModule aud = new AuditModule();
         aud.setAuditDate(new Date());
         aud.setCreatedBy(chUsr);
         aud.setModule(rec);
         aud.setFieldName("MOD_CODE");
         aud.setNewValue(mod.getModuleCode());
         aud.setOldValue(rec.getModuleCode());
         aud.setSource(source);
         aud.setUsrAction('U');
         em.persist(aud);
         rec.setModuleCode(mod.getModuleCode());
        }
        
        if(changed){
         rec.setChangedDate(mod.getChangedDate()); 
         rec.setChangedBy(chUsr);
         trans.commit();
        }
        
        
        
        
        
    }


/**
 * Deletes the given module from the database
 * @param module
 * @throws BacException
 */
    public void deleteModule(ModuleRec module, String src) throws BacException {
        LOGGER.log(INFO, "Data manager deleteModule called with {0} ", module);
        LOGGER.log(INFO, "module id {0} ", module.getId());
        if(module == null || module.getId() < 1 ){
            throw new BacException("No module to delete received");
        }
        try{
         LOGGER.log(INFO, "About to find module with id {0}", module.getId());
         trans.begin();
          Module rec =em.find(Module.class, module.getId());
          if(rec == null){
           LOGGER.log(INFO,"Could not find Module with id {0}",module.getId());
           throw new BacException("Module not found");
          }
          List<AuditModule> auditRecs = rec.getAuditRecords();
          if(auditRecs != null && !auditRecs.isEmpty()){
          for(AuditModule delAud: auditRecs){
           LOGGER.log(INFO, "Delete audit rec {0}", delAud);
           em.remove(delAud);
          }
          }
          em.remove(rec);
          
          AuditModule aud = new AuditModule();
          aud.setAuditDate(new Date());
          aud.setNewValue(module.getName());
          aud.setSource(src);
          aud.setUsrAction('D');
          em.persist(aud);
        trans.commit();
        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Object type incorrect {0}", e.getLocalizedMessage());
            throw new BacException("No module to delete received "+e.getLocalizedMessage());

        }catch(TransactionRequiredException e){
            LOGGER.log(INFO,"Database transaction error {0}",e.getLocalizedMessage());
            throw new BacException("No module to delete received "+e.getLocalizedMessage());
        }


    }

    public void createAccountType(AccountTypeRec rec){
     LOGGER.log(INFO, "ConfigDM.createAccountType called with act type {0}", rec);
     
    }
    public LineTypeRuleRec createLineType(LineTypeRuleRec lineType, String pg) throws BacException {
     LOGGER.log(INFO, "Called DM.LineTypeRuleRec with {0}",lineType);
     if(!trans.isActive()){
      trans.begin();
     }
     LineTypeRule l = this.buildLineTypeRule(lineType, pg);
     lineType.setId(l.getId());
     trans.commit();
     return lineType;
    }

 public List<NumberRangeChequeRec> getChequekBksForBnkAcnt(BankAccountCompanyRec bnkAcnt ){
  
  
  List<NumberRangeChequeRec> retList = new ArrayList<>();
  
  Query q = em.createNamedQuery("chqBk4BnkAcnt");
  q.setParameter("bnkActId", bnkAcnt.getId());
  
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  for(Object o:rs){
   NumberRangeChequeRec currRec = this.buildNumberRangeChequeRec((NumberRangeCheque)o);
   retList.add(currRec);
  }
  return retList;
 }
 
 public List<ChequeVoidReasonRec> getChequeVoidReasonRecsAll(){
  
  TypedQuery q = em.createNamedQuery("chqVoidRsnAll", ChequeVoidReason.class);
  List<ChequeVoidReason> rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   LOGGER.log(INFO, "No cheque voids reasons found");
   return null;
  }
  
  List<ChequeVoidReasonRec> retList = new ArrayList<>();
  for(ChequeVoidReason rsn: rs){
   retList.add(this.buildChequeVoidReasonRec(rsn));
  }
  return retList;
 }
    public List<UomRec> getAllUoms() throws BacException {
        LOGGER.log(INFO, "DB get all UOMS");
        List<UomRec> uoms = new ArrayList<>();
        Query q = em.createNamedQuery("UomAll");
        List uomList = q.getResultList();
        ListIterator it = uomList.listIterator();
        while(it.hasNext()){
            Uom rec = (Uom)it.next();
            UomRec u = this.buildUomRec(rec);
            uoms.add(u);


        }
        LOGGER.log(INFO, "Number of UOMS passed back from DB: {0}", uoms.size());
        return uoms;
    }

    /**
     * Creates a new Unit of measure in the database.
     * @param rec Unit of measure Business object
     * @throws BacException
     */
    public void createUom(UomRec rec, String pg) throws BacException {
     LOGGER.log(INFO, "Create UOM in DB");
     try{
      trans.begin();
      Uom u = this.buildUom(rec,pg);
      if(rec.getId() == null){
      AuditUom aud = new AuditUom();
      aud.setAuditDate(new Date());
      aud.setCreatedBy(u.getCreatedBy());
      aud.setNewValue(rec.getUomCode());
      aud.setUom(u);
      aud.setSource(pg);
      aud.setUsrAction('I');
      em.persist(aud);
      }
      trans.commit();
     }catch(EntityExistsException | IllegalArgumentException | TransactionRequiredException e){
      LOGGER.log(INFO, "Duplicate entity: {0}",e.getMessage());
      throw new BacException(e.getMessage());

     }
                 
    }

    public void createNumberControl(NumberRangeRec rec, String src) throws BacException {

     NumberRange r = this.buildNumberRange(rec, src);
     User crUsr = r.getCreatedBy();
     LOGGER.log(INFO, "Number control to save is {0}", r);
     try{
      if(!trans.isActive()){
      trans.begin();
      }
      em.persist(r);
      AuditNumberControl aud = new AuditNumberControl();
      aud.setAuditDate(new Date());
      aud.setCreatedBy(crUsr);
      aud.setNewValue(r.getShortDescr());
      aud.setNumControl(r);
      aud.setSource(src);
      aud.setUsrAction('I');
      em.persist(aud);
      LOGGER.log(INFO, "After save num control: {0}", r);
      trans.commit();
     }catch(EntityExistsException | IllegalArgumentException | TransactionRequiredException e){
      LOGGER.log(INFO, "Number control Duplicate entity: {0}",e.getMessage());
       throw new BacException("Number control "+e.getMessage());
     }
    }

    public LocaleCodeRec getLocaleCodeByCd(String code){
     
     Query q = em.createNamedQuery("ocaleByCode");
     q.setParameter("code", code);
     LocaleCodeRec loc = (LocaleCodeRec)q.getSingleResult();
     return loc;
    }
    public ModuleRec getModuleById(Long id) throws BacException {
        LOGGER.log(INFO, "DB get module by ID called with", id);
        ModuleRec rec;
        try{
            Module m = em.find(Module.class, id);
            if(m == null) {
                throw new BacException("Could not find module");
            }
            rec = this.buildModuleRec(m);
            return rec;

        }catch(IllegalArgumentException e){
            throw new BacException("System error Module type incorrect");
        }

    }

    public synchronized int  getNextNum4NumRangeId(Long numRngId){
     LOGGER.log(INFO, "getNextNum4NumRange called with curr num {0}", numRngId);
     NumberRange nr = em.find(NumberRange.class, numRngId);
     int retInt = nr.getNextNum();
     int newNext = retInt++;
     nr.setNextNum(newNext);
     em.flush();
     return retInt;
    }
    
    public NumberRangeRec getNumCntrById(Long id){
     LOGGER.log(INFO, "DB getNumCntrById call with id {0}", id);
     NumberRangeRec rec;
     trans.begin();
     NumberRange cntrl = em.find(NumberRange.class, id);
     rec = this.buildNumberControlRec(cntrl);
     trans.commit();
     LOGGER.log(INFO, "DB getNumCntrById return with long descr {0}",rec.getLongDescr());
     return rec;
    }
  
    public NumberRangeRec getNumCntrBySrtDescr(String sDescr){
     LOGGER.log(INFO, "DB getgetNumCntrBySrtDescr called with {0}", sDescr);
     Query q = em.createNamedQuery("numberControlBySrtDescr");
     q.setParameter("sDescr", sDescr);
     try{
     NumberRange nc = (NumberRange)q.getSingleResult();
     NumberRangeRec rec = this.buildNumberControlRec(nc);
     return rec;
     }catch(NoResultException ex){
      return null;
     }
     
    }
  public ArrayList<NumberRangeRec> getNumCntrlAll() throws BacException {
     LOGGER.log(INFO, "ConfigDM getNumCntrlAll called");
     ArrayList<NumberRangeRec> retList = new ArrayList<>();
     try{
      if(!trans.isActive()){
      trans.begin();
      }
      Query q = em.createNamedQuery("getNumberControlAll");
      List lst = q.getResultList();
      if(lst.isEmpty()){
       return retList;
      }
      LOGGER.log(INFO, "Query returned {0}", lst.size());
      ListIterator li = lst.listIterator();
      while(li.hasNext()){
       NumberRange num = (NumberRange)li.next();
       NumberRangeRec rec = this.buildNumberControlRec(num);
      retList.add(rec);
     }
     LOGGER.log(INFO, "Number ranges returned by datamanager {0}", retList.size());
     trans.commit();
     return retList;
     }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Criteria not defined: {0}", e.getMessage());
     }catch(IllegalStateException e){
            LOGGER.log(INFO, "Query type error: {0}", e.getMessage());
     } catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Query timeout: {0}", e.getMessage());
     }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction missing: {0}", e.getMessage());
     }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Locking error transaction rolled back: {0}", e.getMessage());
     }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Locking error statement rolled back: {0}", e.getMessage());
     }catch(PersistenceException e){
            LOGGER.log(INFO, "Other database error: {0}", e.getMessage());
   }
   return null;
  }
/**
 * Actually saves the Account type to DB
 * @param accountType
 * @throws BacException
 */
    public AccountTypeRec saveAccountType(AccountTypeRec accountType, String pg) throws BacException {
        LOGGER.log(INFO,"ConfigurationDM.saveGlActType  called with {0} number range id {1}",
                new Object[]{accountType,accountType.getNumberRange().getNumberControlId()});
       if(!trans.isActive()){
        trans.begin();
       }
       AccountType actTy =this.buildAccountType(accountType, pg);
       accountType.setId(actTy.getId());
       trans.commit();
       return accountType;
       
    }


    public List<LineTypeRuleRec> getLineTypesAll(){
     List<LineTypeRuleRec> retList = new ArrayList<>();
     Query q = em.createNamedQuery("lineTypesAll");
     List rl = q.getResultList();
     ListIterator li = rl.listIterator();
     while(li.hasNext()){
      LineTypeRule lineType = (LineTypeRule)li.next();
      LineTypeRuleRec rec = this.buildLineTypeRuleRec(lineType);
      retList.add(rec);
     }
     return retList;
    }
    
    public LineTypeRuleRec getLineTypeByCode(String code){
     LineTypeRuleRec rec ;
     
     Query q = em.createNamedQuery("lineTypeByDescr");
     q.setParameter("lineCode", code);
     try{
     LineTypeRule ln = (LineTypeRule)q.getSingleResult();
     rec = this.buildLineTypeRuleRec(ln);
     return rec;
     }catch(NoResultException ex){
      return null;
     }
     
    }
    public List<FundCategoryRec> getFundCategoriesAll(){
     List<FundCategoryRec> categories = new ArrayList<FundCategoryRec>();
     Query q = em.createNamedQuery("fndCatAll");
     List rs = q.getResultList();
     ListIterator li = rs.listIterator();
     while(li.hasNext()){
      FundCategory cat = (FundCategory)li.next();
      FundCategoryRec catRec = this.buildFundCategory(cat);
      categories.add(catRec);
     }
     return categories;
    }
    
    public FundCategoryRec getFundCategoryById(long fndCatId){
     if(!trans.isActive()){
      trans.begin();
     }
     FundCategory fndCat = em.find(FundCategory.class, fndCatId, OPTIMISTIC);
     FundCategoryRec fndCatRec = this.buildFundCategory(fndCat);
     return fndCatRec;
    }
    
    public FundCategoryRec getFundCategoryByRef(String ref){
     Query q = em.createNamedQuery("fndCatByRef");
     q.setParameter("ref", ref);
     try{
      FundCategory fndCat = (FundCategory)q.getSingleResult();
      FundCategoryRec fndCatRec = this.buildFundCategory(fndCat);
      return fndCatRec;
     }catch(NonUniqueResultException ex){
      LOGGER.log(INFO, "Multiple fun categories {0}", ex.getLocalizedMessage());
      return null;
     } catch(NoResultException ex){
      LOGGER.log(INFO, "No fund type found for {0}", ref);
      return null;
     }
     
     
    
    }
    public List<LedgerRec> getLedgersAll() throws BacException{
      LOGGER.log(FINEST, "ConfigDM.getLedgersAll Called  ");
      ArrayList<LedgerRec> ledgers = new ArrayList<>();
      try{
       
        Query q = em.createNamedQuery("getAllLedgers");
       
        List rs = q.getResultList();

        Ledger l;
        LedgerRec lRec;
        ListIterator it = rs.listIterator();
        while(it.hasNext()){
          l = (Ledger)it.next();
          lRec = this.buildLedgerRec(l);
          ledgers.add(lRec);
        }


      }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Account type Criteria not defined: {0}", e.getMessage());
            throw new BacException("Ledger not defined","CLDGR:01");
        }catch(IllegalStateException e){
            LOGGER.log(INFO, "Query type error: {0}", e.getMessage());
            throw new BacException("Ledger Query type error","CLDGR:02");
        } catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Query timeout: {0}", e.getMessage());
            throw new BacException("Ledger Query timeout","CLDGR:02");
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction missing: {0}", e.getMessage());
            throw new BacException("Ledger Transaction missing","CLDGR:03");
        }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Locking error transaction rolled back: {0}", e.getMessage());
            throw new BacException("Ledger  Locking error ","CLDGR:04");
        }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Locking error statement rolled back: {0}", e.getMessage());
            throw new BacException("Ledger Locking error","CLDGR:05");
        }catch(PersistenceException e){
            LOGGER.log(INFO, "Other database error: {0}", e.getMessage());
            throw new BacException("Ledger Other database error","CLDGR:06");
        }
      LOGGER.log(INFO, "Ledgers to return  {0}", ledgers);
      return ledgers;
    }
    
    public List<LedgerRec> getLedgersByCriteria(String name, String description, String code){
     LOGGER.log(FINEST, "getLedgersByCriteria called with name: {0}");
        List<LedgerRec> ledgers = new ArrayList<>();
        try{
            Query ledgerQ = em.createNamedQuery("getLedgersByCriteria");
            if(!name.isEmpty()){
                ledgerQ.setParameter("name", name);
            }
            if(!description.isEmpty()){
                ledgerQ.setParameter("descr",description);
            }
            if(!code.isEmpty()){
                ledgerQ.setParameter("cd", code );
            }
            LOGGER.log(FINEST, "About to call get ledger resultslist with query: {0}",ledgerQ);
           List ldgrList = ledgerQ.getResultList();
           LOGGER.log(FINEST, "num ledgers returned from DB {0}", ldgrList.size());
           ListIterator ldgrIT = ldgrList.listIterator();
           
           while(ldgrIT.hasNext()){
               LOGGER.log(FINEST, "number of ledgers at start of loop: {0}",ledgers.size());
               Ledger ldgr = (Ledger)ldgrIT.next();
               LedgerRec rec = this.buildLedgerRec(ldgr);
               ledgers.add(rec);
               LOGGER.log(FINEST, "number of ledgers is now: {0}", ledgers.size());

           }
           

        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Get ledgers query error: {0}", e.getMessage());
        }catch(IllegalStateException e){
            LOGGER.log(INFO, "Get ledgers query type error: {0}", e.getMessage());
        }catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Get ledgers time out: {0}", e.getMessage());
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Get ledgers transaction missing: {0}", e.getMessage());
        }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Get ledgers locking failed trans rolledback: {0}", e.getMessage());
        }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Get ledgers locking failed statement rolledback: {0}", e.getMessage());
        }catch(PersistenceException e){
            LOGGER.log(INFO, "Get ledgers other DB error: {0}", e.getMessage());
        }

        LOGGER.log(FINEST, "number of ledgers is now: {0}", ledgers.size());
        return ledgers;
        
    }
    public LedgerRec saveledger(LedgerRec rec, String pg) throws BacException {
     LOGGER.log(INFO, "ConfigDM.saveledge Called with {0}",rec);
     Ledger l = this.buildLedger(rec, pg);
     
     rec.setId(l.getId());
     return rec;
      

    }
/**
 * Actual retrieves the account list from DB
 * @return ArrayList of GL
 * @throws BacException/**
 * Actual retrieves the account list from DB
 * @return ArrayList of GL
 * @throws BacException
 */

  public List<AccountTypeRec> getAccountTypes() throws BacException {

    LOGGER.log(INFO, "DM getGlAccountTypes called");
    try{
      List<AccountTypeRec> actTypes = new ArrayList<>();
      Query q = em.createNamedQuery("allGlActTypes");
      List rl = q.getResultList();
      LOGGER.log(INFO, "Number of account types found {0}", rl.size());
      ListIterator<AccountType> li = rl.listIterator();
      while(li.hasNext()){
        AccountType r = li.next();
        AccountTypeRec rec = this.buildAccountTypeRec(r);
        actTypes.add(rec);

      }
      LOGGER.log(INFO, "Num Account types returned {0}", actTypes.size());
      return actTypes;


    }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Account type Criteria not defined: {0}", e.getMessage());
            throw new BacException("Account type Criteria not defined");
        }catch(IllegalStateException e){
            LOGGER.log(INFO, "Query type error: {0}", e.getMessage());
            throw new BacException("Account type Query type error");
        } catch(QueryTimeoutException e){
            LOGGER.log(INFO, "Query timeout: {0}", e.getMessage());
            throw new BacException("Account type Query timeout");
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction missing: {0}", e.getMessage());
            throw new BacException("Account type Transaction missing");
        }catch(PessimisticLockException e){
            LOGGER.log(INFO, "Locking error transaction rolled back: {0}", e.getMessage());
            throw new BacException("Account type  Locking error ");
        }catch(LockTimeoutException e){
            LOGGER.log(INFO, "Locking error statement rolled back: {0}", e.getMessage());
            throw new BacException("Account type Locking error");
        }catch(PersistenceException e){
            LOGGER.log(INFO, "Other database error: {0}", e.getMessage());
            throw new BacException("Account type Other database error");
        }
    
  }

  public ArrayList<SortOrderRec> getSortOrders() throws BacException {
    LOGGER.log(INFO, "Confi DM getSortOrders() called");
    ArrayList<SortOrderRec> soList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("findAllSortOrders");
      List lst = q.getResultList();
      ListIterator li = lst.listIterator();
      while(li.hasNext()){
        SortOrder s = (SortOrder)li.next();
        SortOrderRec rec = this.buildSortOrderRec(s);
        soList.add(rec);
      }
      return soList;

    }catch(IllegalStateException ex){
      throw new BacException("Get sort oder query type error","SO:04");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get sort oder query query timeout error","SO:05");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get sort oder query transaction error","SO:06");
    }catch(PessimisticLockException ex){
      throw new BacException("Get sort oder query get lock error","SO:06");
    }catch(LockTimeoutException ex){
      throw new BacException("Get sort oder query get lock timeout error","SO:07");
    }catch(PersistenceException ex){
      throw new BacException("Get sort oder query get other DB error","SO:08");
    }


   
  }

  /**
   * Stores a new sort order
   * @param sortOrder
   * @throws BacException
   */
  public SortOrderRec addSortOrder(SortOrderRec sortOrder, String pg) throws BacException {
    LOGGER.log(INFO,"Config DM addSortOrder called with {0}", sortOrder);
    if(!trans.isActive()){
     trans.begin();
    }
    SortOrder so = this.buildSortOrder(sortOrder,pg);
    sortOrder.setId(so.getId());
    trans.commit();
    //trans.rollback();
    return sortOrder;

  }
  
  
  
  
  public List<ProcessCodeRec> getProcessCodesAll(){
   List<ProcessCodeRec> retList = new ArrayList<>();
   /*if(!trans.isActive()){
    trans.begin();
   }*/
   Query q = em.createNamedQuery("processCodesAll");
   List rs = q.getResultList();
   ListIterator rsLi = rs.listIterator();
   while(rsLi.hasNext()){
    ProcessCode pCode = (ProcessCode)rsLi.next();
    ProcessCodeRec rec = this.buildProcessCodeRec(pCode);
    retList.add(rec);
   }
   return retList;
  }
  
  public ProcessCodeRec getProcessCodesByName(String name){
   LOGGER.log(INFO, "ConfigDM.getProcessCodesByName with {0}", name);
   
   TypedQuery q = em.createNamedQuery("processCodeByName", ProcessCode.class);
   q.setParameter("name", name);
   q.setMaxResults(1);
   List<ProcessCode> rs = q.getResultList();
   LOGGER.log(INFO, "rs size {0}", rs.isEmpty());
   if(!rs.isEmpty()){
    for(ProcessCode p:rs){
     return this.buildProcessCodeRec(p);
     
    }
   }
   return null;
  }
/**
 * Retrieves the post types from the data base
 * @return
 * @throws BacException
 */
  public ArrayList<PostTypeRec> getPostTypesDB() throws BacException {
    
    ArrayList<PostTypeRec> postTypeList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("findAllPostTypes");
      List rs = q.getResultList();
      if(rs.isEmpty()){
        LOGGER.log(INFO, "No posting types in DB");
        return postTypeList;
      //  throw new BacException("No Posting types found","PTY:07");
      }
      ListIterator it = rs.listIterator();
      PostType pType;
      PostTypeRec pTypeRec;
      while(it.hasNext()){
        pType = (PostType)it.next();
        pTypeRec = this.buildPostTypeRec(pType);
        if(pType.getRevPostType() != null){
         PostTypeRec revPtRec = buildPostTypeRec(pType.getRevPostType());
         pTypeRec.setRevPostType(revPtRec);
        }
        postTypeList.add(pTypeRec);
      }
    }catch(IllegalStateException ex){
      throw new BacException("Get Post Type query type error","PTY:01");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get Post Type query query timeout error","PTY:02");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get Post Type query transaction error","PTY:03");
    }catch(PessimisticLockException ex){
      throw new BacException("Get Post Type query get lock error","PTY:04");
    }catch(LockTimeoutException ex){
      throw new BacException("Get Post Type query get lock timeout error","PTY:05");
    }catch(PersistenceException ex){
      throw new BacException("Get Post Type query get other DB error","PTY:06");
    }

    return postTypeList;
  }
  
  
/**
 * Creates a posting type in the DB
 * @param postingType Posting type to be created
 */
  public PostTypeRec createPostType(PostTypeRec postingType, String pg) {
    LOGGER.log(INFO, "createPostType called with {0}", postingType);
    if(!trans.isActive()){
     trans.begin();
    }
    PostType pt = buildPostType(postingType,pg);
    postingType.setId(pt.getId());
    trans.commit();
    return postingType;
  }

  public ArrayList<PaymentTermsRec> getAllPaymentTerms() throws BacException {
    LOGGER.log(INFO, "DB getAllPaymentTerms called");
    ArrayList<PaymentTermsRec> ptList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("allPaymentTerms");
      List lst = q.getResultList();
      ListIterator li = lst.listIterator();
      while(li.hasNext()){
        PaymentTerms p = (PaymentTerms)li.next();
        PaymentTermsRec rec = buildPaymentTermsRec(p);
        ptList.add(rec);
      }
      return ptList;

    }catch(IllegalStateException ex){
      throw new BacException("Get Payment terms query type error","PTerm:01");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get Payment terms query query timeout error","PTerm:02");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get Payment terms query transaction error","PTerm:03");
    }catch(PessimisticLockException ex){
      throw new BacException("Get Payment terms query get lock error","PTerm:04");
    }catch(LockTimeoutException ex){
      throw new BacException("Get Payment terms query get lock timeout error","PTerm:05");
    }catch(PersistenceException ex){
      throw new BacException("Get Payment terms query get other DB error","PTerm:06");
    }
   
  }

  /**
   * Add Payment terms record to DB and returns allocated ID
   * @param terms
   * @return
   * @throws BacException
   */
  public void addPaymentTerm(PaymentTermsRec terms, String pg) throws BacException {
    LOGGER.log(INFO, "DB addPaymentTerm called with {0}", terms);
    PaymentTerms payTerm = this.buildPaymentTerms(terms, pg);
    try{
      em.persist(payTerm);

    }catch(EntityExistsException ex){
      throw new BacException("Add Payment terms duplicate","PTerm:07");
    }catch(IllegalArgumentException ex){
      throw new BacException("Add Payment terms not DB record","PTerm:08");
    }catch(TransactionRequiredException ex){
      throw new BacException("Add Payment terms no transaction","PTerm:09");
    }
  }

/**
 * 
 * @return
 * @throws BacException
 */
  public List<PaymentTypeRec> getAllPaymentTypes() throws BacException {
    LOGGER.log(INFO, "DB getAllPaymentTypes called");
    ArrayList<PaymentTypeRec> ptList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("allPaymentTypes");
      List lst = q.getResultList();
      ListIterator li = lst.listIterator();
      while(li.hasNext()){
        PaymentType p = (PaymentType)li.next();
        PaymentTypeRec rec = buildPaymentTypeRec(p);
        ptList.add(rec);
      }
      return ptList;

    }catch(IllegalStateException ex){
      throw new BacException("Get Payment type query type error","PTtype:01");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get Payment type query query timeout error","PTtype:02");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get Payment type query transaction error","PTtype:03");
    }catch(PessimisticLockException ex){
      throw new BacException("Get Payment type query get lock error","PTtype:04");
    }catch(LockTimeoutException ex){
      throw new BacException("Get Payment type query get lock timeout error","PTtype:05");
    }catch(PersistenceException ex){
      throw new BacException("Get Payment type query get other DB error","PTtype:06");
    }
    
  }

  public void addPaymentType(PaymentTypeRec payType, String view) throws BacException {
    LOGGER.log(INFO, "DB addPaymentType called");
    PaymentType pType = this.buildPaymentType(payType, view);
    try{
      em.persist(pType);

    }catch(EntityExistsException ex){
      throw new BacException("Add Payment type duplicate","PTtype:07");
    }catch(IllegalArgumentException ex){
      throw new BacException("Add Payment type not DB record","PTtype:08");
    }catch(TransactionRequiredException ex){
      throw new BacException("Add Payment type no transaction","PTtype:09");
    }

  }

  private void auditPaymentTypeChange(PaymentType payType, String fieldName, String newValue, 
          String oldValue, User usr,Date updated, String view){
   LOGGER.log(INFO, "auditPaymentTypeChange called");
   AuditPaymentType aud = new AuditPaymentType();
   aud.setAuditDate(updated);
   aud.setCreatedBy(usr);
   aud.setFieldName(fieldName);
   aud.setNewValue(newValue);
   aud.setOldValue(oldValue);
   aud.setPayType(payType);
   aud.setSource(view);
   aud.setUsrAction('U');
   em.persist(aud);
   
   
  }
  
  
  public void deleteNumberRange(NumberRangeRec numberRange) throws BacException {
    LOGGER.log(INFO, "DB deleteNumberRange called for {0}", numberRange);
    NumberRange rec = em.find(NumberRange.class, numberRange.getNumberControlId());
    em.remove(rec);
    LOGGER.log(INFO, "DB After delete");
  }



    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<TransactionTypeRec> getTransactionTypes() {
  LOGGER.log(INFO, "Config DM getTransactionTypes() called");
  List<TransactionTypeRec> retTransTypesList = new ArrayList<>();
  Query tranTypesQ = em.createNamedQuery("allTransTypes");
  List tranTypesDb = tranTypesQ.getResultList();
  if(tranTypesDb == null){
   LOGGER.log(INFO, "No transaction types returned");
   return null;
  } else{
   ListIterator li = tranTypesDb.listIterator();
   while(li.hasNext()){
    TransactionType ttDb = (TransactionType)li.next(); 
    TransactionTypeRec tt = this.buildTransactionTypeRec(ttDb);
    retTransTypesList.add(tt);
   }
   return retTransTypesList;
  }
  
 }

 /**
  * Creates an instance of transaction type in the database
  * @param tt
  * @return 
  */
 public TransactionTypeRec saveTransactionType(TransactionTypeRec tt, String pg) {
  LOGGER.log(INFO, "saveTransactionType called with {0}", tt);
  TransactionType ttDb = this.buildTransactionType(tt, pg);
  
  tt.setId(ttDb.getId());
  LOGGER.log(INFO, "saveTransactionType with id: {0}",tt.getId());
  return tt;
 }

 /**
  * Build User record version
  * Called at DB layer
  * @param paymentTerms
  * @return
  * @throws BacException 
  */
 public PaymentTermsRec getPaymentTermsRec(PaymentTerms paymentTerms) throws BacException {
  LOGGER.log(INFO, "getPaymentTermsRec called with {0}", paymentTerms);
  PaymentTermsRec payTermRec = this.buildPaymentTermsRec(paymentTerms);
  return payTermRec;
 }

 public PaymentTypeRec getPaymentTypeRec(PaymentType payType) {
  LOGGER.log(INFO, "getPaymentTypeRec called with {0}", payType);
  PaymentTypeRec payTypeRec = this.buildPaymentTypeRec(payType);
  return payTypeRec;
 }
 
 public PaymentTypeRec getPaymentTypeBnkAcntRec(PaymentTypeRec ptRec) {
  LOGGER.log(INFO, "getPaymentTypeBnkAcntRec called with {0}", ptRec);
  
  PaymentType pt = em.find(PaymentType.class, ptRec.getId());
  LOGGER.log(INFO, "pt.getGlAccount() {0}", pt.getGlAccount());
  FiGlAccountCompRec bnkActRec = fiMastDM.getFiCompGlAccountRec(pt.getGlAccount());
  ptRec.setGlBankAccount(bnkActRec);
    
  return ptRec;
 }

 public SortOrderRec getSortOrderREc(SortOrder sortOrder) {
  SortOrderRec sortOrderRec = this.buildSortOrderRec(sortOrder);
  return sortOrderRec;
 }

 /**
  * Update account type and returns the updated record
  * @param actType 
  * @param source Page called from
  * @return 
  */
 public AccountTypeRec updateAccountType(AccountTypeRec actType, String source) throws BacException {
  LOGGER.log(INFO,"DB upDateAccountType called with {0}",actType.getName());
  LOGGER.log(INFO, "ConfigDM.actType changed by id {0}", actType.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  AccountType ty = buildAccountType(actType, source);
  if(actType.getId() == null){
   actType.setId(ty.getId());
  }
  trans.commit();
  return actType;
  
 }

 public boolean deleteAccountType(AccountTypeRec accountType, String pg) throws BacException {
  LOGGER.log(INFO, "DB layer deleteAccountType called with {0}", accountType);
  AccountType actTy = em.find(AccountType.class, accountType.getId(), OPTIMISTIC);
  em.remove(actTy);
  Date curr = new Date();
  User usr = em.find(User.class,accountType.getChangedBy().getId(),OPTIMISTIC);
  AuditAccountType aud = new AuditAccountType();
  aud.setAuditDate(curr);
  aud.setCreatedBy(usr);
  aud.setNewValue(accountType.getName());
  aud.setSource(pg);
  aud.setUsrAction('D');
  em.persist(aud);
  return true;
 }

 public NumberRangeChequeRec updateNumberRangeCheque(NumberRangeChequeRec numRng, String pg){
  LOGGER.log(INFO, "confif DM updateNumberRangeCheque calkled with update id {0}", numRng.getCreatedBy().getId());
  if(!trans.isActive()){
   trans.begin();
  }
  LOGGER.log(INFO, "numRng id {0}", numRng.getNumberControlId());
  NumberRangeCheque nc = buildNumberRangeCheque(numRng, pg);
  LOGGER.log(INFO, "nc id {0}", numRng.getNumberControlId());
  BankAccountCompany bnkAcnt = em.find(BankAccountCompany.class, numRng.getBankAccountComp().getId());
  nc.setBankAccountComp(bnkAcnt);
  if(numRng.getNumberControlId() == null){
   numRng.setNumberControlId(nc.getNumberControlId());
  }
  
  if(numRng.getNumberControlId() != null){
   trans.commit();
  }else{
   trans.rollback();
  }
  return numRng;
 }
 
 public NumberRangeTypeRec upateNumberRangeType(NumberRangeTypeRec nrTyRec, String source){
  LOGGER.log(INFO, "upateNumberRangeType called with {0}", nrTyRec.getNumRangeTypeCode());
  if(!trans.isActive()){
   trans.begin();
  }
  NumberRangeType nrTy = buildNumberRangeType(nrTyRec, source);
  if(nrTyRec.getId() == null){
   nrTyRec.setId(nrTy.getId());
  }
  trans.commit();
  return nrTyRec;
 }
 
 
 public NumberRangeRec updateNumControl(NumberRangeRec numControl, String source) {
  LOGGER.log(INFO, "ConfigDM.updateNumberControl called with num cntrl {0} source {1}", 
          new Object[]{numControl,source});
  if(!trans.isActive()){
   trans.begin();
  }
  
  NumberRange nc = buildNumberRange(numControl, source);
  if(numControl.getNumberControlId() == null){
   numControl.setNumberControlId(nc.getNumberControlId());
  }
  trans.commit();
  return numControl;
 }
/**
 * Permanently deletes number control from database
 * @param numberControl Number control to be deleted
 * @param source
 * @return
 * @throws BacException 
 */
 public boolean deleteNumberControl(NumberRangeRec numberCntrl, String source) throws BacException {
  LOGGER.log(INFO, "BasicSetup.deleteNumberControl called with num cntrl {0} source {1}", 
          new Object[]{numberCntrl,source});
  trans.begin();
  User changeUsr = em.find(User.class, numberCntrl.getChangedBy().getId(), OPTIMISTIC);
  if(changeUsr == null){
   throw new  BacException("User "+numberCntrl.getChangedBy().getName()+" not found","NUMCNTRL:01");
  }
  Date changeDate;
  if(numberCntrl.getChangedDate() == null){
   changeDate = new Date();
  }else{
   changeDate = numberCntrl.getChangedDate();
  }
  NumberRange numCntrlDb = em.find(NumberRange.class, numberCntrl.getNumberControlId(), OPTIMISTIC_FORCE_INCREMENT);
  if(numCntrlDb == null){
   throw new  BacException("Number control not found","NUMCNTRL:01");
  }
  
  // remove old audit history
  ListIterator<AuditNumberControl> audList = numCntrlDb.getAuditNumberContolChanges().listIterator();
  while(audList.hasNext()){
   AuditNumberControl audRec = audList.next();
   em.remove(audRec);
   audList.remove();
   LOGGER.log(INFO, "Delete audit record {0}", audRec.getId());
  }
  em.flush();
  trans.commit();
  trans.begin();
  AuditNumberControl aud = new AuditNumberControl();
  aud.setAuditDate(changeDate);
  aud.setCreatedBy(changeUsr);
  aud.setOldValue(numCntrlDb.getShortDescr());
  aud.setSource(source);
  aud.setUsrAction('D');
  em.persist(aud);
  em.remove(numCntrlDb);
  
  trans.commit();
   
  
  return true;
  
 }

 public List<PostType> getPostingTypeList() throws BacException {
  LOGGER.log(INFO,"ConfigDB called ");
    ArrayList<PostType> postTypeList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("findAllPostTypes");
      List rs = q.getResultList();
      if(rs.isEmpty()){
        LOGGER.log(INFO, "No posting types in DB");
        return postTypeList;
      //  throw new BacException("No Posting types found","PTY:07");
      }
      ListIterator it = rs.listIterator();
      PostType pType;
      
      while(it.hasNext()){
       pType = (PostType)it.next();
       postTypeList.add(pType);
      }
    }catch(IllegalStateException ex){
      throw new BacException("Get Post Type query type error","PTY:01");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get Post Type query query timeout error","PTY:02");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get Post Type query transaction error","PTY:03");
    }catch(PessimisticLockException ex){
      throw new BacException("Get Post Type query get lock error","PTY:04");
    }catch(LockTimeoutException ex){
      throw new BacException("Get Post Type query get lock timeout error","PTY:05");
    }catch(PersistenceException ex){
      throw new BacException("Get Post Type query get other DB error","PTY:06");
    }

  return postTypeList;
  
  
 }

 public PaymentTypeRec updatePaymentType(PaymentTypeRec payType,  String view) 
         throws BacException {
  LOGGER.log(INFO, "ConfigurationDM.updatePaymentType called with {0}  view {1}", 
          new Object[]{  payType,view});
  LOGGER.log(INFO, "changed by {0}", payType.getChangedBy()); 
  if(!trans.isActive()){
   trans.begin();
  }
  PaymentType pt = buildPaymentType(payType, view);
  if(payType.getId() == null){
   payType.setId(pt.getId());
  }
 LOGGER.log(INFO, "Config updatePayType id {0} code {1}", 
   new Object[]{payType.getId(),payType.getPayTypeCode()});
  trans.commit();
  return payType;
 }
 
 public PaymentTermsRec updatePaymentTerms(PaymentTermsRec termsRec, String pg){
  LOGGER.log(INFO, "Config DM  updatePaymentTerms called with {0}", termsRec.getPayTermsCode());
  if(!trans.isActive()){
   trans.begin();
  }
  LOGGER.log(INFO, "rec ID before buildPaymentTerms {0} ", termsRec.getId());
  PaymentTerms terms = buildPaymentTerms(termsRec,pg);
  LOGGER.log(INFO, "ID after buildPaymentTerms {0} ", terms.getId());
  if(termsRec.getId() == null){
   termsRec.setId(terms.getId());
  }
  
  LOGGER.log(INFO, "end updatePaymentTerms terms code {0} id {1} ", 
    new Object[]{termsRec.getPayTermsCode(),termsRec.getId()});
  if(termsRec.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  
  
  return termsRec;
 }

 public ChequeVoidReasonRec updateChequeVoidReason(ChequeVoidReasonRec rsnRec, String pg){
  LOGGER.log(INFO, "rsnRec created by   {0} id {1}", new Object[]{rsnRec.getCreatedBy(),rsnRec.getId()});
  if(!trans.isActive()){
   trans.begin();
  }
  ChequeVoidReason rsn = buildChequeVoidReason(rsnRec, pg);
  if(rsnRec.getId() == null){
   rsnRec.setId(rsn.getId());
  }
  LOGGER.log(INFO, "rsn id after build {0}", rsn.getId());
  trans.commit();
  return rsnRec;
 }
 
 public DocReversalReasonRec updateDocReversalReason(DocReversalReasonRec revRsnRec, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  DocReversalReason revRsn = this.buildDocReversalReason(revRsnRec, pg);
  if(revRsnRec.getId() == null){
   revRsnRec.setId(revRsn.getId());
  }
  LOGGER.log(INFO, "After build Doc rev rsn id {0}", revRsnRec.getId());
  if(revRsnRec.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  return revRsnRec;
 }
 public DocTypeRec updateDocType(DocTypeRec docTyRec, String pg){
  LOGGER.log(INFO, "ConfigDM.updateDocType called with {0}", docTyRec);
  
  if(!trans.isActive()){
   trans.begin();
  }
  
  DocType docTy = this.buildDocType(docTyRec, pg);
  if(docTyRec.getId() == null){
   docTyRec.setId(docTy.getId());
  }
  trans.commit();
 return docTyRec; 
 }
 public FundCategoryRec updateFundCategoryRec(FundCategoryRec catRec, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  FundCategory cat = this.buildFundCategory(catRec, pg);
  if(catRec.getId() == null){
   catRec.setId(cat.getId());
  }
  trans.commit();
  return catRec;
 }
 public PostTypeRec updatePostType(PostTypeRec postingType, String pg) {
    LOGGER.log(INFO, "UpdatePostType called with {0}", postingType);
    if(!trans.isActive()){
     trans.begin();
    }
    PostType pt = buildPostType(postingType,pg);
    
    trans.commit();
    return postingType;
  }
 
 public TransactionTypeRec updateTransactionType(TransactionTypeRec rec, String pg){
  LOGGER.log(INFO, "updateTransactionType config updateTransactionType called with {0}", rec);
  if(!trans.isActive()){
   trans.begin();
  }
  TransactionType transTy = buildTransactionType(rec, pg);
  if(rec.getId() == null){
   rec.setId(transTy.getId());
  }
  trans.commit();
  return rec;
 }
 
 public SortOrderRec updateSortOrder(SortOrderRec rec, String src){
  LOGGER.log(INFO, "updateSortOrder clled with sort {0}", rec.getName());
  if(!trans.isActive()){
   trans.begin();
  }
  SortOrder s = this.buildSortOrder(rec, src);
  if(rec.getId() == null){
   rec.setId(s.getId());
  }
  trans.commit();
  return rec;
 }
 public UomRec updateUom(UomRec uom, String pg){
  LOGGER.log(INFO, "configDM.updateUom {0} ", uom);
  if(!trans.isActive()){
   trans.begin();
  }
  Uom u = this.buildUom(uom, pg);
  if(uom.getId() == null){
   uom.setId(u.getId());
  }
  
  trans.commit();
  return uom;
 }
 
 public boolean isChequeTemplUsedByPayType(ChequeTemplateRec templ){
  LOGGER.log(INFO, "isChequeTemplUsedByPayType {0}", templ.getId());
  Query q = em.createNamedQuery("payTypesByChqTempl");
  q.setParameter("chqTemplId", templ.getId());
  
  List rs = q.getResultList();
  LOGGER.log(INFO, "Templates for payment type {0}", rs);
  if(rs == null || rs.isEmpty()){
   return false;
  }else{
   return true;
  }
 }
 public int paymentTypeCopyCompany(CompanyBasicRec c1, CompanyBasicRec c2,
         UserRec user, String pg){
  LOGGER.log(INFO, "ConfigDM.paymentTypeCopyCompany called with comp {0} and {1}", 
          new Object[]{c1.getReference(),c2.getReference()});
  // Check if comp 2 has payment types
  if(!trans.isActive()){
   trans.begin();
  }
  TypedQuery q1 = em.createNamedQuery("payTypesForComp", PaymentType.class);
  q1.setMaxResults(1);
  q1.setParameter("compId", c2.getId());
  List<PaymentType> rs = q1.getResultList();
  if(!rs.isEmpty()){
   return CompanyManager.ATTR_IN_DEST_COMP;
  }
  TypedQuery q2 = em.createNamedQuery("payTypesForComp", PaymentType.class);
  q2.setParameter("compId", c1.getId());
  rs = q2.getResultList();
  if(rs.isEmpty()){
   return CompanyManager.ATTR_NOT_IN_SRC_COMP;
  }
  CompanyBasic comp = em.find(CompanyBasic.class, c2.getId());
  User usr = em.find(User.class, user.getId());
  int numCreated = 0;
  for(PaymentType py:rs){
   PaymentType curr = new PaymentType();
   curr.setBacsTransCode(py.getBacsTransCode());
   curr.setChqTemplate(py.getChqTemplate());
   curr.setCompany(comp);
   curr.setCreatedBy(usr);
   curr.setCreatedOn(new Date());
   curr.setDescription(py.getDescription());
   if(py.getGlAccount() != null){
    Query q_gl = em.createNamedQuery("GlCompAcnt");
    q_gl.setParameter("compId", c2.getId());
    LOGGER.log(INFO, "payType GL acccount {0}", py.getGlAccount());
    LOGGER.log(INFO, "chart accnt {0}", py.getGlAccount().getCoaAccount());
    q_gl.setParameter("acntRef", py.getGlAccount().getCoaAccount().getRef());
    FiGlAccountComp glAcnt = (FiGlAccountComp)q_gl.getSingleResult();
    LOGGER.log(INFO,"glAcnt from new comp {0}",glAcnt);
    curr.setGlAccount(glAcnt);
   }
   curr.setHasChqTemplate(py.isHasChqTemplate());
   curr.setInbound(py.isInbound());
   curr.setLedger(py.getLedger());
   curr.setMediumUom(py.getMediumUom());
   curr.setPayMedium(py.getPayMedium());
   curr.setPayTypeCode(py.getPayTypeCode());
   curr.setSummLevel(py.getSummLevel());
   em.persist(curr);
   AuditPaymentType aud = this.buildAuditPaymentType(py, 'I', usr, pg);
   aud.setNewValue(curr.getPayTypeCode());
   numCreated++;
  }
  LOGGER.log(INFO, "numCreated {0}", numCreated);
  if(numCreated > 0){
   trans.commit();
   return CompanyManager.ATTR_CREATED;
  }else{
   trans.rollback();
   return CompanyManager.ATTR_NOT_CREATED;
  }
 }
 
 
 public PaymentTypeRec paymentTypeCreate(PaymentTypeRec payType, UserRec usr, String view) 
         throws BacException {
  LOGGER.log(INFO, "ConfigurationDM.updatePaymentType called with {0} user id {1} view {2}", 
          new Object[]{  payType,usr.getId(),view});
  User usrAudit = em.find(User.class, usr.getId(), OPTIMISTIC);
  PaymentType pt = buildPaymentType(payType, view);
  payType.setId(pt.getId());
  payType.setRevision(pt.getRevision());
  AuditPaymentType au = new AuditPaymentType();
  au.setAuditDate(new Date());
  au.setCreatedBy(usrAudit);
  au.setNewValue(pt.getPayTypeCode());
  au.setPayType(pt);
  au.setSource(view);
  au.setUsrAction('I');
  em.persist(au);
  return payType;
 }

 public ProcessCodeRec processCodeUpdate(ProcessCodeRec rec, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  ProcessCode code = this.buildProcessCode(rec, pg);
  if(rec.getId() == null){
   rec.setId(code.getId());
  }
  trans.commit();
  return rec;
 }
 /**
  * Save new value date to DB
  * @param valueDate
  * @param usr
  * @param source
  * @return
  * @throws BacException 
  */
 public ValueDateRec addValueDate(ValueDateRec valueDate, UserRec usr, String source) throws BacException {
  LOGGER.log(INFO, "addValueDate called with value date {0} user {1} source {2}", new Object[]{
   valueDate,usr,source });
  Date prDate = new Date();
  valueDate.setCreatedOn(prDate);
  valueDate.setCreatedBy(usr);
  
  ValueDate vd = this.buildValueDate(valueDate);
  valueDate.setId(vd.getId());
  valueDate.setNumber(vd.getNumber());
  AuditValueDate aud = new AuditValueDate();
  aud.setAuditDate(prDate);
  User audUser = em.find(User.class, usr.getId(), OPTIMISTIC);
  aud.setCreatedBy(audUser);
  aud.setNewValue(String.valueOf(valueDate.getNumber()));
  aud.setSource(source);
  aud.setUsrAction('I');
  aud.setValueDate(vd);
  
  return valueDate;
 }
 
 
 
 
 
}
