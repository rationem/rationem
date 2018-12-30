/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.ApAgePaySel;
import com.rationem.util.ApArAgedBal;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.comparator.DocLIneApRecByAcntRefPostDate;
import com.rationem.util.helper.comparator.DocLineApRecByAcntRefDocDate;
import com.rationem.util.helper.comparator.DocLineApRecByAcntRefDueDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.context.RequestContext;
/**
 *
 * @author user
 */
public class ApAgedPayable extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ApAgedPayable.class.getName());
 
 public static final int SEL = 0;
 public static final int REPORT = 1;
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private DocumentManager docMgr;
 
 
 private CompanyBasicRec compSel;
 private int days1;
 private int days2;
 private int days3;
 private int days4;
 private List<ApArAgedBal> agedBals;
 private ApArAgedBal agedBalSel;
 private List<ApArAgedBal> agedBalsFilter;
 private List<DocLineApRec> apLines;
 private List<DocLineApRec> apLinesSel;
 private ApAgePaySel selOpt;
 private String lineTypeDlg;
 private String daysUntil;
 private String step0Text;
 private String step1Text;
 private String bucket1Text;
 private String bucket2Text;
 private String bucket3Text;
 private String bucket4Text;
 private String bucket5Text;
 private String ageBasis;
 private double apLinesSelDr;
 private double apLinesSelCr;
 private double balTot;
 private double days1Tot;
 private double days2Tot;
 private double days3Tot;
 private double days4Tot;
 private double days5Tot;
 
 
 

 /**
  * Creates a new instance of ApAgedPayable
  */
 public ApAgedPayable() {
  
 }
 
 @PostConstruct
 private void init(){
  selOpt = new ApAgePaySel();
  agedBals = new ArrayList<>();
  step0Text = this.formTextForKey("rptSelCrit");
  step1Text = this.formTextApForKey("rptAgedBal");
  ageBasis = "dueDt";
  setStep(0);
  this.setStepName(step0Text);
  compSel = getCompList().get(0);
  if(compSel.getCompApAr() == null){
   compSel = sysBuff.getCompBasicArApAging(compSel);
   
  }
  LOGGER.log(INFO, "Comp apar sett {0}", compSel.getCompApAr());
  days1 = 30;
  days2 = 60;
  days3 = 90;
  days4 = 120;
  if(getCompList() == null){
   MessageUtil.addClientErrorMessage("agedPayFrm:errMsg", "compsNone", "errorText");
   RequestContext.getCurrentInstance().update("agedPayFrm:errMsg");
   return;
  }
  
  
  
 }

 public String getBucket1Text() {
  return bucket1Text;
 }

 public void setBucket1Text(String bucket1Text) {
  this.bucket1Text = bucket1Text;
 }

 public String getBucket2Text() {
  return bucket2Text;
 }

 public void setBucket2Text(String bucket2Text) {
  this.bucket2Text = bucket2Text;
 }

 public String getBucket3Text() {
  return bucket3Text;
 }

 public void setBucket3Text(String bucket3Text) {
  this.bucket3Text = bucket3Text;
 }

 public String getBucket4Text() {
  return bucket4Text;
 }

 public void setBucket4Text(String bucket4Text) {
  this.bucket4Text = bucket4Text;
 }

 public String getBucket5Text() {
  return bucket5Text;
 }

 public void setBucket5Text(String bucket5Text) {
  this.bucket5Text = bucket5Text;
 }

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public int getDays1() {
  return days1;
 }

 public void setDays1(int days1) {
  this.days1 = days1;
 }

 public double getDays1Tot() {
  return days1Tot;
 }

 public void setDays1Tot(double days1Tot) {
  this.days1Tot = days1Tot;
 }

 
 public int getDays2() {
  return days2;
 }

 public void setDays2(int days2) {
  this.days2 = days2;
 }

 public double getDays2Tot() {
  return days2Tot;
 }

 public void setDays2Tot(double days2Tot) {
  this.days2Tot = days2Tot;
 }

 
 public int getDays3() {
  return days3;
 }

 public void setDays3(int days3) {
  this.days3 = days3;
 }

 public double getDays3Tot() {
  return days3Tot;
 }

 public void setDays3Tot(double days3Tot) {
  this.days3Tot = days3Tot;
 }

 
 public int getDays4() {
  return days4;
 }

 public void setDays4(int days4) {
  this.days4 = days4;
 }

 public double getDays4Tot() {
  return days4Tot;
 }

 public void setDays4Tot(double days4Tot) {
  this.days4Tot = days4Tot;
 }

 public double getDays5Tot() {
  return days5Tot;
 }

 public void setDays5Tot(double days5Tot) {
  this.days5Tot = days5Tot;
 }

 public String getDaysUntil() {
  return daysUntil;
 }

 public void setDaysUntil(String daysUntil) {
  this.daysUntil = daysUntil;
 }

 private int getDocLineAge(DocLineApRec ln){
  DateTime currDate = new DateTime(new Date());
  DateTime docEffectiveDate = null;
  int docAgeDays;
  
   switch(this.ageBasis){
    case "dueDt":
     docEffectiveDate = new DateTime(ln.getDueDate());
     break;
    case "pstDt":
     docEffectiveDate = new DateTime(ln.getDocFi().getPostingDate());
     break;
    default:
     docEffectiveDate = new DateTime(ln.getDocFi().getDocumentDate());
     break;
   }
   docAgeDays = Days.daysBetween(docEffectiveDate.toLocalDate(), currDate.toLocalDate() ).getDays();
   return docAgeDays; 
 }
 
 public List<ApArAgedBal> getAgedBals() {
  return agedBals;
 }

 public void setAgedBals(List<ApArAgedBal> agedBals) {
  this.agedBals = agedBals;
 }

 public ApArAgedBal getAgedBalSel() {
  return agedBalSel;
 }

 public void setAgedBalSel(ApArAgedBal agedBalSel) {
  this.agedBalSel = agedBalSel;
 }

 
 public List<ApArAgedBal> getAgedBalsFilter() {
  return agedBalsFilter;
 }

 public void setAgedBalsFilter(List<ApArAgedBal> agedBalsFilter) {
  this.agedBalsFilter = agedBalsFilter;
 }

 
 public String getAgeBasis() {
  return ageBasis;
 }

 public void setAgeBasis(String ageBasis) {
  this.ageBasis = ageBasis;
 }

 public List<DocLineApRec> getApLines() {
  return apLines;
 }

 public void setApLines(List<DocLineApRec> apLines) {
  this.apLines = apLines;
 }

 public List<DocLineApRec> getApLinesSel() {
  return apLinesSel;
 }

 public void setApLinesSel(List<DocLineApRec> apLinesSel) {
  this.apLinesSel = apLinesSel;
 }

 public double getApLinesSelDr() {
  return apLinesSelDr;
 }

 public void setApLinesSelDr(double apLinesSelDr) {
  this.apLinesSelDr = apLinesSelDr;
 }

 public double getApLinesSelCr() {
  return apLinesSelCr;
 }

 public void setApLinesSelCr(double apLinesSelCr) {
  this.apLinesSelCr = apLinesSelCr;
 }

 public double getBalTot() {
  return balTot;
 }

 public void setBalTot(double balTot) {
  this.balTot = balTot;
 }

 
 public String getLineTypeDlg() {
  return lineTypeDlg;
 }

 public void setLineTypeDlg(String lineTypeDlg) {
  this.lineTypeDlg = lineTypeDlg;
 }
 
 

 public ApAgePaySel getSelOpt() {
  return selOpt;
 }

 public void setSelOpt(ApAgePaySel selOpt) {
  this.selOpt = selOpt;
 }

 public String getStep0Text() {
  return step0Text;
 }

 public void setStep0Text(String step0Text) {
  this.step0Text = step0Text;
 }

 public String getStep1Text() {
  return step1Text;
 }

 public void setStep1Text(String step1Text) {
  this.step1Text = step1Text;
 }
 
 public void onAcntRefFr(ValueChangeEvent evt){
  LOGGER.log(INFO, "onAcntRefFr called with {0}", evt.getNewValue());
  selOpt.setActRefFr((String)evt.getNewValue());
  if(StringUtils.isBlank(selOpt.getActRefTo())){
   selOpt.setActRefTo(selOpt.getActRefFr());
   RequestContext.getCurrentInstance().update("agedPayFrm:acntRefTo");
  }else if(selOpt.getActRefFr().compareTo(selOpt.getActRefTo()) > 0){
   selOpt.setActRefTo(selOpt.getActRefFr());
   RequestContext.getCurrentInstance().update("agedPayFrm:acntRefTo");
  }
 }
 
 public void onAcntRefTo(ValueChangeEvent evt){
  LOGGER.log(INFO, "onAcntRefFr called with {0}", evt.getNewValue());
  if(evt.getNewValue() == null && selOpt.getActRefFr() != null){
   selOpt.setActRefFr(null);
   RequestContext.getCurrentInstance().update("agedPayFrm:acntRefFr");
   return;
  }
  selOpt.setActRefTo((String)evt.getNewValue());
  if(StringUtils.isBlank(selOpt.getActRefFr())){
   selOpt.setActRefFr(selOpt.getActRefTo());
   RequestContext.getCurrentInstance().update("agedPayFrm:acntRefFr");
   
  }else if(selOpt.getActRefFr().compareTo(selOpt.getActRefTo()) > 0 ){
   selOpt.setActRefFr(selOpt.getActRefTo());
   RequestContext.getCurrentInstance().update("agedPayFrm:acntRefFr");
  }
  
 }
 
 public void onBackBtn(){
  setStep(0);
  setStepName(step0Text);
  RequestContext.getCurrentInstance().update("agedPayFrm");
 }
 
 public void onExecRpt(){
  LOGGER.log(INFO, "onExecRpt called");
  apLines = docMgr.getApAgedLines(selOpt);
  LOGGER.log(INFO, "apLines after docMgr getApAgedLines {0}", apLines.size());
  if(apLines != null && !apLines.isEmpty()){
   switch (ageBasis){
    case "dueDt":
     Collections.sort(apLines, new DocLineApRecByAcntRefDueDate());
     break;
    case "pstDt":
     Collections.sort(apLines, new DocLIneApRecByAcntRefPostDate());
     break;
    case "docDt":
     Collections.sort(apLines, new DocLineApRecByAcntRefDocDate());
     break;
   }
   agedBals = new ArrayList<>();
   balTot = 0;
   days1Tot = 0;
   days2Tot = 0;
   days3Tot = 0;
   days4Tot = 0;
   days5Tot = 0;
   ApArAgedBal currAgeBal = null;
   int index = 0;
   int docLnNum = 0;
   DateTime currDate = new DateTime(new Date());
   for(DocLineApRec docLn:apLines){
    LOGGER.log(INFO, "Process docLn with index {0}", index);
    docLnNum++;
    LOGGER.log(INFO, "docLn num {0}", docLnNum);
    boolean foundAged = false;
    if(agedBals.isEmpty()){
     foundAged = false;
     LOGGER.log(INFO, "agedBals is empty");
    }else{
     LOGGER.log(INFO, "index {0} agedBals size {1}", new Object[]{index,agedBals.size()});
     ListIterator<ApArAgedBal> li = agedBals.listIterator();
     while(li.hasNext() && !foundAged){
      ApArAgedBal curr = li.next();
      index++;
      if(StringUtils.equals(docLn.getAccountRef(), curr.getAcntRef())){
       currAgeBal = curr;
       foundAged = true;
      }
     }
    }
    LOGGER.log(INFO, "Index is now {0}", index);
     LOGGER.log(INFO, "Found aged bal {0}", foundAged);
     if(!foundAged){
      currAgeBal = new ApArAgedBal();
      currAgeBal.setAcntRef(docLn.getAccountRef());
      index = 0;
      LOGGER.log(INFO,"created currAgeBal with acnt ref {0} ",currAgeBal.getAcntRef());
     }else{
      LOGGER.log(INFO, "Need to find currAgeBal record for line");
      
     }
    
    
    
    LOGGER.log(INFO, "currAgeBal account ref {0} line ref {1} ", 
      new Object[]{currAgeBal.getAcntRef(),docLn.getAccountRef()});
     // which age category does doc fall into
     int docAge;
     DateTime lineDate;
     switch (ageBasis){
      case "dueDt":
       lineDate = new DateTime(docLn.getDueDate());
       break;
      case "pstDt":
       lineDate = new DateTime(docLn.getDocFi().getPostingDate());
       break;
      default:
       lineDate = new DateTime(docLn.getDocFi().getDocumentDate());
       break;
     }
     
     
     docAge = Days.daysBetween(lineDate.toLocalDate(), currDate.toLocalDate() ).getDays();
     LOGGER.log(INFO, "Doc num {0} line effective date {1} age in days {2}", 
       new Object[]{docLn.getDocFi().getDocNumber(),lineDate.toLocalDate(),docAge});
     double bal = currAgeBal.getBal();
     if(docLn.getPostType().isDebit()){
       bal -= docLn.getDocAmount();
       balTot -= docLn.getDocAmount();
      }else{
       bal += docLn.getDocAmount();
       balTot += docLn.getDocAmount();
      }
     currAgeBal.setBal(bal);
     if(docAge <= days1){
      double days1Bal = currAgeBal.getAmount1();
      if(docLn.getPostType().isDebit()){
       days1Bal -= docLn.getDocAmount();
       days1Tot -= docLn.getDocAmount();
      }else{
       days1Bal += docLn.getDocAmount();
       days1Tot += docLn.getDocAmount();
      }
      currAgeBal.setAmount1(days1Bal);
     }else if (docAge <= days2){
      double days1Bal = currAgeBal.getAmount2();
      if(docLn.getPostType().isDebit()){
       days1Bal -= docLn.getDocAmount();
       days2Tot -= docLn.getDocAmount();
      }else{
       days1Bal += docLn.getDocAmount();
       days2Tot += docLn.getDocAmount();
      }
      currAgeBal.setAmount2(days1Bal);
     }else if (docAge <= days3){
      double days1Bal = currAgeBal.getAmount3();
      if(docLn.getPostType().isDebit()){
       days1Bal -= docLn.getDocAmount();
       days3Tot -= docLn.getDocAmount();
      }else{
       days1Bal += docLn.getDocAmount();
       days3Tot += docLn.getDocAmount();
      }
      currAgeBal.setAmount3(days1Bal);
     }else if (docAge <= days4){
      double days1Bal = currAgeBal.getAmount4();
      if(docLn.getPostType().isDebit()){
       days1Bal -= docLn.getDocAmount();
       days4Tot -= docLn.getDocAmount();
      }else{
       days1Bal += docLn.getDocAmount();
       days4Tot += docLn.getDocAmount();
      }
      currAgeBal.setAmount4(days1Bal);
     }else{
      double days1Bal = currAgeBal.getAmount5();
      if(docLn.getPostType().isDebit()){
       days1Bal -= docLn.getDocAmount();
       days5Tot -= docLn.getDocAmount();
      }else{
       days1Bal += docLn.getDocAmount();
       days5Tot += docLn.getDocAmount();
      }
      currAgeBal.setAmount5(days1Bal);
     }
    
    
    // is the account in the list
    
    LOGGER.log(INFO, "End of process line - agedBals size {0}", agedBals.size());
    LOGGER.log(INFO, "index {0}", index);
    LOGGER.log(INFO,"Found line {0}",foundAged);
    if(!foundAged){
     agedBals.add(currAgeBal);
    }
   }
   
  }
  setStep(1);
  setStepName(step1Text);
  RequestContext.getCurrentInstance().update("agedPayFrm");
 }
 
 public void onDetailBal(){
  LOGGER.log(INFO, "onDetailBal called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getBal()});
  lineTypeDlg = "bal";
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  for(DocLineApRec ln:apLines){
   LOGGER.log(INFO, "ln.getAccountRef() {0}", ln.getAccountRef());
   if(StringUtils.equals(agedBalSel.getAcntRef(), ln.getAccountRef())){
    apLinesSel.add(ln);
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm:linesDlg");
  rCtx.execute("PF('linesWv').show()");
  
 }
 
 public void onDetailCat1(){
  LOGGER.log(INFO, "onDetailCat1 called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getAmount1()});
  lineTypeDlg = "day";
  
  daysUntil = String.valueOf(days1);
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  int docAgeDays;
  for(DocLineApRec ln:apLines){
   
   docAgeDays = this.getDocLineAge(ln);
   LOGGER.log(INFO, "docAgeDays {0} doc num {1}", new Object[]{docAgeDays, ln.getDocNumber()});
   if(docAgeDays <= days1){
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
    apLinesSel.add(ln);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm");
  rCtx.execute("PF('linesWv').show()");
 }
 
 public void onDetailCat2(){
  LOGGER.log(INFO, "onDetailCat2 called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getAmount2()});
  lineTypeDlg = "day";
  daysUntil = String.valueOf(days2);
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  int docAgeDays;
  for(DocLineApRec ln:apLines){
   
   docAgeDays = this.getDocLineAge(ln);
   LOGGER.log(INFO, "docAgeDays {0} doc num {1}", new Object[]{docAgeDays, ln.getDocNumber()});
   if(docAgeDays > days1 && docAgeDays <= days2){
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
    apLinesSel.add(ln);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm");
  rCtx.execute("PF('linesWv').show()");
 }
 
 public void onDetailCat3(){
  LOGGER.log(INFO, "onDetailCat3 called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getAmount3()});
  lineTypeDlg = "day";
  daysUntil = String.valueOf(days3);
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  int docAgeDays;
  for(DocLineApRec ln:apLines){
   
   docAgeDays = this.getDocLineAge(ln);
   LOGGER.log(INFO, "docAgeDays {0} doc num {1}", new Object[]{docAgeDays, ln.getDocNumber()});
   if(docAgeDays > days2 && docAgeDays <= days3){
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
    apLinesSel.add(ln);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm");
  rCtx.execute("PF('linesWv').show()");
 }
 
 public void onDetailCat4(){
  LOGGER.log(INFO, "onDetailCat4 called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getAmount4()});
   daysUntil = String.valueOf(days4);
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  int docAgeDays;
  for(DocLineApRec ln:apLines){
   
   docAgeDays = this.getDocLineAge(ln);
   if(docAgeDays > days3 && docAgeDays <= days4){
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
    apLinesSel.add(ln);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm");
  rCtx.execute("PF('linesWv').show()");
 }
 
 public void onDetailCat5(){
  LOGGER.log(INFO, "onDetailCat5 called  ref {0} bal {1}",
    new Object[]{agedBalSel.getAcntRef(),agedBalSel.getAmount5()});
   daysUntil = String.valueOf(days3);
  apLinesSel = new ArrayList<>();
  apLinesSelCr = 0;
  apLinesSelDr = 0;
  int docAgeDays;
  for(DocLineApRec ln:apLines){
   
   docAgeDays = this.getDocLineAge(ln);
   
   if(docAgeDays > days4){
    if(ln.getPostType().isDebit()){
     apLinesSelDr += ln.getDocAmount();
    }else{
     apLinesSelCr += ln.getDocAmount();
    }
    apLinesSel.add(ln);
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("displLinesFrm");
  rCtx.execute("PF('linesWv').show()");
 }
}
