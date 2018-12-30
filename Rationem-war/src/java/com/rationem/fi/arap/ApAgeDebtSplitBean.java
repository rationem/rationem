/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.company.CompanyApArRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;


/**
 *
 * @author user
 */
public class ApAgeDebtSplitBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ApAgeDebtSplitBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 private CompanyBasicRec compSel;
 private CompanyApArRec ageBucket;
 
 

 /**
  * Creates a new instance of ApAgeDebtSplit
  */
 public ApAgeDebtSplitBean() {
 }
 
 @PostConstruct()
 private void init(){
  if(getCompList() == null){
   MessageUtil.addClientErrorMessage("agedDebt:errMsg", "compsNone", "errorText");
   RequestContext.getCurrentInstance().update("agedDebt:errMsg");
   return;
  }
  compSel = getCompList().get(0);
  
  LOGGER.log(INFO, "compSel id {0}", compSel.getId());
  
  if(compSel.getCompApAr() == null){
   compSel = sysBuff.getCompBasicArApAging(compSel);
   LOGGER.log(INFO, "web after populate apAr settings {0}", this);
  }
  
    
 }

 public CompanyApArRec getAgeBucket() {
  return ageBucket;
 }

 public void setAgeBucket(CompanyApArRec ageBucket) {
  this.ageBucket = ageBucket;
 }

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }
 
 public void onUpdate(){
  LOGGER.log(INFO, "Update called");
  compSel.getCompApAr().setChangedBy(this.getLoggedInUser());
  compSel.getCompApAr().setChangedDate(new Date());
  compSel = sysBuff.updateCompArAP(compSel, getView());
  MessageUtil.addClientInfoMessage("agedDebt:okMsg", "apAgeSplOk", "blacResponse");
  RequestContext.getCurrentInstance().update("agedDebt:okMsg");
 }
 
}
