/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.doc;

import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class GlStdJnlDisplay extends BaseBean{
 
 private static  final Logger logger = Logger.getLogger(GlStdJnlDisplay.class.getName());
 
 private FiDoclSelectionOpt selectOpts;
 private List<DocTypeRec> docTypes;
 private GregorianCalendar cal;
 private List<DocFiRec> docList;
 private DocFiRec docSelected;
 
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private DocumentManager docMgr;
 

 /**
  * Creates a new instance of GlStdJnlDisplay
  */
 public GlStdJnlDisplay() {
 }
 
 @PostConstruct
 private void init(){
  selectOpts = new FiDoclSelectionOpt();
  if(this.getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   
  }else{
   selectOpts.setComp(this.getCompList().get(0));
   
  
  }
  cal = (GregorianCalendar)Calendar.getInstance(selectOpts.getComp().getLocale());
  docTypes = this.sysBuff.getDocTypes();
 }

 public DocFiRec getDocSelected() {
  return docSelected;
 }

 public void setDocSelected(DocFiRec docSelected) {
  this.docSelected = docSelected;
 }

 
 public List<DocTypeRec> getDocTypes() {
  return docTypes;
 }

 public void setDocTypes(List<DocTypeRec> docTypes) {
  this.docTypes = docTypes;
 }

 public List<DocFiRec> getDocList() {
  return docList;
 }

 public void setDocList(List<DocFiRec> docList) {
  this.docList = docList;
 }

 
 public FiDoclSelectionOpt getSelectOpts() {
  return selectOpts;
 }

 public void setSelectOpts(FiDoclSelectionOpt selectOpts) {
  this.selectOpts = selectOpts;
 }

 public void onClearSelections(){
  logger.log(INFO, "onClearSelections called");
  selectOpts = new FiDoclSelectionOpt();
  selectOpts.setComp(this.getCompList().get(0));
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("selectionPg ");
 }
 public void onDocDtFromChange(ValueChangeEvent evt){
  logger.log(INFO, "onDocDtFromChange called with {0}", evt.getNewValue());
  Date fromDate = (Date)evt.getNewValue();
  selectOpts.setDocDateFrom(fromDate);
  selectOpts.setDocDateTo(fromDate);
  logger.log(INFO, "Date to {0}", selectOpts.getDocDateTo());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("docDtTo");
  
  
 }
 
 public String onDocListFlowProcess(FlowEvent evt){
  
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  logger.log(INFO, "onDocListFlowProcess called with current {0} next {1}", new Object[]{currStep,nextStep});
  
  if(currStep.equals("selOpts") && nextStep.equals("jnlList")){
   logger.log(INFO, "About to list docs");
   if(selectOpts.getDocDateTo() != null){
    cal.setTime(selectOpts.getDocDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, 1);
    selectOpts.setDocDateTo(cal.getTime());
   }
   if(selectOpts.getPostDateTo() != null){
    cal.setTime(selectOpts.getPostDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, 1);
    selectOpts.setPostDateTo(cal.getTime());
   }
   if(selectOpts.getEntryDateTo() != null){
    cal.setTime(selectOpts.getEntryDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, 1);
    selectOpts.setEntryDateTo(cal.getTime());
   }
   
   logger.log(INFO, "Entry to {0}", selectOpts.getEntryDateTo());
   
   docList = this.docMgr.getFiDocsBySelOpt(selectOpts);
   logger.log(INFO, "docList {0}", docList);
   if(selectOpts.getDocDateTo() != null){
    cal.setTime(selectOpts.getDocDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, -1);
    selectOpts.setDocDateTo(cal.getTime());
   }
   if(selectOpts.getPostDateTo() != null){
    cal.setTime(selectOpts.getPostDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, -1);
    selectOpts.setPostDateTo(cal.getTime());
   }
   if(selectOpts.getEntryDateTo() != null){
    cal.setTime(selectOpts.getEntryDateTo());
    int day = Calendar.DAY_OF_MONTH;
    cal.add(day, -1);
    selectOpts.setEntryDateTo(cal.getTime());
   }
   
  }
  
  return nextStep;
 }

 
 public void onEntryDateFrChange(ValueChangeEvent evt){
  logger.log(INFO, "onEntryDateFrChange called with {0}", evt.getNewValue());
  Date entryFromDate = (Date)evt.getNewValue();
  selectOpts.setEntryDateFrom(entryFromDate);
  
  if(selectOpts.getEntryDateTo() == null || selectOpts.getEntryDateTo().before(entryFromDate)){
   selectOpts.setEntryDateTo(entryFromDate);
   logger.log(INFO, "EntryDateTo {0}", selectOpts.getEntryDateTo());
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("entryDtTo");
  }
  
 }
 
 

 public void onDocLineMenu(SelectEvent evt){
  logger.log(INFO, "onDocLineMenu called docSelected {0}", docSelected.getDocNumber());
 }
 
 public void onDocLineMenuDisp(){
  
  logger.log(INFO, "docSelected lines {0}", docSelected.getDocLines());
  if(docSelected.getDocLines() == null){
   // get lines and save to list of docs
   docSelected = this.docMgr.getDocLines(docSelected);
  }
  logger.log(INFO, "docSelected lines  2 {0}", docSelected.getDocLines());
  
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("docDispFrm");
  rCtx.execute("PF('docDispWv').show()");
 }
 
 public void onDocLineMenuEdit(){
  logger.log(INFO, "onDocLineMenuEdit docSelected lines {0}", docSelected.getDocLines());
  if(docSelected.getDocLines() == null){
   // get lines and save to list of docs
   docSelected = this.docMgr.getDocLines(docSelected);
  }
  logger.log(INFO, "docSelected lines  2 {0}", docSelected.getDocLines());
  
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("docEditFrm");
  rCtx.execute("PF('docEditWv').show()");
 }
 
 public List<DocTypeRec> onDocTypeComplete(String input){
 
 List<DocTypeRec> retList;
 
 if(input == null || input.isEmpty()){
  return this.docTypes;
 }else{
  retList = new ArrayList<>();
  if(this.docTypes == null){
   logger.log(INFO, "No document types");
   return null;
  }
  for(DocTypeRec docTy: docTypes){
   if(docTy.getCode().startsWith(input)){
    retList.add(docTy);
   }
  }
  return retList;
 }
  
 
 
}
 
 public void onDocChangeSave(){
  logger.log(INFO, "onDocChangeSave called with {0}", docSelected.getDocHdrText());
  docSelected.setChangedBy(this.getLoggedInUser());
  docSelected.setChangedOn(new Date());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  try{
  docSelected = this.docMgr.updateDocument(docSelected, getLoggedInUser(), getView());
  logger.log(INFO, "Posted doc id {0}", docSelected.getId());
  MessageUtil.addInfoMessage("docUpdated", "blacResponse");
  ListIterator<DocFiRec> docLi = docList.listIterator();
  boolean foundDoc = false;
  while(docLi.hasNext() && !foundDoc){
   DocFiRec curr = docLi.next();
   if(Objects.equals(curr.getId(), docSelected.getId())){
    docLi.set(docSelected);
    rCtx.update("jnlListDt");
    rCtx.execute("PF('docEditWv').hide();");
   }
  }
  }catch(Exception ex){
   logger.log(INFO, "Not updated doc  {0}", ex.getLocalizedMessage()); 
   MessageUtil.addErrorMessageParam1("docUpdate", "errorText", ex.getLocalizedMessage());
  }
 }
public void onPostDateFrChange(ValueChangeEvent evt){
 Date newDate = (Date)evt.getNewValue();
 selectOpts.setPostDateFrom(newDate);
 if(selectOpts.getPostDateTo() == null || selectOpts.getPostDateTo().before(newDate)){
  logger.log(INFO, "Need to set post date to {0}", newDate);
  selectOpts.setPostDateTo(newDate);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("postDtTo");
 }
}
public List<UserRec> onUserComplete(String input){
 
 List<UserRec> usrRetList = new ArrayList<>();
 return usrRetList;
} 
}
