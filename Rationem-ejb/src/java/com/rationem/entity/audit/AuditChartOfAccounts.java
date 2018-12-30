/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.company.ChartOfAccounts;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.JoinColumn;

import javax.persistence.DiscriminatorValue;




/**
 *
 * @author Chris
 */
@Entity
@Table(name="audit31")
@DiscriminatorValue("audit.AuditChartOfAccounts")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditChartOfAccounts extends AuditBase {
 
 @ManyToOne
 @JoinColumn(name="coa_id",referencedColumnName="accounts_chart_id")
 private ChartOfAccounts coa;

 public ChartOfAccounts getCoa() {
  return coa;
 }

 public void setCoa(ChartOfAccounts coa) {
  this.coa = coa;
 }
 
 
 
}
