/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.mdm;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * 
 * @author Chris
 */
public class CountryRec implements Serializable {
 private static Logger LOGGER = Logger.getLogger(CountryRec.class.getName());
 private Long id;
 private String countryName;
 private String countryNameFr;
 private String countryCode2;
 private String countryCode3;
 private int countryNumeric;
 private String dialCd;
 private String currCode;
 private int fractionalLength;
 private String currMajorTitle;
 private String currMajorTitlePlural;
 private String currName;
 private int currNumCode;
 private boolean independent;
 private String dependency;
 private Date createdOn;
 private UserRec createdBy;
 private Date changedOn;
 private UserRec changedBy;
 private Locale locale;

 public CountryRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNameFr() {
        return countryNameFr;
    }

    public void setCountryNameFr(String countryNameFr) {
        this.countryNameFr = countryNameFr;
    }

    public String getCountryCode2() {
        return countryCode2;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getCountryCode3() {
        return countryCode3;
    }

    public void setCountryCode3(String countryCode3) {
        this.countryCode3 = countryCode3;
    }

    public int getCountryNumeric() {
        return countryNumeric;
    }

    public void setCountryNumeric(int countryNumeric) {
        this.countryNumeric = countryNumeric;
    }

    public String getDialCd() {
        return dialCd;
    }

    public void setDialCd(String dialCd) {
        this.dialCd = dialCd;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public int getFractionalLength() {
        return fractionalLength;
    }

    public void setFractionalLength(int fractionalLength) {
        this.fractionalLength = fractionalLength;
    }

 public String getCurrMajorTitle() {
  return currMajorTitle;
 }

 public void setCurrMajorTitle(String currMajorTitle) {
  this.currMajorTitle = currMajorTitle;
 }

 public String getCurrMajorTitlePlural() {
  return currMajorTitlePlural;
 }

 public void setCurrMajorTitlePlural(String currMajorTitlePlural) {
  this.currMajorTitlePlural = currMajorTitlePlural;
 }

    
    public String getCurrName() {
        return currName;
    }

    public void setCurrName(String currName) {
        this.currName = currName;
    }

    public int getCurrNumCode() {
        return currNumCode;
    }

    public void setCurrNumCode(int currNumCode) {
        this.currNumCode = currNumCode;
    }

    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

 public Locale getLocale() {
  return locale;
 }

 public void setLocale(Locale locale) {
  this.locale = locale;
 }


 
 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }
 
 
}
