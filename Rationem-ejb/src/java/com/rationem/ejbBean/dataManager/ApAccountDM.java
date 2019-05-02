/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.arap.FiApPeriodBalanceRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.entity.audit.AuditApAccount;
import com.rationem.entity.audit.AuditFiApPeriodBalance;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.arap.FiApPeriodBalance;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.user.User;
import com.rationem.exception.BacException;
import com.rationem.helper.ApArAccountBal;
import com.rationem.util.GenUtilServer;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class ApAccountDM {
 private static final Logger LOGGER =
   Logger.getLogger("com.rationem.ejbBean.dataManager.ApAccountDM"); 
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
 private FiMasterRecordDM fiMastDM;
 
 @EJB
 private SysBuffer sysBuff;
    
 
 private EntityManager em;
 private EntityTransaction trans;
 
 @PostConstruct  
 private void init(){
  FacesContext fc = FacesContext.getCurrentInstance();
  String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
  HashMap<String, String> properties = new HashMap<String, String>();
  properties.put("tenantId", tenantId);
  properties.put("eclipselink.session-name", "sessionName"+tenantId);
  em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
  trans = em.getTransaction();
 }
 
 private ApAccount buildApAccount(ApAccountRec rec, String view){
  
  ApAccount acnt;
  boolean newAcnt = false;
  boolean changed = false;
  User crUsr = em.find(User.class, rec.getCreatedBy().getId());
  if(rec.getId() == null){
   acnt = new ApAccount();
   
   acnt.setCreatedBy(crUsr);
   acnt.setCreatedOn(rec.getCreatedOn());
   newAcnt = true;
   em.persist(acnt);
  }else{
   acnt = em.find(ApAccount.class, rec.getId());
  }
  
  if(newAcnt){
   if(rec.getAccountAddress() != null){
    
    Address addr;
    if(rec.getAccountAddress().getId() == null){
     addr = this.masterDataDm.buildAddressDM(rec.getAccountAddress(), view);
    }else{
     addr = em.find(Address.class, rec.getAccountAddress().getId());
    }
    
    acnt.setAccountAddress(addr);
   }
   acnt.setAccountBalance(rec.getAccountBalance());
   if(rec.getAccountClerk() != null){
    PartnerPerson resp = em.find(PartnerPerson.class, rec.getAccountClerk().getId());
    acnt.setAccountClerk(resp);
   }
   acnt.setAccountCode(rec.getAccountCode());
   acnt.setAccountName(rec.getAccountName());
   acnt.setApAccntEmail(rec.getAcntEmail());
   acnt.setAcntNote(rec.getAcntNote());
   if(rec.getApAccountFor() != null){
    PartnerBase ptnr = em.find(PartnerBase.class, rec.getApAccountFor().getId());
    acnt.setApAccountFor(ptnr);
   }
   acnt.setBalBucket1(rec.getBalBucket1());
   acnt.setBalBucket2(rec.getBalBucket2());
   acnt.setBalBucket3(rec.getBalBucket3());
   acnt.setBalBucket4(rec.getBalBucket4());
   CompanyBasic comp = this.sysBuff.getComp(rec.getCompany());
   acnt.setCompany(comp);
   if(rec.getPaymentTerms() != null){
    PaymentTerms payTerm = em.find(PaymentTerms.class, rec.getPaymentTerms().getId());
    acnt.setPaymentTerms(payTerm);
   }
   if(rec.getPaymentType() != null){
    PaymentType payTyp = em.find(PaymentType.class, rec.getPaymentType().getId());
    acnt.setPaymentType(payTyp);
   }
   if(rec.getReconciliationAc() != null){
    FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, rec.getReconciliationAc().getId());
    acnt.setReconciliationAc(glAcnt);
   }
   if(rec.getSortOrder() != null){
    SortOrder s = em.find(SortOrder.class, rec.getSortOrder().getId());
    acnt.setSortOrder(s);
   }
   AuditApAccount aud = this.buildAuditApAccount(acnt, crUsr, view, 'I');
   
  }else{
   User chUsr = em.find(User.class, rec.getChangedBy().getId());
   
   if((rec.getAccountAddress() == null && acnt.getAccountAddress() != null) ||
     (rec.getAccountAddress() != null && acnt.getAccountAddress() == null) ||
     !Objects.equals(rec.getAccountAddress().getId(), acnt.getAccountAddress().getId())){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_ADDR");
    aud.setOldValue(acnt.getAccountAddress().getPostCode());
    aud.setNewValue(rec.getAccountAddress().getPostCode());
    Address addr = em.find(Address.class, rec.getAccountAddress().getAddrRef());
    acnt.setAccountAddress(addr);
    changed = true;
   }
   if((rec.getAccountClerk() == null && acnt.getAccountClerk() != null) ||
     (rec.getAccountClerk() != null && acnt.getAccountClerk() == null) || 
     (rec.getAccountClerk() != null && acnt.getAccountClerk() != null &&
     !Objects.equals(rec.getAccountClerk().getId(), acnt.getAccountClerk().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_CLRK");
    aud.setOldValue(acnt.getAccountClerk().getNameStructured());
    aud.setNewValue(rec.getAccountClerk().getNameStructured());
    PartnerPerson clerk = em.find(PartnerPerson.class, rec.getAccountClerk().getId());
    acnt.setAccountClerk(clerk);
    changed = true;
   }
   
   if(!Objects.equals(rec.getAccountCode(),acnt.getAccountCode())){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_CD");
    aud.setOldValue(acnt.getAccountCode());
    aud.setNewValue(rec.getAccountCode());
    acnt.setAccountCode(rec.getAccountCode());
    changed = true;
   }
   
   if(!Objects.equals(rec.getAccountName(),acnt.getAccountName())){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_NM");
    aud.setOldValue(acnt.getAccountName());
    aud.setNewValue(rec.getAccountName());
    acnt.setAccountName(rec.getAccountName());
    changed = true;
   }
   
   if(!Objects.equals(rec.getAcntEmail(),acnt.getApAccntEmail())){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_NM");
    aud.setOldValue(acnt.getApAccntEmail());
    aud.setNewValue(rec.getAcntEmail());
    acnt.setApAccntEmail(rec.getAcntEmail());
    changed = true;
   }
   
   if(!StringUtils.equals(acnt.getAcntNote(), rec.getAcntNote())){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_NT");
    aud.setOldValue(acnt.getAcntNote());
    aud.setNewValue(rec.getAcntNote());
    acnt.setAcntNote(rec.getAcntNote());
    changed = true;
   }
   
   if((rec.getApAccountBanks() == null && acnt.getApAccountBanks() != null) ||
     (rec.getApAccountBanks() != null && acnt.getApAccountBanks() == null) ||
     (rec.getApAccountBanks() != null && acnt.getApAccountBanks() != null)){
    // check each bank to see if new account
    for(ArBankAccountRec arBnkRec:rec.getApAccountBanks()){
     boolean foundBnk = false;
     for(ArBankAccount arBnk:acnt.getApAccountBanks()){
      if(Objects.equals(arBnkRec.getId(), arBnk.getId())){
       foundBnk = true;
      }
     }
     if(!foundBnk){
      // Rec has additional bank so need to add
      ArBankAccount arBnk;
      if(arBnkRec.getId() != null){
        arBnk = em.find(ArBankAccount.class, arBnkRec.getId());
      }else{
        arBnk = this.bankDM.buildBankAccountArApPvt(arBnkRec, view);
      }
      acnt.getApAccountBanks().add(arBnk);
      AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
      aud.setFieldName("AP_ACNT_BNK_ADD");
      aud.setNewValue(arBnk.getBankAccount().getAccountNumber());
      changed = true;
     }
    }
    ListIterator<ArBankAccount> bnkLi = acnt.getApAccountBanks().listIterator();
    while(bnkLi.hasNext()){
     ArBankAccount currBnkAc = bnkLi.next();
     boolean foundRec = false;
     for(ArBankAccountRec currRec:rec.getApAccountBanks()){
      if(Objects.equals(currRec.getId(), currBnkAc.getId())){
       foundRec = true;
      }
     }
     if(!foundRec){
      bnkLi.remove();
      AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
      aud.setFieldName("AP_ACNT_BNK_DEL");
      aud.setNewValue(currBnkAc.getBankAccount().getAccountNumber());
      changed = true;
     }
    }
   }
   
   if((rec.getApAccountFor() == null && acnt.getApAccountFor() != null) || 
     (rec.getApAccountFor() != null && acnt.getApAccountFor() == null) ||
     (rec.getApAccountFor() != null && 
     !Objects.equals(rec.getApAccountFor().getId(), acnt.getApAccountFor().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_PTNR");
    aud.setOldValue(acnt.getApAccountFor().getTradingName());
    aud.setNewValue(rec.getApAccountFor().getTradingName());
    PartnerBase ptnr = em.find(PartnerBase.class, rec.getApAccountFor().getId());
    acnt.setApAccountFor(ptnr);
    changed = true;
   }
   
   if((rec.getCompany() == null && acnt.getCompany() != null) || 
      (rec.getCompany() != null && acnt.getCompany() == null) ||
      (rec.getCompany() != null && 
     !Objects.equals(acnt.getCompany().getId(), rec.getCompany().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_COMP");
    aud.setOldValue(acnt.getCompany().getNumber());
    aud.setNewValue(rec.getCompany().getReference());
    CompanyBasic comp = this.sysBuff.getComp(rec.getCompany());
    acnt.setCompany(comp);
    changed = true;
   }
   
   if((rec.getPaymentTerms() == null && acnt.getPaymentTerms() != null)||
      (rec.getPaymentTerms() != null && acnt.getPaymentTerms() == null)||
      (rec.getPaymentTerms() != null && 
       !Objects.equals(acnt.getPaymentTerms().getId(), rec.getPaymentTerms().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_PAYTRM");
    aud.setOldValue(acnt.getPaymentTerms().getPayTermsCode());
    aud.setNewValue(rec.getPaymentTerms().getPayTermsCode());
    PaymentTerms payTerm = em.find(PaymentTerms.class, rec.getPaymentTerms().getId());
    acnt.setPaymentTerms(payTerm);
    changed = true;
   }
   
   if((rec.getPaymentType() == null && rec.getPaymentType() != null) ||
      (rec.getPaymentType() != null && rec.getPaymentType() == null) ||
      (rec.getPaymentType() != null &&
       !Objects.equals(acnt.getPaymentType().getId(), rec.getPaymentType().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_PAYTY");
    aud.setOldValue(acnt.getPaymentType().getPayTypeCode());
    aud.setNewValue(rec.getPaymentType().getPayTypeCode());
    PaymentType payType = em.find(PaymentType.class, rec.getPaymentType().getId());
    acnt.setPaymentType(payType);
    changed = true;
   }
   
   if((rec.getReconciliationAc() == null && acnt.getReconciliationAc() != null) ||
      (rec.getReconciliationAc() != null && 
      !Objects.equals(acnt.getReconciliationAc().getId(), rec.getReconciliationAc().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_REC_ACNT");
    aud.setOldValue(acnt.getReconciliationAc().getCoaAccount().getRef());
    aud.setNewValue(rec.getReconciliationAc().getCoaAccount().getRef());
    FiGlAccountComp recAcnt = em.find(FiGlAccountComp.class, rec.getReconciliationAc().getId());
    acnt.setReconciliationAc(recAcnt);
    changed = true;
   }
   
   if((rec.getSortOrder() == null && acnt.getSortOrder() != null ) ||
      (rec.getSortOrder() != null && 
       !Objects.equals(acnt.getSortOrder().getId(), rec.getSortOrder().getId()))){
    AuditApAccount aud = this.buildAuditApAccount(acnt, chUsr, view, 'U');
    aud.setFieldName("AP_ACNT_SORT");
    aud.setOldValue(acnt.getSortOrder().getName());
    aud.setNewValue(rec.getSortOrder().getName());
    SortOrder srt = em.find(SortOrder.class, rec.getSortOrder().getId());
    acnt.setSortOrder(srt);
    changed = true;
   }
   
   if(changed){
    acnt.setChangedBy(chUsr);
    acnt.setChangedOn(rec.getChangedOn());
   }
  }
  return acnt;
 }
 
 private AuditApAccount buildAuditApAccount(ApAccount acnt, User usr, String pg, char action){
  AuditApAccount aud = new AuditApAccount();
  aud.setAcnt(acnt);
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(action);
  em.persist(aud);
  return aud;
 }
 
 private AuditFiApPeriodBalance buildAuditFiApPeriodBalance(FiApPeriodBalance bal, User usr, String pg, char action){
  AuditFiApPeriodBalance aud = new AuditFiApPeriodBalance();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setPeriodBalance(bal);
  aud.setSource(pg);
  aud.setUsrAction(action);
  em.persist(aud);
  return aud;
 }
 
 private ApAccountRec buildApAccountRec(ApAccount acnt){
  ApAccountRec rec = new ApAccountRec();
  rec.setId(acnt.getId());
  UserRec crUsr = this.usrDM.getUserRecPvt(acnt.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreatedOn(acnt.getCreatedOn());
  if(acnt.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(acnt.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(acnt.getChangedOn());
  }
  rec.setAcntNote(acnt.getAcntNote());
  if(acnt.getAccountAddress() != null){
   AddressRec addr = this.masterDataDm.getAddressRec(acnt.getAccountAddress());
   rec.setAccountAddress(addr);
  }
  rec.setAccountBalance(acnt.getAccountBalance());
  LOGGER.log(INFO, "Build Ap account acnt bal from DB {0} in rec {1}", 
    new Object[]{acnt.getAccountBalance(),rec.getAccountBalance()});
  rec.setAccountCode(acnt.getAccountCode());
  if(acnt.getAccountClerk() != null){
   PartnerPersonRec clrk = masterDataDm.buildPartnerPersonRecPvt(acnt.getAccountClerk());
   rec.setAccountClerk(clrk);
  }
  if(acnt.getApAccountFor() != null){
   String ptnrType = acnt.getApAccountFor().getClass().getSimpleName();
   if(ptnrType.equals("PartnerCorporate")){
    PartnerCorporateRec corp = this.masterDataDm.getPartnerCorporateRec((PartnerCorporate)acnt.getApAccountFor());
    rec.setApAccountFor(corp);
   }else{
    PartnerPersonRec pers = masterDataDm.buildPartnerPersonRecPvt((PartnerPerson) acnt.getApAccountFor());
    rec.setApAccountFor(pers);
   }
   CompanyBasicRec comp = this.sysBuff.getCompanyById(acnt.getCompany().getId());
   rec.setCompany(comp);
   rec.setId(acnt.getId());
   if(acnt.getPaymentTerms() != null){
    PaymentTermsRec pt = this.sysBuff.getPaymentTerms(acnt.getPaymentTerms());
    rec.setPaymentTerms(pt);
   }
   if(acnt.getPaymentType() != null){
    PaymentTypeRec pt = this.sysBuff.getPaymentType(acnt.getPaymentType());
    rec.setPaymentType(pt);
   }
   if(acnt.getReconciliationAc() != null){
    FiGlAccountCompRec reconAc = this.fiMastDM.buildFiCompGlAccountRecPvt(acnt.getReconciliationAc());
    rec.setReconciliationAc(reconAc);
   }
   if(acnt.getSortOrder() != null){
    SortOrderRec sort = sysBuff.getSortOrderById(acnt.getSortOrder().getId());
    rec.setSortOrder(sort);
    
   }
   
  }
  return rec;
  
 }
 
 public FiApPeriodBalance buildFiApPeriodBalance(FiApPeriodBalanceRec perBalRec, String view){
  FiApPeriodBalance perBal = null;
  boolean newBal = false;
  boolean changedBal = false;
  
  if(perBalRec.getId() == null){
   perBal = new FiApPeriodBalance();
   User crUser = em.find(User.class, perBalRec.getCreatedBy().getId());
   perBal.setCreatedBy(crUser);
   perBal.setCreated(perBalRec.getCreated());
   em.persist(perBal);
   AuditFiApPeriodBalance aud = this.buildAuditFiApPeriodBalance(perBal, crUser, view, 'I');
   aud.setNewValue(perBalRec.getBalYearPeriod());
   newBal = true;
  }else{
   perBal = em.find(FiApPeriodBalance.class, perBalRec.getId());
  }
  
  if(newBal){
   ApAccount acnt = em.find(ApAccount.class, perBalRec.getApAccount().getId());
   perBal.setApAccount(acnt);
   perBal.setBalPeriod(perBalRec.getBalPeriod());
   perBal.setBalYear(perBalRec.getBalYear());
   perBal.setPeriodCreditAmount(perBalRec.getPeriodCreditAmount());
   perBal.setPeriodDebitAmount(perBalRec.getPeriodDebitAmount());
   perBal.setPeriodDocAmount(perBalRec.getPeriodDocAmount());
   perBal.setPeriodLocalAmount(perBalRec.getPeriodDocAmount());
  }else{
   // changed
   User chUsr = em.find(User.class, perBalRec.getUpdateBy().getId());
   String amnt;
   
   if(perBalRec.getPeriodCreditAmount() != perBal.getPeriodCreditAmount()){
    AuditFiApPeriodBalance aud = this.buildAuditFiApPeriodBalance(perBal, chUsr, view, 'I');
    aud.setFieldName("PER_BAL_CR");
    amnt= GenUtilServer.formatNumberLocDp(perBalRec.getPeriodCreditAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setNewValue(amnt);
    amnt= GenUtilServer.formatNumberLocDp(perBal.getPeriodCreditAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setOldValue(amnt);
    perBal.setPeriodCreditAmount(perBalRec.getPeriodCreditAmount());
    changedBal = true;
   }
   if(perBalRec.getPeriodDebitAmount() != perBal.getPeriodDebitAmount()){
    AuditFiApPeriodBalance aud = this.buildAuditFiApPeriodBalance(perBal, chUsr, view, 'I');
    aud.setFieldName("PER_BAL_DR");
    amnt= GenUtilServer.formatNumberLocDp(perBalRec.getPeriodDebitAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setNewValue(amnt);
    amnt= GenUtilServer.formatNumberLocDp(perBal.getPeriodDebitAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setOldValue(amnt);
    perBal.setPeriodDebitAmount(perBalRec.getPeriodDebitAmount());
    changedBal = true;
   }
   if(perBalRec.getPeriodDocAmount() != perBal.getPeriodDocAmount()){
    AuditFiApPeriodBalance aud = this.buildAuditFiApPeriodBalance(perBal, chUsr, view, 'I');
    aud.setFieldName("PER_BAL_DOC");
    amnt= GenUtilServer.formatNumberLocDp(perBalRec.getPeriodDocAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setNewValue(amnt);
    amnt= GenUtilServer.formatNumberLocDp(perBal.getPeriodDocAmount(), perBalRec.getApAccount().getCompany().getLocale());
    aud.setOldValue(amnt);
    perBal.setPeriodDocAmount(perBalRec.getPeriodDocAmount());
    changedBal = true;
   }
   if(changedBal){
    perBal.setUpdateBy(chUsr);
    perBal.setUpdateDate(perBal.getUpdateDate());
    
   }
  }
  
  return perBal;
 }
 
 public FiApPeriodBalanceRec buildFiApPeriodBalanceRec(FiApPeriodBalance perBal){
  
  FiApPeriodBalanceRec balRec = new FiApPeriodBalanceRec();
  balRec.setBalPeriod(perBal.getBalPeriod());
  balRec.setBalYear(perBal.getBalYear());
  balRec.setCreated(perBal.getCreated());
  UserRec crUsr = this.usrDM.getUserRecPvt(perBal.getCreatedBy());
  balRec.setCreatedBy(crUsr);
  balRec.setId(perBal.getId());
  balRec.setPeriodCreditAmount(perBal.getPeriodCreditAmount());
  balRec.setPeriodDebitAmount(perBal.getPeriodDebitAmount());
  balRec.setPeriodDocAmount(perBal.getPeriodDocAmount());
  balRec.setPeriodLocalAmount(perBal.getPeriodTurnover());
  balRec.setBfwdDocAmount(perBal.getBfwdDocAmount());
  balRec.setBfwdLocalAmount(perBal.getBfwdLocalAmount());
  balRec.setCfwdDocAmount(perBal.getCfwdDocAmount());
  balRec.setCfwdLocalAmount(perBal.getCfwdLocalAmount());
  if(perBal.getUpdateBy() != null){
   UserRec chUsr = usrDM.getUserRecPvt(perBal.getUpdateBy());
   balRec.setUpdateBy(chUsr);
   balRec.setUpdateDate(perBal.getUpdateDate());
  }
  
  return balRec;
 }
 
 public List<ApAccountRec> apAccountsAllCrossComp(List<CompanyBasicRec> comps){
  LOGGER.log(INFO, "ApAcntDM.apAccountsAllCrossComp called with {0}", comps);
  if(comps == null || comps.isEmpty()){
   return null;
  }
  List<ApAccountRec> retList = new ArrayList<>();
 
  List<Long> compIdList = new ArrayList<>();
  for(CompanyBasicRec curr: comps){
   compIdList.add(curr.getId());
  }
 
  TypedQuery q = em.createNamedQuery("apActsByAllCrossComp", ApAccount.class);
  q.setParameter("compIdList", compIdList);
 
  List<ApAccount> apAcnts = q.getResultList();
  LOGGER.log(INFO, "getResult set returns {0}", apAcnts);
  if(apAcnts == null ||apAcnts.isEmpty()){
   LOGGER.log(INFO, "No AP accounts found {0}", apAcnts);
   return null;
  }
  for(ApAccount curr:apAcnts){
   ApAccountRec rec = this.buildApAccountRec(curr);
   retList.add(rec);
  }
  return retList;
 }
 
 public List<ApAccountRec> apAccountsByCodeCrossComp(String code, List<CompanyBasicRec> comps){
  LOGGER.log(INFO, "ApAccountDM apAccountsByCodeCrossComp called with code {0} comps {1}", 
    new Object[]{code,comps});
  
  if(comps ==  null){
   return null;
  }
  
  code = StringUtils.remove(code, "%");
  code = StringUtils.appendIfMissing(code, "%");
  List<Long> compIds = new ArrayList<>();
  for(CompanyBasicRec curr: comps){
   compIds.add(curr.getId());
  }
  TypedQuery q = em.createNamedQuery("apActsByNameXcomp", ApAccount.class);
  q.setParameter("acntCode", code);
  q.setParameter("compIdList",compIds );
  
  List<ApAccount> rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   LOGGER.log(INFO, "Query returned no acs");
   return null;
  }
  List<ApAccountRec> retList = new ArrayList<>();
  for(ApAccount curr:rs){
   ApAccountRec rec = this.buildApAccountRec(curr);
   retList.add(rec);
  }
  return retList;
 }
 
 public List<ApAccountRec> apAccountsForCompAll(CompanyBasicRec comp){
  LOGGER.log(INFO, "apAccountsForCompAll called with comp id {0}", comp.getId());
  Query q = em.createNamedQuery("apActsByCompAll");
  q.setParameter("compId", comp.getId());
  List rs = q.getResultList();
  
  List<ApAccountRec> retList = new ArrayList<>();
  for(Object o:rs){
   ApAccountRec curr = this.buildApAccountRec((ApAccount)o);
   retList.add(curr);
  }
  return retList;
 }
 
 
 public List<ApAccountRec> apAccountsForCompByCode(CompanyBasicRec comp, String code){
  LOGGER.log(INFO, "apAccountsForCompAll called with comp id {0}", comp.getId());
  code = StringUtils.replace(code, "%", "");
  code = StringUtils.trim(code);
  code = StringUtils.appendIfMissing(code, "%");
  
  TypedQuery q = em.createNamedQuery("apActsByName", ApAccount.class);
  q.setParameter("compId", comp.getId());
  q.setParameter("acntCode", code);

  List<ApAccount> rs = q.getResultList();
  LOGGER.log(INFO, "Named query called with compId {0} accountCode {1} returned {2}", new Object[]{
  comp.getId(), code, rs});
  
  List<ApAccountRec> retList = new ArrayList<>();
  for(ApAccount o:rs){
   ApAccountRec curr = this.buildApAccountRec(o);
   retList.add(curr);
  }
  return retList;
 }
 
 public boolean apAccountExistsByActNum(ApAccountRec apAccount) throws BacException {
   LOGGER.log(INFO, "apAccountExistsByActNum called with {0}", apAccount);
  Query q = em.createNamedQuery("apActsByName");
  q.setParameter("acntCode", apAccount.getAccountCode());
  q.setParameter("compId", apAccount.getCompany().getId());
  List rs = q.getResultList();
  boolean rc = false;
  LOGGER.log(INFO, "apAccountExistsByActNum found {0}", rs);
  if(rs != null && !rs.isEmpty()){
   rc = true;
  }
  LOGGER.log(INFO, "acnt found {0}", rc);
  return rc;
 }
 
 public ApAccountRec apAccountPeriodBalances(ApAccountRec acntRec){
  LOGGER.log(INFO, "apAccountPeriodBalances called");
  ApAccount acnt = em.find(ApAccount.class, acntRec.getId());
  LOGGER.log(INFO, "Period balance from DB account {0}", acnt.getApPeriodBalnces());
  if(acnt.getApPeriodBalnces() != null){
   List<FiApPeriodBalanceRec> apRecPerBals = new ArrayList<>();
   for(FiApPeriodBalance currBal:acnt.getApPeriodBalnces()){
    FiApPeriodBalanceRec balRec = this.buildFiApPeriodBalanceRec(currBal);
    apRecPerBals.add(balRec);
   }
   acntRec.setApPeriodBalances(apRecPerBals);
  }
  
  LOGGER.log(INFO, "Period balance 0 from DB account {0}", acnt.getApPeriodBalnces());
  return acntRec;
 }
 
 public List<ApAccountRec> apAccountsForPartner(PartnerBaseRec ptnr){
  LOGGER.log(INFO, "apAcntDM.apAccountsForPartner called with Ptnr ref {0}", ptnr.getRef());
  TypedQuery q = em.createNamedQuery("apActsByPtnr",ApAccount.class);
  q.setParameter("ptnrId", ptnr.getId());
  List<ApAccount> rs = q.getResultList();
  List<ApAccountRec> retList = new ArrayList<>();
  for(ApAccount o:rs){
   LOGGER.log(INFO, "acnt balance from DB obj {0}", o.getAccountBalance());
   ApAccountRec curr = buildApAccountRec(o);
   LOGGER.log(INFO, "acnt balance from rec obj {0}", curr.getAccountBalance());
   retList.add(curr);
  }
  return retList;
  
  
 }
 
public ApAccountRec getApAccountRec(ApAccount acnt){
 ApAccountRec rec = buildApAccountRec(acnt);
 return rec;
}
 
public List<ApArAccountBal> getApBalances(CompanyBasicRec comp){
 LOGGER.log(INFO, "apMgrDM.getApBalances called with company id {0}", comp.getId());
 TypedQuery q = em.createNamedQuery("apBalsbyComp",ApArAccountBal.class);
 q.setParameter("compId", comp.getId());
 List<Object[]> rs = q.getResultList();
 List<ApArAccountBal> bals = new ArrayList<>();
 for(Object[]o:rs){
  LOGGER.log(INFO, "o is {0}", o[0]);
  LOGGER.log(INFO, "o is {0}", o[1]);
  ApArAccountBal bal = new ApArAccountBal();
  bal.setId(Long.parseLong(String.valueOf(o[0])));
  bal.setCode(String.valueOf(o[1]));
  bal.setName(String.valueOf(o[2]));
  bal.setAmount(Double.parseDouble(String.valueOf(o[3])));
  bals.add(bal);
 }
 
 LOGGER.log(INFO, "Bals found {0}", bals);
 return bals;
 
}
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
 public ApAccountRec updateApAccount(ApAccountRec acntRec,String view){
  
  if(!trans.isActive()){
   trans.begin();
  }
   ApAccount acnt = this.buildApAccount(acntRec, view);
   if(acntRec.getId() == null){
    acntRec.setId(acnt.getId());
   }
   LOGGER.log(INFO, "ApAccountDM.updateApAccount returns id {0}", acntRec.getId());
   trans.commit();
  return acntRec;
}

 public boolean updateApAccountBal(ApAccountRec acntRec,String view){
  LOGGER.log(INFO, "apAccountDM.updateApAccountBal called with acnt id {0}", acntRec.getId());
  LOGGER.log(INFO, "on call updateApAccountBal acntRec per bals {0}", acntRec.getApPeriodBalances());
  if(!trans.isActive()){
   trans.begin();
  }
  boolean updated = false;
  ApAccount acnt = em.find(ApAccount.class, acntRec.getId());
  LOGGER.log(INFO, "DB Acnt Bal {0} rec acnt bal {1}", new Object[]{acnt.getAccountBalance(),acntRec.getAccountBalance()});
  User chUsr = em.find(User.class, acntRec.getChangedBy().getId());
  if(acnt.getAccountBalance() != acntRec.getAccountBalance()){
   LOGGER.log(INFO, "about to call buildAuditApAccount");
   AuditApAccount aud = buildAuditApAccount(acnt, chUsr, view, 'U');
   aud.setFieldName("AP_ACNT_BAL");
   String amnt = GenUtilServer.formatNumberLocDp(acntRec.getAccountBalance(),acntRec.getCompany().getLocale());
   aud.setNewValue(amnt);
   amnt = GenUtilServer.formatNumberLocDp(acnt.getAccountBalance(),acntRec.getCompany().getLocale());
   aud.setOldValue(amnt);
   acnt.setAccountBalance(acntRec.getAccountBalance());
   acnt.setChangedBy(chUsr);
   acnt.setChangedOn(acntRec.getChangedOn());
   updated = true;
  }
  LOGGER.log(INFO, "acntRec.getApPeriodBalances() {0}", acntRec.getApPeriodBalances());
  if(acntRec.getApPeriodBalances() != null){
   // Account has balances so possible changes 
   LOGGER.log(INFO, "There are ap Balances");
   List<Integer> years = new ArrayList<>();
   
   List<FiApPeriodBalanceRec> apPerBalRecList = acntRec.getApPeriodBalances();
   
   for(FiApPeriodBalanceRec curr:apPerBalRecList){
    
    if(years.isEmpty()){
     years.add(curr.getBalYear());
    }
    boolean foundYr = false;
    for(Integer currYr:years){
     if(currYr == curr.getBalYear()){
      foundYr = true;
      break;
     }
    }
    if(!foundYr){
     years.add(curr.getBalYear());
    }
   }
   
   TypedQuery q = em.createNamedQuery("apPerBal4Yrs", FiApPeriodBalance.class);
   q.setParameter("acntId", acntRec.getId());
   q.setParameter("yrs", years);
   List<FiApPeriodBalance> apPerBalList = q.getResultList();
   LOGGER.log(INFO, "Account Bal lsit from DB apPerBalList {0}", apPerBalList);
   if(apPerBalList == null || apPerBalList.isEmpty()){
    // add rec period balances must be new
    LOGGER.log(INFO, "No balance list in DB need to create");
    apPerBalList = new ArrayList<>();
    for(FiApPeriodBalanceRec currRec:apPerBalRecList){
     currRec.setCreatedBy(acntRec.getChangedBy());
     currRec.setCreated(acntRec.getCreatedOn());
     FiApPeriodBalance curr = this.buildFiApPeriodBalance(currRec, view);
     curr.setApAccount(acnt);
     apPerBalList.add(curr);
    }
   }else{
    LOGGER.log(INFO, "DB Balance list list exists");
    for(FiApPeriodBalanceRec currRec:apPerBalRecList){
     
     FiApPeriodBalance currBalDb;
     if(currRec.getId() != null){
      currBalDb = em.find(FiApPeriodBalance.class, currRec.getId());
     }else{
      currRec.setCreatedBy(acntRec.getChangedBy());
      currRec.setCreated(new Date());
      currBalDb = buildFiApPeriodBalance(currRec, view);
      currBalDb.setApAccount(acnt);
    }
       
     
     ListIterator<FiApPeriodBalance> li = apPerBalList.listIterator();
     boolean foundDb = false;
     while(li.hasNext() && !foundDb){
      FiApPeriodBalance currPerBal = li.next();
      if(Objects.equals(currPerBal.getId(), currRec.getId())){
       FiApPeriodBalance pd = buildFiApPeriodBalance(currRec, view);
       li.set(pd);
       foundDb = true;
       q = em.createNamedQuery("docLinesForBal", DocLineAp.class);
       q.setParameter("drBalId", currPerBal.getId());
       q.setParameter("crBalId", currPerBal.getId());
       List<DocLineAp> rs = q.getResultList();
       ListIterator<DocLineAp> apLnLi = rs.listIterator();
       if(apLnLi.hasNext()){
        DocLineAp currBalLn = apLnLi.next();
        currBalLn.setApCreditPeriodBalance(null);
        currBalLn.setApDebitPeriodBalance(null);
        apLnLi.set(currBalLn);
      }
      }
     }
     
     
     if(currRec.getCreditDocLines() != null && !currRec.getCreditDocLines().isEmpty() ){
      //set new credit balance lines
      LOGGER.log(INFO, "Save credit balance lines ");
      List<Long> creditIds = new ArrayList<>();
      LOGGER.log(INFO, "currRec.getCreditDocLines() {0}", currRec.getCreditDocLines());
      for(DocLineApRec crLnRec:currRec.getCreditDocLines()){
       creditIds.add(crLnRec.getId());
      }
      
      Query crBalUpdtQ = em.createNamedQuery("apLinesCrBalByIdsUpdate");
      crBalUpdtQ.setParameter("perBal", currBalDb);
      crBalUpdtQ.setParameter("ids", creditIds);
      int numUpdates = crBalUpdtQ.executeUpdate();
      LOGGER.log(INFO,"number of credit bal lines updated {0}",numUpdates);
     }
     if(currRec.getDebitDocLines() != null && !currRec.getDebitDocLines().isEmpty()){
      //set new credit balance lines
     // UPDATE doc_line01 SET changes = (changes + ?) WHERE EXISTS(SELECT t0.doc_line_id FROM doc_line01 t0, doc_line07 t1 WHERE ((t1.ap_credit_period_bal_id IN ()) AND (((t1.doc_line_id = t0.doc_line_id) AND (t0.DTYPE = ?)) AND (t0.tenant_id = ?))) AND t0.doc_line_id = doc_line01.doc_line_id)
      LOGGER.log(INFO, "Save debit balance lines ");
      List<Long> debitIds = new ArrayList<>();
      for(DocLineApRec crLnRec:currRec.getDebitDocLines()){
       debitIds.add(crLnRec.getId());
      }
      Query drBalUpdtQ = em.createNamedQuery("apLinesCrBalByIdsUpdate");
      drBalUpdtQ.setParameter("perBal", currBalDb);
      drBalUpdtQ.setParameter("ids", debitIds);
      int numUpdates = drBalUpdtQ.executeUpdate();
      LOGGER.log(INFO,"number of debit bal lines updated {0}",numUpdates);
     }
    }
   }
   
  }else{
   LOGGER.log(INFO, "No account balances now. Need to remove any from DB");
  }
   
  
   // need to fin the period balance updated
  trans.commit();
  return updated;
 }

}
