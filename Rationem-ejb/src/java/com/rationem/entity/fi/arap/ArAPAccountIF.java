/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.entity.fi.arap;

import com.rationem.entity.config.common.SortOrder;
import java.io.Serializable;

/**
 *
 * @author Chris
 */
public interface ArAPAccountIF extends Serializable{
 
 public SortOrder getSortOrder() ;
 public void setSortOrder(SortOrder sortOrder);
 
 
}
