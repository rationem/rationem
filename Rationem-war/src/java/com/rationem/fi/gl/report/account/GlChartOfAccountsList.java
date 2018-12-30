/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.account;

import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;


/**
 *
 * @author user
 */
public class GlChartOfAccountsList extends BaseBean {
  private static final Logger LOGGER =    Logger.getLogger(GlChartOfAccountsList.class.getName());

  @EJB
  private SysBuffer sysBuff;
  
  @EJB
  private GlAccountManager glAcntMgr;
  
  private List<ChartOfAccountsRec> chartList;
  private ChartOfAccountsRec chartSel;
  private List<FiGlAccountBaseRec> chartAccounts;
 /**
  * Creates a new instance of GlChartOfAccountsList
  */
 public GlChartOfAccountsList() {
 }
 
 @PostConstruct
 private void init(){
  chartList = sysBuff.getChartsOfAccounts();
  if(chartList == null || chartList.isEmpty()){
   MessageUtil.addWarnMessage("glAcntNoCoa", "errorText");
   return;
  }
  chartSel = chartList.get(0);
  chartAccounts = glAcntMgr.getGlAccountsForChart(chartSel);
  
 }

 public List<FiGlAccountBaseRec> getChartAccounts() {
  return chartAccounts;
 }

 public void setChartAccounts(List<FiGlAccountBaseRec> chartAccounts) {
  this.chartAccounts = chartAccounts;
 }

 
 public List<ChartOfAccountsRec> getChartList() {
  return chartList;
 }

 public void setChartList(List<ChartOfAccountsRec> chartList) {
  this.chartList = chartList;
 }

 public ChartOfAccountsRec getChartSel() {
  return chartSel;
 }

 public void setChartSel(ChartOfAccountsRec chartSel) {
  this.chartSel = chartSel;
 }

 
 
 
 
}
