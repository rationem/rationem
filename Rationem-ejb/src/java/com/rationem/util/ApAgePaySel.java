/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.io.Serializable;

/**
 *
 * @author user
 */
public class ApAgePaySel implements Serializable {
 
 private CompanyBasicRec comp;
 private String actRefFr;
 private String actRefTo;
 private String acntNameFr;
 private String AcntNameTo;

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public String getActRefFr() {
  return actRefFr;
 }

 public void setActRefFr(String actRefFr) {
  this.actRefFr = actRefFr;
 }

 public String getActRefTo() {
  return actRefTo;
 }

 public void setActRefTo(String actRefTo) {
  this.actRefTo = actRefTo;
 }

 public String getAcntNameFr() {
  return acntNameFr;
 }

 public void setAcntNameFr(String acntNameFr) {
  this.acntNameFr = acntNameFr;
 }

 public String getAcntNameTo() {
  return AcntNameTo;
 }

 public void setAcntNameTo(String AcntNameTo) {
  this.AcntNameTo = AcntNameTo;
 }
 
 
}
