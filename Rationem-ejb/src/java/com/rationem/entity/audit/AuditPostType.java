/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.audit;
import com.rationem.entity.config.company.PostType;
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
@DiscriminatorValue("audit.AuditPostType")
@Table(name="audit26")
@PrimaryKeyJoinColumn(name="audit_id",referencedColumnName = "audit_id")
public class AuditPostType extends AuditBase {
 @ManyToOne
 @JoinColumn(name="post_type_id",referencedColumnName="post_type_id")
 private PostType postType;

 public PostType getPostType() {
  return postType;
 }

 public void setPostType(PostType postType) {
  this.postType = postType;
 }
 
 
}
