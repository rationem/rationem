/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ma.programme;
import com.rationem.util.BaseBean;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Chris
 */
public class ProgrammeBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger("programme.ProgrammeBean");
 private ProgrammeRec programme;
 private CompanyBasicRec compSelected;
 private List<PartnerPersonRec> respPersons;
 private PartnerPersonRec respPerson;
 private List<ProgrammeRec> programmes;
 private List<ProgrammeRec> programmesFiltered;
 
 
 @EJB
 private SysBuffer buff;
 
 @EJB
 private MasterDataManager masterDataMgr;
 
 @EJB
 private ProgrammeMgr programmeMgr;
 /**
  * Creates a new instance of ProgrammeBean
  */
 public ProgrammeBean()  {
 }

 @PostConstruct
 private void init(){
  compSelected = getCompList().get(0);
  programmes = programmeMgr.getAllProgrammes(compSelected);
 }
 public CompanyBasicRec getCompSelected() {
  if(compSelected == null){
   compSelected =this.getCompList().get(0);
  }
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }

 public ProgrammeRec getProgramme() {
  if(programme == null){
   programme = new ProgrammeRec();
   Date fromDt = new Date();
   GregorianCalendar cal = new GregorianCalendar();
   cal.set(9999, 11, 31);
   Date toDt = cal.getTime();
   programme.setValidFrom(fromDt);
   programme.setValidTo(toDt);
   programme.setProgrammeForCompany(this.getCompList().get(0));
  }
  return programme;
 }

 public void setProgramme(ProgrammeRec programme) {
  this.programme = programme;
 }
 
 public List<ProgrammeRec> getProgrammes() {
  if(programmes == null){
   programmes = this.programmeMgr.getAllProgrammes(getCompList().get(0));
  }
  return programmes;
 }

 public void setProgrammes(List<ProgrammeRec> programmes) {
  this.programmes = programmes;
 }

 public List<ProgrammeRec> getProgrammesFiltered() {
  return programmesFiltered;
 }

 public void setProgrammesFiltered(List<ProgrammeRec> programmesFiltered) {
  this.programmesFiltered = programmesFiltered;
 }

 
 public List<PartnerPersonRec> getRespPersons() {
  if(respPersons == null){
   respPersons = new ArrayList<PartnerPersonRec>(); 
  }
  return respPersons;
 }

 public void setRespPersons(List<PartnerPersonRec> respPersons) {
  this.respPersons = respPersons;
 }

 public List<PartnerPersonRec> respPersComplete(String input){
  List<PartnerPersonRec> pers; 
  if(input == null || input.isEmpty()){
   pers = masterDataMgr.getAllPartnerIndividual();
  }else{
   
   pers = masterDataMgr.getIndivPtnrsBySurname(input);
  }
  return pers;
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
 
 public List<ProgrammeRec> onProgComplete(String input){
  logger.log(INFO, "onProgComplete called with {0}", programme);
  List<ProgrammeRec> list = this.programmeMgr.getProgrammesByRef(this.programme.getProgrammeForCompany(), input);
  
  return list;
 }
 
 public void onProgSelect(SelectEvent evt){
  logger.log(INFO, "onProgSelect called with {0}", evt.getObject());
  this.programme = (ProgrammeRec)evt.getObject();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("programmeUpdt");
  
 }
 public void onAddRespPersDlg(){
  logger.log(INFO, "onAddRespPersDlg called");
  respPerson = new PartnerPersonRec();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addRespPersDlg");
  pf.executeScript("PF('crPersDlg').show()");
 }
 
 
 public void onCompanyChange(ValueChangeEvent evt){
  compSelected = (CompanyBasicRec)evt.getNewValue();
  programmes = this.programmeMgr.getAllProgrammes(compSelected);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("prgTbl");
 }
 public void onSaveNewProgramme(){
  logger.log(INFO, "onSaveNewProgramme called");
  try{
   UserRec usr = getLoggedInUser();
   Date crDate = new Date();
   programme.setCreatedBy(usr);
   programme.setCreatedOn(crDate);
   programmeMgr.updateProgramme(programme, usr, getView());
   MessageUtil.addInfoMessage("maProgrammeCr", "blacResponse");
   programme = null;
  }catch(BacException ex){
   logger.log(INFO, "Prog save BacException {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("maProgrammeSave", "errorText");
  }catch(Exception ex){
   logger.log(INFO, "Prog save Exception {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("maProgrammeSave", "errorText");
  }
 }
 
 public void onSaveNewRespPerson(){
  try{
   respPerson.setCreatedBy(getLoggedInUser());
   respPerson.setCreatedDate(new Date());
   Long id = this.masterDataMgr.createIndivPartnerAR(respPerson, this.getLoggedInUser(), this.getView());
   if(id == null || id == 0){
    MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
    
   }else{
    respPerson.setId(id);
    programme.setResponsibilityOf(respPerson);
    MessageUtil.addInfoMessage("ptnrIndivCr", "blacResponse");
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("resp");
    pf.executeScript("PF('crPersDlg').hide()");
   }
   
  }catch(BacException ex){
   logger.log(INFO, "Program partner not created. Error: {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
  }catch(Exception ex){
   logger.log(INFO, "Program partner not created. Error: {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("partnerIndivCr", "errorText");
  }finally{
   respPerson = null;
  }
  
 }
 
 public void onSaveUpdateProgramme(){
  logger.log(INFO, "onSaveUpdateProgramme called");
  try{
   UserRec usr = getLoggedInUser();
   Date crDate = new Date();
   programme.setChangedBy(usr);
   programme.setChangedOn(crDate);
   programmeMgr.updateProgramme(programme, usr, getView());
   MessageUtil.addInfoMessage("maProgrammeUpdt", "blacResponse");
   programme = null;
  }catch(BacException ex){
   logger.log(INFO, "Prog save BacException {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("maProgrammeUpdate", "errorText");
  }catch(Exception ex){
   logger.log(INFO, "Prog save Exception {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("maProgrammeUpdate", "errorText");
  }
 }
}
