/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Structure to hold a Fiscal year and date combination
 * @author Chris
 */
public class FiscalPeriodYearRec implements Serializable {

  private int period;
  private int year;
  private String yearPeriod;

  public FiscalPeriodYearRec() {
  }

  public FiscalPeriodYearRec(int period, int year) {
    this.period = period;
    this.year = year;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

 public String getYearPeriod() {
  if(StringUtils.isBlank(yearPeriod)){
   yearPeriod = String.valueOf(year) + "/"+String.valueOf(period);
  }
  return yearPeriod;
 }

 public void setYearPeriod(String yearPeriod) {
  this.yearPeriod = yearPeriod;
 }

  

}
