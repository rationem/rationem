/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * Module Business object. DB object is converted into the business object releasing the DB connection
 * @author Chris
 */
public class ModuleRec implements Serializable {


    private Long id;
    private String name;
    private String description;
    private String moduleCode;
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private int revision;

    private static final Logger logger =
            Logger.getLogger(ModuleRec.class.getName());


    public ModuleRec() {
    
    }

    public ModuleRec(Long id,  String name, String description, String moduleCode, Date createdDate, Date changedDate, int changes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.moduleCode = moduleCode;
        this.createdDate = createdDate;
        this.changedDate = changedDate;
        this.revision = changes;
    }

    public ModuleRec( String name, String description, String moduleCode) {
        
        this.name = name;
        this.description = description;
        this.moduleCode = moduleCode;
    }



    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public int getChanges() {
        return revision;
    }

    public void setChanges(int changes) {
        this.revision = changes;
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

    public void setDescription(String descr) {
        description = descr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {

        this.moduleCode = moduleCode;
        
    }

    public String getName() {
        
        return name;
    }

    public void setName(String n) {
        
        name = n;
    }

    public UserRec getChangedBy() {
        return changedBy;
    }

    public void setChangedById(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public UserRec getCreatedBy() {
     return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }





}
