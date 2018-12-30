/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ma.costCentre;

import com.rationem.util.BaseBean;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Chris
 */
public class CostCenreBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("accounts.ma.costCentre.CostCenreBean");
 
 private CostCentreRec newCostCent;
 private CostCentreRec updateCostCent;
 private CompanyBasicRec compSelected;
 private List<CostCentreRec> costCentreList;
 private List<CostCentreRec> costCentresFiltered;
 private CostCentreRec costCentreSelected;
 
 
 private List<PartnerPersonRec> respPersons;
 private PartnerPersonRec respPerson;
 
 @EJB
 private SysBuffer buff;
 
 @EJB
 private MasterDataManager masterDataMgr;
 
 @EJB
 private CostCentreMgr costCentreMgr;

 /**
  * Creates a new instance of CostCenreBean
  */
 public CostCenreBean() {
 }

 @PostConstruct
 private void init(){
 
  compSelected = this.getCompList().get(0);
  costCentreList = this.costCentreMgr.getCostCentresForCompany(compSelected);
  
 }

 public CostCentreRec getNewCostCent() {
  if(newCostCent == null){
   newCostCent = new CostCentreRec();
  }
  return newCostCent;
 }

 public void setNewCostCent(CostCentreRec newCostCent) {
  this.newCostCent = newCostCent;
 }

 
 public List<CostCentreRec> onCcComplete(String input){
  CompanyBasicRec comp = this.getCompList().get(0);
  List<CostCentreRec> retList = this.costCentreMgr.getCostCentresByRef(comp, input);
  logger.log(INFO, "onCcComplete returns from DB {0}", retList);
  
  
  return retList;
 }
 
 public void onCcEditDlgShow(){
  
  updateCostCent = this.costCentreSelected;
  logger.log(INFO, "Update ref {0}", updateCostCent.getRefrence());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editCcFrm");
  rCtx.execute("PF('editCcDlgWv').show()");
  
 }
 
 public void onCcEditTransfer(){
  ListIterator<CostCentreRec> li = this.costCentreList.listIterator();
  boolean foundCostCent = false;
  while(li.hasNext() && !foundCostCent){
   CostCentreRec cc = li.next();
   if(cc.getId() == updateCostCent.getId()){
    li.set(updateCostCent);
    foundCostCent = true;
   }
  }
  
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("ccList");
  rCtx.execute("PF('editCcDlgWv').hide()");
  updateCostCent = null;
  
 }
 
 public void onCcSelect(SelectEvent evt){
  CostCentreRec selected = (CostCentreRec)evt.getObject();
  this.updateCostCent = selected;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("costCntreUpdt");
 }
 
 public void onAddRespPerson(){
  logger.log(INFO, "onAddRespPerson called for person family name {0}",respPerson.getFamilyName());
  if(respPerson.getFamilyName() == null || respPerson.getFamilyName().isEmpty()){
   MessageUtil.addErrorMessage("ptnrNameNone", "validationText");
   return;
  }
  try{
   respPerson.setCreatedBy(this.getLoggedInUser());
   respPerson.setCreatedDate(new Date());
   Long id = masterDataMgr.createIndivPartnerAR(respPerson, this.getLoggedInUser(), this.getView());
   MessageUtil.addInfoMessage("ptnrIndivCr", "blacResponse");
   respPerson.setId(id);
   newCostCent.setResponsibilityOf(respPerson);
   respPerson = null;
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("addRespPersDlg");
   rCtx.update("resp");
   rCtx.execute("PF('crPersDlg').hide();");
  }catch(BacException ex){
   MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
   
  }catch(Exception ex){
   MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
   
  }
  
 }
 
 public void onAddRespPersonClose(){
  logger.log(INFO, "onAddRespPersonClose");
  respPerson = new PartnerPersonRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addRespPersDlg");
  rCtx.execute("PF('crPersDlg').hide();");
 }
 public void onAddRespPersonDlg(){
  logger.log(INFO, "onAddRespPersonDlg called");
  respPerson = new PartnerPersonRec();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addRespPersDlg");
  rCtx.execute("PF('crPersDlg').show();");
 }
 
 public void onSaveCostCentre(){
  logger.log(INFO, "onSaveCostCentre called");
  Date currDt = new Date();
  UserRec usr = getLoggedInUser();
  newCostCent.setCreatedBy(usr);
  newCostCent.setCreatedOn(currDt);
  try{
   this.costCentreMgr.updateCostCentre(newCostCent, usr, this.getView());
   MessageUtil.addInfoMessage("maCostCentreCr", "blacResponse");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   newCostCent = new CostCentreRec();
   rCtx.update("costCntreCr");
   
  }catch(BacException ex){
   MessageUtil.addErrorMessage("maCostCentSave", "errotText");
   
  }catch(Exception ex){
   MessageUtil.addErrorMessage("maCostCentSave", "errotText");
  }
  
 }
 
 public void onSaveUpdateCostCent(){
  logger.log(INFO, "onSaveUpdateCostCent");
  updateCostCent.setChangedBy(this.getLoggedInUser());
  updateCostCent.setChangedOn(new Date());
  try{
   costCentreMgr.updateCostCentre(updateCostCent, getLoggedInUser(), getView());
   MessageUtil.addInfoMessage("maCostCentreUpdt", "blacResponse");
  }catch(Exception ex){
   logger.log(INFO, "Update cost centre error {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("maCostCentSave", "errorText");
  }
 }

 public CompanyBasicRec getCompSelected() {
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }

 public List<CostCentreRec> getCostCentreList() {
  return costCentreList;
 }

 public void setCostCentreList(List<CostCentreRec> costCentreList) {
  this.costCentreList = costCentreList;
 }

 public List<CostCentreRec> getCostCentresFiltered() {
  return costCentresFiltered;
 }

 public void setCostCentresFiltered(List<CostCentreRec> costCentresFiltered) {
  this.costCentresFiltered = costCentresFiltered;
 }

 public CostCentreRec getCostCentreSelected() {
  return costCentreSelected;
 }

 public void setCostCentreSelected(CostCentreRec costCentreSelected) {
  logger.log(INFO, "setCostCentreSelected called with {0}", costCentreSelected);
  this.costCentreSelected = costCentreSelected;
 }
 
 
 public List<PartnerPersonRec> getRespPersons() {
  if(respPersons == null){
   respPersons = masterDataMgr.getAllPartnerIndividual();
  }
  
  return respPersons;
 }
 
 

 public void setRespPersons(List<PartnerPersonRec> respPersons) {
  this.respPersons = respPersons;
 }

 public PartnerPersonRec getRespPerson() {
  if(respPerson == null){
   respPerson = new PartnerPersonRec();
  }
  return respPerson;
 }

 public void setRespPerson(PartnerPersonRec respPerson) {
  this.respPerson = respPerson;
 }
 
 public List<PartnerPersonRec> respPersComplete(String input){
  List<PartnerPersonRec> pers = new ArrayList<PartnerPersonRec>();
  if(input == null || input.isEmpty()){
   pers = getRespPersons();
  }else{
   
   pers = masterDataMgr.getIndivPtnrsBySurname(input);
  }
  return pers;
 }

 public CostCentreRec getUpdateCostCent() {
  if(updateCostCent == null){
   updateCostCent = new CostCentreRec();
   updateCostCent.setCostCentreForCompany(this.getCompList().get(0));
  }
  return updateCostCent;
 }

 public void setUpdateCostCent(CostCentreRec updateCostCent) {
  this.updateCostCent = updateCostCent;
 }
 
 
}
