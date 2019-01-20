/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.fi.ArManager;
import com.rationem.util.ArAcntBalChkRec;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class ArAccountBalCheckBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ArAccountBalCheckBean.class.getName());
 
 @EJB
 private ArManager arMgr;
 
 
 private CompanyBasicRec compDefault;
 private CompanyBasicRec compSelected;
 private List<ArAcntBalChkRec> accountBalanceList;
 private List<ArAcntBalChkRec> accountBalancesSelected;
 

 /**
  * Creates a new instance of ArAccountBalanceCheck
  */
 
 @PostConstruct
 private void init(){
  compDefault = this.getCompList().get(0);
 }
 public ArAccountBalCheckBean() {
  super();
 }

 public CompanyBasicRec getCompSelected() {
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }

 public CompanyBasicRec getCompDefault() {
  return compDefault;
 }

 public void setCompDefault(CompanyBasicRec compDefault) {
  this.compDefault = compDefault;
 }
 
 

 public List<ArAcntBalChkRec> getAccountBalanceList() {
  return accountBalanceList;
 }

 public void setAccountBalanceList(List<ArAcntBalChkRec> accountBalanceList) {
  this.accountBalanceList = accountBalanceList;
 }

 public List<ArAcntBalChkRec> getAccountBalancesSelected() {
  return accountBalancesSelected;
 }

 public void setAccountBalancesSelected(List<ArAcntBalChkRec> accountBalancesSelected) {
  this.accountBalancesSelected = accountBalancesSelected;
 }
 
 public void onBalanceUpdate(){
  LOGGER.log(INFO,"onBalanceUpdate called selected list is {0}",this.accountBalancesSelected);
  ListIterator<ArAcntBalChkRec> li = accountBalanceList.listIterator();
  int numUpdates = 0;
  while(li.hasNext()){
   ArAcntBalChkRec currBal = li.next();
   boolean foundBal = false;
   ListIterator<ArAcntBalChkRec> selLi = accountBalancesSelected.listIterator();
   while(selLi.hasNext() && !foundBal){
    ArAcntBalChkRec selBal = selLi.next();
    if(Objects.equals(selBal.getAccountId(), currBal.getAccountId())){
     currBal.setAcntBal(currBal.getLineBal());
     
     selBal.setAcntBal(selBal.getLineBal());
     li.set(selBal);
     selLi.set(selBal);
     foundBal = true;
     numUpdates++;
    }
   }
  }
  LOGGER.log(INFO, "numUpdates {0}", numUpdates);
  if(numUpdates > 0){
   // TODO: update database
   List<String> updates = new ArrayList<>();
   boolean updtOk = this.arMgr.updateArAccountBal(accountBalanceList, this.getLoggedInUser(), this.getView());
   if(updtOk){
    MessageUtil.addClientInfoMessage("arActChkFrm:successGrl", "arAcntBalUpdt", "blacResponse");
    updates.add("arActChkFrm:successGrl");
    updates.add("arActChkFrm:acntList");
   }else{
    MessageUtil.addClientWarnMessage("arActChkFrm:errMsg", "arAcntBalUpdt", "errorText");
    updates.add("arActChkFrm:errMsg");
   }
   PrimeFaces.current().ajax().update(updates);
  }
 }
 
 public void onCompanySelect(SelectEvent evt){
  LOGGER.log(INFO, "onCompanySelect called with company ref {0}", (CompanyBasicRec)evt.getObject());
  compSelected = (CompanyBasicRec)evt.getObject();
  compDefault = compSelected;
  accountBalanceList = this.arMgr.getAccountsBalCheck((CompanyBasicRec)evt.getObject());
  PrimeFaces.current().ajax().update("arActChkFrm:acntList");
  PrimeFaces.current().ajax().update("arActChkFrm:updateBtn");
 }
}
