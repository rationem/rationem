/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.company;


import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class CalendarRuleBaseRec implements Serializable {
    private Long id;
    private UserRec createdBy;
    private Date createdOn;
    private UserRec changedBy;
    private Date changedOn;
    private int version;
    private String reference;
    private String description;
    private String calType;
    private boolean naturalCal;
    private boolean monthCal;
    private boolean flexCal;
    private List<CalendarRuleFlexYearRec> calRuleFlexYears;


    public CalendarRuleBaseRec() {
    }

    public CalendarRuleBaseRec(Long id, UserRec createdBy, Date createdOn,
            UserRec changedBy, Date changedOn, int version) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.changedBy = changedBy;
        this.changedOn = changedOn;
        this.version = version;
    }

    public UserRec getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public Date getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(Date changedOn) {
        this.changedOn = changedOn;
    }

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

 public String getCalType() {
  return calType;
 }

 public void setCalType(String calType) {
  this.calType = calType;
 }

 public boolean isNaturalCal() {
  return naturalCal;
 }

 public void setNaturalCal(boolean naturalCal) {
  this.naturalCal = naturalCal;
 }

 public boolean isMonthCal() {
  return monthCal;
 }

 public void setMonthCal(boolean monthCal) {
  this.monthCal = monthCal;
 }

 public boolean isFlexCal() {
  return flexCal;
 }

 public void setFlexCal(boolean flexCal) {
  this.flexCal = flexCal;
 }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 public List<CalendarRuleFlexYearRec> getCalRuleFlexYears() {
  return calRuleFlexYears;
 }

 public void setCalRuleFlexYears(List<CalendarRuleFlexYearRec> calRuleFlexYears) {
  this.calRuleFlexYears = calRuleFlexYears;
 }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }




}
