/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.account;

import com.rationem.util.BaseBean;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author Chris
 */
public class GlAccountList extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger("accounts.fi.gl.GlAccountList");
 
 @EJB 
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glMgr;
 
 private CompanyBasicRec comp;
 
 private List<FiGlAccountCompRec> glcompAcs;
 private List<FiGlAccountCompRec> glcompAcsFiltered;
 private FiGlAccountCompRec glAcExpanded;
 private FiBsAccountRec glBsAcnt;
 private FiPlAccountRec glPlAcnt; 
 
 

 /**
  * Creates a new instance of GlAccountList
  */
 public GlAccountList() {
 }
 
 @PostConstruct
 private void init(){
  comp = this.getCompList().get(0);
  glcompAcs = glMgr.getCompanyAccounts(comp);
  LOGGER.log(INFO, "Number of company accounts {0}", glcompAcs.size());
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public FiGlAccountCompRec getGlAcExpanded() {
  return glAcExpanded;
 }

 public void setGlAcExpanded(FiGlAccountCompRec glAcExpanded) {
  this.glAcExpanded = glAcExpanded;
 }

 public FiBsAccountRec getGlBsAcnt() {
  return glBsAcnt;
 }

 public void setGlBsAcnt(FiBsAccountRec glBsAcnt) {
  this.glBsAcnt = glBsAcnt;
 }

 public List<FiGlAccountCompRec> getGlcompAcs() {
  return glcompAcs;
 }

 public void setGlcompAcs(List<FiGlAccountCompRec> glcompAcs) {
  this.glcompAcs = glcompAcs;
 }

 public List<FiGlAccountCompRec> getGlcompAcsFiltered() {
  return glcompAcsFiltered;
 }

 public void setGlcompAcsFiltered(List<FiGlAccountCompRec> glcompAcsFiltered) {
  this.glcompAcsFiltered = glcompAcsFiltered;
 }

 public FiPlAccountRec getGlPlAcnt() {
  return glPlAcnt;
 }

 public void setGlPlAcnt(FiPlAccountRec glPlAcnt) {
  this.glPlAcnt = glPlAcnt;
 }

 public void onGlAcExpand(ToggleEvent evt){
  LOGGER.log(INFO, "Row expand toggle {0}", evt.getData());
  glAcExpanded = (FiGlAccountCompRec)evt.getData();
  
 }
 
 public void onCompSel(SelectEvent evt){
  LOGGER.log(INFO, "onCompSel called with {0}", evt.getObject());
  comp = (CompanyBasicRec)evt.getObject();
  glcompAcs = glMgr.getCompanyAccounts(comp);
  PrimeFaces.current().ajax().update("glAcntLstCompFrm:glAcslst");
 }
 
 
}
