/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

/**
 * Business record for Ledger Entity
 * @author Chris
 */
import com.rationem.busRec.config.arap.PaymentTypeRec;
//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.fi.company.PeriodControlRec;
import com.rationem.busRec.fi.glAccount.FiBsAccountRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
public class LedgerRec implements Serializable{
    
    private Long id;
    private String code;
    private String name;
    private String descr;
    private List<AccountTypeRec> accountTypes;
    private List<PeriodControlRec> periodControls;
    private List<PostTypeRec> postTypes;
    private List<PaymentTypeRec> paymentTypes;
    private List<FiBsAccountRec> reconcilAcnts;
    private boolean subLeder;
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private long changes;
    
    public LedgerRec() {
    }

    

    public UserRec getChangedBy() {
        return changedBy;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public long getChanges() {
        return changes;
    }

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDescr() {
        return descr;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    

    public void setChangedBy(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public void setChanges(long changes) {
        this.changes = changes;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

 public List<AccountTypeRec> getAccountTypes() {
  return accountTypes;
 }

 public void setAccountTypes(List<AccountTypeRec> accountTypes) {
  this.accountTypes = accountTypes;
 }

 public List<PaymentTypeRec> getPaymentTypes() {
  return paymentTypes;
 }

 public void setPaymentTypes(List<PaymentTypeRec> paymentTypes) {
  this.paymentTypes = paymentTypes;
 }

 public List<PeriodControlRec> getPeriodControls() {
  return periodControls;
 }

 public void setPeriodControls(List<PeriodControlRec> periodControls) {
  this.periodControls = periodControls;
 }

 public List<PostTypeRec> getPostTypes() {
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }

 public List<FiBsAccountRec> getReconcilAcnts() {
  return reconcilAcnts;
 }

 public void setReconcilAcnts(List<FiBsAccountRec> reconcilAcnts) {
  this.reconcilAcnts = reconcilAcnts;
 }

 public boolean isSubLeder() {
  return subLeder;
 }

 public void setSubLeder(boolean subLeder) {
  this.subLeder = subLeder;
 }

 
 @Override
 public int hashCode() {
  int hash = 7;
  hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
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
  final LedgerRec other = (LedgerRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

    

    


    
}
