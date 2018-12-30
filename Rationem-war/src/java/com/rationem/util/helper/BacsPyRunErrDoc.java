/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import com.rationem.busRec.doc.DocLineArRec;
import java.util.UUID;

/**
 *
 * @author Chris
 */
public class BacsPyRunErrDoc {
 
 private Long id;
 private DocLineArRec doc;
 private String errorCD;
 private String errorText;

 public BacsPyRunErrDoc() {
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

 public DocLineArRec getDoc() {
  return doc;
 }

 public void setDoc(DocLineArRec doc) {
  this.doc = doc;
 }

 public String getErrorCD() {
  return errorCD;
 }

 public void setErrorCD(String errorCD) {
  this.errorCD = errorCD;
 }

 public String getErrorText() {
  return errorText;
 }

 public void setErrorText(String errorText) {
  this.errorText = errorText;
 }
 
 
 
}
