/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 * VAT Code Cross company
 * @author Chris
 */
public class VatCodeBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(VatCodeBean.class.getName());
 @EJB
 private BasicSetup setup;
 
 @EJB
 private SysBuffer sysBuff;
 private VatCodeRec vatCode;
 private VatCodeCompanyRec vatPosting;
 private boolean vatCatDisabled = false;
 private List<VatCodeRec> vatCodes;
 private List<SelectItem> vatCategories;
 
 

 /**
  * Creates a new instance of VatCodeBean
  */
 public VatCodeBean() {
 }
 
 @PostConstruct
 private void init(){
 
  // Set VAT categories
  vatCategories = new ArrayList<>();
  String text = this.formTextForKey("vatVatable");
  SelectItem vatable = new SelectItem();
  vatable.setLabel(text);
  vatable.setValue("V");
  vatCategories.add(vatable);
  SelectItem exempt = new SelectItem();
  
  
 }
 

 public VatCodeRec getVatCode() {
  if(vatCode == null){
   vatCode = new VatCodeRec();
  }
  return vatCode;
 }

 public void setVatCode(VatCodeRec vatCode) {
  this.vatCode = vatCode;
 }

 public List<VatCodeRec> getVatCodes() {
  if(vatCodes == null){
   vatCodes = this.sysBuff.getVatCodes();
   if(vatCodes != null && !vatCodes.isEmpty()){
    vatCode = vatCodes.get(0);
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("vatCodeUpdtFrm");
   }
  }
  return vatCodes;
 }

 public void setVatCodes(List<VatCodeRec> vatCodes) {
  this.vatCodes = vatCodes;
 }

 
 public VatCodeCompanyRec getVatPosting() {
  
  return vatPosting;
 }

 public void setVatPosting(VatCodeCompanyRec vatPosting) {
  this.vatPosting = vatPosting;
 }

 public boolean isVatCatDisabled() {
  return vatCatDisabled;
 }

 public void setVatCatDisabled(boolean vatCatDisabled) {
  this.vatCatDisabled = vatCatDisabled;
 }
 
 
 public void onVatCodeChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onVatCodeChange {0}", evt.getNewValue());
  vatCode = (VatCodeRec)evt.getNewValue();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatCodeUpdtFrm");
 }
 
 public List<VatCodeRec> onVatCodeComplete(String input){
  
  if(StringUtils.isBlank(input)){
   return this.sysBuff.getVatCodes();
  }else{
   return sysBuff.getAllVatCodesByRef(input);
  }
  
 }
 
 public void onVatCodeSelect(SelectEvent evt){
  vatCode = (VatCodeRec)evt.getObject();
  PrimeFaces.current().ajax().update("vatCodeUpdtFrm");
 }
  
 
 public void onVatCodeUpdateSave(){
  LOGGER.log(INFO, "onVatCodeUpdateSave called");
  vatCode.setChangedBy(this.getLoggedInUser());
  vatCode.setChangedOn(new Date());
  try{
  vatCode = sysBuff.vatCodeUpdate(vatCode, this.getView());
  
  MessageUtil.addClientInfoMessage("vatCodeUpdtFrm:msg", "vatCodeUpdated", "blacResponse");
  if(vatCode.getId() == null){
   MessageUtil.addClientWarnMessage("vatCodeUpdtFrm:msg", "vatCodeUpdated", "blacResponse");
  }
  PrimeFaces.current().ajax().update("vatCodeUpdtFrm:msg");
  } catch(Exception ex){
   LOGGER.log(INFO, "Update VAT code exception {0}", ex.getLocalizedMessage());
   MessageUtil.addClientWarnMessage("vatCodeUpdtFrm:msg", "vatCodeUpdated", "blacResponse");
   PrimeFaces.current().ajax().update("vatCodeUpdtFrm:msg");
  }
  
 }
 
 public void onVatCodeValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onVatCodeValidate called with {0}", value);
  String valStr = (String)value;
  VatCodeRec vatCode = this.sysBuff.getVatCodeByCode(valStr);
  if(vatCode == null){
   ((EditableValueHolder) toValidate).setValid(true);
  }else{
   ((EditableValueHolder) toValidate).setValid(false);
  }
  PrimeFaces.current().ajax().update("vatCodeCrFrm:vatCd");
 }
 public void onVatRateChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "New vat rate {0}", evt.getNewValue());
  if(evt.getNewValue().getClass().getCanonicalName().equalsIgnoreCase("java.lang.Long")){
   vatCatDisabled = false;
  }else{
   vatCatDisabled = true;
   vatCode.setTaxType('V');
  }
  
 }
 public void onSaveVatCode(){
  LOGGER.log(INFO, "Web onSaveVatCode called");
  vatCode.setCreatedBy(getLoggedInUser());
  vatCode.setCreatedOn(new Date());
  String src = getView();
  try{
   vatCode = setup.addVatCode(vatCode, src);
   String msg = responseForKey("vatCodeAdded") + vatCode.getCode();
   String hdrMsg = responseForKey("vatCode");
   MessageUtil.addInfoMessageWithoutKey(hdrMsg, msg);
   vatCode = new VatCodeRec();
   vatCatDisabled = false;
  }catch(BacException ex){
   String msg = this.errorForKey("vatCd") + ex.getLocalizedMessage();
   String hdrMsg = errorForKey("vat");
   MessageUtil.addErrorMessageWithoutKey(hdrMsg, msg);
   GenUtil.addErrorMessage(msg);
  }catch(Exception ex){
   String msg = this.errorForKey("vatCd") + ex.getLocalizedMessage();
   String hdrMsg = errorForKey("vat");
   MessageUtil.addErrorMessageWithoutKey(hdrMsg, msg);
   
  }
  
 }
 
 
}
