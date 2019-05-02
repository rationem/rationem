/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.mdm.CountryRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import java.util.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import static java.util.logging.Level.INFO;
import javax.faces.event.ValueChangeEvent;



/**
 *
 * @author Chris
 */
public class CountryBean extends BaseBean {

 private static final Logger logger = Logger.getLogger(CountryBean.class.getName());
 @EJB
 private MasterDataManager masterData;
 private CountryRec country;
 private CountryRec countrySelected;
 private List<CountryRec> countries;
 private List<CountryRec> countriesFiltered;
 /**
  * Creates a new instance of CountryBean
  */
 public CountryBean() {
 }

 @PostConstruct
 private void init(){
  countries = this.masterData.getCountriesAll();
 }
 
 public CountryRec getCountry() {
  
  return country;
 }

 public void setCountry(CountryRec country) {
  this.country = country;
 }

 public CountryRec getCountrySelected() {
  return countrySelected;
 }

 public void setCountrySelected(CountryRec countrySelected) {
  this.countrySelected = countrySelected;
 }

 
 public List<CountryRec> getCountries() {
  return countries;
 }

 public void setCountries(List<CountryRec> countries) {
  this.countries = countries;
 }

 public List<CountryRec> getCountriesFiltered() {
  return countriesFiltered;
 }

 public void setCountriesFiltered(List<CountryRec> countriesFiltered) {
  this.countriesFiltered = countriesFiltered;
 }
 
 public void onAddCountry(){
  country = new CountryRec();
  PrimeFaces pf = PrimeFaces.current();
  pf.executeScript("PF('addCntryDlgWv').show()");
  
 }
 
 public void onSaveCountry(){
  if(country == null){
   MessageUtil.addErrorMessage("countryNull", "errorText");
   return;
  }
  
   country.setCreatedBy(getLoggedInUser());
   country.setCreatedOn(new Date());
  
  country = masterData.countryUpdate(country, getLoggedInUser(), getView());
  if(countries == null){
   countries = new ArrayList<CountryRec>();
  }
  countries.add(country);
  PrimeFaces pf = PrimeFaces.current();
  country = new CountryRec();
  pf.ajax().update("countriesTbl");
  pf.ajax().update("newCntryFrm");
  pf.executeScript("PF('addCntryDlgWv').hide()");
 }
 
 public void onSaveEdit(){
  this.countrySelected.setChangedBy(getLoggedInUser());
  countrySelected.setChangedOn(new Date());
  try{
  countrySelected = masterData.countryUpdate(countrySelected, getLoggedInUser(), getView());
  MessageUtil.addInfoMessageVar1("cntryUpdate", "blacResponse", countrySelected.getCountryName());
  ListIterator<CountryRec> li = countries.listIterator();
  boolean foundCntry = false;
  while(li.hasNext() && !foundCntry){
   CountryRec cntry = li.next();
   if(cntry.getId() == countrySelected.getId()){
    li.set(countrySelected);
    foundCntry = true;
    PrimeFaces pf = PrimeFaces.current();
    countrySelected = null;
    pf.ajax().update("countriesTbl");
    pf.ajax().update("editCntryFrm");
    pf.executeScript("PF('editCntryDlgWv').hide()");
   }
  }
  }catch(Exception ex){
   MessageUtil.addErrorMessageParam1("countryUpdt", "errorTet", countrySelected.getCountryName());
   
  }
  
 }
 public void onCloseCountryDlg(){
  country = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("newCntryFrm");
  pf.resetInputs("newCntryFrm");
  pf.executeScript("PF('addCntryDlgWv').hide()");
 }
 
 public void onCloseEditDlg(){
  countrySelected = null;
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editCntryFrm");
  pf.executeScript("PF('editCntryDlgWv').hide()");
 }
 
 public void onCountryEdit(){
  logger.log(INFO, "selected {0}", this.countrySelected);
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("editCntryFrm");
  pf.executeScript("PF('editCntryDlgWv').show()");
  
 }
 public void onCountrySelect(SelectEvent evt){
  logger.log(INFO, "Selected country {0}", evt.getObject());
  this.countrySelected = (CountryRec)evt.getObject();
  logger.log(INFO, "Selected country {0}", evt.getObject());
  //PrimeFaces pf = PrimeFaces.current();
  //pf.ajax().update("editCntryFrm");
  //pf.executeScript("PF('editCntryDlgWv').show()");
  
 }
 
 public void onDepenNewChange(ValueChangeEvent evt){
  if((Boolean)evt.getNewValue()){
   country.getDependency();
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("cDep");
  }
 }
 public void onDepenEditChange(ValueChangeEvent evt){
  logger.log(INFO, "editcDep new {0} ", evt.getNewValue());
  if((Boolean)evt.getNewValue()){
   countrySelected.getDependency();
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editcDep");
  }
 }
}
