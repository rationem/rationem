/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import com.rationem.busRec.config.common.FundCategoryRec;
import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.common.ProcessCodeRec;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.fi.company.Fund;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.fi.company.PeriodControl;
import com.rationem.busRec.fi.company.PeriodControlRec;
import com.rationem.entity.user.User;
import com.rationem.busRec.mdm.AddressRec;
import javax.ejb.EJB;
import com.rationem.entity.mdm.Address;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.entity.config.common.Uom; 
//import com.rationem.busRec.config.company.CalendarRulePeriodLenRec;
import com.rationem.busRec.config.company.CalendarRuleBaseRec;
import com.rationem.entity.config.company.CalendarRuleMonth;
import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.entity.config.company.CalendarRuleBase;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.CalendarRuleFixedDateRec;
import com.rationem.busRec.config.company.CalendarRuleFlexPerRec;
import com.rationem.busRec.config.company.CalendarRuleFlexYearRec;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import com.rationem.entity.config.company.FisPeriodRule;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.EntityExistsException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import com.rationem.exception.BacException;
import com.rationem.entity.config.company.ChartOfAccounts;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyApArRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.entity.audit.AuditAccountType;
import com.rationem.entity.audit.AuditCalRuleBase;
import com.rationem.entity.audit.AuditCalRuleFlexPer;
import com.rationem.entity.audit.AuditCalRuleFlexYr;
import com.rationem.entity.audit.AuditChartOfAccounts;
import com.rationem.entity.audit.AuditCompApAr;
import com.rationem.entity.audit.AuditCompPostPer;
import com.rationem.entity.audit.AuditCompany;
import com.rationem.entity.audit.AuditFisPeriodRule;
import com.rationem.entity.audit.AuditPostType;
import com.rationem.entity.audit.AuditRestrictedFund;
import com.rationem.entity.config.common.FundCategory;
import com.rationem.entity.config.common.Module;
import com.rationem.entity.config.common.NumberRange;
import com.rationem.entity.config.common.ProcessCode;
import com.rationem.entity.config.company.AccountType;
import com.rationem.entity.config.company.CalendarFlexPer;
import com.rationem.entity.config.company.CalendarRuleFixedDate;
import com.rationem.entity.config.company.CalendarRuleFlexYear;
import com.rationem.entity.config.company.Ledger;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.company.CompPostPer;
import com.rationem.entity.fi.company.CompanyApAr;
import com.rationem.entity.fi.company.CompanyDocNumbers;
import com.rationem.entity.mdm.Country;
import com.rationem.entity.mdm.Currency;
import com.rationem.entity.salesTax.vat.VatRegistration;
import com.rationem.entity.tr.bank.BankAccountCompany;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


import javax.ejb.LocalBean;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import static javax.persistence.LockModeType.OPTIMISTIC;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Single point to update DB for
 * @author Chris
 */
@Stateless
@LocalBean
public class CompanyDM {
 private static final Logger LOGGER = Logger.getLogger(CompanyDM.class.getName());

 private ArrayList<FisPeriodRuleRec> periodRules;

 private EntityManager em;
 private EntityTransaction trans;

 @EJB
 private MasterDataDM masterDataDM;

 @EJB
 private UserDM userDM;

 @EJB
 private ConfigurationDM configDM;

 @EJB
 private FiMasterRecordDM flMastRecDM;
    
 @EJB
    private TreasuryDM bankDM;
    
    @EJB
    private VatDM vatDM;

    @EJB
    private SysBuffer sysBuff;
    

    @PostConstruct
    private void init(){
     LOGGER.log(INFO, "Company DM init");
     FacesContext fc = FacesContext.getCurrentInstance();
     String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
     HashMap properties = new HashMap();
     properties.put("tenantId", tenantId);
     properties.put("eclipselink.session-name", "sessionName"+tenantId);
     em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
     trans = em.getTransaction();
    }

    private ChartOfAccounts buildChartOfAccounts(ChartOfAccountsRec rec,  String pg){
     LOGGER.log(INFO, "rec per rule id {0}", rec.getPeriodRule().getId());
      ChartOfAccounts coa ;
      boolean newChart = false;
      boolean changed = false;
      if(rec.getId() == null){
       coa = new ChartOfAccounts();
       User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
       coa.setCreatedBy(crUsr);
       coa.setCreatedDate(rec.getCreatedDate());
       em.persist(coa);
       newChart = true;
       AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'I',crUsr,pg);
       aud.setNewValue(rec.getReference());
      
      }else{
       coa = em.find(ChartOfAccounts.class, rec.getId(), OPTIMISTIC);
       
      }
      
      if(newChart){
       coa.setReference(rec.getReference());
       coa.setName(rec.getName());
       coa.setDefaultChart(rec.isDefaultChart());
       coa.setOibalFwd(rec.isOibalFwd());
       if(rec.getPeriodRule() != null){
        FisPeriodRule perRule = em.find(FisPeriodRule.class, rec.getPeriodRule().getId(), OPTIMISTIC);
        coa.setPeriodRule(perRule);
       }
       if(rec.getChartCountry() != null){
        Country cntry = em.find(Country.class, rec.getChartCountry().getId(), OPTIMISTIC);
        coa.setChartCountry(cntry);
       }
       if(rec.getChartCurrency() != null){
        Currency curr = em.find(Currency.class, rec.getChartCurrency().getId(), OPTIMISTIC);
        coa.setChartCurrency(curr);
       }
      }else{
       User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
       
       if(rec.isDefaultChart() != coa.isDefaultChart()){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_DEF_CHART");
        aud.setNewValue(String.valueOf(rec.isDefaultChart()));
        aud.setOldValue(String.valueOf(coa.isDefaultChart()));
        coa.setDefaultChart(rec.isDefaultChart());
        changed = true;
       }
       if(rec.isOibalFwd() != coa.isOibalFwd()){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_OI_BAL_FWD");
        aud.setNewValue(String.valueOf(rec.isOibalFwd()));
        aud.setOldValue(String.valueOf(coa.isOibalFwd()));
        coa.setOibalFwd(rec.isOibalFwd());
        changed = true;
       }
       
       if(!rec.getName().equalsIgnoreCase(coa.getName())){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_NAME");
        aud.setNewValue(rec.getName());
        aud.setOldValue(coa.getName());
        coa.setName(coa.getName());
        changed = true;
       }
       if(!Objects.equals(rec.getPeriodRule().getId(), coa.getPeriodRule().getId())){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_PER_RULE");
        aud.setNewValue(rec.getPeriodRule().getPeriodRule());
        aud.setOldValue(coa.getPeriodRule().getPeriodRule());
        FisPeriodRule perRule = em.find(FisPeriodRule.class, rec.getPeriodRule().getId(), OPTIMISTIC);
        coa.setPeriodRule(perRule);
        changed = true;
       }
       if(!rec.getReference().equalsIgnoreCase(coa.getReference())){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_REF");
        aud.setNewValue(rec.getReference());
        aud.setOldValue(coa.getReference());
        coa.setReference(rec.getReference());
        changed  = true;
       }
       
       if(!Objects.equals(rec.getChartCountry().getId(), coa.getChartCountry().getId())){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_COUNTRY");
        aud.setNewValue(rec.getChartCountry().getCountryCode2());
        aud.setOldValue(coa.getChartCountry().getCountryCode2());
        Country cntry = em.find(Country.class, rec.getChartCountry().getId(), OPTIMISTIC);
        coa.setChartCountry(cntry);
        changed  = true;
       }
       
       if(!Objects.equals(rec.getChartCurrency().getId(), coa.getChartCurrency().getId())){
        AuditChartOfAccounts aud = buildAuditChartOfAccounts(coa,'U',chUsr,pg);
        aud.setFieldName("COA_COA_CURR");
        aud.setNewValue(rec.getChartCurrency().getName());
        aud.setOldValue(coa.getChartCurrency().getName());
        Currency curr = em.find(Currency.class, rec.getChartCountry().getId(), OPTIMISTIC);
        coa.setChartCurrency(curr);
        changed  = true;
       }
       
       if(changed){
        coa.setChangedBy(chUsr);
        coa.setChangedDate(rec.getChangedDate());
       }
      }
      
      return coa;

    }

    public ChartOfAccountsRec getChartOfAccountsRecPvt(ChartOfAccounts coa){
      return this.buildChartOfAccountsRec(coa);
    }
    private ChartOfAccountsRec buildChartOfAccountsRec(ChartOfAccounts coa){

        ChartOfAccountsRec rec = new ChartOfAccountsRec();
        rec.setChangedDate(coa.getChangedDate());
        rec.setChanges(coa.getChanges());
        rec.setCreatedDate(coa.getCreatedDate());
        rec.setDefaultChart(coa.isDefaultChart());
        rec.setId(coa.getId());
        rec.setName(coa.getName());
        rec.setOibalFwd(coa.isOibalFwd());
        rec.setReference(coa.getReference());
        if(coa.getChartCurrency() != null){
        CurrencyRec currRec = masterDataDM.buildCurrencyRecPvt(coa.getChartCurrency());
        rec.setChartCurrency(currRec);
        }else{
         rec.setChartCurrency(new CurrencyRec() );
        }
        
        if(coa.getChartCountry() != null){
         CountryRec cntry = masterDataDM.buildCountryRecPvt(coa.getChartCountry());
         
        rec.setChartCountry(cntry);
        }else{
         rec.setChartCountry(new CountryRec() );
        }
        
        return rec;
    }

    private FisPeriodRule buildFisPeriodRule(FisPeriodRuleRec rec, String pg){
     FisPeriodRule f;
     boolean changed = false;
     boolean newPer = false;
     if(rec.getId() == null){
      f = new FisPeriodRule();
      User crUsr = em.find(User.class, rec.getCreateBy().getId(), OPTIMISTIC);
      f.setCreateBy(crUsr);
      f.setCreateDate(rec.getCreateDate());
      em.persist(f);
      AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'I', crUsr, pg);
      aud.setNewValue(f.getPeriodRule());
      
      newPer = true;
     }else{
      f = em.find(FisPeriodRule.class, rec.getId(), OPTIMISTIC);
     }
     
     if(newPer){
      f.setCalendarMonthBasis(rec.isCalendarMonthBasis());
      f.setCalendarNatural(rec.isCalendarNatural());
      f.setFixedDayOfMonthBasis(rec.isFixedDayOfMonthBasis());
      f.setAnnualDateScheduleBasis(rec.isAnnualDateScheduleBasis());
      f.setPeriodLenBasis(rec.isPeriodLenBasis());
      f.setNumAuditPeriods(rec.getNumAuditPeriods());
      f.setNumPeriods(rec.getNumPeriods());
      f.setPeriodDescription(rec.getPeriodDescr());
      f.setPeriodRule(rec.getPeriodRule());
     }else{
      // change
      User chUser = em.find(User.class, rec.getChangeBy().getId(),OPTIMISTIC);
      
      if(f.isAnnualDateScheduleBasis() != rec.isAnnualDateScheduleBasis()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_FLEX_RULE");
       aud.setNewValue(String.valueOf(rec.isAnnualDateScheduleBasis()));
       aud.setOldValue(String.valueOf(f.isAnnualDateScheduleBasis()));
       
       f.setAnnualDateScheduleBasis(rec.isAnnualDateScheduleBasis());
       changed = true;
      }
      
      if(f.isCalendarMonthBasis() != rec.isCalendarMonthBasis()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_CAL_RULE");
       aud.setNewValue(String.valueOf(rec.isCalendarMonthBasis()));
       aud.setOldValue(String.valueOf(f.isCalendarMonthBasis()));
       f.setCalendarMonthBasis(rec.isCalendarMonthBasis());
       changed = true;
      }
      if(f.isCalendarNatural()!= rec.isCalendarNatural()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_CAL_NAT_RULE");
       aud.setNewValue(String.valueOf(rec.isCalendarNatural()));
       aud.setOldValue(String.valueOf(f.isCalendarNatural()));
       f.setCalendarNatural(rec.isCalendarNatural());
       changed = true;
      }
      if(f.isFixedDayOfMonthBasis() != rec.isFixedDayOfMonthBasis()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_DOM_RULE");
       aud.setNewValue(String.valueOf(rec.isFixedDayOfMonthBasis()));
       aud.setOldValue(String.valueOf(f.isFixedDayOfMonthBasis()));
       f.setFixedDayOfMonthBasis(rec.isFixedDayOfMonthBasis());
       changed = true;
      }
      if(rec.getNumAuditPeriods() != f.getNumAuditPeriods()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_AUD_PERS");
       aud.setNewValue(String.valueOf(rec.getNumAuditPeriods()));
       aud.setOldValue(String.valueOf(f.getNumAuditPeriods()));
       f.setNumAuditPeriods(rec.getNumAuditPeriods());
      }
      if(rec.getNumPeriods() != f.getNumPeriods()){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_STD_PERS");
       aud.setNewValue(String.valueOf(rec.getNumPeriods()));
       aud.setOldValue(String.valueOf(f.getNumPeriods()));
       aud.setUsrAction('U');
       em.persist(aud);
       f.setNumPeriods(rec.getNumPeriods());
      }
      
      if(!rec.getPeriodRule().equalsIgnoreCase(f.getPeriodRule())){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_RULE");
       aud.setNewValue(rec.getPeriodRule());
       aud.setOldValue(f.getPeriodRule());
       f.setNumPeriods(rec.getNumPeriods());
      }
      
      if(!rec.getPeriodDescr().equalsIgnoreCase(f.getPeriodDescription())){
       AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(f,'U', chUser, pg);
       aud.setFieldName("FIS_PER_RULE");
       aud.setNewValue(rec.getPeriodDescr());
       aud.setOldValue(f.getPeriodDescription());
       f.setPeriodDescription(rec.getPeriodDescr());
      }
      if(changed){
       f.setChangeBy(chUser);
       f.setChangeDate(rec.getChangeDate());
      }
     }
     
     
     return f;
    }
    
  

    private FisPeriodRuleRec buildFisPeriodRuleRec(FisPeriodRule rec){
     LOGGER.log(INFO, "buildFisPeriodRuleRec called with: {0}", rec);
     FisPeriodRuleRec f = new FisPeriodRuleRec();
     f.setChangeDate(rec.getChangeDate());
     f.setCreateDate(rec.getCreateDate());
     // determine option
     
     if(rec.isCalendarMonthBasis()){
      f.setCalBasisOption(1);
      f.setCalendarMonthBasis(rec.isCalendarMonthBasis());
     }else if(rec.isFixedDayOfMonthBasis()){
      f.setCalBasisOption(2);
      f.setFixedDayOfMonthBasis(rec.isFixedDayOfMonthBasis());
     }else{
      f.setCalBasisOption(3);
      f.setAnnualDateScheduleBasis(rec.isAnnualDateScheduleBasis());
     }
      
     //if(f.getCalBasisOption() == 1){
     
      LOGGER.log(INFO, "Need to set calendar rule");
      f.setCalendarMonthBasis(rec.isCalendarMonthBasis());
      if(rec.getCalRule() != null){
      LOGGER.log(INFO, "Cal rule {0} ", rec.getCalRule());
      if(rec.getCalRule().getClass().getSimpleName().equalsIgnoreCase("CalendarRuleMonth")){
       CalendarRuleMonth calMth = (CalendarRuleMonth)rec.getCalRule();
       CalendarRuleMonthRec calMthRec = this.buildCalendarRuleMonthRec(calMth);
       f.setCalendarRule(calMthRec);
      }
    }
     f.setCalendarMonthBasis(rec.isCalendarMonthBasis());
     f.setFixedDayOfMonthBasis(rec.isFixedDayOfMonthBasis());
     f.setAnnualDateScheduleBasis(rec.isAnnualDateScheduleBasis());
     
     f.setPeriodLenBasis(rec.isPeriodLenBasis());
     f.setId(rec.getId());
     f.setNumAuditPeriods(rec.getNumAuditPeriods());
     f.setNumPeriods(rec.getNumPeriods());
     f.setPeriodDescr(rec.getPeriodDescription());
     f.setPeriodRule(rec.getPeriodRule());
     f.setRevision(rec.getRevision());
     LOGGER.log(INFO, "End build period rule basis {0}", f.getCalBasisOption());
     return f;

    }

    private PeriodControlRec buildPeriodControlRec(CompanyBasicRec comp,PeriodControl pc){
      LOGGER.log(INFO,"PeriodControlRec called with {0}",pc);
      PeriodControlRec rec = new PeriodControlRec();
      if(pc.getChangedBy() != null){
        // change recorded so set changed by and change date
        UserRec chUsr = userDM.getUserRecPvt(pc.getChangedBy());
        rec.setChangedBy(chUsr);
        rec.setChangedDate(pc.getChangedDate());

      }
      if(pc.getCompany() != null){
        rec.setCompany(comp);
      }
      if(pc.getCreatedBy()!= null){
        //created by set so set created by user and date
        UserRec crUsr = userDM.getUserRecPvt(pc.getCreatedBy());
        rec.setCreatedBy(crUsr);
        rec.setCreatedDate(pc.getCreatedDate());
      }
      rec.setId(pc.getId());
      if(pc.getLedger() != null){
        LedgerRec ldgrRec = configDM.buildLedgerRecPvt(pc.getLedger());
        rec.setLedger(ldgrRec);
      }
      rec.setPeriodFrom(pc.getPeriodFrom());
      rec.setPeriodTo(pc.getPeriodTo());
      rec.setRevision(pc.getRevision());
      rec.setYearFrom(pc.getYearFrom());
      rec.setYearTo(pc.getYearTo());

      return rec;
    }

    public FundRec getRestrictedFundById(long fundId){
     LOGGER.log(INFO, "getRestrictedFundById called with {0}", fundId);
     if(!trans.isActive()){
      trans.begin();
     }
     Fund fnd = em.find(Fund.class, fundId, OPTIMISTIC);
     FundRec fndRec = this.builddRestrictedFundRec(fnd);
     return fndRec;
     
    }
    
    public FundRec getRestrictedFundRec(CompanyBasicRec comp,Fund f ){
     return buildRestrictedFundRec(comp,f);
    }
    
    private FundRec builddRestrictedFundRec(Fund fnd){
     FundRec fndRec = new FundRec();
     UserRec usr = this.userDM.getUserRecPvt(fnd.getCreatedBy());
     fndRec.setCreatedBy(usr);
     fndRec.setCreatedOn(fnd.getCreatedOn());
     if(fnd.getChangedBy() != null){
      UserRec chUsr = userDM.getUserRecPvt(fnd.getChangedBy());
      fndRec.setChangedBy(chUsr);
      fndRec.setChangedOn(fnd.getChangedOn());
     }
     fndRec.setFndCode(fnd.getFndCode());
     if(fnd.getFundCategory() != null){
      FundCategoryRec cat = this.configDM.getFundCategoryById(fnd.getFundCategory().getId());
      fndRec.setFundCategory(cat);
     }
     CompanyBasicRec comp = this.sysBuff.getCompanyById(fnd.getFundForComp().getId());
     fndRec.setFundForComp(comp);
     fndRec.setName(fnd.getName());
     fndRec.setPurpose(fnd.getPurpose());
     fndRec.setReturnDue(fnd.getReturnDue());
     fndRec.setValidFrom(fnd.getValidFrom());
     fndRec.setValidTo(fnd.getValidTo());
     return fndRec;
     
     
    }
    private FundRec buildRestrictedFundRec(CompanyBasicRec comp,Fund f ){
      FundRec fnd = new FundRec();
      fnd.setId(f.getId());
      if(f.getChangedBy() != null){
        UserRec chUsr = this.userDM.getUserRecPvt(f.getChangedBy());
        fnd.setChangedBy(chUsr);
        fnd.setChangedOn(f.getChangedOn());
      }



      UserRec crUsr = this.userDM.getUserRecPvt(f.getCreatedBy());
      fnd.setCreatedBy(crUsr);
      fnd.setCreatedOn(f.getCreatedOn());
      // mandatory
      fnd.setFndCode(f.getFndCode());
      if(f.getFundForComp() != null){
        fnd.setFundForComp(comp);

        List<FundRec> fndList = comp.getFundList();
        fndList.add(fnd);
        comp.setFundList(fndList);
      }
      if(f.getName() != null){
        fnd.setName(f.getName());
      }
      if(f.getPurpose() != null){
        fnd.setPurpose(f.getPurpose());
      }
      if(f.getReturnDue() != null){
        fnd.setReturnDue(f.getReturnDue());
      }
      fnd.setReturnRequired(f.isReturnRequired());
      if(f.getValidFrom() != null){
        fnd.setValidFrom(f.getValidFrom());
      }
      if(f.getValidTo() != null){
        fnd.setValidTo(f.getValidTo());
      }
      return fnd;
    }


    private Fund buildRestrictedFund( FundRec f, String pg ){
      Fund fnd;
      boolean newFnd = false;
      boolean changedFnd = false;
      
      LOGGER.log(INFO, "f.getId {0}", f.getId());
      if(f.getId() == null){
        //new fund
        fnd = new Fund();
        User crUsr = em.find(User.class, f.getCreatedBy().getId(), OPTIMISTIC);
        fnd.setCreatedBy(crUsr);
        fnd.setCreatedOn(f.getCreatedOn());
        em.persist(fnd);
        AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, crUsr, 'I', pg);
        aud.setNewValue(f.getFndCode());
        newFnd = true;
      }else{
       fnd = em.find(Fund.class, f.getId());
      }
      if(newFnd){
        fnd.setFndCode(f.getFndCode());
        CompanyBasic comp = sysBuff.getComp(f.getFundForComp());
        fnd.setFundForComp(comp);
        fnd.setName(f.getName());
        fnd.setPurpose(f.getPurpose());
        fnd.setReturnDue(f.getReturnDue());
        fnd.setReturnRequired(f.isReturnRequired());
        fnd.setValidFrom(f.getValidFrom());
        fnd.setValidTo(f.getValidTo());
        FundCategory cat = em.find(FundCategory.class, f.getFundCategory().getId(), OPTIMISTIC);
        fnd.setFundCategory(cat);
       }else{
        // Changed
        User chUsr = em.find(User.class, f.getChangedBy().getId(), OPTIMISTIC);
        
        if((f.isReturnRequired() != fnd.isReturnRequired())){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_RET_REQ");
         aud.setNewValue(String.valueOf(f.isReturnRequired()));
         aud.setOldValue(String.valueOf(fnd.isReturnRequired()));
         fnd.setReturnRequired(f.isReturnRequired());
         changedFnd = true;
        }
        
        if((f.getFndCode() == null && fnd.getFndCode() != null) ||
           (f.getFndCode() != null && fnd.getFndCode() == null) ||
           (f.getFndCode() != null && !f.getFndCode().equalsIgnoreCase(fnd.getFndCode()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_CODE");
         aud.setNewValue(f.getFndCode());
         aud.setOldValue(fnd.getFndCode());
         fnd.setFndCode(f.getFndCode());
         changedFnd = true;
        }
        
        if((f.getFundForComp() == null && fnd.getFundForComp() != null) ||
           (f.getFundForComp() != null && fnd.getFundForComp() == null) ||
           (f.getFundForComp() != null && !Objects.equals(f.getFundForComp().getId(), fnd.getFundForComp().getId()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_COMP");
         aud.setNewValue(f.getFundForComp().getReference());
         aud.setOldValue(fnd.getFundForComp().getNumber());
         CompanyBasic comp = sysBuff.getComp(f.getFundForComp());
         fnd.setFundForComp(comp);
         changedFnd = true;
        }
        
        if((f.getName() == null && fnd.getName() != null) ||
           (f.getName() != null && fnd.getName() == null) ||
           (f.getName() != null && !f.getName().equalsIgnoreCase(fnd.getName()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_NAME");
         aud.setNewValue(f.getName());
         aud.setOldValue(fnd.getName());
         fnd.setName(f.getName());
         changedFnd = true;
        }
        
        if((f.getPurpose() == null && fnd.getPurpose() != null) ||
           (f.getPurpose() != null && fnd.getPurpose() == null) ||
           (f.getPurpose() != null && !f.getPurpose().equalsIgnoreCase(fnd.getPurpose()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_PURPOSE");
         aud.setNewValue(f.getPurpose());
         aud.setOldValue(fnd.getPurpose());
         fnd.setPurpose(f.getPurpose());
         changedFnd = true;
        }
        
        if((f.getReturnDue() == null && fnd.getReturnDue() != null) ||
           (f.getReturnDue() != null && fnd.getReturnDue() == null) ||
           (f.getReturnDue() != null && !f.getReturnDue().equals(fnd.getReturnDue()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_RET_DUE");
         if(f.getReturnDue() != null){
         aud.setNewValue(f.getReturnDue().toString());
         }
         if(fnd.getReturnDue() != null){
         aud.setOldValue(fnd.getReturnDue().toString());
         }
         fnd.setReturnDue(f.getReturnDue());
         changedFnd = true;
        }
        
        if((f.getValidFrom() == null && fnd.getValidFrom() != null) ||
           (f.getValidFrom() != null && fnd.getValidFrom() == null) ||
           (f.getValidFrom() != null && !f.getValidFrom().equals(fnd.getValidFrom()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_FROM");
         if(f.getValidFrom() != null){
         aud.setNewValue(f.getValidFrom().toString());
         }
         if(fnd.getValidFrom() != null){
         aud.setOldValue(fnd.getValidFrom().toString());
         }
         fnd.setValidFrom(f.getValidFrom());
         changedFnd = true;
        }
        
        if((f.getValidTo() == null && fnd.getValidTo() != null) ||
           (f.getValidTo() != null && fnd.getValidTo() == null) ||
           (f.getValidTo() != null && !f.getValidTo().equals(fnd.getValidTo()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("RESTR_FND_TO");
         if(f.getValidTo() != null) {
         aud.setNewValue(f.getValidTo().toString());
         }
         if(fnd.getValidTo() != null) {
         aud.setOldValue(fnd.getValidTo().toString());
         }
         fnd.setValidTo(f.getValidTo());
         changedFnd = true;
        }
        
        
        if((f.getFundCategory() == null && fnd.getFundCategory() != null) ||
           (f.getFundCategory() != null && fnd.getFundCategory() == null) ||
           (f.getFundCategory() != null && !Objects.equals(f.getFundCategory().getId(), fnd.getFundCategory().getId()))){
         AuditRestrictedFund aud = buildAuditRestrictedFund(fnd, chUsr, 'U', pg);
         aud.setFieldName("REST_FND_TYPE");
         aud.setNewValue(f.getFundCategory().getCatRef());
         aud.setOldValue(f.getFundCategory().getCatRef());
         FundCategory cat = em.find(FundCategory.class, f.getFundCategory().getId(), OPTIMISTIC);
         fnd.setFundCategory(cat);
         changedFnd = true;
        }
        
        if(changedFnd){
         fnd.setChangedBy(chUsr);
         fnd.setChangedOn(f.getChangedOn());
        }
       

      }
      
      return fnd;
    }

    private CalendarRuleBaseRec buildCalendarRuleBaseRec(CalendarRuleBase rec){
        CalendarRuleBaseRec f = new CalendarRuleBaseRec();
        Date current = new Date();
        f.setChangedOn(current);
        f.setCreatedOn(current);
        f.setReference(rec.getReference());
        f.setDescription(rec.getDescription());
        f.setFlexCal(rec.isFlexCal());
        f.setMonthCal(rec.isMonthCal());
        f.setNaturalCal(rec.isNaturalCal());
        f.setCalType("calBase");
        f.setId(rec.getId());
        f.setVersion(rec.getVersion());
        /*LOGGER.log(INFO, "Flex years {0}", rec.getCalRuleFlexYears());
        if(rec.getCalRuleFlexYears() != null){
         f.setCalType("calFlexYr");
        }
        */
        return f;
    }

    private CompanyApAr buildCompanyArAp(CompanyApArRec rec, String pg){
     
     CompanyApAr apAr;
     boolean newApAr = false;
     boolean changedApAr = false;
     
     if(!trans.isActive()){
      trans.begin();
     }
     if(rec.getId() == null){
      apAr = new CompanyApAr();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId());
      apAr.setCreatedBy(crUsr);
      apAr.setCreatedDate(rec.getCreatedDate());
      em.persist(apAr);
      AuditCompApAr aud = buildAuditCompApAr(apAr, 'I', crUsr, pg);
      aud.setNewValue(rec.getComp().getReference());
      newApAr = true;
     }else{
      apAr = em.find(CompanyApAr.class, rec.getId());
     }
     
     if(newApAr){
      CompanyBasic comp = this.sysBuff.getComp(rec.getComp());
      apAr.setComp(comp);
      apAr.setApBucket1(rec.getApBucket1());
      apAr.setApBucket2(rec.getApBucket2());
      apAr.setApBucket3(rec.getApBucket3());
      apAr.setApBucket4(rec.getApBucket4());
      apAr.setArBucket1(rec.getArBucket1());
      apAr.setArBucket2(rec.getArBucket2());
      apAr.setArBucket3(rec.getArBucket3());
      apAr.setArBucket4(rec.getArBucket4());
     }else{
      User chUsr = em.find(User.class, rec.getChangedBy().getId());
      if(apAr.getApBucket1() != rec.getApBucket1()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AP_B1");
       aud.setNewValue(String.valueOf(rec.getApBucket1()));
       aud.setOldValue(String.valueOf(apAr.getApBucket1()));
       apAr.setApBucket1(rec.getApBucket1());
      }
      if(apAr.getApBucket2() != rec.getApBucket2()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AP_B2");
       aud.setNewValue(String.valueOf(rec.getApBucket2()));
       aud.setOldValue(String.valueOf(apAr.getApBucket2()));
       apAr.setApBucket2(rec.getApBucket2());
      }
      if(apAr.getApBucket3() != rec.getApBucket3()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AP_B3");
       aud.setNewValue(String.valueOf(rec.getApBucket3()));
       aud.setOldValue(String.valueOf(apAr.getApBucket3()));
       apAr.setApBucket3(rec.getApBucket3());
      }
      if(apAr.getApBucket4() != rec.getApBucket4()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AP_B4");
       aud.setNewValue(String.valueOf(rec.getApBucket4()));
       aud.setOldValue(String.valueOf(apAr.getApBucket4()));
       apAr.setApBucket4(rec.getApBucket4());
      }
      if(apAr.getArBucket1() != rec.getArBucket1()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AR_B1");
       aud.setNewValue(String.valueOf(rec.getArBucket1()));
       aud.setOldValue(String.valueOf(apAr.getArBucket1()));
       apAr.setArBucket1(rec.getArBucket1());
      }
      if(apAr.getArBucket2() != rec.getArBucket2()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AR_B2");
       aud.setNewValue(String.valueOf(rec.getArBucket2()));
       aud.setOldValue(String.valueOf(apAr.getArBucket2()));
       apAr.setArBucket2(rec.getArBucket2());
      }
      if(apAr.getArBucket3() != rec.getArBucket3()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AR_B3");
       aud.setNewValue(String.valueOf(rec.getArBucket3()));
       aud.setOldValue(String.valueOf(apAr.getArBucket3()));
       apAr.setArBucket3(rec.getArBucket3());
      }
      if(apAr.getArBucket4() != rec.getArBucket4()){
       AuditCompApAr aud = buildAuditCompApAr(apAr, 'U', chUsr, pg);
       aud.setFieldName("COMP_AR_B4");
       aud.setNewValue(String.valueOf(rec.getArBucket4()));
       aud.setOldValue(String.valueOf(apAr.getArBucket4()));
       apAr.setArBucket4(rec.getArBucket4());
      }
     }
     
     trans.commit();
     return apAr;
    }
    
    private CompanyApArRec buildCompanyArAp(CompanyApAr apAr){
     CompanyApArRec rec = new CompanyApArRec();
     rec.setId(apAr.getId());
     rec.setApBucket1(apAr.getApBucket1());
     rec.setApBucket2(apAr.getApBucket2());
     rec.setApBucket3(apAr.getApBucket3());
     rec.setApBucket4(apAr.getApBucket4());
     rec.setArBucket1(apAr.getApBucket1());
     rec.setArBucket2(apAr.getArBucket2());
     rec.setArBucket3(apAr.getArBucket3());
     rec.setArBucket4(apAr.getArBucket4());
     UserRec crBy = this.userDM.getUserRecPvt(apAr.getCreatedBy());
     rec.setCreatedBy(crBy);
     rec.setCreatedDate(apAr.getCreatedDate());
     if(apAr.getChangedBy() != null){
      UserRec chBy = userDM.getUserRecPvt(apAr.getChangedBy());
      if(chBy != null){
       rec.setChangedBy(chBy);
       rec.setChangedDate(apAr.getChangedDate());
      }
     }
     return rec;
    }
    private CalendarRuleBase buildCalendarRuleBase(CalendarRuleBaseRec rec, String source){
        CalendarRuleBase f;
        boolean newCalRule = false;
        boolean changed = false;
        if(rec.getId() == null){
         f = new CalendarRuleBase();
         User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
         f.setCreatedBy(crUsr);
         f.setCreatedOn(rec.getCreatedOn());
         em.persist(f);
         AuditCalRuleBase aud = this.buildAuditCalendarRule(f, 'I',crUsr, source);
         aud.setNewValue(rec.getReference());
         em.persist(aud);
         newCalRule = true;
        }else{
         f = em.find(CalendarRuleBase.class, rec.getId(), OPTIMISTIC);
        }
        if(newCalRule){
         f.setReference(rec.getReference());
         f.setDescription(rec.getDescription());
         f.setFlexCal(rec.isFlexCal());
         f.setNaturalCal(rec.isNaturalCal());
         f.setFlexCal(rec.isFlexCal());
        }else{
         User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
         
         if((rec.getDescription() != null &&rec.getDescription() != null && 
                 rec.getDescription().equalsIgnoreCase(f.getDescription())) ||(
                 rec.getDescription() == null && f.getDescription() != null) ||
                 (rec.getDescription() != null && f.getDescription() == null)){
          AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, source);
          aud.setNewValue(rec.getDescription());
          aud.setOldValue(f.getDescription());
          aud.setFieldName("FIS_CAL_DESCR");
          aud.setUsrAction('U');
          em.persist(aud);
          f.setDescription(rec.getDescription());
          changed = true;
         }
         if((rec.getReference()!= null &&rec.getReference() != null && 
                 rec.getReference().equalsIgnoreCase(f.getReference())) ||(
                 rec.getReference() == null && f.getReference() != null) ||
                 (rec.getReference() != null && f.getReference() == null)){
          AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, source);
          aud.setNewValue(rec.getReference());
          aud.setOldValue(f.getReference());
          aud.setFieldName("FIS_CAL_REF");
          aud.setUsrAction('U');
          em.persist(aud);
          f.setDescription(rec.getDescription());
          changed = true;
         }
         
         if(rec.isFlexCal() != f.isFlexCal()){
          AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, source);
          aud.setNewValue(String.valueOf(rec.isFlexCal()));
          aud.setOldValue(String.valueOf(f.isFlexCal()));
          aud.setFieldName("FIS_CAL_FLEX");
          f.setFlexCal(rec.isFlexCal());
          changed = true;
         }
         
         if(rec.isMonthCal() != f.isMonthCal()){
          AuditCalRuleBase aud = this.buildAuditCalendarRule(f, 'U',chUsr, source);
          aud.setNewValue(String.valueOf(rec.isMonthCal()));
          aud.setOldValue(String.valueOf(f.isMonthCal()));
          aud.setFieldName("FIS_CAL_MON");
          f.setMonthCal(rec.isMonthCal());
          changed = true;
         }
         if(rec.isNaturalCal() != f.isNaturalCal()){
          AuditCalRuleBase aud = this.buildAuditCalendarRule(f, 'U', chUsr, source);
          aud.setNewValue(String.valueOf(rec.isNaturalCal()));
          aud.setOldValue(String.valueOf(f.isNaturalCal()));
          aud.setFieldName("FIS_CAL_NAT");
          f.setNaturalCal(rec.isNaturalCal());
          changed = true;
         }
         
          
         
         if(changed){
          f.setChangedBy(chUsr);
          f.setChangedOn(rec.getChangedOn());
         }
        }
        
        return f;
    }

    private CalendarRuleMonthRec buildCalendarRuleMonthRec(CalendarRuleMonth rec){
      LOGGER.log(INFO, "buildCalendarRuleMonthRec called with cal rule {0}", rec);
      CalendarRuleMonthRec mthRule = new CalendarRuleMonthRec();
      mthRule.setChangedOn(rec.getChangedOn());
      if(rec.getChangedBy() != null){
        mthRule.setChangedBy(this.userDM.getUserRecPvt(rec.getChangedBy()));
        mthRule.setChangedOn(rec.getChangedOn());
      }
      mthRule.setCreatedOn(rec.getCreatedOn());
      mthRule.setReference(rec.getReference());
      mthRule.setDescription(rec.getDescription());
      mthRule.setStartMonthNumber(rec.getStartMonthNumber());
      mthRule.setId(rec.getId());
      mthRule.setCalType("calMonth");

      mthRule.setStartMonthNumber(rec.getStartMonthNumber());
      LOGGER.log(INFO, "buildCalendarRuleMonthRec return cal start month {0}",mthRule.getStartMonthNumber());
      return mthRule;
    }
    
    private CalendarRuleFixedDate buildCalendarRuleFixedDate(CalendarRuleFixedDateRec rec, 
            String pg){
     LOGGER.log(INFO, "buildCalendarRuleFixedDate called with {0} id {1}", new Object[]{rec, rec.getId()});
     CalendarRuleFixedDate f;
     boolean changed = false;
     boolean newFixedDate =false;
     if(rec.getId() == null){
      f = new CalendarRuleFixedDate();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      f.setCreatedBy(crUsr);
      f.setCreatedOn(rec.getCreatedOn());
      em.persist(f);
      AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'I', crUsr, pg);
      aud.setNewValue(f.getReference());
      aud.setUsrAction('I');
      em.persist(aud);
      newFixedDate = true;
     }else{
      f = em.find(CalendarRuleFixedDate.class, rec.getId(), OPTIMISTIC);
     }
     
     if(newFixedDate){
      f.setDayOfMonth(rec.getDayOfMonth());
      f.setDescription(rec.getDescription());
      f.setReference(rec.getReference());
      f.setStartMonthNumber(rec.getStartMonthNumber());
      f.setPeriodNum(rec.getPeriodNum());
      f.setSpecialPeriodNum(rec.getSpecialPeriodNum());
      
      
     }else{
      // is this a change
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      if(f.getDayOfMonth() != rec.getDayOfMonth()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_DOM");
       aud.setNewValue(String.valueOf(rec.getDayOfMonth()));
       aud.setOldValue(String.valueOf(f.getDayOfMonth()));
       aud.setUsrAction('U');
       f.setDayOfMonth(rec.getDayOfMonth());
       changed = true;
      }
      if(f.getStartMonthNumber() != rec.getStartMonthNumber()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_START_MNTH");
       aud.setNewValue(String.valueOf(rec.getStartMonthNumber()));
       aud.setOldValue(String.valueOf(f.getStartMonthNumber()));
       
       f.setStartMonthNumber(rec.getStartMonthNumber());
       changed = true;
      }
      if(f.getPeriodNum() != rec.getPeriodNum()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_NUM_PER");
       aud.setNewValue(String.valueOf(rec.getPeriodNum()));
       aud.setOldValue(String.valueOf(f.getPeriodNum()));
       
       f.setPeriodNum(rec.getPeriodNum());
       changed = true;
      }
      
      if(f.getSpecialPeriodNum() != rec.getSpecialPeriodNum()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_NUM_SP_PER");
       aud.setNewValue(String.valueOf(rec.getSpecialPeriodNum()));
       aud.setOldValue(String.valueOf(f.getSpecialPeriodNum()));
       
       f.setSpecialPeriodNum(rec.getSpecialPeriodNum());
       changed = true;
      }
      if(changed){
       f.setChangedBy(chUsr);
       f.setChangedOn(rec.getChangedOn());
      }
      
     }
     
     LOGGER.log(INFO, "Cal ref {0} norm {1} sp {2}", new Object[]{f.getReference(),f.getPeriodNum(),
      f.getSpecialPeriodNum()});
    
     return f;
    }
    
    private CalendarRuleFixedDateRec buildCalendarRuleFixedDateRec(CalendarRuleFixedDate rule){
     CalendarRuleFixedDateRec rec = new CalendarRuleFixedDateRec();
     rec.setId(rule.getId());
     UserRec crUsr = this.userDM.getUserRecPvt(rule.getCreatedBy());
     rec.setCreatedBy(crUsr);
     rec.setCreatedOn(rule.getCreatedOn());
     
     if(rule.getChangedBy() != null){
      UserRec chUsr = this.userDM.getUserRecPvt(rule.getChangedBy());
      rec.setChangedBy(chUsr);
      rec.setChangedOn(rule.getChangedOn());
     }
     rec.setDayOfMonth(rule.getDayOfMonth());
     rec.setDescription(rule.getDescription());
     rec.setPeriodNum(rule.getPeriodNum());
     rec.setReference(rule.getReference());
     rec.setSpecialPeriodNum(rule.getSpecialPeriodNum());
     rec.setStartMonthNumber(rule.getStartMonthNumber());
     rec.setCalType("calDoM");
     return rec;
    }

    private CalendarFlexPer buildCalendarRuleFlexPer(CalendarRuleFlexPerRec rec, String pg){
     LOGGER.log(INFO, "buildCalendarRuleFlexPer called with rec {0}", rec);
     CalendarFlexPer calPer;
     boolean newCalPer = false;
     boolean changed = false;
     if(rec.getId() == null){
      calPer = new CalendarFlexPer();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      calPer.setCreatedBy(crUsr);
      calPer.setCreatedOn(rec.getCreatedOn());
      em.persist(calPer);
      AuditCalRuleFlexPer aud = this.buildAuditCalRuleFlexPer(calPer, crUsr, rec.getCreatedOn(), pg);
      aud.setNewValue(String.valueOf(rec.getCalPeriod()));
      aud.setUsrAction('I');
      newCalPer = true;
     }else{
      calPer = em.find(CalendarFlexPer.class, rec.getId(), OPTIMISTIC);
     }
     
     if(newCalPer){
      CalendarRuleFlexYear flexYr = em.find(CalendarRuleFlexYear.class, 
              rec.getCalRuleFlexYr().getId(), OPTIMISTIC);
      calPer.setCalRuleFlexYr(flexYr);
      calPer.setCalPeriod(rec.getCalPeriod());
      calPer.setEndPeriod(rec.getEndPeriod());
      calPer.setStartPeriod(rec.getStartPeriod());
     }else{
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      if(rec.getCalPeriod() != calPer.getCalPeriod()){
       AuditCalRuleFlexPer aud = this.buildAuditCalRuleFlexPer(calPer, chUsr, rec.getChangedOn(), pg);
       aud.setFieldName("FIS_CAL_FLEX_PER");
       aud.setNewValue(String.valueOf(rec.getCalPeriod()));
       aud.setOldValue(String.valueOf(calPer.getCalPeriod()));
       aud.setUsrAction('U');
       calPer.setCalPeriod(rec.getCalPeriod());
       changed = true;
      }
      if(!rec.getStartPeriod().equals(calPer.getStartPeriod())){
       AuditCalRuleFlexPer aud = this.buildAuditCalRuleFlexPer(calPer, chUsr, rec.getChangedOn(), pg);
       aud.setFieldName("FIS_CAL_FLEX_ST_DT");
       aud.setNewValue(String.valueOf(rec.getStartPeriod()));
       aud.setOldValue(String.valueOf(calPer.getStartPeriod()));
       aud.setUsrAction('U');
       calPer.setStartPeriod(rec.getStartPeriod());
       changed = true;
      }
      if(!rec.getEndPeriod().equals(calPer.getEndPeriod())){
       AuditCalRuleFlexPer aud = this.buildAuditCalRuleFlexPer(calPer, chUsr, rec.getChangedOn(), pg);
       aud.setFieldName("FIS_CAL_FLEX_END_DT");
       aud.setNewValue(String.valueOf(rec.getEndPeriod()));
       aud.setOldValue(String.valueOf(calPer.getEndPeriod()));
       aud.setUsrAction('U');
       calPer.setStartPeriod(rec.getEndPeriod());
       changed = true;
      }
      if(changed){
       calPer.setChangedBy(chUsr);
       calPer.setChangedOn(rec.getChangedOn());
      }
     }
     
     return calPer;
    }
    
    private CalendarRuleFlexPerRec buildCalendarRuleFlexPerRec(CalendarFlexPer per){
     CalendarRuleFlexPerRec perRec = new CalendarRuleFlexPerRec();
     perRec.setId(per.getId());
     UserRec crUsr = this.userDM.getUserRecPvt(per.getCreatedBy());
     perRec.setCreatedBy(crUsr);
     perRec.setCreatedOn(per.getCreatedOn());
     if(per.getChangedBy() != null){
      UserRec chUsr = userDM.getUserRecPvt(per.getChangedBy());
      perRec.setChangedBy(chUsr);
      perRec.setChangedOn(per.getChangedOn());
     }
     perRec.setCalPeriod(per.getCalPeriod());
     perRec.setEndPeriod(per.getEndPeriod());
     perRec.setStartPeriod(per.getStartPeriod());
     CalendarRuleFlexYear flexYr = per.getCalRuleFlexYr();
     CalendarRuleFlexYearRec flexYrRec = this.buildCalendarRuleFlexYearRec(flexYr);
     perRec.setCalRuleFlexYr(flexYrRec);
     return perRec;
    }
    
    private CalendarRuleFlexYear buildCalendarRuleFlexYear(CalendarRuleFlexYearRec rec, 
            String pg){
     CalendarRuleFlexYear calYr;
     boolean newYear = false;
     boolean changedYear = false;
     if(rec.getId() == null){
      calYr = new CalendarRuleFlexYear();
      User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      calYr.setCreatedBy(crUsr);
      calYr.setCreatedOn(rec.getCreatedOn());
      calYr.setCalYear(rec.getCalYear());
      
      //CalendarRuleBase ruleBase = em.find(CalendarRuleBase.class, rec.getCalRule().getId(), OPTIMISTIC); //this.buildCalendarRuleBase(rec.getCalRule(), pg);
      //calYr.setCalRule(ruleBase);
      em.persist(calYr);
      AuditCalRuleFlexYr aud = buildAuditCalRuleFlexYr(calYr, crUsr, pg, 'I');
      aud.setNewValue(String.valueOf(rec.getCalYear()));
      
     }else{
      calYr = em.find(CalendarRuleFlexYear.class, rec.getId(), OPTIMISTIC);
      if(rec.getCalYear() != calYr.getCalYear()){
       User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
       AuditCalRuleFlexYr aud = buildAuditCalRuleFlexYr(calYr, chUsr, pg, 'U');
       aud.setFieldName("FIS_CAL_FLEX_YR");
       aud.setNewValue(String.valueOf(rec.getCalYear()));
       }
     }
     
     return calYr;
    }
    private CalendarRuleFlexYearRec buildCalendarRuleFlexYearRec(CalendarRuleFlexYear yr){
     CalendarRuleFlexYearRec calYrRec = new CalendarRuleFlexYearRec();
     calYrRec.setId(yr.getId());
     
      UserRec crUsr = userDM.getUserRecPvt(yr.getCreatedBy());
      calYrRec.setCreatedBy(crUsr);
      calYrRec.setCreatedOn(yr.getCreatedOn());
      calYrRec.setCalYear(yr.getCalYear());
      if(yr.getChangedBy() != null){
       UserRec chUsr = userDM.getUserRecPvt(yr.getChangedBy());
       calYrRec.setChangedBy(chUsr);
       calYrRec.setChangedOn(yr.getChangedOn());
      }
     
     return calYrRec;
    }
    private CalendarRuleMonth buildCalendarRuleMonth(CalendarRuleMonthRec rec, String pg){
     LOGGER.log(INFO, "buildCalendarRuleMonth called with {0} id {1}", new Object[]{rec, rec.getId()});
     CalendarRuleMonth f;
     boolean changed = false;
     boolean newCalMth =false;
     if(rec.getId() == null){
      f = new CalendarRuleMonth();
      User cUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
      f.setCreatedBy(cUsr);
      f.setCreatedOn(new Date());
      em.persist(f);
      AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'I', cUsr, pg);
      aud.setNewValue(f.getReference());
      newCalMth = true;
     }else{
      f = em.find(CalendarRuleMonth.class, rec.getId(), OPTIMISTIC);
     }
        
     if(newCalMth){
      f.setReference(rec.getReference());
      f.setDescription(rec.getDescription());
      f.setStartMonthNumber(rec.getStartMonthNumber());
      f.setMonthCal(rec.isMonthCal());
     }else{
      User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
      if(!f.getReference().equalsIgnoreCase(rec.getReference())){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_REF");
       aud.setNewValue(rec.getReference());
       aud.setOldValue(f.getReference());
       f.setReference(rec.getReference());
       changed = true;
      }
      if(!f.getDescription().equalsIgnoreCase(rec.getDescription())){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_DESCR");
       aud.setNewValue(rec.getDescription());
       aud.setOldValue(f.getDescription());
       f.setDescription(rec.getDescription());
       changed = true;
      }
      if(f.getStartMonthNumber() != rec.getStartMonthNumber()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_START_MNTH");
       aud.setNewValue(String.valueOf(rec.getStartMonthNumber()));
       aud.setOldValue(String.valueOf(f.getStartMonthNumber()));
       f.setStartMonthNumber(rec.getStartMonthNumber());
       changed = true;
      }
      if(rec.isMonthCal() != f.isMonthCal()){
       AuditCalRuleBase aud = this.buildAuditCalendarRule(f,'U', chUsr, pg);
       aud.setFieldName("FIS_CAL_MON");
       aud.setNewValue(String.valueOf(rec.isMonthCal()));
       aud.setOldValue(String.valueOf(f.isMonthCal()));
       f.setMonthCal(rec.isMonthCal());
       changed = true;
      }
      
      if(changed){
       f.setChangedBy(chUsr);
       f.setChangedOn(rec.getChangedOn());
      }
     }
     return f;
    }



/*     private CalendarRulePeriodLength buildCalendarRulePeriodLength(CalendarRulePeriodLenRec rec){
        CalendarRulePeriodLength f = new CalendarRulePeriodLength();
        Date current = new Date();
        f.setChangedOn(current);
        f.setCreatedOn(current);
        f.setReference(rec.getReference());
        f.setDescription(rec.getDescription());
        f.setId(rec.getId());
        f.setVersion(rec.getVersion());
        f.setPeriodLen(rec.getPeriodLen());
        Uom u = this.buildUom(rec.getUnitOfMeasure());
        f.setUnitOfMeasure(u);
        


        return f;
    } */

     private Uom buildUom(UomRec rec){
        Uom f = new Uom();
        f.setChangeDate(rec.getChangeDate());
        f.setCreateDate(rec.getCreateDate());
        f.setDescription(rec.getDescription());
        f.setId(rec.getId());
        f.setName(rec.getName());
        f.setProcessCode(rec.getProcessCode());
        f.setRevision(rec.getRevision());
        f.setUomCode(rec.getUomCode());

        return f;

    }

    private AuditAccountType buildAuditAccountType(AccountType acntType, String pg, User usr){
     AuditAccountType aud = new AuditAccountType();
     aud.setAccountType(acntType);
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     em.persist(aud);
     return aud;
    }
    
    private  AuditCalRuleBase buildAuditCalendarRule(CalendarRuleBase cal, char userAction,
            User usr, String pg){
     AuditCalRuleBase aud = new AuditCalRuleBase();
     aud.setAuditDate(new Date());
     aud.setUsrAction(userAction);
     aud.setCalRul(cal);
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     
     return aud;
    }
    
    private AuditCalRuleFlexPer buildAuditCalRuleFlexPer(CalendarFlexPer calPer,User usr,Date audDate, String pg){
     AuditCalRuleFlexPer aud = new AuditCalRuleFlexPer();
     aud.setAuditDate(audDate);
     aud.setCalPer(calPer);
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     em.persist(aud);
     return aud;
    }
    
    private AuditCalRuleFlexYr buildAuditCalRuleFlexYr(CalendarRuleFlexYear yr, User usr, String src,
            char userAction){
     AuditCalRuleFlexYr aud = new AuditCalRuleFlexYr();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setFlexYr(yr);
     aud.setSource(src);
     aud.setUsrAction(userAction);
     em.persist(aud);
     return aud;
    }
    private AuditFisPeriodRule buildAuditFisPeriodRule(FisPeriodRule periodRule, char usrAction,
            User usr, String pg){
     AuditFisPeriodRule aud = new AuditFisPeriodRule();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setUsrAction(usrAction);
     aud.setPerRule(periodRule);
     aud.setSource(pg);
     em.persist(aud);
     return aud;
    }
    
    private AuditPostType buildAuditPostType(PostType pstTy, char usrAction, User usr, String pg){
     AuditPostType aud = new AuditPostType();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setPostType(pstTy);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     
     return aud;
    }
    private AuditCompany buildAuditCompany(CompanyBasic comp, User usr, String pg){
     AuditCompany aud = new AuditCompany();
     aud.setAuditDate(new Date());
     aud.setComp(comp);
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     em.persist(aud);
     return aud;
    }
    
    private AuditCompApAr buildAuditCompApAr(CompanyApAr apAr,char usrAction, User usr, String pg){
     AuditCompApAr aud = new AuditCompApAr();
     aud.setApAr(apAr);
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setSource(pg);
     aud.setUsrAction(usrAction);
     em.persist(aud);
     return aud;
    }
    
    private AuditCompPostPer buildAuditCompPostPer(CompPostPer postPer, char usrAct, User usr, String pg){
     AuditCompPostPer aud = new AuditCompPostPer();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setCompPostingPeriod(postPer);
     aud.setSource(pg);
     aud.setUsrAction(usrAct);
     em.persist(aud);
     return aud;
    }
    
    private AuditChartOfAccounts buildAuditChartOfAccounts(ChartOfAccounts coa, char usrAction, User usr, String pg){
     AuditChartOfAccounts aud = new AuditChartOfAccounts();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setUsrAction(usrAction);
     aud.setCoa(coa);
     aud.setSource(pg);
     em.persist(aud);
     return aud;
    }
    
    private AuditRestrictedFund buildAuditRestrictedFund(Fund fnd, User usr, char userAct, String pg){
     AuditRestrictedFund aud = new AuditRestrictedFund();
     aud.setAuditDate(new Date());
     aud.setCreatedBy(usr);
     aud.setRestrFnd(fnd);
     aud.setSource(pg);
     aud.setUsrAction(userAct);
     em.persist(aud);
     return aud;
    }
    
    private AccountType buildAccountType(AccountTypeRec actTypeRec, String pg){
     AccountType actType;
     boolean createAction = false;
     boolean updated = false;
     if(actTypeRec.getId() == null){
      actType = new AccountType();
      User usr = em.find(User.class, actTypeRec.getCreatedBy().getId(), OPTIMISTIC);
      actType.setCreatedBy(usr);
      actType.setCreatedDate(actTypeRec.getCreatedDate());
      em.persist(actType);
      AuditAccountType aud = buildAuditAccountType(actType,pg,usr);
      aud.setNewValue(actTypeRec.getName());
      aud.setUsrAction('I');
      createAction = true;
     }else{
      actType = em.find(AccountType.class, actTypeRec.getId(), OPTIMISTIC);
     }
     
     if(createAction){
      actType.setControlAccount(actTypeRec.isControlAccount());
      actType.setDescription(actTypeRec.getDescription());
      actType.setName(actTypeRec.getName());
      actType.setProfitAndLossAccount(actTypeRec.isProfitAndLossAccount());
      actType.setSystemPost(actTypeRec.isSystemPost());
      actType.setDebit(actTypeRec.isDebit());
      Module module = em.find(Module.class, actTypeRec.getModule().getId(), OPTIMISTIC);
      actType.setModule(module);
      NumberRange numRng = em.find(NumberRange.class, actTypeRec.getNumberRange().getNumberControlId(), OPTIMISTIC);
      actType.setNumberRange(numRng);
      ProcessCode prCode = em.find(ProcessCode.class, actTypeRec.getProcessCode().getId(), OPTIMISTIC);
      actType.setProcessCode(prCode);
      actType.setRetainedEarn(actTypeRec.isRetainedEarn());
      Ledger led = em.find(Ledger.class, actTypeRec.getSubLedger().getId(), OPTIMISTIC);
      actType.setSubLedger(led);
       
     } else{
      User chUsr = em.find(User.class, actTypeRec.getChangedBy().getId(), OPTIMISTIC);
      if(actTypeRec.isControlAccount() != actType.isControlAccount()){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(String.valueOf(actTypeRec.isControlAccount()));
       aud.setOldValue(String.valueOf(actType.isControlAccount()));
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_CNTRL");
       actType.setControlAccount(actTypeRec.isControlAccount());
       updated = true;
      }
      if(actTypeRec.isDebit() != actType.isDebit()){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(String.valueOf(actTypeRec.isDebit()));
       aud.setOldValue(String.valueOf(actType.isDebit()));
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_DR");
       actType.setDebit(actTypeRec.isDebit());
       updated = true;
      }
      
      if(actTypeRec.isProfitAndLossAccount() != actType.isProfitAndLossAccount()){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(String.valueOf(actTypeRec.isProfitAndLossAccount()));
       aud.setOldValue(String.valueOf(actType.isProfitAndLossAccount()));
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_PL");
       actType.setProfitAndLossAccount(actTypeRec.isProfitAndLossAccount());
       updated = true;
      }
      if(actTypeRec.isRetainedEarn() != actType.isRetainedEarn()){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(String.valueOf(actTypeRec.isRetainedEarn()));
       aud.setOldValue(String.valueOf(actType.isRetainedEarn()));
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_RET_EARN");
       actType.setRetainedEarn(actTypeRec.isRetainedEarn());
       updated = true;
      }
      if(actTypeRec.isSystemPost() != actType.isSystemPost()){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(String.valueOf(actTypeRec.isSystemPost()));
       aud.setOldValue(String.valueOf(actType.isSystemPost()));
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_SYS_PST");
       actType.setSystemPost(actTypeRec.isSystemPost());
       updated = true;
      }
      if(!actTypeRec.getDescription().equalsIgnoreCase(actType.getDescription())){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getDescription());
       aud.setOldValue(actType.getDescription());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_DESCR");
       actType.setDescription(actTypeRec.getDescription());
       updated = true;
      }
      if(!Objects.equals(actTypeRec.getModule().getId(), actType.getModule().getId())){
       Module newMod = em.find(Module.class, actTypeRec.getModule().getId(), OPTIMISTIC);
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getModule().getModuleCode());
       aud.setOldValue(actType.getModule().getModuleCode());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_MOD");
       actType.setModule(newMod);
       updated = true;
      }
      if(!actTypeRec.getName().equalsIgnoreCase(actType.getName())){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getName());
       aud.setOldValue(actType.getName());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_NAME");
       actType.setDescription(actTypeRec.getDescription());
       updated = true;
      }
      if(!Objects.equals(actTypeRec.getNumberRange().getNumberControlId(), actType.getNumberRange().getNumberControlId())){
       NumberRange numRng = em.find(NumberRange.class, actTypeRec.getNumberRange().getNumberControlId(), OPTIMISTIC);
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getNumberRange().getShortDescr());
       aud.setOldValue(actType.getNumberRange().getShortDescr());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_NUM_RNG");
       actType.setNumberRange(numRng);
       updated = true;
      }
      if(!Objects.equals(actTypeRec.getProcessCode().getId(), actType.getProcessCode().getId())){
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getProcessCode().getName());
       aud.setOldValue(actType.getProcessCode().getName());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_PR_CD");
       ProcessCode prCode = em.find(ProcessCode.class, actTypeRec.getProcessCode().getId(), OPTIMISTIC);
       actType.setProcessCode(prCode);
       updated = true;
      }
      if(!Objects.equals(actTypeRec.getSubLedger().getId(), actType.getSubLedger().getId())){
       Ledger led = em.find(Ledger.class, actTypeRec.getSubLedger().getId(), OPTIMISTIC);
       AuditAccountType aud = buildAuditAccountType(actType,pg,chUsr);
       aud.setNewValue(actTypeRec.getSubLedger().getCode());
       aud.setOldValue(actType.getSubLedger().getCode());
       aud.setUsrAction('U');
       aud.setFieldName("ACT_TY_LED");
       actType.setSubLedger(led);
       updated = true;
      }
      if(updated){
       actType.setChangedBy(chUsr);
       actType.setChangedDate(new Date());
      }
     }
     
     return actType;
    }
    
    private AccountTypeRec buildAccountTypeRec(AccountType actType){
     AccountTypeRec actTypeRec = new AccountTypeRec();
     actTypeRec.setId(actType.getId());
     if(actType.getChangedBy() != null){
      UserRec chUsr = userDM.getUserRecPvt(actType.getChangedBy());
      actTypeRec.setChangedBy(chUsr);
      actTypeRec.setChangedDate(actType.getChangedDate());
     }
     UserRec crUsr = userDM.getUserRecPvt(actType.getCreatedBy());
     actTypeRec.setCreatedBy(crUsr);
     actTypeRec.setCreatedDate(actType.getCreatedDate());
     actTypeRec.setControlAccount(actType.isControlAccount());
     actTypeRec.setDebit(actType.isDebit());
     actTypeRec.setDescription(actType.getDescription());
     if(actType.getModule() != null){
     ModuleRec modRec = this.sysBuff.getModuleById(actType.getModule().getId());
     actTypeRec.setModule(modRec);
     }
     actTypeRec.setName(actType.getName());
     NumberRangeRec numRange = sysBuff.getNumRangeById(actType.getNumberRange().getId());
     actTypeRec.setNumberRange(numRange);
     if(actType.getProcessCode() != null){
     ProcessCodeRec procCode = sysBuff.getProcessCodeById(actType.getProcessCode().getId());
     actTypeRec.setProcessCode(procCode);
     }
     actTypeRec.setProfitAndLossAccount(actType.isProfitAndLossAccount());
     actTypeRec.setRetainedEarn(actType.isRetainedEarn());
     if(actType.getSubLedger() != null){
     LedgerRec led = sysBuff.getLedgerById(actType.getSubLedger().getId());
     actTypeRec.setSubLedger(led);
     }
     actTypeRec.setSystemPost(actType.isSystemPost());
     return actTypeRec;
     }
    
     /**
      * Converts business object into a DB entity object.
      * If this is not an update then a new DB entity is created
      * @param rec
      * @param update Is this an update to an existing DB record?
      * @return
      */
    
    private CompanyBasic buildCompanyBasic(CompanyBasicRec rec, String pg){
      LOGGER.log(INFO, "buidCompanyBasic called with {0}  ",
              new Object[] { rec.getReference()});
      LOGGER.log(INFO, "Comp legal type {0} period rule {1}",
            new Object[] {rec.getCompanyType(),rec.getPeriodRule()});
      
      boolean newComp = false;
      boolean changed = false;
      CompanyBasic f ;
      
      if(rec.getId() == null){
       User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
       f = new CompanyBasic();
       f.setCreatedBy(crUsr);
       f.setCreatedDate(new Date());
       em.persist(f);
       newComp = true;
       
      }else{
       f =  em.find(CompanyBasic.class, rec.getId());
      }
      
      if(newComp){
       LOGGER.log(INFO, "New comp address id {0}", rec.getAddress().getId());
       if(rec.getAddress().getId() == null || rec.getAddress().getId() == 0){
        // new Address
        AddressRec addrRec = rec.getAddress();
        addrRec.setCreatedBy(rec.getCreatedBy());
        addrRec.setCreatedBy(rec.getCreatedBy());
        Address adr = masterDataDM.createAddressPvt(addrRec,pg);
        f.setAddress(adr);
       }else{
        Address adr = em.find(Address.class, rec.getAddress().getId(), OPTIMISTIC);
        f.setAddress(adr);
       }
       f.setCharityNumber(rec.getCharityNumber());
       f.setCharityRegDate(rec.getCharityRegDate());
       ChartOfAccounts coa = em.find(ChartOfAccounts.class, rec.getChartOfAccounts().getId(), OPTIMISTIC);
       f.setChartOfAccounts(coa);
       FisPeriodRule fisPer = em.find(FisPeriodRule.class, rec.getPeriodRule().getId(), OPTIMISTIC);
       f.setPeriodRule(fisPer);
       f.setCompanyNumber(rec.getCompanyNumber());
       f.setCompanyType(rec.getCompanyType());
       if(rec.getCurrency() != null){
       Currency curr = em.find(Currency.class, rec.getCurrency().getId(), OPTIMISTIC);
       f.setCurrency(curr);
       }
       if(rec.getCountry() != null){
        Country cntry = em.find(Country.class, rec.getCountry().getId(), OPTIMISTIC);
        f.setCountry(cntry);
       }
       
       if(rec.getLocale() != null){
        String localStr = rec.getLocale().toLanguageTag();
        f.setLocaleCode(localStr);
       }
       f.setIncorporatedDate(rec.getIncorporatedDate());
       f.setLegalName(rec.getLegalName());
       f.setName(rec.getName());
       f.setNumber(rec.getReference());
       f.setRestrictedFunds(rec.isRestrictedFunds());
       f.setFundAccounting(rec.isFundAccounting());
       f.setBusinessArea(rec.isBusinessUnits());
       f.setCharity(rec.isCharity());
       f.setCorp(rec.isCorp());
       f.setVatReg(rec.isVatReg());
      }else{
       
       User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
       if((rec.getAddress() == null && f.getAddress() != null ) ||
          (rec.getAddress() != null && f.getAddress() == null ) || 
          (rec.getAddress() != null &    !Objects.equals(rec.getAddress().getId(), f.getAddress().getId()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_ADDR");
        aud.setNewValue(rec.getAddress().getAddrRef());
        if(f.getAddress() != null){
         aud.setOldValue(f.getAddress().getAddrRef());
        }
        aud.setUsrAction('U');
        Address newAddr = em.find(Address.class, rec.getAddress().getId(), OPTIMISTIC);
        f.setAddress(newAddr);
        changed = true;
       }
       LOGGER.log(INFO, "rec.getCharityNumber() {0}", rec.getCharityNumber());
       if((rec.getCharityNumber() == null && f.getCharityNumber() != null) ||
          (rec.getCharityNumber() != null && f.getCharityNumber() == null) ||    
          ( rec.getCharityNumber() != null && f.getCharityNumber() != null &&
               !rec.getCharityNumber().equalsIgnoreCase(f.getCharityNumber()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_CHAR_NUM");
        aud.setNewValue(rec.getCharityNumber());
        aud.setOldValue(f.getCharityNumber());
        aud.setUsrAction('U');
        f.setCharityNumber(rec.getCharityNumber());
        changed = true;
       }
       if((rec.getCharityRegDate() != null && f.getCharityRegDate() == null ) ||
          (rec.getCharityRegDate() == null && f.getCharityRegDate() != null ) ||
           (rec.getCharityRegDate() != null && !rec.getCharityRegDate().equals(f.getCharityRegDate()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_CHAR_REG_DATE");
        aud.setNewValue(rec.getCharityRegDate().toString());
        aud.setOldValue(f.getCharityRegDate().toString());
        aud.setUsrAction('U');
        f.setCharityRegDate(rec.getCharityRegDate());
        changed = true;
       }
       
       if(!Objects.equals(rec.getChartOfAccounts().getId(), f.getChartOfAccounts().getId())){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_COA");
        aud.setNewValue(rec.getChartOfAccounts().getReference());
        aud.setOldValue(f.getChartOfAccounts().getReference());
        aud.setUsrAction('U');
        ChartOfAccounts coa = em.find(ChartOfAccounts.class, rec.getChartOfAccounts().getId(), OPTIMISTIC);
        f.setChartOfAccounts(coa);
        changed = true;
       }
       if((rec.getCompanyNumber() == null && f.getCompanyNumber() != null) ||
          (rec.getCompanyNumber() != null && f.getCompanyNumber() == null) ||
          (rec.getCompanyNumber() != null && !rec.getCompanyNumber().equalsIgnoreCase(f.getCompanyNumber()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_NUM");
        aud.setNewValue(rec.getCompanyNumber());
        aud.setOldValue(f.getCompanyNumber());
        aud.setUsrAction('U');
        f.setCompanyNumber(rec.getCompanyNumber());
        changed = true;
       }
       
       if((rec.getCompanyType() == null && f.getCompanyType() != null) ||
          (rec.getCompanyType() != null && f.getCompanyType() == null) || 
          (rec.getCompanyType() != null && !rec.getCompanyType().equalsIgnoreCase(f.getCompanyType()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_TY");
        aud.setNewValue(rec.getCompanyType());
        aud.setOldValue(f.getCompanyType());
        aud.setUsrAction('U');
        f.setCompanyType(rec.getCompanyType());
        changed = true;
       }
       
       if((rec.getCurrency() == null && f.getCurrency() != null )||
          (rec.getCurrency() != null && f.getCurrency() == null )||
          (rec.getCurrency() != null && !Objects.equals(rec.getCurrency().getId(), f.getCurrency().getId()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_CCY_CODE");
        aud.setNewValue(rec.getCurrency().getCurrAlphaCode());
        aud.setOldValue(f.getCurrency().getCurrAlphaCode());
        aud.setUsrAction('U');
        Currency curr = em.find(Currency.class, rec.getCurrency().getId(), OPTIMISTIC);
        f.setCurrency(curr);
        changed = true;
       }
       
       if((rec.getCountry() == null && f.getCountry() != null)||
          (rec.getCountry() != null && f.getCountry() != null) ||
          (rec.getCountry() != null && 
           !Objects.equals(rec.getCountry().getId(), f.getCountry().getId()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_CCY_SYM");
        aud.setNewValue(rec.getCountry().getCountryCode2());
        aud.setOldValue(f.getCountry().getCountryCode2());
        aud.setUsrAction('U');
        Country cntry = em.find(Country.class, rec.getCountry().getId(), OPTIMISTIC);
        f.setCountry(cntry);
        changed = true;
       }
       
       if((rec.getPeriodRule() == null && f.getPeriodRule() != null)||
          (rec.getPeriodRule() != null && f.getPeriodRule() != null) ||
          (rec.getPeriodRule() != null && 
           !Objects.equals(rec.getPeriodRule().getId(), f.getPeriodRule().getId()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_PER_RULE");
        if(rec.getPeriodRule() != null){
        aud.setNewValue(rec.getPeriodRule().getPeriodRule());
        }else{
         aud.setNewValue(" ");
        }
        if(f.getPeriodRule() != null){
        aud.setOldValue(f.getPeriodRule().getPeriodRule());
        }else{
         aud.setOldValue(" ");
        }
        aud.setUsrAction('U');
        
        changed = true;
       }
       
       if((rec.getLocale() == null && f.getLocaleCode() != null) ||
          (rec.getLocale() != null && f.getLocaleCode() == null) ||
          (rec.getLocale() != null && !rec.getLocale().toLanguageTag().equalsIgnoreCase(f.getLocaleCode()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_LOC");
        if(rec.getLocale() != null){
         aud.setNewValue(rec.getLocale().toLanguageTag());
        }
        aud.setOldValue(f.getLocaleCode());
        aud.setUsrAction('U');
        f.setLocaleCode(rec.getLocale().toLanguageTag());
        changed = true;
        
      }
       if((rec.getIncorporatedDate() == null && f.getIncorporatedDate() != null) ||
         (rec.getIncorporatedDate() != null && f.getIncorporatedDate() == null) ||
         (rec.getIncorporatedDate() != null && !rec.getIncorporatedDate().equals(f.getIncorporatedDate()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_CORP_DT");
        aud.setNewValue(rec.getIncorporatedDate().toString());
        aud.setOldValue(f.getIncorporatedDate().toString());
        aud.setUsrAction('U');
        f.setIncorporatedDate(rec.getIncorporatedDate());
        changed = true;
       }
       if((rec.getLegalName() == null && f.getLegalName() != null)||
          (rec.getLegalName() != null && f.getLegalName() == null) ||
          (rec.getLegalName() != null && !rec.getLegalName().equalsIgnoreCase(f.getLegalName()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_LEG_NAME");
        aud.setNewValue(rec.getLegalName());
        aud.setOldValue(f.getLegalName());
        aud.setUsrAction('U');
        f.setLegalName(rec.getLegalName());
        changed = true;
       }
       if((rec.getName() == null && f.getName() != null) ||
          (rec.getName() != null && f.getName() == null) ||
          (rec.getName() != null && !rec.getName().equalsIgnoreCase(f.getName()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_TR_NAME");
        aud.setNewValue(rec.getName());
        aud.setOldValue(f.getName());
        aud.setUsrAction('U');
        f.setName(rec.getName());
        changed = true;
       }
       
       if((rec.getReference() == null && f.getNumber() != null) ||
          (rec.getReference() != null && f.getNumber() == null) ||
          (rec.getReference() != null && !rec.getReference().equalsIgnoreCase(f.getNumber()))){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_REF");
        aud.setNewValue(rec.getReference());
        aud.setOldValue(f.getNumber());
        aud.setUsrAction('U');
        f.setNumber(rec.getReference());
        changed = true;
       }
       
       if(rec.isRestrictedFunds() != f.isRestrictedFunds()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_RESTR_FND");
        aud.setNewValue(String.valueOf(rec.isRestrictedFunds()));
        aud.setOldValue(String.valueOf(f.isRestrictedFunds()));
        aud.setUsrAction('U');
        f.setRestrictedFunds(rec.isRestrictedFunds());
        changed = true;
       }
       
       if(rec.isFundAccounting() != f.isFundAccounting()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setFieldName("COMP_FND_ACNT");
        aud.setNewValue(String.valueOf(rec.isFundAccounting()));
        aud.setOldValue(String.valueOf(f.isFundAccounting()));
        aud.setUsrAction('U');
        f.setFundAccounting(rec.isFundAccounting());
        changed = true;
       }
       if(rec.isBusinessUnits() != f.isBusinessArea()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setAuditDate(new Date());
        aud.setFieldName("COMP_BUS_AREA");
        aud.setNewValue(String.valueOf(rec.isBusinessUnits()));
        aud.setOldValue(String.valueOf(f.isBusinessArea()));
        aud.setUsrAction('U');
        f.setBusinessArea(rec.isBusinessUnits());
        changed = true;
       }
       if(rec.isCharity() != f.isCharity()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setAuditDate(new Date());
        aud.setFieldName("COMP_CHARITY");
        aud.setNewValue(String.valueOf(rec.isCharity()));
        aud.setOldValue(String.valueOf(f.isCharity()));
        aud.setUsrAction('U');
        f.setCharity(rec.isCharity());
        changed = true;
       }
       if(rec.isCorp() != f.isCorp()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setAuditDate(new Date());
        aud.setFieldName("COMP_INCORP");
        aud.setNewValue(String.valueOf(rec.isCorp()));
        aud.setOldValue(String.valueOf(f.isCorp()));
        aud.setUsrAction('U');
        f.setCorp(rec.isCorp());
        changed = true;
       }
       
       if(rec.isVatReg() != f.isVatReg()){
        AuditCompany aud = this.buildAuditCompany(f, chUsr, pg);
        aud.setAuditDate(new Date());
        aud.setFieldName("COMP_VAT_REG");
        aud.setNewValue(String.valueOf(rec.isVatReg()));
        aud.setOldValue(String.valueOf(f.isVatReg()));
        aud.setUsrAction('U');
        f.setVatReg(rec.isVatReg());
        changed = true;
       }
       if(changed){
        f.setChangedBy(chUsr);
        f.setChangedDate(new Date());
       }
       
      }
       
       return f;
   
    }
  /**
   * Add linked elements when company created by copy
   * @param comp Company to be updated
   * @param rec Source for elements to be added
   * @param pg Web Page called from
   * @return 
   */
  private CompanyBasic buildCompanyBasicByCopy(CompanyBasic comp, CompanyBasicRec rec, String pg){
   LOGGER.log(INFO, "buildCompanyBasicByCopy called with new comp {0} source comp {1} ", 
           new Object[]{comp.getName(), rec.getName()});
   List<CompPostPerRec> compPostPerRecs = rec.getCompanyPostingPeriods();
   LOGGER.log(INFO, "Company posting periods {0}", trans);
   return comp;
  }

    
    public CompanyBasicRec buildCompanyBasicRecPvt(CompanyBasic rec){
      return this.buildCompanyBasicRec(rec);
    }
    
    private CompanyBasicRec buildCompanyBasicRec(CompanyBasic rec){
       CompanyBasicRec f = new CompanyBasicRec();
       //Date curr = new Date();
       if(rec.getAddress() != null){
           //Address addr = masterDataMgr.
           AddressRec addrRec = this.masterDataDM.getAddressRec(rec.getAddress());
           f.setAddress(addrRec);

       }
       f.setCompanyNumber(rec.getCompanyNumber());
       f.setCharityNumber(rec.getCharityNumber());
       f.setCharityRegDate(rec.getCharityRegDate());
       f.setCompanyType(rec.getCompanyType());
       f.setReference(rec.getNumber());
       f.setChangedDate(rec.getChangedDate());
       f.setChanges(rec.getChanges());
       if(rec.getChartOfAccounts() != null){
           ChartOfAccountsRec ch = this.buildChartOfAccountsRec(rec.getChartOfAccounts());
           f.setChartOfAccounts(ch);
       }
       
       if(rec.getPeriodControl() != null){
         List<PeriodControl> pcList = rec.getPeriodControl();
         ArrayList<PeriodControlRec> perCntrlList = new ArrayList<>();
         for(PeriodControl pc: pcList){
          PeriodControlRec pstPeriods = this.buildPeriodControlRec(f, pc);
          perCntrlList.add(pstPeriods);
         }

         f.setPostingPeriods(perCntrlList);
       }
       f.setCreatedDate(rec.getCreatedDate());
       f.setId(rec.getId());
       f.setName(rec.getName());
       f.setLegalName(rec.getLegalName());
       f.setIncorporatedDate(rec.getIncorporatedDate());
       f.setVatReg(rec.isVatReg());
       f.setVatNumber(rec.getVatNumber());
       f.setDefaultCompany(rec.isDefaultCompany());
       if(rec.getLocaleCode() != null){
        Locale loc = Locale.forLanguageTag(rec.getLocaleCode());
        f.setLocale(loc);
       }
       if(rec.getCurrency() != null){
        CurrencyRec currRec = masterDataDM.buildCurrencyRecPvt(rec.getCurrency());
        f.setCurrency(currRec);
       }
       
       if(rec.getCountry() != null){
        CountryRec countryRec = masterDataDM.buildCountryRecPvt(rec.getCountry());
        f.setCountry(countryRec);
        f.setLocale(countryRec.getLocale());
        }
       
       
       if(rec.getCompRestrictedFunds() != null){
        ArrayList<FundRec> recFunds = new ArrayList<>();
        List<Fund> funds = rec.getCompRestrictedFunds();
        ListIterator<Fund> fundsLi = funds.listIterator();
        while(fundsLi.hasNext()){
         Fund fund = fundsLi.next();
         FundRec fundRec = this.buildRestrictedFundRec(f, fund);
         recFunds.add(fundRec);
        }
        f.setFundList(recFunds);
        
       }
       f.setRestrictedFunds(rec.isRestrictedFunds());
       f.setFundAccounting(rec.isFundAccounting());
       if(rec.getCurrency() != null){
        CurrencyRec currRec = this.masterDataDM.buildCurrencyRecPvt(rec.getCurrency());
        f.setCurrency(currRec);
       }
       if(rec.getCountry() != null){
        CountryRec cntryRec = masterDataDM.buildCountryRecPvt(rec.getCountry());
        f.setCountry(cntryRec);
       }
       
       return f;
    }
    
    private CompPostPer buildCompPostPer(CompPostPerRec postPerRec, String pg){
     boolean newPostPer = false;
     boolean changePostPer = false;
     CompPostPer postPer ;
     if(postPerRec.getId() == null){
      postPer = new CompPostPer();
      User crUsr = em.find(User.class, postPerRec.getCreatedBy().getId(), OPTIMISTIC);
      postPer.setCreatedBy(crUsr);
      postPer.setCreatedDate(postPerRec.getCreatedDate());
      em.persist(postPer);
      AuditCompPostPer aud = this.buildAuditCompPostPer(postPer, 'I', crUsr, pg);
      aud.setNewValue(postPerRec.getComp().getReference() + "_"+postPerRec.getLedger().getCode());
      newPostPer = true;
     }else{
      postPer = em.find(CompPostPer.class, postPerRec.getId(), OPTIMISTIC);
     }
     
     if(newPostPer){
      CompanyBasic comp = em.find(CompanyBasic.class, postPerRec.getComp().getId(), OPTIMISTIC);
      postPer.setComp(comp);
      Ledger led = em.find(Ledger.class, postPerRec.getLedger().getId(), OPTIMISTIC);
      postPer.setLedger(led);
      postPer.setEndLong(postPerRec.getEndLong());
      postPer.setEndPer(postPerRec.getEndPer());
      postPer.setEndYear(postPerRec.getEndYear());
      postPer.setStartLong(postPerRec.getStartLong());
      postPer.setStartPer(postPerRec.getStartPer());
      postPer.setStartYear(postPerRec.getStartYear());
     }else{
      // changed ?
      User chUsr = em.find(User.class, postPerRec.getChangedBy().getId(), OPTIMISTIC);
      
      if((postPerRec.getComp() == null && postPer.getComp() != null) ||
         (postPerRec.getComp() != null && postPer.getComp() == null) || 
         (postPerRec.getComp() != null && !Objects.equals(postPerRec.getComp().getId(), postPer.getComp().getId()))){
       AuditCompPostPer aud = this.buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_COMP");
       aud.setNewValue(postPerRec.getComp().getReference());
       aud.setOldValue(postPer.getComp().getNumber());
       CompanyBasic comp = em.find(CompanyBasic.class, postPerRec.getComp().getId(), OPTIMISTIC);
       postPer.setComp(comp);
       changePostPer = true;
      }
      
      if((postPerRec.getLedger() == null && postPer.getLedger() != null) ||
         (postPerRec.getLedger() != null && postPer.getLedger() == null) || 
         (postPerRec.getLedger() != null && !Objects.equals(postPerRec.getLedger().getId(), postPer.getLedger().getId()))){
       AuditCompPostPer aud = buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_LED");
       aud.setNewValue(postPerRec.getLedger().getCode());
       aud.setOldValue(postPer.getLedger().getCode());
       Ledger led = em.find(Ledger.class, postPerRec.getLedger().getId(), OPTIMISTIC);
       postPer.setLedger(led);
       changePostPer = true;
      }
      
      if(postPerRec.getEndPer() != postPer.getEndPer()){
       AuditCompPostPer aud = buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_END_PER");
       aud.setNewValue(String.valueOf(postPerRec.getEndPer()));
       aud.setOldValue(String.valueOf(postPer.getEndPer()));
       postPer.setEndPer(postPerRec.getEndPer());
       changePostPer = true;
      }
      if(postPerRec.getEndYear() != postPer.getEndYear()){
       AuditCompPostPer aud = buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_END_YR");
       aud.setNewValue(String.valueOf(postPerRec.getEndYear()));
       aud.setOldValue(String.valueOf(postPer.getEndYear()));
       postPer.setEndYear(postPerRec.getEndYear());
       changePostPer = true;
      }
      if(postPerRec.getStartPer() != postPer.getStartPer()){
       AuditCompPostPer aud = buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_ST_PER");
       aud.setNewValue(String.valueOf(postPerRec.getStartPer()));
       aud.setOldValue(String.valueOf(postPer.getStartPer()));
       postPer.setStartPer(postPerRec.getStartPer());
       changePostPer = true;
      }
      if(postPerRec.getStartYear() != postPer.getStartYear()){
       AuditCompPostPer aud = buildAuditCompPostPer(postPer, 'U', chUsr, pg);
       aud.setFieldName("POST_PER_ST_YR");
       aud.setNewValue(String.valueOf(postPerRec.getStartYear()));
       aud.setOldValue(String.valueOf(postPer.getStartYear()));
       postPer.setStartYear(postPerRec.getStartYear());
       changePostPer = true;
      }
      if(changePostPer){
       postPer.setChangedBy(chUsr);
       postPer.setChangedDate(postPerRec.getChangedDate());
      }
     }
     
     return postPer;
    }
    
    private CompPostPerRec buildCompPostPerRec(CompPostPer postPer){
     CompPostPerRec rec = new CompPostPerRec();
     rec.setId(postPer.getId());
     UserRec crUsr = this.userDM.getUserRecPvt(postPer.getCreatedBy());
     rec.setCreatedBy(crUsr);
     rec.setCreatedDate(postPer.getCreatedDate());
     if(postPer.getChangedBy() != null){
      UserRec chUsr = userDM.getUserRecPvt(postPer.getChangedBy());
      rec.setChangedBy(chUsr);
      rec.setChangedDate(postPer.getChangedDate());
     }
     if(postPer.getComp() != null){
      CompanyBasicRec comp = this.sysBuff.getCompanyById(postPer.getComp().getId());
      rec.setComp(comp);
     }
     if(postPer.getLedger() != null){
      LedgerRec led = this.sysBuff.getLedgerById(postPer.getLedger().getId());
      rec.setLedger(led);
     }
     rec.setEndLong(postPer.getEndLong());
     rec.setEndPer(postPer.getEndPer());
     rec.setEndYear(postPer.getEndYear());
     rec.setStartLong(postPer.getStartLong());
     rec.setStartPer(postPer.getStartPer());
     rec.setStartYear(postPer.getStartYear());
     return rec;
    }
    
    public CompanyDM() {
        LOGGER.log(INFO,"CompanyDM initialised");
    }

    
    public void saveChartOfAccounts(ChartOfAccountsRec rec, String pg) throws BacException {
      trans.begin();
      ChartOfAccounts coa = this.buildChartOfAccounts(rec,  pg);
      if(rec.getId() == null){
       AuditChartOfAccounts aud = new  AuditChartOfAccounts();
       aud.setAuditDate(new Date());
       User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
       aud.setCreatedBy(crUsr);
       aud.setNewValue(rec.getReference());
       aud.setCoa(coa);
       aud.setSource(pg);
       aud.setUsrAction('I');
       em.persist(aud);
      }
      trans.commit();
    }
/**
 * Saves a new instance of Fiscal Period control basic details
 * @param rec Fiscal Period control object populated in client
  * @param pg
  * @return 
 * @throws BacException
 */
    public FisPeriodRuleRec createFiscalPeriod(FisPeriodRuleRec rec, String pg) throws BacException {
        LOGGER.log(INFO, "DM createFiscalPeriodBasic called with: {0}", rec);
        trans.begin();
        FisPeriodRule f = buildFisPeriodRule(rec, pg);
        rec.setId(f.getId());
        LOGGER.log(INFO, pg, trans);
        LOGGER.log(INFO, "entity cal basis {0}", rec.isCalendarMonthBasis());
        if(rec.isCalendarMonthBasis()){
         CalendarRuleMonthRec calmthRec = (CalendarRuleMonthRec)rec.getCalendarRule();
         CalendarRuleMonth calMonth = this.buildCalendarRuleMonth(calmthRec,pg);
         f.setCalRule(calMonth);
        }else if(rec.isFixedDayOfMonthBasis()){
         CalendarRuleFixedDateRec domCalRuleRec = (CalendarRuleFixedDateRec)rec.getCalendarRule();
         CalendarRuleFixedDate domCalRule = this.buildCalendarRuleFixedDate(domCalRuleRec, pg);
         f.setCalRule(domCalRule);
        }else if(rec.isAnnualDateScheduleBasis()){
         CalendarRuleBaseRec calRuleRec = rec.getCalendarRule();
         CalendarRuleBase calRule = buildCalendarRuleBase(calRuleRec, pg);
         List<CalendarRuleFlexYearRec> yrRecs = calRuleRec.getCalRuleFlexYears();
         List<CalendarRuleFlexYear> yrs = new ArrayList<>();
         for(CalendarRuleFlexYearRec calYrRec:yrRecs){
          List<CalendarRuleFlexPerRec> flexPerRecs = calYrRec.getFlexPeriods();
          List<CalendarFlexPer> flexPers = new ArrayList<>();
          CalendarRuleFlexYear calyr = this.buildCalendarRuleFlexYear(calYrRec, pg);
          for(CalendarRuleFlexPerRec flexPerRec: flexPerRecs){
           CalendarFlexPer calPer = this.buildCalendarRuleFlexPer(flexPerRec, pg);
           calPer.setCalRuleFlexYr(calyr);
           flexPers.add(calPer);
          }
          calyr.setFlexPeriods(flexPers);
         // calyr.setCalRule(calRule);
          yrs.add(calyr);
          
         }
        // calRule.setCalRuleFlexYears(yrs);
         f.setCalRule(calRule);
        
        }
     
     trans.commit();
     
     return rec;
    }
    
    
    
/**
 * Returns an ArrayList  list of all Fiscal period rules. Actually get the period rules from the DB
 * @return
 * @throws BacException
 */
    public ArrayList<FisPeriodRuleRec> getFisPeriodRules() throws BacException {
        LOGGER.log(INFO, "DB getFisPeriodRules");
        ArrayList<FisPeriodRuleRec> retLst= new ArrayList<>();
        if(!trans.isActive()){
         trans.begin();
        }
        Query q = em.createNamedQuery("findAllPeriodRules");
        
            List rlst = q.getResultList();
            LOGGER.log(INFO, "List from DB", rlst);
            ListIterator it = rlst.listIterator();
            while(it.hasNext()){
             Object obj = it.next();
             FisPeriodRule recDB = em.find(FisPeriodRule.class, obj, OPTIMISTIC);
             FisPeriodRuleRec rec = this.buildFisPeriodRuleRec(recDB);
             retLst.add(rec);
            }
        trans.commit();
        LOGGER.log(INFO, "DB returns list size {0}", retLst.size());
        return retLst;
    }

 public FisPeriodRuleRec getFisPerCalRule(FisPeriodRuleRec fisPerRec){
  LOGGER.log(INFO, "getFisPerCalRule called with rule id {0}", fisPerRec.getId());
  FisPeriodRule fisPer = em.find(FisPeriodRule.class, fisPerRec.getId(), OPTIMISTIC);
  
  CalendarRuleBase calRule = fisPer.getCalRule();
  
  
  if(fisPer.isAnnualDateScheduleBasis()){
   CalendarRuleBaseRec calRuleRec = this.buildCalendarRuleBaseRec(calRule);
   
//   List<CalendarRuleFlexYear> years = calRule.getCalRuleFlexYears();
   List<CalendarRuleFlexYearRec> yearRecs = new ArrayList<>();
  /* for(CalendarRuleFlexYear yr:years){
    CalendarRuleFlexYearRec yrRec = this.buildCalendarRuleFlexYearRec(yr);
    List<CalendarFlexPer> periods = yr.getFlexPeriods();
    List<CalendarRuleFlexPerRec> perRecs = new ArrayList<>();
    for(CalendarFlexPer per:periods){
     CalendarRuleFlexPerRec perRec = this.buildCalendarRuleFlexPerRec(per);
     perRecs.add(perRec);
    }
    yrRec.setFlexPeriods(perRecs);
    LOGGER.log(INFO, "Periods for year {0}", perRecs);
    yearRecs.add(yrRec);
   }
   */
   LOGGER.log(INFO, "Cal rule years {0}", yearRecs);
   calRuleRec.setCalRuleFlexYears(yearRecs);
   fisPerRec.setCalendarRule(calRuleRec);
  }
  LOGGER.log(INFO, "calRule {0}", calRule);
  return fisPerRec;
 }
 
 public ChartOfAccountsRec getFisPeriodRuleRecForCoa(ChartOfAccountsRec coaRec){
  if(!trans.isActive()){
      trans.begin();
  }
  ChartOfAccounts coa = em.find(ChartOfAccounts.class, coaRec.getId(), OPTIMISTIC);
  FisPeriodRule fpr = coa.getPeriodRule();
  FisPeriodRuleRec fprRec = this.buildFisPeriodRuleRec(fpr);
  coaRec.setPeriodRule(fprRec);
  LOGGER.log(INFO, "CompDM.getFisPeriodRuleRecForCoa return coa with fisPer {0}", fprRec);
  
  return coaRec;
 }
    public void saveFisCalRule(CalendarRuleBaseRec rec, String type, String pg) throws BacException {
        if(type.equalsIgnoreCase("cal")){
            CalendarRuleMonth cal = this.buildCalendarRuleMonth((CalendarRuleMonthRec)rec,pg);
          try{
              em.persist(cal);
              
              }catch(EntityExistsException e){
                LOGGER.log(INFO, "Create Fis Rule Entity exists error", e.getMessage());
                throw new BacException(e.getLocalizedMessage());
              }catch(IllegalArgumentException e){
                LOGGER.log(INFO, "Create Fis Rule Object type error", e.getMessage());
                throw new BacException(e.getLocalizedMessage());
              }catch(TransactionRequiredException e){
                LOGGER.log(INFO, "Create Fis Rule transaction error", e.getMessage());
                throw new BacException(e.getLocalizedMessage());
              }catch(PersistenceException e){
                LOGGER.log(INFO, "Create Fis Rule Other persistence  error");
                throw new BacException(e.getLocalizedMessage());
              }
        }else if(type.equalsIgnoreCase("periodLen")){

        }
    }

 public void createFiscalCalendarRuleMonth(CalendarRuleMonthRec rec, String pg) throws BacException {
  CalendarRuleMonth cal = this.buildCalendarRuleMonth(rec,pg);
  try{
   em.persist(cal);
  }catch(EntityExistsException e){
   LOGGER.log(INFO, "Create Fis Rule Entity exists error", e.getMessage());
   throw new BacException(e.getLocalizedMessage());
  }catch(IllegalArgumentException e){
   LOGGER.log(INFO, "Create Fis Rule Object type error", e.getMessage());
   throw new BacException(e.getLocalizedMessage());
  }catch(TransactionRequiredException e){
   LOGGER.log(INFO, "Create Fis Rule transaction error", e.getMessage());
   throw new BacException(e.getLocalizedMessage());
  }catch(PersistenceException e){
   LOGGER.log(INFO, "Create Fis Rule Other persistence  error");
   throw new BacException(e.getLocalizedMessage());
  }
 }

 public FundRec getFundForCodeComp(String code, Long compId){
  LOGGER.log(INFO, "getFundForCodeComp code {0} comp id {1}", new Object[]{ code,compId});
  Query q = em.createNamedQuery("fndByCodeComp");
  q.setParameter("code", code);
  q.setParameter("compId", compId);
  try{
   Fund fnd = (Fund)q.getSingleResult();
   CompanyBasicRec comp = this.sysBuff.getCompanyById(compId);
   FundRec fndRec = this.buildRestrictedFundRec(comp, fnd);
   return fndRec;
  }catch(NonUniqueResultException ex){
   LOGGER.log(INFO, "duplicate funds found: [0}", ex.getLocalizedMessage());
   return null;
  }catch(NoResultException ex){
   LOGGER.log(INFO, "Fund not found : {0}", ex.getLocalizedMessage());
   return null;
  }
 }
  
 public ArrayList<FisPeriodRuleRec> getPeriodRules() {
  return periodRules;
 }

 public void setPeriodRules(ArrayList<FisPeriodRuleRec> periodRules) {
  this.periodRules = periodRules;
 }

    
    public FisPeriodRuleRec getPeriodRuleById(long id) throws BacException {
     FisPeriodRuleRec rec;
        //Long idRef = new Long(id);
     try{
      FisPeriodRule entity = em.find(FisPeriodRule.class, id);
      LOGGER.log(INFO,"Rules from DB {0}",entity);
      if(entity == null){
       LOGGER.log(INFO, "Found Rule from DB");
       throw new BacException("Could not find period rule with ID: "+ id);
      }
      rec = this.buildFisPeriodRuleRec(entity);
     }catch(IllegalArgumentException e){
      LOGGER.log(INFO, "IllegalArgumentException thrown with: {0}", e.getLocalizedMessage());
      throw new BacException("Object type invalid!!!");
     }

     return rec;
    }

    public CalendarRuleBaseRec getCalendarRuleByRef(String ref) throws BacException{
     LOGGER.log(INFO, "getCalendarRuleByRef called with ref {0}", ref);
     CalendarRuleBaseRec rec = null;
     Query q = em.createNamedQuery("calRuleByRef");
     q.setParameter("ref", ref);
     
     try{
     CalendarRuleBase calRule = (CalendarRuleBase)q.getSingleResult();
     LOGGER.log(INFO, "calRule class {0}", calRule.getClass().getSimpleName());
     String className = calRule.getClass().getSimpleName();
     if(className.equals("CalendarRuleBase"))
      rec = this.buildCalendarRuleBaseRec(calRule);
     
     }catch(NoResultException ex){
      LOGGER.log(INFO, "Cal base rule not found {0}", ref);
      return null;
     }catch(NonUniqueResultException ex){
      LOGGER.log(WARNING, "Duplicate Calendar rule: {0}", ref);
      
     }
     
     
     return rec;
    }
 /*   public ArrayList<CalendarRuleBaseRec> getCalendarRulesByType(int type) throws BacException {
        ArrayList<CalendarRuleBaseRec> recs = new ArrayList<CalendarRuleBaseRec>();
        if(type == 1){
            // natural calendar
            try{
                Query q = em.createNamedQuery("getAllMthCalRules");
                List l = q.getResultList();
                ListIterator it = l.listIterator();
                while(it.hasNext()){
                    CalendarRuleMonth e = (CalendarRuleMonth)it.next();
                    CalendarRuleBaseRec rec = this.buildCalendarRuleBaseRec(e);
                    recs.add(rec);

                }
                LOGGER.log(INFO, "number of calendar rules retuned", recs.size());

            }catch(IllegalArgumentException e){
                throw new BacException(e.getLocalizedMessage());
            }catch(IllegalStateException e){
                throw new BacException("query type incorrect");
            }catch(QueryTimeoutException e ){
                throw new BacException("Query time out statement rolled back");
            }catch(TransactionRequiredException e){
                throw new BacException("Lock set but not transaction");
            }catch(PessimisticLockException e){
                throw new BacException("Could not set lock whole transaction rolled back");
            }catch(LockTimeoutException e){
                throw new BacException("Could not set lock whole statement rolled back");
            }catch(PersistenceException e){
                throw new BacException("Other Database error");
            }


        }
        return recs;
    } */

    /*public FisPeriodRuleRec updatePeriodRule(FisPeriodRuleRec perRuleRec, UString pg){
     LOGGER.log(INFO, "updatePeriodRule called with rule id {0}", perRuleRec);
     LOGGER.log(INFO, "perRuleRec changed by {0}", perRuleRec.getChangeBy().getId());
     if(!trans.isActive()){
      trans.begin();
     }
     // if no longer flexible then delete years
     if(perRuleRec.getCalBasisOption() != 3 && perRuleRec.getCalendarRule().getCalRuleFlexYears() != null){
      LOGGER.log(INFO, "Delete years");
      for(CalendarRuleFlexYearRec yr: perRuleRec.getCalendarRule().getCalRuleFlexYears() ){
       CalendarRuleFlexYear yrDb = em.find(CalendarRuleFlexYear.class, yr.getId(), OPTIMISTIC);
       for(CalendarRuleFlexPerRec per: yr.getFlexPeriods()){
        CalendarFlexPer perDb = em.find(CalendarFlexPer.class, per.getId(), OPTIMISTIC);
        ListIterator<AuditCalRuleFlexPer> audLi = perDb.getAuditRecords().listIterator();
        while(audLi.hasNext()){
         em.remove(audLi.next());
        }
        em.remove(perDb);
       }
       em.remove(yrDb);
      }
      perRuleRec.getCalendarRule().setCalRuleFlexYears(null);
     } 
     
     FisPeriodRule perRule = this.buildFisPeriodRule(perRuleRec, pg);
     LOGGER.log(INFO, "rec cal rule id {0} db cal rule id {1}", 
             new Object[]{perRuleRec.getCalendarRule().getId(), perRule.getCalRule().getId()});
     if(perRuleRec.getCalendarRule().getId() != perRule.getCalRule().getId()){
      User chUsr = em.find(User.class, perRuleRec.getChangeBy().getId());
      AuditFisPeriodRule aud = this.buildAuditFisPeriodRule(perRule,'U',chUsr , pg);
      aud.setFieldName("FIS_PER_CAL_RULE");
      aud.setNewValue(perRuleRec.getCalendarRule().getReference());
      aud.setOldValue(perRule.getCalRule().getReference());
      aud.setUsrAction('U');
      em.persist(aud);
      if(perRuleRec.isCalendarMonthBasis()){
       CalendarRuleMonth calMth = 
               em.find(CalendarRuleMonth.class, perRuleRec.getCalendarRule().getId(), OPTIMISTIC);
       perRule.setCalRule(calMth);
      }else if(perRuleRec.isFixedDayOfMonthBasis()){
       CalendarRuleFixedDate calDay = 
         em.find(CalendarRuleFixedDate.class, perRuleRec.getCalendarRule().getId(), OPTIMISTIC);
       perRule.setCalRule(calDay);
      }else if(perRuleRec.isAnnualDateScheduleBasis()){
       CalendarRuleBase calBase =
         em.find(CalendarRuleBase.class, perRuleRec.getCalendarRule().getId(), OPTIMISTIC);
       perRule.setCalRule(calBase);
      }
      
     }
     trans.commit();
    return perRuleRec;
    }
    */
    public FisPeriodRuleRec updatePeriodRuleCalYearPeriods(FisPeriodRuleRec perRuleRec, UserRec usr,String pg){
     LOGGER.log(INFO, "updatePeriodRuleCalYears called with {0}", perRuleRec);
     if(!trans.isActive()){
      trans.begin();
     }
     
     
     Date procDate = new Date();
     FisPeriodRule perRule = em.find(FisPeriodRule.class, perRuleRec.getId(), OPTIMISTIC);
     CalendarRuleBase calRule = perRule.getCalRule();
//     List<CalendarRuleFlexYear> years = calRule.getCalRuleFlexYears();
    // ListIterator<CalendarRuleFlexYear> yearsLi = years.listIterator();
     List<CalendarRuleFlexYearRec> yearRecs = perRuleRec.getCalendarRule().getCalRuleFlexYears();
     if(yearRecs != null){
      for(CalendarRuleFlexYearRec yrRec: yearRecs){
       LOGGER.log(INFO, "Process yrRec id {0}", yrRec.getId());
        if(yrRec.getId() == null){
         yrRec.setCreatedBy(usr);
         yrRec.setCreatedOn(procDate);
        }else{
         yrRec.setChangedBy(usr);
         yrRec.setChangedOn(procDate);
        }
        CalendarRuleFlexYear yr = buildCalendarRuleFlexYear(yrRec, pg);
        if(yrRec.getId() == null){
         yrRec.setId(yr.getId());
//         yr.setCalRule(calRule);
        }
        LOGGER.log(INFO, "flex year from DB id {0}", yr);
        List<CalendarFlexPer> calPers =  yr.getFlexPeriods();
        if(calPers == null){
         calPers = new ArrayList<>();
        }
        List<CalendarRuleFlexPerRec> perRecs = yrRec.getFlexPeriods();
        ListIterator<CalendarRuleFlexPerRec> perRecLi = perRecs.listIterator();
        while(perRecLi.hasNext()){
         CalendarRuleFlexPerRec perRec = perRecLi.next();
         if(perRec.getId() == null){
          perRec.setCreatedBy(usr);
          perRec.setCreatedOn(procDate);
          
         }else{
          perRec.setChangedBy(usr);
          perRec.setChangedOn(procDate);
         }
         CalendarFlexPer calPer = buildCalendarRuleFlexPer(perRec, pg);
         if(perRec.getId() == null) {
          perRec.setId(calPer.getId());
          perRecLi.set(perRec);
         }
         calPer.setCalRuleFlexYr(yr);
         calPers.add(calPer);
         
        }
        yr.setFlexPeriods(calPers);
        boolean foundYr = false;
        /*
        while(yearsLi.hasNext() && ! foundYr){
         CalendarRuleFlexYear currYr = yearsLi.next();
         if(currYr.getId() == yr.getId()){
          yearsLi.add(yr);
          foundYr = true;
         }
        }
        */
        if(!foundYr){
        //  years.add(yr);
        } 
       
      }
     }
     
     trans.commit();
     return perRuleRec;
    }
/**
 * Updates period rule in the data base
 * @param periodRule
  * @param src
  * @return 
 * @throws BacException
 */
    public FisPeriodRuleRec updatePeriodRule(FisPeriodRuleRec periodRule, String src) throws BacException {
     LOGGER.log(INFO, "upDatePeriodRule in database called with {0}", periodRule.getId());
     LOGGER.log(INFO, "calendar rule is {0}",periodRule.getCalendarRule());
     if(!trans.isActive()){
      trans.begin();
     }
     FisPeriodRule rule = this.buildFisPeriodRule(periodRule, src);
     LOGGER.log(INFO, "buildFisPeriodRule returns id {0}", rule.getId());
     CalendarRuleBaseRec calRuleBase = periodRule.getCalendarRule();
     if(calRuleBase != null){
      CalendarRuleBase cal = em.find(CalendarRuleBase.class, calRuleBase.getId(), OPTIMISTIC);
      rule.setCalRule(cal);
      LOGGER.log(INFO, "Cal id {0}", rule.getCalRule().getId());
     }
     
     if(periodRule.getId() == null){
      periodRule.setId(rule.getId());
     }
     LOGGER.log(INFO, "periodRule to return has id {0}", periodRule.getId());
     trans.commit();
     return periodRule;
     
    }
    
    public int fisPeriodRuleCopyComp(CompanyBasicRec c1,CompanyBasicRec c2, 
            UserRec user, String pg){
     LOGGER.log(INFO, "CompDM.fisPeriodRuleCopyComp called with comp {} and {1} "
             + "source per rule {2} dest per rule {3}", new Object[]{c1.getReference(), c2.getReference(),
              c1.getPeriodRule(), c2.getPeriodRule() });
     
     if(!trans.isActive()){
      trans.begin();
     }
     FisPeriodRule perR = em.find(FisPeriodRule.class, c1.getPeriodRule().getId());
     CompanyBasic c = sysBuff.getComp(c2);
     c.setPeriodRule(perR);
     if(c.getPeriodRule().getId() != null){
      User usr = em.find(User.class, user.getId());
      AuditCompany aud = this.buildAuditCompany(c, usr, pg);
      aud.setFieldName("COMP_FISC_PER");
      aud.setNewValue(perR.getPeriodRule());
      trans.commit();
      return CompanyManager.PER_RULE_UPDATED;
     }else{
      trans.rollback();
      return CompanyManager.PER_RULE_UPDATE_FAIL;
     }
     
    }
  public int fundCopyComp(CompanyBasicRec c1,CompanyBasicRec c2, 
            UserRec userRec, String pg){
   LOGGER.log(INFO,"compDM called with comp {0} and {1}", new Object[]{
    c1.getReference(), c2.getReference()});
   if(!trans.isActive()){
    trans.begin();
   }
   TypedQuery qC2 = em.createNamedQuery("fundsByComp", Fund.class);
   qC2.setMaxResults(1);
   qC2.setParameter("compId", c2.getId());
   List<Fund> rs = qC2.getResultList();
   if(!rs.isEmpty()){
    return CompanyManager.ATTR_IN_DEST_COMP;
   }
   TypedQuery qC1 = em.createNamedQuery("fundsByComp", Fund.class);
   qC1.setParameter("compId", c1.getId());
   rs = qC1.getResultList();
   if(rs.isEmpty()){
    return CompanyManager.ATTR_NOT_IN_SRC_COMP;
   }
   User usr = em.find(User.class, userRec.getId());
   CompanyBasic comp2 = em.find(CompanyBasic.class, c2.getId());
   int numFunds = 0;
   
   for(Fund f:rs){
    Fund curr = new Fund();
    curr.setCreatedBy(usr);
    curr.setCreatedOn(new Date());
    curr.setFndCode(f.getFndCode());
    curr.setFundCategory(f.getFundCategory());
    curr.setFundForComp(comp2);
    curr.setName(f.getName());
    curr.setPurpose(f.getPurpose());
    curr.setReturnDue(f.getReturnDue());
    curr.setReturnRequired(f.isReturnRequired());
    curr.setValidFrom(f.getValidFrom());
    curr.setValidTo(f.getValidTo());
    em.persist(curr);
    AuditRestrictedFund aud = this.buildAuditRestrictedFund(f, usr, 'I', pg);
    aud.setNewValue(curr.getFndCode());
    numFunds++;
   }
   
   if(numFunds == 0){
    trans.rollback();
    return CompanyManager.ATTR_NOT_CREATED;
   }else{
    trans.commit();
    return CompanyManager.ATTR_CREATED;
   }
  }
  
    public List<AccountTypeRec> getAccountTypes(){
     LOGGER.log(INFO, "Comp DM getAccountTypes called ");
     List<AccountTypeRec> retList = new ArrayList<>();
     Query q = em.createNamedQuery("allaccountTypes");
     List rs = q.getResultList();
     ListIterator rsLi = rs.listIterator();
     while(rsLi.hasNext()){
      AccountType acTy = (AccountType)rsLi.next();
      AccountTypeRec acTyRec = this.buildAccountTypeRec(acTy);
      retList.add(acTyRec);
     }
     return retList;
    }
    
    public CalendarRuleBaseRec getCalendarRuleFlexYears(CalendarRuleBaseRec rec){
     
     CalendarRuleBase calBase = em.find(CalendarRuleBase.class, rec.getId(), OPTIMISTIC);
//     List<CalendarRuleFlexYear> years = calBase.getCalRuleFlexYears();
/*     
if(years != null){
      List<CalendarRuleFlexYearRec> recYears = new ArrayList<>();
      for(CalendarRuleFlexYear yr:years){
       CalendarRuleFlexYearRec yrRec = this.buildCalendarRuleFlexYearRec(yr);
       List<CalendarFlexPer> pers = yr.getFlexPeriods();
       if(pers != null && !pers.isEmpty()){
        List<CalendarRuleFlexPerRec> recPers = new ArrayList<>();
        for(CalendarFlexPer per:pers){
         CalendarRuleFlexPerRec recPer = this.buildCalendarRuleFlexPerRec(per);
         recPers.add(recPer);
        }
        yrRec.setFlexPeriods(recPers);
       }
       
       recYears.add(yrRec);
      }
      rec.setCalRuleFlexYears(recYears);
     }
     */
     return rec;
    }
    public CalendarRuleFlexYearRec getCalendarRuleFlexYearRec(String ruleRef, int year) 
            throws BacException{
     LOGGER.log(INFO, "compDM getCalendarRuleFlexYearRec called with rule {0} year {1}", new Object[]{
      ruleRef,year});
     CalendarRuleFlexYearRec flexYr = null;
     
     Query q = em.createNamedQuery("flexYrByRuleRefYear");
     q.setParameter("ruleRef", ruleRef);
     q.setParameter("yr", year);
     try{
      CalendarRuleFlexYear ruleYr = (CalendarRuleFlexYear)q.getSingleResult();
      LOGGER.log(INFO, "ruleYr found {0}", ruleYr);
      flexYr = this.buildCalendarRuleFlexYearRec(ruleYr);
     }catch(NoResultException ex){
      LOGGER.log(INFO, "Flex year not found rule {0} year {1}", new Object[]{ruleRef,year});
      return null;
     }catch(NonUniqueResultException ex){
      LOGGER.log(INFO, "Multiple flex year for rule{0} year {1}", new Object[]{ruleRef,year});
      throw new BacException("Duplicate entry","perCalFlxYrDup");
      
     }
     return flexYr;
    }
    
    public CalendarRuleBaseRec getCalendarRuleById(int id) throws BacException {
        LOGGER.log(INFO, "DB getCalendarRuleById called with id: {0}", id);

         CalendarRuleBase cal =   em.find(CalendarRuleBase.class, id);
         if(cal == null){
             throw new BacException("Calendar rule not found");
         }
         CalendarRuleBaseRec rec = this.buildCalendarRuleBaseRec(cal);

        LOGGER.log(INFO, "DB getCalendarRuleById returns {0}",cal);
        return rec;
    }
    
  public List<CalendarRuleBaseRec> getCalendarRules(){
   
   List<CalendarRuleBaseRec> retList = new ArrayList<>();
   Query q = em.createNamedQuery("getAllCalRules");
   List rs = q.getResultList();
   ListIterator li = rs.listIterator();
   while(li.hasNext()){
    CalendarRuleBase rule = (CalendarRuleBase)li.next();
    String className = rule.getClass().getSimpleName();
    LOGGER.log(INFO, "Calendar class {0}", className);
    switch (className) {
     case "CalendarRuleBase":
      CalendarRuleBaseRec baseRec = this.buildCalendarRuleBaseRec(rule);
      retList.add(baseRec);
      break;
     case "CalendarRuleFixedDate":
      CalendarRuleFixedDateRec dateRec = this.buildCalendarRuleFixedDateRec((CalendarRuleFixedDate)rule);
      retList.add(dateRec);
      break;
     case "CalendarRuleMonth":
      CalendarRuleMonthRec monthRec = this.buildCalendarRuleMonthRec((CalendarRuleMonth)rule);
      retList.add(monthRec);
      break;
    }
   }
   if(retList.isEmpty()){
    return null;}
   else{
    return retList;
   }
  } 
/**
 * Actually gets the chart of accounts list from the database
 * @return
 * @throws BacException
 */
    public ArrayList<ChartOfAccountsRec> getChartsOfAccounts() throws BacException {
        LOGGER.log(INFO, "getChartsOfAccounts in DB");
       ArrayList<ChartOfAccountsRec> chartLst = new ArrayList<>();
        try{
            Query q = em.createNamedQuery("getAllChartsOfAccounts");
            List l = q.getResultList();
            
            ListIterator li = l.listIterator();
            while(li.hasNext()){
                ChartOfAccounts c = (ChartOfAccounts)li.next();
                LOGGER.log(INFO, "DB chart ref found {0}", c.getReference());
                LOGGER.log(INFO, "DB chart id found {0}", c.getId());
                
                ChartOfAccountsRec chartRec = this.buildChartOfAccountsRec(c);
                LOGGER.log(INFO, "Rec chart ref found {0}", chartRec.getReference());
                chartLst.add(chartRec);
                //return chartLst;
            }

        }catch(IllegalArgumentException e){
            throw new BacException("Query not found");
        }catch(IllegalStateException e){
            throw new BacException("Invalid query");
        }catch(QueryTimeoutException e){
            throw new BacException("Database time out statement rollback");
        }catch(TransactionRequiredException e){
            throw new BacException("Transaction missing");
        }catch(PessimisticLockException e){
            throw new BacException("data base lock error - transaction rolled back");
        }catch(LockTimeoutException e){
            throw new BacException("Data base lock error - statement rolled back");
        }catch(PersistenceException e){
            throw new BacException("Other database error");
        }
        LOGGER.log(INFO, "charts returned {0}", chartLst.size());
        return chartLst;
    }

    public ChartOfAccountsRec getChartOfAccountsByRef(String ref) throws BacException{
     ChartOfAccountsRec rec;
     
     Query q = em.createNamedQuery("coaByRef");
     q.setParameter("ref", ref);
     try{
      ChartOfAccounts coa = (ChartOfAccounts)q.getSingleResult();
      LOGGER.log(INFO, "Found coa {0}", coa.getReference());
      rec = this.buildChartOfAccountsRec(coa);
      return rec;
     }catch(NonUniqueResultException ex){
      throw new BacException("Duplicate od COA","coaDupl");
     }catch(NoResultException ex){
      LOGGER.log(INFO, "Chart of accounts ref {0} not found", ref);
      return null;
     }
     
     
    }
    public void updateChartOfAccounts(ChartOfAccountsRec rec) throws BacException {
        LOGGER.log(INFO, "updateChartOfAccounts");
        ChartOfAccounts chart;
        FisPeriodRule rule;
        Date current = new Date();

        try{
           chart = em.find(ChartOfAccounts.class, rec.getId());
           if(chart == null){
               throw new BacException("Could not find chart: "+rec.getReference());
           }
           if(chart.getChanges() != rec.getChanges()){
               throw new BacException("Updated by a different user please repeat the update");
           }

           rule = em.find(FisPeriodRule.class, rec.getPeriodRule().getId());
           if(rule == null){
               throw new BacException("Could not find Fiscal Period rule: "+rec.getPeriodRule().getPeriodRule());
           }
           // update DB copy with values from front end
           chart.setChangedDate(current);
           chart.setDefaultChart(rec.isDefaultChart());
           chart.setName(rec.getName());
           chart.setOibalFwd(rec.isDefaultChart());
           chart.setReference(rec.getReference());

           chart.setPeriodRule(rule);


        }catch(IllegalArgumentException e){
            throw new BacException("Chart of account not found: "+e.getLocalizedMessage());
        }
    }

    public AccountTypeRec accountTypeUpdate(AccountTypeRec rec, String pg){
     LOGGER.log(INFO, "CompDM.accountTypeUpdate called with {0} ", rec);
     AccountType ty = this.buildAccountType(rec, null);
     return rec;
    }
    
 public void coaAddComp(CompanyBasicRec compRec1, CompanyBasicRec compRec2, 
         UserRec usrRec, String pg){
  LOGGER.log(INFO, "coaAddComp called with comp {0} comp {1} ", new Object[]{
   compRec1.getReference(), compRec2.getReference()});
  if(!trans.isActive()){
   trans.begin();
  }
  CompanyBasic comp1 = em.find(CompanyBasic.class, compRec1.getId());
  CompanyBasic comp2 = em.find(CompanyBasic.class, compRec1.getId());
  ChartOfAccounts coa = comp1.getChartOfAccounts();
  List<CompanyBasic> comps = coa.getCompanies();
  if(comps == null || comps.isEmpty()){
   LOGGER.log(INFO, "No companies with chart so cant copy");
   return;
  }
  LOGGER.log(INFO, "Companies associated with chart {0}", comps);
  
  boolean comp1Found = false;
  boolean comp2Found = false;
  ListIterator<CompanyBasic> compLi = comps.listIterator();
  while(compLi.hasNext()  ){
   
   CompanyBasic curr = compLi.next();
   LOGGER.log(INFO, "curr comp id {0} comp 1 id {1} comp id {2}", new Object[]{
    curr.getId(), comp1.getId(), comp2.getId()});
   LOGGER.log(INFO, pg, trans);
   if(Objects.equals(curr.getId(), comp1.getId())){
    comp1Found = true;
   }
   if(Objects.equals(curr.getId(), comp2.getId())){
    comp2Found = true;
   }
  }
  LOGGER.log(INFO, "Comp 1 found {0} comp 2 found {1}", new Object[]{comp1Found,comp2Found});
  if(!comp1Found){
   LOGGER.log(INFO, "Source company not found in chart");
   return;
  }
  if(comp2Found){
   LOGGER.log(INFO, "Dest company already  in chart");
   return;
  }
  boolean updatedComp = false;
  compLi = comps.listIterator();
  while(compLi.hasNext() && !updatedComp){
   CompanyBasic curr = compLi.next();
   if(Objects.equals(curr.getId(), comp1.getId())){
    comp2.setChartOfAccounts(coa);
    updatedComp = true;
   }
  }
  
   
   if(updatedComp){
    User usr = em.find(User.class, usrRec.getId());
    AuditCompany aud = this.buildAuditCompany(comp2, usr, pg);
    aud.setFieldName("GL_COMP_AC_COA_AC");
    aud.setNewValue(coa.getName());
    aud.setUsrAction('U');
    trans.commit();
    LOGGER.log(INFO, "Comp {0} has chart {1}", new Object[]{comp2.getNumber(),coa.getReference()});
   } else{
    LOGGER.log(INFO, "Comp {0} not updated", comp2.getNumber());
    trans.rollback();
   }
  
  
 }
    public ChartOfAccountsRec coaUpdate(ChartOfAccountsRec rec, UserRec usr, String pg){
     LOGGER.log(INFO, "coaUpdate called with rec {0} user id {1} page {2}", 
             new Object[]{rec,usr.getId(),pg});
     if(!trans.isActive()){
      trans.begin();
     }
     ChartOfAccounts coa = buildChartOfAccounts(rec,  pg);
     LOGGER.log(INFO, "coa after DB update {0}", coa.getId());
     if(rec.getId() == null){
      rec.setId(coa.getId());
     }
     trans.commit();
     return rec;
    }
    
    public boolean companyRefValid(String ref){
     boolean compRefValid = true;
     
     Query q = em.createNamedQuery("compsByRef");
     q.setParameter("compRef", ref);
     List rs = q.getResultList();
     ListIterator li = rs.listIterator();
     while(li.hasNext()){
      CompanyBasic comp = (CompanyBasic)li.next();
      if(comp.getNumber().equalsIgnoreCase(ref)){
       return false;
      }
     }
     
     
     return compRefValid;
    }
    public AccountTypeRec createAccountType(AccountTypeRec accountTypeRec, String pg){
     if(!trans.isActive()){
      trans.begin();
     }
     AccountType actType = buildAccountType(accountTypeRec, pg);
     accountTypeRec.setId(actType.getId());
     trans.commit();
     return accountTypeRec;
    }
    
 public CompanyBasicRec createCompany(CompanyBasicRec rec, UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "DB Create company called with: {0}", rec);
  try{
   trans.begin();
      //User actUser = em.find(User.class, usr.getId(), OPTIMISTIC);
   CompanyBasic comp = buildCompanyBasic(rec,pg);
      
   rec.setId(comp.getId());
   sysBuff.setCompany(rec);
   //trans.rollback();
   em.flush();
   trans.commit();
   return rec;
  }catch(BacException e){
      throw new BacException(e.getLocalizedMessage());
  }
 }
 
 public CompanyBasicRec createCompanyWithRef(CompanyBasicRec compRec,  String pg){
  LOGGER.log(INFO, "CompDM create comp with ref new {0} ", compRec.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  
  CompanyBasic comp = buildCompanyBasic(compRec,pg);
  comp = buildCompanyBasicByCopy(comp, compRec, pg);
  LOGGER.log(INFO, "New comp id {0}", comp.getId());
  if(comp.getId() == null){
   trans.rollback();
  }else{
   compRec.setId(comp.getId());
   trans.commit();
  }
  
  return compRec;
 }

   public List<CompPostPerRec> getCompPostPerByComp(CompanyBasicRec comp){
    
    boolean foundPostPer = false;
    List<CompPostPerRec> retList = new ArrayList<>();
    Query q = em.createNamedQuery("postPerByComp");
    q.setParameter("compId", comp.getId());
    
    List rs = q.getResultList();
    for(Object o: rs){
     CompPostPer postPer = (CompPostPer)o;
     if(Objects.equals(postPer.getComp().getId(), comp.getId())){
      CompPostPerRec rec = this.buildCompPostPerRec(postPer);
      retList.add(rec);
      foundPostPer = true;
     }
     
    }
    if(foundPostPer){
     return retList;
    }else{
     return null;
    }
   }
   
   public CompPostPerRec getCompPostPerByCompLed(CompanyBasicRec comp, LedgerRec led){
    
    Query q = em.createNamedQuery("postPerByCompLed");
    q.setParameter("compId", comp.getId());
    q.setParameter("ledId", led.getId());
    
    try{
    CompPostPer postPer = (CompPostPer)q.getSingleResult();
    CompPostPerRec rec = this.buildCompPostPerRec(postPer);
    return rec;
    }catch(NoResultException ex){
     LOGGER.log(INFO, "Post Per not found comp id {0} led id {1}", 
             new Object[]{comp.getId(),led.getId()});
     return null;
    }catch(NonUniqueResultException ex){
     LOGGER.log(INFO, "Multiple Post Pers found comp id {0} led id {1}", 
             new Object[]{comp.getId(),led.getId()});
     return null;
    }
    
    //CompPostPer postPer
    
    //CompPostPer postPer
    
    
   }
  
   /**
    * Get Company plus all list child object in  single action
    * @param compRec company to fill with child objects
    * @param glAcntLoad Load GL accounts
    * @return 
    */
   public CompanyBasicRec getCompFull(CompanyBasicRec compRec, boolean glAcntLoad){
    LOGGER.log(INFO, "getCompFull called with comp {0}", compRec);
    if(!trans.isActive()){
     trans.begin();
    }
    TypedQuery q = em.createNamedQuery("compById", CompanyBasic.class);
    q.setParameter("id", compRec.getId());
    try{
    CompanyBasic c = (CompanyBasic)q.getSingleResult();
    
    ChartOfAccounts ch = c.getChartOfAccounts();
    LOGGER.log(INFO, "Chart found from Comp DB {0}", ch);
    
    
    ChartOfAccountsRec acntsChart = sysBuff.getChartOfAccountsbyId(ch.getId());
    
    LOGGER.log(INFO, "acntsChart for comp {0}",acntsChart);
    compRec.setChartOfAccounts(acntsChart);
    
    FisPeriodRule perRule = c.getPeriodRule();
    FisPeriodRuleRec perRuleRec = sysBuff.getFisPeriodRule(perRule.getId());
    LOGGER.log(INFO, "Period rule from sys buff {0}", perRuleRec);
    compRec.setPeriodRule(perRuleRec);
    
    
    List<BankAccountCompany> banks = c.getBankAccounts();
    if(!banks.isEmpty()){
     List<BankAccountCompanyRec> bankRecs = new ArrayList<>(); 
     for(BankAccountCompany b:banks){
      BankAccountCompanyRec bRec = bankDM.getBankAccountCompany(b);
      bankRecs.add(bRec);
     }
     compRec.setBankAccounts(bankRecs);
    }
    
    LOGGER.log(INFO, "c banks {0}" , banks.size());
    if(glAcntLoad){
     List<FiGlAccountComp> glAcnts = c.getGlAccountComps();
     if(glAcnts.isEmpty()){
      LOGGER.log(INFO, "No GL accounts found");
     }else{
      LOGGER.log(INFO, "Number of GL accounts found {0}", glAcnts.size());
      List<FiGlAccountCompRec> glAcntRecs = new ArrayList<>();
      for(FiGlAccountComp glAcnt:glAcnts){
       FiGlAccountCompRec glAcntRec = this.flMastRecDM.buildFiCompGlAccountRecPvt(glAcnt);
       glAcntRec.setId(null);
       glAcntRec.setCompany(compRec);
       glAcntRecs.add(glAcntRec);
      }
      compRec.setGlAccounts(glAcntRecs);
     }
     
    }
    }catch(NoResultException e){
     LOGGER.log(INFO, "Comp not found");
     return null;
    }
  
    
    
    trans.rollback();
    return compRec;
   }
  /**
   * Should only be called by system buffer
  * @param comp
   * @return
   * @throws BacException 
   */
    public CompanyBasic getCompPvt(CompanyBasicRec comp) throws BacException{
     LOGGER.log(INFO, "comDB.getCompPvt() called with comp id {0}",comp.getId());
     if(!trans.isActive()){
      trans.begin();
     }
     CompanyBasic compDB = em.find(CompanyBasic.class, comp.getId(), OPTIMISTIC);
     LOGGER.log(INFO, "comp from DB {0}",compDB);
     trans.commit();
     return compDB;
     
    }
    public ArrayList<CompanyBasicRec> getCompanies()  {
    
    ArrayList<CompanyBasicRec> companies = new ArrayList<>();
    try{
      TypedQuery q = em.createNamedQuery("allCompanies",CompanyBasic.class);
      List<CompanyBasic> compLst = q.getResultList();
      
      ListIterator<CompanyBasic> li = compLst.listIterator();
      while(li.hasNext()){
        CompanyBasic comp = li.next();
        CompanyBasicRec compRec = this.buildCompanyBasicRec(comp);
        
        companies.add(compRec);
      }

    }catch(IllegalArgumentException e){
      throw new BacException("Could not find get companies query");
    }
    return companies;
  }
    
  public CompanyApArRec updateCompArApRec(CompanyApArRec rec, String pg){
   LOGGER.log(INFO, "CompDM.updateArApRec called");
   CompanyApAr apAr = buildCompanyArAp(rec, pg);
   if(rec.getId() == null){
    rec.setId(apAr.getId());
   }
   
   
   return rec;
  }
  public CalendarRuleBaseRec updateCalendarRule(CalendarRuleBaseRec rule, String pg){
  LOGGER.log(INFO, "updateCalendarRule called with rule{0}", 
          rule.getClass().getSimpleName());
  if(!trans.isActive()){
   trans.begin();
  }
  
 String className = rule.getClass().getSimpleName();
 
 LOGGER.log(INFO, "className {0} natural cal {1} month cal {2} flex cal  {3}", new Object[]{className,
  rule.isNaturalCal(),rule.isMonthCal(),rule.isFlexCal()});
 
 
 
 if(className.equals("CalendarRuleFixedDateRec")){
  CalendarRuleFixedDate r = this.buildCalendarRuleFixedDate((CalendarRuleFixedDateRec) rule, pg);
  if(rule.getId() == null){
   rule.setId(r.getId());
  }
  LOGGER.log(INFO, "Created fixed date calendar with id {0}", rule.getId());
 }else if(rule.isNaturalCal()){
  LOGGER.log(INFO, "Build natural cal ");
  CalendarRuleBase calRule = this.buildCalendarRuleBase(rule, pg);
  if(rule.getId() == null){
   rule.setId(calRule.getId());
  }
  LOGGER.log(INFO, "Created natural Calendar id {0}", rule.getId());
 } else if(rule.isMonthCal()){
  LOGGER.log(INFO, "Build cal month rule");
  CalendarRuleMonth ruleMth = this.buildCalendarRuleMonth((CalendarRuleMonthRec)rule, pg);
  if(rule.getId() == null){
   rule.setId(ruleMth.getId());
  }
 } else if(rule.isFlexCal()){
  LOGGER.log(INFO, "Build flexible rule");
  CalendarRuleBase calRule = this.buildCalendarRuleBase(rule, pg);
  if(rule.getId() == null){
   rule.setId(calRule.getId());
  }
   List<CalendarRuleFlexYearRec> recYears = rule.getCalRuleFlexYears();
   if(recYears != null &&  !recYears.isEmpty()){
    // years have been created
    ListIterator<CalendarRuleFlexYearRec> yearLi = recYears.listIterator();
    while(yearLi.hasNext()){
     
     CalendarRuleFlexYearRec recYr = yearLi.next();
     recYr.setCalRule(rule);
     CalendarRuleFlexYear yr = buildCalendarRuleFlexYear(recYr, pg);
     if(recYr.getId() == null){
      recYr.setId(yr.getId());
     }
     List<CalendarRuleFlexPerRec> recPers = recYr.getFlexPeriods();
     if(recPers != null && !recPers.isEmpty()){
      ListIterator<CalendarRuleFlexPerRec> perLi = recPers.listIterator();
      while(perLi.hasNext()){
       CalendarRuleFlexPerRec perRec = perLi.next();
       perRec.setCalRuleFlexYr(recYr);
       CalendarFlexPer per = this.buildCalendarRuleFlexPer(perRec, pg);
       if(perRec.getId() == null){
        perRec.setId(per.getId());
       }
       perLi.set(perRec);
      }
     }
     recYr.setFlexPeriods(recPers);
    }
   }
 }
  
  
  trans.commit();
  return rule;
 }
 public CalendarRuleFlexPerRec updateCalendarRuleFlexPeriod(CalendarRuleFlexPerRec rule, 
         String pg){
  LOGGER.log(INFO, "compDM.updateCalendarRuleFlexYear called with rule {0} ", rule);
  if(!trans.isActive()){
   trans.begin();
  }
  
  
   CalendarFlexPer yrRule = this.buildCalendarRuleFlexPer(rule, pg);
   if(yrRule.getId() == null){
    rule.setId(yrRule.getId());
    
   }
  trans.commit();
  return rule;
 }
 

 
 public CalendarRuleFlexYearRec updateCalendarRuleFlexYear(CalendarRuleFlexYearRec rule, 
         String pg){
  LOGGER.log(INFO, "compDM.updateCalendarRuleFlexYear called with rule {0} ", rule);
  if(!trans.isActive()){
   trans.begin();
  }
  
  
   CalendarRuleFlexYear yrRule = this.buildCalendarRuleFlexYear(rule, pg);
   if(rule.getId() == null){
    rule.setId(yrRule.getId());
    if(rule.getCalRule().getId() == null){
//     rule.getCalRule().setId(yrRule.getCalRule().getId());
    }
   }
  trans.commit();
  return rule;
 }
/**
 * Update the company passed in. This should only be called by a manager level EJB. 
 * The caller should catch exceptions for error messages
 * @param comp Company to be update
  * @param usr
  * @param pg
 * @return status summary true if no errors
 * @throws BacException
 */
  public CompanyBasicRec updateCompany(CompanyBasicRec comp, UserRec usr, String pg) throws BacException {
    LOGGER.log(INFO, "updateCompanyCentral called with company: {0}", comp.getReference());
    LOGGER.log(INFO, "Comp legal type {0} period rule {1}",
            new Object[] {comp.getCompanyType(),comp.getPeriodRule()});
    if(!trans.isActive()){
     trans.begin();
    }
    CompanyBasic compDb = this.buildCompanyBasic(comp,pg);
    comp.setId(compDb.getId());
    trans.commit();
    /*if(comp.isVatReg()){
     VatRegistrationRec vatReg = comp.getVatRegDetails();
     VatRegistration vatRegDB = vatDM.addVatRegistrationPvt(compDb, vatReg, usr, pg);
     vatReg.setId(vatRegDB.getId());
     LOGGER.log(INFO, "CompanyDM has vat reg id {0}", vatRegDB);
     List<VatRegSchemeRec> schemes = vatReg.getRegSchemes();
     schemes = vatDM.vatRegSchemeUpdatePvt(vatRegDB, schemes, usr, pg);
     vatReg.setRegSchemes(schemes);
     comp.setVatRegDetails(vatReg);
     LOGGER.log(INFO, "comp vatReg id ", comp.getVatRegDetails().getId());
     LOGGER.log(INFO, "Vat Registrations {0}", compDb.getVatRegistrations());
     List<VatRegistrationRec> regs = comp.getVatRegistrations();
     if(regs == null){
      regs = new ArrayList<VatRegistrationRec>();
     }
     regs.add(vatReg);
     comp.setVatRegistrations(regs);
    }
    */
    
    return comp;
  }

  public CompPostPerRec updateCompPostPerRec(CompPostPerRec rec, String pg){
   LOGGER.log(INFO, "compDM.updateCompPostPerRec called with  {0}", rec);
   if(!trans.isActive()){
    trans.begin();
   }
   CompPostPer postPer = buildCompPostPer(rec, pg);
   if(rec.getId() == null){
    rec.setId(postPer.getId());
   }
   if(rec.getId() == null){
    trans.rollback();
   }else{
    trans.commit();
   }
   return rec;
  }
  public ArrayList<FiGlAccountCompRec> getGlAccountsForCompany(CompanyBasicRec company)
          throws BacException {
   if(!trans.isActive()){
    trans.begin();
   } 
   ArrayList<FiGlAccountCompRec> glAccountsLst = new ArrayList<>();
    try{
      //CompanyBasic comp = em.find(CompanyBasic.class, company.getId());
      //List<FiGlAccountComp> accountList = comp.getGlAccountComps();
      Query q = em.createNamedQuery("GlAccountCompByCompId");
      q.setParameter("compId", company.getId());
      try{
      List rs = q.getResultList();
      
      ListIterator li = rs.listIterator();
     //logger.log(INFO, "GL accounts found for company {0}", accountList);
      //ListIterator<FiGlAccountComp> li = accountList.listIterator();
      while(li.hasNext()){
        //Long glAcntId = (Long)li.next();
        //logger.log(INFO, "glAcntId {0}", glAcntId);
       FiGlAccountComp glAcDb = (FiGlAccountComp)li.next();
        //FiGlAccountComp glAcDb = em.find(FiGlAccountComp.class, glAcntId, OPTIMISTIC);
        LOGGER.log(INFO,"Comp GL Account id {0}",glAcDb.getId());
        LOGGER.log(INFO,"Chart of accounts Account   {0}",glAcDb.getCoaAccount());
        LOGGER.log(INFO,"chart of accounts class name {0}",(glAcDb.getCoaAccount()).getClass().getName());
        LOGGER.log(INFO, "GL Account id {0} Account type {1}", new Object[]{glAcDb.getId(),
         (glAcDb.getCoaAccount()).getClass().getName()});
        
        FiGlAccountCompRec glAcRec =  flMastRecDM.buildFiCompGlAccountRecPvt(glAcDb);
        glAccountsLst.add(glAcRec);
      } 
      }catch(Exception ex){
       
       LOGGER.log(INFO, "Get company accounts exception {0}", ex.getLocalizedMessage());
      }
      return glAccountsLst;
    }catch(IllegalArgumentException ex){
      throw new BacException("Company wrong type: ");
    }finally{
     LOGGER.log(INFO, "In finally block");
     trans.rollback();
    }
    
  }

  public void saveRestrictedFundForComp(FundRec fund, int fyr, String pg)
          throws BacException {
    LOGGER.log(INFO, "saveRestrictedFundForComp called with Fund {0} and year {1}",
            new Object[]{fund, fyr});
    try{
     if(!trans.isActive()){
      trans.begin();
     }
      Fund fnd = buildRestrictedFund(fund,pg);
      
      // Add restricted balances to GL accounts assigned to company
     List<FiGlAccountComp> compGlAcs = fnd.getFundForComp().getGlAccountComps();

     int periods = fnd.getFundForComp().getChartOfAccounts().getPeriodRule().getNumPeriods();
     ListIterator<FiGlAccountComp> it = compGlAcs.listIterator();
     while(it.hasNext()){
       FiGlAccountComp compAc = it.next();
       LOGGER.log(INFO, "Add balance to account number {0}", compAc.getCompany().getNumber());
       for(int i=0; i < periods;i++ ){
         LOGGER.log(INFO, "Add restricted balance for period: ", i);
         FiPeriodBalance rBal = new FiPeriodBalance();
         rBal.setBalPeriod(i);
         rBal.setBalYear(fyr);
         rBal.setBalanceType(1);
         rBal.setCreated(fnd.getCreatedOn());
         rBal.setCreatedBy(fnd.getCreatedBy());
         rBal.setFiGlAccountComp(compAc);

         List<FiPeriodBalance> rBals = compAc.getRestrictedBalances();
         rBals.add(rBal);
         try{
           em.persist(rBal);
         }catch(EntityExistsException ex){
           throw new BacException("Restricted Bal already exists in DB: ");
         }catch(IllegalArgumentException | TransactionRequiredException ex){
           throw new BacException("Restricted Bal not DB type: ");
         }
       }

     }
     trans.commit();
    }catch(IllegalArgumentException ex){
      throw new BacException("Company Create fund no company found COMP:FND01");

    }

  }
/**
 * Returns all restricted funds that exist
 * @return
 * @throws BacException/**
 * Returns all restricted funds that exist
 * @return
 * @throws BacException
 */
 
/**
 * returns the restricted funds are assigned the company
 * @param comp
 * @return
 * @throws BacException
 */
  public List<FundRec> getRestrictedFundForComp(CompanyBasicRec comp)
          throws BacException {
    LOGGER.log(INFO, "CompDM getRestrictedFundForComp ref {0}", comp.getReference());
    ArrayList<FundRec> fundList = new ArrayList<>();
    try{
      CompanyBasic compDB = em.find(CompanyBasic.class,comp.getId());
      List<Fund> rFundsLst = compDB.getCompRestrictedFunds();
      ListIterator<Fund> rFundsIt = rFundsLst.listIterator();
      while(rFundsIt.hasNext()){
        Fund f = rFundsIt.next();
        FundRec fundRec = buildRestrictedFundRec(comp,f);
        fundList.add(fundRec);
      }

    }catch(IllegalArgumentException ex){
      throw new BacException("not db company {0}",ex.getLocalizedMessage());
    }
    LOGGER.log(INFO, "Fund List to return {0}", fundList);
    return fundList;
  }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
/**
 * Returns a list of VAT registrations for a company
 * @param comp
 * @return
 * @throws BacException 
 */
 public List<VatRegistrationRec> getVatRegistrationsForCompany(CompanyBasicRec comp) 
         throws BacException {
  List<VatRegistrationRec> vatRegRecs = new ArrayList<>();
  CompanyBasic compDB = em.find(CompanyBasic.class, comp.getId(), OPTIMISTIC);
  List<VatRegistration> vatRegs = compDB.getVatRegistrations();
  ListIterator<VatRegistration> li = vatRegs.listIterator();
  while(li.hasNext()){
   VatRegistration reg = li.next();
   VatRegistrationRec rec = vatDM.buildVatRegistrationRecPvt(reg);
   vatRegRecs.add(rec);
  }
  return vatRegRecs;
 }

 public CompanyBasicRec getCompArAP(CompanyBasicRec compRec){
  LOGGER.log(INFO, "CompDM.getCompArAP called with comp id (0}", compRec.getId());
  if(compRec.getCompApAr() != null){
   LOGGER.log(INFO, "Comapny allready has AR / AP settings");
   return compRec;
  }
  
  CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId());
  if(comp == null){
   LOGGER.log(INFO, "company could not be found in DB id {0}", compRec.getId());
   return compRec;
  }
  
  CompanyApAr apArSett = comp.getCompApAr();
  LOGGER.log(INFO, "DB apAr settings id {0}", apArSett.getId());
  CompanyApArRec apArSettRec = this.buildCompanyArAp(apArSett);
  LOGGER.log(INFO, "after build rec version {0}", apArSett.getId());
  apArSettRec.setComp(compRec);
  compRec.setCompApAr(apArSettRec);
  return compRec;
 }
 
 public synchronized  long getCompDocNumber(long compId, String docType) throws BacException {
  LOGGER.log(INFO, "getCompDocNumber called with comp id {0} type {1}", new Object[]{compId,docType});
  if(!trans.isActive()){
   trans.begin();
  }
  long ret = 0l;
  CompanyDocNumbers docNum ;
  
   Query q = em.createNamedQuery("lastDocNum");
   q.setParameter("docType",docType);
   q.setParameter("compId", compId);
   try{
   docNum = (CompanyDocNumbers)q.getSingleResult();
   
   ret = docNum.getDocNum();
   ret++;
   LOGGER.log(INFO, "docNum from db {0}", docNum);
   docNum.setDocNum(ret) ;
   trans.commit();
   }catch(NoResultException ex){
    LOGGER.log(INFO, "not found doc num for doc Type {0} comp id {1}", new Object[]{docType,compId});
    docNum = new CompanyDocNumbers();
    CompanyBasic comp = em.find(CompanyBasic.class, compId, OPTIMISTIC);
    docNum.setComp(comp);
    docNum.setDocType(docType);
    docNum.setDocNum(01);
    em.persist(docNum);
    ret = docNum.getDocNum();
    trans.commit();
   }
   catch(Exception e){
    LOGGER.log(INFO, "Other error for docType {0}", e.getLocalizedMessage());
   }
   //em.flush();
   //logger.log(INFO, "doc num is now: {0}",docNum.getDocNum()); 
   //em.flush();
   //logger.log(INFO, "doc num is now: {0}",docNum.getDocNum()); 
   
  
  return ret;
 }
  
 public FundRec updateRestrictedFund(FundRec fnd, String pg){
  if(!trans.isActive()){
   trans.begin();
  }
  Fund f = this.buildRestrictedFund(fnd, pg);
  trans.commit();
  return fnd;
 }
 
}
