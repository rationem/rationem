/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.ValueHolder;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.model.DualListModel;
/**
 *
 * @author Chris
 */

public class CompanyBankAccountBean extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger(CompanyBankAccountBean.class.getName());
 
 private BankAccountCompanyRec bankAcComp;
 private BankAccountCompanyRec bankAcCompSel;
 private BankRec bank;
 private BankBranchRec bankBranch;
 private List<BankRec> banks;
 private List<BankBranchRec> branches;
 private boolean bankFound =false;
 private boolean compSelected = false;
 private boolean bankBranchSelected = false;
 private List<FiGlAccountCompRec> glAccounts;
 
 private DualListModel<FiGlAccountCompRec> glAccountsDualListModel;
 private List<FiGlAccountCompRec> unclearedGlAcList = new ArrayList<>();
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BankManager bankMgr;

 /**
  * Creates a new instance of CompanyBankAccountBean
  */
 public CompanyBankAccountBean() {
  
 }

 
 public BankRec getBank() {
  return bank;
 }

 public void setBank(BankRec bank) {
  this.bank = bank;
 }

 public List<BankAccountCompanyRec> getCompBankAccountList(){
  List<BankAccountCompanyRec> retList = this.bankMgr.getBankAccntsCompAll();
  return retList;
 }
 public boolean isBankFound() {
  return bankFound;
 }

 public void setBankFound(boolean bankFound) {
  this.bankFound = bankFound;
 }

 public BankBranchRec getBankBranch() {
  return bankBranch;
 }

 public void setBankBranch(BankBranchRec bankBranch) {
  this.bankBranch = bankBranch;
 }

 public List<BankRec> getBanks() {
  return banks;
 }

 public void setBanks(List<BankRec> banks) {
  this.banks = banks;
 }

 public List<BankBranchRec> getBranches() {
  return branches;
 }

 public void setBranches(List<BankBranchRec> branches) {
  this.branches = branches;
 }

 

 public BankAccountCompanyRec getBankAcComp() {
  if(bankAcComp == null){
   bankAcComp = new BankAccountCompanyRec();
  }
  return bankAcComp;
 }

 public void setBankAcComp(BankAccountCompanyRec bankAcComp) {
  this.bankAcComp = bankAcComp;
 }

 public BankAccountCompanyRec getBankAcCompSel() {
  return bankAcCompSel;
 }

 public void setBankAcCompSel(BankAccountCompanyRec bankAcCompSel) {
  this.bankAcCompSel = bankAcCompSel;
 }

 
 public boolean isBankBranchSelected() {
  return bankBranchSelected;
 }

 public void setBankBranchSelected(boolean bankBranchSelected) {
  this.bankBranchSelected = bankBranchSelected;
 }

 public boolean isCompSelected() {
  return compSelected;
 }

 public void setCompSelected(boolean compSelected) {
  this.compSelected = compSelected;
 }

 
 public List<FiGlAccountCompRec> getGlAccounts() {
  return glAccounts;
 }

 public void setGlAccounts(List<FiGlAccountCompRec> glAccounts) {
  this.glAccounts = glAccounts;
 }

 public DualListModel<FiGlAccountCompRec> getGlAccountsDualListModel() {
  if(glAccountsDualListModel == null){
   if(glAccounts == null){
    glAccounts = new ArrayList<>();
   }
   if(unclearedGlAcList == null){
    unclearedGlAcList = new ArrayList<>();
   }
   glAccountsDualListModel = new DualListModel(glAccounts, unclearedGlAcList);
  }
  return glAccountsDualListModel;
 }

 public void setGlAccountsDualListModel(DualListModel<FiGlAccountCompRec> glAccountsDualListModel) {
  this.glAccountsDualListModel = glAccountsDualListModel;
 }

 public List<FiGlAccountCompRec> getUnclearedGlAcList() {
  return unclearedGlAcList;
 }

 public void setUnclearedGlAcList(List<FiGlAccountCompRec> unclearedGlAcList) {
  this.unclearedGlAcList = unclearedGlAcList;
 }
@PostConstruct
 private void init(){
  bankAcComp = new BankAccountCompanyRec();
  if(getCompList() == null){
   MessageUtil.addWarnMessage("compsNone", "errorText");
  }
  CompanyBasicRec comp = getCompList().get(0);
  bankAcComp.setComp(comp);
 }
 
 public void onBankBlur(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "onBankBlur caled with {0}", evt.getSource());
 }
 
 public void onBankAccountNumberBlur(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "onBankAccountNumberBlur caled with {0}", 
          ((ValueHolder)evt.getSource()).getValue());
  boolean bankAcExists = bankMgr.bankAccountForBranchExists(bankAcComp.getAccountForBranch(), bankAcComp);
  LOGGER.log(INFO, "bankAcExists {0}", bankAcExists);
  if(bankAcExists){
   GenUtil.addErrorMessage(this.errorForKey("bnkAcDupl"));
   this.bankAcComp.setAccountNumber(null);
  }
 }
 
 public void onBankSelect(SelectEvent se){
  LOGGER.log(INFO, "onBankSelect called with {0}", se.getObject());
  bank = (BankRec)se.getObject();
  bankFound = true;
  // set the branches for this bank
  if(bank == null){
   return;
  }
  try{
   branches = bankMgr.getBranchesForBank(bank);
  }catch(BacException ex){
   MessageUtil.addErrorMessage("bnkBankBrNone", "errorText");
   
  }
 }
 
 public void onBankBranchSelect(SelectEvent se){
  LOGGER.log(INFO, "onBankBranchSelect called with {0} ", se.getObject());
  this.bankBranchSelected = true;
 }
 public void onCompanySelect(SelectEvent se){
  LOGGER.log(INFO, "onCompanySelect called with {0}", se);
  CompanyBasicRec selectedComp = (CompanyBasicRec)se.getObject();
  bankAcComp.setComp(selectedComp);
  if(glAccounts == null){
   glAccounts = new ArrayList<FiGlAccountCompRec>();
  }
  List<FiGlAccountCompRec> glAcs = sysBuff.getGlAccountsByCompanyCode(selectedComp);
  for (FiGlAccountCompRec compAc : glAcs ){
   FiGlAccountBaseRec chartAc = compAc.getCoaAccount();
   LOGGER.log(INFO, "compAc name {0} pl {1} acc class {2} account cat {3}", new Object[]{
    chartAc.getRef(), chartAc.isPl(), chartAc.getClass().getSimpleName(), chartAc.getAccountType().getProcessCode() });
   if(chartAc.getClass().getSimpleName().equalsIgnoreCase("FiBsAccountRec") && 
           chartAc.getAccountType().getProcessCode().getName().equalsIgnoreCase("CABNK")){
    glAccounts.add(compAc);
    
   }
  }
  if(unclearedGlAcList == null){
    unclearedGlAcList = new ArrayList<FiGlAccountCompRec>();
   }
   glAccountsDualListModel = new DualListModel(glAccounts, unclearedGlAcList);
  this.compSelected = true;
  LOGGER.log(INFO, "Bank GL accounts {0}", glAccounts);
 }
 
 public void onSaveCreateOwnBankAc(){
  LOGGER.log(INFO, "onSaveCreateOwnBankAc");
  try{
   
   bankAcComp = bankMgr.createOwnBankAccount(bankAcComp, this.getLoggedInUser(), this.getView());
   GenUtil.addInfoMessage(this.responseForKey("trBnkAcOwnCr"));
  }catch(BacException ex){
   GenUtil.addErrorMessage(this.errorForKey("bnkBankAcOwnCr"));
  }
 }
 public List<BankRec> bankComplete(String input){
  List<BankRec> retList;
  if(input == null || input.isEmpty()){
   if(banks == null){
     banks = bankMgr.getBanks();
     return banks;
   }else{
    if(banks == null){
     banks = bankMgr.getBanksByCode(input);
     return banks;
    }else{
     retList = new ArrayList<BankRec>();
     for (BankRec bnk : banks){
      if(bnk.getBankCode().startsWith(input)){
       retList.add(bnk);
      }
     }
     if(retList.isEmpty()){
      //we did not have the bank
      List<BankRec> addBnks = bankMgr.getBanksByCode(input);
      for(BankRec bnk : addBnks){
       banks.add(bnk);
      }
      for (BankRec bnk : banks){
       if(bnk.getBankCode().startsWith(input)){
        retList.add(bnk);
        
       }
       
      }
      
     }
     return retList;
    }
   
   }
   
  
  }
  LOGGER.log(INFO, "Bank not found banks {0}", banks);
  return null;
 }
 
 public List<BankBranchRec> bankBranchComplete(String input){
  LOGGER.log(INFO, "bankBranchComplete completed with input {0}", input);
  if(input == null || input.isEmpty()){
   return branches;
  }
  List<BankBranchRec> branchRetList = new ArrayList<BankBranchRec>();
  for( BankBranchRec branchRec : branches){
   if(branchRec.getSortCode().startsWith(input)){
    branchRetList.add(branchRec);
    
   }
  }
  
  return branchRetList;
 }
 public List<CompanyBasicRec> compComplete(String input){
  LOGGER.log(INFO, "compComplete called with {0}", input);
  if(input == null || input.isEmpty()){
   List<CompanyBasicRec> compl = getCompList();
   return compl;
  }else{
   List<CompanyBasicRec> compListRet = new ArrayList<CompanyBasicRec>();
   ListIterator<CompanyBasicRec> compsLi = this.getCompList().listIterator();
   while(compsLi.hasNext()){
    CompanyBasicRec compRec = compsLi.next();
    
    if(compRec.getReference().startsWith(input)){
     compListRet.add(compRec);
    }
   }
   if(compListRet.isEmpty()){
    GenUtil.addInfoMessage(responseForKey("trBnkCompNotFound"));
   }
   return compListRet;
  }
 }
 
 public List<FiGlAccountCompRec> glAccountComplete(String input){
  LOGGER.log(INFO, "glAccountComplete called with {0}", input);
  if(input == null || input.isEmpty()){
   return this.glAccounts;
  }
  
  List<FiGlAccountCompRec> glAcsRet = new ArrayList<FiGlAccountCompRec>();
  for (FiGlAccountCompRec glAc : glAccounts){
   if(glAc.getCoaAccount().getRef().startsWith(input)){
    glAcsRet.add(glAc);
    
   }
  }
  return glAcsRet;
 }
 
 
}
