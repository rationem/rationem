/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.config.common;

import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.common.SysBuffer;
import com.rationem.ejbBean.dataManager.TreasuryDM;
import com.rationem.exception.BacException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;

/**
 * Manages configuration of BACS processing
 * @author Chris
 */
@Stateless
@LocalBean
public class BacsSetup {
 private static final Logger logger =
            Logger.getLogger("com.rationem.ejbBean.config.common.BacsSetup");
 
 @EJB
 private SysBuffer sysBuff;
 
 @EJB
 private TreasuryDM treasuryDM;
 
 

 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public BacsTransCodeRec createBacsTransCode(BacsTransCodeRec bacsTr, UserRec user, String page) 
         throws BacException {
  bacsTr = treasuryDM.createBacsTransCode(bacsTr, user, page);
  
  if(bacsTr == null || bacsTr.getId() == null || bacsTr.getId() == 0){
   throw new BacException("Could not create BACS trans cd","BACS:01");
  }
  logger.log(INFO, "createBacsTransCode returns trans cd with id {0}", bacsTr.getId());
  return bacsTr;
 }

}
