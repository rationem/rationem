/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.fi.company;

import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 *
 * @author user
 */
public class CompanyApArRec implements Serializable{
 private static final Logger LOGGER =  Logger.getLogger(CompanyApArRec.class.getName());
 private Long id;
 
 private CompanyBasicRec comp;
 private int arBucket1;
 private int arBucket2;
 private int arBucket3;
 private int arBucket4;
 private int apBucket1;
 private int apBucket2;
 private int apBucket3;
 private int apBucket4;
 
 private UserRec createdBy;
 private Date createdDate;
 private UserRec changedBy;
 private Date changedDate;

 public CompanyApArRec() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 
 public int getApBucket1() {
  return apBucket1;
 }

 public void setApBucket1(int apBucket1) {
  this.apBucket1 = apBucket1;
 }

 public int getApBucket2() {
  return apBucket2;
 }

 public void setApBucket2(int apBucket2) {
  this.apBucket2 = apBucket2;
 }

 public int getApBucket3() {
  return apBucket3;
 }

 public void setApBucket3(int apBucket3) {
  this.apBucket3 = apBucket3;
 }

 public int getApBucket4() {
  return apBucket4;
 }

 public void setApBucket4(int apBucket4) {
  this.apBucket4 = apBucket4;
 }

 public int getArBucket1() {
  return arBucket1;
 }

 public void setArBucket1(int arBucket1) {
  this.arBucket1 = arBucket1;
 }

 public int getArBucket2() {
  return arBucket2;
 }

 public void setArBucket2(int arBucket2) {
  this.arBucket2 = arBucket2;
 }

 public int getArBucket3() {
  return arBucket3;
 }

 public void setArBucket3(int arBucket3) {
  this.arBucket3 = arBucket3;
 }

 public int getArBucket4() {
  return arBucket4;
 }

 public void setArBucket4(int arBucket4) {
  this.arBucket4 = arBucket4;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedDate() {
  return createdDate;
 }

 public void setCreatedDate(Date createdDate) {
  this.createdDate = createdDate;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedDate() {
  return changedDate;
 }

 public void setChangedDate(Date changedDate) {
  this.changedDate = changedDate;
 }

 
 
}
