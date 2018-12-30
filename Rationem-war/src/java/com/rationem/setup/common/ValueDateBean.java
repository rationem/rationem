/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.util.BaseBean;
import com.rationem.util.GenUtil;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.common.ValueDateRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */

public class ValueDateBean extends BaseBean{
 private static final Logger LOGGER =
            Logger.getLogger("accounts.beans.setup.common.ValueDateBean");
 @EJB
 private BasicSetup setupMgr;
 
 @EJB
 private SysBuffer sysBuff;;
 private ValueDateRec valueDate;
 /**
  * Creates a new instance of ValueDateBean
  */
 public ValueDateBean() {
 }

 public ValueDateRec getValueDate() {
  if(valueDate == null){
   valueDate = new ValueDateRec();
  }
  return valueDate;
 }

 public void setValueDate(ValueDateRec valueDate) {
  this.valueDate = valueDate;
 }
 
 public List<UomRec> uomComplete(String input){
  List<UomRec> uoms = sysBuff.getUoms();
  if(input == null || input.isEmpty()){
   return uoms;
  }
  ArrayList<UomRec> retUoms = new ArrayList<UomRec>();
  for(UomRec uom : uoms){
   if(uom.getUomCode().startsWith(input)){
    retUoms.add(uom);
   }
    
  }
  return retUoms;
  
 }
 
 public void onSaveValueDate(){
  LOGGER.log(INFO, "onSaveValueDate called");
  try{
   valueDate = this.setupMgr.addValueDate(valueDate, this.getLoggedInUser(), this.getView());
   String msg = this.responseForKey("valueDtSaved");
   GenUtil.addInfoMessage(msg);
  }catch(BacException ex){
   String msg = this.errorForKey("valueDtSaveErrB")+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
  }catch(Exception ex){
   String msg = this.errorForKey("valueDtSaveErrOth")+ex.getLocalizedMessage();
   GenUtil.addErrorMessage(msg);
 }
 }
 
 public void onResetValueDate(){
  LOGGER.log(INFO, "onResetValueDate called");
  valueDate = null;
 }
 
}
