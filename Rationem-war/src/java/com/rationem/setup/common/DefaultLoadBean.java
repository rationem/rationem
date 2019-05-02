/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.setup.common;

import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.ChequeVoidReasonRec;
import com.rationem.busRec.config.common.ContactRoleRec;
import com.rationem.busRec.config.common.DocReversalReasonRec;
import com.rationem.busRec.config.common.FundCategoryRec;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.common.LocaleCodeRec;
import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.common.NumberRangeTypeRec;
import com.rationem.busRec.config.common.ProcessCodeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.CalendarRuleBaseRec;
import com.rationem.busRec.config.company.CalendarRuleFixedDateRec;
import com.rationem.busRec.config.company.CalendarRuleFlexPerRec;
import com.rationem.busRec.config.company.CalendarRuleFlexYearRec;
import com.rationem.busRec.config.company.CalendarRuleMonthRec;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.fi.company.CompanyApArRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPlAccountRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.partner.PartnerRoleRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.ejbBean.common.MasterDataManager;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.ejbBean.config.common.VatManager;
import com.rationem.ejbBean.config.company.CompanyManager;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.exception.BacException;
import com.rationem.util.BaseBean;
import com.rationem.util.MessageUtil;
import com.rationem.util.helper.ConfigurationByOrder;
import com.rationem.util.helper.DefaultConfigSetting;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;



import java.util.logging.Level;

import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces.Ajax;
//import org.apache.poi.hssf.record.RecordFormatException;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;


/**
 *
 * @author chris_000
 */
public class DefaultLoadBean extends BaseBean{

 /**
  * Creates a new instance of DefaultLoadBean
  */
 private InputStream is;
 //private HSSFWorkbook workbook;
 private Workbook workbook;
 private Sheet sheet1;
 //private HSSFSheet sheet1;
 private int stepNum = 0;
 private int steps = 0;
 private int progress;
 private boolean showProgress;
 
 
 private static final String BASE_DIR = "/resources/defaultData/";
 private static final Logger LOGGER = Logger.getLogger(DefaultLoadBean.class.getName());
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private MasterDataManager mdm;

 @EJB
 private BasicSetup setup;
 
 @EJB
 private GlAccountManager glAcntMgr;
 
 @EJB
 private CompanyManager compMgr;
 
 @EJB
 private BankManager bankMgr;
 
 @EJB
 private CostCentreMgr costCentMgr;
 
 @EJB
 private ProgrammeMgr progMgr;
 
 @EJB
 private BasicSetup basicConfigMgr;
 
 @EJB
 private VatManager vatMgr;
 

 
 private DualListModel<DefaultConfigSetting> defaults;
 
 
 
 
 private List<ModuleRec> modules;
 private List<CountryRec> countries;
 
 
 public DefaultLoadBean() {
 }
 
 
 @PostConstruct
 private void init(){
  modules = sysBuff.getModules();
  showProgress = false;
  this.stepNum = 0;
  this.steps = 0;
  List<DefaultConfigSetting> defConfigSource;
  List<DefaultConfigSetting> defConfigTarget;
  defConfigSource = new ArrayList<>();
  defConfigTarget = new ArrayList<>();
  
  // add Defaults
  
  String cntry = formTextForKey("cfgDefCntry");
  DefaultConfigSetting defCntry = new DefaultConfigSetting(cntry,"cntry",1);
  defConfigSource.add(defCntry);
  
  String uom = this.formTextForKey("cfgDefUom");
  DefaultConfigSetting defUom = new DefaultConfigSetting(uom,"uom",3);
  defConfigSource.add(defUom);
  
  String curr = formTextForKey("cfgDefCurr");
  DefaultConfigSetting defCurr = new DefaultConfigSetting(curr,"crncy",2);
  defConfigSource.add(defCurr);
  
  
  String loc = formTextForKey("cfgDefLoc");
  DefaultConfigSetting defLoc = new DefaultConfigSetting(loc,"loc",4);
  defConfigSource.add(defLoc);
  
  String chqVoidRsn = formTextForKey("cfgDefChqVoidRsn");
  DefaultConfigSetting defchqVoidRsn = new DefaultConfigSetting(chqVoidRsn,"chqVoidRsn",4);
  defConfigSource.add(defchqVoidRsn);

  String docRevRsn = formTextForKey("cfgDefDocRev");
  DefaultConfigSetting defdocRevRsn = new DefaultConfigSetting(docRevRsn,"docRevRsn",4);
  defConfigSource.add(defdocRevRsn);
  
  String mod = formTextForKey("cfgDefMod");
  DefaultConfigSetting defMod = new DefaultConfigSetting(mod,"mod",5);
  defConfigSource.add(defMod);
  
  String procCode = formTextForKey("cfgDefPrCd");
  DefaultConfigSetting defPrCd = new DefaultConfigSetting(procCode,"procCode",6);
  defConfigSource.add(defPrCd);
  
  String numRngTy = formTextForKey("cfgDefNumRngTy");
  DefaultConfigSetting defnumRngTy = new DefaultConfigSetting(numRngTy,"numRngTy",7);
  defConfigSource.add(defnumRngTy);
  
  String numRng = formTextForKey("cfgDefNumRng");
  DefaultConfigSetting defnumRng = new DefaultConfigSetting(numRng,"numRng",8);
  defConfigSource.add(defnumRng);
  
  
  
  String lnTy = formTextForKey("cfgDefLnTy");
  DefaultConfigSetting defLnTy = new DefaultConfigSetting(lnTy,"lnTy",8);
  defConfigSource.add(defLnTy);
  
  String docTy = formTextForKey("cfgDefDt");
  DefaultConfigSetting defDocTy = new DefaultConfigSetting(docTy,"docTy",9);
  defConfigSource.add(defDocTy);
  
  String tranTy = formTextForKey("cfgDefTranTy");
  DefaultConfigSetting defTranTy = new DefaultConfigSetting(tranTy,"tranTy",10);
  defConfigSource.add(defTranTy);
  
  String sortOrd = formTextForKey("cfgDefSortOrd");
  DefaultConfigSetting defSortOrd = new DefaultConfigSetting(sortOrd,"sortOrd",11);
  defConfigSource.add(defSortOrd);
  
  String led = formTextForKey("cfgDefLed");
  DefaultConfigSetting defled = new DefaultConfigSetting(led,"led",12);
  defConfigSource.add(defled);
  
  String fndCat = formTextForKey("cfgDefFndCat");
  DefaultConfigSetting defFndCat =  new DefaultConfigSetting(fndCat,"fndCat",13);
  defConfigSource.add(defFndCat);
  
  String acntTy = formTextForKey("cfgDefAcntTy");
  DefaultConfigSetting defacntTy = new DefaultConfigSetting(acntTy,"acntTy",14);
  defConfigSource.add(defacntTy);
  
  String calNatural = formTextForKey("cfgDefcalNatural");
  DefaultConfigSetting defcalNatural = new DefaultConfigSetting(calNatural,"calNatural",15);
  defConfigSource.add(defcalNatural);
  
  String calFxDay = formTextForKey("cfgDefCalFxDay");
  DefaultConfigSetting defcalFxDay = new DefaultConfigSetting(calFxDay,"calFxDay",15);
  defConfigSource.add(defcalFxDay);
  
  String calMonth = formTextForKey("cfgDefCalMonth");
  DefaultConfigSetting defcalMonth = new DefaultConfigSetting(calMonth,"calMonth",16);
  defConfigSource.add(defcalMonth);
  
  String calFlexYr = formTextForKey("cfgDefCalFexYr");
  DefaultConfigSetting defcalFlexYr = new DefaultConfigSetting(calFlexYr,"calFlexYr",17);
  defConfigSource.add(defcalFlexYr);
  
  
  String calFlexPer = formTextForKey("cfgDefCalFexPer");
  DefaultConfigSetting defcalFlexPer = new DefaultConfigSetting(calFlexPer,"calFlexPer",18);
  defConfigSource.add(defcalFlexPer);
  
  String fiscPer = formTextForKey("cfgDefFiscPer");
  DefaultConfigSetting defcalFiscPer = new DefaultConfigSetting(fiscPer,"fiscPer",19);
  defConfigSource.add(defcalFiscPer);
  
  String coa = formTextForKey("cfgDefCoa");
  DefaultConfigSetting defCoa = new DefaultConfigSetting(coa,"coa",20);
  defConfigSource.add(defCoa);
  
  String pstTy = formTextForKey("cfgDefPstTy");
  DefaultConfigSetting defPstTy = new DefaultConfigSetting(pstTy,"pstTy",21);
  defConfigSource.add(defPstTy);
  
  String glAcntCh = formTextForKey("cfgDefGlAcntCh");
  DefaultConfigSetting defGlAcntCh = new DefaultConfigSetting(glAcntCh,"glAcntCh",22);
  defConfigSource.add(defGlAcntCh);
  
  String contRole = formTextForKey("cfgDefContRole");
  DefaultConfigSetting defContRole = new DefaultConfigSetting(contRole,"contRole",22);
  defConfigSource.add(defContRole);
  
  String comp = formTextForKey("cfgDefCompany");
  DefaultConfigSetting defcomp = new DefaultConfigSetting(comp,"comp",23);
  defConfigSource.add(defcomp);
  
   String compApAr = formTextForKey("cfgDefCompApAr");
  DefaultConfigSetting defcompApAr = new DefaultConfigSetting(compApAr,"compApAr",23);
  defConfigSource.add(defcompApAr);
  
  String payTy = formTextForKey("cfgDefPayTy");
  DefaultConfigSetting defPayTy = new DefaultConfigSetting(payTy,"payTy",24);
  defConfigSource.add(defPayTy);
  
  String payTerms = formTextForKey("cfgDefPayTerms");
  DefaultConfigSetting defPayTerms = new DefaultConfigSetting(payTerms,"payTerms",24);
  defConfigSource.add(defPayTerms);
  
  String postPer = formTextForKey("cfgDefCompPostPer");
  DefaultConfigSetting defPostPer = new DefaultConfigSetting(postPer,"postPer",25);
  defConfigSource.add(defPostPer);
  
  String bank = formTextForKey("cfgDefBank");
  DefaultConfigSetting defBank = new DefaultConfigSetting(bank,"bank",26);
  defConfigSource.add(defBank);
  
  String bankBr = formTextForKey("cfgDefBankBranch");
  DefaultConfigSetting defBankBr = new DefaultConfigSetting(bankBr,"bankBr",26);
  defConfigSource.add(defBankBr);
  
  String bankAc = formTextForKey("cfgDefBankAccount");
  DefaultConfigSetting defBankAc = new DefaultConfigSetting(bankAc,"bankAc",27);
  defConfigSource.add(defBankAc);
  
  String bankAcComp = formTextForKey("cfgDefBankAccountComp");
  DefaultConfigSetting defBankAcComp = new DefaultConfigSetting(bankAcComp,"bankAcComp",28);
  defConfigSource.add(defBankAcComp);
  
  String chequeBk = formTextForKey("cfgDefChequeBk");
  DefaultConfigSetting defChequeBk = new DefaultConfigSetting(chequeBk,"chequeBk",29);
  defConfigSource.add(defChequeBk);
  
  String bacsTransCd = formTextForKey("cfgDefBacsTranCode");
  DefaultConfigSetting defBacsTransCd = new DefaultConfigSetting(bacsTransCd,"bacsTransCd",30);
  defConfigSource.add(defBacsTransCd);
  
  
  String glAcntComp = formTextForKey("cfgDefGlAcntComp");
  DefaultConfigSetting defglAcntComp = new DefaultConfigSetting(glAcntComp,"glAcntComp",31);
  defConfigSource.add(defglAcntComp);
  
  String fund = formTextForKey("cfgDefFnd");
  DefaultConfigSetting defFund = new DefaultConfigSetting(fund,"fund",32);
  defConfigSource.add(defFund);
  
  String costCent = formTextForKey("cfgCostCent");
  DefaultConfigSetting defCostCent = new DefaultConfigSetting(costCent,"costCent",33);
  defConfigSource.add(defCostCent);
  
  String prog = formTextForKey("cfgProg");
  DefaultConfigSetting defProg = new DefaultConfigSetting(prog,"prog",34);
  defConfigSource.add(defProg);
  
  String ptnrRole = formTextForKey("cfgPtnrRole");
  DefaultConfigSetting defPtnrRole = new DefaultConfigSetting(ptnrRole,"ptnrRole",35);
  defConfigSource.add(defPtnrRole);
  
  String vatCode = formTextForKey("cfgVatCode");
  DefaultConfigSetting defVatCode = new DefaultConfigSetting(vatCode,"vatCode",36);
  defConfigSource.add(defVatCode);
  
  String vatCodeComp = formTextForKey("cfgVatCodeComp");
  DefaultConfigSetting defVatCodeComp = new DefaultConfigSetting(vatCodeComp,"vatCodeComp",37);
  defConfigSource.add(defVatCodeComp);
  
  
  defaults = new DualListModel<>(defConfigSource, defConfigTarget);
  
 }

 public DualListModel<DefaultConfigSetting> getDefaults() {
  return defaults;
 }

 public void setDefaults(DualListModel<DefaultConfigSetting> defaults) {
  this.defaults = defaults;
 }
 

 public int getProgress() {
  if(steps == 0){
   return 0;
  }
  LOGGER.log(INFO, "getProgress Steps {0} stepNum {1}", new Object[]{steps, stepNum});
  Double currprogress =  (double)stepNum / steps * 100;
  
  LOGGER.log(INFO, "getProgress progress {0}", currprogress); 
  progress = currprogress.intValue();
  //progress = stepNum / steps * 100;
  LOGGER.log(INFO, "progress {0}", progress);
  if(progress > 100){
   progress = 100;
  }
  return progress;
 }

 public void setProgress(int progress) {
  this.progress = progress;
 }
 
 private void loadAccountType(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadAccountType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    if(row.getRowNum() == 0){
     continue;
    }
    String name = row.getCell(0).getStringCellValue();
    AccountTypeRec accntTy = sysBuff.getAcntTypeByName(name);
    if(accntTy != null && accntTy.getId() != null){
     continue;
    }
    
   //For each row, iterate through each columns
    accntTy = new AccountTypeRec();
    accntTy.setCreatedBy(currUsr);
    accntTy.setCreatedDate(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      CellType ct = cell.getCellTypeEnum();
      if(ct == CellType.NUMERIC){
       Double dval = cell.getNumericCellValue();
      }else if(ct == CellType.STRING){
       String strVal = cell.getStringCellValue();
       switch (cell.getColumnIndex()) {
       case 0:
        accntTy.setName(strVal);
        break;
       case 1:
        accntTy.setDescription(strVal);
        break;
       case 2:
        boolean debit = strVal.equals("Y");
        accntTy.setDebit(debit);
        break;
       case 3:
        boolean sysPost = strVal.equals("Y");
        accntTy.setSystemPost(sysPost);
        break;
       case 4:
        boolean recon = (strVal.equals("Y"));
        accntTy.setControlAccount(recon);
        break;
       case 5:
        LOGGER.log(INFO, "Sub led {0} ",strVal);
        if(strVal != null && !strVal.isEmpty()){
         LedgerRec led = sysBuff.getLedgerByName(strVal);
         accntTy.setSubLedger(led);
        }
         break;
       case 6:
        LOGGER.log(INFO, "Module {0}",strVal);
        if(strVal != null && !strVal.isEmpty()){
         ModuleRec mod = sysBuff.getModuleByCode(strVal);
         LOGGER.log(INFO, "Mod from sys buff {0}", mod.getName());
         accntTy.setModule(mod);
        }
        break;
       case 7:
        LOGGER.log(INFO, "PL {0}",strVal);
        boolean pl = (strVal.equals("Y"));
        accntTy.setProfitAndLossAccount(pl);
        break;
       case 8:
        LOGGER.log(INFO, "Process Code {0}",strVal);
        ProcessCodeRec prCd = this.sysBuff.getProcessCodeByName(strVal);
        LOGGER.log(INFO, "Process Code from sys buffer {0}", prCd);
        if(prCd == null){
         LOGGER.log(INFO, "Could not find process code code used {0}", strVal);
         continue;
        }
        accntTy.setProcessCode(prCd);
        break;
       case 9:
        LOGGER.log(INFO, "ret earn {0}",strVal);
        boolean ret = (strVal.equals("Y") ? true:false);
        accntTy.setRetainedEarn(ret);
        break;
       case 10:
        LOGGER.log(INFO, "number Range {0}",strVal);
        NumberRangeRec numRng = this.sysBuff.getNumRangeByShortDescr(strVal);
        LOGGER.log(INFO, "number Range from sys buff {0}",numRng);
        accntTy.setNumberRange(numRng);
        break;
       default:
        break;
       }
      }
      
      }
     
     LOGGER.log(INFO, "Need to update line code {0} descr {1}", new Object[]{accntTy.getName(),accntTy.getDescription()});
     sysBuff.accountTypeUpdate(accntTy,this.getView());
     
   }
   
  }catch(IOException ex ){
   
  } 
 }
 
 private void loadBacsTransCode(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadBacsTransCode called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   Iterator<Row> rowIterator = sheet1.iterator();
   
   while(rowIterator.hasNext()) {
   Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){ // 1st cell must have a value
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    LOGGER.log(INFO, "From Spread sheet name {0}", code);
    BacsTransCodeRec bacsTransCd = sysBuff.getBacsTransactionCodeByCode(code);
    
    if(bacsTransCd != null){
     LOGGER.log(INFO, "Bacs transaction code {0} already exists ", code);
     MessageUtil.addWarnMessageParam("trBacsTranCdExist", "bacResponse", code);
     continue;
    }
    //For each row, iterate through each columns
    bacsTransCd = new BacsTransCodeRec();
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
     LOGGER.log(INFO, "Cell type {0}", cell.getCellType());
     switch(cell.getCellType()) {
      case Cell.CELL_TYPE_BOOLEAN:
       if(cell.getColumnIndex() == 4){
        boolean cellBool = cell.getBooleanCellValue();
        bacsTransCd.setDebit(cellBool);
       }else if(cell.getColumnIndex() == 4){
        boolean cellBool = cell.getBooleanCellValue();
        bacsTransCd.setCollection(cellBool);
       }
       break;
      case Cell.CELL_TYPE_NUMERIC:
       break;
      case Cell.CELL_TYPE_STRING:
       String cellVal = cell.getStringCellValue();
       switch (cell.getColumnIndex()) {
        case 0:
         bacsTransCd.setPtnrBnkTransCode(cellVal);
         break;
        case 1:
         bacsTransCd.setContrTransCode(cellVal);
         break;
        case 2:
         bacsTransCd.setName(cellVal);
         break;
        case 3:
         bacsTransCd.setDescription(cellVal);
         break;
        case 5:
         bacsTransCd.getPrCode();
         default:
        break;
       }
     }
    }
    //completed row
    LOGGER.log(INFO, "Current BACS trans code {0}", bacsTransCd.getPtnrBnkTransCode());
    bacsTransCd.setCreatedBy(currUsr);
    bacsTransCd.setCreatedOn(new Date());
    bacsTransCd = this.bankMgr.updateBacsTransCode(bacsTransCd, src);
    LOGGER.log(INFO, "Created BACS trans Code id{0} code {1}", new Object[]{bacsTransCd.getId(),bacsTransCd.getPtnrBnkTransCode()});
   }
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not open file {0}", filePath);
   MessageUtil.addErrorMessageParam1("defConfigFn", "errorText", filePath);
   
  }
 }
 private void loadBank(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadAccountType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String name = row.getCell(0).getStringCellValue();
    LOGGER.log(INFO, "From Spread sheet name {0}", name);
    
    BankRec bank = this.bankMgr.getBankByBankCode(name);
    
    if(bank != null){
     LOGGER.log(INFO, "Found bank with code {0}", bank.getBankCode());
     continue;
    }
    
    
   //For each row, iterate through each columns
    bank = new BankRec();
    boolean hasRegOff = false;
    boolean hasOrg = false;
    PartnerCorporateRec bnkOrg = null;
    AddressRec bnkAddr = null;
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 5){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setAddrRef(String.valueOf(dval.intValue()));
        }
        
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        LOGGER.log(INFO, "Column number {0}", cell.getColumnIndex());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         bank.setBankCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         bank.setChapsBankCode(strVal);
        }else if(cell.getColumnIndex() == 2){
         if(!hasOrg){
          bnkOrg = new PartnerCorporateRec();
          hasOrg = true;
         }
         bnkOrg.setRef(strVal);
        }else if(cell.getColumnIndex() == 3){
         if(!hasOrg){
          bnkOrg = new PartnerCorporateRec();
          hasOrg = true;
         }
         bnkOrg.setLegalName(strVal);
        }else if(cell.getColumnIndex() == 4){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setAddrRef(strVal);
        }else if(cell.getColumnIndex() == 6){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setHouseName(strVal);
        }else if(cell.getColumnIndex() == 7){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setStreet(strVal);
        }else if(cell.getColumnIndex() == 8){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setStreet2(strVal);
        }else if(cell.getColumnIndex() == 9){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setTown(strVal);
        }else if(cell.getColumnIndex() == 10){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setCountyName(strVal);
        }else if(cell.getColumnIndex() == 11){
         if(!hasRegOff){
          bnkAddr = new AddressRec();
          hasRegOff = true;
         }
         bnkAddr.setPostCode(strVal);
        }
        
       break;
      }
      }
     
     LOGGER.log(INFO, "Need to Bank code {0} ", bank.getBankCode());
     bank.setCreatedBy(currUsr);
     bank.setCreatedOn(new Date());
     bnkOrg.setCreatedBy(currUsr);
     bnkOrg.setCreatedDate(new Date());
     bank.setBankOrganisation(bnkOrg);
     if(hasRegOff){
      bnkAddr.setCreatedBy(currUsr);
      bnkAddr.setCreatedOn(new Date());
      bank.setBankAddress(bnkAddr);
     }
     
     bank = this.bankMgr.createBank(bank, currUsr, getView());
     LOGGER.log(INFO, "bank created with id {0}", bank.getId());
     
     
     
   }
   
  }catch(IOException ex ){
   
  } 
 }
 
 private void loadBankAccount(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadAccountType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }

    Double scNum = row.getCell(6).getNumericCellValue();
    String sortCode = String.valueOf(scNum.intValue());
    
    
    BankBranchRec branch = this.bankMgr.getBankBranchBySortCode(sortCode);
    
    if(branch == null){
     LOGGER.log(INFO, "No branch found for sort code {0}", sortCode);
     continue;
    }  
    
    Double acntNum = row.getCell(1).getNumericCellValue();
    String acntNumStr = String.valueOf(acntNum.intValue());
    BankAccountRec bnkAc = this.bankMgr.getBankAccountByNumber(acntNumStr, branch.getId());
    if(bnkAc != null){
     LOGGER.log(INFO, "Bank Ac num {0} already exists", acntNumStr);
    }
    bnkAc= new BankAccountRec();
    bnkAc.setCreatedBy(currUsr);
    bnkAc.setCreatedOn(null);
   //For each row, iterate through each columns
    
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch (cell.getColumnIndex()){
         case 0:
          bnkAc.setAccountRef(String.valueOf(dval.intValue()));
          break;
         case 1:
          bnkAc.setAccountNumber(String.valueOf(dval.intValue()));
          break;
         case 6:
          bnkAc.setAccountForBranch(branch);
          break;
          
         
        }
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        LOGGER.log(INFO, "Column number {0}", cell.getColumnIndex());
        String strVal = cell.getStringCellValue();
        switch (cell.getColumnIndex()){
         case 0:
          bnkAc.setAccountRef(strVal);
          break;
         case 1:
          bnkAc.setAccountNumber(strVal);
          break;
         case 2:
          bnkAc.setAccountName(strVal);
          break;
         case 3:
          boolean drCrDis = strVal.equals("Y")?true:false;
          bnkAc.setDirectCreditAllowed(drCrDis);
          break;
         case 4:
          boolean drDrDis = strVal.equals("Y")?true:false;
          bnkAc.setDirectDebitsAllowed(drDrDis);
          break;
         case 5:
          boolean fastPymnt = strVal.equals("Y")?true:false;
          bnkAc.setFasterPayments(fastPymnt);
          break;
        }
         

       
       break;
      }
    
    
             
    }
    
    LOGGER.log(INFO, "Save Bank Account ref; {0} num {1} name {2} dir Cr {3} dr Deb {4} fast {5}",
            new Object[]{bnkAc.getAccountRef(), bnkAc.getAccountNumber(), bnkAc.getAccountName(),
             bnkAc.isDirectCreditAllowed(), bnkAc.isDirectDebitsAllowed(), bnkAc.isFasterPayments()
            });
    bnkAc = this.bankMgr.bankAccountUpdate(bnkAc, currUsr, getView(), false);
    LOGGER.log(INFO, "bank Account created with id {0}", bnkAc);
   }
  }catch(IOException ex ){
  } 
 }

  private void loadBankAccountComp(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadAccountType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }

    Double scNum = row.getCell(6).getNumericCellValue();
    String sortCode = String.valueOf(scNum.intValue());
    
    
    BankBranchRec branch = this.bankMgr.getBankBranchBySortCode(sortCode);
    
    if(branch == null){
     LOGGER.log(INFO, "No branch found for sort code {0}", sortCode);
     continue;
    }  
    
    Double acntNum = row.getCell(1).getNumericCellValue();
    String acntNumStr = String.valueOf(acntNum.intValue());
    BankAccountCompanyRec bnkAc = (BankAccountCompanyRec)this.bankMgr.getBankAccountByNumber(acntNumStr, branch.getId());
    if(bnkAc != null){
     LOGGER.log(INFO, "Bank Ac num {0} already exists", acntNumStr);
    }
    bnkAc= new BankAccountCompanyRec();
    bnkAc.setCreatedBy(currUsr);
    bnkAc.setCreatedOn(null);
   //For each row, iterate through each columns
    
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch (cell.getColumnIndex()){
         case 0:
          bnkAc.setAccountRef(String.valueOf(dval.intValue()));
          break;
         case 1:
          bnkAc.setAccountNumber(String.valueOf(dval.intValue()));
          break;
         case 6:
          bnkAc.setAccountForBranch(branch);
          break;
         case 7:
          String compRef = String.valueOf(dval.intValue());
          CompanyBasicRec comp = this.sysBuff.getCompany(compRef);
          if(comp == null){
           LOGGER.log(INFO, "Company not found {0}", compRef);
           MessageUtil.addErrorMessage("bnkAcCompNf", "errorText");
           continue;
          }else{
           bnkAc.setComp(comp);
          }
        }
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        LOGGER.log(INFO, "Column number {0}", cell.getColumnIndex());
        String strVal = cell.getStringCellValue();
        switch (cell.getColumnIndex()){
         case 0:
          bnkAc.setAccountRef(strVal);
          break;
         case 1:
          bnkAc.setAccountNumber(strVal);
          break;
         case 2:
          bnkAc.setAccountName(strVal);
          break;
         case 3:
          boolean drCrDis = strVal.equals("Y")?true:false;
          bnkAc.setDirectCreditAllowed(drCrDis);
          break;
         case 4:
          boolean drDrDis = strVal.equals("Y")?true:false;
          bnkAc.setDirectDebitsAllowed(drDrDis);
          break;
         case 5:
          boolean fastPymnt = strVal.equals("Y")?true:false;
          bnkAc.setFasterPayments(fastPymnt);
          break;
         case 7:
          CompanyBasicRec comp = this.sysBuff.getCompany(strVal);
          if(comp == null){
           LOGGER.log(INFO, "Company not found {0}", strVal);
           MessageUtil.addErrorMessage("bnkAcCompNf", "errorText");
           continue;
          }else{
           bnkAc.setComp(comp);
          }
          
        }
       break;
      }
     }
    
    LOGGER.log(INFO, "Save Bank Account ref; {0} num {1} name {2} dir Cr {3} dr Deb {4} "
            + "fast {5} company ref {6}",
            new Object[]{bnkAc.getAccountRef(), bnkAc.getAccountNumber(), bnkAc.getAccountName(),
             bnkAc.isDirectCreditAllowed(), bnkAc.isDirectDebitsAllowed(), 
             bnkAc.isFasterPayments(), bnkAc.getComp().getReference()
            });
    bnkAc = this.bankMgr.createOwnBankAccount(bnkAc, currUsr, getView());
    LOGGER.log(INFO, "bank Account Comp created with id {0}", bnkAc.getId());
   }
  }catch(IOException ex ){
  } 
 }
 private void loadBankBranch(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadBankBranch called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   
   while(rowIterator.hasNext() ) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }

    String sortCd = null ;
    
    if(row.getCell(0).getCellTypeEnum() == CellType.NUMERIC){
     Double db = row.getCell(0).getNumericCellValue();
     LOGGER.log(INFO, "Sort code number {0}", db);
     sortCd = String.valueOf(db.intValue());
    }if(row.getCell(0).getCellTypeEnum() == CellType.STRING){
     LOGGER.log(INFO, "Sort code number {0}", row.getCell(0).getStringCellValue());
     sortCd = row.getCell(0).getStringCellValue();
    }
    LOGGER.log(INFO, "sortCd {0}", sortCd);
    if(sortCd == null){
     LOGGER.log(INFO, "Sort Code not found for row {0}", row.getRowNum());
     continue;
    }else {
     BankBranchRec br = bankMgr.getBankBranchBySortCode(sortCd);
     LOGGER.log(INFO, "bankMgr returns Bank Branch {0}", br);
     if(br != null){
      LOGGER.log(INFO, "skip to next row");
      continue;
     }
     /*List<BankBranchRec> branches = this.bankMgr.getBanchesBranchBySortCode(sortCd);
     if(branches != null){
      for(BankBranchRec br:branches){
       if(br.getSortCode().equals(sortCd)){
        LOGGER.log(INFO, "Branch with sort code {0} already exists", sortCd);
        continue;
       }
      }
     } 
     */
    }
   BankRec bank;
   String bnkCd = row.getCell(1).getStringCellValue();
   bank = this.bankMgr.getBankByBankCode(bnkCd);
   if(bank == null ){
    LOGGER.log(INFO, "bank for code {0} not found", bnkCd);
    continue;
   }
   BankBranchRec branch = new BankBranchRec();
   branch.setSortCode(sortCd);
   branch.setBank(bank);
   branch.setCreatedBy(currUsr);
   branch.setCreatedOn(new Date());
   
   AddressRec brAddr = new AddressRec();
   boolean hasAddr = false;
   //For each row, iterate through each columns
    
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      if(null != cell.getCellTypeEnum())switch (cell.getCellTypeEnum()) {
      case BOOLEAN:
       LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
       break;
     /*
     switch(cell.getCellTypeEnum()) {
     case CellType.  CELL_TYPE_BOOLEAN:
     LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
     break;
     case Cell.CELL_TYPE_NUMERIC:
     LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
     Double dval = cell.getNumericCellValue();
     if(cell.getColumnIndex() == 5){
     String phonArea = String.valueOf(dval.intValue());
     branch.setPhoneArea(phonArea);
     }else if(cell.getColumnIndex() == 6){
     String phonNum = String.valueOf(dval.intValue());
     branch.setPhoneNumber(phonNum);
     }else if(cell.getColumnIndex() == 10){
     String poBox = String.valueOf(dval.intValue());
     brAddr.setPoBox(poBox);
     }else if(cell.getColumnIndex() == 12){
     String houseNum = String.valueOf(dval.intValue());
     brAddr.setHouseNumber(houseNum);
     }
     break;
     case Cell.CELL_TYPE_STRING:
     LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
     LOGGER.log(INFO, "Column number {0}", cell.getColumnIndex());
     String strVal = cell.getStringCellValue();
     if(cell.getColumnIndex() == 2){
     branch.setBranchName(strVal);
     }else if(cell.getColumnIndex() == 3){
     branch.setSwiftCode(strVal);
     }else if(cell.getColumnIndex() == 4){
     boolean isSubBr = strVal.equals("Y")?true:false;
     branch.setSubBranch(isSubBr);
     }else if(cell.getColumnIndex() == 5){
     branch.setPhoneArea(strVal);
     }else if(cell.getColumnIndex() == 6){
     branch.setPhoneNumber(strVal);
     }else if(cell.getColumnIndex() == 7){
     brAddr.setAddrRef(strVal);
     }else if(cell.getColumnIndex() == 8){
     brAddr.setDepartment(strVal);
     }else if(cell.getColumnIndex() == 9){
     brAddr.setBuilding(strVal);
     }else if(cell.getColumnIndex() == 11){
     brAddr.setHouseName(strVal);
     }else if(cell.getColumnIndex() == 13){
     brAddr.setStreet(strVal);
     }else if(cell.getColumnIndex() == 14){
     brAddr.setStreet2(strVal);
     }else if(cell.getColumnIndex() == 15){
     brAddr.setTown(strVal);
     }else if(cell.getColumnIndex() == 16){
     brAddr.setCountyName(strVal);
     }else if(cell.getColumnIndex() == 17){
     brAddr.setPostCode(strVal);
     hasAddr = true;
     }
     break;
     }
      */
      case NUMERIC:
       LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
       Double dval = cell.getNumericCellValue();
       switch (cell.getColumnIndex()) {
        case 5:
         String phonArea = String.valueOf(dval.intValue());
         branch.setPhoneArea(phonArea);
         break;
        case 6:
         String phonNum = String.valueOf(dval.intValue());
         branch.setPhoneNumber(phonNum);
         break;
        case 10:
         String poBox = String.valueOf(dval.intValue());
         brAddr.setPoBox(poBox);
         break;
        case 12:
         String houseNum = String.valueOf(dval.intValue());
         brAddr.setHouseNumber(houseNum);
         break;
        default:
         break;
       }
       break;
      case STRING:
       LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
       LOGGER.log(INFO, "Column number {0}", cell.getColumnIndex());
       String strVal = cell.getStringCellValue();
       switch (cell.getColumnIndex()) {
        case 2:
         branch.setBranchName(strVal);
         break;
        case 3:
         branch.setSwiftCode(strVal);
         break;
        case 4:
         boolean isSubBr = strVal.equals("Y");
         branch.setSubBranch(isSubBr);
         break;
        case 5:
         branch.setPhoneArea(strVal);
         break;
        case 6:
         branch.setPhoneNumber(strVal);
         break;
        case 7:
         brAddr.setAddrRef(strVal);
         break;
        case 8:
         brAddr.setDepartment(strVal);
         break;
        case 9:
         brAddr.setBuilding(strVal);
         break;
        case 11:
         brAddr.setHouseName(strVal);
         break;
        case 13:
         brAddr.setStreet(strVal);
         break;
        case 14:
         brAddr.setStreet2(strVal);
         break;
        case 15:
         brAddr.setTown(strVal);
         break;
        case 16:
         brAddr.setCountyName(strVal);
         break;
        case 17:
         brAddr.setPostCode(strVal);
         hasAddr = true;
         break;
        default:
         break;
       }
       break;
      default:
       break;
     }

    
             
    }
    if(hasAddr){
     if(brAddr.getId() == null){
     brAddr.setCreatedBy(currUsr);
     brAddr.setCreatedOn(new Date());
    } else {
     brAddr.setChangedBy(currUsr);
     brAddr.setChangedOn(new Date(0));
     }
     branch.setBranchAddress(brAddr);
    }
    bankMgr.createBankBranch(branch, currUsr, getView());
    LOGGER.log(INFO, "Save Bank Branch");
   }
  }catch(IOException ex ){
  } 
 }
 private void loadCalFixedDay(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCalFixedDay called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    String ref = row.getCell(0).getStringCellValue();
    CalendarRuleFixedDateRec calFixedDay; 
    calFixedDay = (CalendarRuleFixedDateRec)this.compMgr.getCalendarRuleByRef(ref);
    if(calFixedDay != null ){
     LOGGER.log(INFO, "Fixed day cal found with with ref {0} found ", ref);
     continue;
    }
    
   //For each row, iterate through each columns
    calFixedDay = new CalendarRuleFixedDateRec();
    calFixedDay.setCreatedBy(currUsr);
    calFixedDay.setCreatedOn(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 1){
         calFixedDay.setDescription(dval.toString());
        }
        if(cell.getColumnIndex() == 2){
         calFixedDay.setDayOfMonth(dval.intValue());
        }else if(cell.getColumnIndex() == 3){
         calFixedDay.setStartMonthNumber(dval.intValue());
        }else if(cell.getColumnIndex() == 4){
         calFixedDay.setPeriodNum(dval.intValue());
        }else if(cell.getColumnIndex() == 5){
         calFixedDay.setSpecialPeriodNum(dval.intValue());
        }
        
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         //reference
        calFixedDay.setReference(strVal);
        }else if(cell.getColumnIndex() == 1){
         calFixedDay.setDescription(strVal);
        }
        
        
       break;
      }
     }
     LOGGER.log(INFO, "Need to cal Fixed date {0} descr {1} class {2} norm per {3} sp Per {4}", 
             new Object[]{calFixedDay.getReference(),calFixedDay.getDescription(),
             calFixedDay.getClass().getSimpleName(),calFixedDay.getPeriodNum(),
             calFixedDay.getSpecialPeriodNum()});
     
    compMgr.updateCalendarRuleBaseRec(calFixedDay, src);
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  } 
  
 }
 
 private void loadCalFlexPeriod(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCalFlexPeriod called with file {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //CalendarRuleFlexPerRec currCalFlPer = null;
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = row.getCell(0).getStringCellValue();
    Double yrDbl = row.getCell(1).getNumericCellValue();
    int yr = yrDbl.intValue();
    CalendarRuleFlexPerRec calFlPer; 
    CalendarRuleFlexYearRec calFlYr;
    try{
    calFlYr = compMgr.getCalendarRuleFlexYear(ref, yr);
    
    LOGGER.log(INFO, "calFlYr from compMgr {0}", calFlYr);
    if(calFlYr == null ){
     LOGGER.log(INFO, "Flex Year found with with ref {0} year {1} ", new Object[]{ref,yr});
     MessageUtil.addErrorMessageParam1("perCalFlxYrNf", "serverErrorText", "ref: "+ref+" year: "+yr);
     
     continue;
    }
    }catch(BacException ex){
     MessageUtil.addErrorMessageParam1("perCalFlxYrDup", "serverErrorText", "ref: "+ref+" year: "+yr);
     continue;
    }
    CalendarRuleBaseRec calRuleBase = compMgr.getCalendarRuleByRef(ref);
    
    calFlYr.setCalRule(calRuleBase);
    
    
    //For each row, iterate through each columns
    calFlPer = new CalendarRuleFlexPerRec();
    calFlPer.setCreatedBy(currUsr);
    calFlPer.setCreatedOn(new Date());
    calFlPer.setCalRuleFlexYr(calFlYr);
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        
        break;
       case Cell.CELL_TYPE_NUMERIC:
        
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 2){
         calFlPer.setCalPeriod(dval.intValue());
        }
        if(cell.getColumnIndex() == 3){
         calFlPer.setStartPeriod(cell.getDateCellValue());
        }
        if(cell.getColumnIndex() == 4){
         calFlPer.setEndPeriod(cell.getDateCellValue());
        }
       break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
       
        if(cell.getColumnIndex() == 5){
         boolean auditB = (strVal.equals("Y"));
         calFlPer.setAuditPer(auditB);
        
        }
        
       break;
      }
     }
     LOGGER.log(INFO, "calFlPer {0}", calFlPer);
     LOGGER.log(INFO, "CalRuleFlexYr {0}", calFlPer.getCalRuleFlexYr());
     LOGGER.log(INFO, "CalRuleFlexYr  cal rule{0}", calFlPer.getCalRuleFlexYr().getCalRule());
     LOGGER.log(INFO, "period {0}",calFlPer.getCalPeriod());
     LOGGER.log(INFO, "start {0}",calFlPer.getStartPeriod());
     LOGGER.log(INFO, "end {0}",calFlPer.getEndPeriod());
     LOGGER.log(INFO, "Need to add flex Year rule ref {0}  year {1} Period {2} start {3} end {4}  ", 
             new Object[]{calFlPer.getCalRuleFlexYr().getCalRule().getReference(),
              calFlPer.getCalRuleFlexYr().getCalYear(),
             calFlPer.getCalPeriod(), calFlPer.getStartPeriod(), calFlPer.getEndPeriod()});
     
     compMgr.updateCalendarRuleFlexPeriodRec(calFlPer, src);
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 private void loadCalFlexYear(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCalFlexYear called with file {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = row.getCell(0).getStringCellValue();
    Double yrDbl = row.getCell(2).getNumericCellValue();
    int yr = yrDbl.intValue();
    CalendarRuleFlexYearRec calFlYr; 
    try{
    calFlYr = compMgr.getCalendarRuleFlexYear(ref, yr);
    LOGGER.log(INFO, "calFlYr from compMgr {0}", calFlYr);
    if(calFlYr != null ){
     LOGGER.log(INFO, "Flex Year found with with ref {0} year {1} ", new Object[]{ref,yr});
     continue;
    }
    }catch(BacException ex){
     MessageUtil.addErrorMessageParam1("perCalFlxYrDup", "serverErrorText", "ref: "+ref+" year: "+yr);
    }
   //For each row, iterate through each columns
    calFlYr = new CalendarRuleFlexYearRec();
    calFlYr.setCreatedBy(currUsr);
    calFlYr.setCreatedOn(new Date());
    
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 2){
         calFlYr.setCalYear(dval.intValue());
        }
       break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         try{
         CalendarRuleBaseRec rulebase = this.compMgr.getCalendarRuleByRef(strVal);
         LOGGER.log(INFO, "rulebase from compMgr {0}", rulebase);
         if(rulebase == null){
          rulebase = new CalendarRuleBaseRec();
          rulebase.setCreatedBy(currUsr);
          rulebase.setCreatedOn(new Date());
          rulebase.setReference(strVal);
          
         }else{
          rulebase.setChangedBy(currUsr);
          rulebase.setChangedOn(new Date());
         }
         LOGGER.log(INFO, "rulebase id {0}", rulebase.getId());
         calFlYr.setCalRule(rulebase);
         }catch(BacException ex){
          MessageUtil.addErrorMessageParam1("calRuleDupl", "serverErrorText", "Ref: "+strVal);
          
         }
        }
        if(cell.getColumnIndex() == 1){
         if(calFlYr.getCalRule().getId() == null){
          LOGGER.log(INFO, "New base rule");
          calFlYr.getCalRule().setDescription(strVal);
         }
        }
        
       break;
      }
     }
     
     LOGGER.log(INFO, "Need to add flex Year rule ref {0} descr {1} year {2} start  ", 
             new Object[]{calFlYr.getCalRule().getReference(),calFlYr.getCalRule().getDescription(),
             calFlYr.getCalYear()});
     calFlYr.getCalRule().setFlexCal(true);
     compMgr.updateCalendarRuleFlexYearRec(calFlYr, src);
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 private void loadCalMonth(String fname, UserRec currUsr, String src){
  
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = row.getCell(0).getStringCellValue();
    CalendarRuleMonthRec calMonth; 
    calMonth = (CalendarRuleMonthRec)this.compMgr.getCalendarRuleByRef(ref);
    if(calMonth != null ){
     LOGGER.log(INFO, "Month cal found with with ref {0} found ", ref);
     continue;
    }
    
   //For each row, iterate through each columns
    calMonth = new CalendarRuleMonthRec();
    calMonth.setCreatedBy(currUsr);
    calMonth.setCreatedOn(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 1){
         calMonth.setDescription(dval.toString());
        } else if(cell.getColumnIndex() == 2){
         calMonth.setStartMonthNumber(dval.intValue());
        }
        
        break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         //reference
        calMonth.setReference(strVal);
        }else if(cell.getColumnIndex() == 1){
         calMonth.setDescription(strVal);
        }
        
        
       break;
      }
     }
     LOGGER.log(INFO, "Need to add calendar month {0} descr {1} class {2} start month {3} ", 
             new Object[]{calMonth.getReference(),calMonth.getDescription(),
             calMonth.getClass().getSimpleName(),calMonth.getStartMonthNumber()});
   calMonth.setMonthCal(true);
   compMgr.updateCalendarRuleBaseRec(calMonth, src);
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 
 private void loadCalNatural(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "Called loadCalMonthNatural with file name {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = row.getCell(0).getStringCellValue();
    CalendarRuleBaseRec ruleBase = new CalendarRuleBaseRec();
    ruleBase.setCreatedBy(currUsr);
    ruleBase.setChangedOn(new Date());
    ruleBase.setReference(ref);
    ruleBase.setNaturalCal(true);
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()){
     Cell cell = cellIterator.next();
     switch(cell.getCellType()) {
      case Cell.CELL_TYPE_STRING:
       LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
       String strVal = cell.getStringCellValue();
       if(cell.getColumnIndex() == 0){
         //reference
        ruleBase.setReference(strVal);
       }else if(cell.getColumnIndex() == 1){
         ruleBase.setDescription(strVal);
       }
       break;
      case Cell.CELL_TYPE_BOOLEAN:
       LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
       ruleBase.setNaturalCal(cell.getBooleanCellValue());
        break;
     }
    }
    ruleBase.setCreatedBy(currUsr);
    ruleBase.setCreatedOn(new Date());
    compMgr.updateCalendarRuleBaseRec(ruleBase, src);
   }
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 
 private void loadChartOfAccounts(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCountryRec called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = row.getCell(0).getStringCellValue();
    ChartOfAccountsRec coa = compMgr.getChartOfAccountByRef(ref);
    
    if(coa != null ){
     LOGGER.log(INFO, "COA with ref {0} found ", ref);
     continue;
    }
    
   //For each row, iterate through each columns
    coa = new ChartOfAccountsRec();
    coa.setCreatedBy(currUsr);
    coa.setCreatedDate(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         coa.setReference(strVal);
        }
        if(cell.getColumnIndex() == 1){
         coa.setName(strVal);
        }
        if(cell.getColumnIndex() == 2){
         CurrencyRec curr = this.mdm.getCurrencyByCode(strVal);
         LOGGER.log(INFO, "mdm returns currency {0} for code {1}", new Object[]{curr,strVal});
         coa.setChartCurrency(curr);
        }
        if(cell.getColumnIndex() == 3){
         try{
         CountryRec country = this.mdm.getCountryByRef2(strVal);
         coa.setChartCountry(country);
         }catch(BacException ex){
          MessageUtil.addErrorMessage(ex.getErrorCode(), "serverErrorText");
         }
        }
        if(cell.getColumnIndex() == 4){
         boolean def = (strVal.equals("Y"));
         coa.setDefaultChart(def);
         
        }
        if(cell.getColumnIndex() == 5){
         boolean balFwd = (strVal.equals("Y"));
         coa.setOibalFwd(balFwd);
        }
        if(cell.getColumnIndex() == 6){
         FisPeriodRuleRec perRule = sysBuff.getFisPeriodRuleByName(strVal);
         LOGGER.log(INFO, "Period rule from sys buff {0}", perRule);
         coa.setPeriodRule(perRule);
        }
      
        
        
       break;
      }
     }
     LOGGER.log(INFO, "creat coa name {0} descr {1} curr code {2} country {3} default {4} balBfwd {5} period rule {6}",
             new Object[]{coa.getReference(),coa.getName(),coa.getChartCurrency(),coa.getChartCountry(),coa.isDefaultChart(),
              coa.isOibalFwd(),coa.getPeriodRule()});
     coa = compMgr.coaUpdate(coa, currUsr, getView());
     LOGGER.log(INFO, "create coa {0}", coa.getName());
   //  mdm.countryUpdate(cntry, currUsr, src);
   
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  } 
 }
 
 private void loadChequeBook(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadChequeBook called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   
  // ModuleRec mod = this.sysBuff.getModuleByCode("TR");
   //LOGGER.log(INFO, "Module {0}", mod);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    
    String cbRef;
    if(row.getCell(4).getCellTypeEnum() == CellType.STRING){
     cbRef = row.getCell(4).getStringCellValue();
    }else{
     Double dval = row.getCell(4).getNumericCellValue();
     cbRef = String.valueOf(dval.intValue());
    }
    LOGGER.log(INFO, "CB ref to check for dupl {0}", cbRef);
    NumberRangeChequeRec cb = (NumberRangeChequeRec)this.setup.getNumControlByShrtDescr(cbRef);
    if(cb != null ){
     LOGGER.log(INFO, "Cheque book with ref {0} found ", cbRef);
     continue;
    }
    
    //For each row, iterate through each columns
    CompanyBasicRec comp = null;
    String bankSortCd = null;
    cb = new NumberRangeChequeRec();
    cb.setCreatedBy(currUsr);
    cb.setCreatedDate(new Date());
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
     LOGGER.log(INFO, "cell type {0}", cell.getCellTypeEnum());
     LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
     
     switch(cell.getCellTypeEnum()) {
      case BOOLEAN:
       LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
       cb.setAutoNum(cell.getBooleanCellValue());
       break;
      case NUMERIC:
       LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
       Double dval = cell.getNumericCellValue();
       switch(cell.getColumnIndex()){
        
        case 1:
         LOGGER.log(INFO, "get company from ref");
         String compRef = String.valueOf(dval.intValue());
         comp = sysBuff.getCompany(compRef);
         
         break;
        case 2:
         LOGGER.log(INFO, "Sort code");
         bankSortCd = String.valueOf(dval.intValue());
         break;
        case 3:
         LOGGER.log(INFO,"set Bank acnt num");
         String bnkAcntNum = String.valueOf(dval.intValue());
         BankAccountCompanyRec bnkAcnt = bankMgr.getBankAccountForCompany(bnkAcntNum, comp);
         LOGGER.log(INFO, "bnkAcnt {0} found by bankMr ", bnkAcnt);
         cb.setBankAccountComp(bnkAcnt);
         break;
        case 6:
         cb.setFromNum(dval.intValue());
         LOGGER.log(INFO, "from num {0}", cb.getFromNum());
         break;
        case 7:
         cb.setToNum(dval.intValue());
         LOGGER.log(INFO, "to num {0}", cb.getToNum());
         break;
        }
      break;
      case STRING:
       String strVal = cell.getStringCellValue();
       LOGGER.log(INFO, "String value {0}", strVal);
       switch(cell.getColumnIndex()){
        case 0:
         LOGGER.log(INFO, "Get number range type {0}", strVal);
         List<NumberRangeTypeRec> numRngTyps = sysBuff.getNumRangeTypes();
         if(numRngTyps == null){
          LOGGER.log(INFO, "No number ranges from sys buff{0}", numRngTyps);
          break;
         }
         ListIterator<NumberRangeTypeRec> numRngTyLi = numRngTyps.listIterator();
         boolean found = false;
         while(numRngTyLi.hasNext() && !found){
          NumberRangeTypeRec currTy = numRngTyLi.next();
          LOGGER.log(INFO, "Type to be checked  has name: {0}", currTy.getName());
          if(StringUtils.equals(currTy.getName(), strVal)){
           cb.setNumberRangeType(currTy);
           found = true;
          }
         }
         LOGGER.log(INFO, "number range type {0}", cb.getNumberRangeType().getCode());
         break;
        case 4:
         cb.setShortDescr(strVal);
         break;
        case 5:
         cb.setLongDescr(strVal);
         break;
        case 9:
         ModuleRec mod = sysBuff.getModuleByCode(strVal);
         LOGGER.log(INFO, "Module is {0}", mod.getModuleCode());
         break;
         
       }
      
    }
   
    
     }

     if(cb.isAutoNum()){
      cb.setNextNum(cb.getFromNum());
     }
     LOGGER.log(INFO, "Create cheque bk ref {0} descr {1} from {2} to {3}", new Object[]{
      cb.getShortDescr(), cb.getLongDescr(), cb.getFromNum(),cb.getToNum()
     });
     LOGGER.log(INFO, "Chq Book for bank account {0}",cb.getBankAccountComp().getAccountNumber());
     this.setup.updateNumberRangeCheque(cb, getView());
   
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  } 
 }
 
 private void loadChequeVoidRsn(String fname){
  LOGGER.log(INFO, "loadChequeVoidRsn called with name {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   UserRec crUser = this.getLoggedInUser();
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    //For each row, iterate through each columns
    ChequeVoidReasonRec voidRsn = new ChequeVoidReasonRec();
    voidRsn.setCreatedBy(crUser);
    voidRsn.setCreatedDate(new Date());
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
     switch(cell.getCellType()) {
      case Cell.CELL_TYPE_BOOLEAN:
       LOGGER.log(INFO, "Cell index {0} Boolean cell value {1}", 
         new Object[]{cell.getColumnIndex(),cell.getBooleanCellValue()});
       voidRsn.setSysUse(cell.getBooleanCellValue());
       break;
      case Cell.CELL_TYPE_STRING:
       LOGGER.log(INFO, "Cell index {0} String cell value {1}", 
         new Object[]{cell.getColumnIndex(),cell.getStringCellValue()});
       if(cell.getColumnIndex() == 0){
        
        voidRsn.setCode(cell.getStringCellValue());
       }else if (cell.getColumnIndex() == 1){
        voidRsn.setDescription(cell.getStringCellValue());
       }
       break;
     }
    }
    LOGGER.log(INFO, " chq Void reason user{0}",voidRsn.getCreatedBy());
    LOGGER.log(INFO, "Create chq Void rsn with code {0} description {1} sys {2}", 
       new Object[]{voidRsn.getCode(),voidRsn.getDescription(),voidRsn.isSysUse()});
    voidRsn = this.basicConfigMgr.updateChequeVoidReason(voidRsn, getView());
    LOGGER.log(INFO, "Create  Void rsn id {0}",voidRsn.getId());
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
  
 }
 
 private void loadCompany(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "Company file used {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK){
     LOGGER.log(INFO, "No ref for row {0}",row.getRowNum());
     break;
    }
    LOGGER.log(INFO, "Cell types string {0}, number {1} bool {2} rich text {3} blank {4}", 
        new Object[]{Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_BOOLEAN, Cell.CELL_TYPE_BLANK});
    LOGGER.log(INFO, "1st Cell in row type {0}", row.getCell(0).getCellType());
    CompanyBasicRec comp;
    String code;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     code = row.getCell(0).getStringCellValue(); 
    }else{
     Double refNum = row.getCell(0).getNumericCellValue();
     LOGGER.log(INFO, "refNum {0}", refNum);
     code = Integer.toString(refNum.intValue());
    }
    LOGGER.log(INFO, "Value of 1st cell {0}", code);
    comp = this.sysBuff.getCompany(code);
    
    if(comp != null){
     LOGGER.log(INFO, "Company ref {0} already exists", comp.getReference());
     MessageUtil.addWarnMessage("compExists", "validationText");
     continue;
    }
    
   //For each row, iterate through each columns
    comp = new CompanyBasicRec();
    comp.setCreatedBy(currUsr);
    comp.setCreatedDate(new Date());
    AddressRec addr = new AddressRec();
    boolean addrLine = false;
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
      switch (cell.getColumnIndex()) {
       case 0:
        // comp ref numberic
        comp.setReference(String.valueOf(dval.intValue()));
        break;
       case 4:
        comp.setCompanyNumber(String.valueOf(dval.intValue()));
        comp.setCorp(true);
        break;
       case 8:
        LOGGER.log(INFO, "comp number {0}", comp.getCompanyNumber());
        if(comp.getCompanyNumber() != null && !comp.getCompanyNumber().isEmpty()){
         boolean dateCell = DateUtil.isCellDateFormatted(cell);
         if(dateCell){
          Date incorpDt = DateUtil.getJavaDate(dval);
          comp.setIncorporatedDate(incorpDt);
         }else{
          MessageUtil.addErrorMessage("compIncorpDt", "errorText");
          continue;
         }
        }
        break;
       case 9:
        comp.setCharityNumber(String.valueOf(dval.intValue()));
        break;
       default:
        break;
      }
        break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         comp.setReference(strVal);
        }else if(cell.getColumnIndex() == 1){
         comp.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         comp.setLegalName(strVal);
        }else if(cell.getColumnIndex() == 3){
         if(strVal.equals("Trust") || strVal.equals("Corporation") || 
                 strVal.equalsIgnoreCase("Individual")){
          comp.setCompanyType(strVal);
         }else{
          MessageUtil.addErrorMessage("compType", "errorText");
          continue;
         }
        }else if(cell.getColumnIndex() == 4){
         comp.setCompanyNumber(strVal);
         comp.setCorp(true);
        }else if(cell.getColumnIndex() == 5){
         boolean defaultComp = (strVal.equals("Y")?true:false);
         comp.setDefaultCompany(defaultComp);
        }else if(cell.getColumnIndex() == 6){
         boolean vatReg = (strVal.equals("Y")?true:false);
         comp.setVatReg(vatReg);
        }else if(cell.getColumnIndex() == 7){
         boolean charity = (strVal.equals("Y")?true:false);
         comp.setCharity(charity);
        }else if(cell.getColumnIndex() == 9){
         comp.setCharityNumber(strVal);
        }else if(cell.getColumnIndex() == 10){
         LOGGER.log(INFO, "Char of accounts string {0}", strVal);
         ChartOfAccountsRec chart = this.compMgr.getChartOfAccountByRef(strVal);
         LOGGER.log(INFO, "Chart from compGr {0}", chart);
         LOGGER.log(INFO, "Chart id {0}", chart.getId());
         comp.setChartOfAccounts(chart);
        }else if(cell.getColumnIndex() == 11){
         FisPeriodRuleRec fisPer = this.sysBuff.getFisPeriodRuleByName(strVal);
         comp.setPeriodRule(fisPer);
        }else if(cell.getColumnIndex() == 12){
         boolean fundAcnt = (strVal.equals("Y")?true:false);
         comp.setFundAccounting(fundAcnt);
        }else if(cell.getColumnIndex() == 13){
         CountryRec cntry = mdm.getCountryByRef2(strVal);
         comp.setCountry(cntry);
        }else if(cell.getColumnIndex() == 14){
         CurrencyRec curr = mdm.getCurrencyByCode(strVal);
         comp.setCurrency(curr);
        }else if(cell.getColumnIndex() == 15){
         String localLangTag = strVal.replace('_', '-');
         Locale loc = Locale.forLanguageTag(localLangTag);
         comp.setLocale(loc);
        }else if(cell.getColumnIndex() == 16){
         addr.setStreet(strVal);
         addrLine = true;
        }else if(cell.getColumnIndex() == 17){
         if(addr.getStreet() != null){
          AddressRec addrStored = this.mdm.getAddressByStreetPostCode(addr.getStreet(), strVal);
          if(addrStored != null){
           addr = addrStored;
          }else{
           addr.setPostCode(strVal);
          }
         }else{
          addr.setPostCode(strVal);
         }
         addrLine = true;
        }else if(cell.getColumnIndex() == 18){
         addr.setTown(strVal);
         addrLine = true;
        }else if(cell.getColumnIndex() == 19){
         addr.setCountyName(strVal);
         addrLine = true;
        }
         
        
       break;
      }
     }
     if(addrLine){
      comp.setAddress(addr);
     }
     
     LOGGER.log(INFO, "Save comp ref {0}", comp.getReference());
     LOGGER.log(INFO, "comp chart {0}",comp.getChartOfAccounts());
     LOGGER.log(INFO, "comp fisc per rule {0}", comp.getPeriodRule());
     comp = this.compMgr.createCompany(comp, currUsr, getView());
    
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
  
 }
 
 private void loadCompApAr(String fname){
  LOGGER.log(INFO, "Comp ar ap settings per file used {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    if(row.getRowNum() == 0 ){
     //dont process title row
     continue;
    }
    CompanyBasicRec comp;
    String compRef = null;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     compRef = row.getCell(0).getStringCellValue();
    }else if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double cmpRefDbl = row.getCell(0).getNumericCellValue();
     compRef = String.valueOf(cmpRefDbl.intValue());
    }
    LOGGER.log(INFO, "compRef from spreadsheet {0}", compRef);
    if(compRef == null){
     LOGGER.log(INFO, "No company for row {0}", row.getRowNum());
     continue;
    }
    comp = sysBuff.getCompany(compRef);
    LOGGER.log(INFO, "Company ref from sys buff {0}", comp.getReference());
    if(comp == null){
     LOGGER.log(INFO, "company for ref {0} not found", compRef);
     continue;
    }
    CompanyApArRec arAp = new CompanyApArRec();
    arAp.setCreatedBy(this.getLoggedInUser());
    arAp.setCreatedDate(new Date());
    arAp.setComp(comp);
    //For each row, iterate through each column
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()){
     Cell cell = cellIterator.next();
     
     switch(cell.getCellType()) {
      case Cell.CELL_TYPE_BOOLEAN:
       LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
       break;
      case Cell.CELL_TYPE_NUMERIC:
       LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
       Double dval = cell.getNumericCellValue();
       
       switch(cell.getColumnIndex()){
        case 1:
         arAp.setApBucket1(dval.intValue());
         break;
        case 2:
         arAp.setApBucket2(dval.intValue());
         break;
        case 3:
         arAp.setApBucket3(dval.intValue());
         break;
        case 4:
         arAp.setApBucket4(dval.intValue());
         break;
        case 5:
         arAp.setArBucket1(dval.intValue());
         break;
        case 6:
         arAp.setArBucket2(dval.intValue());
         break;
        case 7:
         arAp.setArBucket3(dval.intValue());
         break;
        case 8:
         arAp.setArBucket4(dval.intValue());
         break;
        }
       break;
      case Cell.CELL_TYPE_STRING:
      LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
      break;
     }
     
    }
    arAp = compMgr.updateArApRec(arAp, this.getView());
    LOGGER.log(INFO, "Updated Comp ArAp id{0} comp ref {1}", new Object[]{arAp.hashCode(),arAp.getComp().getReference()});
   }
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
  
 }
 private void loadCompPostPer(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "Comp post per file used {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK){
     LOGGER.log(INFO, "No ref for row {0}",row.getRowNum());
     break;
    }
    LOGGER.log(INFO, "Cell types string {0}, number {1} bool {2} rich text {3} blank {4}", 
        new Object[]{Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_BOOLEAN, Cell.CELL_TYPE_BLANK});
    LOGGER.log(INFO, "1st Cell in row type {0}", row.getCell(0).getCellType());
    
   
    
    CompanyBasicRec comp;
    String compRef = null;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     compRef = row.getCell(0).getStringCellValue();
    }else if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double cmpRefDbl = row.getCell(0).getNumericCellValue();
     compRef = String.valueOf(cmpRefDbl.intValue());
    }
    
    if(compRef == null){
     LOGGER.log(INFO, "No company for row {0}", row.getRowNum());
     continue;
    }
    comp = this.sysBuff.getCompany(compRef);
    if(comp == null){
     LOGGER.log(INFO, "company for ref {0} not found", compRef);
     continue;
    }
    
    String ledCode = null;
    if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING){
     ledCode = row.getCell(1).getStringCellValue();
    }else if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double ledConDbl = row.getCell(1).getNumericCellValue();
     ledCode = String.valueOf(ledConDbl.intValue());
    }
    LOGGER.log(INFO, "ledCode {0}", ledCode);
    LedgerRec led = this.sysBuff.getLedgerByName(ledCode);
    if(led == null){
     LOGGER.log(INFO, "ledger not found for code {0}", led);
     continue;
    }
    LOGGER.log(INFO, "Ledger from sys buff {0}", led);
    
    CompPostPerRec postPer = this.compMgr.getCompPostPerByCompLed(comp, led);
    if(postPer != null){
     LOGGER.log(INFO, "Post Per found comp {0} led {1}", 
             new Object[]{comp.getReference(),led.getCode()});
     continue;
    }
    
    postPer = new CompPostPerRec();
    postPer.setCreatedBy(currUsr);
    postPer.setCreatedDate(new Date());
    postPer.setComp(comp);
    postPer.setLedger(led);
   
    
    
   //For each row, iterate through each columns
    
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch(cell.getColumnIndex()){
         case 2:
          postPer.setStartYear(dval.intValue());
          break;
         case 3:
          postPer.setStartPer(dval.intValue());
          break;
         case 4:
          postPer.setEndYear(dval.intValue());
          break;
         case 5:
          postPer.setEndPer(dval.intValue());
          
        }
        break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        break;
      }
     }
     
     String startPer = postPer.getStartPer() < 10? "0"+postPer.getStartPer():String.valueOf(postPer.getStartPer());
     String startLongStr = String.valueOf(postPer.getStartYear()) + startPer;
     postPer.setStartLong(Integer.parseInt(startLongStr));
     
     String endPer = postPer.getEndPer() < 10? "0"+postPer.getEndPer():String.valueOf(postPer.getEndPer());
     String endLongStr = String.valueOf(postPer.getEndYear()) + endPer;
     postPer.setEndLong(Integer.parseInt(endLongStr));
             
    LOGGER.log(INFO, "Create period post for comp {0} startlong {1} endlong {2} ", new Object[]{
     postPer.getComp().getReference(), postPer.getStartLong(), postPer.getEndLong()
    });
    postPer = this.compMgr.updateCompPostPer(postPer, getView());
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
  
 }
 private void loadContactRole(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "Company file used {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK){
     LOGGER.log(INFO, "No ref for row {0}",row.getRowNum());
     break;
    }
    String contactRoleName = null;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     contactRoleName = row.getCell(0).getStringCellValue();
    }
    ContactRoleRec cr = null;
    if(contactRoleName != null){
     cr = this.setup.getContactRoleByName(contactRoleName);
    }
    if(cr != null){
     continue;
    }else{
     cr = new ContactRoleRec();
    }
    // process each column
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
     switch(cell.getCellType()){
      case Cell.CELL_TYPE_STRING:
       String strVal = cell.getStringCellValue();
       switch(cell.getColumnIndex()){
        case 0:
         cr.setName(strVal);
         break;
        case 1:
         cr.setDescription(strVal);
         break;
        case 2:
         boolean in = strVal.equalsIgnoreCase("Y")?true:false;
         cr.setInbound(in);
         break;
       }
       
     }
    }
    cr.setCreatedBy(currUsr);
    cr.setCreatedOn(new Date());
    cr = setup.updateContactRole(cr, src);
    LOGGER.log(INFO, "Contract Role created with id {0}", cr.getId());
     
    
   }
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 private void loadCostCent(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "Company file used {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0).getCellType()== Cell.CELL_TYPE_BLANK){
     LOGGER.log(INFO, "No ref for row {0}",row.getRowNum());
     break;
    }
    LOGGER.log(INFO, "Cell types string {0}, number {1} bool {2} rich text {3} blank {4}", 
        new Object[]{Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_BOOLEAN, Cell.CELL_TYPE_BLANK});
    LOGGER.log(INFO, "1st Cell in row type {0}", row.getCell(0).getCellType());
    
    String ccRef = null;
    CostCentreRec costCent = null;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     ccRef = row.getCell(0).getStringCellValue();
    }
    
    CompanyBasicRec comp;
    String compRef = null;
    if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING){
     compRef = row.getCell(1).getStringCellValue();
    }else if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double cmpRefDbl = row.getCell(1).getNumericCellValue();
     compRef = String.valueOf(cmpRefDbl.intValue());
    }
    
    if(compRef == null){
     LOGGER.log(INFO, "No company for cc ref {0}", ccRef);
     continue;
    }
    comp = this.sysBuff.getCompany(compRef);
    if(comp == null){
     LOGGER.log(INFO, "company for ref {0} not found", compRef);
     continue;
    }
    
    costCent = this.costCentMgr.getCostCentreByRef(comp, ccRef);
    if(costCent != null){
     LOGGER.log(INFO, "Cost cent ref {0} already exists ", ccRef);
     continue;
    }
    
    
    costCent = new CostCentreRec();
    costCent.setCreatedBy(currUsr);
    costCent.setCreatedOn(new Date());
    costCent.setRefrence(ccRef);
    costCent.setCostCentreForCompany(comp);
    
    PartnerPersonRec resp = new PartnerPersonRec();
    boolean addResp = false;
    
   //For each row, iterate through each columns
    
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch(cell.getColumnIndex()){
         case 3:
          boolean dateCell = DateUtil.isCellDateFormatted(cell);
          if(dateCell){
           Date fromDt = DateUtil.getJavaDate(dval);
           costCent.setValidFrom(fromDt);
          }
          break;
         case 4:
          dateCell = DateUtil.isCellDateFormatted(cell);
          if(dateCell){
           Date toDt = DateUtil.getJavaDate(dval);
           costCent.setValidFrom(toDt);
          }
          break;
          
        }
        
        
        
        break;
        
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        switch(cell.getColumnIndex()){
         case 2:
          costCent.setCostCentreName(strVal);
          break;
         case 5:
          resp.setTitle(strVal);
          addResp = true;
          break;
         case 6:
          resp.setFirstName(strVal);
          addResp = true;
          break;
         case 7:
          resp.setFamilyName(strVal);
          addResp = true;
          break;
        }
         
        
       break;
      }
     }
    if(addResp){
     List<PartnerPersonRec> perslist = this.mdm.getIndivPtnrsBySurname(resp.getFamilyName());
     if(perslist != null && !perslist.isEmpty()){
      boolean foundPers = false;
      ListIterator<PartnerPersonRec> persLi = perslist.listIterator();
      while(persLi.hasNext() && !foundPers){
       PartnerPersonRec pers = persLi.next();
       if(pers.getTitle().equals(resp.getTitle()) &&
          pers.getFirstName().equals(resp.getFirstName())){
        resp.setId(pers.getId());
        foundPers = true;
       }
      }
     }
     costCent.setResponsibilityOf(resp);
     
    } 
    LOGGER.log(INFO, "Create cost centr ref {0}", costCent.getRefrence());
    this.costCentMgr.updateCostCentre(costCent, currUsr, src);
     
   }
   
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
  
 }
 
 private void loadCountryRec(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCountryRec called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0 ){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    
    CountryRec cntry; 
    boolean foundCntry = false;
    if(countries != null){
    ListIterator<CountryRec> cntryLi = countries.listIterator();
    while(cntryLi.hasNext() && !foundCntry ){
     CountryRec c = cntryLi.next();
     if(c.getCountryCode3().equals(code)){
      foundCntry = true;
     }
    }
    }
    if(foundCntry ){
     LOGGER.log(INFO, "Country with code {0} found ", code);
     continue;
    }
    
   //For each row, iterate through each columns
    cntry = new CountryRec();
    cntry.setCreatedBy(currUsr);
    cntry.setCreatedOn(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Colun Number {0} ", new Object[]{cell.getColumnIndex()});
      CellType ct = cell.getCellTypeEnum();
      if(ct == CellType.NUMERIC){
       LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 4){
         cntry.setCountryNumeric(dval.intValue());
        }else if(cell.getColumnIndex() == 9){
         
         cntry.setDialCd(String.valueOf(dval.intValue()));
        }
        else if(cell.getColumnIndex() == 16){
         cntry.setFractionalLength(dval.intValue());
        }else if(cell.getColumnIndex() == 18){
         cntry.setCurrNumCode(dval.intValue());
        }   
      }else if(ct == CellType.STRING){
       LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
       switch (cell.getColumnIndex()) {
        case 0:
         cntry.setCountryCode3(strVal);
         break;
        case 1:
         cntry.setCountryName(strVal);
         break;
        case 2:
         cntry.setCountryNameFr(strVal);
         break;
        case 3:
         cntry.setCountryCode2(strVal);
         break;
        case 9:
         cntry.setDialCd(strVal);
         break;
        case 14:
         cntry.setCurrCode(strVal);
         break;
        case 17:
         cntry.setCurrName(strVal);
         break;
        case 19:
         boolean indep = strVal.equalsIgnoreCase("Yes");
         cntry.setIndependent(indep);
         LOGGER.log(INFO, "strVal indep {0}", strVal);
         LOGGER.log(INFO, "indep {0}", indep);
         break;
        default:
         break;
       }
      }
      
     }
     LOGGER.log(INFO, "Need to update line code {0} descr {1}", new Object[]{cntry.getCountryCode2(),cntry.getCountryName()});
    
     mdm.countryUpdate(cntry, currUsr, src);
   
   }
   boolean rc = mdm.userDefaultCountryUpdate("GB", getLoggedInUser(), getView());
   LOGGER.log(INFO, "Users updated with default country");
  }catch(IOException ex ){
   LOGGER.log(INFO, "Could not process file {0}", fname);
  }
 }
 
 private void loadCurrency(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadCurrency called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    
    CurrencyRec curr = this.mdm.getCurrencyByCode(code);
    if(curr != null){
     LOGGER.log(INFO, "Line type with code {0} found ", code);
     continue;
    }
   //For each row, iterate through each columns
    curr = new CurrencyRec();
    curr.setCreatedBy(currUsr);
    curr.setCreatedOn(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 3){
         curr.setMinorUnit(dval.intValue());
        }if(cell.getColumnIndex() == 4){
         curr.setCurrNumCode(dval.intValue());
        }
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         curr.setCurrAlphaCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         curr.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         curr.setCurrSymbol(strVal);
        }
        
       break;
      }
     }
     
    LOGGER.log(INFO, "Need to update curr code {0} name {1} symbol {2} minor {3} num {4}", 
            new Object[]{curr.getCurrAlphaCode(),curr.getName(),curr.getCurrSymbol(),
            curr.getMinorUnit(),curr.getCurrNumCode()});
    
    curr = mdm.updateCurrencyRec(curr, src);
    // setup.updateLineType(lnTy, src);
     //sysBuff.updateDocType(docTy, src);
   
   }
   
  }catch(IOException ex ){
   
  }
 }
 
 private void loadDocRevReason(String fname){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  UserRec currUsr = getLoggedInUser();
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
     if(row.getRowNum() == 0){
     continue;
    }
    DocReversalReasonRec docRev = new DocReversalReasonRec();
    docRev.setCreatedBy(currUsr);
    docRev.setCreatedDate(new Date());
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
     LOGGER.log(INFO, "cell type {0}", cell.getCellType());
     switch(cell.getCellType()) {
      case Cell.CELL_TYPE_BOOLEAN:
       docRev.setSysUse(cell.getBooleanCellValue());
       break;
      case Cell.CELL_TYPE_STRING:
       if(cell.getColumnIndex() == 0){
        docRev.setCode(cell.getStringCellValue());
       }else if(cell.getColumnIndex() == 1){
        docRev.setDescription(cell.getStringCellValue());
       }
     }
    }
    LOGGER.log(INFO, "doc rev code {0} descr {1} sys use {2}", 
      new Object[]{docRev.getCode(),docRev.getDescription(),docRev.isSysUse()});
    docRev = this.basicConfigMgr.updateDocReversalReason(docRev,getView());
    LOGGER.log(INFO, "Load feadults docRev id {0}", docRev.getId());
   }
   
  }catch(IOException ex ){
   MessageUtil.addErrorMessageParam1("defConfigFn", "errotText", fname);
  }
 }
 private void loadDocTypeRec(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadDocType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    DocTypeRec docTy = sysBuff.getDocTypeByCode(code);
    if(docTy != null){
     LOGGER.log(INFO, "Line type with code {0} found ", code);
     continue;
    }
   //For each row, iterate through each columns
    docTy = new DocTypeRec();
    docTy.setCreatedBy(currUsr);
    docTy.setCreated(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         docTy.setCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         docTy.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         LOGGER.log(INFO, "AP ");
         boolean allowed = (strVal.equalsIgnoreCase("y") ? true:false);
         docTy.setApAllowed(allowed);
        }else if(cell.getColumnIndex() == 3){
         LOGGER.log(INFO, "AR ");
         boolean allowed = (strVal.equalsIgnoreCase("Y") ? true:false);
         docTy.setArAllowed(allowed);
        }else if(cell.getColumnIndex() == 4){
         LOGGER.log(INFO, "GL ");
         boolean allowed = (strVal.equalsIgnoreCase("Y") ? true:false);
         docTy.setGlAllowed(allowed);
        }else if(cell.getColumnIndex() == 5){
         LOGGER.log(INFO, "TR ");
         boolean allowed = (strVal.equalsIgnoreCase("Y") ? true:false);
         docTy.setTrAllowed(allowed);
        }
        
       break;
      }
     }
     LOGGER.log(INFO, "Need to update line code {0} descr {1}", new Object[]{docTy.getCode(),docTy.getName()});
    // setup.updateLineType(lnTy, src);
     sysBuff.updateDocType(docTy, src);
   
   }
   
  }catch(IOException ex ){
   
  }
 }

 public boolean isShowProgress() {
  return showProgress;
 }

 public void setShowProgress(boolean showProgress) {
  this.showProgress = showProgress;
 }
 
 
 private void loadFiscalCalRule(String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadFiscalCalRule called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    
    FisPeriodRuleRec rule = sysBuff.getFisPeriodRuleByName(code);
    if(rule != null){
     LOGGER.log(INFO, "Line type with code {0} found ", code);
     continue;
    }
   //For each row, iterate through each columns
    rule = new FisPeriodRuleRec();
    rule.setCreateBy(currUsr);
    rule.setCreateDate(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      LOGGER.log(INFO, "Cell index {0}", cell.getColumnIndex());
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 2){
         rule.setNumPeriods(dval.intValue());
        } else if(cell.getColumnIndex() == 3){
         rule.setNumAuditPeriods(dval.intValue());
        }
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        String strVal = cell.getStringCellValue();
        LOGGER.log(INFO, "String value {0}", strVal);
        if(cell.getColumnIndex() == 0){
         rule.setPeriodRule(strVal);
        }else if(cell.getColumnIndex() == 1){
         rule.setPeriodDescr(strVal);
        }else if(cell.getColumnIndex() == 4){
         boolean calBasis = (strVal.equals("Y")?true:false);
         rule.setCalendarMonthBasis(calBasis);
        }else if(cell.getColumnIndex() == 5){
         boolean calBasis = (strVal.equals("Y")?true:false);
         rule.setCalendarNatural(calBasis);
        }else if(cell.getColumnIndex() == 6){
         boolean fixedDayBasis = (strVal.equals("Y")?true:false);
         rule.setFixedDayOfMonthBasis(fixedDayBasis);
        }else if(cell.getColumnIndex() == 7){
         boolean perlenBasis = (strVal.equals("Y")?true:false);
         rule.setFixedlen(perlenBasis);
        }else if(cell.getColumnIndex() == 8){
         boolean flexBasis = (strVal.equals("Y")?true:false);
         rule.setAnnualDateScheduleBasis(flexBasis);
        }else if(cell.getColumnIndex() == 9){
         LOGGER.log(INFO, "Cal Rule: {0}", strVal);
         CalendarRuleBaseRec ruleRec = compMgr.getCalendarRuleByRef(strVal);
         LOGGER.log(INFO, "Rule from compMgr {0}", ruleRec);
         if(ruleRec != null){
          rule.setCalendarRule(ruleRec);
         }
        }else if(cell.getColumnIndex() == 10){
         LOGGER.log(INFO, "Annual flex Rule: {0}", strVal);
        }
        
        
        
       break;
      }
     }
    // LOGGER.log(INFO, "Need to update ledger code {0} descr {1}", new Object[]{led.getCode(),led.getDescr()});
    // setup.updateLedger(led, src);
     LOGGER.log(INFO, "rule calendar {0}", rule.getCalendarRule());
     
     rule = compMgr.updateFiscalCalendar(rule, src);
     LOGGER.log(INFO, "end of row {0}", rule);
   
   }
   
  }catch(IOException ex ){
   
  }
 }
 
 private void loadFund
         (String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadFund called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    String compRef;
    switch (row.getCell(1).getCellTypeEnum()) {
     case NUMERIC:
      Double dVal = row.getCell(1).getNumericCellValue();
      compRef = String.valueOf(dVal.intValue());
      break;
     case STRING:
      compRef = row.getCell(1).getStringCellValue();
      break;
     default:
      LOGGER.log(INFO, "No company row {0}", row.getRowNum() + 1);
      continue;
    }
    LOGGER.log(INFO, "compRef {0}", compRef);
            
    CompanyBasicRec comp = this.sysBuff.getCompany(compRef);
    LOGGER.log(INFO, "comp from sysBuff {0}", comp);
    
    FundRec fnd = this.compMgr.getFundByCodeComp(code, comp.getId());
    if(fnd != null){
     LOGGER.log(INFO, "Fund category with ref {0} found ", code);
     continue;
    }
    
    fnd = new FundRec();
    fnd.setCreatedBy(currUsr);
    fnd.setCreatedOn(new Date());
   //For each row, iterate through each columns
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      LOGGER.log(INFO, "Cell index {0}", cell.getColumnIndex());
      
      switch(cell.getCellTypeEnum()) {
       case BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        fnd.setReturnRequired(cell.getBooleanCellValue());
        break;
       case NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch (cell.getColumnIndex()){
         case 1:
          fnd.setFundForComp(comp);
          LOGGER.log(INFO, "numeric set Comp {0}", fnd.getFundForComp().getReference());
          break;
        }
        
        break;
       case STRING:
        String strVal = cell.getStringCellValue();
        LOGGER.log(INFO, "String value {0}", strVal);
       switch (cell.getColumnIndex()){
        case 0:
         fnd.setFndCode(cell.getStringCellValue());
         break;
        case 1:
         fnd.setFundForComp(comp);
         LOGGER.log(INFO, "String set Comp {0}", fnd.getFundForComp().getReference());
         break;
        case 2:
         fnd.setName(cell.getStringCellValue());
         break;
        case 3:
         fnd.setPurpose(cell.getStringCellValue());
         break;
        case 7:
         boolean req = cell.getStringCellValue().equals("Y")?true:false;
         fnd.setReturnRequired(req);
         break;
        case 8:
         FundCategoryRec fndCat = this.setup.getFundCategoryByRef(cell.getStringCellValue());
         LOGGER.log(INFO, "Fund cat found by ref {0}", fndCat);
         if(fndCat == null){
          continue;
         }
         fnd.setFundCategory(fndCat);
         break;
       }
        
        break;
       default:
        if(DateUtil.isCellDateFormatted(cell)){
         switch (cell.getColumnIndex()){
          case 4:
           fnd.setValidFrom(cell.getDateCellValue());
           break;
          case 5:
           fnd.setValidTo(cell.getDateCellValue());
           break;
          case 6:
           fnd.setReturnDue(cell.getDateCellValue());
           break;
         }
       }
      }
     }
     
     LOGGER.log(INFO, "Need to update Fund code {0} descr {1}", new Object[]{fnd.getFndCode(),fnd.getFundForComp().getReference()});
     LOGGER.log(INFO, "Fund Category {0}", fnd.getFundCategory().getDescription());
     this.compMgr.updateRestrictedFund(fnd, src);
    // setup.updateLedger(led, src);
     //fndCat = setup.updateFundCategory(fndCat, getView());
     LOGGER.log(INFO, "save fund id {0}", fnd.getFndCode());
   
   }
   
  }catch(IOException ex ){
   
  }
 }
 
 private void loadFundType(String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadFundType called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    FundCategoryRec fndCat = setup.getFundCategoryByRef(code);
    if(fndCat != null){
     LOGGER.log(INFO, "Fund category with ref {0} found ", code);
     continue;
    }
    
    fndCat = new FundCategoryRec();
    fndCat.setCreatedBy(currUsr);
    fndCat.setCreatedOn(new Date());
   //For each row, iterate through each columns
    
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      LOGGER.log(INFO, "Cell index {0}", cell.getColumnIndex());
      
      switch(cell.getCellTypeEnum()) {
       case BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        boolean bVal = cell.getBooleanCellValue();
        switch (cell.getColumnIndex()){
         case 2:
          fndCat.setRestricted(bVal);
          LOGGER.log(INFO, "Fund Type restricted {0}", String.valueOf(fndCat.isRestricted()));
          break;
         case 3:
          fndCat.setDesignated(bVal);
          LOGGER.log(INFO, "Fund Type designated {0}", String.valueOf(fndCat.isDesignated()));
          break;
         case 4:
           fndCat.setEndowment(bVal);
           LOGGER.log(INFO, "Fund Type Endowment {0}", String.valueOf(fndCat.isEndowment()));
           break;
         case 5:
           fndCat.setPermanent(bVal);
           LOGGER.log(INFO, "Fund Type Permanent {0}", String.valueOf(fndCat.isPermanent()));
           break;
        }
        break;
       case NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        break;
       case STRING:
        String strVal = cell.getStringCellValue();
        LOGGER.log(INFO, "String value {0}", strVal);
        switch (cell.getColumnIndex()){
         case 0:
          fndCat.setCatRef(strVal);
          break;
         case 1:
          fndCat.setDescription(strVal);
          break;
         case 6:
          fndCat.setProcessCode(strVal);
          break;
       }
        
       break;
      }
     }
    // LOGGER.log(INFO, "Need to update ledger code {0} descr {1}", new Object[]{led.getCode(),led.getDescr()});
    // setup.updateLedger(led, src);
     fndCat = setup.updateFundCategory(fndCat, getView());
     LOGGER.log(INFO, "saved fund cat id {0}", fndCat.getId());
   
   }
   
  }catch(IOException ex ){
   
  }
 }
 
 private void loadGlAcntChart(String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadGlAcntChart called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   LOGGER.log(INFO, "resource is {0}", is);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     
     break;
    }
    FiGlAccountBaseRec glAcnt = null;
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
    Double acntNum = row.getCell(0).getNumericCellValue();
    glAcnt = glAcntMgr.getGlAccountByRef(String.valueOf(acntNum.intValue()));
    }else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
     glAcnt = glAcntMgr.getGlAccountByRef(row.getCell(0).getStringCellValue());
    }
    
    if(glAcnt != null){
     LOGGER.log(INFO, "GL Account with code found process next ");
     continue;
    }
   //For each row, iterate through each columns
    String glAccntTypeCode = ((Cell)row.getCell(5)).getStringCellValue();
    AccountTypeRec acntTy = sysBuff.getAcntTypeByProcCode(glAccntTypeCode);
    if(acntTy == null){
     LOGGER.log(INFO, "Account type not found for {0}", glAccntTypeCode);
     continue;
    }
    boolean pl = acntTy.isProfitAndLossAccount();
    if(pl){
     glAcnt = new FiPlAccountRec();
    }else{
     glAcnt = new FiBsAccountRec();
    }
    glAcnt.setCreatedBy(currUsr);
    glAcnt.setCreatedOn(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Column index {0}", cell.getColumnIndex());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        if(cell.getColumnIndex() == 0){
         glAcnt.setRef(String.valueOf(dval.intValue()));
        }
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
      switch (cell.getColumnIndex()) {
       case 0:
        glAcnt.setRef(strVal);
        break;
       case 1:
        glAcnt.setDescription(strVal);
        break;
       case 2:
        glAcnt.setName(strVal);
        break;
       case 3:
        ChartOfAccountsRec chart = this.compMgr.getChartOfAccountByRef(strVal);
        glAcnt.setChartOfAccounts(chart);
        LOGGER.log(INFO, "Chart from sys buff {0}", chart);
        continue;
       case 4:
        boolean plB = (strVal.equals("Y"));
        glAcnt.setPl(plB);
        LOGGER.log(INFO, "PL from sys buff {0}", plB);
        break;
       case 5:
        acntTy = sysBuff.getAcntTypeByProcCode(strVal);
        glAcnt.setAccountType(acntTy);
        LOGGER.log(INFO, "Account type frpm sys buff {0}", acntTy);
        break;
       default:
        break;
      }
        
               
       break;
      }
     }
     LOGGER.log(INFO, "Need to create GL accouny ref {0} descr {1}", 
             new Object[]{glAcnt.getRef(),glAcnt.getDescription()});
     this.glAcntMgr.updateGlAccountCoa(glAcnt, this.getLoggedInUser(), getView());
     
   
   }
  /*
  }catch(RecordFormatException ex){
   LOGGER.log(INFO, "REcord format exception {0}", ex.getLocalizedMessage());
   
   
   
  } */
  }catch(IOException ex ){
   LOGGER.log(INFO, "IO exception {0}", ex.getLocalizedMessage());
  }

 }
 
 private void loadGlAcntComp(String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadGlAcntComp called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   LOGGER.log(INFO, "resource is {0}", is);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     
     break;
    }
    int cellType = row.getCell(0).getCellType();
    int compCellType = row.getCell(1).getCellType();
    if(cellType == Cell.CELL_TYPE_BLANK || compCellType == Cell.CELL_TYPE_BLANK){
     MessageUtil.addWarnMessage("glAcntRefComp", "validationText");
     continue;
    }
    
    String acntRef;
    String compRef;
    if(cellType == Cell.CELL_TYPE_NUMERIC){
     Double acntNum = row.getCell(0).getNumericCellValue();
     acntRef = String.valueOf(acntNum.intValue());
    } else{
     acntRef = row.getCell(0).getStringCellValue();
    }
    
    if(compCellType == Cell.CELL_TYPE_NUMERIC){
     Double compNum = row.getCell(1).getNumericCellValue();
     compRef = String.valueOf(compNum.intValue());
    }else{
     compRef = row.getCell(1).getStringCellValue();
    }
    CompanyBasicRec comp = this.sysBuff.getCompany(compRef);
    LOGGER.log(INFO, "Company from syst buff {0}", comp);
    if(comp == null){
     return;
    }
    FiGlAccountCompRec glAcnt = glAcntMgr.getGlAccountForComp(acntRef, comp);
    
    if(glAcnt != null){
     LOGGER.log(INFO, "GL Account with code {0} found ", acntRef);
     continue;
    }
   //For each row, iterate through each columns
    glAcnt = new FiGlAccountCompRec();
    glAcnt.setCreatedBy(currUsr);
    glAcnt.setCreatedOn(new Date());
    FiGlAccountBaseRec coaAcnt = this.glAcntMgr.getGlCoaAccountByRef(acntRef, comp.getChartOfAccounts());
   LOGGER.log(INFO, "coaAcnt from glAcntMgr {0}", coaAcnt);
   if(coaAcnt == null){
    MessageUtil.addWarnMessageParam("glAcntNoCoaAcnt", "errorText", acntRef );
    continue;
   }
    glAcnt.setCoaAccount(coaAcnt);
    glAcnt.setCompany(comp);
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "Column index {0}", cell.getColumnIndex());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 2){
         SortOrderRec so = sysBuff.getSortOrderByCode(strVal);
         glAcnt.setSortOrder(so);
         LOGGER.log(INFO, "Account sort order {0}", glAcnt.getSortOrder());
        }else if(cell.getColumnIndex() == 3){
         glAcnt.setAnalysis1(strVal);
        } else if(cell.getColumnIndex() == 4){
         glAcnt.setAnalysis2(strVal);
        }else if(cell.getColumnIndex() == 5){
         glAcnt.setRepCategory(strVal);
        }else if(cell.getColumnIndex() == 6){
         // vat code
         VatCodeCompanyRec vatCd = this.sysBuff.getVatCodeCompany(strVal, comp);
         glAcnt.setVatCode(vatCd);
        }else if(cell.getColumnIndex() == 7){
         boolean noVatAll = (strVal.equals("Y")?true:false);
         glAcnt.setNoVatAllowed(noVatAll);
        }else if(cell.getColumnIndex() == 8){
         boolean clearing = (strVal.equals("Y")?true:false);
         glAcnt.setAccountClearing(clearing);
        }else if(cell.getColumnIndex() == 9){
         BankAccountCompanyRec bnk = this.bankMgr.getBankAccountForCompany(acntRef, comp);
         glAcnt.setBankAccountCompanyCleared(bnk);
        }else if(cell.getColumnIndex() == 10){
         BankAccountCompanyRec bnk = this.bankMgr.getBankAccountForCompany(acntRef, comp);
         glAcnt.setBankAccountCompanyCleared(bnk);
        }
        
         
        
               
       break;
      }
     }
    LOGGER.log(INFO, "Need to create GL account ref {0} descr {1}", 
             new Object[]{glAcnt.getCoaAccount().getRef(),glAcnt.getSortOrder().getSortCode()});
    glAcnt = glAcntMgr.updateGlAccountComp(glAcnt,  this.getView());
    LOGGER.log(INFO, "glAcnt id {0}", glAcnt.getId());
   
   }
   
/*  }catch(RecordFormatException ex){
   LOGGER.log(INFO, "REcord format exception {0}", ex.getLocalizedMessage());
   
   
   */
  }catch(IOException ex ){
   LOGGER.log(INFO, "IO exception {0}", ex.getLocalizedMessage());
  }
 }
 private void loadLedger(String fname,  UserRec currUsr, String src){
  LOGGER.log(INFO, "loadLedger called with file {0}",fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    
    String code = row.getCell(0).getStringCellValue();
    
    LedgerRec led = sysBuff.getLedgerByName(code);
    if(led != null){
     LOGGER.log(INFO, "Line type with code {0} found ", code);
     continue;
    }
   //For each row, iterate through each columns
    led = new LedgerRec();
    led.setCreatedBy(currUsr);
    led.setCreatedDate(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         led.setCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         led.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         led.setDescr(strVal);
         
        }
        
       break;
      }
     }
     LOGGER.log(INFO, "Need to update ledger code {0} descr {1}", new Object[]{led.getCode(),led.getDescr()});
     setup.updateLedger(led, src);
   
   }
   
  }catch(IOException ex ){
   
  }
  
  
 }
 
 private void loadLineTypes(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadLineType called with file {0}",fname);
  if(modules == null){
   modules = setup.getAllModules();
  }
  LOGGER.log(INFO, "modules {0}", modules);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  
  //process spreadsheet
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String code = row.getCell(0).getStringCellValue();
    LineTypeRuleRec lnTy = setup.getLineTypeByCode(code);
    if(lnTy != null){
     LOGGER.log(INFO, "Line type with code {0} found ", code);
     continue;
    }
   //For each row, iterate through each columns
    lnTy = new LineTypeRuleRec();
    lnTy.setCreatedBy(currUsr);
    lnTy.setCreatedDate(new Date());
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         lnTy.setLineCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         lnTy.setDescription(strVal);
        }else if(cell.getColumnIndex() == 2){
         LOGGER.log(INFO, "modules {0}", modules);
         boolean modFound = false;
         ListIterator<ModuleRec> modLi = modules.listIterator();
         while(modLi.hasNext() && !modFound){
          ModuleRec modRec = modLi.next();
          if(modRec.getModuleCode().equals(strVal)){
           lnTy.setModule(modRec);
           modFound = true;
          }
         }
        }
        
       break;
      }
     }
     LOGGER.log(INFO, "Need to update line code {0} descr {1}", new Object[]{lnTy.getLineCode(),lnTy.getDescription()});
     setup.updateLineType(lnTy, src);
   
   }
   
  }catch(IOException ex ){
   
  }
  
  
 }
 
 private void loadPayType(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadPayType called with file {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   Map payTypes = new HashMap();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    String payTypeCode = row.getCell(1).getStringCellValue();
    LOGGER.log(INFO, "payType code {0}", payTypeCode);
    int intCompRef = (int)row.getCell(0).getNumericCellValue();
    String compRef = String.valueOf(intCompRef);
    LOGGER.log(INFO, "compRef {0}", compRef);
    CompanyBasicRec comp = sysBuff.getCompany(compRef);
    LOGGER.log(INFO, "comp for row {0}", comp);
    if(comp == null){
     continue;
    }
    PaymentTypeRec payTy = sysBuff.getPaymentTypeByCode(payTypeCode, comp);
    
    if(payTy != null){
     continue;
    }
    
    payTy = new PaymentTypeRec();
    payTy.setCreatedOn(new Date());
    payTy.setCreatedBy(currUsr);
    
     //For each row, iterate through each columns
    Iterator<Cell> cellIterator = row.cellIterator();
    while(cellIterator.hasNext()) {
     Cell cell = cellIterator.next();
      
     LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      CellType ct = cell.getCellTypeEnum();
      if(ct == CellType.NUMERIC){
       LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
       double dblVal = cell.getNumericCellValue();
       int intVal = (int)dblVal;
       compRef = String.valueOf(intVal);
       LOGGER.log(INFO, "compRef {0}", compRef);
       payTy.setCompany(sysBuff.getCompany(compRef));
      }if(ct == CellType.STRING){
       LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
       String strVal = cell.getStringCellValue();
       switch(cell.getColumnIndex()){
        case 0:
         payTy.setCompany(sysBuff.getCompany(strVal));
         break;
        case 1:
         payTy.setPayTypeCode(strVal);
         break;
        case 2:
         payTy.setDescription(strVal);
         break;
        case 3:
         payTy.setPayMedium(strVal);
         break;
        case 4:
         UomRec payUom = sysBuff.getUomByCode(strVal);
         LOGGER.log(INFO, "payUom from sysBuff {0}", payUom);
         payTy.setMediumUom(payUom);
         break;
        case 5:
         LedgerRec led = sysBuff.getLedgerByName(strVal);
         payTy.setLedger(led);
         break;
        case 6:
         boolean inbound = strVal.equals("Y");
         payTy.setInbound(inbound);
       }
      }
      
     }
     
     payTy = sysBuff.updatePayType(payTy, src);
       
     LOGGER.log(INFO, "pay type code {0} descr {1} ledger {2} inbound {3} id {4}", 
             new Object[]{payTy.getPayTypeCode(), payTy.getDescription(), payTy.getLedger(),
             payTy.isInbound(),payTy.getId()});
   }
   
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
  }
 }
 
 private void loadPayTerms(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadPayTerms called with file {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   Map payTypes = new HashMap();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String payTermsCode = row.getCell(0).getStringCellValue();
    LOGGER.log(INFO, "payType code {0}", payTermsCode);
    
    PaymentTermsRec payTerms = sysBuff.getPaymentTermsByCode(payTermsCode);
    
    LOGGER.log(INFO, "payTerms from sysBuff {0}", payTerms);
    
    if(payTerms != null){
     continue;
    }
    payTerms = new PaymentTermsRec();
    payTerms.setCreatedOn(new Date());
    payTerms.setCreatedBy(currUsr);
    
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
        int intVal = (int)cell.getNumericCellValue();
        switch(cell.getColumnIndex()){
         case 3:
          payTerms.setDays(intVal);
          break;
         case 4:
          payTerms.setDayOfMonth(intVal);
        }
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        switch(cell.getColumnIndex()){
         case 0:
          payTerms.setPayTermsCode(strVal);
          break;
         case 1:
          payTerms.setDescription(strVal);
          break;
         case 2:
          payTerms.setBaseType(strVal);
          break;
         case 5:
          UomRec uom = sysBuff.getUomByCode(strVal);
          LOGGER.log(INFO, "Uom from sysBuff id {0} uomCode {1} ", new Object[]{uom.getId(),uom.getUomCode()});
          
          payTerms.setUom(uom);
          break;
         
        }
        
       break;
      }
      
     }
    LOGGER.log(INFO, "Payment Terms pre update  code {0}", payTerms.getPayTermsCode());
    payTerms = basicConfigMgr.updatePaymentTerms(payTerms, src);
    payTerms = sysBuff.updatePaymentTerms(payTerms);
    LOGGER.log(INFO, "Payment Terms updated  code {0}", payTerms.getPayTermsCode());
   }
   
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
  }
 }
 
 private void loadPostType(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadPostType called with file {0}",
          new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   Map revPostTypes = new HashMap();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    String pstCode = row.getCell(1).getStringCellValue();
    LOGGER.log(INFO, "post code {0}", pstCode);
    
    PostTypeRec postTy = this.sysBuff.getPostTypeForCode(pstCode);
    
    if(postTy != null){
     continue;
    }
    
    postTy = new PostTypeRec();
    postTy.setCreateDate(new Date());
    postTy.setCreatedBy(currUsr);
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         // code
         postTy.setPostTypeCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         postTy.setDescription(strVal);
        }else if(cell.getColumnIndex() == 2){
         //Module
         ModuleRec mod = this.sysBuff.getModuleByName(strVal);
         if(mod != null){
          postTy.setModule(mod);
         }
        }else if(cell.getColumnIndex() == 3){
         LedgerRec ledg = this.sysBuff.getLedgerByName(strVal);
         if(ledg != null){
          postTy.setLedger(ledg);
         }
        }else if(cell.getColumnIndex() == 4){
         boolean debit = (strVal.equals("Y")?true:false);
         postTy.setDebit(debit);
         if(debit){
          postTy.setSign('+');
         }else{
          postTy.setSign('-');
         }
        }else if(cell.getColumnIndex() == 5){
         postTy.setModuleDescription(strVal);
        }else if(cell.getColumnIndex() == 6){
         revPostTypes.put(postTy.getPostTypeCode(), strVal);
         
         LOGGER.log(INFO, "Reversal type {0}", strVal);
         
        }
       break;
      }
     }
     
     postTy = sysBuff.updatePostType(postTy, src);
     LOGGER.log(INFO, "post type code {0} descr {1} ledger {2} debit {3}", 
             new Object[]{postTy.getPostTypeCode(), postTy.getDescription(), postTy.getLedger(),
             postTy.isDebit()});
     LOGGER.log(INFO, "revPostTypes num {0}", revPostTypes.size());
   }
   LOGGER.log(INFO, "Total revPostTypes num {0}", revPostTypes.size());
   // update to set reversal post types
   for(Object ptobj: revPostTypes.keySet()){
    
    String ptCode = (String)ptobj;
    String revCode = (String)revPostTypes.get(ptCode);
    LOGGER.log(INFO, "Set reversal post type for {0} rev code {1}", new Object[]{ptCode,revCode});
    PostTypeRec ptRec = sysBuff.getPostTypeForCode(ptCode);
    ptRec.setChangedBy(currUsr);
    ptRec.setChangedDate(new Date());
    PostTypeRec revPt = sysBuff.getPostTypeForCode(revCode);
    ptRec.setRevPostType(revPt);
    sysBuff.updatePostType(ptRec,getView());
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadLocale(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadLocale called with file {0}",
          new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  LocaleCodeRec loc = null ;
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    String modCode = row.getCell(1).getStringCellValue();
    LOGGER.log(INFO, "numCode {0}", modCode);
    
   
    loc = new LocaleCodeRec();
    loc.setCreatedBy(this.getLoggedInUser());
    loc.setCreatedOn(new Date());
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         LOGGER.log(INFO, "lang");
         loc.setLangName(strVal);
       }else if(cell.getColumnIndex() == 1){
         loc.setCountryName(strVal);
       }else if(cell.getColumnIndex() == 2){
         loc.setLocaleCode(strVal);
       
       }
       break;
      }
     }
     
     
     loc = this.setup.updateLocaleCode(loc, this.getView());
     LOGGER.log(INFO, "Locale code {0} descr {1} process {2}", new Object[]{loc.getLocaleCode(), loc.getCountryName(), loc.getLangName()});
     
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadModules(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadModules called with file {0}",
          new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  ModuleRec mod = null ;
  List<ModuleRec> modules = this.sysBuff.getModules();
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    String modCode = row.getCell(1).getStringCellValue();
    LOGGER.log(INFO, "numCode {0}", modCode);
    
    if(modules != null && !modules.isEmpty()){
    boolean modFound = false;
    for(ModuleRec m:modules){
     if(m.getModuleCode().equals(modCode)){
      modFound = true;
      break;
     }
    }
    
    if(modFound){
     continue;
    }
    }
    mod = new ModuleRec();
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         LOGGER.log(INFO, "module code");
         mod.setName(strVal);
       }else if(cell.getColumnIndex() == 1){
         mod.setDescription(strVal);
       }else if(cell.getColumnIndex() == 2){
         mod.setModuleCode(strVal);
       
       }
       break;
      }
     }
     
     mod.setCreatedBy(currUsr);
     mod.setCreatedDate(new Date());
     mod = sysBuff.updateModule(mod, src);
     LOGGER.log(INFO, "Module name {0} descr {1} process {2}", new Object[]{mod.getName(), mod.getDescription(), mod.getModuleCode()});
     
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadNumberRanges(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadNumberRanges called with file {0} setup {1}",new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  ModuleRec mod = null ;
  if(modules == null){
   LOGGER.log(INFO, "Sys buffers modules {0}", modules);
   modules = new ArrayList<>();
  } 
  LOGGER.log(INFO, "modules {0}", modules);
  ListIterator<ModuleRec> modLi = modules.listIterator();
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    String numCode = row.getCell(2).getStringCellValue();
    LOGGER.log(INFO, "numCode {0}", numCode);
    LOGGER.log(INFO, "setup {0}", this);
    LOGGER.log(INFO, "setup.getNumControlByShrtDescr called with code {0} result {1}", new Object[]{numCode,
     setup.getNumControlByShrtDescr(numCode)});
    NumberRangeRec numRange = setup.getNumControlByShrtDescr(numCode);
    if(numRange != null){
     continue;
    }
    
    numRange = new NumberRangeRec();
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      LOGGER.log(INFO, "column index {0}", cell.getColumnIndex());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        boolean bBal = cell.getBooleanCellValue();
        numRange.setAutoNum(bBal);
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        
        switch (cell.getColumnIndex()) {
         case 3:
          numRange.setFromNum(dval.intValue());
          break;
         case 4:
          numRange.setToNum(dval.intValue());
          break;
         case 7:
          numRange.setNextNum(dval.intValue());
          break;
         default:
          break;
        }
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        switch (cell.getColumnIndex()) {
         case 0:
          LOGGER.log(INFO, "module column mod {0}",mod);
          LOGGER.log(INFO, "modules {0}", modules);
          if(mod == null || !mod.getModuleCode().equals("GL")){
           boolean modFound= false;
           LOGGER.log(INFO, "modules {0}", modules);
           modLi = modules.listIterator();
           while(modLi.hasNext() && !modFound){
            ModuleRec modt = modLi.next();
            LOGGER.log(INFO, "Current Module code {0}", modt.getModuleCode());
            if(modt.getModuleCode().equals("GL")){
             mod = modt;
             numRange.setModule(mod);
             modFound = true;
             LOGGER.log(INFO, "Found module {0}", mod.getModuleCode());
            }
           }
          }else{
           numRange.setModule(mod);
          }
          break;
         case 1:
        //need to get Number range type
        LOGGER.log(INFO, "Number Range name String value {0}",strVal);
        List<NumberRangeTypeRec> nrTyps = sysBuff.getNumRangeTypes();
        if(nrTyps == null){
         LOGGER.log(INFO, "No number range types");
         continue;
        }
        boolean found = false;
        ListIterator<NumberRangeTypeRec> li = nrTyps.listIterator();
        while(li.hasNext() && !found){
         NumberRangeTypeRec nrTy = li.next();
         LOGGER.log(INFO, "File num range name {0} nrType name {1} ", new Object[]{
          strVal,nrTy.getName()});
         if(StringUtils.equals(nrTy.getName(), strVal)){
          numRange.setNumberRangeType(nrTy);
          found = true;
         }
        }
        if(!found){
         LOGGER.log(INFO, "No number range type found for ");
         
        }else{
         LOGGER.log(INFO, "Num range type is now {0}", numRange.getNumberRangeType().getCode());
        }
        break;
       case 2:
        numRange.setShortDescr(strVal);
        break;
       case 3:
        numRange.setLongDescr(strVal);
        break;
       case 8:
        List<NumberRangeRec> numRngs = sysBuff.getNumRanges();
        ListIterator<NumberRangeRec> numRngLi = numRngs.listIterator();
        found = false;
        while(numRngLi.hasNext() && !found){
         NumberRangeRec curr = numRngLi.next();
         if(StringUtils.equals(curr.getShortDescr(), strVal)){
          numRange.setPriorNumRange(curr);
          found = true;
         }
        }
        if(!found){
         LOGGER.log(INFO, "No number range for prior code {0}", strVal);
        }
        break;
       case 9:
        numRngs = sysBuff.getNumRanges();
        numRngLi = numRngs.listIterator();
        found = false;
        while(numRngLi.hasNext() && !found){
         NumberRangeRec curr = numRngLi.next();
         if(StringUtils.equals(curr.getShortDescr(), strVal)){
          numRange.setNextNumRange(curr);
          found = true;
         }
        }
        if(!found){
         LOGGER.log(INFO, "No number range for net code {0}", strVal);
        }
        break;
       default:
        break;
      }
       break;
      }
     
     }
     
     LOGGER.log(INFO, "Num range code {0} descr {1} from {2} to {3}", 
             new Object[]{numRange.getShortDescr(),
      numRange.getLongDescr(),numRange.getFromNum(), numRange.getToNum()});
     LOGGER.log(INFO, "Module {0} type {1} auto {2}", new Object[]{
      numRange.getModule().getModuleCode(), numRange.getNumberRangeType().getName(),
      String.valueOf(numRange.isAutoNum())
     });
     numRange.setCreatedBy(currUsr);
     numRange.setCreatedDate(new Date());
     numRange = setup.updateNumberControl(numRange, src);
     LOGGER.log(INFO, " end process row - numRange id {0}", numRange.getNumberControlId());
     
   }
   LOGGER.log(INFO, "end processing num range file");
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadNumberRangeType(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "loadNumberRangeType called with file name {0}", fname);
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    NumberRangeTypeRec nrTyRec = new NumberRangeTypeRec();
    //For each row, iterate through each columns
     int colIndex = 0;
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      colIndex = cell.getColumnIndex();
      LOGGER.log(INFO, "column index {0}", String.valueOf(colIndex));
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue());
        break;
       case Cell.CELL_TYPE_STRING:
        String strVal = cell.getStringCellValue();
        LOGGER.log(INFO, "String value {0}" , strVal);
        switch(colIndex){
         case 0:
          nrTyRec.setCode(strVal);
          break;
         case 1:
          nrTyRec.setName(strVal);
        }
      }
      
     }
     nrTyRec.setCreatedBy(currUsr);
    nrTyRec.setCreatedDate(new Date());
    nrTyRec = this.basicConfigMgr.upateNumberRangeType(nrTyRec, getView());
    LOGGER.log(INFO, "Number Range object id {0}", nrTyRec.getId());
   }
  }catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, "Read spread sheet error", ex);
  }
 }
 
 private void loadProcessCode(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "ProcessCode called with file {0}",
          new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  ModuleRec mod ;
  ProcessCodeRec procCd;
  List<ModuleRec> modules = this.sysBuff.getModules();
  List<ProcessCodeRec> procCodes = sysBuff.getProcessCodes();
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String procCode = row.getCell(0).getStringCellValue();
   
    LOGGER.log(INFO, "numCode {0}", procCode);
    
    if(procCodes != null && !procCodes.isEmpty()){
    boolean prCodeFound = false;
    for(ProcessCodeRec p:procCodes){
     if(p.getName().equals(procCode)){
      prCodeFound = true;
      break;
     }
    }
    
    if(prCodeFound){
     continue;
    }
    }
    procCd = new ProcessCodeRec();
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      CellType ct = cell.getCellTypeEnum();
      
      LOGGER.log(INFO, "cell type {0}", ct.name());
      if(ct == CellType.STRING){
       LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
       String strVal = cell.getStringCellValue();
       if(cell.getColumnIndex() == 0){
        LOGGER.log(INFO, "module code");
        procCd.setName(strVal);
       }else if(cell.getColumnIndex() == 1){
        procCd.setDescription(strVal);
       }else if(cell.getColumnIndex() == 2){
        mod = null;
        for(ModuleRec m:modules){
         if(m.getModuleCode().equalsIgnoreCase(strVal)){
          mod = m;
          procCd.setModule(mod);
          break;
         }
        }
        if(mod == null){
         MessageUtil.addErrorMessageParam1("modNfnd", "errorText", strVal);
         continue;
        }
       }
      }
      
     }
     
     procCd.setCreatedBy(currUsr);
     procCd.setCreatedDate(new Date());
     procCd = sysBuff.processCodeUpdate(procCd, src);
     LOGGER.log(INFO, "Process Code name {0} descr {1}", new Object[]{procCd.getName(),procCd.getDescription()});
     
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, "Read spread sheet error", ex);
  }
 }
 
 private void loadProgramme(String fname, UserRec currUsr, String src){
  LOGGER.log(INFO, "ProcessCode called with file {0}",
          new Object[]{ fname});
  FacesContext fc = FacesContext.getCurrentInstance();
  
  String filePath = BASE_DIR+fname;
  try{
   
  is = fc.getExternalContext().getResourceAsStream(filePath);
  
  workbook = new HSSFWorkbook(is);
  sheet1 = workbook.getSheetAt(0);
  
  
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    String ref = null;
    
    if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
    ref= row.getCell(0).getStringCellValue();
    } else if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double refNum = row.getCell(0).getNumericCellValue();
     ref = String.valueOf(refNum.intValue());
    }
    
    String compRef = null;
    if(row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING){
     compRef= row.getCell(1).getStringCellValue();
    } else if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC){
     Double refNum = row.getCell(1).getNumericCellValue();
     compRef = String.valueOf(refNum.intValue());
    }
    
    CompanyBasicRec comp = this.sysBuff.getCompany(compRef);
    if(comp == null){
     LOGGER.log(INFO, "Company with ref {0} not found", compRef);
     continue;
    }
    ProgrammeRec prog = progMgr.getProgrammeByRef(comp, ref);
    if(prog != null){
     LOGGER.log(INFO, "Programme already exists with ref {0}", ref);
     continue;
    }
    
    prog = new ProgrammeRec();
    prog.setCreatedOn(new Date());
    prog.setCreatedBy(currUsr);
    prog.setReference(ref);
    prog.setProgrammeForCompany(comp);
    
    PartnerPersonRec pers = new PartnerPersonRec();
    boolean hasPers = false;
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        Double dval = cell.getNumericCellValue();
        switch(cell.getColumnIndex()){
         case 3:
          boolean dateCell = DateUtil.isCellDateFormatted(cell);
          if(dateCell){
           Date frDt = DateUtil.getJavaDate(dval);
           prog.setValidFrom(frDt);
          }else{
           LOGGER.log(INFO, "Not a valid date  ", dval);
          }
          break;
         case 4:
          dateCell = DateUtil.isCellDateFormatted(cell);
          if(dateCell){
           Date toDt = DateUtil.getJavaDate(dval);
           prog.setValidTo(toDt);
          }else{
           LOGGER.log(INFO, "Not a valid date  ", dval);
          }
          break;
         case 5:
          prog.setBudget(dval);
          break;
         case 6:
          prog.setCost(dval);
          break;
         
        }
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        switch(cell.getColumnIndex()){
         case 2:
          prog.setName(strVal);
          break;
         case 7:
          hasPers = true;
          pers.setTitle(strVal);
          break;
         case 8:
          hasPers = true;
          pers.setFirstName(strVal);
          break;
         case 9:
          hasPers = true;
          pers.setFamilyName(strVal);
          break;
        }
  
       break;
      }
     }
   if(hasPers && pers.getFamilyName() != null){
    List<PartnerPersonRec> ptnrList = this.mdm.getIndivPtnrsBySurname(pers.getFamilyName());
    if(ptnrList != null && !ptnrList.isEmpty()){
     ListIterator<PartnerPersonRec> ptnrLi = ptnrList.listIterator();
     boolean ptnrFound = false;
     while(ptnrLi.hasNext() && !ptnrFound){
      PartnerPersonRec ptnr = ptnrLi.next();
      if(ptnr.getTitle().equals(pers.getTitle()) && ptnr.getFirstName().equals(pers.getFirstName())&&
              ptnr.getFamilyName().equals(pers.getFamilyName())){
       pers = ptnr;
       ptnrFound = true;
      }
     }
    }
    if(pers.getId() == null){
     pers.setCreatedBy(currUsr);
     pers.setCreatedDate(new Date());
    }else{
     pers.setChangedBy(currUsr);
     pers.setChangedOn(new Date());
    }
    prog.setResponsibilityOf(pers);
   }  
   LOGGER.log(INFO, "Save Programme ref {0}", prog.getReference());
   prog = this.progMgr.updateProgramme(prog, currUsr, getView());
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, "Read spread sheet error", ex);
  }
 }
 
 private void loadTransactionType(String fname, UserRec currUsr, String src){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
 
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    TransactionTypeRec trTy = sysBuff.getTransactionTypeRecByCode(row.getCell(0).getStringCellValue());
    
    if(trTy != null){
     continue;
    }
    
    trTy = new TransactionTypeRec();
    trTy.setCreatedBy(currUsr);
    trTy.setCreatedOn(new Date());
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        switch(cell.getColumnIndex()){
         case 0:
          trTy.setCode(strVal);
          break;
         case 1:
          trTy.setShortDescription(strVal);
          break;
         case 2:
          trTy.setDescription(strVal);
          break;
         case 3:
          LedgerRec led = sysBuff.getLedgerByName(strVal);
          trTy.setLedger(led);
          break;
         case 4:
          trTy.setProcessCode(strVal);
        }
       break;
      }
     }
     this.sysBuff.updateTransType(trTy, src);
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadPtnrRole(String fname, UserRec currUsr, String src){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
     //Title row
    }
    if(row.getCell(0) == null){
     break;
    }
    String ptnrRoleCd = row.getCell(0).getStringCellValue();
    PartnerRoleRec role = sysBuff.getPartnerRoleByCode(ptnrRoleCd);//mdm.getPartnerRoleByCode();
    if(role == null){
     role = new PartnerRoleRec();
     role.setRoleCode(row.getCell(0).getStringCellValue());
     role.setRoleName(row.getCell(1).getStringCellValue());
     role.setCreatedBy(currUsr);
     role.setCreatedOn(new Date());
    }else{
     role.setRoleCode(row.getCell(0).getStringCellValue());
     role.setRoleName(row.getCell(1).getStringCellValue());
     role.setChangedBy(currUsr);
     role.setChangedOn(new Date());
    }
    role = this.mdm.updatePartnerRole(role,  src);
    LOGGER.log(INFO, "role name {0} id {1}", new Object[]{role.getRoleName(), role.getId()});
   }
  }catch (IOException ex) {
   LOGGER.log(INFO, "Errot occured loading file: {0} error {1} ", new Object[]{fname,ex.getLocalizedMessage()});
   
  }
  
 }
 private void loadSortOrder(String fname, UserRec currUsr, String src){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
 
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    SortOrderRec sort = sysBuff.getSortOrderByCode(row.getCell(0).getStringCellValue());
    if(sort != null){
     continue;
    }
    
    sort = new SortOrderRec();
    sort.setCreatedBy(currUsr);
    sort.setCreatedOn(new Date());
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         LOGGER.log(INFO, "code col");
         sort.setSortCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         sort.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         sort.setDescription(strVal);
        }
       break;
      }
     }
     this.sysBuff.updateSortOrder(sort, src);
   }
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
  private void loadUnitOfMeasure(String fname, UserRec currUsr, String src){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
 
  //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()) {
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    UomRec uom = sysBuff.getUomByCode(row.getCell(0).getStringCellValue());
    if(uom != null){
     continue;
    }
    
    uom = new UomRec();
    uom.setCreatedBy(currUsr);
    uom.setCreateDate(new Date());
    
     //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      
      LOGGER.log(INFO, "cell type {0}", cell.getCellType());
      switch(cell.getCellType()) {
       case Cell.CELL_TYPE_BOOLEAN:
        LOGGER.log(INFO,"cell boolean {0}",cell.getBooleanCellValue());
        break;
       case Cell.CELL_TYPE_NUMERIC:
        LOGGER.log(INFO, "Cell Numeric {0}" , cell.getNumericCellValue()); 
        
        break;
       case Cell.CELL_TYPE_STRING:
        LOGGER.log(INFO, "Cell String {0}" , cell.getStringCellValue());
        String strVal = cell.getStringCellValue();
        if(cell.getColumnIndex() == 0){
         LOGGER.log(INFO, "code col");
         uom.setUomCode(strVal);
        }else if(cell.getColumnIndex() == 1){
         uom.setName(strVal);
        }else if(cell.getColumnIndex() == 2){
         uom.setDescription(strVal);
        }else if(cell.getColumnIndex() == 3){
         uom.setProcessCode(strVal);
        }
       break;
      }
     }
     LOGGER.log(INFO, "Save UOM with code {0}", uom.getUomCode());
     uom.setCreatedBy(currUsr);
     uom.setCreateDate(new Date());
     sysBuff.updateUom(uom, this.getView());
   }
   
  } catch (IOException ex) {
   Logger.getLogger(DefaultLoadBean.class.getName()).log(Level.SEVERE, null, ex);
  }
 }
 
 private void loadVatCode(String fname){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  LOGGER.log(INFO, "VAT code file name {0}", filePath);
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()){
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    VatCodeRec vatCd = new VatCodeRec();
    
    //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()){
      Cell cell = cellIterator.next();
      CellType cellTy = cell.getCellTypeEnum();
      int colNum = cell.getColumnIndex();
      
      if(cellTy == CellType.STRING){
       String strVal = cell.getStringCellValue();
       switch(colNum){
        case 0:
         vatCd.setCode(strVal);
         break;
        case 1:
         vatCd.setDescription(strVal);
         break;
        case 2:
         char taxType = strVal.charAt(0);
         vatCd.setTaxType(taxType);
         break;
       }
      }
      if(cellTy == CellType.NUMERIC){
       double dblVal = (Double)cell.getNumericCellValue();
       switch(colNum){
        case 3:
         vatCd.setRate(dblVal);
         break;
        case 4:
         vatCd.setIrrrecoverableRate(dblVal);
         break;
        case 6:
         int intVal = ((Double)dblVal).intValue();
         vatCd.setAddnlCat(intVal);
         break;
        case 9:
         if(DateUtil.isCellDateFormatted(cell)){
          Date dt = DateUtil.getJavaDate(dblVal);
          vatCd.setValidFrom(dt);
         }
         break;
        case 10:
         if(DateUtil.isCellDateFormatted(cell)){
          Date dt = DateUtil.getJavaDate(dblVal);
          vatCd.setValidTo(dt);
         }
       }
      }
      if(cellTy == CellType.BOOLEAN){
       boolean bVal = cell.getBooleanCellValue();
       vatCd.setInputTax(bVal);
      }
     }
     // All rows populated
     vatCd.setCreatedBy(this.getLoggedInUser());
     vatCd.setCreatedOn(new Date());
     vatCd = this.sysBuff.updateVatCode(vatCd, this.getView());
    
   
    
   }
 
  }catch (IOException ex){
   LOGGER.log(INFO, "Could not load file {0} eror {1}", 
     new Object[]{fname,ex.getLocalizedMessage()});
   
  }
  
 }
 
 private void loadVatCodeComp(String fname){
  FacesContext fc = FacesContext.getCurrentInstance();
  String filePath = BASE_DIR+fname;
  LOGGER.log(INFO, "VAT code file name {0}", filePath);
  try{
   is = fc.getExternalContext().getResourceAsStream(filePath);
  
   workbook = new HSSFWorkbook(is);
   sheet1 = workbook.getSheetAt(0);
   
   //Iterate through each rows from first sheet
   Iterator<Row> rowIterator = sheet1.iterator();
   while(rowIterator.hasNext()){
    Row row = rowIterator.next();
    LOGGER.log(INFO, "Row num {0}", row.getRowNum());
    if(row.getRowNum() == 0){
     continue;
    }
    if(row.getCell(0) == null){
     break;
    }
    VatCodeCompanyRec vatCdComp = new VatCodeCompanyRec();
    
    //For each row, iterate through each columns
     Iterator<Cell> cellIterator = row.cellIterator();
     while(cellIterator.hasNext()){
      Cell cell = cellIterator.next();
      CellType cellTy = cell.getCellTypeEnum();
      int colNum = cell.getColumnIndex();
      
      if(cellTy == CellType.STRING){
       String strVal = cell.getStringCellValue();
       switch(colNum){
        case 0:
         VatCodeRec vatCode = this.sysBuff.getVatCodeByCode(strVal);
         vatCdComp.setVatCode(vatCode);
         break;
        case 1:
         CompanyBasicRec comp = this.sysBuff.getCompany(strVal);
         vatCdComp.setCompany(comp);
         break;
       }
      }else if(cellTy == CellType.NUMERIC){
       Integer intVal = ((Double)cell.getNumericCellValue()).intValue();
       switch(colNum){
        case 1:
         // spreadsheet has company ref as number data
         String strVal = intVal.toString();
         CompanyBasicRec comp = this.sysBuff.getCompany(strVal);
         vatCdComp.setCompany(comp);
         break;
        case 2:
         // VAT 
       }
      }
      
     }
     
   }
  }catch (IOException ex){
   LOGGER.log(INFO, "Could not load file {0} eror {1}", 
     new Object[]{fname,ex.getLocalizedMessage()});
   
  }
   
  
 }
 public void onLoadComplete(){
  MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "loadDefaultComplete", "blacResponse");
  progress = 0;
 }
 public void onSaveDefConfiguration(){
  LOGGER.log(INFO, "onSaveDefConfiguration called {0}", defaults.getTarget());
  steps = 0;
  stepNum = 0;
  progress = 0;
  
  // sort by step
  List<DefaultConfigSetting> targetList = defaults.getTarget();
  steps = targetList.size();
  LOGGER.log(INFO, "steps {0}", steps);
  //stepNum = 30;
  PrimeFaces pf = PrimeFaces.current();
  this.showProgress = true;
  pf.ajax().update("defaultSetupFrm");
  LOGGER.log(INFO, "showProgress {0}", showProgress);
  
  
  pf.executeScript("PF('progBarWv').start()");
  
  Collections.sort(targetList,new ConfigurationByOrder());
  LOGGER.log(INFO, "Sorted targetList {0}", targetList);
  this.showProgress = true;
  Ajax aj = PrimeFaces.current().ajax();
  for(DefaultConfigSetting def:defaults.getTarget() ){
   switch (def.getCode()){
    case "uom":
     this.loadUnitOfMeasure("uom.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefUom", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "crncy":
     this.loadCurrency("Currency.xls", this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCurr", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "cntry":
     countries = mdm.getCountriesAll();
     this.loadCountryRec("Country.xls", this.getLoggedInUser(), this.getView());
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCntry", "blacResponse");
     stepNum++;
     aj.update("defaultSetupFrm:grwl");
     break;
    case "loc":
     this.loadLocale("Locale.xls", this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefLoc", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "mod":
     this.loadModules("Module.xls", this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefMod", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "procCode":
     this.loadProcessCode("ProcessCode.xls", this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPocCd", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "chqVoidRsn":
     this.loadChequeVoidRsn("ChequeVoidReason.xls");
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefChqRsn", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "docRevRsn":
     this.loadDocRevReason("DocRevReason.xls");
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefDocRevRsn", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "numRngTy":
     this.loadNumberRangeType("NumberRangeType.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefNrRngTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "numRng":
     this.loadNumberRanges("NumberRange.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefNrRng", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break; 
    case "lnTy":
     this.loadLineTypes("LineType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefLnTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "docTy":
     this.loadDocTypeRec("DocType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefDocTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "contRole":
     this.loadContactRole("ContactRole.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefContRole", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "tranTy":
     this.loadTransactionType("TransactionType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefTransTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "sortOrd":
     this.loadSortOrder("SortCode.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefSortCd", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "led":
     this.loadLedger("Ledger.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefLed", "blacResponse");
    aj.update("defaultSetupFrm:grwl");
     break;
    case "acntTy":
     this.loadAccountType("AccountType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefAcntTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "fndCat":
     this.loadFundType("FundCat.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefFndCat", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "calNatural":
     loadCalNatural("CalRule-Natural.xls",getLoggedInUser(),getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalN", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;  
    case "calFxDay" :
     this.loadCalFixedDay("CalRule-FixedDate.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalD", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "calMonth":
     this.loadCalMonth("CalRule-Month.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalM", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "calFlexYr":
     this.loadCalFlexYear("Cal-FlexYr.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalYrF", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "calFlexPer":
     this.loadCalFlexPeriod("Cal-FlexPeriod.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalPerF", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "fiscPer":
     this.loadFiscalCalRule("FiscalCalRule.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCalPer", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "coa":
     this.loadChartOfAccounts("ChartOfAccounts.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCoA", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "pstTy":
     this.loadPostType("PostType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPstTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "glAcntCh" :
     loadGlAcntChart("glaccountchart.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefGlAcnt", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "comp":
     this.loadCompany("Company.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefComp", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "compApAr":
     this.loadCompApAr("CompanyApAr.xls");
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefApAr", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "payTy":
     this.loadPayType("PaymentType.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPayTy", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "payTerms":
     loadPayTerms("PaymentTerms.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPayTerm", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "postPer":
     this.loadCompPostPer("PostPeriod.xls", getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPostPer", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "bank":
     this.loadBank("Bank.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefBnk", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "bankBr":
     this.loadBankBranch("BankBranch.xls",  this.getLoggedInUser(), this.getView());
     //this.loadBank("Bank.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefBnkBr", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "bankAc":
     this.loadBankAccount("BankAccount.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefBnkAc", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "bankAcComp" :
     this.loadBankAccountComp("BankAccountComp.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefBnkHse", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "bacsTransCd":
      this.loadBacsTransCode("BacsTranCode.xls", getLoggedInUser(), this.getView());
      stepNum++;
      MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefBacsTr", "blacResponse");
      aj.update("defaultSetupFrm:grwl");
      break;
    case "chequeBk":
     this.loadChequeBook("ChequeBook.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefChqBk", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "glAcntComp":
     this.loadGlAcntComp("GlAccountComp.xls",  this.getLoggedInUser(), this.getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefGlAcntComp", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "fund":
     this.loadFund("Fund.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefFnd", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "costCent":
     this.loadCostCent("CostCentre.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefCC", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "prog":
     this.loadProgramme("Programme.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefProg", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "ptnrRole":
     this.loadPtnrRole("PartnerRole.xls", getLoggedInUser(), getView());
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefPtnrRl", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    case "vatCode":
     this.loadVatCode("VatCode.xls");
     stepNum++;
     MessageUtil.addClientInfoMessage("defaultSetupFrm:grwl", "setupLdDefVat", "blacResponse");
     aj.update("defaultSetupFrm:grwl");
     break;
    default:
     MessageUtil.addWarnMessage("cfgOptNF", "validationText");
     break;
   }
  
   //stepNum = steps;
   LOGGER.log(INFO, "Step num {0}", stepNum);
   
  }
 
 }
}
