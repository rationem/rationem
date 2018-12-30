/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

/**
 *
 * @author Chris
 */
public class CalendarRuleFixedDateRec extends CalendarRuleBaseRec {
 private int dayOfMonth;
 private int startMonthNumber;
 private int periodNum;
 private int specialPeriodNum;

 public CalendarRuleFixedDateRec() {
  super();
 }
 
 

 public int getDayOfMonth() {
  return dayOfMonth;
 }

 public void setDayOfMonth(int dayOfMonth) {
  this.dayOfMonth = dayOfMonth;
 }

 public int getPeriodNum() {
  return periodNum;
 }

 public void setPeriodNum(int periodNum) {
  this.periodNum = periodNum;
 }

 public int getSpecialPeriodNum() {
  return specialPeriodNum;
 }

 public void setSpecialPeriodNum(int specialPeriodNum) {
  this.specialPeriodNum = specialPeriodNum;
 }

 public int getStartMonthNumber() {
  return startMonthNumber;
 }

 public void setStartMonthNumber(int startMonthNumber) {
  this.startMonthNumber = startMonthNumber;
 }
 
 public Long getStartMonthNumberL() {
  Integer numI = startMonthNumber; 
  return numI.longValue();
 }
 
 public void setStartMonthNumberL(Long num) {
  if(num != null){
   startMonthNumber = num.intValue();
  }
  
 }
 
}
