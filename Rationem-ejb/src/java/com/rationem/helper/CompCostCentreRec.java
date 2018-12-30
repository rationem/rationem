/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.helper;

import com.rationem.busRec.ma.costCent.CostCentreRec;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author user
 */
public class CompCostCentreRec implements Serializable {

 
 private Long compId;
 private CostCentreRec costCent;

 public CompCostCentreRec() {
 }

 public Long getCompId() {
  return compId;
 }

 public void setCompId(Long compId) {
  this.compId = compId;
 }

 public CostCentreRec getCostCent() {
  return costCent;
 }

 public void setCostCent(CostCentreRec costCent) {
  this.costCent = costCent;
 }

 
 @Override
 public int hashCode() {
  int hash = 3;
  hash = 53 * hash + Objects.hashCode(this.compId);
  hash = 53 * hash + Objects.hashCode(this.costCent);
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final CompCostCentreRec other = (CompCostCentreRec) obj;
  if (!Objects.equals(this.compId, other.compId)) {
   return false;
  }
  if (!Objects.equals(this.costCent, other.costCent)) {
   return false;
  }
  return true;
 }
 
 
 
 
}
