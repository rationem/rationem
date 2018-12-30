/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.ChequeVoidReasonRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.entity.mdm.Address;
import java.util.Date;
import com.rationem.entity.user.User;
import javax.persistence.PersistenceException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.Query;
import com.rationem.exception.BacException;
import java.util.ListIterator;
import com.rationem.busRec.doc.DocBankLineBacsRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.ArrayList;
import java.util.List;
import com.rationem.entity.tr.bank.BankBranch;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.tr.bank.Bank;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.entity.audit.AuditArBank;
import com.rationem.entity.audit.AuditBacsTransCode;
import com.rationem.entity.audit.AuditBank;
import com.rationem.entity.audit.AuditBankAccount;
import com.rationem.entity.audit.AuditBankAccountCompany;
import com.rationem.entity.audit.AuditBankBranch;
import com.rationem.entity.audit.AuditBnkPayRun;
import com.rationem.entity.audit.AuditChequeTemplate;
import com.rationem.entity.audit.AuditDocBankLine;
import com.rationem.entity.config.common.ChequeVoidReason;
import com.rationem.entity.config.common.NumberRangeCheque;
import com.rationem.entity.document.DocBankLineBacs;
import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.document.DocBankLineChq;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.tr.bacs.BacsTransCode;
import com.rationem.entity.tr.bank.BankAccount;
import com.rationem.entity.tr.bank.BankAccountCompany;
import com.rationem.entity.tr.bank.BankStatement;
import com.rationem.entity.tr.bank.BnkPaymentRun;
import com.rationem.entity.tr.bank.ChequeTemplate;
import com.rationem.helper.ChequeListSelOpt;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.util.GenUtilServer;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Objects;
import static java.util.logging.Level.INFO;
import javax.persistence.EntityManager;
import java.util.logging.Logger;
import javax.ejb.EJB;


import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static java.text.DateFormat.MEDIUM;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Chris
 */
@Stateless
public class TreasuryDM  {
   private static final Logger LOGGER = Logger.getLogger(TreasuryDM.class.getName());
  
  private EntityManager em;
  private EntityTransaction trans;

  @EJB
  private UserDM usrDM;

  @EJB
  private MasterDataDM masterDataDM;

  @EJB
  private ConfigurationDM configDM;
  
  @EJB
  private FiMasterRecordDM fiMastDM;
  
  @EJB
  private SysBuffer sysBuff;
  
  @EJB
  private DocumentDM fiDocDM;
  
  @EJB
  private ApAccountDM apAcntDM;

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
 
  private AuditBacsTransCode buildAuditBacsTransCode(BacsTransCode code,User usr, String pg, char usrAction){
   AuditBacsTransCode aud = new AuditBacsTransCode();
   aud.setAuditDate(new Date());
   aud.setSource(pg);
   aud.setTransCode(code);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
  private AuditBank buildAuditBank(Bank bnk, User usr, String pg, char usrAction){
  AuditBank aud = new AuditBank();
  aud.setAuditDate(new Date());
  aud.setBank(bnk);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
}
 
private AuditBankAccount buildAuditBankAccount(BankAccount bnk, User usr, String pg, 
        char usrAction){
  AuditBankAccount aud = new AuditBankAccount();
  aud.setAuditDate(new Date());
  aud.setBankAc(bnk);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
}

private AuditBankAccountCompany buildAuditBankAccountCompany(BankAccountCompany acnt, User usr, 
  String pg, char usrAction){
 
 AuditBankAccountCompany aud = new AuditBankAccountCompany();
 aud.setAuditDate(new Date());
 aud.setBnkAcntComp(acnt);
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
private AuditBankBranch buildAuditBankBranch(BankBranch bnk, User usr, String pg, char usrAction){
  AuditBankBranch aud = new AuditBankBranch();
  aud.setAuditDate(new Date());
  aud.setBankBranch(bnk);
  aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
}

private AuditChequeTemplate buildAuditChequeTemplate(ChequeTemplate tmpl,User usr, String pg, char usrAction){
 AuditChequeTemplate aud = new AuditChequeTemplate();
 aud.setAuditDate(new Date());
 aud.setChequeTemplate(tmpl);
 aud.setCreatedBy(usr);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
}

private AuditDocBankLine buildAuditDocBankLine(DocBankLineBase bnkLn, User usr, String pg, char usrAction){
 AuditDocBankLine aud = new AuditDocBankLine();
 aud.setAuditDate(new Date());
 aud.setBankLine(bnkLn);
 aud.setSource(pg);
 aud.setCreatedBy(usr);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 return aud;
}

  public BankAccount getBankAccount(BankAccountRec account){
    BankAccount b = em.find(BankAccount.class, account.getId(), OPTIMISTIC);

    return b;
  }
  
  
  public BankAccountRec getBankAccountByNumSort(String sortCd, String acntNum){
   
   Query q = em.createNamedQuery("accountBySortCdAcntNum");
   q.setParameter("actNum", acntNum);
   q.setParameter("sortCd", sortCd);
   
   try{
   BankAccount acnt = (BankAccount)q.getSingleResult();
   BankAccountRec rec = this.getBankAccountRec(acnt);
   return rec;
   }catch(NoResultException ex){
    LOGGER.log(INFO, "Bank account not found for num {0}", acntNum);
    return null;
   }catch(NonUniqueResultException ex){
    LOGGER.log(INFO, "Multiple accounts found for ac {0}", acntNum);
    return null;
   }
   
  }
  public BankAccountRec getBankAccountByNumber(String acNum, Long branchId){
   Query q = em.createNamedQuery("accountForBranch");
   q.setParameter("actNum",acNum);
   q.setParameter("brId", branchId);
   
   try{
   BankAccount acnt = (BankAccount)q.getSingleResult();
   BankAccountRec rec = this.getBankAccountRec(acnt);
   return rec;
   }catch(NoResultException ex){
    LOGGER.log(INFO, "Bank account not found for num {0}", acNum);
    return null;
   }catch(NonUniqueResultException ex){
    LOGGER.log(INFO, "Multiple accounts found for ac {0}", acNum);
    return null;
   }
   
   
  
  }
  public BankAccount getBankAccountPvt(BankAccountRec acnt,String pg){
   if(!trans.isActive()){
    trans.begin();
   }
   
   
   BankRec bnkRec = acnt.getAccountForBranch().getBank();
   Bank bnk = em.find(Bank.class, bnkRec.getId(), OPTIMISTIC);
   BankBranch bnkBr = em.find(BankBranch.class, acnt.getAccountForBranch().getId());
   BankAccount b = buildBankAccount(acnt,bnkBr,pg);
   return b;
  }
  
  public ArBankAccount buildBankAccountArApPvt(ArBankAccountRec acntRec, String page){
   return buildBankAccountArAp(acntRec,page);
  }
  
  private ArBankAccount buildBankAccountArAp(ArBankAccountRec acntRec, String page){
  ArBankAccount bnk;
  boolean newBnk = false;
  boolean changedBnk = false;
  LOGGER.log(INFO, "Bank rec id {0}", acntRec.getId());
  if(acntRec.getId() != null && acntRec.getId() < 0){
   acntRec.setId(null);
  }
  if(acntRec.getId() == null ){
   bnk = new ArBankAccount();
   User usr = em.find(User.class, acntRec.getCreatedBy().getId());
   bnk.setCreatedBy(usr);
   bnk.setCreatedOn(new Date());
   em.persist(bnk);
   AuditArBank aud = this.buildAuditArBank(bnk, usr, page, 'I');
   aud.setNewValue(acntRec.getBankKey());
   newBnk = true;
  }else{
   bnk = em.find(ArBankAccount.class, acntRec.getId(), OPTIMISTIC);
  }
  
  if(newBnk){
   bnk.setAccountName(acntRec.getAccountName());
   BankAccount bnkAc = em.find(BankAccount.class, acntRec.getBankAccount().getId());
   bnk.setBankAccount(bnkAc);
   if(acntRec.getBankForApAccount() != null){
    ApAccount apAcnt = em.find(ApAccount.class, acntRec.getBankForApAccount().getId());
    bnk.setBankForApAccount(apAcnt);
   }
   if(acntRec.getBankForArAccount() != null){
    ArAccount arAcnt = em.find(ArAccount.class, acntRec.getBankForArAccount().getId());
    bnk.setBankForArAccount(arAcnt);
   }
   bnk.setBankKey(acntRec.getBankKey());
   bnk.setCollValidFrom(acntRec.getCollValidFrom());
   bnk.setCollectionAuthorisation(acntRec.isCollectionAuthorisation());
   bnk.setDdFileType(acntRec.getDdFileType());
   bnk.setDdRef(acntRec.getDdRef());
   bnk.setSignedDD(acntRec.getSignedDD());
  }else {
   User chUsr = em.find(User.class, acntRec.getChangedBy().getId());
      
   if((acntRec.getAccountName() == null && bnk.getAccountName() != null) ||
      (acntRec.getAccountName() != null && bnk.getAccountName() == null) ||
      (acntRec.getAccountName() != null && !acntRec.getAccountName().equals(bnk.getAccountName())
     )){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_NM");
    aud.setNewValue(acntRec.getAccountName());
    aud.setOldValue(bnk.getAccountName());
    bnk.setAccountName(acntRec.getAccountName());
    changedBnk = true;
   }
   if(!acntRec.getCollValidFrom().equals(bnk.getCollValidFrom())){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_COL_FR");
    aud.setNewValue(acntRec.getCollValidFrom().toString());
    aud.setOldValue(bnk.getCollValidFrom().toString());
    bnk.setCollValidFrom(acntRec.getCollValidFrom());
    changedBnk = true;
   }
   if(!acntRec.getBankKey().equals(bnk.getBankKey())){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_BNK_KEY");
    aud.setNewValue(acntRec.getBankKey());
    aud.setOldValue(bnk.getBankKey());
    bnk.setBankKey(acntRec.getBankKey());
    changedBnk = true;
   }
      
   if(acntRec.isCollectionAuthorisation() != bnk.isCollectionAuthorisation()){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_BNK_COL_AUTH");
    aud.setNewValue(String.valueOf(acntRec.isCollectionAuthorisation()));
    aud.setOldValue(String.valueOf(acntRec.isCollectionAuthorisation()));
    bnk.setCollectionAuthorisation(acntRec.isCollectionAuthorisation());
    changedBnk = true;
   }
      
   if((acntRec.getDdFileType() == null && bnk.getDdFileType() != null) || 
      (acntRec.getDdFileType() != null && bnk.getDdFileType() == null) ||
      (acntRec.getDdFileType() != null && !acntRec.getDdFileType().equals(bnk.getDdFileType()))){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_BNK_DD_FT");
    aud.setNewValue(acntRec.getDdFileType());
    aud.setOldValue(bnk.getDdFileType());
    bnk.setDdFileType(acntRec.getDdFileType());
    changedBnk = true;
   }
      
   if((acntRec.getDdRef() == null && bnk.getDdRef() != null) || 
      (acntRec.getDdRef() != null && bnk.getDdRef() == null) ||
      (acntRec.getDdRef() != null && !acntRec.getDdRef().equals(bnk.getDdRef()))){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_BNK_DD_REF");
    aud.setNewValue(acntRec.getDdRef());
    aud.setOldValue(bnk.getDdRef());
    bnk.setDdRef(acntRec.getDdRef());
    changedBnk = true;
   }
   if((acntRec.getSignedDD() == null && bnk.getSignedDD() != null) || 
      (acntRec.getSignedDD() != null && bnk.getSignedDD() == null) ||
      (acntRec.getSignedDD() != null && 
       acntRec.getSignedDD().length != bnk.getSignedDD().length)){
    AuditArBank aud = this.buildAuditArBank(bnk, chUsr, page, 'U');
    aud.setFieldName("AR_ACNT_BNK_DD_MDT");
    bnk.setSignedDD(acntRec.getSignedDD());
    changedBnk = true;
   }
      
   if(changedBnk){
     bnk.setChangedBy(chUsr);
     bnk.setChangedOn(acntRec.getChangedOn());
   }
      
   }
   return bnk;
  }
  private BankAccount buildBankAccount(BankAccountRec account, BankBranch branch, String pg){
    BankAccount accnt;
    boolean newBnkAc;
    boolean bnkChanged = false;
    if(account.getId() != null){
      accnt = em.find(BankAccount.class, account.getId());
      return accnt;
    }else{
      accnt = new BankAccount();
      accnt.setAccountForBranch(branch);
      accnt.setAccountNumber(account.getAccountNumber());
      accnt.setAccountName(account.getAccountName());
      User usr = em.find(User.class, account.getCreatedBy().getId(), OPTIMISTIC);
      accnt.setCreatedBy(usr);
      accnt.setCreatedOn(new Date());
      accnt.setDirectCreditAllowed(account.isDirectCreditAllowed());
      accnt.setDirectDebitsAllowed(account.isDirectDebitsAllowed());
      accnt.setFasterPayments(account.isFasterPayments());
      em.persist(accnt);
      AuditBankAccount aud = buildAuditBankAccount(accnt, usr, pg, 'I');
      aud.setNewValue(accnt.getAccountNumber());
      newBnkAc= true;
    }
    
    if(!newBnkAc){
     // has account been changed
     User chUsr = accnt.getUpdatedBy();
     
     if((account.getAccountNumber() == null && accnt.getAccountNumber() != null) ||
        (account.getAccountNumber() != null && accnt.getAccountNumber() == null) ||
        (account.getAccountNumber() != null 
             && !account.getAccountNumber().equals(accnt.getAccountNumber()))){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_NUM");
      aud.setNewValue(account.getAccountNumber());
      aud.setOldValue(accnt.getAccountNumber());
      accnt.setAccountNumber(account.getAccountNumber());
      bnkChanged =true; 
     }
     if((account.getAccountName() == null && accnt.getAccountName() != null) ||
        (account.getAccountName() != null && accnt.getAccountName() == null) ||
        (account.getAccountName() != null 
             && !account.getAccountName().equals(accnt.getAccountName()))){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_NUM");
      aud.setNewValue(account.getAccountName());
      aud.setOldValue(accnt.getAccountName());
      accnt.setAccountName(account.getAccountNumber());
      bnkChanged =true; 
     }
     if((account.getAccountName() == null && accnt.getAccountName() != null) ||
        (account.getAccountName() != null && accnt.getAccountName() == null) ||
        (account.getAccountName() != null 
             && !account.getAccountName().equals(accnt.getAccountName()))){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_NAME");
      aud.setNewValue(account.getAccountName());
      aud.setOldValue(accnt.getAccountName());
      accnt.setAccountName(account.getAccountNumber());
      bnkChanged =true; 
     }
     if(account.isDirectCreditAllowed() != accnt.isDirectCreditAllowed()){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_CR_DIS");
      aud.setNewValue(String.valueOf(account.isDirectCreditAllowed()));
      aud.setOldValue(String.valueOf(accnt.isDirectCreditAllowed()));
      accnt.setDirectCreditAllowed(account.isDirectCreditAllowed());
      bnkChanged =true; 
     }
     if(account.isDirectDebitsAllowed() != accnt.isDirectDebitsAllowed()){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_DR_DIS");
      aud.setNewValue(String.valueOf(account.isDirectDebitsAllowed()));
      aud.setOldValue(String.valueOf(accnt.isDirectDebitsAllowed()));
      accnt.setDirectDebitsAllowed(account.isDirectDebitsAllowed());
      bnkChanged =true; 
     }
     if(account.isFasterPayments()!= accnt.isFasterPayments()){
      AuditBankAccount aud = buildAuditBankAccount(accnt, chUsr, pg, 'U');
      aud.setFieldName("BNK_AC_DR_DIS");
      aud.setNewValue(String.valueOf(account.isFasterPayments()));
      aud.setOldValue(String.valueOf(accnt.isFasterPayments()));
      accnt.setFasterPayments(account.isFasterPayments());
      bnkChanged =true; 
     }
     if(bnkChanged){
      accnt.setUpdatedBy(chUsr);
      accnt.setUpdatedOn(account.getUpdatedOn());
     } 
     
    }



       

    return accnt;
  }

  private BankAccountCompany buildBankAccountCompany(BankAccountCompanyRec bankAcRec, String view){
   LOGGER.log(INFO, "buildBankAccountCompany called with bank ac num {0}",bankAcRec);
   BankAccountCompany bnkAc;
   boolean newBankCompAc = false;
   boolean changedBankCompAc = false;
   
   if(bankAcRec.getId() == null || bankAcRec.getId() == 0){
    bnkAc = new BankAccountCompany();
    User crUser = em.find(User.class, bankAcRec.getCreatedBy().getId(), OPTIMISTIC);
    bnkAc.setCreatedBy(crUser);
    bnkAc.setCreatedOn(bankAcRec.getCreatedOn());
    em.persist(bnkAc);
    AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, crUser, view, 'I');
    newBankCompAc = true;
   }else{
    bnkAc = em.find(BankAccountCompany.class, bankAcRec.getId(), OPTIMISTIC);
   }
   if(newBankCompAc){
    if(bankAcRec.getAccountForBranch() != null){
     BankBranch br = em.find(BankBranch.class, bankAcRec.getAccountForBranch().getId(), OPTIMISTIC);
     bnkAc.setAccountForBranch(br);
    }
    
    if(bankAcRec.getComp() != null){
     CompanyBasic comp = em.find(CompanyBasic.class, bankAcRec.getComp().getId(), OPTIMISTIC);
     bnkAc.setComp(comp);
    }
    bnkAc.setAccountName(bankAcRec.getAccountName());
    bnkAc.setAccountNumber(bankAcRec.getAccountNumber());
    bnkAc.setChequeNumLen(bankAcRec.getChqueNumlen());
    if(bankAcRec.getClearedGlAccount() != null){
     FiGlAccountComp clGlAc = em.find(FiGlAccountComp.class, bankAcRec.getClearedGlAccount().getId(), OPTIMISTIC);
     bnkAc.setClearedGlAccount(clGlAc);
    }
    
    if(bankAcRec.getChequeTemplate() != null && bankAcRec.getChequeTemplate().getId() != null){
     ChequeTemplate tmpl = em.find(ChequeTemplate.class, bankAcRec.getChequeTemplate().getId());
     bnkAc.setChequeTemplate(tmpl);
    }
   }else{
    // changed Bank account comp?
    User chUsr = em.find(User.class, bankAcRec.getUpdatedBy().getId(), OPTIMISTIC);
    
    if((bankAcRec.getAccountForBranch() == null && bnkAc.getAccountForBranch() != null) ||
      (bankAcRec.getAccountForBranch() != null && bnkAc.getAccountForBranch() == null) ||
      (bankAcRec.getAccountForBranch() != null && 
       !Objects.equals(bnkAc.getAccountForBranch().getId(), bankAcRec.getAccountForBranch().getId()))){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_BR");
     aud.setNewValue(bankAcRec.getAccountForBranch().getSortCode());
     aud.setOldValue(bnkAc.getAccountForBranch().getSortCode());
     BankBranch bnkBr = em.find(BankBranch.class, bankAcRec.getAccountForBranch().getId(), OPTIMISTIC);
     bnkAc.setAccountForBranch(bnkBr);
     changedBankCompAc = true;
    }
    
    if(!StringUtils.equals(bankAcRec.getAccountName(), bnkAc.getAccountName())){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_NAME");
     aud.setNewValue(bankAcRec.getAccountName());
     aud.setOldValue(bnkAc.getAccountName());
     bnkAc.setAccountName(bankAcRec.getAccountName());
     changedBankCompAc = true;
    }
    
    if(!StringUtils.equals(bankAcRec.getAccountNumber(), bnkAc.getAccountNumber())){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_NUM");
     aud.setNewValue(bankAcRec.getAccountNumber());
     aud.setOldValue(bnkAc.getAccountNumber());
     bnkAc.setAccountNumber(bankAcRec.getAccountNumber());
     changedBankCompAc = true;
    }
    
    if(!StringUtils.equals(bankAcRec.getAccountRef(), bnkAc.getAccountRef())){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_REF");
     aud.setNewValue(bankAcRec.getAccountRef());
     aud.setOldValue(bnkAc.getAccountRef());
     bnkAc.setAccountNumber(bankAcRec.getAccountRef());
     changedBankCompAc = true;
    }
    
    if((bankAcRec.getChequeTemplate() == null && bnkAc.getChequeTemplate() != null) ||
      (bankAcRec.getChequeTemplate() != null && bnkAc.getChequeTemplate() == null) ||
      (bankAcRec.getChequeTemplate() != null && 
       !Objects.equals(bnkAc.getChequeTemplate().getId(), bankAcRec.getChequeTemplate().getId()))){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_CHQ_TMPL");
     aud.setNewValue(bankAcRec.getChequeTemplate().getReference());
     if (bnkAc.getChequeTemplate() != null){
      aud.setOldValue(bnkAc.getChequeTemplate().getReference());
     }
     
     ChequeTemplate tmpl = em.find(ChequeTemplate.class, bankAcRec.getChequeTemplate().getId(), OPTIMISTIC);
     bnkAc.setChequeTemplate(tmpl);
     changedBankCompAc = true;
    }
    
    if((bankAcRec.getClearedGlAccount() == null && bnkAc.getClearedGlAccount() != null) ||
      (bankAcRec.getClearedGlAccount() != null && bnkAc.getClearedGlAccount() == null) ||
      (bankAcRec.getClearedGlAccount() != null && 
       !Objects.equals(bnkAc.getClearedGlAccount().getId(), bankAcRec.getClearedGlAccount().getId()))){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_CLR_AC");
     aud.setNewValue(bankAcRec.getClearedGlAccount().getCoaAccount().getRef());
     if(bnkAc.getClearedGlAccount() != null && bnkAc.getClearedGlAccount().getCoaAccount() != null ){
      aud.setOldValue(bnkAc.getClearedGlAccount().getCoaAccount().getRef());
     }
     FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, bankAcRec.getClearedGlAccount().getId(), OPTIMISTIC);
     bnkAc.setClearedGlAccount(glAcnt);
     changedBankCompAc = true;
    }
    
  
     if(bankAcRec.getUnclearedGlAccounts() == null && bnkAc.getUnclearedGlAccounts() != null){
      // removed all uncleared GL accounts
      for(FiGlAccountComp curr : bnkAc.getUnclearedGlAccounts()){
       AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
       aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
       aud.setNewValue(null);
       aud.setOldValue(bankAcRec.getAccountNumber());
       curr.setBankAccountCompanyUncleared(null);
       changedBankCompAc = true;
      }
      bnkAc.setUnclearedGlAccounts(null);
     }
     if(bnkAc.getUnclearedGlAccounts() != null && !bankAcRec.getUnclearedGlAccounts().isEmpty()){
      LOGGER.log(INFO,"Update Uncleared bank acoounts");
      // Adding Uncleared GL acnt when none registered
      List<FiGlAccountComp> newGlAcnts = new ArrayList<>();
      for(FiGlAccountCompRec currRec:bankAcRec.getUnclearedGlAccounts()){
       FiGlAccountComp curr = em.find(FiGlAccountComp.class, currRec.getId());
       curr.setBankAccountCompanyUncleared(bnkAc);
       newGlAcnts.add(curr);
       AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
       aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
       aud.setNewValue(currRec.getCoaAccount().getRef());
       aud.setOldValue(null);
       changedBankCompAc = true;
      }
       bnkAc.setUnclearedGlAccounts(newGlAcnts);
     }
     if(bnkAc.getUnclearedGlAccounts() != null && bankAcRec.getUnclearedGlAccounts() != null){
      // Uncleared GL accounts in DB and new rec
      //Delete an unClearedGLAccounts not in rec set
      
      boolean acntFound;
      // loop over DB uncleared deleting any not in rec list
      ListIterator<FiGlAccountComp> dbAcntli = bnkAc.getUnclearedGlAccounts().listIterator();
      
      while(dbAcntli.hasNext()){
       FiGlAccountComp currDb = dbAcntli.next();
       acntFound = false;
       for(FiGlAccountCompRec currRec:bankAcRec.getUnclearedGlAccounts()){
        if(Objects.equals(currRec.getId(), currDb.getId())){
         acntFound = true;
         break;
        }
       }
       if(!acntFound){
        // db acnt is not found
        AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
        aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
        aud.setNewValue(null);
        aud.setOldValue(currDb.getCoaAccount().getRef());
        currDb.setBankAccountCompanyUncleared(null);
        dbAcntli.remove();
        changedBankCompAc = true;
        currDb.setChangedBy(chUsr);
        currDb.setChangedOn(bankAcRec.getUpdatedOn());
       }
      }
      // add entries in rec not in DB to dB
      for(FiGlAccountCompRec currRec:bankAcRec.getUnclearedGlAccounts()){
       acntFound = false;
       for(FiGlAccountComp currDb:bnkAc.getUnclearedGlAccounts()){
        if(Objects.equals(currRec.getId(), currDb.getId())){
         acntFound = true;
         break;
        }
       }
       if(!acntFound){
        FiGlAccountComp newBnkAc = em.find(FiGlAccountComp.class, currRec.getId());
        AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
        aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
        aud.setNewValue(currRec.getCoaAccount().getRef());
        aud.setOldValue(null);
        bnkAc.getUnclearedGlAccounts().add(newBnkAc);
        changedBankCompAc = true;
        newBnkAc.setChangedBy(chUsr);
        newBnkAc.setChangedOn(bankAcRec.getUpdatedOn());
       }
      }
     }
     
     
    
    
    
    if((bankAcRec.getComp() == null && bnkAc.getComp() != null) ||
      (bankAcRec.getComp() != null && bnkAc.getComp() == null) ||
      (bankAcRec.getComp() != null && 
       !Objects.equals(bnkAc.getComp().getId(), bankAcRec.getComp().getId()))){
     AuditBankAccountCompany aud = this.buildAuditBankAccountCompany(bnkAc, chUsr, view, 'U');
     aud.setFieldName("BNK_AC_COMP_COY");
     aud.setNewValue(bankAcRec.getComp().getReference());
     aud.setOldValue(bnkAc.getComp().getNumber());
     CompanyBasic comp = em.find(CompanyBasic.class, bankAcRec.getComp().getId(), OPTIMISTIC);
     bnkAc.setComp(comp);
     changedBankCompAc = true;
    }
    if(changedBankCompAc){
      bnkAc.setUpdatedBy(chUsr);
      bnkAc.setUpdatedOn(bankAcRec.getUpdatedOn());
    }
    
    
   }
   
   
   
  return bnkAc;
   
  }
  
  private BankAccountCompanyRec buildBankAccountCompanyRec(BankAccountCompany bankAc){
   LOGGER.log(INFO, "buildBankAccountCompany called with bank ac num {0}",bankAc);
   BankAccountCompanyRec bnkAcRec = new BankAccountCompanyRec();
   bnkAcRec.setId(bankAc.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(bankAc.getCreatedBy());
   bnkAcRec.setCreatedBy(crUsr);
   bnkAcRec.setCreatedOn(bankAc.getCreatedOn());
   
   if(bankAc.getUpdatedBy() != null){
    UserRec chUsr = this.usrDM.getUserRecPvt(bankAc.getUpdatedBy());
    bnkAcRec.setUpdatedBy(chUsr);
    bnkAcRec.setUpdatedOn(bankAc.getUpdatedOn());
   }
   if(bankAc.getAccountForBranch() != null){
    BankBranchRec br = this.buildBankBranchRec(bankAc.getAccountForBranch());
    bnkAcRec.setAccountForBranch(br);
   }
   if(bankAc.getComp() != null){
    CompanyBasicRec comp = sysBuff.getCompanyById(bankAc.getComp().getId());
    bnkAcRec.setComp(comp);
   }
   bnkAcRec.setAccountName(bankAc.getAccountName());
   bnkAcRec.setAccountNumber(bankAc.getAccountNumber());
   if(bankAc.getClearedGlAccount() != null){
    FiGlAccountCompRec clGlAc = fiMastDM.buildFiCompGlAccountRecPvt(bankAc.getClearedGlAccount());
    bnkAcRec.setClearedGlAccount(clGlAc);
   }
   bnkAcRec.setChqueNumlen(bankAc.getChequeNumLen());
   
   
  /* if(bankAc.getUnclearedGlAccounts() != null && !bankAc.getUnclearedGlAccounts().isEmpty()){
    List<FiGlAccountCompRec> unclearedAcs = new ArrayList<>();
    for(FiGlAccountComp glAc : bankAc.getUnclearedGlAccounts() ){
     FiGlAccountCompRec glAcRec = fiMastDM.buildFiCompGlAccountRecPvt(glAc);
     unclearedAcs.add(glAcRec);
    }
    bnkAcRec.setUnclearedGlAccounts(unclearedAcs);
   }
   */
   
   if(bankAc.getChequeTemplate() != null){
    ChequeTemplateRec templ = buildChequeTemplateRec(bankAc.getChequeTemplate());
    bnkAcRec.setChequeTemplate(templ);
   }
   
   return bnkAcRec;
   
  }
  
  private BankBranch buildBankBranch(BankBranchRec rec,  String pg){
    BankBranch branch;
    boolean newBranch = false;
    boolean changedBranch = false;
    LOGGER.log(INFO,"buildBankBranch called with branch {0}",
            new Object[]{rec});
    if(rec.getId() != null && rec.getId() != 0){
      branch = em.find(BankBranch.class, rec.getId(), OPTIMISTIC);
      
    }else{
      // need to build branch
      branch = new BankBranch();
      User usr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      branch.setCreatedBy(usr);
      branch.setCreatedOn(new Date());
      em.persist(branch);
      newBranch =true;
      AuditBankBranch aud = this.buildAuditBankBranch(branch, usr, pg, 'I');
      aud.setNewValue(rec.getSortCode());
    }
    
    if(newBranch){
     Bank bank = em.find(Bank.class, rec.getBank().getId());
     branch.setBank(bank);
     branch.setBranchName(rec.getBranchName());
     branch.setSortCode(rec.getSortCode());
     branch.setPhoneAreaCode(rec.getPhoneArea());
     branch.setPhoneNumber(rec.getPhoneNumber());
     branch.setSwiftCode(rec.getSwiftCode());
     branch.setSubBranch(rec.isSubBranch());
     if(rec.getBranchAddress() != null){
      LOGGER.log(INFO, "Post code {0} street {1}", new Object[]{rec.getBranchAddress().getPostCode(),
       rec.getBranchAddress().getStreet() });
      if(rec.getBranchAddress().getId() != null   ){
       Address addr = em.find(Address.class, rec.getBranchAddress().getId(), OPTIMISTIC);
       branch.setBranchAddress(addr);
      }else if ((rec.getBranchAddress().getPostCode() != null || !rec.getBranchAddress().getStreet().isEmpty())){
       if(rec.getBranchAddress().getId() == null){
        rec.getBranchAddress().setCreatedBy(rec.getCreatedBy());
        rec.getBranchAddress().setCreatedOn(rec.getCreatedOn());
       }else{
        rec.getBranchAddress().setChangedBy(rec.getUpdatedBy());
        rec.getBranchAddress().setChangedOn(rec.getUpdatedOn());
       }
       Address addr = masterDataDM.buildAddressDM(rec.getBranchAddress(),  pg);
       branch.setBranchAddress(addr);
      }
     }
    }else{
     // changed
     User chUsr = em.find(User.class, rec.getUpdatedBy().getId(), OPTIMISTIC);
     
     if(!Objects.equals(rec.getBank().getId(), branch.getBank().getId())){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_BANK");
      aud.setNewValue(rec.getBranchName());
      aud.setOldValue(branch.getBank().getBankCode());
      Bank bnk = em.find(Bank.class, rec.getBank().getId(), OPTIMISTIC);
      branch.setBank(bnk);
      changedBranch = true;
     }
     
     if((rec.getBranchName() == null && branch.getBranchName() != null) ||
        (rec.getBranchName() != null && branch.getBranchName() == null) ||
        (rec.getBranchName() != null && !rec.getBranchName().equals(branch.getBranchName()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_NAME");
      aud.setNewValue(rec.getBranchName());
      aud.setOldValue(branch.getBranchName());
      branch.setBranchName(rec.getBranchName());
      changedBranch = true;
     }
     
     if((rec.getBranchAddress() == null && branch.getBranchAddress() != null) ||
        (rec.getBranchAddress() != null && branch.getBranchAddress() == null) ||
        (rec.getBranchAddress() != null 
             && !Objects.equals(rec.getBranchAddress().getId(), branch.getBranchAddress().getId()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_ADDR");
      aud.setNewValue(rec.getBranchAddress().getAddrRef());
      if(branch.getBranchAddress() != null){
      aud.setOldValue(branch.getBranchAddress().getAddrRef());
      }
      Address addr  =em.find(Address.class, rec.getBranchAddress().getId(), OPTIMISTIC);
      branch.setBranchAddress(addr);
      changedBranch = true;
     }
     
     if((rec.getSwiftCode() == null && branch.getSwiftCode() != null) ||
        (rec.getSwiftCode() != null && branch.getSwiftCode() == null) ||
        (rec.getSwiftCode() != null && !rec.getSwiftCode().equals(branch.getSwiftCode()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_CHAPS");
      aud.setNewValue(rec.getSwiftCode());
      aud.setOldValue(branch.getSwiftCode());
      branch.setSwiftCode(rec.getSwiftCode());
      changedBranch = true;
     }
     
     if((rec.getPhoneArea() == null && branch.getPhoneAreaCode()!= null) ||
        (rec.getPhoneArea() != null && branch.getPhoneAreaCode() == null) ||
        (rec.getPhoneArea() != null && !rec.getPhoneArea().equals(branch.getPhoneAreaCode()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_PHONE_AREA");
      aud.setNewValue(rec.getPhoneArea());
      aud.setOldValue(branch.getPhoneAreaCode());
      branch.setPhoneAreaCode(rec.getPhoneArea());
      changedBranch = true;
     }
     
     if((rec.getPhoneNumber() == null && branch.getPhoneNumber()!= null) ||
        (rec.getPhoneNumber() != null && branch.getPhoneNumber() == null) ||
        (rec.getPhoneNumber() != null && !rec.getPhoneNumber().equals(branch.getPhoneNumber()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_PHONE_AREA");
      aud.setNewValue(rec.getPhoneNumber());
      aud.setOldValue(branch.getPhoneNumber());
      branch.setPhoneNumber(rec.getPhoneNumber());
      changedBranch = true;
     }
     
     if((rec.getSortCode() == null && branch.getSortCode()!= null) ||
        (rec.getSortCode() != null && branch.getSortCode() == null) ||
        (rec.getSortCode() != null && !rec.getSortCode().equals(branch.getSortCode()))){
      AuditBankBranch aud = this.buildAuditBankBranch(branch, chUsr, pg, 'U');
      aud.setFieldName("BNK_BR_SORT_CD");
      aud.setNewValue(rec.getSortCode());
      aud.setOldValue(branch.getSortCode());
      branch.setSortCode(rec.getSortCode());
      changedBranch = true;
     }
     
     if(changedBranch){
      branch.setUpdatedBy(chUsr);
      branch.setUpdatedOn(new Date());
     }
    }
    
    
      
      
      
      
      LOGGER.log(INFO, "buildBranch returns branch with id {0} ", branch.getId());
      return branch;
    
    
  }
  private BankBranchRec buildBankBranchRec(BankBranch b){
    BankBranchRec rec = new BankBranchRec();
    if(b.getBank() != null){
      BankRec bank = this.buildBankRec(b.getBank());
      rec.setBankOrg(bank);
    }
    rec.setBranchName(b.getBranchName());
    if(b.getBranchAddress() != null){
      AddressRec addr = this.masterDataDM.getAddressRec(b.getBranchAddress());
      rec.setBranchAddress(addr);
    }
    if(b.getCreatedBy() != null){
      UserRec usr = usrDM.getUserRecPvt(b.getCreatedBy());
      rec.setCreatedBy(usr);
      rec.setCreatedOn(b.getCreatedOn());
    }
    if(b.getUpdatedBy() != null){
      UserRec usr = usrDM.getUserRecPvt(b.getUpdatedBy());
      rec.setUpdatedBy(usr);
      rec.setUpdatedOn(b.getUpdatedOn());
    }
    rec.setId(b.getId());
    rec.setRevision(b.getRevision());
    rec.setSortCode(b.getSortCode());
    return rec;
  }

  private BacsTransCode buildBacsTransCode(BacsTransCodeRec rec, String pg){
   LOGGER.log(INFO, "buildBacsTransCode called with trans cd {0} ",rec);
   BacsTransCode bacsTrans;
   boolean newBacsCode = false;
   boolean changedCode = false;
   
   
   if(rec.getId() == null ){
    bacsTrans = new BacsTransCode();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    bacsTrans.setCreatedBy(crUsr);
    bacsTrans.setCreatedOn(rec.getCreatedOn());
    em.persist(bacsTrans);
    AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, crUsr, pg, 'I');
    aud.setNewValue(rec.getPtnrBnkTransCode());
    newBacsCode = true;
   }else{
    bacsTrans = em.find(BacsTransCode.class, rec.getId(), OPTIMISTIC);
   }
   
   if(newBacsCode){
    bacsTrans.setCollection(rec.isCollection());
    bacsTrans.setDescription(rec.getDescription());
    bacsTrans.setName(rec.getName());
    bacsTrans.setPrCode(rec.getPrCode());
    bacsTrans.setPtnrBnkTransCode(rec.getPtnrBnkTransCode());
    bacsTrans.setContrTransCode(rec.getContrTransCode()); 
    bacsTrans.setDebit(rec.isDebit());
   }else{
    // has there been a change?
    User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
    
    if(rec.isCollection() != bacsTrans.isCollection()){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_CD");
     aud.setNewValue(String.valueOf(rec.isCollection()));
     aud.setOldValue(String.valueOf(bacsTrans.isCollection()));
     bacsTrans.setCollection(rec.isCollection());
     changedCode = true;
    }
    if(rec.isDebit() != bacsTrans.isDebit()){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_CD");
     aud.setNewValue(String.valueOf(rec.isDebit()));
     aud.setOldValue(String.valueOf(bacsTrans.isDebit()));
     bacsTrans.setDebit(rec.isDebit());
     changedCode = true;
    }
    if(!StringUtils.equals(rec.getContrTransCode(), bacsTrans.getContrTransCode())){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_CONTRA");
     aud.setNewValue(rec.getContrTransCode());
     aud.setOldValue(bacsTrans.getContrTransCode());
     bacsTrans.setContrTransCode(rec.getContrTransCode());
     changedCode = true;
    }
    if(!StringUtils.equals(rec.getDescription(), bacsTrans.getDescription())){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_DESCR");
     aud.setNewValue(rec.getDescription());
     aud.setOldValue(bacsTrans.getDescription());
     bacsTrans.setDescription(rec.getDescription());
     changedCode = true;
    }
    if(!StringUtils.equals(rec.getName(), bacsTrans.getName())){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_NAME");
     aud.setNewValue(rec.getName());
     aud.setOldValue(bacsTrans.getName());
     bacsTrans.setName(rec.getName());
     changedCode = true;
    }
    if(!StringUtils.equals(rec.getPrCode(), bacsTrans.getPrCode())){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_PROC");
     aud.setNewValue(rec.getPrCode());
     aud.setOldValue(bacsTrans.getPrCode());
     bacsTrans.setPrCode(rec.getPrCode());
     changedCode = true;
    }
    if(!StringUtils.equals(rec.getPtnrBnkTransCode(), bacsTrans.getPtnrBnkTransCode())){
     AuditBacsTransCode aud = this.buildAuditBacsTransCode(bacsTrans, chUsr, pg, 'U');
     aud.setFieldName("BACS_TRANS_CD");
     aud.setNewValue(rec.getPtnrBnkTransCode());
     aud.setOldValue(bacsTrans.getPtnrBnkTransCode());
     bacsTrans.setPtnrBnkTransCode(rec.getPtnrBnkTransCode());
     changedCode = true;
    }
   }
   
   
   
   return bacsTrans;
  }
  
  public BacsTransCodeRec buildBacsTransCodeRecPvt(BacsTransCode trans){
   return buildBacsTransCodeRec(trans);
  }
  private BacsTransCodeRec buildBacsTransCodeRec(BacsTransCode trans){
   LOGGER.log(INFO, "buildBacsTransCode called with trans cd {0} ",trans);
   BacsTransCodeRec rec = new BacsTransCodeRec();
   
   rec.setId(trans.getId());
   UserRec crUsr = usrDM.getUserRecPvt(trans.getCreatedBy());
   rec.setCreatedBy(crUsr);
   rec.setCreatedOn(trans.getCreatedOn());
   if(trans.getChangedBy() != null){
    UserRec chUsr = usrDM.getUserRecPvt(trans.getChangedBy());
    rec.setChangedBy(chUsr);
    rec.setChangedOn(trans.getChangedOn());
   }
   
   rec.setCollection(trans.isCollection());
   rec.setDescription(trans.getDescription());
   rec.setName(trans.getName());
   rec.setPrCode(trans.getPrCode());
   rec.setPtnrBnkTransCode(trans.getPtnrBnkTransCode());
   rec.setContrTransCode(trans.getContrTransCode());
   
   return rec;
  }
  private Bank buildBank(BankRec rec, String pg){
   LOGGER.log(INFO, "buildBank called with bank {0} id {1}", new Object[]{rec,rec.getId()});
   LOGGER.log(INFO, "rec created by {0}", rec.getCreatedBy());
    Bank bank;
    boolean newBank = false;
    boolean bankChanged = false;
    
    if(rec.getId() != null){
      bank = em.find(Bank.class, rec.getId(), OPTIMISTIC) ;
    }else{
      //No Id so must be new bank account
      bank = new Bank();
      User usr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      bank.setCreatedBy(usr);
      bank.setCreatedOn(new Date());
      em.persist(bank);
      newBank = true;
      AuditBank aud = buildAuditBank(bank, usr, pg, 'I');
      aud.setNewValue(bank.getBankCode());
    }
    
    if(newBank){
     bank.setBankCode(rec.getBankCode());
     bank.setChapsBankCode(rec.getChapsBankCode());
     PartnerCorporate bnkOrg;
     if(rec.getBankOrganisation() != null){
      if(rec.getBankOrganisation().getId() == null){
       rec.getBankOrganisation().setRef(rec.getBankCode());
      } 
      bnkOrg =  (PartnerCorporate)masterDataDM.updatePartnerDM(rec.getBankOrganisation(), pg);
      
      LOGGER.log(INFO, "bank org ptnr id {0}", bnkOrg.getId());
      bank.setBankOrganisation(bnkOrg);
      
     }
     
     LOGGER.log(INFO, "rec.getBankAddress(){0}",rec.getBankAddress());
     if(rec.getBankAddress() != null ){
      
      Address addr ;  //= masterDataDM.buildAddressDM(rec.getBankAddress(),  pg);
      if(rec.getBankAddress().getId() ==  null){
       addr = masterDataDM.buildAddressDM(rec.getBankAddress(),  pg);
      }else{
       addr = em.find(Address.class, rec.getBankAddress().getId());
      }
      bank.setBankAddress(addr);
     }
     LOGGER.log(INFO, "rec.getBankAddress(){0}",rec.getBankContact());
     if(rec.getBankContact() != null){
      PartnerPerson contact = this.masterDataDM.buildPartnerPersonDM(rec.getBankContact(), pg);
      bank.setBankContact(contact);
      
     }
   }else{
     User chUsr = em.find(User.class, rec.getUpdatedBy().getId(), OPTIMISTIC);
     
     if((rec.getBankCode() == null && bank.getBankCode() != null) ||
        (rec.getBankCode() != null && bank.getBankCode() == null) ||
        (rec.getBankCode() != null && !rec.getBankCode().equals(bank.getBankCode()))){
      AuditBank aud = buildAuditBank(bank, chUsr, pg, 'U');
      aud.setFieldName("BNK_BANK_CODE");
      aud.setNewValue(rec.getBankCode());
      aud.setOldValue(bank.getBankCode());
      bank.setBankCode(rec.getBankCode());
      bankChanged = true;
     }
     
     if((rec.getChapsBankCode() == null && bank.getChapsBankCode() != null) ||
        (rec.getChapsBankCode() != null && bank.getChapsBankCode() == null) ||
        (rec.getChapsBankCode() != null && !rec.getChapsBankCode().equals(bank.getChapsBankCode()))){
      AuditBank aud = buildAuditBank(bank, chUsr, pg, 'U');
      aud.setFieldName("BNK_BANK_CHAPS");
      aud.setNewValue(rec.getChapsBankCode());
      aud.setOldValue(bank.getChapsBankCode());
      bank.setChapsBankCode(rec.getChapsBankCode());
      bankChanged = true;
     }
     
     if((rec.getBankOrganisation() == null && bank.getBankOrganisation() != null) ||
        (rec.getBankOrganisation() != null && bank.getBankOrganisation() == null) ||
        (rec.getBankOrganisation() != null && !Objects.equals(rec.getBankOrganisation().getId(), bank.getBankOrganisation().getId()))){
      AuditBank aud = buildAuditBank(bank, chUsr, pg, 'U');
      aud.setFieldName("BNK_BANK_ORG");
      aud.setNewValue(rec.getBankOrganisation().getRef());
      if(bank.getBankOrganisation() != null){
       aud.setOldValue(bank.getBankOrganisation().getRef());
      }
      PartnerCorporate org = em.find(PartnerCorporate.class, rec.getBankOrganisation().getId(), OPTIMISTIC);
      bank.setBankOrganisation(org);
      bankChanged = true;
     }
      
     if((rec.getBankAddress() == null && bank.getBankAddress() != null) ||
        (rec.getBankAddress() != null && bank.getBankAddress() == null) ||
        (rec.getBankAddress() != null && !Objects.equals(rec.getBankAddress().getId(), bank.getBankAddress().getId()))){
      AuditBank aud = buildAuditBank(bank, chUsr, pg, 'U');
      aud.setFieldName("BNK_BANK_ADR");
      aud.setNewValue(rec.getBankAddress().getAddrRef());
      if(bank.getBankAddress() != null){
       aud.setOldValue(bank.getBankAddress().getAddrRef());
      }
      Address addr = em.find(Address.class, rec.getBankAddress().getId(), OPTIMISTIC);
      bank.setBankAddress(addr);
      bankChanged = true;
     } 
      
     if((rec.getBankContact() == null && bank.getBankContact() != null) ||
        (rec.getBankContact() != null && bank.getBankContact() == null) ||
        (rec.getBankContact() != null && !Objects.equals(rec.getBankContact().getId(), bank.getBankContact().getId()))){
      AuditBank aud = buildAuditBank(bank, chUsr, pg, 'U');
      aud.setFieldName("BNK_BANK_CONT");
      aud.setNewValue(rec.getBankContact().getName());
      if(bank.getBankContact() != null){
       aud.setOldValue(((PartnerPerson)bank.getBankContact()).getFirstName()+" "+
               ((PartnerPerson)bank.getBankContact()).getFamilyName());
      }
      PartnerPerson cont = em.find(PartnerPerson.class, rec.getBankContact().getId(), OPTIMISTIC);
      bank.setBankContact(cont);
      bankChanged = true;
     }
      
     if(bankChanged){
      bank.setUpdatedBy(chUsr);
      bank.setUpdatedOn(rec.getUpdatedOn());
     } 
    }
    LOGGER.log(INFO, "bank id after persist {0}", bank.getId());
    return bank;

  }


  
  private BankRec buildBankRec(Bank bnk){
    BankRec rec = new BankRec();
    rec.setId(bnk.getId());
    if(bnk.getCreatedBy() != null){
      UserRec usr = this.usrDM.getUserRecPvt(bnk.getCreatedBy());
      rec.setCreatedBy(usr);
      rec.setCreatedOn(bnk.getCreatedOn());
    }
    if(bnk.getUpdatedBy() != null && bnk.getUpdatedBy().getId() != 0){
      UserRec usr = this.usrDM.getUserRecPvt(bnk.getUpdatedBy());
      rec.setUpdatedBy(usr);
      rec.setUpdatedOn(bnk.getUpdatedOn());
    }
    if(bnk.getBankAddress() != null){
      AddressRec adr = this.masterDataDM.getAddressRec(bnk.getBankAddress());
      rec.setBankAddress(adr);
    }
    rec.setBankCode(bnk.getBankCode());
    if(bnk.getBankContact() != null && bnk.getBankContact().getId() != null ){
      LOGGER.log(INFO, "Get bank Contact called with {0}",bnk.getBankContact());
      PartnerPersonRec ptnr = this.masterDataDM.buildPartnerPersonRecPvt((PartnerPerson)bnk.getBankContact());
      rec.setBankContact(ptnr);
    }
    if(bnk.getBankOrganisation() != null && bnk.getBankOrganisation().getId() != null ){
      LOGGER.log(INFO, "Get bank Org called");
      PartnerCorporateRec ptnr = this.masterDataDM.getPartnerCorporateRec(bnk.getBankOrganisation());
      rec.setBankOrganisation(ptnr);
    }
    return rec;
  }

  private DocBankLineBase buildDocBankLine(DocBankLineBaseRec bnkLnRec, DocLineBaseRec fiLnRec, 
    String pg){
   
   boolean newBnkLn = false;
   boolean changedBnkLn = false;
   String bnkLnClass = bnkLnRec.getClass().getSimpleName();
   DocBankLineBase bnkLn = null;
   if(bnkLnRec.getId() == null){
    if(StringUtils.equals(bnkLnClass, "DocBankLineBacsRec")){
     bnkLn = new DocBankLineBacs();
    }else if(StringUtils.equals(bnkLnClass, "DocBankLineChqRec")){
     bnkLn = new DocBankLineChq();
    }
    if(bnkLn == null){
     LOGGER.log(INFO, "Invalid bank line {0}");
     return null;
    }
    User crUsr = em.find(User.class, bnkLnRec.getCreatedBy().getId());
    bnkLn.setCreatedBy(crUsr);
    bnkLn.setCreatedOn(bnkLnRec.getCreatedOn());
    em.persist(bnkLn);
    AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, crUsr, pg, 'I');
    aud.setNewValue(bnkLnRec.getBnkRef());
    newBnkLn = true;
   }else{
    bnkLn = em.find(DocBankLineBase.class, bnkLnRec.getId());
   }
   
   if(newBnkLn){
    if(bnkLnRec.getClearedBankAc() != null){
     BankAccountCompany clrBnkAc = em.find(BankAccountCompany.class, bnkLnRec.getClearedBankAc().getId());
     bnkLn.setClearedBankAc(clrBnkAc);
    }else if(bnkLnRec.getUnClearedBankAc() != null){
     BankAccountCompany unClrBnkAc = em.find(BankAccountCompany.class, bnkLnRec.getUnClearedBankAc().getId());
     bnkLn.setUnClearedBankAc(unClrBnkAc);
    }
    bnkLn.setAmount(bnkLnRec.getAmount());
    if(bnkLnRec.getApAccount() != null){
     ApAccount apAcnt = em.find(ApAccount.class, bnkLnRec.getApAccount().getId());
     bnkLn.setApAccount(apAcnt);
    }
    if(bnkLnRec.getArAccount() != null){
     ArAccount arAcnt = em.find(ArAccount.class, bnkLnRec.getArAccount().getId());
     bnkLn.setArAccount(arAcnt);
    }
    if(bnkLnRec.getArBank() != null){
     ArBankAccount arBnk = em.find(ArBankAccount.class, bnkLnRec.getArBank());
     bnkLn.setArBank(arBnk);
    }
    bnkLn.setBankTransCode(bnkLnRec.getBankTransCode());
    bnkLn.setBnkRef(bnkLnRec.getBnkRef());
    if(bnkLnRec.getBnkStament() != null){
     BankStatement stmnt = em.find(BankStatement.class, bnkLnRec.getBnkStament().getId());
     bnkLn.setBnkStament(stmnt);
    }
    bnkLn.setClearedDate(bnkLnRec.getClearedDate());
    CompanyBasic comp = em.find(CompanyBasic.class, bnkLnRec.getComp().getId());
    bnkLn.setComp(comp);
    bnkLn.setDocDate(bnkLnRec.getDocDate());
    DocLineBase fiLn = em.find(DocLineBase.class, fiLnRec.getId());
    bnkLn.setDocFiLine(fiLn);
    bnkLn.setPostDate(bnkLnRec.getPostDate());
    bnkLn.setReceipt(bnkLnRec.isReceipt());
    bnkLn.setValueDate(bnkLnRec.getValueDate());
    switch(bnkLnClass){
     case "DocBankLineBacsRec":
      
      BacsTransCode bacsTransCd = 
        em.find(BacsTransCode.class, ((DocBankLineBacsRec)bnkLnRec).getBacsTransCode().getId(), OPTIMISTIC);
      ((DocBankLineBacs)bnkLn).setBacsTransCode(bacsTransCd);
      if(((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr() != null){
       BankAccount bnkAc = 
         em.find(BankAccount.class, ((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr().getId());
       ((DocBankLineBacs)bnkLn).setBankAccountPtnr(bnkAc);
      }
      break;
     case "DocBankLineChqRec": 
      DocBankLineChqRec chqLnRec = (DocBankLineChqRec)bnkLnRec;
      
      if(((DocBankLineChqRec)bnkLnRec).getFileContent() != null){
       ((DocBankLineChq)bnkLn).setFileContent(((DocBankLineChqRec)bnkLnRec).getFileContent());
      }
      ((DocBankLineChq)bnkLn).setFileType(((DocBankLineChqRec)bnkLnRec).getFileType());
      ((DocBankLineChq)bnkLn).setIssueDate(((DocBankLineChqRec)bnkLnRec).getIssueDate());
      if(((DocBankLineChqRec)bnkLnRec).getPayee() != null){
       PartnerBase ptnr = em.find(PartnerBase.class,((DocBankLineChqRec)bnkLnRec).getPayee().getId());
       ((DocBankLineChq)bnkLn).setPayee(ptnr);
      }
      ((DocBankLineChq)bnkLn).setPrinted(((DocBankLineChqRec)bnkLnRec).isPrinted());
      if(((DocBankLineChqRec)bnkLnRec).getVoidBy() != null){
       User voidBy = em.find(User.class, ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId());
       ((DocBankLineChq)bnkLn).setVoidBy(voidBy);
      }
       if(((DocBankLineChqRec)bnkLnRec).getVoidReason() != null){
        ChequeVoidReason voidRsn = 
          em.find(ChequeVoidReason.class, ((DocBankLineChqRec)bnkLnRec).getVoidReason().getId());
        ((DocBankLineChq)bnkLn).setVoidReason(voidRsn);
       }
       
      
    }
    
   }else{
    // has there been a change
    User chUsr = em.find(User.class, bnkLnRec.getChangedBy().getId());
    DateFormat df = DateFormat.getDateInstance(MEDIUM, bnkLnRec.getComp().getCountry().getLocale());
    LOGGER.log(INFO, "bnkLnRec {0}", bnkLnRec);
    LOGGER.log(INFO, "bnkLn {0}", bnkLn);
    if((bnkLnRec.getClearedBankAc() == null && bnkLn.getClearedBankAc() != null ) ||
      (bnkLnRec.getClearedBankAc() != null && bnkLn.getClearedBankAc() == null )||
      (bnkLnRec.getClearedBankAc() != null && 
      !Objects.equals(bnkLnRec.getClearedBankAc().getId(), bnkLn.getClearedBankAc().getId())  )){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_CL_AC");
     aud.setNewValue(bnkLnRec.getClearedBankAc().getAccountNumber());
     aud.setOldValue(bnkLn.getClearedBankAc().getAccountNumber());
     BankAccountCompany bnkAcntComp = 
       em.find(BankAccountCompany.class, bnkLnRec.getClearedBankAc().getId());
     bnkLn.setClearedBankAc(bnkAcntComp);
     changedBnkLn = true;
    }
    
    if(bnkLnRec.isReceipt() != bnkLn.isReceipt()){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_REC");
     aud.setNewValue(String.valueOf(bnkLnRec.isReceipt()));
     aud.setOldValue(String.valueOf(bnkLn.isReceipt()));
     bnkLn.setReceipt(bnkLnRec.isReceipt());
     changedBnkLn = true;
    }
    if((bnkLnRec.getApAccount() == null && bnkLn.getApAccount() != null ) ||
      (bnkLnRec.getApAccount() != null && bnkLn.getApAccount() == null )||
      (bnkLnRec.getApAccount() != null && 
      !Objects.equals(bnkLnRec.getApAccount().getId(), bnkLn.getApAccount().getId())  )){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_AP_ACNT");
     aud.setNewValue(bnkLnRec.getApAccount().getAccountCode());
     aud.setOldValue(bnkLn.getApAccount().getAccountCode());
     changedBnkLn = true;
    }
    if((bnkLnRec.getArAccount() == null && bnkLn.getArAccount() != null ) ||
      (bnkLnRec.getArAccount() != null && bnkLn.getArAccount() == null )||
      (bnkLnRec.getArAccount() != null && 
      !Objects.equals(bnkLnRec.getArAccount().getId(), bnkLn.getArAccount().getId())  )){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_AR_ACNT");
     aud.setNewValue(bnkLnRec.getArAccount().getArAccountCode());
     aud.setOldValue(bnkLn.getArAccount().getAccountCode());
     changedBnkLn = true;
    }
    
    if((bnkLnRec.getArBank() == null && bnkLn.getArBank() != null ) ||
      (bnkLnRec.getArBank() != null && bnkLn.getArBank() == null )||
      (bnkLnRec.getArBank() != null && 
      !Objects.equals(bnkLnRec.getArBank().getId(), bnkLn.getArBank().getId())  )){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_AR_BNK");
     aud.setNewValue(bnkLnRec.getArBank().getAccountName());
     aud.setOldValue(bnkLn.getArBank().getAccountName());
     ArBankAccount arBank = em.find(ArBankAccount.class, bnkLn.getArBank().getId());
     bnkLn.setArBank(arBank);
     changedBnkLn = true;
    }
    
    if(!StringUtils.equals(bnkLnRec.getBankTransCode(), bnkLn.getBankTransCode())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_BNK_TRANS");
     aud.setNewValue(bnkLnRec.getBankTransCode());
     aud.setOldValue(bnkLn.getBankTransCode());
     bnkLn.setBankTransCode(bnkLnRec.getBankTransCode());
     changedBnkLn = true;
    }
    
    if(!bnkLnRec.getClearedDate().equals(bnkLn.getClearedDate())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_CLR_DT");
     String date = df.format(bnkLnRec.getClearedDate());
     aud.setNewValue(date);
     date = df.format(bnkLn.getClearedDate());
     aud.setNewValue(date);
     bnkLn.setClearedDate(bnkLnRec.getClearedDate());
     changedBnkLn = true;
    }
    
    if(!bnkLnRec.getDocDate().equals(bnkLn.getDocDate())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_DOC_DT");
     String date = df.format(bnkLnRec.getDocDate());
     aud.setNewValue(date);
     date = df.format(bnkLn.getDocDate());
     aud.setNewValue(date);
     bnkLn.setDocDate(bnkLnRec.getDocDate());
     changedBnkLn = true;
    }
    
    if(!bnkLnRec.getPostDate().equals(bnkLn.getPostDate())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_PST_DT");
     String date = df.format(bnkLnRec.getPostDate());
     aud.setNewValue(date);
     date = df.format(bnkLn.getPostDate());
     aud.setNewValue(date);
     bnkLn.setDocDate(bnkLnRec.getPostDate());
     changedBnkLn = true;
     
    }
    
    if(!bnkLnRec.getValueDate().equals(bnkLn.getValueDate())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_VAL_DT");
     String date = df.format(bnkLnRec.getValueDate());
     aud.setNewValue(date);
     date = df.format(bnkLn.getValueDate());
     aud.setNewValue(date);
     bnkLn.setValueDate(bnkLnRec.getValueDate());
     changedBnkLn = true;
    }
    
    if((bnkLnRec.getDocFiLine() == null && bnkLn.getDocFiLine() != null) ||
      (bnkLnRec.getDocFiLine() != null && bnkLn.getDocFiLine() == null) ||
      (bnkLnRec.getDocFiLine() != null &&
       !Objects.equals(bnkLnRec.getDocFiLine().getId(), bnkLn.getDocFiLine().getId()))){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_FI_DOC");
     aud.setNewValue(String.valueOf(bnkLnRec.getDocFiLine().getDocHeaderBase().getDocNumber()));
     aud.setOldValue(String.valueOf(bnkLn.getDocFiLine().getDocHeaderBase().getDocNumber()));
     DocLineBase docLn = em.find(DocLineBase.class, bnkLnRec.getDocFiLine().getId());
     bnkLn.setDocFiLine(docLn);
     changedBnkLn = true;
    }
   
    if((bnkLnRec.getGlAccountComp() == null && bnkLn.isReceipt())){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_FI_DOC");
     aud.setNewValue(String.valueOf(bnkLnRec.getDocFiLine().getDocHeaderBase().getDocNumber()));
     aud.setOldValue(String.valueOf(bnkLn.getDocFiLine().getDocHeaderBase().getDocNumber()));
     changedBnkLn = true;
   }
    
   if((bnkLnRec.getPayRun() == null && bnkLn.getBnkPaymentRun() != null )||
     (bnkLnRec.getPayRun() != null && bnkLn.getBnkPaymentRun() == null) ||
     (bnkLnRec.getPayRun() != null && 
      !Objects.equals(bnkLnRec.getPayRun().getId(), bnkLn.getBnkPaymentRun().getId()))){
    AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_PAY_RUN");
    aud.setNewValue(bnkLnRec.getPayRun().getRef());
    aud.setOldValue(bnkLn.getBnkPaymentRun().getReference());
    BnkPaymentRun payRun = em.find(BnkPaymentRun.class, bnkLnRec.getPayRun().getId());
    bnkLn.setBnkPaymentRun(payRun);
    changedBnkLn = true;
   }
   
   if((bnkLnRec.getRestrictedFund() == null && bnkLn.getRestrictedFund() != null) ||
      (bnkLnRec.getRestrictedFund() != null && bnkLn.getRestrictedFund() == null) ||
      (bnkLnRec.getRestrictedFund() != null && 
     !Objects.equals(bnkLnRec.getRestrictedFund().getId(), bnkLn.getRestrictedFund().getId()))){
    AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_FND");
    aud.setNewValue(bnkLnRec.getRestrictedFund().getFndCode());
    aud.setOldValue(bnkLn.getRestrictedFund().getFndCode());
    Fund fnd = em.find(Fund.class, bnkLnRec.getRestrictedFund().getId());
    bnkLn.setRestrictedFund(fnd);
    changedBnkLn = true;
   }
    
   if((bnkLnRec.getUnClearedBankAc() == null && bnkLn.getUnClearedBankAc() != null ) ||
      (bnkLnRec.getUnClearedBankAc() != null && bnkLn.getUnClearedBankAc() == null )||
      (bnkLnRec.getUnClearedBankAc() != null && 
      !Objects.equals(bnkLnRec.getUnClearedBankAc().getId(), bnkLn.getUnClearedBankAc().getId())  )){
     AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
     aud.setFieldName("BNK_LN_UNCL_AC");
     aud.setNewValue(bnkLnRec.getUnClearedBankAc().getAccountNumber());
     aud.setOldValue(bnkLn.getUnClearedBankAc().getAccountNumber());
     BankAccountCompany bnkAcntComp = 
       em.find(BankAccountCompany.class, bnkLnRec.getUnClearedBankAc().getId());
     bnkLn.setUnClearedBankAc(bnkAcntComp);
     changedBnkLn = true;
    }
   
   switch(bnkLnClass){
    case "DocBankLineBacsRec":
     if((((DocBankLineBacsRec)bnkLnRec).getBacsTransCode() == null && ((DocBankLineBacs)bnkLn).getBacsTransCode() != null) ||
       (((DocBankLineBacsRec)bnkLnRec).getBacsTransCode() != null && ((DocBankLineBacs)bnkLn).getBacsTransCode() != null) ||
       (((DocBankLineBacsRec)bnkLnRec).getBacsTransCode() != null && 
       !Objects.equals(((DocBankLineBacsRec)bnkLnRec).getBacsTransCode().getId(), ((DocBankLineBacs)bnkLn).getBacsTransCode().getId())))
     {
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_LN_BACS_CD");
      aud.setNewValue(((DocBankLineBacsRec)bnkLnRec).getBacsTransCode().getName());
      aud.setOldValue(((DocBankLineBacs)bnkLn).getBacsTransCode().getName());
      BacsTransCode bacsTransCd = 
        em.find(BacsTransCode.class, ((DocBankLineBacsRec)bnkLnRec).getBacsTransCode().getId(), OPTIMISTIC);
      ((DocBankLineBacs)bnkLn).setBacsTransCode(bacsTransCd);
      changedBnkLn = true;
     }
     
     if((((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr() == null && ((DocBankLineBacs)bnkLn).getBankAccountPtnr() != null)||
       (((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr() != null && ((DocBankLineBacs)bnkLn).getBankAccountPtnr() == null) ||
       (((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr() != null && 
       !Objects.equals(((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr().getId(), ((DocBankLineBacs)bnkLn).getBankAccountPtnr().getId()))){
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_LN_AR_BNK");
      aud.setNewValue(((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr().getAccountNumber());
      aud.setOldValue(((DocBankLineBacs)bnkLn).getBankAccountPtnr().getAccountNumber());
      BankAccount bnkAc = 
         em.find(BankAccount.class, ((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr().getId());
      ((DocBankLineBacs)bnkLn).setBankAccountPtnr(bnkAc);
      changedBnkLn = true;
     }
     break;
    case "DocBankLineChqRec": 
     DocBankLineChqRec chqLnRec = (DocBankLineChqRec)bnkLnRec;
     
     if((((DocBankLineChqRec)bnkLnRec).getIssueDate() == null && ((DocBankLineChq)bnkLn).getIssueDate() != null) ||
       (((DocBankLineChqRec)bnkLnRec).getIssueDate() != null && ((DocBankLineChq)bnkLn).getIssueDate() == null) ||
       (((DocBankLineChqRec)bnkLnRec).getIssueDate() != null && 
       !((DocBankLineChqRec)bnkLnRec).getIssueDate().equals(((DocBankLineChq)bnkLn).getIssueDate()))) {
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_LN_CH_ISS");
      String date = df.format(((DocBankLineChqRec)bnkLnRec).getIssueDate());
      aud.setNewValue(date);
      date = df.format(((DocBankLineChq)bnkLn).getIssueDate());
      aud.setOldValue(date);
      ((DocBankLineChq)bnkLn).setIssueDate(((DocBankLineChqRec)bnkLnRec).getIssueDate());
      changedBnkLn = true;
     }
     
     if((((DocBankLineChqRec)bnkLnRec).getPayee() == null && ((DocBankLineChq)bnkLn).getPayee() != null )||
       (((DocBankLineChqRec)bnkLnRec).getPayee() != null && ((DocBankLineChq)bnkLn).getPayee() == null ) || 
       (((DocBankLineChqRec)bnkLnRec).getPayee() != null && 
       !Objects.equals(((DocBankLineChqRec)bnkLnRec).getPayee().getId(), ((DocBankLineChqRec)bnkLnRec).getPayee().getId()))){
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_LN_PAYEE");
      aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getPayee().getName());
      aud.setOldValue(((DocBankLineChq)bnkLn).getPayee().getTradingName());
      PartnerBase payee = em.find(PartnerBase.class, ((DocBankLineChqRec)bnkLnRec).getPayee().getId());
      ((DocBankLineChq)bnkLn).setPayee(payee);
      changedBnkLn = true;
     }
     
     if(((DocBankLineChq)bnkLn).isPrinted() != ((DocBankLineChqRec)bnkLnRec).isPrinted() ){
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_CHQ_PRNT");
      aud.setNewValue(String.valueOf(((DocBankLineChqRec)bnkLnRec).isPrinted()));
      aud.setOldValue(String.valueOf(((DocBankLineChq)bnkLn).isPrinted()));
      changedBnkLn = true;
     }
     
     if((((DocBankLineChqRec)bnkLnRec).getVoidDate() == null && ((DocBankLineChq)bnkLn).getVoidDate() != null) ||
       (((DocBankLineChqRec)bnkLnRec).getVoidDate() != null && ((DocBankLineChq)bnkLn).getVoidDate() == null) ||
       (((DocBankLineChqRec)bnkLnRec).getVoidDate() != null && 
       !((DocBankLineChqRec)bnkLnRec).getVoidDate().equals(((DocBankLineChq)bnkLn).getVoidDate()))) {
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_LN_CH_ISS");
      String date = df.format(((DocBankLineChqRec)bnkLnRec).getVoidDate());
      aud.setNewValue(date);
      date = df.format(((DocBankLineChq)bnkLn).getVoidDate());
      aud.setOldValue(date);
      ((DocBankLineChq)bnkLn).setIssueDate(((DocBankLineChqRec)bnkLnRec).getVoidDate());
      changedBnkLn = true;
     }
     
     if((((DocBankLineChqRec)bnkLnRec).getVoidBy() == null && ((DocBankLineChq)bnkLn).getVoidBy() != null )||
       (((DocBankLineChqRec)bnkLnRec).getVoidBy() != null && ((DocBankLineChq)bnkLn).getVoidBy() == null ) || 
       (((DocBankLineChqRec)bnkLnRec).getVoidBy() != null && 
       !Objects.equals(((DocBankLineChqRec)bnkLnRec).getVoidBy().getId(), ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId()))){
      AuditDocBankLine aud = this.buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
      aud.setFieldName("BNK_CHQ_VOID_BY");
      aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getVoidBy().getName());
      aud.setOldValue(((DocBankLineChq)bnkLn).getVoidBy().getTradingName());
      User voidBy = em.find(User.class, ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId());
      ((DocBankLineChq)bnkLn).setVoidBy(voidBy);
      changedBnkLn = true;
     }
     
      ((DocBankLineChq)bnkLn).setPrinted(((DocBankLineChqRec)bnkLnRec).isPrinted());
      if(((DocBankLineChqRec)bnkLnRec).getVoidBy() != null){
       User voidBy = em.find(User.class, ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId());
       ((DocBankLineChq)bnkLn).setVoidBy(voidBy);
      }
       if(((DocBankLineChqRec)bnkLnRec).getVoidReason() != null){
        ChequeVoidReason voidRsn = 
          em.find(ChequeVoidReason.class, ((DocBankLineChqRec)bnkLnRec).getVoidReason().getId());
        ((DocBankLineChq)bnkLn).setVoidReason(voidRsn);
       }
       
      
    }
    
   if(changedBnkLn){
    bnkLn.setChangedBy(chUsr);
    bnkLn.setChangedOn(new Date());
   }
    
   }
   return bnkLn;
  }
  
  public DocBankLineBase buildDocBankLine(BnkPaymentRun payRun,DocBankLineBaseRec bnkLine,
         DocLineBase fiDocLine,UserRec usr, Date date, String page) throws BacException {
  LOGGER.log(INFO, "TreasuryDM.createBankLine called with payRun {0} bnkLine {1} fi doc ln {2}", 
          new Object[]{payRun,bnkLine,fiDocLine});
  DocBankLineBase bnkLnDB;
  
  
  User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  if(bnkLine == null){
   throw new BacException("null bnkLine passed to buildDocBankLine: "+bnkLine);
  }
  LOGGER.log(INFO, "bnkLine.getId() {0}", bnkLine.getId());
  if(bnkLine.getId() > 0){
   // preexisting bank line
   bnkLnDB = this.em.find(DocBankLineBase.class, bnkLine.getId(), OPTIMISTIC);
   bnkLnDB.setChangedBy(crUsr);
   bnkLnDB.setChangedOn(date);
  }else{
   // new bank line
   if(bnkLine.getClass().getSimpleName().equalsIgnoreCase("DocBankLineBaseRec")){
    bnkLnDB = new DocBankLineBase();
   }else if(bnkLine.getClass().getSimpleName().equalsIgnoreCase("DocBankLineBacsRec")){
    LOGGER.log(INFO, "BACS bank line amount {0}",bnkLine.getAmount());
    bnkLnDB = new DocBankLineBacs();
    DocBankLineBacsRec bnkBacsLine = (DocBankLineBacsRec)bnkLine;
     BacsTransCode bacsTransCd = 
             em.find(BacsTransCode.class, bnkBacsLine.getBacsTransCode().getId(), OPTIMISTIC);
    ((DocBankLineBacs)bnkLnDB).setBacsTransCode(bacsTransCd);
     BankAccount bnkAc = em.find(BankAccount.class, bnkBacsLine.getBankAccountPtnr().getId(), OPTIMISTIC);
     ((DocBankLineBacs)bnkLnDB).setBankAccountPtnr(bnkAc);
   }else{
    throw new BacException("Bank doc line class invalid");
   }
   bnkLnDB.setCreatedBy(crUsr);
   bnkLnDB.setCreatedOn(date);
   em.persist(bnkLnDB);
  }
  bnkLnDB.setAmount(bnkLine.getAmount());
  if(bnkLine.getArBank()!= null){
   ArBankAccount arBank = em.find(ArBankAccount.class, bnkLine.getArBank().getId(), OPTIMISTIC);
   bnkLnDB.setArBank(arBank);
  }
  if(payRun != null){
   bnkLnDB.setBnkPaymentRun(payRun);
  }
  
  bnkLnDB.setBnkRef(bnkLine.getBnkRef());
  if(bnkLine.getBnkStament() != null){
   BankStatement stat = bnkLnDB.getBnkStament();
   bnkLnDB.setBnkStament(stat);
  }
  
  if(bnkLine.getComp() != null){
   LOGGER.log(INFO, "bnkLine.getComp() {0} with id {1}", new Object[]{bnkLine.getComp(),bnkLine.getComp().getId()});
   CompanyBasic comp = sysBuff.getComp(bnkLine.getComp());
   LOGGER.log(INFO, "comp from sys buff {0}",comp);
   if(comp == null){
    throw new BacException("Company invalid");
   }
   bnkLnDB.setComp(comp);
  }
  bnkLnDB.setDocDate(bnkLine.getDocDate());
  
  bnkLnDB.setPostDate(bnkLine.getPostDate());
  bnkLnDB.setReceipt(bnkLine.isReceipt());
  if(bnkLine.getUnClearedBankAc() != null){
   BankAccountCompany bnkAc = em.find(BankAccountCompany.class, bnkLine.getUnClearedBankAc().getId(), OPTIMISTIC);
   bnkLnDB.setUnClearedBankAc(bnkAc);
  }
  if(bnkLine.getClearedBankAc() != null){
   BankAccountCompany bnkAc = em.find(BankAccountCompany.class, bnkLine.getUnClearedBankAc().getId(), OPTIMISTIC);
   bnkLnDB.setClearedBankAc(bnkAc);
  }
  
  return bnkLnDB;
 }
  
 public DocBankLineBase buildDocBankLine(DocBankLineBaseRec bnkLnRec,  String pg ){
  LOGGER.log(INFO, "buildDocBankLine called with {0} class {1}",new Object[]{bnkLnRec,bnkLnRec.getClass().getSimpleName()});
  
  String className = bnkLnRec.getClass().getSimpleName();
  DocBankLineBase bnkLn;
  boolean newBnkLn = false;
  boolean changedBnkln = false;
  if(bnkLnRec.getId() == null){
   if(StringUtils.equals(className, "DocBankLineChqRec")){
    bnkLn = new DocBankLineChq();
   }else{
    bnkLn = new DocBankLineBacs();
   }
   newBnkLn = true;
   User crUsr = em.find(User.class, bnkLnRec.getCreatedBy().getId());
   bnkLn.setCreatedBy(crUsr);
   bnkLn.setCreatedOn(bnkLnRec.getCreatedOn());
   em.persist(bnkLn);
   AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, crUsr, pg, 'I');
  }else{
   bnkLn = em.find(DocBankLineBase.class, bnkLnRec.getId());
  }
  
  if(newBnkLn){
   bnkLn.setAmount(bnkLnRec.getAmount());
   if(bnkLnRec.getApAccount() != null){
    ApAccount apAcnt = em.find(ApAccount.class, bnkLnRec.getApAccount().getId());
    bnkLn.setApAccount(apAcnt);
   }
   if(bnkLnRec.getArAccount() != null){
    ArAccount arAcnt = em.find(ArAccount.class, bnkLnRec.getApAccount().getId());
    bnkLn.setArAccount(arAcnt);
   }
   if(bnkLnRec.getArBank() != null){
    ArBankAccount arBnk = em.find(ArBankAccount.class, bnkLnRec.getArBank().getBankAccount());
    bnkLn.setArBank(arBnk);
   }
   bnkLn.setBankTransCode(bnkLnRec.getBankTransCode());
   if(bnkLnRec.getPayRun() != null){
    BnkPaymentRun payRun = em.find(BnkPaymentRun.class, bnkLnRec.getPayRun().getId());
    bnkLn.setBnkPaymentRun(payRun);
   }
   bnkLn.setBnkRef(bnkLnRec.getBnkRef());
   if(bnkLnRec.getBnkStament() != null){
    BankStatement stmnt = em.find(BankStatement.class, bnkLnRec.getBnkStament().getId());
    bnkLn.setBnkStament(stmnt);
   }
   bnkLn.setCleared(bnkLnRec.isCleared());
   if(bnkLnRec.getClearedBankAc() != null){
    BankAccountCompany clrBnkAc = em.find(BankAccountCompany.class, bnkLnRec.getClearedBankAc().getId());
    bnkLn.setClearedBankAc(clrBnkAc);
   }
   bnkLn.setClearedDate(bnkLnRec.getClearedDate());
   CompanyBasic comp = sysBuff.getComp(bnkLnRec.getComp());
   bnkLn.setComp(comp);
   bnkLn.setDocDate(bnkLnRec.getDocDate());
   if(bnkLnRec.getDocFiLine() != null){
    DocLineBase docLn = em.find(DocLineBase.class, bnkLnRec.getDocFiLine().getId());
    bnkLn.setDocFiLine(docLn);
   }
   bnkLn.setPostDate(bnkLnRec.getPostDate());
   bnkLn.setReceipt(bnkLnRec.isReceipt());
   if(bnkLnRec.getRestrictedFund() != null){
    Fund fnd = em.find(Fund.class,bnkLnRec.getRestrictedFund().getId());
    bnkLn.setRestrictedFund(fnd);
   }
   if(bnkLnRec.getUnClearedBankAc() != null){
    BankAccountCompany unClrBnkAc = em.find(BankAccountCompany.class, bnkLnRec.getUnClearedBankAc().getId());
    bnkLn.setUnClearedBankAc(unClrBnkAc);
   }
   bnkLn.setValueDate(bnkLn.getValueDate());
   
   
   if(StringUtils.equals(className, "DocBankLineChqRec")){
    // cheque only details
    ((DocBankLineChq)bnkLn).setIssueDate(((DocBankLineChqRec)bnkLnRec).getIssueDate());
    if(((DocBankLineChqRec)bnkLnRec).getPayee() != null){
     PartnerBase ptnr = em.find(PartnerBase.class, ((DocBankLineChqRec)bnkLnRec).getPayee().getId());
     ((DocBankLineChq)bnkLn).setPayee(ptnr);
    }
    ((DocBankLineChq)bnkLn).setPrinted(((DocBankLineChqRec)bnkLnRec).isPrinted());
    if(((DocBankLineChqRec)bnkLnRec).getVoidBy() != null){
     User voidUsr = em.find(User.class, ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId());
     ((DocBankLineChq)bnkLn).setVoidBy(voidUsr);
    }
    ((DocBankLineChq)bnkLn).setVoidDate(((DocBankLineChqRec)bnkLnRec).getVoidDate());
    if(((DocBankLineChqRec)bnkLnRec).getVoidReason() != null){
     ChequeVoidReason voidRsn = em.find(ChequeVoidReason.class, ((DocBankLineChqRec)bnkLnRec).getVoidReason().getId());
     ((DocBankLineChq)bnkLn).setVoidReason(voidRsn);
    }
   }
   if(StringUtils.equals(className, "DocBankLineBacsRec")){
    DocBankLineBacsRec bacs;
    
    if(((DocBankLineBacsRec)bnkLnRec).getBacsTransCode() != null){
     BacsTransCode bacsCd = em.find(BacsTransCode.class, ((DocBankLineBacsRec)bnkLnRec).getBacsTransCode().getId());
     ((DocBankLineBacs)bnkLn).setBacsTransCode(bacsCd);
    }
    if(((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr() != null){
     BankAccount bnk = em.find(BankAccount.class,((DocBankLineBacsRec)bnkLnRec).getBankAccountPtnr().getId());
     ((DocBankLineBacs)bnkLn).setBankAccountPtnr(bnk);
    }
    ((DocBankLineBacs)bnkLn).setBacsPtnrName(((DocBankLineBacs)bnkLn).getBacsPtnrName());
   }
   
  }else{
   // changed ?
   User chUsr = em.find(User.class, bnkLnRec.getChangedBy().getId());
   if(bnkLnRec.isCleared() != bnkLn.isCleared()){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_CLR");
    aud.setNewValue(String.valueOf(bnkLnRec.isCleared()));
    aud.setOldValue(String.valueOf(bnkLn.isCleared()));
    bnkLn.setCleared(bnkLnRec.isCleared());
    changedBnkln = true;
   }
   
   if((bnkLnRec.getClearedBankAc() == null && bnkLn.getClearedBankAc() != null) ||
      (bnkLnRec.getClearedBankAc() != null && bnkLn.getClearedBankAc() == null) ||
      (bnkLnRec.getClearedBankAc() != null && 
     !Objects.equals(bnkLnRec.getClearedBankAc().getId(), bnkLn.getClearedBankAc().getId()))
     ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_CL_AC");
    aud.setNewValue(bnkLnRec.getClearedBankAc().getAccountNumber());
    aud.setOldValue(bnkLn.getClearedBankAc().getAccountNumber());
    BankAccountCompany clrBnk = em.find(BankAccountCompany.class, bnkLnRec.getClearedBankAc().getId());
    bnkLn.setClearedBankAc(clrBnk);
    changedBnkln = true;
   }
   if((bnkLnRec.getClearedDate() == null && bnkLn.getClearedDate() != null) ||
      (bnkLnRec.getClearedDate() != null && bnkLn.getClearedDate() == null) ||
      bnkLnRec.getClearedDate() == null && !bnkLnRec.getClearedDate().equals(bnkLn.getClearedDate())){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_CLR_DT");
    String dateStr = GenUtilServer.formatDateStringLocalised(bnkLnRec.getClearedDate(), bnkLnRec.getComp().getLocale());
    aud.setNewValue(dateStr);
    dateStr = GenUtilServer.formatDateStringLocalised(bnkLn.getClearedDate(), bnkLnRec.getComp().getLocale());
    aud.setOldValue(dateStr);
    changedBnkln = true;
   }
   
   if((bnkLnRec.getUnClearedBankAc() == null && bnkLn.getUnClearedBankAc() != null) ||
      (bnkLnRec.getUnClearedBankAc() != null && bnkLn.getUnClearedBankAc() == null) ||
      (bnkLnRec.getUnClearedBankAc() != null && 
     !Objects.equals(bnkLnRec.getUnClearedBankAc().getId(), bnkLn.getUnClearedBankAc().getId()))
     ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_UNCL_AC");
    aud.setNewValue(bnkLnRec.getUnClearedBankAc().getAccountNumber());
    aud.setOldValue(bnkLn.getUnClearedBankAc().getAccountNumber());
    BankAccountCompany unClrBnk = em.find(BankAccountCompany.class, bnkLnRec.getUnClearedBankAc().getId());
    bnkLn.setUnClearedBankAc(unClrBnk);
    changedBnkln = true;
   }
   
   // cheque changes
   
   if(((DocBankLineChqRec)bnkLnRec).isPrinted() != ((DocBankLineChq)bnkLn).isPrinted() ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_CHQ_PRNT");
    aud.setNewValue(String.valueOf(((DocBankLineChqRec)bnkLnRec).isPrinted()));
    aud.setOldValue(String.valueOf(((DocBankLineChq)bnkLn).isPrinted()));
    ((DocBankLineChq)bnkLn).setPrinted(((DocBankLineChqRec)bnkLnRec).isPrinted());
    changedBnkln = true;
   }
   if(
    (((DocBankLineChqRec)bnkLnRec).getFileType() == null && ((DocBankLineChq)bnkLn).getFileType() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getFileType() != null && ((DocBankLineChq)bnkLn).getFileType() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getFileType() != null && 
      !StringUtils.equals(((DocBankLineChqRec)bnkLnRec).getFileType(),((DocBankLineChq)bnkLn).getFileType()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_CH_F");
    aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getFileType());
    aud.setOldValue(((DocBankLineChq)bnkLn).getFileType());
    ((DocBankLineChq)bnkLn).setFileContent(((DocBankLineChqRec)bnkLnRec).getFileContent());
    ((DocBankLineChq)bnkLn).setFileType(((DocBankLineChqRec)bnkLnRec).getFileType());
    changedBnkln = true;
   }
   
   if((((DocBankLineChqRec)bnkLnRec).getIssueDate() == null && ((DocBankLineChq)bnkLn).getIssueDate() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getIssueDate() != null && ((DocBankLineChq)bnkLn).getIssueDate() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getIssueDate() != null && 
      !((DocBankLineChqRec)bnkLnRec).getIssueDate().equals(((DocBankLineChq)bnkLn).getIssueDate()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_CH_ISS");
    String dateStr = GenUtilServer.formatDateStringLocalised(((DocBankLineChqRec)bnkLnRec).getIssueDate(), 
      bnkLnRec.getComp().getLocale());
    aud.setNewValue(dateStr);
    dateStr = GenUtilServer.formatDateStringLocalised(((DocBankLineChq)bnkLn).getIssueDate(), 
      bnkLnRec.getComp().getLocale());
    aud.setOldValue(dateStr);
    ((DocBankLineChq)bnkLn).setIssueDate(((DocBankLineChqRec)bnkLnRec).getIssueDate());
    changedBnkln = true;
   }
   
   if((((DocBankLineChqRec)bnkLnRec).getPayee() == null && ((DocBankLineChq)bnkLn).getPayee() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getPayee() != null && ((DocBankLineChq)bnkLn).getPayee() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getPayee() != null && 
      !Objects.equals(((DocBankLineChqRec)bnkLnRec).getPayee().getId(), ((DocBankLineChq)bnkLn).getPayee().getId()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_LN_PAYEE");
    aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getPayee().getName());
    aud.setOldValue(((DocBankLineChq)bnkLn).getPayee().getTradingName());
    PartnerBase payee = em.find(PartnerBase.class, ((DocBankLineChqRec)bnkLnRec).getPayee().getId());
    ((DocBankLineChq)bnkLn).setPayee(payee);
    changedBnkln = true;
   }
   
   if((((DocBankLineChqRec)bnkLnRec).getVoidBy() == null && ((DocBankLineChq)bnkLn).getVoidBy() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidBy() != null && ((DocBankLineChq)bnkLn).getVoidBy() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidBy() != null && 
      !Objects.equals(((DocBankLineChqRec)bnkLnRec).getVoidBy().getId(), ((DocBankLineChq)bnkLn).getVoidBy().getId()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_CHQ_VOID_BY");
    aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getVoidBy().getName());
    aud.setOldValue(((DocBankLineChq)bnkLn).getVoidBy().getTradingName());
    User voidBy = em.find(User.class, ((DocBankLineChqRec)bnkLnRec).getVoidBy().getId());
    ((DocBankLineChq)bnkLn).setVoidBy(voidBy);
    changedBnkln = true;
   }
   
   if((((DocBankLineChqRec)bnkLnRec).getVoidDate() == null && ((DocBankLineChq)bnkLn).getVoidDate() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidDate() != null && ((DocBankLineChq)bnkLn).getVoidDate() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidDate() != null && 
      !((DocBankLineChqRec)bnkLnRec).getVoidDate().equals(((DocBankLineChq)bnkLn).getVoidDate()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_CHQ_VOID_DT");
    String dateStr = GenUtilServer.formatDateStringLocalised(((DocBankLineChqRec)bnkLnRec).getVoidDate(), 
      bnkLnRec.getComp().getLocale());
    aud.setNewValue(dateStr);
    dateStr = GenUtilServer.formatDateStringLocalised(((DocBankLineChq)bnkLn).getVoidDate(), 
      bnkLnRec.getComp().getLocale());
    aud.setOldValue(dateStr);
    ((DocBankLineChq)bnkLn).setVoidDate(((DocBankLineChqRec)bnkLnRec).getVoidDate());
    changedBnkln = true;
   }
   
   if((((DocBankLineChqRec)bnkLnRec).getVoidReason() == null && ((DocBankLineChq)bnkLn).getVoidReason() != null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidReason() != null && ((DocBankLineChq)bnkLn).getVoidReason() == null) ||
    (((DocBankLineChqRec)bnkLnRec).getVoidReason() != null && 
      !Objects.equals(((DocBankLineChqRec)bnkLnRec).getVoidReason().getId(), ((DocBankLineChq)bnkLn).getVoidReason().getId()))
    ){
    AuditDocBankLine aud = buildAuditDocBankLine(bnkLn, chUsr, pg, 'U');
    aud.setFieldName("BNK_CHQ_VOID_CD");
    aud.setNewValue(((DocBankLineChqRec)bnkLnRec).getVoidReason().getCode());
    aud.setOldValue(((DocBankLineChq)bnkLn).getVoidReason().getCode());
    ChequeVoidReason voidRsn = em.find(ChequeVoidReason.class, ((DocBankLineChqRec)bnkLnRec).getVoidReason().getId());
    ((DocBankLineChq)bnkLn).setVoidReason(voidRsn);
    changedBnkln = true;
   }
   
   
  }
  
  
  return bnkLn;
 }
  
 private DocBankLineBaseRec buildDocBankLine4ApLineRec(DocLineAp apLine, NumberRangeCheque chqBk, 
   UserRec usrRec, String startNum){
  
  DocBankLineBaseRec retBnkLnRec;
  PaymentTypeRec payTyRec = sysBuff.getPaymentType(apLine.getPayType());
  Date currDate = new Date();
  if(apLine.getPayType().getPayMedium().equals("CHQ")){
   retBnkLnRec = new DocBankLineChqRec();
   //get cheque number
    
  }else{
   retBnkLnRec = new DocBankLineBacsRec();
   
  }
  retBnkLnRec.setAmount(apLine.getDocAmount());
  ApAccountRec apAcntRec = apAcntDM.getApAccountRec(apLine.getApAccount());
  retBnkLnRec.setApAccount(apAcntRec);
  // get ch
  /*AR bank = bacs 
  */ 
  
  CompanyBasicRec compRec = sysBuff.getCompanyById(apLine.getComp().getId());
  retBnkLnRec.setComp(compRec);
  
  retBnkLnRec.setCreatedBy(usrRec);
  retBnkLnRec.setCreatedOn(new Date());
  retBnkLnRec.setDocDate(currDate);
  retBnkLnRec.setPostDate(currDate);
  retBnkLnRec.setReceipt(payTyRec.isInbound());
  retBnkLnRec.setUnClearedBankAc(payTyRec.getPayTypeForBankAccount());
  
  if(apLine.getPayType().getPayMedium().equals("CHQ")){
   
   String chqNum;
   if(chqBk.isAutoNum()){
    // get next cheque number
    int nextNum = this.configDM.getNextNum4NumRangeId(chqBk.getId());
    chqNum = String.valueOf(nextNum);
   }else{
    chqNum = startNum;
   }
   chqNum = StringUtils.leftPad(chqNum, chqBk.getBankAccountComp().getChequeNumLen(), '0');
   retBnkLnRec.setBnkRef(chqNum);
   PartnerBaseRec ptnr = masterDataDM.buildPartnerBaseRecPvt(apLine.getApAccount().getApAccountFor());
   ((DocBankLineChqRec)retBnkLnRec).setPayee(ptnr);
  }else{
   // bacs payment
   DocBankLineBacsRec bacs;
   ArBankAccount ptnrBankAc = apLine.getApAccount().getApAccountBanks().get(0);
   retBnkLnRec.setBnkRef(apLine.getApAccount().getApAccountBanks().get(0).getDdRef());
   BacsTransCodeRec bacsTransCd = this.sysBuff.getBacsTransactionCodeByCode("99");
   ((DocBankLineBacsRec)retBnkLnRec).setBacsTransCode(bacsTransCd);
   ((DocBankLineBacsRec)retBnkLnRec).setBacsPtnrName(apLine.getApAccount().getApAccountFor().getTradingName());
  }
  
  return retBnkLnRec;
 }
 
 
  private DocBankLineBaseRec buildDocBankLineRec(DocBankLineBase bnkLn){
   DocBankLineBaseRec bnkLnRec;
   LOGGER.log(INFO, " buildDocBankLineRec called with class {0}", bnkLn.getClass().getSimpleName());
   String className = bnkLn.getClass().getSimpleName();
   
   if(className.equals("DocBankLineChq")){
    bnkLnRec = new DocBankLineChqRec();
   }else{
    bnkLnRec = new DocBankLineBacsRec();
   }
   bnkLnRec.setAmount(bnkLn.getAmount());
   bnkLnRec.setBankTransCode(bnkLn.getBankTransCode());
   bnkLnRec.setBnkRef(bnkLn.getBnkRef());
   bnkLnRec.setCleared(bnkLn.isCleared());
   bnkLnRec.setClearedDate(bnkLn.getClearedDate());
   
   CompanyBasicRec comp = sysBuff.getCompanyById(bnkLn.getComp().getId());
   bnkLnRec.setComp(comp);
   UserRec crUsr = this.usrDM.getUserRecPvt(bnkLn.getCreatedBy());
   bnkLnRec.setCreatedBy(crUsr);
   bnkLnRec.setCreatedOn(bnkLn.getCreatedOn());
   if(bnkLn.getChangedBy() != null){
    UserRec chUsr = this.usrDM.getUserRecPvt(bnkLn.getChangedBy());
    bnkLnRec.setChangedBy(chUsr);
    bnkLnRec.setChangedOn(bnkLn.getChangedOn());
   }
   bnkLnRec.setDocDate(bnkLn.getDocDate());
   bnkLnRec.setId(bnkLn.getId());
   bnkLnRec.setPostDate(bnkLn.getPostDate());
   bnkLnRec.setReceipt(bnkLn.isReceipt());
   bnkLnRec.setValueDate(bnkLn.getValueDate());
   
   
   if(className.equals("DocBankLineChq")){
    // cheque settings 
    ((DocBankLineChqRec)bnkLnRec).setIssueDate(((DocBankLineChq)bnkLn).getIssueDate());
    ((DocBankLineChqRec)bnkLnRec).setPrinted(((DocBankLineChq)bnkLn).isPrinted());
    if(((DocBankLineChq)bnkLn).getVoidBy() != null){
     UserRec vUsr = this.usrDM.getUserRecPvt(((DocBankLineChq)bnkLn).getVoidBy());
     ((DocBankLineChqRec)bnkLnRec).setVoidBy(vUsr);
     ((DocBankLineChqRec)bnkLnRec).setVoidDate(((DocBankLineChq)bnkLn).getVoidDate());
     ChequeVoidReasonRec rsn = this.sysBuff.getChqVoidReasonById(((DocBankLineChq)bnkLn).getVoidReason().getId());
     ((DocBankLineChqRec)bnkLnRec).setVoidReason(rsn);
    }
    if(((DocBankLineChq)bnkLn).getPayee() != null){
     PartnerBase p = ((DocBankLineChq)bnkLn).getPayee();
     PartnerBaseRec payee ;
     if(p.getClass().getSimpleName().equals("PartnerPerson")){
      payee = masterDataDM.buildPartnerPersonRecPvt((PartnerPerson)p);
     }else{
      payee = masterDataDM.getPartnerCorporateRec((PartnerCorporate)p);
     }
     ((DocBankLineChqRec)bnkLnRec).setPayee(payee);
    }else if(bnkLn.getApAccount() != null ){
     PartnerBase p = bnkLn.getApAccount().getApAccountFor();
     PartnerBaseRec payee ;
     if(p.getClass().getSimpleName().equals("PartnerPerson")){
      payee = masterDataDM.buildPartnerPersonRecPvt((PartnerPerson)p);
     }else{
      payee = masterDataDM.getPartnerCorporateRec((PartnerCorporate)p);
     }
     ((DocBankLineChqRec)bnkLnRec).setPayee(payee);
    }else if(bnkLn.getArAccount() != null ){
     PartnerBase p = bnkLn.getArAccount().getArAccountFor();
     PartnerBaseRec payee ;
     if(p.getClass().getSimpleName().equals("PartnerPerson")){
      payee = masterDataDM.buildPartnerPersonRecPvt((PartnerPerson)p);
     }else{
      payee = masterDataDM.getPartnerCorporateRec((PartnerCorporate)p);
     }
     ((DocBankLineChqRec)bnkLnRec).setPayee(payee);
    }
    LOGGER.log(INFO,"Chq payee {0}",((DocBankLineChqRec)bnkLnRec).getPayee());
   } else{
    //BACS 
    BacsTransCodeRec bacsCode = this.sysBuff.getBacsTransactionCodeByCode(((DocBankLineBacs)bnkLn).getBacsTransCode().getPtnrBnkTransCode());
    ((DocBankLineBacsRec)bnkLnRec).setBacsTransCode(bacsCode);
   }
   
  
   
   
   return bnkLnRec;
  } 
  
private ChequeTemplate buildChequeTemplate(ChequeTemplateRec tmplRec, String view){
 LOGGER.log(INFO, "buildChequeTemplate called with tmpl ref {0} ", tmplRec.getReference());
 LOGGER.log(INFO, "templ created by id {0} ", tmplRec.getCreatedBy().getId());
 
 ChequeTemplate tmpl;
 boolean newTmpl = false;
 boolean changedTempl = false;
 
 if(tmplRec.getId() == null){
  tmpl = new ChequeTemplate();
  User crUsr = em.find(User.class,tmplRec.getCreatedBy().getId() );
  tmpl.setCreatedBy(crUsr);
  tmpl.setCreatedOn(tmplRec.getCreatedOn());
  
  em.persist(tmpl);
  AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, crUsr, view, 'I');
  aud.setNewValue(tmplRec.getReference());
  newTmpl = true;
 }else{
  tmpl = em.find(ChequeTemplate.class, tmplRec.getId());
 }
 
 if(newTmpl){
  tmpl.setDescription(tmplRec.getDescription());
  tmpl.setOriginalData(tmplRec.getOriginalData());
  tmpl.setOriginalFileExt(tmplRec.getOriginalFileExt());
  tmpl.setOriginalFileName(tmplRec.getOriginalFileName());
  tmpl.setPdfData(tmplRec.getPdfData());
  tmpl.setPdfFileExt(tmplRec.getPdfFileExt());
  tmpl.setPdfFileName(tmplRec.getPdfFileName());
  tmpl.setReference(tmplRec.getReference());
  tmpl.setPrintChqNumLine(tmplRec.isPrintChqNumLine());
  
 }else{
  // changed ??
  User chUsr = em.find(User.class, tmplRec.getChangedBy().getId());
  if(!StringUtils.equals(tmplRec.getReference(), tmpl.getReference())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_REF");
   aud.setNewValue(tmplRec.getReference());
   aud.setOldValue(tmpl.getReference());
   tmpl.setReference(tmplRec.getReference());
   changedTempl = true;
  }
  
  if(!StringUtils.equals(tmplRec.getDescription(), tmpl.getDescription())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_DESCR");
   aud.setNewValue(tmplRec.getDescription());
   aud.setOldValue(tmpl.getDescription());
   tmpl.setDescription(tmplRec.getDescription());
   changedTempl = true;
  }
  
  if(!StringUtils.equals(tmplRec.getOriginalFileExt(), tmpl.getOriginalFileExt())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_ORIG_FEXT");
   aud.setNewValue(tmplRec.getOriginalFileExt());
   aud.setOldValue(tmpl.getOriginalFileExt());
   tmpl.setOriginalFileExt(tmplRec.getOriginalFileExt());
   changedTempl = true;
  }
  
  if(!StringUtils.equals(tmplRec.getPdfFileExt(), tmpl.getPdfFileExt())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_ORIG_FEXT");
   aud.setNewValue(tmplRec.getPdfFileExt());
   aud.setOldValue(tmpl.getPdfFileExt());
   tmpl.setPdfFileExt(tmplRec.getPdfFileExt());
   changedTempl = true;
  }
  
  if(!StringUtils.equals(tmplRec.getOriginalFileName(), tmpl.getOriginalFileName())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_ORIG_F_NM");
   aud.setNewValue(tmplRec.getOriginalFileName());
   aud.setOldValue(tmpl.getOriginalFileName());
   tmpl.setOriginalFileName(tmplRec.getOriginalFileName());
   changedTempl = true;
  }
  
  if(!StringUtils.equals(tmplRec.getPdfFileName(), tmpl.getPdfFileName())){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_PDF_F_NM");
   aud.setNewValue(tmplRec.getPdfFileName());
   aud.setOldValue(tmpl.getPdfFileName());
   tmpl.setPdfFileName(tmplRec.getPdfFileName());
   changedTempl = true;
  }
  
  
  if(tmplRec.getOriginalData() != tmpl.getOriginalData()){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_ORIG");
   tmpl.setOriginalData(tmplRec.getOriginalData());
   changedTempl = true;
  }
  
  if(tmplRec.getPdfData() != tmpl.getPdfData()){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_PDF");
   tmpl.setPdfData(tmplRec.getPdfData());
   changedTempl = true;
  }
  
  if(tmplRec.isPrintChqNumLine() != tmpl.isPrintChqNumLine()){
   AuditChequeTemplate aud = this.buildAuditChequeTemplate(tmpl, chUsr, view, 'U');
   aud.setFieldName("CHQ_TMPL_PRNT_BNK_LN");
   aud.setOldValue(String.valueOf(tmpl.isPrintChqNumLine()));
   aud.setNewValue(String.valueOf(tmplRec.isPrintChqNumLine()));
   tmpl.setPrintChqNumLine(tmplRec.isPrintChqNumLine());
   changedTempl = true;
  }
  
  if(changedTempl){
   tmpl.setChangedBy(chUsr);
   tmpl.setChangedOn(tmplRec.getChangedOn());
  }
  
 }
 
 return tmpl;
 
}

private ChequeTemplateRec buildChequeTemplateRec(ChequeTemplate templ){
 
 ChequeTemplateRec rec = new ChequeTemplateRec();
 rec.setId(templ.getId());
 if(templ.getChangedBy() != null){
  UserRec chUsr = this.usrDM.getUserRecPvt(templ.getChangedBy());
  rec.setChangedBy(chUsr);
  rec.setChangedOn(templ.getChangedOn());
 }
 UserRec crUsr = this.usrDM.getUserRecPvt(templ.getCreatedBy());
 rec.setCreatedBy(crUsr);
 rec.setCreatedOn(templ.getCreatedOn());
 rec.setDescription(templ.getDescription());
 rec.setOriginalFileExt(templ.getOriginalFileExt());
 rec.setOriginalFileName(templ.getOriginalFileName());
 rec.setPdfFileExt(templ.getPdfFileExt());
 rec.setPdfFileName(templ.getPdfFileName());
 rec.setReference(templ.getReference());
 rec.setPrintChqNumLine(templ.isPrintChqNumLine());
 rec.setPrintChqNumLine(templ.isPrintChqNumLine());
 
 return rec;
}

  public Bank getBank(BankRec rec, String pg){
    Bank bnk = this.buildBank(rec, pg);
    return bnk;
  }

  public BankBranch getBankBranch(BankBranchRec rec, Bank bnk,  String pg){
    BankBranch branch = this.buildBankBranch(rec,   pg);
    return branch;
  }
  
  
  public BankAccountRec getBankAccountRec(BankAccount b){
    return this.buildBankAccountRec(b,false);
  }
  
  
  private BankAccountRec buildBankAccountRec(BankAccount b, boolean houseBank){
    BankAccountRec rec = new BankAccountRec();
    if(b.getAccountForBranch() != null){
      BankBranchRec branch = this.buildBankBranchRec(b.getAccountForBranch());
      rec.setAccountForBranch(branch);
    }
    rec.setAccountNumber(b.getAccountNumber());
    rec.setAccountName(b.getAccountName());
    rec.setValidated(b.isValidated());
    if(houseBank){
  
    }
    if(b.getCreatedBy() != null){
      UserRec usr = usrDM.getUserRecPvt(b.getCreatedBy());
      rec.setCreatedBy(usr);
      rec.setCreatedOn(b.getCreatedOn());
    }
    if(b.getUpdatedBy() != null){
      UserRec usr = usrDM.getUserRecPvt(b.getUpdatedBy());
      rec.setUpdatedBy(usr);
      rec.setUpdatedOn(b.getUpdatedOn());

    }
    rec.setId(b.getId());
    rec.setRevision(b.getRevision());
    return rec;
  }
/**
 * returns the bank branch details for a sort code
 * @param sortCode
 * @return
 * @throws BacException
 */
  public BankBranchRec getBranchBySortCode(String sortCode) throws BacException {
    LOGGER.log(INFO, "DB getBranchBySortCode called with sort code {0}", sortCode);
    try{
      Query q = em.createNamedQuery("bankBranchBySortCode");
      try{
        q.setParameter("sortCd", sortCode);
        List l = q.getResultList();
        LOGGER.log(INFO, "DB Branch list {0}", l);
        ListIterator it = l.listIterator();
        while(it.hasNext()){
          BankBranch br = (BankBranch)it.next();
          BankBranchRec rec = this.buildBankBranchRec(br);
          return rec;
        }
        // if we get here no branch found
        return null;
      }catch(IllegalStateException e){
        throw new BacException("Get Branch Bank invlaid query type","TRBR:01");
      }catch(QueryTimeoutException e){
        throw new BacException("Get Branch Bank query time out","TRBR:02");
      }catch(TransactionRequiredException e){
        throw new BacException("Get Branch Bank query time out","TRBR:03");
      }catch(PessimisticLockException e){
        throw new BacException("Get Branch Bank query time out","TRBR:04");
      }catch(LockTimeoutException e){
        throw new BacException("Get Branch Bank query locking timeout","TRBR:06");
      }catch(PersistenceException e){
        throw new BacException("Get Branch Bank query database error","TRBR:07");
      }

    }catch(IllegalArgumentException e){
      throw new BacException("Get Branch Bank invalid query type ","TRBR:08");
    }

    }

  public BankBranchRec getBrancheBySortCode(String sortCode){
   Query q = em.createNamedQuery("bankBranchBySortCode");
   q.setParameter("sortCd", sortCode);
   try{
   BankBranch bnkBranch = (BankBranch)q.getSingleResult();
   BankBranchRec rec = this.buildBankBranchRec(bnkBranch);
   return rec;
   }catch(NoResultException ex){
    LOGGER.log(INFO, "Branch not found for sort code {0}", sortCode);
    return null;
   }catch (NonUniqueResultException ex){
    LOGGER.log(INFO, "Multiple branches for sort code {0}", sortCode);
    return null;
   }

  } 
  
  public List<BankBranchRec> getBranchesAll(){
   Query q = em.createNamedQuery("bankBranchesAll");
   List rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<BankBranchRec> branches = new ArrayList<>();
   for(Object o: rs){
    BankBranchRec curr = this.buildBankBranchRec((BankBranch)o);
    branches.add(curr);
   }
   
   return branches;
  }
  
  public List<BankBranchRec> getBranchesBySortCode(String sortCode) throws BacException {
    LOGGER.log(INFO, "DB getBranchesBySortCode called with sort code {0}", sortCode);
    try{
      Query q = em.createNamedQuery("bankBranchesBySortCode");
      try{
        List<BankBranchRec> branchList = new ArrayList<>();
        if(sortCode == null || sortCode.isEmpty()){
          sortCode = "%";
        }else{
          sortCode = sortCode + "%";
        }
        LOGGER.log(INFO, "Sort code passed is {0}", sortCode);
        q.setParameter("sortCd", sortCode);
        List l = q.getResultList();
        LOGGER.log(INFO, "DB Branch list {0}", l);
        ListIterator it = l.listIterator();
        while(it.hasNext()){
          BankBranch br = (BankBranch)it.next();
          BankBranchRec rec = this.buildBankBranchRec(br);
         branchList.add(rec);
        }
        // if we get here no branch found
        return branchList;
      }catch(IllegalStateException e){
        throw new BacException("Get Branch Bank invlaid query type","TRBR:01");
      }catch(QueryTimeoutException e){
        throw new BacException("Get Branch Bank query time out","TRBR:02");
      }catch(TransactionRequiredException e){
        throw new BacException("Get Branch Bank query time out","TRBR:03");
      }catch(PessimisticLockException e){
        throw new BacException("Get Branch Bank query time out","TRBR:04");
      }catch(LockTimeoutException e){
        throw new BacException("Get Branch Bank query locking timeout","TRBR:06");
      }catch(PersistenceException e){
        throw new BacException("Get Branch Bank query database error","TRBR:07");
      }

    }catch(IllegalArgumentException e){
      throw new BacException("Get Branch Bank invalid query type ","TRBR:08");
    }

    }

  public List<BankAccountRec> getBankAccountsByBranch(BankBranchRec br){
   
   if(br == null){
    return null;
   }
   if(!trans.isActive()){
    trans.begin();
   }
   BankBranch branch = em.find(BankBranch.class, br.getId(), OPTIMISTIC);
   ArrayList<BankAccountRec> bnkAcntList = new ArrayList<>();
   List<BankAccount> accountList = branch.getAccounts();
   trans.rollback();
   if(accountList == null){
    LOGGER.log(INFO, "No accounts for branch");
    return null;
   }
   for(BankAccount curr:accountList){
    BankAccountRec rec = this.getBankAccountRec(curr);
    bnkAcntList.add(rec);
   }
   return bnkAcntList;
   
   
  }
  public List<BankAccountRec> getBankAccountsByBrAcntNumPt(BankBranchRec br, String acntNum){
   
   if(br == null){
    return null;
   }
   String srchNum;
   if(StringUtils.isBlank(acntNum)){
    srchNum = "%";
   }else{
    srchNum = StringUtils.deleteWhitespace(acntNum);
    srchNum = StringUtils.remove(srchNum, "%");
    srchNum = srchNum + "%";
   }
   Query q = em.createNamedQuery("accountsPartNumforBranch");
   q.setParameter("actNum", srchNum);
   q.setParameter("brId", br.getId());
   List rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<BankAccountRec> retList = new ArrayList<>();
   for(Object o:rs){
    BankAccountRec rec = this.getBankAccountRec((BankAccount)o);
    retList.add(rec);
   }
   return retList;
   
  }
  /**
   * Find the accounts for a branch
   * @param sortCode
   * @return
   * @throws BacException
   */
  public List<BankAccountRec> getBankAccountsForBranch(String sortCode) throws BacException {
    LOGGER.log(INFO, "getBankAccountsForBranch called with sort Code {0}", sortCode);
    BankBranchRec branchRec = this.getBranchBySortCode(sortCode);
    if(branchRec == null || branchRec.getId() == null || branchRec.getId() == 0){
      return null;
    }
    try{
      BankBranch branch = em.find(BankBranch.class, branchRec.getId(), OPTIMISTIC);
      ArrayList<BankAccountRec> accountList = new ArrayList<>();
      List<BankAccount> bankAcList = branch.getAccounts();
      ListIterator<BankAccount> li = bankAcList.listIterator();
      while(li.hasNext()){
        BankAccount account = li.next();
        BankAccountRec accountRec = this.buildBankAccountRec(account,false);
        accountList.add(accountRec);
      }
      LOGGER.log(INFO, "Accounts to be returned {0}", accountList);
      return accountList;
    }catch(IllegalArgumentException e){
      throw new BacException("Get Branch Bank invalid query type ","TRBA:01");
    }catch(QueryTimeoutException e){
        throw new BacException("Get Branch Bank query time out","TRBA:02");
      }catch(TransactionRequiredException e){
        throw new BacException("Get Branch Bank query time out","TRBA:03");
      }catch(PessimisticLockException e){
        throw new BacException("Get Branch Bank query time out","TRBA:04");
      }catch(LockTimeoutException e){
        throw new BacException("Get Branch Bank query locking timeout","TRBA:05");
      }catch(PersistenceException e){
        throw new BacException("Get Branch Bank query database error","TRBA:06");
      }
  }

  public List<BankRec> getAllBanks() {
    List<BankRec> bankRecList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("allBanks");
      try{
        List banksList = q.getResultList();
        ListIterator li = banksList.listIterator();
        while(li.hasNext()){
          Bank bnk = (Bank)li.next();
          BankRec bankRec = this.buildBankRec(bnk);
          bankRecList.add(bankRec);
        }
        LOGGER.log(INFO, "TreasuryDM returns bank List {0}", bankRecList);
        return bankRecList;
      }catch(QueryTimeoutException e){
        throw new BacException("Get Bank query time out","TRBNK:02");
      }catch(TransactionRequiredException e){
        throw new BacException("Get Bank query time out","TRBNK:03");
      }catch(PessimisticLockException e){
        throw new BacException("Get Bank query time out","TRBNK:04");
      }catch(LockTimeoutException e){
        throw new BacException("Get Branch Bank query locking timeout","TRBNK:05");
      }catch(PersistenceException e){
        throw new BacException("Get Branch Bank query database error","TRBNK:06");
      }
    }catch(IllegalArgumentException e){
      throw new BacException("Get Bank invalid query type ","TRBA:01");
    }
    
  }

  public boolean bankExists(String bankCd){
   LOGGER.log(INFO, "TreasuryDM bank exists {0}", bankCd);
   TypedQuery q = em.createNamedQuery("bankByBnkCode", Bank.class);
   q.setParameter("code", bankCd);
   
   List<Bank> rs = q.getResultList();
   
   return (rs != null && !rs.isEmpty());
  }
  public boolean chequeTemplateUsedByBnk(ChequeTemplateRec templ){
   
   boolean rc = false;
   if(!trans.isActive()){
    trans.begin();
   }
   
   Query q = em.createNamedQuery("bankAccntCompByChqTempl");
   q.setParameter("chqTemplId", templ.getId());
   List rs = q.getResultList();
   LOGGER.log(INFO, "bank acnts for template query returns {0} ", rs);
   
    return !(rs == null || rs.isEmpty());
   
  }
  public BacsTransCodeRec createBacsTransCode(BacsTransCodeRec transCd, UserRec usr, String view){
   LOGGER.log(INFO,"TreasuryDM.createBacsTransCode called with bacsTrans {0} user {1} view {2}", new Object[]{
    transCd,usr,view});
   Date crDate = new Date();
   transCd.setCreatedOn(crDate);
   transCd.setCreatedBy(usr);
   BacsTransCode bacsTrans = this.buildBacsTransCode(transCd,view);
   transCd.setId(bacsTrans.getId());
   transCd.setChanges(bacsTrans.getChanges());
   
   AuditBacsTransCode aud = new AuditBacsTransCode();
   
   aud.setAuditDate(crDate);
   aud.setCreatedBy(bacsTrans.getCreatedBy());
   aud.setNewValue(bacsTrans.getPtnrBnkTransCode());
   aud.setSource(view);
   aud.setTransCode(bacsTrans);
   aud.setUsrAction('I');
   em.persist(aud);
   
   return transCd;
  }
  
  
  public BankRec createBank(BankRec bank, String pg) {
    LOGGER.log(INFO,"Treasury DM createBank called with bank {0} bank Org {1}",
            new Object[] {bank.getBankCode(),bank.getBankOrganisation().getTradingName()});
    LOGGER.log(INFO, "created by {0}", bank.getCreatedBy());
    if(!trans.isActive()){
     trans.begin();
    }
    Bank bnk = this.buildBank(bank, pg);
    bank.setId(bnk.getId());
    //PartnerCorporate bnkOrg = masterDataDM.buildPartnerCorporatePvt(bank.getBankOrganisation(),pg);
    //bnk.setBankOrganisation(bnkOrg);
    //BankRec bankRec = this.buildBankRec(bnk);
    LOGGER.log(INFO, "BankDM to return bnk with id {0}", bank.getId());
    trans.commit();
    //trans.rollback();
    return bank;

    
  }


  public BankRec getBankByCode(String bankCd) {
    LOGGER.log(INFO, "BankDM getBankByCode called with bank code {0}", bankCd);

    try{
      Query q = em.createNamedQuery("banksByBnkCode");
      q.setParameter("code", bankCd);
      try{
        List banksList = q.getResultList();
        boolean found = false;
        ListIterator li = banksList.listIterator();
        while(li.hasNext() && !found){
          Bank bnk = (Bank)li.next();
          BankRec bankRec = this.buildBankRec(bnk);
          return bankRec;
        }
        LOGGER.log(INFO, "TreasuryDM  no bank found");
        return null;
      }catch(QueryTimeoutException e){
        throw new BacException("Get Bank query time out","TRBNK:02");
      }catch(TransactionRequiredException e){
        throw new BacException("Get Bank query time out","TRBNK:03");
      }catch(PessimisticLockException e){
        throw new BacException("Get Bank query time out","TRBNK:04");
      }catch(LockTimeoutException e){
        throw new BacException("Get Branch Bank query locking timeout","TRBNK:05");
      }catch(PersistenceException e){
        throw new BacException("Get Branch Bank query database error","TRBNK:06");
      }
    }catch(IllegalArgumentException e){
      throw new BacException("Get Bank invalid query type ","TRBA:01");
    }
    
  }

  /**
   * Creates bank branch with audit record
   * @param br
   * @param usr
   * @param view
   * @return Bank Branch record with id
   */
  public BankBranchRec createBankBranch(BankBranchRec br, UserRec usr, String view) {
   LOGGER.log(INFO, "TreasuryDM createBankBranch called with branch {0} user {1} view {2}",
            new Object[]{br,usr, view});
   if(!trans.isActive()){
    trans.begin();
   }
   Bank bank ;
   if(br.getBank().getId() == null){
    bank = this.buildBank(br.getBank(), view);
   } else {
    bank = em.find(Bank.class, br.getBank().getId(), OPTIMISTIC);
   }
   BankBranch branch = buildBankBranch(br, view );
   br.setId(branch.getId());
   
   trans.commit();
   LOGGER.log(INFO, "new branch id {0}",br.getId());
   return br;
  }
  
  /*public BankBranchRec createBankBranch(BankBranchRec br, UserRec crUsr, String pg) {
    LOGGER.log(INFO, "TreasuryDM createBankBranch called with branch {0} user {1}",
            new Object[]{br,usr});
    Bank bank = this.buildBank(br.getBank());
    BankBranch branch = this.buildBankBranch(br,bank,usr,pg );
    em.persist(branch);
    LOGGER.log(INFO, "After persist branch");
    br.setId(branch.getId());
    LOGGER.log(INFO, "new branch id {0}",br.getId());
    return br;
  }*/

  public List<BankAccountRec> getAccountsByActNumPart(String accountNumber) {
    LOGGER.log(INFO, "TreasuryDM.getAccountsByActNumPart called with number {0}", accountNumber);
    List<BankAccountRec> actRecList = new ArrayList<>();
    Query q;
    List dbResult;
    if(accountNumber == null || accountNumber.isEmpty()){
      q = em.createNamedQuery("allBankAccounts");
      dbResult = q.getResultList();
    }else{
      q = em.createNamedQuery("accountsByActNumStartingWith");
      accountNumber = accountNumber + "%";
      LOGGER.log(INFO, "accountsByActNumStartingWith called with {0}", accountNumber);
      q.setParameter("actNum", accountNumber);
      dbResult = q.getResultList();
    }
    ListIterator dbLi =  dbResult.listIterator();
    while(dbLi.hasNext()){
      BankAccount act = (BankAccount)dbLi.next();
      BankAccountRec rec = this.buildBankAccountRec(act,false);
      actRecList.add(rec);
    }
    LOGGER.log(INFO, "BankDM return accounts {0}", actRecList);
    return actRecList;
  }

  public List<BankAccountRec> getAccountsByActNumPartForBr(String accountNumber, BankBranchRec br) {
    LOGGER.log(INFO, "TreasuryDM.getAccountsByActNumPart called with number {0}", accountNumber);
    List<BankAccountRec> retList = new ArrayList<>();
    Query q;
    List dbResult;
    
      q = em.createNamedQuery("accountsPartNumforBranch");
      q.setParameter("actNum", accountNumber);
      q.setParameter("brId", br.getId());
      
      dbResult = q.getResultList();
      if(dbResult == null || dbResult.isEmpty()){
       LOGGER.log(INFO, "No accounts with num {0} found for branch {1}", new Object[]{
        accountNumber, br.getSortCode() });
       return null;
      }
     for(Object obj: dbResult){
      BankAccount act = (BankAccount)obj;
      BankAccountRec rec = buildBankAccountRec(act,false);
      retList.add(rec);
     }
     
    LOGGER.log(INFO, "BankDM return accounts {0}", retList);
    return retList;
  }
  
  public BankAccountRec createBankAccount(BankAccountRec act, String page) throws BacException {
    LOGGER.log(INFO, "Bank DM createBankAccount called with {0}", act);
    if(!trans.isActive()){
     trans.begin();
    }
    BankBranch branch = em.find(BankBranch.class, act.getAccountForBranch().getId());
    if(branch == null){
      throw new BacException("Could not find Bank Branch in Database");
    }
    BankAccount account = this.buildBankAccount(act, branch,page);
    List<BankAccount> branchAccounts = branch.getAccounts();
    branchAccounts.add(account);
    account.setAccountForBranch(branch);
    em.persist(account);
    LOGGER.log(INFO, "bank Account created  with id: {0}", account.getId());
    trans.commit();
    if(account.getId() != null){
      BankAccountRec returnRec = this.buildBankAccountRec(account,false);
      LOGGER.log(INFO, "bank Account rec returned id: {0}",returnRec);
      return returnRec;
    }
    return null;
  }

 



    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
/**
 * Save to database a bank account that has been validated. 
 * If the bank or branch do not exist they will be created
 * @param account
 * @param pg Page method is called from
 * @return
 * @throws BacException 
 */
 public BankAccountRec bankAccountValidatedUpdate(BankAccountRec account,  
   String pg) throws BacException {
  LOGGER.log(INFO, "Bank DB bankAccountValidatedUpdate called with {0}", account);
  if(!trans.isActive()){
   trans.begin();
  }
  BankBranchRec bankBranchRec = account.getAccountForBranch();
  
  BankRec bankRec = bankBranchRec.getBank();
  UserRec usrRec = account.getCreatedBy();
  String bankCode = bankRec.getBankCode();
  LOGGER.log(INFO, "Bank Code {0}",bankCode );
  Query bnkQ = em.createNamedQuery("bankByBnkCode");
  bnkQ.setParameter("code", bankCode);
  Bank bank;
  try{
   bank = (Bank)bnkQ.getSingleResult();
   bankBranchRec.getBank().setId(bank.getId());
   
  }catch(NoResultException ex) {
   bank = this.buildBank(bankRec,pg);
   bankBranchRec.getBank().setId(bank.getId());
   /*PartnerCorporateRec bankOrgRec = bankRec.getBankOrganisation();
   bankOrgRec.setId(masterDataDM.getCorpPartnerIdByTradingName(bankOrgRec.getTradingName()));
   PartnerCorporate bankOrg  = masterDataDM.createCorporatePartnerDM(bankOrgRec, usrRec,pg);
   bank.setBankOrganisation(bankOrg);*/
   
  }catch(NonUniqueResultException ex){
   throw new BacException("DUPL Bank",ex.getLocalizedMessage());
  }
  
  LOGGER.log(INFO, "Build bank returned org id {0}", bank.getBankOrganisation().getId());
  Query brQ = em.createNamedQuery("bankBranchBySortCode");
  brQ.setParameter("sortCd", bankBranchRec.getSortCode());
  BankBranch br;
  try{
  br = (BankBranch)brQ.getSingleResult();
  LOGGER.log(INFO, "Found branch {0}", br);
  }catch(NoResultException ex){
   LOGGER.log(INFO, "Branch not found");
   // add branch details to DB object
   br = this.buildBankBranch(bankBranchRec, pg);
   
   AddressRec brAddress = bankBranchRec.getBranchAddress();
   if(brAddress.getCreatedBy() == null){
    brAddress.setCreatedBy(bankBranchRec.getCreatedBy());
    brAddress.setCreatedOn(bankBranchRec.getCreatedOn());
   }
   LOGGER.log(INFO, "Branch Address {0}", brAddress);
   if(brAddress.getId() != null){
    LOGGER.log(INFO, "Create Branch Address ");
    Address addr = this.masterDataDM.createAddressPvt(brAddress,pg);
    br.setBranchAddress(addr);
   }
   
   br.setBank(bank);
   
   List<BankBranch> branches = bank.getBankBranches();
   if(branches == null){
    branches = new ArrayList<>();
   }
   branches.add(br);
   bank.setBankBranches(branches);
   
  }
  // bank account
  Query bankActQ = em.createNamedQuery("accountByActNum");
  bankActQ.setParameter("actNum", account.getAccountNumber());
  BankAccount act;
  try{
   act = (BankAccount)bankActQ.getSingleResult();
  }catch(NoResultException ex){
   act = buildBankAccount(account, br, pg);
   
  }
  account.setId(act.getId());
  trans.commit();
  return account;
 }

 public List<BankRec> getBanksByCode(String bankCode) throws BacException {
  LOGGER.log(INFO, "TreasuryDM getBanksByCode called with code {0}", bankCode);
  List<BankRec> bankList = new ArrayList<>();
  if(bankCode == null || bankCode.isEmpty()){
   bankCode = "%";
  }else{
   bankCode = bankCode + "%";
  }
  Query q = em.createNamedQuery("banksByBnkCode");
  q.setParameter("code", bankCode);
  List rs = q.getResultList();
  LOGGER.log(INFO, "Number of banks returned by query {0}", rs.size());
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   Bank bnk = (Bank)li.next();
   BankRec bnkRec = this.buildBankRec(bnk);
   bankList.add(bnkRec);
  }
  LOGGER.log(INFO, "Number of banks returned by getBanksByCode {0}", bankList.size());
  return bankList;
 }
/**
 * Obtains the branches that exist for the bank.
 * If there are no branches then returns null
 * @param bank
 * @return
 * @throws BacException 
 */
 public List<BankBranchRec> getBranchesForBank(BankRec bank) throws BacException {
  LOGGER.log(INFO, "bankDM.getBranchesForBank called with bank id {0}", bank.getId());
  Bank bnk = em.find(Bank.class, bank.getId(), OPTIMISTIC);
  List<BankBranch> branches = bnk.getBankBranches();
  LOGGER.log(INFO, "Branches {0}", branches);
  if(branches != null && !branches.isEmpty()){
   LOGGER.log(INFO, "process Branches");
   List<BankBranchRec> retList = new ArrayList<>();
   for (BankBranch br : branches){
    LOGGER.log(INFO, "Loop at branches");
    BankBranchRec branchRec = this.buildBankBranchRec(br);
    retList.add(branchRec);
   }
   LOGGER.log(INFO, "Branch retlist {0}",retList);
   return retList;
  }
  return null;
 }

 public ChequeTemplateRec getChqTemplOrigFileData(ChequeTemplateRec tmplRec){
  ChequeTemplate tmpl = em.find(ChequeTemplate.class, tmplRec.getId());
  tmplRec.setOriginalData(tmpl.getOriginalData());
  tmplRec.setOriginalFileExt(tmpl.getOriginalFileExt());
  LOGGER.log(INFO, "File data len {0}", tmplRec.getOriginalData().length);
  return tmplRec;
 }
 
 public ChequeTemplateRec getChqTemplPdfFileData(ChequeTemplateRec tmplRec){
  ChequeTemplate tmpl = em.find(ChequeTemplate.class, tmplRec.getId());
  tmplRec.setPdfData(tmpl.getPdfData());
  tmplRec.setPdfFileExt(tmpl.getPdfFileExt());
  LOGGER.log(INFO, "File data len {0}", tmplRec.getPdfData().length);
  return tmplRec;
 }
 
 public List<DocBankLineChqRec> getChequeList(ChequeListSelOpt selOpt){
 LOGGER.log(INFO, "TreasuryDM.getChequeList called");
 List<DocBankLineChqRec> chequeRecList = new ArrayList<>();
 Query q = null;
 LOGGER.log(INFO, "Chq num fr {0} issue fr {1}", new Object[]{selOpt.getChqNumFr(),selOpt.getChqIssueFrom()} );
 if(StringUtils.isBlank(selOpt.getChqNumFr())  && selOpt.getChqIssueFrom() == null){
  switch(selOpt.getCashmntStat()){
   case ChequeListSelOpt.ALL:
    LOGGER.log(INFO, "All cheques for bank account");
    q = em.createNamedQuery("chqListBnkAll");
    q.setParameter("compId", selOpt.getCompSel().getId());
    q.setParameter("unclBnkAcntId", selOpt.getBnkAcntComp().getId());
    q.setParameter("clearedBnkAcntId", selOpt.getBnkAcntComp().getId());
    break;
  }
  
 }
 if(q == null){
  LOGGER.log(INFO, "Invalid selection options");
  return null;
 }
 List rs = q.getResultList();
 if(rs == null){
  LOGGER.log(INFO, "No cheques returned from query", rs);
  return null;
 }
 
 for(Object o:rs){
  DocBankLineChqRec cur = (DocBankLineChqRec)buildDocBankLineRec((DocBankLineBase)o);
  LOGGER.log(INFO, "cur chq {0}", cur);
  chequeRecList.add(cur);
 }
 
 
 return chequeRecList;
 
}
 public List<ChequeTemplateRec> getChequeTemplByRef(String ref){
  
  if(StringUtils.isBlank(ref)){
   return this.getChequeTemplatesAll();
  }else{
   Query q = em.createNamedQuery("chqTemplByRef");
   q.setParameter("ref", ref);
   List rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<ChequeTemplateRec> retList = new ArrayList<>();
   for(Object o:rs){
    ChequeTemplateRec rec = this.buildChequeTemplateRec((ChequeTemplate)o);
    retList.add(rec);
   }
   return retList;
  }
 }
 
 public List<ChequeTemplateRec> getChequeTemplatesAll(){
  LOGGER.log(INFO, "TreasuryDM getChequeTemplatesAll called");
  Query q = em.createNamedQuery("chqTemplAll");
  List rs = q.getResultList();
  LOGGER.log(INFO, "chqTemplAll found {0}", rs);
  if(rs ==  null || rs.isEmpty()){
   return null;
  }
  
   LOGGER.log(INFO, "Process templates from query");
   List<ChequeTemplateRec> retList = new ArrayList<>();
   for(Object o:rs){
    ChequeTemplateRec rec = this.buildChequeTemplateRec((ChequeTemplate)o);
    retList.add(rec);
    LOGGER.log(INFO, "retList is now {0}",retList);
   }
   return retList;
  
  
 }
 
 public BankAccountRec updateBankAccount(BankAccountRec bnkAcRec, String pg){
  
  if(!trans.isActive()){
   trans.begin();
  }
  BankBranch br = em.find(BankBranch.class, bnkAcRec.getAccountForBranch().getId());
  BankAccount bnkAc = buildBankAccount(bnkAcRec, br, pg);
  if(bnkAcRec.getId() == null){
   bnkAcRec.setId(bnkAc.getId());
  }
  if(bnkAcRec.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  return bnkAcRec;
 }
 public BankAccountCompanyRec updateBankAccountCompany(BankAccountCompanyRec bankAc,  
         String page) throws BacException {
  LOGGER.log(INFO, "TreasuryDM createBankAccountCompany called with bank ac num {0}  page {1}",
          new Object[]{bankAc,page});
  if(!trans.isActive()){
   trans.begin();
  }
  BankAccountCompany bnkAc = buildBankAccountCompany(bankAc, page);
  bankAc.setId(bnkAc.getId());
  
  trans.commit();
  return bankAc;
 }
 
 /**
  * Creates the Banking entry recording the payment
  * @param bnkLnRec Banking line
  * @param payTyRec Payment type
  * @param fiLnRec FI line
  * @param Pg
  * @return 
  */
public DocBankLineBaseRec updateDocBankLine(DocBankLineBaseRec bnkLnRec, DocLineBaseRec fiLnRec,
  PaymentTypeRec payTyRec, String Pg){
 LOGGER.log(INFO, "updateDocBankLine called with bnkLn {0} and payTy id {1}", 
   new Object[]{bnkLnRec, payTyRec.getId()});
 if(!trans.isActive()){
  trans.begin();
 }
 //String lineClass = bnkLnRec.getClass().getSimpleName();
 DocLineBase fiLn = em.find(DocLineBase.class, fiLnRec.getId());
 DocBankLineBase docLn = buildDocBankLine(bnkLnRec, fiLnRec, Pg);
 docLn.setDocFiLine(fiLn);
 fiLn.setBankLine(docLn);
 trans.commit();
 if(bnkLnRec.getId() == null){
  bnkLnRec.setId(docLn.getId());
 }
 
 
 
 
 return bnkLnRec;
}
 /**
  * used to check in the bank account is already used
  * @param bankBranch
  * @param bankAccount
  * @return
  * @throws BacException 
  */
 public boolean bankAccountForBranchExists(BankBranchRec bankBranch, 
         BankAccountRec bankAccount) 
         throws BacException {
  
  Query q = em.createNamedQuery("accountForBranch");
  q.setParameter("actNum", bankAccount.getAccountNumber());
  q.setParameter("brId", bankBranch.getId());
  try{
  Object result = q.getSingleResult();
  LOGGER.log(INFO, "Found account {0}", result);
  return true;
  }catch(NoResultException ex){
   return false;
  }catch(NonUniqueResultException ex){
   return true;
  }
 }

 
 
 public List<BankAccountCompanyRec> getBankAccntsCompByName(CompanyBasicRec comp, String bnkName){
  Query q = em.createNamedQuery("bankAccntCompByName");
  q.setParameter("compId", comp.getId());
  q.setParameter("acntName", bnkName);
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  List<BankAccountCompanyRec> bnkAcList = new ArrayList<>();
  while(li.hasNext()){
   BankAccountCompany acnt = (BankAccountCompany)li.next();
   BankAccountCompanyRec account = this.buildBankAccountCompanyRec(acnt);
   bnkAcList.add(account);
  } 
  
  return bnkAcList;
 }
 
 public  List<BankAccountCompanyRec> getBankAccountsCompAll(){
  
  Query q = em.createNamedQuery("bankAccntCompAll");
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  
  List<BankAccountCompanyRec> retList = new ArrayList<>();
  for(Object o:rs){
   BankAccountCompanyRec rec = this.buildBankAccountCompanyRec((BankAccountCompany)o);
   retList.add(rec);
  }
  return retList;
 
 
  
 }
 public List<BankAccountCompanyRec> getBankAccountsForCompany(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "TreasuryDM.getBankAccountsForCompany called with comp id {0}", comp.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  Query q = em.createNamedQuery("bankAcsForComp");
  q.setParameter("compId", comp.getId());
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  List<BankAccountCompanyRec> bnkAcList = new ArrayList<>();
  while(li.hasNext()){
   BankAccountCompany acnt = (BankAccountCompany)li.next();
   BankAccountCompanyRec account = this.buildBankAccountCompanyRec(acnt);
   bnkAcList.add(account);
  }
  return bnkAcList;
 }

 
 public BankAccountCompanyRec getBankAccountCompany(BankAccountCompany bankAc) {
  BankAccountCompanyRec bnkAcComp = this.buildBankAccountCompanyRec(bankAc);
  return bnkAcComp;
 }

 public BankAccountCompanyRec getBankAccountForCompany(String accntNum, CompanyBasicRec comp) {
  Query q = em.createNamedQuery("bankAccountForComp");
  q.setParameter("compId", comp.getId());
  q.setParameter("acntNum", accntNum);
  try{
   BankAccountCompany bnkAcnt = (BankAccountCompany)q.getSingleResult();
   BankAccountCompanyRec acntRec = this.buildBankAccountCompanyRec(bnkAcnt);
   return acntRec;
  }catch(NoResultException x) {
   return null;
  }      
  
 } 
 public List<BacsTransCodeRec> getBacsTransCodesByDdType(String dd_type) throws BacException {
  LOGGER.log(INFO, "getBacsTransCodesByDdType called with dd_type {0}", dd_type);
  List<BacsTransCodeRec> retList = null;
  if(dd_type.equalsIgnoreCase("DD")){
   retList = new ArrayList<>();
   Query q = em.createNamedQuery("BacsTransDD");
   List l = q.getResultList();
   ListIterator li = l.listIterator();
   while(li.hasNext()){
    BacsTransCodeRec rec = this.buildBacsTransCodeRec((BacsTransCode)li.next());
    retList.add(rec);
   }
  }else if(dd_type.equalsIgnoreCase("DC")){
   retList = new ArrayList<>();
   Query q = em.createNamedQuery("BacsTransDC");
   List l = q.getResultList();
   ListIterator li = l.listIterator();
   while(li.hasNext()){
    BacsTransCodeRec rec = this.buildBacsTransCodeRec((BacsTransCode)li.next());
    retList.add(rec);
   }
  }
  return retList;
 }

 public List<BacsTransCodeRec> getBacsTransCodesAll() throws BacException {
  List<BacsTransCodeRec> retList = new ArrayList<>();
  Query q = em.createNamedQuery("BacsTransAll");
  List l = q.getResultList();
   ListIterator li = l.listIterator();
   while(li.hasNext()){
    BacsTransCodeRec rec = buildBacsTransCodeRec((BacsTransCode)li.next());
    retList.add(rec);
   }
  return retList;
 }

 public boolean paymentRunRefAvailable(CompanyBasicRec comp, String payRef) {
  
  Query q = em.createNamedQuery("bnkPayRunForComp");
  q.setParameter("compId", comp.getId());
  q.setParameter("ref", payRef);
  List rs = q.getResultList();
  LOGGER.log(INFO, "Check for pay reun returned {0}", rs);
    return !(rs != null && !rs.isEmpty());
 }
 
 public List<DocBankLineChqRec> postDocBankLinesCheque(FiDoclSelectionOpt selOpts, String chqNumStart,
   NumberRangeChequeRec chqBkRec, String pg){
  
  List<DocBankLineChqRec> retList = new ArrayList<>();
  List<DocFi> docs = fiDocDM.getFiDocsBySelOptDM(selOpts);
  LOGGER.log(INFO, "recDocs from docDM {0}", docs);
  TypedQuery q =  em.createNamedQuery("docLineApPdChNoChqDoc", DocLineAp.class);
  List<Long> payTypeIds = new ArrayList<>();
  List<PaymentTypeRec> payTypes = this.sysBuff.getPaymentTypesForLed(selOpts.getComp(), selOpts.getLedgerCode());
  LOGGER.log(INFO, "Payment Type found {0}", payTypes);
  if(payTypes != null && !payTypes.isEmpty()){
   for(PaymentTypeRec pt:payTypes ){
    payTypeIds.add(pt.getId());
   }
  }
  q.setParameter("payTypes", payTypeIds);
  q.setParameter("docs", docs);
  q.setParameter("compId", selOpts.getComp().getId());
  List<DocLineAp> apLines = q.getResultList();
  LOGGER.log(INFO, "AP lines due for cheque {0}", apLines);
  if(apLines == null || apLines.isEmpty()){
   return null;
  }
  
  NumberRangeCheque chqBk = em.find(NumberRangeCheque.class, chqBkRec.getNumberControlId());
  
  int chqNum;
  if(chqBkRec.isAutoNum()){
   chqNum = configDM.getNextNum4NumRangeId(chqBkRec.getNumberControlId());
   
  }
   
  chqNum = Integer.parseInt("chqNumStart");
  
  // loop over AP lines creating Cheque lines set printed = false
  for(DocLineAp linAp:apLines){
   DocBankLineChqRec chqLineRec = new DocBankLineChqRec();
   DocBankLineChq chqLine  = (DocBankLineChq)buildDocBankLine(chqLineRec, pg);
   
  }
  
  return retList;
 }

 public BnkPaymentRunRec createPayRun(BnkPaymentRunRec payRun, Date currDt,UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "TreasuryDM.createPayRun called with {0} usr id {1} page {2}", 
          new Object[]{payRun,usr.getId(),page});
  
  
  Query q = em.createNamedQuery("bnkPayRunForComp");
  q.setParameter("compId", payRun.getComp().getId());
  q.setParameter("ref", payRun.getRef());
  List rs = q.getResultList();
  LOGGER.log(INFO, "Check for pay reun returned {0}", rs);
  if(rs != null && !rs.isEmpty()){
   throw new BacException("Pay run not unique");
  }
  
  User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  BnkPaymentRun pRun = new BnkPaymentRun();
  pRun.setCreateBy(crUsr);
  pRun.setCreateDate(currDt);
  pRun.setReference(payRun.getRef());
  pRun.setRunDate(payRun.getRunDate());
  CompanyBasic comp = em.find(CompanyBasic.class, payRun.getComp().getId(), OPTIMISTIC);
  pRun.setComp(comp);
  em.persist(pRun);
  
  AuditBnkPayRun aud = new AuditBnkPayRun();
  aud.setAuditDate(currDt);
  aud.setBnkPayRun(pRun);
  aud.setCreatedBy(crUsr);
  aud.setNewValue(pRun.getReference());
  aud.setSource(page);
  aud.setUsrAction('I');
  em.persist(aud);
  payRun.setId(pRun.getId());
  return payRun;
 }

 /**
  * Create the bank account entry that is represented by the bank transaction. This is primarily used for
  * reconciling a bank statement.
  * The bank line is linked to an account document line  
  * @param payRun The payment run that this bank line was created on
  * @param bnkLine Bank line Business record structure
  * @param fiDocLine Accounting line that this bank transaction relates to
  * @param usr User that made the entry
  * @param date  Date on which the entry was made
  * @param page Page the user was on when this transaction was called
  * @return
  * @throws BacException 
  */
 public DocBankLineBase createPayRunBankLinePvt(BnkPaymentRun payRun,DocBankLineBaseRec bnkLine,
         DocLineBase fiDocLine,UserRec usr, Date date, String page) throws BacException {
  LOGGER.log(INFO, "TreasuryDM.createBankLine called with payRun {0} bnkLine [1} fi doc ln {2} ", 
          new Object[]{payRun,bnkLine,fiDocLine});
  
  
  DocBankLineBase bnkLnDB = buildDocBankLine(payRun, bnkLine, fiDocLine, usr, date, page);
  List<DocBankLineBase> payRunLines = payRun.getBankLines();
  
  payRunLines.add(bnkLnDB);
  LOGGER.log(INFO, "buildDocBankLine returned {0}", bnkLnDB);
  
  return bnkLnDB;
 }
 
 public boolean deleteBacsTransCode(BacsTransCodeRec codeRec, UserRec delUser,String page){
  LOGGER.log(INFO, "treasuryDM.deleteBacsTransCode called for code {0}", codeRec.getPtnrBnkTransCode());
  boolean rc;
  if(!trans.isActive()){
   trans.begin();
  }
  BacsTransCode code = em.find(BacsTransCode.class, codeRec.getId());
  User delUsr = em.find(User.class, delUser.getId());
  AuditBacsTransCode aud = this.buildAuditBacsTransCode(null, delUsr, page, 'D');
  aud.setNewValue(codeRec.getPtnrBnkTransCode());
  em.remove(code);
  rc = true;
  trans.commit();
  return rc;
 }
 public boolean deleteChequeTemplate(ChequeTemplateRec templRec, String view){
  LOGGER.log(INFO, "TreasuryDM.deleteChequeTemplate called with templ id {0}",templRec.getId());
  if(!trans.isActive()){
   trans.begin();
  } 
 
  User usr = em.find(User.class, templRec.getChangedBy().getId());
  ChequeTemplate templ = em.find(ChequeTemplate.class, templRec.getId());
  LOGGER.log(INFO, "templ {0}", templ);
  if(templ == null){
   return false;
  }
  List<AuditChequeTemplate> auditRecs = templ.getAuditRecords();
  LOGGER.log(INFO, "audit recs {0}", auditRecs);
  if(auditRecs != null){
   for(AuditChequeTemplate audDel:auditRecs){
    audDel.setChequeTemplate(null);
   }
  }
  AuditChequeTemplate aud = this.buildAuditChequeTemplate(null, usr, view, 'D');
  aud.setNewValue(templ.getReference());
  em.remove(templ);
  trans.commit();
  return true;
 }
 
 
 public BacsTransCodeRec updateBacsTransCode(BacsTransCodeRec bacsCodeRec, String pg){
  LOGGER.log(INFO, "trasuryDM.updateBacsTransCode called with transCode {0}", bacsCodeRec.getPtnrBnkTransCode());
  if(!trans.isActive()){
   trans.begin();
  }
  BacsTransCode bacsCode = buildBacsTransCode(bacsCodeRec,pg);
  LOGGER.log(INFO, "bacsCode id after build {0}", bacsCode.getId());
  LOGGER.log(INFO, "bacsCodeRec id {0}", bacsCodeRec.getId());
  if(bacsCodeRec.getId() == null){
   bacsCodeRec.setId(bacsCode.getId());
  }
  if(bacsCodeRec.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  LOGGER.log(INFO, "bacsCodeRec id before return {0}", bacsCodeRec.getId());
  return bacsCodeRec;
 }
 
 public BankRec updateBank(BankRec bankRec, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  
  Bank bank = buildBank(bankRec, pg);
  if(bankRec.getId() == null){
   bankRec.setId(bank.getId());
  }
  trans.commit();
  return bankRec;
 }
 public ArBankAccountRec updateBankAccountArAp(ArBankAccountRec acntRec, String pg){
  ArBankAccount acnt = this.buildBankAccountArAp(acntRec, pg);
  if(acntRec.getId() == null){
   acntRec.setId(acnt.getId());
  }
  return acntRec;
 }
 public BankBranchRec updateBankBranch(BankBranchRec br, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  BankBranch brDb = buildBankBranch(br, pg);
  if (br.getId() == null){
   br.setId(brDb.getId());
  }
  trans.commit();
  return br;
 }
 
public ChequeTemplateRec updateChequeTemplate(ChequeTemplateRec chqTmplRec, String view){
 LOGGER.log(INFO, "Called updateChequeTemplate with templ {0} ", chqTmplRec.getReference());
 if(!trans.isActive()){
  trans.begin();
 }
 ChequeTemplate tmpl = buildChequeTemplate(chqTmplRec, view);
 if(chqTmplRec.getId() == null){
  chqTmplRec.setId(tmpl.getId());
 }
 trans.commit();
 return chqTmplRec;
} 
 
}
