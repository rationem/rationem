/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.dataManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class DataBaseDM {

  @PersistenceContext(name="Active-ejbPU")
  private EntityManager em;

  public boolean backUpDatabase(String file) {
    Date date = new Date();
    SimpleDateFormat dateFmt =  new SimpleDateFormat("yyyyMMdd");
    String qStr ="CALL SYSCS_UTIL_SYSCS_BACKUP_DATABASE(\'"+file+"\');";
    Query q = em.createNativeQuery(qStr) ;
    q.executeUpdate();



    return false;
  }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


 
}
