/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;

import com.rationem.busRec.user.UserRec;
import com.rationem.common.UserBean;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


/**
 *  Holds session info
 * @author Chris / Last modified by $Author: $
 * @Version $Revision: 1.0 $
 */

public class UserSessionBean extends BaseBean implements Serializable {
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
 private static final Logger LOGGER = Logger.getLogger(UserSessionBean.class.getName());
            
 private UserRec usr;
 private Locale loc;
 private int slInvLinesInp = 8;
 private int mastDataRows = 10;
 private int tblRows = 10;
 private Map<String, String> themes;
 private String theme = "aristo";
 private Map<String,String> numberTexts;
 private int maxLoginAttempts = 4;
 private int autoComplScroll = 400;
 

 /** Creates a new instance of UserSessionBean */
 public UserSessionBean() {
 }

 @PostConstruct
 public void init(){
  theme = "cupertino";
  FacesContext fc = FacesContext.getCurrentInstance();
  loc = fc.getExternalContext().getRequestLocale();
  LOGGER.log(INFO, "user location {0}", loc.getDisplayCountry());
  themes = new TreeMap<>();
  themes.put("Aristo", "aristo");
  themes.put("Cupertino", "cupertino");
   
 }

 public int getAutoComplScroll() {
  return autoComplScroll;
 }

 public void setAutoComplScroll(int autoComplScroll) {
  this.autoComplScroll = autoComplScroll;
 }
 
 
 public Locale getLoc() {
  if(loc == null){
   loc =  new Locale("en", "GB");
   
   //FacesContext fc = FacesContext.getCurrentInstance();
   //loc = fc.getExternalContext().getRequestLocale();
   
    
  }
  return loc;
 }

 public void setLoc(Locale loc) {
  this.loc = loc;
 }

 public int getSlInvLinesInp() {
  return slInvLinesInp;
 }

 public void setSlInvLinesInp(int slInvLinesInp) {
  this.slInvLinesInp = slInvLinesInp;
 }

 public int getMastDataRows() {
  return mastDataRows;
 }

 public void setMastDataRows(int mastDataRows) {
  this.mastDataRows = mastDataRows;
 }

 
 /**
  * @return the maxLoginAttempts
  */
 public int getMaxLoginAttempts() {
  return maxLoginAttempts;
 }

 /**
  * @param maxLoginAttempts the maxLoginAttempts to set
  */
 public void setMaxLoginAttempts(int maxLoginAttempts) {
  this.maxLoginAttempts = maxLoginAttempts;
 }

 public Map<String, String> getNumberTexts() {
  if(numberTexts == null){
  numberTexts = new HashMap<>(40);
  String txt = formTextForKey("one");
  numberTexts.put("1", txt);
  txt = formTextForKey("two");
  numberTexts.put("2", txt);
  txt = formTextForKey("three");
  numberTexts.put("3", txt);
  txt = formTextForKey("four");
  numberTexts.put("4", txt);
  txt = formTextForKey("five");
  numberTexts.put("5", txt);
  txt = formTextForKey("six");
  numberTexts.put("6", txt);
  txt = formTextForKey("seven");
  numberTexts.put("7", txt);
  txt = formTextForKey("eight");
  numberTexts.put("8", txt);
  txt = formTextForKey("nine");
  numberTexts.put("9", txt);
  txt = formTextForKey("ten");
  numberTexts.put("10", txt);
  txt = formTextForKey("eleven");
  numberTexts.put("11", txt);
  txt = formTextForKey("twelve");
  numberTexts.put("12", txt);
  txt = formTextForKey("thirteen");
  numberTexts.put("13", txt);
  txt = formTextForKey("fourteen");
  numberTexts.put("14", txt);
  txt = formTextForKey("fifteen");
  numberTexts.put("15", txt);
  txt = formTextForKey("sixteen");
  numberTexts.put("16", txt);
  txt = formTextForKey("seventeen");
  numberTexts.put("17", txt);
  txt = formTextForKey("eighteen");
  numberTexts.put("18", txt);
  txt = formTextForKey("nineteen");
  numberTexts.put("19", txt);
  txt = formTextForKey("twenty");
  numberTexts.put("20", txt);
  txt = formTextForKey("thirty");
  numberTexts.put("30", txt);
  txt = formTextForKey("forty");
  numberTexts.put("40", txt);
  txt = formTextForKey("fifty");
  numberTexts.put("50", txt);
  txt = formTextForKey("sixty");
  numberTexts.put("60", txt);
  txt = formTextForKey("seventy");
  numberTexts.put("70", txt);
  txt = formTextForKey("eighty");
  numberTexts.put("80", txt);
  txt = formTextForKey("ninety");
  numberTexts.put("90", txt);
  txt = formTextForKey("hundred");
  numberTexts.put("cent", txt);
  txt = formTextForKey("tho");
  numberTexts.put("kilo", txt);
  txt = formTextForKey("mil");
  numberTexts.put("mil", txt);
  txt = formTextForKey("and");
  numberTexts.put("and", txt);
  }
  return numberTexts;
 }

 public void setNumberTexts(Map<String, String> numberTexts) {
  this.numberTexts = numberTexts;
 }

 
 public int getTblRows() {
  return tblRows;
 }

 public void setTblRows(int tblRows) {
  this.tblRows = tblRows;
 }

 public String getTheme() {
  return theme;
 }

 public void setTheme(String theme) {
  this.theme = theme;
 }

 public Map<String, String> getThemes() {
  return themes;
 }

 public void setThemes(Map<String, String> themes) {
  this.themes = themes;
 }

 
 
  public UserRec getUsr() {
   LOGGER.log(INFO, "Session get user {0}", usr);
    return this.usr;
  }

  public void setUsr(UserRec usr) {
   LOGGER.log(INFO, "set usr with {0}", usr);
    this.usr = usr;
  }
  
  

  public NumberFormat getNumberFormat(int decPlaces){
   NumberFormat nf = NumberFormat.getNumberInstance(loc);
   nf.setMinimumFractionDigits(decPlaces);
   nf.setMinimumIntegerDigits(1);
     //Currency curr = Currency.getInstance(loc);
     //nf.setMinimumFractionDigits(decPlaces);
     //nf.setCurrency(curr);
     return nf;
  }
  
  public NumberFormat getNumberFormat(){
   Currency curr = Currency.getInstance(loc);
   NumberFormat nf = NumberFormat.getNumberInstance(loc);
   nf.setMinimumFractionDigits(curr.getDefaultFractionDigits());
   return nf; 
   
  }
  public Currency getCurrency(){
   Currency curr = Currency.getInstance(loc);
   return curr;
  }
  


}
