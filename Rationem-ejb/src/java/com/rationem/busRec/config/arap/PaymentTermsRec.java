/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.arap;

import com.rationem.busRec.config.common.UomRec;
import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class PaymentTermsRec implements Serializable {
    private Long id;
  private String payTermsCode;
  /**
   * This is one of docDT document date, postDT posting date, Day of following month
   */
  private String baseType;
  private String description;
  private int days;
  private int dayOfMonth;
  private UomRec uom;
  private UserRec createdBy;
  private Date createdOn;
  private UserRec changedBy;
  private Date changedOn;
  private int revision;

  private ArrayList<DocLineArRec> aRDocLines;

  public PaymentTermsRec() {
  }

  public ArrayList<DocLineArRec> getaRDocLines() {
    return aRDocLines;
  }

  public void setaRDocLines(ArrayList<DocLineArRec> aRDocLines) {
    this.aRDocLines = aRDocLines;
  }

  public String getBaseType() {
    return baseType;
  }

  public void setBaseType(String baseType) {
    this.baseType = baseType;
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

  public int getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(int dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public int getDays() {
    return days;
  }

  public void setDays(int days) {
    this.days = days;
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

  public String getPayTermsCode() {
    return payTermsCode;
  }

  public void setPayTermsCode(String payTermsCode) {
    this.payTermsCode = payTermsCode;
  }

 public UomRec getUom() {
  return uom;
 }

 public void setUom(UomRec uom) {
  this.uom = uom;
 }

  
  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }




}
