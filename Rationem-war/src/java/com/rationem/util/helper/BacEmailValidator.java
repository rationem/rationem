/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rationem.util.helper;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;
import org.apache.commons.validator.routines.EmailValidator;


import java.util.logging.Logger;


/**
 *
 * @author Chris
 */
@FacesValidator("com.rationem.util.helper.emailValidator")
public class BacEmailValidator  implements Validator, ClientValidator {
 private static final Logger logger =
            Logger.getLogger(BacEmailValidator.class.getName());
 private EmailValidator validator = EmailValidator.getInstance();
 
 ResourceBundle validationText = ResourceBundle.getBundle("com.rationem.localisation.ValidationText");
 
 public BacEmailValidator() {
  
 }
 
 @Override
 public void validate(FacesContext context, UIComponent component, Object value)
throws ValidatorException {
  
 String testEmail = (String)value;
 
if(testEmail == null || testEmail.isEmpty()) {
return;
}


if(!validator.isValid(testEmail)) {
throw new ValidatorException(new
FacesMessage(FacesMessage.SEVERITY_ERROR, validationText.getString("emailValidation"),
value + " "+validationText.getString("emailNotValid")));
}
}
 @Override
 public Map<String, Object> getMetadata() {
return null;
}
 @Override
 public String getValidatorId() {
return "com.rationem.util.helper.emailValidator";
}
 
}
