/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package com.rationem.ejbBean.dataManager;
import java.io.Serializable;
import com.rationem.exception.BacException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;


import static java.util.logging.Level.INFO;
import static java.util.logging.Level.FINEST;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author Chris
 */
@Stateless
public class DataBaseAdminDM implements Serializable {
  private static final Logger logger =
            Logger.getLogger("com.rationem.ejbBean.dataManager.ConfigurationDM");

    @PersistenceContext(name="Active-ejbPU")
    private EntityManager em;

  public String backUpDB(String filePath) {
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
    String backupDirectory = filePath + dateFmt.format(Calendar.getInstance().getTime());
    String resp = "Backed database to: "+backupDirectory;
    Query q = em.createNamedQuery("SYSCS_UTIL.SYSCS_BACKUP_DATABASE");
    q.setParameter(1, backupDirectory);
    q.executeUpdate();
    logger.log(INFO, "end of backUpDB with response {0}",resp);
    return resp;

  }



}
