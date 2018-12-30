/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.fi.tr;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BacsSetup;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Chris
 */
public class BacsTransCodeBean extends BaseBean {
 private static final Logger LOGGER =
            Logger.getLogger("accounts.fi.tr.BacsTransCodeBean");
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private BacsSetup bacsSetup;
 
 @EJB
 private BankManager bankMgr;
 
 private BacsTransCodeRec bacsTransCode;
 private List<BacsTransCodeRec> bacsTransCodes;
 private BacsTransCodeRec bacsTransCodeSel;
 

 /**
  * Creates a new instance of BacsTransCodeBean
  */
 public BacsTransCodeBean() {
 }

 @PostConstruct
 private void init(){
  if(this.getViewSimple().equals("bacsTransCodeUpdt")){
   this.bacsTransCodes = this.sysBuff.getBacsTransactionCodes();
   
  }else{
   bacsTransCode = new BacsTransCodeRec();
  }
 }

 public BacsTransCodeRec getBacsTransCode() {
  if(bacsTransCode == null){
   bacsTransCode = new BacsTransCodeRec();
  }
  return bacsTransCode;
 }

 public void setBacsTransCode(BacsTransCodeRec bacsTransCode) {
  this.bacsTransCode = bacsTransCode;
 }

 public List<BacsTransCodeRec> getBacsTransCodes() {
  return bacsTransCodes;
 }

 public void setBacsTransCodes(List<BacsTransCodeRec> bacsTransCodes) {
  this.bacsTransCodes = bacsTransCodes;
 }

 public BacsTransCodeRec getBacsTransCodeSel() {
  return bacsTransCodeSel;
 }

 public void setBacsTransCodeSel(BacsTransCodeRec bacsTransCodeSel) {
  this.bacsTransCodeSel = bacsTransCodeSel;
 }
 
 
 
 public void onCreateSave(){
  LOGGER.log(INFO, "BacsTrans.onCreateSave called");
  try{
   bacsTransCode.setId(null);
   bacsTransCode.setCreatedBy(this.getLoggedInUser());
   bacsTransCode.setCreatedOn(new Date());
   bacsTransCode = bankMgr.updateBacsTransCode(bacsTransCode, getView());
   LOGGER.log(INFO, "Bean bacs code id {0}",bacsTransCode.getId());
   if(bacsTransCode.getId() != null){
    MessageUtil.addInfoMessageVar1("trBacsTransCdCr", "blacResponse", bacsTransCode.getPtnrBnkTransCode());
    bacsTransCode = null;
   }else{
    MessageUtil.addErrorMessageParam1("bacsTransCdCr", "errorText", bacsTransCode.getPtnrBnkTransCode());
   }
  }catch(BacException ex){
   MessageUtil.addErrorMessageParam1("bacsTransCdCr", "errorText", bacsTransCode.getPtnrBnkTransCode());
  }catch(Exception ex){
   GenUtil.addErrorMessage(ex.getLocalizedMessage());
  }
 }
 
 public void onEditTransCode(){
  LOGGER.log(INFO, "onEditTransCode");
  LOGGER.log(INFO, "Select trans code {0}", this.bacsTransCodeSel);
  
 }
 
 public void onEditTransf(){
  LOGGER.log(INFO, "onEditTransf called");
  ListIterator<BacsTransCodeRec> li = bacsTransCodes.listIterator();
  boolean found = false;
  while(li.hasNext() && !found){
   BacsTransCodeRec curr = li.next();
   
   if(Objects.equals(curr.getId(), bacsTransCodeSel.getId())){
    bacsTransCodeSel.setChangedBy(getLoggedInUser());
    bacsTransCodeSel.setChangedOn(new Date());
    bacsTransCodeSel = bankMgr.updateBacsTransCode(bacsTransCodeSel, getView());
    li.set(bacsTransCodeSel);
    found = true;
   }
  }
  if(found){
   RequestContext rCtx = RequestContext.getCurrentInstance();
   rCtx.update("bacsCdLst:codeList");
   rCtx.execute("PF('editTranCdDlg').hide();");
  }
 }
 
 public void onDelTransCode(){
  LOGGER.log(INFO, "onDelTransCode");
  LOGGER.log(INFO, "Select trans code {0}", this.bacsTransCodeSel);
  if(bacsTransCodeSel == null){
   LOGGER.log(INFO, "No item selected for deletion");
   return ;
  }
  boolean rc = this.bankMgr.bacsTransCodeDelete(bacsTransCodeSel, this.getLoggedInUser(), getView());
  if(rc){
   ListIterator<BacsTransCodeRec> li = this.bacsTransCodes.listIterator();
   boolean found = false;
   while(li.hasNext()){
    BacsTransCodeRec curr = li.next();
    if(Objects.equals(curr.getId(), bacsTransCodeSel.getId())){
     li.remove();
     found = true;
    }
   }
   if(found){
    RequestContext.getCurrentInstance().update("bacsCdLst:codeList");
    MessageUtil.addInfoMessageVar1("trBacsTransCdDel", "blacResponse", bacsTransCodeSel.getPtnrBnkTransCode());
   }else{
    MessageUtil.addErrorMessageParam1("bacsTransCodeDel", "errorText", bacsTransCodeSel.getPtnrBnkTransCode());
   }
   
  }
  
 }
 public void onReset(){
  bacsTransCode = new BacsTransCodeRec();
 }
 
}
