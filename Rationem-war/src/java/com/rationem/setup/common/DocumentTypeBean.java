/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import javax.faces.component.UIInput;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class DocumentTypeBean extends BaseBean {
 private static final Logger logger = Logger.getLogger(DocumentTypeBean.class.getName());

 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BasicSetup setup;
 
 private DocTypeRec docType ;
 private List<DocTypeRec> docTypes;
 private List<DocTypeRec> docTypesFiltered;
 private DocTypeRec docTypeSelected;
 

 /**
  * Creates a new instance of DocumentTypeBean
  */
 public DocumentTypeBean() {
 }

 public DocTypeRec getDocType() {
  if(docType == null){
   docType = new DocTypeRec();
   DefaultLoadBean ld = new DefaultLoadBean();
  
  }
  return docType;
 }

 public void setDocType(DocTypeRec docType) {
  this.docType = docType;
 }

 public DocTypeRec getDocTypeSelected() {
  return docTypeSelected;
 }

 public void setDocTypeSelected(DocTypeRec docTypeSelected) {
  this.docTypeSelected = docTypeSelected;
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

 public List<DocTypeRec> getDocTypesFiltered() {
  return docTypesFiltered;
 }

 public void setDocTypesFiltered(List<DocTypeRec> docTypesFiltered) {
  this.docTypesFiltered = docTypesFiltered;
 }
 
 public void onDocTyCodeValidate(FacesContext c, UIComponent comp, Object val){
  logger.log(INFO, "onValDocTyCode called with val {0}", val);
  UIInput uiComp = (UIInput)comp;
  if(val == null){
   MessageUtil.addErrorMessage("docTyCode", "validationText");
   uiComp.setValid(false);
   return;
  }
  String input = (String)val;
  if(input.isEmpty()){
   MessageUtil.addErrorMessage("docTyCode", "validationText");
   uiComp.setValid(false);
   return;
  }
  boolean unique = sysBuff.docTypeCodeUnique((String)val);
  logger.log(INFO, "unique {0}", unique);
  uiComp.setValid(unique);
  if(!unique){
   MessageUtil.addErrorMessage("docTyCodeDup", "validationText");
  }
 }
 
 public void onDocTyNewSave(){
  logger.log(INFO, "onDocTyNewSave called");
  
  if(!docType.isApAllowed() && !docType.isArAllowed() && !docType.isGlAllowed() && !docType.isTrAllowed()){
   MessageUtil.addErrorMessage("docTyCodeNoPst", "validationText");
   return;
  }
  try{
  docType.setCreated(new Date());
  docType.setCreatedBy(this.getLoggedInUser());
  docType = this.sysBuff.updateDocType(docType, getView());
  docType = null;
  MessageUtil.addInfoMessage("docTyCr", "blacResponse");
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("docTypeCrFrm");
  }catch(Exception ex){
   logger.log(INFO, "Could not create doc type: {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("docTyCr", "errorText");
   
  }
  
 }
 
 public void onDocTypeUpdateSave(){
  logger.log(INFO, "onDocTypeUpdateSave called");
  if(!this.docTypeSelected.isApAllowed() && !docTypeSelected.isArAllowed() && !docTypeSelected.isGlAllowed() &&
          !docTypeSelected.isTrAllowed()){
   MessageUtil.addErrorMessage("docTyCodeNoPst", "validationText");
   return;
  }
  
  try{
   docTypeSelected.setChangedBy(this.getLoggedInUser());
   docTypeSelected.setChangedOn(new Date());
   docTypeSelected = this.sysBuff.updateDocType(docTypeSelected, getView());
   MessageUtil.addWarnMessageParam("docTyUpdt", "blacResponse",docTypeSelected.getCode());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("docsTbl");
   pf.executeScript("PF('editDocTyWv').hide()");
  }catch(Exception ex){
   logger.log(INFO, "Doc type update failed {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("docTyUpDt", "errorText");
   
  }
  
 }
 
 public void onShowEditDlg(){
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editDocTyFrm");
  pf.executeScript("PF('editDocTyWv').show()");
 }
 
}
