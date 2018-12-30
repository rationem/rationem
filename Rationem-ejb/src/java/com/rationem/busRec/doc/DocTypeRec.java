/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.doc;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class DocTypeRec implements Serializable {

  private Long Id;
  private String code;
  private String name;
  /**
     * Can document include GL lines
  */
  private boolean glAllowed;
   /**
     * Can document include AR lines
  */
  private boolean arAllowed;
   /**
     * Can document include AP lines
  */
  private boolean apAllowed;
   /**
     * Can document include TR lines
  */
  private boolean trAllowed;
  private List<DocBaseRec> documents;
  private Date created;
  private UserRec createdBy;
  private Date changedOn;
  private UserRec changedBy;
  private int revision;

  public DocTypeRec() {
  }

  public DocTypeRec(Long Id, String code, String name, boolean glAllowed, boolean arAllowed, boolean apAllowed,
          boolean trAllowed, List<DocBaseRec> documents, Date created, UserRec createdBy, Date updateDate,
          UserRec updateBy, int revision) {
    this.Id = Id;
    this.code = code;
    this.name = name;
    this.glAllowed = glAllowed;
    this.arAllowed = arAllowed;
    this.apAllowed = apAllowed;
    this.trAllowed = trAllowed;
    this.documents = documents;
    this.created = created;
    this.createdBy = createdBy;
    this.changedOn = updateDate;
    this.changedBy = updateBy;
    this.revision = revision;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long Id) {
    this.Id = Id;
  }

  public boolean isApAllowed() {
    return apAllowed;
  }

  public void setApAllowed(boolean apAllowed) {
    this.apAllowed = apAllowed;
  }

  public boolean isArAllowed() {
    return arAllowed;
  }

  public void setArAllowed(boolean arAllowed) {
    this.arAllowed = arAllowed;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public UserRec getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserRec createdBy) {
    this.createdBy = createdBy;
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

  public List<DocBaseRec> getDocuments() {
    return documents;
  }

  public void setDocuments(List<DocBaseRec> documents) {
    this.documents = documents;
  }

  public boolean isGlAllowed() {
    return glAllowed;
  }

  public void setGlAllowed(boolean glAllowed) {
    this.glAllowed = glAllowed;
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

  public boolean isTrAllowed() {
    return trAllowed;
  }

  public void setTrAllowed(boolean trAllowed) {
    this.trAllowed = trAllowed;
  }

    




}
