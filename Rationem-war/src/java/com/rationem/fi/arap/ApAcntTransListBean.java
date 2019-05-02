/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.ApLineSel;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;


/**
 *
 * @author user
 */
public class ApAcntTransListBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger( ApAcntTransListBean.class.getName());
 
 @EJB
 private ApManager apMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private DocumentManager docMgr;
 
 private List<DocLineApRec> apLines;
 private List<DocLineApRec> apLinesFiltered;
 private DocLineApRec apLineSelected;
 private ApLineSel selOpt;
 private List<String> selTexts;
 private int numComps = 0;
 
 

 /**
  * Creates a new instance of ApAcntTransListBean
  */
 public ApAcntTransListBean() {
 }
 
 @PostConstruct
 private void init(){
  selOpt = new ApLineSel();
  if(getCompList() == null || getCompList().isEmpty()){
   MessageUtil.addWarnMessage("compsNone", "errorText");
   return;
  }
  selOpt.setComp(getCompList().get(0));
  selTexts = new ArrayList<>();
  selTexts.add(formTextApForKey("transRptSelOption"));
  selTexts.add(formTextApForKey("rptAcntTrans"));
  setStepName(selTexts.get(0));
  //apLineSelected.getDocFi()
 }

 public List<DocLineApRec> getApLines() {
  return apLines;
 }

 public void setApLines(List<DocLineApRec> apLines) {
  this.apLines = apLines;
 }

 public DocLineApRec getApLineSelected() {
  return apLineSelected;
 }

 public void setApLineSelected(DocLineApRec apLineSelected) {
  this.apLineSelected = apLineSelected;
 }

 public List<DocLineApRec> getApLinesFiltered() {
  return apLinesFiltered;
 }

 public void setApLinesFiltered(List<DocLineApRec> apLinesFiltered) {
  this.apLinesFiltered = apLinesFiltered;
 }

 public void onApLineToggle(ToggleEvent evt){
  LOGGER.log(INFO, "Row expand/collapse called for {0}", evt.getData());
  boolean updated = false;
  DocLineApRec apLine = (DocLineApRec)evt.getData();
  LOGGER.log(INFO, "Cleared {0} Clearing line {1}", new Object[]{apLine.isClearedLine(),apLine.isClearingLine()});
  if(apLine.isClearingLine()){
   // clearing line need to check to see if cleared doc set is included
   LOGGER.log(INFO, "Cleared docs {0}", apLine.getClearingLineForLines());
   if(apLine.getClearingLineForLines() == null || apLine.getClearingLineForLines().isEmpty()){
    apLine = docMgr.getApClearedLines(apLine);
    updated = true;
   }
  }
  if(apLine.isClearedLine()){
   LOGGER.log(INFO, "Cleared {0} cleared by {1}", 
     new Object[]{apLine.isClearedLine(),apLine.getClearedByLine()});
   if(apLine.getClearedByLine() == null){
    apLine = docMgr.getApClearedByLine(apLine);
    updated = true;
   }
  }
  if(updated){
   ListIterator<DocLineApRec> li = apLines.listIterator();
   boolean foundLn = false;
   while(li.hasNext() && !foundLn){
    DocLineApRec currLn = li.next();
    if(Objects.equals(currLn.getId(), apLine.getId())){
     li.set(apLine);
     foundLn = true;
     PrimeFaces.current().ajax().update("acntTransFrm:linesDt");
    }
   } 
  }
 }
 
 public int getNumComps() {
  return numComps;
 }

 public void setNumComps(int numComps) {
  this.numComps = numComps;
 }
 
 

 public ApLineSel getSelOpt() {
  return selOpt;
 }

 public void setSelOpt(ApLineSel selOpt) {
  this.selOpt = selOpt;
 }

 @Override
 public String getStepName(){
  return this.selTexts.get(getStep());
 }
 
 public List<ApAccountRec> onApAccountComplete(String input){
  LOGGER.log(INFO,"onApAccountComplete called with comps {0}",selOpt.getCompList());
  if(input == null || input.isEmpty()){
   return this.apMgr.getApAccountsAll(selOpt.getComp());
  }else{
   return apMgr.getApAccountsStartinfWithCode(selOpt.getComp(), input);
  }
  
 }

 public void onBackBtn(){
  setStep(getStep() - 1);
 }
 
 public void onCompSel(SelectEvent evt){
  LOGGER.log(INFO, "onCompSel called with {0}", evt.getObject());
  LOGGER.log(INFO, "selOpt comps {0}", selOpt.getCompList());
  
 
 }
 public List<DocTypeRec> onDocTypeComplete(String input){
  LOGGER.log(INFO, "onDocTypeComplete called with {0}", input);
  
  if(sysBuff.getDocTypesForLedgerCode("AP") == null || sysBuff.getDocTypesForLedgerCode("AP").isEmpty()){
   MessageUtil.addClientErrorMessage("acntTransFrm:errMsg", "docTypeNone", "errorText");
   return null;
  }
  if(StringUtils.isBlank(input)){
   return sysBuff.getDocTypesForLedgerCode("AP");
  }else{
   List<DocTypeRec> retList = new ArrayList<>();
   
   for(DocTypeRec curr:sysBuff.getDocTypesForLedgerCode("AP") ){
    if(StringUtils.startsWith(curr.getName(), input)){
     retList.add(curr);
    }
   }
   return retList;
  }
  
 }
 
 
 public void onDocDateFromSelect(SelectEvent evt){
  LOGGER.log(INFO, "onDocDateFromSelect called with {0}", evt.getObject());
  selOpt.setDocDateFr((Date)evt.getObject());
 }
 
  public void onDocDateFromBlur(){
  LOGGER.log(INFO, "onDocDateFromBlur called with selOpt.getDocDateFr(){0}", selOpt.getDocDateFr());
 }
 
 public void onDocDateFromValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onDocDateFromValidate called with {0}", value);
  boolean fromDateNull = (value == null);
  boolean fromDateValid = false;
  LOGGER.log(INFO, "fromDateNull {0}", fromDateNull);
  LOGGER.log(INFO, "selOpt doc date fr {0} to  {1}", new Object[]{selOpt.getDocDateFr(),selOpt.getDocDateTo()});
  Date fromDate = (Date)value;
  List<String> updateLst = new ArrayList<>();
  PrimeFaces pf = PrimeFaces.current();
  if(fromDateNull){
   /*if(selOpt.getDocDateTo() != null){
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelDocDtFrBlank", "validationText");
    updateLst.add("acntTransFrm:errMsg");
   
   } */
   fromDateValid = true;
  }else{
   if(selOpt.getDocDateTo() == null){
   fromDateValid = true;
  }else{
   LOGGER.log(INFO, "fromDate {0} selOpt.getDocDateTo() {1} date after {3} ", 
     new Object[]{fromDate,selOpt.getDocDateTo(), fromDate.after(selOpt.getDocDateTo())});
   if(fromDate.after(selOpt.getDocDateTo())){
    LOGGER.log(INFO, "From date after to date from [0] to [1]", new Object[]{fromDate.toString(),selOpt.getDocDateTo()});
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelDocDtFrgtTo", "validationText");
    updateLst.add("acntTransFrm:errMsg");
   }else{
     fromDateValid = true;
     LOGGER.log(INFO, "from date before to Date ");
   }
  }
  
  LOGGER.log(INFO, "fromDateValid {0} time {1}", new Object[]{fromDateValid,new Date()});
  ((EditableValueHolder) toValidate).setValid(fromDateValid);
  if(fromDateValid){
   selOpt.setDocDateFr(fromDate);
  }
  updateLst.add("acntTransFrm:docDateFr");
  updateLst.add("acntTransFrm:docDateTo");
  updateLst.add("acntTransFrm:errMsg");
  pf.ajax().update(updateLst);
  
  }
  
 }
 
 public void onDocDateToSel(SelectEvent evt){
  LOGGER.log(INFO, "onDocDateToSel called with {0}", evt.getObject());
 }
 
 public void onDocDateToValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onDocDateToValidate called with value {0}", value);
  LOGGER.log(INFO, "SelOpt docDates from {0} to {1}", new Object[]{selOpt.getDocDateFr(),selOpt.getDocDateTo()});
  boolean toDateValid = false;
  List<String> updateSet = new ArrayList<>();
  Date toDate = null;
  if (value != null){
   toDate = (Date)value;
  }
  PrimeFaces pf = PrimeFaces.current();
  if(value == null){
   LOGGER.log(INFO, "DocDateToValidate called with value null");
   ((EditableValueHolder) toValidate).setValid(true);
   selOpt.setDocDateTo(selOpt.getDocDateFr());
   pf.ajax().update("acntTransFrm:docDateTo");
   return;
  }
  if(selOpt.getDocDateFr() == null){
   MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelDocDtFrBlank", "validationText");
   updateSet.add("acntTransFrm:errMsg");
  }else{
   if(selOpt.getDocDateFr().after(toDate)){
    LOGGER.log(INFO, "From after to date from {0} to {1}", new Object[]{selOpt.getDocDateFr(),toDate});
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelDocDtFrgtTo", "validationText");
    updateSet.add("acntTransFrm:errMsg");
   }else{
    toDateValid = true;
   }
  }
  LOGGER.log(INFO, "fromDateValid {0} time {1}", new Object[]{toDateValid,new Date()});
  ((EditableValueHolder) toValidate).setValid(toDateValid);
  updateSet.add("acntTransFrm:docDateTo");
  pf.ajax().update(updateSet);
 }
 
 public void onDocPostDateFrValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onDocPostDateFrValidate called with value {0}", value);
  LOGGER.log(INFO, "Post date from {0} to {1}", new Object[]{selOpt.getPostDateFr(), selOpt.getPostDateTo()});
  boolean fromDateValid = false;
  Date fromDate = null;
  if(value != null){
   fromDate = (Date)value;
  }
  List<String> updateList = new ArrayList<>();
  if(value == null){
   if(selOpt.getPostDateTo() != null){
    LOGGER.log(INFO, "From is blank and to has a value");
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelPstDtFrEmpt", "validationText");
    updateList.add("acntTransFrm:errMsg");
    
   }
   fromDateValid = true;
  }else{
   LOGGER.log(INFO, "Value has a value");
   if(selOpt.getPostDateTo() != null){
    LOGGER.log(INFO, "To date {0}");
    if(fromDate != null && fromDate.after(selOpt.getPostDateTo())){
     LOGGER.log(INFO, "from date after to date");
     MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelPstDtToFr", "validationText");
     updateList.add("acntTransFrm:errMsg"); 
    }else{
     fromDateValid = true; 
     LOGGER.log(INFO, "From date valid");
    }
   }else{
    fromDateValid = true;
    LOGGER.log(INFO, "From date has value to is null");
   }
  }
  LOGGER.log(INFO, "fromDateValid {0} time {1}", new Object[]{fromDateValid,new Date()});
  LOGGER.log(INFO,"UpdateList {0}",updateList);
  ((EditableValueHolder) toValidate).setValid(fromDateValid);
  updateList.add("acntTransFrm:postDateFr");
  PrimeFaces.current().ajax().update(updateList);
 }
 
 public void onDocPostDateToValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onDocPostDateToValidate called with value {0}", value);
  LOGGER.log(INFO, "selOpt from {0} to {1}", new Object[]{selOpt.getPostDateFr(),selOpt.getPostDateTo()});
  boolean toDateValid = false;
  Date toDate = null;
  List<String> updateList = new ArrayList<>();
  if(value != null){
   toDate = (Date)value;
  }
  if(value == null){
   if(selOpt.getPostDateFr() == null){
    toDateValid = true;
   }else{
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelPstDtToEmpt", "validationText");
    updateList.add("acntTransFrm:errMsg");
   }
  }else{
   // value is populated
   if(selOpt.getPostDateFr() == null){
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelPstDtFrEmpt", "validationText");
    updateList.add("acntTransFrm:errMsg");
   }else{
    // to and From have a value
    LOGGER.log(INFO, "To and from have a value");
    if(toDate.before(selOpt.getPostDateFr())){
     MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelPstDtToFr", "validationText");
     updateList.add("acntTransFrm:errMsg");
    }else{
     toDateValid = true;
    }
   }
  }
  LOGGER.log(INFO, "toDateValid {0} time {1}", new Object[]{toDateValid,new Date()});
  LOGGER.log(INFO,"UpdateList {0}",updateList);
  ((EditableValueHolder) toValidate).setValid(toDateValid);
  updateList.add("acntTransFrm:postDateTo");
  PrimeFaces.current().ajax().update(updateList);
 }
 
 public void onFiscalPeriodFromValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onFiscalPeriodFromValidate called with value {0}",value);
  PrimeFaces pf = PrimeFaces.current();
  boolean fromValid = false;
  if(value == null){
   LOGGER.log(INFO, "Value is null");
   if(selOpt.getFiscPerTo() != null){
    selOpt.setFiscPerTo(null);
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelFpNull", "validationText");
    pf.ajax().update("acntTransFrm:errMsg");
    pf.ajax().update("acntTransFrm:fiscPerTo");
    fromValid = true;
    
   }else{
    fromValid = true;
   }
  }else{
   LOGGER.log(INFO, "Value is not null");
   LOGGER.log(INFO, "selOpt Fisc year from {0} to {1}", 
     new Object[]{selOpt.getFiscPerFr(),selOpt.getFiscPerTo()});
   String perFrVal = String.valueOf(value);
   selOpt.setFiscPerFr(perFrVal);
   if(selOpt.getFiscPerTo() == null){
    LOGGER.log(INFO, "period to is null");
    selOpt.setFiscPerTo(perFrVal);
    pf.ajax().update("acntTransFrm:fiscPerTo");
    fromValid = true;
    LOGGER.log(INFO, "selOpt FiscPerTo is {0}",selOpt.getFiscPerTo());
   }else{
    LOGGER.log(INFO, "Period to has a value");
    LOGGER.log(INFO, "periodFrVal {0} perToVal {1}", new Object[]{
     selOpt.getFiscPerFr(), selOpt.getFiscPerTo() });
    if(Long.parseLong(selOpt.getFiscPerFr()) > Long.parseLong(selOpt.getFiscPerTo())){
     LOGGER.log(INFO, "From cannot be greater than to");
     MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelFiscPerFrTo", "validationText");
     pf.ajax().update("acntTransFrm:errMsg");
    }else{
     fromValid = true;
    }
   }
  }
  
  LOGGER.log(INFO, "selOpt FiscPerFr is {0}",selOpt.getFiscPerFr());
  LOGGER.log(INFO, "fromValid {0}", String.valueOf(fromValid));
  ((EditableValueHolder) toValidate).setValid(fromValid);
  pf.ajax().update("acntTransFrm:fiscPerFr");
 }
 
 public void onFiscalPeriodToValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO,"onFiscalPeriodToValidate called with value {0}",value);
  LOGGER.log(INFO, "selOpt.getFiscPerFr() {0} getFiscPerTo {1}", new Object[]{selOpt.getFiscPerFr(),
   selOpt.getFiscPerTo()});
  
  PrimeFaces pf = PrimeFaces.current();
  if(value == null && (selOpt.getFiscPerFr() == null || 
    selOpt.getFiscPerFr().equals(selOpt.getFiscPerTo()))){
   ((EditableValueHolder) toValidate).setValid(true);
   LOGGER.log(INFO, "Called with value blank and selOpt FiscPerFr equals FiscPerTo");
   pf.ajax().update("acntTransFrm:fiscPerTo");
   return;
  }
  List<String> updateList = new ArrayList<>();
  boolean toValid = false;
  if(value == null && selOpt.getFiscPerFr() != null){
   LOGGER.log(INFO, "Value to null and from not null");
   MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDocSelFpNull", "validationText");
   selOpt.setFiscPerTo(selOpt.getFiscPerFr());
   updateList.add("acntTransFrm:errMsg");
   updateList.add("acntTransFrm:fiscYrTo");
   
  }else if(value == null && selOpt.getFiscPerFr() == null){
   toValid = true;
  }else if(value != null && selOpt.getFiscPerFr() == null) {
   LOGGER.log(INFO, "Value {0} from {1}",new Object[]{value,selOpt.getFiscPerFr()});
   MessageUtil.addClientErrorMessage("acntTransFrm:errMsg", "apDocSelFiscPerFrTo", "validationText");
   updateList.add("acntTransFrm:errMsg");
   updateList.add("acntTransFrm:fiscYrTo");
  }else{
   LOGGER.log(INFO, "Else block");
   Long perTo = Long.valueOf(selOpt.getFiscPerTo());
   Long perFr = Long.valueOf(selOpt.getFiscPerFr());
   LOGGER.log(INFO, "Year to {0} year from {1}", new Object[]{perTo,perFr});
   if(perTo < perFr){
    MessageUtil.addClientErrorMessage("acntTransFrm:errMsg", "apDodSelFisYrToLtFr", "validationText");
    updateList.add("acntTransFrm:errMsg");
    
   }else{
    toValid = true;
    updateList.add("acntTransFrm:errMsg");
    
   }
  }
  LOGGER.log(INFO, "toValid {0}", String.valueOf(toValid));
  
  ((EditableValueHolder) toValidate).setValid(toValid);
  pf.ajax().update("acntTransFrm:fiscPerTo");
  
  pf.ajax().update(updateList);
  LOGGER.log(INFO, "end selOpt.getFiscPerFr() {0} getFiscPerTo {1}", 
    new Object[]{selOpt.getFiscPerFr(),selOpt.getFiscPerTo()});  
 }
 
 
 
 public void onFiscalYearFromValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO, "onFiscalYearFromValidate called with value {0}",value);
  PrimeFaces pf = PrimeFaces.current();
  boolean fromValid = false;
  if(value == null){
   LOGGER.log(INFO, "Value is null");
   if(selOpt.getFiscYearTo() != null){
    selOpt.setFiscYearTo(null);
    MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDodSelFisYrFrEmpt", "validationText");
    pf.ajax().update("acntTransFrm:errMsg");
    pf.ajax().update("acntTransFrm:fiscYrTo");
    fromValid = true;
    
   }else{
    fromValid = true;
   }
  }else{
   LOGGER.log(INFO, "Value is not null");
   LOGGER.log(INFO, "selOpt Fisc year from {0} to {1}", new Object[]{selOpt.getFiscYearFr(),selOpt.getFiscYearFr()});
   String yearFrVal = String.valueOf(value);
   selOpt.setFiscYearFr(yearFrVal);
  
  
   if(selOpt.getFiscYearTo() == null){
    LOGGER.log(INFO, "Year to is null");
    selOpt.setFiscYearTo(yearFrVal);
    pf.ajax().update("acntTransFrm:fiscYrTo");
    fromValid = true;
   }else{
    LOGGER.log(INFO, "Year to has a value");
    LOGGER.log(INFO, "yearFrVal {0} yearToVal {1}", new Object[]{selOpt.getFiscYearFr(), selOpt.getFiscYearTo()});
    if(Long.parseLong(selOpt.getFiscYearFr()) > Long.parseLong(selOpt.getFiscYearTo())){
     LOGGER.log(INFO, "From cannot be greater than to");
     MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apTransRptFiscYrFrTo", "errorText");
     pf.ajax().update("acntTransFrm:errMsg");
    }else{
     fromValid = true;
    }
   }
  }
  LOGGER.log(INFO, "fromValid {0}", String.valueOf(fromValid));
  ((EditableValueHolder) toValidate).setValid(fromValid);
  pf.ajax().update("acntTransFrm:fiscYrFr");
 }
 
 public void onFiscalYearToValidate(FacesContext context, UIComponent toValidate, Object value){
  LOGGER.log(INFO,"onFiscalYearToValidate value {0}");
  LOGGER.log(INFO, "selOpt.getFiscPerFr() {0} getFiscPerTo {1}", 
    new Object[]{selOpt.getFiscYearFr(),selOpt.getFiscYearTo()});
  PrimeFaces pf = PrimeFaces.current();
  List<String> updateList = new ArrayList<>();
  boolean toValid = false;
  if(value == null && selOpt.getFiscYearFr() != null){
   LOGGER.log(INFO, "Valie null and from not null");
   MessageUtil.addClientWarnMessage("acntTransFrm:errMsg", "apDodSelFisYrToEmptFr", "validationText");
   selOpt.setFiscYearTo(selOpt.getFiscYearFr());
   updateList.add("acntTransFrm:errMsg");
   updateList.add("acntTransFrm:fiscYrTo");
   
  }else if(value == null && selOpt.getFiscYearFr() == null){
   toValid = true;
  }else if(value != null && selOpt.getFiscYearFr() == null) {
   LOGGER.log(INFO, "Value {0} from {1}",new Object[]{value,selOpt.getFiscYearFr()});
   MessageUtil.addClientErrorMessage("acntTransFrm:errMsg", "apDodSelFisYrToLtFr", "validationText");
   updateList.add("acntTransFrm:errMsg");
   updateList.add("acntTransFrm:fiscYrTo");
  }else{
   LOGGER.log(INFO, "Else block");
   Long yearTo = Long.valueOf(selOpt.getFiscYearTo());
   Long yearFr = Long.valueOf(selOpt.getFiscYearFr());
   LOGGER.log(INFO, "Year to {0} year from {1}", new Object[]{yearTo,yearFr});
   if(yearTo < yearFr){
    MessageUtil.addClientErrorMessage("acntTransFrm:errMsg", "apDodSelFisYrToLtFr", "validationText");
    updateList.add("acntTransFrm:errMsg");
    
   }else{
    toValid = true;
    updateList.add("acntTransFrm:errMsg");
    
   }
  }
  LOGGER.log(INFO, "toValid {0}", String.valueOf(toValid));
  ((EditableValueHolder) toValidate).setValid(toValid);
  pf.ajax().update("acntTransFrm:fiscYrTo");
  
  pf.ajax().update(updateList);
  LOGGER.log(INFO, "end selOpt.getFiscYearFr() {0} getFiscYearTo {1}", new Object[]{selOpt.getFiscYearFr(),selOpt.getFiscYearTo()});  
 }
 public void onGetTrans(){
  setStep(getStep() + 1);
  apLines = docMgr.getApLines(selOpt);
  
 }
 
 


}
