/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.doc;

import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocFiTemplAccrPrePayRec;
import com.rationem.busRec.doc.DocLineFiTemplGlRec;
import com.rationem.busRec.doc.DocLineFiTemplateRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.helper.TemplSelectOption;
import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.util.MessageUtil;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.primefaces.event.FlowEvent;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.apache.commons.lang3.SerializationUtils;
/**
 *
 * @author user
 */
public class GlTemplateJnlDisplay extends BaseBean {
 private static final Logger logger = Logger.getLogger(GlTemplateJnlDisplay.class.getName());

 @EJB
 private DocumentManager docMgr;
 
 @EJB
 private SysBuffer sysBuff;
 /**
  * Creates a new instance of GlTemplateJnlDisplay
  */
 
 private List<DocFiTemplAccrPrePayRec> glTemplates;
 private DocFiTemplAccrPrePayRec glTemplSelected;
 private DocFiTemplAccrPrePayRec glTemplEdit;
 private List<DocLineFiTemplGlRec> glSelectedLines;
 private DocLineFiTemplGlRec glSelectedLine;
 private DocLineFiTemplGlRec glSelectedLineEdit;
 private DocLineFiTemplGlRec glSelectedLineAdd;
 private List<DocTypeRec> docTypes;
 private List<PostTypeRec> postTypes;
 private List<FiGlAccountCompRec> glAccounts;
 private List<FundRec> funds;
 private List<CostCentreRec> costCentres;
 private List<ProgrammeRec> progs;
 private TemplSelectOption selOpts;
    
 private CompanyBasicRec compSel;
 private String templCode;
 private String templDescr;
 private double docBalance;
 private Locale docLocale;
 public GlTemplateJnlDisplay() {
 }
 @PostConstruct
 private void init(){
  try{
  compSel = this.getCompList().get(0);
  logger.log(INFO, "compSel Local {0}", compSel.getLocale());
  docLocale = compSel.getLocale();
  logger.log(INFO, "docLocale {0}", docLocale);
  }catch(IndexOutOfBoundsException ex){
   MessageUtil.addErrorMessage("compsNone", "errorText");
  }
  selOpts = new TemplSelectOption();
  selOpts.setComp(compSel);
  selOpts.setTemplType("ALL");
 }
 
 
 public CompanyBasicRec getCompSel() {
  return compSel;
 }

 public void setCompSel(CompanyBasicRec compSel) {
  this.compSel = compSel;
 }

 public List<CostCentreRec> getCostCentres() {
  if(costCentres == null){
   costCentres = this.sysBuff.getCostCentForComp(compSel.getId());
  }
  return costCentres;
 }

 public void setCostCentres(List<CostCentreRec> costCentres) {
  this.costCentres = costCentres;
 }

 
 public double getDocBalance() {
  return docBalance;
 }

 public void setDocBalance(double docBalance) {
  this.docBalance = docBalance;
 }

 public Locale getDocLocale() {
  return docLocale;
 }

 public void setDocLocale(Locale docLocale) {
  this.docLocale = docLocale;
 }

 
 
 public List<DocTypeRec> getDocTypes() {
  if(docTypes == null){
   docTypes = sysBuff.getDocTypes();
  }
  return docTypes;
 }

 public void setDocTypes(List<DocTypeRec> docTypes) {
  this.docTypes = docTypes;
 }

 
 public TemplSelectOption getSelOpts() {
  return selOpts;
 }

 public void setSelOpts(TemplSelectOption selOpts) {
  this.selOpts = selOpts;
 }

 public List<FundRec> getFunds() {
  if(funds == null){
   funds = compSel.getFundList();
   logger.log(INFO, "Compsel funds {0}", funds);
   if(funds == null || funds.isEmpty()){
    compSel = sysBuff.getCompFunds(compSel);
    funds = compSel.getFundList();
   }
   
  }
  logger.log(INFO, "web getFunds returns {0}", funds);
  return funds;
 }

 public void setFunds(List<FundRec> funds) {
  this.funds = funds;
 }
 
 
 
 public List<DocFiTemplAccrPrePayRec> getGlTemplates() {
  return glTemplates;
 }

 public void setGlTemplates(List<DocFiTemplAccrPrePayRec> glTemplates) {
  this.glTemplates = glTemplates;
 }

 public String getTemplCode() {
  return templCode;
 }

 public void setTemplCode(String templCode) {
  this.templCode = templCode;
 }

 public String getTemplDescr() {
  
  templDescr = this.formTextForKey(glTemplSelected.getTmplTypeCode());
  return templDescr;
 }

 public void setTemplDescr(String templDescr) {
  this.templDescr = templDescr;
 }

 public List<FiGlAccountCompRec> getGlAccounts() {
  if(glAccounts == null){
   glAccounts = sysBuff.getGlAccountsByCompanyCode(glTemplSelected.getCompany());
   Collections.sort(glAccounts, new FiGlAccountCompByRef());
  }
  return glAccounts;
 }

 public void setGlAccounts(List<FiGlAccountCompRec> glAccounts) {
  this.glAccounts = glAccounts;
 }

 public DocFiTemplAccrPrePayRec getGlTemplEdit() {
  return glTemplEdit;
 }

 public void setGlTemplEdit(DocFiTemplAccrPrePayRec glTemplEdit) {
  this.glTemplEdit = glTemplEdit;
 }

 
 public DocFiTemplAccrPrePayRec getGlTemplSelected() {
  return glTemplSelected;
 }

 public void setGlTemplSelected(DocFiTemplAccrPrePayRec glTemplSelected) {
  this.glTemplSelected = glTemplSelected;
 }

 public DocLineFiTemplGlRec getGlSelectedLine() {
  return glSelectedLine;
 }

 public void setGlSelectedLine(DocLineFiTemplGlRec glSelectedLine) {
  this.glSelectedLine = glSelectedLine;
 }

 public DocLineFiTemplGlRec getGlSelectedLineAdd() {
  if(glSelectedLineAdd == null){
   glSelectedLineAdd = new DocLineFiTemplGlRec();
  }
  return glSelectedLineAdd;
 }

 public void setGlSelectedLineAdd(DocLineFiTemplGlRec glSelectedLineAdd) {
  this.glSelectedLineAdd = glSelectedLineAdd;
 }

 
 public DocLineFiTemplGlRec getGlSelectedLineEdit() {
  return glSelectedLineEdit;
 }

 public void setGlSelectedLineEdit(DocLineFiTemplGlRec glSelectedLineEdit) {
  this.glSelectedLineEdit = glSelectedLineEdit;
 }

 
 public List<DocLineFiTemplGlRec> getGlSelectedLines() {
  return glSelectedLines;
 }

 public void setGlSelectedLines(List<DocLineFiTemplGlRec> glSelectedLines) {
  this.glSelectedLines = glSelectedLines;
 }

 public List<CostCentreRec> onCostCentreComplete(String input){
  
  if(input == null || input.isEmpty()){
   return this.getCostCentres();
  }
  List<CostCentreRec> ccList = new ArrayList<>();
  for(CostCentreRec ccRec: costCentres){
   if(ccRec.getRefrence().contains(input)){
    ccList.add(ccRec);
   }
  }
  return ccList;
 }
 public void onContextMenu(SelectEvent evt){
  glTemplSelected = (DocFiTemplAccrPrePayRec)evt.getObject();
  logger.log(INFO, "Set select jnl {0}", glTemplSelected);
 }
 
 public void onDlgAddShow(){
  glSelectedLineAdd = new DocLineFiTemplGlRec();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addJnlLnFrm");
  pf.executeScript("PF('addJnlLnDlgWv').show();");
  
 }
 
 public void onDlgAddTransfer(){
  long lines = glSelectedLines.size();
  lines++;
  glSelectedLineAdd.setLineNum(lines);
  glSelectedLineAdd.setId(lines * -1);
  glSelectedLines.add(glSelectedLineAdd);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editLinesDt");
  pf.executeScript("PF('addJnlLnDlgWv').hide();");
  
 }
 public void onDlgDisplShow(){
  logger.log(INFO, "onDlgDisplShow called with jnl {0}", this.glTemplSelected);
  logger.log(INFO, "Selected Jnl lines {0}", glTemplSelected.getDocLines());
  if(glTemplSelected.getDocLines() == null){
   glTemplSelected = docMgr.getAccrGlJnlOpenLines(glTemplSelected);
   logger.log(INFO, "doc lines {0}", glTemplSelected.getDocLines());
  }
  glSelectedLines = new ArrayList<>(); 
  double crTot = 0.0;
  double drTot = 0.0;
  for(DocLineFiTemplateRec t: glTemplSelected.getDocLines()){
   DocLineFiTemplGlRec glLn = (DocLineFiTemplGlRec)t;
   if(glLn.getPostType().isDebit()){
    drTot += glLn.getHomeAmount();
   }else{
    crTot += glLn.getHomeAmount();
   }
   glTemplSelected.setTotalCredit(crTot);
   glTemplSelected.setTotalDebit(drTot);
   glSelectedLines.add(glLn);
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("displDetfrm");
  pf.executeScript("PF('displDlgWv').show();");
 }
 
 public void onDlgEditLineDelete(){
  logger.log(INFO, "Called onDlgEditLineDelete", glSelectedLineEdit);
  ListIterator<DocLineFiTemplGlRec> li = glSelectedLines.listIterator();
  boolean foundLn = false;
  while(li.hasNext() && !foundLn){
   DocLineFiTemplGlRec curr = li.next();
   if(Objects.equals(curr.getLineNum(), glSelectedLineEdit.getLineNum())){
    li.remove();
    foundLn = true;
   }
  }
  
 }
 public void onDlgEditLineShow(){
  logger.log(INFO, "onDlgEditShow called with jnl {0}", glSelectedLineEdit);
  logger.log(INFO, "glSelectedLineEdit post type {0}", glSelectedLineEdit.getPostType().getPostTypeCode());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editLineFrm");
  pf.executeScript("PF('editLineWv').show()");
 }
 
 public void onDlgEditLineTransfer(){
  
  ListIterator<DocLineFiTemplGlRec> linesIt =    glSelectedLines.listIterator();
  boolean foundLine = false;
  while(linesIt.hasNext() && !foundLine){
   DocLineFiTemplGlRec curr = linesIt.next();
   if(Objects.equals(curr.getLineNum(), glSelectedLineEdit.getLineNum())){
    logger.log(INFO, "Update line num {0}", curr.getLineNum());
    linesIt.set(glSelectedLineEdit);
    foundLine = true;
    }
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editLinesDt");
  pf.executeScript("PF('editLineWv').hide()");
  
 }
 
 public void onDlgEditShow(){
  logger.log(INFO, "onDlgEditShow called with jnl {0}", this.glTemplSelected);
  logger.log(INFO, "Selected Jnl lines {0}", glTemplSelected.getDocLines());
  glTemplEdit = SerializationUtils.clone(glTemplSelected);
  if(glTemplSelected.getDocLines() == null){
   glTemplEdit = docMgr.getAccrGlJnlOpenLines(glTemplSelected);
   logger.log(INFO, "doc lines {0}", glTemplSelected.getDocLines());
  }
  logger.log(INFO, "glTemplEdit doc lines {0}", glTemplEdit.getDocLines());
  glSelectedLines = new ArrayList<>(); 
  double crTot = 0.0;
  double drTot = 0.0;
  ListIterator<DocLineFiTemplateRec> glTemplIt = glTemplSelected.getDocLines().listIterator();
  while(glTemplIt.hasNext()){
   DocLineFiTemplateRec curr = glTemplIt.next();
   if(curr.getPostType().isDebit()){
    drTot += curr.getHomeAmount();
   }else{
    crTot += curr.getHomeAmount();
   }
   logger.log(INFO, "curr.isLazyLoaded() {0}", curr.isLazyLoaded());
   if(!curr.isLazyLoaded()){
    curr = this.docMgr.getDocLineFiTemplGlRecAddlazy(curr);
    glTemplIt.set(curr);
   }
   glSelectedLines.add((DocLineFiTemplGlRec)curr);
   
   
   logger.log(INFO, "curr lazyLoaded {0} fund {1}", new Object[]{curr.isLazyLoaded(),((DocLineFiTemplGlRec)curr).getRestrictedFund()});
  }
  glTemplEdit.setTotalCredit(crTot);
  glTemplEdit.setTotalDebit(drTot);
  logger.log(INFO, "comp {0}", glTemplEdit.getCompany().getReference());
  docLocale = glTemplEdit.getCompany().getLocale();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editDetfrm");
  pf.executeScript("PF('editDlgWv').show();");
 
 }
 
 public void onDlgEditTransfer(){
  logger.log(INFO, "onDlgEditTransfer called");
  UserRec currUsr = this.getLoggedInUser();
  Date currDate = new Date();
  
  List<DocLineFiTemplateRec> currLines = new ArrayList<>();
  ListIterator<DocLineFiTemplGlRec> linesLi = glSelectedLines.listIterator();
  while(linesLi.hasNext()){
   DocLineFiTemplateRec curr = linesLi.next();
   if(curr.getId() < 0){
    curr.setId(null);
    curr.setCreateBy(currUsr);
    curr.setCreateDate(currDate);
   }else{
    curr.setChangeBy(currUsr);
    curr.setChangeDate(currDate);
   }
   currLines.add(curr);
  }
 glTemplSelected.setChangedBy(currUsr);
 glTemplSelected.setChangedOn(currDate);
 glTemplSelected = glTemplEdit;
 glTemplEdit = null;
 glSelectedLines = null;
 glTemplSelected.setDocLines(currLines);
 try{
 glTemplSelected = this.docMgr.templateJnlUpdate(glTemplSelected, this.getView());
 logger.log(INFO, "Doc hdr text after save {0}", glTemplSelected.getDocHdrText());
 
 ListIterator<DocFiTemplAccrPrePayRec> tmplIt = glTemplates.listIterator();
 boolean foundDoc = false;
 while(tmplIt.hasNext() && !foundDoc){
  DocFiTemplAccrPrePayRec curr = tmplIt.next();
  if(Objects.equals(curr.getId(), glTemplSelected.getId()) ){
   tmplIt.set(glTemplSelected);
   foundDoc = true;
  }
 }
 }catch(Exception ex){
  logger.log(INFO, "Could not save journal {0}", ex.getLocalizedMessage());
 }
 logger.log(INFO, "glTempSelected pre update {0}", glTemplSelected.getDocLines());
 
 PrimeFaces pf = PrimeFaces.current();
 pf.ajax().update("jnlListDt");
 pf.executeScript("PF('editDlgWv').hide();");
  
 }
 public void onDocDtFromSel(SelectEvent evt){
  logger.log(INFO, "Doc from date select {0}", evt.getObject());
  Date curr = (Date)evt.getObject();
  this.selOpts.setDocDateFrom(curr);
  if(selOpts.getDocDateTo() != null && selOpts.getDocDateTo().before(curr)){
  selOpts.setDocDateTo(curr);
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("docDtTo");
 }
 
 public void onDocDtFromChange(ValueChangeEvent evt){
  logger.log(INFO, "Doc from date change {0}", evt.getNewValue());
  Date curr = (Date)evt.getNewValue();
  this.selOpts.setDocDateFrom(curr);
  if(selOpts.getDocDateTo() != null && selOpts.getDocDateTo().before(curr)){
  selOpts.setDocDateTo(curr);
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("docDtTo");
  
 }
 
 
 public void onDocDtToChange(ValueChangeEvent evt){
  logger.log(INFO, "Doc to date change {0}", evt.getNewValue());
  Date curr = (Date)evt.getNewValue();
  this.selOpts.setDocDateTo(curr);
  if(selOpts.getDocDateFrom() != null && selOpts.getDocDateFrom().before(curr)){
   selOpts.setDocDateFrom(curr);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("docDtFr");
  }
 }
 
 public void onDocDtToSel(SelectEvent evt){
  logger.log(INFO, "Doc from date select {0}", evt.getObject());
  Date curr = (Date)evt.getObject();
  this.selOpts.setDocDateTo(curr);
  if(selOpts.getDocDateFrom() != null && selOpts.getDocDateFrom().before(curr)){
   selOpts.setDocDateFrom(curr);
   
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("docDtFr");
   
 }
 
 public void onDocLineDisplToggle(ToggleEvent evt){
  logger.log(INFO, "Toggle Source {0}", evt.getSource());
  logger.log(INFO, "Toggle data {0}", evt.getData());
  if(evt.getVisibility() == Visibility.VISIBLE){
   DocLineFiTemplGlRec ln = (DocLineFiTemplGlRec)evt.getData();
   if(ln.getRestrictedFund() == null){
    ln = (DocLineFiTemplGlRec)docMgr.getDocLineFiTemplGlRecAddlazy(ln);
    ListIterator<DocLineFiTemplGlRec> li =    glSelectedLines.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
     DocLineFiTemplGlRec curr = li.next();
     if(Objects.equals(curr.getId(), ln.getId())){
      li.set(ln);
     }
    }
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("displLazyId");
   }
  }
 }
 
 public List<FundRec> onFundComplete(String input){
  logger.log(INFO, "onFundComplete input {0}", input);
  List<FundRec> fnds = this.getFunds();
  if(input == null || input.isEmpty()){
   return fnds;
  }else{
   List<FundRec> selFnds = new ArrayList<>();
   for(FundRec fnd:fnds){
    if(fnd.getFndCode().contains(input)){
     selFnds.add(fnd);
    }
   }
   return selFnds;
  }
  
 }
 public void onNextDtFromSel(SelectEvent evt){
  logger.log(INFO, "Doc from date select {0}", evt.getObject());
  Date curr = (Date)evt.getObject();
  this.selOpts.setNextDateFrom(curr);
  if(selOpts.getNextDateTo() != null && selOpts.getNextDateTo().before(curr)){
   selOpts.setNextDateTo(curr);
   
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("nextPstTo");
  
 }
 
 
 public void onNextDtFromChange(ValueChangeEvent evt){
  logger.log(INFO, "Doc from date change {0}", evt.getNewValue());
  Date curr = (Date)evt.getNewValue();
  this.selOpts.setNextDateFrom(curr);
  if(selOpts.getNextDateTo() != null && selOpts.getNextDateTo().before(curr)){
   selOpts.setNextDateTo(curr);
   
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("nextPstTo");
  
  
 }
 
 
 public void onNextDtToChange(ValueChangeEvent evt){
  logger.log(INFO, "Doc to date change {0}", evt.getNewValue());
  Date curr = (Date)evt.getNewValue();
  this.selOpts.setNextDateTo(curr);
  if(selOpts.getNextDateFrom() != null && selOpts.getNextDateFrom().before(curr)){
   selOpts.setNextDateFrom(curr);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("nextPstFr");
  }
 }
 
 public void onNextDtToSel(SelectEvent evt){
  logger.log(INFO, "Doc from date select {0}", evt.getObject());
  Date curr = (Date)evt.getObject();
  this.selOpts.setDocDateTo(curr);
  if(selOpts.getNextDateFrom() != null && selOpts.getNextDateFrom().before(curr)){
   selOpts.setNextDateFrom(curr);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("nextPstFr");
  }
 }

 public List<ProgrammeRec> onProgComplete(String input){
  logger.log(INFO, "onProgComplete called with {0}", input);
  List<ProgrammeRec> retList  = this.getProgs();
  if(retList == null || retList.isEmpty()){
   return null;
  }
  logger.log(INFO, "getProgs returned {0}", retList);
  if(input == null || input.isEmpty()){
   return retList;
  }else{
   
   for(ProgrammeRec rec: progs){
    if(rec.getReference().contains(input)){
     retList.add(rec);
    }
    return retList;
   }
  }
  return null;
 }
 public List<ProgrammeRec> getProgs() {
  if(progs == null){
   progs = this.sysBuff.getProgrammeForComp(this.selOpts.getComp().getId());
  }
  return progs;
 }

 public void setProgs(List<ProgrammeRec> progs) {
  this.progs = progs;
 }

 
 public List<PostTypeRec> getPostTypes() {
  if(postTypes == null){
   postTypes = sysBuff.getPostCodesForLedger("GL");
  }
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }
 
 
 public String processEditFlow(FlowEvent evt){
  logger.log(INFO, "processEditFlow called with old {0} new {1}", 
     new Object[]{evt.getOldStep(),evt.getNewStep()});
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  
  if(currStep.equals("editLines") && nextStep.equals("editOverview")){
   docBalance = 0;
   for(DocLineFiTemplGlRec ln: glSelectedLines){
    if(ln.getPostType().isDebit()){
     docBalance += ln.getHomeAmount();
    }else{
     docBalance -= ln.getHomeAmount();
    }
   }
   
   if(docBalance != 0){
    String docBalStr = GenUtil.formatCurrency(docBalance, glTemplEdit.getCompany().getLocale());
    MessageUtil.addErrorMessageParam1("docBal", "errorText", docBalStr);
    nextStep = currStep;
   }
  }
  
  return nextStep;
 }
 public String processSelectFlow(FlowEvent evt){
  logger.log(INFO, "processSelectFlow called with old {0} new {1}", 
     new Object[]{evt.getOldStep(),evt.getNewStep()});
  String currStep = evt.getOldStep();
  String nextStep = evt.getNewStep();
  if(currStep.equals("selOpts") && nextStep.equals("jnlList")){
   // move from selection to display result
   
   glTemplates = this.docMgr.getAccrGlJnlOpen(selOpts);
   logger.log(INFO, "glTemplates returned my DocMgr {0}", glTemplates);
  }
  return nextStep;
  
 }
}
