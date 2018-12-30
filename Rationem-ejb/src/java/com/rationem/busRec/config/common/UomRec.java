/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chris
 * Unit of measures used by all modules
 */
public class UomRec implements Serializable {
    private Long id;
    private String uomCode;
    private String name;
    private String description;
    private String processCode;
    private Date createDate;
    private Date changeDate;
    private UserRec createdBy;
    private UserRec changedBy;
    private int revision;

    public UomRec() {
    }

    public UomRec(Long id, String uomCode,  String name, String description, String processCode,
            Date createDate, UserRec createdBy) {
        this.id = id;
        this.uomCode = uomCode;
        this.name = name;
        this.description = description;
        this.processCode = processCode;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

    public UomRec(Long id, String uomCode,  String name, String description, String processCode, Date createDate, Date changeDate, UserRec createdBy, UserRec changedBy, int revision) {
        this.id = id;
        this.uomCode = uomCode;
        this.name = name;
        this.description = description;
        this.processCode = processCode;
        this.createDate = createDate;
        this.changeDate = changeDate;
        this.createdBy = createdBy;
        this.changedBy = changedBy;
        this.revision = revision;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public UserRec getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    

}
