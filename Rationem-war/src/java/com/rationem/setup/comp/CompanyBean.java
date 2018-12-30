/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;


import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Chris
 */


public class CompanyBean extends BaseBean implements Serializable  {
 private static final Logger LOGGER = Logger.getLogger(CompanyBean.class.getName());
    
    
 @EJB 
 CompanyManager compMgr;

 @EJB
 BasicSetup setup;

 @EJB
 SysBuffer sysBuffer;
    
 @EJB
 MasterDataManager addrMgr;
    
 private CompanyBasicRec company;
 private List<ChartOfAccountsRec> coaList;
 private List<SelectItem> coaSelList;
 private List<SelectItem> legalTypeSelList;
 private List<FisPeriodRuleRec> fiscalPeriodRules;
 private List<VatSchemeRec> vatSchemes;
 private VatRegistrationRec vatRegistration;
 private VatRegSchemeRec vatRegScheme;
 private List<AddressRec> addrList;
 private List<CountryRec> countries;
 private AddressRec addrSelected;
 private String postCodeSrch;
 private List<CompanyBasicRec> companies;
 private CompanyBasicRec companyNew;
 private CompanyBasicRec companySource;
 // TODO: Need to returm ArrayList of Subledgers
 private String subLedger;
    
 private String plAct;
 private boolean actTypeBs = false;
 private boolean showCompSearch = true;
 private boolean showCompDetail = false;
 private boolean showCoyDetails = false;
 private boolean compEditDisabled = false;
 private boolean showVatNum = false;
 private boolean compCreated = false;
 private boolean copyGlAcnts = false;
 private UploadedFile logoFile;
 private String compSelId;
 /**
     * Chart of accounts reference
     */
 private String coaRef;
 private int periodRuleId = 4;
 
 /** Creates a new instance of CompanyBean */
 public CompanyBean()  {
  LOGGER.log(INFO, "company setup called");
 }
 
 @PostConstruct
 private void init(){
  if(StringUtils.equals(getViewSimple(), "companyCrWithRef")){
   companyNew = new CompanyBasicRec();
   
  }
 }

 public boolean isCompEditDisabled() {
  return compEditDisabled;
 }

 public void setCompEditDisabled(boolean compEditDisabled) {
  this.compEditDisabled = compEditDisabled;
 }

    

    public CompanyManager getCompMgr() {
        return compMgr;
    }

    public void setCompMgr(CompanyManager compMgr) {
        this.compMgr = compMgr;
    }

    public String getSubLedger() {
        if(subLedger == null){
            subLedger = new String();
        }
        return subLedger;
    }

    public void setSubLedger(String subLedger) {
        this.subLedger = subLedger;
    }

    public String getPlAct() {
        return plAct;
    }

    public void setPlAct(String plAct) {
        this.plAct = plAct;
    }

 public String getPostCodeSrch() {
  return postCodeSrch;
 }

 public void setPostCodeSrch(String postCodeSrch) {
  this.postCodeSrch = postCodeSrch;
 }

    public boolean isActTypeBs() {
        return actTypeBs;
    }



    public void setActTypeBs(boolean actTypeBs) {
        this.actTypeBs = actTypeBs;
    }

 public List<AddressRec> getAddrList() {
  if(addrList == null){
   addrList = new ArrayList<>(); 
  }
  return addrList;
 }

 public void setAddrList(List<AddressRec> addrList) {
  this.addrList = addrList;
 }

 public AddressRec getAddrSelected() {
  return addrSelected;
 }

 public void setAddrSelected(AddressRec addrSelected) {
  LOGGER.log(INFO,"setAddrSelected called with {0}",addrSelected);
  this.addrSelected = addrSelected;
 }

 
  
  public boolean isShowCompDetail() {
    return showCompDetail;
  }

  public void setShowCompDetail(boolean showCompDetail) {
    this.showCompDetail = showCompDetail;
  }

  public boolean isShowCompSearch() {
    return showCompSearch;
  }

  public void setShowCompSearch(boolean showCompSearch) {
    this.showCompSearch = showCompSearch;
  }

  public String getCompSelId() {
    return compSelId;
  }

  public void setCompSelId(String compSelId) {
    this.compSelId = compSelId;
  }

  public int getPeriodRuleId() {
    LOGGER.log(INFO, "getPeriodRuleId {0}", periodRuleId);
    return periodRuleId;
  }

  public void setPeriodRuleId(int periodRuleId) {
    LOGGER.log(INFO, "setPeriodRuleId {0}", periodRuleId);
    this.periodRuleId = periodRuleId;
  }



  
    

  public List<FisPeriodRuleRec> getFiscalPeriodRules() {
    LOGGER.log(INFO, "CompanyBean getFiscalPeriodRules called {0}",fiscalPeriodRules);
    if(fiscalPeriodRules == null){
      fiscalPeriodRules = compMgr.getPeriodRules();
    }
    LOGGER.log(INFO, "CompanyBean getFiscalPeriodRules rules {0}",fiscalPeriodRules);
    return fiscalPeriodRules;
  }

  public void setFiscalPeriodRules(List<FisPeriodRuleRec> fiscalPeriodRules) {
    this.fiscalPeriodRules = fiscalPeriodRules;
  }

    public boolean isShowVatNum() {
        return showVatNum;
    }

    public void setShowVatNum(boolean showVatNum) {
        this.showVatNum = showVatNum;
    }

  public String getCoaRef() {
    return coaRef;
  }

  public void setCoaRef(String coaRef) {
    this.coaRef = coaRef;
  }

 
    
  public void onLogoUpload(FileUploadEvent evt){
   this.logoFile = evt.getFile();
   company.setLogoFname(logoFile.getFileName());
   company.setLogoImg(logoFile.getContents());
   company.setLogoFtype(logoFile.getContentType());
   LOGGER.log(INFO, "uploaded file name{0} type {1} ", 
           new Object[]{company.getLogoFname(), company.getLogoFtype()});
  } 
  
  public void onNewCompVatRegistration(){
   LOGGER.log(INFO, "onNewCompVatRegistration");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.execute("PF('addRegDlgWv').show()");
  }
  public void onPeriodRuleSel(ValueChangeEvent e) {
     LOGGER.log(INFO, "onPeriodRuleSel {0}", e.getNewValue());
     LOGGER.log(INFO, "Source {0}", e.getSource());
   }
   
  public void onPostCodeSearch(){
   LOGGER.log(INFO, "onPostCodeSearch called");
   addrList = this.addrMgr.getAddressesForPostCodePart(postCodeSrch);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("selectedAddrTbl");
  }
   public void onCompTySel(ValueChangeEvent e){
    LOGGER.log(INFO, "new comp type {0}", e.getNewValue());
    String compType = (String)e.getNewValue();
    if(compType.startsWith("Comp")){
     company.setCorp(true);
    }else{
     company.setCorp(false);
    }
    LOGGER.log(INFO, "company.isCorp {0}", company.isCorp());
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("compCrPg");
   }
   
   public void onAddrAdd(){
    LOGGER.log(INFO, "onAddrAdd called");
    addrSelected = new AddressRec();
    RequestContext rCtx = RequestContext.getCurrentInstance();
    
    rCtx.execute("PF('addrAddDlgWv').show()");
    rCtx.update("addrAddPnlId");
   }
   public void onAddrAddReset(){
    LOGGER.log(INFO, "onAddrAdd called");
    addrSelected = null;
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.execute("PF('addrAddDlgWv').hide()");
    rCtx.update("addrAddPnlId");
    rCtx.update("compCrPg");
    rCtx.reset("addrAddFrm:addrAddPnlId");
   }
   public void onAddrAddSave(){
    LOGGER.log(INFO, "onAddrAdd called");
    addrSelected = addrMgr.createAddress(addrSelected, getLoggedInUser(), getView());
    company.setAddress(addrSelected);
    addrSelected = null;
    RequestContext rCtx = RequestContext.getCurrentInstance();
    
    rCtx.execute("PF('addrAddDlgWv').hide()");
    rCtx.update("addrAddPnlId");
    rCtx.update("compCrPg");
   }
   public void onAddrChng(){
    LOGGER.log(INFO, "onAddrChng called");
    addrSelected = company.getAddress();
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("addrEditFrm");
    rCtx.execute("PF('addrEditDlgWv').show()");
    
   }
   public void onAddrChngReset(){
    addrSelected = null;
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.execute("PF('addrEditDlgWv').hide()");
    rCtx.update("addrEditFrm");
   }
   public void onAddrChngSave(){
    LOGGER.log(INFO, "onAddrChngSave called for addr {0}", addrSelected);
    
    
    addrSelected = this.addrMgr.addressUpdate(addrSelected, this.getLoggedInUser(), getView());
    company.setAddress(addrSelected);
    MessageUtil.addInfoMessage("addrUpdated", "blacResponse");
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("compCrPg");
    addrSelected = null;
    rCtx.execute("PF('addrEditDlgWv').hide()");
   }
   public void onAddrSelRowSel(SelectEvent evt){
    LOGGER.log(INFO, "onAddrSelRowSel called with {0}", evt.getObject());
    LOGGER.log(INFO, "addrSelected called with {0}", this.addrSelected);
    company.setAddress(addrSelected);
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("compCrPg");
    rCtx.execute("PF('selAddrDlgWv').hide()");
   }
   
   public void onAddrSelClick(){
    LOGGER.log(INFO, "onAddrSel");
    LOGGER.log(INFO, "Current post code {0}", company.getAddress().getPostCode());
    addrList = addrMgr.getAddressesForPostCodePart(company.getAddress().getPostCode());
    LOGGER.log(INFO, "Address found {0}", addrList);
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.execute("PF('selAddrDlgWv').show()");
   }
   public void onCharityBtn(ValueChangeEvent evt){
    company.setCharity(Boolean.parseBoolean(String.valueOf(evt.getNewValue())));
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("compDetPgId");
   }
   public void onCoASelect(ValueChangeEvent e){
     LOGGER.log(INFO, "onCoASelect called with new value", e.getNewValue());
     LOGGER.log(INFO, "Source", e.getSource());

   }

   public void onCompSourceSel(SelectEvent evt){
    LOGGER.log(INFO, "onCompSourceSel called with {0}", evt.getObject());
    companySource = (CompanyBasicRec)evt.getObject();
    
    if(companySource != null){
     PrimeFaces.current().ajax().update("compCrRef:compOldName");
    }
   }
   public void onCompRefChange(ValueChangeEvent evt){
    LOGGER.log(INFO, "onCompRefChange called with {0}", evt.getNewValue());
    boolean refValid = this.compMgr.companyRefValid(String.valueOf(evt.getNewValue()));
    LOGGER.log(INFO, "refValid {0}", refValid);
    if(!refValid){
     MessageUtil.addErrorMessage("compRefUnique", "validationText");
     company.setReference(null);
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("comCrFrm");
     rCtx.reset("comCrFrm");
    }
    
   }
   public void onCompSelect(ValueChangeEvent e){
     LOGGER.log(INFO, "Company change listener old {0} new {1}", 
             new Object[]{e.getOldValue(),e.getNewValue()});
     
   }
   
   
   
    
   public String onCompCrWorkflowStep(FlowEvent evt){
    String nextStep = evt.getNewStep();
    LOGGER.log(INFO, "wf from step {0} to step {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
    
    if(evt.getNewStep().equalsIgnoreCase("compDetId")&& evt.getOldStep().equalsIgnoreCase("basic")){
     LOGGER.log(INFO, "Move to detail from basic {0}", company.getChartOfAccounts());
     CountryRec cntry = company.getAddress().getCountry();
     
     if(cntry == null){
      if(countries == null){
       MessageUtil.addErrorMessage("countryNull", "errorText");
       return evt.getOldStep();
      }
      cntry = this.countries.get(0);
      company.getAddress().setCountry(cntry);
     }
     LOGGER.log(INFO, "Company country {0}", company.getAddress().getCountry());
     //this.company.setCurrency(cntry.getCurrCode());
     Locale loc = this.getLocale(cntry);
     company.setLocale(loc);
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("coa");
     
    }
    if(evt.getNewStep().equalsIgnoreCase("vatTabId") && evt.getOldStep().equalsIgnoreCase("compDetId")){
     if(!company.isVatReg()){
      nextStep = "summTabId";
     }
     else{
      if(company.getId() != null && company.getVatRegDetails().getId() == null){
       company = sysBuffer.getCompCurrentVatRec(company);
      }
     }
    }
    else if(evt.getNewStep().equalsIgnoreCase("summTabId") &&evt.getOldStep().equalsIgnoreCase("vatTabId")){
     boolean error = false;
     
     if(vatRegistration.getVatNumber() == null || vatRegistration.getVatNumber().isEmpty()){
      MessageUtil.addErrorMessage("compVatNumber", "validationText");
       error = true;
      }
      if(vatRegistration.getRegistrationDate() == null){
       MessageUtil.addErrorMessage("compVatRegDate", "validationText");
       
       error = true;
      }
      if(error){
       nextStep = evt.getOldStep();
      }
    }
    else if(evt.getNewStep().equalsIgnoreCase("vatTabId") && evt.getOldStep().equalsIgnoreCase("summTabId")){
     if(!company.isVatReg()){
      nextStep = "compDetId";
     }
    }
    
     
    return nextStep;
   }
  public List<ChartOfAccountsRec> getCoaList() {
    if(coaList == null){
      try{
        coaList = sysBuffer.getChartsOfAccounts();
      }catch(BacException e){
        addErrorMessage(this.getErrorMessage().getString("compNoCoa"));
      }
    }
    return coaList;
  }

  public List<SelectItem> getLegalTypeSelList() {
    LOGGER.log(INFO, "CompanyBean getCoaSelList {0}", coaSelList);

    if(legalTypeSelList == null){
      legalTypeSelList = new ArrayList<>();
      SelectItem item = new SelectItem();
      item.setLabel("Indvidual");
      item.setValue("IND");
      legalTypeSelList.add(item);
      item = new SelectItem();
      item.setLabel("Partnership");
      item.setValue("PTNR");
      legalTypeSelList.add(item);
      item = new SelectItem();
      item.setLabel("Company");
      item.setValue("COMP");
      legalTypeSelList.add(item);

    }

    return legalTypeSelList;
  }

  public void setLegalTypeSelList(ArrayList<SelectItem> list) {
    legalTypeSelList = list;
    
  }

  public Locale getLocale(CountryRec cntry){
   
   Locale[] locArray = Locale.getAvailableLocales();
   List<Locale> locList = Arrays.asList(locArray);
   ListIterator<Locale> locLi = locList.listIterator();
   boolean foundLocale = false;
   while(locLi.hasNext() && !foundLocale){
    Locale currLoc = locLi.next();
    if(currLoc.getCountry().equalsIgnoreCase(cntry.getCountryCode2())){
     //retString = currLoc.toLanguageTag();
     return currLoc;
    }
   }
   
   return null;
  }
 public UploadedFile getLogoFile() {
  return logoFile;
 }

 public void setLogoFile(UploadedFile logoFile) {
  this.logoFile = logoFile;
 }

  public void setCoaList(List<ChartOfAccountsRec> coaList) {
    this.coaList = coaList;
  }

  public List<SelectItem> getCoaSelList() {
    LOGGER.log(INFO, "CompanyBean getCoaSelList {0}", coaSelList);
    if(coaSelList == null){
      coaSelList = new ArrayList<>();
      getCoaList();
      LOGGER.log(INFO, "Number of charts is: {0}",coaList.size());
      ListIterator li = coaList.listIterator();
      while(li.hasNext()){
        ChartOfAccountsRec coa = (ChartOfAccountsRec)li.next();
        SelectItem sel = new SelectItem();
        sel.setValue(coa.getId());
        sel.setLabel(coa.getName());
        coaSelList.add(sel);
      }
    }
    return coaSelList;
  }

  public void setCoaSelList(ArrayList<SelectItem> coaSelList) {
    this.coaSelList = coaSelList;
  }
    
    
    
    
    public void addErrorMessage(String summary) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }
    
    public void addInfoMessage(String summary) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }
    
    public String createAcType(){
        LOGGER.log(INFO, "createAcType button clicked");
        return "not known";
    }
    
    
    
    public void deletAction(ActionEvent e){
        LOGGER.log(INFO, "deletAction");
        
    }
    
    public void createAcButton(ActionEvent e){
        LOGGER.log(INFO, "createAcButton1 with {0}", e);
    }

    public CompanyBasicRec getCompany() {
     if(company == null){
      company = new CompanyBasicRec();
     }
     /*   if(company == null){
         
          if(this.getCompList() != null && !this.getCompList().isEmpty()){
            LOGGER.log(INFO, "get company from company list");
            company = getCompList().get(0);
            LOGGER.log(INFO, "Comp type {0}", company.getCompanyType());
            
          } else{
            LOGGER.log(INFO, "Need to create a new company");
            company = new CompanyBasicRec();
            LOGGER.log(INFO, "New Company  ",company);
          }
        }
        */
        return this.company;
    }

    public void setCompany(CompanyBasicRec company) {
        LOGGER.log(INFO, "setCompany called with {0}",company);
        this.company = company;
    }

 public CompanyBasicRec getCompanyNew() {
  return companyNew;
 }

 public void setCompanyNew(CompanyBasicRec companyNew) {
  this.companyNew = companyNew;

 }

 public CompanyBasicRec getCompanySource() {
  return companySource;
 }

 public void setCompanySource(CompanyBasicRec companySource) {
  this.companySource = companySource;
 }

 
 public List<CompanyBasicRec> getCompanies() {
  if(companies == null){
   companies = this.getCompList();
   if(companies == null || companies.isEmpty()){
    this.compEditDisabled = true;
    MessageUtil.addErrorMessage("compsNone", "errorText");
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("compUpdtFrm");
    return null;
   }
   if(companies != null){
    company = companies.get(0);
   }
  
  }
  return companies;
 }

 public void setCompanies(List<CompanyBasicRec> companies) {
  this.companies = companies;
 }

 public boolean isCopyGlAcnts() {
  return copyGlAcnts;
 }

 public void setCopyGlAcnts(boolean copyGlAcnts) {
  this.copyGlAcnts = copyGlAcnts;
 }

    
 public List<CountryRec> getCountries() {
  if(countries == null){
   countries = addrMgr.getCountriesAll();
  }
  return countries;
 }

 public void setCountries(List<CountryRec> countries) {
  this.countries = countries;
 }

    public boolean getShowCoyDetails() {
        return showCoyDetails;
    }

    public void setShowCoyDetails(boolean showCoyDetails) {
        LOGGER.log(INFO,"setShowCoyDetails");
        this.showCoyDetails = showCoyDetails;
    }

    public void companyTypeAction(){
        LOGGER.log(INFO,"companyTypeAction");
        if(company.getCompanyType().startsWith("COMP")){
            this.showCoyDetails = true;
        }
    }

    public void VatRegAction(){
        LOGGER.log(INFO, "VatRegAction");
        if(company.isVatReg()){
            this.showVatNum = true;
        }else{
            this.showVatNum = false;
            this.company.setVatNumber(null);
        }

    }

    public void searchCompAction(){
      LOGGER.log(INFO, "searchCompAction() called with {0)",this.compSelId);
      this.getCompList();
      if(getCompList() == null || getCompList().isEmpty()){
        this.addErrorMessage(this.getErrorMessage().getString("compsNone"));

      }else if(getCompList().size() == 1){
        showCompDetail = true;
        showCompSearch = false;
      }
      else if (getCompList().size() >1){
        //more than one company need to choose the correct one
        ListIterator li = getCompList().listIterator();
        boolean found = false;
        while(li.hasNext() && !found){
          CompanyBasicRec c = (CompanyBasicRec)li.next();
          if(c.getReference().equalsIgnoreCase(this.compSelId)){
            found = true;
            company = c;
            showCompDetail = true;
            showCompSearch = false;
            LOGGER.log(INFO, "Set company to ref {0}", company.getReference());
            
          }
        }
      }
      LOGGER.log(INFO, "showCompDetail {0}", showCompDetail);
      LOGGER.log(INFO, "End ofsearchCompAction company is {0}", company.getReference());
    }

    public void onCompUpdateCountryChange(ValueChangeEvent evt){
     LOGGER.log(INFO, "onCompUpdateCountryChange called with {0}", evt.getNewValue());
     CountryRec cntry = (CountryRec)evt.getNewValue();
     Locale locale = this.getLocale((CountryRec)evt.getNewValue());
     LOGGER.log(INFO, "locale {0}", locale);
     company.setLocale(locale);
     //company.setCurrency(cntry.getCurrCode());
     //Locale.forLanguageTag(subLedger)
     
    }
    public void onCompUpdateSave(){
     LOGGER.log(INFO, "onCompUpdateSave called");
     company.setChangedBy(this.getLoggedInUser());
     company.setChangedDate(new Date());
     if(company.isVatReg()){
       // add vat reg to company
      vatRegistration = company.getVatRegDetails();
      vatRegistration.setCreatedBy(this.getLoggedInUser());
      vatRegistration.setCreatedOn(new Date());
      vatRegistration.setActiveReg(true);
      vatRegistration.setComp(company);
       vatRegScheme.setValidFrom(vatRegistration.getRegistrationDate());
       vatRegScheme.setValidTo(vatRegistration.getRegistrationEnd());
       vatRegScheme.setVatReg(vatRegistration);
       vatRegScheme.setCreatedBy(getLoggedInUser());
       vatRegScheme.setCreatedOn(new Date());
       if(vatRegScheme.getDescription() == null || vatRegScheme.getDescription().isEmpty()){
       vatRegScheme.setDescription("Default Registrations scheme");
       }
       if(vatRegScheme.getRef() == null | vatRegScheme.getRef().isEmpty()){
       vatRegScheme.setRef("Default");
       }
       LOGGER.log(INFO, "vatScheme id  {0}", vatRegScheme.getVatScheme().getId());
       
       List<VatRegSchemeRec> regSchemes = vatRegistration.getRegSchemes();
       if(regSchemes == null){
        regSchemes = new ArrayList<>();
       }
       LOGGER.log(INFO, "regSchemes {0}", regSchemes);
       ListIterator<VatRegSchemeRec> regSchemesLi = regSchemes.listIterator();
       while(regSchemesLi.hasNext()){
        VatRegSchemeRec sch = regSchemesLi.next();
        if(sch.getId() == null){
         sch.setCreatedBy(this.getLoggedInUser());
         sch.setCreatedOn(new Date());
        }else{
         sch.setChangedBy(getLoggedInUser());
         sch.setChangedOn(new Date());
        }
        regSchemesLi.set(sch);
       }
       regSchemes.add(vatRegScheme);
       for(VatRegSchemeRec sch:regSchemes){
        LOGGER.log(INFO, "valid from {0}", sch.getValidFrom());
        LOGGER.log(INFO, "created on {0}", sch.getCreatedOn());
       }
       vatRegistration.setRegSchemes(regSchemes);
       List<VatRegistrationRec> vatRegList = new ArrayList<>();
       vatRegList.add(vatRegistration);
       company.setVatRegDetails(vatRegistration);
       company.setVatRegistrations(vatRegList);
     }
     LOGGER.log(INFO, "Company VAT  reg{0} reg list {1}", 
             new Object[]{company.getVatRegDetails(),company.getVatRegistrations()});
     compMgr.updateCompany(company, getLoggedInUser(),getView());//createCompany(company,getLoggedInUser(),getView());
     compCreated = true;
     String hdrMsg = this.responseForKey("compUpdtHdr");
     String msg = this.responseForKey("compUpdt") +company.getReference();
     MessageUtil.addInfoMessageWithoutKey(hdrMsg, msg);
     compMgr = null;
     company = null;
     vatRegScheme = null;
     vatRegistration = null;
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("compUpdtFrm");
      
    }
    public String onCreatCompAction(){
     if(compCreated){
      return "home";
     }else{
      return null;
     }
    }
    
    public void onCreateCompButton(){
     LOGGER.log(INFO, "companyCrSubmit()");
     try{
      LOGGER.log(INFO, "getLoggedInUser() {0}", getLoggedInUser());
      LOGGER.log(INFO, "getLoggedInUser().id {0}", getLoggedInUser().getId());
      company.setCreatedBy(getLoggedInUser());
      company.setCreatedDate(new Date());
      if(company.isVatReg()){
       // add vat reg to company
       vatRegScheme.setValidFrom(vatRegistration.getRegistrationDate());
       vatRegScheme.setValidTo(vatRegistration.getRegistrationEnd());
       vatRegScheme.setCreatedBy(this.getLoggedInUser());
       vatRegScheme.setCreatedOn(new Date());
       vatRegScheme.setVatReg(vatRegistration);
       List<VatRegSchemeRec> regSchemes = vatRegistration.getRegSchemes();
       if(regSchemes == null){
        regSchemes = new ArrayList<>();
       }
       regSchemes.add(vatRegScheme);
       vatRegistration.setCreatedBy(this.getLoggedInUser());
       vatRegistration.setCreatedOn(new Date());
       vatRegistration.setRegSchemes(regSchemes);
       vatRegistration.setActiveReg(true);
       company.setVatRegDetails(vatRegistration);
       LOGGER.log(INFO, "company vatREg user {0}", company.getVatRegDetails().getCreatedBy().getId());
      }
      LOGGER.log(INFO, "Comp addr {0}", company.getAddress());
      compMgr.createCompany(company,getLoggedInUser(),getView());
      compCreated = true;
      String hdrMsg = this.responseForKey("compCrHdr");
      String msg = this.responseForKey("compCreated") +company.getReference();
      MessageUtil.addInfoMessageWithoutKey(hdrMsg, msg);
      compMgr = null;
      PrimeFaces.current().ajax().update("comCrFrm");
      company = null;
      vatRegScheme = null;
      vatRegistration = null;
     }catch(BacException e){
      
      MessageUtil.addErrorMessage("compCr", "errorText");
     }

    }
    
    public void onCreateCompWithRef(){
     LOGGER.log(INFO, "onCreateCompWithRef called");
     LOGGER.log(INFO, "copyGlAcnts {0}", copyGlAcnts);
     LOGGER.log(INFO, "companySource banks {0}", companySource.getBankAccounts());
     companyNew.setCreatedBy(this.getLoggedInUser());
     companyNew.setCreatedDate(new Date());
     companyNew = compMgr.createCompWithRef(companyNew, companySource, copyGlAcnts,getView());
     if(companyNew.getId() == null){
      MessageUtil.addClientErrorMessage("compCrRef:errMsg", "compCr", "errorText");
      PrimeFaces.current().ajax().update("compCrRef:errMsg");
     }else{
      
      MessageUtil.addClientInfoMessage("compCrRef:grl", "compCreated", "blacResponse");
      List<String> updateIds = new ArrayList<>();
      updateIds.add("compCrRef:grl");
      updateIds.add("compCrRef:compNewRef");
      updateIds.add("compCrRef:compNewName");
      PrimeFaces.current().ajax().update(updateIds);
      companyNew = null;
     }
    }

    public void updateCompButton(){
        LOGGER.log(INFO, "updateCompButton()");
        try{
          Date curr = new Date();
          company.setChangedBy(this.getLoggedInUser());
          company.setChangedDate(curr);
            LOGGER.log(INFO, "Company Chart is {0}", company.getChartOfAccounts().getName());
            LOGGER.log(INFO, "Company ref {0}", company.getReference());
            // find the period type rule
            ListIterator li = this.fiscalPeriodRules.listIterator();
            boolean found = false;
            while(li.hasNext() && !found){
              FisPeriodRuleRec item = (FisPeriodRuleRec)li.next();
              if(item.getId() == this.periodRuleId){
                company.setPeriodRule(item);
                found = true;
              }
            }
            LOGGER.log(INFO, "before call to comp mdr rul: {0} found {1}",
                    new Object[] {company.getPeriodRule(),found});
            compMgr.updateCompany(company,getLoggedInUser(),getView());
            GenUtil.addInfoMessage("Updated Company "+company.getReference());
        }catch(BacException e){
            GenUtil.addErrorMessage("Error updating company: "+e.getLocalizedMessage());
        }

    }
    
public List<AddressRec> vatOfficeAddressComplete(String input){
   List<AddressRec> addrListRet  = new ArrayList<>();
   if(input == null || input.isEmpty()){
    // empty search - get all post codes
    addrListRet = addrMgr.getAllAddresses();
    
   }else{
    //get post code based on input
    addrListRet = addrMgr.getAddressesForPostCodePart(input);
    
   }
   LOGGER.log(INFO, "vatOfficeAddressComplete returns to web page {0}", addrList);
   return addrList;
}

public List<PartnerPersonRec> vatInspectorComplete(String input){
 List<PartnerPersonRec> inspList;
 if(input == null || input.isEmpty()){
  inspList = addrMgr.getAllPartnerIndividual();
 }else{
  inspList = addrMgr.getIndivPtnrsBySurname(input);
 }
 return inspList;
}

 public VatRegistrationRec getVatRegistration() {
  if(vatRegistration == null){
   vatRegistration = new VatRegistrationRec();
  }
  return vatRegistration;
 }

 public void setVatRegistration(VatRegistrationRec vatRegistration) {
  this.vatRegistration = vatRegistration;
 }


 public VatRegSchemeRec getVatRegScheme() {
  if(vatRegScheme == null){
   vatRegScheme = new VatRegSchemeRec();
  }
  return vatRegScheme;
 }

 public Date getVegRegCompMinDate(){
  Date minDate = new Date();
  LOGGER.log(INFO, "vatRegistrations {0} incorp date {1}", 
          new Object[]{company.getVatRegistrations(),company.getIncorporatedDate()});
  if(company.getVatRegistrations() == null ||company.getVatRegistrations().isEmpty() ){
   LOGGER.log(INFO, "vat reg null");
   if(company.getIncorporatedDate() == null){
    minDate = company.getCreatedDate();
    LOGGER.log(INFO, "incorp null created date {0}",company.getCreatedDate());
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(company.getLocale());
   }else{
    minDate = company.getIncorporatedDate();
   }
  }
  return minDate;
 }
 public void setVatRegScheme(VatRegSchemeRec vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }


 public List<VatSchemeRec> getVatSchemes() {
  if(vatSchemes == null){
   vatSchemes = this.setup.getVatSchemesAll();
  }
  return vatSchemes;
 }

 public void setVatSchemes(List<VatSchemeRec> vatSchemes) {
  this.vatSchemes = vatSchemes;
 }


public void onVatReg(ValueChangeEvent evt){
 LOGGER.log(INFO, "onVatReg new value{0} comp vat {1}", 
         new Object[]{evt.getNewValue(),this.company.isVatReg()});
 company.setVatReg(Boolean.parseBoolean(String.valueOf(evt.getNewValue())));
 
}
public void onVatOffPostCodeSelect(SelectEvent evt){
 LOGGER.log(INFO, "onVatOffPostCodeSelect called with {0}", evt.getObject());
 
}

public void saveCompVatRegistration(){
 LOGGER.log(INFO,"saveCompVatRegistration called with vat Reg {0} ",company.getVatRegDetails());
 try{
  VatRegistrationRec vatRegUpdt = company.getVatRegDetails();
  List<VatRegistrationRec> vatRegs = company.getVatRegistrations();
  ListIterator<VatRegistrationRec> li = vatRegs.listIterator();
  while(li.hasNext()){
   VatRegistrationRec vatReg = li.next();
   // if new reg has start between start and end date of existing
   Date newStart = vatRegUpdt.getRegistrationDate();
   Date currStart = vatReg.getRegistrationDate();
   Date currEnd = vatReg.getRegistrationEnd();
   LOGGER.log(INFO, "New start date {0} db end date {1}", new Object[]{newStart,currEnd});
   if( newStart.compareTo(currEnd) <= 0 ){
    String msg = this.errorForKey("vatRefDupPeriod");
    GenUtil.addErrorMessage(msg);
    return;
   }
  }
  LOGGER.log(INFO, "saveCompVatRegistration for vat reistrations {0}", vatRegs);
  vatRegUpdt.setCreatedBy(this.getLoggedInUser());
  vatRegUpdt.setCreatedOn(new Date());
  company.setVatRegDetails(this.compMgr.saveVatRegistration(company,vatRegUpdt, getLoggedInUser(), 
          getView()));
  String msg = this.responseForKey("vatRegAdd")+company.getVatRegDetails().getVatNumber();
  GenUtil.addInfoMessage(msg);
  company = new CompanyBasicRec();
  
 }catch(BacException ex){
  String msg = this.errorForKey("vatRegAdd")+" BAC "+ex.getLocalizedMessage();
  GenUtil.addErrorMessage(msg);
 }catch(Exception ex){
  String msg = this.errorForKey("vatRegAdd")+" OTH "+ex.getLocalizedMessage();
  GenUtil.addErrorMessage(msg);
 }
} 
public void saveVatInspector(){
 LOGGER.log(INFO, "saveVatInspector called with {0} ",company.getVatRegDetails().getInspector());
 if(company.getVatRegDetails().getInspector() == null){
  GenUtil.addErrorMessage("You must enter inspectyor details");
 }
 try{
  Long ptnrId = addrMgr.createIndivPartnerAR(company.getVatRegDetails().getInspector(), 
          this.getLoggedInUser(),this.getView());
  company.getVatRegDetails().getInspector().setId(ptnrId);
  GenUtil.addInfoMessage("Saved Inpector ");
 }catch(BacException ex){
  GenUtil.addErrorMessage("Could not save inspector: (BACS) "+ex.getLocalizedMessage());
 }catch(Exception ex){
  GenUtil.addErrorMessage("Could not save inspector: (OTHER) "+ex.getLocalizedMessage());
 }
 
}
public void saveVatAddr(){
 LOGGER.log(INFO, "saveVatAddr called with ", this.company.getVatRegDetails().getVatOfficeAddress());
 try{
  AddressRec vatOffAddr = addrMgr.createAddress(company.getVatRegDetails().getVatOfficeAddress(), this.getLoggedInUser(), this.getView());
  company.getVatRegDetails().setVatOfficeAddress(vatOffAddr);
  GenUtil.addInfoMessage("Created VAT ofice address");
 }catch(BacException ex){
  GenUtil.addErrorMessage("Could not save VAT office address. Reason BAC: "+ex.getLocalizedMessage());
 }catch(Exception ex){
  GenUtil.addErrorMessage("Could not save VAT office address. Reason OTH: "+ex.getLocalizedMessage());
 }
}
}
