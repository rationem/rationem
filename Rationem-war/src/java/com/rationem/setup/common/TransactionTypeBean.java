/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.ejbBean.common.SysBuffer;
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;


/**
 *
 * @author Chris
 */
public class TransactionTypeBean extends BaseBean{
 private static final Logger logger =
            Logger.getLogger(TransactionTypeBean.class.getName());
 
 private TransactionTypeRec transType;
 private TransactionTypeRec transTypeSelected;
 private List<TransactionTypeRec> transTypes;
 private List<TransactionTypeRec> transTypesFiltered;
 private List<LedgerRec> ledgers;
 
 @EJB
 private SysBuffer sysBuff;
 
 

 /**
  * Creates a new instance of TransactionTypeBean
  */
 public TransactionTypeBean() {
 }
 
 @PostConstruct
 private void init(){
  logger.log(INFO, "TransactionTypeBean init");
  transTypes = sysBuff.getTransactionTypes(this.getLoggedInUser(),this.getView());
  ledgers = sysBuff.getLedgers();
  logger.log(INFO, "Ledgers from sys buff {0}", ledgers);
  if(ledgers != null && !ledgers.isEmpty()){
   getTransType().setLedger(ledgers.get(0));
  }
 }

 public List<LedgerRec> getLedgers() {
  return ledgers;
 }

 public void setLedgers(List<LedgerRec> ledgers) {
  this.ledgers = ledgers;
 }

 
 public TransactionTypeRec getTransType() {
  if(transType == null){
   transType = new TransactionTypeRec(); 
  }
  return transType;
 }

 public void setTransType(TransactionTypeRec transType) {
  this.transType = transType;
 }

 public TransactionTypeRec getTransTypeSelected() {
  return transTypeSelected;
 }

 public void setTransTypeSelected(TransactionTypeRec transTypeSelected) {
  this.transTypeSelected = transTypeSelected;
 }

 
 public List<TransactionTypeRec> getTransTypes() {
  return transTypes;
 }

 public void setTransTypes(List<TransactionTypeRec> transTypes) {
  this.transTypes = transTypes;
 }

 public List<TransactionTypeRec> getTransTypesFiltered() {
  return transTypesFiltered;
 }

 public void setTransTypesFiltered(List<TransactionTypeRec> transTypesFiltered) {
  this.transTypesFiltered = transTypesFiltered;
 }
 
 public void onEditTransTypeDlg(){
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editFrmId");
  pf.executeScript("PF('editDlgWv').show()");
 }
 public void onSaveNewTransType(){
  logger.log(INFO, "onSaveNewTransType called trans ty {0}", transType);
  transType.setCreatedBy(this.getLoggedInUser());
  transType.setCreatedOn(new Date());
  try{
  transType = sysBuff.updateTransType(transType, getView());
  MessageUtil.addInfoMessageVar1("tranTyCr", "blacResponse", transType.getCode());
  transType = new TransactionTypeRec();
  transType.setLedger(ledgers.get(0));
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("tranTyCrFrm");
  }catch(Exception ex){
   logger.log(INFO, "Trans type create failed reason {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessageParam1("tranTyCr", "blacResponse", transType.getCode());
  }
 }
 
 public void onSaveTransfer(){
  logger.log(INFO, "onSaveTransfer called with trans ty {0}", transTypeSelected);
  transTypeSelected.setChangedBy(this.getLoggedInUser());
  transTypeSelected.setChangedOn(new Date());
  try{
  transTypeSelected = sysBuff.updateTransType(transTypeSelected, getView());
  MessageUtil.addInfoMessageVar1("tranTyUpdt", "blacResponse", transTypeSelected.getCode());
  }catch(Exception ex){
   MessageUtil.addErrorMessageParam1("tranTyUpdt", "errorText", transTypeSelected.getCode());
  }
  ListIterator<TransactionTypeRec> li = this.transTypes.listIterator();
  boolean transTyFound = false;
  while(li.hasNext() && !transTyFound){
   TransactionTypeRec transTy = li.next();
   if(transTy.getId() == transTypeSelected.getId()){
    li.set(transTypeSelected);
    transTyFound = true;
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  if(transTyFound){
   transTypeSelected = null;
   List<String> toUpdate = new ArrayList<String>();
   toUpdate.add("transTypes");
   toUpdate.add("editFrmId");
   pf.ajax().update(toUpdate);
   pf.executeScript("PF('editDlgWv').hide()");
   
  }
 }
 
 
 public void onValidateTransTypeCode(FacesContext context,UIComponent toValidate,Object value){
  logger.log(INFO, "onValidateTransTypeCode called with value {0}", value);
  String enteredCode = (String)value;
  boolean foundCode = false;
  ListIterator<TransactionTypeRec> li = this.transTypes.listIterator();
  while(li.hasNext() && !foundCode){
   TransactionTypeRec tranTy = li.next();
   if(tranTy.getCode().equals(enteredCode)){
    foundCode = true;
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  if(foundCode){
   transType.setCode(null);
   ((UIInput) toValidate).setValid(false);
   MessageUtil.addWarnMessage("transTyCdUniq", "validationText");
   pf.ajax().update("tranTyCrFrm");
   
  }else{
   ((UIInput) toValidate).setValid(true);
   List<String> updates = new ArrayList<String>();
   updates.add("code");
   updates.add("codeLbl");
   pf.ajax().update(updates);
   
  }
  
 
 }
 public void onValidateTransTypeCodeChange(FacesContext context,UIComponent toValidate,Object value){
  logger.log(INFO, "onValidateTransTypeCode called with value {0} selected value {1}", new Object[]{value,transTypeSelected.getCode()});
  String enteredCode = (String)value;
  if(enteredCode.equals(transTypeSelected.getCode())){
   ((UIInput) toValidate).setValid(true);
   return;
  }
  boolean foundCode = false;
  ListIterator<TransactionTypeRec> li = this.transTypes.listIterator();
  while(li.hasNext() && !foundCode){
   TransactionTypeRec tranTy = li.next();
   if(tranTy.getCode().equals(enteredCode)){
    foundCode = true;
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  if(foundCode){
   transType.setCode(null);
   ((UIInput) toValidate).setValid(false);
   MessageUtil.addWarnMessage("transTyCdUniq", "validationText");
   pf.ajax().update("tranTyCrFrm");
   
  }else{
   ((UIInput) toValidate).setValid(true);
   List<String> updates = new ArrayList<String>();
   updates.add("code");
   updates.add("codeLbl");
   pf.ajax().update(updates);
   
  }
  
 
 }
}
