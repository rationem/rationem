
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

//~--- non-JDK imports --------------------------------------------------------

import com.rationem.busRec.cm.ContactRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.arap.FiArPeriodBalanceRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.entity.audit.AuditArAccount;
import com.rationem.entity.audit.AuditArBank;
import com.rationem.entity.cm.Contact;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.config.company.FisPeriodRule;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.arap.FiArPeriodBalance;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.tr.bank.BankAccount;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
//import com.sun.org.apache.xerces.internal.utils.Objects;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;

import javax.persistence.EntityManager;
//import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.Persistence;
import javax.annotation.PostConstruct;

import static java.util.logging.Level.INFO;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;

/**
 * Manages DB access for AR accounts
 * @author Chris
 */
@Stateless
@LocalBean
public class ArAccountDM {
 private static final Logger LOGGER = Logger.getLogger("com.rationem.ejbBean.dataManager.ArAccountDM");
 @EJB
 private TreasuryDM          bankDM;
    
 @EJB
 private MasterDataDM masterDataDm;
    
 @EJB
 private UserDM usrDM;
    
 @EJB
 private CompanyDM compDM;
    
 @EJB
 private ConfigurationDM configDM;
 
 @EJB
 private FiMasterRecordDM glMastDM;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private ContactDM contDm;
 
    
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
    

 private ArBankAccount buildArBank(ArBankAccountRec rec,   String page){
  ArBankAccount bnk;
  boolean newBnk = false;
  boolean changedBnk = false;
  LOGGER.log(INFO, "Bank rec id {0}", rec.getId());
  if(rec.getId() != null && rec.getId() < 0){
   rec.setId(null);
  }
     if(rec.getId() == null ){
      bnk = new ArBankAccount();
      User usr = em.find(User.class, rec.getCreatedBy().getId());
      bnk.setCreatedBy(usr);
      bnk.setCreatedOn(new Date());
      em.persist(bnk);
      AuditArBank aud = this.buildAuditArBank(bnk, usr, page, 'I');
      aud.setNewValue(rec.getBankKey());
      newBnk = true;
     }else{
      bnk = em.find(ArBankAccount.class, rec.getId(), OPTIMISTIC);
     }
     if(newBnk){
      bnk.setAccountName(rec.getAccountName());
      BankAccount bnkAc = em.find(BankAccount.class, rec.getBankAccount().getId());
      bnk.setBankAccount(bnkAc);
      if(rec.getBankForApAccount() != null){
       ApAccount apAcnt = em.find(ApAccount.class, rec.getBankForApAccount().getId());
       bnk.setBankForApAccount(apAcnt);
      }
      if(rec.getBankForArAccount() != null){
       ArAccount arAcnt = em.find(ArAccount.class, rec.getBankForArAccount().getId());
       bnk.setBankForArAccount(arAcnt);
      }
      bnk.setBankKey(rec.getBankKey());
      bnk.setCollValidFrom(rec.getCollValidFrom());
      bnk.setCollectionAuthorisation(rec.isCollectionAuthorisation());
      bnk.setDdFileType(rec.getDdFileType());
      bnk.setDdRef(rec.getDdRef());
      bnk.setSignedDD(rec.getSignedDD());
      }else {
      User chUsr = em.find(User.class, rec.getChangedBy().getId());
      
      if((rec.getAccountName() == null && bnk.getAccountName() != null) ||
        (rec.getAccountName() != null && bnk.getAccountName() == null) ||
        (rec.getAccountName() != null && !rec.getAccountName().equals(bnk.getAccountName())
         )){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_NM");
       aud.setNewValue(rec.getAccountName());
       aud.setOldValue(bnk.getAccountName());
       bnk.setAccountName(rec.getAccountName());
       changedBnk = true;
      }
      if(!rec.getCollValidFrom().equals(bnk.getCollValidFrom())){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_COL_FR");
       aud.setNewValue(rec.getCollValidFrom().toString());
       aud.setOldValue(bnk.getCollValidFrom().toString());
       bnk.setCollValidFrom(rec.getCollValidFrom());
       changedBnk = true;
      }
      if(!rec.getBankKey().equals(bnk.getBankKey())){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_BNK_KEY");
       aud.setNewValue(rec.getBankKey());
       aud.setOldValue(bnk.getBankKey());
       bnk.setBankKey(rec.getBankKey());
       changedBnk = true;
      }
      
      if(rec.isCollectionAuthorisation() != bnk.isCollectionAuthorisation()){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_BNK_COL_AUTH");
       aud.setNewValue(String.valueOf(rec.isCollectionAuthorisation()));
       aud.setOldValue(String.valueOf(rec.isCollectionAuthorisation()));
       bnk.setCollectionAuthorisation(rec.isCollectionAuthorisation());
       changedBnk = true;
      }
      
      if((rec.getDdFileType() == null && bnk.getDdFileType() != null) || 
         (rec.getDdFileType() != null && bnk.getDdFileType() == null) ||
         (rec.getDdFileType() != null && !rec.getDdFileType().equals(bnk.getDdFileType()))){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_BNK_DD_FT");
       aud.setNewValue(rec.getDdFileType());
       aud.setOldValue(bnk.getDdFileType());
       bnk.setDdFileType(rec.getDdFileType());
       changedBnk = true;
      }
      
      if((rec.getDdRef() == null && bnk.getDdRef() != null) || 
         (rec.getDdRef() != null && bnk.getDdRef() == null) ||
         (rec.getDdRef() != null && !rec.getDdRef().equals(bnk.getDdRef()))){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_BNK_DD_REF");
       aud.setNewValue(rec.getDdRef());
       aud.setOldValue(bnk.getDdRef());
       bnk.setDdRef(rec.getDdRef());
       changedBnk = true;
      }
      if((rec.getSignedDD() == null && bnk.getSignedDD() != null) || 
         (rec.getSignedDD() != null && bnk.getSignedDD() == null) ||
         (rec.getSignedDD() != null && 
         rec.getSignedDD().length != bnk.getSignedDD().length)){
       AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
       aud.setFieldName("AR_ACNT_BNK_DD_MDT");
       bnk.setSignedDD(rec.getSignedDD());
       changedBnk = true;
      }
      
      if(changedBnk){
       bnk.setChangedBy(chUsr);
       bnk.setChangedOn(rec.getChangedOn());
      }
      
     }
     return bnk;
    }
    
    private ArBankAccount buildArBank(ArBankAccountRec rec, BankAccount bnkAc) {
        ArBankAccount bnk;
        LOGGER.log(INFO, "buildArBank arbank {0} bank ac {1}", 
                new Object[]{rec,bnkAc});

        if ((rec.getId() != null) && (rec.getId() != 0)) {
            bnk = em.find(ArBankAccount.class, rec.getId(), OPTIMISTIC);
        } else {
            bnk = new ArBankAccount();
            em.persist(bnk);
        }

        bnk.setAccountName(rec.getAccountName());
        bnk.setBankAccount(bnkAc);

        if (rec.getCreatedBy() != null) {
            User usr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);

            bnk.setCreatedBy(usr);
            bnk.setCreatedOn(new Date());
        }

        bnk.setCollValidFrom(rec.getCollValidFrom());
        bnk.setCollectionAuthorisation(rec.isCollectionAuthorisation());

        return bnk;
    }
    
    private ArBankAccount buildArBank(ArBankAccountRec arBnkRec, ArBankAccount arBnk){
     
     if(!arBnk.getAccountName().equalsIgnoreCase(arBnkRec.getAccountName())){
      arBnk.setAccountName(arBnkRec.getAccountName());
     }
     if(arBnk.getBankKey().equalsIgnoreCase(arBnkRec.getAccountName())){
      arBnk.setBankKey(arBnkRec.getAccountName());
     }
     if(arBnk.getCollValidFrom().compareTo(arBnkRec.getCollValidFrom()) != 0){
      arBnk.setCollectionAuthorisation(arBnkRec.isCollectionAuthorisation());
     }
     if(arBnk.getDdRef().equalsIgnoreCase(arBnkRec.getDdRef())){
      arBnk.setDdRef(arBnkRec.getDdRef());
     }
     if(arBnk.getSignedDD() != arBnkRec.getSignedDD()){
      arBnk.setSignedDD(arBnkRec.getSignedDD());
     }
     return arBnk;
    }
    
    
    private ArBankAccountRec buildArBankRec(ArBankAccount rec, ArAccountRec arAc) {
        ArBankAccountRec arBnk = new ArBankAccountRec();
        LOGGER.log(INFO, "buildArBank arbank {0} ", 
                new Object[]{rec});

        arBnk.setAccountName(rec.getAccountName());
        BankAccountRec bnk = this.bankDM.getBankAccountRec(rec.getBankAccount());
        
        arBnk.setBankAccount(bnk);
        arBnk.setBankForArAccount(arAc);
        arBnk.setBankKey(rec.getBankKey());
        if(rec.getChangedBy() != null){
         UserRec usr = usrDM.getUserRecPvt(rec.getChangedBy());
         arBnk.setChangedBy(usr);
         arBnk.setChangedOn(rec.getChangedOn());
        }
        arBnk.setChanges(rec.getChanges());
        arBnk.setCollValidFrom(rec.getCollValidFrom());
        arBnk.setCollectionAuthorisation(rec.isCollectionAuthorisation());
        if(rec.getCreatedBy() != null){
         UserRec usr = usrDM.getUserRecPvt(rec.getCreatedBy());
         arBnk.setCreatedBy(usr);
         arBnk.setCreatedOn(rec.getCreatedOn());
        }
        arBnk.setDdRef(rec.getDdRef());
        arBnk.setId(rec.getId());
        arBnk.setSignedDD(rec.getSignedDD());
        
        
        return arBnk;
    }

    private ArAccount buildArAccount(ArAccountRec rec,   String page) {
        ArAccount ac;
        LOGGER.log(INFO, "ArAccountDM buildArAccount called with account {0} create User {1} update usr {2}",
                new Object[]{rec});
        Date currDate = new Date();
        boolean acntNew = false;
        boolean acntChanged = false;

        if (rec.getId() != null) {
         ac = em.find(ArAccount.class, rec.getId(), OPTIMISTIC);
            
        } else {
         User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
         ac = new ArAccount();
         ac.setCreatedBy(crUsr);
         ac.setCreatedOn(currDate);
         em.persist(ac);
         AuditArAccount aud = this.buildAuditArAccount(ac, crUsr, page, 'I');
         aud.setNewValue(rec.getArAccountCode());
         acntNew = true;
        }
        
        if(acntNew){
         if(rec.getAccountAddress() != null){
          if(rec.getAccountAddress().getId() == null){
           AddressRec addrRec = this.masterDataDm.createAddress(rec.getAccountAddress(), rec.getCreatedBy(), page);
           rec.getAccountAddress().setId(addrRec.getId());
          }
          Address addr = em.find(Address.class, rec.getAccountAddress().getId());
          ac.setAccountAddress(addr);
         }
         if(rec.getAccountClerk().getId() != null){
          PartnerPerson ptnr = em.find(PartnerPerson.class, rec.getAccountClerk().getId());
          ac.setAccountClerk(ptnr);
         }
         ac.setAccountCode(rec.getArAccountCode());
         ac.setCompany(sysBuff.getComp(rec.getCompany()));
         if(rec.getPaymentTerms() != null){
          PaymentTerms pt = em.find(PaymentTerms.class, rec.getPaymentTerms().getId());
          ac.setPaymentTerms(pt);
         }
         
         LOGGER.log(INFO, "rec.getArAccountFor() {0}", rec.getArAccountFor());
         
         if(rec.getArAccountFor() != null){
          PartnerBase ptnrB = em.find(PartnerBase.class, rec.getArAccountFor().getId());
          ac.setArAccountFor(ptnrB);
          LOGGER.log(INFO,"partner for {0}",ac.getArAccountFor());
          LOGGER.log(INFO,"partner class {0}",rec.getArAccountFor().getClass().getSimpleName());
          String actForCl = rec.getArAccountFor().getClass().getSimpleName();
          switch(actForCl){
           case "PartnerCorporateRec":
            PartnerCorporate ptnrCorp = (PartnerCorporate)ptnrB;
            ac.setArAccPtnrType("CORP");
            ac.setArAccountForCorporate(ptnrCorp);
            break;
           case "PartnerPersonRec":
            PartnerPerson ptnrPers = (PartnerPerson)ptnrB;
            ac.setArAccPtnrType("PERS");
            ac.setArAccountForPerson(ptnrPers);
          }
         }
         LOGGER.log(INFO, "ac", page);
         if(rec.getPaymentType() != null){
          PaymentType pty = em.find(PaymentType.class, rec.getPaymentType().getId());
          ac.setPaymentType(pty);
         }
         if(rec.getReconciliationAc() != null){
          FiGlAccountComp acnt = em.find(FiGlAccountComp.class, rec.getReconciliationAc().getId());
          ac.setReconciliationAc(acnt);
         }
         if(rec.getSortOrder() != null){
          SortOrder srt = em.find(SortOrder.class, rec.getSortOrder().getId());
          ac.setSortOrder(srt);
         }
         ac.setAcntNote(rec.getAcntNote());
         if(rec.getContacts() != null){
          ArrayList<Contact> cntlist = new ArrayList<>();
          for(ContactRec c:rec.getContacts()){
           LOGGER.log(INFO, "Contact {0}", c.getSummary());
           
          }
         }
        }else{
         // any changes made?
         User chUser = em.find(User.class, rec.getChangedBy().getId());
         
         if((rec.getAccountAddress() == null && ac.getAccountAddress() != null)||
            (rec.getAccountAddress() != null && ac.getAccountAddress() == null) ||
            (rec.getAccountAddress() != null && 
            !ac.getAccountAddress().getId().equals(rec.getAccountAddress().getId()))){
           AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
           aud.setFieldName("AR_ACNT_ADDR");
           aud.setNewValue(rec.getAccountAddress().getAddrRef());
           aud.setOldValue(ac.getAccountAddress().getAddrRef());
           Address newAdr = null;
           if(rec.getAccountAddress() != null){
            newAdr = em.find(Address.class, rec.getAccountAddress().getId());
           }
           ac.setAccountAddress(newAdr);
           acntChanged = true;
         }
         
         if((rec.getAccountClerk() == null && ac.getAccountClerk() != null)||
            (rec.getAccountClerk() != null && ac.getAccountClerk() == null) ||
            (rec.getAccountClerk().getId() != null && 
            !ac.getAccountClerk().getId().equals(rec.getAccountClerk().getId()))){
          AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
          aud.setFieldName("AR_ACNT_CLRK");
          aud.setNewValue(rec.getAccountClerk().getFamilyName());
          if(ac.getAccountClerk() != null){
           aud.setOldValue(ac.getAccountClerk().getFamilyName());
          }
          if(rec.getAccountClerk().getId() == null){
           ac.setAccountClerk(null);
          }else{
           PartnerPerson pers = em.find(PartnerPerson.class, rec.getAccountClerk().getId());
           ac.setAccountClerk(pers);
          }
          acntChanged = true;
        }
        if((rec.getArAccountCode() == null && ac.getAccountCode() != null)||
            (rec.getArAccountCode() != null && ac.getAccountCode() == null) ||
            (rec.getArAccountCode() != null && 
            !ac.getAccountCode().equals(rec.getArAccountCode()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_CD");
         aud.setNewValue(rec.getArAccountCode());
         aud.setOldValue(ac.getAccountCode());
         ac.setAccountCode(rec.getArAccountCode());
         acntChanged = true;
        }
        if((rec.getArAccountName()== null && ac.getAccountName() != null)||
            (rec.getArAccountName() != null && ac.getAccountName() == null) ||
            (rec.getArAccountName() != null && 
            !ac.getAccountName().equals(rec.getArAccountName()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_NM");
         aud.setNewValue(rec.getArAccountName());
         aud.setOldValue(ac.getAccountName());
         ac.setAccountName(rec.getArAccountName());
         acntChanged = true;
        }
        if((rec.getAcntNote()== null && ac.getAcntNote() != null)||
            (rec.getAcntNote() != null && ac.getAcntNote() == null) ||
            (rec.getAcntNote() != null && 
            !ac.getAcntNote().equals(rec.getAcntNote()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_NOTE");
         aud.setNewValue(rec.getAcntNote());
         aud.setOldValue(ac.getAcntNote());
         ac.setAcntNote(rec.getAcntNote());
         acntChanged = true;
        }
        
        if((rec.getArAccountFor() == null && ac.getArAccountFor()!= null)||
            (rec.getArAccountFor() != null && ac.getArAccountFor() == null) ||
            (rec.getArAccountFor() != null && 
            !ac.getArAccountFor().getId().equals(rec.getArAccountFor().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_PTNR");
         aud.setNewValue(rec.getArAccountFor().getTradingName());
         aud.setOldValue(ac.getArAccountFor().getTradingName());
         PartnerBase ptnr = em.find(PartnerBase.class, rec.getArAccountFor().getId());
         ac.setArAccountFor(ptnr);
         acntChanged = true;
        }
        if((rec.getCompany() == null && ac.getCompany()!= null)||
            (rec.getCompany() != null && ac.getCompany() == null) ||
            (rec.getCompany() != null && 
            !ac.getCompany().getId().equals(rec.getCompany().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_PTNR");
         aud.setNewValue(rec.getCompany().getReference());
         aud.setOldValue(ac.getCompany().getNumber());
         ac.setCompany(this.sysBuff.getComp(rec.getCompany()));
         acntChanged = true;
        }
        if((rec.getPaymentTerms() == null && ac.getPaymentTerms()!= null)||
            (rec.getPaymentTerms() != null && ac.getPaymentTerms() == null) ||
            (rec.getPaymentTerms() != null && 
            !ac.getPaymentTerms().getId().equals(rec.getPaymentTerms().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_PAYTERM");
         aud.setNewValue(rec.getPaymentTerms().getPayTermsCode());
         aud.setOldValue(ac.getPaymentTerms().getPayTermsCode());
         PaymentTerms pt = em.find(PaymentTerms.class, rec.getPaymentTerms().getId());
         ac.setPaymentTerms(pt);
         acntChanged = true;
        }
        if((rec.getPaymentTerms() == null && ac.getPaymentTerms()!= null)||
            (rec.getPaymentTerms() != null && ac.getPaymentTerms() == null) ||
            (rec.getPaymentTerms() != null && 
            !ac.getPaymentTerms().getId().equals(rec.getPaymentTerms().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_PAYTERM");
         aud.setNewValue(rec.getPaymentTerms().getPayTermsCode());
         aud.setOldValue(ac.getPaymentTerms().getPayTermsCode());
         PaymentTerms pt = em.find(PaymentTerms.class, rec.getPaymentTerms().getId());
         ac.setPaymentTerms(pt);
         acntChanged = true;
        }
        if((rec.getReconciliationAc() == null && ac.getReconciliationAc()!= null)||
            (rec.getReconciliationAc() != null && ac.getReconciliationAc() == null) ||
            (rec.getReconciliationAc() != null && 
            !ac.getReconciliationAc().getId().equals(rec.getReconciliationAc().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_REC_GL");
         aud.setNewValue(rec.getReconciliationAc().getCoaAccount().getRef());
         aud.setOldValue(ac.getReconciliationAc().getCoaAccount().getRef());
         LOGGER.log(INFO, "rec.getReconciliationAc() {0}", rec.getReconciliationAc());
         LOGGER.log(INFO, "rec.getReconciliationAc().getId() {0}", rec.getReconciliationAc().getId());
         FiGlAccountComp gl = em.find(FiGlAccountComp.class, rec.getReconciliationAc().getId());
         ac.setReconciliationAc(gl);
         acntChanged = true;
        }
        if((rec.getSortOrder() == null && ac.getSortOrder()!= null)||
            (rec.getSortOrder() != null && ac.getSortOrder() == null) ||
            (rec.getSortOrder() != null && 
            !ac.getSortOrder().getId().equals(rec.getSortOrder().getId()))){
         AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
         aud.setFieldName("AR_ACNT_SORT");
         aud.setNewValue(rec.getSortOrder().getSortCode());
         aud.setOldValue(ac.getSortOrder().getSortCode());
         SortOrder srt = em.find(SortOrder.class, rec.getSortOrder().getId());
         ac.setSortOrder(srt);
         acntChanged = true;
        }
        
        if((rec.getContacts() == null && ac.getContacts() != null) ||
          (rec.getContacts() != null && ac.getContacts() == null) ||
          (rec.getContacts() != null && ac.getContacts() != null)){
         for(ContactRec c:rec.getContacts()){
          if (c.getId() == null){
           // new contact
           AuditArAccount aud = buildAuditArAccount(ac, chUser, page, 'u');
           aud.setFieldName("AR_ACNT_CONT_ADD");
           aud.setNewValue(c.getSummary());
           LOGGER.log(INFO, "ArManDM cont created by {0}", c.getCreatedBy().getId());
           c = contDm.updateContact(c, page);
           acntChanged= true;
          }else{
           c = contDm.updateContact(c, page);
          }
         }
        }
        if(acntChanged){
         ac.setChangedBy(chUser);
         ac.setChangedOn(rec.getChangedOn());
        }
        }
        LOGGER.log(INFO, "End of build arAccount has partner {0}", ac.getArAccountFor());
     return ac;
    }
    
    public ArAccountRec buildArAccountRecPvt(ArAccount rec, UserRec usr, String pg){
     return buildArAccountRec(rec);
    }
    
    private ArAccountRec buildArAccountRec(ArAccount rec) throws BacException{
        ArAccountRec ac = new ArAccountRec();
        LOGGER.log(INFO, "ArAccountDM buildArAccount called with account {0} create User {1} update usr {2}",
                new Object[]{rec});
        
        ac.setId(rec.getId());
        
        if (rec.getAccountAddress() != null ) {
         AddressRec addr = this.masterDataDm.getAddressRec(rec.getAccountAddress());
         ac.setAccountAddress(addr);
            
        }
        
        if (rec.getInvoiceAddress() != null ) {
         AddressRec addr = this.masterDataDm.getAddressRec(rec.getInvoiceAddress());
         ac.setInvoiceAddress(addr);
            
        }
        
        ac.setAccountBalance(rec.getAccountBalance());
        ac.setBalBucket1(rec.getBalBucket1());
        ac.setAcntNote(rec.getAcntNote());

        if (rec.getAccountClerk() != null ) {
            PartnerPersonRec clerk = this.masterDataDm.buildPartnerPersonRecPvt(rec.getAccountClerk());
            ac.setAccountClerk(clerk);
        }

        ac.setArAccountCode(rec.getAccountCode());

        if (rec.getArAccountBanks() != null) {
         
         List<ArBankAccount> arBkActsList = rec.getArAccountBanks();
         ArrayList<ArBankAccountRec> arBkActRecsList = new ArrayList<>();
         ListIterator<ArBankAccount> li = arBkActsList.listIterator();
         while(li.hasNext()){
          ArBankAccountRec bnkRec = this.buildArBankRec(li.next(), ac);
          arBkActRecsList.add(bnkRec);
         }
         ac.setArAccountBanks(arBkActRecsList);
         }

        if (rec.getArAccountFor() != null) {
         PartnerBase ptnrB = rec.getArAccountFor();
         String partnerCl = rec.getArAccountFor().getClass().getSimpleName();
         LOGGER.log(INFO, "partnerCl {0}", partnerCl);
         if(StringUtils.equals(partnerCl, "PartnerPerson")){
          PartnerBaseRec ptnr = this.masterDataDm.buildPartnerPersonRecPvt((PartnerPerson)ptnrB);
          ac.setArAccountName(ptnr.getName());
          ac.setArAccountFor(ptnr);
         }else{
          PartnerBaseRec ptnr = this.masterDataDm.getPartnerCorporateRec((PartnerCorporate)ptnrB);
          
          ac.setArAccountName(ptnr.getName());
          ac.setArAccountFor(ptnr);
         }
         PartnerBaseRec ptnr = ac.getArAccountFor();
         if(ptnr.getArAccounts() != null){
          ArrayList<ArAccountRec> accountList = ptnr.getArAccounts();
          accountList.add(ac);
          ptnr.setArAccounts(accountList);
         }
        }

        if ((rec.getCompany() != null) ) {
         CompanyBasicRec comp =this.compDM.buildCompanyBasicRecPvt(rec.getCompany());
         ac.setCompany(comp);
        }

       

        if (rec.getPaymentTerms() != null) {
            PaymentTermsRec pt = this.configDM.getPaymentTermsRec(rec.getPaymentTerms());
            ac.setPaymentTerms(pt);
        }

        if (rec.getPaymentType() != null) {
            PaymentTypeRec pty = this.configDM.getPaymentTypeRec(rec.getPaymentType());

            ac.setPaymentType(pty);
        }
        if(rec.getReconciliationAc() != null){
         FiGlAccountCompRec recAcnt = glMastDM.buildFiCompGlAccountRecPvt(rec.getReconciliationAc());
         ac.setReconciliationAc(recAcnt);
        }
        if (rec.getSortOrder() != null) {
         
            SortOrderRec so = configDM.getSortOrderREc(rec.getSortOrder());
            ac.setSortOrder(so);
        }
        return ac;
    }

    private AuditArAccount buildAuditArAccount(ArAccount acnt, User usr, String pg, char usrAction){
     AuditArAccount aud = new AuditArAccount();
     aud.setArAccount(acnt);
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditArBank buildAuditArBank(ArBankAccount bnk, User usr, String pg, char usrAction){
     AuditArBank aud = new AuditArBank();
     aud.setArBank(bnk);
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
 }
 
 private FiArPeriodBalance buildFiARPeriodBalance( FiArPeriodBalanceRec balRec){
  FiArPeriodBalance bal;
  if(balRec.getId() != null){
   bal = new FiArPeriodBalance();
  }else{
   bal = em.find(FiArPeriodBalance.class, balRec.getId());
  }
  ArAccount acnt = em.find(ArAccount.class, balRec.getId());
  bal.setArAccount(acnt);
  bal.setBalPeriod(balRec.getBalPeriod());
  bal.setBalYear(balRec.getBalYear());
  bal.setBfwdDocAmount(balRec.getBfwdDocAmount());
  bal.setBfwdLocalAmount(balRec.getBfwdLocalAmount());
  bal.setCfwdDocAmount(balRec.getCfwdDocAmount());
  bal.setCfwdLocalAmount(balRec.getCfwdLocalAmount());
  bal.setCreated(balRec.getCreated());
  if(bal.getCreatedBy() != null){
   User crUsr = em.find(User.class, balRec.getCreatedBy().getId());
   bal.setCreatedBy(crUsr);
  }
  bal.setId(balRec.getId());
  bal.setPeriodCreditAmount(balRec.getPeriodCreditAmount());
  bal.setPeriodDebitAmount(balRec.getPeriodDebitAmount());
  bal.setPeriodDocAmount(balRec.getPeriodDocAmount());
  bal.setPeriodLocalAmount(balRec.getPeriodLocalAmount());
  bal.setPeriodTurnover(balRec.getPeriodTurnover());
  if(balRec.getUpdateBy() != null){
   User upDtUsr = em.find(User.class, balRec.getUpdateBy().getId());
   bal.setUpdateBy(upDtUsr);
   bal.setUpdateDate(bal.getUpdateDate());
  }
  return bal;
 }
 
 private FiArPeriodBalanceRec buildFiARPeriodBalanceRec(ArAccountRec  acntRec, FiArPeriodBalance bal){
  FiArPeriodBalanceRec balRec = new FiArPeriodBalanceRec();
  balRec.setArAccount(acntRec);
  balRec.setBalPeriod(bal.getBalPeriod());
  balRec.setBalYear(bal.getBalYear());
  balRec.setBfwdDocAmount(bal.getBfwdDocAmount());
  balRec.setBfwdLocalAmount(bal.getBfwdLocalAmount());
  balRec.setCfwdDocAmount(bal.getCfwdDocAmount());
  balRec.setCfwdLocalAmount(bal.getCfwdLocalAmount());
  balRec.setCreated(bal.getCreated());
  if(bal.getCreatedBy() != null){
   UserRec crUsr = this.usrDM.getUserRecPvt(bal.getCreatedBy());
   balRec.setCreatedBy(crUsr);
  }
  balRec.setId(bal.getId());
  balRec.setPeriodCreditAmount(bal.getPeriodCreditAmount());
  balRec.setPeriodDebitAmount(bal.getPeriodDebitAmount());
  balRec.setPeriodDocAmount(bal.getPeriodDocAmount());
  balRec.setPeriodLocalAmount(bal.getPeriodLocalAmount());
  balRec.setPeriodTurnover(bal.getPeriodTurnover());
  if(bal.getUpdateBy() != null){
   UserRec upDtUsr = this.usrDM.getUserRecPvt(bal.getUpdateBy());
   balRec.setUpdateBy(upDtUsr);
   balRec.setUpdateDate(bal.getUpdateDate());
  }
  return balRec;
 }
    
 public ArAccountRec getArAccountRecPvt(ArAccount arAcnt){
  ArAccountRec arAcntRec = this.buildArAccountRec(arAcnt);
  return arAcntRec;
 }
    private ArrayList<ArAccount> getArAccountByActCode(ArAccountRec account) throws BacException {
        try {
            Query q = em.createNamedQuery("getAccountNumber");

            q.setParameter("actCd", account.getArAccountCode());

            List                    rs   = q.getResultList();
            ArrayList<ArAccount>    lst  = new ArrayList<>();
            ListIterator<ArAccount> acIt = rs.listIterator();

            while (acIt.hasNext()) {
                ArAccount ac = acIt.next();

                lst.add(ac);
            }

            return lst;
        } catch (IllegalArgumentException ex) {
            throw new BacException("Get Account by Account number nor found", "AR:01");
        }
    }

    public boolean accountNumberExists(ArAccountRec account) throws BacException {
        try {
            ArrayList<ArAccount> acs = getArAccountByActCode(account);

         return !(acs == null || acs.isEmpty());
        } catch (IllegalArgumentException ex) {
            throw new BacException("Get Account by Account number nor found", "AR:01");
        }
    }

    /**
     * Create an Accounts Receiveable account
     * @param account
     * @param usr
     * @param actUsr
     * @param page
     * @return
     * @throws BacException 
     */
    public ArAccountRec createArAccount(ArAccountRec account, UserRec usr, UserRec actUsr, String page) throws BacException {
        LOGGER.log(INFO, "ArAccountDM createArAccount called");

        if ((usr == null) || (usr.getId() == null)) {
            throw new BacException("AR account create called with no user", "AR_USR_01");
        }

        User user = em.find(User.class, usr.getId(), OPTIMISTIC);
        ArAccount ac   = this.buildArAccount(account,page);
        CompanyBasic comp = em.find(CompanyBasic.class, account.getCompany().getId(), OPTIMISTIC);
        ac.setCompany(comp);
        account.setId(ac.getId());

        if (ac != null) {
            return account;
        } else {
            return account;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<ArAccountRec> getAccountsBySurName(String surName) {
  LOGGER.log(INFO, "getAccountsBySurName called with family name {0}", surName);
  List<ArAccountRec> actList = new ArrayList<>();
  Query q = em.createNamedQuery("getActsByFamilyNamePart");
  q.setParameter("fname", surName);
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   ArAccount actDb = (ArAccount)li.next();
   ArAccountRec actRec = this.buildArAccountRec(actDb);
   actList.add(actRec);
  }
  LOGGER.log(INFO, "getAccountsBySurName returns {0} accounts",actList.size());
  return actList;
 }

 public List<ArAccountRec> getArAccountsForCompany(CompanyBasicRec comp){
  LOGGER.log(INFO, "getArAccountsByTradingNamePart called with company id {0}", comp.getId());
  List<ArAccountRec> actList = new ArrayList<>();
  TypedQuery q = em.createNamedQuery("getActsForCompany",ArAccount.class);
  q.setParameter("compId", comp.getId());
  List<ArAccount> rs = q.getResultList();
  LOGGER.log(INFO, "Query returned {0}", rs);
  if(rs == null || rs.isEmpty()){
   return null;
  }
  ListIterator<ArAccount> li = rs.listIterator();
  while(li.hasNext()){
   ArAccount actDb = li.next();
   
   ArAccountRec actRec = buildArAccountRec(actDb);
   LOGGER.log(INFO, "ArAccount id: {0}", actRec.getId());
   actList.add(actRec);
  }
  LOGGER.log(INFO, "getAccountsForCompany returns {0} accounts",actList.size());
  return actList;
 }
 
 public List<ArAccountRec> getArAccountsForPtnr(PartnerBaseRec ptnr){
  LOGGER.log(INFO, "ArAccntDM.getArAccountsForPtnr called with ptnr {0}", ptnr.getName());
  TypedQuery q = em.createNamedQuery("getArActsForPtnr",ArAccount.class);
  q.setParameter("ptnrId", ptnr.getId());
  List<ArAccount> rs = q.getResultList();
  LOGGER.log(INFO, "Accounts found {0}", rs);
  if(rs == null || rs.isEmpty()){
   LOGGER.log(INFO, "No accounts for partner");
   return null;
  }
  List<ArAccountRec> returnList = new ArrayList<>();
  for(ArAccount curr:rs){
   ArAccountRec rec = this.buildArAccountRec(curr);
   returnList.add(rec);
  }
  LOGGER.log(INFO, "ArAccountDM.getArAccountsForPtnr returns acs {0}", returnList);
  return returnList;
  
 }
 
 public List<ArAccountRec> getArAccountsByAcntName(String acntName){
  LOGGER.log(INFO, "getArAccountsByTradingNamePart called with acount name {0}", acntName);
  List<ArAccountRec> actList = new ArrayList<>();
  TypedQuery q = em.createNamedQuery("getActsByActName",ArAccount.class);
  q.setParameter("actName", acntName);
  List<ArAccount> rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  for(ArAccount a:rs){
   ArAccountRec rec = this.buildArAccountRec(a);
   actList.add(rec);
  }
  LOGGER.log(INFO, "Number of accounts returned {0}", actList.size());
  return actList;
  
 }
 public List<ArAccountRec> getArAccountsByTradingNamePart(String tradingName) {
  LOGGER.log(INFO, "getArAccountsByTradingNamePart called with trading name {0}", tradingName);
  List<ArAccountRec> actList = new ArrayList<>();
  Query q = em.createNamedQuery("getActsByTradingNamePart");
  q.setParameter("tname", tradingName);
  List rs = q.getResultList();
  LOGGER.log(INFO, "Query returned {0}", rs);
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   ArAccount actDb = (ArAccount)li.next();
   ArAccountRec actRec = this.buildArAccountRec(actDb);
   actList.add(actRec);
  }
  LOGGER.log(INFO, "getAccountsBySurName returns {0} accounts",actList.size());
  return actList;
 }

 public List<ArAccountRec> getAccountsByActNumPart(String actNum) {
  LOGGER.log(INFO, "ArAccountDM.getAccountsByActNumPart called with {0}", actNum);
  List<ArAccountRec> actList = new ArrayList<>();
  TypedQuery q = em.createNamedQuery("getActsByActNumPart",ArAccount.class);
  q.setParameter("actCd", actNum);
  List<ArAccount> rs = q.getResultList();
  LOGGER.log(INFO, "Query returned {0}", rs);
  if(rs == null || rs.isEmpty()){
   return null;
  }
  ListIterator<ArAccount> li = rs.listIterator();
  while(li.hasNext()){
   ArAccount actDb = (ArAccount)li.next();
   ArAccountRec actRec = buildArAccountRec(actDb);
   LOGGER.log(INFO, "Ar Account by acnt num id {0}", actRec.getId());
   actList.add(actRec);
  }
  LOGGER.log(INFO, "getAccountsByActNumPart returns {0} accounts",actList.size());
  
  return actList;
 }
 
 
 public List<ArAccountRec> getAccountsByActNumPart(CompanyBasicRec compRec, String actNum) {
  LOGGER.log(INFO, "ArAccountDM.getAccountsByActNumPart called with comp{0}", actNum);
  List<ArAccountRec> actList = new ArrayList<>();
  
  if(StringUtils.isBlank(actNum)){
   return getArAccountsForCompany(compRec);
  }
  actNum = StringUtils.remove(actNum, "%");
  actNum = actNum + "%";
  TypedQuery q = em.createNamedQuery("getActsByCompActNumPart", ArAccount.class);
  q.setParameter("compId", compRec.getId());
  q.setParameter("actCd",actNum);
  
  List<ArAccount> rs = q.getResultList();
  LOGGER.log(INFO, "query with acnt num returned {0}", rs);
  if(rs.isEmpty()){
   return null; //getArAccountsForCompany(compRec);
  }
  for(ArAccount a:rs){
   ArAccountRec curr = this.buildArAccountRec(a);
   actList.add(curr);
  }
  return actList;
 }

 public ArAccountRec updateArAccount(ArAccountRec acnt,  String page) 
         throws BacException {
  LOGGER.log(INFO, "updateArAccount called with acnt {0} page {1}", new Object[]
  {acnt.getArAccountCode(),page});
  if(!trans.isActive()){
   trans.begin();
  }
  ArAccount acntDb = this.buildArAccount(acnt, page);
  if(acnt.getId() == null){
   acnt.setId(acntDb.getId());
  }
  if(acnt.getArAccountBanks() != null){
   
  
  List<ArBankAccount> acntBanks = acntDb.getArAccountBanks();
  if(acntBanks == null){
   acntBanks = new ArrayList<>();
  }
  ListIterator<ArBankAccountRec> arBnkLi = acnt.getArAccountBanks().listIterator();
  while(arBnkLi.hasNext()){
   ArBankAccountRec currArBnk = arBnkLi.next();
   LOGGER.log(INFO, "Build Ar bank name {0} bank ac id {1}", 
     new Object[]{currArBnk.getAccountName(), currArBnk.getBankAccount().getId()});
   ArBankAccount arBnk = this.buildArBank(currArBnk, page);
   if(currArBnk.getId() == null){
    currArBnk.setId(arBnk.getId());
   }
   BankAccount bnkAc = em.find(BankAccount.class,currArBnk.getBankAccount().getId());
   if(currArBnk.getBankAccount().getId() == null){
    currArBnk.getBankAccount().setId(bnkAc.getId());
   }
   arBnk.setBankAccount(bnkAc);
   if(acntBanks.isEmpty()){
    acntBanks.add(arBnk);
   }else{
    boolean foundArBnk = false;
    
    ListIterator<ArBankAccount> bankRecAcLi =  acntBanks.listIterator();
    while(bankRecAcLi.hasNext() && !foundArBnk){
     ArBankAccount arBnkChk = bankRecAcLi.next();
     if(Objects.equals(arBnkChk.getId(),arBnk.getId() )){
      bankRecAcLi.set(arBnk);
     }
     
    }
    if(!foundArBnk){
     User usr;
     if(acnt.getCreatedBy().getId() != null){
      usr = em.find(User.class, acnt.getCreatedBy().getId());
     }else{
      usr = em.find(User.class, acnt.getChangedBy().getId());
     }
      AuditArAccount aud = this.buildAuditArAccount(acntDb, usr, page, 'U');
      aud.setFieldName("AR_ACNT_BNK");
      aud.setNewValue(arBnk.getAccountName());
      aud.setOldValue("None");
      bankRecAcLi.set(arBnk); 
      currArBnk.setId(arBnk.getId());
    }
    
   }
  }
  acntDb.setArAccountBanks(acntBanks);
  }
 LOGGER.log(INFO, "End of Ar Account update - banks {0}",acnt.getArAccountBanks());
 trans.commit();
 return acnt; 
 }

 public List<FiArPeriodBalanceRec> getArPerBals(ArAccountRec acntRec, int year){
  LOGGER.log(INFO, "getArPerBals called with accnt {0}", acntRec.getArAccountCode());
  List<FiArPeriodBalanceRec> retList = new ArrayList<>();
  Query q = em.createNamedQuery("arAnnBals");
  q.setParameter("acntId", acntRec.getId());
  q.setParameter("yr", year);
  List bals = q.getResultList();
  if(bals == null || bals.isEmpty()){
   LOGGER.log(INFO, "No period balances build");
   int per = 0;
   for( int i=0;i<12;i++){
    per++;
    FiArPeriodBalanceRec curr = new FiArPeriodBalanceRec();
    curr.setArAccount(acntRec);
    curr.setBalPeriod(per);
    curr.setBalYear(year);
    retList.add(curr);
   // FiArPeriodBalance newBal = this.buildFiARPeriodBalance(curr);
   }
     
  }else{
   for(Object o:bals){
    FiArPeriodBalanceRec curr = this.buildFiARPeriodBalanceRec(acntRec, (FiArPeriodBalance)o);
    retList.add(curr);
   }
  }
  
  return retList;
 }
 
 public FiArPeriodBalance setArPeriodBalance(FiArPeriodBalance perBal, DocLineAr arLine, 
         String updateType,String page) throws BacException {
  double perDebitAmt = perBal.getPeriodDebitAmount();
  double perCreditAmt = perBal.getPeriodCreditAmount();
  double perDocAmnt = perBal.getPeriodDocAmount();
  double perLocAmnt = perBal.getPeriodLocalAmount();
  double perTurnover = perBal.getPeriodTurnover();
  double bfwdDocAmnt = perBal.getBfwdDocAmount();
  double bfwdLocAmnt = perBal.getBfwdLocalAmount();
  double cfwdDocAmnt = perBal.getCfwdDocAmount();
  double cfwdLocAmnt = perBal.getCfwdLocalAmount();
  
  if(updateType.equalsIgnoreCase("INV")){
   // invoice balance update -- Debit
   perDebitAmt = perDebitAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   perTurnover = perTurnover + arLine.getDocAmount();
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  }else if(updateType.equalsIgnoreCase("REV_DR")){
   // invoice balance update -- Debit
   perDebitAmt = perDebitAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   perTurnover = perTurnover + arLine.getDocAmount();
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  }else if(updateType.equalsIgnoreCase("CRN")){
   // credit note balance update  --Credit
   perCreditAmt = perCreditAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   perTurnover = perTurnover - arLine.getDocAmount();
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  }else if(updateType.equalsIgnoreCase("REV_CR")){
   // credit note balance update  --Credit
   perCreditAmt = perCreditAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   perTurnover = perTurnover - arLine.getDocAmount();
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  }
  else if(updateType.equalsIgnoreCase("PAYMENT")){
   // Cash payment
   perCreditAmt = perCreditAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  } else if(updateType.equalsIgnoreCase("REFUND")){
   // Customer refund
   perDebitAmt = perDebitAmt + arLine.getDocAmount();
   perDocAmnt = perDebitAmt - perCreditAmt;
   perLocAmnt = perDocAmnt;
   cfwdDocAmnt = bfwdDocAmnt + perDocAmnt;
   cfwdLocAmnt = bfwdLocAmnt + perLocAmnt;
  }
  perBal.setPeriodDebitAmount(perDebitAmt);
  perBal.setPeriodCreditAmount(perCreditAmt);
  perBal.setPeriodDocAmount(perDocAmnt);
  perBal.setPeriodLocalAmount(perLocAmnt);
  perBal.setPeriodTurnover(perTurnover);
  perBal.setBfwdDocAmount(bfwdDocAmnt);
  perBal.setBfwdLocalAmount(bfwdLocAmnt);
  perBal.setCfwdDocAmount(cfwdDocAmnt);
  perBal.setCfwdLocalAmount(cfwdLocAmnt);
  em.merge(perBal);
  return perBal;
 }

 public List<FiArPeriodBalance> addAnnualAcBalances(ArAccount arAccount, List<FiArPeriodBalance> perBalList ,
          int fiscalYear) throws BacException {
  LOGGER.log(INFO, "addAnnualAcBalances called with account {0} comp {1} year {3}", new Object[]{
   arAccount,fiscalYear });
  boolean newtrans = false;
  if(!trans.isActive()){
   trans.begin();
   newtrans = true;
  }
  if(perBalList == null){
   perBalList = new ArrayList<>();
  }
  
  CompanyBasic comp = arAccount.getCompany();
  FisPeriodRule perRule = comp.getChartOfAccounts().getPeriodRule();
  int numPer = perRule.getNumPeriods();
  for(int i = 0; i <= numPer;i++){
   LOGGER.log(INFO, "Create period bal for Period {0} year {1}", new Object[]{i,fiscalYear});
   FiArPeriodBalance perBal = new FiArPeriodBalance();
   perBal.setArAccount(arAccount);
   perBal.setBalYear(fiscalYear);
   perBal.setBalYear(i);
   em.persist(perBal);
   perBalList.add(perBal);
  }
  if(newtrans){
  //trans.commit();
  }
  return perBalList;
 }

 
 public ArAccount addPeriodBalance(FiArPeriodBalance perBalance, ArAccount arAccount) throws BacException {
  LOGGER.log(INFO, "addPeriodBalance called with perbal {0} arAccount {1}", new Object[]{
   perBalance,arAccount });
   em.persist(perBalance);
   List<FiArPeriodBalance> balances = arAccount.getArPeriodBalances();
   balances.add(perBalance);
   arAccount.setArPeriodBalances(balances);
  return arAccount;
 }
 
 
 
 

}


