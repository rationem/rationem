/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr;

import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;
import org.primefaces.event.SelectEvent;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author user
 */
public class ChequePostBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ChequeSingleBean.class.getName());
 public static final int SELECT = 0;
 public static final int LIST = 1;
 
 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private BankManager bankMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private List<BankAccountCompanyRec> bankAccounts;
 private List<NumberRangeChequeRec> chequeBooks;
 private List<DocBankLineChqRec> chequeDocs;
 private List<DocTypeRec> docTypes;
 private FiDoclSelectionOpt selOpts;
 private int wfStep;
 private String ledgerCode;
 private double totalPosted;
 private String chqNumStart;
 

 /**
  * Creates a new instance of CheuePost
  */
 public ChequePostBean() {
 }
 
 @PostConstruct
 private void init(){
  selOpts = new FiDoclSelectionOpt ();
  if(getCompList() == null || getCompList().isEmpty()){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }
  selOpts.setComp(getCompList().get(0));
  selOpts.setLedgerCode("AP");
  bankAccounts = sysBuff.getCompBankAccounts(selOpts.getComp());
  if(bankAccounts != null && !bankAccounts.isEmpty()) {
   selOpts.setBnkAc(bankAccounts.get(0));
   chequeBooks = selOpts.getBnkAc().getChequeBooks();
   if(chequeBooks == null){
    BankAccountCompanyRec bnkAc = selOpts.getBnkAc();
    bnkAc = bankMgr.getChequeBooksForBankAcnt(bnkAc);
    chequeBooks = bnkAc.getChequeBooks();
    
    if(chequeBooks != null && !chequeBooks.isEmpty()){
     selOpts.setChkBk(chequeBooks.get(0));
     LOGGER.log(INFO, "Check book auto num {0}", selOpts.getChkBk().isAutoNum());
    }
   }
  }
  
  
 }

 public List<BankAccountCompanyRec> getBankAccounts() {
  return bankAccounts;
 }

 public void setBankAccounts(List<BankAccountCompanyRec> bankAccounts) {
  this.bankAccounts = bankAccounts;
 }

 public List<NumberRangeChequeRec> getChequeBooks() {
  return chequeBooks;
 }

 public void setChequeBooks(List<NumberRangeChequeRec> chequeBooks) {
  this.chequeBooks = chequeBooks;
 }

 
 public List<DocBankLineChqRec> getChequeDocs() {
  return chequeDocs;
 }

 public void setChequeDocs(List<DocBankLineChqRec> chequeDocs) {
  this.chequeDocs = chequeDocs;
 }

 public String getChqNumStart() {
  return chqNumStart;
 }

 public void setChqNumStart(String chqNumStart) {
  this.chqNumStart = chqNumStart;
 }

 
 
 public List<DocTypeRec> getDocTypes() {
  LOGGER.log(INFO, "Web getDocTypes called");
  if(docTypes == null || docTypes.isEmpty()){
   docTypes = sysBuff.getDocTypes();
     
  }
  return docTypes;
 }

 public void setDocTypes(List<DocTypeRec> docTypes) {
  this.docTypes = docTypes;
 }

 public String getLedgerCode() {
  return ledgerCode;
 }

 public void setLedgerCode(String ledgerCode) {
  this.ledgerCode = ledgerCode;
 }

 
 
 
 public FiDoclSelectionOpt getSelOpts() {
  return selOpts;
 }

 public void setSelOpts(FiDoclSelectionOpt selOpts) {
  this.selOpts = selOpts;
 }

 public double getTotalPosted() {
  return totalPosted;
 }

 public void setTotalPosted(double totalPosted) {
  this.totalPosted = totalPosted;
 }

 
 public int getWfStep() {
  return wfStep;
 }

 public void setWfStep(int wfStep) {
  this.wfStep = wfStep;
 }
 

public void onBankAcntSelect(SelectEvent evt){
 LOGGER.log(INFO, "onBankAcntSelect call with {0}", evt.getObject());
 BankAccountCompanyRec bnkAcnt = (BankAccountCompanyRec)evt.getObject();
 chequeBooks = bnkAcnt.getChequeBooks();
  if(chequeBooks == null){
   BankAccountCompanyRec bnkAc = selOpts.getBnkAc();
   bnkAc = bankMgr.getChequeBooksForBankAcnt(bnkAc);
   chequeBooks = bnkAc.getChequeBooks();
    
   if(chequeBooks != null && !chequeBooks.isEmpty()){
    selOpts.setChkBk(chequeBooks.get(0));
    LOGGER.log(INFO, "Check book auto num {0}", selOpts.getChkBk().isAutoNum());
   }
  }
  PrimeFaces pf = PrimeFaces.current();
  if(chequeBooks == null || chequeBooks.isEmpty()){
   MessageUtil.addErrorMessage("chkBk4BnkNo", "errorText");
  }
 
}

public void onCheckBookSelect(SelectEvent evt){
 LOGGER.log(INFO, "onCheckBookSelect {0}", evt.getObject());
 selOpts.setChkBk((NumberRangeChequeRec)evt.getObject());
 
}
 public List<DocTypeRec> onDocTypeComplete(String input){
 LOGGER.log(INFO, "onDocTypeComplete called with input {0}", input);
 List<DocTypeRec> retList = getDocTypes();
 LOGGER.log(INFO, "Web getDocTypes() returned {0}", retList);
 if(StringUtils.isBlank(input)){
  LOGGER.log(INFO, "Blank input returns {0}", retList);
  return retList;
 }
 ListIterator<DocTypeRec> li = retList.listIterator();
 while(li.hasNext()){
  DocTypeRec currDt = li.next();
  if(!StringUtils.startsWith(currDt.getName(), input)){
   li.remove();
  }
 }
 return retList;
}

public void onSaveAction(){
 // create cheque posting doc
 chequeDocs = this.bankMgr.postDocBankLinesCheque(selOpts,null,selOpts.getChkBk(),getLoggedInUser(),chqNumStart);
 LOGGER.log(INFO, "chequeDocs returned by bankMgr {0}", chequeDocs);
 wfStep = 1;
}
 
}
