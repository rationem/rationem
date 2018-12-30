/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.util;
import com.rationem.busRec.partner.PartnerCorporateRec;
import com.rationem.busRec.tr.BankRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.tr.BankAccountRec;
import com.rationem.ejbBean.tr.BankManager;
import com.rationem.exception.BacException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import uk.co.unifiedsoftware.bankvaluk.WebMethods;


import static java.util.logging.Level.INFO;
import javax.ejb.EJB;


/**
 *
 * @author Chris
 */
public class BankValidation {
  private static final Logger logger =
            Logger.getLogger("com.rationem.util.BankValidation");

  @EJB
  private BankManager bnkMgr;
  private WebMethods bv = new WebMethods();
  private String validBank = "600706|79716001|600704|NWBKGB21|05T|00|DOVER|NAT WEST BANK PLC|NATIONAL WESTMINSTER BANK PLC||0006||A||23/09/2008|0|M|06/06/2005||||0006|01|00|0006|01|Y|N|||||||||||||I|09/07/2001||006|NWBKGB55|XXX|N||23/09/2008|||||||M|12/10/1983||6|||GB|A|23/02/2008||||01|00|0010|01|00|0010|Y|I||M|||Dover|Dover|1||Dover|||Chatham Customer Service Centre|Western Avenue|Waterside|Chatham Maritime|Chatham|Kent|ME4|4RT|0870|2403355||||#]";
  private String bankAcError = "INVALID - Modulus Check Failed";

    /** Creates a new instance of BankValidation */
    public BankValidation() {
    }

    
   /**
    * Return the bank account details found - error are returned in the exception
    * @param sortCode
    * @param AccountNumber
    * @return
    * @throws BacException
    */
     public BankAccountRec validateBank(String sortCode, String AccountNumber) 
       throws BacException{
       logger.log(INFO, "validateBank called with sort code {0} account number {1} name {2}",
               new Object[] {sortCode,AccountNumber});
       BankAccountRec bankAc;
      // GET https://www.unifiedservices.co.uk/services/bankvaluk/bankvalplus2/userid/<userid>/pin/<pin>/sortcode/<sortcode>/account/<account>/<format>/
       /// test string
       //String result = bv.bankValPlus2("Hunt551", "20205", "600704", "79715001", "csv");
       //logger.log(INFO, "Result from BV test {0}", result);
       //String result = "INVALID - Modulus Check Failed";
       String result ="VALID|600704|79715001|600704|NWBKGB21|05T|00|DOVER|NAT WEST BANK PLC|NATIONAL WESTMINSTER BANK PLC||0006||A||11/03/2013|0|M|06/06/2005||||0006|01|00|0006|01|Y|N|||||||||||||I|09/07/2001||006|NWBKGB55|XXX|N||23/09/2008|||||||M|12/10/1983||6|||GB|A|23/02/2008||||01|00|0010|01|00|0010|Y|I||M|||Dover|Dover|1||Dover|||Chatham Customer Service Centre|Western Avenue|Waterside Court|Chatham Maritime|Chatham|Kent|ME4|4RT|0870|2403355||||#]";
       
       boolean validationFailed = result.startsWith("INVALID -");
       if(validationFailed ){
         throw new BacException(result);
       }
       logger.log(INFO, "result {0}", result);
       result = result.replace('|', ':');
       logger.log(INFO, "result after replace  {0}", result);
       String[] bankResult = result.split(":");

       logger.log(INFO,"BankResult elements1: {0}",bankResult.length);
       if(bankResult != null){
         logger.log(INFO,"BankResult elements: {0}",bankResult.length);
         int numElements = bankResult.length;
         for(int i=0; i <numElements;i++){
           logger.log(INFO,"Element: {0} value {1}",
                   new Object[]{i,bankResult[i]});
           //Build bank
           BankRec bank = new BankRec();
           bank.setBankCode(bankResult[4]);
           bank.setChapsBankCode(bankResult[45]);
           PartnerCorporateRec bankOrg = bank.getBankOrganisation();
           if(bankOrg == null){
            bankOrg = new PartnerCorporateRec();
           }
           bankOrg.setTradingName(bankResult[8]);
           bankOrg.setLegalName(bankResult[9]);
           bank.setBankOrganisation(bankOrg);
           
           //build branch & add to bank
           BankBranchRec branch = new BankBranchRec();
           branch.setSortCode(bankResult[1]);
           branch.setBranchName(bankResult[7]);
           
           AddressRec branchAddr = branch.getBranchAddress();
           if(branchAddr == null){
            branchAddr = new AddressRec();
           }
           branchAddr.setAddrRef(bankResult[87]);
           if(!bankResult[90].isEmpty()){
            // 3 address lines
            branchAddr.setBuilding(bankResult[87]);
            branchAddr.setStreet(bankResult[88]);
            branchAddr.setStreet2(bankResult[89]);
            }else{
            branchAddr.setStreet(bankResult[87]);
            branchAddr.setStreet2(bankResult[88]);
           }
           
           branchAddr.setTown(bankResult[91]);
           branchAddr.setCountyName(bankResult[92]);
           
           branchAddr.setPostCode(bankResult[93]+" "+bankResult[94]);
           branch.setBranchAddress(branchAddr);
           branch.setPhoneArea(bankResult[95]);
           branch.setPhoneNumber(bankResult[96]);
           
           branch.setBankOrg(bank);
           List<BankBranchRec> bankBranches = bank.getBankBranches();
           if(bankBranches == null){
            bankBranches = new ArrayList<>();
           }
           bankBranches.add(branch);
           // build account and add to branch
           
           
           bankAc = new BankAccountRec();
           bankAc.setAccountNumber(bankResult[2]);
          // bankAc.setAccountName(actName);
           
            
           String fpFlg = bankResult[63];
           if(!fpFlg.isEmpty() && fpFlg.equalsIgnoreCase("A")){
            bankAc.setFasterPayments(true);
           }else{
            bankAc.setFasterPayments(false);
           }
           
           String cr = bankResult[30];
           if(cr != null && !cr.isEmpty()){
             bankAc.setDirectCreditAllowed(false);
           }else{
            bankAc.setDirectCreditAllowed(true);
           }
           String ddFlag = bankResult[29];
           if(ddFlag != null && !ddFlag.isEmpty()){
             bankAc.setDirectDebitsAllowed(false);
           }else{
            bankAc.setDirectDebitsAllowed(true);
           }
           bankAc.setAccountForBranch(branch);
           List<BankAccountRec> branchAcs = branch.getAccounts();
           if(branchAcs == null){
            branchAcs = new ArrayList<BankAccountRec>();
           }
           branchAcs.add(bankAc);
           
           logger.log(INFO, "Validated account returns bank code {0} bank name {1}", 
                   new Object[]{bankAc.getAccountForBranch().getBank().getBankCode(),bankAc.getAccountForBranch().getBank().getBankOrganisation().getTradingName()});
           logger.log(INFO, "Validated account returns branch code {0} sort code {1}", 
                   new Object[]{bankAc.getAccountForBranch().getBranchName(),bankAc.getAccountForBranch().getSortCode()});
           logger.log(INFO, "Validated account returns accouny number {0} fasterPayments {1} name {2}", 
                   new Object[]{bankAc.getAccountNumber(),bankAc.isFasterPayments(),bankAc.getAccountName()});
           return bankAc;

           
         }

       }
      // 600704|79715001|600704|NWBKGB21|05T|00|DOVER|NAT WEST BANK PLC|NATIONAL WESTMINSTER BANK PLC||0006||A||23/09/2008|0|M|06/06/2005||||0006|01|00|0006|01|Y|N|||||||||||||I|09/07/2001||006|NWBKGB55|XXX|N||23/09/2008|||||||M|12/10/1983||6|||GB|A|23/02/2008||||01|00|0010|01|00|0010|Y|I||M|||Dover|Dover|1||Dover|||Chatham Customer Service Centre|Western Avenue|Waterside|Chatham Maritime|Chatham|Kent|ME4|4RT|0870|2403355||||#]";

      return null;

    }


    

}
