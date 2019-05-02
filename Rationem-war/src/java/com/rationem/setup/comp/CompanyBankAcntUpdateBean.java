/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.tr.PaymentMediumManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 *
 * @author user
 */
public class CompanyBankAcntUpdateBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(CompanyBankAcntUpdateBean.class.getName());
 
 @EJB
 private BankManager bankMgr;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private PaymentMediumManager chqMediumMgr;
 
 
 private List<BankAccountCompanyRec> bnkAcntCompList;
 private BankAccountCompanyRec bnkAcntCompSel;
 private List<FiGlAccountCompRec> glAccounts;

 /**
  * Creates a new instance of CompanyBankAcntUpdateBean
  */
 public CompanyBankAcntUpdateBean() {
 }
 
 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "CompanyBankAcntUpdateBean init called");
  bnkAcntCompList = bankMgr.getBankAccntsCompAll();
  
 }

 public List<BankAccountCompanyRec> getBnkAcntCompList() {
  return bnkAcntCompList;
 }

 public void setBnkAcntCompList(List<BankAccountCompanyRec> bnkAcntCompList) {
  this.bnkAcntCompList = bnkAcntCompList;
 }

 public BankAccountCompanyRec getBnkAcntCompSel() {
  return bnkAcntCompSel;
 }

 public void setBnkAcntCompSel(BankAccountCompanyRec bnkAcntCompSel) {
  this.bnkAcntCompSel = bnkAcntCompSel;
  
 }

 public List<FiGlAccountCompRec> getGlAccounts() {
  return glAccounts;
 }

 public void setGlAccounts(List<FiGlAccountCompRec> glAccounts) {
  this.glAccounts = glAccounts;
 }
 
 
 /**
  *  Gets the bank GL accounts that can be used by Company bank account
  * @param input
  * @return 
  */
 public List<FiGlAccountCompRec> onGlAccountBankCompl(String input){
  
  if(StringUtils.isBlank(input)){
   if(glAccounts == null || glAccounts.isEmpty()){
    AccountTypeRec acntTy = this.sysBuff.getAcntTypeByProcCode("GL_ACNT_BNK_CSH");
    glAccounts = glAcntMgr.getCompanyAcntsByAcntTy(this.bnkAcntCompSel.getComp(), acntTy);
   }
   return glAccounts;
  }else{
   List retList;
   if (glAccounts != null && !glAccounts.isEmpty()){
    retList = glAccounts;
   
    ListIterator<FiGlAccountCompRec> li = retList.listIterator();
    while(li.hasNext()){
     FiGlAccountCompRec curr = li.next();
     if(!StringUtils.startsWith(curr.getCoaAccount().getRef(), input)){
      li.remove();
     }
    }
    return retList;
   }else{
    return null;
   }
   
  }
 }
  
 public List<ChequeTemplateRec> onChequeTemplateComplete(String input){
  
  List<ChequeTemplateRec> retList;
  
  if(StringUtils.isBlank(input)){
   retList = chqMediumMgr.getChqTemplatesAll();
  }else{
   input = StringUtils.remove(input, "%");
   input = StringUtils.appendIfMissing(input, "%");
   retList = chqMediumMgr.getChqTemplByRef(input);
  }
  
   
   return retList;
  }
 
 public void onContextMenuEdit(SelectEvent evt){
  LOGGER.log(INFO, "onContextMenuEdit called with {0}", evt.getObject());
  this.bnkAcntCompSel = (BankAccountCompanyRec)evt.getObject();
  List<String> updts = new ArrayList<>();
  updts.add("updateBnkAc");
  
  PrimeFaces.current().ajax().update(updts);
 }
 
 public void onEditTransfer(){
  LOGGER.log(INFO, "onEditTransfer called");
  LOGGER.log(INFO, "uncleared GL accounts {0}",bnkAcntCompSel.getUnclearedGlAccounts());
  bnkAcntCompSel.setUpdatedBy(this.getLoggedInUser());
  bnkAcntCompSel.setUpdatedOn(new Date());
  bnkAcntCompSel = bankMgr.updateBankAccountCompany(bnkAcntCompSel, getView());
  MessageUtil.addClientInfoMessage("chqTemplUptFrm:bnkUpdated", "trBnkAcOwnUpdt", "blacResponse");
  PrimeFaces.current().executeScript("PF('updtDlgWv').hide()");
  PrimeFaces.current().ajax().update("chqTemplUptFrm:bnkUpdated");
 }
 
 
}
