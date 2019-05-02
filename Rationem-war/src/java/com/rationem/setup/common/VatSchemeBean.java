/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.util.BaseBean;
import com.rationem.busRec.salesTax.vat.VatFlatRateIndRateRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */

public class VatSchemeBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.common.VatSchemeBean");
 @EJB
 private BasicSetup setup;
 
 private VatSchemeRec vatScheme;
 private VatFlatRateIndRateRec flIndRate;
 private List<VatSchemeRec> vatSchemes;
 private List<VatFlatRateIndRateRec> flIndRates;
 

 /**
  * Creates a new instance of VatSchemeBean
  */
 public VatSchemeBean() {
 }

 public VatSchemeRec getVatScheme() {
  if(vatScheme == null){
   vatScheme = new VatSchemeRec();
  }
  return vatScheme;
 }

 public void setVatScheme(VatSchemeRec vatScheme) {
  this.vatScheme = vatScheme;
 }

 public List<VatSchemeRec> getVatSchemes() {
  if(vatSchemes == null){
   vatSchemes = setup.getVatSchemesAll();
   if(!vatSchemes.isEmpty()){
    vatScheme = vatSchemes.get(0);
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("vatSchemeUpdtFrm");
    logger.log(INFO, "update page called");
   }
  }
  
  return vatSchemes;
 }

 public void setVatSchemes(List<VatSchemeRec> vatSchemes) {
  this.vatSchemes = vatSchemes;
 }

 
 public VatFlatRateIndRateRec getFlIndRate() {
  if(flIndRate == null){
   flIndRate = new VatFlatRateIndRateRec(); 
  }
  return flIndRate;
 }

 public void setFlIndRate(VatFlatRateIndRateRec flIndRate) {
  this.flIndRate = flIndRate;
 }

 public List<VatFlatRateIndRateRec> getFlIndRates() {
  if(flIndRates == null){
   flIndRates = this.setup.getVatIndustriesAll();
   if(!flIndRates.isEmpty()){
    flIndRate = flIndRates.get(0);
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("flRatePg");
   }
  }
  return flIndRates;
 }

 public void setFlIndRates(List<VatFlatRateIndRateRec> flIndRates) {
  this.flIndRates = flIndRates;
  
 }
 
 public void onSaveIndustryRateUpdate(){
  try{
   flIndRate.setChangedBy(getLoggedInUser());
   flIndRate.setChangedOn(new Date());
   flIndRate = setup.vatIndustryRateUpdate(flIndRate, getLoggedInUser(), getView());
   
   MessageUtil.addInfoMessage("vatIndRateUpdate", "blacResponse");
   
   flIndRate = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("flRatePg");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("vatIndRateUpdt", "errorText");
  }
 }
 public void onSaveRateNew(){
  logger.log(INFO,"onSaveRateNew");
  String source = getView();
  try{
   flIndRate.setCreatedBy(getLoggedInUser());
   flIndRate.setCreatedOn(new Date());
   flIndRate = setup.vatIndustryRateUpdate(flIndRate, getLoggedInUser(), source);
   String msg = this.responseForKey("vatIndRateAdd") + flIndRate.getRef();
   String msgHdr = this.responseForKey("vatFlatRate");
   MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
   
   flIndRate = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("flRatePg");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("vatIndRateAdd", "errorText");
  }
 }
 
 public void onSaveSchemeNew(){
  logger.log(INFO, "on save called with scheme {0}",vatScheme);
  String source = this.getView();
  try{
   vatScheme.setCreatedBy(getLoggedInUser());
   vatScheme.setCreatedOn(new Date());
   VatSchemeRec vatCr = setup.addVatScheme(vatScheme, getLoggedInUser(), source);
   String msgHdr = this.responseForKey("vatScheme");
   String msg =this.responseForKey("vatSchemeCreated")+ vatCr.getRef();
   MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
   vatScheme = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("vatSchemeFrm");
   //GenUtil.addInfoMessage("Created VAT Scheme ref: "+vatCr.getRef());
  }catch(BacException ex){
   //GenUtil.addErrorMessage("BAC Error "+ex.getLocalizedMessage());
  }catch(Exception ex){
   MessageUtil.addErrorMessage("vatScheme", "errorText");
   //GenUtil.addErrorMessage("VAT scheme could not be created. Error: "+ex.getLocalizedMessage());
  }
 }
 
 public void onSaveSchemeUpdate(){
  vatScheme.setChangedBy(this.getLoggedInUser());
  vatScheme.setChangedOn(new Date());
  try{
  
  vatScheme = this.setup.vatSchemeUpdate(vatScheme, this.getLoggedInUser(), getView());
  
  MessageUtil.addInfoMessage("vatSchemeUpdated", "blacResponse");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("vatSchemeUpdt", "errorText");
  }
 }
 
 public void onVatSchemeChange(ValueChangeEvent evt){
  vatScheme = (VatSchemeRec)evt.getNewValue();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatSchemeUpdtFrm");
 }
 
 public void onVatIndustryChange(ValueChangeEvent evt){
  flIndRate = (VatFlatRateIndRateRec)evt.getNewValue();
  logger.log(INFO, "onVatIndustryChange new rate {0}", evt.getNewValue());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatFlatRateUpdtFrm");
 }
 
}
