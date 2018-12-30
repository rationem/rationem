/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.ma.programme.ProgramAccountRec;
import com.rationem.busRec.ma.programme.ProgrammeRec;
import com.rationem.busRec.sales.SalesPartCompanyRec;
import com.rationem.busRec.sales.SalesPartRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.exception.BacException;
import java.util.List;

/**
 *
 * @author Chris
 */
public class SalesPartFiLineRec {
 private int lineNum;
 private String partCode;
 private SalesPartRec part;
 private SalesPartCompanyRec partComp;
 private String description;
 private FiGlAccountCompRec glaccount;
 private VatCodeCompanyRec vatCode;
 private String qtyString;
 private int qty;
 private double unitPrice;
 private UomRec uom;
 private double lineTotal;
 private FundRec fund;
 private CostCentreRec costCent;
 private CostAccountDirectRec costAct;
 private ProgrammeRec prog;
 private ProgramAccountRec progAcnt;
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

 public String getPartCode() {
  return partCode;
 }

 public void setPartCode(String partCode) {
  this.partCode = partCode;
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

 public VatCodeCompanyRec getVatCode() {
  if(vatCode == null){
   vatCode = new VatCodeCompanyRec();
  }
  return vatCode;
 }

 public void setVatCode(VatCodeCompanyRec vatCode) {
  this.vatCode = vatCode;
 }

 public int getQty() {
  return qty;
 }

 public void setQty(int qty) {
  this.qty = qty;
  qtyString = String.valueOf(qty);
 }

 public String getQtyString() {
  if(qtyString== null || qtyString.isEmpty() ){
   qtyString = String.valueOf(qty);
  }
  return qtyString;
 }

 public void setQtyString(String qtyString) throws BacException {
  this.qtyString = qtyString;
  try{
   qty = Integer.parseInt(qtyString);
  }catch(NumberFormatException ex){
   qty = 0;
  }
 }

 public UomRec getUom() {
  return uom;
 }

 public void setUom(UomRec uom) {
  this.uom = uom;
 }

 public int getLineNum() {
  return lineNum;
 }

 public void setLineNum(int lineNum) {
  this.lineNum = lineNum;
 }

 public double getLineTotal() {
  return lineTotal;
 }

 public void setLineTotal(double lineTotal) {
  this.lineTotal = lineTotal;
 }

 public double getUnitPrice() {
  return unitPrice;
 }

 public void setUnitPrice(double unitPrice) {
  this.unitPrice = unitPrice;
 }

 public List<DocLineBaseRec> getClearedLines() {
  return clearedLines;
 }

 public void setClearedLines(List<DocLineBaseRec> clearedLines) {
  this.clearedLines = clearedLines;
 }

 public CostAccountDirectRec getCostAct() {
  return costAct;
 }

 public void setCostAct(CostAccountDirectRec costAct) {
  this.costAct = costAct;
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

 public ProgramAccountRec getProgAcnt() {
  return progAcnt;
 }

 public void setProgAcnt(ProgramAccountRec progAcnt) {
  this.progAcnt = progAcnt;
 }

 @Override
 public int hashCode() {
  int hash = 5;
  hash = 89 * hash + this.lineNum;
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final SalesPartFiLineRec other = (SalesPartFiLineRec) obj;
  return this.lineNum == other.lineNum;
 }
 
 
 
 
}
