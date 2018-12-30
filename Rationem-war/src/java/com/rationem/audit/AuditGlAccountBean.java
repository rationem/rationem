/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.audit;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
        
import com.rationem.busRec.audit.AuditGlAccountRec;
import java.util.ResourceBundle;
import com.rationem.util.BaseBean;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.common.SysBuffer;
import javax.ejb.EJB;
import java.io.Serializable;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.AuditManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
/**
 *
 * @author Chris
 */
public class AuditGlAccountBean extends BaseBean implements Serializable {
  /**
  * 
  */
 private static final long serialVersionUID = 1L;


  private static final Logger logger =
            Logger.getLogger("accounts.audit.AuditGlAccountBean");


  @EJB
  private SysBuffer sysBuffer;

  @EJB
  private GlAccountManager glActMgr;

  @EJB
  private AuditManager auditMgr;

  private Date fromDate;
  private Date toDate;
  private UserRec changedByUser;
  private ArrayList<SelectItem> auditTypes;
  private int selectedAuditType;
  private String userName;
  private ArrayList<AuditGlAccountRec> coaActChanges;

  private ResourceBundle formText = this.getFormText();
  private ResourceBundle formHelpText = this.getFormHelpText();

  //page status
  private boolean retrievedCoaRecs = false;



    /** Creates a new instance of AuditGlAccountBean */
    public AuditGlAccountBean() {
      logger.log(INFO, "AuditGlAccountBean bean called");
    }

  public boolean isRetrievedCoaRecs() {
    return retrievedCoaRecs;
  }

  public void setRetrievedCoaRecs(boolean retrievedRecords) {
    this.retrievedCoaRecs = retrievedRecords;
  }



  public ArrayList<SelectItem> getAuditTypes() {
    logger.log(INFO, "getAuditTypes {0}",auditTypes);
    if(auditTypes == null){
      auditTypes = new ArrayList<SelectItem>();
      SelectItem item = new SelectItem();
      item.setValue("1");
      item.setLabel(formText.getString("glAccountCoaAccount"));
      auditTypes.add(item);
      item = new SelectItem();
      item.setValue("2");
      item.setLabel(formText.getString("glAccountCompAccount"));
      auditTypes.add(item);
    }
    logger.log(INFO, "getAuditTypes {0}",auditTypes);
    return auditTypes;
  }

  public void setAuditTypes(ArrayList<SelectItem> auditTypes) {
    this.auditTypes = auditTypes;
  }

  public UserRec getChangedByUser() {
    return changedByUser;
  }

  public void setChangedByUser(UserRec changedByUser) {
    this.changedByUser = changedByUser;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    logger.log(INFO, "From date set to {0}", fromDate);
    this.fromDate = fromDate;
  }

  public int getSelectedAuditType() {
    return selectedAuditType;
  }

  public void setSelectedAuditType(int selectedAuditType) {
    logger.log(INFO, "setSelectedAuditType is now {0}", selectedAuditType);
    this.selectedAuditType = selectedAuditType;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }



  public void onAuditTypeChange(ValueChangeEvent evt){
    logger.log(INFO, "onAuditTypeChange");
  }

  public void auditTypeChangeAction(){
    logger.log(INFO, "Audit type change action");
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

public void onSearchAuditRecords(){
  logger.log(INFO, "Search audit records");
  if(this.selectedAuditType == 1){
    logger.log(INFO, "Get coa account changes type is: {0}", selectedAuditType);
    coaActChanges = (ArrayList<AuditGlAccountRec>)auditMgr.getGlCoaAccountChanges(null, fromDate, toDate, changedByUser);
    ListIterator<AuditGlAccountRec> li = coaActChanges.listIterator();
    while(li.hasNext()){
     AuditGlAccountRec rec = li.next();
     logger.log(INFO, "field name {0}", rec.getFieldName());
     String fn = fieldNameForKey(rec.getFieldName());
     rec.setFieldName(fn);
     li.set(rec);
    }
    logger.log(INFO, "Returned from DB: {0}", coaActChanges);
    this.retrievedCoaRecs = true;
  } else if(selectedAuditType == 2){
    logger.log(INFO, "Company account changes");
  }
  
}

  public ArrayList<AuditGlAccountRec> getCoaActChanges() {

    return coaActChanges;
  }

  public void setCoaActChanges(ArrayList<AuditGlAccountRec> coaActChanges) {
    this.coaActChanges = coaActChanges;
  }

public void postProcessXLS(Object document) {
  HSSFWorkbook wb = (HSSFWorkbook) document;
  HSSFSheet sheet = wb.getSheetAt(0);
  HSSFRow header = sheet.getRow(0);
  HSSFCellStyle cellStyle = wb.createCellStyle();
  cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
  cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  
  for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
    header.getCell(i).setCellStyle(cellStyle);
  }
}

}
