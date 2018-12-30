/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.comp;

import org.primefaces.event.SelectEvent;
import javax.faces.event.ActionEvent;
import com.rationem.entity.config.company.Ledger;
import java.util.ListIterator;
import com.rationem.ejbBean.config.common.BasicSetup;
import java.util.ArrayList;
import javax.faces.event.AjaxBehaviorEvent;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.setup.common.DefaultLoadBean;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;


import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Chris
 */
public class LedgerBean extends BaseBean {
    private static final Logger logger =
            Logger.getLogger(LedgerBean.class.getName());

    @EJB
    private BasicSetup setup;
    
    @EJB
    private SysBuffer sysBuff;

    private LedgerRec ledger;
    private LedgerRec editLedger;
    private Long selectedID;
    public List<LedgerRec> ledgers;

    /** Creates a new instance of LedgerBean */
    public LedgerBean() {
        logger.log(INFO, "ledger Bean constructor");
    }

    @PostConstruct
    private void init(){
     ledgers = sysBuff.getLedgers();
     
     logger.log(INFO, "init ledgers {0}", ledgers);
    }
    
    public LedgerRec getLedger() {
        
        return ledger;
    }

    public void setLedger(LedgerRec ledger) {
        logger.log(INFO, "setLedger called with {0}", ledger);
        this.ledger = ledger;
    }

    public LedgerRec getEditLedger() {
     logger.log(INFO, "getEditLedger {0}", editLedger);
     if(editLedger == null){
      editLedger = new LedgerRec();
     }
        return editLedger;
    }

    public void setEditLedger(LedgerRec editLedger) {
     logger.log(INFO, "setEditLedger {0}", editLedger);
        this.editLedger = editLedger;
    }

    public Long getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(Long selectedID) {
        this.selectedID = selectedID;
    }



    public void onLedgerSelect(SelectEvent e){
        logger.log(INFO, "onLedgerSelect called with: {0}", e);
        this.editLedger = ((LedgerRec)e.getObject());
     logger.log(INFO, "editLedger {0}",editLedger);   
    }


public void onUpdtFrmDisp(){
 logger.log(INFO, "onUpdtFrmDisp called Selected {0}",this.editLedger);
 RequestContext rCtx = RequestContext.getCurrentInstance();
 logger.log(INFO, "editLedger name {0}", editLedger.getName());
 rCtx.update("dialogContent");
 rCtx.execute("PF('ledgerDgWv').show()");
}
public void onEditRow(SelectEvent e) {
    logger.log(INFO,"onEditRow with {0}",e);
}

public List<LedgerRec> getLedgers() {
logger.log(INFO, "get ledgers: {0}",this.ledgers);
if(ledgers == null || ledgers.isEmpty() ){
 ledgers =  sysBuff.getLedgers();
 logger.log(INFO, "Ledgers returned {0}", ledgers);
}
return ledgers;
}

    public void setLedgers(ArrayList<LedgerRec> ledgers) {
        logger.log(INFO, "setLedgers called with {0}", ledgers);
        this.ledgers = ledgers;
    }

    public BasicSetup getSetup() {
        return setup;
    }

    public void setSetup(BasicSetup setup) {
        this.setup = setup;
    }



    public void validateInput(FacesContext context, UIComponent component, Object value)
       throws ValidatorException {
        String id = component.getId();
        addError("test message");

        

      }
    public void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                msg,msg));
    }

    public String submit(){
        logger.log(INFO, "subit called");
        return "none";
    }

    public void saveNewLedger(){
        logger.log(INFO, "submitClick called with ledger {0}",ledger);
        ledger.setCreatedBy(this.getLoggedInUser());
        ledger.setCreatedDate(new Date());
        logger.log(INFO, "Created by date {0}", ledger.getCreatedBy());
        ledger = setup.insertLedger(ledger, getView());
        if(ledger.getId() != null){
         MessageUtil.addInfoMessage("ledgerCr", "blacResponse");
         ledger = null;
         RequestContext rCtx = RequestContext.getCurrentInstance();
         rCtx.update("ledCrPG");
        }else{
         MessageUtil.addErrorMessage("ledCr", "errorText");
        }
        
        

    }
    
    

    public void editLedgerAction(ActionEvent e){
        logger.log(INFO, "editLedger called with: {0}",e);
        logger.log(INFO,"Edit ledger  name is: {0}",this.editLedger.getName());
    }

    public void deleteLedgerAction(ActionEvent e){
        logger.log(INFO, "deleteLedger called with: {0}",e);
        logger.log(INFO,"Delete ledger  name is: {0}",this.editLedger.getName());
    }

    public void ajaxFieldCheck(AjaxBehaviorEvent e){
        logger.log(INFO, "ajaxFieldCheck called with: {0}", e);
        logger.log(INFO,"Calling object id: {0}",e.getComponent().getId());
        UIComponent comp = e.getComponent();
        String id = e.getComponent().getId();
        logger.log(INFO,"Ledgers: {0}",this.ledgers);
        logger.log(INFO,"Ledgers: {0}",this.ledger.getName());
        if(this.ledgers == null){
            logger.log(INFO, "need to get ledgers");
            this.ledgers = setup.getLedgers();
        }
        logger.log(INFO,"Ledgers: {0}",this.ledgers);
        if(this.ledgers.isEmpty()){
            return;
        }
        ListIterator it = ledgers.listIterator();
        boolean found = false;
        while(it.hasNext() && !found){
          LedgerRec l = (LedgerRec)it.next();
          if(id.equalsIgnoreCase("ldrName")){
           if(ledger.getName().equalsIgnoreCase(l.getName())){
            found = true;
            MessageUtil.addErrorMessage("ledName", "ledNameDup", "errorText");
            //this.addError("You cannot reuse the same ledger name");
            this.ledger.setName(null);
            RequestContext rCtx = RequestContext.getCurrentInstance();
            rCtx.update("ledCrPG");
           }
          }
        }
    }

    public void updateLedger(){
        logger.log(INFO, "updateLedger called with: {0}"   ,editLedger );
        logger.log(INFO, "edit ledger id: {0}", this.editLedger.getId());
        editLedger.setChangedBy(this.getLoggedInUser());
        editLedger.setChangedDate(new Date());
        logger.log(INFO, "EditLedger descr {0}", editLedger.getDescr());
        logger.log(INFO, "EditLedger descr post update {0}", editLedger.getDescr());
        editLedger = setup.updateLedger(editLedger,getView());
        ledgers.set(0, editLedger);
        RequestContext rCtx = RequestContext.getCurrentInstance();
        rCtx.update("ldgrlstId");
        rCtx.execute("PF('ledgerDgWv').hide()");

        logger.log(INFO, "edit ledger id {0}",this.editLedger.getId());

    }

    public void deletLedger(ActionEvent e){
        logger.log(INFO, "deletLedger called with {0}", e);
    }
    public void searchLedgers(ActionEvent e){
        logger.log(INFO,"searchLedgers");
        String name = new String();
        String description = new String();
        String code = new String();
        if(ledger.getName() != null){
            name = ledger.getName() ;
        }
        name = name + '%';
        if(ledger.getDescr() != null){
            description = ledger.getDescr();
        }
        description = description + '%';

        if(ledger.getCode() != null){
            code = ledger.getCode();
        }
        code = code + '%';

        this.ledgers = this.setup.getLedgersByCriteria(name, description, code);
        logger.log(INFO, "Number of ledgers returned: {0}", this.ledgers.size());
        System.out.println("searchLedgers updated ledgers to ");
        
    }

    public String searchBtn(){
        logger.log(INFO, "Search Btn");
        return new String("none");
    }

    public void searchClick(){
        logger.log(INFO, "searchClick called");
    }



}
