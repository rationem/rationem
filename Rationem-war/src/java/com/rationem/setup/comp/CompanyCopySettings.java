/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.config.common.VatManager;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.fi.SalesManager;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.PickListItem;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;




import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;


/**
 *
 * @author user
 */
public class CompanyCopySettings extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(CompanyBean.class.getName());
    
    
 @EJB 
 CompanyManager compMgr;
 
 @EJB
 BankManager bankMgr;

 @EJB
 BasicSetup setup;

 @EJB
 SysBuffer sysBuffer;
 
 @EJB
 CostCentreMgr costCentMgr;
 
 @EJB
 GlAccountManager glAcntMgr;
 
 @EJB
 ProgrammeMgr progMgr;
 
 @EJB
 SalesManager salesMgr;
 
 @EJB
 VatManager vatMgr;
 
 private CompanyBasicRec compDest;
 private CompanyBasicRec compSource;
 

 ResourceBundle formText = ResourceBundle.getBundle("com.rationem.localisation.formText");
 private List<PickListItem> sourceList;
 private List<PickListItem> selectedList;
 
 private DualListModel<PickListItem> copySet;
 
 
 

 @PostConstruct
 private void init(){
  selectedList = new ArrayList<>();
  sourceList = new ArrayList<>();
  sourceList.add(this.buildPickListItem("compSettCoA"));
  sourceList.add(this.buildPickListItem("compSettCC"));
  sourceList.add(this.buildPickListItem("compSettGL"));
  sourceList.add(this.buildPickListItem("compSettFund"));
  sourceList.add(this.buildPickListItem("compSettPayTy"));
  sourceList.add(this.buildPickListItem("compSettPerCntrl"));
  sourceList.add(this.buildPickListItem("compSettProg"));
  sourceList.add(this.buildPickListItem("compSettSlPart"));
  sourceList.add(this.buildPickListItem("compSettSlCat"));
  sourceList.add(this.buildPickListItem("compSettVatCode"));
  
  copySet = new DualListModel<>(sourceList,selectedList);
 }
 
 private PickListItem buildPickListItem(String code){
  String description = formText.getString(code);
  PickListItem item = new PickListItem(code,description);
  return item;
 }

private void copyChartofAccount(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "copyChartofAccount called with comp {0} Comp {1}", new Object[]{
  c1.getReference(),c2.getReference() });
 compMgr.coaAddComp(c1, c2,this.getLoggedInUser(),getView());
} 

private void copyCostCentres(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "copyChartofAccount called with comp {0} Comp {1}", new Object[]{
  c1.getReference(),c2.getReference() });
 costCentMgr.copyToComp(compDest, compDest, this.getLoggedInUser(), this.getView());
}

private void copyGenLedAcnts(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "copyGenLedAcnts called with comp {0} and {1}", new Object[]{
  c1.getReference(), c2.getReference() });
 int rc = this.glAcntMgr.copyGenLedAcntsComp(c1, c2, this.getLoggedInUser(), getView());
 if(rc == GlAccountManager.PROCESS_OK){
  LOGGER.log(INFO, "GL accounts copied okay");
  MessageUtil.addClientInfoMessage("compCpySett:grl", "glAccountCpy", "blacResponse");
 }else if(rc == GlAccountManager.DEST_COMP_HAS_ACNT){
  LOGGER.log(INFO, "cannot copy GL accounts to a company with accounts set up");
  MessageUtil.addClientWarnMessage("compCpySett:grl", "glAcntCpyDup", "errorText");
 }else if(rc == GlAccountManager.DEST_COMP_HAS_ACNT){
  LOGGER.log(INFO, "Source Company has no GL accounts");
  MessageUtil.addClientWarnMessage("compCpySett:grl", "glAcntCpySrcE", "errorText");
 }
 PrimeFaces.current().ajax().update("compCpySett:grl");
}

private void copyFiscPeriodRule(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "copyGenLedAcnts called with comp {0} and {1}", new Object[]{
  c1.getReference(), c2.getReference() });
 int rc = this.compMgr.fisPeriodRuleCopyComp(c1, c2, getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.PER_RULE_UPDATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "compFiscPerUpdt", "blacResponse");
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:grl", "compPerRuleInDest", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.PER_RULE_UPDATE_FAIL:
   MessageUtil.addClientWarnMessage("compCpySett:grl", "compPerRuleCpy", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:grl");
  break;
 }
 }

private void copyFund(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "Called copyFund comp {0} and {1}", 
         new Object[]{c1.getReference(),c2.getReference()});
 int rc = this.compMgr.fundCopyComp(c1, c2, this.getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "restrFundCpyComp", "blacResponse");
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "restFundInDest", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "restFundSrcNone", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.PER_RULE_UPDATE_FAIL:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "restrFndCpy", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
  break;
 }
}
 
private void copyPaymentType(CompanyBasicRec c1, CompanyBasicRec c2){
 LOGGER.log(INFO, "Called copyPaymentType comp {0} and {1}", 
         new Object[]{c1.getReference(),c2.getReference()});
 int rc = compMgr.payTypeCopyComp(c1, c2, getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "compPayTyCpy", "blacResponse");
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "compPayTyInDest", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "compPayTyNotSrc", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_CREATED:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "compPayTyCpy", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
 }
}

public void copyProg(CompanyBasicRec c1,CompanyBasicRec c2){
 LOGGER.log(INFO, "CompCopySett.copyProg {0}", new Object[]{c1.getReference(),c2.getReference()});
 
 int rc = this.progMgr.copyProgrammesToComp(c1, c2, getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "maProgCpyComp", "blacResponse",c2.getReference());
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "maProgInDest", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "maProgSrcNone", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_CREATED:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "maProgrammeCopy", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
 }
 
}

public void copySalesPart(CompanyBasicRec c1,CompanyBasicRec c2){
 LOGGER.log(INFO, "CompCopySett.copySalesPart {0}", new Object[]{c1.getReference(),c2.getReference()});
 int rc = this.salesMgr.copySalesPartCompByComp(c1, c2, this.getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "slPartCpyComp", "blacResponse",c2.getReference());
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slPartInDestComp", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slPartSrcCompNone", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_CREATED:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slPartCpyComp", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
 }
}

public void copySalesCat(CompanyBasicRec c1,CompanyBasicRec c2){
 LOGGER.log(INFO, "CompCopySett.copySalesCat {0}", new Object[]{c1.getReference(),c2.getReference()});
 int rc = this.salesMgr.copySalesCatByComp(c1, c2, this.getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "slCatCpyComp", "blacResponse",c2.getReference());
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slCatInDestComp", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slCatSrcCompNone", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_CREATED:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "slCatCpyComp", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
 }
}

public void copyVatCode(CompanyBasicRec c1,CompanyBasicRec c2){
 LOGGER.log(INFO, "CompCopySett.copyVatCode {0}", new Object[]{c1.getReference(),c2.getReference()});
 int rc = vatMgr.copyVatCodeComp(c1, c2, this.getLoggedInUser(), getView());
 switch(rc){
  case CompanyManager.ATTR_CREATED:
   MessageUtil.addClientInfoMessage("compCpySett:grl", "vatCodeCompCpy", "blacResponse",c2.getReference());
   PrimeFaces.current().ajax().update("compCpySett:grl");
   break;
  case CompanyManager.ATTR_IN_DEST_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "vatCdCompInDest", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_IN_SRC_COMP:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "vatCdCompSrcCompNone", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
  case CompanyManager.ATTR_NOT_CREATED:
   MessageUtil.addClientWarnMessage("compCpySett:msgs", "vatCdCompCpy", "errorText");
   PrimeFaces.current().ajax().update("compCpySett:msgs");
   break;
 }
}
 /**
  * Creates a new instance of CompanyCopySettings
  */
 public CompanyCopySettings() {
 }

 public CompanyBasicRec getCompDest() {
  return compDest;
 }

 public void setCompDest(CompanyBasicRec compDest) {
  this.compDest = compDest;
 }

 public CompanyBasicRec getCompSource() {
  return compSource;
 }

 public void setCompSource(CompanyBasicRec compSource) {
  this.compSource = compSource;
 }
 
 

 public List<PickListItem> getSourceList() {
  return sourceList;
 }

 public void setSourceList(List<PickListItem> sourceList) {
  this.sourceList = sourceList;
 }

 public List<PickListItem> getSelectedList() {
  return selectedList;
 }

 public void setSelectedList(List<PickListItem> selectedList) {
  this.selectedList = selectedList;
 }

 public DualListModel<PickListItem> getCopySet() {
  return copySet;
 }

 public void setCopySet(DualListModel<PickListItem> copySet) {
  this.copySet = copySet;
 }
 
 


 
 
 
 private void processSetting(PickListItem item){
  LOGGER.log(INFO, "processSetting called with item descr {0}", item.getDescription());
  List<String> processedCodes = new ArrayList<>();
  String descr;
  switch(item.getCode()){
   
   case "compSettCoA":
    copyChartofAccount(compSource, compDest);
    descr = formText.getString("compSettCoA");
    processedCodes.add(descr);
    break;
   case "compSettCC":
    copyCostCentres(compSource, compDest);
    descr = formText.getString("compSettCC");
    processedCodes.add(descr);
    break;
   case "compSettGL":
    this.copyGenLedAcnts(compSource, compDest);
    descr = formText.getString("compSettGL");
    processedCodes.add(descr);
    break;
   case "compSettFund":
    this.copyFund(compSource, compDest);
    descr = formText.getString("compSettFund");
    processedCodes.add(descr);
    break;
   case "compSettPayTy":
    this.copyPaymentType(compSource, compDest);
    descr = formText.getString("compSettPayTy");
    processedCodes.add(descr);
    break;
   case "compSettPerCntrl":
    this.copyFiscPeriodRule(compSource, compDest);
    descr = formText.getString("compSettPerCntrl");
    processedCodes.add(descr);
    break;
   case "compSettProg":
    this.copyProg(compSource, compDest);
    descr = formText.getString("compSettProg");
    processedCodes.add(descr);
    break;
   case "compSettSlPart":
    this.copySalesPart(compSource, compDest);
    descr = formText.getString("compSettProg");
    processedCodes.add(descr);
    break;
   case "compSettSlCat":
    this.copySalesCat(compSource, compDest);
    descr = formText.getString("compSettSlCat");
    processedCodes.add(descr);
    break;
   case "compSettVatCode":
    this.copyVatCode(compSource, compDest);
    descr = formText.getString("compSettVatCode");
    processedCodes.add(descr);
    break;
  }
  if(!processedCodes.isEmpty()){
   StringBuilder sb = new StringBuilder();
   for(Object curr:processedCodes){
    sb.append((String)curr).append(" , ");
    
   }
   MessageUtil.addClientInfoDetMessage("compCpySett:grlTrf", "pickListAddOpt", "formText", sb.toString());
  }else{
   MessageUtil.addClientInfoMessage("compCpySett:grlTrf", "compSettNonel", "formText");
  }
  
  PrimeFaces.current().ajax().update("compCpySett:grl");
 }
 
 
 
 public void onCopyExec(){
  LOGGER.log(INFO, "onCopyExec called Target list {0}",copySet.getTarget());
  if(copySet.getTarget().isEmpty()){
   MessageUtil.addClientWarnMessage("compCpySett:grl", "compCopySel", "validationText");
   PrimeFaces.current().ajax().update("compCpySett:grl");
   LOGGER.log(INFO, "Growl called");
   return;
  }
  
  for(PickListItem item:copySet.getTarget()){
   
   this.processSetting(item);
  }
  
 }
 
 public void onCompDestSel(SelectEvent evt){
  compDest = (CompanyBasicRec)evt.getObject();
  LOGGER.log(INFO, "compDest {0}", compDest.getReference());
 } 
 
 public void onCompSourceSel(SelectEvent evt){
  compSource = (CompanyBasicRec)evt.getObject();
  LOGGER.log(INFO, "compSource {0}", compSource.getReference());
 }
 
 public void onOptTransfer(TransferEvent event){
  LOGGER.log(INFO, "onOptTransfer called with {0}", event.getItems());
  
  StringBuilder sb = new StringBuilder();
  for(Object i:event.getItems()){
   LOGGER.log(INFO, "Item {0}", i);
   PickListItem curr = (PickListItem)i;
   sb.append(curr.getDescription()).append("<br /> ");
  }
  
  //StringUtils.removeEnd(items, ",");
  if(event.isAdd()){
  MessageUtil.addClientInfoDetMessage("compCpySett:grl", "pickListAddOpt", "formText", sb.toString());
  }else{
   MessageUtil.addClientInfoDetMessage("compCpySett:grl", "pickListRemOpt", "formText", sb.toString());
  }
  PrimeFaces.current().ajax().update("compCpySett:grlTrf");
 }
 
 
 
 
}
