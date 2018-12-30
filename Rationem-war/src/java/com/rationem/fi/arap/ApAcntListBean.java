/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.arap;

import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.fi.ApManager;
import com.rationem.util.BaseBean;
import java.util.List;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author user
 */
public class ApAcntListBean extends BaseBean {
 //private static final Logger LOGGER =  Logger.getLogger(ApAcntListBean.class.getName());
 
 @EJB
 private ApManager apMgr;
 private CompanyBasicRec selComp;
 private List<ApAccountRec> apAcnts;
 private List<ApAccountRec> apAcntsFiltered;

 /**
  * Creates a new instance of apAcntListBean
  */
 public ApAcntListBean() {
 }
 @PostConstruct
 private void init(){
//  LOGGER.log(INFO, "Account List Bean created");
 
  selComp = getCompList().get(0);
  apAcnts = apMgr.getApAccountsAll(selComp);
  
 }
 
 // Getter and setters

 public CompanyBasicRec getSelComp() {
  return selComp;
 }

 public void setSelComp(CompanyBasicRec selComp) {
  this.selComp = selComp;
 }

 public List<ApAccountRec> getApAcnts() {
  return apAcnts;
 }

 public void setApAcnts(List<ApAccountRec> apAcnts) {
  this.apAcnts = apAcnts;
 }

 public List<ApAccountRec> getApAcntsFiltered() {
  return apAcntsFiltered;
 }

 public void setApAcntsFiltered(List<ApAccountRec> apAcntsFiltered) {
  this.apAcntsFiltered = apAcntsFiltered;
 }

  
}
