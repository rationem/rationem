/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.arap;

//import javax.inject.Named;
//import javax.enterprise.context.ConversationScoped;
import com.rationem.busRec.cm.ContactRec;
import javax.ejb.EJBException;
import com.rationem.busRec.tr.BankRec;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.util.BankValidation;
import com.rationem.busRec.tr.BankBranchRec;
import java.util.ListIterator;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.exception.BacException;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.config.common.SortOrderRec;
import javax.faces.context.FacesContext;
import java.io.IOException;
import org.primefaces.event.ItemSelectEvent;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.ejbBean.common.MasterDataManager;
import java.util.List;
import org.primefaces.event.SelectEvent;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import java.util.ResourceBundle;
import com.rationem.util.GenUtil;
import com.rationem.ejbBean.fi.ArManager;
import org.primefaces.event.FlowEvent;
import com.rationem.util.BaseBean;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.ejbBean.common.ContactManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.helper.PartnerSelectOption;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.comparator.AddressByPostCode;
import com.rationem.util.helper.comparator.ArAccountByRef;
import com.rationem.util.helper.comparator.PartnerBaseByTradeName;
import com.rationem.util.helper.comparator.PartnerPersonByNameStructured;
import com.rationem.util.helper.comparator.PaymentTermsByCode;
import com.rationem.util.helper.comparator.PaymentTypeByCode;
import com.rationem.util.helper.comparator.PaymentTypeByDescr;
import com.rationem.util.helper.comparator.SortOrderByName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import uk.co.unifiedsoftware.bankvaluk.WebMethods;


/**
 *
 * @author Chris
 */
public class ArMastBean extends BaseBean {

   
 private final  WebMethods bv = new WebMethods();
 private static final Logger LOGGER =
            Logger.getLogger(ArMastBean.class.getSimpleName());

 @EJB
 private SysBuffer buff;

 @EJB
 private ArManager arManager;

 @EJB
 private MasterDataManager ptnrMgr;

   @EJB
   private GlAccountManager glAcMgr;

   @EJB
   private BankManager bankMgr;
   
   @EJB
   private ContactManager contMgr;
   
   @EJB
   private MasterDataManager mdmMgr;

 private static final int AR_ACNT_NEW_STEP_MAX = 4;
 private static final int AR_ACNT_NEW_PTNR = 0;
 private static final int AR_ACNT_NEW_ADDR = 1;
 private static final int AR_ACNT_NEW_GL_ACNTS = 2;
 private static final int AR_ACNT_NEW_BNK = 3;
 private static final int AR_ACNT_NEW_SUMM = 4;
 

   private ArAccountRec arAccount;
   private ArAccountRec arAccountEdit;
   private CompanyBasicRec comp;
   private ArrayList<PartnerCorporateRec> corpPtnrs;
   private ArrayList<ArBankAccountRec> arBanks;
   private List<AddressRec> addressList;
   private AddressRec addressSelected;
   private List<ArAccountRec> arAccountList;
   private List<PartnerBaseRec> ptnrBaseList;
   private List<PartnerBaseRec> custSrchList;
   private long selectedCompanyId;
   private String selectedPtnrTypeCd = "corp";
   boolean arAccountEntered = false;
   boolean arAccountSelected = false;
   private AddressRec arAcntAddrEdit;
   private PartnerCorporateRec ptnrCorp;
   private PartnerCorporateRec ptnrCorpEdit;
   private PartnerPersonRec ptnrPerson;
   private PartnerPersonRec ptnrPersonEdit;
   private PartnerPersonRec ptnrPersonNew;
   private PartnerBaseRec ptnrBase;
   private List<PartnerCorporateRec> ptnrCorpList;
   private List<PartnerPersonRec> ptnrPersonList;
   private List<BankAccountRec> bankActList;
   private List<ContactRoleRec> contactRoleList;
   
   
   private List<BankBranchRec> bankBranchList;
   private boolean newPartnerCorp = false;
   private boolean newPartnerIndiv = false;
 
   private ArrayList<SortOrderRec> sortOrderList;
   private SortOrderRec selectedSortOrder;
   private List<FiGlAccountCompRec> glReconciliationAcs;
   private List<PaymentTermsRec> payTermsList;
   private List<PaymentTypeRec> payTypes;
   private List<BankRec> banks;
   private BankRec bankNew;
   private ArBankAccountRec newBank;
   private BankAccountRec newBnkAcnt;
   private BankBranchRec newBankBranch;
   private BankValidation bnkVal;
   private ContactRec contactNew;
   private List<CountryRec> countries;
   
   private ArBankAccountRec selectedBank;
   private PartnerBaseRec selectedCust;
   
   private boolean createBankDisabled = true;
   private boolean createBankBranch = false;
   private boolean createBnkAccount = false;
   private boolean sortCodeEntered = false;
   private boolean bankAccountEntered = false;
   private boolean bankAccountSelected = false;
   private boolean bankAccountValid = false;
   private boolean sortCodeValid = false;
   private boolean branchFound = false;
   private boolean accountNumberEntered = false;
   private boolean bankSelected = false;
   private boolean bankValidateDisabled = true;
   private boolean bankActAddDisabled = true;
   private boolean skipToEnd = false;
   private boolean partnerSelected = false;
   private boolean custEdit = false;
   private String sortCodeEntry;
   private String bankAccountNumEntry;
   private String postCodeEntry;
   private AddressRec postCodeAddress;
   private boolean saveOk = false;
   private PartnerSelectOption custSelOpts;
   private DualListModel <PartnerRoleRec> custRoles;
   private List<PartnerRoleRec> rolesAvailable;
   private List<PartnerRoleRec> rolesAssigned;
  private String currView;

     /** Creates a new instance of ArMastBean */
   public ArMastBean()  {
   }
   
   @PostConstruct
   public void init(){
    LOGGER.log(INFO, "Current view {0}", getView());
    currView = getViewSimple();
    
    LOGGER.log(INFO, " view end {0}", currView);
    if(arAccount == null){
     arAccount = new  ArAccountRec();
     arAccount.setCompany(this.getCompList().get(0));
    }
    ptnrBase = (PartnerBaseRec)getSessionMap().get("partner");
    if(ptnrBase != null){
     getSessionMap().remove("partner");
     if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerPersonRec")){
      ptnrPerson = (PartnerPersonRec)ptnrBase;
      selectedPtnrTypeCd = "indiv";
     }else if(StringUtils.equals(ptnrBase.getClass().getSimpleName(), "PartnerPersonRec")){
      this.ptnrCorp = (PartnerCorporateRec)ptnrBase;
      selectedPtnrTypeCd = "corp";
     }
    }
    LOGGER.log(INFO, "ArMastBean partner from session {0}", ptnrBase);
    rolesAvailable = new ArrayList<>();
    rolesAssigned = new ArrayList<>();
    custRoles = new DualListModel<>(rolesAvailable,rolesAssigned);
   }

 public List<AddressRec> getAddressList() {
  return addressList;
 }

 public void setAddressList(List<AddressRec> addressList) {
  this.addressList = addressList;
 }

 public AddressRec getAddressSelected() {
  return addressSelected;
 }

 public void setAddressSelected(AddressRec addressSelected) {
  this.addressSelected = addressSelected;
 }

 
    public ArAccountRec getArAccount() {
    if(arAccount == null){
      arAccount = new ArAccountRec();
    }
    return arAccount;
  }




  public void setArAccount(ArAccountRec arAccount) {
    this.arAccount = arAccount;

  }

 public ArAccountRec getArAccountEdit() {
  return arAccountEdit;
 }

 public void setArAccountEdit(ArAccountRec arAccountEdit) {
  this.arAccountEdit = arAccountEdit;
 }

  
  public CompanyBasicRec getComp() {
    if(comp == null){

    }
    return comp;
  }

  public void setComp(CompanyBasicRec comp) {
    this.comp = comp;
  }

  

  public ArBankAccountRec getNewBank() {
    if(newBank == null){
      newBank = new ArBankAccountRec();
    }
    return newBank;
  }

  public void setNewBank(ArBankAccountRec newBank) {
    LOGGER.log(INFO, "setNewBank called with {0}", newBank);
    this.newBank = newBank;
  }

 public BankAccountRec getNewBnkAcnt() {
  return newBnkAcnt;
 }

 public void setNewBnkAcnt(BankAccountRec newBnkAcnt) {
  this.newBnkAcnt = newBnkAcnt;
 }

 public boolean isSaveOk() {
  return saveOk;
 }

 public void setSaveOk(boolean saveOk) {
  this.saveOk = saveOk;
 }

 
 public ArBankAccountRec getSelectedBank() {
  return selectedBank;
 }

 public void setSelectedBank(ArBankAccountRec selectedBank) {
  LOGGER.log(INFO, "setSelectedBank called with {0}", selectedBank);
  this.selectedBank = selectedBank;
 }

 public ArrayList<ArBankAccountRec> getArBanks() {
  
  return arBanks;
 }


  public void setArBanks(ArrayList<ArBankAccountRec> arBanks) {
    this.arBanks = arBanks;
  }

  public boolean isCreateBankBranch() {
    LOGGER.log(INFO, "isCreateBankBranch called value: {0}", createBankBranch);
    createBankBranch = false;
    if(bankAccountEntered){
      // bank account entered
      if(this.sortCodeEntered && !branchFound){
        createBankBranch = true;
      }
    }
    return createBankBranch;
  }

  public void setCreateBankBranch(boolean createBankBranch) {
    this.createBankBranch = createBankBranch;
  }

  public boolean isCreateBnkAccount() {
    return createBnkAccount;
  }

  public void setCreateBnkAccount(boolean createBnkAccount) {
    this.createBnkAccount = createBnkAccount;
  }

  public boolean isCreateBankDisabled() {
    String sortCd = newBank.getBankAccount().getAccountForBranch().getSortCode();

    LOGGER.log(INFO,"isCreateBankDisabled called sortCodeEntered {0}, branchFound: {1}, accountNumberEntered {2} ",
            new Object[]{sortCodeEntered,branchFound,accountNumberEntered});
    LOGGER.log(INFO,"Sort code {0}",sortCd);
    if(sortCodeEntered && !branchFound && accountNumberEntered){
     createBankDisabled = false;
    }

    return createBankDisabled;
  }

  public void setCreateBankDisabled(boolean createBankDisabled) {
    this.createBankDisabled = createBankDisabled;
  }


  public long getSelectedCompanyId() {
    return selectedCompanyId;
  }

  public void setSelectedCompanyId(long selectedCompanyId) {
    this.selectedCompanyId = selectedCompanyId;
  }

 public PartnerBaseRec getSelectedCust() {
  return selectedCust;
 }

 public void setSelectedCust(PartnerBaseRec selectedCust) {
  this.selectedCust = selectedCust;
 }

  
  public String getSelectedPtnrTypeCd() {
    if(selectedPtnrTypeCd == null){
      selectedPtnrTypeCd = "corp";
    }

    return selectedPtnrTypeCd;
  }

  public void setSelectedPtnrTypeCd(String selectedPtnrTypeCd) {
    this.selectedPtnrTypeCd = selectedPtnrTypeCd;
  }

 public List<ArAccountRec> getArAccountList() {
  return arAccountList;
 }

 public void setArAccountList(List<ArAccountRec> arAccountList) {
  this.arAccountList = arAccountList;
 }



  public boolean isArAccountEntered() {
    return arAccountEntered;
  }

  public void setArAccountEntered(boolean arAccountEntered) {
    this.arAccountEntered = arAccountEntered;
  }

 public boolean isArAccountSelected() {
  return arAccountSelected;
 }

 public void setArAccountSelected(boolean arAccountSelected) {
  this.arAccountSelected = arAccountSelected;
 }

 public AddressRec getArAcntAddrEdit() {
  return arAcntAddrEdit;
 }

 public void setArAcntAddrEdit(AddressRec arAcntAddrEdit) {
  this.arAcntAddrEdit = arAcntAddrEdit;
 }

 public List<PartnerBaseRec> getPtnrBaseList() {
  return ptnrBaseList;
 }

 public void setPtnrBaseList(List<PartnerBaseRec> ptnrBaseList) {
  this.ptnrBaseList = ptnrBaseList;
 }

 public PartnerBaseRec getPtnrBase() {
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
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

 public PartnerCorporateRec getPtnrCorpEdit() {
  return ptnrCorpEdit;
 }

 public void setPtnrCorpEdit(PartnerCorporateRec ptnrCorpEdit) {
  this.ptnrCorpEdit = ptnrCorpEdit;
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

 public PartnerPersonRec getPtnrPersonEdit() {
  return ptnrPersonEdit;
 }

 public void setPtnrPersonEdit(PartnerPersonRec ptnrPersonEdit) {
  this.ptnrPersonEdit = ptnrPersonEdit;
 }

  
  public String getSortCodeEntry() {
    return sortCodeEntry;
  }

  public void setSortCodeEntry(String sortCodeEntry) {
    LOGGER.log(INFO, "setSortCodeEntry {0}", sortCodeEntry);
    this.sortCodeEntry = sortCodeEntry;
  }

  public String getBankAccountNumEntry() {
    return bankAccountNumEntry;
  }

  public void setBankAccountNumEntry(String bankAccountNumEntry) {
    this.bankAccountNumEntry = bankAccountNumEntry;
  }

 public String getPostCodeEntry() {
  return postCodeEntry;
 }

 public void setPostCodeEntry(String postCodeEntry) {
  this.postCodeEntry = postCodeEntry;
 }

 public AddressRec getPostCodeAddress() {
  return postCodeAddress;
 }

 public void setPostCodeAddress(AddressRec postCodeAddress) {
  LOGGER.log(INFO, "setPostCodeAddress called with {0}", postCodeAddress);
  this.postCodeAddress = postCodeAddress;
 }


  


  public List<BankAccountRec> onBankAccountNumberComplete(String val){
    LOGGER.log(INFO, "bankAccountComplete called with input: {0}", val);
    LOGGER.log(INFO, "Branch {0}", newBank.getBankAccount().getAccountForBranch());
    LOGGER.log(INFO, "newBankBranch {0}", newBankBranch);
    if(val != null && !val.isEmpty()){
     bankAccountEntered = true;
    }else{
     this.bankMgr.getAccountsByPartAccntNumForBr("%", newBankBranch);
    }
    
    List<BankAccountRec> retlist = new ArrayList<>();
    boolean acntFound = false;
    if(bankActList == null || bankActList.isEmpty()){
     acntFound = false;
     bankActList = new ArrayList<>();
    }else{
     for(BankAccountRec curr:bankActList){
      if(curr.getAccountNumber().startsWith(val) && 
        curr.getAccountForBranch().getSortCode().equals(newBankBranch.getSortCode()) 
        ){
       retlist.add(curr);
       acntFound = true;
      }
     }
    }
    LOGGER.log(INFO, "acntFound {0}", acntFound);
    if(!acntFound){
     bankActList = this.bankMgr.getAccountsByPartAccntNumForBr(val, newBankBranch);
     retlist = bankActList;
    }
    
    LOGGER.log(INFO, "complete return act List {0}", retlist);
    LOGGER.log(INFO, "bankAcList {0}", bankActList);
    
   return retlist;
    
  }


  public List<BankBranchRec> onBankBranchComplete(String val) {
    LOGGER.log(INFO,"bankBranchComplete called with {0}",val);
    this.sortCodeEntered = true;
    newBnkAcnt = new BankAccountRec();
    bankActList = new ArrayList<>();
    if(val == null || val.isEmpty()){
     val = "%";
     bankBranchList = bankMgr.getBanchesBranchBySortCode(val);
     return bankBranchList;
    }
    List<BankBranchRec> retList = new ArrayList<>();
    sortCodeEntry = val;
    
    this.sortCodeEntered = true;
    
    if(bankBranchList == null){
     retList = bankMgr.getBanchesBranchBySortCode(val);
    }else{
      for(BankBranchRec curr : bankBranchList){
       if(curr.getSortCode().startsWith(val)){
        retList.add(curr);
       }
      }
      if(retList.isEmpty()){
       bankBranchList = this.bankMgr.getBanchesBranchBySortCode(val);
       return bankBranchList;
      }
      
    }
    
    if(retList.isEmpty()){
      BankBranchRec br = new BankBranchRec();
      br.setSortCode("Not found");
    }
    bankBranchList = retList;
    LOGGER.log(INFO, "Master Data Mgr returned {0}", retList);
    return retList;
  }
  
  public List<PartnerBaseRec> ptnrBaseComplete(String input){
   LOGGER.log(INFO, "ptnrBaseComplete called with input {0}", input);
   List<PartnerBaseRec> ptnrLst;
   if(input == null || input.isEmpty()){
   ptnrLst = ptnrMgr.getPartnersAll();
   }else{
    ptnrLst = ptnrMgr.getPartnersByName(input);
   }
   LOGGER.log(INFO, "ptnrLst from ptnrMgr {0}", ptnrLst);
   if(ptnrLst == null){
    if(input == null || input.isEmpty()){
     MessageUtil.addInfoMessage("contactNone", "validationText");
    }else{
     MessageUtil.addInfoMessageVar1("contNone4Opt","validationText", input);
    }
    return null;
   }
   for(PartnerBaseRec ptnr : ptnrLst){
    LOGGER.log(INFO, "ptnr class {0}", ptnr.getClass().getSimpleName());
    LOGGER.log(INFO, "name {0}", ptnr.getName());
   }
   LOGGER.log(INFO, "ptnrBaseComplete returns {0}", ptnrLst);
   
   return  ptnrLst;      
  }
  public List<PartnerPersonRec> personPtnrComplete(String val) {
   LOGGER.log(INFO, "personPtnrComplete called with {0}", val);
   List<PartnerPersonRec> retList; 
   if(val == null || val.isEmpty()){
     retList = ptnrMgr.getAllPartnerIndividual();
     return retList;
    }
    
    retList = ptnrMgr.getIndivPtnrsBySurname(val);
    if(retList == null){
     return null;
    }
    LOGGER.log(INFO, "personPtnrComplete returns {0}", retList);
    if(retList.size() > 0){
     LOGGER.log(INFO, "Person id {0}", retList.get(0).getId());
    List<PartnerPersonRec> selList = new ArrayList<>();
    for(PartnerPersonRec p:retList){
     
     if(StringUtils.startsWithIgnoreCase(p.getDisplayName(), val)){
      selList.add(p);
     }
    }
    Collections.sort(selList, new PartnerPersonByNameStructured());
    return selList;
    }else{
    Collections.sort(retList, new PartnerPersonByNameStructured());
    return retList;
    }
  }
public List<PartnerCorporateRec> corpPtnrComplete(String val) {

  LOGGER.log(INFO, "corpPtnrComplete called with {0}", val);
  if(val == null || val.isEmpty()){
    val = new String();
  }
  
  List<PartnerCorporateRec> retList = ptnrMgr.getCorpPartnersByTradingName(val);
  /*if(retList == null){
    retList = new ArrayList<>();
    PartnerCorporateRec ptnr = new PartnerCorporateRec();
    ptnr.setTradingName("No Corp partner - please create");
    Long noPtnrId = Long.valueOf("0");
    ptnr.setId(noPtnrId);
    retList.add(ptnr);
  }*/
  if(retList == null || retList.isEmpty()){
   return null;
  }
  Collections.sort(retList, new PartnerBaseByTradeName());
  LOGGER.log(INFO, "corpPtnrComplete returns {0}", retList);
  return retList;
  }

public List<CountryRec> countryComplete(String input){
 LOGGER.log(INFO, "countryComplete called with {0}", input);
 List<CountryRec> retList ;
 if(input == null || input.isEmpty()){
    retList = mdmMgr.getCountriesAll();
 }else{
  retList = mdmMgr.getCountriesByName(input);
 }
 
 return retList;
}

  public List<AddressRec> postCodeComplete(String entry){
   LOGGER.log(INFO, "postCodeComplete called with {0}", entry);
   List<AddressRec> addresses;
   if(entry == null || entry.isEmpty()){
    addresses = ptnrMgr.getAllAddresses();
    }else{
    addresses = ptnrMgr.getAddressesForPostCodePart(entry);
   }
   LOGGER.log(INFO, "postCodeComplete returns {0}", addresses);
   return addresses;
   
  }
  
  public void onSaveArActUpdateAction(){
   LOGGER.log(INFO, "saveArActUpdateAction called {0}");
   String page = getView();
   try{
    arAccount.setChangedBy(getLoggedInUser());
    arAccount.setChangedOn(new Date());
    arAccount.getAccountAddress().setChangedBy(getLoggedInUser());
    arAccount.getAccountAddress().setChangedOn(new Date());
    if(arAccount.getContacts() != null){
     ListIterator<ContactRec> li = arAccount.getContacts().listIterator();
     while(li.hasNext()){
      ContactRec cont = li.next();
      if(cont.getId() == null){
       cont.setCreatedBy(getLoggedInUser());
       cont.setCreatedOn(new Date());
       cont.setArAccount(arAccount);
       cont.setContactFor(arAccount.getArAccountFor());
       li.set(cont);
      }else{
       cont.setChangedBy(getLoggedInUser());
       cont.setChangedOn(new Date());
       li.set(cont);
      }
     }
    }
    for(ContactRec cont: arAccount.getContacts()){
     LOGGER.log(INFO, "cont summary {0} user id {1}", new Object[]{cont.getSummary(),cont.getCreatedBy().getId()});
    }
    if(arAccount.getArAccountBanks() != null){
     ListIterator<ArBankAccountRec> bnkLi = arAccount.getArAccountBanks().listIterator();
     while(bnkLi.hasNext()){
      ArBankAccountRec bnk = bnkLi.next();
      if(bnk.getId() == null || bnk.getId() < 0 ){
       bnk.setCreatedBy(getLoggedInUser());
       bnk.setCreatedOn(new Date());
       bnk.setId(null);
      }else{
       bnk.setChangedBy(getLoggedInUser());
       bnk.setChangedOn(new Date());
      }
      bnkLi.set(bnk);
     }
    }
    LOGGER.log(INFO, "arAccount.getReconciliationAc() {0}", arAccount.getReconciliationAc()); 
    LOGGER.log(INFO, "arAccount.getReconciliationAc().getId() {0}", arAccount.getReconciliationAc().getId());
    LOGGER.log(INFO, "arAccount note {0}", arAccount.getAcntNote()); 
    arAccount = arManager.updateArAccount(arAccount, page);
    MessageUtil.addInfoMessage("arAcntUpdate", "blacResponse");
    
   }catch(BacException e){
    GenUtil.addErrorMessage("Could not update account");
   }catch(Exception e){
    GenUtil.addErrorMessage("Other update error: "+e.getCause().getLocalizedMessage());
   }
  }
  
  public void onSaveArAccountNew(){
   LOGGER.log(INFO, "ArMastBean onSaveArAccountNew called", arAccount);
   LOGGER.log(INFO,"Address Post Code {0}",arAccount.getAccountAddress().getPostCode());
   LOGGER.log(INFO, "company for account is {0}",arAccount.getCompany().getId());
   UserRec usr = this.getLoggedInUser();
   Date currDate = new Date();
   try{
    arAccount.setCreatedBy(usr);
    arAccount.setCreatedOn(currDate);
    LOGGER.log(INFO, "arAccount.getArAccountBanks() n {0}", arAccount.getArAccountBanks());
    if(arAccount.getArAccountBanks() != null){
    ListIterator<ArBankAccountRec> banksLi = arAccount.getArAccountBanks().listIterator();
    while(banksLi.hasNext()){
     ArBankAccountRec curr = banksLi.next();
     LOGGER.log(INFO, "Ar bank id {0} bank id {1}", new Object[]{curr.getId(),curr.getBankAccount().getId()});
     if(curr.getId() < 0){
      curr.setId(null);
      curr.setCreatedBy(usr);
      curr.setCreatedOn(currDate);
      curr.setBankForArAccount(arAccount);
      banksLi.set(curr);
     }
    }
    for(ArBankAccountRec r:arAccount.getArAccountBanks()){
     LOGGER.log(INFO, "Pre update Ar bank id {0} bank id {1}", new Object[]{r.getId(),r.getBankAccount().getId()});
    }
    }
    LOGGER.log(INFO,"About to call arManager update account after bank");
    
    arAccount = arManager.updateArAccount(arAccount,getView());
    if(arAccount.getId() != null){
     MessageUtil.addInfoMessageVar1("arActCreateSuccess", "blacResponse", arAccount.getArAccountCode());
          
     saveOk = true;
     setStep(0);
     arAccount = new ArAccountRec();
     ptnrCorp  = null;
     ptnrPerson = null;
     
     PrimeFaces.current().ajax().update("arActCrFrm");
     /*RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.execute("PF('actWv').back();");
     rCtx.update("actDetails");
     */
     LOGGER.log(INFO, "end onSaveArAccountNew");
    }else{
     MessageUtil.addWarnMessageParam("arActCreateFail", "errorText", "Incorrect information entered");
     
    }
   }catch(BacException ex){
    MessageUtil.addWarnMessageParam("arActCreateFail", "errorText", ex.getLocalizedMessage());
    
   }catch(Exception ex){
    MessageUtil.addWarnMessageParam("arActCreateFail", "errorText", ex.getLocalizedMessage());
    
   }
  }

 public ContactRec getContactNew() {
  return contactNew;
 }

 public void setContactNew(ContactRec contactNew) {
  this.contactNew = contactNew;
 }

 public List<ContactRoleRec> getContactRoleList() {
  return contactRoleList;
 }

 public void setContactRoleList(List<ContactRoleRec> contactRoleList) {
  this.contactRoleList = contactRoleList;
 }
  
  
  
  public List<ContactRec> getContacts(){
   if(this.arAccount.getContacts() == null || arAccount.getContacts().isEmpty() ){
    List<ContactRec> contacts = this.contMgr.conactsForArAccount(arAccount);
    arAccount.setContacts(contacts);
   }
   return arAccount.getContacts();
  }
  
  public ArrayList<PartnerCorporateRec> getCorpPtnrs() {
    return corpPtnrs;
  }

  public void setCorpPtnrs(ArrayList<PartnerCorporateRec> corpPtnrs) {
    this.corpPtnrs = corpPtnrs;
  }

 public List<CountryRec> getCountries() {
  return countries;
 }

 public void setCountries(List<CountryRec> countries) {
  this.countries = countries;
 }

  
  public boolean isNewPartnerCorp() {
    return newPartnerCorp;
  }

  public void setNewPartnerCorp(boolean newPartnerCorp) {
    this.newPartnerCorp = newPartnerCorp;
  }

  public boolean isNewPartnerIndiv() {
    return newPartnerIndiv;
  }

  public void setNewPartnerIndiv(boolean newPartnerIndiv) {
    this.newPartnerIndiv = newPartnerIndiv;
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

 public PartnerPersonRec getPtnrPersonNew() {
  return ptnrPersonNew;
 }

 public void setPtnrPersonNew(PartnerPersonRec ptnrPersonNew) {
  this.ptnrPersonNew = ptnrPersonNew;
 }

 public List<PartnerRoleRec> getRolesAvailable() {
  return rolesAvailable;
 }

 public void setRolesAvailable(List<PartnerRoleRec> rolesAvailable) {
  this.rolesAvailable = rolesAvailable;
 }

 public List<PartnerRoleRec> getRolesAssigned() {
  return rolesAssigned;
 }

 public void setRolesAssigned(List<PartnerRoleRec> rolesAssigned) {
  this.rolesAssigned = rolesAssigned;
 }
  
 

  public List<BankAccountRec> getBankActList() {
    return bankActList;
  }

  public void setBankActList(List<BankAccountRec> bankActList) {
    this.bankActList = bankActList;
  }

 public BankBranchRec getNewBankBranch() {
  return newBankBranch;
 }

 public void setNewBankBranch(BankBranchRec newBankBranch) {
  this.newBankBranch = newBankBranch;
 }

  public List<BankBranchRec> getBankBranchList() {
    return bankBranchList;
  }

  public void setBankBranchList(List<BankBranchRec> bankBranchList) {
    this.bankBranchList = bankBranchList;
  }



  public ArrayList<SortOrderRec> getSortOrderList() {
    if(sortOrderList == null){
      // need to get sort orders from sys buffer
      LOGGER.log(INFO, "WEb need to get sort orders");
      UserRec usr = getLoggedInUser();
      LOGGER.log(INFO, "LOgged in user {0}", usr);
      sortOrderList = this.buff.getSortOrders(usr,getView());
      Collections.sort(sortOrderList, new SortOrderByName());
      /*if(sortOrderList != null && !sortOrderList.isEmpty()){
       SortOrderRec sort = sortOrderList.get(0);
       
       if(arAccount == null){
        arAccount = this.getArAccount();
       }
       if(arAccount.getSortOrder() == null){
        arAccount.setSortOrder(sort);
       }
       
      }
      */
      LOGGER.log(INFO, "Sort orders from sys buffer {0}", sortOrderList);
    }
    return sortOrderList;
  }

  public void setSortOrderList(ArrayList<SortOrderRec> sortOrderList) {
    this.sortOrderList = sortOrderList;
  }

  public boolean isAccountNumberEntered() {
    return accountNumberEntered;
  }

  public void setAccountNumberEntered(boolean accountNumberEntered) {
    this.accountNumberEntered = accountNumberEntered;
  }

  public boolean isBranchFound() {
    return branchFound;
  }

  public void setBranchFound(boolean branchFound) {
    this.branchFound = branchFound;
  }

 public BankRec getBankNew() {
  return bankNew;
 }

 public void setBankNew(BankRec bankNew) {
  this.bankNew = bankNew;
 }

  
  public boolean isBankSelected() {
    return bankSelected;
  }

  public void setBankSelected(boolean bankSelected) {
    this.bankSelected = bankSelected;
  }

 public boolean isCustEdit() {
  return custEdit;
 }

 public void setCustEdit(boolean custEdit) {
  this.custEdit = custEdit;
 }

 public DualListModel<PartnerRoleRec> getCustRoles() {
  return custRoles;
 }

 public void setCustRoles(DualListModel<PartnerRoleRec> custRoles) {
  this.custRoles = custRoles;
 }

 public PartnerSelectOption getCustSelOpts() {
  return custSelOpts;
 }

 public void setCustSelOpts(PartnerSelectOption custSelOpts) {
  this.custSelOpts = custSelOpts;
 }

 public List<PartnerBaseRec> getCustSrchList() {
  return custSrchList;
 }

 public void setCustSrchList(List<PartnerBaseRec> custSrchList) {
  this.custSrchList = custSrchList;
 }

 
  public List<FiGlAccountCompRec> getGlReconciliationAcs(){
   
   if(glReconciliationAcs == null){
    LOGGER.log(INFO, "getGlReconciliationAcs called {0}", glReconciliationAcs);
    getGlReconciliationAcs(this.arAccount.getCompany());
   }
   LOGGER.log(INFO, "getGlReconciliationAcs Returns {0}", glReconciliationAcs);
   return glReconciliationAcs;
  }
  public List<FiGlAccountCompRec> getGlReconciliationAcs(CompanyBasicRec comp) {
    if(glReconciliationAcs == null){
      glReconciliationAcs = glAcMgr.getDebtorReconciliationAcs(comp);
      if(glReconciliationAcs == null || glReconciliationAcs.isEmpty()){
       MessageUtil.addClientErrorMessage("arActCrFrm:msgs", "arAcntGlDrsNone", "errorText");
       PrimeFaces.current().ajax().update("arActCrFrm:msgs");
       return null;
      }
      LOGGER.log(INFO, "Gl accountamanger returned {0}", glReconciliationAcs);
      LOGGER.log(INFO, "glReconciliationAc id {0} number {1}", new Object[]{
       glReconciliationAcs.get(0).getId(), glReconciliationAcs.get(0).getCoaAccount().getRef()
      });
      
    }
    return glReconciliationAcs;
  }

  public void setGlReconciliationAcs(List<FiGlAccountCompRec> glReconciliationAcs) {
    this.glReconciliationAcs = glReconciliationAcs;
  }

 public boolean isSkipToEnd() {
  return skipToEnd;
 }

 public void setSkipToEnd(boolean skipToEnd) {
  this.skipToEnd = skipToEnd;
 }

  
  public boolean isSortCodeEntered() {
   if(newBank == null){
    sortCodeEntered = false;
   }
    return sortCodeEntered;
  }

  public void setSortCodeEntered(boolean sortCodeEntered) {
    this.sortCodeEntered = sortCodeEntered;
  }

  public boolean isBankAccountEntered() {
    return bankAccountEntered;
  }

  public void setBankAccountEntered(boolean bankAccountEntered) {
    this.bankAccountEntered = bankAccountEntered;
  }

  public boolean isBankAccountSelected() {
    return bankAccountSelected;
  }

  public void setBankAccountSelected(boolean bankAccountSelected) {
    this.bankAccountSelected = bankAccountSelected;
  }

 public boolean isBankAccountValid() {
  return bankAccountValid;
 }

 public void setBankAccountValid(boolean bankAccountValid) {
  this.bankAccountValid = bankAccountValid;
 }

  public boolean isSortCodeValid() {
    return sortCodeValid;
  }

  public void setSortCodeValid(boolean sortCodeValid) {
    this.sortCodeValid = sortCodeValid;
  }

  public boolean isBankValidateDisabled() {
   if(bankAccountValid){
    bankValidateDisabled = true;
   }else bankValidateDisabled = !(!bankAccountValid &&(sortCodeEntered || branchFound)  && bankAccountEntered);
    LOGGER.log(INFO, "isBankValidateDisabled returns {0} sortEntered {1} acc entered {2}",
            new Object[]{bankValidateDisabled,sortCodeEntered,bankAccountEntered});
    return bankValidateDisabled;
  }

  public void setBankValidateDisabled(boolean bankValidateDisabled) {
    this.bankValidateDisabled = bankValidateDisabled;
  }

  public boolean isBankActAddDisabled() {
   if(this.bankAccountValid){
    bankActAddDisabled = true;
   } else bankActAddDisabled = !((this.bankAccountSelected || bankAccountEntered) && (sortCodeValid || branchFound));
    LOGGER.log(INFO, "isBankActAddDisabled returns {0} sortEntered {1} branch found {2} acc entered {3}",
            new Object[]{bankActAddDisabled,sortCodeEntered,branchFound,bankAccountEntered});
    return bankActAddDisabled;
  }

  public void setBankActAddDisabled(boolean bankActAddDisabled) {
    this.bankActAddDisabled = bankActAddDisabled;
  }

 public boolean isPartnerSelected() {
  return partnerSelected;
  
 }

 public boolean  getPartnerSelected(){
  return partnerSelected;
 }
 public void setPartnerSelected(boolean partnerSelected) {
  this.partnerSelected = partnerSelected;
 }
 


  public List<PaymentTermsRec> getPayTermsList() {
    LOGGER.log(INFO, "Web getPayTermsList called terms are {0}", payTermsList);
    if(payTermsList == null){
      try{
        UserRec usr = getLoggedInUser();
        LOGGER.log(INFO, "Logged in user {0}", usr);
        payTermsList = buff.getPaymentTermsList();
        Collections.sort(payTermsList, new PaymentTermsByCode());
      }catch(BacException ex){
        GenUtil.addErrorMessage("Could not retrieve payment terms due to: "+ex.getLocalizedMessage());
      }
    }
    return payTermsList;
  }

  public void setPayTermsList(ArrayList<PaymentTermsRec> payTermsList) {
    this.payTermsList = payTermsList;
  }

  public List<PaymentTypeRec> getPayTypes() {
    LOGGER.log(INFO, "ArMastBean.getPayTypes called -  terms  {0}", payTypes);
    LOGGER.log(INFO,"comp {0}",arAccount.getCompany());
    if(payTypes == null){
      try{
       if(arAccount.getCompany() != null){
        payTypes = buff.getPaymentTypesForLed(arAccount.getCompany(),"AR");
        Collections.sort(payTypes, new PaymentTypeByDescr());
        LOGGER.log(INFO, "payTypes from buffer {0}", payTypes);
        return payTypes;
       }else{
        return new ArrayList<>();
       }
      }catch(BacException ex){
        GenUtil.addErrorMessage("Could not get payment types. reason: "+ex.getLocalizedMessage());
      }

    }
    Collections.sort(payTypes, new PaymentTypeByCode());
    return payTypes;
  }

  public void setPayTypes(List<PaymentTypeRec> payTypes) {
    this.payTypes = payTypes;
  }

  public List<BankRec> getBanks() {
   if(banks == null){
    banks = this.bankMgr.getBanks();
   }
    return banks;
  }

  public void setBanks(List<BankRec> banks) {
    this.banks = banks;
  }


  public SortOrderRec getSelectedSortOrder() {
    return selectedSortOrder;
  }

  public void setSelectedSortOrder(SortOrderRec selectedSortOrder) {
    this.selectedSortOrder = selectedSortOrder;
  }



  public List<String> tradingNameComplete(String input){
    LOGGER.log(INFO, "tradingNameComplete called with input {0}", input);

    ArrayList<String> list = new ArrayList<>();
    List<PartnerCorporateRec> ptnrList =  ptnrMgr.getCorpPartnersByTradingName(input);
    LOGGER.log(INFO,"Partner List returned by Manager is: {0}",ptnrList);
    return list;

  }

  public void onNewPtnrDlg(){
   RequestContext rCtx = RequestContext.getCurrentInstance();
   List<String> updts = new ArrayList<>();
   updts.add("newPtrAcnt");
   updts.add("newPtnrPg");
   
   rCtx.update(updts);
   
   rCtx.execute("PF('crPtnrWv').show()");
   
   LOGGER.log(INFO, "onNewPtnrDlg");
  }
  
  public void onNewPtnrTrf(){
   LOGGER.log(INFO, "onNewPtnrTrf called");
   UserRec currUsr = this.getLoggedInUser();
   Date currDate = new Date();
   RequestContext rCtx = RequestContext.getCurrentInstance();
   switch(selectedPtnrTypeCd){
    case "indiv":
     ptnrPerson.setCreatedBy(currUsr);
     ptnrPerson.setCreatedDate(currDate);
     long ptnrId = ptnrMgr.createIndivPartnerAR(ptnrPerson, currUsr, getView()); 
     this.ptnrPerson.setId(ptnrId);
     rCtx.update("personName");
     break;
    case "corp":
     ptnrCorp.setCreatedBy(currUsr);
     ptnrCorp.setCreatedDate(currDate);
     LOGGER.log(INFO, "currUsr id {0}", currUsr.getId());
     ptnrId = ptnrMgr.createCorporatePartnerAR(ptnrCorp, currUsr, getView()); 
     ptnrCorp.setId(ptnrId);
     rCtx.update("tradName");
   }
   rCtx.execute("PF('crPtnrWv').hide()");
  }
  
  
  public void onPostCodeSearchDlg(){
   LOGGER.log(INFO, "called onPostCodeSearchDlg to open search dlg");
   PrimeFaces pf = PrimeFaces.current();
   this.postCodeEntry = arAccount.getAccountAddress().getPostCode();
   pf.ajax().update("addrSrchFrm");
   pf.executeScript("PF('pstCdSrchDlgWv').show();");
  }
  
  
  public void onPostCodeSelect(SelectEvent evt){
   LOGGER.log(INFO, "onPostCodeSelect called with value {0}", evt.getObject());
   LOGGER.log(INFO, "called by {0}", evt.getComponent().getClientId());
   String clientId = evt.getComponent().getClientId();
   RequestContext rCtx = RequestContext.getCurrentInstance();
   switch(clientId){
    case "addrPostCdSearchEd":
     arAccountEdit.setAccountAddress((AddressRec)evt.getObject());
     rCtx.update("editAddrPg");
     break;
    default:
     arAccount.setAccountAddress((AddressRec)evt.getObject());
     break;
   }
   
  }
  
  public void onTradingNameSelect(SelectEvent evt){
    LOGGER.log(INFO, "onTradingNameSelect called with {0}", evt.getObject());
    arAccount.setArAccountFor((PartnerBaseRec)evt.getObject());
    AddressRec ptrnrDefaultAddr = arAccount.getArAccountFor().getDefaultAddress();
    if(ptrnrDefaultAddr != null){
     if( ptrnrDefaultAddr.getId() != null && ptrnrDefaultAddr.getId() != 0){
      LOGGER.log(INFO, "Use partner default Address");
      arAccount.setAccountAddress(ptrnrDefaultAddr);
     // PrimeFaces.current().ajax().update(clientIds);
     }
    
    }
  }
  
  public void onBankBranchTransfer(){
   LOGGER.log(INFO, "onBankBranchTransfer called");
   LOGGER.log(INFO, "newBankBranch sort{0} bank{1} ", 
           new Object[]{newBank.getBankAccount().getAccountForBranch().getSortCode(),
           newBank.getBankAccount().getAccountForBranch()});
   
   
   BankBranchRec bnkBr = newBank.getBankAccount().getAccountForBranch();
   LOGGER.log(INFO, "bnkBr id {0}", bnkBr.getId());
   if(bnkBr.getId() == null){
    bnkBr.setCreatedBy(getLoggedInUser());
    bnkBr.setCreatedOn(new Date());
   }else{
    bnkBr.setUpdatedBy(getLoggedInUser());
    bnkBr.setUpdatedOn(new Date());
   }
   LOGGER.log(INFO, "bnkBr cr usr {0} updt usr {1}", new Object[]{bnkBr.getCreatedBy(),
    bnkBr.getUpdatedBy()});
   //RequestContext rCtx = RequestContext.getCurrentInstance();
   try{
   bnkBr = this.bankMgr.updateBankBranch(bnkBr, getView());
   newBank.getBankAccount().setAccountForBranch(bnkBr);
   LOGGER.log(INFO, "onBankBranchTransfer {0}", bnkBr);
   MessageUtil.addInfoMessageVar1("bnkBrCr", "blacResponse", bnkBr.getSortCode());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("newBankActFrm:addSortCode");
   pf.executeScript("PF('addBnkBrWv').hide()");
   //rCtx.update("addSortCode");
   //rCtx.execute("PF('addBnkBrWv').hide()");
   }catch (Exception e){
    //MessageUtil.addErrorMessage("bnkBankBrCr", "errorText");
    MessageUtil.addClientWarnMessage("addBranchFrm:addBrMsg", "bnkBranchCr", "errorText");
   }
  }
  
  public void onBacsCollectAuthCheck(){
   if(newBank.getBankAccount() == null){
    MessageUtil.addClientWarnMessage("newBankActFrm:bnkAcCrMsg", "arAcntBnkDDCol", "validationText");
   }
   LOGGER.log(INFO, "onBacsCollectAuthCheck coll auth {0}", newBank.getBankAccount().isDirectDebitsAllowed());
   if(newBank.isCollectionAuthorisation()){
    if(!newBank.getBankAccount().isDirectDebitsAllowed()){
     MessageUtil.addWarnMessage("bacsDDColnotVal", "validationText");
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("ddColMsg");
    }
   }
  }
  
  public void onBankAccountArTransfer(){
   LOGGER.log(INFO, "onBankAccountArTransfer called");
   String view = this.getViewSimple();
   
   LOGGER.log(INFO, "view {0}", view);
   LOGGER.log(INFO, "newBnkAcnt acnt num {0}", newBank.getBankAccount().getAccountNumber());
   LOGGER.log(INFO, "newBnkAcnt acnt branch {0}", newBank.getBankAccount().getAccountForBranch());
   
   
   LOGGER.log(INFO, "newBnkAcnt acnt sort {0}", newBank.getBankAccount().getAccountForBranch().getSortCode());
   
   
    
   
   LOGGER.log(INFO, "AR bank bank ac {0}", newBank.getBankAccount());
   LOGGER.log(INFO, "AR bank bank ac id {0}", newBank.getBankAccount().getId());
   
   List<ArBankAccountRec> arBbanks = arAccount.getArAccountBanks();
   if(arBbanks == null){
    arBbanks = new ArrayList<>();
   }
   ListIterator<ArBankAccountRec> bankLi = arBbanks.listIterator();
   boolean foundBnk = false;
   while(bankLi.hasNext() && !foundBnk ){
    ArBankAccountRec curr = bankLi.next();
    LOGGER.log(INFO, "Curr acnt num {0}", curr.getBankAccount().getAccountNumber());
    LOGGER.log(INFO, "Curr sort {0}", curr.getBankAccount().getAccountForBranch().getSortCode());
    if(StringUtils.equals(curr.getBankAccount().getAccountNumber(), newBank.getBankAccount().getAccountNumber()) || 
      StringUtils.equals(curr.getBankAccount().getAccountForBranch().getSortCode(), 
              newBank.getBankAccount().getAccountForBranch().getSortCode())){
     foundBnk = true;
    }
   }
   LOGGER.log(INFO, "arBbanks {0}", arBbanks);
   if(!foundBnk){
    long numEntries = arBbanks.size();
    numEntries++;
    numEntries = numEntries * -1;
    //newBank.setBankAccount(newBnkAcnt);
    newBank.setId(numEntries);
    LOGGER.log(INFO, "newBnkAcnt acnt {0}  newBank acnt num {1} bank rec {2}", new Object[]{
     newBnkAcnt.getAccountNumber(), newBank.getBankAccount().getAccountNumber(),
     newBank.getBankAccount().getId()});
    
    
    if(newBank.getBankAccount().getAccountName() == null || newBank.getBankAccount().getAccountName().isEmpty()){
     newBank.getBankAccount().setAccountName(newBank.getAccountName());
    }
    
    LOGGER.log(INFO, "After check bank sort {0}", newBank.getBankAccount().getAccountForBranch().getSortCode());
    LOGGER.log(INFO, "ar bank id {0}",newBank.getId());
    arBbanks.add(newBank);
   }
   LOGGER.log(INFO, "banks after add {0}", arBbanks);
   LOGGER.log(INFO, "1st bank num {0}", arBbanks.get(0).getBankAccount().getAccountNumber());
   LOGGER.log(INFO, "1st bank name {0}", arBbanks.get(0).getAccountName());
   //banks.add(newBank);
   arAccount.setArAccountBanks(arBbanks);
   
   
   for(ArBankAccountRec curr: arAccount.getArAccountBanks()){
    LOGGER.log(INFO, "Account id {0}", curr.getBankAccount().getId());
    LOGGER.log(INFO, "Account num  {0}", curr.getBankAccount().getAccountNumber());
    LOGGER.log(INFO, "Sort code in set  {0}", curr.getBankAccount().getAccountForBranch().getSortCode());
   }
   
   //RequestContext rCtx = RequestContext.getCurrentInstance();
   PrimeFaces pf = PrimeFaces.current();
   if(view.equals("arActUpdate")){
    pf.ajax().update("actDetails:bnkTbl");
   }else{
    pf.ajax().update("arActCrFrm:bnkTbl");
   }
   
   pf.executeScript("PF('addBnkAcntArWv').hide()");
  }
  
  public void onBankAccountArTransferEdCust(){
   LOGGER.log(INFO, "onBankAccountArTransferEdCust called");
   if(bankAccountValid){
    newBnkAcnt.setAccountForBranch(newBankBranch);
   }
   newBank.setBankAccount(newBnkAcnt);
   List<ArBankAccountRec> arBbanks = arAccount.getArAccountBanks();
   if(arBbanks == null){
    arBbanks = new ArrayList<>();
   }
   ListIterator<ArBankAccountRec> bankLi = arBbanks.listIterator();
   boolean foundBnk = false;
   while(bankLi.hasNext() ){
    ArBankAccountRec curr = bankLi.next();
    LOGGER.log(INFO, "Curr acnt num {0}", curr.getBankAccount().getAccountNumber());
    LOGGER.log(INFO, "Curr sort {0}", curr.getBankAccount().getAccountForBranch().getSortCode());
    
   }
   LOGGER.log(INFO, "arBbanks {0}", arBbanks);
   if(!foundBnk){
    long numEntries = arBbanks.size();
    numEntries++;
    numEntries = numEntries * -1;
    newBank.setBankAccount(newBnkAcnt);
    newBank.setId(numEntries);
    LOGGER.log(INFO, "newBnkAcnt acnt {0}  newBank acnt num {1} bank rec {2}", new Object[]{
     newBnkAcnt.getAccountNumber(), newBank.getBankAccount().getAccountNumber(),
     newBank.getBankAccount().getId()});
    
    
    if(newBank.getBankAccount().getAccountName() == null || newBank.getBankAccount().getAccountName().isEmpty()){
     newBank.getBankAccount().setAccountName(newBank.getAccountName());
    }
    
    LOGGER.log(INFO, "After check bank sort {0}", newBank.getBankAccount().getAccountForBranch().getSortCode());
    LOGGER.log(INFO, "ar bank id {0}",newBank.getId());
    arBbanks.add(newBank);
   }
   LOGGER.log(INFO, "banks after add {0}", arBbanks);
   LOGGER.log(INFO, "1st bank num {0}", arBbanks.get(0).getBankAccount().getAccountNumber());
   LOGGER.log(INFO, "1st bank name {0}", arBbanks.get(0).getAccountName());
   //banks.add(newBank);
   arAccount.setArAccountBanks(arBbanks);
   
   
   for(ArBankAccountRec curr: arAccount.getArAccountBanks()){
    LOGGER.log(INFO, "Account id {0}", curr.getBankAccount().getId());
    LOGGER.log(INFO, "Account num  {0}", curr.getBankAccount().getAccountNumber());
    LOGGER.log(INFO, "Sort code in set  {0}", curr.getBankAccount().getAccountForBranch().getSortCode());
   }
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("bnkTbl");
   rCtx.execute("PF('addBnkAcntArWv').hide()");
  }
  
  public void onBankAccountAutoCrDlg(){
   LOGGER.log(INFO, "onBankAccountAutoCrDlg called");
   newBnkAcnt.setAccountForBranch(new BankBranchRec());
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("bnkAcntAutoFrm");
   rCtx.execute("PF('bnkAcntAutoWv').show()");
  }
 
  public void onBankAccountSelect(SelectEvent evt){
    LOGGER.log(INFO, "onBankAccountSelect called with value {0}", evt.getObject());
    LOGGER.log(INFO, "onBankAccountSelect branch  {0}", newBank.getBankAccount().getAccountForBranch());
    LOGGER.log(INFO, "onBankAccountSelect branch 2 {0}", newBankBranch);
    
    if(evt.getObject() == null){
     return;
    }
    BankAccountRec selected = (BankAccountRec)evt.getObject();
    
    // check to see if sort code and account num used for this AR account
    
     
    ListIterator<BankAccountRec> li = bankActList.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     BankAccountRec actRec = li.next();
     if(actRec.getId().equals(selected.getId())){
       newBank.setBankAccount(actRec);
       newBank.setAccountName(actRec.getAccountName());
       LOGGER.log(INFO, "Selected  account number is {0} account name {1}",new Object[]{ 
               newBank.getBankAccount().getAccountNumber(),newBank.getBankAccount().getAccountName()});
       this.bankAccountSelected = true;
       this.bankAccountValid = true;
       List<String> updt = new ArrayList<>();
       
       updt.add("newBankActFrm:newBnkAcntNm");
       updt.add("newBankActFrm:newBnkAcntRef");
       updt.add("newBankActFrm:bnkCol");
       PrimeFaces.current().ajax().update(updt);
       
     }
    }



  }
  
  public void onBankAcntUniqueValidate(FacesContext context, UIComponent toValidate, Object value){
   LOGGER.log(INFO, "Value passed {0} ", value);
   BankAccountRec inp = (BankAccountRec)value;
   
   if(arAccount.getArAccountBanks() == null || arAccount.getArAccountBanks().isEmpty()){
    ((EditableValueHolder) toValidate).setValid(true);
    PrimeFaces.current().ajax().update("newBankActFrm:bnkAcCrMsg");
    return;
   }
   
   for(ArBankAccountRec arB : arAccount.getArAccountBanks()){
    if(StringUtils.equals(inp.getAccountNumber(), arB.getBankAccount().getAccountNumber()) &&
     StringUtils.equals(inp.getAccountForBranch().getSortCode(), arB.getBankAccount().getAccountForBranch().getSortCode())){
     LOGGER.log(INFO, "Duplicate bank account");
     ((EditableValueHolder) toValidate).setValid(false);
     PrimeFaces.current().ajax().update("newBankActFrm:bnkAcCrMsg");
     return;
    }
   }
   
   LOGGER.log(INFO, "duplicate not found ");
   ((EditableValueHolder) toValidate).setValid(true);
   PrimeFaces.current().ajax().update("newBankActFrm:bnkAcCrMsg");       
           
  }
  
  public void onSortCodeSelect(SelectEvent evt){
    LOGGER.log(INFO, "onSortCodeSelect called with {0}", evt.getObject());
    BankBranchRec selBr = (BankBranchRec)evt.getObject();
    //RequestContext rCtx = RequestContext.getCurrentInstance();
    ListIterator<BankBranchRec> li = bankBranchList.listIterator();
    boolean found = false;
    branchFound = false;
    while(li.hasNext() && ! found){
      BankBranchRec rec = li.next();
      LOGGER.log(INFO, "Sort code from bankBranch List {0}", rec.getSortCode());
      LOGGER.log(INFO, "selBr id {0} rec id {1} ", new Object[]{selBr.getId(),rec.getId()});
      LOGGER.log(INFO,"newBank {0}",newBank);
      LOGGER.log(INFO,"newBank bank {0}",newBank.getBankAccount());
      if(rec.getId().equals(selBr.getId())){
       branchFound = true;
       if(newBank.getBankAccount() == null){
        BankAccountRec bnkAc = new BankAccountRec();
        newBank.setBankAccount(bnkAc);
       }
       newBank.getBankAccount().setAccountForBranch(rec);
       newBankBranch = rec;
       
        List<String> updt = new ArrayList<>();
        updt.add("newBankActFrm:addSortCode");
        updt.add("newBankActFrm:addNewBnkAcntNum");
        updt.add("newBankActFrm:addNmBtn");
        
        PrimeFaces.current().ajax().update(updt);
        //logger.log(INFO, "Branch sort code {0}", newBank.getBankAccount().getAccountForBranch().getSortCode());
        return;
       
      }
    }


  }

  public void onSortCodeBlur(){
    LOGGER.log(INFO, "onSortCodeBlur() sort code {0}",newBank.getBankAccount().getAccountForBranch().getSortCode());
    if(sortCodeEntry != null && sortCodeEntry.length() != 6){
      ResourceBundle sortCdErr = this.getErrorMessage();
      String Msg = sortCdErr.getString("bnkSortCodeLen");
      GenUtil.addErrorMessage(Msg);
      this.sortCodeEntry = null;
    }
  }
  
  public List<SortOrderRec> onSortOrderComplete(String input){
   LOGGER.log(INFO, "onSortOrderComplete called with {0}", input);
   List<SortOrderRec> retList;
   if(StringUtils.isBlank(input)){
    retList = buff.getSortOrders();
   }else{
    retList = buff.getSortOrdersByName(input);
   }
   return retList;
  }

  public void onBankBranchKeyUp(){
    LOGGER.log(INFO, "onBankBranchKeuUp called");
    sortCodeEntered = true;
    branchFound = false;
    bankAccountValid = false;
  }
  
  public void onBankBranchNewDlg(){
   LOGGER.log(INFO, "start onBankBrCrBtn");
   if(newBank == null){
    newBank = new ArBankAccountRec();
   }
   
   if(newBank.getBankAccount() == null){
    newBank.setBankAccount(new BankAccountRec());
   }
   if(newBank.getBankAccount().getAccountForBranch() == null){
    newBank.getBankAccount().setAccountForBranch(new BankBranchRec());
   }
   if(newBank.getBankAccount().getAccountForBranch().getBranchAddress() == null){
    newBank.getBankAccount().getAccountForBranch().setBranchAddress(new AddressRec());
   }
   
   LOGGER.log(INFO, "Branch for account {0}", newBank.getBankAccount().getAccountForBranch());
   
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("addBranchFrm");
   pf.executeScript("PF('addBnkBrWv').show()");
   //rCtx.update("newbankBrPg");
   //rCtx.execute("PF('addBnkBrWv').show()");
   
   
  }
  
  public void onNewBankAcntDlg(){
   LOGGER.log(INFO, "onNewBankAcntDlg");
   
   //newBnkAcnt = new BankAccountRec();
   newBank = new ArBankAccountRec();
   if(newBank.getBankAccount() == null){
    newBank.setBankAccount(new BankAccountRec());
   }
   if(newBank.getBankAccount().getAccountForBranch() == null){
    newBank.getBankAccount().setAccountForBranch(new BankBranchRec());
   }
   LOGGER.log(INFO, "new AR Bank a/c {0}", newBank);
   LOGGER.log(INFO, "TR Bank a/c{0}", newBank.getBankAccount());
   LOGGER.log(INFO, "Banch{0}", newBank.getBankAccount().getAccountForBranch());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("newBankActFrm");
   pf.executeScript("PF('addBnkAcntArWv').show()");
   
  }
  public void onNewContRespPersSel(SelectEvent evt){
   LOGGER.log(INFO, "onNewContRespPersSel called with {0}", ((PartnerPersonRec)evt.getObject()).getFamilyName());
   contactNew.setRespibilityOf((PartnerPersonRec)evt.getObject());
  }
  
  public void onAcntCrCompSel(SelectEvent evt){
   LOGGER.log(INFO, "onAcntCrCompSel called new comp {0}",evt.getObject());
   arAccount.setCompany((CompanyBasicRec)evt.getObject());
   PrimeFaces.current().ajax().update("arActCrFrm:compName");
  }
  
  public String onAcntCrFlow(FlowEvent evt){
   RequestContext rCtx = RequestContext.getCurrentInstance();
   String currStep = evt.getOldStep();
   String nextStep = evt.getNewStep();
   LOGGER.log(INFO,"curr {0} next {1}",new Object[]{currStep,nextStep});
   /*if(currStep.equals("ptnr") && nextStep.equals("account")){
    if(arAccount.getPaymentTerms() == null){
     arAccount.setPaymentTerms(this.getPayTermsList().get(0));
    }
    rCtx.update("actDetails:arAccountPanel");
    LOGGER.log(INFO, "payment terms {0}", arAccount.getPaymentTerms().getPayTermsCode());
    
   }*/
   if(currStep.equals("account") && nextStep.equals("bank") ){
    
   
    if(skipToEnd){
     nextStep ="summary";
    }
    
   }
   if(currStep.equals("summary") && nextStep.equals("bank") ){
    if(this.saveOk){
     nextStep = "ptnr";
     arAccount = null;
     selectedPtnrTypeCd = null;
     ptnrCorp = null;
     ptnrPerson = null;
     postCodeAddress = null;
     saveOk = false;
     
     rCtx.update("arActCrFrm");
    }
   }
   if(nextStep.equals("summary")){
    /*if(arAccount.getArAccountName() == null || arAccount.getArAccountName().isEmpty()){
     String acntName = selectedPtnrTypeCd.equals("indiv")?ptnrPerson.getNameStructured():ptnrCorp.getName();
     this.arAccount.setArAccountName(acntName);
    }*/
   }
   return nextStep;
  }
  
  public void onAcntCrNextStep(){
   LOGGER.log(INFO, "onAcntCrNextStep called step is {0}", getStep());
   int cs =getStep();
   int cs_old = cs;
   boolean blockNext = false;
   
   if(cs == AR_ACNT_NEW_ADDR){
    // check that defaults are set after leaving address
    LOGGER.log(INFO, "Ar recon acnts {0}", arAccount.getReconciliationAc().getCoaAccount().getRef());
    if(arAccount.getReconciliationAc().getCoaAccount().getRef() == null  ){
     List<FiGlAccountCompRec> glAcnts = getGlReconciliationAcs(arAccount.getCompany());
     LOGGER.log(INFO, "glAcnts {0}", glAcnts);
     LOGGER.log(INFO, "company {0}", arAccount.getCompany());
     if(glAcnts != null && !glAcnts.isEmpty()){
      if(arAccount.getReconciliationAc() != null){
       arAccount.setReconciliationAc(glAcnts.get(0));
      }
     }else{
      blockNext = true;
      
     }
    }
    if(arAccount.getSortOrder() == null){
     List<SortOrderRec> so = this.getSortOrderList();
     if(so != null && !so.isEmpty()){
      arAccount.setSortOrder(so.get(0));
     }else{
      blockNext = true;
      MessageUtil.addClientErrorMessage("arActCrFrm:msgs", "arAcntSoNone", "errorText");
     }
     
    }
    
    if(arAccount.getPaymentTerms() == null){
     List<PaymentTermsRec> pTerms = this.getPayTermsList();
     if(pTerms != null && !pTerms.isEmpty()){
      arAccount.setPaymentTerms(pTerms.get(0));
     }else{
      blockNext = true;
      MessageUtil.addClientErrorMessage("arActCrFrm:msgs", "arAcntPayTermNone", "errorText");
     }
    }
    
    if(arAccount.getPaymentType() == null){
     List<PaymentTypeRec> pType = this.getPayTypes();
     if(pType != null && !pType.isEmpty()){
      arAccount.setPaymentType(pType.get(0));
     }else{
      blockNext = true;
      MessageUtil.addClientErrorMessage("arActCrFrm:msgs", "arAcntPayTyNone", "errorText");
     }
     
     if(StringUtils.equals(selectedPtnrTypeCd, "corp")){
      arAccount.setArAccountFor(ptnrCorp);
     }else{
      arAccount.setArAccountFor(ptnrPerson);
     }
     LOGGER.log(INFO, "ac For {0}", arAccount.getArAccountFor());
    }
    if(blockNext){
    return;
   }
   LOGGER.log(INFO, "blockNext {0}", blockNext);
    PrimeFaces.current().ajax().update("arActCrFrm:arAccountPnl");
   }
   if(blockNext){
    return;
   }
   if(skipToEnd){
    cs = ArMastBean.AR_ACNT_NEW_STEP_MAX;
    skipToEnd = false;
   }
   if(cs < ArMastBean.AR_ACNT_NEW_STEP_MAX){
    cs++;
    
   }
   setStep(cs);
   LOGGER.log(INFO, "step now {0}",getStep());
   PrimeFaces.current().ajax().update("arActCrFrm");
  }
  
  public void onAcntCrPrevStep(){
   LOGGER.log(INFO, "onAcntCrPrevStep called step is {0}", getStep());
   int cs =getStep();
   if(cs > 0){
    cs--;
    setStep(cs);
   }
   LOGGER.log(INFO, "step now {0}",getStep());
   PrimeFaces.current().ajax().update("arActCrFrm");
  }
  
  public void onArAcntAddrEdDlg(){
   LOGGER.log(INFO, "onArAcntAddrEdDlg called");
   arAcntAddrEdit = arAccount.getAccountAddress();
   if(countries == null){
    countries = mdmMgr.getCountriesAll();
   }
   LOGGER.log(INFO, "arAcntAddrEdit.getCountry() {0}", arAcntAddrEdit.getCountry());
   if(arAcntAddrEdit.getCountry() == null){
    for(CountryRec cntry:countries){
     if(Objects.equals(cntry.getId(), arAccount.getCompany().getCountry().getId())){
      arAcntAddrEdit.setCountry(cntry);
      break;
     }
    }
   }
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("arAcntAddrEdPgId");
   rCtx.execute("PF('custAddrEdDlgWv').show();");
  }
  
  public void onArAcntAddrEdTransf(){
   LOGGER.log(INFO, "onArAcntAddrEdTransf called");
   arAcntAddrEdit = mdmMgr.addressUpdate(arAcntAddrEdit, this.getLoggedInUser(), getView());
   arAccount.setAccountAddress(arAcntAddrEdit);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("actDetails:addressPglId");
   rCtx.execute("PF('custAddrEdDlgWv').hide();");
  }
  
  public void onArAcntAddrNew(){
   arAcntAddrEdit = new AddressRec();
   if(countries == null){
    CountryRec cntry = arAccount.getCompany().getCountry();
    arAcntAddrEdit.setCountry(cntry);
   }else{
    for(CountryRec cntry:countries){
     if(Objects.equals(cntry.getId(), arAccount.getCompany().getCountry().getId())){
      arAcntAddrEdit.setCountry(cntry);
      break;
     }
    }
   }
   LOGGER.log(INFO, "Address country {0}", arAcntAddrEdit);
   LOGGER.log(INFO, "currView {0}", currView);
   if(StringUtils.equals(currView, "arActCr2")){
    List<String> updates = new ArrayList<>();
    arAccount.setAccountAddress(arAcntAddrEdit);
    arAcntAddrEdit = null;
    updates.add("arActCrFrm:addrStrPG");
    updates.add("arActCrFrm:addrRemPG");
    updates.add("arActCrFrm:addrPostCdPG");
    PrimeFaces.current().ajax().update(updates);
   }else{
    //RequestContext rCtx = RequestContext.getCurrentInstance();
    PrimeFaces.current().ajax().update("arAcntAddrEdPgId");
   }
  }
  public void onAcntChPayTermsValCh(ValueChangeEvent evt){
   PaymentTermsRec newTerms = (PaymentTermsRec)evt.getNewValue();
   arAccount.setPaymentTerms(newTerms);
   //RequestContext rCtx = RequestContext.getCurrentInstance();
   PrimeFaces.current().ajax().update("actDetails:arAccountPanel");
  }
  
  
  public void onAddressSearchByPostCd(){
   LOGGER.log(INFO, "onAddressSearchByPostCd called with post code {0}", this.postCodeEntry);
   addressList = mdmMgr.getAddressesForPostCodePart(postCodeEntry);
   if(addressList == null || addressList.isEmpty()){
    LOGGER.log(INFO, "No addresses found");
    
   }
   Collections.sort(addressList, new AddressByPostCode());
   PrimeFaces.current().ajax().update("addrSrchFrm:addrList");
   
  }
  
  public void onAddressSelectCr(SelectEvent evt){
   LOGGER.log(INFO, "onAddressSelectCr called with address  {0}", evt.getObject());
   arAccount.setAccountAddress((AddressRec)evt.getObject());
   List<String> updates = new ArrayList<>();
   updates.add("arActCrFrm:addrStrPG");
   updates.add("arActCrFrm:addrRemPG");
   updates.add("arActCrFrm:addrPostCdPG");
   PrimeFaces pf = PrimeFaces.current();
   pf.executeScript("PF('pstCdSrchDlgWv').hide()");
   pf.ajax().update(updates);
   
  }
  public void onAccountNameBlur(ValueChangeEvent evt){
   LOGGER.log(INFO, "evt {0}", evt.getNewValue());
   arAccount.setArAccountName((String)evt.getNewValue());
   LOGGER.log(INFO, "Account name {0}", arAccount.getArAccountName());
   PrimeFaces pf = PrimeFaces.current();
   if(StringUtils.equals(currView, "arActCr")){
    pf.ajax().update("summAcntName");
   }
   
  }
  
  public void onBankAcntNewRowDelete(){
   LOGGER.log(INFO, "selectedBank {0}", selectedBank);
   if(arAccount.getArAccountBanks() == null || arAccount.getArAccountBanks().isEmpty()){
    MessageUtil.addClientWarnMessage("arActCrFrm:bnkMsg", "arAcntRmNon", "errorText");
    PrimeFaces.current().ajax().update("arActCrFrm:bnkMsg");
    return;
   }
   ListIterator<ArBankAccountRec> li = arAccount.getArAccountBanks().listIterator();
   boolean found = false;
   while(li.hasNext() || !found){
    ArBankAccountRec curr = li.next();
    if(Objects.equals(curr.getId(), selectedBank.getId())){
     li.remove();
     found = true;
    }
   }
   if(found){
    PrimeFaces.current().ajax().update("arActCrFrm:bnkTbl");
   }
  }
  public void onBankAccountNumberKeyUp(){
    LOGGER.log(INFO,"onBankAccountNumnberKeyUp() Act Num: {0}",
           newBank.getBankAccount().getAccountNumber() );
    bankAccountEntered = true;
    bankAccountValid = false;
  }
  
  public void onBankAccountTrTransfer(){
   LOGGER.log(INFO,"onBankAccountTrTransfer called");
   //RequestContext rCtx = RequestContext.getCurrentInstance();
   PrimeFaces pf = PrimeFaces.current();
   newBank.getBankAccount().setCreatedBy(this.getLoggedInUser());
   newBank.getBankAccount().setCreatedOn(new Date());
   BankAccountRec bnkAc = newBank.getBankAccount();
   LOGGER.log(INFO, "Branch {0}", bnkAc.getAccountForBranch());
   bnkAc = this.bankMgr.updateBankAccount(bnkAc, this.getView());
   if(bnkAc.getId() == null){
    MessageUtil.addClientWarnMessage("newBankActFrm:bnkAcCrMsg", "bnkAcCr", "errorText"); 
    pf.ajax().update("newBankActFrm:bnkAcCrMsg");
    return;
   }
   newBank.setBankAccount(bnkAc);
   
   //newBnkAcnt.setId(1l);
   branchFound = true;
   pf.ajax().update("newBankActFrm:addNewBnkAcntNum");
   pf.executeScript("PF('addNewBnkAcntTrWv').hide()");
   MessageUtil.addClientInfoMessage("newBankActFrm:bnkAcCrMsg", "bnkActCr", "blacResponse");
   pf.ajax().update("newBankActFrm:bnkAcCrMsg");
   // rCtx.execute("PF('addNewBnkAcntTrWv').hide()");
    //MessageUtil.addInfoMessage("bnkActCr", "blacResponse");
    //rCtx.update("newbankBrPg");
    
  }
  
  public void onTradingNameBlur(){
    LOGGER.log(INFO, "onTradingName Blur trading name: ");


  }

  public void onPtnrSelEdit(SelectEvent evt){
   Object selObj = evt.getObject();
   PartnerBaseRec ptnr = (PartnerBaseRec)selObj;
   arAccountList = this.arManager.getAccountsForPartner(ptnr);
   if(arAccountList == null || arAccountList.isEmpty()){
    MessageUtil.addWarnMessage("noAcntsAr", "validationText");
    partnerSelected = false;
   }else{
    partnerSelected = true;
    
   }
   RequestContext rCtx = RequestContext.getCurrentInstance();
   if(arAccountSelected){
    arAccountSelected = false;
    rCtx.update("actDetOp");
   }
   this.arAccount = null;
   rCtx.update("actCd");
  }
  
  public List<PaymentTermsRec> onPayTermsComplete(String input){
   LOGGER.log(INFO, "onPayTermsComplete called with {0}", input);
   
   List<PaymentTermsRec> pTerms = buff.getPaymentTermsList();
   Collections.sort(pTerms, new PaymentTermsByCode());
   if(pTerms == null || pTerms.isEmpty()){
    return null;
   }
   if(StringUtils.isBlank(input)){
    return pTerms; 
   } else{
    List<PaymentTermsRec> retList = new ArrayList<>();
    for(PaymentTermsRec p:pTerms){
     if(StringUtils.startsWithIgnoreCase(p.getPayTermsCode(), input)){
      retList.add(p);
     }
    }
    return retList;
   }
  }
  
  public List<PaymentTypeRec> onPayTypeComplete(String input){
   LOGGER.log(INFO, "onPayTypeComplete called with {0}", input);
   this.getPayTypes();
   if(payTypes == null || payTypes.isEmpty()){
    return null;
   }
   if(StringUtils.isBlank(input)){
    return payTypes;
   }else{
    List<PaymentTypeRec> retList = new ArrayList<>();
    for(PaymentTypeRec p:payTypes){
     if(StringUtils.startsWithIgnoreCase(p.getDescription(), input)){
      retList.add(p);
     }
    }
    return retList;
   }
         
  }
  
  public List<PartnerPersonRec> onPartnerPersonComplete(String input){
   LOGGER.log(INFO, "onPartnerPersonComplete {0}", input);
   
   if(StringUtils.isBlank(input)){
    List<PartnerPersonRec> partners = this.mdmMgr.getAllPartnerIndividual();
    return partners;
   }else{
    String sName = StringUtils.substringBefore(input, ",");
    sName = StringUtils.strip(sName);
    LOGGER.log(INFO, "sName is {0}", sName);
    List<PartnerPersonRec> partners = mdmMgr.getPersonPartnersByName(sName);
    return partners;
   }
  }
  
  
  
  


  public void onCustEditBtn(){
   LOGGER.log(INFO,"onCustEditBtn called with edit {0}",custEdit);
   arAccountEdit = arAccount;
   ptnrPersonEdit = ptnrPerson;
   ptnrCorpEdit = ptnrCorp;
   rolesAvailable = this.buff.getPartnerRoles();
   if(selectedPtnrTypeCd.equals("corp")){
    rolesAssigned = ptnrCorpEdit.getPartnerRoles();
   }else{
    rolesAssigned = ptnrPersonEdit.getPartnerRoles();
   }
   if(rolesAssigned == null){
    rolesAssigned = new ArrayList<>();
   }
  
    
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("custEdFrm");
   rCtx.execute("PF('CustEditWv').show()");
  /* if(selectedPtnrTypeCd.equalsIgnoreCase("corp")){
    rCtx.update("ptnrCorpName");
   }else{
    rCtx.update("custIndivPnlId");
   }*/
   LOGGER.log(INFO,"end with edit {0}",custEdit);
   }
  
  public void onCustEditTabCh(TabChangeEvent evt){
   LOGGER.log(INFO, "Current tab {0}", evt.getTab().getId());
   if(evt.getTab().getId().equals("roleTab")){
    rolesAvailable = buff.getPartnerRoles();
    rolesAssigned = this.arAccount.getArAccountFor().getPartnerRoles();
    if(rolesAssigned == null){
     rolesAssigned = new ArrayList<>();
    }
    LOGGER.log(INFO, "rolesAvailable {0}", rolesAvailable);
    custRoles.setSource(rolesAvailable);
    custRoles.setTarget(rolesAssigned);
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("custEdAP:roleTab");
   }
  }
  
  public void onCustEditRoleTrf(TransferEvent evt){
   LOGGER.log(INFO, "All transferred {0}", evt.getItems());
   LOGGER.log(INFO, "added {0}", evt.isAdd());
   LOGGER.log(INFO, "Removed {0}", evt.isRemove());

  }
  public void onCustEditTrf(){
   LOGGER.log(INFO,"onCustEditTrf {0}",custEdit);
   arAccount = arAccountEdit;
   ptnrPerson = ptnrPersonEdit;
   ptnrCorp = ptnrCorpEdit ;
   if(selectedPtnrTypeCd.equalsIgnoreCase("indiv")){
    
    arAccount.setArAccountFor(ptnrPerson);
    if(ptnrCorp != null){
     ptnrCorp = null;
    }
   }else{
    
    arAccount.setArAccountFor(ptnrCorp);
    if(ptnrPerson != null){
     ptnrPerson = null;
    }
   }
   PartnerBaseRec custPtnrBase = arAccount.getArAccountFor();
   custPtnrBase.setChangedBy(this.getLoggedInUser());
   custPtnrBase.setChangedOn(new Date());
   custPtnrBase.setPartnerRoles(custRoles.getTarget());
   ptnrMgr.updatePartner(custPtnrBase, getView());
   arAccount.setArAccountFor(custPtnrBase);
   
   LOGGER.log(INFO, "Updated Partner");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("actDetails");
   rCtx.execute("PF('CustEditWv').hide();");
  }
  
  public void onCustEditBankAddBtn(){ 
   LOGGER.log(INFO, "onCustEditBankAdd called");
   bankAccountSelected = false;
   bankAccountValid = false;
   newBank = new ArBankAccountRec();
   newBank.setBankAccount(new BankAccountRec());
   newBank.getBankAccount().setAccountForBranch(new BankBranchRec());
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("custBnkFrm");
   rCtx.execute("PF('addArBnkWVar').show();");
  
  }
  
  public void onCustSelBtn(){
   
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.execute("PF('custSelWv').show()");
   custSelOpts = new PartnerSelectOption();
  }
  
  public void onCustSelEvt(SelectEvent evt){
   LOGGER.log(INFO, "onCustSelEvt evt {0}", evt.getObject());
   if(evt.getObject().getClass().getSimpleName().equals("PartnerIndividualRec")){
    ptnrPerson = (PartnerPersonRec)evt.getObject();
   }else{
    ptnrCorp = (PartnerCorporateRec)evt.getObject();
   }
   
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("actDetOp");
   rCtx.execute("PF('custSelWv').hide()");
  }
  public void onCustSrchBtn(){
   LOGGER.log(INFO, "onCustSrchBtn called");
   custSrchList = this.mdmMgr.getPartnersBySelOpt(custSelOpts);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("selPtnrTbl");
  }

  public void onCharityValueChange(ValueChangeEvent evt){
    LOGGER.log(INFO, "onCharityValueChange called with new value {0}",evt.getNewValue());
  }
  
  public void onClerkAddBtn(){
   
   LOGGER.log(INFO, "onClerkAddBtn called");
   ptnrPersonNew = new PartnerPersonRec();
   PrimeFaces pf = PrimeFaces.current();
   
   pf.ajax().update("addClerkFrm");
   pf.executeScript("PF('clerkAddDlgWv').show()");
  }
  
  public void onClerkTrf(){
   LOGGER.log(INFO, "onClerkTrf called");
   PartnerRoleRec clerkRole = this.buff.getPartnerRoleByCode("AR_CLRK");
   LOGGER.log(INFO, "clerk role {0}", clerkRole);
   List<PartnerRoleRec> roles = new ArrayList<>();
   roles.add(clerkRole);
   ptnrPersonNew.setPartnerRoles(roles);
   ptnrPersonNew.setCreatedDate(new Date());
   ptnrPersonNew.setCreatedBy(this.getLoggedInUser());
   ptnrPersonNew = (PartnerPersonRec)mdmMgr.updatePartner(ptnrPersonNew, this.getView());
   arAccount.setAccountClerk(ptnrPersonNew);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("arActCrFrm");
   
   pf.executeScript("PF('clerkAddDlgWv').hide();");
   
   
   
  }

  public void onContactAddDlg(){
   contactNew = new ContactRec();
   if(contactRoleList == null || contactRoleList.isEmpty()){
    contactRoleList = buff.getContactRoles();
   }
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("contactAddFrm");
   rCtx.execute("PF('contAddWv').show()");
  }
  
  public void onContactNewTrf(){
   LOGGER.log(INFO, "onContactNewTrf called");
   List<ContactRec> contList;
   if(arAccount.getContacts() == null){
    contList = new ArrayList<>();
   }else{
    contList = arAccount.getContacts();
   }
   contactNew.setCreatedOn(new Date());
   contactNew.setCreatedBy(this.getLoggedInUser());
   contList.add(contactNew);
   arAccount.setContacts(contList);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("actDetails:contactsTbl");
   rCtx.execute("PF('contAddWv').hide()");
  }
  public void onContAttachUpload(FileUploadEvent evt){
   LOGGER.log(INFO, "onContAttachUpload called");
   UploadedFile file = evt.getFile();
   contactNew.setAttFile(file.getContents());
   contactNew.setAttFileType(file.getContentType());
   MessageUtil.addInfoMessage("contAttLoaded", "blacResponse");
   
  }
  public void onDdUpload(FileUploadEvent evt){
   UploadedFile file = evt.getFile();
   LOGGER.log(INFO, "File type {0}", file.getContentType());
   
   newBank.setSignedDD(file.getContents()); 
   newBank.setDdFileName(file.getFileName());
   newBank.setDdFileType(file.getContentType());
   LOGGER.log(INFO, "DD file size {0}", newBank.getSignedDD().length);
   this.responseForKey("trDdUploaded");
   MessageUtil.addInfoMessage("trDdUploaded", "blacResponse");
   
  }
  public void onPtnrTypeValueChange(ValueChangeEvent evt){
    LOGGER.log(INFO, "onPtnrTypeValueChange called with new value {0}",evt.getNewValue());
  }
  
  public List<FiGlAccountCompRec> onReconciliationAcComplete(String input){
   List<FiGlAccountCompRec> acntList = glAcMgr.getDebtorReconciliationAcs(arAccount.getCompany());
   if(StringUtils.isBlank(input)){
    return acntList;
   }
   if(acntList == null || acntList.isEmpty()){
    return null;
   }
   List<FiGlAccountCompRec> retList = new ArrayList<>();
   for(FiGlAccountCompRec c:acntList){
    if(StringUtils.startsWith(c.getCoaAccount().getRef(), input)){
     retList.add(c);
    }
   }
   
   
   return retList;
  }
  public void onBnkActAddDlg(){
   LOGGER.log(INFO, "onBnkActAddDlg called");
   //newBank = new ArBankAccountRec();
   newBnkAcnt = new BankAccountRec();
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("newBankActTrFrm");
   pf.executeScript("PF('addNewBnkAcntTrWv').show();");
   
   
  }
  
  public void onBankAcntNewDlg(){
   LOGGER.log(INFO, "onNewBankAcntDlg");
   //newBnkAcnt = new BankAccountRec();
   newBank = new ArBankAccountRec();
   if(newBank.getBankAccount() == null ){
    newBank.setBankAccount(new BankAccountRec());
   }
   
   if(newBank.getBankAccount().getAccountForBranch() == null){
    newBank.getBankAccount().setAccountForBranch(new BankBranchRec());
   }
   
   LOGGER.log(INFO, "AR bank a/c {0}", newBank);
   LOGGER.log(INFO, "GL bank a/c {0}", newBank.getBankAccount());
   LOGGER.log(INFO, "Branch {0}", newBank.getBankAccount().getAccountForBranch());
   
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("newBankActFrm");
   pf.executeScript("PF('addBnkAcntArWv').show()");
   
  }
  public void onBnkAcntAutoCr(){
   LOGGER.log(INFO, "onBnkAcntAutoCr");
   bnkVal = new BankValidation();
   RequestContext rCtx = RequestContext.getCurrentInstance();
   try{
   newBnkAcnt = bnkVal.validateBank(newBnkAcnt.getAccountForBranch().getSortCode(),
     newBnkAcnt.getAccountNumber());
    this.bankAccountValid = true;
    newBankBranch = newBnkAcnt.getAccountForBranch();
    LOGGER.log(INFO, "newBnkAcnt num {0} sort {1}", new Object[]{newBnkAcnt.getAccountNumber(),
     newBnkAcnt.getAccountForBranch().getSortCode()
    });
    UserRec currUser = this.getLoggedInUser();
    Date currDate = new Date();
    if(newBnkAcnt.getId() == null){
     newBnkAcnt.setCreatedBy(currUser);
     newBnkAcnt.setCreatedOn(currDate);
    }else{
     newBnkAcnt.setUpdatedBy(currUser);
     newBnkAcnt.setUpdatedOn(currDate);
    }
    if(newBnkAcnt.getAccountForBranch().getId() == null){
     newBnkAcnt.getAccountForBranch().setCreatedBy(currUser);
     newBnkAcnt.getAccountForBranch().setCreatedOn(currDate);
    }else{
     newBnkAcnt.getAccountForBranch().setUpdatedBy(currUser);
     newBnkAcnt.getAccountForBranch().setUpdatedOn(currDate);
    }
    if(newBnkAcnt.getAccountForBranch().getBank().getId() == null){
     newBnkAcnt.getAccountForBranch().getBank().setCreatedBy(currUser);
     newBnkAcnt.getAccountForBranch().getBank().setCreatedOn(currDate);
    }else{
     newBnkAcnt.getAccountForBranch().getBank().setUpdatedBy(currUser);
     newBnkAcnt.getAccountForBranch().getBank().setUpdatedOn(currDate);
    }
    if(newBnkAcnt.getAccountForBranch().getBank().getBankOrganisation().getId() == null){
     newBnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setCreatedBy(currUser);
     newBnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setCreatedDate(currDate);
    }else{
     newBnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setChangedBy(currUser);
     newBnkAcnt.getAccountForBranch().getBank().getBankOrganisation().setChangedOn(currDate);
    }
    LOGGER.log(INFO, "Bank account created by {0}", newBnkAcnt.getId());
    LOGGER.log(INFO, "Bank branch created by {0}", newBnkAcnt.getAccountForBranch().getId());
    LOGGER.log(INFO, "Bank created by {0}", newBnkAcnt.getAccountForBranch().getBank().getId());
    newBnkAcnt = bankMgr.bankAccountUpdate(newBnkAcnt, this.getLoggedInUser(), getView(), true);
    
    branchFound = true;
    
    rCtx.execute("PF('bnkAcntAutoWv').hide()");
    MessageUtil.addInfoMessage("bnkCrVal", "blacResponse");
    rCtx.update("newbankBrPg");
    rCtx.update("arAcntCrGr");
   }catch(BacException ex){
    MessageUtil.addErrorMessage("bnkDetInval", "errorText", ex.getMessage());
    rCtx.update("autoCrMsg");
   }
    
   LOGGER.log(INFO, "newBnkAcnt sort {0}", newBnkAcnt.getAccountForBranch().getSortCode());
   LOGGER.log(INFO, "newBnkAcnt account num {0}", newBnkAcnt.getAccountNumber());
  }

  public void createCorpPartnerBtnListner(){
    LOGGER.log(INFO, "createCorpPartnerBtnListner called");
    this.newPartnerCorp = true;
    UserRec usr = this.getLoggedInUser();

    Long partnerId = this.ptnrMgr.createCorporatePartnerAR(this.ptnrCorp, usr,getView());
    this.ptnrCorp.setId(partnerId);

  }

  public void createInidivualPartnrBtnListner(){
    LOGGER.log(INFO, "createCorpPartnerBtnListner called");
    newPartnerIndiv = true;
    UserRec usr = this.getLoggedInUser();
    Long partnerId = this.ptnrMgr.createCorporatePartnerAR(this.ptnrCorp, usr, getView());
    this.ptnrPerson.setId(partnerId);
    LOGGER.log(INFO, "Created party with id: {0}",partnerId);
  }

  public void findAddressBtnListner(){
    LOGGER.log(INFO, "findAddressBtnListner called");
  }

  public List<ArAccountRec> arAccountComplete(String input){
   List<ArAccountRec> acList;
   acList = this.arManager.getAccountsByActNumberPart(input);
   Collections.sort(acList, new ArAccountByRef());
   LOGGER.log(INFO, "arManager.getAccountsByActNumberPart returns", acList);
   return acList;
  }
  
  public List<ArAccountRec> arAccountCompleteEdit(String input){
   if(arAccountList == null || arAccountList.isEmpty()){
    LOGGER.log(INFO, "No AR accounts");
    MessageUtil.addWarnMessage("arAcntsNone", "validationText");
   }
   if(input == null || input.isEmpty()){
    return this.arAccountList;
   }
   
   List<ArAccountRec> acList = new ArrayList<>();
   for(ArAccountRec curr:arAccountList){
    if(curr.getArAccountCode().startsWith(input)){
     acList.add(curr);
    }
   }
   if(acList.isEmpty()){
    return null;
   }else{
    return acList;
   }
   
  }
  
  public void onArAccountSelect(SelectEvent evt){
   arAccount = (ArAccountRec)evt.getObject();
   LOGGER.log(INFO, "arAccount recon ac {0}", arAccount.getReconciliationAc().getId());
   arAccountSelected = true;
   getGlReconciliationAcs(arAccount.getCompany());
   if(arAccount.getArAccountFor().getClass().getCanonicalName().endsWith("PartnerCorporateRec")){
    selectedPtnrTypeCd="corp";
    ptnrCorp = (PartnerCorporateRec)arAccount.getArAccountFor();
    if(ptnrCorp.getDefaultAddress() == null){
     ptnrCorp.setDefaultAddress(new AddressRec());
    }
   }else{
    selectedPtnrTypeCd="indiv";
    this.ptnrPerson = (PartnerPersonRec)arAccount.getArAccountFor();
   }
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("actDetOp");
   LOGGER.log(INFO, "arAccountSelect complete");
   
  }
  
  public void arActNumberListener(){
    boolean accountExists = this.arManager.arAccountExistsByActNumber(arAccount);
    if(accountExists){
     MessageUtil.addErrorMessage("arAccountNumUnique", "validationText");
     arAccountEntered = false;
     arAccount.setArAccountCode(null);
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("accountCode");
      
    }else{
      arAccountEntered = true;
    }

  }
  
public List<PartnerPersonRec> arAcntClerkComplete(String input){
 List<PartnerPersonRec> clerks;
 if(input == null){
  input = new String();
 }
 LOGGER.log(INFO, "ptnrMgr.getIndivPtnrsBySurname called with name {0}", input);
 clerks = ptnrMgr.getIndivPtnrsBySurname(input);
 ListIterator<PartnerPersonRec> clerksLi = clerks.listIterator();
 while(clerksLi.hasNext()){
  PartnerPersonRec cl = clerksLi.next();
  if(cl.getFamilyName() == null ||cl.getFamilyName().isEmpty() ){
   clerksLi.remove();
  }
 }
 LOGGER.log(INFO, "arAcntClerkComplete returns clerks {0}", clerks);
 
 return clerks;
}
  
public void actionCreateCorpPtnr(){
  LOGGER.log(INFO, "actionCreateCorpPtnr");
}
  public void onCompanySelectValueChange(ValueChangeEvent evt){
    LOGGER.log(INFO, "company Value changed to: {0} ",evt.getNewValue());
    comp = (CompanyBasicRec)evt.getNewValue();
    selectedCompanyId = comp.getId();
    LOGGER.log(INFO, "selectedCompanyId changed to: {0} ",selectedCompanyId);

  }
 public List<CountryRec> onCountryComplete(String input){
  LOGGER.log(INFO, "onCountryComplete called with {0}", input);
  
  List<CountryRec> retList ;
  if(input == null || input.isEmpty()){
    retList = mdmMgr.getCountriesAll();
  }else{
   retList = mdmMgr.getCountriesByName(input);
  }
 return retList;
  
 }

  public List<BankBranchRec> sortCodeComplete(String inputValue){
   LOGGER.log(INFO, "sortCodeComplete called with {0}",inputValue);
   
   RequestContext rCtx = RequestContext.getCurrentInstance();
   String baseInput = inputValue;
   List<BankBranchRec> retList = new ArrayList<>();
   if(inputValue == null || inputValue.isEmpty()){
     inputValue = "%";
   }else{
     inputValue = inputValue + "%";
     branchFound = false;
     sortCodeEntered = true;
     List<String> updt = new ArrayList<>();
     updt.add("brDescrOp");
     updt.add("createBranchBtn");
     updt.add("arValidateBnkBtnId");
     rCtx.update(updt);
   }
   LOGGER.log(INFO, "bankBranchList {0}", bankBranchList);
   if(bankBranchList == null || bankBranchList.isEmpty()){
    bankBranchList = bankMgr.getBanchesBranchBySortCode(inputValue);
    LOGGER.log(INFO, "bankBranchList  returned from {0}", bankBranchList);
    
    LOGGER.log(INFO, "sortCodeEntered {0} bankAccountEntered {1}", new Object[]{sortCodeEntered, bankAccountEntered});
    rCtx.update("createBranchBtn");
    return bankBranchList;
   }
   boolean brFound = false;
   for(BankBranchRec curr: bankBranchList){
    if(curr.getSortCode().startsWith(baseInput)){
     retList.add(curr);
     brFound = true;  
    }
   }
   if(!brFound){
    bankBranchList = this.bankMgr.getBanchesBranchBySortCode(inputValue);
    return bankBranchList;
   }
   LOGGER.log(INFO, "List to return {0}", retList);
   return retList ;
  }
  

  public void accountNumberValueChange(ValueChangeEvent evt){
    LOGGER.log(INFO, "Account number  Value changed to: {0} ",evt.getNewValue());
    this.newBank.getBankAccount().setAccountNumber((String)evt.getNewValue());
    this.accountNumberEntered = true;

  }

  
  
  public void partnerTypeValueChange(ValueChangeEvent evt){
   selectedPtnrTypeCd = (String)evt.getNewValue();
  }



public void itemSelectAgedBalance(ItemSelectEvent evt) throws IOException{
  LOGGER.log(INFO, "itemSelectAgedBalance called with item index:{0}",
          new Object[]{evt.getItemIndex(),evt.getSeriesIndex()});
  FacesContext.getCurrentInstance().getExternalContext().redirect(selectedPtnrTypeCd);
  FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation
          (FacesContext.getCurrentInstance(), null, "home");
}

public void addBankValidateBtnListener(){
  LOGGER.log(INFO, "addBankValidateBtnListener");
  String sortCode = newBank.getBankAccount().getAccountForBranch().getSortCode();
  String accountNumber = newBank.getBankAccount().getAccountNumber();
  
    BankValidation bankValWeb = new BankValidation();
    try{
    BankAccountRec bnk = bankValWeb.validateBank(sortCode,accountNumber);
    LOGGER.log(INFO, "bank Account returned from validate Account num {0} sort order {1} branch name {2}",
            new Object[]{bnk.getAccountNumber(),bnk.getAccountForBranch().getSortCode(),
            bnk.getAccountForBranch().getBranchName()
    });
    LOGGER.log(INFO, "call bankMgr.createBankAccount with {0} ",bnk);
    UserRec usr = this.getLoggedInUser();
    Date currDate = new Date();
    if(bnk.getId() == null){
     bnk.setCreatedBy(usr);
     bnk.setCreatedOn(currDate);
    }else{
     bnk.setUpdatedBy(usr);
     bnk.setUpdatedOn(currDate);
    }
    if(bnk.getAccountForBranch().getId() == null){
     bnk.getAccountForBranch().setCreatedBy(usr);
     bnk.getAccountForBranch().setCreatedOn(currDate);
    }else{
     bnk.getAccountForBranch().setUpdatedBy(usr);
     bnk.getAccountForBranch().setUpdatedOn(currDate);
    }
    if(bnk.getAccountForBranch().getBank().getId() == null){
     bnk.getAccountForBranch().getBank().setCreatedBy(usr);
     bnk.getAccountForBranch().getBank().setCreatedOn(currDate);
    }else{
     bnk.getAccountForBranch().getBank().setUpdatedBy(usr);
     bnk.getAccountForBranch().getBank().setUpdatedOn(currDate);
    }
    LOGGER.log(INFO, "call bankMgr.createBankAccount bnk");
    bnk = bankMgr.bankAccountUpdate(bnk, this.getLoggedInUser(),getView(), true);
    LOGGER.log(INFO, "After call bankMgr.createBankAccount bnk:  ",bnk);
    
    newBank.setBankAccount(bnk);
    this.bankAccountValid = true;

    /*ArrayList<ArBankRec> bnkList = new ArrayList<ArBankRec>();
     if(arAccount.getArAccountBanks() == null){
      arAccount.setArAccountBanks(bnkList);
    }
     arAccount.getArAccountBanks().add(newBank);

*/
  }catch(BacException e){
    LOGGER.log(INFO, "Bank account Validation failed :{0} ",e.getLocalizedMessage());
    String errTxt;
    if(e.getLocalizedMessage().startsWith("INVALID - Modulus Check Failed")){
      LOGGER.log(INFO, "Account number error");
      //errTxt = this.getvalidationText().getString("bnkActNum");
      errTxt = "Create bank error" + e;
      GenUtil.addErrorMessage(accountNumber+ " - "+errTxt );
      return;
    }
  }

 LOGGER.log(INFO, "End addBankValidateBtnListener");

}

public void addBankActToAr(){
 LOGGER.log(INFO,"addArBank called");
 if(this.bankAccountValid){
  LOGGER.log(INFO, "Add bank {0}", newBank.getBankAccount().getAccountNumber());
  List<ArBankAccountRec> bankList = arAccount.getArAccountBanks();
  if(bankList == null){
   bankList = new ArrayList<>();
  }
   bankList.add(newBank);
   arAccount.setArAccountBanks(bankList);
   LOGGER.log(INFO, "AR Number of banks is now {0}",arAccount.getArAccountBanks().size() );
  newBank = null;
  ResourceBundle formText = this.getFormText();
  String msg = formText.getString("arBankAdded");
  GenUtil.addInfoMessage(msg);
 }
 
}

public void deleteNewBankAction(){
 LOGGER.log(INFO, "deleteNewBank called with {0} ",selectedBank.getBankAccount().getAccountNumber());
 ListIterator<ArBankAccountRec> li = this.arAccount.getArAccountBanks().listIterator() ;
 boolean found = false;
 while(li.hasNext() && !found){
  ArBankAccountRec bnk = li.next();
  if(bnk == selectedBank ){
   LOGGER.log(INFO, "Delete bank {0} from AR account", selectedBank.getBankAccount().getAccountNumber());
   li.remove();
   found = true;
   String msg = getFormText().getString("arBankDelete");
   GenUtil.addInfoMessage(msg);
   
  }
 }
 
 
}

public void addBankBranchBtnListener(){
 LOGGER.log(INFO, "addBankBranchBtnListener called branch details: sort code {0} branch name {1}",
         new Object[]{newBank.getBankAccount().getAccountForBranch().getSortCode(),
         newBank.getBankAccount().getAccountForBranch().getBranchName()});
LOGGER.log(INFO, "bank for branch id {0}",newBank.getBankAccount().getAccountForBranch().getBank().getId());
try{
BankBranchRec br = this.bankMgr.createBankBranch(newBank.getBankAccount().getAccountForBranch(),
        this.getLoggedInUser(),getView());
newBank.getBankAccount().setAccountForBranch(br);
LOGGER.log(INFO, "End addBankBranchBtnListener branch id {0}", br.getId());
  }catch(EJBException ex){
    GenUtil.addErrorMessage("Could not create bank branch");
  }
}

public void addBankBtnListener(){
  LOGGER.log(INFO, "addBankBranchBtnListener called");
  UserRec usr = this.getLoggedInUser();
  bankMgr.createBank(this.newBank.getBankAccount().getAccountForBranch().getBank(),usr,getView());
}

public void addrSearchBtnListener(){
  LOGGER.log(INFO, "addrSearchBtnListener called");
}
public void addArBankBtnListener(){
  LOGGER.log(INFO, "addArBankBtnListener");
  if(newBank.getAccountName() == null || newBank.getAccountName().isEmpty()){
    ResourceBundle errBundle = this.getErrorMessage();
    String msg = errBundle.getString("bnkActName");
    
    GenUtil.addErrorMessage(msg);
    return;
  }
  
  
  LOGGER.log(INFO, "addArBankBtnListener AR banks {0}",arAccount.getArAccountBanks());
  LOGGER.log(INFO, "bank account id: {0} branch id {1}",
          new Object[]{newBank.getBankAccount().getId(),newBank.getBankAccount().getAccountForBranch().getId()});
  if(newBank.getBankAccount().getId() == null){
    GenUtil.addInfoMessage("need to create bank account");
    UserRec usr = this.getLoggedInUser();
    Date currDate = new Date();
    try{
     if(newBank.getId() == null){
      newBank.setCreatedBy(usr);
      newBank.setCreatedOn(currDate);
     }else{
      newBank.setChangedBy(usr);
      newBank.setChangedOn(currDate);
     }
     BankRec bnk = newBank.getBank();
     if(bnk.getId() == null){
      bnk.setCreatedBy(usr);
      bnk.setCreatedOn(currDate);
     }else{
      bnk.setUpdatedBy(usr);
      bnk.setUpdatedOn(currDate);
     }
     newBank.setBank(bnk);
     BankAccountRec bnkAcnt = newBank.getBankAccount();
     if(bnkAcnt.getId() == null){
      bnkAcnt.setCreatedBy(usr);
      bnkAcnt.setCreatedOn(currDate);
     }else{
      bnkAcnt.setUpdatedBy(usr);
      bnkAcnt.setUpdatedOn(currDate);
     }
     newBank.setBankAccount(bnkAcnt);
     LOGGER.log(INFO, "Bank created by {0} account created by {1}", new Object[]{
      newBank.getBank().getCreatedBy(),newBank.getBankAccount().getCreatedBy()});
    newBank.setBankAccount(bankMgr.bankAccountUpdate(newBank.getBankAccount(),usr,getView(),false));
    this.bankAccountValid = true;
    }catch(Exception ex){
      GenUtil.addErrorMessage("Error creating bank account: "+ex.getLocalizedMessage());
    }
    
    GenUtil.addInfoMessage("Bank Account created with id: "+newBank.getBankAccount().getId());
  }
  //arAccount.getArAccountBanks().add(newBank);
  //newBank = null;
  LOGGER.log(INFO, "addArBankBtnListener AR banks {0}",arAccount.getArAccountBanks().size());

}

public List<BankRec> bankComplete(String input){
  LOGGER.log(INFO, "bankComplete called with {0}",input);
  List<BankRec> returnList = new ArrayList<>();
  List<BankRec> bnkList = bankMgr.getBanks();
  LOGGER.log(INFO, "bnkList from bnk<ge {0}", bnkList);
  if(bnkList == null || bnkList.isEmpty()){
   return null;
  }
  if(input == null || input.isEmpty()){
   return bnkList;
  } else{
   for(BankRec curr:bnkList){
    if(curr.getBankCode().startsWith(input)){
     returnList.add(curr);
    }
   }
  }
  LOGGER.log(INFO,"No banks found returned {0}",returnList);
  return returnList;
   
 

  
  
}

public List<BankRec> onBankComplete(String input){
 LOGGER.log(INFO, "onBankComplete called with {0}", input);
 List<BankRec> bankList;
 if(StringUtils.isBlank(input)){
  bankList = bankMgr.getBanks();
  
 }else{
  String bankCode = StringUtils.remove(input, "%");
  bankCode = bankCode + "%";
  bankList = bankMgr.getBanksByCode(bankCode);
 }
 if(bankList != null){
  for(BankRec b:bankList){
   LOGGER.log(INFO, "Bank {0}", b);
   LOGGER.log(INFO, "Bank organ {0}", b.getBankOrganisation());
  }
 }
 return bankList;
}

public void bankSelect(SelectEvent event) {
  LOGGER.log(INFO, "bankSelect called with evt object {0}", event.getObject());
  
}

public void onBankOrgSelect(SelectEvent event) {
  LOGGER.log(INFO, "onBankOrgSelect called with evt object {0}", event.getObject());
  String bnkCd = (String)event.getObject();
  BankRec bnk = this.bankMgr.getBankByBankCode(bnkCd);
  newBank.getBankAccount().getAccountForBranch().setBankOrg(bnk);
  LOGGER.log(INFO, "Bank is set to bank code {0} bank name {1} bank id {2}",
          new Object[]{newBank.getBankAccount().getAccountForBranch().getBank().getBankCode(),
          newBank.getBankAccount().getAccountForBranch().getBank().getBankOrganisation().getTradingName(),
          newBank.getBankAccount().getAccountForBranch().getBank().getId()});

  this.bankSelected = true;

}

public void onBankSave(){
 LOGGER.log(INFO, "onBankSave");
 UserRec crUsr = getLoggedInUser();
 Date crDate = new Date();
 newBank.getBankAccount().getAccountForBranch().getBank().getBankOrganisation().setCreatedBy(crUsr);
 newBank.getBankAccount().getAccountForBranch().getBank().getBankOrganisation().setCreatedDate(crDate);
 newBank.getBankAccount().getAccountForBranch().getBank().setCreatedBy(crUsr);
 newBank.getBankAccount().getAccountForBranch().getBank().setCreatedOn(crDate);
 newBank.getBankAccount().getAccountForBranch().setBank(bankMgr.updateBank(newBank.getBankAccount().getAccountForBranch().getBank(), getView()));
 if(newBank.getBankAccount().getAccountForBranch().getBank().getId() != null){
  PrimeFaces.current().executeScript("PF('newBankDlgWv').hide();");
 }else{
  MessageUtil.addClientWarnMessage("newBankFrm:msgs", "bnkCr", "errorText");
 }
 
}

public void onBankNewBtn(){
 LOGGER.log(INFO, "onBankNewBtn called");
 LOGGER.log(INFO, "arMastBean.newBank.bankAccount.accountForBranch {0}", newBank.getBankAccount().getAccountForBranch());
 LOGGER.log(INFO, "arMastBean.newBank.bankAccount.accountForBranch.bank {0}", newBank.getBankAccount().getAccountForBranch().getBank());
 if(newBank.getBankAccount().getAccountForBranch().getBank().getBankOrganisation() == null){
  PartnerCorporateRec bnkPartner = new PartnerCorporateRec();
  PartnerRoleRec ptnrRoleBank = this.buff.getPartnerRoleByCode("BNK");
  List<PartnerRoleRec> roles = new ArrayList<>();
  roles.add(ptnrRoleBank);
  bnkPartner.setPartnerRoles(roles);
  
  newBank.getBankAccount().getAccountForBranch().getBank().setBankOrganisation(new PartnerCorporateRec());
 }
 
 PrimeFaces pf = PrimeFaces.current();
 pf.ajax().update("newBankFrm");
 pf.executeScript("PF('newBankDlgWv').show()");
 
}

public List<PartnerCorporateRec> bankOrgComplete(String value) {
  LOGGER.log(INFO, "bankOrgComplete called with value {0}",value);
  ArrayList<String> retList = new ArrayList<>();
  PartnerRoleRec rl = buff.getPartnerRoleByCode("BNK");
  List<PartnerBaseRec> ptnrs = this.ptnrMgr.getPartnerByRoleAndTradingName(rl,value);
  if(ptnrs == null){
   return null;
  }
  List<PartnerCorporateRec> bankOrgList = new ArrayList<>();
  for(PartnerBaseRec pb:ptnrs){
   if(pb.getClass().getSimpleName().equals("PartnerCorporateRec") ){
    PartnerCorporateRec c = (PartnerCorporateRec)pb;
    if(c.getTradingName().startsWith(value)){
     bankOrgList.add(c);
    }
   }
  }
  
  return bankOrgList;
}
public void onBankOrgBlur(){
  LOGGER.log(INFO, "onBankOrgBlur trading name {0}",
          newBank.getBankAccount().getAccountForBranch().getBank().getBankOrganisation().getTradingName());


}


public void createBankAccountBtnListener(){
  LOGGER.log(INFO, "createBankAccountBtnListener called Account number {0} sort code {1} branch id {2}",
          new Object[]{newBank.getBankAccount().getAccountNumber(),
          newBank.getBankAccount().getAccountForBranch().getSortCode(),
          newBank.getBankAccount().getAccountForBranch().getId()
  });
  GenUtil.addInfoMessage("createBankAccountBtnListener called");
}
public void createBankBranchBtnListener(){
  LOGGER.log(INFO, "createBankBranchBtnListener");
  this.createBankBranch = true;
  LOGGER.log(INFO, "createBankBranch is now {0}",createBankBranch);
  GenUtil.addInfoMessage("createBankBranchBtnListener called");
}
public void createBankOrgBtnListener(){
  LOGGER.log(INFO, "createBankOrgBtnListener");
  //this.createBankBranch = true;
  LOGGER.log(INFO, "createBankBranch is now {0}",createBankBranch);
  //this.ptnrMgr.createCorporatePartnerAR(ptnrCorp, null)
  GenUtil.addInfoMessage("Bank Branch created");
}


}
