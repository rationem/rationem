/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;

import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.mdm.CountryRec;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.buffer.AppBuffer;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.UserDM;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;



/**
 *
 * @author Chris
 */
public class BaseBean implements Serializable {
  private static final Logger LOGGER = Logger.getLogger(BaseBean.class.getSimpleName());

@EJB
private UserDM usrDM;

@EJB
private MasterDataManager mastDataMgr;
        
  ResourceBundle formText = ResourceBundle.getBundle("com.rationem.localisation.formText");
  ResourceBundle formTextAp = ResourceBundle.getBundle("com.rationem.localisation.formTextAp");
  ResourceBundle formHelpText = ResourceBundle.getBundle("com.rationem.localisation.formHelpText");
  ResourceBundle errorMessage = ResourceBundle.getBundle("com.rationem.localisation.errorText");
  ResourceBundle formFieldName = ResourceBundle.getBundle("com.rationem.localisation.FieldNames");
  ResourceBundle validationText = ResourceBundle.getBundle("com.rationem.localisation.ValidationText");
  ResourceBundle responseMessage = ResourceBundle.getBundle("com.rationem.localisation.responses");
  ResourceBundle pageTitle = ResourceBundle.getBundle("com.rationem.localisation.pageTitle");
  ResourceBundle listHeadeing = ResourceBundle.getBundle("com.rationem.localisation.listHeading");
  
  
  @EJB
  private SysBuffer sysBuff;
  
  @EJB
  private AppBuffer appBuff;
  
  private UserSessionBean usrBuff;
  private UserRec user;
  private int completeResultSize;
  private SelectItem selectNone;
  private String viewSimple;
  private String view;
  private Date maxDate;
  private FiscalPeriodYearRec fisPerYr;
  private List<AuditBaseRec> auditList;
  private int step = 0;
  private String stepName;
  private AppBuffer tenentBuff;
  
  
  private List<CompanyBasicRec> compList;

  public BaseBean()  {
 /*FacesContext context = FacesContext.getCurrentInstance();
 user = (UserRec)context.getExternalContext().getSessionMap().get("userRec");
 HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
 
 
 
 LOGGER.log(INFO, "BaseBean Constructor principal: {0}", request.getUserPrincipal());
      if(user == null){        try{
          request.login(null, null);
        }catch(ServletException e){
          GenUtil.addErrorMessage("Login error");
        }

      }
      */
  }
  
  @PostConstruct
  private void init(){
   LOGGER.log(INFO, "Basebean post construct");
   FacesContext ctx = FacesContext.getCurrentInstance();
   Object usrBuf = ctx.getExternalContext().getSessionMap().get("userBuff");
   
   String tenantId = (String)ctx.getExternalContext().getSessionMap().get("tenantId");
   if(tenantId ==  null){
    ctx.getExternalContext().getSessionMap().put("tenantId", "t1");   
   }
  
   
   LOGGER.log(INFO, "From session usr {0}", new Object[]{usrBuf});
   if(usrBuf != null){
    this.usrBuff = (UserSessionBean)usrBuf;
    this.user = usrBuff.getUsr();
    if(user == null){
     Long userid = Long.valueOf(String.valueOf(ctx.getExternalContext().getSessionMap().get("userId")));
     tenantId = String.valueOf(ctx.getExternalContext().getSessionMap().get("tenantId"));
     user = this.usrDM.getUserByIdNum(userid, tenantId);
     LOGGER.log(INFO, "user from DM {0}", user);
     
    }
    
   }
   LOGGER.log(INFO, "this.user {0}", this.user);
   this.getView();
   this.getViewSimple();
  }

 public List<AuditBaseRec> getAuditList() {
  return auditList;
 }

 public void setAuditList(List<AuditBaseRec> auditList) {
  
  
  if(auditList == null || auditList.isEmpty()){
   return ;
  }
  ListIterator<AuditBaseRec> audLi = auditList.listIterator();
  while(audLi.hasNext()){
   AuditBaseRec curr = audLi.next();
   if(!StringUtils.isBlank(curr.getFieldName())){
    String name = fieldNameForKey(curr.getFieldName());
    curr.setFieldNameTranslated(name);
    audLi.set(curr);
   }
  }
  this.auditList = auditList;
 }
  
 

 public SelectItem getSelectNone() {
  if(selectNone == null){
   selectNone = new SelectItem();
   String val = this.formTextForKey("select");
   selectNone.setLabel(val);
   selectNone.setNoSelectionOption(true);
  }
  return selectNone;
 }
 
 public String getSortTextGl(SortOrderRec s,DocFiRec doc, ApAccountRec apAcnt, ArAccountRec arAcnt ){
  
  String sText = null;
  int style = DateFormat.SHORT;
  DateFormat df = DateFormat.getDateInstance(style, doc.getCompany().getLocale());
  
  switch(s.getSortCode()){
   case "DocDt":
    sText = df.format(doc.getDocumentDate());
    break;
   case "entryDt":
    sText = df.format(doc.getCreateOn());
    break;
   case "postDt":
    sText = df.format(doc.getPostingDate());
    break;
   case "taxDtDt":
    sText = df.format(doc.getTaxDate());
    break;
   case "extDocRef":
    sText = doc.getPartnerRef();
    break;
   case "ArAcNum":
    if(arAcnt == null){
     sText = df.format(doc.getDocumentDate());
    } else{
     sText = arAcnt.getArAccountCode();
    }
   break;
   case "ApAcNum":
    if(arAcnt == null){
     sText = df.format(doc.getDocumentDate());
    } else{
     sText = apAcnt.getAccountCode();
    }
    break;
  }
  
  return sText;
 }

 public void setSelectNone(SelectItem selectNone) {
  this.selectNone = selectNone;
 }

 public int getStep() {
  return step;
 }

 public void setStep(int step) {
  this.step = step;
 }

 public String getStepName() {
  return stepName;
 }

 public void setStepName(String stepName) {
  this.stepName = stepName;
 }

 
 /**
  * @return the tenentBuff
  */
 public AppBuffer getTenentBuff() {
  tenentBuff = appBuff;
  return tenentBuff;
 }

 /**
  * @param tenentBuff the tenentBuff to set
  */
 public void setTenentBuff(AppBuffer tenentBuff) {
  this.tenentBuff = tenentBuff;
 }

 public ResourceBundle getFormTextAp() {
  return formTextAp;
 }

 public void setFormTextAp(ResourceBundle formTextAp) {
  this.formTextAp = formTextAp;
 }

 public ResourceBundle getValidationText() {
  return validationText;
 }

 public void setValidationText(ResourceBundle validationText) {
  this.validationText = validationText;
 }

 public ResourceBundle getPageTitle() {
  return pageTitle;
 }

 public void setPageTitle(ResourceBundle pageTitle) {
  this.pageTitle = pageTitle;
 }
  
 

/**
 * Used to get the page ID
 * @return VIEW_ID
 */
public String getView(){
 if(StringUtils.isBlank(view)){
  FacesContext ctx = FacesContext.getCurrentInstance();
 view = ctx.getViewRoot().getViewId();
 }
 
return view;
/*
 * FacesContext ctx = FacesContext.getCurrentInstance();
HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
// returns something like "/myapplication/home.faces"
String fullURI = servletRequest.getRequestURI();
 */
}

  public ResourceBundle getErrorMessage() {
    if(errorMessage == null){
      errorMessage = ResourceBundle.getBundle("com.rationem.localisation.ErrotText");
    }
    return errorMessage;
  }

  public void setErrorMessage(ResourceBundle errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ResourceBundle getFormHelpText() {
    if(formHelpText == null){
      formHelpText = ResourceBundle.getBundle("localisedMessages.FormHelpText");
    }
    return formHelpText;
  }

  
  public void setFormHelpText(ResourceBundle formHelpText) {
    this.formHelpText = formHelpText;
  }

  public ResourceBundle getFormText() {
    if(formText == null){
      formText = ResourceBundle.getBundle("localisedMessages.FormTexts");
    }
    return formText;
  }

  public void setFormText(ResourceBundle formText) {
    this.formText = formText;
  }

 public ResourceBundle getFormFieldName() {
  if(formFieldName == null){
   formFieldName = ResourceBundle.getBundle("om.rationem.localisation.FieldNames");
  }
  return formFieldName;
 }

 public void setFormFieldName(ResourceBundle formFieldName) {
  this.formFieldName = formFieldName;
 }

  public ResourceBundle getvalidationText() {
    if(validationText == null ){
      validationText = ResourceBundle.getBundle("localisedMessages.ValidationText");

    }
    return validationText;
  }

  public void setvalidationText(ResourceBundle validationText) {
    this.validationText = validationText;
  }

 public Date getMaxDate() {
  if(maxDate == null){
   maxDate = sysBuff.getMaxDate();
  }
  return maxDate;
 }

 public void setMaxDate(Date maxDate) {
  this.maxDate = maxDate;
 }

 
 public ResourceBundle getResponseMessage() {
  return responseMessage;
 }

 public void setResponseMessage(ResourceBundle responseMessage) {
  this.responseMessage = responseMessage;
 }

 public String responseForKey(String key){
  String internationalisedResp = responseMessage.getString(key);
  return internationalisedResp;
 }
 
 public String errorForKey(String key){
  String err = this.getErrorMessage().getString(key);
  return err;
 }
 
 public String validationForKey(String key){
  String val = getvalidationText().getString(key);
  return val;
 }
 
 public String formTextForKey(String key){
  String val = formText.getString(key);
  return val;
 }
 
 public String formTextApForKey(String key){
  String val = formTextAp.getString(key);
  return val;
 }
 
 public String fieldNameForKey(String key){
  LOGGER.log(INFO, "fieldNameForKey called with key {0} resourceBundle {1}", new Object[]{key,formFieldName});
  String val = formFieldName.getString(key);
  return val;
 }
 
  public UserRec getUser() throws ServletException {
   LOGGER.log(INFO, "getUser() user {0}", user);
   
    if(user == null){
      FacesContext context = FacesContext.getCurrentInstance();
      String userName = context.getExternalContext().getRemoteUser();
      LOGGER.log(INFO, "getUser remote user {0}", userName);
      user = this.usrDM.getUserByLoginName(userName);
      LOGGER.log(INFO, "getUser user from remote {0}",user.getId());
      //user = (UserRec)context.getExternalContext().getSessionMap().get("userRec");
      
      LOGGER.log(INFO,"user from sessionMap {0} with id {1}", new Object[]{user,user.getId()});
      context.getExternalContext().getSessionMap().put("userRec", user);
    }
    
    return user;
  }

  public void setUser(UserRec user) {
    this.user = user;
  }

  public UserRec getLoggedInUser(){
    LOGGER.log(INFO, "getLoggedInUser user is {0} ",user);
    
    if(user == null){
     
     user = usrBuff.getUsr();
     LOGGER.log(INFO, "user from usrBuff {0}", user);
     FacesContext fc = FacesContext.getCurrentInstance();
     String userName = fc.getExternalContext().getUserPrincipal().getName();
     LOGGER.log(INFO, "getLoggedInUser userPrincipalName {0}", userName);
     user = this.usrDM.getUserByLoginName(userName);
     CountryRec cntry = this.mastDataMgr.getCountryByUsr(user);
     user.setCountry(cntry);
     usrBuff.setUsr(user);
     LOGGER.log(INFO, "User from usrBuff {0}", usrBuff.getUsr());
     Object testUsr = fc.getExternalContext().getSessionMap().get("usrRec");
     
     LOGGER.log(INFO, "testUsr from buffer {0}", testUsr);
     
     
    }
    LOGGER.log(INFO, "getLoggedInUser country {0}",user.getCountry());
    if(user.getCountry() == null){
     CountryRec cntry = this.mastDataMgr.getCountryByUsr(user);
     user.setCountry(cntry);
     usrBuff.setUsr(user);
    }
    LOGGER.log(INFO, "getLoggedInUser return user {0} id {1}",new Object[]{user,user.getId()});

    return user;
  }

public void postProcessXLS(Object document) {
  //HSSFWorkbook wb = (HSSFWorkbook) document;
  LOGGER.log(INFO, "Base Bean postProcess XLS");
  HSSFWorkbook wb = (HSSFWorkbook)document;
  HSSFSheet sh = wb.getSheetAt(0);
  LOGGER.log(INFO, "sh {0}", sh);
//  Workbook workbook = (HSSFWorkbook) document;
//  Sheet sheet = workbook.getSheetAt(0);
  //HSSFSheet sheet = wb.getSheetAt(0);
  //Row header = sheet.getRow(0);
  
  // create fonts
  HSSFFont font = wb.createFont();        
  font.setColor(IndexedColors.BLACK.index);
  font.setBold(false);//setBoldweight(HSSFFont.DEFAULT_CHARSET);
 
  HSSFFont headerFont = wb.createFont();
  headerFont.setColor(IndexedColors.BLUE.index);
  headerFont.setBold(true);
        //headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
  // create style
  
  HSSFCellStyle cellStyle= wb.createCellStyle();
  cellStyle.setFont(font);
  cellStyle.setFillBackgroundColor(IndexedColors.AQUA.index);
 
  HSSFCellStyle cellHeaderStyle= wb.createCellStyle();
  cellHeaderStyle.setFont(headerFont);
  cellHeaderStyle.setFillBackgroundColor(IndexedColors.AQUA.index);
  HSSFRow header = sh.getRow(0);
  LOGGER.log(INFO, "Header  {0}", header);
  LOGGER.log(INFO, "Header cells {0}", header.getPhysicalNumberOfCells());
 // HSSFCellStyle cellStyle = wb.createCellStyle();
  /*CellStyle backgroundStyle = workbook.createCellStyle();
  backgroundStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
  
  backgroundStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
  backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); */
  
  //CellStyle cellStyle = workbook.createCellStyle(); old
  //cellStyle.setFillBackgroundColor(IndexedColors.PALE_BLUE.getIndex());
  
  
  
  //cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  //cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
  //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
  for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
    //header.getCell(i).setCellStyle(cellStyle);
    header.getCell(i).setCellStyle(cellHeaderStyle);
  }
  int numCols = header.getLastCellNum();
  for(int i=0; i < numCols;i++){
   //sheet.autoSizeColumn(i);
   sh.autoSizeColumn(i);
  }
  int lastRowNum = sh.getLastRowNum();
  //int lastRowNum = sheet.getLastRowNum();
  //Iterator<Row> rowIterator = sheet.iterator();
  Iterator<Row> rowIterator = sh.iterator();
  while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    if(row.getRowNum() == 0){
     continue;
    }
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      cell.setCellStyle(cellStyle);
      if(cell.getCellTypeEnum() == CellType.STRING){
       String strVal = cell.getStringCellValue();
       boolean numbericData = GenUtil.isNumeric(strVal);
       if(numbericData){
        strVal = strVal.replace(",", "");
        cell.setCellValue(Double.parseDouble(strVal));
       }
       
      }
      if(cell.getCellTypeEnum() == CellType.NUMERIC){
       CellStyle currCellStyle = cell.getCellStyle();
       currCellStyle.setAlignment(HorizontalAlignment.RIGHT);
       cell.setCellStyle(currCellStyle);
      }
    }
  }

}

 public int getCompleteResultSize() {
  if(completeResultSize == 0){
   completeResultSize = sysBuff.getMaxComplSize();
  }
  return completeResultSize;
 }

 public void setCompleteResultSize(int completeResultSize) {
  this.completeResultSize = completeResultSize;
 }

 public List<CompanyBasicRec> getCompList() {
  if(compList == null){
   LOGGER.log(INFO, "Base bean getCompList()");
   compList = sysBuff.getCompanies();
   LOGGER.log(INFO, "Companies returned from sys buffer {0}",compList);
  }
  return compList;
 }

 public void setCompList(List<CompanyBasicRec> compList) {
  this.compList = compList;
 }

 public Date getDueDateFromPayTerm(PaymentTermsRec terms, Date docDate, Date postDate){
  
  Date dueDate;
  Calendar cal = Calendar.getInstance();
  if(terms.getBaseType().equals("docDT")){
   cal.setTime(docDate);
  }else{
   cal.setTime(postDate);
  }
  int offset = terms.getDays();
  if(StringUtils.equalsIgnoreCase(terms.getUom().getUomCode(), "DY")){
   // Offset is in days
   cal.add(Calendar.DATE, offset);
  }else if(StringUtils.equalsIgnoreCase(terms.getUom().getUomCode(), "MTH")){
   // Offset is Months
   cal.add(Calendar.MONTH, offset);
  }
  
  if(terms.getDayOfMonth() > 0 ){
   // day of month entered
   int dom = cal.get(Calendar.DAY_OF_MONTH);
   if(dom < terms.getDayOfMonth()){
    cal.set(Calendar.DAY_OF_MONTH, dom);
   }else if (dom > terms.getDayOfMonth()){
    cal.set(Calendar.DAY_OF_MONTH, dom);
    cal.add(Calendar.MONTH, 1);
   }
  }
  LOGGER.log(INFO, "Final Cal date {0}", cal.getTime());
  dueDate = cal.getTime();
  return dueDate;
 }
 public String getViewSimple() {
  LOGGER.log(INFO, "getCurrView");
  if(StringUtils.isBlank(viewSimple)){
   int startIdx = view.lastIndexOf('/');
   int endIdx = view.length();
   LOGGER.log(INFO, "view {0} start of page name {1} size {2}", new Object[]{view,startIdx,view.length()});
   viewSimple = view.substring(startIdx + 1, endIdx - 6);
  }
  
  return viewSimple;
 }

 public Map<String,Object> getSessionMap(){
  Map<String,Object> sessMap;
  sessMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
  return sessMap;
 }
 public FiscalPeriodYearRec getFisPerYr() {
  return fisPerYr;
 }
 
 public FiscalPeriodYearRec getFisPerYr(CompanyBasicRec comp, Date postDate) {
  if(fisPerYr == null){
   fisPerYr = sysBuff.getCompFiscalPeriodYearForDate(comp, 
           postDate);
  }
  return fisPerYr;
 }

 public void setFisPerYr(FiscalPeriodYearRec fisPerYr) {
  this.fisPerYr = fisPerYr;
 }

 
 public void setViewSimple(String viewSimple) {
  this.viewSimple = viewSimple;
 }

 public UserSessionBean getUsrBuff() {
  if(usrBuff == null){
   //attempt to get from session
   FacesContext context = FacesContext.getCurrentInstance();
   usrBuff = (UserSessionBean)context.getExternalContext().getSessionMap().get("userBuff");
   LOGGER.log(INFO, "Base bean get userbuff from session {0}", usrBuff);
   if(usrBuff == null){
    // no user buffer on session
    usrBuff = new UserSessionBean();
    String usename = context.getExternalContext().getRemoteUser();
    LOGGER.log(INFO, "Remote user name {0}", usename);
    UserRec usr = usrDM.getUserByLoginName(usename);
    LOGGER.log(INFO, "UserRec from userDM {0} ", new Object[]{
   usrBuff.getUsr()});
    usrBuff.setUsr(usr);
    context.getExternalContext().getSessionMap().put("usrBuff", usrBuff);
   }
  }
  
  return usrBuff;
 }

 public void setUsrBuff(UserSessionBean usrBuff) {
  this.usrBuff = usrBuff;
 }

public List<CompanyBasicRec> onCompComplete(String input){
 
 if(StringUtils.isBlank(input)){
  return getCompList();
 }else{
  List<CompanyBasicRec> retLst = new ArrayList<>();
  for(CompanyBasicRec curr: getCompList() ){
   if(StringUtils.startsWith(curr.getReference(), input)){
    retLst.add(curr);
   }
  }
  return retLst;
 }
}
 
public void validateDate(FacesContext c, UIComponent comp, Object val){
 LOGGER.log(INFO, "validateDate sent with {0}", val);
 

}


}
