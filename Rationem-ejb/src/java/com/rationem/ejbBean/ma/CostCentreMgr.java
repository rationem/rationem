/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.ejbBean.ma;

import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.ma.costCent.CostAccountDirectRec;
import com.rationem.busRec.ma.costCent.CostCentreRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.dataManager.CostCentreDM;
import com.rationem.ejbBean.dataManager.FiMasterRecordDM;
import com.rationem.ejbBean.dataManager.MasterDataDM;
import com.rationem.exception.BacException;
import com.rationem.helper.comparitor.CostAccountByRef;
import com.rationem.helper.comparitor.FiGlAccountCompByRef;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.ejb.EJB;

/**
 * Manages Cost Centres. Multiple instances that are called by Web tier and hold business logic
 * @author Chris
 */
@Stateless
@LocalBean
public class CostCentreMgr {
 private static final Logger logger =
            Logger.getLogger("com.rationem.ejbBean.ma.CostCentreMgr");
 
 @EJB
 private CostCentreDM ccDM;
 
 @EJB
 private MasterDataDM masterDataDm;
 
 @EJB
 private FiMasterRecordDM fiMastDm;
 
 

 
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public void copyToComp(CompanyBasicRec compRec1,CompanyBasicRec compRec2, UserRec usr, String pg){
  logger.log(INFO, "costCentMgr copyToComp called with comp {0} comp {1}", new Object[]{
   compRec1.getReference(), compRec2.getReference()  });
  this.ccDM.copyToComp(compRec1, compRec2, usr, pg);
 }
 public CostCentreRec autoAddCostAccounts(CostCentreRec costCentre, UserRec user, String pg){
  logger.log(INFO, "autoAddCostAccounts called with cost cent id {0}", costCentre.getId());
  List<CostAccountDirectRec> costAcs = costCentre.getCostAccountDirectAcs();
  if(costAcs == null){
   // try to get from DB 
   costAcs = this.getCostAccountsByCostCent(costCentre);
   if(costAcs == null){
    costAcs = new ArrayList<>();
   }
  }
  CompanyBasicRec comp = costCentre.getCostCentreForCompany();
  List<FiGlAccountCompRec> glAccounts = comp.getGlAccounts();
  List<FiGlAccountCompRec> accountsReq = new ArrayList<>();
  if(glAccounts == null || glAccounts.isEmpty()){
   // try to get from db
   glAccounts = this.fiMastDm.getGlAccountsForCompany(comp.getId());
   if(glAccounts == null || glAccounts.isEmpty()){
    // No GL account;
    return costCentre;
   }
  }
  Collections.sort(glAccounts, new FiGlAccountCompByRef());
  Collections.sort(costAcs, new CostAccountByRef());
  logger.log(INFO, "GL accounts  {0} cost Acs {1}", new Object[]{glAccounts,costAcs});
  for(FiGlAccountCompRec glAcnt:glAccounts ){
   if(!glAcnt.getCoaAccount().isPl()){
    continue;
   }
   ListIterator<CostAccountDirectRec> cstAcntLi = costAcs.listIterator();
   boolean foundCstAcnt = false;
   while(cstAcntLi.hasNext() && !foundCstAcnt){
    CostAccountDirectRec curr = cstAcntLi.next();
    if(curr.getRef().equals(glAcnt.getCoaAccount().getRef()) ){
     foundCstAcnt = true;
     
    }
   }
   if(!foundCstAcnt){
    accountsReq.add(glAcnt);
   }
  }
  logger.log(INFO, "Number of cost sccaounts required {0}", accountsReq.size());
  
  if(!accountsReq.isEmpty()){
   Date currDate = new Date();
   for(FiGlAccountCompRec curr:accountsReq){
    
    CostAccountDirectRec currAcnt = this.ccDM.createCostAccountDirect(curr, costCentre, user, currDate, pg);
    logger.log(INFO, "cost acnt id {0}", currAcnt.getId());
    costAcs .add(currAcnt);
   }
   costCentre.setCostAccountDirectAcs(costAcs);
   
  }
  logger.log(INFO, "num Cost accounts {0} for cost cent ", costCentre.getCostAccountDirectAcs());
  return costCentre;
 }
 
 public CostAccountDirectRec updateCostAccountDirectRec(CostAccountDirectRec acnt, String pg){
  acnt = this.ccDM.updateCostAccount(acnt, pg);
  return acnt;  
 }
 public CostCentreRec updateCostCentre(CostCentreRec costCentre, UserRec user, String source) 
 throws BacException{
  logger.log(INFO,"CostCentreMgr.addCostCentre called with cc {0} source {1}",
          new Object[]{costCentre,source});
  if(costCentre.getResponsibilityOf() != null){
   PartnerPersonRec resp = costCentre.getResponsibilityOf();
   if(resp.getId() == null){
    resp.setCreatedBy(costCentre.getCreatedBy());
    resp.setCreatedDate(costCentre.getCreatedOn());
    resp = (PartnerPersonRec)masterDataDm.updatePartner(resp, source);
    costCentre.setResponsibilityOf(resp);
   }
  }
  costCentre = ccDM.updateCostCentre(costCentre,  source);
  
  return costCentre;
 }

 public List<CostCentreRec> getCostCentresForCompany(CompanyBasicRec comp) throws BacException {
  if(comp == null){
   throw new BacException("Comp required for getCostCentresForCompany");
  }
  logger.log(INFO, "getCostCentresForCompany called with company id {0}", comp.getId());
  List<CostCentreRec> costCentLst = this.ccDM.getAllCostCentres(comp);
  logger.log(INFO, "CC list returned by DB layer {0}", costCentLst);
  return costCentLst;
 }

 public CostCentreRec getCostCentreByRef(CompanyBasicRec comp, String ref) {
  
  CostCentreRec rec = this.ccDM.getCostCentreByRef(comp, ref);
  return rec;
 }
 public List<CostCentreRec> getCostCentresByRef(CompanyBasicRec comp, String ref) throws BacException {
  logger.log(INFO, "CostCentMgr.getCostCentresByRef called with comp {0} and ref {1}", 
          new Object[]{comp,ref});
  if(comp == null){
   throw new BacException("Comp required for getCostCentresForCompany");
  }
  List<CostCentreRec> costCentLst = ccDM.getCostCentresByRef(comp, ref);
  return costCentLst;
 }

 public List<CostAccountDirectRec> getCostAccountsByCostCent( 
         CostCentreRec costCent) throws BacException {
  logger.log(INFO, "getCostAccount called with glAccount {0} cosr Centre {1} ",
          new Object[]{costCent});
  List<CostAccountDirectRec> ccAcnts = ccDM.getCostAccountsByCostCent(costCent);
  logger.log(INFO, "DB layer return cost acs {0}", ccAcnts);
  
  return ccAcnts;
 }
 public CostAccountDirectRec getCostAccountByCostCentGlAc(FiGlAccountCompRec glAccount, 
         CostCentreRec costCent, boolean autoCreate, UserRec usr, Date dt, String pg) throws BacException {
  logger.log(INFO, "CostCentMgr.getCostAccountsByCostCentGlAc called with glAccount.id {0} cosr Centre {1} AutoCreate {2}",
          new Object[]{glAccount,costCent,autoCreate});
  
  CostAccountDirectRec rec = this.ccDM.getCostAccountRecByCostCentGlAc(glAccount, costCent);
  logger.log(INFO,"rec from query {0}",rec);
  if(rec == null || rec.getId() == null){
   if(autoCreate){
    logger.log(INFO, "call ccDM.createCostAccountDirect");
    rec = ccDM.createCostAccountDirect(glAccount, costCent, usr, dt,pg);
   }
  }
  
  logger.log(INFO, "CostCentMgr.getCostAccountsByCostCentGlAc 1 returns {0}",rec);
  return rec;
 }

 
 
}
