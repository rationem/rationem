/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;

import java.io.Serializable;

/**
 * Structure to hold a Fiscal year and date combination
 * @author Chris
 */
public class FiscalPeriodYearRec implements Serializable {

  private int period;
  private int year;

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

  

}
