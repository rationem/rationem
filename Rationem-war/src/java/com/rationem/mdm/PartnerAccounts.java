/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.ejbBean.fi.ArManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class PartnerAccounts extends BaseBean {

 private static final Logger LOGGER = Logger.getLogger(PartnerAccounts.class.getName());
 
 @EJB
 private MasterDataManager ptnrMgr;
 
 @EJB
 private ArManager arMgr;
 
 @EJB
 private ApManager apMgr;
 
 private PartnerBaseRec ptnrBase;
 private List<ArAccountRec> arAccounts;
 private List<ApAccountRec> apAccounts;
 private ArAccountRec arAccntSel;
 private ArAccountRec apAccntSel;
 private double apTotal;
 private double arTotal;
 
 
 /**
  * Creates a new instance of PartNerAccounts
  */
 public PartnerAccounts() {
 }

 public PartnerBaseRec getPtnrBase() {
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
 }

 public List<ArAccountRec> getArAccounts() {
  return arAccounts;
 }

 public void setArAccounts(List<ArAccountRec> arAccountsarAccounts) {
  this.arAccounts = arAccountsarAccounts;
 }

 public ArAccountRec getArAccntSel() {
  return arAccntSel;
 }

 public void setArAccntSel(ArAccountRec arAccntSel) {
  this.arAccntSel = arAccntSel;
 }

 
 public double getArTotal() {
  return arTotal;
 }

 public void setArTotal(double arTotal) {
  this.arTotal = arTotal;
 }

 
 public List<ApAccountRec> getApAccounts() {
  return apAccounts;
 }

 public void setApAccounts(List<ApAccountRec> apAccounts) {
  this.apAccounts = apAccounts;
 }

 public ArAccountRec getApAccntSel() {
  return apAccntSel;
 }

 public void setApAccntSel(ArAccountRec apAccntSel) {
  this.apAccntSel = apAccntSel;
 }

 
 public double getApTotal() {
  return apTotal;
 }

 public void setApTotal(double apTotal) {
  this.apTotal = apTotal;
 }
 
 
 public String onPartnerArAccntAddMi(){
  LOGGER.log(INFO, "onPartnerArAccntAddMi called with account {0}", ptnrBase.getDisplayName());
  if(ptnrBase == null){
   MessageUtil.addClientWarnMessage("ptnrAcntsFrm:msgs", "ptnrReq4Acnt", "validationText");
   PrimeFaces.current().ajax().update("ptnrAcntsFrm:msgs");
   return null;
  }
  this.getSessionMap().put("partner", ptnrBase);
  
  //PrimeFaces.current().dialog().openDynamic("ptnrArAcntCrDlg");
  return "arActCr2";
 }
 
 public List<PartnerBaseRec> onPartnerComplete(String input){
  LOGGER.log(INFO, "onPartnerComplete called with input {0}", input);
  
  List<PartnerBaseRec> retList = null;
  if(StringUtils.isBlank(input)){
   retList = ptnrMgr.getPartnersAll();
  }else{
   
   retList = ptnrMgr.getPartnersByRef(input);
   LOGGER.log(INFO, "Get partners by name returned {0}", retList);
  }
  return retList;
 }
 
 public void onPartnerSelect(SelectEvent evt){
  LOGGER.log(INFO, "onPartnerSelect called with {0}", evt.getObject());
  ptnrBase = (PartnerBaseRec)evt.getObject();
  arAccounts = arMgr.getAccountsForPartner(ptnrBase);
  if(arAccounts != null){
   arTotal = 0;
   ListIterator<ArAccountRec> arLi = arAccounts.listIterator();
   while(arLi.hasNext()){
    ArAccountRec currArAcnt = arLi.next();
    arTotal += currArAcnt.getAccountBalance();
   }
  }
  
  apAccounts = apMgr.getApAccountsForPartner(ptnrBase);
  LOGGER.log(INFO, "apAccounts from apMgr {0}", apAccounts);
  if(apAccounts != null){
   apTotal = 0;
   for(ApAccountRec curr:apAccounts){
    LOGGER.log(INFO, "Account rerf {0} bal {1}", new Object[]{curr.getAccountCode(),curr.getAccountBalance()});
    LOGGER.log(INFO, "Min units {0}", curr.getCompany().getCurrency().getMinorUnit()); 
    apTotal += curr.getAccountBalance();
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updateIds= new ArrayList<>();
  updateIds.add("ptnrAcntsFrm:ptnrName");
  if(arAccounts != null){
   updateIds.add("ptnrAcntsFrm:arAcntsDt");
  }
  if(apAccounts != null){
   updateIds.add("ptnrAcntsFrm:apAcntsDt");
  }
  //updateIds.add("ptnrAcntsFrm:arAcntMenu");
  PrimeFaces.current().ajax().update(updateIds);
  
 }
 
 
}
