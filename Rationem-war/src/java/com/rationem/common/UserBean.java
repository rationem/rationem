/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rationem.common;


import java.io.Serializable;
import javax.faces.event.ActionEvent;
import com.rationem.util.GenUtil;
import com.rationem.busRec.user.UserRec;
import com.rationem.ejbBean.manager.UserManager;
import com.rationem.exception.BacException;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import static java.util.logging.Level.INFO;
import javax.faces.context.ExternalContext;


/**
 *
 * @author Chris
 */
public class UserBean implements Serializable {

    @EJB
    private UserManager usrMgr;

    private UserRec user;
    private String password2;
    private String password;


    private static final Logger logger =
            Logger.getLogger("IAS.beans.common.UserBean");

    /** Creates a new instance of UserBean */
    public UserBean() {
    }

    public UserRec getUser() {
        if(user == null){
            try{
             ExternalContext ecntx = FacesContext.getCurrentInstance().getExternalContext();
                user = usrMgr.getUser(ecntx.getRemoteUser());

            }catch(BacException e){
              GenUtil.addErrorMessage("Could not find user");
            }
        }
        return user;
    }

    public void setUser(UserRec user) {
        this.user = user;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void pwEntryValueChange(ValueChangeEvent e){
        logger.log(INFO, "Password  entered with {0}", e.getNewValue());
        password = (String)e.getNewValue();
        if(password.isEmpty()){
            GenUtil.addErrorMessage("You cannot use a blank password");
            return;
        }

    }
    public void pwRentryValueChange(ValueChangeEvent e){
        logger.log(INFO, "Password 2 entered with {0}", e.getNewValue());
        this.password2 = (String)e.getNewValue();
        logger.log(INFO, "Password entered {0}", password);
        if(password2.isEmpty()){
            
            return;
        }
        logger.log(INFO, "1st password: {0}", password);

        if(!password2.equals(password)){
            GenUtil.addErrorMessage("Passwords do not match please reenter");
            logger.log(INFO, "Passwords do not match");
        }
    }

    public void validatePassword(FacesContext c, UIComponent comp, Object val){
        logger.log(INFO, "validatePassword value {0}", val);
        String input = (String)val;
        if(this.password == null || this.password.isEmpty()){
            ((UIInput)comp).setValid(false);
            GenUtil.addErrorMessage("Enter password");
        } else if(!input.equals(this.password)){
            ((UIInput)comp).setValid(false);
            GenUtil.addErrorMessage("2 Passwords must be the same ");
        }


    }

    public void saveBtn(ActionEvent e){
        logger.log(INFO, "Save user selected");
        try{
            this.usrMgr.saveUser(user);
            logger.log(INFO, "User saved");
            GenUtil.addInfoMessage("Created User: "+user.getFamilyName());

        }catch(BacException ex){
            logger.log(INFO,"error saving user");
            GenUtil.addErrorMessage("Could not created user because: "+ex.getLocalizedMessage());

        }
        
        


    }


public String redirect(String page, String newPage){
 return page+"?faces-redirect=true"; 
}


}
