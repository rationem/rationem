/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.util.BaseBean;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
//import javax.enterprise.context.Dependent;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Chris
 */
public class PostCodeBean extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger("accounts.beans.setup.comp.PostCode");

 @EJB
 private CompanyManager compMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private PostTypeRec postType;
 private PostTypeRec postTypeEdit;
 private PostTypeRec postTypeNone;
 private List<PostTypeRec> postTypes;
 private List<LedgerRec> ledgers;
 private ModuleRec moduleNone;
 private List<ModuleRec> modules;
 
 /**
  * Creates a new instance of PostCodeBean
  */
 public PostCodeBean() {
 }
@PostConstruct 
 private void init(){
  postTypes = this.sysBuff.getPostTypes();
  if(postTypes != null && !postTypes.isEmpty()){
   postTypeEdit = postTypes.get(0);
  }
 }

 public List<LedgerRec> getLedgers() {
  if(ledgers == null){
   ledgers = sysBuff.getLedgers();
  }
  return ledgers;
 }

 public void setLedgers(List<LedgerRec> ledgers) {
  this.ledgers = ledgers;
 }

 public ModuleRec getModuleNone() {
  if(moduleNone == null){
   moduleNone = new ModuleRec();
   String name =this.formTextForKey("select");
   moduleNone.setName(name);
  }
  return moduleNone;
 }

 public void setModuleNone(ModuleRec moduleNone) {
  this.moduleNone = moduleNone;
 }

 
 public List<ModuleRec> getModules() {
  if(modules == null){
   modules = this.sysBuff.getModules();
  }
  return modules;
 }

 public void setModules(List<ModuleRec> modules) {
  this.modules = modules;
 }

 
 public PostTypeRec getPostType() {
  if(postType == null){
   postType = new PostTypeRec();
  }
  return postType;
 }

 public void setPostType(PostTypeRec postType) {
  this.postType = postType;
 }

 public PostTypeRec getPostTypeEdit() {
  return postTypeEdit;
 }

 public void setPostTypeEdit(PostTypeRec postTypeEdit) {
  this.postTypeEdit = postTypeEdit;
 }

 
 public PostTypeRec getPostTypeNone() {
  if(postTypeNone == null){
   postTypeNone = new PostTypeRec();
   postTypeNone.setDescription("select");
  }
  return postTypeNone;
 }

 public void setPostTypeNone(PostTypeRec postTypeNone) {
  this.postTypeNone = postTypeNone;
 }

 public List<PostTypeRec> getPostTypes() {
  if(postTypes == null){
   postTypes = this.sysBuff.getPostTypes();
  }
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }
 
 public void onChangePostTypeCode(ValueChangeEvent evt){
  this.postTypeEdit = (PostTypeRec)evt.getNewValue();
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("postTyUpdtFrm");
 }
 public void onCreatePostType(){
  LOGGER.log(INFO, "onCreatePostType called");
  try{
   postType.setCreatedBy(this.getLoggedInUser());
   postType.setCreateDate(new Date());
  // postType = compMgr.createPostType(postType, this.getLoggedInUser(), this.getView());
   LOGGER.log(INFO, "Comp Mgr returns postType with id: {0}", postType.getId());
   MessageUtil.addInfoMessage("postTyCr", "blacResponse");
   postType = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("postTyCrFrm");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("postTyCr", "errorText");
   
  }
  
 }
 
 public void onUpdatePostType(){
  LOGGER.log(INFO, "onUpdatePostType called");
  try{
   postTypeEdit.setChangedBy(this.getLoggedInUser());
   postTypeEdit.setChangedDate(new Date());
   postTypeEdit = compMgr.updatePostType(postTypeEdit, this.getLoggedInUser(), this.getView());
   LOGGER.log(INFO, "Comp Mgr returns postType with id: {0}", postTypeEdit.getId());
   ListIterator<PostTypeRec> li = postTypes.listIterator();
   boolean foundPstTy = false;
   while(li.hasNext() && !foundPstTy){
    PostTypeRec pstTy = li.next();
    if(pstTy.getId() == postTypeEdit.getId()){
     li.set(postTypeEdit);
    }
   }
   MessageUtil.addInfoMessage("postTyUpdt", "blacResponse");
   //postTypeEdit = null;
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("postTyUpdtFrm");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("postTyUpdt", "errorText");
   
  }
 }
 public void validatePostTypeCode(FacesContext context,
                UIComponent toValidate,
              Object value) {
  LOGGER.log(INFO, "validatePostTypeCode class {0}", value.getClass().getSimpleName());
  String entered = (String)value;
  boolean foundPostCode = false;
  for(PostTypeRec pstCd: this.postTypes){
   if(pstCd.getPostTypeCode().equalsIgnoreCase(entered)){
    foundPostCode = true;
   }
  }
  
  if(foundPostCode){
  ((UIInput) toValidate).setValid(false);
  
  
  MessageUtil.addErrorMessage("postTyCodeDupl", "validationText");
  }else{
   ((UIInput) toValidate).setValid(true);
  }
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("postCd");
  
  
  
 }
 
 
         
 
}
