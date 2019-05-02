/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.gl;

import java.util.Date;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.util.GenUtil;
import com.rationem.exception.BacException;
import com.rationem.util.BaseBean;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.LedgerRec;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import java.util.ListIterator;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.util.MessageUtil;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.Tab;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;


/**
 * Manages GL account account master data: create, update and delete
 * @author Chris
 */
public class GlAccountBean extends BaseBean implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(GlAccountBean.class.getSimpleName());
    public static final int STEP_START = 0;
    public static final int STEP_END = 1;

    @EJB
    private SysBuffer sysBuffer;

    @EJB
    private GlAccountManager glActMgr;
    
    @EJB
    private BankManager bnkMgr;


    private String crType = "none";
    private boolean showGl = false;
    private boolean showBs = false;
    private boolean coaSelected = false;
    private boolean actTypeSelected = false;
    private boolean editFields = false;
    private boolean acNumDisabled = true;
    private boolean autoGlNum = true;
    private boolean showCompAccounts = false;
    private boolean showEditAccount = false;
    private boolean showEditCompAccount = false;
    private boolean saveGlBtnDisabled = true;
    private boolean editCompAcDisabled = true;
    private boolean compVatable = false;
    private boolean glAccountVatable= false;
    private Long selectedActTyId;
    private Long selectedCoaId;
    private Long selectedCompanyId;
    private String accountTypeCode = "none";
    private String action = null;
    

    // balance sheet variables
    private String AccountCategory;
    private boolean balFwd = false;
    private boolean userClear = false;

    //Pl Account variables

    private String wmActNum;



    private ArrayList<ChartOfAccountsRec> coaList;
    
    private ArrayList<SelectItem> coaSelList;
    private ChartOfAccountsRec selectedCoA;

    private List<AccountTypeRec> accountTyList;
    private AccountTypeRec selectedAccountType;
    

    
    private ArrayList<SelectItem> companySelList;
    private CompanyBasicRec selectedCompany;
    
    private FiGlAccountBaseRec refAccount;
    private CompanyBasicRec refComp;

    
    private ArrayList<BankAccountRec> bankList;
    private List<VatCodeCompanyRec> vatCodeList;
    private String vatCodeDescr;
    private List<FiGlAccountCompRec> compActList;
    private List<FiGlAccountBaseRec> accountList;

    private FiGlAccountBaseRec glAccount;
    private FiGlAccountBaseRec glAccountSelected;
    private List<FiGlAccountCompRec> selectedCoaAccountCompActs;
    private FiPlAccountRec plAccount;
    private FiPlAccountRec selectedPlAccount;
    private FiBsAccountRec bsAccount;
    private FiBsAccountRec selectedBsAccount;
    private String selectedCoaClass;
    private AccountTypeRec actType;
    
    private FiGlAccountCompRec glAccountComp;
    private FiGlAccountCompRec selectedCompAc;
    private FiGlAccountCompRec newCompAc;
    private FiGlAccountBaseRec refAccountChart;
    
    private boolean accountCreated = false;

    @PostConstruct
    private void init(){
     LOGGER.log(INFO, "GlAccount update called by view {0}", getViewSimple());
     accountTyList = this.sysBuffer.getAcntTypes();
     LOGGER.log(INFO, "accountTyList {0}", accountTyList);
     actType = accountTyList.get(0);
     if(actType.isProfitAndLossAccount()){
      glAccount = new FiPlAccountRec();
     }else{
      glAccount = new FiBsAccountRec();
     }
     glAccount.setAccountType(actType);
     selectedCompany = getCompList().get(0);
     LOGGER.log(INFO, "init selectedCompany id {0}", selectedCompany.getId());
     if(!StringUtils.equals("glCoaAcntUpdate", getViewSimple())){
     coaList = glActMgr.getCoaList();
      if(coaList != null){
       selectedCoA = coaList.get(0);
      }else{
       MessageUtil.addWarnMessage("glAcntNoCoa", "errorText");
      }
     }
     setStep(GlAccountBean.STEP_START);
     
     
    }
    
    private FiPlAccountRec addBaseToPlAct(FiPlAccountRec plAct,FiGlAccountBaseRec act ){
      LOGGER.log(INFO, "addBaseToPlAct called with plact {0} and base rec {1}",
              new Object[] {plAct,act});
    //  FiPlAccountRec plAct = new FiPlAccountRec();

      //Copy basic account into Base
      LOGGER.log(INFO, " call setAccountType with {0}", act.getAccountType());
      if(plAct == null){
       plAct = new FiPlAccountRec();
      }
      plAct.setAccountType(act.getAccountType());
      LOGGER.log(INFO, " call setChartOfAccounts with {0}", act.getChartOfAccounts());
      plAct.setChartOfAccounts(act.getChartOfAccounts());
      plAct.setCreatedBy(act.getCreatedBy());
      plAct.setCreatedOn(act.getCreatedOn());
      plAct.setDescription(act.getDescription());
      plAct.setId(act.getId());
      plAct.setName(act.getName());
      plAct.setPl(act.isPl());
      plAct.setRef(act.getRef());
      plAct.setRevision(act.getRevision());
      plAct.setUpdateBy(act.getUpdateBy());

      return plAct;
    }

    private FiBsAccountRec addBaseToBsAct(FiBsAccountRec bsAct,FiGlAccountBaseRec act ){
      LOGGER.log(INFO, "addBaseToBsAct called with bsAct {0} and base rec {1}",
              new Object[] {bsAct,act});

      //Copy basic account into Base
      bsAct.setAccountType(act.getAccountType());
      bsAct.setChartOfAccounts(act.getChartOfAccounts());
      bsAct.setCreatedBy(act.getCreatedBy());
      bsAct.setCreatedOn(act.getCreatedOn());
      bsAct.setDescription(act.getDescription());
      bsAct.setId(act.getId());
      bsAct.setName(act.getName());
      bsAct.setPl(act.isPl());
      bsAct.setRef(act.getRef());
      bsAct.setRevision(act.getRevision());
      bsAct.setUpdateBy(act.getUpdateBy());

      return bsAct;
    }




    /** Creates a new instance of GlAccountBean */
    public GlAccountBean() {
      super();
    }

    public String getCrType() {
        
        return crType;

    }

    public void setCrType(String crType) {
        this.crType = crType;
    }

    public boolean isShowGl() {
        return showGl;
    }

    public void setShowGl(boolean showGl) {
        this.showGl = showGl;
    }

  public boolean isShowBs() {
    return showBs;
  }

  public void setShowBs(boolean showBs) {
    this.showBs = showBs;
  }


  public boolean isAutoGlNum() {
    return autoGlNum;
  }

  public void setAutoGlNum(boolean autoGlNum) {
    this.autoGlNum = autoGlNum;
  }

  public boolean isShowCompAccounts() {
    return showCompAccounts;
  }

  public void setShowCompAccounts(boolean showCompAccounts) {
    this.showCompAccounts = showCompAccounts;
  }


    public boolean isCoaSelected() {
        return coaSelected;
    }

 public boolean isCompVatable() {
  
  return compVatable;
 }

 public void setCompVatable(boolean compVatable) {
  this.compVatable = compVatable;
 }

  public boolean isEditFields() {
    return editFields;
  }

  public void setEditFields(boolean editFields) {
    this.editFields = editFields;
  }

 public boolean isEditCompAcDisabled() {
  return editCompAcDisabled;
 }

 public void setEditCompAcDisabled(boolean editCompAcDisabled) {
  this.editCompAcDisabled = editCompAcDisabled;
 }

  public CompanyBasicRec getSelectedCompany() {
    if(selectedCompany == null){
      selectedCompany = new CompanyBasicRec();
    }
    return selectedCompany;
  }

  public void setSelectedCompany(CompanyBasicRec selectedCompany) {
    this.selectedCompany = selectedCompany;
  }

  public String getAccountCategory() {
    return AccountCategory;
  }

  public void setAccountCategory(String AccountCategory) {
    this.AccountCategory = AccountCategory;
  }

  public boolean isBalFwd() {
    return balFwd;
  }

  public void setBalFwd(boolean balFwd) {
    this.balFwd = balFwd;
  }

 
  public void accountSelect(SelectEvent evt){
   glAccount = (FiGlAccountBaseRec)evt.getObject();
   LOGGER.log(INFO, "Account select glAccount comp account details {0}", glAccount.getCompanyAccounts());
   LOGGER.log(INFO, "Web account select glAccount class {0}", glAccount.getClass().getCanonicalName());
   if(glAccount.getCompanyAccounts() == null ||glAccount.getCompanyAccounts().isEmpty() ){
    // need to get company code details
    List<FiGlAccountCompRec> compAcs = this.glActMgr.getCompanyAccounts(glAccount);
    LOGGER.log(INFO,"comp acs {0}",compAcs);
    glAccount.setCompanyAccounts(compAcs);
    saveGlBtnDisabled = false;
    if(glAccount.getClass().getCanonicalName().equalsIgnoreCase("com.rationem.busRec.fi.glAccount.FiPlAccountRec")){
     this.plAccount = (FiPlAccountRec)glAccount;
     glAccount.setPl(true);
    }else{
     this.bsAccount = (FiBsAccountRec)glAccount;
     glAccount.setPl(false);
    }
    
   }
   showEditAccount = true;
  }
  
  public void setAccountList(ArrayList<FiGlAccountBaseRec> accountList) {
    this.accountList = accountList;
  }




 

  public List<FiGlAccountCompRec> getCompActList() {
   if(compActList == null){
    // need to build account list
    if(selectedCompany.getId() == null){
     selectedCompany = getCompList().get(0);
    }
    compActList = this.glActMgr.getCompanyAccounts(selectedCompany);
   }
   
   
    return compActList;
  }

  public void setCompActList(ArrayList<FiGlAccountCompRec> compActList) {
    this.compActList = compActList;
  }



  public List<VatCodeCompanyRec> getVatCodeList() {
    if(vatCodeList == null){
      vatCodeList = new ArrayList<>();
      if(glAccountComp == null){
       glAccountComp = this.getGlAccountComp();
      }
      
      List<VatCodeCompanyRec> vatCodeListL = sysBuffer.getCompVatCodes(selectedCompAc.getCompany());
      LOGGER.log(INFO, "sysBuffer returned comp vat codes {0}", vatCodeListL);
      if(vatCodeListL == null){
       return null;
      }
       
      ListIterator<VatCodeCompanyRec> li = vatCodeListL.listIterator();
      while(li.hasNext()){
       // get system VAT types
       VatCodeCompanyRec vatCode = li.next();
       if(vatCode.getVatCode().isVatRule()){
        vatCodeList.add(vatCode);
       }
      }
      li = vatCodeListL.listIterator();
      while(li.hasNext()){
       // get non-system VAT types
       VatCodeCompanyRec vatCode = li.next();
       if(!vatCode.getVatCode().isVatRule()){
        vatCodeList.add(vatCode);
       }
      }
      
    }
    return vatCodeList;
  }

  public void setVatCodeList(ArrayList<VatCodeCompanyRec> vatCodeList) {
    this.vatCodeList = vatCodeList;
  }

 public String getVatCodeDescr() {
  if(vatCodeDescr == null){
   vatCodeDescr = this.formTextForKey("vatCdNone");
  }
  return vatCodeDescr;
 }

 public void setVatCodeDescr(String vatCodeDescr) {
  this.vatCodeDescr = vatCodeDescr;
 }

 



  public boolean isUserClear() {
    return userClear;
  }

  public void setUserClear(boolean usrClear) {
    this.userClear = usrClear;
  }

  public String getAccountTypeCode() {
    if(this.actType != null){
      if(this.actType.isProfitAndLossAccount()){
        accountTypeCode = "pl";
      }else{
        accountTypeCode = "bs";
      }
    }
    return accountTypeCode;
  }

  public void setAccountTypeCode(String accountTypeCode) {
    this.accountTypeCode = accountTypeCode;
  }

  public ArrayList<BankAccountRec> getBankList() {
    if(bankList == null){
      bankList = new ArrayList<>();
    }
    return bankList;
  }

  public void setBankList(ArrayList<BankAccountRec> bankList) {
    this.bankList = bankList;
  }

  

  public ChartOfAccountsRec getSelectedCoA() {
    return selectedCoA;
  }

  public void setSelectedCoA(ChartOfAccountsRec selectedCoA) {
    this.selectedCoA = selectedCoA;
    
  }

  public Long getSelectedCompanyId() {
    return selectedCompanyId;
  }

  public void setSelectedCompanyId(Long selectedCompanyId) {
    this.selectedCompanyId = selectedCompanyId;
  }

 public FiGlAccountCompRec getSelectedCompAc() {
  return selectedCompAc;
 }

 public void setSelectedCompAc(FiGlAccountCompRec selectedCompAc) {
  this.selectedCompAc = selectedCompAc;
  editCompAcDisabled = false;
 }

 public FiPlAccountRec getSelectedPlAccount() {
  return selectedPlAccount;
 }

 public void setSelectedPlAccount(FiPlAccountRec selectedPlAccount) {
  this.selectedPlAccount = selectedPlAccount;
 }

 public FiBsAccountRec getSelectedBsAccount() {
  return selectedBsAccount;
 }

 public void setSelectedBsAccount(FiBsAccountRec selectedBsAccount) {
  this.selectedBsAccount = selectedBsAccount;
 }

  public boolean isAcNumDisabled() {
    //logger.log(INFO, "isAcNumDisabled");
    LOGGER.log(INFO, "isAcNumDisabled: {0}", acNumDisabled);
    LOGGER.log(INFO, "isActTypeSelected: {0}", actTypeSelected);
    if(this.selectedCoA == null || !this.isActTypeSelected()){
      LOGGER.log(INFO, "Chart {0} account type selected: {1}",new Object[]  {selectedCoA,actTypeSelected});
      acNumDisabled = true;
    }else if(isActTypeSelected() ){
      LOGGER.log(INFO,"is auto Number: {0}",this.actType.getNumberRange().isAutoNum());
      if(actType != null && actType.getNumberRange().isAutoNum()){
        LOGGER.log(INFO, "Account type is auto number");
        autoGlNum = true;
        acNumDisabled = true;
        LOGGER.log(INFO, "isAcNumReadOnly to return: {0}", acNumDisabled);
        
      }else{
        LOGGER.log(INFO, "Account type is not auto number");
        autoGlNum = false;
        acNumDisabled = false;
        
      }
      
    }
     LOGGER.log(INFO, "isAcNumReadOnly to return: {0}", acNumDisabled);
    return acNumDisabled;
  }

  public void setAcNumDisabled(boolean acNumDisabled) {
    this.acNumDisabled = acNumDisabled;
  }




    public void setCoaSelected(boolean coaSelected) {
        this.coaSelected = coaSelected;
    }

  public FiGlAccountBaseRec getGlAccount() {
    if(glAccount == null){
      LOGGER.log(INFO,"New glAccount()");
      glAccount = new FiGlAccountBaseRec();
      glAccount.setChartOfAccounts(new ChartOfAccountsRec());
    }
    return glAccount;
  }

  public void setGlAccount(FiGlAccountBaseRec acct) {
    this.glAccount = acct;
  }

  public AccountTypeRec getActType() {
    if(actType == null){
      actType = new AccountTypeRec();
    }
    return actType;
  }

  public void setActType(AccountTypeRec actType) {
    this.actType = actType;
  }

  public boolean isActTypeSelected() {
    return actTypeSelected;
  }

  public void setActTypeSelected(boolean actTypeSelected) {
    this.actTypeSelected = actTypeSelected;
  }

  public Long getSelectedActTyId() {
    return selectedActTyId;
  }

  public void setSelectedActTyId(Long selectedActTyId) {
    this.selectedActTyId = selectedActTyId;
  }

  public Long getSelectedCoaId() {
    return selectedCoaId;
  }

  public void setSelectedCoaId(Long selectedCoaId) {
    this.selectedCoaId = selectedCoaId;
  }

 public String getSelectedCoaClass() {
  return selectedCoaClass;
 }

 public void setSelectedCoaClass(String selectedCoaClass) {
  this.selectedCoaClass = selectedCoaClass;
 }

 public List<FiGlAccountCompRec> getSelectedCoaAccountCompActs() {
  if(selectedCoaAccountCompActs == null){
   selectedCoaAccountCompActs = new ArrayList<>();
  }
  return selectedCoaAccountCompActs;
 }

 public void setSelectedCoaAccountCompActs(List<FiGlAccountCompRec> selectedCoaAccountCompActs) {
  this.selectedCoaAccountCompActs = selectedCoaAccountCompActs;
 }

  

  public GlAccountManager getGlActMgr() {
    return glActMgr;
  }

  public void setGlActMgr(GlAccountManager glActMgr) {
    this.glActMgr = glActMgr;
  }

  public FiPlAccountRec getPlAccount() {
    if(plAccount == null){
      plAccount = new FiPlAccountRec();
    }
    return plAccount;
  }

  public void setPlAccount(FiPlAccountRec plAccount) {
    this.plAccount = plAccount;
  }

 public FiGlAccountBaseRec getRefAccountChart() {
  return refAccountChart;
 }

 public void setRefAccountChart(FiGlAccountBaseRec refAccountChart) {
  this.refAccountChart = refAccountChart;
 }

  
  
  public FiBsAccountRec getBsAccount() {
    if(bsAccount == null){
      bsAccount = new FiBsAccountRec();
    }
    return bsAccount;
  }

  public void setBsAccount(FiBsAccountRec bsAccount) {
    this.bsAccount = bsAccount;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public FiGlAccountCompRec getGlAccountComp() {
    if(glAccountComp == null){
      glAccountComp = new FiGlAccountCompRec();
    }
    return glAccountComp;
  }

  public void setGlAccountComp(FiGlAccountCompRec glAccountComp) {
    this.glAccountComp = glAccountComp;
  }

 public FiGlAccountBaseRec getGlAccountSelected() {
  if(glAccountSelected == null){
   glAccountSelected = new FiGlAccountBaseRec();
  }
  return glAccountSelected;
 }

 public void setGlAccountSelected(FiGlAccountBaseRec glAccountSelected) {
  this.glAccountSelected = glAccountSelected;
 }

  
 public boolean isGlAccountVatable() {
  return glAccountVatable;
 }

 public void setGlAccountVatable(boolean glAccountVatable) {
  this.glAccountVatable = glAccountVatable;
 }

 public List<FiGlAccountBaseRec> getAccountList() {
  if(accountList == null && selectedCoA != null){
   accountList = this.glActMgr.getGlAccountsForChart(selectedCoA);
   if(accountList != null && !accountList.isEmpty()){
    this.glAccountSelected = accountList.get(0);
   }
  }
    
  if(accountList == null || accountList.isEmpty()){
  MessageUtil.addWarnMessage("glAcsForCoaNone", "validationText");
 }
  return accountList;
 }

 public void setAccountList(List<FiGlAccountBaseRec> accountList) {
  this.accountList = accountList;
 }

  
 public FiGlAccountCompRec getNewCompAc() {
  if(newCompAc == null){
   LOGGER.log(INFO, "getNewCompAc is null compList {0}", getCompList());
   newCompAc = new FiGlAccountCompRec(); 
   newCompAc.setCompany(getCompList().get(0));
   newCompAc.setCoaAccount(glAccount);
  }
  return newCompAc;
 }

 public void setNewCompAc(FiGlAccountCompRec newCompAc) {
  this.newCompAc = newCompAc;
 }



  

  public String getWmActNum() {
    LOGGER.log(INFO,"getWmActNum called");
    LOGGER.log(INFO,"selectedCoaId {0}, selectedActTyId: {1}, autoGlNum {2} ",
            new Object[] {selectedCoaId,selectedActTyId,autoGlNum});

    if(selectedCoaId == null || selectedCoaId == 0){
      wmActNum = this.getvalidationText().getString("glNoCoaTy");
      LOGGER.log(INFO,"Watermark set to: {0}",wmActNum);
    }else if(actType.getId() == null || selectedActTyId == 0){
      wmActNum = this.getvalidationText().getString("glNoActTy");
    }else if(this.autoGlNum){
      wmActNum = this.getvalidationText().getString("glActAutoNum");
    }
    else{
      wmActNum = this.getvalidationText().getString("glActRef");
    }
    if(wmActNum == null){
      wmActNum = "not set";
    }
    return wmActNum;
  }

  public void setWmActNum(String wmActNum) {
    this.wmActNum = wmActNum;
  }

  public boolean isShowEditAccount() {
    return showEditAccount;
  }

  public void setShowEditAccount(boolean showEditAccount) {
    this.showEditAccount = showEditAccount;
  }

  public boolean isShowEditCompAccount() {
    return showEditCompAccount;
  }

  public void setShowEditCompAccount(boolean showEditCompAccount) {
    this.showEditCompAccount = showEditCompAccount;
  }

  public boolean isSaveGlBtnDisabled() {
    return saveGlBtnDisabled;
  }

  public void setSaveGlBtnDisabled(boolean saveGlBtnDisabled) {
    this.saveGlBtnDisabled = saveGlBtnDisabled;
  }

  


    public void createAllBtn(){
        LOGGER.log(INFO, "createAllBtn called");
        crType = "ALL";
        showGl = true;
        

    }


    public void createCoaBtn(){
        LOGGER.log(INFO, "createCoaBtn called");
        crType="COA";
        showGl = true;

    }
    public void createCompBtn(){
        LOGGER.log(INFO, "createCompBtn called");
        crType="COMP";
        showGl = true;

    }
/*
  public List<BankBranchRec> bankBranchComplete(String input){
   List<BankBranchRec> branchLst = selectedCompany.getBanks();
   
   if(input == null || input.isEmpty()){
    input = new String();
    return branchLst;
   }else{
    input = input.trim();
    ListIterator<BankBranchRec> bnkIt = branchLst.listIterator();
    while(bnkIt.hasNext()){
     BankBranchRec rec = bnkIt.next();
     if(!rec.getSortCode().startsWith(input)){
      bnkIt.remove();
     }
    }
    return branchLst;
   }
   
   
  }
*/
    public ArrayList<ChartOfAccountsRec> getCoaList() {
       if (coaList == null || coaList.isEmpty()){
         LOGGER.log(INFO, "Build Chart list");
         try{
           coaList = glActMgr.getCoaList();
           if(coaList != null && coaList.size() >0){
             selectedCoA = coaList.get(0);
             LOGGER.log(INFO, "selectedCoA is {0}", selectedCoA.getName());
             if(glAccount == null){
               glAccount = new FiGlAccountBaseRec();
             }
             glAccount.setChartOfAccounts(selectedCoA);
             
             
           }
           LOGGER.log(INFO, "GlAccountBean - Number of charts of accounts returned: {0}", coaList.size());

         }catch(BacException e){
           GenUtil.addErrorMessage("Could not build Chart of Accounts list "+e.getLocalizedMessage());
         }

       }
       LOGGER.log(INFO, "getCoaList() returns {0}", coaList);
        return coaList;
    }

    public void setCoaList(ArrayList<ChartOfAccountsRec> coaList) {
        this.coaList = coaList;
    }

    public ArrayList<SelectItem> getCoaSelList() {
        LOGGER.log(INFO, "Called getCoaSelList");
        if(coaSelList == null){
            coaSelList = new ArrayList<>();
            SelectItem item = new SelectItem();
            item.setLabel("choose Chart");
            item.isNoSelectionOption();
            item.setValue("0");
            coaSelList.add(item);
            coaList = getCoaList();
            
            ListIterator li = coaList.listIterator();
            while(li.hasNext()){
                ChartOfAccountsRec coa = (ChartOfAccountsRec)li.next();
                item = new SelectItem();
                item.setValue(coa.getId());
                item.setLabel(coa.getName());
                coaSelList.add(item);
            }
            
        }
        return coaSelList;
    }

  public List<AccountTypeRec> getAccountTyList() {
    if(accountTyList == null){
      accountTyList = glActMgr.getGlAccountTypes();

    
    if(accountTyList != null && accountTyList.size() > 0){
      selectedAccountType = accountTyList.get(0);
      this.glAccount.setAccountType(selectedAccountType);
    }
    }
    LOGGER.log(INFO, "getAccountTyList selected account type is {0}", selectedAccountType);
    return accountTyList;
  }

  public void setAccountTyList(List<AccountTypeRec> accountTyList) {
    this.accountTyList = accountTyList;
  }

 public FiGlAccountBaseRec getRefAccount() {
  return refAccount;
 }

 public void setRefAccount(FiGlAccountBaseRec refAccount) {
  this.refAccount = refAccount;
 }

 public CompanyBasicRec getRefComp() {
  return refComp;
 }

 public void setRefComp(CompanyBasicRec refComp) {
  this.refComp = refComp;
 }

  
  public AccountTypeRec getSelectedAccountType() {
    return selectedAccountType;
  }

  public void setSelectedAccountType(AccountTypeRec selectedAccountType) {
    this.selectedAccountType = selectedAccountType;
  }

  
  public ArrayList<SelectItem> getCompanySelList() {
    LOGGER.log(INFO, "Web getCompanySelList called ");
    if(companySelList == null || companySelList.isEmpty() ){
      LOGGER.log(INFO, "Need to build company SelList ");
      companySelList = new ArrayList<>();
      SelectItem item;
      
      LOGGER.log(INFO, "companyList {0}",this.getCompList());
      ListIterator li = getCompList().listIterator();
      while(li.hasNext()){
        CompanyBasicRec comp = (CompanyBasicRec)li.next();
        item = new SelectItem();
        item.setValue(comp.getId());
        item.setLabel(comp.getReference());
        LOGGER.log(INFO, "Company to be added is id: {0} number {1}",new Object[] { comp.getId(),comp.getCompanyNumber()});
        companySelList.add(item);
      }
      LOGGER.log(INFO, "Number of companies is select list {0}", companySelList.size());
    }
    return companySelList;
  }

  public void setCompanySelList(ArrayList<SelectItem> companySelList) {
    this.companySelList = companySelList;
  }

public void onCompanyChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onCompanyChange called with new company id {0}",evt.getNewValue());
  //selectedCompanyId = (Long)evt.getNewValue();
  try{
    this.selectedCompany = this.sysBuffer.getCompanyById(selectedCompanyId);
  }catch(BacException e){
    MessageUtil.addClientErrorMessage("glUpdt:msgs", "compNone", "errorText", " ref: "+ this.glAccount.getRef());
    PrimeFaces.current().ajax().update("glUpdt:msgs");
  }

}


public void onContextMenuCoaAcntUpdate(SelectEvent evt){
 LOGGER.log(INFO, "onContextMenuCoaAcntUpdate called with {0}", evt.getObject());
 String acntType = evt.getObject().getClass().getSimpleName();
 if(acntType.equals("FiPlAccountRec")){
  selectedPlAccount = (FiPlAccountRec)evt.getObject();
  selectedCoaClass = "pl";
 }else{
  selectedBsAccount = (FiBsAccountRec)evt.getObject();
  selectedCoaClass = "bs";
 }
}

public void onContextmenuCompAcntUpdate(SelectEvent evt){
 LOGGER.log(INFO, "onContextmenuCoaCompAcntUpdate called with {0}", evt.getObject());
 this.selectedCompAc = (FiGlAccountCompRec)evt.getObject();
 
}
public void onEditAccountChange(ValueChangeEvent evt){
  LOGGER.log(INFO,"onEditAccountChange called with account id {0} company name {1} ",
          new Object[] {evt.getNewValue(),this.selectedCompanyId});
  Long selectedAccountId = (Long)evt.getNewValue();
  if(selectedCompany == null){
    selectedCompany = this.sysBuffer.getCompanyById(selectedCompanyId);

  }
  LOGGER.log(INFO,"onEditAccountChange selectedCompany is {0}",selectedCompany);
  
   ListIterator li = this.accountList.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
    FiGlAccountBaseRec rec = (FiGlAccountBaseRec)li.next();
    if(Objects.equals(rec.getId(), selectedAccountId)){
      found = true;
      glAccount = rec;
      LOGGER.log(INFO, "Company GL accounts from Coa account {0} is {1}", 
              new Object[] {glAccount,glAccount.getCompanyAccounts()});
      glAccountComp = glActMgr.getCompanyAcctForCoaAccount(glAccount, selectedCompany);
      LOGGER.log(INFO, "glAccountComp is {0}", glAccountComp);

      this.showEditAccount = true;
    }
  }

  if(!found){
    GenUtil.addErrorMessage("Could not find GL account");
  }

}

public void onAccountChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onAccountChange called with ", evt.getNewValue());
  Long selectedAccountId = (Long)evt.getNewValue();
  ListIterator li = this.accountList.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
    FiGlAccountBaseRec rec = (FiGlAccountBaseRec)li.next();
    if(Objects.equals(rec.getId(), selectedAccountId)){
      found = true;
      this.glAccount = rec;

      this.showEditAccount = true;
    }
  }
  if(!found){
    GenUtil.addErrorMessage("Could not find GL account");
  }


  }

public void onAccountClearValueChange(ValueChangeEvent evt){
 LOGGER.log(INFO, " onAccountClearValueChange called with {0} ", evt.getNewValue());
 boolean clrState = (Boolean)evt.getNewValue();
 
 if(clrState){
  glAccountComp.setBankAccountCompanyCleared(null) ;
 }else{
  glAccountComp.setBankAccountCompanyUncleared(null);
 }
}

public List<FiGlAccountBaseRec> onAccountComplete(String input){
 List<FiGlAccountBaseRec> retList = glActMgr.getGlAccountsByRef(input);
 
 return retList;
}
  public void onActTypeChanged(ValueChangeEvent evt){
    LOGGER.log(INFO, "Account type changed to : {0}", evt.getNewValue());
     this.selectedAccountType = (AccountTypeRec)evt.getNewValue();
     actTypeSelected = this.glAccount.getAccountType() == null;


    if(this.coaSelected && actTypeSelected){
      this.editFields = true;
      acNumDisabled = false;
    }else{
      this.editFields = false;
    }
    
    // set accountype selected and determine if auto number range
    
    

    
  }

  
  public void onEditActAddBtnClick(){
   LOGGER.log(INFO, "onEditActAddBtnClick called ");
   selectedCompAc.setChangedBy(getLoggedInUser());
   selectedCompAc.setChangedOn(new Date());
   glActMgr.updateGlAccountComp(selectedCompAc, getView());
   List<FiGlAccountCompRec> compAcs = glAccountSelected.getCompanyAccounts();
   ListIterator<FiGlAccountCompRec> compAcsLi = compAcs.listIterator();
   boolean found = false;
   while(compAcsLi.hasNext() && !found){
    FiGlAccountCompRec rec = compAcsLi.next();
    if(Objects.equals(rec.getCompany().getId(), this.selectedCompAc.getCompany().getId())){
     LOGGER.log(INFO, "Found selectedCompAc {0}",selectedCompAc);
     compAcsLi.set(selectedCompAc);
    }
   }
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("tabV:comAcTbl");
   pf.executeScript("PF('editAcWVar').hide()");
  }
  
  public void onEditAcntEditCompAcDlg(){
   PrimeFaces pf = PrimeFaces.current();
   pf.executeScript("PF('editAcWVar').show()");
  }
 
  public void onEditCoaAcntDlg(){
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editActDlgId");
   pf.executeScript("PF('editActDlgWv').show()");
  }
  
  public void onEditCoaPlAcntTrf(){
   LOGGER.log(INFO, "onEditCoaPlAcntTrf ");
   LOGGER.log(INFO, "selectedBsAccount {0}", selectedBsAccount);
   LOGGER.log(INFO, "selectedPlAccount {0}", selectedPlAccount);
   if(this.selectedCoaClass.equals("pl")){
    selectedPlAccount.setUpdateBy(this.getLoggedInUser());
    selectedPlAccount.setUpdateOn(new Date());
    selectedPlAccount = 
      (FiPlAccountRec)this.glActMgr.updateGlAccountCoa(selectedPlAccount, this.getLoggedInUser(), getView());
   }else{
    selectedBsAccount.setUpdateBy(this.getLoggedInUser());
    selectedBsAccount.setUpdateOn(new Date());
    selectedBsAccount = 
      (FiBsAccountRec)this.glActMgr.updateGlAccountCoa(selectedBsAccount, this.getLoggedInUser(), getView());
   }
   ListIterator<FiGlAccountBaseRec> acntLi = this.accountList.listIterator();
   boolean accountFound = false;
   while(acntLi.hasNext() && !accountFound){
    FiGlAccountBaseRec acnt = acntLi.next();
    if(selectedPlAccount != null &&Objects.equals(selectedPlAccount.getId(), acnt.getId())){
     acntLi.set(selectedPlAccount);
     accountFound = true;
    }else if(selectedBsAccount != null &&Objects.equals(selectedBsAccount.getId(), acnt.getId())){
     acntLi.set(selectedBsAccount);
     accountFound = true;
    }
   }
   if(accountFound){
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("glUpdt:acntTbl");
    pf.executeScript("PF('editActDlgWv').hide()");
   }
   
  }
  
  public void onEditCoaRowToggle(ToggleEvent evt){
   LOGGER.log(INFO, "onEditCoaRowToggle called {0}", evt.getData());
   if(evt.getData().getClass().getSimpleName().equals("FiPlAccountRec")){
    selectedCoaClass = "pl";
    selectedPlAccount = (FiPlAccountRec) evt.getData();
   }else{
    selectedCoaClass = "bs";
    selectedBsAccount = (FiBsAccountRec) evt.getData();
   }
           
   
  }
  
  public void onEditCompRowSel(SelectEvent evt){
   LOGGER.log(INFO, "Row select event {0}", evt.getObject());
  }
  public void onEditCompRowToggle(ToggleEvent evt){
   LOGGER.log(INFO, "Called with {0}", evt.getData());
   selectedCompAc = (FiGlAccountCompRec)evt.getData();
  }
  
  public void onEditCompAcntDlg(){
   LOGGER.log(INFO, "selectedCompAc {0}", selectedCompAc);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editCompAcnt");
   pf.executeScript("PF('editCompAcntWv').show()");
   
  }
  
  public void onEditCompAntTransf(){
   LOGGER.log(INFO, "onEditCompAntTransf {0}", selectedCompAc);
   selectedCompAc.setChangedBy(getLoggedInUser());
   selectedCompAc.setChangedOn(new Date());
   //LOGGER.log(INFO, "Comp Vat code id {0}", selectedCompAc.getVatCode().getId());
   if(selectedCompAc.getVatCode() == null ){
    selectedCompAc.setVatCode(null);
   }
   selectedCompAc = this.glActMgr.updateGlAccountComp(selectedCompAc,  getView());
   ListIterator<FiGlAccountCompRec> li =   compActList.listIterator();
   boolean foundActnt = false;
   while(li.hasNext() && !foundActnt){
    FiGlAccountCompRec compAcnt = li.next();
    if(Objects.equals(compAcnt.getId(), selectedCompAc.getId())){
     li.set(selectedCompAc);
     foundActnt = true;
    }
   }
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("compAcsTbl");
   pf.executeScript("PF('editCompAcntWv').hide()");
  }
  public void onEditCoaCompAcsDlg(){
   LOGGER.log(INFO, "onEditCoaCompAcsDlg called {0}", selectedCoaClass);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editCoaCompDlg");
   pf.executeScript("PF('editCoaCompDlgWv').show()");
   if(selectedCoaClass.equals("pl")){
    selectedCoaAccountCompActs = this.glActMgr.getGlCompRecAcntsForCoaAcnt(selectedPlAccount);
    
   }else{
    selectedCoaAccountCompActs
            = this.glActMgr.getGlCompRecAcntsForCoaAcnt(selectedBsAccount);
   }
   
   LOGGER.log(INFO, "compActList {0}", compActList);
  
  }
  
  
  public void onEditAcntAddCompDlg(){
   LOGGER.log(INFO, "onEditAcntAddCompDlg called select Ac {0}",this.selectedCompAc);
   
   PrimeFaces pf = PrimeFaces.current();
   pf.executeScript("PF('newCompAcWVar').show()");
  }
  
  public void onEditTabChange(TabChangeEvent evt){
   LOGGER.log(INFO, "tab now {0}", evt.getTab());
   Tab activeTab = evt.getTab();
   
   LOGGER.log(INFO, "Current tab id {0}", activeTab.getClientId());
   String tabId = activeTab.getClientId();
   if(tabId.equalsIgnoreCase("tabV:companyDetailsTab")){
    LOGGER.log(INFO, "Company account details");
    LOGGER.log(INFO, "glAccountSelected name {0}", glAccountSelected.getName());
    glAccountSelected.setUpdateBy(this.getLoggedInUser());
    glAccountSelected.setUpdateOn(new Date());
    this.glActMgr.updateGlAccountCoa(glAccountSelected, this.getLoggedInUser(), getView());
    LOGGER.log(INFO, "company acs {0}", this.glAccountSelected.getCompanyAccounts());
    if(glAccountSelected.getCompanyAccounts() == null){
     List<FiGlAccountCompRec> compAcs = glActMgr.getCompanyAccounts(glAccountSelected);
     LOGGER.log(INFO, "compAcs {0}", compAcs);
     glAccountSelected.setCompanyAccounts(compAcs);
    }
   }
  }
  
  public void onEditVatStatusSelect(SelectEvent evt){
   LOGGER.log(INFO, "onEditVatStatusSelect called ith value {0}", evt.getObject());
   selectedCompAc.setVatStatus((Integer)evt.getObject());
   PrimeFaces.current().ajax().update("editAcFrm:vatCd");
  }
  
  public String onAccountFlowProcess(FlowEvent evt){
   String nextStep = evt.getNewStep();
   if(evt.getOldStep().equalsIgnoreCase("refCopy") && evt.getNewStep().equalsIgnoreCase("Chart")){
    LOGGER.log(INFO, "ref to chart w/f");
    LOGGER.log(INFO, "glAccount {0}", glAccount.getDescription());
    LOGGER.log(INFO, "glAccountComp {0}", glAccountComp.getCompany().getCompanyNumber());
    // build account based on ref account
    glAccount.setAccountType(refAccount.getAccountType());
    glAccount.setChartOfAccounts(refAccount.getChartOfAccounts());
    LOGGER.log(INFO,"refAccount chart descr {0}", 
      new Object[]{refAccount.getChartOfAccounts().getName()});
    LOGGER.log(INFO, "glaccount chart name {0}", glAccount.getChartOfAccounts().getName());
    glAccount.setDescription(refAccount.getDescription());
    glAccount.setName(refAccount.getName());
    glAccount.setPl(refAccount.isPl());
    glAccount.setReportCat(refAccount.getReportCat());
    LOGGER.log(INFO, "glAccount id {0}", glAccount.getId());
    // comp account based on ref account comp account
    
    if(refAccount.getCompanyAccounts() != null &&!refAccount.getCompanyAccounts().isEmpty()){
     
     for(FiGlAccountCompRec curr:refAccount.getCompanyAccounts() ){
      if(Objects.equals(curr.getCompany().getId(), refComp.getId())){
       glAccountComp.setAccountClearing(curr.isAccountClearing());
       glAccountComp.setAnalysis1(curr.getAnalysis1());
       glAccountComp.setAnalysis2(curr.getAnalysis2());
       glAccountComp.setBankAccountCompanyCleared(curr.getBankAccountCompanyCleared());
       glAccountComp.setBankAccountCompanyUncleared(curr.getBankAccountCompanyUncleared());
       glAccountComp.setCoaAccount(glAccount);
       glAccountComp.setNoVatAllowed(curr.isNoVatAllowed());
       glAccountComp.setPaymentTypes(curr.getPaymentTypes());
       glAccountComp.setPersonResponsible(curr.getPersonResponsible());
       glAccountComp.setRepCategory(curr.getRepCategory());
       glAccountComp.setSortOrder(curr.getSortOrder());
       glAccountComp.setVatCode(curr.getVatCode());
      }
     }
     LOGGER.log(INFO, "glAccountComp id {0}", glAccountComp.getId());
    }
    
    PrimeFaces.current().ajax().update("glActMstfrm:actTylst");
   } else if(evt.getOldStep().equalsIgnoreCase("Chart") && evt.getNewStep().equalsIgnoreCase("Company")){
    // move from Chart to company data
    LOGGER.log(INFO, "Account type {0}", glAccount.getAccountType());
    
    if(glAccountComp == null){
     glAccountComp = new FiGlAccountCompRec();
     glAccountComp.setCompany(getCompList().get(0));
    }
    LOGGER.log(INFO, "company.isVetReg {0}", glAccountComp.getCompany().isVatReg());
    
    
    if(glAccountComp.getCompany().isVatReg()){
     LOGGER.log(INFO, "vatCodeList {0}", vatCodeList);
     if(vatCodeList == null){
      List<VatCodeCompanyRec> tempVatList = sysBuffer.getCompVatCodes(glAccountComp.getCompany());
      VatCodeCompanyRec vatCodeCompNone = new VatCodeCompanyRec();
      VatCodeRec vatCodeNone = new VatCodeRec();
      vatCodeNone.setDescription(this.formTextForKey("vatCdNone"));
      vatCodeCompNone.setVatCode(vatCodeNone);
      vatCodeList = new ArrayList<>();
      vatCodeList.add(vatCodeCompNone);
      for(VatCodeCompanyRec vc:tempVatList){
       vatCodeList.add(vc);
      }
      
      LOGGER.log(INFO, "Vat code list returned from sysBuff {0}", vatCodeList);
     }
     LOGGER.log(INFO, "vatCodeList after get {0}", vatCodeList);
    
    }
    LOGGER.log(INFO, "end flow step from {0} to {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
   }
   
   return nextStep;
  }
  
  public String onAcntCompFlowProcess(FlowEvent evt){
   LOGGER.log(INFO, "onAcntCompFlowProcess");
   String currStep = evt.getOldStep();
   String nextStep = evt.getNewStep();
   
   if(currStep.equals("refAcntTab") && nextStep.equals("compAcntTab")){
    LOGGER.log(INFO,"glAccountComp {0}",glAccountComp);
    LOGGER.log(INFO, "Ref acnt num {0} acnt ty process cd {1}", new Object[]{refAccount.getRef(), refAccount.getAccountType().getProcessCode().getName()});
    if(glAccountComp == null){
     glAccountComp = new FiGlAccountCompRec();
     glAccountComp.setCompany(selectedCompany);
     glAccountComp.setCoaAccount(refAccount);
    }
   }
   
   return nextStep;
  }
  
  public void onAcntCrActTyShow(){
   LOGGER.log(INFO, "called show account type settings. Gl acnt name {0}",
     this.glAccount.getName());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("displAcntTySettFrm:glref");
   pf.executeScript("PF('displAcntTySettDlWv').show();");
   
  }
  
  public void onAcntCrAcntTySelect(SelectEvent evt){
   LOGGER.log(INFO, "onAcntCrAcntTySelect called with {0}", evt.getObject().getClass().getSimpleName());
   this.glAccount.setAccountType((AccountTypeRec)evt.getObject());
   PrimeFaces.current().ajax().update("glActMstfrm");
   
  }
  public void onAcntCrStepBack(){
   LOGGER.log(INFO,"onAcntCrStepBack called. Step at start {0}",String.valueOf(getStep()));
   int curr = getStep();
   curr++;
   if(curr < GlAccountBean.STEP_START){
    curr = GlAccountBean.STEP_START;
   }
   setStep(curr);
   PrimeFaces.current().ajax().update("glActMstfrm");
  }
  public void onAcntCrStepNext(){
   LOGGER.log(INFO,"onAcntCrNextStep called. Step at start {0}",String.valueOf(getStep()));
   int curr = getStep();
   curr++;
   if(curr > GlAccountBean.STEP_END){
    curr = GlAccountBean.STEP_END;
   }
   setStep(curr);
   PrimeFaces.current().ajax().update("glActMstfrm");
   
  }
  
  
  public void onAccountRefUniqueValidate(FacesContext context, UIComponent comp, Object val){
   LOGGER.log(INFO, "onAccountRefUniqueValidate called with {0}", val);
   String input = (String)val;
   if(StringUtils.isBlank(input)){
    ((EditableValueHolder)comp).setValid(false);
    MessageUtil.addErrorMessage("glAcntNumNone", "errorText");
   }else{
    FiGlAccountBaseRec chkAcnt = glActMgr.getGlAccountByRef(input);
    
    if(chkAcnt != null){
     ((EditableValueHolder)comp).setValid(false);
     MessageUtil.addErrorMessage("glAcntNumDupl", "errorText");
    }
    else{
     ((EditableValueHolder)comp).setValid(true);
    }
   }
   PrimeFaces.current().ajax().update("glActMstfrm:newAcnt");
  }
  
  public List<LedgerRec> onLedgerComplete(String input){
 LOGGER.log(INFO, "onLedgerComplete", input);
 List<LedgerRec> ledgersBuff = this.sysBuffer.getLedgers();
 List<LedgerRec> ledgers = new ArrayList<>();
 LOGGER.log(INFO, "Ledgers from sys buff {0}", ledgersBuff);
 for(LedgerRec curr:ledgersBuff){
  if(curr.isSubLeder()){
   ledgers.add(curr);
  }
 }
 LOGGER.log(INFO, "Sub Ledgers  {0}", ledgers);
 if(StringUtils.isBlank(input)){
  return ledgers;
 }else{
  List<LedgerRec> retList = new ArrayList<>();
  for(LedgerRec curr:ledgers){
   if(StringUtils.startsWith(curr.getName(), input)){
    retList.add(curr);
   }
  }
  return retList;
 }
}
  public void onNewActAddBtnClick(){
   LOGGER.log(INFO, "onNewActAddBtnClick called ");
   List<FiGlAccountCompRec> compAcs = glAccount.getCompanyAccounts();
   if(compAcs == null){
    compAcs = new ArrayList<>();
   }
   long id = compAcs.size();
   id++;
   id = id * -1;
   LOGGER.log(INFO, "Id is now {0}", id);
   newCompAc.setId(id);
   compAcs.add(newCompAc);
   glAccount.setCompanyAccounts(compAcs);
   LOGGER.log(INFO, "glAccount.getCompanyAccounts().size {0}", glAccount.getCompanyAccounts().size());
   newCompAc = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("tabV:comAcTbl");
   pf.executeScript("PF('newCompAcWVar').hide()");
   LOGGER.log(INFO, "End add comp");
  }
  
  public void onNewActCancBtnClick(){
   LOGGER.log(INFO, "onNewActCancBtnClick called");
   newCompAc = null;
  }
  
  public void onRefAccountSelect(SelectEvent evt){
   LOGGER.log(INFO, "onRefAccountSelect called with {0}", evt.getObject());
   refAccount = (FiGlAccountBaseRec)evt.getObject();
   
   LOGGER.log(INFO, "refAccount chart name {0}", refAccount.getChartOfAccounts());
   LOGGER.log(INFO, "refAccount Account type {0}", refAccount.getAccountType());
  }
  
  public List<FiGlAccountBaseRec> onGlAcntChartComplete(String input){
   
   List<FiGlAccountBaseRec> retList; 
   ListIterator<FiGlAccountBaseRec> acntBaseLi;
   FiGlAccountBaseRec currAcnt;
   
   if(StringUtils.isBlank(input)){
    LOGGER.log(INFO, "Blank input");
    retList = glActMgr.getGlCoaAcntsNotInComp(selectedCompany,selectedCompany.getChartOfAccounts());
    if(retList == null){
     return null;
    }
    
    
    return retList;
   }else{
    input = StringUtils.remove(input, "%");
    input = StringUtils.appendIfMissing(input, "%");
    retList = glActMgr.getGlCoaAcntsRefNotInComp(input, selectedCompany,selectedCompany.getChartOfAccounts());
    if(retList == null){
     return null;
    }
    /*ListIterator<FiGlAccountBaseRec> li = retList.listIterator();
    
    while(li.hasNext()){
     FiGlAccountBaseRec curr = li.next();
     if(curr.getCompanyAccounts() != null){
       ListIterator<FiGlAccountCompRec> compLi = curr.getCompanyAccounts().listIterator();
       boolean foundCompAct = false;
       while(compLi.hasNext() && !foundCompAct){
        FiGlAccountCompRec currCompAct = compLi.next();
        if(Objects.equals(currCompAct.getCompany().getId(), this.selectedCompany.getId())){
         li.remove();
        }
       }
     }
    }*/
    return retList;
   }
   
   
   
  }
  public void onGlAccountChange(ValueChangeEvent evt){
   LOGGER.log(INFO, "onGlAccountChange called with new {0}", evt.getNewValue());
   this.glAccountSelected = (FiGlAccountBaseRec)evt.getNewValue();
   PrimeFaces pf = PrimeFaces.current();
   List<String> updates = new ArrayList<>();
   updates.add("acTabV");
   updates.add("acName");
   pf.ajax().update(updates);
   
  }
  
  public void onGlAccountRefBlur(){
    LOGGER.log(INFO, "onGlAccountBlur called gl account number {0}", this.glAccount.getRef());
    String actNum = this.glAccount.getRef();
    LOGGER.log(INFO, "actNum {0}", actNum);
    PrimeFaces pf = PrimeFaces.current();
    
    FiGlAccountBaseRec glAct = this.glActMgr.getGlAccountCoaByRef(glAccount);
    LOGGER.log(INFO, "account from Manager {0}",glAct);
    if(glAct != null){
      MessageUtil.addErrorMessage("glAcntNumUnique", "validationText");
      glAccount.setRef(null);

    }else{
      LOGGER.log(INFO, "GL account free ");
      LOGGER.log(INFO, "Account number range {0}", glAccount.getAccountType());
      if(glAccount.getAccountType() == null){
       if(accountTyList == null || accountTyList.isEmpty()){
        MessageUtil.addErrorMessage("glActNoAcntTypes", "validationText");
        glAccount.setRef(null);
        
        pf.ajax().update("glActMstfrm:ref");
        
        return;
       }else{
        
        glAccount.setAccountType(accountTyList.get(0));
       }
      }
      LOGGER.log(INFO, "Account number range 2 {0}", glAccount.getAccountType());
      NumberRangeRec numRange = glAccount.getAccountType().getNumberRange();
      LOGGER.log(INFO, "GL account account type  {0} name {1} numRange {2}",
              new Object[]{glAccount.getAccountType(), glAccount.getAccountType().getName(),
               numRange});
      int minValue = numRange.getFromNum();
      int maxValue = numRange.getToNum();
      int glNumber;
      try{
        glNumber = Integer.parseInt(actNum);
      }catch(NumberFormatException ex){
        GenUtil.addErrorMessage("Account number must be numberic "+ex.getLocalizedMessage());
        glAccount.setRef(null);
        pf.ajax().update("glActMstfrm:ref");
        return;
      }
      LOGGER.log(INFO, "Test numbers min val {0} max value {1} gl act num {2}",
              new Object[]{minValue,maxValue,glNumber});
      if(glNumber < minValue || glNumber > maxValue){
        String errMsg = "Account number must be between "+minValue+" and "+maxValue;
        String msgHdr = this.errorForKey("glAcntNumRange");
        MessageUtil.addErrorMessageWithoutKey(msgHdr, errMsg);
        glAccount.setRef(null);
      }else{
        saveGlBtnDisabled = false;
      }
    }
    pf.ajax().update("glActMstfrm:ref");
    LOGGER.log(INFO, "saveGlBtnDisabled is now {0}", saveGlBtnDisabled);
  }

  
  public List<BankAccountCompanyRec> onBankAccountComplete(String input){
   LOGGER.log(INFO, "onBankAccountComplete called with {0}", input);
   List<BankAccountCompanyRec> bnkActs;
   if(StringUtils.isBlank(input)){
    bnkActs = bnkMgr.getBankAccountsForCompany(selectedCompany);
   }else{
    bnkActs = bnkMgr.getBankAccntsCompByName(selectedCompany, input);
   }
   
   
   return bnkActs;
  }
  
  public List<ChartOfAccountsRec> onChartSelect(SelectEvent evt){
   
   return null;
  }
  public List<FiGlAccountBaseRec> onCoaAcntComplete(String input){
   
   
   return null;
  }
  public void onCoaAcntCoaChanged(ValueChangeEvent evt){
   LOGGER.log(INFO,"onCoaAcntCoaChanged called with {0}",evt.getNewValue() );
   selectedCoA = (ChartOfAccountsRec)evt.getNewValue();
   accountList = this.glActMgr.getGlAccountsForChart(selectedCoA);
   LOGGER.log(INFO, "accountList from glActMgr {0}", accountList);
   
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("acntTbl");
  }
  
  public List<ChartOfAccountsRec> onCoaComplete(String input){
   LOGGER.log(INFO, "onCoaComplete called with {0}", input);
   
   if(StringUtils.isBlank(input)){
    coaList = glActMgr.getCoaList();
    return coaList;
   }else{
    List<ChartOfAccountsRec> coas = glActMgr.getCoaList();
    coaList = new ArrayList<>();
    for(ChartOfAccountsRec curr:coas){
     
     if(StringUtils.startsWith(curr.getReference(), input)){
      coaList.add(curr);
     }
    }
    return coaList;
   }
  
  }

    public void onCoaChanged(ValueChangeEvent evt){
      LOGGER.log(INFO,"coaChanged called with {0}",evt );
      coaSelected = true;
      ListIterator li = coaList.listIterator();
      boolean found = false;
      while(li.hasNext() && !found){
        ChartOfAccountsRec ch = (ChartOfAccountsRec)li.next();
        if(Objects.equals(ch.getId(), evt.getNewValue())){
          coaSelected = true;
          selectedCoA = ch;
        }
      }
      // fill companies list now
      
      LOGGER.log(INFO, " companyList is {0}", getCompList());

      companySelList = getCompanySelList();
      LOGGER.log(INFO, " companySelList is {0}", companySelList);

      this.selectedCompanyId =(Long) companySelList.get(0).getValue();
      accountList = new ArrayList<>();
      FiGlAccountBaseRec rec = new FiGlAccountBaseRec();
      rec.setId(new Long(0));
      rec.setName("Select GL account");
      accountList.add(rec);
      List<FiGlAccountBaseRec> accountListMgr  = this.glActMgr.getGlAccountsForCompany(selectedCompanyId);
      if(accountListMgr == null || accountListMgr.isEmpty()){
       showCompAccounts = false;
       return;
      }
      ListIterator liMgr = accountListMgr.listIterator();
      while(liMgr.hasNext()){
        rec = (FiGlAccountBaseRec)liMgr.next();
        accountList.add(rec);
      }
      LOGGER.log(INFO, " Account List is {0}", accountList);
      this.showCompAccounts = true;
    }

    public void onCoaAcntCompEditDlg(){
     LOGGER.log(INFO, "onCoaAcntCompEditDlg called");
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("editCompAcntDlg");
     pf.executeScript("PF('editCompAcntWv').show()");
    }
    
    public void onCoaSelect(SelectEvent evt){
     LOGGER.log(INFO, "onCoaSelect called with {0}", evt.getObject());
     coaSelected = true;
     selectedCoA = (ChartOfAccountsRec)evt.getObject();
     accountList = this.glActMgr.getGlAccountsForChart(selectedCoA);
     PrimeFaces.current().ajax().update("glUpdt:acntTbl");
    }
    
    public List<SortOrderRec> onSortOrderComplete(String input){
     LOGGER.log(INFO, "onSortOrderComplete called with {0}", input );
     if(StringUtils.isBlank(input)){
      return sysBuffer.getSortOrders();
     }else{
      return sysBuffer.getSortOrdersByCodePrefix(input);
     }
    }
    public void onSortOrderChange(ValueChangeEvent evt){
     SortOrderRec selected = (SortOrderRec)evt.getNewValue();
     glAccountComp.setSortOrder(selected);
    }
    
    public void onCoaCompAcntTrf(){
     LOGGER.log(INFO, "onCoaCompAcntTrf {0}", selectedCompAc.getId());
     selectedCompAc.setChangedBy(this.getLoggedInUser());
     selectedCompAc.setChangedOn(new Date());
     selectedCompAc = this.glActMgr.updateGlAccountComp(selectedCompAc,  getView());
     ListIterator<FiGlAccountCompRec> li = this.selectedCoaAccountCompActs.listIterator();
     boolean foundCompAcnt = false;
     while(li.hasNext() && !foundCompAcnt){
      FiGlAccountCompRec curr = li.next();
      if(Objects.equals(curr.getId(), selectedCompAc.getId())){
       li.set(selectedCompAc);
       foundCompAcnt = true;
      }
     }
     if(foundCompAcnt){
      PrimeFaces pf = PrimeFaces.current();
      pf.ajax().update("compAcsTbl");
      pf.executeScript("PF('editCompAcntWv').hide()");
     }
    }
    public void setCoaSelList(ArrayList<SelectItem> coaSelList) {
      
     this.coaSelList = coaSelList;
    }

    public void onVatCodeChange(ValueChangeEvent evt){
     LOGGER.log(INFO, "onVatCodeChange called with: {0}",evt.getNewValue());
     VatCodeCompanyRec selected = (VatCodeCompanyRec)evt.getNewValue();
     LOGGER.log(INFO, "selected {0}", selected);
     glAccountComp.setVatCode(selected);
    }
    
    public List<VatCodeCompanyRec> onVatCodeComplete(String input){
     LOGGER.log(INFO, "onVatCodeComplete called with {0}", input);
     LOGGER.log(INFO, "selectedCompAc {0}", selectedCompAc);
     if(selectedCompAc == null){
      return null;
     }
     if(StringUtils.isBlank(input)){
      return sysBuffer.getCompVatCodes(selectedCompAc.getCompany());
     }else{
      return sysBuffer.getCompVatCodesByCode(selectedCompAc.getCompany(), input);
     }
    }
    
    public void onVatEditorSave(){
     LOGGER.log(INFO, "onVatEditorSave called {0}", glAccountComp.getVatCode().getVatCode().getDescription());
     vatCodeDescr = glAccountComp.getVatCode().getVatCode().getCode() +" "+glAccountComp.getVatCode().getVatCode().getDescription();
    }
    
    
    public void searchAction(){
      LOGGER.log(INFO, "Search Action called");
      try{
        this.glActMgr.getGlAccountsForCompany(selectedCompanyId);
        showCompAccounts = true;

      }catch(BacException e){
        GenUtil.addErrorMessage("Get companies error: "+e.getLocalizedMessage());
      }
      showGl = true;

    }

    public void updateCoaAccountAction(){
      LOGGER.log(INFO,"updateCoaAccountAction");
      FiGlAccountBaseRec updtAccount;
      if(glAccount.isPl()){
        updtAccount = this.addBaseToPlAct(plAccount, glAccount);
      }else{
        updtAccount = this.addBaseToBsAct(bsAccount, glAccount);
      }

      try{

        glActMgr.updateGlAccountCoa(updtAccount,this.getLoggedInUser(), this.getView());
        
        
      }catch(BacException e){
        GenUtil.addErrorMessage("Error updating GL account: "+updtAccount.getRef()+" : "+e.getLocalizedMessage());
      }
    }

    public void deleteCoaAccountAction(){
      LOGGER.log(INFO, "deleteCoaAccountAction");
      try{
        glActMgr.deleteGlAccountCoa(glAccount, this.getLoggedInUser());
        GenUtil.addInfoMessage("Deleted GL account: "+glAccount.getRef());
        LOGGER.log(INFO, "deleteCoaAccountAction ok");
      }catch(BacException ex){
        GenUtil.addErrorMessage("Could not delete Account: "+glAccount.getRef()+" reason: "+ex.getLocalizedMessage());
        LOGGER.log(INFO, "deleteCoaAccountAction BacException {0}", ex.getLocalizedMessage());
      }catch(Exception ex){
        GenUtil.addErrorMessage("Could not delete Account: "+glAccount.getRef()+" reason: "+ex.getLocalizedMessage());
        LOGGER.log(INFO, "deleteCoaAccountAction TransactionRolledbackLocalException {0}",ex.getLocalizedMessage());

      }
    }

    public void updateCompAccountAction(){
      LOGGER.log(INFO, "updateCompAccountAction");

      try{
        glActMgr.updateGlAccountComp(glAccountComp,  this.getView());
        GenUtil.addInfoMessage("Updated GL account number: "+this.glAccount.getRef());
      }catch(BacException ex){
        GenUtil.addErrorMessage("Error updating account number"+glAccount.getRef()+
                " name:"+glAccountComp.getCoaAccount().getName()+" reason: "+ex.getLocalizedMessage());
      }catch(Exception ex){
        GenUtil.addErrorMessage("Error updating account number"+glAccount.getRef()+
                " name:"+glAccount.getName()+" reason: "+ex.getLocalizedMessage());

      }
    }

    public void deleteCompccountAction(){
      LOGGER.log(INFO, "deleteCompccountAction");
      try{
        this.glActMgr.deleteGlAccountComp(glAccountComp, this.getLoggedInUser());
        GenUtil.addInfoMessage("Deleted company account ref: "+glAccount.getRef());
      }catch(BacException ex){
        GenUtil.addErrorMessage("Could not delete company account ref: "+glAccount.getRef());
      }catch(Exception ex){
        GenUtil.addErrorMessage("Transaction rollback Could not delete company account ref: "+glAccount.getRef());
      }

    }
    
    public void onSaveAccountComp(){
     LOGGER.log(INFO, "onSaveAccountComp called");
     glAccountComp.setCreatedBy(this.getLoggedInUser());
     glAccountComp.setCreatedOn(new Date());
     glAccountComp = this.glActMgr.updateGlAccountComp(glAccountComp, getView());
     if(glAccountComp.getId() == null){
      MessageUtil.addWarnMessage("glAcntCompCr", "errorText");
     }else{
      MessageUtil.addInfoMessage("glAcntCompCr", "blacResponse");
     }
    }
    
    
    public void onSaveAccountChangesAction(){
     LOGGER.log(INFO, "onSaveAccountChangesAction called (0}",glAccount);
     List<FiGlAccountCompRec> compAcs = glAccount.getCompanyAccounts();
     LOGGER.log(INFO, "compAcs is {0}", compAcs);
     if(this.plAccount != null){
      // must be a pl account transfer changable central data to pl account
      plAccount.setRef(glAccount.getRef());
      plAccount.setDescription(glAccount.getDescription());
      if(compAcs != null){
       plAccount.setCompanyAccounts(compAcs);
      }
      plAccount.setUpdateOn(new Date());
      plAccount.setUpdateBy(getLoggedInUser());
      try{
       plAccount = (FiPlAccountRec)glActMgr.updateGlAccountCoa(plAccount, getLoggedInUser(), this.getView());
       
        String msg = responseForKey("glAccountUpdated") + glAccount.getRef();
        String msgHdr = responseForKey("glAccountUpdatedHdr");
        MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
        
       
      }catch(BacException ex){
       String msg = this.responseForKey("glActUpdateError") + glAccount.getRef();
       GenUtil.addErrorMessage(msg);
      }
     }else{
      bsAccount.setRef(glAccount.getRef());
      bsAccount.setDescription(glAccount.getDescription());
      
      if(compAcs != null){
       bsAccount.setCompanyAccounts(compAcs);
      }
      bsAccount.setUpdateOn(new Date());
      bsAccount.setUpdateBy(this.getLoggedInUser());
      bsAccount = (FiBsAccountRec)glActMgr.updateGlAccountCoa(bsAccount, getLoggedInUser(), this.getView());
      
       String msg = this.responseForKey("glAccountUpdated") + glAccount.getRef();
       String msgHdr = this.responseForKey("glAccountUpdatedHdr");
       MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
     }
     
    }
    
    public void onSaveNewAccount(){
      LOGGER.log(INFO, "WEB saveAccountAction {0}", glAccountComp);
      if(glAccountComp == null){
       LOGGER.log(INFO, "glAccountComp to default values");
       glAccountComp = new FiGlAccountCompRec();
       }
       if(glAccountComp.getCompany() == null){
        List<CompanyBasicRec> compl = this.getCompList();
        glAccountComp.setCompany(compl.get(0));
       }
       
      LOGGER.log(INFO,"WEB saveAccountAction for account {0} for company {1}",
              new Object[]{glAccountComp,glAccountComp.getCompany()} );
      LOGGER.log(INFO,"sort order is {0} ",new Object[]{
        glAccountComp.getSortOrder()
      });
      LOGGER.log(INFO,"glAccountComp.getComp() {0} id {1}",new Object[]{glAccountComp.getCompany(), 
       glAccountComp.getCompany().getId()});
      Date curr = new Date();
      String msgHdr = this.responseForKey("glAccount");
      if(glAccount.getAccountType().isProfitAndLossAccount()){
        LOGGER.log(INFO, "PL account to save");
        // Add Basic details to pl account
        
          plAccount.setAccountType(glAccount.getAccountType());
          plAccount.setCreatedBy(getLoggedInUser());
          plAccount.setCreatedOn(curr);
          glAccountComp.setCreatedBy(plAccount.getCreatedBy());
          glAccountComp.setCreatedOn(curr);

        plAccount.setChartOfAccounts(glAccount.getChartOfAccounts());
        plAccount.setDescription(glAccount.getDescription());
        plAccount.setName(glAccount.getName());
        plAccount.setPl(glAccount.getAccountType().isProfitAndLossAccount());
        plAccount.setRef(glAccount.getRef());

        
        try{
          LOGGER.log(INFO, "plAccount is {0}", plAccount);
          
          glActMgr.createPlAccount(plAccount, glAccountComp,getView());
          String msg = this.responseForKey("glAccountCr") +" "+plAccount.getRef();
          MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
          accountCreated = true;
          
        }catch(BacException e){
         String msg = this.errorForKey("glAcntCr") +" "+plAccount.getRef();
         MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
          
        }


      }else{
        LOGGER.log(INFO, "BS account to save with bs account {0}",bsAccount);
        //populate common attributes
        if(bsAccount == null){
           bsAccount = new FiBsAccountRec();
        }
        bsAccount.setAccountType(glAccount.getAccountType());

        bsAccount.setChartOfAccounts(glAccount.getChartOfAccounts());
        bsAccount.setCreatedBy(this.getLoggedInUser());
        bsAccount.setCreatedOn(curr);
        bsAccount.setDescription(this.glAccount.getDescription());
        bsAccount.setName(this.glAccount.getName());
        bsAccount.setPl(this.glAccount.isPl());
        bsAccount.setRef(this.glAccount.getRef());
        glAccountComp.setCoaAccount(bsAccount);
        if(selectedCompany != null){
         glAccountComp.setCompany(selectedCompany);
        }
        glAccountComp.setCreatedBy(bsAccount.getCreatedBy());
        glAccountComp.setCreatedOn(curr);
        try{
         LOGGER.log(INFO, "Before call to createBsAccount glAccountComp {0} comp {1}", 
                 new Object[]{glAccountComp,glAccountComp.getCompany()});
          glActMgr.createBsAccount(bsAccount, glAccountComp,glAccount.isPl(),getView());
        
        LOGGER.log(INFO,"after call to Mgr");
        MessageUtil.addClientInfoMessage("glActMstfrm:grl", "glAccountCr", "blacResponse", glAccountComp.getCoaAccount().getRef());
        accountCreated = true;
        glAccountComp = null;
        
        } catch(Exception ex){
         String msg = this.errorForKey("glAcntCr") +" "+plAccount.getRef();
         MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
        }
      }
    }

    public String onSaveNewAccountAction(){
     if(accountCreated){
      return "home";
     }else{
      return null;
     }
    }
    
    public List<VatCodeCompanyRec> vatCodeComplete(String input){
     List<VatCodeCompanyRec> vatCdList;
     vatCdList = getVatCodeList();
     if(input == null || input.isEmpty()){
      return vatCdList;
     }else{
      input = input.trim();
      ListIterator<VatCodeCompanyRec> vatLi =  vatCdList.listIterator();
      while(vatLi.hasNext()){
       VatCodeCompanyRec rec = vatLi.next();
       if(!rec.getVatCode().getCode().startsWith(input)){
        vatLi.remove();
       }
      }
      return vatCdList;
     }
     
     
    }
public void vatSelect(SelectEvent evt){
 LOGGER.log(INFO, "vatSelect called with {0}", evt.getObject());
 this.glAccountVatable = true;
}

}
