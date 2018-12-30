/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.util.BaseBean;
import javax.annotation.PostConstruct;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author user
 */
public class PartnerDisplayBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(PartnerBean.class.getName());
 
 @EJB
 private MasterDataManager ptnrMgr;
 
 @EJB
 private SysBuffer sysBuff;
 
 private String partnerType;
 private String ptnrRoleText;
 private boolean ptnrTrading; 
 
 private PartnerBaseRec ptnrBase;
 private PartnerPersonRec ptnrPerson;
 private PartnerCorporateRec ptnrCorp;
 

 /**
  * Creates a new instance of PartnerDisplayBean
  */
 public PartnerDisplayBean() {
 }
 
 @PostConstruct
 private void init(){
  
  
 }

 

 
 public MasterDataManager getPtnrMgr() {
  return ptnrMgr;
 }

 public void setPtnrMgr(MasterDataManager ptnrMgr) {
  this.ptnrMgr = ptnrMgr;
 }

 public SysBuffer getSysBuff() {
  return sysBuff;
 }

 public void setSysBuff(SysBuffer sysBuff) {
  this.sysBuff = sysBuff;
 }

 

 public String getPartnerType() {
  return partnerType;
 }

 public void setPartnerType(String partnerType) {
  this.partnerType = partnerType;
 }

 
 public PartnerBaseRec getPtnrBase() {
  return ptnrBase;
 }

 public void setPtnrBase(PartnerBaseRec ptnrBase) {
  this.ptnrBase = ptnrBase;
 }

 public PartnerPersonRec getPtnrPerson() {
  return ptnrPerson;
 }

 public void setPtnrPerson(PartnerPersonRec ptnrPerson) {
  this.ptnrPerson = ptnrPerson;
 }

 public PartnerCorporateRec getPtnrCorp() {
  return ptnrCorp;
 }

 public void setPtnrCorp(PartnerCorporateRec ptnrCorpptnrCorp) {
  this.ptnrCorp = ptnrCorpptnrCorp;
 }

 public String getPtnrRoleText() {
  return ptnrRoleText;
 }

 public void setPtnrRoleText(String ptnrRoleText) {
  this.ptnrRoleText = ptnrRoleText;
 }

 public boolean isPtnrTrading() {
  return ptnrTrading;
 }

 public void setPtnrTrading(boolean ptnrTrading) {
  this.ptnrTrading = ptnrTrading;
 }
 
 
 public List<PartnerBaseRec> onPtnrRefComplete(String input){
  List<PartnerBaseRec> retList;
  LOGGER.log(INFO, "onPtnrRefComplete called with {0}", input);
  if(input == null || input.isEmpty()){
  retList = this.ptnrMgr.getPartnersAll();
  }else{
   retList = this.ptnrMgr.getPartnersByRef(input);
  }
  return retList;
 }
 
 public  void onPtnrRefSelect(SelectEvent evt){
  this.ptnrBase = (PartnerBaseRec)evt.getObject();
  String pClass = ptnrBase.getClass().getSimpleName();
  if(pClass.equalsIgnoreCase("PartnerCorporateRec")){
   this.partnerType = formTextForKey("ptnrCorp");
   this.ptnrCorp = (PartnerCorporateRec)ptnrBase;
   
  }else{
   this.partnerType = formTextForKey("ptnrIndiv");
   this.ptnrPerson = (PartnerPersonRec)ptnrBase;
   
  }
  this.ptnrTrading = false;
  if(ptnrBase.getPartnerRoles() == null || ptnrBase.getPartnerRoles().isEmpty()){
   ptnrBase = this.ptnrMgr.getRolesForPtnr(ptnrBase);
  }
  
  if(ptnrBase.getPartnerRoles() == null || !ptnrBase.getPartnerRoles().isEmpty()){
   StringBuilder sb = null; 
   for(PartnerRoleRec pr:ptnrBase.getPartnerRoles()){
    
    if(sb == null){
     sb = new StringBuilder(); 
     sb.append(pr.getRoleName());
    }else{
     sb.append(", ");
     sb.append(pr.getRoleName());
    }
    // check to see if partnerTrading
    if(!ptnrTrading){
     if(pr.isTaxable()){
      ptnrTrading = true;
     }
    }
   }
   if(sb != null){
   this.ptnrRoleText = sb.toString();
   }
  }
  LOGGER.log(INFO, "ptnrType {0}", partnerType);
  PrimeFaces pf = PrimeFaces.current();
  List<String> fields = new ArrayList<>();
  fields.add("ptnrCrFrm:tabV");
  fields.add("ptnrCrFrm:ptnrName");
  pf.ajax().update(fields);
  
  
  
  
 }
 
}
