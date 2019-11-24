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
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Chris
 */

public class VatCodePostingBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(VatCodePostingBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BasicSetup setup;
 
 @EJB
 private GlAccountManager glAcntMgr;

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
  LOGGER.log(INFO, "VatCodePostingBean constructor");
 }

@PostConstruct
private void init(){
 vatCodeList = setup.getVatCodes();
 LOGGER.log(INFO, "vatCodeList in init {0}", vatCodeList);
 if(vatCodeList == null || vatCodeList.isEmpty()){
  MessageUtil.addClientWarnMessage("vatCodePostFrm:msgs", "vatCodeNone", "errorText");
  PrimeFaces.current().ajax().update("vatCodePostFrm:msgs");
  
  LOGGER.log(INFO, "Message sent");
 }
}

public List<FiGlAccountCompRec> getGlAccountList(CompanyBasicRec comp) {
  LOGGER.log(INFO, "getGlAccountList call list at start {0}", glAccountList);
  if(glAccountList == null || glAccountList.isEmpty()){
   glAccountList = new ArrayList<>();
 
   
   LOGGER.log(INFO, "Company id {0}", comp.getId());
   if(comp == null){
    return null;
   }
   List<FiGlAccountCompRec> compAcs = sysBuff.getGlAccountsByCompanyCode(comp);
   LOGGER.log(INFO, "vatposting bean gl accounts for company {0}", compAcs);
   boolean found = false;
   ListIterator<FiGlAccountCompRec> li = compAcs.listIterator();
   while(li.hasNext() && !found){
    FiGlAccountCompRec compAc = li.next();
    glAccountList.add(compAc);
   }
  }
  return glAccountList;
}

 public List<FiGlAccountCompRec> getGlAccountList() {
  LOGGER.log(INFO, "getGlAccountList call list at start {0}", glAccountList);
  if(glAccountList == null || glAccountList.isEmpty()){
   glAccountList = new ArrayList<>();
 
   CompanyBasicRec comp = getAddVatPosting().getCompany();
   LOGGER.log(INFO, "Company id {0}", comp);
   if(comp == null){
    return null;
   }
   List<FiGlAccountCompRec> compAcs = glAcntMgr.getCompanyAccounts(comp);//sysBuff.getGlAccountsByCompanyCode(comp);
   LOGGER.log(INFO, "vatposting bean gl accounts for company {0}", compAcs);
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
   bsGlAccountList = new ArrayList<>();
  }
  LOGGER.log(INFO, "Need to build new getBsGlAccountList" );
  if(glAccountList == null){
   glAccountList = getGlAccountList();
  }
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   LOGGER.log(INFO, "glAct coaAcnt {0}", glAct.getCoaAccount().getRef());
   LOGGER.log(INFO, "glAct coaAcnt type {0}", glAct.getCoaAccount().getAccountType());
   LOGGER.log(INFO, "coaAcnt Process code  name{0}", glAct.getCoaAccount().getAccountType().getProcessCode().getName());
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
   rateGlAccountList = new ArrayList<>();
  }
  LOGGER.log(INFO, "Need to build new rateGlAccountList" );
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
  LOGGER.log(INFO,"getWoGlAccountList called ");
  if(woGlAccountList != null){
   return woGlAccountList;
  }else{
   woGlAccountList = new ArrayList<>();
  }
  if(glAccountList == null){
   glAccountList = getGlAccountList();
  }
  LOGGER.log(INFO, "Need to build new pl account list");
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   LOGGER.log(INFO, "glAct is pl {0}",glAct.getCoaAccount().isPl() );
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
   if(vatCodeList == null || vatCodeList.isEmpty()){
    
    MessageUtil.addClientWarnMessage("vatCodePostFrm:msgs", "vatCodeNone", "errorText");
    PrimeFaces.current().ajax().update("vatCodePostFrm");
    //return null;
   }else {
    selectedVatCode = vatCodeList.get(0);
   }
   
   LOGGER.log(INFO, "VAT codes returned from setup {0}", vatCodeList);
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
  LOGGER.log(INFO, "vatCodeCompRowSelected {0}", vatCodeCompRowSelected);
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
   LOGGER.log(INFO, "build addVatPosting");
   addVatPosting = new VatCodeCompanyRec();
   List<CompanyBasicRec> comps = this.sysBuff.getCompanies();
   LOGGER.log(INFO, "Companies from sys buffer {0}", comps);
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

 public void onVatCompValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO,"onVatCompValidate calledwith{0}",val);
  CompanyBasicRec comp =(CompanyBasicRec)val;
  boolean found = false;
  if(selectedVatCode.getVatCodeCompanies() != null && 
    !selectedVatCode.getVatCodeCompanies().isEmpty()){
   ListIterator<VatCodeCompanyRec> li = selectedVatCode.getVatCodeCompanies().listIterator();
   while(li)
   for(VatCodeCompanyRec vatComp:selectedVatCode.getVatCodeCompanies()){
    if(Objects.equals(vatComp.getCompany().getId(), comp.getId())){
     
    }
   }
  }
  
 /* MessageUtil.addClientWarnMessage("addCompVatRateFrm:msgs", 
       "vatCdCompAssng", "validationText",vatComp.getCompany().getReference());
     PrimeFaces.current().ajax().update("addCompVatRateFrm:msgs");
     return;
  */
 }
 public void onRateCompSelect(SelectEvent evt){
  LOGGER.log(INFO, "onRateCompSelect called with {0}", (CompanyBasicRec)evt.getObject());
  
  if(addVatPosting == null){
   addVatPosting = new VatCodeCompanyRec();
  }
  addVatPosting.setCompany((CompanyBasicRec)evt.getObject());
  PrimeFaces.current().ajax().update("addCompVatRateFrm:liabGl");
  
 }
 
 
 public void onSelectVatCode(ValueChangeEvent evt){
  LOGGER.log(INFO, "onSelectVatCode called with {0}", evt.getNewValue());
  if(vatPosting == null){
   vatPosting = new VatCodeCompanyRec();
  }
  this.selectedVatCode = (VatCodeRec)evt.getNewValue();
  LOGGER.log(INFO, "Selected VAT code {0}", selectedVatCode.getCode());
  LOGGER.log(INFO, "Company VAT code rec {0}", selectedVatCode.getVatCodeCompanies());
  if(selectedVatCode.getVatCodeCompanies() == null){
   List<VatCodeCompanyRec> compVatRecs = this.setup.getVatCompCodeForVatCode(selectedVatCode);
   selectedVatCode.setVatCodeCompanies(compVatRecs);
   LOGGER.log(INFO, "Vat codes company {0}", selectedVatCode.getVatCodeCompanies());
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatCodePostFrm:vatComp");
  
 }
 
 
 public List<VatCodeRec> onVatCodeCompComplete(String input){
  LOGGER.log(INFO, "onVatCodeCompComplete called with {0}", input);
  if(StringUtils.isBlank(input)){
   this.vatCodeList = sysBuff.getAllVatCodes();
  }else{
   this.vatCodeList = sysBuff.getAllVatCodesByRef(input);
  }
  LOGGER.log(INFO, "vatCodeList is {0}", vatCodeList);
  return vatCodeList; 
 }
 
 
 public void onVatRateChangeListener(ValueChangeEvent evt){
  FiGlAccountCompRec newGlAccount = (FiGlAccountCompRec)evt.getNewValue();
  addVatPosting.setRateGlAccount(newGlAccount);
 }
 public void onAddCompVatActionListener(){
  LOGGER.log(INFO, "addCompVatActListner ");
  
 }
 
 public void onUpdateVatPosting(){
  LOGGER.log(INFO, "onUpdateVatPosting selectedVatPosting.getId() {0}",selectedVatPosting.getId());
  if(selectedVatPosting.getId() == null){
   LOGGER.log(INFO, "new VAT record update added list");
   ListIterator<VatCodeCompanyRec> li = updateAddList.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec rec = li.next();
    if(Objects.equals(rec.getCompany().getId(), this.selectedVatPosting.getCompany().getId())){
     li.set(selectedVatPosting);
    }
   }
   selectedVatPosting.setChangedBy(this.getLoggedInUser());
   selectedVatPosting.setChangedOn(new Date());
   selectedVatPosting = sysBuff.vatCodeCompanyUpdate(selectedVatPosting, getView());
   ListIterator<VatCodeCompanyRec> liAdd = selectedVatCode.getVatCodeCompanies().listIterator();
   while(liAdd.hasNext()){
    VatCodeCompanyRec rec = liAdd.next();
    if(Objects.equals(rec.getCompany().getId(), this.selectedVatPosting.getCompany().getId())){
     liAdd.set(selectedVatPosting);
    }
   }
  }else{
   selectedVatPosting.setChangedBy(this.getLoggedInUser());
   selectedVatPosting.setChangedOn(new Date());
   selectedVatPosting = sysBuff.vatCodeCompanyUpdate(selectedVatPosting, getView());
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatCodePostFrm:vatCompTbl");
  pf.executeScript("PF('editDlgWv').hide()");
 }
 
 public void onDeleteVatPosting(){
  LOGGER.log(INFO, "onDeleteVatPosting {0}",this.selectedVatPosting.getId());
  if(updateDeleteList == null){
   updateDeleteList = new ArrayList<>();
  }
  if(selectedVatPosting.getId() == null || selectedVatPosting.getId() == 0){
   LOGGER.log(INFO,"not a saved record so just remove from new list");
   ListIterator<VatCodeCompanyRec> li = updateAddList.listIterator();
   while(li.hasNext()){
    VatCodeCompanyRec rec = li.next();
    if(Objects.equals(rec.getId(), this.selectedVatPosting.getCompany().getId())){
     li.remove();
    }
   }
/*   ListIterator<VatCodeCompanyRec> liAdd = selectedVatCode.getVatCodeCompanies().listIterator();
   while(liAdd.hasNext()){
    VatCodeCompanyRec rec = liAdd.next();
    if(Objects.equals(rec.getCompany().getId(), selectedVatPosting.getCompany().getId())){
     liAdd.remove();
    }
   }
   */
   
   
  }else{
   LOGGER.log(INFO,"onDeleteVatPosting else  selectedVatPosting {0}",selectedVatPosting);
   ListIterator<VatCodeCompanyRec> li = selectedVatCode.getVatCodeCompanies().listIterator();
   boolean found = false;
   PrimeFaces pf = PrimeFaces.current();
   while(li.hasNext() && !found){
    VatCodeCompanyRec curr = li.next();
    if(Objects.equals(curr.getId(), selectedVatPosting.getId())){
     
     boolean okay = setup.vatCodeCompanyDel(selectedVatPosting, this.getView(), this.getLoggedInUser());
     if(okay){
     li.remove();
     pf.ajax().update("vatCodePostFrm:vatCompTbl");
     }else{
      MessageUtil.addClientWarnMessage("vatCodePostFrm:msgs", "vatCdCompDel", "validationText");
      pf.ajax().update("vatCodePostFrm:msgs");
     }
     found = okay;
    }
   }
   LOGGER.log(INFO, "found: {0}", found);
   
   
   pf.executeScript("PF('delConfVar').hide();");
   
  }
  LOGGER.log(INFO, "Number of entries in datatable {0} ", selectedVatCode.getVatCodeCompanies().size());
 }
 
 public void onDeleteVatPostingDlg(){
  LOGGER.log(INFO, "onDeleteVatPostingDlg called -selected comp rec {0}", selectedVatPosting);
  
  
  boolean status = setup.vatCodeCompanyCanDel(selectedVatPosting, this.getView());
  LOGGER.log(INFO, "status after attempt to delete {0}", status);
  
  if(status){
   PrimeFaces.current().executeScript("PF('delConfVar').show();");
  }else{
   MessageUtil.addClientWarnMessage("vatCodePostFrm:msgs", "vatCdCompDel", "validationText");
   PrimeFaces.current().ajax().update("vatCodePostFrm:msgs");
   
  }
  
  
 }
 
 public List<FiGlAccountCompRec> onEditVatLiabGlComplete(String input){
  LOGGER.log(INFO, "onVatLiabGlComplete called bsGL {0} input {1}", new Object[]{
   bsGlAccountList, input });
  if(bsGlAccountList != null && !bsGlAccountList.isEmpty() ){
   return bsGlAccountList;
  }else{
   bsGlAccountList = new ArrayList<>();
  }
  LOGGER.log(INFO, "Need to build new getBsGlAccountList" );
  if(glAccountList == null || glAccountList.isEmpty() ){
   glAccountList = this.getGlAccountList(selectedVatPosting.getCompany());
  }
  LOGGER.log(INFO, "glAccountList {0}", glAccountList);
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   if(glAct.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("GL_ACNT_VAT_CL")){
    bsGlAccountList.add(glAct);
   }
  }
  if(bsGlAccountList == null || bsGlAccountList.isEmpty()){
   return null;
  }
  if(StringUtils.isBlank(input)){
   LOGGER.log(INFO, "bsGlAccountList no input{0}", bsGlAccountList);
   return bsGlAccountList; 
  }else{
   LOGGER.log(INFO, "bsGlAccountList check input{0}", bsGlAccountList);
   List<FiGlAccountCompRec> retList = new ArrayList<>();
   for(FiGlAccountCompRec curr: bsGlAccountList){
    if(StringUtils.startsWith(curr.getCoaAccount().getRef(), input)){
     retList.add(curr);
    }
   }
   LOGGER.log(INFO, "retlist {0}", retList);
   return retList;
  }
  
 }
 
 public void onEditVatLiabGlSelect(SelectEvent evt){
  LOGGER.log(INFO, "onEditVatLiabGlSelect called with {0} ", evt.getObject());
  
  PrimeFaces.current().ajax().update("editVatPostingFrm:saveBtn");
  
 }
 public List<FiGlAccountCompRec> onVatLiabGlComplete(String input){
  LOGGER.log(INFO, "onVatLiabGlComplete called bsGL {0} input {1}", new Object[]{
   bsGlAccountList, input });
  LOGGER.log(INFO, "base object selectedVatPosting {0} addVatPosting {1} ", 
    new Object[]{addVatPosting,addVatPosting});
  LOGGER.log(INFO, "selectedVatPosting {0} addVatPosting {1} ", 
    new Object[]{addVatPosting.getCompany().getId(),addVatPosting.getCompany().getId()});
  if(bsGlAccountList != null && !bsGlAccountList.isEmpty() ){
   return bsGlAccountList;
  }else{
   bsGlAccountList = new ArrayList<>();
  }
  LOGGER.log(INFO, "Need to build new getBsGlAccountList" );
  if(glAccountList == null || glAccountList.isEmpty() ){
   
   glAccountList = getGlAccountList();
  }
  ListIterator<FiGlAccountCompRec> li = glAccountList.listIterator();
  while(li.hasNext()){
   FiGlAccountCompRec glAct = li.next();
   if(glAct.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("GL_ACNT_VAT_CL")){
    bsGlAccountList.add(glAct);
   }
  }
  if(bsGlAccountList == null || bsGlAccountList.isEmpty()){
   return null;
  }
  if(StringUtils.isBlank(input)){
   LOGGER.log(INFO, "bsGlAccountList no input{0}", bsGlAccountList);
   return bsGlAccountList; 
  }else{
   LOGGER.log(INFO, "bsGlAccountList check input{0}", bsGlAccountList);
   List<FiGlAccountCompRec> retList = new ArrayList<>();
   for(FiGlAccountCompRec curr: bsGlAccountList){
    if(StringUtils.startsWith(curr.getCoaAccount().getRef(), input)){
     retList.add(curr);
    }
   }
   LOGGER.log(INFO, "retlist {0}", retList);
  }
  LOGGER.log(INFO, "Nothing to return");
  return null;
 }
 public void onWoAcntCBListener(){
  
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addCompVatRateFrm:woGl");
 }
 
 public void onWoAcntCBEdit(){
  LOGGER.log(INFO, "WoffIrrecoverable {0}", selectedVatPosting.isWoffIrrecoverable());
   
  
  List<String> updates = new ArrayList<>();
  //updates.add("editVatPostingFrm:noIrrLine")
  updates.add("editVatPostingFrm:editWoAct");
  PrimeFaces.current().ajax().update(updates);
  
 }
 
 public void onVatIrrRateValidate(FacesContext context, UIComponent toValidate, Object val){
  LOGGER.log(INFO, "onVatIrrRateValidate called with val {0}", val);
  
  boolean ok = false;
  if(val == null ){
   ok = true;
  }else{
   LOGGER.log(INFO, "onVatIrrRateValidate called with val class {0}", val.getClass());
   if(StringUtils.equals(val.getClass().getSimpleName(), "Double")){
    Double valD = (Double)val;
    if(this.selectedVatCode.getRate() >= valD){
     ok = true;
    }
   }else if(StringUtils.equals(val.getClass().getSimpleName(), "Integer")){
    Double valD = ((Integer)val).doubleValue();
    if(this.selectedVatCode.getRate() >= valD){
     ok = true;
    }
   }else if(StringUtils.equals(val.getClass().getSimpleName(), "Long")){
    Double valD = ((Long)val).doubleValue();
    if(this.selectedVatCode.getRate() >= valD){
     ok = true;
    }
   } 
   
  }
  LOGGER.log(INFO, "OK status {0}", ok);
  ((EditableValueHolder) toValidate).setValid(ok);
  List<String> updates = new ArrayList<>();
  updates.add("editVatPostingFrm:irrRate");
  if(!ok){
   MessageUtil.addClientWarnMessage("editVatPostingFrm:msgs", "vatIndIrrRate", "validationText");
   
  }
  updates.add("editVatPostingFrm:msgs");
  PrimeFaces.current().ajax().update(updates);
 }
 public void onVatIrrRateChangeEd(ValueChangeEvent evt){
  LOGGER.log(INFO, "onVatIrrRateChangeEd called with {0} class {1}", 
    new Object[]{evt.getNewValue()});
  boolean irrecov = false;
  List<String> updates = new ArrayList<>();
  updates.add("editVatPostingFrm:editWoIrr");
  updates.add("editVatPostingFrm:editWoAct");
  PrimeFaces.current().ajax().update(updates);
  
  
 }
 public List<FiGlAccountCompRec> onVatWoAcntEdComplete(String input){
  LOGGER.log(INFO, "onVatWoLineComplete called with {0}", input);
  List<FiGlAccountCompRec> retList = new ArrayList<>();
  List<FiGlAccountCompRec> compActs = sysBuff.getGlAccountsByCompanyCode(selectedVatPosting.getCompany());
  if(StringUtils.isBlank(input)){
   // all Pl accounts
   for(FiGlAccountCompRec curr:compActs){
    if(curr.getCoaAccount().isPl()){
     retList.add(curr);
    }
   }
  }else{
   for(FiGlAccountCompRec curr:compActs){
    if(curr.getCoaAccount().isPl() && StringUtils.startsWith(curr.getCoaAccount().getRef(), input)){
     retList.add(curr);     
    }
   }
  }
  Collections.sort(retList, new FiGlAccountCompByRef());
  return retList; 
 }
 public void onAddVatPostingDlg(){
  LOGGER.log(INFO, "onAddVatPostingDlg called");
  
  addVatPosting = new VatCodeCompanyRec();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addCompVatRateFrm");
  
  pf.executeScript("PF('addDlgWv').show()");
  
 }
 
 public void onAddVatPstCloseDlg(){
 PrimeFaces pf = PrimeFaces.current(); 
 pf.executeScript("PF('addDlgWv').hide()");
 }
 public void onAddVatPosting(){
  LOGGER.log(INFO, "onAddVatPosting caled code {0} comp {1}", new Object[]{
   addVatPosting, addVatPosting.getCompany()});
  List<VatCodeCompanyRec> vatCodes = selectedVatCode.getVatCodeCompanies();
  
  if(vatCodes == null){
   vatCodes = new ArrayList<>();
  }
  if(updateAddList == null){
   updateAddList = new ArrayList<>();
  }
  addVatPosting.setVatCode(selectedVatCode);
  addVatPosting.setCreatedBy(getLoggedInUser());
  addVatPosting.setCreatedOn(new Date());
  addVatPosting = sysBuff.vatCodeCompanyUpdate(addVatPosting, getView());
  
  vatCodes.add(addVatPosting);
  selectedVatCode.setVatCodeCompanies(vatCodes);
  updateAddList.add(addVatPosting);
  addVatPosting = null;
  LOGGER.log(INFO, "End onAddVatPosting num entries {0}",selectedVatCode.getVatCodeCompanies().get(0).getId());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("vatCodePostFrm:vatCompTbl");
  pf.executeScript("PF('addDlgWv').hide()");
  
 }
 
 
 public void onCompVatRateEditListener(){
  LOGGER.log(INFO, "onCompVatRateEditListener rate ac selected is {0}" ,selectedVatPosting.getVatCode().getRate());
  List<String> updates = new ArrayList<>();
  updates.add("editVatPostingFrm:addDlgHdr");
  updates.add("editVatPostingFrm:hdrRate");
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update(updates);
  pf.executeScript("PF('editDlgWv').show()");
 }
 
 public void onVatCodeSelect(SelectEvent evt){
  LOGGER.log(INFO, "onVatCodeSelect called with {0}", evt.getObject());
  selectedVatCode = (VatCodeRec)evt.getObject();
  LOGGER.log(INFO, "Company VAT codes {0}", selectedVatCode.getVatCodeCompanies());
  LOGGER.log(INFO, "Input tax {0}", selectedVatCode.isInputTax());
  if(selectedVatCode.getVatCodeCompanies() == null){
   List<VatCodeCompanyRec> compCodes = setup.getVatCompCodeForVatCode(selectedVatCode);
   LOGGER.log(INFO, "Company VAT codes from setup {0}", compCodes);
   selectedVatCode.setVatCodeCompanies(compCodes);
   for(VatCodeCompanyRec curr:compCodes){
    LOGGER.log(INFO, "VatPOsting bean compVAT rate gl account {0} write-off rate {1} "
      + "write-off {2} ", new Object[]{
     curr.getRateGlAccount(),curr.getIrrecoverRate(),
     curr.isWoffIrrecoverable() });
   }
  }
  List<String> updates = new ArrayList<>();
  updates.add("vatCodePostFrm:vatDirection");
  updates.add("vatCodePostFrm:vatRate"); 
  updates.add("vatCodePostFrm:vatCompTbl");
  PrimeFaces.current().ajax().update(updates);
 }
 public void onVatRateSelect(SelectEvent evt){
  
  this.vatCodeCompRowSelected = true;
  
 }
 
 
 
 public void onSave(){
  LOGGER.log(INFO,"onSave called");
  if(this.selectedVatCode == null){
   GenUtil.addErrorMessage("No VAT code selected");
   return;
  }
  LOGGER.log(INFO, "Added list {0}", updateAddList);
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
