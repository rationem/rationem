/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;
 
import com.rationem.busRec.config.arap.PaymentTermsRec;
import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocBankLineBacsRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocBaseRec;
import com.rationem.busRec.doc.DocFiPartialRec;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiPeriodBalance;
import com.rationem.entity.document.DocFi;
import com.rationem.entity.config.common.SortOrder;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.document.DocLineBase;
import com.rationem.entity.document.DocLineGl;
import java.util.Iterator;
import com.rationem.busRec.doc.DocLineBaseRec;
import java.util.Collection;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocFiTemplAccrPrePayRec;
import com.rationem.busRec.doc.DocGlVatCodeSummary;
import com.rationem.busRec.doc.DocInvoiceArRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.doc.DocLineBasePartialRec;
import com.rationem.busRec.doc.DocLineFiTemplGlRec;
import com.rationem.busRec.doc.DocLineFiTemplateRec;
import com.rationem.busRec.doc.DocLineGlPartialRec;
import java.util.ListIterator;
import java.util.Date;
import com.rationem.entity.user.User;
import com.rationem.busRec.user.UserRec;
import com.rationem.entity.document.DocType;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.Query;
import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.doc.DocVatSummary;
import com.rationem.busRec.doc.SalesPartFiLineRec;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.tax.GiftAidScheduleLine;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatRegSchemeRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.salesTax.vat.VatReturnRec;
import com.rationem.busRec.salesTax.vat.VatSchemeRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.entity.audit.AuditDocFi;
import com.rationem.entity.audit.AuditDocFiPartial;
import com.rationem.entity.audit.AuditDocFiTemplateBase;
import com.rationem.entity.audit.AuditDocInvoiceAr;
import com.rationem.entity.audit.AuditDocLine;
import com.rationem.entity.audit.AuditDocLinePartial;
import com.rationem.entity.audit.AuditDocLineTemplate;
import com.rationem.entity.config.arap.PaymentTerms;
import com.rationem.entity.config.arap.PaymentType;
import com.rationem.entity.config.common.LineTypeRule;
import com.rationem.entity.config.common.TransactionType;
import com.rationem.entity.config.common.Uom;
import com.rationem.entity.document.DocBankLineBase;
import com.rationem.entity.document.DocBankLineChq;
import com.rationem.entity.document.DocBase;
import com.rationem.entity.document.DocFiPartial;
import com.rationem.entity.document.DocFiTemplAccrPrePay;
import com.rationem.entity.document.DocInvoiceAr;
import com.rationem.entity.document.DocLineAp;
import com.rationem.entity.document.DocLineAr;
import com.rationem.entity.document.DocLineBasePartial;
import com.rationem.entity.document.DocLineFiTemplGl;
import com.rationem.entity.document.DocLineGlPartial;
import com.rationem.entity.document.DocLineSubLedgerIF;
import com.rationem.entity.document.DocLineTemplate;
import com.rationem.entity.fi.arap.ApAccount;
import com.rationem.entity.fi.arap.ArAPAccountIF;
import com.rationem.entity.fi.arap.ArAccount;
import com.rationem.entity.fi.arap.ArBankAccount;
import com.rationem.entity.fi.arap.FiArPeriodBalance;
import com.rationem.entity.fi.company.CompanyBasic;
import com.rationem.entity.fi.company.CompanyDocNumbers;
import com.rationem.entity.ma.CostAccountDirect;
import com.rationem.entity.ma.CostCentre;
import com.rationem.entity.ma.ProgramAccount;
import com.rationem.entity.ma.Programme;
import com.rationem.entity.mdm.PartnerBase;
import com.rationem.entity.mdm.PartnerCorporate;
import com.rationem.entity.mdm.PartnerPerson;
import com.rationem.entity.sales.SalesPartCompany;
import com.rationem.entity.salesTax.vat.VatCodeCompany;
import com.rationem.entity.salesTax.vat.VatRegScheme;
import com.rationem.entity.salesTax.vat.VatReturn;
import com.rationem.entity.tr.bank.BankAccountCompany;
import com.rationem.entity.tr.bank.BnkPaymentRun;
import com.rationem.helper.RestrictFundBalance;
import com.rationem.helper.RestrictedFundBal;
import com.rationem.helper.RestrictedFundArDocBal;
import com.rationem.util.ArPayRunSelection;
import java.util.ArrayList;
import com.rationem.exception.BacException;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.helper.SelectOptSalesDayBook;
import com.rationem.helper.TemplSelectOption;
import com.rationem.util.ApAgePaySel;
import com.rationem.util.ApLineSel;
import com.rationem.util.FundBalance;
import com.rationem.util.GenUtilServer;
import com.rationem.util.RestrictedFundAmount;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ejb.EJB;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import org.apache.commons.lang3.StringUtils;

import static javax.persistence.LockModeType.OPTIMISTIC;
import static java.util.logging.Level.INFO;
import javax.persistence.TypedQuery;


/**
 *
 * @author Chris
 */

@Stateless
@LocalBean
public class DocumentDM {
  private static final Logger LOGGER =   Logger.getLogger(DocumentDM.class.getName());

  private EntityManager em;
  private EntityTransaction trans;
  
  List<LineTypeRule> lineTypes;
  List<Fund> restrictedFunds;

  @EJB
  private CompanyDM compDM;
  
  
  
  @EJB
  private CostCentreDM costCentDM;
  
  @EJB
  private UserDM usrDM;

  @EJB
  private MasterDataDM mastDM;
  
  @EJB
  private FiMasterRecordDM glActDM;
  
  @EJB
  private VatDM vatDM;
  
  @EJB
  private ArAccountDM arAccountDM;

  @EJB
  private ApAccountDM apAcntDM;  
  @EJB
  private SysBuffer sysBuff;
  
  @EJB
  private SalesDM salesDM;
  
  @EJB
  private TreasuryDM bankDM;
  
  
  
  @EJB
  private ProgrammeDM progDM;
  
 //private CostCentreRec CostCentreRec;
  
  @PostConstruct
  public void init(){
   LOGGER.log(INFO, "Init documentDM. lineTypes {0}", lineTypes);
   
   FacesContext fc = FacesContext.getCurrentInstance();
   String tenantId = (String)fc.getExternalContext().getSessionMap().get("tenantId");
   HashMap properties = new HashMap();
   properties.put("tenantId", tenantId);
   properties.put("eclipselink.session-name", "sessionName"+tenantId);
   em = Persistence.createEntityManagerFactory("rationemPU", properties).createEntityManager(); 
  
   trans = em.getTransaction();
   
   lineTypes = new ArrayList<>();
   Query linQ = em.createNamedQuery("lineTypesAll");
   List rs = linQ.getResultList();
   ListIterator li = rs.listIterator();
   while(li.hasNext()){
    LineTypeRule lineType = (LineTypeRule)li.next();
    lineTypes.add(lineType);
   }
   LOGGER.log(INFO, "End init line types {0}", lineTypes);
  }
  
  private DocFi addARLine(DocFi docFi, List<DocLineBaseRec> docLinesRec,
          List<RestrictFundBalance> fndBals,CompanyBasic comp,
          User usr, Date crDate, String view){
   LOGGER.log(INFO, "addARLine called with {0}", docLinesRec);
   UserRec userRec = this.usrDM.getUserRecPvt(usr);
   ArAPAccountIF arApAccount;// = null;
   String partnerType;// = null;
   
   ListIterator<DocLineBaseRec> docLinesLi = docLinesRec.listIterator();
   List<DocLineBase> docLines = docFi.getDocLines();
   if(docLines == null){
    docLines = new ArrayList<>();
   }
   long lineNum = docLines.size();
   DocLineArRec arLineRec = null;
   boolean foundArLine = false;
   while(docLinesLi.hasNext() && !foundArLine){
    DocLineBaseRec docLine = docLinesLi.next();
    LOGGER.log(INFO, "docLine type {0}", docLine.getClass().getCanonicalName());
    if(docLine.getClass().getCanonicalName().endsWith("DocLineArRec")){
     arLineRec = (DocLineArRec)docLine;
     
     foundArLine = true;
     LOGGER.log(INFO, "Found AR line with posting type: {0}", arLineRec.getPostType());
     LOGGER.log(INFO, "Found AR line with posting type code: {0}", arLineRec.getPostType().getPostTypeCode());
    }
    LOGGER.log(INFO, "DocLine class {0}", docLine.getClass().getCanonicalName());
   }
   if(arLineRec == null){
    LOGGER.log(INFO, "No AR Line for doc id {0}",docFi.getId());
    return null;
   }
   String invCrn = null;
   if(arLineRec.getPostType() != null){
    String postTypeCode = arLineRec.getPostType().getPostTypeCode();
    LOGGER.log(INFO,"postTypeCode {0}",postTypeCode);
   
    if(postTypeCode.equalsIgnoreCase("arCrn")){
     invCrn = "CRN";
    }else if(postTypeCode.equalsIgnoreCase("arInv")){
     invCrn = "INV";
    }
   }
   if(invCrn == null){
    throw new BacException("Invalid posting type");
   }
   LOGGER.log(INFO, "invCrn is {0}", invCrn);
   double totalAmnt = arLineRec.getInvoice().getTotalAmount();
   arLineRec.setDocAmount(totalAmnt);
   arLineRec.setHomeAmount(totalAmnt);
   
   lineNum++;
   LOGGER.log(INFO, "lineNum is {0}", lineNum);
   arLineRec.setLineNum(lineNum);
   DocLineAr arLine = buildDocLineAr(arLineRec,docFi,view);
   arLine.setDocNumber(docFi.getDocNumber());
   LOGGER.log(INFO, "arline DB post type {0}", arLine.getPostType().getPostTypeCode());
   docLines.add(arLine);
   
   
   // Update AR account balances
    ArAccount arAccount = arLine.getArAccount();
    
  
  arApAccount = arAccount;
  partnerType = "AR";
  // set AR account balance
  try{
   arAccount = arAccount.updatePeriodBalance(arLine, docFi.getFisYear(), docFi.getFisPeriod());
   
  }catch(BacException ex){
   // period balance does not exist
   LOGGER.log(INFO, "AR balance not found for account id {0} year {1} period {2}", 
           new Object[]{arAccount.getId(),docFi.getFisYear(), docFi.getFisPeriod()});
  }
  double arAcBal = arAccount.getAccountBalance();
  arAcBal = arAcBal + totalAmnt;
  arAccount.setAccountBalance(arAcBal);
 
  LOGGER.log(INFO, "arLine post type after set balances  {0}", arLine.getPostType().getPostTypeCode());
  
  // Add Invoice to sub-ledger line
  DocInvoiceArRec invRec = arLineRec.getInvoice();
  invRec.setCreatedBy(userRec);
  invRec.setCreatedOn(crDate);
  DocInvoiceAr docInvoiceAr = buildDocInvoiceAr(arLineRec.getInvoice(),docFi.getCompany().getId(),invCrn,
    view);
  LOGGER.log(INFO, "docInvoiceAr crn number {0}", docInvoiceAr.getInvoiceNumber());
  //docInvoiceAr.setInvoiceNumber(invCrn);
  
  docInvoiceAr.setFiDocument(docFi);
  docFi.setDocInvoiceAr(docInvoiceAr);
  docFi.setDocLines(docLines);
  LOGGER.log(INFO, "End of arLine add post code {0}", arLine.getPostType().getPostTypeCode());
  LOGGER.log(INFO, "Doc lines at end of assArLine {0}", docFi.getDocLines().size());
  docLines = docFi.getDocLines();
  ListIterator<DocLineBase> docLinesLi2 = docLines.listIterator();
  while(docLinesLi2.hasNext()){
   DocLineBase lineBase = docLinesLi2.next();
   if(lineBase.getClass().getCanonicalName().endsWith("DocLineAR")){
    DocLineAr docLineAr = (DocLineAr)lineBase;
    LOGGER.log(INFO, "Ar line post type code {0}", docLineAr.getPostType().getPostTypeCode());
   }
   
  }
  
  // create Reconciliation account entry and attach to docLineAr
  FiGlAccountComp reconAcGl = arAccount.getReconciliationAc();
  
  addSubLedgerGlReconLine((DocLineSubLedgerIF)arLine, reconAcGl,fndBals);
  
   
  return docFi;
  }
  
  /**
   * Add a GL reconciliation line to an AR posting. The GL line is attached to the AR line
   * @param recDoc
   * @param receiptLineDb 
   */
  private void addSubLedGlRecLine(DocFi doc,DocLineAr arLine, LineTypeRule lineType ){
   LOGGER.log(INFO, "addSubLedGlRecLine is called with doc {0} ar line {1}", new Object[]{doc,arLine});
   FiGlAccountComp reconGlAc = arLine.getArAccount().getReconciliationAc();
   double docAmount = arLine.getDocAmount();
   // build GL line
   DocLineGl reconLine = new DocLineGl();
   reconLine.setAutoGenerated(true);
   reconLine.setComp(doc.getCompany());
   reconLine.setCreateBy(doc.getCreatedBy());
   reconLine.setCreateDate(doc.getCreateOn());
   reconLine.setDocAmount(docAmount);
   reconLine.setGlAccount(reconGlAc);
   reconLine.setHomeAmount(docAmount);
   reconLine.setLineNum(1l);
   reconLine.setLineText(arLine.getLineText());
   reconLine.setLineType(lineType);
   PostType pstTy = this.sysBuff.getPostTypeForPCode("glRecDr");
   reconLine.setPostType(pstTy);
   reconLine.setReconcilForArLine(arLine);
   String sortTxt = this.buildGlLineSortOrderTxt(doc, reconGlAc.getSortOrder(), arLine);
   reconLine.setSortOrder(sortTxt);
   em.persist(reconLine);
   
   //Find Period balance
   FiPeriodBalance perBal;
   int year = doc.getFisYear();
   int period = doc.getFisPeriod();
   Query q = em.createNamedQuery("getGlAccPerBal");
   q.setParameter("glAc", reconGlAc);
   q.setParameter("fisYear", year);
   q.setParameter("fisPer", period);
   q.setParameter("balType", FiPeriodBalance.GL_ACT);
   try{
    perBal = (FiPeriodBalance)q.getSingleResult();
    LOGGER.log(INFO, "Line 378 perBal id", perBal.getId());
    perBal.updateActualBalance(reconLine);
   }catch(NoResultException ex){
    LOGGER.log(INFO, "addSubLedGlRecLine no period balance found {0}",ex.getLocalizedMessage());
    perBal = new FiPeriodBalance();
    perBal.setBalPeriod(period);
    perBal.setBalYear(year);
    perBal.setBalanceType(FiPeriodBalance.GL_ACT);
    perBal.setCreatedBy(doc.getCreatedBy());
    perBal.setCreated(doc.getCreateOn());
    perBal.setFiGlAccountComp(reconGlAc);
    em.persist(perBal);
    perBal.updateActualBalance(reconLine);
    //throw new BacException("Period balance not found");
   }catch(NonUniqueResultException ex){
    LOGGER.log(INFO, "addSubLedGlRecLine {0}",ex.getLocalizedMessage());
    throw new BacException("Multiple Period balances found");
   }
   
  }
  
 /**
  * Adds GL reconciliation lines by restricted fund balances 
  * @param receiptLineDb
  * @param fndBals
  * @param doc
  * @param lineType
  * @param usr
  * @param crDate
  * @return List of reconciliation lines
  */ 
  private List<DocLineGl> addSubLedGlRecLnFnds(DocLineSubLedgerIF subLedLine, 
          List<RestrictedFundBal> fndBals,DocFi doc, LineTypeRule lineType,Locale loc, User usr, 
          Date crDate ){
   List<DocLineGl> retLines = new ArrayList<>();
   LOGGER.log(INFO, "addSubLedGlRecLnFnds called arline {0} bals {1} line type {2}", new Object[]{
    subLedLine,fndBals, lineType });
   boolean subLedFound = false;
   double unallocAmnt = 0;
   double allocAmnt = 0;
   if(subLedLine.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
    subLedFound = true;
    DocLineAr arLine = (DocLineAr)subLedLine;
    FiGlAccountComp reconAc = arLine.getArAccount().getReconciliationAc();
    double totalReceivable = arLine.getDocAmount();
    List<DocLineGl> reclines = arLine.getReconiliationLines();
    if(reclines == null){
     reclines = new ArrayList<>();
    }
    if(fndBals != null){
     // there exists at least 1 restricted fund
     for(RestrictedFundBal arFundBal : fndBals){
      LOGGER.log(INFO, "Add rec ac for restricted fnd {0}", arFundBal.getFund());
      
      DocLineGl glLine = buildDocLineGlRecon(arLine, reconAc,arFundBal.getAmount(),arFundBal.getFund());
      LOGGER.log(INFO, "buildDocLineGlRecon returns {0}",glLine);
      glLine.setReconcilForArLine(arLine);
      List<DocLineGl> recLines = arLine.getReconiliationLines();
      LOGGER.log(INFO, "recLines {0}", recLines);
      if(recLines == null){
       recLines = new ArrayList<>();
      }
      recLines.add(glLine);
      arLine.setReconiliationLines(recLines);
      allocAmnt = allocAmnt + arFundBal.getAmount();
      
     }
     LOGGER.log(INFO, "Allocated {0} totalReceivable {1} ", new Object[]{allocAmnt,totalReceivable});
     
    }
   }
   return null;//retLines;
  }
  private DocLineSubLedgerIF addSubLedgerGlReconLine(DocLineSubLedgerIF subLedgerLine,
          FiGlAccountComp reconAc, List<RestrictFundBalance> fndBals){
   LOGGER.log(INFO, "add recon line called with line type {0} ", subLedgerLine.getClass().getSimpleName());
   String subLedger = subLedgerLine.getClass().getSimpleName();
   boolean subLedFound = false;
   double unallocAmnt = 0;
   double allocAmnt = 0;
   if(subLedger.equalsIgnoreCase("DocLineAR")){
    
    subLedFound = true;
    
    DocLineAr line = (DocLineAr)subLedgerLine;
    double totalReceivable = line.getDocAmount();
    
    List<DocLineGl> reclines = line.getReconiliationLines();
    if(reclines == null){
     reclines = new ArrayList<>();
    }
    if(fndBals != null){
    LOGGER.log(INFO, "fndBals {0}", fndBals);
    ListIterator<RestrictFundBalance> fndBalsLi = fndBals.listIterator();
    while(fndBalsLi.hasNext()){
     RestrictFundBalance fndBal = fndBalsLi.next();
     FundRec fndRec = fndBal.getFund();
     Fund fnd = this.getRestrictedFund(fndBal.getFund().getId());
     double balAmnt = fndBal.getAmount(); 
     if(fndBal != null){
      
     }
     DocLineGl glLine = this.buildDocLineGlRecon(subLedgerLine, reconAc,balAmnt,fnd);
     glLine.setReconcilForArLine(line);
     LOGGER.log(INFO, "glLine id {0} has ar line {1}", new Object[]{glLine.getId(),glLine.getReconcilForArLine().getId()});
     reclines.add(glLine);
     allocAmnt = allocAmnt + balAmnt;
    }
   }
    
   unallocAmnt = totalReceivable - allocAmnt;
   if(unallocAmnt != 0){
    LOGGER.log(INFO, "add non restricted fund reconciliation line");
    DocLineGl glLine = buildDocLineGlRecon(subLedgerLine, reconAc,unallocAmnt,null);
    LOGGER.log(INFO, "rec account {0}", glLine);
    LOGGER.log(INFO, "arLine {0}", line);
    glLine.setReconcilForArLine(line);
    reclines.add(glLine);
    LOGGER.log(INFO, "glLine id {0} has ar line {1}", new Object[]{glLine.getId(),glLine.getReconcilForArLine().getId()});
   }
   line.setReconiliationLines(reclines);
   LOGGER.log(INFO, "Reconciliation lines {0}", reclines);
    
    
   } // end of AR line processing 
   else if(subLedger.equalsIgnoreCase("DocLineAP")){
    // process AP reconciliation line
    subLedFound = true;
    DocLineAp line = (DocLineAp)subLedgerLine;
    double totalPayable = line.getDocAmount();
    
    List<DocLineGl> reclines = line.getReconDocLines();
    if(reclines == null){
     reclines = new ArrayList<>();
    }
    if(fndBals != null){
    LOGGER.log(INFO, "fndBals {0}", fndBals);
    ListIterator<RestrictFundBalance> fndBalsLi = fndBals.listIterator();
    while(fndBalsLi.hasNext()){
     RestrictFundBalance fndBal = fndBalsLi.next();
     FundRec fndRec = fndBal.getFund();
     Fund fnd = this.getRestrictedFund(fndBal.getFund().getId());
     double balAmnt = fndBal.getAmount(); 
     if(fndBal != null){
      
     }
     DocLineGl glLine = this.buildDocLineGlRecon(subLedgerLine, reconAc,balAmnt,fnd);
     glLine.setReconGlLnForApLine(line);
     
     LOGGER.log(INFO, "glLine id {0} has ar line {1}", new Object[]{glLine.getId(),glLine.getReconcilForArLine().getId()});
     reclines.add(glLine);
     allocAmnt = allocAmnt + balAmnt;
    }
   }
    
   unallocAmnt = totalPayable - allocAmnt;
   if(unallocAmnt != 0){
    LOGGER.log(INFO, "add non restricted fund reconciliation line");
    DocLineGl glLine = buildDocLineGlRecon(subLedgerLine, reconAc,unallocAmnt,null);
    LOGGER.log(INFO, "rec account {0}", glLine);
    LOGGER.log(INFO, "arLine {0}", line);
    glLine.setReconGlLnForApLine(line);
    reclines.add(glLine);
    LOGGER.log(INFO, "glLine id {0} has ar line {1}", new Object[]{glLine.getId(),glLine.getReconcilForArLine().getId()});
   }
   line.setReconDocLines(reclines);
   LOGGER.log(INFO, "Reconciliation lines {0}", reclines);
    
   }
   
    
   if(!subLedFound){
    throw new BacException("addSubLedgerGlReconLine could not process sub-ledger "+subLedger);
   }
   
   return subLedgerLine;
  }
  private DocFi addArGLLines(DocFi docFi,List<SalesPartFiLineRec> lines, 
          CompanyBasic comp, 
          User usr, Date crDate,String view, List<DocLineBaseRec> docLinesRec, 
          DocFiRec docRec, 
          LineTypeRule glLineType, PostType glPostType,VatReturnRec vatReturn, String pg){
   
   LOGGER.log(INFO, "addArGLLines called with doc {0} lines {1}", new Object[]{docFi, lines});
   LOGGER.log(INFO, "addArGLLines called with glPostType {0}", glPostType);
   
   ArrayList<DocGlVatCodeSummary> vatSplit = new ArrayList<>();
   DocLineArRec arLineRec = null;
   boolean foundArLine = false;
   List<FiGlAccountComp> glAcs = null;
   List<FiPeriodBalance> glActBalances = null;
   List<FiPeriodBalance> glAcCostCentAcBalances = new ArrayList<>();
   List<ProgramAccount> progActs = new ArrayList<>();
   List<FiPeriodBalance> programAcBalances = new ArrayList<>();
   List<CostAccountDirect> costCentAccs = new ArrayList<>();
  
   ListIterator<DocLineBaseRec> docLinesLi = docLinesRec.listIterator();
   while(docLinesLi.hasNext() && !foundArLine){
    DocLineBaseRec docLine = docLinesLi.next();
    if(docLine.getClass().getCanonicalName().endsWith("DocLineArRec")){
     arLineRec = (DocLineArRec)docLine;
     foundArLine = true;
     LOGGER.log(INFO, "Found AR line with posting type: {0}", arLineRec.getPostType().getPostTypeCode());
    }
    LOGGER.log(INFO, "DocLine class {0}", docLine.getClass().getCanonicalName());
   }
  ListIterator<SalesPartFiLineRec> partListIt = lines.listIterator();
  while(partListIt.hasNext()){
   SalesPartFiLineRec partLine = partListIt.next();
   SalesPartCompanyRec partComp = partLine.getPartComp();
  LOGGER.log(INFO,"add GL lines getSalesAccount() id {0} gl Ac ref {1} vat code {2}",
          new Object[]{
   partLine.getPartComp().getSalesAccount().getId(), partLine.getPartComp().getSalesAccount().getCoaAccount().getRef(),
   partLine.getVatCode()
  } ); 
  List<DocLineBase> docLines = docFi.getDocLines();
  if(glAcs == null){
   glAcs = new ArrayList<>();
  }
  long lineNum = docLines.size();
   double lineAmount;
   FiGlAccountComp glAc = null;
   DocLineGl docLineGl;
   SortOrder glAcSortOrder;
   String sortText;
   lineAmount = partLine.getLineTotal();
   VatCodeCompanyRec lineVatCode = partLine.getVatCode();//partLine.getVatCode();
   LOGGER.log(INFO, "lineVatCode {0}", lineVatCode);
   // sales account
   LOGGER.log(INFO, "doc gl acs {0}",glAcs);
   FiGlAccountComp salesGlAc = this.getGlAccount(glAcs, partLine.getPartComp().getSalesAccount());
   LOGGER.log(INFO, "salesGlAc {0} glaccounts {1}", new Object[]{salesGlAc,glAcs});
   
   ListIterator<FiGlAccountComp> glacsLi = glAcs.listIterator();
   boolean glAcFound = false;
  
   FiPeriodBalance salesAcBal;
   try{
   salesAcBal = salesGlAc.getActualPeriodBalance(docFi.getFisYear(), docFi.getFisPeriod());
   }catch(BacException ex){
    LOGGER.log(INFO, "Balance not found for GL account id {0} year {1} period {2}", new Object[]{
     salesGlAc.getId(),docFi.getFisYear(), docFi.getFisPeriod() });
    salesAcBal = new FiPeriodBalance();
    salesAcBal.setBalPeriod(docFi.getFisPeriod());
    salesAcBal.setBalYear(docFi.getFisYear());
    salesAcBal.setBalanceType(0);
    salesAcBal.setFiGlAccountComp(salesGlAc);
    em.persist(salesAcBal);
    salesGlAc.addActualBalance(salesAcBal);
   }
   
   LOGGER.log(INFO, "gl account {0} gl ac balances {1}", new Object[]{glAc,glActBalances});
   
   glAcSortOrder = salesGlAc.getSortOrder();
   docLineGl = buildGlDocLine(partLine, salesGlAc, lineAmount);
   sortText = buildGlLineSortOrderTxt(docRec, glAcSortOrder,  arLineRec);
   docLineGl.setSortOrder(sortText);
   lineNum++;
   docLineGl.setLineNum(lineNum);
   docLineGl.setDocHeaderBase(docFi);
   docLineGl.setLineType(glLineType);
   docLineGl.setPostType(glPostType);
   
   salesGlAc = updateGlAcBalance(salesGlAc, salesAcBal, docLineGl);
   docLines.add(docLineGl);
   
   //Cost centre balance update required?
   if(partLine.getCostCent().getId()!= null){
    LOGGER.log(INFO, "partLine.getCostCent() {0}", partLine.getCostCent());
    LOGGER.log(INFO, "partLine.getCostCent() id {0}", partLine.getCostCent().getId());
    LOGGER.log(INFO, "Add sales doc to Cost Centre balances ");
    //CC update required
    ListIterator<CostAccountDirect> costCentAcLi = costCentAccs.listIterator();
    CostAccountDirect costCentAc = null;
    boolean costCentAcFound = false;
    while(costCentAcLi.hasNext() && !costCentAcFound){
     CostAccountDirect costCentAcT = costCentAcLi.next();
     if(costCentAcT.getCostCentre().getId() == partLine.getCostCent().getId() &&
         costCentAcT.getGlAccount().getId() ==  partLine.getGlaccount().getId()){
      costCentAc = costCentAcT;
      costCentAcFound = true; 
     }
    }
    if(!costCentAcFound){
     costCentAc =  costCentDM.getCostAccountByCostCentGlAc(partLine.getGlaccount(), 
            partLine.getCostCent());
     LOGGER.log(INFO, "costCentAc found from DB layer {0}", costCentAc);
     if(costCentAc == null){
      CostAccountDirectRec costCentAcRec = 
         costCentDM.createCostAccountDirect(partLine.getGlaccount(), partLine.getCostCent(), 
             docRec.getCreatedBy(), crDate, pg);
      costCentAc = em.find(CostAccountDirect.class, costCentAcRec.getId(), OPTIMISTIC);
     }
     costCentAccs.add(costCentAc);
    }
    LOGGER.log(INFO, "Cost entre Ac{0}",costCentAc);
    FiPeriodBalance costCentAcBal = null;
    // have we already got the cost centre ac balance
    ListIterator<FiPeriodBalance> costCentAcBalLi = glAcCostCentAcBalances.listIterator();
    boolean costCentAcBalFound = false;
    while(costCentAcBalLi.hasNext() && !costCentAcBalFound ){
     FiPeriodBalance costCentAcBalT = costCentAcBalLi.next();
     if(costCentAcBalT.getCostAccountActual().getId()  == costCentAc.getId() && 
             costCentAcBalT.getBalPeriod() == docFi.getFisPeriod() &&
             costCentAcBalT.getBalYear() == docFi.getFisYear()){
      costCentAcBal = costCentAcBalT;
      costCentAcBalFound = true;
     }
    }
    if(!costCentAcBalFound){
     try{
      LOGGER.log(INFO, "docFi {0}", docFi);
      LOGGER.log(INFO, "docFi.getFisYear() {0}", docFi.getFisYear());
      LOGGER.log(INFO, "docFi.getFisPeriod() {0}", docFi.getFisPeriod());
      costCentAcBal = costCentAc.getActualBalance(docFi.getFisYear(), docFi.getFisPeriod());
      glAcCostCentAcBalances.add(costCentAcBal);
     }catch(BacException ex){
      costCentAcBal = new FiPeriodBalance();
      costCentAcBal.setBalPeriod(docFi.getFisPeriod());
      costCentAcBal.setBalYear(docFi.getFisYear());
      costCentAcBal.setBalanceType(FiPeriodBalance.CC_ACT);
      costCentAcBal.setCostAccountActual(costCentAc);
      em.persist(costCentAcBal);
      costCentAc.addActualBalance(costCentAcBal);
      glAcCostCentAcBalances.add(costCentAcBal);
     }
    }
    LOGGER.log(INFO, "Cost entre Ac balance: {0}",costCentAcBal);
    costCentAcBal.updateActualBalance(docLineGl);
    if(docLineGl.getPostType().isDebit()){
     List<DocLineGl> costCentDebitLines = costCentAcBal.getCostDebitDocLines();
     costCentDebitLines.add(docLineGl);
     costCentAcBal.setCostDebitDocLines(costCentDebitLines);
    }else{
     List<DocLineGl> costCentCreditLines = costCentAcBal.getCostCreditLines();
     costCentCreditLines.add(docLineGl);
     costCentAcBal.setCostCreditLines(costCentCreditLines);
    }
   }
   if(partLine.getProg().getId() != null){
    LOGGER.log(INFO, "Add sales doc to program balances ");
    LOGGER.log(INFO, "usr {0} docRec.createdBy {1}", new Object[]{usr,docRec.getCreatedBy()});
    // project costs required update project balances
    ListIterator<ProgramAccount> progActsLi = progActs.listIterator();
    boolean progActFound = false;
    ProgramAccount progAct = null;
    while(progActsLi.hasNext() && !progActFound){
     ProgramAccount progActT = progActsLi.next();
     if(progActT.getProgram().getId() == partLine.getProg().getId() && 
             progActT.getGlAccount().getId() == partLine.getGlaccount().getId()){
      progAct = progActT;
      progActFound = true;
     }
    }
    FiPeriodBalance progAcBal = null;
    if(!progActFound){
     progAct = progDM.getProgActByGlAc(partLine.getGlaccount(), partLine.getProg(),
            true, docRec.getCreatedBy(), crDate, view);
     progActs.add(progAct);
    }
    ListIterator<FiPeriodBalance> progActBalsLi = programAcBalances.listIterator();
    boolean progActBalFound = false;
    while(progActBalsLi.hasNext() && !progActBalFound){
     FiPeriodBalance progAcBalT =  progActBalsLi.next();
     if(progAcBalT.getProgramCostAccount().getId() == progAct.getId() && 
             progAcBalT.getBalPeriod() == docFi.getFisPeriod() &&
             progAcBalT.getBalYear() == docFi.getFisYear()){
      progAcBal = progAcBalT;
      progActBalFound = true;
     }
    }
    if(!progActBalFound){
     try{
      progAcBal = progAct.getProgrammeCostBalance(docFi.getFisYear(), docFi.getFisPeriod());
      programAcBalances.add(progAcBal);
     }catch(BacException ex){
      progAcBal = new FiPeriodBalance();
      progAcBal.setBalPeriod(docFi.getFisPeriod());
      progAcBal.setBalYear(docFi.getFisYear());
      progAcBal.setBalanceType(FiPeriodBalance.PROG_ACT);
      progAcBal.setProgramCostAccount(progAct);
      em.persist(progAcBal);
      progAct.addProgrammeCostBalance(progAcBal);
      programAcBalances.add(progAcBal);
     }
    }
    progAcBal.updateActualBalance(docLineGl);
    if(docLineGl.getPostType().isDebit()){
     List<DocLineGl> progDebitLines = progAcBal.getProjectDebitDocLines();
     LOGGER.log(INFO, "progAcBal {0}", progAcBal);
     LOGGER.log(INFO, "progDebitLines {0}", progDebitLines);
     progDebitLines.add(docLineGl);
     progAcBal.setProjectDebitDocLines(progDebitLines);
    }else{
     List<DocLineGl> progCreditLines = progAcBal.getProjectCreditDocLines();
     progCreditLines.add(docLineGl);
     progAcBal.setProjectCreditDocLines(progCreditLines);
    }
    
   } 
   
   
   if(partLine.getFund() != null){
    // need to add to restricted fund balance
    LOGGER.log(INFO, "Add restricted bal to gl ac {0} fund {1}", new Object[]{
     salesGlAc.getCoaAccount().getRef(),partLine.getFund().getFndCode() });
    FiPeriodBalance salesActRestrBal;
    try{
     salesActRestrBal = salesGlAc.getRestrictedBalance(docFi.getFisYear(), 
             docFi.getFisPeriod(),partLine.getFund()); 
    }catch(BacException ex){
     salesActRestrBal = new FiPeriodBalance();
     salesActRestrBal.setBalPeriod(docFi.getFisPeriod());
     salesActRestrBal.setBalYear(docFi.getFisYear());
     salesActRestrBal.setBalanceType(FiPeriodBalance.RESTRICTED_ACT);
     salesActRestrBal.setFiGlAccountComp(glAc);
     Fund fnd = em.find(Fund.class, partLine.getFund().getId(), OPTIMISTIC);
     salesActRestrBal.setRestrictedFund(fnd);
     
    }
    salesGlAc = updateGlAcBalance(salesGlAc, salesActRestrBal, docLineGl);
   }
   glacsLi = glAcs.listIterator();
   glAcFound = false;
   
   while(glacsLi.hasNext() && !glAcFound){
    FiGlAccountComp glAcUpdt = glacsLi.next();
    if(glAcUpdt == salesGlAc){
     glacsLi.set(salesGlAc);
     glAcFound = true;
     LOGGER.log(INFO, "Found sales gl ac to update in list of gl acs");
    }
   }
   LOGGER.log(INFO, "Pre-vatSplit docLineGlgetPostType() {0} ", docLineGl.getPostType().getPostTypeCode());
  
   vatSplit = setVatCodeSummary(vatSplit, docLineGl, lineVatCode,partLine.getPartComp().getCompany());
   
   if(partComp.isCostOfSalesAccounting()){
    // cost of sales accounting required
    LOGGER.log(INFO, "Cost od sales accounting required");
    
    double cosLineAmount = partLine.getQty() * partComp.getStockValue();
    
    if(docLineGl.getCostAccount() != null && docLineGl.getCostAccount().getId() != 0){
     // Credit cost of sales
     FiGlAccountCompRec cosGlAcRec = partComp.getCosAccount();
     FiGlAccountComp cosGlAc = getGlAccount(glAcs, cosGlAcRec);
     FiPeriodBalance cosAcBal;
     try{
      cosAcBal = cosGlAc.getActualPeriodBalance(docFi.getFisYear(), docFi.getFisPeriod());
     }catch(BacException ex){
      LOGGER.log(INFO, "Balance not found for GL account id {0} year {1} period {2}", new Object[]{
      cosGlAc.getId(),docFi.getFisYear(), docFi.getFisPeriod() });
      cosAcBal = new FiPeriodBalance();
      cosAcBal.setBalPeriod(docFi.getFisPeriod());
      cosAcBal.setBalYear(docFi.getFisYear());
      cosAcBal.setBalanceType(FiPeriodBalance.GL_ACT);
      cosAcBal.setFiGlAccountComp(cosGlAc);
      em.persist(cosAcBal);
      cosGlAc.addActualBalance(cosAcBal);
     }
     lineNum = docLines.size();
     PostType cosPostType = this.sysBuff.getPostTypeForPCode("Credit");
     DocLineGl docCosLineGl = buildGlDocLine(partLine, cosGlAc, cosLineAmount);
     SortOrder costGlAcSortOrder = cosGlAc.getSortOrder();;
     String costGlAcSortText = buildGlLineSortOrderTxt(docRec, costGlAcSortOrder,  arLineRec);
     lineNum++;
     docCosLineGl.setSortOrder(costGlAcSortText);
     docCosLineGl.setLineNum(lineNum);
     docCosLineGl.setDocHeaderBase(docFi);
     docCosLineGl.setLineType(glLineType);
     docCosLineGl.setPostType(cosPostType);
     cosGlAc = updateGlAcBalance(cosGlAc, cosAcBal, docCosLineGl);
     docLines.add(docCosLineGl);
     
     cosAcBal.updateActualBalance(docCosLineGl); 
     glacsLi = glAcs.listIterator();
     glAcFound = false;
   
     while(glacsLi.hasNext() && !glAcFound){
      FiGlAccountComp glAcUpdt = glacsLi.next();
      if(glAcUpdt == cosGlAc){
       glacsLi.set(cosGlAc);
       glAcFound = true;
      LOGGER.log(INFO, "Found sales gl ac to update in list of gl acs");
      }
     }
    
    // debit stock
    FiGlAccountCompRec stockGlAcRec = partComp.getStockAccount();
    FiGlAccountComp stockGlAc = getGlAccount(glAcs, stockGlAcRec);
    FiPeriodBalance stockAcBal;
     try{
      stockAcBal = stockGlAc.getActualPeriodBalance(docFi.getFisYear(), docFi.getFisPeriod());
     }catch(BacException ex){
      LOGGER.log(INFO, "Balance not found for GL account id {0} year {1} period {2}", new Object[]{
      stockGlAc.getId(),docFi.getFisYear(), docFi.getFisPeriod() });
      stockAcBal = new FiPeriodBalance();
      stockAcBal.setBalPeriod(docFi.getFisPeriod());
      stockAcBal.setBalYear(docFi.getFisYear());
      stockAcBal.setBalanceType(FiPeriodBalance.GL_ACT);
      stockAcBal.setFiGlAccountComp(stockGlAc);
      em.persist(stockAcBal);
      stockGlAc.addActualBalance(stockAcBal);
    }
    lineNum = docLines.size();
    PostType stockPostType = this.sysBuff.getPostTypeForPCode("Debit");
    DocLineGl docStockLineGl = buildGlDocLine(partLine, stockGlAc, cosLineAmount);
    SortOrder stocktGlAcSortOrder = stockGlAc.getSortOrder();
    String stockGlAcSortText = buildGlLineSortOrderTxt(docRec, stocktGlAcSortOrder,  arLineRec);
    lineNum++;
    docStockLineGl.setSortOrder(stockGlAcSortText);
    docStockLineGl.setLineNum(lineNum);
    docStockLineGl.setDocHeaderBase(docFi);
    docStockLineGl.setLineType(glLineType);
    docStockLineGl.setPostType(stockPostType);
    stockGlAc = updateGlAcBalance(stockGlAc, stockAcBal, docCosLineGl);
    docLines.add(docStockLineGl);
    stockAcBal.updateActualBalance(docStockLineGl); 
    glacsLi = glAcs.listIterator();
    glAcFound = false;
   
    while(glacsLi.hasNext() && !glAcFound){
     FiGlAccountComp glAcUpdt = glacsLi.next();
     if(glAcUpdt == stockGlAc){
      glacsLi.set(stockGlAc);
      glAcFound = true;
     LOGGER.log(INFO, "Found stock gl ac to update in list of gl acs");
     }
    }
    
     
    
    }
   }
    
   /*
   //glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docFi,usr,crDate);
   if(docLineGl.getCostAccount() != null && docLineGl.getCostAccount().getId() != 0){
    // set Cost Account balances
    LOGGER.log(INFO, "Update cost account {0} balances ", docLineGl.getCostAccount().getId());
    glAcCostAcBalances = buildDocLineGlCostAcBalance(docLineGl, glAcCostAcBalances, docFi, usr, crDate);
   }
   
   if(docLineGl.getProgramCostAccount() != null && docLineGl.getProgramCostAccount().getId() != 0){
    // set program Account balances
    LOGGER.log(INFO, "Update cost account {0} balances ", docLineGl.getCostAccount().getId());
    glAcCostAcBalances = this.buildDocLineGlProgramAcBalance(docLineGl, glAcCostAcBalances, 
            docFi, usr, crDate);
   }
   // is cost of sales accounting required?
   LOGGER.log(INFO, "partLine.getPartComp().isCostOfSalesAccounting() {0}", 
           partLine.getPartComp().isCostOfSalesAccounting());
   if(partLine.getPartComp().isCostOfSalesAccounting() && (partLine.getPartComp().getCosAccount() != null &&
           partLine.getPartComp().getCosAccount().getId() != null)){
    LOGGER.log(INFO, "Add cost accounting lines");
    lineAmount = partLine.getPartComp().getStockValue().doubleValue();
    lineAmount = lineAmount * partLine.getQty();
    // debit cost of sales
    LOGGER.log(INFO, "partLine.getPartComp().getCosAccount() {0}", partLine.getPartComp().getCosAccount());
    glAc = em.find(FiGlAccountComp.class, partLine.getPartComp().getCosAccount().getId(), OPTIMISTIC);
    glAcSortOrder = glAc.getSortOrder();
    docLineGl = buildGlDocLine(partLine, glAc, lineAmount,comp, usr, crDate);
    sortText = this.buildGlLineSortOrderTxt(docRec, glAcSortOrder,  arLineRec);
    docLineGl.setSortOrder(sortText);
    lineNum++;
    docLineGl.setLineNum(lineNum);
    docLineGl.setDocHeaderBase(docFi);
    docLineGl.setLineType(glLineType);
    docLineGl.setPostType(glPostType);
    docLines.add(docLineGl);
    glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docFi,usr,crDate);
    // credit stock account
    glAc = em.find(FiGlAccountComp.class, partLine.getPartComp().getStockAccount().getId(), OPTIMISTIC);
    glAcSortOrder = glAc.getSortOrder();
    docLineGl = buildGlDocLine(partLine, glAc,lineAmount, comp, usr, crDate);
    sortText = this.buildGlLineSortOrderTxt(docRec, glAcSortOrder,  arLineRec);
    docLineGl.setSortOrder(sortText);
    lineNum++;
    docLineGl.setLineNum(lineNum);
    docLineGl.setDocHeaderBase(docFi);
    docLineGl.setLineType(glLineType);
    docLineGl.setPostType(glPostType);
    docLines.add(docLineGl);
    glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docFi,usr,crDate);
   }
   */
  }
  LOGGER.log(INFO, "vatSplit {0} vatReturn {1}", new Object[]{vatSplit, vatReturn});
  if(vatSplit != null && vatReturn != null){
   LOGGER.log(INFO, "Doc lines before add VAT lines {0}", docFi.getDocLines().size());
   docFi = addArVatLines(docFi, vatSplit, vatReturn,usr,crDate,view,docRec ,glLineType);
   LOGGER.log(INFO, "Doc lines After add VAT lines {0}", docFi.getDocLines().size());
  }
  return docFi;
  }
  
  private AuditDocFi buildAuditDocFi(DocFi doc, User usr, Date audDate, String pg, char usrAct){
   AuditDocFi aud = new AuditDocFi();
   aud.setAuditDate(audDate);
   aud.setCreatedBy(usr);
   aud.setDoc(doc);
   aud.setSource(pg);
   aud.setUsrAction(usrAct);
   em.persist(aud);
   return aud;
  } 
  private AuditDocFiPartial  buildAuditDocFiPartial(DocFiPartial doc, User usr, String pg, char usrAction){
   AuditDocFiPartial aud = new AuditDocFiPartial();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setDoc(doc);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
 
 private AuditDocFiTemplateBase  buildAuditDocFiTemplate(DocFiTemplAccrPrePay doc, User usr, String pg, char usrAction){
   AuditDocFiTemplateBase aud = new AuditDocFiTemplateBase();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setTmpl(doc);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
 }
 
 private AuditDocInvoiceAr buildAuditDocInvoiceAr(DocInvoiceAr inv, User usr,String pg, char usrAction){
  AuditDocInvoiceAr aud = new AuditDocInvoiceAr();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setInv(inv);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
  
 }
 
 private AuditDocLine buildAuditDocLine(DocLineBase ln, User usr, String pg, char usrAction){
   AuditDocLine aud = new AuditDocLine();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setDocLine(ln);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
 
 private AuditDocLinePartial buildAuditDocLinePartial(DocLineBasePartial ln, User usr, String pg, char usrAction){
   AuditDocLinePartial aud = new AuditDocLinePartial();
   aud.setAuditDate(new Date());
   aud.setCreatedBy(usr);
   aud.setSource(pg);
   aud.setUsrAction(usrAction);
   em.persist(aud);
   return aud;
  }
 
 private AuditDocLineTemplate buildAuditDocLineTemplate(DocLineTemplate ln, User usr, 
    String pg, char usrAction){
  AuditDocLineTemplate aud = new AuditDocLineTemplate();
  aud.setAuditDate(new Date());
  aud.setCreatedBy(usr);
  aud.setDocLine(ln);
  aud.setSource(pg);
  aud.setUsrAction(usrAction);
  em.persist(aud);
  return aud;
  
 }
 
 
  private FiGlAccountComp updateGlAcBalance(FiGlAccountComp glAc, FiPeriodBalance balance, 
          DocLineGl docLineGl  ){
   LOGGER.log(INFO, "DocDM.updateGlAcBalance called with glAc {0} balance {1}  line {2}", 
           new Object[]{glAc,balance,docLineGl});
   
   LOGGER.log(INFO, "Balance type {0}", balance.getBalanceType());
   double bfwd = balance.getBfwdDocAmount();
   double perCredit =balance.getPeriodCreditAmount();
   double perDebit =balance.getPeriodDebitAmount();
   double perAmnt = balance.getPeriodDocAmount();
   double cfwd = balance.getCfwdDocAmount();
   if(docLineGl.getPostType().isDebit()){
    //debit line
    LOGGER.log(INFO, "debit gl line");
    perDebit = perDebit + docLineGl.getDocAmount();
    balance.setPeriodDebitAmount(perDebit);
   }else{
    LOGGER.log(INFO, "credit gl line");
    perCredit = perCredit + docLineGl.getDocAmount(); 
    balance.setPeriodCreditAmount(perCredit);
   }
   perAmnt = perDebit - perCredit;
   balance.setPeriodDocAmount(perAmnt);
   cfwd = bfwd + perAmnt;
   balance.setCfwdDocAmount(cfwd);
   
   // update period balance assigned to glac
   ListIterator<FiPeriodBalance> perBalLi = glAc.getPeriodBalances().listIterator();
   while(perBalLi.hasNext()){
    FiPeriodBalance perBal = perBalLi.next();
    if(perBal == balance){
     perBalLi.set(perBal);
     return glAc;
    }
   }
   
   return glAc;
  }
  
  // called from add GL lines after gl lines added
  /**
   * Adds VAT lines to document and adds VAT return line to the appropriate VAT return
   * @param doc
   * @param docVatSum
   * @param vatReturnRec
   * @param usr
   * @param crDate
   * @param view
   * @param docRec
   * @param glLineType
   * @return Add
   */
  private DocFi addArVatLines(DocFi doc,List<DocGlVatCodeSummary> docVatSum, 
          VatReturnRec vatReturnRec,
          User usr, Date crDate, String view,DocFiRec docRec,
          LineTypeRule glLineType){
   
   LOGGER.log(INFO, "addArVatLines called vatReturn {0}", vatReturnRec);
   
   VatReturn vatReturn = em.find(VatReturn.class, vatReturnRec.getId(), OPTIMISTIC);
   //this.setVatLines(docLines, null, compRec, docHdr, glLineType, glDrPostTypeRec, glCrPostTypeRec, user, crDate, arlineRec)
   VatRegScheme regScheme = em.find(VatRegScheme.class, vatReturn.getVatRegScheme().getId(), 
           OPTIMISTIC);
   boolean foundArLine = false;
   DocLineAr arLine = null;
   ArAPAccountIF partnerAc = null;
    
   List<DocLineBase> docLines = doc.getDocLines();
   long lineNum = docLines.size();
   ListIterator<DocLineBase> docLinesBaseLi = docLines.listIterator();
   String invCrn;
   String taxType = null;
   while(docLinesBaseLi.hasNext() && !foundArLine){
    DocLineBase docLine = docLinesBaseLi.next();
    LOGGER.log(INFO, "docLine type {0}", docLine.getClass().getCanonicalName());
    if(docLine.getClass().getCanonicalName().endsWith("DocLineAR")){
     arLine = (DocLineAr)docLine;
     foundArLine = true;
     partnerAc = arLine.getArAccount();
     invCrn = arLine.getPostType().getPostTypeCode();
     //set VAT tax transaction type
     if(invCrn.equalsIgnoreCase("arInv")){
      taxType = "suppDomInv";
     }else if(invCrn.equalsIgnoreCase("arCrn")){
      taxType = "suppDomCrn";
     }
    }
   }
   
    
   LOGGER.log(INFO, "taxType {0}", taxType);
   List<DocLineBaseRec> docLinesRec = docRec.getDocLines();
   DocLineArRec arLineRec = null;
   foundArLine = false;
   ListIterator<DocLineBaseRec> docLinesLi = docLinesRec.listIterator();
   while(docLinesLi.hasNext() && !foundArLine){
    DocLineBaseRec docLine = docLinesLi.next();
    if(docLine.getClass().getCanonicalName().endsWith("DocLineArRec")){
     arLineRec = (DocLineArRec)docLine;
     foundArLine = true;
    }
   }
   
   
   UserRec userRec = this.usrDM.getUserRecPvt(usr);
   ListIterator<DocGlVatCodeSummary> vatSumLi = docVatSum.listIterator();
   VatCodeCompanyRec vatCodeComp = null;
   
   while(vatSumLi.hasNext()){
    DocGlVatCodeSummary vatSumRec = vatSumLi.next();
    LOGGER.log(INFO, "vatSumRec vat code {0} post type {1}", new Object[]{vatSumRec.getVatCode().getVatCode().getCode(),vatSumRec.getPostType()});
    double vatAmnt = vatSumRec.getGoods() * vatSumRec.getVatCode().getVatCode().getRate();
    vatSumRec.setVatAmount(vatAmnt);
    LOGGER.log(INFO,"vatSumRec gl account id {0} vat code {1}", 
            new Object[]{vatSumRec.getGlAccount(),vatSumRec.getVatCode()});
    
    List<VatCodeCompanyRec> compVatCodes = vatSumRec.getVatCode().getVatCode().getVatCodeCompanies();
    LOGGER.log(INFO, "compVatCodes from vatSumRec.getVatCode() {0}", compVatCodes);
    if(compVatCodes == null){
     LOGGER.log(INFO, "about to call sysBuff.getCompVatCode with comp id {0} and vat code {1}", 
             new Object[]{docRec.getCompany().getId(),vatSumRec.getVatCode().getVatCode().getCode()});
     compVatCodes = this.sysBuff.getCompVatCode(docRec.getCompany(), vatSumRec.getVatCode());
     LOGGER.log(INFO, "compVatCodes from sysBuff {0}", compVatCodes);
    }
    ListIterator<VatCodeCompanyRec> compVatCodesLi = compVatCodes.listIterator();
    
    boolean vatCodeCompFound = false;
    while(compVatCodesLi.hasNext() && !vatCodeCompFound){
     VatCodeCompanyRec vatCodeCompT = compVatCodesLi.next();
     if(vatCodeCompT.getCompany().getId() == doc.getCompany().getId()){
      vatCodeCompFound = true;
      vatCodeComp = vatCodeCompT;
     }
    }
    
    
    FiGlAccountComp vatRateGlAc = em.find(FiGlAccountComp.class, vatCodeComp.getRateGlAccount().getId(), OPTIMISTIC);
    
    FiPeriodBalance glAcBal;
    try{
     glAcBal = vatRateGlAc.getActualPeriodBalance(doc.getFisYear(), doc.getFisPeriod());
    }catch(BacException ex){
     glAcBal = new FiPeriodBalance();
     glAcBal.setBalPeriod(doc.getFisPeriod());
     glAcBal.setBalYear(doc.getFisYear());
     glAcBal.setFiGlAccountComp(vatRateGlAc);
     em.persist(glAcBal);
    }
    
    
    LOGGER.log(INFO, "VAT rate GL account id {0}", vatRateGlAc.getId());
    LOGGER.log(INFO,"VAT rec values vat{0} goods {1}", new Object[]{vatSumRec.getVatAmount(), vatSumRec.getGoods()});
    SortOrder glAcSortOrder = vatRateGlAc.getSortOrder();
    double lineAmount = vatSumRec.getVatAmount();
    
    lineNum++;
    DocLineGlRec lineRec = new DocLineGlRec();
    lineRec.setComp(docRec.getCompany());
    //lineRec.setGlAccount( vatSumRec.getGlAccount());
    lineRec.setCreateBy(userRec);
    lineRec.setCreateDate(crDate);
    lineRec.setDocAmount(lineAmount);
    lineRec.setHomeAmount(lineAmount);
    lineRec.setReference1(taxType);
    lineRec.setLineText("VAT");
    lineRec.setLineNum(lineNum);
    
    /*if(vatSumRec.getFund() != null){
     lineRec.setRestrictedFund(vatSumRec.getFund());
    }*/
    String sortOrderTxt = buildGlLineSortOrderTxt(docRec, glAcSortOrder, arLineRec);
    lineRec.setSortOrder(sortOrderTxt);
    //lineRec.setPostType(vatSumRec.getPostType());
    
    DocLineGl glLine = this.buildDocLineGL(lineRec, view);
    glLine.setLineNum(lineNum);
    glLine.setAutoGenerated(true);
    PostType postType = this.sysBuff.getPostTypeForPCode(vatSumRec.getPostType().getPostTypeCode());
    glLine.setPostType(postType);
    glLine.setGlAccount(vatSumRec.getGlAccount());
    glLine.setLineType(glLineType);
    glLine.setDocHeaderBase(doc);
    glLine.setGlAccount(vatRateGlAc);
    
    docLines.add(glLine);
    LOGGER.log(INFO, "before bal update period bals doc debit {0} doc crdit {1}", 
            new Object[]{glAcBal.getPeriodDebitAmount(),glAcBal.getPeriodCreditAmount()});
    
    glAcBal.updateActualBalance(glLine);
    
    // add vat return line
    LOGGER.log(INFO, "About to ");
    VatCodeCompany vatCodeCompDB = em.find(VatCodeCompany.class, vatCodeComp.getId(), OPTIMISTIC);
    this.vatDM.addVatReturnLineArPvt(vatReturn, regScheme, partnerAc, doc, vatSumRec, vatCodeCompDB, glLine, taxType);
    LOGGER.log(INFO,"Called VAT set GL bal with GL ac id {0}",glLine.getGlAccount().getId());
   // glActBalances = buildDocLineGlAcBalance(glLine, glActBalances, docHdr,usr,crDate);
   }
   return doc;
  }
  /**
   * Builds sales invoice and creates the required DB entities
   * @param rec
   * @return 
   */
  private DocInvoiceAr buildDocInvoiceAr(DocInvoiceArRec rec, long compId,String invCrn, String pg){
   LOGGER.log(INFO, "buildDocInvoiceAr called with {0}", invCrn);
   DocInvoiceAr inv;
   boolean newInv = false;
   boolean changedInv = false;
   if(rec.getId() == null || rec.getId() == 0){
    inv = new DocInvoiceAr();
    User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
    inv.setCreatedBy(crUsr);
    inv.setCreatedOn(rec.getCreatedOn());
    em.persist(inv);
    newInv = true;
   }else{
    inv = em.find(DocInvoiceAr.class, rec.getId(), OPTIMISTIC);
   }
   
   if(newInv){
    inv.setGoodsAmount(rec.getGoodsAmount());
    inv.setVatAmount(rec.getVatAmount());
    inv.setTotalAmount(rec.getTotalAmount());
    if(rec.getOrderedBy() != null && rec.getOrderedBy().getId() != null && rec.getOrderedBy().getId() > 0){
     PartnerPerson orderedBy = em.find(PartnerPerson.class, rec.getOrderedBy().getId(), OPTIMISTIC);
     inv.setOrderedBy(orderedBy);
    }
    inv.setPrinted(rec.isPrinted());
    inv.setPurchaseOrderNumber(rec.getPurchaseOrderNumber());
    if(rec.getUploadOrderFileData() != null){
     inv.setUploadOrderFileName(rec.getUploadOrderFileName());
     inv.setUploadOrderFileData(rec.getUploadOrderFileData());
     inv.setUploadOrderContentType(rec.getUploadOrderContentType());
    }
    inv.setNote(rec.getNotes());
    String invNum = null;
    if(invCrn.equalsIgnoreCase("INV")){
     invNum = "I" + compDM.getCompDocNumber(compId, "INV_CRN");
    }else if(invCrn.equalsIgnoreCase("CRN")){
     invNum = "C" +compDM.getCompDocNumber(compId, "INV_CRN");
    }
    LOGGER.log(INFO, "Invoice/CRN number {0}", invNum);
    inv.setInvoiceNumber(invNum);
    inv.setDebit(rec.isDebit());
   }else{
    // has inv been changed
    User updtUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
    
    if(!Objects.equals(rec.getOrderedBy().getId(), inv.getOrderedBy().getId())){
     AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(inv, updtUsr, pg, 'U');
     aud.setFieldName("DOC_SL_ORDER_BY");
     if(inv.getOrderedBy() != null){
      aud.setOldValue(inv.getOrderedBy().getNameStructured());
     }
     if(rec.getOrderedBy() != null){
      aud.setNewValue(rec.getOrderedBy().getNameStructured());
     }
     if(rec.getOrderedBy() == null){
      inv.setOrderedBy(null);
     }else{
      PartnerPerson orderedBy = em.find(PartnerPerson.class, rec.getOrderedBy().getId(), OPTIMISTIC);
      inv.setOrderedBy(orderedBy);
     }
     changedInv = true;
    }
    
    if(rec.isPrinted() != inv.isPrinted()){
     AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(inv, updtUsr, pg, 'U');
     aud.setFieldName("DOC_SL_PRNTD");
     
     aud.setNewValue(String.valueOf(rec.isPrinted()));
     aud.setOldValue(String.valueOf(rec.isPrinted()));
     inv.setPrinted(rec.isPrinted());
     changedInv = true;
    }
    if(!StringUtils.equals(rec.getNotes(), inv.getNote())){
     AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(inv, updtUsr, pg, 'U');
     aud.setFieldName("DOC_SL_NOTE");
     aud.setOldValue(inv.getNote());
     aud.setNewValue(rec.getNotes());
     inv.setNote(rec.getNotes());
     changedInv = true;
    }
    if((rec.getPurchaseOrderDate() == null && inv.getPurchaseOrderDate() != null ) ||
      (rec.getPurchaseOrderDate() != null && inv.getPurchaseOrderDate() == null ) ||
      (rec.getPurchaseOrderDate() != null 
      && !inv.getPurchaseOrderDate().equals(rec.getPurchaseOrderDate()))){
     AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(inv, updtUsr, pg, 'U');
     aud.setFieldName("DOC_SL_PO_DT");
     if(inv.getPurchaseOrderDate() != null){
      aud.setOldValue(inv.getPurchaseOrderDate().toString());
     }
     if(rec.getPurchaseOrderDate() != null){
      aud.setNewValue(rec.getPurchaseOrderDate().toString());
     }
     inv.setPurchaseOrderDate(rec.getPurchaseOrderDate());
     changedInv = true;
    }
    
    if(!StringUtils.equals(rec.getPurchaseOrderNumber(), inv.getPurchaseOrderNumber())){
     AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(inv, updtUsr, pg, 'U');
     aud.setFieldName("DOC_SL_PO_NUM");
     aud.setOldValue(inv.getPurchaseOrderNumber());
     aud.setNewValue(rec.getPurchaseOrderNumber());
     inv.setPurchaseOrderNumber(rec.getPurchaseOrderNumber());
     changedInv = true;
    }
    if(changedInv){
     inv.setChangedBy(updtUsr);
     inv.setChangedOn(rec.getChangedOn());
    }
    
    
   }
   
   return inv;
  }

  private DocInvoiceArRec buildDocInvoiceArRec(DocInvoiceAr inv){
   LOGGER.log(INFO, "buildDocInvoiceAr called with {0}", inv);
   DocInvoiceArRec ret = new DocInvoiceArRec();
   
   UserRec crUsr = usrDM.getUserRecPvt(inv.getCreatedBy());
   ret.setId(inv.getId());
   ret.setCreatedBy(crUsr);
   ret.setCreatedOn(inv.getCreatedOn());
   
   
   if(inv.getChangedBy() != null){
    UserRec chgUsr = usrDM.getUserRecPvt(inv.getChangedBy());
    ret.setChangedBy(chgUsr);
    ret.setChangedOn(inv.getChangedOn());
   }
   ret.setGoodsAmount(inv.getGoodsAmount());
   ret.setVatAmount(inv.getVatAmount());
   ret.setTotalAmount(inv.getTotalAmount());
   if(inv.getOrderedBy() != null && inv.getOrderedBy().getId() != null 
           && inv.getOrderedBy().getId() > 0){
    PartnerPersonRec  orderedBy = mastDM.buildPartnerPersonRecPvt(inv.getOrderedBy());
    ret.setOrderedBy(orderedBy);
   }
   ret.setPrinted(inv.isPrinted());
   ret.setDebit(inv.isDebit());
   ret.setPurchaseOrderNumber(inv.getPurchaseOrderNumber());
   if(inv.getUploadOrderFileData() != null){
    ret.setUploadOrderFileName(inv.getUploadOrderFileName());
    ret.setUploadOrderFileData(inv.getUploadOrderFileData());
    ret.setUploadOrderContentType(inv.getUploadOrderContentType());
   }
   if(inv.getInvCrnPdf() != null){
    ret.setInvoicePdf(inv.getInvCrnPdf());
   }
   ret.setNotes(inv.getNote());
   ret.setInvoiceNumber(inv.getInvoiceNumber());
   return ret;
  }
  
  private DocTypeRec buildDocTypeRec(DocType d){
    DocTypeRec rec = new DocTypeRec();
    rec.setApAllowed(d.isApAllowed());
    rec.setArAllowed(d.isArAllowed());
    rec.setCode(d.getCode());
    if(d.getCreatedBy() != null){
      d.setCreated(d.getCreated());
      UserRec usr = usrDM.getUserRecPvt(d.getCreatedBy());
      rec.setCreatedBy(usr);
    }
    if(d.getChangedBy() != null){
      rec.setChangedOn(d.getChangedOn());
      UserRec usr = usrDM.getUserRecPvt(d.getChangedBy());
      rec.setChangedBy(usr);
    }
    if(d.getDocuments() != null){

    }
    rec.setGlAllowed(d.isGlAllowed());
    rec.setId(d.getId());
    rec.setName(d.getName());
    rec.setRevision(d.getRevision());
    rec.setTrAllowed(d.isTrAllowed());
    return rec;
  }

 private DocLineBaseRec buildDocLineBaseRecPvt(DocLineBase ln){
  DocLineBaseRec lnRec = new DocLineBaseRec(); 
  lnRec.setId(ln.getId());
  UserRec crUsr = usrDM.getUserRecPvt(ln.getCreateBy());
  lnRec.setCreateBy(crUsr);
  lnRec.setCreateDate(ln.getCreateDate());
  if(ln.getChangeBy() != null){
   UserRec chUsr = usrDM.getUserRecPvt(ln.getCreateBy());
   lnRec.setChangeBy(chUsr);
   lnRec.setChangeDate(ln.getChangeDate());
  }
  lnRec.setAccountRef(ln.getReference1());
  lnRec.setAutoGenerated(ln.isAutoGenerated());
  lnRec.setClearedLine(ln.isClearedLine());
  lnRec.setClearingDate(ln.getClearingDate());
  lnRec.setClearingLine(ln.isClearingLine());
  CompanyBasicRec co = this.sysBuff.getCompanyById(ln.getComp().getId());
  lnRec.setComp(co);
  lnRec.setDocAmount(ln.getHomeAmount());
  lnRec.setHomeAmount(ln.getHomeAmount());
  lnRec.setLineNum(ln.getLineNum());
  lnRec.setLineText(ln.getLineText());
  LineTypeRuleRec lnTy = sysBuff.getLineTypeRule(ln.getLineType());
  lnRec.setLineType(lnTy);
  lnRec.setNotes(ln.getNotes());
  lnRec.setOrginalAmount(ln.getOrginalAmount());
  lnRec.setPaidAmount(ln.getPaidAmount());
  PostTypeRec pt = sysBuff.getPostTypeForCode(ln.getPostType().getPostTypeCode());
  lnRec.setPostType(pt);
  lnRec.setReference1(ln.getReference1());
  lnRec.setReference2(ln.getReference2());
  lnRec.setSortOrder(ln.getSortOrder());
  
  return lnRec;
  
 }
  private DocLineBase buildDocLineBase(DocLineBase ln, DocLineBaseRec rec, String pg){
   boolean newLn = false;
   boolean lnChanged = false;
   if(rec.getId() == null){
    newLn = true;
   }
   if(newLn){
    User crUsr = em.find(User.class, rec.getCreateBy().getId());
    ln.setCreateBy(crUsr);
    ln.setCreateDate(rec.getCreateDate());
    ln.setAutoGenerated(rec.isAutoGenerated());
    if(rec.getBankLine() != null){
     DocBankLineBase bnkLn = em.find(DocBankLineBase.class, rec.getBankLine().getId(), OPTIMISTIC);
     ln.setBankLine(bnkLn);
    }
    if(rec.getBnkPaymentRun() != null){
     BnkPaymentRun payRun = em.find(BnkPaymentRun.class, rec.getBnkPaymentRun().getId(), OPTIMISTIC);
     ln.setBnkPaymentRun(payRun);
    }
    if(rec.getClearedByLine() != null){
     DocLineBase clrLn = em.find(DocLineBase.class, rec.getClearedByLine().getId(), OPTIMISTIC);
     ln.setClearedByLine(clrLn);
    }
    ln.setClearedLine(rec.isClearedLine());
    ln.setClearingDate(rec.getClearingDate());
    ln.setClearingLine(rec.isClearingLine());
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
    ln.setComp(comp);
    ln.setLineNum(rec.getLineNum());
    ln.setLineText(rec.getLineText());
    LineTypeRule lt = em.find(LineTypeRule.class, rec.getLineType().getId(), OPTIMISTIC);
    ln.setLineType(lt);
    ln.setNotes(rec.getNotes());
    ln.setOrginalAmount(rec.getOrginalAmount());
    ln.setPaidAmount(rec.getPaidAmount());
    if(rec.getPartPaymentLine() != null){
     DocLineBase pyDoc = em.find(DocLineBase.class, rec.getPartPaymentLine().getId(), OPTIMISTIC);
     ln.setPartPaymentLine(pyDoc);
    }
    PostType pt = em.find(PostType.class, rec.getPostType().getId(), OPTIMISTIC);
    ln.setPostType(pt);
    ln.setReference1(rec.getReference1());
    ln.setReference2(rec.getReference2());
    
   }else{
    //only limited changes allowed
    User chUsr = em.find(User.class, rec.getChangeBy().getId(), OPTIMISTIC);
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMdd");
    
    if((rec.getClearedByLine() == null && ln.getClearedByLine() != null) ||
       (rec.getClearedByLine() != null && ln.getClearedByLine() == null) ||
       (rec.getClearedByLine() != null && rec.getClearedByLine().getId() != ln.getClearedByLine().getId())){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_CL_BY");
     aud.setNewValue(String.valueOf(rec.getDocHeaderBase().getDocNumber()));
     aud.setOldValue(String.valueOf(ln.getDocHeaderBase().getDocNumber()));
     DocLineBase lnClrBy = em.find(DocLineBase.class, rec.getClearedByLine().getId(), OPTIMISTIC);
     ln.setClearedByLine(lnClrBy);
     lnChanged = true;
    }
    if(rec.isClearedLine() != ln.isClearedLine()){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_CLEARED");
     aud.setNewValue(String.valueOf(rec.isClearedLine()));
     aud.setOldValue(String.valueOf(ln.isClearedLine()));
     ln.setClearedLine(rec.isClearedLine());
     lnChanged = true;
    }
    if(rec.isClearingLine() != ln.isClearingLine()){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_CLEARING");
     aud.setNewValue(String.valueOf(rec.isClearingLine()));
     aud.setOldValue(String.valueOf(ln.isClearingLine()));
     ln.setClearingLine(rec.isClearingLine());
     lnChanged = true;
    }
    if((rec.getClearingDate() == null && ln.getClearingDate() != null) ||
       (rec.getClearingDate() != null && ln.getClearingDate() == null) ||
       (rec.getClearingDate() != null && !rec.getClearingDate().equals(ln.getClearingDate()))){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_CL_DT");
     aud.setNewValue(dateFmt.format(rec.getClearingDate()));
     aud.setOldValue(dateFmt.format(ln.getClearingDate()));
     ln.setClearingDate(ln.getClearingDate());
     lnChanged = true;
    }
    if((rec.getLineText() == null && ln.getLineText() != null) ||
       (rec.getLineText() != null && ln.getLineText() == null) ||
       (rec.getLineText() != null && !rec.getLineText().equals(ln.getLineText()))){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getLineText());
     aud.setOldValue(rec.getLineText());
     ln.setLineText(rec.getLineText());
     lnChanged = true;
    }
    if((rec.getNotes() == null && ln.getNotes() != null) ||
       (rec.getNotes() != null && ln.getNotes() == null) ||
       (rec.getNotes() != null && !rec.getNotes().equals(ln.getNotes()))){
     AuditDocLine aud = this.buildAuditDocLine(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NOTES");
     aud.setNewValue(rec.getNotes());
     aud.setOldValue(rec.getNotes());
     ln.setNotes(rec.getNotes());
     lnChanged = true;
    }
    if(lnChanged){
     ln.setChangeBy(chUsr);
     ln.setChangeDate(rec.getChangeDate());
    }
   }
    
   return ln;
  }
  
private DocLineFiTemplGl buildDocLineFiTemplGl(DocLineFiTemplGlRec templRec, 
   String pg){
 LOGGER.log(INFO, "buildDocLineFiTemplGl called with {0}", templRec);
 LOGGER.log(INFO, "templRec post type code {0}", templRec.getPostType().getPostTypeCode());
 LOGGER.log(INFO, "templRec post type id {0}", templRec.getPostType().getId());
 DocLineFiTemplGl templ;
 boolean newTempl = false;
 boolean changedTempl = false;
 
 if(templRec.getId() == null){
  templ = new DocLineFiTemplGl();
  User crUsr = em.find(User.class, templRec.getCreateBy().getId(), OPTIMISTIC);
  templ.setCreateBy(crUsr);
  templ.setCreateDate(templRec.getCreateDate());
  em.persist(templ);
  newTempl = true;
 }else{
  templ = em.find(DocLineFiTemplGl.class, templRec.getId(), OPTIMISTIC);
 }
 
 if(newTempl){
  
  
  CompanyBasic comp = this.sysBuff.getComp(templRec.getComp());
  templ.setComp(comp);
  
  if(templRec.getCostCentre() != null){
   CostCentre cc = em.find(CostCentre.class, templRec.getCostCentre().getId());
   templ.setCostCentre(cc);
  }
  FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, templRec.getGlAccount().getId());
  templ.setGlAccount(glAcnt);
  templ.setDocAmount(templRec.getHomeAmount());
  templ.setLineNum(templRec.getLineNum());
  templ.setLineText(templRec.getLineText());
  LineTypeRule lnTy = em.find(LineTypeRule.class, templRec.getLineType().getId());
  templ.setLineType(lnTy);
  templ.setNotes(templRec.getNotes());
  
  PostType pt = em.find(PostType.class, templRec.getPostType().getId());
  templ.setPostType(pt);
  
  if(templRec.getProgramme() != null){
   Programme prog = em.find(Programme.class, templ.getProgramme().getId());
   templ.setProgramme(prog);
  }
  templ.setRef1(templRec.getReference1());
  templ.setRef2(templRec.getReference2());
  if(templRec.getRestrictedFund() != null){
   Fund fnd = em.find(Fund.class, templRec.getRestrictedFund().getId());
   templ.setRestrictedFund(fnd);
  }
  templ.setSortOrder(templRec.getSortOrder());
  if(templRec.getVatCode() != null){
   VatCodeCompany vatCd = em.find(VatCodeCompany.class, templRec.getVatCode().getId());
   templ.setVatCodeComp(vatCd);
  }
 }else{
  // change made
  User chUsr = em.find(User.class, templRec.getChangeBy().getId());
  
  
  if(!Objects.equals(templRec.getComp().getId(), templ.getComp().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_COMP");
   aud.setNewValue(templRec.getComp().getReference());
   aud.setOldValue(templ.getComp().getNumber());
   CompanyBasic comp = this.sysBuff.getComp(templRec.getComp());
   templ.setComp(comp);
   
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getCostCentre().getId(), templ.getCostCentre().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_COMP");
   aud.setNewValue(templRec.getCostCentre().getRefrence());
   aud.setOldValue(templ.getCostCentre().getRefrence());
   CostCentre cc = em.find(CostCentre.class, templRec.getCostCentre().getId(), OPTIMISTIC);
   templ.setCostCentre(cc);
   changedTempl = true;
  }
  if(!Objects.equals(templRec.getGlAccount().getId(), templ.getGlAccount().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_CC");
   aud.setNewValue(templRec.getGlAccount().getCoaAccount().getRef());
   aud.setOldValue(templ.getGlAccount().getCoaAccount().getRef());
   FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, templRec.getGlAccount().getId(), OPTIMISTIC);
   templ.setGlAccount(glAcnt);
   changedTempl = true;
  }
  
  if(templRec.getHomeAmount() != templ.getDocAmount()){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_AMNT_HM");
   aud.setNewValue(String.valueOf(templRec.getHomeAmount()));
   aud.setOldValue(String.valueOf(templRec.getHomeAmount()));
   templ.setDocAmount(templRec.getHomeAmount());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getLineNum(), templ.getLineNum())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_NUM");
   aud.setNewValue(templRec.getLineNum().toString());
   aud.setOldValue(templ.getLineNum().toString());
   templ.setLineNum(templRec.getLineNum());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getLineText(), templ.getLineText())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_LINE_TXT");
   aud.setNewValue(templRec.getLineText());
   aud.setOldValue(templ.getLineText());
   templ.setLineText(templRec.getLineText());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getLineType().getId(), templ.getLineType().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_TY");
   aud.setNewValue(templRec.getLineType().getLineCode());
   aud.setOldValue(templ.getLineType().getLineCode());
   LineTypeRule lty = em.find(LineTypeRule.class, templRec.getLineType().getId());
   templ.setLineType(lty);
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getNotes(), templ.getNotes())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_NOTES");
   aud.setNewValue(templRec.getNotes());
   aud.setOldValue(templ.getNotes());
   templ.setNotes(templRec.getNotes());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getPostType().getId(), templ.getPostType().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_PT");
   aud.setNewValue(templRec.getPostType().getPostTypeCode());
   aud.setOldValue(templ.getPostType().getPostTypeCode());
   PostType pt = em.find(PostType.class, templRec.getPostType().getId());
   templ.setPostType(pt);
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getProgramme().getId(), templ.getProgramme().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_PT");
   aud.setNewValue(templRec.getProgramme().getName());
   aud.setOldValue(templ.getProgramme().getName());
   Programme prog = em.find(Programme.class, templRec.getProgramme().getId());
   templ.setProgramme(prog);
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getReference1(), templ.getRef1())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_REF1");
   aud.setNewValue(templRec.getReference1());
   aud.setOldValue(templ.getRef1());
   templ.setRef1(templRec.getReference1());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getReference2(), templ.getRef2())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_REF2");
   aud.setNewValue(templRec.getReference2());
   aud.setOldValue(templ.getRef2());
   templ.setRef2(templRec.getReference2());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getRestrictedFund().getId(), templ.getRestrictedFund().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_FND");
   aud.setNewValue(templRec.getRestrictedFund().getName());
   aud.setOldValue(templ.getRestrictedFund().getName());
   Fund fnd = em.find(Fund.class, templRec.getRestrictedFund().getId());
   templ.setRestrictedFund(fnd);
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getSortOrder(), templ.getSortOrder())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_SORT");
   aud.setNewValue(templRec.getSortOrder());
   aud.setOldValue(templ.getSortOrder());
   templ.setSortOrder(templRec.getSortOrder());
   changedTempl = true;
  }
  
  if(!Objects.equals(templRec.getVatCode().getId(), templ.getVatCodeComp().getId())){
   AuditDocLineTemplate aud = buildAuditDocLineTemplate(templ, chUsr, pg, 'U');
   aud.setFieldName("DOC_LN_FND");
   aud.setNewValue(templRec.getVatCode().getVatCode().getCode());
   aud.setOldValue(templ.getVatCodeComp().getVatCode().getCode());
   VatCodeCompany vat = em.find(VatCodeCompany.class, templRec.getVatCode().getId(),OPTIMISTIC);
   templ.setVatCodeComp(vat);
   changedTempl = true;
  }
  
  if(changedTempl){
   templ.setChangeBy(chUsr);
   templ.setChangeDate(templRec.getChangeDate());
  }
 }
 
 return templ;
}
private DocLineFiTemplGlRec buildDocLineFiTemplGlAddLazyRec(DocLineFiTemplGlRec lineRec){
 DocLineFiTemplGl ln = em.find(DocLineFiTemplGl.class, lineRec.getId());
 
 if(ln.getComp() != null){
  CompanyBasicRec comp = this.sysBuff.getCompanyById(ln.getComp().getId());
  lineRec.setComp(comp);
 }
 
 if(ln.getCostAccount() != null){
  CostAccountDirectRec cstAcnt = costCentDM.getCostAccountRecPvt(ln.getCostAccount());
  lineRec.setCostAcnt(cstAcnt);
 }
 
 if(ln.getCostCentre() != null){
  CostCentreRec cc = costCentDM.getCostCentreRec(ln.getCostCentre());
  lineRec.setCostCentre(cc);
 }
 
 if(ln.getProgramCostAccount() != null){
  ProgramAccountRec progAcnt = this.progDM.getProgramAccountRec(ln.getProgramCostAccount());
  lineRec.setProgAcnt(progAcnt);
 }
 
 if(ln.getProgramme() != null){
  ProgrammeRec prog = progDM.getProgrammeRec(ln.getProgramme());
  lineRec.setProgramme(prog);
 }
 
 if(ln.getRestrictedFund() != null){
  FundRec fnd = this.compDM.getRestrictedFundRec(lineRec.getComp(), ln.getRestrictedFund());
  lineRec.setRestrictedFund(fnd);
 }
 
 if(ln.getVatCodeComp() != null){
  VatCodeCompanyRec vatCd = this.vatDM.getVatCodeCompRec(ln.getVatCodeComp());
  lineRec.setVatCode(vatCd);
 }
 
 lineRec.setLazyLoaded(true);
 return lineRec;
}
private DocLineFiTemplGlRec buildDocLineFiTemplGlRec(DocLineFiTemplGl templ, boolean fullRec){
 LOGGER.log(INFO, "buildDocLineFiTemplGl called with {0} ", templ);
 DocLineFiTemplGlRec templRec = new DocLineFiTemplGlRec();
 
 // build core obj
 UserRec usr = this.usrDM.getUserRecPvt(templ.getCreateBy());
 templRec.setId(templ.getId());
 templRec.setCreateBy(usr);
 templRec.setCreateDate(templ.getCreateDate());
 if(templ.getChangeBy() != null){
  usr = usrDM.getUserRecPvt(templ.getChangeBy());
  templRec.setChangeBy(usr);
  templRec.setChangeDate(templ.getChangeDate());
 }
 
 templRec.setHomeAmount(templ.getDocAmount());
 if(templ.getGlAccount() != null){
  FiGlAccountCompRec glAcnt = this.glActDM.buildFiCompGlAccountRecPvt(templ.getGlAccount());
  templRec.setGlAccount(glAcnt);
 }
 templRec.setLineNum(templ.getLineNum());
 templRec.setLineText(templ.getLineText());
 LineTypeRuleRec lnTy = this.sysBuff.getLineTypeRule(templ.getLineType());
 templRec.setLineType(lnTy);
 PostTypeRec postTy = sysBuff.getPostTypeForCode(templ.getPostType().getPostTypeCode());
 templRec.setPostType(postTy);
 templRec.setReference1(templ.getRef1());
 templRec.setReference2(templ.getRef2());
 templRec.setSortOrder(templ.getSortOrder());
 if(fullRec){
  CompanyBasicRec comp = sysBuff.getCompanyById(templ.getComp().getId());
  templRec.setComp(comp);
 
 
 
  if(templ.getCostCentre() != null){
   CostCentreRec cc = this.costCentDM.getCostCentreRec(templ.getCostCentre());
   templRec.setCostCentre(cc);
  }
  if(templ.getProgramCostAccount() != null){
   ProgramAccountRec progCstAcnt = this.progDM.getProgramAccountRec(templ.getProgramCostAccount());
   templRec.setProgAcnt(progCstAcnt);
  }
  
  if(templ.getProgramme() != null){
   ProgrammeRec prog = this.progDM.getProgrammeRec(templ.getProgramme());
   templRec.setProgramme(prog);
  }
  
  if(templ.getRestrictedFund() != null){
   FundRec fnd = this.compDM.getRestrictedFundById(templ.getRestrictedFund().getId());
   templRec.setRestrictedFund(fnd);
  }
  
  if(templ.getVatCodeComp() != null){
   VatCodeCompanyRec vatCd = this.vatDM.getVatCodeCompRec(templ.getVatCodeComp());
   templRec.setVatCode(vatCd);
  }
 }
 
 
 
 return templRec;
}
  /**
   * Called when need to create a new line
   * @param lineRec
   * @return
   */
  private DocLineGl buildDocLineGL(DocLineGlRec lineRec, String pg){
    LOGGER.log(INFO,"buildDocLineGL  called with {0}", lineRec.getDocAmount());
    LOGGER.log(INFO, "lineRec id {0}", lineRec.getId());
    LOGGER.log(INFO, "lineRec post type {0}", lineRec.getPostType().getPostTypeCode());
    boolean newDocLn = false;
    boolean changedDocLn = false;
    DocLineGl line;
    if(lineRec.getId() == null  ){
     line = new DocLineGl();
     em.persist(line);
     
     newDocLn = true;
     }else{
     line = em.find(DocLineGl.class, lineRec.getId(), OPTIMISTIC);
    }
    LOGGER.log(INFO, "newDocLn {0}", newDocLn);
    if(newDocLn){
     //Populate super attributes
     LOGGER.log(INFO, "line post type code {0}", lineRec.getPostType().getPostTypeCode());
     LOGGER.log(INFO, "line post type id {0}", lineRec.getPostType().getId());
     LOGGER.log(INFO, "line  type 1{0}", lineRec.getLineType());
     
     LOGGER.log(INFO, "line  type 2 code {0}", lineRec.getLineType().getLineCode());
     line = (DocLineGl)buildDocLineBase(line, lineRec, pg);
     if(lineRec.getGlAccount() !=null){
      FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, lineRec.getGlAccount().getId(), OPTIMISTIC);
      line.setGlAccount(glAcnt);
     }
     line.setDocAmount(lineRec.getDocAmount());
     line.setDocAmount(lineRec.getDocAmount());
     if(lineRec.getCostAccount() != null){
      CostAccountDirect cstAc = em.find(CostAccountDirect.class, lineRec.getCostAccount().getId());
      line.setCostAccount(cstAc);
     }
     if(lineRec.getCostCentre() != null){
      CostCentre cc = em.find(CostCentre.class, lineRec.getCostCentre().getId());
      line.setCostCentre(cc);
     }
     
     if(lineRec.getProgrameAccount() != null){
      ProgramAccount prgAcnt = em.find(ProgramAccount.class, lineRec.getProgrameAccount().getId());
      line.setProgramCostAccount(prgAcnt);
     }
     
     if(lineRec.getProgramme() != null){
      Programme prg = em.find(Programme.class, lineRec.getProgramme().getId());
      line.setProgramme(prg);
     }
     
     if(lineRec.getReconcilForApLine() != null && lineRec.getReconcilForApLine().getId() != null){
      
      DocLineAp apLine = em.find(DocLineAp.class, lineRec.getReconcilForApLine().getId());
      line.setReconGlLnForApLine(apLine);
     }
    
     if(lineRec.getReconcilForArLine() != null && lineRec.getReconcilForArLine().getId() != null){
      DocLineAr arLine = em.find(DocLineAr.class, lineRec.getReconcilForArLine().getId());
      line.setReconcilForArLine(arLine);
     }
     
     if(lineRec.getRestrictedFund() != null){
      Fund fnd = em.find(Fund.class, lineRec.getRestrictedFund().getId());
      line.setRestrictedFund(fnd);
     }
     
     if(lineRec.getSalesPart() != null){
      SalesPartCompany slPt = em.find(SalesPartCompany.class, lineRec.getSalesPart().getId());
      line.setSalesPart(slPt);
     }
     
    } else{
     // changed line
     // cannot change these details
    }
    
    return line;
  }
  
  private DocLineGl buildNewGlDocLine(DocLineGlRec lineRec,DocFi docHdr, 
          long lineNum, LineTypeRule lineType){
   LOGGER.log(INFO, "buildNewGlDocLine called with lineRec {0} docHdr {1}, line num{2} lineType{3}", 
           new Object[]{lineRec,docHdr,lineNum,lineType});
     DocLineGl line = new DocLineGl();
    em.persist(line);
    line = buildGlDocLine(lineRec,line);
    
    line.setCreateBy(docHdr.getCreatedBy());
    line.setCreateDate(docHdr.getCreateOn());
    line.setDocHeaderBase(docHdr);
    
    line.setLineType(lineType);
    line.setLineNum(lineNum);
    line.setComp(docHdr.getCompany());
    return line;
  }
 
 private String buildSortText(ArAPAccountIF acnt,DocFiRec docHdr ){
  SortOrder sort = acnt.getSortOrder();
  if(sort == null){
   LOGGER.log(INFO, "Need to set default sort order");
   // default is DocDt
   Query sq = em.createNamedQuery("sortOrdBySortCd");
   sq.setParameter("sc", "docDt");
   sort = (SortOrder)sq.getSingleResult();
   acnt.setSortOrder(sort);
  }
  String acntType = acnt.getClass().getSimpleName();
  String retString = null;
  String sortCode = sort.getSortCode();
  SimpleDateFormat formater = new SimpleDateFormat();
  formater.applyPattern("yyyyMMdd");
  if(sortCode.equalsIgnoreCase("DocDt")){
   retString = formater.format(docHdr.getDocumentDate());
  }else if(sortCode.equalsIgnoreCase("entryDt")){
   Date entryDt = docHdr.getCreateOn();
   retString = formater.format(entryDt);
  }else if(sortCode.equalsIgnoreCase("postDt")){
   Date postDt = docHdr.getPostingDate();
   retString =  formater.format(postDt);
  }else if(sortCode.equalsIgnoreCase("taxDt")){
   Date taxDt = docHdr.getTaxDate();
   retString = formater.format(taxDt);
  }else if(sortCode.equalsIgnoreCase("extDocRef")){
   retString = docHdr.getDocInvoiceAr().getInvoiceNumber();
  }else if(sortCode.equalsIgnoreCase("ArAcNum")){
   if(acntType.equals("ArAccount")){
    retString = ((ArAccount)acnt).getAccountCode();
   }else{
    retString = ((ApAccount)acnt).getAccountCode();
   }
  }else if(sortCode.equalsIgnoreCase("AcAcName")){
   retString = ((ArAccount)acnt).getArAccountFor().getTradingName();
  }
  LOGGER.log(INFO, "AR line sort order is: {0}",retString);
  return retString;
 }
 private String buildSortText(ArAPAccountIF acnt,DocFi docHdr ){
  SortOrder sort = acnt.getSortOrder();
  if(sort == null){
   LOGGER.log(INFO, "Need to set default sort order");
   // default is DocDt
   Query sq = em.createNamedQuery("sortOrdBySortCd");
   sq.setParameter("sc", "docDt");
   sort = (SortOrder)sq.getSingleResult();
   acnt.setSortOrder(sort);
  }
  String acntType = acnt.getClass().getSimpleName();
  String retString = null;
  String sortCode = sort.getSortCode();
  SimpleDateFormat formater = new SimpleDateFormat();
  formater.applyPattern("yyyyMMdd");
  if(sortCode.equalsIgnoreCase("DocDt")){
   retString = formater.format(docHdr.getDocumentDate());
  }else if(sortCode.equalsIgnoreCase("entryDt")){
   Date entryDt = docHdr.getCreateOn();
   retString = formater.format(entryDt);
  }else if(sortCode.equalsIgnoreCase("postDt")){
   Date postDt = docHdr.getPostingDate();
   retString =  formater.format(postDt);
  }else if(sortCode.equalsIgnoreCase("taxDt")){
   Date taxDt = docHdr.getTaxDate();
   retString = formater.format(taxDt);
  }else if(sortCode.equalsIgnoreCase("extDocRef")){
   retString = docHdr.getDocInvoiceAr().getInvoiceNumber();
  }else if(sortCode.equalsIgnoreCase("ArAcNum")){
   if(acntType.equals("ArAccount")){
    retString = ((ArAccount)acnt).getAccountCode();
   }else{
    retString = ((ApAccount)acnt).getAccountCode();
   }
  }else if(sortCode.equalsIgnoreCase("AcAcName")){
   retString = ((ArAccount)acnt).getArAccountFor().getTradingName();
  }
  LOGGER.log(INFO, "AR line sort order is: {0}",retString);
 return retString;
 }
  
  /**
   * create a new AR line with the same details as the line passed in
   * @param ln
   * @param usr
   * @param dt
   * @return 
   */
  private DocLineAr newDocLineAr(DocLineAr ln, User usr, Date dt){
   LOGGER.log(INFO, "newDocLineAr called with doc {0} usr {1} date {2}", new Object[]{ln,usr,dt});
   
   DocLineAr newLn = new DocLineAr();
   newLn.setCreateBy(usr);
   newLn.setCreateDate(dt);
   newLn.setArAccount(ln.getArAccount());
   newLn.setAutoGenerated(true);
   newLn.setClearedByLine(ln.getClearedByLine());
   newLn.setClearedLine(ln.isClearedLine());
   newLn.setClearingDate(ln.getClearingDate());
   newLn.setClearingLineForLines(ln.getClearingLineForLines());
   newLn.setComp(ln.getComp());
   newLn.setCurrID(ln.getCurrID());
   newLn.setDocAmount(ln.getDocAmount());
   newLn.setDocBase(ln.getDocBase());
   newLn.setDocFi(ln.getDocFi());
   newLn.setDocHeaderBase(ln.getDocHeaderBase());
   newLn.setDueDate(ln.getDueDate());
   newLn.setGiftAid(ln.isGiftAid());
   newLn.setHomeAmount(ln.getHomeAmount());
   newLn.setLineText(ln.getLineText());
   newLn.setLineType(ln.getLineType());
   newLn.setNotes(ln.getNotes());
   newLn.setOrginalAmount(ln.getOrginalAmount());
   newLn.setPaidAmount(ln.getPaidAmount());
   newLn.setPartPaymentLine(ln.getPartPaymentLine());
   newLn.setPayTerms(ln.getPayTerms());
   
   newLn.setPayType(ln.getPayType());
   newLn.setPaymntBank(ln.getPaymntBank());
   newLn.setPostType(ln.getPostType());
   newLn.setReference1(ln.getReference1());
   newLn.setReference2(ln.getReference2());
   newLn.setSortOrder(ln.getSortOrder());
   em.persist(newLn);
   
   return newLn;
  }
  
  private DocLineGl newDocLineGL(DocLineGl ln,  User usr, Date dt){
   
   DocLineGl glLn = new DocLineGl();
   glLn.setAutoGenerated(true);
   glLn.setCreateBy(usr);
   glLn.setCreateDate(dt);
   glLn.setClearedByLine(ln.getClearedByLine());
   glLn.setClearedLine(ln.isClearedLine());
   glLn.setClearingDate(ln.getClearingDate());
   glLn.setClearingLine(ln.isClearingLine());
   glLn.setClearingLineForLines(ln.getClearingLineForLines());
   glLn.setComp(ln.getComp());
   glLn.setCostAccount(ln.getCostAccount());
   glLn.setCostCentre(ln.getCostCentre());
   glLn.setCurrID(ln.getCurrID());
   glLn.setDocAmount(ln.getDocAmount());
   glLn.setDocBase(ln.getDocBase());
   glLn.setDocFi(ln.getDocFi());
   glLn.setDocHeaderBase(ln.getDocHeaderBase());
   glLn.setGlAccount(ln.getGlAccount());
   glLn.setHomeAmount(ln.getHomeAmount());
   glLn.setLineText(ln.getLineText());
   glLn.setLineType(ln.getLineType());
   glLn.setNotes(ln.getNotes());
   glLn.setOrginalAmount(ln.getOrginalAmount());
   glLn.setPaidAmount(ln.getPaidAmount());
   glLn.setPartPaymentLine(ln.getPartPaymentLine());
   glLn.setPostType(ln.getPostType());
   glLn.setProgramCostAccount(ln.getProgramCostAccount());
   glLn.setProgramme(ln.getProgramme());
   glLn.setReconcilForArLine(ln.getReconcilForArLine());
   glLn.setReference1(ln.getReference1());
   glLn.setReference2(ln.getReference2());
   glLn.setRestrictedFund(ln.getRestrictedFund());
   glLn.setSalesPartCompany(ln.getSalesPart());
   glLn.setSortOrder(ln.getSortOrder());
   glLn.setVatCode(ln.getVatCode());
   em.persist(glLn);
   return glLn;
  }
  /**
   * allocates a fully paid AR line
   * @param receiptLn
   * @param paidLn 
   */
  
  // need to return summary of values by restricted fund
  private void allocPaidArLnFull(DocLineAr receiptLn, DocLineArRec paidLn, Date clDate, User usr){
   LOGGER.log(INFO, "allocPaidArLnFull called with receipt ln{0} paid {1}", new Object[]{receiptLn,paidLn});
   DocLineAr pd = em.find(DocLineAr.class, paidLn.getId(), OPTIMISTIC);
   pd.setClearedByLine(receiptLn);
   pd.setPaidAmount(paidLn.getPaidAmount());
   pd.setOrginalAmount(paidLn.getDocAmount());
   if(pd.getPaidAmount() == pd.getDocAmount()){
    pd.setClearedLine(true);
    pd.setClearingDate(clDate);
   }
   // allocate the reconciliation ac lines
   
   
   
  }
  private DocFi allocateArCredit(DocFiRec crn, DocFi doc, DocLineAr creditLine, 
          List<DocLineArRec> allocatedLines, UserRec user, String source)  throws BacException{
   LOGGER.log(INFO, "allocateArCredit called with cen {0} fidoc {1} allocated {2}", new Object[]{
    crn, doc,allocatedLines });
   LOGGER.log(INFO, "Credit amount {0}",creditLine.getDocAmount());
   
   creditLine.setClearingLine(true);
   creditLine.setClearingDate(new Date());
   // get the AR posting line
   
   double creditAmount = creditLine.getDocAmount();
   double allocatedAmount = 0;
   double unallocated = creditAmount;
   ListIterator<DocLineArRec> alloctedLinesLi = allocatedLines.listIterator();
   while(alloctedLinesLi.hasNext() && allocatedAmount == 0){
    DocLineArRec allocatedRec = alloctedLinesLi.next();
    LOGGER.log(INFO,"process allocated line id {0}",allocatedRec.getId());
    DocLineAr allocLine = em.find(DocLineAr.class, allocatedRec.getId(), OPTIMISTIC);
    if(allocLine == null){
     throw new BacException("allocated Ar line not found in DB");
    }
    double lineAmount = allocLine.getDocAmount();
    if(!allocLine.getPostType().isDebit()){
     // other credit allocated 
     LOGGER.log(INFO, "Credit line allocated Ar line id {0}", allocLine.getId());
     allocLine.setClearedByLine(creditLine);
     allocLine.setClearedLine(true);
     allocLine.setPaidAmount(lineAmount);
     creditLine.addClearedLine(allocLine);
     unallocated = unallocated + lineAmount;
    }
    else{
     // debit allocated
     LOGGER.log(INFO, "allocated Debit Ar line id {0}", allocLine.getId());
     LOGGER.log(INFO, "line amount {0} remaining unallocated {1}", new Object[]{lineAmount,unallocated});
     if(lineAmount <= unallocated){
      // debit fully line is fully paid
      LOGGER.log(INFO, "Debit fully allocated");
      allocLine.setClearedByLine(creditLine);
      allocLine.setClearedLine(true);
      allocLine.setPaidAmount(lineAmount);
      creditLine.addClearedLine(allocLine);
      unallocated = unallocated - lineAmount;
     }else if(lineAmount > unallocated){
      // debit is more than the available credit
      LOGGER.log(INFO, "Debit partly allocated");
      double remainingAmnt= lineAmount - unallocated;
      allocLine.setClearedByLine(creditLine);
      allocLine.setClearedLine(true);
      allocLine.setPaidAmount(lineAmount);
      creditLine.addClearedLine(allocLine);
      unallocated = 0;
      //add new line for unpaid amount
      DocLineAr unpaidLine = new DocLineAr();
      User crUsr = em.find(User.class, user.getId(), OPTIMISTIC);
      unpaidLine.setCreateBy(crUsr);
      unpaidLine.setCreateDate(new Date());
      
      em.persist(unpaidLine);
      long lines = doc.getDocLines().size();
      lines++;
      unpaidLine.setLineNum(lines);
      unpaidLine = buildAllocationAdjArLine(unpaidLine,allocLine,remainingAmnt);
      doc.getDocLines().add(unpaidLine);
     }
     
     if(unallocated > 0){
      // there is credit line remaining
      DocLineAr unpaidLine = new DocLineAr();
      User crUsr = em.find(User.class, user.getId(), OPTIMISTIC);
      unpaidLine.setCreateBy(crUsr);
      unpaidLine.setCreateDate(new Date());
      em.persist(unpaidLine);
      long lines = doc.getDocLines().size();
      lines++;
      unpaidLine.setLineNum(lines);
      unpaidLine = buildAllocationAdjArLine(unpaidLine,allocLine,unallocated);
      doc.getDocLines().add(unpaidLine);
     } else{
      LOGGER.log(INFO, "unallocated is now {0}", unallocated);
      if(unallocated == 0){
       LOGGER.log(INFO, "Credit line id {0} is allocated", creditLine.getId());
       creditLine.setClearingLine(true);
       creditLine.setChangeDate(new Date());
      }
     }
    }
   }
   
   return doc;
   
  }
  
  private DocLineAr buildAllocationAdjArLine(DocLineAr newLine, DocLineAr existingLine, 
          double paid){
   LOGGER.log(INFO, "buildAllocationAdjArLine called with paid {0}",paid);
   //determine the amounts for the allocAdj line
   double orginalAmnt = existingLine.getOrginalAmount() + existingLine.getDocAmount();
   double paidAmnt = existingLine.getPaidAmount() + paid;
   double remainingAmnt = orginalAmnt - paidAmnt;
   newLine.setOrginalAmount(orginalAmnt);
   newLine.setPaidAmount(paidAmnt);
   newLine.setDocAmount(remainingAmnt);
   LOGGER.log(INFO, "Existingline orginal {0} paid {1} doc {2} ", new Object[]{
    existingLine.getOrginalAmount(), existingLine.getPaidAmount(),existingLine.getDocAmount() });
   LOGGER.log(INFO, "newline orginal {0} paid {1} doc {2} ", new Object[]{
    newLine.getOrginalAmount(), newLine.getPaidAmount(),newLine.getDocAmount() });
   newLine.setArAccount(existingLine.getArAccount());
   newLine.setArCreditPeriodBalance(existingLine.getArCreditPeriodBalance());
   newLine.setArDebitPeriodBalance(existingLine.getArDebitPeriodBalance());
   newLine.setChangeBy(existingLine.getChangeBy());
   newLine.setChangeDate(existingLine.getChangeDate());
   newLine.setComp(existingLine.getComp());
   if(newLine.getCreateBy() == null){
    newLine.setCreateBy(existingLine.getCreateBy());
    newLine.setCreateDate(existingLine.getCreateDate());
   }
   newLine.setCurrID(existingLine.getCurrID());
   //newLine.setDocAmount(allocLine.getDocAmount());
   newLine.setDocHeaderBase(existingLine.getDocHeaderBase());
   newLine.setDueDate(existingLine.getDueDate());
   newLine.setLineText(existingLine.getLineText());
   newLine.setLineType(existingLine.getLineType());
   newLine.setNotes(existingLine.getNotes());
   newLine.setPayTerms(existingLine.getPayTerms());
   newLine.setPayType(existingLine.getPayType());
   newLine.setPaymntBank(existingLine.getPaymntBank());
   newLine.setPostType(existingLine.getPostType());
   newLine.setReconiliationLines(existingLine.getReconiliationLines());
   newLine.setReference1(existingLine.getReference1());
   newLine.setReference2(existingLine.getReference2());
   newLine.setSortOrder(existingLine.getSortOrder());
   newLine.setVatReturnLines(existingLine.getVatReturnLines());
   
   return newLine;
  }
  
  private FiArPeriodBalance buildArAnnualAcBalance(ArAccount arAcnt, int yr, int per, User usr ){
   List<FiArPeriodBalance> perBalList = arAcnt.getArPeriodBalances();
   boolean found = false;
   if(perBalList != null && !perBalList.isEmpty()){
    ListIterator<FiArPeriodBalance> pbLi = perBalList.listIterator();
    while (pbLi.hasNext() && !found){
     FiArPeriodBalance pb = pbLi.next();
     if(pb.getBalYear() == yr && pb.getBalPeriod() == per ){
      found = true;
      return pb;
     }
    }
   }
   LOGGER.log(INFO, "buildArAnnualAcBalance found {0}", found);
   if(!found){
    FiArPeriodBalance pb = new FiArPeriodBalance();
    pb.setArAccount(arAcnt);
    pb.setBalPeriod(per);
    pb.setBalYear(yr);
    pb.setCreated(new Date());
    pb.setCreatedBy(usr);
    em.persist(pb);
    return pb;
    
   }
  return null;
  }
  
  
  
  private DocLineBase buildArPayRunArLine(DocFiRec doc, DocFi docDB, BnkPaymentRunRec payRun, 
          DocBankLineBaseRec bnkLn, String lineText, LineTypeRuleRec arLineType, 
          PostTypeRec postCd,Long docLinNum, UserRec usr, User crUser,String source, Date crDate){
   LOGGER.log(INFO, "buildArPayRunArLine called with doc {0} payRun {1} bnkLn {2} docLinMum {3}", 
           new Object[]{doc,payRun,bnkLn,docLinNum});
   LOGGER.log(INFO, "docLinNum {0}", docLinNum);
   DocLineArRec arLineRec = new DocLineArRec();
   LOGGER.log(INFO, "New arLineRec has id {0}", arLineRec.getId());
   
   arLineRec.setArAccount(bnkLn.getArAccount());
   arLineRec.setAutoGenerated(true);
   arLineRec.setBacsPayRunRef(payRun.getRef());
   arLineRec.setBankAc(bnkLn.getArBank());
   arLineRec.setComp(doc.getCompany());
   arLineRec.setCreateBy(usr);
   arLineRec.setCreateDate(crDate);
   arLineRec.setDocAmount(bnkLn.getAmount());
   arLineRec.setDocHeaderBase(doc);
   arLineRec.setHomeAmount(bnkLn.getAmount());
   
   arLineRec.setLineNum(docLinNum);
   LOGGER.log(INFO, "arLineRec Line num {0}", arLineRec.getLineNum());
   arLineRec.setLineText(lineText);
   arLineRec.setLineType(arLineType);
   arLineRec.setPostType(postCd);
   arLineRec.setComp(doc.getCompany());
   SortOrderRec sort = bnkLn.getArAccount().getSortOrder();
   String sortTxt = buildGlLineSortOrderTxt(docDB,sort,arLineRec);
   LOGGER.log(INFO, "sort order {1}", sortTxt);
   arLineRec.setSortOrder(sortTxt);
   arLineRec.setClearedLine(true);
   DocLineAr arLine = this.buildDocLineAr(arLineRec, docDB,source);
   arLine.setDocNumber(docDB.getDocNumber());
   List<DocLineArRec> pdLines = bnkLn.getArPymntLines();
   docDB = this.allocateArCredit(doc, docDB,arLine, pdLines, usr, source);
   // build AR reconciliation account entries
   LOGGER.log(INFO, "pdLines {0}", pdLines);
   arLine = this.addArPayRunArRecLines(arLine, pdLines, docDB.getCompany(), crUser, source);
   LOGGER.log(INFO, "arLine.reconAcs {0}", arLine.getReconiliationLines());
   
   
   LOGGER.log(INFO, "after process ");
   
   return arLine;
  }
  
  /**
   * Adds reconciliation lines based on the invoices that are paid
   */
  private DocLineAr addArPayRunArRecLines(DocLineAr arLine, List<DocLineArRec> pdLines, CompanyBasic comp, User usr, String pg ){
   LOGGER.log(INFO, "addArPayRunArRecLines called with arLn {0} pdLines {1} comp {2} usr {3}", 
           new Object[]{arLine,pdLines,comp,usr});
   FiGlAccountComp reconGlAc =  arLine.getArAccount().getReconciliationAc();
   LOGGER.log(INFO, "reconGlAc.id {0}", reconGlAc.getId());
   //build list of 
   List<DocLineGl> pdReconLns = new ArrayList<>();
   List<RestrictedFundAmount> restrictedFundAmounts = null;
   boolean arLineDebit = arLine.getPostType().isDebit();
   for(DocLineArRec pdLn: pdLines){
    LOGGER.log(INFO, "paid line id {0}", pdLn.getId());
    double pdAmount = pdLn.getDocAmount();
    double restrictedAmount = 0;
    
    List<DocLineGl> reconGlLines = getArReconLinesForArLine(pdLn);
    if(reconGlLines != null){
    for(DocLineGl glLn : reconGlLines){
     LOGGER.log(INFO, "Recon gl line id {0}", glLn.getId());
     if(glLn.getRestrictedFund() != null){
      
      // restricted fund
      double glLnAmount = glLn.getDocAmount();
      if(restrictedFundAmounts == null){
       restrictedFundAmounts = new ArrayList<>();
      }
      RestrictedFundAmount fndAmnt = new RestrictedFundAmount(glLn.getRestrictedFund().getId(),
              reconGlAc,glLnAmount );
      restrictedFundAmounts.add(fndAmnt);
      restrictedAmount = restrictedAmount + glLnAmount;
     }
    }
    if(restrictedAmount > 0){
     // there exist restricted amounts to add
     for(RestrictedFundAmount fndLn: restrictedFundAmounts){
      Fund fnd = em.find(Fund.class, fndLn.getFund(), OPTIMISTIC);
      DocLineGl reconGlLn = buildDocLineGlRecon(arLine, reconGlAc, pdAmount, fnd);
      pdReconLns.add(reconGlLn);
     }
    }
    if(pdAmount > restrictedAmount){
     // add unrestricted recon amount
     DocLineGl reconGlLn = buildDocLineGlRecon(arLine, reconGlAc, pdAmount - restrictedAmount , null);
     pdReconLns.add(reconGlLn);
    }
    }else{
     LOGGER.log(INFO, "pdLn id {0} has no recon lines", pdLn.getId());
    }
   }
   for(DocLineGl glRecLn:pdReconLns){
    glRecLn.setReconcilForArLine(arLine);
    LOGGER.log(INFO, "gl line id {0} is reconciliation for ar lin {1}", 
            new Object[]{glRecLn.getId(),glRecLn.getReconcilForArLine().getId()});
   }
   arLine.setReconiliationLines(pdReconLns);
   return arLine;
  }
  
  /**
   * Build GL line for uncleared bank GL a/c entry
   * @param doc
   * @param docDB
   * @param payRun
   * @param bnkLn
   * @param lineText
   * @param loc
   * @param arLineType
   * @param postCd
   * @param docLinNum
   * @param usr
   * @param crUser
   * @param source
   * @param crDate
   * @return 
   */
  private DocLineBase buildArPayRunGlLine(DocFiRec doc, DocFi docDB, BnkPaymentRunRec payRun, 
          DocBankLineBacsRec bnkLn, String lineText, LineTypeRuleRec lineType,  
          PostTypeRec postCd,Long docLinNum, UserRec usr, User crUser,String source, Date crDate){
   LOGGER.log(INFO, "buildArPayRunArLine called with doc {0} payRun {1} bnkLn {2} docLinMum {3}", 
           new Object[]{doc,payRun,bnkLn,docLinNum});
   DocLineGlRec glLineRec = new DocLineGlRec();
   FiGlAccountCompRec glAcnt = bnkLn.getGlAccountComp();
   LOGGER.log(INFO, "glAcnt {0}", glAcnt);
   glLineRec.setAccountRef(glAcnt.getCoaAccount().getRef());
   glLineRec.setAutoGenerated(true);
   glLineRec.setComp(doc.getCompany());
   glLineRec.setDocAmount(bnkLn.getAmount());
   glLineRec.setCreateBy(usr);
   glLineRec.setCreateDate(crDate);
   glLineRec.setDocHeaderBase(doc);
   glLineRec.setGlAccount(glAcnt);
   glLineRec.setHomeAmount(bnkLn.getAmount());
   glLineRec.setLineNum(docLinNum);
   glLineRec.setLineText(lineText);
   glLineRec.setLineType(lineType);
   glLineRec.setPostType(postCd);
   DocLineGl glLine = this.buildDocLineGL(glLineRec, source);
   glLine.setDocHeaderBase(docDB);
   
   LOGGER.log(INFO, "glLineDB to return with id: {0}", glLine.getId());
   return glLine;
   
  }
  private String buildGlLineSortOrderTxt(DocFi docHdr, SortOrder s, DocLineAr arLine ){
   LOGGER.log(INFO, "buildGlLineSortOrderTxt called with doc {0} sort {1} arLine {3}", 
           new Object[]{docHdr,s,arLine});
   String ret = new String();
    
   String sortCode = s.getSortCode();
   SimpleDateFormat formater = new SimpleDateFormat();
     formater.applyPattern("yyyyMMdd");
    if(sortCode.equalsIgnoreCase("DocDt")){
     ret = formater.format(docHdr.getDocumentDate());
    }else if(sortCode.equalsIgnoreCase("entryDt")){
     Date entryDt = docHdr.getCreateOn();
     ret = formater.format(entryDt);
    }
    else if(sortCode.equalsIgnoreCase("postDt")){
     Date postDt = docHdr.getPostingDate();
     ret = formater.format(postDt);
    }else if(sortCode.equalsIgnoreCase("taxDt")){
     Date taxDt = docHdr.getTaxDate();
     ret = formater.format(taxDt);
    }else if(sortCode.equalsIgnoreCase("extDocRef")){
     ret = docHdr.getPartnerRef();
    }else if(sortCode.equalsIgnoreCase("ArAcNum")){
     ret = arLine.getArAccount().getAccountCode();
    }else if(sortCode.equalsIgnoreCase("AcAcName")){
     PartnerBase ptnrB = arLine.getArAccount().getArAccountFor();
     if(ptnrB.getClass().getSimpleName().equalsIgnoreCase("PartnerCorporate")){
      PartnerCorporate ptnrC = (PartnerCorporate)arLine.getArAccount().getArAccountFor();
      ret =ptnrC.getTradingName();
     }else{
      PartnerPerson ptnrP = (PartnerPerson)arLine.getArAccount().getArAccountFor();
      ret = ptnrP.getFamilyName() + " "+ptnrP.getFirstName();
     }
     
    }
    LOGGER.log(INFO, "GL Sort order returned {0}", ret);
   return ret;
  
  }
  
  
  private String buildGlLineSortOrderTxt(DocFi docHdr, SortOrderRec s, DocLineArRec arLine ){
   String ret = new String();
   
    LOGGER.log(INFO, "GL ac sort order {0}", s);
    
    String sortCode = s.getSortCode();
    SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
    if(sortCode.equalsIgnoreCase("DocDt")){
     ret = formater.format(docHdr.getDocumentDate());
    }else if(sortCode.equalsIgnoreCase("entryDt")){
     Date entryDt = docHdr.getCreateOn();
     ret = formater.format(entryDt);
    }
    else if(sortCode.equalsIgnoreCase("postDt")){
     Date postDt = docHdr.getPostingDate();
     ret = formater.format(postDt);
    }else if(sortCode.equalsIgnoreCase("taxDt")){
     Date taxDt = docHdr.getTaxDate();
     ret = formater.format(taxDt);
    }else if(sortCode.equalsIgnoreCase("extDocRef")){
     ret = docHdr.getPartnerRef();
    }else if(sortCode.equalsIgnoreCase("ArAcNum")){
     ret = arLine.getArAccount().getArAccountCode();
    }else if(sortCode.equalsIgnoreCase("ApAcName")){
     ret = arLine.getArAccount().getArAccountFor().getName();
    }
    LOGGER.log(INFO, "GL Sort order returned {0}", ret);
   return ret;
  
  }
  private String buildGlLineSortOrderTxt(DocFiRec docHdr, SortOrder s, DocLineArRec arLine ){
   String ret = new String();
   
    LOGGER.log(INFO, "GL ac sort order {0}", s);
    LOGGER.log(INFO, "docHdr create date {0}", docHdr.getCreateOn());
    String sortCode = s.getSortCode();
    SimpleDateFormat formater = new SimpleDateFormat();
     formater.applyPattern("yyyyMMdd");
    if(sortCode.equalsIgnoreCase("DocDt")){
     ret = formater.format(docHdr.getDocumentDate());
    }else if(sortCode.equalsIgnoreCase("entryDt")){
     Date entryDt = docHdr.getCreateOn();
     ret = formater.format(entryDt);
    }
    else if(sortCode.equalsIgnoreCase("postDt")){
     Date postDt = docHdr.getPostingDate();
     ret = formater.format(postDt);
    }else if(sortCode.equalsIgnoreCase("taxDt")){
     Date taxDt = docHdr.getTaxDate();
     ret = formater.format(taxDt);
    }else if(sortCode.equalsIgnoreCase("extDocRef")){
     ret = docHdr.getPartnerRef();
    }else if(sortCode.equalsIgnoreCase("ArAcNum")){
     ret = arLine.getArAccount().getArAccountCode();
    }else if(sortCode.equalsIgnoreCase("AcAcName")){
     ret = arLine.getArAccount().getArAccountFor().getName();
    }
    LOGGER.log(INFO, "GL Sort order returned {0}", ret);
   return ret;
  }

  /**
   * Copies existing lines to the new part paid line
   * @param rec
   * @param lnAmnt
   * @param docHdr
   * @param comp
   * @param usr
   * @param crDate
   * @return 
   */
  private DocLineAr buildDocLineArPtPay(DocLineAr rec,double lnAmnt,DocFi pdDocHdr,
          DocFi recDocHdr,long lineNum, CompanyBasic comp,  User usr, Date crDate){
   LOGGER.log(INFO, "buildDocLineArPtPay called with line {0} comp {1} user {2} date {3}", 
           new Object[]{rec.getId(),comp,usr,crDate});
   LOGGER.log(INFO, "line num {0}",lineNum);
   ArAccount ar;
   
    DocLineAr l = new DocLineAr();
    l.setCreateBy(usr);
    l.setCreateDate(crDate);
    
    l.setDocHeaderBase(recDocHdr);
    l.setComp(recDocHdr.getCompany());
    l.setLineType(rec.getLineType());
    
    
   LOGGER.log(INFO, "Persist part pd line with id {0}",l.getId());
   
   l.setLineNum(lineNum);
   LOGGER.log(INFO, "rec line num {0} db line num {1}", new Object[]{rec.getLineNum(),l.getLineNum()});
   if(rec.getArAccount() != null){
    LOGGER.log(INFO, "AR account {0} id {1}", new Object[]{rec.getArAccount(),rec.getArAccount().getId()});
    ar = em.find(ArAccount.class, rec.getArAccount().getId(), OPTIMISTIC);
    
    SortOrder sort = ar.getSortOrder();
    LOGGER.log(INFO, "AR ac sort order {0}", sort);
    if(sort == null){
     LOGGER.log(INFO, "Need to set default sort order");
     // default is DocDt
     Query sq = em.createNamedQuery("sortOrdBySortCd");
     sq.setParameter("sc", "docDt");
     sort = (SortOrder)sq.getSingleResult();
     ar.setSortOrder(sort);
    }
    l.setSortOrder(rec.getSortOrder());
    LOGGER.log(INFO, "AR line sort order is: {0}", l.getSortOrder());
    
    l.setArAccount(ar);
    
    l.setPostType(rec.getPostType());
    l.setDocAmount(lnAmnt);
   l.setOrginalAmount(rec.getDocAmount());
   l.setPaidAmount(rec.getPaidAmount());
   l.setDueDate(rec.getDueDate());
   l.setHomeAmount(lnAmnt);
   
   // set AR line text to doc header text
   l.setLineText(pdDocHdr.getDocHdrText());
   l.setNotes(rec.getNotes());
   if(rec.getPayTerms() != null){
    l.setPayTerms(rec.getPayTerms());
   }
   if(rec.getPayType() != null){
    LOGGER.log(INFO, "Pay type {0}", rec.getPayType());
    l.setPayType(rec.getPayType());
   }
   if(rec.getPaymntBank() != null){
    LOGGER.log(INFO, "Bank {0}", rec.getPaymntBank());
    l.setPaymntBank(rec.getPaymntBank());
   }
   l.setReference1(rec.getReference1());
   l.setReference2(rec.getReference2());
    
    List<FiArPeriodBalance> perBals = ar.getArPeriodBalances();
    FiArPeriodBalance perBal = null;
    if(perBals == null || perBals.isEmpty()){
     LOGGER.log(INFO, "AR Period bals is empty {0}");
     perBals = new ArrayList<>();
     perBal = new FiArPeriodBalance();
     perBal.setCreatedBy(usr);
     perBal.setCreated(crDate);
     perBal.setArAccount(ar);
     perBal.setBalPeriod(pdDocHdr.getFisPeriod());
     perBal.setBalYear(pdDocHdr.getFisYear());
     em.persist(perBal);
     perBals.add(perBal);
    }else{
     boolean foundPerBal = false;
     ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
     while(perBalsLi.hasNext() && !foundPerBal){
      FiArPeriodBalance currPerBal = perBalsLi.next();
      if(currPerBal.getBalYear() == pdDocHdr.getFisYear() && 
              currPerBal.getBalPeriod() == pdDocHdr.getFisPeriod()){
       // found period balance
       perBal = currPerBal;
       foundPerBal = true;
      }
     }
     if(!foundPerBal){
      perBal = new FiArPeriodBalance();
      perBal.setCreatedBy(usr);
      perBal.setCreated(crDate);
      perBal.setArAccount(ar);
      perBal.setBalPeriod(pdDocHdr.getFisPeriod());
      perBal.setBalYear(pdDocHdr.getFisYear());
      em.persist(perBal);
     }
     
             
    }
    double turnover = 0.0;
    double perDebit = 0.0;
    double perCredit = 0.00;
    turnover = perBal.getPeriodTurnover();
    turnover = turnover + lnAmnt;
    perBal.setPeriodTurnover(turnover);
    perDebit = perBal.getPeriodDebitAmount() + lnAmnt;
    perBal.setPeriodDebitAmount(perDebit);
    perCredit = perBal.getPeriodCreditAmount();
    perBal.setPeriodDocAmount(perDebit - perCredit);
    perBal.setCfwdDocAmount(perBal.getBfwdDocAmount() + perBal.getPeriodDocAmount());
    perBal.setCfwdLocalAmount(perBal.getCfwdDocAmount());
    l.setArDebitPeriodBalance(perBal);
    LOGGER.log(INFO, "Period Balances turnover {0} debit {1} credit {2} bal {3} cfwd {4}", new Object[]{
    perBal.getPeriodTurnover(),perBal.getPeriodDebitAmount(),perBal.getPeriodCreditAmount(),
    perBal.getPeriodDocAmount(),perBal.getCfwdDocAmount()});
    perBals = ar.getArPeriodBalances();
    ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
    boolean foundPerBal = false;
    while(perBalsLi.hasNext() && !foundPerBal){
     FiArPeriodBalance currPerBal = perBalsLi.next();
     if(currPerBal.getBalYear() == perBal.getBalYear() && 
             currPerBal.getBalPeriod() == perBal.getBalPeriod()){
      perBalsLi.set(perBal);
      foundPerBal = true;
     }
    }
    ar.setArPeriodBalances(perBals);
    
    
   }
   
   em.persist(l);
   return l;
  }

 private DocLineAp buildDocLineAp(DocLineApRec rec, String pg){
  
  DocLineAp line;
  boolean newLineAp = false;
  boolean changedLineAp = false;
  
  if(rec.getId() == null){
   line = new DocLineAp();
   User crUsr = em.find(User.class,rec.getCreateBy().getId());
   line.setCreateBy(crUsr);
   line.setCreateDate(rec.getCreateDate());
   em.persist(line);
   newLineAp = true;
  }else{
   line = em.find(DocLineAp.class, rec.getId());
  }
  
  if(newLineAp){
   // Populate new AP line
   line.setLineNum(rec.getLineNum());
   ApAccount apAcnt = em.find(ApAccount.class, rec.getApAccount().getId());
   line.setApAccount(apAcnt);
   
   CompanyBasic comp = sysBuff.getComp(rec.getComp());
   line.setComp(comp);
   line.setAutoGenerated(rec.isAutoGenerated());
   if(rec.getReconiliationLines() != null){
    LOGGER.log(INFO, "Create AP reconco;iation lines {0}", rec.getReconiliationLines());
   }
   if(rec.getBankLine() != null){
     DocBankLineBase bnkLn = em.find(DocBankLineBase.class, rec.getBankLine().getId());
     line.setBankLine(bnkLn);
   }
   if(rec.getBankPayRunLine() != null){
    DocBankLineBase bnkLn = em.find(DocBankLineBase.class, rec.getBankPayRunLine().getId());
    line.setBankPayRunLine(bnkLn);
   }
   if(rec.getBnkPaymentRun() != null){
    BnkPaymentRun  payRun = em.find(BnkPaymentRun .class, rec.getBnkPaymentRun().getId());
    line.setBnkPaymentRun(payRun);
   }
   
   if(rec.getClearedByLine() != null){
     DocLineBase clrLn = em.find(DocLineBase.class, rec.getClearedByLine().getId());
     line.setClearedByLine(clrLn);
   }
   line.setClearedLine(changedLineAp);
   line.setClearingDate(rec.getClearingDate());
   line.setClearingLine(rec.isClearingLine());
   line.setDocAmount(rec.getDocAmount());
   line.setDueDate(rec.getDueDate());
   line.setGiftAid(rec.isGiftAid());
   line.setHomeAmount(rec.getHomeAmount());
   line.setLineText(rec.getLineText());
   LineTypeRule lt = em.find(LineTypeRule.class, rec.getLineType().getId());
   line.setLineType(lt);
   line.setNotes(rec.getNotes());
   line.setOderFileName(rec.getOderFileName());
   line.setOderFileType(rec.getOderFileType());
   line.setOrderFileData(rec.getOrderFileData());
   line.setSupplierDocFileContents(rec.getSupplierDocFileContents());
   line.setSupplierDocFileName(rec.getSupplierDocFileName());
   line.setSupplierDocFileType(rec.getSupplierDocFileType());
   line.setOrderReference(rec.getOrderReference());
   LOGGER.log(INFO, "build AP line inv/crn file name {0} type {1}", 
     new Object[]{line.getSupplierDocFileName(), line.getSupplierDocFileType()});
   if(rec.getPayTerms() != null){
    PaymentTerms pterm = em.find(PaymentTerms.class, rec.getPayTerms().getId());
   line.setPayTerms(pterm);
   }
   
   PaymentType pty = em.find(PaymentType.class, rec.getPayType().getId());
   line.setPayType(pty);
   PostType postTy = sysBuff.getPostTypeForPCode(rec.getPostType().getPostTypeCode());
   line.setPostType(postTy);
   double acntBal =  apAcnt.getAccountBalance();
   if(postTy.isDebit()){
    acntBal += rec.getDocAmount();
   }else{
    acntBal -= rec.getDocAmount();
   }
   apAcnt.setAccountBalance(acntBal);
   LOGGER.log(INFO, pg, trans);
   line.setReference1(rec.getReference1());
   line.setReference2(rec.getReference2());
   line.setSortOrder(rec.getSortOrder());
   
  
   
  }else{
   // line changed
   User chUsr = em.find(User.class, rec.getChangeBy().getId());
   if(Objects.equals(rec.getClearedByLine().getId(), line.getClearedByLine().getId())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_CL_BY");
    aud.setNewValue(String.valueOf(rec.getClearedByLine().getDocHeaderBase().getDocNumber()));
    aud.setOldValue(String.valueOf(line.getClearedByLine().getDocHeaderBase().getDocNumber()));
    DocLineBase ln = em.find(DocLineBase.class, rec.getClearedByLine().getId()); 
    line.setClearedByLine(ln);
    changedLineAp = true;
   }
   if(rec.isAutoGenerated() != line.isAutoGenerated()){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_AUTO");
    aud.setNewValue(String.valueOf(rec.isAutoGenerated()));
    aud.setOldValue(String.valueOf(line.isAutoGenerated()));
    line.setAutoGenerated(rec.isAutoGenerated());
    changedLineAp = true;
   }
   
   if(rec.isClearedLine() != line.isClearedLine()){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_CLEARED");
    aud.setNewValue(String.valueOf(rec.isClearedLine()));
    aud.setOldValue(String.valueOf(line.isClearedLine()));
    line.setClearedLine(rec.isClearedLine());
    changedLineAp = true;
   }
   
   if(rec.isClearingLine() != line.isClearingLine()){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_CLEARING");
    aud.setNewValue(String.valueOf(rec.isClearingLine()));
    aud.setOldValue(String.valueOf(line.isClearingLine()));
    line.setClearingLine(rec.isClearingLine());
    changedLineAp = true;
   }
   if(rec.isGiftAid() != line.isGiftAid()){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("FOC_LN_GIFT_A");
    aud.setNewValue(String.valueOf(rec.isGiftAid()));
    aud.setOldValue(String.valueOf(line.isGiftAid()));
    line.setGiftAid(rec.isGiftAid());
    changedLineAp = true;
   }
   
   if((rec.getClearingDate() == null && line.getClearingDate() != null ) ||
      (rec.getClearingDate() != null && line.getClearingDate() == null) ||
      (rec.getClearingDate() == null && !rec.getClearingDate().equals(line.getClearingDate()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_CL_DT");
    String dtStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(rec.getClearingDate());
    aud.setNewValue(dtStr);
    dtStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(rec.getClearingDate());
    aud.setOldValue(dtStr);
    line.setClearingDate(rec.getClearingDate());
    changedLineAp = true;
   }
   
   if((rec.getDueDate() == null && line.getDueDate() != null ) ||
      (rec.getDueDate() != null && line.getDueDate() == null) ||
      (rec.getDueDate() == null && !rec.getDueDate().equals(line.getDueDate()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_DUE_DT");
    String dtStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(rec.getDueDate());
    aud.setNewValue(dtStr);
    dtStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(rec.getDueDate());
    aud.setOldValue(dtStr);
    line.setClearingDate(rec.getDueDate());
    changedLineAp = true;
   }
   
   if(!StringUtils.equals(rec.getLineText(), line.getLineText())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_LINE_TXT");
    aud.setNewValue(rec.getLineText());
    aud.setOldValue(line.getLineText());
    line.setLineText(rec.getLineText());
    changedLineAp = true;
   }
   
   if(!StringUtils.equals(rec.getNotes(), line.getNotes())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_NOTES");
    aud.setNewValue(rec.getNotes());
    aud.setOldValue(line.getNotes());
    line.setNotes(rec.getNotes());
    changedLineAp = true; 
   }
   
   if(!StringUtils.equals(rec.getOderFileName(), line.getOderFileName())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_ORD_FN");
    aud.setNewValue(rec.getOderFileName());
    aud.setOldValue(line.getOderFileName());
    line.setOderFileName(rec.getOderFileName());
    changedLineAp = true; 
   }
   
   if(!StringUtils.equals(rec.getOderFileType(), line.getOderFileType())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_ORD_FT");
    aud.setNewValue(rec.getOderFileType());
    aud.setOldValue(line.getOderFileType());
    line.setOderFileType(rec.getOderFileType());
    changedLineAp = true; 
   }
   
   if(!StringUtils.equals(rec.getOrderReference(), line.getOrderReference())){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_ORD_REF");
    aud.setNewValue(rec.getOrderReference());
    aud.setOldValue(line.getOrderReference());
    line.setOrderReference(rec.getOrderReference());
    changedLineAp = true; 
   }
   
   if( (rec.getOrderedBy() == null && line.getOrderBy() != null) ||
      (rec.getOrderedBy() != null && line.getOrderBy() == null) ||
      (rec.getOrderedBy() != null && 
     !Objects.equals(rec.getOrderedBy().getId(), line.getOrderBy().getId()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_ORD_BY");
    String name = rec.getOrderedBy().getNameStructured();
    aud.setNewValue(name);
    name = line.getOrderBy().getNameStructured();
    aud.setOldValue(name);
    PartnerPerson pp = em.find(PartnerPerson.class, rec.getOrderedBy().getId());
    line.setOrderBy(pp);
    changedLineAp = true;
   }
   
   if( (rec.getPayTerms() == null && line.getPayTerms() != null) ||
      (rec.getPayTerms() != null && line.getPayTerms() == null) ||
      (rec.getPayTerms() != null && 
     !Objects.equals(rec.getPayTerms().getId(), line.getPayTerms().getId()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_PAY_TERM");
    String name = rec.getPayTerms().getPayTermsCode();
    aud.setNewValue(name);
    name = line.getPayTerms().getPayTermsCode();
    aud.setOldValue(name);
    PaymentTerms pt = em.find(PaymentTerms.class, rec.getPayTerms().getId());
    line.setPayTerms(pt);
    changedLineAp = true;
   }
   
   if( (rec.getPayType() == null && line.getPayType() != null) ||
      (rec.getPayType() != null && line.getPayType() == null) ||
      (rec.getPayType() != null && !Objects.equals(rec.getPayType().getId(), line.getPayType().getId()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_PAY_TYPE");
    String name = rec.getPayType().getPayTypeCode();
    aud.setNewValue(name);
    name = line.getPayType().getPayTypeCode();
    aud.setOldValue(name);
    PaymentType ptyp = em.find(PaymentType.class, rec.getPayType().getId());
    line.setPayType(ptyp);
    changedLineAp = true;
   }
   
   if( (rec.getPaymntBank() == null && line.getPaymentBank() != null) ||
      (rec.getPaymntBank() != null && line.getPaymentBank() == null) ||
      (rec.getPaymntBank() != null &&
     !Objects.equals(rec.getPaymntBank().getId(), line.getPaymentBank().getId()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_BNK");
    String name = rec.getPaymntBank().getBankAccount().getAccountNumber();
    aud.setNewValue(name);
    name = line.getPaymentBank().getBankAccount().getAccountNumber();
    aud.setOldValue(name);
    ArBankAccount bnk = em.find(ArBankAccount.class, rec.getPaymntBank().getId());
    line.setPaymentBank(bnk);
    changedLineAp = true;
   }
   
   if( !StringUtils.equals(rec.getReference1(), line.getReference1())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_REF1");
    aud.setNewValue(rec.getReference1());
    aud.setOldValue(line.getReference1());
    line.setReference1(rec.getReference1());
    changedLineAp = true;
   }
   
   if( !StringUtils.equals(rec.getReference2(), line.getReference2())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_REF2");
    aud.setNewValue(rec.getReference2());
    aud.setOldValue(line.getReference2());
    line.setReference2(rec.getReference2());
    changedLineAp = true;
   }
  
   if( !StringUtils.equals(rec.getSortOrder(), line.getSortOrder())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_REF2");
    aud.setNewValue(rec.getSortOrder());
    aud.setOldValue(line.getSortOrder());
    line.setSortOrder(rec.getSortOrder());
    changedLineAp = true;
   }
   
   if( (rec.getSupplierDocFileContents() == null && line.getSupplierDocFileContents() != null) ||
      (rec.getSupplierDocFileContents() != null && line.getSupplierDocFileContents() == null) ||
      (rec.getSupplierDocFileContents() != null && 
       !Arrays.equals(rec.getSupplierDocFileContents(), line.getSupplierDocFileContents()))){
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_SUPL_INV_FC");
    line.setSupplierDocFileContents(rec.getSupplierDocFileContents());
    changedLineAp = true;
   }
   
   if( !StringUtils.equals(rec.getSupplierDocFileName(), line.getSupplierDocFileName())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_SUPL_INV_FN");
    aud.setNewValue(rec.getSupplierDocFileName());
    aud.setOldValue(line.getSupplierDocFileName());
    line.setSupplierDocFileName(rec.getSupplierDocFileName());
    changedLineAp = true;
   }
   
   if( !StringUtils.equals(rec.getSupplierDocFileName(), line.getSupplierDocFileName())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_SUPL_INV_FN");
    aud.setNewValue(rec.getSupplierDocFileName());
    aud.setOldValue(line.getSupplierDocFileName());
    line.setSupplierDocFileName(rec.getSupplierDocFileName());
    changedLineAp = true;
   }
  
   if( !StringUtils.equals(rec.getSupplierDocFileType(), line.getSupplierDocFileType())) {
    AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
    aud.setFieldName("DOC_LN_SUPL_INV_FT");
    aud.setNewValue(rec.getSupplierDocFileType());
    aud.setOldValue(line.getSupplierDocFileType());
    line.setSupplierDocFileType(rec.getSupplierDocFileType());
    changedLineAp = true;
   }
   if(changedLineAp){
    line.setChangeBy(chUsr);
    line.setChangeDate(rec.getChangeDate());
   }
  }
  return line;
 }
 
 private DocLineApRec buildDocLineApRec(DocLineAp ln){
  
  DocLineApRec rec = new DocLineApRec();
  rec.setId(ln.getId());
  UserRec crUsr = this.usrDM.getUserRecPvt(ln.getCreateBy());
  rec.setCreateBy(crUsr);
  rec.setCreateDate(rec.getCreateDate());
  ApAccountRec apAcnt = apAcntDM.getApAccountRec(ln.getApAccount());
  rec.setApAccount(apAcnt);
  CompanyBasicRec comp = sysBuff.getCompanyById(ln.getComp().getId());
  rec.setComp(comp);
  if(ln.getDocBase() == null){
   LOGGER.log(INFO, "No doc record for line id {0}", ln.getId());
  }else {
   
   DocFi doc = em.find(DocFi.class, ln.getDocBase().getId());
   rec.setDocNumber(doc.getDocNumber());
   rec.setDocTypeName(doc.getDocType().getCode());
   rec.setPtnrRef(doc.getPartnerRef());
   
  }
  rec.setLineNum(ln.getLineNum());
  rec.setAutoGenerated(ln.isAutoGenerated());
  rec.setClearingDate(ln.getClearingDate());
  rec.setClearingLine(ln.isClearingLine());
  rec.setClearedLine(ln.isClearedLine());
  rec.setDocAmount(ln.getDocAmount());
  rec.setDueDate(ln.getDueDate());
  rec.setGiftAid(ln.isGiftAid());
  rec.setHomeAmount(ln.getHomeAmount());
  rec.setLineText(ln.getLineText());
  LineTypeRuleRec lt = sysBuff.getLineTypeRule(ln.getLineType());
  rec.setLineType(lt);
   rec.setNotes(ln.getNotes());
   rec.setOderFileName(ln.getOderFileName());
   rec.setOderFileType(ln.getOderFileType());
   rec.setOrderFileData(ln.getOrderFileData());
   rec.setSupplierDocFileName(ln.getSupplierDocFileName());
   rec.setSupplierDocFileType(ln.getSupplierDocFileType());
   rec.setSupplierDocFileContents(ln.getSupplierDocFileContents());
   rec.setOrderReference(ln.getOrderReference());
   if(ln.getPayTerms() != null){
    PaymentTermsRec pterm = sysBuff.getPaymentTerms(ln.getPayTerms());
   rec.setPayTerms(pterm);
   }
   
   PaymentTypeRec pty = sysBuff.getPaymentType(ln.getPayType());
   rec.setPayType(pty);
   PostTypeRec postTy = sysBuff.getPostTypeForCode(ln.getPostType().getPostTypeCode());
   rec.setPostType(postTy);
   rec.setReference1(ln.getReference1());
   rec.setReference2(ln.getReference2());
   rec.setSortOrder(ln.getSortOrder());
   
  
  return rec;
 }
  
 private DocLineAr buildDocLineAr(DocLineArRec rec, String pg){
  LOGGER.log(INFO, "buildDocLineAr called with line {0} id {1} ", new Object[]{rec, rec.getId()});
  DocLineAr line ;
  ArAccount ar = null;
  boolean newLine = false;
  boolean changedLine = false;
  DocBaseRec hdr = rec.getDocHeaderBase();
  CompanyBasic comp = this.sysBuff.getComp(rec.getComp());
  if(rec.getId() == null || rec.getId() == 0 || rec.getId() < 0 ){
   line = new DocLineAr();
   
   User crUsr = em.find(User.class, hdr.getCreatedBy().getId());
   line.setCreateBy(crUsr);
   line.setCreateDate(hdr.getCreateOn());
   //l.setDocHeaderBase(docHdr); set doc linked to in caller
   em.persist(line);
   
   newLine = true;
  }else{
    line = em.find(DocLineAr.class, rec.getId(), OPTIMISTIC);
  }
  if(newLine){
   line.setLineNum(rec.getLineNum());
   LOGGER.log(INFO, "rec line num {0} db line num {1}", new Object[]{rec.getLineNum(),line.getLineNum()});
   if(rec.getArAccount() != null){
    LOGGER.log(INFO, "AR account {0} id {1}", new Object[]{rec.getArAccount(),rec.getArAccount().getId()});
    ar = em.find(ArAccount.class, rec.getArAccount().getId(), OPTIMISTIC);
    line.setArAccount(ar);
    PostType postingType = sysBuff.getPostTypeForPCode(rec.getPostType().getPostTypeCode());
    line.setPostType(postingType);
    LineTypeRule lineType = em.find(LineTypeRule.class, rec.getLineType().getId(), OPTIMISTIC);
    line.setClearedLine(rec.isClearedLine());
    line.setClearingLine(rec.isClearingLine());
   line.setGiftAid(rec.isGiftAid());
   line.setLineType(lineType);
   line.setDocAmount(rec.getDocAmount());
   line.setOrginalAmount(rec.getOrginalAmount());
   line.setPaidAmount(rec.getPaidAmount());
   line.setDueDate(rec.getDueDate());
   line.setHomeAmount(rec.getHomeAmount());
   // set AR line text to doc header text
   if(StringUtils.isBlank(rec.getLineText())){
    line.setLineText(hdr.getDocHdrText());
   }else{
    line.setLineText(rec.getLineText());
   }
   LOGGER.log(INFO, "rec header text {0} rec line text {1} db line text {2}", new Object[]{
    hdr.getDocHdrText(), rec.getLineText(), line.getLineText() });
   
   line.setNotes(rec.getNotes());
   if(rec.getPayTerms() != null){
    LOGGER.log(INFO, "Pay terms {0}", rec.getPayTerms());
    PaymentTerms  terms = em.find(PaymentTerms.class, rec.getPayTerms().getId(), OPTIMISTIC);
    line.setPayTerms(terms);
   }
   if(rec.getPayType() != null){
    LOGGER.log(INFO, "Pay type {0}", rec.getPayType());
    PaymentType payType = em.find(PaymentType.class, rec.getPayType().getId(), OPTIMISTIC);
    line.setPayType(payType);
   }
   if(rec.getBankAc() != null){
    LOGGER.log(INFO, "Bank {0}", rec.getBankAc());
    ArBankAccount bank = em.find(ArBankAccount.class, rec.getBankAc().getId(), OPTIMISTIC);
    line.setPaymntBank(bank);
   }
   line.setReference1(rec.getReference1());
   line.setReference2(rec.getReference2());
   if(comp != null){
    line.setComp(comp);
   }
   }
   }else{
    // changed
    User chUsr = em.find(User.class, rec.getChangeBy().getId());
    Date chDate = rec.getChangeDate();
    
    if(rec.isGiftAid() != line.isGiftAid()){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_GIFT_A");
     aud.setNewValue(String.valueOf(rec.isGiftAid()));
     aud.setOldValue(String.valueOf(line.isGiftAid()));
     line.setGiftAid(rec.isGiftAid());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getNotes(), line.getNotes())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NOTES");
     aud.setNewValue(rec.getNotes());
     aud.setOldValue(line.getNotes());
     line.setNotes(rec.getNotes());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getLineText(), line.getLineText())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getLineText());
     aud.setOldValue(line.getLineText());
     line.setLineText(rec.getLineText());
     changedLine = true;
    }
     if(!StringUtils.equals(rec.getReference1(), line.getReference1())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getReference1());
     aud.setOldValue(line.getReference1());
     line.setReference1(rec.getReference1());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getReference2(), line.getReference2())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getReference2());
     aud.setOldValue(line.getReference2());
     line.setReference2(rec.getReference2());
     changedLine = true;
    }
    if(rec.getDueDate().equals(line.getDueDate())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_DUE_DT");
     aud.setNewValue(rec.getDueDate().toString());
     aud.setOldValue(line.getDueDate().toString());
     line.setDueDate(rec.getDueDate());
     changedLine = true;
    }
    if(Objects.equals(rec.getPayTerms().getId(), line.getPayTerms().getId())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PAY_TERM");
     aud.setNewValue(rec.getPayTerms().getPayTermsCode());
     aud.setOldValue(line.getPayTerms().getPayTermsCode());
     PaymentTerms pTerm = em.find(PaymentTerms.class, rec.getPayTerms().getId());
     line.setPayTerms(pTerm);
     changedLine = true; 
    }
    if(!Objects.equals(rec.getPayType().getId(), line.getPayType().getId())){
     AuditDocLine aud = this.buildAuditDocLine(line, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PAY_TYPE");
     aud.setNewValue(rec.getPayType().getPayTypeCode());
     aud.setOldValue(line.getPayType().getPayTypeCode());
     PaymentType pType = em.find(PaymentType.class, rec.getPayType().getId());
     line.setPayType(pType);
     changedLine = true; 
    }
    
    if(changedLine){
     line.setChangeBy(chUsr);
     line.setChangeDate(chDate);
    }
   }
   
   if(ar != null){
    String sort = this.buildSortText(ar, (DocFiRec)hdr);
    
    line.setSortOrder(sort);
   }
    
    
   
   
   
   
   
    List<FiArPeriodBalance> perBals = ar.getArPeriodBalances();
    FiArPeriodBalance perBal = null;
    if(perBals == null || perBals.isEmpty()){
     LOGGER.log(INFO, "AR Period bals is empty {0}");
     perBals = new ArrayList<>();
     perBal = new FiArPeriodBalance();
     
     perBal.setArAccount(ar);
     perBal.setBalPeriod(((DocFiRec)hdr).getFisPeriod());
     perBal.setBalYear(((DocFiRec)hdr).getFisYear());
     em.persist(perBal);
     perBals.add(perBal);
    }else{
     boolean foundPerBal = false;
     ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
     while(perBalsLi.hasNext() && !foundPerBal){
      FiArPeriodBalance currPerBal = perBalsLi.next();
      if(currPerBal.getBalYear() == ((DocFiRec)hdr).getFisYear() && 
              currPerBal.getBalPeriod() == ((DocFiRec)hdr).getFisPeriod()){
       // found period balance
       perBal = currPerBal;
       foundPerBal = true;
      }
     }
     if(!foundPerBal){
      perBal = new FiArPeriodBalance();
      perBal.setArAccount(ar);
      perBal.setBalPeriod(((DocFiRec)hdr).getFisPeriod());
      perBal.setBalYear(((DocFiRec)hdr).getFisYear());
      em.persist(perBal);
     }
     
             
    }
    double turnover = 0.0;
    double perDebit = 0.0;
    double perCredit = 0.00;
    turnover = perBal.getPeriodTurnover();
    turnover = turnover + rec.getDocAmount();
    perBal.setPeriodTurnover(turnover);
    perDebit = perBal.getPeriodDebitAmount() + rec.getDocAmount() ;
    perBal.setPeriodDebitAmount(perDebit);
    perCredit = perBal.getPeriodCreditAmount();
    perBal.setPeriodDocAmount(perDebit - perCredit);
    perBal.setCfwdDocAmount(perBal.getBfwdDocAmount() + perBal.getPeriodDocAmount());
    perBal.setCfwdLocalAmount(perBal.getCfwdDocAmount());
    line.setArDebitPeriodBalance(perBal);
    LOGGER.log(INFO, "Period Balances turnover {0} debit {1} credit {2} bal {3} cfwd {4}", new Object[]{
    perBal.getPeriodTurnover(),perBal.getPeriodDebitAmount(),perBal.getPeriodCreditAmount(),
    perBal.getPeriodDocAmount(),perBal.getCfwdDocAmount()});
    perBals = ar.getArPeriodBalances();
    ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
    boolean foundPerBal = false;
    while(perBalsLi.hasNext() && !foundPerBal){
     FiArPeriodBalance currPerBal = perBalsLi.next();
     if(currPerBal.getBalYear() == perBal.getBalYear() && currPerBal.getBalPeriod() == perBal.getBalPeriod()){
      perBalsLi.set(perBal);
      foundPerBal = true;
     }
    }
    ar.setArPeriodBalances(perBals);
    
    
  
  return line;
 }
 
 private DocLineAr buildDocLineAr(DocLineArRec rec,DocFi docHdr, String pg){
  LOGGER.log(INFO, "buildDocLineAr called with line {0} ", 
           rec.getId());
  LOGGER.log(INFO, "rec createdBy {0} docHdr createdBy {1} ", 
           new Object[]{rec.getCreateBy(), docHdr.getCreatedBy()});
  DocLineAr l ;
  ArAccount ar = null;
  boolean newLine = false;
  boolean changedLine = false;
  CompanyBasic comp = this.sysBuff.getComp(rec.getComp());
   
  if(rec.getId() == null || rec.getId() == 0 || rec.getId() < 0 ){
   l = new DocLineAr();
   //User crUsr = em.find(User.class, rec.getCreateBy().getId());
   l.setCreateBy(docHdr.getCreatedBy());
   l.setCreateDate(rec.getCreateDate());
   l.setDocHeaderBase(docHdr);
   em.persist(l);
   
   newLine = true;
   }else{
    l = em.find(DocLineAr.class, rec.getId(), OPTIMISTIC);
   }
  
   
   if(newLine){
    l.setLineNum(rec.getLineNum());
    LOGGER.log(INFO, "rec line num {0} db line num {1}", new Object[]{rec.getLineNum(),l.getLineNum()});
    if(rec.getArAccount() != null){
    LOGGER.log(INFO, "AR account {0} id {1}", new Object[]{rec.getArAccount(),rec.getArAccount().getId()});
    ar = em.find(ArAccount.class, rec.getArAccount().getId(), OPTIMISTIC);
    l.setArAccount(ar);
    PostType postingType = sysBuff.getPostTypeForPCode(rec.getPostType().getPostTypeCode());
    l.setPostType(postingType);
    LineTypeRule lineType = em.find(LineTypeRule.class, rec.getLineType().getId(), OPTIMISTIC);
   l.setClearedLine(rec.isClearedLine());
   l.setClearingLine(rec.isClearingLine());
   l.setGiftAid(rec.isGiftAid());
   l.setLineType(lineType);
   l.setDocAmount(rec.getDocAmount());
   l.setOrginalAmount(rec.getOrginalAmount());
   l.setPaidAmount(rec.getPaidAmount());
   l.setDueDate(rec.getDueDate());
   l.setHomeAmount(rec.getHomeAmount());
   // set AR line text to doc header text
   if(StringUtils.isBlank(rec.getLineText())){
    l.setLineText(docHdr.getDocHdrText());
   }else{
    l.setLineText(rec.getLineText());
   }
   LOGGER.log(INFO, "Rec header text {0} line text {1} db line text {2}", new Object[]{
    docHdr.getDocHdrText(), rec.getLineText(), l.getLineText()});
   l.setNotes(rec.getNotes());
   if(rec.getPayTerms() != null){
    LOGGER.log(INFO, "Pay terms {0}", rec.getPayTerms());
    PaymentTerms  terms = em.find(PaymentTerms.class, rec.getPayTerms().getId(), OPTIMISTIC);
    l.setPayTerms(terms);
   }
   if(rec.getPayType() != null){
    LOGGER.log(INFO, "Pay type {0}", rec.getPayType());
    PaymentType payType = em.find(PaymentType.class, rec.getPayType().getId(), OPTIMISTIC);
    l.setPayType(payType);
   }
   if(rec.getBankAc() != null){
    LOGGER.log(INFO, "Bank {0}", rec.getBankAc());
    ArBankAccount bank = em.find(ArBankAccount.class, rec.getBankAc().getId(), OPTIMISTIC);
    l.setPaymntBank(bank);
   }
   l.setReference1(rec.getReference1());
   l.setReference2(rec.getReference2());
   if(comp != null){
    l.setComp(comp);
   }
   }
   }else{
    // changed
    User chUsr = em.find(User.class, rec.getChangeBy().getId());
    Date chDate = rec.getChangeDate();
    
    if(rec.isGiftAid() != l.isGiftAid()){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_GIFT_A");
     aud.setNewValue(String.valueOf(rec.isGiftAid()));
     aud.setOldValue(String.valueOf(l.isGiftAid()));
     l.setGiftAid(rec.isGiftAid());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getNotes(), l.getNotes())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NOTES");
     aud.setNewValue(rec.getNotes());
     aud.setOldValue(l.getNotes());
     l.setNotes(rec.getNotes());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getLineText(), l.getLineText())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getLineText());
     aud.setOldValue(l.getLineText());
     l.setLineText(rec.getLineText());
     changedLine = true;
    }
     if(!StringUtils.equals(rec.getReference1(), l.getReference1())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getReference1());
     aud.setOldValue(l.getReference1());
     l.setReference1(rec.getReference1());
     changedLine = true;
    }
    if(!StringUtils.equals(rec.getReference2(), l.getReference2())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getReference2());
     aud.setOldValue(l.getReference2());
     l.setReference2(rec.getReference2());
     changedLine = true;
    }
    if(rec.getDueDate().equals(l.getDueDate())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_DUE_DT");
     aud.setNewValue(rec.getDueDate().toString());
     aud.setOldValue(l.getDueDate().toString());
     l.setDueDate(rec.getDueDate());
     changedLine = true;
    }
    if(Objects.equals(rec.getPayTerms().getId(), l.getPayTerms().getId())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PAY_TERM");
     aud.setNewValue(rec.getPayTerms().getPayTermsCode());
     aud.setOldValue(l.getPayTerms().getPayTermsCode());
     PaymentTerms pTerm = em.find(PaymentTerms.class, rec.getPayTerms().getId());
     l.setPayTerms(pTerm);
     changedLine = true; 
    }
    if(!Objects.equals(rec.getPayType().getId(), l.getPayType().getId())){
     AuditDocLine aud = this.buildAuditDocLine(l, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PAY_TYPE");
     aud.setNewValue(rec.getPayType().getPayTypeCode());
     aud.setOldValue(l.getPayType().getPayTypeCode());
     PaymentType pType = em.find(PaymentType.class, rec.getPayType().getId());
     l.setPayType(pType);
     changedLine = true; 
    }
    
    if(changedLine){
     l.setChangeBy(chUsr);
     l.setChangeDate(chDate);
    }
   }
   
   if(ar != null){
    String sort = this.buildSortText(ar, docHdr);
    l.setSortOrder(sort);
   }
    
    
   
   
   
   
   
    List<FiArPeriodBalance> perBals = ar.getArPeriodBalances();
    FiArPeriodBalance perBal = null;
    if(perBals == null || perBals.isEmpty()){
     LOGGER.log(INFO, "AR Period bals is empty {0}");
     perBals = new ArrayList<>();
     perBal = new FiArPeriodBalance();
     
     perBal.setArAccount(ar);
     perBal.setBalPeriod(docHdr.getFisPeriod());
     perBal.setBalYear(docHdr.getFisYear());
     em.persist(perBal);
     perBals.add(perBal);
    }else{
     boolean foundPerBal = false;
     ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
     while(perBalsLi.hasNext() && !foundPerBal){
      FiArPeriodBalance currPerBal = perBalsLi.next();
      if(currPerBal.getBalYear() == docHdr.getFisYear() && 
              currPerBal.getBalPeriod() == docHdr.getFisPeriod()){
       // found period balance
       perBal = currPerBal;
       foundPerBal = true;
      }
     }
     if(!foundPerBal){
      perBal = new FiArPeriodBalance();
      perBal.setArAccount(ar);
      perBal.setBalPeriod(docHdr.getFisPeriod());
      perBal.setBalYear(docHdr.getFisYear());
      em.persist(perBal);
     }
     
             
    }
    double turnover = 0.0;
    double perDebit = 0.0;
    double perCredit = 0.00;
    turnover = perBal.getPeriodTurnover();
    turnover = turnover + rec.getDocAmount();
    perBal.setPeriodTurnover(turnover);
    perDebit = perBal.getPeriodDebitAmount() + rec.getDocAmount() ;
    perBal.setPeriodDebitAmount(perDebit);
    perCredit = perBal.getPeriodCreditAmount();
    perBal.setPeriodDocAmount(perDebit - perCredit);
    perBal.setCfwdDocAmount(perBal.getBfwdDocAmount() + perBal.getPeriodDocAmount());
    perBal.setCfwdLocalAmount(perBal.getCfwdDocAmount());
    l.setArDebitPeriodBalance(perBal);
    LOGGER.log(INFO, "Period Balances turnover {0} debit {1} credit {2} bal {3} cfwd {4}", new Object[]{
    perBal.getPeriodTurnover(),perBal.getPeriodDebitAmount(),perBal.getPeriodCreditAmount(),
    perBal.getPeriodDocAmount(),perBal.getCfwdDocAmount()});
    perBals = ar.getArPeriodBalances();
    ListIterator<FiArPeriodBalance> perBalsLi = perBals.listIterator();
    boolean foundPerBal = false;
    while(perBalsLi.hasNext() && !foundPerBal){
     FiArPeriodBalance currPerBal = perBalsLi.next();
     if(currPerBal.getBalYear() == perBal.getBalYear() && currPerBal.getBalPeriod() == perBal.getBalPeriod()){
      perBalsLi.set(perBal);
      foundPerBal = true;
     }
    }
    ar.setArPeriodBalances(perBals);
    
    
   
   
   
   return l;
  }
  
  private DocLineBaseRec buildDocLineBaseRec(DocLineBase rec,  DocFiRec docRec){
   LOGGER.log(INFO, "buildDocLineArRec called with line {0} ",rec.getId());
   DocLineBaseRec l = new DocLineBaseRec(); 
   l.setId(rec.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(rec.getCreateBy());
   l.setCreateBy(crUsr);
   l.setCreateDate(rec.getCreateDate());
   if(rec.getChangeBy() != null){
    UserRec chUsr = usrDM.getUserRecPvt(rec.getChangeBy());
    l.setChangeBy(chUsr);
    l.setChangeDate(rec.getChangeDate());
   }
   l.setLineNum(rec.getLineNum());
   if(rec.getPostType() != null){
    PostTypeRec ptype =  this.sysBuff.getPostTypeForCode(rec.getPostType().getPostTypeCode());
    l.setPostType(ptype);
   }
   l.setOrginalAmount(rec.getOrginalAmount());
   l.setPaidAmount(rec.getPaidAmount());
   l.setHomeAmount(rec.getHomeAmount());
   // set AR line text to doc header text
   l.setLineText(rec.getLineText());
   l.setReference1(rec.getReference1());
   l.setReference2(rec.getReference2());
   if(rec.getDocBase() == null){
    DocBaseRec docbaseRec = this.buildDocBaseRec(rec.getDocBase());
    l.setDocHeaderBase(docbaseRec);
   }
   l.setDocHeaderBase(docRec);
   
   return l;
  }
  private DocLineArRec buildDocLineArRecMin(DocLineAr ln){
   LOGGER.log(INFO, "Called buildDocLineArRecMin with ln id {0}", ln.getId());
   DocLineArRec rec  = new DocLineArRec();
   rec.setId(ln.getId());
   rec.setAccountRef(ln.getArAccount().getAccountCode());
   if(ln.getArAccount()!= null){
    ArAccountRec acnt = this.arAccountDM.getArAccountRecPvt(ln.getArAccount());
    rec.setArAccount(acnt);
   }
   rec.setAutoGenerated(ln.isAutoGenerated());
   if(ln.getChangeBy() != null){
    UserRec chUsr = this.usrDM.getUserRecPvt(ln.getChangeBy());
    rec.setChangeBy(chUsr);
    rec.setChangeDate(ln.getChangeDate());
   }
   rec.setClearedLine(ln.isClearedLine());
   rec.setClearingDate(ln.getClearingDate());
   rec.setClearingLine(ln.isClearingLine());
   rec.setComp(this.sysBuff.getCompanyById(ln.getComp().getId()));
   if(ln.getCreateBy() != null){
    UserRec crUsr = this.usrDM.getUserRecPvt(ln.getCreateBy());
    rec.setCreateBy(crUsr);
    rec.setCreateDate(ln.getCreateDate());
   }
   rec.setDocAmount(ln.getDocAmount());
   rec.setDueDate(ln.getDueDate());
   rec.setGiftAid(ln.isGiftAid());
   rec.setHomeAmount(ln.getHomeAmount());
   rec.setLineNum(ln.getLineNum());
   rec.setLineText(ln.getLineText());
   rec.setNotes(ln.getNotes());
   rec.setOrginalAmount(ln.getOrginalAmount());
   rec.setPaidAmount(ln.getPaidAmount());
   if(ln.getPayTerms() != null){
    rec.setPayTerms(sysBuff.getPaymentTerms(ln.getPayTerms()));
   }
   if(ln.getPayType() != null){
    rec.setPayType(sysBuff.getPaymentType(ln.getPayType()));
   }
   if(ln.getPostType() != null){
    rec.setPostType(sysBuff.getPostTypeForCode(ln.getPostType().getPostTypeCode()));
   }
   rec.setReference1(ln.getReference1());
   rec.setReference2(ln.getReference2());
   rec.setSortOrder(ln.getSortOrder());
   return rec;
  }
  
  private DocLineArRec buildDocLineArRec(DocLineAr rec, UserRec usr, String pg){
   LOGGER.log(INFO, "buildDocLineArRec called with line {0} ",rec.getId());
   DocLineArRec l  = new DocLineArRec();
   ArAccountRec arAc = null;
   l.setId(rec.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(rec.getCreateBy());
   l.setCreateBy(crUsr);
   l.setCreateDate(rec.getCreateDate());
   if(rec.getChangeBy() != null){
    UserRec chUsr = usrDM.getUserRecPvt(rec.getChangeBy());
    l.setChangeBy(chUsr);
    l.setChangeDate(rec.getChangeDate());
   }
   
   l.setLineNum(rec.getLineNum());
   if(rec.getArAccount() != null){
    LOGGER.log(INFO, "AR account {0} id {1}", new Object[]{rec.getArAccount(),rec.getArAccount().getId()});
    arAc = this.arAccountDM.buildArAccountRecPvt(rec.getArAccount(),usr, pg);
    l.setArAccount(arAc);
    
   }
   if(rec.getPostType() != null){
    PostTypeRec ptype =  this.sysBuff.getPostTypeForCode(rec.getPostType().getPostTypeCode());
    l.setPostType(ptype);
   }
   l.setDocAmount(rec.getDocAmount());
   l.setOrginalAmount(rec.getOrginalAmount());
   l.setPaidAmount(rec.getPaidAmount());
   l.setDueDate(rec.getDueDate());
   l.setHomeAmount(rec.getHomeAmount());
   // set AR line text to doc header text
   l.setLineText(rec.getLineText());
   l.setNotes(rec.getNotes());
   if(rec.getPayTerms() != null){
    LOGGER.log(INFO, "Pay terms {0}", rec.getPayTerms());
    PaymentTermsRec  terms = this.sysBuff.getPaymentTerms(rec.getPayTerms());
    l.setPayTerms(terms);
   }
   if(rec.getPayType() != null){
    LOGGER.log(INFO, "Pay type {0}", rec.getPayType());
    PaymentTypeRec payType = sysBuff.getPaymentType(rec.getPayType());
    l.setPayType(payType);
   }
   
   if(rec.getPaymntBank() != null && arAc != null ){
    LOGGER.log(INFO, "Bank {0}", rec.getPaymntBank());
    List<ArBankAccountRec> banks = arAc.getArAccountBanks();
    boolean foundBank = false;
    ListIterator<ArBankAccountRec> bankLi = banks.listIterator();
    while(bankLi.hasNext() && !foundBank ){
     ArBankAccountRec arBank = bankLi.next();
     if(Objects.equals(arBank.getId(), rec.getPaymntBank().getId()) ){
      foundBank = true;
      l.setBankAc(arBank);
     }
    }
    
   }
   l.setReference1(rec.getReference1());
   l.setReference2(rec.getReference2());
   l.setClearedLine(rec.isClearedLine());
   l.setClearingLine(rec.isClearingLine());
   if(rec.getComp() != null){
    CompanyBasicRec comp = this.sysBuff.getCompanyById(rec.getComp().getId());
    l.setComp(comp);
   }
   /*
   if(rec.getDocHeaderBase() != null){
    DocFi docHead = (DocFi)rec.getDocHeaderBase();
    DocFiRec docHeadRec = this.buildDocFiRec(docHead);
    l.setDocHeaderBase(docHeadRec);
   }
   */
   
   LOGGER.log(INFO, "rec.getReconiliationLines() {0}", rec.getReconiliationLines());
   if(rec.getReconiliationLines() != null && !rec.getReconiliationLines().isEmpty() ){
    List<DocLineGlRec> glRecLines = new ArrayList<>();
    ListIterator<DocLineGl> recLinesLi = rec.getReconiliationLines().listIterator();
    while(recLinesLi.hasNext()){
     DocLineGl line = recLinesLi.next();
     DocLineGlRec lineRec = this.buildGlDocLineRec(line,usr,pg);
     lineRec.setReconcilForArLine(l);
     glRecLines.add(lineRec);
    } 
    l.setReconiliationLines(glRecLines);
    LOGGER.log(INFO, "End add reconciliation lines {0} num {1}", new Object[]{l.getReconiliationLines(),
     l.getReconiliationLines().size() });
   }
   
   return l;
  }
  private DocLineArRec buildDocLineArRec(DocLineAr rec,ArAccountRec ar, boolean reconLines){
   LOGGER.log(INFO, "buildDocLineArRec called with line {0} ar account {1} ", 
           new Object[]{rec.getId(),ar});
   LOGGER.log(INFO, "DB line line id {0} line text {1}", new Object[]{rec.getId(), rec.getLineText()});
   DocLineArRec l  = new DocLineArRec();
   l.setId(rec.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(rec.getCreateBy());
   l.setCreateBy(crUsr);
   l.setCreateDate(rec.getCreateDate());
   if(rec.getChangeBy() != null){
    UserRec chUsr = usrDM.getUserRecPvt(rec.getChangeBy());
    l.setChangeBy(chUsr);
    l.setChangeDate(rec.getChangeDate());
   }
   
   l.setLineNum(rec.getLineNum());
   if(rec.getArAccount() != null){
    l.setArAccount(ar);
    
   }
   if(rec.getPostType() != null){
    PostTypeRec ptype =  sysBuff.getPostTypeForCode(rec.getPostType().getPostTypeCode());
    l.setPostType(ptype);
   }
   l.setDocAmount(rec.getDocAmount());
   LineTypeRuleRec lineType = sysBuff.getLineTypeRule(rec.getLineType());
   l.setLineType(lineType);
   l.setOrginalAmount(rec.getOrginalAmount());
   l.setPaidAmount(rec.getPaidAmount());
   l.setDueDate(rec.getDueDate());
   l.setHomeAmount(rec.getHomeAmount());
   l.setLineText(rec.getLineText());
   l.setNotes(rec.getNotes());
   l.setSortOrder(rec.getSortOrder());
   if(rec.getPayTerms() != null){
    PaymentTermsRec  terms = this.sysBuff.getPaymentTerms(rec.getPayTerms());
    l.setPayTerms(terms);
   }
   if(rec.getPayType() != null){
    PaymentTypeRec payType = sysBuff.getPaymentType(rec.getPayType());
    l.setPayType(payType);
   }
   
   if(rec.getPaymntBank() != null){
    List<ArBankAccountRec> banks = ar.getArAccountBanks();
    boolean foundBank = false;
    ListIterator<ArBankAccountRec> bankLi = banks.listIterator();
    while(bankLi.hasNext() && !foundBank ){
     ArBankAccountRec arBank = bankLi.next();
     if(arBank.getId() == rec.getPaymntBank().getId() ){
      foundBank = true;
      l.setBankAc(arBank);
     }
    }
    
   }
   l.setReference1(rec.getReference1());
   l.setReference2(rec.getReference2());
   l.setClearedLine(rec.isClearedLine());
   
   
  
   l.setClearingLine(rec.isClearingLine());
   if(rec.getComp() != null){
    CompanyBasicRec comp = this.sysBuff.getCompanyById(rec.getComp().getId());
    l.setComp(comp);
   }
   
   if(rec.getDocHeaderBase() != null){
    DocFi docHead = (DocFi)rec.getDocHeaderBase();
    DocFiRec docHeadRec = this.buildDocFiRec(docHead);
    l.setDocHeaderBase(docHeadRec);
   }
   
   
   if(reconLines){
   if(rec.getReconiliationLines() != null && !rec.getReconiliationLines().isEmpty()){
    ListIterator<DocLineGl> recLinesLi = rec.getReconiliationLines().listIterator();
    List<DocLineGlRec> glRecLines = new ArrayList<>(); 
    while(recLinesLi.hasNext()){
     DocLineGl glLine = recLinesLi.next();
     DocLineGlRec glLineRec = this.buildGlRecDocLine(glLine);
     glLineRec.setReconcilForArLine(l);
     glRecLines.add(glLineRec);
     
    }
    l.setReconiliationLines(glRecLines);
   }
   }
   LOGGER.log(INFO, "buildDocLineArRec returns line amounts: doc {0} home {1}", new Object[]{l.getDocAmount(),l.getHomeAmount()});
   return l;
  }
  
  private DocLineBasePartial buildDocLineBasePartial(DocLineBasePartial ln, DocLineBasePartialRec rec, String pg ){
   
   boolean changedLine = false;
   if(rec.getId() == null){
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
    ln.setComp(comp);
    ln.setHomeAmount(rec.getHomeAmount());
    ln.setLineNum(rec.getLineNum());
    ln.setLineText(rec.getLineText());
    LineTypeRule lnTy = em.find(LineTypeRule.class, rec.getLineType().getId(), OPTIMISTIC);
    ln.setLineType(lnTy);
    ln.setNotes(rec.getNotes());
    ln.setOrginalAmount(rec.getOrginalAmount());
    ln.setPaidAmount(rec.getPaidAmount());
    PostType pstTy = em.find(PostType.class, rec.getPostType().getId(), OPTIMISTIC);
    ln.setPostType(pstTy);
    ln.setReference1(rec.getReference1());
    ln.setReference2(rec.getReference2());
    ln.setSortOrder(rec.getSortOrder());
    
   }else{
    User chUsr = em.find(User.class, rec.getChangeBy().getId(), OPTIMISTIC);
    if((rec.getComp() == null && ln.getComp() != null)||
       (rec.getComp() != null && ln.getComp() == null) ||
       (rec.getComp() != null && rec.getComp().getId() != rec.getComp().getId())){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_COMP");
     aud.setNewValue(rec.getComp().getReference());
     aud.setOldValue(ln.getComp().getNumber());
     CompanyBasic comp = em.find(CompanyBasic.class, rec.getComp().getId(), OPTIMISTIC);
     ln.setComp(comp);
     changedLine = true;
    }
    
    if(ln.getHomeAmount() != rec.getHomeAmount()){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_HOME_AMNT");
     aud.setNewValue(String.valueOf(rec.getHomeAmount()));
     aud.setOldValue(String.valueOf(ln.getHomeAmount()));
     ln.setHomeAmount(rec.getHomeAmount());
     changedLine = true;
    }
    
    
    if(ln.getLineNum() != rec.getLineNum()){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NUM");
     aud.setNewValue(rec.getLineNum().toString());
     aud.setOldValue(ln.getLineNum().toString());
     ln.setLineNum(rec.getLineNum());
     changedLine = true;
    }
    
    if((rec.getLineText() == null && ln.getLineText() != null)||
       (rec.getLineText() != null && ln.getLineText() == null) ||
       (rec.getLineText() != null && !rec.getLineText().equals(ln.getLineText()))){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getLineText());
     aud.setOldValue(ln.getLineText());
     ln.setLineText(rec.getLineText());
     changedLine = true;
    }
    
    if((rec.getLineType() == null && ln.getLineType() != null)||
       (rec.getLineType() != null && ln.getLineType() == null) ||
       (rec.getLineType() != null && rec.getLineType().getId() != ln.getLineType().getId())){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_LINE_TXT");
     aud.setNewValue(rec.getLineType().getLineCode());
     aud.setOldValue(ln.getLineType().getLineCode());
     LineTypeRule lnTy = em.find(LineTypeRule.class, rec.getLineType().getId(), OPTIMISTIC);
     ln.setLineType(lnTy);
     changedLine = true;
    }
    
    if((rec.getNotes() == null && ln.getNotes() != null)||
       (rec.getNotes() != null && ln.getNotes() == null) ||
       (rec.getNotes() != null && !rec.getNotes().equals(ln.getNotes()))){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NOTES");
     aud.setNewValue(rec.getNotes());
     aud.setOldValue(ln.getNotes());
     ln.setNotes(rec.getNotes());
     changedLine = true;
    }
    
    if(rec.getOrginalAmount() != ln.getOrginalAmount()){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_NOTES");
     aud.setNewValue(String.valueOf(rec.getOrginalAmount()));
     aud.setOldValue(String.valueOf(ln.getOrginalAmount()));
     ln.setOrginalAmount(rec.getOrginalAmount());
     changedLine = true; 
    }
    
    if(rec.getPaidAmount() != ln.getPaidAmount()){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PATD_AMNT");
     aud.setNewValue(String.valueOf(rec.getPaidAmount()));
     aud.setOldValue(String.valueOf(ln.getPaidAmount()));
     ln.setPaidAmount(rec.getPaidAmount());
     changedLine = true; 
    }
    
    
     if((rec.getPostType() == null && ln.getPostType() != null)||
       (rec.getPostType() != null && ln.getPostType() == null) ||
       (rec.getPostType() != null && rec.getPostType().getId() != ln.getPostType().getId())){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_PST_TY");
     aud.setNewValue(rec.getPostType().getPostTypeCode());
     aud.setOldValue(ln.getPostType().getPostTypeCode());
     PostType pstTy = em.find(PostType.class, rec.getLineType().getId(), OPTIMISTIC);
     ln.setPostType(pstTy);
     changedLine = true;
    }
     
    if((rec.getReference1() == null && ln.getReference1() != null)||
       (rec.getReference1() != null && ln.getReference1() == null) ||
       (rec.getReference1() != null && !rec.getReference1().equals(ln.getReference1()))){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_REF1");
     aud.setNewValue(rec.getReference1());
     aud.setOldValue(ln.getReference1());
     ln.setReference1(rec.getReference1());
     changedLine = true;
    }
     
    if((rec.getReference2() == null && ln.getReference2() != null)||
       (rec.getReference2() != null && ln.getReference2() == null) ||
       (rec.getReference2() != null && !rec.getReference2().equals(ln.getReference2()))){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_REF2");
     aud.setNewValue(rec.getReference2());
     aud.setOldValue(ln.getReference2());
     ln.setReference2(rec.getReference2());
     changedLine = true;
    }
    
    if((rec.getSortOrder() == null && ln.getSortOrder() != null)||
       (rec.getSortOrder() != null && ln.getSortOrder() == null) ||
       (rec.getSortOrder() != null && !rec.getSortOrder().equals(ln.getSortOrder()))){
     AuditDocLinePartial aud = buildAuditDocLinePartial(ln, chUsr, pg, 'U');
     aud.setFieldName("DOC_LN_REF2");
     aud.setNewValue(rec.getSortOrder());
     aud.setOldValue(ln.getSortOrder());
     ln.setSortOrder(rec.getSortOrder());
     changedLine = true;
    }
    
    
    
    if(changedLine){
     ln.setChangeBy(chUsr);
     ln.setChangeDate(rec.getChangeDate());
    }
   }
   
   
   return ln;
  }
  
  private DocLineBasePartialRec buildDocLineBasePartialRec(DocLineBasePartial ln, DocLineBasePartialRec rec){
   
   
    CompanyBasicRec comp = this.sysBuff.getCompanyById(ln.getComp().getId());
    rec.setComp(comp);
    rec.setHomeAmount(ln.getHomeAmount());
    rec.setLineNum(ln.getLineNum());
    rec.setLineText(ln.getLineText());
    LineTypeRuleRec lnTy = sysBuff.getLineTypeRule(ln.getLineType());
    rec.setLineType(lnTy);
    rec.setNotes(ln.getNotes());
    rec.setOrginalAmount(ln.getOrginalAmount());
    rec.setPaidAmount(ln.getPaidAmount());
    PostTypeRec pstTy = sysBuff.getPostTypeForCode(ln.getPostType().getPostTypeCode());
    rec.setPostType(pstTy);
    rec.setReference1(ln.getReference1());
    rec.setReference2(ln.getReference2());
    ln.setSortOrder(rec.getSortOrder());
    
   
   return rec;
  }
  private DocLineGlPartial buildDocLineGlPartial(DocLineGlPartialRec rec, String pg){
   
   boolean newLn = false;
   boolean changedLn = false;
   DocLineGlPartial ln;
   
   if(rec.getId() == null){
    ln = new DocLineGlPartial();
    User crUsr = em.find(User.class, rec.getCreateBy().getId(), OPTIMISTIC);
    ln.setCreateBy(crUsr);
    ln.setCreateDate(rec.getCreateDate());
    em.persist(ln);
    AuditDocLinePartial aud = this.buildAuditDocLinePartial(ln, crUsr, pg, 'I');
    aud.setNewValue(rec.getLineNum().toString());
    newLn = true;
   }else{
    ln = em.find(DocLineGlPartial.class, rec.getId(), OPTIMISTIC);
   }
   
   ln = (DocLineGlPartial)buildDocLineBasePartial(ln, rec, pg);
   
   if(newLn){
    ln.setDocAmount(rec.getDocAmount());
    ln.setCurrID(rec.getCurrID());
    
    if(rec.getCostAccount() != null){
     CostAccountDirect cstAc = em.find(CostAccountDirect.class, rec.getCostAccount().getId(), OPTIMISTIC);
     ln.setCostAccount(cstAc);
    }
    if(rec.getCostCentre() != null){
     CostCentre cc = em.find(CostCentre.class, rec.getCostCentre().getId(), OPTIMISTIC);
     ln.setCostCentre(cc);
    }
    if(rec.getGlAccount() != null){
     FiGlAccountComp glAcnt = em.find(FiGlAccountComp.class, rec.getGlAccount().getId(), OPTIMISTIC);
     ln.setGlAccount(glAcnt);
    }
    if(rec.getVatCodeComp() != null){
     VatCodeCompany vatCodeComp = em.find(VatCodeCompany.class, rec.getVatCodeComp().getId(), OPTIMISTIC);
     ln.setVatCodeComp(vatCodeComp);
    }
    
    
   }else{
    // Changed ?
    User chUsr = em.find(User.class, rec.getChangeBy().getId(), OPTIMISTIC);
    if(rec.getCurrID() != ln.getCurrID()){
     
    }
   }
   
   return ln;
  }
  
  private DocLineGlPartialRec buildDocLineGlPartialRec(DocLineGlPartial ln){
   DocLineGlPartialRec rec = new DocLineGlPartialRec();
   rec.setId(ln.getId());
   UserRec crUsr = this.usrDM.getUserRecPvt(ln.getCreateBy());
   rec.setCreateBy(crUsr);
   rec.setCreateDate(ln.getCreateDate());
   
   
   rec = (DocLineGlPartialRec)buildDocLineBasePartialRec(ln,rec);
   rec.setDocAmount(ln.getDocAmount());
   rec.setCurrID(ln.getCurrID());
   
   if(ln.getCostAccount() != null){
    CostAccountDirectRec cstAcRec = this.costCentDM.getCostAccountRecPvt(ln.getCostAccount());
     rec.setCostAccount(cstAcRec);
    }
   if(ln.getCostCentre() != null){
    CostCentreRec ccRec = this.costCentDM.getCostCentreRec(ln.getCostCentre());
    rec.setCostCentre(ccRec);
   }
    
   rec.setCurrID(ln.getCurrID());
    
    
   if(ln.getGlAccount() != null){
    FiGlAccountCompRec glActRec = this.glActDM.buildFiCompGlAccountRecPvt(ln.getGlAccount());
    rec.setGlAccount(glActRec);
   }
   return rec;
  }
  private List<FiPeriodBalance> buildDocLineGlProgramAcBalance(DocLineGl docLine, 
          List<FiPeriodBalance> programBalances,DocFi docHdr){
   LOGGER.log(INFO, "buildDocLineGlAcBalance called with docLine {0} balance list {1} doc Hdr {2} ",
           new Object[]{docLine,programBalances,docHdr});
   FiGlAccountComp glAct = docLine.getGlAccount();
   ProgramAccount progAct = docLine.getProgramCostAccount();
   LOGGER.log(INFO, "Balance for Cost Act ", progAct.getId());
   if(progAct.getId() == null || progAct.getId() == 0){
    throw new BacException("Cannot process cost ac with no cost act id");
   }
   int fyr = docHdr.getFisYear();
   int fper = docHdr.getFisPeriod();
   boolean foundBal = false;
   FiPeriodBalance progPerBal = null;
   if(programBalances == null){
    programBalances = new ArrayList<>();
   }
   ListIterator<FiPeriodBalance> programBalancesLi = programBalances.listIterator();
   while(programBalancesLi.hasNext() && !foundBal){
    FiPeriodBalance listBal = programBalancesLi.next();
    if(listBal.getProgramCostAccount() == progAct  &&
            listBal.getBalPeriod() == fper 
            && listBal.getBalYear() == fyr  ){
     progPerBal = listBal;
     foundBal = true;
    }
   }
   LOGGER.log(INFO, "Found glBal {0} glbal is {1}", new Object[]{foundBal,progPerBal});
   if(!foundBal){
    Query q = em.createNamedQuery("getProgActPerBal");
   // get account period balance
    q.setParameter("progtAc", progAct);
    q.setParameter("fisYear", fyr);
    q.setParameter("fisPer", fper);
    q.setParameter("balType", 6);   // Cost centre actual cost
    try{
    progPerBal = (FiPeriodBalance)q.getSingleResult();
    
    }catch(NoResultException ex){
     LOGGER.log(INFO, "Need to create a new program act balance");
     progPerBal = new FiPeriodBalance();
//     progPerBal.setCreatedBy(usr);
//     progPerBal.setCreated(currentDate);
     progPerBal.setFiGlAccountComp(glAct);
     progPerBal.setProgramCostAccount(progAct);
     progPerBal.setBalPeriod(fper);
     progPerBal.setBalYear(fyr);
     progPerBal.setBalanceType(6);  // Cost Centre act cost
     em.persist(progPerBal);
     LOGGER.log(INFO, "new cost balance {0}", progPerBal);
    }
   }
  LOGGER.log(INFO, "glPerBal now {0}", progPerBal);
  
  if(docLine.getPostType().isDebit()){
    // debit line
    double currDebit = progPerBal.getPeriodDebitAmount();
    progPerBal.setPeriodDebitAmount(currDebit + docLine.getDocAmount());
    docLine.setDebitBalance(progPerBal);
    docLine.setProjectDebtBalance(progPerBal);
    }else{
    // credit line
    double currCredit = progPerBal.getPeriodCreditAmount();
    progPerBal.setPeriodCreditAmount(currCredit + docLine.getDocAmount());
    docLine.setCreditBalance(progPerBal);
    docLine.setProjectCreditBalance(progPerBal);
   }
   progPerBal.setPeriodDocAmount(progPerBal.getPeriodDebitAmount() - progPerBal.getPeriodCreditAmount() );
   progPerBal.setPeriodLocalAmount(progPerBal.getPeriodDocAmount());
   progPerBal.setCfwdDocAmount(progPerBal.getBfwdDocAmount() + progPerBal.getPeriodDocAmount());
   progPerBal.setCfwdLocalAmount(progPerBal.getCfwdDocAmount());
   
   
   //update record in balances list
   programBalancesLi = programBalances.listIterator();
   boolean balFound = false;
   while(programBalancesLi.hasNext() && !balFound){
    FiPeriodBalance listBal = programBalancesLi.next();
    if(listBal == progPerBal){
     programBalancesLi.set(progPerBal);
     balFound = true;
    }
    if(!balFound){
     throw new BacException("Could not update program balance");
    }
   }
   return programBalances;
  }
  
  private List<FiPeriodBalance> buildDocLineGlCostAcBalance(DocLineGl docLine, 
          List<FiPeriodBalance> costAcBalances,DocFi docHdr){
   LOGGER.log(INFO, "buildDocLineGlAcBalance called with docLine {0} balance list {1} doc Hdr {2} ",
           new Object[]{docLine,costAcBalances,docHdr});
   FiGlAccountComp glAct = docLine.getGlAccount();
   CostAccountDirect costAct = docLine.getCostAccount();
   LOGGER.log(INFO, "Balance for Cost Act ", costAct.getId());
   if(costAct.getId() == null || costAct.getId() == 0){
    throw new BacException("Cannot process cost ac with no cost act id");
   }
   int fyr = docHdr.getFisYear();
   int fper = docHdr.getFisPeriod();
   boolean foundBal = false;
   FiPeriodBalance glPerBal = null;
   if(costAcBalances == null){
    costAcBalances = new ArrayList<>();
   }
   ListIterator<FiPeriodBalance> costAcBalancesLi = costAcBalances.listIterator();
   while(costAcBalancesLi.hasNext() && !foundBal){
    FiPeriodBalance listBal = costAcBalancesLi.next();
    if(listBal.getCostAccountActual() == costAct  &&
            listBal.getBalPeriod() == fper 
            && listBal.getBalYear() == fyr  ){
     glPerBal = listBal;
     foundBal = true;
    }
   }
   LOGGER.log(INFO, "Found glBal {0} glbal is {1}", new Object[]{foundBal,glPerBal});
   if(!foundBal){
    Query q = em.createNamedQuery("getCostActPerBal");
   // get account period balance
    q.setParameter("costAc", costAct);
    q.setParameter("fisYear", fyr);
    q.setParameter("fisPer", fper);
    q.setParameter("balType", 4);   // Cost centre actual cost
    try{
    glPerBal = (FiPeriodBalance)q.getSingleResult();
    
    }catch(NoResultException ex){
     LOGGER.log(INFO, "Need to create a new cost act balance");
     glPerBal = new FiPeriodBalance();
     //glPerBal.setCreatedBy(usr);
     //glPerBal.setCreated(currentDate);
     glPerBal.setFiGlAccountComp(glAct);
     glPerBal.setCostAccountActual(costAct);
     glPerBal.setBalPeriod(fper);
     glPerBal.setBalYear(fyr);
     glPerBal.setBalanceType(4);  // Cost Centre act cost
     em.persist(glPerBal);
     LOGGER.log(INFO, "new cost balance {0}", glPerBal);
    }
   }
  LOGGER.log(INFO, "glPerBal now {0}", glPerBal);
   if(docLine.getPostType().isDebit()){
    // debit line
    double currDebit = glPerBal.getPeriodDebitAmount();
    glPerBal.setPeriodDebitAmount(currDebit + docLine.getDocAmount());
    docLine.setDebitBalance(glPerBal);
    docLine.setCostAcDebtBalance(glPerBal);
    }else{
    // credit line
    double currCredit = glPerBal.getPeriodCreditAmount();
    glPerBal.setPeriodCreditAmount(currCredit + docLine.getDocAmount());
    docLine.setCreditBalance(glPerBal);
    docLine.setCostCreditBalance(glPerBal);
   }
   glPerBal.setPeriodDocAmount(glPerBal.getPeriodDebitAmount() - glPerBal.getPeriodCreditAmount() );
   glPerBal.setPeriodLocalAmount(glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdDocAmount(glPerBal.getBfwdDocAmount() + glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdLocalAmount(glPerBal.getCfwdDocAmount());
   
   
   //update record in balances list
   costAcBalancesLi = costAcBalances.listIterator();
   boolean balFound = false;
   while(costAcBalancesLi.hasNext() && !balFound){
    FiPeriodBalance listBal = costAcBalancesLi.next();
    if(listBal == glPerBal){
     costAcBalancesLi.set(glPerBal);
     balFound = true;
    }
    if(!balFound){
     throw new BacException("Could not update balance");
    }
   }
   
   
   return costAcBalances;
  }
  
  private List<FiPeriodBalance> buildDocLineGlRestrfndAcBalance(DocLineGl docLine, 
          List<FiPeriodBalance> glBalances,DocFi docHdr){
   LOGGER.log(INFO, "buildDocLineGlRestrfndAcBalance called with docLine {0} balance list {1} doc Hdr {2} ",
           new Object[]{docLine,glBalances,docHdr});
   
  
   
   FiGlAccountComp glAct = docLine.getGlAccount();
   Fund fnd = docLine.getRestrictedFund();
   LOGGER.log(INFO, "balance for GL account {0} resttricted balance {1}",new Object[]{
    glAct,fnd});
   if(glAct.getId() == null || fnd.getId() == null ){
    throw new BacException("Gl account and restricted balance required to set GL account balance");
   }
   // do we have the restricted
   int fyr = docHdr.getFisYear();
   int fper = docHdr.getFisPeriod();
   boolean foundBal = false;
   FiPeriodBalance glPerBal = null;
   if(glBalances == null){
    glBalances = new ArrayList<>();
   }
   ListIterator<FiPeriodBalance> glBalancesLi = glBalances.listIterator();
   while(glBalancesLi.hasNext() && !foundBal){
    FiPeriodBalance listBal = glBalancesLi.next();
    if(listBal.getFiGlAccountComp() == glAct && listBal.getRestrictedFund() == fnd &&
            listBal.getBalPeriod() == fper 
            && listBal.getBalYear() == fyr  ){
     glPerBal = listBal;
     foundBal = true;
    }
   }
   LOGGER.log(INFO, "Found glBal {0} glbal is {1}", new Object[]{foundBal,glPerBal});
   if(!foundBal){
    Query q = em.createNamedQuery("getGlAccRestrPerBal");
   // get account period balance
    q.setParameter("glAc", glAct);
    q.setParameter("fnd", fnd);
    q.setParameter("fisYear", fyr);
    q.setParameter("fisPer", fper);
    q.setParameter("balType", 1);
    try{
    glPerBal = (FiPeriodBalance)q.getSingleResult();
    
    }catch(NoResultException ex){
     LOGGER.log(INFO, "Need to create a new balance");
     glPerBal = new FiPeriodBalance();
     //glPerBal.setCreatedBy(usr);
    // glPerBal.setCreated(currentDate);
     glPerBal.setFiGlAccountComp(glAct);
     glPerBal.setRestrictedFund(fnd);
     glPerBal.setBalPeriod(fper);
     glPerBal.setBalYear(fyr);
     glPerBal.setBalanceType(1);
     em.persist(glPerBal);
     LOGGER.log(INFO, "new gl balance {0}", glPerBal);
    }
   }
   
  // balance record to docLine
   
   LOGGER.log(INFO, "glPerBal now {0}", glPerBal);
   if(docLine.getPostType().isDebit()){
    // debit line
    double currDebit = glPerBal.getPeriodDebitAmount();
    glPerBal.setPeriodDebitAmount(currDebit + docLine.getDocAmount());
    docLine.setDebitBalance(glPerBal);
    docLine.setRestrictedDebitBalance(glPerBal);
    
   }else{
    // credit line
    double currCredit = glPerBal.getPeriodCreditAmount();
    glPerBal.setPeriodCreditAmount(currCredit + docLine.getDocAmount());
    docLine.setCreditBalance(glPerBal);
    docLine.setRestrictedCreditBalance(glPerBal);
   }
   glPerBal.setPeriodDocAmount(glPerBal.getPeriodDebitAmount() - glPerBal.getPeriodCreditAmount() );
   glPerBal.setPeriodLocalAmount(glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdDocAmount(glPerBal.getBfwdDocAmount() + glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdLocalAmount(glPerBal.getCfwdDocAmount());
   
   
   //update record in balances list
   glBalancesLi = glBalances.listIterator();
   boolean balFound = false;
   while(glBalancesLi.hasNext() && !balFound){
    FiPeriodBalance listBal = glBalancesLi.next();
    if(listBal == glPerBal){
     glBalancesLi.set(glPerBal);
     balFound = true;
    }
    if(!balFound){
     throw new BacException("Could not update balance");
    }
   }
   
   
   return glBalances;
  }
  
  private List<FiPeriodBalance> buildDocLineGlAcBalance(DocLineGl docLine, 
          List<FiPeriodBalance> glBalances,DocFi docHdr){
   LOGGER.log(INFO, "buildDocLineGlAcBalance called with docLine {0} balance list {1} doc Hdr {2} ",
           new Object[]{docLine,glBalances,docHdr});
   
   FiGlAccountComp glAct = docLine.getGlAccount();
   LOGGER.log(INFO, "glAct.getId() {0}", glAct.getId());
   if(glAct.getId() == null ){
    throw new BacException("Gl account required to set GL account balance");
   }
   LOGGER.log(INFO, "line post type {0} glaccount {1}", new Object[]{docLine.getPostType(),
    docLine.getGlAccount() });
   LOGGER.log(INFO, "line post type {0} debit {1} glaccount {2}", new Object[]{docLine.getPostType().getPostTypeCode(),
    docLine.getPostType().isDebit(),docLine.getGlAccount().getCoaAccount().getRef()
   });
    FiPeriodBalance glPerBal = null; 
   // do we already have the GL balance record?
   boolean foundBal = false;
   if(glBalances != null && !glBalances.isEmpty()){
    ListIterator<FiPeriodBalance> balLi = glBalances.listIterator();
    while(balLi.hasNext() && !foundBal){
     FiPeriodBalance currBal = balLi.next();
     if(Objects.equals(glAct.getId(), currBal.getFiGlAccountComp().getId())&&
       Objects.equals(docHdr.getFisYear(), currBal.getBalYear()) &&
       Objects.equals(docHdr.getFisPeriod(), currBal.getBalPeriod())){
      glPerBal = currBal;
      foundBal = true;
     }
    }
   }
   
   LOGGER.log(INFO,"After check for bal foundBal {0}",foundBal);
   if(!foundBal){
    Query glPerBalQ = em.createNamedQuery("getGlAccPerBal");
    glPerBalQ.setParameter("glAcId", glAct.getId());
    glPerBalQ.setParameter("fisYear", docHdr.getFisYear());
    glPerBalQ.setParameter("fisPer", docHdr.getFisPeriod());
    glPerBalQ.setParameter("balType", FiPeriodBalance.GL_ACT);
  
    try{
     glPerBal = (FiPeriodBalance)glPerBalQ.getSingleResult();
     LOGGER.log(INFO, " query to finr glPerBal returned {0}", glPerBal.getId());
    
    }catch(NoResultException ex ){
     glPerBal = new FiPeriodBalance();
     glPerBal.setFiGlAccountComp(glAct);
     glPerBal.setBalPeriod(docHdr.getFisPeriod());
     glPerBal.setBalYear(docHdr.getFisYear());
     glPerBal.setBalanceType(0);
     em.persist(glPerBal);
     LOGGER.log(INFO,"Created after NoResultException id {0} ",glPerBal.getId());
    }catch(PersistenceException ex ){
     LOGGER.log(INFO,"bal find error {0}",ex.getLocalizedMessage());
    }
   }
   
   
   
   if(glPerBal == null){
    glPerBal = new FiPeriodBalance();
   }
   
   
   if(docLine.getPostType().isDebit()){
    // debit line
    double currDebit = glPerBal.getPeriodDebitAmount();
    LOGGER.log(INFO, "currDebit starts as {0}", currDebit);
    currDebit += docLine.getDocAmount();
    LOGGER.log(INFO, "currDebit ends  as {0}", currDebit);
    glPerBal.setPeriodDebitAmount(currDebit );
    docLine.setDebitBalance(glPerBal);
   }else{
    // credit line
    double currCredit = glPerBal.getPeriodCreditAmount();
    LOGGER.log(INFO, "currCredit starts  as {0}", currCredit);
    currCredit +=  docLine.getDocAmount();
    LOGGER.log(INFO, "currCredit ends  as {0}", currCredit);
    glPerBal.setPeriodCreditAmount(currCredit );
    docLine.setCreditBalance(glPerBal);
   }
   glPerBal.setPeriodDocAmount(glPerBal.getPeriodDebitAmount() - glPerBal.getPeriodCreditAmount() );
   glPerBal.setPeriodLocalAmount(glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdDocAmount(glPerBal.getBfwdDocAmount() + glPerBal.getPeriodDocAmount());
   glPerBal.setCfwdLocalAmount(glPerBal.getCfwdDocAmount());
   
   if(glBalances == null){
    glBalances = new ArrayList<>();
    glBalances.add(glPerBal);
   }else{
    ListIterator<FiPeriodBalance> bLi = glBalances.listIterator();
    boolean balFound = false;
    while(bLi.hasNext() && !balFound){
     FiPeriodBalance currBal = bLi.next();
     if(Objects.equals(currBal.getId(), glPerBal.getId())){
      bLi.set(glPerBal);
      balFound = true;
     }
    }
    if(!balFound){
     glBalances.add(glPerBal);
    }
   }
   
   
   LOGGER.log(INFO, "End of acnt balance - glBalances {0} ", glBalances);
   return glBalances;
  }
  
  private DocBase buildDocBase(DocBaseRec rec, String pg){
   LOGGER.log(INFO, "buildDocFi called with {0}", rec);
    boolean newDoc = false;
    boolean changedDoc = false;
    DocBase doc;
    if(rec.getId() == null){
     User crUsr = em.find(User.class, rec.getCreatedBy().getId(), OPTIMISTIC);
     doc = new DocFi();
     doc.setCreatedBy(crUsr);
     doc.setCreateOn(rec.getCreateOn());
     CompanyBasic comp = sysBuff.getComp(rec.getCompany());
     doc.setDocNumber(nextCompDocNumber(comp, "FI_DOC_FIN"));
     em.persist(doc);
     AuditDocFi aud = this.buildAuditDocFi((DocFi)doc, crUsr, doc.getCreateOn(), pg, 'I');
     aud.setNewValue(String.valueOf(doc.getDocNumber()));
     newDoc = true;
     
    }else{
     doc = em.find(DocFi.class, rec.getId(), OPTIMISTIC);
    }
    if(newDoc){
     doc.setDocHdrText(rec.getDocHdrText());
     //DocType docTy = em.find(DocType.class, rec.getDocType().getId());
     //doc.setDocumentDate(rec.getDocumentDate());
     //doc.setPartnerRef(rec.);
     //doc.setPostingDate(rec.getPostingDate());
     //doc.setTaxDate(rec.getTaxDate());
     //doc.setFisPeriod(docRec.getFisPeriod());
     //doc.setFisYear(rec.getFisYear());
     doc.setNotes(rec.getNotes());
     CompanyBasic comp = this.sysBuff.getComp(rec.getCompany());
     doc.setCompany(comp);
    
    }else{
     User chUsr = em.find(User.class, rec.getChangedBy().getId(), OPTIMISTIC);
     
     if((rec.getDocHdrText() == null && doc.getDocHdrText() != null) || 
        (rec.getDocHdrText() != null && doc.getDocHdrText() == null) ||
        (rec.getDocHdrText() != null && !rec.getDocHdrText().equals(doc.getDocHdrText()))){
      AuditDocFi aud = this.buildAuditDocFi((DocFi)doc, chUsr, doc.getCreateOn(), pg, 'U');
      aud.setFieldName("docHdrText");
      aud.setNewValue(rec.getDocHdrText());
      aud.setOldValue(doc.getDocHdrText());
      doc.setDocHdrText(rec.getDocHdrText());
      changedDoc = true;
     }
     if((rec.getNotes() == null && doc.getNotes() != null) || 
        (rec.getNotes() != null && doc.getNotes() == null) ||
        (rec.getNotes() != null && !rec.getNotes().equals(doc.getNotes()))){
      AuditDocFi aud = this.buildAuditDocFi((DocFi)doc, chUsr, doc.getCreateOn(), pg, 'U');
      aud.setFieldName("docNotes");
      aud.setNewValue(rec.getNotes());
      aud.setOldValue(doc.getNotes());
      doc.setNotes(rec.getNotes());
      changedDoc = true;
     }
     
     if(changedDoc){
      doc.setChangedBy(chUsr);
      doc.setChangedOn(rec.getChangedOn());
     }
    }
    
   LOGGER.log(INFO, "buildDocFi doc number {0} doc id {1} comp {2}",
     new Object[]{doc.getDocNumber(), doc.getId(), doc.getCompany().getId()});
   return doc;
  }
  private DocFi buildDocFi(DocFiRec docRec,CompanyBasic comp,User usr, String pg){
   DocFi doc = buildDocFi(docRec, pg);
   doc.setCompany(comp);
   return doc;
  } 
  
  private DocFi buildDocFi(DocFiRec docRec,String pg ){
    LOGGER.log(INFO, "buildDocFi called with {0}", docRec);
    boolean newDoc = false;
    boolean changedDoc = false;
    DocFi doc;
    if(docRec.getId() == null){
     LOGGER.log(INFO, "new doc Header");
     User crUsr = em.find(User.class, docRec.getCreatedBy().getId(), OPTIMISTIC);
     doc = new DocFi();
     doc.setCreatedBy(crUsr);
     doc.setCreateOn(docRec.getCreateOn());
     CompanyBasic comp = sysBuff.getComp(docRec.getCompany());
     doc.setDocNumber(nextCompDocNumber(comp, "FI_DOC_FIN"));
     doc.setCompany(comp);
     em.persist(doc);
     AuditDocFi aud = this.buildAuditDocFi(doc, crUsr, doc.getCreateOn(), pg, 'I');
     aud.setNewValue(String.valueOf(doc.getDocNumber()));
     newDoc = true;
    }else{
     doc = em.find(DocFi.class, docRec.getId(), OPTIMISTIC);
    }
   
    if(newDoc){
     doc.setDocHdrText(docRec.getDocHdrText());
     DocType docTy = em.find(DocType.class, docRec.getDocType().getId());
     doc.setDocType(docTy);
     doc.setDocumentDate(docRec.getDocumentDate());
     doc.setPartnerRef(docRec.getPartnerRef());
     doc.setPostingDate(docRec.getPostingDate());
     doc.setTaxDate(docRec.getTaxDate());
     doc.setFisPeriod(docRec.getFisPeriod());
     doc.setFisYear(docRec.getFisYear());
     doc.setNotes(docRec.getNotes());
     
    
    }else{
     User chUsr = em.find(User.class, docRec.getChangedBy().getId(), OPTIMISTIC);
     
     if((docRec.getDocHdrText() == null && doc.getDocHdrText() != null) || 
        (docRec.getDocHdrText() != null && doc.getDocHdrText() == null) ||
        (docRec.getDocHdrText() != null && !docRec.getDocHdrText().equals(doc.getDocHdrText()))){
      AuditDocFi aud = this.buildAuditDocFi(doc, chUsr, doc.getCreateOn(), pg, 'U');
      aud.setFieldName("docHdrText");
      aud.setNewValue(docRec.getDocHdrText());
      aud.setOldValue(doc.getDocHdrText());
      doc.setDocHdrText(docRec.getDocHdrText());
      changedDoc = true;
     }
     if((docRec.getNotes() == null && doc.getNotes() != null) || 
        (docRec.getNotes() != null && doc.getNotes() == null) ||
        (docRec.getNotes() != null && !docRec.getNotes().equals(doc.getNotes()))){
      AuditDocFi aud = this.buildAuditDocFi(doc, chUsr, doc.getCreateOn(), pg, 'U');
      aud.setFieldName("docNotes");
      aud.setNewValue(docRec.getNotes());
      aud.setOldValue(doc.getNotes());
      doc.setNotes(docRec.getNotes());
      changedDoc = true;
     }
     if((docRec.getPartnerRef() == null && doc.getPartnerRef() != null) || 
        (docRec.getPartnerRef() != null && doc.getPartnerRef() == null) ||
        (docRec.getPartnerRef() != null && !docRec.getPartnerRef().equals(doc.getPartnerRef()))){
      AuditDocFi aud = this.buildAuditDocFi(doc, chUsr, doc.getCreateOn(), pg, 'U');
      aud.setFieldName("docNotes");
      aud.setNewValue(docRec.getPartnerRef());
      aud.setOldValue(doc.getPartnerRef());
      doc.setPartnerRef(docRec.getPartnerRef());
      changedDoc = true;
     }
     if(changedDoc){
      doc.setChangedBy(chUsr);
      doc.setChangedOn(docRec.getChangedOn());
     }
    }
   if(changedDoc || newDoc){
    LOGGER.log(INFO, "Flush Doc header");
    em.flush();
   } 
   LOGGER.log(INFO, "buildDocFi doc number {0} doc id {1} comp {2}",
     new Object[]{doc.getDocNumber(), doc.getId(), doc.getCompany().getId()});
   return doc;
  }
  
  public DocFiRec buildDocFiRecPvt(DocFi docRec ){
   return buildDocFiRec(docRec);
  }
  
  private DocBaseRec buildDocBaseRec(DocBase doc){
   LOGGER.log(INFO, "buildDocFiRec called with {0}", doc);
    DocBaseRec rec = new DocBaseRec();
    rec.setId(doc.getId());
    UserRec crUsr = this.usrDM.getUserRecPvt(doc.getCreatedBy());
    rec.setCreatedBy(crUsr);
    rec.setCreateOn(doc.getCreateOn());
    if(doc.getChangedBy() != null){
     UserRec updUser = usrDM.getUserRecPvt(doc.getChangedBy());
     rec.setChangedBy(updUser);
     rec.setChangedOn(doc.getChangedOn());
    }
   if(doc.getCompany() != null){
    CompanyBasicRec comp = sysBuff.getCompanyById(doc.getCompany().getId());
    rec.setCompany(comp);
   }
   if(doc.getReversalDoc() != null){
    rec.setReversalDoc(rec); 
   }
   if(doc.getDocHdrText() != null){
     rec.setDocHdrText(doc.getDocHdrText());
   }
   rec.setDocNumber(doc.getDocNumber());
   rec.setNotes(doc.getNotes());
   
   LOGGER.log(INFO, "doc returned with number {0}", doc.getDocNumber());
   return rec;
  }
  
  private DocFiRec buildDocFiRec(DocFi docRec ){
    LOGGER.log(INFO, "buildDocFiRec called with {0}", docRec);
    DocFiRec doc = new DocFiRec();
    doc.setId(docRec.getId());
    UserRec crUsr = this.usrDM.getUserRecPvt(docRec.getCreatedBy());
    doc.setCreatedBy(crUsr);
    doc.setCreateOn(docRec.getCreateOn());
    if(docRec.getChangedBy() != null){
     UserRec updUser = usrDM.getUserRecPvt(docRec.getChangedBy());
     doc.setChangedBy(updUser);
     doc.setChangedOn(doc.getChangedOn());
    }
   if(docRec.getCompany() != null){
    CompanyBasicRec comp = sysBuff.getCompanyById(docRec.getCompany().getId());
    doc.setCompany(comp);
   }
   if(docRec.getReversalDoc() != null){
    doc.setReversed(true);
   }
   if(docRec.getDocHdrText() != null){
     doc.setDocHdrText(docRec.getDocHdrText());
   }
   doc.setDocNumber(docRec.getDocNumber());
   LOGGER.log(INFO, "Doc Ar INV {0}", docRec.getDocInvoiceAr());
   if(docRec.getDocInvoiceAr() != null){
    doc.setDocInvoiceAr(this.buildDocInvoiceArRec(docRec.getDocInvoiceAr()));
   }
   LOGGER.log(INFO, "Doc has Ar INV {0}",doc.getDocInvoiceAr()); 
   
   
   if(docRec.getDocType() != null){
    List<DocTypeRec> docTypes = sysBuff.getDocTypes();
    ListIterator<DocTypeRec> docTypeLi = docTypes.listIterator();
    boolean docTypeFound = false;
    while(docTypeLi.hasNext() && !docTypeFound){
     DocTypeRec test = docTypeLi.next();
     if(Objects.equals(test.getId(), docRec.getDocType().getId())){
      doc.setDocType(test);
      docTypeFound = true;
     }
    }
   }
    
   doc.setDocumentDate(docRec.getDocumentDate());
   doc.setPartnerRef(docRec.getPartnerRef());
   doc.setPostingDate(docRec.getPostingDate());
   doc.setTaxDate(docRec.getTaxDate());
   doc.setFisPeriod(docRec.getFisPeriod());
   doc.setFisYear(docRec.getFisYear());
   doc.setNotes(docRec.getNotes());
   LOGGER.log(INFO, "db doc ref {0}", docRec.getPartnerRef());
   
   LOGGER.log(INFO, "doc returned with number {0}", doc.getDocNumber());
   return doc;
  }
 
 private DocFiTemplAccrPrePay buildDocFiTemplAccPrePay(DocFiTemplAccrPrePayRec tmplRec, String page){
  boolean newTmpl = false;
  boolean changedTmpl = false;
  DocFiTemplAccrPrePay tmpl;
  if(tmplRec.getId()== null){
   tmpl = new DocFiTemplAccrPrePay();
   User crUsr = em.find(User.class, tmplRec.getCreatedBy().getId(), OPTIMISTIC);
   tmpl.setCreatedBy(crUsr);
   tmpl.setCreateOn(tmplRec.getCreateOn());
   CompanyBasic comp = sysBuff.getComp(tmplRec.getCompany());
   tmpl.setCompany(comp);
   tmpl.setDocHdrText(tmplRec.getDocHdrText());
   long docNum = nextCompDocNumber(comp,DocFiTemplAccrPrePay.DOC_TYPE);
   tmpl.setDocNumber(docNum);
   em.persist(tmpl);
   AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, crUsr, page, 'I');
   aud.setNewValue(String.valueOf(tmpl.getDocNumber()));
   newTmpl = true;
  }else{
   tmpl = em.find(DocFiTemplAccrPrePay.class, tmplRec.getId(), OPTIMISTIC);
  }
   
  if(newTmpl){
   
   DocType dt = em.find(DocType.class, tmplRec.getDocType().getId(), OPTIMISTIC);
   tmpl.setDocType(dt);
   tmpl.setDocumentDate(tmplRec.getDocumentDate());
   tmpl.setFisPeriod(tmplRec.getFisPeriod());
   tmpl.setFisYear(tmplRec.getFisYear());
   tmpl.setNextPostDate(tmplRec.getNextPostDate());
   tmpl.setNotes(tmplRec.getNotes());
   tmpl.setPartnerRef(tmplRec.getPartnerRef());
   tmpl.setPostingDate(tmplRec.getPostingDate());
   tmpl.setTaxDate(tmplRec.getTaxDate());
   tmpl.setTmplType(tmplRec.getTmplType());
   TransactionType tt = em.find(TransactionType.class, tmplRec.getTransactionType().getId(), OPTIMISTIC);
   tmpl.setTransactionType(tt);
  }else{
   //changed ?
   User chUsr = em.find(User.class, tmplRec.getChangedBy().getId(), OPTIMISTIC);
   
   if(!Objects.equals(tmplRec.getCompany().getId(), tmpl.getCompany().getId())){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_COMP");
    aud.setNewValue(tmplRec.getCompany().getReference());
    aud.setOldValue(tmpl.getCompany().getNumber());
    CompanyBasic comp = em.find(CompanyBasic.class, tmplRec.getCompany().getId(), OPTIMISTIC);
    tmpl.setCompany(comp);
    changedTmpl = true;
   }
   
   if(tmplRec.isComplete() != tmpl.isComplete()){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_COMPL");
    aud.setNewValue(String.valueOf(tmplRec.isComplete()));
    aud.setOldValue(String.valueOf(tmplRec.isComplete()));
    tmpl.setComplete(tmplRec.isComplete());
    changedTmpl = true;
   }
   
   if((tmplRec.getDocHdrText() == null && tmpl.getDocHdrText() != null) ||
      (tmplRec.getDocHdrText() != null && tmpl.getDocHdrText() == null) || 
      (tmplRec.getDocHdrText() != null && !tmplRec.getDocHdrText().equals(tmpl.getDocHdrText()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_HDR_TXT");
    aud.setNewValue(tmplRec.getDocHdrText());
    aud.setOldValue(tmpl.getDocHdrText());
    tmpl.setDocHdrText(tmplRec.getDocHdrText());
    changedTmpl = true;
   }
   
   if((tmplRec.getDocType() == null && tmpl.getDocType() != null) ||
      (tmplRec.getDocType() != null && tmpl.getDocType() == null) || 
      (tmplRec.getDocType() != null && 
           !Objects.equals(tmplRec.getDocType().getId(), tmpl.getDocType().getId()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_DOC_TY");
    aud.setNewValue(tmplRec.getDocType().getCode());
    aud.setOldValue(tmpl.getDocType().getCode());
    DocType dt = em.find(DocType.class, tmplRec.getDocType().getId(), OPTIMISTIC);
    tmpl.setDocType(dt);
    changedTmpl = true;
   }
   
   if((tmplRec.getDocumentDate() == null && tmpl.getDocumentDate() != null) ||
      (tmplRec.getDocumentDate() != null && tmpl.getDocumentDate() == null) || 
      (tmplRec.getDocumentDate() != null && 
           !tmplRec.getDocumentDate().equals(tmpl.getDocumentDate()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_DOC_DT");
    aud.setNewValue(tmplRec.getDocumentDate().toString());
    aud.setOldValue(tmpl.getDocumentDate().toString());
    tmpl.setDocumentDate(tmplRec.getDocumentDate());
    changedTmpl = true;
   }
   
   if((tmplRec.getNextPostDate() == null && tmpl.getNextPostDate() != null) ||
      (tmplRec.getNextPostDate() != null && tmpl.getNextPostDate() == null) || 
      (tmplRec.getNextPostDate() != null && 
           !tmplRec.getNextPostDate().equals(tmpl.getNextPostDate()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_NXT_PST_DT");
    aud.setNewValue(tmplRec.getNextPostDate().toString());
    aud.setOldValue(tmpl.getNextPostDate().toString());
    tmpl.setNextPostDate(tmplRec.getNextPostDate());
    changedTmpl = true;
   }
   
   if(tmplRec.getNumPosted() != tmpl.getNumPosted()){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_NUM_PST");
    aud.setNewValue(String.valueOf(tmplRec.getNumPosted()));
    aud.setOldValue(String.valueOf(tmpl.getNumPosted()));
    tmpl.setNumPosted(tmplRec.getNumPosted());
    changedTmpl = true;
   }
   
   if(tmplRec.getNumRecur() != tmpl.getNumRecur()){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_NUM_RECUR");
    aud.setNewValue(String.valueOf(tmplRec.getNumRecur()));
    aud.setOldValue(String.valueOf(tmpl.getNumRecur()));
    tmpl.setNumRecur(tmplRec.getNumRecur());
    changedTmpl = true;
   }
   
   if((tmplRec.getPartnerRef() == null && tmpl.getPartnerRef() != null) ||
      (tmplRec.getPartnerRef() != null && tmpl.getPartnerRef() == null) || 
      (tmplRec.getPartnerRef() != null && 
           !tmplRec.getPartnerRef().equals(tmpl.getPartnerRef()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_PTNR_REF");
    aud.setNewValue(tmplRec.getPartnerRef());
    aud.setOldValue(tmpl.getPartnerRef());
    tmpl.setPartnerRef(tmplRec.getPartnerRef());
    changedTmpl = true;
   }
   
   if((tmplRec.getPostingDate()== null && tmpl.getPostingDate() != null) ||
      (tmplRec.getPostingDate() != null && tmpl.getPostingDate() == null) || 
      (tmplRec.getPostingDate() != null && 
           !tmplRec.getPostingDate().equals(tmpl.getPostingDate()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_PST_DT");
    aud.setNewValue(tmplRec.getPostingDate().toString());
    aud.setOldValue(tmpl.getPostingDate().toString());
    tmpl.setPostingDate(tmplRec.getPostingDate());
    changedTmpl = true;
   }
   
   if((tmplRec.getTaxDate()== null && tmpl.getTaxDate() != null) ||
      (tmplRec.getTaxDate() != null && tmpl.getTaxDate() == null) || 
      (tmplRec.getTaxDate() != null && 
           !tmplRec.getTaxDate().equals(tmpl.getTaxDate()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TAX_DT");
    aud.setNewValue(tmplRec.getTaxDate().toString());
    aud.setOldValue(tmpl.getTaxDate().toString());
    tmpl.setTaxDate(tmplRec.getTaxDate());
    changedTmpl = true;
   }
   
   if((tmplRec.getRecurUom()== null && tmpl.getRecurUom() != null) ||
      (tmplRec.getRecurUom() != null && tmpl.getRecurUom() == null) || 
      (!Objects.equals(tmplRec.getRecurUom().getId(), tmpl.getRecurUom().getId()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_FREQ_UNIT");
    aud.setNewValue(tmplRec.getRecurUom().getUomCode());
    aud.setOldValue(tmpl.getRecurUom().getUomCode());
    Uom uom = em.find(Uom.class, tmplRec.getRecurUom().getId(), OPTIMISTIC);
    tmpl.setRecurUom(uom);
    changedTmpl = true;
   }
   
   if((tmplRec.getTransactionType()== null && tmpl.getTransactionType() != null) ||
      (tmplRec.getTransactionType() != null && tmpl.getTransactionType() == null) || 
      (!Objects.equals(tmplRec.getTransactionType().getId(), tmpl.getTransactionType().getId()))){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TMPL_FREQ_UNIT");
    aud.setNewValue(tmplRec.getTransactionType().getCode());
    aud.setOldValue(tmpl.getTransactionType().getCode());
    TransactionType tt = em.find(TransactionType.class, tmplRec.getTransactionType().getId(), OPTIMISTIC);
    tmpl.setTransactionType(tt);
    changedTmpl = true;
   }
   
   if(tmplRec.getTmplType() !=  tmpl.getTmplType() ){
    AuditDocFiTemplateBase aud = buildAuditDocFiTemplate(tmpl, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TRAN_TY");
    aud.setNewValue(tmplRec.getTransactionType().getCode());
    aud.setOldValue(tmpl.getTransactionType().getCode());
    TransactionType tt = em.find(TransactionType.class, tmplRec.getTransactionType().getId(), OPTIMISTIC);
    tmpl.setTransactionType(tt);
    changedTmpl = true;
   }
   if(changedTmpl){
    tmpl.setChangedBy(chUsr);
    tmpl.setChangedOn(tmplRec.getChangedOn());
   }
  }
  return tmpl;
 }
 
 private DocFiTemplAccrPrePayRec buildDocFiTemplateRec(DocFiTemplAccrPrePay tmpl){
  DocFiTemplAccrPrePayRec rec = new DocFiTemplAccrPrePayRec();
  UserRec crUsr = usrDM.getUserRecPvt(tmpl.getCreatedBy());
  rec.setCreatedBy(crUsr);
  rec.setCreateOn(tmpl.getCreateOn());
  if(tmpl.getChangedBy() != null){
   UserRec chUsr = usrDM.getUserRecPvt(tmpl.getChangedBy());
   rec.setChangedBy(chUsr);
   rec.setChangedOn(tmpl.getChangedOn());
  }
  CompanyBasicRec compRec = this.sysBuff.getCompanyById(tmpl.getCompany().getId());
  rec.setCompany(compRec);
  rec.setComplete(tmpl.isComplete());
  rec.setDocHdrText(tmpl.getDocHdrText());
  rec.setDocNumber(tmpl.getDocNumber());
  DocTypeRec docTy =sysBuff.getDocTypeById(tmpl.getDocType().getId());
  rec.setDocType(docTy);
  rec.setDocumentDate(tmpl.getDocumentDate());
  rec.setFisPeriod(tmpl.getFisPeriod());
  rec.setFisYear(tmpl.getFisYear());
  rec.setId(tmpl.getId());
  rec.setNextPostDate(tmpl.getNextPostDate());
  rec.setNumPosted(tmpl.getNumPosted());
  rec.setNumRecur(tmpl.getNumRecur());
  rec.setPartnerRef(tmpl.getPartnerRef());
  rec.setPostingDate(tmpl.getPostingDate());
  rec.setRecurStartPer(tmpl.getRecurStartPer());
  rec.setRecurStartYear(tmpl.getRecurStartYear());
  if(tmpl.getTmplType() == DocFiTemplAccrPrePay.RECURRING){
  UomRec uom = sysBuff.getUomByCode(tmpl.getRecurUom().getUomCode());
  rec.setRecurUom(uom);
  }
  rec.setRevPer(tmpl.getRevPer());
  rec.setRevYer(tmpl.getRevYer());
  rec.setTaxDate(tmpl.getTaxDate());
  rec.setTmplType(tmpl.getTmplType());
  TransactionTypeRec tranTy = sysBuff.getTransactionTypeRecById(tmpl.getTransactionType().getId());
  rec.setTransactionType(tranTy);
  
  return rec;
 }
 
 private DocFiPartial buildDocFiPartial(DocFiPartialRec rec, String page){
  DocFiPartial doc = null;
  boolean newDoc = false;
  boolean changedDoc = false;
  LOGGER.log(INFO, "buildDocFiPartial rec id {0}", rec.getId());
  
  if(rec.getId() == null){
   doc = new DocFiPartial();
   User crUsr = em.find(User.class, rec.getCreateBy().getId(), OPTIMISTIC);
   doc.setCreatedBy(crUsr);
   doc.setCreateOn(rec.getCreateDate());
   em.persist(doc);
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, crUsr, page, 'I');
   aud.setNewValue(doc.getDocHdrText());
   newDoc = true;
  }else{
   doc = em.find(DocFiPartial.class, rec.getId(), OPTIMISTIC);
  }
  
  if(newDoc){
   CompanyBasic comp = em.find(CompanyBasic.class, rec.getCompany().getId(), OPTIMISTIC);
   doc.setCompany(comp);
   doc.setDocNumber(nextCompDocNumber(comp, "FI_DOC_FIN_PART"));
   doc.setDocHdrText(rec.getDocHdrText());
   doc.setDocumentDate(rec.getDocumentDate());
   doc.setFisPeriod(rec.getFisPeriod());
   doc.setFisYear(rec.getFisYear());
   doc.setNotes(rec.getNotes());
   doc.setPartnerRef(rec.getPartnerRef());
   doc.setPostingDate(rec.getPostingDate());
   doc.setTaxDate(rec.getTaxDate());
   DocType ty = em.find(DocType.class, rec.getDocType().getId(), OPTIMISTIC);
   doc.setDocType(ty);
   TransactionType tr = em.find(TransactionType.class, rec.getTransactionType().getId(), OPTIMISTIC);
   doc.setTransactionType(tr);
  }else{
   // Changed ?
   User chUsr = em.find(User.class, rec.getChangeBy().getId(), OPTIMISTIC);
   
   if((rec.getCompany() == null && doc.getCompany() != null) ||
      (rec.getCompany() != null && doc.getCompany() == null) ||
      (rec.getCompany() != null && rec.getCompany().getId() != doc.getCompany().getId())){
    AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_COMP");
    aud.setNewValue(rec.getCompany().getReference());
    aud.setOldValue(doc.getCompany().getNumber());
    CompanyBasic comp = em.find(CompanyBasic.class, rec.getCompany().getId(), OPTIMISTIC);
    doc.setCompany(comp);
    changedDoc = true;
   }
   
   if((rec.getDocHdrText() == null && doc.getDocHdrText() != null) ||
      (rec.getDocHdrText() != null && doc.getDocHdrText() == null) ||
      (rec.getDocHdrText() != null && !rec.getDocHdrText().equals(doc.getDocHdrText()) )){
    AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_HDR_TXT");
    aud.setNewValue(rec.getDocHdrText());
    aud.setOldValue(doc.getDocHdrText());
    doc.setDocHdrText(rec.getDocHdrText());
    changedDoc = true;
   }
   
   if((rec.getDocType() == null && doc.getDocType() != null) ||
      (rec.getDocType() != null && doc.getDocType() == null) ||
      (rec.getDocType() != null && rec.getDocType().getId() != doc.getDocType().getId())){
    AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TY");
    aud.setNewValue(rec.getDocType().getCode());
    aud.setOldValue(doc.getDocType().getCode());
    DocType ty = em.find(DocType.class, rec.getDocType().getId(), OPTIMISTIC);
    doc.setDocType(ty);
    changedDoc = true;
   }
   
  if((rec.getDocumentDate()== null && doc.getDocumentDate() != null) ||
      (rec.getDocumentDate() != null && doc.getDocumentDate() == null) ||
      (rec.getDocumentDate() != null && !rec.getDocumentDate().equals(doc.getDocumentDate()) )){
    AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_DOC_DT");
    aud.setNewValue(rec.getDocumentDate().toString());
    aud.setOldValue(doc.getDocumentDate().toString());
    doc.setDocumentDate(rec.getDocumentDate());
    changedDoc = true;
   }
  
  if(rec.getFisPeriod() != doc.getFisPeriod()){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_FIS_PER");
    aud.setNewValue(String.valueOf(rec.getFisPeriod()));
    aud.setOldValue(String.valueOf(doc.getFisPeriod()));
    doc.setFisPeriod(rec.getFisPeriod());
    changedDoc = true;
  }
  
  if(rec.getFisYear() != doc.getFisYear()){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_FIS_YR");
    aud.setNewValue(String.valueOf(rec.getFisYear()));
    aud.setOldValue(String.valueOf(doc.getFisYear()));
    doc.setFisYear(rec.getFisYear());
    changedDoc = true;
  }
  
  if((rec.getNotes() == null && doc.getNotes() != null) ||
      (rec.getNotes() != null && doc.getNotes() == null) ||
      (rec.getNotes() != null && !rec.getNotes().equals(doc.getNotes()))){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_NOTES");
    aud.setNewValue(rec.getNotes());
    aud.setOldValue(doc.getNotes());
    doc.setNotes(rec.getNotes());
    changedDoc = true;
  }
  
  if((rec.getPartnerRef() == null && doc.getPartnerRef() != null) ||
      (rec.getPartnerRef() != null && doc.getPartnerRef() == null) ||
      (rec.getPartnerRef() != null && !rec.getPartnerRef().equals(doc.getPartnerRef()))){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_PTNR_REF");
    aud.setNewValue(rec.getPartnerRef());
    aud.setOldValue(doc.getPartnerRef());
    doc.setPartnerRef(rec.getPartnerRef());
    changedDoc = true;
  }
  
  if((rec.getPostingDate() == null && doc.getPostingDate() != null) ||
      (rec.getPostingDate() != null && doc.getPostingDate() == null) ||
      (rec.getPostingDate() != null && !rec.getPostingDate().equals(doc.getPostingDate()))){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_PST_DT");
    aud.setNewValue(rec.getPostingDate().toString());
    aud.setOldValue(doc.getPostingDate().toString());
    doc.setPostingDate(rec.getPostingDate());
    changedDoc = true;
  }
  
  if((rec.getTaxDate() == null && doc.getTaxDate() != null) ||
      (rec.getTaxDate() != null && doc.getTaxDate() == null) ||
      (rec.getTaxDate() != null && !rec.getTaxDate().equals(doc.getTaxDate()))){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_PST_DT");
    aud.setNewValue(rec.getTaxDate().toString());
    aud.setOldValue(doc.getTaxDate().toString());
    doc.setTaxDate(rec.getTaxDate());
    changedDoc = true;
  }
  
  if((rec.getTransactionType() == null && doc.getTransactionType() != null) ||
      (rec.getTransactionType() != null && doc.getTransactionType() == null) ||
      (rec.getTransactionType() != null && rec.getTransactionType().getId() != doc.getTransactionType().getId())){
   AuditDocFiPartial aud = this.buildAuditDocFiPartial(doc, chUsr, page, 'U');
    aud.setFieldName("DOC_FI_TRANS_TY");
    aud.setNewValue(rec.getTaxDate().toString());
    aud.setOldValue(doc.getTaxDate().toString());
    doc.setTaxDate(rec.getTaxDate());
    changedDoc = true;
  }
  if(changedDoc){
   doc.setChangedBy(chUsr);
   doc.setChangedOn(rec.getChangeDate());
  }
  
  }
  
  return doc;
 }
 
 private DocFiPartialRec buildDocFiPartialRec(DocFiPartial doc){
  DocFiPartialRec rec = new DocFiPartialRec();
  if(doc.getChangedBy() != null){
   UserRec chUsr = this.usrDM.getUserRecPvt(doc.getChangedBy());
   rec.setChangeBy(chUsr);
   rec.setChangeDate(doc.getChangedOn());
  }
  
  if(doc.getCompany() != null){
   CompanyBasicRec comp = sysBuff.getCompanyById(doc.getCompany().getId());
   rec.setCompany(comp);
  }
  rec.setCreateDate(doc.getCreateOn());
  UserRec crUsr =usrDM.getUserRecPvt(doc.getCreatedBy());
  rec.setCreateBy(crUsr);
  rec.setDocHdrText(doc.getDocHdrText());
  rec.setDocNumber(doc.getDocNumber());
  if(doc.getDocType() != null){
   DocTypeRec dt = sysBuff.getDocTypeById(doc.getDocType().getId());
   rec.setDocType(dt);
  }
  
  rec.setDocumentDate(doc.getDocumentDate());
  rec.setFisPeriod(doc.getFisPeriod());
  rec.setFisYear(doc.getFisYear());
  rec.setId(doc.getId());
  rec.setNotes(doc.getNotes());
  rec.setPartnerRef(doc.getPartnerRef());
  rec.setPostingDate(doc.getPostingDate());
  rec.setTaxDate(doc.getTaxDate());
  if(doc.getTransactionType() != null){
   TransactionTypeRec trTy = sysBuff.getTransactionTypeRecById(doc.getTransactionType().getId());
   rec.setTransactionType(trTy);
  }
  return rec;
 }
 
 private DocLineGl buildDocLineGlRecon(DocLineSubLedgerIF subLedgerLine,
          FiGlAccountComp reconAc, double lineAmount, Fund restrFnd){ 
  DocLineGl line = new DocLineGl();
  
  
  String lineText = null;
  CompanyBasic comp = null;
  User usr = null;; 
  Date entryDt = null;
  PostType postType = null;
  
  if(subLedgerLine.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
   LOGGER.log(INFO, "AR reconciliation line");
   DocLineAr lineAr = (DocLineAr)subLedgerLine;
   
   lineAmount = lineAr.getDocAmount();
   lineText = lineAr.getLineText();
   comp = lineAr.getComp();
   usr = lineAr.getCreateBy();
   entryDt = lineAr.getCreateDate();
   PostType arPostType = lineAr.getPostType();
   
   if(arPostType.isDebit()){
    //AR debit 
    postType = sysBuff.getPostTypeForPCode("Debit");
   }else{
    postType = sysBuff.getPostTypeForPCode("Credit");
   }
   
  } else if(subLedgerLine.getClass().getSimpleName().equalsIgnoreCase("DocLineAP")){
   LOGGER.log(INFO, "AP reconciliation line");
   DocLineAp lineAp = (DocLineAp)subLedgerLine;
   
   lineAmount = lineAp.getDocAmount();
   lineText = lineAp.getLineText();
   comp = lineAp.getComp();
   usr = lineAp.getCreateBy();
   entryDt = lineAp.getCreateDate();
   PostType apPostType = lineAp.getPostType();
   
   if(apPostType.isDebit()){
    //AR debit 
    postType = sysBuff.getPostTypeForPCode("Debit");
   }else{
    postType = sysBuff.getPostTypeForPCode("Credit");
   }
   
  } 
  line.setCreateBy(usr);
  line.setCreateDate(entryDt);
  em.persist(line);
  if(comp != null){
   line.setComp(comp);
  }
  LineTypeRule glLineType= null;
  ListIterator<LineTypeRule> lineTypeLi = this.lineTypes.listIterator();
  boolean glLineTypeFound = false;
  while(lineTypeLi.hasNext() && !glLineTypeFound){
   LineTypeRule lt = lineTypeLi.next();
   if(lt.getLineCode().equalsIgnoreCase("GL")){
    glLineType = lt;
    glLineTypeFound = true;
   }
  }
  line.setLineType(glLineType);
  line.setPostType(postType);
  line.setAutoGenerated(true);
  line.setDocAmount(lineAmount);
  line.setHomeAmount(lineAmount);
  line.setGlAccount(reconAc);
  line.setLineText(lineText);
  if(restrFnd != null){
   line.setRestrictedFund(restrFnd);
  }
  return line;
 }
 
  /**
   * Builds the GL line for a sales invoice.
   * If entity does not exist will be created and registered with Entity Manager 
   * @param rec
   * @param doc
   * @param comp
   * @param usr
   * @param entryDt
   * @return Add
   */
  private DocLineGl buildGlDocLine(SalesPartFiLineRec rec, FiGlAccountComp glAccount, double lineAmount){
   LOGGER.log(INFO,"buildGlDocLine(SalesPartFiLineRec {0}, FiGlAccountComp {1} called",
  new Object[]{rec,glAccount}  );
   LOGGER.log(INFO, "glAccount id {0} ", glAccount.getId());
   
   
   DocLineGl line = new DocLineGl();
   //line.setCreateBy(usr);
   //line.setCreateDate(entryDt);
   em.persist(line);
   //if(comp != null){
  //  line.setComp(comp);
  // }
   
   line.setDocAmount(lineAmount);
   line.setHomeAmount(lineAmount);
   line.setGlAccount(glAccount);
   line.setLineText(rec.getDescription());
   if(rec.getFund() != null){
    Fund fnd = em.find(Fund.class, rec.getFund().getId(), OPTIMISTIC);
    line.setRestrictedFund(fnd);
   }
   if(rec.getCostCent() != null && rec.getCostCent().getId() != null  ){
    CostCentre cc = em.find(CostCentre.class, rec.getCostCent().getId(), OPTIMISTIC);
    line.setCostCentre(cc);
    CostAccountDirect cstAct = em.find(CostAccountDirect.class, rec.getCostAct().getId(), OPTIMISTIC);
    line.setCostAccount(cstAct);
    LOGGER.log(INFO, "DocDM cost cent {0} cost ac {1}", new Object[]{line.getCostCentre(),
     line.getCostAccount()});
   }
   if(rec.getProg() != null && rec.getProg().getId() != null ){
    LOGGER.log(INFO, "rec.getProg() {0}", rec.getProg());
    Programme prog = em.find(Programme.class, rec.getProg().getId(), OPTIMISTIC);
    line.setProgramme(prog);
   }
   
   if(rec.getPartComp() != null){
    SalesPartCompany partComp = em.find(SalesPartCompany.class, rec.getPartComp().getId(), OPTIMISTIC);
    line.setSalesPartCompany(partComp);
   }
   if(rec.getCostCent() != null && rec.getCostCent().getId() != null){
    CostCentre cc = em.find(CostCentre.class, rec.getCostCent().getId(), OPTIMISTIC);
    line.setCostCentre(cc);
   }
   return line;
  }
  
  
  private DocLineGlRec buildGlRecDocLine(DocLineGl line){
   LOGGER.log(INFO, "buildGlRecDocLine called with for line id {0}", line.getId());
   DocLineGlRec ret = new DocLineGlRec();
   ret.setId(line.getId());
   UserRec crUser = usrDM.getUserRecPvt(line.getCreateBy());
   ret.setCreateBy(crUser);
   ret.setCreateDate(line.getCreateDate());
   if(line.getChangeBy() != null){
    UserRec chUser = usrDM.getUserRecPvt(line.getChangeBy());
    ret.setChangeBy(chUser);
    ret.setChangeDate(line.getChangeDate());
   }
   CompanyBasicRec comp = this.sysBuff.getCompanyById(line.getComp().getId());
   ret.setComp(comp);
   ret.setDocAmount(line.getDocAmount());
   FiGlAccountCompRec glAc = this.glActDM.buildFiCompGlAccountRecPvt(line.getGlAccount());
   ret.setGlAccount(glAc);
   ret.setHomeAmount(line.getHomeAmount());
   ret.setLineNum(line.getLineNum());
   ret.setLineText(line.getLineText());
   LineTypeRuleRec lineTypeRec = this.sysBuff.getLineTypeRule(line.getLineType());
   ret.setLineType(lineTypeRec);
   ret.setOrginalAmount(line.getOrginalAmount());
   ret.setPaidAmount(line.getPaidAmount());
   if(line.getPostType() != null){
    PostTypeRec postType = this.sysBuff.getPostTypeForCode(line.getPostType().getPostTypeCode());
    ret.setPostType(postType);
   }
   ret.setReference1(line.getReference1());
   ret.setReference2(line.getReference2());
   if(line.getRestrictedCreditBalance() != null){
    FiPeriodBalanceRec bal = glActDM.getFiPeriodBalanceRec(line.getRestrictedCreditBalance(),
            comp);
    ret.setRestrictedCreditBalance(bal);
   }        
   if(line.getRestrictedDebitBalance() != null){
    FiPeriodBalanceRec bal = glActDM.getFiPeriodBalanceRec(line.getRestrictedDebitBalance(),
            comp);
    ret.setRestrictedDebitBalance(bal);
   }
   if(line.getRestrictedFund() != null){
    FundRec fnd = this.compDM.getRestrictedFundRec(comp, line.getRestrictedFund());
    ret.setRestrictedFund(fnd);
   }
   ret.setRevision(line.getRevision());
   ret.setSortOrder(line.getSortOrder());
   return ret;
  }
  
  
  private DocLineGlRec buildGlDocLineRec(DocLineGl line, UserRec usr, String pg){
   LOGGER.log(INFO, "buildGlDocLine for glLine id {0}", line.getId());
   DocLineGlRec ret = new DocLineGlRec();
   ret.setId(line.getId());
   UserRec crUser = usrDM.getUserRecPvt(line.getCreateBy());
   ret.setCreateBy(crUser);
   ret.setCreateDate(line.getCreateDate());
   
   if(line.getChangeBy() != null){
    UserRec chUser = usrDM.getUserRecPvt(line.getChangeBy());
    ret.setChangeBy(chUser);
    ret.setChangeDate(line.getChangeDate());
   }
   CompanyBasicRec comp = this.sysBuff.getCompanyById(line.getComp().getId());
   ret.setComp(comp);
   if(line.getCostCentre() != null){
    CostCentreRec costCentRec = costCentDM.getCostCentreRec(line.getCostCentre());
    ret.setCostCentre(costCentRec);
   }
   ret.setCurrID(line.getCurrID());
   if(line.getCostAcBalance() != null){
     FiPeriodBalanceRec costBal = glActDM.getFiPeriodBalanceRec(line.getCostAcBalance(), 
             comp) ;
     ret.setCostAcBalance(costBal);
   }
   if(line.getCostAcDebtBalance() != null){
    FiPeriodBalanceRec cosDrtBal = glActDM.getFiPeriodBalanceRec(line.getCostAcDebtBalance(), 
             comp);
    ret.setCostAcDebtBalance(cosDrtBal);
   }
   if(line.getCostCreditBalance() != null){
    FiPeriodBalanceRec cosCrtBal = glActDM.getFiPeriodBalanceRec(line.getCostCreditBalance(), 
             comp);
    ret.setCostCreditBalance(cosCrtBal);
   }
   ret.setDocAmount(line.getDocAmount());
   FiGlAccountCompRec glAc = this.glActDM.buildFiCompGlAccountRecPvt(line.getGlAccount());
   ret.setGlAccount(glAc);
   ret.setHomeAmount(line.getHomeAmount());
   ret.setLineNum(line.getLineNum());
   ret.setLineText(line.getLineText());
   LineTypeRuleRec lineTypeRec = this.sysBuff.getLineTypeRule(line.getLineType());
   ret.setLineType(lineTypeRec);
   ret.setOrginalAmount(line.getOrginalAmount());
   ret.setPaidAmount(line.getPaidAmount());
   if(line.getPostType() != null){
    PostTypeRec postType = this.sysBuff.getPostTypeForCode(line.getPostType().getPostTypeCode());
    ret.setPostType(postType);
   }
   if(line.getProgramme() != null){
    ProgrammeRec prog = this.progDM.getProgrammeRec(line.getProgramme());
    ret.setProgramme(prog);
   }
   if(line.getProjectBalance() != null){
    FiPeriodBalanceRec progBal = glActDM.getFiPeriodBalanceRec(line.getProjectBalance(), 
             comp);
    ret.setProjectBalance(progBal);
   }
   if(line.getProjectCreditBalance() != null){
    FiPeriodBalanceRec progCrBal = glActDM.getFiPeriodBalanceRec(line.getProjectCreditBalance(), 
             comp);
    ret.setProjectCreditBalance(progCrBal);
   }
   if(line.getProjectDebtBalance() != null){
    FiPeriodBalanceRec progDrBal = glActDM.getFiPeriodBalanceRec(line.getProjectDebtBalance(), 
             comp);
    ret.setProjectDebtBalance(progDrBal);
   }
   if(line.getReconcilForArLine() != null){
    DocLineArRec arLine = this.buildDocLineArRec(line.getReconcilForArLine(),usr,pg);
    ret.setReconcilForArLine(arLine);
   }
   ret.setReference1(line.getReference1());
   ret.setReference2(line.getReference2());
   ret.setAutoGenerated(line.isAutoGenerated());
   if(line.getRestrictedCreditBalance() != null){
    FiPeriodBalanceRec bal = glActDM.getFiPeriodBalanceRec(line.getRestrictedCreditBalance(),
            comp);
    ret.setRestrictedCreditBalance(bal);
   }        
   if(line.getRestrictedDebitBalance() != null){
    FiPeriodBalanceRec bal = glActDM.getFiPeriodBalanceRec(line.getRestrictedDebitBalance(),
            comp);
    ret.setRestrictedDebitBalance(bal);
   }
   if(line.getRestrictedFund() != null){
    FundRec fnd = this.compDM.getRestrictedFundRec(comp, line.getRestrictedFund());
    ret.setRestrictedFund(fnd);
   }
   ret.setRevision(line.getRevision());
   if(line.getSalesPart() != null){
    SalesPartCompanyRec salesPartComp = this.salesDM.getSalesPartCompany(line.getSalesPart());
    ret.setSalesPart(salesPartComp);
   }
   ret.setSortOrder(line.getSortOrder());
   
   return ret;
  }
  
  private DocLineGl buildGlDocLine(DocLineGlRec lineRec,DocLineGl line ){
    LOGGER.log(INFO, "buildGlDocLine called");
    LOGGER.log(INFO,"line {0}",line);
    if(line == null){
     line = new DocLineGl();
    }
    if(lineRec.getId() != null && lineRec.getId() > 0){
      line.setId(lineRec.getId());
    }
    LOGGER.log(INFO, "line id {0}", line.getId());
    if(lineRec.getChangeBy() != null){
      User chUsr = em.find(User.class, lineRec.getChangeBy().getId());
      line.setChangeBy(chUsr);
      line.setChangeDate(lineRec.getChangeDate());
    }
    line.setLineNum(lineRec.getLineNum());
    if(lineRec.getClearedByLine() != null){
      //line.setClearedByLineId(buildDocLineBase(lineRec).getClearedByLineId());
    }
    if(lineRec.getLineType() != null){
     LOGGER.log(INFO, "set line type {0}", lineRec.getLineType());
     LineTypeRule lineType = em.find(LineTypeRule.class, lineRec.getLineType().getId(), OPTIMISTIC);
     line.setLineType(lineType);
    }

    if(lineRec.getClearingLineForLines() != null){
      LOGGER.log(INFO, "Need to implement getClearingLineForLines"  );
      throw new BacException("get getClearingLineForLines");
    }

    line.setCurrID(lineRec.getCurrID());
    line.setDocAmount(lineRec.getDocAmount());
    
    line.setHomeAmount(lineRec.getHomeAmount());
    if(lineRec.getLineText() != null){
      line.setLineText(lineRec.getLineText());
    }
    if(lineRec.getPostType().getId() != null){
    try{
      PostType pType = em.find(PostType.class, lineRec.getPostType().getId());
      line.setPostType(pType);
    }catch(IllegalArgumentException ex){
        LOGGER.log(INFO, "Error getting post code id {0}", lineRec.getPostType().getId());
        throw new BacException("Error getting post code");
      }
    }
    if(lineRec.getReference1() != null){
      line.setReference1(lineRec.getReference1());
    }
    if(lineRec.getReference2() != null){
      line.setReference2(lineRec.getReference1());
    }

    if(lineRec.getSortOrder() != null){
     line.setSortOrder(lineRec.getSortOrder());
    }

    if(lineRec.getComp() != null){
     LOGGER.log(INFO, "Set company for line to id {0}", lineRec.getComp().getId());
     CompanyBasic comp = em.find(CompanyBasic.class, lineRec.getComp().getId(), OPTIMISTIC);
     line.setComp(comp);
    }
    LOGGER.log(INFO, "buildGlDocLine restricted fund id: {0}",lineRec.getRestrictedFund());
    if(lineRec.getRestrictedFund() != null && lineRec.getRestrictedFund().getId() > 0){
      Fund fnd = em.find(Fund.class, lineRec.getRestrictedFund().getId());
      line.setRestrictedFund(fnd);
      LOGGER.log(INFO, "Restricted Fund set to {0}", fnd);
    }
    LOGGER.log(INFO, "buildGlDocLine Gl Ac id: {0}",lineRec.getGlAccount());
    if(lineRec.getGlAccount() != null){
     FiGlAccountComp glac = em.find(FiGlAccountComp.class, lineRec.getGlAccount().getId(), OPTIMISTIC);
     line.setGlAccount(glac);
    }
    return line;
  }
  
  private FiGlAccountComp getGlAccount(List<FiGlAccountComp> glAcs, FiGlAccountCompRec glAcRec ){
   LOGGER.log(INFO, "getGlAccount called with accounts list {0} account rec {1}", new Object[]{
    glAcs,glAcRec });
   FiGlAccountComp glAc = null;
   ListIterator<FiGlAccountComp> glacsLi = glAcs.listIterator();
   boolean glAcFound = false;
   while(glacsLi.hasNext() && !glAcFound){
    glAc = glacsLi.next();
    if(glAc.getId() == glAcRec.getId()){
     glAcFound = true;
     
    }
   }
   if(!glAcFound){
    glAc = em.find(FiGlAccountComp.class, glAcRec.getId(), 
           OPTIMISTIC);
    glAcs.add(glAc);
   }
   
   return glAc;
  }
  
  public List<DocLineApRec> getApDocOpenByAcntRef(ApAgePaySel opt){
   TypedQuery q = em.createNamedQuery("docLineApOpenByAcntRef", DocLineAp.class);
   q.setParameter("compId", opt.getComp().getId());
   q.setParameter("refFr", opt.getActRefFr());
   q.setParameter("refTo", opt.getActRefTo());
   List<DocLineAp> rs = q.getResultList();
   if(rs == null){
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp c:rs){
    DocLineApRec rec = this.buildDocLineApRec(c);
    retList.add(rec);
   }
   
   return retList;
  }
  
  public List<DocLineApRec> getApAgedOpenLines(ApAgePaySel agedSel, String mode){
   LOGGER.log(INFO, "getApAgedOpenLines called with {0}", agedSel);
   TypedQuery q;
   if(StringUtils.equals(mode, "comp")){
    q = em.createNamedQuery("docLineApOpen4Comp", DocLineAp.class);
    q.setParameter("compId", agedSel.getComp().getId());
   }else{
    q = em.createNamedQuery("docLineApOpenByAcntRef", DocLineAp.class);
    q.setParameter("compId", agedSel.getComp().getId());
    q.setParameter("refFr", agedSel.getActRefFr());
    q.setParameter("refTo", agedSel.getActRefTo());
   }
   List<DocLineAp> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    LOGGER.log(INFO, "No lines found for query mode {0}",mode);
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp ln:rs){
    DocLineApRec lnRec = this.buildDocLineApRec(ln);
    DocFiRec docFiRec = this.buildDocFiRec(ln.getDocFi());
    lnRec.setDocFi(docFiRec);
    retList.add(lnRec);
   }
   return retList;
  }
  public List<DocLineApRec> getApChequesUnissued(String processCode, CompanyBasicRec comp){
   LOGGER.log(INFO, "DocDM getApCheqsUnissued called with processCode {0} comp {1}", 
     new Object[]{processCode,comp.getReference()});
   
   PaymentTypeRec pt = sysBuff.getPaymentTypeByMeduim(processCode, "AP",false, comp);
   LOGGER.log(INFO, "pt id from sys buff {0}", pt.getId());
   List<DocLineApRec> retList = new ArrayList<>();
   Query q = em.createNamedQuery("docLineApChqUnIssued");
   q.setParameter("procCode", processCode);
   q.setParameter("compId", comp.getId());
   q.setParameter("payTypeId", pt.getId());
   
   List rs = q.getResultList();
   for(Object o:rs){
    DocLineApRec lnRec = this.buildDocLineApRec((DocLineAp)o);
    retList.add(lnRec);
   }
  
   LOGGER.log(INFO,"lines returned {0}",retList);
   return retList;
  }
  
  public DocLineApRec getApClearedByLine(DocLineApRec apLnRec){
   LOGGER.log(INFO, "getApClearedByLine called {0}", apLnRec);
   if(apLnRec.getClearedByLine() != null){
    return apLnRec;
   }
   DocLineAp apLn = em.find(DocLineAp.class, apLnRec.getId());
   LOGGER.log(INFO, "Cleared by line {0}", apLn.getClearedByLine());
   if(apLn.getClearedByLine() != null){
    DocLineApRec clrdLineRec = this.buildDocLineApRec((DocLineAp)apLn.getClearedByLine());
    apLnRec.setClearedByLine(clrdLineRec);
   }
   return apLnRec;
  }
  public DocLineApRec getApClearedLinesForLine(DocLineApRec apLn){
   LOGGER.log(INFO, "getApClearedLinesForLine called for line id {0}", apLn.getId()); 
   Query q = em.createNamedQuery("docLineApPaidLines");
   q.setParameter("compId", apLn.getComp().getId());
   q.setParameter("paymntLnId", apLn.getId());
   
   List rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    LOGGER.log(INFO, "No paid lines found for payment line id {0}", apLn.getId());
    return null;
   }
   List<DocLineBaseRec> pdLinesList = new ArrayList<>();
   for(Object o:rs){
    DocLineApRec apLnRec = this.buildDocLineApRec((DocLineAp)o);
    pdLinesList.add(apLnRec);
   }
   LOGGER.log(INFO, "paid lines found {0}", pdLinesList);
   apLn.setClearingLineForLines(pdLinesList);
   
   return apLn;
  }
  
  
  public List<DocLineApRec> getApDayBook(ApLineSel selOpts){
   LOGGER.log(INFO, "DocDM.getApDayBook called with {0}", selOpts);
   
   int queryCat = 0;
   
   if(selOpts.getDocDateFr() == null && selOpts.getDocTy() == null && selOpts.getFiscPerFr() == null &&
     selOpts.getFiscYearFr() == null && selOpts.getPostDateFr() == null){
    queryCat = 1; 
   }
   
   List<PostTypeRec> postTypes = sysBuff.getPostCodesForLedger("AP");
   if(postTypes == null || postTypes.isEmpty()){
    LOGGER.log(INFO, "No AP post types found {0}", postTypes);
    return null;
   }
   
   LOGGER.log(INFO, "postTypes {0}",postTypes); 
   List<Long> pstTypeIds = new ArrayList<>();
   for(PostTypeRec pt:postTypes){
    LOGGER.log(INFO, "Process Code {0}", pt.getProcCode());
    if(StringUtils.startsWith(pt.getProcCode(), "AP_INV") || StringUtils.startsWith(pt.getProcCode(), "AP_CRN")){
     pstTypeIds.add(pt.getId());
    }
   }
   
   LOGGER.log(INFO, "pstTypeIds {0}", pstTypeIds);
   if(pstTypeIds.isEmpty()){
    return null;
   }
   TypedQuery q = null;
   switch(queryCat){
    case 1:
      q = em.createNamedQuery("apDayBk1", DocLineAp.class);
      q.setParameter("compId", selOpts.getComp().getId());
      q.setParameter("pstTypes",pstTypeIds);
     break;
   }
   
   if(q ==  null){
    LOGGER.log(INFO, "Invalid options");
    return null;
   }
   
   List<DocLineAp> rs = q.getResultList();
   LOGGER.log(INFO, "returned from DB {0}", rs);
   if(rs == null){
    return null;
   }
   
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp l:rs){
    DocLineApRec lnRec = buildDocLineApRec(l);
    DocFiRec docFi = buildDocFiRec(l.getDocFi());
    lnRec.setDocFi(docFi);
    lnRec.setDocHeaderBase(docFi);
    LOGGER.log(INFO, "Doc num {0}", lnRec.getDocFi().getDocNumber());
    retList.add(lnRec);
   }
   
   
   return retList;
  }
  
  public List<DocLineApRec> getApTransForPayRun(ApLineSel selOpts){
   LOGGER.log(INFO, "called with getApTransForPayRun {0}", selOpts);
   
   int queryCat = 0;
   if(selOpts.getDueDateFr() == null && selOpts.getDueDateTo() == null){
    queryCat = 1;
    // both due from and due to null
   }else {
    queryCat = 2;
   }
   List<Long> payTyIds = new ArrayList<>();
   for(PaymentTypeRec p:selOpts.getPaymentTypeList()){
    payTyIds.add(p.getId());
    LOGGER.log(INFO, "payment type id {0}", p.getId());
   }
   
   LOGGER.log(INFO, "queryCat {0}", queryCat);
   TypedQuery q = null;
   switch(queryCat){
    case 1:
     q = em.createNamedQuery("apSelOptTrans90", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("payTypeIds", payTyIds);
     break;
    case 2:
     q = em.createNamedQuery("apSelOptTrans90", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("payTypeIds", payTyIds);
     q.setParameter("dueDateFr", selOpts.getDueDateFr());
     q.setParameter("dueDateTo", selOpts.getDueDateTo());
     break;
   }
   if(q == null){
    return null;
   }
   LOGGER.log(INFO, "Pay type Ids {0} result set {1}", new Object[]{payTyIds,q.getResultList()});
   List<DocLineAp> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp l:rs){
    DocLineApRec rec = buildDocLineApRec(l);
    retList.add(rec);
   }
   
   return retList;
  }
  
  public List<DocLineApRec> getApTransForSel(ApLineSel selOpts){
   LOGGER.log(INFO, "DocDM.getApTransForSel called with {0}", selOpts);
   
   int queryCat = 0;
   LOGGER.log(INFO, "Selection options fis yr {0} fs per {1} doc date {2} doc type {3} post date {4} pay status {5}", 
     new Object[]{selOpts.getFiscYearFr(),selOpts.getFiscPerFr(),selOpts.getDocDateFr(),selOpts.getDocTy(),
     selOpts.getPostDateFr(), selOpts.getPayStatus()
     });
   if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() == null
     && selOpts.getApAcnt() != null){
   // && selOpts.getPayStatus() == ApLineSel.STATUS_ALL){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 1;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 30;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 59;
      break;
    }
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() == null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 2;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 31;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 60;
      break;
    }
    
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 3;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 32;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 61;
      break;
    }
     
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() == null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 4;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 33;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 62;
      break;
    }
    //queryCat = 4; 
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() != null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 5;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 34;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 63;
      break;
    }
   // queryCat = 5; 
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() == null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 6;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 35;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 64;
      break;
    }
    //queryCat = 6;
  }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
    && selOpts.getDocDateFr() == null && selOpts.getDocTypeList() == null 
    && selOpts.getPostDateFr() == null 
    && selOpts.getApAcnt() != null){
   switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 7;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 36;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 65;
      break;
    }
    //queryCat = 7;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 8;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 37;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 66;
      break;
    }
    //queryCat = 8;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null 
     && selOpts.getDocTypeList() == null 
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 9;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 38;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 67;
      break;
    }
    //queryCat = 9;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 10;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 39;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 68;
      break;
    }
    //queryCat = 10;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 11;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 40;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 69;
      break;
    }
    //queryCat = 11;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 12;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 41;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 70;
      break;
    }
    //queryCat = 12;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 13;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 42;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 71;
      break;
    }
    //queryCat = 13;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 14;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 43;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 72;
      break;
    }
    //queryCat = 14;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 15;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 44;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 73;
      break;
    }
    //queryCat = 15;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 16;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 45;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 74;
      break;
    }
    //queryCat = 16;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 17;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 46;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 75;
      break;
    }
   // queryCat = 17;
   }else if(selOpts.getFiscYearFr() != null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() != null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 18;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 47;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 76;
      break;
    }
   // queryCat = 18;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() == null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 19;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 48;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 77;
      break;
    }
    //queryCat = 19;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 20;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 49;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 78;
      break;
    }
    //queryCat = 20;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 21;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 50;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 79;
      break;
    }
   // queryCat = 21;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 22;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 51;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 80;
      break;
    }
    //queryCat = 22;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() == null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 23;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 52;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 81;
      break;
    }
    //queryCat = 23;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() == null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 24;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 53;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 82;
      break;
    }
    //queryCat = 24;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() != null  
     && selOpts.getDocDateFr() != null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getPostDateFr() != null
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 25;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 54;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 83;
      break;
    }
   // queryCat = 25;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null 
     && selOpts.getDocTypeList() == null
     && selOpts.getPostDateFr() != null 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 26;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 55;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 84;
      break;
    }
    //queryCat = 26;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() != null 
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 27;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 56;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 85;
      break;
    }
   // queryCat = 27;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getPostDateFr() != null
     && selOpts.getDocDateFr() != null
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() )
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 28;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 57;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 86;
      break;
    }
    //queryCat = 28;
   }else if(selOpts.getFiscYearFr() == null && selOpts.getFiscPerFr() == null  
     && selOpts.getDocDateFr() == null 
     && selOpts.getPostDateFr() != null
     && ( selOpts.getDocTypeList() != null  && !selOpts.getDocTypeList().isEmpty() ) 
     && selOpts.getApAcnt() != null){
    switch (selOpts.getPayStatus()){
     case ApLineSel.STATUS_ALL:
      queryCat = 29;
      break;
     case ApLineSel.STATUS_PAID:
      queryCat = 58;
      break;
     case ApLineSel.STATUS_OS:
      queryCat = 87;
      break;
    }
   
   } else if(selOpts.getApAcnt() == null 
     && selOpts.getDueDateFr() == null
     && selOpts.getPayStatus() == ApLineSel.STATUS_OS){
    LOGGER.log(INFO, "Run query for all due items");
    queryCat = 88;
   }
   
   
   LOGGER.log(INFO, "Query Category {0}", queryCat);
   TypedQuery q = null;
   switch(queryCat){
    case 1:
     q = em.createNamedQuery("apSelOptTrans1", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     break;
    case 2:
     q = em.createNamedQuery("apSelOptTrans2", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     break;
    case 3:
     q = em.createNamedQuery("apSelOptTrans3", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 4:
     q = em.createNamedQuery("apSelOptTrans4", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 5:
     q = em.createNamedQuery("apSelOptTrans5", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 6:
     List<Long> docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans6", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docTy", docTyIds);
     break;
    case 7:
     q = em.createNamedQuery("apSelOptTrans7", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 8:
     q = em.createNamedQuery("apSelOptTrans8", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 9:
     q = em.createNamedQuery("apSelOptTrans9", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 10:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans10", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 11:
     q = em.createNamedQuery("apSelOptTrans11", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 12:
     q = em.createNamedQuery("apSelOptTrans12", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 13:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans13", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 14:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans14", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 15:
     q = em.createNamedQuery("apSelOptTrans15", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 16:
     q = em.createNamedQuery("apSelOptTrans16", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 17:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans17", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 18:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans18", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 19:
     q = em.createNamedQuery("apSelOptTrans19", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 20:
     q = em.createNamedQuery("apSelOptTrans20", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 21:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans21", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 22:
     q = em.createNamedQuery("apSelOptTrans22", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 23:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans23", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 24:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans24", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 25:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans25", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 26:
     q = em.createNamedQuery("apSelOptTrans26", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 27:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans27", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 28:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans28", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 29:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans29", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    
    // paid 
    case 30:
     q = em.createNamedQuery("apSelOptTrans30", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     break;
    case 31:
     q = em.createNamedQuery("apSelOptTrans31", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     break;
    case 32:
     q = em.createNamedQuery("apSelOptTrans32", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 33:
     q = em.createNamedQuery("apSelOptTrans33", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 34:
     q = em.createNamedQuery("apSelOptTrans34", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 35:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans35", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docTy", docTyIds);
     break;
    case 36:
     q = em.createNamedQuery("apSelOptTrans36", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 37:
     q = em.createNamedQuery("apSelOptTrans37", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 38:
     q = em.createNamedQuery("apSelOptTrans38", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 39:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans39", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 40:
     q = em.createNamedQuery("apSelOptTrans40", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 41:
     q = em.createNamedQuery("apSelOptTrans41", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 42:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans42", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 43:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans43", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 44:
     q = em.createNamedQuery("apSelOptTrans44", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 45:
     q = em.createNamedQuery("apSelOptTrans45", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 46:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans46", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 47:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans47", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 48:
     q = em.createNamedQuery("apSelOptTrans48", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 49:
     q = em.createNamedQuery("apSelOptTrans49", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 50:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans50", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 51:
     q = em.createNamedQuery("apSelOptTrans51", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 52:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans52", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 53:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans53", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 54:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans54", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 55:
     q = em.createNamedQuery("apSelOptTrans55", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 56:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans56", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 57:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans57", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 58:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans58", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
     
     //Outstanding
     case 59:
     q = em.createNamedQuery("apSelOptTrans59", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     break;
    case 60:
     q = em.createNamedQuery("apSelOptTrans60", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     break;
    case 61:
     q = em.createNamedQuery("apSelOptTrans61", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 62:
     q = em.createNamedQuery("apSelOptTrans62", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 63:
     q = em.createNamedQuery("apSelOptTrans63", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 64:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans64", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docTy", docTyIds);
     break;
    case 65:
     q = em.createNamedQuery("apSelOptTrans65", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     break;
    case 66:
     q = em.createNamedQuery("apSelOptTrans66", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 67:
     q = em.createNamedQuery("apSelOptTrans67", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("pstDateFr", selOpts.getPostDateFr());
     q.setParameter("pstDateTo", selOpts.getPostDateTo());
     break;
    case 68:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans68", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 69:
     q = em.createNamedQuery("apSelOptTrans69", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 70:
     q = em.createNamedQuery("apSelOptTrans70", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 71:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans71", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 72:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans72", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 73:
     q = em.createNamedQuery("apSelOptTrans173", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 74:
     q = em.createNamedQuery("apSelOptTrans74", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 75:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans75", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 76:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans76", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsYrfr", Integer.parseInt(selOpts.getFiscYearFr()));
     q.setParameter("fsYrto", Integer.parseInt(selOpts.getFiscYearTo()));
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 77:
     q = em.createNamedQuery("apSelOptTrans77", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     break;
    case 78:
     q = em.createNamedQuery("apSelOptTrans78", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 79:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans79", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docTy", docTyIds);
     break;
    case 80:
     q = em.createNamedQuery("apSelOptTrans80", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 81:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans81", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 82:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans82", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 83:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans83", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("fsPerfr", Integer.parseInt(selOpts.getFiscPerFr()));
     q.setParameter("fsPerto", Integer.parseInt(selOpts.getFiscPerTo()));
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 84:
     q = em.createNamedQuery("apSelOptTrans84", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     break;
    case 85:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans85", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 86:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans86", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("docDateFr", selOpts.getDocDateFr());
     q.setParameter("docDateTo", selOpts.getDocDateTo());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 87:
     docTyIds = new ArrayList<>();
     for(DocTypeRec dt:selOpts.getDocTypeList()){
      docTyIds.add(dt.getId());
     }
     q = em.createNamedQuery("apSelOptTrans87", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("apAcntId", selOpts.getApAcnt().getId());
     q.setParameter("postDateFr", selOpts.getPostDateFr());
     q.setParameter("postDateTo", selOpts.getPostDateTo());
     q.setParameter("docTy", docTyIds);
     break;
    case 88:
     q = em.createNamedQuery("apSelOptTrans88", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     break;
    case 89:
     q = em.createNamedQuery("apSelOptTrans88", DocLineAp.class);
     q.setParameter("compId", selOpts.getComp().getId());
     q.setParameter("dueDateFr", selOpts.getDueDateFr());
     q.setParameter("dueDateTo", selOpts.getDueDateTo());
   }
   
   
   
   if(q ==  null){
    LOGGER.log(INFO, "Invalid options");
    return null;
   }
   
   List<DocLineAp> rs = q.getResultList();
   LOGGER.log(INFO, "returned from DB {0}", rs);
   if(rs == null){
    return null;
   }
   
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp l:rs){
    LOGGER.log(INFO, "DOCDM - getDocFi {0}", l.getDocFi());
    LOGGER.log(INFO, "DOCDM - getDocFi {0}", l.getDocBase());
    DocFi docFid = l.getDocFi();
    if(docFid == null){
     docFid = (DocFi)l.getDocBase();
    }
    LOGGER.log(INFO, "docFid {0}", docFid);
    //LOGGER.log(INFO, "getDocFi doc type {0}", docFid.getDocType());
    DocLineApRec lnRec = buildDocLineApRec(l);
    
    DocFiRec docFi = buildDocFiRec(docFid);
    lnRec.setDocFi(docFi);
    lnRec.setDocHeaderBase(docFi);
    //LOGGER.log(INFO, "Doc num {0}", lnRec.getDocFi().getDocNumber());
    retList.add(lnRec);
   }
   
   
   return retList;
  }
  
  
  public List<DocLineApRec> getApLinesForAcnt(CompanyBasicRec comp, ApAccountRec acnt){
   LOGGER.log(INFO, "DocDM.getApLinesForAcnt called with comp id {0} account id {1}",
     new Object[]{comp.getId(),acnt.getId()});
   TypedQuery q = em.createNamedQuery("docLineApByAcnt", DocLineAp.class);
   q.setParameter("compId", comp.getId());
   q.setParameter("acntId", acnt.getId());
   List<DocLineAp> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp curr:rs){
    DocLineApRec currRec = this.buildDocLineApRec(curr);
    DocFiRec docRec = buildDocFiRec(curr.getDocFi());
    currRec.setDocFi(docRec);
    retList.add(currRec);
   }
   return retList;
  }
  public List<DocLineApRec> getApLinesForComp(CompanyBasicRec comp){
   LOGGER.log(INFO, "DocDM.getApLinesForComp called with comp id {0}", comp.getId());
   TypedQuery q = em.createNamedQuery("docLineApByComp", DocLineAp.class);
   q.setParameter("compId", comp.getId());
   List<DocLineAp> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp curr:rs){
    DocLineApRec currRec = buildDocLineApRec(curr);
    DocFiRec docRec = buildDocFiRec(curr.getDocFi());
    currRec.setDocFi(docRec);
    
    retList.add(currRec);
   }
   return retList;
  }
  
  public List<DocLineApRec> getApOpenLinesForAcnt(ApAccountRec acnt){
   LOGGER.log(INFO, "getApOpenLinesForAcnt called for acnt id {0}", acnt.getId());
   TypedQuery q = em.createNamedQuery("docLineApOpenByAcnt",DocLineAp.class);
   q.setParameter("acntId", acnt.getId());
   List<DocLineAp> rs = q.getResultList();
   if(rs == null || rs.isEmpty()){
    LOGGER.log(INFO, "No open items for vendor");
    return null;
   }
   List<DocLineApRec> retList = new ArrayList<>();
   for(DocLineAp o:rs){
    LOGGER.log(INFO, "line returned {0}", o.getClass().getSimpleName());
    DocFi docFid = o.getDocFi();
    if(docFid == null){
     docFid = (DocFi)o.getDocBase();
    }
    DocLineApRec rec = buildDocLineApRec(o);
    DocFiRec docFi = buildDocFiRec(docFid);
    rec.setDocFi(docFi);
    rec.setDocHeaderBase(docFi);
    retList.add(rec);
   }
   
   return retList;
  }
  
  private List<RestrictedFundBal> getArLineFndBal(DocLineAr ln,List<RestrictedFundBal> fndSplit,
          List<RestrictedFundArDocBal> docFndBal,List<DocLineAr> pdDocs){
   LOGGER.log(INFO, "getArLineFndBal called");
   if(fndSplit == null){
    fndSplit = new ArrayList<>();
   } 
   if(docFndBal == null){
    docFndBal = new ArrayList<>();
   }
   if(pdDocs == null){
    pdDocs = new ArrayList<>();
   }
    
    List<DocLineBase> docLinesbase = ln.getDocHeaderBase().getDocLines();
    // get AR line
    ListIterator<DocLineBase> docLinesLi = docLinesbase.listIterator();
    boolean lnFound = false;
    while(docLinesLi.hasNext() && !lnFound){
     DocLineBase lineBase = docLinesLi.next();
     LOGGER.log(INFO, "lineType {0}", lineBase.getClass().getSimpleName());
     if(lineBase.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
      lnFound = true;
      DocLineAr lineAR = (DocLineAr)lineBase;
      pdDocs.add(lineAR);
      List<DocLineGl> recLines = lineAR.getReconiliationLines();
      for(DocLineGl recLn :recLines){
       LOGGER.log(INFO, "Fund", recLn.getRestrictedFund());
        RestrictedFundArDocBal arDocFnd = new RestrictedFundArDocBal();
        arDocFnd.setArDocId(lineAR.getId());
        arDocFnd.setFundId(recLn.getId());
        arDocFnd.setAmount(recLn.getDocAmount());
        docFndBal.add(arDocFnd);
      }
     }
    }
    if(!lnFound){
     throw new BacException("No ar line in doc"); 
    }
    
    for(RestrictedFundArDocBal arDocFnd : docFndBal){
     // loop over rec doc fund summary to build single ar doc summary
     
     if(fndSplit == null || fndSplit.isEmpty()){
      // fnd split so add  new rec
      fndSplit = new ArrayList<>();
      RestrictedFundBal bal = new RestrictedFundBal();
      if(arDocFnd.getFundId() != 0){
       Fund fund = em.find(Fund.class, arDocFnd.getFundId(), OPTIMISTIC);
       bal.setFund(fund);
      }
      bal.setId(fndSplit.size());
      bal.setAmount(arDocFnd.getAmount());
      fndSplit.add(bal);
     }else{
      // need to find split
      ListIterator<RestrictedFundBal> fndLi = fndSplit.listIterator();
      boolean fndSplitFound = false;
      long fndId = arDocFnd.getFundId();
      while(fndLi.hasNext() && !fndSplitFound){
       RestrictedFundBal arFndBal = fndLi.next();
       LOGGER.log(INFO, "fndId {0}", fndId);
       LOGGER.log(INFO, "arFndBal.getFund() {0}", arFndBal.getFund());
       if(fndId == 0 && arFndBal.getFund() == null){
        fndSplitFound = true;
        double amnt = arFndBal.getAmount();
        amnt = amnt + arDocFnd.getAmount();
        arFndBal.setAmount(amnt);
        fndLi.set(arFndBal);
       }else{
        // there is a fund
        
        //RestrictedFund fund = em.find(Fund.class, fndId, OPTIMISTIC);
        if(arFndBal.getFund() != null && arFndBal.getFund().getId() == fndId){
         // found find
         fndSplitFound = true;
         double amnt = arFndBal.getAmount();
         amnt = amnt + arDocFnd.getAmount();
         arFndBal.setAmount(amnt);
         fndLi.set(arFndBal);
        }
        
       }
      }
      if(!fndSplitFound){
       RestrictedFundBal arFundBal = new RestrictedFundBal();
       if(arDocFnd.getFundId() != 0){
        Fund fnd = em.find(Fund.class, arDocFnd.getFundId(), OPTIMISTIC);
        arFundBal.setFund(fnd);
       }
       arFundBal.setId(fndSplit.size());
       arFundBal.setAmount(arDocFnd.getAmount());
       fndSplit.add(arFundBal);
       
      }
      
     }
    }
   return fndSplit;
  }
  
  private DocLineAr setArAccountBalance(DocLineAr arLine,DocFi docHdr,String updateType, User usr, String page){
   LOGGER.log(INFO, "setArAccountBalance called with arLine {0}", arLine);
   ArAccount arAccount = arLine.getArAccount();
   double arAcBal = arAccount.getAccountBalance();
   LOGGER.log(INFO, "starting ac balance {0}", arAcBal);
   arAcBal = arAcBal + arLine.getDocAmount();
   arAccount.setAccountBalance(arAcBal);
  List<FiArPeriodBalance> arAcBalList = arAccount.getArPeriodBalances();
  LOGGER.log(INFO, "araccount {1} period bals {2}", new Object[]{arAccount,arAcBalList});
  if(arAcBalList == null){
   // no balances so need to create 
   arAccountDM.addAnnualAcBalances(arAccount,arAcBalList,  docHdr.getFisYear());
   arAcBalList = arAccount.getArPeriodBalances();
  }
  ListIterator<FiArPeriodBalance> arAcBalListLi = arAcBalList.listIterator();
  boolean perBalFound = false;
  while(arAcBalListLi.hasNext() && !perBalFound){
   FiArPeriodBalance arPeriodBalance = arAcBalListLi.next();
   if(arPeriodBalance.getBalYear() == docHdr.getFisYear() && arPeriodBalance.getBalPeriod() == docHdr.getFisPeriod()){
    perBalFound = true;
    arPeriodBalance = arAccountDM.setArPeriodBalance(arPeriodBalance, arLine, updateType,  page);
    arAcBalListLi.set(arPeriodBalance);
   }
  }
  arAccount.setArPeriodBalances(arAcBalList);
  if(!perBalFound){
   // need to create balances for the doc year
   arAccountDM.addAnnualAcBalances(arAccount,arAcBalList,  docHdr.getFisYear());
   arAcBalListLi = arAcBalList.listIterator();
   while(arAcBalListLi.hasNext() && !perBalFound){
   FiArPeriodBalance arPeriodBalance = arAcBalListLi.next();
   if(arPeriodBalance.getBalYear() == docHdr.getFisYear() && 
           arPeriodBalance.getBalPeriod() == docHdr.getFisPeriod()){
    perBalFound = true;
    arPeriodBalance = arAccountDM.setArPeriodBalance(arPeriodBalance, arLine, updateType, page);
    arAcBalListLi.set(arPeriodBalance);
   }
   
   }
  }
  LOGGER.log(INFO, "final line balance {0}", arLine.getArAccount().getAccountBalance());
   return arLine;
  }
  private ArrayList<DocGlVatCodeSummary> setVatCodeSummary(ArrayList<DocGlVatCodeSummary> vatlist, 
          DocLineGl glLine, VatCodeCompanyRec vatCode, CompanyBasicRec comp){
   LOGGER.log(INFO, "setVatCodeSummary called with VAT Summary is {0} vat code {1}", new Object[]{
    vatlist,vatCode });
   LOGGER.log(INFO, "glLine.getRestrictedFund() is {0}",glLine.getRestrictedFund());
   LOGGER.log(INFO,"glLine.postType {0}",glLine.getPostType().getPostTypeCode());
   PostTypeRec postType = this.sysBuff.getPostTypeForCode(glLine.getPostType().getPostTypeCode());
   FundRec fnd = null;
   if(glLine.getRestrictedFund() != null){
    fnd = this.sysBuff.getRestrictedFundById(glLine.getRestrictedFund().getId(),comp);
   }
   ListIterator<DocGlVatCodeSummary> li = vatlist.listIterator();
   boolean found = false;
   while(li.hasNext() && !found){
    DocGlVatCodeSummary vatSum = li.next();
    LOGGER.log(INFO, "vatSum.getFund() {0}",vatSum.getFund());
    LOGGER.log(INFO,"vatSum.getVATcode {0}",vatSum.getVatCode());
    if(vatSum.getGlAccount() == glLine.getGlAccount() && vatSum.getVatCode() == vatCode
              ){
     if(vatSum.getFund() != null && glLine.getRestrictedFund() != null 
             && vatSum.getFund().getId() == glLine.getRestrictedFund().getId()){
      double goodsAmnt = vatSum.getGoods() + glLine.getDocAmount();
      double vatAmnt = goodsAmnt * vatCode.getVatCode().getRate();
      vatSum.setGoods(goodsAmnt);
      vatSum.setVatAmount(vatAmnt);
      li.set(vatSum);
     }else if(vatSum.getFund() != null && glLine.getRestrictedFund() != null){
      double goodsAmnt = vatSum.getGoods() + glLine.getDocAmount();
      double vatAmnt = goodsAmnt * vatCode.getVatCode().getRate();
      vatSum.setGoods(goodsAmnt);
      vatSum.setVatAmount(vatAmnt);
      li.set(vatSum);
     }
     
    }
    }
   LOGGER.log(INFO, "found {0}", found);
   LOGGER.log(INFO,"vatlist empty {0}",vatlist.isEmpty());
   LOGGER.log(INFO,"vatlist size {0}",vatlist.size());
   
   if(!found){
    LOGGER.log(INFO,"Add valid to vatList"); 
    
    DocGlVatCodeSummary vatSum = new DocGlVatCodeSummary();
    if(fnd != null){
     vatSum.setFund(fnd);
    }
    vatSum.setGlAccount(glLine.getGlAccount());
    vatSum.setGoods(glLine.getDocAmount());
    vatSum.setVatCode(vatCode);
    vatSum.setPostType(postType);
    
    vatlist.add(vatSum);
   }
   
   LOGGER.log(INFO, "end VAT Summary is {0}", vatlist);
   ListIterator<DocGlVatCodeSummary> vatListLi = vatlist.listIterator();
   while(vatListLi.hasNext()){
    LOGGER.log(INFO, "vat code {0}", vatListLi.next().getVatCode());
   }
   return vatlist;
   
  }
  
  /**
   * Adds VAT lines to the document according to the VAT settings applicable to the company
   * @param doclines
   * @param vatSplit
   * @param comp
   * @param docHdr
   * @param glDrPostType
   * @param glCrPostType 
   */
  private DocFi setVatLines(List<DocLineBase> doclines, ArrayList<DocGlVatCodeSummary> vatSplit, 
          CompanyBasicRec comp, DocFi docHdr,LineTypeRule glLineType,PostTypeRec glDrPostType, PostTypeRec glCrPostType, UserRec usr,
          Date crDate, DocLineArRec arLine){
   LOGGER.log(INFO, "setVatLines called with docLines {0} valSplit {1} comp {2}, dochdr {3}", 
           new Object[]{doclines,vatSplit,vatSplit,docHdr});
   Date taxDate = docHdr.getTaxDate();
   VatRegistrationRec vatReg = sysBuff.getVatRegForCompany(comp);
   VatRegSchemeRec vatRegScheme = null;
   boolean flatRate = false;
   boolean cashAccounting= false;
   boolean annualAccounting = false;
   boolean stdVat = false;
   boolean found = false;
   FiGlAccountCompRec vatGlAc = null;
   FiGlAccountCompRec vatWoGlAc = null;
   FiGlAccountCompRec vatProvnGlAc = null;
   VatSchemeRec vatScheme = null;
   VatCodeCompanyRec vatCodeComp = null;
   List<VatRegSchemeRec> regSchemes = vatReg.getRegSchemes();
   if(regSchemes == null){
    throw new BacException("No Vat scheme","DocVat01");
   }
   ListIterator<VatRegSchemeRec> regSchemesLi = regSchemes.listIterator();
   while(regSchemesLi.hasNext() && !found){
    VatRegSchemeRec vatRegSchemeRec = regSchemesLi.next();
    if(vatRegSchemeRec.getValidFrom().before(taxDate) && vatRegSchemeRec.getValidTo().after(taxDate)){
     vatRegScheme = vatRegSchemeRec;
     vatScheme = vatRegScheme.getVatScheme();
     //List<VatSchemeRec> vatSchemes = vatRegScheme.getVatSchemes();
     LOGGER.log(INFO, "vatScheme {0}", vatScheme);
     //logger.log(INFO, "vatSchemes {0}", vatSchemes);
     flatRate = vatScheme.isFlatRate();
     cashAccounting = vatScheme.isCashAccounting();
     annualAccounting = vatScheme.isAnnualAccounting();
     if(!flatRate &&!cashAccounting && ! annualAccounting ){
      stdVat = true;
     }
     found = true;
    }
   }
   if(vatRegScheme == null){
    throw new BacException("No valid vat scheme for tax date","DocVat02");
   }
   
   ListIterator<DocGlVatCodeSummary> vatSplitLi = vatSplit.listIterator();
   while(vatSplitLi.hasNext()){
    // process each vat entry
    DocGlVatCodeSummary vatSumm = vatSplitLi.next();
    VatCodeCompanyRec vatCode = vatSumm.getVatCode();
    
    List<VatCodeCompanyRec> vatCodeCompList = this.sysBuff.getCompVatCode(comp, vatCode);
    LOGGER.log(INFO, "vatCode {0} vatCodeCompList {1}", new Object[]{vatCode,vatCodeCompList});
    ListIterator<VatCodeCompanyRec>vatCodeCompListLi = vatCodeCompList.listIterator();
    boolean vatCodeCompFound = false;
    long lineNum = doclines.size();
    
    while(vatCodeCompListLi.hasNext() && !vatCodeCompFound){
     VatCodeCompanyRec vatCodeCompRec = vatCodeCompListLi.next();
     LOGGER.log(INFO, "vatCodeCompRec.getCompany() {0} docHdr.getCompany() {1} ", 
             new Object[]{vatCodeCompRec.getCompany(),docHdr.getCompany(),docHdr.getCompany()});
     if(vatCodeCompRec.getCompany().getId() == docHdr.getCompany().getId()){
      vatCodeComp = vatCodeCompRec;
      vatCodeCompFound = true;
     }
    }
    lineNum++;
    LOGGER.log(INFO, "Line num before add VAT lines {0}", lineNum);
    double vatAmount = vatSumm.getGoods() * vatCode.getVatCode().getRate();
    vatSumm.setVatAmount(vatAmount);
    vatSplitLi.set(vatSumm);
    LOGGER.log(INFO, "vatSumm vat code {0} GL ac {1}  goods:{3} vat:{4}", new Object[]{
     vatSumm.getVatCode().getVatCode().getCode(),vatSumm.getGlAccount().getCoaAccount().getRef(), 
     
     vatSumm.getGoods(), vatSumm.getVatAmount()});
    if(stdVat){
     vatGlAc = vatCodeComp.getRateGlAccount();
     DocLineGlRec docLine = new DocLineGlRec();
     docLine.setComp(comp);
     docLine.setCreateBy(usr);
     docLine.setCreateDate(crDate);
     docLine.setGlAccount(vatGlAc);
     docLine.setHomeAmount(vatAmount);
     docLine.setDocAmount(vatAmount);
     if(arLine != null && vatAmount > 0){
      docLine.setPostType(glCrPostType);
     }else if(arLine != null && vatAmount < 0){
      docLine.setPostType(glDrPostType);
     }
     LOGGER.log(INFO, "Build sort order called with docHdr {0} vatGlAc {1} arLine {2}", new Object[]{
      docHdr, vatGlAc, arLine});
     
     String sortText = this.buildGlLineSortOrderTxt(docHdr, vatGlAc.getSortOrder(), arLine);
     docLine.setSortOrder(sortText);
     docLine.setReference1("VAT - Auto");
     DocLineGl docLineGl = this.buildNewGlDocLine(docLine,docHdr, 
             lineNum,glLineType );
     
     
     
     doclines.add(docLineGl);
     // build standard VAT lines
     LOGGER.log(INFO, "End standard VAT doclines {0} num lines {1}", new Object[]{doclines,doclines.size()});
    }
    
   }
   return docHdr;
  }
  
  public ArrayList<DocTypeRec> getAllDocumentTypes(UserRec usr) throws BacException {
    LOGGER.log(INFO, "Document DM getAllDocumentTypes called");
    ArrayList<DocTypeRec> docTypeRecList = new ArrayList<>();
    try{
      Query q = em.createNamedQuery("allDocTypes");
      try{
        List list = q.getResultList();
        if(list.isEmpty()){
          // no document types exist create defaults
          LOGGER.log(INFO, "No doc types found");
          DocType doc = new DocType();
          User usrDB = em.find(User.class, usr.getId());
          Date curr = new Date();
          doc.setCode("GL JNL");
          doc.setGlAllowed(true);
          doc.setCreated(curr);
          doc.setCreatedBy(usrDB);
          doc.setName("General Ledger Journal");
          em.persist(doc);

          doc = new DocType();
          doc.setCode("AP Doc");
          doc.setGlAllowed(true);
          doc.setApAllowed(true);
          doc.setCreated(curr);
          doc.setCreatedBy(usrDB);
          doc.setName("Accounts Payable Document");
          em.persist(doc);

          doc = new DocType();
          doc.setCode("AR Doc");
          doc.setGlAllowed(true);
          doc.setArAllowed(true);
          doc.setCreated(curr);
          doc.setCreatedBy(usrDB);
          doc.setName("Accounts Receivable Document");
          em.persist(doc);
          list = q.getResultList();

        }

        LOGGER.log(INFO, "Doc types found from DB {0}", list.size());
        DocTypeRec rec;
        ListIterator li = list.listIterator();
        while(li.hasNext()){
          DocType doc = (DocType)li.next();
          rec = this.buildDocTypeRec(doc);
          docTypeRecList.add(rec);
        }
        LOGGER.log(INFO, "Doc type list to return {0}", docTypeRecList);
        return docTypeRecList;




      }catch(IllegalStateException ex){
        throw new BacException("Doc type query type","Doc:02");

      }catch(QueryTimeoutException ex){
        throw new BacException("Doc type query timeout","Doc:03");

      }catch(TransactionRequiredException ex){
        throw new BacException("Doc type transaction error","Doc:04");

      }catch(PessimisticLockException ex){
        throw new BacException("Doc type query lock","Doc:05");

      }catch(LockTimeoutException ex){
        throw new BacException("Doc type query lock timeout","Doc:06");

      }catch(PersistenceException ex){
        throw new BacException("Doc type query other Database error","Doc:07");

      }

    }catch(IllegalArgumentException ex){
      throw new BacException("All doc types query not found","Doc:01");
    }

    
  }

  private DocFi postGlJnl(DocFiRec jnl,  String pg){
   DocFi fiDoc = buildDocFi(jnl, pg);
   
   CompanyBasic comp = em.find(CompanyBasic.class, jnl.getCompany().getId(), OPTIMISTIC);
   fiDoc.setCompany(comp);
   List<DocLineBaseRec> glDocRecLines = jnl.getDocLines();
   ArrayList<DocLineGl> glDocLines = new ArrayList<>();
   Iterator<DocLineBaseRec> it = glDocRecLines.iterator();
   long lineNum = 0;
   while(it.hasNext()){
    DocLineGlRec docLnRec = (DocLineGlRec)it.next(); 
    LOGGER.log(INFO, "docLnRec post type {0}", docLnRec.getPostType().getPostTypeCode());
    DocLineGl docLine = buildDocLineGL(docLnRec,pg);
    docLine.setDocHeaderBase(fiDoc);
    lineNum++;
    docLine.setLineNum(lineNum);
    if(docLnRec.getGlAccount() != null) {
     FiGlAccountComp glAccount = em.find(FiGlAccountComp.class, docLnRec.getGlAccount().getId());
     List periodBal = glAccount.getPeriodBalances();
     if(periodBal.isEmpty()){
      // need to create period balances
      int numperiods = glAccount.getCompany().getChartOfAccounts().getPeriodRule().getNumPeriods();
      int year =  jnl.getFisYear();
      if(numperiods < 1){
       throw new BacException("Invalid number of periods :"+numperiods);
      }
      ArrayList<FiPeriodBalance> balList = new ArrayList<>();
      for(int i=0;i <numperiods; i++ ){
       FiPeriodBalance perBal = glActDM.addGlPeriodBal(glAccount, year, i + 1);
       balList.add(perBal);
      }
      glAccount.setPeriodBalances(balList);
     }
     boolean found = false;
     periodBal = glAccount.getPeriodBalances();
     ListIterator<FiPeriodBalance> perBalIt = periodBal.listIterator();
     while(perBalIt.hasNext() && !found){
      FiPeriodBalance perBal = perBalIt.next();
      FiPeriodBalance restrBal;
      if(perBal.getBalYear() == jnl.getFisYear() && 
              perBal.getBalPeriod() == jnl.getFisPeriod()){
       if(docLine.getRestrictedFund() != null){
        restrBal = this.getRestrictedFundBalances(perBal, docLine);
        if(docLine.getPostType().isDebit()){
         double restrDebit = restrBal.getPeriodDebitAmount() + docLine.getDocAmount();
         double periodRestrAmnt = restrBal.getPeriodDocAmount() + docLine.getDocAmount();
         restrBal.setPeriodDebitAmount(restrDebit);
         restrBal.setPeriodDocAmount(periodRestrAmnt);
         restrBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
         List<DocLineGl> restDrDocs = restrBal.getRestrictedDebitDocLines();
         if(restDrDocs == null){
          restDrDocs = new ArrayList<>();
         }
         restDrDocs.add(docLine);
         docLine.setRestrictedDebitBalance(restrBal);
        }else{
         double restrCredit = restrBal.getPeriodCreditAmount() + docLine.getDocAmount() ;
         double restrPeriodAmnt = restrBal.getPeriodDocAmount() - docLine.getDocAmount();
         restrBal.setPeriodDebitAmount(restrCredit);
         restrBal.setPeriodDocAmount(restrPeriodAmnt);
         restrBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
         List<DocLineGl> restCrDocs = restrBal.getRestrictedCreditDocLines();
         if(restCrDocs == null){
          restCrDocs = new ArrayList<>();
         }
         restCrDocs.add(docLine);
         docLine.setRestrictedCreditBalance(restrBal);
        }
       }
       if(docLine.getPostType().isDebit()){
        double debit = perBal.getPeriodDebitAmount() + docLine.getDocAmount() ;
        double periodAmnt = perBal.getPeriodDocAmount() + docLine.getDocAmount();
        perBal.setPeriodDebitAmount(debit);
        perBal.setPeriodDocAmount(periodAmnt);
        perBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
        List<DocLineGl> drDocs = perBal.getDebitDocLines();
        if(drDocs == null){
         drDocs = new ArrayList<>();
        }
        drDocs.add(docLine);
        perBal.setDebitDocLines(drDocs);
        docLine.setDebitBalance(perBal);    
       }else{
        double credit = perBal.getPeriodCreditAmount() + docLine.getDocAmount() ;
        double periodAmnt = perBal.getPeriodDocAmount() - docLine.getDocAmount();
        perBal.setPeriodDebitAmount(credit);
        perBal.setPeriodDocAmount(periodAmnt);
        perBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
        List<DocLineGl> crDocs = perBal.getCreditDocLines();
        if(crDocs == null){
         crDocs = new ArrayList<>();
        }
        crDocs.add(docLine);
        perBal.setCostCreditLines(crDocs);
        docLine.setCreditBalance(perBal);
       }
      }
     }
     docLine.setGlAccount( glAccount);
     em.persist(docLine);
    }
    glDocLines.add(docLine);
   }
   ArrayList<DocLineBase> docLines = new ArrayList<>();
   ListIterator<DocLineGl> lineIT = glDocLines.listIterator();
   while(lineIT.hasNext()){
    DocLineBase docLineBase = lineIT.next();
    docLines.add(docLineBase);
   }
   fiDoc.setDocLines(docLines);
   LOGGER.log(INFO, "fiDoc doc num {0}", fiDoc.getDocNumber());
  // fiDoc.setDocNumber(compDM.getCompDocNumber(fiDoc.getCompany().getId(), "FI_DOC_FIN"));
   fiDoc.setDocLines(docLines);
   em.persist(fiDoc);
   
   return fiDoc;
  }
  
  /**
 * Posts Posts a standard Journal and returns the ID generated. This is the document number
 * @param jnl
 * @param usr
 * @return
 */
  public long postStdJnl(DocFiRec jnl,  String pg) {
    LOGGER.log(INFO,"Doc DM postStdJnl called with Jnl {0} ",
            new Object[]{jnl});
    if(!trans.isActive()){
     trans.begin();
    }
    DocFi docHdr = buildDocFi(jnl, pg);
    CompanyBasic comp = em.find(CompanyBasic.class, jnl.getCompany().getId(), OPTIMISTIC);
    docHdr.setCompany(comp);
    LOGGER.log(INFO, "jnl {0} docHdr {1}", new Object[]{jnl,docHdr});
    Collection<DocLineBaseRec> glDocRecLines = jnl.getDocLines();
    LOGGER.log(INFO, "Number of doc lines {0}", glDocRecLines.size());
    ArrayList<DocLineGl> glDocLines = new ArrayList<>();
    Iterator<DocLineBaseRec> it = glDocRecLines.iterator();
    long lineNum = 0;
    while(it.hasNext()){
      DocLineGlRec docLnRec = (DocLineGlRec)it.next();
      LOGGER.log(INFO, "doclinerec  {0} page {1}",
              new Object[]{docLnRec.getClass().getSimpleName(),pg});
      DocLineGl docLine = buildDocLineGL(docLnRec,pg);
      LOGGER.log(INFO, "docline from buildDocLineGL {0} ", docLine);
      docLine.setDocHeaderBase(docHdr);
      lineNum++;
      docLine.setLineNum(lineNum);
      if(docLnRec.getGlAccount() != null) {
        LOGGER.log(INFO, "glAccount rec from web attached to doc line : {0} id: {1} ref: {2}",
                new Object[] {docLnRec.getGlAccount(),docLnRec.getGlAccount().getId(),
                docLnRec.getGlAccount().getCoaAccount().getRef()});
        try{
          FiGlAccountComp glAccount = em.find(FiGlAccountComp.class, docLnRec.getGlAccount().getId());

          List periodBal = glAccount.getPeriodBalances();
          LOGGER.log(INFO, "Number of period balances {0}", periodBal.isEmpty());
          if(periodBal.isEmpty()){
            LOGGER.log(INFO, "Need to create period balances for GL account id {0}",glAccount.getId());
            int numperiods = glAccount.getCompany().getChartOfAccounts().getPeriodRule().getNumPeriods();
            int year =  jnl.getFisYear();
            if(numperiods < 1){
              throw new BacException("Invalid number of periods :"+numperiods);
            }
            ArrayList<FiPeriodBalance> balList = new ArrayList<>();
            for(int i=0;i <numperiods; i++ ){
              FiPeriodBalance perBal = glActDM.addGlPeriodBal(glAccount, year, i + 1);
              balList.add(perBal);
            }
            glAccount.setPeriodBalances(balList);
          }
          boolean found = false;
          periodBal = glAccount.getPeriodBalances();
          ListIterator<FiPeriodBalance> perBalIt = periodBal.listIterator();

          while(perBalIt.hasNext() && !found){
            FiPeriodBalance perBal = (FiPeriodBalance)perBalIt.next();
            FiPeriodBalance restrBal = null;
            if(perBal.getBalYear() == jnl.getFisYear() && perBal.getBalPeriod() == jnl.getFisPeriod()){
              LOGGER.log(INFO, "found period balance");
              LOGGER.log(INFO, "DocLine restr fund {0}", docLine.getRestrictedFund());
              if(docLine.getRestrictedFund() != null){
                LOGGER.log(INFO, "get restricted fund for period balance");
                restrBal = this.getRestrictedFundBalances(perBal, docLine);
                LOGGER.log(INFO, "restricted fund for period balance {0}",restrBal);
                if(docLine.getPostType().isDebit()){
                  LOGGER.log(INFO, "Add rest debit amount {0}", docLine.getDocAmount());
                  double restrDebit = restrBal.getPeriodDebitAmount() + docLine.getDocAmount() ;
                  double periodRestrAmnt = restrBal.getPeriodDocAmount() + docLine.getDocAmount();
                  restrBal.setPeriodDebitAmount(restrDebit);
                  restrBal.setPeriodDocAmount(periodRestrAmnt);
                  restrBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
                  List<DocLineGl> restDrDocs = restrBal.getRestrictedDebitDocLines();
                  LOGGER.log(INFO, "Add restr debit doc lines: {0}",restDrDocs);
                  if(restDrDocs == null){
                    restDrDocs = new ArrayList<>();
                  }
                  restDrDocs.add(docLine);
                  docLine.setRestrictedDebitBalance(restrBal);

                }else{
                  LOGGER.log(INFO, "Add rest Fund credit amount {0}", docLine.getDocAmount());
                  double restrCredit = restrBal.getPeriodCreditAmount() + docLine.getDocAmount() ;
                  double restrPeriodAmnt = restrBal.getPeriodDocAmount() - docLine.getDocAmount();
                  restrBal.setPeriodDebitAmount(restrCredit);
                  restrBal.setPeriodDocAmount(restrPeriodAmnt);
                  restrBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
                  List<DocLineGl> restCrDocs = restrBal.getRestrictedCreditDocLines();
                  if(restCrDocs == null){
                    restCrDocs = new ArrayList<>();
                  }
                  restCrDocs.add(docLine);
                  docLine.setRestrictedCreditBalance(restrBal);

                }
              }
              if(docLine.getPostType().isDebit()){
                LOGGER.log(INFO, "Add debit amount {0}", docLine.getDocAmount());
                double debit = perBal.getPeriodDebitAmount() + docLine.getDocAmount() ;
                double periodAmnt = perBal.getPeriodDocAmount() + docLine.getDocAmount();
                perBal.setPeriodDebitAmount(debit);
                perBal.setPeriodDocAmount(periodAmnt);
                perBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
                List<DocLineGl> drDocs = perBal.getDebitDocLines();
                if(drDocs == null){
                 drDocs = new ArrayList<>();
                }
                drDocs.add(docLine);
                perBal.setDebitDocLines(drDocs);
                docLine.setDebitBalance(perBal);


              }else{
                LOGGER.log(INFO, "Add credit amount {0}", docLine.getDocAmount());
                double credit = perBal.getPeriodCreditAmount() + docLine.getDocAmount() ;
                double periodAmnt = perBal.getPeriodDocAmount() - docLine.getDocAmount();
                perBal.setPeriodDebitAmount(credit);
                perBal.setPeriodDocAmount(periodAmnt);
                perBal.setCfwdDocAmount(perBal.getPeriodDebitAmount() - perBal.getPeriodCreditAmount());
                List<DocLineGl> crDocs = perBal.getCreditDocLines();
                if(crDocs == null){
                 crDocs = new ArrayList<>();
                }
                crDocs.add(docLine);
                perBal.setCostCreditLines(crDocs);
                docLine.setCreditBalance(perBal);

              }
            }
          }

         LOGGER.log(INFO,"glAccount before call to setGlAccount: {0} ",glAccount);
          docLine.setGlAccount( glAccount);
          LOGGER.log(INFO, "Persist doc line {0}",docLine);
          em.persist(docLine);
        }catch(IllegalArgumentException ex){
          LOGGER.log(INFO, "Error getting GL account id {0}", docLnRec.getGlAccount().getId());
          throw new BacException("Error getting GL account");
       }
      }
      glDocLines.add(docLine);
      LOGGER.log(INFO, "After add to doc GL lines num lines {0} ", glDocLines.size());
      
    }
    ArrayList<DocLineBase> docLines = new ArrayList<>();
    ListIterator<DocLineGl> lineIT = glDocLines.listIterator();
    while(lineIT.hasNext()){
      DocLineBase docLineBase = lineIT.next();
      docLines.add(docLineBase);
    }

    docHdr.setDocLines(docLines);
     LOGGER.log(INFO, "docHdr doc num {0}", docHdr.getDocNumber());
    //docHdr.setDocNumber(compDM.getCompDocNumber(docHdr.getCompany().getId(), "FI_DOC_FIN"));
     docHdr.setDocLines(docLines);
     try{
     em.persist(docHdr);
     trans.commit();
     return docHdr.getDocNumber();
     
    }catch(IllegalStateException ex){
     trans.rollback();
     LOGGER.log(INFO, "Save Doc Illegal state {0}", ex.getLocalizedMessage());
     throw new BacException("Save Doc Illegal state "+ex.getLocalizedMessage(),"Doc:03");
    }catch(QueryTimeoutException ex){
     trans.rollback();
     throw new BacException("Doc type query timeout"+ex.getLocalizedMessage(),"Doc:03");
    }catch(TransactionRequiredException ex){
     trans.rollback();
     throw new BacException("Doc type transaction error"+ex.getLocalizedMessage(),"Doc:04");
    }catch(PessimisticLockException ex){
     trans.rollback();
     throw new BacException("Doc type query lock"+ex.getLocalizedMessage(),"Doc:05");
    }catch(LockTimeoutException ex){
     trans.rollback();
     throw new BacException("Doc type query lock timeout"+ex.getLocalizedMessage(),"Doc:06");
    }catch(PersistenceException ex){
     LOGGER.log(INFO, "Save doc other db error {0}", ex.getLocalizedMessage());
     throw new BacException("Doc type query other Database error"+ex.getLocalizedMessage(),"Doc:07");
    }
  }


  public DocFiPartialRec postStdJnlPartial(DocFiPartialRec rec, String pg){
   LOGGER.log(INFO, "docDM.postStdJnlPartial called with doc id{0} page {1}", new Object[]{rec.getId(),pg});
   if(!trans.isActive()){
    trans.begin();
   }
   DocFiPartial doc = this.buildDocFiPartial(rec, pg);
   if(rec.getId() == null){
    rec.setId(doc.getId());
    rec.setDocNumber(doc.getDocNumber());
   }
   List<DocLineGlPartialRec> recLines = rec.getGlLines();
   List<DocLineBasePartial> docLines = new ArrayList<>();
   long lineNum = 0;
   if(recLines != null && !recLines.isEmpty()){
   for(DocLineGlPartialRec glLineRecL :recLines){
    lineNum++;
    glLineRecL.setLineNum(lineNum);
    DocLineGlPartial docLine = this.buildDocLineGlPartial(glLineRecL, pg);
    docLine.setDocHeaderBase(doc);
    docLines.add(docLine);
   }
   doc.setDocLines(docLines);
   }
   trans.commit();
   return rec;
  }
  
  public DocFiTemplAccrPrePayRec postTemplateJnl(DocFiTemplAccrPrePayRec tmpl, DocFiRec fiDocRec, int mode, String pg){
   LOGGER.log(INFO, "postTemplateJnl called with templ {0} fiDoc {1} mode {2}", 
           new Object[]{tmpl,fiDocRec,mode});
   if(!trans.isActive()){
    trans.begin();
   }
   UserRec crUsr = tmpl.getCreatedBy();
   Date crDate = tmpl.getCreateOn();
   CompanyBasicRec comp = tmpl.getCompany();
   switch(mode){
    case DocFiTemplAccrPrePayRec.REPEAT:
     LOGGER.log(INFO, "Repeat");
     break;
    case DocFiTemplAccrPrePayRec.REVERSAL:
     LOGGER.log(INFO, "Reversal");
     break;
    case DocFiTemplAccrPrePayRec.SETUP:
     LOGGER.log(INFO, "Setup");
     // post FI doc
     //List<DocLineBaseRec> fiLines = fiDocRec.getDocLines();
     ListIterator<DocLineBaseRec> fiLnLi = fiDocRec.getDocLines().listIterator();
     while(fiLnLi.hasNext()){
      DocLineBaseRec fiLn = fiLnLi.next();
      fiLn.setCreateBy(crUsr);
      fiLn.setCreateDate(crDate);
      fiLn.setComp(comp);
      LOGGER.log(INFO, "fiLn post Type {0}", fiLn.getPostType().getPostTypeCode());
     }
     
     fiDocRec.setCreatedBy(crUsr);
     fiDocRec.setCreateOn(crDate);
     fiDocRec.setCompany(comp);
     
     
     DocFi fiDoc = postGlJnl(fiDocRec, pg);
     LOGGER.log(INFO, "FI doc posted with id {0}", fiDoc.getId());
     DocFiTemplAccrPrePay tmplDoc = this.buildDocFiTemplAccPrePay(tmpl, pg);
     if(tmpl.getDocNumber() == 0){
      tmpl.setDocNumber(tmplDoc.getDocNumber());
     }
     tmplDoc.setOriginalJnl(fiDoc);
     
     List<DocLineTemplate> tmplLns = new ArrayList<>();
     List<DocLineFiTemplateRec> tmplRecLines = tmpl.getDocLines();
     for(DocLineFiTemplateRec templLineRec:tmplRecLines){
      DocLineTemplate ln = this.buildDocLineFiTemplGl((DocLineFiTemplGlRec)templLineRec, pg);
      ln.setDocHeader(tmplDoc);
      tmplLns.add(ln);
     }
     tmplDoc.setDocLines(tmplLns);
     
     LOGGER.log(INFO, "Fi lines {0}", tmplLns);
     LOGGER.log(INFO, "Templ rec lines {0}", tmplRecLines);
     LOGGER.log(INFO, "end template doc save");
     
     break;
    default:
     LOGGER.log(INFO, "Invalid mode {0}", mode);
   }
   trans.commit();
   return tmpl;
  }
  
  /**
   * Update document printed - when downloaded assumed printed
   * @param docRec
   * @param usr Who downloaded
   * @param pg 
   */
  public void printedSalesFiDoc(DocInvoiceArRec docRec, UserRec usr, String pg ){
   LOGGER.log(INFO, "called printedSalesFiDoc");
   if(!trans.isActive()){
    trans.begin();
   }
   
   DocInvoiceAr doc = em.find(DocInvoiceAr.class, docRec.getId());
   if(!doc.isPrinted()){
    
    User crUsr = em.find(User.class, usr.getId());
    AuditDocInvoiceAr aud = this.buildAuditDocInvoiceAr(doc, crUsr, pg, 'U');
    aud.setFieldName("SL_DOC_PRNT");
    aud.setNewValue(String.valueOf(true));
    aud.setOldValue(String.valueOf(doc.isPrinted()));
    doc.setPrinted(true);
    trans.commit();
    LOGGER.log(INFO, "after DB update printed status {0}", doc.isPrinted());
   }
   
   
  }
  public synchronized  long nextCompDocNumber(CompanyBasic comp, String docType) throws BacException {
  LOGGER.log(INFO, "Doc Mgr getCompDocNumber called with comp id {0} type {1}", new Object[]{comp.getId(),docType});
  
  long ret;
  CompanyDocNumbers docNum;
  try{
   Query q = em.createNamedQuery("lastDocNum");
   q.setParameter("docType",docType);
   q.setParameter("compId", comp.getId());
   
   docNum = (CompanyDocNumbers)q.getSingleResult();
   ret = docNum.getDocNum();
   ret++;
   
   docNum.setDocNum(ret) ;
   
   //em.flush();
   LOGGER.log(INFO, "doc num is now: {0}",docNum.getDocNum()); 
  }catch(NoResultException ex){
   LOGGER.log(INFO, "Need new compDocNum for doc type {0}", docType);
   docNum = new CompanyDocNumbers();
   docNum.setComp(comp);
   docNum.setDocType(docType);
   docNum.setDocNum(1);
   em.persist(docNum);
   ret = docNum.getDocNum();
   LOGGER.log(INFO, "Doc num id {0} doc type {1} number {2}", new Object[]{docNum.getId(),
    docNum.getDocType(), docNum.getDocNum() });
  }
  
  return ret;
 }
  private List<DocLineGl> getArReconLinesForArLine(DocLineArRec arLnRec){
   List<DocLineGl> reconLines = new ArrayList<>();
   Query q = em.createNamedQuery("glReconLinesForArLine");
   q.setParameter("arLineid", arLnRec.getId());
   List l = q.getResultList();
   ListIterator li = l.listIterator();
   while(li.hasNext()){
    DocLineGl glLn = (DocLineGl)li.next();
    reconLines.add(glLn);
   }
   return reconLines;
  }
  private Fund getRestrictedFund(long id){
   Fund fnd = null;
   
   if(restrictedFunds == null){
    // no restricted funds loaded
    restrictedFunds = new ArrayList<>();
    fnd = em.find(Fund.class, id,OPTIMISTIC);
    restrictedFunds.add(fnd);
    return fnd;
   }
   ListIterator<Fund> li = restrictedFunds.listIterator();
   while(li.hasNext() ){
    Fund fund = li.next();
    if(fund.getId() == id){
     fnd = fund;
     
     return fnd;
    }
   }
   // if get here we need to add fund
   fnd = em.find(Fund.class, id,OPTIMISTIC);
   restrictedFunds.add(fnd);
   return fnd;
  }
  public FiPeriodBalance getRestrictedFundBalances(FiPeriodBalance glBal,
          DocLineGl docLine) throws BacException {
    LOGGER.log(INFO, "getRestrictedFundBalances called with glBalance {0} doc line {1}",
            new Object[]{glBal,docLine});
    List<FiPeriodBalance> rBals = glBal.getRestrPerBals();
    if(rBals == null || rBals.isEmpty()){
      FiPeriodBalance rBal = new FiPeriodBalance();
      rBal.setBalPeriod(glBal.getBalPeriod());
      rBal.setBalYear(glBal.getBalYear());
      rBal.setYear(glBal.getYear());
      rBal.setBalanceType(1);
      rBal.setRestrictedFund(docLine.getRestrictedFund());
      rBal.setGlPerBal(glBal);
      if(rBals == null){
        rBals = new ArrayList<>();
      }
      rBals.add(rBal);
      glBal.setRestrPerBals(rBals);
      em.persist(rBal);
      return rBal;
    }else{
      ListIterator<FiPeriodBalance> rBalsIt = rBals.listIterator();
      boolean found = false;
      while(rBalsIt.hasNext() && !found){
        FiPeriodBalance rBal = rBalsIt.next();
        if(rBal.getRestrictedFund().getId() == docLine.getRestrictedFund().getId()){
          return rBal;
        }
      }
      if(!found){
        FiPeriodBalance rBal = new FiPeriodBalance();
        rBal.setBalPeriod(glBal.getBalPeriod());
        rBal.setBalYear(glBal.getBalYear());
        rBal.setYear(glBal.getYear());
        rBal.setBalanceType(1);
        rBal.setRestrictedFund(docLine.getRestrictedFund());
        rBal.setGlPerBal(glBal);
        rBals.add(rBal);
        glBal.setRestrPerBals(rBals);
        em.persist(rBal);
        return rBal;
      }
    }
    return null;
  }

  

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 public List<DocInvoiceArRec> getSalesInvoices(SelectOptSalesDayBook opt){
  LOGGER.log(INFO, "getSalesInvoices called");
  Query q = em.createNamedQuery("salesInvsComp");
  q.setParameter("compId", opt.getComp().getId());
  List rs = q.getResultList();
  if(rs == null || rs.isEmpty()){
   return null;
  }
  List<DocInvoiceArRec> retList = new ArrayList<>();
  for(Object i:rs){
   DocInvoiceAr inv = (DocInvoiceAr)i;
   DocInvoiceArRec invRec = buildDocInvoiceArRec(inv);
   DocFi docFi = inv.getFiDocument();
   Query qLn = em.createNamedQuery("lineForDoc");
   qLn.setParameter("docId", docFi.getId());
   try{
    DocLineAr lnAr = (DocLineAr)qLn.getSingleResult();
    DocLineArRec lnArRec = this.buildDocLineArRecMin(lnAr);
    invRec.setArDocLine(lnArRec);
   }catch(NoResultException ex){
    LOGGER.log(INFO, "No line for doc id {0}", docFi.getId());
   }
   DocFiRec docFiRec = buildDocFiRec(docFi);
   List<DocLineBase> lines = docFi.getDocLines();
   LOGGER.log(INFO, "docFi lines {0}", lines);
   if(lines != null){
    for(DocLineBase l:lines){
     LOGGER.log(INFO, "line type {0}", i.getClass().getSimpleName());
    }
   }
   invRec.setFiDocument(docFiRec);
   retList.add(invRec);
  }
  LOGGER.log(INFO, "getSalesInvoices returns {0}",retList);
  return retList;
  
 }
 
 public DocFiRec postSalesInvoice(DocFiRec invoice, List<SalesPartFiLineRec> lines, 
         List<RestrictFundBalance> fundBals,List<DocVatSummary> docVatSum, VatReturnRec vatReturnRec, 
          String source)  throws BacException {
  LOGGER.log(INFO, "DocumentDM.postSaleInvoice called with invoice {0} user {1} source {2}", 
          new Object[]{invoice,source});
  LOGGER.log(INFO, "Fund bals {0}", fundBals);
  LOGGER.log(INFO,"docVatSum {0}",docVatSum);
  
  if(!trans.isActive()){
   trans.begin();
  }
  User crUsr = em.find(User.class, invoice.getCreatedBy().getId());
  boolean found = false;
  long lineNum = 0;
  List<FiPeriodBalance> glActBalances = null;
  List<FiPeriodBalance> glAcRestrtBalances = null;
  List<FiPeriodBalance> glAcCostAcBalances = null;
  List<FiPeriodBalance> glAcProjAcBalances = null;
  
  ArAPAccountIF arApAccount = null;
  String partnerType = null;
  List<DocLineBaseRec> docLinesRec = invoice.getDocLines();
  LOGGER.log(INFO,"lines {0}", lines);
  if(lines == null || lines.isEmpty()){
   throw new BacException("Cannot create sales invoice with no lines");
  }
  
  if(source == null || source.isEmpty()){
   throw new BacException("Must specify page update came from");
  }
  
  // build document header
  LOGGER.log(INFO, "About gbuild doc header");
  DocFi docHdr = buildDocFi(invoice, source);
     
  if(docLinesRec == null){
   throw  new BacException("No AR account line");
  }
 
  List<LineTypeRuleRec>lineTypeRecList = sysBuff.getLineTypeRules();
  ListIterator<LineTypeRuleRec> lineTypeLi = lineTypeRecList.listIterator();
  LineTypeRule glLineType = null;
  LineTypeRule arLineType = null;
  List<DocLineBase> docLines = null;
  found = false;
  while(lineTypeLi.hasNext() ){
   LineTypeRuleRec lRec = lineTypeLi.next();
   if(lRec.getLineCode().equalsIgnoreCase("GL")){
    glLineType = em.find(LineTypeRule.class, lRec.getId(), OPTIMISTIC);
   }
   if(lRec.getLineCode().equalsIgnoreCase("AR")){
    arLineType = em.find(LineTypeRule.class, lRec.getId(), OPTIMISTIC);
    
   }
  }
  if(glLineType == null ||arLineType == null ){
   throw new BacException("Could not find line types");
  }
  
  // get posting types for SL invoice
  List<PostTypeRec> postTypes = sysBuff.getPostTypes();
  ListIterator<PostTypeRec> postTypesLI = postTypes.listIterator();
  PostType glDrPostType = null;
  PostType glCrPostType = null;
  PostType arInvPostType = null;
  PostTypeRec glDrPostTypeRec = null;
  PostTypeRec glCrPostTypeRec = null;
  PostTypeRec arInvPostTypeRec = null;
  //arInv
  found = false;
  while(postTypesLI.hasNext() && !found){
   PostTypeRec pRec = postTypesLI.next();
   if(pRec.getPostTypeCode().equalsIgnoreCase("glDr")){
    glDrPostType = em.find(PostType.class, pRec.getId(), OPTIMISTIC);
    glDrPostTypeRec = pRec;
    
   }
   if(pRec.getPostTypeCode().equalsIgnoreCase("glCr")){
    glCrPostType = em.find(PostType.class, pRec.getId(), OPTIMISTIC);
    glCrPostTypeRec = pRec;
   }
   if(pRec.getPostTypeCode().equalsIgnoreCase("arInv")){
    arInvPostTypeRec = pRec;
    arInvPostType = em.find(PostType.class, pRec.getId(), OPTIMISTIC);
   }
  }
  
  // add AR subLedger line
  
  DocLineArRec arlineRec = (DocLineArRec)docLinesRec.get(0);
  LOGGER.log(INFO, "user {0}", arlineRec.getCreateBy());
  LOGGER.log(INFO, "arlineRec {0}", arlineRec);
  LOGGER.log(INFO, "post typer  {0}", arlineRec.getPostType());
  String invCrn = null;
   if(arlineRec.getPostType().getPostTypeCode().equalsIgnoreCase("arCrn")){
    invCrn = "CRN";
   }else if(arlineRec.getPostType().getPostTypeCode().equalsIgnoreCase("arInv")){
    invCrn = "INV";
   }
  
  double totalAmnt = arlineRec.getInvoice().getTotalAmount();
  arlineRec.setDocAmount(totalAmnt);
  arlineRec.setHomeAmount(totalAmnt);
  arlineRec.setPostType(arInvPostTypeRec);
  lineNum++;// = lineNum + 1;
  LOGGER.log(INFO, "lineNum is {0}", lineNum);
  arlineRec.setLineNum(lineNum);
  LOGGER.log(INFO, "Line type {0}", arlineRec.getLineType());
  if(arlineRec.getLineType() == null){
   arlineRec.setLineType(sysBuff.getLineTypeRuleByCode("AR"));
  }
  LOGGER.log(INFO, "Line type 2 {0}", arlineRec.getLineType());
  DocLineAr arLine = buildDocLineAr(arlineRec,docHdr,source);
  arLine.setDocNumber(docHdr.getDocNumber());
  LOGGER.log(INFO,"Attempt commit after buildDocLineAr ln 5619 ");
  

  arLine.setLineType(arLineType);
  arLine.setPostType(arInvPostType);
  docLines = docHdr.getDocLines();
  if(docLines == null){
   docLines = new ArrayList<>();
  }
  docLines.add(arLine);
  // get AR Account
  ArAccount arAccount = arLine.getArAccount();
  
  arApAccount = arAccount;
  partnerType = "AR";
  // set AR account balance
  double arAcBal = arAccount.getAccountBalance();
  arAcBal = arAcBal + totalAmnt;
  arAccount.setAccountBalance(arAcBal);
  FiArPeriodBalance arPeriodBalance;
  Query perBalQ = em.createNamedQuery("arPerBal");
  perBalQ.setParameter("acntId", arAccount.getId());
  perBalQ.setParameter("yr", docHdr.getFisYear());
  perBalQ.setParameter("per", docHdr.getFisPeriod());
  try{
   arPeriodBalance = (FiArPeriodBalance)perBalQ.getSingleResult();
  } catch(NoResultException ex   ){
   arPeriodBalance = this.buildArAnnualAcBalance(arAccount, docHdr.getFisYear(), docHdr.getFisPeriod(), crUsr);
  } catch (PersistenceException ex){
   arPeriodBalance = this.buildArAnnualAcBalance(arAccount, docHdr.getFisYear(), docHdr.getFisPeriod(), crUsr);
  }
  
  arAccountDM.setArPeriodBalance(arPeriodBalance, arLine, invCrn,  source);
  
  
  
  
  // Add Invoice to sub-ledger line
  DocInvoiceArRec invRec = arlineRec.getInvoice();
  
  invRec.setCreatedBy(invoice.getCreatedBy());
  invRec.setCreatedOn(invoice.getCreateOn());
  arlineRec.setInvoice(invRec);
  
  LOGGER.log(INFO, "About to call buildDocInvoiceAr arlineRec {0} ", arlineRec.getInvoice());
  LOGGER.log(INFO, "docHdr Comp {0}", docHdr.getCompany());
  LOGGER.log(INFO, "invCrn {0}", invCrn);
  DocInvoiceAr docInvoiceAr = buildDocInvoiceAr(arlineRec.getInvoice(),docHdr.getCompany().getId(),invCrn,
    source);
  LOGGER.log(INFO, "docInvoiceAr inv num {0}", docInvoiceAr.getInvoiceNumber());
  String invNumber = docInvoiceAr.getInvoiceNumber();
  
  
  docInvoiceAr.setFiDocument(docHdr);
  docHdr.setDocInvoiceAr(docInvoiceAr);
  invRec.setInvoiceNumber(docInvoiceAr.getInvoiceNumber());
  invoice.setDocInvoiceAr(invRec);
  
  // Add GL lines to document
  ArrayList<DocGlVatCodeSummary> vatSplit = new ArrayList<>();
  ListIterator<SalesPartFiLineRec> partListIt = lines.listIterator();
  while(partListIt.hasNext()){
   SalesPartFiLineRec partLine = partListIt.next();
  LOGGER.log(INFO,"add GL lines getSalesAccount() id {0} gl Ac ref {1} vat code {2}",
          new Object[]{
   partLine.getPartComp().getSalesAccount().getId(), partLine.getPartComp().getSalesAccount().getCoaAccount().getRef(),
   partLine.getVatCode()
  } ); 
  
   double lineAmount;
   FiGlAccountComp glAc;
   DocLineGl docLineGl;
   SortOrder glAcSortOrder;
   String sortText;
   lineAmount = partLine.getLineTotal();
   VatCodeCompanyRec lineVatCode = partLine.getVatCode();
   LOGGER.log(INFO, "lineVatCode {0}", lineVatCode);
   LOGGER.log(INFO, "GL account ", partLine.getPartComp().getSalesAccount());
   glAc = em.find(FiGlAccountComp.class, partLine.getPartComp().getSalesAccount().getId(), OPTIMISTIC);
   glAcSortOrder = glAc.getSortOrder();
   docLineGl = buildGlDocLine(partLine, glAc, lineAmount);
   sortText = this.buildGlLineSortOrderTxt(invoice, glAcSortOrder,  arlineRec);
  
   docLineGl.setSortOrder(sortText);
   lineNum++;
   docLineGl.setLineNum(lineNum);
   docLineGl.setDocHeaderBase(docHdr);
   docLineGl.setLineType(glLineType);
   docLineGl.setPostType(glCrPostType);
   docLines.add(docLineGl);
   
   vatSplit = this.setVatCodeSummary(vatSplit, docLineGl, lineVatCode,partLine.getPartComp().getCompany());
   LOGGER.log(INFO,"About to set balance");
   glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docHdr);
   
   if(docLineGl.getCostAccount() != null && docLineGl.getCostAccount().getId() != 0){
    // set Cost Account balances
    LOGGER.log(INFO, "Update cost account {0} balances ", docLineGl.getCostAccount().getId());
    glAcCostAcBalances = buildDocLineGlCostAcBalance(docLineGl, glAcCostAcBalances, docHdr);
   }
   
   if(docLineGl.getProgramCostAccount() != null && docLineGl.getProgramCostAccount().getId() != 0){
    // set program Account balances
    LOGGER.log(INFO, "Update cost account {0} balances ", docLineGl.getCostAccount().getId());
    glAcCostAcBalances = buildDocLineGlProgramAcBalance(docLineGl, glAcCostAcBalances, docHdr);
   }
   // is cost of sales accounting required?
   LOGGER.log(INFO, "partLine.getPartComp().isCostOfSalesAccounting() {0}", 
           partLine.getPartComp().isCostOfSalesAccounting());
   if(partLine.getPartComp().isCostOfSalesAccounting() && (partLine.getPartComp().getCosAccount() != null &&
           partLine.getPartComp().getCosAccount().getId() != null)){
    LOGGER.log(INFO, "Add cost accounting lines");
    lineAmount = partLine.getPartComp().getStockValue()
      ;
    lineAmount = lineAmount * partLine.getQty();
    // debit cost of sales
    LOGGER.log(INFO, "partLine.getPartComp().getCosAccount() {0}", partLine.getPartComp().getCosAccount());
    glAc = em.find(FiGlAccountComp.class, partLine.getPartComp().getCosAccount().getId(), OPTIMISTIC);
    glAcSortOrder = glAc.getSortOrder();
    docLineGl = buildGlDocLine(partLine, glAc, lineAmount);
    sortText = this.buildGlLineSortOrderTxt(invoice, glAcSortOrder,  arlineRec);
    docLineGl.setSortOrder(sortText);
    lineNum++;
    docLineGl.setLineNum(lineNum);
    docLineGl.setDocHeaderBase(docHdr);
    docLineGl.setLineType(glLineType);
    docLineGl.setPostType(glDrPostType);
    docLines.add(docLineGl);
    glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docHdr);
    // credit stock account
    glAc = em.find(FiGlAccountComp.class, partLine.getPartComp().getStockAccount().getId(), OPTIMISTIC);
    glAcSortOrder = glAc.getSortOrder();
    docLineGl = buildGlDocLine(partLine, glAc,lineAmount);
    sortText = this.buildGlLineSortOrderTxt(invoice, glAcSortOrder,  arlineRec);
    docLineGl.setSortOrder(sortText);
    lineNum++;
    docLineGl.setLineNum(lineNum);
    docLineGl.setDocHeaderBase(docHdr);
    docLineGl.setLineType(glLineType);
    docLineGl.setPostType(glCrPostType);
    docLines.add(docLineGl);
    glActBalances = buildDocLineGlAcBalance(docLineGl, glActBalances, docHdr);
   }
   
  }
  
  LOGGER.log(INFO, "After add oart lines the FI doc lines are ");
  for(DocLineBase ln: docLines){
   LOGGER.log(INFO, "Line class {0}", ln.getClass().getSimpleName());
   
   LOGGER.log(INFO, "Amount {0}", ln.getHomeAmount());
  }
  // add VAT lines if any to the document
  LOGGER.log(INFO,"docVatSum {0}",docVatSum);
  if(docVatSum != null){
   //need to add VAT lines
   VatReturn vatReturn = em.find(VatReturn.class, vatReturnRec.getId(), OPTIMISTIC);
   //this.setVatLines(docLines, null, compRec, docHdr, glLineType, glDrPostTypeRec, glCrPostTypeRec, user, crDate, arlineRec)
   VatRegScheme regScheme = em.find(VatRegScheme.class, vatReturn.getVatRegScheme().getId(), 
           OPTIMISTIC);
   ListIterator<DocVatSummary> vatSumLi = docVatSum.listIterator();
   
   while(vatSumLi.hasNext()){
    DocVatSummary vatSumRec = vatSumLi.next();
    LOGGER.log(INFO,"vatSumRec rate account {0} vat code {1}", 
            new Object[]{vatSumRec.getRateAccount(),vatSumRec.getVatCode().getVatCode().getCode()});
    
    FiGlAccountComp glAc = em.find(FiGlAccountComp.class, vatSumRec.getRateAccount().getId(), OPTIMISTIC);
    LOGGER.log(INFO, "VAT rate GL account id {0}", glAc.getId());
    SortOrder glAcSortOrder = glAc.getSortOrder();
    double lineAmount = vatSumRec.getVat();
    lineNum++;
    DocLineGlRec lineRec = new DocLineGlRec();
    lineRec.setComp(invoice.getCompany());
    lineRec.setGlAccount( vatSumRec.getRateAccount());
    //lineRec.setCreateBy(user);
   // lineRec.setCreateDate(crDate);
    lineRec.setDocAmount(lineAmount);
    lineRec.setHomeAmount(lineAmount);
    lineRec.setLineNum(lineNum);
    if(lineAmount > 0){
     lineRec.setPostType(glCrPostTypeRec);
    }else{
     lineRec.setPostType(glDrPostTypeRec);
    }
    /*if(vatSumRec.getFund() != null){
     lineRec.setRestrictedFund(vatSumRec.getFund());
    }*/
    String sortOrderTxt = buildGlLineSortOrderTxt(invoice, glAcSortOrder, arlineRec);
    lineRec.setSortOrder(sortOrderTxt);
    
    DocLineGl glLine = buildDocLineGL(lineRec, source);
    glLine.setLineType(glLineType);
    glLine.setDocHeaderBase(docHdr);
    glLine.setGlAccount(glAc);
    docLines.add(glLine);
    // add vat return line
    LOGGER.log(INFO, "about to add VAT line");
    vatReturn = vatDM.addVatReturnLinePvt(vatReturn,regScheme, arAccount, docHdr, vatSumRec,glLine,"suppDomInv");
    LOGGER.log(INFO, "after add VAT line");
    LOGGER.log(INFO,"Called VAT set GL bal with GL ac id {0}",glLine.getGlAccount().getId());
    glActBalances = buildDocLineGlAcBalance(glLine, glActBalances, docHdr);
   }
  }
  
  LOGGER.log(INFO, "docLines after VAT processong {0}", docLines);
  
  
  // TODO: loop over lines to work out restricted fund split
  double recAcTotal = arLine.getDocAmount();
  SortOrder glAcSortOrder;
  List<DocLineGl> recLines = arLine.getReconiliationLines();
  if(recLines == null){
   recLines = new ArrayList<>();
  }
  FiGlAccountComp glRecAc = arLine.getArAccount().getReconciliationAc();
  glAcSortOrder = glRecAc.getSortOrder();
  if(fundBals != null && fundBals.size() > 0){
   LOGGER.log(INFO, "Need to add control account recs to AR line");
   }
   
  if(fundBals != null && !fundBals.isEmpty()){
   ListIterator<RestrictFundBalance> fndBalsLi = fundBals.listIterator();
   while(fndBalsLi.hasNext()){
    RestrictFundBalance fndBal = fndBalsLi.next();
    double recAcAmount = fndBal.getAmount();
    recAcTotal = recAcTotal - recAcAmount;
    DocLineGlRec lineRec = new DocLineGlRec();
    
    lineRec.setComp(invoice.getCompany());
/*    lineRec.setCreateBy(user);
    lineRec.setCreateDate(crDate);
*/
    lineRec.setDocAmount(recAcAmount);
    lineRec.setHomeAmount(recAcAmount);
    if(recAcAmount > 0){
     lineRec.setPostType(glDrPostTypeRec);
    }else{
     lineRec.setPostType(glCrPostTypeRec);
    }
    lineRec.setRestrictedFund(fndBal.getFund());
     
    String sortOrderTxt = buildGlLineSortOrderTxt(invoice, glAcSortOrder, arlineRec);
    lineRec.setSortOrder(sortOrderTxt);
    DocLineGl glLine = buildDocLineGL(lineRec, source);
    glLine.setReconcilForArLine(arLine);
    glLine.setGlAccount(glRecAc);
    glAcRestrtBalances = buildDocLineGlRestrfndAcBalance(glLine, glAcRestrtBalances, 
            docHdr);
    recLines.add(glLine);
   }
  }
  
  if(recAcTotal > 0){
   //There is an non restricted balance
   DocLineGlRec lineRec = new DocLineGlRec();
    
   lineRec.setComp(invoice.getCompany());
   //lineRec.setCreateBy(user);
  // lineRec.setCreateDate(crDate);
   lineRec.setDocAmount(recAcTotal);
   lineRec.setHomeAmount(recAcTotal);
   if(recAcTotal > 0){
    lineRec.setPostType(glDrPostTypeRec);
   }else{
    lineRec.setPostType(glCrPostTypeRec);
   }
   String sortOrderTxt = buildGlLineSortOrderTxt(invoice,glAcSortOrder , arlineRec);
   lineRec.setSortOrder(sortOrderTxt);
   //lineRec.setGlAccount(glAc);
   DocLineGl glLine = buildDocLineGL(lineRec, source);
   glLine.setReconcilForArLine(arLine);
   glLine.setGlAccount(glRecAc);
   LOGGER.log(INFO, "call set balances for non restr bal rec gl ac id {0}", glLine.getGlAccount());
   glActBalances = buildDocLineGlAcBalance(glLine, glActBalances, docHdr);
   recLines.add(glLine);
   
  }
  // set GL account balances
  
  // 
  LOGGER.log(INFO, "docLines {0} number of lines {1}", new Object[]{docLines, docLines.size()});
  //TODO: set IDs in rec lines
  
  invoice.setId(docHdr.getId());
  arlineRec.getInvoice().setInvoiceNumber(invNumber);
  
  invoice.getDocLines().set(0, arlineRec);
  docHdr.setPartnerRef(invNumber);
  salesDM.addSalesInfoRecordPvt(docHdr, "INV", source);
  invoice.setPartnerRef(invNumber);
  
  
  LOGGER.log(INFO, "End post Sl invoice inv num {0}", invoice.getPartnerRef());
  LOGGER.log(INFO, "doc header inv {0}", docHdr.getDocInvoiceAr().getInvoiceNumber());
  // set sales info object
  
  trans.commit();
  return invoice;
 }

 public List<DocLineArRec> getOutstandingDocsForArAccount(ArAccountRec arAccount) throws BacException {
  LOGGER.log(INFO, "getOutstandingDocsForArAccount called with ac {0}", arAccount);
  if(!trans.isActive()){
   trans.begin();
     
  }
  ArAccount ac = em.find(ArAccount.class, arAccount.getId(), OPTIMISTIC);
  List<DocLineAr> arLines = ac.getArLines();
  LOGGER.log(INFO, "Invoices from ar account {0}", arLines);
  int i = 0;
  List<DocLineArRec> arDocLines = new ArrayList<>();
  for(DocLineAr line : arLines){
   LOGGER.log(INFO, "Ar line bank {0} ", line.getPaymntBank());
   LOGGER.log(INFO, "AR line id {0} ", line.getId());
   DocLineArRec lineRec = this.buildDocLineArRec(line, arAccount, false);
   arDocLines.add(lineRec);
 /*  List<DocLineGL> recLines = line.getReconiliationLines();
   LOGGER.log(INFO, "recLines {0}", recLines);
   if(recLines != null && !recLines.isEmpty()){
    if(!(line.isClearedLine() || line.isClearingLine())){
     if(line.getDocAmount() != 0 && line.getHomeAmount() != 0){
      DocLineArRec lineRec = this.buildDocLineArRec(line, arAccount, false);
      arDocLines.add(lineRec);
     }
    }
   }
   */
   
  }
  
  return arDocLines;
 }

 
 public DocFiRec postSalesCreditNote(DocFiRec crn, List<SalesPartFiLineRec> lines, 
         List<DocLineArRec> allocated,
         List<RestrictFundBalance> fundBals,List<DocVatSummary> docVatSum, 
         VatReturnRec vatReturn,  String source)  throws BacException{
  LOGGER.log(INFO,"postSalesCreditNote called with crn {0} lines {1} fundbals {2} vatsum {3} "
          + "vatReturn {4} allocated {5}" , new Object[]{crn,lines,fundBals,docVatSum,vatReturn,
          allocated});
  
  LOGGER.log(INFO, "DocumentDM.postSaleInvoice called with invoice {0}  s", 
          new Object[]{crn,});
  LOGGER.log(INFO, "Fund bals {0}", fundBals);
  LOGGER.log(INFO,"docVatSum {0}",docVatSum);
  //crn.setCreatedBy(user);
  //crn.setCreateOn(new Date());
 boolean found;
  if(!trans.isActive()){
   trans.begin();
  }
  List<DocLineBaseRec> docLinesRec = crn.getDocLines();
  List<FiPeriodBalance> glBalances = null;
  
  User usr = em.find(User.class, crn.getCreatedBy().getId(), OPTIMISTIC);
  Date crDate = new Date();
  CompanyBasicRec compRec = docLinesRec.get(0).getComp();
  CompanyBasic comp = em.find(CompanyBasic.class, compRec.getId(), OPTIMISTIC);
  // build document header
  DocFi docHdr = this.buildDocFi(crn, source);
  docHdr.setCompany(comp);
  docHdr.setCreateOn(crDate);
  crn.setCreateOn(crDate);
  
  if(docLinesRec == null){
   throw  new BacException("No AR account line");
  }
  
  List<LineTypeRuleRec> lineTypes = sysBuff.getLineTypeRules();
  ListIterator<LineTypeRuleRec> lineTypeLi = lineTypes.listIterator();
  LineTypeRule glLineType = null; // = new LineTypeRule();
  LineTypeRule arLineType = null;
  LineTypeRuleRec glLineTypeRec = null; 
  LineTypeRuleRec arLineTypeRec = null;
  List<DocLineBase> docLines = null;
  found = false;
  while(lineTypeLi.hasNext() ){
   LineTypeRuleRec lRec = lineTypeLi.next();
   if(lRec.getLineCode().equalsIgnoreCase("GL")){
    glLineTypeRec = lRec;
    glLineType = em.find(LineTypeRule.class, lRec.getId(), OPTIMISTIC);
   }
   if(lRec.getLineCode().equalsIgnoreCase("AR")){
    arLineType = em.find(LineTypeRule.class, lRec.getId(), OPTIMISTIC);
    arLineTypeRec = lRec;
   }
  }
  if(glLineType == null ||arLineType == null ){
   throw new BacException("Could not find line types");
  }
  
  // get posting types for SL invoice
  List<PostTypeRec> postTypes = sysBuff.getPostTypes();
  LOGGER.log(INFO, "Post Types list {0}", postTypes);
  ListIterator<PostTypeRec> postTypesLI = postTypes.listIterator();
  PostType glDrPostType = sysBuff.getPostTypeForPCode("glDr");
  PostType glCrPostType = sysBuff.getPostTypeForPCode("glCr");;
  PostType arCrnPostType = sysBuff.getPostTypeForPCode("arCrn");
  PostTypeRec glDrPostTypeRec = sysBuff.getPostTypeForCode("glDr");
  PostTypeRec glCrPostTypeRec = sysBuff.getPostTypeForCode("glCr");
  PostTypeRec arCrnPostTypeRec = sysBuff.getPostTypeForCode("arCrn");
  //AR Credit Note
  found = false;
  ListIterator<DocLineBaseRec> docLnLi = docLinesRec.listIterator();
  while(docLnLi.hasNext()){
   DocLineBaseRec ln = docLnLi.next();
  
   LOGGER.log(INFO, "6281 Doc line class {0}", ln.getClass().getSimpleName());
   LOGGER.log(INFO, "Doc line num {0}", ln.getLineNum());
   LOGGER.log(INFO, "line post debit {0}", ln.getPostType().isDebit());
   if(StringUtils.equals(ln.getClass().getSimpleName(), "DocLineArRec")){
    // AR line
    ln.setLineType(arLineTypeRec);
    ln.setPostType(arCrnPostTypeRec);
    
   }else if( StringUtils.equals(ln.getClass().getSimpleName(), "DocLineGLRec")){
    // AR line
    ln.setLineType(arLineTypeRec);
    ln.setPostType(arCrnPostTypeRec);
    
   }
   
  }
  docHdr = addARLine(docHdr,docLinesRec,fundBals,comp,usr,crDate,source);
  LOGGER.log(INFO, "Doc has {0} lines", docHdr.getDocLines().size());
  LOGGER.log(INFO, "docHdr inv line id {0}", docHdr.getDocInvoiceAr().getId());
  DocInvoiceArRec docInvArRec = buildDocInvoiceArRec(docHdr.getDocInvoiceAr());
  crn.setDocInvoiceAr(docInvArRec);
  //DocFI docFi,List<SalesPartFiLineRec> lines, CompanyBasic comp, 
  //        User usr, Date crDate,String view, DocLineArRec arLineRec, DocFiRec docRec, 
  //        LineTypeRule glLineType, PostType glPostType
  LOGGER.log(INFO,"docLinesRec size {0}",docLinesRec);
  LOGGER.log(INFO,"glDrPostType {0}",glDrPostType);
  docHdr = this.addArGLLines(docHdr, lines, comp, usr, crDate, source, docLinesRec, crn, 
          glLineType,
          glDrPostType,vatReturn,source);
   LOGGER.log(INFO,"docLines after add GL lines size {0}",docHdr.getDocLines().size());       
  // add invoice number to ext ref
  crn.setPartnerRef(docHdr.getDocInvoiceAr().getInvoiceNumber());
  LOGGER.log(INFO, "External CRN number {0}", crn.getPartnerRef());
  
  // add gl lines to doc
 // docHdr = this.addArGLLines(docHdr, lines, comp, usr, crDate, source, docLinesRec, 
 //         crn, glLineType, glCrPostType, vatReturn);
  LOGGER.log(INFO,"docLines after add GL lines size {0}",docHdr.getDocLines().size());
  // add VAT lines if any to the document
 
  
 if(docHdr.getDocLines() != null){
  for(DocLineBase ln: docHdr.getDocLines() ){
   // set GL balances 
   
   LOGGER.log(INFO,"Line num {0}",ln.getLineNum());
   LOGGER.log(INFO,"Line class {0}",ln.getClass().getSimpleName());
   LOGGER.log(INFO,"Line type {0}",ln.getLineType().getLineCode());
   LOGGER.log(INFO,"Line debit {0}",ln.getPostType().isDebit());
   LOGGER.log(INFO,"Line home Amount {0}",ln.getHomeAmount());
   
   if(StringUtils.equals(ln.getClass().getSimpleName(), "DocLineAR")){
    // AR line so need to get GL reconciliation line and then GL balance
    DocLineAr lnAr = (DocLineAr)ln;
    List<DocLineGl> glLns = lnAr.getReconiliationLines();
    if(glLns != null){
     for(DocLineGl glLn: glLns){
      glLn.setLineType(glLineType);
      glLn.setPostType(glCrPostType);
      glBalances = buildDocLineGlAcBalance(glLn, glBalances, docHdr);
     }
    }
   
   }else if(StringUtils.equals(ln.getClass().getSimpleName(), "DocLineGL")){
    DocLineGl glLn = (DocLineGl)ln;
    glLn.setLineType(glLineType);
    glLn.setPostType(glDrPostType);
    glBalances = buildDocLineGlAcBalance(glLn, glBalances, docHdr);
   }
   LOGGER.log(INFO, "glBalances {0}", glBalances);
  }
 } 
  
  LOGGER.log(INFO,"docVatSum {0}",docVatSum);
  
  if(docVatSum != null){
  
   }
  LOGGER.log(INFO, "CRN allocation {0}", allocated);
  if(allocated != null && !allocated.isEmpty()){
   List<DocLineBase> allocDocLines = docHdr.getDocLines();
   ListIterator<DocLineBase> allocDocLinesLi = allocDocLines.listIterator();
   boolean arLineFound = false;
   while(allocDocLinesLi.hasNext() && !arLineFound){
    DocLineBase allocDocLineBase = allocDocLinesLi.next();
    LOGGER.log(INFO, "allocDocLineBase {0}", allocDocLineBase);
    if(allocDocLineBase.getClass().getCanonicalName().endsWith("DocLineAR")){
     DocLineAr docLineAr = (DocLineAr)allocDocLineBase;
     docHdr = allocateArCredit(crn, docHdr, docLineAr, allocated, crn.getCreatedBy(), source);
     arLineFound = true;
    }
   }
  }
  
  LOGGER.log(INFO, "crn doc id {0}", crn.getDocInvoiceAr().getId());
  salesDM.addSalesInfoRecordPvt(docHdr, "CRN", source); 
  /*if(crn.getId() != null){
   trans.commit();
  }else{
   trans.rollback();
  }*/
  return crn;
 }

 public void updateSlInvCrnPdf(DocInvoiceArRec inv) throws BacException {
  LOGGER.log(INFO, "DocDM.updateSlInvCrnPdf called with {0}", inv.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  DocInvoiceAr invoiceAr = em.find(DocInvoiceAr.class, inv.getId(), OPTIMISTIC);
  if(invoiceAr ==null){
   trans.rollback();
   throw new BacException("Could not find DocInvoiceAr to add pdf");
  }
  invoiceAr.setInvCrnPdf(inv.getInvoicePdf());
  LOGGER.log(INFO, "pdf added to inv/crn line");
  trans.commit();
 }

 private DocFiRec addDocLines(DocFiRec docFiRec, List<DocLineBase> docLinesBase, UserRec usr, String pg ){
  LOGGER.log(INFO, "DocDM.addDocLines called with lines {0}",docLinesBase);
  List<DocLineBaseRec> docRecLines = new ArrayList<>();
     
     ListIterator<DocLineBase> docLinesBaseLi = docLinesBase.listIterator();
     while(docLinesBaseLi.hasNext()){
      LOGGER.log(INFO, "loop over docLinesBaseLi ");
      DocLineBase docLineBase = docLinesBaseLi.next();
      LOGGER.log(INFO,"add doc line with id {0}",docLineBase.getId());
      LOGGER.log(INFO,"add doc line class {0}",docLineBase.getClass().getSimpleName());
      if(docLineBase.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
       // Ar line
       
       DocLineAr docLineAr = (DocLineAr)docLineBase; 
       LOGGER.log(INFO, "Add AR line  with id {0}", docLineAr.getId());
       ArAccountRec arAcRec = this.arAccountDM.buildArAccountRecPvt(docLineAr.getArAccount(),usr,pg);
       LOGGER.log(INFO, "addDoclines arAcRec name {0}", arAcRec.getArAccountName());
       DocLineArRec docLineArRec = buildDocLineArRec(docLineAr, arAcRec,true);
       LOGGER.log(INFO, "docLineArRec post type {0}", docLineArRec.getPostType());
       if(docLineAr.isClearingLine() || 
               (docLineAr.getClearingLineForLines() != null && 
               docLineAr.getClearingLineForLines().size() > 0)){
        // add clearing docs
        LOGGER.log(INFO,"Clearing status {0}",docLineAr.isClearingLine());
        List<DocLineBaseRec> clearedLines = docLineArRec.getClearingLineForLines();
        for(DocLineBase clearedBseLn : docLineAr.getClearingLineForLines()){
         DocLineAr clearedLineAr = (DocLineAr)clearedBseLn;
         LOGGER.log(INFO, "Cleared line DB id {0}", clearedLineAr.getId());
         DocLineArRec clearedLineArRec = buildDocLineArRec(clearedLineAr, arAcRec,true);
         LOGGER.log(INFO, "Cleared line rec id {0}", clearedLineArRec.getId());
         if(clearedLines == null){
          clearedLines = new ArrayList<>();
         }
         clearedLines.add(clearedLineArRec);
        }
        docLineArRec.setClearingLineForLines(clearedLines);
        LOGGER.log(INFO, "Cleared doc rec lines {0}", clearedLines.size());
       }
       if(docLineAr.getClearedByLine()!= null){
        DocLineBase clrdBy = docLineAr.getClearedByLine();
        LOGGER.log(INFO, "doclin.id {0} cleared by ln {1}", new Object[]{docLineAr.getId(),clrdBy});
        DocLineBaseRec  clearedByRec = this.buildDocLineBaseRec(clrdBy, null);
        clearedByRec.setDocHeaderBase(docFiRec);
        clearedByRec.setComp(docFiRec.getCompany());
        docLineArRec.setClearedByLine(clearedByRec);
       }
       LOGGER.log(INFO, "reconcil lines {0}", docLineAr.getReconiliationLines());
       docRecLines.add(docLineArRec);
      }
      if(docLineBase.getClass().getSimpleName().equalsIgnoreCase("DocLineGL")){
       // GL lines
       DocLineGl docLineGl = (DocLineGl)docLineBase;
       LOGGER.log(INFO, "Add GL line  with id {0}", docLineGl.getId());
       //throw new BacException("ad gl line");
       DocLineGlRec docLineGlRec = this.buildGlDocLineRec(docLineGl, usr,pg);
       docRecLines.add(docLineGlRec);
      }
     }
     docFiRec.setDocLines(docRecLines);
     LOGGER.log(INFO, "addDocLines returns doc number {0} with {1} lines ", new Object[]{docFiRec.getDocNumber(),
      docFiRec.getDocLines().size()});
  return docFiRec;
 }
 
 public List<DocFiRec> getAllFiDocsForCompany(CompanyBasicRec comp, boolean inclLines, UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "DocDM.getAllFiDocsForCompany called with comp {0}", comp.getReference());
  int listSize = this.sysBuff.getMaxComplSize();
  TypedQuery q = em.createNamedQuery("fiDocsForCompAll",DocFi.class);
  //q.setMaxResults(listSize);
  q.setParameter("compId", comp.getId());
  List rs = q.getResultList();
  LOGGER.log(INFO, "rs returned {0}", rs);
  LOGGER.log(INFO, "listSize {0}", listSize);
  ListIterator<DocFi> docsLi  = rs.listIterator();
  List<DocFiRec> resList = new ArrayList<>();
  while(docsLi.hasNext()){
   DocFi docFi = docsLi.next();
   LOGGER.log(INFO, "Process Doc num {0}", docFi.getDocNumber());
   //if(docBase.getClass().getCanonicalName().endsWith("DocFI"))
    
    DocFiRec docFiRec = this.buildDocFiRec(docFi);
    if(docFi.getDocInvoiceAr() != null){
    DocInvoiceAr inv = docFi.getDocInvoiceAr();
    // if(inv != null){
      //does doc have an Sales invoice
      DocInvoiceArRec invRec = this.buildDocInvoiceArRec(inv);
      docFiRec.setDocInvoiceAr(invRec);
     }
    if(inclLines){ 
     LOGGER.log(INFO, "Add doc lines to doc");
     docFiRec = this.addDocLines(docFiRec, docFi.getDocLines(),usr,pg);
    }
    resList.add(docFiRec);
   
   
  }
  LOGGER.log(INFO, "FI Docs returned from DocDM {0}", resList);
  return resList;
 }

private List<DocFi>  getFiDocsBySelOptPvt(FiDoclSelectionOpt selOpt){
 List<DocFi> rs ;
 Query q = null;
 String selTy = null;
 if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
 selTy = "comp";
 }  else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docDt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() != null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "entryDt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() != null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "pstDt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() != null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docType";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() != null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "entBy";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && !StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docTxt";
 }else if(selOpt.getComp() != null & selOpt.getDocDateFrom() == null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && !StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "ptnr";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() != null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docPostDt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() != null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docEntryDt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() != null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docDtEntBy";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() != null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docDtType";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && !StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docDtHdrtxt";
 } else if(selOpt.getComp() != null & selOpt.getDocDateFrom() != null && selOpt.getEntryDateFrom() == null 
    && selOpt.getPostDateFrom() == null && selOpt.getDocType() == null && StringUtils.isBlank(selOpt.getDocText())
    && selOpt.getEnteredBy() == null && !StringUtils.isBlank(selOpt.getPtnrRef())){
  selTy = "docDtPtnrtxt";
 } 
 
  
     

 LOGGER.log(INFO, "selTy {0}", selTy);
 LOGGER.log(INFO, "Opts comp {0} doc dt {1} entry dt {2} post dt {3} doc ty {4} doc Text {5} "
    + "entered by id {6} ptnr ref {7}", new Object[]{selOpt.getComp(), selOpt.getDocDateFrom(),
    selOpt.getEntryDateFrom(), selOpt.getPostDateFrom(), selOpt.getDocType(), selOpt.getDocText(),
    selOpt.getEnteredBy(), selOpt.getPtnrRef() });
 if(selTy == null){
  LOGGER.log(INFO, "user dynamic query");
  String queryStr = "Select d from DocFi d where d.company.id = "+String.valueOf(selOpt.getComp().getId());
  String startDate;
  String endDate;
  if(selOpt.getDocDateFrom() != null){
   startDate = GenUtilServer.jdbcDate(selOpt.getDocDateFrom());
   endDate = GenUtilServer.jdbcDate(selOpt.getDocDateTo());
   LOGGER.log(INFO, "startDate {0}", startDate);
   queryStr += " and d.documentDate between "+startDate +" and "+ endDate;
  }
  if(selOpt.getEntryDateFrom() != null){
   startDate = GenUtilServer.jdbcDate(selOpt.getEntryDateFrom());
   endDate = GenUtilServer.jdbcDate(selOpt.getEntryDateTo());
   queryStr += " and d.createOn between "+startDate +" and "+ endDate;
  }
  if(selOpt.getPostDateFrom() != null){
   startDate = GenUtilServer.jdbcDate(selOpt.getPostDateFrom());
   endDate = GenUtilServer.jdbcDate(selOpt.getPostDateTo());
   queryStr += " and d.postingDate between "+startDate +" and "+ endDate;
  }
  if(selOpt.getDocType() != null){
   queryStr += " and d.docType.id  =" + selOpt.getDocType().getId() ;
  }
  if(!selOpt.getDocText().isEmpty()){
   queryStr += " and d.docHdrText like "+ GenUtilServer.jdbcStringValue(selOpt.getDocText()) ;
  }
  
  if(selOpt.getEnteredBy() != null){
   queryStr += " and d.createdBy.id =" + selOpt.getEnteredBy().getId() ;
  }
  
  if(!selOpt.getPtnrRef().isEmpty()){
   queryStr += "  and   d.partnerRef like "+ GenUtilServer.jdbcStringValue(selOpt.getPtnrRef());
  }
  LOGGER.log(INFO, "queryStr {0}", queryStr);
  //q = em.createQuery(queryStr);
  q = em.createNamedQuery(queryStr, DocFi.class);
 }else{
 LOGGER.log(INFO, "user named query");
 switch(selTy){
  case "comp":
   q = em.createNamedQuery("fiDocsForCompAll");
   q.setParameter("compId", selOpt.getComp().getId());
  break;
  case "docDt":
   q = em.createNamedQuery("fiDocsComp1");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   break;
  case "entryDt":
   LOGGER.log(INFO, "selOpt.getEntryDateTo {0}", selOpt.getEntryDateTo());
   
   q = em.createNamedQuery("fiDocsComp2");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("entrySt", selOpt.getEntryDateFrom());
   q.setParameter("entryEnd", selOpt.getEntryDateTo());
   break;
  case "pstDt":
   q = em.createNamedQuery("fiDocsComp3");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("postSt", selOpt.getPostDateFrom());
   q.setParameter("postEnd", selOpt.getPostDateTo());
   break;
  case "docType":
   q = em.createNamedQuery("fiDocsComp4");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docTyId", selOpt.getDocType().getId());
   break;
  case "entBy":
   q = em.createNamedQuery("fiDocsComp5");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("createdById", selOpt.getEnteredBy().getId());
   break;
  case "docTxt":
   q = em.createNamedQuery("fiDocsComp6");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("hdrTxt", selOpt.getDocText());
   break;
  case "ptnr":
   q = em.createNamedQuery("fiDocsComp7");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("ref", selOpt.getPtnrRef());
   break;
  case "docPostDt":
   q = em.createNamedQuery("fiDocsComp8");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("pstSt", selOpt.getPostDateFrom());
   q.setParameter("pstEnd", selOpt.getPostDateTo());
   break;
  case "docEntryDt":
   q = em.createNamedQuery("fiDocsComp9");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("entrySt", selOpt.getEntryDateFrom());
   q.setParameter("entryEnd", selOpt.getEntryDateTo());
   break;
  case "docDtEntBy":
   q = em.createNamedQuery("fiDocsComp10");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("createdById", selOpt.getEnteredBy().getId());
   break;
  case "docDtType":
   q = em.createNamedQuery("fiDocsComp11");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("docTy", selOpt.getDocType().getId());
   break;
  case "docDtHdrtxt":
   q = em.createNamedQuery("fiDocsComp12");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("hdrTxt", selOpt.getDocText());
   break;
  case "docDtPtnrtxt":
   q = em.createNamedQuery("fiDocsComp13");
   q.setParameter("compId", selOpt.getComp().getId());
   q.setParameter("docSt", selOpt.getDocDateFrom());
   q.setParameter("docEnd", selOpt.getDocDateTo());
   q.setParameter("ref", selOpt.getPtnrRef());
   break;
 }
 }
 
 if(q == null){
  return null;
 }
 rs = q.getResultList();
 /**if(rs == null || rs.isEmpty()){
  LOGGER.log(INFO, "No docs for comp id", trans);
  return null;
 } */
 return rs;
}

public List<DocFi> getFiDocsBySelOptDM(FiDoclSelectionOpt selOpt){
 List<DocFi> rs = this.getFiDocsBySelOptPvt(selOpt);
 LOGGER.log(INFO, "Docs found {0}", rs);
 return rs;
}

public List<DocFiRec> getFiDocsBySelOpt(FiDoclSelectionOpt selOpt){
 
 List<DocFi> rs = this.getFiDocsBySelOptPvt(selOpt);
 
 
 List<DocFiRec> retDocs = new ArrayList<>();
 for(Object o: rs){
  DocFi d = (DocFi)o;
  DocFiRec rec = this.buildDocFiRec(d);
  retDocs.add(rec);
 }
 return retDocs;
}
 public List<DocFiTemplAccrPrePayRec> getFiTemplateDocsAllActive(TemplSelectOption opt){
 LOGGER.log(INFO, "getFiTemplateDocs called with opt {0}", new Object[]{opt.getDocDateFrom(),
    opt.getNextDateTo(), opt.getNextDateFrom(), opt.getNextDateTo()
 });
 List<DocFiTemplAccrPrePayRec> retList = new ArrayList<>();
 Query q;
 q = em.createNamedQuery("allFiTemplAct");
 q.setParameter("compId", opt.getComp().getId());
 List rs = q.getResultList();
 for(Object o:rs){
  DocFiTemplAccrPrePayRec rec = this.buildDocFiTemplateRec((DocFiTemplAccrPrePay)o);
  retList.add(rec);
 }
 LOGGER.log(INFO, "Query returns {0}", rs.size());
 
 return retList;
} 
 
 
 public List<DocTypeRec> getAllDocTypes() throws BacException {
  LOGGER.log(INFO, "DocDM.getAllDocTypes called");
  List<DocTypeRec> docTypeList = new ArrayList<>();
  Query q = em.createNamedQuery("allDocTypes");
  List rs = q.getResultList();
  ListIterator li = rs.listIterator();
  while(li.hasNext()){
   DocType docType = (DocType)li.next();
   DocTypeRec docTypeRec = this.buildDocTypeRec(docType);
   docTypeList.add(docTypeRec);
  }
  return docTypeList;
 }

 public DocFiRec getFullDocDetails(DocFiRec doc, UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "DocDM.getFullDocDetails called with doc id {0}", doc.getId());
  if(!trans.isActive()){
   trans.begin();
  }
  DocFi docDb = em.find(DocFi.class, doc.getId(), OPTIMISTIC);
  List<DocLineBase> docLinesDb = docDb.getDocLines();
  LOGGER.log(INFO, "docLinesDb {0}", docLinesDb);
  
  doc = getDocLines(doc);
  
  trans.rollback();
  return doc;
 }
 
 
/**
 * Returns list of documents for company with document numbers greater than the input value. 
 * @param comp
 * @param docNum
 * @param inclLines
 * @return r
 */
 public List<DocFiRec> getDocFiCompByDocNumPart(CompanyBasicRec comp, long docNum, 
         boolean inclLines, UserRec usr, String pg) {
  LOGGER.log(INFO, "DocDM.getDocFiCompByDocNumPart called with comp {0} doc num {1} lines {2}", 
          new Object[]{comp,docNum,inclLines});
  int listSize = this.sysBuff.getMaxComplSize();
  LOGGER.log(INFO, "Max list size {0}", listSize);
  TypedQuery q = em.createNamedQuery("docCompDocNumPartAll", DocBase.class);
  q.setMaxResults(listSize);
  q.setParameter("compId", comp.getId());
  q.setParameter("docNum", docNum);
  List<DocBase> rs = q.getResultList();
  LOGGER.log(INFO, "Docs found {0}", rs.size());
  ListIterator<DocBase> docsLi  = rs.listIterator();
  List<DocFiRec> resList = new ArrayList<>();
  while(docsLi.hasNext()){
   DocBase docBase = docsLi.next();
   LOGGER.log(INFO, "Doc class name {0}", docBase.getClass().getSimpleName());
   if(StringUtils.equals(docBase.getClass().getSimpleName(), "DocFi")){
    DocFi docFi = (DocFi)docBase;
    LOGGER.log(INFO, "getDocFiCompByDocNumPart docFI lines {0}", docFi.getDocLines());
    DocFiRec docFiRec = this.buildDocFiRec(docFi);
    LOGGER.log(INFO, "getDocFiCompByDocNumPart doc rec lines {0}", docFiRec.getDocLines());
    DocInvoiceAr inv = docFi.getDocInvoiceAr();
     if(inv != null){
      //does doc have an Sales invoice
      DocInvoiceArRec invRec = this.buildDocInvoiceArRec(inv);
      docFiRec.setDocInvoiceAr(invRec);
     }
    if(inclLines){ 
     LOGGER.log(INFO, "Add doc lines to doc");
     docFiRec = addDocLines(docFiRec, docFi.getDocLines(),usr, pg);
    }
    resList.add(docFiRec);
   }
   
  }
  return resList;
 }
 
 
 public DocLineBaseRec getDocFiForLine(DocLineBaseRec ln){
  LOGGER.log(INFO, "getDocFiForLine called with line {0}", ln.getId());
  Query q = em.createNamedQuery("doc4Line");
  q.setParameter("lineId", ln.getId());
  Object o = q.getSingleResult();
  DocFiRec docRec = this.buildDocFiRec((DocFi)o);
  LOGGER.log(INFO, "docRec {0}", docRec.getId());
 // ln.setDocFi(docRec);
  
  return ln;
  
 }
 public List<GiftAidScheduleLine> getGiftAidForFisYearPeriod(int year, int periodFrom, 
         int periodTo) throws BacException {
  Query q = em.createNamedQuery("giftAid");
  q.setParameter(1, year);
  q.setParameter(2, periodFrom);
  q.setParameter(3, periodTo);
  List rs = q.getResultList();
  
  ListIterator li = rs.listIterator();
  List<GiftAidScheduleLine> giftAidList = new ArrayList<>();
  while(li.hasNext()){
   DocFi doc = (DocFi)li.next();
   
   LOGGER.log(INFO, "docLine {0}", doc);
   LOGGER.log(INFO, "date {0}",doc.getDocumentDate());
   LOGGER.log(INFO, "Doc Lines {0}", doc.getDocLines());
   GiftAidScheduleLine giftAidLine = new GiftAidScheduleLine();
   giftAidLine.setDonationDate(doc.getDocumentDate());
   List<DocLineBase> docLines = doc.getDocLines();
   ListIterator<DocLineBase> docLinesLi = docLines.listIterator();
   while(docLinesLi.hasNext()){
    DocLineBase line = docLinesLi.next();
    LOGGER.log(INFO, "line class {0}", line.getClass().getSimpleName());
    if(line.getClass().getSimpleName().equals("DocLineAR") ){
     giftAidLine.setId(line.getId());
     PartnerBase ptnr = ((DocLineAr)line).getArAccount().getArAccountFor();
     LOGGER.log(INFO, "ptnr {0}", ptnr.getClass().getSimpleName());
     giftAidLine.setDonorRef(ptnr.getRef());
     if(ptnr.getClass().getSimpleName().equals("PartnerCorporate")){
      giftAidLine.setDonorName(((PartnerCorporate)ptnr).getTradingName());
     }else{
      String name = ((PartnerPerson)ptnr).getFirstName() + " "+((PartnerPerson)ptnr).getFamilyName();
      giftAidLine.setDonorName(name);
     }
     if(ptnr.getDefaultAddress() == null){
      giftAidLine.setAddress("Not known");
     }else{
      giftAidLine.setAddress(ptnr.getDefaultAddress().getStreetFormatted());
      giftAidLine.setPostCode(ptnr.getDefaultAddress().getPostCode());
     }
     giftAidLine.setDonationAmount(line.getHomeAmount());
    }
   }
   
   giftAidList.add(giftAidLine);
   
  }
  LOGGER.log(INFO, "Gift Aid list returned {0}", giftAidList);
  return giftAidList;
 }

 public List<DocFiPartialRec> getDocFiPartialRecForComp(CompanyBasicRec comp){
 
 List<DocFiPartialRec> retList = new ArrayList<>();
 
 Query q = em.createNamedQuery("glPartDocForComp");
 q.setParameter("compId", comp.getId());
 List rs = q.getResultList();
 ListIterator li = rs.listIterator();
 while(li.hasNext()){
  DocFiPartial doc = (DocFiPartial)li.next();
  DocFiPartialRec rec = this.buildDocFiPartialRec(doc);
  retList.add(rec);
 }
 
 return retList;
 }
 
 public DocFiPartialRec getDocFiPartialRecLines(DocFiPartialRec rec){
  LOGGER.log(INFO, "getDocFiPartialRecLines called with {0}", rec);
  DocFiPartial doc = em.find(DocFiPartial.class, rec.getId(), OPTIMISTIC);
  
  List<DocLineBasePartial> lines = doc.getDocLines();
  LOGGER.log(INFO, "doc.getDocLines() returns {0}", lines);
  
  if(lines == null || lines.isEmpty()){
   return rec;
  }
  List<DocLineGlPartialRec> recLines = new ArrayList<>();
  for(DocLineBasePartial ln: lines){
   LOGGER.log(INFO, "getDocFiPartialRecLines doc amnt {0}", ((DocLineGlPartial)ln).getDocAmount());
   DocLineGlPartialRec glLnRec = this.buildDocLineGlPartialRec((DocLineGlPartial)ln);
   recLines.add(glLnRec);
  }
  rec.setGlLines(recLines);
  return rec;
 }
 
 public List<DocLineBaseRec> getDocLinesForDoc(DocFiRec doc){
  
  Query q = em.createNamedQuery("lines4Doc");
  q.setParameter("docId", doc.getId());
  
  List rs = q.getResultList();
  
  if(rs != null){
   List<DocLineBaseRec> retList = new ArrayList<>();
   for(Object curr:rs){
    String lineClass = curr.getClass().getSimpleName();
    switch ( lineClass ) {
     case "DocLineGL":
      DocLineGlRec glLn = this.buildGlRecDocLine((DocLineGl)curr);
      retList.add(glLn);
      break;
     case "DocLineAP" :
      DocLineApRec apLn = this.buildDocLineApRec((DocLineAp)curr);
      retList.add(apLn);
      break;
     }
   }
    
    return retList;
   
  }
  return null;
 }
 
 public DocLineApRec getDocLineApReconLines(DocLineApRec apLine){
  LOGGER.log(INFO, "DocDM getDocLineApReconLines called with apLine {0}", apLine);
  if(apLine == null){
   return null;
  }
  
  Query q = em.createNamedQuery("glReconLinesForApLine");
  q.setParameter("apLineid", apLine.getId());
  List rs = q.getResultList();
  
  LOGGER.log(INFO, "rec lines rs {0}", rs);
  if (rs == null){
   return null;
  }
  List<DocLineGlRec> recLines = new ArrayList<>();
  for(Object curr:rs){
   DocLineGlRec currGl = this.buildGlRecDocLine((DocLineGl)curr);
   recLines.add(currGl);
  }
  apLine.setReconiliationLines(recLines);
  
  return apLine;
 }
 
 public DocLineArRec getDocLineArReconLines(DocLineArRec arLine){
  LOGGER.log(INFO, "DocDM getDocLineApReconLines called with apLine {0}", arLine);
  if(arLine == null){
   return null;
  }
  
  TypedQuery q = em.createNamedQuery("glReconLinesForArLine",DocLineGl.class);
  q.setParameter("arLineid", arLine.getId());
  List<DocLineGl> rs = q.getResultList();
  
  LOGGER.log(INFO, "rec lines rs {0}", rs);
  if (rs == null){
   return null;
  }
  List<DocLineGlRec> recLines = new ArrayList<>();
  for(DocLineGl curr:rs){
   DocLineGlRec currGl = this.buildGlRecDocLine(curr);
   recLines.add(currGl);
  }
  arLine.setReconiliationLines(recLines);
  
  return arLine;
 }
 public DocLineBaseRec getDocLineBaseRecPvt(DocLineBase ln){
  DocLineBaseRec lnRec = this.buildDocLineBaseRecPvt(ln);
  return lnRec;
 }
 
 public DocLineFiTemplGlRec getDocLineFiTemplGlRecAddlazy(DocLineFiTemplGlRec line){
  line = buildDocLineFiTemplGlAddLazyRec(line);
  return line;
 }
 
 public DocFiRec getDocLines(DocFiRec docRec){
  LOGGER.log(INFO, "DB getDocLines called with {0}", docRec);
  
  DocFi doc = em.find(DocFi.class, docRec.getId());
  List<DocLineBaseRec> baseRecLines = new ArrayList<>();
  LOGGER.log(INFO,"doc lines from getDocLines {0}",doc.getDocLines().size());
  for(DocLineBase lnBase : doc.getDocLines()){
   LOGGER.log(INFO, "lnBase class {0} id {1}", new Object[]{lnBase.getClass().getSimpleName(),lnBase.getId()});
   String lineClass = lnBase.getClass().getSimpleName();
  
   switch(lineClass){
    case "DocLineAp" :
     DocLineApRec lnAp = buildDocLineApRec((DocLineAp)lnBase);
     
     baseRecLines.add(lnAp);
     LOGGER.log(INFO, "AP Line id {0}", lnAp.getId());
     break;
    case "DocLineAr" :
     DocLineArRec lnAr = buildDocLineArRecMin((DocLineAr)lnBase);
     baseRecLines.add(lnAr);
     LOGGER.log(INFO, "AR Line id {0}", lnAr.getId());
     break;
    case "DocLineGl" :
     DocLineGlRec lnGl = buildGlRecDocLine((DocLineGl)lnBase);
     baseRecLines.add(lnGl);
     LOGGER.log(INFO, "GL Line id {0}", lnGl.getId());
     break;
   }
  }
  docRec.setDocLines(baseRecLines);
  
  //${foo.class.name}
  
  return docRec;
 }
 
 
 public List<DocFiRec> getDocsApForComp(CompanyBasicRec comp){
  
  Query q = em.createNamedQuery("docsFiApComp");
  q.setParameter("compId", comp.getId());
  
  List rs = q.getResultList();
  LOGGER.log(INFO, "getDocsApForComp rs {0}", rs);
  List<DocFiRec> retList = new ArrayList<>();
  for(Object curr:rs){
   DocFiRec rec = this.buildDocFiRec((DocFi)curr);
   retList.add(rec);
  }
  return retList;
 }
 
 public List<DocFiRec> getDocsApForComp(CompanyBasicRec comp, Long docNum){
  
  Query q = em.createNamedQuery("docsFiApDocNum");
  q.setParameter("compId", comp.getId());
  q.setParameter("docNum", docNum);
  
  List rs = q.getResultList();
  LOGGER.log(INFO, "getDocsApForComp gr Doc Num rs {0}", rs);
  List<DocFiRec> retList = new ArrayList<>();
  for(Object curr:rs){
   DocFiRec rec = this.buildDocFiRec((DocFi)curr);
   
   retList.add(rec);
  }
  return retList;
 }
 
 public List<DocFiRec> getDocsApForAcnt(CompanyBasicRec comp, ApAccountRec acntRec){
  Query q = em.createNamedQuery("docsFiByVend");
  q.setParameter("compId", comp.getId());
  q.setParameter("apAcntId", acntRec.getId());
  
  List rs = q.getResultList();
  LOGGER.log(INFO, "getDocsApForAcnt rs {0}", rs);
  List<DocFiRec> retList = new ArrayList<>();
  for(Object curr:rs){
   DocFiRec rec = this.buildDocFiRec((DocFi)curr);
   retList.add(rec);
  }
  return retList;
 }
 
 public DocFiTemplAccrPrePayRec getDocFiTemplateGlLines(DocFiTemplAccrPrePayRec tmplRec){
  LOGGER.log(INFO, "getDocFiTemplateGlLines called with {0}", tmplRec);
  if(!trans.isActive()){
   trans.begin();
  }
  DocFiTemplAccrPrePay tmpl = em.find(DocFiTemplAccrPrePay.class, tmplRec.getId(), OPTIMISTIC);
  LOGGER.log(INFO, "tmpl {0}", tmpl);
  List<DocLineFiTemplateRec> lineRecList = new ArrayList<>();
  List<DocLineTemplate> lines =  tmpl.getDocLines();
  LOGGER.log(INFO, "lines {0}", lines);
  ListIterator<DocLineTemplate> linesLi = lines.listIterator();
  while(linesLi.hasNext()){
   DocLineFiTemplGl ln = (DocLineFiTemplGl)linesLi.next();
   LOGGER.log(INFO, "ln {0}", ln.getClass().getSimpleName());
   DocLineFiTemplGlRec lnRec = this.buildDocLineFiTemplGlRec(ln, false);
   lineRecList.add(lnRec);
  }
  tmplRec.setDocLines(lineRecList);
  /*List<DocLineGlPartialRec> glLines = new ArrayList<>();
  for(DocLineBasePartial ln:lines){
   LOGGER.log(INFO, "ln {0}", ln.getClass().getSimpleName());
   DocLineGlPartial glLn = (DocLineGlPartial)ln;
   DocLineGlPartialRec glLnRec = this.buildDocLineGlPartialRec(glLn);
   glLines.add(glLnRec);
   }
  
   
   
   tmplRec.setGlLines(glLines);
   LOGGER.log(INFO, "Accrual lines returned {0}", glLines);
  
  */
  trans.commit();
  return tmplRec;
 }
 public DocFiRec postArReceiptSingle(DocFiRec doc, List<DocLineArRec> paidLines,Locale loc, 
          UserRec usr, String source) 
         throws BacException {
  LOGGER.log(INFO,"DocDM.postArReceiptSingle called with doc {0} paidLines {1} user {2} source {3}", 
          new Object[]{doc,paidLines,usr,source});
  if(doc == null ){
   throw new BacException("no Receipt doc");
  }
  if(doc.getDocLines().get(0) == null){
   throw new BacException("no Receipt line");
  }
  Date processDate = new Date();
  DocLineArRec receiptLine = (DocLineArRec)doc.getDocLines().get(0);
  double paid = receiptLine.getPaidAmount();
  double receiptAmount = receiptLine.getDocAmount();
  double unalloc = receiptAmount - paid;
  int numPayments = 0;
  if(paidLines != null && !paidLines.isEmpty()){
   numPayments = paidLines.size();
  }
  LOGGER.log(INFO, "Number of payments {0}", numPayments);
  LineTypeRuleRec glLineTyRec = this.sysBuff.getLineTypeRuleByCode("GL");
  LineTypeRule glLineTy = em.find(LineTypeRule.class, glLineTyRec.getId(), OPTIMISTIC);
  CompanyBasic comp = em.find(CompanyBasic.class, doc.getCompany().getId(), OPTIMISTIC);
  
  
  LOGGER.log(INFO, "Receipt {0} paid {1}", new Object[]{receiptLine.getDocAmount(),receiptLine.getPaidAmount()});
  
  PaymentTypeRec payType = receiptLine.getPayType();
  
  
  LOGGER.log(INFO, "Build fi doc ", unalloc);
  User crUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  DocFi recDoc = this.buildDocFi(doc,source);
  doc.setId(recDoc.getId());
  doc.setDocNumber(recDoc.getDocNumber());
  long lineNum = 0;
  
  //basic logic
  //if unallocated amount
  //  ar line and set reconciliation with no restricted fund
  // add to docFi
  
  //if there paid lines
  // loop over paid lines to calulcate the restricted fund paid props
  // create ar paid line and add to doc
  // create ar rec line per rectriced fund and add to cleared ar line
 
  
  // loop over each paid line 
  // set clearing doc = ar clearing line and clearing date
  // if remaining amount
  //   create ar invoice with paid line details and attach to paid line.
  
  if(unalloc != 0){
   LOGGER.log(INFO, "Unallocated amount");
   if(unalloc < 0){
    receiptLine.setPostType(sysBuff.getPostTypeForCode("arDr"));
    
   }else{
    receiptLine.setPostType(sysBuff.getPostTypeForCode("arCr"));
   }
   receiptLine.setLineType(sysBuff.getLineTypeRuleByCode("AR"));
   DocLineAr receiptLineDb = buildDocLineAr(receiptLine, recDoc, source);
   receiptLineDb.setDocNumber(recDoc.getDocNumber());
   lineNum++;
   receiptLineDb.setLineNum(lineNum);
   recDoc.getDocLines().add(receiptLineDb);
   // Add GL reconciliation a/c entry
   DocLineGlRec arReconLine = new DocLineGlRec();
   FiGlAccountCompRec glAc = receiptLine.getArAccount().getReconciliationAc();
   
   addSubLedGlRecLine(recDoc,receiptLineDb, glLineTy);
   arReconLine.setGlAccount(glAc);
  }
  LOGGER.log(INFO, "after post on-ac");
  if(numPayments != 0){
   LOGGER.log(INFO, "paid amount {0}", paid);
   
   List<DocLineAr> paidDocsDB = new ArrayList<>();
   List<RestrictedFundArDocBal> docFndBal = new ArrayList<>();
   List<RestrictedFundBal> paidFundSplit = new ArrayList<>();
   LOGGER.log(INFO, "paidLines {0}", paidLines);
   for(DocLineArRec pdLine : paidLines){
    LOGGER.log(INFO, "paidFundSplit.isEmpty() {0}", paidFundSplit.isEmpty());
    DocLineAr paidLn = em.find(DocLineAr.class, pdLine.getId(), OPTIMISTIC);
    
    //if(paidFundSplit.isEmpty()){
    paidFundSplit = this.getArLineFndBal(paidLn, paidFundSplit, docFndBal,paidDocsDB);
    LOGGER.log(INFO, "paidFundSplit amount {0}", paidFundSplit.get(0).getAmount());
    LOGGER.log(INFO, "docFndBal amount {0}", docFndBal.get(0).getAmount());
  //  }
    
   }
   // add AR line for total of the invoice amounts
   double totalClearingAmnt = 0;
   double unpaidAmnt = 0;
   for(DocLineAr ln : paidDocsDB){
    LOGGER.log(INFO, "ln post type code {0} debit {1}",new Object[]{
     ln.getPostType().getPostTypeCode(), ln.getPostType().isDebit()});
    if(ln.getPostType().isDebit()){
     totalClearingAmnt = totalClearingAmnt + ln.getDocAmount();
    }else{
     totalClearingAmnt = totalClearingAmnt - ln.getDocAmount();
    }
    LOGGER.log(INFO, "totalClearingAmnt {0}", totalClearingAmnt);
    LOGGER.log(INFO, "Paid amount {0} for ln",paid);
    LOGGER.log(INFO, "unPaid doc id {0}",ln.getDocAmount() - ln.getPaidAmount());
    
   }
   
   if(totalClearingAmnt > 0){
    receiptLine.setDocAmount(totalClearingAmnt);
    receiptLine.setPostType(sysBuff.getPostTypeForCode("arPymnt"));
   }else{
    receiptLine.setDocAmount(totalClearingAmnt * -1);
    receiptLine.setPostType(sysBuff.getPostTypeForCode("arRfnd"));
   }
   
   receiptLine.setClearingLine(true);
   receiptLine.setLineType(sysBuff.getLineTypeRuleByCode("AR"));
   LOGGER.log(INFO, "AR line type", receiptLine.getLineType().getLineCode());
   DocLineAr receiptLineDb = buildDocLineAr(receiptLine, recDoc,source);
   receiptLineDb.setDocNumber(recDoc.getDocNumber());
   
   lineNum++;
   receiptLineDb.setLineNum(lineNum);
   recDoc.getDocLines().add(receiptLineDb);
   
   addSubLedGlRecLnFnds(receiptLineDb, paidFundSplit, recDoc, glLineTy, loc, crUsr, doc.getCreateOn());
   LOGGER.log(INFO, "reconLines {0}", receiptLineDb.getReconiliationLines());
   // mark lines as paid
   
   for(DocLineAr ln : paidDocsDB){
    ln.setClearingDate(doc.getCreateOn());
    ln.setClearedByLine(receiptLineDb);
    ln.setClearedLine(true);
   }
   
  //if(paid < totalClearingAmnt){
    LOGGER.log(INFO, "Check for part payment");
    for(DocLineArRec arLn : paidLines){
     LOGGER.log(INFO, "arLn.getDocAmount() {0} arLn.getPaidAmount() {1}", new Object[]{
      arLn.getDocAmount(),arLn.getPaidAmount()});
     
     if(arLn.getPaidAmount() != arLn.getDocAmount()){
      double unPaid = arLn.getDocAmount() - arLn.getPaidAmount();
      if(unPaid < 0){
       unPaid = unPaid * -1;
       
      }
      LOGGER.log(INFO, "Unpaid {0}", arLn.getDocAmount() - arLn.getPaidAmount());
      // generate AR line for current line 
      DocLineAr arDbln = em.find(DocLineAr.class, arLn.getId(), OPTIMISTIC);
      DocFi pdLineDoc = arDbln.getDocFi();
      lineNum++;
      DocLineAr arUnPaidDbln = buildDocLineArPtPay(arDbln, unPaid, pdLineDoc,recDoc,
              lineNum, comp,crUsr, processDate);
      arUnPaidDbln.setOriginalLinePtpaid(arDbln);
      arDbln.setPartPaymentLine(arUnPaidDbln);
      LOGGER.log(INFO, "Part paid line id {0}",arUnPaidDbln.getId());
      LOGGER.log(INFO, "arUnPaidDbln orig line id {0}", arUnPaidDbln.getOriginalLinePtpaid().getId());
      LOGGER.log(INFO, "arDbln ptpay line id {0}", arDbln.getPartPaymentLine().getId());
      
      // calculate  the restricted balance for the part paid line.
      List<DocLineGl> arDocRecLines = arDbln.getReconiliationLines();
      int numLines = arDocRecLines.size();
      List<RestrictedFundBal> fndBals = new ArrayList<>();
      
      for(DocLineGl recLn : arDocRecLines){
       RestrictedFundBal fndBal = new RestrictedFundBal();
       fndBal.setAmount(recLn.getDocAmount());
       if(recLn.getRestrictedFund() != null){
        fndBal.setFund(recLn.getRestrictedFund());
       }
       double prop = arDbln.getDocAmount() / recLn.getDocAmount();
       fndBal.setProportion(prop);
       fndBals.add(fndBal);
      }
      
      addSubLedGlRecLnFnds(arUnPaidDbln, fndBals, recDoc, glLineTy,loc, crUsr, doc.getCreateOn());
      
      
     }
    }
   
  }
  
  
  // Bank Entry
  FiGlAccountCompRec bnkGlAc = receiptLine.getPayType().getGlBankAccount();
  LOGGER.log(INFO, "receiptLine {0} paytype {1} paytype id {2}", new Object[]{receiptLine,receiptLine.getPayType(),receiptLine.getPayType().getId()});
  LOGGER.log(INFO, "bank gl Ac {0}", bnkGlAc);
  BankAccountCompany compBankAc;
  compBankAc = em.find(BankAccountCompany.class, 
          receiptLine.getPayType().getPayTypeForBankAccount().getId(), OPTIMISTIC);
  // generate Bank gl line
  lineNum++;
  DocLineGlRec bnkGlLine = new DocLineGlRec();
  bnkGlLine.setAutoGenerated(true);
  bnkGlLine.setComp(doc.getCompany());
  bnkGlLine.setLineNum(lineNum);
  bnkGlLine.setCreateBy(doc.getCreatedBy());
  bnkGlLine.setCreateDate(doc.getCreateOn());
  bnkGlLine.setDocAmount(receiptAmount);
  bnkGlLine.setDocHeaderBase(doc);
  bnkGlLine.setGlAccount(bnkGlAc);
  bnkGlLine.setHomeAmount(receiptAmount);
  bnkGlLine.setLineType(glLineTyRec);
  LOGGER.log(INFO, "recDoc {0} sort order {1} receipt line {2}", 
          new Object[]{recDoc,bnkGlAc.getSortOrder(),receiptLine});
  String sortTxt = buildGlLineSortOrderTxt(recDoc, bnkGlAc.getSortOrder(), receiptLine);
  bnkGlLine.setSortOrder(sortTxt);
  bnkGlLine.setPostType(sysBuff.getPostTypeForCode("Debit"));
  bnkGlLine.setLineText(doc.getDocHdrText());
  doc.getDocLines().add(bnkGlLine);
  LOGGER.log(INFO, "bnkLine rec comp {0} line num {1} line type {2}", new Object[]{bnkGlLine.getComp(),
   bnkGlLine.getLineNum(), bnkGlLine.getLineType() });
  DocLineGl bnkGlLineDB = buildDocLineGL(bnkGlLine, source);
  LOGGER.log(INFO, "Bank Line id {0}", bnkGlLineDB.getId());
  LOGGER.log(INFO, "bnkLine DB comp {0} line num {1} line type {2}", new Object[]{bnkGlLineDB.getComp(),
   bnkGlLineDB.getLineNum(), bnkGlLineDB.getLineType() });
  recDoc.getDocLines().add(bnkGlLineDB);
  bnkGlLineDB.setDocFi(recDoc);
  // create bank transaction line
  DocBankLineBase bnkLine;
  if(payType.getPayMedium().equalsIgnoreCase("CHQ") && !payType.isInbound()){
   bnkLine = new DocBankLineChq();
   em.persist(bnkLine);
  }else{
   bnkLine = new DocBankLineBase();
   em.persist(bnkLine);
  }
  bnkLine.setCreatedBy(crUsr);
  bnkLine.setCreatedOn(doc.getCreateOn());
  bnkLine.setComp(comp);
  bnkLine.setUnClearedBankAc(compBankAc);
  bnkLine.setBnkRef(doc.getPartnerRef());
  bnkLine.setDocDate(doc.getDocumentDate());
  bnkLine.setPostDate(doc.getPostingDate());
  bnkLine.setAmount(receiptLine.getDocAmount());
  bnkLine.setReceipt(true);
  
  compBankAc.updateBalance(bnkLine, "N",crUsr,source,em);
  LOGGER.log(INFO, "doc returned id {0} number {2}", new Object[]{doc.getId(),doc.getDocNumber()});
  
  
  return doc;
 }

    

 public List<DocLineArRec> getOpenDocBySelectOpt(ArPayRunSelection opts, int type, UserRec usr, String pg) {
  LOGGER.log(INFO, "docDM.getOpenDocBySelectOpt called with {0}", opts);
  List rs;
  Query q = em.createNamedQuery("ArPayRun0");
  if(type == 0){
   q = em.createNamedQuery("ArPayRun0");
   rs = q.getResultList();
  LOGGER.log(INFO, "Number of invoices {0}", rs.size());
  }else if(type == 1){
   q = em.createNamedQuery("ArPayRun1");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   rs = q.getResultList();
  LOGGER.log(INFO, "Number of invoices {0}", rs.size());
  
  }else if(type == 2){
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun2");
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Num for doc type {0}", rs.size());
  }else if(type == 3){
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   q = em.createNamedQuery("ArPayRun3");
   q.setParameter("arAcs", arAcIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of invoices {0}", rs.size());
  }else if(type == 4){
   // posting type
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   q = em.createNamedQuery("ArPayRun4");
   q.setParameter("pstTypes", pstTyIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  } else if(type == 5){
   // date + doc ty
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun5");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 6){
   // date + ar ac
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   q = em.createNamedQuery("ArPayRun6");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("arAcs", arAcIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 7){
   // date + ar ac + doc Ty
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun7");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("arAcs", arAcIds);
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 8){
   // ar ac + doc Ty
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun8");
   q.setParameter("arAcs", arAcIds);
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of doc type and ar acs invoices {0}", rs.size());
  }else if(type == 9){
   // date + pst type ac
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   q = em.createNamedQuery("ArPayRun9");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("pstTypes", pstTyIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 10){
   // date + ar ac + pst type ac
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   q = em.createNamedQuery("ArPayRun10");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("pstTypes", pstTyIds);
   q.setParameter("arAcs", arAcIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 11){
   // Dates +  Ar cs + + doc tyoe + pst type
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun11");
   q.setParameter("dueFr", opts.getDueDateFrom());
   q.setParameter("dueTo", opts.getDueDateTo());
   q.setParameter("docFr", opts.getDocDateFrom());
   q.setParameter("docTo", opts.getDocDateTo());
   q.setParameter("postFr", opts.getPostDateFrom());
   q.setParameter("postTo", opts.getPostDateTo());
   q.setParameter("entryFr", opts.getEntryDateFrom());
   q.setParameter("entryTo", opts.getEntryDateTo());
   q.setParameter("pstTypes", pstTyIds);
   q.setParameter("arAcs", arAcIds);
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 12){
   // Ar cs + doc tyoe + pst type
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun12");
   q.setParameter("pstTypes", pstTyIds);
   q.setParameter("arAcs", arAcIds);
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 13){
   // doc type + pst type
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   
   List<Long> docTyeIds = new ArrayList<>();
   for(DocTypeRec ty : opts.getDocTypes()){
    Long tyId = ty.getId();
    docTyeIds.add(tyId);
   }
   q = em.createNamedQuery("ArPayRun13");
   q.setParameter("pstTypes", pstTyIds);
   q.setParameter("docTypeLst", docTyeIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }else if(type == 14){
   // Acs + pst type
   List<Long> pstTyIds = new ArrayList<>();
   for(PostTypeRec ac : opts.getPostTypes()){
    Long id = ac.getId();
    pstTyIds.add(id);
   }
   List<Long> arAcIds = new ArrayList<>();
   for(ArAccountRec ac : opts.getArAccounts()){
    Long id = ac.getId();
    arAcIds.add(id);
   }
   
   q = em.createNamedQuery("ArPayRun14");
   q.setParameter("pstTypes", pstTyIds);
   q.setParameter("arAcs", arAcIds);
   rs = q.getResultList();
   LOGGER.log(INFO, "Number of pst type invoices {0}", rs.size());
  }
  rs = q.getResultList();
  List<DocLineArRec> retList = new ArrayList<>();
  ListIterator li = rs.listIterator();
  //int cnt = 0;
  while(li.hasNext() ){
   DocLineAr ln = (DocLineAr)li.next();
   if(ln.getDocAmount() > 0.0){
    LOGGER.log(INFO, "ln doc num {0}", ln.getDocBase().getDocNumber());
    LOGGER.log(INFO, "ln bank {0} line {1}", new Object[]{ln.getPaymntBank(), ln.getLineNum()});
    ArAccount arAc = ln.getArAccount();
    ArAccountRec arAcRec = arAccountDM.buildArAccountRecPvt(arAc, usr, pg);
    DocLineArRec rec = this.buildDocLineArRec(ln, arAcRec, false);
    retList.add(rec);
   }
 //  cnt++;
  }
  LOGGER.log(INFO, "DB get lines returns {0}", retList);
  return retList;
 }

 public List<DocFiRec> getEditableFiDocsForComp(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "DocDm.getEditableFiDocsForComp called with comp {0}", comp.getId());
  int listSize = this.sysBuff.getMaxComplSize();
  Query q = em.createNamedQuery("docCompAll");
  q.setMaxResults(listSize);
  q.setParameter("compId", comp.getId());
  List rs = q.getResultList();
  ListIterator docsLi  = rs.listIterator();
  List<DocFiRec> resList = new ArrayList<>();
  while(docsLi.hasNext()){
   DocBase docBase = (DocBase)docsLi.next();
   if(docBase.getClass().getCanonicalName().endsWith("DocFI")){
    DocFi docFi = (DocFi)docBase;
    DocFiRec docFiRec = buildDocFiRec(docFi);
    List<DocLineBase> lines = docFi.getDocLines();
    LOGGER.log(INFO, "Doc id {0} has lines {1}", new Object[]{docFi.getId(), lines});
    if(!lines.isEmpty()){
     ListIterator<DocLineBase> li = lines.listIterator();
     int arLineNum = 0;
     boolean arCleared = false;
     while(li.hasNext()){
      DocLineBase ln = li.next();
      LOGGER.log(INFO, "Line class {0}", ln.getClass().getSimpleName());
      if(ln.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
       arLineNum++;
       if(ln.isClearedLine() && arLineNum == 1){
        arCleared = true;
       }
      }
     }
     if(!arCleared){
      resList.add(docFiRec);
      LOGGER.log(INFO, "Add doc");
     }
    }else{
     LOGGER.log(INFO,"no line items ");
     
    }
    
   }
   
  }
  LOGGER.log(INFO, "DocDm.getEditableFiDocsForComp returns {0}",resList);
  return resList;
  
 }

 public DocBankLineChqRec getDocBankLineChqExtraFld(DocBankLineChqRec lnRec, List<String> fields){
  LOGGER.log(INFO, "DocDM.getDocBankLineChqExtraFld called");
  DocBankLineChq ln = em.find(DocBankLineChq.class, lnRec.getId());
  LOGGER.log(INFO, "Fields {0}", fields);
  for(String fld:fields){
   switch (fld){
    case "docFi":
     if(lnRec.getDocFiLine() == null){
      // need doc line
      
      DocLineBaseRec  docLnRec = null;
      switch(ln.getDocFiLine().getClass().getSimpleName()){
       case "DocLineAp":
        docLnRec = this.buildDocLineApRec((DocLineAp)ln.getDocFiLine());
        break;
       case "DocLineAr":
        docLnRec = this.buildDocLineArRecMin((DocLineAr)ln.getDocFiLine());
        break;
       case "DocLineGL":
        docLnRec = this.buildGlRecDocLine((DocLineGl)ln.getDocFiLine());
        break;
      }
      lnRec.setDocFiLine(docLnRec);
     }
     if(lnRec.getDocFiLine().getDocHeaderBase() == null){
     // DocFiRec docFi = this.buildDocFiRec(ln.getDocFiLine().getDocFi());
     // lnRec.getDocFiLine().setDocFi(docFi);
     }
     
     break;
    
  }
  }
  
  return lnRec;
 }
 public List<DocFiRec> getDocEditFiCompByDocNumPart(CompanyBasicRec comp, Long docNum) {
  LOGGER.log(INFO, "DocDM.getDocFiCompByDocNumPart called with comp {0} doc num {1} lines {2}", 
          new Object[]{comp,docNum});
  int listSize = this.sysBuff.getMaxComplSize();
  LOGGER.log(INFO, "Max list size {0", listSize);
  Query q = em.createNamedQuery("docCompDocNumPartAll");
  q.setMaxResults(listSize);
  q.setParameter("compId", comp.getId());
  q.setParameter("docNum", docNum);
  List rs = q.getResultList();
  ListIterator docsLi  = rs.listIterator();
  List<DocFiRec> resList = new ArrayList<>();
  while(docsLi.hasNext()){
   DocBase docBase = (DocBase)docsLi.next();
   if(docBase.getClass().getCanonicalName().endsWith("DocFI")){
    DocFi docFi = (DocFi)docBase;
    List<DocLineBase> lines = docFi.getDocLines();
    if(!lines.isEmpty()){
     int numArLines = 0;
     boolean arCleared = false;
     for(DocLineBase ln : lines){
      if(ln.getClass().getSimpleName().equalsIgnoreCase("DocLineAr")){
       numArLines++;
       LOGGER.log(INFO, "Line id {0} ar line num {1} cleared: {2}", new Object[]{
        ln.getId(),numArLines,ln.isClearedLine()});
       if(ln.isClearedLine() && numArLines == 1){
        arCleared = true;
       }
      }
     }
     LOGGER.log(INFO, null, rs);
    if(!arCleared){
     DocFiRec docFiRec = buildDocFiRec(docFi);
     DocInvoiceAr inv = docFi.getDocInvoiceAr();
     if(inv != null){
      //does doc have an Sales invoice
      DocInvoiceArRec invRec = this.buildDocInvoiceArRec(inv);
      docFiRec.setDocInvoiceAr(invRec);
     }
     resList.add(docFiRec);
     LOGGER.log(INFO, "editDocByNum add doc {0}", docFiRec);
    }
    }else{
     LOGGER.log(INFO, "No doc lines");
    }
   }
  }
  return resList;
 }

 public DocFiRec updateDocument(DocFiRec doc, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "DocDM.updateDocument called with doc {0} user {1} page {2}", new Object[]{
   doc,usr,page });
  if(!trans.isActive()){
   trans.begin();
  }
  //Date updateOn = new Date();
  //User upDtUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
 // DocFi docDB = em.find(DocFi.class, doc.getId(), OPTIMISTIC);
  
  DocFi docDB = buildDocFi(doc, page);
  LOGGER.log(INFO, "after buildDoc Fi id {0}", docDB.getId());
  List<DocLineBaseRec> lines = doc.getDocLines();
  LOGGER.log(INFO, "lines {0}", lines);
  if(lines == null){
   LOGGER.log(INFO, " No lines ");
  }else{
   List<DocLineBase> docLines = new ArrayList<>();
   List<DocLineAr> arLines = new ArrayList<>();
   List<DocLineAp> apLines = new ArrayList<>();
   for(DocLineBaseRec line:lines){
    LOGGER.log(INFO, "line class {0}", line.getClass().getSimpleName());
    String className = line.getClass().getSimpleName();
    switch(className){
     case "DocLineGlRec":
      DocLineGl glLine = buildDocLineGL((DocLineGlRec)line, page);
      LOGGER.log(INFO, "glLine {0}", glLine);
      glLine.setDocNumber(docDB.getDocNumber());
      glLine.setDocBase(docDB);
      glLine.setDocFi(docDB);
      docLines.add(glLine);
      LOGGER.log(INFO, "Add gl Line num {0} id {1}", new Object[]{glLine.getLineNum(), glLine.getId()});
      break;
     case "DocLineArRec":
      DocLineAr arLine = buildDocLineAr((DocLineArRec)line, docDB, page);
      arLine.setDocNumber(docDB.getDocNumber());
      arLine.setDocBase(docDB);
      arLine.setDocFi(docDB);
      docLines.add(arLine);
      arLines.add(arLine);
      LOGGER.log(INFO, "Add ar line num {0} id {1}", new Object[]{arLine.getLineNum(), arLine.getId()});
      break;
     case "DocLineApRec":
      DocLineAp apLine = this.buildDocLineAp((DocLineApRec)line, page);
      apLine.setDocNumber(docDB.getDocNumber());
      apLine.setDocBase(docDB);
      apLine.setDocFi(docDB);
      docLines.add(apLine);
      apLines.add(apLine);
      LOGGER.log(INFO, "Add ap line num {0} id {1}", new Object[]{apLine.getLineNum(), apLine.getId()});
      break;
    }
    docDB.setDocLines(docLines);
    docDB.setApLines(apLines);
    docDB.setArLines(arLines);
      
   }
  }
  LOGGER.log(INFO, "Number of Doc Lines posted {0}", docDB.getDocLines().size());
  if(docDB.getDocLines() != null){
   double balChk = 0;
   for(DocLineBase curr: docDB.getDocLines()){
    if(curr.getPostType().isDebit()){
     balChk += curr.getHomeAmount();
    }
    else{
     balChk -= curr.getHomeAmount();
    }
   }
   if(balChk != 0){
    LOGGER.log(INFO, "posted bal {0}", balChk );
    trans.rollback();
    return doc;
   }
  }
  
  if(doc.getId() == null){
   doc.setId(docDB.getId());
  }
  
  trans.commit();
  return doc;
 }

 public DocBankLineChqRec postApBankChqLine(DocLineApRec apLine, String view ){
  LOGGER.log(INFO, "postApBankChqLine called with apLine {0}", apLine);
  if(apLine.getPayType() == null){
   LOGGER.log(INFO, "no payment type");
   return null;
  }
  PaymentTypeRec pt = apLine.getPayType();
  if(!StringUtils.equals(pt.getPayMedium(), "CHQ")){
   LOGGER.log(INFO, "Payment type medium not chq {0}",apLine.getPayType().getPayMedium());
   return null;
  }
   
  DocBankLineChqRec bnkLine = null;
  FiGlAccountCompRec glBnkAccount = pt.getGlBankAccount();
  BankAccountCompanyRec bnkAcnt = pt.getPayTypeForBankAccount();
  List<NumberRangeChequeRec> cb = bnkAcnt.getChequeBooks(); 
  
  return bnkLine;
  
 }
 public DocFiRec postApInvoice(DocFiRec doc,List<DocVatSummary> vatSumm, List<FundBalance> fndSplit, String page){
  LOGGER.log(INFO, "docDM postApInvoice called");
  LOGGER.log(INFO, "doc id {0}",doc.getId());
  if(doc.getDocLines() == null || doc.getDocLines().isEmpty()){
   LOGGER.log(INFO, "No document lines");
   return doc;
  }
  
  
  
  for(DocLineBaseRec curr:doc.getDocLines()){
   LOGGER.log(INFO, "Doc line type {0} id {1} amount {2} post type {3}", new Object[]{curr.getLineType().getLineCode(),
    curr.getId(), curr.getDocAmount(), curr.getPostType().getDescription()
   });
  }
  
  if(!trans.isActive()){
   trans.begin();
  }
  // Save document and get ID
  DocFi docFi = buildDocFi(doc, page);
  LOGGER.log(INFO, "Doc id is now {0}", docFi.getId());
  
  // save entered doc lines
  List<DocLineBase> docLines = new ArrayList<>();
  FiGlAccountComp reconcilAcnt;
  for(DocLineBaseRec curr:doc.getDocLines()){
   if(StringUtils.equals(curr.getLineType().getLineCode(), "AP")){
    DocLineApRec currAp = (DocLineApRec)curr;
    DocLineAp lineAp = buildDocLineAp(currAp, page);
    
    lineAp.setDocHeaderBase(docFi);
    lineAp.setDocBase(docFi);
    lineAp.setDocFi(docFi);
    FiGlAccountCompRec reconcilAcntRec = currAp.getApAccount().getReconciliationAc();
    reconcilAcnt = em.find(FiGlAccountComp.class, reconcilAcntRec.getId());
    List<DocLineGlRec> recLinesRec = currAp.getReconiliationLines();
    List<DocLineGl> recLines = new ArrayList<>();
    for(DocLineGlRec currGlLine:recLinesRec){
     Fund fnd = null;
     if(currGlLine.getRestrictedFund() != null){
      fnd = em.find(Fund.class, currGlLine.getRestrictedFund().getId());
     }
     DocLineGl recLine = buildDocLineGL(currGlLine, page);
     recLine.setReconGlLnForApLine(lineAp);
//buildDocLineGlRecon(lineAp, reconcilAcnt, currApLine.getDocAmount(), 
       //);
     recLines.add(recLine);
    }
    lineAp.setReconDocLines(recLines);
    docLines.add(lineAp);
   
   } else if(StringUtils.equals(curr.getLineType().getLineCode(), "GL")){
    // gl line.
    DocLineGl lineGl = this.buildDocLineGL((DocLineGlRec)curr, page);
    lineGl.setDocHeaderBase(docFi);
    lineGl.setDocFi(docFi);
    docLines.add(lineGl);
    
   }
  
  }
  
  LOGGER.log(INFO, "docFi.getId() {0}", docFi.getId());
  if(docFi.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  docFi.setDocLines(docLines);
  doc.setId(docFi.getId());
  doc.setDocNumber(docFi.getDocNumber());
  return doc;
 }
 
 public DocFiRec postApPaySingle(DocFiRec docRec, String pg){
  LOGGER.log(INFO, "DocDM.postApPaySingle called with", trans);
  if(!trans.isActive()){
   trans.begin();
  }
  // create doc header
  DocFi doc = this.buildDocFi(docRec, pg);
  
  List<DocLineBaseRec> docRecLines = docRec.getDocLines();
  List<DocLineBase> docLines = doc.getDocLines();
  LOGGER.log(INFO, "docLines {0}",docLines);
  
  for(DocLineBaseRec docLnRec:docRecLines){
   
   LOGGER.log(INFO, "Doc line type {0}", docLnRec.getClass().getSimpleName());
   if(StringUtils.equals(docLnRec.getClass().getSimpleName(), "DocLineApRec")){
    LOGGER.log(INFO, "Start processing AP line (8338)");
    DocLineApRec apLnRec = (DocLineApRec)docLnRec;
    DocLineAp apLine = buildDocLineAp(apLnRec, pg);
    LOGGER.log(INFO, "Aplone clearing date {0}", apLine.getClearingDate());
    apLine.setDocFi(doc);
    apLine.setDocBase(doc);
    docLines.add(apLine);
    List<DocLineBaseRec> clearedLines = docLnRec.getClearingLineForLines();
    
    LOGGER.log(INFO, "clearedLines by doc {0}", clearedLines);
    if(clearedLines != null){
     Date clrDate = new Date();
     List<DocLineBase> clearingFor = new ArrayList<>();
     for(DocLineBaseRec clearedRec: clearedLines){
      DocLineAp clearedAp = em.find(DocLineAp.class, clearedRec.getId());
      clearedAp.setClearedLine(true);
      clearedAp.setClearingDate(clrDate);
      clearedAp.setClearedByLine(apLine);
      clearedAp.setPaidAmount(clearedRec.getPaidAmount());
      clearingFor.add(clearedAp);
     }
     // set clearing line status
     apLine.setClearingLineForLines(clearingFor);
    }
    FiGlAccountComp recAcnt = apLine.getApAccount().getReconciliationAc();
    LOGGER.log(INFO, "recAcnt {0}", recAcnt.getCoaAccount().getRef());
    List<DocLineGl> reconLines = new ArrayList<>();
    List<DocLineGlRec> reconRecLines = apLnRec.getReconiliationLines();
    for(DocLineGlRec glLn:reconRecLines ){
     LOGGER.log(INFO, "recon line GL account {0}", glLn.getGlAccount());
     
     DocLineGl reconLn = buildDocLineGL(glLn, pg);
     if(glLn.getGlAccount() == null){
      reconLn.setGlAccount(recAcnt);
     }
     
     reconLn.setReconGlLnForApLine(apLine);
     LOGGER.log(INFO, "recon line GL account {0}", reconLn.getGlAccount());
     LOGGER.log(INFO, "recon debit {0} amount {1}", new Object[]{reconLn.getPostType().isDebit(),reconLn.getDocAmount()});
     reconLines.add(reconLn);
    }
    apLine.setReconDocLines(reconLines);
    
   }else if(StringUtils.equals(docLnRec.getClass().getSimpleName(), "DocLineGLRec")){
    LOGGER.log(INFO,"GL account on rec line {0}", ((DocLineGlRec)docLnRec).getGlAccount().getCoaAccount().getRef() );
    DocLineGl glLine = this.buildDocLineGL((DocLineGlRec)docLnRec, pg);
    docLines.add(glLine);
   }
   long line = 0;
   
   for(DocLineBase ln:doc.getDocLines()){
    if(ln.getClass().getSimpleName().equals("DocLineGL")){
     LOGGER.log(INFO, "docline type {0} debit {1} amount {2}", 
      new Object[]{ln.getClass().getSimpleName(),ln.getPostType().isDebit(),
       ((DocLineGl)ln).getDocAmount()});
     
    
    
    }
    if(ln.getClass().getSimpleName().equals("DocLineAP")){
     LOGGER.log(INFO, "docline type {0} debit {1} amount {2}", 
      new Object[]{ln.getClass().getSimpleName(),ln.getPostType().isDebit(),
       ((DocLineAp)ln).getDocAmount()});
     
    
    }
    
    LOGGER.log(INFO, "Post type {0}", ln.getPostType().getPostTypeCode());
    line++;
    ((DocLineAp)ln).setDocFi(doc);
   // ln.setDocFi(doc);
    ln.setLineNum(line);
    
   }
   doc.setDocLines(docLines);
  }
  double docBal = 0;
  for(DocLineBase ln:doc.getDocLines()){
   if(ln.getClass().getSimpleName().equals("DocLineAP")){
    if(ln.getPostType().isDebit()){
      docBal += ((DocLineAp)ln).getDocAmount();
     }else{
      docBal -= ((DocLineAp)ln).getDocAmount();
     } 
    LOGGER.log(INFO, "AP reconciliation lines {0}", ((DocLineAp)ln).getReconDocLines());
   }else if(ln.getClass().getSimpleName().equals("DocLineGL")){
    if(ln.getPostType().isDebit()){
      docBal += ((DocLineGl)ln).getDocAmount();
     }else{
      docBal -= ((DocLineGl)ln).getDocAmount();
     } 
   }
   LOGGER.log(INFO, "docBal {0}", docBal);
  }
  
  
  LOGGER.log(INFO, "Doc balance {0}", docBal);
  if(docBal == 0){
   docRec.setId(doc.getId());
   docRec.setDocNumber(doc.getDocNumber());
   LOGGER.log(INFO, "End of Cr single payment {0} doc num {1}", new Object[]{docRec.getId(),docRec.getDocNumber()});
   //trans.rollback();
   trans.commit();
  } else{
   trans.rollback(); 
  }
  return docRec;
 }
 
 public DocFiRec postArInvoice(DocFiRec doc,List<DocVatSummary> vatSumm, List<FundBalance> fndSplit, String page){
  LOGGER.log(INFO, "docDM postArInvoice called");
  LOGGER.log(INFO, "doc id {0}",doc.getId());
  if(doc.getDocLines() == null || doc.getDocLines().isEmpty()){
   LOGGER.log(INFO, "No document lines");
   return doc;
  }
  
  for(DocLineBaseRec curr:doc.getDocLines()){
   LOGGER.log(INFO, "Doc line type {0} id {1} amount {2} post type {3}", new Object[]{curr.getLineType().getLineCode(),
    curr.getId(), curr.getDocAmount(), curr.getPostType().getDescription()
   });
  }
  
  if(!trans.isActive()){
   trans.begin();
  }
  // Save document and get ID
  DocFi docFi = buildDocFi(doc, page);
  LOGGER.log(INFO, "Doc id is now {0}", docFi.getId());
  
  // save entered doc lines
  List<DocLineBase> docLines = new ArrayList<>();
  FiGlAccountComp reconcilAcnt;
  for(DocLineBaseRec curr:doc.getDocLines()){
   if(StringUtils.equals(curr.getLineType().getLineCode(), "AR")){
    DocLineArRec currAr = (DocLineArRec)curr;
    DocLineAr lineAr = buildDocLineAr(currAr, page);
    
    lineAr.setDocHeaderBase(docFi);
    lineAr.setDocBase(docFi);
    lineAr.setDocFi(docFi);
    FiGlAccountCompRec reconcilAcntRec = currAr.getArAccount().getReconciliationAc();
    reconcilAcnt = em.find(FiGlAccountComp.class, reconcilAcntRec.getId());
    List<DocLineGlRec> recLinesRec = currAr.getReconiliationLines();
    List<DocLineGl> recLines = new ArrayList<>();
    for(DocLineGlRec currGlLine:recLinesRec){
     DocLineGl recLine = buildDocLineGL(currGlLine, page);
     Fund fnd;
     if(currGlLine.getRestrictedFund() != null){
      fnd = em.find(Fund.class, currGlLine.getRestrictedFund().getId());
      recLine.setRestrictedFund(fnd);
     }
     recLine.setReconcilForArLine(lineAr);
     recLines.add(recLine);
    }
    lineAr.setReconiliationLines(recLines);
    docLines.add(lineAr);
    
    // update account balance 
     ArAccount arAcnt = lineAr.getArAccount();
     double currBal = arAcnt.getAccountBalance();
     if(lineAr.getPostType().isDebit()){
      currBal += lineAr.getDocAmount();
     }else{
      currBal -= lineAr.getDocAmount();
     }
     arAcnt.setAccountBalance(currBal);
     LOGGER.log(INFO, "Current account balance {0}", arAcnt.getAccountBalance());
   
   } else if(StringUtils.equals(curr.getLineType().getLineCode(), "GL")){
    // gl line.
    DocLineGl lineGl = this.buildDocLineGL((DocLineGlRec)curr, page);
    lineGl.setDocHeaderBase(docFi);
    lineGl.setDocFi(docFi);
    docLines.add(lineGl);
    
   }
  
  }
  
  LOGGER.log(INFO, "docFi.getId() {0}", docFi.getId());
  if(docFi.getId() == null){
   trans.rollback();
  }else{
   trans.commit();
  }
  docFi.setDocLines(docLines);
  doc.setId(docFi.getId());
  doc.setDocNumber(docFi.getDocNumber());
  return doc;
 }
 
 public DocFiRec postArPayRunBacs(DocFiRec doc, BnkPaymentRunRec payRun, BankAccountCompanyRec compBnkAc,
 String lineText, UserRec usr, String source) throws BacException {
  LOGGER.log(INFO, "docDM.postArPayRunBacs called with doc {0} payRun {1} loc {2} usr {3} souve {4}", 
          new Object[]{doc,payRun,usr,source});
  User crUser = em.find(User.class, usr.getId(), OPTIMISTIC);
  Date crDate = new Date();
  LOGGER.log(INFO, "doc.getCompany {0}", doc.getCompany());
  DocFi docDB = this.buildDocFi(doc, source);
  docDB.setDocNumber(compDM.getCompDocNumber(docDB.getCompany().getId(), "FI_DOC_FIN"));
  doc.setDocNumber(docDB.getDocNumber());
  List<DocLineBase> docDBLines = docDB.getDocLines();
  if(docDBLines == null){
   docDBLines = new ArrayList<>();
  }
  LOGGER.log(INFO, "docDB.getCompany {0}", docDB.getCompany());
  doc.setId(docDB.getId());
  LineTypeRuleRec arLineType = this.sysBuff.getLineTypeRuleByCode("AR");
  LineTypeRuleRec glLineType = this.sysBuff.getLineTypeRuleByCode("GL");
  PostTypeRec postCd = sysBuff.getPostTypeForCode("arPymnt");
  PostTypeRec glDrPostCd = sysBuff.getPostTypeForCode("Debit");
  BnkPaymentRun payRunDB = em.find(BnkPaymentRun.class, payRun.getId(), OPTIMISTIC);
  List<DocBankLineBaseRec> bnkLines = payRun.getBankLines();
  long docLinNum = 0;
  for(DocBankLineBaseRec bnkLn: bnkLines){
   docLinNum++;
   // build ar payment line for each bank line
    if(bnkLn.getArAccount() != null){
     // AR receipt line
     DocLineAr docLine = (DocLineAr)buildArPayRunArLine(doc, docDB, payRun, bnkLn, lineText,  arLineType, postCd, docLinNum, usr,
           crUser, source, crDate);
     
     LOGGER.log(INFO, "docLine {0}", docLine);
     LOGGER.log(INFO, source, payRun);
      docLine.setBnkPaymentRun(payRunDB);
      DocBankLineBase bnkLnDB = bankDM.createPayRunBankLinePvt(payRunDB, bnkLn, docLine, usr, crDate, 
              source);
      LOGGER.log(INFO, "Created bnk line for AR line {0}", bnkLnDB.getId());
      LOGGER.log(INFO, "Payrun bnk lines {0}", payRunDB.getBankLines());
      docDBLines.add(docLine);
     
    }else{
     DocLineGl docLine = (DocLineGl)buildArPayRunGlLine(doc, docDB, payRun, 
             (DocBankLineBacsRec)bnkLn, lineText, glLineType, glDrPostCd, docLinNum, usr, 
             crUser, source, crDate);
     
     LOGGER.log(INFO, "Created Gl line with id: {0} amount {1}", new Object[]{docLine.getId(),docLine.getDocAmount()});
     bnkLn.setUnClearedBankAc(compBnkAc);
     DocBankLineBase bnkLnDB = bankDM.createPayRunBankLinePvt(payRunDB, bnkLn, docLine, usr, crDate, 
             source);
     LOGGER.log(INFO, "Created bnk line for Contra bank line {0}", bnkLnDB.getId());
     LOGGER.log(INFO, "Payrun bnk lines {0}", payRunDB.getBankLines());
     //docLine = this.buildGlDocLine(null, docLine)
     docDBLines.add(docLine);
     
    }
   
  }
  docDB.setDocLines(docDBLines);
 return doc;
 }

 public boolean stdJnlPartialDel(DocFiPartialRec ptDocRec) {
  LOGGER.log(INFO, "stdJnlPartialDel called with {0}", ptDocRec);
  DocFiPartial ptDoc = em.find(DocFiPartial.class, ptDocRec.getId(), OPTIMISTIC);
  if(!trans.isActive()){
   trans.begin();
  }
  
  try{
  em.remove(ptDoc);
  trans.commit();
   return true;
  }catch(IllegalArgumentException ex)  {
   trans.rollback();
   LOGGER.log(INFO, "Could not delete partial doc id {0} reason: {1}", new Object[]{ptDocRec.getId(),ex.getLocalizedMessage()});
   return false;
  }      
         
 }
 
public DocFiTemplAccrPrePayRec templateJnlCreate(DocFiTemplAccrPrePayRec jnl, String pg){
 LOGGER.log(INFO, "templateJnlCreate called with jnl {0}", jnl);
 
 // build Standard journal;
 
 
 return jnl;
}
public DocLineBaseRec resetArLine(DocLineArRec ln, User upDtUsr, Date upDtDate, String page) throws BacException{
 LOGGER.log(INFO, "resetArLine called with line {0} usr {1} page {2}", new Object[]{ln,upDtUsr,page});
 LOGGER.log(INFO, "reset {0} cleared {1} clearing {2} cleared by {3} clearing for line {4}", 
         new Object[]{ln.isReset(), ln.isClearedLine(), ln.isClearingLine(),ln.getClearedByLine(),
          ln.getClearingLineForLines()});
 
 DocLineBase clearingLine = em.find(DocLineBase.class, ln.getId(), OPTIMISTIC);
 
 List<DocLineBase> clearedDocs = clearingLine.getClearingLineForLines();
 ListIterator<DocLineBase> li = clearedDocs.listIterator();
 while(li.hasNext()){
  DocLineAr clearedDoc = (DocLineAr)li.next();
  // unclear AR line and audit
  LOGGER.log(INFO, "Reset clearing of AR line id {0}", clearedDoc.getId());
  
  
  AuditDocLine au = new AuditDocLine();
  au.setAuditDate(upDtDate);
  au.setDocLine(clearedDoc);
  au.setFieldName("DOC_LN_CL_BY");
  au.setNewValue(null);
  au.setOldValue(String.valueOf(clearingLine.getDocBase().getDocNumber()));
  au.setSource(page);
  au.setUsrAction('U');
  em.persist(au);
  au = new AuditDocLine();
  au.setAuditDate(upDtDate);
  au.setDocLine(clearedDoc);
  au.setFieldName("DOC_LN_CL_DT");
  au.setNewValue(null);
  au.setOldValue(clearedDoc.getClearingDate().toString());
  au.setSource(page);
  au.setUsrAction('U');
  em.persist(au);
  
  clearedDoc.setClearedByLine(null);
  clearedDoc.setClearingDate(null);
  clearedDoc.setClearedLine(false);
  clearedDoc.setChangeBy(upDtUsr);
  clearedDoc.setChangeDate(upDtDate);
  // unclear Reconciliation account
  List<DocLineGl> recLines = (clearedDoc).getReconiliationLines();
  for(DocLineGl recLn:recLines){
   if(recLn.isClearedLine()){
    DocLineBase clearingDoc = recLn.getClearedByLine();
    LOGGER.log(INFO, "Reset clearing of GL line id {0}", clearingDoc.getId());
    recLn.setClearedByLine(null);
    recLn.setClearingDate(null);
    recLn.setClearedLine(false);
    
    recLn.setChangeBy(upDtUsr);
    recLn.setChangeDate(upDtDate);
    au = new AuditDocLine();
    au.setAuditDate(upDtDate);
    au.setDocLine(recLn);
    au.setFieldName("DOC_LN_CL_BY");
    au.setNewValue(null);
    au.setOldValue(String.valueOf(recLn.getDocBase().getDocNumber()));
    au.setSource(page);
    au.setUsrAction('U');
    em.persist(au);
    au = new AuditDocLine();
    au.setAuditDate(upDtDate);
    au.setDocLine(recLn);
    au.setFieldName("DOC_LN_CL_DT");
    au.setNewValue(null);
    au.setOldValue(String.valueOf(recLn.getDocBase().getDocNumber()));
    au.setSource(page);
    au.setUsrAction('U');
    em.persist(au);
    
    au = new AuditDocLine();
    au.setAuditDate(upDtDate);
    au.setDocLine(recLn);
    au.setFieldName("DOC_LN_CLEARED");
    au.setNewValue(String.valueOf(false));
    au.setOldValue(String.valueOf(recLn.isClearedLine()));
    au.setSource(page);
    au.setUsrAction('U');
    em.persist(au);
   }
  }
  li.remove();
 }
 clearingLine.setClearingLineForLines(clearedDocs);
 clearingLine.setClearingLine(false);
 AuditDocLine au = new AuditDocLine();
 au.setAuditDate(upDtDate);
 au.setDocLine(clearingLine);
 au.setFieldName("DOC_LN_CLEARING");
 au.setNewValue(null);
 au.setOldValue(String.valueOf(clearingLine.isClearingLine()));
 au.setSource(page);
 au.setUsrAction('U');
 em.persist(au);
 return ln;
}
 public DocFiRec docRestClearing(DocFiRec doc, UserRec usr, String page) throws BacException {
  LOGGER.log(INFO, "DocDM.docRestClearing called with doc {0} user {1} page{2}", new Object[]{
   doc,usr,page});
  User upDtUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  Date upDtDate = new Date();
  List<DocLineBaseRec> docLines = doc.getDocLines();
  ListIterator<DocLineBaseRec> docLinesLi = docLines.listIterator();
  while(docLinesLi.hasNext()){
   DocLineBaseRec ln = docLinesLi.next();
   LOGGER.log(INFO, "line num {0} is type {1}", new Object[]{ln.getLineNum(),ln.getClass().getSimpleName()});
   if(ln.isReset()){
    // from here
    if(ln.getClass().getSimpleName().equalsIgnoreCase("DocLineArRec")){
     LOGGER.log(INFO, "AR line to reset");
     try{
      ln = resetArLine((DocLineArRec)ln, upDtUsr, upDtDate, page);
      ln.setClearingLine(false);
      ln.setChangeBy(usr);
      ln.setChangeDate(upDtDate);
     }catch(BacException ex){
      LOGGER.log(INFO, "Error clearing line {0} reason {1}", new Object[]{ln.getLineNum(),ex.getLocalizedMessage()});
      throw new BacException("Error clearing line: "+ln.getLineNum()+" reason: "+ex.getLocalizedMessage());
     }
    }
    
   }
   docLinesLi.set(ln);
  }
  doc.setDocLines(docLines);
  return doc;
 }

 public DocFiRec reverseFiDoc(DocFiRec originalDoc, DocFiRec reversalDoc, UserRec usr, String page)
         throws BacException {
  LOGGER.log(INFO, "DocDM.reverseFiDoc called with original {0} rev {1} usr{2} page{3}", new Object[]{
   originalDoc,reversalDoc,usr,page });
  LOGGER.log(INFO, "originalDoc id {0}", originalDoc.getId());
  User upDtUsr = em.find(User.class, usr.getId(), OPTIMISTIC);
  Date upDtDate = new Date();
  DocFi origDoc = em.find(DocFi.class,originalDoc.getId(), OPTIMISTIC);
  LOGGER.log(INFO, "originalDoc id {0}", origDoc.getId());
  if(origDoc.getReversalDoc() != null){
   throw new BacException(" - Cannot rev doc already reversed","DOC_PRIOR_REV");
  }
  DocFi revDoc = this.buildDocFi(reversalDoc, page);
  LOGGER.log(INFO, "reversalDoc id {0}", revDoc.getId());
  List<DocLineBase> origInes =  origDoc.getDocLines();
  LOGGER.log(INFO, "Original Doc has lines {0}", origInes);
  List<DocLineBase> revInes =  revDoc.getDocLines();
  if(revInes == null){
   revInes = new ArrayList<>();
  }
  long revLnNum = 0;
  List<FiPeriodBalance> glActBalances = null;
  List<FiPeriodBalance> glAcRestrictBalances = null;
  List<FiPeriodBalance> glAcCostAcBalances = null;
  List<FiPeriodBalance> glAcProjAcBalances = null;
  for(DocLineBase origLn:origInes){
   LOGGER.log(INFO, "Module {0}", origLn.getLineType().getModule().getName());
   revLnNum++;
   if(origLn.getClass().getSimpleName().equalsIgnoreCase("DocLineGL")){
    // process GL line
    DocLineGl glLineRev = this.newDocLineGL((DocLineGl)origLn, upDtUsr, upDtDate);
    glLineRev.setDocFi(revDoc);
    glLineRev.setDocHeaderBase(revDoc);
    glLineRev.setLineNum(revLnNum);
    PostType revPstTy = (origLn).getPostType().getRevPostType();
    if(revPstTy == null){
     throw new BacException("No reversal posting type for: "+origLn.getPostType().getPostTypeCode());
    }
    glLineRev.setPostType(revPstTy);
    glActBalances = buildDocLineGlAcBalance(glLineRev, glActBalances, revDoc);
    if(glLineRev.getRestrictedFund() != null){
     // need to update restricted fund balance
     glAcRestrictBalances = buildDocLineGlRestrfndAcBalance(glLineRev, glAcRestrictBalances, 
         revDoc);
    }
    if(glLineRev.getCostCentre() != null){
     //need to update cost centre balance
     glAcCostAcBalances = buildDocLineGlCostAcBalance(glLineRev, glAcCostAcBalances, revDoc );
    }
    if(glLineRev.getProjectBalance() != null){
     glAcProjAcBalances = buildDocLineGlProgramAcBalance(glLineRev, glAcProjAcBalances, revDoc );
    }
     
    revInes.add(glLineRev);
   }else if(origLn.getClass().getSimpleName().equalsIgnoreCase("DocLineAR")){
    // process AR line
    DocLineAr arLineRev = newDocLineAr((DocLineAr)origLn,upDtUsr,upDtDate);
    arLineRev.setLineNum(revLnNum);
    arLineRev.setDocBase(revDoc);
    arLineRev.setDocFi(revDoc);
    arLineRev.setDocHeaderBase(revDoc);
    PostType revPstTy = ((DocLineAr)origLn).getPostType().getRevPostType();
    if(revPstTy == null){
     throw new BacException("No reversal posting type for: "+origLn.getPostType().getPostTypeCode());
    }
    arLineRev.setPostType(revPstTy);
    
    if(arLineRev.getPostType().isDebit()){
     arLineRev = setArAccountBalance(arLineRev, revDoc, "REV_DR", upDtUsr, page);
    }else{
     arLineRev = setArAccountBalance(arLineRev, revDoc, "REV_CR", upDtUsr, page);
    }
     
    List<DocLineGl> revRecLines = arLineRev.getReconiliationLines();
    if(revRecLines == null){
     revRecLines = new ArrayList<>();
    }
    List<DocLineGl> recLines = arLineRev.getReconiliationLines();
    if(recLines != null){
     long revRecLnNum = 0;
     for(DocLineGl recLn:recLines ){
      revRecLnNum++;
      DocLineGl revRecLn = this.newDocLineGL(recLn, upDtUsr, upDtDate);
      revRecLn.setLineNum(revRecLnNum);
      PostType revRecPstTy = recLn.getPostType().getRevPostType();
      if(revRecPstTy == null){
       throw new BacException("No reversal posting type for: "+recLn.getPostType().getPostTypeCode());
      }
      revRecLn.setPostType(revRecPstTy);
      revRecLn.setDocBase(revDoc);
      revRecLn.setDocFi(revDoc);
      revRecLn.setDocHeaderBase(revDoc);
      glActBalances = buildDocLineGlAcBalance(revRecLn, glActBalances, revDoc);
      if(revRecLn.getRestrictedFund() != null){
       // need to update restricted fund balance
       glAcRestrictBalances = buildDocLineGlRestrfndAcBalance(revRecLn, glAcRestrictBalances, 
            revDoc);
      }
      revRecLines.add(revRecLn);
     }
    }
    arLineRev.setReconiliationLines(revRecLines);
    revInes.add(arLineRev);
   }
   
  }
  revDoc.setDocLines(revInes);
  revDoc.setReversalOfDoc(origDoc);
  origDoc.setReversalDoc(revDoc);
  reversalDoc.setDocNumber(revDoc.getDocNumber());
  LOGGER.log(INFO, "Reversal doc has lines {0}", revDoc.getDocLines());
  
  return reversalDoc;
 }

}
