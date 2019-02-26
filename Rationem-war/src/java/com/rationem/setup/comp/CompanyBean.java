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
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.VatRegByStartDateRev;
import com.rationem.util.MessageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
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
 private CompanyBasicRec companySel;
 private List<ChartOfAccountsRec> coaList;
 private List<SelectItem> coaSelList;
 private List<SelectItem> legalTypeSelList;
 private List<FisPeriodRuleRec> fiscalPeriodRules;
 private List<VatSchemeRec> vatSchemes;
 private VatRegistrationRec vatRegistration;
 private VatRegistrationRec vatRegSel;
 private VatRegistrationRec vatRegNew;
 private VatRegSchemeRec vatRegScheme;
 private Date vatRegMinDate;
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
  String currView = getViewSimple();
  LOGGER.log(INFO, "Processing view {0}", currView);
 
  switch(currView){
   case  "companyCrWithRef":
    companyNew = new CompanyBasicRec();
    break;
   case "vatRegistrationCr":
    company = this.getCompList().get(0);
    LOGGER.log(INFO, "Init default company {0}", company.getId());
    break;
  
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
   
   public void onCompanyVatSelect(SelectEvent evt){
    LOGGER.log(INFO, "onCompanyVatSelect called with {0}", evt.getObject());
    company = (CompanyBasicRec)evt.getObject();
    LOGGER.log(INFO, "company vat regList {0}", company.getVatRegistrations());
    if(company != null){
     if(company.getVatRegistrations() == null || company.getVatRegistrations().isEmpty()){
      company = this.compMgr.getVatRegsistrations(company);
      LOGGER.log(INFO, "after get vat registions from comp mgr {0}", company.getVatRegistrations());
      ListIterator<CompanyBasicRec> compLi = getCompList().listIterator();
      boolean foundComp = false;
      while(compLi.hasNext() && !foundComp){
       CompanyBasicRec curr = compLi.next();
       if(Objects.equals(curr.getId(), company.getId())){
        compLi.set(company);
        foundComp = true;
       }
       
      }
     }
     
     List<String> updates = new ArrayList<>();
     updates.add("vatRegistrationFrm");
     //updates.add("vatRegAddFrm");
     PrimeFaces.current().ajax().update(updates);
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
  return this.company;
 }

 public void setCompany(CompanyBasicRec company) {
        LOGGER.log(INFO, "setCompany called with {0}",company);
        this.company = company;
 }

 public CompanyBasicRec getCompanySel() {
  return companySel;
 }

 public void setCompanySel(CompanyBasicRec companySel) {
  this.companySel = companySel;
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
   List<AddressRec> addrListRet ;
   if(input == null || input.isEmpty()){
    // empty search - get all post codes
    addrListRet = addrMgr.getAllAddresses();
    
   }else{
    //get post code based on input
    addrListRet = addrMgr.getAddressesForPostCodePart(input);
    
   }
   LOGGER.log(INFO, "vatOfficeAddressComplete returns to web page {0}", addrListRet);
   return addrListRet;
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

 public VatRegistrationRec getVatRegNew() {
  return vatRegNew;
 }

 public void setVatRegNew(VatRegistrationRec vatRegNew) {
  this.vatRegNew = vatRegNew;
 }

 
 public VatRegistrationRec getVatRegSel() {
  return vatRegSel;
 }

 public void setVatRegSel(VatRegistrationRec vatRegSel) {
  this.vatRegSel = vatRegSel;
 }

 

 public VatRegSchemeRec getVatRegScheme() {
  if(vatRegScheme == null){
   vatRegScheme = new VatRegSchemeRec();
  }
  return vatRegScheme;
 }

 
 public Date getVatRegMinDate(){
  LOGGER.log(INFO, "getVatRegMinDate called");
  return this.vatRegMinDate;
  
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

public void onVatRegDelete(){
 LOGGER.log(INFO, "onVatRegDelete called selected {0}", this.vatRegSel.getVatNumber());
 boolean rc = this.compMgr.vatRegistrationDelete( vatRegSel,company, this.getLoggedInUser(), getView());
 List<String> updates = new ArrayList<>();
 
 if(rc){
  ListIterator<VatRegistrationRec> vatRegLi = this.company.getVatRegistrations().listIterator();
  boolean foundReg = false;
  while(vatRegLi.hasNext() && !foundReg){
   VatRegistrationRec curr = vatRegLi.next();
   if(Objects.equals(curr.getId(), vatRegSel.getId())){
    vatRegLi.remove();
    foundReg = true;
    
   }
  }
  MessageUtil.addClientInfoMessage("vatRegistrationFrm:msgs", "compVatDel", "blacResponse");
  updates.add("vatRegistrationFrm:msgs");
  updates.add("vatRegistrationFrm:vatRegs");
  PrimeFaces.current().ajax().update(updates);
 
 }else{
  MessageUtil.addClientErrorMessage("vatRegistrationFrm:msgs", "compVatRegDel", "errorText");
  PrimeFaces.current().ajax().update("vatRegistrationFrm:msgs");
 }
}
 
public void onVatRegNewBtn(){
 LOGGER.log(INFO, "onVatRegNewBtn called");
 this.vatRegNew = new VatRegistrationRec();
 Date minDate = new Date();
  LOGGER.log(INFO, "vatRegistrations {0} incorp date {1} company {2}", 
          new Object[]{company.getVatRegistrations(),company.getIncorporatedDate(),company.getId()});
  if(company.getId() == null){
   company = this.getCompList().get(0);
  }
  LOGGER.log(INFO, "Company is now {0}", company.getId());
  LOGGER.log(INFO, "VAT registrations {0}", company.getVatRegistrations());
  if(company.getVatRegistrations() == null || company.getVatRegistrations().isEmpty()){
   LOGGER.log(INFO, "No VAT registrations");
   if(company.getIncorporatedDate() == null){
    minDate = company.getCreatedDate();
     LOGGER.log(INFO, "No Company incorpoation date so created date {0}",minDate.toString());
   }else{
    minDate = company.getIncorporatedDate();
    LOGGER.log(INFO, "Use Company incorpoation date so created date {0}",minDate.toString());
   }
  }else{
   Collections.sort(company.getVatRegistrations(), new VatRegByStartDateRev());
   VatRegistrationRec lastReg = company.getVatRegistrations().get(0);
   minDate = lastReg.getRegistrationEnd();
   LOGGER.log(INFO, "VAT reg end date {0}", minDate.toString());
   Calendar c = Calendar.getInstance();
   c.setTime(minDate);
   c.add(Calendar.DAY_OF_MONTH, 1);
   minDate.setTime(c.getTimeInMillis());
   LOGGER.log(INFO, "Mindate set date after last period ended {0}", minDate.toString());
   
  }
  vatRegMinDate = minDate;
 
 List<String> updates = new ArrayList<>();
 updates.add("vatRegAddFrm:hdrText");
 updates.add("vatRegAddFrm:vatRegAddPGdBlk1");
 updates.add("vatRegAddFrm:vatRegAddPGdBlk2");
   
 PrimeFaces.current().ajax().update(updates);
 PrimeFaces.current().executeScript("PF('addRegDlgWv').show();");
}
public void onVatRegTrf(){
 LOGGER.log(INFO, "VAT reg transfer called with {0}", vatRegSel.getVatNumber());
 vatRegSel.setChangedBy(getLoggedInUser());
 vatRegSel.setChangedOn(new Date());
 LOGGER.log(INFO, "Vat reg inspector {0}", vatRegSel.getInspector());
 if(company.getVatRegistrations() != null && !company.getVatRegistrations().isEmpty()){
  boolean overLapp = false;
  ListIterator<VatRegistrationRec> vatRegLi = company.getVatRegistrations().listIterator();
  while(vatRegLi.hasNext() && !overLapp){
   VatRegistrationRec curr = vatRegLi.next();
   if(!Objects.equals(curr.getId(), vatRegSel.getId())){
    if(vatRegSel.getRegistrationDate().before(curr.getRegistrationDate()) && 
      vatRegSel.getRegistrationEnd().after(curr.getRegistrationEnd())){
      overLapp = true;
      LOGGER.log(INFO, "Edit Vat reg includes existing vat REg");
    }
   }
  }
  LOGGER.log(INFO, "overLapp {0}", overLapp);
  if(overLapp){
   MessageUtil.addClientWarnMessage("editRegDetailFrm:vatRegDetMsg", "vatRegOverLapp", "validationText");
   PrimeFaces.current().ajax().update("editRegDetailFrm:vatRegDetMsg");
   return;
  }
 }
 LOGGER.log(INFO, "Not Overlap");
 vatRegSel = compMgr.saveVatRegistration(company, vatRegSel, this.getLoggedInUser(), 
   this.getView());
 
 ListIterator<VatRegistrationRec> vatRegLi = company.getVatRegistrations().listIterator();
 LOGGER.log(INFO, "company.getVatRegistrations() {0} listit", plAct);
 boolean foundReg = false;
 while(vatRegLi.hasNext() && !foundReg){
  VatRegistrationRec curr = vatRegLi.next();
  LOGGER.log(INFO, "VAt reg in list {0} vat reg being edited {1}", new Object[]{curr.getId(),vatRegSel.getId()});
  if(Objects.equals(curr.getId(), vatRegSel.getId())){
   vatRegLi.set(vatRegSel);
   vatRegSel = null;
   PrimeFaces pf = PrimeFaces.current();
   List<String> updates = new ArrayList<>();
   updates.add("vatRegistrationFrm:vatRegs");
   updates.add("editRegDetailFrm:vatRegPgBlk1");
   updates.add("editRegDetailFrm:vatRegPgBlk2");
   pf.ajax().update(updates);
   pf.executeScript("PF('editRegDlgWVar').hide();");
   LOGGER.log(INFO, "Exit after update");
   return;
  }
 }
 
}

public void onVatRegCompDelete(){
 LOGGER.log(INFO, "onVatRegCompDelete called selected vat reg {0}", vatRegSel.getVatNumber());
}

public void onVatRegDateEndValidate(FacesContext context, UIComponent toValidate, 
  Object value){
 
 String naming = toValidate.getNamingContainer().getId();
 String toValidateId = toValidate.getId();
 String toValidComp = naming+":"+toValidateId;
 String msgRef = naming+":vatRegDetMsg";
 LOGGER.log(INFO, "naming {0} id {1}", new Object[]{toValidComp,msgRef});
 
 //boolean startDateInvalid = false;
 if(company.getVatRegistrations() == null || company.getVatRegistrations().isEmpty()){
  ((EditableValueHolder) toValidate).setValid(true);
  PrimeFaces.current().ajax().update(msgRef);
  return;
 }
 ListIterator<VatRegistrationRec> li = company.getVatRegistrations().listIterator();
 Date endDate = (Date)value;
 while (li.hasNext() ){
  VatRegistrationRec curr = li.next();
  if(endDate.after(curr.getRegistrationDate()) && 
    endDate.before(curr.getRegistrationEnd())){
    //startDateInvalid = true;
    LOGGER.log(INFO, "end date between another VAT period ");
    //MessageUtil.addClientWarnMessage("editRegDetailFrm:vatRegDetMsg", "vatRegOverLapp", "validationText");
    MessageUtil.addClientWarnMessage(msgRef, "vatRegOverLapp", "validationText");
    PrimeFaces.current().ajax().update(toValidComp);
    PrimeFaces.current().ajax().update(msgRef);
    ((EditableValueHolder) toValidate).setValid(false);
    
    return;
  }
  // check to see if totally within another  VAT period
  
 }
 LOGGER.log(INFO, "Start ");
 PrimeFaces.current().ajax().update(msgRef);
 ((EditableValueHolder) toValidate).setValid(true);
}

public void onVatRegDateStartValidate(FacesContext context, UIComponent toValidate, 
  Object value){
 
 LOGGER.log(INFO, "onVatRegDateStartValidate called with {0}", value);
 String naming = toValidate.getNamingContainer().getId();
 String toValidateId = toValidate.getId();
 String toValidComp = naming+":"+toValidateId;
 String msgRef = naming+":vatRegDetMsg";
 LOGGER.log(INFO, "naming {0} id {1}", new Object[]{toValidComp,msgRef});
 //boolean startDateInvalid = false;
 if(company.getVatRegistrations() == null || company.getVatRegistrations().isEmpty()){
  ((EditableValueHolder) toValidate).setValid(true);
  PrimeFaces.current().ajax().update(msgRef);
  return;
 }
 ListIterator<VatRegistrationRec> li = company.getVatRegistrations().listIterator();
 Date startDate = (Date)value;
 while (li.hasNext() ){
  VatRegistrationRec curr = li.next();
  if(startDate.after(curr.getRegistrationDate()) && 
    startDate.before(curr.getRegistrationEnd())){
    //startDateInvalid = true;
    LOGGER.log(INFO, "Start date between another VAT period ");
    MessageUtil.addClientWarnMessage(msgRef, "vatRegOverLapp", "validationText");
    PrimeFaces.current().ajax().update(toValidComp);
    PrimeFaces.current().ajax().update(msgRef);
    ((EditableValueHolder) toValidate).setValid(false);
    return;
  }
 }
 LOGGER.log(INFO, "Start date okay");
 PrimeFaces.current().ajax().update(toValidComp);
 PrimeFaces.current().ajax().update(msgRef);
 
 ((EditableValueHolder) toValidate).setValid(true);
}

public void onVatOffPostCodeSelect(SelectEvent evt){
 LOGGER.log(INFO, "onVatOffPostCodeSelect called with {0}", evt.getObject());
 
}

public void onVatRegistrationNewSave(){
 LOGGER.log(INFO,"onVatRegistrationNewSave called with vat Reg {0} ",vatRegNew);
 try{
  LOGGER.log(INFO,"try block started");
  VatRegistrationRec vatRegUpdt = vatRegNew;
  LOGGER.log(INFO,"vatRegUpdt {0}",vatRegUpdt);
  List<VatRegistrationRec> vatRegs = company.getVatRegistrations();
  LOGGER.log(INFO,"vatRegs {0}",vatRegs);
  if(vatRegs != null){
  ListIterator<VatRegistrationRec> li = vatRegs.listIterator();
  LOGGER.log(INFO,"li {0}",li);
  
  while(li.hasNext()  ){
   VatRegistrationRec curr = li.next();
   // if new reg has start between start and end date of existing
   
   
   if( vatRegNew.getRegistrationDate().after(curr.getRegistrationDate()) && 
     vatRegNew.getRegistrationDate().before(curr.getRegistrationEnd())){
    MessageUtil.addClientWarnMessage("vatRegAddFrm:vatRegDetMsg", "vatRegOverlap", "errorText");
    PrimeFaces.current().ajax().update("vatRegAddFrm:vatRegDetMsg");
    return;
   }
  }
  }
  LOGGER.log(INFO, "No overlap");
  LOGGER.log(INFO, "saveCompVatRegistration for vat reistrations {0}", vatRegs);
  vatRegUpdt.setCreatedBy(this.getLoggedInUser());
  vatRegUpdt.setCreatedOn(new Date());
  company.setVatRegDetails(compMgr.saveVatRegistration(company,vatRegUpdt, getLoggedInUser(), 
          getView()));
  List<VatRegistrationRec> vatList = company.getVatRegistrations();
  if(vatList == null){
   vatList = new ArrayList<>();
  }
  vatList.add(vatRegUpdt);
  company.setVatRegistrations(vatList);
  
  MessageUtil.addClientInfoMessage("vatRegistrationFrm:msgs", "vatRegCompAdd", "blacResponse", 
    company.getVatRegDetails().getVatNumber());
  PrimeFaces pf = PrimeFaces.current();
  List<String> updates = new ArrayList<>();
  updates.add("vatRegistrationFrm:msgs");
  updates.add("vatRegistrationFrm:vatRegs");
  pf.executeScript("PF('addRegDlgWv').hide()");
  pf.ajax().update(updates);
  
  //company = new CompanyBasicRec();
  
 }catch(BacException ex){
  String msg = " BAC "+ex.getLocalizedMessage();
  MessageUtil.addClientWarnMessage("vatRegAddFrm:vatRegDetMsg", "vatRegCompAdd", "errorText", msg);
  PrimeFaces.current().ajax().update("vatRegAddFrm:vatRegDetMsg");
 }catch(Exception ex){
  String msg = " OTHER "+ex.getLocalizedMessage();
  MessageUtil.addClientWarnMessage("vatRegAddFrm:vatRegDetMsg", "vatRegCompAdd", "errorText", msg);
  PrimeFaces.current().ajax().update("vatRegAddFrm:vatRegDetMsg");
 }
 LOGGER.log(INFO, "onSaveCompVatRegistration return comp with VAT reg list {0}", company.getVatRegistrations());
} 
public void onSaveVatInspector(){
 LOGGER.log(INFO, "saveVatInspector called with {0} ",company.getVatRegDetails().getInspector());
 
 if(company.getVatRegDetails().getInspector() == null || 
   StringUtils.isBlank(company.getVatRegDetails().getInspector().getFamilyName())){
  MessageUtil.addClientWarnMessage("inspCrFrm:vatInspMsgs", "vatRegInspReq", "errorText");
  
  PrimeFaces.current().ajax().update("inspCrFrm:vatInspMsgs");
  return;
 }
 try{
  PartnerPersonRec vatInspector = company.getVatRegDetails().getInspector();
  PartnerRoleRec  vatPtnrRole= this.sysBuffer.getPartnerRoleByCode("VAT_INSP");
  if(vatPtnrRole == null){
  MessageUtil.addClientWarnMessage("inspCrFrm:vatInspMsgs", "vatRegInspRoleNone", "errorText");
  PrimeFaces.current().ajax().update("inspCrFrm:vatInspMsgs");
  return;
 }
  List<PartnerRoleRec> roles = vatInspector.getPartnerRoles();
  if(roles == null ){
   roles = new ArrayList<>();
  }
  
  boolean foundRole = false;
  ListIterator<PartnerRoleRec> li = roles.listIterator();
  while(li.hasNext() && !foundRole){
   PartnerRoleRec curr = li.next();
   if(Objects.equals(curr.getId(), vatPtnrRole.getId())){
    li.set(vatPtnrRole);
    foundRole = true;
   }
  }
  if(!foundRole){
   roles.add(vatPtnrRole);
  }
  vatInspector.setPartnerRoles(roles);
  if(vatInspector.getId() == null ){
   vatInspector.setCreatedBy(this.getLoggedInUser());
   vatInspector.setCreatedDate(new Date());
  }else{
   vatInspector.setChangedBy(getLoggedInUser());
   vatInspector.setChangedOn(new Date());
  }
  //Long ptnrId = addrMgr.createIndivPartnerAR(vatInspector, 
   //       this.getLoggedInUser(),this.getView());
   vatInspector = (PartnerPersonRec)addrMgr.updatePartner(vatInspector, this.getView());
  
  
  company.getVatRegDetails().setInspector(vatInspector);
  
  MessageUtil.addClientInfoMessage("vatRegDetailsFrm:vatRegDetMsg", "vatRegInspC", "blacResponse");
  List<String> updates = new ArrayList<>();
  updates.add("vatRegAddFrm:vatRegDetMsg");
  updates.add("vatRegAddFrm:insp");
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update(updates);
  pf.executeScript("PF('inspCrWVar').hide()");
  GenUtil.addInfoMessage("Saved Inpector ");
 }catch(BacException ex){
  GenUtil.addErrorMessage("Could not save inspector: (BACS) "+ex.getLocalizedMessage());
 }catch(Exception ex){
  GenUtil.addErrorMessage("Could not save inspector: (OTHER) "+ex.getLocalizedMessage());
 }
 
}
public void onSaveVatAddr(){
 LOGGER.log(INFO, "saveVatAddr called with  {0}", this.company.getVatRegDetails().getVatOfficeAddress());
 try{
  AddressRec vatOffAddr = addrMgr.createAddress(company.getVatRegDetails().getVatOfficeAddress(), this.getLoggedInUser(), this.getView());
  company.getVatRegDetails().setVatOfficeAddress(vatOffAddr);
  MessageUtil.addClientInfoMessage("vatRegAddFrm:vatRegDetMsg", "addrCr", "blacResponse");
  PrimeFaces pf = PrimeFaces.current();
  pf.executeScript("PF('offCrWvar').hide();");
  pf.ajax().update("vatRegAddFrm:vatRegDetMsg");
  
 }catch(BacException ex){
  MessageUtil.addClientWarnMessage("vatAddrN:addrMsg", "addrCr", "errorText");
  PrimeFaces.current().ajax().update("vatAddrN:addrMsg");
  
 }
}
}
