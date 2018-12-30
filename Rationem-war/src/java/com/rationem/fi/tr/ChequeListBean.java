/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr;

import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.helper.ChequeListSelOpt;
import com.rationem.helper.comparitor.BankAccountCompByAcntNumber;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author user
 */
public class ChequeListBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ChequeListBean.class.getName());
 public static final int SELECT = 0; 
 public static final int LIST = 1; 
 
 @EJB
 private BankManager bankMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private List<BankAccountCompanyRec> bnkAcntComps;
 private NumberRangeChequeRec chequeBook;
 private List<NumberRangeChequeRec> chequeBooks;
 private ChequeListSelOpt chqSelOpts;
 private List<DocBankLineChqRec> cheques;
 private int step;
 

 /**
  * Creates a new instance of ChequeBookList
  */
 public ChequeListBean() {
 }
 
 
 @PostConstruct
 private void init(){
  LOGGER.log(INFO, "Init called view {0}", this.getViewSimple());
  step = ChequeListBean.SELECT;
  chqSelOpts = new ChequeListSelOpt();
  chqSelOpts.setCashmntStat(ChequeListSelOpt.UN_CLEARED);
  chqSelOpts.setCompSel(getCompList().get(0));
  bnkAcntComps = this.acntsForComp(chqSelOpts.getCompSel());
  if(bnkAcntComps != null){
   chqSelOpts.setBnkAcntComp(bnkAcntComps.get(0));
  }
  LOGGER.log(INFO,"chqSelOpts.getCompSel().getLocale() {0}" , chqSelOpts.getCompSel().getLocale());
 }

 private List<BankAccountCompanyRec> acntsForComp(CompanyBasicRec comp){
  List<BankAccountCompanyRec> retList = bankMgr.getBankAccountsForCompany(comp);
  Collections.sort(retList, new BankAccountCompByAcntNumber());
  return retList;
 }
 
 

 public List<BankAccountCompanyRec> getBnkAcntComps() {
  return bnkAcntComps;
 }

 public void setBnkAcntComps(List<BankAccountCompanyRec> bnkAcntComps) {
  this.bnkAcntComps = bnkAcntComps;
 }

 
 
 
 public NumberRangeChequeRec getChequeBook() {
  return chequeBook;
 }

 public void setChequeBook(NumberRangeChequeRec chequeBook) {
  this.chequeBook = chequeBook;
 }

 public List<NumberRangeChequeRec> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeChequeRec> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 public List<DocBankLineChqRec> getCheques() {
  return cheques;
 }

 public void setCheques(List<DocBankLineChqRec> cheques) {
  this.cheques = cheques;
 }

 
 public ChequeListSelOpt getChqSelOpts() {
  return chqSelOpts;
 }

 public void setChqSelOpts(ChequeListSelOpt chqSelOpts) {
  this.chqSelOpts = chqSelOpts;
 }
 
 
 public void onChequeListAction(){
  LOGGER.log(INFO, "onChequeListAction Called ");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  
  cheques = bankMgr.getChequesBySel(chqSelOpts);
  
  if(cheques == null || cheques.isEmpty()){
   MessageUtil.addWarnMessage("chqsNone", "errorText");
   rCtx.update("chqListFrm:mess");
   return;
  }
  if(step == ChequeListBean.SELECT){
   step = ChequeListBean.LIST;
  }else{
   step = ChequeListBean.SELECT; 
  }
  
  
  
  rCtx.update("chqListFrm");
 }
 
 public void onChqListToggle(ToggleEvent evt){
  LOGGER.log(INFO, "onChqListToggle called with {0} ", evt.getData());
  DocBankLineChqRec ln = (DocBankLineChqRec)evt.getData();
  LOGGER.log(INFO, "filine  {0} ", new Object[]{ln.getDocFiLine()});
  if(ln.getDocFiLine() == null || ln.getDocFiLine().getDocHeaderBase() == null ){
   List<String> fields = new ArrayList<>();
   fields.add("docFi");
   docMgr.gtDocBankChqExtrFlds(ln, fields);
  }
  
 }

 @Override
 public int getStep() {
  return step;
 }

 @Override
 public void setStep(int step) {
  this.step = step;
 }
 public void onValidateChqNumTo(FacesContext fc, UIComponent comp, Object val){
  LOGGER.log(INFO, "onValidateChqNumTo called with ctx {0} comp {1} obj {2}", new Object[]{fc,comp,val});
  LOGGER.log(INFO, "chqSelOpts.getChqNumFr() {0} val {1}", new Object[]{chqSelOpts.getChqNumFr(), val.getClass().getSimpleName()});
  if((val.getClass().getSimpleName().equals("String") && StringUtils.isBlank((CharSequence)val)) && StringUtils.isBlank(chqSelOpts.getChqNumFr())){
   ((EditableValueHolder)comp).setValid(true); 
   return;
  }
  if(val != null && StringUtils.isBlank(chqSelOpts.getChqNumFr()) ){
   ((EditableValueHolder)comp).setValid(false);
   MessageUtil.addErrorMessage("chqNumToFrPt", "validationText");
  }else {
   Integer chqNumFr = Integer.parseInt(chqSelOpts.getChqNumFr());
   Integer chqNumTo = Integer.parseInt((String)val);
   if(chqNumTo.compareTo(chqNumFr) < 0){
    ((EditableValueHolder)comp).setValid(false);
   String msg = this.validationForKey("chqNumToFr");
   MessageUtil.addErrorMessage("chqNumToFr", "validationText");
   } else {
    ((EditableValueHolder)comp).setValid(true); 
   }
   
  }
 }
 public void onValidateChqNumFr(FacesContext fc, UIComponent comp, Object val){
  LOGGER.log(INFO, "onValidateChqNumFr called");
  LOGGER.log(INFO, "chqSelOpts.getChqNumTo() {0}", chqSelOpts.getChqNumTo());
  if(StringUtils.isBlank(chqSelOpts.getChqNumTo())){
   ((EditableValueHolder)comp).setValid(true); 
   return;
  }
  Integer fromVal = Integer.parseInt((String)val);
  Integer toVal = Integer.parseInt(chqSelOpts.getChqNumTo());
  if(toVal.compareTo(fromVal) < 0){
   ((EditableValueHolder)comp).setValid(false);
  }
  
 }
 
 public void onValidateDateFr(FacesContext fc, UIComponent comp, Object val){
  LOGGER.log(INFO, "onValidateDateFr Date validate", val);
  if(chqSelOpts.getChqIssueTo() == null){
   ((EditableValueHolder)comp).setValid(true);
   return;
  }
  
  LOGGER.log(INFO, "val class {0}", val.getClass().getSimpleName());
  
 }
 
 
 public void postProcessXLS2(Object document) {
  super.postProcessXLS(document);
  Workbook workbook = (HSSFWorkbook) document;
  Sheet sheet = workbook.getSheetAt(0);
  Row header = sheet.getRow(0);
  //HSSFWorkbook wb = (HSSFWorkbook) document;
  //HSSFSheet sheet = wb.getSheetAt(0);
  //HSSFRow header = sheet.getRow(0);
  Iterator<Row> rowIterator = sheet.iterator();
  while(rowIterator.hasNext()) {
   Row row = rowIterator.next();
   LOGGER.log(INFO, "Row num {0}", row.getRowNum());
   if(row.getRowNum() == 0){
    continue;
   }
   Iterator<Cell> cellIterator = row.cellIterator();
   while(cellIterator.hasNext()) {
    Cell currCell = cellIterator.next();
    LOGGER.log(INFO, "Cell index {0} type {1}", new Object[]{currCell.getColumnIndex(),currCell.getCellType()});
    if(currCell.getColumnIndex() == 2){
     currCell.setCellType(CellType.NUMERIC);
     CellStyle cellStyle = currCell.getCellStyle();
     cellStyle.setAlignment(HorizontalAlignment.RIGHT);
     //cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    }
    LOGGER.log(INFO, "Cell index {0} type {1}", new Object[]{currCell.getColumnIndex(),currCell.getCellType()});
   }
    
  }
 }
 
 
 

 
 
 
 
 
}
