/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config06")
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRuleMonth")
@PrimaryKeyJoinColumn(name="fis_per_calFIS_PER_CAL_MONTH_ID",referencedColumnName = "fis_per_cal_base_id")
@NamedQuery(name = "getAllMthCalRules",
query="Select calRule from CalendarRuleMonth calRule order by calRule.startMonthNumber")

public class CalendarRuleMonth extends CalendarRuleBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="month_num")
 private int startMonthNumber;

 public int getStartMonthNumber() {
  return startMonthNumber;
 }

 public void setStartMonthNumber(int startMonthNumber) {
  this.startMonthNumber = startMonthNumber;
 }



}
