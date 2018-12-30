/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class LineTypeRuleRec implements Serializable{
    private Long id;
    private String lineCode;
    private String langCode;
    private String description;
    private ModuleRec module;
    private ArrayList<LineTypeRuleTextRec> texts;
    private boolean sysUse;
    // TO-DO change to User record when users created
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private long changes;

    

    public LineTypeRuleRec() {
    }

    public LineTypeRuleRec(Long id, String langCode, String description, ModuleRec module, 
            UserRec createdBy, Date createdDate, UserRec changedBy, Date changedDate, long changes) {
        this.id = id;
        this.langCode = langCode;
        this.description = description;
        this.module = module;
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

    public ModuleRec getIasModule() {
        return module;
    }

    public void setIasModule(ModuleRec module) {
        this.module = module;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public ModuleRec getModule() {
        return module;
    }

    public void setModule(ModuleRec module) {
        this.module = module;
    }

    public ArrayList<LineTypeRuleTextRec> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<LineTypeRuleTextRec> texts) {
        this.texts = texts;
    }

 public boolean isSysUse() {
  return sysUse;
 }

 public void setSysUse(boolean sysUse) {
  this.sysUse = sysUse;
 }

    


    
    

}
