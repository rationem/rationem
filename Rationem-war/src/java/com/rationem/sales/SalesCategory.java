/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.sales;

import com.rationem.util.BaseBean;
import com.rationem.busRec.sales.SalesCatRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.fi.SalesManager;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Chris
 */
public class SalesCategory extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger(SalesCategory.class.getName());
 
 private SalesCatRec cat;
 private SalesCatRec selectedCat;
 private DualListModel<SalesCatRec> subCategories;
 
 private List<SalesCatRec> categories;
 private List<SalesCatRec> unAllocatedCategories;
 private List<SalesCatRec> assignedSubCategories;
  
 @EJB
 private SalesManager salesMgr;
 
 @EJB 
 private SysBuffer sysBuff;

 /**
  * Creates a new instance of salesCategory
  */
 public SalesCategory() {
 }

 @PostConstruct
 private void init(){
  String currPage = this.getViewSimple();
  if(currPage.equals("salesCategoryUpdate")){
   this.getCategories();
   if(categories != null){
    cat = categories.get(0);
    cat = this.salesMgr.getSubCats(cat);
   }
   
  }
 }

 public List<SalesCatRec> getAssignedSubCategoriesgetAssignedSubCategories() {
  return assignedSubCategories;
 }

 public void setAssignedSubCategoriessetAssignedSubCategories(List<SalesCatRec> assignedSubCategoriesassignedSubCategories) {
  this.assignedSubCategories = assignedSubCategoriesassignedSubCategories;
 }
 
 
 public SalesCatRec getCat() {
  if(cat == null){
   cat = new SalesCatRec(); 
  }
  return cat;
 }

 public void setCat(SalesCatRec cat) {
  this.cat = cat;
 }

 public SalesCatRec getSelectedCat() {
  if(selectedCat == null){
   selectedCat = new SalesCatRec();
  }
  //salesCategoryUpdate
  LOGGER.log(INFO, "getSelectedCat called {0}",selectedCat);
  LOGGER.log(INFO, "selectedCat sub cats {0}",selectedCat.getSubCategories());
  LOGGER.log(INFO, "viewSimple {0}", this.getViewSimple());
  if(selectedCat.getSubCategories() == null || selectedCat.getSubCategories().isEmpty() ){
   selectedCat = this.salesMgr.getSubCats(selectedCat);
  }
  return selectedCat;
 }

 public void setSelectedCat(SalesCatRec selectedCat) {
  LOGGER.log(INFO, "setSelectedCat called with {0}", selectedCat);
  this.selectedCat = selectedCat;
 }



 public List<SalesCatRec> getCategories() {
  if(categories == null){
   categories = this.salesMgr.getAllSalesCategories();
  }
  return categories;
 }

 public void setCategories(List<SalesCatRec> categories) {
  this.categories = categories;
 }

 public DualListModel<SalesCatRec> getSubCategories() {
  return subCategories;
 }

 public void setSubCategories(DualListModel<SalesCatRec> subCategories) {
  this.subCategories = subCategories;
 }

 
 public List<SalesCatRec> getUnAllocatedCategories() {
  if(unAllocatedCategories == null){
   unAllocatedCategories = new ArrayList<>();
   if(categories == null){
    getCategories();
   }
   if(categories == null){
    return unAllocatedCategories;
   }else{
    ListIterator<SalesCatRec> li = categories.listIterator();
    while(li.hasNext()){
     SalesCatRec currCat = li.next();
     if(cat.getSalesCatParent() == null){
      unAllocatedCategories.add(currCat);
     }
    }
   }
  }
  return unAllocatedCategories;
 }

 public void setUnAllocatedCategories(List<SalesCatRec> unAllocatedCategories) {
  this.unAllocatedCategories = unAllocatedCategories;
 }

 
 
 public void onAddSalesCategory(){
  LOGGER.log(INFO, "onAddSalesCategory called sales cat {0}",cat);
  try{
   cat.setCreatedBy(this.getLoggedInUser());
   cat.setCreatedOn(new Date());
   cat = salesMgr.addSalesCategory(cat,getViewSimple());
   MessageUtil.addInfoMessageVar1("slCatAdded", "blacResponse", cat.getCode());
   RequestContext rCtx = RequestContext.getCurrentInstance();
   cat = null;
   rCtx.update("salesCatCrFrm");
  }catch(BacException ex){
   MessageUtil.addErrorMessage("slCatAdd", "errorText");
  }
 }
 
 public void onEditAddSubcat(){
  
  if (categories != null){
   
    unAllocatedCategories = new ArrayList<>();
   
   for(SalesCatRec currCat:categories){
    if(!Objects.equals(currCat.getId(), cat.getId())){
     unAllocatedCategories.add(currCat);
    }
   }
  }
  if(unAllocatedCategories == null || unAllocatedCategories.isEmpty()){
   MessageUtil.addWarnMessage("slCatAvail", "errorText");
  }else{
   if(assignedSubCategories == null){
    assignedSubCategories = new ArrayList<>();
   }
   assignedSubCategories = cat.getSubCategories();
   this.subCategories = new DualListModel<>(unAllocatedCategories, assignedSubCategories);
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("assignSubCatFrm");
   rCtx.execute("PF('assignSubCatDlgWv').show();");
  }
  
 }
 
 public void onEditCatSubCatAssignTrf(TransferEvent evt){
  LOGGER.log(INFO, "onEditCatSubCatAssignTrf called");
  LOGGER.log(INFO, "Trf list {0} added {1} revoved {2}", 
    new Object[]{evt.getItems(), evt.isAdd(), evt.isRemove()});
  LOGGER.log(INFO, "assognedSubCategories {0}", assignedSubCategories);
  assignedSubCategories = cat.getSubCategories();
  
  for(Object o:evt.getItems()){
   //loop of current target list
   SalesCatRec currCat = (SalesCatRec)o;
   if(evt.isAdd()){
    assignedSubCategories.add(currCat);
   }else {
    boolean found = false;
    ListIterator<SalesCatRec> li = assignedSubCategories.listIterator();
    while(li.hasNext() && !found){
     SalesCatRec t = li.next();
     if(Objects.equals(t.getId(), currCat.getId())){
      li.remove();
      found = true;
     }
    }
   }
   
   
  
  }
  cat.setSubCategories(assignedSubCategories);
  RequestContext.getCurrentInstance().update("subCatTbl");
  
 }
 
 public void onAddSubCategory(ValueChangeEvent evt){
  LOGGER.log(INFO, "onAddSubCategory called with category {0} ",((SalesCatRec)evt.getNewValue()).getCode());
  SalesCatRec subCat =  (SalesCatRec)evt.getNewValue();
  List<SalesCatRec>subCategoryList = cat.getSubCategories(); 
  if(subCategoryList == null){
   subCategoryList = new ArrayList<>();
  }
  subCat.setSalesCatParent(cat);
  subCategoryList.add((SalesCatRec)evt.getNewValue());
  cat.setSubCategories(subCategoryList);
  LOGGER.log(INFO, "Number of sub-categories {0}", cat.getSubCategories().size());
 }
 

 
 public List<SalesCatRec> onSubCatComplete(String input){
  
  if(StringUtils.isBlank(input)){
   return sysBuff.getSalesCategories();
  }else{
   List<SalesCatRec> retList = new ArrayList<>();
   for(SalesCatRec currCat:sysBuff.getSalesCategories()){
    if(StringUtils.startsWithIgnoreCase(currCat.getCode(), input)){
     retList.add(currCat);
    }
   }
   return retList;
  }
 
 }
 
 public void onCatCtxMnu(SelectEvent evt){
  selectedCat = (SalesCatRec)evt.getObject();
  LOGGER.log(INFO, "onCatCtxMnu selected {0}", selectedCat);
 }
 
 public void onSubCatRemove(){
  
  ListIterator<SalesCatRec> li = cat.getSubCategories().listIterator();
  boolean foundCat = false;
  while(li.hasNext() && !foundCat){
   SalesCatRec curr = li.next();
   if(Objects.equals(curr.getId(), selectedCat.getId())){
    li.remove();
    foundCat = true;
   }
  }
  
  if(foundCat){
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("subCatTbl");
   rCtx.execute("PF('remSubCatWv').hide();");
  }else{
   MessageUtil.addWarnMessage("slCatRem", "errorText");
  }
 }
 
 public void onUpdateSave(){
  LOGGER.log(INFO, "onUpdateSave called ");
  cat.setChangedBy(this.getLoggedInUser());
  cat.setChangedOn(new Date());
  try{
   cat = salesMgr.updateSalesCat(cat,this.getViewSimple());
   MessageUtil.addInfoMessage("slCatUpdt", "blacResponse");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("slCatUpdt", "errorText");
  }
 }
         
}
