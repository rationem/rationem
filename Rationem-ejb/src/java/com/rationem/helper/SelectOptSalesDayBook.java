/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.Date;

/**
 *
 * @author user
 */
public class SelectOptSalesDayBook {
 
 
 private CompanyBasicRec comp;
 private int perFrom;
 private int perTo;
 private int yrFrom;
 private int yrTo;
 private Date postDateFrom;
 private Date postDateTo;
 private Date docDateFrom;
 private Date docDateTo;
 private Date entryDateFrom;
 private Date entryDateTo;
 
 public SelectOptSalesDayBook() {
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 
 public int getPerFrom() {
  return perFrom;
 }

 public void setPerFrom(int perFrom) {
  this.perFrom = perFrom;
 }

 public int getPerTo() {
  return perTo;
 }

 public void setPerTo(int perTo) {
  this.perTo = perTo;
 }

 public int getYrFrom() {
  return yrFrom;
 }

 public void setYrFrom(int yrFrom) {
  this.yrFrom = yrFrom;
 }

 public int getYrTo() {
  return yrTo;
 }

 public void setYrTo(int yrTo) {
  this.yrTo = yrTo;
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
 
 
 
 
}
