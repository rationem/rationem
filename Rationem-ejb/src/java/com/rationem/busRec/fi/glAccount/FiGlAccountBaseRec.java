/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.glAccount;

//import com.rationem.busRec.config.fi.FiGlActTypeRec;
import com.rationem.busRec.config.company.AccountTypeRec;
import com.rationem.busRec.fi.company.ChartOfAccountsRec;
import com.rationem.busRec.user.UserRec;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * Root of GL account hierarchy
 * @author Chris
 */
public class FiGlAccountBaseRec implements Serializable {
  private static final Logger LOGGER = Logger.getLogger(FiGlAccountBaseRec.class.getName());

    private Long id; 

    private ChartOfAccountsRec chartOfAccounts;
    private String ref;
    private String name;
    private String description;
    private boolean pl;
    private AccountTypeRec accountType;
    private String reportCat;
    private UserRec createdBy;
    private Date createdOn;
    private Date updateOn;
    private UserRec updateBy;
    private int revision;
    private List<FiGlAccountCompRec> companyAccounts;

  public FiGlAccountBaseRec(Long id, ChartOfAccountsRec chartOfAccounts, String ref, String name,
          String description, boolean pl, AccountTypeRec accountType, Date createdOn, Date updateDate,
          UserRec updateBy, int revision) {
    this.id = id;
    this.chartOfAccounts = chartOfAccounts;
    this.ref = ref;
    this.name = name;
    this.description = description;
    this.pl = pl;
    this.accountType = accountType;
    this.createdOn = createdOn;
    this.updateOn = updateDate;
    this.updateBy = updateBy;
    this.revision = revision;
  }

    

    public FiGlAccountBaseRec() {
    }

    public ChartOfAccountsRec getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(ChartOfAccountsRec chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date created) {
        this.createdOn = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
       return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

 public String getReportCat() {
  return reportCat;
 }

 public void setReportCat(String reportCat) {
  this.reportCat = reportCat;
 }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public UserRec getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(UserRec updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

  public UserRec getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(UserRec createdBy) {
    this.createdBy = createdBy;
  }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

  public AccountTypeRec getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeRec accountType) {
    this.accountType = accountType;
  }

  public boolean isPl() {
    
    return pl;
  }

  public void setPl(boolean pl) {
    this.pl = pl;
  }

  public List<FiGlAccountCompRec> getCompanyAccounts() {
    return companyAccounts;
  }

  public void setCompanyAccounts(List<FiGlAccountCompRec> companyAccounts) {
    this.companyAccounts = companyAccounts;
  }

  




}
