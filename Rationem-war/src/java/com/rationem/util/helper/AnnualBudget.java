/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;

import com.rationem.busRec.fi.glAccount.FiPeriodBalanceRec;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author user
 */
public class AnnualBudget {
 
 private long id;
 private int year;
 private List<FiPeriodBalanceRec> perBudgets;

 public AnnualBudget() {
  UUID uuid = UUID.randomUUID();
  id = uuid.getLeastSignificantBits();
 }

 public long getId() {
  return id;
 }

 public void setId(long id) {
  this.id = id;
 }

 public int getYear() {
  return year;
 }

 public void setYear(int year) {
  this.year = year;
 }

 public List<FiPeriodBalanceRec> getPerBudgets() {
  return perBudgets;
 }

 public void setPerBudgets(List<FiPeriodBalanceRec> perBudgets) {
  this.perBudgets = perBudgets;
 }
 
 
 
 
}
