/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.common.ProcessCodeRec;
import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import javax.faces.event.ActionEvent;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.ejbBean.common.SysBuffer;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class AccountTypeBean extends BaseBean {

    @EJB
    private CompanyManager compMgr;
    
    @EJB
    private BasicSetup basicConfig;
    
    @EJB
    private SysBuffer sysBuff;
    
    private AccountTypeRec acType;
    private AccountTypeRec selectedAcType;
    private AccountTypeRec delAcType;
    private List<AccountTypeRec> acTypes;
    private List<NumberRangeRec> numControls;
    private List<LedgerRec> ledgers;
    private List<ModuleRec> modules;
    private List<ProcessCodeRec> processCodes;
    private boolean subLedAllowed;

    
    private static final Logger LOGGER =   Logger.getLogger(AccountTypeBean.class.getName());
    
    /** Creates a new instance of AccountTypeBean */
    public AccountTypeBean() {
        LOGGER.log(INFO, "Account Type constructor");
        
    }

    
    public CompanyManager getCompMgr() {
        return compMgr;
    }

    public void setCompMgr(CompanyManager compMgr) {
        this.compMgr = compMgr;
    }

    public AccountTypeRec getAcType() {
     if(acType == null){
      acType = new AccountTypeRec();
      LOGGER.log(INFO, "Account Type is: {0}",acType);   
     }
     return acType;
    }

    public void setAcType(AccountTypeRec acType) {
        this.acType = acType;
    }
    
    public void createAcType(){
     LOGGER.log(INFO, "Save button clicked with name: ");
     LOGGER.log(INFO, "Account type module {0} num range {1} ledger {2}", 
                new Object[]{acType.getModule(),acType.getNumberRange(),acType.getSubLedger()});
     acType.setCreatedBy(this.getLoggedInUser());
     acType.setCreatedDate(new Date());
     acType = compMgr.createAccountType(acType,getView());
     //String msgHdr = this.responseForKey("actType");
     LOGGER.log(INFO,"acType id {0}",acType.getId() );
     if(acType.getId() != null){
      
      //String msg = this.responseForKey("actTypeCr") + acType.getName();
      MessageUtil.addClientInfoMessage("actTypFrm:msgs", "actTypeCr", "blacResponse", acType.getName());
      //MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
      acType = null;
      PrimeFaces pf = PrimeFaces.current();
      pf.ajax().update("actTypFrm:acntTypPG");
      pf.ajax().update("actTypFrm:msgs");
     }else{
      //String msg = this.errorForKey("acntTypeCr") + acType.getName();
      //MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
      MessageUtil.addClientInfoMessage("actTypFrm:msgs", "actTypeCr", "errorYText", acType.getName());
     }  
        
    }
    
    public void saveButtonAction(ActionEvent e){
      LOGGER.log(INFO, "Save button action event ");  
    }

    public AccountTypeRec getSelectedAcType() {
        
        return selectedAcType;
    }

    public void setSelectedAcType(AccountTypeRec sel) {
     LOGGER.log(INFO, "setSelectedAcType called with {0}", sel);
        this.selectedAcType = sel;
    }

    public AccountTypeRec getDelAcType() {
        return delAcType;
    }

    public void setDelAcType(AccountTypeRec delAcType) {
        LOGGER.log(INFO, "setDelAcType with {0}", delAcType.getId());
        this.delAcType = delAcType;
        boolean rc = compMgr.deleteAccountType(delAcType);
        if(rc){
            // delete succeeded
            boolean found = false;
            ListIterator it = acTypes.listIterator();
            while(it.hasNext() && !found){
               AccountTypeRec del = (AccountTypeRec)it.next();
               if(del.getId() == delAcType.getId()){
                   it.remove();
                   found = true;
               }
               
            }
            
            String mess = this.getvalidationText().getString("actTypeDelActTyp")+ " "+delAcType.getName();;
            GenUtil.addInfoMessage(mess);
            
        }
        else{
         String mess = this.getErrorMessage().getString("actTypeNoDelete");
         GenUtil.addErrorMessage(mess);
        }
        
    }

 
 public List<LedgerRec> getLedgers() {
  if(ledgers == null ){
   ledgers = sysBuff.getLedgers();
   LOGGER.log(INFO, "Ledgers from sys buff {0}", ledgers);
  }
  return ledgers;
 }

 public void setLedgers(List<LedgerRec> ledgers) {
  this.ledgers = ledgers;
 }

 public List<ModuleRec> getModules() {
  if(modules == null){
   modules = sysBuff.getModules();
  }
  return modules;
 }

 public void setModules(List<ModuleRec> modules) {
  this.modules = modules;
 }

    
 public List<NumberRangeRec> getNumControls() {
  if(numControls == null || numControls.isEmpty()){
   numControls = this.basicConfig.getNumerControlsAll();
  }
  return numControls;
 }

 public void setNumControls(List<NumberRangeRec> numControls) {
  this.numControls = numControls;
 }

 public List<ProcessCodeRec> getProcessCodes() {
  if(processCodes == null){
   processCodes = this.sysBuff.getProcessCodes();
  }
  return processCodes;
 }

 public void setProcessCodes(List<ProcessCodeRec> processCodes) {
  this.processCodes = processCodes;
 }

 
 public boolean isSubLedAllowed() {
  return subLedAllowed;
 }

 public void setSubLedAllowed(boolean subLedAllowed) {
  this.subLedAllowed = subLedAllowed;
 }
  
 public void onDeleteActTy(){
  LOGGER.log(INFO, "onDeleteActTy called");
 }
 
 public void onEditActTyMnu(){
  LOGGER.log(INFO, "onEditActTyMnu called with selection {0}", this.selectedAcType);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("actTyEdFrmId");
  pf.executeScript("PF('editAcntGrpWvar').show();");
 }
 
 
 public void onSubLedgerChanged(ValueChangeEvent evt){
  
 }
 
 public void onCntrlAcntEdChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onCntrlAcntEdChange called with old {0} new {1} ", new Object[]{
   evt.getOldValue(),evt.getNewValue() });
  Boolean cntrlSet = (Boolean)evt.getNewValue();
  selectedAcType.setControlAccount(cntrlSet);
  if(!cntrlSet){
   selectedAcType.setSubLedger(null);
  }
  PrimeFaces pf = PrimeFaces.current();
  List<String> updates = new ArrayList<String>();
  updates.add("edCntrlAct1m");
  updates.add("edCntrlActOl");
  pf.ajax().update(updates);
  
          
 }
 
 public void onActTypeUpdate(){
  LOGGER.log(INFO, "onActTypeUpdate called with selection {0}",selectedAcType);
  selectedAcType.setChangedBy(this.getLoggedInUser());
  selectedAcType.setChangedDate(new Date());
  selectedAcType = this.basicConfig.updateAccountType(selectedAcType, getView());
  
  ListIterator<AccountTypeRec> li = this.acTypes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   AccountTypeRec rec = li.next();
   if(rec.getId() == selectedAcType.getId()){
    li.set(rec);
    found = true;
   }
  }
  String msg =  this.responseForKey("actTypeChanged")+" " + selectedAcType.getName();
  String msgHdr = responseForKey("actType");
  MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
  selectedAcType = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("actTypeTbl");
  pf.executeScript("PF('editAcntGrpWvar').hide()");
 }
 
 public void onModuleChange(ValueChangeEvent evt){
  LOGGER.log(INFO, "onModuleChange called with Mod {0}", evt.getNewValue());
 }
 public void onNumCntrlChanged(ValueChangeEvent evt){
  LOGGER.log(INFO, "NumCntrlChanged old {0} new {1}", new Object[]{evt.getOldValue(),evt.getNewValue()});
  getAcType();
  NumberRangeRec newNumCntrl = (NumberRangeRec)evt.getNewValue();
  acType.setNumberRange(newNumCntrl);
 }
 
 public void onPlChanged(ValueChangeEvent evt){
  LOGGER.log(INFO, "Pl changed old {0} new {1}", new Object[]{evt.getOldValue(),evt.getNewValue()});
  getAcType();
  boolean pl = (Boolean)evt.getNewValue();
  this.acType.setProfitAndLossAccount(pl);
  
 }  
 public List<AccountTypeRec> getAcTypes() {
  if(acTypes == null){
   acTypes = sysBuff.getAcntTypes();
   LOGGER.log(INFO, "acTypes from sys buff {0}", acTypes.size());
  }
  return acTypes;
 }

    public void setAcTypes(List<AccountTypeRec> acTypes) {
        this.acTypes = acTypes;
    }
 
    
    
}
