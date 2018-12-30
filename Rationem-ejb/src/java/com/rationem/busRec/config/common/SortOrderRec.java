/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.config.common;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;

/**
 * Specifies a sort order. In GL accounts this field is moved into the sort column
 * @author Chris
 */
public class SortOrderRec implements Serializable{
  private Long id;
  private String sortCode;
  private String name;
  private String description;

  private UserRec createdBy;
  private Date createdOn;
  private UserRec updatedBy;
  private Date updatedOn;
  private int revision;

  public SortOrderRec() {
  }

  public SortOrderRec(Long id, String sortCode, String name, String description, UserRec createdBy,
          Date createdOn, UserRec updatedBy, Date updatedOn, int revision) {
    this.id = id;
    this.sortCode = sortCode;
    this.name = name;
    this.description = description;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
    this.revision = revision;
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

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public String getSortCode() {
    return sortCode;
  }

  public void setSortCode(String sortCode) {
    this.sortCode = sortCode;
  }

  public UserRec getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(UserRec updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }


}
