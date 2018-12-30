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
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class ProcessCodeBean extends BaseBean {
 private static final Logger logger = Logger.getLogger(ProcessCodeBean.class.getName());
 
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
  if(processCode == null){
   processCode = new ProcessCodeRec();
  }
  return processCode;
 }

 public void setProcessCode(ProcessCodeRec processCode) {
  this.processCode = processCode;
 }

 public List<ProcessCodeRec> getProcessCodes() {
  if(processCodes == null){
   processCodes = sysBuff.getProcessCodes();
   logger.log(INFO, "Process codes frpm Sys buff {0}", processCodes);
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
 
 
 public void onAddProcCodeSave(){
  
  processCode.setCreatedBy(this.getLoggedInUser());
  processCode.setCreatedDate(new Date());
  processCode = setup.addProcessCode(processCode, getView());
  String msgHdr = responseForKey("procCd");
  if(processCode.getId() != null){
   String msg = this.responseForKey("procCdCr") + processCode.getName();
   MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
   processCode = null;
   PrimeFaces rCtx = PrimeFaces.current();
   rCtx.ajax().update("procCodeCrFrm");
  }else{
   String msg = this.errorForKey("procCodeCr") + processCode.getName();
   MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
  }
  
 }
 
 public void onEditDlg(){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editProcPg");
  rCtx.execute("PF('editProcCdWv').show()");
 }
 
 public void onEditSave(){
  logger.log(INFO, "onEditSAve called {0}", processCodeSel);
  processCodeSel.setChangedBy(this.getLoggedInUser());
  processCodeSel.setChangedDate(new Date());
  processCodeSel = sysBuff.processCodeUpdate(processCodeSel, getView());
  ListIterator<ProcessCodeRec> li = this.processCodes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   ProcessCodeRec rec = li.next();
   logger.log(INFO, "processCodeSel id {0} rec id {1}", new Object[]{processCodeSel.getId(),rec.getId()});
   if(rec.getId() == processCodeSel.getId()){
    li.set(processCodeSel);
    found = true;
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("prCodeTbl");
  rCtx.execute("PF('editProcCdWv').hide()");
 }
 
 public void onEditReset(){
  processCodeSel = null;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editProcPg");
  rCtx.execute("PF('editProcCdWv').hide()");
 }
}
