/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp.arap;

import com.rationem.util.GenUtil;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.ArApSetup;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.tr.PaymentMediumManager;
import com.rationem.exception.BacException;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Chris
 */
public class PaymentTypeBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(PaymentTypeBean.class.getName());
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 private PaymentTypeRec payType;
 private CompanyBasicRec comp;
 private List<PaymentTypeRec> payTypes;
 private List<PaymentTypeRec> payTypesFiltered;
 private boolean compSelected = false;
 private boolean bankSelected = false;
 private boolean payTypeSelected = false;
 private boolean bacsTransDisabled = true;
 private List<LedgerRec> ledgers;
 List<BankAccountCompanyRec> bankAcsComp;
 List<BacsTransCodeRec> bacsTransCodes;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BankManager bnkMgr;
 
 @EJB
 private ArApSetup arapSetup;
 
 @EJB
 private PaymentMediumManager payMediumMgr; 

 /**
  * Creates a new instance of PaymentTypeBean
  */
 public PaymentTypeBean() {
  LOGGER.log(INFO, "PaymentTypeBean constructor called");
 }
 
 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  
  comp = getCompList().get(0);
  LOGGER.log(INFO, "getViewSimple() {0}", getViewSimple());
  if(StringUtils.equals(getViewSimple(), "payTypeList")){
   payTypes = this.sysBuff.getPaymentTypes();
   LOGGER.log(INFO, "payTypes {0}", payTypes);
  }  
  
  
  
 }

 public List<BacsTransCodeRec> getBacsTransCodes() {
  if(bacsTransCodes == null){
   bacsTransCodes = new ArrayList<>();
  }
  return bacsTransCodes;
 }

 public void setBacsTransCodes(List<BacsTransCodeRec> bacsTransCodes) {
  this.bacsTransCodes = bacsTransCodes;
 }

 
 public List<BankAccountCompanyRec> getBankAcsComp() {
  if(bankAcsComp == null){
   LOGGER.log(INFO, "getBankAcsComp called. Comp is ", payType.getCompany());
   if(payType.getCompany() == null){
    List<CompanyBasicRec> compList = sysBuff.getCompanies();
    payType.setCompany(compList.get(0));
   }
   bankAcsComp = bnkMgr.getBankAccountsForCompany(payType.getCompany());
   LOGGER.log(INFO, "bank mgr returns {0}", bankAcsComp);
  }
  return bankAcsComp;
 }

 public void setBankAcsComp(List<BankAccountCompanyRec> bankAcsComp) {
  this.bankAcsComp = bankAcsComp;
 }

 public boolean isBankSelected() {
  return bankSelected;
 }

 public void setBankSelected(boolean bankSelected) {
  this.bankSelected = bankSelected;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 
 public boolean isCompSelected() {
  return compSelected;
 }

 public void setCompSelected(boolean compSelected) {
  this.compSelected = compSelected;
 }

 public List<LedgerRec> getLedgers() {
  if(ledgers == null){
   ledgers = new ArrayList<>();
   List<LedgerRec> ledgersT = sysBuff.getLedgers();
   for (LedgerRec led : ledgersT){
    if(led.getCode().equalsIgnoreCase("AR") || led.getCode().equalsIgnoreCase("AP")){
     ledgers.add(led);
    }
   }
  }
  return ledgers;
 }

 public void setLedgers(List<LedgerRec> ledgers) {
  this.ledgers = ledgers;
 }

 
 public PaymentTypeRec getPayType() {
  if(payType == null){
   payType = new PaymentTypeRec();
  }
  return payType;
 }

 public void setPayType(PaymentTypeRec payType) {
  this.payType = payType;
 }

 public List<PaymentTypeRec> getPayTypes() {
  return payTypes;
 }

 public void setPayTypes(List<PaymentTypeRec> payTypes) {
  this.payTypes = payTypes;
 }

 public List<PaymentTypeRec> getPayTypesFiltered() {
  return payTypesFiltered;
 }

 public void setPayTypesFiltered(List<PaymentTypeRec> payTypesFiltered) {
  this.payTypesFiltered = payTypesFiltered;
 }

 
 public boolean isPayTypeSelected() {
  return payTypeSelected;
 }

 public void setPayTypeSelected(boolean payTypeSelected) {
  this.payTypeSelected = payTypeSelected;
 }

 public boolean isBacsTransDisabled() {
  if(payType == null || payType.getPayMedium() == null){
   bacsTransDisabled = true;
   return bacsTransDisabled;
  }
  bacsTransDisabled = (!payType.getPayMedium().equalsIgnoreCase("DD") && !payType.getPayMedium().equalsIgnoreCase("DC"));
  return bacsTransDisabled;
 }

 public void setBacsTransDisabled(boolean bacsTransDisabled) {
  this.bacsTransDisabled = bacsTransDisabled;
 }
 
 public List<BankAccountCompanyRec> bankAccountComplete(String input){
  LOGGER.log(INFO, "bankAccountComplete called comp is {0}", payType.getCompany());
  LOGGER.log(INFO, "Called with inputn {0}", input);
  if(input == null || input.isEmpty()){
   
   return getBankAcsComp();
  }else{
   List<BankAccountCompanyRec> retList = new ArrayList<>();
   for(BankAccountCompanyRec ac : getBankAcsComp()){
    if(ac.getAccountNumber().startsWith(input)){
     retList.add(ac);
    }
   }
   return retList;
  }
  
 }
 public List<CompanyBasicRec> compComplete(String input){
  List<CompanyBasicRec> compList = sysBuff.getCompanies(); 
  if(input == null || input.isEmpty()){
   
   return compList;
  }else{
   List<CompanyBasicRec> retList = new ArrayList<>();
   for(CompanyBasicRec comprec : compList ){
    if(comprec.getReference().startsWith(input)){
     retList.add(comprec);
    }
   }
   return retList;
  }
 }
 
 public List<FiGlAccountCompRec> onGlAccountComplete(String input){
  LOGGER.log(INFO, "glAccountComplete called with input {0} bank id {1}", 
          new Object[]{input, payType.getPayTypeForBankAccount().getId()});
  if(payType.getPayTypeForBankAccount() == null || payType.getPayTypeForBankAccount().getId() == 0){
   return null;
  }
  LOGGER.log(INFO, "GL accounts for bank a/c {0}", payType.getPayTypeForBankAccount().getUnclearedGlAccounts());
  
  List<FiGlAccountCompRec> retList;
  
  if(input == null || input.isEmpty()){
   
   retList = glAcntMgr.getGlAccountsForBankUnclr(payType.getPayTypeForBankAccount());
   return retList;
   
  }else{
   retList = new ArrayList<>();
   if(glAcntMgr.getGlAccountsForBankUnclr(payType.getPayTypeForBankAccount()) == null){
    MessageUtil.addClientWarnMessage("payTypeCr:errMsg", "payTyUnClGL", "errorText");
    RequestContext.getCurrentInstance().update("payTypeCr:errMsg");
    return null;
   }
   for(FiGlAccountCompRec bnkGlAc : glAcntMgr.getGlAccountsForBankUnclr(payType.getPayTypeForBankAccount())){
    if(bnkGlAc.getCoaAccount().getRef().startsWith(input)){
     retList.add(bnkGlAc);
    }
   }
   return retList;
  }
 }
 public List<PaymentTypeRec> payTypeComplete(String input){
  LOGGER.log(INFO, "payTypeComplete called comp id {0} ", comp.getId());
  List<PaymentTypeRec> payTypeList = sysBuff.getPaymentTypes(comp);
  if(input == null || input.isEmpty()){
   return payTypeList;
  }else{
   List<PaymentTypeRec> listRet = new ArrayList<>();
   for(PaymentTypeRec payType : payTypeList){
    if(payType.getPayTypeCode().startsWith(input)){
     listRet.add(payType);
    }
   }
   return listRet;
  }
  
 }
 
 public void onCompSelect(SelectEvent se){
  LOGGER.log(INFO, "onCompSelect called with {0}", se.getObject());
  comp = (CompanyBasicRec)se.getObject();
  payType.setCompany(comp);
  bankAcsComp = bnkMgr.getBankAccountsForCompany(payType.getCompany());
  LOGGER.log(INFO, "bankAcsComp {0}",bankAcsComp);
  compSelected = true;
  LOGGER.log(INFO, "company selected is: {0}",payType.getCompany().getId());
  LOGGER.log(INFO, "comp is: {0}",comp.getId());
 }
 
 public void onBankSelect(SelectEvent se){
  LOGGER.log(INFO, "onCompSelect called with {0}", se.getObject());
  bankSelected = true;
 }
 public void onGlAccountSelect(SelectEvent se){
  LOGGER.log(INFO, "onGlAccountSelect called with {0}", se.getObject());
 }
 
 public void onPayMediumChange(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "onPayMediumChange caled  with {0}", evt.getSource());
  updateBacTransCodes(payType);
  UomRec payMediumUom = this.sysBuff.getUomByCode(payType.getPayMedium());
  payType.setMediumUom(payMediumUom);
 }
 
 public void onPayTypeSelect(SelectEvent se){
  LOGGER.log(INFO, "onPayTypeSelect called with {0}", se.getObject());
  payType = (PaymentTypeRec)se.getObject();
  LOGGER.log(INFO, "payType comp id {0}", payType.getCompany().getId());
  LOGGER.log(INFO, "payType mediun {0}", payType.getPayMedium());
  if(payType.getPayMedium().equalsIgnoreCase("DD") || payType.getPayMedium().equalsIgnoreCase("DC")){
   updateBacTransCodes(payType);
  }
  if(payType.getPayTypeForBankAccount() != null){
   bankSelected = true;
  }
  payTypeSelected = true;
  
  }
 
 public void onPayTypeCodeBlur(AjaxBehaviorEvent evt){
  LOGGER.log(INFO, "Pay type code {0}", payType.getPayTypeCode());
  List<PaymentTypeRec> payTypeList = sysBuff.getPaymentTypes(comp);
  if(payType == null){
   payType = payTypeList.get(0);
  }
  boolean payTypeCodeFound = false;
  ListIterator<PaymentTypeRec> li = payTypeList.listIterator();
  while(li.hasNext() && !payTypeCodeFound){
   PaymentTypeRec pt = li.next();
   
   if(pt.getPayTypeCode().equalsIgnoreCase(payType.getPayTypeCode())){
    payTypeCodeFound = true;
   }
  }
  if(payTypeCodeFound){
   GenUtil.addErrorMessage(validationForKey("payTyCodeDupl"));
   payType.setPayTypeCode(null);
   return;
  }
  payTypeSelected = true;
  
  
 }
 
 public void onResetCreate(){
  LOGGER.log(INFO, "onResetCreate");
  payType = null;
 }
 public void onSaveCreate(){
  LOGGER.log(INFO, "onSaveCreate called with payType called pay type comp is {0}",payType.getCompany());
  payType.setCreatedBy(this.getLoggedInUser());
  payType.setCreatedOn(new Date());
  try{
   payType = arapSetup.paymentTypeCreate(payType, getLoggedInUser(), getView());
   GenUtil.addInfoMessage(responseForKey("payTyCreateOk"));
   payType = new PaymentTypeRec();
   compSelected = false;
   bankSelected = false;
   payTypeSelected = false;
   bacsTransDisabled = true;
  }catch(BacException ex){
   GenUtil.addErrorMessage(errorForKey("payTyCreateErr"));
  }
 }
 
 public void onSaveUpdate(){
  LOGGER.log(INFO, "onSaveUpdate for payType  id {0}", payType.getId());
  LOGGER.log(INFO, "onSaveUpdate for comp  id {0}", comp.getId());
  LOGGER.log(INFO, "PayType comp {0}", payType.getCompany());
  
  if(payType.getId() == null){
   // new payment type
   payType.setCreatedBy(this.getLoggedInUser());
   payType.setCreatedOn(new Date());
  }else{
   // update
   payType.setChangedBy(this.getLoggedInUser());
   payType.setChangedOn(new Date());
  }
  try{
   LOGGER.log(INFO, "changed by  {0}", payType.getChangedBy());
   LOGGER.log(INFO, "Created by {0}", payType.getCreatedBy());
   
   payType = arapSetup.updatePaymentType(payType,  getView());
   if(StringUtils.equals(getViewSimple(), "paymentTypeCreate")){
    MessageUtil.addClientInfoMessage("payTypeCr:successMsg", "payTyCreateOk", "blacResponse");
    RequestContext.getCurrentInstance().update("payTypeCr:successMsg");
   }else if (StringUtils.equals(getViewSimple(), "paymentTypeUpdate")){
    MessageUtil.addClientInfoMessage("payTypeUpdt:successMsg", "payTyUpdateOk", "blacResponse");
    RequestContext.getCurrentInstance().update("payTypeUpdt:successMsg");
  }
  }catch(BacException ex){
   MessageUtil.addClientErrorMessage("payTypeCr:errMsg", "payTyUpdateErr", "errorText");
   RequestContext.getCurrentInstance().update("payTypeCr:errMsg");
  }
 }
 
 public List<ChequeTemplateRec> onChequeTemplateComplete(String input){
  List<ChequeTemplateRec> retList =   payMediumMgr.getChqTemplByRef(input);
  LOGGER.log(INFO, "onChequeTemplateComplete returns {0}", retList);
  return retList;
 }
 
 public void updateBacTransCodes(PaymentTypeRec pt){
  LOGGER.log(INFO, "updateBacTransCodes called with payType code {0}", pt.getPayTypeCode());
  if(pt.getPayMedium().equalsIgnoreCase("DD") ||pt.getPayMedium().equalsIgnoreCase("DC")){
   LOGGER.log(INFO, "DD payment types");
   bacsTransCodes = bnkMgr.getBacsTransCodesByDirection(pt.getPayMedium());
   LOGGER.log(INFO, "bank Mgr returns bacs codes {0}", bacsTransCodes);
   
  }else {
   LOGGER.log(INFO, "Not bacs");
   bacsTransCodes = null; //this.bnkMgr.getBacsTransCodesByDirection(pt.getPayMedium());
   LOGGER.log(INFO, "bank Mgr returns bacs codes {0}", bacsTransCodes);
  }
 }
}
