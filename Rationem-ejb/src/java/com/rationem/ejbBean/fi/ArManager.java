/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.fi;

import com.rationem.busRec.doc.DocLineArRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.FiArPeriodBalanceRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.partner.PartnerBaseRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.dataManager.ArAccountDM;
import com.rationem.ejbBean.dataManager.DocumentDM;
import com.rationem.ejbBean.dataManager.MasterDataDM;
import com.rationem.util.ArPayRunSelection;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.ArAccountByRef;
import com.rationem.util.ArAcntBalChkRec;
import com.rationem.util.ArAcntSrchSelOpt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;


/**
 * Manages Accounts receivable
 * @author Chris
 */
@Stateless
@LocalBean
public class ArManager {
  private static final Logger LOGGER = Logger.getLogger(ArManager.class.getName());
  @EJB
  private ArAccountDM arAccountDM;
  
  @EJB
  private MasterDataDM masterDataDM;

  @EJB
  private DocumentDM docDM;


  public boolean arAccountExistsByActNumber(ArAccountRec account) throws BacException {
    LOGGER.log(INFO, "arAccountExistsByActNumber called with {0}", account);
    boolean actExists = arAccountDM.accountNumberExists(account);
    LOGGER.log(INFO, "DM returned {0}",actExists);

    return actExists;
  }

  public ArAccountRec createArAccount(ArAccountRec ac, UserRec usr, String page) throws BacException {
    LOGGER.log(INFO, "arManager createArAccount called with {0} ", ac);
    ArAccountRec actRet = arAccountDM.createArAccount(ac,ac.getCreatedBy(), usr, page);
    LOGGER.log(INFO, "AR Account DB layer returned {0}", actRet);
    return actRet;
  }

  
  
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

  public List<ArAccountRec> getAccountsBySelOpt(ArAcntSrchSelOpt opt){
   LOGGER.log(INFO, "getAccountsBySelOpt called with ref {0} acnt name: {1} trade {2}", 
     new Object[]{opt.getAcntCode(),opt.getAcntName(),opt.getTradeName()});
   
   if(StringUtils.isNotBlank(opt.getAcntCode())){
    String actCodeSch = opt.getAcntCode();
    actCodeSch = StringUtils.remove(actCodeSch, "%");
    actCodeSch = StringUtils.appendIfMissing(actCodeSch, "%");
    List<ArAccountRec> actList = arAccountDM.getAccountsByActNumPart(actCodeSch);
    LOGGER.log(INFO, "arAccDM acnt num search returned {0}", actList);
    if(actList == null || actList.isEmpty()){
     return null;
    }
    if(StringUtils.isNotBlank(opt.getAcntName())){
     
     List<ArAccountRec> currList = new ArrayList<>();
     ListIterator<ArAccountRec> nameLi = actList.listIterator();
     for(ArAccountRec a:actList){
      LOGGER.log(INFO, "List account name {0}",a.getArAccountName());
      if(StringUtils.startsWithIgnoreCase(a.getArAccountName(), opt.getAcntName())){
       currList.add(a);
       LOGGER.log(INFO, "Add to current list");
      }
     }
     actList = currList;
     LOGGER.log(INFO, "After Account name list {0", nameLi);
    }
    if(StringUtils.isNotBlank(opt.getTradeName())){
     LOGGER.log(INFO, "Display name {0}", opt.getTradeName());
     List<ArAccountRec> currList = new ArrayList<>();
     for(ArAccountRec a:actList){
      LOGGER.log(INFO, "List trade name {0}",a.getArAccountFor().getDisplayName());
      if(StringUtils.startsWithIgnoreCase(a.getArAccountFor().getDisplayName(), opt.getTradeName())){
       currList.add(a);
       LOGGER.log(INFO, "Add to current list");
      }
     }
     actList = currList;
    }
    
    return actList;
   }
   if(StringUtils.isNotBlank(opt.getAcntName())){
    String actNameSch = opt.getAcntName();
    actNameSch = StringUtils.remove(actNameSch, "%");
    actNameSch = StringUtils.appendIfMissing(actNameSch, "%");
    List<ArAccountRec> actList = arAccountDM.getAccountsByActNumPart(actNameSch);
    if(actList == null || actList.isEmpty()){
     return null;
    }
    if(StringUtils.isNotBlank(opt.getTradeName())){
     List<ArAccountRec> currList = new ArrayList<>();
     for(ArAccountRec a:actList){
      LOGGER.log(INFO, "List trade name {0}",a.getArAccountFor().getDisplayName());
      if(StringUtils.startsWithIgnoreCase(a.getArAccountFor().getDisplayName(), opt.getTradeName())){
       currList.add(a);
       LOGGER.log(INFO, "Add to current list");
      }
    }
    actList = currList;
    
   }
   return actList;
   }
   
   if(StringUtils.isNotBlank(opt.getTradeName())){
    String tradeNameSch = opt.getTradeName();
    tradeNameSch = StringUtils.remove(tradeNameSch, "%");
    tradeNameSch = StringUtils.appendIfMissing(tradeNameSch, "%");
    List<ArAccountRec> actList = arAccountDM.getArAccountsByTradingNamePart(tradeNameSch);
    if(actList == null || actList.isEmpty()){
     return null;
    }
    return actList;
   }
   LOGGER.log(INFO, "No Ar accounts found");
   return null;
  }
  
  public List<ArAccountRec> getAccountsBySurname(String surName) throws BacException {
  LOGGER.log(INFO, "getAccountsBySurname called with {0}", surName);
  List<ArAccountRec> actlist;
  if(surName == null || surName.isEmpty()){
   surName = "%";
  }
  else{
   surName = surName + "%";
  }
  actlist = this.arAccountDM.getAccountsBySurName(surName);
  LOGGER.log(INFO, "Armanager getAccountsBySurname got {0} from DB layer", actlist);
  return actlist ;
 }

 public List<ArAccountRec> getAccountsByTradingName(String tradingName) {
  LOGGER.log(INFO, "getAccountsByTradingName called with {0}", tradingName);
  List<ArAccountRec> actlist ;
  if(tradingName == null || tradingName.isEmpty()){
   tradingName = "%";
  }
  else{
   tradingName = tradingName + "%";
  }
  actlist = this.arAccountDM.getArAccountsByTradingNamePart(tradingName);
  LOGGER.log(INFO, "Armanager getAccountsBySurname got {0} from DB layer", actlist);
  return actlist ;
 }
 
 public List<ArAccountRec> getAccountsForCompany(CompanyBasicRec comp){
  LOGGER.log(INFO, "getAccountsByActNumberPart called with number {0}", comp.getId());
  List<ArAccountRec> actlist;
  actlist = this.arAccountDM.getArAccountsForCompany(comp);
  return actlist;
 }
 
 public List<ArAccountRec> getAccountsForPartner(PartnerBaseRec ptnr){
  LOGGER.log(INFO, "ArManager.getAccountsForPartner called with {0}", ptnr);
  List<ArAccountRec> actList = arAccountDM.getArAccountsForPtnr(ptnr);
  return actList;
 }
 
 public List<ArAcntBalChkRec> getAccountsBalCheck(CompanyBasicRec comp){
  LOGGER.log(INFO, "getAccountsBalCheck called with company {0}", comp);
  List<ArAcntBalChkRec> retList = new ArrayList<>();
  
  List<ArAccountRec> actList = arAccountDM.getArAccountsForCompany(comp);
  LOGGER.log(INFO, "actList {0}", actList);
  if(actList == null){
   LOGGER.log(INFO, "No AR accounts for comp ref {0}", comp.getReference());
   return null;
  }
  for(ArAccountRec arAcnt:actList){
   ArAcntBalChkRec acntBalChk = new ArAcntBalChkRec();
   acntBalChk.setAccountId(arAcnt.getId());
   acntBalChk.setAccountRef(arAcnt.getArAccountCode());
   acntBalChk.setName(arAcnt.getArAccountName());
   acntBalChk.setAcntBal(arAcnt.getAccountBalance());
   
   List<DocLineArRec> arLines = docDM.getOutstandingDocsForArAccount(arAcnt);
   LOGGER.log(INFO, "Lines found {0}", arLines);
   if(arLines == null || arLines.isEmpty()){
    acntBalChk.setLineBal(0);
   }else{
    double lineBal = 0;
    for(DocLineArRec arLn:arLines){
     if(arLn.getPostType().isDebit()){
      lineBal += arLn.getDocAmount();
     }else{
      lineBal -= arLn.getDocAmount();
     }
    }
    acntBalChk.setLineBal(lineBal);
   }
   retList.add(acntBalChk);
  }
  LOGGER.log(INFO, "Ret list to return {0}", retList);
  
  return retList;
 }
/**
 * retrieves account numbers based on partial account numbers
 * @param actNumber
 * @return 
 */
 public List<ArAccountRec> getAccountsByActNumberPart(String actNumber) {
  LOGGER.log(INFO, "getAccountsByActNumberPart called with number {0}", actNumber);
  List<ArAccountRec> actlist ;
  if(actNumber == null || actNumber.isEmpty()){
   actNumber = "%";
  }
  else{
   actNumber = actNumber + "%";
  }
  actlist = this.arAccountDM.getAccountsByActNumPart(actNumber);
  Collections.sort(actlist, new ArAccountByRef());
  
  return actlist;
 }
 
 public List<ArAccountRec> getAccountsByCompActNumPart(CompanyBasicRec compRec,String actNum) {
  LOGGER.log(INFO,"ArMgr.getAccountsByCompActNumPart comp {0} acnt num {1}", 
          new Object[]{compRec.getReference(), actNum});
  List<ArAccountRec> actList ;
  if(StringUtils.isBlank(actNum)){
   LOGGER.log(INFO, "All accounts for comp");
    actList = arAccountDM.getArAccountsForCompany(compRec);
  }else{
   LOGGER.log(INFO, "Limited by acnt ref");
   actNum = StringUtils.remove(actNum, "%");
   actNum = actNum + "%";
   
   actList = arAccountDM.getAccountsByActNumPart(compRec, actNum);
   if(actList == null){
    LOGGER.log(INFO, "Could not find accounts with reference {0}", actNum);
    return null;
   }
   Collections.sort(actList, new ArAccountByRef());
   
  }
  
  return actList;
 }

 
 public List<ArAccountRec> getAccountsAll() throws BacException {
  LOGGER.log(INFO, "getAccountsAll() called");
  
  return null;
 }

 public ArAccountRec updateArAccount(ArAccountRec account,  String page) 
         throws BacException {
  LOGGER.log(INFO, "updateArAccount called with account {0}  page {1}", 
          new Object[]{account.getArAccountFor().getRef(),page});
  account = arAccountDM.updateArAccount(account, page);
  LOGGER.log(INFO, "ArManger updates mades: {0}",account);
  return account;
  
 }
 public boolean updateArAccountBal(List<ArAcntBalChkRec> bals, UserRec usr, String pg){
  LOGGER.log(INFO, "updateArAccountBal called with {0}", bals);
  boolean rc = this.arAccountDM.updateArAccountBal(bals, usr, pg);
  LOGGER.log(INFO, "DB updates ok {0}", rc);
  return rc;
 }
/**
 * Gets all open documents for an Accounts Receivable account.
 * @param arAccount
 * @return
 * @throws BacException 
 */
 public List<DocLineArRec> getOpenDocsForArAccount(ArAccountRec arAccount) throws BacException {
  LOGGER.log(INFO, "ArManager.getOpenDocsForArAccount called with account {0}", arAccount);
  List<DocLineArRec> retList = docDM.getOutstandingDocsForArAccount(arAccount);
  
  return retList;
 }

 public List<DocLineArRec> getOpenDocsBySelectOpt(ArPayRunSelection selOpt, int queryParts, 
         UserRec usr, String pg) throws BacException {
  LOGGER.log(INFO, "ArMgr.getOpenDocsBySelectOpt called with {0}", selOpt);
  
  List<DocLineArRec> retList = docDM.getOpenDocBySelectOpt(selOpt,queryParts,usr,pg);
  
  return retList;
 }

 public List<FiArPeriodBalanceRec> getPerBalsForAcnt(ArAccountRec acnt, int yr){
  List<FiArPeriodBalanceRec> perBals = this.arAccountDM.getArPerBals(acnt,yr);
  if(perBals == null){
   return null;
  }else{
   return perBals;
  }
  
  
 }
 
}
