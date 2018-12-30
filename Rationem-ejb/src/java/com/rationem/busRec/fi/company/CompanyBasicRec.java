/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.company;

import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.config.company.LedgerRec;
import com.rationem.busRec.fi.glAccount.FiGlAccountCompRec;
import com.rationem.busRec.mdm.AddressRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.tr.BankBranchRec;
import com.rationem.busRec.user.UserRec;
import com.rationem.busRec.salesTax.vat.VatRegistrationRec;
import com.rationem.busRec.tr.BankAccountCompanyRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
/**
 * Central Company details linked to Chart of accounts.
 * Each company has a linked record
 * @author Chris
 */
public class CompanyBasicRec implements Serializable{
   private static final Logger logger =  Logger.getLogger(CompanyBasicRec.class.getName());
    private Long id;
    private String reference;
    private String name;
    private String legalName;
    
    /**
     * This should be Sole trader, partnership or incorporated
     */
    private String companyType;
    private String companyNumber;
    
    private String charityNumber;
    private boolean defaultCompany;
    private boolean vatReg;
    private boolean corp;
    private boolean charity;
    private VatRegistrationRec vatRegDetails;
    private String vatNumber;
    private Date incorporatedDate;
    private Date charityRegDate;
    private CompanyApArRec compApAr;
    private ChartOfAccountsRec chartOfAccounts;
    private AddressRec address;
    private AddressRec registeredAddress;
    private String registeredAddressFormatted;
    private List<AccountTypeRec> acTypes;
    private List<LedgerRec> ledgers;
    private FisPeriodRuleRec periodRule;
    private List<PeriodControlRec> postingPeriods;
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private long changes;
    private boolean restrictedFunds;
    private boolean fundAccounting;
    private boolean businessUnits;
    private CurrencyRec currency;
    private CountryRec country;
    private byte[] logoImg;
    private String logoFname;
    private String logoFtype;
    private boolean logo;
    private Locale locale;
    
    private List<FundRec> fundList;
    private List<String> businessUnitList;
    private List<FiGlAccountCompRec> glAccounts;
    private List<VatRegistrationRec> vatRegistrations;
    private List<BankAccountCompanyRec> bankAccounts;
    //private List<BankBranchRec> banks;
    private List<CompPostPerRec> companyPostingPeriods;
    
    
    




    public CompanyBasicRec() {
    }

    public CompanyBasicRec(Long id, String reference, String name, String legalName, String companyType,
            String companyNumber, boolean vatReg, String vatNumber, Date incorporatedDate,
            ChartOfAccountsRec chartOfAccounts, AddressRec address, AddressRec registeredAddress,
            UserRec createdBy, Date createdDate, UserRec changedBy, Date changedDate, long changes) {
        this.id = id;
        this.reference = reference;
        this.name = name;
        this.legalName = legalName;
        this.companyType = companyType;
        this.companyNumber = companyNumber;
        this.vatReg = vatReg;
        this.vatNumber = vatNumber;
        this.incorporatedDate = incorporatedDate;
        this.chartOfAccounts = chartOfAccounts;
        this.address = address;
        this.registeredAddress = registeredAddress;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.changes = changes;
    }

    public AddressRec getAddress() {
        if(address == null){
            address = new AddressRec();
        }
        return address;
    }

    public void setAddress(AddressRec address) {
        this.address = address;
    }

 public List<CompPostPerRec> getCompanyPostingPeriods() {
  return companyPostingPeriods;
 }

 public void setCompanyPostingPeriods(List<CompPostPerRec> companyPostingPeriods) {
  this.companyPostingPeriods = companyPostingPeriods;
 }

 
    public List<BankAccountCompanyRec> getBankAccounts() {
        return bankAccounts;
    }

 /*
 public List<BankBranchRec> getBanks() {
 return banks;
 }
 public void setBanks(List<BankBranchRec> banks) {
 this.banks = banks;
 }
  */
 public void setBankAccounts(List<BankAccountCompanyRec> bankAccounts) {
  this.bankAccounts = bankAccounts;
 }

 public List<String> getBusinessUnitList() {
  return businessUnitList;
 }

    public void setBusinessUnitList(ArrayList<String> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public boolean isBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(boolean businessUnits) {
        this.businessUnits = businessUnits;
    }

 public boolean isCharity() {
  return charity;
 }

 public void setCharity(boolean charity) {
  this.charity = charity;
 }

 public String getCharityNumber() {
  return charityNumber;
 }

 public void setCharityNumber(String charityNumber) {
  this.charityNumber = charityNumber;
 }

 public Date getCharityRegDate() {
  return charityRegDate;
 }

 public void setCharityRegDate(Date charityRegDate) {
  this.charityRegDate = charityRegDate;
 }

    public UserRec getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public long getChanges() {
        return changes;
    }

    public void setChanges(long changes) {
        this.changes = changes;
    }

    public ChartOfAccountsRec getChartOfAccounts() {
      if(chartOfAccounts == null){
        chartOfAccounts = new ChartOfAccountsRec();
      }
        return chartOfAccounts;
    }

    public void setChartOfAccounts(ChartOfAccountsRec chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

 public CompanyApArRec getCompApAr() {
  return compApAr;
 }

 public void setCompApAr(CompanyApArRec compApAr) {
  this.compApAr = compApAr;
 }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

 
 
    public String getCompanyType() {
      if(companyType == null){
        companyType = new String();
      }
        return companyType;
    }

    public void setCompanyType(String companyType) {
      
        this.companyType = companyType;
    }

 public boolean isCorp() {
  return corp;
 }

 public void setCorp(boolean corp) {
  this.corp = corp;
 }

 public CountryRec getCountry() {
  return country;
 }

 public void setCountry(CountryRec country) {
  this.country = country;
 }

 
 public CurrencyRec getCurrency() {
  return currency;
 }

 public void setCurrency(CurrencyRec currency) {
  this.currency = currency;
 }

 public String getCurrencySymbol() {
  
  return currency.getCurrSymbol();
 }

 

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

 public boolean isFundAccounting() {
  return fundAccounting;
 }

 public void setFundAccounting(boolean fundAccounting) {
  this.fundAccounting = fundAccounting;
 }

    public List<FundRec> getFundList() {
      if(fundList == null){
        fundList = new ArrayList<>();
      }
        return fundList;
    }

    public void setFundList(List<FundRec> fundList) {
        this.fundList = fundList;
    }

  public List<FiGlAccountCompRec> getGlAccounts() {
    if(glAccounts == null) {
      glAccounts = new ArrayList<>();
    }
    return glAccounts;
  }

  public void setGlAccounts(List<FiGlAccountCompRec> glAccounts) {
    this.glAccounts = glAccounts;
  }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getIncorporatedDate() {
        return incorporatedDate;
    }

    public void setIncorporatedDate(Date incorporatedDate) {
        this.incorporatedDate = incorporatedDate;
    }

 public String getLogoFname() {
  return logoFname;
 }

 public void setLogoFname(String logoFname) {
  this.logoFname = logoFname;
 }

 public String getLogoFtype() {
  return logoFtype;
 }

 public void setLogoFtype(String logoFtype) {
  this.logoFtype = logoFtype;
 }

 public byte[] getLogoImg() {
  return logoImg;
 }

 public void setLogoImg(byte[] logoImg) {
  this.logoImg = logoImg;
 }

 public boolean isIcon() {
  return logo;
 }

 public Locale getLocale() {
  return locale;
 }

 public void setLocale(Locale locale) {
  this.locale = locale;
 }

 public void setLogo(boolean logo) {
  this.logo = logo;
 }

  public List<AccountTypeRec> getAcTypes() {
    return acTypes;
  }

  public void setAcTypes(List<AccountTypeRec> acTypes) {
    this.acTypes = acTypes;
  }

  public boolean isDefaultCompany() {
    return defaultCompany;
  }

  public void setDefaultCompany(boolean defaultCompany) {
    this.defaultCompany = defaultCompany;
  }

  public List<LedgerRec> getLedgers() {
    if(ledgers == null){
      ledgers = new ArrayList<>();
    }
    return ledgers;
  }

  public void setLedgers(List<LedgerRec> ledgers) {
    this.ledgers = ledgers;
  }

  public FisPeriodRuleRec getPeriodRule() {
    if(periodRule == null){
      periodRule = new FisPeriodRuleRec();
    }
    return periodRule;
  }

  public void setPeriodRule(FisPeriodRuleRec periodRule) {
    this.periodRule = periodRule;
  }

  public List<PeriodControlRec> getPostingPeriods() {
    return postingPeriods;
  }

  public void setPostingPeriods(List<PeriodControlRec> postingPeriods) {
    this.postingPeriods = postingPeriods;
  }

    

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

 public String getRegisteredAddressFormatted() {
  String ret = new String();
  if(registeredAddress != null){
   StringBuilder retStr = new StringBuilder();
   
   if(registeredAddress.getTown() != null){
    retStr.append(registeredAddress.getTown());
   }
   if(registeredAddress.getCountyName() != null){
    retStr.append(registeredAddress.getCountyName());
   }
   if(registeredAddress.getPostCode() != null){
    retStr.append(registeredAddress.getPostCode());
   }
   if(retStr.length() > 0){
    ret = retStr.toString();
   }
  }
  return ret;
 }

    
    public AddressRec getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(AddressRec registeredAddress) {
        this.registeredAddress = registeredAddress;
    }
    
    

    public boolean isRestrictedFunds() {
        return restrictedFunds;
    }

    public void setRestrictedFunds(boolean restrictedFunds) {
        this.restrictedFunds = restrictedFunds;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public boolean isVatReg() {
        return vatReg;
    }

    public void setVatReg(boolean vatReg) {
        this.vatReg = vatReg;
    }

 public VatRegistrationRec getVatRegDetails() {
  if(vatRegDetails == null){
   vatRegDetails = new VatRegistrationRec(); 
  }
  return vatRegDetails;
 }

 public void setVatRegDetails(VatRegistrationRec vatRegDetails) {
  this.vatRegDetails = vatRegDetails;
 }

 public List<VatRegistrationRec> getVatRegistrations() {
  return vatRegistrations;
 }

 public void setVatRegistrations(List<VatRegistrationRec> vatRegistrations) {
  this.vatRegistrations = vatRegistrations;
 }

 
 @Override
 public boolean equals(Object obj) {
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  final CompanyBasicRec other = (CompanyBasicRec) obj;
  if (!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id))) {
   return false;
  }
  return true;
 }

 @Override
 public int hashCode() {
  int hash = 7;
  hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
  return hash;
 }

    






}
