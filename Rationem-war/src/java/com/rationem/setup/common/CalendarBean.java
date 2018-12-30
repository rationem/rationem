/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.company.CalendarRuleBaseRec;
import com.rationem.busRec.config.company.CalendarRuleFixedDateRec;
import com.rationem.busRec.config.company.CalendarRuleFlexPerRec;
import com.rationem.busRec.config.company.CalendarRuleFlexYearRec;
import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author user
 */
public class CalendarBean  extends BaseBean{
 private static final Logger logger = Logger.getLogger(CalendarBean.class.getName());

 private CalendarRuleBaseRec calBase;
 private CalendarRuleFixedDateRec calFixeDt;
 private CalendarRuleBaseRec calFlex;
 private CalendarRuleMonthRec calMonth;
 private String selectType;
 private CalendarRuleFlexYearRec flexYrSelected;
 private CalendarRuleFlexYearRec flexYrNew;
 private CalendarRuleFlexPerRec flexPerSelected;
 private CalendarRuleFlexPerRec flexPerNew;
 private String startMonthName; 
 
 private List<CalendarRuleBaseRec> calendars;
 
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private CompanyManager compMgr;
 /**
  * Creates a new instance of CalendarBean
  */
 public CalendarBean() {
 }
 
 @PostConstruct
 private void init(){
  calendars = sysBuff.getCalendarRules();
  ListIterator<CalendarRuleBaseRec> li = calendars.listIterator();
  while(li.hasNext()){
   CalendarRuleBaseRec cal = li.next();
   
   String type = cal.getCalType();
   if(type.startsWith("cal")){
    
   
   String typeLoc = this.formTextForKey(type);
   cal.setCalType(typeLoc);
   li.set(cal);
   }
  }
  logger.log(INFO, "Calendar Bean calendars {0}", calendars);
 }

 public CalendarRuleBaseRec getCalBase() {
  if(calBase == null){
   calBase = new CalendarRuleBaseRec();
  }
  return calBase;
 }

 public void setCalBase(CalendarRuleBaseRec calBase) {
  this.calBase = calBase;
 }

 public CalendarRuleFixedDateRec getCalFixeDt() {
  return calFixeDt;
 }

 public void setCalFixeDt(CalendarRuleFixedDateRec calFixeDt) {
  this.calFixeDt = calFixeDt;
 }

 public CalendarRuleBaseRec getCalFlex() {
  return calFlex;
 }

 public void setCalFlex(CalendarRuleBaseRec calFlex) {
  this.calFlex = calFlex;
 }

 public CalendarRuleMonthRec getCalMonth() {
  return calMonth;
 }

 public void setCalMonth(CalendarRuleMonthRec calMonth) {
  this.calMonth = calMonth;
 }

 
 public List<CalendarRuleBaseRec> getCalendars() {
  return calendars;
 }

 public void setCalendars(List<CalendarRuleBaseRec> calendars) {
  this.calendars = calendars;
 }

 public CalendarRuleFlexYearRec getFlexYrSelected() {
  return flexYrSelected;
 }

 public void setFlexYrSelected(CalendarRuleFlexYearRec flexYrSelected) {
  this.flexYrSelected = flexYrSelected;
  logger.log(INFO, "flexYrSelected updated with {0}", flexYrSelected);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addPeriodFrm");
 }

 
 public String getSelectType() {
  return selectType;
 }

 public CalendarRuleFlexPerRec getFlexPerNew() {
  if(flexPerNew == null){
   flexPerNew = new CalendarRuleFlexPerRec();
   if(flexYrSelected == null){
    flexPerNew.setCalPeriod(1);
   }else{
    if(flexYrSelected.getFlexPeriods() == null || flexYrSelected.getFlexPeriods().isEmpty()){
     flexPerNew.setCalPeriod(1);
    }else{
     int newPerNum = flexYrSelected.getFlexPeriods().size();
     
     flexPerNew.setCalPeriod(++newPerNum);
    }
   }
  }
  return flexPerNew;
 }

 public void setFlexPerNew(CalendarRuleFlexPerRec flexPerNew) {
  this.flexPerNew = flexPerNew;
 }

 public CalendarRuleFlexPerRec getFlexPerSelected() {
  return flexPerSelected;
 }

 public void setFlexPerSelected(CalendarRuleFlexPerRec flexPerSelected) {
  this.flexPerSelected = flexPerSelected;
 }

 
 public CalendarRuleFlexYearRec getFlexYrNew() {
  if(flexYrNew == null){
   flexYrNew = new CalendarRuleFlexYearRec();
  }
  return flexYrNew;
 }

 public void setFlexYrNew(CalendarRuleFlexYearRec flexYrNew) {
  this.flexYrNew = flexYrNew;
 }

 public void setSelectType(String selectType) {
  this.selectType = selectType;
 }

 public String getStartMonthName() {
  if(startMonthName == null){
   startMonthName = new String();
  }
  return startMonthName;
 }

 public void setStartMonthName(String startMonthName) {
  this.startMonthName = startMonthName;
 }
 
 public String onCreateWizFlow(FlowEvent evt){
  String oldStep = (String)evt.getOldStep();
  String newStep = (String)evt.getNewStep();
  
  if(oldStep.equals("basicId")){
   // coming from 1st page
   if(calBase.getCalType().equals("cal")){
    calBase.setNaturalCal(true);
    calMonth = null;
    calFlex = null;
    newStep = "summId";
   }else if(calBase.getCalType().equals("mon")){
    calMonth = new CalendarRuleMonthRec();
    calMonth.setDescription(calBase.getDescription());
    calMonth.setReference(calBase.getReference());
    calMonth.setMonthCal(true);
    calFlex = null;
    
    newStep = "monthId";
   }else if(calBase.getCalType().equals("flex")){
    calFlex = calBase;
    calMonth = null;
    
    calFlex.setFlexCal(true);
    newStep = "flexId";
   }
  }else if(oldStep.equals("summId")) {
   // coming back from last page
   if(calBase.getCalType().equals("cal")){
    newStep = "basicId";
   }else if(calBase.getCalType().equals("mon")){
    newStep = "monthId";
   }else if(calBase.getCalType().equals("flex")){
    newStep = "flexId";
   }
  }else if(oldStep.equals("monthId") && newStep.equals("flexId")){
   newStep = "summId";
  }else if(oldStep.equals("flexId") && newStep.equals("monthId")){
   newStep = "basicId";
  }
  return newStep;
 }
 public void onDisplayDlg(){
  logger.log(INFO, "selectType {0}", selectType);
  if(selectType.equals("CalendarRuleFlexPerRec")){
   if(calFlex.getCalRuleFlexYears() == null || calFlex.getCalRuleFlexYears().isEmpty() ){
    calFlex = sysBuff.getCalendarFlexYears(calFlex);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displFrm");
  rCtx.execute("PF('disCalWv').show()");
 }
 
 public void onSaveNewCal(){
  CalendarRuleBaseRec newCal;
  if(calBase.getCalType().equals("cal")){
   newCal = calBase;
  }else if(calBase.getCalType().equals("mon")){
   newCal = calMonth;
  }else {
   newCal = calFlex;
  }
  newCal.setCreatedBy(this.getLoggedInUser());
  newCal.setCreatedOn(new Date());
  logger.log(INFO, "Save cal ref {0}", newCal.getReference());
  logger.log(INFO, "cal {0} month {1} flex {2}", new Object[]{newCal.isNaturalCal(),
   newCal.isMonthCal(),newCal.isFlexCal()});
  try{
   newCal = this.compMgr.addNewCalendar(newCal, getView());
   MessageUtil.addInfoMessageVar1("calRuleCreated", "blacResponse", newCal.getReference());
  }catch(Exception ex){
   MessageUtil.addErrorMessageParam1("calRuleCr", "errorText", newCal.getReference());
   logger.log(INFO, "Error msg {0}", ex.getLocalizedMessage());
  }
 }
 
 public void onTransferDoM(){
  ListIterator<CalendarRuleBaseRec> li = calendars.listIterator();
  
  while(li.hasNext() ){
   CalendarRuleBaseRec calRec = li.next();
   if(calRec.getId() == calFixeDt.getId()){
    logger.log(INFO, "calMonth ref {0}", calFixeDt.getReference());
    calFixeDt.setChangedBy(this.getLoggedInUser());
    calFixeDt.setChangedOn(new Date());
    calFixeDt = (CalendarRuleFixedDateRec)sysBuff.updateCalendar(calFixeDt, getView());
    li.set(calFixeDt);
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("calTbl");
    logger.log(INFO, "Calendars updated with {0}", calFixeDt);
    rCtx.execute("PF('updtCalDlgWv').hide()");
    return;
   }
  }
 }
 public void onTransferMonth(){
  logger.log(INFO, "onTransferMonth called");
  
  ListIterator<CalendarRuleBaseRec> li = calendars.listIterator();
  
  while(li.hasNext() ){
   CalendarRuleBaseRec calRec = li.next();
   if(calRec.getId() == calMonth.getId()){
    logger.log(INFO, "calMonth ref {0}", calMonth.getReference());
    calMonth.setChangedBy(this.getLoggedInUser());
    calMonth.setChangedOn(new Date());
    calMonth = (CalendarRuleMonthRec)sysBuff.updateCalendar(calRec, getView());
    li.set(calMonth);
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("calTbl");
    logger.log(INFO, "Calendars updated with {0}", calMonth);
    rCtx.execute("PF('updtCalDlgWv').hide()");
    return;
   }
  }
  
 }
 public void onUpdateDlg(){
  logger.log(INFO, "onUpdateDlg selectType {0}", selectType);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(selectType.equals("CalendarRuleFlexPerRec")){
   if(calFlex.getCalRuleFlexYears() == null || calFlex.getCalRuleFlexYears().isEmpty() ){
    calFlex = sysBuff.getCalendarFlexYears(calFlex);
   }
  }
  
  
  rCtx.update("upDtDlgOp");
  rCtx.execute("PF('updtCalDlgWv').show()");
  
  
  
 }
 public void onDispRowToggle(ToggleEvent evt){
  logger.log(INFO,"on row toggle");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("dispCalDlgId");
 }

 public void onFlexYearAddDlg(){
  flexYrNew = new CalendarRuleFlexYearRec();
  GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
  cal.setTime(new Date());
  int yr = cal.get(Calendar.YEAR);
  flexYrNew.setCalYear(yr);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addYearFrm");
  rCtx.execute("PF('addyearWv').show()");
 }
 
 public void onFlexPerAddDlg(){
  logger.log(INFO, "Selected year {0}", this.flexYrSelected);
 }
 
 public void onFlexPerAddTrf(){
  logger.log(INFO, "onFlexPerAddTrf called with year {0}",flexYrSelected.getCalYear() );
  flexPerNew.setCreatedBy(this.getLoggedInUser());
  flexPerNew.setCreatedOn(new Date());
  flexPerNew.setCalRuleFlexYr(flexYrSelected);
  flexPerNew = compMgr.updateCalendarRuleFlexPeriodRec(flexPerNew, getView());
  List<CalendarRuleFlexPerRec> periods =  flexYrSelected.getFlexPeriods();
  if(periods == null){
   periods = new ArrayList<CalendarRuleFlexPerRec>();
  }
  periods.add(flexPerNew);
  flexYrSelected.setFlexPeriods(periods);
  List<CalendarRuleFlexYearRec> years = calFlex.getCalRuleFlexYears();
  ListIterator <CalendarRuleFlexYearRec> li = years.listIterator();
  boolean foundYr = false;
  while(li.hasNext() && !foundYr ){
   CalendarRuleFlexYearRec flYr = li.next();
   if(flYr.getId() == flexYrSelected.getId()){
    logger.log(INFO, "Found yr to update {0}", flexYrSelected.getCalYear());
    li.set(flexYrSelected);
    foundYr = true;
   }
  }
  if(!foundYr){
   years.add(flexYrSelected);
  }
  calFlex.setCalRuleFlexYears(years);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("flexYrUpdtTbl");
  rCtx.execute("PF('addPerWv').hide()");
  flexPerNew = null;
 }
 
 public void onFlexPerAddNew(){
  logger.log(INFO, "onFlexPerAddNew called with year {0}",flexYrSelected.getCalYear() );
  flexPerNew.setCreatedBy(this.getLoggedInUser());
  flexPerNew.setCreatedOn(new Date());
  flexPerNew.setCalRuleFlexYr(flexYrSelected);
  //flexPerNew = compMgr.updateCalendarRuleFlexPeriodRec(flexPerNew, getView());
  List<CalendarRuleFlexPerRec> periods =  flexYrSelected.getFlexPeriods();
  if(periods == null){
   periods = new ArrayList<CalendarRuleFlexPerRec>();
  }
  periods.add(flexPerNew);
  flexYrSelected.setFlexPeriods(periods);
  List<CalendarRuleFlexYearRec> years = calFlex.getCalRuleFlexYears();
  ListIterator <CalendarRuleFlexYearRec> li = years.listIterator();
  boolean foundYr = false;
  while(li.hasNext() && !foundYr ){
   CalendarRuleFlexYearRec flYr = li.next();
   if(flYr.getId() == flexYrSelected.getId()){
    logger.log(INFO, "Found yr to update {0}", flexYrSelected.getCalYear());
    li.set(flexYrSelected);
    foundYr = true;
   }
  }
  if(!foundYr){
   years.add(flexYrSelected);
  }
  calFlex.setCalRuleFlexYears(years);
  flexPerNew = null;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addPeriodFrm");
 }
 public void onFlexPerAddNewTrf(){
  logger.log(INFO, "onFlexPerAddNewTrf called with year {0}",flexYrSelected.getCalYear() );
  flexPerNew.setCreatedBy(this.getLoggedInUser());
  flexPerNew.setCreatedOn(new Date());
  flexPerNew.setCalRuleFlexYr(flexYrSelected);
  //flexPerNew = compMgr.updateCalendarRuleFlexPeriodRec(flexPerNew, getView());
  List<CalendarRuleFlexPerRec> periods =  flexYrSelected.getFlexPeriods();
  if(periods == null){
   periods = new ArrayList<CalendarRuleFlexPerRec>();
  }
  periods.add(flexPerNew);
  flexYrSelected.setFlexPeriods(periods);
  List<CalendarRuleFlexYearRec> years = calFlex.getCalRuleFlexYears();
  ListIterator <CalendarRuleFlexYearRec> li = years.listIterator();
  boolean foundYr = false;
  while(li.hasNext() && !foundYr ){
   CalendarRuleFlexYearRec flYr = li.next();
   if(flYr.getId() == flexYrSelected.getId()){
    logger.log(INFO, "Found yr to update {0}", flexYrSelected.getCalYear());
    li.set(flexYrSelected);
    foundYr = true;
   }
  }
  if(!foundYr){
   years.add(flexYrSelected);
  }
  calFlex.setCalRuleFlexYears(years);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("flexYrNewTbl");
  rCtx.execute("PF('addPerWv').hide()");
  flexPerNew = null;
 }
 
 public void onFlexYearAdd(){
  calFlex.setChangedBy(getLoggedInUser());
  calFlex.setChangedOn(new Date());
  flexYrNew.setCalRule(calFlex);
  flexYrNew.setCreatedBy(this.getLoggedInUser());
  flexYrNew.setCreatedOn(new Date());
  flexYrNew = compMgr.updateCalendarRuleFlexYearRec(flexYrNew, getView());
  List<CalendarRuleFlexYearRec> years = calFlex.getCalRuleFlexYears();
  years.add(flexYrNew);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("flexYrUpdtTbl");
  rCtx.execute("PF('addyearWv').hide()");
 }
 
 public void onFlexYearAddNew(){
  calFlex.setChangedBy(getLoggedInUser());
  calFlex.setChangedOn(new Date());
  flexYrNew.setCalRule(calFlex);
  flexYrNew.setCreatedBy(this.getLoggedInUser());
  flexYrNew.setCreatedOn(new Date());
  //flexYrNew = compMgr.updateCalendarRuleFlexYearRec(flexYrNew, getView());
  List<CalendarRuleFlexYearRec> years = calFlex.getCalRuleFlexYears();
  if(years == null){
   years = new ArrayList<CalendarRuleFlexYearRec>();
  }
  years.add(flexYrNew);
  calFlex.setCalRuleFlexYears(years);
  logger.log(INFO, "calFlex years {0}", years);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("flexYrNewTbl");
  rCtx.execute("PF('addyearWv').hide()");
 }
 
 public void onPeriodRowEdit(RowEditEvent evt){
  logger.log(INFO, "onPeriodRowEdit called with {0}", evt.getObject());
  CalendarRuleFlexPerRec rowPer = (CalendarRuleFlexPerRec)evt.getObject();
  logger.log(INFO, "row year {0}",rowPer.getCalRuleFlexYr().getCalYear());
 }
 public void onMenuSelect(SelectEvent evt){
  logger.log(INFO, "onMenuSelect called with {0}", evt.getObject());
  this.calBase = (CalendarRuleBaseRec)evt.getObject();
  String className = calBase.getClass().getSimpleName();
  logger.log(INFO, "className {0}", className);
  if(className.equals("CalendarRuleFixedDateRec")){
   calFixeDt = (CalendarRuleFixedDateRec)calBase;
   selectType = "CalendarRuleFixedDateRec";
  }else if(className.equals("CalendarRuleBaseRec")){
   calFlex = calBase;
   selectType = "CalendarRuleFlexPerRec";
  }else if(className.equals("CalendarRuleMonthRec")){
   calMonth = (CalendarRuleMonthRec)calBase;
   selectType = "CalendarRuleMonthRec";
  }
  logger.log(INFO, "cals fixed {0} flex {1} month {2} base {3}", new Object[]
  {calFixeDt,calFlex,calMonth});
 }

 public void onDayOfMonthChange(ValueChangeEvent evt){
  calFixeDt.setStartMonthNumberL((Long)evt.getNewValue());
 }
 public void validateDayOfMonth(FacesContext c, UIComponent comp, Object val){
  
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(new Date());
  cal.set(Calendar.MONTH, calFixeDt.getStartMonthNumber() - 1);
  int maxDayNum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  int entryDay;
  if(val.getClass().getSimpleName().equals("Long")){
   
   entryDay = ((Long)val).intValue();
  }else {
   entryDay = (Integer)val;
  }
  if(entryDay < 1){
   MessageUtil.addErrorMessage("calDomZero", "validationText");
   ((UIInput)comp).setValid(false);
  }
  if(entryDay > maxDayNum ){
   ((UIInput)comp).setValid(false);
   MessageUtil.addErrorMessage("calDomAfterMthEnd", "validationText");
  }
  if(!((UIInput)comp).isValid()){
   return;
  }
  ((UIInput)comp).setValid(true);
  
 }
}
