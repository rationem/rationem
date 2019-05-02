/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr;

import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.ejbBean.tr.PaymentMediumManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.logging.Logger;
import org.primefaces.event.SelectEvent;


import javax.annotation.PostConstruct;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
public class ChequeTemplateBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(ChequeTemplateBean.class.getName());

 @EJB
 private PaymentMediumManager payMediumMgr;
 private ChequeTemplateRec templ;
 private ChequeTemplateRec templateSelected;
 private List<ChequeTemplateRec> templates;
 /**
  * Creates a new instance of ChequeTemplate
  */
 public ChequeTemplateBean() {
 }

 @PostConstruct
 private void init(){
  templ = new ChequeTemplateRec();
  if(StringUtils.equals(this.getViewSimple(), "chequeTemplateUpdate")){
   templates = this.payMediumMgr.getChqTemplatesAll();
   
  }
 }
 public ChequeTemplateRec getTempl() {
  return templ;
 }

 public void setTempl(ChequeTemplateRec templ) {
  this.templ = templ;
 }

 public List<ChequeTemplateRec> getTemplates() {
  return templates;
 }

 public void setTemplates(List<ChequeTemplateRec> templates) {
  this.templates = templates;
 }

 public ChequeTemplateRec getTemplateSelected() {
  return templateSelected;
 }

 public void setTemplateSelected(ChequeTemplateRec templateSelected) {
  this.templateSelected = templateSelected;
 }
 
 
 public String onDeleteTemplate(){
  LOGGER.log(INFO, "onDeleteTemplate called");
  templateSelected.setChangedBy(this.getLoggedInUser());
  templateSelected.setChangedOn(new Date());
  LOGGER.log(INFO, "Payment method {0}", templateSelected.getId());
  boolean templInUse = this.payMediumMgr.isChqTemplateInUse(templateSelected);
  if(templInUse){
   MessageUtil.addWarnMessage("chqTemplInUse", "validationText");
   return null;
  }
  LOGGER.log(INFO, "currently in use {0}", templInUse);
  boolean deleted = false;
  deleted = this.payMediumMgr.deleteChequeTemplate(templateSelected, this.getView());
  if(deleted){
   MessageUtil.addInfoMessage("trChqTemplDel", "blacResponse");
   
   PrimeFaces.current().executeScript("PF('templDelDlg').hide();");
   PrimeFaces.current().ajax().update("chqTemplUptFrm");
   templateSelected = null;
   return "chqTemplUpdt";
  }
  else{
   MessageUtil.addWarnMessage("chqTemplNoDel", "errorText");
   return null;
  }
 }
 
 public void onReset(){
  templ = new ChequeTemplateRec() ;
  PrimeFaces.current().ajax().update("chqTemplFrm:chqTemplPnlG");
 }
 public void onSaveTemplate(){
  LOGGER.log(INFO, "ViewSimple {0}", getViewSimple());
  if(StringUtils.equals(getViewSimple(), "chequeTemplateCreate")){
   templ.setCreatedBy(getLoggedInUser());
   templ.setCreatedOn(new Date());
   LOGGER.log(INFO, "Created by ID {0}", templ.getCreatedBy().getId());
  } else {
   templ.setChangedBy(getLoggedInUser());
   templ.setChangedOn(new Date());
  }
  
  templ = payMediumMgr.updateChequeTemplate(templ, null, null, getView());
  if(templ.getId() == null){
   MessageUtil.addErrorMessage("chqTemplNoSave", "errorText");
   PrimeFaces.current().ajax().update("msg");
  }else{
   MessageUtil.addInfoMessage("trChqTemplSaved", "formText");
   templ = new ChequeTemplateRec();
   PrimeFaces.current().ajax().update("chqTemplFrm:chqTemplPnlG");
  }
  
 }
 
 public void onTemplateRowSelect(SelectEvent evt){
  LOGGER.log(INFO,"onTemplateRowSelect called ");
  this.templateSelected = (ChequeTemplateRec)evt.getObject();
  LOGGER.log(INFO, "templateSelected {0}", templateSelected);
  
 }
 
 public void onTemplateTransfer(){
  LOGGER.log(INFO, "onTemplateTransfer called");
  boolean found = false;
  ListIterator<ChequeTemplateRec> li = templates.listIterator();
  while(li.hasNext()){
   ChequeTemplateRec templRec = li.next();
   if(Objects.equals(templRec.getId(), templateSelected.getId())){
    templateSelected.setChangedBy(this.getLoggedInUser());
    templateSelected.setChangedOn(new Date());
    templateSelected = this.payMediumMgr.updateChequeTemplate(templateSelected, null, null, getView());
    li.set(templateSelected);
    found = true;
   }
  }
  if(found){
   PrimeFaces pf = PrimeFaces.current();
   
   pf.ajax().update("chqTemplUptFrm:templateList");
   pf.executeScript("PF('editDlgWv').hide();");
   pf.resetInputs("chqTemplUptFrm:editDlg");
   
  }
 }
 
 public void onUploadEditBaseFile(FileUploadEvent evt){
  UploadedFile f = evt.getFile();
  byte[] fileData = f.getContents();
  templateSelected.setOriginalData(fileData);
  templateSelected.setOriginalFileExt(f.getContentType());
  templateSelected.setOriginalFileName(f.getFileName());
  LOGGER.log(INFO, " File name {0} file ext {1}", new Object[]{templateSelected.getOriginalFileName(),templateSelected.getOriginalFileExt()});
 }
 
 public void onUploadEditPdfFormFile(FileUploadEvent evt){
  UploadedFile f = evt.getFile();
  byte[] fileData = f.getContents();
  templateSelected.setPdfData(fileData);
  templateSelected.setPdfFileExt(f.getContentType());
  templateSelected.setPdfFileName(f.getFileName());
  LOGGER.log(INFO, "pdf File name {0} file ext {1}", new Object[]{templateSelected.getPdfFileName(),templateSelected.getPdfFileExt()});
 }
 
 
 public StreamedContent getBaseFile(){
  LOGGER.log(INFO, "templateSelected {0}", templateSelected);
  LOGGER.log(INFO, "templateSelected.getOriginalData() {0}", templateSelected.getOriginalData());
  if(templateSelected.getOriginalData() == null){
   templateSelected = payMediumMgr.getChqTemplOrigFileData(templateSelected);
  }
  ByteArrayInputStream bais = new ByteArrayInputStream(templateSelected.getOriginalData());
  StreamedContent f = new DefaultStreamedContent(bais,templateSelected.getOriginalFileExt(),templateSelected.getOriginalFileName());
  return f;
 }
 
 public StreamedContent getPdfFile(){
  
  if(templateSelected.getPdfData() == null){
   templateSelected = payMediumMgr.getChqTemplPdfFileData(templateSelected);
  }
  ByteArrayInputStream bais = new ByteArrayInputStream(templateSelected.getPdfData());
  StreamedContent f = new DefaultStreamedContent(bais,templateSelected.getPdfFileExt(),
    templateSelected.getPdfFileName());
  return f;
 }
 
 public void onUploadBaseFile(FileUploadEvent evt){
  
  UploadedFile f = evt.getFile();
  byte[] fileData = f.getContents();
  templ.setOriginalData(fileData);
  templ.setOriginalFileExt(f.getContentType());
  templ.setOriginalFileName(f.getFileName());
  LOGGER.log(INFO, "File name {0} file ext {1}", new Object[]{templ.getOriginalFileName(),templ.getOriginalFileExt()});
  
 }
 
 public void onUploadPdfFormFile(FileUploadEvent evt){
  UploadedFile f = evt.getFile();
  byte[] fileData = f.getContents();
  templ.setPdfData(fileData);
  templ.setPdfFileExt(f.getContentType());
  templ.setPdfFileName(f.getFileName());
  LOGGER.log(INFO, "File name {0} ext {1}", new Object[]{ templ.getPdfFileName(), templ.getPdfFileExt()});
 }
 
 
}
