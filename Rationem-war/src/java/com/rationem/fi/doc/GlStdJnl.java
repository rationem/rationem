/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.doc;


import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.util.RestFundPostBalance;
import org.primefaces.event.SelectEvent;
import java.util.List;
import com.rationem.ejbBean.fi.DocumentManager;
import com.rationem.busRec.user.UserRec;
import org.primefaces.event.FlowEvent;
import java.util.ListIterator;
import javax.ejb.EJBTransactionRolledbackException;
import com.rationem.busRec.doc.DocTypeRec;
import java.text.SimpleDateFormat;
import com.rationem.busRec.config.common.SortOrderRec;
import com.rationem.busRec.config.common.TransactionTypeRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import java.util.logging.Level;
import javax.faces.event.ValueChangeEvent;
import com.rationem.util.GenUtil;
import com.rationem.busRec.config.company.FiscalPeriodYearRec;
import com.rationem.ejbBean.config.company.CompanyManager;
import java.util.Date;
import javax.servlet.ServletException;
//import org.primefaces.event.DateSelectEvent;
import javax.ejb.TransactionRolledbackLocalException;
import com.rationem.exception.BacException;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.doc.DocFiPartialRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import java.util.ArrayList;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineGlRec;
import com.rationem.busRec.doc.DocLineGlPartialRec;
import com.rationem.busRec.fi.company.CompPostPerRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.ejbBean.fi.GlAccountManager;
import com.rationem.ejbBean.ma.CostCentreMgr;
import com.rationem.ejbBean.ma.ProgrammeMgr;
import com.rationem.util.MessageUtil;
import java.util.UUID;
import javax.ejb.EJB;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;



/**
 *
 * @author Chris
 */
public class GlStdJnl extends DocCommon  {
  private static final Logger logger =
            Logger.getLogger("Accounts.fi.doc.GlStdJnl");
  private static final String GLLINE = "GL";
  private static final String GLLNPT = "GLJNLPT";

  @EJB
  private SysBuffer sysBuff;

  @EJB
  private CompanyManager compMgr;

  @EJB
  private GlAccountManager glAcntMgr;
  
  @EJB
  private DocumentManager docMgr;
  
  @EJB
  private CostCentreMgr costCentreMgr;
  
  @EJB
  private ProgrammeMgr progMgr;

  private DocFiRec jnl ;
  private CompanyBasicRec docComp;
  private List<DocFiPartialRec> partialDocs;
  private List<DocFiPartialRec> partialDocsFiltered;
  
  private DocFiPartialRec partialDocSelected;
  private boolean partialDocCompl;
  private ArrayList<DocLineGlRec> glDocLines;
  private ArrayList<DocTypeRec> glDocTypes;
  private List<LineTypeRuleRec> lineTypes; 
  
  private DocTypeRec docTypeSelected;
  private DocLineGlRec glDocLine;
  private DocLineGlRec glDocLinePreEdit;
  private DocLineGlRec glDocLineSelected;
  
  private JnlSummaryLine summaryLine;
  private ArrayList<PostTypeRec> postTypes;
  private List<FiGlAccountCompRec> glAccounts;
  private FiGlAccountCompRec glAccountSelected;
  private List<FundRec> restrictedFunds;
  private List<CostCentreRec> costCentres;
  private List<ProgrammeRec> programmes;
  private ArrayList<JnlSummaryLine> summaryLines;
  private List<VatCodeCompanyRec> vatCodes;
  private JnlSummaryLine selectedSummLine;
  private  String fundCode;
  private List<TransactionTypeRec> glTransTypes;


  private double totalDebits;
  private double totalCredits;
  private double docbalance;
  private int numLines;
  private Long selectedPostType;
  private String postType;
  private String accountRef;
  private boolean lineDisabled = true;
  private boolean addLineDisabled = true;
  private boolean canPost = false;
  private boolean jnlPosted = false;

  private ArrayList<RestFundPostBalance> fundPostingBals;
  /** Creates a new instance of GlStdJnl */
  public GlStdJnl()  {
  }

  @PostConstruct
  private void init(){
   jnl = this.fiDoc;
   
   
   glTransTypes = this.sysBuff.getGlTransactionTypes();
   if(glTransTypes == null){
    MessageUtil.addWarnMessage("glTypes", "errorText");
    return;
   }
   lineTypes = sysBuff.getLineTypeRules();
   logger.log(INFO, "lineTypes {0}", lineTypes);
  
   
  }
  
  
  private DocFiPartialRec buildDocFiPartialRec(DocFiRec fiDoc){
   DocFiPartialRec part = new DocFiPartialRec();
   part.setChangeBy(fiDoc.getChangedBy());
   part.setChangeDate(fiDoc.getChangedOn());
   part.setCreateBy(fiDoc.getCreatedBy());
   part.setCreateDate(fiDoc.getCreateOn());
   part.setDocHdrText(fiDoc.getDocHdrText());
   part.setDocumentDate(fiDoc.getDocumentDate());
   part.setDocType(fiDoc.getDocType());
   part.setFisPeriod(fiDoc.getFisPeriod());
   part.setFisYear(fiDoc.getFisYear());
   part.setTransactionType(fiDoc.getTransactionType());
   part.setNotes(fiDoc.getNotes());
   part.setPartnerRef(fiDoc.getPartnerRef());
   part.setPostingDate(fiDoc.getPostingDate());
   part.setCompany(fiDoc.getCompany());
   part.setReversed(fiDoc.isReversed());
   part.setTaxDate(fiDoc.getTaxDate());
   part.setTaxDateStr(fiDoc.getTaxDateStr());
   return part;
  }
   
  private DocFiRec buildDocFiRec( DocFiPartialRec doc){
   DocFiRec rec = new DocFiRec();
   rec.setChangedBy(doc.getChangeBy());
   rec.setChangedOn(doc.getChangeDate());
   rec.setCreatedBy(doc.getCreateBy());
   rec.setCreateOn(doc.getCreateDate());
   rec.setDocHdrText(doc.getDocHdrText());
   rec.setDocumentDate(doc.getDocumentDate());
   rec.setDocType(doc.getDocType());
   rec.setFisPeriod(doc.getFisPeriod());
   rec.setFisYear(doc.getFisYear());
   rec.setTransactionType(doc.getTransactionType());
   rec.setNotes(doc.getNotes());
   rec.setPartnerRef(doc.getPartnerRef());
   rec.setPostingDate(doc.getPostingDate());
   rec.setCompany(doc.getCompany());
   rec.setReversed(doc.isReversed());
   rec.setTaxDate(doc.getTaxDate());
   
   return rec;
  }
  
   private DocLineGlPartialRec buildDocLineGlPartialRec(DocLineGlRec ln, DocFiPartialRec hdr ){
    DocLineGlPartialRec part = new DocLineGlPartialRec();
    part.setId(ln.getId());
    part.setAutoGenerated(ln.isAutoGenerated());
    part.setChangeBy(ln.getChangeBy());
    part.setComp(ln.getComp());
    part.setCostCentre(ln.getCostCentre());
    part.setCreateBy(ln.getCreateBy());
    part.setCreditValue(ln.getCreditValue());
    part.setDebitValue(ln.getDebitValue());
    part.setDocAmount(ln.getDocAmount());
    part.setDocHeaderBase(hdr);
    part.setGlAccount(ln.getGlAccount());
    part.setLineText(ln.getLineText());
    part.setLineType(ln.getLineType());
    part.setPostType(ln.getPostType());
    part.setProgramme(ln.getProgramme());
    part.setReference1(ln.getReference1());
    part.setReference2(ln.getReference2());
    part.setSortOrder(ln.getSortOrder());
    //part.setVatCode(ln.getVatCode());
    
    return part;
    
   }
  
   private DocLineGlRec buildDocLineGlRec(DocLineGlPartialRec ln, DocFiRec hdr ){
    DocLineGlRec rec = new DocLineGlRec();
    rec.setId(ln.getId());
    rec.setAutoGenerated(ln.isAutoGenerated());
    rec.setChangeBy(ln.getChangeBy());
    rec.setComp(ln.getComp());
    rec.setCostCentre(ln.getCostCentre());
    rec.setCreateBy(ln.getCreateBy());
    rec.setCreditValue(ln.getCreditValue());
    rec.setDebitValue(ln.getDebitValue());
    rec.setDocAmount(ln.getDocAmount());
    rec.setDocHeaderBase(hdr);
    rec.setGlAccount(ln.getGlAccount());
    rec.setLineText(ln.getLineText());
    rec.setLineType(ln.getLineType());
    rec.setPostType(ln.getPostType());
    rec.setProgramme(ln.getProgramme());
    rec.setReference1(ln.getReference1());
    rec.setReference2(ln.getReference2());
    rec.setSortOrder(ln.getSortOrder());
    //rec.setVatCode(ln.getVatCode());
    return rec;
   }
  public DocFiRec getJnl() {
    if(jnl == null){
     jnl = new DocFiRec();
    }
    
    return jnl;
  }

  public boolean isLineDisabled() {
    return lineDisabled;
  }

  public void setLineDisabled(boolean lineDisabled) {
    this.lineDisabled = lineDisabled;
  }

 public List<LineTypeRuleRec> getLineTypes() {
  return lineTypes;
 }

 public void setLineTypes(List<LineTypeRuleRec> lineTypes) {
  this.lineTypes = lineTypes;
 }

  
  public boolean isAddLineDisabled() {
    logger.log(INFO, "isAddLineDisabled: {0}", addLineDisabled);
    return addLineDisabled;
  }

  public void setAddLineDisabled(boolean addLineDisabled) {
    this.addLineDisabled = addLineDisabled;
  }

 

  public void updateCanAddLine(){
    logger.log(INFO, "updateCanAddLine glaccount id: {0} post code {1} amount {2}",
            new Object[] {glDocLine.getGlAccount(),glDocLine.getPostType(),
            glDocLine.getDocAmount()});
    logger.log(INFO, "GL account id: {0}, post code id: {1} amouny {2}",
            new Object[]{glDocLine.getGlAccount(),glDocLine.getPostType(),
            glDocLine.getDocAmount() > 0});
    logger.log(INFO, "canAddLine {0} ",addLineDisabled);
    if(glDocLine.getGlAccount().getId() == null || glDocLine.getPostType().getId() == null ||
            glDocLine.getDocAmount() == 0 ){
      logger.log(INFO, "entry not set");
    }else{
      addLineDisabled = false;
    }
    /*if(glDocLine.getGlAccount().getId()> 1 && glDocLine.getPostType().getId() > 1 &&
       glDocLine.getDocAmount() > 0     ){
      canAddLine = true;
    }*/
    logger.log(INFO, "canAddLine {0} ",addLineDisabled);
  }

  
  public String onDocFlow(FlowEvent event) {
    String currentStepId = event.getOldStep();
    String nextStepId = event.getNewStep();
    logger.log(INFO, "Doc flow called current step {0} next step {1}",
            new Object[] {currentStepId,nextStepId});
    if(currentStepId.equals("header") && nextStepId.equals("lines")){
     if(partialDocCompl){
      return "overview";
     }
     // about to show lines
     if(!jnl.getCompany().isVatReg()){
      jnl.setVatable(false);
     }else{
      // company is vatable is doc in vat reg period
      List<VatRegistrationRec> vatRegs = jnl.getCompany().getVatRegistrations();
      if(vatRegs == null || vatRegs.isEmpty()){
       jnl.setVatable(false);
      }else{
       boolean foundReg = false;
       ListIterator<VatRegistrationRec> vli = vatRegs.listIterator();
       while(vli.hasNext() && !foundReg){
        VatRegistrationRec curr = vli.next();
        if(curr.getRegistrationDate().before(jnl.getTaxDate()) && curr.getRegistrationEnd().after(jnl.getTaxDate())){
         jnl.setVatable(true);
         foundReg = true;
        }
       }
       if(foundReg){
        jnl.setVatable(false);
       }
      }
     }
     logger.log(INFO, "vatable doc {0}", jnl.isVatable());
     logger.log(INFO, "glDocLine {0}", glDocLine);
     logger.log(INFO, "glaccounts {0} post codes {0}", new Object[]{getGlAccounts(),getPostTypes()});
     if(glDocLine == null){
      glDocLine = new DocLineGlRec();
     }
     if(getGlAccounts() == null || getGlAccounts().isEmpty()){
      
      MessageUtil.addWarnMessageParam("fiDocNoCompGlAcs", "validationText", jnl.getCompany().getReference());
      return currentStepId;
     }
      FiGlAccountCompRec glAcnt = getGlAccounts().get(0);
      logger.log(INFO, "Sort order {0}", glAcnt.getSortOrder().getSortCode());
      String sortStr = determineSortOrder(jnl, glDocLine, glAcnt, jnl.getCompany().getLocale());
      glDocLine.setSortOrder(sortStr);
      glDocLine.setGlAccount(glAcnt);
      glDocLine.setPostType(getPostTypes().get(0));
      
     
     
    } else if(currentStepId.equals("lines") && nextStepId.equals("overview")){
     if(this.numLines < 1){
      MessageUtil.addWarnMessage("fiDocLinesNone", "validationText");
      nextStepId = currentStepId;
     }
    }else if(currentStepId.equals("overview") && nextStepId.equals("lines") ){
     
    }
    return nextStepId;


}

  public String onPartialDocFlow(FlowEvent event){
   logger.log(INFO, "onPartialDocFlow called new step {0}", event.getNewStep());
   String currentStepId = event.getOldStep();
    String nextStepId = event.getNewStep();
    logger.log(INFO, "Doc flow called current step {0} next step {1}",
            new Object[] {currentStepId,nextStepId});
    if(currentStepId.equals("header") && nextStepId.equals("lines")){
     
     if(this.partialDocCompl){
      if(glDocLines == null || glDocLines.isEmpty()){
       glDocLines = new ArrayList<>();
       this.partialDocSelected = this.docMgr.getDocFiPartialLines(partialDocSelected);
       List<DocLineGlPartialRec> partialLines = partialDocSelected.getGlLines();
       if(partialLines != null){
        for(DocLineGlPartialRec ptLn:partialLines){
         logger.log(INFO, "ptLn {0}", ptLn.getId());
         DocLineGlRec glLineRec = this.buildDocLineGlRec(ptLn, jnl);
         if(glLineRec.getPostType().isDebit()){
          this.totalDebits += glLineRec.getDocAmount();
          this.docbalance += glLineRec.getDocAmount();
         }else{
          this.totalCredits += glLineRec.getDocAmount();
          this.docbalance -= glLineRec.getDocAmount();
         }
         glDocLines.add(glLineRec);
        }
       }
       logger.log(INFO, "glDocLines {0}", glDocLines);
       logger.log(INFO, "glDocLine {0}", glDocLine);
       if(glDocLine == null){
        glDocLine = new DocLineGlRec();
        glDocLine.setComp(
                partialDocSelected.getCompany());
        
       }
       
      }
      if(docbalance == 0 && glDocLines != null && !glDocLines.isEmpty()){
       canPost = true;
      }
      return "overview";
     }else{
      if(glDocLine == null){
       glDocLine = new DocLineGlRec();
       glDocLine.setGlAccount(getGlAccounts().get(0));
       glDocLine.setPostType(this.getPostTypes().get(0));
      }
      logger.log(INFO, "glDocLine gl account {0}", glDocLine.getGlAccount());
      logger.log(INFO, " jnl vat {0}", jnl.isVatable());
      if(!jnl.getCompany().isVatReg()){
       jnl.setVatable(false);
      }else{
      // company is vatable is doc in vat reg period
       List<VatRegistrationRec> vatRegs = jnl.getCompany().getVatRegistrations();
       if(vatRegs == null || vatRegs.isEmpty()){
        jnl.setVatable(false);
       }else{
        boolean foundReg = false;
        ListIterator<VatRegistrationRec> vli = vatRegs.listIterator();
        while(vli.hasNext() && !foundReg){
         VatRegistrationRec curr = vli.next();
         if(curr.getRegistrationDate().before(jnl.getTaxDate()) && curr.getRegistrationEnd().after(jnl.getTaxDate())){
          jnl.setVatable(true);
          foundReg = true;
         }
        }
        if(foundReg){
         jnl.setVatable(false);
        }
       }
      }
      
      if(glDocLine.getGlAccount() == null){
        glDocLine.setGlAccount(this.getGlAccounts().get(0));
       }
       if(glDocLine.getSortOrder() == null || glDocLine.getSortOrder().isEmpty()){
        
        
        logger.log(INFO, "glAccountSelected {0} ", glDocLine.getGlAccount());
        String sort = this.determineSortOrder(partialDocSelected, glDocLine, glDocLine.getGlAccount(),
                partialDocSelected.getCompany().getLocale());
        glDocLine.setSortOrder(sort);
       }
       logger.log(INFO, "Sort order {0}",glDocLine.getSortOrder());
     }
    }else if(currentStepId.equals("overview") && nextStepId.equals("lines")){
     
     // about to show lines
     logger.log(INFO, "glDocLine {0}", glDocLine);
     logger.log(INFO, "glaccounts {0} post codes {0}", new Object[]{getGlAccounts(),getPostTypes()});
     if(glDocLine == null){
      glDocLine = new DocLineGlRec();
     }
     if(getGlAccounts() == null || getGlAccounts().isEmpty()){
      MessageUtil.addWarnMessageParam("fiDocNoCompGlAcs", "errorText", docComp.getReference());
      return currentStepId;
     }
      FiGlAccountCompRec glAcnt = getGlAccounts().get(0);
      logger.log(INFO, "Sort order {0}", glAcnt.getSortOrder().getSortCode());
      String sortStr = determineSortOrder(jnl, glDocLine, glAcnt, jnl.getCompany().getLocale());
      glDocLine.setSortOrder(sortStr);
      glDocLine.setGlAccount(glAcnt);
      glDocLine.setPostType(getPostTypes().get(0));
      
     
     
    } else if(currentStepId.equals("lines") && nextStepId.equals("overview")){
     logger.log(INFO, "Lines to overview lines {0}", numLines);
     if(this.numLines < 1){
      MessageUtil.addWarnMessage("fiDocLinesNone", "validationText");
      nextStepId = currentStepId;
     }
     if(docbalance == 0 && glDocLines != null && !glDocLines.isEmpty()){
      canPost = true;
     }
     
    }else if(currentStepId.equals("overview") && nextStepId.equals("lines") ){
     
    } 
     
    
    
    return nextStepId;
  }
public void onDocSavePartial(){
 logger.log(INFO, "onDocSavePartial called");
 UserRec crUsr = this.getLoggedInUser();
 DocFiPartialRec partDoc = this.buildDocFiPartialRec(jnl);
 List<DocLineGlPartialRec> pLns = new ArrayList<>();
 for(DocLineGlRec glLn:glDocLines){
  DocLineGlPartialRec pLn = this.buildDocLineGlPartialRec(glLn, partDoc);
  pLn.setCreateBy(crUsr);
  pLn.setCreateDate(new Date());
  pLns.add(pLn);
  }
 partDoc.setGlLines(pLns);
 partDoc.setCreateBy(crUsr);
 partDoc.setCreateDate(new Date());
}

 public boolean isPartialDocCompl() {
  return partialDocCompl;
 }

 public void setPartialDocCompl(boolean partialDocCompl) {
  this.partialDocCompl = partialDocCompl;
 }

 public List<DocFiPartialRec> getPartialDocs() {
  if(partialDocs == null){
   if(docComp == null || docComp.getId() == null){
    docComp = getCompList().get(0);
   }
   partialDocs = this.docMgr.getDocsFiPartialForComp(docComp);
  }
  return partialDocs;
 }

 public void setPartialDocs(List<DocFiPartialRec> partialDocs) {
  this.partialDocs = partialDocs;
 }

 public DocFiPartialRec getPartialDocSelected() {
  return partialDocSelected;
 }

 public void setPartialDocSelected(DocFiPartialRec partialDocSelected) {
  this.partialDocSelected = partialDocSelected;
 }

 public List<DocFiPartialRec> getPartialDocsFiltered() {
  return partialDocsFiltered;
 }

 public void setPartialDocsFiltered(List<DocFiPartialRec> partialDocsFiltered) {
  this.partialDocsFiltered = partialDocsFiltered;
 }

 

  public String getPostType() {
    return postType;
  }

  public void setPostType(String postType) {
    this.postType = postType;
  }

  public String getAccountRef() {
    return accountRef;
  }

  public void setAccountRef(String accountRef) {
    this.accountRef = accountRef;
  }

  public List<String> glAccountComplete(String input){
   logger.log(INFO, "glAccountComplete called with {0}", input);
    List<String> results = new ArrayList<>();
    if(glAccounts == null){
      this.getGlAccounts();
    }
    boolean all = input.isEmpty();
    ListIterator<FiGlAccountCompRec> li = glAccounts.listIterator();
    while(li.hasNext()){
      FiGlAccountCompRec rec = li.next();
      if(all){
        results.add(rec.getCoaAccount().getRef());
      }else if(rec.getCoaAccount().getRef().toLowerCase().startsWith(input.toLowerCase())){
        results.add(rec.getCoaAccount().getRef());
      }
    }
    return results;
  }

  public List<String> postTypeComplete(String input){
    logger.log(INFO, "postTypeComplete called with {0}", input);
    List<String> results = new ArrayList<>();
    if(postTypes == null){
      this.getPostTypes();
    }
    boolean all = input.isEmpty();
    ListIterator<PostTypeRec> li = postTypes.listIterator();
    while(li.hasNext()){
      PostTypeRec rec = li.next();

      logger.log(INFO, "rec type code {0} input{1}", new Object[]{rec.getPostTypeCode(),input});
      if(all){
       results.add(rec.getPostTypeCode());

      }else if(rec.getPostTypeCode().toLowerCase().startsWith(input.toLowerCase()))
        results.add(rec.getPostTypeCode());
    }
    return results;
  }

  public boolean isCanPost() {
    return canPost;
  }

  public void setCanPost(boolean canPost) {
    this.canPost = canPost;
  }

  public int getNumLines() {
    return numLines;
  }

  public void setNumLines(int numLines) {
    this.numLines = numLines;
  }

  

  public double getTotalCredits() {
    return totalCredits;
  }

  public void setTotalCredits(double totalCredits) {
    this.totalCredits = totalCredits;
  }

  public double getTotalDebits() {
    return totalDebits;
  }

  public void setTotalDebits(double totalDebits) {
    this.totalDebits = totalDebits;
  }

  public double getDocbalance() {
    return docbalance;
  }

  public void setDocbalance(double docbalance) {
    this.docbalance = docbalance;
  }

 public CompanyManager getCompMgr() {
  return compMgr;
 }

 public void setCompMgr(CompanyManager compMgr) {
  this.compMgr = compMgr;
 }

 public List<CostCentreRec> getCostCentres() {
  return costCentres;
 }

 public void setCostCentres(List<CostCentreRec> costCentres) {
  this.costCentres = costCentres;
 }

 @Override
 public DocFiRec getFiDoc() {
  if(this.fiDoc == null){
   fiDoc = new DocFiRec();
  }
  return fiDoc;
 }

 @Override
 public void setFiDoc(DocFiRec fiDoc) {
  this.fiDoc = fiDoc;
 }

  
  
  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  

  public ArrayList<DocLineGlRec> getGlDocLines() {
    return glDocLines;
  }

  public void setGlDocLines(ArrayList<DocLineGlRec> glDocLines) {
    this.glDocLines = glDocLines;
  }

 public DocLineGlRec getGlDocLineSelected() {
  return glDocLineSelected;
 }

 public void setGlDocLineSelected(DocLineGlRec glDocLineSelected) {
  this.glDocLineSelected = glDocLineSelected;
 }

  public ArrayList<JnlSummaryLine> getSummaryLines() {
    return summaryLines;
  }

  public void setSummarylines(ArrayList<JnlSummaryLine> summaryLines) {
    this.summaryLines = summaryLines;
  }

  public JnlSummaryLine getSelectedSummLine() {
    logger.log(INFO,"getSelectedSummLine called {0}",selectedSummLine);
    return selectedSummLine;
  }

  public void setSelectedSummLine(JnlSummaryLine selectedSummLine) {
    logger.log(INFO,"setSelectedSummLine called selectedSummLine {0}",selectedSummLine);
    logger.log(INFO,"setSelectedSummLine called this.selectedSummLine {0}",this.selectedSummLine);
    this.selectedSummLine = selectedSummLine;
  }

  public ArrayList<DocTypeRec> getGlDocTypes() {
    if(glDocTypes == null){
      try {
        glDocTypes = sysBuff.getLedgerDocumentTypes("GL", this.getUser());
        if(!glDocTypes.isEmpty()){
          docTypeSelected = glDocTypes.get(0);
        }

      } catch (ServletException ex) {
        Logger.getLogger(GlStdJnl.class.getName()).log(Level.SEVERE, null, ex);
        GenUtil.addErrorMessage("Could not load document types due to: "+ex.getLocalizedMessage());
      }
    }
    return glDocTypes;
  }

  public void setGlDocTypes(ArrayList<DocTypeRec> glDocTypes) {
    this.glDocTypes = glDocTypes;
  }

 public List<TransactionTypeRec> getGlStdLine() {
  return glTransTypes;
 }

 public void setGlStdLine(List<TransactionTypeRec> glStdLine) {
  this.glTransTypes = glStdLine;
 }

 public List<TransactionTypeRec> getGlTransTypes() {
  return glTransTypes;
 }

 public void setGlTransTypes(List<TransactionTypeRec> glTransTypes) {
  this.glTransTypes = glTransTypes;
 }

 public boolean isJnlPosted() {
  return jnlPosted;
 }

 public void setJnlPosted(boolean jnlPosted) {
  this.jnlPosted = jnlPosted;
 }

 
 public DocTypeRec getDocTypeSelected() {
  return docTypeSelected;
 }

 public void setDocTypeSelected(DocTypeRec docTypeSelected) {
  this.docTypeSelected = docTypeSelected;
 }

  
  public List<FiGlAccountCompRec> getGlAccounts() {
    if(glAccounts == null || glAccounts.isEmpty()){
      try{
        logger.log(INFO, "Need to get GL accounts for company: {0}", this.getDocComp());
        if(this.getDocComp() != null){
          logger.log(INFO, "Document Company found get GL accounts for comp ref {0}", this.getDocComp().getReference());
          logger.log(INFO, "docComp gl accounts: {0}", this.getDocComp().getGlAccounts());
          glAccounts = this.glAcntMgr.getCompanyAccounts(getDocComp());
          logger.log(INFO, "Glaccounts {0}for comp id {1}", new Object[]{glAccounts,getDocComp().getId()});
         //glAccounts = new ArrayList<FiGlAccountCompRec>();
        }else if(jnl.getCompany() != null){
         logger.log(INFO, "Get accounts for jnl comp");
         glAccounts = glAcntMgr.getCompanyAccounts(jnl.getCompany());
        }
      }catch(EJBTransactionRolledbackException ex){
        GenUtil.addErrorMessage("Could not find Company GL accounts reason: "+ex.getLocalizedMessage());
      }
    }
    return glAccounts;
  }

 public FiGlAccountCompRec getGlAccountSelected() {
  return glAccountSelected;
 }

 public void setGlAccountSelected(FiGlAccountCompRec glAccountSelected) {
  this.glAccountSelected = glAccountSelected;
 }



  public DocLineGlRec getGlDocLine() {
    if(glDocLine == null){
      glDocLine = new DocLineGlRec();
      UUID uuid = UUID.randomUUID();
      long id = uuid.getLeastSignificantBits();
      glDocLine.setId(id);
    }
    return glDocLine;
  }

  public void setGlDocLine(DocLineGlRec glDocLine) {
    this.glDocLine = glDocLine;
  }

  public JnlSummaryLine getSummaryLine() {
    return summaryLine;
  }

  public void setSummaryLine(JnlSummaryLine summaryLine) {
    this.summaryLine = summaryLine;
  }

  
  public Long getSelectedPostType() {
    return selectedPostType;
  }

  public void setSelectedPostType(Long selectedPostType) {
    this.selectedPostType = selectedPostType;
  }

  

  


  public ArrayList<PostTypeRec> getPostTypes() {
    logger.log(INFO, "std Jnl - getPostTypes called postTypes is {0}", postTypes);
    if(postTypes == null){
      logger.log(INFO, "Need to get post types from system buffer");
      try{
        postTypes = sysBuff.getPostCodesForLedger("GL");
        logger.log(INFO, "Sys buffer returned post types {0}",postTypes);
        ListIterator<PostTypeRec> li = postTypes.listIterator();
        while(li.hasNext()){
          PostTypeRec rec = li.next();
          logger.log(INFO, "std Jnl Post type name {0} is debit {1} sign {2}",
                  new Object[]{rec.getDescription(),rec.isDebit(),rec.getSign()});
        }
      }catch(BacException ex){
        logger.log(INFO, "error from sysBuff {0}",ex.getLocalizedMessage());
      }catch(TransactionRolledbackLocalException ex){
        logger.log(INFO, "Trans roll back error from sysBuff {0}",ex.getLocalizedMessage());
      }
    }
    logger.log(INFO, "postTypes returned {0}", postTypes);
    return postTypes;
  }

  public void setPostTypes(ArrayList<PostTypeRec> postTypes) {
    this.postTypes = postTypes;
  }

 public List<ProgrammeRec> getProgrammes() {
  return programmes;
 }

 public void setProgrammes(List<ProgrammeRec> programmes) {
  this.programmes = programmes;
 }

  
  public ArrayList<RestFundPostBalance> getFundPostingBals() {
    return fundPostingBals;
  }

  public void setFundPostingBals(ArrayList<RestFundPostBalance> fundPostingBals) {
    this.fundPostingBals = fundPostingBals;
  }

 
  
  public void onDocDateSelect(SelectEvent event) {
    logger.log(INFO, "docDateselect called with {0}",event.getObject());
    Date date = (Date)event.getObject();
    this.jnl.setDocumentDate(date);
    this.jnl.setTaxDate(date);
    logger.log(INFO, "Dates Doc date {0} posting date {1}",
            new Object[]{jnl.getDocumentDate(),jnl.getPostingDate()});
    if(jnl.getDocumentDate() != null && jnl.getPostingDate() != null){
      lineDisabled = false;
    }else{
      lineDisabled = true;
    }
    logger.log(INFO,"lineDisabled: {0}",lineDisabled );

  }

  public void onPartialJnlDispl(){
   logger.log(INFO, "onPartialJnlDispl called selected {0}",partialDocSelected);
   logger.log(INFO, "partial lines {0}", partialDocSelected.getGlLines());
   partialDocSelected = this.docMgr.getDocFiPartialLines(partialDocSelected);
   List<DocLineGlPartialRec> partLines = partialDocSelected.getGlLines();
   
   logger.log(INFO, "partial lines 2 {0}", partialDocSelected.getGlLines());
   jnl = this.buildDocFiRec(partialDocSelected);
   
   if(partLines != null){
    List<DocLineBaseRec> docLines = new ArrayList<>();
    for(DocLineGlPartialRec partLn: partLines){
     DocLineGlRec ln = this.buildDocLineGlRec(partLn, jnl);
     docLines.add(ln);
    }
    jnl.setDocLines(docLines);
    this.numLines = jnl.getDocLines().size();
   }
   logger.log(INFO, "jnl.getDocLines {0}", jnl.getDocLines());
   if(jnl.getDocLines() == null){
    partialDocCompl = false;
   }else{
    partialDocCompl = jnl.getDocLines().size() > 0?true:false;
   }
   
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editPartDocFrm");
   pf.executeScript("PF('editPtDocWv').show()");
   
  }

  public void onPartialPostAsFiDoc(){
   logger.log(INFO, "onPartialPostAsFiDoc with partial doc id {0}", partialDocSelected.getId());
   if(docbalance != 0){
    MessageUtil.addWarnMessage("DocJnlNotBal", "validationText");
    return;
   }
   UserRec crUsr =this.getLoggedInUser();
   Date crDate = new Date();
   
   jnl.setCreatedBy(crUsr);
   jnl.setCreateOn(crDate);
   jnl = this.buildDocFiRec(partialDocSelected);
   List<DocLineBaseRec> fiLines = new ArrayList<>();
   for(DocLineGlRec glLine: glDocLines){
    glLine.setCreateBy(crUsr);
    glLine.setCreateDate(crDate);
    fiLines.add(glLine);
   }
   jnl.setDocLines(fiLines);
   logger.log(INFO, "jnl {0} lines {1}", new Object[]{jnl,jnl.getDocLines()});
   postDocument();
   if(jnlPosted){
    ListIterator<DocFiPartialRec> partDocsLi = partialDocs.listIterator();
    boolean foundPartDoc = false;
    while(partDocsLi.hasNext() && !foundPartDoc){
     DocFiPartialRec partDoc = partDocsLi.next();
     if(partDoc.getId() == partialDocSelected.getId()){
      boolean deleted = this.docMgr.partialDocDelete(partDoc);
      logger.log(INFO, "Part doc deleted {0}", String.valueOf(deleted));
      if(deleted){
       partDocsLi.remove();
       PrimeFaces pf = PrimeFaces.current();
       pf.ajax().update("docsTbl");
       pf.executeScript("PF('editPtDocWv').hide()");
            
      }
     }
    }
    
   }
   
  }
  
  public String onPostStdJnlAction(){
   if(this.jnlPosted){
    return "home";
   }else{
    return null;
   }
  }
  public void onPostDateSelect(SelectEvent event) {
    logger.log(INFO, "onPostDateSelect called with {0}",event.getObject());
    logger.log(INFO, "Call compMgr.getFiscalPeriodYearForDate ");
   
    
    logger.log(INFO, "Dates Doc date {0} posting date {1}",
            new Object[]{jnl.getDocumentDate(),jnl.getPostingDate()});
    if(jnl.getDocumentDate() != null && jnl.getPostingDate() != null){
      lineDisabled = false;
    }else{
      lineDisabled = true;
    }
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("period");
   pf.ajax().update("fisYr");
   
  }

  public void onGlActSelect(ValueChangeEvent evt){
    logger.log(INFO, "onGlActSelect called with new val {0}", evt.getNewValue());
    glAccountSelected = (FiGlAccountCompRec) evt.getNewValue();
    glDocLine.setGlAccount(glAccountSelected);
    String sortStr = determineSortOrder(jnl, glDocLine, glAccountSelected, jnl.getCompany().getLocale());
    glDocLine.setSortOrder(sortStr);
    
    
    PrimeFaces pf = PrimeFaces.current();
    ArrayList<String> toUpdate = new ArrayList<>();
    toUpdate.add("defSort");
    toUpdate.add("glActName");
    toUpdate.add("addLn");
    pf.ajax().update(toUpdate);
    updateCanAddLine();
  }

  public void onCompanyChange(ValueChangeEvent evt){
   logger.log(INFO, "New company {0}", (CompanyBasicRec)evt.getNewValue());
   jnl.setCompany((CompanyBasicRec)evt.getNewValue());
   PrimeFaces pf = PrimeFaces.current();
   ArrayList<String> toUpdate = new ArrayList<>();
   toUpdate.add("period");
   toUpdate.add("fisYr");
   pf.ajax().update(toUpdate);
  }
  
  public List<CostCentreRec> onCostCentComplete(String input){
   
   List<CostCentreRec> retList = new ArrayList<>();
   if(this.costCentres == null){
    costCentres = this.costCentreMgr.getCostCentresForCompany(jnl.getCompany());
   }else{
    CostCentreRec cc = costCentres.get(0);
    
    if(cc.getCostCentreForCompany().getId() != jnl.getCompany().getId()){
     costCentres = this.costCentreMgr.getCostCentresForCompany(jnl.getCompany());
    }
   }
   
   if(input == null || input.isEmpty()){
    return costCentres;
   }else{
    for(CostCentreRec cc: costCentres){
     if(cc.getRefrence().contains(input)){
      retList.add(cc);
     }
    }
    return retList;
   }
   
   
   
  }
  
  public void onDeleteLine(){
    logger.log(INFO,"onDeleteLine called selected {0}",glDocLineSelected);
    boolean found = false;
    ListIterator<DocLineGlRec> li = glDocLines.listIterator();
    while(li.hasNext() && !found){
      DocLineGlRec glLine = li.next();
      if(glLine.getId() == glDocLineSelected.getId()){
       if(glDocLineSelected.getPostType().isDebit()){
        totalDebits -= glDocLineSelected.getDocAmount();
        docbalance -= glDocLineSelected.getDocAmount();
       }else{
        totalCredits -= glDocLineSelected.getDocAmount();
        docbalance += glDocLineSelected.getDocAmount();
       }
        li.remove();
        found = true;
      }
    }
    numLines = glDocLines.size();
    if(numLines == 0){
     canPost = false;
    }else{
     if(docbalance != 0){
      canPost = false;
     }
    }
    PrimeFaces pf = PrimeFaces.current();
    List<String> toUpdate = new ArrayList();
    toUpdate.add("postFiDocBtn");
    toUpdate.add("docSumTbl");
    toUpdate.add(":editPartDocFrm:docTotalsPG");
    toUpdate.add("numLns");
    
    pf.ajax().update(toUpdate);
    pf.executeScript("PF('delLnWvar').hide()");
    logger.log(INFO,"doclines {0}",glDocLines.size());
    logger.log(INFO,"numLines {0}",numLines);
  }
  
  public void onDelLineConfDlg(){
   PrimeFaces pf = PrimeFaces.current();
   pf.executeScript("PF('delLnWvar').show()");
  }

  public void onEdLineDlg(){
   logger.log(INFO, "wizard header to liness");
   if(glAccounts == null || glAccounts.isEmpty()){
      getGlAccounts();
   }
   logger.log(INFO, "glAccounts {0}", glAccounts);
   logger.log(INFO, "glDocLineSelected id {0}", glDocLineSelected.getId());
   String sortText = this.determineSortOrder(jnl, glDocLineSelected, glDocLineSelected.getGlAccount(), jnl.getCompany().getLocale());
   glDocLineSelected.setSortOrder(sortText);
   glDocLinePreEdit = new DocLineGlRec();
   glDocLinePreEdit.setDocAmount(this.glDocLineSelected.getDocAmount());
   glDocLinePreEdit.setPostType(glDocLineSelected.getPostType());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("editLineFrm");
   pf.executeScript("PF('editLineDlgWv').show()");
  }
  /**
   * Add a line to the document
   */
  public void onAddLine(){
    logger.log(INFO,"onAddLine called");
    logger.log(INFO, "Post selected post type id {0} glAccount id: {1}",
            new Object[]{glDocLine.getPostType().getId(),glDocLine.getGlAccount().getId()});
    Long ptyId = glDocLine.getPostType().getId();
    Long tempId;
    
    ListIterator<PostTypeRec> ptLi = postTypes.listIterator();
    boolean found = false;
    while(ptLi.hasNext() && !found){
      PostTypeRec pTyRec = ptLi.next();
      if(pTyRec.getId() == ptyId){
        found = true;
        glDocLine.setPostType(pTyRec);
      }
    }
    
    ListIterator<LineTypeRuleRec> lnTyLi = lineTypes.listIterator();
    boolean foundType = false;
    while(lnTyLi.hasNext() && !foundType){
     LineTypeRuleRec curr = lnTyLi.next();
     if(curr.getLineCode().equals(GlStdJnl.GLLINE)){
      glDocLine.setLineType(curr);
      foundType = true;
     }
    }
    
    boolean isDebitLine = glDocLine.getPostType().isDebit();
    logger.log(INFO," addline isDebitLine: {0}",isDebitLine);
    // is there a restricted fund balance
    logger.log(INFO, "before addFundBal glDocLine.getRestrictedFundId() : {0}",glDocLine.getRestrictedFund());
    if(glDocLine.getRestrictedFund() != null && glDocLine.getRestrictedFund().getId() != null && glDocLine.getRestrictedFund().getId() != 0  ){
      logger.log(INFO, "before addFundBal fundBals : {0}", fundBals);
      

      this.addFundBal(glDocLine);
      logger.log(INFO, "after addFundBal fundBals : {0}", fundBals);
      ListIterator<RestFundPostBalance> fundBalsIt = fundBals.listIterator();
      while(fundBalsIt.hasNext()){
        RestFundPostBalance fbal = fundBalsIt.next();
        logger.log(INFO, "Fund id {0} balance {1}", new Object[]{fbal.getId(),fbal.getBalance()});
      }
    }

    if(glDocLines == null){
       glDocLines = new  ArrayList<>();
       summaryLines = new ArrayList<>();
    }
    glDocLine.setLineNum(Long.valueOf(glDocLines.size()));
    tempId = UUID.randomUUID().getLeastSignificantBits();
    glDocLine.setId(tempId);
    logger.log(INFO, "Add line with id {0}", glDocLine.getId());
 
    glDocLines.add(glDocLine);
    numLines++;
    if(isDebitLine){
      //debit posting
      totalDebits = totalDebits + glDocLine.getDocAmount();
      
    }else{
      //credit posting
      totalCredits = totalCredits + glDocLine.getDocAmount();
    }
    docbalance =  totalDebits - totalCredits ;
    if(docbalance == 0){
      this.canPost = true;
    }
    glDocLine = new DocLineGlRec();
    glDocLine.setPostType(this.getPostTypes().get(0));
    glDocLine.setGlAccount(this.getGlAccounts().get(0));
    UUID uuid = UUID.randomUUID();
    long id = uuid.getMostSignificantBits();
    glDocLine.setId(id);
    
    addLineDisabled= true;
    PrimeFaces pf = PrimeFaces.current();
    ArrayList<String> toUpdate = new ArrayList<>();
    toUpdate.add("docTotalsPG");
    toUpdate.add("postBtn");
    toUpdate.add("messages");
    toUpdate.add("lineDet");
    pf.ajax().update(toUpdate);
    //set the post key and GL accounts for the current line item
    MessageUtil.addInfoMessage("docLineAdded", "blacResponse");

  }
  
public void onTransPartIalDoc(){
 logger.log(INFO, "onTransPartIalDoc called");
 UserRec currUsr = this.getLoggedInUser();
 Date currDate = new Date();
 if(partialDocSelected.getCreateBy() == null){
  partialDocSelected.setCreateBy(currUsr);
  partialDocSelected.setCreateDate(currDate);
 }else{
  partialDocSelected.setChangeBy(currUsr);
  partialDocSelected.setChangeDate(currDate);
 }
 //glStdJnl.glDocLines
 partialDocSelected.setCompany(docComp);
 logger.log(INFO, "partialDocSelected id {0} ", partialDocSelected.getId());
 logger.log(INFO, "partialDocSelected changed by id {0}", partialDocSelected.getChangeBy().getId());
 logger.log(INFO, "glDocLines {0}", glDocLines);
 if(glDocLines != null && !glDocLines.isEmpty()){
  List<DocLineGlPartialRec> glDocPartialLines = new ArrayList<>();
  for(DocLineGlRec glLine: glDocLines){
   DocLineGlPartialRec partialLine = buildDocLineGlPartialRec(glLine, partialDocSelected);
   if(partialLine.getId() < 1){
    partialLine.setId(null);
    partialLine.setCreateBy(currUsr);
    partialLine.setCreateDate(currDate);
    partialLine.setComp(partialDocSelected.getCompany());
   }else{
    partialLine.setChangeBy(currUsr);
    partialLine.setChangeDate(currDate);
   }
   
   logger.log(INFO, "partialLine id {0}", partialLine.getId());
   
   
   glDocPartialLines.add(partialLine);
  }
  partialDocSelected.setGlLines(glDocPartialLines);
 }
 partialDocSelected = docMgr.postStdJnlPartial(partialDocSelected, getView());
 
 
 ListIterator<DocFiPartialRec> ptDocLi = partialDocs.listIterator();
 boolean docFound = false;
 while(ptDocLi.hasNext() && !docFound){
  DocFiPartialRec ptDoc = ptDocLi.next();
  if(ptDoc.getId() == partialDocSelected.getId()){
   docFound = true;
   ptDocLi.set(partialDocSelected);
  }
 }
 if(!docFound){
  partialDocs.add(partialDocSelected);
 }
 PrimeFaces pf = PrimeFaces.current();
 pf.ajax().update("docsTbl");
 pf.executeScript("PF('editPtDocWv').hide()");
}

 public void onTransEdLine(){
  logger.log(INFO, "onTransEdLine called edit line {0}", this.glDocLineSelected);
  ListIterator<DocLineGlRec> li = glDocLines.listIterator();
  while(li.hasNext()){
   DocLineGlRec oldLn = li.next();
   if(oldLn.getId() == glDocLineSelected.getId()){
    logger.log(INFO, "glDocLinePreEdit post type  debit {0}", glDocLinePreEdit.getPostType().isDebit());
    logger.log(INFO, "glDocLineSelected post type debit {0}", glDocLineSelected.getPostType().isDebit());
    // found changed line
    if(glDocLinePreEdit.getPostType().isDebit()){
     totalDebits -= glDocLinePreEdit.getDocAmount();
     docbalance -= glDocLinePreEdit.getDocAmount();
    }else{
     totalCredits -= glDocLinePreEdit.getDocAmount();
     docbalance += glDocLinePreEdit.getDocAmount();
    }
    logger.log(INFO, "after remove old bals totalDebits {0} totalCredits {1} bal {2}", new Object[]{totalDebits,totalCredits,docbalance});
    if(glDocLineSelected.getPostType().isDebit()){
     totalDebits += glDocLineSelected.getDocAmount();
     docbalance += glDocLineSelected.getDocAmount();
    }else{
     totalCredits += glDocLineSelected.getDocAmount();
     docbalance -= glDocLineSelected.getDocAmount();
    }
    logger.log(INFO, "totalDebits {0} totalCredits {1} bal {2}", new Object[]{totalDebits,totalCredits,docbalance});
    li.set(glDocLineSelected);
    PrimeFaces pf = PrimeFaces.current();
    List<String> toUpdate = new ArrayList<>();
    toUpdate.add("drTot");
    toUpdate.add("crTot");
    toUpdate.add("docBal");
    toUpdate.add("docSumTbl");
    pf.ajax().update(toUpdate);
    pf.executeScript("PF('editLineDlgWv').hide()");
   }
  }
 }

  private DocLineGlRec lineSetPostKeyGLAccount(DocLineGlRec line,long postKeyId, long glaccount){
    logger.log(INFO,"lineSetPostKeyGLAccount called with accouny {0} post key {1} account {2}",
            new Object[]{line,postKeyId,glaccount});
    //set post key
    ListIterator<PostTypeRec> li = this.postTypes.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
      PostTypeRec rec = li.next();

      if(rec.getId() == postKeyId){
        line.setPostType(rec);
        found = true;
        logger.log(INFO, "Doc postType set to id: {0}", glDocLine.getPostType().getId());
      }
    }
    
    //set GL accouny
    
    return line;
  }


 public List<FundRec> onRestrictedFundsComplete(String input){
  logger.log(INFO, "onRestrictedFundsComplete called with {0}", input);
  List<FundRec> fnds = new ArrayList<>();
  if(this.restrictedFunds == null){
   restrictedFunds = sysBuff.getRestrictedFunds(jnl.getCompany());
  }
  if(restrictedFunds == null){
   return new ArrayList<>();
  }
  if(input == null || input.isEmpty()){
   return restrictedFunds;
  }else{
   for(FundRec fnd: restrictedFunds){
    if(fnd.getFndCode().contains(input) ){
     fnds.add(fnd);
    }
   }
  }
  return fnds;
 }

  public void onRestrictedFundSelect(SelectEvent evt){
    logger.log(INFO, "onPostTypeSelect called with: {0}",evt.getObject().toString());
    if(glDocLine != null){
     glDocLine.setRestrictedFund((FundRec)evt.getObject());
    }else if(this.glDocLineSelected != null){
     glDocLineSelected.setRestrictedFund((FundRec)evt.getObject());
    }
    

    }

  public void onPostTypeSelect(SelectEvent evt){
    logger.log(INFO, "onPostTypeSelect called with: {0}",evt.getObject().toString());
    postType = evt.getObject().toString();
    logger.log(INFO, "postType: {0}",postType);
    ListIterator<PostTypeRec> li = this.postTypes.listIterator();
    boolean found = false;
    while(li.hasNext() && !found){
      PostTypeRec rec = li.next();

      if(rec.getPostTypeCode().equalsIgnoreCase(postType) ){
        glDocLine.setPostType(rec);
        found = true;
        logger.log(INFO, "Doc postType set to id: {0}", glDocLine.getPostType().getId());
      }
    }



    updateCanAddLine();

  

  }
  
  public List<ProgrammeRec> onProgrammeComplete(String input){
   
   List<ProgrammeRec> retList = new ArrayList<>();
   if(programmes == null || programmes.isEmpty()){
    
    programmes = this.progMgr.getAllProgrammes(jnl.getCompany());
    if(programmes == null || programmes.isEmpty()){
     MessageUtil.addWarnMessage("maProgFoundNone", "validationText");
     return null;
    }
   }else{
    ProgrammeRec prog = programmes.get(0);
    if(prog.getProgrammeForCompany().getId() != jnl.getCompany().getId()){
     programmes = this.progMgr.getAllProgrammes(jnl.getCompany());
    }
   }
   
   if(input == null || input.isEmpty()){
    return programmes;
   }else{
    for(ProgrammeRec prog:programmes){
     if(prog.getReference().contains(input)){
      retList.add(prog);
     }
    }
    return retList;
   }
   
   
  }
  public void onPostTypeChange(ValueChangeEvent evt){
    logger.log(INFO, "onPostTypeChange old value {0} new value {1}",
            new Object[] {evt.getOldValue(),evt.getNewValue()});
    this.glDocLine.setPostType((PostTypeRec)evt.getNewValue());
    
    updateCanAddLine();
  }
  
  

   public void postDocument(){
      logger.log(INFO, "Web postDocument called");
      // check that funds balance
      if(fundBals != null){
      ListIterator<RestFundPostBalance> fndIt = fundBals.listIterator();
      boolean fndsBal = true;
      while(fndIt.hasNext()){
        RestFundPostBalance fndBal = fndIt.next();
        if(fndBal.getBalance() != 0.0){
          // set the name of the fund
          ListIterator<FundRec> fndNameIt = super.getRestrictedFunds().listIterator();
          boolean found = false;
          while(fndNameIt.hasNext() && !found){
            FundRec rec = fndNameIt.next();
            if(rec.getId() == fndBal.getId()){
              fndBal.setName(rec.getFndCode());
            }
          }

          GenUtil.addErrorMessage("Fund: "+fndBal.getName()+" has a balance of: "+fndBal.getBalance()
                  +". Not allowed. Fund must balance");
          fndsBal = false;
        }
      }
      logger.log(INFO, "Funds balance: {0}", fndsBal);
      if(!fndsBal){
        return;
      }
     }
      ListIterator<TransactionTypeRec> glTyLi = this.glTransTypes.listIterator();
      boolean glTransTyFound = false;
      while(glTyLi.hasNext() && !glTransTyFound){
       TransactionTypeRec tranTy = glTyLi.next();
       if(tranTy.getProcessCode().equals("StdGlJnl")){
        jnl.setTransactionType(tranTy);
       }
      }
      UserRec usr = this.getLoggedInUser();
      Date crDate = new Date();

      // add lines to document
      ArrayList<DocLineBaseRec> docLines = new ArrayList<>();
      DocLineBaseRec docLine;
      logger.log(INFO,"Number of glDocLines {0}",glDocLines.size());
      ListIterator<DocLineGlRec> glLineIt = glDocLines.listIterator();
      while(glLineIt.hasNext()){
        
        docLine = glLineIt.next();
        logger.log(INFO, "docLine id {0}", docLine.getId());
        
        docLine.setCreateBy(usr);
        docLine.setCreateDate(crDate);
        docLine.setDocHeaderBase(jnl);
        docLine.setComp(jnl.getCompany());
        docLine.setId(null);
        
        docLines.add(docLine);
      }


      logger.log(INFO,"number of docLines {0}",docLines.size());
       jnl.setCreatedBy(usr);
       jnl.setCreateOn(crDate);
      jnl.setDocLines(docLines);
      logger.log(INFO,"number of jnl. {0}",jnl.getDocLines().size());
      long postedDocNum;
      try{
        logger.log(INFO, "Call doc manager to post jnl");
        postedDocNum = docMgr.postStdJnl(this.jnl, usr, this.getView());
        MessageUtil.addInfoMessageVar1("stdLnlPosted", "blacResponse", String.valueOf(postedDocNum));
        jnlPosted = true;
        

      }catch(BacException ex){
        logger.log(INFO, "Post error {0}",ex.getLocalizedMessage());
        GenUtil.addErrorMessage("Doc posting failed");
      }
      
    }

  public void saveIncompleteDoc(){
   logger.log(INFO, "Web postDocument called");
   DocFiPartialRec jnlPart = this.buildDocFiPartialRec(jnl);
   ListIterator<TransactionTypeRec> glTyLi = this.glTransTypes.listIterator();
    boolean glTransTyFound = false;
    while(glTyLi.hasNext() && !glTransTyFound){
     TransactionTypeRec tranTy = glTyLi.next();
     logger.log(INFO, "Trans Ty process code {0}", tranTy.getProcessCode());
     if(tranTy.getProcessCode().equals("GLPTJN")){
      jnlPart.setTransactionType(tranTy);
      glTransTyFound = true;
     }
    }
    logger.log(INFO, "part Doc trans type {0}", jnlPart.getTransactionType().getProcessCode());
   jnlPart.setCreateBy(getLoggedInUser());
   jnlPart.setCreateDate(new Date());
   List<DocLineGlPartialRec> linesPart = new ArrayList<>();
   if(glDocLines != null && !glDocLines.isEmpty()){
    LineTypeRuleRec lType = this.sysBuff.getLineTypeRuleByCode("GLJNLPT");
    logger.log(INFO, "lType frpm sys buffer {0}", lType);
    Long lNum = 0l;
    for(DocLineGlRec ln: glDocLines){
     DocLineGlPartialRec lnPt = this.buildDocLineGlPartialRec(ln, jnlPart);
     lnPt.setLineType(lType);
     lNum++;
     lnPt.setLineNum(lNum);
     lnPt.setComp(jnl.getCompany());
     lnPt.setCreateBy(getLoggedInUser());
     lnPt.setCreateDate(new Date());
     linesPart.add(lnPt);
    }
    jnlPart.setGlLines(linesPart);
   }
   try{
    jnlPart = docMgr.postStdJnlPartial(jnlPart, getView());
    MessageUtil.addInfoMessageVar1("ptJnlSaved", "blacResponse", String.valueOf(jnlPart.getDocNumber()));
    jnlPosted = true;
   }catch(Exception ex){
    MessageUtil.addErrorMessage("docPtSave", "errorText");
   }
    
     
     
   }

  public void onLineAmountChange(ValueChangeEvent evt){
    logger.log(INFO, "onLineAmountChange old value {0} new value {1}",
            new Object[] {evt.getOldValue(),evt.getNewValue()});
    String val = String.valueOf(evt.getNewValue());
    logger.log(INFO, "Amount string {0}", val);
    glDocLine.setDocAmount(Double.valueOf(val));
    updateCanAddLine();
    PrimeFaces pf = PrimeFaces.current();
    pf.ajax().update("addLn");
  }

  public void onGlAccountSelect(SelectEvent evt){
    logger.log(INFO, "onGlAccountSelect called with {0}", evt.getObject().toString());
    String selValue = evt.getObject().toString();
    ListIterator<FiGlAccountCompRec> li = this.glAccounts.listIterator();
     boolean found = false;
     //FiGlAccountCompRec glac = glDocLine.getGlAccount();
     FiGlAccountCompRec glacRec;
     while(li.hasNext() && !found){
      glacRec = li.next();
      if(glacRec.getCoaAccount().getRef().equalsIgnoreCase(selValue) ){
       glDocLine.setGlAccount(glacRec);
       logger.log(INFO, "Set glDocLine to: {0}", glacRec.getCoaAccount().getName());
       logger.log(INFO, "Set glDocLine to: {0}", glDocLine.getGlAccount().getCoaAccount().getName());
      }
     }

     logger.log(INFO, "after getGlAccount {0}", glDocLine.getGlAccount().getCoaAccount().getName());
     glDocLine.setSortOrder("");
      SortOrderRec sort = glDocLine.getGlAccount().getSortOrder();
      logger.log(INFO, "gl account sort {0}", sort);
      if(sort.getSortCode() == null){
        logger.log(INFO, "gl account sort {0}", sort.getId());
        return;
      }
      if(sort.getSortCode().equalsIgnoreCase("DocDt")){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String docDateStr = fmt.format(jnl.getDocumentDate());

        glDocLine.setSortOrder(docDateStr);

      }else if(sort.getSortCode().equalsIgnoreCase("entryDt")){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String docDateStr = fmt.format(new Date());
        glDocLine.setSortOrder(docDateStr);
      } else if(sort.getSortCode().equalsIgnoreCase("postDt")){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String docDateStr = fmt.format(jnl.getPostingDate());
        glDocLine.setSortOrder(docDateStr);
      }
      logger.log(INFO, "sort set to {0}", glDocLine.getSortOrder());
      updateCanAddLine();
      GenUtil.addInfoMessage("End onGlAccountChange ");

  }
  
  

//Add facesmessage

 public List<VatCodeCompanyRec> getVatCodes() {
  if(vatCodes == null){
   
   vatCodes = this.sysBuff.getCompVatCodes(jnl.getCompany());
   if(vatCodes == null){
    return new ArrayList<>();
   }
  }
  return vatCodes;
 }

 public void setVatCodes(List<VatCodeCompanyRec> vatCodes) {
  this.vatCodes = vatCodes;
 }

 public void validatePostDate(FacesContext c, UIComponent uiComp, Object val){
  logger.log(INFO, "validatePostDate called with {0}", val);
  Date pstDt = (Date)val;
  FiscalPeriodYearRec fisPer = getFiscPerYr(pstDt);
  logger.log(INFO, "Validate check peryear year {0} per {1}", new Object[]{fisPer.getYear(),fisPer.getPeriod()});
  logger.log(INFO, "per as string {0}", String.valueOf(fisPer.getPeriod()));
  String perStr =  fisPer.getPeriod() < 10?"0"+String.valueOf(fisPer.getPeriod()):String.valueOf(fisPer.getPeriod());
  
  int yrPer = Integer.parseInt(String.valueOf(fisPer.getYear())+perStr);
  logger.log(INFO, "yrPer {0}", yrPer);
   logger.log(INFO, "docComp {0}", jnl.getCompany());  
   docComp = jnl.getCompany();
   if(jnl.getCompany().getCompanyPostingPeriods() == null){
    CompanyBasicRec comp = this.sysBuff.getCompAvailPostPeriod(jnl.getCompany());
    logger.log(INFO, "comp avail per after call to sys buff {0}", comp.getCompanyPostingPeriods());
   }
  logger.log(INFO, "openPeriods {0}", docComp.getCompanyPostingPeriods());  
  
  List<CompPostPerRec> openPeriods = this.docComp.getCompanyPostingPeriods();
  logger.log(INFO, "openPeriods {0}", openPeriods);
  for(CompPostPerRec openPer: openPeriods){
   // only gl needs to be open
   logger.log(INFO, "openPer led {0}", openPer.getLedger().getCode());
   if (openPer.getLedger().getCode().equals("GL")){
    logger.log(INFO, "yrPer {0} start {1} end {2}", new Object[]{yrPer,openPer.getStartLong(),openPer.getEndLong()});
    if(yrPer >= openPer.getStartLong() && yrPer <= openPer.getEndLong()){
     
     jnl.setFisYear(fisPer.getYear());
     jnl.setFisPeriod(fisPer.getPeriod());
     logger.log(INFO, "found valid period");
     ((UIInput)uiComp).setValid(true);
     PrimeFaces pf = PrimeFaces.current();
     List<String> toUpdate = new ArrayList<>();
     toUpdate.add("period");
     toUpdate.add("fisYr");
     pf.ajax().update("messages");
     pf.ajax().update(toUpdate);
     return;
    }
   }
  }
  // if we get here then not valid
  logger.log(INFO, "Period not valid");
  PrimeFaces pf = PrimeFaces.current();
  MessageUtil.addErrorMessage("fiDocPerNotOpen", "validationText");
  
  jnl.setPostingDate(null);
  pf.ajax().update("messages");
  pf.ajax().update("postDate");
  ((UIInput)uiComp).setValid(false);
 }
}
