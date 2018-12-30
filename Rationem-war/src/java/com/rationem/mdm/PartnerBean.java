/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.helper.PartnerSelectOption;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
//import com.sun.org.apache.xerces.internal.utils.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.ejb.EJB;
import org.primefaces.event.FlowEvent;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.event.TabEvent;

/**
 *
 * @author Chris
 */
public class PartnerBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(PartnerBean.class.getName());
 private static final int CR_STEP_MAX = 4;
 private static final int CR_STEP_MIN = 0;
 @EJB
 private MasterDataManager ptnrMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private PartnerBaseRec ptnrBase; 
 private PartnerCorporateRec ptnrCorp;
 private PartnerPersonRec ptnrPerson;
 private List<PartnerBaseRec> ptnrList;
 private List<PartnerRoleRec> ptnrRoles;
 private boolean ptnrVatable = false;
 
 private String ptnrType;
 private String ptnrRolesOutput;
 private PartnerSelectOption ptnrSelOpt;
 private String addrTypeSrch;
 private String newAddDlgFor;
 private String addrSrchByPostCd;
 private AddressRec addrDefault;
 private boolean addrDefaultChanged;
 private AddressRec addrHeadOff;
 private AddressRec addrRegOff;
 private AddressRec addrNew;
 private List<CountryRec> countries;
 private List<AddressRec> addrList;
 private AddressRec addrSelected;
 private int addrDefEntries = 0;
 private int addrHeadOffEntries = 0;
 private int addrRegOffEntries = 0;
 private String addrTabCurr = "addDefTab";
 private int addrNewEntries = 0;
 private boolean createOk = false;
 private boolean updateOk = false;
 private final int ptnrCrStep = 0;
 private CountryRec defaultCountry;
 

 
 

 /**
  * Creates a new instance of PartnerBean
  */
 public PartnerBean() {
 }
 
 @PostConstruct
 private void init(){
  defaultCountry = this.getLoggedInUser().getCountry();
  LOGGER.log(INFO, "dafault country {0}", defaultCountry);
 }

 private String buildPtnrRolesOutput(PartnerBaseRec rec){
  LOGGER.log(INFO, "buildPtnrRolesOutput {0}", rec.getId());
  StringBuilder sb = null; 
  if(rec.getPartnerRoles() == null){
   return null;
  }
   
  for(PartnerRoleRec r:rec.getPartnerRoles()){
   if(sb == null){
     sb = new StringBuilder();
     sb.append(r.getRoleName());
   }else{
     sb.append(", ");
     sb.append(r.getRoleName());
   }
  }
  if(sb == null){
   return null;
  }else{
   return sb.toString();
  }
  
 }
 public AddressRec getAddrDefault() {
 
  return addrDefault;
 }

 public void setAddrDefault(AddressRec addrDefault) {
  this.addrDefault = addrDefault;
 }

 public boolean isAddrDefaultChanged() {
  return addrDefaultChanged;
 }

 public void setAddrDefaultChanged(boolean addrDefaultChanged) {
  this.addrDefaultChanged = addrDefaultChanged;
 }

 
 public AddressRec getAddrHeadOff() {
  if(addrHeadOff == null){
   addrHeadOff = new AddressRec();
   addrHeadOff.setCountry(defaultCountry);
  }
  return addrHeadOff;
 }

 public void setAddrHeadOff(AddressRec addrHeadOff) {
  this.addrHeadOff = addrHeadOff;
 }

 public List<AddressRec> getAddrList() {
  return addrList;
 }

 public void setAddrList(List<AddressRec> addrList) {
  this.addrList = addrList;
 }

 public AddressRec getAddrNew() {
  return addrNew;
 }

 public void setAddrNew(AddressRec addrNew) {
  this.addrNew = addrNew;
 }

 public AddressRec getAddrRegOff() {
  if(addrRegOff == null){
   addrRegOff = new AddressRec();
   addrRegOff.setCountry(defaultCountry);
   
  }
  return addrRegOff;
 }

 public void setAddrRegOff(AddressRec addrRegOff) {
  this.addrRegOff = addrRegOff;
 }

 public AddressRec getAddrSelected() {
  return addrSelected;
 }

 public void setAddrSelected(AddressRec addrSelected) {
  this.addrSelected = addrSelected;
 }

 public String getAddrSrchByPostCd() {
  return addrSrchByPostCd;
 }

 public void setAddrSrchByPostCd(String addrSrchByPostCd) {
  this.addrSrchByPostCd = addrSrchByPostCd;
 }

 
 public String getAddrTypeSrch() {
  return addrTypeSrch;
 }

 public void setAddrTypeSrch(String addrTypeSrch) {
  this.addrTypeSrch = addrTypeSrch;
 }

 
 public List<CountryRec> getCountries() {
  if(countries == null){
   countries = this.ptnrMgr.getCountriesAll();
  }
  return countries;
 }

 public void setCountries(List<CountryRec> countries) {
  this.countries = countries; 
 }

 public boolean isCreateOk() {
  return createOk;
 }

 public void setCreateOk(boolean createOk) {
  this.createOk = createOk;
 }

 

 public PartnerBaseRec getPtnrBase() {
  if(ptnrBase == null){
   ptnrBase = new PartnerBaseRec();
  }
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
 }

 public PartnerCorporateRec getPtnrCorp() {
  return ptnrCorp;
 }

 public void setPtnrCorp(PartnerCorporateRec ptnrCorp) {
  this.ptnrCorp = ptnrCorp;
 }

 public List<PartnerBaseRec> getPtnrList() {
  return ptnrList;
 }

 public void setPtnrList(List<PartnerBaseRec> ptnrList) {
  this.ptnrList = ptnrList;
 }
 

 

 public PartnerPersonRec getPtnrPerson() {
  return ptnrPerson;
 }

 public void setPtnrPerson(PartnerPersonRec ptnrPerson) {
  this.ptnrPerson = ptnrPerson;
 }

 public List<PartnerRoleRec> getPtnrRoles() {
  if(ptnrRoles == null || ptnrRoles.isEmpty()){
   // get partner roles
   ptnrRoles = sysBuff.getPartnerRoles();
  }
  return ptnrRoles;
 }

 public void setPtnrRoles(List<PartnerRoleRec> ptnrRoles) {
  this.ptnrRoles = ptnrRoles;
 }

 public String getPtnrRolesOutput() {
  return ptnrRolesOutput;
 }

 public void setPtnrRolesOutput(String ptnrRolesOutput) {
  this.ptnrRolesOutput = ptnrRolesOutput;
 }

  
 public PartnerSelectOption getPtnrSelOpt() {
  if(ptnrSelOpt == null){
   ptnrSelOpt = new PartnerSelectOption();
  }
  return ptnrSelOpt;
 }

 public void setPtnrSelOpt(PartnerSelectOption ptnrSelOpt) {
  this.ptnrSelOpt = ptnrSelOpt;
 }

 
 public String getPtnrType() {
  return ptnrType;
 }

 public void setPtnrType(String ptnrType) {
  this.ptnrType = ptnrType;
 }

 public boolean isPtnrVatable() {
  return ptnrVatable;
 }

 public void setPtnrVatable(boolean ptnrVatable) {
  this.ptnrVatable = ptnrVatable;
 }

 
 public boolean isUpdateOk() {
  return updateOk;
 }

 public void setUpdateOk(boolean updateOk) {
  this.updateOk = updateOk;
 }
 
 public List<CountryRec> onCountryComplete(String input){
  
  
  if(StringUtils.isBlank(input)){
   return getCountries();
  }else{
   List<CountryRec> ret = new ArrayList<>();
   for(CountryRec cntry:getCountries()){
    if(cntry.getCountryName().startsWith(input)){
     ret.add(cntry);
    }
   }
   return ret;
  }
  
  
 }
 public String onCreateWizStepChange(FlowEvent evt){
  LOGGER.log(INFO, "create step from {0} to {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
  String nextStep = evt.getNewStep();
  if(evt.getOldStep().equalsIgnoreCase("ptnrTypT") && evt.getNewStep().equalsIgnoreCase("ptnrDetTab")){
   LOGGER.log(INFO, "partner type to partner details");
   LOGGER.log(INFO, "partner type {0}", ptnrType);
   if(ptnrType.equalsIgnoreCase("corp") || ptnrType.equalsIgnoreCase("Company")){
    // corporate partner
    if(ptnrCorp == null){
    ptnrCorp = new PartnerCorporateRec();
    ptnrCorp.setRef(this.ptnrBase.getRef());
    }
    if(ptnrPerson != null){
     ptnrPerson = null;
    }
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("corpDetPg");
    LOGGER.log(INFO, "ptnrCorp {0}", ptnrCorp);
   }else{
    if(ptnrPerson == null){
    ptnrPerson = new PartnerPersonRec();
    ptnrPerson.setRef(ptnrBase.getRef());
    }
    if(ptnrCorp != null){
     ptnrCorp = null;
    }
   }
  }
  if(evt.getOldStep().equalsIgnoreCase("ptnrDetTab") && evt.getNewStep().equalsIgnoreCase("addrTabId")){
   // move forward to address tab
   if(ptnrType.equalsIgnoreCase("corp") || ptnrType.equalsIgnoreCase("Company")){
    
    if(ptnrCorp.getTradingName() == null || ptnrCorp.getTradingName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrTradingName", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrCorp.getLegalName() == null || ptnrCorp.getLegalName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrLegalname", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrCorp.getId() != null){
     LOGGER.log(INFO, "Update so check addresses");
     ptnrCorp = (PartnerCorporateRec)this.ptnrMgr.getPartnerAddress(ptnrCorp);
     this.addrDefault = ptnrCorp.getDefaultAddress();
     this.addrHeadOff = ptnrCorp.getHeadOfficeAddress();
     this.addrRegOff = ptnrCorp.getRegisteredOfficeAddress();
    }
   }else{
    if(ptnrPerson.getFamilyName() == null || ptnrPerson.getFamilyName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrFamName", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrPerson.getId() != null){
     ptnrPerson = (PartnerPersonRec)this.ptnrMgr.getPartnerAddress(ptnrPerson);
     LOGGER.log(INFO, "Pers def addr {0}", ptnrPerson.getDefaultAddress());
     this.addrDefault = ptnrPerson.getDefaultAddress();
    }
   }
  }
  if(evt.getOldStep().equalsIgnoreCase("addrTabId") && evt.getNewStep().equalsIgnoreCase("summTabId")){
   LOGGER.log(INFO, "Default addr {0} ho addr {1} reg addr {2}", new Object[]{addrDefault,addrHeadOff,addrRegOff});
   boolean error = false;
   if(addrDefEntries > 0){
    LOGGER.log(INFO, "default address {0}", addrDefault);
    if(addrDefault.getAddrRef() == null || addrDefault.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrDefAddrRef", "validationText");
     error = true;
    }
    if(addrDefault.getStreet() == null || addrDefault.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrDefStreet", "validationText");
     error = true;
    }
    if(addrDefault.getPostCode() == null || addrDefault.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrDefPostCode", "validationText");
     error = true;
    }
    
   }
   
   if(this.addrHeadOffEntries > 0){
    //Head office entry made
    
    if(this.addrHeadOff.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffAddrRef", "validationText");
     error = true;
    }
    if(addrHeadOff.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffStreet", "validationText");
     error = true;
    }
    if(addrHeadOff.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffPostCode", "validationText");
     error = true;
    }
   }
   
   if(this.addrRegOffEntries > 0){
    //Head office entry made
    
    if(this.addrRegOff.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffAddrRef", "validationText");
     error = true;
    }
    if(addrRegOff.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffStreet", "validationText");
     error = true;
    }
    if(addrRegOff.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffPostCode", "validationText");
     error = true;
    }
   }
   
   if(error){
     nextStep = evt.getOldStep();
    }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addrPnl");
  return nextStep;
 }
 
 public String onUpdateWizStepChange(FlowEvent evt){
  LOGGER.log(INFO, "create step from {0} to {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
  String nextStep = evt.getNewStep();
  if(evt.getOldStep().equalsIgnoreCase("ptnrTypT") && evt.getNewStep().equalsIgnoreCase("ptnrDetTab")){
   LOGGER.log(INFO, "partner type to partner details");
   LOGGER.log(INFO, "partner type {0}", ptnrType);
   if(ptnrType.equalsIgnoreCase("corp") || ptnrType.equalsIgnoreCase("Company")){
    // corporate partner
    if(ptnrCorp == null){
    ptnrCorp = new PartnerCorporateRec();
    ptnrCorp.setRef(this.ptnrBase.getRef());
    }
    if(ptnrPerson != null){
     ptnrPerson = null;
    }
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("corpDetPg");
    LOGGER.log(INFO, "ptnrCorp {0}", ptnrCorp);
   }else{
    if(ptnrPerson == null){
    ptnrPerson = new PartnerPersonRec();
    ptnrPerson.setRef(ptnrBase.getRef());
    }
    if(ptnrCorp != null){
     ptnrCorp = null;
    }
   }
  }
  if(evt.getOldStep().equalsIgnoreCase("ptnrDetTab") && evt.getNewStep().equalsIgnoreCase("addrTabId")){
   // move forward to address tab
   if(ptnrType.equalsIgnoreCase("corp") || ptnrType.equalsIgnoreCase("Company")){
    
    if(ptnrCorp.getTradingName() == null || ptnrCorp.getTradingName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrTradingName", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrCorp.getLegalName() == null || ptnrCorp.getLegalName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrLegalname", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrCorp.getId() != null){
     LOGGER.log(INFO, "Update so check addresses");
     ptnrCorp = (PartnerCorporateRec)this.ptnrMgr.getPartnerAddress(ptnrCorp);
     this.addrDefault = ptnrCorp.getDefaultAddress();
     this.addrHeadOff = ptnrCorp.getHeadOfficeAddress();
     this.addrRegOff = ptnrCorp.getRegisteredOfficeAddress();
    }
   }else{
    if(ptnrPerson.getFamilyName() == null || ptnrPerson.getFamilyName().isEmpty()){
     MessageUtil.addErrorMessage("ptnrFamName", "validationText");
     nextStep = evt.getOldStep();
    }
    if(ptnrPerson.getId() != null){
     ptnrPerson = (PartnerPersonRec)this.ptnrMgr.getPartnerAddress(ptnrPerson);
     LOGGER.log(INFO, "Pers def addr {0}", ptnrPerson.getDefaultAddress());
     this.addrDefault = ptnrPerson.getDefaultAddress();
    }
   }
  }
  if(evt.getOldStep().equalsIgnoreCase("addrTabId") && evt.getNewStep().equalsIgnoreCase("summTabId")){
   LOGGER.log(INFO, "Default addr {0} ho addr {1} reg addr {2}", new Object[]{addrDefault,addrHeadOff,addrRegOff});
   boolean error = false;
   if(addrDefEntries > 0){
    LOGGER.log(INFO, "default address {0}", addrDefault);
    if(addrDefault.getAddrRef() == null || addrDefault.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrDefAddrRef", "validationText");
     error = true;
    }
    if(addrDefault.getStreet() == null || addrDefault.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrDefStreet", "validationText");
     error = true;
    }
    if(addrDefault.getPostCode() == null || addrDefault.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrDefPostCode", "validationText");
     error = true;
    }
    
   }
   
   if(this.addrHeadOffEntries > 0){
    //Head office entry made
    
    if(this.addrHeadOff.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffAddrRef", "validationText");
     error = true;
    }
    if(addrHeadOff.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffStreet", "validationText");
     error = true;
    }
    if(addrHeadOff.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrHeadOffPostCode", "validationText");
     error = true;
    }
   }
   
   if(this.addrRegOffEntries > 0){
    //Head office entry made
    
    if(this.addrRegOff.getAddrRef().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffAddrRef", "validationText");
     error = true;
    }
    if(addrRegOff.getStreet().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffStreet", "validationText");
     error = true;
    }
    if(addrRegOff.getPostCode().isEmpty()){
     MessageUtil.addErrorMessage("addrRegOffPostCode", "validationText");
     error = true;
    }
   }
   
   if(error){
     nextStep = evt.getOldStep();
    }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addrPnl");
  return nextStep;
 }
 
 public void onFindPartners(){
  LOGGER.log(INFO, "onFindPartners called");
  this.ptnrList = ptnrMgr.getPartnersBySelOpt(ptnrSelOpt);
  LOGGER.log(INFO, "ptnrList {0}", ptnrList);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("partnerListId");
 }
 public void onNewAddrDefDlg(){
  LOGGER.log(INFO, "onNewAddrDefDlg called");
  newAddDlgFor = "default";
  this.addrNew = new AddressRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("newAddrFrm");
  rCtx.execute("PF('addNewAddrDlgWv').show()");
  
 }
 
 public void onNewAddrHoDlg(){
  LOGGER.log(INFO, "onNewAddrHoDlg called");
  newAddDlgFor = "headOff";
  this.addrNew = new AddressRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("newAddrFrm");
  rCtx.execute("PF('addNewAddrDlgWv').show()");
 }
 
 public void onNewAddrRegDlg(){
  LOGGER.log(INFO, "onNewAddrRegDlg called");
  newAddDlgFor = "regOff";
  this.addrNew = new AddressRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("newAddrFrm");
  pf.executeScript("PF('addNewAddrDlgWv').show()");
 }
 
 public List<PartnerRoleRec> onPtnrRoleComplete(String input){
  LOGGER.log(INFO, "onPtnrRoleComplete called with {0}", input);
  LOGGER.log(INFO, "ptnrRoles {0}",ptnrRoles);
  List<PartnerRoleRec> roles = new ArrayList<>();
  if(ptnrRoles == null || ptnrRoles.isEmpty()){
   ptnrRoles = sysBuff.getPartnerRoles();
  }
  LOGGER.log(INFO, "Roles after call to sys buff  {0}", ptnrRoles);
  if(ptnrRoles == null){
   return null;
  }
  if(StringUtils.isBlank(input)){
   return ptnrRoles;
  }
  for(PartnerRoleRec r:ptnrRoles){
   if(StringUtils.startsWith(r.getRoleName(), input)){
    roles.add(r);
   }
  }
  return roles;
 }
 public List<PartnerBaseRec> onPtnrRefComplete(String input){
  List<PartnerBaseRec> retList;
  LOGGER.log(INFO, "onPtnrRefComplete called with {0}", input);
  if(input == null || input.isEmpty()){
  retList = this.ptnrMgr.getPartnersAll();
  }else{
   retList = this.ptnrMgr.getPartnersByRef(input);
  }
  return retList;
 }
 
 public  void onPtnrRefSelect(SelectEvent evt){
  this.ptnrBase = (PartnerBaseRec)evt.getObject();
  String pClass = ptnrBase.getClass().getSimpleName();
  if(pClass.equalsIgnoreCase("PartnerCorporateRec")){
   this.ptnrType = this.formTextForKey("ptnrCorp");
   this.ptnrCorp = (PartnerCorporateRec)ptnrBase;
  }else{
   this.ptnrType = this.formTextForKey("ptnrIndiv");
   this.ptnrPerson = (PartnerPersonRec)ptnrBase;
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("ptnrType");
 }
 
 public void onPtnrRefValidate(FacesContext c, UIComponent comp, Object val){
  LOGGER.log(INFO, "onPtnrRefValidate called with val {0}", val);
  
  boolean ptnrUnique = this.ptnrMgr.isPartnerRefUnique((String)val);
  if(ptnrUnique){
   ((UIInput)comp).setValid(true);
  }else{
   ((UIInput)comp).setValid(false);
   MessageUtil.addWarnMessage("ptnrRefUniq", "validationText");
  }
  LOGGER.log(INFO, "ptnrUnique {0}", ptnrUnique);
  PrimeFaces.current().ajax().update("ptnrCrFrm:ptnrRef");
  
 }
 
 public  void onPtnrCrStepBack(){
  LOGGER.log(INFO, "onPtnrCrStepBack called from step {0}", getStep());
  int currStep = getStep();
  currStep--;
  if(currStep < CR_STEP_MIN){
   currStep = CR_STEP_MIN;
  }
  setStep(currStep);
  PrimeFaces.current().ajax().update("ptnrCrFrm");
  LOGGER.log(INFO, "step returned {0}", getStep());
 } 
       
 public void onPtnrCrStepNext(){
  LOGGER.log(INFO, "onPtnrCrStepNext called from step {0}", getStep());
  LOGGER.log(INFO,"addrDef {0}",this.addrDefault);
  int currStep = getStep();
  if(currStep == 0){
   this.ptnrVatable = false;
   if(ptnrBase.getPartnerRoles() == null ||ptnrBase.getPartnerRoles().isEmpty() ){
    MessageUtil.addClientErrorMessage("ptnrCrFrm:ptnrMsg", "ptnrNoRole", "validationText");
    return;
   }else{
    for(PartnerRoleRec pr:ptnrBase.getPartnerRoles()){
     LOGGER.log(INFO, "Ptnr taxable {0}", pr.isTaxable());
     if(pr.isTaxable()){
      ptnrVatable = true;
     }
    }
   }
   LOGGER.log(INFO, "After partner type taxable {0}", ptnrVatable);
   ptnrBase.setCountry(defaultCountry);
  }
  if(currStep == 0 && StringUtils.equals(ptnrType, "indiv") && ptnrPerson ==  null){
   ptnrPerson = new PartnerPersonRec();
   ptnrPerson.setRef(ptnrBase.getRef());
   ptnrPerson.setPartnerRoles(ptnrBase.getPartnerRoles());
  }
  if(currStep == 0 && StringUtils.equals(ptnrType, "corp") && ptnrCorp ==  null){
   ptnrCorp = new PartnerCorporateRec();
   ptnrCorp.setRef(ptnrBase.getRef());
   ptnrCorp.setPartnerRoles(ptnrBase.getPartnerRoles());
  }
  
  if(currStep == 0){
   // set partRolesOutput
   StringBuilder sb = null; 
   for(PartnerRoleRec r:ptnrBase.getPartnerRoles()){
    
    if(sb == null){
     sb = new StringBuilder();
     sb.append(r.getRoleName());
    }else{
     sb.append(", ");
     sb.append(r.getRoleName());
    }
   }
   this.ptnrRolesOutput = sb.toString();
   LOGGER.log(INFO, "ptnrRolesOutput {0}", ptnrRolesOutput);
  }
  
  currStep++;
  if(currStep > CR_STEP_MAX){
   currStep = CR_STEP_MAX;
  }
  LOGGER.log(INFO, "currStep {0}  ptnrType {1}", new Object[]{currStep,ptnrType});
  if(currStep == 2 && StringUtils.equals(ptnrType, "indiv")){
   LOGGER.log(INFO, "addrDefault {0} partBase Address {1}", new Object[]{addrDefault,ptnrBase.getDefaultAddress()});
   addrTypeSrch = "default";
   
   if(addrDefault == null){
    addrDefault = new AddressRec();
    addrDefault.setCountry(this.defaultCountry);
    PrimeFaces.current().ajax().update("ptnrCrFrm:addrPnl");
   }
   
   
   LOGGER.log(INFO, "addrDefault {0}", addrDefault);
   
  }
  
 
  LOGGER.log(INFO, "addrDefault {0}", addrDefault);
  if(currStep == 3  && StringUtils.equals(ptnrType, "indiv")){
   LOGGER.log(INFO, "addrDefault.getPostCode() blank", StringUtils.isBlank(addrDefault.getPostCode()));
   LOGGER.log(INFO, "addrDefault.getPostCode()",addrDefault.getPostCode());
   
   // if there has been an entry in address make sure minimum fields have 
   
   if(!addrDefaultChanged && ( !StringUtils.isBlank(addrDefault.getAddrRef())
    || !StringUtils.isBlank(addrDefault.getRoom())|| !StringUtils.isBlank(addrDefault.getBuilding())
    || !StringUtils.isBlank(addrDefault.getHouseName()) || !StringUtils.isBlank(addrDefault.getHouseNumber())
    || !StringUtils.isBlank(addrDefault.getStreet()) || !StringUtils.isBlank(addrDefault.getStreet2())
    || !StringUtils.isBlank(addrDefault.getTown()) || !StringUtils.isBlank(addrDefault.getCountyName())
    
    || !StringUtils.isBlank(addrDefault.getPostCode()))){
    addrDefaultChanged = true;
    LOGGER.log(INFO, "default address changed");
    LOGGER.log(INFO, "addrDefault.getPostCode() blank", StringUtils.isBlank(addrDefault.getPostCode()));
    LOGGER.log(INFO, "addrDefault.getAddrRef() blank", StringUtils.isBlank(addrDefault.getAddrRef()));
    if(StringUtils.isBlank(addrDefault.getAddrRef())){
     currStep = 2;
     MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrNoAddrRef", "validationText");
     PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
    }
    if(StringUtils.isBlank(addrDefault.getPostCode())){
     currStep = 2;
     MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrDefPostCode", "validationText");
     PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
    }
    PrimeFaces.current().ajax().update("ptnrCrFrm:addrPnl:addrRefDef");
   }
   
  }
  
  
  setStep(currStep);
  PrimeFaces.current().ajax().update("@(form:ptnrCrFrm)");
   
 }
 
 public void onPtnrDisplay(){
  
  
  List<String> ptnrParam = new ArrayList<>();
  ptnrParam.add(ptnrBase.getId().toString());
  
  LOGGER.log(INFO, "ptnrParam {0}", ptnrParam);
  Map<String, List<String>> params = new HashMap<>();
  params.put("partnerId", ptnrParam );
  
  Map<String, Object> options = new HashMap<>();
  options.put("includeViewParams", true);
  
  LOGGER.log(INFO, "params {0}", params);
  PrimeFaces.current().dialog().openDynamic("ptnrDispDlg",options,params);
  FacesContext fc = FacesContext.getCurrentInstance();
  fc.getExternalContext().getSessionMap().put("partner", ptnrBase);
 }
   
 public void onPtnrListContextMenu(SelectEvent evt){
  LOGGER.log(INFO, "onPtnrListContext Menu called ptnrBase {0} evt obj {1}", 
          new Object[]{this.ptnrBase,evt.getObject()});
  ptnrPerson = null;
  ptnrCorp = null;
  ptnrBase = (PartnerBaseRec)evt.getObject();
  if(((PartnerBaseRec)evt.getObject()).getPartnerRoles() == null){
   ptnrBase = this.ptnrMgr.getRolesForPtnr(ptnrBase);
   ListIterator<PartnerBaseRec> ptnrLi = this.ptnrList.listIterator();
   boolean found = false;
   while(ptnrLi.hasNext() && !found){
    PartnerBaseRec c = ptnrLi.next();
    if(Objects.equals(c.getId(), ptnrBase.getId())){
     ptnrLi.set(c);
     found = true;
    }
   }
  }
  if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerPersonRec")){
   ptnrPerson = (PartnerPersonRec)evt.getObject();
   ptnrType = "pers";
  }else if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerCorporateRec")){
   this.ptnrCorp = (PartnerCorporateRec)evt.getObject();
   ptnrType = "corp";
  }
  ptnrRolesOutput = buildPtnrRolesOutput(ptnrBase);
  
  PrimeFaces.current().ajax().update("dispPtnrFm");
 }
 
 public void onPtnrListMItemDispl(){
  LOGGER.log(INFO, "onPtnrListMItemDispl called ptnrType {0}",ptnrType );
  if(ptnrType.equals("pers")){
   
   PrimeFaces.current().executeScript("PF('dispPersPtnrDlgWv').show();");
   
   
  }else{
   
   PrimeFaces.current().executeScript("PF('dispCorpPtnrDlgWv').show();");
   
  }
 }
 public void onPtnrListRowSelect(SelectEvent evt){
  LOGGER.log(INFO, "onPtnrListRowSelect called ptnrBase {0} evt obj {1}", 
          new Object[]{this.ptnrBase,((PartnerBaseRec)evt.getObject())});
  ptnrPerson = null;
  ptnrCorp = null;
  ptnrBase = (PartnerBaseRec)evt.getObject();
  if(((PartnerBaseRec)evt.getObject()).getPartnerRoles() == null){
   ptnrBase = this.ptnrMgr.getRolesForPtnr(ptnrBase);
   ListIterator<PartnerBaseRec> ptnrLi = this.ptnrList.listIterator();
   boolean found = false;
   while(ptnrLi.hasNext() && !found){
    PartnerBaseRec c = ptnrLi.next();
    if(Objects.equals(c.getId(), ptnrBase.getId())){
     ptnrLi.set(c);
     found = true;
    }
   }
  }
  if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerPersonRec")){
   ptnrPerson = (PartnerPersonRec)evt.getObject();
   ptnrType = "pers";
  }else if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerCorporateRec")){
   this.ptnrCorp = (PartnerCorporateRec)evt.getObject();
   ptnrType = "corp";
  }
  ptnrRolesOutput = buildPtnrRolesOutput(ptnrBase);
  
  
      
  
  
  
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("dispPtnrFm");
  if(ptnrType.equals("pers")){
   
   pf.executeScript("PF('dispPersPtnrDlgWv').show();");
   
   
  }else{
   
   pf.executeScript("PF('dispCorpPtnrDlgWv').show();");
   
  }
  
 }
 
 
 
 

 
 public void onAddrHeadOffPostCodeSearch(){
  addrTypeSrch = "headOffice";
  String srchPc = addrHeadOff.getPostCode();
  LOGGER.log(INFO, "srchPc {0}", srchPc);
  addrList = null;
  addrList = this.ptnrMgr.getAddressesForPostCodePart(srchPc);
  if(addrList == null || addrList.isEmpty()){
   MessageUtil.addWarnMessage("addrNoAddrPc", "validationText");
  }else{
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("srchAddrLst");
   rCtx.execute("PF('addrSrchWv').show()");
  }
 }
  
  public void onAddrRegOffPostCodeSearch(){
  addrTypeSrch = "regOffice";
  String srchPc = addrRegOff.getPostCode();
  LOGGER.log(INFO, "srchPc {0}", srchPc);
  addrList = null;
  addrList = this.ptnrMgr.getAddressesForPostCodePart(srchPc);
  if(addrList == null || addrList.isEmpty()){
   MessageUtil.addWarnMessage("addrNoAddrPc", "validationText");
  }else{
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("srchAddrLst");
   rCtx.execute("PF('addrSrchWv').show()");
  }
 }
 
 public void onAddrPostCdSrchDlgBtn(){
  addrSrchByPostCd = this.addrDefault.getPostCode();
  addrList = null;
  PrimeFaces pf = PrimeFaces.current();
  
  pf.ajax().update("postCdSearch");
  pf.executeScript("PF('addrPstcdSrchDlgWv').show()");
 }
 
 public void onAddrPostCdSrch(){
  
  List<AddressRec> foundAddrList = this.ptnrMgr.getAddressesForPostCodePart(addrSrchByPostCd);
  this.addrList = foundAddrList;
  PrimeFaces.current().ajax().update("postCdSearch:addrList");
  
  
 }
 public void onAddrHeadOffFieldChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "Field entry {0}", evt.getNewValue().getClass().getSimpleName());
  String entry = ((String)evt.getNewValue()).trim();
  
  boolean priorEntry = false;
  
  if(evt.getOldValue() != null){
   priorEntry = true;
  }
  LOGGER.log(INFO, "Entry empty {0}", entry.isEmpty());
  if(entry.isEmpty() ){
   if(priorEntry){
    
   addrHeadOffEntries--;
   }
  }else{
   addrHeadOffEntries++;
  }
  if(addrHeadOffEntries < 0){
   addrHeadOffEntries = 0;
  }
  LOGGER.log(INFO, "addrHeadOffEntries {0}", addrHeadOffEntries);
 }
 
 public void onAddrNewFieldChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "Field entry {0}", evt.getNewValue().getClass().getSimpleName());
  String entry = ((String)evt.getNewValue()).trim();
  
  boolean priorEntry = false;
  
  if(evt.getOldValue() != null){
   priorEntry = true;
  }
  LOGGER.log(INFO, "Entry empty {0}", entry.isEmpty());
  if(entry.isEmpty() ){
   if(priorEntry){
    
   addrNewEntries--;
   }
  }else{
   addrNewEntries++;
  }
  if(addrNewEntries < 0){
   addrNewEntries = 0;
  }
  LOGGER.log(INFO, "addrNewEntries {0}", addrNewEntries);
 }
 
 public List<AddressRec> onPostCodeComplete(String input){
  LOGGER.log(INFO, "Called onPOstCodeComplete with {0}", input);
  List<AddressRec> retList;
  if(StringUtils.isBlank(input)){
   retList = ptnrMgr.getAllAddresses();
  }else{
   retList = ptnrMgr.getAddressesForPostCodePart(input);
  }
  return retList;
 }
 
 public void onAddrRefValidate(FacesContext c, UIComponent comp, Object val){
  LOGGER.log(INFO, "onAddrRefValidate val {0}", val);
  
 }
 public void onAddrRegOffFieldChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "Field entry {0}", evt.getNewValue().getClass().getSimpleName());
  String entry = ((String)evt.getNewValue()).trim();
  
  boolean priorEntry = false;
  
  if(evt.getOldValue() != null){
   priorEntry = true;
  }
  LOGGER.log(INFO, "Entry empty {0}", entry.isEmpty());
  if(entry.isEmpty() ){
   if(priorEntry){
    
   addrRegOffEntries--;
   }
  }else{
   addrRegOffEntries++;
  }
  if(addrRegOffEntries < 0){
   addrRegOffEntries = 0;
  }
  LOGGER.log(INFO, "addrHeadOffEntries {0}", addrRegOffEntries);
 }
 
 public void onAddrSelectRow(SelectEvent evt){
  LOGGER.log(INFO, "onAddrSelectRow called with row {0}", evt.getObject());
  LOGGER.log(INFO, "addrTypeSrch {0}", addrTypeSrch);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(StringUtils.equals(addrTypeSrch, "default") ){
   //default address
   addrDefault = (AddressRec)evt.getObject();
   LOGGER.log(INFO, "default pc {0}", addrDefault.getPostCode());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("ptnrCrFrm:addrPnl");
   pf.executeScript("PF('addrPstcdSrchDlgWv').hide()");
   
   
  }else if(addrTypeSrch.equalsIgnoreCase("headOffice")){
   addrHeadOff = (AddressRec)evt.getObject();
   rCtx.update("hoAddrPg");
   rCtx.execute("PF('addrSrchWv').hide()");
  }else if(addrTypeSrch.equalsIgnoreCase("regOffice")){
   addrRegOff = (AddressRec)evt.getObject();
   rCtx.update("regOffAddrPg");
   rCtx.execute("PF('addrSrchWv').hide()");
  }
  
 }
 
 public boolean onAddrTabControl(TabEvent evt){
  LOGGER.log(INFO, "addrTabControl called with tab title {0} id {1}", 
          new Object[]{evt.getTab().getTitle(), evt.getTab().getId()});
  LOGGER.log(INFO, "addrTabCurr {0}", addrTabCurr);
  boolean addrOk = true;
  switch (addrTabCurr) {
   case "addrHeadOffTab":
    if( !StringUtils.isBlank(addrHeadOff.getAddrRef())
            || !StringUtils.isBlank(addrHeadOff.getRoom())|| !StringUtils.isBlank(addrHeadOff.getBuilding())
            || !StringUtils.isBlank(addrHeadOff.getHouseName()) || !StringUtils.isBlank(addrHeadOff.getHouseNumber())
            || !StringUtils.isBlank(addrHeadOff.getStreet()) || !StringUtils.isBlank(addrHeadOff.getStreet2())
            || !StringUtils.isBlank(addrHeadOff.getTown()) || !StringUtils.isBlank(addrHeadOff.getCountyName())
            
            || !StringUtils.isBlank(addrHeadOff.getPostCode())){
     
     if(StringUtils.isBlank(addrHeadOff.getAddrRef())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrNoAddrRef", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
     if(StringUtils.isBlank(addrHeadOff.getPostCode())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrDefPostCode", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
    }break;
   case "addrRegTab":
    if( !StringUtils.isBlank(addrRegOff.getAddrRef())
            || !StringUtils.isBlank(addrRegOff.getRoom())|| !StringUtils.isBlank(addrRegOff.getBuilding())
            || !StringUtils.isBlank(addrRegOff.getHouseName()) || !StringUtils.isBlank(addrRegOff.getHouseNumber())
            || !StringUtils.isBlank(addrRegOff.getStreet()) || !StringUtils.isBlank(addrRegOff.getStreet2())
            || !StringUtils.isBlank(addrRegOff.getTown()) || !StringUtils.isBlank(addrRegOff.getCountyName())
            
            || !StringUtils.isBlank(addrRegOff.getPostCode())){
     
     if(StringUtils.isBlank(addrRegOff.getAddrRef())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrNoAddrRef", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
     if(StringUtils.isBlank(addrRegOff.getPostCode())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrDefPostCode", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
    }break;
   case "addDefTab":
    LOGGER.log(INFO, "addrDefault {0}", addrDefault.getHouseName());
    if( !StringUtils.isBlank(addrDefault.getAddrRef())
            || !StringUtils.isBlank(addrDefault.getRoom())|| !StringUtils.isBlank(addrDefault.getBuilding())
            || !StringUtils.isBlank(addrDefault.getHouseName()) || !StringUtils.isBlank(addrDefault.getHouseNumber())
            || !StringUtils.isBlank(addrDefault.getStreet()) || !StringUtils.isBlank(addrDefault.getStreet2())
            || !StringUtils.isBlank(addrDefault.getTown()) || !StringUtils.isBlank(addrDefault.getCountyName())
            
            || !StringUtils.isBlank(addrDefault.getPostCode())){
     LOGGER.log(INFO,"Address changed");
     LOGGER.log(INFO,"addrOk {0}",addrOk);
     LOGGER.log(INFO,"getAddrRef() {0} post code",new Object[]{addrDefault.getAddrRef(),addrDefault.getPostCode()});
     if(StringUtils.isBlank(addrDefault.getAddrRef())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrNoAddrRef", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
     if(StringUtils.isBlank(addrDefault.getPostCode())){
      addrOk = false;
      MessageUtil.addClientWarnMessage("ptnrCrFrm:addrMsgs", "addrDefPostCode", "validationText");
      PrimeFaces.current().ajax().update("ptnrCrFrm:addrMsgs");
     }
    }break;
   default:
    break;
  }
  LOGGER.log(INFO, "addrOk {0}", addrOk);
  if(addrOk){
   addrTabCurr = evt.getTab().getId();
   return true;
   
  }else{
  return false; 
  }
  
  
  
  
 }
 
 public void onAddrTabChange(TabChangeEvent evt){
  LOGGER.log(INFO, "onAddrTabChange called with tab title {0} id {1}", 
          new Object[]{evt.getTab().getTitle(), evt.getTab().getId()});
  switch (addrTabCurr) {
   case "addrHeadOffTab":
    if(addrHeadOff.getCountry() == null){ 
     addrHeadOff.setCountry(defaultCountry);
     PrimeFaces.current().ajax().update("ptnrCrFrm");
    }
    break;
   case "addrRegTab":
    if(this.addrRegOff.getCountry() == null){ 
     addrRegOff.setCountry(defaultCountry);
     PrimeFaces.current().ajax().update("ptnrCrFrm");
    }
   break;
  }
 }
 public void onAddrTabClose(TabCloseEvent evt){
  LOGGER.log(INFO, "onAddrTabClose called with tab title {0} id {1}", 
          new Object[]{evt.getTab().getTitle(), evt.getTab().getId()});
 }
 public void onSaveNewPtnr(){
  LOGGER.log(INFO, "Called save ptnr");
  // Add addresses as required
  PartnerBaseRec updatePtnr = null;
  if(StringUtils.equals(ptnrType, "indiv")){
   // fill out individual partner
   // Step 0  basic data
   ptnrPerson.setRef(ptnrBase.getRef());
   ptnrPerson.setPartnerRoles(ptnrBase.getPartnerRoles());
   ptnrPerson.setCountry(ptnrBase.getCountry());
   
   // step 2 Address data
   if(!StringUtils.isBlank(addrDefault.getAddrRef())){
    addrDefault.setCreatedBy(this.getLoggedInUser());
    addrDefault.setCreatedOn(new Date());
    ptnrPerson.setDefaultAddress(addrDefault);
   }
   if(addrHeadOff != null){
    addrHeadOff.setCreatedBy(this.getLoggedInUser());
    addrHeadOff.setCreatedOn(new Date());
    ptnrPerson.setHeadOfficeAddress(addrHeadOff);
   }
   updatePtnr = ptnrPerson;
  }else if(StringUtils.equals(ptnrType, "corp")){
   ptnrCorp.setRef(ptnrBase.getRef());
   ptnrCorp.setPartnerRoles(ptnrBase.getPartnerRoles());
   ptnrCorp.setCountry(ptnrBase.getCountry());
   
   if(!StringUtils.isBlank(addrDefault.getAddrRef())){
    addrDefault.setCreatedBy(this.getLoggedInUser());
    addrDefault.setCreatedOn(new Date());
    ptnrCorp.setDefaultAddress(addrDefault);
   }
   if( addrHeadOff != null){
    addrHeadOff.setCreatedBy(this.getLoggedInUser());
    addrHeadOff.setCreatedOn(new Date());
    ptnrCorp.setHeadOfficeAddress(addrHeadOff);
   }
   if(addrRegOff != null){
    ptnrCorp.setRegisteredOfficeAddress(addrRegOff);
   }
   updatePtnr = ptnrCorp;
  }
  LOGGER.log(INFO, "roles {0}", updatePtnr.getPartnerRoles());
  updatePtnr.setCreatedBy(this.getLoggedInUser());
  updatePtnr.setCreatedDate(new Date());
  
  LOGGER.log(INFO,"default address is {0}",updatePtnr.getDefaultAddress());
  updatePtnr = ptnrMgr.updatePartner(updatePtnr, getView());
  LOGGER.log(INFO, "updatePtnr id {0}", updatePtnr.getId());
  if(updatePtnr.getId() != null){
   if(StringUtils.equals(ptnrType, "indiv")){
    MessageUtil.addClientInfoMessage("ptnrCrFrm:ptnrMsg", "ptnrIndivCr", "blacResponse");
   }else if(StringUtils.equals(ptnrType, "corp")){
    MessageUtil.addClientInfoMessage("ptnrCrFrm:ptnrMsg", "ptnrCorpCr", "blacResponse");
   }
   this.setStep(0);
   ptnrBase = new PartnerBaseRec();
   ptnrType = null;
   ptnrPerson = null;
   ptnrCorp = null;
   PrimeFaces.current().ajax().update("@(form:ptnrCrFrm)");
   
  }else{
   LOGGER.log(INFO, "failed update");
   LOGGER.log(INFO, "partner type {0} update type {1}", 
           new Object[]{ptnrType,updatePtnr.getClass().getSimpleName()});
   if(StringUtils.equals(ptnrType, "indiv")){
    MessageUtil.addClientWarnMessage("ptnrCrFrm:ptnrErrMsg", "partnerIndivCr", "errorText");
   
   }else if(StringUtils.equals(ptnrType, "corp")){
    MessageUtil.addClientWarnMessage("ptnrCrFrm:ptnrErrMsg", "partnerCorpCr", "errorText");
   }
   //PrimeFaces.current().ajax().update("@(form:ptnrCrFrm)");
   PrimeFaces.current().ajax().update("ptnrCrFrm:ptnrErrMsg");
  }
  /*
PartnerBaseRec ptnrUpdate;
  LOGGER.log(INFO, "Addresses default {0} head off {1} reg off {2}", new Object[]{addrDefault,addrHeadOff,addrRegOff});
  
  if(ptnrType.equalsIgnoreCase("corp")){
   if(addrDefEntries >0){
    if(addrDefault.getId() == null){
     addrDefault.setCreatedBy(this.getLoggedInUser());
     addrDefault.setCreatedOn(new Date());
    }
    ptnrCorp.setDefaultAddress(addrDefault);
   }
   if(this.addrHeadOffEntries >0){
    if(addrHeadOff.getId() == null){
     addrHeadOff.setCreatedBy(this.getLoggedInUser());
     addrHeadOff.setCreatedOn(new Date());
    }
    ptnrCorp.setHeadOfficeAddress(addrHeadOff);
   }
   if(this.addrRegOffEntries >0){
    if(addrRegOff.getId() == null){
     addrRegOff.setCreatedBy(this.getLoggedInUser());
     addrRegOff.setCreatedOn(new Date());
    }
    ptnrCorp.setRegisteredOfficeAddress(addrRegOff);
   }
   ptnrUpdate = ptnrCorp;
   
  }else{
   if(addrDefEntries > 0){
    if(addrDefault.getId() == null){
     addrDefault.setCreatedBy(this.getLoggedInUser());
     addrDefault.setCreatedOn(new Date());
    }
    LOGGER.log(INFO, ptnrType, ptnrMgr);
    ptnrPerson.setDefaultAddress(addrDefault);
   }else{
    ptnrPerson.setDefaultAddress(null);
   }
   LOGGER.log(INFO, "addrDefEntries {0} ptnrPerson addr {1}", new Object[]{addrDefEntries,ptnrPerson.getDefaultAddress()});
   ptnrUpdate = ptnrPerson;
  }
  ptnrUpdate.setRef(this.ptnrBase.getRef());
   try{
   ptnrUpdate.setCreatedBy(this.getLoggedInUser());
   ptnrUpdate.setCreatedDate(new Date());
   LOGGER.log(INFO, "ptnrUpdate created by {0}", ptnrUpdate.getCreatedBy());
   ptnrUpdate = ptnrMgr.updatePartner(ptnrUpdate, getView());
   LOGGER.log(INFO, "Partner manager returns partner id {0}", ptnrUpdate.getId());
   
   if(ptnrType.equalsIgnoreCase("corp")){
    MessageUtil.addInfoMessage("ptnrCorpCr", "blacResponse");
   }else{
    MessageUtil.addInfoMessage("ptnrIndivCr", "blacResponse");
   }
   createOk = true;
  }catch(Exception e){
   if(ptnrType.equalsIgnoreCase("corp")){
    MessageUtil.addErrorMessage("partnerCorpCr", "errorText");
   }else{
    MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
   }
   
  }
  */
 }
 
 public String onSaveUpdatePtnrAction(){
  if(this.updateOk){
   return "home";
  }else{
   return null;
  }

 }
 public void onSaveUpdatePtnr(){
  
  LOGGER.log(INFO, "onSaveUpdatePtnr Addresses default {0} head off {1} reg off {2}", new Object[]{addrDefault,addrHeadOff,addrRegOff});
  PartnerBaseRec ptnrUpdate;
  if(ptnrType.equalsIgnoreCase("Company")){
   if(addrDefEntries >0){
    if(addrDefault.getId() == null){
     addrDefault.setCreatedBy(this.getLoggedInUser());
     addrDefault.setCreatedOn(new Date());
    }else{
     addrDefault.setChangedBy(getLoggedInUser());
     addrDefault.setChangedOn(new Date());
    }
    ptnrCorp.setDefaultAddress(addrDefault);
   }
   if(this.addrHeadOffEntries >0){
    if(addrHeadOff.getId() == null){
     addrHeadOff.setCreatedBy(this.getLoggedInUser());
     addrHeadOff.setCreatedOn(new Date());
    }else{
     addrHeadOff.setChangedBy(getLoggedInUser());
     addrHeadOff.setChangedOn(new Date());
    }
    ptnrCorp.setHeadOfficeAddress(addrHeadOff);
   }
   if(this.addrRegOffEntries >0){
    if(addrRegOff.getId() == null){
     addrRegOff.setCreatedBy(this.getLoggedInUser());
     addrRegOff.setCreatedOn(new Date());
    }else{
     addrHeadOff.setChangedBy(getLoggedInUser());
     addrHeadOff.setChangedOn(new Date());
    }
    ptnrCorp.setRegisteredOfficeAddress(addrRegOff);
   }
   ptnrUpdate = ptnrCorp;
  }else{
    if(addrDefEntries > 0){
    if(addrDefault.getId() == null){
     addrDefault.setCreatedBy(this.getLoggedInUser());
     addrDefault.setCreatedOn(new Date());
    }else{
     addrDefault.setChangedBy(getLoggedInUser());
     addrDefault.setChangedOn(new Date());
    }
    LOGGER.log(INFO, ptnrType, ptnrMgr);
    ptnrPerson.setDefaultAddress(addrDefault);
   }
   LOGGER.log(INFO, "addrDefEntries {0} ptnrPerson addr {1}", new Object[]{addrDefEntries,ptnrPerson.getDefaultAddress()});
   ptnrUpdate = ptnrPerson;
  }
  
  ptnrUpdate.setRef(this.ptnrBase.getRef());
   try{
   ptnrUpdate.setChangedBy(this.getLoggedInUser());
   ptnrUpdate.setChangedOn(new Date());
   LOGGER.log(INFO, "ptnrUpdate created by {0}", ptnrUpdate.getCreatedBy());
   ptnrUpdate = ptnrMgr.updatePartner(ptnrUpdate, getView());
   LOGGER.log(INFO, "Partner manager returns partner id {0}", ptnrUpdate.getId());
   
   if(ptnrType.equalsIgnoreCase("Company")){
    MessageUtil.addInfoMessage("ptnrCorpUpdt", "blacResponse");
   }else{
    MessageUtil.addInfoMessage("ptnrIndivUpdt", "blacResponse");
   }
   updateOk = true;
  }catch(Exception e){
   if(ptnrType.equalsIgnoreCase("Company")){
    MessageUtil.addErrorMessage("partnerCorpCr", "errorText");
   }else{
    MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
   }
   
  }
  
 }
 
 public String onSaveNewPtnrAction(){
  if(this.createOk){
   return "home";
  }else{
   return null;
  }
 }
 
 public void onTransferAddr(){
  LOGGER.log(INFO, "onTransferAddr called for addr {0}",this.newAddDlgFor);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(newAddDlgFor.equalsIgnoreCase("default")){
   this.addrDefault = this.addrNew;
   rCtx.update("addrDefPg");
   this.addrDefEntries = addrNewEntries;
  }else if(newAddDlgFor.equalsIgnoreCase("regOff")){
   this.addrRegOff = addrNew;
   this.addrRegOffEntries = addrNewEntries;
   rCtx.update("regOffAddrPg");
  }else if(newAddDlgFor.equalsIgnoreCase("headOff")){
   this.addrHeadOff = addrNew;
   this.addrRegOffEntries = addrNewEntries;
   rCtx.update("hoAddrPg");
   
  }
  addrNew = null;
  addrNewEntries = 0;
  rCtx.execute("PF('addNewAddrDlgWv').hide()");
 }
}
