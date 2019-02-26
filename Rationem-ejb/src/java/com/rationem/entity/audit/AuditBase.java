/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.user.User;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import java.io.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.Version;


import javax.persistence.SequenceGenerator;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import static javax.persistence.TemporalType.TIMESTAMP;
import static javax.persistence.DiscriminatorType.STRING;
import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.DiscriminatorType.STRING;


/**
 *
 * @author Chris
 */
@Entity

@Inheritance(strategy=JOINED )
@DiscriminatorColumn(name="DTYPE",discriminatorType=STRING,length=50)
@DiscriminatorValue("audit.auditBase")
@Table(name="audit01" )
@SequenceGenerator(name = "auditBase_s1", sequenceName = "audit01_s1", allocationSize = 1,initialValue=1)
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name="tenant_id",discriminatorType=STRING,length=50, contextProperty="tenantId")
public class AuditBase implements Serializable{
 @Id
 @GeneratedValue(generator = "auditBase_s1")
 @Column(name="audit_id")
 private Long id;
 
 public static final char ACTION_INSERT = 'I';
 public static final char ACTION_UPDATE = 'U';
 public static final char ACTION_DELETE = 'D';
    
 @Temporal(TIMESTAMP)
 @Column(name="event_date")
 private Date auditDate;
/**
  * User action is either I=Insert, U=Update, D=Delete
  */
 @Column(name="user_action")
 private char usrAction;
    /**
     * page user was on when the event occurred
     */
 @Column(name="source")
 private String source;

   /**
    * This is an audit record for company GL account
    */
 @Column(name="field_name")
 private String fieldName;

 @Column(name="new_value")
 private String newValue;

 @Column(name="old_value")
 private String oldValue;

 @ManyToOne
 @JoinColumn(name="created_by_id", referencedColumnName="partner_id")
 private User createdBy;
    
 public AuditBase() {
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public Date getAuditDate() {
  return auditDate;
 }

 public void setAuditDate(Date auditDate) {
  this.auditDate = auditDate;
 }

 public char getUsrAction() {
  return usrAction;
 }

 public void setUsrAction(char usrAction) {
  this.usrAction = usrAction;
 }

 public String getSource() {
  return source;
 }

 public void setSource(String source) {
  this.source = source;
 }

 public String getFieldName() {
  return fieldName;
 }

 public void setFieldName(String fieldName) {
  this.fieldName = fieldName;
 }

 public String getNewValue() {
  return newValue;
 }

 public void setNewValue(String newValue) {
  this.newValue = newValue;
 }

 public String getOldValue() {
  return oldValue;
 }

 public void setOldValue(String oldValue) {
  this.oldValue = oldValue;
 }

 public User getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(User createdBy) {
  this.createdBy = createdBy;
 }
 
 
 
}
