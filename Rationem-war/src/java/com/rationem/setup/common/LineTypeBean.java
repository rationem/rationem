/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.common;

import java.util.Date;
import com.rationem.busRec.config.common.LineTypeRuleTextRec;
import com.rationem.exception.BacException;
import com.rationem.util.GenUtil;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.busRec.config.common.ModuleRec;
import java.util.ListIterator;
import java.io.Serializable;
import com.rationem.ejbBean.config.common.BasicSetup;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.event.AjaxBehaviorEvent;
import java.util.ArrayList;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import java.util.List;


import java.util.logging.Logger;
import static java.util.logging.Level.INFO;
import org.primefaces.context.RequestContext;



/**
 *
 * @author Chris
 */
public class LineTypeBean extends BaseBean  {

    private LineTypeRuleRec lineType;
    private LineTypeRuleRec lineTypeSelected;
    private List<LineTypeRuleRec> lineTypes;
    private LineTypeRuleRec selectedLineType;
    private List<ModuleRec> modules;
    


    @EJB
    BasicSetup setup;

    @EJB
    SysBuffer buffer;

    private static final Logger logger =
            Logger.getLogger("accounts.beans.setup.common.LineTypeBean");

    /** Creates a new instance of LineTypeBean */
    public LineTypeBean() {
        logger.log(INFO, "Create LineTypeBean");
    }

    public LineTypeRuleRec getLineType() {
        
        if(lineType == null){
            lineType = new LineTypeRuleRec();
         DefaultLoadBean load = new DefaultLoadBean();
                 
        }
        
        return lineType;
    }

    public void setLineType(LineTypeRuleRec lineType) {
        this.lineType = lineType;
        
    }

 public LineTypeRuleRec getLineTypeSelected() {
  return lineTypeSelected;
 }

 public void setLineTypeSelected(LineTypeRuleRec lineTypeSelected) {
  this.lineTypeSelected = lineTypeSelected;
 }

    
    public List<LineTypeRuleRec> getLineTypes() {
     if(lineTypes == null ){
      lineTypes = buffer.getLineTypeRules();
     }
        return lineTypes;
    }

    public void setLineTypes(List<LineTypeRuleRec> lineTypes) {
        this.lineTypes = lineTypes;
    }

 public List<ModuleRec> getModules() {
  if(modules == null){
   modules = buffer.getModules();
   logger.log(INFO, "modules from buffer {0}", modules);
  }
  return modules;
 }

 public void setModules(List<ModuleRec> modules) {
  this.modules = modules;
 }

    
   
    public LineTypeRuleRec getSelectedLineType() {
        return selectedLineType;
    }

    public void setSelectedLineType(LineTypeRuleRec selectedLineType) {
        this.selectedLineType = selectedLineType;
    }

    

    
    public void onCreateLineTypeSave(){
     logger.log(INFO, "called onCreateLineTypeSave");
     try{
      lineType.setCreatedBy(this.getLoggedInUser());
      lineType.setCreatedDate(new Date());
      setup.createLineType(lineType, getView());
      buffer.lineTypeUpdate(lineType);
      MessageUtil.addInfoMessage("lineTypeCr", "blacResponse");
     }catch(Exception e){
      MessageUtil.addErrorMessage("lineTypeSv", "errorText");
      

     }
    }

    public void onLineTypeEditDlg(){
     logger.log(INFO, "onLineTypeEditDlg called with selected {0}", this.lineTypeSelected);
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.update("editLineTyFrm");
     rCtx.execute("PF('lineTypeEditDlg').show()");
    }

    public void onLineTypeEditClose(){
     RequestContext rCtx = RequestContext.getCurrentInstance();
     rCtx.execute("PF('lineTypeEditDlg').hide()");
    }
    
    public void onLineTypeEditTrf(){
     logger.log(INFO, "onLineTypeEditTrf");
     try{
     lineTypeSelected.setChangedBy(this.getLoggedInUser());
     lineTypeSelected.setChangedDate(new Date());
     setup.updateLineType(lineTypeSelected, getView());
     buffer.lineTypeUpdate(lineTypeSelected);
     
     ListIterator<LineTypeRuleRec> li = lineTypes.listIterator();
     boolean foundLn = false;
     while(li.hasNext() && !foundLn){
      LineTypeRuleRec ln = li.next();
      
      if(ln.getId() == lineTypeSelected.getId()){
       li.set(lineTypeSelected);
       foundLn = true;
       RequestContext rCtx = RequestContext.getCurrentInstance();
       rCtx.update("lineTypeFrm");
       rCtx.execute("PF('lineTypeEditDlg').hide()");
      }
     }
     MessageUtil.addInfoMessage("lineTypeUpdt", "blacResponse");
     }catch(Exception ex){
      logger.log(INFO, "Exception {0}", ex.getLocalizedMessage());
      MessageUtil.addErrorMessage("lineTypeUpdt", "errorText");
     }
    }
    

}
