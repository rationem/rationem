/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.busRec.user;

import com.rationem.busRec.mdm.CountryRec;
import com.rationem.busRec.partner.PartnerPersonRec;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Chris
 */
public class UserRec extends PartnerPersonRec implements Serializable{

    UserLoginRec loginDetails;

    private Date startDate;

    private Date endDate;
    private boolean systemUser;
    private ArrayList<UserLoginRecOld> userLogins;
    private CountryRec country;


    public UserRec(String firstName, String middleName, String familyName) {
    }

    public UserRec(int createdBy, Date createdDate, String firstName, String middleName,
            String familyName) {
    }

    public UserRec() {
        
    }

 public CountryRec getCountry() {
  return country;
 }

 public void setCountry(CountryRec country) {
  this.country = country;
 }
    
    

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public UserLoginRec getLoginDetails() {
        if(loginDetails == null){
           loginDetails = new UserLoginRec();
        }
        return loginDetails;
    }

    public void setLoginDetails(UserLoginRec loginDetails) {
        this.loginDetails = loginDetails;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
    }

    public ArrayList<UserLoginRecOld> getUserLogins() {
        return userLogins;
    }

    public void setUserLogins(ArrayList<UserLoginRecOld> userLogins) {
        this.userLogins = userLogins;
    }

    
    



}
