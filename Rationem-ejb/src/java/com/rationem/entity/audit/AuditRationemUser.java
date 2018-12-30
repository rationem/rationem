package com.rationem.entity.audit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import com.rationem.entity.user.User;

@Entity
@DiscriminatorValue("audit.AuditUser")
@Table(name="audit73")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditRationemUser extends AuditBase {
 private static final long serialVersionUID = 1L;
 @ManyToOne
 @JoinColumn(name="user_id",referencedColumnName="partner_id")
 private User usr;
 /**
  * @return the usr
  */
 public User getUsr() {
  return usr;
 }
 /**
  * @param usr the usr to set
  */
 public void setUsr(User usr) {
  this.usr = usr;
 }
 
 

}
