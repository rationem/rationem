/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.util.BaseBean;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.salesTax.vat.VatFlatRateIndRateRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.config.common.VatManager;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;


/**
 *
 * @author Chris
 */
public class VatRegAssignBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.comp.vatRegAssignBean");

 @EJB
 private BasicSetup setup;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private VatManager vatMgr;

 private CompanyBasicRec comp;
 private VatRegistrationRec vatReg;
 private VatRegSchemeRec vatRegScheme;
 private List<CompanyBasicRec> comps;
 private List<VatSchemeRec> availSchemes;
 private List<VatSchemeRec> selectedSchemes;
 private List<VatRegistrationRec> vatRegistrations;
 private DualListModel<VatSchemeRec> vatSchemes;
 private List<VatFlatRateIndRateRec> vatInds;
 private VatFlatRateIndRateRec selectVatInd;
 private VatRegSchemeRec vatRegSchemeSelected;
 private List<VatSchemeRec> vatSchemeList;
 
 /**
  * Creates a new instance of VatRegAssignBean
  */
 public VatRegAssignBean() {
  logger.log(INFO,"vatRegAssignBean constructor called");
  
 }

 @PostConstruct
 private void init(){
  logger.log(INFO, "VatRegAssign bean");
  comps = this.sysBuff.getCompanies();
  setCompList(sysBuff.getCompanies());
  logger.log(INFO, "Companies {0}", getCompList());
  comp = getCompList().get(0);
  if(comp.getVatRegDetails() == null || comp.getVatRegDetails().getId() == null ){
   comp = this.sysBuff.getCompCurrentVatRec(comp);
  }
  logger.log(INFO, "comp vat reg {0}", comp.getVatRegDetails().getId());
  vatRegistrations = comp.getVatRegistrations();
  if(vatRegistrations == null){
   vatRegistrations = vatMgr.getVatRegistrationsForComp(comp);
  }
  logger.log(INFO, "vatRegistrations {0}", vatRegistrations);
  
 }
 public List<VatSchemeRec> getAvailSchemes() {
  if(availSchemes == null){
   logger.log(INFO, "getAvailSchemes");
   availSchemes = setup.getVatSchemesAll();
   logger.log(INFO, "availSchemes after setup call {0}");
  }
  return availSchemes;
 }

 public void setAvailSchemes(List<VatSchemeRec> availSchemes) {
  this.availSchemes = availSchemes;
 }

 

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public List<CompanyBasicRec> getComps() {
  return comps;
 }

 public void setComps(List<CompanyBasicRec> comps) {
  this.comps = comps;
 }

 
 public List<VatSchemeRec> getSelectedSchemes() {
  if(selectedSchemes == null){
   selectedSchemes = new ArrayList<VatSchemeRec>();
  }
  return selectedSchemes;
 }

 public void setSelectedSchemes(List<VatSchemeRec> selectedSchemes) {
  this.selectedSchemes = selectedSchemes;
 }

 

 public VatRegistrationRec getVatReg() {
  return vatReg;
 }

 public void setVatReg(VatRegistrationRec vatReg) {
  this.vatReg = vatReg;
 }

 public VatRegSchemeRec getVatRegScheme() {
  if(vatRegScheme == null){
   vatRegScheme = new VatRegSchemeRec();
   if(this.vatRegistrations == null){
    getVatRegistrations();
   }
   vatRegScheme.setValidFrom(this.vatReg.getRegistrationDate());
  }
  return vatRegScheme;
 }
 

 public void setVatRegScheme(VatRegSchemeRec vatRegScheme) {
  this.vatRegScheme = vatRegScheme;
 }

 public VatRegSchemeRec getVatRegSchemeSelected() {
  return vatRegSchemeSelected;
 
 }

 public void setVatRegSchemeSelected(VatRegSchemeRec vatRegSchemeSelected) {
  this.vatRegSchemeSelected = vatRegSchemeSelected;
 }

 

 public List<VatRegistrationRec> getVatRegistrations() {
  if(vatRegistrations == null){
   if(comp == null){
    comp = this.getComp();
   }
   logger.log(INFO, "Comp is {0}", comp);
   vatRegistrations = comp.getVatRegistrations();
   if(vatRegistrations != null){
    vatReg = vatRegistrations.get(0);
    if(vatRegScheme == null){
     vatRegScheme = new VatRegSchemeRec();
    }
    vatRegScheme.setVatReg(vatReg);
    vatRegScheme.setValidFrom(vatReg.getRegistrationDate());
    vatRegScheme.setValidTo(vatReg.getRegistrationEnd());
   }
   logger.log(INFO, "vatRegistrations {0}", vatRegistrations);
  }
  return vatRegistrations;
 }

 public void setVatRegistrations(List<VatRegistrationRec> vatRegistrations) {
  this.vatRegistrations = vatRegistrations;
 }

 public VatFlatRateIndRateRec getSelectVatInd() {
  
  return selectVatInd;
 }

 public void setSelectVatInd(VatFlatRateIndRateRec selectVatInd) {
  this.selectVatInd = selectVatInd;
 }

 public List<VatFlatRateIndRateRec> getVatInds() {
  if(vatInds == null){
   vatInds = setup.getVatIndustriesAll();
   VatFlatRateIndRateRec option = new VatFlatRateIndRateRec();
   option.setRef("select one");
   option.setId(Long.valueOf("0"));
   vatInds.add(0, option);
  }
  return vatInds;
 }

 public void setVatInds(List<VatFlatRateIndRateRec> vatInds) {
  this.vatInds = vatInds;
 }

 public List<VatSchemeRec> getVatSchemeList() {
  if(vatSchemeList == null){
   vatSchemeList = vatMgr.getVatSchemesAll();
  }
  return vatSchemeList;
 }

 public void setVatSchemeList(List<VatSchemeRec> vatSchemeList) {
  this.vatSchemeList = vatSchemeList;
 }

 public DualListModel<VatSchemeRec> getVatSchemes() {
  if(vatSchemes == null){
   logger.log(INFO, "availSchemes {0} selectedSchemes {1}", new Object[]{getAvailSchemes(), getSelectedSchemes()});
   vatSchemes = new DualListModel<VatSchemeRec>(getAvailSchemes(), getSelectedSchemes());
   logger.log(INFO, "vatSchemes {0}", vatSchemes);
  }
  return vatSchemes;
 }

 public void setVatSchemes(DualListModel<VatSchemeRec> vatSchemes) {
  this.vatSchemes = vatSchemes;
 }

 public void onRegSchRowSelect(SelectEvent evt){
  this.vatRegSchemeSelected =(VatRegSchemeRec)evt.getObject();
  logger.log(INFO, "Select reg sch {0}", vatRegSchemeSelected);
  
 }
 public void onRegSchEdit(){
  logger.log(INFO, "onRegSchEdit called. Selected reg scheme is {0}", this.vatRegSchemeSelected);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editSchFrm");
  rCtx.execute("PF('editRegSchWv').show()");
 }
 
 public void onRegSchEditClose(){
  vatRegSchemeSelected = null;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('editRegSchWv').hide()");
 }
 
 public void onRegSchEditSave(){
  logger.log(INFO, "onRegSchEditSave");
  try{
  ListIterator<VatRegSchemeRec> li = comp.getVatRegDetails().getRegSchemes().listIterator();
  while(li.hasNext()){
   VatRegSchemeRec currRegSch = li.next();
   currRegSch.setChangedOn(new Date());
   currRegSch.setChangedBy(this.getLoggedInUser());
   li.set(currRegSch);
  }
  vatReg = this.vatMgr.vatRegSchemeUpdate(comp.getVatRegDetails(), this.getLoggedInUser(), getView());
  MessageUtil.addInfoMessage("vatRegSchemeUpdt", "blacResponse");
  }catch (Exception ex){
   logger.log(INFO, "save exception {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("vatRegSchUpdt", "errorText");
  }
 

  
 }
 public void onRegSchEditTransfer(){
  logger.log(INFO, "onRegSchEditTransfer {0}", vatRegSchemeSelected);
  ListIterator<VatRegSchemeRec> li = comp.getVatRegDetails().getRegSchemes().listIterator();
  boolean foundRegSch = false;
  while(li.hasNext() && !foundRegSch){
   VatRegSchemeRec sch = li.next();
   if(sch.getId() == vatRegSchemeSelected.getId()){
    li.set(sch);
    foundRegSch = true;
   }
  }
  vatRegSchemeSelected = null;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("vatschemetblId");
  rCtx.execute("PF('editRegSchWv').hide()");
 }
 /*public void onSaveSchemeAssignment(){
  logger.log(INFO, "called onsaveSchemeAssignment vat schemes {0}",vatSchemes);
  logger.log(INFO,"vatRegScheme {0} vat reg", vatRegScheme );
  Date currDt = new Date();
  UserRec currUsr = getLoggedInUser();
  String currView = getView();
  if(vatRegScheme.getValidFrom().compareTo(currDt) <= 0 && vatRegScheme.getValidTo().compareTo(currDt)>= 0 ){
   //value from before today and valid 2 after today
   vatRegScheme.setActive(true);
  }else{
   vatRegScheme.setActive(false);
  }
  if(vatSchemes.getTarget() == null || vatSchemes.getTarget().isEmpty() ){
   GenUtil.addErrorMessage("You must select a VAT scheme");
   return;
  }else{
   vatRegScheme.setVatSchemes(vatSchemes.getTarget());
   logger.log(INFO, "Set vat reg schemes to {0}", vatRegScheme.getVatSchemes());
  }
  // do we have a flat rate scheme
  boolean found = false;
  ListIterator<VatSchemeRec> li = vatRegScheme.getVatSchemes().listIterator(); 
  while(li.hasNext() && !found){
   VatSchemeRec scheme = li.next();
   if(scheme.getCreatedBy() == null){
    
    scheme.setCreatedBy(currUsr);
    scheme.setCreatedOn(currDt);
    li.set(scheme);
   }
   if(scheme.isFlatRate()){
    // this is a flat rate scheme need to have and inductry
    if(selectVatInd == null || selectVatInd.getId() == 0){
     GenUtil.addErrorMessage("You must specify an industry for the flat rate scheme");
     return;
    }
   }
  }
  if(vatRegScheme.getCreatedBy() == null){
   vatRegScheme.setCreatedBy(currUsr);
   vatRegScheme.setCreatedOn(currDt);
  }
  try{
   vatRegScheme = this.setup.VatRegAddScheme(vatRegScheme, currUsr, currView);
   GenUtil.addInfoMessage("Saved  VAT reg scheme "+vatRegScheme.getRef());
  }catch(BacException ex){
   GenUtil.addErrorMessage("Save VAT reg sch failed BACS. Reason: "+ex.getLocalizedMessage());
  }catch(Exception ex){
   GenUtil.addErrorMessage("Save VAT reg sch failed OTHER Excep. Reason: "+ex.getLocalizedMessage());
  }
  
  
 }*/

 
 
 
}
