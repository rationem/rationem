/*

* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package com.rationem.ejbBean.fi;

import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.util.ListIterator;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.ejbBean.dataManager.FiMasterRecordDM;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 * Manages GL activity
 * @author Chris
 */
@Stateless
@LocalBean
public class GlAccountManager {
  private static final Logger LOGGER =  Logger.getLogger(GlAccountManager.class.getName());

  @EJB
  private ConfigurationDM configDm;

  @EJB
  private CompanyManager compMgr;

  @EJB
  private FiMasterRecordDM fiMastDM;
  
  @EJB
  private SysBuffer sysBuff;
  
  public static final int DEST_COMP_HAS_ACNT = -1;
  public static final int SRC_COMP_NO_ACNT = -2;
  public static final int PROCESS_OK = 0;

  public FiGlAccountBaseRec getGlAccountBase() throws BacException {

    return new FiGlAccountBaseRec();
  }

  /**
   * Returns the GL account
   * @param ac
   * @return
   * @throws BacException
   */
  public FiGlAccountBaseRec getGlAccountCoaByRef(FiGlAccountBaseRec ac) throws BacException{
    LOGGER.log(INFO, "getGlAccountCoa called with ac {0} id {1} ref {2}",
            new Object[]{ac,ac.getId(),ac.getRef()});

    FiGlAccountBaseRec account = this.fiMastDM.getGlAccountByActRef(ac.getRef());

    return account;
  }
  
  public FiGlAccountBaseRec getGlAccountByRef(String ref) throws BacException{
    LOGGER.log(INFO, "getGlAccountCoa called with ref {0}",ref);

    FiGlAccountBaseRec account = this.fiMastDM.getGlAccountByActRef(ref);
    LOGGER.log(INFO, "fiMastDM ref returns account {0}", account);

    return account;
  }
  
  public FiGlAccountBaseRec getGlCoaAccountByRef(String ref, ChartOfAccountsRec chart){
   FiGlAccountBaseRec coaAcnt = this.fiMastDM.getGlCoaAccountByActRef(ref, chart);
   
   return coaAcnt;
  }

  public List<FiGlAccountBaseRec> getGlAccountsByRef(String ref){
   List<FiGlAccountBaseRec> retList = fiMastDM.getGlAccountsByRef(ref);
   return retList;
  }
  
  public List<FiGlAccountCompRec> getGlAccountsForBankUnclr(BankAccountCompanyRec bnk){
   LOGGER.log(INFO," glAcntMgr.getGlAccountsForBankUnclr called") ;
   
   List<FiGlAccountCompRec> retList = this.fiMastDM.getGlAccountsForBankUnclr(bnk);
   return retList;
   
  }
  
  public List<FiGlAccountBaseRec> getGlAccountsForChart(ChartOfAccountsRec ac) throws BacException{
  List<FiGlAccountBaseRec> acList = fiMastDM.getAccountsByChart(ac);
  return acList;
   
  }
  
  
  public FiPlAccountRec getPlAccount() throws BacException {
    return null;
  }

  public List<AccountTypeRec> getGlAccountTypes() throws BacException {

    return configDm.getAccountTypes();
  }

  public ArrayList<ChartOfAccountsRec> getCoaList() throws BacException {
     return compMgr.getChartsOfAccounts();

  }

 
  public List<FiGlAccountCompRec> glCompAcntFrCoaCreate(ChartOfAccountsRec coa, 
    CompanyBasicRec comp, UserRec usrRec, String pg){
   LOGGER.log(INFO, "glCompAcntFrCoaCreate called with coa {0} and Comp ref {1}", new Object[]{
    coa.getName(), comp.getReference() });
   
   List<FiGlAccountCompRec> retList = fiMastDM.getGlAccountsForCompany(comp.getId());
   if(retList != null && !retList.isEmpty()){
    LOGGER.log(INFO, "There already exist {0} accounts for Company", retList.size());
    return null;
   }
   retList = fiMastDM.glCompRecAcntsForCoaCreate(coa, comp, usrRec, pg);
   LOGGER.log(INFO, " Number company accounts created {0}", retList.size());
   return retList;
   
  }
   


  public void saveNewGlAccount(FiGlAccountBaseRec glaccount,String type, Long company, String pg) throws BacException {
    LOGGER.log(INFO, "saveNewGlAccount called with account {0} company {1}",
            new Object[] {glaccount,company});
    // Create chart of accounts Account
    try{
      fiMastDM.createGlAccountinChart(glaccount, type,pg);
    }catch(BacException ex){
      throw new BacException("Could not create GL account due to: "+ex.getLocalizedMessage());
    }


  }

  public void saveNewCompPlAccount(FiPlAccountRec glaccount,FiGlAccountCompRec compAct,String type,
          Long companyId, String pg) throws BacException {
    LOGGER.log(INFO, "saveNewGlAccount called with account {0} company Act {1} company {2}",
            new Object[] {glaccount,compAct,companyId});
    // Create chart of accounts Account
    try{
     LOGGER.log(INFO, "Company Sort order {0}", compAct.getSortOrder());
      fiMastDM.newCompPlAccount(glaccount, compAct,companyId,pg);

      LOGGER.log(INFO, "After create GLaccount");
    }catch(BacException ex){
      throw new BacException("Could not create GL account due to: "+ex.getLocalizedMessage());
    }


  }


/**
 * returns an array of GL accounts for the given company id
 * @param companyId
 * @return
 * @throws BacException
 */
  public List<FiGlAccountBaseRec> getGlAccountsForCompany(Long companyId) throws BacException {
    LOGGER.log(INFO, "GL Ac Mgr getGlAccountsForCompany called with comp id{0} ",
            new Object[] {companyId});
    List<FiGlAccountCompRec> compAccounts = fiMastDM.getGlAccountsForCompany(companyId);
    LOGGER.log(INFO, "compAccounts found {0}",compAccounts);
    if(compAccounts ==  null || compAccounts.isEmpty()){
     return null;
    }
    LOGGER.log(INFO, "fiMastDM.getGlAccountsForCompany for company {0} found {1} accounts",
            new Object[] {companyId,compAccounts.size()});
    ListIterator li = compAccounts.listIterator();
    List<FiGlAccountBaseRec> accounts = new ArrayList<>();
    while(li.hasNext()){
      FiGlAccountCompRec compAc = (FiGlAccountCompRec)li.next();
      LOGGER.log(INFO, "compAc {0}", compAc);
      FiGlAccountBaseRec ac = compAc.getCoaAccount();
      LOGGER.log(INFO, "compAc {0}", ac);
      accounts.add(ac);
    }
    LOGGER.log(INFO, "Number of GL accounts for company found {0}", accounts.size());
    return accounts;
  }

  public List<Integer> getGlAccountBalanceYears(FiGlAccountCompRec acnt, int balType){
   return this.fiMastDM.getGlAccountBalYears(acnt,balType);
  }
  
  public List<FiPeriodBalanceRec> getFiBalForAccntYr(FiGlAccountCompRec acnt, int year, int balType){
   List<FiPeriodBalanceRec> bals = fiMastDM.getFiBalForAccntYr(acnt, year, balType);
   return bals;
  }
  
  public List<FiPeriodBalanceRec> getFiBalForAcntFundYr(FiGlAccountCompRec acnt,FundRec fnd, int year){
   List<FiPeriodBalanceRec> bals = fiMastDM.getFiBalForAcntFundYr(acnt, fnd, year);
   LOGGER.log(INFO, "Data layer returns {0}", bals);
   return bals;
  }
  public List<Integer> getFiAcntFundBalsYrs(FiGlAccountCompRec acnt, FundRec fnd){
   List<Integer> years = fiMastDM.getFiAcntFundBalsYrs(acnt, fnd);
   LOGGER.log(INFO, "years from DM {0}", fnd);
   return years;
  }
  public List<FiGlAccountCompRec> getCompanyAccounts(CompanyBasicRec comp){
   LOGGER.log(INFO, "glAcntMgr.getCompanyAccounts called with comp{0}", comp.getId());
   List<FiGlAccountCompRec> compAccounts = fiMastDM.getGlAccountsForCompany(comp.getId());
   Collections.sort(compAccounts, new FiGlAccountCompByRef());
   return compAccounts;
  }
  
  public List<FiGlAccountCompRec> getCompanyAcntsByAcntTy(CompanyBasicRec comp, AccountTypeRec acntTy){
   List<FiGlAccountCompRec> retList = fiMastDM.getGlAcntsByTypeForComp(comp, acntTy);
   
   Collections.sort(retList, new FiGlAccountCompByRef());
   return retList;
  }
  public List<FiGlAccountCompRec> getCompanyAccounts(FiGlAccountBaseRec account){
   LOGGER.log(INFO,"getCompanyAccounts called with base account {0}",account.getRef());
   List<FiGlAccountCompRec> compAcs = fiMastDM.getCompanyAccountsForCoaAccount(account);
   LOGGER.log(INFO,"Company acs from DB layer {0}",compAcs);
   return compAcs;
  }
  
  public int copyGenLedAcntsComp(CompanyBasicRec c1, CompanyBasicRec c2, UserRec usr, String pg){
   LOGGER.log(INFO, "compMgr.copyGenLedAcntsComp called with comp ref {0}  ref {1}", 
           new Object[]{c1.getReference(),c2.getReference()});
   return fiMastDM.copyCompGlAccounts(c1, c2, usr, pg);
  }
  public void createPlAccount(FiGlAccountBaseRec account,FiGlAccountCompRec compAc, String pg) throws BacException{
   LOGGER.log(INFO,"GlAccountManager.createPlAccount called with account {0} type {1} compAc {2}",
           new Object[]{account,account.getClass().getCanonicalName(),compAc});
   if(!account.getClass().getCanonicalName().equalsIgnoreCase("com.rationem.busRec.fi.glAccount.FiPlAccountRec")){
    throw new BacException("Wrong account type called");
   }
   FiPlAccountRec ac = (FiPlAccountRec)account;
   fiMastDM.createGlAccount(ac, compAc,pg);
  }
  
  public FiGlAccountBaseRec createCoaGlAccount(FiGlAccountBaseRec account, String pg){
    this.fiMastDM.createGlAccountinChart(account, pg, pg);
   return account;
  }
  public void createBsAccount(FiGlAccountBaseRec bsAccount,FiGlAccountCompRec compAc,boolean pl, String pg ) {
    LOGGER.log(INFO, "GL Account Mananger create BS Account called with Ac ref {0} class {1} compRec {2}",
            new Object[] { bsAccount.getRef(), bsAccount.getClass(),compAc});
    if(!pl){
      FiBsAccountRec account = (FiBsAccountRec)bsAccount;
      LOGGER.log(INFO, "account is {0}",account);
      this.fiMastDM.createGlAccount(bsAccount, compAc,pg);

  }

  }

  public FiGlAccountBaseRec updateGlAccountCoa(FiGlAccountBaseRec account, UserRec usr, String view) throws BacException {
    LOGGER.log(INFO, "updateGlAccountCoa called with {0} class {1}", new Object[] {account,account.getClass()});
    
    account = this.fiMastDM.updateGlAccount(account, view);
    
    return account;
    
  }
/**
 * Deletes GL at chart of account level - any company GL accounts must have already been deleted
 * @param account Business Record representing a GL account PL or Balance Sheet
 * @param usr Business Record of user performing the action
 * @throws BacException
 */
  public void deleteGlAccountCoa(FiGlAccountBaseRec account, UserRec usr) throws BacException {
    LOGGER.log(INFO, "GL Act mgr deleteGlAccountCoa called with account {0} user {1}",
            new Object[] {account,usr});
    boolean hasCompanyAccounts = false;
    List<FiGlAccountCompRec> compAccounts = this.fiMastDM.getCompanyAccountsForCoaAccount(account);
    if(compAccounts != null && !compAccounts.isEmpty()){
     hasCompanyAccounts = true;
     return;
    }
    LOGGER.log(INFO, "GLMgr deleteGlAccountCoa hasCompanyAccounts {0} num company Accounts {1}" ,
            new Object[] {hasCompanyAccounts,compAccounts.size()});
    if(hasCompanyAccounts){
      throw new BacException("GLmgr:02 Delete GL account error - company accounts exist","GlMgr:02");
    }else{
      try{
        fiMastDM.deleteCoaGLAccount(account,usr);
      }catch(BacException evt){
        throw new BacException("GLmgr:03 Delete GL account DB error - "+evt.getLocalizedMessage(),"GlMgr:03");
      }

    }

  

  }

  public FiGlAccountCompRec updateGlAccountComp(FiGlAccountCompRec account, String view) throws BacException {
    LOGGER.log(INFO, "GL MgrupdateGlAccountComp called with account {0}  ",
            new Object[]{account});
    
    try{
     account = fiMastDM.updateGlAccountComp(account, view);
     return account;
      
    }catch(BacException evt){
      throw new BacException("GLmgr:04 Update company GL account DB error - "+evt.getLocalizedMessage(),"GlMgr:04");
    }
  }

  public FiGlAccountCompRec getGlAccountForComp(String glAccountRef, CompanyBasicRec comp){
   LOGGER.log(INFO, "getGlAccountForComp called with accnt {0} company {1}", new Object[]{
    glAccountRef,comp.getReference()});
   
   FiGlAccountCompRec compAc = this.fiMastDM.getFiCompGlAccountRec(glAccountRef, comp);
   LOGGER.log(INFO, "fiMastDM returns compAcnt {0}", compAc);
   
   return compAc;
  }
  
  public List<FiGlAccountBaseRec> getGlCoaAcntsNotInComp(CompanyBasicRec compRec,
    ChartOfAccountsRec chartRec){
   List<FiGlAccountBaseRec> retList = fiMastDM.getGlCoaAcntsNotInComp(compRec, chartRec);
   return retList;
  }
  
  public List<FiGlAccountBaseRec> getGlCoaAcntsRefNotInComp(String glRef, CompanyBasicRec compRec,
    ChartOfAccountsRec chartRec){
   List<FiGlAccountBaseRec> retList = fiMastDM.getGlCoaAcntsRefNotInComp(glRef, compRec, chartRec);
   return retList;
  }
  
  public FiGlAccountCompRec getCompanyAcctForCoaAccount(FiGlAccountBaseRec coaAccount,
          CompanyBasicRec company) throws BacException {
    LOGGER.log(INFO, "getCompanyAcctForCoaAccount called with coaAccount {0} company {1}",
            new Object[] {coaAccount.getRef(),company});
    FiGlAccountCompRec compAc = null;
    try{
      List<FiGlAccountCompRec> compAcList = fiMastDM.getCompanyAccountsForCoaAccount(coaAccount);
      boolean found = false;
      ListIterator li = compAcList.listIterator();
      while(li.hasNext() && !found){
        FiGlAccountCompRec rec = (FiGlAccountCompRec)li.next();
        if(Objects.equals(rec.getCompany().getId(), company.getId())){
          LOGGER.log(INFO, "Found company GL account: {0}", rec.getCompany().getReference());
          compAc = rec;
          found = true;
        }
      }
      if(!found){
        throw new BacException("Company rec not found for company");
      }
    }catch(BacException ex){
      throw new BacException(ex.getLocalizedMessage());
    }
    return compAc;
  }

  public List<FiGlAccountCompRec> getGlCompRecAcntsForCoaAcnt(FiGlAccountBaseRec coaAcntRec ){
   List<FiGlAccountCompRec> compAcnts = this.fiMastDM.getCompanyAccountsForCoaAccount(coaAcntRec);
   return compAcnts;
  }
  public void deleteGlAccountComp(FiGlAccountCompRec account, UserRec usr) throws BacException {
    LOGGER.log(INFO,"deleteGlAccountComp called with {0}",account);
    try{
      this.fiMastDM.deleteGlAccountComp(account, usr );
    }catch(BacException ex){
      throw new BacException("Could not delete account");
    }
  }

  public List<FiGlAccountCompRec> getDebtorReconciliationAcs(CompanyBasicRec comp) throws BacException {
    LOGGER.log(INFO, "getDebtorReconciliationAcs() called");
    List<FiGlAccountCompRec> acList = this.fiMastDM.getDebtorReconAccounts(comp);
    LOGGER.log(INFO, "accounts returned from fiMastDM {0}",fiMastDM);

    return acList;
  }
  
  public List<FiGlAccountCompRec> getCreditorReconciliationAcs(CompanyBasicRec comp) throws BacException {
    LOGGER.log(INFO, "getCreditorReconciliationAcs() called");
    List<FiGlAccountCompRec> acList = fiMastDM.getCreditorReconAccounts(comp);
    LOGGER.log(INFO, "Accounts returned from fiMastDM {0}",acList);

    return acList;
  }





    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<VatCodeRec> getGlAccountVatCodes(FiGlAccountCompRec glAc) {
  LOGGER.log(INFO, "getGlAccountVatCodes called for ac {0}", glAc);
  List<VatCodeRec> glAcVatCodes = this.fiMastDM.getGlAccountVatCodes(glAc);
  LOGGER.log(INFO, "glAcVatCodes returned from DB layer {0}", glAcVatCodes);
  return glAcVatCodes;
 }

 public List<FiPeriodBalanceRec> getPeriodBalancesAct(CompanyBasicRec comp, int fiscYear){
  
  List<FiPeriodBalanceRec> balList = this.fiMastDM.getFiPerBalancesActYr(comp, fiscYear);
  LOGGER.log(INFO, "Period balances returned from DB layer {0}", balList);
  return balList;
 }
public FiPeriodBalanceRec updatePeriodBalances(FiPeriodBalanceRec bal, String page){
 LOGGER.log(INFO, "GlAcntMgr called with bal id {0}", bal.getId());
  bal = this.fiMastDM.updatePeriodBalances(bal, page);
  LOGGER.log(INFO, "GlAcntMgr returns bal id {0}", bal.getId());
  return bal;
}

public FiGlAccountCompRec updateGlAcntBugets(FiGlAccountCompRec acnt, String page){
 List<FiPeriodBalanceRec> bals = acnt.getPeriodBalances();
 if(bals != null && !bals.isEmpty()){
  for(FiPeriodBalanceRec bal:bals){
   bal = this.fiMastDM.updatePeriodBalances(bal, page);
  }
 }
 return acnt;
}
public FiGlAccountCompRec updateGlAcntFndBudgets(FiGlAccountCompRec acnt, String page){
 List<FiPeriodBalanceRec> bals = acnt.getRestrictedBalances();
 if(bals != null && !bals.isEmpty()){
  for(FiPeriodBalanceRec bal:bals){
   bal = this.fiMastDM.updatePeriodBalances(bal, page);
  }
 }
 return acnt;
}

 
}
