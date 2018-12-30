/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.busRec.doc;
import com.rationem.busRec.fi.arap.ApAccountRec;
import com.rationem.busRec.fi.arap.ArAccountRec;
import com.rationem.busRec.fi.arap.ArBankAccountRec;
import com.rationem.busRec.fi.company.CompanyBasicRec;
import com.rationem.busRec.fi.company.FundRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import com.rationem.busRec.tr.BankStatementRec;
import com.rationem.busRec.tr.BnkPaymentRunRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Chris
 */
public class DocBankLineBaseRec {
 private Long id;
 private int lineNum;
 private CompanyBasicRec comp;
 private BnkPaymentRunRec payRun;
 private ApAccountRec apAccount;
 private ArAccountRec arAccount;
 private FiGlAccountCompRec glAccountComp;
 private BankAccountCompanyRec unClearedBankAc;
 private BankAccountCompanyRec clearedBankAc;
 private BankStatementRec bnkStament;
 private Date docDate;
 private Date postDate;
 private boolean cleared;
 private Date clearedDate;
 private Date valueDate;
 private String bnkRef;
 private String bankTransCode;
 /*private String bnkSortCode;
 private String bnkAccount;
 private BacsTransCodeRec bacsTransCode;*/
 private List<DocLineGlRec> glLines;
 private DocLineBaseRec docFiLine;
 private List<DocLineArRec> arPymntLines;
 private FundRec restrictedFund;
 private ArBankAccountRec arBank;
 private double amount;
 private String amountStr;
 private boolean receipt;
 private UserRec createdBy;
 private Date createdOn;
 private UserRec changedBy;
 private Date changedOn;
 private int changes;

 public DocBankLineBaseRec() {

 }

 
 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getAmountStr() {
  return amountStr;
 }

 public void setAmountStr(String amountStr) {
  this.amountStr = amountStr;
 }

 public List<DocLineArRec> getArPymntLines() {
  return arPymntLines;
 }

 public void setArPymntLines(List<DocLineArRec> arPymntLines) {
  this.arPymntLines = arPymntLines;
 }

 public ApAccountRec getApAccount() {
  return apAccount;
 }

 public void setApAccount(ApAccountRec apAccount) {
  this.apAccount = apAccount;
 }

 
 public ArAccountRec getArAccount() {
  return arAccount;
 }

 public void setArAccount(ArAccountRec arAccount) {
  this.arAccount = arAccount;
 }

 public ArBankAccountRec getArBank() {
  return arBank;
 }

 public void setArBank(ArBankAccountRec arBank) {
  this.arBank = arBank;
 }

 
 public CompanyBasicRec getComp() {
  return comp;
 }

 public void setComp(CompanyBasicRec comp) {
  this.comp = comp;
 }

 public BankAccountCompanyRec getUnClearedBankAc() {
  return unClearedBankAc;
 }

 public void setUnClearedBankAc(BankAccountCompanyRec unClearedBankAc) {
  this.unClearedBankAc = unClearedBankAc;
 }

 public boolean isCleared() {
  return cleared;
 }

 public void setCleared(boolean cleared) {
  this.cleared = cleared;
 }

 
 public BankAccountCompanyRec getClearedBankAc() {
  return clearedBankAc;
 }

 public void setClearedBankAc(BankAccountCompanyRec clearedBankAc) {
  this.clearedBankAc = clearedBankAc;
 }

 public BankStatementRec getBnkStament() {
  return bnkStament;
 }

 public void setBnkStament(BankStatementRec bnkStament) {
  this.bnkStament = bnkStament;
 }
 

 public Date getDocDate() {
  return docDate;
 }

 public void setDocDate(Date docDate) {
  this.docDate = docDate;
 }

 public int getLineNum() {
  return lineNum;
 }

 public void setLineNum(int lineNum) {
  this.lineNum = lineNum;
 }

 public DocLineBaseRec getDocFiLine() {
  return docFiLine;
 }

 public void setDocFiLine(DocLineBaseRec docFiLine) {
  this.docFiLine = docFiLine;
 }

 
 public BnkPaymentRunRec getPayRun() {
  return payRun;
 }

 public void setPayRun(BnkPaymentRunRec payRun) {
  this.payRun = payRun;
 }

 public Date getPostDate() {
  return postDate;
 }

 public void setPostDate(Date postDate) {
  this.postDate = postDate;
 }

 public Date getClearedDate() {
  return clearedDate;
 }

 public void setClearedDate(Date clearedDate) {
  this.clearedDate = clearedDate;
 }

 public Date getValueDate() {
  return valueDate;
 }

 public void setValueDate(Date valueDate) {
  this.valueDate = valueDate;
 }

 public String getBankTransCode() {
  return bankTransCode;
 }

 public void setBankTransCode(String bankTransCode) {
  this.bankTransCode = bankTransCode;
 }

 public String getBnkRef() {
  return bnkRef;
 }

 public void setBnkRef(String bnkRef) {
  this.bnkRef = bnkRef;
 }

 /*
 public String getBankTransCode() {
 return bankTransCode;
 }
 public void setBankTransCode(String bankTransCode) {
 this.bankTransCode = bankTransCode;
 }
 public BacsTransCodeRec getBacsTransCode() {
 return bacsTransCode;
 }
 public void setBacsTransCode(BacsTransCodeRec bacsTransCode) {
 this.bacsTransCode = bacsTransCode;
 }
 public String getBnkSortCode() {
 return bnkSortCode;
 }
 public void setBnkSortCode(String bnkSortCode) {
 this.bnkSortCode = bnkSortCode;
 }
 public String getBnkAccount() {
 return bnkAccount;
 }
 public void setBnkAccount(String bnkAccount) {
 this.bnkAccount = bnkAccount;
 }
  */
 public FiGlAccountCompRec getGlAccountComp() {
  return glAccountComp;
 }

 public void setGlAccountComp(FiGlAccountCompRec glAccountComp) {
  this.glAccountComp = glAccountComp;
 }
 
 public List<DocLineGlRec> getGlLines() {
  return glLines;
 }

 public void setGlLines(List<DocLineGlRec> glLines) {
  this.glLines = glLines;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public boolean isReceipt() {
  return receipt;
 }

 public void setReceipt(boolean receipt) {
  this.receipt = receipt;
 }
/**
 * Restricted fund is only populated for the bank account line 
 * @return 
 */
 public FundRec getRestrictedFund() {
  return restrictedFund;
 }

 public void setRestrictedFund(FundRec restrictedFund) {
  this.restrictedFund = restrictedFund;
 }

 public UserRec getCreatedBy() {
  return createdBy;
 }

 public void setCreatedBy(UserRec createdBy) {
  this.createdBy = createdBy;
 }

 public Date getCreatedOn() {
  return createdOn;
 }

 public void setCreatedOn(Date createdOn) {
  this.createdOn = createdOn;
 }

 public UserRec getChangedBy() {
  return changedBy;
 }

 public void setChangedBy(UserRec changedBy) {
  this.changedBy = changedBy;
 }

 public Date getChangedOn() {
  return changedOn;
 }

 public void setChangedOn(Date changedOn) {
  this.changedOn = changedOn;
 }

 public int getChanges() {
  return changes;
 }

 public void setChanges(int changes) {
  this.changes = changes;
 }

 @Override
 public int hashCode() {
  int hash = 5;
  hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final DocBankLineBaseRec other = (DocBankLineBaseRec) obj;
  if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public String toString() {
  return "DocBankLineBaseRec{" + "id=" + id + '}';
 }
 
 
 
}
