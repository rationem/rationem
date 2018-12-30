/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.comp;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.util.BaseBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.primefaces.PrimeFaces;

/**
 *
 * @author user
 */
public class CompanyList extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(CompanyList.class.getName());
 
 private CompanyBasicRec compSelected;

 /**
  * Creates a new instance of CompanyList
  */
 public CompanyList() {
 }

 public CompanyBasicRec getCompSelected() {
  return compSelected;
 }

 public void setCompSelected(CompanyBasicRec compSelected) {
  this.compSelected = compSelected;
 }
 
 public void onAddDetBtn(){
  LOGGER.log(INFO, "onAddDetBtn called selected is {0}", this.compSelected.getName());
  PrimeFaces pf = PrimeFaces.current();
  pf.ajax().update("addDetFrm");
  pf.executeScript("PF('addDetDlgV').show();");
 }
 
}
