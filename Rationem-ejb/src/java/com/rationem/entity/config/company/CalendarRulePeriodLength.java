/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.config.company;

import com.rationem.entity.config.common.Uom;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Chris
 */
@Entity
@Table(name="bac_config07")
@DiscriminatorValue("com.rationem.entity.config.company.CalendarRulePeriodLength")
@PrimaryKeyJoinColumn(name="fis_per_cal_per_len_id",referencedColumnName = "fis_per_cal_base_id")

public class CalendarRulePeriodLength extends CalendarRuleBase implements Serializable {
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="uom_id", referencedColumnName="uom_id")
 private Uom unitOfMeasure;
    
 @Column(name="per_len")
 private int periodLen;

 public Uom getUnitOfMeasure() {
  return unitOfMeasure;
 }

 public void setUnitOfMeasure(Uom unitOfMeasure) {
  this.unitOfMeasure = unitOfMeasure;
 }

 public int getPeriodLen() {
  return periodLen;
 }

 public void setPeriodLen(int periodLen) {
  this.periodLen = periodLen;
 }

 
}
