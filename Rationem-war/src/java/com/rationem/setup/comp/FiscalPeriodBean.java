/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.comp;

import javax.ejb.TransactionRolledbackLocalException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIInput; 
import javax.faces.component.UIForm;
import javax.faces.event.ComponentSystemEvent;

import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.busRec.config.company.CalendarRuleBaseRec;
import com.rationem.util.GenUtil;
import com.rationem.exception.BacException;
import com.rationem.ejbBean.common.SysBuffer;
import java.io.Serializable;
import java.util.ListIterator;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.CalendarRuleFixedDateRec;
import com.rationem.busRec.config.company.CalendarRuleFlexPerRec;
import com.rationem.busRec.config.company.CalendarRuleFlexYearRec;
import java.util.ArrayList;
import javax.faces.model.SelectItem;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.UserSessionBean;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import java.util.logging.Logger;



import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;


/**
 *
 * @author Chris
 */
public class FiscalPeriodBean extends BaseBean implements Serializable {
    private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.comp.FiscalPeriodBean");

    @EJB
    private CompanyManager compMgr;

    
    @EJB
    private SysBuffer sysBuffer;

    private FisPeriodRuleRec fisPerRule;

    private CalendarRuleBaseRec fisCal;
    private CalendarRuleMonthRec fisCalMth;
    private CalendarRuleFixedDateRec fisCalFixedDate;
    private CalendarRuleFlexYearRec fisCalFlexYr;
    private CalendarRuleFlexYearRec fisCalFlexYrAdd;
    private List<CalendarRuleFlexPerRec> fisCalFlexPeriods;
    private CalendarRuleFlexPerRec perFlexPeriodPreEdit;
/*    private CalendarRulePeriodLengthRec fisCalPeriodLength; */

    private int uomId;

    private int selectedFisRuleId;
    private int selectedCalRuleId;
    private int currentCalbasis;
    private boolean createdPerRuleOk;
    private boolean updatePerRuleOk;
    private ArrayList<SelectItem> selectRule;
    private ArrayList<SelectItem> selectCalRule;
    private ArrayList<CalendarRuleBaseRec> calendarRules;
    
    private String detailPanelCr;
    private String detailPanelUpdt;
    
    
    



    public static final int CALBASIS = 1;
    public static final int FIX_DAY = 2;
    public static final int PER_LEN = 3;
    public static final int ANN_PER = 4;

private ArrayList<SelectItem> buildCalSelectList(ArrayList<CalendarRuleBaseRec> l){
    ArrayList<SelectItem> selList = new ArrayList<SelectItem>();
    ListIterator li = l.listIterator();
    while(li.hasNext()){
     CalendarRuleBaseRec rec = (CalendarRuleBaseRec)li.next();
     SelectItem item = new SelectItem();
     item.setValue(rec.getId());
     item.setLabel(rec.getDescription());
     selList.add(item);
    }
    logger.log(INFO, "Cal select list {0}", selList);
    return selList;
}

    /** Creates a new instance of FiscalPeriodBean */
    public FiscalPeriodBean() {
        logger.log(INFO, "Created FiscalPeriodBean ");

    }

    public ArrayList<CalendarRuleBaseRec> getCalendarRules() {
        return calendarRules;
    }

    public void setCalendarRules(ArrayList<CalendarRuleBaseRec> calendarRules) {
        this.calendarRules = calendarRules;
    }

 public boolean isCreatedPerRuleOk() {
  return createdPerRuleOk;
 }

 public void setCreatedPerRuleOk(boolean createdPerRuleOk) {
  this.createdPerRuleOk = createdPerRuleOk;
 }



    public FisPeriodRuleRec getFisPerRule() {
        if(fisPerRule == null){
            fisPerRule = compMgr.getFiscalPeriodRule();

        }
        return fisPerRule;
    }

    public void setFisPerRule(FisPeriodRuleRec fisPerRule) {
        this.fisPerRule = fisPerRule;
    }

 public CalendarRuleFixedDateRec getFisCalFixedDate() {
  
  return fisCalFixedDate;
 }

 public void setFisCalFixedDate(CalendarRuleFixedDateRec fisCalFixedDate) {
  this.fisCalFixedDate = fisCalFixedDate;
 }

 public CalendarRuleFlexYearRec getFisCalFlexYr() {
  return fisCalFlexYr;
 }

 public void setFisCalFlexYr(CalendarRuleFlexYearRec fisCalFlexYr) {
  this.fisCalFlexYr = fisCalFlexYr;
 }

 public List<CalendarRuleFlexPerRec> getFisCalFlexPeriods() {
  return fisCalFlexPeriods;
 }

 public CalendarRuleFlexYearRec getFisCalFlexYrAdd() {
  return fisCalFlexYrAdd;
 }

 public void setFisCalFlexYrAdd(CalendarRuleFlexYearRec fisCalFlexYrAdd) {
  this.fisCalFlexYrAdd = fisCalFlexYrAdd;
 }

 public CalendarRuleFlexPerRec getPerFlexPeriodPreEdit() {
  return perFlexPeriodPreEdit;
 }

 public void setPerFlexPeriodPreEdit(CalendarRuleFlexPerRec perFlexPeriodPreEdit) {
  this.perFlexPeriodPreEdit = perFlexPeriodPreEdit;
 }

 public void setFisCalFlexPeriods(List<CalendarRuleFlexPerRec> fisCalFlexPeriods) {
  this.fisCalFlexPeriods = fisCalFlexPeriods;
 }



    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
    }

    public int getCurrentCalbasis() {
        logger.log(INFO, "getCurrentCalbasis");
        return currentCalbasis;
    }

    public void setCurrentCalbasis(int currentCalbasis) {
        logger.log(INFO, "setCurrentCalbasis {0}", currentCalbasis);
        this.currentCalbasis = currentCalbasis;
    }

 public String getDetailPanelCr() {
  return detailPanelCr;
 }

 public void setDetailPanelCr(String detailPanelCr) {
  this.detailPanelCr = detailPanelCr;
 }

 public String getDetailPanelUpdt() {
  return detailPanelUpdt;
 }

 public void setDetailPanelUpdt(String detailPanelUpdt) {
  this.detailPanelUpdt = detailPanelUpdt;
 }





    public List<SelectItem> getUomSelItems(){
        logger.log(INFO, "getUomSelItems");
        SelectItem item;
        ArrayList<SelectItem> l = new ArrayList<SelectItem>();
        item = new SelectItem("0","choose Unit of measure");
        l.add(item);
        UomRec uom;
        List<UomRec> uoms = this.sysBuffer.getUoms();
        
        if(uoms == null){
            return l;
        }
        logger.log(INFO, "Number of UOMS returned to bean: {0}", uoms.size());
        ListIterator<UomRec> it = uoms.listIterator();
        while(it.hasNext()){
            uom = (UomRec)it.next();
            item = new SelectItem(uom.getId(),uom.getName());
            l.add(item);
        }

        return l;
    }

    public String onFiscPerUpdateWfStep(FlowEvent evt){
     String nextStep = evt.getNewStep();
     logger.log(INFO, "onFiscPerUpdateWfStep curr step {0} next step {1}", 
             new Object[]{evt.getOldStep(),evt.getNewStep()});
     detailPanelUpdt = this.formTextForKey("fisPerCntrl_rulePnl");
     logger.log(INFO, "fisPerRule calRule {0}", fisPerRule.getCalendarRule());
     logger.log(INFO, "getCalBasisOption() {0}",fisPerRule.getCalBasisOption());
     if(fisPerRule.getCalendarRule() == null){
       FisPeriodRuleRec calRec = sysBuffer.getFisPeriodRuleCal(fisPerRule.getId());
       fisPerRule.setCalendarRule(calRec.getCalendarRule());
       logger.log(INFO, "fisPerRule after call to sysBuffer calRule {0}", fisPerRule);
       
     }
     if(fisPerRule.getCalBasisOption() == 1){
      detailPanelUpdt =  detailPanelUpdt + formTextForKey("fisPerCntrl_calPnl");
      logger.log(INFO, "Calendar setting {0}", fisPerRule.getCalendarRule());
      logger.log(INFO, "Month num {0}", ((CalendarRuleMonthRec)fisPerRule.getCalendarRule()).getStartMonthNumber());
      fisCalMth = (CalendarRuleMonthRec)fisPerRule.getCalendarRule();
     }else if (fisPerRule.getCalBasisOption() == 2){
      detailPanelUpdt =  detailPanelUpdt + formTextForKey("fisPerCntrl_DayOfMnthPnl");
     }else if (fisPerRule.getCalBasisOption() == 3){
      detailPanelUpdt =  detailPanelUpdt + formTextForKey("fisPerCntrl_flexPnl");
      logger.log(INFO, "Period cal rule {0}", fisPerRule.getCalendarRule());
      logger.log(INFO, "Period cal rule years {0}", fisPerRule.getCalendarRule().getCalRuleFlexYears());
      fisCalFlexYr = fisPerRule.getCalendarRule().getCalRuleFlexYears().get(0);
      
     }
    
     return nextStep;
    }
    public String onFiscPerCreateWfStep(FlowEvent evt){
     logger.log(INFO, "From step {0} to step {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
     logger.log(INFO, "period type {0}", fisPerRule.getCalBasisOption());
     this.detailPanelCr = this.formTextForKey("fisPerCntrl_rulePnl");
     Date currDate = new Date();
     UserSessionBean usrBuff =this.getUsrBuff();
     Locale loc = usrBuff.getLoc();
     GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(loc);
     cal.setTime(currDate);
     if(evt.getOldStep().equalsIgnoreCase("basicId") && evt.getNewStep().equalsIgnoreCase("ruleId")){
      if(fisPerRule.getNumPeriods() <1){
       MessageUtil.addErrorMessage("fiscalPeriods", "errorText");
       return evt.getOldStep();
      }
     if(fisPerRule.getCalBasisOption() == 1){
      fisCalMth = new CalendarRuleMonthRec();
      detailPanelCr = detailPanelCr + formTextForKey("fisPerCntrl_calPnl");
     }else if(fisPerRule.getCalBasisOption() == 2){
      fisCalFixedDate = new CalendarRuleFixedDateRec();
      fisCalFixedDate.setCreatedBy(this.getLoggedInUser());
      fisCalFixedDate.setCreatedOn(new Date());
      detailPanelCr = detailPanelCr + formTextForKey("fisPerCntrl_DayOfMnthPnl");
     }else if(fisPerRule.getCalBasisOption() == 3){
      
      detailPanelCr = detailPanelCr + formTextForKey("fisPerCntrl_flexPnl");
      fisCalFlexYr = new CalendarRuleFlexYearRec();
      fisCalFlexYr.setCalYear(cal.get(Calendar.YEAR));
      this.fisCalFlexPeriods = new ArrayList<CalendarRuleFlexPerRec>();
      int pers = fisPerRule.getNumPeriods();
      int audPers = fisPerRule.getNumAuditPeriods();
      int perNum = 0;
      
      for(int i = 0; i < pers; i++){
       CalendarRuleFlexPerRec per = new CalendarRuleFlexPerRec();
       cal.set(Calendar.MONTH, i);
       cal.set(Calendar.DAY_OF_MONTH, 1);
       per.setStartPeriod(cal.getTime());
       int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
       cal.set(Calendar.DAY_OF_MONTH, lastDay);
       per.setEndPeriod(cal.getTime());
       perNum++;
       per.setCalPeriod(perNum);
       fisCalFlexPeriods.add(per);
      }
      
      for(int i = 0; i < audPers; i++ ){
       CalendarRuleFlexPerRec per = new CalendarRuleFlexPerRec();
       perNum++;
       per.setAuditPer(true);
       per.setCalPeriod(perNum);
       fisCalFlexPeriods.add(per);
      }
      fisCalFlexYr.setFlexPeriods(fisCalFlexPeriods);
      fisPerRule.setCalendarRule(fisCal);
      if(fisCal == null){
       fisCal = new CalendarRuleBaseRec();
      }
      
     }
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("rulPnlId");
     }else if(evt.getOldStep().equalsIgnoreCase("ruleId") && evt.getNewStep().equalsIgnoreCase("summaryId")){
      if(fisPerRule.getCalBasisOption() == 2){
       if(fisCalFixedDate.getDayOfMonth() == 0){
        //MessageUtil.addInfoMessage("fiscalDayOfMnth", "errorText");
        return evt.getOldStep();
       }
      }
      if(fisPerRule.getCalBasisOption() == 3 ){
       CalendarRuleBaseRec calBase = new CalendarRuleBaseRec();
       calBase.setCreatedBy(this.getLoggedInUser());
       calBase.setCreatedOn(currDate);
   
       List<CalendarRuleFlexYearRec> flexYrs = calBase.getCalRuleFlexYears();
       if(flexYrs == null){
        flexYrs = new ArrayList<CalendarRuleFlexYearRec>();
       }
       flexYrs.add(fisCalFlexYr);
       calBase.setCalRuleFlexYears(flexYrs);
       fisPerRule.setCalendarRule(calBase);
      }
     }
     String retStep = evt.getNewStep();
     
     return retStep;
    }
    
    public void onFlexPerEditInit(RowEditEvent evt){
     logger.log(INFO, "onFlexPerEditInit value {0}", evt.getObject());
     perFlexPeriodPreEdit = new CalendarRuleFlexPerRec();
     perFlexPeriodPreEdit.setCalPeriod(((CalendarRuleFlexPerRec)evt.getObject()).getCalPeriod());
     perFlexPeriodPreEdit.setStartPeriod(((CalendarRuleFlexPerRec)evt.getObject()).getStartPeriod());
     perFlexPeriodPreEdit.setEndPeriod(((CalendarRuleFlexPerRec)evt.getObject()).getEndPeriod()); 
     
    }
    public void onFlexPerAddYr(){
     fisCalFlexYrAdd = new CalendarRuleFlexYearRec();
     PrimeFaces pf = PrimeFaces.current();
     pf.executeScript("PF('addYrDlgWv').show()");
     logger.log(INFO, "onFlexPerAddYr After show dlg");
    }

    
    public void onAddFlexYearSave(){
     logger.log(INFO, "onAddFlexYearSave called");
     logger.log(INFO, "fisCalFlexYrAdd {0} periods {1}", new Object[]{fisCalFlexYrAdd,fisCalFlexYrAdd.getFlexPeriods().size()});
     logger.log(INFO, "origin flex years {0}", fisPerRule.getCalendarRule().getCalRuleFlexYears().size());
     List<CalendarRuleFlexYearRec> years = fisPerRule.getCalendarRule().getCalRuleFlexYears();
     years.add(fisCalFlexYrAdd);
     logger.log(INFO, "origin flex years {0}", fisPerRule.getCalendarRule().getCalRuleFlexYears().size());
     fisPerRule.getCalendarRule().setCalRuleFlexYears(years);
     fisPerRule = sysBuffer.fisPeriodRuleCalUpdateYears(fisPerRule, this.getLoggedInUser(),getView());
     PrimeFaces pf = PrimeFaces.current();
     pf.executeScript("PF('addYrDlgWv').hide()");
     pf.ajax().update("flexYr");
     
    }
    public void onFlexPerEdit(RowEditEvent evt){
     logger.log(INFO, "onFlexPerEdit called with {0}", evt.getObject());
     CalendarRuleFlexPerRec currFlexPer = (CalendarRuleFlexPerRec)evt.getObject();
     
     if(currFlexPer.getEndPeriod().before(currFlexPer.getStartPeriod())){
      logger.log(INFO, "Invalid start end periods start {0} end {1}", 
              new Object[]{currFlexPer.getStartPeriod(),currFlexPer.getEndPeriod()});
      MessageUtil.addErrorMessage("fiscalPerDates", "errorText");
      ListIterator<CalendarRuleFlexPerRec> persLi = this.fisCalFlexYr.getFlexPeriods().listIterator();
      boolean found = false;
      while(persLi.hasNext() && !found){
       CalendarRuleFlexPerRec testPer = persLi.next();
       if(testPer.getCalPeriod() == perFlexPeriodPreEdit.getCalPeriod()){
        
        logger.log(INFO, "perFlexPeriodPreEdit start end periods start {0} end {1}", 
              new Object[]{perFlexPeriodPreEdit.getStartPeriod(),perFlexPeriodPreEdit.getEndPeriod()});
        testPer.setStartPeriod(perFlexPeriodPreEdit.getStartPeriod());
        testPer.setEndPeriod(perFlexPeriodPreEdit.getEndPeriod());
        logger.log(INFO, "testPer start end periods start {0} end {1}", 
              new Object[]{testPer.getStartPeriod(),testPer.getEndPeriod()});
        persLi.set(testPer);
       }
      }
      
     }
    }
    
    public String onPeriodRuleUpdateAction(){
     if(updatePerRuleOk){
      return  "home";
     }else{
      return null;
     }
    }
    public void onSavePeriodRule(){
     logger.log(INFO, "updatePeriodRuleAction called with {0}",this.fisPerRule);
     fisPerRule.setChangeBy(this.getLoggedInUser());
     fisPerRule.setChangeDate(new Date());
     fisPerRule = sysBuffer.fisPeriodRuleCalUpdate(fisPerRule, this.getLoggedInUser(), getView());
     selectRule = null;
     String msg = this.responseForKey("fiscPerUpdt") + fisPerRule.getPeriodRule();
     String msgHdr = this.responseForKey("fiscPerRuleHdr");
     updatePerRuleOk = true;
     /*PrimeFaces pf = PrimeFaces.current();
     pf.executeScript("PF('wizWv').back()");
     pf.ajax().update("fisPerRuleUpdtFrm");*/
     MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);   
    }
    public void onStartMonthChange(ValueChangeEvent evt){
     logger.log(INFO, "onStartMonthChange called with {0}", evt.getNewValue());
     logger.log(INFO, "fisCalMth start mont {0}", fisCalMth.getStartMonthNumber());
     fisCalMth.setStartMonthNumber((Integer)evt.getNewValue());
     logger.log(INFO, "fisCalMth start month after set {0}", fisCalMth.getStartMonthNumber());
    }
    
    public void onFlexPerYearChange(ValueChangeEvent evt){
     logger.log(INFO, "onFlexPerYearChange new year {0}", evt.getNewValue());
     CalendarRuleFlexYearRec newYear = (CalendarRuleFlexYearRec)evt.getNewValue();
     CalendarRuleFlexYearRec oldYear = (CalendarRuleFlexYearRec)evt.getOldValue();
     List<CalendarRuleFlexYearRec> currYrs = fisPerRule.getCalendarRule().getCalRuleFlexYears();
     ListIterator<CalendarRuleFlexYearRec> currYrLi = currYrs.listIterator();
     boolean foundYr = false;
     while(currYrLi.hasNext() && !foundYr){
      CalendarRuleFlexYearRec currYr = currYrLi.next();
      if(currYr.getId() == oldYear.getId()){
       currYrLi.set(oldYear);
       foundYr = true;
      }
     }
     if(foundYr){
      fisPerRule.getCalendarRule().setCalRuleFlexYears(currYrs);
     }
     logger.log(INFO, "newYear periods {0}", newYear.getFlexPeriods());
     fisCalFlexYr = newYear;
     PrimeFaces pf = PrimeFaces.current();
     pf.ajax().update("flexPersTblId");
      
     
     
     //fisCalFlexYr
    }
    public int getSelectedFisRuleId() {
        return selectedFisRuleId;
    }

    public void setSelectedFisRuleId(int selectedFisRuleId) {
        logger.log(INFO, "setSelectedFisRuleId called with: {0}", selectedFisRuleId);
        this.selectedFisRuleId = selectedFisRuleId;
    }

    public int getSelectedCalRuleId() {
        return selectedCalRuleId;
    }

    public void setSelectedCalRuleId(int selectedCalRuleId) {
        this.selectedCalRuleId = selectedCalRuleId;
    }



    public ArrayList<SelectItem> getSelectRule() {
     if(selectRule != null){
      return selectRule;
     }
     ArrayList<FisPeriodRuleRec> rules = sysBuffer.getFisPeriodRules();
     if(rules == null || rules.isEmpty()){
      MessageUtil.addInfoMessage("fisPerRuleNone", "validationText");
     }
     selectRule = new ArrayList<SelectItem>();
     SelectItem item = new SelectItem();
     selectRule.add(item);
     item.setNoSelectionOption(true);
     item.setLabel("Please select a rule");
     ListIterator it = rules.listIterator();
      while(it.hasNext()){
       FisPeriodRuleRec rule = (FisPeriodRuleRec)it.next();
       item = new SelectItem();
       item.setValue(rule.getId());
       item.setLabel(rule.getPeriodDescr());
       selectRule.add(item);
      }

      logger.log(INFO, "Select items returned {0}", selectRule.size());
      return selectRule;
    }

    public void setSelectRule(ArrayList<SelectItem> selectRule) {
        this.selectRule = selectRule;
    }
/**
 * Select array for calendar rules.
 * If the set of rules has already been populated then return this don't go back to DB
 * @return
 */
 /*
    public ArrayList<SelectItem> getSelectCalRule() {
        logger.log(INFO, "getSelectCalRule current calendar rule is: ", this.currentCalbasis);
        if(selectCalRule != null){
            this.compMgr.getCalRuleByTypeList(this.currentCalbasis);
        }
        logger.log(INFO, "Calendar rule list {0}", selectCalRule);
        return selectCalRule;
    }

public ArrayList<SelectItem> getSelectCalRuleByRuleId(int ruleId) {
        logger.log(INFO, "getSelectCalRuleByRuleId current calendar rule is: ", ruleId);
        if(selectCalRule != null){

         ArrayList<CalendarRuleBaseRec> list = this.compMgr.getCalRuleByTypeList(ruleId);
         logger.log(INFO, "Calendar list returned", list);
        }
        logger.log(INFO, "Calendar rule list {0}", selectCalRule);
        return selectCalRule;
    }
    */
/**
 * Return and a select list for calendars based on the calendar selected
 * @param selectCalRule
 */
 /*
    public void setSelectCalRule(ArrayList<SelectItem> selectCalRule) {
        logger.log(INFO, "setSelectCalRule");
        if(selectCalRule == null){
            //need to calendar info from sysBuffer
            ArrayList lst = sysBuffer.getCalendarRuleList(this.fisPerRule.getCalBasisOption());
            logger.log(INFO, "Calendar Rules returned");

        }
        this.selectCalRule = selectCalRule;
    }

*/



    public String addFiscalPeriodAction(){
     if(this.createdPerRuleOk){
      return "home";
     }else{
      return null;
     }
    }
    public void addFiscalPeriodRule(){
        logger.log(INFO, "saveBasicPeriodControl");
        fisPerRule.setCreateBy(getLoggedInUser());
        fisPerRule.setCreateDate(new Date());
        if(fisPerRule.getCalBasisOption() == 1){
         fisPerRule.setCalendarMonthBasis(true);
         fisPerRule.setAnnualDateScheduleBasis(false);
         fisPerRule.setFixedDayOfMonthBasis(false);
         fisPerRule.setFixedlen(false);
         fisCalMth.setCreatedBy(getLoggedInUser());
         fisCalMth.setCreatedOn(new Date());
         logger.log(INFO, "fisPerRule {0}", fisCalMth.getStartMonthNumber());
         fisPerRule.setCalendarRule(fisCalMth);
         
            
        }else if(this.fisPerRule.getCalBasisOption() == 2){
         fisPerRule.setCalendarMonthBasis(false);
         fisPerRule.setAnnualDateScheduleBasis(false);
         fisPerRule.setFixedDayOfMonthBasis(true);
         fisPerRule.setFixedlen(false);
         fisPerRule.setCalendarRule(fisCalFixedDate);
        }else if(this.fisPerRule.getCalBasisOption() == 3){
         fisPerRule.setCalendarMonthBasis(false);
         fisPerRule.setAnnualDateScheduleBasis(true);
         fisPerRule.setFixedDayOfMonthBasis(false);
         fisPerRule.setFixedlen(false);
         UserRec crUsr = this.getLoggedInUser();
         Date crDate = new Date();
         CalendarRuleBaseRec calRule = fisPerRule.getCalendarRule();
         List<CalendarRuleFlexYearRec> calYears = calRule.getCalRuleFlexYears();
         ListIterator<CalendarRuleFlexYearRec> calYrLi = calYears.listIterator();
         while(calYrLi.hasNext()){
          CalendarRuleFlexYearRec yrRec = calYrLi.next();
          List<CalendarRuleFlexPerRec> periods = yrRec.getFlexPeriods();
          ListIterator<CalendarRuleFlexPerRec> periodLi = periods.listIterator();
          while(periodLi.hasNext()){
           CalendarRuleFlexPerRec perRec = periodLi.next();
           perRec.setCreatedBy(crUsr);
           perRec.setCreatedOn(crDate);
           periodLi.set(perRec);
          }
          yrRec.setFlexPeriods(periods);
          yrRec.setCreatedBy(crUsr);
          yrRec.setCreatedOn(crDate);
          calYrLi.set(yrRec);
         }
         calRule.setCreatedBy(crUsr);
         calRule.setCreatedOn(crDate);
         fisPerRule.setCalendarRule(calRule);
        }
        
        try{
            logger.log(INFO, "Bean saveBasicPeriodAction ", this.fisPerRule.isCalendarMonthBasis());
            fisPerRule.setCreateBy(this.getLoggedInUser());
            fisPerRule.setCreateDate(fisPerRule.getCreateDate());
            compMgr.saveFisPeriod(fisPerRule, getView());
            String msgHdr = this.responseForKey("fiscPerRuleHdr");
            String msg = responseForKey("fiscPerRuleCr")+fisPerRule.getPeriodRule();
            MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
            fisPerRule = null;
            createdPerRuleOk = true;
            PrimeFaces pf = PrimeFaces.current();
            
            pf.ajax().update("fisPCntrlFrm");
        }catch(BacException e){
            logger.log(INFO, "Bean saveBasicPeriodAction error");
            String msgHdr = this.responseForKey("fiscPerRuleHdr");
            String msg = errorForKey("fisPerRuleCr") +fisPerRule.getPeriodRule();
            MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
           
        }
    }

    
    public void saveCalendarAction(){
       logger.log(INFO, "saveCalendar");
       if(fisPerRule.getCalBasisOption() == 1){
           // for calendar must have a start month number
           logger.log(INFO, "start month {0}", fisCalMth.getStartMonthNumber());
           if(fisCalMth.getStartMonthNumber() == 0
                   || fisCalMth.getStartMonthNumber() > 12  ){
               GenUtil.addErrorMessage("Start must be between 01 and 12 ");
           }else{
               fisCalMth.setReference(this.getFisCal().getReference());
               fisCalMth.setDescription(getFisCal().getDescription());
               try{
                this.compMgr.createFisCal(fisCalMth, "cal", getView());
                GenUtil.addInfoMessage("Calendar saved");
               }catch(BacException e){
                   GenUtil.addErrorMessage("Could not save calendar");
               }

           }

       }
    }
    

    public CalendarRuleBaseRec getFisCal() {
        if(fisCal == null){
            fisCal = this.compMgr.getFiscalCalendar();
        }
        return fisCal;
    }

    public void setFisCal(CalendarRuleBaseRec fisCal) {
        this.fisCal = fisCal;
    }

    public CalendarRuleMonthRec getFisCalMth() {
        // get Month fiscal Calendar
        if(fisCalMth == null){
            logger.log(INFO, "fisCalMth create");
           fisCalMth = this.compMgr.getFiscalCalendarMonth();
        }
        return fisCalMth;
    }

    public void setFisCalMth(CalendarRuleMonthRec fisCalMth) {
        this.fisCalMth = fisCalMth;
    }

   
    public void validatePeriodRule(ComponentSystemEvent e){
        logger.log(INFO, "validatePeriodRule called");
        UIForm form = (UIForm)e.getComponent();
        UIInput PeriodRuleType = (UIInput)form.findComponent("caLBasisOpt");
        logger.log(INFO, "period rule type is", PeriodRuleType.getValue());
        if(PeriodRuleType.getValue().equals("1")) {
            logger.log(INFO, "period rule type is", PeriodRuleType.getValue());
        }
    }


    public void periodRuleChangeListener(ValueChangeEvent evt){
     logger.log(INFO,"periodRuleChangeListener evt {0}",evt.getNewValue());
     // get the selected period rule by id
     Long selectedId = Long.valueOf(String.valueOf(evt.getNewValue()));
     try{
      //fisPerRule = compMgr.getPeriodRuleById(selectedId);
      fisPerRule = this.sysBuffer.getFisPeriodRule(selectedId);
      logger.log(INFO, "After Call to get fiscPerRule {0}", fisPerRule);
      currentCalbasis = fisPerRule.getCalBasisOption();
      logger.log(INFO, "currentCalbasis {0}", this.fisPerRule.getCalBasisOption());
//      calendarRules = this.compMgr.getCalRuleByTypeList(this.fisPerRule.getCalBasisOption());
      selectCalRule = this.buildCalSelectList(calendarRules);
      logger.log(INFO, "this.selectCalRule {0}", selectCalRule);
      logger.log(INFO, "Cal list {0}", calendarRules);
      logger.log(INFO, "after get period rule cal basis option is: {0}", currentCalbasis);
      PrimeFaces pf = PrimeFaces.current();
      pf.ajax().update("fisPerRuleUpdtFrm");
     }catch(BacException e){
      MessageUtil.addErrorMessage("fiscalPeriodNf", "errorText");
     }catch(TransactionRolledbackLocalException e){
      MessageUtil.addErrorMessage("fiscalPeriodNf", "errorText");
     }
    }

    public void calendarBasisChangeListener(ValueChangeEvent evt){
     logger.log(INFO, "Web Bean calendarChangeListener called with value {0}", evt.getNewValue());
     int newCalBasis = (Integer)evt.getNewValue();
     fisPerRule.setCalBasisOption(newCalBasis);
     if(newCalBasis == 1){
      fisPerRule.setAnnualDateScheduleBasis(false);
      fisPerRule.setFixedDayOfMonthBasis(false);
      fisPerRule.setCalendarMonthBasis(true);
      fisPerRule.setPeriodLenBasis(false);
     }else if(newCalBasis == 2){
      fisPerRule.setAnnualDateScheduleBasis(false);
      fisPerRule.setFixedDayOfMonthBasis(true);
      fisPerRule.setCalendarMonthBasis(false);
      fisPerRule.setPeriodLenBasis(false);
    }else if(newCalBasis == 3){
      fisPerRule.setAnnualDateScheduleBasis(true);
      fisPerRule.setFixedDayOfMonthBasis(false);
      fisPerRule.setCalendarMonthBasis(false);
      fisPerRule.setPeriodLenBasis(false);
      
    }
    logger.log(INFO, "end change rule basis option {0} annual {1} day of month {2} calendar based {3}", 
            new Object[]{fisPerRule.getCalBasisOption(),fisPerRule.isAnnualDateScheduleBasis(),
             fisPerRule.isFixedDayOfMonthBasis(),fisPerRule.isCalendarMonthBasis() });
        

    }
/*
    public void calendarRuleChangeListener(){
        logger.log(INFO, "calendarRuleChangeListener called selected rule id: {0} ",this.fisPerRule.getCalBasisOption());
        try{
            ArrayList<CalendarRuleBaseRec> calRules = this.compMgr.getCalRuleByTypeList(this.fisPerRule.getCalBasisOption());
            this.selectCalRule = new ArrayList<SelectItem>();
            CalendarRuleBaseRec cal;
            SelectItem item ;
            ListIterator li = calRules.listIterator();
            while(li.hasNext()){
                cal = (CalendarRuleBaseRec)li.next();
                item = new SelectItem();
                item.setValue(cal.getId());
                item.setLabel(cal.getDescription());
                selectCalRule.add(item);
            }

          GenUtil.addInfoMessage("Calendar rules built: "+selectCalRule);
        }catch(BacException e){
            GenUtil.addErrorMessage("Could not get Calendar rules");
        }

    }
*/





 public void validateAddYear(FacesContext fc, UIComponent comp, Object val){
  logger.log(INFO, "validateAddYear called with ctx {0} comp {1} obj {2}", new Object[]{fc,comp,val.getClass().getName()});
  logger.log(INFO, "val class {0}", val.getClass().getSimpleName());
  String valStr = String.valueOf(val);
  logger.log(INFO, "Value length {0}", valStr.length());
  if(val.getClass().getSimpleName().equalsIgnoreCase("Integer")){
   logger.log(INFO, "Check input");
   int yearInput = Integer.parseInt(valStr);
   PrimeFaces pf = PrimeFaces.current();
   if(valStr.length() == 4){
    logger.log(INFO, "4 digit number entered");
    List<CalendarRuleFlexYearRec> yearsUsed = fisPerRule.getCalendarRule().getCalRuleFlexYears();
    logger.log(INFO, "yearsUsed {0}", yearsUsed);
    boolean found = false;
    ListIterator<CalendarRuleFlexYearRec> li = yearsUsed.listIterator();
    while(li.hasNext() && !found){
     CalendarRuleFlexYearRec testYr = li.next();
     if(testYr.getCalYear() == yearInput){
      found = true;
      ((UIInput)comp).setValid(false);
      MessageUtil.addErrorMessage("fiscPerFlexYr", "validationText");
      
     }
    }
    if(!found){
     logger.log(INFO, "Year valid {0}", yearInput);
     UserSessionBean usrBuff =this.getUsrBuff();
     Locale loc = usrBuff.getLoc();
     GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(loc);
     cal.setTime(new Date());
     cal.set(yearInput, 0, 1);
     int maxMth = this.fisPerRule.getNumPeriods();
     int currPeriod = 1;
     List<CalendarRuleFlexPerRec> pers = new ArrayList<CalendarRuleFlexPerRec>();
     for(int i = 0; i<maxMth; i++,currPeriod++ ){
      CalendarRuleFlexPerRec per = new CalendarRuleFlexPerRec();
      per.setCalPeriod(currPeriod);
      cal.set(Calendar.MONTH, i);
      cal.set(Calendar.DAY_OF_MONTH,1);
      per.setStartPeriod(cal.getTime());
      int monthEndDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      cal.set(Calendar.DAY_OF_MONTH, monthEndDate);
      per.setEndPeriod(cal.getTime());
      
      pers.add(per);
     }
     fisCalFlexYrAdd.setFlexPeriods(pers);
     logger.log(INFO, "fisPerRule Years {0}", this.fisPerRule.getCalendarRule().getCalRuleFlexYears());
    }
    pf.ajax().update("addYrFrm");
   }
   
  }
 
 }   

}
