/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.common;

import java.util.ListIterator;
import com.rationem.ejbBean.dataManager.AuditDM;
import javax.ejb.EJB;
import com.rationem.busRec.audit.AuditGlAccountRec;
import com.rationem.busRec.audit.AuditBaseRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountBaseRec;
import com.rationem.busRec.user.UserRec;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * Audit manager business rules for auditing
 * @author Chris
 */
@Stateless
@LocalBean
public class AuditManager {
   private static final Logger logger =
            Logger.getLogger(AuditManager.class.getSimpleName());

   @EJB
   private AuditDM auditDm;

  public ArrayList<AuditGlAccountRec> getGlCoaAccountChanges(FiGlAccountBaseRec account, Date fromDt,
          Date toDt, UserRec usr) {
    logger.log(INFO, "getGlCoaAccountChanges called with account {0} from date {1} to date {2} user {3}"
            , new Object[] {account,fromDt,toDt,usr});
    ArrayList<AuditGlAccountRec> changes = new ArrayList<>();
    ArrayList<AuditBaseRec> dbChanges ;
    if(account == null &&fromDt == null && toDt == null && usr == null ){
      logger.log(INFO,"Get all audit recs");
      dbChanges  = auditDm.allGlAccountChanges();
      logger.log(INFO, "Audit recs returned from AuditDM:  {0}", dbChanges);
      ListIterator li = dbChanges.listIterator();
      while(li.hasNext()){
        //logger.log(INFO, "audit class from DB manger is {0}", li.next().getClass().getName());
        AuditGlAccountRec glAuditRec = (AuditGlAccountRec) li.next();
        changes.add(glAuditRec);
      }
      logger.log(INFO, "Audit recs found {0}", changes);
    }else if(account != null && fromDt == null && toDt == null && usr == null ){
      logger.log(INFO,"Get all audit recs for account {0}",account);
    }else if(account == null &&fromDt != null && toDt == null && usr == null ){
      logger.log(INFO,"Get all audit recs for  from date {0}",fromDt);
    }else if(account != null &&fromDt != null && toDt != null && usr == null ){
      logger.log(INFO,"Get all audit recs for  to date {0}",
              fromDt);
    }else if(account == null &&fromDt == null && toDt == null && usr != null ){
      logger.log(INFO,"Get all audit recs for user {0}",
             usr);
    }else if(account == null &&fromDt != null && toDt != null && usr == null ){
      logger.log(INFO,"Get all audit recs for  from date {0} to date {1} ",
              new Object [] {fromDt,toDt});
    }else if(account == null &&fromDt != null && toDt == null && usr != null ){
      logger.log(INFO,"Get all audit recs for user {0} and from date {1} ",
              new Object [] {usr,fromDt});
    }else if(account == null &&fromDt != null && toDt == null && usr != null ){
      logger.log(INFO,"Get all audit recs for user {0} and from date {1} ",
              new Object [] {usr,toDt});
    }

      return changes;

  }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


 
}
