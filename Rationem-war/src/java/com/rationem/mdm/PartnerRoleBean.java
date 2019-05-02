/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.mdm;

import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.PrimeFaces;

/**
 *
 * @author user
 */
public class PartnerRoleBean extends BaseBean {
 private static final Logger LOGGER = Logger.getLogger(PartnerRoleBean.class.getName());
 
 @EJB
 private MasterDataManager mastDataMgr;
 
 private PartnerRoleRec role;
 private List<PartnerRoleRec> roles;
 private PartnerRoleRec roleSelected;
 
 /**
  * Creates a new instance of PartnerRoleBean
  */
 public PartnerRoleBean() {
 }
 
 @PostConstruct
 private void init(){
  role = new PartnerRoleRec();
  
  if(getViewSimple().equals("partnerRoleUpdt")){
   roles = mastDataMgr.getPartnerRoles();
  }
  
 }

 public PartnerRoleRec getRole() {
  return role;
 }

 public void setRole(PartnerRoleRec role) {
  this.role = role;
 }

 public List<PartnerRoleRec> getRoles() {
  return roles;
 }

 public void setRoles(List<PartnerRoleRec> roles) {
  this.roles = roles;
 }

 public PartnerRoleRec getRoleSelected() {
  return roleSelected;
 }

 public void setRoleSelected(PartnerRoleRec roleSelected) {
  this.roleSelected = roleSelected;
 }
 
 
 public void onSaveRole(){
  LOGGER.log(INFO, "onSaveRole called");
  if(getViewSimple().equals("partnerRoleCr")){
   role.setCreatedBy(getLoggedInUser());
   role.setCreatedOn(new Date());
   role.setInUse(true);
   role = mastDataMgr.updatePartnerRole(role,getView());
   MessageUtil.addClientInfoMessage("okMsg", "ptnrRoleCr", "blacResponse");
   if(role.getId() == null){
    MessageUtil.addClientWarnMessage("errMsg", "partnerRoleSave", "errorText");
   }
  }else{
   roleSelected.setChangedBy(getLoggedInUser());
   roleSelected.setChangedOn(new Date());
   roleSelected = mastDataMgr.updatePartnerRole(roleSelected,getView());
   MessageUtil.addClientInfoMessage("okMsg", "ptnrRoleUpdt", "blacResponse");
   ListIterator<PartnerRoleRec> li = roles.listIterator();
   List<String> updateList = new ArrayList<>();
   updateList.add("ptnrRoleFrm:okMsg");
   PrimeFaces pf = PrimeFaces.current();
   while(li.hasNext()){
    PartnerRoleRec curr = li.next();
    if(Objects.equals(curr.getId(),roleSelected.getId())){
     li.set(roleSelected);
     updateList.add("ptnrRoleFrm:rolesList");
     pf.ajax().update(updateList);
     pf.executeScript("PF('edRlDlgWv').hide();");
    }
   }
  }
  
  if(role.getId() != null){
   if(this.getViewSimple().equals("partnerRoleCr")){
    
    role = new PartnerRoleRec();
   }else{
    
   }
  }else{
   
  }
 }
 
}
