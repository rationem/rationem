/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.common;

import com.rationem.util.BankValCredential;
import java.util.List;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.common.*;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.ejbBean.dataManager.CompanyDM;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.ejbBean.dataManager.DocumentDM;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.config.company.PostTypeRec;
import java.util.Date;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import java.util.ListIterator;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.config.company.CalendarRuleBaseRec;
import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.busRec.config.company.CompanyFiscalPeriodYearForDate;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyApArRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.sales.SalesCatRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatReturnRec;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.config.common.VatManager;
import com.rationem.ejbBean.dataManager.*;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.LineTypeRule;
import com.rationem.entity.config.common.Uom;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.helper.CompCostCentreRec;
import com.rationem.helper.CompProgrammeRec;
import com.rationem.helper.CompVatRegistrationRec;
import com.rationem.helper.DocTypeByName;
import com.rationem.helper.comparitor.NumRangeTypeByCode;
import com.rationem.helper.comparitor.PartnerRoleRecByName;
import com.rationem.helper.comparitor.PaymentTermsByBasisDays;
import com.rationem.helper.comparitor.PostTypeByDescr;
import com.rationem.helper.comparitor.SortOrderByCode;
import com.rationem.helper.comparitor.SortOrderByName;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;

import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;



@Stateless
@LocalBean
public class SysBuffer {
    private static final Logger LOGGER =
            Logger.getLogger("com.rationem.ejbBean.common.SysBuffer");
    public static final int BUFF_RESET_ALL = 0;
    public static final int BUFF_RESET_COMP = 1;
    private static final int MAX_PER_DATES = 30;

    @EJB
    private ConfigurationDM configDM;

    @EJB
    private BasicSetup setup;

    @EJB
    private CompanyManager compMgr;
    
    @EJB
    private CostCentreDM ccDm;

    @EJB
    private DocumentDM docDM;

    @EJB
    private CompanyDM compDM;
    
    @EJB
    private ContactDM contDm;

    @EJB
    private ProgrammeDM progDm;
    
    @EJB
    private SalesDM salesDM;
    
    @EJB
    private VatDM vatDM;
    
    @EJB
    private VatManager vatMgr;

    @EJB 
    private TreasuryDM treasuryDM; 
    
    @EJB
    private MasterDataDM mastDataDM;
    
        

    private List<AccountTypeRec> acntTypes;
    private List<UomRec> uoms;
    private List<ModuleRec> modules;
    private List<ProcessCodeRec> processCodes;
    private List<NumberRangeRec> numRanges;
    private List<NumberRangeTypeRec> numRangeTypes;
    private List<TransactionTypeRec> transactionTypes;
    private List<LineTypeRuleRec> lineTypeRules;
    private ArrayList<FisPeriodRuleRec> fisPeriodRules;
    private List<CalendarRuleBaseRec> calendarRules;
    private List<ValueDateRec> valueDates;
    private List<BacsTransCodeRec> bacsTransactionCodes;
    private CompanyBasicRec company;
    private List<CompanyBasicRec> companies;
    private List<ChequeVoidReasonRec> chqVoidReasons;
    
    private List<CompanyBasic> comps;
    private ArrayList<ChartOfAccountsRec> chartsOfAccounts;
    private List<FiGlAccountCompRec> glAccountsForCompany;
    private List<BankAccountCompanyRec> compBankAccounts;

    private ArrayList<SortOrderRec> sortOrders;
    private List<CompCostCentreRec> compCostCents;
    private List<CompProgrammeRec> compProgs;

    private List<PostTypeRec> postTypes;
    private List<PostType> postTypeList;
    private List<LedgerRec> ledgers;
    private List<DocTypeRec> docTypes;
    private List<FundRec> restrictedFunds;
    private List<FundCategoryRec> fundCategories;
    private List<PaymentTermsRec> paymentTermsList;
    private List<PaymentTypeRec> paymentTypes;
    private List<BankValCredential> bankValCredentials;
    private List<SalesCatRec> salesCategories;
    private List<SalesPartRec> salesParts;
    private List<SalesPartCompanyRec> companySalesParts;
    private List<VatCodeRec> vatCodes;
    private List<VatCodeCompanyRec> vatCompanyCodes;
    private int maxComplSize;
    private Date maxDate;
    private List<CompanyFiscalPeriodYearForDate> compFiscalPerYrForDate;
    private List<CompVatRegistrationRec> compVatRegList;
    private List<PartnerRoleRec> partnerRoles;
    private List<ContactRoleRec> contactRoles;
    private HashMap<Integer, String> numberText;
    private HashMap<String,String> numberPlaceText;
    
    
    private static final int PERIOD_LIST_SIZE = 63;


    public SysBuffer() {
        
    }

  
  @PostConstruct
  private void init(){
   LOGGER.log(INFO, "session buffer init");
  }
  
  
     
  public AccountTypeRec getAcntTypeByName(String name){
   if(acntTypes == null){
    acntTypes = this.compDM.getAccountTypes();
   }
   
   for(AccountTypeRec acntTy: acntTypes){
    if(acntTy.getName().equals(name)){
     return acntTy;
    }
   }
   return null;
  }  
  
 public AccountTypeRec getAcntTypeByProcCode(String processCode){
  LOGGER.log(INFO, "getAcntTypeByProcCode called with code {0}", processCode);
  if(acntTypes == null){
    acntTypes = compDM.getAccountTypes();
   }
   
  if(acntTypes == null){
   return null;
  }
  ListIterator<AccountTypeRec> acntTyLi = acntTypes.listIterator();
  boolean found = false;
  AccountTypeRec acntTy = null;
  while(acntTyLi.hasNext() && !found){
   acntTy = acntTyLi.next();
   LOGGER.log(INFO, "acntTy name {0} acnt type proc code {1} process code {2}", 
           new Object[]{acntTy.getName(),acntTy.getProcessCode(),processCode});
   
   if(StringUtils.equals(acntTy.getProcessCode().getName(), processCode)){
    found = true;
   }
  }
  return acntTy;
    
   
 } 
 public List<AccountTypeRec> getAcntTypes() {
  LOGGER.log(INFO, "getAcntTypes acntTypes: {0}", acntTypes);
  if(acntTypes == null){
   acntTypes = compDM.getAccountTypes();
  }
  return acntTypes;
 }

 public void setAcntTypes(List<AccountTypeRec> acntTypes) {
  this.acntTypes = acntTypes;
 }

    
 public BacsTransCodeRec getBacsTransactionCodeByCode(String code) {
  
  ListIterator<BacsTransCodeRec> li = getBacsTransactionCodes().listIterator();
  while(li.hasNext()){
   BacsTransCodeRec bacsTransCode = li.next();
   if(bacsTransCode.getPtnrBnkTransCode().equalsIgnoreCase(code)){
    return bacsTransCode;
   }
  }
  
  return null;
  
 }
 
 public List<BacsTransCodeRec> getBacsTransactionCodes() {
  if(bacsTransactionCodes == null){
   bacsTransactionCodes = this.treasuryDM.getBacsTransCodesAll();
  }
  return bacsTransactionCodes;
 }

 public void setBacsTransactionCodes(List<BacsTransCodeRec> bacsTransactionCodes) {
  this.bacsTransactionCodes = bacsTransactionCodes;
 }

    
    public LineTypeRuleRec getLineTypeRuleByCode(String lineCode){
     LineTypeRuleRec rec = null;
     for(LineTypeRuleRec lineType : getLineTypeRules()){
      if(lineType.getLineCode().equalsIgnoreCase(lineCode)){
       return lineType;
      }
     }
     LOGGER.log(INFO, "Line rule not found for: {0}", lineCode);
     return rec;
    }
    public LineTypeRuleRec getLineTypeRule(LineTypeRule l){
     LOGGER.log(INFO, "getLineTypeRule called with line type {0}", l);
     if(l == null){
      return null;
     }
     LineTypeRuleRec rec = null;
     ListIterator<LineTypeRuleRec> lineRules = getLineTypeRules().listIterator();
     LOGGER.log(INFO, "Found linetypes {0}",lineRules);
     while(lineRules.hasNext()){
      LineTypeRuleRec lineRule = lineRules.next();
      if(Objects.equals(lineRule.getId(), l.getId())){
       LOGGER.log(INFO, "Sys buff getLineTypeRule returns lline type with id  {0}", lineRule.getId());
       return lineRule;
      }
     }
     return rec;
    }
    
    public List<LineTypeRuleRec> getLineTypeRules() {
     if(lineTypeRules == null || lineTypeRules.isEmpty()){
      lineTypeRules = this.configDM.getLineTypesAll();
      return lineTypeRules;
     } else{
      return lineTypeRules;
     }
     
     
    }

  public void setLineTypeRules(List<LineTypeRuleRec> lineTypeRules) {
        this.lineTypeRules = lineTypeRules;
  }

 public LineTypeRuleRec lineTypeUpdate(LineTypeRuleRec lineType){
  if(lineTypeRules == null){
   lineTypeRules = new ArrayList<>();
   }
  ListIterator<LineTypeRuleRec> li = lineTypeRules.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   LineTypeRuleRec ty = li.next();
   if(Objects.equals(ty.getId(), lineType.getId())){
    li.set(ty);
    found = true;
   }
  }
  if(!found){
   lineTypeRules.add(lineType);
  }
  LOGGER.log(INFO, "lineTypeRules after lineTypeUpdate {0}", lineTypeRules);
  return lineType;
 }    

 public List<SalesPartCompanyRec> getCompanySalesParts() {
  if(companySalesParts == null){
   companySalesParts = new ArrayList<>();
  }
  companySalesParts = salesDM.getAllCompanySalesParts();
  return companySalesParts;
 }

 public void setCompanySalesParts(List<SalesPartCompanyRec> companySalesParts) {
  this.companySalesParts = companySalesParts;
 }

 public List<SalesPartCompanyRec> getCompanySalesPartsForComp(CompanyBasicRec comp)throws BacException{
  if(comp == null){
   throw new BacException("Company Required","genCompRequired");
  }
  
  List<SalesPartCompanyRec> partsList = new ArrayList<>();
  if(companySalesParts == null ||companySalesParts.isEmpty() ){
   companySalesParts = getCompanySalesParts();
  }
  ListIterator<SalesPartCompanyRec> li = companySalesParts.listIterator();
  
  if(companySalesParts  != null){
  for(SalesPartCompanyRec part:companySalesParts){ 
   if(part.getCompany().getId() == comp.getId()){
    partsList.add(part);
   }
  }
  }
  
  LOGGER.log(INFO, "getCompanySalesPartsForComp returns parts {0}", partsList);
  return partsList;
 }

 public CompanyBasicRec getCompCurrentVatRec(CompanyBasicRec compRec){
  if(compRec.getVatRegDetails().getId() != null){
   return compRec;
  }
  if(compRec.isVatReg()){
  VatRegistrationRec vatReg = vatDM.getVatRegistrationForCompany(compRec);
  compRec.setVatRegDetails(vatReg);
  }
  ListIterator<CompanyBasicRec> li = companies.listIterator();
  boolean compFound = false;
  while(li.hasNext() && !compFound){
   CompanyBasicRec currComp = li.next();
   if(Objects.equals(currComp.getId(), compRec.getId())){
    li.set(compRec);
    compFound = true;
   }
  }
  
  return compRec;
 }
 public CompanyBasic getComp(CompanyBasicRec compRec){
  CompanyBasic comp;
  if(comps == null || comps.isEmpty()){
   LOGGER.log(INFO, "No comps in buffer");
   comp = this.compDM.getCompPvt(compRec);
   comps = new ArrayList<>();
   comps.add(comp);
   return comp;
  }
  for(CompanyBasic c: comps){
   if(Objects.equals(c.getId(), compRec.getId())){
    LOGGER.log(INFO, "Company found in buffer ", c);
    return c;
   }
  }
  comp = this.compDM.getCompPvt(compRec);
  LOGGER.log(INFO, "Need to add comp to buffer list");
   comps = new ArrayList<>();
   comps.add(comp);
   return comp;
 }
 
 public List<CompanyBasic> getComps() {
  LOGGER.log(INFO, "Sys buff get comps called comps: {0}",comps);
  
  return comps;
 }

 public void setComps(List<CompanyBasic> comps) {
  this.comps = comps;
 }

 public BankAccountCompanyRec getCompBankAcntById(Long id, CompanyBasicRec comp){
  for(BankAccountCompanyRec acnt:getCompBankAccounts(comp)){
   if(Objects.equals(acnt.getId(), id)){
    return acnt;
   }
    
  }
  LOGGER.log(INFO, "getCompBankAcntById not found account by id {0}",id);
  return null;
 }
 public List<BankAccountCompanyRec> getCompBankAccounts(CompanyBasicRec comp) {
  
  if(compBankAccounts == null){
   compBankAccounts = this.treasuryDM.getBankAccountsForCompany(comp);
  }
  return compBankAccounts;
 }

 public void setCompBankAccounts(List<BankAccountCompanyRec> compBankAccounts) {
  this.compBankAccounts = compBankAccounts;
 }

 
 
 public List<CompCostCentreRec> getCompCostCents() {
  return compCostCents;
 }

 public void setCompCostCents(List<CompCostCentreRec> compCostCents) {
  this.compCostCents = compCostCents;
 }

 public List<ContactRoleRec> getContactRoles() {
  if(contactRoles == null || contactRoles.isEmpty()){
   contactRoles = this.contDm.getContactRoles();
  }
  return contactRoles;
 }
 
 public ContactRoleRec getContactRoleByName(String name){
  if(contactRoles == null || contactRoles.isEmpty()){
   contactRoles = this.contDm.getContactRoles();
  }
  for(ContactRoleRec rec:contactRoles){
   if(rec.getName().equals(name)){
    return rec;
   }
  }
  return null;
 }

 public void setContactRoles(List<ContactRoleRec> contactRoles) {
  this.contactRoles = contactRoles;
 }
 
 
 public List<CostCentreRec> getCostCentForComp(Long compId){
  
  List<CostCentreRec> costCents = new ArrayList<>();
  
  if(compCostCents == null){
   List<CostCentreRec> ccList = this.ccDm.getCostCentByCompId(compId);
   if(ccList == null){
    return null;
   }
   compCostCents = new ArrayList<>();
   for(CostCentreRec costCent : ccList){
    costCents.add(costCent);
    CompCostCentreRec compCc = new CompCostCentreRec ();
    compCc.setCompId(compId);
    compCc.setCostCent(costCent);
    compCostCents.add(compCc);
   }
   return ccList;
  }
  
  for(CompCostCentreRec curr : compCostCents){
   if(Objects.equals(curr.getCompId(), compId)){
    costCents.add(curr.getCostCent());
   }
  }
  LOGGER.log(INFO, "Sys Buff returns cost cents {0}", costCents);
  return costCents; 
 }
 
 public void buffRefresh(int buff){

  if  (buff == SysBuffer.BUFF_RESET_COMP){
   companies = this.compDM.getCompanies();
   LOGGER.log(INFO, "Companies is now {0}", companies);
  }
 }
 public boolean docTypeCodeUnique(String input){
  boolean rc = true;
  if(input == null || input.isEmpty()){
   return true;
  }
  for(DocTypeRec dt: getDocTypes()){
   if(dt.getCode().equalsIgnoreCase(input)){
    return false;
   }
  }
  
  return rc;
 }
 public List<DocTypeRec> getDocTypes() {
  LOGGER.log(INFO, "getDocTypes called");
  
  if(docTypes == null || docTypes.isEmpty()){
   docTypes = docDM.getAllDocTypes();
  }
  
  Collections.sort(docTypes, new DocTypeByName());
  
  return docTypes;
 }
 
 public List<DocTypeRec> getDocTypesForLedgerCode(String ledCd){
  LOGGER.log(INFO, "getDocTypesForLedgerCode called with {0}", ledCd);
  boolean toAdd = false;
  List<DocTypeRec> dtList = getDocTypes();
  if(dtList == null){
   return null;
  }
  List<DocTypeRec> retList = new ArrayList<>();
  
  for(DocTypeRec curr: dtList){
   toAdd = false;
   if(curr.isApAllowed() && StringUtils.equalsIgnoreCase(ledCd, "AP")){
    toAdd = true;
   }
   if(curr.isArAllowed() && StringUtils.equalsIgnoreCase(ledCd, "AR")){
    toAdd = true;
   }
   if(curr.isGlAllowed() && StringUtils.equalsIgnoreCase(ledCd, "GL")){
    toAdd = true;
   }
  
   
   if(toAdd){
    if(retList.isEmpty()){
     retList.add(curr);
    }else{
     boolean found = false;
     ListIterator<DocTypeRec> li = retList.listIterator();
     while(li.hasNext() && !found){
      DocTypeRec c = li.next();
      if(Objects.equals(c.getId(), curr.getId())){
       found = true;
      }
     }
     if(!found){
      retList.add(curr);
     }
    }
   }
  }
  return retList;
 }

 public void setDocTypes(ArrayList<DocTypeRec> docTypes) {
  this.docTypes = docTypes;
 }

 public DocTypeRec getDocTypeByCode(String code){
  for(DocTypeRec docTy: getDocTypes()){
   if(docTy.getCode().equals(code)){
    return docTy;
   }
  }
  return null;
 }
 
 public DocTypeRec getDocTypeById(Long docId){
  
  for(DocTypeRec docTy: getDocTypes()){
   if(Objects.equals(docTy.getId(), docId)){
    return docTy;
   }
  }
  return null;
 }
 public List<FiGlAccountCompRec> getGlAccountsForCompany() {
  return glAccountsForCompany;
 }
 
 public AccountTypeRec accountTypeUpdate(AccountTypeRec acntTy, String src){
  LOGGER.log(INFO, "accountTypeUpdate called with {0}", acntTy);
  acntTy = this.configDM.updateAccountType(acntTy, src);
  if(acntTy.getId() == null){
   LOGGER.log(INFO,"could not save account type");
   return acntTy;
  }
  if(acntTypes == null || acntTypes.isEmpty()){
   acntTypes = new ArrayList<>();
   acntTypes.add(acntTy);
   return acntTy;
  }
  ListIterator<AccountTypeRec> li = acntTypes.listIterator();
  boolean tyFound = false;
  while(li.hasNext() && !tyFound){
   AccountTypeRec ty = li.next();
   if(Objects.equals(ty.getId(), acntTy.getId())){
    li.set(acntTy);
    tyFound = true;
   }
  }
  if(!tyFound){
   acntTypes.add(acntTy);
  }
  return acntTy;
 }
 
 /**
  * Adds a new company GL account to the company in System buffer
  * @param glAc Company GL Account to be added /**
 Adds a new company GL account to the company in System buffer
  */
 public void addGlAccountToCompany(FiGlAccountCompRec glAc ){
  LOGGER.log(INFO, "Sys Buff addGlAccountToCompany called with gl ac {0} ", 
          new Object[]{glAc});
  CompanyBasicRec comp = glAc.getCompany();
  if(comp.equals(this.company)){
   List<FiGlAccountCompRec> glAcList = company.getGlAccounts();
   if(glAcList == null){
    glAcList = new ArrayList<>();
   }
   glAcList.add(glAc);
   company.setGlAccounts(glAcList);
  }
  ListIterator<CompanyBasicRec> compLi = companies.listIterator();
  while(compLi.hasNext()){
   CompanyBasicRec sysComp = compLi.next();
   if(sysComp.equals(comp)){
    List<FiGlAccountCompRec> glAcList = company.getGlAccounts();
    if(glAcList == null){
    glAcList = new ArrayList<>();
    }
    glAcList.add(glAc);
    sysComp.setGlAccounts(glAcList);
   }
  }
 }
 
 public void setGlAccountsForCompany(List<FiGlAccountCompRec> glAccounts) {
  this.glAccountsForCompany = glAccounts;
 }
 


 public int getMaxComplSize() {
  if(maxComplSize == 0){
   maxComplSize = 10;
  }
  return maxComplSize;
 }

 public void setMaxComplSize(int maxComplSize) {
  this.maxComplSize = maxComplSize;
 }

 public Date getMaxDate() {
  if(maxDate ==  null){
   Calendar cal = Calendar.getInstance();
   cal.set(9999, Calendar.DECEMBER, 31 );
   maxDate = cal.getTime();
  }
  return maxDate;
 }

 public void setMaxDate(Date maxDate) {
  this.maxDate = maxDate;
 }

 
 public List<PartnerRoleRec> getPartnerRoles() {
  if(partnerRoles == null){
   partnerRoles = mastDataDM.getPartnerRoles();
   Collections.sort(partnerRoles, new PartnerRoleRecByName());
  }
  return partnerRoles;
 }

 public void setPartnerRoles(List<PartnerRoleRec> partnerRoles) {
  this.partnerRoles = partnerRoles;
 }

public PartnerRoleRec getPartnerRoleByCode(String code){
 
 for( PartnerRoleRec rec : getPartnerRoles()){
  if(rec.getRoleCode().equals(code)){
   return rec;
  }
 }
 return null;
}
 

 
 public List<PaymentTermsRec> getPaymentTermsList() {
  if(paymentTermsList == null) {
   paymentTermsList = configDM.getAllPaymentTerms();
   
   Collections.sort(paymentTermsList, new PaymentTermsByBasisDays());
  }
  LOGGER.log(INFO, "Buffer getPaymentTermsList() returns {0}", paymentTermsList);
  return paymentTermsList;
 }

 public void setPaymentTermsList(ArrayList<PaymentTermsRec> paymentTermsList) {
  this.paymentTermsList = paymentTermsList;
 }

 public List<PaymentTypeRec> getPaymentTypes() {
  if(paymentTypes == null){
   paymentTypes = configDM.getAllPaymentTypes();
   LOGGER.log(INFO, "SysBuff Payment types from DB layer {0}", paymentTypes);
  }
  return paymentTypes;
 }
 
 public List<PaymentTypeRec> getPaymentTypesByLedCode(String cd) {
  if(paymentTypes == null){
   paymentTypes = configDM.getAllPaymentTypes();
   LOGGER.log(INFO, "SysBuff Payment types from DB layer {0}", paymentTypes);
  }
  List<PaymentTypeRec> retList = new ArrayList<>();
  for(PaymentTypeRec p:paymentTypes){
   if(StringUtils.equals(p.getLedger().getCode(), "AP")){
    retList.add(p);
   }
  }
  return retList;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public PaymentTypeRec getPaymentTypeByCode(String code, CompanyBasicRec comp){
  LOGGER.log(INFO,"getPaymentTypeByCode called with code {0} comp {1}", new Object[]{code,comp});
  for(PaymentTypeRec curr:getPaymentTypes()){
   if (curr.getPayTypeCode().equals(code) && curr.getCompany().getId().equals(comp.getId())){
    return curr;
  }
  }
  return null;
 }
 
 public PaymentTypeRec getPaymentTypeByMeduim(String mediumCode, String ledger,
   boolean inbound,CompanyBasicRec comp){
  LOGGER.log(INFO, "SysBuff called with medium {0} inbound {1} comp id {2}", 
    new Object[]{mediumCode,inbound,comp.getId()});
  mediumCode = "CHQ";
  for(PaymentTypeRec pt:getPaymentTypes()){
   LOGGER.log(INFO, "pt id {0}pt.medium {1} medium Code {2} ledger {3} comp id {4} inbound {5} ", 
     new Object[]{pt.getId(),pt.getPayMedium(),mediumCode,pt.getLedger().getCode(),
      comp.getId(),inbound});
   if(StringUtils.equals(pt.getPayMedium(), mediumCode) &&
     StringUtils.equals(pt.getLedger().getCode(), ledger) &&
     Objects.equals(pt.getCompany().getId(), comp.getId()) && pt.isInbound() == inbound){
    LOGGER.log(INFO, "Found payment type");
    return pt;
   }
  }
  LOGGER.log(INFO, "Could not Find payment type");
  return null;
  
 }
 public List<FundRec> getRestrictedFunds(CompanyBasicRec comp) {
  if(restrictedFunds == null){
   restrictedFunds = this.compDM.getRestrictedFundForComp(comp);
  }
  return restrictedFunds;
 }

 public void setRestrictedFunds(ArrayList<FundRec> restrictedFunds) {
  this.restrictedFunds = restrictedFunds;
 }
 
 
 public FundRec getRestrictedFundById(long id,CompanyBasicRec comp){
  FundRec fund = null;
  
  ListIterator<FundRec> fundLi = this.getRestrictedFunds(comp).listIterator();
  while(fundLi.hasNext()){
   fund = fundLi.next();
   if(fund.getId() == id){
    return fund;
   }
  }
  return fund;
  
 }
 
 public List<SalesPartRec> getSalesParts() {
  if(salesParts == null){
   salesParts = salesDM.getAllSalesParts();
  }
  return salesParts;
 }
 
 public void setSalesParts(List<SalesPartRec> salesParts) {
  this.salesParts = salesParts;
 }

 public List<SortOrderRec> getSortOrdersByName(String name){
  
  
  List<SortOrderRec> sOrd = getSortOrders();
  if(StringUtils.isBlank(name)){
   return sOrd;
  }
  
  if(sOrd == null || sOrd.isEmpty()){
   return null;
  }
  List<SortOrderRec> retList = new ArrayList<>();
  
  for(SortOrderRec s: sOrd){
   
   if(StringUtils.startsWithIgnoreCase(s.getName(), name)){
    retList.add(s);
   }
  }
  return retList;
 }
 
 public ArrayList<SortOrderRec> getSortOrders() {
  if(sortOrders == null){
   sortOrders = this.configDM.getSortOrders();
  }
  Collections.sort(sortOrders, new SortOrderByCode());
  return sortOrders;
 }

 public void setSortOrders(ArrayList<SortOrderRec> sortOrders) {
  this.sortOrders = sortOrders;
 }

    
    public List<ModuleRec> getModules() {
        
        if(modules == null){
            modules = configDM.getAllModules();
        }

        return modules;
    }

    public void setModules(ArrayList<ModuleRec> modules) {
        this.modules = modules;
    }
    
    public ModuleRec getModuleByName(String name){
      this.getModules();
     for(ModuleRec m: modules){
      if(m.getModuleCode().equals(name)){
       return m;
      }
     }
     return null;
    }
    
    public ModuleRec getModuleByCode(String code){
     this.getModules();
     for(ModuleRec m: modules){
      if(m.getModuleCode().equals(code)){
       
       return m;
      }
     }
     return null;
    }
    
    public ModuleRec getModuleById(Long modId){
     ListIterator<ModuleRec> modLi = this.getModules().listIterator();
     while(modLi.hasNext()){
      ModuleRec modRec = modLi.next();
      
      if(Objects.equals(modRec.getId(), modId)){
       return modRec;
      }
     }
     return null;
    }

 public HashMap<Integer, String> getNumberText() {
  
  return numberText;
 }

 public void setNumberText(HashMap<Integer, String> numberText) {
  this.numberText = numberText;
 }

 public HashMap<String, String> getNumberPlaceText() {
  return numberPlaceText;
 }

 public void setNumberPlaceText(HashMap<String, String> numberPlaceText) {
  this.numberPlaceText = numberPlaceText;
 }

 
 public List<NumberRangeRec> getNumRanges() {
  if(numRanges == null){
   numRanges = this.configDM.getNumCntrlAll();
  }
  return numRanges;
 }

 public void setNumRanges(List<NumberRangeRec> numRanges) {
  this.numRanges = numRanges;
 }

 public List<NumberRangeRec> getNumRangesOfType(NumberRangeTypeRec nrTy) {
  List<NumberRangeRec> rngs = getNumRanges();
  if(rngs == null){
   return null;
  }
  List<NumberRangeRec> rngsOfTy = new ArrayList<>();
  for(NumberRangeRec curr:rngs){
   if(Objects.equals(curr.getNumberRangeType().getId(), nrTy.getId())){
    rngsOfTy.add(curr);
   }
  }
  if(rngsOfTy.isEmpty()){
   return null;
  }else{
   return rngsOfTy;
  }
 }
 public NumberRangeRec getNumRangeByShortDescr(String descr){
  getNumRanges();
  for(NumberRangeRec nr:numRanges){
  if(nr.getShortDescr().equals(descr)){
   return nr;
  }
 }
  return null;
 }
 public NumberRangeRec getNumRangeById(Long numRangeId){
  ListIterator<NumberRangeRec> numRngLi = this.getNumRanges().listIterator();
  while(numRngLi.hasNext()){
   NumberRangeRec numRng = (NumberRangeRec)numRngLi.next();
   
   if(numRng.getNumberControlId() == numRangeId){
    return numRng;
   }
  }
  return null;
 }

 public NumberRangeTypeRec getNumRangeTypeById(Long id){
  NumberRangeTypeRec nrTyRec = null;
  List<NumberRangeTypeRec> nrTypes = getNumRangeTypes();
  if(nrTypes == null){
   return null;
  }
  for(NumberRangeTypeRec curr:nrTypes){
   if(Objects.equals(curr.getId(), id))
   {
    return curr;
   }
  }
  return null;
 }
 public List<NumberRangeTypeRec> getNumRangeTypes() {
  if(numRangeTypes == null){
   numRangeTypes = this.configDM.getNumberRangeTypesAll();
   if(numRangeTypes != null){
    Collections.sort(numRangeTypes, new NumRangeTypeByCode());
   }
  }
  return numRangeTypes;
 }

 public void setNumRangeTypes(List<NumberRangeTypeRec> numRangeTypes) {
  this.numRangeTypes = numRangeTypes;
 }
 
 
 public NumberRangeRec updateNumberControl(NumberRangeRec nr, String src){
  LOGGER.log(INFO, "updateNumberControl called with {0}", nr);
  
  nr = this.configDM.updateNumControl(nr, src);
  ListIterator<NumberRangeRec> numRngLi = this.getNumRanges().listIterator();
  while(numRngLi.hasNext()){
   NumberRangeRec numRng = (NumberRangeRec)numRngLi.next();
   
   if(numRng.getNumberControlId() == nr.getNumberControlId()){
    numRngLi.set(nr);
    return nr;
   }
  }
  numRngLi.add(nr);
  return nr;
 }
 
 public NumberRangeTypeRec updateNumberRngType(NumberRangeTypeRec nrTy, String pg){
  LOGGER.log(INFO, "Called sysBuff update num Rng ty with type id {0}", nrTy.getId());
  nrTy = configDM.upateNumberRangeType(nrTy, pg);
  ListIterator<NumberRangeTypeRec> li = numRangeTypes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   NumberRangeTypeRec curr = li.next();
   if(Objects.equals(curr.getId(), nrTy.getId())){
    found = true;
    li.set(nrTy);
   }
  }
  if(found){
   Collections.sort(numRangeTypes, new NumRangeTypeByCode());
  }
  return nrTy;
 }

 public void setTransactionTypes(List<TransactionTypeRec> transactionTypes) {
  this.transactionTypes = transactionTypes;
 }

    
 public List<UomRec> getUoms() {
  if(uoms == null){
   uoms = this.configDM.getAllUoms();
  }
  return uoms;
 }

 public void setUoms(ArrayList<UomRec> uoms) {
  this.uoms = uoms;
 }
 
 public UomRec getUom(Uom uom){
  UomRec rec;
  ListIterator<UomRec> li = getUoms().listIterator();
  boolean found = false;
  while(li.hasNext()){
   UomRec uomRec = li.next();
   if(uomRec.getId() == uom.getId()){
    return uomRec;
   }
  }
  if(!found){
   throw new BacException("Could not find UOM in system buffer");
  }
  rec = new UomRec();
  return rec;
 }

 public UomRec getUomByCode(String code){
  for(UomRec uom: this.getUoms()){
   if(uom.getUomCode().equals(code)){
    return uom;
   }
  }
  return null;
 }
 public List<ValueDateRec> getValueDates() {
  return valueDates;
 }

 public ValueDateRec addValueDate(ValueDateRec vd){
  
  return vd;
 }
 
 public FisPeriodRuleRec getFisPeriodRuleByName(String name){
  LOGGER.log(INFO, "getFisPeriodRuleByName called with name {0}", name);
  List<FisPeriodRuleRec> rules = getFisPeriodRules();
  LOGGER.log(INFO, "rules {0}", rules);
  for(FisPeriodRuleRec rec:rules){
   LOGGER.log(INFO, "FisPeriodRuleRec rule {0}", rec.getPeriodRule());
   if(rec.getPeriodRule().equals(name)){
    return rec;
   }
  }
  return null;
 }
 public FisPeriodRuleRec getFisPeriodRule(Long ruleId){
   
  List<FisPeriodRuleRec> rules = getFisPeriodRules();
  for(FisPeriodRuleRec rec:rules){
   
   if(rec.getId() == ruleId){
    return rec;
   }
  }
  
  return null;
 }
 
 public FisPeriodRuleRec getFisPeriodRuleCal(Long ruleId){
  List<FisPeriodRuleRec> rules = getFisPeriodRules();
  ListIterator<FisPeriodRuleRec> li = rules.listIterator();
  
  while(li.hasNext() ){
   FisPeriodRuleRec rec = li.next();
   if(rec.getId() == ruleId){
    if(rec.getCalendarRule() == null){
     rec = compDM.getFisPerCalRule(rec);
     li.set(rec);
     return rec;
    }
     
   }
  }
 
  return null;
 }
 
 public FisPeriodRuleRec fisPeriodRuleCalUpdate(FisPeriodRuleRec perRule,UserRec usr, String pg){
  LOGGER.log(INFO, "fisPeriodRuleCalUpdate called with rule id {0}", perRule.getId());
  ListIterator<FisPeriodRuleRec> rulesLi = this.fisPeriodRules.listIterator();
  
  while(rulesLi.hasNext() ){
   FisPeriodRuleRec currRule = rulesLi.next();
   if(currRule.getId() == perRule.getId()){
    currRule = this.compDM.updatePeriodRule(perRule, pg);
    rulesLi.set(currRule);
    return currRule;
   }
  
  }
  LOGGER.log(INFO, "Could not find rule to update {0}", perRule.getId());
  return perRule;
 }
 public FisPeriodRuleRec fisPeriodRuleCalUpdateYears(FisPeriodRuleRec perRule,UserRec usr, String pg){
  perRule = this.compDM.updatePeriodRuleCalYearPeriods(perRule, usr,pg);
  LOGGER.log(INFO, "sysBuff.fisPeriodRuleCalUpdateYears compDM returned {0}", perRule);
  return perRule;
 }
 public ArrayList<FisPeriodRuleRec> getFisPeriodRules() {
  if(fisPeriodRules == null || fisPeriodRules.isEmpty()){
   fisPeriodRules = compMgr.getPeriodRules();
  }
  return fisPeriodRules;
 }

 public void setFisPeriodRules(ArrayList<FisPeriodRuleRec> fisPeriodRules) {
  this.fisPeriodRules = fisPeriodRules;
 }
 
 public void fiscalPerRuleAdd(FisPeriodRuleRec rec){
  LOGGER.log(INFO, "fiscalPerRuleAdd called with rec id {0}", rec.getId());
  //ListIterator<FisPeriodRuleRec> li = getFisPeriodRules().listIterator();
  boolean foundRule = false;
  for(FisPeriodRuleRec perRule: getFisPeriodRules()){
   if(perRule.getId() == rec.getId())
    foundRule = true;
  }
  if(!foundRule){
   getFisPeriodRules().add(rec);
  }
  
  LOGGER.log(INFO, "num of period rules {0}", getFisPeriodRules().size());
 }

 public List<FundCategoryRec> getFundCategories() {
  if(fundCategories == null || fundCategories.isEmpty()){
   fundCategories = this.setup.getFundCategoriesAll();
  }
  return fundCategories;
 }

 public void setFundCategories(List<FundCategoryRec> fundCategories) {
  this.fundCategories = fundCategories;
 }
 
 
    public List<CalendarRuleBaseRec> getCalendarRules() {
     if(calendarRules == null || calendarRules.isEmpty()){
      calendarRules = this.compDM.getCalendarRules();
     }
        return calendarRules;
    }

    public void setCalendarRules(ArrayList<CalendarRuleBaseRec> calendarRules) {
        this.calendarRules = calendarRules;
    }

    public CalendarRuleBaseRec getCalendarFlexYears(CalendarRuleBaseRec cal){
     LOGGER.log(INFO, " sysbuff getCalendarFlexYears called with {0}", cal);
     boolean foundRule = false;
     if(calendarRules == null || calendarRules.isEmpty()){
      getCalendarRules();
     }
     ListIterator<CalendarRuleBaseRec> li = calendarRules.listIterator();
     CalendarRuleBaseRec buffRule = null;
     while(li.hasNext() && !foundRule){
      buffRule = li.next();
      if(buffRule.getId() == cal.getId()){
       foundRule = true;
       LOGGER.log(INFO, "Rule found in sys buff list");
       if(buffRule.getCalRuleFlexYears() != null && !buffRule.getCalRuleFlexYears().isEmpty()){
        cal.setCalRuleFlexYears(buffRule.getCalRuleFlexYears());
        LOGGER.log(INFO, "Years from sys buff copy {0}", buffRule.getCalRuleFlexYears());
        return cal;
       }else{
        
        cal = compDM.getCalendarRuleFlexYears(cal);
        LOGGER.log(INFO, "Years found from comDM {0}", cal.getCalRuleFlexYears());
        li.set(cal);
        return cal;
       }
      }
     }
     if(!foundRule){
      cal = compDM.getCalendarRuleFlexYears(cal);
      LOGGER.log(INFO, "New rule added with years {0}", cal.getCalRuleFlexYears());
      li.add(cal);
      return cal;
     }
     
     return cal;
    }
    
   /*
    public ArrayList<CalendarRuleBaseRec> getCalendarRuleList(int calendarType) throws BacException {
        if(calendarRules == null){
            //first time access so need to get the calendar rules
            this.compMgr.getCalRuleByTypeList(calendarType);
        }

        return null;
    }
*/
  public List<CompanyBasicRec> getCompanies() throws BacException {
    LOGGER.log(INFO,"Sys Buffer getCompanies called. Companies: {0} ",companies);
    if(companies == null || companies.isEmpty()){
      companies = compMgr.getCompanies();
      LOGGER.log(INFO, "Company Mgr returns companies {0}", companies);
     }
    return companies;

  }

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  if(companies == null){
     this.getCompanies();
  }
  ListIterator<CompanyBasicRec> compLi = companies.listIterator();
  boolean foundComp = false;
  while(compLi.hasNext() && !foundComp){
   CompanyBasicRec currComp = compLi.next();
   if(currComp.getId() == company.getId()){
    compLi.set(company);
     foundComp = true;
   }
  }
  if(!foundComp){
   companies.add(company);
  }
  this.company = company;
 }

 public CompanyBasicRec getCompany(String ref) throws BacException {
    LOGGER.log(INFO,"Sys Buffer getCompany called with ref: {0}",ref);
    if(companies == null){
     this.getCompanies();
    }
    for(CompanyBasicRec comp : companies){
     LOGGER.log(INFO, "comp ref {0} param ref {1}", new Object[]{comp.getReference(),ref});
     if(comp.getReference().equalsIgnoreCase(ref)){
      return comp;
     }
    }
    return null;
  }

 public CompanyBasicRec getCompBasicArApAging(CompanyBasicRec comp){
  LOGGER.log(INFO, "Sys Buff getCompBasicArApAging called with comp ref {0}", comp.getReference());
  if(getCompanies() == null){
   LOGGER.log(INFO, "No companies");
   return comp;
  }
  
  ListIterator<CompanyBasicRec> compLi = this.getCompanies().listIterator();
  while(compLi.hasNext()){
   CompanyBasicRec c = compLi.next();
   if(Objects.equals(c.getId(), comp.getId()) ){
    if(c.getCompApAr() == null){
     c = compDM.getCompArAP(comp);
     compLi.set(c);
     return c;
    }
   }
  }
  return comp;
 }
  public CompanyBasicRec getCompanyById(Long compId)throws BacException{
    //CompanyBasicRec comp = null;
    if(compId == null || compId == 0){
      throw new BacException("No company Selected","SYS:01");
    }
    boolean found = false;
    if(companies == null){
     this.getCompanies();
    }
    
    ListIterator<CompanyBasicRec> li = companies.listIterator();
    while(li.hasNext()&& !found){
      CompanyBasicRec rec = (CompanyBasicRec)li.next();
      if(rec.getId()==compId){
       return rec;
        
      }

    }
    if(!found){
     LOGGER.log(INFO, "Company not found {0}");
      throw new BacException("Company not found","SYSBUFF:02" +compId);
    }
    return null;
  }

 public List<CompanyFiscalPeriodYearForDate> getCompFiscalPerYrForDate() {
  LOGGER.log(INFO,"getCompFiscalPerYrForDate {0}",compFiscalPerYrForDate);
  if(compFiscalPerYrForDate == null){
   compFiscalPerYrForDate = new ArrayList<> (PERIOD_LIST_SIZE);
   
  }
  return compFiscalPerYrForDate;
 }
 
 
 public FiscalPeriodYearRec getCompFiscalPeriodYearForDate(CompanyBasicRec comp, Date date){
  LOGGER.log(INFO, "chart of accounts {0}", comp.getChartOfAccounts().getId());
  LOGGER.log(INFO, "compFiscalPerYrForDate {0}", compFiscalPerYrForDate);
  if(compFiscalPerYrForDate == null){
   compFiscalPerYrForDate = new ArrayList<>();
  }
  
  for(CompanyFiscalPeriodYearForDate curr:compFiscalPerYrForDate){
   LOGGER.log(INFO, "curr fis yr {0} Period {1}", 
     new Object[]{curr.getFisPeriodYear().getYear(),curr.getFisPeriodYear().getPeriod()});
   if(Objects.equals(curr.getComp().getId(), comp.getId()) && curr.getDate().equals(date)){
    LOGGER.log(INFO, "Found fis per in buff date {0}", curr.getDate());
    LOGGER.log(INFO, "Fisc Period {0} year {1}", 
      new Object[]{curr.getFisPeriodYear().getPeriod(),curr.getFisPeriodYear().getYear()});
    return curr.getFisPeriodYear();
   }
  }
  
  LOGGER.log(INFO, "Need to determine fis per");
   ChartOfAccountsRec chart = comp.getChartOfAccounts();
   LOGGER.log(INFO, "Period rule  {0}", comp.getChartOfAccounts().getPeriodRule());
    FisPeriodRuleRec perRule = comp.getChartOfAccounts().getPeriodRule();
   if(perRule == null){
    // need to get period rule
    chart = this.compDM.getFisPeriodRuleRecForCoa(chart);
    comp.setChartOfAccounts(chart);
    boolean compFound = false;
    ListIterator<CompanyBasicRec> compLi = this.companies.listIterator();
    while(compLi.hasNext() && !compFound){
     CompanyBasicRec curr = compLi.next();
     if(Objects.equals(curr.getId(), comp.getId()) ){
      compFound = true;
      compLi.set(comp);
     }
    }
    LOGGER.log(INFO, "companies in sys buff {0}", this.companies);
    LOGGER.log(INFO, "chart per rule id{0}", chart.getPeriodRule().getId());
    perRule = chart.getPeriodRule();
    LOGGER.log(INFO, "Per rule flex {0} cal {1} natural Cal {2} ", new Object[]{perRule.isAnnualDateScheduleBasis()
            ,perRule.isCalendarMonthBasis(), perRule.isCalendarNatural()});
   }
  
  FiscalPeriodYearRec fisPer = new FiscalPeriodYearRec();
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(comp.getLocale());
  cal.setTime(date);
  if(perRule.isAnnualDateScheduleBasis()){
   LOGGER.log(INFO, "flexible cal");
  }else if (perRule.isCalendarMonthBasis()){
   
   LOGGER.log(INFO, "calendar basis");
   CalendarRuleMonthRec calRule = (CalendarRuleMonthRec)perRule.getCalendarRule();
   int startMth = calRule.getStartMonthNumber();
   LOGGER.log(INFO, "Month offset {0} per rule descr {1}", new Object[]{startMth,perRule.getPeriodDescr()});
   int calMonth = cal.get(Calendar.MONTH) +1;
   int calYear = cal.get(Calendar.YEAR);
   int perMonth;
   if(calMonth == startMth){
    perMonth = 0;
    calYear++;
   }else if(calMonth < startMth){
    perMonth = 12 - (startMth - calMonth);
    
   }else{
    perMonth = calMonth - startMth;
    calYear++;
   }
   perMonth++;
   fisPer.setPeriod(perMonth);
   
   fisPer.setYear(calYear);
   
  }else{
   LOGGER.log(INFO, "Natural calendar");
   fisPer.setYear(cal.get(Calendar.YEAR));
   fisPer.setPeriod(cal.get(Calendar.MONTH) + 1);
  }
  CompanyFiscalPeriodYearForDate compFisPer = new CompanyFiscalPeriodYearForDate();
  compFisPer.setComp(comp);
  compFisPer.setCompanyId(comp.getId());
  compFisPer.setDate(date);
  compFisPer.setFisPeriodYear(fisPer);
  if(compFiscalPerYrForDate.size() < SysBuffer.MAX_PER_DATES){
   compFiscalPerYrForDate.add(compFisPer);
  }else{
   compFiscalPerYrForDate.remove(0);
   compFiscalPerYrForDate.add(compFisPer);
  }
  LOGGER.log(INFO, "Buffer {0}", compFiscalPerYrForDate);
  return fisPer;
  
 }

 public void setCompFiscalPerYrForDate(List<CompanyFiscalPeriodYearForDate> compFiscalPerYrForDate) {
  LOGGER.log(INFO, "setCompFiscalPerYrForDate called with {0}", compFiscalPerYrForDate);
  this.compFiscalPerYrForDate = compFiscalPerYrForDate;
 }

public CompanyBasicRec getCompFunds(CompanyBasicRec comp){
 LOGGER.log(INFO, "getCompFunds called with comp {0}", comp.getId());
 
 ListIterator<CompanyBasicRec> compLi = this.companies.listIterator();
 boolean compFound = false;
 while(compLi.hasNext() && !compFound){
  
  CompanyBasicRec curr = compLi.next();
  LOGGER.log(INFO, "comp id {0} curr list id {1}", new Object[]{comp.getId(), curr.getId()});
  if(Objects.equals(curr.getId(), comp.getId())){
   if(comp.getFundList() != null && !comp.getFundList().isEmpty() ){
    LOGGER.log(INFO, "comp.getFundList() {0}", comp.getFundList());
    //return comp;
   }
   List<FundRec> funds = compDM.getRestrictedFundForComp(comp);
   LOGGER.log(INFO, "Funds returned from compDM {0}", funds);
   comp.setFundList(funds);
   compLi.set(comp);
   
   return comp;
  }
 }
 
 
 return comp;
}



  public ArrayList<CompanyBasicRec> getCompaniesForChart(String coaRef) throws BacException {
    ArrayList<CompanyBasicRec> compList = new ArrayList<>();
    if(companies == null){
      companies = this.getCompanies();
    }
    ListIterator li = companies.listIterator();
    while(li.hasNext()){
      CompanyBasicRec comp = (CompanyBasicRec)li.next();
      if(comp.getChartOfAccounts().getReference().equalsIgnoreCase(coaRef)){
       compList.add(comp);
       
      }
    }

    return compList;
  }

  public List<ChartOfAccountsRec> getChartsOfAccounts() throws BacException {
    if(chartsOfAccounts == null){
      chartsOfAccounts = compMgr.getChartsOfAccounts();
        }
    return chartsOfAccounts;
  }
  
 public List<ChartOfAccountsRec> getChartsOfAccountsByRef(String ref){
  
  getChartsOfAccounts();
  if(chartsOfAccounts == null){
   return null;
  }
  List<ChartOfAccountsRec> retList = new ArrayList();
  for(ChartOfAccountsRec curr: chartsOfAccounts){
   if(StringUtils.startsWith(curr.getReference(), ref)){
    retList.add(curr);
   }
  }
  if(retList.isEmpty()){
   return null;
  }else{
   return retList;
  }
 }

 public ChartOfAccountsRec getChartOfAccountsbyId(Long chartId){
  List<ChartOfAccountsRec> chartAcntsList = getChartsOfAccounts();
  if(chartAcntsList == null || chartAcntsList.isEmpty()){
   return null;
  }
  for(ChartOfAccountsRec chartRec:chartAcntsList){
   if(Objects.equals(chartRec.getId(), chartId)){
    LOGGER.log(INFO, "Found chartOd accounts with ID {0}",chartId);
    return chartRec;
   }
  }
  LOGGER.log(INFO, "No chart found for id {0}", chartId);
  return null;
 }
 
 
 
 public ChequeVoidReasonRec getChqVoidReasonById(Long id){
  if (getChqVoidReasons() == null){
   return null;
  }
  for(ChequeVoidReasonRec rsn: chqVoidReasons){
   if(Objects.equals(rsn.getId(), id)){
    return rsn;
   }
   
  }
  return null;
 }
 public List<ChequeVoidReasonRec> getChqVoidReasons() {
  if(chqVoidReasons == null){
   chqVoidReasons = this.configDM.getChequeVoidReasonRecsAll();
  }
  return chqVoidReasons;
 }

 public void setChqVoidReasons(List<ChequeVoidReasonRec> chqVoidReasons) {
  this.chqVoidReasons = chqVoidReasons;
 }

  
  public SortOrderRec getSortOrderByCode(String code){
   SortOrderRec rec = null;
   if(sortOrders == null || sortOrders.isEmpty() ){
      sortOrders = this.getSortOrders();
   }
   for(SortOrderRec s:sortOrders){
    if(s.getSortCode().equals(code)){
     return s;
    }
   }
   
   return rec;
  }
  
  public SortOrderRec getSortOrderById(Long id){
    SortOrderRec rec;
    if(sortOrders == null || sortOrders.isEmpty() ){
      sortOrders = this.getSortOrders();
    }
    ListIterator li = sortOrders.listIterator();
    boolean found = false;
    while(li.hasNext()){
      rec = (SortOrderRec)li.next();
      if(Objects.equals(rec.getId(), id)){
        found = true;
        return rec;
      }

    }
    if(!found){
      return null;
    }



    return null;
  }
  public List<SortOrderRec> getSortOrdersByCodePrefix(String prefix) throws BacException {
   LOGGER.log(INFO, "getSortOrdersByCodePrefix called with {0}", prefix);
   if(StringUtils.isBlank(prefix)){
    return this.getSortOrders();
   }
  List<SortOrderRec> retList = new ArrayList<>(); 
  for(SortOrderRec curr:getSortOrders()){
   if(StringUtils.startsWith(curr.getSortCode(), prefix)){
    retList.add(curr);
   }
  } 
  Collections.sort(retList, new SortOrderByCode());
  return retList;
    
    
  }
  
  public List<SortOrderRec> getSortOrdersByNamePrefix(String prefix) throws BacException {
   LOGGER.log(INFO, "getSortOrdersByCodePrefix called with {0}", prefix);
   if(StringUtils.isBlank(prefix)){
    return this.getSortOrders();
   }
  List<SortOrderRec> retList = new ArrayList<>(); 
  for(SortOrderRec curr:getSortOrders()){
   if(StringUtils.startsWith(curr.getName(), prefix)){
    retList.add(curr);
   }
  } 
  Collections.sort(retList, new SortOrderByName());
  return retList;
    
    
  }
 
  

 public PaymentTypeRec getPostTypeBnkGlAcnt(PaymentTypeRec ptRec){
  ptRec = configDM.getPaymentTypeBnkAcntRec(ptRec);
  
  if(paymentTypes == null || paymentTypes.isEmpty()){
   paymentTypes = new ArrayList<>();
   paymentTypes.add(ptRec);
   return ptRec;
  }
  
  boolean ptFound = false;
  ListIterator<PaymentTypeRec> li = paymentTypes.listIterator();
  while(li.hasNext() && !ptFound){
   PaymentTypeRec c = li.next();
   if(Objects.equals(c.getId(), ptRec.getId())){
    li.set(ptRec);
    ptFound = true;
    
   }
  }
  
  LOGGER.log(INFO, "Payment type in list updated with bank GL");
  return ptRec;
  
  
 }
 
 public List<PostTypeRec> getPostTypes() throws BacException {
    
    if(postTypes == null){
     LOGGER.log(INFO, "get post types from Comp manager ");
     postTypes = compMgr.getPostTypes();
     Collections.sort(postTypes, new PostTypeByDescr());
    }
    return postTypes;
  }

 public List<PostType> getPostTypeList() {
  if(postTypeList == null){
   postTypeList = this.configDM.getPostingTypeList();
  }
  return postTypeList;
 }

 public void setPostTypeList(ArrayList<PostType> postTypeList) {
  this.postTypeList = postTypeList;
 }
 
 
  
  
  



  public List<LedgerRec> getLedgers() throws BacException {
   
    if(ledgers == null){
      ledgers = configDM.getLedgersAll();
    }
    return ledgers;
  }

  public LedgerRec getLedgerById(Long ledId){
   ListIterator<LedgerRec> ledLi = this.getLedgers().listIterator();
   while(ledLi.hasNext()){
    LedgerRec led = ledLi.next();
    if(led.getId() == ledId){
     return led;
    }
   }
   return null;
  }
  
  public LedgerRec getLedgerByName(String name) throws BacException {
    ledgers = this.getLedgers();
    ListIterator it = ledgers.listIterator();
    boolean found = false;
    while(it.hasNext() && !found){
     LedgerRec rec = (LedgerRec)it.next();
     if(rec.getName().equalsIgnoreCase(name)){
       found = true;
       return rec;
     }
    }

    return null;
  }
/**
 * retrieve the posting codes for a given ledger
 * @param ledgerName
 * @return
 * @throws BacException
 */
  public ArrayList<PostTypeRec> getPostCodesForLedger(String ledgerName) throws BacException {
    ArrayList<PostTypeRec> ledgerPostTypes = new ArrayList<>();
    if(postTypes == null){
      this.getPostTypes();
    }

    ListIterator it = postTypes.listIterator();
    boolean found = false;
    while(it.hasNext() ){
      PostTypeRec ptype = (PostTypeRec)it.next();
      if(ptype.getLedger().getName().equalsIgnoreCase(ledgerName)){
        ledgerPostTypes.add(ptype);
        found = true;
      }
    }
    
    return ledgerPostTypes;
  }
/**
 * Called by client to get document types from buffer
 * @return
 * @throws BacException
 */
  public List<DocTypeRec> getAllDocumentTypes(UserRec usr) throws BacException {
    if(docTypes == null){
      docTypes = docDM.getAllDocumentTypes(usr);
    }
    return docTypes;
  }

  /**
   * Returns the Document types for the given ledger
   * @param ledger
   * @return
   * @throws BacException
   */
  public ArrayList<DocTypeRec> getLedgerDocumentTypes(String ledger, UserRec usr) throws BacException {
    ArrayList<DocTypeRec> docs = new ArrayList<>();
    DocTypeRec doc;
    if(docTypes == null){
      docTypes = this.getAllDocumentTypes(usr);
    }
    ListIterator<DocTypeRec> li = docTypes.listIterator();
    while(li.hasNext()){
      doc = li.next();
      if(ledger.equalsIgnoreCase("GL")){
        if(doc.isGlAllowed()){
          docs.add(doc);
        }
      }else if(ledger.equalsIgnoreCase("SL")){
        if(doc.isArAllowed()){
          docs.add(doc);
        }
      }else if(ledger.equalsIgnoreCase("AR")){
        if(doc.isArAllowed()){
          docs.add(doc);
        }
      }else if(ledger.equalsIgnoreCase("AP")){
        if(doc.isApAllowed()){
          docs.add(doc);
        }
      }else if(ledger.equalsIgnoreCase("TR")){
        if(doc.isTrAllowed()){
          docs.add(doc);
        }
      }
      
    }
    return docs;
    
  }

  
/**
 * returns the restricted funds for a company
 * @param comp
 * @return
 * @throws BacException
 */
  public List<FundRec> getRestrictedFundsForComp(CompanyBasicRec comp)
          throws BacException {
    LOGGER.log(INFO,"getRestrictedFundsForComp called for comp: {0}",comp.getReference());
    List<CompanyBasicRec> comps = this.getCompanies();
    List<FundRec> fundLst;
    fundLst = comp.getFundList();
    LOGGER.log(INFO, "comp.getFundList {0}", fundLst);
    
    if(fundLst == null || fundLst.isEmpty()){
     fundLst = compDM.getRestrictedFundForComp(comp);
     comp.setFundList(fundLst);
     LOGGER.log(INFO, "Fund list returned from DM {0}", fundLst);
     if(fundLst != null && !fundLst.isEmpty() ){
      ListIterator<CompanyBasicRec> compLi = getCompanies().listIterator();
      boolean found = false;
      while(compLi.hasNext() && !found){
       CompanyBasicRec curr = compLi.next();
       if(curr.getId() == comp.getId()){
        compLi.set(comp);
        found = true;
       }
      }
      if(!found){
      throw new BacException("No fund lsit found for company ref: "+comp.getReference());
    }
     }
    }else{
     
     return fundLst;
    }
    
    
    return null;
  }

  public PaymentTermsRec getPaymentTermsByCode(String termsCode){
   LOGGER.log(INFO, "getPaymentTermsByCode called with code {0}", termsCode);
   LOGGER.log(INFO, "paymentTermsList {0}",paymentTermsList);
   if(paymentTermsList == null){
    paymentTermsList = this.getPaymentTermsList();
    LOGGER.log(INFO, "after call tp configDM {0}",paymentTermsList);
   }
   if(paymentTermsList == null){
    return null;
    
   }
   for(PaymentTermsRec pt:paymentTermsList){
    if(StringUtils.equals(pt.getPayTermsCode(), termsCode)){
     return pt;
    }
   }
   return null;
  }
  public PaymentTermsRec getPaymentTerms(PaymentTerms t){
   PaymentTermsRec rec = null;
   if(paymentTermsList == null){
    paymentTermsList = this.getPaymentTermsList();
   }
   ListIterator<PaymentTermsRec> payTermsLi = paymentTermsList.listIterator();
   boolean found=false;
   while(payTermsLi.hasNext() && !found){
    PaymentTermsRec test = payTermsLi.next();
    if(test.getId() == t.getId()){
     
     return test;
    }
   }
   return rec;
  }
  
  
  
  public PaymentTypeRec getPaymentType(PaymentType t){
   PaymentTypeRec rec = null;
   if(paymentTypes == null){
    paymentTypes = configDM.getAllPaymentTypes();
   }
   ListIterator<PaymentTypeRec> payTypesLi = paymentTypes.listIterator();
   while(payTypesLi.hasNext()){
    PaymentTypeRec test = payTypesLi.next();
    if(test.getId() == t.getId()){
     return test;
    }
   }
   
   
   return rec;
  }
  
  
  public List<PaymentTypeRec> getPaymentTypes(CompanyBasicRec comp) throws BacException {
    List<PaymentTypeRec> retList = new ArrayList<>();
    if(paymentTypes == null || paymentTypes.isEmpty() ){
      // get payment types from DB layer
     try{
        paymentTypes = configDM.getAllPaymentTypes();
        if(paymentTypes == null || paymentTypes.isEmpty()){
         return new ArrayList<>();
        }else{
         ListIterator<PaymentTypeRec> li = paymentTypes.listIterator();
         while(li.hasNext()){
          PaymentTypeRec pt = li.next();
          
          if(pt.getCompany() != null && Objects.equals(pt.getCompany().getId(), comp.getId())){
           retList.add(pt);
          }
         }
         return retList;
        } 
      }catch (BacException ex){
        throw new BacException("DB error getting payment terms: "+ex.getErrorCode(),"SYSBUFF_PTY:01");

      }
    }
    for(PaymentTypeRec pt : paymentTypes){
     if(pt.getCompany() != null && Objects.equals(pt.getCompany().getId(), comp.getId())){
      retList.add(pt);
     }
    }
    return retList;
  }
  
  public List<PaymentTypeRec> getPaymentTypesForLed(CompanyBasicRec comp, String ledCode){
   LOGGER.log(INFO, "getPaymentTypesForLed called with code {0}", ledCode);
   List<PaymentTypeRec> retList = new ArrayList<>();
    LOGGER.log(INFO, "retList is {0}",retList);
    if(paymentTypes == null || paymentTypes.isEmpty() ){
      // get payment types from DB layer
     LOGGER.log(INFO, "Get payment types from DB");
      try{
        paymentTypes = configDM.getAllPaymentTypes();
        if(paymentTypes == null || paymentTypes.isEmpty()){
         return new ArrayList<>();
        }else{
         ListIterator<PaymentTypeRec> li = paymentTypes.listIterator();
         while(li.hasNext()){
          PaymentTypeRec pt = li.next();
          if(pt.getCompany() != null && Objects.equals(pt.getCompany().getId(), comp.getId())
            && pt.getLedger().getCode().equals(ledCode)){
           retList.add(pt);
          }
         }
         return retList;
        } 
      }catch (BacException ex){
        throw new BacException("DB error getting payment terms: "+ex.getErrorCode(),"SYSBUFF_PTY:01");

      }
    }
    LOGGER.log(INFO, "Loop over {0}", paymentTypes);
    for(PaymentTypeRec pt : paymentTypes){
     if(pt.getCompany() != null && Objects.equals(pt.getCompany().getId(), comp.getId())
       && pt.getLedger().getCode().equals(ledCode)){
      retList.add(pt);
     }
    }
    return retList;
   
  }
  
  public List<PaymentTypeRec> getPaymentTypes(CompanyBasicRec comp, String ledCode, boolean inbound){
   LOGGER.log(INFO, "getPaymentTypes called with comp id {0} leder {1} inbound {2}", 
     new Object[]{comp.getId(),ledCode, inbound});
   List<PaymentTypeRec> retList = new ArrayList<>();
   for(PaymentTypeRec pt: this.getPaymentTypes(comp)){
    if(pt.isInbound() == inbound && pt.getLedger().getCode().equals(ledCode)){
     retList.add(pt);
    }
   }
      
   return retList;
  }

  
  public List<BankValCredential> getBankValCredentials() {
    BankValCredential bvCred = new BankValCredential();
    bvCred.setType("bankVal");
    bvCred.setUserName("Hunt456");
    bvCred.setPassword("87165");
    bankValCredentials = new ArrayList<>();
    bankValCredentials.add(bvCred);
    return bankValCredentials;
  }




    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
  
public TransactionTypeRec getTransactionTypeRecByCode(String code){
 if(transactionTypes == null){
   transactionTypes = this.configDM.getTransactionTypes();
   if(transactionTypes == null){
    return null;
   }
 }
 for(TransactionTypeRec tranTy: transactionTypes){
  if(tranTy.getCode().equals(code)){
   return tranTy;
  }
 }
 
 return null;
}
public TransactionTypeRec getTransactionTypeRecById(Long tranId){
 if(transactionTypes == null){
   transactionTypes = this.configDM.getTransactionTypes();
   if(transactionTypes == null){
    return null;
   }
 }
 for(TransactionTypeRec tranTy: transactionTypes){
  if(tranTy.getId() == tranId){
   return tranTy;
  }
 }
 
 return null;
}
 public List<TransactionTypeRec> getTransactionTypes(UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "Sys buffer get getTransactionTypes() called");
  if(transactionTypes == null){
   transactionTypes = this.configDM.getTransactionTypes();
   // if still none then need to create default
   LOGGER.log(INFO, "After getTransactionTypes from DB called transactionTypes: {0}",transactionTypes);
 /*
   if(transactionTypes == null || transactionTypes.isEmpty() ){
    // need to build default 
    LOGGER.log(INFO,"Need to build default transaction types");
    TransactionTypeRec arInv = new TransactionTypeRec();
    arInv.setCreatedBy(usr);
    arInv.setChangedOn(new Date());
    arInv.setCode("arInv");
    arInv.setDescription("Invoice");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ListIterator<LedgerRec> ledgerLi = ledgers.listIterator();
    boolean ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AR")){
      arInv.setLedger(l);
      ledgerFound = true;
     }
    }
    arInv.setProcessCode("arInv");
    arInv.setShortDescription("AR Inv");
    arInv = configDM.updateTransactionType(arInv,pg);
    LOGGER.log(INFO, "AR Invoice transaction type id: {0}", arInv.getId());
    transactionTypes.add(arInv);
    TransactionTypeRec arCrn = new TransactionTypeRec();
    arCrn.setCreatedBy(usr);
    arCrn.setChangedOn(new Date());
    arCrn.setCode("arCrn");
    arCrn.setDescription("Credit Note");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AR")){
      arCrn.setLedger(l);
      ledgerFound = true;
     }
    }
    arCrn.setProcessCode("arCRN");
    arCrn.setShortDescription("AR Crn");
    arCrn = configDM.updateTransactionType(arCrn,pg );
    LOGGER.log(INFO, "AR Credit note transaction type id: {0}", arCrn.getId());
    transactionTypes.add(arCrn);
    TransactionTypeRec arPay = new TransactionTypeRec();
    arPay.setCreatedBy(usr);
    arPay.setChangedOn(new Date());
    arPay.setCode("arPay");
    arPay.setDescription("Payment");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AR")){
      arPay.setLedger(l);
      ledgerFound = true;
     }
    }
    arPay.setProcessCode("arPay");
    arPay.setShortDescription("AR pay");
    arPay = configDM.updateTransactionType(arPay,pg);
    LOGGER.log(INFO, "AR Payment transaction type id: {0}", arPay.getId());
    transactionTypes.add(arPay);
    TransactionTypeRec arRefund = new TransactionTypeRec();
    arRefund.setCreatedBy(usr);
    arRefund.setChangedOn(new Date());
    arRefund.setCode("arRfd");
    arRefund.setDescription("refund");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AR")){
      arRefund.setLedger(l);
      ledgerFound = true;
     }
    }
    arRefund.setProcessCode("arRfd");
    arRefund.setShortDescription("AR refund");
    arRefund = configDM.updateTransactionType(arRefund,pg);
    LOGGER.log(INFO, "AR Refund transaction type id: {0}", arRefund.getId());
    transactionTypes.add(arRefund);
    // Accounts payable
    TransactionTypeRec apInv = new TransactionTypeRec();
    apInv.setCreatedBy(usr);
    apInv.setChangedOn(new Date());
    apInv.setCode("apInv");
    apInv.setDescription("Invoice");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AP")){
      apInv.setLedger(l);
      ledgerFound = true;
     }
    }
    apInv.setProcessCode("apInv");
    apInv.setShortDescription("AP Inv");
    apInv = configDM.updateTransactionType(apInv,pg);
    LOGGER.log(INFO, "AP Invoice transaction type id: {0}", apInv.getId());
    transactionTypes.add(arInv);
    TransactionTypeRec apCrn = new TransactionTypeRec();
    apCrn.setCreatedBy(usr);
    apCrn.setChangedOn(new Date());
    apCrn.setCode("apCrn");
    apCrn.setDescription("Credit Note");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AP")){
      arCrn.setLedger(l);
      ledgerFound = true;
     }
    }
    apCrn.setProcessCode("apCRN");
    apCrn.setShortDescription("AP Crn");
    apCrn = configDM.updateTransactionType(apCrn,pg);
    LOGGER.log(INFO, "AP Credit note transaction type id: {0}", apCrn.getId());
    transactionTypes.add(arCrn);
    TransactionTypeRec apPay = new TransactionTypeRec();
    apPay.setCreatedBy(usr);
    apPay.setChangedOn(new Date());
    apPay.setCode("apPay");
    apPay.setDescription("Payment");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AP")){
      arPay.setLedger(l);
      ledgerFound = true;
     }
    }
    apPay.setProcessCode("apPay");
    apPay.setShortDescription("AP pay");
    apPay = configDM.updateTransactionType(apPay,pg);
    LOGGER.log(INFO, "AP Payment transaction type id: {0}", apPay.getId());
    transactionTypes.add(apPay);
    TransactionTypeRec apRefund = new TransactionTypeRec();
    apRefund.setCreatedBy(usr);
    apRefund.setChangedOn(new Date());
    apRefund.setCode("apRfd");
    apRefund.setDescription("Refund");
    if(ledgers == null){
     ledgers = this.getLedgers();
    }
    ledgerLi = ledgers.listIterator();
    ledgerFound = false;
    while(ledgerLi.hasNext() && !ledgerFound){
     LedgerRec l = ledgerLi.next();
     if(l.getName().equalsIgnoreCase("AP")){
      apRefund.setLedger(l);
      ledgerFound = true;
     }
    }
    apRefund.setProcessCode("apRfd");
    apRefund.setShortDescription("AP refund");
    apRefund = configDM.updateTransactionType(apRefund,pg);
    
    LOGGER.log(INFO, "AP Refund transaction type id: {0}", apRefund.getId());
    transactionTypes.add(apRefund);
   }
   */
  }
  LOGGER.log(INFO, "Sys Buffer returns transaction types {0}", transactionTypes);
  return transactionTypes;
 }

 public List<TransactionTypeRec> getGlTransactionTypes(){
  if(transactionTypes == null){
   transactionTypes = this.configDM.getTransactionTypes();
   
  
  if(transactionTypes == null){
   return null;
  }
  }
  List<TransactionTypeRec> retList = new ArrayList<>();
  for(TransactionTypeRec tranTy:transactionTypes ){
   if(tranTy.getLedger().getName().equals("GL")){
    retList.add(tranTy);
   }
  }
  return retList;
 }
 public List<TransactionTypeRec> getArTransactionTypes(UserRec usr,String pg) throws BacException {
  
  if(transactionTypes == null){
   transactionTypes = this.getTransactionTypes(usr,pg);
  }
  List<TransactionTypeRec> arTransTypeList = new ArrayList<>();
  ListIterator<TransactionTypeRec> li = transactionTypes.listIterator();
  while(li.hasNext()){
   TransactionTypeRec tranType = li.next();
   if(tranType.getLedger().getName().equalsIgnoreCase("AR")){
    arTransTypeList.add(tranType);
   }
  }
  return arTransTypeList;
 }

 public List<SalesCatRec> getSalesCategories() throws BacException {
  if(salesCategories == null){
   salesCategories = salesDM.allSalesCategories();
   
  }
  return salesCategories;
 }
 
/**
 * Return from buffer all cross company sales parts
 * @return
 * @throws BacException 
 */
 public List<SalesPartRec> getAllSalesParts() throws BacException {
  if(salesParts == null){
   
  }
  return salesParts;
 }
/**
 * Return from the buffer the sales parts that are used by a specific company.
 * Used extensively by sales 
 * @param company
 * @return
 * @throws BacException 
 */
 public List<SalesPartCompanyRec> getSalesPartsForCompany(CompanyBasicRec company) throws BacException {
  return null;
 }
/**
 * returns from buffer the Gl accounts for a company code
 * @param company
 * @return
 * @throws BacException 
 */
 public List<FiGlAccountCompRec> getGlAccountsByCompanyCode(CompanyBasicRec company) 
         throws BacException {
  LOGGER.log(INFO, "SysBuff called with {0}", company);
  if(company == null){
   throw new BacException("No company in call to get GL acs by company");
  }
  
  List<FiGlAccountCompRec> compGlAcs = null;
  List<CompanyBasicRec> comps = this.getCompanies();
  int index = comps.indexOf(company);
  CompanyBasicRec comp = comps.get(index);
  if(comp.getGlAccounts() == null || comp.getGlAccounts().isEmpty() ){
   LOGGER.log(INFO, "No GL accounts for company {0}", comp);
   compGlAcs = compDM.getGlAccountsForCompany(comp);
   comp.setGlAccounts(compGlAcs);
   comps.set(index, comp);
  }else{
   compGlAcs = comp.getGlAccounts();
  }
  return compGlAcs;
 }

 /**
  * Returns a list of VAT codes from system buffer. If this is 1st call then they are
  * loaded from DB layer
  * @return
  * @throws BacException 
  */
 public List<VatCodeRec> getAllVatCodes() throws BacException {
  LOGGER.log(INFO, "sys buffer getAllVatCodes {0} ", vatCodes);
  if(vatCodes != null){
   return vatCodes;
  }else{
   vatCodes = vatDM.getAllVatCodes();
   LOGGER.log(INFO,"VAT codes returned from DB layer {0}", vatCodes);
   return vatCodes;
  }
 }
 
 public List<VatCodeRec> getAllVatCodesByRef(String ref){
  LOGGER.log(INFO, "sys buffer getAllVatCodesByRef called with {0}",ref);
  if(StringUtils.isBlank(ref)){
   LOGGER.log(INFO, "Ref cannot be blank. Ref received {0}", ref);
   return null;
  }
  ref = StringUtils.remove(ref, '%');
  
  getAllVatCodes();
  if(this.vatCodes == null || getAllVatCodes().isEmpty()){
   LOGGER.log(INFO, "No VAT codes found");
   return null;
  }
  List<VatCodeRec> retList = new ArrayList<>();
  for(VatCodeRec curr:vatCodes){
   if(StringUtils.startsWith(curr.getCode(), ref)){
    retList.add(curr);
   }
  }
  return retList;
 }


 public List<VatCodeCompanyRec> getCompVatCodes(CompanyBasicRec company){
  LOGGER.log(INFO, "sysBuff.getCompVatCodes called with comp id {0} ", company.getId());
  List<VatCodeCompanyRec> retList = new ArrayList<>();
  if(vatCompanyCodes == null || vatCompanyCodes.isEmpty()){
   
   vatCompanyCodes = this.vatDM.getVatCompanyCodesAll();
   LOGGER.log(INFO, "vatDM returns comp vatCodes {0}", vatCompanyCodes);
  }
  if(vatCompanyCodes == null || vatCompanyCodes.isEmpty()){
   return null;
  }
  
  ListIterator<VatCodeCompanyRec> li = vatCompanyCodes.listIterator();
  while(li.hasNext()){
   VatCodeCompanyRec compVat = li.next();
   if(Objects.equals(compVat.getCompany().getId(), company.getId())  ){
    retList.add(compVat);
   }
  } 
  return retList;
 }
 
 public List<VatCodeCompanyRec> getCompVatCodesByCode(CompanyBasicRec company, String code){
  LOGGER.log(INFO, "getCompVatCodesByCode called with comp {0} and code {1}", 
    new Object[]{company.getReference(), code});
  if(company == null){
   LOGGER.log(INFO, "Company is required {0} ", company);
   return null;
  }
  List<VatCodeCompanyRec> list = this.getCompVatCodes(company);
  if(list == null || list.isEmpty()){
   return null;
  }
  if(StringUtils.isBlank(code)){
   return list;
  }else {
   List<VatCodeCompanyRec> ret = new ArrayList<>();
   for(VatCodeCompanyRec curr:list){
    if(StringUtils.startsWith(curr.getVatCode().getCode(), code)){
     ret.add(curr);
    }
   }
   return ret;
  }
 }
 
 public CompanyBasicRec getCompArAPSetting(CompanyBasicRec rec){
  
  rec = compDM.getCompArAP(rec);
  ListIterator<CompanyBasicRec> compLi = companies.listIterator();
  boolean foundComp = false;
  while(compLi.hasNext() && !foundComp){
   CompanyBasicRec curr = compLi.next();
   if(Objects.equals(rec.getId(), curr.getId())){
    compLi.set(rec);
    foundComp = true;
   }
  }
  return rec;
 }
 public CompanyBasicRec getCompAvailPostPeriod(CompanyBasicRec comp){
  LOGGER.log(INFO, "Sysbuff getCompAvailPostPeriod called");
  List<CompPostPerRec> periods = this.compDM.getCompPostPerByComp(comp);
  comp.setCompanyPostingPeriods(periods);
  if(companies == null || companies.isEmpty()){
   companies = new ArrayList<>();
   companies.add(comp);
  }else{
   ListIterator<CompanyBasicRec> compLi = companies.listIterator();
   boolean compFound = false;
   while(compLi.hasNext() && !compFound ){
    CompanyBasicRec c = compLi.next();
    if(c.getId() == comp.getId()){
     compLi.set(comp);
     compFound = true;
    }
   }
   if(!compFound){
   companies.add(comp);
   }
  }
  return comp;
 }
 public List<VatCodeCompanyRec> getCompVatCode(CompanyBasicRec company, VatCodeCompanyRec vatCode){
  LOGGER.log(INFO, "Sys Buffer getCompVatCode called vatCompanyCodes {0}",vatCompanyCodes);
  LOGGER.log(INFO, "company id {0} vat code {1}",new Object[]{company.getId(),vatCode.getVatCode().getCode()});
  List<VatCodeCompanyRec> retList = new ArrayList<>();
  if(vatCompanyCodes == null || vatCompanyCodes.isEmpty() ){
   LOGGER.log(INFO, "get vat comp codes from db");
   vatCompanyCodes = this.vatDM.getVatCompanyCodesAll();
  }
  LOGGER.log(INFO, "vatCompanyCodes now size {0}",vatCompanyCodes.size());
   ListIterator<VatCodeCompanyRec> li = vatCompanyCodes.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec compVat = li.next();
    LOGGER.log(INFO, "Loop vat list iterator compVat comp id {0} vat code id {1} ", new Object[]{
     compVat.getCompany().getId(),compVat.getVatCode().getCode()
    });
    if(compVat.getCompany().getId() == company.getId() && 
            compVat.getVatCode().getCode().equalsIgnoreCase(vatCode.getVatCode().getCode()) ){
     retList.add(compVat);
    }
   }
  LOGGER.log(INFO, "retList {0}", retList);
  return retList;
 }
 
 public VatCodeCompanyRec getVatCodeCompany(String  vatCd,CompanyBasicRec comp ){
  
  if(vatCompanyCodes == null){
   vatCompanyCodes = this.vatDM.getVatCompanyCodesAll();
  }
  if(vatCompanyCodes == null){
   return null;
  }
  for(VatCodeCompanyRec vc: this.vatCompanyCodes ){
   if(vc.getVatCode().getCode().equals(vatCd) && vc.getCompany().getId() == comp.getId()){
    return vc;
   }
   
  }
  return null;
 }
 public VatCodeCompanyRec getVatCodeCompany(VatCodeRec vatCd,CompanyBasicRec comp ){
  LOGGER.log(INFO, "Sysbuff getVatCodeCompany called");
  
  ListIterator<VatCodeRec> vatCodeLi = vatCodes.listIterator();
  boolean vatCodeFound = false;
  VatCodeRec vatCodeUpdate = null;
  while(vatCodeLi.hasNext() && !vatCodeFound){
   VatCodeRec vatCodeT = vatCodeLi.next();
   if(vatCodeT.getId() == vatCd.getId() ){
    vatCodeUpdate = vatCd;
    vatCodeFound = true;
   }
  }
  if(!vatCodeFound){
   vatCodes.add(vatCd);
   vatCodeUpdate = vatCd;
  }
  LOGGER.log(INFO, "vatCodeUpdate {0}",vatCodeUpdate.getCode());
  List<VatCodeCompanyRec> compVatCodes = vatCodeUpdate.getVatCodeCompanies();
  LOGGER.log(INFO, "compVatCodes {0}",compVatCodes);
  if(compVatCodes == null){
   // need to get company vat codes from DB
   compVatCodes = vatDM.getVatCompCodesForVatCode(vatCd);
   //add to buffer record
   vatCodeUpdate.setVatCodeCompanies(compVatCodes);
   vatCodeLi = vatCodes.listIterator();
   vatCodeFound = false;
   while(vatCodeLi.hasNext() && !vatCodeFound){
    VatCodeRec vatCodeT = vatCodeLi.next();
    if(vatCodeT.getId() == vatCodeUpdate.getId() ){
     vatCodeLi.set(vatCodeUpdate);
    }
   }
   
  }
  compVatCodes = vatCodeUpdate.getVatCodeCompanies();
  ListIterator<VatCodeCompanyRec> compVatCodesLi = compVatCodes.listIterator();
  while(compVatCodesLi.hasNext()){
   VatCodeCompanyRec compVatCode = compVatCodesLi.next();
   if(compVatCode.getCompany().getId() == compVatCode.getId()){
    return compVatCode;
   }
  }
  
  throw new BacException("Could not find company vat code for vat code "+vatCd.getCode());
 }
 
 public VatCodeCompanyRec getVatCodeCompany(Long vatCompId, CompanyBasicRec comp ){
  LOGGER.log(INFO, "Sysbuff getVatCodeCompany called");
  
  if(vatCompanyCodes == null || vatCompanyCodes.isEmpty()){
   vatCompanyCodes = this.getCompVatCodes(comp);
  }
  
  if(vatCompanyCodes == null || vatCompanyCodes.isEmpty()){
   return null;
  }
  
  LOGGER.log(INFO, "vatCompanyCodes {0}", vatCompanyCodes);
  for(VatCodeCompanyRec vatComp:vatCompanyCodes){
   if(Objects.equals(vatComp.getId(), vatCompId)){
    return vatComp;
   }
  }
  return null;
 }
 
 public VatCodeCompanyRec getVatCodeForCompVatCode(VatCodeCompanyRec vatCodeComp){
  LOGGER.log(INFO, "SYS buff getVatCodeForCompVatCode called with vat code {0}", vatCodeComp);
  VatCodeRec vatCode = vatDM.getVatCodeForCompVatCode(vatCodeComp);
  LOGGER.log(INFO, "Vat code  {0}", vatCode.getDescription());
  vatCodeComp.setVatCode(vatCode);
  ListIterator<VatCodeRec> li = getVatCodesForCompany(vatCodeComp.getCompany()).listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   VatCodeRec curr = li.next();
   if(Objects.equals(curr.getId(), vatCodeComp.getId())){
    li.set(curr);
    found = true;
   }
  }
  return vatCodeComp;
 }
 public List<VatCodeRec> getVatCodesForCompany(CompanyBasicRec company) throws BacException {
  LOGGER.log(INFO, "getVatCodesForCompany called with company id {0}", company.getId());
  List<VatCodeRec> retCompVatCodes = new ArrayList<>();
  if(vatCompanyCodes == null){
   // need to get records into buffer
   vatCompanyCodes = vatDM.getVatCompanyCodesAll();
   LOGGER.log(INFO,"getVatCodesForCompany Comp VAT DB layer returned {0}", vatCompanyCodes);
  }
  
   
   ListIterator<VatCodeCompanyRec> li = vatCompanyCodes.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec compVat = li.next();
    LOGGER.log(INFO, "Comp vat code found id {0}", compVat.getId());
    LOGGER.log(INFO,"Company {0} VAT code {1}", new Object[]{compVat.getCompany(),
    compVat.getVatCode()});
    if(compVat.getCompany().getId() == company.getId()){
     retCompVatCodes.add(compVat.getVatCode());
    }
   }
  
  
  return retCompVatCodes;
 }

 public List<CompVatRegistrationRec> getCompVatRegList() throws BacException {
  LOGGER.log(INFO, "getCompVatRegList called {0} ",compVatRegList);
  if(compVatRegList == null){
   LOGGER.log(INFO, "new compVatRegList required {0}",compVatRegList);
   List<CompanyBasicRec> comps = this.getCompanies();
   ListIterator<CompanyBasicRec> compsLi = comps.listIterator();
   while(compsLi.hasNext()){
    CompanyBasicRec comp = compsLi.next();
   
    compVatRegList = new ArrayList<>();
    List<VatRegistrationRec> vatRegCompList = vatDM.getVatRegistrationsForCompany(comp);
    if(vatRegCompList != null){
     ListIterator<VatRegistrationRec> vatRegCompListLi = vatRegCompList.listIterator();
     while(vatRegCompListLi.hasNext()){
      VatRegistrationRec vatReg = vatRegCompListLi.next();
      vatReg.setComp(comp);
      LOGGER.log(INFO, "vatReg comp {0}", vatReg.getComp());
      CompVatRegistrationRec compVatReg = new CompVatRegistrationRec();
      compVatReg.setCompanyId(comp.getId());
      compVatReg.setVatRegSumm(vatReg);
      compVatRegList.add(compVatReg);
     }
    }
   } 
   LOGGER.log(INFO, "compVatRegList  {0}", compVatRegList);
  }
  LOGGER.log(INFO,"end getCompVatRegList {0}",compVatRegList);
   
  return compVatRegList;
 }

 public void getCompVatRegList(List<CompVatRegistrationRec> vatRegs) {
  compVatRegList = vatRegs;
 }
 

public List<VatRegistrationRec> getVatRegistrationsForCompany(CompanyBasicRec comp){
 LOGGER.log(INFO, "getVatRegistrationsForCompany called with comp {0} id {1}", new Object[]{comp,comp.getId()});
 ArrayList<VatRegistrationRec> vatRegList = new ArrayList<>();
 if(compVatRegList == null){
  compVatRegList = new ArrayList<>();
  List<VatRegistrationRec> vatRegCompList = vatDM.getVatRegistrationsForCompany(comp);
  if(vatRegCompList != null){
   ListIterator<VatRegistrationRec> vatRegCompListLi = vatRegCompList.listIterator();
   while(vatRegCompListLi.hasNext()){
    VatRegistrationRec vatReg = vatRegCompListLi.next();
    vatReg.setComp(comp);
    CompVatRegistrationRec compVatReg = new CompVatRegistrationRec();
    compVatReg.setCompanyId(comp.getId());
    compVatReg.setVatRegSumm(vatReg);
    compVatRegList.add(compVatReg);
    vatRegList.add(vatReg);
   }
  }
 }else{
  // build list for company id
  
 }
 return vatRegList;
}

 public VatRegistrationRec getVatRegForCompany(CompanyBasicRec comp) {
  LOGGER.log(INFO, "Sys Buff getVatRegForCompany called with comp {0}", comp);
  LOGGER.log(INFO, "compVatRegList is {0}",compVatRegList);
  VatRegistrationRec vatReg = null;
  if(compVatRegList == null){
   compVatRegList = this.getCompVatRegList();
   /*LOGGER.log(INFO, "compVatRegList is {0}",compVatRegList);
   compVatRegList = new ArrayList<CompVatRegistrationRec>();
   VatRegistrationRec vatReg = vatDM.getVatRegistrationForCompany(comp);
   vatReg.setComp(comp);
   CompVatRegistrationRec compVatReg = new CompVatRegistrationRec();
   compVatReg.setCompanyId(comp.getId());
   compVatReg.setVatRegSumm(vatReg);
   compVatRegList.add(compVatReg);*/
   //return vatReg;
  }//else{
   ListIterator<CompVatRegistrationRec> li = compVatRegList.listIterator();
   while(li.hasNext()){
    CompVatRegistrationRec compVatReg = li.next();
    LOGGER.log(INFO, "compVatReg id {0} comp {1}", new Object[]{compVatReg.getCompanyId(),compVatReg.getVatRegSumm().getComp().getId()});
    if(compVatReg.getCompanyId() == comp.getId()){
     return compVatReg.getVatRegSumm();
    }
   }
   /*VatRegistrationRec vatReg = vatDM.getVatRegistrationForCompany(comp);
   vatReg.setComp(comp);
   CompVatRegistrationRec compVatReg = new CompVatRegistrationRec();
   compVatReg.setCompanyId(comp.getId());
   compVatReg.setVatRegSumm(vatReg);
   compVatRegList.add(compVatReg);*/
   return vatReg;
  
  
 }

 
 public VatRegSchemeRec getRegScheme(CompanyBasicRec comp, Date taxPoint) throws BacException {
  LOGGER.log(INFO,"getVatReturn called with comp {0} tax date {1}", new Object[]{comp,taxPoint});
  
  if(compVatRegList == null) {
   this.getVatRegistrationsForCompany(comp);
   // need to get the vat regs for company
   LOGGER.log(INFO, "compVatRegList is {0}",compVatRegList);
  }
  ListIterator<CompVatRegistrationRec> compVatRegLi = compVatRegList.listIterator();
  boolean found = false;
  VatRegistrationRec vatReg = null;
  boolean compFound = false;
  while(compVatRegLi.hasNext() && !compFound){
   CompVatRegistrationRec compVatReg = compVatRegLi.next();
   LOGGER.log(INFO, "compVatReg getCompanyId() {0} compVatReg.getVatRegSumm().getComp().getId() {1}", 
           new Object[]{compVatReg.getCompanyId(),compVatReg.getVatRegSumm().getComp().getId()});
   LOGGER.log(INFO, "Comp vat reg comp id {0} start {1} end {2}", new Object[]{compVatReg.getCompanyId(),
    compVatReg.getVatRegSumm().getRegistrationDate(),compVatReg.getVatRegSumm().getRegistrationEnd()
   });
   if(compVatReg.getCompanyId() == comp.getId() && compVatReg.getVatRegSumm().getRegistrationDate().before(taxPoint)
           &&compVatReg.getVatRegSumm().getRegistrationEnd().after(taxPoint)){
    LOGGER.log(INFO, "Reg found by comp and date");
    compFound = true;
    found = true;
    vatReg = compVatReg.getVatRegSumm();
   }else 
   if(compVatReg.getCompanyId() == comp.getId()){
    compFound = true;
   }
  }
  LOGGER.log(INFO, "compFound {0}", compFound);
  /*if(!compFound){
   compVatRegList = this.getCompVatRegList();
   List<VatRegistrationRec> vatRegList = vatDM.getVatRegistrationsForCompany(comp);
   if(vatRegList != null){
    ListIterator<VatRegistrationRec> vatRegListLi = vatRegList.listIterator();
    while(vatRegListLi.hasNext()){
     VatRegistrationRec vatRegRec = vatRegListLi.next();
     CompVatRegistrationRec compVatReg = new CompVatRegistrationRec();
     compVatReg.setCompanyId(comp.getId());
     compVatReg.setVatRegSumm(vatRegRec);
     compVatRegList.add(compVatReg);
    }
    // try to find again
    compVatRegLi = compVatRegList.listIterator();
    found = false;
    vatReg = null;
    compFound = false;
    while(compVatRegLi.hasNext() && !found){
     CompVatRegistrationRec compVatReg = compVatRegLi.next();
     LOGGER.log(INFO, "Comp vat reg comp id {0} start {1} end {2}", new Object[]{compVatReg.getCompanyId(),
      compVatReg.getVatRegSumm().getRegistrationDate(),compVatReg.getVatRegSumm().getRegistrationEnd()
      });
     if(compVatReg.getCompanyId() == comp.getId() && compVatReg.getVatRegSumm().getRegistrationDate().before(taxPoint)
           &&compVatReg.getVatRegSumm().getRegistrationEnd().after(taxPoint)){
      found = true;
      vatReg = compVatReg.getVatRegSumm();
     }
    }
   }
  }*/
  if(!found){
   LOGGER.log(INFO, "No VAT registration found for company id {0} taxpoint {1} ", 
           new Object[]{comp.getId(),
    taxPoint});
   return null;
  }
  LOGGER.log(INFO, "VAT registration id found {0}", vatReg.getId());
  found = false;
  List<VatRegSchemeRec> regSchemes = vatReg.getRegSchemes();
  ListIterator<VatRegSchemeRec> regSchemesLi = regSchemes.listIterator();
  while(regSchemesLi.hasNext() && !found){
   VatRegSchemeRec schemeRec = regSchemesLi.next();
   if(schemeRec.getValidFrom().before(taxPoint) && schemeRec.getValidTo().after(taxPoint)){
    return schemeRec;
   }
  }
  
  return null;
 }

 
 /*public boolean addVatReurnToRegScheme(VatRegSchemeRec regScheme, VatReturnRec vatReturn, UserRec usr, String page) 
         throws BacException {
  LOGGER.log(INFO,"addVatReurnToRegScheme called with regScheme {0} VAT return {1}",
          new Object[]{regScheme,vatReturn});
  vatReturn.setVatRegScheme(regScheme);
  this.vatDM.addVatReturn(vatReturn, usr, page);
  return true;
 }
 */
 
 public VatReturnRec getVatReturn(VatRegSchemeRec vatRegScheme, CompanyBasicRec comp, 
         Date taxPoint, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "SysBuff.getVatReturn called with scheme {0} taxPoint {1}", 
          new Object[]{vatRegScheme, taxPoint});
 
  VatReturnRec vatReturn = null;
  List<VatReturnRec> vatReturns = vatRegScheme.getVatReturns();
  
  if(vatReturns == null){
   
   LOGGER.log(INFO,"No VAT returns vatreturns is null");
   //vatMgr.getVatReturn(vatRegScheme, taxPoint, usr, page);
   LOGGER.log(INFO, "VatReturn {0}", vatRegScheme.getVatReturns());
   LOGGER.log(INFO, "vatRegScheme vatReg {0}", vatRegScheme.getVatReg());
   // update 
   // Get VAT reg scheme for comnpany
   
   ListIterator<CompVatRegistrationRec> compVatRegLi =  compVatRegList.listIterator();
   boolean found = false;
   while(compVatRegLi.hasNext() &&!found){
    CompVatRegistrationRec compVatRegRec = compVatRegLi.next();
    LOGGER.log(INFO, "vatReg {0}", vatRegScheme.getVatReg());
    LOGGER.log(INFO, "vatReg comp {0}", vatRegScheme.getVatReg().getComp());
    LOGGER.log(INFO, "vatReg compid {0}", compVatRegRec.getCompanyId());
    LOGGER.log(INFO, "comp.getId() {0}", comp.getId());
    if( comp.getId() == compVatRegRec.getCompanyId()){
     // we have the vat Registration
     found = true;
     VatRegistrationRec vatRegRec = compVatRegRec.getVatRegSumm();
     
     
     List<VatRegSchemeRec> vatRegSchemes = vatRegRec.getRegSchemes();
     LOGGER.log(INFO,"vatRegSchemes {0}",vatRegSchemes);
     boolean foundRegScheme = false;
     ListIterator<VatRegSchemeRec> vatRegSchemesLi = vatRegSchemes.listIterator();
     while(vatRegSchemesLi.hasNext() && !foundRegScheme){
      VatRegSchemeRec regScheme = vatRegSchemesLi.next();
      LOGGER.log(INFO, "regScheme valid from {0} valid to {1}", new Object[]{regScheme.getValidFrom(),
       regScheme.getValidTo()});
      if(regScheme == vatRegScheme){
       LOGGER.log(INFO, "regScheme found");
       vatRegSchemesLi.set(vatRegScheme);
       foundRegScheme = true;
      }
      if(regScheme.getValidTo().after(taxPoint) && regScheme.getValidFrom().before(taxPoint) ){
       vatReturns = vatMgr.getVatReturnsForRegScheme(regScheme,taxPoint, usr, page);
       LOGGER.log(INFO, "vatReturns returned by VAT mgr {0}", vatReturns);
       regScheme.setVatReturns(vatReturns);
       
      }
     }
     
     vatRegRec.setRegSchemes(vatRegSchemes);
     
    }
   }
  }
  LOGGER.log(INFO,"vatReturns is now {0}",vatReturns);
  ListIterator<VatReturnRec> vatReturnsLi = vatReturns.listIterator();
  
  while(vatReturnsLi.hasNext()){
   LOGGER.log(INFO,"looping over vat returns");
   VatReturnRec vRetRec = vatReturnsLi.next();
   LOGGER.log(INFO,"vRetRec return date {0} tax point {1}",new Object[]{vRetRec.getReturnDate(),taxPoint});
   if(vRetRec.getReturnDate().compareTo(taxPoint) >= 0 ){
    return vRetRec;
   }
  }
  LOGGER.log(INFO, "get vatreturn returns {0}",vatReturn); 
  return vatReturn;
 }
/**
 * Returns a list of VAT codes that may be used for a GL account 
 * @param glAccount
 * @return
 * @throws BacException 
 */
 public List<VatCodeRec> getVatCodesForGlAccount(FiGlAccountCompRec glAccount) throws BacException {
  return null;
 }
 
 
 public PostTypeRec getPostTypeForCode(String pCode) throws BacException {
  LOGGER.log(INFO, "Buffer getPostTypeForCode called with code {0}", pCode);
  ListIterator<PostTypeRec> pTypesLi = this.getPostTypes().listIterator();
  boolean pTypeFound = false;
  while(pTypesLi.hasNext() && !pTypeFound){
   PostTypeRec postingType = pTypesLi.next();
   if(postingType.getPostTypeCode().equalsIgnoreCase(pCode)){
    return postingType;
   }
  }
 // throw new BacException("Posting type code is invalid");
  return null;
  
 }

 public PostType getPostTypeForPCode(String postingCode) throws BacException {
  List<PostType> pTypeList = this.getPostTypeList();
  
  ListIterator<PostType> pTypeLi = pTypeList.listIterator();
  while(pTypeLi.hasNext()){
   PostType postType = pTypeLi.next();
   if(postType.getPostTypeCode().equalsIgnoreCase(postingCode)){
    return postType;
   }
  }
  return null;
 }

 public List<ProcessCodeRec> getProcessCodes() {
  //LOGGER.log(INFO, "SysBuff getProcessCodes processCodes at start {0}", processCodes);
  if(processCodes == null || processCodes.isEmpty() ){
   processCodes = configDM.getProcessCodesAll();
   
  }
  
  return processCodes;
 }

 public List<ProgrammeRec> getProgrammeForComp(Long compId){
  
  List<ProgrammeRec> proglist = new ArrayList<>();
  if(this.compProgs == null){
   List<ProgrammeRec> progs = this.progDm.getProgrammesForComp(compId);
   LOGGER.log(INFO, "progDM returns progs {0}", progs);
   if(progs == null || progs.isEmpty()){
    return null;
   }
   compProgs = new ArrayList<>();
   for(ProgrammeRec rec:progs){
    CompProgrammeRec compPr = new CompProgrammeRec();
    compPr.setCompId(compId);
    compPr.setProg(rec);
    compProgs.add(compPr);
   }
   return progs;
  }else{
   boolean foundComp = false;
   for(CompProgrammeRec compPr:compProgs){
    if(Objects.equals(compPr.getCompId(), compId)){
     proglist.add(compPr.getProg());
     foundComp = true;
    }
    if(foundComp){
     return proglist;
    }else{
     List<ProgrammeRec> progs = this.progDm.getProgrammesForComp(compId);
     if(progs == null || progs.isEmpty()){
      return null;
     }else{
      for(ProgrammeRec prRec:progs){
       compPr = new CompProgrammeRec();
       compPr.setCompId(compId);
       compPr.setProg(prRec);
       compProgs.add(compPr);
      }
      return progs;
     }
    }
   }
  }
  return null;
 }
 public void setProcessCodes(List<ProcessCodeRec> processCodes) {
  this.processCodes = processCodes;
 }

 public ProcessCodeRec getProcessCodeByName(String name){
  LOGGER.log(INFO, "SysBuff.getProcessCodeByName called with {0}",name);
  getProcessCodes();
  for(ProcessCodeRec pr: processCodes){
   LOGGER.log(INFO, "curr process code name {0} called with name {1}", new Object[]{
    pr.getName(), name });
   if(StringUtils.equalsIgnoreCase(pr.getName(), name)){
    return pr;
   }
  }
  ProcessCodeRec pr = this.configDM.getProcessCodesByName(name);
  LOGGER.log(INFO, "Process code from DB {0}", pr);
  if(pr != null){
   if(this.processCodes == null){
    this.processCodes = new ArrayList<>();
   }
   this.processCodes.add(pr);
  }
  return pr;
 }
 public ProcessCodeRec getProcessCodeById(Long id){
  ListIterator<ProcessCodeRec> li = getProcessCodes().listIterator();
  boolean found = false;
  while(li.hasNext() &&!found){
   ProcessCodeRec test = li.next();
   
   if(Objects.equals(test.getId(), id)){
    
    return test;
   }
   
  }
  return null;
 }
 
 public ProcessCodeRec processCodeUpdate(ProcessCodeRec rec, String pg){
  LOGGER.log(INFO, "SysBuff procesCodeUpdate called with process  {0}", rec.getName());
  rec = configDM.processCodeUpdate(rec, pg);
  if(getProcessCodes() != null){
   LOGGER.log(INFO, "current process code size {0}",getProcessCodes().size());
  }else{
   this.processCodes = new ArrayList<>();
  }
  
  LOGGER.log(INFO, "processCodeUpdate current processCodes {0}", processCodes.size());
  ListIterator<ProcessCodeRec> li = getProcessCodes().listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   ProcessCodeRec prCode = li.next();
   LOGGER.log(INFO, "rec id {0} curr List id {0}", new Object[]{rec.getId(),prCode.getId()});
   if(Objects.equals(prCode.getId(), rec.getId())){
    li.set(rec);
    found = true;
   }
  }
  LOGGER.log(INFO, "found {0}", found);
  if(!found){
   this.processCodes.add(rec);
  }
  LOGGER.log(INFO, "buffer size {0}", processCodes.size());
  return rec;
 }
 
 public SortOrderRec sortOrderUpdate(SortOrderRec sort, String pg){
  sort = configDM.addSortOrder(sort, pg);
  if(this.sortOrders == null){
   sortOrders = new ArrayList<>();
  }
  ListIterator<SortOrderRec> li = sortOrders.listIterator();
  boolean sortFound = false;
  while(li.hasNext() && !sortFound){
   SortOrderRec s = li.next();
   if(Objects.equals(s.getId(), sort.getId())){
    li.set(s);
    sortFound = true;
   }
  }
  if(!sortFound){
   sortOrders.add(sort);
  }
  return sort;
 }
 
 public CalendarRuleBaseRec updateCalendar(CalendarRuleBaseRec rec, String pg){
  rec = compDM.updateCalendarRule(rec, pg);
  return rec;
 }
 
 public CompanyBasicRec updateCompArAP(CompanyBasicRec comp, String pg){
  
  ListIterator<CompanyBasicRec> compLi = getCompanies().listIterator();
  while(compLi.hasNext()){
   CompanyBasicRec rec = compLi.next();
   if(Objects.equals(rec.getId(), comp.getId())){
    CompanyApArRec apAr = rec.getCompApAr();
    apAr = compDM.updateCompArApRec(apAr, pg);
    rec.setCompApAr(apAr);
    compLi.set(rec);
    return rec;
   }
  }
  return comp;
 }
 public DocTypeRec updateDocType(DocTypeRec doc, String pg){
  doc = this.configDM.updateDocType(doc, pg);
  ListIterator<DocTypeRec> li = this.getDocTypes().listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   DocTypeRec dt = li.next();
   if(dt.getId() == doc.getId()){
    li.set(doc);
    found = true;
   }
  }
  if(!found){
   if(docTypes == null){
    docTypes = new ArrayList<>();
   }
   docTypes.add(doc);
  }
  
  return doc;
 }
 
 public ModuleRec updateModule(ModuleRec mod, String pg){
  if(modules == null){
   modules = setup.getAllModules();
  }
  
  if(this.modules == null){
   
   modules = new ArrayList<>();
   mod = this.setup.createModule(mod, mod.getCreatedBy(), pg);
   modules.add(mod);
   return mod;
  }else{
   ListIterator<ModuleRec> li = modules.listIterator();
   boolean modFound = false;
   while(li.hasNext() && !modFound){
    ModuleRec m =li.next();
    if(m.getModuleCode().equals(mod.getModuleCode())){
      setup.updateModule(m, pg);
      li.set(mod);
      modFound = true;
      return mod;
    }
     
   }
   if(!modFound){
    mod = setup.createModule(mod, mod.getCreatedBy(), pg);
    modules.add(mod);
   }
  }
  
  return mod;
 }
 
 public PaymentTypeRec updatePayType(PaymentTypeRec payTy, String src){
  ListIterator<PaymentTypeRec> li = this.getPaymentTypes().listIterator();
  if(payTy.getId() == null){
   // new payment type
   payTy = this.configDM.updatePaymentType(payTy, src);
   li.add(payTy);
   return payTy;
  }
  while(li.hasNext()){
   PaymentTypeRec curr = li.next();
   if(curr.getId().equals(payTy.getId())){
    li.set(payTy);
    return payTy;
   }
  }
  return null;
 }
 
 public PaymentTermsRec updatePaymentTerms(PaymentTermsRec terms){
  if(paymentTermsList == null){
   paymentTermsList = new ArrayList<>();
  }
  boolean found = false;
  ListIterator<PaymentTermsRec> termsLi = paymentTermsList.listIterator();
  while(termsLi.hasNext() && !found){
   PaymentTermsRec currT  = termsLi.next();
   if(Objects.equals(currT.getId(), terms.getId())){
    termsLi.set(terms);
    found = true;
   }
  }
  
  if(!found){
   paymentTermsList.add(terms);
  }
  return terms;
 }
 
 public CompPostPerRec updatePostPerRec(CompPostPerRec postPer, CompanyBasicRec comp, String pg){
  
  postPer = this.compDM.updateCompPostPerRec(postPer, pg);
  
  ListIterator<CompPostPerRec> li = comp.getCompanyPostingPeriods().listIterator();
  
  while(li.hasNext()){
   CompPostPerRec curr = li.next();
   if(Objects.equals(curr.getId(), postPer.getId())){
    li.set(postPer);
    return postPer;
   }
  }
  return null;
 }
 
 public PostTypeRec updatePostType(PostTypeRec pt, String src){
  if( this.postTypes == null){
   postTypes = new ArrayList<>();
   
  }
  pt = this.configDM.updatePostType(pt, src);
  LOGGER.log(INFO, "SysBuff Update post type code  {0} rev {1} ", new Object[]{pt.getPostTypeCode(),pt.getRevPostType()});
  boolean foundPstTy = false;
  ListIterator<PostTypeRec> li = postTypes.listIterator();
  while(li.hasNext() && !foundPstTy){
   PostTypeRec currPostType = li.next();
   if(currPostType.getId() == pt.getId()){
    li.set(pt);
    foundPstTy = true;
   }
  }
  if(!foundPstTy){
   
   postTypes.add(pt);
  }
  LOGGER.log(INFO, "Sys buff after updatePostType postTypes {0}", postTypes);
  return pt;
 }
 
 /*public ProcessCodeRec updateProcessCode(ProcessCodeRec pr){
  LOGGER.log(INFO, "SysBuff.updateProcessCode called with proc code {0} num {1} ", 
    new Object[]{pr.getName(),getProcessCodes().size()});
  
  ListIterator<ProcessCodeRec> li = this.getProcessCodes().listIterator();
  boolean foundPrCd = false;
  while(li.hasNext() && !foundPrCd){
   ProcessCodeRec code = li.next();
   LOGGER.log(INFO, "pr id {0} curr from loop {1}", new Object[]{pr.getId(),code.getId()});
   if(Objects.equals(code.getId(), pr.getId())){
    foundPrCd = true;
    li.set(pr);
    
   }
  }
  if(!foundPrCd){
   processCodes.add(pr);
  }
  LOGGER.log(INFO, "updateProcessCodes with {0}",processCodes.size());
  return pr;
 }
 */
 public SortOrderRec updateSortOrder(SortOrderRec sort, String src){
  sort = this.configDM.updateSortOrder(sort, src);
  ListIterator<SortOrderRec> li = this.getSortOrders().listIterator();
  if(sortOrders.isEmpty()){
   sortOrders.add(sort);
   return sort;
  }
  boolean sortFound = false;
  while(li.hasNext() && !sortFound){
   SortOrderRec s = li.next();
   if(s.getId() == sort.getId()){
    li.set(sort);
    sortFound = true;
   }
  }
  return sort;
 }
 
 public LedgerRec updateLeder(LedgerRec led){
  boolean foundLed = false;
  List<LedgerRec> ledgers = this.getLedgers();
  ListIterator<LedgerRec> ledIt = ledgers.listIterator();
  while(ledIt.hasNext() && !foundLed){
   LedgerRec ledCurr = ledIt.next();
   if(ledCurr.getId() == led.getId()){
    ledIt.set(led);
    foundLed = true;
   }
   }
  
  ledgers.add(led);
  return led;
 }
 public List<FundRec> getRestrictedFunds() {
  return restrictedFunds;
 }

 public void setRestrictedFunds(List<FundRec> restrictedFunds) {
  this.restrictedFunds = restrictedFunds;
 }

 public VatCodeRec getVatCodeByCode(String cd){
  if(vatCodes == null){
   vatCodes = this.vatDM.getAllVatCodes();
  }
  if(vatCodes == null){
   LOGGER.log(INFO, "No VAT codes");
   return null;
  }
  for(VatCodeRec curr:vatCodes){
   if(StringUtils.equals(curr.getCode(), cd)){
    return curr;
   }
  }
  LOGGER.log(INFO, "Could not find VAT code {0}", cd);
  return null;
 }
 public List<VatCodeRec> getVatCodes() {
  if(vatCodes == null){
   vatCodes = this.vatDM.getAllVatCodes();
  }
  return vatCodes;
 }

 public void setVatCodes(List<VatCodeRec> vatCodes) {
  this.vatCodes = vatCodes;
 }

 public List<VatCodeCompanyRec> getVatCompanyCodes() {
  return vatCompanyCodes;
 }

 public void setVatCompanyCodes(List<VatCodeCompanyRec> vatCompanyCodes) {
  this.vatCompanyCodes = vatCompanyCodes;
 }

 public VatCodeCompanyRec vatCodeCompanyUpdate(VatCodeCompanyRec compVatCode, String pg){
  LOGGER.log(INFO, "vatCodeCompanyUpdate called with {0}", compVatCode);
  boolean newVatCd = compVatCode.getId() == null;
  LOGGER.log(INFO, "New vatcode {0}", newVatCd);
  compVatCode = vatDM.vatCodeCompanyUpdate(compVatCode, pg);
  if(vatCompanyCodes == null){
   vatCompanyCodes = new ArrayList<>();
  }
  if(newVatCd){
   vatCompanyCodes.add(compVatCode);
  }
  ListIterator<VatCodeCompanyRec> li = vatCompanyCodes.listIterator();
  boolean foundVatComp = false;
  while(li.hasNext() && !foundVatComp){
   VatCodeCompanyRec currVatComp = li.next();
   if(currVatComp.getId() == compVatCode.getId()){
    li.set(compVatCode);
    foundVatComp = true;
   }
  }
  return compVatCode;
 }
 public VatCodeRec vatCodeUpdate(VatCodeRec code, String page){
  LOGGER.log(INFO, "vatCodeUpdate called with VAT code {0}", code);
  code = this.vatDM.vatCodeUpdate(code, page);
  if(vatCodes == null){
   getVatCodes();
  }
  ListIterator<VatCodeRec> li = vatCodes.listIterator();
  boolean codeFound = false;
  while(li.hasNext() && !codeFound){
   VatCodeRec vatCode = li.next();
   if(vatCode.getId() == code.getId()){
    li.set(code);
    codeFound = true;
   }
  }
  if(!codeFound){
   vatCodes.add(code);
  }
  return code;
 }

 public TransactionTypeRec updateTransType(TransactionTypeRec rec, String pg){
  LOGGER.log(INFO, "sysBuff updateTransType called with {0}", rec);
  boolean foundTranTy = false;
  if(transactionTypes == null){
   transactionTypes = new ArrayList<>();
  }
  rec = configDM.updateTransactionType(rec, pg);
  ListIterator<TransactionTypeRec> li = transactionTypes.listIterator();
  while(li.hasNext() && !foundTranTy){
   TransactionTypeRec transTy = li.next();
   if(transTy.getId() == rec.getId()){
    li.set(rec);
    foundTranTy = true;
   }
  }
  if(!foundTranTy){
   transactionTypes.add(rec);
  }
   
  
  return rec;
 }
 
 public UomRec updateUom(UomRec uom, String pg){
  uom = this.configDM.updateUom(uom, pg);
  LOGGER.log(INFO, "uom id {0}", uom.getId());
  if(uoms == null || uoms.isEmpty()){
   uoms = new ArrayList<>();
   uoms.add(uom);
  }else{
   ListIterator<UomRec> uomLi = uoms.listIterator();
   boolean uomFound = false;
   while(uomLi.hasNext() && !uomFound){
    UomRec curr = uomLi.next();
    if(curr.getId() == uom.getId()){
     uomLi.set(uom);
     uomFound = true;
    }
   }
   if(!uomFound){
    uoms.add(uom);
   }
  }
  return uom;
 }
 
 public VatCodeRec updateVatCode(VatCodeRec vatcd, String pg){
  vatcd = this.vatDM.vatCodeUpdate(vatcd, pg);
  if(vatcd.getId() == null){
   LOGGER.log(INFO, "Failed to create new VAT code for code {0}", vatcd.getCode());
   return null;
  }
  if(vatCodes == null ){
   vatCodes = new ArrayList<>();
  }
  boolean found = false;
  ListIterator<VatCodeRec> li = vatCodes.listIterator();
  while(li.hasNext() && !found){
   VatCodeRec curr = li.next();
   if(Objects.equals(curr.getId(), vatcd.getId())){
    li.set(vatcd);
    found = true;
   }
  }
  if(!found){
   vatCodes.add(vatcd);
  }
  return vatcd;
 }
}
