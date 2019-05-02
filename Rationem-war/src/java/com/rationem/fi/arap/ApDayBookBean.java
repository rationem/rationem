/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.ApLineSel;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author user
 */
public class ApDayBookBean extends BaseBean {
 private static final Logger LOGGER =  Logger.getLogger(ApDayBookBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private DocumentManager docMgr;
 
 private List<DocLineApRec> apLines;
 private DocLineApRec apLineSel;
 private ApLineSel selOpts;
 private int step;
 private String stepName;
 private double totalDoc;
 private double totalCredit;
 private double totalDebit;
 
 

 /**
  * Creates a new instance of ApDayBookBean
  */
 public ApDayBookBean() {
 }
 
 @PostConstruct
 private void init(){
  selOpts = new ApLineSel();
  step = 0;
  stepName = formTextForKey("selOpt");
  if(this.getCompList() == null){
   MessageUtil.addWarnMessage("compsNone", "errorText");
   return;
  }
  selOpts.setComp(getCompList().get(0));
 }

 public List<DocLineApRec> getApLines() {
  return apLines;
 }

 public void setApLines(List<DocLineApRec> apLines) {
  this.apLines = apLines;
 }

 public DocLineApRec getApLineSel() {
  return apLineSel;
 }

 public void setApLineSel(DocLineApRec apLineSel) {
  this.apLineSel = apLineSel;
  
 }

 public double getTotalDoc() {
  return totalDoc;
 }

 public void setTotalDoc(double totalDoc) {
  this.totalDoc = totalDoc;
 }

 public double getTotalCredit() {
  return totalCredit;
 }

 public void setTotalCredit(double totalCredit) {
  this.totalCredit = totalCredit;
 }

 public double getTotalDebit() {
  return totalDebit;
 }

 public void setTotalDebit(double totalDebit) {
  this.totalDebit = totalDebit;
 }

 
 
 public ApLineSel getSelOpts() {
  return selOpts;
 }

 public void setSelOpts(ApLineSel selOpts) {
  this.selOpts = selOpts;
 }

 public int getStep() {
  return step;
 }

 public void setStep(int step) {
  this.step = step;
 }

 public String getStepName() {
  return stepName;
 }

 public void setStepName(String stepName) {
  this.stepName = stepName;
 }
 
 
 public List<DocTypeRec> onDocTypeComplete(String input){
 LOGGER.log(INFO, "onDocTypeComplete called with {0}", input);
 
 List<DocTypeRec> docTypes = sysBuff.getDocTypes();
 if(StringUtils.isBlank(input)){
  
 }else{
  ListIterator<DocTypeRec> li = docTypes.listIterator();
  while(li.hasNext()){
   DocTypeRec curr = li.next();
   if(!StringUtils.startsWith(curr.getName(), input)){
    li.remove();
   }
  }
 }
 return docTypes;
  
 }
 
 public void onGetDocs(){
  LOGGER.log(INFO, "onGetDocs called");
  apLines = docMgr.getApDayBook(selOpts);
  PrimeFaces pf = PrimeFaces.current();
  
  if(apLines == null || apLines.isEmpty()){
   LOGGER.log(INFO, "No documents found");
   pf.ajax().update("dayBkSel:errMsg");
  }else{
   step = 1;
   stepName = this.formTextApForKey("dayBookList");
   
   MessageUtil.addInfoMessageVar1("dayBookNumDoc", "formTextAp", String.valueOf(apLines.size()));
   
   totalDoc = 0;
   for(DocLineApRec ln:apLines){
    LOGGER.log(INFO,"Web layer doc number {0}",ln.getDocFi().getDocNumber());
    if(ln.getPostType().isDebit()){
     totalDoc -= ln.getDocAmount();
    
    }else{
     totalDoc += ln.getDocAmount();
    }
   }
   LOGGER.log(INFO, "totalDoc {0}", totalDoc);
   pf.ajax().update("dayBkSel");
  }
 }
 
 
}
