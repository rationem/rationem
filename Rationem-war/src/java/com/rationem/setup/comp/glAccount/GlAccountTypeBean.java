/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.comp.glAccount;
import com.rationem.util.BaseBean;
import com.rationem.ejbBean.common.SysBuffer;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import com.rationem.busRec.config.company.LedgerRec;
import java.util.ListIterator;
import com.rationem.util.GenUtil;
import com.rationem.exception.BacException;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.MessageUtil;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.primefaces.event.SelectEvent;


/**
 * Defines GL account types. The Account Type the defines the rules for a GL account
 * @author Chris
 */
public class GlAccountTypeBean extends BaseBean {
    private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.comp.glAccount.GlAccountTypeBean");

    @EJB
    private BasicSetup setupEjb;

    @EJB
    private SysBuffer sysBuffer;

    private AccountTypeRec accountType;
    

    private List<NumberRangeRec> numCntrlLst;
    private List<AccountTypeRec> accountTypes;
    private List<AccountTypeRec> updateAccountTypes;
    private List<AccountTypeRec> deleteAccountTypes;
    private AccountTypeRec selectedAcntType;
    private AccountTypeRec selectedAccountType;
    private ArrayList<SelectItem> numCntrlSel;
    private List<NumberRangeRec> numCntrls;
    private Long numCntrlId;
    
    private List<LedgerRec> ledgerLst;
    private List<SelectItem> ledgerSelLst;
    

    private boolean subLedgerDisabled = false;
    private boolean sysPostDisabled = false;
    private boolean edNumRangeChange = false;






    
    




    /** Creates a new instance of GlAccountTypeBean */
    public GlAccountTypeBean() {
        logger.log(INFO, "Create GL act type Bean");

    }

    public AccountTypeRec getAccountType() {
        if(accountType == null){
            accountType = this.setupEjb.getAccountType();
        }
        return accountType;
    }

    public void setAccountType(AccountTypeRec accountType) {
        this.accountType = accountType;
    }

  public AccountTypeRec getSelectedAccountType() {
   if(selectedAccountType == null){
    logger.log(INFO, "new selectedAccountType");
    selectedAccountType = new AccountTypeRec(); 
   }
    return selectedAccountType;
  }

  public void setSelectedAccountType(AccountTypeRec actType) {
   logger.log(INFO, "setSelectedAccountType called with {0}", actType);
    this.selectedAccountType = actType;
  }

  public List<AccountTypeRec> getAccountTypes() {
    if(accountTypes == null){
      accountTypes = this.setupEjb.getFiGlAccountTypesAll();
    }
    return accountTypes;
  }

  public void setAccountTypes(ArrayList<AccountTypeRec> accountTypes) {
    this.accountTypes = accountTypes;
  }

 public List<AccountTypeRec> getUpdateAccountTypes() {
  return updateAccountTypes;
 }

 public void setUpdateAccountTypes(List<AccountTypeRec> updateAccountTypes) {
  this.updateAccountTypes = updateAccountTypes;
 }

 public AccountTypeRec getSelectedAcntType() {
  if(selectedAcntType == null){
   selectedAcntType = new AccountTypeRec();
  }
  logger.log(INFO,"getSelectedAcntType {0}",selectedAcntType);
  return selectedAcntType;
 }

 public void setSelectedAcntType(AccountTypeRec selectedAcntType) {
  this.selectedAcntType = selectedAcntType;
 }

  
    public Long getNumCntrlId() {
        return numCntrlId;
    }

    public void setNumCntrlId(Long numCntrlId) {
        this.numCntrlId = numCntrlId;
    }

   

    

    public void setNumCntrlSel(ArrayList<SelectItem> numCntrlSel) {
        this.numCntrlSel = numCntrlSel;
    }

 public List<NumberRangeRec> getNumCntrls() {
  if(numCntrls == null){
   numCntrls = setupEjb.getNumerControlsAll();
   //GL
   ListIterator<NumberRangeRec> li = numCntrls.listIterator();
   while(li.hasNext()){
    NumberRangeRec numCntrl = li.next();
    if(!numCntrl.getModule().getModuleCode().equalsIgnoreCase("GL")){
     li.remove();
    }
   }
  }
  return numCntrls;
 }

 public void setNumCntrls(List<NumberRangeRec> numCntrls) {
  this.numCntrls = numCntrls;
 }

 
 public List<LedgerRec> getLedgerLst() {
  logger.log(INFO, "GL Ac bean getLedgerLst called ledgerList: {0}",ledgerLst);
  if(ledgerLst != null){
   logger.log(INFO, "Ledger list is {0} return exixting list", ledgerLst);
    return ledgerLst;
  }else{
   try{
    ledgerLst = setupEjb.getLedgers();
    LedgerRec lgr = new LedgerRec();
    long defId = -1;
    lgr.setId(defId);
    lgr.setCode("NONE");
    lgr.setDescr("Select");
    ledgerLst.add(0, lgr);
    logger.log(INFO, "Number of ledgers returened from DB {0}", ledgerLst.size());
    ListIterator li = ledgerLst.listIterator();
                while(li.hasNext()){

                    //remove GL as a subledger
                    LedgerRec led = (LedgerRec)li.next();
                    logger.log(INFO, "Check ledger for gl {0}", led.getCode());
                    if(led.getCode().equalsIgnoreCase("GL")){
                        li.remove();
                    }
                }
                logger.log(INFO, "Number of ledgers returened from EJB {0}", ledgerLst.size());
                return ledgerLst;
            }catch(BacException e){
                GenUtil.addErrorMessage("Could not find Ledgers: "+e.getLocalizedMessage());
            }

        }
        logger.log(INFO, "Number of ledgers returened from EJB {0} - should not be here",ledgerLst);
        return ledgerLst;
    }

    public void setLedgerLst(ArrayList<LedgerRec> ledgerLst) {
        this.ledgerLst = ledgerLst;
    }

    public List<NumberRangeRec> getNumCntrlLst() {
        return numCntrlLst;
    }

    public void setNumCntrlLst(ArrayList<NumberRangeRec> numCntrlLst) {
        this.numCntrlLst = numCntrlLst;
    }

    public boolean isSubLedgerDisabled() {
        return this.subLedgerDisabled;

    }

    public void setSubLedgerDisabled(boolean subLedgeDisabled) {
        this.subLedgerDisabled = subLedgeDisabled;
    }

    public boolean isSysPostDisabled() {
        return sysPostDisabled;
    }

    public void setSysPostDisabled(boolean sysPostDisabled) {
        this.sysPostDisabled = sysPostDisabled;
    }

 public boolean isEdNumRangeChange() {
  return edNumRangeChange;
 }

 public void setEdNumRangeChange(boolean edNumRangeChange) {
  this.edNumRangeChange = edNumRangeChange;
 }




    public void onNumCntrlChanged(ValueChangeEvent ev){
        logger.log(INFO,"numCntrlChanged called with {0}",((NumberRangeRec)ev.getNewValue()).getNumberControlId());
        /*boolean found = false;
        ListIterator li = this.numCntrlLst.listIterator();
        while(li.hasNext()){
            NumberRangeRec num = (NumberRangeRec)li.next();
            if(num.getNumberControlId() == (Long)ev.getNewValue()){
                found = true;
                this.accountType.setNumberControl(num);

            }
        }
        if(!found){
            GenUtil.addErrorMessage("Could not set the number range");
        }*/
    }

    public void ledgerChanged(ValueChangeEvent evt){
        logger.log(INFO,"ledgerChanged called with {0}",evt.getNewValue());
        LedgerRec ldgr = (LedgerRec)evt.getNewValue();
        logger.log(INFO,"ledger id {0}",ldgr.getId());
        if(ldgr.getId() == -1){
         accountType.setSubLedger(null);
        }
        /*if(ev.getNewValue() == null){
            this.sysPostDisabled = false;
            ListIterator li = this.ledgerSelLst.listIterator();
            SelectItem item = (SelectItem)li.next();
            item.setNoSelectionOption(true);
            li.set(item);
            return;
        }else{
            this.sysPostDisabled = true;
            this.accountType.setSysPost(true);
        }*/
        boolean found = false;
        ListIterator<LedgerRec> li = this.ledgerLst.listIterator();
        while(li.hasNext()){
            LedgerRec led = (LedgerRec)li.next();
            if(led == ldgr){
                found = true;
                accountType.setSubLedger(led);

            }
        }
        if(!found){
            GenUtil.addErrorMessage("Could not set the subledger");
        }
        logger.log(INFO, "accountType ledger: {0}", accountType.getSubLedger());
    }
    

    public void plChanged(ValueChangeEvent ev){
       logger.log(INFO, "plChanged called with ledger id {0}", ev.getNewValue());
       if(ev.getNewValue().toString().equalsIgnoreCase("true") ){
           this.subLedgerDisabled = true;
           
           
       }else{
           this.subLedgerDisabled = false;
       }
       logger.log(INFO, "subLedgerDisabled is now: {0}", subLedgerDisabled);

    }

    public void saveActType(){
        logger.log(INFO, "Save button pressed number range is {0}",accountType.getNumberRange().getNumberControlId());
        try{
         logger.log(INFO, "about to call setupEjb.saveGlAccountType");
            setupEjb.saveAccountType(accountType, getView());
            GenUtil.addInfoMessage("Saved Account type: "+accountType.getName());
            this.accountType = null;
            this.subLedgerDisabled = false;
            this.sysPostDisabled = false;
        }catch(BacException ex){
            GenUtil.addErrorMessage("Could not save account type: "+ex.getLocalizedMessage());
        }

    }

    public void onReset(){
     logger.log(INFO, "onReset called");
     accountTypes = null;
     getAccountTypes();
     updateAccountTypes = null;
     
     
    }
    
    public void onDeleteBtn(){
     logger.log(INFO,"onDeleteBtn");
     ListIterator<AccountTypeRec> li = accountTypes.listIterator();
     boolean found = false;
     while(li.hasNext() && !found){
      AccountTypeRec actTy = li.next();
      if(actTy.getId() == selectedAccountType.getId() ){
       li.remove();
       if(deleteAccountTypes == null){
        deleteAccountTypes = new ArrayList<AccountTypeRec>();
       }
       deleteAccountTypes.add(selectedAccountType);
      }
     }
    }
    
    public void onSaveBtn(){
     logger.log(INFO, "glAccountTypeBean.onSaveBtn called");
     if(updateAccountTypes != null ){
      logger.log(INFO, "Save changes to account types {0}", updateAccountTypes);
      ListIterator<AccountTypeRec> li = updateAccountTypes.listIterator();
      while(li.hasNext()){
       AccountTypeRec rec = li.next();
       rec.setChangedBy(getLoggedInUser());
       rec.setChangedDate(new Date());
       
       rec = setupEjb.updateAccountType(rec, getView());
       
        String msgHdr = this.responseForKey("actType");
        String msg = responseForKey("actTypeChanged");
        MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
        
      }
      
     }
     if(deleteAccountTypes != null){
      logger.log(INFO, "Delete changes to account types {0}", deleteAccountTypes);
      ListIterator<AccountTypeRec> li = deleteAccountTypes.listIterator();
      while(li.hasNext()){
       AccountTypeRec rec = li.next();
       rec.setChangedBy(getLoggedInUser());
       rec.setChangedDate(new Date());
       
       boolean update = setupEjb.deleteAccountType(rec,getView());
       logger.log(INFO,"After call to update result is {0}",update);
       if(update){
        GenUtil.addInfoMessage("Delete rec: {0}"+rec.getName());
        int index = accountTypes.indexOf(rec);  
        logger.log(INFO, "Object index {0}", index);
        accountTypes.remove(index);
       }else{
        GenUtil.addErrorMessage("Delete failed for rec: {0}"+rec.getName());
       }
      }
     }
    }

    public void onEditTblEntry(){
     logger.log(INFO, "onEditTblEntry clicked selection is {0}", selectedAccountType.getNumberRange());
     //selectedAccountType.getNumberControl().getShortDescr()
    }
    public void onUpdateBtn(){
 logger.log(INFO, "onUpdateBtn clicked selection is {0}", selectedAccountType.getName());
 ListIterator<AccountTypeRec> li = accountTypes.listIterator();
 boolean found = false;
 while(li.hasNext() && !found){
  AccountTypeRec actTy = li.next();
  if(actTy.getId() == selectedAccountType.getId() ){
   li.set(selectedAccountType);
   if(updateAccountTypes == null){
    updateAccountTypes = new ArrayList<AccountTypeRec>();
   }
   updateAccountTypes.add(selectedAccountType);
  }
 }
 
}



public void onRowSelect(SelectEvent evt){
 logger.log(INFO, "onRowSelect Event {0}", evt.getObject());
 selectedAccountType = (AccountTypeRec)evt.getObject();
 logger.log(INFO, "onRowSelect selectedAccountType is {0}", selectedAccountType.getName());
 logger.log(INFO,"Number range {0} descr {1}",
         new Object[]{((AccountTypeRec)evt.getObject()).getNumberRange().getShortDescr(),
          selectedAccountType.getNumberRange().getShortDescr()});
 
}

public void onNumRangeEditBtn(){
 logger.log(INFO, "onNumRangeEditBtn called {0}", edNumRangeChange);
 edNumRangeChange = true;
 logger.log(INFO, "Number range list {0}", numCntrlLst);
}    

}
