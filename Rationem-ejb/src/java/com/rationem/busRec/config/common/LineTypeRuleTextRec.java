/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import java.util.Date;

/**
 *
 * @author Chris
 */
public class LineTypeRuleTextRec {

    private Long id;
    private LineTypeRuleRec ruleId;
    private String langCode;
    private String Code;
    private String description;
    // TO-DO: change to user object
    private boolean defaultText;
    private int createdBy;
    private Date createDate;
    // TO-DO: change to user object
    private int changedBy;
    private Date changedDate;
    private int revision;

    public LineTypeRuleTextRec() {
    }

    public LineTypeRuleTextRec(Long id, LineTypeRuleRec ruleId, String langCode, String Code, String description, int createdBy, Date createDate, int changedBy, Date changedDate, int revision) {
        this.id = id;
        this.ruleId = ruleId;
        this.langCode = langCode;
        this.Code = Code;
        this.description = description;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.revision = revision;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefaultText() {
        return defaultText;
    }

    public void setDefaultText(boolean defaultText) {
        this.defaultText = defaultText;
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

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public LineTypeRuleRec getRuleId() {
        return ruleId;
    }

    public void setRuleId(LineTypeRuleRec ruleId) {
        this.ruleId = ruleId;
    }


}
