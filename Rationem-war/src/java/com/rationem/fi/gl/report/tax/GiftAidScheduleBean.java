/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.gl.report.tax;

import com.rationem.busRec.tax.GiftAidScheduleLine;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.util.BaseBean;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;


import org.primefaces.model.LazyDataModel;
/**
 *
 * @author Chris
 */
public class GiftAidScheduleBean extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger("accounts.fi.gl.reports.tax.giftAidScheduleBean");

 @EJB
 private DocumentManager docMgr;
 
 private String yearStr;
 private String periodFrStr;
 private String periodToStr;
 private boolean giftLinesLoaded;
 
 private LazyDataModel<GiftAidScheduleLine> giftAidLineslazy;
 private List<GiftAidScheduleLine> giftAidLines;

 /**
  * Creates a new instance of giftAidScheduleBean
  */
 public GiftAidScheduleBean() {
 }

 public String getYearStr() {
  return yearStr;
 }

 public void setYearStr(String yearStr) {
  this.yearStr = yearStr;
 }

 public String getPeriodFrStr() {
  return periodFrStr;
 }

 public void setPeriodFrStr(String periodFrStr) {
  this.periodFrStr = periodFrStr;
 }

 public String getPeriodToStr() {
  return periodToStr;
 }

 public void setPeriodToStr(String periodToStr) {
  this.periodToStr = periodToStr;
 }

 public List<GiftAidScheduleLine> getGiftAidLines() {
  return giftAidLines;
 }

 public void setGiftAidLines(List<GiftAidScheduleLine> giftAidLines) {
  this.giftAidLines = giftAidLines;
 }

 public boolean isGiftLinesLoaded() {
  return giftLinesLoaded;
 }

 public void setGiftLinesLoaded(boolean giftLinesLoaded) {
  this.giftLinesLoaded = giftLinesLoaded;
 }

 public LazyDataModel<GiftAidScheduleLine> getGiftAidLineslazy() {
  return giftAidLineslazy;
 }

 public void setGiftAidLineslazy(LazyDataModel<GiftAidScheduleLine> giftAidLineslazy) {
  this.giftAidLineslazy = giftAidLineslazy;
 }

 public void onGetGiftAid(){
  LOGGER.log(INFO, "onGetGiftAid called");
  int yr = Integer.parseInt(yearStr);
  int perTo = Integer.parseInt(periodToStr);
  int perFr = Integer.parseInt(periodFrStr);
  giftAidLines = docMgr.getGiftAidForYearPeriods(yr, perFr, perTo);
  LOGGER.log(INFO, "GiftaidLines returned by docMgr {0}",giftAidLines);
  this.giftLinesLoaded = true;
 }
 
 public void postProcess(Object file){
  LOGGER.log(INFO, "postProcess {0}", file);
 } 
}
