/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.ejbBean.tr;

import com.rationem.busRec.config.arap.PaymentTypeRec;
import com.rationem.busRec.config.common.NumberRangeChequeRec;
import com.rationem.busRec.doc.DocBankLineBaseRec;
import com.rationem.busRec.doc.DocBankLineChqRec;
import com.rationem.busRec.doc.DocLineBaseRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import java.util.ListIterator;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.tr.BacsTransCodeRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.ejbBean.dataManager.MasterDataDM;
import java.util.Date;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.ejbBean.dataManager.ConfigurationDM;
import com.rationem.ejbBean.dataManager.TreasuryDM;

import com.rationem.exception.BacException;
import com.rationem.helper.ChequeListSelOpt;
import com.rationem.helper.FiDoclSelectionOpt;
import com.rationem.helper.comparitor.ChequeBookByShrtDescr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import org.apache.commons.lang3.StringUtils;



/**
 *
 * @author Chris
 */
@Stateless
@LocalBean
public class BankManager {
   private static final Logger LOGGER =
            Logger.getLogger(BankManager.class.getSimpleName());


  @EJB
  private TreasuryDM bankDM;

  @EJB
  private MasterDataDM masterDataDM;
  
  @EJB
  private ConfigurationDM configDm;

  public BankBranchRec getBankBranchBySortCode(String sortCode){
   BankBranchRec branch = this.bankDM.getBranchBySortCode(sortCode);
   return branch;
  }
  public List<BankBranchRec> getBanchesBranchBySortCode(String sortCode) throws BacException {

    List<BankBranchRec> branchList = bankDM.getBranchesBySortCode(sortCode);
    return branchList;
  }

  public List<BankBranchRec> getBranchesAll(){
   return bankDM.getBranchesAll();
  } 
  
  public List<BankAccountRec> getBankAccountsForBranch(BankBranchRec br){
   List<BankAccountRec> accountList = bankDM.getBankAccountsByBranch(br);
   return accountList;
  }
  
  public List<BankAccountRec> getBankAccountsForBranchAcntNum(BankBranchRec br, String acntNum){
   List<BankAccountRec> accountList = bankDM.getBankAccountsByBrAcntNumPt(br, acntNum);
   return accountList;
  }
  public List<BankAccountRec> getBankAccountsForSortCode(String sortCode) {
    LOGGER.log(INFO,"Bank Manager getBankAccountsForSortCode called with sortCode {0}",sortCode);
    List<BankAccountRec> accountList = bankDM.getBankAccountsForBranch(sortCode);
    LOGGER.log(INFO, "Accounts return from DB layer {0}", accountList);
    return accountList;
  }
  
  

  public List<BankRec> getBanks() throws BacException {
    LOGGER.log(INFO, "BankManager getBanks called");
    List<BankRec> banks = this.bankDM.getAllBanks();
    LOGGER.log(INFO, "BankManager getBanks returns bank List {0}",banks);
    return banks;
  }
  
  public boolean bacsTransCodeDelete(BacsTransCodeRec code, UserRec delUser, String page){
   LOGGER.log(INFO, "Called  bacsTransCodeDelete");
   
   boolean rc = bankDM.deleteBacsTransCode(code, delUser, page);
   return rc;
  }

  public BankRec createBank(BankRec bank, UserRec usr, String pg) {
    LOGGER.log(INFO, "Bank Mgr called with {0} and user {1}",
            new Object[]{bank,usr});
    bank.setCreatedBy(usr);
    String tradingName = bank.getBankOrganisation().getTradingName();
    List<PartnerCorporateRec> bnkPtnrs =
            this.masterDataDM.getCorpPartnerByTradingName(tradingName);
    if(bnkPtnrs == null || bnkPtnrs.isEmpty() ){
      // new bank partner
      bank.getBankOrganisation().setCreatedBy(usr);
      bank.getBankOrganisation().setCreatedDate(new Date());
    }else{
      boolean found = false;
      ListIterator<PartnerCorporateRec> li = bnkPtnrs.listIterator();
      while(li.hasNext() && !found){
       PartnerCorporateRec bnkPtnr = li.next();
       if(bnkPtnr.getTradingName().equalsIgnoreCase(tradingName)){
         bank.setBankOrganisation(bnkPtnr);
       }
      }
    }
    
    bank = this.bankDM.createBank(bank,pg);
    
    LOGGER.log(INFO, "bank after call to DB layer", bank);
    return bank;
  }

  public BankRec getBankByBankCode(String bankCode) {
    LOGGER.log(INFO, "bank manager getBankByBankCode called with {0}",bankCode );
    return bankDM.getBankByCode(bankCode);
  }

  public BankBranchRec createBankBranch(BankBranchRec branch,UserRec usr, String view) 
          throws BacException {
    LOGGER.log(INFO,"Bank manager createBankBranch with branch {0} user {1} view {2}",
            new Object[]{branch, usr,view});
    // check that branch does not already exist
    BankBranchRec existingBr = bankDM.getBranchBySortCode(branch.getSortCode());
    LOGGER.log(INFO, "Get branch by sort code returns {0}", existingBr);
    if(existingBr != null){
     throw new BacException("bnkBrExists");
    }
    branch.setCreatedBy(usr);
    branch.setCreatedOn(new Date());
    branch = bankDM.createBankBranch(branch, usr, view);
    
    return branch;
    
  }
  /*public BankBranchRec createBankBranch(BankBranchRec branch,UserRec usr, String pg) throws BacException {
    LOGGER.log(INFO,"Bank manager createBankBranch with branch {0} user {1}",
            new Object[]{branch, usr});
    branch.setCreatedBy(usr);
    branch.setCreatedOn(new Date());
    branch = this.bankDM.createBankBranch(branch, usr);
    return branch;
  }*/

  /**
   * Get a list of bank Accounts by an account number
   * @param accountNumber
   * @return
   * @throws BacException
   */
  public List<BankAccountRec> getAccountsByPartAccountNum(String accountNumber) throws BacException {
    LOGGER.log(INFO, "getAccountsByPartAccountNum called with {0}", accountNumber);
    List<BankAccountRec> acList = bankDM.getAccountsByActNumPart(accountNumber);
    LOGGER.log(INFO, "Bank manager layer returns {0}",acList);
    return acList;
  }
  
  public List<BankAccountRec> getAccountsByPartAccntNumForBr(String accountNumber, BankBranchRec br){
   LOGGER.log(INFO, "getAccountsByPartAccntNumForBr called with num {0} barnch {1}", 
     new Object[]{accountNumber,br.getSortCode()});
   String acntNum ;
   if(accountNumber == null || accountNumber.isEmpty()){
    acntNum = "%";
   }else {
    accountNumber = accountNumber.replace("%", "");
    accountNumber = accountNumber.trim();
    acntNum = accountNumber +"%";
   }
   LOGGER.log(INFO, "acntNum used {0}", acntNum);
   List<BankAccountRec> retList = this.bankDM.getAccountsByActNumPartForBr(acntNum, br);
   LOGGER.log(INFO, " acnts returned from DB layer {0} ", retList);
   return retList;
   
  }
/**
 * 
 * @param account
 * @param usr
 * @param pg
 * @param validated
 * @return 
 */
  public BankAccountRec bankAccountUpdate(BankAccountRec account, UserRec usr, String pg, boolean validated) {
    LOGGER.log(INFO, "BankManager.createBankAccount act {0} user {1}",
            new Object[]{account,usr});
    
    if(validated){
      LOGGER.log(INFO, "Validated Bank account create");
      LOGGER.log(INFO, "Bank created by {0}", account.getAccountForBranch().getBank().getCreatedBy());
      LOGGER.log(INFO, "Bank branch created by {0}", account.getAccountForBranch().getCreatedBy());
      LOGGER.log(INFO, "Bank account created by {0}", account.getCreatedBy());
      BankAccountRec newBankAccount = bankDM.bankAccountValidatedUpdate(account,pg);
      LOGGER.log(INFO, "after Validated Bank account create BANK: {0}",newBankAccount);
      
      return newBankAccount;
    }else{
      BankAccountRec newBankAccount = this.bankDM.createBankAccount(account, pg);
    LOGGER.log(INFO, "After DB create account id is {0}", newBankAccount.getId());
    return newBankAccount;
    }
    
  }

 public boolean bankExists(String bnkCode){
  
  boolean bankExists = bankDM.bankExists(bnkCode);
  LOGGER.log(INFO, "bankExists {0}", bankExists);
  return bankExists;
 }

  
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

 public List<BankRec> getBanksByCode(String code) throws BacException {
  LOGGER.log(INFO, "Bank Mgr getBanksByCode called with {0}", code);
  List<BankRec> bankList = this.bankDM.getBanksByCode(code);
  LOGGER.log(INFO, "DB layer returns {0} banks",bankList.size());
  return bankList;
 }

 public List<BankBranchRec> getBranchesForBank(BankRec bank) throws BacException {
  LOGGER.log(INFO, "BankMgr getBranchesForBank called with  bank id {0}", bank.getId());
  List<BankBranchRec> brList = bankDM.getBranchesForBank(bank);
  LOGGER.log(INFO, "DB layer returns {0} branches",brList);
  if(brList == null || brList.isEmpty()){
   throw new BacException("No branches for bank","BNK:BR01");
  }
  return brList;
 }
 
 public BankAccountCompanyRec getChequeBooksForBankAcnt(BankAccountCompanyRec bnkAcnt){
  
  List<NumberRangeChequeRec> chqBks = configDm.getChequekBksForBnkAcnt(bnkAcnt);
  if(chqBks != null && !chqBks.isEmpty() ){
   Collections.sort(chqBks, new ChequeBookByShrtDescr());
   bnkAcnt.setChequeBooks(chqBks);
  }
  return bnkAcnt;
 }
 
 public List<DocBankLineChqRec> getChequesBySel(ChequeListSelOpt selOpt){
  LOGGER.log(INFO, "BankMgr.getChequesBySel called");
  List<DocBankLineChqRec> retList = bankDM.getChequeList(selOpt);
  LOGGER.log(INFO, "Cheques returned from DB layer {0}", retList);
  
  return retList;
 }
 
 
 
 
 
 public BankAccountCompanyRec createOwnBankAccount(BankAccountCompanyRec bankAc, UserRec usr, String page)
         throws BacException {
  LOGGER.log(INFO, "BankMgr createOwnBankAccount called with bank ac num {0} user id {1} page {2}", 
          new Object[]{bankAc.getAccountNumber(),usr,page});
  Date crDate = new Date();
  bankAc.setCreatedOn(crDate);
  bankAc.setCreatedBy(usr);
  bankAc = bankDM.updateBankAccountCompany(bankAc,  page);
  if(bankAc.getId() == null || bankAc.getId() == 0){
   throw new BacException("could not create own Bank Ac","BNK:BAC01");
  }
  return bankAc;
 }

 public boolean bankAccountForBranchExists(BankBranchRec branch, 
         BankAccountRec bankAccount) throws BacException {
  LOGGER.log(INFO, "bankMgr.bankAccountForBranchExists called with branch id {0} acnt num {1}",
          new Object[]{branch.getId(),bankAccount.getAccountNumber()});
  
  boolean rc = this.bankDM.bankAccountForBranchExists(branch, bankAccount);
  LOGGER.log(INFO, "TreasuryDM returns {0}",rc);
  return rc;
 }

 public BankAccountCompanyRec getBankAccountCompByNum(CompanyBasicRec comp,String sortCd, String accntNum){
  BankAccountCompanyRec ret = null;
  
  
  return ret;
 }
 public BankAccountRec  getBankAccountByNumSortCd(String sortCd, String accntNum){
  
   BankAccountRec bnkAc = this.bankDM.getBankAccountByNumSort(sortCd, accntNum);
   return bnkAc;
 }
 public BankAccountRec getBankAccountByNumber(String acntNum, Long branchId ){
  
  BankAccountRec bnkAc = this.bankDM.getBankAccountByNumber(acntNum, branchId);
  LOGGER.log(INFO, "Bank ac with id returned by TreasuryDM {0}",bnkAc);
  return bnkAc;
 }
 
 public BankAccountCompanyRec getBankAccountForCompany(String acntNum, CompanyBasicRec comp){
  BankAccountCompanyRec acntRec = bankDM.getBankAccountForCompany(acntNum, comp);
  LOGGER.log(INFO, "bankDM return account {0}", acntRec);
  return acntRec;
 }
 
 
 public List<BankAccountCompanyRec> getBankAccntsCompAll(){
  List<BankAccountCompanyRec> retList = this.bankDM.getBankAccountsCompAll();
  return retList;
 } 
 public List<BankAccountCompanyRec> getBankAccntsCompByName(CompanyBasicRec comp, String name){
  LOGGER.log(INFO, "getBankAccntsCompByName called with name {0}", name);
  // remove SQL wild cards and add one at back
  StringUtils.remove(name, "%");
  StringUtils.appendIfMissing(name, "%");
  LOGGER.log(INFO, "Account name search {0}", name);
  
  List<BankAccountCompanyRec> retList = bankDM.getBankAccntsCompByName(comp, name);
  
  return retList;
 }
 
 
 public List<BankAccountCompanyRec> getBankAccountsForCompany(CompanyBasicRec comp) throws BacException {
  LOGGER.log(INFO, "BankMgr.getBankAccountsForCompany called with comp id {0}", comp.getId());
  List<BankAccountCompanyRec> retList = this.bankDM.getBankAccountsForCompany(comp);
  LOGGER.log(INFO, "DB layer returns {0}",retList);
  return retList;
 }

 public List<BacsTransCodeRec> getBacsTransCodesByDirection(String dd_type) throws BacException {
  LOGGER.log(INFO, "getBacsTransCodesByDirection called with {0}", dd_type);
  List<BacsTransCodeRec> list = this.bankDM.getBacsTransCodesByDdType(dd_type);
  LOGGER.log(INFO, "TR dm return bacs codes {0}", list);
  return list;
 }

 public boolean paymentRunRefAvailable(CompanyBasicRec comp, String payRunRef) throws BacException {
  LOGGER.log(INFO, "BankMgr.paymentRunRefAvailable called with comp {0} ref {1} ", new Object[]{comp.getId(),payRunRef});
  boolean rc = bankDM.paymentRunRefAvailable(comp, payRunRef);
  LOGGER.log(INFO, "DB returned {0}",rc);
  return rc;
 }

 /**
  * Create of update BACS transaction code as appropriate
  * @param code
  * @param pg
  * @return If created the returned object has the newly allocated ID
  */
 public BacsTransCodeRec updateBacsTransCode(BacsTransCodeRec code, String pg){
  
  code = bankDM.updateBacsTransCode(code, pg);
  LOGGER.log(INFO, "BankMgr code id {0}", code.getId());
  return code;
  
 }
 
 public BankRec updateBank(BankRec b, String pg){
  b = bankDM.updateBank(b, pg);
  return b;
  
 }
 public BankBranchRec updateBankBranch(BankBranchRec br, String pg){
  
  br = this.bankDM.updateBankBranch(br, pg);
  return br;
 }
 
 public BankAccountRec updateBankAccount(BankAccountRec bnkAc, String pg){
   bnkAc = bankDM.updateBankAccount(bnkAc, pg);
  return bnkAc;
 }
 
 public BankAccountCompanyRec updateBankAccountCompany(BankAccountCompanyRec acnt, String pg){
  acnt = bankDM.updateBankAccountCompany(acnt, pg);
  return acnt;
 }
 
 public DocBankLineBaseRec updateDocBankLine(DocBankLineBaseRec bnkLnRec, DocLineBaseRec fiLnRec,
  PaymentTypeRec payTyRec, String Pg){
  LOGGER.log(INFO,"updateDocBankLine called {0}", payTyRec);
  
  bnkLnRec = this.bankDM.updateDocBankLine(bnkLnRec, fiLnRec, payTyRec, Pg);
  return bnkLnRec;
  
 }
 
 public List<DocBankLineChqRec> postDocBankLinesCheque(FiDoclSelectionOpt selOpts, String chqNumStart, 
   NumberRangeChequeRec chqBk, UserRec usrRec, String pg ){
  LOGGER.log(INFO, "postDocBankLineCheque called with opts {0}",selOpts );
  List<DocBankLineChqRec> retList = new ArrayList<>();
  //List<DocBankLineChqRec> retList = bankDM.postDocBankLinesCheque(selOpts,chqNumStart,chqBk,usrRec,pg);
  
  return retList;
  
 }
}
