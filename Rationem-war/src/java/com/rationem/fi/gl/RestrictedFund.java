/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.fi.gl;

import com.rationem.busRec.config.common.FundCategoryRec;
import java.util.Date;
import com.rationem.util.BaseBean;
import com.rationem.ejbBean.config.company.CompanyManager;
import javax.ejb.EJB;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.ejbBean.config.common.BasicSetup;
import com.rationem.util.MessageUtil;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;


/**
 * Controller for Restricted Fund management
 * @author Chris
 */
public class RestrictedFund extends BaseBean {

  @EJB
  private CompanyManager compMgr;
  
  @EJB
  private BasicSetup setup;
  
  
  private CompanyBasicRec currComp;
  private FundRec fund;
  private List<FundRec> funds;
  private List<FundCategoryRec> fndCategories;


private static final Logger logger =
            Logger.getLogger("accounts.fi.gl.GlAccountBean");

    /** Creates a new instance of RestrictedFund */
    public RestrictedFund() {
    }

@PostConstruct
private void init(){
 currComp = this.getCompList().get(0);
 funds = this.compMgr.getFundsForComp(currComp);
 if(funds != null && !funds.isEmpty()){
  fund = funds.get(0);
 }else{
  MessageUtil.addWarnMessage("restrFndNone", "validationText");
 }

 
}  

 public CompanyBasicRec getCurrComp() {
  return currComp;
 }

 public void setCurrComp(CompanyBasicRec currComp) {
  this.currComp = currComp;
 }

  

  public FundRec getFund() {
    if(fund == null){
      fund = new FundRec();
    }
    return fund;
  }

  public void setFund(FundRec fund) {
    this.fund = fund;
  }

 public List<FundCategoryRec> getFndCategories() {
  if(fndCategories == null){
   fndCategories = this.setup.getFundCategoriesAll();
  }
  return fndCategories;
 }

 public void setFndCategories(List<FundCategoryRec> fndCategories) {
  this.fndCategories = fndCategories;
 }

 public List<FundRec> getFunds() {
  
  return funds;
 }

 public void setFunds(List<FundRec> funds) {
  this.funds = funds;
 }
  

  public void saveNewFund(){
    logger.log(INFO, "Save fund called");
    fund.setCreatedBy(this.getLoggedInUser());
    logger.log(INFO, "Fund created by in web SaveFund {0}", fund.getCreatedBy());
    fund.setCreatedOn(new Date());
    if(fund.getFundForComp() == null){
     fund.setFundForComp(this.getCompList().get(0));
    }
    compMgr.addRestFundToComp( fund,getView());
    MessageUtil.addInfoMessage("restrFndCr", "blacResponse");
  }
  
  public void onEditStart(SelectEvent evt){
   Date minDate = (Date)evt.getObject();
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("fndEndDt");
   pf.ajax().update("fndRetDt");
  }
  
  public void onEditValidFr(SelectEvent evt){
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("fndEndDt");
   pf.ajax().update("fndRetDt");
  }
  
  public void onFundChange(ValueChangeEvent evt){
   fund = (FundRec)evt.getNewValue();
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("restrFndUpdtFrm");
   
  }
  public void onReturnRequiredChange(ValueChangeEvent evt){
   logger.log(INFO, "Return required {0}", evt.getNewValue());
   fund.setReturnRequired((Boolean)evt.getNewValue());
   PrimeFaces pf = PrimeFaces.current();
   pf.ajax().update("restrFndUpdtFrm:fndRetDt");
  }
  
  public void onSaveUpdate(){
   fund.setChangedBy(this.getLoggedInUser());
   fund.setChangedOn(new Date());
   try{
    fund = this.compMgr.updateRestrictedFund(fund, getView());
    MessageUtil.addInfoMessage("restrFndUpdt", "blacResponse");
   }catch(Exception ex){
    MessageUtil.addErrorMessage("restrFndUpdt", "errorText");
   }
  }

}
