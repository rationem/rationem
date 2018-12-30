/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.FundCategoryRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Chris
 */
public class FundCategoryBean extends BaseBean {
 
 @EJB
 private BasicSetup setUp;
 private FundCategoryRec fndCat;
 private FundCategoryRec fndCatSelected;
 private List<FundCategoryRec> fndCategories;
 

 /**
  * Creates a new instance of FundCategoryBean
  */
 public FundCategoryBean() {
 }

 public FundCategoryRec getFndCat() {
  if(fndCat == null){
   fndCat = new FundCategoryRec();
  }
  return fndCat;
 }

 public void setFndCat(FundCategoryRec fndCat) {
  this.fndCat = fndCat;
 }

 public FundCategoryRec getFndCatSelected() {
  return fndCatSelected;
 }

 public void setFndCatSelected(FundCategoryRec fndCatSelected) {
  this.fndCatSelected = fndCatSelected;
 }

 public List<FundCategoryRec> getFndCategories() {
  if(fndCategories == null){
   fndCategories = this.setUp.getFundCategoriesAll();
  }
  return fndCategories;
 }

 public void setFndCategories(List<FundCategoryRec> fndCategories) {
  this.fndCategories = fndCategories;
 }
 
 public void onEditMenu(){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editCatFrm");
  rCtx.execute("PF('editCatDlg').show()");
 }
 
 public void onSaveEditCategory(){
  try{
   fndCatSelected.setChangedBy(this.getLoggedInUser());
   fndCatSelected.setChangedOn(new Date());
   fndCatSelected = setUp.updateFundCategory(fndCatSelected, getView());
   boolean foundCat = false;
   ListIterator<FundCategoryRec> li = this.fndCategories.listIterator();
   while(li.hasNext() && !foundCat){
    FundCategoryRec rec = li.next();
    if(rec.getId() == fndCatSelected.getId()){
     li.set(rec);
    }
   }
   MessageUtil.addInfoMessage("fndCatUpdt", "blacResponse");
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("catlistFrm");
   rCtx.execute("PF('editCatDlg').hide()");
  }catch(Exception ex){
   
  }
 }
 public void onSaveNewCategory(){
  try{
  fndCat.setCreatedBy(this.getLoggedInUser());
  fndCat.setCreatedOn(new Date());
  fndCat = setUp.updateFundCategory(fndCat, getView());
  MessageUtil.addInfoMessage("fndCatCr", "blacResponse");
  fndCat = null;
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("fndCatCrFrm");
  }catch(Exception ex){
   MessageUtil.addErrorMessage("fndCatCr", "errorText");
  }
 }
 
 
}
