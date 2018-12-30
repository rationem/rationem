/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.fi;

import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.ejbBean.dataManager.ApAccountDM;
import com.rationem.exception.BacException;
import com.rationem.helper.ApArAccountBal;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;

/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class ApManager {
 private static final Logger LOGGER =
            Logger.getLogger("com.rationem.ejbBean.fi.ApManager");
@EJB
private ApAccountDM apMgrDm;
 public boolean apAccountExists(ApAccountRec apAccount) throws BacException{
  LOGGER.log(INFO, "apAccountExists {0}", apAccount);
  try{
   boolean rc = apMgrDm.apAccountExistsByActNum(apAccount);
   LOGGER.log(INFO, "Account exists from DB {0}",rc);
   return rc;
  }catch(BacException ex){
   LOGGER.log(INFO, "DB Error checking em {0}", ex.getLocalizedMessage());
  } 
  
  return  false;
 }

 
 public List<ApAccountRec> getApAccountsAllCrossComp(List<CompanyBasicRec> comps){
  
  List<ApAccountRec> retList = apMgrDm.apAccountsAllCrossComp(comps);
  LOGGER.log(INFO, "getApAccountsAllCrossComp acs from DB layer {0}", retList);
  return retList;
 }
 
 public List<ApAccountRec> getApAccountsAll(CompanyBasicRec comp){
  LOGGER.log(INFO, "getApAccountsAll called with company {0}", comp.getId());
  List<ApAccountRec> retList = apMgrDm.apAccountsForCompAll(comp);
  return retList;
 }
 
 public List<ApAccountRec> getApAccountsStartinfWithCode(CompanyBasicRec comp, String startCode){
  List<ApAccountRec> retList = apMgrDm.apAccountsForCompByCode(comp, startCode);
  return retList;
  
 }
 
 public List<ApAccountRec> getApAccountByCodeCrossComp(String code, List<CompanyBasicRec> comps){
  LOGGER.log(INFO, "ApMgr getApAccountByCodeCrossComp called with code {0} companies {1} ", 
    new Object[]{code,comps});
  List<ApAccountRec> retList = apMgrDm.apAccountsByCodeCrossComp(code, comps);
  LOGGER.log(INFO, "AP accounts returned from DB layer {0}", retList);
  return retList;
  
 }
 
 public ApAccountRec getApAccountPeriodBalances(ApAccountRec acntRec){
  acntRec = apMgrDm.apAccountPeriodBalances(acntRec);
  return acntRec;
 }
 
 public List<ApAccountRec> getApAccountsForPartner(PartnerBaseRec ptnr){
  List<ApAccountRec> retList = apMgrDm.apAccountsForPartner(ptnr);
  LOGGER.log(INFO, "apManger.getApAccountsForPartner");
  for(ApAccountRec curr:retList){
   LOGGER.log(INFO, "Acnt bal {0}",curr.getAccountBalance());
  }
  return retList;
  
 }
 
 public List<ApArAccountBal> getApBalances(CompanyBasicRec comp){
  List<ApArAccountBal> bals = apMgrDm.getApBalances(comp);
  return bals;
 }
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")
 
 public ApAccountRec updateApAccount(ApAccountRec acnt,String view){
  LOGGER.log(INFO, "updateApAccount called acnt {0} ", acnt);
  acnt = apMgrDm.updateApAccount(acnt, view);
  LOGGER.log(INFO, "Acnt id {0} ", acnt.getId());
  return acnt;
 } 
 
 public boolean updateApAccountBal(ApAccountRec acntRec,String view){
  boolean rc = apMgrDm.updateApAccountBal(acntRec, view);
  return rc;
 }

}
