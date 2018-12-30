/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;

import com.rationem.entity.ma.CostAccountDirect;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@DiscriminatorValue("AuditCostAccountDirect")
@Table(name="audit59")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")

public class AuditCostAccountDirect extends AuditBase implements Serializable {
 
 @ManyToOne
 @JoinColumn(name="cost_account_id",referencedColumnName="cost_accnt_id")
 private CostAccountDirect costAccount;

 public CostAccountDirect getCostAccount() {
  return costAccount;
 }

 public void setCostAccount(CostAccountDirect costAccount) {
  this.costAccount = costAccount;
 }
 
 
}
