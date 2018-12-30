/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr.bank;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class ChequeBookBean extends BaseBean {
 private static final Logger LOGGER =    Logger.getLogger(ChequeBookBean.class.getName());
 
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BankManager bnkMgr;
 
 @EJB
 private BasicSetup setupMgr;
 private List<BankAccountCompanyRec> bnkAcs;
 
 private List<NumberRangeChequeRec> chequeBooks;
 private NumberRangeChequeRec chequeBkSel;
 private CompanyBasicRec compSel;
 private ModuleRec bankModule;
 /**
  * Creates a new instance of ChequeBookBean
  */
 public ChequeBookBean() {
 }
 
 @PostConstruct
 private void init(){
  
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  compSel = getCompList().get(0);
  
  LOGGER.log(INFO, "Current view {0}", getViewSimple());
  if(StringUtils.equals(getViewSimple(), "chequeBookCr")){
   bnkAcs = bnkMgr.getBankAccountsForCompany(compSel);
   chequeBkSel = new NumberRangeChequeRec();
   if(bnkAcs != null && !bnkAcs.isEmpty()){
    chequeBkSel.setBankAccountComp(bnkAcs.get(0));
   }
   bankModule = sysBuff.getModuleByCode("TR");
   LOGGER.log(INFO, "Bank module {0}", bankModule.getName());
  }else{
   chequeBooks = setupMgr.getNumberRangeChequeAll();
  }
  
  
 }

 public List<BankAccountCompanyRec> getBnkAcs() {
  return bnkAcs;
 }

 public void setBnkAcs(List<BankAccountCompanyRec> bnkAcs) {
  this.bnkAcs = bnkAcs;
 }

 public List<NumberRangeChequeRec> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeChequeRec> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 public NumberRangeChequeRec getChequeBkSel() {
  return chequeBkSel;
 }

 public void setChequeBkSel(NumberRangeChequeRec chequeBkSel) {
  this.chequeBkSel = chequeBkSel;
 }

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }
 
 public void onNewChqBkStartNumValCh(ValueChangeEvent evt){
  LOGGER.log(INFO, "onNewChqBkStartNumValCh called with {0}", evt.getNewValue());
  chequeBkSel.setNextNum((Integer)evt.getNewValue());
  
 }
 
 public List<NumberRangeChequeRec> onChqBkComplete(String input){
   if(chequeBooks == null){
   return null;
  }
  
  if(StringUtils.isBlank(input)){
   return this.chequeBooks;
  }
  List<NumberRangeChequeRec> retList = new ArrayList<>();
  for(NumberRangeChequeRec rec:chequeBooks ){
   
  }
  
  return retList;
 }
 
 public void onEditChqBkSel(SelectEvent evt){
  LOGGER.log(INFO, "onEditChqBkSel called with chq bk id {0}", ((NumberRangeRec)evt.getObject()).getNumberControlId());
  NumberRangeChequeRec selChqBk = (NumberRangeChequeRec)evt.getObject();
  chequeBkSel.setNextNumRange(selChqBk);
  
  
 }
 
 public void onEditChqBkTrf(){
  LOGGER.log(INFO, "onEditChqBkTrf called");
  chequeBkSel.setChangedBy(this.getLoggedInUser());
  chequeBkSel.setChangedDate(new Date());
  chequeBkSel = setupMgr.updateNumberRangeCheque(chequeBkSel, this.getView());
  ListIterator<NumberRangeChequeRec> li = this.chequeBooks.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   NumberRangeChequeRec curr = li.next();
   if(Objects.equals(curr.getNumberControlId(), chequeBkSel.getNumberControlId())){
    li.set(chequeBkSel);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('updtDlgWv').hide()");
  rCtx.update("chqBkUpdt:chqBkTbl");
 }
 
 public void onSaveNewChequBook(){
  chequeBkSel.setCreatedBy(getLoggedInUser());
  chequeBkSel.setCreatedDate(new Date());
  chequeBkSel.setModule(bankModule);
  LOGGER.log(INFO, "CheckBookBean save cb -  created by {0}", chequeBkSel.getCreatedBy());
  chequeBkSel = setupMgr.updateNumberRangeCheque(chequeBkSel, this.getView());
  LOGGER.log(INFO,"chequeBkSel id is now {0}",chequeBkSel.getNumberControlId());
  if(chequeBkSel.getNumberControlId() != null){
   
   MessageUtil.addInfoMessageVar1("trChqBkCr", "blacResponse", chequeBkSel.getShortDescr());
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.reset("chqBkCr:chqPnl");
   rCtx.update("chqBkCr:grwl");
   
  }else{
   MessageUtil.addErrorMessageParam1("chqBkNoSave", "errorText", chequeBkSel.getShortDescr());
   RequestContext.getCurrentInstance().update("chqBkCr:msg");
 }
 }
 
 public void validateNextChqBk(FacesContext fc, UIComponent comp, Object val){
  LOGGER.log(INFO, "validatePaymentRun called with ctx {0} comp {1} obj {2}", new Object[]{fc,comp,val});
  if (val == null){
   return ;
  }
  NumberRangeChequeRec testBk = (NumberRangeChequeRec)val;
  if(Objects.equals(testBk.getNumberControlId(), this.chequeBkSel.getNumberControlId())){
   ((EditableValueHolder)comp).setValid(false);
   
   MessageUtil.addErrorMessage("chqBkNextSelf", "errorText");
  }
  LOGGER.log(INFO, "Valid  {0}", ((EditableValueHolder)comp).isValid());
  
 }
}
