/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.ma.programme.ProgrammeRec;
import java.io.Serializable;

/**
 *
 * @author user
 */
public class CompProgrammeRec implements Serializable  {
 
 private Long compId;
 private ProgrammeRec prog;

 public CompProgrammeRec() {
 }

 public Long getCompId() {
  return compId;
 }

 public void setCompId(Long compId) {
  this.compId = compId;
 }

 public ProgrammeRec getProg() {
  return prog;
 }

 public void setProg(ProgrammeRec prog) {
  this.prog = prog;
 }
 
 
 
}
