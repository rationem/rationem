/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.fi.company;

import com.rationem.busRec.config.company.FisPeriodRuleRec;
import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.mdm.CurrencyRec;
import com.rationem.busRec.user.UserRec;
import java.util.Date;
import java.io.Serializable;
import java.util.List;


import java.util.logging.Logger;


/**
 * A chart of accounts holds the processing rules for a set of accounts.
 * A company must have exactly 1 chart of accounts
 * This object is a Business version of the DB entity
 * @author Chris
 */
public class ChartOfAccountsRec implements Serializable {
 private static final Logger LOGGER = Logger.getLogger(ChartOfAccountsRec.class.getName());

 private Long id;
 private String reference;
    private String name;
    private CurrencyRec chartCurrency;
    private CountryRec chartCountry;
    
    private boolean defaultChart;
    private List<CompanyBasicRec> companies;
    private FisPeriodRuleRec periodRule;
    private UserRec createdBy;
    private Date createdDate;
    private UserRec changedBy;
    private Date changedDate;
    private int changes;
    private boolean OibalFwd;

    public ChartOfAccountsRec() {
   }

    

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDefaultChart() {
        return defaultChart;
    }

    public void setDefaultChart(boolean defaultChart) {
        this.defaultChart = defaultChart;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public FisPeriodRuleRec getPeriodRule() {
        return periodRule;
    }

    public void setPeriodRule(FisPeriodRuleRec periodRule) {
        this.periodRule = periodRule;
    }

    

    public UserRec getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserRec changedBy) {
        this.changedBy = changedBy;
    }

    public UserRec getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRec createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isOibalFwd() {
        return OibalFwd;
    }

    public void setOibalFwd(boolean OibalFwd) {
        this.OibalFwd = OibalFwd;
    }

 public CurrencyRec getChartCurrency() {
  return chartCurrency;
 }

 public void setChartCurrency(CurrencyRec chartCurrency) {
  this.chartCurrency = chartCurrency;
 }

 public CountryRec getChartCountry() {
  return chartCountry;
 }

 public void setChartCountry(CountryRec chartCountry) {
  this.chartCountry = chartCountry;
 }
  
  
  public List<CompanyBasicRec> getCompanies() {
    return companies;
  }

  public void setCompanies(List<CompanyBasicRec> companies) {
    this.companies = companies;
  }



    




}
