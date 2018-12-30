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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;

/**
 * VAT Code Cross company
 * @author Chris
 */
public class VatCodeBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.common.VatCodeBean");
 @EJB
 private BasicSetup setup;
 
 @EJB
 private SysBuffer sysBuff;
 private VatCodeRec vatCode;
 private VatCodeCompanyRec vatPosting;
 private boolean vatCatDisabled = false;
 private List<VatCodeRec> vatCodes;
 

 /**
  * Creates a new instance of VatCodeBean
  */
 public VatCodeBean() {
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
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("vatCodeUpdtFrm");
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
  logger.log(INFO, "onVatCodeChange {0}", evt.getNewValue());
  vatCode = (VatCodeRec)evt.getNewValue();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("vatCodeUpdtFrm");
 }
 
 public void onVatCodeUpdateSave(){
  logger.log(INFO, "onVatCodeUpdateSave called");
  vatCode.setChangedBy(this.getLoggedInUser());
  vatCode.setChangedOn(new Date());
  try{
  vatCode = this.sysBuff.vatCodeUpdate(vatCode, this.getView());
  MessageUtil.addInfoMessage("vatCodeUpdated", "blacResponse");
  } catch(Exception ex){
   logger.log(INFO, "Update VAT code exception {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("vatCodeUpdt", "errorText");
  }
  
 }
 public void onVatRateChange(ValueChangeEvent evt){
  logger.log(INFO, "New vat rate {0}", evt.getNewValue());
  if(evt.getNewValue().getClass().getCanonicalName().equalsIgnoreCase("java.lang.Long")){
   vatCatDisabled = false;
  }else{
   vatCatDisabled = true;
   vatCode.setTaxType('V');
  }
  
 }
 public void onSaveVatCode(){
  logger.log(INFO, "Web onSaveVatCode called");
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
