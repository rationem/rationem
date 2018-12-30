/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.company.AccountTypeRec;
import javax.persistence.PessimisticLockException;
import javax.persistence.LockTimeoutException;
import javax.persistence.QueryTimeoutException;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import javax.persistence.PersistenceException;
import com.rationem.entity.audit.AuditGlCompAccount;
import javax.persistence.EntityNotFoundException;
import com.rationem.entity.audit.AuditGlAccount;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.ListIterator;
import javax.persistence.Query;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.config.common.NumberRange;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.ArrayList;
import java.util.List;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import com.rationem.entity.fi.glAccount.FiGLAccountBase;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.entity.fi.glAccount.FiBsAccount;
import javax.persistence.TransactionRequiredException;
import javax.persistence.EntityExistsException;
import com.rationem.entity.user.User;
import com.rationem.entity.config.company.ChartOfAccounts;
//import com.rationem.entity.config.fi.FiGlActType;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.entity.fi.glAccount.FiPlAccount;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.entity.audit.AuditPeriodBalance;
import com.rationem.entity.config.company.AccountType;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.salesTax.vat.VatCode;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import com.rationem.entity.tr.bank.BankAccountCompany;
import com.rationem.exception.BacException;
import java.util.HashMap;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ejb.LocalBean;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;

import static java.util.logging.Level.INFO;
import javax.ejb.*;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import static javax.persistence.LockModeType.OPTIMISTIC;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;


/**
 * Performs the actual Database updates for GL, AR and AP master data
 * Error codes GL:01 Object type incorrect
GL:02 Account type incorrect
GL:03 user type incorrect
GL:05 PL Account already exists
GL:06 PL Account not a DB object
GL:06 PL Account DB transaction error
GL:07 PL Account DB transaction error
GL:08 BS Account already exists
GL:09 BS Account not a DB object
GL:10 BS Account DB transaction error
GL:11 BS Account DB transaction error
 * @author Chris
 */
@Stateless
@LocalBean
@ConcurrencyManagement(CONTAINER)
public class FiMasterRecordDM {
      private static final Logger LOGGER =
            Logger.getLogger(FiMasterRecordDM.class.getName());

private EntityManager em;
private EntityTransaction trans;

@EJB
private SysBuffer sysBuffer;

@EJB
private ConfigurationDM configDM;

@EJB
private UserDM userDM;

@EJB
private CompanyDM compDM;

@EJB
private ProgrammeDM progDM;
@EJB
private VatDM vatDM;


@PostConstruct
 void init(){
  FacesContext fc = FacesContext.getCurrentInstance();
  String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
  HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
     
    }

private AuditGlCompAccount buildAuditGlCompAccount(FiGlAccountComp ac, User usr, char usrAction, String pg){
 AuditGlCompAccount aud = new AuditGlCompAccount();
 aud.setAuditDate(new Date());
 aud.setCompAccount(ac);
 aud.setCreatedBy(usr);
 aud.setSource(pg);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 return aud;
}

private AuditPeriodBalance buildAuditPeriodBalance(FiPeriodBalance bal, User usr, char usrAction, String pg){
 AuditPeriodBalance aud = new AuditPeriodBalance();
 aud.setAuditDate(new Date());
 aud.setCreatedBy(usr);
 aud.setPerBal(bal);
 aud.setSource(pg);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 return aud;
}

/**
 * Build DB object.
 * @param plAccount Business plAccount to be updated
 * @param newAccount if true create new DB object otherwise update and existing DB object
 * @return
 */
private FiPlAccount buildPlAccount(FiGlAccountBaseRec glAccount, String pg){
  LOGGER.log(INFO, "buildPlAccount with account {0} newAccount {1}",
          new Object[] {glAccount});
  boolean newPlAct = false;
  boolean changedPlAct = false;
  FiPlAccountRec plAccount = (FiPlAccountRec)glAccount;
  FiPlAccount ac ;
  if(glAccount.getId() == null ){
   ac = new FiPlAccount();
   User crUsr = em.find(User.class, glAccount.getCreatedBy().getId(), OPTIMISTIC);
   ac.setCreatedBy(crUsr);
   ac.setCreated(glAccount.getCreatedOn());
   newPlAct = true;
   em.persist(ac);
   AuditGlAccount aud = buildAuditGlAccount(ac, crUsr, 'I', pg);
   aud.setNewValue(plAccount.getRef());
  }else{
   ac = em.find(FiPlAccount.class, plAccount.getId(),OPTIMISTIC_FORCE_INCREMENT);
  }
  
  ac = (FiPlAccount)buildBaseAccountForGlAccount(ac, glAccount, pg);
  if(newPlAct){
   ac.setAccountCat(plAccount.getAccountCat());
   
  }else{
   User chUsr = em.find(User.class, plAccount.getUpdateBy().getId(), OPTIMISTIC);
   
   if((plAccount.getAccountCat() == null && ac.getAccountCat() != null) || 
      (plAccount.getAccountCat() != null && ac.getAccountCat() == null) ||
      (plAccount.getAccountCat() != null && !plAccount.getAccountCat().equalsIgnoreCase(ac.getAccountCat()))){
    AuditGlAccount aud = buildAuditGlAccount(ac, chUsr, 'U', pg);
    aud.setFieldName("GL_PL_ACNT_CAT");
    aud.setNewValue(plAccount.getAccountCat());
    aud.setOldValue(ac.getAccountCat());
    changedPlAct = true;
   }
   
   if(changedPlAct){
    ac.setUpdateBy(chUsr);
    ac.setUpdateDate(plAccount.getUpdateOn());
   }
  }
  
  
  return ac;

}


private AuditGlCompAccount buildGlCompAudit(FiGlAccountComp glAccount, UserRec usr){
  AuditGlCompAccount audit = new AuditGlCompAccount();
  Date auditDate = new Date();
  User dbUsr = em.find(User.class, usr.getId());
  audit.setAuditDate(auditDate);
  audit.setCompAccount(glAccount);

  audit.setCreatedBy(dbUsr);

  return audit;
}

private AuditGlAccount auditChange(AuditGlAccount aud, char action, String field, String oldVal,
        String newVal ){

  aud.setUsrAction(action);
  aud.setFieldName(field);
  aud.setOldValue(oldVal);
  aud.setNewValue(newVal);

  return aud;
}
private AuditGlCompAccount auditChange(AuditGlCompAccount aud, char action, String field, String oldVal,
        String newVal ){

  aud.setUsrAction(action);
  aud.setFieldName(field);
  aud.setOldValue(oldVal);
  aud.setNewValue(newVal);

  return aud;
}
public FiBsAccountRec getFiBsAccountRec(FiBsAccount ac){
  return buildFiBsAccountRec(ac);
}
private FiBsAccountRec buildFiBsAccountRec(FiBsAccount ac){
  FiBsAccountRec act = new FiBsAccountRec();
  if(ac.getAccountType() != null){
    AccountTypeRec actType = this.configDM.buildAccountTypeRecPvt(ac.getAccountType());
    act.setAccountType(actType);
  }
  act.setBalFwd(ac.isBalFwd());
  if(ac.getChartOfAccounts() != null){
    ChartOfAccountsRec coa = this.compDM.getChartOfAccountsRecPvt(ac.getChartOfAccounts());
    act.setChartOfAccounts(coa);
  }
  if(ac.getCreatedBy() != null){
    UserRec usr = this.userDM.getUserRecPvt(ac.getCreatedBy());
    act.setCreatedBy(usr);
    act.setCreatedOn(ac.getCreated());
  }
  if(ac.getUpdateBy() != null){
    UserRec usr = this.userDM.getUserRecPvt(ac.getUpdateBy());
    act.setUpdateBy(usr);
    act.setUpdateOn(ac.getUpdateDate());
  }
    act.setDescription(ac.getDescription());
    act.setId(ac.getId());
    act.setName(ac.getName());
    act.setRef(ac.getRef());
    act.setRevision(ac.getRevision());
    act.setPl(ac.isPl());
    
  return act;
}

private FiBsAccount buildBsAccount(FiGlAccountBaseRec glAccount, String pg){
  LOGGER.log(INFO, "buildBsAccount with account {0} page {1}",
          new Object[] {glAccount,pg});
  FiBsAccountRec bsAccount = (FiBsAccountRec)glAccount;
  FiBsAccount ac ;
  boolean newBsAct = false;
  boolean changedBSAct = false;
  
  if(glAccount.getId() == null  ){
   ac = new FiBsAccount();
   User crUsr = em.find(User.class, glAccount.getCreatedBy().getId(), OPTIMISTIC);
   ac.setCreatedBy(crUsr);
   ac.setCreated(glAccount.getCreatedOn());
   newBsAct = true;
   em.persist(ac);
   AuditGlAccount aud = buildAuditGlAccount(ac, crUsr, 'I', pg);
   aud.setNewValue(bsAccount.getRef());
  }else{
   ac = em.find(FiBsAccount.class, bsAccount.getId());
  }
  
  ac = (FiBsAccount)buildBaseAccountForGlAccount(ac, glAccount, pg);
  LOGGER.log(INFO, "After buildBaseAccountForGlAccount");
  if(newBsAct){
  ac.setPl(bsAccount.isPl());
  ac.setBalFwd(bsAccount.isBalFwd());
  } else{
   // changed ?
   User chUsr = em.find(User.class, glAccount.getUpdateBy().getId(), OPTIMISTIC);
   
   if(bsAccount.isBalFwd() != ac.isBalFwd()){
    AuditGlAccount aud = buildAuditGlAccount(ac, chUsr, 'U', pg);
    aud.setFieldName("GL_BS_ACNT_BFWD");
    aud.setNewValue(String.valueOf(bsAccount.isBalFwd()));
    aud.setOldValue(String.valueOf(ac.isBalFwd()));
    changedBSAct = true;
   }
   if(changedBSAct){
    ac.setUpdateBy(chUsr);
    ac.setUpdateDate(bsAccount.getUpdateOn());
   }
  }

  return ac;

}

public FiGlAccountBaseRec FiGlAccountCoaRecPvt(FiGLAccountBase ac){
  return buildFiGlAccountCoaRec(ac);
}
private FiGlAccountBaseRec buildFiGlAccountCoaRec(FiGLAccountBase ac){
  
  FiGlAccountBaseRec rec = new FiGlAccountBaseRec();
  if(ac.getAccountType() != null){
    rec.setAccountType(configDM.buildAccountTypeRecPvt(ac.getAccountType()));
  }
  
  if(ac.getChartOfAccounts() != null){
   rec.setChartOfAccounts(compDM.getChartOfAccountsRecPvt(ac.getChartOfAccounts()));
  }
  
  rec.setCreatedOn(ac.getCreated());
  rec.setCreatedBy(this.userDM.getUserRecPvt(ac.getCreatedBy()));
  rec.setDescription(ac.getDescription());
  rec.setId(ac.getId());
  rec.setName(ac.getName());
  rec.setPl(ac.getAccountType().isProfitAndLossAccount());
  rec.setRef(ac.getRef());
  rec.setRevision(ac.getRevision());
  if(ac.getUpdateBy() != null){
   rec.setUpdateBy(userDM.getUserRecPvt(ac.getUpdateBy()));
   rec.setUpdateOn(ac.getUpdateDate());
  }


  return rec;
}

public FiGlAccountCompRec buildFiCompGlAccountRecPvt(FiGlAccountComp ac){
  FiGlAccountCompRec glAct = buildFiCompGlAccountRec(ac);
  return glAct;
}
/**
 * build application object to represent a pl account
 * @param dbRec
 * @return 
 */
private FiPlAccountRec buildPlAccountRec(FiPlAccount dbRec){
 LOGGER.log(INFO, "buildPlAccountRec called with {0}", dbRec);
 FiPlAccountRec plAc = new FiPlAccountRec();
 plAc = (FiPlAccountRec)buildBaseAccountForGlAccountRec(dbRec,plAc);  
 plAc.setAccountCat(dbRec.getAccountCat());
 
 return plAc;
}
private AuditGlAccount buildAuditGlAccount(FiGLAccountBase acnt, User usr, char usrAction,String pg){
 AuditGlAccount aud = new AuditGlAccount();
 aud.setAuditDate(new Date());
 aud.setCreatedBy(usr);
 aud.setGlAccount(acnt);
 aud.setSource(pg);
 aud.setUsrAction(usrAction);
 em.persist(aud);
 
 return aud;
}

private FiPeriodBalance buildFiPeriodBalance(FiPeriodBalanceRec balRec, String src ){
 
 FiPeriodBalance bal;
 boolean newBal = false;
 boolean changedBal = false;
 
 if(balRec.getId() == null){
  bal = new FiPeriodBalance();
  User usr = em.find(User.class, balRec.getCreatedBy().getId(), OPTIMISTIC);
  bal.setCreatedBy(usr);
  bal.setCreated(balRec.getCreated());
  bal.setBalanceType(balRec.getBalanceType());
  em.persist(bal);
  newBal = true;
 }else{
  bal = em.find(FiPeriodBalance.class, balRec.getId(), OPTIMISTIC);
 }
 if(newBal){
  FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, balRec.getFiGlAccountComp().getId(), OPTIMISTIC);
  bal.setFiGlAccountComp(glAcnt);
  bal.setYear(balRec.getBalYear());
  bal.setBalPeriod(balRec.getBalPeriod());
  AuditPeriodBalance aud = this.buildAuditPeriodBalance(bal, bal.getCreatedBy(), 'I', src);
  aud.setNewValue(String.valueOf(bal.getBalYear()+bal.getBalPeriod()));
  if(balRec.getRestrictedFund() != null && balRec.getRestrictedFund().getId() != null ){
   Fund fnd = em.find(Fund.class, balRec.getRestrictedFund().getId());
   bal.setRestrictedFund(fnd);
  }
 }else{
  User chUsr = em.find(User.class, balRec.getUpdateBy().getId(), OPTIMISTIC);
  Date chDate = balRec.getUpdateDate();
  
  if(balRec.getPeriodBudgetAmount() != bal.getPeriodBudgetAmount()){
   AuditPeriodBalance aud = buildAuditPeriodBalance(bal, chUsr, 'U', src);
   aud.setOldValue(String.valueOf(bal.getPeriodBudgetAmount()));
   aud.setNewValue(String.valueOf(balRec.getPeriodBudgetAmount()));
   bal.setPeriodBudgetAmount(balRec.getPeriodBudgetAmount());
   changedBal = true;
  }
  if(changedBal){
   bal.setUpdateBy(chUsr);
   bal.setUpdateDate(chDate);
 }
 }
 
 return bal;
}
private FiPeriodBalanceRec buildFiPeriodBalanceRec(FiPeriodBalance bal ){
 FiPeriodBalanceRec rec = new FiPeriodBalanceRec();
 
 if(bal.getCreatedBy() != null){
  UserRec crUsr = this.userDM.getUserRecPvt(bal.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreated(bal.getCreated());
 }
 if(bal.getUpdateBy() != null){
  UserRec chUsr = userDM.getUserRecPvt(bal.getUpdateBy());
  rec.setUpdateBy(chUsr);
  rec.setUpdateDate(bal.getUpdateDate());
 }
 rec.setId(bal.getId());
 rec.setBalPeriod(bal.getBalPeriod());
 rec.setBalYear(bal.getBalYear());
 rec.setBalanceType(bal.getBalanceType());
 rec.setBfwdDocAmount(bal.getBfwdDocAmount());
 rec.setCfwdLocalAmount(bal.getCfwdDocAmount());
 FiGlAccountCompRec glAcnt = this.buildFiCompGlAccountRec(bal.getFiGlAccountComp());
 rec.setFiGlAccountComp(glAcnt);
 rec.setPeriodBudgetAmount(bal.getPeriodBudgetAmount());
 rec.setPeriodCreditAmount(bal.getPeriodCreditAmount());
 rec.setPeriodDebitAmount(bal.getPeriodDebitAmount());
 rec.setPeriodDocAmount(bal.getPeriodDocAmount());
 rec.setPeriodLocalAmount(bal.getPeriodLocalAmount());
 if(bal.getProgramBudgetAccount() != null){
  ProgramAccountRec progAcnt = this.progDM.getProgramAccountRec(bal.getProgramBudgetAccount());
  rec.setProgramBudgetAccount(progAcnt);
 }
 if(bal.getProgramCostAccount() != null){
  if(bal.getProgramBudgetAccount() != null && 
          bal.getProgramBudgetAccount().getId() == bal.getProgramCostAccount().getId()){
   rec.setProgramCostAccount(rec.getProgramBudgetAccount());
  }else{
   ProgramAccountRec progAcnt = this.progDM.getProgramAccountRec(bal.getProgramCostAccount());
   rec.setProgramCostAccount(progAcnt);
  }
 }
 if(bal.getRestrictedFund() !=  null){
   FundRec fnd = this.compDM.getRestrictedFundById(bal.getRestrictedFund().getId());
   rec.setRestrictedFund(fnd);
 }
 return rec;
}

private FiGLAccountBase buildBaseAccountForGlAccount(FiGLAccountBase base, 
        FiGlAccountBaseRec rec, String pg ){
 
 if(rec.getChartOfAccounts() == null){
  LOGGER.log(INFO,"Account rec {0} has not chart of account",rec.getRef());
 }
 boolean changedBase = false;
 LOGGER.log(INFO, "GL account base from DB {0} rec {1} rec id {2}", new Object[]{base,rec,rec.getId()});
 if(rec.getId() == null){
  // new account
  AccountType acTy = em.find(AccountType.class, rec.getAccountType().getId(), OPTIMISTIC);
  base.setAccountType(acTy);
  ChartOfAccounts coa = em.find(ChartOfAccounts.class, rec.getChartOfAccounts().getId(), OPTIMISTIC);
  base.setChartOfAccounts(coa);
  base.setDescription(rec.getDescription());
  base.setName(rec.getName());
  base.setPl(rec.isPl());
  base.setRef(rec.getRef());
  base.setReportCat(rec.getReportCat());
 }else{
  // change
  User chUsr = em.find(User.class, rec.getUpdateBy().getId(), OPTIMISTIC);
  
  if((base.getAccountType() == null  && rec.getAccountType() != null) ||
     (base.getAccountType() != null  && rec.getAccountType() == null) ||
     (base.getAccountType() != null  && base.getAccountType().getId() != rec.getAccountType().getId())){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_ACT_TYPE");
   aud.setNewValue(rec.getAccountType().getName());
   aud.setOldValue(base.getAccountType().getName());
   AccountType acTy = em.find(AccountType.class, rec.getAccountType().getId(), OPTIMISTIC);
   base.setAccountType(acTy);
   changedBase = true;
  }
  
  if((base.getChartOfAccounts() == null && rec.getChartOfAccounts() != null )||
     (base.getChartOfAccounts() != null && rec.getChartOfAccounts() == null ) ||
     (base.getChartOfAccounts() != null && base.getChartOfAccounts().getId() != rec.getChartOfAccounts().getId())){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_AC_COA");
   aud.setNewValue(rec.getChartOfAccounts().getReference());
   aud.setOldValue(base.getRef());
   ChartOfAccounts coa = em.find(ChartOfAccounts.class, rec.getChartOfAccounts().getId(), OPTIMISTIC);
   base.setChartOfAccounts(coa);
   changedBase = true;
  }
  
  if((base.getDescription() == null && rec.getDescription() != null)||
     (base.getDescription() != null && rec.getDescription() == null) ||
     base.getDescription() != null && !base.getDescription().equalsIgnoreCase(rec.getDescription())){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_AC_DESC");
   aud.setNewValue(rec.getDescription());
   aud.setOldValue(base.getDescription());
   base.setDescription(rec.getDescription());
   changedBase = true;
  }
  LOGGER.log(INFO, "rec name {0} db name {1}", new Object[]{rec.getName(),base.getName()});
  if((rec.getName() == null && base.getName() != null) ||
     (rec.getName() != null && base.getName() == null) ||
     (rec.getName() != null && !rec.getName().equalsIgnoreCase(base.getName()))   ){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_AC_NAME");
   aud.setNewValue(rec.getName());
   aud.setOldValue(base.getName());
   base.setName(rec.getName());
   changedBase = true;
  }
  
  if((rec.getRef() == null && base.getRef() != null) ||
     (rec.getRef() != null && base.getRef() == null) ||
     (rec.getRef() != null && !rec.getRef().equalsIgnoreCase(base.getRef()))){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_AC_REF");
   aud.setNewValue(rec.getRef());
   aud.setOldValue(base.getRef());
   base.setRef(rec.getRef());
   changedBase = true;
  }
  
  if((rec.getReportCat() == null && base.getReportCat() != null) ||
     (rec.getReportCat() != null && base.getReportCat() == null) ||
     (rec.getReportCat() != null && !rec.getRef().equalsIgnoreCase(base.getReportCat()))){
   AuditGlAccount aud = buildAuditGlAccount(base, chUsr, 'U', pg);
   aud.setFieldName("GL_AC_REP_CAT");
   aud.setNewValue(rec.getReportCat());
   aud.setOldValue(base.getReportCat());
   base.setReportCat(rec.getReportCat());
   changedBase = true;
  }
  
  if(changedBase){
   base.setUpdateBy(chUsr);
   base.setUpdateDate(rec.getUpdateOn());
  }
 }
 return base;
}
/**
 * Called from build plAccount or bsAccount
 * Builds the common part of GL accouunt
 * @param dbRec
 * @param rec
 * @return 
 */
private FiGlAccountBaseRec buildBaseAccountForGlAccountRec(FiGLAccountBase dbRec, 
        FiGlAccountBaseRec rec ){
 LOGGER.log(INFO, "dbRec {0} rec {1}", new Object[]{dbRec,rec});
 rec.setId(dbRec.getId());
 rec.setName(dbRec.getName());
 rec.setDescription(dbRec.getDescription());
 UserRec crUsr =  userDM.getUserRecPvt(dbRec.getCreatedBy());
 rec.setCreatedBy(crUsr);
 rec.setCreatedOn(dbRec.getCreated());
 if(dbRec.getUpdateBy() != null){
  UserRec updtUsr = userDM.getUserRecPvt(dbRec.getUpdateBy());
  rec.setUpdateBy(updtUsr);
  rec.getUpdateOn();
 }
 AccountTypeRec actType = configDM.buildAccountTypeRecPvt(dbRec.getAccountType());
 rec.setAccountType(actType);
 ChartOfAccountsRec coa = compDM.getChartOfAccountsRecPvt(dbRec.getChartOfAccounts());
 rec.setChartOfAccounts(coa);
 rec.setPl(dbRec.isPl());
 rec.setRef(dbRec.getRef());
 
 //rec.setRevision(dbRec.getRevision());
 
 
 
 return rec;
}

public FiGlAccountCompRec getFiCompGlAccountRec(FiGlAccountComp ac){
 return buildFiCompGlAccountRec(ac);
}

public FiGlAccountCompRec getFiCompGlAccountRec(String glAcntRef, CompanyBasicRec comp){
 FiGlAccountCompRec glCompAcRec;
 Query q = em.createNamedQuery("GlCompAcnt");
 q.setParameter("compId", comp.getId());
 q.setParameter("acntRef", glAcntRef);
 try{
  FiGlAccountComp compAc = (FiGlAccountComp)q.getSingleResult();
  glCompAcRec = this.buildFiCompGlAccountRec(compAc);
  return glCompAcRec;
 }catch(NoResultException ex){
  LOGGER.log(INFO, "No Gl account for acnt {0} comp ref {1}", 
          new Object[]{glAcntRef, comp.getReference()});
  return null;
 }catch(NonUniqueResultException ex){
  LOGGER.log(INFO, "GL account not found");
  return null;
 }catch ( PersistenceException ex){
  LOGGER.log(INFO, "Other persistence exception {0}", ex.getLocalizedMessage());
  return null;
 }
 
 
}

public List<FiGlAccountCompRec> getGlCompRecAcntsForCoaAcnt(FiGlAccountBaseRec coaAcntRec ){
 
 FiGLAccountBase coaAcnt = em.find(FiGLAccountBase.class, coaAcntRec.getId(), OPTIMISTIC);
 if(coaAcnt == null){
  return null;
 }
 List<FiGlAccountComp> compAcnts = coaAcnt.getGlAccountsComp();
 if(compAcnts == null){
  LOGGER.log(INFO, "No comp accounts for GL account {0}", coaAcnt.getRef());
  return null;
 }
 List<FiGlAccountCompRec> compAcntRecList = new ArrayList<FiGlAccountCompRec>();
 for(FiGlAccountComp compAcnt:compAcnts ){
  FiGlAccountCompRec compAcntRec = this.buildFiCompGlAccountRec(compAcnt);
  compAcntRecList.add(compAcntRec);
 }
 return compAcntRecList;
}
private FiGlAccountCompRec buildFiCompGlAccountRec(FiGlAccountComp ac){
  if(ac == null){
   return null;
  }
  FiGlAccountCompRec rec = new FiGlAccountCompRec();
  rec.setAnalysis1(ac.getAnalysis1());
  rec.setAnalysis2(ac.getAnalysis2());
  rec.setRepCategory(ac.getRepCategory());
  rec.setPersonResponsible(ac.getRespPerson());

  if(ac.getChangedBy() != null){
    UserRec usr = userDM.getUserRecPvt(ac.getChangedBy());
    rec.setChangedOn(ac.getChangedOn());
  }
  String coaAccountType = ac.getCoaAccount().getClass().getName();
  
  if(coaAccountType.equalsIgnoreCase("com.rationem.entity.fi.glAccount.FiPlAccount")){
   FiPlAccountRec plAc = (FiPlAccountRec)buildPlAccountRec((FiPlAccount)ac.getCoaAccount());
   rec.setCoaAccount(plAc); 
  }else{
   FiBsAccountRec bsAc = new FiBsAccountRec();
  // bsAc = (FiBsAccountRec)buildBaseAccountForGlAccount(ac.getGlAccountCoa(), bsAc);
   bsAc = (FiBsAccountRec)this.buildFiBsAccountRec((FiBsAccount)ac.getCoaAccount());
   rec.setCoaAccount(bsAc);
  }
  
  if(ac.getCompany() != null){

    rec.setCompany(compDM.buildCompanyBasicRecPvt(ac.getCompany()));
  }
  if(ac.getCreatedBy() != null){
   rec.setCreatedBy(userDM.getUserRecPvt(ac.getCreatedBy()));
   rec.setCreatedOn(ac.getCreatedOn());
  }

  
  rec.setId(ac.getId());
  rec.setNoVatAllowed(ac.isNoVatAllowed());
  rec.setRevision(ac.getRevision());
  if(ac.getSortOrder() != null){
    rec.setSortOrder(sysBuffer.getSortOrderById(ac.getSortOrder().getId()));
  }
  VatCodeCompanyRec vatCode = new VatCodeCompanyRec();  //TODO: build VAT 
  rec.setVatCode(vatCode);
  



  return rec;
  
}
private FiGlAccountComp buildFiCompGlAccount(FiGlAccountCompRec ac, String pg){
  LOGGER.log(INFO, "buildFiCompGlAccount called with {0} for company {1}", 
          new Object[]{ac,ac.getCompany()});
  FiGlAccountComp acnt;
  boolean newGlAcnt = false;
  boolean changedGlAcnt = false;
  
  if(ac.getId() == null  ){
   acnt = new FiGlAccountComp();
   User crUsr = em.find(User.class, ac.getCreatedBy().getId(), OPTIMISTIC);
   acnt.setCreatedBy(crUsr);
   acnt.setCreatedOn(ac.getCreatedOn());
   em.persist(acnt);
   AuditGlCompAccount aud = this.buildAuditGlCompAccount(acnt, crUsr, 'I', pg);
   aud.setNewValue(ac.getCoaAccount().getRef() + ":"+ac.getCompany().getReference());
   newGlAcnt = true;
  }else{
   acnt = em.find(FiGlAccountComp.class, ac.getId(), OPTIMISTIC);
  }
  
  if(newGlAcnt){
   acnt.setAccountClearing(ac.isAccountClearing());
   acnt.setAnalysis1(ac.getAnalysis1());
   acnt.setAnalysis2(ac.getAnalysis2());
   if(ac.getBankAccountCompanyCleared() != null){
    BankAccountCompany bnk = em.find(BankAccountCompany.class, ac.getBankAccountCompanyCleared().getId(), OPTIMISTIC);
    acnt.setBankAccountCompanyCleared(bnk);
   }
   if(ac.getBankAccountCompanyUncleared() != null){
    BankAccountCompany bnk = em.find(BankAccountCompany.class, ac.getBankAccountCompanyUncleared().getId(), OPTIMISTIC);
    acnt.setBankAccountCompanyUncleared(bnk);
   }
   if(ac.getCoaAccount() != null && ac.getCoaAccount().getId() != null){
    FiGLAccountBase coaAcnt = em.find(FiGLAccountBase.class, ac.getCoaAccount().getId(), OPTIMISTIC);
    acnt.setCoaAccount(coaAcnt);
   }
   if(ac.getCompany() != null){
    CompanyBasic company = em.find(CompanyBasic.class, ac.getCompany().getId());
    acnt.setCompany(company);
   }
   acnt.setRepCategory(ac.getRepCategory());
   acnt.setRespPerson(ac.getPersonResponsible());
   acnt.setNoVatAllowed(ac.isNoVatAllowed());
   if(ac.getSortOrder() != null && ac.getSortOrder() != null){
    LOGGER.log(INFO, "Sort order id {0} name {1}", new Object[]{ac.getSortOrder().getId(),ac.getSortOrder().getName()});
    SortOrder so = em.find(SortOrder.class, ac.getSortOrder().getId(), OPTIMISTIC);
    acnt.setSortOrder(so);
   }
   if(ac.getVatCode() != null){
    VatCodeCompany v = em.find(VatCodeCompany.class, ac.getVatCode().getId(), OPTIMISTIC);
    acnt.setVatCode(v);
   }
  }else{
   // changes
   User chUsr = em.find(User.class, ac.getChangedBy().getId(), OPTIMISTIC);
   if(ac.isAccountClearing() != acnt.isAccountClearing()){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_CL");
    aud.setNewValue(String.valueOf(ac.isAccountClearing()));
    aud.setOldValue(String.valueOf(acnt.isAccountClearing()));
    acnt.setAccountClearing(ac.isAccountClearing());
    changedGlAcnt = true;
   }
   
   if(ac.isNoVatAllowed() != acnt.isNoVatAllowed()){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_NO_VAT");
    aud.setNewValue(String.valueOf(ac.isAccountClearing()));
    aud.setOldValue(String.valueOf(acnt.isAccountClearing()));
    acnt.setAccountClearing(ac.isAccountClearing());
    changedGlAcnt = true;
   }
   
   if((ac.getAnalysis1() == null && acnt.getAnalysis1() != null) ||
      (ac.getAnalysis1() != null && acnt.getAnalysis1() == null) ||
      (ac.getAnalysis1() != null && !ac.getAnalysis1().equalsIgnoreCase(acnt.getAnalysis1()))  ){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_ANAL1");
    aud.setNewValue(ac.getAnalysis1());
    aud.setOldValue(acnt.getAnalysis1());
    acnt.setAnalysis1(ac.getAnalysis1());
    changedGlAcnt = true;
   }
   
   if((ac.getAnalysis2() == null && acnt.getAnalysis2() != null) ||
      (ac.getAnalysis2() != null && acnt.getAnalysis2() == null) ||
      (ac.getAnalysis2() != null && !ac.getAnalysis2().equalsIgnoreCase(acnt.getAnalysis2()))  ){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_ANAL2");
    aud.setNewValue(ac.getAnalysis2());
    aud.setOldValue(acnt.getAnalysis2());
    acnt.setAnalysis2(ac.getAnalysis2());
    changedGlAcnt = true;
   }
   
   if((ac.getBankAccountCompanyCleared() == null && acnt.getBankAccountCompanyCleared() != null) ||
      (ac.getBankAccountCompanyCleared() != null && acnt.getBankAccountCompanyCleared() == null) ||
      (ac.getBankAccountCompanyCleared() != null && !Objects.equals(ac.getBankAccountCompanyCleared().getId(), acnt.getBankAccountCompanyCleared().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_BNK_CL");
    if(ac.getBankAccountCompanyCleared() != null){
    aud.setNewValue(ac.getBankAccountCompanyCleared().getAccountNumber());
    }
    if(acnt.getBankAccountCompanyCleared() != null){
    aud.setOldValue(acnt.getBankAccountCompanyCleared().getAccountNumber());
    }
    BankAccountCompany clBnk = em.find(BankAccountCompany.class, ac.getBankAccountCompanyCleared().getId(), OPTIMISTIC);
    acnt.setBankAccountCompanyCleared(clBnk);
    
    changedGlAcnt = true;
    
   }
    
   
   if((ac.getBankAccountCompanyUncleared() == null && acnt.getBankAccountCompanyUncleared() != null) ||
      (ac.getBankAccountCompanyUncleared() != null && acnt.getBankAccountCompanyUncleared() == null) ||
      (ac.getBankAccountCompanyUncleared() != null && !Objects.equals(ac.getBankAccountCompanyUncleared().getId(), acnt.getBankAccountCompanyUncleared().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
    aud.setNewValue(ac.getBankAccountCompanyUncleared().getAccountNumber());
    if(acnt.getBankAccountCompanyUncleared() != null){
     aud.setOldValue(acnt.getBankAccountCompanyUncleared().getAccountNumber());
    }
    BankAccountCompany unclBnk = em.find(BankAccountCompany.class, ac.getBankAccountCompanyUncleared().getId(), OPTIMISTIC);
    acnt.setBankAccountCompanyCleared(unclBnk);
    changedGlAcnt = true;
   }
   
   if((ac.getCoaAccount() == null && acnt.getCoaAccount() != null) ||
      (ac.getCoaAccount() != null && acnt.getCoaAccount() == null) ||
      (ac.getCoaAccount() != null && !Objects.equals(ac.getCoaAccount().getId(), acnt.getCoaAccount().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_COA_AC");
    aud.setNewValue(ac.getCoaAccount().getRef());
    aud.setOldValue(acnt.getCoaAccount().getRef());
    FiGLAccountBase coaAcnt = em.find(FiGLAccountBase.class, ac.getCoaAccount().getId(), OPTIMISTIC);
    acnt.setCoaAccount(coaAcnt);
    changedGlAcnt = true;
   }
   
   if((ac.getCompany() == null && acnt.getCompany() != null) ||
      (ac.getCompany() != null && acnt.getCompany() == null) ||
      (ac.getCompany() != null && !Objects.equals(ac.getCompany().getId(), acnt.getCompany().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_COMP");
    aud.setNewValue(ac.getCoaAccount().getRef());
    aud.setOldValue(acnt.getCoaAccount().getRef());
    CompanyBasic comp = em.find(CompanyBasic.class, ac.getCompany().getId(), OPTIMISTIC);
    acnt.setCompany(comp);
    changedGlAcnt = true;
   }
   
   if((ac.getPersonResponsible() == null && acnt.getRespPerson() != null) ||
      (ac.getPersonResponsible() != null && acnt.getRespPerson() == null) ||
      (ac.getPersonResponsible() != null && !ac.getPersonResponsible().equalsIgnoreCase(acnt.getRespPerson()))    ){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_PERS_RESP");
    aud.setNewValue(ac.getPersonResponsible());
    aud.setOldValue(acnt.getRespPerson());
    acnt.setRespPerson(ac.getPersonResponsible());
    changedGlAcnt = true;
   }
   
   if((ac.getRepCategory() == null && acnt.getRepCategory() != null) ||
      (ac.getRepCategory() != null && acnt.getRepCategory() == null) ||
      (ac.getRepCategory() != null && !ac.getRepCategory().equalsIgnoreCase(acnt.getRepCategory()) )){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_REP_CAT");
    aud.setNewValue(ac.getRepCategory());
    aud.setOldValue(acnt.getRepCategory());
    acnt.setRepCategory(ac.getRepCategory());
    changedGlAcnt = true;
   }
   
   if((ac.getSortOrder() == null && acnt.getSortOrder() != null) ||
      (ac.getSortOrder() != null && acnt.getSortOrder() == null) ||
      (ac.getSortOrder() != null && !Objects.equals(ac.getSortOrder().getId(), acnt.getSortOrder().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_SORT");
    aud.setNewValue(ac.getSortOrder().getSortCode());
    aud.setOldValue(acnt.getSortOrder().getSortCode());
    SortOrder s = em.find(SortOrder.class, ac.getSortOrder().getId(), OPTIMISTIC);
    acnt.setSortOrder(s);
    changedGlAcnt = true;
   }
  //VatCodeCompanyRec 
   if((ac.getVatCode() == null && acnt.getVatCode() != null) ||
      (ac.getVatCode() != null && acnt.getVatCode() == null)||
     (ac.getVatCode() != null && !Objects.equals(ac.getVatCode().getId(), acnt.getVatCode().getId()))){
    AuditGlCompAccount aud = buildAuditGlCompAccount(acnt, chUsr, 'U', pg);
    aud.setFieldName("GL_COM_AC_VAT_CD");
    if(ac.getVatCode() != null && ac.getVatCode().getVatCode() == null){
     VatCodeCompanyRec vc = sysBuffer.getVatCodeForCompVatCode(ac.getVatCode());
     ac.setVatCode(vc);
    }
    aud.setNewValue(ac.getVatCode().getVatCode().getCode());
    if(acnt.getVatCode() != null){
     aud.setOldValue(acnt.getVatCode().getVatCode().getCode());
    }
    VatCodeCompany v = em.find(VatCodeCompany.class, ac.getVatCode().getId(), OPTIMISTIC);
    acnt.setVatCode(v);
    changedGlAcnt = true;
   }
   
   if(changedGlAcnt){
    acnt.setChangedBy(chUsr);
    acnt.setChangedOn(ac.getChangedOn());
   }
  }
  LOGGER.log(INFO, "Company account returned {0}", acnt);
  return acnt;
}

private FiGlAccountComp buildFiCompGlAccount(FiGLAccountBase chartAct,
        FiGlAccountCompRec ac, String pg) throws BacException{
  LOGGER.log(INFO, "buildFiCompGlAccount with db GL chart ac {0} company GL rec {1} ",
    new Object[] {chartAct,ac});
  FiGlAccountComp compAct;
  boolean newAc = false;
  boolean changedAc = false;
  if(ac.getId() == null){
    compAct = new FiGlAccountComp ();
    User crUsr = em.find(User.class, ac.getCreatedBy().getId(), OPTIMISTIC);
    compAct.setCreatedBy(crUsr);
    compAct.setCreatedOn(ac.getCreatedOn());
    em.persist(compAct);
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, crUsr, 'I', pg);
    aud.setNewValue(ac.getCoaAccount().getRef());
    newAc = true;
  }else{
    compAct = em.find(FiGlAccountComp.class, ac.getId(), OPTIMISTIC);
  }
  LOGGER.log(INFO, "Line 211");
  if(newAc){
   compAct.setAccountClearing(ac.isAccountClearing());
   compAct.setAnalysis1(ac.getAnalysis1());
   compAct.setAnalysis2(ac.getAnalysis2());
   if(ac.getBankAccountCompanyCleared() != null){
    BankAccountCompany bnkAcComp = em.find(BankAccountCompany.class, ac.getBankAccountCompanyCleared().getId(), OPTIMISTIC);
    compAct.setBankAccountCompanyCleared(bnkAcComp);
   }
   if(ac.getBankAccountCompanyUncleared() != null){
    BankAccountCompany bnkAcComp = em.find(BankAccountCompany.class, ac.getBankAccountCompanyUncleared().getId(), OPTIMISTIC);
    compAct.setBankAccountCompanyUncleared(bnkAcComp);
   }
   
   if(ac.getCoaAccount() != null){
    FiGLAccountBase coaAcnt = em.find(FiGLAccountBase.class, ac.getCoaAccount().getId(), OPTIMISTIC);
    compAct.setCoaAccount(coaAcnt);
   }
   
   if(ac.getCompany() != null){
    CompanyBasic comp = em.find(CompanyBasic.class, ac.getCompany().getId(), OPTIMISTIC);
    compAct.setCompany(comp);
   }
   compAct.setNoVatAllowed(ac.isNoVatAllowed());
   compAct.setRespPerson(ac.getPersonResponsible());
   compAct.setRepCategory(ac.getRepCategory());
   if(ac.getSortOrder() != null){
    SortOrder s = em.find(SortOrder.class, ac.getSortOrder().getId(), OPTIMISTIC);
    compAct.setSortOrder(s);
   }
   if(ac.getVatCode() != null){
    VatCodeCompany v = em.find(VatCodeCompany.class, ac.getVatCode().getId(), OPTIMISTIC);
    compAct.setVatCode(v);
   }
   compAct.setNoVatAllowed(ac.isNoVatAllowed());
  }else{
   // changed ?
   User chUsr = em.find(User.class, ac.getChangedBy().getId(), OPTIMISTIC);
   if(ac.isAccountClearing() != compAct.isAccountClearing()){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_CL");
    aud.setNewValue(String.valueOf(ac.isAccountClearing()));
    aud.setOldValue(String.valueOf(compAct.isAccountClearing()));
    compAct.setAccountClearing(ac.isAccountClearing());
    changedAc = true;
   }
   
   if(ac.isNoVatAllowed() != compAct.isNoVatAllowed()){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_NO_VAT");
    aud.setNewValue(String.valueOf(ac.isNoVatAllowed()));
    aud.setOldValue(String.valueOf(compAct.isNoVatAllowed()));
    compAct.setNoVatAllowed(ac.isNoVatAllowed());
    changedAc = true;
   }
   
   if((ac.getAnalysis1() == null && compAct.getAnalysis1() != null )|| 
      (ac.getAnalysis1() != null && compAct.getAnalysis1() == null ) ||
      (ac.getAnalysis1() != null && !ac.getAnalysis1().equalsIgnoreCase(compAct.getAnalysis1()))){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_ANAL1");
    aud.setNewValue(ac.getAnalysis1());
    aud.setOldValue(compAct.getAnalysis1());
    compAct.setAnalysis1(ac.getAnalysis1());
    changedAc = true;
   }
   
   if((ac.getAnalysis2() == null && compAct.getAnalysis2() != null )|| 
      (ac.getAnalysis2() != null && compAct.getAnalysis2() == null ) ||
      (ac.getAnalysis2() != null && !ac.getAnalysis2().equalsIgnoreCase(compAct.getAnalysis2()))){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_ANAL2");
    aud.setNewValue(ac.getAnalysis2());
    aud.setOldValue(compAct.getAnalysis2());
    compAct.setAnalysis1(ac.getAnalysis2());
    changedAc = true;
   }
   
   if((ac.getBankAccountCompanyCleared() == null && compAct.getBankAccountCompanyCleared() != null )|| 
      (ac.getBankAccountCompanyCleared() != null && compAct.getBankAccountCompanyCleared() == null ) ||
      (ac.getBankAccountCompanyCleared() != null && 
           ac.getBankAccountCompanyCleared().getId() !=  compAct.getBankAccountCompanyCleared().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_BNK_CL");
    aud.setNewValue(ac.getBankAccountCompanyCleared().getAccountNumber());
    aud.setOldValue(compAct.getBankAccountCompanyCleared().getAccountNumber());
    BankAccountCompany bnkAc = em.find(BankAccountCompany.class, ac.getBankAccountCompanyCleared().getId(), OPTIMISTIC);
    compAct.setBankAccountCompanyCleared(bnkAc);
    changedAc = true;
   }
   
   if((ac.getBankAccountCompanyUncleared() == null && compAct.getBankAccountCompanyUncleared() != null )|| 
      (ac.getBankAccountCompanyUncleared() != null && compAct.getBankAccountCompanyUncleared() == null ) ||
      (ac.getBankAccountCompanyUncleared() != null && 
           ac.getBankAccountCompanyUncleared().getId() !=  compAct.getBankAccountCompanyUncleared().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
    aud.setNewValue(ac.getBankAccountCompanyUncleared().getAccountNumber());
    aud.setOldValue(compAct.getBankAccountCompanyUncleared().getAccountNumber());
    BankAccountCompany bnkAc = em.find(BankAccountCompany.class, ac.getBankAccountCompanyUncleared().getId(), OPTIMISTIC);
    compAct.setBankAccountCompanyUncleared(bnkAc);
    changedAc = true;
   }
   
   if((ac.getBankAccountCompanyUncleared() == null && compAct.getBankAccountCompanyUncleared() != null )|| 
      (ac.getCoaAccount() != null && compAct.getCoaAccount() == null ) ||
      (ac.getCoaAccount() != null && 
           ac.getCoaAccount().getId() !=  compAct.getCoaAccount().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_BNK_UNCLR");
    aud.setNewValue(ac.getCoaAccount().getRef());
    aud.setOldValue(compAct.getCoaAccount().getRef());
    FiGLAccountBase coa = em.find(FiGLAccountBase.class, ac.getCoaAccount().getId(), OPTIMISTIC);
    compAct.setCoaAccount(coa);
    changedAc = true;
   }
   if((ac.getCompany() == null && compAct.getCompany() != null )|| 
      (ac.getCompany() != null && compAct.getCompany() == null ) ||
      (ac.getCompany() != null && 
           ac.getCompany().getId() !=  compAct.getCompany().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_COMP");
    aud.setNewValue(ac.getCompany().getReference());
    aud.setOldValue(compAct.getCompany().getNumber());
    CompanyBasic comp = em.find(CompanyBasic.class, ac.getCompany().getId(), OPTIMISTIC);
    compAct.setCompany(comp);
    changedAc = true;
   }
   
   if((ac.getPersonResponsible() == null && compAct.getRespPerson() != null) ||
      (ac.getPersonResponsible() != null && compAct.getRespPerson() == null) ||
      (ac.getPersonResponsible() != null && !ac.getPersonResponsible().equalsIgnoreCase(compAct.getRespPerson()))){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_PERS_RESP");
    aud.setNewValue(ac.getPersonResponsible());
    aud.setOldValue(compAct.getRespPerson());
    compAct.setRespPerson(ac.getPersonResponsible());
    changedAc = true;
   }
   
   if((ac.getRepCategory() == null && compAct.getRepCategory() != null) ||
     (ac.getRepCategory() != null && compAct.getRepCategory() == null) ||
      (ac.getRepCategory() != null && 
           !ac.getRepCategory().equalsIgnoreCase(compAct.getRepCategory()))){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_REP_CAT");
    aud.setNewValue(ac.getRepCategory());
    aud.setOldValue(compAct.getRepCategory());
    compAct.setRepCategory(ac.getRepCategory());
    changedAc = true;
   }
   
   if((ac.getSortOrder() == null && compAct.getSortOrder() != null) ||
     (ac.getSortOrder() != null && compAct.getSortOrder() == null) ||
      (ac.getSortOrder() != null && 
           ac.getSortOrder().getId() != compAct.getSortOrder().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COMP_AC_SORT");
    aud.setNewValue(ac.getSortOrder().getSortCode());
    aud.setOldValue(compAct.getSortOrder().getSortCode());
    SortOrder s = em.find(SortOrder.class, ac.getSortOrder().getId(), OPTIMISTIC);
    compAct.setSortOrder(s);
    changedAc = true;
   }
   
   if((ac.getVatCode() == null && compAct.getVatCode() != null) ||
     (ac.getVatCode() != null && compAct.getVatCode() == null) ||
      (ac.getVatCode() != null && 
           ac.getVatCode().getId() != compAct.getVatCode().getId())){
    AuditGlCompAccount aud = this.buildAuditGlCompAccount(compAct, chUsr, 'U', pg);
    aud.setFieldName("GL_COM_AC_VAT_CD");
    aud.setNewValue(ac.getVatCode().getVatCode().getCode());
    aud.setOldValue(compAct.getVatCode().getVatCode().getCode());
    VatCodeCompany v = em.find(VatCodeCompany.class, ac.getVatCode().getId(), OPTIMISTIC);
    compAct.setVatCode(v);
    changedAc = true;
   }
   
   if(changedAc){
    compAct.setChangedBy(chUsr);
    compAct.setChangedOn(ac.getChangedOn());
   }
  }
  
  



  
 
 LOGGER.log(INFO, "End buildFiCompGlAccount with account: {0} ",compAct);
  return compAct;
}

/**
 * Create Central GL Account - at Chart of Accounts level
 * @param glAccount Account of sub-type plAccount or bsAccount
 * @param type Type of account. Is pl for Profit and loss or bs for Balance Sheet
 * @throws BacException
 */
  public void createGlAccountinChart(FiGlAccountBaseRec glAccount, String type, String pg) throws BacException {
    LOGGER.log(INFO,"createGlAccountinChart called with account: {0} and type {1}",
            new Object[] {glAccount,type});
    if(type.equalsIgnoreCase("pl")){
      FiPlAccount ac = this.buildPlAccount((FiPlAccountRec)glAccount,pg );
      try{
        em.persist(ac);
      }catch(EntityExistsException ex){
        throw new BacException("GL:05 PL Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:06 PL Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:07 PL Account DB transaction error");
      }
    }else{
      FiBsAccount ac = this.buildBsAccount(glAccount, pg);
      try{
        em.persist(ac);
      }catch(EntityExistsException ex){
        throw new BacException("GL:08 BS Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:09 BS Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:10 BS Account DB transaction error");
      }
    }
  }

public FiGlAccountBaseRec getGlAccountById(Long compId) throws BacException{
  LOGGER.log(INFO, "getGlAccountById called with id {0}", compId);
  FiGlAccountBaseRec compRet = null;
  FiGLAccountBase compBase = em.find(FiGLAccountBase.class, compId);
  if(compBase == null){
   throw new BacException("Could not find GL account","DM FI Master:01");
  }
  compRet = this.buildFiGlAccountCoaRec(compBase);
  return compRet;
}

public boolean addCompAccount(FiGLAccountBase accntBase, FiGlAccountCompRec compAc, User usr, String view){
 LOGGER.log(INFO, "addCompAccount called for comp ac with id {0}",compAc.getId());
 boolean rc = false;
 FiGlAccountComp compActBase;
 boolean newAc = false;
 if(compAc.getId() != null && compAc.getId() > 1 ){
  
  compActBase = em.find(FiGlAccountComp.class, compAc.getId(), OPTIMISTIC);
 }else{
  compActBase = new FiGlAccountComp();
  newAc = true;
 }
 LOGGER.log(INFO, "New comp ac {0}", newAc);
 if(newAc){
  compActBase.setCreatedBy(usr);
  compActBase.setCreatedOn(new Date());
  em.persist(compActBase);
  compActBase.setRepCategory(compAc.getRepCategory());

  CompanyBasic comp = em.find(CompanyBasic.class, compAc.getCompany().getId(), OPTIMISTIC);
  compActBase.setCompany(comp);
  compActBase.setCoaAccount(accntBase);
  compActBase.setAnalysis1(compAc.getAnalysis1());
  compActBase.setAnalysis2(compAc.getAnalysis2());
  if(compAc.getSortOrder() != null){
   SortOrder s = em.find(SortOrder.class, compAc.getSortOrder().getId(), OPTIMISTIC);
   compActBase.setSortOrder(s);
  }
  if(compAc.getVatCode() != null){
   VatCode vat = em.find(VatCode.class, compAc.getVatCode().getId(), OPTIMISTIC);
   List<VatCodeCompany> compVatList = vat.getVatCodeComps();
   boolean found = false;
   ListIterator<VatCodeCompany> compVatLi = compVatList.listIterator();
   while(compVatLi.hasNext() && !found){
    VatCodeCompany rec = compVatLi.next();
    if(rec.getCompany().getId() == comp.getId()){
     compActBase.setVatCode(rec);
     found = true;
    }
   }
  }
  compActBase.setRespPerson(compAc.getPersonResponsible());
  AuditGlCompAccount aud = new AuditGlCompAccount();
  aud.setAuditDate(new Date());
  aud.setCompAccount(compActBase);
  aud.setCreatedBy(usr);
  aud.setNewValue(compActBase.getCoaAccount().getRef() + compActBase.getCompany().getNumber() );
  aud.setSource(view);
  aud.setUsrAction('I');
  em.persist(aud);
  rc = true;
   
  
 }
 return rc;
}
  
  
/**
 * Creates Profit and loss  account at both Chart and company level
 * @param glAcountChart Chart of accounts details
 * @param glAccountComp Company code account details
 * @throws BacException
 */
  public FiPlAccountRec newCompPlAccount(FiPlAccountRec glAcountChart,
          FiGlAccountCompRec glAccountComp, Long companyId, String pg)
          throws BacException {
    LOGGER.log(INFO, "newCompGlAccount called with Chart GL {0} company GL {1}",
            new Object[] {glAcountChart,glAccountComp});
    //if(type.equalsIgnoreCase("pl")){
      FiPlAccount ac = this.buildPlAccount(glAcountChart, pg);
      LOGGER.log(INFO, "newCompPlAccount completed buildPlAccount {0}", ac);

      glAcountChart.setRef(ac.getRef());
      FiGlAccountComp acComp = this.buildFiCompGlAccount(ac, glAccountComp, pg);
      LOGGER.log(INFO, "newCompPlAccount After buildFiCompGlAccount {0}", ac);
      try{
        em.persist(ac);
        LOGGER.log(INFO, "newCompPlAccount After persist {0}", ac);

      }catch(EntityExistsException ex){
        throw new BacException("GL:05 PL Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:05 PL Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:06 PL Account DB transaction error");
      }
      try{
        em.persist(acComp);
        LOGGER.log(INFO, "newCompPlAccount After persist {0}", ac);
      }catch(EntityExistsException ex){
        throw new BacException("GL:05 PL Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:05 PL Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:06 PL Account DB transaction error");
      }
      
  /*  }else{
      FiBsAccount ac = this.buildBsAccount(glAcountChart, true);
      FiGlAccountComp acComp = this.buildFiCompGlAccount(ac, glAccountComp, true);
      try{
        em.persist(ac);
      }catch(EntityExistsException ex){
        throw new BacException("GL:05 BS Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:05 BS Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:06 BS Account DB transaction error");
      }
      try{
        em.persist(acComp);
      }catch(EntityExistsException ex){
        throw new BacException("GL:05 PL Account already exists");
      }catch(IllegalArgumentException ex){
        throw new BacException("GL:05 PL Account not a DB object");
      }catch(TransactionRequiredException ex){
        throw new BacException("GL:06 PL Account DB transaction error");
      }
    }*/
      return glAcountChart;
  }
  public List<FiGlAccountBaseRec> getGlAccountsByRef(String ref){
   
   List<FiGlAccountBaseRec> retList = new ArrayList<>();
   if(StringUtils.isAnyBlank(ref)){
    Query q = em.createNamedQuery("findAllGlAccounts");
    List rs = q.getResultList();
    for(Object cur: rs){
     FiGlAccountBaseRec ac = this.buildFiGlAccountCoaRec((FiGLAccountBase)cur);
     retList.add(ac);
    }
   }else{
    // entry in ref
    ref = StringUtils.remove(ref, "%");
    ref = StringUtils.appendIfMissing(ref, "%");
    Query q = em.createNamedQuery("glAccountsByRef");
    q.setParameter("GlReference", ref);
    List rs = q.getResultList();
    for(Object cur: rs){
     FiGlAccountBaseRec ac = this.buildFiGlAccountCoaRec((FiGLAccountBase)cur);
     retList.add(ac);
    }
    
   }
   
   
   return retList;
  }

 public List<FiGlAccountCompRec> getGlAccountsForBankUnclr(BankAccountCompanyRec bnk){
  
 LOGGER.log(INFO, "getGlAccountsForBankUnclr called");
 CompanyBasicRec comp = bnk.getComp();
 Query q = em.createNamedQuery("GlCompAcntaAllForBnkClr");
 q.setParameter("compId", comp.getId());
 q.setParameter("bnkAcntId", bnk.getId());
 
 List rs = q.getResultList();
 LOGGER.log(INFO, "RS {0}",rs);
 if (rs == null || rs.isEmpty()){
  return null;
 }
 
 List<FiGlAccountCompRec> retList = new ArrayList<>();
 for(Object curr: rs){
  FiGlAccountCompRec currRec = this.buildFiCompGlAccountRec((FiGlAccountComp)curr);
  retList.add(currRec);
 }
 return retList;
 }
  /**
 *
 * @param companyId ID of the company for which the GL accounts should be returned
 * @return
 * @throws BacException
 */
 public List<FiGlAccountCompRec> getGlAcntsByTypeForComp(CompanyBasicRec comp,AccountTypeRec acntTy) {
  LOGGER.log(INFO, "getGlAcntsByTypeForComp");
  
  Query q = em.createNamedQuery("GlCompAcntsAcntTyp");
  q.setParameter("compId", comp.getId());
  q.setParameter("acntTyId", acntTy.getId());
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  List<FiGlAccountCompRec> retList = new ArrayList<>();
  for(Object o:rs){
   FiGlAccountCompRec curr = this.buildFiCompGlAccountRec((FiGlAccountComp)o);
   retList.add(curr);
  }
  return retList;
 }
 public List<FiGlAccountCompRec> getGlAccountsForCompany(Long companyId) throws BacException {
    LOGGER.log(INFO, "FiMastRecDM.getGlAccountsForCompany called with company ID {0} ", companyId);
    if(!trans.isActive()){
     trans.begin();
    }
    List<FiGlAccountCompRec> accounts = new ArrayList<>();
    try{
      //Query q = em.createNamedQuery("findAllGlAccounts");
      CompanyBasic comp = em.find(CompanyBasic.class, companyId);
      LOGGER.log(INFO, "Compay found {0}", comp.getId());
      Query q = em.createNamedQuery("GlCompAccountsAll");
      
      List acList = q.getResultList();
      if(acList == null || acList.isEmpty()){
       return null;
      }
      ListIterator acListLi = acList.listIterator();
      while(acListLi.hasNext()){
       Long acntId = (Long)acListLi.next();
       FiGlAccountComp ac = em.find(FiGlAccountComp.class, acntId, OPTIMISTIC);
       FiGlAccountCompRec rec = this.buildFiCompGlAccountRec(ac);
       accounts.add(rec);
      }
    }catch(IllegalArgumentException e){
     LOGGER.log(INFO, "Get comp acs err {0}", e.getLocalizedMessage());
      throw new BacException("query invalid "+e.getLocalizedMessage(),"FI_MAST:01");
    }catch(PersistenceException e){
     LOGGER.log(INFO, "Persistence exception {0}", e.getMessage());
    }finally{
     LOGGER.log(INFO, "in finally block");
     trans.rollback();
    }
    LOGGER.log(INFO, "getGlAccountsForCompany returns {0} accounts", accounts.size());
//    trans.commit();
    return accounts;
  }
  
  private  FiGlAccountComp copyCompAccount(FiGlAccountComp acnt1, CompanyBasic comp, 
          User usr, String pg){
   
   FiGlAccountComp acnt2 = new FiGlAccountComp();
   acnt2.setAccountClearing(acnt1.isAccountClearing());
   acnt2.setAnalysis1(acnt1.getAnalysis1());
   acnt2.setAnalysis2(acnt1.getAnalysis2());
   acnt2.setChangedBy(null);
   acnt2.setChangedOn(null);
   acnt2.setCoaAccount(acnt1.getCoaAccount());
   acnt2.setCompany(comp);
   acnt2.setCreatedBy(usr);
   acnt2.setCreatedOn(new Date());
   acnt2.setNoVatAllowed(acnt1.isNoVatAllowed());
   acnt2.setPaymentTypes(acnt1.getPaymentTypes());
   acnt2.setRepCategory(acnt1.getRepCategory());
   acnt2.setSortOrder(acnt1.getSortOrder());
   acnt2.setVatCode(acnt1.getVatCode());
   em.persist(acnt2);
   AuditGlCompAccount aud = this.buildAuditGlCompAccount(acnt2, usr, 'I', pg);
   aud.setFieldName("GL_AC_REF_C");
   aud.setNewValue(acnt1.getCompany().getNumber());
   
   return acnt2;
  }
  public int copyCompGlAccounts(CompanyBasicRec c1Rec,CompanyBasicRec c2Rec, 
          UserRec usrRec, String pg){
   LOGGER.log(INFO, "fiMastDM.copyCompGlAccounts called with comp {0} and {1}", new Object[]{
   c1Rec.getReference(),c2Rec.getReference()});
   if(!trans.isActive()){
    trans.begin();
   }
   TypedQuery q = em.createNamedQuery("GlAccountCompByCompId", FiGlAccountComp.class);
   q.setParameter("compId", c2Rec.getId());
   List<FiGlAccountComp> c2List = q.setMaxResults(1).getResultList();
   LOGGER.log(INFO, "c2List empty {0}", c2List.isEmpty());
   if(!c2List.isEmpty()){
    LOGGER.log(INFO, "Comp dest has GL accounts so copy not possible");
    return GlAccountManager.DEST_COMP_HAS_ACNT;
   }
   TypedQuery q2 = em.createNamedQuery("GlAccountCompByCompId", FiGlAccountComp.class);
   q2.setParameter("compId", c1Rec.getId());
   List<FiGlAccountComp> c1List = q2.getResultList();
   LOGGER.log(INFO, "Source company has {0} gl accounts", c1List.size());
   if(c1List.isEmpty()){
    return GlAccountManager.SRC_COMP_NO_ACNT;
   }
   User crUsr = em.find(User.class, usrRec.getId());
   CompanyBasic c2 = em.find(CompanyBasic.class, c2Rec.getId());
   boolean copiedOk = false;
   for(FiGlAccountComp curr:c1List){
    FiGlAccountComp newComp = this.copyCompAccount(curr, c2, crUsr, pg);
    if(newComp.getId() != null){
     copiedOk = true;
    }
   }
   LOGGER.log(INFO, "copiedOk {0}", copiedOk);
   if(copiedOk){
    trans.commit();
   }else{
    trans.rollback();
   }
   return GlAccountManager.PROCESS_OK;
  }
  
  public void createGlAccount(FiGlAccountBaseRec coaAccount, FiGlAccountCompRec compAccount, String pg)
          throws BacException {
    LOGGER.log(INFO, "Account class {0} pl{1} ", new Object[] {coaAccount.getClass(), coaAccount.isPl()});
    LOGGER.log(INFO,"createGlAccount compAc {0} for comp {1}",new Object[]{compAccount,compAccount.getCompany()});
    LOGGER.log(INFO,"createGlAccount compAc {0} for comp {1} id {2}", 
            new Object[]{compAccount,compAccount.getCompany(),compAccount.getCompany().getId()});
    if(!trans.isActive()){
     trans.begin();
    }
    FiGLAccountBase dbAct;
    if(coaAccount.getClass().getCanonicalName().equalsIgnoreCase("com.rationem.busRec.fi.glAccount.FiBsAccountRec")){
     // build BS account
     dbAct = buildBsAccount(coaAccount, pg);
    }else{
     dbAct = buildPlAccount(coaAccount, pg);
    }
    
    LOGGER.log(INFO,"Account built {0}",dbAct);
    FiGlAccountComp dbCompAct = this.buildFiCompGlAccount( compAccount, pg);
    dbCompAct.setCoaAccount(dbAct);
    LOGGER.log(INFO, "createGlAccount comp has COA account {0}", dbCompAct.getCoaAccount().getId());
    LOGGER.log(INFO, "createGlAccount comp has comp account {0}", dbCompAct.getCompany().getId());
   trans.commit();
  }

  /**
   * Update chart of accounts account
   * @param account
   * @throws BacException
   */
  public FiGlAccountBaseRec updateGlAccount(FiGlAccountBaseRec account,  String view) throws BacException {
    LOGGER.log(INFO, "DB updateGlAccount called with account {0}",
            account);
    if(!trans.isActive()){
     trans.begin();
    }
    FiGLAccountBase dbAct;
    if(account.getClass().getSimpleName().equalsIgnoreCase("FiPlAccountRec")){
     dbAct = this.buildPlAccount(account, view);
    
    }else{
     dbAct = this.buildBsAccount(account, view);
    }
    if(account.getId() == null){
     account.setId(dbAct.getId());
    }
    trans.commit();
    return account;
    
  }
/**
 * Build base class elements. This must be called after create of the sub-class
 * @param rec
 * @return 
 */
  private FiGLAccountBase buildFiGlAccountBase(FiGLAccountBase base, FiGlAccountBaseRec rec){
   LOGGER.log(INFO,"buildFiGlAccountBase called");
   if(rec.getAccountType() != null){
    AccountType actType = em.find(AccountType.class, rec.getAccountType().getId(), OPTIMISTIC);
    base.setAccountType(actType);
   }
   LOGGER.log(INFO, "Chart of accounts {0}", rec.getChartOfAccounts());
   if(rec.getChartOfAccounts() != null){
    ChartOfAccounts chart = em.find(ChartOfAccounts.class, rec.getChartOfAccounts().getId(), OPTIMISTIC) ; 
    base.setChartOfAccounts(chart);
   }
   if(rec.getCreatedBy()!= null){
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    base.setCreatedBy(crUsr);
    base.setCreated(rec.getCreatedOn());
   }
   base.setDescription(rec.getDescription());
   base.setName(rec.getName());
   NumberRange numCntrl = em.find(NumberRange.class, rec.getAccountType().getNumberRange().getNumberControlId());
    boolean autoNum = numCntrl.isAutoNum();
    LOGGER.log(INFO, "Account is auto number: {0}", autoNum);
    if(autoNum){
      int acRefNum;
      LOGGER.log(INFO, "Last number used: {0} from num: {1}",
              new Object[]{numCntrl.getNextNum(),numCntrl.getFromNum()});
      if(numCntrl.getNextNum() == 0){
        acRefNum = numCntrl.getFromNum();
        base.setRef(String.valueOf(acRefNum));
      } else{
        acRefNum = numCntrl.getNextNum();
        base.setRef(String.valueOf(acRefNum));
        acRefNum++;
        numCntrl.setNextNum(acRefNum);
      }
    }
   base.setRef(rec.getRef());
   if(rec.getUpdateBy() != null){
    User upDate = em.find(User.class, rec.getUpdateBy().getId(), OPTIMISTIC);
    base.setUpdateBy(upDate);
    base.setUpdateDate(rec.getUpdateOn());
   }
            
   return base;
  }
  private AuditGlAccount buildGlAccountCoaAudit(FiGLAccountBase act,User usr,Date date,
          char action,String field, Object dbValue, Object newVal){
   AuditGlAccount aud = new AuditGlAccount();
   aud.setAuditDate(date);
   aud.setGlAccount(act);
   aud.setCreatedBy(usr);
   aud.setFieldName(field);
   aud.setNewValue(newVal.toString());
   aud.setOldValue(dbValue.toString());
   aud.setUsrAction(action);

   return aud;
  }
  
  public List<FiGlAccountBaseRec> getAccountsByChart(ChartOfAccountsRec chart) {
   LOGGER.log(INFO, "Called getAccountsByChart with chart {0}", chart);
   Query q = em.createNamedQuery("accountsByChart");
   q.setParameter("chartId", chart.getId());
   List l = q.getResultList();
   ListIterator li = l.listIterator();
   List<FiGlAccountBaseRec> acList = new ArrayList<FiGlAccountBaseRec>();
   while(li.hasNext()){
    FiGLAccountBase ac = (FiGLAccountBase)li.next();
    FiGlAccountBaseRec rec ;
    if(ac.getClass().getCanonicalName().equalsIgnoreCase("com.rationem.entity.fi.glAccount.FiBsAccount")){
     rec = this.buildFiBsAccountRec((FiBsAccount)ac);
    }
    else{
     rec = this.buildPlAccountRec((FiPlAccount)ac);
    }
    //FiGlAccountBaseRec rec = this.buildFiGlAccountCoaRec(ac);
    acList.add(rec);
   }
   return acList;
  }

  /**
   * Retrieve from DB company accounts for the Chart of Accounts account passed in
   * @param coaAccount
   * @return
   * @throws BacException
   */
  public List<FiGlAccountCompRec> getCompanyAccountsForCoaAccount(FiGlAccountBaseRec coaAccount)
          throws BacException {
    LOGGER.log(INFO, "DB getCompanyAccountsForCoaAccount for account {0} passed in ", coaAccount.getRef());
    List<FiGlAccountCompRec> glCompanyAccounts = new ArrayList<FiGlAccountCompRec>();

    try{
      
      Query q = em.createNamedQuery("glCompAcsForCoaAcnt");
      q.setParameter("comActId", coaAccount.getId());
      List rs = q.getResultList();
      if(rs == null){
       LOGGER.log(INFO, "No comp acts");
      }
      for(Object obj: rs){
       FiGlAccountComp compAcnt = (FiGlAccountComp)obj;
       FiGlAccountCompRec compAcntRec = this.buildFiCompGlAccountRec(compAcnt);
       glCompanyAccounts.add(compAcntRec);
      }
      
      
      LOGGER.log(INFO, "Number of company accounts to return {0}", glCompanyAccounts.size());
      return glCompanyAccounts;
    }catch(IllegalArgumentException e){
      throw new BacException("GL:17 COA object error","GL:17");
    }
    
  }
/**
 * Deletes Chart of Accounts
 * @param coaAccount account to be deleted
 * @throws BacException/**
 * Deletes Chart of Accounts
 * @param coaAccount account to be deleted
 * @throws BacException
 */

  public void deleteCoaGLAccount(FiGlAccountBaseRec coaAccount, UserRec usr) throws BacException {
    LOGGER.log(INFO, "deleteCoaGLAccount called with ", coaAccount);
    FiGLAccountBase account = em.find(FiGLAccountBase.class,coaAccount.getId());
    try{
      em.remove(account);
      Date auditDt = new Date();
      User dbUsr = em.find(User.class, usr.getId());
      AuditGlAccount audit = this.buildGlAccountCoaAudit(account, dbUsr, auditDt , 'D', null, null, null);
      em.persist(audit);

    }catch(IllegalArgumentException ex){
      throw new BacException("GL:17 COA object error","GL:17");

    }catch(TransactionRequiredException ex){
      throw new BacException("GL:06 Delete COA  Account object error","GL:06");
    }catch(EntityNotFoundException ex){
      throw new BacException("GL:18 Delete COA  Account already deleted","GL:18");
    }catch(javax.ejb.EJBException ex){
      throw new BacException("GL:06 Delete COA  Account object error: "+ex.getLocalizedMessage());
    }


  }

  public FiGlAccountCompRec updateGlAccountComp(FiGlAccountCompRec compAct, String view) throws BacException {
    LOGGER.log(INFO,"DB updateGlAccountComp called with account {0}",
            new Object[] {compAct});
    if(!trans.isActive()){
     trans.begin();
    }
    FiGlAccountComp compAcntDb= this.buildFiCompGlAccount(compAct, view);
    if(compAct.getId() == null){
     compAct.setId(compAcntDb.getId());
    }
    trans.commit();
    return compAct;
  }

  public void deleteGlAccountComp(FiGlAccountCompRec account, UserRec usr) throws BacException {
    LOGGER.log(INFO, "DB deleteGlAccountComp called with ", account);
    try{
      FiGlAccountComp dbRec = em.find(FiGlAccountComp.class, account.getId());
      User dbUsr = em.find(User.class, usr.getId());
      if(dbRec != null){

       AuditGlCompAccount audit = this.buildGlCompAudit(dbRec, usr);
       this.auditChange(audit, 'D', null, null, null);
       em.remove(dbRec);
       em.persist(audit);
       LOGGER.log(INFO, "Save account deletion {0} ", dbRec);
        
       
      }
    }catch(IllegalArgumentException ex){
      throw new BacException("Could not find GL account to delete");
    }catch(TransactionRequiredException ex){
      throw new BacException("transaction missing");
    }
  }
/**
 * Adds a period balance to a company GL account
 * @param glCompAc
 * @param year
 * @param period
 * @throws BacException
 */
  public FiPeriodBalance addGlPeriodBal(FiGlAccountComp glCompAc, int year, int period)
          throws BacException {
    LOGGER.log(INFO, "addGlPeriodBal for company {0} year {1} period {2}",
            new Object[] {glCompAc.getId(),year,period});
    FiPeriodBalance periodBal = new FiPeriodBalance();
    periodBal.setCreated(new Date());
    periodBal.setFiGlAccountComp(glCompAc);
    periodBal.setBalYear(year);
    periodBal.setPeriod(period);
    try{
      em.persist(periodBal);
      return periodBal;
    }catch(EntityExistsException ex){
      throw new BacException("Period balance already exists");
    }catch(IllegalArgumentException ex){
      throw new BacException("Period balance Error not a DB entity");
    }catch(TransactionRequiredException ex){
      throw new BacException("Period balance Transaction Error");
    }
  }

  public List<Integer> getFiAcntFundBalsYrs(FiGlAccountCompRec acnt, FundRec fnd){
   
   List<Integer> years = new ArrayList<>();
   Query q = em.createNamedQuery("fiGlFndBalYrs");
   q.setParameter("acntId", acnt.getId());
   q.setParameter("fndId", fnd.getId());
   List<Object> rs = q.getResultList();
   LOGGER.log(INFO, "Gl acnt id {0} fund id {1} returns years {2}", new Object[]{
    acnt.getId(),fnd.getId(),rs});
   if(rs == null || rs.isEmpty()){
    return null;
   }
   for(Object o:rs){
    Integer curr = (Integer)o;
    years.add(curr);
   }
   LOGGER.log(INFO, "Years {0}", years);
   return years;
  }
  
  public List<FiPeriodBalanceRec> getFiBalForAcntFundYr(FiGlAccountCompRec acnt,FundRec fnd, int year){
   LOGGER.log(INFO, "DM getFiBalForAcntFundYr called with acnt id {0} fund id {1} year {2}", new Object[]{
    acnt.getId(),fnd.getId(),year });
   if(!trans.isActive()){
    trans.begin();
   }
   Query q = em.createNamedQuery("fiGlAcntFndBalsForYr");
   q.setParameter("acntId", acnt.getId());
   q.setParameter("fndId", fnd.getId());
   q.setParameter("yr", year);
   
   List<Object> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    LOGGER.log(INFO, "No fnd bals for year");
    return null;
   }else{
    List<FiPeriodBalanceRec> bals = new ArrayList<>();
    for(Object o:rs){
     FiPeriodBalanceRec curr = this.buildFiPeriodBalanceRec((FiPeriodBalance)o);
     bals.add(curr);
    }
    LOGGER.log(INFO, "Num fnd bals found {0}", bals.size());
    trans.commit();
    return bals;
   }
   
  }
  public List<FiPeriodBalanceRec> getFiBalForAccntYr(FiGlAccountCompRec acnt, int year, int balType){
   LOGGER.log(INFO, "FiBalsForAccntYr called for Account id {0} year {1} type code {3}", new Object[]{
    acnt.getId(),year, balType });
   Query q = em.createNamedQuery("fiGlAcntBalsForYr");
   q.setParameter("acntId", acnt.getId());
   q.setParameter("yr", year);
   q.setParameter("balTy", balType);
   try{
    List<Object> rs = q.getResultList();
    
    if(rs != null && !rs.isEmpty()){
     List<FiPeriodBalanceRec> balList = new ArrayList<>();
     for(Object o:rs){
      FiPeriodBalanceRec currRec = this.buildFiPeriodBalanceRec((FiPeriodBalance)o);
      balList.add(currRec);
     }
     int periods = acnt.getCompany().getPeriodRule().getNumPeriods();
     LOGGER.log(INFO, "Num periods {0}", periods);
     return balList;
    }else{
     return null;
    }
   }catch(PersistenceException ex){
    LOGGER.log(INFO, "Could not get balances for year reason {0}", ex.getLocalizedMessage());
   }
   
   
   return null;
  }
  public List<FiPeriodBalanceRec> getFiPerBalancesActYr(CompanyBasicRec comp, int fiscYear){
   LOGGER.log(INFO, "getFiPerBalActYr called for comp id {0} year {1}", new Object[]{
    comp.getId(),fiscYear });
   List<FiPeriodBalanceRec> balList = new ArrayList<>();
   Query q = em.createNamedQuery("fiGlActBalsForYr");
   q.setParameter("compId", comp.getId());
   q.setParameter("yr", fiscYear);
   List rs = q.getResultList();
   ListIterator rsLi = rs.listIterator();
   LOGGER.log(INFO, "Period balance query returned {0}", rs);
   while(rsLi.hasNext()){
    FiPeriodBalance curr = (FiPeriodBalance)rsLi.next();
    FiPeriodBalanceRec rec = this.buildFiPeriodBalanceRec(curr);
    balList.add(rec);
   }
   return balList;
  }
  public FiPeriodBalanceRec getFiPeriodBalanceRec(FiPeriodBalance b, CompanyBasicRec comp){
   FiPeriodBalanceRec bal = new FiPeriodBalanceRec();
   bal.setId(b.getId());
   UserRec crUser = this.userDM.getUserRecPvt(b.getCreatedBy());
   bal.setCreatedBy(crUser);
   bal.setCreated(b.getCreated());
   if(b.getUpdateBy() != null){
    UserRec upDtUsr = userDM.getUserRecPvt(b.getUpdateBy());
    bal.setUpdateBy(upDtUsr);
    bal.setUpdateDate(b.getUpdateDate());
   }
   bal.setBalPeriod(b.getBalPeriod());
   bal.setBalYear(b.getBalYear());
   bal.setBalanceType(b.getBalanceType());
   bal.setBfwdDocAmount(b.getBfwdDocAmount());
   bal.setBfwdLocalAmount(b.getBfwdLocalAmount());
   bal.setCfwdDocAmount(b.getCfwdDocAmount());
   bal.setCfwdLocalAmount(b.getCfwdLocalAmount());
   bal.setPeriodBudgetAmount(b.getPeriodBudgetAmount());
   bal.setPeriodCreditAmount(b.getPeriodCreditAmount());
   bal.setPeriodDebitAmount(b.getPeriodDebitAmount());
   bal.setPeriodDocAmount(b.getPeriodDocAmount());
   bal.setPeriodLocalAmount(b.getPeriodLocalAmount());
   if(b.getProgramBudgetAccount() != null){
    ProgramAccountRec prog = progDM.getProgramAccountRec(b.getProgramBudgetAccount());
    bal.setProgramBudgetAccount(prog);
   }
   if(b.getProgramCostAccount()!= null){
    ProgramAccountRec prog = progDM.getProgramAccountRec(b.getProgramCostAccount());
    bal.setProgramCostAccount(prog);
   }
   if(b.getRestrictedFund() != null){
    FundRec restr = this.compDM.getRestrictedFundRec(comp, b.getRestrictedFund());
    bal.setRestrictedFund(restr);
   }
   bal.setRevision(b.getRevision());
   
   return bal;
  }
  
  public List<FiGlAccountBaseRec> getGlCoaAcntsNotInComp(CompanyBasicRec compRec, 
    ChartOfAccountsRec chartRec){
   List<FiGlAccountBaseRec> retList = new ArrayList<>();
   Query q = em.createNamedQuery("glAcntsNotInComp");
   q.setParameter("chartId", chartRec.getId());
   q.setParameter("compId", compRec.getId());
   List rs = q.getResultList();
   if(rs != null){
    for(Object acnt:rs ){
     FiGLAccountBase curr = (FiGLAccountBase)acnt;
     FiGlAccountBaseRec currRec = this.FiGlAccountCoaRecPvt(curr);
     retList.add(currRec);
    }
   }
   LOGGER.log(INFO, "Chart acnts not in comp {0}", rs);
   return retList;
  }
  
  public List<FiGlAccountBaseRec> getGlCoaAcntsRefNotInComp(String glRef, CompanyBasicRec compRec, 
    ChartOfAccountsRec chartRec){
   List<FiGlAccountBaseRec> retList = new ArrayList<>();
   Query q = em.createNamedQuery("glAcntsRefNotInComp");
   q.setParameter("chartId", chartRec.getId());
   q.setParameter("compId", compRec.getId());
   q.setParameter("glRef", glRef);
   List rs = q.getResultList();
   if(rs != null){
    for(Object acnt:rs ){
     FiGLAccountBase curr = (FiGLAccountBase)acnt;
     FiGlAccountBaseRec currRec = this.FiGlAccountCoaRecPvt(curr);
     retList.add(currRec);
    }
   }
   LOGGER.log(INFO, "Chart acnts not in comp {0}", rs);
   return retList;
  }
  
  public FiGlAccountBaseRec getGlCoaAccountByActRef(String ref,ChartOfAccountsRec chart){
   FiGlAccountBaseRec coaAcRec;
   Query q = em.createNamedQuery("accountByChart");
   q.setParameter("chartId", chart.getId());
   q.setParameter("acntRef", ref);
   try{
    FiGLAccountBase coaAcnt = (FiGLAccountBase)q.getSingleResult();
    coaAcRec = this.buildFiGlAccountCoaRec(coaAcnt);
    return coaAcRec;
   }catch(NoResultException ex){
    LOGGER.log(INFO, "comp gl account not found ");
    return null;
   }
 }
  
 public List<Integer> getGlAccountBalYears(FiGlAccountCompRec acnt, int balType){
  LOGGER.log(INFO, "FimastDm.getGlAccountBudgetYears called with acnt id {0} ", acnt.getId());
  Query q = em.createNamedQuery("fiGlBalYrs");
  q.setParameter("acntId", acnt.getId());
  q.setParameter("balType", balType);
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  } else {
   List<Integer> yrs = new ArrayList<>();
   for(Object o:rs){
    Integer curr = (Integer)o;
    yrs.add(curr);
   }
   return yrs;
  }
  
 }
 public FiGlAccountBaseRec getGlAccountByActRef(String ref) throws BacException {

    Query q = em.createNamedQuery("findGlAccountByRef");
    try{
      q.setParameter("GlReference", ref);
      LOGGER.log(INFO, "set parameter GlReference to {0}", ref);
      List l = q.getResultList();
      ListIterator li = l.listIterator();
      while(li.hasNext()){
        FiGLAccountBase glActDb = (FiGLAccountBase)li.next();
        FiGlAccountBaseRec glActRec = this.buildFiGlAccountCoaRec(glActDb);
        return glActRec;
      }

    }catch(IllegalStateException ex){
      throw new BacException("Get GL account statement error");
    }catch(QueryTimeoutException ex){
      throw new BacException("Get GL account statementquery time out");
    }catch(TransactionRequiredException ex){
      throw new BacException("Get GL account statement transaction missing");
    }catch(PessimisticLockException ex){
      throw new BacException("Get GL account statement Could not set lock");
    }catch(LockTimeoutException ex){
      throw new BacException("Get GL account statement timeout setting lock");
    }catch(PersistenceException ex){
      throw new BacException("Get GL account statement other DB error");
    }

    return null;
  }

  public List<FiGlAccountCompRec> getDebtorReconAccounts(CompanyBasicRec comp) throws BacException {
    LOGGER.log(INFO, "FiMast DM called with company {0}", comp);
    List<FiGlAccountCompRec> recAcList = new ArrayList<>();
                
    try{
      AccountTypeRec acntTy = sysBuffer.getAcntTypeByProcCode("GL_ACNT_DRS_TR");
      
      TypedQuery q = em.createNamedQuery("accountsByType",FiGLAccountBase.class);
      q.setParameter("acntTyId", acntTy.getId());

      try{
        List<FiGLAccountBase> l = q.getResultList(); 
        
        ListIterator<FiGLAccountBase> li = l.listIterator();
        while(li.hasNext()){
          FiGLAccountBase actBase = li.next();
          FiBsAccount act = (FiBsAccount)actBase;
          
          List<FiGlAccountComp> compAcs = act.getGlAccountsComp();
          LOGGER.log(INFO, "compAcs {0}", compAcs.size());
          if(compAcs == null || compAcs.isEmpty()){
           continue;
          }
          ListIterator<FiGlAccountComp> compAcsLi = compAcs.listIterator();
          boolean compAcFound = false;
          while(compAcsLi.hasNext() && !compAcFound){
           FiGlAccountComp compAc = compAcsLi.next();
           CompanyBasic cmp = compAc.getCompany();
           LOGGER.log(INFO, "comp {0}", cmp);
           if(Objects.equals(compAc.getCompany().getId(), comp.getId())){
            FiGlAccountCompRec compAcRec = this.buildFiCompGlAccountRec(compAc);
            recAcList.add(compAcRec);
            compAcFound = true;
           }
          }
        }
        return recAcList;
      }catch(IllegalStateException e){
        throw new BacException("Account cat statement invalid");
      }catch(QueryTimeoutException e){
        throw new BacException("Account cat Query timeout");
      }catch(TransactionRequiredException e){
        throw new BacException("Account cat no transaction");
      }catch(PessimisticLockException e){
        throw new BacException("Account cat no lock");
      }catch(LockTimeoutException e){
        throw new BacException("Account cat lock timeout");
      }catch(PersistenceException e){
        throw new BacException("Account cat other DB error");
      }

    }catch(IllegalArgumentException e){
      throw new BacException("Get account by category not found");
    }
    
  }

private List<FiGlAccountCompRec> getAccountsByType(CompanyBasicRec comp, AccountTypeRec acntTy ){
 LOGGER.log(INFO, "FiMastDM.getAcciuntsByType called");
 List<FiGlAccountCompRec> recAcList = new ArrayList<>();
 Query q = em.createNamedQuery("accountsByType");
 q.setParameter("acntTyId", acntTy.getId());
 List l = q.getResultList();
 ListIterator li = l.listIterator();
 while(li.hasNext()){
  FiBsAccount act = (FiBsAccount)li.next();
  List<FiGlAccountComp> compAcs = act.getGlAccountsComp();
  ListIterator<FiGlAccountComp> compAcsLi = compAcs.listIterator();
  boolean compAcFound = false;
  while(compAcsLi.hasNext() && !compAcFound){
   FiGlAccountComp compAc = compAcsLi.next();
   if(Objects.equals(compAc.getCompany().getId(), comp.getId())){
    FiGlAccountCompRec compAcRec = this.buildFiCompGlAccountRec(compAc);
    recAcList.add(compAcRec);
    compAcFound = true;
   }
  }
 }
 return recAcList;
}   
public List<FiGlAccountCompRec> getCreditorReconAccounts(CompanyBasicRec comp) throws BacException {
 LOGGER.log(INFO, "FiMastDM.getCreditorReconAccounts called");
 AccountTypeRec acntTy = sysBuffer.getAcntTypeByProcCode("GL_ACNT_CRS_TR");
 
 List<FiGlAccountCompRec> recAcList = this.getAccountsByType(comp, acntTy);
 
 return recAcList;
 /*try{
  AccountTypeRec acntTy = sysBuffer.getAcntTypeByProcCode("GL_ACNT_CRS_TR");
  LOGGER.log(INFO, "CRS acntTy {0}", acntTy.getId());
  Query q = em.createNamedQuery("accountsByType");
    q.setParameter("acntTyId", acntTy.getId());

  try{
   List l = q.getResultList();
   ListIterator li = l.listIterator();
   while(li.hasNext()){
    FiBsAccount act = (FiBsAccount)li.next();
    LOGGER.log(INFO, "Account type {0}", act.getAccountType().getName());
    if(act.getAccountType().getName().equalsIgnoreCase("CL")){
     List<FiGlAccountComp> compAcs = act.getGlAccountsComp();
     ListIterator<FiGlAccountComp> compAcsLi = compAcs.listIterator();
     boolean compAcFound = false;
     while(compAcsLi.hasNext() && !compAcFound){
      FiGlAccountComp compAc = compAcsLi.next();
      if(compAc.getCompany().getId() == comp.getId()){
       FiGlAccountCompRec compAcRec = this.buildFiCompGlAccountRec(compAc);
       recAcList.add(compAcRec);
       compAcFound = true;
      }
     }
    }
   }
   return recAcList;
      }catch(IllegalStateException e){
        throw new BacException("Account cat statement invalid");
      }catch(QueryTimeoutException e){
        throw new BacException("Account cat Query timeout");
      }catch(TransactionRequiredException e){
        throw new BacException("Account cat no transaction");
      }catch(PessimisticLockException e){
        throw new BacException("Account cat no lock");
      }catch(LockTimeoutException e){
        throw new BacException("Account cat lock timeout");
      }catch(PersistenceException e){
        throw new BacException("Account cat other DB error");
      }

    }catch(IllegalArgumentException e){
      throw new BacException("Get account by category not found");
    }
   */ 
  }





    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
/**
 * Returns the VAT codes associated with a GL account.
 * @param glAccount
 * @return
 * @throws BacException 
 */
 public List<VatCodeRec> getGlAccountVatCodes(FiGlAccountCompRec glAccount) throws BacException {
  LOGGER.log(INFO, "getGlAccountVatCodes for glAccount {0}", glAccount.getId());
  if(glAccount == null || glAccount.getId() == null || glAccount.getId() < 1){
   throw new BacException("getGlAccountVatCodes - glAccount required {0}");
  }
  FiGlAccountComp glAc = em.find(FiGlAccountComp.class, glAccount.getId(), OPTIMISTIC);
  LOGGER.log(INFO, "GL account id found in DB {0} vat code {1}", new Object[]{glAc.getId(),glAc.getVatCode()});
  CompanyBasicRec comp = this.compDM.buildCompanyBasicRecPvt(glAc.getCompany());
  VatCodeCompany vatcdComp = glAc.getVatCode();
  LOGGER.log(INFO, "vatcdComp found {0}", vatcdComp);
  if(vatcdComp == null){
   // no comp vat code 
   return new ArrayList<VatCodeRec>();
  }
  VatCode vatCd = vatcdComp.getVatCode();
  List<VatCodeRec> vatCdList = new ArrayList<VatCodeRec>();
  if(!vatCd.isVatRule()){
   // only this VAT vat allowed
   VatCodeRec vatRec = vatDM.buildVatCodeRecPvt(vatCd);
   vatCdList.add(vatRec);
   
  }else{
   if(vatCd.isInputTax()){
    // need to get all input vat codes;
    vatCdList = vatDM.getVatCodesInput(comp);
   }else{
    vatCdList = vatDM.getVatCodesOutput(comp);
   }
  }
  
  return vatCdList;
 }
 public FiPeriodBalanceRec updatePeriodBalances(FiPeriodBalanceRec bal, String page){
  LOGGER.log(INFO, "FiMastRecDM.updatePeriodBalances called with bal yr {0} per {1} src {2}", 
     new Object[]{bal.getBalYear(),bal.getBalPeriod(),page});
  if(!trans.isActive()){
   trans.begin();
  }
  FiPeriodBalance balDB = buildFiPeriodBalance(bal, page);
  if(bal.getId() == null){
   bal.setId(balDB.getId());
   balDB.setFiGlAccountComp(balDB.getFiGlAccountComp());
   List<FiPeriodBalanceRec> glBals = bal.getFiGlAccountComp().getPeriodBalances();
   if(glBals == null){
    glBals = new ArrayList<>();
   }
   glBals.add(bal);
  }
  trans.commit();
  return bal;
 }
}
