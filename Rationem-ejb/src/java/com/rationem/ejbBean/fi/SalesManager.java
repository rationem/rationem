/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.fi;

import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.sales.SalesCatRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.dataManager.DocumentDM;
import com.rationem.ejbBean.dataManager.SalesDM;
import com.rationem.exception.BacException;
import com.rationem.helper.SelectOptSalesDayBook;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class SalesManager {
 private static final Logger LOGGER =
            Logger.getLogger("com.rationem.ejbBean.fi.SalesManager");
 @EJB
 SalesDM sales;
 
 @EJB
 DocumentDM docMgr;
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public SalesCatRec addSalesCategory(SalesCatRec salesCat, String pg) throws BacException {
  LOGGER.log(INFO, "SalesManager.addSalesCategory called with {0}", salesCat);
  SalesCatRec cat = sales.addSalesCategory(salesCat,pg);
  LOGGER.log(INFO, "Id after DB layer returns {0}", cat.getId());
  return cat;
 }

 public List<SalesCatRec> getAllSalesCategories() throws BacException {
  LOGGER.log(INFO,"SalesManager.getAllSalesCategories called");
  List<SalesCatRec> categories = sales.allSalesCategories();
  LOGGER.log(INFO,"Sales categories returned by DB layer {0}",categories);
  return categories;
 }

 public SalesPartRec addSalesPart(SalesPartRec part, UserRec usr, String source) throws BacException {
  LOGGER.log(INFO,"SalesManager.addSalesPart called");
  part = sales.addSalesPart(part, usr, source);
  LOGGER.log(INFO,"addSalesPart part after DB layer returns {0}",part);
  return part;
 }
 
 public int copySalesCatByComp(CompanyBasicRec c1, CompanyBasicRec c2, 
         UserRec usrRec, String pg){
  LOGGER.log(INFO, "SalesMgr.copySalesCatByComp called with {0} and {1}", new Object[]{
   c1.getReference(), c2.getReference()});
  return sales.copySalesCatByComp(c1, c2, usrRec, pg);
 }
 
 public int copySalesPartCompByComp(CompanyBasicRec c1, CompanyBasicRec c2, 
         UserRec usrRec, String pg){
  LOGGER.log(INFO, "SalesMgr.copySalesPartCompByComp called with {0} and {1}", new Object[]{
   c1.getReference(), c2.getReference()});
  return this.sales.copySalesPartCompByComp(c1, c2, usrRec, pg);
 }

 public List<SalesPartRec> getAllSalesParts() throws BacException {
  LOGGER.log(INFO,"SalesManager.getAllSalesParts called");
  List<SalesPartRec> parts = this.sales.getAllSalesParts();
  LOGGER.log(INFO, "Sales parts returned by DB layer {0}", parts);
  return parts;
 }
 
 
 public SalesPartRec addCompSalesParts(SalesPartRec part){
  SalesPartRec retPart = this.sales.getCompPartsForPart(part);
  return retPart;
 }

 public List<SalesPartRec> getPartsByCodePartial(String partCdStarts) {
  LOGGER.log(INFO, "SalesMgr.getPartsByPartCodePartial called with {0}", partCdStarts);
  
  if(partCdStarts == null || partCdStarts.isEmpty()){
   partCdStarts = "%";
  }else if(!partCdStarts.endsWith("%")){
   partCdStarts = partCdStarts + "%";
  }
  List<SalesPartRec> retList = sales.getPartsByPartCodePartial(partCdStarts);
  return retList;
 }
 
 
 public List<DocInvoiceArRec> getSalesDayBook(SelectOptSalesDayBook opts){
   
  
  return docMgr.getSalesInvoices(opts);
 }
 
 public SalesCatRec getSubCats(SalesCatRec rec){
  LOGGER.log(INFO, "getSubCats called with cat id {0}", rec.getId());
  rec = this.sales.getSubCats(rec);
  return rec;
 }

 public String updateParts(List<SalesPartRec> updates, UserRec user, String page) throws BacException {
  LOGGER.log(INFO, "SalesMgr.updateParts called with parts {0} user {1} page {2}", new Object[]{
   updates, user, page });
  Date updateDt = new Date();
  int numUpdates = 0;
  int numNew =0;
  ListIterator<SalesPartRec> updatesLi = updates.listIterator();
  while(updatesLi.hasNext()){
   
   // set update user and time
   SalesPartRec updateRec = updatesLi.next();
   LOGGER.log(INFO, "Update for part code: {0}", updateRec.getPartCode());
   updateRec.setChangedBy(user);
   updateRec.setChangedOn(updateDt);
   List<SalesPartCompanyRec> compPartRecs = updateRec.getSalesPartCompanies();
   LOGGER.log(INFO, "Company parts {0}",compPartRecs );
   ListIterator<SalesPartCompanyRec> compPartRecsLi = compPartRecs.listIterator();
   while(compPartRecsLi.hasNext()){
    SalesPartCompanyRec partCompRec = compPartRecsLi.next();
     LOGGER.log(INFO, "Company part id {0}",partCompRec.getId());
    if(partCompRec.getId() < 1){
     // new Comp part
     try{
      LOGGER.log(INFO, "need to create compPart");
      partCompRec = sales.addCompPartToSalesPart(updateRec, partCompRec, user, updateDt, page);
      LOGGER.log(INFO, "need to create compPart");
      compPartRecsLi.set(partCompRec);
      numNew++;
    }catch(BacException ex){
     throw new BacException("Could not create part for rec"+updateRec.getPartCode());
    }
    }else{
     LOGGER.log(INFO, "Need to update comp part {0}", partCompRec);
     try{
      LOGGER.log(INFO, "updateSaleCompPart with comp Part {0}", partCompRec);
      partCompRec = sales.updateSaleCompPart(partCompRec, user, updateDt, page);
      LOGGER.log(INFO, "After updateSaleCompPart  comp Part {0}", partCompRec);
     compPartRecsLi.set(partCompRec);
     numUpdates++;
     }catch(BacException ex){
     throw new BacException("Could not create part for rec"+updateRec.getPartCode());
    }
   }
   try{
    LOGGER.log(INFO, "Call updateSalespart for part: {0}", updateRec);
    updateRec = sales.updateSalespart(updateRec,  page);
    updatesLi.set(updateRec);
    numUpdates++;
     }catch(BacException ex){
     throw new BacException("Could not create part for rec"+updateRec.getPartCode());
    }
  }
  
 }
  LOGGER.log(INFO, "End update parts numNew {0} numupdates {1}", new Object[]{numNew,numUpdates});
 String retStatus = numNew + " | "+ numUpdates; 
 return retStatus;
 }
 
 public SalesPartRec updateSalesPart(SalesPartRec pt, String pg){
  pt = this.sales.updateSalespart(pt, pg);
  return pt;
 }
 
 public SalesCatRec updateSalesCat(SalesCatRec cat, String pg){
  cat = sales.updateSalesCat(cat, pg);
  return cat;
 }
 
}
