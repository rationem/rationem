/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.tr;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocFiRec;
import com.rationem.busRec.doc.DocLineApRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.ChequeTemplateRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import com.rationem.ejbBean.dataManager.DocumentDM;
import com.rationem.ejbBean.dataManager.TreasuryDM;
import com.rationem.helper.comparitor.ChequeTemplateByRef;
import com.rationem.util.GenUtilServer;
import com.rationem.util.NameLocalisationMap;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell; 

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import javax.ejb.EJB;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PaymentMediumManager {
 private static final Logger LOGGER = Logger.getLogger(PaymentMediumManager.class.getName());
 private static final int ALIGN_RIGHT = Element.ALIGN_RIGHT;
 private static final int ALIGN_LEFT = Element.ALIGN_LEFT;
 private static final int ALIGN_MIDDLE = Element.ALIGN_MIDDLE;
 
 @EJB
 private TreasuryDM treasuryDm;
 
 @EJB
 private ConfigurationDM payTypeDm;
 
 @EJB
 private DocumentDM docDm;
 
 
 
 private PdfPTable addChqRemHeaderRowPdf(PdfPTable table, List<NameLocalisationMap> fieldNames){
  LOGGER.log(INFO, "addInvHeaderRowPdf called with {0}",table);
  String title ;
  
  title = getFieldNameLocal("CHQ_PD_DATE",fieldNames);
  table.addCell(setHeaderCell(title,false));
  title = getFieldNameLocal("CHQ_PD_INV_REF",fieldNames);
  table.addCell(setHeaderCell(title,false));
  title = getFieldNameLocal("CHQ_PD_INV_DESC",fieldNames);
  table.addCell(setHeaderCell(title,false));
  title = getFieldNameLocal("CHQ_PD_INV_AMNT",fieldNames);
  table.addCell(setHeaderCell( title,true));
  //table.addCell(setHeaderCell("Vat Cd",true));
  
  table.completeRow();
  LOGGER.log(INFO, "table.size()", table.getRows());
  return table;
 }
 
 public PdfPCell chqRemLineCell(String val, int align, int colSpan, int rowSpan, boolean rowEnd){
  Paragraph para = new Paragraph(val);
  para.getFont().setSize(10);
  para.setAlignment(align);
  PdfPCell cell = new PdfPCell();
  cell.addElement(para);
  cell.setBorder(Rectangle.NO_BORDER);
  cell.setBorderWidth(1f);
  cell.setBorderWidthBottom(1f);
  cell.setBorderColor(new Color(0,0,255));
  cell.setBorderWidthLeft(1f);
  //cell.setBorderWidthTop(1f);
  if(rowEnd){
   cell.setBorderWidthRight(1f);
  }
  cell.setUseBorderPadding(true);
  if(colSpan > 0){
   cell.setColspan(colSpan);
  }
  if(rowSpan > 0){
   cell.setRowspan(rowSpan);
  }
  return cell;
  
 }
 
 private PdfPTable addChqRemLinesPdf(PdfPTable table, DocLineApRec payLn){
  LOGGER.log(INFO, "addChqRemLinesPdf called with pdftable {0} and payment line id {1}", 
    new Object[]{table,payLn.getId()});
  
  List<DocLineBaseRec> paidLines = payLn.getClearingLineForLines();
  LOGGER.log(INFO,"paidLines {0}",paidLines);
  if(paidLines == null || paidLines.isEmpty()){
   payLn = docDm.getApClearedLinesForLine(payLn);
   paidLines = payLn.getClearingLineForLines();
  }
  LOGGER.log(INFO,"paidLines {0}",paidLines);
  if(payLn.getDocFi() == null){
   payLn = (DocLineApRec)docDm.getDocFiForLine(payLn);
   
  }
  LOGGER.log(INFO, "payLn doc {0}", payLn.getDocFi());
  DocFiRec doc = payLn.getDocFi();
  Date docDate = doc.getDocumentDate();
  String date = GenUtilServer.formatDateStringLocalised(docDate, payLn.getComp().getLocale());
  String amntStr = GenUtilServer.numberDp(payLn.getDocAmount(), payLn.getComp().getCurrency().getMinorUnit());
  table.addCell(chqRemLineCell(date, ALIGN_LEFT, 0, 0, false));
  table.addCell(chqRemLineCell(doc.getPartnerRef(), ALIGN_LEFT, 0, 0, false));
  table.addCell(chqRemLineCell(doc.getDocHdrText(), ALIGN_LEFT, 0, 0, false));
  table.addCell(chqRemLineCell(amntStr, ALIGN_RIGHT, 0, 0, true));
  table.completeRow();
  
  return table;
 }
 
 private String getFieldNameLocal(String name,List<NameLocalisationMap> fieldNames){
  
  for(NameLocalisationMap nameMap:fieldNames){
   LOGGER.log(INFO, "Name {0} curr name {1}", new Object[]{name,nameMap.getName()});
   if(StringUtils.equals(name, nameMap.getName())){
    LOGGER.log(INFO, "nameLocal {0}", nameMap.getNameLocal());
    return nameMap.getNameLocal();
   }
  }
  return null;
 }
 private PdfPCell setHeaderCell(String hdrText, boolean last ){
  Font fnt = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 255));
  Color cellBkCol = new Color(230,230,230);
  Paragraph para = new Paragraph(hdrText);
  para.setFont(fnt);
  
  //PdfPCell cell = new PdfPCell(new Paragraph(hdrText));
  PdfPCell cell = new PdfPCell();
  
  cell.addElement(para);
  cell.setBackgroundColor(cellBkCol);
  cell.setBorder(Rectangle.NO_BORDER);
  cell.setBorderWidth(1f);
  cell.setBorderWidthBottom(3f);
  cell.setBorderColor(new Color(0,0,255));
  cell.setBorderWidthLeft(1f);
  cell.setBorderWidthTop(1f);
  if(last){
   cell.setBorderWidthRight(1f);
  }
  cell.setUseBorderPadding(true);
  
  
  return cell;
 }
 
 public InputStream getChequePdfTemplate(DocFiRec paymentDoc, PaymentTypeRec payType){
  LOGGER.log(INFO, "Called getChequePdf");
  if(payType.isHasCheqTemplate()){
   LOGGER.log(INFO, "Paytype has cheque template with pdf data {0}", payType.getChqTemplate());
  }else{
   BankAccountCompanyRec bnkAcnt = payType.getPayTypeForBankAccount();
   LOGGER.log(INFO, "Payment type bank account {0}", bnkAcnt);
   LOGGER.log(INFO, "Bank has cheque template {0} template data {1}", 
     new Object[]{bnkAcnt.isHasChqTemplate(),bnkAcnt.getChequeTemplate()});
   
  }
  byte[] fileData = null;
  
  InputStream pdfReturn = new ByteArrayInputStream(fileData);
  
  
 return pdfReturn;
  
 }
 
 public boolean isChqTemplateInUse(ChequeTemplateRec templ){
  LOGGER.log(INFO, "isChqTemplateInUse called with templ id {0}", templ.getId());
  
  boolean rc;
  
  rc = treasuryDm.chequeTemplateUsedByBnk(templ);
  if(!rc){
   rc = payTypeDm.isChequeTemplUsedByPayType(templ);
  }
  
  return rc;
 }
 
 public ChequeTemplateRec getChqTemplOrigFileData(ChequeTemplateRec templRec){
  templRec = treasuryDm.getChqTemplOrigFileData(templRec);
  return templRec;
 }
 
 public ChequeTemplateRec getChqTemplPdfFileData(ChequeTemplateRec templRec){
  templRec = treasuryDm.getChqTemplPdfFileData(templRec);
  return templRec;
 }
 
 public List<ChequeTemplateRec> getChqTemplByRef(String ref){
  
  if(StringUtils.isBlank(ref)){
   return getChqTemplatesAll();
  }else{
   ref = StringUtils.remove(ref, '%');
   ref = StringUtils.appendIfMissing(ref, "%");
   return treasuryDm.getChequeTemplByRef(ref);
  }
 }
 public List<ChequeTemplateRec> getChqTemplatesAll(){
  LOGGER.log(INFO, "getChqTemplatesAll");
  List<ChequeTemplateRec> ret = treasuryDm.getChequeTemplatesAll();
  Collections.sort(ret, new ChequeTemplateByRef());
  LOGGER.log(INFO, "payment meduim Mgr to return {0}", ret);
  return ret;
 }
 
 
 public ByteArrayOutputStream getApChequePdf(DocLineApRec docLineAp, DocBankLineChqRec chqLine,
   ChequeTemplateRec chqTempl,  List<NameLocalisationMap> tabTitles, Map<String,String> numberTexts)throws IOException, DocumentException{
 LOGGER.log(INFO,"Called getApChequePdf with docLine {0} chqTempl {1}",new Object[]{docLineAp,chqTempl}); 
  byte[] templ = chqTempl.getPdfData();
  CompanyBasicRec comp = docLineAp.getComp();
  if(chqTempl.isPrintChqNumLine()){
   PaymentTypeRec pt = docLineAp.getPayType();
  }
  
  LOGGER.log(INFO, "Chq num {0}", chqLine.getBnkRef());
  LOGGER.log(INFO, "Chq amount {0}", chqLine.getAmount());
  LOGGER.log(INFO, "Chq account num {0}", chqLine.getUnClearedBankAc().getAccountNumber());
  LOGGER.log(INFO, "Chq branch code {0}", chqLine.getUnClearedBankAc().getAccountForBranch().getSortCode());
  
  if(chqLine.getAmount() == 0){
   chqLine.setAmount(docLineAp.getDocAmount());
   chqLine.setAmount(9231114.90); //9,231,114.90
   LOGGER.log(INFO, "Chq amount {0}", chqLine.getAmount());
  }
  PartnerBaseRec ptnr =  docLineAp.getApAccount().getApAccountFor();
  AddressRec acntAddr = docLineAp.getApAccount().getAccountAddress();
  DocFiRec doc = docLineAp.getDocFi();
  if(doc == null){
   docLineAp = (DocLineApRec)docDm.getDocFiForLine(docLineAp);
   doc = docLineAp.getDocFi();
  }
   
  
  
  //Document stampDoc = new Document(PageSize.A4, 50, 50, 50, 50);
  ByteArrayOutputStream stampBaos = new ByteArrayOutputStream();
  ByteArrayInputStream bis = new ByteArrayInputStream(templ);
  
  
  try{
   Document stampDoc = new Document(PageSize.A4, 50, 50, 50, 50);
   PdfReader pdfReader = new PdfReader(bis);
   PdfStamper pdfStamper = new PdfStamper(pdfReader,stampBaos);
   PdfWriter stampWriter = pdfStamper.getWriter();
   stampWriter.setMargins(50, 50, 50, 50);
   
   
   Rectangle page = stampWriter.getPageSize();
   LOGGER.log(INFO, "pdfStamper is {0}", pdfStamper);
   
   pdfStamper.getAcroFields().setField("compName", comp.getName());
   if(comp.getAddress() != null){
   pdfStamper.getAcroFields().setField("compAddr1", comp.getAddress().getStreet());
   pdfStamper.getAcroFields().setField("compAddr2", comp.getAddress().getStreet2());
   pdfStamper.getAcroFields().setField("compAddr3", comp.getAddress().getTown());
   pdfStamper.getAcroFields().setField("compAddr4", comp.getAddress().getCountyName());
   
   pdfStamper.getAcroFields().setField("compPostCd", comp.getAddress().getPostCode());
  }
   // payee details
   pdfStamper.getAcroFields().setField("payeeName", ptnr.getName());
   if(acntAddr != null){
   pdfStamper.getAcroFields().setField("payeeAddr1", acntAddr.getStreet());
   pdfStamper.getAcroFields().setField("payeeAddr2", acntAddr.getStreet2());
   pdfStamper.getAcroFields().setField("payeeAddr3", acntAddr.getTown());
   pdfStamper.getAcroFields().setField("payeeAddr4", acntAddr.getCountyName());
   pdfStamper.getAcroFields().setField("payeePostCd", acntAddr.getPostCode());
   String chqDateStr = GenUtilServer.formatDateStringLocalised(doc.getDocumentDate(), comp.getLocale());
   pdfStamper.getAcroFields().setField("chqDate", chqDateStr);
   pdfStamper.getAcroFields().setField("chqPayee", ptnr.getName());
   String payAmnt = GenUtilServer.formatNumberLocDp(chqLine.getAmount(), docLineAp.getComp().getLocale());
   pdfStamper.getAcroFields().setField("chqAmntDigit", payAmnt);
   List<String> payAmntAlpha = GenUtilServer.getChequeAmountWords(comp.getCurrency(), chqLine.getAmount(), numberTexts);
   int size = payAmntAlpha.size();
   for(int i=0;i<size;i++){
    switch (i){
     case 0:
      pdfStamper.getAcroFields().setField("chqAmntAlpha1", payAmntAlpha.get(0));
      break;
     case 1:
      pdfStamper.getAcroFields().setField("chqAmntAlpha2", payAmntAlpha.get(1));
      break;
     case 2:
      pdfStamper.getAcroFields().setField("chqAmntAlpha3", payAmntAlpha.get(2));
      break;
    }
   
   
   
   }
   if(chqTempl.isPrintChqNumLine()){
    pdfStamper.getAcroFields().setField("chqNum", chqLine.getBnkRef() );
    pdfStamper.getAcroFields().setField("chqSort", String.valueOf(chqLine.getUnClearedBankAc().getAccountForBranch().getSortCode()));
    pdfStamper.getAcroFields().setField("chqAcntNum", chqLine.getUnClearedBankAc().getAccountNumber());
    
   }
    
   
   }
   
   float[] yChqLinesAll = pdfStamper.getAcroFields().getFieldPositions("chqInvTab");
   float yChqRemLines = yChqLinesAll[2];
   
   PdfPTable table = new PdfPTable(4);
   int[] widths;
   widths = new int[]{3,4,10,5};
   table.setWidths(widths);
   table.setWidthPercentage(100);
   table.setHeaderRows(1);
   table.setTotalWidth(page.getWidth() - 120);
   table = addChqRemHeaderRowPdf(table, tabTitles);
   table = addChqRemLinesPdf(table, docLineAp);
   LOGGER.log(INFO, "width {0} border righ {1} total width {2}", new Object[]{
    page.getWidth(), page.getBorderWidthRight(),table.getTotalWidth()
   });
   
   LOGGER.log(INFO, "table rows after addInvLinesPdf {0}", table.getRows());
   
   float xPos = (page.getWidth() / 2) - (table.getTotalWidth() / 2);
   LOGGER.log(INFO, "xPos {0} page.getWidth() /2 {1} table.getTotalWidth() / 2 {2} page.getBorderWidthLeft() {3}", new Object[]{
   xPos, page.getWidth() / 2,table.getTotalWidth() / 2, page.getBorderWidthLeft()});
   PdfContentByte cont = pdfStamper.getUnderContent(1);
      
   float currYpos = table.writeSelectedRows(0, -1, xPos, yChqRemLines, cont);
   LOGGER.log(INFO, "currYpos {0}", currYpos);
   pdfStamper.setFormFlattening(true);
   pdfStamper.setFreeTextFlattening(true);
   pdfStamper.close();
   stampDoc.close();
   
  }catch(IOException ex){
    throw new IOException("Could not find invoice template");
  }
  return stampBaos;
 }
 
 public boolean deleteChequeTemplate(ChequeTemplateRec tmpl, String view){
  LOGGER.log(INFO, "deleteChequeTemplate called with templ id {0}", tmpl.getId());
  boolean rc = this.treasuryDm.deleteChequeTemplate(tmpl, view);
  
  return rc;
 }
 
 public ChequeTemplateRec updateChequeTemplate(ChequeTemplateRec tmpl, 
   BankAccountCompanyRec bnkAct, PaymentTypeRec ptTy, String view){
  LOGGER.log(INFO, "updateChequeTemplate called with {0}", tmpl);
  LOGGER.log(INFO, "template created by id  {0}", tmpl.getCreatedBy().getId());
  UserRec chUser = tmpl.getChangedBy();
  if (chUser == null){
   chUser = tmpl.getCreatedBy();
  }
  if (bnkAct != null){
   bnkAct.setChequeTemplate(tmpl);
   bnkAct.setUpdatedBy(chUser);
   bnkAct.setUpdatedOn(new Date());
   treasuryDm.updateBankAccountCompany(bnkAct, view);
  }
  if(ptTy != null){
   ptTy.setChqTemplate(tmpl);
   ptTy.setHasCheqTemplate(true);
   ptTy.setChangedBy(chUser);
   ptTy.setChangedOn(new Date());
   payTypeDm.updatePaymentType(ptTy, view);
  }
    
  tmpl = treasuryDm.updateChequeTemplate(tmpl, view);
  
  
  
  
  return tmpl;
 }
 
}
