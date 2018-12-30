/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.common;

import com.rationem.exception.BacException;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Chris
 */
public class UnitOfmeasureBean extends BaseBean {
    private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.common.UnitOfmeasureBean");

    @EJB
    private BasicSetup setup;

    private UomRec uom;
    private UomRec uomSelected;
    private List<UomRec> uomList;
    private List<UomRec> uomListFiltered;
    

    /** Creates a new instance of UnitOfmeasureBean */
    public UnitOfmeasureBean() {
        logger.log(INFO, "Create UnitOfmeasureBean instance");
    }

    public UomRec getUom() {
        if(uom == null){
            logger.log(INFO, "Need to get new uom");
            uom = setup.getUom();
        }

        return uom;
    }

    public void setUom(UomRec uom) {
        this.uom = uom;
    }

 public List<UomRec> getUomList() {
  if(uomList == null ){
   uomList = this.setup.getUoms();
  }
  return uomList;
 }

 public void setUomList(List<UomRec> uomList) {
  this.uomList = uomList;
 }

    public List<UomRec> getUomListFiltered() {
        return uomListFiltered;
    }

    public void setUomListFiltered(List<UomRec> uomListFiltered) {
        this.uomListFiltered = uomListFiltered;
    }

 public UomRec getUomSelected() {
  if(uomSelected == null){
   uomSelected = new UomRec();
  }
  return uomSelected;
 }

 public void setUomSelected(UomRec uomSelected) {
  logger.log(INFO, "uomSelected {0}", uomSelected);
  if(uomSelected != null){
  this.uomSelected = uomSelected;
  }
 }
  
 public void onEdit(){
  logger.log(INFO, "uomSelected {0}", uomSelected);
  if(uomSelected == null){
   MessageUtil.addErrorMessage("uomSelected", "errorText");
   return;
  }
  RequestContext rCtx = RequestContext.getCurrentInstance();
  rCtx.update("editPgId");
  rCtx.update("editCode");
  
  rCtx.execute("PF('editUomWv').show()");
 }
    
    public void createUomAction(){
        logger.log(INFO, "CreateUomkAction");
        try{
         uom.setCreatedBy(this.getLoggedInUser());
         uom.setCreateDate(new Date());
         setup.createUom(uom,getView() );
         String msgHdr = this.responseForKey("uom");
         String msg = responseForKey("uomCr") +uom.getName();
         MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
         uom = new UomRec();
         RequestContext rCtx = RequestContext.getCurrentInstance();
         rCtx.update("uom");
        }catch(BacException e){
         String msgHdr = this.responseForKey("uom") ;
         String msg = this.errorForKey("uomCr")  +uom.getName();
         MessageUtil.addErrorMessageWithoutKey(msgHdr, msg);
            
        }

    }

  public void onUpdateUom(){
   logger.log(INFO, "onUpdateUom {0}", this.uomSelected);
   ListIterator<UomRec> li = this.uomList.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    UomRec toUpdate = li.next();
    if(Objects.equals(toUpdate.getId(), uomSelected.getId())){
     li.set(uomSelected);
     int currVer = uomSelected.getRevision();
     uomSelected.setChangedBy(getLoggedInUser());
     uomSelected.setChangeDate(new Date());
     uomSelected = setup.updateUom(uomSelected, getView());
     int updtVer = uomSelected.getRevision();
     logger.log(INFO, "Pre update {0} after update {1}", new Object[]{currVer,updtVer});
     found = true;
     if(updtVer > currVer){
     String msg = this.responseForKey("uomUpdt") + uomSelected.getUomCode();
     String msgHdr = this.responseForKey("uom");
     MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
     }else{
      String msgHdr = this.responseForKey("uom");
      String msg = this.responseForKey("uomNoCh") +uomSelected.getUomCode();
      MessageUtil.addWarnMessageWithoutKey(msgHdr, msg);
      
     }
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("uomTblId");
     rCtx.execute("PF('editUomWv').hide()");
    }
   }
  }


}
