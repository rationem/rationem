/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.fi;

import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocFiPartialRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.ejbBean.dataManager.DocumentDM;
import javax.ejb.EJB;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocFiTemplAccrPrePayRec;
import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineFiTemplGlRec;
import com.rationem.busRec.doc.DocLineFiTemplateRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocLineGlPartialRec;
import com.rationem.busRec.doc.DocVatSummary;
import com.rationem.busRec.doc.SalesPartFiLineRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.tax.GiftAidScheduleLine;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatReturnRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.ejbBean.dataManager.TreasuryDM;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.exception.BacException;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.helper.RestrictFundBalance;
import com.rationem.helper.TemplSelectOption;
import com.rationem.helper.comparitor.ApLineByDocNum;
import com.rationem.util.ApAgePaySel;
import com.rationem.util.ApLineSel;
import com.rationem.util.FundBalance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;



/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class DocumentManager {
  private static final Logger LOGGER = Logger.getLogger(DocumentManager.class.getName());

  @EJB
  DocumentDM docDM;
  
  @EJB
  CompanyManager compMgr;
  
  @EJB
  SysBuffer buffer;
  
  @EJB
  private CostCentreMgr costCentMgr;
  
  @EJB
  private ProgrammeMgr progMgr;
  
  @EJB
  private TreasuryDM treasuryDM;

  private DocLineGlRec DocLineFiTemplGlRecToDocLineGlRec(DocLineFiTemplGlRec p){
   DocLineGlRec gl = new DocLineGlRec();
   gl.setComp(p.getComp());
   gl.setCostCentre(p.getCostCentre());
   gl.setChangeBy(p.getChangeBy());
   gl.setChangeDate(p.getChangeDate());
   gl.setCreateBy(p.getCreateBy());
   gl.setCreateDate(p.getCreateDate());
   gl.setDocAmount(p.getHomeAmount());
   gl.setGlAccount(p.getGlAccount());
   gl.setHomeAmount(p.getHomeAmount());
   gl.setLineNum(p.getLineNum());
   gl.setLineText(p.getLineText());
   gl.setLineType(p.getLineType());
   gl.setNotes(p.getNotes());
   gl.setPostType(p.getPostType());
   gl.setProgramme(p.getProgramme());
   gl.setReference1(p.getReference1());
   gl.setReference2(p.getReference2());
   gl.setSortOrder(p.getSortOrder());
   gl.setVatCodeCompany(p.getVatCode());
   return gl;
  }
  
  private DocLineGlRec DocLineGlPartialRecToDocLineGlRec(DocLineGlPartialRec p){
   DocLineGlRec gl = new DocLineGlRec();
   gl.setComp(p.getComp());
   gl.setCostCentre(p.getCostCentre());
   gl.setChangeBy(p.getChangeBy());
   gl.setChangeDate(p.getChangeDate());
   gl.setCreateBy(p.getCreateBy());
   gl.setCreateDate(p.getCreateDate());
   gl.setDocAmount(p.getDocAmount());
   gl.setGlAccount(p.getGlAccount());
   gl.setHomeAmount(p.getHomeAmount());
   gl.setLineNum(p.getLineNum());
   gl.setLineText(p.getLineText());
   gl.setLineType(p.getLineType());
   gl.setNotes(p.getNotes());
   gl.setPostType(p.getPostType());
   gl.setProgramme(p.getProgramme());
   gl.setReference1(p.getReference1());
   gl.setReference2(p.getReference2());
   gl.setSortOrder(p.getSortOrder());
   gl.setVatCodeCompany(p.getVatCode());
   return gl;
  }
  
  private DocFiRec templateToFiDoc(DocFiTemplAccrPrePayRec tmpl, Date postDate, Date docDate, 
          Date taxDate, int mode){
   DocFiRec fi = new DocFiRec();
   fi.setChangedBy(tmpl.getChangedBy());
   fi.setChangedOn(tmpl.getChangedOn());
   fi.setCompany(tmpl.getCompany());
   fi.setCreatedBy(tmpl.getCreatedBy());
   fi.setCreateOn(tmpl.getCreateOn());
   fi.setDocHdrText(tmpl.getDocHdrText());
   fi.setDocType(tmpl.getDocType());
   fi.setDocumentDate(docDate);
   fi.setPostingDate(postDate);
   fi.setTaxDate(taxDate);
   fi.setDocumentDate(docDate);
   fi.setPostingDate(postDate);
   fi.setTaxDate(taxDate);
   fi.setFisPeriod(tmpl.getFisPeriod());
   fi.setFisYear(tmpl.getFisYear());
   fi.setNotes(tmpl.getNotes());
   fi.setPartnerRef(tmpl.getPartnerRef());
   
   switch(mode){
    case DocFiTemplAccrPrePayRec.REVERSING:
     TransactionTypeRec trTy = buffer.getTransactionTypeRecByCode("GLREVSALJNL");
     fi.setTransactionType(trTy);
     break;
    case DocFiTemplAccrPrePayRec.REVERSAL:
     trTy = buffer.getTransactionTypeRecByCode("GLREVSALJNL");
     fi.setTransactionType(trTy);
     break;
    case DocFiTemplAccrPrePayRec.RECURRING:
     trTy = buffer.getTransactionTypeRecByCode("GLRECJNL");
     fi.setTransactionType(trTy);
     break;
    case DocFiTemplAccrPrePayRec.REPEAT:
     trTy = buffer.getTransactionTypeRecByCode("GLRPTJNL");
     fi.setTransactionType(trTy);
     break;
     
   }
   
   
   
   
   List<DocLineFiTemplateRec> tmplLines = tmpl.getDocLines();
   List<DocLineBaseRec> fiLines = new ArrayList<>();
   if(tmplLines != null){
   for(DocLineFiTemplateRec curr : tmplLines){
    String simpleName = curr.getClass().getSimpleName();
    LOGGER.log(INFO, "Template line type {0}", simpleName);
    DocLineGlRec ln = DocLineFiTemplGlRecToDocLineGlRec((DocLineFiTemplGlRec)curr);
    if(tmpl.getTmplType() == DocFiTemplAccrPrePayRec.REVERSAL){
     PostTypeRec pt =ln.getPostType();
     PostTypeRec revPt = pt.getRevPostType();
     ln.setPostType(revPt);
    }
    ln.setDocHeaderBase(fi);
    fiLines.add(ln);
   }
   }
   fi.setDocLines(fiLines);
   return fi;
  }
  public long postStdJnl(DocFiRec doc, UserRec user, String page) {
    LOGGER.log(INFO, "Doc manager postStdJnl called with doc {0} usr {1}",
            new Object[]{doc,user});
    /*Collection<DocLineBaseRec> docLines = doc.getDocLines();
    Iterator<DocLineBaseRec> docLinesIt = docLines.iterator();
    while(docLinesIt.hasNext()){
      DocLineGlRec glLine = (DocLineGlRec)docLinesIt.next();
      
    }*/
    long docNum = docDM.postStdJnl(doc, page);
    return docNum;
  }
  
  public DocFiPartialRec postStdJnlPartial(DocFiPartialRec doc, String pg){
   LOGGER.log(INFO, "postStdJnlPartial call with changed by {0}", doc.getChangeBy());
   LOGGER.log(INFO, "postStdJnlPartial id {0}", doc.getId());
   
   doc = this.docDM.postStdJnlPartial(doc, pg);
   LOGGER.log(INFO, "docDM returns {0}", doc);
   return doc;
  }
    
  
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public DocFiRec postSaleInvoice(DocFiRec doc, List<SalesPartFiLineRec> lines,
         List<DocVatSummary> docVatSummary,
         List<RestrictFundBalance> fundBals,
         UserRec user, String source) 
         throws BacException {
  LOGGER.log(INFO, "postSaleInvoice called with invoice {0} user {1} source {2}", 
          new Object[]{doc,user,source});
  LineTypeRuleRec glLineTy = buffer.getLineTypeRuleByCode("GL");
  try{
   
   // get AR account
   
   // Check Cost Account Exists
   ListIterator<SalesPartFiLineRec> docLinesLi = lines.listIterator();
   while(docLinesLi.hasNext()){
    SalesPartFiLineRec line = docLinesLi.next();
     
    FiGlAccountCompRec glAct = line.getPartComp().getSalesAccount();
    LOGGER.log(INFO, "line.getCostCent() id {0}", line.getCostCent().getId());
    if(line.getCostCent().getId() != null){
     CostCentreRec costCent = line.getCostCent(); 
     LOGGER.log(INFO,"Doc manger call get cost ac");
     CostAccountDirectRec costAc = costCentMgr.getCostAccountByCostCentGlAc(glAct, costCent, 
            true,user,new Date(),source);
     if(costAc == null){
      throw new BacException("Could not create cost Account");
     }else{
      LOGGER.log(INFO,"costAc after get cost ac {0}",costAc);
     line.setCostAct(costAc);
     }
    }
    
    LOGGER.log(INFO, "DocMgr Set Program prog {0}",line.getProg());
    if(line.getProg() != null && line.getProg().getId()!= null &&  line.getProg().getId() > 0){
     ProgrammeRec prog = line.getProg();
     LOGGER.log(INFO, "prog id {0}",prog.getId());
     LOGGER.log(INFO, "Doc manger call to get program with user id {0}",user.getId());
    
     ProgramAccountRec progAct = progMgr.getProgramActForGlAct(glAct, prog, true, user, new Date(), source);
     LOGGER.log(INFO, "progMgr.getProgramActForGlAct returned {0}",progAct);
     if(progAct == null || progAct.getId() == null || progAct.getId() == 0){
      throw new BacException("Could not create program Account");
     }else{
      line.setProgAcnt(progAct);
     }
    }
    
    docLinesLi.set(line);
    LOGGER.log(INFO,"doc line has cost ac id {0}",line.getCostAct());
   }
   
   // set VAT summary
   LOGGER.log(INFO, "DocMgr Set VAT summary");
   VatReturnRec vatRet = null;
   if(docVatSummary != null && !docVatSummary.isEmpty()){
    VatRegSchemeRec regScheme = buffer.getRegScheme(doc.getCompany(), doc.getTaxDate());
    LOGGER.log(INFO, "regScheme found {0}", regScheme);
    vatRet = buffer.getVatReturn(regScheme, doc.getCompany(), doc.getTaxDate(), user, source);
   }
   //List<CostAccountDirectRec> costAc = costCentMgr.getCostAccount(null, null, true)
   DocFiRec docRec = docDM.postSalesInvoice(doc,lines, fundBals,docVatSummary,vatRet,  source);
   //DocFiRec docRec = new DocFiRec();
   LOGGER.log(INFO, "inv returned from DB layer {0} ext ref {1}", new Object[]{docRec,docRec.getPartnerRef()});
   // VAT info
  
   //sales information
   LOGGER.log(INFO, "Update sales info ");
   return docRec;
  }catch(BacException ex){
   LOGGER.log(INFO, "DB BACException error : {0}", ex.getLocalizedMessage());
   return null;
  }catch(Exception ex){
   LOGGER.log(INFO, "DB Other error : {0}", ex.getLocalizedMessage());
   return null;
  }
 }

 public DocFiRec postSaleCreditNote(DocFiRec doc, List<SalesPartFiLineRec> lines,
         List<DocLineArRec> alloc,
         List<DocVatSummary> docVatSummary,
         List<RestrictFundBalance> fundBals,
          String source) throws BacException {
  LOGGER.log(INFO, "postSaleCreditNote called with doc {0} lines {1} allocated {2} vat sum {3} "
          + "funds {4}",  new Object[]{doc,lines,alloc,docVatSummary,fundBals});
  
  try{
   
   // set document line types
   
   // Check Cost Account Exists
   
   ListIterator<SalesPartFiLineRec> docLinesLi = lines.listIterator();
   while(docLinesLi.hasNext()){
    SalesPartFiLineRec line = docLinesLi.next();
    LOGGER.log(INFO, "loop at line num {0}", line.getLineNum());
    
    
    
    
    FiGlAccountCompRec glAct = line.getPartComp().getSalesAccount();
    
    LOGGER.log(INFO, "Check getCostCent  {0} id {1}", 
      new Object[]{line.getCostCent(), line.getCostCent().getId()});
    if(line.getCostCent() != null && line.getCostCent().getId() != null ){
     CostCentreRec costCent = line.getCostCent(); 
     LOGGER.log(INFO,"Doc manger call get cost ac");
     CostAccountDirectRec costAc = 
       costCentMgr.getCostAccountByCostCentGlAc(glAct,costCent, true, doc.getCreatedBy(),
       doc.getCreateOn(),source);
    
     if(costAc == null){
      throw new BacException("Could not create cost Account");
     }else{
      LOGGER.log(INFO,"costAc after get cost ac {0}",costAc);
     line.setCostAct(costAc);
     }
    }
     LOGGER.log(INFO, "Check program {0} id {1}", 
      new Object[]{line.getProg(), line.getProg().getId()});
    if(line.getProg() != null && line.getProg().getId()!= null ){
     ProgrammeRec prog = line.getProg();
     LOGGER.log(INFO, "prog id {0}",prog.getId());
     LOGGER.log(INFO, "Doc manger call to get program with user id {0}", doc.getCreatedBy().getId());
    
     ProgramAccountRec progAct = progMgr.getProgramActForGlAct(glAct, prog, true, doc.getCreatedBy(), new Date(), source);
     LOGGER.log(INFO, "progMgr.getProgramActForGlAct returned {0}",progAct);
     if(progAct == null || progAct.getId() == null || progAct.getId() == 0){
      throw new BacException("Could not create program Account");
     }else{
      line.setProgAcnt(progAct);
     }
    }
    docLinesLi.set(line);
    
   }
   
   // set VAT summary
   VatReturnRec vatRet = null;
   LOGGER.log(INFO,"Doc manager VAT summary {0}", docVatSummary);
   if(docVatSummary != null && !docVatSummary.isEmpty()){
    VatRegSchemeRec regScheme = buffer.getRegScheme(doc.getCompany(), doc.getTaxDate());
    LOGGER.log(INFO, "regScheme found {0}", regScheme);
    LOGGER.log(INFO, "getVatReturn called with regscheme id {0} comp {1} tax date {2}", new Object[]{
     regScheme.getId(), doc.getCompany().getId(), doc.getTaxDate()
    });
    vatRet = buffer.getVatReturn(regScheme, doc.getCompany(), doc.getTaxDate(), doc.getCreatedBy(), source);
   }
   LOGGER.log(INFO, "About to call docDM.postSalesCreditNote to make posting");
   DocFiRec docRec = docDM.postSalesCreditNote(doc,lines, alloc, fundBals,docVatSummary,
           vatRet,  source);
   LOGGER.log(INFO, "crn returned from DB layer {0} ext ref {1}", new Object[]{docRec,docRec.getPartnerRef()});
   
  
  return docRec;
  }catch(BacException ex){
   LOGGER.log(INFO, "DB BACException error : {0}", ex.getCause().getLocalizedMessage());
   return null;
  }catch(Exception ex){
   LOGGER.log(INFO, "DB Other error : {0}", ex.getCause().getLocalizedMessage());
   return null;
  }
 }
 
 public void printedSalesFiDoc(DocInvoiceArRec docRec, UserRec usr, String pg ){
  LOGGER.log(INFO, "DocMgr. printedSalesFiDoc called for docRec id {0} ",docRec.getId());
  docDM.printedSalesFiDoc(docRec, usr, pg);
 }

 public void updateInvCrnFiledata(DocInvoiceArRec inv) throws BacException {
  LOGGER.log(INFO, "updateInvCrnFiledata called with id {0}", inv);
  docDM.updateSlInvCrnPdf(inv);
 }

 public List<DocFiTemplAccrPrePayRec> getAccrGlJnlOpen( TemplSelectOption opt){
  LOGGER.log(INFO, "getAccrGlJnlOpen called with type {0}", opt.getTemplType());
  List<DocFiTemplAccrPrePayRec> retList;
  //Query q;
  switch (opt.getTemplType()){
   case "ALL" :
    retList = this.docDM.getFiTemplateDocsAllActive(opt);
    break;
   default:
    return null;
  }
  
  LOGGER.log(INFO, "Accr doc list {0}", retList);
  
  return retList;
 }
 
 public DocFiTemplAccrPrePayRec getAccrGlJnlOpenLines( DocFiTemplAccrPrePayRec jnl){
  LOGGER.log(INFO, "getAccrGlJnlOpenLines called for jnl{0}", jnl);
  jnl = this.docDM.getDocFiTemplateGlLines(jnl);
  return jnl;
 }
 public List<DocFiRec> getAllFiDocsForComp(CompanyBasicRec comp, boolean inclLines, UserRec usr, String pg) {
  LOGGER.log(INFO, "DocMgr.getAllFiDocsForComp called with comp {0}", comp.getReference());
  List<DocFiRec> docs = this.docDM.getAllFiDocsForCompany(comp, inclLines, usr,pg);
  LOGGER.log(INFO, "Docs returned from db layer {0}", docs);
  return docs;
 }
 
 
 public List<DocLineApRec> getChequeApNotIssued(CompanyBasicRec comp, Long docNum, String procCode){
  LOGGER.log(INFO, "getChequeApNotIssued called with comp {0} doc num {1} proc code {2}", 
    new Object[]{comp.getReference(),docNum,procCode});
  
  List<DocLineApRec> ret = docDM.getApChequesUnissued(procCode,comp);
  LOGGER.log(INFO, "apLines from docDM {0}", ret);
  Collections.sort(ret, new ApLineByDocNum());
  
  if(ret!= null && !ret.isEmpty()){
   ListIterator<DocLineApRec> retLi = ret.listIterator();
   while(retLi.hasNext()){
    DocLineApRec ln = retLi.next();
    if(ln.getDocNumber() < docNum){
     retLi.remove();
    }
   }
   
  }
  
  return ret;
 }

 public DocBankLineChqRec gtDocBankChqExtrFlds(DocBankLineChqRec ln, List<String> fields){
  LOGGER.log(INFO,"gtDocBankChqExtrFlds called {0}");
  ln = this.docDM.getDocBankLineChqExtraFld(ln, fields);
  return ln;
 }
 /**
  * Return the all details associated with the document.
  * This will generally be called when getAllFiDocsForComp is called for header only.
  * @param doc
  * @param usr
  * @param pg
  * @return
  * @throws BacException 
  */
 public DocFiRec getDocDetails(DocFiRec doc, UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "DocMgr.getDocDetails called with {0}", doc);
  doc = docDM.getFullDocDetails(doc,usr,pg);
  LOGGER.log(INFO, "after call to DB layer number of lines is {0}", doc.getDocLines().size());
  return doc;
 }

 public List<DocFiRec> getDocsApForComp(CompanyBasicRec comp){
  List<DocFiRec> retList = this.docDM.getDocsApForComp(comp);
  
  return retList;
 }
 public List<DocFiRec> getDocsApForComp(CompanyBasicRec comp, Long docNum){
  List<DocFiRec> retList = docDM.getDocsApForComp(comp, docNum);
  return retList;
 }
 
 public List<DocFiRec> getDocsApForAcnt(CompanyBasicRec comp, ApAccountRec acntRec){
   List<DocFiRec> retList = docDM.getDocsApForAcnt(comp, acntRec);
   return retList;
 }
 /**
  * Get documents for a company where the doc number is equal or greater than the number entered
  * @param comp
  * @param docNum
  * @param inclLines
  * @param usr
  * @param pg Page that method was called from
  * @return
  * @throws BacException 
  */
 public List<DocFiRec> getFiDocsByDocNumPartial(CompanyBasicRec comp, Long docNum, boolean inclLines,
         UserRec usr, String pg) throws BacException {
  List<DocFiRec> docList = docDM.getDocFiCompByDocNumPart(comp, docNum, inclLines,usr,pg);
  return docList;
 }

 /**
  * 
  * @param year
  * @param periodFrom
  * @param periodTo
  * @return Gift aid claim schedule for period
  */
 public List<GiftAidScheduleLine> getGiftAidForYearPeriods(int year, int periodFrom, int periodTo) {
  LOGGER.log(INFO, "DocMgrgetGiftAidForYearPeriods called with year {0} fr per {1} to per {2}",
          new Object[]{year,periodFrom, periodTo});
  List<GiftAidScheduleLine> retlist = this.docDM.getGiftAidForFisYearPeriod(year, periodFrom, periodTo);
  LOGGER.log(INFO, "DB layer returns {0}", retlist);
  return retlist;
 }

 /**
  * Maybe called multiple times
  * @param subLedLine AR or AP line that this is a reconciliation account for
  * @param amount Amount applicable to this reconciliation. Apportionment done in web layer
  * @param fund Fund the reconciliation line is for
  * @return 
  */
 public DocLineGlRec getReconcilLine(DocLineBaseRec subLedLine,double amount, FundRec fund){
  LOGGER.log(INFO, "getReconcilLine called with line type [0}", subLedLine.getClass().getSimpleName());
  
  String subLedClass = subLedLine.getClass().getSimpleName();
  DocLineGlRec reconLine = new DocLineGlRec();
  reconLine.setId(subLedLine.getId());
  reconLine.setChangeBy(subLedLine.getChangeBy());
  reconLine.setChangeDate(subLedLine.getChangeDate());
  reconLine.setClearedByLine(subLedLine.getClearedByLine());
  reconLine.setClearingLine(subLedLine.isClearingLine());
  reconLine.setClearingDate(subLedLine.getClearingDate());
  reconLine.setClearingLine(subLedLine.isClearingLine());
  reconLine.setComp(subLedLine.getComp());
  reconLine.setCreateBy(subLedLine.getCreateBy());
  reconLine.setCreateDate(subLedLine.getCreateDate());
  reconLine.setDocAmount(amount);
  reconLine.setRestrictedFund(fund);
  reconLine.setDocHeaderBase(subLedLine.getDocHeaderBase());
  reconLine.setDocNumber(subLedLine.getDocNumber());
  reconLine.setLineNum(subLedLine.getLineNum());
  reconLine.setLineText(subLedLine.getLineText());
  reconLine.setLineType(subLedLine.getLineType());
  reconLine.setNotes(subLedLine.getNotes());
  PostTypeRec subLedPt;
  if(subLedLine.getPostType().isDebit()){
   subLedPt = this.buffer.getPostTypeForCode("glDrRecon");
  }else{
   subLedPt = this.buffer.getPostTypeForCode("glCrRecon");
  }
  reconLine.setPostType(subLedPt);
  switch(subLedClass){
   case "DocLineArRec":
    reconLine.setReconcilForArLine((DocLineArRec)subLedLine);
    break;
   case "DocLineApRec":
    reconLine.setReconcilForApLine((DocLineApRec)subLedLine);
    break;
  }
  reconLine.setReference1(subLedLine.getReference1());
  reconLine.setReference2(subLedLine.getReference2());
  reconLine.setReset(subLedLine.isReset());
  reconLine.setSortOrder(subLedLine.getSortOrder());
  reconLine.setTaxAmnt(subLedLine.getTaxAmnt());
  reconLine.setVatCodeCompany(subLedLine.getVatCodeCompany());
  
  
  return reconLine;
 }
 
 
 public List<DocLineBaseRec> getDocLinesForDoc(DocFiRec doc){
  List<DocLineBaseRec> lines = docDM.getDocLinesForDoc(doc);
  
  return lines;
 }
 
 public List<DocLineApRec> getApAgedLines(ApAgePaySel selOpt){
  LOGGER.log(INFO, "getApAgedLines called with {0}", selOpt);
  List<DocLineApRec> retList;
  String mode;
  if(StringUtils.isBlank(selOpt.getActRefFr()) && StringUtils.isBlank(selOpt.getActRefTo()) ){
   mode = "comp";
  }else{
   mode = "acntRef";
  }
  retList = docDM.getApAgedOpenLines(selOpt, mode);
  
  
  return retList;
 }
 public DocLineApRec getApClearedByLine(DocLineApRec apLnRec){
  LOGGER.log(INFO, "getApClearedByLine called with {0}", apLnRec);
  apLnRec = docDM.getApClearedByLine(apLnRec);
  return apLnRec;
 }
 public DocLineApRec getApClearedLines(DocLineApRec apLine){
  apLine = docDM.getApClearedLinesForLine(apLine);
  LOGGER.log(INFO, "Cleared lines from DM {0}", apLine.getClearingLineForLines());
  return apLine;
 }
 
 
 public List<DocLineApRec> getApLinesForPayRun(ApLineSel selOpts){
  LOGGER.log(INFO, "getApLinesForPayRun call with {0}", selOpts);
  List<DocLineApRec> retList = docDM.getApTransForPayRun(selOpts);
  LOGGER.log(INFO, "DB layer returned {0}", retList);
  return retList;
 }
 public List<DocLineApRec> getApLines(ApLineSel selOpt){
  LOGGER.log(INFO, "getApLines called for payment status {0}", selOpt.getPayStatus());
  
  List<DocLineApRec> retList = docDM.getApTransForSel(selOpt);
  LOGGER.log(INFO, "Trans returned {0}", retList);
  
  return retList;
 }
 
 public List<DocLineApRec> getApLinesForAcnt(CompanyBasicRec comp, ApAccountRec acnt){
  List<DocLineApRec> retList = docDM.getApLinesForAcnt(comp, acnt);
  LOGGER.log(INFO, "DocMgr getApLinesByComp - lines from DB layer {0}", retList);
  return retList;
 }
 public List<DocLineApRec> getApLinesByComp(CompanyBasicRec comp){
  List<DocLineApRec> retList = docDM.getApLinesForComp(comp);
  LOGGER.log(INFO, "DocMgr getApLinesByComp - lines from DB layer {0}", retList);
  return retList;
 }
 
 public List<DocLineApRec> getApDayBook(ApLineSel selOpts){
  LOGGER.log(INFO, "getApDayBook called");
  List<DocLineApRec> docList = docDM.getApDayBook(selOpts);
  LOGGER.log(INFO, "Docs found", docList);
  return docList;
 }
 
 public DocLineApRec getDocLineApReconLines(DocLineApRec apLine){
  apLine = docDM.getDocLineApReconLines(apLine);
  LOGGER.log(INFO, "getDocLineApReconLines {0}", apLine.getReconiliationLines());
  return apLine;
 }
 
 public DocLineArRec getDocLineArReconLines(DocLineArRec arLine){
  arLine = docDM.getDocLineArReconLines(arLine);
  return arLine;
 }
 
 public DocLineFiTemplateRec getDocLineFiTemplGlRecAddlazy(DocLineFiTemplateRec line){
  line = this.docDM.getDocLineFiTemplGlRecAddlazy((DocLineFiTemplGlRec)line);
  return line;
 }
 
 public DocFiRec getDocLines(DocFiRec doc){
  doc = this.docDM.getDocLines(doc);
  return doc;
 }
 
 public List<DocLineApRec> getDocLinesApOpenForAcnt(ApAccountRec acnt){
  
   List<DocLineApRec> retList = docDM.getApOpenLinesForAcnt(acnt);
   
    
   return retList;
 }
 
 public List<DocFiPartialRec> getDocsFiPartialForComp(CompanyBasicRec comp){
  
  List<DocFiPartialRec> retList = this.docDM.getDocFiPartialRecForComp(comp);
  LOGGER.log(INFO, "docDM.getDocFiPartialRecForComp returns {0}", retList);
  
  return retList;
  
 }
 
 public DocFiPartialRec getDocFiPartialLines(DocFiPartialRec doc){
  
  doc = docDM.getDocFiPartialRecLines(doc);
  
  return doc;
 }

 public List<DocFiTemplAccrPrePayRec> postAccrGlReversal(List<DocFiTemplAccrPrePayRec> accruals, TemplSelectOption opt,
         UserRec usr, String pg){
  LOGGER.log(INFO, "DocMgr.postAccrGlReversal called with accrList {0} ", accruals);
  int tmplMode = DocFiTemplAccrPrePayRec.REVERSAL;
  Date pstDate = opt.getPostDate();
  ListIterator<DocFiTemplAccrPrePayRec> accrSelIt = accruals.listIterator(); 
  while(accrSelIt.hasNext()){ 
   DocFiTemplAccrPrePayRec accrRec = accrSelIt.next();
   LOGGER.log(INFO, "accrRec {0}", accrRec);
   if(accrRec.getDocLines() == null || accrRec.getDocLines().isEmpty()){
    accrRec = docDM.getDocFiTemplateGlLines(accrRec);
    LOGGER.log(INFO, "docDM.getDocFiTemplateGlLines {0}", accrRec.getDocLines());
    accrSelIt.set(accrRec);
   }
  }
   /*DocFiRec fiDoc = templateToFiDoc(accrRec, pstDate, pstDate, pstDate, tmplMode);
   fiDoc.setChangedBy(null);
   fiDoc.setChangedOn(null);
   fiDoc.setCreatedBy(usr);
   fiDoc.setId(null);
   fiDoc.setCreateOn(new Date());
   
   LOGGER.log(INFO,"accrRec lines {0}",accrRec.getGlLines());
   LOGGER.log(INFO, "fiDoc.getDocLines() {0}", fiDoc.getDocLines());
   for(ListIterator<DocLineBaseRec> lnIt = fiDoc.getDocLines().listIterator();lnIt.hasNext();){
    DocLineBaseRec fiLnRec = lnIt.next();
    fiLnRec.setChangeBy(null);
    fiLnRec.setChangeDate(null);
    fiLnRec.setCreateBy(usr);
    fiLnRec.setCreateDate(fiDoc.getCreateOn());
    fiLnRec.setId(null);
    lnIt.set(fiLnRec);
   }
   LOGGER.log(INFO, "fiDoc id {0}create date {1} change date {2}", new Object[]{fiDoc.getId(),fiDoc.getCreateOn(),
    fiDoc.getChangedOn() });
   
   for(ListIterator<DocLineBaseRec> lnIt = fiDoc.getDocLines().listIterator();lnIt.hasNext();){
    DocLineBaseRec fiLnRec = lnIt.next();
    LOGGER.log(INFO, "Line id {0} pst key {1} created on {2}", new Object[]{fiLnRec.getId(),
     fiLnRec.getPostType().getPostTypeCode(),fiLnRec.getCreateDate()  });
   }
  }
  */
  return accruals;
 }
 
 public DocFiRec postArInv(DocFiRec doc, List<DocVatSummary> vatSum,List<FundBalance> fndSplit, String pg){
  LOGGER.log(INFO, "postArInv called with doc {0}, vatSum {1}, Fund bal {2} page {3}", new Object[]{
   doc,vatSum, fndSplit, pg});
  
  doc = this.docDM.postArInvoice(doc, vatSum, fndSplit, pg);
  return doc;
  
 }
 public DocFiRec postArReceiptSingle(DocFiRec doc, List<DocLineArRec> paidLines, Locale loc,UserRec usr, String source)
         throws BacException {
  LOGGER.log(INFO, "DocMgr.postArReceiptSingle called with doc {0} paidLines {1} usr {2} source {3}", new Object[]{
   doc,paidLines,usr,source  });
  if(doc.getDocLines() == null || doc.getDocLines().isEmpty()){
   throw new BacException("NO Receipt amount");
  }
  doc = this.docDM.postArReceiptSingle(doc, paidLines, loc, usr,source);
  return doc;
 }

 public List<DocFiRec> getEditableFiDocsForComp(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "getEditableFiDocsForComp called with comp id {0}", comp.getId());
  List<DocFiRec> lst = this.docDM.getEditableFiDocsForComp(comp);
  return lst;
 }

 public List<DocFiRec> getFiEdDocsByNumPartial(CompanyBasicRec comp, Long docNum) throws BacException {
  LOGGER.log(INFO, "DocMgr.getFiEdDocsByNumPartial called with comp id {0} docNum {1}", 
          new Object[]{comp.getId(),docNum});
  List<DocFiRec> docList = docDM.getDocEditFiCompByDocNumPart(comp, docNum);
  return docList;
 }

 public List<DocFiRec> getFiDocsBySelOpt(FiDoclSelectionOpt selOpt){
  LOGGER.log(INFO, "getFoDocsBySelOpt called with {0}", selOpt);
  
  String oldDocTxt = null;
  String oldPtnrRef = null;
  boolean oldDocTextSet= false;
  boolean oldPtnrRefSet= false;
  if(!selOpt.getDocText().isEmpty() && !selOpt.getDocText().endsWith("%")){
   oldDocTxt = selOpt.getDocText();
   selOpt.setDocText(oldDocTxt + "%");
   oldDocTextSet = true;
  }
  
  if(!selOpt.getPtnrRef().isEmpty() && !selOpt.getPtnrRef().endsWith("%")){
   oldPtnrRef = selOpt.getPtnrRef();
   selOpt.setPtnrRef(oldPtnrRef + "%");
   oldPtnrRefSet = true;
  }
  List<DocFiRec> docs = this.docDM.getFiDocsBySelOpt(selOpt);
  
  if(oldDocTextSet){
   selOpt.setDocText(oldDocTxt);
  }
  if(oldPtnrRefSet){
   selOpt.setPtnrRef(oldPtnrRef);
  }
  
  
  LOGGER.log(INFO, "Docs returned by docDM {0}", docs);
  return docs;
 }
 
 public DocFiRec updateDocument(DocFiRec doc, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "DocMgr.updateDocument called with doc {0} user {1} page {2}", new Object[]{
   doc,usr,page});
  doc = this.docDM.updateDocument(doc, usr, page);
  
  return doc;
 }

 public boolean partialDocDelete(DocFiPartialRec partDoc){
  boolean deleted = docDM.stdJnlPartialDel(partDoc);
  return deleted;
 }
 
 public DocFiRec postApInvoice(DocFiRec doc,List<DocVatSummary> vatSumm, List<FundBalance> fndSplit, String page){
  LOGGER.log(INFO, "called DocMgr postApInvoice");
  doc = this.docDM.postApInvoice(doc, vatSumm,fndSplit, page);
  return doc;
  
 }
 
 public DocFiRec postApPaySingle(DocFiRec doc,DocLineApRec apLine, String page){
  LOGGER.log(INFO, "called DocMgr postApPaySingle");
  
  
  doc = docDM.postApPaySingle(doc, page);
  LOGGER.log(INFO, "After DM post single payment {0}", doc.getId());
  
  // if ap line has cheque payment type then add cheque info
  if(apLine.getPayType().getPayMedium().equals("CHQ")){
   LOGGER.log(INFO, "Need to add cheque");
   
  }
  
  
  return doc;
 }
 
 public DocFiRec postArBacsReceiptRun(DocFiRec doc, BnkPaymentRunRec payRun, 
         BankAccountCompanyRec compBnkAc, String lineText, UserRec usr, String page) 
         throws BacException {
  LOGGER.log(INFO, "DocMgr.postArBacsReceiptRun called with doc {0} payRun {1}  usr {2}", 
          new Object[]{doc,payRun,usr.getId()});
  Date currDt = new Date();
  // create payment run posting
  boolean payRunRefUnique = treasuryDM.paymentRunRefAvailable(doc.getCompany(), payRun.getRef());
  try{
   payRun = treasuryDM.createPayRun(payRun, currDt, usr, page);
  } catch(BacException ex){
   LOGGER.log(INFO,"create pay run error");
   throw new BacException("Treasury error thrown in doc mgr create pay run error");
  }
  if(!payRunRefUnique){
   throw new BacException("payrun not unique");
  }
  try{
  doc = docDM.postArPayRunBacs(doc, payRun,compBnkAc,lineText,  usr, page);
  LOGGER.log(INFO, "Posted Doc id is: {0} ", doc.getId());
  
  }catch(BacException ex){
   LOGGER.log(INFO, "payrun posting error {0}", ex.getLocalizedMessage());
   throw new BacException(ex.getLocalizedMessage(),ex.getErrorCode());
  }
  
  
  LOGGER.log(INFO, "payment lines {0}", payRun.getBankLines());
  List<DocBankLineBaseRec> bnkLines = payRun.getBankLines();
  for(DocBankLineBaseRec bnkLn: bnkLines){
   LOGGER.log(INFO, "arlines for bnkline {0}", bnkLn.getArPymntLines());
   // if  bnkLn.getArPymntLines() is null this is a contra line
  }
  
  
  
  
  
  // post FI doc single bank multiple AR account receipt posting each line has reference to payment run. 
  // Assign bank tr
 
  return doc;
 }

 
 public DocLineBaseRec resetDocLine(DocLineBaseRec clearLine, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "DocMgr resetDocLine called with line {0} usr {1} page {2}", new Object[]{
   clearLine, usr, page  });
  return null;
 }

 public DocFiRec docResetLines(DocFiRec doc, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "DocMgr docResetLines called with line {0} usr {1} page {2}", new Object[]{
   doc, usr, page  });
  doc = docDM.docRestClearing(doc, usr, page);
  LOGGER.log(INFO,"DM returns {0}",doc);
  return doc;
 }

 public DocFiRec reverseDoc(DocFiRec originalDoc, DocFiRec reversalDoc, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "reverseDoc called with original {0} rev {1} usr {2} view {3}", new Object[]{
   originalDoc,reversalDoc,usr,page });
    reversalDoc = this.docDM.reverseFiDoc(originalDoc, reversalDoc, usr, page);
    LOGGER.log(INFO, "DocDM returns doc {0}", reversalDoc);
  // add lines to reversal doc
  /*List<DocLineBaseRec> origDocLines = docDM.getDocFiLines(originalDoc);
  if(origDocLines == null || origDocLines.isEmpty()){
   throw new BacException("No lines to reverse");
  }
  List<DocLineBaseRec> revDocLines = reversalDoc.getDocLines();
  LOGGER.log(INFO, "revDocLines {0}", revDocLines);
  if(revDocLines == null){
   revDocLines = new ArrayList<DocLineBaseRec>();
  }
  for(DocLineBaseRec origLn: origDocLines){
   LOGGER.log(INFO, "Line type {0}", origLn.getClass().getSimpleName());*/
  /* if(origLn.getLineType().getModule().getName().equalsIgnoreCase("AR")){
    
   }*/
   
  //}
  return reversalDoc;
 }
 
 public DocFiTemplAccrPrePayRec templateJnlUpdate(DocFiTemplAccrPrePayRec jnl, String pg){
  LOGGER.log(INFO, "templateJnlUpdate called with Jnl {0}", jnl);
  LOGGER.log(INFO, "called with Jnl gllines {0}", jnl.getDocLines());
  LOGGER.log(INFO, "called with Jnl lines {0}", jnl.getDocLines());
  UserRec crUsr = jnl.getCreatedBy();
  Date crDate = new Date();
  CompanyBasicRec comp = jnl.getCompany();
  LineTypeRuleRec linTyGl = this.buffer.getLineTypeRuleByCode("GL");
  LOGGER.log(INFO, "GL line type {0}", linTyGl);
  int mode;
  
  if(jnl.getId() == null){
   mode = DocFiTemplAccrPrePayRec.SETUP;
   
  }else if(jnl.getTmplType() == DocFiTemplAccrPrePayRec.RECURRING){
   mode = DocFiTemplAccrPrePayRec.REPEAT;
  }else{
   mode = DocFiTemplAccrPrePayRec.REVERSAL;
  }
  
  DocFiRec fiDoc;
  switch(mode){
   case DocFiTemplAccrPrePayRec.REVERSAL:
    Date nextDate = jnl.getNextPostDate();
  
    fiDoc = templateToFiDoc(jnl,nextDate,nextDate,nextDate,mode);
    break;
   case DocFiTemplAccrPrePayRec.REPEAT:
    nextDate = jnl.getNextPostDate();
    UomRec freq = jnl.getRecurUom();
    
    GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(jnl.getCompany().getLocale());
    cal.setTime(nextDate);
    int unit;
    switch(freq.getProcessCode()){
     case "DY":
      unit = cal.get(GregorianCalendar.DAY_OF_MONTH);
      break;
     case "MTH_CAL":
      unit = cal.get(GregorianCalendar.MONTH);
      break;
     default:
      unit = cal.get(GregorianCalendar.YEAR);
    }
    cal.add(unit, 1);
    nextDate = cal.getTime();
    fiDoc = templateToFiDoc(jnl,nextDate,nextDate,nextDate,mode);
    break;
   default:
    
    fiDoc = templateToFiDoc(jnl,jnl.getPostingDate(),jnl.getPostingDate(),jnl.getTaxDate(),mode);
    break;
  }
  fiDoc.setCreatedBy(crUsr);
  fiDoc.setCreateOn(crDate);
  fiDoc.setCompany(comp);
  //List<DocLineBaseRec> fiLines = fiDoc.getDocLines();
  
  ListIterator<DocLineBaseRec> fiLnLi = fiDoc.getDocLines().listIterator();
  while(fiLnLi.hasNext()){
   DocLineBaseRec ln = fiLnLi.next();
   LOGGER.log(INFO, "ln post type {0}", ln.getPostType().getPostTypeCode());
   ln.setComp(comp);
   ln.setCreateBy(crUsr);
   ln.setCreateDate(crDate);
   ln.setLineType(linTyGl);
  
  }
  //fiDoc.setDocLines(fiLines);
  jnl = this.docDM.postTemplateJnl(jnl, fiDoc, mode, pg);
  return jnl;
 }
 
}

