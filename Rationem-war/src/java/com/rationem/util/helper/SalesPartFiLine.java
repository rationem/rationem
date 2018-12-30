/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;


import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import java.math.BigDecimal;
import java.util.List;

/**
 * used by GUI for invoice line combining part and AR line
 * @author Chris
 */
public class SalesPartFiLine {
 
 private SalesPartRec part;
 private SalesPartCompanyRec partComp;
 private String description;
 private FiGlAccountCompRec glaccount;
 private VatCodeRec vatCode;
 private int qty;
 private BigDecimal unitPrice;
 private UomRec uom;
 private BigDecimal lineTotal;
 private FundRec fund;
 private CostCentreRec costCent;
 private ProgrammeRec prog;
 private List<DocLineBaseRec> clearedLines;

 public FiGlAccountCompRec getGlaccount() {
  return glaccount;
 }

 public void setGlaccount(FiGlAccountCompRec glaccount) {
  this.glaccount = glaccount;
 }

 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public SalesPartRec getPart() {
  if(part == null){
   part = new SalesPartRec();
  }
  return part;
 }

 public void setPart(SalesPartRec part) {
  this.part = part;
 }

 public SalesPartCompanyRec getPartComp() {
  if(partComp == null){
   partComp = new SalesPartCompanyRec();
  }
  return partComp;
 }

 public void setPartComp(SalesPartCompanyRec partComp) {
  this.partComp = partComp;
  
 }

 public VatCodeRec getVatCode() {
  if(vatCode == null){
   vatCode = new VatCodeRec();
  }
  return vatCode;
 }

 public void setVatCode(VatCodeRec vatCode) {
  this.vatCode = vatCode;
 }

 public int getQty() {
  return qty;
 }

 public void setQty(int qty) {
  this.qty = qty;
 }

 public UomRec getUom() {
  return uom;
 }

 public void setUom(UomRec uom) {
  this.uom = uom;
 }

 public BigDecimal getLineTotal() {
  return lineTotal;
 }

 public void setLineTotal(BigDecimal lineTotal) {
  this.lineTotal = lineTotal;
 }

 public BigDecimal getUnitPrice() {
  return unitPrice;
 }

 public void setUnitPrice(BigDecimal unitPrice) {
  this.unitPrice = unitPrice;
 }

 public List<DocLineBaseRec> getClearedLines() {
  return clearedLines;
 }

 public void setClearedLines(List<DocLineBaseRec> clearedLines) {
  this.clearedLines = clearedLines;
 }

 public CostCentreRec getCostCent() {
  if(costCent == null){
   costCent = new CostCentreRec();
  }
  return costCent;
 }

 public void setCostCent(CostCentreRec costCent) {
  this.costCent = costCent;
 }

 public ProgrammeRec getProg() {
  if(prog == null){
   prog = new ProgrammeRec();
  }
  return prog;
 }

 public void setProg(ProgrammeRec prog) {
  this.prog = prog;
 }
 
 
 
}
