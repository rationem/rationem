/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.doc;

import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocFiTemplAccrPrePayRec;
import com.rationem.busRec.doc.DocLineFiTemplGlRec;
import com.rationem.busRec.doc.DocLineFiTemplateRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.exception.BacException;
import com.rationem.helper.RestrictFundBalance;
import com.rationem.helper.TemplSelectOption;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.UUID;
import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import java.util.logging.Logger;
import javax.faces.component.EditableValueHolder;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author user
 */
public class GlTemplateJnl extends DocCommon {
 private static final Logger logger = Logger.getLogger(GlTemplateJnl.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 @EJB
 private ProgrammeMgr progMgr;
 
 @EJB
 private CostCentreMgr costCentMgr;
 
 @EJB
 private DocumentManager docMgr;
 
 private List<SelectItem> tmpJnlTypSel;
 private String tmpJnlTyp;
 private CompanyBasicRec compSel;
 private DocFiTemplAccrPrePayRec tmplJnl;
 private List<DocFiTemplAccrPrePayRec> accrJnls;
 private List<DocFiTemplAccrPrePayRec> accrualsSelected;
 private List<DocTypeRec> docTypeList;
 private List<UomRec> uomList;
 private List<PostTypeRec> postTypes;
 private List<DocLineFiTemplGlRec> glLines;
 private DocLineFiTemplGlRec glLine;
 private DocLineFiTemplGlRec glLineSel;
 private List<FiGlAccountCompRec> glAcnts;
 private List<FundRec> funds;
 private List<CostCentreRec> costCenters;
 private List<ProgrammeRec> programmes;
 private double totalDebit;
 private double totalCredit;
 private double totalBal;
 private int totalLines;
 private double preEditAmount;
 private PostTypeRec preEditPostTy;
 private boolean docPosted = false;
 private TemplSelectOption accrualSelOpt;
 private TemplSelectOption provnSelOpt;

 /**
  * Creates a new instance of GlTemplateJnl
  */
 public GlTemplateJnl() {
  
 }

 @PostConstruct
 private void init(){
  if(getCompList() == null){
   MessageUtil.addErrorMessage("compsNone", "errorText");
   return;
  }else{
   this.compSel = getCompList().get(0);
   
  }
  List<DocTypeRec> allDocTypes = sysBuff.getDocTypes();
  docTypeList = new ArrayList<>();
  for(DocTypeRec dt:allDocTypes){
   if(dt.isGlAllowed()){
    docTypeList.add(dt);
   } 
  }
  
  uomList = sysBuff.getUoms();
  
  logger.log(INFO, "uomList from sys Buff {0}", uomList);
  
  ListIterator<UomRec> uomLi = uomList.listIterator();
  while(uomLi.hasNext()){
   UomRec curr = uomLi.next();
   switch(curr.getUomCode()){
    case "DY":
     break;
    case "MTH":
     break;
    case "YR":
     break;
    default:
     uomLi.remove();
     break;
   }
   
  }
  tmpJnlTypSel = new ArrayList<>();
  
  SelectItem nullSel = new SelectItem();
  nullSel.setNoSelectionOption(true);
  String label= this.formTextForKey("select");
  nullSel.setLabel(label);
  tmpJnlTypSel.add(nullSel);
  SelectItem revSel = new SelectItem();
  revSel.setValue("REV");
  label = this.formTextForKey("glJnlTemplRev");
  
  revSel.setLabel(label);
  tmpJnlTypSel.add(revSel);
  SelectItem revRecur = new SelectItem();
  revRecur.setValue("REC");
  label = this.formTextForKey("glJnlTemplRec");
  revRecur.setLabel(label);
  tmpJnlTypSel.add(revRecur);
  
  
 }

 public List<DocFiTemplAccrPrePayRec> getAccrJnls() {
 
  return accrJnls;
 }

 public void setAccrJnls(List<DocFiTemplAccrPrePayRec> accrJnls) {
  this.accrJnls = accrJnls;
 }

 public List<DocFiTemplAccrPrePayRec> getAccrualsSelected() {
  return accrualsSelected;
 }

 public void setAccrualsSelected(List<DocFiTemplAccrPrePayRec> accrualsSelected) {
  this.accrualsSelected = accrualsSelected;
 }

 
 public TemplSelectOption getAccrualSelOpt() {
  if(accrualSelOpt == null){
   accrualSelOpt = new TemplSelectOption();
   accrualSelOpt.setComp(compSel);
   Date curr = new Date();
   GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance(compSel.getLocale());
   cal.setTime(curr);
   int minDay = cal.getMinimum(GregorianCalendar.DAY_OF_MONTH);
   cal.set(GregorianCalendar.DAY_OF_MONTH, minDay);
   Date frDt = cal.getTime();
   int maxDay = cal.getMaximum(GregorianCalendar.DAY_OF_MONTH);
   cal.set(GregorianCalendar.DAY_OF_MONTH, maxDay);
   Date toDt = cal.getTime();
   accrualSelOpt.setDocDateFrom(frDt);
   accrualSelOpt.setDocDateTo(toDt);
  }
  return accrualSelOpt;
 }

 public void setAccrualSelOpt(TemplSelectOption accrualSelOpt) {
  this.accrualSelOpt = accrualSelOpt;
 }

 
 public List<DocTypeRec> getDocTypeList() {
  return docTypeList;
 }

 public void setDocTypeList(List<DocTypeRec> docTypeList) {
  this.docTypeList = docTypeList;
 }

 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<CostCentreRec> getCostCenters() {
  return costCenters;
 }

 public void setCostCenters(List<CostCentreRec> costCenters) {
  this.costCenters = costCenters;
 }

 
 public List<FundRec> getFunds() {
  return funds;
 }

 public void setFunds(List<FundRec> funds) {
  this.funds = funds;
 }

 
 public List<FiGlAccountCompRec> getGlAcnts() {
  return glAcnts;
 }

 public void setGlAcnts(List<FiGlAccountCompRec> glAcnts) {
  this.glAcnts = glAcnts;
 }

 public DocLineFiTemplGlRec getGlLineSel() {
  return glLineSel;
 }

 public void setGlLineSel(DocLineFiTemplGlRec glLineSel) {
  this.glLineSel = glLineSel;
 }

 
 
 public List<DocLineFiTemplGlRec> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineFiTemplGlRec> glLines) {
  this.glLines = glLines;
 }

 public DocLineFiTemplGlRec getGlLine() {
  return glLine;
 }

 public void setGlLine(DocLineFiTemplGlRec glLine) {
  this.glLine = glLine;
 }

 
 public List<PostTypeRec> getPostTypes() {
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }

 public List<ProgrammeRec> getProgrammes() {
  return programmes;
 }

 public void setProgrammes(List<ProgrammeRec> programmes) {
  this.programmes = programmes;
 }

 public TemplSelectOption getProvnSelOpt() {
  return provnSelOpt;
 }

 public void setProvnSelOpt(TemplSelectOption provnSelOpt) {
  this.provnSelOpt = provnSelOpt;
 }

 

 public DocFiTemplAccrPrePayRec getTmplJnl() {
  if(tmplJnl == null){
   tmplJnl = new DocFiTemplAccrPrePayRec();
   tmplJnl.setCompany(compSel);
   Date currDate = new Date();
   tmplJnl.setDocumentDate(currDate);
   //tmplJnl.setPostingDate(currDate);
   tmplJnl.setTaxDate(currDate);
   FiscalPeriodYearRec fisPerYr = sysBuff.getCompFiscalPeriodYearForDate(compSel, currDate);
   tmplJnl.setFisYear(fisPerYr.getYear());
   tmplJnl.setFisPeriod(fisPerYr.getPeriod());
  }
  return tmplJnl;
 }

 public void setTmplJnl(DocFiTemplAccrPrePayRec tmplJnl) {
  this.tmplJnl = tmplJnl;
 }

 
 public String getTmpJnlTyp() {
  return tmpJnlTyp;
 }

 public void setTmpJnlTyp(String tmpJnlTyp) {
  this.tmpJnlTyp = tmpJnlTyp;
 }
 
 
 public List<SelectItem> getTmpJnlTypSel() {
  
  return tmpJnlTypSel;
 }

 public void setTmpJnlTypSel(List<SelectItem> tmpJnlTypSel) {
  this.tmpJnlTypSel = tmpJnlTypSel;
 }

 
 public List<UomRec> getUomList() {
  return uomList;
 }

 public double getTotalDebit() {
  return totalDebit;
 }

 public void setTotalDebit(double totalDebit) {
  this.totalDebit = totalDebit;
 }

 public double getTotalCredit() {
  return totalCredit;
 }

 public void setTotalCredit(double totalCredit) {
  this.totalCredit = totalCredit;
 }

 public double getTotalBal() {
  return totalBal;
 }

 public void setTotalBal(double totalBal) {
  this.totalBal = totalBal;
 }

 public int getTotalLines() {
  return totalLines;
 }

 public void setTotalLines(int totalLines) {
  this.totalLines = totalLines;
 }

 public void setUomList(List<UomRec> uomList) {
  this.uomList = uomList;
 }
 public List<CostCentreRec> costCentreComplete(String input){
  
  if(input == null || input.isEmpty()){
   return costCenters;
  }else{
   List<CostCentreRec> retList = new ArrayList<>();
   for(CostCentreRec curr:costCenters){
    if(curr.getRefrence().startsWith(input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
 public List<FundRec> fundComplete(String input){
  
  if(input == null || input.isEmpty()){
   return funds;
  }else{
   List<FundRec> retList = new ArrayList<>();
   ListIterator<FundRec> fndLi = funds.listIterator();
   while(fndLi.hasNext()){
    FundRec curr = fndLi.next();
    if(curr.getName().startsWith(input)){
     retList.add(curr);
    }
   }
   return retList;
  }
 }
 
 public List<ProgrammeRec> programmeComplete(String input){
  
  if(input == null || input.isEmpty()){
   return programmes;
  }else{
   List<ProgrammeRec> retList = new ArrayList<>();
   for(ProgrammeRec curr:programmes){
    if(curr.getReference().startsWith(input)){
     retList.add(curr);
    }
   }
   return retList;
  }
  
 }
 public void onDeleteConfirm(){
  logger.log(INFO, "onDeleteConfirm called with selected account {0}", this.glLineSel.getGlAccount().getCoaAccount().getRef());
  ListIterator<DocLineFiTemplGlRec> lineLi = this.glLines.listIterator();
  boolean found = false;
  while(lineLi.hasNext() && !found){
   DocLineFiTemplGlRec curr = lineLi.next();
   if(Objects.equals(curr.getId(), glLineSel.getId())){
    lineLi.remove();
    RequestContext rCtx = RequestContext.getCurrentInstance();
    rCtx.update("linesOverview");
    rCtx.execute("PF('delLnWv').hide()");
    
    return;
   }
           
  }
 }
 public void onDocAmountChange(ValueChangeEvent evt){
  logger.log(INFO, "Amount change class {0}", evt.getNewValue());
  if(evt.getNewValue() == null){
   return;
  }
  String className = evt.getNewValue().getClass().getSimpleName();
  
  double amnt = Double.parseDouble(evt.getNewValue().toString());
  this.glLine.setHomeAmount(amnt);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("addLnBtn");
  
 }
 
 public String onDocEntryFlow(FlowEvent evt){
  logger.log(INFO, "Curr step {0} next step {1}", new Object[]{evt.getOldStep(),evt.getNewStep()});
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  if(currStep.equals("header") && nextStep.equals("lineTb")){
   tmplJnl.setCompany(compSel);
   // move header to line
   if(glLine == null){
    glLine = new DocLineFiTemplGlRec();
   }
   if(postTypes == null || postTypes.isEmpty()){
    postTypes = sysBuff.getPostCodesForLedger("GL");
   }
   if(glAcnts == null || glAcnts.isEmpty() ){
    glAcnts = glAcntMgr.getCompanyAccounts(this.tmplJnl.getCompany());
   }
   glLine.setGlAccount(glAcnts.get(0));
   String sortStr = this.determineSortOrder(tmplJnl, glLine, glLine.getGlAccount(), tmplJnl.getCompany().getLocale());
   glLine.setSortOrder(sortStr);
   if(tmplJnl.getCompany().isRestrictedFunds() && funds == null){
    try{
     funds = sysBuff.getRestrictedFundsForComp(compSel);
     
    }catch(BacException ex){
     logger.log(INFO, "No funds found");
     MessageUtil.addWarnMessage("fiDocNoFnds", "validationText");
    }
   }
   if(programmes == null ||programmes.isEmpty() ){
    programmes = progMgr.getAllProgrammes(tmplJnl.getCompany());
   }
   if(costCenters == null || costCenters.isEmpty()){
    costCenters = this.costCentMgr.getCostCentresForCompany(tmplJnl.getCompany());
   }
   logger.log(INFO, "Comp has restricted funds {0}", tmplJnl.getCompany().isRestrictedFunds());
   
  }
  return nextStep;
 }
 
 public void onEditNewLnShow(){
  logger.log(INFO, "onEditNewLnShow called with selected {0}", this.glLineSel);
  preEditAmount = glLineSel.getHomeAmount();
  preEditPostTy = glLineSel.getPostType();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editLnDlg");
  rCtx.execute("PF('editLnDlgWv').show()");
 }
 
 public void onEditNewLnTrf(){
  logger.log(INFO, "onEditNewLnTrf called");
  
  
  boolean found = false;
  ListIterator<DocLineFiTemplGlRec> lnLi = glLines.listIterator();
  while(lnLi.hasNext() && !found){
   DocLineFiTemplGlRec curr = lnLi.next();
   if(Objects.equals(curr.getId(), glLineSel.getId())){
    // remove original entry
    if(preEditPostTy.isDebit()){
     totalDebit -= preEditAmount;
     totalBal -= preEditAmount;
    }else{
     totalCredit -= preEditAmount;
     totalBal += preEditAmount;
    }
    // set new values
    if(glLineSel.getPostType().isDebit()){
     totalDebit += glLineSel.getHomeAmount();
     totalBal += glLineSel.getHomeAmount();
    }else{
     totalCredit += glLineSel.getHomeAmount();
     totalBal -= glLineSel.getHomeAmount();
    }
    logger.log(INFO, "Total debit {0} credit {1}", new Object[]{totalDebit,totalCredit});
    lnLi.set(glLineSel);
    found = true;
   }
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("linesOverview");
  updates.add("listtTotals");
  updates.add("savebtn");
  rCtx.update(updates);
  rCtx.execute("PF('editLnDlgWv').hide()");
  
 }
 public void onFreqSelect(ValueChangeEvent evt){
  UomRec curr = (UomRec)evt.getNewValue();
  logger.log(INFO, "New uom pc code {0}", curr.getProcessCode());
  String procCd = curr.getProcessCode().trim();
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(tmplJnl.getPostingDate());
  switch (procCd){
   case "DY":
    logger.log(INFO, "DY proc");
    cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
    break;
   case "MTH_CAL":
    cal.add(GregorianCalendar.MONTH, 1);
    break;
   case "YR_CAL":
    cal.add(GregorianCalendar.YEAR, 1);
    break;
  }
  tmplJnl.setNextPostDate(cal.getTime());
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("startDate");
 }
 
 public void onGlAcntChange(ValueChangeEvent evt){
  FiGlAccountCompRec curr = (FiGlAccountCompRec)evt.getNewValue();
  this.glLine.setGlAccount(curr);
  String sort = this.determineSortOrder(tmplJnl, glLine, curr, tmplJnl.getCompany().getLocale());
  glLine.setSortOrder(sort);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("acntName");
  updates.add("lnSort");
  rCtx.update(updates);
 }
 
 public String onAccrPostFlow(FlowEvent evt){
  
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  if(currStep.equals("select") && nextStep.equals("post")){
   logger.log(INFO, "From select to post");
   accrJnls = docMgr.getAccrGlJnlOpen(accrualSelOpt);
   logger.log(INFO, "onAccrPostFlow accrList {0}", accrJnls);
   if(accrJnls == null || accrJnls.isEmpty()){
    MessageUtil.addWarnMessage("docAccrOpenNf", "errorText");
   // RequestContext rCtx = RequestContext.getCurrentInstance();
   // rCtx.update("accrNfMsg");
    
    
   }
  }else if(currStep.equals("post") && nextStep.equals("select")){
   logger.log(INFO, "From post to select ");
  }
  
  return nextStep;       
  
 }
 
 public void onAccrPostDateSelect(SelectEvent evt){
  accrualSelOpt.setPostDate((Date)evt.getObject());
  logger.log(INFO, "Post Date {0}", evt.getObject());
 }
 public void onAccrualPostToggle(ToggleSelectEvent evt){
  logger.log(INFO, "onAccrualPostToggle called with selected {0}", evt.isSelected());
  logger.log(INFO, "Selected {0}", this.accrualsSelected);
  if(evt.isSelected()){
   int num = accrualsSelected.size();
   MessageUtil.addInfoMessageVar1("accrPostSelAll", "blacResponse", String.valueOf(num));
  }else{
   MessageUtil.addInfoMessage("accrPostUnSelAll", "blacResponse");
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("selectGr");
  updates.add("pstAccrSelBtn");
  rCtx.update(updates);
 }
 
 public void onAccrualPostChecked(SelectEvent evt){
  logger.log(INFO, "onAccrualPostChecked called with selected {0}", evt.getObject());
  logger.log(INFO, "Selected {0}", this.accrualsSelected);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("pstAccrSelBtn");
  
  
 }
 
 public void onAccualPostUnchecked(UnselectEvent evt){
  logger.log(INFO, "onAccrualPostUnchecked called with selected {0}", evt.getObject());
  logger.log(INFO, "Selected {0}", this.accrualsSelected); 
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("pstAccrSelBtn");
  
 }
 public void onAddLineBtn(){
  logger.log(INFO, "onAddLineBtn");
  UUID uuid = UUID.randomUUID();
  glLine.setId(uuid.getLeastSignificantBits());
  double amount = glLine.getHomeAmount();
  if(glLine.getPostType().isDebit()){
   this.totalDebit += amount;
   this.totalBal += amount;
  }else{
   this.totalCredit += amount;
   this.totalBal -= amount;
  }
  if(this.glLines == null){
   glLines = new ArrayList<>();
  }
  logger.log(INFO, "glLine post type {0}", glLine.getPostType().getPostTypeCode());
  if(glLine.getPostType() == null){
   glLine.setPostType(postTypes.get(0));
  }
  glLines.add(glLine);
  totalLines = glLines.size();
  glLine = new DocLineFiTemplGlRec();
  glLine.setPostType(postTypes.get(0));
  glLine.setGlAccount(glAcnts.get(0));
  String Sort = determineSortOrder(tmplJnl, glLine, glLine.getGlAccount(), tmplJnl.getCompany().getLocale());
  glLine.setSortOrder(Sort);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  updates.add("linePg");
  updates.add("docTotalsPG");
  rCtx.update(updates);
 }
 
 public void onPostDateSelect(SelectEvent evt){
  Date pstDate = (Date)evt.getObject();
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("period");
  rCtx.update("fisYr");
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
   cal.setTime(pstDate);
   logger.log(INFO, "tmpJnlTyp {0}", tmpJnlTyp);
  switch (tmpJnlTyp) {
   case "REV":
    cal.add(GregorianCalendar.MONTH, 1);
    tmplJnl.setNextPostDate(cal.getTime());
    rCtx.update("tmpHdrPnl");
    break;
   case "REC":
    tmplJnl.setNumRecur(1);
     if(this.uomList == null || uomList.isEmpty()){
      uomList = this.sysBuff.getUoms();
     }
     UomRec curr = uomList.get(0);
     logger.log(INFO, "next posting {0}", tmplJnl.getNextPostDate());
     logger.log(INFO, "UOm proc code {0}", curr.getProcessCode());
     String procCd = curr.getProcessCode();
     logger.log(INFO, "procCd size {0}", procCd.length());
     procCd = procCd.trim();
     logger.log(INFO, "procCd size trim {0}", procCd.length());
     switch (procCd){
      case "DY":
       logger.log(INFO, "DY proc");
       cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
       break;
      case "MTH_CAL":
       cal.add(GregorianCalendar.MONTH, 1);
       break;
      case "YR_CAL":
       cal.add(GregorianCalendar.YEAR, 1);
       break;
     }
     logger.log(INFO, "cal {0}", cal.getTime());
     tmplJnl.setNextPostDate(cal.getTime());
    rCtx.update("tmpHdrPnl");
    break;
  }
 }
 
 public String onSaveNewTemplJnlAction(){
  String ret = docPosted ?"home":null;
  return ret;
 }
 
 public void onSaveAccrRev(){
  logger.log(INFO, "onSaveAccrRev called with {0}",accrualsSelected);
  
  accrualsSelected = this.docMgr.postAccrGlReversal(accrJnls, accrualSelOpt, this.getLoggedInUser(),
          getView());
  logger.log(INFO, "accrualsSelected after save by doc Mgr {0}", accrualsSelected);
 }
 public void onSaveNewTemplJnl(){
  logger.log(INFO, "onSaveNewTemplJnl called with {0}", this.tmplJnl);
  RequestContext rCtx = RequestContext.getCurrentInstance();
  if(this.glLines == null || glLines.isEmpty()){
   MessageUtil.addErrorMessage("docNoLines", "errorText");
   rCtx.update("overviewMsg");
   return;
  }
  
  LineTypeRuleRec templLineTy = this.sysBuff.getLineTypeRuleByCode("GLJNLTEMPL");
  logger.log(INFO, "templLineTy {0}", templLineTy);
  if(tmplJnl.getCompany().isFundAccounting()){
   //need to check that each fund balances
   List<RestrictFundBalance> fndBals = new ArrayList<>();
   for (DocLineFiTemplGlRec curr : glLines) {
    if(curr.getRestrictedFund() != null){
     long fundId = curr.getRestrictedFund().getId();
     boolean found = false;
     if(!fndBals.isEmpty()){
      ListIterator<RestrictFundBalance> fndLi = fndBals.listIterator();
      while(fndLi.hasNext() && !found){
       RestrictFundBalance fndAmnt =  fndLi.next();
       if( Objects.equals(fndAmnt.getFund().getId(), curr.getRestrictedFund().getId())   ){
        //found fund gl account entry
        double val = fndAmnt.getAmount();
        if(curr.getPostType().isDebit()){
         val += curr.getHomeAmount();
        }else{
         val -= curr.getHomeAmount();
        }
        fndAmnt.setAmount(val);
        fndLi.set(fndAmnt);
        found = true;
       }
      }
     }
     if(!found){
      // need to create new fundAmnt
      RestrictFundBalance fndAmnt = new RestrictFundBalance();
      fndAmnt.setFund(curr.getRestrictedFund());
      double val = 0;
      if(curr.getPostType().isDebit()){
       val += curr.getHomeAmount();
      }else{
       val -= curr.getHomeAmount();
      }
      fndAmnt.setAmount(val);
      fndBals.add(fndAmnt);
     }
    }
   }
   boolean fundBal = false;
   for(RestrictFundBalance curr:fndBals ){
    if(curr.getAmount() != 0){
     fundBal = true;
     MessageUtil.addErrorMessageParam1("docFndBal", "errorText", curr.getFund().getName());
     rCtx.update("overviewMsg");
     
    }
   }
   if(fundBal){
    return;
   }
  }
  
  UserRec crUsr = this.getLoggedInUser();
  Date currDt = new Date();
  ListIterator<DocLineFiTemplGlRec> linesLi = glLines.listIterator();
  
  long lineNum = 0;
  
  while(linesLi.hasNext()){
   DocLineFiTemplGlRec curr = linesLi.next();
   lineNum++;
   curr.setId(null);
   curr.setLineNum(lineNum);
   curr.setCreateBy(crUsr);
   curr.setCreateDate(currDt);
   curr.setComp(tmplJnl.getCompany());
   curr.setLineType(templLineTy);
   curr.setDoc(tmplJnl);
   linesLi.set(curr);
   
  }
  tmplJnl.setId(null);
  tmplJnl.setCreatedBy(crUsr);
  tmplJnl.setCreateOn(currDt);
  List<DocLineFiTemplateRec> templLines = new ArrayList<>();
  for(DocLineFiTemplGlRec glLn:glLines){
   DocLineFiTemplateRec tmplLn = glLn;
   templLines.add(tmplLn);
  }
  tmplJnl.setDocLines(templLines);
  TransactionTypeRec tt = sysBuff.getTransactionTypeRecByCode("glTempJnl");
  logger.log(INFO, "Trans ty {0}", tt);
  tmplJnl.setTransactionType(tt);
  try{
  tmplJnl = docMgr.templateJnlUpdate(tmplJnl,getView());
  docPosted = true;
  MessageUtil.addInfoMessageVar1("templJnlSaved", "blacResponse", String.valueOf(tmplJnl.getDocNumber()));
  }catch(Exception ex){
   MessageUtil.addErrorMessage("docTmplPost", "errorText");
  }
  
 }
 
 public void onTemplTypeSelect(ValueChangeEvent evt){
  logger.log(INFO, "Item select called {0} ", evt.getNewValue());
  tmpJnlTyp = (String)evt.getNewValue();
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(tmplJnl.getPostingDate());
  switch (tmpJnlTyp) {
   case "REV":
    cal.add(GregorianCalendar.MONTH, 1);
    tmplJnl.setNextPostDate(cal.getTime());
    tmplJnl.setTmplType(DocFiTemplAccrPrePayRec.REVERSING);
    break;
   case "REC":
    tmplJnl.setNumRecur(1);
    tmplJnl.setTmplType(DocFiTemplAccrPrePayRec.RECURRING);
    if(this.uomList == null || uomList.isEmpty()){
     uomList = this.sysBuff.getUoms();
    }
    UomRec curr = uomList.get(0);
    logger.log(INFO, "next posting {0}", tmplJnl.getNextPostDate());
    logger.log(INFO, "UOm proc code {0}", curr.getProcessCode());
    String procCd = curr.getProcessCode();
    logger.log(INFO, "procCd size {0}", procCd.length());
    procCd = procCd.trim();
    logger.log(INFO, "procCd size trim {0}", procCd.length());
    switch (procCd){
     case "DY":
      logger.log(INFO, "DY proc");
      cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
      break;
     case "MTH_CAL":
      cal.add(GregorianCalendar.MONTH, 1);
      break;
     case "YR_CAL":
      cal.add(GregorianCalendar.YEAR, 1);
      
      break;
    }
    logger.log(INFO, "cal {0}", cal.getTime());
    
    tmplJnl.setNextPostDate(cal.getTime());
    logger.log(INFO, "next posting {0}", tmplJnl.getNextPostDate());
    break;
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("tmpHdrPnl");
 }
 
 public void validateDocAmount(FacesContext c, UIComponent uiComp, Object val){
  logger.log(INFO, "validateDocAmount called with {0}", val);
  double amnt = Double.parseDouble(val.toString());
  boolean isNeg = amnt < 0.0;
  if(isNeg){
   
   glLine.setHomeAmount(0);
   MessageUtil.addWarnMessage("fiDocAmntNeg", "validationText");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("lnAmnt");
   rCtx.update("addLnBtn");
   rCtx.update("linGr");
   ((UIInput)uiComp).setValid(false);
  }else{
   ((UIInput)uiComp).setValid(true);
  }
  
 }
 public void validateAccrPostDate(FacesContext c, UIComponent uiComp, Object val){
  logger.log(INFO, "validateAccrPostDate called with {0}", val);
  Date pstDt = (Date)val;
  
  FiscalPeriodYearRec fisPer = sysBuff.getCompFiscalPeriodYearForDate(accrualSelOpt.getComp(), pstDt);
  String perStr =  fisPer.getPeriod() < 10?"0"+String.valueOf(fisPer.getPeriod()):String.valueOf(fisPer.getPeriod());
  int yrPer = Integer.parseInt(String.valueOf(fisPer.getYear())+perStr);
  List<CompPostPerRec> openPeriods = accrualSelOpt.getComp().getCompanyPostingPeriods();
  if(openPeriods == null){
   accrualSelOpt.setComp(sysBuff.getCompAvailPostPeriod(accrualSelOpt.getComp()));
   openPeriods = accrualSelOpt.getComp().getCompanyPostingPeriods();
  }
  
  RequestContext rCtx = RequestContext.getCurrentInstance();
  List<String> updates = new ArrayList<>();
  for(CompPostPerRec openPer: openPeriods){
   if (openPer.getLedger().getCode().equals("GL")){
    if(yrPer >= openPer.getStartLong() && yrPer <= openPer.getEndLong()){
     ((UIInput)uiComp).setValid(true);
     updates.add("pstDate");
     updates.add("msgs");
     rCtx.update(updates);
     return;
    }
   }
  }
  logger.log(INFO, "invalid post date");
  
  
  MessageUtil.addErrorMessage("fiDocPerNotOpen", "validationText");
  ((EditableValueHolder)uiComp).setValid(false);
  
  updates.add("pstDate");
  updates.add("msgs");
  rCtx.update(updates);
  
  
 }
 
 public void validatePostDate(FacesContext c, UIComponent uiComp, Object val){
  logger.log(INFO, "validatePostDate called with {0}", val);
  Date pstDt = (Date)val;
  FiscalPeriodYearRec fisPer = getFiscPerYr(pstDt);
  logger.log(INFO, "Validate check peryear year {0} per {1}", new Object[]{fisPer.getYear(),fisPer.getPeriod()});
  logger.log(INFO, "per as string {0}", String.valueOf(fisPer.getPeriod()));
  String perStr =  fisPer.getPeriod() < 10?"0"+String.valueOf(fisPer.getPeriod()):String.valueOf(fisPer.getPeriod());
  
  int yrPer = Integer.parseInt(String.valueOf(fisPer.getYear())+perStr);
  logger.log(INFO, "yrPer {0}", yrPer);
   logger.log(INFO, "docComp {0}", tmplJnl.getCompany());  
   CompanyBasicRec docComp = tmplJnl.getCompany();
   if(tmplJnl.getCompany().getCompanyPostingPeriods() == null){
    CompanyBasicRec comp = this.sysBuff.getCompAvailPostPeriod(tmplJnl.getCompany());
    logger.log(INFO, "comp avail per after call to sys buff {0}", comp.getCompanyPostingPeriods());
   }
  logger.log(INFO, "openPeriods {0}", docComp.getCompanyPostingPeriods()); 
  
  
  List<CompPostPerRec> openPeriods = docComp.getCompanyPostingPeriods();
  logger.log(INFO, "openPeriods {0}", openPeriods);
  for(CompPostPerRec openPer: openPeriods){
   // only gl needs to be open
   logger.log(INFO, "openPer led {0}", openPer.getLedger().getCode());
   if (openPer.getLedger().getCode().equals("GL")){
    logger.log(INFO, "yrPer {0} start {1} end {2}", new Object[]{yrPer,openPer.getStartLong(),openPer.getEndLong()});
    if(yrPer >= openPer.getStartLong() && yrPer <= openPer.getEndLong()){
     
     tmplJnl.setFisYear(fisPer.getYear());
     tmplJnl.setFisPeriod(fisPer.getPeriod());
     logger.log(INFO, "found valid period");
     ((UIInput)uiComp).setValid(true);
     RequestContext rCtx = RequestContext.getCurrentInstance();
     List<String> toUpdate = new ArrayList<>();
     toUpdate.add("period");
     toUpdate.add("fisYr");
     rCtx.update("messages");
     rCtx.update(toUpdate);
     return;
    }
   }
  }
  // if we get here then not valid
  logger.log(INFO, "Period not valid");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  MessageUtil.addErrorMessage("fiDocPerNotOpen", "validationText");
  
  tmplJnl.setPostingDate(null);
  rCtx.update("messages");
  rCtx.update("postDate");
  ((UIInput)uiComp).setValid(false);
 }

 @Override
 public DocFiRec getFiDoc() {
  if(this.fiDoc == null){
   fiDoc = new DocFiRec();
  }
  return fiDoc;
 }

 @Override
 public void setFiDoc(DocFiRec fiDoc) {
  this.fiDoc = fiDoc;
 }

}
