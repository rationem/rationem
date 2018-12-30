/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.doc;

import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.doc.DocFiPartialRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocFiTemplAccrPrePayRec;
import com.rationem.busRec.doc.DocLineFiTemplGlRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import java.util.List;
import java.util.ListIterator;
import com.rationem.util.BaseBean;
import com.rationem.util.RestFundPostBalance;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.ejbBean.fi.DocumentManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;


/**
 * Common posting functions
 * @author Chris
 */
abstract class DocCommon extends BaseBean {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;

 private static final Logger logger =
            Logger.getLogger("accounts.fi.arap.DocCommon");

 public List<RestFundPostBalance> fundBals;
 private List<FundRec> restrictedFunds;

 private CompanyBasicRec docComp;
 protected DocFiRec fiDoc;


 @EJB
 private SysBuffer sysBuff;
 @EJB
 private DocumentManager docMgr;
 
 
 


  public DocCommon() {
  }

  @PostConstruct
  private void init()
  {
   logger.log(INFO, "Doc Common constructor");
   if(fiDoc == null){
    fiDoc = this.getFiDoc();
   }
   docComp = getCompList().get(0);
   
   fiDoc.setCompany(docComp);
  }
   public DocumentManager getDocMgr() {
  return docMgr;
 }

 public void setDocMgr(DocumentManager docMgr) {
  this.docMgr = docMgr;
 }

  public CompanyBasicRec getDocComp() {
    if(docComp == null){
     docComp = this.getCompList().get(0);
     logger.log(INFO, "Doc  common docComp locale {0}", docComp.getLocale());
    }
    return docComp;
  }

  public void setDocComp(CompanyBasicRec docComp) {
    this.docComp = docComp;
  }

  public List<RestFundPostBalance> getFundBals() {
    return fundBals;
  }

 public void setFundBals(List<RestFundPostBalance> fundBals) {
  this.fundBals = fundBals;
 }
  
  

  public List<FundRec> getRestrictedFunds() {
    return restrictedFunds;
  }

  public void setRestrictedFunds(List<FundRec> restrictedFunds) {
    this.restrictedFunds = restrictedFunds;
  }

 public SysBuffer getSysBuff() {
  return sysBuff;
 }

 public void setSysBuff(SysBuffer sysBuff) {
  this.sysBuff = sysBuff;
 }





  public void addFundBal(DocLineGlRec glDocLine){
    logger.log(INFO, "addFundBal called with docline {0} fund bals {1}",
            new Object[]{glDocLine,fundBals});
    RestFundPostBalance fundBal = new RestFundPostBalance();
    if(fundBals == null){
      logger.log(INFO, "No fund balances - add new balance");
      fundBals = new ArrayList<RestFundPostBalance>();
      fundBal.setId(glDocLine.getRestrictedFund().getId());

      if(glDocLine.getPostType().isDebit()){
        fundBal.setBalance(glDocLine.getDocAmount());
      }else{
        fundBal.setBalance(glDocLine.getDocAmount() * -1);
      }
     fundBals.add(fundBal);
    }else{
      logger.log(INFO, "und balances exist either update or add new fund");
    ListIterator<RestFundPostBalance> fundIt = fundBals.listIterator();
    boolean found = false;
    while(fundIt.hasNext() && !found){
      RestFundPostBalance rec = fundIt.next();
      if(rec.getId() == glDocLine.getRestrictedFund().getId()){
        logger.log(INFO, "found fund to update existing id: {0} line fund id {1}",
                new Object[]{rec.getId(),glDocLine.getRestrictedFund().getId()});
        found = true;
        fundBal.setId(glDocLine.getId());
        double bal = rec.getBalance();
        if(glDocLine.getPostType().isDebit()){
          bal = bal + glDocLine.getDocAmount();
        }else{
          bal = bal - glDocLine.getDocAmount();
        }
        rec.setBalance(bal);
      }
      fundIt.set(rec);
    }
    if(!found){
      // id not in balance list so need to add
      fundBal.setId(glDocLine.getId());
      if(glDocLine.getPostType().isDebit()){
        fundBal.setBalance(glDocLine.getDocAmount());
      }else{
        fundBal.setBalance(glDocLine.getDocAmount() * -1);
      }
    }
    }
   logger.log(INFO, "End FunBals {0}", fundBals);

  }
  
  
  public String determineSortOrder(DocFiRec doc, DocLineGlRec line, FiGlAccountCompRec glAcnt, Locale loc){
   String ret = null;
   String sortCode = glAcnt.getSortOrder().getSortCode();
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",loc);
   if(sortCode.equalsIgnoreCase("DocDt")){
    ret = dateFormat.format(doc.getDocumentDate());
    
   }else if(sortCode.equalsIgnoreCase("postDt")){
    ret = dateFormat.format(doc.getPostingDate());
   }else if (sortCode.equalsIgnoreCase("entryDt")){
    if(doc.getCreateOn() == null){
     doc.setCreateOn(new Date());
    }
    ret = dateFormat.format(doc.getCreateOn());
   }
   
   return ret;
  }
  
  public String determineSortOrder(DocFiTemplAccrPrePayRec doc, DocLineFiTemplGlRec line, FiGlAccountCompRec glAcnt, Locale loc){
   String ret = null;
   String sortCode = glAcnt.getSortOrder().getSortCode();
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",loc);
   switch(sortCode){
    case "DocDt":
     ret = dateFormat.format(doc.getDocumentDate());
     break;
    case "postDt" :
     ret = dateFormat.format(doc.getPostingDate());
     break;
    case "entryDt" :
     Date crDt ;
     if(doc.getCreateOn() == null){
      crDt = new Date();
     }else{
      crDt = doc.getCreateOn();
     }
     ret = dateFormat.format(crDt);
   }
   return ret;
  }
  
  public String determineSortOrder(DocFiPartialRec doc, DocLineGlRec line, FiGlAccountCompRec glAcnt, Locale loc){
   String ret = null;
   String sortCode = glAcnt.getSortOrder().getSortCode();
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",loc);
   if(sortCode.equalsIgnoreCase("DocDt")){
    ret = dateFormat.format(doc.getDocumentDate());
    
   }else if(sortCode.equalsIgnoreCase("postDt")){
    ret = dateFormat.format(doc.getPostingDate());
   }else if (sortCode.equalsIgnoreCase("entryDt")){
    if(doc.getCreateDate() == null){
     doc.setCreateDate(new Date());
    }
    ret = dateFormat.format(doc.getCreateDate());
   }
   
   return ret;
  }

 public abstract DocFiRec getFiDoc();
  

 public abstract void setFiDoc(DocFiRec fiDoc);

public FiscalPeriodYearRec getFiscPerYr(Date pstDate){
  logger.log(INFO, "getFiscPerYr called with date {0}", pstDate);
  logger.log(INFO, "DocComp per rule id {0}", docComp.getPeriodRule().getId());
  //FisPeriodRuleRec perRule = docComp.getPeriodRule();
  FiscalPeriodYearRec perYr = sysBuff.getCompFiscalPeriodYearForDate(docComp, pstDate);
  
  if(perYr == null){
   return null;
  }else{
   return perYr;
  }
  }
 
  
 
  
  
  public void setFundBals(ArrayList<RestFundPostBalance> fundBals) {
    this.fundBals = fundBals;
  }

  public List<String> restrictedFundsComplete(String input){
    logger.log(INFO, "restrictedFundsComplete called input {0} comp: {1}",new Object[]{input,docComp});
   List<String> retList = new ArrayList<String>();
   if(restrictedFunds == null){
     logger.log(INFO,"Web restrictedFundsComplete need to get fund list for comp {0}",docComp);
    restrictedFunds = sysBuff.getRestrictedFundsForComp(docComp);
   }
   boolean all = input.isEmpty();

   ListIterator<FundRec> it = restrictedFunds.listIterator();
    while(it.hasNext()){
      FundRec fnd = it.next();
      if(all){
        retList.add(fnd.getFndCode());
      }else{
        if(fnd.getFndCode().startsWith(input)){
          retList.add(fnd.getFndCode());
        }
      }


    }
   return retList;
  }
  
}



