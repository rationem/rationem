/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.company;

import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.busRec.config.company.CalendarRuleBaseRec;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;

import javax.ejb.EJBTransactionRolledbackException;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.ejbBean.dataManager.CompanyDM;
//import com.rationem.ejbBean.dataManager.ConfigurationDM;
import javax.ejb.EJB;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.exception.BacException;
import javax.persistence.PersistenceException;
import java.util.ListIterator;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import javax.persistence.EntityManager;
import com.rationem.entity.config.company.AccountType;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.CalendarRuleFlexPerRec;
import com.rationem.busRec.config.company.CalendarRuleFlexYearRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyApArRec;
import com.rationem.busRec.fi.company.PeriodControlRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.dataManager.VatDM;
import com.rationem.helper.comparitor.CompanyByRef;
import com.rationem.helper.comparitor.VatRegByStartDateRev;
import java.util.Collections;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class CompanyManager {
 private static final Logger LOGGER = Logger.getLogger(CompanyManager.class.getName());
 public static final int ATTR_IN_DEST_COMP = -1;
 public static final int ATTR_NOT_IN_SRC_COMP = -2;
 public static final int ATTR_CREATED = 1;
 public static final int ATTR_NOT_CREATED = -4;
 public static final int PER_RULE_UPDATED = 0;
 public static final int PER_RULE_UPDATE_FAIL = -3;
 
 
 @EJB
    private CompanyDM compDM;

    @EJB
    private ConfigurationDM configDM;
    
    @EJB
    private VatDM vatDM;

    @EJB
    private SysBuffer sysBuff;

    private AccountTypeRec accountType;

    private LedgerRec ledger;

    private CalendarRuleBaseRec fisCalendar;
    private CompanyBasicRec company;
    
    private ArrayList<AccountTypeRec> accountTypes;
    private ArrayList<FisPeriodRuleRec> periodRules;
    private ArrayList<PostTypeRec> postTypes;


    private ArrayList<ChartOfAccountsRec> chartsOfAccounts;

    @PersistenceContext 
    private EntityManager em;

    public AccountTypeRec getAccountType() {
        LOGGER.log(INFO, "getAccountType() accountType is {0}:  ", accountType);
        if(accountType == null){
            accountType = new AccountTypeRec();
        }
        LOGGER.log(INFO, "getAccountType() accountType returned is {0}:  ", accountType);
        return accountType;
    }
    
    
    

   

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public AccountTypeRec getAccountTypeByRef(String reference) {
        
        return null;
    }
 
 public void coaAddComp(CompanyBasicRec c1, CompanyBasicRec c2, UserRec usr, String pg){
  LOGGER.log(INFO, "Company Manager coaAddComp called with comp {0} and comp {1}", 
          new Object[]{c1.getReference(), c2.getReference()});
  compDM.coaAddComp(c1, c2, usr, pg);
 }
    public ChartOfAccountsRec coaUpdate(ChartOfAccountsRec rec, UserRec usr, String pg){
     rec = this.compDM.coaUpdate(rec, usr, pg);
     return rec;
    }
    
    public boolean companyRefValid(String compRef){
     boolean refValid = this.compDM.companyRefValid(compRef);
     
     return refValid;
    }
    
    
    public AccountTypeRec createAccountType(AccountTypeRec accountType, String pg) {
     LOGGER.log(INFO, "createAccountType called wirh: {0}", accountType);
     
      accountType = this.compDM.createAccountType(accountType, pg);
      LOGGER.log(INFO, "DB entity returned: {0}",accountType);
      accountType = this.sysBuff.accountTypeUpdate(accountType,pg);
      return   accountType;    
        
    }


    
    
    
    private AccountType buildAccountTypeEntity(AccountTypeRec a){
        LOGGER.log(INFO, "buildAccountTypeEntity called with: {0}", a);
       AccountType actType = new AccountType();
       actType.setChangedDate(a.getChangedDate());
       actType.setControlAccount(a.isControlAccount());
       actType.setCreatedDate(a.getCreatedDate());
       actType.setDescription(a.getDescription());
       actType.setName(a.getName());
       actType.setProfitAndLossAccount(a.isProfitAndLossAccount());
       actType.setSystemPost(a.isSystemPost());
       return actType;
    }
    
    private AccountTypeRec buildAccountTypeRec(AccountType a){
        LOGGER.log(INFO, "buildAccountTypeRec called with: {0}", a.getId());
       AccountTypeRec actType = new AccountTypeRec();
       actType.setId(a.getId());
       actType.setChangedDate(a.getChangedDate());
       actType.setControlAccount(a.isControlAccount());
       actType.setCreatedDate(a.getCreatedDate());
       actType.setDescription(a.getDescription());
       actType.setName(a.getName());
       actType.setProfitAndLossAccount(a.isProfitAndLossAccount());
       actType.setSystemPost(a.isSystemPost());
       return actType;
    }



    public ArrayList<AccountTypeRec> getAccountTypes() {
        LOGGER.log(INFO, "getAccountTypes");
        if( accountTypes != null && accountTypes.size() > 0){
            return accountTypes;
        }
       try{ 
       Query acTypeQ = em.createNamedQuery("allaccountTypes");
       
       List acTypeL = acTypeQ.getResultList();
     
       ListIterator it =  acTypeL.listIterator();
       while(it.hasNext()){
           AccountType a = (AccountType)it.next();
           AccountTypeRec rec = this.buildAccountTypeRec(a);
           LOGGER.log(INFO, "account rec returned is: {0}", rec.getName());
           if(accountTypes == null){
               
               accountTypes = new ArrayList<>();
           }
           accountTypes.add(rec);
       }
       }catch(IllegalArgumentException | IllegalStateException | PersistenceException e){
           LOGGER.log(INFO, "get account types error: {0}", e.getMessage());
       }
       
       
       
       if(accountType == null){
           accountTypes = new ArrayList<>();
       }
     LOGGER.log(INFO, "Number of account types to return: {0}", accountTypes.size());
        return accountTypes;
    }

    public ArrayList<FisPeriodRuleRec> getPeriodRules() {
      LOGGER.log(INFO, "Comp Mgr getPeriodRules() called periodRules {0} ",periodRules );
        if(periodRules == null || periodRules.isEmpty()){
            periodRules = compDM.getFisPeriodRules();
        }
      LOGGER.log(INFO, "Period rules returned {0}",periodRules);
        return periodRules;
    }

    public void setPeriodRules(ArrayList<FisPeriodRuleRec> periodRules) {
        this.periodRules = periodRules;
    }




    public AccountTypeRec updateAccountType(AccountTypeRec rec, String pg) {
       
     rec = this.compDM.accountTypeUpdate(rec, pg);
     rec = this.sysBuff.accountTypeUpdate(rec, pg);
        
        return rec;
    }



    public boolean deleteAccountType(AccountTypeRec rec) {
        LOGGER.log(INFO, "deleteAccountType called with: {0}", rec);
        boolean status = false;
        try{
        AccountType acType = em.find(AccountType.class, rec.getId());
        LOGGER.log(INFO, "delete db is: {0}", acType.getId());
        em.remove(acType);
        status = true;
        }catch(IllegalArgumentException e){
          LOGGER.log(INFO, "Delete error for account type : {0}", e.getMessage());              
        }catch(TransactionRequiredException e){
           LOGGER.log(INFO, "Delete error for account type :{0} ", e.getMessage()); 
        }
        
        return status;
        
 
    }
 public int fisPeriodRuleCopyComp(CompanyBasicRec c1,CompanyBasicRec c2, 
         UserRec usr, String pg){
  LOGGER.log(INFO, "fisPeriodRuleCopyComp called with comp {0} and {1}", new Object[]{
   c1.getReference(),c2.getReference() });
  if(c2.getPeriodRule() != null){
   return CompanyManager.ATTR_IN_DEST_COMP;
  }
   return compDM.fisPeriodRuleCopyComp(c1, c2, usr,pg);
  
  
 }
 
 public int fundCopyComp(CompanyBasicRec c1,CompanyBasicRec c2, 
         UserRec usr, String pg){
  LOGGER.log(INFO, "CompMgr.fundCopyComp called with Comp {0} and {1}", new Object[]{
   c1.getReference(),c2.getReference()});
  return compDM.fundCopyComp(c1, c2, usr, pg);
 }
/**
 * Get a new empty Ledger
 * @return LedgerRec
 */
    public LedgerRec getLedger() {
        LedgerRec l = new LedgerRec();
        return l;
    }

    public ChartOfAccountsRec getChartOfAccount() throws BacException {
        ChartOfAccountsRec coa = new ChartOfAccountsRec();
        return coa;
    }

    public ChartOfAccountsRec getChartOfAccountByRef(String ref) throws BacException {
     ChartOfAccountsRec coa = this.compDM.getChartOfAccountsByRef(ref);
     LOGGER.log(INFO, "Comp Mgr to return coa {0}", coa);
     return coa;
    }
    public void newChartOfAccounts(ChartOfAccountsRec coa, String pg) throws BacException {
     LOGGER.log(INFO, "newChartOfAccounts called with ", coa.getReference());
     try{
      compDM.saveChartOfAccounts(coa,pg);
      LOGGER.log(INFO, "Company manager saved COA");
     }catch(BacException e){
      throw new BacException(e.getLocalizedMessage());
     }


    }

    public FisPeriodRuleRec getFiscalPeriodRule() throws BacException {

        return new FisPeriodRuleRec();
    }

    public void saveFisPeriod(FisPeriodRuleRec rec, String pg) throws BacException{
     LOGGER.log(INFO, "Called with {0}",rec);
     LOGGER.log(INFO, "Calendar basis {0}", rec.isCalendarMonthBasis());
     LOGGER.log(INFO, "Fixed day basis {0}", rec.isFixedDayOfMonthBasis());
     LOGGER.log(INFO, "Flexible basis {0}", rec.isAnnualDateScheduleBasis());
     try{
      rec = compDM.createFiscalPeriod(rec,pg);
      
      this.sysBuff.fiscalPerRuleAdd(rec);
      
     }catch(BacException e){
      LOGGER.log(INFO, "saveFisPerBasic caught save error");
      throw new BacException("Could not save Fiscal Period rule");
     }catch(EJBTransactionRolledbackException e){
      throw new BacException("Could not save Fiscal Period rule - duplicate rule id");
     }
    }
/**
 * Get base calendar rule. This will return all calendar rules
 * @return
 * @throws BacException
 */
    public CalendarRuleBaseRec getFiscalCalendar() throws BacException {
        LOGGER.log(INFO, "getFiscalCalendar()");
        return new CalendarRuleBaseRec();
    }

    public CalendarRuleMonthRec getFiscalCalendarMonth() throws BacException {
        return new CalendarRuleMonthRec();
    }

/*    public CalendarRulePeriodLenRec getFiscalCalendarPeriodLen() throws BacException {
        return null;
    }
*/
    public void createFisCal(CalendarRuleBaseRec rec, String type, String pg) throws BacException {
        this.compDM.saveFisCalRule(rec, type, pg);
    }

    public void createFiscalCalendarRuleMonth(CalendarRuleMonthRec rec, String pg) throws BacException {
        LOGGER.log(INFO, "createFiscalCalendarRuleMonth");

            this.compDM.createFiscalCalendarRuleMonth(rec, pg);

    }

    public PostTypeRec createPostType(PostTypeRec pt, UserRec crUsr, String view )throws BacException{
     LOGGER.log(INFO,"createPostType called with {0}",pt.getPostTypeCode());
      //pt = this.configDM.createPostType(pt, view);
      pt = sysBuff.updatePostType(pt,view);
     return pt;
    }
    
    public FisPeriodRuleRec getPeriodRuleById(long id) throws BacException {
        LOGGER.log(INFO,"getPeriodRuleById called with {0}",id);
        FisPeriodRuleRec rec = this.compDM.getPeriodRuleById(id);
        return rec;
    }
/**
 * Returns an array list of the type of calendar rule based on the basis option.
 * @param calBasisOption 1= Calendar, 2 = Fixed day of month, 3 = Period length, 4 = Calendar
 * @return
 * @throws BacException
 */
   /* public ArrayList<CalendarRuleBaseRec> getCalRuleByTypeList(int calBasisOption) throws BacException {
        LOGGER.log(INFO, "Comp manager getCalRuleList called with option {0}", calBasisOption);

         return   this.compDM.getCalendarRulesByType(calBasisOption);

        
    }
*/
    /**
     * returns all calendar rules
     * @return
     * @throws BacException
     */
    public ArrayList<CalendarRuleBaseRec> getAllCalendarRulesList() throws BacException {
        return null;
    }

    public FisPeriodRuleRec updateFiscalCalendar(FisPeriodRuleRec periodRule, String src) throws BacException {
     LOGGER.log(INFO, "updateFiscalCalendar called with {0}", periodRule);
     periodRule = this.compDM.updatePeriodRule(periodRule,src);
     LOGGER.log(INFO, "periodRule id from compMgr {0}", periodRule.getId());
     return periodRule;
    }



    public CalendarRuleBaseRec getCalendarRuleById(int id) throws BacException {

        return null;
    }
    
    public CalendarRuleBaseRec getCalendarRuleByRef(String ref) throws BacException{
     LOGGER.log(INFO, "compMgr.getCalendarRuleByRef called with {0}",ref);
     return this.compDM.getCalendarRuleByRef(ref);
    }
    
    public CalendarRuleFlexYearRec getCalendarRuleFlexYear(String rule, int year) throws BacException{
      CalendarRuleFlexYearRec yrRule  = compDM.getCalendarRuleFlexYearRec(rule, year);
      LOGGER.log(INFO, "compDM returns year rule {0}", yrRule);
     return yrRule;
    }
/**
 * Returns of client layer a list of charts of accounts
 * @return
 * @throws BacException
 */
    public ArrayList<ChartOfAccountsRec> getChartsOfAccounts() throws BacException {
        LOGGER.log(INFO, "Company Mgr getChartsOfAccounts ");
        ArrayList<ChartOfAccountsRec> charLst = this.compDM.getChartsOfAccounts();
        ListIterator li = charLst.listIterator();
            while(li.hasNext()){
               ChartOfAccountsRec c = (ChartOfAccountsRec)li.next();
               LOGGER.log(INFO, "CompMgr chart id {0}", c.getId());
               LOGGER.log(INFO, "CompMgr chart ref {0}", c.getReference());
            }
        LOGGER.log(INFO, "returns getChartsOfAccounts {0}", charLst.size());
        return charLst;
    }

    public void setChartsOfAccounts(ArrayList<ChartOfAccountsRec> chartsOfAccounts) {
        this.chartsOfAccounts = chartsOfAccounts;
    }

    public CompanyApArRec updateArApRec(CompanyApArRec compArAp, String pg){
     compArAp = compDM.updateCompArApRec(compArAp, pg);
     return compArAp;
    }
    public CalendarRuleBaseRec updateCalendarRuleBaseRec(CalendarRuleBaseRec rec, String src){
     
     rec = this.compDM.updateCalendarRule(rec, src);
     
     return rec;
    }
    
    public CalendarRuleFlexPerRec updateCalendarRuleFlexPeriodRec(CalendarRuleFlexPerRec rec,
            String src){
     rec = compDM.updateCalendarRuleFlexPeriod(rec, src);
     LOGGER.log(INFO, "updateCalendarRuleFlexYearRec return from compDM id {0}", rec.getId());
     return rec;
    }
    public CalendarRuleFlexYearRec updateCalendarRuleFlexYearRec(CalendarRuleFlexYearRec rec,
            String src){
     rec = compDM.updateCalendarRuleFlexYear(rec, src);
     LOGGER.log(INFO, "updateCalendarRuleFlexYearRec return from compDM id {0}", rec.getId());
     return rec;
    }
    /**
     * Update an existing chart of accounts
     * @param rec
     * @throws BacException
     */
    public void updateChartOfAccounts(ChartOfAccountsRec rec) throws BacException {
        LOGGER.log(INFO, "updateChartOfAccounts");
        try{
            this.compDM.updateChartOfAccounts(rec);
        }catch(BacException e){
            LOGGER.log(INFO,"Database update threw error: {0}",e.getLocalizedMessage());
            throw new BacException(e.getLocalizedMessage());
        }
    }

    public CompanyBasicRec getCompanyRec() throws BacException {

        return new CompanyBasicRec();
    }
public CompPostPerRec getCompPostPerByCompLed(CompanyBasicRec comp, LedgerRec led){
 
 CompPostPerRec rec = this.compDM.getCompPostPerByCompLed(comp, led);
 return rec;
 
}
/**
 * Creates a company based on the values passed from UI
 * @param rec
  * @param usr
  * @param pg
  * @return 
 * @throws BacException
 */
    public CompanyBasicRec createCompany(CompanyBasicRec rec, UserRec usr, String pg) throws BacException {
        LOGGER.log(INFO, "Comp Mgr createCompany");
        LOGGER.log(INFO, "Company Type {0} fiscal rule {1}",
                new Object[] {rec.getCompanyType(), rec.getPeriodRule()} );
        LOGGER.log(INFO, "rec created by {0}", rec.getCreatedBy().getId());
        rec = compDM.createCompany(rec,usr, pg);
        
        LOGGER.log(INFO, "Company rec {0}", rec);
        if(rec.isVatReg()){
        VatRegistrationRec vatReg = rec.getVatRegDetails();
        vatReg.setComp(rec);
        rec.setVatRegDetails(vatReg);
        
        vatReg = vatDM.vatRegistrationUpdate(rec, rec.getVatRegDetails(), usr, pg);
        LOGGER.log(INFO, "vatReg {0}", vatReg);
        //vatReg = vatDM.vatRegistrationSetActive(rec, rec.getVatRegDetails(), usr, pg);
        rec.setVatRegDetails(vatReg);
        }
        LOGGER.log(INFO, "Company saved to DB id {0}",rec.getId());
        return rec;
        
    }
    
    public CompanyBasicRec createCompWithRef(CompanyBasicRec compNew, CompanyBasicRec compOld,
            boolean copyGl,String pg){
     LOGGER.log(INFO, "createCompByCopy called with oldComp ref {0} new comp {1} page {2}", 
             new Object[]{compOld.getReference(),compNew.getReference(),pg});
     LOGGER.log(INFO, "copyGl {0}", copyGl);
     compOld = compDM.getCompFull(compOld,copyGl);
     // populate company basic
     // set primative properties
     compNew.setCharity(compOld.isCharity());
     compNew.setCharityNumber(compOld.getCharityNumber());
     compNew.setCharityRegDate(compOld.getCharityRegDate());
     compNew.setCompanyNumber(compOld.getCompanyNumber());
     compNew.setCompanyType(compOld.getCompanyType());
     compNew.setCorp(compOld.isCorp());
     compNew.setDefaultCompany(compOld.isDefaultCompany());
     compNew.setFundAccounting(compOld.isFundAccounting());
     compNew.setIncorporatedDate(compOld.getIncorporatedDate());
     compNew.setRestrictedFunds(compOld.isRestrictedFunds());
     compNew.setVatNumber(compOld.getVatNumber());
     compNew.setChartOfAccounts(compOld.getChartOfAccounts());
     LOGGER.log(INFO, "compNew chart {0}", compNew.getChartOfAccounts());
     
     
     
     AddressRec address = compOld.getAddress();
     
     compNew.setAddress(address);
     
     List<CompPostPerRec> compPostPeriodsOld = compOld.getCompanyPostingPeriods();
     if(compPostPeriodsOld != null && !compPostPeriodsOld.isEmpty()){
      List<CompPostPerRec> compPostPeriodsNew = new ArrayList<>();
     
      for(CompPostPerRec compPostPer:compPostPeriodsOld){
       compPostPer.setAuditRecs(null);
       compPostPer.setComp(compNew);
       compPostPer.setCreatedBy(compNew.getCreatedBy());
       compPostPer.setCreatedDate(compNew.getCreatedDate());
       compPostPer.setChangedBy(null);
       compPostPer.setChangedDate(null);
       compPostPer.setId(null);
       compPostPeriodsNew.add(compPostPer);
      }
      compNew.setCompanyPostingPeriods(compPostPeriodsNew);
     }
     List<BankAccountCompanyRec> bankAccounts = compOld.getBankAccounts();
     if(bankAccounts != null && !bankAccounts.isEmpty()){
      List<BankAccountCompanyRec> newBnkAcnts = new ArrayList<>();
      for(BankAccountCompanyRec b:bankAccounts){
       b.setAuditRecs(null);
       b.setId(null);
       b.setComp(compNew);
       b.setCreatedBy(compNew.getCreatedBy());
       b.setCreatedOn(compNew.getCreatedDate());
       b.setUpdatedBy(null);
       b.setUpdatedOn(null);
       newBnkAcnts.add(b);
      }
      compNew.setBankAccounts(newBnkAcnts);
     }
     compNew.setBankAccounts(bankAccounts);
     compNew.setChartOfAccounts(compNew.getChartOfAccounts());
     compNew.setCountry(compOld.getCountry());
     compNew.setCurrency(compOld.getCurrency());
     compNew.setFundList(compOld.getFundList());
     LOGGER.log(INFO, "copyGl {0}", copyGl);
     if(copyGl){
      List<FiGlAccountCompRec> glAcnts = compOld.getGlAccounts();
      if(glAcnts != null && !glAcnts.isEmpty()){
       List<FiGlAccountCompRec> glAcntsNew = new ArrayList<>();
       for(FiGlAccountCompRec glAcnt:glAcnts){
        glAcnt.setChangedBy(null);
        glAcnt.setChangedOn(null);
        glAcnt.setCompany(compNew);
        glAcnt.setPeriodBalances(null);
        glAcnt.setRestrictedBalances(null);
        glAcnt.setCreatedBy(compNew.getCreatedBy());
        glAcnt.setCreatedOn(compNew.getCreatedDate());
        glAcnt.setAuditGlCompAccounts(null);
        glAcntsNew.add(glAcnt);
       }
       compNew.setGlAccounts(glAcntsNew);
      }
      
      
     }
     List<LedgerRec> ledgers  = compOld.getLedgers();
     LOGGER.log(INFO, "ledgers {0}", ledgers);
     compNew.setLedgers(ledgers);
     compNew.setLocale(compOld.getLocale());
      
     compNew.setPeriodRule(compOld.getPeriodRule());
     List<PeriodControlRec> periods = compOld.getPostingPeriods();
     if(periods != null){
      List<PeriodControlRec> periodsNew = new ArrayList<>();
      for(PeriodControlRec p:periods){
       p.setId(null);
       p.setCompany(compNew);
       p.setChangedBy(null);
       p.setChangedDate(null);
       periodsNew.add(p);
      }
      compNew.setPostingPeriods(periodsNew);
     }
     compNew.setRegisteredAddress(compOld.getRegisteredAddress());
     compNew.setVatRegDetails(compOld.getVatRegDetails());
     if(compOld.getVatRegistrations() != null && !compOld.getVatRegistrations().isEmpty()){
      Collections.sort(compOld.getVatRegistrations(), new VatRegByStartDateRev());
      List<VatRegistrationRec> vatRegs = new ArrayList<>();
      vatRegs.add(compOld.getVatRegistrations().get(0));
      compNew.setVatRegistrations(vatRegs);
     }
    
     LOGGER.log(INFO, "GlAccounts {0}", compNew.getGlAccounts());
     compNew = this.compDM.createCompanyWithRef(compNew, pg);
     if(compNew.getId() != null){
      sysBuff.buffRefresh(SysBuffer.BUFF_RESET_COMP);
     }
     
     return compNew;
    }

    

    /**
     * returns a list companies
     * @return
     * @throws BacException
     */
  public List<CompanyBasicRec> getCompanies() throws BacException {
    
    try{
      List<CompanyBasicRec> companies = compDM.getCompanies();
      if(companies == null || companies.isEmpty()){
       return null;
      }
      Collections.sort(companies, new CompanyByRef());
      return companies;
    }catch(BacException e){
      throw new BacException("Error retrieving Companies from DB");
    }
    
  }
/**
 * Update company apply any relevant business rules before calling data manager to update database
 * @param company
  * @param usr
  * @param pg
  * @return 
 * @throws BacException - Error messages on processing failures
 */
  public CompanyBasicRec updateCompany(CompanyBasicRec company,UserRec usr, String pg) throws BacException {
    LOGGER.log(INFO, "updateCompany called with {0}", company);
    boolean rc;
    try{
      LOGGER.log(INFO, "Company Type {0} fiscal rule {1}",
                new Object[] {company.getCompanyType(), company.getPeriodRule()} );
      company = compDM.updateCompany(company,usr,pg);
      if(company.isVatReg()){
       VatRegistrationRec vatRegComp = company.getVatRegDetails();
       vatRegComp = vatDM.vatRegistrationUpdate(company, vatRegComp, usr, pg);
       LOGGER.log(INFO, "vatRegComp id {0}", vatRegComp.getId());
       
      }
      
      
      return company;

    }catch(BacException e){
      throw new BacException("Update company manager failed: "+e.getLocalizedMessage());
    }
  }
  
  public CompPostPerRec updateCompPostPer(CompPostPerRec postPer , String pg){
   LOGGER.log(INFO, "compMgr.updateCompPostPer called with {0}", postPer);
   postPer = this.compDM.updateCompPostPerRec(postPer, pg);
   return postPer;
  }
  
  public int payTypeCopyComp(CompanyBasicRec c1,CompanyBasicRec c2, UserRec user, 
          String pg ){
   LOGGER.log(INFO, "CompManager.payTypeCopyComp called with Comp {0} and {1} ", 
           new Object[]{c1.getReference(),c2.getReference()});
   return this.configDM.paymentTypeCopyCompany(c1, c2, user, pg);
  }
/**
 * Creates a new Post type. Only to be used by Sys Admin
 * @param postType
 * @param usr
 * @param view
 * @return 
 */
  public PostTypeRec postTypeCreate(PostTypeRec postType, UserRec usr, String view){
   return null;
  }
  public ArrayList<PostTypeRec> getPostTypes() throws BacException {
    
   LOGGER.log(INFO, "Call configDM.getPostTypesDB() ");
   postTypes = configDM.getPostTypesDB();
   return postTypes;
  }

  public FundRec getFundByCodeComp(String code, Long compId){
   
   return this.compDM.getFundForCodeComp(code, compId);
  }
  public List<FundRec> getFundsForComp(CompanyBasicRec comp){
   List<FundRec> returnList = compDM.getRestrictedFundForComp(comp);
   return returnList;
  }
  private void buildDefaultPostTypes(String pg)throws BacException{
    LOGGER.log(INFO,"buildDefaultPostTypes");
    //GL Debit
    PostTypeRec glDebit = new PostTypeRec();
    glDebit.setDebit(true);
    glDebit.setDescription("General Ledger Debit");
    glDebit.setPostTypeCode("glDr");
    LedgerRec ldgr = sysBuff.getLedgerByName("GL");
    glDebit.setLedger(ldgr);
    glDebit.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", glDebit);
    try{
      configDM.createPostType(glDebit,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //GL Credit
    PostTypeRec postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("General Ledger Credit");
    postType.setPostTypeCode("glCr");
    ldgr = sysBuff.getLedgerByName("GL");
    postType.setLedger(ldgr);
    postType.setSign('-');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType, pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Invoice
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Receivable Invoice");
    postType.setPostTypeCode("arInv");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Debit
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Receivable debit");
    postType.setPostTypeCode("arDr");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }

    //AR Cancelled payment
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Receivable cancelled payment");
    postType.setPostTypeCode("arCanPymnt");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Payment
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Receivable Payment");
    postType.setPostTypeCode("arPymnt");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('-');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Canelled Invoice
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Receivable Cancelled Invoice");
    postType.setPostTypeCode("arCanInv");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('-');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Credit
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Receivable Credit");
    postType.setPostTypeCode("arCanInv");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('-');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AP Invoice
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Payable Invoice");
    postType.setPostTypeCode("apInv");
    ldgr = sysBuff.getLedgerByName("AP");
    postType.setLedger(ldgr);
    postType.setSign('1');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AP Crebit
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Payable Credit");
    postType.setPostTypeCode("apCr");
    ldgr = sysBuff.getLedgerByName("AP");
    postType.setLedger(ldgr);
    postType.setSign('1');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }

    //AP Cancelled payment
    postType = new PostTypeRec();
    postType.setDebit(false);
    postType.setDescription("Accounts Payable cancelled payment");
    postType.setPostTypeCode("apCanPymnt");
    ldgr = sysBuff.getLedgerByName("AR");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AR Payment
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Payable Payment");
    postType.setPostTypeCode("apPymnt");
    ldgr = sysBuff.getLedgerByName("AP");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AP Canelled Invoice
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Payable Cancelled Invoice");
    postType.setPostTypeCode("apCanInv");
    ldgr = sysBuff.getLedgerByName("AP");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }
    //AP Debdit
    postType = new PostTypeRec();
    postType.setDebit(true);
    postType.setDescription("Accounts Payable Debit");
    postType.setPostTypeCode("apDr");
    ldgr = sysBuff.getLedgerByName("AP");
    postType.setLedger(ldgr);
    postType.setSign('+');
    LOGGER.log(INFO, "About to call config DM to save post type {0}", postType);
    try{
      configDM.createPostType(postType,pg);
      
    }catch(BacException ex){
      LOGGER.log(INFO, "ConfigDM threw exception with code", ex.getErrorCode());
      throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
    }


  }

/**
 * Finds the posting period and year for a company.
 * @param postingDate
 * @param company
 * @return Posting period and year based on the period control rules of the company
 * @throws BacException
 */
  public FiscalPeriodYearRec getFiscalPeriodYearForDate(Date postingDate, CompanyBasicRec company)
          throws BacException {
    LOGGER.log(INFO, "getFiscalPeriodYearForDate called with date: {0} for company {1}",
            new Object[] {postingDate,company});
    FisPeriodRuleRec perRule = company.getChartOfAccounts().getPeriodRule();
    if(perRule == null){
     ChartOfAccountsRec coa =   this.compDM.getFisPeriodRuleRecForCoa(company.getChartOfAccounts());
     perRule = coa.getPeriodRule();
     LOGGER.log(INFO, "fpr {0}", perRule);
     company.setPeriodRule(coa.getPeriodRule());
    }
           
          
    LOGGER.log(INFO, "company COA chart {0}", perRule);
    int period =0;
    int year = 0;
    if(perRule == null){
      throw new BacException("PerRule not found");
    }
    LOGGER.log(INFO, "Calendar date basis is {0}", perRule.isCalendarMonthBasis());
    if(perRule.isCalendarMonthBasis()){
      LOGGER.log(INFO, "Calendar month rule");
      CalendarRuleMonthRec calRuleMonth = (CalendarRuleMonthRec)perRule.getCalendarRule();
      LOGGER.log(INFO,"Cal rule month: {0}",calRuleMonth);
      int startMonth = calRuleMonth.getStartMonthNumber();
      LOGGER.log(INFO, "Rule Start month {0}", startMonth);
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(postingDate);
      int postDateMonth = cal.get(Calendar.MONTH);
      LOGGER.log(INFO, "Entry month {0}", postDateMonth);
      period = postDateMonth - startMonth;
      period += 1;
      LOGGER.log(INFO, "period {0}", period);
      if(period < 1){
       period += 12;
      }
      
      //period = period +startMonth;
      year = cal.get(Calendar.YEAR);
      if(startMonth > 1){
        year++;
      }
      
    }
    LOGGER.log(INFO, "period is {0} Year  is {1}", new Object[] {period,year});
    FiscalPeriodYearRec periodYear = new FiscalPeriodYearRec(period,year);
    LOGGER.log(INFO, "periodYear to return is {0}", periodYear);
    return periodYear;
  }
/**
 * use to return a  list of all fiscal period rules. How a date is converted into
 * a Fiscal period and year
 * @return
 * @throws BacException
 */
  public ArrayList<FisPeriodRuleRec> getFiscalPeriodRules() throws BacException {
    LOGGER.log(INFO, "getFiscalPeriodRules() called");
    ArrayList<FisPeriodRuleRec> FiscPeriodRules = this.compDM.getFisPeriodRules();
    LOGGER.log(INFO, "Fiscal Period rules returned: {0}",FiscPeriodRules);
    return FiscPeriodRules;
  }

/**
 * Returns a list of GL accounts that have been created for the company passed in
 * 
 * @param company
 * @return
 * @throws BacException
 */
  public ArrayList<FiGlAccountCompRec> getGlAccountsForCompany(CompanyBasicRec company)
          throws BacException {
    LOGGER.log(INFO, "Comp Mgr getGlAccountsForCompany called with comp id", company.getId());
    ArrayList<FiGlAccountCompRec> glAccounts =  this.compDM.getGlAccountsForCompany(company);
    LOGGER.log(INFO, "GL accounts returned by data manager", glAccounts);

    return glAccounts;
  }

public CompanyBasicRec getVatRegsistrations(CompanyBasicRec compRec){
 List<VatRegistrationRec> regList = compDM.getVatRegistrationsForCompany(compRec);
 LOGGER.log(INFO, "Vat registrations found {0}", regList);
 compRec.setVatRegistrations(regList);
 return compRec;
}
  
public  CalendarRuleBaseRec addNewCalendar(CalendarRuleBaseRec cal, String src){
 LOGGER.log(INFO, "addFlexCalendar called with cal {0} nat {1} mon {2} flex {3}", new Object[]{
  cal.getReference(), cal.isNaturalCal(),cal.isMonthCal(), cal.isFlexCal()});
 
  cal = compDM.updateCalendarRule(cal, src);
 
 
 
 
 return cal;
} 
/**
 * Add a restricted fund to a company
 * @param fund Restricted fund
 * @param pg Calling page
 * @throws BacException
 */
  public void addRestFundToComp(FundRec fund,String pg) throws BacException {
    LOGGER.log(INFO, "addRestFundToComp called with company {0} and fund {1}",
            new Object[]{fund});

    
    FiscalPeriodYearRec period =  getFiscalPeriodYearForDate(fund.getCreatedOn(), fund.getFundForComp());
    

    try{
      LOGGER.log(INFO, "Save restricted fund with  fund {0}, fis year {1}",
              new Object[]{fund,period.getYear()});
      this.compDM.saveRestrictedFundForComp( fund,period.getYear(),pg);
    }catch(BacException ex){
      throw new BacException("Could not save restricted fund: "+fund.getFndCode()+" due to "
              +ex.getLocalizedMessage());
    }

  }

  public VatRegistrationRec saveVatRegistration(CompanyBasicRec comp, VatRegistrationRec vatReg, 
         UserRec usr, String source) throws BacException {
  LOGGER.log(INFO, "CompanyMgr.saveVatRegistration called with company {1} reg {2}", 
          new Object[]{comp,vatReg});
  vatReg.setComp(comp);
  vatReg = vatDM.vatRegistrationUpdate(comp, vatReg, usr, source);
  
  LOGGER.log(INFO, "Company manager returns var reg with id {0}", vatReg.getId());
  return vatReg;
 }

 public PostTypeRec updatePostType(PostTypeRec pt, UserRec crUsr, String view )throws BacException{
     LOGGER.log(INFO,"updatePostType called with {0}",pt.getPostTypeCode());
      //pt = this.configDM.UpdatePostType(pt, view);
      pt = sysBuff.updatePostType(pt,view);
     return pt;
    }
 public FundRec updateRestrictedFund(FundRec fund, String pg){
  return this.compDM.updateRestrictedFund(fund, pg);
 }
 
 public VatRegistrationRec updateVatRegistration(VatRegistrationRec vatReg, 
   CompanyBasicRec comp, UserRec usrRec, String pg){
  LOGGER.log(INFO, "CompMgr.updateVatRegistration called with var reg {0}", vatReg.getVatNumber());
  vatReg = vatDM.vatRegistrationUpdate(comp, vatReg, usrRec, pg);
  return vatReg;
 }
 
 public boolean vatRegistrationDelete(VatRegistrationRec vatReg,CompanyBasicRec comp,UserRec usrRec, String pg ){
  LOGGER.log(INFO, "CompMgr.vatRegistrationDelete called with vatReg {0}", vatReg.getVatNumber());
  boolean rc = vatDM.vatRegistrationDelete( vatReg, comp, usrRec, pg);
  LOGGER.log(INFO, "vatDM.vat reg del returned {0}", rc);
  return rc;
 }
 
 
}
