/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Chris
 */
public class SortOrderBean extends BaseBean {
 private static final Logger logger =
            Logger.getLogger(SortOrderBean.class.getName());
 
 private SortOrderRec sortOrder;
 private SortOrderRec sortOrderSelected;
 private List<SortOrderRec> sortOrders;
 
 @EJB
 private SysBuffer sysBuff;
 

 /**
  * Creates a new instance of SortOrderBean
  */
 public SortOrderBean() {
 }

 public SortOrderRec getSortOrder() {
  if(sortOrder == null){
   sortOrder = new SortOrderRec();
  }
  return sortOrder;
 }

 public void setSortOrder(SortOrderRec sortOrder) {
  this.sortOrder = sortOrder;
 }

 public List<SortOrderRec> getSortOrders() {
  if(sortOrders == null){
   sortOrders = this.sysBuff.getSortOrders();
  }
  return sortOrders;
 }

 public void setSortOrders(List<SortOrderRec> sortOrders) {
  this.sortOrders = sortOrders;
 }

 public SortOrderRec getSortOrderSelected() {
  return sortOrderSelected;
 }

 public void setSortOrderSelected(SortOrderRec sortOrderSelected) {
  this.sortOrderSelected = sortOrderSelected;
 }
 
 public void onEditDlg(){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editPanel");
  rCtx.execute("PF('editSortOrderWv').show()");
 }
 
 public void onSaveEditSortOrder(){
  RequestContext rCtx = RequestContext.getCurrentInstance();
  try{
   sortOrderSelected.setUpdatedBy(this.getLoggedInUser());
   sortOrderSelected.setUpdatedOn(new Date());
   sortOrderSelected = sysBuff.sortOrderUpdate(sortOrderSelected, getView());
   ListIterator<SortOrderRec> li = this.sortOrders.listIterator();
   boolean sortOrderFound = false;
   while(li.hasNext() && !sortOrderFound){
    SortOrderRec s = li.next();
    if(s.getId() == sortOrderSelected.getId()){
     li.set(s);
     sortOrderFound = true;
    }
   }
  MessageUtil.addInfoMessage("sortOrderUpdt", "blacResponse");
  
  rCtx.update("sortTblId");
  rCtx.execute("PF('editSortOrderWv').hide()");
  }catch(Exception ex){
   logger.log(INFO, "Could not save sort order {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("sortOrderUpdt", "errorText");
  }
 }
 public void onSaveNewSortOrder(){
  logger.log(INFO, "Called onSaveNewSortOrder");
  RequestContext rCtx = RequestContext.getCurrentInstance();
  try{
   sortOrder.setCreatedBy(this.getLoggedInUser());
   sortOrder.setCreatedOn(new Date());
  sortOrder = sysBuff.sortOrderUpdate(sortOrder, getView());
  MessageUtil.addInfoMessage("sortOrderCr", "blacResponse");
  sortOrder = null;
  rCtx.update("sortOrderFrm");
  }catch(Exception ex){
   logger.log(INFO, "Could not save sort order {0}", ex.getLocalizedMessage());
   MessageUtil.addErrorMessage("sortOrderCr", "errorText");
  }
 }
 
}
