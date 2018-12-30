/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.sales;

import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.ejbBean.fi.SalesManager;
import com.rationem.helper.SelectOptSalesDayBook;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabEvent;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
public class SalesDaybook extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger(SalesDaybook.class.getName());


 @EJB
 private SalesManager salesMgr;
 private double totalValue = 120.0;
 
 private SelectOptSalesDayBook opts;
 private List<DocInvoiceArRec> invList;
 
 /**
  * Creates a new instance of SalesDaybook
  */
 public SalesDaybook() {
 }
 
 @PostConstruct
 private void init(){
  opts = new SelectOptSalesDayBook();
  if(getCompList() != null){
   opts.setComp(getCompList().get(0));
  }
  
 }

 public List<DocInvoiceArRec> getInvList() {
  return invList;
 }

 public void setInvList(List<DocInvoiceArRec> invList) {
  this.invList = invList;
 }

 public SelectOptSalesDayBook getOpts() {
  return opts;
 }

 public void setOpts(SelectOptSalesDayBook opts) {
  this.opts = opts;
 }

 public double getTotalValue() {
  return totalValue;
 }

 public void setTotalValue(double totalValue) {
  this.totalValue = totalValue;
 }
 
 
 public void onTabChange(TabChangeEvent evt){
  LOGGER.log(INFO, "onTabChange tab {0}", evt.getTab().getId());
  
  
 }
 
 public boolean selectValid(TabEvent evt){
  Tab currTab = evt.getTab();
  boolean retVal ;
  LOGGER.log(INFO, "selectValid tab {0}", currTab.getId());
  if (currTab.getId().equals("sel")){
   retVal =  true;
  }else {
   
   if(opts.getPerTo() < opts.getPerFrom()){
    String field = this.formTextForKey("docPeriod");
    MessageUtil.addWarnMessageParam("fromGrTo", "validationText", field);
    LOGGER.log(INFO, "to return false getPerTo() {0} getPerFrom(){1} ", new Object[]{opts.getPerTo(),opts.getPerFrom()});
   retVal = false;
  }else if (opts.getYrTo() < opts.getYrFrom()){
   String field = this.formTextForKey("docFisYr");
   MessageUtil.addWarnMessageParam("fromGrTo", "validationText", field);
   retVal = false;
  }else if( (opts.getDocDateFrom() != null && opts.getDocDateTo() != null ) 
    && opts.getDocDateTo().before(opts.getDocDateFrom())){
   LOGGER.log(INFO, "doc date error ");
   String field = this.formTextForKey("docDocDate");
   MessageUtil.addWarnMessageParam("fromGrTo", "validationText", field);
   retVal = false;
  }else if((opts.getPostDateFrom() != null && opts.getPostDateTo() != null ) 
    && opts.getPostDateTo().before(opts.getPostDateFrom())){
   String field = this.formTextForKey("docPostDate");
   MessageUtil.addWarnMessageParam("fromGrTo", "validationText", field);
   retVal = false;
  }else if((opts.getEntryDateFrom() != null && opts.getEntryDateTo() != null ) 
    && opts.getEntryDateTo().before(opts.getEntryDateFrom())){
   String field = this.formTextForKey("docEntryDate");
   MessageUtil.addWarnMessageParam("fromGrTo", "validationText", field);
   retVal = false;
   
  }else{
   retVal= true;
   // get invoices
   invList = salesMgr.getSalesDayBook(opts);
   totalValue = 0.0;
   

   ListIterator<DocInvoiceArRec> li = invList.listIterator();
   while(li.hasNext()){
    DocInvoiceArRec i = li.next();
    if(!i.isDebit()){
      double total = i.getTotalAmount() * -1;
      i.setTotalAmount(total);
      li.set(i);
     }
    totalValue += i.getTotalAmount();
   }
     
     
    
   
   RequestContext.getCurrentInstance().update("salesAP:dayBookList");
   
  }
 }
  LOGGER.log(INFO, "retVal {0}", retVal);
  return retVal;
 }

}