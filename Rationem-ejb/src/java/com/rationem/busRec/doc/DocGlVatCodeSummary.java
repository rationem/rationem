/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;

import com.rationem.busRec.config.common.LineTypeRuleRec;
import com.rationem.busRec.config.company.PostTypeRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.salesTax.vat.VatCodeCompanyRec;
import com.rationem.busRec.salesTax.vat.VatCodeRec;
import com.rationem.entity.config.common.LineTypeRule;
import com.rationem.entity.config.company.PostType;
import com.rationem.entity.fi.company.Fund;
import com.rationem.entity.fi.glAccount.FiGlAccountComp;
import com.rationem.entity.salesTax.vat.VatCode;

/**
 * 
 * @author Chris
 */
public class DocGlVatCodeSummary {
 
 private FiGlAccountComp glAccount;
 private FundRec fund;
 private VatCodeCompanyRec vatCode;
 private LineTypeRuleRec lineType;
 private PostTypeRec postType;
 private double goods;
 private double vatAmount;

 public DocGlVatCodeSummary() {
 }

 public FundRec getFund() {
  return fund;
 }

 public void setFund(FundRec fund) {
  this.fund = fund;
 }

 public FiGlAccountComp getGlAccount() {
  return glAccount;
 }

 public void setGlAccount(FiGlAccountComp glAccount) {
  this.glAccount = glAccount;
 }

 public LineTypeRuleRec getLineTypeRec() {
  return lineType;
 }

 public void setLineType(LineTypeRuleRec lineType) {
  this.lineType = lineType;
 }

 public PostTypeRec getPostType() {
  return postType;
 }

 public void setPostType(PostTypeRec postType) {
  this.postType = postType;
 }

 public VatCodeCompanyRec getVatCode() {
  return vatCode;
 }

 public void setVatCode(VatCodeCompanyRec vatCode) {
  this.vatCode = vatCode;
 }

 public double getGoods() {
  return goods;
 }

 public void setGoods(double goods) {
  this.goods = goods;
 }

 public double getVatAmount() {
  return vatAmount;
 }

 public void setVatAmount(double vatAmount) {
  this.vatAmount = vatAmount;
 }
 
 
 
}
