/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.doc;

import com.rationem.util.GenUtil;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.exception.BacException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import static java.util.logging.Level.INFO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.logging.Logger;


import org.primefaces.event.SelectEvent;

/**
 *
 * @author Chris
 */

public class DocRev extends DocCommon {
 private static final Logger logger =
            Logger.getLogger("accounts.fi.doc.DocRev");

 private boolean companySelected = false;
 private DocFiRec docSel;
 private List<DocFiRec> docSelList;
 private boolean docSelected = false;
 private DocFiRec fiDocument;
 
 /**
  * Creates a new instance of DocRev
  */
 public DocRev() {
 }

 public boolean isCompanySelected() {
  return companySelected;
 }

 public void setCompanySelected(boolean companySelected) {
  this.companySelected = companySelected;
 }

 
 public DocFiRec getDocSel() {
  return docSel;
 }

 public void setDocSel(DocFiRec docSel) {
  this.docSel = docSel;
 }

 public boolean isDocSelected() {
  return docSelected;
 }

 public void setDocSelected(boolean docSelected) {
  this.docSelected = docSelected;
 }

 public List<DocFiRec> getDocSelList() {
  return docSelList;
 }

 public void setDocSelList(List<DocFiRec> docSelList) {
  this.docSelList = docSelList;
 }

 @Override
 public DocFiRec getFiDoc() {
  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public void setFiDoc(DocFiRec fiDoc) {
  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 
 
 
 public List<DocFiRec> docComplete(String input){
 logger.log(INFO, "docComplete called with input{0}", input.getClass().getCanonicalName());
 if(getDocComp() == null){
  ListIterator<CompanyBasicRec> compLi = this.getCompList().listIterator();
  setDocComp(compLi.next());
 }
 logger.log(INFO, "comp {0}", getDocComp());
 if(input == null || input.isEmpty()){
  docSelList = getDocMgr().getAllFiDocsForComp(getDocComp(), false,getLoggedInUser(),getView());
 }else{
  try{
   input = input.trim();
  long docNumInpt = Long.parseLong(input);
  docSelList = getDocMgr().getFiDocsByDocNumPartial(getDocComp(), docNumInpt, false,getLoggedInUser(),getView());
  logger.log(INFO, "after call to get docs");
  }catch(NumberFormatException ex){
   GenUtil.addErrorMessage("Not a number");
  }
  
 }
 logger.log(INFO, "docComplete returns {0}", docSelList);
 return docSelList;
}
 
 public void onDocSelectCompl(SelectEvent evt){
  docSel = (DocFiRec)evt.getObject();
  logger.log(INFO, "Selected doc has compy id {0}", docSel.getCompany().getId());
  logger.log(INFO, "Selected doc company {0}", evt);
  fiDocument = new DocFiRec();
  fiDocument.setCompany(docSel.getCompany());
  fiDocument.setDocumentDate(docSel.getDocumentDate());
  fiDocument.setPostingDate(docSel.getPostingDate());
  fiDocument.setFisPeriod(docSel.getFisPeriod());
  fiDocument.setFisYear(docSel.getFisYear());
  fiDocument.setDocHdrText(docSel.getDocHdrText());
  fiDocument.setDocType(docSel.getDocType());
  fiDocument.setPartnerRef(docSel.getPartnerRef());
  fiDocument.setNotes(docSel.getNotes());
  docSelected = true;
  companySelected = true;
 }
 public void onPostDateSelect(SelectEvent event){
  Date postDt = (Date)event.getObject();
  FiscalPeriodYearRec perYr = getSysBuff().getCompFiscalPeriodYearForDate(this.getDocComp(), postDt);
  fiDocument.setFisPeriod(perYr.getPeriod());
  fiDocument.setFisYear(perYr.getYear());
  
 }
 public void onRevDoc(){
  logger.log(INFO, "Selected doc {0} rev doc {1}", new Object[]{docSel,fiDocument});
  try{
   fiDocument = this.getDocMgr().reverseDoc(docSel, fiDocument, this.getLoggedInUser(), this.getView());
   String msg = this.responseForKey("docRec")+String.valueOf(fiDocument.getDocNumber());
   GenUtil.addInfoMessage(msg);
   fiDocument = null;
   docSel = null;
   setDocSelected(false);
  }catch(BacException ex){
   String msg = this.errorForKey("docReverse") + ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
  }
 }
}
