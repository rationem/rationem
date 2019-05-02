/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.ProcessCodeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class ProcessCodeBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ProcessCodeBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BasicSetup setup;
 
 private ProcessCodeRec processCode;
 private List<ProcessCodeRec> processCodes;
 private ProcessCodeRec processCodeSel;
 private List<ModuleRec> modules;

 /**
  * Creates a new instance of ProcessCodeBean
  */
 public ProcessCodeBean() {
  
  
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

 
 public ProcessCodeRec getProcessCode() {
  
  return processCode;
 }

 public void setProcessCode(ProcessCodeRec processCode) {
  this.processCode = processCode;
 }

 public List<ProcessCodeRec> getProcessCodes() {
  if(processCodes == null){
   processCodes = sysBuff.getProcessCodes();
   LOGGER.log(INFO, "Process codes frpm Sys buff {0}", processCodes);
  }
  return processCodes;
 }

 public void setProcessCodes(List<ProcessCodeRec> processCodes) {
  this.processCodes = processCodes;
 }

 public ProcessCodeRec getProcessCodeSel() {
  return processCodeSel;
 }

 public void setProcessCodeSel(ProcessCodeRec processCodeSel) {
  this.processCodeSel = processCodeSel;
 }
 
 @PostConstruct
 private void init(){
  if(processCode == null){
   processCode = new ProcessCodeRec();
  }
 }
 
 public void onAddProcCodeSave(){
  LOGGER.log(INFO, "onAddProcCodeSave called  with id {0}", processCode.getId());
  processCode.setCreatedBy(this.getLoggedInUser());
  processCode.setCreatedDate(new Date());
  processCode = this.sysBuff.processCodeUpdate( processCode, getView());
  String msgHdr = responseForKey("procCd");
  if(processCode.getId() != null){
   String msg = this.responseForKey("procCdCr") + processCode.getName();
   MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
   processCode = new ProcessCodeRec();
   PrimeFaces rCtx = PrimeFaces.current();
   rCtx.ajax().update("procCodeCrFrm");
  }else{
   String msg = this.errorForKey("procCodeCr") + processCode.getName();
   MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
  }
  
 }
 
 public void onEditDlg(){
  PrimeFaces pf = PrimeFaces.current();
  
  pf.ajax().update("editProcPg");
  pf.executeScript("PF('editProcCdWv').show()");
 }
 
 public void onEditSave(){
  LOGGER.log(INFO, "onEditSAve called {0}", processCodeSel);
  processCodeSel.setChangedBy(this.getLoggedInUser());
  processCodeSel.setChangedDate(new Date());
  processCodeSel = sysBuff.processCodeUpdate(processCodeSel, getView());
  ListIterator<ProcessCodeRec> li = this.processCodes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   ProcessCodeRec rec = li.next();
   LOGGER.log(INFO, "processCodeSel id {0} rec id {1}", new Object[]{processCodeSel.getId(),rec.getId()});
   if(Objects.equals(rec.getId(), processCodeSel.getId())){
    li.set(processCodeSel);
    found = true;
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("prCodeTbl");
  pf.executeScript("PF('editProcCdWv').hide()");
 }
 
 public void onEditReset(){
  processCodeSel = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editProcPg");
  pf.executeScript("PF('editProcCdWv').hide()");
 }
 
 public List<ModuleRec> onModuleComplete(String input){
  LOGGER.log(INFO, "onModuleComplete called with {0}", input);
  
  if(StringUtils.isBlank(input)){
   return sysBuff.getModules();
  }else{
   List<ModuleRec> modLst = sysBuff.getModules();
   List<ModuleRec> ret = new ArrayList<>();
   if(modLst == null || modLst.isEmpty()){
    return null;
   }
   for(ModuleRec curr:modLst){
    if(StringUtils.startsWith(curr.getName(), input)){
     ret.add(curr);
    }
   }
   return ret;
  }
 }
}
