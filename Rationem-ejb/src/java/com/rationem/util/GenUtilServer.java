/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.mdm.CurrencyRec;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Chris
 */
public class GenUtilServer {
 
 private static final Logger LOGGER = Logger.getLogger(GenUtilServer.class.getName());
 
 public static List<String> getChequeAmountWords(CurrencyRec curr, double amount, 
   Map<String, String> texts ){
  LOGGER.log(INFO, "getChequeAmountWords called");
  LOGGER.log(INFO, "texts {0}", texts);
  List<String> retList = new ArrayList<>(3);
  // 40 characters each line
  String amntStr = GenUtilServer.numberDp(amount, curr.getMinorUnit());
  amntStr = StringUtils.remove(amntStr, ',');
  char[] valsCh = amntStr.toCharArray();
 
  LOGGER.log(INFO, "amntStr {0}",amntStr);
  LOGGER.log(INFO, "valsCh {0}",valsCh[0]);
  
  int majorUnits = amntStr.indexOf('.');
  LOGGER.log(INFO, "Major unit size {0}", majorUnits);
  int currChar = 0;
  
  int currPos = majorUnits;
  String currValStr;
  
  String payLine = new String();
  String currValPlace ;
  while(currPos > 0){
   LOGGER.log(INFO,"currPos {0}",currPos);
   currValStr = null;
   currValPlace = null;
   
   
   switch (currPos) {
    
    case 7:
      
     currValStr = String.valueOf(valsCh[currChar]);
     LOGGER.log(INFO,"currValStr {0}",currValStr);
     String currValText = texts.get(currValStr);
     LOGGER.log(INFO,"currText {0}",currValText);
     String currPlaceText = texts.get("mil");
     LOGGER.log(INFO, amntStr, payLine);
     currValPlace =  currValText + " "+currPlaceText +" ";
     LOGGER.log(INFO,"currValPlace {0}",currValPlace);
     break;
    case 6:
     
     
     currValStr = String.valueOf(valsCh[currChar]);
     LOGGER.log(INFO,"currValStr {0}",currValStr);
    
     currValText = texts.get(currValStr);
     currPlaceText = texts.get("cent");
     currValPlace =  currValText + " "+currPlaceText+" ";
     String andStr = texts.get("and");
     currValPlace += andStr + " ";     
     LOGGER.log(INFO, "currValPlace {0}", currValPlace);
     
     
     break;
    
    case 5:
     currValStr = String.valueOf(valsCh[currChar]);
     //currValText = texts.get(currValStr);
     
     if(valsCh[currChar] == '1'){
      //need to do teens
     }else{
      currValStr += "0";
      LOGGER.log(INFO, "currValStr {0}", currValStr);
      currValText = texts.get(currValStr);
      currValPlace =  currValText + " ";
     }
     
     break; 
    case 4:
     currValStr = String.valueOf(valsCh[currChar]);
     currValText = texts.get(currValStr);
     currValPlace = currValText + " ";
     currPlaceText = texts.get("kilo");
     currValPlace += currPlaceText + " ";
     break;
    case 3:
     currValStr = String.valueOf(valsCh[currChar]);
     currValText = texts.get(currValStr);
     currValPlace = currValText + " ";
     currPlaceText = texts.get("cent");
     currValPlace += currPlaceText + " ";
     andStr = texts.get("and");
     currValPlace += andStr + " ";
     break;
    case 2:
     currValStr = String.valueOf(valsCh[currChar]);
     //currValText = texts.get(currValStr);
     
     if(valsCh[currChar] == '1'){
      //need to do teens
      LOGGER.log(INFO, "number in 10s");
      currChar++;
      String nextValStr = String.valueOf(valsCh[currChar]);
      currValStr += nextValStr;
      currValText = texts.get(currValStr);
      currValPlace =  currValText + " "+"Pounds";
      currPos--;
     }else{
      currValStr += "0";
      LOGGER.log(INFO, "currValStr {0}", currValStr);
      currValText = texts.get(currValStr);
      currValPlace =  currValText + " ";
      
     }
     break;
     
    case 1:
     currValStr = String.valueOf(valsCh[currChar]);
     
     currValText = texts.get(currValStr);
     currValPlace =  currValText + " "+"Pounds";
     
     
    default:
     break;
   }
   
   currChar ++;
   currPos--;
  
  if(currValPlace != null){
  if(payLine.isEmpty()){
   payLine = currValPlace;
  }else {
   // preceding value
   payLine += currValPlace;
  }
  }
  LOGGER.log(INFO, "end of loop payLine {0}", payLine);
  LOGGER.log(INFO, "payLine len {0}", payLine.length());
  if(payLine.length() > 40){
   // need to add to payline array 
   int splitIndex = StringUtils.lastIndexOf(payLine, " ");
   String currPayLine = StringUtils.substring(payLine, 0, splitIndex);
   LOGGER.log(INFO, "currPayLine {0}", currPayLine);
   retList.add(currPayLine);
   payLine = StringUtils.substring(payLine, splitIndex);
   LOGGER.log(INFO, "payLine {0} length now {1}", new Object[]{payLine,payLine.length()});
  }
  
  }
  retList.add(payLine);
  
  
  
  return retList;
 }
 
 public static Date getFirstWeekDayForDate(Date dt){
  GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
  cal.setTime(dt);
  int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
  if(dayOfWeek == Calendar.SATURDAY){
   cal.add(Calendar.DATE, -1);
   return cal.getTime();
  }else if(dayOfWeek == Calendar.SATURDAY){
   cal.add(Calendar.DATE, -2);
   return cal.getTime();
  }else{
   return dt;
  }
  
  
 }
 
 public static synchronized String formatNumberLocDp(double number, Locale loc){
  NumberFormat nf = NumberFormat.getNumberInstance(loc);
  NumberFormat cf = NumberFormat.getCurrencyInstance(loc);
  nf.setMinimumFractionDigits(cf.getMinimumFractionDigits());
  nf.setMinimumIntegerDigits(cf.getMinimumIntegerDigits());
  nf.setMaximumFractionDigits(cf.getMaximumFractionDigits());
  String output = nf.format(number);
  return output;
 }
 
 public static synchronized String formatDateStringLocalised(Date dt, Locale loc){
  
  
  DateFormat fmt = DateFormat.getDateInstance(DateFormat.MEDIUM, loc);
  String ret = fmt.format(dt);
  
  return ret;
  

 }
 
 public static String jdbcDate(Date dt){
  String dateStr = DateFormatUtils.ISO_DATE_FORMAT.format(dt);
  dateStr = "\'"+dateStr+"\'";
  return dateStr;
 }
 
 public static String jdbcStringValue(String str){
  return "\'"+str+"\'";
 }
 
 public static String numberDp(double val, int minorUnit){
  NumberFormat nf = NumberFormat.getNumberInstance();
  nf.setMinimumFractionDigits(minorUnit);
  nf.setMaximumFractionDigits(minorUnit);
  nf.setGroupingUsed(true);
  nf.setRoundingMode(RoundingMode.HALF_UP);
  return nf.format(val);
 }
 
}
