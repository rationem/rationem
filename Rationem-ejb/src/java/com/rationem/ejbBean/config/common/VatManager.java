/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.common;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatReturnRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.VatDM;
import com.rationem.exception.BacException;
import com.rationem.util.GenUtilServer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
public class VatManager {
private static final Logger LOGGER =  Logger.getLogger(VatManager.class.getName());

@EJB
private VatDM vatDM;

@EJB
private SysBuffer sysBuff;



private Date determineDueDate(Date periodDate, String type){
 LOGGER.log(INFO, "determineDueDate called with ac date {0} type {0}", new Object[]{periodDate,type});
 // TODO: VAT config options in new DB table
 int paperMonths = 1;
 int onLineAddDays = 7;
 Date retDate = null;
 GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
 if(type.equalsIgnoreCase("dueDatePaper")){
  cal.setTime(periodDate);
  cal.add(Calendar.MONTH, paperMonths);
  retDate = GenUtilServer.getFirstWeekDayForDate(cal.getTime());
  
 }else if(type.equalsIgnoreCase("dueDateonLine")){
  cal.setTime(periodDate);
  cal.add(Calendar.MONTH, paperMonths);
  cal.add(Calendar.DATE, onLineAddDays);
  retDate = cal.getTime();
 }else if(type.equalsIgnoreCase("payDateonLine")){
  cal.setTime(periodDate);
  cal.add(Calendar.MONTH, paperMonths);
  cal.add(Calendar.DATE, onLineAddDays);
  retDate = GenUtilServer.getFirstWeekDayForDate(cal.getTime());;
 }
 
 return retDate;
} 
 
public int copyVatCodeComp(CompanyBasicRec c1, CompanyBasicRec c2, 
        UserRec usrRec, String pg){
 LOGGER.log(INFO, "VatMgr.copyVatCodeComp called with Comp {0} and {1}", new Object[]{
  c1.getReference(),c2.getReference() });
 return this.vatDM.copyVatCodeComp(c1, c2, usrRec, pg);
}
public VatRegistrationRec saveVatRegistration(CompanyBasicRec comp, VatRegistrationRec vatReg, 
         UserRec usr, String source) throws BacException {
  LOGGER.log(INFO, "CompanyMgr.saveVatRegistration called with company {1} reg {2}", 
          new Object[]{comp,vatReg});
  
  vatReg = vatDM.vatRegistrationUpdate(comp, vatReg, usr, source);
  LOGGER.log(INFO, "VAT manager returns vat reg with id {0}", vatReg.getId());
  return vatReg;
 }

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public VatReturnRec getVatReturn(VatRegSchemeRec regScheme, Date taxPoint, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "VatMgr.getVatReturn called with {0}", regScheme);
  VatReturnRec vatRet = null;
  VatSchemeRec vatScheme = regScheme.getVatScheme();
  LOGGER.log(INFO, "vatScheme {0}", vatScheme);
  char payFreq = vatScheme.getPaymentFrequency();
  if(regScheme.getVatReturns() == null){
   //no VAT returns
   vatRet = new VatReturnRec();
   
   vatRet.setVatRegScheme(regScheme);
   List<VatReturnRec> vatReturns = new ArrayList<VatReturnRec>();
   vatReturns.add(vatRet);
   regScheme.setVatReturns(vatReturns);
   vatRet.setCreatedBy(usr);
   vatRet.setCreatedOn(new Date());
   if(payFreq == 'M'){
    // monthly payments
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(taxPoint);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(GregorianCalendar.YEAR);
    StringBuilder sb = new StringBuilder();
    if(month < 10){
     sb.append('0');
    }
    sb.append(month);
    sb.append('/');
    sb.append(year);
    vatRet.setReturnRef(sb.toString());
   }else{
    // quarterly payments
   }
   
  }
  return vatRet;
 }

 public List<VatRegistrationRec> getVatRegistrationsForComp(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "VatMgr.getVatRegistrationsForComp called with comp {0}", comp);
  List<VatRegistrationRec> vatRegs = this.vatDM.getVatRegistrationsForCompany(comp);
  LOGGER.log(INFO, "vatRegs returned from DB layer {0}", vatRegs);
  return vatRegs;
 }

 public List<VatReturnRec> getVatReturnsForRegScheme(VatRegSchemeRec scheme, Date taxPoint,UserRec user, String page) 
 {
  LOGGER.log(INFO, "vatMgr.getVatReturnsForRegScheme called with scheme {0} and date {1}", new Object[]{
   scheme,taxPoint});
  List<VatReturnRec> vatRetList = vatDM.getVatReturnsForRegScheme(scheme, taxPoint);
  LOGGER.log(INFO, "Vatreturns from DB layer {0}", vatRetList);
  LOGGER.log(INFO, "Vatreturns from DB layer empty {0}", vatRetList.isEmpty());
  
  if(vatRetList.isEmpty()){
   // need to create vat freturn record
   LOGGER.log(INFO,"Need to create new Vat RETURN");
   vatRetList = new ArrayList<VatReturnRec>();
   char freq = scheme.getVatScheme().getPaymentFrequency();
   LOGGER.log(INFO, "scheme.getVatScheme() {0}", scheme.getVatScheme());
   LOGGER.log(INFO,"Vat sch freq {0}",freq);
   if(freq == 'M'){
    // monthly payment return
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(taxPoint);
    int dayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    LOGGER.log(INFO, "dayOfMonth {0}", dayOfMonth);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) ;
    
    LOGGER.log(INFO, "month {0}", month);
    GregorianCalendar retDateCal = new GregorianCalendar(year,month,dayOfMonth);
    
    Date retDate = retDateCal.getTime();
    LOGGER.log(INFO,"retDateCal {0} retDate {1} ", new Object[]{retDateCal,retDate});
    GregorianCalendar payDateCal = new GregorianCalendar(year,month,dayOfMonth);
    payDateCal.add(Calendar.DATE, 14);
    Date payDate = payDateCal.getTime();
    LOGGER.log(INFO, "retDateCal {0} year {1} month {2} dayOfMonth {3}" , 
            new Object[]{retDateCal,year,month,dayOfMonth});
    String retRef = month + 1 + "/" + year;
    VatReturnRec vatReturnRec = new VatReturnRec();
    vatReturnRec.setCreatedBy(user);
    vatReturnRec.setCreatedOn(new Date());
    vatReturnRec.setReturnDate(retDate);
    vatReturnRec.setPaymentDueDate(payDate );
    vatReturnRec.setVatRegScheme(scheme);
    vatReturnRec.setReturnRef(retRef);
    vatReturnRec = vatDM.addVatReturn(vatReturnRec, user, page);
    vatRetList.add(vatReturnRec);
    
    LOGGER.log(INFO, "new VatReturnRec id {0}", vatReturnRec.getId());
   }else{
    // quarterly payments
   }
   
   scheme.setVatReturns(vatRetList);
  }
  
  return vatRetList;
 }
 
 public List<VatSchemeRec> getVatSchemesAll(){
  List<VatSchemeRec> vatSchemes = this.vatDM.getVatSchemesAll();
  return vatSchemes;
 }
/**
 * Retrieve all input VAT codes
 * @return
 * @throws BacException 
 */
 public List<VatCodeRec> getInputVatCodes(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO,"VAT manager getInputVatCodes()");
  List<VatCodeRec> inputVatCodes = this.vatDM.getVatCodesInput(comp);
  return inputVatCodes;
 }
 
 public List<VatCodeRec> getOutputVatCodes(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO,"VAT manager getOutputVatCodes()");
  List<VatCodeRec> inputVatCodes = this.vatDM.getVatCodesOutput(comp);
  return inputVatCodes;
 }
 
public VatRegistrationRec vatRegSchemeUpdate(VatRegistrationRec vatRegRec, UserRec usr, String pg){
 vatRegRec = this.vatDM.vatRegSchemeUpdate(vatRegRec, usr, pg);
 return vatRegRec;
} 

}
