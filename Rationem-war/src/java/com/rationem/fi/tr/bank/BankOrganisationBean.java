/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr.bank;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Chris
 */
public class BankOrganisationBean extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger("accounts.fi.tr.bank.BankOrganisationBean");

    @EJB
    private SysBuffer sysBuffer;

    @EJB
    private GlAccountManager glActMgr;
    
    @EJB
    private MasterDataManager masterDta;
    
    @EJB
    private BankManager bnkMgr;
    
    private BankRec bankOrg;
    private PartnerCorporateRec bankOrgParty;
    private PartnerPersonRec bankContact;
    private List<PartnerCorporateRec> bankOrgList;
    private List<AddressRec> bankAddrList;
    private AddressRec bankAddr;
    private AddressRec bankBranchAddr;
    private AddressRec bankAddrSel;
    private List<BankRec> bankList;
    private BankBranchRec bankBranch;
    private boolean bnkContactUsed;
    private int bnkContactChanges;

 /**
  * Creates a new instance of BankOrganisationBean
  */
 public BankOrganisationBean() {
  LOGGER.log(INFO, "BankOrganisationBean contructor");
 }

 public AddressRec getBankAddr() {
  if(bankAddr == null){
   bankAddr = new AddressRec();
  }
  return bankAddr;
 }

 public void setBankAddr(AddressRec bankAddr) {
  this.bankAddr = bankAddr;
 }

 public List<AddressRec> getBankAddrList() {
  return bankAddrList;
 }

 public void setBankAddrList(List<AddressRec> bankAddrList) {
  this.bankAddrList = bankAddrList;
 }

 public AddressRec getBankAddrSel() {
  return bankAddrSel;
 }

 public void setBankAddrSel(AddressRec bankAddrSel) {
  this.bankAddrSel = bankAddrSel;
 }

 public BankBranchRec getBankBranch() {
  if(bankBranch == null){
   bankBranch = new BankBranchRec();
  }
  return bankBranch;
 }

 public void setBankBranch(BankBranchRec bankBranch) {
  this.bankBranch = bankBranch;
 }

 
 public AddressRec getBankBranchAddr() {
  if(bankBranchAddr == null){
   bankBranchAddr = new AddressRec(); 
  }
  return bankBranchAddr;
 }

 public void setBankBranchAddr(AddressRec bankBranchAddr) {
  this.bankBranchAddr = bankBranchAddr;
 }

 
 public PartnerPersonRec getBankContact() {
  if(bankContact == null){
   bankContact = new PartnerPersonRec();
  }
  return bankContact;
 }

 public void setBankContact(PartnerPersonRec bankContact) {
  this.bankContact = bankContact;
 }

 public List<BankRec> getBankList() {
  if(bankList == null){
   bankList = this.bnkMgr.getBanks();
  }
  return bankList;
 }

 public void setBankList(List<BankRec> bankList) {
  this.bankList = bankList;
 }

 public BankRec getBankOrg() {
  if(bankOrg == null){
   bankOrg = new BankRec();
  }
  return bankOrg;
 }

 public void setBankOrg(BankRec bankOrg) {
  this.bankOrg = bankOrg;
 }

 public List<PartnerCorporateRec> getBankOrgList() {
  if(bankOrgList == null){
   PartnerRoleRec rl = this.sysBuffer.getPartnerRoleByCode("BNK");
   List<PartnerBaseRec> ptnrs = masterDta.getPartnerByRole(rl);
   if(ptnrs == null){
    return null;
   }
   for(PartnerBaseRec rec: ptnrs){
    if(rec.getClass().getSimpleName().equals("PartnerCorporateRec")){
     bankOrgList.add((PartnerCorporateRec)rec);
    }
   }
   
  }
  return bankOrgList;
 }
 

 public void setBankOrgList(List<PartnerCorporateRec> bankOrgList) {
  this.bankOrgList = bankOrgList;
 }

 public PartnerCorporateRec getBankOrgParty() {
  if(bankOrgParty == null){
   bankOrgParty = new PartnerCorporateRec(); 
  }
  return bankOrgParty;
 }

 public void setBankOrgParty(PartnerCorporateRec bankOrgParty) {
  this.bankOrgParty = bankOrgParty;
 }
 
 
 public List<BankRec> bankComplete(String input){
  List<BankRec> bankCompList;
  
  if(input == null || input.isEmpty()){
   bankList = this.bnkMgr.getBanks();
   return bankList;
  }else{
   bankCompList = new ArrayList<BankRec>();
   ListIterator<BankRec> bankLi = getBankList().listIterator();
   boolean bankFound = false;
   while(bankLi.hasNext() && !bankFound){
    BankRec bnk = bankLi.next();
    if(bnk.getBankCode().startsWith(input)){
     
     bankCompList.add(bnk);
    }
   }
   if(bankFound){
    return bankCompList;
   }else{
    bankList = this.bnkMgr.getBanksByCode(input);
    return bankList;
    
   }
           
  }
 }
  
  
 public List<PartnerPersonRec> bankContactCompl(String input){
  List<PartnerPersonRec> contacts = masterDta.getIndivPtnrsBySurname(input);
  
  return contacts;
 }
 public List<AddressRec> bankOrgAddrCompl(String input){
  if(input == null){
   input = " ";
  }
  List<AddressRec> addrlst = masterDta.getAddressesForPostCodePart(input);
  
  return addrlst; 
 }
 public List<PartnerCorporateRec> bankOrgCompl(String input){
  List<PartnerCorporateRec> retList = new ArrayList<PartnerCorporateRec>();
  if(input == null || input.isEmpty()){
   return getBankOrgList();
  }else{
   ListIterator<PartnerCorporateRec> bnkOrgLi = getBankOrgList().listIterator();
   while(bnkOrgLi.hasNext()){
    PartnerCorporateRec ptnr = bnkOrgLi.next();
    if(ptnr.getRef().startsWith(input)){
     retList.add(ptnr);
    }
   }
  }
  
  return retList;
 }

 public void onCreateBankContact(ActionEvent ae){
  LOGGER.log(INFO, "onCreateBankContact called with {0}", ae.getSource());
  LOGGER.log(INFO, "family name {0} ",bankContact.getFamilyName());
  bankOrg.setBankContact(bankContact);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("bnkMgr");
  pf.executeScript("PF('bnkContactCrWv').hide();");
  bnkContactUsed = true;
 }
 
 public void onAddressSelect(SelectEvent se){
  LOGGER.log(INFO, "onAddressSelect called {0}",se.getObject() );
  bankAddr = (AddressRec)se.getObject();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addressGrd");
  pf.executeScript("PF('addrSrchWv').hide();");
 }
 
 public void onBranchAddressClear(){
  LOGGER.log(INFO, "onBranchAddressClear called");
  bankBranchAddr = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addressGrd");
  
 }
 public void onBranchAddressSelect(SelectEvent evt){
  LOGGER.log(INFO, "onBranchAddressSelect called {0}",evt.getObject() );
  bankBranchAddr = (AddressRec)evt.getObject();
  LOGGER.log(INFO, "bankBranchAddr post cd {0}", bankBranchAddr.getPostCode());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addressGrd");
  pf.executeScript("PF('addrSrchWv').hide();");
 }
 
 public void onBranchAddressDlg(){
  
  LOGGER.log(INFO, "onAddressDlg called with post code {0}", bankBranchAddr);
  if(bankBranchAddr != null){
  bankAddrList = masterDta.getAddressesForPostCodePart(bankBranchAddr.getPostCode());
  }else{
   bankAddrList = masterDta.getAddressesForPostCodePart(new String());
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addRSchFrm");
  pf.executeScript("PF('addrSrchWv').show();");
 }
 public void onAddressDlg(){
  LOGGER.log(INFO, "onAddressDlg called with post code {0}", this.bankAddr.getPostCode());
  bankAddrList = masterDta.getAddressesForPostCodePart(bankAddr.getPostCode());
  LOGGER.log(INFO, "Addresses found {0}", bankAddrList);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addRSchFrm");
  pf.executeScript("PF('addrSrchWv').show();");
 }
 
 public void onAddressClear(){
  bankAddr =  new AddressRec();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addrPnl"); 
 }
 
 public void onBnkContactChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onBnkContactChange called with {0}", evt.getNewValue());
 }
 public void onBnkContactSelect(SelectEvent evt){
  LOGGER.log(INFO, "onBnkContactSelect for partner {0}", ((PartnerPersonRec)evt.getObject()).getName());
  this.bnkContactUsed = true;
 }
 
 public void onBnkContactUnSelect(UnselectEvent evt){
  LOGGER.log(INFO, "onBnkContactUnselect for partner {0}", ((PartnerPersonRec)evt.getObject()).getName());
  this.bnkContactUsed = false;
 }
 
 
 public void onBankMgrDlg(){
  PrimeFaces pf = PrimeFaces.current();
  //pf.ajax().update("bnkContactCr:bnkContactCrGrd");
  pf.executeScript("PF('bnkContactCrWv').show()");
  LOGGER.log(INFO, "onBankMgrDlg called");
 }
 public void onBankSelect(SelectEvent se){
  LOGGER.log(INFO,"onBankSelect called with bank {0} ",se.getObject());
 }
 public void onCreateBank(){
  LOGGER.log(INFO, "bank org {0}", this.bankOrg);
  if(bankOrg.getBankCode() == null || bankOrg.getBankCode().isEmpty()){
   MessageUtil.addErrorMessage("bnkBankCode", "errorText");
   return;
  }
  LOGGER.log(INFO, "Bank org {0}", bankOrgParty.getTradingName());
  if(bankOrgParty.getTradingName() == null || bankOrgParty.getLegalName() == null ){
   MessageUtil.addErrorMessage("bnkBankName", "errorText");
   return;
  }
  
  if(bankOrg.getBankAddress() != null){
  if((bankOrg.getBankAddress().getId() == null || bankOrg.getBankAddress().getId() == 0) &&
          (bankOrg.getBankAddress().getPostCode() != null && !bankOrg.getBankAddress().getPostCode().isEmpty())){
   LOGGER.log(INFO, "Need to create new address {0}", bankOrg.getBankAddress().getPostCode());
  }
  }
  //logger.log(INFO, "Bank contact {0}", bankOrg.getBankContact().getFamilyName());
  //if(bankOrg.getBankContact() != null && (bankOrg.getBankContact().getFamilyName() != null && !bankOrg.getBankContact().getFamilyName().isEmpty() && bankOrg.getBankContact().getId() != 0)){
  // LOGGER.log(INFO, "Need to create contact for bank {0}", bankOrg.getBankContact().getFamilyName());
 // }
  if(!this.bnkContactUsed){
   bankOrg.setBankContact(null);
  }else{
   if(bankContact.getId() == null){
   bankContact.setCreatedBy(getLoggedInUser());
   bankContact.setCreatedDate(new Date());
   }else{
    bankContact.setChangedBy(getLoggedInUser());
    bankContact.setChangedOn(new Date());
   }
   bankOrg.setBankContact(bankContact);
  }
  if(bankAddr.getPostCode() != null && !bankAddr.getPostCode().isEmpty()){
   LOGGER.log(INFO, "Add address with post code {0} to bankOrg", bankAddr.getPostCode());
   if(bankAddr.getId() == null){
    bankAddr.setCreatedBy(this.getLoggedInUser());
    bankAddr.setCreatedOn(new Date());
   }else{
    bankAddr.setChangedBy(getLoggedInUser());
    bankAddr.setChangedOn(new Date());
   }
   bankOrg.setBankAddress(bankAddr);
  }
  try{
   bankOrgParty.setCreatedBy(this.getLoggedInUser());
   bankOrgParty.setCreatedDate(new Date());
   bankOrgParty.setCategory(this.fieldNameForKey("BNK_BANK_PARTY_CAT"));
   bankOrg.setBankOrganisation(bankOrgParty);
   bankOrg.setCreatedBy(this.getLoggedInUser());
   bankOrg.setCreatedOn(new Date());
   
  masterDta.createBankOrganisation(bankOrg,  this.getView());
  MessageUtil.addInfoMessage("bnkCr", "blacResponse");
  bankAddr = null;
  bankOrgParty = null;
  bankOrg = null;
  bankContact = null;
  bankAddr = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("bnkOrgCr");
  }catch(Exception ex){
   LOGGER.log(WARNING, "Create bank failed {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("bnkBankCr", "errorText");
   
  }
  
 }
 
 public void onCreateBankBranch(){
  LOGGER.log(INFO, "onCreateBankBranch");
  // set sort code to correct value
  
  LOGGER.log(INFO, "Branch sort code {0}", bankBranch.getSortCode());
  try{
   bankBranch = this.bnkMgr.createBankBranch(bankBranch, this.getLoggedInUser(),this.getView());
   
   MessageUtil.addInfoMessage("trBnkBrCreated", "blacResponse");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("bnkBankBrCr", "errorText");
   
  }
 }
 public void onSortCodeBlur(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "onSortCodeBlur {0}",((org.primefaces.component.inputmask.InputMask)evt.getSource()).getValue());
  String input = ((org.primefaces.component.inputmask.InputMask)evt.getSource()).getValue().toString();
  if(input.length() != 8){
   String msg = "Sort code incorrect";
   GenUtil.addErrorMessage(msg);
  }else{
   String[] sortCdParts = input.split("-");
   StringBuilder sb = new StringBuilder();
   sb.append(sortCdParts[0]);
   sb.append(sortCdParts[1]);
   sb.append(sortCdParts[2]);
   bankBranch.setSortCode(sb.toString());
  }
 }
}
