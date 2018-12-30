/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class NumberRangeRec implements Serializable {

 private Long NumberControlId;
 private String shortDescr;
 private String longDescr;
 private List<AccountTypeRec> accountTypes;
 private ModuleRec module;
 private int fromNum;
 private int toNum;
 private int nextNum;
 private boolean autoNum = false;
 private NumberRangeRec nextNumRange;
 private NumberRangeRec priorNumRange;
 private NumberRangeTypeRec numberRangeForType;



 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;
 private long changes;

 

 public NumberRangeRec() {
 }

 public NumberRangeRec(Long validNumberRangeId, String shortDescr, String longDescr,
            List<AccountTypeRec> accountTypes, ModuleRec forModule, int fromNum, int toNum, 
            UserRec createdBy, Date createdDate, UserRec changedBy, Date changedDate, long changes) {
  this.NumberControlId = validNumberRangeId;
  this.shortDescr = shortDescr;
  this.longDescr = longDescr;
  this.accountTypes = accountTypes;
  this.module = forModule;
  this.fromNum = fromNum;
  this.toNum = toNum;
  this.createdBy = createdBy;
  this.createdDate = createdDate;
  this.changedBy = changedBy;
  this.changedDate = changedDate;
  this.changes = changes;
 }

 public List<AccountTypeRec> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountTypeRec> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
 }

 
 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public ModuleRec getModule() {
  return module;
 }

 public void setModule(ModuleRec module) {
  this.module = module;
 }

 public int getFromNum() {
  return fromNum;
 }

 public void setFromNum(int fromNum) {
  this.fromNum = fromNum;
 }

 public String getLongDescr() {
  return longDescr;
 }

 public void setLongDescr(String longDescr) {
  this.longDescr = longDescr;
 }

 public String getShortDescr() {
  return shortDescr;
 }

 public void setShortDescr(String shortDescr) {
  this.shortDescr = shortDescr;
 }

 public int getToNum() {
  return toNum;
 }

 public void setToNum(int toNum) {
  this.toNum = toNum;
 }


 public Long getNumberControlId() {
  return NumberControlId;
 }

 public void setNumberControlId(Long NumberControlId) {
  this.NumberControlId = NumberControlId;
 }

 public NumberRangeTypeRec getNumberRangeForType() {
  return numberRangeForType;
 }

 public void setNumberRangeForType(NumberRangeTypeRec numberRangeForType) {
  this.numberRangeForType = numberRangeForType;
 }

 
    
 public NumberRangeRec getPriorNumRange() {
  return priorNumRange;
 }

 public void setPriorNumRange(NumberRangeRec priorNumRange) {
  this.priorNumRange = priorNumRange;
 }

 

    public boolean isAutoNum() {
      if(!autoNum){
        autoNum = false;
      }
        return autoNum;
    }

    public void setAutoNum(boolean autoNum) {
        this.autoNum = autoNum;
    }

    public int getNextNum() {
        return nextNum;
    }

    public void setNextNum(int nextNum) {
        this.nextNum = nextNum;
    }

 public NumberRangeRec getNextNumRange() {
  return nextNumRange;
 }

 public void setNextNumRange(NumberRangeRec nextNumRange) {
  this.nextNumRange = nextNumRange;
 }

  
  
  

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final NumberRangeRec other = (NumberRangeRec) obj;
  if (this.NumberControlId != other.NumberControlId && (this.NumberControlId == null || !this.NumberControlId.equals(other.NumberControlId))) {
   return false;
  }
  return true;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 17 * hash + (this.NumberControlId != null ? this.NumberControlId.hashCode() : 0);
  hash = 17 * hash + (int) (this.changes ^ (this.changes >>> 32));
  return hash;
 }

    

    


    


}
