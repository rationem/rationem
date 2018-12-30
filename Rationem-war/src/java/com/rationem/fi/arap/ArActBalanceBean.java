/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Chris
 */
public class ArActBalanceBean {
 
 @EJB
 private SysBuffer sysBuff;
 
 private CompanyBasicRec compFrom;
 private CompanyBasicRec compTo;
 
 private List<CompanyBasicRec> compList;
 private List<ArAccountRec> arAccountList;
 

 /**
  * Creates a new instance of arActBalanceBean
  */
 public ArActBalanceBean() {
 }

 public CompanyBasicRec getCompFrom() {
  return compFrom;
 }

 public void setCompFrom(CompanyBasicRec compFrom) {
  this.compFrom = compFrom;
 }

 public CompanyBasicRec getCompTo() {
  return compTo;
 }

 public void setCompTo(CompanyBasicRec compTo) {
  this.compTo = compTo;
 }

 public List<CompanyBasicRec> companyComplete(String input){
  List<CompanyBasicRec> compList = new ArrayList<CompanyBasicRec>();
  return compList;
 }
 public List<CompanyBasicRec> getCompList() {
  if(compList == null){
   compList = sysBuff.getCompanies();
  }
  return compList;
 }

 public void setCompList(List<CompanyBasicRec> compList) {
  this.compList = compList;
 }

 public List<ArAccountRec> getArAccountList() {
  return arAccountList;
 }

 public void setArAccountList(List<ArAccountRec> arAccountList) {
  this.arAccountList = arAccountList;
 }
 
 
 
}
