/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.util.BaseBean;
import java.util.logging.Logger;
import javax.ejb.EJB;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author user
 */
public class PartnerArAccountCr extends BaseBean{
 private static final Logger LOGGER = Logger.getLogger(PartnerBean.class.getName());
 
 @EJB
 private MasterDataManager ptnrMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private PartnerBaseRec ptnrBase;
 private ArAccountRec arAccount;

 /**
  * Creates a new instance of PartnerArAccountCr
  */
 public PartnerArAccountCr() {
 }
 
 @PostConstruct
 private void init(){
  ptnrBase = (PartnerBaseRec)getSessionMap().get("partner");
  getSessionMap().remove("partner");
  arAccount = new ArAccountRec();
 }

 public ArAccountRec getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccountRec arAccount) {
  this.arAccount = arAccount;
 }

 
 public PartnerBaseRec getPtnrBase() {
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
 }
 
 public void onAcntNumValidate(FacesContext c, UIComponent comp, Object val){
  LOGGER.log(INFO, "onAcntNumValidate called with val {0}", val);
 }
 
 
 
 
}
