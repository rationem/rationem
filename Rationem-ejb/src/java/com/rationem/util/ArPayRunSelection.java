/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;


import com.rationem.busRec.doc.DocTypeRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.config.company.PostTypeRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Chris
 */
public class ArPayRunSelection implements Serializable {
 
 private Long id;
 private Date docDateFrom;
 private Date docDateTo;
 private Date postDateFrom;
 private Date postDateTo;
 private Date entryDateFrom;
 private Date entryDateTo;
 private Date dueDateFrom;
 private Date dueDateTo;
 
 private List<DocTypeRec> docTypes;
 private List<ArAccountRec> arAccounts;
 private List<PostTypeRec> postTypes;
 

 public ArPayRunSelection() {
 }

 public Long getId() {
  if(id == null){
   id = UUID.randomUUID().getMostSignificantBits() * -1;
  }
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 
 public Date getDocDateFrom() {
  
  return docDateFrom;
 }

 public void setDocDateFrom(Date docDateFrom) {
  this.docDateFrom = docDateFrom;
 }

 public Date getDocDateTo() {
  
  return docDateTo;
 }

 public void setDocDateTo(Date docDateTo) {
  this.docDateTo = docDateTo;
 }

 public Date getPostDateFrom() {
  return postDateFrom;
 }

 public void setPostDateFrom(Date postDateFrom) {
  this.postDateFrom = postDateFrom;
 }

 public Date getPostDateTo() {
  return postDateTo;
 }

 public void setPostDateTo(Date postDateTo) {
  this.postDateTo = postDateTo;
 }

 public Date getEntryDateFrom() {
  return entryDateFrom;
 }

 public void setEntryDateFrom(Date entryDateFrom) {
  this.entryDateFrom = entryDateFrom;
 }

 public Date getEntryDateTo() {
  return entryDateTo;
 }

 public void setEntryDateTo(Date entryDateTo) {
  this.entryDateTo = entryDateTo;
 }

 public Date getDueDateFrom() {
  return dueDateFrom;
 }

 public void setDueDateFrom(Date dueDateFrom) {
  this.dueDateFrom = dueDateFrom;
 }

 public Date getDueDateTo() {
  return dueDateTo;
 }

 public void setDueDateTo(Date dueDateTo) {
  this.dueDateTo = dueDateTo;
 }

 public List<DocTypeRec> getDocTypes() {
  return docTypes;
 }

 public void setDocTypes(List<DocTypeRec> docTypes) {
  this.docTypes = docTypes;
 }

 public List<ArAccountRec> getArAccounts() {
  return arAccounts;
 }

 public void setArAccounts(List<ArAccountRec> arAccounts) {
  this.arAccounts = arAccounts;
 }

 public List<PostTypeRec> getPostTypes() {
  return postTypes;
 }

 public void setPostTypes(List<PostTypeRec> postTypes) {
  this.postTypes = postTypes;
 }

 
 
 
}
