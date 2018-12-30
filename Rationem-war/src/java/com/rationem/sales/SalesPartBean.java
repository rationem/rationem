/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.sales;
import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
//import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
//import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.busRec.sales.SalesCatRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.SalesManager;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.UUID;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;


/**
 *
 * @author Chris
 */
public class SalesPartBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(SalesPartBean.class.getName());
 
 private SalesPartRec part;
 private SalesPartRec selectedPart;
 private SalesPartCompanyRec selectedPartCompany;
 private SalesPartCompanyRec addPartCompany;
 private boolean partCompanyRowSelected = false;
 private SalesPartCompanyRec partCompany;
 private List<FiGlAccountCompRec> salesAccounts;  //pl accounts for company
 private List<FiGlAccountCompRec> editSalesAccounts;
 private List<FiGlAccountCompRec> editSalesAccountsAdd;
 private List<FiGlAccountCompRec> cosAccounts;    //pl accounts for company
 private List<FiGlAccountCompRec> cosEditAccounts; 
 private List<FiGlAccountCompRec> stockAccounts;   //bs accounts for company Transfer from stock account
 private List<FiGlAccountCompRec> stockEditAccounts;
 private SelectItem defaultFund;
 private List<FundRec> funds;
 private List<UomRec> uoms;
 private CompanyBasicRec company;
 private List<SalesPartCompanyRec> partCompanyLines;
 private List<SalesPartCompanyRec> partCompanyUpdated;
 private List<SalesCatRec> salesCatgories;
 private List<SalesPartRec> salesParts;
 private List<SalesPartRec> salesPartUpdates;
 private List<SalesPartRec> filteredSalesParts;
 private String partCodeSearchCriteria;
 
 
 @EJB
 private SysBuffer buffer;
 
 @EJB 
 private SalesManager salesMgr;

 public SalesPartBean() {
 }
 

 

 public SalesPartRec getPart() {
  if(part == null){
   part = new SalesPartRec();
  }
  return part;
 }

 public void setPart(SalesPartRec part) {
  this.part = part;
 }

 public SalesPartCompanyRec getPartCompany() {
  if(partCompany == null) {
   partCompany = new SalesPartCompanyRec();
  }
  return partCompany;
 }

 public void setPartCompany(SalesPartCompanyRec partCompany) {
  this.partCompany = partCompany;
 }

 public List<SalesPartCompanyRec> getPartCompanyLines() {
  return partCompanyLines;
 }

 public void setPartCompanyLines(List<SalesPartCompanyRec> partCompanyLines) {
  this.partCompanyLines = partCompanyLines;
 }

 public List<SalesPartCompanyRec> getPartCompanyUpdated() {
  return partCompanyUpdated;
 }

 public void setPartCompanyUpdated(List<SalesPartCompanyRec> partCompanyUpdated) {
  this.partCompanyUpdated = partCompanyUpdated;
 }

 public boolean isPartCompanyRowSelected() {
  return partCompanyRowSelected;
 }

 public void setPartCompanyRowSelected(boolean partCompanyRowSelected) {
  this.partCompanyRowSelected = partCompanyRowSelected;
 }

 
 public String getPartCodeSearchCriteria() {
  return partCodeSearchCriteria;
 }

 public void setPartCodeSearchCriteria(String partCodeSearchCriteria) {
  this.partCodeSearchCriteria = partCodeSearchCriteria;
 }

 public SalesPartCompanyRec getAddPartCompany() {
  if(addPartCompany == null){
   addPartCompany = new SalesPartCompanyRec();
  }
  return addPartCompany;
 }

 public void setAddPartCompany(SalesPartCompanyRec addPartCompany) {
  this.addPartCompany = addPartCompany;
 }

 
 

 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 public List<UomRec> getUoms() {
  if(uoms == null){
   List<UomRec> fullList = buffer.getUoms();
   uoms = new ArrayList<>();
   if(fullList == null){
    return null;
   }
   for(UomRec curr:fullList){
    if(curr.getProcessCode().equalsIgnoreCase("QTY")){
     uoms.add(curr);
    }
   }
   
  }
  return uoms;
  
 }

 public void setUoms(List<UomRec> uoms) {
  this.uoms = uoms;
 }

 public List<FiGlAccountCompRec> getCosAccounts() {
  if(cosAccounts == null){
   cosAccounts = new ArrayList<>();
   CompanyBasicRec comp= partCompany.getCompany();
   
   LOGGER.log(INFO, "getcosAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    
    partCompany.setCompany(getCompList().get(0));
    comp = partCompany.getCompany();
   }
   LOGGER.log(INFO, "getCosAccounts comp 2 is {0}", comp);
   List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
   for(FiGlAccountCompRec curr: compAccounts){
    if(curr.getCoaAccount().getAccountType().getProcessCode().getName().equals("GL_ACNT_DC")){
     cosAccounts.add(curr);
    }
     
   }
   
   
  }
  return cosAccounts;
  
 }

 public void setCosAccounts(List<FiGlAccountCompRec> cosAccounts) {
  this.cosAccounts = cosAccounts;
 }
/**
 * Accounts to transfer cost from stock at cost price
 * @return 
 */
 public List<FiGlAccountCompRec> getStockAccounts() {
  LOGGER.log(INFO, "getCostAccounts called acs {0}", stockAccounts);
  if(stockAccounts == null){
   // need to get the GL accounts from sys buffer
   stockAccounts = new ArrayList<>();
   CompanyBasicRec comp= partCompany.getCompany();
   
   LOGGER.log(INFO, "getCostAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    
    partCompany.setCompany(getCompList().get(0));
    comp = partCompany.getCompany();
   }
    List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
    
    if(compAccounts == null){
     return null;
    }
    for(FiGlAccountCompRec curr:compAccounts){
     if(curr.getCoaAccount().getAccountType().getProcessCode().getName().equals("GL_ACNT_INV")){
      stockAccounts.add(curr);
     }
    }
    
    
  }
  return stockAccounts;
 }

 public void setCostAccounts(List<FiGlAccountCompRec> stockAccounts) {
  this.stockAccounts = stockAccounts;
 }

 public List<FiGlAccountCompRec> getEditCosAccounts() {
  if(selectedPartCompany == null){
    return null;
   }
  if(cosEditAccounts == null){
   cosEditAccounts = new ArrayList<>();
   CompanyBasicRec comp= selectedPartCompany.getCompany();
   
   LOGGER.log(INFO, "getcosAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    
    selectedPartCompany.setCompany(getCompList().get(0));
    comp = selectedPartCompany.getCompany();
   }
   LOGGER.log(INFO, "getCosAccounts comp 2 is {0}", comp);
   List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
   ListIterator<FiGlAccountCompRec> liCompAcs = compAccounts.listIterator();
   while(liCompAcs.hasNext()){
    FiGlAccountCompRec compAc = liCompAcs.next();
    if(compAc.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("DC")){
     cosEditAccounts.add(compAc);
    }
   }
   
  }
  return cosEditAccounts;
  
 }

 public void setCosEditAccounts(List<FiGlAccountCompRec> cosEditAccounts) {
  this.cosEditAccounts = cosEditAccounts;
 }
 
 public List<FiGlAccountCompRec> getEditSalesAccountsAdd() {
  LOGGER.log(INFO, "getAddCompPartSalesAccounts called editSalesAccounts {0} selectedPartCompany {1} "
          + "selectedPart {2} ", new Object[]{
   editSalesAccounts, selectedPartCompany, selectedPart
  });
  if(editSalesAccountsAdd != null ){
   return editSalesAccountsAdd;
  }else{
   editSalesAccountsAdd = new ArrayList<>();
  }
  
  LOGGER.log(INFO, "addPartCompany.company {0}", addPartCompany.getCompany());
  
  CompanyBasicRec comp;
  if(addPartCompany.getCompany() == null){
   comp = getCompList().get(0);
  } else {
   comp = addPartCompany.getCompany();
  }
  LOGGER.log(INFO, "comp is {0}", comp);
  List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
   ListIterator<FiGlAccountCompRec> liCompAcs = compAccounts.listIterator();
   while(liCompAcs.hasNext()){
    FiGlAccountCompRec compAc = liCompAcs.next();
    if(compAc.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("SL")){
     editSalesAccountsAdd.add(compAc);
    }
   }
  
  return editSalesAccountsAdd;
 }
 
 public List<FiGlAccountCompRec> getEditSalesAccounts() {
  LOGGER.log(INFO, "getEditSalesAccounts called editSalesAccounts {0} selectedPartCompany {1} selectedPart {2}", new Object[]{
   editSalesAccounts, selectedPartCompany, selectedPart
  });
  if(editSalesAccounts == null || editSalesAccounts.isEmpty() ){
   editSalesAccounts = new ArrayList<>();
   LOGGER.log(INFO, "Get editsalesAc selectedPartCompany {0}", selectedPartCompany);
   if(selectedPartCompany == null){
    return new ArrayList<>();
   }
   CompanyBasicRec comp;
   if(selectedPartCompany == null){
    comp = company;
   }else{
    comp= selectedPartCompany.getCompany();
   }
   
   LOGGER.log(INFO, "getSalesAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    
    selectedPartCompany.setCompany(getCompList().get(0));
    comp = selectedPartCompany.getCompany();
   }
   LOGGER.log(INFO, "getSalesAccounts comp 2 is {0}", comp);
   List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
   ListIterator<FiGlAccountCompRec> liCompAcs = compAccounts.listIterator();
   while(liCompAcs.hasNext()){
    FiGlAccountCompRec compAc = liCompAcs.next();
    if(compAc.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("SL")){
     editSalesAccounts.add(compAc);
    }
   }
   
  }
  LOGGER.log(INFO, "getEditSalesAccounts returns {0}", editSalesAccounts);
  return editSalesAccounts;
 }

 public void setEditSalesAccounts(List<FiGlAccountCompRec> editSalesAccounts) {
  this.editSalesAccounts = editSalesAccounts;
 }

 public List<FiGlAccountCompRec> getStockEditAccounts() {
  LOGGER.log(INFO, "getCostAccounts called acs {0}", stockEditAccounts);
  if(stockEditAccounts == null){
   // need to get the GL accounts from sys buffer
   stockEditAccounts = new ArrayList<>();
   CompanyBasicRec comp= selectedPartCompany.getCompany();
   
   LOGGER.log(INFO, "getCostAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    
    selectedPartCompany.setCompany(getCompList().get(0));
    comp = selectedPartCompany.getCompany();
   }
    List<FiGlAccountCompRec> compAccounts = this.buffer.getGlAccountsByCompanyCode(comp);
    ListIterator<FiGlAccountCompRec> li = compAccounts.listIterator();
    while(li.hasNext()){
     FiGlAccountCompRec compAc = li.next();
     LOGGER.log(INFO, "compAc id {0} coa Ac id {1} coa name{2} act type code {3}", 
             new Object[]{compAc.getId(),compAc.getCoaAccount().getId(),compAc.getCoaAccount().getName(),
             compAc.getCoaAccount().getAccountType().getProcessCode() 
             });
     if(compAc.getCoaAccount().getAccountType().getProcessCode().getName().equalsIgnoreCase("CA_INV")){
      stockEditAccounts.add(compAc);
     }
     
    }
    
  }
  return stockEditAccounts;
 }

 public void setStockEditAccounts(List<FiGlAccountCompRec> stockEditAccounts) {
  this.stockEditAccounts = stockEditAccounts;
 }

 
 
 public List<FiGlAccountCompRec> getSalesAccounts() {
  if(salesAccounts == null){
   salesAccounts = new ArrayList<>();
   CompanyBasicRec comp;
   if(partCompany != null){
    comp = partCompany.getCompany();
   }else{
    comp = selectedPartCompany.getCompany();
   }
   
   LOGGER.log(INFO, "getSalesAccounts comp is {0}", comp);
   if(comp == null){
    // company not so set it to 1st in list
    if(partCompany != null){
    partCompany.setCompany(getCompList().get(0));
    comp = partCompany.getCompany();
    }else{
     partCompany.setCompany(getCompList().get(0));
    comp = selectedPartCompany.getCompany();
    }
   }
   LOGGER.log(INFO, "getSalesAccounts comp 2 is {0}", comp);
   List<FiGlAccountCompRec> compAccounts = buffer.getGlAccountsByCompanyCode(comp);
   if(compAccounts == null){
    return null;
   }
   for(FiGlAccountCompRec currAcnt: compAccounts){
   
    if(currAcnt.getCoaAccount().getAccountType().getProcessCode().getName().startsWith("GL_ACNT_INC")){
     salesAccounts.add(currAcnt);
    }
   }
  }
  return salesAccounts;
 }

 public void setSalesAccounts(List<FiGlAccountCompRec> salesAccounts) {
  this.salesAccounts = salesAccounts;
 }

 public SelectItem getDefaultFund() {
  if(defaultFund == null){
   defaultFund = new SelectItem();
   defaultFund.setLabel("Select Fund");
   
  }
  return defaultFund;
 }

 public void setDefaultFund(SelectItem defaultFund) {
  this.defaultFund = defaultFund;
 }

 
 public List<FundRec> getFunds() {
  LOGGER.log(INFO, "getFunds called funds {0} comp {1}", new Object[]{funds,company});
  if(funds == null){
   if(company == null){
    company = getCompList().get(0);
   }
   funds = buffer.getRestrictedFunds(company);
  }
  LOGGER.log(INFO, "getFunds returns funds {0}",funds);
  return funds;
 }

 public void setFunds(List<FundRec> funds) {
  this.funds = funds;
 }

 public List<SalesCatRec> getSalesCatgories() {
  if(salesCatgories == null){
   salesCatgories = buffer.getSalesCategories();
   LOGGER.log(INFO,"Number of categories returned by buffer {0}",salesCatgories.size());
  }
  if(salesCatgories != null){
   for(SalesCatRec cat :salesCatgories){
    LOGGER.log(INFO, "cat id {0} code {1}", new Object[]{cat.getId(),cat.getCode()});
   }
  }
  return salesCatgories;
 }

 public void setSalesCatgories(List<SalesCatRec> salesCatgories) {
  this.salesCatgories = salesCatgories;
 }
 
 public void onAddCompPartBtn(){
  LOGGER.log(INFO, "onAddCompPartBtn selected part {0}",selectedPart);
  LOGGER.log(INFO, "Selected part {0} physical {1}",new Object[]{selectedPart.getPartCode(),selectedPart.isPhysicalPart()});
  
 }
 
 public void onEditAddCompToPart(){
  LOGGER.log(INFO, "onEditAddCompToPart with {0}", addPartCompany);
  List<SalesPartCompanyRec> compParts = selectedPart.getSalesPartCompanies();
  if(compParts == null){
   compParts = new ArrayList<>();
  }
  long numLines = compParts.size() * -1;
  LOGGER.log(INFO, "addPartCompany id {0}", numLines);
  addPartCompany.setId(numLines);
  compParts.add(addPartCompany);
  
  selectedPart.setSalesPartCompany(compParts);
  addPartCompany = null;
  LOGGER.log(INFO, " num comp parts {0}", selectedPart.getSalesPartCompanies().size());
  
 }
 public void onAddCompToPart(){
  
  LOGGER.log(INFO, "onAddCompToPart companyPart ");
  LOGGER.log(INFO, "Sales account {0}", partCompany.getSalesAccount());
  LOGGER.log(INFO, "Physical Part {0}", part.isPhysicalPart());
  if(part.isPhysicalPart()){
   LOGGER.log(INFO, "cost price {0}", partCompany.getStockValue());
  }
  UUID uuid = UUID.randomUUID();
  long id = uuid.getLeastSignificantBits();
  if(id < 0){
   id *= -1;
  }
  partCompany.setId(id);
  partCompany.setCompany(getCompList().get(0));
  List<SalesPartCompanyRec> compParts = part.getSalesPartCompanies();
  if(compParts == null){
   compParts = new ArrayList<>();
  }
  compParts.add(partCompany);
  LOGGER.log(INFO, "partCompany minor units {0}", partCompany.getCompany().getCurrency().getMinorUnit());
  part.setSalesPartCompany(compParts);
  partCompany = null;
  LOGGER.log(INFO,"Comps for part {0}",part.getSalesPartCompanies().size());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(part.getSalesPartCompanies().size() > 0){
   rCtx.update("compsGrid");
   rCtx.update("compPartLines");
   rCtx.execute("PF('addCompDlgWv').hide()");
  }
 }
 
 public void onEditPartsSave(){
  LOGGER.log(INFO,"onEditPartsSave called with updates: {0}",salesPartUpdates);
  try{
  
  String updateStatus = this.salesMgr.updateParts(salesPartUpdates,getLoggedInUser(), getView());
  String[] statusRet = updateStatus.split("|");
  String numNew = statusRet[0];
  String numUpdates = statusRet[1];
  GenUtil.addInfoMessage("Part Updates", "Created "+numNew+" company parts " +" updated "+numUpdates+ "parts");
 }catch(BacException ex){
  GenUtil.addErrorMessage("Could not update parts. Reason: "+ex.getLocalizedMessage());
 }
 }

 public void onEditPartTabChange(TabChangeEvent evt){
  LOGGER.log(INFO, "EditPartTabChange curr tab id {0}", evt.getTab().getClientId());
  LOGGER.log(INFO, "selectedPart {0}", selectedPart.getSalesPartCompanies());
  if(StringUtils.endsWith(evt.getTab().getClientId(), "edPartCompTb")){
   // get Company parts
   if(selectedPart.getSalesPartCompanies() == null || selectedPart.getSalesPartCompanies().isEmpty()){
    selectedPart = salesMgr.addCompSalesParts(selectedPart);
   }
   LOGGER.log(INFO, "selectedPart {0}", selectedPart.getSalesPartCompanies());
  }
 }
 
 public void onEditPartUpdateBtn(){
  LOGGER.log(INFO,"onEditPartSaveBtn called with selectedPart {0}",selectedPart);
  LOGGER.log(INFO,"getLoggedInUser() {0}",getLoggedInUser());
  selectedPart.setChangedBy(getLoggedInUser());
  selectedPart.setChangedOn(new Date());
  if(selectedPart.getSalesPartCompanies() != null){
   ListIterator<SalesPartCompanyRec> li = selectedPart.getSalesPartCompanies().listIterator();
   while(li.hasNext()){
    SalesPartCompanyRec curr = li.next();
    curr.setChangedBy(selectedPart.getChangedBy());
    curr.setChangedOn(selectedPart.getChangedOn());
    li.set(curr);
   }
  }
  try{
  selectedPart = salesMgr.updateSalesPart(selectedPart, getView());
   MessageUtil.addInfoMessage("slPartUpdt", "blacRepsonse");
  }catch(Exception ex){
   MessageUtil.addErrorMessageParam1("slPartUpdt", "errorText", ex.getLocalizedMessage());
  }
  
  /*
  ListIterator<SalesPartRec> partsLi = salesParts.listIterator();
  boolean foundSalesPart = false;
  while(partsLi.hasNext() && !foundSalesPart){
   SalesPartRec partRec = partsLi.next();
   if(partRec == selectedPart){
    
    partsLi.set(selectedPart);
    foundSalesPart = true;
    LOGGER.log(INFO, "Updated salesParts with part {0}", selectedPart);
   }
  }
  if(foundSalesPart){
   // need to save to updates
   List<SalesPartRec> updates = getSalesPartUpdates();
   LOGGER.log(INFO, "foundSalesPart updates: {0}", updates);
   ListIterator<SalesPartRec> updateLi = updates.listIterator();
   boolean updateRecFound = false;
   while(updateLi.hasNext() && !updateRecFound ){
    SalesPartRec updateRec = updateLi.next();
    if(updateRec == selectedPart ){
     // have already updated part replace with new update
     updateLi.set(updateRec);
     updateRecFound = true;
    }
   }
   if(!updateRecFound){
    // no updates yet so add
    updates.add(selectedPart);
   }
   setSalesPartUpdates(updates);
   
  }
  */
  LOGGER.log(INFO, "Updates made {0}", getSalesPartUpdates());
 }
 
 public List<SalesPartRec> onSalePartComplete(String input){
  List<SalesPartRec> retList;
  if(StringUtils.isBlank(input)){
   retList = salesMgr.getAllSalesParts();
  }else{
   retList =  salesMgr.getPartsByCodePartial(input);
  }
  
  return retList;
 }
 
 public void onSalePartSelect(SelectEvent evt){
   
  selectedPart = (SalesPartRec)evt.getObject();
  LOGGER.log(INFO, "onSalePartSelect selectedPart {0}", selectedPart);
  if(selectedPart != null){
   RequestContext.getCurrentInstance().update("editOp");
  }

 }
 
 public void onSavePart(){
  LOGGER.log(INFO,"onSavePart");
  UserRec currUsr = this.getLoggedInUser();
  Date currDate = new Date();
  String page = getView();
  part.setCreatedBy(currUsr);
  part.setCreatedOn(currDate);
  List<SalesPartCompanyRec> comps = part.getSalesPartCompanies();
  ListIterator<SalesPartCompanyRec> li = comps.listIterator();
  while(li.hasNext()){
   SalesPartCompanyRec partComp = li.next();
   partComp.setId(null);
   partComp.setCreatedBy(currUsr);
   partComp.setCreatedOn(currDate);
   partComp.setActive(partComp.getValidTo().after(currDate));
   LOGGER.log(INFO, "compPart id {0}", partComp.getId());
   LOGGER.log(INFO, "compPart part id {0}", partComp.getPart().getId());
   LOGGER.log(INFO, "compPart comp id {0}", partComp.getCompany().getId());
   li.set(partComp);
  }
  
  part.setSalesPartCompany(comps);
  try{
   part = salesMgr.addSalesPart(part, currUsr, page);
   MessageUtil.addInfoMessage("slPartAdded", "blacResponse");
   part = new SalesPartRec(); 
   partCompany = new SalesPartCompanyRec();
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("salesPartFrm");
  }catch(BacException ex){
  LOGGER.log(INFO, "Could save part BacException {0}", ex.getLocalizedMessage());
  MessageUtil.addErrorMessage("slPartAdd", "errorText");
 }catch(Exception ex){
  LOGGER.log(INFO, "Could save part Exception {0}", ex.getLocalizedMessage());
  MessageUtil.addErrorMessage("slPartAdd", "errorText");
 }
 
  
 }

 public void onSearchPartsBtn(){
  LOGGER.log(INFO, "onSearchPartsBtn called partCodeSearchCriteria {0}",partCodeSearchCriteria);
  this.salesParts = salesMgr.getPartsByCodePartial(partCodeSearchCriteria);
 }
 
 public void onEditCompPartBtn(){
  LOGGER.log(INFO, "onEditCompPartBtn called selected part {0}",selectedPartCompany);
 }
 
 public void onEditAddCompPartBtn(){
  LOGGER.log(INFO, "onEditAddCompPartBtn called selected part {0}",this.addPartCompany);
 }
 
 public void onEditCompPartSaveBtn(){
  LOGGER.log(INFO, "onEditCompPartSaveBtn called selected part {0}",selectedPartCompany);
  if(partCompanyUpdated == null){
   partCompanyUpdated = new ArrayList<>();
  }
  partCompanyUpdated.add(selectedPartCompany); 
  LOGGER.log(INFO, "partCompanyUpdated {0}", partCompanyUpdated);
  List<SalesPartCompanyRec> selectedPartCompRecs = selectedPart.getSalesPartCompanies();
  LOGGER.log(INFO, "selectedPartCompRecs {0}", selectedPartCompRecs);
  ListIterator<SalesPartCompanyRec> compPartsLi =  selectedPartCompRecs.listIterator();
  boolean foundCompPart = false;
  while(compPartsLi.hasNext() && !foundCompPart){
   SalesPartCompanyRec compPartRec = compPartsLi.next();
   if(compPartRec == selectedPartCompany){
    compPartsLi.set(selectedPartCompany);
    foundCompPart = true;
    LOGGER.log(INFO, "updated selectedPart.getSalesPartCompanies() with {0}", selectedPartCompany);
   }
  }
  
  selectedPart.setSalesPartCompany(selectedPartCompRecs);
  ListIterator<SalesPartRec> salesPartsLi = salesParts.listIterator();
  boolean salesPartFound = false;
  while(salesPartsLi.hasNext() && !salesPartFound){
   SalesPartRec partRec = salesPartsLi.next();
   LOGGER.log(INFO,"partRec {0} selectedPart {1}",new Object[]{partRec.getId(),selectedPart.getId()});
   if(partRec == selectedPart){
    LOGGER.log(INFO,"parts rec found partRec price {0} ",selectedPart.getSalesPartCompanies().get(0).getSaleValue());
    salesPartsLi.set(selectedPart);
    salesPartFound = true;
    
   }
  }
  LOGGER.log(INFO, "selectedPart comp sales price {0}", selectedPart.getSalesPartCompanies().get(0).getSaleValue());
  
 }
 
 public void onEditPartPhysBool(){
  LOGGER.log(INFO, "onEditPartPhysBool {0}", selectedPart.isPhysicalPart());
  if(!selectedPart.isPhysicalPart()){
   if(selectedPart.getSalesPartCompanies() != null){
    ListIterator<SalesPartCompanyRec> li = selectedPart.getSalesPartCompanies().listIterator();
    while(li.hasNext()){
     SalesPartCompanyRec curr = li.next();
     curr.setCosAccount(null);
     curr.setCostValue(0);
     curr.setStockAccount(null);
     curr.setStockValue(0);
     curr.setCostOfSalesAccounting(false);
     li.set(curr);
    }
   }
  }
  RequestContext.getCurrentInstance().update("editCompPartLines");
 }
 public void onDispPartMenu(){
  LOGGER.log(INFO, "onDispPartMenu {0}", selectedPart.getSalesPartCompanies());
  if(selectedPart.getSalesPartCompanies() == null || selectedPart.getSalesPartCompanies().isEmpty() ){
   LOGGER.log(INFO, "Refresh comp parts from db");
   selectedPart = salesMgr.addCompSalesParts(selectedPart);
   
  }
  
 }
 public void onEditPartMenu(){
  LOGGER.log(INFO, "onEditPartMenu {0}", selectedPart.isPhysicalPart());
  if(selectedPart.getSalesPartCompanies() == null || selectedPart.getSalesPartCompanies().isEmpty() ){
   LOGGER.log(INFO, "Refresh comp parts from db");
   selectedPart = salesMgr.addCompSalesParts(selectedPart);
   
  }
 }
 public void onSelectPart(SelectEvent evt){
  LOGGER.log(INFO,"onSelectPart called witg {0}", evt.getObject() );
  if(selectedPart == null){
   selectedPart = (SalesPartRec)evt.getObject();
  }
  
   selectedPart = salesMgr.addCompSalesParts(selectedPart);
  
  LOGGER.log(INFO, "onSelectPart end comp parts {0}", selectedPart.getSalesPartCompanies());
 }
 
 public void onDispPartClose(){
  LOGGER.log(INFO,"onDispPartClose called");
 
 }
 public List<SalesPartRec> getSalesParts() {
  if(salesParts == null){
   salesParts = new ArrayList<>();//this.salesMgr.getAllSalesParts();
  }
  return salesParts;
 }

 public void setSalesParts(List<SalesPartRec> salesParts) {
  this.salesParts = salesParts;
 }

 public SalesPartRec getSelectedPart() {
  return selectedPart;
 }

 public List<SalesPartRec> getSalesPartUpdates() {
  if(salesPartUpdates == null){
   salesPartUpdates = new ArrayList<>();
  }
  return salesPartUpdates;
 }

 public void setSalesPartUpdates(List<SalesPartRec> salesPartUpdates) {
  this.salesPartUpdates = salesPartUpdates;
 }

 
 public void setSelectedPart(SalesPartRec selectedPart) {
  LOGGER.log(INFO,"setSelectedPart called with {0}",selectedPart);
   if(selectedPart != null){
    this.selectedPart = selectedPart;
    LOGGER.log(INFO,"setSelectedPart called with {0}",selectedPart.getSalesPartCompanies());
   }
 }

 public SalesPartCompanyRec getSelectedPartCompany() {
  return selectedPartCompany;
 }

 public void setSelectedPartCompany(SalesPartCompanyRec selectedPartCompany) {
  //this.selectedPartCompany = selectedPartCompany;
 }

 public List<SalesPartRec> getFilteredSalesParts() {
  return filteredSalesParts;
 }

 public void setFilteredSalesParts(List<SalesPartRec> filteredSalesParts) {
  this.filteredSalesParts = filteredSalesParts;
 }
 
 
 public void onChangeSalesPrices(ValueChangeEvent evt){
  LOGGER.log(INFO, "onChangeSalesPrices called with {0}", evt.getNewValue());
 }
 
 public void onCompPartSelect(SelectEvent evt){
  LOGGER.log(INFO,"onCompPartSelect called with {0}",evt.getObject());
  partCompanyRowSelected = true;
  selectedPartCompany = (SalesPartCompanyRec)evt.getObject();
  
  LOGGER.log(INFO, "salesAccount {0}", selectedPartCompany.getId());
  LOGGER.log(INFO, "selectedPartCompany comp {0}", selectedPartCompany.getCompany().getReference());
 }
 
 public void onCompPartToggle(ToggleEvent evt){
  LOGGER.log(INFO, "onCompPartToggle called with visinility {0}", evt.getVisibility());
  LOGGER.log(INFO, "onCompPartToggle called with object {0}", evt.getData());
  if(evt.getVisibility()== Visibility.VISIBLE ){
   this.selectedPartCompany = (SalesPartCompanyRec)evt.getData();
  }
 }
 
 public void onCompPartEditDlg(){
  LOGGER.log(INFO, "onCompPartEditDlg selected comp part {0}", selectedPartCompany.getCompany().getReference());
  LOGGER.log(INFO, "Cat {0}", selectedPartCompany.getCategory().getSortDescr());
  if(selectedPartCompany.getCategory() == null){
   selectedPartCompany.setCategory(this.getSalesCatgories().get(0));
  }
 }
 
 public void onCompPartNewAdd(){
  partCompany = new SalesPartCompanyRec();
  partCompany.setPart(part);
  partCompany.setCompany(getCompList().get(0));
  partCompany.setValidTo(getMaxDate());
  LOGGER.log(INFO, "currency minor unit {0}", partCompany.getCompany().getCurrency().getMinorUnit());
  LOGGER.log(INFO, "partCompany valid to  {0}", partCompany.getValidTo());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addCompPtGrid");
  rCtx.execute("PF('addCompDlgWv').show();");
  
  
 }
 public void onCompPartNewDelete(){
  ListIterator<SalesPartCompanyRec> li = part.getSalesPartCompanies().listIterator();
  boolean foundPart = false;
  while(li.hasNext() && !foundPart){
   if(Objects.equals(li.next().getId(), selectedPartCompany.getId()) ){
    li.remove();
    foundPart= true;
   }
  }
  if(foundPart){
   RequestContext.getCurrentInstance().update("compPartLines");
  }
 }
 
 public void onCompPartEditTrf(){
  LOGGER.log(INFO, "onCompPartEditTrf");
  selectedPartCompany.setChangedBy(this.getLoggedInUser());
  selectedPartCompany.setChangedOn(new Date());
  if(selectedPartCompany.getValidTo().before(new Date())){
   selectedPartCompany.setActive(false);
  }else{
   selectedPartCompany.setActive(true);
  }
  LOGGER.log(INFO, "Active {0}", selectedPartCompany.isActive());
  ListIterator<SalesPartCompanyRec> li = selectedPart.getSalesPartCompanies().listIterator();
  boolean foundCompPt = false;
  while(li.hasNext() && !foundCompPt){
   SalesPartCompanyRec curr = li.next();
   if(Objects.equals(curr.getId(), selectedPartCompany.getId()) ){
    li.set(selectedPartCompany);
    foundCompPt = true;
   }
  }
  if(foundCompPt){
   RequestContext  rCtx = RequestContext.getCurrentInstance();
   rCtx.update("changePartAP:editCompPartLines");
   rCtx.execute("PF('editCompDlgWv').hide();");
  }
 }
 
 public void onDispTabChange(TabChangeEvent evt){
  LOGGER.log(INFO, "onDispTabChange called with {0}", evt);
  LOGGER.log(INFO, "selected part is {0}", this.selectedPart);
  selectedPart = this.salesMgr.addCompSalesParts(selectedPart);
  LOGGER.log(INFO, "selected part has comp parts {0}",selectedPart.getSalesPartCompanies());
 }
}

