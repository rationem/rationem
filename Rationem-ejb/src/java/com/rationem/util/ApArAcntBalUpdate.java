/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util;

import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import java.io.Serializable;

/**
 *
 * @author user
 */
public class ApArAcntBalUpdate implements Serializable{
 
 
 private ApAccountRec apAcnt;
 private ArAccountRec arAcnt;
 private boolean acntBalUpdate;
 private boolean perBalUpdate;

 public ApArAcntBalUpdate() {
 }

 public ApAccountRec getApAcnt() {
  return apAcnt;
 }

 public void setApAcnt(ApAccountRec apAcnt) {
  this.apAcnt = apAcnt;
 }

 public ArAccountRec getArAcnt() {
  return arAcnt;
 }

 public void setArAcnt(ArAccountRec arAcnt) {
  this.arAcnt = arAcnt;
 }

 public boolean isAcntBalUpdate() {
  return acntBalUpdate;
 }

 public void setAcntBalUpdate(boolean acntBalUpdate) {
  this.acntBalUpdate = acntBalUpdate;
 }

 public boolean isPerBalUpdate() {
  return perBalUpdate;
 }

 public void setPerBalUpdate(boolean perBalUpdate) {
  this.perBalUpdate = perBalUpdate;
 }
 
 
 
}
