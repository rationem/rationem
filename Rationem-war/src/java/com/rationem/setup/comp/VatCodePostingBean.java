/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Chris
 */

public class VatCodePostingBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.comp.VatCodePostingBean");
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BasicSetup setup;

 private List<CompanyBasicRec> compList;
 private VatCodeRec selectedVatCode;
 private VatCodeCompanyRec selectedVatPosting;
 private VatCodeCompanyRec addVatPosting;
 private VatCodeCompanyRec vatPosting;
 private List<FiGlAccountCompRec> glAccountList;
 private List<FiGlAccountCompRec> woGlAccountList;
 private List<FiGlAccountCompRec> bsGlAccountList;
 private List<FiGlAccountCompRec> rateGlAccountList;
 private List<VatCodeRec> vatCodeList;
 private ArrayList<VatCodeCompanyRec> vatCodeCompList;
 private FiGlAccountCompRec noWoActSelect;
 private List<VatCodeCompanyRec> updateAddList;
 private List<VatCodeCompanyRec> updateChangeList;
 private List<VatCodeCompanyRec> updateDeleteList;
 private boolean vatCodeCompRowSelected;

 /**
  * Creates a new instance of VatCodePostingBean
  */
 public VatCodePostingBean() {
  logger.log(INFO, "VatCodePostingBean constructor");
 }

 


 public List<FiGlAccountCompRec> getGlAccountList() {
  if(glAccountList == null){
   glAccountList = new ArrayList<FiGlAccountCompRec>();
   
   logger.log(INFO,"addVatPosting : {0}",addVatPosting);
   
   CompanyBasicRec comp = getAddVatPosting().getCompany();
   logger.log(INFO, "Company is {0}", comp);
   if(comp == null){
    return null;
   }
   List<FiGlAccountCompRec> compAcs = sysBuff.getGlAccountsByCompanyCode(comp);
   logger.log(INFO, "vatposting bean gl accounts for company {0}", compAcs);
   boolean found = false;
   ListIterator<FiGlAccountCompRec> li = compAcs.listIterator();
   while(li.hasNext() && !found){
    FiGlAccountCompRec compAc = li.next();
    glAccountList.add(compAc);
   }
  }
  return glAccountList;
 }

 public void setGlAccountList(List<FiGlAccountCompRec> glAccountList) {
  this.glAccountList = glAccountList;
 }

 public List<FiGlAccountCompRec> getBsGlAccountList() {
  if(bsGlAccountList != null){
   return bsGlAccountList;
  }else{
   bsGlAccountList = new ArrayList<FiGlAccountCompRec>();
  }
  logger.log(INFO, "Need to build new getBsGlAccountList" );
  if(glAccountList == null){
   glAccountList = getGlAccountList();
  }
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   logger.log(INFO, "glAct coaAcnt {0}", glAct.getCoaAccount());
   logger.log(INFO, "glAct coaAcnt type {0}", glAct.getCoaAccount().getAccountType());
   logger.log(INFO, "coaAcnt Process code {0}", glAct.getCoaAccount().getAccountType().getProcessCode());
   if(glAct.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("CLVAT")){
    bsGlAccountList.add(glAct);
   }
  }
  return bsGlAccountList;
 }

 public void setBsGlAccountList(List<FiGlAccountCompRec> bsGlAccountList) {
  this.bsGlAccountList = bsGlAccountList;
 }

 public List<FiGlAccountCompRec> getRateGlAccountList() {
  if(rateGlAccountList != null){
   return rateGlAccountList;
  }else{
   rateGlAccountList = new ArrayList<FiGlAccountCompRec>();
  }
  logger.log(INFO, "Need to build new rateGlAccountList" );
  if(glAccountList == null){
   glAccountList = getGlAccountList();
  }
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   
   if(glAct.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("CLVAT")){
    rateGlAccountList.add(glAct);
   }
  }
  return rateGlAccountList;
 }

 public void setRateGlAccountList(List<FiGlAccountCompRec> rateGlAccountList) {
  this.rateGlAccountList = rateGlAccountList;
 }

 
 public List<FiGlAccountCompRec> getWoGlAccountList() {
  if(woGlAccountList != null){
   return woGlAccountList;
  }else{
   woGlAccountList = new ArrayList<FiGlAccountCompRec>();
  }
  if(glAccountList == null){
   glAccountList = getGlAccountList();
  }
  logger.log(INFO, "Need to build new pl account list");
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   logger.log(INFO, "glAct is pl {0}",glAct.getCoaAccount().isPl() );
   if(!glAct.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("CLVAT")){
    woGlAccountList.add(glAct);
   
   }
  }
  return woGlAccountList;
 }

 public void setWoGlAccountList(List<FiGlAccountCompRec> woGlAccountList) {
  this.woGlAccountList = woGlAccountList;
 }

 public FiGlAccountCompRec getNoWoActSelect() {
  if(noWoActSelect == null){
   noWoActSelect = new FiGlAccountCompRec();
   Long id = Long.valueOf("0");
   noWoActSelect.setId(id);
   String msg = "Select";
   noWoActSelect.getCoaAccount().setName(msg);
   
  }
  return noWoActSelect;
 }

 public void setNoWoActSelect(FiGlAccountCompRec noWoActSelect) {
  this.noWoActSelect = noWoActSelect;
 }
 
 

 public List<VatCodeRec> getVatCodeList() {
  if(vatCodeList == null){
   vatCodeList = setup.getVatCodes();
   if(vatPosting == null){
    vatPosting = new VatCodeCompanyRec();
   }
   selectedVatCode = vatCodeList.get(0);
   
   logger.log(INFO, "VAT codes returned from setup {0}", vatCodeList);
  }
  return vatCodeList;
 }

 public void setVatCodeList(List<VatCodeRec> vatCodeList) {
  this.vatCodeList = vatCodeList;
 }

 public ArrayList<VatCodeCompanyRec> getVatCodeCompList() {
  if(vatCodeCompList == null){
   List<CompanyBasicRec> comp = this.compList;
  }
  return vatCodeCompList;
 }

 public void setVatCodeCompList(ArrayList<VatCodeCompanyRec> vatCodeCompList) {
  this.vatCodeCompList = vatCodeCompList;
 }

 public VatCodeRec getSelectedVatCode() {
  return selectedVatCode;
 }

 public boolean isVatCodeCompRowSelected() {
  logger.log(INFO, "vatCodeCompRowSelected {0}", vatCodeCompRowSelected);
  return vatCodeCompRowSelected;
 }

 public void setVatCodeCompRowSelected(boolean vatCodeCompRowSelected) {
  this.vatCodeCompRowSelected = vatCodeCompRowSelected;
 }

 public void setSelectedVatCode(VatCodeRec selectedVatCode) {
  this.selectedVatCode = selectedVatCode;
 }

 public VatCodeCompanyRec getSelectedVatPosting() {
  if(selectedVatPosting == null){
   selectedVatPosting = new VatCodeCompanyRec();
  }
  return selectedVatPosting;
 }

 public void setSelectedVatPosting(VatCodeCompanyRec selectedVatPosting) {
  this.selectedVatPosting = selectedVatPosting;
 }

 public VatCodeCompanyRec getAddVatPosting() {
  if(addVatPosting == null){
   logger.log(INFO, "build addVatPosting");
   addVatPosting = new VatCodeCompanyRec();
   List<CompanyBasicRec> comps = this.sysBuff.getCompanies();
   logger.log(INFO, "Companies from sys buffer {0}", comps);
   CompanyBasicRec defaultComp = comps.get(0);
   addVatPosting.setCompany(defaultComp);
  }
  return addVatPosting;
 }

 public void setAddVatPosting(VatCodeCompanyRec addVatPosting) {
  this.addVatPosting = addVatPosting;
 }

 
 public VatCodeCompanyRec getVatPosting() {
  return vatPosting;
 }

 public void setVatPosting(VatCodeCompanyRec vatPosting) {
  this.vatPosting = vatPosting;
 }

 public List<VatCodeCompanyRec> getUpdateAddList() {
  return updateAddList;
 }

 public void setUpdateAddList(List<VatCodeCompanyRec> updateAddList) {
  this.updateAddList = updateAddList;
 }

 public List<VatCodeCompanyRec> getUpdateChangeList() {
  return updateChangeList;
 }

 public void setUpdateChangeList(List<VatCodeCompanyRec> updateChangeList) {
  this.updateChangeList = updateChangeList;
 }

 public List<VatCodeCompanyRec> getUpdateDeleteList() {
  return updateDeleteList;
 }

 public void setUpdateDeleteList(List<VatCodeCompanyRec> updateDeleteList) {
  this.updateDeleteList = updateDeleteList;
 }

 
 
 
 
 public void onSelectVatCode(ValueChangeEvent evt){
  logger.log(INFO, "onSelectVatCode called with {0}", evt.getNewValue());
  if(vatPosting == null){
   vatPosting = new VatCodeCompanyRec();
  }
  this.selectedVatCode = (VatCodeRec)evt.getNewValue();
  logger.log(INFO, "Selected VAT code {0}", selectedVatCode.getCode());
  logger.log(INFO, "Company VAT code rec {0}", selectedVatCode.getVatCodeCompanies());
  if(selectedVatCode.getVatCodeCompanies() == null){
   List<VatCodeCompanyRec> compVatRecs = this.setup.getVatCompCodeForVatCode(selectedVatCode);
   selectedVatCode.setVatCodeCompanies(compVatRecs);
   logger.log(INFO, "Vat codes company {0}", selectedVatCode.getVatCodeCompanies());
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("vatCodePostFrm:vatComp");
  
 }
 
 public void onVatRateChangeListener(ValueChangeEvent evt){
  FiGlAccountCompRec newGlAccount = (FiGlAccountCompRec)evt.getNewValue();
  addVatPosting.setRateGlAccount(newGlAccount);
 }
 public void onAddCompVatActionListener(){
  logger.log(INFO, "addVatPosting ");
  
 }
 
 public void onUpdateVatPosting(){
  logger.log(INFO, "onUpdateVatPosting selectedVatPosting.getId() {0}",selectedVatPosting.getId());
  if(selectedVatPosting.getId() == null){
   logger.log(INFO, "new VAT record update added list");
   ListIterator<VatCodeCompanyRec> li = updateAddList.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec rec = li.next();
    if(rec.getCompany().getId() == this.selectedVatPosting.getCompany().getId()){
     li.set(selectedVatPosting);
    }
   }
   selectedVatPosting.setChangedBy(this.getLoggedInUser());
   selectedVatPosting.setChangedOn(new Date());
   selectedVatPosting = sysBuff.vatCodeCompanyUpdate(selectedVatPosting, getView());
   ListIterator<VatCodeCompanyRec> liAdd = selectedVatCode.getVatCodeCompanies().listIterator();
   while(liAdd.hasNext()){
    VatCodeCompanyRec rec = liAdd.next();
    if(rec.getCompany().getId() == this.selectedVatPosting.getCompany().getId()){
     liAdd.set(selectedVatPosting);
    }
   }
  }else{
   selectedVatPosting.setChangedBy(this.getLoggedInUser());
   selectedVatPosting.setChangedOn(new Date());
   selectedVatPosting = sysBuff.vatCodeCompanyUpdate(selectedVatPosting, getView());
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("vatCodePostFrm:vatComp");
  rCtx.execute("PF('editDlgWv').hide()");
 }
 
 public void onDeleteVatPosting(){
  logger.log(INFO, "onDeleteVatPosting {0}",this.selectedVatPosting.getId());
  if(updateDeleteList == null){
   updateDeleteList = new ArrayList<VatCodeCompanyRec>();
  }
  if(selectedVatPosting.getId() == null || selectedVatPosting.getId() == 0){
   logger.log(INFO,"not a saved record so just remove from new list");
   ListIterator<VatCodeCompanyRec> li = updateAddList.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec rec = li.next();
    if(rec.getCompany().getId() == this.selectedVatPosting.getCompany().getId()){
     li.remove();
    }
   }
   ListIterator<VatCodeCompanyRec> liAdd = selectedVatCode.getVatCodeCompanies().listIterator();
   while(liAdd.hasNext()){
    VatCodeCompanyRec rec = liAdd.next();
    if(rec.getCompany().getId() == selectedVatPosting.getCompany().getId()){
     liAdd.remove();
    }
   }
   logger.log(INFO, "Number of entries in datatable {0} ", selectedVatCode.getVatCodeCompanies().size());
   
  }
  
 }
 public void onWoAcntCBListener(){
  logger.log(INFO, "onWoAcntCBListener state {0}", this.addVatPosting.isWoffIrrecoverable());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addWoAct");
 }
 
 public void onAddVatPostingDlg(){
  logger.log(INFO, "onAddVatPostingDlg called");
  addVatPosting = new VatCodeCompanyRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addVatPosting");
  rCtx.update("addDlg");
  rCtx.execute("PF('addDlgWv').show()");
  
 }
 
 public void onAddVatPstCloseDlg(){
 RequestContext rCtx = RequestContext.getCurrentInstance(); 
 rCtx.execute("PF('addDlgWv').hide()");
 }
 public void onAddVatPosting(){
  List<VatCodeCompanyRec> vatCodes = selectedVatCode.getVatCodeCompanies();
  
  if(vatCodes == null){
   vatCodes = new ArrayList<VatCodeCompanyRec>();
  }
  if(updateAddList == null){
   updateAddList = new ArrayList<VatCodeCompanyRec>();
  }
  addVatPosting.setVatCode(selectedVatCode);
  addVatPosting.setCreatedBy(getLoggedInUser());
  addVatPosting.setCreatedOn(new Date());
  addVatPosting = this.sysBuff.vatCodeCompanyUpdate(addVatPosting, getView());
  
  vatCodes.add(addVatPosting);
  selectedVatCode.setVatCodeCompanies(vatCodes);
  updateAddList.add(addVatPosting);
  addVatPosting = null;
  logger.log(INFO, "End onAddVatPosting num entries {0}",selectedVatCode.getVatCodeCompanies().get(0).getId());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("vatCodePostFrm:vatCompListOp");
  rCtx.execute("PF('addDlgWv').hide()");
  
 }
 
 public void onCompVatRateEditListener(){
  logger.log(INFO, "onCompVatRateEditListener rate ac selected is {0}" ,selectedVatPosting.getRateGlAccount().getCoaAccount().getRef());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.execute("PF('editDlgWv').show()");
 }
 
 public void onVatRateSelect(SelectEvent evt){
  
  this.vatCodeCompRowSelected = true;
  
 }
 
 public void validateAddComp(){
  logger.log(INFO, "validateAddComp called");
  
 }
 
 public void onSave(){
  logger.log(INFO,"onSave called");
  if(this.selectedVatCode == null){
   GenUtil.addErrorMessage("No VAT code selected");
   return;
  }
  logger.log(INFO, "Added list {0}", updateAddList);
 UserRec usr = this.getLoggedInUser();
 String view = this.getView();
 Date currDate = new Date();
 ListIterator<VatCodeCompanyRec> li = updateAddList.listIterator();
 while(li.hasNext()){
  VatCodeCompanyRec compRec = li.next();
  compRec.setCreatedOn(currDate);
  compRec.setCreatedBy(usr);
  li.set(compRec);
 }
 
 try{
  setup.addVatPostingForCompanies(selectedVatCode,updateAddList, usr, view);
  String msg = this.responseForKey("vatCodeCompAdded") +" "+ selectedVatCode.getCode();
  GenUtil.addInfoMessage(msg);
 }catch(Exception ex){
  String msg = this.errorForKey("vatCodeCompAdd") + ex.getLocalizedMessage();
  GenUtil.addErrorMessage(msg);
  
 }
 
  
 }
}
