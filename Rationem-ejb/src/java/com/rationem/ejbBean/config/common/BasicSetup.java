/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.config.common;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.common.*;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.entity.config.common.Uom;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import javax.ejb.EJB;
import com.rationem.exception.BacException;
import java.util.Date;
import com.rationem.entity.config.company.Ledger;
import java.util.List;
import com.rationem.entity.config.company.AccountType;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.*;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.ContactDM;
import com.rationem.ejbBean.dataManager.VatDM;
import com.rationem.helper.comparitor.NumberRangeByShrtDescr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import javax.ejb.Stateless;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Level.FINEST;

/**
 *
 * @author Chris
 */
@Stateless

//@Remote
public class BasicSetup {

    private static final Logger LOGGER =
            Logger.getLogger(BasicSetup.class.getName());

    private AccountTypeRec accountType;

    private LedgerRec ledger;

    private List<AccountTypeRec> accountTypes;
    private List<LedgerRec> ledgers;

    private ModuleRec module;
    private List<ModuleRec> modules;

    private UomRec uom = null;
    private List<UomRec> uoms;

    private Uom uomDB;

    private CompanyBasicRec company;

    

    @EJB
    private ConfigurationDM configDM;
    
    
    
    @EJB
    private VatDM vatDM;
    
    @EJB
    private SysBuffer sysBuff;
    
    @EJB
    private ContactDM contactDM;
    
    
    

    private AccountType buildAccountTypeEntity(AccountTypeRec a){
        LOGGER.log(FINEST, "buildAccountTypeEntity called with: {0}", a);
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
        LOGGER.log(FINEST, "buildAccountTypeRec called with: {0}", a.getId());
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

    private Ledger buildLedger(LedgerRec rec){
        Ledger l = new Ledger();
        l.setId(rec.getId());
        l.setChangedDate(rec.getChangedDate());
        l.setCreatedDate(rec.getCreatedDate());
        l.setName(rec.getName());
        l.setDescr(rec.getDescr());
        l.setCode(rec.getCode());
        return l;
    }



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

    


    public AccountTypeRec getAccountType() {
        LOGGER.log(FINEST, "getAccountType() accountType is {0}:  ", accountType);
        if(accountType == null){
            accountType = new AccountTypeRec();
        }
        LOGGER.log(FINEST, "getAccountType() accountType returned is {0}:  ", accountType);
        return accountType;
    }

    public AccountTypeRec getAccountTypeByRef(String reference) {
        return null;
    }

   /* public boolean createAccountType(com.rationem.busRec.config.company.AccountTypeRec rec) {
        LOGGER.log(FINEST, "createAccountType called wirh: {0}", rec);
        boolean result = false;
        AccountType actType;
        try{
            actType = buildAccountTypeEntity(rec);
            LOGGER.log(FINEST, "DB entity returned: {0}",actType);

            em.persist(actType);
            result = true;
        } catch(EntityExistsException e){
           LOGGER.log(INFO, "Entity exists error: {0)", e.getMessage());
        }catch(IllegalArgumentException e){
            LOGGER.log(INFO, "Argument error: {0)", e.getMessage());
        }catch(TransactionRequiredException e){
            LOGGER.log(INFO, "Transaction error: {0)", e.getMessage());
        }


        LOGGER.log(FINEST, "Create Account success result: {0}", em);
        return result;
    }

*/

   

    public AccountTypeRec updateAccountType(AccountTypeRec rec, String source) {
     LOGGER.log(INFO, "basic setup updateAccountType called with actType {0}  source {1}",
             new Object[]{rec.getName(),source});
        LOGGER.log(INFO, "rec changed by {0}", rec.getChangedBy().getId());
        LOGGER.log(INFO, "rec changed on {0}", rec.getChangedDate());
        rec  = configDM.updateAccountType(rec, source);
        this.sysBuff.accountTypeUpdate(rec,source);
        return rec;
    }

    public boolean deleteAccountType(AccountTypeRec  rec,String pg) {
        LOGGER.log(INFO, "deleteAccountType called with: {0}", rec);
        boolean status = this.configDM.deleteAccountType(rec,pg);
        return status;

    }

    public LedgerRec getLedger() {
        LedgerRec l = new LedgerRec();
        return l;
    }

    public List<LedgerRec> getLedgers() throws BacException {
     LOGGER.log(FINEST, "EJB Called getLedgers");
     ledgers = this.configDM.getLedgersAll();
     return ledgers;

    }



 public LedgerRec insertLedger(LedgerRec rec, String pg) {
        LOGGER.log(INFO, "Called insert ledger called with {0}",rec);
        try{
         rec =   configDM.saveledger(rec, pg);
         return rec;
        }catch(BacException e){
            throw new BacException(e.getLocalizedMessage());
        }
    }



    public List<LedgerRec> getLedgersByCriteria(String name, String description, String code) {
        LOGGER.log(FINEST, "getLedgersByCriteria called with name: {0}");
        ledgers = this.configDM.getLedgersByCriteria(name, description, code);
        
        LOGGER.log(FINEST, "number of ledgers is now: {0}", ledgers.size());
        return ledgers;
        
    }

    public List<ModuleRec> getAllModules()throws BacException{
        modules = new ArrayList<>();
        LOGGER.log(INFO, "BasicSetup.getAllModules  called");
        try{
            modules = this.configDM.getAllModules();
        }catch(BacException e){
            throw new BacException(e.getLocalizedMessage());
        }



        return modules;
    }
    
    
 public List<ModuleRec> getModulesByCriteria(ModuleRec m){
     LOGGER.log(FINEST, "getModulesByCriteria with {0}", m);
     modules = this.configDM.getModulesByCriteria(m);
     return modules;
 }
 
 public ChequeVoidReasonRec updateChequeVoidReason(ChequeVoidReasonRec reason, String pg){
   reason = configDM.updateChequeVoidReason(reason, pg);
  
  return reason;
 }
 public ContactRoleRec updateContactRole(ContactRoleRec contRole, String pg){
  contRole = contactDM.updateContactRole(contRole, pg);
  return contRole; 
 }

 
 public DocReversalReasonRec updateDocReversalReason(DocReversalReasonRec revRsn, String page){
  
  return revRsn = configDM.updateDocReversalReason(revRsn, page);
 }

 public FundCategoryRec updateFundCategory(FundCategoryRec cat, String pg){
  return this.configDM.updateFundCategoryRec(cat, pg);
 }

     
/**
 * Save changed ledger to database
 * @param ledger
 * @return
 */
    public LedgerRec updateLedger(LedgerRec ledger, String pg) {
        LOGGER.log(INFO, "setup.updateLedger called with {0}", ledger);
     ledger = this.configDM.updateLedger(ledger, pg);
     return ledger;
    }
    
    public LineTypeRuleRec updateLineType(LineTypeRuleRec lnTy, String pg){
     lnTy = this.configDM.updateLineType(lnTy, pg);
     return lnTy;
    }

    public LocaleCodeRec getLocaleByCode(String code){
     LocaleCodeRec loc = this.configDM.getLocaleCodeByCd(code);
     return loc;
    }
    public ModuleRec getModule() throws BacException {
        ModuleRec rec = new ModuleRec();
        rec.setCreatedDate(new Date());
        return rec;
    }

    public ModuleRec createModule(ModuleRec module, UserRec usr, String page) throws BacException {
        
        try{
        module = configDM.createModule(module, usr,page);
        }catch(BacException e){
          LOGGER.log(INFO, "caught BacException {0}", e.getMessage());
          throw new BacException(e.getMessage());
        }
        return module;
    }
public LocaleCodeRec updateLocaleCode(LocaleCodeRec loc, String pg){
 loc = this.configDM.updateLocaleCode(loc, pg);
 LOGGER.log(INFO, "configDM returns loc with id {0}", loc.getId());
 return loc;
}
    public void updateModule(ModuleRec m, String source) throws BacException {
        LOGGER.log(INFO, "updateModule called with {0}", m+" id: "+m.getId());
        configDM.updateModule(m,source);
        
    }

    public void deleteModule(ModuleRec module, String src) throws BacException {
        LOGGER.log(INFO, "EJB deleteModule called with: {0} ", module);
        if(module == null){
            throw new BacException("No module passed to delete");
        }
        try{
            configDM.deleteModule(module,src);
        }catch(BacException e){
            LOGGER.log(INFO, "Delete modulke at DB level failed {0}",e.getLocalizedMessage());
            throw new BacException(e.getLocalizedMessage());
        }

    }

    /**
     * Called to return an empty line type rule object.
     * @return
     * @throws BacException
     */

    public LineTypeRuleRec getLineTypeRule() throws BacException {
        LOGGER.log(INFO, "Called getLineTypeRule");
        LineTypeRuleRec rec = new LineTypeRuleRec();
        return rec;
    }

    public LineTypeRuleRec getLineTypeByCode(String code){
     LineTypeRuleRec lTy = this.configDM.getLineTypeByCode(code);
     return lTy;
    }
    
    public void createLineType(LineTypeRuleRec lineType, String pg) throws BacException {
        LOGGER.log(FINEST, "Called BasicSetup.createLineType with {0}", lineType);
        try{
            
            this.configDM.createLineType(lineType,pg);
        }catch(BacException e){
            LOGGER.log(WARNING, "Failed to save line type reason: {0}", e.getLocalizedMessage());
            throw new BacException(e.getLocalizedMessage());
        }

    }

    public UomRec getUom() throws BacException {
        if(uom == null) {
            uom = new UomRec();
        }
        return uom;

    }

    public List<UomRec> getUoms() throws BacException {
        LOGGER.log(FINEST, "Called BasicSetup.getUoms");
        try{
            uoms = configDM.getAllUoms();
            LOGGER.log(FINEST, "UOMS returned from DB layer: {0}", uoms.size());
        }catch(BacException e){
            throw new BacException("Could not create uoms: "+e.getMessage());
            
        }
        return uoms;
    }

    public void createUom(UomRec rec, String pg) throws BacException {
        LOGGER.log(INFO,"Create UOM");

        try{
            
            this.configDM.createUom(rec, pg);
        }catch(BacException e){
            LOGGER.log(INFO, "DB manager could not save UOM");
            throw new BacException(e.getMessage());
        }
    }
/**
 * Unit of measure. Used internally. Returns the DB equivalent of the business record.
 * @param rec
 * @return DB version for Unit of Measure
 * @throws BacException
 */


    public Uom getUomDB(UomRec rec) throws BacException {
        return this.buildUom(rec);
    }

    public void setCompany(CompanyBasicRec company) {
        this.company = company;
    }

    public CompanyBasicRec getCompanyRec() throws BacException {
        return null;
    }

 public ContactRoleRec getContactRoleByName(String name){
  ContactRoleRec cr = this.contactDM.getContactRoleByName(name);
  return cr;
 }
    public NumberRangeRec getNumberControl() throws BacException {
     LOGGER.log(INFO, "getNumberControl()  called");
     
        return new NumberRangeRec();
    }
    
    public NumberRangeRec getNumControlById(Long id){
     NumberRangeRec rec = this.configDM.getNumCntrById(id);
     LOGGER.log(INFO, "Num control from DB {0}", rec);
     return rec;
    }
    
    public NumberRangeRec getNumControlByShrtDescr(String descr){
     LOGGER.log(INFO, "basicSetup getNumControlByShrtDescr called with {0}", descr);
     NumberRangeRec rec = this.configDM.getNumCntrBySrtDescr(descr);
     LOGGER.log(INFO, "DB layer returns {0}", rec);
     return rec;
    }
    public void createNumberControl(NumberRangeRec rec, String src) throws BacException {
        LOGGER.log(INFO, "Basicset.createNumberControl called with {0}", rec);
        configDM.createNumberControl(rec,src);
        LOGGER.log(INFO, "BasicSetup after call to DB", rec);
    }





    public ModuleRec getModuleById(Long id) throws BacException {
        LOGGER.log(INFO, "DM getModuleById called with {0}", id);
        try{
            ModuleRec rec = this.configDM.getModuleById(id);
            return rec;
        }catch(BacException e){
            throw new BacException("Could not retrieve module from DB");
        }

    }

    public List<NumberRangeRec> getNumerControlsAll() throws BacException {
        LOGGER.log(INFO, "Setup getNumerControlsAll called ");
        try{
          List<NumberRangeRec> numRanges = configDM.getNumCntrlAll();
          LOGGER.log(INFO, "Number of ranges returned from data manager {0}", numRanges);
          Collections.sort(numRanges, new NumberRangeByShrtDescr());
          return numRanges;
        }catch(BacException e){
            throw new BacException(e.getLocalizedMessage());
        }
    }
  public List<NumberRangeChequeRec> getNumberRangeChequeAll(){
   List<NumberRangeChequeRec> retList = this.configDM.getNumberRangeChequeAll();
   return retList;
  }
  
  
   public List<ProcessCodeRec> getProcessCodesAll(){
    List<ProcessCodeRec> retList = configDM.getProcessCodesAll();
    LOGGER.log(INFO, "ConfigDM returned process codes {0}", retList);
    return retList;
   } 


    public void saveAccountType(AccountTypeRec actType, String pg) throws BacException {
        LOGGER.log(INFO, "EJB saveGlAccountType called with {0}", actType);
        LOGGER.log(INFO,"Number range code {0} id {1}", new Object[]{actType.getNumberRange().getShortDescr(),
         actType.getNumberRange().getNumberControlId()});
        Date curr = new Date();
        actType.setCreatedDate(curr);
        configDM.saveAccountType(actType, pg);
        LOGGER.log(INFO,"After DB save account type");
    }

  public ArrayList<SortOrderRec> getSortOrders(String parameter) throws BacException {
    return null;
  }

  public ProcessCodeRec addProcessCode(ProcessCodeRec pr, String pg){
   //pr = this.configDM.addProcessCode(pr, pg);
   pr = sysBuff.processCodeUpdate(pr, pg);
   return pr;
  }
  /**
   * Creates a new sort order
   * @param sortOrder
   * @throws BacException
   */
  public void addSortOrder(SortOrderRec sortOrder) throws BacException {
  }

  /**
   * Called once for each number control to be deleted by DB layer
   * @param numRange
   * @throws BacException 
   */
  public boolean deleteNumberControl(NumberRangeRec numRange,String source) throws BacException {
    LOGGER.log(INFO, "Basic setup delete Number control", numRange);
    boolean deleted = configDM.deleteNumberControl(numRange, source);
    
    LOGGER.log(INFO, "After DB call");
    return deleted;
  }

  public List<AccountTypeRec> getFiGlAccountTypesAll() throws BacException {
    LOGGER.log(INFO,"BasicSetup getFiGlAccountTypesAll called");
    List<AccountTypeRec> actTypeList = this.configDM.getAccountTypes();
    LOGGER.log(INFO, "config dm returned account types {0}", actTypeList);
    return actTypeList;
  }

  public List<FundCategoryRec> getFundCategoriesAll(){
   return this.configDM.getFundCategoriesAll();
  }
  
  public FundCategoryRec getFundCategoryByRef(String ref){
   return configDM.getFundCategoryByRef(ref);
  }
  public WsPartnerRec getWsPartnerByName(String name) throws BacException {
    return null;
  }




    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public NumberRangeRec updateNumberControl(NumberRangeRec numCntrol, String source) throws BacException {
  LOGGER.log(INFO, "BasicSetup.updateNumberControl called with num cntrl {0} source {1}", 
          new Object[]{numCntrol,source});
  numCntrol = configDM.updateNumControl(numCntrol, source);
  return numCntrol;
 }
 
 public NumberRangeChequeRec updateNumberRangeCheque(NumberRangeChequeRec numRng, String source) throws BacException {
  LOGGER.log(INFO, "BasicSetup.updateNumberControl called with cheque book {0} source {1}", 
          new Object[]{numRng,source});
  LOGGER.log(INFO, "updateNumberRangeCheque create by {0}", numRng.getCreatedBy().getId());
  numRng = configDM.updateNumberRangeCheque(numRng, source);
  return numRng;
 }
 
 public NumberRangeTypeRec upateNumberRangeType(NumberRangeTypeRec nrTyRec, String source){
  LOGGER.log(INFO, "Basic Setup called with nr {0}", nrTyRec.getCode());
  LOGGER.log(INFO, "create date nr {0}", nrTyRec.getCreatedDate().toString());
  nrTyRec = this.configDM.upateNumberRangeType(nrTyRec, source);
  LOGGER.log(INFO, "nr  id after call to DM {0}", nrTyRec.getId());
  
  return nrTyRec;
 }

 public PaymentTermsRec updatePaymentTerms(PaymentTermsRec rec, String pg){
  LOGGER.log(INFO,"Config Mgr updatePaymentTerms called with {0}", rec.getPayTermsCode());
  rec = configDM.updatePaymentTerms(rec, pg);
  return rec;
 }
 public UomRec updateUom(UomRec uom, String pg){
  LOGGER.log(INFO, "Setup.updateUom {0}", uom);
  uom = this.sysBuff.updateUom(uom, pg);
  return uom;
 }
 /**
  * Save new VAT code 
  * @param vatCode
  * @param source
  * @return
  * @throws BacException 
  */
 public VatCodeRec addVatCode(VatCodeRec vatCode, String source) throws BacException {
  LOGGER.log(INFO, "Basicsetup.addVatCode called with vat code {0} and source {1}", 
          new Object[]{vatCode,source});
  vatCode = vatDM.addVatCode(vatCode, source);
  LOGGER.log(INFO, "VAT Code id after DB layer {0}", vatCode.getId());
  return vatCode;
 }
/**
 * retrieves all VAT codes from DB layer
 * @return
 * @throws BacException 
 */
 public List<VatCodeRec> getVatCodes() throws BacException {
  
  List<VatCodeRec> vatCodes = this.vatDM.getAllVatCodes();
  LOGGER.log(INFO, "Number of VAT Codes returned from DB layer {0}", vatCodes.size());
  return vatCodes;
 }

 /**
  * Get the Company posting details for a vat code 
  * @param vatCode
  * @return
  * @throws BacException 
  */
 public List<VatCodeCompanyRec> getVatCompCodeForVatCode(VatCodeRec vatCode) throws BacException {
  LOGGER.log(INFO, "Basic setup getVatCodeCompForVatCode called with Vat code {0}", vatCode.getCode());
  vatCode = this.vatDM.getVatCompRecsForVatCode(vatCode);
  List<VatCodeCompanyRec> retlst = vatCode.getVatCodeCompanies();
          
  
  return retlst;
 }
/**
 * Setup manager add vat code
 * @param vatCode
 * @param vatCodeComp - vat code posting records to be added
 * @param createdUser - User that made the updates
 * @param source - page used to make the updates
 * @return 
 */
 public VatCodeRec addVatPostingForCompanies(VatCodeRec vatCode, List<VatCodeCompanyRec> vatpostingList, 
         UserRec createdUser, String source) throws BacException {
  LOGGER.log(INFO,"addVatPostingForCompanies called with vat code {0}",vatCode);
  vatCode = this.vatDM.addVatPostingForCompanies(vatCode,vatpostingList, createdUser, source);
  
  
  return vatCode;
 }
/**
 * Business Logic layer add 
 * @param scheme
 * @param user
 * @param source
 * @return
 * @throws BacException 
 */
 public VatSchemeRec addVatScheme(VatSchemeRec scheme, UserRec user, String source) throws BacException {
  LOGGER.log(INFO, "addVatScheme called with scheme {0} user {1} source {2}", 
          new Object[]{scheme, user,source});
  VatSchemeRec schemeDB = this.vatDM.addVatScheme(scheme, user, source);
  LOGGER.log(INFO, "VAT Scheme returned by DB {0}", schemeDB);
  return schemeDB;
 }

 public VatFlatRateIndRateRec vatIndustryRateUpdate(VatFlatRateIndRateRec rate, UserRec usr, String source) 
         throws BacException {
  LOGGER.log(INFO, "Setup EJB addVatIndustryRate called with rate {0} user {1} page source {2}", 
          new Object[]{rate,usr,source});
  rate = vatDM.vatIndustryFlatRateSave(rate, usr, source);
  LOGGER.log(INFO, "Ind rate returned by DB layer to setup is {0}", rate);
  return rate;
 }

 /**
  * finds a list of all the available VAT schemes
  * @return
  * @throws BacException 
  */
 public List<VatSchemeRec> getVatSchemesAll() throws BacException {
  LOGGER.log(INFO, "BasicSetup.getVatSchemesAll called");
  List<VatSchemeRec> schemes = vatDM.getVatSchemesAll();
  LOGGER.log(INFO, "Vat Schemes returned from DB layer", schemes);
  return schemes;
 }

 public List<VatFlatRateIndRateRec> getVatIndustriesAll() throws BacException {
  List<VatFlatRateIndRateRec> inds = vatDM.getVatIndustries();
  LOGGER.log(INFO, "Setup returns vat industries {0}", inds);
  return inds;
 }

 public VatRegSchemeRec VatRegAddScheme(VatRegSchemeRec vatReg, UserRec usr,String source) 
         throws BacException {
  LOGGER.log(INFO, "Basic Setup.VatRegAddScheme called with reg Scheme {0} user {1} page {2}", 
          new Object[]{vatReg,usr,source});
  if(vatReg == null){
   throw new BacException("VatScheme is null","VAT:01");
  }
  //vatReg = vatDM.VatRegistrationAddSchemes(vatReg, usr, source);
  
  LOGGER.log(INFO, "Vat reg scheme id from DB layer {0}", vatReg.getId());
  return vatReg;
 }

 /**
  * Create a new value date
  * @param valueDate
  * @param usr
  * @param source
  * @return
  * @throws BacException 
  */
 public ValueDateRec addValueDate(ValueDateRec valueDate, UserRec usr, String source) 
         throws BacException {
  try{
   valueDate = configDM.addValueDate(valueDate, usr, source);
  }catch(Exception ex){
   LOGGER.log(INFO, "addValueDate DB exception {0}", ex.getLocalizedMessage());
   throw new BacException("Could not save value date ","VALDT:01");
  }
  LOGGER.log(INFO, "BasicSetup.addValueDate returns value date with id {0}", valueDate.getId());
  return valueDate;
 }
 
public VatSchemeRec vatSchemeUpdate(VatSchemeRec scheme, UserRec user, String source) throws BacException {
  LOGGER.log(INFO, "addVatScheme called with scheme {0} user {1} source {2}", 
          new Object[]{scheme, user,source});
  VatSchemeRec schemeDB = this.vatDM.vatSchemeUpdate(scheme, user, source);
  LOGGER.log(INFO, "VAT Scheme returned by DB {0}", schemeDB);
  return schemeDB;
 } 
 
 
 

 
}
