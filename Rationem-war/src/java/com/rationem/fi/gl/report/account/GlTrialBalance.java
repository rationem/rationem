/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.account;

import com.rationem.busRec.config.common.FundCategoryRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import com.rationem.util.helper.comparator.FiPerBalByGlAcntFiscPer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;

import static java.util.logging.Level.INFO;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
public class GlTrialBalance extends BaseBean{
 private static final Logger logger = Logger.getLogger(GlTrialBalance.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 private boolean incZeroBal = false;
 private List<FundCategoryRec> fndCategories;
 private boolean compSel = false;
 private CompanyBasicRec company;
 private Date periodDate;
 private FiscalPeriodYearRec fiscYrPer;
 private List<FiPeriodBalanceRec> acntBalList; 
 private double bfwdAmnt;
 private double periodDebitAmnt;
 private double periodCreditAmnt;
 private double balanceAmnt;
 private int minorUnit = 2;

 /**
  * Creates a new instance of GlTrialBalance
  */
 public GlTrialBalance() {
 }
 
 @PostConstruct
 private void init(){
  fiscYrPer = new FiscalPeriodYearRec();
  fndCategories = new ArrayList<>();
  String fndDescr;
  FundCategoryRec desig = new FundCategoryRec();
  desig.setDesignated(true);
  fndDescr = formTextForKey("restFndDesig");
  desig.setDescription(fndDescr);
  fndCategories.add(desig);
  FundCategoryRec restr= new FundCategoryRec();
  restr.setRestricted(true);
  fndDescr = formTextForKey("restFndRestr");
  restr.setDescription(fndDescr);
  fndCategories.add(restr);
  FundCategoryRec endown = new FundCategoryRec();
  endown.setRestricted(true);
  endown.setEndowment(true);
  fndDescr = formTextForKey("restFndCap");
  endown.setDescription(fndDescr);
  fndCategories.add(endown);
  FundCategoryRec inc = new FundCategoryRec();
  inc.setRestricted(true);
  inc.setEndowment(false);
  fndDescr = formTextForKey("restFndInc");
  inc.setDescription(fndDescr);
  fndCategories.add(inc);
  FundCategoryRec perm = new FundCategoryRec();
  perm.setRestricted(true);
  perm.setEndowment(true);
  perm.setPermanent(true);
  fndDescr = formTextForKey("restFndPerm");
  perm.setDescription(fndDescr);
  fndCategories.add(perm);
  FundCategoryRec exp = new FundCategoryRec();
  exp.setRestricted(true);
  exp.setEndowment(true);
  exp.setPermanent(false);
  fndDescr = formTextForKey("restFndExpend");
  exp.setDescription(fndDescr);
  fndCategories.add(exp);
  
  
 }

 
 public List<FiPeriodBalanceRec> getAcntBalList() {
  return acntBalList;
 }

 public void setAcntBalList(List<FiPeriodBalanceRec> acntBalList) {
  this.acntBalList = acntBalList;
 }

 public double getBfwdAmnt() {
  return bfwdAmnt;
 }

 public void setBfwdAmnt(double bfwdAmnt) {
  this.bfwdAmnt = bfwdAmnt;
 }

 public double getBalanceAmnt() {
  return balanceAmnt;
 }

 public void setBalanceAmnt(double balanceAmnt) {
  this.balanceAmnt = balanceAmnt;
 }

 
 public CompanyBasicRec getCompany() {
  return company;
 }

 public void setCompany(CompanyBasicRec company) {
  this.company = company;
 }

 
 public boolean isCompSel() {
  return compSel;
 }

 public void setCompSel(boolean compSel) {
  this.compSel = compSel;
 }

 public FiscalPeriodYearRec getFiscYrPer() {
  
  return fiscYrPer;
 }

 public void setFiscYrPer(FiscalPeriodYearRec fiscYrPer) {
  this.fiscYrPer = fiscYrPer;
 }
 

 public boolean isIncZeroBal() {
  return incZeroBal;
 }

 public void setIncZeroBal(boolean incZeroBal) {
  this.incZeroBal = incZeroBal;
 }

 public int getMinorUnit() {
  return minorUnit;
 }

 public void setMinorUnit(int minorUnit) {
  this.minorUnit = minorUnit;
 }

 public Date getPeriodDate() {
  return periodDate;
 }

 public void setPeriodDate(Date periodDate) {
  this.periodDate = periodDate;
 }

 public double getPeriodDebitAmnt() {
  return periodDebitAmnt;
 }

 public void setPeriodDebitAmnt(double periodDebitAmnt) {
  this.periodDebitAmnt = periodDebitAmnt;
 }

 public double getPeriodCreditAmnt() {
  return periodCreditAmnt;
 }

 public void setPeriodCreditAmnt(double periodCreditAmnt) {
  this.periodCreditAmnt = periodCreditAmnt;
 }
 
 
 
 public List<CompanyBasicRec> compComplete(String entry){
  List<CompanyBasicRec> compList = this.getCompList();
  if(entry == null || entry.isEmpty()){
   return compList;
  }else{
   List<CompanyBasicRec> compRet = new ArrayList<>();
   for(CompanyBasicRec curr: this.getCompList()){
    if(curr.getReference().startsWith(entry)){
     compRet.add(curr);
    }
   }
   return compRet;
  }
  
 }
 
 public List<FundRec> fundComplete(String input){
  if(!this.compSel){
   return null;
  }
  List<FundRec> funds = company.getFundList();
  if(funds == null || funds.isEmpty()){
   company = this.sysBuff.getCompFunds(company);
   funds = company.getFundList();
  }
  logger.log(Level.OFF, "funds {0}", funds);
  if(input == null || input.isEmpty()){
   return funds;
  }else{
   ListIterator<FundRec> fundLi = funds.listIterator();
   while(fundLi.hasNext()){
    FundRec curr = fundLi.next();
    if(!curr.getName().startsWith(input)){
     fundLi.remove();
    }
   }
   return funds;
  }
 }
 public List<FundCategoryRec> fundCatComplete(String input){
  
  
  if(input == null || input.isEmpty()){
   return this.fndCategories;
  }else{
   List<FundCategoryRec> retCats = new ArrayList<>();
   for(FundCategoryRec cat:fndCategories){
    if(cat.getDescription().startsWith(input)){
     retCats.add(cat);
    }
   }
   if(!retCats.isEmpty()){
    return retCats;
   }else{
    return null;
   }
   
           
  }
 }
 
 public void onBuildTbReport(){
  logger.log(INFO, "onBuildTbReport called");
  balanceAmnt = 0;
  bfwdAmnt = 0;
  periodCreditAmnt = 0;
  periodDebitAmnt = 0;
  List<FiPeriodBalanceRec> bals = glAcntMgr.getPeriodBalancesAct(company, fiscYrPer.getYear());
  List<FiPeriodBalanceRec> sumBals = new ArrayList<>();
  
  Collections.sort(bals,new FiPerBalByGlAcntFiscPer());
  int maxPer = fiscYrPer.getPeriod();
  String currGlRef = "none";
  FiPeriodBalanceRec currBal = null;
  ListIterator<FiPeriodBalanceRec> balsLi = bals.listIterator();
  while(balsLi.hasNext()){
   FiPeriodBalanceRec curr = balsLi.next();
   logger.log(INFO, "Gl ref {0} bal yr {1} per {2} debit {3} credit {4}", 
     new Object[]{curr.getFiGlAccountComp().getCoaAccount().getRef(),curr.getBalYear(),curr.getBalPeriod(),
    curr.getPeriodDebitAmount(),curr.getPeriodCreditAmount()
   });
   if (curr.getBalPeriod() >= maxPer){
    continue;
   }
   logger.log(INFO, "Curr bal ref {0} last ref {1}  ", new Object[]{
    curr.getFiGlAccountComp().getCoaAccount().getRef(),currGlRef
   });
   if(!curr.getFiGlAccountComp().getCoaAccount().getRef().equals(currGlRef) ){
    if(currBal != null){
     
     double closeBal = currBal.getBfwdDocAmount() + currBal.getPeriodDebitAmount() - currBal.getPeriodCreditAmount();
     currBal.setCfwdDocAmount(closeBal);
     sumBals.add(currBal);
    }
    currBal = new FiPeriodBalanceRec();
    currBal.setFiGlAccountComp(curr.getFiGlAccountComp());
    currBal.setBfwdDocAmount(curr.getBfwdDocAmount());
    currBal.setPeriodCreditAmount(curr.getPeriodCreditAmount());
    currBal.setPeriodDebitAmount(curr.getPeriodDebitAmount());
    currGlRef = curr.getFiGlAccountComp().getCoaAccount().getRef();
    balanceAmnt += curr.getCfwdDocAmount();
    bfwdAmnt +=  curr.getBfwdDocAmount();
    periodCreditAmnt += curr.getPeriodCreditAmount();
    periodDebitAmnt += curr.getPeriodDebitAmount();
            
   }
   
  }
  // set the GL account summary
  if(currBal != null){
   double closeBal = currBal.getBfwdDocAmount() + currBal.getPeriodDebitAmount() - 
          currBal.getPeriodCreditAmount();
   currBal.setCfwdDocAmount(closeBal);
   logger.log(INFO, "Final GL account {0}", currBal.getCfwdDocAmount());
   sumBals.add(currBal);
  }
  //acntBalList incZeroBal
  if(incZeroBal){
   List<FiGlAccountCompRec> glacs = glAcntMgr.getCompanyAccounts(company);
  Collections.sort(glacs,new FiGlAccountCompByRef());
  Collections.sort(sumBals,new FiPerBalByGlAcntFiscPer());
   acntBalList = new ArrayList<>();
   for(FiGlAccountCompRec compAc:glacs){
    ListIterator<FiPeriodBalanceRec> balLi = sumBals.listIterator();
    boolean sumFound = false;
    while(balLi.hasNext() && !sumFound){
     currBal = balLi.next();
     if(currBal.getFiGlAccountComp().getCoaAccount().getRef().equals(compAc.getCoaAccount().getRef())){
      // balance for account
      acntBalList.add(currBal);
      sumFound = true;
     }
    }
    if(!sumFound){
     FiPeriodBalanceRec zeroBal = new FiPeriodBalanceRec();
     zeroBal.setFiGlAccountComp(compAc);
     zeroBal.setBfwdDocAmount(0);
     zeroBal.setPeriodCreditAmount(0);
     zeroBal.setPeriodDebitAmount(0);
     zeroBal.setCfwdDocAmount(0);
     acntBalList.add(zeroBal);
    }
    
    
   }
   
  }else{
   acntBalList = sumBals;
  }
  logger.log(INFO, "acntBalList after call to gl Account mgr {0}", acntBalList);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(acntBalList == null || acntBalList.isEmpty()){
   MessageUtil.addWarnMessage("glReptTbNoBals", "validationText");
   rCtx.update("tbGenGr");
   
   
  }else{
   MessageUtil.addInfoMessage("glReptTbGen", "formText");
   rCtx.update("tbGenGr");
   rCtx.update("accordPnl");
   rCtx.execute("PF('accordWv').select(1)");
  }
 }
 public void onCompSelect(SelectEvent evt){
  logger.log(INFO, "onCompSelect called with comp id {0}", evt.getObject());
  company = (CompanyBasicRec)evt.getObject();
  logger.log(INFO, "Company Country {0}", company.getCountry().getFractionalLength());
  logger.log(INFO, "getMinorUnit {0}",company.getCurrency().getMinorUnit());
  minorUnit = company.getCurrency().getMinorUnit();
  compSel = true;
  Date curr = new Date();
  fiscYrPer = this.sysBuff.getCompFiscalPeriodYearForDate(company, curr);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("accordPnl");
  rCtx.update(updates);
 }
 
 @Override
 public void postProcessXLS(Object document) {
  Workbook workbook = (HSSFWorkbook) document;
  Sheet sheet = workbook.getSheetAt(0);
  Row header = sheet.getRow(0);
  //HSSFWorkbook wb = (HSSFWorkbook) document;
  //HSSFSheet sheet = wb.getSheetAt(0);
  //HSSFRow header = sheet.getRow(0);
  //HSSFCellStyle cellStyle = wb.createCellStyle();
  CellStyle backgroundStyle = workbook.createCellStyle();
  backgroundStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
  backgroundStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
  backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  //cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
  //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
  for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
    header.getCell(i).setCellStyle(backgroundStyle);
  }
  int numCols = header.getLastCellNum();
  for(int i=0; i < numCols;i++){
   sheet.autoSizeColumn(i);
  }
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
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        logger.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        logger.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
        
        break;
       case Cell.CELL_TYPE_STRING:
        logger.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        if(cell.getColumnIndex() > 1){
         String val = cell.getStringCellValue();
         double dbl = Double.parseDouble(val);
         cell.setCellValue(dbl);
         CellStyle cellStyleCurr = cell.getCellStyle();
         short sN = 4;
         cellStyleCurr.setDataFormat(sN);
         }
        break;
       case Cell.CELL_TYPE_FORMULA:
        logger.log(INFO, "Cell Formula {0}" , cell.getStringCellValue());
      }
    }
  }
}
}

