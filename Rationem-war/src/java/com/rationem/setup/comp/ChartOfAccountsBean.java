/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.comp;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import java.util.ListIterator;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import com.rationem.exception.BacException;
import java.util.Date;
import java.io.Serializable;
import com.rationem.util.GenUtil;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class ChartOfAccountsBean extends BaseBean implements Serializable {
 private static final Logger logger =
  Logger.getLogger("accounts.beans.setup.comp.ChartOfAccountsBean");

 @EJB
 private CompanyManager compMgr;
 
 @EJB
 private SysBuffer sysBuff;
    


 private ChartOfAccountsRec coa;
 private int selectedCoa;
 private int selectPerCntrlId;
 private ArrayList<SelectItem> selCoas;
 private ArrayList<SelectItem> selPerCntrl;
 private ArrayList<ChartOfAccountsRec> charts;
 private List<FisPeriodRuleRec> perCntrlRules;
 private boolean showChartDetail = false;
    /** Creates a new instance of ChartOfAccountsBean */
 public ChartOfAccountsBean() {
  logger.log(INFO, "Initialised ChartOfAccountsBean");
 }

 private ArrayList<SelectItem> buildSelCoas(ArrayList<ChartOfAccountsRec> chartLst){
  logger.log(INFO, "buildSelCoas called with {0}",chartLst.size() );
  ArrayList<SelectItem> items = new ArrayList<SelectItem>();
  SelectItem item =new SelectItem();
  item.setLabel("Please select chart");
  items.add(item);
  ListIterator li = chartLst.listIterator();
  while(li.hasNext()){
   ChartOfAccountsRec ch = (ChartOfAccountsRec)li.next();
   logger.log(INFO, "Build chart with id: {0}", ch.getId());
   item = new SelectItem();
   item.setValue(ch.getId());
   item.setLabel(ch.getName());
   logger.log(INFO, "select item id: {0}", item.getValue());
   logger.log(INFO, "select item va;ue: {0}", item.getDescription());
   items.add(item);
  }

  return items;
 }

    private ArrayList<SelectItem> buildSelPerCntrl(ArrayList<FisPeriodRuleRec> rules){
        logger.log(INFO, "buildSelPerCntrl called with", rules);
        ArrayList<SelectItem> selLst = new ArrayList<SelectItem>();
        ListIterator li = rules.listIterator();
        while(li.hasNext()){
           FisPeriodRuleRec rule = (FisPeriodRuleRec)li.next();
           logger.log(INFO, "Rule with descr {0} added", rule.getPeriodDescr());
           SelectItem item = new SelectItem();
           item.setValue(rule.getId());
           item.setLabel(rule.getPeriodDescr());
           selLst.add(item);

        }
        logger.log(INFO, "Rule select list {0} ", selLst);
        return selLst;
    }

    public ChartOfAccountsRec getCoa() {
        if(coa == null){
            logger.log(INFO, "Need to get new Chart of Accounts");
            coa = compMgr.getChartOfAccount();
        }
        return coa;
    }

    public void setCoa(ChartOfAccountsRec coa) {
        this.coa = coa;
    }

    public int getSelectedCoa() {
        return selectedCoa;
    }

    public void setSelectedCoa(int selectedCoa) {
        this.selectedCoa = selectedCoa;
    }

    public ArrayList<ChartOfAccountsRec> getCharts() {
     if(charts == null || charts.isEmpty()){
      charts = compMgr.getChartsOfAccounts();
      if(!charts.isEmpty()){
       coa = charts.get(0);
      }
     }
     return charts;
    }

    public void setCharts(ArrayList<ChartOfAccountsRec> charts) {
        this.charts = charts;
    }

    public ArrayList<SelectItem> getSelCoas() {
        if(this.selCoas == null){
            try{
            ArrayList<ChartOfAccountsRec> chDB = this.compMgr.getChartsOfAccounts();
            logger.log(INFO, "Web charts {0}", chDB.size());
            ListIterator li = chDB.listIterator();
            while(li.hasNext()){
               ChartOfAccountsRec c = (ChartOfAccountsRec)li.next();
               logger.log(INFO, "Chart id returned {0}", c.getId());
            }
            this.charts = chDB;
            logger.log(INFO, "Web charts {0}", this.charts.size());

            this.selCoas = this.buildSelCoas(chDB);
            logger.log(INFO, "Web charts {0}", this.selCoas.toString());
            }catch(BacException e){
                GenUtil.addErrorMessage(e.getLocalizedMessage());
            }
            logger.log(INFO, "Chart of accounts select {0}", selCoas.size());
        }
        logger.log(INFO, "Chart of Accounts select items is: {0}", selCoas.size());
        return selCoas;
    }

    public void setSelCoas(ArrayList<SelectItem> selCoas) {
        this.selCoas = selCoas;
    }

    public boolean isShowChartDetail() {
        return showChartDetail;
    }

    public void setShowChartDetail(boolean showChartDetail) {
        this.showChartDetail = showChartDetail;
    }

    public List<FisPeriodRuleRec> getPerCntrlRules() {
     if(perCntrlRules == null){
      perCntrlRules = this.sysBuff.getFisPeriodRules();
     }
     return perCntrlRules;
    }

    public void setPerCntrlRules(List<FisPeriodRuleRec> perCntrlRules) {
        this.perCntrlRules = perCntrlRules;
    }

    public ArrayList<SelectItem> getSelPerCntrl() {
        logger.log(INFO, "called getSelPerCntrl");
       // if(selPerCntrl == null){
       //   perCntrlRules = this.compMgr.getPeriodRules();
       //   selPerCntrl = this.buildSelPerCntrl(perCntrlRules);
       // }
        return selPerCntrl;
    }

    public void setSelPerCntrl(ArrayList<SelectItem> selPerCntrl) {
        this.selPerCntrl = selPerCntrl;
    }

    public int getSelectPerCntrlId() {
        return selectPerCntrlId;
    }

    public void setSelectPerCntrlId(int selectPerCntrlId) {
        this.selectPerCntrlId = selectPerCntrlId;
    }



    public void createCoa(){
        logger.log(INFO, "Called create chart of accounts called");
        logger.log(INFO, "coa ref: {0}", coa.getReference());
        logger.log(INFO, "coa perRule {0}", coa.getPeriodRule().getId());
        Date today = new Date();
        coa.setCreatedDate(today);
        coa.setCreatedBy(getLoggedInUser());
        
        try{
         compMgr.newChartOfAccounts(coa, getView());
         MessageUtil.addInfoMessage("coaCreated", "blacResponse");
         PrimeFaces pf = PrimeFaces.current();
         coa = null;
         pf.ajax().update("coaCrFrm");
         
        }catch(BacException e){
         MessageUtil.addErrorMessage("coaCr", "errorText");   
        }


    }

    public void onChartChange(ValueChangeEvent evt){
     logger.log(INFO, "onChartChange new Value {0}", evt.getNewValue());
     coa = (ChartOfAccountsRec)evt.getNewValue();
     logger.log(INFO, "coa now {0}", coa.getName());
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("coaChFrm");
    }
    
    public void onChartUpdateSave(){
     logger.log(INFO, "onChartUpdateSave called with coa {0}", coa);
     coa.setChangedBy(this.getLoggedInUser());
     coa.setChangedDate(new Date());
     coa = this.compMgr.coaUpdate(coa, getLoggedInUser(), getView());
     logger.log(INFO, "after comp mgr update {0}", coa);
    }
    public void updateChartAction(){
        logger.log(INFO, "updateChartAction called");
        ListIterator li = this.perCntrlRules.listIterator();
        boolean found = false;
        FisPeriodRuleRec fisRule;
        while(li.hasNext() && !found){
            fisRule = (FisPeriodRuleRec)li.next();
            if(fisRule.getId() == this.selectPerCntrlId);
            found = true;
            this.coa.setPeriodRule(fisRule);
            logger.log(INFO, "added period rule to chart of accounts");


        }
        try{
            this.compMgr.updateChartOfAccounts(coa);
            GenUtil.addInfoMessage("Updated chart of accounts: "+coa.getReference());
        }catch(BacException e){
            logger.log(INFO, "Company mager error {0}",e.getLocalizedMessage());
            GenUtil.addErrorMessage("Update error: "+e.getLocalizedMessage());
        }

    }
/**
 * Ajax call from web page when user chooses a chart of accounts
 */
    public void selectChartAction(){
        logger.log(INFO,"selectChartAction");
        // set display chart of accounts details
        ListIterator li = charts.listIterator();
        boolean found = false;
        while(li.hasNext() && !found){
            ChartOfAccountsRec chart = (ChartOfAccountsRec)li.next();
            if(chart.getId() == this.selectedCoa){
                found = true;
                this.coa = chart;
                this.showChartDetail = true;
                

            }
        }
        

    }

    

}
