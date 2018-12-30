/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Table;
import javax.persistence.Column;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;



/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config03" )
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRuleFixedDate")
@PrimaryKeyJoinColumn(name="fixed_period_id",referencedColumnName = "fis_per_cal_base_id")


public class CalendarRuleFixedDate extends CalendarRuleBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="DAY_OF_MONTH")
 private int dayOfMonth;
 @Column(name="MONTH_NUM")
 private int startMonthNumber;
 @Column(name="YEAR_OFFSET")
 private int yearOffset;
 @Column(name="PERIOD_NUM")
 private int periodNum;
 @Column(name="SPECIAL_PERIOD")
 private int specialPeriodNum;

 public int getDayOfMonth() {
  return dayOfMonth;
 }

 public void setDayOfMonth(int dayOfMonth) {
  this.dayOfMonth = dayOfMonth;
 }

 public int getStartMonthNumber() {
  return startMonthNumber;
 }

 public void setStartMonthNumber(int startMonthNumber) {
  this.startMonthNumber = startMonthNumber;
 }

 public int getYearOffset() {
  return yearOffset;
 }

 public void setYearOffset(int yearOffset) {
  this.yearOffset = yearOffset;
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
 
 


 
}
