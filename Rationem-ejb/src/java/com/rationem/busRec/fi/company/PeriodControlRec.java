/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.fi.company;

import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class PeriodControlRec {
    private Long id;
    private LedgerRec ledger;
    private int periodFrom;
    private int periodTo;
    private int yearFrom;
    private int yearTo;
    private int revision;
    private Date createdDate;
    private Date changedDate;
    private UserRec createdBy;
    private UserRec changedBy;
    private CompanyBasicRec company;

    public PeriodControlRec() {
    }

    public PeriodControlRec(Long id, LedgerRec ledger, int periodFrom, int periodTo, int yearFrom,
            int yearTo, int revision, Date createdDate, Date changedDate, UserRec createdBy,
            UserRec changedBy, CompanyBasicRec company) {
        this.id = id;
        this.ledger = ledger;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.revision = revision;
        this.createdDate = createdDate;
        this.changedDate = changedDate;
        this.createdBy = createdBy;
        this.changedBy = changedBy;
        this.company = company;
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

    public CompanyBasicRec getCompany() {
        return company;
    }

    public void setCompany(CompanyBasicRec company) {
        this.company = company;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LedgerRec getLedger() {
        return ledger;
    }

    public void setLedger(LedgerRec ledger) {
        this.ledger = ledger;
    }

    public int getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(int periodFrom) {
        this.periodFrom = periodFrom;
    }

    public int getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(int periodTo) {
        this.periodTo = periodTo;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    
    
}
