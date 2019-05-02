/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.setup.common;

import com.rationem.util.BaseBean;
import org.primefaces.event.SelectEvent;
import javax.faces.event.ValueChangeEvent;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.util.GenUtil;
import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeTypeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.exception.BacException;
import com.rationem.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;



/**
 *
 * @author Chris
 */
public class NumberControlBean extends BaseBean{

    private static final Logger LOGGER = Logger.getLogger(NumberControlBean.class.getName());


    @EJB
    public BasicSetup setup;
    
    @EJB
    public SysBuffer sysBuff;

    public NumberRangeRec numRange;

    private int modSelId;

    private List<SelectItem> moduleSel;
    private List<NumberRangeRec> numberControlList;
    private List<NumberRangeRec> numberControlsFiltered;
    private List<NumberRangeRec> changedNumCntrls;
    private List<NumberRangeRec> deletedNumCntrls;
    private List<NumberRangeTypeRec> numRangeTypeList;
    private NumberRangeTypeRec numRangeTypeFiltered;
    private NumberRangeTypeRec numRangeTypeSelected;
    private NumberRangeRec selectNumberControl;
    private List<ModuleRec> modules;
    private boolean updatesDisabled = true;
    private DefaultLoadBean ld = null;



    /** Creates a new instance of NumberControlBean */
    public NumberControlBean() {
    }

    @PostConstruct
    private void init(){
     numRangeTypeList = sysBuff.getNumRangeTypes();
     if(StringUtils.equals(this.getViewSimple(), "numberRangeCr")){
      numRange = new NumberRangeRec();
     }
     
      
    // ld.loadNumberRanges("NumberRange.xls");
    }
    
    public NumberRangeRec getNumRange() {
     if(numRange == null){
      numRange = setup.getNumberControl();
      LOGGER.log(INFO, "ld {0}", ld);
      if(ld == null){
       ld = new DefaultLoadBean();
      }
      LOGGER.log(INFO, "ld 2 {0}", ld);
      
      LOGGER.log(INFO, "After load defaults");
     }
     return numRange;
    }

    public void setNumRange(NumberRangeRec numRange) {
        this.numRange = numRange;
    }

  public List<ModuleRec> getModules() {

    if(modules == null || modules.isEmpty()){
      modules = setup.getAllModules();
    }
    LOGGER.log(INFO,"getModules {0}",modules.size());
    return modules;
  }

  public void setModules(ArrayList<ModuleRec> modules) {
    this.modules = modules;
  }

    public List<SelectItem> getModuleSel(){
        LOGGER.log(INFO, "Web moduleSel");
        moduleSel = new ArrayList<>();
        SelectItem sel = new SelectItem();
        sel.setNoSelectionOption(true);
        sel.setLabel("Please select a module");
        moduleSel.add(sel);
        try{
            modules = setup.getAllModules();
            ListIterator it = modules.listIterator();
            while(it.hasNext()){
               ModuleRec module = (ModuleRec)it.next();
               sel = new SelectItem();
               sel.setValue(module.getId());
               sel.setLabel(module.getDescription());
               moduleSel.add(sel);
            }
        }catch(BacException e){
            GenUtil.addErrorMessage("Could not retrieve modules: "+e.getLocalizedMessage());
        }
        LOGGER.log(INFO, "module Select items returned {0}",moduleSel.size());
        return moduleSel;
    }

    public void setModuleSel(ArrayList<SelectItem> moduleSel) {
        this.moduleSel = moduleSel;
    }

    

    public int getModSelId() {
        return modSelId;
    }

    public void setModSelId(int modSelId) {
        this.modSelId = modSelId;
    }

    public void moduleValueChange(ValueChangeEvent e){
        LOGGER.log(INFO, "moduleValueChange called with {0}", e.getNewValue());
        if(e.getNewValue() == null){
          return ;
        }
        try{
            
            Long modId = new Long(String.valueOf(e.getNewValue()));
            ModuleRec rec = this.setup.getModuleById(modId);
            this.numRange.setModule(rec);
            LOGGER.log(INFO, "Web module has been set to" , rec);
        }catch(BacException er){
            GenUtil.addErrorMessage("Error finding module");
        }

    }

  public List<NumberRangeRec> getNumberControlList() {
    if(numberControlList == null || numberControlList.isEmpty()){
      numberControlList = setup.getNumerControlsAll();
    }
    
    return numberControlList;
  }

  public void setNumberControlList(ArrayList<NumberRangeRec> numberControlList) {
    this.numberControlList = numberControlList;
  }

 public List<NumberRangeRec> getNumberControlsFiltered() {
  return numberControlsFiltered;
 }

 public void setNumberControlsFiltered(List<NumberRangeRec> numberControlsFiltered) {
  this.numberControlsFiltered = numberControlsFiltered;
 }
 
 public List<ModuleRec> onModuleComplete(String descr){
  if(getModules() == null){
   return null;
  }
  if(StringUtils.isBlank(descr)){
   return getModules();
  }
  
  List<ModuleRec> retList = new ArrayList<>();
  for(ModuleRec curr:getModules()){
   if(StringUtils.startsWith(curr.getDescription(), descr)){
    retList.add(curr);
   }
  }
  if(retList.isEmpty()){
   return null;
  }else{
   return retList;
  }
 }
 
 public List<NumberRangeTypeRec> onNumRngTypeComplete(String code){
  LOGGER.log(INFO, "onNumRngTypeComplete called with {0}", code);
  if(getNumRangeTypeList() == null){
   LOGGER.log(INFO, "getNumRangeTypeList() returns {0}", getNumRangeTypeList());
   return null;
  }
  if(StringUtils.isBlank(code)){
   LOGGER.log(INFO, "Return all getNumRangeTypeList {0} ",getNumRangeTypeList());
   for(NumberRangeTypeRec curr:getNumRangeTypeList()){
    LOGGER.log(INFO, "id is {0}", curr.getId());
   }
   return getNumRangeTypeList();
  }
  
  List<NumberRangeTypeRec> retList = new ArrayList<>();
  for(NumberRangeTypeRec curr:getNumRangeTypeList()){
   if(StringUtils.startsWith(curr.getCode(), code)){
    retList.add(curr);
   }
  }
  if(retList.isEmpty()){
   return null;
  }else{
   return retList;
  }
 }
 
 public List<NumberRangeRec> onAddNumRngComplete(String nrCode){
  LOGGER.log(INFO, "Called onAddNumRng with {0} ", nrCode);
  if(numRange.getNumberRangeType() == null){
   MessageUtil.addClientWarnMessage("numRngCr:msgs", "numRangeTypeNone", "validationText");
   PrimeFaces.current().ajax().update("numRngCr:msgs");
   return null;
  }
  List<NumberRangeRec> nrRngList = sysBuff.getNumRangesOfType(numRange.getNumberRangeType());
  if(StringUtils.isBlank(nrCode)){
   return nrRngList;
  }
  List<NumberRangeRec> retList = new ArrayList<>();
  for(NumberRangeRec curr:nrRngList){
   if(StringUtils.startsWith( curr.getShortDescr(),nrCode)){
    retList.add(curr);
   }
  }
  return retList;
 }
 
 public void onAddSave(){
  LOGGER.log(INFO, "onAddSave called");
  // check to see that there is no other number range between start and end
  
  List<NumberRangeRec> numRngs = sysBuff.getNumRanges();
  if(!numRngs.isEmpty()){
   boolean found = false;
   for(NumberRangeRec curr:numRngs){
    if(this.numRange.getFromNum() >= curr.getFromNum() && this.numRange.getFromNum() <= curr.getToNum()){
     // start is with a range 
     MessageUtil.addClientErrorMessage("numRngCr:msgs", "numRngDupl", "errorText");
     PrimeFaces.current().ajax().update("numRngCr:msgs");
     return;
    }
    if(numRange.getToNum() >= curr.getFromNum() && numRange.getToNum() <= curr.getToNum()){
     MessageUtil.addClientErrorMessage("numRngCr:msgs", "numRngDupl", "errorText");
     PrimeFaces.current().ajax().update("numRngCr:msgs");
     return;
    }
   }
  }
  numRange.setCreatedBy(this.getLoggedInUser());
  numRange.setCreatedDate(new Date());
  numRange = sysBuff.updateNumberControl(numRange, getView());
  MessageUtil.addClientInfoMessage("numRngCr:msgs", "numRangeCr", "blacResponse");
  numRange = new NumberRangeRec();
  PrimeFaces.current().ajax().update("numRngCr");
  
 }
 
 public void onDeleteAction(){
   LOGGER.log(INFO,"Web deleteAction called for number control {0} ",selectNumberControl.getNumberControlId());
   boolean delOk = setup.deleteNumberControl(selectNumberControl, getView());
   if(delOk){
    boolean found = false;
    ListIterator<NumberRangeRec> numlist = numberControlList.listIterator();
    while(numlist.hasNext() && !found){
     NumberRangeRec numRec = numlist.next();
     if(Objects.equals(numRec.getNumberControlId(), selectNumberControl.getNumberControlId())){
      numlist.remove();
      found = true;
     }
    }
    MessageUtil.addInfoMessage("numRngDel", "blacResponse");
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("editList");
    pf.executeScript("PF('delRngConfWv').hide()");
   }else{
    MessageUtil.addErrorMessage("numRngDel", "errorText");
   }
   
   
  }

  public void onDeleteDlg(){
   LOGGER.log(INFO, "onDeleteDlg calld with selected {0}", selectNumberControl.getNumberControlId());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("delRngConfDlg");
   pf.executeScript("PF('delRngConfWv').show()");
  }
  
  public boolean onFilterByRngNum(Object value, Object filter, java.util.Locale loc) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

  public void updateAction(){
    LOGGER.log(INFO,"Web updateAction called");
  }

    public void moduleAction(){
        LOGGER.log(INFO,"Web get module with {0} ",this.modSelId);
        try{

            Long modId = new Long(this.modSelId);
            ModuleRec rec = this.setup.getModuleById(modId);
            this.numRange.setModule(rec);
            LOGGER.log(INFO, "Web module has been set to" , rec);
        }catch(BacException er){
          MessageUtil.addErrorMessage("numRngNf", "errorText");
        }

        LOGGER.log(INFO, "moduleAction module in num range {0}", this.numRange.getModule());
    }
    public void submit(){
        LOGGER.log(INFO, "Submit with action event");
        try{
         this.setup.createNumberControl(numRange, getView());
         String msgHdr = responseForKey("numRng");
         String msg = this.responseForKey("numRngCr") + numRange.getShortDescr();
         MessageUtil.addInfoMessageWithoutKey(msgHdr, msg);
         numRange = null;
         PrimeFaces pf = PrimeFaces.current();
         pf.ajax().update("valNumRngFrm");
            
        }catch(BacException er){
         MessageUtil.addErrorMessage("numRngCr", "errorText");
         
        }

    }

  public ArrayList<NumberControlBean> getNumberRanges(){
    LOGGER.log(INFO, "getNumberRanges called");
    

    return null;
  }

 public List<NumberRangeTypeRec> getNumRangeTypeList() {
  LOGGER.log(INFO, "getNumRangeTypeList called list is {0}", this);
  return numRangeTypeList;
 }

 public void setNumRangeTypeList(List<NumberRangeTypeRec> numRangeTypeList) {
  this.numRangeTypeList = numRangeTypeList;
 }

 public NumberRangeTypeRec getNumRangeTypeFiltered() {
  return numRangeTypeFiltered;
 }

 public void setNumRangeTypeFiltered(NumberRangeTypeRec numRangeTypeFiltered) {
  this.numRangeTypeFiltered = numRangeTypeFiltered;
 }

 
 public NumberRangeTypeRec getNumRangeTypeSelected() {
  return numRangeTypeSelected;
 }

 public void setNumRangeTypeSelected(NumberRangeTypeRec numRangeTypeSelected) {
  this.numRangeTypeSelected = numRangeTypeSelected;
 }
  
 
  

  public NumberRangeRec getSelectNumberControl() {
    if(selectNumberControl == null){
      selectNumberControl = new NumberRangeRec();
    }
    return selectNumberControl;
  }

  public boolean isUpdatesDisabled() {
    return updatesDisabled;
  }

  public void setUpdatesDisabled(boolean updatesDisabled) {
    this.updatesDisabled = updatesDisabled;
  }

  public void setSelectNumberControl(NumberRangeRec selectNumberControl) {
    LOGGER.log(INFO, "setSelectNumberControl called with {0}", selectNumberControl);
    this.selectNumberControl = selectNumberControl;
    
  }

  

  public void tblListener(SelectEvent evt){
    LOGGER.log(INFO, "Row select {0}", evt.getObject().getClass().getName());
    updatesDisabled = false;
  }

  public void onEditNumDlg(){
   LOGGER.log(INFO, "onEditNumDlg selectNumberControl  {0}", selectNumberControl);
   selectNumberControl = this.setup.getNumControlById(selectNumberControl.getNumberControlId());
   LOGGER.log(INFO, "onEditNumDlg selected item is: {0}",selectNumberControl.getShortDescr());
   LOGGER.log(INFO, "selectNumberControl id {0}",selectNumberControl.getNumberControlId());
   LOGGER.log(INFO, "long descr {0}",selectNumberControl.getLongDescr());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("edit");
   pf.executeScript("PF('editNumRngWVar').show()");
   
  }
  
  public void onEditNumTyDlg(){
   LOGGER.log(INFO,"onEditNumTyDlg called select ty is {0}" , numRangeTypeSelected);
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("edTyDlgFrm");
   pf.executeScript("PF('editNumRngTyWVar').show()");
  }
  
  public void onEditSaveAction(){
    LOGGER.log(INFO, "editSAveAction selected item is: {0}",selectNumberControl.getShortDescr());
    LOGGER.log(INFO, "getLoggedInUser() {0}", getLoggedInUser());
    selectNumberControl.setChangedBy(this.getLoggedInUser());
    selectNumberControl.setChangedDate(new Date());
    try{
    
    selectNumberControl = this.setup.updateNumberControl(selectNumberControl, getView());
    ListIterator<NumberRangeRec> nrLi = numberControlList.listIterator();
    boolean foundNr = false;
    while(nrLi.hasNext() && !foundNr){
     NumberRangeRec curr = nrLi.next();
     if(Objects.equals(curr.getNumberControlId(), selectNumberControl.getNumberControlId())){
      nrLi.set(selectNumberControl);
      foundNr = true;
     }
    }
    MessageUtil.addClientInfoMessage("listFrm:msgs", "numRangeUpdated", "blacResponse");
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("listFrm:numRngList");
    pf.ajax().update("listFrm:msgs");
    pf.executeScript("PF('editNumRngWVar').hide()");
    }catch(BacException ex){
     LOGGER.log(INFO, "Number Range update error {0}", ex.getLocalizedMessage());
     MessageUtil.addClientErrorMessage("edit:msgs", "numRngUpdt", "errorText");
     PrimeFaces.current().ajax().update("\"edit:msgs\"");
    } 
    
    
  }
  
  
  public void onNumRngeTyContext(SelectEvent evt){
   LOGGER.log(INFO, "onNumRngeTyContext {0}", evt.getObject());
   this.numRangeTypeSelected = (NumberRangeTypeRec)evt.getObject();
  }
  
  public void onEditTySaveAction(){
   LOGGER.log(INFO, "onEditTySaveAction called ");
   ListIterator<NumberRangeTypeRec> li = numRangeTypeList.listIterator();
   boolean found= false;
   while(li.hasNext() && !found){
    NumberRangeTypeRec curr = li.next();
    if(Objects.equals(curr.getId(), numRangeTypeSelected.getId())){
     found = true;
     numRangeTypeSelected.setChangedBy(this.getLoggedInUser());
     numRangeTypeSelected.setChangedDate(new Date());
     numRangeTypeSelected = sysBuff.updateNumberRngType(numRangeTypeSelected, getView());
     li.set(numRangeTypeSelected);
     LOGGER.log(INFO, "Updated list");
    }
   }
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("listFrm:numRngTyList");
   pf.executeScript("PF('editNumRngTyWVar').hide()");
   
  }
  
  public void onSaveBtnClick(){
   LOGGER.log(INFO, "onSaveBtnClick called");
   UserRec changeUsr = this.getLoggedInUser();
   Date changeDate = new Date();
   int changedControls = 0;
   if(changedNumCntrls != null){
   ListIterator<NumberRangeRec> li = changedNumCntrls.listIterator();
   while(li.hasNext()){
    NumberRangeRec numRec = li.next();
    numRec.setChangedBy(changeUsr);
    numRec.setChangedDate(changeDate);
    try{
     numRec = setup.updateNumberControl(numRec, this.getView());
     MessageUtil.addInfoMessageVar1("numRangeUpdated", "blacReposnes", numRec.getShortDescr());
     int selectedInx = numberControlList.indexOf(numRec);
     numberControlList.set(selectedInx, numRec);
    }catch(BacException ex){
     LOGGER.log(INFO, "Number range update error {0}", ex.getLocalizedMessage());
     MessageUtil.addErrorMessageParam1("numRngUpdt", "errorText", numRec.getShortDescr());
    }
    
   }
   
   if(changedControls > 0){
    String msg = this.getResponseMessage().getString("numRangeUpdated") + changedControls;
    GenUtil.addInfoMessage(msg);
   }else{
    String msg = this.getResponseMessage().getString("numRangeUpdatedNone");
    GenUtil.addInfoMessage(msg);
   }
   numberControlList = null;
   }
   // now check for deletions
   if(deletedNumCntrls != null){
   changedControls = 0;
   ListIterator<NumberRangeRec> lid = deletedNumCntrls.listIterator();
   while(lid.hasNext()){
    NumberRangeRec numRec = lid.next();
    numRec.setChangedBy(changeUsr);
    numRec.setChangedDate(changeDate);
    boolean deleted = setup.deleteNumberControl(numRec, this.getView());
    if(deleted){
     changedControls++;
    
    }
    
   }
   if(changedControls > 0){
    String msg = this.getResponseMessage().getString("numRangeDeleted") + changedControls;
    GenUtil.addInfoMessage(msg);
   }else{
    String msg = this.getErrorMessage().getString("numCntrlNoDelete");
    GenUtil.addErrorMessage(msg);
   }
   deletedNumCntrls = null;
   }
   
  }
}
