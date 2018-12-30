/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.FiArPeriodBalanceRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.fi.ArManager;
import com.rationem.util.BaseBean;
import com.rationem.util.helper.comparator.ArAccountByRef;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;

import static java.util.logging.Level.INFO;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class ArAccntListBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ArAccntListBean.class.getSimpleName());
 
 @EJB
 private ArManager arMgr;
 
 private List<ArAccountRec> acntList;
 private List<FiArPeriodBalanceRec> perBalanceList;
 private ArAccountRec acntSel;
 private CompanyBasicRec compSel;
 private int yrSel;

 /**
  * Creates a new instance of ArAccntListBean
  */
 public ArAccntListBean() {
 }
 
@PostConstruct
 private void init(){
  if(getCompList() ==  null){
   return ;  
  }   
  compSel = getCompList().get(0);
  acntList = arMgr.getAccountsForCompany(compSel);
  if(acntList == null || acntList.isEmpty()){
   return;
  }
  Collections.sort(acntList, new ArAccountByRef());
  acntSel = acntList.get(0);
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(new Date());
  yrSel = cal.get(Calendar.YEAR);
  perBalanceList = arMgr.getPerBalsForAcnt(acntSel, yrSel);
 }

 public List<ArAccountRec> getAcntList() {
  return acntList;
 }

 public void setAcntList(List<ArAccountRec> acntList) {
  this.acntList = acntList;
 }

 public ArAccountRec getAcntSel() {
  return acntSel;
 }

 public void setAcntSel(ArAccountRec acntSel) {
  this.acntSel = acntSel;
 }

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<FiArPeriodBalanceRec> getPerBalanceList() {
  return perBalanceList;
 }

 public void setPerBalanceList(List<FiArPeriodBalanceRec> perBalanceList) {
  this.perBalanceList = perBalanceList;
 }

 public int getYrSel() {
  return yrSel;
 }

 public void setYrSel(int yrSel) {
  this.yrSel = yrSel;
 }
 
 public void onYearChange(ValueChangeEvent evt){
  LOGGER.log(Level.INFO, "onYearChange called with year {0}", evt.getNewValue());
 }
 
 public void onCompanySelect(SelectEvent evt){
  LOGGER.log(INFO, "onCompanySelect called with object {0}", evt.getObject().getClass().getSimpleName());
  compSel = (CompanyBasicRec)evt.getObject();
  acntList = arMgr.getAccountsForCompany(compSel);
  if(acntList == null || acntList.isEmpty()){
   return;
  }
  Collections.sort(acntList, new ArAccountByRef());

  PrimeFaces.current().ajax().update("arAcntListFrm:acntLstTbl");
  PrimeFaces.current().ajax().update("arAcntListFrm:compName");
 }
 
 
}
