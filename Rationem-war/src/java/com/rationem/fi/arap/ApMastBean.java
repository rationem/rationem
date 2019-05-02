/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.BankByBankCode;
import com.rationem.helper.comparitor.PartnerByTradingName;
import com.rationem.util.BankValidation;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.comparator.PartnerBaseByTradeName;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;
import uk.co.unifiedsoftware.bankvaluk.WebMethods;


/**
 *
 * @author Chris
 */

public class ApMastBean extends BaseBean{

 private final WebMethods bv = new WebMethods();
   private static final Logger LOGGER =  Logger.getLogger(ApMastBean.class.getName());
   public static final int CR_PARTNER_ST = 0;
   public static final int CR_ADDR_ST = 1;
   public static final int CR_ACNT_ST = 2;
   public static final int CR_BNK_ST = 3;
   public static final int CR_SUM_ST = 4;

   @EJB
   private SysBuffer buff;

   @EJB
   private ApManager apManager;

   @EJB
   private MasterDataManager ptnrMgr;

   @EJB
   private GlAccountManager glAcMgr;

   @EJB
   private BankManager bankMgr;


   private ApAccountRec apAccount;
   private boolean apAccountSelected;
   private int apAcntCrStep;
   private AddressRec addrEdit;
   private AddressRec addrSelect;
   private CompanyBasicRec comp;
   private AddressRec postCodeAddress;
   private List<AddressRec> addressList;
   private PartnerCorporateRec ptnrCorp;
   private PartnerPersonRec ptnrPerson;
   private PartnerBaseRec ptnrBase;
   private boolean ptnrSelected;
   private List<PartnerCorporateRec> ptnrCorpList;
   private List<PartnerPersonRec> ptnrPersonList;
   private List<SortOrderRec> sortOrderList;
   private List<FiGlAccountCompRec> glReconciliationAcs;
   private List<PaymentTermsRec> payTermsList;
   private List<PaymentTypeRec> payTypes;
   private List<BankRec> banks;
   private BankRec bankNoSelect;
   private List<BankBranchRec> bankBranches;
   private List<BankAccountRec> bankAccounts;
   private List<CountryRec> countries;
   private ArBankAccountRec bankAcApSelected;
   private ArBankAccountRec bankAcApNew;
   private PartnerCorporateRec bnkNewOrg;
   private String bnkSortWs;
   private String bnkAcntNumWs;
   private boolean bnkAcntAddrEdit;
   private boolean usePtnrAddr;
   private boolean acntAddrNew;
   private String acntpostCdSel;
   private String dialogId;
   
   
   
   private String selectedPtnrTypeCd = "corp";
   private boolean apAccountEntered = false;
   private boolean apAcntCrStepCompl= false;
   
 /**
  * Creates a new instance of ApMastBean
  */
 public ApMastBean() {
 
 }
 
 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "apMastBean init called");
  comp = this.getCompList().get(0);
  apAccount = new ApAccountRec();
  apAccount.setCompany(comp);
  apAcntCrStep = 0;
  acntAddrNew = false;
  bnkAcntAddrEdit = true;
  
  LOGGER.log(INFO, "end of init");
 }

 public boolean isAcntAddrNew() {
  return acntAddrNew;
 }

 public void setAcntAddrNew(boolean acntAddrNew) {
  this.acntAddrNew = acntAddrNew;
 }

 public String getAcntpostCdSel() {
  return acntpostCdSel;
 }

 public void setAcntpostCdSel(String acntpostCdSel) {
  this.acntpostCdSel = acntpostCdSel;
 }

 
 
 public AddressRec getAddrEdit() {
  return addrEdit;
 }

 public void setAddrEdit(AddressRec addrEdit) {
  this.addrEdit = addrEdit;
 }

 public List<AddressRec> getAddressList() {
  return addressList;
 }

 public void setAddressList(List<AddressRec> addressList) {
  this.addressList = addressList;
 }

 public AddressRec getAddrSelect() {
  return addrSelect;
 }

 public void setAddrSelect(AddressRec addrSelect) {
  this.addrSelect = addrSelect;
 }

 
 
 public ApAccountRec getApAccount() {
  if(apAccount == null){
   apAccount = new ApAccountRec();
   
  }
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 
 public boolean isApAccountEntered() {
  return apAccountEntered;
 }

 public void setApAccountEntered(boolean apAccountEntered) {
  this.apAccountEntered = apAccountEntered;
 }

 public boolean isApAccountSelected() {
  return apAccountSelected;
 }

 public void setApAccountSelected(boolean apAccountSelected) {
  this.apAccountSelected = apAccountSelected;
 }

 public int getApAcntCrStep() {
  return apAcntCrStep;
 }

 public void setApAcntCrStep(int apAcntCrStep) {
  this.apAcntCrStep = apAcntCrStep;
 }

 
 public boolean isApAcntCrStepCompl() {
  return apAcntCrStepCompl;
 }

 public void setApAcntCrStepCompl(boolean apAcntCrStepCompl) {
  this.apAcntCrStepCompl = apAcntCrStepCompl;
 }

 
 public ArBankAccountRec getBankAcApSelected() {
  return bankAcApSelected;
 }

 public void setBankAcApSelected(ArBankAccountRec bankAcApSelected) {
  this.bankAcApSelected = bankAcApSelected;
 }

 public List<BankAccountRec> getBankAccounts() {
  return bankAccounts;
 }

 public void setBankAccounts(List<BankAccountRec> bankAccounts) {
  this.bankAccounts = bankAccounts;
 }

 public List<BankBranchRec> getBankBranches() {
  return bankBranches;
 }

 public void setBankBranches(List<BankBranchRec> bankBranches) {
  this.bankBranches = bankBranches;
 }

 public BankRec getBankNoSelect() {
  return bankNoSelect;
 }

 public void setBankNoSelect(BankRec bankNoSelect) {
  this.bankNoSelect = bankNoSelect;
 }

 public List<BankRec> getBanks() {
  return banks;
 }

 public void setBanks(List<BankRec> banks) {
  this.banks = banks;
 }

 
 public ArBankAccountRec getBankAcApNew() {
  return bankAcApNew;
 }

 public void setBankAcApNew(ArBankAccountRec bankAcApNew) {
  this.bankAcApNew = bankAcApNew;
 }

 public PartnerCorporateRec getBnkNewOrg() {
  return bnkNewOrg;
 }

 public void setBnkNewOrg(PartnerCorporateRec bnkNewOrg) {
  this.bnkNewOrg = bnkNewOrg;
 }

 public String getBnkSortWs() {
  return bnkSortWs;
 }

 public void setBnkSortWs(String bnkSortWs) {
  this.bnkSortWs = bnkSortWs;
 }

 public boolean isBnkAcntAddrEdit() {
  return bnkAcntAddrEdit;
 }

 public void setBnkAcntAddrEdit(boolean bnkAcntAddrEdit) {
  this.bnkAcntAddrEdit = bnkAcntAddrEdit;
 }

 
 public String getBnkAcntNumWs() {
  return bnkAcntNumWs;
 }

 public void setBnkAcntNumWs(String bnkAcntNumWs) {
  this.bnkAcntNumWs = bnkAcntNumWs;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public List<CountryRec> getCountries() {
  
  return countries;
 }

 public void setCountries(List<CountryRec> countries) {
  this.countries = countries;
 }

 public List<PartnerCorporateRec> corpPtnrComplete(String val) {

  LOGGER.log(INFO, "corpPtnrComplete called with {0}", val);
  if(val == null || val.isEmpty()){
    val = new String();
  }
  
  List<PartnerCorporateRec> retList = ptnrMgr.getCorpPartnersByTradingName(val,true);
  if(retList == null){
    retList = new ArrayList<>();
    PartnerCorporateRec ptnr = new PartnerCorporateRec();
    ptnr.setTradingName("No Corp partner - please create");
    Long noPtnrId = Long.valueOf("0");
    ptnr.setId(noPtnrId);
    retList.add(ptnr);
    return retList;
  }
  Collections.sort(retList, new PartnerBaseByTradeName());
  List<PartnerCorporateRec> ptnrList = new ArrayList<>();
  ListIterator<PartnerCorporateRec> ptnrLi = retList.listIterator();
  
  boolean foundBnk = false;
  while(ptnrLi.hasNext() ){
   foundBnk = false;
   PartnerCorporateRec currPtnr = ptnrLi.next();
   LOGGER.log(INFO, "Partner trading name {0}", currPtnr.getTradingName());
   LOGGER.log(INFO,"num roles {0}",currPtnr.getPartnerRoles());
   if(currPtnr.getPartnerRoles() != null && !currPtnr.getPartnerRoles().isEmpty()){
    ListIterator<PartnerRoleRec> rolesLi = currPtnr.getPartnerRoles().listIterator();
    while(rolesLi.hasNext() && !foundBnk){
     PartnerRoleRec rl = rolesLi.next();
     LOGGER.log(INFO, "role code {0}", rl.getRoleCode());
     if(rl.getRoleCode().equals("BNK")){
      LOGGER.log(INFO, "Found BNK role");
      foundBnk = true;
     }
    }
   }
   LOGGER.log(INFO, "Partner trading name {0}", currPtnr.getTradingName());
   LOGGER.log(INFO, "foundBnk {0}", foundBnk);
   if(!foundBnk){
    ptnrList.add(currPtnr);
    LOGGER.log(INFO, "Added to partner list");
   }
  }
  LOGGER.log(INFO, "corpPtnrComplete returns {0}", ptnrList);
  return ptnrList;
  }

 

 
 public List<FiGlAccountCompRec> getGlReconciliationAcs() {
  if(glReconciliationAcs == null){
      glReconciliationAcs = glAcMgr.getCreditorReconciliationAcs(apAccount.getCompany());
      LOGGER.log(INFO, "Gl accountamanger returned {0}", glReconciliationAcs);
      if(glReconciliationAcs != null && !glReconciliationAcs.isEmpty() && this.apAccountEntered ){
        apAccount.setReconciliationAc(glReconciliationAcs.get(0));
      }
    }
  return glReconciliationAcs;
 }

 public void setGlReconciliationAcs(List<FiGlAccountCompRec> glReconciliationAcs) {
  this.glReconciliationAcs = glReconciliationAcs;
 }

 public List<PaymentTermsRec> getPayTermsList() {
  if(payTermsList == null){
   payTermsList = buff.getPaymentTermsList();
  }
  return payTermsList;
 }

 public void setPayTermsList(List<PaymentTermsRec> payTermsList) {
  this.payTermsList = payTermsList;
 }

 public List<PaymentTypeRec> getPayTypes() {
  if(payTypes == null){
   payTypes = buff.getPaymentTypes();
  }
  return payTypes;
 }

 public void setPayTypes(List<PaymentTypeRec> payTypes) {
  this.payTypes = payTypes;
 }

 public AddressRec getPostCodeAddress() {
  return postCodeAddress;
 }

 public void setPostCodeAddress(AddressRec postCodeAddress) {
  this.postCodeAddress = postCodeAddress;
 }
 
 public MasterDataManager getPtnrMgr() {
  return ptnrMgr;
 }

 public void setPtnrMgr(MasterDataManager ptnrMgr) {
  this.ptnrMgr = ptnrMgr;
 }

 public PartnerCorporateRec getPtnrCorp() {
  if(ptnrCorp == null){
   ptnrCorp = new PartnerCorporateRec();
  }
  return ptnrCorp;
 }

 public void setPtnrCorp(PartnerCorporateRec ptnrCorp) {
  this.ptnrCorp = ptnrCorp;
 }

 public PartnerPersonRec getPtnrPerson() {
  if(ptnrPerson == null){
   ptnrPerson = new PartnerPersonRec();
  }
  
  return ptnrPerson;
 }

 public void setPtnrPerson(PartnerPersonRec ptnrPerson) {
  
  this.ptnrPerson = ptnrPerson;
 }

 public PartnerBaseRec getPtnrBase() {
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
 }

 public List<PartnerCorporateRec> getPtnrCorpList() {
  return ptnrCorpList;
 }

 public void setPtnrCorpList(List<PartnerCorporateRec> ptnrCorpList) {
  this.ptnrCorpList = ptnrCorpList;
 }

 public List<PartnerPersonRec> getPtnrPersonList() {
  return ptnrPersonList;
 }

 public void setPtnrPersonList(List<PartnerPersonRec> ptnrPersonList) {
  this.ptnrPersonList = ptnrPersonList;
 }

 public boolean isPtnrSelected() {
  return ptnrSelected;
 }

 public void setPtnrSelected(boolean ptnrSelected) {
  this.ptnrSelected = ptnrSelected;
 }

 

 public String getSelectedPtnrTypeCd() {
  return selectedPtnrTypeCd;
 }

 public void setSelectedPtnrTypeCd(String selectedPtnrTypeCd) {
  this.selectedPtnrTypeCd = selectedPtnrTypeCd;
 }

 public List<SortOrderRec> getSortOrderList() {
  
    if(sortOrderList == null){
      // need to get sort orders from sys buffer
      LOGGER.log(INFO, "WEb need to get sort orders");
      UserRec usr = getLoggedInUser();
      LOGGER.log(INFO, "LOgged in user {0}", usr);
      sortOrderList = this.buff.getSortOrders();
      if(sortOrderList != null && !sortOrderList.isEmpty()){
       SortOrderRec sort = sortOrderList.get(0);
       
       if(apAccount == null){
        apAccount = this.getApAccount();
       }
       apAccount.setSortOrder(sort);
      }
      LOGGER.log(INFO, "Sort orders from sys buffer {0}", sortOrderList);
    }
    return sortOrderList;
  }
 
 public void setSortOrderList(List<SortOrderRec> sortOrderList) {
  this.sortOrderList = sortOrderList;
 }

 public boolean isUsePtnrAddr() {
  return usePtnrAddr;
 }

 public void setUsePtnrAddr(boolean usePtnrAddr) {
  this.usePtnrAddr = usePtnrAddr;
 }
 
 
 
 public void onApActNumberBlur(){
  LOGGER.log(INFO, "onApActNumberBlur called acnt code: {0} ",apAccount.getAccountCode()); 
  
   if(apAccount.getCompany() == null){
    apAccount.setCompany(this.getCompList().get(0))  ;
    LOGGER.log(INFO, "onArActNumberBlur called company  after set company{0}", apAccount.getCompany().getId());
   }
   boolean accountExists = apManager.apAccountExists(apAccount);
   LOGGER.log(INFO, "accountExists {0}", accountExists);
   if(accountExists){
    apAccountEntered = false;
    MessageUtil.addWarnMessageParam("apAcntExists", "validationText", apAccount.getAccountCode());
    List<String> updtList = new ArrayList<>();
    updtList.add("apActCrFrm:errMsg");
    updtList.add("apActCrFrm:apAcntName");
    updtList.add("apActCrFrm:ptnrDetPg");
    PrimeFaces.current().ajax().update(updtList);
    
   }else{
    apAccountEntered = true;
    List<String> updtList = new ArrayList<>();
    updtList.add("apActCrFrm:errMsg");
    updtList.add("apActCrFrm:apAcntName");
    updtList.add("apActCrFrm:ptnrDetPg");
    PrimeFaces.current().ajax().update(updtList);
   }
  
 }
 
 public void onApAcntSelect(SelectEvent evt){
  apAccount = (ApAccountRec)evt.getObject();
  apAccountSelected = true;
 }
 public void onAddrEditNewAddr(){
  addrEdit = new AddressRec();
  addrEdit.setCountry(this.apAccount.getCompany().getCountry());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addrEdFrm:addrEdPgId");
 }
 
 public void onAddrPostCdSrch(){
  LOGGER.log(INFO, "onAddrPostCdSrch called");
  addressList = ptnrMgr.getAddressesForPostCodePart(acntpostCdSel);
  LOGGER.log(INFO, "Addresses returned {0}",addressList);
  if(addressList == null || addressList.isEmpty() ){
   MessageUtil.addClientWarnMessage("selAddrFrm:grl", "addrNoAddrPc", "validationText");
   PrimeFaces.current().ajax().update("selAddrFrm:grl");
  }else{
   PrimeFaces.current().ajax().update("selAddrFrm:addrSelTbl");
  }
  
  
 }
 
 public void onAddrSrchRowSel(SelectEvent evt){
  LOGGER.log(INFO, "onAddrSrchRowSel called with {0}", evt.getObject());
  LOGGER.log(INFO,"current step {0}",apAcntCrStep);
  LOGGER.log(INFO," dialogId {0}",dialogId);
  
  
  if(evt.getObject() != null){
   AddressRec selected = (AddressRec)evt.getObject();
   PrimeFaces pf = PrimeFaces.current();
   List<String> updateList = new ArrayList<>();
   if(apAcntCrStep == 1){
    apAccount.setAccountAddress(selected);
    updateList.add("apActCrFrm:acntAddrPg");
    updateList.add("apActCrFrm:acntAddrNumNamePg");
   } else if( apAcntCrStep == 3 && StringUtils.equals(dialogId, "trBnkBrNewDlg")) {
    addrEdit = selected;
    updateList.add("trBnkBrNewFrm:bnkBranchAddrPg"); 
   }else{
     bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankAddress(selected);
    updateList.add("trBnkNewFrm:bnkAddrPg");
   }
   pf.ajax().update(updateList);
   pf.executeScript("PF('selAddrWv').hide()");
   
  // apAccount.getApAccountBanks().get(0).getBank().getBankOrganisation().getName();
   
   
  }
 }
 
 public void onAddrEditDlg(){
  LOGGER.log(INFO, "onAddrEdit called");
  PrimeFaces pf = PrimeFaces.current();
  addrEdit = apAccount.getAccountAddress();
  if(addrEdit == null){
   addrEdit = new AddressRec();
  }
  if(countries == null){
   countries = ptnrMgr.getCountriesAll();
  }
  LOGGER.log(INFO, "ap Acnt Comp addr {0}", apAccount.getCompany().getCountry());
  for(CountryRec c:countries){
   
   if(apAccount.getCompany().getCountry() != null){
   if(Objects.equals(c.getId(), apAccount.getCompany().getCountry().getId())){
    addrEdit.setCountry(c);
    pf.ajax().update("addrEdFrm:addrEdPgId");
    break;
   }
   }else{
    
   }
  }
  
  pf.executeScript("PF('addrEdDlgWv').show();");
 }
 
 public void onApAcntCrBack(){
  LOGGER.log(INFO, "onApAcntCrNext called");
  apAcntCrStep--;
  PrimeFaces.current().ajax().update("apActCrFrm");
 }
 public void onApAcntCrNext(){
  LOGGER.log(INFO, "onApAcntCrNext called");
  if(apAcntCrStep == 0 && apAcntCrStepCompl){
   apAcntCrStep = 4;
  }else{
   apAcntCrStep++;
  }
  
  LOGGER.log(INFO, "apAcntCrStep {0}",apAcntCrStep);
  List<String> updtList = new ArrayList<>();
  
  updtList.add("apActCrFrm");
  PrimeFaces.current().ajax().update(updtList);
  
 }
 
 public void onApAcntAddrCopyPtnr(){
  LOGGER.log(INFO, "onApAcntCrNext called");
 }
 
 public void onApAcntAddrFind(){
  LOGGER.log(INFO, "onApAcntAddrFind called");
  acntAddrNew = false;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("selAddrFrm");
  pf.executeScript("PF('selAddrWv').show();");
 }
 
 public void onApAcntAddrNew(){
  LOGGER.log(INFO, "onApAcntAddrNew called");
  acntAddrNew = true;
  List<String> updateLst = new ArrayList<>(); 
  updateLst.add("apActCrFrm:acntAddrNumNamePg");
  updateLst.add("apActCrFrm:acntAddrPg");
  PrimeFaces.current().ajax().update(updateLst);
 }
 public String onApAcntCrStepCh(FlowEvent evt){
  LOGGER.log(INFO, "onApAcntCrStepCh called");
  LOGGER.log(INFO, "Current step {0} next step {1}", 
    new Object[]{evt.getOldStep(), evt.getNewStep()});
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  LOGGER.log(INFO, "selectedPtnrTypeCd {0}",selectedPtnrTypeCd);
  if(this.isApAcntCrStepCompl()){
   nextStep = "summTab";
   apAcntCrStepCompl = false;
   if(apAccount.getReconciliationAc() == null){
    apAccount.setReconciliationAc(this.getGlReconciliationAcs().get(0));
   }
   if(apAccount.getSortOrder() == null){
    apAccount.setSortOrder(this.getSortOrderList().get(0));
   }
   if(apAccount.getPaymentTerms() == null){
    apAccount.setPaymentTerms(this.getPayTermsList().get(0));
   }
   if(apAccount.getPaymentType() == null){
    apAccount.setPaymentType(this.getPayTypes().get(0));
   }
   return nextStep;
  }
  
  return nextStep;
 }
 
 public List<ApAccountRec> onApAcntByCodeComplete(String input){
  LOGGER.log(INFO, "onApAcntByCodeComplete called with {0}", input);
  List<ApAccountRec> retList;
  if(StringUtils.isBlank(input)){
   retList = this.apManager.getApAccountsAll(comp);
  }else{
   retList = apManager.getApAccountsStartinfWithCode(comp, input);
  }
  
  return retList;
 }
 public List<BankAccountRec> onBankAccountComplete(String input){
  LOGGER.log(INFO, "onBankAccountComplete called with {0}", input);
  
  BankBranchRec br = null;
  switch(this.getViewSimple()){
   case "apActCr":
    br = this.bankAcApNew.getBankAccount().getAccountForBranch();
    
    break;
   default:
    LOGGER.log(INFO, "Default page");
  }
  if(StringUtils.isBlank(input)){
   return bankMgr.getBankAccountsForBranch(br);
  }else{
   return bankMgr.getBankAccountsForBranchAcntNum(br, input);
  }

 }
 
 public void onBankBranchNewBtn(){
  if(bankAcApNew.getBankAccount().getAccountForBranch() == null){
   bankAcApNew.getBankAccount().setAccountForBranch(new BankBranchRec());
  }
  BankBranchRec br = bankAcApNew.getBankAccount().getAccountForBranch();
  if(br.getBranchAddress() == null){
   br.setBranchAddress(new AddressRec());
  }
  if(br.getBranchAddress().getCountry() == null){
   br.getBranchAddress().setCountry(apAccount.getCompany().getCountry());
  }
  bankAcApNew.getBankAccount().setAccountForBranch(br);
  PrimeFaces pf = PrimeFaces.current();
  pf.executeScript("PF('trBnkBrNewDlg').show();");
  dialogId = "trBnkBrNewDlg";
  addrEdit = br.getBranchAddress();
  LOGGER.log(INFO, "branch address country {0}", addrEdit.getCountry());
 }
 
 
 public void onBankAcntDF(){
  Map<String,Object> options = new HashMap<>();
  options.put("modal", true);
  options.put("width", 640);
  options.put("height", 340);
  options.put("contentWidth", "100%");
  options.put("contentHeight", "100%");
  options.put("headerElement", "apBnkAcntCrhdr");
  
  //PrimeFaces.current().dialog("apBnkBrCrDlg",options,null);
  
 }
 
 public void onBankAccountSelect(SelectEvent evt){
  BankAccountRec s = (BankAccountRec)evt.getObject();
  PrimeFaces pf = PrimeFaces.current();
  if(getViewSimple().equals("apActCr") 
    && StringUtils.isBlank(bankAcApNew.getAccountName())){
   bankAcApNew.setAccountName(s.getAccountName());
   pf.ajax().update("newBankActApFrm:newBnkAcntNm");
  }
  
 }
 
 public void onBankAccountSubLedTrf(){
  PrimeFaces pf = PrimeFaces.current();
  List<ArBankAccountRec> arBankActs = this.apAccount.getApAccountBanks();
  LOGGER.log(INFO, "Pre-transfer banks {0}", arBankActs);
  if(arBankActs == null){
   arBankActs = new ArrayList<>();
  }
  
  if(this.getViewSimple().equals("apActCr")){
   
   
   UUID uuid = UUID.randomUUID();
   long id = uuid.getLeastSignificantBits();
   if(id < 0){
    id *= -1;
   }
   bankAcApNew.setId(id);
   arBankActs.add(bankAcApNew);
   apAccount.setApAccountBanks(arBankActs);
   pf.ajax().update("apActCrFrm:bnkTbl");
   pf.executeScript("PF('addBnkAcntApWv').hide()");
  }
  
  LOGGER.log(INFO, "Pre-transfer banks {0}", arBankActs);
     
 }
 
 public void onBankAddressSelect(SelectEvent evt){
  LOGGER.log(INFO, "onBankAddressSelect called with {0}", evt.getObject());
  AddressRec selAddr = (AddressRec)evt.getObject();
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankAddress(selAddr);
  bnkAcntAddrEdit = false;
  
 }
 
 
 public void onBankAddressToggle(ToggleEvent evt){
  LOGGER.log(INFO, "onBankAddressToggle evt called with visibility {0}", evt.getVisibility());
  if(evt.getVisibility() == Visibility.VISIBLE){
   LOGGER.log(INFO, "Bank Address {0}", bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankAddress().getCountry());
   if(bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankAddress().getCountry() == null){
    AddressRec currAddr = new AddressRec();
    currAddr.setCountry(apAccount.getCompany().getCountry());
    bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankAddress(currAddr);
    
   }
   
   PrimeFaces.current().ajax().update("trBnkNewFrm:bnkAddrPg");
  }
 }
 
 public void onBankAddressCbCh(ValueChangeEvent evt){
  LOGGER.log(INFO, "onBankAddressCbCh called with value {0}", evt.getNewValue());
 }
 
 public List<BankBranchRec> onBankBranchComplete(String input){
  LOGGER.log(INFO, "onBankBranchComplete called with {0}", input);
  if(StringUtils.isBlank(input)){
   return this.bankMgr.getBranchesAll();
  }else{
   return bankMgr.getBanchesBranchBySortCode(input);
  }
 }
 
 public void onBankCodeValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO, "onBankCodeValidate called with {0}", val);
  
  boolean bankExists = bankMgr.bankExists((String)val);
  
  if(bankExists){
   ((EditableValueHolder) toValidate).setValid(false);
   MessageUtil.addClientErrorMessage("trBnkNewFrm:errMsg", "bnkDupl", "validationText");
   PrimeFaces.current().ajax().update("trBnkNewFrm:errMsg");
  }else{
   ((EditableValueHolder) toValidate).setValid(true);
  }
 }
 public List<BankRec> onBankComplete(String input){
  List<BankRec> bnkList;
  if(StringUtils.isBlank(input)){
   bnkList = bankMgr.getBanks();
 }else{
   bnkList = bankMgr.getBanksByCode(input);
  }
  Collections.sort(bnkList, new BankByBankCode());
  return bnkList;
 }
 
 public List<PartnerCorporateRec> onBankOrgComplete(String input){
  PartnerRoleRec rl = this.buff.getPartnerRoleByCode("BNK");
  List<PartnerCorporateRec> corpList = new ArrayList<>();
  List<PartnerBaseRec> ptnrs;
  if(StringUtils.isBlank(input)){
    ptnrs = ptnrMgr.getPartnerByRole(rl);
   if(ptnrs == null){
    return null;
   }
   
  }else{
   ptnrs = ptnrMgr.getPartnerByRoleAndTradingName(rl,input);
   
  }
  for (PartnerBaseRec pb : ptnrs) {
   if(pb.getClass().getSimpleName().equals("PartnerCorporateRec")){
    corpList.add((PartnerCorporateRec)pb);
   }
  }
  Collections.sort(corpList, new PartnerByTradingName());
   return corpList;
 }
 public void onBankOrgNewBtn(){
  LOGGER.log(INFO, "Called onBankOrgNewBtn");
  bankAcApNew.getBankAccount().getAccountForBranch().setBank(new BankRec());
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankAddress(new AddressRec());
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankOrganisation(new PartnerCorporateRec());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("trBnkNewFrm");
  pf.executeScript("PF('trBnkNewDlg').show()");
  
 }
 
 
 public void onBnkOrgCrBtn(){
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankOrganisation(new PartnerCorporateRec());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("trNewNewBnkOrgFrm");
  pf.executeScript("PF('newNewBnkOrgWv').show()");
 }
 
 public void onBankOrgPtnrTransf(){
  LOGGER.log(INFO, "onBankOrgPtnrTransf");
  bnkNewOrg = bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankOrganisation();
  LOGGER.log(INFO, "bnk Org Ptnr trading {0}", bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankOrganisation().getTradingName());
  LOGGER.log(INFO, "bnkNewOrg {0}", bnkNewOrg.getTradingName());
  bnkNewOrg.setCreatedBy(getLoggedInUser());
  bnkNewOrg.setCreatedDate(new Date());
  LOGGER.log(INFO, "bnkNewOrg created by {0}", bnkNewOrg.getCreatedBy());
  
  bnkNewOrg = ptnrMgr.createCorporatePartnerBank(bnkNewOrg, getLoggedInUser(), getView());
  LOGGER.log(INFO, "bnkNewOrg {0}",bnkNewOrg);
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankOrganisation(bnkNewOrg);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("trBnkNewFrm:trBnkOrg");
  pf.executeScript("PF('newBnkOrgWv').hide()");
  
  
 }
 
 public void onBankAddrTransf(){
  LOGGER.log(INFO, "onBankAddrTransf called");
  LOGGER.log(INFO, "Post code {0}", bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankAddress().getPostCode());
  LOGGER.log(INFO, "Street {0}", bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankAddress().getStreet());
  boolean newAddr = this.ptnrMgr.checkAddressUnique(bankAcApNew.getBankAccount().getAccountForBranch().getBank().getBankAddress());
  PrimeFaces pf = PrimeFaces.current();
  if(!newAddr){
    MessageUtil.addClientWarnMessage("newBnkAddr:errMsg", "addrDupl", "validationText");
    pf.ajax().update("newBnkAddr:errMsg");
  }else{
   pf.ajax().update("trBnkNewFrm:bnkAddrPg");
   pf.executeScript("PF('trBnkNewDlgWv').hide();");
  }
 }
 public void onBankOrgTransfer(){
  LOGGER.log(INFO, "onBankOrgTransfer called");
  BankRec bnk = bankAcApNew.getBankAccount().getAccountForBranch().getBank();
  
  
  UserRec crUsr = getLoggedInUser();
  bnk.setCreatedBy(crUsr);
  bnk.setCreatedOn(new Date());
  bnk.getBankOrganisation().setChangedBy(crUsr);
  bnk.getBankOrganisation().setChangedOn(new Date());
  LOGGER.log(INFO, "bnk org  {0}", bnk.getCreatedBy());
  
  if(bnk.getBankAddress() != null){
   if(bnk.getBankAddress().getId() == null){
    bnk.setCreatedBy(crUsr);
    bnk.setCreatedOn(new Date());
    AddressRec currAddr = ptnrMgr.addressUpdate(bnk.getBankAddress(), crUsr, getView());
    bnk.setBankAddress(currAddr);
   }
  }
  bnk = bankMgr.createBank(bnk, crUsr, getView());
  LOGGER.log(INFO, "bnk id after create {0}", bnk.getId());
  PrimeFaces pf = PrimeFaces.current();
  if(bnk.getId() == null){
   MessageUtil.addClientErrorMessage("trBnkNewFrm:errMsg", "bnkBankCr", "errorText"); 
   pf.ajax().update("trBnkNewFrm:errMsg");
   
  }else{
   bankAcApNew.getBankAccount().getAccountForBranch().setBank(bnk);
   
   pf.executeScript("PF('trBnkNewDlg').hide()");
   pf.ajax().update("trBnkBrNewFrm:trBnkBrBank");
  }
  
  
 }
  public void onCompanySelect(SelectEvent evt){
   LOGGER.log(INFO, "onCompanySelect called with {0}", evt.getObject());
   apAccount.setCompany((CompanyBasicRec)evt.getObject());
   PrimeFaces.current().ajax().update("apActCrFrm:compName");
  }
 
 public List<CountryRec> onCountryComplete(String input){
  LOGGER.log(INFO, "onCountryComplete called with {0}", input);
  if(StringUtils.isBlank(input)){
  return countries;
  }else{
   List<CountryRec> retList = new ArrayList<>();
   for(CountryRec c: countries){
    if(StringUtils.contains(c.getCountryName(), input)){
     retList.add(c);
    }
   }
   return retList;
  }
 }
 
 public void onCreatePartnerBtn(){
  LOGGER.log(INFO, "onCreatePartnerBtn called partner type {0}",selectedPtnrTypeCd);
  
  switch(selectedPtnrTypeCd){
   case "corp":
    if(ptnrMgr.isPartnerRefUnique(ptnrCorp.getRef())){
     onCreateCorpPartnerBtn();
    }else{
     LOGGER.log(INFO, "Account ref already used");
     MessageUtil.addClientWarnMessage("newPtnrFrm:errMsg", "ptnrRefUniq", "validationText");
     PrimeFaces.current().ajax().update("newPtnrFrm:errMsg");
    }
    break;
   case "indiv":
    if(ptnrMgr.isPartnerRefUnique(ptnrPerson.getRef())){
     onCreatePartnerBtn();
    }else{
     LOGGER.log(INFO, "Account ref already used");
     MessageUtil.addClientWarnMessage("newPtnrFrm:errMsg", "ptnrRefUniq", "validationText");
     PrimeFaces.current().ajax().update("newPtnrFrm:errMsg");
    }
    
  }
 }
 public void onCreateCorpPartnerBtn(){
    LOGGER.log(INFO, "createCorpPartnerBtnListner called");
    boolean error = false;
    LOGGER.log(INFO, "ptnrCorp ref {0} legal name {1} trading name {2}", new Object[]{ptnrCorp.getRef(),
     ptnrCorp.getLegalName(), ptnrCorp.getTradingName()   });
    if(StringUtils.isBlank(ptnrCorp.getRef()) ){
     MessageUtil.addClientErrorMessage("newPtnrFrm:errMsg", "ptnrRef", "validationText");
     error = true;
    }
    if(StringUtils.isBlank(ptnrCorp.getLegalName()) ){
     MessageUtil.addClientErrorMessage("newPtnrFrm:errMsg", "ptnrLegalname", "validationText");
     error = true;
    }
    if(StringUtils.isBlank(ptnrCorp.getTradingName())){
     MessageUtil.addClientErrorMessage("newPtnrFrm:errMsg", "ptnrTradingName", "validationText");
     error = true;
    }
    if(error){
     //ptnrCorp = null;
     PrimeFaces.current().ajax().update("newPtnrFrm:errMsg");
     return;
    }
    try{
     ptnrCorp.setCreatedBy(this.getLoggedInUser());
     ptnrCorp.setCreatedDate(new Date());
     ptnrCorp = (PartnerCorporateRec)ptnrMgr.updatePartner(ptnrCorp, getView());
     LOGGER.log(INFO, "ptnrCorp id after update {0}", ptnrCorp.getId());
     //ptnrCorp = ptnrMgr.createCorporatePtnrSubLed(ptnrCorp, this.getLoggedInUser(), this.getView());
     apAccount.setApAccountFor(ptnrCorp);
     MessageUtil.addClientInfoMessage("newPtnrFrm:grl", "ptnrIndivCr", "blacResponse");
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("apActCrFrm");
     
     pf.executeScript("PF('crPtnrDlg').hide();");
    
    }catch(BacException ex){
     GenUtil.addErrorMessage(errorForKey("PTNR_EXISTS"));
    }

  }
 
 public void onDeleteNewApBank(){
  LOGGER.log(INFO, "onDeleteNewApBank called");
  ListIterator<ArBankAccountRec> li = apAccount.getApAccountBanks().listIterator();
  boolean found = false;
  while(li.hasNext()&& !found){
   ArBankAccountRec currBnk = li.next();
   if(Objects.equals(this.bankAcApSelected.getId(), currBnk.getId())){
    found = true;
    li.remove();
   }
  }
  if(found){
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("apActCrFrm:bnkTbl");
  }
 }
 
 public void onDdUpload(FileUploadEvent evt){
  
  UploadedFile dd = evt.getFile();
  LOGGER.log(INFO, "onDdUpload called with file type {0}", dd.getContentType());
  bankAcApNew.setDdFileType(dd.getContentType());
  bankAcApNew.setSignedDD(dd.getContents());
  bankAcApNew.setDdFileName(dd.getFileName());
  PrimeFaces.current().ajax().update("newBankActApFrm:ddFilenamePg");
 }
 
 public void onNewAcntAddrTrf(){
  LOGGER.log(INFO, "onNewAcntAddrTrf called with {0}", addrEdit);
  
  if(addrEdit != null){
  addrEdit = ptnrMgr.addressUpdate(addrEdit, getLoggedInUser(), getView());
  apAccount.setAccountAddress(addrEdit);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addrEdFrm:addrEdPgId");
  pf.executeScript("PF('addrEdDlgWv').hide();");
  }
 }
 
 public void onBacsCollectAuthChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "Collection authorisation {0}", evt.getNewValue());
  Boolean coll = (Boolean)evt.getNewValue();
  if(coll){
   if(!bankAcApNew.getBankAccount().isValidated()){
    MessageUtil.addWarnMessage("bnkAcntValid", "validationText");
   }else{
    if(!bankAcApNew.getBankAccount().isDirectDebitsAllowed()){
      MessageUtil.addWarnMessage("bnkAcntCollNone", "validationText");
    }
   }
  
  }
 }
   
 public void onBankAccountAutoCrDlg(){
  bankAcApNew.setBankAccount(new BankAccountRec());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("trBnkAcntCrWs:bnkAcntCrWs");
  pf.executeScript("PF('trBnkAcntWsDlgWv').show();");
  
 }
 public void onNewAcntCrPtnrDlg(){
  LOGGER.log(INFO, "onNewAcntCrPtnrDlg");
  if(selectedPtnrTypeCd.equals("corp")){
   ptnrCorp = new PartnerCorporateRec(); 
  }else{
   ptnrPerson = new PartnerPersonRec();
  }
  
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("newPtnrFrm");
  pf.executeScript("PF('crPtnrDlg').show();");
 }
 
 public void onNewBankAcntArDlg(){
  LOGGER.log(INFO, "onNewBankAcntDlg called");
  bankAcApNew = new ArBankAccountRec();
  bankAcApNew.setBank(new BankRec());
  bankAcApNew.setBankAccount(new BankAccountRec());
  LOGGER.log(INFO, "viewSimple {0}",this.getViewSimple());
  
  
  if(banks == null || banks.isEmpty()){
   banks = this.bankMgr.getBanks();
  }
  
  if(this.bankNoSelect == null){
   bankNoSelect = new BankRec();
   PartnerCorporateRec corp = new PartnerCorporateRec();
   String name = this.formTextForKey("select");
   corp.setTradingName(name);
   bankNoSelect.setBankOrganisation(corp);
  }
  
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("newBankActApFrm");
  pf.executeScript("PF('addBnkAcntApWv').show()");
  
  
 }
 
 public void onNewBankAcntArSelBr(SelectEvent evt){
  LOGGER.log(INFO, "onNewBankAcntArSelBr called with {0}", evt.getObject());
  if(evt.getObject() == null){
   return;
  }
  bankAcApNew.getBankAccount().setAccountForBranch((BankBranchRec)evt.getObject());
  bankAccounts = ((BankBranchRec)evt.getObject()).getAccounts();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("newBankActApFrm:addApBankAcBnkAc");
  
  
 }
 
 public void onNewBankAcntTrTransfer(){
  LOGGER.log(INFO, "onNewBankAcntTrTransfer called with {0} ", bankAcApNew.getBankAccount());
  LOGGER.log(INFO,"bank Branch {0}",bankAcApNew.getBankAccount().getAccountForBranch());
  PrimeFaces pf = PrimeFaces.current();
  // attempt to create in DB
  BankAccountRec bnkAcnt = bankAcApNew.getBankAccount();
  if(bnkAcnt.getId() == null){
   bnkAcnt.setCreatedBy(this.getLoggedInUser());
   bnkAcnt.setCreatedOn(new Date());
  }else{
   bnkAcnt.setUpdatedBy(this.getLoggedInUser());
   bnkAcnt.setUpdatedOn(new Date());
  }
  bnkAcnt = this.bankMgr.bankAccountUpdate(bnkAcnt, getLoggedInUser(), this.getView(), false);
  if(bnkAcnt.getId() == null){
   MessageUtil.addErrorMessage("bnkActCr", "errorText");
   return;
  }else{
   bankAcApNew.setBankAccount(bnkAcnt);
  }
  List<String> updates = new ArrayList<>();
  updates.add("newBankActApFrm:addApBankAcBranch");
  updates.add("newBankActApFrm:addApBankAcBnkAc");
  
  pf.ajax().update(updates);
  pf.executeScript("PF('trBnkAcntDlgWv').hide()");
 }
 
 public void onNewBankAcntValidTrf(){
  LOGGER.log(INFO, "onNewBankAcntValidTrf called");
  PrimeFaces pf = PrimeFaces.current();
  BankAccountRec bnkAcnt;
  
  BankValidation bankValWeb = new BankValidation();
  try{
   bnkAcnt = bankValWeb.validateBank(bnkSortWs, bnkAcntNumWs);
   UserRec currUsr = this.getLoggedInUser();
   Date currDate = new Date();
   if(bnkAcnt.getId() == null){
    bnkAcnt.setCreatedBy(currUsr);
    bnkAcnt.setCreatedOn(currDate);
   }else{
    bnkAcnt.setUpdatedBy(currUsr);
    bnkAcnt.setUpdatedOn(currDate);
   }
   
   if(bnkAcnt.getAccountForBranch().getId() == null){
    bnkAcnt.getAccountForBranch().setCreatedBy(currUsr);
    bnkAcnt.getAccountForBranch().setCreatedOn(currDate);
   }else{
    bnkAcnt.getAccountForBranch().setUpdatedBy(currUsr);
    bnkAcnt.getAccountForBranch().setUpdatedOn(currDate);
   }
   
   if(bnkAcnt.getAccountForBranch().getBranchAddress() != null){
    if(bnkAcnt.getAccountForBranch().getBranchAddress().getId() == null){
     bnkAcnt.getAccountForBranch().getBranchAddress().setCreatedBy(currUsr);
     bnkAcnt.getAccountForBranch().getBranchAddress().setCreatedOn(currDate);
    }else{
     bnkAcnt.getAccountForBranch().getBranchAddress().setChangedBy(currUsr);
     bnkAcnt.getAccountForBranch().getBranchAddress().setChangedOn(currDate);
    }
   }
   
   if(bnkAcnt.getAccountForBranch().getBank().getId() == null){
    bnkAcnt.getAccountForBranch().getBank().setCreatedBy(currUsr);
    bnkAcnt.getAccountForBranch().getBank().setCreatedOn(currDate);
   }else{
    bnkAcnt.getAccountForBranch().getBank().setUpdatedBy(currUsr);
    bnkAcnt.getAccountForBranch().getBank().setUpdatedOn(currDate);
   }
   
   if(bnkAcnt.getAccountForBranch().getBank().getBankOrganisation().getId() == null){
    bnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setCreatedBy(currUsr);
    bnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setCreatedDate(currDate);
   }else{
    bnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setChangedBy(currUsr);
    bnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setChangedOn(currDate);
   }
   
   //bnkAcnt = this.bankMgr.bankAccountUpdate(bnkAcnt, currUsr, getView(), true);
   bankAcApNew.setBankAccount(bnkAcnt);
   pf.ajax().update("newBankActApFrm:newApbankAcntPg");
   pf.executeScript("PF('trBnkAcntWsDlgWv').hide()");
  }catch(BacException ex){
   LOGGER.log(INFO, "Bank account Validation failed :{0} ",ex.getLocalizedMessage());
   if(StringUtils.startsWith(ex.getLocalizedMessage(), "INVALID - Modulus Check Failed")){
    MessageUtil.addErrorMessage("bvModulus", "validationText");
   }else if(StringUtils.startsWith(ex.getLocalizedMessage(), "INVALID - Account")){
    MessageUtil.addErrorMessage("bvAcntNum", "validationText");
   }else if (StringUtils.startsWith(ex.getLocalizedMessage(), "INVALID Sortcode")){
    MessageUtil.addErrorMessage("bvSortCode", "validationText");
   }else if (StringUtils.startsWith(ex.getLocalizedMessage(), "ERROR - Invalid User ID/PIN")){
    MessageUtil.addErrorMessage("bvlogin", "validationText");
   }else if (StringUtils.startsWith(ex.getLocalizedMessage(), "ERROR - User ID Expired")){
    MessageUtil.addErrorMessage("bvId", "validationText");
   }
   pf.ajax().update("trBnkAcntCrWs:bnkValMsg");
  }
 }
 public void onNewBankBranchAddrToggle(ToggleEvent evt){
  
  Visibility visibility = evt.getVisibility();
  LOGGER.log(INFO, "onNewBankBranchAddrToggle visible {0}", visibility);
  if(visibility == Visibility.VISIBLE && addrEdit == null){
   addrEdit =  new AddressRec();
  }
  LOGGER.log(INFO, "Branch address {0}", addrEdit); 
  
 }
 
 public void onNewBankBranchSortCdValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO, "onNewBankBranchSortCdValidate called with {0}", val);
  String sortCd = String.valueOf(val);
  BankBranchRec br = this.bankMgr.getBankBranchBySortCode(sortCd);
  LOGGER.log(INFO, "br found for sort code {0}", br);
  if(br ==  null){
   ((EditableValueHolder) toValidate).setValid(true);
  }else{
   ((EditableValueHolder) toValidate).setValid(false);
   MessageUtil.addClientWarnMessage("trBnkBrNewFrm:msgs", "bnkBrDupl", "validationText");
  }
  List<String> updates = new ArrayList<>();
  updates.add("trBnkBrNewFrm:msgs");
  updates.add("trBnkBrNewFrm:trBnkBrSort");
  PrimeFaces.current().ajax().update(updates);
 }
 public void onNewBankBranchTransfer(){
  LOGGER.log(INFO, bnkSortWs, ptnrMgr);
  PrimeFaces pf = PrimeFaces.current();
  LOGGER.log(INFO, "Transfer Branch address {0}", addrEdit);
  
  if (addrEdit != null){
   boolean errFnd = false;
   if(StringUtils.isNoneBlank(addrEdit.getHouseNumber()) ||
     StringUtils.isNoneBlank(addrEdit.getHouseName() )||
     StringUtils.isNoneBlank(addrEdit.getStreet() )||
     StringUtils.isNoneBlank(addrEdit.getStreet2() )||
     StringUtils.isNoneBlank(addrEdit.getTown()) ||
     StringUtils.isNoneBlank(addrEdit.getCountyName())||
     StringUtils.isNoneBlank(addrEdit.getPostCode() )){
    //check the street and post pode populate
    if(StringUtils.isBlank(addrEdit.getPostCode())){
     MessageUtil.addErrorMessage("addrDefPostCode", "validationText");
     errFnd = true;
    }
    if(StringUtils.isBlank(addrEdit.getStreet())){
     MessageUtil.addErrorMessage("addrDefStreet", "validationText");
     errFnd = true;
    }
    if(errFnd){
     return ;
    }
   }
  }
  bankAcApNew.getBankAccount().getAccountForBranch().setBranchAddress(addrEdit);
  BankBranchRec br = bankAcApNew.getBankAccount().getAccountForBranch();
  LOGGER.log(INFO, "br {0}", br);
  LOGGER.log(INFO, "branch for bank {0}", br.getBank());
  UserRec currUsr = this.getLoggedInUser();
  Date currDate = new Date();
  if(br.getBranchAddress() != null){
   if(br.getId() == null){
    br.getBranchAddress().setCreatedBy(currUsr);
    br.getBranchAddress().setCreatedOn(currDate);
   }else{
    br.getBranchAddress().setChangedBy(currUsr);
    br.getBranchAddress().setChangedOn(currDate);
   }
  }
  if(br.getId() == null){
   br.setCreatedBy(currUsr);
   br.setCreatedOn(currDate);
  }else{
   br.setUpdatedBy(currUsr);
   br.setUpdatedOn(currDate);
  }
  br = this.bankMgr.updateBankBranch(br, getView());
  if(br.getId() == null){
   // Branch not ceated
   MessageUtil.addErrorMessage("bnkBankBrCr", "errorText");
  }else{
   pf.ajax().update("trBnkAcntFrm:trBnkSortCd");
   pf.executeScript("PF('trBnkBrNewDlg').hide()");
  }
 }
 
 public void onNewBankOrgSel(SelectEvent evt){
  LOGGER.log(INFO,"onNewBankOrgSel called with {0}",evt.getObject());
  bankAcApNew.getBankAccount().getAccountForBranch().getBank().setBankOrganisation((PartnerCorporateRec)evt.getObject());
  
 }
 
 public void onPartnerBylegalValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO, "partnerBylegalNameCheck called with legal name {0}",val);
  
  String legName = StringUtils.replace((String)val, " ", "%");
  if(!legName.endsWith("%")){
   legName += "%";
  }
  List<PartnerCorporateRec> ptnrs = this.ptnrMgr.getPartnersByNameLegal(legName);
  if(ptnrs == null){
   ((EditableValueHolder) toValidate).setValid(true);
   return;
  }
  ((EditableValueHolder) toValidate).setValid(false);
  MessageUtil.addClientWarnMessage("trNewBnkOrgFrm:errMsg", "ptnrLegalNameDupl", "validationText");
 
  PrimeFaces.current().ajax().update("trNewBnkOrgFrm:errMsg");
  
  
 }
 public List<PartnerBaseRec> onPartnerComplete(String input){
  LOGGER.log(INFO, "onPartnerComplete called with {0}", input);
  List<PartnerBaseRec> ptnrList;
  if(StringUtils.isBlank(input)){
   ptnrList = ptnrMgr.getPartnersAll();
  }else{
   ptnrList = ptnrMgr.getPartnersByName(input);
  }
   
  if(ptnrList != null){
   Collections.sort(ptnrList, new PartnerBaseByTradeName());
  }
  
   return ptnrList;
  
 }
 public List<PartnerPersonRec> onPartnerPersonComplete(String input){
  LOGGER.log(INFO, "onPartnerPersonComplete called with {0}", input);
  
  if(StringUtils.isBlank(input)){
   return ptnrMgr.getAllPartnerIndividual();
  }else{
   List<PartnerPersonRec> retList = ptnrMgr.getIndivPtnrsBySurname(input);
   LOGGER.log(INFO, "partner list {0}", retList);
   return retList;
  }
 }
 
 public void onPartnerSelect(SelectEvent evt){
  ptnrBase = (PartnerBaseRec)evt.getObject();
  ptnrSelected = true;
  if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerCorporateRec")){
   selectedPtnrTypeCd = "corp";
   ptnrCorp = (PartnerCorporateRec)ptnrBase;
  }else{
   selectedPtnrTypeCd = "indiv";
   ptnrPerson = (PartnerPersonRec)ptnrBase;
  }
  LOGGER.log(INFO, "End partner select selected {0}type code {1} ", new Object[]{ptnrSelected,selectedPtnrTypeCd});
 }
 
 
 public void onPartnerTypeValueChange(ValueChangeEvent evt){
   selectedPtnrTypeCd = (String)evt.getNewValue();
  }
   
 public List<AddressRec> onPostCodeComplete(String entry){
   LOGGER.log(INFO, "postCodeComplete called with {0}", entry);
   List<AddressRec> addresses ;
   if(entry == null || entry.isEmpty()){
    addresses = ptnrMgr.getAllAddresses();
    }else{
    addresses = ptnrMgr.getAddressesForPostCodePart(entry);
   }
   LOGGER.log(INFO, "postCodeComplete returns {0}", addresses);
   return addresses;
   
  }
 
 public void onPostCodeSelect(SelectEvent evt){
   LOGGER.log(INFO, "onPostCodeSelect called with value {0}", evt.getObject());
   apAccount.setAccountAddress((AddressRec)evt.getObject());
  }
 
 /**
  * Process edit partner button click
  */
 public void onPtnrEditBtn(){
  if(StringUtils.equals(ptnrBase.getClass().getSimpleName(),"PartnerCorporateRec")){
   selectedPtnrTypeCd = "corp";
  }else {
   selectedPtnrTypeCd = "indiv";
  }
  
 }
 public void onAddrEditPostCodeSel(SelectEvent evt){
  LOGGER.log(INFO, "onAddrEditPostCodeSel called with {0}", evt.getObject());
  AddressRec selAddr = (AddressRec)evt.getObject();
  this.addrEdit = selAddr;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addrEdFrm:addrEdPgId");
  
 }
 
 public String onSaveApAcntNew(){
  LOGGER.log(INFO, "onSaveApAcntNew called");
  UserRec currUsr = this.getLoggedInUser();
  Date currDate = new Date();
  if(apAccount.getId() == null){
   apAccount.setCreatedBy(currUsr);
   apAccount.setCreatedOn(currDate);
  }else{
   apAccount.setChangedBy(currUsr);
   apAccount.setChangedOn(currDate);
  }
  
  if(apAccount.getAccountAddress() != null){
   if(apAccount.getAccountAddress().getId() == null){
    apAccount.getAccountAddress().setCreatedBy(currUsr);
    apAccount.getAccountAddress().setCreatedOn(currDate);
   }else{
    apAccount.getAccountAddress().setChangedBy(currUsr);
    apAccount.getAccountAddress().setChangedOn(currDate);
   }
  }
  
  if(apAccount.getApAccountFor() != null){
   if(apAccount.getApAccountFor().getId() == null){
    apAccount.getApAccountFor().setCreatedBy(currUsr);
    apAccount.getApAccountFor().setCreatedDate(currDate);
   }else{
    apAccount.getApAccountFor().setChangedBy(currUsr);
    apAccount.getApAccountFor().setChangedOn(currDate);
   }
  } 
  if(apAccount.getApAccountBanks() != null){
   ListIterator<ArBankAccountRec> li = apAccount.getApAccountBanks().listIterator();
   while(li.hasNext()){
    ArBankAccountRec currBnk = li.next();
    if(currBnk.getId() == null){
     currBnk.setCreatedBy(currUsr);
     currBnk.setCreatedOn(currDate);
    }else{
     currBnk.setChangedBy(currUsr);
     currBnk.setChangedOn(currDate);
    }
    li.set(currBnk);
   }
  }
  
  if(this.isApAcntCrStepCompl()){
   // need to set all the defeaults for the skipped screens
   LOGGER.log(INFO, "set defaults required");
   if(apAccount.getReconciliationAc() == null){
    apAccount.setReconciliationAc(this.getGlReconciliationAcs().get(0));
   }
   LOGGER.log(INFO, "glREconciliation Ac {0}", apAccount.getReconciliationAc().getCoaAccount().getRef());
   if(apAccount.getSortOrder() ==  null){
    apAccount.setSortOrder(this.getSortOrderList().get(0));
   }
   LOGGER.log(INFO, "sort order {0}", apAccount.getSortOrder());
   if(apAccount.getPaymentTerms() == null){
    apAccount.setPaymentTerms(this.getPayTermsList().get(0));
   }
   LOGGER.log(INFO,"Payment terms {0}",apAccount.getPaymentTerms());
   if(apAccount.getPaymentType() == null){
    apAccount.setPaymentType(this.getPayTypes().get(0));
   }
   LOGGER.log(INFO, "Payment type {0}", apAccount.getPaymentType());
  }
  
  try{
   apAccount = apManager.updateApAccount(apAccount, getViewSimple());
   MessageUtil.addInfoMessage("apAcntCr", "blacResponse");
   apAccount = null;
   this.init();
   this.apAcntCrStepCompl = false;
   this.apAcntCrStep = 0;
   PrimeFaces.current().ajax().update("apActCrFrm");
   return null;
  }catch(Exception ex){
   MessageUtil.addErrorMessage("apActCreate", "errorText");
   return null; 
  }
  
 }
 public void onTradingNameSelect(SelectEvent evt){
    LOGGER.log(INFO, "onTradingNameSelect called with {0}", evt.getObject());
    List<String> updates = new ArrayList<>();
    apAccount.setApAccountFor((PartnerBaseRec)evt.getObject());
    if(StringUtils.isBlank(apAccount.getAccountName())){
     apAccount.setAccountName(apAccount.getApAccountFor().getName());
     updates.add("apActCrFrm:apAcntName");
    }
    LOGGER.log(INFO, "apAcntName {0} vendor name {1}", new Object[]{
     apAccount.getAccountName(),apAccount.getApAccountFor().getName(),
    });
    
    AddressRec ptrnrDefaultAddr = apAccount.getApAccountFor().getDefaultAddress();
    
     if( ptrnrDefaultAddr != null && ptrnrDefaultAddr.getId() != null && ptrnrDefaultAddr.getId() != 0){
      LOGGER.log(INFO, "Use partner default Address");
      apAccount.setAccountAddress(ptrnrDefaultAddr);
     }else{
      MessageUtil.addClientWarnMessage("apActCrFrm:grl", "apVenNoAddress", "formTextAp");
      PrimeFaces.current().ajax().update("apActCrFrm:grl");
      LOGGER.log(INFO, "Warn grl far addr {0}",ptrnrDefaultAddr);
     }
    
    
    LOGGER.log(INFO, "Partner Address {0}", ptrnrDefaultAddr);
    
    
    updates.add("apActCrFrm:addrStNum");
    updates.add("apActCrFrm:addHouseName");
    updates.add("apActCrFrm:addrStreet");
    updates.add("apActCrFrm:addrStr2");
    updates.add("apActCrFrm:addrCounty");
    updates.add("apActCrFrm:addrCountry");
    updates.add("apActCrFrm:addrPostCd");
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update(updates);


  }
}
