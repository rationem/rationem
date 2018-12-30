/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.config.company;

import com.rationem.busRec.config.common.ModuleRec;
import com.rationem.busRec.config.common.NumberRangeRec;
import com.rationem.busRec.config.common.ProcessCodeRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author Chris
 */
public class AccountTypeRec implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(AccountTypeRec.class.getName());
    
    private Long id;
    private String name;
    private String description ;
    private boolean systemPost;
    private boolean controlAccount;
    private boolean debit;
    private LedgerRec subLedger ;
    private ModuleRec module;
    private NumberRangeRec numberRange;
    private boolean profitAndLossAccount;
    private ProcessCodeRec processCode;
    private boolean retainedEarn;
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private long changes;

    public AccountTypeRec() {
    }

    public AccountTypeRec(Long id, String name, String description, boolean systemPost, 
            boolean controlAccount, LedgerRec subLedger, boolean profitAndLossAccount, 
            UserRec createdBy, Date createdDate, UserRec changedBy, Date changedDate, long changes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.systemPost = systemPost;
        this.controlAccount = controlAccount;
        this.subLedger = subLedger;
        this.profitAndLossAccount = profitAndLossAccount;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.changes = changes;
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

    public long getChanges() {
        return changes;
    }

    public void setChanges(long changes) {
        this.changes = changes;
    }

    public boolean isControlAccount() {
        return controlAccount;
    }

    public void setControlAccount(boolean controlAccount) {
        this.controlAccount = controlAccount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 public boolean isDebit() {
  return debit;
 }

 public void setDebit(boolean debit) {
  this.debit = debit;
 }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

 public ModuleRec getModule() {
  return module;
 }

 public void setModule(ModuleRec module) {
  this.module = module;
 }

 public NumberRangeRec getNumberRange() {
  return numberRange;
 }

 public void setNumberRange(NumberRangeRec numberRange) {
  this.numberRange = numberRange;
 }

    public String getName() {
        
        if(name == null){
            name = new String();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 public ProcessCodeRec getProcessCode() {
  return processCode;
 }

 public void setProcessCode(ProcessCodeRec processCode) {
  this.processCode = processCode;
 }

 
    public boolean isProfitAndLossAccount() {
        return profitAndLossAccount;
    }

    public void setProfitAndLossAccount(boolean profitAndLossAccount) {
        this.profitAndLossAccount = profitAndLossAccount;
    }

 public boolean isRetainedEarn() {
  return retainedEarn;
 }

 public void setRetainedEarn(boolean retainedEarn) {
  this.retainedEarn = retainedEarn;
 }

    
    public LedgerRec getSubLedger() {
        return subLedger;
    }

    public void setSubLedger(LedgerRec subLedger) {
        this.subLedger = subLedger;
    }

    public boolean isSystemPost() {
        return systemPost;
    }

    public void setSystemPost(boolean systemPost) {
        this.systemPost = systemPost;
    }

    
}


