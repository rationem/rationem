/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;



import static java.util.logging.Level.INFO;

/**
 * Rule to determine the accounting period used by a chart of accounts
 * @author Chris
 */
public class FisPeriodRuleRec implements Serializable {
    private static final Logger logger =
            Logger.getLogger("com.rationem.busRec.config.company.FisPeriodRuleRec");

    private Long id;
    private String periodRule;
    private String periodDescr;
    private int numPeriods;
    private int numAuditPeriods;
    private boolean calendarMonthBasis;
    private boolean calendarNatural;
    private int calOffset;
    private int calBasisOption;
    private boolean fixedDayOfMonthBasis;
    private boolean annualDateScheduleBasis;
    private boolean periodLenBasis;
    private UomRec periodUoM;

    private boolean fixedlen;
    private int periodLen;
    private Date createDate;
    private Date changeDate;
    private int revision;
    
    private UserRec createBy;
    private UserRec changeBy;
    private CalendarRuleBaseRec calendarRule;
    

    public FisPeriodRuleRec() {
    }

    public FisPeriodRuleRec(Long id, String periodRule, int numPeriods, int numAuditPeriods, 
            boolean calendarMonthBasis, int calOffset, boolean fixedDayOfMonthBasis,
            boolean annualDateScheduleBasis, UomRec uom, Date createDate,
            Date changeDate, int revision, UserRec createBy, UserRec changeBy) {
        this.id = id;
        this.periodRule = periodRule;
        this.numPeriods = numPeriods;
        this.numAuditPeriods = numAuditPeriods;
        this.calendarMonthBasis = calendarMonthBasis;
        this.calOffset = calOffset;
        this.fixedDayOfMonthBasis = fixedDayOfMonthBasis;
        this.annualDateScheduleBasis = annualDateScheduleBasis;
        this.periodUoM = uom;
        this.createDate = createDate;
        this.changeDate = changeDate;
        this.revision = revision;
        this.createBy = createBy;
        this.changeBy = changeBy;
    }

    public String getPeriodDescr() {
        return periodDescr;
    }

    public void setPeriodDescr(String periodDescr) {
        this.periodDescr = periodDescr;
    }

    public int getCalBasisOption() {
        if(calBasisOption == 0){
            calBasisOption = 1;
        }
        return calBasisOption;
    }

    public void setCalBasisOption(int calBasisOption) {
        logger.log(INFO, "setCalBasisOption value: {0}", calBasisOption);
        this.calBasisOption = calBasisOption;
    }



    public boolean isAnnualDateScheduleBasis() {
        return annualDateScheduleBasis;
    }

    public void setAnnualDateScheduleBasis(boolean annualDateScheduleBasis) {
        this.annualDateScheduleBasis = annualDateScheduleBasis;
    }

    public boolean isCalendarMonthBasis() {
        return calendarMonthBasis;
    }

    public void setCalendarMonthBasis(boolean calendarMonthBasis) {
        this.calendarMonthBasis = calendarMonthBasis;
    }

 public boolean isCalendarNatural() {
  return calendarNatural;
 }

 public void setCalendarNatural(boolean calendarNatural) {
  this.calendarNatural = calendarNatural;
 }

    
    public boolean isPeriodLenBasis() {
        return periodLenBasis;
    }

    public void setPeriodLenBasis(boolean periodLenBasis) {
        this.periodLenBasis = periodLenBasis;
    }

    

    public int getCalOffset() {
        if(!calendarMonthBasis){
           calOffset = 0;
        }
        return calOffset;
    }

    public void setCalOffset(int calOffset) {
        this.calOffset = calOffset;
    }

    public boolean isFixedlen() {
        return fixedlen;
    }

    public void setFixedlen(boolean fixedlen) {
        this.fixedlen = fixedlen;
    }

 
    public UomRec getPeriodUoM() {
        return periodUoM;
    }

    public void setPeriodUoM(UomRec periodUoM) {
        this.periodUoM = periodUoM;
    }

    public int getPeriodLen() {
        return periodLen;
    }

    public void setPeriodLen(int periodLen) {
        this.periodLen = periodLen;
    }



    public UserRec getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(UserRec changeBy) {
        this.changeBy = changeBy;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public UserRec getCreateBy() {
        return createBy;
    }

    public void setCreateBy(UserRec createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isFixedDayOfMonthBasis() {
        return fixedDayOfMonthBasis;
    }

    public void setFixedDayOfMonthBasis(boolean fixedDayOfMonthBasis) {
        this.fixedDayOfMonthBasis = fixedDayOfMonthBasis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumAuditPeriods() {
        return numAuditPeriods;
    }

    public void setNumAuditPeriods(int numAuditPeriods) {
        this.numAuditPeriods = numAuditPeriods;
    }

    public int getNumPeriods() {
        return numPeriods;
    }

    public void setNumPeriods(int numPeriods) {
        this.numPeriods = numPeriods;
    }

    public String getPeriodRule() {
        return periodRule;
    }

    public void setPeriodRule(String periodRule) {
        this.periodRule = periodRule;
    }

    public CalendarRuleBaseRec getCalendarRule() {
        return calendarRule;
    }

    public void setCalendarRule(CalendarRuleBaseRec calendarRule) {
        this.calendarRule = calendarRule;
    }



    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    

}
