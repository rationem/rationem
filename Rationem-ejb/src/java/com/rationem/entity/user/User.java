/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.user;

import com.rationem.entity.fi.company.CompanyApAr;
import com.rationem.entity.mdm.PartnerPerson;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;



import static javax.persistence.TemporalType.DATE;

import javax.persistence.OneToMany;



/**
 *
 * @author Chris
 */
@Entity

@DiscriminatorValue("user.User")
@NamedQueries({
 @NamedQuery(name="userById", query="select u from User u where u.id = :uId"),
 @NamedQuery(name="userAll", query="select u from User u"),
 @NamedQuery(name="userCntryBlnk",query="select u from User u where u.country is null")
})
@Table(name="usr01")

@PrimaryKeyJoinColumn(name="partner_id",referencedColumnName = "partner_id")

public class User extends PartnerPerson implements Serializable {
 private static final long serialVersionUID = 1L;
 @Column(name="start_date")
 @Temporal(DATE)
 private Date startDate;
 @Column(name="end_date")
 @Temporal(DATE)
 private Date endDate;
 @Column(name="sys_usr")
 private boolean systemUser;
 @OneToMany(mappedBy = "createdBy")
 private List<CompanyApAr> compApArs;
 
 
  
 

 public User() {
 }

 public List<CompanyApAr> getCompApArs() {
  return compApArs;
 }

 public void setCompApArs(List<CompanyApAr> compApArs) {
  this.compApArs = compApArs;
 }

 

 
 
 public Date getStartDate() {
  return startDate;
 }

 public void setStartDate(Date startDate) {
  this.startDate = startDate;
 }

 public Date getEndDate() {
  return endDate;
 }

 public void setEndDate(Date endDate) {
  this.endDate = endDate;
 }

 public boolean isSystemUser() {
  return systemUser;
 }

 public void setSystemUser(boolean systemUser) {
  this.systemUser = systemUser;
 }

 
}
