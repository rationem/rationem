/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class CompanyFiscalPeriodYearForDate {
 
 private Long companyId;
 private CompanyBasicRec comp;
 private Date date;
 private FiscalPeriodYearRec fisPeriodYear;

 public CompanyFiscalPeriodYearForDate() {
 }

 public Long getCompanyId() {
  return companyId;
 }

 public void setCompanyId(Long companyId) {
  this.companyId = companyId;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public Date getDate() {
  return date;
 }

 public void setDate(Date date) {
  this.date = date;
 }

 public FiscalPeriodYearRec getFisPeriodYear() {
  return fisPeriodYear;
 }

 public void setFisPeriodYear(FiscalPeriodYearRec fisPeriodYear) {
  this.fisPeriodYear = fisPeriodYear;
 }
 
 
 
 
}
