/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.common;

import java.util.ListIterator;
import com.rationem.exception.BacException;
import java.io.Serializable;
import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author Chris
 */
public class ModuleBean extends BaseBean implements Serializable{
    private static final Logger logger =
            Logger.getLogger(ModuleBean.class.getName());

    @EJB
    BasicSetup setup;

    ModuleRec module;
    ModuleRec selectedModule;
    List<ModuleRec> modules;
    List<SelectItem> moduleCodes;


    public ModuleRec getModule() {
        if(module == null){
        module= setup.getModule();
        }
        return module;
    }

    public void setModule(ModuleRec module) {
        this.module = module;
    }

    public ModuleRec getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(ModuleRec selectedModule) {
        logger.log(INFO, "setSelectedModule is {0}", selectedModule);
        this.selectedModule = selectedModule;
    }


    /** Creates a new instance of ModuleBean */
    public ModuleBean() {
    }

    private SelectItem getCode(String s){
        return new SelectItem(s,s,"");
    }
    public List<SelectItem> getModuleCodes(){
        
        if(moduleCodes == null){
            
            moduleCodes = new ArrayList<SelectItem>();
            moduleCodes.add(getCode("Please select"));
            moduleCodes.add(getCode("FI"));
            moduleCodes.add(getCode("SL"));
            moduleCodes.add(getCode("TR"));
            moduleCodes.add(getCode("AM"));
            moduleCodes.add(getCode("MA"));
        }
        
       return moduleCodes;
    }

    public List<ModuleRec> getModules() {
     
     if(modules == null || modules.isEmpty()){
      modules = setup.getAllModules();
      logger.log(INFO, "modules from setup {0}", modules);
     }
     return modules;
    }

    public void setModules(ArrayList<ModuleRec> modules) {
        this.modules = modules;
    }



    public void setModuleCodes(ArrayList<SelectItem> moduleCodes) {
        this.moduleCodes = moduleCodes;
    }

    public void createBtnAction(){
        logger.log(INFO, "createBtnAction");
        try{
        UserRec usr =   getLoggedInUser();
        logger.log(INFO, "createBtnAction getLoggedInUser() {0}", usr);
        module.setCreatedBy(usr);
        module.setCreatedDate(new Date());
        logger.log(INFO, "module created by {0}", module.getCreatedBy());
        module  = setup.createModule(module, usr,getView());
        
        logger.log(INFO, "create module returned: {0}", module.getId());
        if(module.getId() > 0){
         MessageUtil.addInfoMessage("modCr", "blacResponse");
         module = null;
         PrimeFaces pf = PrimeFaces.current();
         pf.ajax().update("createModule");
            
        }else{
         MessageUtil.addErrorMessage("moduleNotCr", "errorText");
            

        }
        }catch(EJBException e){
         String msgHdr = errorForKey("module");
         String msg = errorForKey("moduleNotCr")+e.getLocalizedMessage();
         MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
        }


    }

    public void onCtxMenuDisp(SelectEvent evt){
     logger.log(INFO, "context menu selected {0}", evt.getObject());
     this.selectedModule = (ModuleRec)evt.getObject();
     
    }
    
    public void onModuleEditDlg(){
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("updatePgId");
     pf.executeScript("PF('editModWv').show()");
    }
    
    public void onModuleEditSaveBtn(){
     logger.log(INFO, "onModuleEditSaveBtn called with selected row {0}", this.selectedModule);
     ListIterator<ModuleRec> mods = this.modules.listIterator();
     boolean found = false;
     while(mods.hasNext() && !found){
      ModuleRec currMod = mods.next();
      if(currMod.getId() == selectedModule.getId()){
       selectedModule.setChangedDate(new Date());
       logger.log(INFO, "Loggedin user {0}", getLoggedInUser());
       selectedModule.setChangedById(this.getLoggedInUser());
       this.setup.updateModule(selectedModule,this.getView());
       mods.set(selectedModule);
       MessageUtil.addInfoMessage("modUpdt", "blacResponse");
      }
     }
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("modLst");
     pf.executeScript("PF('editModWv').hide()");
    }
    public void onDeleteModule(){
     logger.log(INFO, "onDeleteModule called with selected row {0}", this.selectedModule);
     PrimeFaces pf = PrimeFaces.current();
     pf.executeScript("PF('modDelWv').show()");
    }
    
    public void onRowSelect(SelectEvent evt){
     logger.log(INFO, "Row select {0}", evt.getObject());
     this.selectedModule = (ModuleRec)evt.getObject();
     logger.log(INFO, "selectedModule {0}", selectedModule);
    }
    
    public void deleteModuleAction(){
        logger.log(INFO, "deleteModuleAction: Selected {0}", this.selectedModule);

        boolean completed = false;
        try{
            setup.deleteModule(this.selectedModule,getView());
            ListIterator<ModuleRec> lIt = modules.listIterator();
            while(lIt.hasNext() && !completed){
                ModuleRec rec = (ModuleRec)lIt.next();
                if(rec.getId() == selectedModule.getId()){
                 this.setup.deleteModule(selectedModule, this.getView());
                  lIt.remove();
                  completed = true;
                  PrimeFaces pf = PrimeFaces.current();
                  pf.ajax().update("modLst");
                  pf.executeScript("PF('modDelWv').hide()");
                }
                if(!completed){
                 MessageUtil.addErrorMessage("modNoDel", "errorText");
                 
                }else{
                 MessageUtil.addInfoMessage("modDel", "blacResponse");
                 logger.log(INFO, "Delete module completed ok num {0}",modules.size());
                }
            }
        }catch(BacException e){
            MessageUtil.addErrorMessage("modNoDel", "errorText");
        }
    }

    public void searchBtnAction(){
        logger.log(INFO,"searchBtnAction");
         modules = this.setup.getModulesByCriteria(module);
         logger.log(INFO, "Modules returned Web layer: {0}", modules);
    }

    



}
