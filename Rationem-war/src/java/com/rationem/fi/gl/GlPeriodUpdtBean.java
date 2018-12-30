/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl;

import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Objects;
//import com.sun.org.apache.bcel.internal.util.Objects;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.ObjectUtils;

/**
 *
 * @author user
 */
public class GlPeriodUpdtBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(GlAccountBean.class.getSimpleName());
 
 @EJB
 private CompanyManager compMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private CompanyBasicRec compSel;
 private List<CompPostPerRec> postPeriods;
 private CompPostPerRec  postPeriodSel;

 /**
  * Creates a new instance of GlPeriodUpdtBean
  */
 public GlPeriodUpdtBean() {
 }
 
 @PostConstruct
 private void init(){
  if(getCompList() == null || getCompList().isEmpty() ){
   MessageUtil.addClientErrorMessage("errMsg", "compsNone", "errotText");
   return;
  }
  compSel = getCompList().get(0);
  if(compSel.getCompanyPostingPeriods() == null || compSel.getCompanyPostingPeriods().isEmpty()){
   compSel = sysBuff.getCompAvailPostPeriod(compSel); 
  }
  postPeriods = compSel.getCompanyPostingPeriods();
  
 }

 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<CompPostPerRec> getPostPeriods() {
  return postPeriods;
 }

 public void setPostPeriods(List<CompPostPerRec> postPeriods) {
  this.postPeriods = postPeriods;
 }

 public CompPostPerRec getPostPeriodSel() {
  return postPeriodSel;
 }

 public void setPostPeriodSel(CompPostPerRec postPeriodSel) {
  this.postPeriodSel = postPeriodSel;
 }
 
 public void onTransfPostPer(){
  
  boolean found = false;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  ListIterator<CompPostPerRec> li = postPeriods.listIterator();
  while(li.hasNext() && !found){
   CompPostPerRec curr = li.next();
   if(Objects.equals(curr.getId(), postPeriodSel.getId())){
    postPeriodSel.setChangedBy(this.getLoggedInUser());
    postPeriodSel.setChangedDate(new Date());
    postPeriodSel = this.sysBuff.updatePostPerRec(postPeriodSel, compSel, getView());
    if(postPeriodSel == null){
     continue;
    }
    li.set(postPeriodSel);
    rCtx.update("glPerOpenClose:postPeriodTbl");
    found = true;
    
   }
  }
  
  if(found){
   rCtx.execute("PF('editDlgWv').hide();");
  }else{
   MessageUtil.addClientWarnMessage("postPerEdFrm:dlgErrMsg", "glPostPerUpdtNo", "validationText");
   rCtx.update("postPerEdFrm:dlgErrMsg");
  }
 }
 
 
}
