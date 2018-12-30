/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.tr;

import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class BnkPaymentRunRec {
 
 private long id;
 private String ref;
 private CompanyBasicRec comp;
 private Date runDate;
 private List<DocBankLineBaseRec> bankLines;
 private Date createDate;
 private UserRec createBy;
 private Date updateDate;
 private UserRec updateOn;
 private long changes;

 public BnkPaymentRunRec() {
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public String getRef() {
  return ref;
 }

 public void setRef(String ref) {
  this.ref = ref;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public Date getRunDate() {
  return runDate;
 }

 public void setRunDate(Date runDate) {
  this.runDate = runDate;
 }

 public List<DocBankLineBaseRec> getBankLines() {
  return bankLines;
 }

 public void setBankLines(List<DocBankLineBaseRec> bankLines) {
  this.bankLines = bankLines;
 }

 public Date getCreateDate() {
  return createDate;
 }

 public void setCreateDate(Date createDate) {
  this.createDate = createDate;
 }

 public UserRec getCreateBy() {
  return createBy;
 }

 public void setCreateBy(UserRec createBy) {
  this.createBy = createBy;
 }

 public Date getUpdateDate() {
  return updateDate;
 }

 public void setUpdateDate(Date updateDate) {
  this.updateDate = updateDate;
 }

 public UserRec getUpdateOn() {
  return updateOn;
 }

 public void setUpdateOn(UserRec updateOn) {
  this.updateOn = updateOn;
 }

 public long getChanges() {
  return changes;
 }

 public void setChanges(long changes) {
  this.changes = changes;
 }
 
 
 
 
}
