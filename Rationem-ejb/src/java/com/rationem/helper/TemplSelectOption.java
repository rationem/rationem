/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.Date;

/**
 *
 * @author user
 */
public class TemplSelectOption {
 
 private CompanyBasicRec comp;
 private Date docDateFrom;
 private Date docDateTo;
 private Date postDate;
 private Date nextDateFrom;
 private Date nextDateTo;
 private String templType;
 

 public TemplSelectOption() {
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public Date getPostDate() {
  return postDate;
 }

 public void setPostDate(Date postDate) {
  this.postDate = postDate;
 }

 
 public String getTemplType() {
  return templType;
 }

 public void setTemplType(String templType) {
  this.templType = templType;
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

 public Date getNextDateFrom() {
  return nextDateFrom;
 }

 public void setNextDateFrom(Date nextDateFrom) {
  this.nextDateFrom = nextDateFrom;
 }

 public Date getNextDateTo() {
  return nextDateTo;
 }

 public void setNextDateTo(Date nextDateTo) {
  this.nextDateTo = nextDateTo;
 }
 
 
 
}
