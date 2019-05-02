/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ma.costCentre;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.DateFormatConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author user
 */
public class CostAcnt extends BaseBean {
 private static final Logger logger = Logger.getLogger(CostAcnt.class.getName());
 
 private boolean actsCreated;
 
 private List<CostAccountDirectRec> accounts;
 private CostAccountDirectRec accountSel;

 private CompanyBasicRec compSel;
 private List<CostCentreRec> costCentList;
 private CostCentreRec costCentSel;
 private PartnerRoleRec emplRole;
 private List<PartnerPersonRec> employees;
 
 @EJB
 private CostCentreMgr costCentMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private MasterDataManager mastDataMgr; 
 

 /**
  * Creates a new instance of CostAcnt
  */
 public CostAcnt() {
 }
 
 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  emplRole = sysBuff.getPartnerRoleByCode("EMPL");
  logger.log(INFO, "emplRole from sys buff {0}", emplRole);
  compSel = getCompList().get(0);
  costCentList = costCentMgr.getCostCentresForCompany(compSel);
  logger.log(INFO, "costCentList {0}", costCentList);
  if(costCentList != null && !costCentList.isEmpty()){
   costCentSel = costCentList.get(0);
   logger.log(INFO, "costCentSel accounts {0}", costCentSel.getCostAccountDirectAcs());
   if(costCentSel.getCostAccountDirectAcs() == null || costCentSel.getCostAccountDirectAcs().isEmpty()){
    // check db for cost lines
    List<CostAccountDirectRec> acts = this.costCentMgr.getCostAccountsByCostCent(costCentSel);
    logger.log(INFO, "Accounts from CostCentMgr {0}", acts);
    costCentSel.setCostAccountDirectAcs(acts);
    logger.log(INFO, "Number of cost acsnts {0}", acts);
    
   }
  }
  actsCreated = false;
 }

 public List<CostAccountDirectRec> getAccounts() {
  return accounts;
 }

 public void setAccounts(List<CostAccountDirectRec> accounts) {
  this.accounts = accounts;
 }

 public CostAccountDirectRec getAccountSel() {
  return accountSel;
 }

 public void setAccountSel(CostAccountDirectRec accountSel) {
  this.accountSel = accountSel;
 }
 
 
 public boolean isActsCreated() {
  return actsCreated;
 }

 public void setActsCreated(boolean actsCreated) {
  this.actsCreated = actsCreated;
 }

 

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<CostCentreRec> getCostCentList() {
  return costCentList;
 }

 public void setCostCentList(List<CostCentreRec> costCentList) {
  this.costCentList = costCentList;
 }

 public CostCentreRec getCostCentSel() {
  return costCentSel;
 }

 public void setCostCentSel(CostCentreRec costCentSel) {
  this.costCentSel = costCentSel;
 }
 
 public void onContextMenu(SelectEvent evt){
  accountSel = (CostAccountDirectRec)evt.getObject();
 }
 
 public List<CostCentreRec> onCostCentreComplete(String input){
  if(input == null || input.isEmpty()){
   return costCentList;
  }
  List<CostCentreRec> retList = new ArrayList<>();
  for(CostCentreRec cc:this.costCentList){
   if(cc.getRefrence().startsWith(input)){
    retList.add(cc);
   }
  }
  return retList;
 }
 
 public void onCostAcntDispDlg(){
  logger.log(INFO, "Displ Dlg called account {0}", this.accountSel.getRef());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("displAcntFrm");
  pf.executeScript("PF('dispDlgWv').show();");
 }
 
 public void onCostAcntUpdtDlg(){
  logger.log(INFO, "Update Dlg called account {0}", this.accountSel.getRef());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("updtAcntFrm");
  pf.executeScript("PF('updtDlgWv').show();");
 }
 
 public void onCreateAcntsBtn(){
  logger.log(INFO, "onCreateAcntsBtn called");
  this.costCentSel = this.costCentMgr.autoAddCostAccounts(costCentSel, this.getLoggedInUser(), getView());
  logger.log(INFO, "Number of accounts for cost centre {0}", costCentSel.getCostAccountDirectAcs().size());
  this.actsCreated = true;
  MessageUtil.addInfoMessageVar1("maCostAcsCreated", "blacResponse", 
     String.valueOf(costCentSel.getCostAccountDirectAcs().size()));
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("acsDt");
 }
 
 public List<PartnerPersonRec> onRespPers(String input){
  logger.log(INFO, "onRespPers called input {0} ees {1}", new Object[]{input,employees} );
  if(employees == null){
   employees = mastDataMgr.getPartnersInvForRole(emplRole);
   
  }
  logger.log(INFO, "ees after call to mast data mgr {0}", employees);
  if(employees == null){
   return null;
  }
  List<PartnerPersonRec> retList = new ArrayList<>();
  for(PartnerPersonRec p:employees){
   if(p.getFamilyName().startsWith(input)){
    retList.add(p);
   }
  }
  if(retList.isEmpty()){
   return null;
  }else{
   return retList;
  }
  
 }
 
 public void onTransferEdit(){
  logger.log(INFO, "onTransferEdit called");
  boolean foundCacnt = false;
  ListIterator<CostAccountDirectRec> caLi = costCentSel.getCostAccountDirectAcs().listIterator();
  while(caLi.hasNext() && !foundCacnt){
   CostAccountDirectRec curr = caLi.next();
   if(Objects.equals(curr.getId(), accountSel.getId())){
    accountSel.setChangedBy(this.getLoggedInUser());
    accountSel.setChangedOn(new Date());
    accountSel = this.costCentMgr.updateCostAccountDirectRec(accountSel, getView());
    logger.log(INFO, "cost account after update {0}", accountSel.getName());
    caLi.set(accountSel);
    foundCacnt = true;
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  if(foundCacnt){
   MessageUtil.addInfoMessageVar1("maCstAcntUpdt", "blacResponse", accountSel.getRef());
   pf.ajax().update("msgs");
   pf.ajax().update("acsDt");
   pf.executeScript("PF('updtDlgWv').hide();");
   
  }else{
   MessageUtil.addWarnMessageParam("maCostAcntUpdt", "errorText", accountSel.getRef());
   pf.ajax().update("msgs");
  }
 }
 @Override
 public void postProcessXLS(Object document) {
  super.postProcessXLS(document);
  HSSFWorkbook wb = (HSSFWorkbook) document;
  HSSFSheet sheet = wb.getSheetAt(0);
  Iterator<Row> rowIterator = sheet.iterator();
  while(rowIterator.hasNext()) {
   Row row = rowIterator.next();
   logger.log(INFO, "Row num {0}", row.getRowNum());
   if(row.getRowNum() == 0){
    continue;
   }
   Iterator<Cell> cellIterator = row.cellIterator();
   while(cellIterator.hasNext()) {
    Cell cell = cellIterator.next();
    if(cell.getColumnIndex() < 3){
     continue;
    }
    if(cell.getCellType() == Cell.CELL_TYPE_STRING){
     String val = cell.getStringCellValue();
     logger.log(INFO, "val {0}", val);
     DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy").withLocale(compSel.getLocale());  
     DateTime dt = fmt.parseDateTime(val);
     logger.log(INFO, "Date {0}", dt.toDate().toString());
     cell.setCellValue(dt.toDate());
     HSSFCellStyle dateStyle = wb.createCellStyle();
     String excelFormatPattern = DateFormatConverter.convert(compSel.getLocale(), "dd/MMM/yyyy");
     DataFormat poiFormat = wb.createDataFormat();
     dateStyle.setDataFormat(poiFormat.getFormat(excelFormatPattern));
     cell.setCellStyle(dateStyle);
    }
   }
  }
 }
}
